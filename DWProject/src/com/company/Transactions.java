package com.company;

import java.io.*;

public class Transactions {

  public static void main(String[] args) throws Exception {
  TransactionMain m=new TransactionMain();
    // For testing purpose run any of the following queries and then confirm the
    // result with the original txt file


    //Runnable is being implemented using Lambda functions.
    Runnable task1 = () -> {  //by default calls the run method.

    File f1=new File("user2/transactions/transaction1.txt");
      BufferedReader br1= null;
      try {
        br1 = new BufferedReader(new FileReader(f1));

      String query="";
      query= br1.readLine();
        query= br1.readLine();
      while(query!=null){


          m.main("user2/transactions", "user2", query);
        query= br1.readLine();
        }

      }catch (Exception e) {

        e.printStackTrace();

      }

    };

    Runnable task2 = () -> {  //by default calls the run method.
      File f=new File("user2/transactions");
      File[] f2=f.listFiles();
      System.out.println(f2[3]);
      File f1=new File("user2/transactions/transaction2.txt");
      BufferedReader br1= null;
      try {
        br1 = new BufferedReader(new FileReader(f1));

        String query="";
        query= br1.readLine();
        query= br1.readLine();
        while(query!=null){


          m.main("user2/transactions", "user2", query);
          query= br1.readLine();
        }

      }catch (Exception e) {

        e.printStackTrace();

      }

    };

    Thread thread1 = new Thread(task1);

    Thread thread2 = new Thread(task2);

    thread1.start();
    thread2.start();



  return;
  }

  private static String getTableFromQuery(String query) {

    return query.split(" ")[1];


  }

}
