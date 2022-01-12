package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Delete {

    File f;
    public void deleteStatement(String query, String rpath) throws IOException {

        query = query.replace(";", "");
        query = query.replace("'", "");
        String[] query_params = query.split(" ");
        String table_name = query_params[2];

        f = new File(rpath+"/"+table_name + ".txt");

        if (query.contains("where")) {
            deleteRow(query, f);
        } else {
            deleteTableContent(f);
        }
        return;
    }

    public void deleteTableContent(File f) throws IOException {
        FileWriter fw = new FileWriter(f, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("");
        fw.close();
        bw.close();
        System.out.println("Table content deleted");
    }

    public void deleteRow(String query, File f) throws IOException {
        FileInputStream fIn = new FileInputStream(f);
        ArrayList<String> lines = new ArrayList<>();
        String[] value = query.split("=");
        value[1] = value[1].trim();
        Scanner sc1 = new Scanner(fIn);
        while (sc1.hasNextLine()) {
            String temp = sc1.nextLine();
            if (!temp.contains(value[1])) {
                lines.add(temp);
            }
        }
        sc1.close();
        fIn.close();
        FileWriter fw = new FileWriter(f, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("");
        fw.close();
        bw.close();
        FileWriter fw2 = new FileWriter(f, true);
        BufferedWriter bw2 = new BufferedWriter(fw2);
        for (int i = 0; i < lines.size(); i++) {
            if (i == lines.size() - 1) {
                bw2.write(lines.get(i));
            } else {
                bw2.write(lines.get(i) + "\n");
            }
        }
        bw2.close();
        System.out.println("Table Row deleted");
    }
}
