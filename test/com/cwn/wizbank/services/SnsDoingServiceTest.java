package com.cwn.wizbank.services;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.services.SnsDoingService;

public class SnsDoingServiceTest extends BaseTest{

	private static final Logger logger = LoggerFactory.getLogger(SnsDoingServiceTest.class);
	
	@Autowired
	SnsDoingService snsDoingService;
	
	@Test
	public void test() {
		snsDoingService.getDate();
		logger.debug(snsDoingService.getDate()+"");
	}

}
