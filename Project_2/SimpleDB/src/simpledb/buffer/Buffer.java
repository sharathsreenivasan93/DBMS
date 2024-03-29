package simpledb.buffer;

import simpledb.server.SimpleDB;
import simpledb.file.*;

/**
 * An individual buffer.
 * A buffer wraps a page and stores information about its status,
 * such as the disk block associated with the page,
 * the number of times the block has been pinned,
 * whether the contents of the page have been modified,
 * and if so, the id of the modifying transaction and
 * the LSN of the corresponding log record.
 * @author Edward Sciore
 */
public class Buffer {
   private Page contents = new Page();
   public Block blk = null;
   private int pins = 0;
   private int modifiedBy = -1;  // negative means not modified
   private int logSequenceNumber = -1; // negative means no corresponding log record
   private int readCount=0;
   private int writeCount=0;
   private int flushCount=0;
   private int pinCount=0;
   private int unpinCount=0;
   private int blockCount=0;
   private int newCount=0;


   /**
    * Creates a new buffer, wrapping a new 
    * {@link simpledb.file.Page page}.  
    * This constructor is called exclusively by the 
    * class {@link BasicBufferMgr}.   
    * It depends on  the 
    * {@link simpledb.log.LogMgr LogMgr} object 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * That object is created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    */
   public Buffer() {}
   
   /**
    * Returns the integer value at the specified offset of the
    * buffer's page.
    * If an integer was not stored at that location,
    * the behavior of the method is unpredictable.
    * @param offset the byte offset of the page
    * @return the integer value at that offset
    */
   public int getInt(int offset) {
	   //incReadCount();
      return contents.getInt(offset);
   }

   /**
    * Returns the string value at the specified offset of the
    * buffer's page.
    * If a string was not stored at that location,
    * the behavior of the method is unpredictable.
    * @param offset the byte offset of the page
    * @return the string value at that offset
    */
   public String getString(int offset) {
	   //incReadCount();
      return contents.getString(offset);
   }

   /**
    * Writes an integer to the specified offset of the
    * buffer's page.
    * This method assumes that the transaction has already
    * written an appropriate log record.
    * The buffer saves the id of the transaction
    * and the LSN of the log record.
    * A negative lsn value indicates that a log record
    * was not necessary.
    * @param offset the byte offset within the page
    * @param val the new integer value to be written
    * @param txnum the id of the transaction performing the modification
    * @param lsn the LSN of the corresponding log record
    */
   public void setInt(int offset, int val, int txnum, int lsn) {
      modifiedBy = txnum;
      if (lsn >= 0)
	      logSequenceNumber = lsn;
      contents.setInt(offset, val);
      //incWriteCount();
   }

   /**
    * Writes a string to the specified offset of the
    * buffer's page.
    * This method assumes that the transaction has already
    * written an appropriate log record.
    * A negative lsn value indicates that a log record
    * was not necessary.
    * The buffer saves the id of the transaction
    * and the LSN of the log record.
    * @param offset the byte offset within the page
    * @param val the new string value to be written
    * @param txnum the id of the transaction performing the modification
    * @param lsn the LSN of the corresponding log record
    */
   public void setString(int offset, String val, int txnum, int lsn) {
      modifiedBy = txnum;
      if (lsn >= 0)
	      logSequenceNumber = lsn;
      contents.setString(offset, val);
      //incWriteCount();
   }

   /**
    * Returns a reference to the disk block
    * that the buffer is pinned to.
    * @return a reference to a disk block
    */
   public Block block() {
      return blk;
   }

   /**
    * Writes the page to its disk block if the
    * page is dirty.
    * The method ensures that the corresponding log
    * record has been written to disk prior to writing
    * the page to disk.
    */
   void flush() {
      if (modifiedBy >= 0) {
         SimpleDB.logMgr().flush(logSequenceNumber);
         contents.write(blk);
         modifiedBy = -1;
         //incFlushCount();
      }
   }

   /**
    * Increases the buffer's pin count.
    */
   void pin() {
      pins++;
      //incPinCount();
   }

   /**
    * Decreases the buffer's pin count.
    */
   void unpin() {
      pins--;
      //incUnpinCount();
   }

   /**
    * Returns true if the buffer is currently pinned
    * (that is, if it has a nonzero pin count).
    * @return true if the buffer is pinned
    */
   boolean isPinned() 
   {
      return pins > 0;
   }

   /**
    * Returns true if the buffer is dirty
    * due to a modification by the specified transaction.
    * @param txnum the id of the transaction
    * @return true if the transaction modified the buffer
    */
   boolean isModifiedBy(int txnum) 
   {
      return txnum == modifiedBy;
   }

   /**
    * Reads the contents of the specified block into
    * the buffer's page.
    * If the buffer was dirty, then the contents
    * of the previous page are first written to disk.
    * @param b a reference to the data block
    */
   void assignToBlock(Block b) 
   {
      flush();
      blk = b;
      contents.read(blk);
      pins = 0;
      //incBlockCount();
   }

   /**
    * Initializes the buffer's page according to the specified formatter,
    * and appends the page to the specified file.
    * If the buffer was dirty, then the contents
    * of the previous page are first written to disk.
    * @param filename the name of the file
    * @param fmtr a page formatter, used to initialize the page
    */
   void assignToNew(String filename, PageFormatter fmtr) 
   {
      flush();
      fmtr.format(contents);
      blk = contents.append(filename);
      pins = 0;
      //incNewCount();
   }
   
   public void setLogSequenceNumber(int logSequenceNumber) 
   {
	this.logSequenceNumber = logSequenceNumber;
   }

	public int getBlockCount() 
	{
		return blockCount;
	}
	
	public void incBlockCount() 
	{
		this.blockCount++;
	}
	
	public int getNewCount() 
	{
		return newCount;
	}
	
	public void incNewCount() 
	{
		this.newCount++;
	}
	
	public int getPinCount() 
	{
		return pinCount;
	}
	
	public void incPinCount() 
	{
		this.pinCount++;
	}
	
	public int getUnpinCount() 
	{
		return unpinCount;
	}
	
	public void incUnpinCount() {
		this.unpinCount++;
	}
	
	public int getReadCount() 
	{
		return readCount;
	}
	
	public void incReadCount() 
	{
		this.readCount++;
	}
	
	public int getWriteCount() 
	{
		return writeCount;
	}
	
	public void incWriteCount() 
	{
		this.writeCount++;
	}
	
	public int getFlushCount() 
	{
		return flushCount;
	}
	
	public void incFlushCount() 
	{
		this.flushCount++;
	}
	
	public int getLogSequenceNumber() 
	{
		return logSequenceNumber;
	}
	
	public int getModifiedBy() 
	{
		return modifiedBy;
	}

}