package com.scheduleyoga.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import sun.util.calendar.Gregorian;

public class CalendarRenderer {

	private List<Event> events; 
	
	public static CalendarRenderer createNew(){
		CalendarRenderer newObj = new CalendarRenderer();
		return newObj;
	}	
	
	protected CalendarRenderer() {
		this.events = new ArrayList<Event>();
	}
	
	public void addEvent(Event event){
		if (event == null) 
			return;
		this.events.add(event);
	}
	
	public void addEvents(Collection<Event> events){
		if (events == null) 
			return;
		this.events.addAll(events);
	}
	
	public int countEvents (){
		return events.size();
	}
	
	public SortedSet<Date> getAllDistinctDates(){

		SortedSet<Date> allDates = new TreeSet<Date>();
		
		//TODO: inside Map do not use List<Event> of fixed length - it is awkward. instead Use Map<int, Event>
		//Helper helper = Helper.createNew();
		//int dayDiff = helper.getDayDiff(event.getStartTime(), firstDate);
		for (Event event : events){
			
			Calendar startTimeCal = new GregorianCalendar();
			startTimeCal.setTime(event.getStartTime());
			
			Calendar justDateCal = new GregorianCalendar();			
			justDateCal.set(startTimeCal.get(Calendar.YEAR),
						 startTimeCal.get(Calendar.MONTH),
						 startTimeCal.get(Calendar.DATE));
			
			allDates.add(justDateCal.getTime());			
		}
		
		return allDates;
	}
	
	public List<Date> getFormattedDates(final Date startDate, int numDays){
		List<Date> allDates = new ArrayList();

		Helper helper = Helper.createNew();
		Date tmpDate = helper.justDateWithoutTime(startDate);
		Calendar tmpCal = new GregorianCalendar ();
		for (int i=0;i<numDays;i++){
			tmpCal.setTime(tmpDate);
			tmpCal.add(Calendar.DAY_OF_MONTH, i);
			allDates.add(tmpCal.getTime());
		}
		
		//startDate = helper.justDateWithoutTime(startDate);
//		
//		Calendar tmpCal = new GregorianCalendar ();
//		tmpCal.setTime(startDate);
//		tmpCal.add(Calendar.DAY_OF_MONTH, numDays+1);
//		
//		Date endDate = tmpCal.getTime();
		
		
		
		
//		for (Date dt : getAllDistinctDates()){
//			if (helper.getDayDiff(dt, startDate)<0){
//				continue;
//			}
//			if (helper.getDayDiff(dt, startDate)>numDays){
//				break;
//			}
//			allDates.add(dt);
//		}
//		
		return allDates;
	}
	
	
	public SortedSet<Date> getAllDistinctTimes(){
		SortedSet<Date> allTimes = new TreeSet<Date>();
		
		Calendar startTimeCal = new GregorianCalendar();
		Calendar justDateCal = new GregorianCalendar();	
		for (Event event : events){
			startTimeCal.setTime(event.getStartTime());
			justDateCal.set(1900,1,1, startTimeCal.get(Calendar.HOUR_OF_DAY), startTimeCal.get(Calendar.MINUTE),0);
			allTimes.add(justDateCal.getTime());			
		}
		
		return allTimes;
	}

	public List<Event> getEventsDateAndTime(Date date, Date time){
		
		Map<Date, List<Event>> map = new HashMap<Date, List<Event>>();
		Calendar tmpCal = new GregorianCalendar(); //Used to reset seconds field to zero
		for (Event event : events){
			tmpCal.setTime(event.getStartTime());
			tmpCal.set(Calendar.SECOND, 0);
			tmpCal.set(Calendar.MILLISECOND, 0);
			if(!map.containsKey(tmpCal.getTime())){
				map.put(tmpCal.getTime(), new ArrayList<Event>());
			}
			map.get(tmpCal.getTime()).add(event);
		}
		
		Calendar targetTimeCal = new GregorianCalendar();
		targetTimeCal.setTime(time);
		
		
		Calendar targetDateCal = new GregorianCalendar();
		targetDateCal.setTime(date);
		targetDateCal.set(Calendar.HOUR_OF_DAY, targetTimeCal.get(Calendar.HOUR_OF_DAY));
		targetDateCal.set(Calendar.MINUTE, targetTimeCal.get(Calendar.MINUTE));
		targetDateCal.set(Calendar.SECOND, 0);
		targetDateCal.set(Calendar.MILLISECOND, 0);
		
		if(map.containsKey(targetDateCal.getTime())){
			return map.get(targetDateCal.getTime());
		}
		
		return Collections.EMPTY_LIST;		
	}
	
	public String render(Date firstDate, int daysInCalendar){
		
		if (countEvents()<=0){
			return "<div></div>";
		}
		
		//TODO: inside Map do not use List<Event> of fixed length - it is awkward. instead Use Map<int, Event>
		Map<String, List<Event>> cal = new HashMap<String, List<Event>>();
		Helper helper = Helper.createNew();
		for (Event event : events){
			int dayDiff = helper.getDayDiff(event.getStartTime(), firstDate);
			if (dayDiff<0){
				//The event is before FirstDate, so ignore it
				continue;
			}
			
			if (dayDiff >= daysInCalendar){
				//The event is after daysInCalendar days from firstDate 
				continue;
			}
			
			if (!cal.containsKey(event.getStartTimeStr())){
				//Initialize list to be of length daysInCalendar with each element being null
				List<Event> newList =  new ArrayList<Event>(Collections.nCopies(daysInCalendar,(Event) null));
				cal.put(event.getStartTimeStr(), newList);
			}				

			List<Event> tmp = cal.get(event.getStartTimeStr());
			//System.out.println("The size of tmpArray is: "+tmp.size()+" it should be "+daysInCalendar+" dayDiff is "+dayDiff+" firstDate "+firstDate+" event Date: "+event.getStartTime());
			
			tmp.set(dayDiff, event);
		}
				
		
		String outty = "";
		
				
		//outty = buildHTMLOutput(daysInCalendar, cal);
		outty = buildHTMLOutput2(daysInCalendar, cal, firstDate);
		
		return outty;
	}

	/**
	 * @param daysInCalendar
	 * @param cal
	 * @return
	 */
	protected String buildHTMLOutput2(int daysInCalendar, Map<String, List<Event>> cal, Date firstDate) {
		
		Calendar tmpCalnedar = new GregorianCalendar();
		tmpCalnedar.setTime(firstDate);
		
		SortedSet<String> startTimes = new TreeSet<String>(cal.keySet());
		String outty = "";
		outty += "<table class=\"calendar\"> \n";
		
		outty += "<tr>\n";
		outty += "<th class=\"cal_time_cell\">Start Time</th>\n";
		
		for (int columnIdx=0; columnIdx< daysInCalendar; columnIdx++) {			
			outty += "<th class=\"cal_info_cell summary\"> ";
			String colHeading = new SimpleDateFormat("EEE '<br>' MMM d").format(tmpCalnedar.getTime());
			colHeading = colHeading.trim();
			outty += colHeading;
			outty += "</th> \n";
			
			tmpCalnedar.roll(Calendar.DATE, true);			
		}
		outty += "</tr>\n";
		
		for (String startTime : startTimes){
			
			String rowStr = "";
			rowStr += "<tr>"; //class=\"cal_row\"
			rowStr += "<td class=\"cal_time_cell\">";
			rowStr += "<span class=\"carrot_compact\">&nbsp;</span>";
			rowStr += startTime;
			rowStr +="</td>";
			for (int columnIdx=0; columnIdx< daysInCalendar; columnIdx++) {
				//for (Event event : cal.get(startTime)){
				rowStr += "<td  class=\"cal_info_cell summary\" >"; 
				Event event = cal.get(startTime).get(columnIdx);
				if (event != null){
					//We have an event to display in this column
					//
					
					rowStr += "<abbr class=\"dtstart\" title=\"";
					rowStr += new SimpleDateFormat("yyyy-MM-dd").format(event.getStartTime());
					rowStr += "T";
					rowStr += new SimpleDateFormat("HH:mm:00.000").format(event.getStartTime());
					rowStr += "\">";
					rowStr += " </abbr>";
					
					rowStr += "<div class=\"summary\">"; 
					rowStr += cal.get(startTime).get(columnIdx).getComment();
					rowStr += "</div>"; 
					
					rowStr += "<div class=\"location cal_studio_name\"> Studio: ";
					rowStr += "<a href=\"/studios/new-york/" +
							event.getStudio().getNameForUrl() +
							"/"+ new SimpleDateFormat("yyyy-MM-dd").format(event.getStartTime())+".html\" "+							
							"title=\"Schedule for Yoga Studio: "+event.getStudio().getName()+"\">"; 
					//rowStr += "<a href="/studios/$stateNameUrl/${event.getStudio().getNameForUrl()}/${date.format('yyyy-MM-dd',$event.getStartTime())}.html" title="Schedule for Yoga Studio: "+event.getStudio().getName()">" 
					rowStr += cal.get(startTime).get(columnIdx).getStudio().getName();
					rowStr += "</a>"; 
					rowStr += "</div>"; 
					//</a>
					//rowStr +="</span> ";
				} else {
					rowStr +="&nbsp;";
				}
				rowStr += "</td>";
			}
			//surround in a div
			rowStr += "</tr>";
			outty += rowStr;
		}
		outty += "</table>";
		return outty;
	}
	
	/**
	 * @param daysInCalendar
	 * @param cal
	 * @return
	 */
	protected String buildHTMLOutput(int daysInCalendar, Map<String, List<Event>> cal) {
		String outty = "";
		SortedSet<String> startTimes = new TreeSet<String>(cal.keySet());
		
		for (String startTime : startTimes){
			
			String rowStr = "";
			
			rowStr += "<div class=\"cal_time_cell\" >";
			rowStr += "<span class=\"carrot\">&nbsp;</span>";
			rowStr += startTime;
			rowStr +="</div>";
			for (int columnIdx=0; columnIdx< daysInCalendar; columnIdx++) {
				//for (Event event : cal.get(startTime)){
				rowStr += "<div  class=\"cal_info_cell summary\" >"; 
				if (cal.get(startTime).get(columnIdx) != null){
					//We have an event to display in this column
					//rowStr +="<span class=\"summary\" >";
					rowStr += cal.get(startTime).get(columnIdx).getComment();
					//rowStr +="</span> ";
				} else {
					rowStr +="&nbsp;  ";
				}
				rowStr += "</div>";
			}
			//surround in a div
			rowStr = "<div class=\"cal_row\" >"+rowStr+"</div>";
			outty += rowStr;
		
			//System.out.println("Outty is: "+outty); ff
		}
		return outty;
	}

}
