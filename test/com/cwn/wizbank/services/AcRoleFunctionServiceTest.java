package com.cwn.wizbank.services;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.cwn.wizbank.base.BaseTest;
import com.cwn.wizbank.entity.AcFunction;

public class AcRoleFunctionServiceTest extends BaseTest {

	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	
	@Autowired
	AcFunctionService acFunctionService;

	@Test
	@Rollback(false)
	public void test() {
		try {
			//清除acRoleFunction表
			acRoleFunctionService.getAcRoleFunctionMapper().truncate();
			//清除acFunction表
			acFunctionService.getAcFunctionMapper().deleteAll();
			
			String dirPath = getFilePath();
			
			acFunctionService.initFunction(dirPath + "acfunction.xlsx");
			//学员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionLearnning.xlsx");
			//培训管理员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionTaAdmin.xlsx");
			//系统管理员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionSysAdmin.xlsx");
			//讲师
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionInstructor.xlsx");
			//考试监考员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionInvigilator.xlsx");

			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFilePath(){
		String path = this.getClass().getResource("").getPath();
		path = path.substring(0, path.lastIndexOf("/"));
		path = path.substring(0, path.lastIndexOf("/") + 1);
		path = "D:/dev/CORE/trunk/test/com/cwn/wizbank/resources/copy/";
		return path;
	}
	
	@Test
	public void testGetFunctions(){
		List<AcFunction> pfuns = acRoleFunctionService.getFunctions("TADM_1");
		Assert.assertNotNull(pfuns);
		Assert.assertTrue(pfuns.size() > 0);
		Assert.assertNotNull(pfuns.get(0).getSubFunctions());
		Assert.assertTrue(pfuns.get(0).getSubFunctions().size() > 0);
	}

}
