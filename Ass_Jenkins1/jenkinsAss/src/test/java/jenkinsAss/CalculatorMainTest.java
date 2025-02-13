package jenkinsAss;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculatorMainTest {

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}

	
	    @Test
	    public void testAddition() {
	        Calculator calc = new Calculator();
	        assertEquals(30, calc.add(10, 20));
	    }
}
