package com.company;

import java.io.*;

public class TruncateTable {

    public void truncateTableStatement(String query, String path) throws IOException {

        query = query.replace(";", "");
        query = query.replace("'", "");
        String[] query_params = query.split(" ");
        String table_name = query_params[2];

        File f;
        f = new File(path + "/" + table_name + ".txt");

        truncateTable(f);
    }

    public void truncateTable(File f) throws IOException {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String firstLine = br.readLine();
        FileWriter fw = new FileWriter(f, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(firstLine);
        bw.close();
        System.out.println("Table Truncated!");
    }
}