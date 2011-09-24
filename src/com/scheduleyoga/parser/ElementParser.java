package com.scheduleyoga.parser;

import java.lang.String;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ElementParser extends DefaultHandler{

	private String value;
	private String tagName = "";
	private int treeLevel;
	
	private List<String> values;
	
	public ElementParser(){
		treeLevel = 0;
		value = StringUtils.EMPTY;
		values = new ArrayList<String>();
	}

	/**
	 * @param xmlStr
	 * @return
	 */
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
			
			InputStream is = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));// ISO-8859-1 UTF-8 US-ASCII
			InputSource inSource = new InputSource(is);
			
//			StringReader strReader = new StringReader(xmlStr);
//			InputSource inSource = new InputSource(strReader);
//			inSource.setEncoding("UTF-8");
			
			//System.out.println("String to Parse: "+xmlStr);
			
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
		
		if (tagName.equalsIgnoreCase("script")){
			return;
		}
		
		String tempVal = new String(ch,start,length);
		
		tempVal = StringEscapeUtils.escapeHtml(tempVal);
		tempVal = tempVal.replaceAll("&nbsp;", " ");
		tempVal = StringEscapeUtils.unescapeHtml(tempVal);
		
		tempVal = StringUtils.trim(StringUtils.strip(tempVal));
		if (!StringUtils.isBlank(tempVal)){
			if (treeLevel == 1){
				//this text is inside an item on the top level in XML. record it 
				value += tempVal+" ";
			}
			values.add(tempVal);
		}
	}
	
	public List<String> getValues() {
		return values;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		tagName = qName;
		treeLevel++;
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		tagName = "";
		treeLevel--;
	}
}
