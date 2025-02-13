import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/employeedb";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "tiger";

    public static JdbcRowSet getRowSet() {
        try {
            JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
            rowSet.setUrl(URL);
            rowSet.setUsername(USERNAME);
            rowSet.setPassword(PASSWORD);
            return rowSet;
        } catch (SQLException e) {
            System.out.println("RowSet initialization failed: " + e.getMessage());
        }
        return null;
    }
}

class EmployeeDAO {
    public static void addEmployee() {
        try (JdbcRowSet rowSet = DBConnection.getRowSet();
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            if (rowSet == null) {
                System.out.println("Database connection unavailable.");
                return;
            }

            System.out.print("Enter Name: ");
            String name = br.readLine();

            System.out.print("Enter Age (21-60): ");
            int age = Integer.parseInt(br.readLine());

            System.out.print("Enter Salary: ");
            double salary = Double.parseDouble(br.readLine());

            System.out.print("Enter Designation: ");
            String designation = br.readLine();

            rowSet.setCommand("SELECT * FROM Employee");
            rowSet.execute();
            rowSet.moveToInsertRow();
            rowSet.updateString("NAME", name);
            rowSet.updateInt("AGE", age);
            rowSet.updateDouble("SALARY", salary);
            rowSet.updateString("DESIGNATION", designation);
            rowSet.insertRow();
            rowSet.moveToCurrentRow();

            System.out.println("Employee added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    public static void displayEmployees() {
        try (JdbcRowSet rowSet = DBConnection.getRowSet()) {
            if (rowSet == null) {
                System.out.println("Database connection unavailable.");
                return;
            }

            rowSet.setCommand("SELECT * FROM Employee ORDER BY EID");
            rowSet.execute();

            System.out.println("\n===== Employee List =====");
            while (rowSet.next()) {
                System.out.printf("ID: %d | Name: %s | Age: %d | Salary: %.2f | Designation: %s\n",
                        rowSet.getInt("EID"), rowSet.getString("NAME"), rowSet.getInt("AGE"),
                        rowSet.getDouble("SALARY"), rowSet.getString("DESIGNATION"));
            }
        } catch (Exception e) {
            System.out.println("Error displaying employees: " + e.getMessage());
        }
    }

    public static void deleteEmployee() {
        try (JdbcRowSet rowSet = DBConnection.getRowSet();
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            if (rowSet == null) {
                System.out.println("Database connection unavailable.");
                return;
            }

            System.out.print("Enter Employee ID to delete: ");
            int eid = Integer.parseInt(br.readLine());

            rowSet.setCommand("SELECT * FROM Employee WHERE EID = ?");
            rowSet.setInt(1, eid);
            rowSet.execute();

            if (rowSet.next()) {
                rowSet.deleteRow();
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Employee not found!");
            }
        } catch (Exception e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
}

class SalaryDAO {
    public static void raiseSalary() {
        try (JdbcRowSet rowSet = DBConnection.getRowSet();
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            if (rowSet == null) {
                System.out.println("Database connection unavailable.");
                return;
            }

            System.out.print("Enter Employee ID to raise salary: ");
            int eid = Integer.parseInt(br.readLine());

            System.out.print("Enter salary increment amount: ");
            double increment = Double.parseDouble(br.readLine());

            rowSet.setCommand("SELECT * FROM Employee WHERE EID = ?");
            rowSet.setInt(1, eid);
            rowSet.execute();

            if (rowSet.next()) {
                rowSet.updateDouble("SALARY", rowSet.getDouble("SALARY") + increment);
                rowSet.updateRow();
                System.out.println("Salary updated successfully!");
            } else {
                System.out.println("Employee not found!");
            }
        } catch (Exception e) {
            System.out.println("Error updating salary: " + e.getMessage());
        }
    }

    public static void displayEmployeeSalary() {
        try (JdbcRowSet rowSet = DBConnection.getRowSet();
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            if (rowSet == null) {
                System.out.println("Database connection unavailable.");
                return;
            }

            System.out.print("Enter Employee ID to view salary: ");
            int eid = Integer.parseInt(br.readLine());

            rowSet.setCommand("SELECT NAME, SALARY FROM Employee WHERE EID = ?");
            rowSet.setInt(1, eid);
            rowSet.execute();

            if (rowSet.next()) {
                System.out.printf("Employee: %s | Salary: %.2f\n", rowSet.getString("NAME"), rowSet.getDouble("SALARY"));
            } else {
                System.out.println("Employee not found!");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving salary: " + e.getMessage());
        }
    }
}

class Menu {
    public static void displayMainMenu() {
        System.out.println("\n===== Employee Management System =====");
        System.out.println("1. Manage Employees");
        System.out.println("2. Manage Salaries");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void displayEmployeeMenu() {
        System.out.println("\n===== Employee Management =====");
        System.out.println("1. Add Employee");
        System.out.println("2. Display Employees");
        System.out.println("3. Delete Employee");
        System.out.println("4. Back");
        System.out.print("Enter your choice: ");
    }

    public static void displaySalaryMenu() {
        System.out.println("\n===== Salary Management =====");
        System.out.println("1. Raise Salary");
        System.out.println("2. Display Employee Salary");
        System.out.println("3. Back");
        System.out.print("Enter your choice: ");
    }

    public static int readChoice(int limit) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int choice = Integer.parseInt(br.readLine());
            return (choice >= 1 && choice <= limit) ? choice : limit;
        } catch (Exception e) {
            return limit;
        }
    }
}

public class EmployeeManagementApp {
    public static void main(String[] args) {
        while (true) {
            Menu.displayMainMenu();
            int choice = Menu.readChoice(3);

            switch (choice) {
                case 1 -> manageEmployees();
                case 2 -> manageSalaries();
                case 3 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
            }
        }
    }

    public static void manageEmployees() {
        while (true) {
            Menu.displayEmployeeMenu();
            int choice = Menu.readChoice(4);

            switch (choice) {
                case 1 -> EmployeeDAO.addEmployee();
                case 2 -> EmployeeDAO.displayEmployees();
                case 3 -> EmployeeDAO.deleteEmployee();
                case 4 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
            }
        }
    }

    public static void manageSalaries() {
        while (true) {
            Menu.displaySalaryMenu();
            int choice = Menu.readChoice(3);

            switch (choice) {
                case 1 -> SalaryDAO.raiseSalary();
                case 2 -> SalaryDAO.displayEmployeeSalary();
                case 3 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
            }
        }
    }
}
