package com.scheduleyoga.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
	
	
	private Helper() {
		// TODO Auto-generated constructor stub
	}

	public static Helper createNew(){
		Helper newObj = new Helper();
		return newObj;
	}
	
	public boolean containsTime(String text){
		
		//Pattern that matches "4 pm " --"(\\d{1,2}\\s*[aApP][mM]{0,2})" 
		//Pattern that matches "12:30 am " -- "([0-1]{0,1}[\\d]:[\\d]{2}\\s*[aApP][mM]{0,2})"  
		String ptrn = "(\\d{1,2}\\s*[aApP][mM]{0,2})|([0-1]{0,1}[\\d]:[\\d]{2}\\s*[aApP][mM]{0,2})"; //"[0-9]+\\s*([aApP][mM]{0,2})\\s*$";
		Pattern pattern = Pattern.compile(ptrn);
		
		Matcher matcher = pattern.matcher(text);
		boolean found = matcher.find();		
		if (found) {
			return true;
		}
				
		ptrn = "[0-1]{0,1}[\\d]:[\\d]{2}"; //"[0-9]+\\s*([aApP][mM]{0,2})\\s*$";
		pattern = Pattern.compile(ptrn);
		
		matcher = pattern.matcher(text);
		found = matcher.find();		
		if (found) {
			return true;
		}
		
			
//		// Matches " 4:30 PM "
//		ptrn = "[0-1]{0,1}\\d:[\\d]{2}\\s*[aApP][mM]{0,2}";
//		pattern = Pattern.compile(ptrn);
//		
//		matcher = pattern.matcher(text);
//		found = matcher.find();
		
		
//		if (found){
//			System.out.println("Found text: "+matcher.group() +" in position: "+matcher.start() + " till postition: "+matcher.end());
//		} else {
//			System.out.println ("Not found time");
//		}
		
		return found;
	}
}
