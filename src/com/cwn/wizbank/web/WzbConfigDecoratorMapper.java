package com.cwn.wizbank.web;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwn.wizbank.utils.CommonLog;
import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.ConfigDecoratorMapper;
import com.opensymphony.module.sitemesh.mapper.ConfigLoader;

public class WzbConfigDecoratorMapper extends ConfigDecoratorMapper {

	public static Logger logger = LoggerFactory.getLogger(WzbConfigDecoratorMapper.class);

	private ConfigLoader configLoader = null;

	public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
		if (logger.isDebugEnabled()) {
			logger.debug("init()...");
		}
		super.init(config, properties, parent);
		try {
			String fileName = properties.getProperty("config", "/WEB-INF/decorators.xml");
			configLoader = new ConfigLoader(fileName, config);
		} catch (Exception e) {
			throw new InstantiationException(e.toString());
		}
	}

	public Decorator getDecorator(HttpServletRequest request, Page page) {
		if (logger.isDebugEnabled()) {
			logger.debug("getDecorator()...");
			logger.debug("getPathInfo(): " + request.getPathInfo());
			logger.debug("getServletPath(): " + request.getServletPath());
			logger.debug("getRequestURI(): " + request.getRequestURI());
			logger.debug("getContextPath(): " + request.getContextPath());
		}
		String thisPath = request.getServletPath();
		String requestURI = request.getRequestURI();
		thisPath = requestURI.substring(request.getContextPath().length(), requestURI.length());
		logger.debug("ThisPath: " + thisPath);
		String name = null;
		try {
			if("excludes".equals(request.getAttribute("sitemesh_parameter"))){
				name = "simple";
			}else{
				name = configLoader.getMappedName(thisPath);
			}
		} catch (ServletException e) {
			CommonLog.error(e.getMessage(),e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Resolved decorator name: " + name);
		}
		Decorator result = getNamedDecorator(request, name);
		if (logger.isDebugEnabled()) {
			logger.debug("Decorator is null ? " + (result == null));
		}
		return result == null ? super.getDecorator(request, page) : result;
	}
}