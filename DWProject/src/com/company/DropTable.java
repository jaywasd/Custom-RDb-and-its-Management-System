package com.company;

import java.io.*;

public class DropTable {

  public void dropTableStatement(String query, String path) throws IOException {

    query = query.replace(";", "");
    query = query.replace("'", "");
    String[] query_params = query.split(" ");
    String table_name = query_params[2];

    //Event logs
    FileWriter fileWriter = new FileWriter(path + "/logs_eventlogs.txt", true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    bufferedWriter.write("table " + table_name + " dropped successfully.\n");
    bufferedWriter.close();
    fileWriter.close();

    File f;
    f = new File(path + "/" + table_name + ".txt");

    dropTable(f);
  }

  public void dropTable(File f) throws IOException {

    boolean delete = f.delete();
    if (delete) {
      System.out.println("Table deleted!");
    } else {
      System.out.println("Deletion couldn't be completed");
    }
  }
}