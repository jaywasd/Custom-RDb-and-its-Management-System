package com.company;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class TransactionMain {

  public static void main(String path,String user,String query) {
      String[] query_params=query.split("\\s");

    String table_name=query_params[1];
    String tableBackup="";

    if(query_params[0].equals("update")){
      Update u=new Update();
      Locks lock = Locks.getInstance();
      while (true) {
        if (lock.addLockToTable(table_name)){  //check if the lock is set, if not set then apply lock before option
          break;}
        else {
          System.out.println(Thread.currentThread().getName()+" waiting for the table = " + table_name);
        }
      }
      try {
        u.updateStatement(query,path);  //release the lock.
      } catch (IOException e) {
        System.out.println(e.getMessage());
      } finally {
        lock.removeLockforTable(table_name);
      }
    }
    else if(query_params[0].equals("insert")){
      Insert i=new Insert();
      Locks lock=Locks.getInstance();
      table_name=query_params[2];

      while (true) {
        if (lock.addLockToTable(table_name)){  //check if the lock is set, if not set then apply lock before option
          break;}
        else {
          System.out.println(Thread.currentThread().getName()+" waiting for the table = " + table_name);
        }
      }
      try {
        i.main(query,path);  //release the lock.
      }  catch (Exception e) {
        e.printStackTrace();
      } finally {
        lock.removeLockforTable(table_name);
      }

    }
//    else if(query.equals("STOP TRANSACTION")){
//      Locks locks= Locks.getInstance();
//      locks.removeLockforTable(table_name);
//    }


  }

}
