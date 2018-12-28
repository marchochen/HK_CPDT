package com.cw.wizbank.JsonMod;

public class ErrorMsg {
	private String code;
	private String action;
	private String url;
	private String message;
	private String status;
	
	public ErrorMsg() {}
	
	public ErrorMsg(String status,String code,String action,String url, String message){
	    this.status = status;
	    this.code=code;
        this.action=action;
        this.url=url;
        this.message=message;
    }
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
