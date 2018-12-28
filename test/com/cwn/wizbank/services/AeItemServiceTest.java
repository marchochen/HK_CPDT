package com.cwn.wizbank.services;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.WzbApplicationContext;

public class AeItemServiceTest extends BaseTest {

	@Autowired
	AeItemService aeItemService;
	
	@Test
	public void test() {
		aeItemService =	(AeItemService) WzbApplicationContext.getBean("aeItemService");
		System.out.println(aeItemService.get(1).getItm_title());
		
		//aeItemService.getDetial(null,14, 0, 0);
	}
	
	
	@Test
	public void testGetCatalogCourse(){
		Page<AeItem> page = new Page<AeItem>();
		//page.getParams().put("periods", "quarter");
		page.getParams().put("periods", "month");
		//page.getParams().put("periods", "week");
		aeItemService.getCatalogCourse(3,"", page);
		Assert.assertEquals(false, page.getResults().isEmpty());
	}
	
	@Test
	public void testLearningMap(){
		//List<AeItem> list = aeItemService.getLearningMap(9);
		//System.out.println(list.size());
	}
	
	@Test
	public void testPageAdmin(){
		Page<AeItem> page = new Page<AeItem>();
	
		aeItemService.pageAdmin(page, 3,"TADM_1");
	}
	

}
