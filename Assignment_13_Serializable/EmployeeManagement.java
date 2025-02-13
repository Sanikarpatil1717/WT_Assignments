import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
abstract class Employee implements Serializable{

    private static final long serialVersionUID = 1L; // Serialization identifier
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

    public String toCSV() {
        return id + "," + name + "," + age + "," + salary + "," + designation;
    }
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

// Utility to handle file operations
class FileHandler {
    private static final String FILE_NAME = "Employees.csv";

    public static void writeToFile(Collection<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Employee employee : employees) {
                writer.write(employee.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<Employee> readFromFile() {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                int age = Integer.parseInt(fields[2]);
                double salary = Double.parseDouble(fields[3]);
                String designation = fields[4];
                employees.add(EmployeeFactory.createEmployee(designation, id, name, age, salary));
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return employees;
    }

    public static void updateRecord(Employee updatedEmployee) {
        List<Employee> employees = readFromFile();
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).id == updatedEmployee.id) {
                employees.set(i, updatedEmployee);
                break;
            }
        }
        writeToFile(employees);
    }
}



class EmployeeManagement {

    static LinkedHashMap<Integer, Employee> employees = new LinkedHashMap<>();
    static final String FILE_NAME = "employees.csv";

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

        // Load employees from file if exists
        loadEmployeesFromFile();

        // Main menu after CEO is created
        while (true) {
            System.out.println("1. Create CEO");
            System.out.println("2. Create Employee");
            System.out.println("3. Display Employees");
            System.out.println("4. Raise Salaries");
            System.out.println("5. Remove Employee");
            System.out.println("6. Display CEO");
            System.out.println("7. Search Employee by ID");
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
                    saveEmployeesToFile();  // Save employees to file before exiting
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
       // saveEmployeesToFile();  // Save updated list to file
    }

    static void displayEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees to display.");
            return;
        }

        // Display submenu for sorting options
        while (true) {
            System.out.println("Choose display option:");
            System.out.println("1. Sort by ID");
            System.out.println("2. Sort by Name");
            System.out.println("3. Sort by Age");
            System.out.println("4. Sort by Salary");
            System.out.println("5. Sort by Designation");
            System.out.println("6. Display as Entered");
            System.out.println("7. Exit to main menu");
            System.out.print("Enter choice: ");

            int choice = Menu.readChoice(7);

            // Create a list of employees for sorting or use LinkedHashMap values directly
            List<Employee> sortedEmployees = new ArrayList<>(employees.values());
            switch (choice) {
                case 1 -> sortedEmployees.sort(Comparator.comparingInt(e -> e.id));
                case 2 -> sortedEmployees.sort(Comparator.comparing(e -> e.name));
                case 3 -> sortedEmployees.sort(Comparator.comparingInt(e -> e.age));
                case 4 -> sortedEmployees.sort(Comparator.comparingDouble(e -> e.salary));
                case 5 -> sortedEmployees.sort(Comparator.comparing(e -> e.designation));
                case 6 -> {
                    System.out.println("Employee Details (As Entered):");
                    for (Employee employee : employees.values()) { // Direct iteration
                        employee.display();
                        System.out.println("----------------");
                    }
                    continue; // Loop back to the menu
                }
                case 7 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }
            }

            // Display the sorted list of employees
            System.out.println("Employee Details (Sorted):");
            for (Employee employee : sortedEmployees) {
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
          //  saveEmployeesToFile();  // Save updated list to file
        }
    }

    static void removeEmployee() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ID of the employee to remove: ");
        int id = scanner.nextInt();

        if (employees.remove(id) != null) {
            System.out.println("Employee removed successfully.");
           // saveEmployeesToFile();  // Save updated list to file
        } else {
            System.out.println("No employee found with ID " + id + ".");
        }
    }

    static void searchEmployee() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Search Options:");
            System.out.println("1. Search by ID");
            System.out.println("2. Search by Designation");
            System.out.println("3. Search by Name");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter choice: ");

            int choice = Menu.readChoice(4);
            System.out.println("-------------------------");

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Employee ID: ");
                    int id = scanner.nextInt();
                    Employee employee = employees.get(id);
                    if (employee != null) {
                        employee.display();
                    } else {
                        System.out.println("No employee found with ID " + id + ".");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Designation (Clerk/Programmer/Manager): ");
                    String designation = scanner.next();
                   List<Employee> employeesByDesignation = searchByCriteria(new ArrayList<>(employees.values()), 
                                                                            e -> e.designation.equalsIgnoreCase(designation));

                    if (employeesByDesignation.isEmpty()) {
                        System.out.println("No employees found with designation: " + designation + ".");
                    } else {
                        System.out.println("Employees with designation " + designation + ":");
                        for (Employee e : employeesByDesignation) {
                            e.display();
                            System.out.println("----------------");
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Enter Name: ");
                    scanner.nextLine(); // Consume newline
                    String name = scanner.nextLine();
                    List<Employee> employeesByName = searchByCriteria(new ArrayList<>(employees.values()), 
                                                                   e -> e.name.equalsIgnoreCase(name));

                    if (employeesByName.isEmpty()) {
                        System.out.println("No employees found with name: " + name + ".");
                    } else {
                        System.out.println("Employees with name " + name + ":");
                        for (Employee e : employeesByName) {
                            e.display();
                            System.out.println("----------------");
                        }
                    }
                }
                case 4 -> {
                    System.out.println("Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static List<Employee> searchByCriteria(List<Employee> employees, Predicate<Employee> criteria) 
    {
         return employees.stream().filter(criteria).toList();
    }

    static void saveEmployeesToFile() {
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
        	oos.writeObject(employees);
        	System.out.println("Employees data saved successfully to " + FILE_NAME);
        } catch (IOException e) {
        	System.out.println("Error while saving employees: " + e.getMessage());
        	e.printStackTrace();
        }
   }


    static void loadEmployeesFromFile() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
        employees = (LinkedHashMap<Integer, Employee>) ois.readObject();
        System.out.println("Employees data loaded successfully from " + FILE_NAME);
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("No existing employee data found. Starting fresh.");
        e.printStackTrace();
    }
}


/*    // Method to load employees from CSV file
    static void loadEmployeesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                int age = Integer.parseInt(fields[2]);
                double salary = Double.parseDouble(fields[3]);
                String designation = fields[4];
                Employee employee = EmployeeFactory.createEmployee(designation, id, name, age, salary);
                employees.put(id, employee);
            }
        } catch (IOException e) {
            System.out.println("No existing employee data found, starting fresh.");
        }
    }

    // Method to save employees to CSV file
    static void saveEmployeesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Employee employee : employees.values()) {
                bw.write(employee.id + "," + employee.name + "," + employee.age + "," + employee.salary + "," + employee.designation);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while saving employees to file.");
        }
    }
*/
}


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
                scanner.nextLine();
            } catch (InvalidChoiceException e) {
                System.out.println("Invalid choice. Please select a number between 1 and " + maxChoice + ".");
            }
        }
    }
}

class InvalidChoiceException extends RuntimeException {
}

class IDReader {
    public static int readID(LinkedHashMap<Integer, Employee> employees) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Employee ID: ");
            int id = scanner.nextInt();
            if (employees.containsKey(id)) {
                System.out.println("ID already exists. Please enter a unique ID.");
            } else {
                return id;
            }
        }
    }
}



class NameReader {
    public static String readName() {
        Scanner scanner = new Scanner(System.in);
        String name;
        while (true) {
            System.out.print("Enter Name: ");
            name = scanner.nextLine();
            
            // Check if the name consists of exactly two words, each starting with a capital letter
            if (name.matches("^[A-Z][a-z]+ [A-Z][a-z]+$")) {
                break;
            } else {
                System.out.println("Invalid name. Please enter two words, each starting with a capital letter.");
            }
        }
        return name;
    }
}


class AgeReader {
    public static int readAge(int minAge, int maxAge) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter Age: ");
                int age = scanner.nextInt();
                if (age < minAge || age > maxAge) {
                    throw new InvalidAgeException("Age must be between " + minAge + " and " + maxAge + ".");
                }
                return age;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
            } catch (InvalidAgeException e) {
                System.out.println(e.getMessage());
            }
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
                if (salary <= 0) {
                    throw new IllegalArgumentException("Salary must be greater than 0.");
                }
                return salary;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

class DesignationReader {
    public static String readDesignation() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Designation (Clerk/Programmer/Manager): ");
            String designation = scanner.nextLine();
            if (designation.equalsIgnoreCase("Clerk") || designation.equalsIgnoreCase("Programmer") || designation.equalsIgnoreCase("Manager")) {
                return designation;
            } else {
                System.out.println("Invalid designation. Please choose from Clerk, Programmer, or Manager.");
            }
        }
    }
}
