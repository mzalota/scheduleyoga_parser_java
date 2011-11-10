package com.scheduleyoga.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

//import org.apache.commons.logging.impl.Log4JLogger org.apache.log4j.Logger;

import com.scheduleyoga.dao.DBAccess;
import com.scheduleyoga.dao.Instructor;
import com.scheduleyoga.dao.Studio;
import com.scheduleyoga.parser.Parser;

public class MainLoop {

	private static final int NTHREADS = 6;
	private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
	private static final Logger logger = Logger.getLogger(MainLoop.class);
	
	public void refreshAllStudios(boolean flag) {

		// List <Studio> studios = new ArrayList<Studio>(3);
		// studios.add(Studio.createFromNameURL("babtiste"));
		// studios.add(Studio.createFromNameURL("kaia-yoga"));
		// studios.add(Studio.createFromNameURL("abhayayoga"));

		//https://clients.mindbodyonline.com/ASP/main_class.asp?tg=0&vt=&lvl=&view=&trn=&date=10/24/2011&loc=0&page=1&pMode=&prodid=&stype=7&classid=0&catid=&justloggedin=&nLgIn=%22%20name=%22mainFrame%22%3E
		
		List<Studio> studios = Studio.getAllStudios();
		
//		Instructor instructor= Instructor.createInstructor("Max Zalota", "max-zalota", null);
//		instructor.linkToStudio(studios.get(0));
//		DBAccess.saveObject(instructor);
//		if (flag){
//			logger.info("Just created Instructor");
//			return;
//		}				
		
//		int i = 1;
		for (final Studio studio : studios) {
//			 if (i>1){
//				 break;
//			 }
//			 i++;

			final String studioName = studio.getName();
			logger.info("In The loop reading studio: " + studioName);
			Runnable task = new Runnable() {
				public void run() {
					Parser parser;
					parser = Parser.createNew(Parser.STUDIO_JOSCHI_NYC);
					try {
						logger.error("Starting To Process Studio: " + studioName);
						parser.parseStudioSite(studio);
						logger.error("Finished Processing Studiooo: " + studioName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			exec.execute(task);

			logger.info("At the end of the loop - studio: " + studioName);
		}
	}
}
