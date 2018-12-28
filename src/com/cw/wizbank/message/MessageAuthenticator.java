package com.cw.wizbank.message;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MessageAuthenticator extends Authenticator {
	String username=null;
	String password=null;
		
	public MessageAuthenticator(String user,String pass){
		username = user;
		password = pass;
	}
		
	protected  PasswordAuthentication getPasswordAuthentication(){ 
		return new PasswordAuthentication(username,password); 
	}
}
