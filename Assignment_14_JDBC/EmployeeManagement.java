import java.util.*;
import java.sql.*;

class EmployeeFactory {
	public static Employee createEmployee(String designation, int id, String name, int age, double salary,
			String department) {
		return new Employee(id, name, age, salary, designation, department);
	}
}

public class EmployeeManagement {

	static Connection connection;

	static {
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/employeedb", "postgres",
					"tiger");
		} catch (SQLException e) {
			System.err.println("Database connection failed: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		CEO ceo = null;
		Scanner scanner = new Scanner(System.in);

		// Ensure CEO is created first
		while (ceo == null) {
			System.out.println("CEO must be created before performing other operations.");
			System.out.println("1. Create CEO");
			System.out.println("7. Exit");
			System.out.print("Enter choice: ");
			int choice = scanner.nextInt();

			if (choice == 1) {
				ceo = createCEO();
			} else if (choice == 7) {
				System.out.println("Exiting...");
				return;
			} else {
				System.out.println("Invalid choice. You must create the CEO first.");
			}
		}

		// Main menu after CEO is created
		while (true) {
			System.out.println("1. Create Employee");
			System.out.println("2. Display Employees");
			System.out.println("3. Appraisal");
			System.out.println("4. Search Employee");
			System.out.println("5. Remove Employee");
			System.out.println("6. Display CEO");
			System.out.println("7. Exit");
			System.out.print("Enter choice: ");
			int mainChoice = scanner.nextInt();
			scanner.nextLine();
			switch (mainChoice) {
			case 1 -> createEmployee();
			case 2 -> displayEmployees();
			case 3 -> appraisal();
			case 4 -> searchEmployee();
			case 5 -> removeEmployee();
			case 6 -> ceo.display(); // Display CEO details
			case 7 -> {
				System.out.println("Exiting...");
				return;
			}
			default -> System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	// Create CEO (Singleton)
	static CEO createCEO() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter CEO Name: ");
		String name = scanner.nextLine();
		return CEO.getInstance(name); // Create the CEO
	}

	static void createEmployee() {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.print("Enter Employee ID: ");
			int id = scanner.nextInt();
			scanner.nextLine(); // Consume newline
			System.out.print("Enter Name: ");
			String name = scanner.nextLine();
			System.out.print("Enter Age: ");
			int age = scanner.nextInt();
			scanner.nextLine(); // Consume newline
			System.out.print("Enter Salary: ");
			double salary = scanner.nextDouble();
			scanner.nextLine(); // Consume newline
			System.out.print("Enter Designation (Clerk/Programmer/Manager): ");
			String designation = scanner.nextLine();
			System.out.print("Enter Department: ");
			String department = scanner.nextLine(); // Capture Department

			String query = "INSERT INTO employee (eid, name, age, salary, designation, department) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement stmt = connection.prepareStatement(query)) {
				stmt.setInt(1, id);
				stmt.setString(2, name);
				stmt.setInt(3, age);
				stmt.setDouble(4, salary);
				stmt.setString(5, designation);
				stmt.setString(6, department); // Set the department
				stmt.executeUpdate();
				System.out.println("Employee added successfully!");
			} catch (SQLException e) {
				System.err.println("Failed to add employee: " + e.getMessage());
			}
		} catch (Exception e) {
			System.err.println("Invalid input. Please try again." + e);
		}
	}
//--------------------------------------------------------------------

	static void displayEmployees() {
    Scanner scanner = new Scanner(System.in);
    try {
        System.out.println("Choose display option:");
        System.out.println("1. Sort by ID");
        System.out.println("2. Sort by Name");
        System.out.println("3. Sort by Age");
        System.out.println("4. Sort by Salary");
        System.out.println("5. Sort by Designation");
        System.out.println("6. Sort by Department");
        System.out.println("7. Display as Entered");
        System.out.println("8. Exit to main menu");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        String query = "SELECT * FROM employee"; // Default query
        switch (choice) {
            case 1 -> query += " ORDER BY eid";
            case 2 -> query += " ORDER BY name";
            case 3 -> query += " ORDER BY age";
            case 4 -> query += " ORDER BY salary";
            case 5 -> query += " ORDER BY designation";
            case 6 -> query += " ORDER BY department";
            case 7 -> query += ""; // Keep as entered (default order)
            case 8 -> {
                System.out.println("Exiting to main menu...");
                return;
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID: " + rs.getInt("eid"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Salary: " + rs.getDouble("salary"));
                System.out.println("Designation: " + rs.getString("designation"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("----------------------------");
            }
            if (!found) {
                System.out.println("No employees found.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve employees: " + e.getMessage());
        }
    } catch (Exception e) {
        System.err.println("Invalid input. Please try again.");
    }
}


	// Appraisal Logic (Increase salary based on ID with Exception Handling)
	static void appraisal() {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.print("Enter Employee ID for Appraisal: ");
			int id = scanner.nextInt();
			String query = "SELECT * FROM employee WHERE eid = ?";
			try (PreparedStatement stmt = connection.prepareStatement(query)) {
				stmt.setInt(1, id);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					double currentSalary = rs.getDouble("salary");
					System.out.print("Enter appraisal amount: ");
					double appraisalAmount = scanner.nextDouble();
					double newSalary = currentSalary + appraisalAmount;

					String updateQuery = "UPDATE employee SET salary = ? WHERE eid = ?";
					try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
						updateStmt.setDouble(1, newSalary);
						updateStmt.setInt(2, id);
						updateStmt.executeUpdate();
						System.out.println("Salary updated to: " + newSalary);
					}
				} else {
					System.out.println("No employee found with ID " + id + ".");
				}
			} catch (SQLException e) {
				System.err.println("Error during appraisal: " + e.getMessage());
			}
		} catch (Exception e) {
			System.err.println("Invalid input. Please try again.");
		}
	}

	static void searchEmployee() {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.println("Search Options:");
			System.out.println("1. Search by ID");
			System.out.println("2. Search by Designation");
			System.out.println("3. Search by Name");
			System.out.println("4. Search by Department");
			System.out.println("5. Return to Main Menu");
			System.out.print("Enter choice: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			String query = "";
			PreparedStatement stmt = null;

			switch (choice) {
			case 1 -> {
				System.out.print("Enter Employee ID: ");
				int id = scanner.nextInt();
				query = "SELECT * FROM employee WHERE eid = ?";
				stmt = connection.prepareStatement(query);
				stmt.setInt(1, id);
			}
			case 2 -> {
				System.out.print("Enter Designation (Clerk/Programmer/Manager): ");
				String designation = scanner.nextLine();
				query = "SELECT * FROM employee WHERE designation = ?";
				stmt = connection.prepareStatement(query);
				stmt.setString(1, designation);
			}
			case 3 -> {
				System.out.print("Enter Name: ");
				String name = scanner.nextLine();
				query = "SELECT * FROM employee WHERE name = ?";
				stmt = connection.prepareStatement(query);
				stmt.setString(1, name);
			}
			case 4 -> {
				System.out.print("Enter Department: ");
				String department = scanner.nextLine();
				query = "SELECT * FROM employee WHERE department = ?";
				stmt = connection.prepareStatement(query);
				stmt.setString(1, department);
			}
			case 5 -> {
				System.out.println("Exiting to main menu..");
				return;
			}
			default -> {
				System.out.println("Invalid choice.");
				return;
			}
			}

			// Execute the query
			try (ResultSet rs = stmt.executeQuery()) {
				boolean found = false;
				while (rs.next()) {
					found = true;
					System.out.println("ID: " + rs.getInt("eid"));
					System.out.println("Name: " + rs.getString("name"));
					System.out.println("Age: " + rs.getInt("age"));
					System.out.println("Salary: " + rs.getDouble("salary"));
					System.out.println("Designation: " + rs.getString("designation"));
					System.out.println("Department: " + rs.getString("department"));
					System.out.println("----------------------------");
				}
				if (!found) {
					System.out.println("No results found.");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error during search: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Invalid input. Please try again.");
		}
	}

	// Remove Employee with Exception Handling
	static void removeEmployee() {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.print("Enter Employee ID to remove: ");
			int id = scanner.nextInt();
			String query = "DELETE FROM employee WHERE eid = ?";
			try (PreparedStatement stmt = connection.prepareStatement(query)) {
				stmt.setInt(1, id);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Employee removed successfully.");
				} else {
					System.out.println("No employee found with ID " + id + ".");
				}
			} catch (SQLException e) {
				System.err.println("Error removing employee: " + e.getMessage());
			}
		} catch (Exception e) {
			System.err.println("Invalid input. Please try again.");
		}
	}
}

// Employee Class for easier handling
class Employee {
	private int id;
	private String name;
	private int age;
	private double salary;
	private String designation;
	private String department; // Added department field

	// Constructor and Getters
	public Employee(int id, String name, int age, double salary, String designation, String department) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.salary = salary;
		this.designation = designation;
		this.department = department; // Initialize department
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public double getSalary() {
		return salary;
	}

	public String getDesignation() {
		return designation;
	}

	public String getDepartment() {
		return department; // Getter for department
	}

	@Override
	public String toString() {
		return "Employee{id=" + id + ", name='" + name + "', age=" + age + ", salary=" + salary + ", designation='"
				+ designation + "', department='" + department + "'}"; // Updated toString
	}
}

// CEO Class (Singleton pattern)
class CEO {
	private static CEO instance;
	private String name;

	private CEO(String name) {
		this.name = name;
	}

	public static CEO getInstance(String name) {
		if (instance == null) {
			instance = new CEO(name);
		}
		return instance;
	}

	public void display() {
		System.out.println("CEO: " + name);
	}
}
