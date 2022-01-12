package com.company;

import java.io.*;
import java.util.Scanner;

public class Login {



  public static void main(String[] args) {
    boolean invalid = true;
    while (invalid) {
      try {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();

        System.out.print("Enter password: ");
        String pass = sc.nextLine().trim();
        String password = cipher(pass, 4);

        FileInputStream fis = new FileInputStream("passwords.txt");
        Scanner s = new Scanner(fis);
        String[] passwords = new String[4];
        int i = 0;
        while (s.hasNextLine()) {
          passwords[i] = s.nextLine();
          i += 1;
        }
        s.close();

        if (username.equals("user1") && password.equals(passwords[0])) {

          System.out.println("What is your birthplace?");
          String securityQuestion = sc.nextLine().trim();
          String sq = cipher(securityQuestion, 4);

          if (sq.equals(passwords[1])) {
            System.out.println(username + " is logged in.");


            Main m = new Main();
            try {
              m.main("user1", "user1", null);
              invalid = false;
            } catch (Exception e) {
              System.out.println(e.getMessage());
              continue;
            }
          } else {

            System.out.println("invalid details, please try again");
            continue;
          }
        } else
          if (username.equals("user2") && password.equals(passwords[2])) {

            System.out.println("What is your birthplace?");
            String securityQuestion = sc.nextLine().trim();
            String sq = cipher(securityQuestion, 4);
            if (sq.equals(passwords[3])) {

              System.out.println(username + " is logged in.");


              Main m = new Main();
              try {
                m.main("user2", "user2", null);
                invalid = false;
              } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
              }
            } else {

              System.out.println("invalid details, please try again");
              continue;
            }
          } else {

            System.out.println("invalid details, please try again");
            continue;
          }
        sc.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //citation: https://www.baeldung.com/java-caesar-cipher
  public static String cipher(String message, int offset) {

    StringBuilder result = new StringBuilder();
    for (char character : message.toCharArray()) {
      if (character != ' ') {
        int originalAlphabetPosition = character - 'a';
        int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
        char newCharacter = (char) ('a' + newAlphabetPosition);
        result.append(newCharacter);
      } else {
        result.append(character);
      }
    }
    return String.valueOf(result);
  }
}

