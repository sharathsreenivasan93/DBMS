package simpledb.log;

import simpledb.server.SimpleDB;
import simpledb.file.*;
import static simpledb.file.Page.*;
import java.util.*;

/**
 * The low-level log manager. This log manager is responsible for writing log
 * records into a log file. A log record can be any sequence of integer and
 * string values. The log manager does not understand the meaning of these
 * values, which are written and read by the
 * {@link simpledb.tx.recovery.RecoveryMgr recovery manager}.
 * 
 * @author Edward Sciore
 */
public class LogMgr implements Iterable<BasicLogRecord> {
	/**
	 * The location where the pointer to the last integer in the page is. A
	 * value of 0 means that the pointer is the first value in the page.
	 */
	public static final int LAST_POS = 0;
	
	public static int numberOfBlocks;

	private String logfile;
	private Page mypage = new Page();
	private Block currentblk;
	private int currentpos;

	// This is to have a copy of the total size of the objects appended
	private int total_size = 0;

	/**
	 * Creates the manager for the specified log file. If the log file does not
	 * yet exist, it is created with an empty first block. This constructor
	 * depends on a {@link FileMgr} object that it gets from the method
	 * {@link simpledb.server.SimpleDB#fileMgr()}. That object is created during
	 * system initialization. Thus this constructor cannot be called until
	 * {@link simpledb.server.SimpleDB#initFileMgr(String)} is called first.
	 * 
	 * @param logfile
	 *            the name of the log file
	 */
	public LogMgr(String logfile) {
		this.logfile = logfile;
		int logsize = SimpleDB.fileMgr().size(logfile);
		numberOfBlocks = logsize;
		
		if (logsize == 0)
			appendNewBlock();
		else {
			currentblk = new Block(logfile, logsize - 1);
			mypage.read(currentblk);

			currentpos = getLastRecordPosition() + 2*INT_SIZE;
		}
	}

	/**
	 * Ensures that the log records corresponding to the specified LSN has been
	 * written to disk. All earlier log records will also be written to disk.
	 * 
	 * @param lsn
	 *            the LSN of a log record
	 */
	public void flush(int lsn) {
		if (lsn >= currentLSN())
			flush();
	}

	/**
	 * Returns an iterator for the log records, which will be returned in
	 * reverse order starting with the most recent.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public synchronized Iterator<BasicLogRecord> iterator() {
		flush();
		return new LogIterator(currentblk, LAST_POS);
	}
	
	/**
	 * Returns an iterator for the log records, which will be returned in
	 * forward order starting with the most oldest.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public synchronized LogIterator forwardIterator() {
		flush();
		Page temp = new Page();
		Block blk = new Block(logfile, 0);
		temp.read(blk);
		int p = temp.getInt(LogMgr.LAST_POS) + INT_SIZE;
		return new LogIterator(blk, p);
	}
	

	/**
	 * Appends a log record to the file. The record contains an arbitrary array
	 * of strings and integers. The method also writes an integer to the end of
	 * each log record whose value is the offset of the corresponding integer
	 * for the previous log record. These integers allow log records to be read
	 * in reverse order.
	 * 
	 * @param rec
	 *            the list of values
	 * @return the LSN of the final value
	 */
	public synchronized int append(Object[] rec) {
		int recsize = 2*INT_SIZE; 
		
		for (Object obj : rec)
			recsize += size(obj);
		if (currentpos + recsize >= BLOCK_SIZE) { 
			flush(); 
			appendNewBlock();
		}
		for (Object obj : rec)
			appendVal(obj);

		total_size = recsize;
		finalizeRecord();
		return currentLSN();
	}

	/**
	 * Adds the specified value to the page at the position denoted by
	 * currentpos. Then increments currentpos by the size of the value.
	 * 
	 * @param val
	 *            the integer or string to be added to the page
	 */
	private void appendVal(Object val) {
		if (val instanceof String) {
			mypage.setString(currentpos, (String) val);
		} else {
			mypage.setInt(currentpos, (Integer) val);
		}
		currentpos += size(val);
	}

	/**
	 * Calculates the size of the specified integer or string.
	 * 
	 * @param val
	 *            the value
	 * @return the size of the value, in bytes
	 */
	private int size(Object val) {
		if (val instanceof String) {
			String sval = (String) val;
			return STR_SIZE(sval.length());
		} else
			return INT_SIZE;
	}

	/**
	 * Returns the LSN of the most recent log record. As implemented, the LSN is
	 * the block number where the record is stored. Thus every log record in a
	 * block has the same LSN.
	 * 
	 * @return the LSN of the most recent log record
	 */
	private int currentLSN() {
		return currentblk.number();
	}

	/**
	 * Writes the current page to the log file.
	 */
	private void flush() {
		mypage.write(currentblk);
	}

	/**
	 * Clear the current page, and append it to the log file.
	 */
	private void appendNewBlock() {
		
		setLastRecordPosition(0);
		mypage.setInt(total_size, INT_SIZE);
		
		currentpos = 2*INT_SIZE;
		currentblk = mypage.append(logfile);
	}

	/**
	 * Sets up a circular chain of pointers to the records in the page. There is
	 * an integer added to the end of each log record whose value is the offset
	 * of the previous log record. The first four bytes of the page contain an
	 * integer whose value is the offset of the integer for the last log record
	 * in the page.
	 */
	private void finalizeRecord() {
		
		mypage.setInt(currentpos - total_size + INT_SIZE, currentpos + INT_SIZE);
		mypage.setInt(currentpos, getLastRecordPosition());
		mypage.setInt(currentpos + INT_SIZE, total_size);
		setLastRecordPosition(currentpos);
		currentpos += 2*INT_SIZE;
	}

	private int getLastRecordPosition() {
		return mypage.getInt(LAST_POS);
	}

	private void setLastRecordPosition(int pos) {
		mypage.setInt(LAST_POS, pos);
	}
}