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
import com.scheduleyoga.dao.DBAccess;

public class Segment implements ISegment {


	protected HtmlElement element;

	protected EventsParser eventsParser;

	public static Segment createNewFromElement(HtmlElement elem, EventsParser eventsParserParam) {
		Segment newObj = new Segment();
		newObj.element = elem;
		newObj.eventsParser = eventsParserParam;
		return newObj;
	}

	/* (non-Javadoc)
	 * @see com.scheduleyoga.parser.ISegment#getElement()
	 */
	@Override
	public HtmlElement getElement() {
		return element;
	}

	/* (non-Javadoc)
	 * @see com.scheduleyoga.parser.ISegment#setElement(com.gargoylesoftware.htmlunit.html.HtmlElement)
	 */
	@Override
	public void setElement(HtmlElement element) {
		this.element = element;
	}

	protected List<Segment> getRows() {
		if (element.getClass() == HtmlTable.class) {
			HtmlTable table = (HtmlTable) element;

			List<Segment> rows = new ArrayList<Segment>(table.getRows().size());
			for (final HtmlTableRow row : table.getRows()) {
				rows.add(Segment.createNewFromElement(row, eventsParser));
			}
			return rows;
		}

		return Collections.EMPTY_LIST;
	}

	protected List<Segment> buildList(List<?> elements) {

		int elementSize = elements.size();
		List<Segment> segList = new ArrayList<Segment>(elementSize);
		for (int i = 0; i < elementSize; i++) {
			segList.add(Segment.createNewFromElement((HtmlElement) elements.get(i), eventsParser));
		}
		return segList;
	}

	protected List<Segment> getCells() {
//		HtmlTableRow httpRow = (HtmlTableRow) element;
//
//		List<Segment> cells = new ArrayList<Segment>(httpRow.getCells().size());
//		for (final HtmlTableCell cell : httpRow.getCells()) {
//			cells.add(Segment.createNewFromElement(cell, eventsParser));
//		}
//
//		return cells;
		return getCells(element);
	}

	protected List<Segment> getCells(HtmlElement elementParam) {
		HtmlTableRow httpRow = (HtmlTableRow) elementParam;

		List<Segment> cells = new ArrayList<Segment>(httpRow.getCells().size());
		for (final HtmlTableCell cell : httpRow.getCells()) {
			cells.add(Segment.createNewFromElement(cell, eventsParser));
		}

		return cells;
	}
	
	public List<List<List<Event> > >  extractEvents() {
		
		List<List<List<Event> > > allEvents = new ArrayList<List<List<Event> > >();
		
		int rowNum = 0;
		for (final Segment row : this.getRows()) {
			allEvents.add(new ArrayList<List<Event> >());
			int colNum = 0;
			for (final ISegment cell : row.getCells()) {
				if (!cell.isBlank()) {
					List<Event> events = eventsFromCell(cell.getElement(), colNum);
					//saveEventsToDB(events);
					allEvents.get(rowNum).add(events);
				} else {
					allEvents.get(rowNum).add(new ArrayList<Event>());
				}
				colNum++;
			}
			rowNum++;
		}
		
		return allEvents;
	}
	
	
	public String asHTMLTable_horizontal() {
		String output = "<table>";
		for (final ISegment row : this.getRows()) {
			output = output + "<tr style=\"border: 1px solid red;\">";
			// for (final Segment cell : row.getCells()){
			output = output + "<td style=\"border: 1px solid red;\">";
			if (!row.isBlank()) {
				Event event = eventFromRow(row.getElement(),0);
				if (null != event){
					List<Event> events = new ArrayList<Event>(1);
					events.add(event);
					output = output + buildHTMLForEvents(events);
				}
			}
			output = output + "</td>";
			// }
			output = output + "</tr>\n";
		}
		output = output + "</table>";
		return output;
	}

	public List<Event> eventsFromCell(HtmlElement elementParam, int columnNum) {

		List<Event> events = new ArrayList<Event>();
		if (elementParam == null) {
			return events;
		}

		if (!HtmlTableCell.class.isAssignableFrom(elementParam.getClass())) {
			return events;
		}

		List<String> parts = buildParts(elementParam);

		Helper helper = Helper.createNew();
		List<String> eventParts = new ArrayList<String>();
		for (int i = 0; i < parts.size(); i++) {
			if (helper.containsTime(parts.get(i))) {
				eventsParser.setColumnNumber(columnNum);
				Event event = eventsParser.createEventFromParts(eventParts);
				if (event != null) {
					events.add(event);
				}
				eventParts.clear();
			}
			eventParts.add(parts.get(i));
		}

		eventsParser.setColumnNumber(columnNum);
		Event event = eventsParser.createEventFromParts(eventParts);
		if (event != null) {
			events.add(event);
		}

		return events;
	}

	/* (non-Javadoc)
	 * @see com.scheduleyoga.parser.ISegment#eventFromRow(com.gargoylesoftware.htmlunit.html.HtmlElement, int)
	 */
	@Override
	public Event eventFromRow(HtmlElement elementParam, int columnNum) {

		Event event = Event.createNew();
		if (elementParam == null) {
			return event;
		}

		if (!HtmlTableRow.class.isAssignableFrom(elementParam.getClass())) {
			return event;
		}
		
		List<Segment> cells = getCells(elementParam);
		
		List<String> parts = new ArrayList<String>();
		for (int i=0; i<cells.size();i++){
			List<String> partsTmp = buildParts(cells.get(i).getElement());
			if (partsTmp.size()>0){
				parts.add(partsTmp.get(0));
			}
		}
		
		//List<String> parts = buildParts(elementParam);
		System.out.println ("Found following parts: "+parts);

		event = eventsParser.createEventFromParts(parts);
		
//		Helper helper = Helper.createNew();
//		List<String> eventParts = new ArrayList<String>();
//		for (int i = 0; i < parts.size(); i++) {
//			if (helper.containsTime(parts.get(i))) {
//				eventsParser.setColumnNumber(columnNum);
//				Event event = eventsParser.createEventFromParts(eventParts);
//				if (event != null) {
//					events.add(event);
//				}
//				eventParts.clear();
//			}
//			eventParts.add(parts.get(i));
//		}
//
//		eventsParser.setColumnNumber(columnNum);
//		Event event = eventsParser.createEventFromParts(eventParts);
//		if (event != null) {
//			events.add(event);
//		}

		return event;
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
	 * @return String output
	 */
	protected String buildHTMLForEvents(List<Event> events) {
		String output = "";
		for (final Event event : events) {
			output = output + "<div style=\"color: red\">" + event.getStartTimeStr() + "</div> <br/>";
			output = output + "<div style=\"color: blue\">" + event.getComment()+ "</div> <br/>";
			output = output + "<div style=\"color: black\">" + event.getInstructorName() + "</div> <br/>";
			output = output + "<hr />";
		}

		System.out.println("BuildHTMLForEvents: " + output);

		return output;
	}




	/* (non-Javadoc)
	 * @see com.scheduleyoga.parser.ISegment#isBlank()
	 */
	@Override
	public boolean isBlank() {
		if (element == null) {
			return true;
		}
		return StringUtils.isBlank(element.asText());
	}

	/* (non-Javadoc)
	 * @see com.scheduleyoga.parser.ISegment#asText()
	 */
	@Override
	public String asText() {
		return element.asText();
	}

	/* (non-Javadoc)
	 * @see com.scheduleyoga.parser.ISegment#toString()
	 */
	@Override
	public String toString() {
		return "Segment [element=" + element + "]";
	}
}


//protected List<HtmlElement> getLeafElements(HtmlElement elementParam) {
//List<HtmlElement> retList = new ArrayList<HtmlElement>();
//
//Iterable<HtmlElement> children = elementParam.getChildElements();
//Iterator<HtmlElement> iter = children.iterator();
//if (!iter.hasNext()) {
//	// this elementParam does not have any children. return itself
//	ElementParser tmpParserExample = new ElementParser();
//	String elementValue = tmpParserExample.parseDocument(elementParam
//			.asXml());
//	elementValue = StringUtils.trim(elementValue);
//	if (!StringUtils.isBlank(elementValue)) {
//		retList.add(elementParam);
//	}
//	return retList;
//}
//
//ElementParser tmpParserExample = new ElementParser();
//String elementValue = tmpParserExample.parseDocument(elementParam
//		.asXml());
//
//if (!StringUtils.isBlank(elementValue)) {
//	System.out.println("Node Value is: " + elementValue);
//	retList.add(elementParam);
//}
//// This elementParam is not a leaf node. Recursively iterate into its
//// children and add all leaf elements to retList array
//while (iter.hasNext()) {
//	retList.addAll(getLeafElements(iter.next()));
//}
//
//return retList;
//}
