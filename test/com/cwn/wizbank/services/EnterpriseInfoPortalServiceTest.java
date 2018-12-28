package com.cwn.wizbank.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.EnterpriseInfoPortal;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.MessageException;

public class EnterpriseInfoPortalServiceTest extends BaseTest{

	@Autowired
	EnterpriseInfoPortalService enterpriseInfoPortalService;
	
	@Test
	public void testAddEip() {
		EnterpriseInfoPortal eip = new EnterpriseInfoPortal();
		eip.setEip_account_num(1000l);
		eip.setEip_code("LLL");
		eip.setEip_create_timestamp(enterpriseInfoPortalService.getDate());
		eip.setEip_create_usr_id(3 + "");
		eip.setEip_domain("http://lll.com.cn");
		eip.setEip_name("LLL科技");
		eip.setEip_status("ON");
		eip.setEip_update_timestamp(enterpriseInfoPortalService.getDate());
		eip.setEip_update_usr_id(3 + "");
		List<Long> usgIds =	null;
		Assert.assertNotNull(usgIds);
		Assert.assertTrue(usgIds.size() > 0);
		for(Long usgId : usgIds) {
			System.out.println("创建成功的用户组Id :    " + usgId);
		}
	}
	
	
	@Test
	public void testEidtEip() throws DataNotFoundException {
		
		EnterpriseInfoPortal eip = new EnterpriseInfoPortal();
		eip.setEip_account_num(1000l);
		eip.setEip_code("LLL");
		eip.setEip_create_timestamp(enterpriseInfoPortalService.getDate());
		eip.setEip_create_usr_id(3 + "");
		eip.setEip_domain("http://lll.com.cn");
		eip.setEip_name("LLL科技");
		eip.setEip_status("ON");
		eip.setEip_update_timestamp(enterpriseInfoPortalService.getDate());
		eip.setEip_update_usr_id(3 + "");
		List<Long> usgIds =	null;
		Assert.assertNotNull(usgIds);
		Assert.assertTrue(usgIds.size() > 0);
		for(Long usgId : usgIds) {
			System.out.println("创建成功的用户组Id :    " + usgId);
		}
		eip.setEip_name("LLL科技ddddd");
		enterpriseInfoPortalService.editEip(eip, 3);
		
		EnterpriseInfoPortal dbEip = enterpriseInfoPortalService.getEipMapper().getByCode(eip.getEip_code());
		Assert.assertNotNull(dbEip);
		Assert.assertTrue("LLL科技ddddd".equals(dbEip.getEip_name()));

	}

}
