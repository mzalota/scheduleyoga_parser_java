package com.scheduleyoga.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.scheduleyoga.parser.Event;
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
	
	public void testGetDayDiff_sameDateTime() {
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 14, 00);
		Calendar cal2 = new GregorianCalendar(2011, 11, 1, 14, 00);
		
		//EXECUTE
		int daysDiff = Helper.createNew().getDayDiff(cal1.getTime(), cal2.getTime());
		
		//ASSERT
		assertEquals(0, daysDiff);
	}
	
	public void testGetDayDiff_sameDateDiffTime() {
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 8, 00);
		Calendar cal2 = new GregorianCalendar(2011, 11, 1, 14, 00);
		
		//EXECUTE
		int daysDiff = Helper.createNew().getDayDiff(cal1.getTime(), cal2.getTime());
		int daysDiffBackward = Helper.createNew().getDayDiff(cal2.getTime(), cal1.getTime());
		
		//ASSERT
		assertEquals(0, daysDiff);				
		assertEquals(0, daysDiffBackward);
	}
	
	public void testGetDayDiff_diffDate_moreThen24HourDiff() {
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 8, 00);
		Calendar cal2 = new GregorianCalendar(2011, 11, 2, 14, 00);
		
		//EXECUTE
		int daysDiff = Helper.createNew().getDayDiff(cal2.getTime(), cal1.getTime());
		
		//ASSERT
		assertEquals(1, daysDiff);
	}
	
	public void testGetDayDiff_diffDate_lessThen24HoursDiff() {
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 21, 00);
		Calendar cal2 = new GregorianCalendar(2011, 11, 2, 12, 00);
		
		//EXECUTE
		int daysDiff = Helper.createNew().getDayDiff(cal2.getTime(), cal1.getTime());
		
		//ASSERT
		assertEquals(1, daysDiff);
	}
	
	public void testGetDayDiff_twoDaysApart_moreThen48HoursDiff() {
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 14, 00);
		Calendar cal2 = new GregorianCalendar(2011, 11, 3, 19, 30);
		
		//EXECUTE
		int daysDiff = Helper.createNew().getDayDiff(cal2.getTime(), cal1.getTime());
		int daysDiffBackward = Helper.createNew().getDayDiff(cal1.getTime(), cal2.getTime());
		
		//ASSERT
		assertEquals(2, daysDiff);
		assertEquals(-2, daysDiffBackward);
	}
	
	public void testGetDayDiff_twoDaysApart_lessThen48HoursDiff() {
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 14, 00);
		Calendar cal2 = new GregorianCalendar(2011, 11, 3, 13, 30);
		
		//EXECUTE
		int daysDiff = Helper.createNew().getDayDiff(cal2.getTime(), cal1.getTime());
		int daysDiffBackward = Helper.createNew().getDayDiff(cal1.getTime(), cal2.getTime());
		
		//ASSERT
		assertEquals(2, daysDiff);
		assertEquals(-2, daysDiffBackward);
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
	

	public void testNextDateFromDayOfTheWeek_string_todayIsTue() throws ParseException {
		
		//SETUP
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date testTodayDate = (Date)formatter.parse("2012-05-29"); //"today" is Tuesday		

		String TEST_DAYOFWEEK_TUE = " TUESDAY "; 
		String expectedDate_TUE = "2012-05-29"; //This is Thu

		String TEST_DAYOFWEEK_THU = " THURSDAY "; 
		String expectedDate_THU = "2012-05-31"; //This is Thu

		String TEST_DAYOFWEEK_SUN = " SUNDAY "; 
		String expectedDate_SUN = "2012-06-03"; //This is Sun
		
		
		//EXECUTE
		Date result_tue = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_TUE, testTodayDate);
		Date result_thu = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_THU, testTodayDate);
		Date result_sun = Helper.createNew().nextDateFromDayOfTheWeek(TEST_DAYOFWEEK_SUN, testTodayDate);
		//ASSERT
		assertNotNull(result_tue);
		assertEquals(expectedDate_TUE, formatter.format(result_tue));

		//ASSERT
		assertNotNull(result_thu);
		assertEquals(expectedDate_THU, formatter.format(result_thu));
		
		//ASSERT
		assertNotNull(result_sun);
		assertEquals(expectedDate_SUN, formatter.format(result_sun));
		
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
	

	public void testSortEventsByStartTime_simpleTimesInAfternoon() {
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 14, 00); //2:00pm on Nov 1,2011 
		Calendar cal2 = new GregorianCalendar(2011, 11, 1, 13, 30); //1:30pm on Nov 1,2011
		
		Event event1 = Event.createNew();
		event1.setStartTime(cal1.getTime());
		Event event2 = Event.createNew();
		event2.setStartTime(cal2.getTime());
		
		List<Event> eventsList = new ArrayList<Event>(2);
		eventsList.add(0, event1);
		eventsList.add(1, event2);
		
		//EXECUTE
		List<Event> result = Helper.createNew().sortEventsByStartTime(eventsList);
		
		//ASSERT
		assertEquals(event2, result.get(0));
		assertEquals(event1, result.get(1));
	}	
	
	public void testNameToURLName_variousWhiteSpaces() {
		
		//SETUP
		String TEST_STR = " ( Rachael  	  \n O'Lappen  \n ] ";
		
		//EXECUTE
		String result = Helper.createNew().nameToURLName(TEST_STR);
		
		//ASSERT
		assertEquals("rachael-olappen", result);
	}	
	
	public void testCleanUpInstructorName() {
		
		//SETUP		
		String result;
		
		//EXECUTE		
		result = Helper.createNew().cleanUpInstructorName(" eli ");
		assertEquals("Eli", result);
		
		
				
		result = Helper.createNew().cleanUpInstructorName(" Carrie   Gaynor@ 800 Ayrault Rd. ");
		assertEquals("Carrie Gaynor", result);
		
		//If name contains dashes, all names should be capitalized
		result = Helper.createNew().cleanUpInstructorName(" Jeanne-Marie Derrick ");
		assertEquals("Jeanne-Marie Derrick", result);
		
		//If name contains apostrofies, both sides should be capitalized
		result = Helper.createNew().cleanUpInstructorName(" T'ai Jamar Hanna ");
		assertEquals("T'Ai Jamar Hanna", result);					
		
		result = Helper.createNew().cleanUpInstructorName(" * * ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" Community   Class  teacher ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" 1 hour & 15 minutes ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName("45 Minutes");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName("Constanza Roldan / Arthur Roldan");
		assertEquals("Constanza Roldan", result);
		
		result = Helper.createNew().cleanUpInstructorName("Barbara & Kristin");
		assertEquals("Barbara", result);
				
		result = Helper.createNew().cleanUpInstructorName(" Eastern Calisthenics/Body Sculpting ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName("Wendy/Santoshi Baranello");
		assertEquals("Wendy", result);
		
		result = Helper.createNew().cleanUpInstructorName("GOLDEN BRIDGE STAFF");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" Staff ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" Staff Instructor ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" Teacher Independent ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" Yoga Collective ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" TBD ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" TBA ");
		assertNull(result);

		//exclude if contains word "class" at the end
		result = Helper.createNew().cleanUpInstructorName(" GYROTONIC® Group Class ");
		assertNull(result);
		
		//exclude if contains word "class" in the middle
		result = Helper.createNew().cleanUpInstructorName(" Dharma yoga class for beginners ");
		assertNull(result);
		
		//exclude if contains word "level"
		result = Helper.createNew().cleanUpInstructorName(" GYROKINESIS® Level II ");
		assertNull(result);
		
		//exclude if contains phrase "Body Sculpting"
		result = Helper.createNew().cleanUpInstructorName(" Eastern Calisthenics/Body Sculpting ");
		assertNull(result);
		
		//exclude if contains phrase "for Adults"
		result = Helper.createNew().cleanUpInstructorName(" Shanti Yoga for Adults ");
		assertNull(result);
				
		//exclude if contains phrase "Vinyasa"
		result = Helper.createNew().cleanUpInstructorName(" Shanti Sweat-it-Out Vinyasa-Style ");
		assertNull(result);
		
		//exclude if starts with word "Yoga"
		result = Helper.createNew().cleanUpInstructorName(" Yoga Tune Up® ");
		assertNull(result);
		
		//exclude if its word "open" or "rest"
		result = Helper.createNew().cleanUpInstructorName(" Open ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" Rest ");
		assertNull(result);
		
		//exclude if ends with word "Basics"
		result = Helper.createNew().cleanUpInstructorName(" Budokon Basics ");
		assertNull(result);
		
		//exclude if contains word "Lunchtime"
		result = Helper.createNew().cleanUpInstructorName(" Lunchtime  Yoga Flow ");
		assertNull(result);
		
		//exclude if contains phrase "Community Yoga"
		result = Helper.createNew().cleanUpInstructorName(" Community Yoga ");
		assertNull(result);
		
		//exclude if contains phrase "Strength Yoga"
		result = Helper.createNew().cleanUpInstructorName(" Strength Yoga ");
		assertNull(result);
		
		//exclude if contains phrase "Hot Yoga"
		result = Helper.createNew().cleanUpInstructorName(" Traditional Hot Yoga ");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName(" Illuminated Journey Grads ");
		assertNull(result);
		
		//Yoga Center
		result = Helper.createNew().cleanUpInstructorName("Sivananda Yoga Centers NY");
		assertNull(result);
		
		result = Helper.createNew().cleanUpInstructorName("DY Teacher");
		assertNull(result);			
		
		result = Helper.createNew().cleanUpInstructorName(" Anna M ");
		assertNull(result);	
		
		//If name contains dot at the end, strip it 
		result = Helper.createNew().cleanUpInstructorName(" Mariyah X. ");
		assertNull(result);	
		
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