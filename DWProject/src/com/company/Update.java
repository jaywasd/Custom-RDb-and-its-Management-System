package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Update {
  File f;
  public void updateStatement(String query,String rpath) throws IOException {

    query = query.replace(";", "");
    String[] query_params = query.split(" ");
    String table_name = query_params[1];

//    File f;
    f = new File(rpath+"/"+table_name + ".txt");
    BufferedReader br = new BufferedReader(new FileReader(f));

    // Getting the columns names and their respective values for updation
    ArrayList<String> set_columns = new ArrayList<>();
    ArrayList<String> set_values = new ArrayList<>();
    int i = 3;
    while (!query_params[i].equals("where")) {  //the while loop here looks for multiple columns to update till we reach the where clause.

        set_columns.add(query_params[i++]);
        i++;
        set_values.add(query_params[i++].replace(",", ""));

    }

    // Getting the column and there respective values for where condition
    i++;
    ArrayList<String> condition_coloumns = new ArrayList<>();
    ArrayList<String> operations = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();

    do {
      condition_coloumns.add(query_params[i++]);
      operations.add(query_params[i++]);
      values.add(query_params[i++]);
    } while (++i < query_params.length);
    updateFile(br, query, set_columns, set_values, condition_coloumns, operations, values,rpath);


    System.out.println("Updates have been applied on "+f.getName()+" "+Thread.currentThread().getName());

  }

  private void updateFile(BufferedReader br, String query, ArrayList<String> set_columns,
                          ArrayList<String> set_values, ArrayList<String> condition_coloumns, ArrayList<String> operations,
                          ArrayList<String> values,String rpath) throws IOException {

    String line = br.readLine();
    String[] headers = line.split("\\|");
    for (int i = 0; i < headers.length; i++) {
      headers[i] = headers[i].split("\\(")[0];
    }

//    String path = "./temp.txt";
    String path=rpath+"/temp.txt"; //temprory file to put records.
    File f2 = new File(path);
    FileWriter fw = new FileWriter(f2, true);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(line + "\n");

    // read from br and validate and insert into bw
    ArrayList<Integer> condition_column_indexes = new ArrayList<>();
    ArrayList<Integer> set_columns_indexes = new ArrayList<>();

    // Generating the index for the columns where condition need to be
    // checked(condition_column_indexes) and where new values needs to be
    // set(set_columns_indexes)
    for (int i = 0; i < headers.length; i++) {
      for (String temp : condition_coloumns) {
        if (headers[i].equals(temp))
          condition_column_indexes.add(i); //check here
      }

      for (String str : set_columns) {
        if (headers[i].equals(str))
          set_columns_indexes.add(i);
      }
    }

    line = br.readLine();
    boolean isValid = true;
    while (line != null) {
      String[] row = line.split("\\|");

      if (query.contains("and")) {
        for (int i = 0; i < condition_column_indexes.size(); i++) {
          isValid = isValidRow(row, condition_column_indexes.get(i), operations.get(i), values.get(i));
          if (isValid == false)
            break;
        }
      } else {
        if (query.contains("or")) {
          for (int i = 0; i < condition_column_indexes.size(); i++) {
            isValid = isValidRow(row, condition_column_indexes.get(i), operations.get(i), values.get(i));
            if (isValid == true)
              break;
          }
        } else {
          isValid = isValidRow(row, condition_column_indexes.get(0), operations.get(0), values.get(0));
        }
      }
      String oldLine=line;
      line = br.readLine();
      if (isValid) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
          if (set_columns_indexes.contains(i))
            data.append(set_values.get(set_columns_indexes.indexOf(i)) + "|");
          else
            data.append(row[i] + "|");
        }
        data.deleteCharAt(data.length() - 1);
        if(line!=null) {
          bw.write(data + "\n");
        }
        else {
          bw.write(data.toString());
        }
      } else {
        if (line != null) {
          bw.write(oldLine + "\n");
        }
      else {
          bw.write(oldLine);
        }
      }




    }

    bw.close();
    br.close();
    f.delete();  //deleting the old file.
    f2.renameTo(f);  //renaming this file as the old file.



  }

  private boolean isValidRow(String[] row, Integer condition_column_index, String operation, String value) {
    switch (operation) {
      case "=":
        if (row[condition_column_index].equals(value))
          return true;
        else
          return false;
      case "!=":
        if (row[condition_column_index].equals(value))
          return false;
        else
          return true;
      case "<":
        if (Integer.valueOf(row[condition_column_index]) < Integer.valueOf(value))
          return true;
        else
          return false;
      case ">":
        if (Integer.valueOf(row[condition_column_index]) > Integer.valueOf(value))
          return true;
        else
          return false;
      case ">=":
        if (Integer.valueOf(row[condition_column_index]) >= Integer.valueOf(value))
          return true;
        else
          return false;
      case "<=":
        if (Integer.valueOf(row[condition_column_index]) <= Integer.valueOf(value))
          return true;
        else
          return false;
    }

    return false;
  }


}
