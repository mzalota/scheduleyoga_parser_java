package com.scheduleyoga.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.scheduleyoga.dao.Studio;
import com.scheduleyoga.parser.Parser;

public class MainLoop {

	private static final int NTHREADS = 6;
	private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
	
	public void refreshAllStudios(boolean flag){
		
		if (flag) {
			System.out.println("Hello Spring 3.0");
		} else {
		
//			List <Studio> studios = new ArrayList<Studio>(3); 
//			studios.add(Studio.createFromNameURL("babtiste"));
//			studios.add(Studio.createFromNameURL("kaia-yoga"));
//			studios.add(Studio.createFromNameURL("abhayayoga"));
			
			List <Studio> studios = Studio.getAllStudios();
			
//			int i = 1;
			for(final Studio studio : studios){
//				if (studio.getNameForUrl().equals("om-yoga")){
//					//TODO: for now do not refresh Om Yoga. Only refresh MindbodyONly studios
//					continue;
//				}
//				if (i>2){
//					break;
//				}
//				i++;
					
				final String studioName = studio.getName();
				System.out.println("In The loop reading studio: "+studioName);
				Runnable task = new Runnable(){
					public void run() {
						Parser parser;			
						parser = Parser.createNew(Parser.STUDIO_JOSCHI_NYC);		
						try {
							System.out.println("Starting To Process Studio: "+studioName);
							parser.parseStudioSite(studio);
							System.out.println("Finished Processing Studio: "+studioName);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				exec.execute(task);
				
				System.out.println("At the end of the loop - studio: "+studioName);
			}
		}
	}
}
