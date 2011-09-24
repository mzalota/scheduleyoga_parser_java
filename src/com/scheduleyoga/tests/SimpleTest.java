package com.scheduleyoga.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.scheduleyoga.parser.Helper;
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
		boolean result = Helper.createNew().containsTime(TEST_TIME);
		
		//ASSERT
		assertTrue(result);
	}
	
	public void testContainsTime_basicWithMinutes() {
		
		//SETUP
		String TEST_TIME = "   4:30PM  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = Helper.createNew().containsTime(TEST_TIME);
		
		//ASSERT
		assertTrue(result);
	}
	

	public void testContainsTime_startAndEndTime() {
		
		//SETUP
		String TEST_TIME = "  3:00 - 4:30PM  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = Helper.createNew().containsTime(TEST_TIME);
		
		//ASSERT
		assertTrue(result);
	}	


	public void testContainsTime_hourMinutesAmerican() {
		
		//SETUP
		String TEST_DATE = "  4:30 PM  ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = Helper.createNew().containsTime(TEST_DATE);
		
		//ASSERT
		assertTrue(result);
	}
	
	public void testContainsTime_hourMinutesAmerican_noSuffix() {
		
		//SETUP
		String TEST_DATE = "  4:15 ";
		
		//EXECUTE
		Parser parser = new Parser();
		boolean result = Helper.createNew().containsTime(TEST_DATE);
		
		//ASSERT
		assertTrue(result);
	}
	
	
	public void testContainsDate_hasYear() {
		
		//SETUP
		String TEST_DATE = "  Sun May 15, 2011  ";
		
		//EXECUTE
		boolean result = Helper.createNew().containsDate(TEST_DATE);
		
		//ASSERT
		assertTrue(result);
	}	
	
	public void testContainsDate_hasDayOfWeek() {
		
		//SETUP
		String TEST_DATE_1 = "  Sun  ";
		String TEST_DATE_2 = "  Tuesday  ";
		
		//EXECUTE
		boolean result1 = Helper.createNew().containsDate(TEST_DATE_1);
		boolean result2 = Helper.createNew().containsDate(TEST_DATE_2);
		
		//ASSERT
		assertTrue(result1);
		assertTrue(result2);
	}	
	
	
	public void testnextDateFromDayOfTheWeek_todayIsThu() throws ParseException {
		
		//SETUP
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date testTodayDate = (Date)formatter.parse("2011-08-11"); //"today" is Thursday		

		int TEST_DAYOFWEEK_SUN = Calendar.SUNDAY; //Sunday
		String expectedDate_SUN = "2011-08-14"; //This is Sunday
		
		int TEST_DAYOFWEEK_THU = Calendar.THURSDAY; 
		String expectedDate_THU = "2011-08-11"; 
		
		int TEST_DAYOFWEEK_MON = Calendar.MONDAY; 
		String expectedDate_MON = "2011-08-15"; 
		
		int TEST_DAYOFWEEK_FRI = Calendar.FRIDAY; 
		String expectedDate_FRI = "2011-08-12"; 
		
		//EXECUTE
		Parser parser = new Parser();
		Date result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_SUN, testTodayDate);
		
		//ASSERT
		assertNotNull(result);
		assertEquals(expectedDate_SUN, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_THU, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_THU, formatter.format(result));
		
		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_MON, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_MON, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_FRI, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_FRI, formatter.format(result));
	}
	
	public void testnextDateFromDayOfTheWeek_todayIsMon() throws ParseException {
		
		//SETUP
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date testTodayDate = (Date)formatter.parse("2011-08-08"); //"today" is Monday		

		int TEST_DAYOFWEEK_SUN = Calendar.SUNDAY; //Sunday
		String expectedDate_SUN = "2011-08-14"; //This is Sunday
		
		int TEST_DAYOFWEEK_THU = Calendar.THURSDAY; 
		String expectedDate_THU = "2011-08-11"; 
		
		int TEST_DAYOFWEEK_MON = Calendar.MONDAY; 
		String expectedDate_MON = "2011-08-08"; 
		
		int TEST_DAYOFWEEK_FRI = Calendar.FRIDAY; 
		String expectedDate_FRI = "2011-08-12"; 
		
		//EXECUTE
		Parser parser = new Parser();
		Date result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_SUN, testTodayDate);
		
		//ASSERT
		assertNotNull(result);
		assertEquals(expectedDate_SUN, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_THU, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_THU, formatter.format(result));
		
		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_MON, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_MON, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_FRI, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_FRI, formatter.format(result));
	}
	
	
	public void testnextDateFromDayOfTheWeek_todayIsSun() throws ParseException {
		
		//SETUP
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date testTodayDate = (Date)formatter.parse("2011-08-07"); //"today" is Sunday		

		int TEST_DAYOFWEEK_SUN = Calendar.SUNDAY; //Sunday
		String expectedDate_SUN = "2011-08-07"; //This is Sunday
		
		int TEST_DAYOFWEEK_THU = Calendar.THURSDAY; 
		String expectedDate_THU = "2011-08-11"; 
		
		int TEST_DAYOFWEEK_MON = Calendar.MONDAY; 
		String expectedDate_MON = "2011-08-08"; 
		
		int TEST_DAYOFWEEK_FRI = Calendar.FRIDAY; 
		String expectedDate_FRI = "2011-08-12"; 
		
		//EXECUTE
		Parser parser = new Parser();
		Date result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_SUN, testTodayDate);
		
		//ASSERT
		assertNotNull(result);
		assertEquals(expectedDate_SUN, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_THU, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_THU, formatter.format(result));
		
		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_MON, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_MON, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_FRI, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_FRI, formatter.format(result));
	}	
	
	
	public void testnextDateFromDayOfTheWeek_todayIsSat() throws ParseException {
		
		//SETUP
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date testTodayDate = (Date)formatter.parse("2011-08-06"); //"today" is Sunday		

		int TEST_DAYOFWEEK_SUN = Calendar.SUNDAY; //Sunday
		String expectedDate_SUN = "2011-08-07"; //This is Sunday
		
		int TEST_DAYOFWEEK_THU = Calendar.THURSDAY; 
		String expectedDate_THU = "2011-08-11"; 
		
		int TEST_DAYOFWEEK_MON = Calendar.MONDAY; 
		String expectedDate_MON = "2011-08-08"; 
		
		int TEST_DAYOFWEEK_FRI = Calendar.FRIDAY; 
		String expectedDate_FRI = "2011-08-12"; 
		
		int TEST_DAYOFWEEK_SAT = Calendar.SATURDAY; 
		String expectedDate_SAT = "2011-08-06"; 
		
		//EXECUTE
		Parser parser = new Parser();
		Date result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_SUN, testTodayDate);
		
		//ASSERT
		assertNotNull(result);
		assertEquals(expectedDate_SUN, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_THU, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_THU, formatter.format(result));
		
		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_MON, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_MON, formatter.format(result));

		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_FRI, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_FRI, formatter.format(result));
		
		result = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_SAT, testTodayDate);
		assertNotNull(result);
		assertEquals(expectedDate_SAT, formatter.format(result));
		
	}	
	
	public void testConvertSimpleTimeStrToDate_earlyMorning() {
		
		//Setup
		SimpleDateFormat formatterTime = new SimpleDateFormat("h:mm"); //in 12 hour format
		SimpleDateFormat formatterAmPm = new SimpleDateFormat("a"); //in 12 hour format
		String TEST_TIME = "6:00";
		
		//Execute
		Parser parser = new Parser();
		Date resultDate = Helper.createNew().deduceDateFromSimpleTimeString(TEST_TIME, false);
		
		//Assert
		assertNotNull(resultDate);
		assertEquals(TEST_TIME, formatterTime.format(resultDate));
		assertEquals("AM", formatterAmPm.format(resultDate));
	}
	
	public void testConvertSimpleTimeStrToDate_earlyAfternoon_ampmParamIsTrue() {
		
		//Setup
		SimpleDateFormat formatterTime = new SimpleDateFormat("h:mm"); //in 12 hour format
		SimpleDateFormat formatterAmPm = new SimpleDateFormat("a"); //in 12 hour format
		String TEST_TIME = "1:00";
		
		//Execute
		Parser parser = new Parser();
		Date resultDate = Helper.createNew().deduceDateFromSimpleTimeString(TEST_TIME, true);
		
		//Assert
		assertNotNull(resultDate);
		assertEquals(TEST_TIME, formatterTime.format(resultDate));
		assertEquals("PM", formatterAmPm.format(resultDate));
	}	
	
	public void testConvertSimpleTimeStrToDate_earlyAfternoon_ampmParamIsFalse() {
		
		//Setup
		SimpleDateFormat formatterTime = new SimpleDateFormat("h:mm"); //in 12 hour format
		SimpleDateFormat formatterAmPm = new SimpleDateFormat("a"); //in 12 hour format
		String TEST_TIME = "1:00";
		
		//Execute
		Parser parser = new Parser();
		Date resultDate = Helper.createNew().deduceDateFromSimpleTimeString(TEST_TIME, false);
		
		//Assert
		assertNotNull(resultDate);
		assertEquals(TEST_TIME, formatterTime.format(resultDate));
		assertEquals("PM", formatterAmPm.format(resultDate));
	}		

	public void testConvertSimpleTimeStrToDate_afternoon_ampmParamIsFalse() {
		
		//Setup
		SimpleDateFormat formatterTime = new SimpleDateFormat("h:mm"); //in 12 hour format
		SimpleDateFormat formatterAmPm = new SimpleDateFormat("a"); //in 12 hour format
		String TEST_TIME = "4:30";
		
		//Execute
		Parser parser = new Parser();
		Date resultDate = Helper.createNew().deduceDateFromSimpleTimeString(TEST_TIME, false);
		
		//Assert
		assertNotNull(resultDate);
		assertEquals(TEST_TIME, formatterTime.format(resultDate));
		assertEquals("PM", formatterAmPm.format(resultDate));
	}	
	
	
	public void testConvertSimpleTimeStrToDate_lateAfternoon_ampmParamIsTrue() {
		
		//Setup
		SimpleDateFormat formatterTime = new SimpleDateFormat("h:mm"); //in 12 hour format
		SimpleDateFormat formatterAmPm = new SimpleDateFormat("a"); //in 12 hour format
		String TEST_TIME = "6:00";
		
		//Execute
		Parser parser = new Parser();
		Date resultDate = Helper.createNew().deduceDateFromSimpleTimeString(TEST_TIME, true);
		
		//Assert
		assertNotNull(resultDate);
		assertEquals(TEST_TIME, formatterTime.format(resultDate));
		assertEquals("PM", formatterAmPm.format(resultDate));
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