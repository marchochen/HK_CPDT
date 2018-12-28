package com.cwn.wizbank.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.persistence.AcFunctionMapper;
import com.cwn.wizbank.utils.CommonLog;

/**
 * service 实现
 */
@Service
public class AcFunctionService extends BaseService<AcFunction> {

	@Autowired
	AcFunctionMapper acFunctionMapper;

	public int initFunction(String filePath) throws BiffException, IOException {

		AcFunction fun = null;

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
			fun = new AcFunction();
			for (int columnIndex = 1; columnIndex <= row.getLastCellNum() - 1; columnIndex++) {
				cell = row.getCell(columnIndex);
				if (cell != null) {
					//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					//System.out.println(cell.getStringCellValue());
					if (columnIndex == 1) {
						String ftnExtId = cell.getStringCellValue();
						if(StringUtils.isEmpty(ftnExtId)) {
							break outside;
						} else {
							ftnExtId = ftnExtId.toUpperCase();
						}
						fun.setFtn_ext_id(ftnExtId);
					} else if (columnIndex == 2) {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						fun.setFtn_level(cell.getStringCellValue());
					} else if (columnIndex == 3) {
						fun.setFtn_tc_related(cell.getStringCellValue());
					} else if(columnIndex == 4) {
						fun.setFtn_status(new Double(cell.getNumericCellValue()).intValue()+"");
					} else if(columnIndex == 5) {
						fun.setFtn_assign(new Double(cell.getNumericCellValue()).intValue()+"");
					} else if(columnIndex == 6) {
						String funExtId = cell.getStringCellValue();
						if(!StringUtils.isEmpty(funExtId)){
							funExtId = funExtId.toUpperCase();
							AcFunction function = acFunctionMapper.getByExtId(funExtId);
							fun.setFtn_parent_id(function.getFtn_id());
						}
					} else if(columnIndex == 7) { 
						fun.setFtn_order(new Double(cell.getNumericCellValue()).intValue());
					}
				}
			}
			fun.setFtn_creation_timestamp(getDate());
			acFunctionMapper.insert(fun);
			++count;
			CommonLog.debug(String.valueOf(count));
		}
		return count;
	}
	
	
	public int getFunctionLabel(String filePath) throws BiffException,
			IOException {

		InputStream is = new FileInputStream(new File(filePath));
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFCell cell = null;
		XSSFSheet st = wb.getSheetAt(0);
		int count = 0;
		outside: for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {
			XSSFRow row = st.getRow(rowIndex);
			if (row == null) {
				break;
			}
			String ftnExtId = "";
			String ftnName = "";
			for (int columnIndex = 0; columnIndex <= 1; columnIndex++) {
				cell = row.getCell(columnIndex);
				if (cell != null) {
					if (columnIndex == 0) {
						ftnName = cell.getStringCellValue();
						if (StringUtils.isEmpty(ftnName)) {
							break outside;
						} else {
							ftnName = ftnName.toUpperCase();
						}
					}
					if (columnIndex == 1) {
						ftnExtId = cell.getStringCellValue();
						if (StringUtils.isEmpty(ftnExtId)) {
							break outside;
						} else {
							ftnExtId = ftnExtId.toUpperCase();
						}
					}
				}
			}
			++count;
			CommonLog.info("FUN_" + ftnExtId + " : \'" + ftnName + "\',");
		}
		return count;
	}


	public AcFunctionMapper getAcFunctionMapper() {
		return acFunctionMapper;
	}
	
	
}