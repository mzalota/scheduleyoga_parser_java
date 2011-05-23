package com.scheduleyoga.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;


import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.*;

import java.net.SocketException;
import java.util.ListIterator;

import com.scheduleyoga.parser.Parser;
import com.scheduleyoga.tests.SimpleTest;

public class Main {

	
	public static void executeTests() {
		junit.framework.TestSuite testSuite = new junit.framework.TestSuite(SimpleTest.class);
		junit.textui.TestRunner.run(testSuite);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		//executeTests();
		parse();
	}
	
	public static void parse() {	
		
		Parser parser;
		try {
			
			System.out.println("Loading Schedule from "+Parser.STUDIO_OM_YOGA);
			parser = Parser.createNew(Parser.STUDIO_OM_YOGA);
			parser.parseSite();
			
//			System.out.println("Loading Schedule from "+Parser.STUDIO_JIVAMUKTIYOGA);
//			parser = Parser.createNew(Parser.STUDIO_JIVAMUKTIYOGA);
//			parser.parseSite();			
			
//			System.out.println("Loading Schedule from "+Parser.STUDIO_JOSCHI_NYC);
//			parser = Parser.createNew(Parser.STUDIO_JOSCHI_NYC);
//			parser.parseSite();
			


//			System.out.println("Loading Schedule from "+Parser.STUDIO_LAUGHING_LOTUS);
//			parser = Parser.createNew(Parser.STUDIO_LAUGHING_LOTUS);
//			parser.parseSite();			
//			
			
			
//			System.out.println("Loading Schedule from "+Parser.STUDIO_KAIAYOGA);
//			parser = Parser.createNew(Parser.STUDIO_KAIAYOGA);
//			parser.parseSite();
//			
//			System.out.println("Loading Schedule from "+Parser.STUDIO_BABTISTE);
//			parser = Parser.createNew(Parser.STUDIO_BABTISTE);
//			parser.parseSite();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void iCal() {
		String myCalendarString = testICal();
		

		StringReader sin = new StringReader(myCalendarString);

		CalendarBuilder builder = new CalendarBuilder();

		try {
			Calendar calendar = builder.build(sin);
			System.out.println("here is the result");
			System.out.println(calendar.toString());
			
			ComponentList comps = calendar.getComponents();
			System.out.println("size of list is: "+comps.size());
			
			ListIterator listIterator = comps.listIterator();
			
			while(listIterator.hasNext() ){
				Component comp = (Component) listIterator.next();
				String eventName = comp.getName();
				System.out.println("name is: " +eventName);
				if (eventName=="VEVENT"){
					VEvent event = (VEvent) comp;
					String startDate = event.getStartDate().getDate().toString();
					System.out.println("startDate is: "+startDate);
				}
				
			}
			
			for(int i =0; i<comps.size();i++){
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
	

	private static String testICal(){
		
		net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
		cal.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);
		
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(java.util.Calendar.MONTH, java.util.Calendar.JANUARY);
		calendar.set(java.util.Calendar.DAY_OF_MONTH, 25);

		// Initialize as an all-day event..
		VEvent christmas = new VEvent(new Date(calendar.getTime()), "Christmas Day");
		System.out.println("hi");
		// Generate a UID for the event..
		UidGenerator ug;
		try {
			ug = new UidGenerator("1");
			christmas.getProperties().add(ug.generateUid());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cal.getComponents().add(christmas);
		System.out.println(cal.toString());

		return cal.toString();
	}
	
}
