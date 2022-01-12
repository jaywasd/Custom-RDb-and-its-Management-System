package com.company;

import java.io.*;

public class DropDb {

    public void dropDbStatement(String query, String path) throws IOException {

        File f = new File(path);
        if(dropDb(f)) {
            System.out.println("Database Successfully deleted!");
        }
    }

    public boolean dropDb(File f) throws IOException {

        File[] fileList = f.listFiles();
        if (fileList != null) {
            for (File l : fileList) {
                System.out.println("Visiting: " + l);
                dropDb(l);
            }
        }

        if (f.delete()) {
            System.out.printf("Deleted: %s%n", f);
        } else {
            System.err.printf("Unable to delete file or directory: %s%n", f);
            return false;
        }
        return true;
    }
}
