package com.scheduleyoga.tests;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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