package com.scheduleyoga.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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

	public List<Segment> getRows() {
		if (element.getClass() == HtmlTable.class) {
			HtmlTable table = (HtmlTable) element;
			
			List<Segment> rows = new ArrayList<Segment> (table.getRows().size());
			for (final HtmlTableRow row : table.getRows()) {
				rows.add(Segment.createNewFromElement(row));
			}
			return rows;
		}
				
		if (HtmlTableCell.class.isAssignableFrom(element.getClass())) {
			HtmlTableCell cell = (HtmlTableCell) element;
			List<HtmlElement> pElements = cell.getHtmlElementsByTagName("p");
			return buildList(pElements);
		}
		
		System.out.println("class is "+element.getClass().getName());
		return Collections.EMPTY_LIST;
	}
	
	public boolean isBlank() {
		if (element == null){
			return true;
		}
		return StringUtils.isBlank(element.asText());
	}
	
	protected List<Segment> buildList (List<?> elements){
		
		int elementSize = elements.size();
		List<Segment> segList = new ArrayList<Segment> (elementSize);
		for (int i=0; i<elementSize; i++) {			
			segList.add( Segment.createNewFromElement((HtmlElement) elements.get(i)));
		}
		return segList;
	}
	
	/**
	 * @param row
	 */
	public List<Segment> getCells() {
		HtmlTableRow httpRow = (HtmlTableRow) element;
		
		List<Segment> cells = new ArrayList<Segment> (httpRow.getCells().size());
		for (final HtmlTableCell cell : httpRow.getCells()) {			
			cells.add(Segment.createNewFromElement(cell));
		}
		
		return cells;
	}	

	
	public String asText() {
		return element.asText();
	}	
	
	@Override
	public String toString() {
		return "Segment [element=" + element + "]";
	}	
}

