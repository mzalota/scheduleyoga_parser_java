/**
 * 
 */
package com.scheduleyoga.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.scheduleyoga.common.LookUp;


/**
 * @author mzalota
 * 
 */

@Entity
@Table(name = "studios")
public class Studio {

	static public final int STUDIO_FRESH_YOGA = 104;
	static public final int STUDIO_FRESH_YOGA_ERECTOR_SQUARE = 105;
	static public final int STUDIO_BALANCED_YOGA = 107;
	
	private static final Logger logger = Logger.getLogger(Studio.class);
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected int id;

	@Column(name = "name", length = 100)
	protected String name;
	
	@Column(name = "state", length = 100)
	protected String state;

	@Column(name = "name_url", unique = true, length = 100)
	protected String nameForUrl;

	@Column(name = "url_home", length = 255)
	protected String urlHome;

	@Column(name = "url_schedule", length = 1024)
	protected String urlSchedule;

	@Column(name = "xpath", length = 1024)
	protected String xpath;	

	@Column(name = "mindbodyonline_id", length = 10)
	protected String mindbodyId;		
	
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
		logger.debug("Finished DELETITNG");
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

	public String getMindbodyId() {
		return mindbodyId;
	}
	
	public Date getCreatedOn() {
		return (this.createdOn == null) ? new Date() : this.createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	
	static public List<Studio> getAllStudios() {
		Query q = DBAccess.openSession().createQuery(
				"from Studio");

		List<Studio> results = (List<Studio>) q.list();		
		return results;
	}
	
	@Override
	public String toString() {
		return "Studio [id=" + id + ", name=" + name + ", nameForUrl="
				+ nameForUrl + ", urlHome=" + urlHome + ", urlSchedule="
				+ urlSchedule + ", xpath=" + xpath + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + "]";
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
		if (!(obj instanceof Studio)) {
			return false;
		}
		Studio other = (Studio) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}


	/**
	 * @param state the state to set
	 */
	public void setStateForUrl(String state) {
		this.state = state;
	}
	
	public String getStateForUrl() {
		return state;
	}
	
	public String getState() {
		String stateName = getStateForUrl();
		stateName = stateName.replace("-", " ");
		stateName = WordUtils.capitalizeFully(stateName);
		return stateName;
	}
}
