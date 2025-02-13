import java.util.Scanner;
import java.util.InputMismatchException;

// Custom Exception for Invalid Age
class InvalidAgeException extends Exception {
        public InvalidAgeException(String message) {
                super(message);
        }
}

class IDReader {
        public static int readID(Employee[] employees, int employeeCount) {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                        try {
                                System.out.print("Enter ID: ");
                                int id = scanner.nextInt();
                                if (isIDExists(employees, employeeCount, id)) {
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

        private static boolean isIDExists(Employee[] employees, int employeeCount, int id) {
                for (int i = 0; i < employeeCount; i++) {
                        if (employees[i].id == id) {
                                return true;
                        }
                }
                return false;
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
                        System.out.print("Enter Designation (Clerk, Programmer, Manager): ");
                        String designation = scanner.nextLine();
                        if (designation.equals("Clerk") || designation.equals("Programmer") || designation.equals("Manager")) {
                                return designation;
                        } else {
                                System.out.println("Error: Designation must be one of 'Clerk', 'Programmer', or 'Manager'.");
                        }
                }
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

// Main Class
public class EmployeeManagement {
        static final int MAX_EMPLOYEES = 100;
        static Employee[] employees = new Employee[MAX_EMPLOYEES];
        static int employeeCount = 0;

        public static void main(String[] args) {
                while (true) {
                        System.out.println("1. Create Employee");
                        System.out.println("2. Display Employees");
                        System.out.println("3. Raise Salaries");
                        System.out.println("4. Remove Employee");
                        System.out.println("5. Exit");
                        System.out.print("Enter choice: ");
                        int choice = Menu.readChoice(5);

                        switch (choice) {
                                case 1:
                                        createEmployee();
                                        break;
                                case 2:
                                        displayEmployees();
                                        break;
                                case 3:
                                        raiseSalaries();
                                        break;
                                case 4:
                                        removeEmployee();
                                        break;
                                case 5:
                                        System.out.println("Exiting...");
                                        return;
                        }
                }
        }

        static void createEmployee() {
                int id = IDReader.readID(employees, employeeCount);
                String name = NameReader.readName();
                int age = AgeReader.readAge(21, 60);
                double salary = SalaryReader.readSalary();
                String designation = DesignationReader.readDesignation();

                Employee employee = switch (designation) {
                        case "Clerk" -> new Clerk(id, name, age, salary);
                        case "Programmer" -> new Programmer(id, name, age, salary);
                        case "Manager" -> new Manager(id, name, age, salary);
                        default -> null;
                };

                if (employee != null) {
                        employees[employeeCount++] = employee;
                        System.out.println("Employee added successfully!");
                }
        }

        static void displayEmployees() {
                if (employeeCount == 0) {
                        System.out.println("No employees to display.");
                } else {
                        for (int i = 0; i < employeeCount; i++) {
                                employees[i].display();
                                System.out.println("----------------");
                        }
                }
        }

        static void raiseSalaries() {
                if (employeeCount == 0) {
                        System.out.println("No employees to raise salaries.");
                } else {
                        for (Employee employee : employees) {
                                if (employee != null) {
                                        employee.raiseSalary();
                                }
                        }
                        System.out.println("Salaries raised for all employees.");
                }
        }

        static void removeEmployee() {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter ID of the employee to remove: ");
                int id = scanner.nextInt();

                for (int i = 0; i < employeeCount; i++) {
                        if (employees[i].id == id) {
                                for (int j = i; j < employeeCount - 1; j++) {
                                        employees[j] = employees[j + 1];
                                }
                                employees[--employeeCount] = null;
                                System.out.println("Employee removed successfully.");
                                return;
                        }
                }
                System.out.println("No employee found with ID " + id + ".");
        }
}

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

class InvalidChoiceException extends RuntimeException
{
	public InvalidChoiceException()
	{
	}
	public InvalidChoiceException(String msg)
	{
		super(msg);
	}
	public void displayMessage(int maxC)
	{
		System.out.println("Plzzz enter choice between 1 and "+maxC);
	}
}

