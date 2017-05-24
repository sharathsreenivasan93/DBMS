package simpledb.server;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.remote.*;
import simpledb.tx.recovery.LogFwdRecordIterator;
import simpledb.tx.recovery.LogRecord;
import simpledb.tx.recovery.LogRecordIterator;
import simpledb.tx.recovery.RecoveryMgr;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.Iterator;

public class Startup {
	

   public static void main(String args[]) throws Exception {
      // configure and initialize the database
      SimpleDB.init("sample6");
      
      // create a registry specific for the server on the default port
      Registry reg = LocateRegistry.createRegistry(1099);
      
      // and post the server entry in it
      RemoteDriver d = new RemoteDriverImpl();
      reg.rebind("simpledb", d);
      
      System.out.println("database server ready");
      
      
      
   }
}
