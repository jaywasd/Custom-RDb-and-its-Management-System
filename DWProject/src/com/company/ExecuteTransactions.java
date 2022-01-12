package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecuteTransactions {

  public static void main(String path) {
    System.out.println("Transaction Stated! enter your queries one by one");
    StringBuilder sb=new StringBuilder();
    ArrayList<String> lines=new ArrayList<>();
    Scanner sc1 = new Scanner(System.in);
    while (true) {
      String temp = sc1.nextLine();
      if (!temp.contains("rollback") && !temp.contains("commit")) {
        lines.add(temp);
      }
      else if(temp.contains("rollback")){
        System.out.println("Transaction has been aborted!\n");
        return ;
      }
      else if(temp.contains("commit")){
          try {
            execute(lines,path);
          } catch (IOException e) {
            e.printStackTrace();
          }
          System.out.println("Transaction completed!\n");
          return;
      } else {
          System.out.println("Enter a valid query!");
      }
    }
  }

  private static void execute(ArrayList<String> lines, String path) throws IOException {
    for (String line : lines) {
      Instant start = Instant.now();
      String[] dbNameArr = path.split("/");
      String user = dbNameArr[0];
      if (line.toLowerCase().contains("insert")) {
        String[] table = line.split("\\s");
        Insert j = new Insert();
        Locks lock = Locks.getInstance();
        while (true) {
          if (lock.addLockToTable(table[1])) {  //check if the lock is set, if not set then apply lock before option
            break;
          } else {
            System.out.println(Thread.currentThread().getName() + " waiting for the table = " + table[1]);
          }
        }
        try {
          j.main(line, path);  //release the lock.
        } catch (Exception e) {
          System.out.println(e.getMessage());
        } finally {
          lock.removeLockforTable(table[1]);
        }

      } else
        if (line.toLowerCase().contains("create")) {
          Create c = new Create();
          c.main(line, path, user);
        } else
          if (line.toLowerCase().contains("select")) {
            Select s = new Select();
            s.selectSatatment(line, path);
          } else
            if (line.toLowerCase().contains("update")) {
              String[] table = line.split("\\s");
              Update u = new Update();
              Locks lock = Locks.getInstance();
              while (true) {
                if (lock.addLockToTable(table[1])) {  //check if the lock is set, if not set then apply lock before option
                  break;
                } else {
                  System.out.println(Thread.currentThread().getName() + " waiting for the table = " + table[1]);
                }
              }
              try {
                u.updateStatement(line, path);  //release the lock.
              } catch (IOException e) {
                System.out.println(e.getMessage());
              } finally {
                lock.removeLockforTable(table[1]);
              }
              //                  u.updateStatement(query,path);
            } else
              if (line.toLowerCase().contains("delete")) {
                Delete delete = new Delete();
                delete.deleteStatement(line, path);
              } else
                if (line.toLowerCase().contains("drop") && line.toLowerCase().contains("database")) {
                  DropDb drop = new DropDb();
                  drop.dropDbStatement(line, path);
                } else
                  if (line.toLowerCase().contains("drop") && line.toLowerCase().contains("table")) {
                    DropTable dropTable = new DropTable();
                    dropTable.dropTableStatement(line, path);
                  } else
                    if (line.toLowerCase().contains("truncate")) {
                      TruncateTable truncateTable = new TruncateTable();
                      truncateTable.truncateTableStatement(line, path);
                    } else
                      if (line.toLowerCase().contains("create")) {
                        Create c = new Create();
                        c.main(line, path, user);
                      } else
                        if (line.toLowerCase().contains("select")) {
                          Select s = new Select();
                          s.selectSatatment(line, path);
                        } else
                          if (line.toLowerCase().contains("update")) {
                            String[] table = line.split("\\s");
                            Update u = new Update();
                            Locks lock = Locks.getInstance();
                            while (true) {
                              if (lock.addLockToTable(table[1])) {  //check if the lock is set, if not set then apply lock before option
                                break;
                              } else {
                                System.out.println(Thread.currentThread().getName() + " waiting for the table = " + table[1]);
                              }
                            }
                            try {
                              u.updateStatement(line, path);  //release the lock.
                            } catch (IOException e) {
                              System.out.println(e.getMessage());
                            } finally {
                              lock.removeLockforTable(table[1]);
                            }
                          } else
                            if (line.toLowerCase().contains("delete")) {
                              Delete delete = new Delete();
                              delete.deleteStatement(line, path);
                            } else
                              if (line.toLowerCase().contains("drop") && line.toLowerCase().contains("database")) {
                                DropDb drop = new DropDb();
                                drop.dropDbStatement(line, path);
                              } else
                                if (line.toLowerCase().contains("drop") && line.toLowerCase().contains("table")) {
                                  DropTable dropTable = new DropTable();
                                  dropTable.dropTableStatement(line, path);
                                } else
                                  if (line.toLowerCase().contains("truncate")) {
                                    TruncateTable truncateTable = new TruncateTable();
                                    truncateTable.truncateTableStatement(line, path);
                                  }
                              Instant end = Instant.now();
                              Duration timeTaken = Duration.between(start, end);
                              long executionTime = timeTaken.getNano();

                              Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                              FileWriter fileWriter = new FileWriter(path  + "/logs_querylogs.txt",true);
                              BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                              bufferedWriter.write(line + "  ----  " + timestamp + "  ----  " + executionTime + "\n");
                              bufferedWriter.close();
    }
  }


}
