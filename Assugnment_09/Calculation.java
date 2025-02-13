
import java.lang.reflect.Method;
import java.util.Scanner;

class Calculator 
{
	public int add(int a, int b) 
	{
		return a + b;
	}

	public int sub(int a, int b) 
	{
		return a - b;
	}

	public int mul(int a, int b) 
	{
		return a * b;
	}

	public int div(int a, int b) 
	{
		return a / b;
	}
}

class Calculation 
{
	public static void main(String[] args) 
	{
		try 
		{
			Scanner scanner = new Scanner(System.in);
			
			// Get user inputs
			System.out.print("Enter operation (add/sub/mul/div) : ");
			String operation = scanner.next();

			System.out.print("Enter parameter 1: ");
			int param1 = scanner.nextInt();

			System.out.print("Enter parameter 2: ");
			int param2 = scanner.nextInt();

			
			Calculator calculator = new Calculator();

			Method method = Calculator.class.getMethod(operation, int.class, int.class);

			
			int result = (int) method.invoke(calculator, param1, param2);

			
			System.out.println("Result: " + result);
		} 
		catch (Exception e) {
			System.out.println("An error occurred: " + e);
		}
	}
}
