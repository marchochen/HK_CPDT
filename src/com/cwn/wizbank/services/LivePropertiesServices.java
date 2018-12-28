package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获取liveProperties
 * @author mt-201
 *
 */
@Component
public class LivePropertiesServices {
	
	@Value("#{liveProperties['layout']}")
    private String layout;
	
	@Value("#{liveProperties['is_open']}")
    private String is_open;
	
	@Value("#{liveProperties['type']}")
    private String type;

	@Value("#{liveProperties['auto_record']}")
    private String auto_record;
	
	@Value("#{liveProperties['is_chat']}")
    private String is_chat;
	
	@Value("#{liveProperties['buffer']}")
    private String buffer;
	
	@Value("#{liveProperties['auth_type']}")
    private String auth_type;
	
	@Value("#{liveProperties['account']}")
    private String account;
	
	@Value("#{liveProperties['password']}")
    private String password;

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getIs_open() {
		return is_open;
	}

	public void setIs_open(String is_open) {
		this.is_open = is_open;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuto_record() {
		return auto_record;
	}

	public void setAuto_record(String auto_record) {
		this.auto_record = auto_record;
	}

	public String getIs_chat() {
		return is_chat;
	}

	public void setIs_chat(String is_chat) {
		this.is_chat = is_chat;
	}

	public String getBuffer() {
		return buffer;
	}

	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}

	public String getAuth_type() {
		return auth_type;
	}

	public void setAuth_type(String auth_type) {
		this.auth_type = auth_type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	
}
