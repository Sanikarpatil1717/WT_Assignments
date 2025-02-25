package com.carmart.dao;

import com.carmart.util.DBConnection;
import com.carmart.util.Menu;
import java.sql.*;

public class CarDAO 
{
	//add car to DB
	public void addCar(int companyID, String model, int seater, String fuelType, String type, double price) 
	{
	    String sql = "INSERT INTO Car (companyID, model, seater, fuelType, type, price) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) 
	    {
	        
	        pstmt.setInt(1, companyID);
	        pstmt.setString(2, model);
	        pstmt.setInt(3, seater);
	        pstmt.setString(4, fuelType);
	        pstmt.setString(5, type);
	        pstmt.setDouble(6, price);
	        pstmt.executeUpdate();
	        
	        System.out.println("Car added successfully...!");
	    } 
	    catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }
	}
    
    //  Searches for cars based on different criteria.
    
    public void searchCars(int searchChoice) {
        String sql = switch (searchChoice) {
            case 1 -> "SELECT * FROM Car WHERE carID NOT IN (SELECT carID FROM Sale WHERE sold = TRUE)";
            case 2 -> "SELECT * FROM Car WHERE companyID = (SELECT companyID FROM Company WHERE companyName = ?) AND carID NOT IN (SELECT carID FROM Sale WHERE sold = TRUE)";
            case 3 -> "SELECT * FROM Car WHERE type = ? AND carID NOT IN (SELECT carID FROM Sale WHERE sold = TRUE)";
            case 4 -> "SELECT * FROM Car WHERE price BETWEEN ? AND ? AND carID NOT IN (SELECT carID FROM Sale WHERE sold = TRUE)";
            default -> null;
        };

        if (sql == null) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (searchChoice == 2) {
                String companyName = Menu.getStringInput("Enter Company Name: ");
                pstmt.setString(1, companyName);
            } else if (searchChoice == 3) {
                String type = Menu.getStringInput("Enter Car Type: ");
                pstmt.setString(1, type);
            } else if (searchChoice == 4) {
                double minPrice = Menu.getDoubleInput("Enter Min Price: ");
                double maxPrice = Menu.getDoubleInput("Enter Max Price: ");
                pstmt.setDouble(1, minPrice);
                pstmt.setDouble(2, maxPrice);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt("carID") + " | " +
                                       rs.getString("model") + " | " +
                                       rs.getInt("seater") + " | " +
                                       rs.getString("fuelType") + " | " +
                                       rs.getString("type") + " | " +
                                       rs.getDouble("price"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    //Updates the price of a car.
     
    public void updatePrice(int carId, double newPrice) {
        String sql = "UPDATE Car SET price = ? WHERE carID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, carId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Price updated successfully.");
            } else {
                System.out.println("Car ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
  

    //Displays all sold cars.
     
    public void displaySoldCars() {
        String sql = "SELECT Car.carID, Car.model, Car.price, Company.companyName " +
                     "FROM Car JOIN Sale ON Car.carID = Sale.carID " +
                     "JOIN Company ON Car.companyID = Company.companyID WHERE Sale.sold = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getInt("carID") + " | " +
                                   rs.getString("model") + " | " +
                                   rs.getDouble("price") + " | " +
                                   rs.getString("companyName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void displayCompanies() {
        String sql = "SELECT companyID, companyName FROM Company";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("Available Companies:");
            while (rs.next()) {
                System.out.println(rs.getInt("companyID") + " - " + rs.getString("companyName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleSoldMenu(int soldChoice) {
        switch (soldChoice) {
            case 1 ->displaySoldCars();
            case 2 -> markCarAsSold();
            case 3 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice! Please try again.");
        }
    }
    
    public void markCarAsSold() {
        int carID = Menu.getIntInput("Enter Car ID to mark as sold: ");
        
        String checkSql = "SELECT * FROM Sale WHERE carID = ?";
        String updateSql = "UPDATE Sale SET sold = TRUE WHERE carID = ?";
        String insertSql = "INSERT INTO Sale (carID, sold) VALUES (?, TRUE)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setInt(1, carID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                updateStmt.setInt(1, carID);
                updateStmt.executeUpdate();
            } else {
                insertStmt.setInt(1, carID);
                insertStmt.executeUpdate();
            }

            System.out.println("Car marked as sold.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	

}
