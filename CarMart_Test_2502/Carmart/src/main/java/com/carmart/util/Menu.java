package com.carmart.util;

import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    public static int displayMainMenu() {
        System.out.println("\nJava CarMart");
        System.out.println("---------------");
        System.out.println("1. Add Car");
        System.out.println("2. Search Car");
        System.out.println("3. Update Price");
        System.out.println("4. Sold");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
        return scanner.nextInt();
    }

    public static int displaySearchMenu() {
        System.out.println("\nSearch Menu");
        System.out.println("-----------");
        System.out.println("1. All (Unsold Cars)");
        System.out.println("2. By Company");
        System.out.println("3. By Type");
        System.out.println("4. By Price Range");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
        return scanner.nextInt();
    }

    public static int displaySoldMenu() {
        System.out.println("\nSold Menu");
        System.out.println("---------");
        System.out.println("1. View All Sold Cars");
        System.out.println("2. Mark Car as Sold");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        return scanner.nextInt();
    }

    public static String getStringInput(String message) {
        System.out.print(message);
        return scanner.next();
    }

    public static int getIntInput(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static double getDoubleInput(String message) {
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
