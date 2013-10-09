package com.scheduleyoga.tests;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;

import com.scheduleyoga.parser.CalendarRenderer;
import com.scheduleyoga.parser.Event;
import com.scheduleyoga.parser.Helper;
import com.scheduleyoga.parser.Parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import org.junit.matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//import org.hamcrest.Description;
//import org.hamcrest.Factory;
//import org.hamcrest.TypeSafeMatcher;


import static org.mockito.Mockito.*;

//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matcher;
//import static org.hamcrest.MatcherAssert.assertThat;

//import static org.hamcrest.CoreMatchers.*;


/**
 * Some simple tests.
 *
 */

@RunWith(PowerMockRunner.class)
//@PrepareForTest( { YourClassWithEgStaticMethod.class })
public class CalendarRendererTest extends TestCase {
	protected int fValue1;
	protected int fValue2;

	@Override
	protected void setUp() {
		fValue1= 2;
		fValue2= 3;
	}
	public static Test suite() {
		return new TestSuite(CalendarRendererTest.class);
	}
	
	public void testCreateNew() {
		
		//SETUP
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();	
		
		//ASSERT
		assertNotNull(renderer);
	}
	
	public void testCountEvents() {
		
		//SETUP
		CalendarRenderer renderer = CalendarRenderer.createNew();
		
		//EXECUTE
			
		
		//ASSERT
		assertEquals(0,renderer.countEvents());
	}
	
	public void testAddEvent_addNull() {
		
		//SETUP
		CalendarRenderer renderer = CalendarRenderer.createNew();
		
		//EXECUTE
		renderer.addEvent(null);	
		
		//ASSERT
		assertEquals(0,renderer.countEvents());
	}
	
	public void testAddEvent_addOneEvent() {
		
		//SETUP
		CalendarRenderer renderer = CalendarRenderer.createNew();
		Event mockEvent= mock(Event.class);
		
		//EXECUTE
		renderer.addEvent(mockEvent);	
		
		//ASSERT
		assertEquals(1,renderer.countEvents());
	}	
	
	
	public void testGetAllDistinctDates_oneDate() {
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 13, 30);
		
		
		Event event = Event.createNew();
		event.setStartTime(cal1.getTime());
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event);
		SortedSet<Date> result = renderer.getAllDistinctDates();
		
		//ASSERT
		assertNotNull(result);
		assertEquals(1, result.size());		
	}
	
	public void testGetAllDistinctDates_twoDates() {
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 13, 30);
		Calendar cal2 = new GregorianCalendar(2011, 11, 3, 14, 45);
		
		Event event1 = Event.createNew();
		event1.setStartTime(cal1.getTime());

		Event event2 = Event.createNew();
		event2.setStartTime(cal2.getTime());

		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event1);
		renderer.addEvent(event2);
		SortedSet<Date> result = renderer.getAllDistinctDates();
		
		//ASSERT
		assertNotNull(result);
		assertEquals(2, result.size());		
	}
	
	public void testGetFormatedDates_4days() {
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 13, 30);

		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		List<Date> result = renderer.getFormattedDates(cal1.getTime(), 4);
		
		//ASSERT
		assertNotNull(result);
		assertEquals(4, result.size());		
	}
	
	public void testGetAllDistinctTimes_twoDates() {
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 6, 45);
		Calendar cal2 = new GregorianCalendar(2011, 11, 3, 18, 30);
		
		Event event1 = Event.createNew();
		event1.setStartTime(cal1.getTime());

		Event event2 = Event.createNew();
		event2.setStartTime(cal2.getTime());

		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event1);
		renderer.addEvent(event2);
		SortedSet<Date> result = renderer.getAllDistinctTimes();
				
		//ASSERT
		assertNotNull(result);
		assertEquals(2, result.size());
		assert(result.first().compareTo(result.last())<0);
		
		String time1 = new SimpleDateFormat("HH:mm").format(result.first());
		String time2 = new SimpleDateFormat("HH:mm").format(result.last());
		
//		System.out.println("calnedar 1 is "+cal1);
//		System.out.println("calnedar 2 is "+cal2);
//		System.out.println("event1 startTime "+event1.getStartTime());
//		System.out.println("event2 startTime "+event2.getStartTime());
//		System.out.println("resuilt is "+result);
//		System.out.println("tim1 is "+time1+" and time2 is: "+time2);
		
		assertThat(time1, containsString("06:45"));
		assertThat(time2, containsString("18:30"));
	}
	

	public void testGetEventsDateAndTime_twoDifferentTimes() {
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 6, 45);
		Calendar cal1Date = new GregorianCalendar(2011, 11, 1);
		Calendar cal1Time = new GregorianCalendar(1976, 4, 23, 6, 45);
		
		Calendar cal2 = new GregorianCalendar(2011, 11, 3, 18, 30);
		Calendar cal2Date = new GregorianCalendar(2011, 11, 3);
		Calendar cal2Time = new GregorianCalendar(1956, 10, 13, 18, 30);
		
		Event event1 = Event.createNew();
		event1.setStartTime(cal1.getTime());

		Event event2 = Event.createNew();
		event2.setStartTime(cal2.getTime());
		
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event1);
		renderer.addEvent(event2);
				
		//ASSERT
		List<Event> result = renderer.getEventsDateAndTime(cal1Date.getTime(),cal1Time.getTime());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(event1, result.get(0));
		
		result = renderer.getEventsDateAndTime(cal2Date.getTime(),cal2Time.getTime());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(event2, result.get(0));
	}	
	
	public void testGetEventsDateAndTime_differentDatesSameTimes() {
		int TIME_HOUR = 13;
		int TIME_MINUTE = 45;
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, TIME_HOUR, TIME_MINUTE);
		Calendar cal1Date = new GregorianCalendar(2011, 11, 1);
		Calendar cal1Time = new GregorianCalendar(1976, 4, 23, TIME_HOUR, TIME_MINUTE);
		
		Calendar cal2 = new GregorianCalendar(2011, 11, 3, TIME_HOUR, TIME_MINUTE);
		Calendar cal2Date = new GregorianCalendar(2011, 11, 3);
		Calendar cal2Time = new GregorianCalendar(1956, 10, 13, TIME_HOUR, TIME_MINUTE);
		
		Event event1 = Event.createNew();
		event1.setStartTime(cal1.getTime());

		Event event2 = Event.createNew();
		event2.setStartTime(cal2.getTime());
		
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event1);
		renderer.addEvent(event2);
				
		//ASSERT
		List<Event> result = renderer.getEventsDateAndTime(cal1Date.getTime(),cal1Time.getTime());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(event1, result.get(0));
		
		result = renderer.getEventsDateAndTime(cal2Date.getTime(),cal2Time.getTime());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(event2, result.get(0));
	}	
	
	public void testGetEventsDateAndTime_sameDatesSameTimes() {
		int TIME_HOUR = 16;
		int TIME_MINUTE = 30;
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, TIME_HOUR, TIME_MINUTE);
		Calendar cal1Date = new GregorianCalendar(2011, 11, 1);
		Calendar cal1Time = new GregorianCalendar(1976, 4, 23, TIME_HOUR, TIME_MINUTE);
		
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime((Date)cal1.getTime().clone());
		
		Event event1 = Event.createNew();
		event1.setStartTime(cal1.getTime());
		event1.setComment("blabla Event1");

		Event event2 = Event.createNew();
		event2.setStartTime(cal2.getTime());
		event2.setComment("blabla Event2");
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event1);
		renderer.addEvent(event2);
				
		//ASSERT
		List<Event> result = renderer.getEventsDateAndTime(cal1Date.getTime(),cal1Time.getTime());
		assertNotNull(result);
		assertEquals(2, result.size());
		
		assertThat(result, hasItems(event1));
		assertThat(result, hasItems(event2));
	}	
	
	public void testGetEventsDateAndTime_differentSeconds() {

		int SECONDS1 = 34;		
		int SECONDS2 = 5;
		
		int MILLISECONDS1 = 234;
		int MILLISECONDS2 = 342;
		
		//SETUP
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 6, 45, SECONDS1);
		cal1.set(Calendar.MILLISECOND, MILLISECONDS1);
		
		Calendar cal1Date = new GregorianCalendar(2011, 11, 1);
		Calendar cal1Time = new GregorianCalendar(1976, 4, 23, 6, 45, SECONDS2);
		cal1Time.set(Calendar.MILLISECOND, MILLISECONDS2);
		
		Event event1 = Event.createNew();
		event1.setStartTime(cal1.getTime());

		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event1);
				
		//ASSERT
		List<Event> result = renderer.getEventsDateAndTime(cal1Date.getTime(),cal1Time.getTime());
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(event1, result.get(0));
	}	
	
	
//	public void testRender_oneEvent() {
//		//SETUP
//		String TEST_CLASS_NAME = "ClassName";
//		CalendarRenderer renderer = CalendarRenderer.createNew();
//		Event mockEvent= mock(Event.class);
//		when(mockEvent.getComment()).thenReturn(TEST_CLASS_NAME);		
//		
//		//EXECUTE
//		renderer.addEvent(mockEvent);	
//		String result = renderer.render(new Date(),1);
//		//ASSERT
//		assertTrue(result.contains(TEST_CLASS_NAME));		
//	}
	
//	public void testRender_twoEvents() {
//		//SETUP
//		String TEST_CLASS_NAME1 = "ClassName1";
//		String TEST_CLASS_NAME2 = "ClasNam2";
//		
//		Event mockEvent1= mock(Event.class);
//		when(mockEvent1.getComment()).thenReturn(TEST_CLASS_NAME1);		
//		
//		Event mockEvent2= mock(Event.class);
//		when(mockEvent2.getComment()).thenReturn(TEST_CLASS_NAME2);
//		
//		//EXECUTE
//		CalendarRenderer renderer = CalendarRenderer.createNew();
//		renderer.addEvent(mockEvent1);
//		renderer.addEvent(mockEvent2);
//		String result = renderer.render(new Date(),1);
//		
//		//ASSERT
//		assertTrue(result.contains(TEST_CLASS_NAME1));
//		assertTrue(result.contains(TEST_CLASS_NAME2));
//	}
	
	public void testRender_containsStartTime() {
		//SETUP
		String YOGA_CLASS_NAME = "ClassName1";
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 13, 30);
		
		
		Event event = Event.createNew();
		event.setComment(YOGA_CLASS_NAME);
		event.setStartTime(cal1.getTime());
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event);
		String result = renderer.render(cal1.getTime(),1);
		
		//ASSERT
		assertThat(result, containsString(YOGA_CLASS_NAME));
		assertThat(result.toLowerCase(), containsString("01:30 pm")); //containsItemWith
	}
	
	public void testRender_containsStartTimeForTwoEvents() {
		//SETUP
		String YOGA_CLASS_NAME1 = "ClassName1";		
		Calendar cal1 = new GregorianCalendar(2011, 11, 1, 13, 30);
		
		String YOGA_CLASS_NAME2 = "ClasNam2";
		Calendar cal2 = new GregorianCalendar(2011, 11, 1, 20, 45);
		
		
		Event event1 = Event.createNew();
		event1.setComment(YOGA_CLASS_NAME1);
		event1.setStartTime(cal1.getTime());
		
		Event event2 = Event.createNew();
		event2.setComment(YOGA_CLASS_NAME2);
		event2.setStartTime(cal2.getTime());
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event1);
		renderer.addEvent(event2);
		String result = renderer.render(cal1.getTime(),1);		
		
		//ASSERT
		assertThat(result, containsString(YOGA_CLASS_NAME1));
		assertThat(result.toLowerCase(), containsString("01:30 pm")); 
		assertThat(result, containsString(YOGA_CLASS_NAME2));
		assertThat(result.toLowerCase(), containsString("08:45 pm"));		
	}
	
	
	public void testRender_containsEventsForTheSameDate() {
		//SETUP
		String YOGA_CLASS_NAME1 = "ClassName1";		
		String START_TIME1 = "01:30 PM";
		Calendar cal1 = new GregorianCalendar(2011, 11, 2, 13, 30);
		
		String YOGA_CLASS_NAME2 = "ClasNam2";
		String START_TIME2 = "02:00 PM";
		Calendar cal2 = new GregorianCalendar(2011, 11, 2, 14, 00);
		
		
		Event event1 = Event.createNew();
		event1.setComment(YOGA_CLASS_NAME1);
		event1.setStartTime(cal1.getTime());
		
		Event event2 = Event.createNew();
		event2.setComment(YOGA_CLASS_NAME2);
		event2.setStartTime(cal2.getTime());
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event2);
		renderer.addEvent(event1);
		String result = renderer.render(cal1.getTime(),1);		
		
		//ASSERT
		assertThat(parseToXML(result), hasXPath("/root/div"));
		
		//First Column contains date
		assertThat(parseToXML(result), hasXPath("/root/div/div",equalToIgnoringCase(START_TIME1)));
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div",equalToIgnoringCase(START_TIME2)));
		
		//Second Column
		assertThat(parseToXML(result), hasXPath("/root/div/div[2]",equalToIgnoringCase(YOGA_CLASS_NAME1)));		
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div[2]",equalToIgnoringCase(YOGA_CLASS_NAME2)));
	}
	
	public void testRender_containsEventsOnSeparateDays() {
		//SETUP
		String YOGA_CLASS_NAME1 = "ClassName1";		
		String START_TIME1 = "01:30 PM";
		Calendar cal1 = new GregorianCalendar(2011, 11, 3, 13, 30);
		
		String YOGA_CLASS_NAME2 = "ClasNam2";
		String START_TIME2 = "02:00 PM";
		Calendar cal2 = new GregorianCalendar(2011, 11, 1, 14, 00);
		
		
		Event event1 = Event.createNew();
		event1.setComment(YOGA_CLASS_NAME1);
		event1.setStartTime(cal1.getTime());
		
		Event event2 = Event.createNew();
		event2.setComment(YOGA_CLASS_NAME2);
		event2.setStartTime(cal2.getTime());
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event2);
		renderer.addEvent(event1);
		String result = renderer.render(cal2.getTime(),8);		
		
		//ASSERT
		assertThat(result, containsString(YOGA_CLASS_NAME1));
		assertThat(result.toLowerCase(), containsString(START_TIME1.toLowerCase())); 
		assertThat(result, containsString(YOGA_CLASS_NAME2));
		assertThat(result.toLowerCase(), containsString(START_TIME2.toLowerCase()));
		
		assertThat(parseToXML(result), hasXPath("/root/div"));
		
		//First Column
		assertThat(parseToXML(result), hasXPath("/root/div/div",equalToIgnoringCase(START_TIME1)));
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div",equalToIgnoringCase(START_TIME2)));
		
		//Second Column
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div[2]",equalToIgnoringCase(YOGA_CLASS_NAME2)));
		
		//Third Column
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div[3]",equalToIgnoringWhiteSpace("")));
		
		//Fourth Column
		assertThat(parseToXML(result), hasXPath("/root/div/div[4]",equalToIgnoringCase(YOGA_CLASS_NAME1)));
	}
	
	public void testRender_containsEventsOnSeparateDays_CalendarExcludesFirstDate() {
		//SETUP
		String YOGA_CLASS_NAME1 = "ClassName1";		
		String START_TIME1 = "01:30 PM";
		Calendar cal1 = new GregorianCalendar(2011, 11, 3, 13, 30);
		
		String YOGA_CLASS_NAME2 = "ClasNam2";
		String START_TIME2 = "02:00 PM";
		Calendar cal2 = new GregorianCalendar(2011, 11, 1, 14, 00);
		
		
		Event event1 = Event.createNew();
		event1.setComment(YOGA_CLASS_NAME1);
		event1.setStartTime(cal1.getTime());
		
		Event event2 = Event.createNew();
		event2.setComment(YOGA_CLASS_NAME2);
		event2.setStartTime(cal2.getTime());
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event2);
		renderer.addEvent(event1);
		String result = renderer.render(cal1.getTime(),3);		
		
		//ASSERT
		assertThat(result, containsString(YOGA_CLASS_NAME1));
		assertThat(result.toLowerCase(), containsString(START_TIME1.toLowerCase()));
		
		assertThat(parseToXML(result), hasXPath("/root/div"));
		
		//First Column
		assertThat(parseToXML(result), hasXPath("/root/div/div",equalToIgnoringCase(START_TIME1)));
		
		//Second Column
		assertThat(parseToXML(result), hasXPath("/root/div/div[2]",equalToIgnoringCase(YOGA_CLASS_NAME1)));
		
		//Third Column
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div[3]",equalToIgnoringWhiteSpace("")));
		
		//Fourth Column
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div[4]",equalToIgnoringWhiteSpace("")));
	}
	
	public void testRender_containsEventsOnSeparateDays_CalendarExcludesSecondDate() {
		//SETUP
		String YOGA_CLASS_NAME1 = "ClassName1";		
		String START_TIME1 = "01:30 PM";
		Calendar cal1 = new GregorianCalendar(2011, 11, 5, 13, 30);
		
		String YOGA_CLASS_NAME2 = "ClasNam2";
		String START_TIME2 = "02:00 PM";
		Calendar cal2 = new GregorianCalendar(2011, 11, 1, 14, 00);
		
		
		Event event1 = Event.createNew();
		event1.setComment(YOGA_CLASS_NAME1);
		event1.setStartTime(cal1.getTime());
		
		Event event2 = Event.createNew();
		event2.setComment(YOGA_CLASS_NAME2);
		event2.setStartTime(cal2.getTime());
		
		//EXECUTE
		CalendarRenderer renderer = CalendarRenderer.createNew();
		renderer.addEvent(event2);
		renderer.addEvent(event1);
		String result = renderer.render(cal2.getTime(),3);		
		
		//ASSERT
		assertThat(result, containsString(YOGA_CLASS_NAME2));
		assertThat(result.toLowerCase(), containsString(START_TIME2.toLowerCase()));
		
		assertThat(parseToXML(result), hasXPath("/root/div"));
		
		//First Column
		assertThat(parseToXML(result), hasXPath("/root/div/div",equalToIgnoringCase(START_TIME2)));
		
		//Second Column
		assertThat(parseToXML(result), hasXPath("/root/div/div[2]",equalToIgnoringCase(YOGA_CLASS_NAME2)));
		
		//Third Column
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div[3]",equalToIgnoringWhiteSpace("")));
		
		//Fourth Column
		assertThat(parseToXML(result), hasXPath("/root/div[2]/div[4]",equalToIgnoringWhiteSpace("")));
	}
	
	  private Document parseToXML(String xmlStr) {
		  	xmlStr = "<root>"+xmlStr+"</root>";
		  	Document xmlDoc =null;
		  	
		  	try {
		  		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		  		documentBuilderFactory.setNamespaceAware(true);
		  		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        
	        	xmlDoc = documentBuilder.parse(new ByteArrayInputStream(xmlStr.getBytes()));
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        return xmlDoc;
	    }
}