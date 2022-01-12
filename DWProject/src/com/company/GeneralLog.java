package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneralLog {

  public static void main(String path) throws IOException {

    String userdb = path;

    File ftables = new File(userdb);
    String[] tables = ftables.list();

    FileWriter fileWriter = new FileWriter(userdb + "/logs_generallogs.txt");
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

    for (int i = 0; i < tables.length; i++) {

      if (!tables[i].equals("CreateStatements.txt") && !tables[i].equals("DataDictionary.txt")
          && !tables[i].equals("dump.sql") && !tables[i].equals("InsertStatements.txt")
          && !tables[i].equals("logs_querylogs.txt") && !tables[i].equals("logs_eventlogs.txt") &&
          !tables[i].equals("logs_generallogs.txt") && !(tables[i].charAt(0) == '.')) {

        Path p = Paths.get(userdb + "/" + tables[i]);

        long entries = Files.lines(p).count() - 1;
        bufferedWriter.write("tablename: " + tables[i] + "    Number of records: " + entries + "\n");
      }
    }
    bufferedWriter.close();
    System.out.println("General Logs created successfuly !\n");
  }
}