package com.cwn.wizbank.services;

import java.util.List;



import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.vo.UserGroupVo;
import com.cwn.wizbank.utils.Params;

public class UserGroupServiceTest extends BaseTest {

	@Autowired
	UserGroupService userGroupService;
	
	@Test
	public void testGetList() {
		Params params = new Params();
		//params.getParams().put("last_update", "2015-01-12 14:16:59.577");
		//params.getParams().put("eip_code", "demo");
		
		List<UserGroupVo> list = userGroupService.getList(params);
		
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() > 0);
		
		for(UserGroupVo ug : list) {
			System.out.println(ug);
		}
		
	}

}
