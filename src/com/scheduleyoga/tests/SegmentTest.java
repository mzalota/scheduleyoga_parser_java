package com.scheduleyoga.tests;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.junit.internal.runners.TestMethod;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.scheduleyoga.parser.ElementParser;
import com.scheduleyoga.parser.Event;
import com.scheduleyoga.parser.EventsParser;
import com.scheduleyoga.parser.ISegment;
import com.scheduleyoga.parser.Parser;
import com.scheduleyoga.parser.Segment;
import com.scheduleyoga.parser.Parser.EventsParser_Baptista;
import com.scheduleyoga.parser.Parser.EventsParser_OmYoga;
import com.scheduleyoga.parser.SegmentHorizontal;

public class SegmentTest {

//	@Test
//	public void testEventsFromCell_printString() throws Exception {
////		String xmlSample = "<td id=\"maxtest\" valign=\"top\" style=\"border: 1px solid #9fc6e4; text-align: left;\">\r\n" + 
////				"		<p>\r\n" + 
////				"		<p>&nbsp;</p>\r\n" + 
////				"		<p>&nbsp;</p>\r\n" + 
////				"		<p>\r\n" + 
////				"		<span style=\"color: #0079c2;\">\r\n" + 
////				"		<span style=\"font-size: 8pt;\">\r\n" + 
////				"		<strong>5:30</strong>\r\n" + 
////				"		<br>\r\n" + 
////				"		<span style=\"color: #800080;\">meditation</span>\r\n" + 
////				"		$5\r\n" + 
////				"		<br>\r\n" + 
////				"		<a href=\"/index.php?option=com_content&view=article&id=46:cyndi-bio&catid=37&Itemid=59\">cyndi</a>\r\n" + 
////				"		</span>\r\n" + 
////				"		</span>\r\n" + 
////				"		</p>\r\n" + 
////				"		<p>\r\n" + 
////				"		<p>\r\n" + 
////				"		<p>\r\n" + 
////				"		</td>";
//		
//		String xmlSample = "" +
//		"<td id=\"maxtest\" valign=\"top\" style=\"border: 1px solid #9fc6e4; text-align: left;\">" +"\r\n"+ 
//		"	<p>" +"\r\n"+ 
//		"		<span style=\"color: #0079c2;\">" +"\r\n"+ 
//		"			<span style=\"font-size: 8pt;\">" +"\r\n"+ 
//		"				<strong>5:30</strong>" +"\r\n"+ 
//		"				<br>" +"\r\n"+ 
//		"				<span style=\"color: #800080;\">meditation</span>" +"\r\n"+ 
//		"				$5" +"\r\n"+ 
//		"				<br>" + "\r\n"+
//		"				<a href=\"/index.php\">cyndi</a>" +"\r\n"+ 
//		"			</span>" +"\r\n"+ 
//		"		</span>" +"\r\n"+ 
//		"	</p>" +"\r\n"+ 
//		"</td>";		
//		
//		HtmlElement element = createHtmlElement(xmlSample);
//		
//		String parsedXML = element.asXml();
//		assertEquals(xmlSample, parsedXML);
//	}
	
	@Test
	public void testEventsFromCell_singleElement() throws Exception {
		
		String xmlSample = "" +
		"<td id=\"maxtest\" valign=\"top\" style=\"border: 1px solid #9fc6e4; text-align: left;\">" +"\r\n"+ 
		"	<p>" +"\r\n"+ 
		"		<span style=\"color: #0079c2;\">" +"\r\n"+ 
		"			<span style=\"font-size: 8pt;\">" +"\r\n"+ 
		"				<strong>5:30</strong>" +"\r\n"+ 
		"			</span>" +"\r\n"+ 
		"		</span>" +"\r\n"+ 
		"	</p>" +"\r\n"+ 
		"</td>";		
		
		HtmlElement element = createHtmlElement(xmlSample);

		EventsParser eventParser = new Parser().new EventsParser_OmYoga();
		Segment segment = Segment.createNewFromElement(element,eventParser);
		List<Event> events = segment.eventsFromCell(element,0);
		
		assertEquals(1, events.size());
		assertEquals("05:30", events.get(0).getStartTimeStr());
	}
	
	
	@Test
	public void testEventsFromCell_twoElements() throws Exception {
		
		String xmlSample = "" +
		"<td id=\"maxtest\" valign=\"top\" style=\"border: 1px solid #9fc6e4; text-align: left;\">" +"\r\n"+ 
		"	<p>" +"\r\n"+ 
		"		<span style=\"color: #0079c2;\">" +"\r\n"+ 
		"			<span style=\"font-size: 8pt;\">" +"\r\n"+ 
		"				<strong>5:30</strong>" +"\r\n"+ 
		"				<span style=\"color: #800080;\">meditation</span>" +"\r\n"+ 		
		"			</span>" +"\r\n"+ 
		"		</span>" +"\r\n"+ 
		"	</p>" +"\r\n"+ 
		"</td>";		
		
		HtmlElement element = createHtmlElement(xmlSample);

		EventsParser eventParser = new Parser().new EventsParser_OmYoga();
		Segment segment = Segment.createNewFromElement(element,eventParser);
		List<Event> events = segment.eventsFromCell(element,0);
		
		assertEquals(1, events.size());
		assertEquals("meditation", events.get(0).getComment());
		
	}	
	
	@Test
	public void testEventsFromCell_threeElements() throws Exception {
		
		String xmlSample = "" +
		"<td id=\"maxtest\" valign=\"top\" style=\"border: 1px solid #9fc6e4; text-align: left;\">" +"\r\n"+ 
		"	<p>" +"\r\n"+ 
		"		<span style=\"color: #0079c2;\">" +"\r\n"+ 
		"			<span style=\"font-size: 8pt;\">" +"\r\n"+ 
		"				<strong>5:30</strong>" +"\r\n"+ 
		"				<br>" +"\r\n"+ 
		"				<span style=\"color: #800080;\">meditation</span>" +"\r\n"+ 	
		"				$5" +"\r\n"+ 
		"				<br>" + "\r\n"+		
		"			</span>" +"\r\n"+ 
		"		</span>" +"\r\n"+ 
		"	</p>" +"\r\n"+ 
		"</td>";		
		
		HtmlElement element = createHtmlElement(xmlSample);

		EventsParser eventParser = new Parser().new EventsParser_OmYoga();
		Segment segment = Segment.createNewFromElement(element,eventParser);
		List<Event> events = segment.eventsFromCell(element,0);
		
		assertEquals(1, events.size());
		assertEquals("05:30", events.get(0).getStartTimeStr());
		//assertEquals("meditation $5", events.get(0).getComment());
	}	
	
	@Test
	public void testEventsFromCell_smthelse() throws Exception {
		
		String xmlSample = "" +
		"<td id=\"maxtest\" valign=\"top\" style=\"border: 1px solid #9fc6e4; text-align: left;\">" +"\r\n"+ 
		"\r\n" + 
		"<p><span style=\"color: #0079c2;\"><span style=\"font-size: 8pt;\"> </span></span></p>\r\n" + 
		"<p><span style=\"color: #0079c2;\"><span style=\"font-size: 8pt;\"><strong>6:00</strong><br><a href=\"/index.php?option=com_content&amp;view=article&amp;id=164%3Aom-yoga-stretch-open&amp;catid=57&amp;Itemid=104\" title=\"OM yoga Class Descriptions\"><span style=\"color: #0fae09;\">express</span></a><br></span></span><a title=\"Edward Jones\" href=\"/index.php?option=com_content&amp;view=article&amp;id=101:edward-jones&amp;catid=59&amp;Itemid=197\"><span style=\"font-size: 8pt;\"> </span></a><a href=\"/index.php?option=com_content&amp;view=article&amp;id=146%3Aeli-jacobowitz&amp;catid=59&amp;Itemid=104\">eli</a><span style=\"color: #0079c2;\"><br></span></p>\r\n" + 
		"<p>&nbsp;</p>\r\n" + 
		"<p>&nbsp;</p>\r\n" + 
		"<p>&nbsp;</p>\r\n" + 
		"<p>&nbsp;</p>\r\n" + 
		"<p>&nbsp;</p>"+
		"</td>";		
		
		HtmlElement element = createHtmlElement(xmlSample);

		EventsParser eventParser = new Parser().new EventsParser_OmYoga();
		Segment segment = Segment.createNewFromElement(element,eventParser);
		List<Event> events = segment.eventsFromCell(element,0);
		
		assertEquals(1, events.size());
		assertEquals("06:00", events.get(0).getStartTimeStr());
		assertEquals("express", events.get(0).getComment());
		assertEquals("eli", events.get(0).getInstructorName());
		//assertEquals("meditation $5", events.get(0).getComment());
	}	
	
	//@Test
	public void testEventsFromCell_fourTextElements() throws Exception {
		
		String xmlSample = "" +
		"<html lang=\"en-gb\" xml:lang=\"en-gb\" xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
		"<head>\r\n" + 
		"<meta content=\"text/html; charset=utf-8\" http-equiv=\"content-type\">"+
		"</head>"+
		"<body>"+
		"<td id=\"maxtest\" valign=\"top\" style=\"border: 1px solid #9fc6e4; text-align: left;\">" +"\r\n"+ 
		"	<p>" +"\r\n"+ 
		"		<span style=\"color: #0079c2;\">" +"\r\n"+ 
		"			<span style=\"font-size: 8pt;\">" +"\r\n"+ 
		"				<strong>5:30</strong>" +"\r\n"+ 
		"				<br>" +"\r\n"+ 
		"				<span style=\"color: #800080;\">meditation</span>" +"\r\n"+ 
		"				$5" +"\r\n"+ 
		"				<br>" + "\r\n"+
		"				<a href=\"/index.php\">cyndi</a>" +"\r\n"+ 
		"			</span>" +"\r\n"+ 
		"		</span>" +"\r\n"+ 
		"	</p>" +"\r\n"+ 
		"</td>"+
		"</body>"+
		"</html>";		
		
		HtmlElement element = createHtmlElement(xmlSample);

		EventsParser eventParser = new Parser().new EventsParser_OmYoga();
		Segment segment = Segment.createNewFromElement(element,eventParser);
		List<Event> events = segment.eventsFromCell(element,0);
		
		assertEquals(4, events.size());
	}

	@Test
	public void testSegmentTest_NestedHTML() throws Exception {
		String xmlSample = "" +
		"	<td colspan=\"2\" class=\"textSmall\">\r\n" + 
		"	  <a name=\"an1\">\r\n" + 
		"	    <b>\r\n" + 
		"	                           \r\n" + 
		"	      <span class=\"headText\">\r\n" + 
		"	                    Sun            \r\n" + 
		"	      </span>\r\n" + 
		"	                September 04, 2011        \r\n" + 
		"	    </b>\r\n" + 
		"	  </a>\r\n" + 
		"	</td>";
	
		ElementParser textExtractor = new ElementParser();
		textExtractor.parseDocument(xmlSample);
		List<String> values = textExtractor.getValues();
		
		assertEquals(2, values.size());
		
//		HtmlElement element = createHtmlElement(xmlSample);
//
//		EventsParser eventParser = new Parser().new EventsParser_OmYoga();
//		Segment segment = Segment.createNewFromElement(element,eventParser);
//		List<Event> events = segment.eventsFromCell(element,0);
//		
//		assertEquals(1, events.size());
//		assertEquals("meditation", events.get(0).getComment());
	}
	
	@Test
	public void testAaa_twoElements() throws Exception {
				
		//SETUP
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		FileInputStream fis = new FileInputStream("C:\\workspace_springsource\\SchYogaParser\\src\\com\\scheduleyoga\\tests\\babtiste_studio.html"); 
		
		StringBuilder text = new StringBuilder();
		
		Scanner scanner = new Scanner(fis);
		try {
		      while (scanner.hasNextLine()){
		        text.append(scanner.nextLine());
		      }
		    }
		    finally{
		      scanner.close();
		    }
		String calendarXHtml = text.toString();
		
		HtmlElement element = createHtmlElement(calendarXHtml);

		//String new Parser().parseTable2(element);
		
		//EXECUTE
		EventsParser eventParser = new Parser().new EventsParser_Baptista();
		SegmentHorizontal segment = (SegmentHorizontal) SegmentHorizontal.createNewFromElement(element, eventParser);
		List<Event> events = segment.extractEvents();
		
		//System.out.println(events.toString());
		
		//ASSERT
		assertEquals(71, events.size());
		
		Event lastEvent = events.get(67);
		assertEquals("Samantha Moland (32)", lastEvent.getInstructorName());
		assertEquals("All Levels Brookline", lastEvent.getComment());
		
		assertEquals("2011-09-04 16:30", formatter.format(lastEvent.getStartTime()));
	}	

	
	/**
	 * @param xmlSample
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected HtmlElement createHtmlElement(String xmlSample)
			throws MalformedURLException, IOException {
		String xmlSampleWithTags = "<table><tr><div id=\"maxtest2\""+xmlSample+"</div></tr></table>";
		
		URL url = new URL("http://www.example.com");
		StringWebResponse response = new StringWebResponse(xmlSampleWithTags, url);
		WebClient client = new WebClient();
		HtmlPage page = HTMLParser.parseHtml(response, client.getCurrentWindow());
		
		//HtmlElement element = page.getHtmlElementById("maxtest");
		
	
		
		
		HtmlElement element = page.getHtmlElementById("maxtest2");
		Iterable<HtmlElement> children = element.getChildElements();
		Iterator<HtmlElement> iter = children.iterator();
		
		return iter.next();
	}
	
	
}
