package com.scheduleyoga.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.scheduleyoga.parser.Event;
import com.scheduleyoga.parser.EventsDAO;

@Entity
@Table(name = "parsing_history")
public class ParsingHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected int id;
	
	@Column(name = "studio_id")
	protected int studioId;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_crawling")
	protected Date lastCrawledTime;
	
	@Lob 
	@Column(name = "calendar_html")
	protected String calendarHtml;

	public ParsingHistory() {}
	
	static public ParsingHistory createNew(int studioId, String calendarHtml){
		ParsingHistory newObj = new ParsingHistory();
		newObj.studioId = studioId;
		newObj.calendarHtml = calendarHtml;
		return newObj;
	}
	
	public int getId() {
		return id;
	}

	public int getStudioId() {
		return studioId;
	}

	public Date getLastCrawledTime() {
		return lastCrawledTime;
	}

	public String getCalendarHtml() {
		return calendarHtml;
	}
}
