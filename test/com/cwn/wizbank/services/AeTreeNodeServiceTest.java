package com.cwn.wizbank.services;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;

public class AeTreeNodeServiceTest extends BaseTest {

	@Autowired
	AeTreeNodeService aeTreeNodeService;
	
	
	@Test
	public void testGetTraingCenterCatalog() {
		List<AeTreeNodeVo> list = aeTreeNodeService.getTraingCenterCatalog(1,1,3,"",null);
		Assert.assertTrue(list!=null&&list.size()>0);
	}

	@Test
	public void testGetAdminTraingCenterCatalog() {
		List<AeTreeNodeVo> list = aeTreeNodeService.getAdminTraingCenterCatalog(1,3,"",null);
		Assert.assertTrue(list!=null&&list.size()>0);
	}
	
}
