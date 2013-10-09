/**
 * 
 */
package com.scheduleyoga.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
//import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.scheduleyoga.parser.Helper;

/**
 * @author mzalota
 * 
 */

@Entity
@Table(name = "instructors")
public class Instructor implements Comparable<Instructor> {

	private static final Logger logger = Logger.getLogger(Instructor.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected int id;

	@Column(name = "instructor_name", length = 150)
	protected String name;

	@Column(name = "name_url", unique = true, length = 150)
	protected String nameForUrl;
	
	@Column(name = "aliases",  length = 1000)
	protected String aliases;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	protected Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_on")
	protected Date modifiedOn;
	
//    @ManyToMany(
//        targetEntity=Studio.class,
//        cascade={CascadeType.PERSIST, CascadeType.MERGE}
//    )
//    @JoinTable(
//        name="studios_instructors",
//        joinColumns=@JoinColumn(name="instructor_id"),
//        inverseJoinColumns=@JoinColumn(name="studio_id")
//    )	
//	private Set<Studio> studios = new HashSet<Studio>(0);

//    public void linkToStudio(Studio studio){
//    	if (studio != null) {
//    		studios.add(studio);
//    	}
//    }
    
	public static Instructor createInstructor(String name, String aliases) {
		
		Instructor newObj = new Instructor();
		newObj.name = name;
		
		//TODO: Check that chosen name does not have duplicates 
		newObj.nameForUrl = Helper.createNew().nameToURLName(name);
		newObj.aliases = aliases;
		
		return newObj;
	}

	static public Instructor createFromNameURL(String nameURL) {
		Query q = DBAccess.openSession().createQuery(
				"from Instructor where nameForUrl=:nameUrl");
		q.setParameter("nameUrl", nameURL);

		List<Instructor> results = (List<Instructor>) q.list();
		if (results.size() <= 0) {
			return null;
		}

		return results.get(0);
	}
	
	static public Instructor fetchInstructorByName(String instructorName, Studio studio){
		if (StringUtils.isEmpty(instructorName)){			
			return null;
		}
		
		instructorName = instructorName.trim();
		
		if (!instructorName.contains(" ")){
			//Instructor name contains only one name. This is not unique enough
			//we cannot fetch instructor based on that.
			//TODO: check studio to see if this instructor was already created 
			return null;
		}
		
		//TODO: rewrite this using regex.
		if (instructorName.substring(instructorName.length()-2, instructorName.length()-1).equals(" ")){
			//the character before last is a space. It may mean that the instructor's last name is abbrivated
			if (StringUtils.countMatches(instructorName, " ") == 1){
				//this string contains only one space, so it ends with a single letter -- its an abbriviation.
				return null;
			}			
		}		
		
		//instructorName = instructorName.replace("-"," ");	//If first or last name includes hyphens, replace with spaces		
		
		
		String urlName = Helper.createNew().nameToURLName(instructorName);
		
//		Query q = DBAccess.openSession().createQuery(
//				"from Instructor where instructor_name=:name");
//		q.setParameter("name", instructorName);

		
		Query q = DBAccess.openSession().createQuery(
			"from Instructor where name_url=:name");
		q.setParameter("name", urlName);
		
		@SuppressWarnings("unchecked")
		List<Instructor> results = (List<Instructor>) q.list();
		if (results.size() <= 0) {			
			return Instructor.createInstructor(instructorName, null);
		}
		
		if (results.size() > 1){
			//TODO: need to use Studio parameter to select correct instructor from duplicates
			throw new RuntimeException("Found more then one rows in instructor table for name "+instructorName+", name_url "+urlName+". Need to implement duplicate check");
		}
		
		return results.get(0); 
	}

	static public Instructor createFromID(int id) {
		return (Instructor) DBAccess.openSession().get(Instructor.class, new Long(id));
	}
	
	static public List<Instructor> getAllInstructors() {
		Query q = DBAccess.openSession().createQuery(
				"from Instructor");

		return (List<Instructor>) q.list();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameForUrl() {
		return nameForUrl;
	}

	public void setNameForUrl(String nameForUrl) {
		this.nameForUrl = nameForUrl;
	}

	public int getId() {
		return id;
	}

	public Date getCreatedOn() {
		return (this.createdOn == null) ? new Date() : this.createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	@Override
	public String toString() {
		return "Instructor [id=" + id + ", name=" + name + ", nameForUrl="
				+ nameForUrl + ", aliases=" + aliases + ", createdOn="
				+ createdOn + ", modifiedOn=" + modifiedOn + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Instructor)) {
			return false;
		}
		Instructor other = (Instructor) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(Instructor p) {
		return this.getName().compareTo(p.getName());
	}
}
