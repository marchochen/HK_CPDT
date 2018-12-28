package com.cwn.wizbank.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cwn.wizbank.base.MockMvcBaseTest;


public class DoingControllerTest extends MockMvcBaseTest{

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testAllList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.patch("/doing/list"))
		.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
	}

}
