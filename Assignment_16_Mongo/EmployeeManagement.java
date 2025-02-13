import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.Scanner;

public class EmployeeManagement {
    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase database = mongoClient.getDatabase("EmployeeDB");
    private static final MongoCollection<Document> collection = database.getCollection("Employee");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nEmployee Management System");
            System.out.println("1. Add Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Appraisal (Update Salary)");
            System.out.println("5. Remove Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createEmployee(scanner);
                    break;
                case 2:
                    displayEmployees();
                    break;
                case 3:
                    searchEmployee(scanner);
                    break;
                case 4:
                    appraisal(scanner);
                    break;
                case 5:
                    removeEmployee(scanner);
                    break;
                case 6:
                    mongoClient.close();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createEmployee(Scanner scanner) {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Designation: ");
        String designation = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();

        Document employee = new Document("name", name)
                .append("age", age)
                .append("salary", salary)
                .append("designation", designation)
                .append("department", department);

        collection.insertOne(employee);
        System.out.println("Employee added successfully!");
    }

    private static void displayEmployees() {
        System.out.println("\nEmployee List:");
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }

    private static void searchEmployee(Scanner scanner) {
        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();

        FindIterable<Document> result = collection.find(Filters.eq("name", name));
        boolean found = false;
        for (Document doc : result) {
            System.out.println(doc.toJson());
            found = true;
        }
        if (!found) {
            System.out.println("Employee not found.");
        }
    }

    private static void appraisal(Scanner scanner) {
        System.out.print("Enter Employee Name for Appraisal: ");
        String name = scanner.nextLine();
        System.out.print("Enter Appraisal Amount: ");
        double appraisalAmount = scanner.nextDouble();

        collection.updateOne(Filters.eq("name", name), Updates.inc("salary", appraisalAmount));
        System.out.println("Salary updated successfully!");
    }

    private static void removeEmployee(Scanner scanner) {
        System.out.print("Enter Employee Name to Remove: ");
        String name = scanner.nextLine();

        collection.deleteOne(Filters.eq("name", name));
        System.out.println("Employee removed successfully!");
    }
}
