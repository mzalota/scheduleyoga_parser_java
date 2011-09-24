package com.scheduleyoga.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.scheduleyoga.dao.Studio;
import com.scheduleyoga.parser.Parser;

public class MainLoop {

	public void refreshAllStudios(boolean flag){
		
		if (flag) {
			System.out.println("Hello Spring 3.0");
		} else {
		
			List <Studio> studios = new ArrayList<Studio>(3); 
			studios.add(Studio.createFromNameURL("babtiste"));
			studios.add(Studio.createFromNameURL("kaia-yoga"));
			studios.add(Studio.createFromNameURL("abhayayoga"));
			
			for(Studio studio : studios){
				Parser parser;			
				parser = Parser.createNew(Parser.STUDIO_JOSCHI_NYC);		
				try {
					String htmlTable = parser.parseStudioSite(studio);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
