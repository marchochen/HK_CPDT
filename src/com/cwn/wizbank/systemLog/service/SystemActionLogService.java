package com.cwn.wizbank.systemLog.service;

import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.SpringContextUtil;

public abstract class SystemActionLogService<T> extends BaseService<T> {
	
	SystemActionLogService(){
		init();
	}
	
	/*
	 * 用于加载spring上下文
	 *	若用在旧代码时可以通过这个函数获取spring上下文的bean
	 */
	Object getSpringBean(String bean){
	  return SpringContextUtil.getBean(bean);
	}
	
	/*
	 * 用于类的初始化类的操作，如看看有没注入一些用到的类，如果没有在这里赋值
	 */
	abstract void init();

	//保存日志
	public abstract void saveLog(T t);
}
