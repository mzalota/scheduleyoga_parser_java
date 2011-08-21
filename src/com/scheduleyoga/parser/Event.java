package com.scheduleyoga.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.scheduleyoga.parser.EventsDAO;
import com.scheduleyoga.dao.Studio;


@Entity
@Table(name="events")
public class Event {

	protected long id;
	protected long studio_id;
	protected String comment;
	protected Date startTime;
	protected String instructorName;
	protected Studio studio = null;
	protected String startTimeStr;
	
	protected Event() {
		// TODO Auto-generated constructor stub
	}
	
	public static Event createNew(){
		Event newObj = new Event();
		
		return newObj;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public long getId() {
		return id;
	}
	
	protected void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "instructor_name", length=100)
	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	@Column(name = "comments", length=100)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time") //, nullable = false
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity=Studio.class )
    @JoinColumn(name="studio_id")
    public Studio getStudio() {
        return studio;
    }	
	
	protected void setStudio(Studio studio) {
		this.studio = studio;
	}

	
	public void saveToDB(){
		EventsDAO daoObj = new EventsDAO();
		daoObj.store(this);
	}
}
