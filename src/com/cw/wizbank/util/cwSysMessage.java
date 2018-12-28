package com.cw.wizbank.util;

import java.util.Vector;

/**
A class to get system message depending on the selected language <BR>
*/
public class cwSysMessage extends Exception {
    
    public static final String GEN_REC_NOT_FOUND = "GEN005";
    
    /**
    Vector of values that will substitute to the system message
    */
    private Vector data = null;
    /**
    ID of the system message
    */
    private String id = null;
    
    private String error_code = null;
    /**
    Default language of the system message
    */
    private static final String DEFAULT_ENCODING         = "ISO-8859-1";
    /**
    Default ID of the  system message
    */
    private String DEFAULT_ID = "GEN000";
    /**
    Default data tag in the system message
    */
    private String DATA_TAG = "$data";
    
    /**
    Constructor : create a system message
    @param id_ ID of the system message
    */
    public cwSysMessage (String id_) { 
            super();
            id = new String(id_);
            data = null;
    }

	public cwSysMessage(String id_, String data_, String error_code_) {
		super(data_);
		id = new String(id_);
		if( data_!= null ) {
			data = new Vector();
			data.addElement(new String(data_));
		}
		error_code = error_code_;
		return;
	}

	public String getErrorCode(){
		return this.error_code;
	}

    /**
    Constructor : create a system message with one tag to be subsituted
    @param id_ ID of the system message
    @param data_ value to subsitute the tag
    */
    public cwSysMessage (String id_, String data_) {
		super(data_);
        id = new String(id_);
        if( data_ != null ) {
			data = new Vector();
			data.addElement(new String(data_));
        }
		return;
    }

    /**
    Constructor : create a system message with more than one tag to be subsituted
    @param id_  ID of the system message
    @param data_ values to subsitute the tags
    */
    public cwSysMessage (String id_, Vector data_) {
            super();
            id = new String(id_);
            data = data_;
    }
    
    public cwSysMessage(String id_, String[] data_){
    	super();
    	Vector vector_data = new Vector();
    	for (int i = 0; i < data_.length; i++) {
			vector_data.add(data_[i]);
		}
    	 id = new String(id_);
    	 data = vector_data;
    }

    /**
    Get the id of the message
    @return ID of the system message
    */
    public String getId() {
            return id;
    }
    
    /**
    Get the message with given language
    @param lan language of the system message
    */
    public String getSystemMessage(String lan)
    {
            if (lan == null)
                lan = DEFAULT_ENCODING;
                
            String mesg_temp = LangLabel.getValue(lan, id);
            StringBuffer message = new StringBuffer();
            String val = null;
            int index = mesg_temp.indexOf(DATA_TAG);
            
            if (index >=0) {
                int cnt = 0;
                do {
                    cnt ++ ;
                    
                    val = null;
                    if (data != null && cnt <= data.size()) {
                        val = (String) data.elementAt(cnt-1);
                    }
                    if (val == null) {
                        val = new String();
                    }
                    
                    message.append(mesg_temp.substring(0,index));
                    
                    message.append(val);
                    
                    mesg_temp = mesg_temp.substring(index+DATA_TAG.length()); 
                    
                    index = mesg_temp.indexOf(DATA_TAG);
                    
                }while (index > 0);
                // Add the last substring
                message.append(mesg_temp);
            }else {
                    message.append(mesg_temp);            
            }
            
            return message.toString(); 
    }
    
    
    public String getErrorMessageXML(String lan) {
    	StringBuffer xml = new StringBuffer(256);
		xml.append("<error_message>")
		   .append("<content>")
		   .append(getSystemMessage(lan))
		   .append("</content>");
		if( getErrorCode() != null ) {
			xml.append("<code>").append(getErrorCode()).append("</code>");
		}
		xml.append("</error_message>");
		return xml.toString();
    }
    
}
