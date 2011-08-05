package com.scheduleyoga.parser;

import java.util.HashMap;
import java.util.Map;

public class Studio {

	static public final int STUDIO_ID_BABTISTE = 1;  
	static public final int STUDIO_ID_KAIAYOGA = 2;  
	static public final int STUDIO_ID_JOSCHI_NYC = 3;  
	static public final int STUDIO_ID_JIVAMUKTIYOGA = 4;  
	static public final int STUDIO_ID_LAUGHING_LOTUS = 5;
	static public final int STUDIO_ID_OM_YOGA = 6;
	
	protected Map<String, String> xPathMap = new HashMap<String, String>();
	protected Map<String, String> scheduleURL = new HashMap<String, String>();
	
	protected int id;
	protected String name; 
	protected String url;
	protected String xPath;
	
	protected Studio() {
		// TODO Auto-generated constructor stub
	}
	
	public static Studio createNew(int idParam){
		Studio newObj = new Studio();
		newObj.setId(idParam);
		return newObj;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}
}
