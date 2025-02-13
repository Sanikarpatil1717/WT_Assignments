class SingletonInstanceException extends RuntimeException 
{
    	public SingletonInstanceException(String message) 
	{
        	super(message);
    	}
}

class A 
{
        private static A instance;

        private A() {}

        public static A getObject() 
	{
        	if (instance == null) 
		{
            		instance = new A();
        	} 
		else 
		{
            		throw new SingletonInstanceException("Cannot create more than one instance of class A");
        	}
        	return instance;
    	}
}

class B extends A 
{
        private static B instance;

        private B() {}

        public static B getObject() 
	{
        	if (instance == null) 
		{
            		instance = new B();
        	} 
		else 
		{
            		throw new SingletonInstanceException("Cannot create more than one instance of class B");
        	}
        	return instance;
    	}
}

public class SingletonAssignment 
{
    	public static void main(String[] args) 
	{
       
        	try 
		{
            // Create first instance of A and B
            		A a1 = A.getObject();
            		B b1 = B.getObject();

            // Attempting to create additional instances
            		A a2 = A.getObject();
        	} 
		catch (SingletonInstanceException e) 
		{
            		System.out.println(e.getMessage());
		}
        }
    }
}
