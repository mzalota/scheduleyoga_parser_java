package com.scheduleyoga.parser;

public class Event {

	protected String comment;
	protected String startTimeStr;
	
	protected Event() {
		// TODO Auto-generated constructor stub
	}
	
	public static Event createNew(){
		Event newObj = new Event();
		
		return newObj;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	
	
}
