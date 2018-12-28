package com.cwn.wizbank.exception;

import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.util.LangLabel;
import com.cwn.wizbank.utils.LabelContent;

public class cwMessageException extends Exception {
    
	private static final long serialVersionUID = 1L;
   
	private List<String> id_list = new ArrayList<String>();
	
    public cwMessageException (String id_) { 
            super();
            id_list.add(id_);
    }
    
    public cwMessageException (List<String> idList) { 
    	  super();
    	  id_list = idList;
    }

    public List<String> getId() {
            return id_list;
    }
    
    /**
    Get the message with given language
    @param lan language of the system message
    */
    public String getSystemMessage(String lan,String curLang) {
    	  	StringBuffer message = new StringBuffer();
    	  	String val = null;
    	  	for (int i = 0; i < id_list.size(); i++) {
            	val = LabelContent.get(curLang, id_list.get(i));
            	if(val.indexOf("!!!"+curLang) != -1){
            		val = LangLabel.getValue(lan, id_list.get(i));
            	}
            	message.append(val);
            	if(id_list.size() > i+1){
            		message.append("<br/>");
            	}
			}
            return message.toString(); 
    }
    
}
