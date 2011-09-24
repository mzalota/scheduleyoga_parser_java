package com.scheduleyoga.tests;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.scheduleyoga.parser.Event;
import com.scheduleyoga.parser.EventsParser;
import com.scheduleyoga.parser.ISegment;
import com.scheduleyoga.parser.Parser;
import com.scheduleyoga.parser.Segment;
import com.scheduleyoga.parser.Parser.EventsParser_OmYoga;

public class ParserTest {

	
	//@Test
	public void testaaa_twoElements() throws Exception {
		
		
		
		FileInputStream fis = new FileInputStream("test.txt"); 
		
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
		
		EventsParser eventParser = new Parser().new EventsParser_Baptista();
		ISegment segment = Segment.createNewFromElement(element, eventParser);
		List<List<List<Event> > > events = segment.extractEvents();
		
		assertEquals(1, events.size());
		//assertEquals("meditation", events.get(0).getComment());
		
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
