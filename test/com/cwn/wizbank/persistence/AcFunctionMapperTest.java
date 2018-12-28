package com.cwn.wizbank.persistence;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.AcFunction;

public class AcFunctionMapperTest extends BaseTest {

	@Autowired
	AcFunctionMapper acFunctionMapper;
	
	@Test
	public void test() {
		//List<AcFunction> list = acFunctionMapper.getAllFunctions(null);
		
		List<AcFunction> taList = acFunctionMapper.getAllFunctions("TADM_1");
		System.out.println(taList.size());
	}

}
