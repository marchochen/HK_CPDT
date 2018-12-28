package com.cwn.wizbank.web;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WzbSitemeshListener implements ServletContextListener {

	private final static String DEFAULT_FILE_ENCODING = "UTF-8";

	public void contextInitialized(ServletContextEvent event) {
		Properties prop = System.getProperties();
		prop.put("file.encoding", DEFAULT_FILE_ENCODING);
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}