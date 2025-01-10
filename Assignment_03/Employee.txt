package ass3.emp;

import java.util.Scanner;

abstract class Employee 
{
    int id;
    String name;
    int age;
    double salary;
    String designation;

   	Employee(int id, String name, int age, double salary, String designation) 
	{
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.designation = designation;
   	}

    	void display() 
	{
        	System.out.println("ID: " + id);
        	System.out.println("Name: " + name);
        	System.out.println("Age: " + age);
        	System.out.println("Salary: " + salary);
        	System.out.println("Designation: " + designation);
   	}

    abstract void raiseSalary(double increment);
}


final class Clerk extends Employee 
{
    	Clerk(int id, String name, int age, double salary) 
	{
        	super(id, name, age, salary, "Clerk");
    	}

   	@Override
    	void raiseSalary(double increment) 
	{
        	salary += increment;
    	}
}

final class Programmer extends Employee 
{
    	Programmer(int id, String name, int age, double salary) 
	{
        	super(id, name, age, salary, "Programmer");
    	}

    	@Override
    	void raiseSalary(double increment) 	
	{
        	salary += increment * 1.2;
    	}
}

final class Manager extends Employee 
{
    	Manager(int id, String name, int age, double salary) 
	{
        	super(id, name, age, salary, "Manager");
    	}

   	@Override
    	void raiseSalary(double increment) 
	{
       		salary += increment * 1.5; 
   	}
}


public class EmployeeManagement 
{
		static int MAX_EMPLOYEES = 100; 
    	static Employee[] employees = new Employee[MAX_EMPLOYEES];
    	static int employeeCount = 0; 
   	static Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) 
	{
        	while (true) 
        	{
            		System.out.println("1. Create");
            		System.out.println("2. Display");
            		System.out.println("3. Raise Salary");
            		System.out.println("4. Remove");
            		System.out.println("5. Exit");
            		System.out.println("-------------------");
            		System.out.print("Enter choice: ");
            		int choice = scanner.nextInt();

            		switch (choice) 
            		{
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

	static void createEmployee()
	{
        	if (employeeCount >= MAX_EMPLOYEES) 
		{
            		System.out.println("Cannot add more employees. Maximum limit reached.");
            		return;
       		}
		int id;
        	while (true) 
		{
            		System.out.print("Enter ID: ");
            		id = scanner.nextInt();
            		if (isIdExists(id)) 
			{
                		System.out.println("Employee with ID " + id + " already exists. Please enter a different ID.");
            		} 
			else 
			{
               			break;
            		}
        	}
		scanner.nextLine(); 

       		System.out.print("Enter Name: ");
        	String name = scanner.nextLine();

        	System.out.print("Enter Age: ");
        	int age = scanner.nextInt();

        	System.out.print("Enter Salary: ");
        	double salary = scanner.nextDouble();

        	scanner.nextLine(); // Consume newline

        	System.out.println("Enter Designation (Clerk/Programmer/Manager): ");
        	String designation = scanner.nextLine();


		Employee employee;
        	switch (designation.toLowerCase()) 
		{
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

	static boolean isIdExists(int id) 
	{
        	for (int i = 0; i < employeeCount; i++) 
		{
            		if (employees[i].id == id) 
			{
               			return true;
           		}
        	}
        	return false;
    	}

	static void displayEmployee()
	{
		if(employeeCount==0)
		{
			System.out.println("No Employees");
			return;
		}
		for(int i=0;i<employeeCount;i++)
		{
			employees[i].display();
			System.out.println("-------------------");
		}
	}

	static void raiseEmployeeSalary()
	{
		System.out.print("Enter Employee ID to raise salary: ");
        	int id = scanner.nextInt();

        	for (int i = 0; i < employeeCount; i++) 
		{
            		if (employees[i].id == id) 
			{
                		System.out.print("Enter salary increment: ");
                		double increment = scanner.nextDouble();
                		employees[i].raiseSalary(increment);
                		return;
            		}
        	}

        	System.out.println("Employee with ID " + id + " not found.");
	}
	static void removeEmployee()
	{
		System.out.print("Enter Employee ID to remove: ");
        	int id = scanner.nextInt();
		for (int i = 0; i < employeeCount; i++) 
		{
            		if (employees[i].id == id) 
			{
                		employees[i].display();
                		System.out.print("Do you really want to remove this employee record (Y/N): ");
                		char confirm = scanner.next().charAt(0);

                		if (confirm == 'Y' || confirm == 'y') 
				{
                    
                    			for (int j = i; j < employeeCount - 1; j++) 
					{
                        			employees[j] = employees[j + 1];
                    			}

                    			employees[--employeeCount] = null; 
                    			System.out.println("Employee removed successfully!");
                		}
				else 
				{
                    			System.out.println("Employee record not removed.");
                		}
               		return;
            		}
        	}
	}

}
	