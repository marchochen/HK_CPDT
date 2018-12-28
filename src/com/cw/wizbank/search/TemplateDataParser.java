package com.cw.wizbank.search;

import org.xml.sax.*;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.dbUtils;
 
public class TemplateDataParser extends HandlerBase {
    public String templateData = "";
    
    TemplateDataParser() {
    }
 
    public void endElement(String name){
    }
    
    public void startElement(String name, AttributeList inAttribList) throws SAXException {
    }
    
    public void characters(char buf[], int offset, int len) throws SAXException {
        String thisStr = new String(buf, offset, len);
        templateData += dbUtils.unEscXML(thisStr) + cwUtils.NEWL;
    }
}