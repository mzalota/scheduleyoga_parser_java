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
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;


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
		
		int studioID2 = studioIDFromStudioName(studioID);
		switch (studioID2) {
			case STUDIO_ID_OM_YOGA:
				return parseOmYoga();
		}
		return "";
	}
	
	public String parseStudioSite(Studio studio) throws IOException{
		
		studioID = studio.getName();
		switch (studio.getId()) {
			case Studio.STUDIO_ID_OM_YOGA:
				return parseOmYoga();
			case Studio.STUDIO_ID_BABTISTE:
				return parseMindAndBodyOnline2();
		}
		return "";
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
    		if (!containsDate(cell.asText())){
    			return false;
    		}
    	}
    	return true;
	}


	/**
	 * 
	 * @return Studio
	 */
	public Studio getStudio(String studioName) {
		Studio studio = Studio.createNew(studioIDFromStudioName(studioName));
        
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
		WebClient webClient = new WebClient();
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
		WebClient webClient = new WebClient();
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
            if (containsDate(time)){
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
		WebClient webClient = new WebClient();
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
	    		if (containsDate(dateTime)) {
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
	
	protected String parseOmYoga() throws FailingHttpStatusCodeException, IOException{
		
		Studio studio = getStudio(studioID);
			
		WebClient webClient = new WebClient();
        URL url = new URL(studio.getUrl()); 
		
        HtmlPage page = (HtmlPage) webClient.getPage(url);
        HtmlTable table = (HtmlTable) page.getByXPath(studio.getxPath()).get(0);
        
        Segment calendarSegment = Segment.createNewFromElement(table);
        System.out.println("The table is: "+calendarSegment);                
        
        return calendarSegment.asHTMLTable();
	}

	protected String parseMindAndBodyOnline2() throws FailingHttpStatusCodeException, IOException {
		Studio studio = getStudio(studioID);
		
		WebClient webClient = new WebClient();
        URL url = new URL(studio.getUrl());
        
        HtmlPage pageOuter;
        System.out.println("Loading Page 1");
        pageOuter = (HtmlPage)webClient.getPage(url);
        List<FrameWindow> frames = pageOuter.getFrames();        
        HtmlPage page = (HtmlPage) frames.get(1).getEnclosedPage();
        
        
        String xPathParam = xPath.get(studioID); 
        HtmlTable table = (HtmlTable) page.getByXPath(xPathParam).get(0);
        return parseTable2(table);
        
        //return "";
	}
	

	private String parseTable2(HtmlTable table) {
				
		Segment calendarSegment = Segment.createNewFromElement(table);
		return calendarSegment.asHTMLTable();
		
	}


	
	/**
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected void parseMindAndBodyOnline() throws MalformedURLException,
			IOException {
		String urlStr = scheduleURL.get(studioID);
		
		final WebClient webClient = new WebClient();
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
            if (containsDate(time)) {
            
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
			
			if(containsDate(cell.asText())){
				rowsWithDate.put(rowCount, columnCount);
			}
			
			if (Helper.createNew().containsTime(cell.asText())){
				columnsWithTime.put(columnCount,rowCount);
			}
			System.out.println(" Columns: " + cell.asText());
		}
	}

	public boolean containsDate(String text){
		
		text = StringUtils.trim(text.toLowerCase());
				
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		if (text.contains(", "+curYear)){
			return true;
		}
		
		List<String> daysOfWeek = Arrays.asList("sunday",
												"monday",
												"tuesday",
												"wednesday",
												"thursday",
												"friday",
												"saturday",
												"sun",
												"mon",
												"tue",
												"wed",
												"thu",
												"fri",
												"sat");
		Iterator<String> iterator = daysOfWeek.iterator();
		while ( iterator.hasNext() ){		  
			if (StringUtils.containsIgnoreCase(text, iterator.next())){
				return true;
			}
		}
		//System.out.println();
		
		
		return false;
	}
	
//	public boolean containsTime(String text){
//				
//		//Pattern that matches "4 pm " --"(\\d{1,2}\\s*[aApP][mM]{0,2})" 
//		//Pattern that matches "12:30 am " -- "([0-1]{0,1}[\\d]:[\\d]{2}\\s*[aApP][mM]{0,2})"  
//		String ptrn = "(\\d{1,2}\\s*[aApP][mM]{0,2})|([0-1]{0,1}[\\d]:[\\d]{2}\\s*[aApP][mM]{0,2})"; //"[0-9]+\\s*([aApP][mM]{0,2})\\s*$";
//		Pattern pattern = Pattern.compile(ptrn);
//		
//		Matcher matcher = pattern.matcher(text);
//		boolean found = matcher.find();
//		
//		if (found) {
//			return true;
//		}
//			
////		// Matches " 4:30 PM "
////		ptrn = "[0-1]{0,1}\\d:[\\d]{2}\\s*[aApP][mM]{0,2}";
////		pattern = Pattern.compile(ptrn);
////		
////		matcher = pattern.matcher(text);
////		found = matcher.find();
//		
//		
////		if (found){
////			System.out.println("Found text: "+matcher.group() +" in position: "+matcher.start() + " till postition: "+matcher.end());
////		} else {
////			System.out.println ("Not found time");
////		}
//		
//		return found;
//	}

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
        
        System.out.println("Hello Max 00: Found Button:"+btn.toString());
        
        HtmlPage page2;
		try {
			page2 = (HtmlPage) btn.click();
			//printPageSourceCode(page2);
			return page2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("could not load next page");
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
	
//	public static void main(String[] args) throws IOException, SAXException {
//		WebConversation wc = new WebConversation();
//	    WebRequest     req = new GetMethodWebRequest( "https://clients.mindbodyonline.com/ASP/home.asp?studioid=2148" );
//	    WebResponse   resp = wc.getResponse( req );
//	    
//	    Document doc = resp.getDOM();
//	    System.out.println(xmlToString(doc));
//	    
//	}
	

	public static void main2(String[] args) throws IOException {
		// TODO Auto-generated method stub
//
//		HttpClient client = new HttpClient();
//		HttpHead method = new HttpHead("http://www.google.com");
//		int responseCode = client.executeMethod(method);
//		if (responseCode != 200) {
//		    throw new HttpException("HttpMethod Returned Status Code: " + responseCode + " when attempting: " + url);
//		}
//		String rtn = StringEscapeUtils.unescapeHtml(method.getResponseBodyAsString());
//		
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("https://clients.mindbodyonline.com/ASP/main_class.asp?tg=0&vt=&lvl=&view=&trn=&date=5/1/2011&loc=1&page=1&pMode=&prodid=&stype=-7&classid=0&catid=&justloggedin=");
		//https://clients.mindbodyonline.com/ASP/main_class.asp?tg=0&vt=&lvl=&view=&trn=&date=5/1/2011&loc=1&page=1&pMode=&prodid=&stype=-7&classid=0&catid=&justloggedin=
		//https://clients.mindbodyonline.comASP/home.asp?studioid=2148
		try {
			HttpResponse response = httpclient.execute(httpget);		
			HttpEntity entity = response.getEntity();
			
//			// If the response does not enclose an entity, there is no need
//			// to worry about connection release
//			if (entity != null) {
//			    InputStream instream = entity.getContent();
//			    try {
//
//			        BufferedReader reader = new BufferedReader(
//			                new InputStreamReader(instream));
//			        // do something useful with the response
//			        System.out.println(reader.readLine());
//
//			    } catch (IOException ex) {
//
//			        // In case of an IOException the connection will be released
//			        // back to the connection manager automatically
//			        throw ex;
//
//			    } catch (RuntimeException ex) {
//
//			        // In case of an unexpected exception you may want to abort
//			        // the HTTP request in order to shut down the underlying
//			        // connection and release it back to the connection manager.
//			        httpget.abort();
//			        throw ex;
//
//			    } finally {
//
//			        // Closing the input stream will trigger connection release
//			        instream.close();
//
//			    }
//			}
			
			
			
			if (entity != null) {
				
			    InputStream instream = entity.getContent();
			
			    StringWriter writer = new StringWriter();
			    IOUtils.copy(instream, writer);
			    //String theString = writer.toString();
			    
			    
//			    Reader reader = new BufferedReader(
//			    		new InputStreamReader(instream, "ISO-8859-1")); //"UTF-8"
//			    Writer writer = new StringWriter();
//			    int n;
//			    char[] buffer = new char[1024];
//			    while ((n = reader.read(buffer)) != -1) {
//			    	writer.write(buffer, 0, n);
//			    }
			    
//			    int l;
//			    byte[] tmp = new byte[2048];
//			    while ((l = instream.read(tmp)) != -1) {
//			    	System.out.println("bytes read: "+l );
//			    	writer.write(tmp, 0, l);
//			    }
			    instream.close();
			    System.out.println(writer.toString());
			}
		} catch (ClientProtocolException e){
			//TODO: add exception-handling
			throw(e);
		} catch (IOException e){
			//TODO: add exception-handling
			throw(e);
		}
		
	}

	
}