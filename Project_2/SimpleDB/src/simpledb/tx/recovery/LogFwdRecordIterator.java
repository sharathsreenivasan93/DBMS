package simpledb.tx.recovery;

import static simpledb.tx.recovery.LogRecord.*;
import java.util.Iterator;
import simpledb.log.BasicLogRecord;
import simpledb.log.LogIterator;
import simpledb.server.SimpleDB;

/**
 * A class that provides the ability to read records
 * from the log in reverse order.
 * Unlike the similar class 
 * {@link simpledb.log.LogIterator LogIterator},  
 * this class understands the meaning of the log records.
 * @author Edward Sciore
 */
public class LogFwdRecordIterator implements Iterator<LogRecord> {
   private LogIterator iter = SimpleDB.logMgr().forwardIterator();
   
   public boolean hasNext() {
      return iter.hasNextForward();
   }
   
   /**
    * Constructs a log record from the values in the 
    * current basic log record.
    * The method first reads an integer, which denotes
    * the type of the log record.  Based on that type,
    * the method calls the appropriate LogRecord constructor
    * to read the remaining values.
    * @return the next log record, or null if no more records
    */
   public LogRecord next() {
      BasicLogRecord rec = iter.nextForward();
      int op = rec.nextInt();
      switch (op) {
         case CHECKPOINT:
            return new CheckpointRecord(rec);
         case START:
            return new StartRecord(rec);
         case COMMIT:
            return new CommitRecord(rec);
         case ROLLBACK:
            return new RollbackRecord(rec);
         case SETINT:
            return new SetIntRecord(rec);
         case SETSTRING:
            return new SetStringRecord(rec);
         default:
            return null;
      }
   } 
   
   public void remove() {
      throw new UnsupportedOperationException();
   }
}
