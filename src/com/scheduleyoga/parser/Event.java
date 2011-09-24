package com.scheduleyoga.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.scheduleyoga.parser.EventsDAO;
import com.scheduleyoga.dao.DBAccess;
import com.scheduleyoga.dao.Studio;


@Entity
@Table(name="events")
public class Event {

	@Override
	public String toString() {
		return "Event [id=" + id + ", studio_id=" + studio_id + ", comment="
				+ comment + ", startTime=" + startTime + ", instructorName="
				+ instructorName + ", studio=" + studio + ", startTimeStr="
				+ startTimeStr + ", createdOn=" + createdOn + ", modifiedOn="
				+ modifiedOn + "]";
	}

	protected long id;
	protected long studio_id;
	protected String comment;
	protected Date startTime;
	protected String instructorName;
	protected Studio studio = null;
	protected String startTimeStr;
	protected Date createdOn;
	protected Date modifiedOn;
	
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
		
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	public Date getCreatedOn() {
		return (this.createdOn == null) ? new Date() : this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_on")
	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public void saveToDB(){
		EventsDAO daoObj = new EventsDAO();
		daoObj.store(this);
	}
	
	public static List<Event> findEventsForStudioForDate(String studioNameUrl, Date date){
		
		String queryStr = 	" select ev " +
							" from Event as ev " +
							" join ev.studio as st " +
							" where date(ev.startTime) = date(:dateParam) " +
								" and st.nameForUrl= :studioURLName ";
		Query q = DBAccess.openSession().createQuery(queryStr);
		q.setParameter("dateParam", date);
		q.setParameter("studioURLName", studioNameUrl);
		
		@SuppressWarnings("unchecked")
		List<Event> events = (List<Event>) q.list();
		
		return events;
	}
	
}
