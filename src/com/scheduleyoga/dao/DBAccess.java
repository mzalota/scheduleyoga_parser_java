package com.scheduleyoga.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
}
