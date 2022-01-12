package com.company;

import java.io.*;
import java.util.Scanner;

public class Create {

  public static void main(String s, String rpath, String user) throws IOException {

    boolean pk = false;
    boolean fk = false;
    String fkCol = "";
    String pkCol = "";
    String fktablefield = "";
    String fktable = "";
    Scanner sc = new Scanner(System.in);

    String[] st = s.split(" \\(");

    String[] words = st[0].split("\\s");

    st[1] = st[1].substring(0, st[1].length() - 2);

    String[] columns = st[1].split(",");
    for (int i = 0; i < columns.length; i++) {
      if (columns[i].contains("PRIMARY KEY") || columns[i].contains("primary " + "key")) {
        columns[i] = columns[i].replace("PRIMARY KEY", "");
        pk = true; //this table has a primary key
        pkCol = columns[i];
      } else if (columns[i].contains("foreign key") || columns[i].contains("FOREIGN KEY")) {
        String[] a = columns[i].split(" REFERENCES ");
        String[] b = a[1].split("\\(");
        fktable = b[0].substring(0, b[0].length());
        b[1] = b[1].replace(")", "");
        fktablefield = b[1];

        columns[i] = a[0];
        columns[i] = columns[i].replace("FOREIGN KEY", "");
        columns[i] = columns[i].replace("foreign key", "");
        fk = true; //this table has a foreign key

        columns[i] = columns[i].replace("(", "");
        columns[i] = columns[i].replace(")", "");
        columns[i] = columns[i].trim();

        fkCol = columns[i];
      }
      columns[i] = columns[i].trim();
    }

    String[][] columnNames = new String[columns.length][columns.length];

    for (int i = 0; i < columns.length; i++) {
      columns[i] = columns[i].trim();
      String[] temp1 = columns[i].split("\\s");
      if (temp1.length > 1) {
        columnNames[i][0] = temp1[0];
        columnNames[i][1] = temp1[1];
      }
    }


    if (words[0].toLowerCase().equals("create")) {

      String absolutePath = rpath + "/";

      if (words[1].toLowerCase().equals("table")) {

        String path_table = absolutePath + words[2];
        String tableName = words[2];

        //Event logs
        FileWriter fileWriter = new FileWriter(absolutePath +
            "/logs_eventlogs.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("table " + tableName + " created successfully.\n");
        bufferedWriter.close();
        fileWriter.close();

        String path = absolutePath + tableName + ".txt";
        File f = new File(path);
        PrintStream out = System.out;
        PrintStream ps = new PrintStream(path);
        System.setOut(ps);

        for (int i = 0; i < columns.length; i++) {

          String[] temp1 = columns[i].split("\\s");
          if (temp1.length > 1) {
            columnNames[i][0] = temp1[0];
            columnNames[i][1] = temp1[1];
            System.out.print(columnNames[i][0] + "(" + columnNames[i][1] + "|");
          }
        }
        System.setOut(out);
        File position = new File("./" + rpath);
        File[] all = position.listFiles();
        boolean fileExist = false;
        for (int i = 0; i < all.length; i++) {
          if (all[i].getName().equals("CreateStatements")) {
            fileExist = true;
            break;
          }
        }
        String createStatementDump = rpath + "/CreateStatements.txt";
        FileWriter fw = new FileWriter(createStatementDump, true);
        if (fileExist == false) {

          BufferedWriter bw = new BufferedWriter(fw);
          bw.append(s + "\n");
          bw.close();
        } else {
          BufferedWriter bw = new BufferedWriter(fw);
          bw.append(s + "\n");
          bw.close();
        }

        FileWriter db_data_dict = new FileWriter(rpath + "/DataDictionary.txt", true);
        BufferedWriter bw3 = new BufferedWriter(db_data_dict);

        String columnsString = "";
        for (int i = 0; i < columns.length; i++) {
          if (columnNames[i][0] != null) {
            columnsString += columnNames[i][0] + ",";
          }
        }

        columnsString = columnsString.substring(0, columnsString.length() - 1);
        if (pk == true && fk == true) {
          bw3.append("Table: " + tableName + "\nColumns: " + columnsString + "\nPrimary Key: " + pkCol + "\nForeign Key: " + fkCol + "Foreign Key table name: " + fktable + "\nForeign Key table field: " + fktablefield + "\n*\n");

        } else if (pk == true && fk == false) {
          bw3.append("Table: " + tableName + "\nColumns: " + columnsString + "\nPrimary Key: " + pkCol + "\n*\n");

        } else if (pk == false && fk == true) {
          bw3.append("Table: " + tableName + "\nColumns: " + columnsString + "\nForeign Key: " + fkCol + "\nForeign Key table name: " + fktable + "\nForeign Key table field: " + fktablefield + "\n*\n");
        } else {
          bw3.append("Table: " + tableName + "\nColumns: " + columnsString + "\n*\n");

        }
        bw3.close();

      }
    }
  }
}
//create table trial(Id int,ph Varchar);
//create table Persons (ID int  PRIMARY KEY, LastName varchar, FirstName varchar, Age int);
//create table Persons (ID int  PRIMARY KEY, LastName varchar(20), FirstName varchar(20), Age int);
//insert into Persons(ID,LastName,FirstName,Age) values(890,'Jaggi', 'Ridam', 25);
//insert into Persons(ID,LastName,FirstName,Age) values(890,'Sandhu','Ravinder',23);