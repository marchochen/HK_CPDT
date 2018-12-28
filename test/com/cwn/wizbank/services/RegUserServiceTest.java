package com.cwn.wizbank.services;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cw.wizbank.qdb.qdbException;
import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.vo.UserVo;
import com.cwn.wizbank.utils.Params;

public class RegUserServiceTest extends BaseTest{

	@Autowired
	RegUserService regUserService;
	
	@Test
	public void testGetUserList() throws Exception {
		Params params = new Params();
		params.getParams().put("last_update", "2015-01-12 14:16:59.577");
		params.getParams().put("eip_code", "demo");
		List<UserVo> userList;
		try {
			userList = regUserService.getList(params);
			Assert.assertTrue(userList != null);
			Assert.assertTrue(userList.size() > 0);
			for(UserVo user: userList) {
				System.out.println(user.toString());
			}
			System.out.println(userList.size());
			
		} catch (qdbException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
