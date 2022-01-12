package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Main {

    public static void main(String path,String user,String q) throws Exception {
    boolean loggedIn = true;
    while(loggedIn) {
      System.out.println("Enter the schema or 0 to Logout: ");
      Scanner sc2 = new Scanner(System.in);
      String queryDb = sc2.nextLine();
        if (queryDb.equals("0")){
            loggedIn = false;
            continue;
        }
        String schema="";
        if(queryDb.contains("use")){
      String[] schemaStatement = queryDb.split("\\s");
      schema = schemaStatement[1].replace(";","");}
        else if(queryDb.toLowerCase().contains("create database")){
          String[] createSchema=queryDb.split("\\s");
          schema=createSchema[2];
          schema=schema.substring(0,schema.length()-1);
          File f=new File(path+"/"+schema);
          f.mkdir();

        }
      path = path + "/" + schema;
      boolean switchLoop = true;
            while(switchLoop){
                System.out.println("1. Enter your query\n2. Generate Sql " +
                    "dump\n" + "3. ERD\n" +
                        "4. Generate General logs\n5. To run a transaction\n6. Run Transaction Simulation" +
                        "\n0. Exit\nEnter your " +
                    "selection:");
                Scanner sc3=new Scanner(System.in);
                String option=sc3.nextLine();
                switch (option){
                    case "1":
                        boolean queryLoop = true;
                        while(queryLoop){
                        System.out.println("Please enter your query, or enter 0 to exit");
                        Instant start = Instant.now();
                        Scanner sc=new Scanner(System.in);
                        String query=sc.nextLine();
                        if (query.equals("0")){
                            queryLoop = false;
                            continue;
                        }
                        String[] q_param=query.split("\\s");
                        String query_type=q_param[0];
                        if(query_type.toLowerCase().equals("insert")){
                          String[] table=query.split("\\s");
                          Insert i=new Insert();
                          Locks lock = Locks.getInstance();
                          while (true) {
                            if (lock.addLockToTable(table[1])){  //check if the lock is set, if not set then apply lock before option
                              break;}
                            else {
                              System.out.println(Thread.currentThread().getName()+" waiting for the table = " + table[1]);
                            }
                          }
                          try {
                            i.main(query,path);  //release the lock.
                          } catch (IOException e) {
                            System.out.println(e.getMessage());
                          } finally {
                            lock.removeLockforTable(table[1]);
                          }

                        }
                        else if(query_type.toLowerCase().equals("create")){
                          Create c=new Create();
                          c.main(query,path,user);
                        }
                        else if(query_type.toLowerCase().equals("select")){
                          Select s=new Select();
                          s.selectSatatment(query,path);
                        }
                        else if(query_type.toLowerCase().equals("update")){
                          String[] table=query.split("\\s");
                          Update u=new Update();
                          Locks lock = Locks.getInstance();
                          while (true) {
                            if (lock.addLockToTable(table[1])){  //check if the lock is set, if not set then apply lock before option
                              break;}
                            else {
                              System.out.println(Thread.currentThread().getName()+" waiting for the table = " + table[1]);
                            }
                          }
                          try {
                            u.updateStatement(query,path);  //release the lock.
                          } catch (IOException e) {
                            System.out.println(e.getMessage());
                          } finally {
                            lock.removeLockforTable(table[1]);
                          }
        //                  u.updateStatement(query,path);
                        }
                        else if(query_type.toLowerCase().equals("delete")){
                            Delete delete=new Delete();
                            delete.deleteStatement(query,path);
                        }
                        else if(query_type.toLowerCase().equals("drop") && query.toLowerCase().contains("database")){
                            DropDb drop=new DropDb();
                            drop.dropDbStatement(query, path);
                        }
                        else if(query_type.toLowerCase().equals("drop") && query.toLowerCase().contains("table")){
                            DropTable dropTable = new DropTable();
                            dropTable.dropTableStatement(query, path);
                        }
                        else if(query_type.toLowerCase().equals("truncate")){
                            TruncateTable truncateTable = new TruncateTable();
                            truncateTable.truncateTableStatement(query, path);
                        } else {
                            System.out.println("Please enter a valid query!");
                            continue;
                        }
                          Instant end = Instant.now();
                          Duration timeTaken = Duration.between(start, end);
                          long executionTime = timeTaken.getNano();

                          Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                          FileWriter fileWriter = new FileWriter(path  + "/logs_querylogs.txt",true);
                          BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                          bufferedWriter.write(query + "  ----  " + timestamp + "  ----  " + executionTime + "\n");
                          bufferedWriter.close();
                      }
                      break;
                    case "2":
                        Dump d = new Dump();
                        d.takeSqlDump(path);
                        break;
                    case "3":
                      ERD erd = new ERD();
                      erd.main(path);
                      break;
                    case "4":
                        GeneralLog g = new GeneralLog();
                        g.main(path);
                        break;
                    case "5":
                        ExecuteTransactions e1=new ExecuteTransactions();
                        e1.main(path);
                        break;
                    case "6":
                        Transactions t=new Transactions();
                        t.main(null);
                        break;
                    case "0":
                        switchLoop = false;
                        break;
                    default:
                        System.out.println("PLease Enter a Valid Input");
                        break;
                }
            }
        path = path.replace(schema, "");
    }
    }
}
