package com.scheduleyoga.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.Query;

import com.scheduleyoga.dao.DBAccess;
import com.scheduleyoga.dao.Instructor;
import com.scheduleyoga.dao.Studio;
import com.scheduleyoga.dao.Style;


@Entity
@Table(name="events")
public class Event {

	protected long id;
	protected long studio_id;
	protected String comment;
	protected Date startTime;
	protected String instructorName;
	protected Studio studio = null;
	protected Instructor instructor = null;
	protected Date createdOn;
	protected Date modifiedOn;	
	protected Set<String> styleNames = new HashSet<String>();
	
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

	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="instructor_id")    
	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	
	@Column(name = "comments", length=100)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
	public String getClassName() {
		return comment.replaceAll("(\\(.*\\))", " ").trim();
	}
	
	//http://stackoverflow.com/questions/796347/map-a-list-of-strings-with-jpa-hibernate-annotations
	//http://stackoverflow.com/questions/1093153/hibernate-collectionofelements-eager-fetch-duplicates-elements
	//@CollectionOfElements(fetch = FetchType.EAGER)
//	@CollectionOfElements
//	@JoinTable(
//	        table=@Table(name="events_styles"),
//	        joinColumns = @JoinColumn(name="event_id") // References parent
//	)
//	@Column(name="name", nullable=false)
	
//	Read this to learn how to avoid deleting all records when inserting new one
//	http://stackoverflow.com/questions/3742897/hibernate-elementcollection-strange-delete-insert-behavior
	
	//http://docs.jboss.org/hibernate/annotations/3.5/reference/en/html_single/#entity-mapping-association
	//See section 2.2.5.3.3 for an example
	@ElementCollection//(targetClass="String")
	@CollectionTable(name="events_styles", joinColumns=@JoinColumn(name="event_id"))//, table="events_styles", referencedColumnName="id"
	@Column(name="name", length=100, nullable=false)
	public Set<String> getStyleNames() {
		return styleNames;
	}
	
	public void setStyleNames(Set<String> styleNames) {
		this.styleNames = styleNames;
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

//	public void setStartTimeStr(String startTimeStr) {
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
//		Date dt;
//		try {
//			dt = (Date)formatter.parse(startTimeStr);
//			this.startTime = dt;
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
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

	public static List<Event> findEventsForInstructorForDate(Instructor instructor, Date date){
		
		String queryStr = 	" select ev " +
							" from Event as ev " +							
							" where date(ev.startTime) = date(:dateParam) " +
								" and instructor = :instructor ";
		Query q = DBAccess.openSession().createQuery(queryStr);
		q.setParameter("dateParam", date);
		q.setParameter("instructor", instructor);
		
		@SuppressWarnings("unchecked")
		List<Event> events = (List<Event>) q.list();
		
		return events;
	}

	public static List<Event> findEventsForStyleForDate(Style style, Date date, String stateNameUrl){
		
		String queryStr = 	" SELECT ev " +
							" FROM Event AS ev "+
							" JOIN ev.styleNames AS st " +
							" JOIN ev.studio AS std " +
							" WHERE date(ev.startTime) = date(:dateParam) " +
							"    AND st in (:styleName) " +
							"    AND std.state = :stateName ";
		Query q = DBAccess.openSession().createQuery(queryStr);
		q.setParameter("dateParam", date);
		q.setParameter("styleName", style.getName());
		q.setParameter("stateName", stateNameUrl);
		
		@SuppressWarnings("unchecked")
		List<Event> events = (List<Event>) q.list();
		
		return events;
	}

	@Override
	public String toString() {
		return "Event  [id=" + id + ", studio_id=" + studio_id + ", comment="
				+ comment + ", startTime=" + startTime + ", instructorName="
				+ instructorName + ", studio=" + studio + ", createdOn=" + createdOn + ", modifiedOn="
				+ modifiedOn + ", instructor=" + instructor + "]";
	}
	
}
