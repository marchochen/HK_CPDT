package com.cw.wizbank.ae;

import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cw.wizbank.ae.aeLearningSoln.XMLparser;
import com.cwn.wizbank.utils.CommonLog;

public class SimpleXmlToTextParser extends DefaultHandler {
	
	private static String field_break = "\n\r";
	
	StringBuffer text;
	public Hashtable srhContentField;
	public boolean addToSrhContent;
	
	public StringBuffer getText() {
		return text;
	}

	public void setText(StringBuffer text) {
		this.text = text;
	}

	SimpleXmlToTextParser() {
		text = new StringBuffer(100);
	}

	public void characters(char buf[], int offset, int len) throws SAXException {
		String thisStr = new String(buf, offset, len);
		if (thisStr != null && addToSrhContent) {
			text.append(thisStr).append(field_break);
		}
	}

	public void endDocument() throws SAXException {
		super.endDocument();
	}

	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if (!name.equalsIgnoreCase("subfield_list") && !name.equalsIgnoreCase("subfield")) {
			if (srhContentField.get(name) != null && srhContentField.get(name).toString().equalsIgnoreCase("true")) {
				addToSrhContent = true;
			} else {
				addToSrhContent = false;
			}
		}
		super.startElement(uri, localName, name, attributes);
		if (addToSrhContent) {
			int att_cnt = attributes.getLength();
			for (int i = 0; i < att_cnt; i++) {
				if (!attributes.getQName(i).equalsIgnoreCase("id")) {
					text.append(attributes.getValue(i)).append(field_break);
				}
			}
		}
	}

	public String parseXml2Text(String xml, Hashtable srhContentFieldHt) {
		try {
            StringReader in = new StringReader(xml);
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            SAXParser saxParser  = saxFactory.newSAXParser();
            SimpleXmlToTextParser parser = new SimpleXmlToTextParser();
            parser.srhContentField = srhContentFieldHt;
            saxParser.parse(new InputSource(in), parser);
            in.close();
			return parser.getText().toString();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		}
		return null;
	}
}