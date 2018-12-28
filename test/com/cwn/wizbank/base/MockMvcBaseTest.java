/**
 * 
 */
package com.cwn.wizbank.base;


import javax.servlet.ServletConfig;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author linchers@gmail.com
 * 2014-4-18 3:39:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "www")
//@ContextConfiguration({"/applicationContext-test.xml", "/mvc-servlet-test.xml"})
@ContextHierarchy({
	@ContextConfiguration(name = "parent", locations = "/applicationContext-test.xml"),
    @ContextConfiguration(name = "child", locations = "/mvc-servlet-test.xml")
})
@Transactional
//@ActiveProfiles("test")
public class MockMvcBaseTest {
	
	@Autowired
	public WebApplicationContext wac;

	public MockMvc mockMvc;
	


}
