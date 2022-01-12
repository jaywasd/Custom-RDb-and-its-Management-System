package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Dump {

    public void takeSqlDump(String path) throws IOException {

        String userdb = path;

        File ftables = new File(userdb);
        String[] tables = ftables.list();
        File create = new File(userdb + "/CreateStatements.txt");
        ArrayList<String> lines = new ArrayList<>();
        String[] dbName = userdb.split("/");
        lines.add("CREATE DATABASE  IF NOT EXISTS " + "`" + dbName[1] + "`;");
        lines.add("USE " + "`" + dbName[1] + "`;\n");
        FileInputStream fIn1 = new FileInputStream(create);
        Scanner sc1 = new Scanner(fIn1);
        while (sc1.hasNextLine()) {
            String temp = sc1.nextLine();
            String[] temp1 = temp.split("\\(");
            String[] temp2 = temp1[0].toLowerCase().split("table");
            lines.add("DROP TABLE IF EXISTS `"+temp2[1].trim()+"`;");
            lines.add(temp+"\n");
        }
        sc1.close();
        fIn1.close();

        for (int i = 0; i < tables.length; i++) {
            if (!tables[i].equals("CreateStatements.txt") && !tables[i].equals("DataDictionary.txt")
                    && !tables[i].equals("dump.sql") && !tables[i].equals("InsertStatements.txt") && !tables[i].equals("erd.txt")
                    && !tables[i].equals("logs_querylogs.txt") && !tables[i].equals("logs_eventlogs.txt") &&
                    !tables[i].equals("logs_generallogs.txt") && !(tables[i].charAt(0) == '.')) {

                lines.add("LOCK TABLES `"+tables[i].substring(0, tables[i].length()-4)+"` WRITE;");

                String insert = "insert into "
                        + tables[i].substring(0, tables[i].length()-4) + " (";

                File table = new File(userdb + "/" + tables[i]);
                FileReader fr = new FileReader(table);
                BufferedReader metadata = new BufferedReader(fr);
                String scMeta = metadata.readLine();
                String[] metaArr = scMeta.split("\\|");
                metadata.close();
                String middle = "";
                for (int j=0; j<metaArr.length; j++){
                    String[] colName = metaArr[j].split("\\(");
                    if(j != metaArr.length-1) {
                        middle = middle + colName[0] + ", ";
                    } else {
                        middle = middle + colName[0] + ") values(";
                    }
                }

                FileInputStream fIn2 = new FileInputStream(table);
                Scanner sc2 = new Scanner(fIn2);
                int count = 0;
                while (sc2.hasNextLine()) {
                    String end = "";
                    String temp = sc2.nextLine();
                    if(count>0){
                        String[] values = temp.split("\\|");
                        for(int k=0; k<values.length; k++){
                            if(metaArr[k].toLowerCase().contains("var")){
                                if(k != values.length-1){
                                    end = end + "'" + values[k] + "', ";
                                } else {
                                    end = end + "'" + values[k] + "');";
                                }
                            } else {
                                if(k != values.length-1){
                                    end = end + values[k] + ", ";
                                } else {
                                    end = end + values[k] + ");";
                                }
                            }
                        }
                        lines.add(insert+middle+end);
                    }
                    count++;
                }
                sc2.close();
                fIn2.close();
                lines.add("UNLOCK TABLES;\n");
            }
        }
        FileWriter fileWriter = new FileWriter(userdb + "/dump.sql");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (String line : lines) {
            bufferedWriter.write(line + "\n");
        }
        bufferedWriter.close();
        System.out.println("Sql dump file for the database has been Generated!");
    }
}
