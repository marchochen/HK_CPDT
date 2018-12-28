package com.cwn.wizbank.services;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.entity.AcRoleFunction;
import com.cwn.wizbank.persistence.AcFunctionMapper;
import com.cwn.wizbank.persistence.AcRoleFunctionMapper;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.utils.CommonLog;
/**
 *  service 实现
 */
@Service
public class AcRoleFunctionService extends BaseService<AcRoleFunction> {


	@Autowired
	AcRoleFunctionMapper acRoleFunctionMapper;
	
	@Autowired
	AcFunctionMapper acFunctionMapper;
	
	@Autowired
	AclService aclService;
	
	
	public int initRoleFunction(String filePath) throws Exception{
		AcRoleFunction fun = null;

		InputStream is = new FileInputStream(new File(filePath));
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFCell cell = null;
		XSSFSheet st = wb.getSheetAt(0);
		int count = 0;
		outside : for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {
			XSSFRow row = st.getRow(rowIndex);
			if(row == null) {
				break;
			}
			fun = new AcRoleFunction();
			AcFunction function_p = null;
			for (int columnIndex = 1; columnIndex <= row.getLastCellNum() - 1; columnIndex++) {
				
				cell = row.getCell(columnIndex);
				if (cell != null) {
					//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					//System.out.println(cell.getStringCellValue());
					if (columnIndex == 1) {
						if(cell.getNumericCellValue()  < 1) {
							break;
						}
						fun.setRfn_rol_id(new Double(cell.getNumericCellValue()).intValue());
					} else if (columnIndex == 2) {
						String extId = cell.getStringCellValue();
						if(StringUtils.isEmpty(extId)){
							break outside;
						} else {
							extId = extId.toUpperCase();
						}
						CommonLog.debug("----------------------------   " + extId);
						AcFunction function = acFunctionMapper.getByExtId(extId);
						fun.setRfn_ftn_id(function.getFtn_id());
					} else if (columnIndex == 3) {
						String extId = cell.getStringCellValue();
						if(StringUtils.isEmpty(extId)){
							//break outside;
						} else {
							extId = extId.toUpperCase();
							CommonLog.debug("*******extId="+extId);
							function_p = acFunctionMapper.getByExtId(extId);
							CommonLog.debug(cell.getStringCellValue());
							fun.setRfn_ftn_parent_id(function_p.getFtn_id());
						}

					} else if(columnIndex == 4) {
						fun.setRfn_ftn_order(new Double(cell.getNumericCellValue()).intValue()+"");
					} else if(columnIndex == 5) {
						fun.setRfn_ftn_favorite(new Double(cell.getNumericCellValue()).intValue()+"");
					}
				}
			}
			if(fun.getRfn_rol_id() == null || fun.getRfn_ftn_id() == null) continue;
			
			CommonLog.debug(String.valueOf(fun.getRfn_ftn_parent_id()));
			CommonLog.debug(String.valueOf(fun.getRfn_rol_id()));
			CommonLog.debug(String.valueOf(fun.getRfn_ftn_parent_id()));
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rol_id", fun.getRfn_rol_id());
			map.put("ftn_id", fun.getRfn_ftn_parent_id());
			
			
			
			if(fun.getRfn_ftn_parent_id() != null && fun.getRfn_ftn_parent_id() > 0 && !acRoleFunctionMapper.hasRolFtn(map)){
				AcRoleFunction p_fun = new AcRoleFunction();
				p_fun.setRfn_create_timestamp(getDate());
				p_fun.setRfn_create_usr_id(fun.getRfn_create_usr_id());
				//p_fun.setRfn_ftn_favorite(fun.get);
				p_fun.setRfn_ftn_id(fun.getRfn_ftn_parent_id());
				p_fun.setRfn_ftn_parent_id( function_p.getFtn_parent_id());
				p_fun.setRfn_rol_id(fun.getRfn_rol_id());
				acRoleFunctionMapper.insert(p_fun);
				
			}
			fun.setRfn_create_timestamp(getDate());
			acRoleFunctionMapper.insert(fun);
			++count;
		}
		return count;
	}

	/**
	 * 按角色获取菜单
	 * @param current_role
	 * @return
	 */
	public List<AcFunction> getFunctions(String current_role) {
		return acRoleFunctionMapper.getFunctions(current_role);
	}

	public AcRoleFunctionMapper getAcRoleFunctionMapper() {
		return acRoleFunctionMapper;
	}

	public List<AcFunction> getMenusMarkFavorite(loginProfile prof) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("current_role", prof.current_role);
		map.put("usr_ent_id", prof.usr_ent_id);
		return acRoleFunctionMapper.getMenusMarkFavorite(map);
	}

	public List<AcFunction> roleFavoriteFunctionJson(String current_role,long role_id) {
		List<AcFunction> list = acRoleFunctionMapper.roleFavoriteFunctionJson(current_role);
		for(AcFunction ac : list){
			for(AcFunction af : ac.getSubFunctions()){
				/**根据二级菜单ftn_id获取三级菜单*/
				List<AcFunction> Functions = acRoleFunctionMapper.getRoleFunctions(af.getFtn_id());
				
				af.setSubFunctions(Functions);
			}
		}
		return list;
	}
	
	/**查询当前角色所有的权限功能
	 * @param role_id 当前角色ID
	 * @return
	 * */
	public List<AcFunction> roleHasFavoriteFunction(long role_id) {
		List<AcFunction> list = acRoleFunctionMapper.getRoleHasFunctions(role_id);
		return list;
	}
}