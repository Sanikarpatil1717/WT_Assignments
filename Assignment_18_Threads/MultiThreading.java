class A 
{
	public void print1to10() 
	{
		for (int i = 1; i <= 10; i++) 
		{
			System.out.println("Range : " + i);
		}
	}
}

class B 
{
	public void evenTill50() 
	{
		for (int i = 0; i <= 50; i += 2) 
		{
			System.out.println("Even : " + i);
		}
	}
}

class C 
{
	public void fibonacciFrom1to1000() 
	{
		int a = 1, b = 1;
		while (a <= 1000) 
		{
			System.out.println("Fibonacci : " + a);
			int temp = a + b;
			a = b;
			b = temp;
		}
	}
}

public class MultiThreading 
{
	public static void main(String args[]) 
	{
		// Thread for Class A
		Thread threadA = new Thread(new Runnable() {
			public void run() {
				new A().print1to10();
			}
		});

		// Thread for Class B
		Thread threadB = new Thread(new Runnable() {
			public void run() {
				new B().evenTill50();
			}
		});

		// Thread for Class C
		Thread threadC = new Thread(new Runnable() {
			public void run() {
				new C().fibonacciFrom1to1000();
			}
		});

		// Start all threads
		threadA.start();
		threadB.start();
		threadC.start();
	}
}


--------
en