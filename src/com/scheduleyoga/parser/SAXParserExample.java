package com.scheduleyoga.parser;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserExample extends DefaultHandler{

	private String value;
	private int treeLevel;
	//private Stack<String> uriStack;
	
	public SAXParserExample(){
		treeLevel = 0;
		value = StringUtils.EMPTY;
		//uriStack = new Stack<String>();
	}

	public String parseDocument(String xmlStr) {
		
		if(StringUtils.isBlank(xmlStr)){
			return StringUtils.EMPTY;
		}
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			InputStream is = new ByteArrayInputStream(xmlStr.getBytes("US-ASCII"));
			sp.parse(is, this);
			
			return StringUtils.trim(value); 
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String tempVal = new String(ch,start,length);
		if (!StringUtils.isBlank(tempVal)){
			//System.out.println("TreeLevel: "+treeLevel+", Value of Element: "+tempVal);
			if (treeLevel == 1){
				//this text is inside an item on the top level in XML. record it 
				value += StringUtils.trim(tempVal)+" ";
				
				//System.out.println("Value in SAX is : "+value);
			}
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		treeLevel++;
		//System.out.println("Staring Element: "+qName+" TreeLevel: "+treeLevel);
		
		//uriStack.push(uri);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		treeLevel--;
		//System.out.println("Ending Element: "+qName+" TreeLevel: "+treeLevel);
		//String oldUri = uriStack.pop();
		//System.out.println("Popped URI: "+oldUri +" Should have been: "+uri);
	}
}
