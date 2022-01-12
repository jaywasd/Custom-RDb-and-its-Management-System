package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Select {

  public void selectSatatment(String query,String rpath) throws IOException {

    query = query.replaceAll(";", "");
    String[] query_params = query.split(" ");
    String table_name = query_params[3];

    File f;
    f = new File(rpath+"/"+table_name + ".txt");
    BufferedReader br = new BufferedReader(new FileReader(f));

    // Getting header from the file
    String[] headers = br.readLine().split("\\|");
    for (int i = 0; i < headers.length; i++) {
      headers[i] = headers[i].split("\\(")[0];
    }

    // Getting the columns to display
    String[] columns;
    if (query_params[1].equals("*")) {  //we check if the statement wants all columns and all data.
      columns = headers;  //get the headers feed in the columns
      StringBuilder cols = new StringBuilder();
      for (String str : headers)
        cols.append(str + ",");  //make the String and then send to printOutput with all columns since * is requested.
      query = query.replace("*", cols);  //replace so that we know the columns to base.
    } else {
      columns = query_params[1].split(",");  //if not all of the columns are requested, we get the requested and then send for printing.
    }

    if (!query.contains("where")) {  //if the statement is without Where clause.

      printOutput(br, headers, columns, query);
    } else {
      // when where statement is executed.
      ArrayList<String> condition_coloumns = new ArrayList<>();
      ArrayList<String> operations = new ArrayList<>();
      ArrayList<String> values = new ArrayList<>();

      // Getting the column and there respective values for where condition
      int i = 5;
      do {
        condition_coloumns.add(query_params[i++]);
        operations.add(query_params[i++]);
        values.add(query_params[i++]);

      } while (++i < query_params.length);
      printOutputWithCondition(br, headers, columns, query, condition_coloumns, operations, values);

    }
    System.out.println();
  }

  private void printOutput(BufferedReader br, String[] headers, String[] columns, String query) throws IOException {

    ArrayList<Integer> list = new ArrayList<>();

    // Printing the request header column and adding the respective indexes to list.
    for (int i = 0; i < headers.length; i++) {
      if (query.contains(headers[i])) {
        System.out.printf("%30s",headers[i] + "       |");
        list.add(i);  //adding the required columns needed for printing to the list.
      }
    }
    System.out.println();
    System.out.println("------------------------------------------------------------------------------------------");

    String line = br.readLine();
    // Printing the requested row columns as per the list created above
    while (line != null) {
      String[] row = line.split("\\|");
      for (Integer i : list) {
        System.out.printf("%30s",row[i] + "       |");
      }
      System.out.println();
      line = br.readLine();
    }
  }

  private void printOutputWithCondition(BufferedReader br, String[] headers, String[] columns, String query,
                                        ArrayList<String> condition_coloumns, ArrayList<String> operations, ArrayList<String> values)
      throws IOException {

    ArrayList<Integer> condition_column_indexs = new ArrayList<>();
    ArrayList<Integer> list = new ArrayList<>();

    // Printing the request header column and adding the respective indexes to list.
    for (int i = 0; i < headers.length; i++) {
      for (String temp : condition_coloumns) {
        if (headers[i].equals(temp))
          condition_column_indexs.add(i);
      }
      if (query.contains(headers[i])) {
        System.out.print(headers[i] + " ");
        list.add(i);   //columns that need to be displayed while printing
      }
    }
    System.out.println();

    // Printing the requested row columns as per the list created above
    String line = br.readLine();
    boolean isValid = true;
    while (line != null) { //check the condition columns and the value matching for each line, when found satisfying the where condition, print it.
      String[] row = line.split("\\|");

      if (query.contains(" and ")) {    //we first check here if the select statement has multiple and
        for (int i = 0; i < condition_coloumns.size(); i++) {
          isValid = isValidRow(row, condition_column_indexs.get(i), operations.get(i), values.get(i)); //checks the condition column with the value.
          if (isValid == false)
            break;
        }
      } else {
        if (query.contains(" or ")) {
          for (int i = 0; i < condition_coloumns.size(); i++) {
            isValid = isValidRow(row, condition_column_indexs.get(i), operations.get(i), values.get(i));
            if (isValid == true)
              break;
          }
        } else {
          isValid = isValidRow(row, condition_column_indexs.get(0), operations.get(0), values.get(0));
        }
      }

      if (isValid) {  //if the checking turned out to be true, we print this row.
        for (Integer i : list) {  //list only contains the desired columns we want to print.
          System.out.print(row[i] + " ");
        }
        System.out.println();
      }

      line = br.readLine();
    }

  }

  private boolean isValidRow(String[] row, int condition_column_index, String operation, String value) {

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
