/**
 * 
 */
package com.cwn.wizbank.base;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author linchers@gmail.com
 * 2014-4-17 11:50:25
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = "classpath:applicationContext-test.xml"
)
@Transactional
@ActiveProfiles("test")
public class BaseTest {

}
