package com.cwn.wizbank.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class WzbApplicationContext implements ApplicationContextAware {

	private static ApplicationContext context = null;
	private static WzbApplicationContext stools = null;

	public synchronized static WzbApplicationContext init() {
		if (stools == null) {
			stools = new WzbApplicationContext();
		}
		return stools;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}

	public synchronized static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
}