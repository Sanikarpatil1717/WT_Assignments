import java.util.*;
import java.util.stream.*;

class Student {
	private int rollNo;
	private String name;
	private int age;
	private int standard;
	private String school;
	private String gender;
	private double percentage;
	private Fees fees;

	public Student(int rollNo, String name, int age, int standard, String school, String gender, double percentage,
			Fees fees) {
		if (standard < 1 || standard > 10)
			throw new IllegalArgumentException("Standard must be between 1-10");
		if (age < 5 || age > 18)
			throw new IllegalArgumentException("Age must be between 5-18");

		this.rollNo = rollNo;
		this.name = name;
		this.age = age;
		this.standard = standard;
		this.school = school;
		this.gender = gender;
		this.percentage = percentage;
		this.fees = fees;
	}

	public int getStandard() {
		return standard;
	}

	public String getSchool() {
		return school;
	}

	public String getGender() {
		return gender;
	}

	public double getPercentage() {
		return percentage;
	}

	public int getAge() {
		return age;
	}

	public Fees getFees() {
		return fees;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "(" + name + ", Std " + standard + ", " + school + ", " + percentage + "%)";
	}
}

class Fees {
	private double totalFees;
	private double feesPaid;

	public Fees(double totalFees, double feesPaid) {
		this.totalFees = totalFees;
		this.feesPaid = feesPaid;
	}

	public double getFeesPending() {
		return totalFees - feesPaid;
	}

	public double getFeesPaid() {
		return feesPaid;
	}
}

public class StudentAnalysis {
	public static void main(String[] args) {
		List<Student> students = Arrays.asList(
				new Student(1, "Amit", 12, 5, "School A", "Male", 85.5, new Fees(50000, 45000)),
				new Student(2, "Priya", 10, 4, "School A", "Female", 72.0, new Fees(45000, 40000)),
				new Student(3, "Raj", 14, 8, "School B", "Male", 38.5, new Fees(55000, 30000)),
				new Student(4, "Simran", 13, 7, "School B", "Female", 92.0, new Fees(60000, 60000)),
				new Student(5, "Rahul", 15, 9, "School C", "Male", 67.5, new Fees(70000, 70000)),
				new Student(6, "Neha", 11, 6, "School C", "Female", 39.0, new Fees(65000, 50000)),
				new Student(7, "Karan", 17, 10, "School D", "Male", 78.0, new Fees(75000, 50000)),
				new Student(8, "Meera", 16, 10, "School D", "Female", 95.5, new Fees(80000, 80000)),
				new Student(9, "Suresh", 13, 7, "School A", "Male", 45.0, new Fees(52000, 45000)),
				new Student(10, "Rina", 9, 3, "School A", "Female", 88.0, new Fees(42000, 42000)),
				new Student(11, "Ankit", 11, 6, "School B", "Male", 33.5, new Fees(57000, 30000)),
				new Student(12, "Sunita", 12, 5, "School B", "Female", 94.5, new Fees(58000, 58000)),
				new Student(13, "Pooja", 14, 8, "School C", "Female", 89.0, new Fees(69000, 69000)),
				new Student(14, "Vikas", 10, 4, "School C", "Male", 72.5, new Fees(47000, 47000)),
				new Student(15, "Alok", 18, 10, "School D", "Male", 51.5, new Fees(76000, 50000)),
				new Student(16, "Jaya", 15, 9, "School D", "Female", 81.0, new Fees(73000, 73000)),
				new Student(17, "Ramesh", 16, 10, "School A", "Male", 90.0, new Fees(77000, 77000)),
				new Student(18, "Kavita", 17, 10, "School B", "Female", 92.5, new Fees(78000, 78000)),
				new Student(19, "Santosh", 14, 8, "School C", "Male", 55.0, new Fees(67000, 50000)),
				new Student(20, "Nikita", 13, 7, "School D", "Female", 48.5, new Fees(62000, 60000)),
				new Student(21, "Dinesh", 12, 5, "School A", "Male", 60.0, new Fees(51000, 45000)),
				new Student(22, "Sneha", 11, 6, "School B", "Female", 71.0, new Fees(53000, 53000)),
				new Student(23, "Mohit", 9, 3, "School C", "Male", 30.0, new Fees(40000, 25000)),
				new Student(24, "Sakshi", 8, 2, "School D", "Female", 84.0, new Fees(39000, 39000)),
				new Student(25, "Arjun", 10, 4, "School A", "Male", 41.0, new Fees(46000, 46000)));

		// 1. Students count in each standard
		Map<Integer, Long> m1 = students.stream()
				.collect(Collectors.groupingBy(e -> e.getStandard(), Collectors.counting()));
		System.out.println("Students in each standard: ");
		m1.forEach((k, v) -> System.out.println("Std" + k + "=" + v));
		System.out.println("-----------------------------------------------------------");

		// 2. Male & Female count
		Map<String, Long> m2 = students.stream()
				.collect(Collectors.groupingBy(e -> e.getGender(), Collectors.counting()));
		System.out.println("Male & Female count: " + m2);
		System.out.println("-----------------------------------------------------------");

		// 3. Pass & Fail count (Whole University)
		Map<Boolean, Long> m3 = students.stream()
				.collect(Collectors.partitioningBy(e -> e.getPercentage() >= 40, Collectors.counting()));
		System.out.println("Pass & Fail count (University): " + m3);
		System.out.println("-----------------------------------------------------------");

		// 4. Pass & Fail count (School-wise)
		Map<String, Map<Boolean, Long>> m4 = students.stream().collect(Collectors.groupingBy(e -> e.getSchool(),
				Collectors.partitioningBy(e -> e.getPercentage() >= 40, Collectors.counting())));
		System.out.println("Pass & Fail count (School-wise): " + m4);
		System.out.println("-----------------------------------------------------------");

		// 5. Top 3 students (University)
		List<Student> m5 = students.stream().sorted(Comparator.comparingDouble(e -> -e.getPercentage())).limit(3)
				.collect(Collectors.toList());
		System.out.println("Top 3 students: " + m5);
		System.out.println("-----------------------------------------------------------");

		// 6. Top scorer school-wise
		Map<String, Optional<Student>> m6 = students.stream().collect(Collectors.groupingBy(e -> e.getSchool(),
				Collectors.maxBy(Comparator.comparingDouble(e -> e.getPercentage()))));
		System.out.println("Top scorer school-wise: " + m6);
		System.out.println("-----------------------------------------------------------");

		// 7. Average age of Male & Female students
		Map<String, Double> m7 = students.stream()
				.collect(Collectors.groupingBy(e -> e.getGender(), Collectors.averagingInt(e -> e.getAge())));
		System.out.println("Average age (Male & Female): " + m7);
		System.out.println("-----------------------------------------------------------");

		// 8. Total fees collected school-wise
		Map<String, Double> m8 = students.stream().collect(
				Collectors.groupingBy(e -> e.getSchool(), Collectors.summingDouble(e -> e.getFees().getFeesPaid())));
		System.out.println("Total fees collected school-wise: " + m8);
		System.out.println("-----------------------------------------------------------");

		// 9. Total fees pending school-wise
		Map<String, Double> m9 = students.stream().collect(
				Collectors.groupingBy(e -> e.getSchool(), Collectors.summingDouble(e -> e.getFees().getFeesPending())));
		System.out.println("Total fees pending school-wise: " + m9);
		System.out.println("-----------------------------------------------------------");

		// 10. Total students in university
		System.out.println("Total students in university: " + students.size());
		System.out.println("-----------------------------------------------------------");

	}
}
