package com.cwn.wizbank.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cwn.wizbank.base.BaseTest;

@Transactional(rollbackFor=Exception.class)
public class SnsValuationServiceTest extends BaseTest {

	@Autowired
	SnsValuationService snsValuationService;
	
	@Test
	public void testPraise() {
	}

}
