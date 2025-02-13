//exception handling for resource oriented programming use try with resources
class A implements AutoCloseable
{
	A()
	{
		System.out.println("Allocating res for A class obj");
	}
	public void process()
	{
		System.out.println("Perforing req op on A class obj");
	}
	//compulsory code for release written in close() method
	public void close()
	{
		System.out.println("Releasing res for A class obj");
	}
}
class B implements AutoCloseable //AutoCloseable is imp then only we can use try(----)
{
	B()
	{
		System.out.println("Allocating res for B class obj");
	}
	public void process()
	{
		System.out.println("Perforing req op on B class obj");
	}
	//compulsory code for release written in close() method
	public void close()
	{
		System.out.println("Releasing res for B class obj");
	}
}
public class TryWithResources
{
	public static void main(String args[])
	{
		
		try(A a1=new A(); B b1=new B();)//relese done in reverse order so this seq is imp
		{
			b1.process();
			if(true)
				//return;
				throw new NullPointerException();
			a1.process();	
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		//System.out.println(a1);//not available outside
	}
}
/*
public class TryWithResources
{
	public static void main(String args[])
	{
		A a1=null;
		try
		{
			a1=new A();
			a1.process();
			if(true)
				throw new NullPointerException();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			a1.release();
		}
		System.out.println(a1);
	}
}
*/