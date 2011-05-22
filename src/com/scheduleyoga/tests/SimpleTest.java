package com.scheduleyoga.tests;

import com.scheduleyoga.parser.Parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Some simple tests.
 *
 */
public class SimpleTest extends TestCase {
	protected int fValue1;
	protected int fValue2;

	@Override
	protected void setUp() {
		fValue1= 2;
		fValue2= 3;
	}
	public static Test suite() {

		/*
		 * the type safe way
		 *
		TestSuite suite= new TestSuite();
		suite.addTest(
			new SimpleTest("add") {
				 protected void runTest() { testAdd(); }
			}
		);

		suite.addTest(
			new SimpleTest("testDivideByZero") {
				 protected void runTest() { testDivideByZero(); }
			}
		);
		return suite;
		*/

		/*
		 * the dynamic way
		 */
		return new TestSuite(SimpleTest.class);
	}
	public void testContainsTime_basicAmericanTime() {
		
		//SETUP
		String TEST_TIME = "   4PM  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = parser.containsTime(TEST_TIME);
		
		//ASSERT
		assertTrue(result);
	}
	
	public void testContainsTime_basicWithMinutes() {
		
		//SETUP
		String TEST_TIME = "   4:30PM  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = parser.containsTime(TEST_TIME);
		
		//ASSERT
		assertTrue(result);
	}
	

	public void testContainsTime_startAndEndTime() {
		
		//SETUP
		String TEST_TIME = "  3:00 - 4:30PM  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = parser.containsTime(TEST_TIME);
		
		//ASSERT
		assertTrue(result);
	}	


	public void testContainsTime_hourMinutesAmerican() {
		
		//SETUP
		String TEST_DATE = "  4:30 PM  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = parser.containsTime(TEST_DATE);
		
		//ASSERT
		assertTrue(result);
	}
	
	public void testContainsDate_hasYear() {
		
		//SETUP
		String TEST_DATE = "  Sun May 15, 2011  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = parser.containsDate(TEST_DATE);
		
		//ASSERT
		assertTrue(result);
	}	
	
	public void testContainsDate_hasDayOfWeek() {
		
		//SETUP
		String TEST_DATE_1 = "  Sun  ";
		String TEST_DATE_2 = "  Tuesday  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result1 = parser.containsDate(TEST_DATE_1);
		boolean result2 = parser.containsDate(TEST_DATE_2);
		
		//ASSERT
		assertTrue(result1);
		assertTrue(result2);
	}	
	
	
	//	public void testDivideByZero() {
//		int zero= 0;
//		int result= 8/zero;
//		result++; // avoid warning for not using result
//	}
//	public void testEquals() {
//		assertEquals(12, 12);
//		assertEquals(12L, 12L);
//		assertEquals(new Long(12), new Long(12));
//
//		assertEquals("Size", 12, 13);
//		assertEquals("Capacity", 12.0, 11.99, 0.0);
//	}

}