package com.cwn.wizbank.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.ItemTargetLrnDetail;
import com.cwn.wizbank.utils.Page;

public class ItemTargetLrnDetailServiceTest  extends BaseTest {

	@Autowired
	ItemTargetLrnDetailService itemTargetLrnDetailService;
	
	
	@Test
	public void test() {
		Page<ItemTargetLrnDetail> page = new Page<ItemTargetLrnDetail>();
		page = itemTargetLrnDetailService.recommend(3,"zh_cn", page);
		
		for(ItemTargetLrnDetail itd : page.getResults()){
			if(itd.getApp() != null) {
				System.out.println(" >>>>>>>>>>>>>>>>>>>: "+ itd.getApp().getApp_status());
			}
		}
	}

}
