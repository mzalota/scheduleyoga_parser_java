package com.scheduleyoga.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.util.FalsifyingWebConnection;
//import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import com.scheduleyoga.Main.MainLoop;
import com.scheduleyoga.dao.DBAccess;
import com.scheduleyoga.dao.Instructor;
import com.scheduleyoga.dao.ParsingHistory;
import com.scheduleyoga.dao.Studio;
import com.scheduleyoga.dao.StudioOld;
import com.scheduleyoga.dao.Style;


public class Parser {

	static public final String STUDIO_BABTISTE = "Babtiste";  //http://www.baronbaptiste.com/
	static public final String STUDIO_KAIAYOGA = "KaiaYoga";  //http://kaiayoga.com/
	static public final String STUDIO_JOSCHI_NYC = "Joschi_NYC";  //http://www.joschinyc.com
	static public final String STUDIO_JIVAMUKTIYOGA = "JivamuktiYoga";  //http://www.jivamuktiyoga.com
	static public final String STUDIO_LAUGHING_LOTUS = "Laughing_Lotus"; //http://www.laughinglotus.com/
	static public final String STUDIO_OM_YOGA = "Om_yoga"; //http://www.omyoga.com
	
	static public final int STUDIO_ID_BABTISTE = 1;  
	static public final int STUDIO_ID_KAIAYOGA = 2;  
	static public final int STUDIO_ID_JOSCHI_NYC = 3;  
	static public final int STUDIO_ID_JIVAMUKTIYOGA = 4;  
	static public final int STUDIO_ID_LAUGHING_LOTUS = 5;
	static public final int STUDIO_ID_OM_YOGA = 6;
	
	private final Logger logger = Logger.getLogger(Parser.class);
	
	protected String studioID;
	protected Map<String, String> xPath = new HashMap<String, String>();
	protected Map<String, String> scheduleURL = new HashMap<String, String>();
	
	protected Map<String, Integer> studioIDNameMap = new HashMap<String, Integer>();
	
	public static Parser createNew (String studioIDParam) {
		
		Parser newObj = new Parser();
		
		newObj.xPath.put(Parser.STUDIO_BABTISTE, "/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[2]/td/table");
		newObj.xPath.put(Parser.STUDIO_KAIAYOGA, "/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table");
		newObj.xPath.put(Parser.STUDIO_JOSCHI_NYC, "/html/body/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]"); //http://www.joschinyc.com
		newObj.xPath.put(Parser.STUDIO_JIVAMUKTIYOGA, "/html/body/table[2]");
		newObj.xPath.put(Parser.STUDIO_LAUGHING_LOTUS, "/html/body/div/div[3]/div[2]/div/div[2]/div/div[2]/table");
		newObj.xPath.put(Parser.STUDIO_OM_YOGA, "/html/body/div/div/div/div[6]/div[2]/div/div[2]/table/tbody/tr/td/table[2]/tbody/tr/td/table[2]");
		
		newObj.scheduleURL.put(Parser.STUDIO_BABTISTE, "https://clients.mindbodyonline.com/ASP/home.asp?studioid=1466");
		newObj.scheduleURL.put(Parser.STUDIO_KAIAYOGA, "https://clients.mindbodyonline.com/ASP/home.asp?studioid=2148");
		newObj.scheduleURL.put(Parser.STUDIO_JOSCHI_NYC, "http://www.joschinyc.com/schedule.html");
		newObj.scheduleURL.put(Parser.STUDIO_JIVAMUKTIYOGA, "http://www.jivamuktiyoga.com/class/schedules.jsp?scheduleType=Center&viewCriteriaID=1");
		newObj.scheduleURL.put(Parser.STUDIO_LAUGHING_LOTUS, "http://nyc.laughinglotus.com/classschedule.html");  //https://clients.mindbodyonline.com/ASP/home.asp?studioid=137
		newObj.scheduleURL.put(Parser.STUDIO_OM_YOGA, "http://www.omyoga.com/index.php?option=com_content&view=article&id=54&Itemid=104");
		
		newObj.studioIDNameMap.put(Parser.STUDIO_BABTISTE, STUDIO_ID_BABTISTE);
		newObj.studioIDNameMap.put(Parser.STUDIO_KAIAYOGA, STUDIO_ID_KAIAYOGA);
		newObj.studioIDNameMap.put(Parser.STUDIO_JOSCHI_NYC, STUDIO_ID_JOSCHI_NYC);
		newObj.studioIDNameMap.put(Parser.STUDIO_JIVAMUKTIYOGA, STUDIO_ID_JIVAMUKTIYOGA);
		newObj.studioIDNameMap.put(Parser.STUDIO_LAUGHING_LOTUS, STUDIO_ID_LAUGHING_LOTUS);
		newObj.studioIDNameMap.put(Parser.STUDIO_OM_YOGA, STUDIO_ID_OM_YOGA);
		
		if (!newObj.scheduleURL.containsKey(studioIDParam)) {
			//TODO: throw exception
			return null;
		}
		
		newObj.studioID = studioIDParam;
				
		return newObj;	 
	}

	public String parseSite() throws IOException{
		
		if (!scheduleURL.containsKey(studioID)) {
			//TODO: throw exception
			return "";
		}
		
		if (studioID.equalsIgnoreCase(Parser.STUDIO_BABTISTE) || studioID.equalsIgnoreCase(Parser.STUDIO_KAIAYOGA)){		
			parseMindAndBodyOnline();
			return "";
		}
		
		if (studioID.equalsIgnoreCase(Parser.STUDIO_JOSCHI_NYC)) {
			parseYoschiNYC();
		}
		
		if (studioID.equalsIgnoreCase(Parser.STUDIO_JIVAMUKTIYOGA)) {
			parseJivamukti();
		}
		
		if (studioID.equalsIgnoreCase(Parser.STUDIO_LAUGHING_LOTUS)) {
			parseLaughingLotus();
		}
        
		if (studioID.equalsIgnoreCase(Parser.STUDIO_OM_YOGA)) {
			parseLaughingLotus();
		}
		
		return "";
	}
	
	protected void saveEvents(Studio studio, List<List<List<Event> > > allEvents) {
		studio.deleteEvents();
		for (int rowNum = 0; rowNum < allEvents.size(); rowNum++) {
			for (int colNum=0; colNum < allEvents.get(rowNum).size(); colNum++) {
				saveEventsOneList(studio,allEvents.get(rowNum).get(colNum));
//				for (int eventNum = 0; eventNum < allEvents.get(rowNum).get(colNum).size(); eventNum++){
//					Event event = allEvents.get(rowNum).get(colNum).get(eventNum);
//					//save event to DB
//					event.setStudio(studio);
//					event.saveToDB();
//				}
			}
		}
	}
	
	protected void saveEventsOneList(Studio studio, List<Event> allEvents) {		
		for (int eventNum = 0; eventNum < allEvents.size(); eventNum++) {
			Event event = allEvents.get(eventNum);
			
			event.setStudio(studio);			
			
			String instructorName = Helper.createNew().cleanUpInstructorName(event.getInstructorName());
			if (StringUtils.isEmpty(event.getInstructorName()) && (instructorName != event.getInstructorName().trim())){
				if (!StringUtils.isEmpty(event.getInstructorName())) {
					logger.warn("Invalid Instructor Name: -"+event.getInstructorName()+"- cleaned up is -"+instructorName+"-");
				}
			}
			
			Set<String> styleNames = Style.styleNamesFromClassName(event.getComment());
			logger.debug("ClassName is: "+event.getComment()+", styleNames are: "+styleNames);
			event.setStyleNames(styleNames);
			
			Instructor instructor = Instructor.fetchInstructorByName(instructorName,studio);
			logger.debug("Instructor is: "+instructor);
			if (instructor != null) {
				event.setInstructor(instructor);
			}
						
			DBAccess.saveObject(event);
		}
	}
	
	public String parseStudioSite(Studio studio) throws IOException{
		
		studioID = studio.getName();
		switch (studio.getId()) {
			case StudioOld.STUDIO_ID_OM_YOGA:
				return parseOmYoga(studio);
			case StudioOld.STUDIO_ID_BABTISTE:				
				return parseMindAndBodyOnline2(studio);
			case StudioOld.STUDIO_ID_KAIAYOGA:
				return parseMindAndBodyOnline2(studio);
			case Studio.STUDIO_FRESH_YOGA:
			case Studio.STUDIO_FRESH_YOGA_ERECTOR_SQUARE:
			try {
				return parseFreshYoga(studio);
			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			} catch (ParsingErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}			
			case Studio.STUDIO_BALANCED_YOGA:
			try {
				return parseBalancedYoga(studio);
			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParsingErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			default:
				return parseMindAndBodyOnline2(studio);	
				
		}
	}
	
	
	protected int studioIDFromStudioName(String studioName){		
		return studioIDNameMap.get(studioName);
	}
	

	protected boolean rowIsHeaderWithDaysOfTheWeek(HtmlTableRow row){
		
		List<HtmlTableCell> cells = row.getCells();
		if (cells.size() != 7){
			return false;
		}
    	for (final HtmlTableCell cell : row.getCells()) {
    		if (!Helper.createNew().containsDate(cell.asText())){
    			return false;
    		}
    	}
    	return true;
	}


	/**
	 * 
	 * @return Studio
	 */
	public StudioOld getStudio(String studioName) {
		StudioOld studio = StudioOld.createNew(studioIDFromStudioName(studioName));
        
		//studio.setId();
		studio.setName(studioName);
		
        String urlStr = scheduleURL.get(studioName);
        studio.setUrl(urlStr);
        
        String xPathParam = xPath.get(studioName); 
        studio.setxPath(xPathParam);
        
        return studio;
	}
	
	protected void parseJivamukti() throws FailingHttpStatusCodeException, IOException{
		
		String urlStr = scheduleURL.get(studioID);		
		WebClient webClient = getWebClient();
        URL url = new URL(urlStr); 
        
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        //printPageSourceCode(page);
        
        //String xPathParam = "";
        String xPathParam = xPath.get(studioID); 
        HtmlTable table = (HtmlTable) page.getByXPath(xPathParam).get(0);
        
		for (final HtmlTableRow row : table.getRows()) {
			// rowCount++;
			// System.out.println("Found row: "+rowCount);
			// System.out.println("Row value: "+row.toString());
			List<HtmlTableCell> cells = row.getCells();

			System.out.println("Number of cells: " + cells.size());

			if (rowIsHeaderWithDaysOfTheWeek(row)) {
				System.out.println("Columns Are Days of the week!!!");
			}

			// printCellsInRow(row);

			for (final HtmlTableCell cell : row.getCells()) {
				if (Helper.createNew().containsTime(cell.asText())) {
					System.out
							.println(" Time and Instructor: " + cell.asText());
				} else if (StringUtils.isNotBlank(cell.asText().trim())) {
					System.out.println(" Class is: " + cell.asText());
				}
			}
		}
	        
//    		
//    		if (cells.size()<4){
//    			continue;
//    		}
//    		String dateTime = StringUtils.trim(cells.get(1).asText()).replaceAll("\\s+", " ");;
//    		if (containsDate(dateTime)) {
//    			System.out.println("Date is "+dateTime);
//    			continue;	    			
//    		}
//    		String time = StringUtils.trim(cells.get(1).asText());
//    		String className = StringUtils.trim(cells.get(2).asText());
//    		String instructor = StringUtils.trim(cells.get(3).asText());
//    		System.out.println("Time is: "+time+", class is: "+className+", instructor is: "+instructor);
        
	}
	
	protected void parseLaughingLotus() throws FailingHttpStatusCodeException, IOException {
		String urlStr = scheduleURL.get(studioID);		
		WebClient webClient = getWebClient();
        URL url = new URL(urlStr); 
        
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        printPageSourceCode(page);
        
        String xPathParam = xPath.get(studioID); 
        HtmlTable table = (HtmlTable) page.getByXPath(xPathParam).get(0);
    
        for (final HtmlTableRow row : table.getRows()) {
            //System.out.println("Found row: "+rowCount);
            //System.out.println("value: "+row.toString());
            List<HtmlTableCell> cells = row.getCells();
          
            //determineColumnMeaning(rowCount, rowsWithDate, columnsWithTime, row);
            
            
            String time = cells.get(0).asText();
            if (Helper.createNew().containsDate(time)){
            	System.out.println("date is: "+time);
            	continue;
            }
            
            String className = cells.get(1).asText();
            String instructor = cells.get(2).asText();
            
            System.out.println("time: "+time+", class: "+className+", instructor: "+instructor);
            
        }
        
	}
	
	protected void parseYoschiNYC() throws FailingHttpStatusCodeException, IOException{
		
		String urlStr = scheduleURL.get(studioID);		
		WebClient webClient = getWebClient();
        URL url = new URL(urlStr); 
        
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        //printPageSourceCode(page);
        
        //String xPathParam = "/html/body/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]";
        String xPathParam = xPath.get(studioID); 
        HtmlTableCell allCalendarElement = (HtmlTableCell) page.getByXPath(xPathParam).get(0);
        
        for (final HtmlElement subSection : allCalendarElement.getHtmlElementsByTagName("table")) {//getChildElements()) {
        	
//        	if (subSection.getClass() != "HtmlTable") {
//        		continue;
//        	}
        	HtmlTable table = (HtmlTable) subSection;
	        for (final HtmlTableRow row : table.getRows()) {
				//rowCount++;
	            //System.out.println("Found row: "+rowCount);
	            //System.out.println("Row value: "+row.toString());
	            List<HtmlTableCell> cells = row.getCells();
	            
	    		//printCellsInRow(row);
	    		
	    		if (cells.size()<4){
	    			continue;
	    		}
	    		String dateTime = StringUtils.trim(cells.get(1).asText()).replaceAll("\\s+", " ");;
	    		if (Helper.createNew().containsDate(dateTime)) {
	    			System.out.println("Date is "+dateTime);
	    			continue;	    			
	    		}
	    		String time = StringUtils.trim(cells.get(1).asText());
	    		String className = StringUtils.trim(cells.get(2).asText());
	    		String instructor = StringUtils.trim(cells.get(3).asText());
	    		System.out.println("Time is: "+time+", class is: "+className+", instructor is: "+instructor);
	        }
        }
	}

	/**
	 * @param row
	 */
	protected void printCellsInRow(final HtmlTableRow row) {
		for (final HtmlTableCell cell : row.getCells()) {
			System.out.println(" Columns: " + cell.asText());
		}
	}
	
	
	protected List<String> buildParts(HtmlElement subRow) {

		ElementParser textExtractor = new ElementParser();
		textExtractor.parseDocument(subRow.asXml());
		return textExtractor.getValues();
	}
	
	
	protected String parseBalancedYoga(Studio studio) throws FailingHttpStatusCodeException, IOException, ParsingErrorException{
		
		WebClient webClient = getWebClient();
        URL url = new URL(studio.getUrlSchedule()); 
		
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        
        HtmlElement schedSection = (HtmlElement) page.getByXPath(studio.getXpath()).get(0);            
        
        List<HtmlTable> daySchedTables = (List<HtmlTable>) schedSection.getByXPath("//table");
                
        if (daySchedTables.size()<1){
        	//TODO: log error/warning that we could not parse the page.
        	return "";
        }
        
        List<Event> events = new ArrayList<Event>();
        int tblCount = 0;
        for (HtmlTable tbl : daySchedTables) {
        	String dayOfTheWeek = "MONDAY";
        
        	if (tblCount == 0){
        		dayOfTheWeek = "MONDAY";
        	} else if (tblCount == 1){
        		dayOfTheWeek = "TUESDAY";
        	} else if (tblCount == 2){
        		dayOfTheWeek = "WEDNESDAY";
        	} else if (tblCount == 3){
        		dayOfTheWeek = "THURSDAY";
        	} else if (tblCount == 4){
        		dayOfTheWeek = "FRIDAY";
        	} else if (tblCount == 5){
        		dayOfTheWeek = "SATURDAY";
        	} else if (tblCount == 6){
        		dayOfTheWeek = "SUNDAY";
        	}    		
        	tblCount++;
        	
        	boolean afterNoon = false;
        	for (int rowNum = 0; rowNum< tbl.getRowCount(); rowNum++){
        		List<HtmlTableCell> cells = tbl.getRow(rowNum).getCells();
        		if (cells.size()<4){
        			//TODO: Show warning that could not parse event
        			continue;
        		}
        		List<String> timeParts = buildParts(cells.get(0));
        		List<String> classNameParts = buildParts(cells.get(1));
        		List<String> teacherNameParts = buildParts(cells.get(2));
        		List<String> commentParts = buildParts(cells.get(3));
        		
        		if (timeParts.size()<0){
        			//Time is invalid
        			continue;
        		}
        		if (classNameParts.size()<0){
        			//Time is invalid
        			continue;
        		}
        		if (teacherNameParts.size()<0){
        			//Time is invalid
        			continue;
        		}
        		if (commentParts.size()<0){
        			//Time is invalid
        			continue;
        		}
        		
        		String className = WordUtils.capitalizeFully(classNameParts.get(0));
        		String teacherName = WordUtils.capitalizeFully(teacherNameParts.get(0));
        		String comment = WordUtils.capitalizeFully(commentParts.get(0));        		
        		
        		Date startTime = freshYoga_deriveStartTime(timeParts.get(0), dayOfTheWeek, afterNoon);
        		if (startTime == null){
        			continue;
        		}
        		
        		if (new SimpleDateFormat("a").format(startTime).equalsIgnoreCase("pm")){
        			//This is the first time that we saw a time in the afternoon. 
        			//set afteNoon flag to be true - all times that follow will be in the afternoon.
        			afterNoon=true;
        		}
        		
        		logger.info("dayOfTheWeek: "+dayOfTheWeek+" time is: "+timeParts.get(0)+
        				" className is: "+className+" "+comment+
        				" instructorName is: " + teacherName +
        				" startTime: "+startTime);
        		
        		
        		Set<String> styles = Style.styleNamesFromClassName(className);
        		Set<String> styles2 = Style.styleNamesFromClassName(commentParts.get(0));
        		styles.addAll(styles2);
        		
        		Event event = Event.createNew();
        		event.setStartTime(startTime);
        		event.setInstructorName(teacherName);
        		event.setComment(className+" ("+comment+")");
        		event.setStyleNames(styles);  
        		
        		//Event For next week
        		Calendar cal = new GregorianCalendar();
        		cal.setTime(startTime);
        		cal.add(Calendar.DATE, 7);
        		
        		Event eventNextWeek = Event.createNew();
        		eventNextWeek.setStartTime(cal.getTime());
        		eventNextWeek.setInstructorName(teacherName);
        		eventNextWeek.setComment(className+" ("+comment+")");
        		eventNextWeek.setStyleNames(styles);
        		
        		events.add(event);
        		events.add(eventNextWeek);
        		
        	}
        }
        
        if(events.size()<1){
        	//TODO: Show warning that we did not find any events.
        }
        
        //Delete all the events.
        studio.deleteEvents();
        saveEventsOneList(studio, events);    
        
        return "";
	}
	
	protected String parseFreshYoga(Studio studio) throws FailingHttpStatusCodeException, IOException, ParsingErrorException{
		
		WebClient webClient = getWebClient();
        URL url = new URL(studio.getUrlSchedule()); 
		
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        
        HtmlElement schSection = (HtmlElement) page.getByXPath(studio.getXpath()).get(0);
        
        //DomNodeList<HtmlElement> listOfSchedules = schSection.getElementsByTagName("table");
        
        List<HtmlTable> schedSections = (List<HtmlTable>) schSection.getByXPath("//*[@id=\"column2\"]/div/table");
        
        if (schedSections.size()<1){
        	//TODO: log error/warning that we could not parse the page.
        	return "";
        }
        
        Helper helper = Helper.createNew();
        
        List<Event> events = new ArrayList<Event>();
        for (HtmlTable tbl : schedSections){
        	String dayOfTheWeek = tbl.getPreviousSibling().asText();
        	
        	boolean afterNoon = false;
        	for (int rowNum = 0; rowNum< tbl.getRowCount(); rowNum++){
        		List<HtmlTableCell> cells = tbl.getRow(rowNum).getCells();
        		if (cells.size()<2){
        			//TODO: Show warning that could not parse event
        			continue;
        		}
        		String time = cells.get(0).asText();
        		List<String> descr = buildParts(cells.get(1));
        		
        		if (descr.size()<2){
        			//TODO: Show warning that could not parse the event
        			continue;
        		}
        		String className = descr.get(0);
        		String instructorName = descr.get(1);
        		
        		if (StringUtils.isBlank(className)){
        			//TODO: Show warning that could not parse the event
        			continue;
        		}
        		
        		if (StringUtils.isBlank(instructorName)){
        			//TODO: Show warning that could not parse the event
        			continue;
        		}
        		
        		Date startTime = freshYoga_deriveStartTime(time, dayOfTheWeek, afterNoon);
        		if (startTime == null){
        			continue;
        		}
        		
        		if (new SimpleDateFormat("a").format(startTime).equalsIgnoreCase("pm")){
        			//This is the first time that we saw a time in the afternoon. 
        			//set afteNoon flag to be true - all times that follow will be in the afternoon.
        			afterNoon=true;
        		}         		
    			
        		logger.info("dayOfTheWeek: "+dayOfTheWeek+" time is: "+time+" className is: "+className+" instructorName is: "+instructorName+" startTime: "+startTime);
        		
        		Event event = Event.createNew();
        		event.setStartTime(startTime);
        		event.setInstructorName(instructorName);
        		event.setComment(className);
        		event.setStyleNames(Style.styleNamesFromClassName(className));

        		
        		//Event For next week
        		Calendar cal = new GregorianCalendar();
        		cal.setTime(startTime);
        		cal.add(Calendar.DATE, 7);
        		Event eventNextWeek = Event.createNew();
        		eventNextWeek.setStartTime(cal.getTime());
        		eventNextWeek.setInstructorName(instructorName);
        		eventNextWeek.setComment(className);
        		eventNextWeek.setStyleNames(Style.styleNamesFromClassName(className));
        		
        		events.add(event);
        		events.add(eventNextWeek);
        	}        	
        }
        
        if(events.size()<1){
        	//TODO: Show warning that we did not find any events.
        }
        
        //Delete all the events.
        studio.deleteEvents();
        saveEventsOneList(studio, events);        
        
        return "";
	}


	protected Date freshYoga_deriveStartTime(String time, String dayOfTheWeek, boolean afterNoon){
		
		Helper helper = Helper.createNew();
		
	   	Date curDate;
		try {
			curDate = helper.nextDateFromDayOfTheWeek(dayOfTheWeek, new Date());
		} catch (ParseException e1) {
			return null;		
		}
		
//		String[] timeArr = StringUtils.split(time, "-");
//		String startTimeStr = timeArr[0].trim();
		
		String startTimeStr = StringUtils.substringBefore(time, "-");
		startTimeStr = startTimeStr.replaceAll("\\s+", " "); //replace any sequence of white space characters with a single s[ace
				
		if (startTimeStr.equalsIgnoreCase("noon")){
			startTimeStr = "12:00";
		}
		
		Date eventTime;
		try {
			eventTime = helper.deduceDateFromSimpleTimeString(startTimeStr, afterNoon);
		} catch (ParseException e1) {
			logger.warn(e1.getMessage());
			logger.warn(e1.getStackTrace());
			return null;
		}    			
		String eventTimeStr = new SimpleDateFormat("HH:mm").format(eventTime);
		
		String eventDateStr = new SimpleDateFormat("yyyy-MM-dd").format(curDate);
		
		Date startTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");		
		try {
			startTime = (Date)formatter.parse(eventDateStr+" "+eventTimeStr);
		} catch (ParseException e) {
			return null;
		}
		
		return startTime;
	}
	
	protected String parseOmYoga(Studio studio) throws FailingHttpStatusCodeException, IOException{
		
		WebClient webClient = getWebClient();
        URL url = new URL(studio.getUrlSchedule()); 
		
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        HtmlTable table = (HtmlTable) page.getByXPath(studio.getXpath()).get(0);
        
        EventsParser_OmYoga eventParser = new EventsParser_OmYoga();
        
        Segment calendarSegment = Segment.createNewFromElement(table, eventParser);
        logger.debug("The table is: "+calendarSegment);                
        
        List<List<List<Event> > > allEvents = calendarSegment.extractEvents();
        
        saveEvents(studio, allEvents);
        
        return eventParser.asHTMLTable(allEvents);
	}
	
	protected String parseMindAndBodyOnline2(Studio studio) throws FailingHttpStatusCodeException, IOException {

		WebClient webClient = getWebClient();
		
        URL url = new URL(studio.getUrlSchedule());
        
        HtmlPage pageOuter;
        logger.info("Loading WebPage: "+url);
        pageOuter = (HtmlPage)webClient.getPage(url);        
        
        List<FrameWindow> frames = pageOuter.getFrames();        
        HtmlPage page = (HtmlPage) frames.get(0).getEnclosedPage(); //TODO: just changed 1 to 0
        
        studio.deleteEvents();
        String outputPage1 = ParseMindBodyOnlinePage(studio, webClient, page);
        page = loadNextPage(page);
       	String outputPage2 = ParseMindBodyOnlinePage(studio, webClient, page);
        
        webClient.closeAllWindows();
        
        return outputPage1+"  "+outputPage2;
	}

	/**
	 * @param studio
	 * @param webClient
	 * @param page
	 * @return
	 */
	protected String ParseMindBodyOnlinePage(Studio studio,
			WebClient webClient, HtmlPage page) {
		//System.out.println(page.asXml());
        
        //System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "info");               
        
		if (page == null){
			logger.error("Page parameter is null");
			return null;
		}
			
        String xPathParam = studio.getXpath(); //xPath.get(studioID); 
                
        //TODO: Hardcoded XPath for MindBodyOnline
        xPathParam = "/html/body/div[4]/div/div/div/table/tbody/tr[2]/td/table";
        xPathParam = "//*[@id=\"classSchedule-header\"]";
        xPathParam =  "//*[@id=\"classSchedule-mainTable\"]";
        logger.info("XPath is: "+xPathParam);
        List<HtmlElement> elements = (List<HtmlElement>) page.getByXPath(xPathParam);
        logger.info("Count of HTML elements: "+elements.size());
        
        if (elements.size() <=0 ){
        	logger.error("COULD NOT FIND specified XPath: "+xPathParam);
        	logger.debug("Not too good: "+page.asXml());
        	webClient.closeAllWindows();
        	//String msg = "COULD NOT FIND specified XPath: "+xPathParam;
        	return "</br><strong>COULD NOT FIND specified XPath: "+xPathParam+"</strong></br>";
        }
    	HtmlTable table = (HtmlTable) page.getByXPath(xPathParam).get(0);        
    	String calendarXHTML = table.asXml();
             
    	logger.debug("CalendarTable XML has "+calendarXHTML.length()+" characters");
        
        ParsingHistory parsingHist = ParsingHistory.createNew(studio.getId(), calendarXHTML);
        DBAccess.saveObject(parsingHist);
        
        String outputPage = parseTable2(studio, table);
		return outputPage;
	}

	/**
	 * @return
	 */
	protected WebClient getWebClient() {
				
		//System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", logLevel);
		
		WebClient webClient = new WebClient();
		webClient.setCssEnabled(true); //false
		//webClient.setThrowExceptionOnScriptError(false); 
		//webClient.setJavaScriptEnabled(false);
		
		
//		webClient.setThrowExceptionOnFailingStatusCode(false);
//		webClient.setThrowExceptionOnScriptError(false);

//		webClient.setIncorrectnessListener(new IncorrectnessListener() {
//			@Override
//			public void notify(String arg0, Object arg1) {
//				// TODO Auto-generated method stub
//				System.out.println("In setIncorrectnessListener.notify: "+arg0);
//			}
//		});

//		webClient.setCssErrorHandler(new SilentCssErrorHandler());		
//
//		webClient.setHTMLParserListener(new HTMLParserListener() {
//			@Override
//			public void warning(String arg0, URL arg1, int arg2, int arg3, String arg4) {
//				// TODO Auto-generated method stub
//			}
//			@Override
//			public void error(String arg0, URL arg1, int arg2, int arg3, String arg4) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
//			@Override
//			public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
//				// TODO Auto-generated method stub
//			}
//			@Override
//			public void scriptException(HtmlPage arg0, ScriptException arg1) {
//				// TODO Auto-generated method stub
//				System.out.println("In scriptException");
//			}
//			@Override
//			public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
//				// TODO Auto-generated method stub
//			}
//			@Override
//			public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
//				// TODO Auto-generated method stub
//				System.out.println("In loadScriptError");
//			}
//		});
		
		
		//Do not load Google Analytics or Facebook javascripts because they cause a lot of errors in HtmlUnit(e.g. Shockwave Flash) 
		webClient.setWebConnection(new FalsifyingWebConnection(webClient) {
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
            	URL url = request.getUrl();
            	if ("www.google-analytics.com".equals(url.getHost())) {
            		return createWebResponse(request, "", "application/javascript"); // -> empty script
            	}
            	if ("ssl.google-analytics.com".equals(url.getHost())) {
            		return createWebResponse(request, "", "application/javascript"); // -> empty script
            	}
            	if ("connect.facebook.net".equals(url.getHost())) {
            		return createWebResponse(request, "", "application/javascript"); // -> empty script
            	}       
            	String fileName = url.getFile().substring( url.getFile().lastIndexOf('/')+1, url.getFile().length() );
            	if("mb.facebook.js".equals(fileName)){
            		return createWebResponse(request, "", "application/javascript"); // -> empty script
            	}
            	return super.getResponse(request);
            }
        });
		
		return webClient;
	}
	
	private String parseTable2(Studio studio, HtmlTable table) {	
		
		EventsParser_Baptista eventParser = new EventsParser_Baptista();
		
		SegmentHorizontal calendarSegment = (SegmentHorizontal) SegmentHorizontal.createNewFromElement(table, eventParser);
		
		List<Event> events = calendarSegment.extractEvents();
		
		if (events.size()<=0){
			logger.warn("Did not extract any events.");
		} else {
			logger.info("Extracted "+events.size()+" events");
		}
		
		saveEventsOneList(studio, events);
        
        return eventParser.asHTMLTable(events);
        
		//return calendarSegment.asHTMLTable_horizontal();		
	}


	
	/**
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected void parseMindAndBodyOnline() throws MalformedURLException,
			IOException {
		String urlStr = scheduleURL.get(studioID);
		
		final WebClient webClient = getWebClient();
        URL url =  new URL(urlStr); 
        
        HtmlPage pageOuter;
        System.out.println("Loading Page 1");
        pageOuter = (HtmlPage)webClient.getPage(url);
        List<FrameWindow> frames = pageOuter.getFrames();        
        HtmlPage page = (HtmlPage) frames.get(1).getEnclosedPage();
        
        //printPageSourceCode(page); 
        loadCalendarData(page);

        System.out.println("Loading Page 2");
        page = loadNextPage(page);
        //printPageSourceCode(page);        
        loadCalendarData(page);
        
        System.out.println("Loading Page 3");
        page = loadNextPage(page);
        loadCalendarData(page);
	}
	

	private void loadCalendarData(HtmlPage page)
			throws IOException {
		
		if (!xPath.containsKey(studioID)){
			//TODO: throw exception
			return;
		}
		
        String xPathParam = xPath.get(studioID); 
        HtmlTable table = (HtmlTable) page.getByXPath(xPathParam).get(0);
        parseTable(table);
	}


	private void parseTable(HtmlTable table) {
				
		Integer timeCellNum = 0;
		Integer classCellNum = 2;
		Integer instructorCellNum = 3;
		Integer locationCellNum = 5;
		
		List<Integer> cellNums = Arrays.asList(timeCellNum,classCellNum,instructorCellNum,locationCellNum);
		Integer maxCellNum = Collections.max(cellNums);
		
		Integer rowCount = 0;	
		
		Map<Integer,Integer> rowsWithDate = new HashMap<Integer, Integer>();// = Collections.emptyList();
		Map<Integer,Integer> columnsWithTime = new HashMap<Integer, Integer>();// = Collections.emptyList();
		for (final HtmlTableRow row : table.getRows()) {
			rowCount++;
            List<HtmlTableCell> cells = row.getCells();
          
            if (cells.size()<= timeCellNum) {
            	continue;
            }
                                  
            String time = StringUtils.trim(cells.get(timeCellNum).asText()).replaceAll("\\s+", " ");;
            if (Helper.createNew().containsDate(time)) {
            
            	//Fri   May 20, 2011
            	SimpleDateFormat parser = new SimpleDateFormat("EEE MMMMM dd, yyyy");
            	SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yy");
            	Date d;
				try {
					d = parser.parse(time);
					System.out.println(formater.format(d));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("formatter did not work -"+time+"-");
					System.exit(0);
				}            	
            	System.out.println(time);
            	continue;
            }
            
            if (cells.size()<= maxCellNum) {
            	continue;
            }
            
            String className = cells.get(classCellNum).asText();
            String instructor = cells.get(instructorCellNum).asText();
            String location = cells.get(locationCellNum).asText();
            
            System.out.println(time+", "+className+", "+instructor+", "+location);
        }
		System.out.println("Done");
	}


	private void determineColumnMeaning(Integer rowCount,
			Map<Integer, Integer> rowsWithDate,
			Map<Integer, Integer> columnsWithTime, final HtmlTableRow row) {
		Integer columnCount = 0;
		for (final HtmlTableCell cell : row.getCells()) {
			columnCount++;
			if (StringUtils.isBlank(cell.asText())) {
				//continue;
			}
			
			if(Helper.createNew().containsDate(cell.asText())){
				rowsWithDate.put(rowCount, columnCount);
			}
			
			if (Helper.createNew().containsTime(cell.asText())){
				columnsWithTime.put(columnCount,rowCount);
			}
			System.out.println(" Columns: " + cell.asText());
		}
	}

	private void printPageSourceCode(HtmlPage page) {
		System.out.println(page.getTitleText());
        final String pageAsText = page.asText();
        System.out.println("Hello Max 10");
        System.out.println(pageAsText);
        System.out.println("Hello Max 20");
        
        final String pageAsXml = page.asXml();
        System.out.println(pageAsXml);
        System.out.println("Hello Max 30");
	}
	
	protected HtmlPage loadNextPage(HtmlPage pageTwo){
		
	    //HtmlForm form = (HtmlForm) page.getForms().get(0);
        //HtmlTextInput text = (HtmlTextInput) form.getInputByName("q");
        //text.setValueAttribute("HtmlUnit");
        //HtmlSubmitInput btn = (HtmlSubmitInput) form.getInputByName("btnG");
        //HtmlPage page2 = (HtmlPage) btn.click();
        //HtmlAnchor link = page2.getAnchorByHref("http://htmlunit.sourceforge.net/");
        //HtmlPage page3 = (HtmlPage) link.click();
        //assertEquals(page3.getTitleText(), "htmlunit - Welcome to HtmlUnit");
        //assertNotNull(page3.getAnchorByHref("gettingStarted.html"));       
		
		//final List<FrameWindow> frames = page.getFrames();
        //final HtmlPage pageTwo = (HtmlPage) frames.get(1).getEnclosedPage();
		
		
        String btnXPath = "//*[@id=\"week-arrow-r\"]";
        HtmlElement btn = (HtmlElement) pageTwo.getElementById("week-arrow-r");
        
        logger.info("Found Button:"+btn.toString());
        
        HtmlPage page2;
		try {
			page2 = (HtmlPage) btn.click();
			//printPageSourceCode(page2);
			return page2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Could not load next page:"+pageTwo.getUrl().toString());
			System.exit(0);
		}
        return null;
	}
	
    protected static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
	
//	
//
//	public static void main2(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpGet httpget = new HttpGet("https://clients.mindbodyonline.com/ASP/main_class.asp?tg=0&vt=&lvl=&view=&trn=&date=5/1/2011&loc=1&page=1&pMode=&prodid=&stype=-7&classid=0&catid=&justloggedin=");
//		//https://clients.mindbodyonline.com/ASP/main_class.asp?tg=0&vt=&lvl=&view=&trn=&date=5/1/2011&loc=1&page=1&pMode=&prodid=&stype=-7&classid=0&catid=&justloggedin=
//		//https://clients.mindbodyonline.comASP/home.asp?studioid=2148
//		try {
//			HttpResponse response = httpclient.execute(httpget);		
//			HttpEntity entity = response.getEntity();
//			
//			if (entity != null) {
//				
//			    InputStream instream = entity.getContent();
//			
//			    StringWriter writer = new StringWriter();
//			    IOUtils.copy(instream, writer);
//
//			    instream.close();
//			    System.out.println(writer.toString());
//			}
//		} catch (ClientProtocolException e){
//			//TODO: add exception-handling
//			throw(e);
//		} catch (IOException e){
//			//TODO: add exception-handling
//			throw(e);
//		}
//		
//	}

    
    public class EventsParser_FreshYoga implements EventsParser {
		private String tmpVar;
		private int colNum;
		private boolean afterNoon = false;
		
		public int getColumnNumber() {
			return colNum;
		}

		public void setColumnNumber(int colNum) {
			this.colNum = colNum;
		}

		public EventsParser_FreshYoga() {
		}

		/**
		 * @param parts
		 */
		public Event createEventFromParts(List<String> parts) {
			if (parts.size() <= 0) {
				return null;
			}
			Event event = Event.createNew();
			
			if (!Helper.createNew().containsTime(parts.get(0))){
				//The first part is not a time string. This is an invalid event.
				return null;
			}
			
			Date startTime = convertStrToDate(parts.get(0));
			//event.setStartTimeStr(parts.get(0));
			event.setStartTime(startTime);
			parts.remove(0);

			if (parts.size() > 1 ){
				event.setInstructorName(parts.get(parts.size()-1));
				parts.remove(parts.size()-1);
			}
			
			String txt3 = StringUtils.join(parts.toArray(), " ");
			event.setComment(txt3);					
			
			
			return event;
		}
		
		//@Override
		public String asHTMLTable(List<List<List<Event> > > allEvents) {
			String output = "<table>";
			for (int rowNum = 0; rowNum < allEvents.size(); rowNum++) {
				output = output + "<tr style=\"border: 1px solid red;\">";
				for (int colNum=0; colNum < allEvents.get(rowNum).size(); colNum++) {
					output = output + "<td style=\"border: 1px solid red;\">";
					for (int eventNum = 0; eventNum < allEvents.get(rowNum).get(colNum).size(); eventNum++){
						Event event = allEvents.get(rowNum).get(colNum).get(eventNum);
						//save event to DB
						//event.saveToDB();
						//createHTML
						output = output + buildHTMLForEvent(event);
					}
					output = output + "</td>";
				}
				output = output + "</tr>\n";
			}
			output = output + "</table>";
			return output;
		}
		
		/**
		 * @param events
		 * @return String output
		 */
		public String buildHTMLForEvent(Event event) {
			
			if (null == event){
				return "";
			}
			
			String output = "";
			output = output + "<div style=\"color: red\">" + event.getStartTimeStr() + "</div> <br/>";
			output = output + "<div style=\"color: blue\">" + event.getComment()+ "</div> <br/>";
			output = output + "<div style=\"color: black\">" + event.getInstructorName() + "</div> <br/>";
			output = output + "<hr />";

			return output;
		}
		
		protected Date convertStrToDate(String startTimeStr) {
			
			Helper helper = Helper.createNew();
			Date eventDate = helper.nextDateFromDayOfTheWeek(getColumnNumber()+1, new Date());			
			String eventDateStr = new SimpleDateFormat("yyyy-MM-dd").format(eventDate); 			
			
			Date eventTime;
			try {
				eventTime = helper.deduceDateFromSimpleTimeString(startTimeStr, afterNoon);
			} catch (ParseException e1) {
				logger.warn(e1.getMessage());
				logger.warn(e1.getStackTrace());
				return null;
			}
			String eventTimeStr = new SimpleDateFormat("hh:mm a").format(eventTime);
			
			//SimpleDateFormat formatterAmPm = ;
			
			if (new SimpleDateFormat("a").format(eventTime).equalsIgnoreCase("pm")){
				//This is the first time that we saw a time in the afternoon. 
				//set afteNoon flag to be true - all times that follow will be in the afternoon.
				afterNoon=true;
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			Date dt;
			try {
				return (Date)formatter.parse(eventDateStr+" "+eventTimeStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
    
	public class EventsParser_OmYoga implements EventsParser {
		private String tmpVar;
		private int colNum;
		private boolean afterNoon = false;
		
		public int getColumnNumber() {
			return colNum;
		}

		public void setColumnNumber(int colNum) {
			this.colNum = colNum;
		}

		public EventsParser_OmYoga() {
		}

		/**
		 * @param parts
		 */
		public Event createEventFromParts(List<String> parts) {
			if (parts.size() <= 0) {
				return null;
			}
			Event event = Event.createNew();
			
			if (!Helper.createNew().containsTime(parts.get(0))){
				//The first part is not a time string. This is an invalid event.
				return null;
			}
			
			Date startTime = convertStrToDate(parts.get(0));
			//event.setStartTimeStr(parts.get(0));
			event.setStartTime(startTime);
			parts.remove(0);

			if (parts.size() > 1 ){
				event.setInstructorName(parts.get(parts.size()-1));
				parts.remove(parts.size()-1);
			}
			
			String txt3 = StringUtils.join(parts.toArray(), " ");
			event.setComment(txt3);					
			
			
			return event;
		}
		
		//@Override
		public String asHTMLTable(List<List<List<Event> > > allEvents) {
			String output = "<table>";
			for (int rowNum = 0; rowNum < allEvents.size(); rowNum++) {
				output = output + "<tr style=\"border: 1px solid red;\">";
				for (int colNum=0; colNum < allEvents.get(rowNum).size(); colNum++) {
					output = output + "<td style=\"border: 1px solid red;\">";
					for (int eventNum = 0; eventNum < allEvents.get(rowNum).get(colNum).size(); eventNum++){
						Event event = allEvents.get(rowNum).get(colNum).get(eventNum);
						//save event to DB
						//event.saveToDB();
						//createHTML
						output = output + buildHTMLForEvent(event);
					}
					output = output + "</td>";
				}
				output = output + "</tr>\n";
			}
			output = output + "</table>";
			return output;
		}
		
		/**
		 * @param events
		 * @return String output
		 */
		public String buildHTMLForEvent(Event event) {
			
			if (null == event){
				return "";
			}
			
			String output = "";
			output = output + "<div style=\"color: red\">" + event.getStartTimeStr() + "</div> <br/>";
			output = output + "<div style=\"color: blue\">" + event.getComment()+ "</div> <br/>";
			output = output + "<div style=\"color: black\">" + event.getInstructorName() + "</div> <br/>";
			output = output + "<hr />";

			return output;
		}
		
		protected Date convertStrToDate(String startTimeStr) {
			
			Helper helper = Helper.createNew();
			Date eventDate = helper.nextDateFromDayOfTheWeek(getColumnNumber()+1, new Date());			
			String eventDateStr = new SimpleDateFormat("yyyy-MM-dd").format(eventDate); 			
			
			//startTimeStr = startTimeStr.split("-")[0];
			
			startTimeStr = StringUtils.substringBefore(startTimeStr, "-");
			
			Date eventTime;
			try {
				eventTime = helper.deduceDateFromSimpleTimeString(startTimeStr, afterNoon);
			} catch (ParseException e1) {
				logger.warn("Could not Parse Date: "+startTimeStr, e1);
				return null;
			}
			String eventTimeStr = new SimpleDateFormat("hh:mm a").format(eventTime);
			
			//SimpleDateFormat formatterAmPm = ;
			
			if (new SimpleDateFormat("a").format(eventTime).equalsIgnoreCase("pm")){
				//This is the first time that we saw a time in the afternoon. 
				//set afteNoon flag to be true - all times that follow will be in the afternoon.
				afterNoon=true;
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			Date dt;
			try {
				return (Date)formatter.parse(eventDateStr+" "+eventTimeStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
//		protected Date convertStrToDate(String startTimeStr) {
//			
//			Date eventDate = Helper.createNew().nextDateFromDayOfTheWeek(getColumnNumber()+1, new Date());			
//			String eventDateStr = new SimpleDateFormat("yyyy-MM-dd").format(eventDate); 			
//			
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
//			Date dt;
//			try {
//				if (afterNoon){
//					String dateStr = eventDateStr+" "+startTimeStr+" pm";
//					dt = (Date)formatter.parse(dateStr);
//					return dt;
//				} else {
//				
//					String dateStr = eventDateStr+" "+startTimeStr+" am";
//					dt = (Date)formatter.parse(dateStr);
//					
//					Calendar cal = GregorianCalendar.getInstance();
//					cal.setTime(dt);
//					if (cal.get(Calendar.HOUR)<6){
//						afterNoon = true;
//					}
//				}
//				
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
//		
	}
	
	

	public class EventsParser_Baptista implements EventsParser {
		private final String TAG_CANCELLED_TODAY = "Cancelled Today";

		private boolean afterNoon = false;
		private Date curDate;
		
		@Override
		public void setColumnNumber(int columnNum) {
		}
		

		public EventsParser_Baptista() {
			curDate = new Date();
		}

		/**
		 * @param parts
		 */
		public Event createEventFromParts(List<String> parts) {
			if (parts.size() <= 0) {
				return null;
			}
			Event event = Event.createNew();
			
			//System.out.println("in createEventFromParts Parts parameter: "+parts);
			
			if (Helper.createNew().containsDate(parts.get(0))){
				//This is a row probably  that contains date
				SimpleDateFormat formatter = new SimpleDateFormat("EEE MMMMM dd, yyyy");
				try {
					curDate = (Date)formatter.parse(parts.get(0));
					afterNoon=false; //reset afterNoon flag to false, because we are starting to parse schedule for a new day 
					return null;
				} catch (ParseException e) {
					//This row does not contain date. Ignore this row
					//TODO:: add warning that the date is unpareable					
					return null;
				}
			}
			
			if (!Helper.createNew().containsTime(parts.get(0))){
				//The first part is not a time string. This is an invalid event.
				return null;
			}
			
			Date startTime = convertStrToDate(parts.get(0));
			event.setStartTime(startTime);
			//parts.remove(0);

			if (parts.size() >= 3 ){
				String instructorName = parts.get(2);
				//Remove a note at the end of the instructor name. The note is a number inside brackets, e.g. "Kristyn Durie (1)"
				instructorName = StringUtils.substringBefore(instructorName,"(");
				event.setInstructorName(instructorName);
			}			
			
			if (parts.size() >= 2 ){
				event.setComment(parts.get(1));
			}
			
			if (parts.size() >= 4 ){
				//String txt3 = StringUtils.join(parts.toArray(), " ");
				event.setComment(event.getComment()+" ("+parts.get(3)+")");
			}

			if (TAG_CANCELLED_TODAY.equals(event.getInstructorName())){
				return null;
			}
			
			return event;
		}
		
		public String asHTMLTable(List<Event> allEvents) {
			String dateStr = "";
			
			String output = "<table>";
			for (int rowNum = 0; rowNum < allEvents.size(); rowNum++) {
								
				String newDateStr = new SimpleDateFormat("EEE MMM dd, yyyy ").format(allEvents.get(rowNum).getStartTime());
				
				if (!newDateStr.equals(dateStr)){
					dateStr = newDateStr;
					output = output + "<tr style=\"border: 1px solid red;\">";
					output = output + "<td colspan=3>"+dateStr+"</td>\n";
					output = output + "</tr>\n";
				}
				
				output = output + "<tr style=\"border: 1px solid red;\">";
				//output = output + "<td style=\"border: 1px solid red;\">";
				output = output + buildHTMLForEvent(allEvents.get(rowNum));					
				//output = output + "</td>";
				output = output + "</tr>\n";
			}
			output = output + "</table>";
			return output;
		}
		
		/**
		 * @param events
		 * @return String output
		 */
		public String buildHTMLForEvent(Event event) {
			
			if (null == event){
				return "";
			}					
			
			String output = "";
			output = output + "<td style=\"border: 1px solid red; color: red\">" +  event.getStartTimeStr() + "</td>";
			output = output + "<td style=\"border: 1px solid red; color: blue\">" + event.getComment()+ "</td>";
			output = output + "<td style=\"border: 1px solid red; color: black\">" + event.getInstructorName() + "</td>";
			
			return output;
		}
		
		protected Date convertStrToDate(String startTimeStr) {
			
			//string is in the format 6:30-8:00 am. Discard everything before -
			startTimeStr = StringUtils.substringBefore(startTimeStr, "-");
			startTimeStr = StringUtils.trim(startTimeStr);
			
			Helper helper = Helper.createNew();
			String eventDateStr = new SimpleDateFormat("yyyy-MM-dd").format(curDate);
			
			Date eventTime;
			try {
				eventTime = helper.deduceDateFromSimpleTimeString(startTimeStr, afterNoon);
			} catch (ParseException e1) {
				logger.warn(e1.getMessage());
				logger.warn(e1.getStackTrace());
				return null;
			}
			String eventTimeStr = new SimpleDateFormat("HH:mm").format(eventTime);
			
			if (new SimpleDateFormat("a").format(eventTime).equalsIgnoreCase("pm")){
				//This is the first time that we saw a time in the afternoon. 
				//set afteNoon flag to be true - all times that follow will be in the afternoon.
				afterNoon=true;
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dt;
			try {
				dt = (Date)formatter.parse(eventDateStr+" "+eventTimeStr);
				return dt;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
}