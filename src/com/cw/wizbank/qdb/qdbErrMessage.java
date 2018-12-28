package com.cw.wizbank.qdb;

import com.cw.wizbank.util.LangLabel;

public class qdbErrMessage extends Exception {
    private String data = null;
    private String id = null;
    
    private String DEFAULT_ID = "GEN000";
    private String DATA_TAG = "$data";
    
    public qdbErrMessage (String id_) { 
            super();
            id = new String(id_);
            data = null;
    }

    public qdbErrMessage (String id_, String data_) {
            super();
            id = new String(id_);
            data = new String(data_);
    }

    public qdbErrMessage() {super();}

    public String getData()
    {
        return data;
    }

    public String getId()
    {
        return id;
    }
    
    public String getSystemMessage(String lan)
    {
            String mesg_temp = LangLabel.getValue(lan, id);
            String message = new String();
            int index = mesg_temp.indexOf(DATA_TAG);
            if (index >=0) {
                    message = mesg_temp.substring(0,index);
                    if (data !=null)
                        message += data;
                    message += mesg_temp.substring(index+DATA_TAG.length()); 
            }else {
                    message = mesg_temp;            
            }
            
            return message; 
            
    }
}
