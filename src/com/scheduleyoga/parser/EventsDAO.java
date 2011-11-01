package com.scheduleyoga.parser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;


import com.scheduleyoga.parser.Event;

//THIS CLASS IS NOT USED ANYWHERE. Delete it
public class EventsDAO {

	static public SessionFactory sessionFactory = null;
	
	@SuppressWarnings("deprecation")
	public void store(Event event){
		Configuration config = new AnnotationConfiguration().configure();
		// .addResource("Item.hbm.xml")
		
		if (sessionFactory == null){
			sessionFactory = config.buildSessionFactory();
		}
		Session session = sessionFactory.openSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			session.saveOrUpdate(event);
			tx.commit();
		} catch (RuntimeException e){
			tx.rollback();
			throw e;			
		} finally {
			session.close();
		}		
	}
}
