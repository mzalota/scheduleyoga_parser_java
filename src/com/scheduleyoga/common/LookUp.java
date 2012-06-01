package com.scheduleyoga.common;

import java.util.HashMap;
import java.util.Map;

public class LookUp {
	
	private static LookUp instance = new LookUp();
	private static Map<String, String> stateURLs = new HashMap<String, String>();
	
	public static LookUp getInstance(){
		if (instance==null){
			instance = new LookUp();
		}
		
		if (stateURLs.size()<1){
			stateURLs.put("connecticut","ct");
			stateURLs.put("new-york","ny");
			stateURLs.put("massachusetts","ma");
		}
		
		return instance;
	}
	
	private LookUp(){
	}
	
	public final Map<String,String> getStatesMap (){
		return stateURLs;
	}
}
