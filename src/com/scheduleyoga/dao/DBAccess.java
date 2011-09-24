package com.scheduleyoga.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class DBAccess {
	
	static private SessionFactory sessionFactory = null;
	
	public static Session openSession() {
		Configuration config = new AnnotationConfiguration().configure();
		// .addResource("Item.hbm.xml")
		
		if (sessionFactory == null){
			sessionFactory = config.buildSessionFactory();
		}
		
		return sessionFactory.openSession();
	}
	
	public static void saveObject(Object obj){		
		Session session = DBAccess.openSession();
		Transaction tx = session.getTransaction();
		try {
			tx.begin();
			session.saveOrUpdate(obj);
			tx.commit();
		} catch (RuntimeException e){
			tx.rollback();
			throw e;			
		} finally {
			session.close();
		}
	}
	
}
