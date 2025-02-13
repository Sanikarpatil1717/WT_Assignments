import java.util.Scanner;
import java.util.InputMismatchException;


//------------class for custom exception handling 
class InvalidAgeException extends Exception {
    public InvalidAgeException() {
        super();
    }

    public InvalidAgeException(String message) {
        super(message);
    }
}

//------------------

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

    abstract void raiseSalary(double increment);
}

final class Clerk extends Employee {
    Clerk(int id, String name, int age, double salary) {
        super(id, name, age, salary, "Clerk");
    }

    @Override
    void raiseSalary(double increment) {
        salary += increment;
    }
}

final class Programmer extends Employee {
    Programmer(int id, String name, int age, double salary) {
        super(id, name, age, salary, "Programmer");
    }

    @Override
    void raiseSalary(double increment) {
        salary += increment * 1.2;
    }
}

final class Manager extends Employee {
    Manager(int id, String name, int age, double salary) {
        super(id, name, age, salary, "Manager");
    }

    @Override
    void raiseSalary(double increment) {
        salary += increment * 1.5;
    }
}

public class EmployeeManagement {
    static int MAX_EMPLOYEES = 100;
    static Employee[] employees = new Employee[MAX_EMPLOYEES];
    static int employeeCount = 0;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            
                System.out.println("1. Create");
                System.out.println("2. Display");
                System.out.println("3. Raise Salary");
                System.out.println("4. Remove");
                System.out.println("5. Exit");
                System.out.println("-------------------");
                System.out.print("Enter choice: ");
                int choice = Menu.readChoice(5);

                switch (choice) {
                    case 1:
                        createEmployee();
                        break;
                    case 2:
                        displayEmployee();
                        break;
                    case 3:
                        raiseEmployeeSalary();
                        break;
                    case 4:
                        removeEmployee();
                        break;
                    case 5:
                        System.out.println("Exiting----");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
           
        }
    }

    static void createEmployee() {
        if (employeeCount >= MAX_EMPLOYEES) {
            System.out.println("Cannot add more employees. Maximum limit reached.");
            return;
        }

        int id = 0;

        while (true) {
            try {
                System.out.print("Enter ID: ");
                id = scanner.nextInt();
                if (isIdExists(id)) {
                    System.out.println("Employee with ID " + id + " already exists. Please enter a different ID.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error: ID must be a valid integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        scanner.nextLine(); // Consume newline

        System.out.print("Enter Name: ");
	int spaces=0;
	
        String name = scanner.nextLine();

        int age = 0;
        while (true) {
            try {
                System.out.print("Enter Age: ");
                age = scanner.nextInt();
                validateAge(age);
                break;
            } catch (InvalidAgeException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        System.out.print("Enter Salary: ");
        double salary = scanner.nextDouble();

        scanner.nextLine(); // Consume newline

        int designationChoice;
        String designation = null;
        while (true) {
            System.out.println("Select Designation:");
            System.out.println("1. Clerk");
            System.out.println("2. Programmer");
            System.out.println("3. Manager");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            designationChoice =Menu.readChoice(4);
            scanner.nextLine(); // Consume newline

            switch (designationChoice) {
                case 1:
                    designation = "Clerk";
                    break;
                case 2:
                    designation = "Programmer";
                    break;
                case 3:
                    designation = "Manager";
                    break;
                case 4:
                    System.out.println("Exiting designation selection...");
                    return;
                default:
                    System.out.println("Invalid choice. Please select again.");
            }

            if (designation != null) break;
        }

        Employee employee;
        switch (designation.toLowerCase()) {
            case "clerk":
                employee = new Clerk(id, name, age, salary);
                break;
            case "programmer":
                employee = new Programmer(id, name, age, salary);
                break;
            case "manager":
                employee = new Manager(id, name, age, salary);
                break;
            default:
                System.out.println("Invalid designation. Employee not created.");
                return;
        }

        employees[employeeCount++] = employee;
        System.out.println("Employee added successfully!");
    }

    static void validateAge(int age) throws InvalidAgeException {
        if (age < 21 || age > 60) {
            throw new InvalidAgeException("Age must be between 21 and 60.");
        }
    }

    static boolean isIdExists(int id) {
        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].id == id) {
                return true;
            }
        }
        return false;
    }

    static void displayEmployee() {
        if (employeeCount == 0) {
            System.out.println("No Employees");
            return;
        }
        for (int i = 0; i < employeeCount; i++) {
            employees[i].display();
            System.out.println("-------------------");
        }
    }

    static void raiseEmployeeSalary() {
        System.out.print("Enter Employee ID to raise salary: ");
        int id = scanner.nextInt();

        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].id == id) {
                System.out.print("Enter salary increment: ");
                double increment = scanner.nextDouble();
                employees[i].raiseSalary(increment);
                return;
            }
        }

        System.out.println("Employee with ID " + id + " not found.");
    }

    static void removeEmployee() {
        System.out.print("Enter Employee ID to remove: ");
        int id = scanner.nextInt();
        for (int i = 0; i < employeeCount; i++) {
            if (employees[i].id == id) {
                employees[i].display();
                System.out.print("Do you really want to remove this employee record (Y/N): ");
                char confirm = scanner.next().charAt(0);

                if (confirm == 'Y' || confirm == 'y') {
                    for (int j = i; j < employeeCount - 1; j++) {
                        employees[j] = employees[j + 1];
                    }

                    employees[--employeeCount] = null;
                    System.out.println("Employee removed successfully!");
                } else {
                    System.out.println("Employee record not removed.");
                }
                return;
            }
        }
    }
}



class Menu
{
	private static int maxChoice;
	public static int readChoice(int max)
	{
		maxChoice=max;
		while(true)
		{
			System.out.println("Enter Choice :");
			try
			{
				int choice=new Scanner(System.in).nextInt();
				if(choice<1 || choice>maxChoice)
				{
					throw new InvalidChoiceException();
				}
				return choice;
			}
			catch(InputMismatchException e)
			{
				System.out.println("Plz enter no. only");
			}
			catch(InvalidChoiceException e)
			{
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


