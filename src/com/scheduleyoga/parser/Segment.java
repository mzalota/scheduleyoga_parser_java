package com.scheduleyoga.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.HtmlBreak;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class Segment {
	
	protected HtmlElement element;

	
	public static Segment createNewFromElement (HtmlElement elem) {
		Segment newObj = new Segment();
		newObj.element = elem;
		
		return newObj;
	}

	public HtmlElement getElement() {
		return element;
	}

	public void setElement(HtmlElement element) {
		this.element = element;
	}

	protected List<Segment> getRows() {
		if (element.getClass() == HtmlTable.class) {
			HtmlTable table = (HtmlTable) element;
			
			List<Segment> rows = new ArrayList<Segment> (table.getRows().size());
			for (final HtmlTableRow row : table.getRows()) {
				rows.add(Segment.createNewFromElement(row));
			}
			return rows;
		}
		
		return Collections.EMPTY_LIST;
	}
	
	protected List<Segment> buildList (List<?> elements){
		
		int elementSize = elements.size();
		List<Segment> segList = new ArrayList<Segment> (elementSize);
		for (int i=0; i<elementSize; i++) {			
			segList.add( Segment.createNewFromElement((HtmlElement) elements.get(i)));
		}
		return segList;
	}
	
	protected List<Segment> getCells() {
		HtmlTableRow httpRow = (HtmlTableRow) element;
		
		List<Segment> cells = new ArrayList<Segment> (httpRow.getCells().size());
		for (final HtmlTableCell cell : httpRow.getCells()) {			
			cells.add(Segment.createNewFromElement(cell));
		}
		
		return cells;
	}	

	public String asHTMLTable(){
		String output = "<table>";
		for (final Segment row : this.getRows()){
			output = output+"<tr style=\"border: 1px solid red;\">";
			for (final Segment cell : row.getCells()){
				output = output+"<td style=\"border: 1px solid red;\">";
				if (!cell.isBlank()){ 
					List<Event> events = eventsFromCell(cell.getElement());
					saveEventsToDB(events);
					output = output+buildHTMLForEvents(events);
				}				
				output = output+"</td>";
			}
			output = output+"</tr>\n";
		}
		output = output+"</table>";
		return output;
	}
	
	public String asHTMLTable_horizontal(){
		String output = "<table>";
		for (final Segment row : this.getRows()){
			output = output+"<tr style=\"border: 1px solid red;\">";
			//for (final Segment cell : row.getCells()){
				output = output+"<td style=\"border: 1px solid red;\">";
				if (!row.isBlank()){ 
					List<Event> events = eventsFromCell(row.getElement());
					output = output+buildHTMLForEvents(events);
				}				
				output = output+"</td>";
			//}
			output = output+"</tr>\n";
		}
		output = output+"</table>";
		return output;
	}
	
	
	protected List<Event> eventsFromCell(HtmlElement elementParam) {
		
		List<Event> events = new ArrayList<Event>();
		
		if (!HtmlTableCell.class.isAssignableFrom(elementParam.getClass())) {
			return events;
		}
		
		List<HtmlElement> pElements = new ArrayList<HtmlElement>();
		pElements.add(elementParam);
		
		for (final HtmlElement subRow : pElements){     
			if (subRow == null){
				continue;
			}
			
			List<String> parts = buildParts(subRow);
	
			Helper helper = Helper.createNew();
			List<String> eventParts = new ArrayList<String>();
			for (int i=0; i<parts.size(); i++){
				if(helper.containsTime(parts.get(i))){
					Event event = createEventFromParts(eventParts);
					if (event != null){
						events.add(event);
					}
					eventParts.clear();
				}
				eventParts.add(parts.get(i));
			}
			
			Event event = createEventFromParts(eventParts);	
			if (event != null){
				events.add(event);
			}
		}
		
		return events;
	}

	/**
	 * @param leafElements
	 * @return
	 */
	protected List<String> buildParts(HtmlElement subRow) {
		
		ElementParser textExtractor = new ElementParser();
		textExtractor.parseDocument(subRow.asXml());
		return textExtractor.getValues();
		
	}

	/**
	 * @param events
	 */
	protected void saveEventsToDB(List<Event> events) {
		for (final Event event : events){ 
			event.saveToDB();
		}
	}
	
	/**
	 * @param events
	 * @return String output
	 */
	protected String buildHTMLForEvents(List<Event> events) {
		String output = "";
		for (final Event event : events){ 
			output = output+"++"+event.getStartTimeStr()+"++ <br/>";
			output = output+event.getComment();
			output = output+"<hr />";
		}
		
		System.out.println("NbuildHTMLForEvents: "+output);
		
		return output;
	}

	/**
	 * @param parts
	 */
	protected Event createEventFromParts(List<String> parts) {
		if (parts.size()<=0){
			return null;
		}
		Event event = Event.createNew();
		event.setStartTimeStr(parts.get(0));
		parts.remove(0);

		String txt3 = StringUtils.join(parts.toArray(),"<br/>");
		event.setComment(txt3);	
		return event;
	}
	

	
	protected List<HtmlElement> getLeafElements (HtmlElement elementParam){
		List<HtmlElement> retList = new ArrayList<HtmlElement>();
		
		Iterable<HtmlElement> children = elementParam.getChildElements();
		Iterator<HtmlElement> iter = children.iterator();
		if (!iter.hasNext()){
			//this elementParam does not have any children. return itself
			ElementParser tmpParserExample = new ElementParser();
			String elementValue = tmpParserExample.parseDocument(elementParam.asXml());
			elementValue = StringUtils.trim(elementValue);
			if (!StringUtils.isBlank(elementValue)){
				retList.add(elementParam);
			}
			return retList;
		}		
		
		ElementParser tmpParserExample = new ElementParser();
		String elementValue = tmpParserExample.parseDocument(elementParam.asXml());
		
		if (!StringUtils.isBlank(elementValue)){
			System.out.println("Node Value is: "+elementValue);
			retList.add(elementParam);
		}
		//This elementParam is not a leaf node. Recursively iterate into its children and add all leaf elements to retList array 
		while (iter.hasNext()){
			retList.addAll(getLeafElements(iter.next()));
		}
		
		return retList;
	}
	
	
	public boolean isBlank() {
		if (element == null){
			return true;
		}
		return StringUtils.isBlank(element.asText());
	}
	
	public String asText() {
		return element.asText();
	}	
	
	@Override
	public String toString() {
		return "Segment [element=" + element + "]";
	}	
}

