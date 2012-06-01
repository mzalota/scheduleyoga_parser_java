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

	private final int numThreads;
	private int numStudios;
	
	private static final int NTHREADS = 1;
	private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
	private static final Logger logger = Logger.getLogger(MainLoop.class);
	
	public MainLoop(int numThreads){
		this.numThreads = numThreads;
		numStudios = 0;
	}
	
	public void refreshAllStudios(boolean flag) {

		// List <Studio> studios = new ArrayList<Studio>(3);
		// studios.add(Studio.createFromNameURL("babtiste"));
		// studios.add(Studio.createFromNameURL("kaia-yoga"));
		// studios.add(Studio.createFromNameURL("abhayayoga"));

		//https://clients.mindbodyonline.com/ASP/main_class.asp?tg=0&vt=&lvl=&view=&trn=&date=10/24/2011&loc=0&page=1&pMode=&prodid=&stype=7&classid=0&catid=&justloggedin=&nLgIn=%22%20name=%22mainFrame%22%3E
		
		//List<Studio> studios = Studio.getAllStudios();				
		
		List <Studio> studios = new ArrayList<Studio>(1);
		studios.add(Studio.createFromNameURL("fresh-yoga-9th-square"));		
		
		logger.info("MainLoop.numThreads is autowired to "+numThreads);
		logger.info("MainLoop.numStudios is autowired to "+numStudios);
		
		int studiosCount = 0;
		for (final Studio studio : studios) {
			if (numStudios>0 && studiosCount>numStudios){
				break;
			}
			studiosCount++;
			
			logger.info("In The loop reading studio: " + studio.getName());			
			
			launchStudioParser_Sync(studio);

			logger.info("At the end of the loop - studio: " + studio.getName()+", id="+studio.getId());
		}
	}

	/**
	 * @param studio
	 * @return
	 */
	protected void launchStudioParser_Sync(final Studio studio) {
		
		Parser parser;
		parser = Parser.createNew(Parser.STUDIO_JOSCHI_NYC);		
		try {
			logger.info("Starting To Process Studio: " + studio.getName()+", id="+studio.getId());
			parser.parseStudioSite(studio);
			logger.info("Finished Processing Studiooo: " + studio.getName()+", id="+studio.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * @param studio
	 * @return
	 */
	protected void launchStudioParser_Async(final Studio studio) {
		final String studioName = studio.getName();
		
		Runnable task = new Runnable() {
			public void run() {				
				Parser parser;
				parser = Parser.createNew(Parser.STUDIO_JOSCHI_NYC);
				try {
					logger.info("Starting To Process Studio: " + studioName+", id="+studio.getId());
					parser.parseStudioSite(studio);
					logger.info("Finished Processing Studiooo: " + studioName+", id="+studio.getId());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		exec.execute(task);
	}

	/**
	 * @return the exec
	 */
	public static Executor getExec() {
		return exec;
	}

	/**
	 * @param numStudios the numStudios to set
	 */
	public void setNumStudios(int numStudios) {
		this.numStudios = numStudios;
	}
}
