package com.scheduleyoga.parser;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

public interface ISegment {

	public abstract HtmlElement getElement();

	public abstract void setElement(HtmlElement element);

	public abstract List extractEvents();

	public abstract Event eventFromRow(HtmlElement elementParam, int columnNum);

	public abstract boolean isBlank();

	public abstract String asText();

	public abstract String toString();

}