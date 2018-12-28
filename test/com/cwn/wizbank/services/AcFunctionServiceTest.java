package com.cwn.wizbank.services;

import java.io.IOException;

import jxl.read.biff.BiffException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;

public class AcFunctionServiceTest extends BaseTest {

	@Autowired
	AcFunctionService acFunctionService;
	
	public static final String fileLocation = "D:/project/wizbank6.1/test/com/cwn/wizbank/resources/copy/acfunction.xlsx";
	
	@Test
	public void test() {
		try {
			acFunctionService.initFunction(fileLocation);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetLabel() throws BiffException, IOException{
		acFunctionService.getFunctionLabel(fileLocation);
	}

}
