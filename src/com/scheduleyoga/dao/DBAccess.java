package com.scheduleyoga.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class DBAccess {
	
	static private SessionFactory sessionFactory = null;
	static private Session session = null;
	

//	public static Session openSession() {
//		
//		// .addResource("Item.hbm.xml")
//		
//		if (sessionFactory == null){
//			Configuration config = new AnnotationConfiguration().configure();
//			sessionFactory = config.buildSessionFactory();
//			session = sessionFactory.openSession();
//		}	
//		
//		if (!session.isConnected()){
//			session.reconnect();
//		}
//		
//		return session;
//	}
//	
//
//	public static void saveObject(Object obj){		
//		Session session = DBAccess.openSession();
//		Transaction tx = session.getTransaction();
//		try {
//			tx.begin();
//			session.saveOrUpdate(obj);
//			tx.commit();
//		} catch (RuntimeException e){
//			tx.rollback();
//			throw e;			
//		} finally {
//			//session.close();
//		}
//	}
	
// Use this code for running parser - it openes separate connections for all threads.
	// Use the code above for the website, because it uses the same session throught request processing.

	public static Session openSession() {
		
		// .addResource("Item.hbm.xml")
		
		if (sessionFactory == null){
			Configuration config = new AnnotationConfiguration().configure();
			sessionFactory = config.buildSessionFactory();
		}
		
		//return sessionFactory.getCurrentSession();
		
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
