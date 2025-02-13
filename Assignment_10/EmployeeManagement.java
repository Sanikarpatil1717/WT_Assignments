import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Scanner;

// Custom Exception for Invalid Age
class InvalidAgeException extends Exception {
	public InvalidAgeException(String message) {
		super(message);
	}
}

// Custom Exception for Missing CEO
class MissingCEOException extends RuntimeException {
	public MissingCEOException(String message) {
		super(message);
	}
}

class CEO {
	private static CEO instance; // Singleton instance
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

	public static CEO getInstance() throws MissingCEOException {
		if (instance == null) {
			throw new MissingCEOException("CEO must be created before performing other operations.");
		}
		return instance;
	}

	public void display() {
		System.out.println("CEO Name: " + name);
	}
}

// Employee Class Definitions
abstract class Employee {
	int id;
	String name;
	int age;
	double salary;
	String designation;

	Employee(int id, String name, int age, double salary, String designation) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.salary = salary;
		this.designation = designation;
	}

	void display() {
		System.out.println("ID: " + id);
		System.out.println("Name: " + name);
		System.out.println("Age: " + age);
		System.out.println("Salary: " + salary);
		System.out.println("Designation: " + designation);
	}

	abstract void raiseSalary();
}

final class Clerk extends Employee {
	Clerk(int id, String name, int age, double salary) {
		super(id, name, age, salary, "Clerk");
	}

	@Override
	void raiseSalary() {
		salary += 2000;
	}
}

final class Programmer extends Employee {
	Programmer(int id, String name, int age, double salary) {
		super(id, name, age, salary, "Programmer");
	}

	@Override
	void raiseSalary() {
		salary += 5000;
	}
}

final class Manager extends Employee {
	Manager(int id, String name, int age, double salary) {
		super(id, name, age, salary, "Manager");
	}

	@Override
	void raiseSalary() {
		salary += 10000;
	}
}

// Factory Design Pattern for Employee Creation
abstract class EmployeeFactory {
	public static Employee createEmployee(String designation, int id, String name, int age, double salary) {
		return switch (designation) {
		case "Clerk" -> new Clerk(id, name, age, salary);
		case "Programmer" -> new Programmer(id, name, age, salary);
		case "Manager" -> new Manager(id, name, age, salary);
		default -> throw new IllegalArgumentException("Invalid Designation");
		};
	}
}

// Custom Iterator for Employee Array
class EmployeeIterator implements Iterator<Employee> {
	private final Employee[] employees;
	private int position;

	EmployeeIterator(Employee[] employees, int employeeCount) {
		this.employees = new Employee[employeeCount];
		System.arraycopy(employees, 0, this.employees, 0, employeeCount);
		this.position = 0;
	}

	@Override
	public boolean hasNext() {
		return position < employees.length && employees[position] != null;
	}

	@Override
	public Employee next() {
		return employees[position++];
	}
}

public class EmployeeManagement {
	static HashMap<Integer, Employee> employees = new HashMap<>(); // Stores employees with unique IDs

	public static void main(String[] args) {
		CEO ceo = null;

		// Ensure CEO is created first
		while (ceo == null) {
			System.out.println("CEO must be created before performing other operations.");
			System.out.println("1. Create CEO");
			System.out.println("7. Exit");
			System.out.print("Enter choice: ");
			int choice = Menu.readChoice(7);

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
			System.out.println("1. Create CEO");
			System.out.println("2. Create Employee");
			System.out.println("3. Display Employees");
			System.out.println("4. Raise Salaries");
			System.out.println("5. Remove Employee");
			System.out.println("6. Display CEO");
			System.out.println("7. Search Employee by ID"); // Added search option
			System.out.println("8. Exit");
			System.out.print("Enter choice: ");
			int choice = Menu.readChoice(8);
			System.out.println("-------------------------");

			switch (choice) {
			case 1 -> System.out.println("Error: CEO already exists!");
			case 2 -> createEmployee();
			case 3 -> displayEmployees();
			case 4 -> raiseSalaries();
			case 5 -> removeEmployee();
			case 6 -> ceo.display(); // Display CEO details
			case 7 -> searchEmployee(); // Search by ID
			case 8 -> {
				System.out.println("Exiting...");
				return;
			}
			default -> System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	static CEO createCEO() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter CEO Name: ");
		String name = scanner.nextLine();
		return CEO.getInstance(name); // Create the CEO
	}

	static void createEmployee() {
		int id = IDReader.readID(employees); // Updated to check HashMap for uniqueness
		String name = NameReader.readName();
		int age = AgeReader.readAge(21, 60);
		double salary = SalaryReader.readSalary();
		String designation = DesignationReader.readDesignation();

		Employee employee = EmployeeFactory.createEmployee(designation, id, name, age, salary);
		employees.put(id, employee); // Store in HashMap
		System.out.println("Employee added successfully!");
	}

	static void displayEmployees() {
		if (employees.isEmpty()) {
			System.out.println("No employees to display.");
		} else {
			for (Employee employee : employees.values()) {
				employee.display();
				System.out.println("----------------");
			}
		}
	}

	static void raiseSalaries() {
		if (employees.isEmpty()) {
			System.out.println("No employees to raise salaries.");
		} else {
			for (Employee employee : employees.values()) {
				employee.raiseSalary();
			}
			System.out.println("Salaries raised for all employees.");
		}
	}

	static void removeEmployee() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter ID of the employee to remove: ");
		int id = scanner.nextInt();

		if (employees.remove(id) != null) {
			System.out.println("Employee removed successfully.");
		} else {
			System.out.println("No employee found with ID " + id + ".");
		}
	}

	static void searchEmployee() { // New method to search employee by ID
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter Employee ID to search: ");
		int id = scanner.nextInt();

		Employee employee = employees.get(id);
		if (employee != null) {
			employee.display();
		} else {
			System.out.println("No employee found with ID " + id + ".");
		}
	}
}
// Utility Classes
// (Menu, IDReader, NameReader, AgeReader, SalaryReader, DesignationReader remain the same as in the provided code)

// Utility Classes
class Menu {
	public static int readChoice(int maxChoice) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				int choice = scanner.nextInt();
				if (choice < 1 || choice > maxChoice) {
					throw new InvalidChoiceException();
				}
				return choice;
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid number.");
				scanner.nextLine(); // Clear invalid input
			} catch (InvalidChoiceException e) {
				e.displayMessage(maxChoice);
			}
		}
	}
}

class InvalidChoiceException extends RuntimeException {
	public InvalidChoiceException() {
	}

	public InvalidChoiceException(String msg) {
		super(msg);
	}

	public void displayMessage(int maxC) {
		System.out.println("Please enter a choice between 1 and " + maxC);
	}
}

class IDReader {
	public static int readID(HashMap<Integer, Employee> employees) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Enter ID: ");
				int id = scanner.nextInt();
				if (employees.containsKey(id)) {
					System.out.println("Employee with ID " + id + " already exists. Please enter a different ID.");
				} else {
					return id;
				}
			} catch (InputMismatchException e) {
				System.out.println("Error: ID must be a valid integer.");
				scanner.nextLine(); // Clear invalid input
			}
		}
	}
}

class NameReader {
	public static String readName() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Enter Name (Firstname Lastname): ");
			String name = scanner.nextLine();
			if (name.matches("[A-Z][a-z]* [A-Z][a-z]*")) {
				return name;
			} else {
				System.out.println("Error: Name must be in 'Firstname Lastname' format.");
			}
		}
	}
}

class AgeReader {
	public static int readAge(int minAge, int maxAge) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Enter Age: ");
				int age = scanner.nextInt();
				validateAge(age, minAge, maxAge);
				return age;
			} catch (InvalidAgeException e) {
				System.out.println("Error: " + e.getMessage());
			} catch (InputMismatchException e) {
				System.out.println("Error: Please enter a valid integer.");
				scanner.nextLine(); // Clear invalid input
			}
		}
	}

	private static void validateAge(int age, int minAge, int maxAge) throws InvalidAgeException {
		if (age < minAge || age > maxAge) {
			throw new InvalidAgeException("Age must be between " + minAge + " and " + maxAge + ".");
		}
	}
}

class SalaryReader {
	public static double readSalary() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				System.out.print("Enter Salary: ");
				double salary = scanner.nextDouble();
				if (salary > 0) {
					return salary;
				} else {
					System.out.println("Error: Salary must be a positive number.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Error: Please enter a valid number.");
				scanner.nextLine(); // Clear invalid input
			}
		}
	}
}

class DesignationReader {
	public static String readDesignation() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Enter Designation (Clerk/Programmer/Manager): ");
			String designation = scanner.next();
			if (designation.matches("Clerk|Programmer|Manager")) {
				return designation;
			} else {
				System.out.println("Error: Invalid designation.");
			}
		}
	}
}
