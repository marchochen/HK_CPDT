package com.cwn.wizbank.systemLog;

public enum SystemLogTypeEnum {
	
	    LOGIN_ACTION_LOG(1, "login_log"),
	   
	    OBJECT_ACTION_LOG(2, "object_action_log"),
	   
	    ;
	   
	   
	    private int value;
	   
	   
	    private String description;
	   
	   
	   
	    private SystemLogTypeEnum(int value, String description) {
	       this.value = value;
	       this.description = description;
	    }
	      
	    public int value() {
	       return value;
	    }
	    public String description() {
	       return description;
	    }
	   
	 
	    public static SystemLogTypeEnum valueOf(int value) {
	        for(SystemLogTypeEnum type : SystemLogTypeEnum.values()) {
	            if(type.value() == value) {
	                return type;
	            }
	        }
	        return null;
	    }
}
