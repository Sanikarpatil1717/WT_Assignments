package com.carmart;

import com.carmart.util.Menu;
import com.carmart.dao.CarDAO;
import java.util.Scanner;

public class CarMartApp 
{
    private static final CarDAO carDAO = new CarDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            int choice = Menu.displayMainMenu();
            handleUserChoice(choice);
        }
    }

    private static void handleUserChoice(int choice) 
    {
        switch (choice) {
            case 1 -> addCar();
            case 2 -> searchCar();
            case 3 -> updateCarPrice();
            case 4 -> handleSoldCars();
            case 5 -> exitApplication();
            default -> System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void addCar() 
    {
        carDAO.displayCompanies(); 
        int companyID = Menu.getIntInput("Enter Company ID from above list: "); 
        String model = Menu.getStringInput("Enter Model: ");
//        int seater = Menu.getIntInput("Enter Seater: ");
//        String fuelType = Menu.getStringInput("Enter Fuel Type: ");
//        String type = Menu.getStringInput("Enter Type (Sedan/SUV etc.): ");
//        double price = Menu.getDoubleInput("Enter Price: ");
        int seater;
        while (true) {
            seater = Menu.getIntInput("Enter Seater (2/3/4/5/7): ");
            if (seater == 2 || seater == 3 || seater == 4 || seater == 5 || seater == 7) {
                break;
            }
            System.out.println("Invalid seater value! Please enter 2, 3, 4, 5, or 7.");
        }
        String fuelType;
        while (true) {
            fuelType = Menu.getStringInput("Enter Fuel Type (CNG/Petrol/Diesel/Electric): ").toLowerCase();
            if (fuelType.equals("cng") || fuelType.equals("petrol") || fuelType.equals("diesel") || fuelType.equals("electric")) {
                break;
            }
            System.out.println("Invalid fuel type! Please enter CNG, Petrol, Diesel, or Electric.");
        }
        String type;
        while (true) {
            type = Menu.getStringInput("Enter Type (Sedan/SUV/Hatchback): ").toLowerCase();
            if (type.equals("sedan") || type.equals("suv") || type.equals("hatchback")) {
                break;
            }
            System.out.println("Invalid type! Please enter Sedan, SUV, or Hatchback.");
        }
        double price = Menu.getDoubleInput("Enter Price: ");

        carDAO.addCar(companyID, model, seater, fuelType, type, price);
    }


    private static void searchCar() 
    {
        while (true) 
        { 
            int searchChoice = Menu.displaySearchMenu();
            if (searchChoice == 5) 
            	break; 
            carDAO.searchCars(searchChoice);
        }
    }


    private static void updateCarPrice() 
    {
        int carId = Menu.getIntInput("Enter Car ID to update price: ");
        double newPrice = Menu.getDoubleInput("Enter new price: ");
        carDAO.updatePrice(carId, newPrice);
    }

    private static void handleSoldCars() 
    {
        while (true) 
        { 
            int soldChoice = Menu.displaySoldMenu();
            if (soldChoice == 3) 
            	break; 
            carDAO.handleSoldMenu(soldChoice);
        }
    }


    private static void exitApplication() 
    {
        System.out.println("Exiting CarMart...");
        scanner.close();
        System.exit(0);
    }
}
