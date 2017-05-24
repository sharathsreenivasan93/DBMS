package simpledb.buffer;

import simpledb.file.*;
import java.util.*;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
public class BasicBufferMgr {
   private int numAvailable;
   private int count_buffers;
   
   private LinkedHashMap<Block, Buffer> bufferPoolMap;
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   public BasicBufferMgr(int numbuffs) {
	  System.out.println("number of buffers = "+numbuffs+"\n"); 
      numAvailable = numbuffs;
      count_buffers = numbuffs;
      bufferPoolMap = new LinkedHashMap<Block, Buffer>();
      
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   
   
   
   public int getNumAvailable()
   {
	   return numAvailable;
   }
   
   public LinkedHashMap<Block, Buffer> getBufferPoolMap()
   {
	   return bufferPoolMap;
   }
   
   synchronized void flushAll(int txnum) {
	  Iterator<Buffer> it = bufferPoolMap.values().iterator();
      while (it.hasNext()){
    	  Buffer buff = it.next();
         if (buff.isModifiedBy(txnum))
         buff.flush();
      }
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) {
	  Buffer buff = findExistingBuffer(blk);
      if (buff == null) {
         buff = chooseUnpinnedBuffer();
         if (buff == null)
            return null;
         if(bufferPoolMap.containsKey(buff.block()))
        	 bufferPoolMap.remove(buff.block());
         buff.assignToBlock(blk);
      }
      if(!buff.isPinned())
         numAvailable--;
      bufferPoolMap.put(blk,buff);
      buff.pin();
      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer();
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
      numAvailable--;
      bufferPoolMap.put(buff.block(),buff);
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
      {   numAvailable++;
      	  //bufferPoolMap.remove(buff.block());
      	  
      }
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private Buffer findExistingBuffer(Block blk) {
      
	   /* for (Buffer buff : bufferpool) {
         Block b = buff.block();
         if (b != null && b.equals(blk))
            return buff;
      }
      return null; */
	   
	   return bufferPoolMap.get(blk);
	   	   
   }
   
   public Buffer chooseUnpinnedBuffer() {
	   
	   if(bufferPoolMap.size() < count_buffers)
		   return new Buffer();
      
	   //if(numAvailable > 0)
	   { Buffer tmpBuff = null;
	   	 Block  tmpBlk = null;
	   	 
	     // Get the value which was inserted first into the hash map.
	   	 //tmpBuff = bufferPoolMap.entrySet().iterator().next().getValue();
	   
	   Iterator<Block> it = bufferPoolMap.keySet().iterator();		
	   while(it.hasNext())
	   		{
	   			tmpBlk = it.next();
	   			tmpBuff = bufferPoolMap.get(tmpBlk);
	   			System.out.println("Inside iterator "+tmpBlk.number());
	   		    if(!tmpBuff.isPinned())
	   		    {	
	   		    	//bufferPoolMap.remove(tmpBlk);
	   		    	return tmpBuff;  // Returns the unpinned buffer using FIFO.
	   		    }
	   		}
         
	   }
      return null; 
   }
   
   public Buffer getBuffer(Block blk)
   {
	 return bufferPoolMap.get(blk);   
   }
   
   boolean containsMapping(Block blk) 
   {
		return bufferPoolMap.containsKey(blk);
   }
   
   Buffer getMapping(Block blk) 
   {
		return bufferPoolMap.get(blk);
   }
   
     
   public void iterateMap() 
   {
		System.out.println("\n\nIterating over Buffer Pool Map : \n");
		for (Map.Entry<Block, Buffer> entry : bufferPoolMap.entrySet()) 
		{
		    System.out.println("Block = " + entry.getKey() + ", Buffer = " + entry.getValue());
		}
	}

}

