package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ERD {
  public static void main(String path) throws IOException {

    String[] dbNameArr = path.split("/");
    String dbName = dbNameArr[1];

    ArrayList<String> lines = new ArrayList<>();

    lines.add("Database: " + dbName + "\n");

    lines.add("-------------------------------------*\n");

    FileInputStream fIn1 = new FileInputStream(path + "/DataDictionary.txt");
    Scanner sc1 = new Scanner(fIn1);
    while (sc1.hasNextLine()) {
      String temp = sc1.nextLine();
      if(temp.contains("Table")) {
        lines.add("*****  "+temp+"  *****");
      }
      else if(temp.contains("*")) {
        lines.add("\n-------------------------------------*\n");
      }else {
        lines.add(temp);
      }
    }
    sc1.close();
    fIn1.close();
    FileWriter fileWriter = new FileWriter(path + "/erd.txt");
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    for (String line : lines) {
      bufferedWriter.write(line + "\n");
    }
    bufferedWriter.close();
    System.out.println("ERD File created successfully.\n");
  }
}
