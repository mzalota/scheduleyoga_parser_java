/**
 * 
 */
package com.scheduleyoga.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * @author mzalota
 * 
 */

@Entity
@Table(name = "studios")
public class Studio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected int id;

	@Column(name = "name", length = 100)
	protected String name;

	@Column(name = "name_url", unique = true, length = 100)
	protected String nameForUrl;

	@Column(name = "url_home", length = 255)
	protected String urlHome;

	@Column(name = "url_schedule", length = 1024)
	protected String urlSchedule;

	@Column(name = "xpath", length = 1024)
	protected String xpath;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	protected Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_on")
	protected Date modifiedOn;

	static public Studio createFromNameURL(String nameURL) {
		Query q = DBAccess.openSession().createQuery(
				"from Studio where nameForUrl=:nameUrl");
		q.setParameter("nameUrl", nameURL);

		List<Studio> results = (List<Studio>) q.list();
		if (results.size() <= 0) {
			return null;
		}

		return results.get(0);
	}

	static public Studio createFromID(int id) {
		return (Studio) DBAccess.openSession().get(Studio.class, new Long(id));
	}

	public void deleteEvents() {
		String hqlDelete = "delete Event e where e.studio = :this";
		Session sess = DBAccess.openSession();
		sess.beginTransaction();
		Query q = sess.createQuery(hqlDelete);
		q.setParameter("this", this);
		q.executeUpdate();
		sess.getTransaction().commit();
		System.out.println("Finished DELETITNG");
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameForUrl() {
		return nameForUrl;
	}

	public String getUrlHome() {
		return urlHome;
	}

	public String getUrlSchedule() {
		return urlSchedule;
	}
	
	public String getXpath() {
		return xpath;
	}	

	public Date getCreatedOn() {
		return createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	@Override
	public String toString() {
		return "Studio [id=" + id + ", name=" + name + ", nameForUrl="
				+ nameForUrl + ", urlHome=" + urlHome + ", urlSchedule="
				+ urlSchedule + ", xpath=" + xpath + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + "]";
	}
}
