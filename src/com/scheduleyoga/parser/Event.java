package com.scheduleyoga.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.scheduleyoga.parser.EventsDAO;



@Entity
@Table(name="events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected long id;
	
	@Column(name = "studio_id")
	protected long studio_id;
	
	@Column(name = "comments", length=100)
	protected String comment;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time") //, nullable = false
	protected Date startTime;
	
	@Transient
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
		if (null == startTime){
			return "";
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
		return formatter.format(startTime);
		
		//startTime.toString();
	}

	public void setStartTimeStr(String startTimeStr) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
		Date dt;
		try {
			dt = (Date)formatter.parse(startTimeStr);
			this.startTime = dt;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.startTimeStr = startTimeStr;
	}
	
	public void saveToDB(){
		EventsDAO daoObj = new EventsDAO();
		daoObj.store(this);
	}
}
