package com.cwn.wizbank.services;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.utils.Page;

public class AeApplicationServiceTest extends BaseTest {

	@Autowired
	AeApplicationService aeApplicationService;

	@Test
	public void testSignup() {
		Page<AeApplication> page = new Page<AeApplication>();

		aeApplicationService.signup(3, "zh_cn", page);

		Assert.assertEquals(false, page.getResults().isEmpty());
	}

}
