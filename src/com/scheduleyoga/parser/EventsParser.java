package com.scheduleyoga.parser;

import java.util.List;

public interface EventsParser {

	public Event createEventFromParts(List<String> parts);
	
	public void setColumnNumber (int columnNum);

	//public abstract String asHTMLTable(List<List<List<Event> > > allEvents);
}
