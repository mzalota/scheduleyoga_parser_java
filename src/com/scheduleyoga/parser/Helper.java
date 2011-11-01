package com.scheduleyoga.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public class Helper {

	private Helper() {
		// TODO Auto-generated constructor stub
	}

	public static Helper createNew() {
		Helper newObj = new Helper();
		return newObj;
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
	
	public boolean containsTime(String text) {

		// Pattern that matches "4 pm " --"(\\d{1,2}\\s*[aApP][mM]{0,2})"
		// Pattern that matches "12:30 am " --
		// "([0-1]{0,1}[\\d]:[\\d]{2}\\s*[aApP][mM]{0,2})"
		String ptrn = "(\\d{1,2}\\s*[aApP][mM]{0,2})|([0-1]{0,1}[\\d]:[\\d]{2}\\s*[aApP][mM]{0,2})"; // "[0-9]+\\s*([aApP][mM]{0,2})\\s*$";
		Pattern pattern = Pattern.compile(ptrn);

		Matcher matcher = pattern.matcher(text);
		boolean found = matcher.find();
		if (found) {
			return true;
		}

		ptrn = "[0-1]{0,1}[\\d]:[\\d]{2}"; // "[0-9]+\\s*([aApP][mM]{0,2})\\s*$";
		pattern = Pattern.compile(ptrn);

		matcher = pattern.matcher(text);
		found = matcher.find();
		if (found) {
			return true;
		}

		// // Matches " 4:30 PM "
		// ptrn = "[0-1]{0,1}\\d:[\\d]{2}\\s*[aApP][mM]{0,2}";
		// pattern = Pattern.compile(ptrn);
		//
		// matcher = pattern.matcher(text);
		// found = matcher.find();

		// if (found){
		// System.out.println("Found text: "+matcher.group()
		// +" in position: "+matcher.start() +
		// " till postition: "+matcher.end());
		// } else {
		// System.out.println ("Not found time");
		// }

		return found;
	}

	/**
	 * @param dayOfWeek //value as defined in Calendar e.g. Calendar.MONDAY, Calendar.TUESDAY, etc.
	 * @param today
	 * @return
	 */
	public Date nextDateFromDayOfTheWeek(int dayOfWeek, Date today) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		int dayDiff = dayOfWeek - cal.get(Calendar.DAY_OF_WEEK);
		if (dayDiff < 0)
			dayDiff += 7;

		cal.add(Calendar.DATE, dayDiff);

		return cal.getTime();
	}
	
	public Date deduceDateFromSimpleTimeString(String timeStr, boolean afterNoon) {
		
		String dateStr;
		if (afterNoon){
			dateStr = timeStr+" pm";
		} else {
			dateStr = timeStr+" am";
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
		Date dt;
		try {
			dt = (Date) formatter.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if (afterNoon){
			//the caller specified that its the time is in the afternoon. Believe the caller
			return dt;
		} 
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(dt);
		if (cal.get(Calendar.HOUR)>5){
			//user specified that its "before noon" and time after 6am. We believe user.
			return dt;
		}
		
		//We don't believe the user that this time is before Noon.
		//We found that the time is in wee hours of the morning (0-5:59am). 
		//It is unlikely that a yoga class is offered at such an early time.
		//Hence it must be afternoon. Overwrite user's afterNoon flag to be TRUE.
		return deduceDateFromSimpleTimeString(timeStr,true);
	}
	
	/**
	 * determines how many calendar days schedDate is from today.
	 * @param schedDate
	 * @return
	 */
	public long getDayDiffWithToday(Date schedDate) {
		Calendar calToday = new GregorianCalendar();
		Calendar calSchedDate = new GregorianCalendar();
		calSchedDate.setTime(schedDate);
		
		long MILLSECS_PER_DAY = 1000 * 60 * 60 * 24;
		long schedDateMilsec = calSchedDate.getTimeInMillis() + calSchedDate.getTimeZone().getOffset( calSchedDate.getTimeInMillis() ); 
		long todayMilsec = calToday.getTimeInMillis() + calToday.getTimeZone().getOffset( calToday.getTimeInMillis() );
		long dayDiff = (schedDateMilsec - todayMilsec) / MILLSECS_PER_DAY;
		
		if (dayDiff == 0) {
			//today and schedDate are less then 24 hours away from each other
			if (calSchedDate.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH)){
				//today's date and schedDate's date are the same. So its the same calendar date
				return 0;
			} 
			if (schedDateMilsec > todayMilsec){
				//schedDate is larger then today, so it is tomorrow (1 day diff)
				return 1;
			}
			
			if (todayMilsec > schedDateMilsec){
				//today is larger then schedDate, so shedDate is yesterday (-1 day diff)
				return -1;
			}
		}
		return dayDiff;
	}
	
	/**
	 * @param urlPath
	 * @return
	 */
	public List<String> getURLParts(String urlPath) {
		StringTokenizer stringTokenizer = new StringTokenizer(urlPath, "/");

		List<String> pathElements = new ArrayList<String>();
		while (stringTokenizer.hasMoreElements()) {
			String urlPart = stringTokenizer.nextToken();

			// remove file extension if it exists (e.g. .html or .jsp)
			urlPart = StringUtils.substringBeforeLast(urlPart, ".");
			if (StringUtils.isBlank(urlPart)) {
				// this element is empty
				continue;
			}

			pathElements.add(urlPart);
		}
		return pathElements;
	}
	
	public String nameToURLName(String name) {		
		name = name.replace("'", ""); //remove any apostrophies
		name = name.replaceAll("\\W+", " "); //replace any non-word characters with space
		name = name.trim().toLowerCase(); 		
		name = name.replaceAll("\\s+", "-"); //replace any sequence of white space characters with a single dash - 
		return name;
	}
	
	public String cleanUpInstructorName(String name) {	
		name = name.replace('.', ' ');		
		name = name.trim().toLowerCase(); 		
		name = name.replaceAll("\\s+", " "); //replace any sequence of white space characters with a single dash -	
		if (includesSuspiciousWords(name)){
			return null;
		}
		
		if (name.contains("/")){
			//the string contains two instructors. Take first one, clean it up and return.
			return cleanUpInstructorName(name.split("/")[0]);
		}
		
		if (name.contains("&")){
			//the string contains two instructors. Take first one, clean it up and return.
			return cleanUpInstructorName(name.split("&")[0]);
		}
		
		name = StringUtils.substringBefore(name,"@").trim();
		
		name = WordUtils.capitalize(name,new char[] { '-', ' ', '\'' });
		
		//TODO: rewrite this using regex.
		if (name.substring(name.length()-2, name.length()-1).equals(" ")){
			//the character before last is a space. It may mean that the instructor's last name is abbrivated
			if (StringUtils.countMatches(name, " ") == 1){
				//this string contains only one space, so it ends with a single letter -- its an abbriviation.
				return null;
			}			
		}
		
		return name;
	}
	
	private boolean includesSuspiciousWords(String name){
		//Replace non-word characters and see if anything is left.
		if(name.replaceAll("\\W+", " ").trim().isEmpty()) return true;
		
		if (name.equals("community class teacher")) return true;
		
		if (name.contains(" hour ")) return true;
		if (name.contains(" minute")) return true;
		
		if (name.equals("staff")) return true;
		if (name.startsWith("staff ")) return true;
		if (name.endsWith(" staff")) return true;
		
		if (name.equals("tbd")) return true;		
		if (name.equals("tba")) return true;
		
		if (name.endsWith(" class")) return true;		
		if (name.contains(" class ")) return true;
		
		if (name.contains(" level ")) return true;
		
		if (name.contains("body sculpting")) return true;
		
		if (name.contains("for adults")) return true;
		
		if (name.contains("vinyasa")) return true;
		
		if (name.startsWith("yoga")) return true;
		
		if (name.equals("open")) return true;
		
		if (name.equals("rest")) return true;
		
		if (name.endsWith(" basics")) return true;
		
		if (name.contains("lunchtime")) return true;
		
		if (name.contains("strength yoga")) return true;
		
		if (name.contains("community yoga")) return true;
		
		if (name.contains("hot yoga")) return true;
		
		if (name.contains("teacher independent")) return true;
		
		if (name.contains("illuminated journey grads")) return true;
		
		if (name.contains("yoga center")) return true;
		
		if (name.endsWith("teacher")) return true;
		
		return false;
	}

	
}
