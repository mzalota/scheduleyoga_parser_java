package com.scheduleyoga.parser;

public class Studio {

	protected int id;
	protected String name; 
	protected String url;
	protected String xPath;
	
	protected Studio() {
		// TODO Auto-generated constructor stub
	}
	
	public static Studio createNew(){
		Studio newObj = new Studio();
		
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
