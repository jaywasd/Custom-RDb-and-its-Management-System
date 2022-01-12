package com.company;

import java.io.*;
import java.util.Scanner;

public class Insert {

  public static void main(String s,String rpath) throws Exception {


    String[] values = s.split("values");
    String value = values[1];
    value = value.replace("(", "");
    value = value.replace(");", "");
    value = value.replace(",", "");
//    System.out.println(value);

    String[] entries = value.split("\\s");


    entries[1] = entries[1].replaceAll("'", "");

    String[] insertQuery = s.split("\\s");

    String[] tableName = insertQuery[2].split("\\(");


    String path = "./"+rpath+ "/"+tableName[0] + ".txt";

    File f = new File(path);
    FileWriter fw = new FileWriter(f, true);
    BufferedWriter bw = new BufferedWriter(fw);

    StringBuilder toTable=new StringBuilder();
    for(int i=0;i<entries.length;i++){

      entries[i]=entries[i].replace("'","");
      toTable.append(entries[i]);
      toTable.append("|");

    }
    String inp=toTable.substring(0,toTable.toString().length()-1);
    bw.write("\n"+inp);
    bw.close();

    File position=new File("./"+rpath);
    File[] all=position.listFiles();
    boolean fileExist=false;
    for(int i=0;i<all.length;i++){
      if(all[i].getName().equals("InsertStatements")){
        fileExist=true;
        break;
      }
    }
    String createStatementDump=rpath+"/InsertStatements.txt";
    FileWriter fw2 = new FileWriter(createStatementDump, true);
    if(fileExist==false){

      BufferedWriter bw2 = new BufferedWriter(fw2);
      bw2.append(s+"\n");
      bw2.close();
    }
    else{
      BufferedWriter bw2=new BufferedWriter(fw2);
      bw2.append(s+"\n");
      bw2.close();
    }

  }
}
//insert into Persons(ID,LastName,FirstName,Age) values(890, 'Jaggi', 'Ridam', 25);
//insert into Persons(ID,LastName,FirstName,Age) values(899, 'Sandhu', 'Ravinder', 22);