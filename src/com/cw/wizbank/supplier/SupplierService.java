package com.cw.wizbank.supplier;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import Word.Border;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supplier.dao.SupplierDao;
import com.cw.wizbank.supplier.entity.Supplier;
import com.cw.wizbank.supplier.entity.SupplierCooperationExperience;
import com.cw.wizbank.supplier.entity.SupplierMainCourse;
import com.cw.wizbank.supplier.utils.XMLHelper;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
/**
 * 
 * @author Leon.li 2013-5-24 4:41:20 
 * message: Supplier service 
 */
public class SupplierService {


	public static final String HAVE_COOPERATION = "a_have"; 
	public static final String BID_PASS = "b_pass"; 
	public static final String HAD_MEETING = "c_meet"; 
	public static final String FIRST_CONTACT = "d_contact"; 
	public static final String EXITED = "e_exited"; 
	public static final String DEL = "f_deleted"; 

	public static final String SPL_NAME = "996";
	public static final String SPL_STATUS = "997";
	public static final String SPL_REPRESENTATIVE= "1006";
	public static final String SPL_ESTABLISHEDDATE = "1007";
	public static final String SPL_REGISTEREDCAPITAL = "1008";
	public static final String SPL_TYPE = "1009";
	public static final String SPL_ADDRESS = "1010";
	public static final String SPL_CONTACT = "1011";
	public static final String SPL_TEL = "1012";
	public static final String SPL_MOBILE = "1013";
	public static final String SPL_EMAIL = "1014";
	public static final String SPL_TOTAL_STAFF = "1015";
	public static final String SPL_FULL_TIME_INST = "1016";
	public static final String SPL_PART_TIME_INST = "1017";
	public static final String SPL_EXPERTISE = "1018";
	public static final String SPL_COURSE = "1019";
	
	public static final String SHEET_NAME = "1035";
	public static final String SHEET2_NAME = "1036";
	
	public static final String SCE_START_DATE = "1025";
	public static final String SCE_END_DATE = "1026";
	public static final String SCE_ITM_NAME = "1027";
	public static final String SCE_DESC = "1028";
	public static final String SCE_DPT = "1029";


	//public static final String SCM_SCORE = "979";
	
	public static final int XLS_COULMN_WIDTH = 20;	
	
	
	SupplierDao spDao = new SupplierDao();

	/**
	 * Put the supplier list into XML
	 * @param con  
	 * @param cwPage
	 * @param isLrn
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String getAllSupplier2Xml(Connection con, SupplierModuleParam modParam,
			boolean isLrn) throws SQLException, IllegalArgumentException, IllegalAccessException {

		StringBuffer result = new StringBuffer();
		Supplier sp = null;
		List<Supplier> splist = spDao.getAllSupplierList(con, modParam ,DEL);

		result.append("<suppliers>");
		for (int i = 0; i < splist.size(); i++) {
			sp = splist.get(i);
			result.append(XMLHelper.javaBeanToXML(sp));
		}
		result.append("</suppliers>");
		result.append(modParam.getCwPage().asXML());
		
		//Record user search
		result.append("<select_params p_spl_name = \"")
		.append(modParam.getSplName()==null?"":cwUtils.esc4XML(modParam.getSplName())).append("\" ")
		.append(" p_spl_course = \"")
		.append(modParam.getSplCourse()==null?"":cwUtils.esc4XML(modParam.getSplCourse())).append("\" ")
		.append(" p_sce_itm_name = \"")
		.append(modParam.getSceItmName()==null?"":cwUtils.esc4XML(modParam.getSceItmName())).append("\" ")
		.append("/>");
		return result.toString();
	}
	
	
	/**
	 * Put the supplier into XML
	 * @param con
	 * @param modParam
	 * @return
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String getSupplier2ListXml(Connection con,
			SupplierModuleParam modParam,String saveDirPath) throws SQLException, IllegalArgumentException, IllegalAccessException {
		StringBuffer result = new StringBuffer();
		Supplier sp = spDao.getSupplierViewByID(con, modParam.getSplId(),DEL);
		if(sp!=null){
			if(sp.getSplAttachment1()!=null&&!"".equals(sp.getSplAttachment1()))
			sp.setSplAttachment1(saveDirPath+"/"+sp.getSplAttachment1());
			if(sp.getSplAttachment2()!=null&&!"".equals(sp.getSplAttachment2()))
			sp.setSplAttachment2(saveDirPath+"/"+sp.getSplAttachment2());
			if(sp.getSplAttachment3()!=null&&!"".equals(sp.getSplAttachment3()))
			sp.setSplAttachment3(saveDirPath+"/"+sp.getSplAttachment3());
			if(sp.getSplAttachment4()!=null&&!"".equals(sp.getSplAttachment4()))
			sp.setSplAttachment4(saveDirPath+"/"+sp.getSplAttachment4());
			if(sp.getSplAttachment5()!=null&&!"".equals(sp.getSplAttachment5()))
			sp.setSplAttachment5(saveDirPath+"/"+sp.getSplAttachment5());
			if(sp.getSplAttachment6()!=null&&!"".equals(sp.getSplAttachment6()))
			sp.setSplAttachment6(saveDirPath+"/"+sp.getSplAttachment6());
			if(sp.getSplAttachment7()!=null&&!"".equals(sp.getSplAttachment7()))
			sp.setSplAttachment7(saveDirPath+"/"+sp.getSplAttachment7());
			if(sp.getSplAttachment8()!=null&&!"".equals(sp.getSplAttachment8()))
			sp.setSplAttachment8(saveDirPath+"/"+sp.getSplAttachment8());
			if(sp.getSplAttachment9()!=null&&!"".equals(sp.getSplAttachment9()))
			sp.setSplAttachment9(saveDirPath+"/"+sp.getSplAttachment9());
			if(sp.getSplAttachment10()!=null&&!"".equals(sp.getSplAttachment10()))
			sp.setSplAttachment10(saveDirPath+"/"+sp.getSplAttachment10());
		}
		result.append(XMLHelper.javaBeanToXML(sp));
		return result.toString();
	}

	/**
	 * is the data existsed ?
	 * @param con
	 * @param splId
	 * @param upd_time
	 * @return
	 * @throws SQLException
	 */
	public boolean isExists(Connection con, Integer splId)
			throws SQLException {
		return spDao.isSupplierIdExist(con, splId , DEL);
	}

	
	/**
	 * supplier to xml
	 * @param con
	 * @param modParam
	 * @param upd_time
	 * @return
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String getSupplierXml2View(Connection con,
			SupplierModuleParam modParam,String saveDirPath) throws SQLException,
			IllegalArgumentException, IllegalAccessException {
		Supplier sp = spDao.getSupplierViewByID(con, modParam.getSplId(),DEL);
		if(sp!=null){
			if(sp.getSplAttachment1()!=null&&!"".equals(sp.getSplAttachment1()))
			sp.setSplAttachment1(saveDirPath+"/"+sp.getSplAttachment1());
			if(sp.getSplAttachment2()!=null&&!"".equals(sp.getSplAttachment2()))
			sp.setSplAttachment2(saveDirPath+"/"+sp.getSplAttachment2());
			if(sp.getSplAttachment3()!=null&&!"".equals(sp.getSplAttachment3()))
			sp.setSplAttachment3(saveDirPath+"/"+sp.getSplAttachment3());
			if(sp.getSplAttachment4()!=null&&!"".equals(sp.getSplAttachment4()))
			sp.setSplAttachment4(saveDirPath+"/"+sp.getSplAttachment4());
			if(sp.getSplAttachment5()!=null&&!"".equals(sp.getSplAttachment5()))
			sp.setSplAttachment5(saveDirPath+"/"+sp.getSplAttachment5());
			if(sp.getSplAttachment6()!=null&&!"".equals(sp.getSplAttachment6()))
			sp.setSplAttachment6(saveDirPath+"/"+sp.getSplAttachment6());
			if(sp.getSplAttachment7()!=null&&!"".equals(sp.getSplAttachment7()))
			sp.setSplAttachment7(saveDirPath+"/"+sp.getSplAttachment7());
			if(sp.getSplAttachment8()!=null&&!"".equals(sp.getSplAttachment8()))
			sp.setSplAttachment8(saveDirPath+"/"+sp.getSplAttachment8());
			if(sp.getSplAttachment9()!=null&&!"".equals(sp.getSplAttachment9()))
			sp.setSplAttachment9(saveDirPath+"/"+sp.getSplAttachment9());
			if(sp.getSplAttachment10()!=null&&!"".equals(sp.getSplAttachment10()))
			sp.setSplAttachment10(saveDirPath+"/"+sp.getSplAttachment10());
		}
		return XMLHelper.javaBeanToXML(sp);
	}

	/**
	 * Soft delete
	 * @param con
	 * @param modParam
	 * @return
	 * @throws SQLException
	 */
	public boolean delSupplierById(Connection con, SupplierModuleParam modParam)
			throws SQLException {
		return spDao.updateDel(con, modParam.getSplId(), DEL);
	}

	
	/**
	 * 	Get the status of XML
	 * @param status
	 * @param prof
	 * @return
	 */
	public String getStatusXmlString(String status, loginProfile prof) {
		String cur_lang = prof.cur_lan;
		StringBuffer sb = new StringBuffer();
		sb.append("<status_list>");
		//System.out.println("................."+status);
		if (HAVE_COOPERATION.equals(status)) {
			sb.append("<status sel=\"selected\">");
		} else {
			sb.append("<status>");
		}
		sb.append("<name>").append(LangLabel.getValue(cur_lang, HAVE_COOPERATION)).append("</name>")
				.append("<value>" + HAVE_COOPERATION + "</value>")
				.append("</status>");

		if (BID_PASS.equals(status)) {
			sb.append("<status sel=\"selected\">");
		} else {
			sb.append("<status>");
		}

		sb.append("<name>").append(LangLabel.getValue(cur_lang, BID_PASS))
				.append("</name>").append("<value>" + BID_PASS + "</value>")
				.append("</status>");

		if (HAD_MEETING.equals(status)) {
			sb.append("<status sel=\"selected\">");
		} else {
			sb.append("<status>");
		}
		sb.append("<name>").append(LangLabel.getValue(cur_lang, HAD_MEETING))
				.append("</name>").append("<value>" + HAD_MEETING + "</value>")
				.append("</status>");

		if (FIRST_CONTACT.equals(status)) {
			sb.append("<status sel=\"selected\">");
		} else {
			sb.append("<status>");
		}

		sb.append("<name>")
				.append(LangLabel.getValue(cur_lang, FIRST_CONTACT))
				.append("</name>")
				.append("<value>" + FIRST_CONTACT + "</value>")
				.append("</status>");

		if (EXITED.equals(status)) {
			sb.append("<status sel=\"selected\">");
		} else {
			sb.append("<status>");
		}
		sb.append("<name>").append(LangLabel.getValue(cur_lang, EXITED))
				.append("</name>").append("<value>" + EXITED + "</value>")
				.append("</status>")

				.append("</status_list>");

		//System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * add one supplier
	 * 
	 * @param con
	 * @param prof
	 * @param modParam
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public long add(Connection con, loginProfile prof,
			SupplierModuleParam modParam) throws SQLException,
			IllegalArgumentException, IllegalAccessException {
		Supplier sp = new Supplier();

		Field[] fs = sp.getClass().getDeclaredFields();
		Field[] params = modParam.getClass().getDeclaredFields();
		for (Field f : fs) {
			f.setAccessible(true);
			for (Field p : params) {
				p.setAccessible(true);
				if (f.getName().equals(p.getName())) {
					Object value = p.get(modParam);
					f.set(sp, value);
					//System.out.println(value);
					break;
				}
			}
		}
		sp.setSplCreateDatetime(new Timestamp(new Date().getTime()));
		sp.setSplCreateUsrId(prof.getUsr_id());
		sp.setSplUpdateDatetime(new Timestamp(new Date().getTime()));
		sp.setSplUpdateUsrId(prof.getUsr_id());
		sp.setSplTcrId(prof.my_top_tc_id);
		return spDao.insert(con, sp);
	}
	
	/**
	 *  update supplier
	 * @param con
	 * @param modParam
	 * @param upd_time
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SQLException 
	 */
	public void update(Connection con,loginProfile prof, SupplierModuleParam modParam ) throws IllegalArgumentException, IllegalAccessException, SQLException{	
		Supplier sp = spDao.getSupplierViewByID(con, modParam.getSplId(), DEL);
		Field[] fs = sp.getClass().getDeclaredFields();
		Field[] params = modParam.getClass().getDeclaredFields();
		for (Field f : fs) {
			f.setAccessible(true);
			for (Field p : params) {
				p.setAccessible(true);
				if (f.getName().equals(p.getName())) {
					Object value = p.get(modParam);
					if(p.getName().indexOf("splAttachment")> -1){
						if("delete".equals(value)){
							value = "";
						}else if("".equals(value)||"original".equals(value)){
							break;
						}
					}
					f.set(sp, value);
					break;
				}
			}
		}
		sp.setSplUpdateDatetime(new Timestamp(new Date().getTime()));
		sp.setSplUpdateUsrId(prof.getUsr_id());
		spDao.update(con,sp);
	}
	
	
	/**
	 * 	Generate excel file and return to the module
	 * @param con
	 * @param fileName
	 * @param filePath
	 * @param tempDir
	 * @param tempDirName
	 * @param usr_ent_id
	 * @param cur_lang
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws cwException
	 */
	public File download(Connection con, String fileName, String filePath,
			String tempDir, String tempDirName, long usr_ent_id, String cur_lang,String DOWN_LIST_NAME) throws SQLException, IOException, cwException{
		
		List<Supplier> splist = spDao.getSupplierDownList(con , null , DEL);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	WritableWorkbook wwbFile = null;
    	File fin = null;
    	File fout = new File(tempDir + cwUtils.SLASH);
    	if (!fout.exists()) {
    		fout.mkdirs();
    	}
			
    	try {
    		fin = new File(fout, fileName);
        	wwbFile = Workbook.createWorkbook(fin);
        	WritableFont font = new  WritableFont(WritableFont.TIMES, 11 ,WritableFont.NO_BOLD); 
        	
        	WritableCellFormat format = new  WritableCellFormat(font); 
        	format.setAlignment(jxl.format.Alignment.LEFT);	        	 // Set the cell font center on horizontal
        	format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// Set the cell font center on Vertical
        	format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
        	
        	WritableFont font2 = new  WritableFont(WritableFont.TIMES, 12 ,WritableFont.BOLD); 
        	
        	WritableCellFormat format2 = new  WritableCellFormat(font2); 
        	format2.setAlignment(jxl.format.Alignment.CENTRE);	        	 // Set the cell font center on horizontal
        	format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// Set the cell font center on Vertical
        	format2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
        	format2.setBackground(jxl.format.Colour.GREY_25_PERCENT); 
        
        	//excel export name,sheet 1 name
        	String sheetName = LangLabel.getValue(cur_lang, SHEET_NAME);

        	WritableSheet sheet = wwbFile.createSheet(sheetName, 1);
        	
        	int i = 0;	//row number
        	int j = 0;	//column number

        	//sheet.mergeCells( j , i , 4 , i );	
        	//set excel title 	        	
        	//sheet.addCell(new Label(i,j,sheetName,format));
        	//i++;
        	
        	//Add the title to list
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_NAME),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_STATUS),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_REPRESENTATIVE),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_ESTABLISHEDDATE),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_REGISTEREDCAPITAL),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_TYPE),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_ADDRESS),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_CONTACT),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_TEL),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_MOBILE),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_EMAIL),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_TOTAL_STAFF),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_FULL_TIME_INST),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_PART_TIME_INST),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_EXPERTISE),format2));
        	sheet.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_COURSE),format2));
	    
        	if(splist != null && splist.size() > 0){

	        	i++;
	        	for (Supplier spt : splist) {
	        		j = 0;
	        	
	        		addCell(sheet,j++,i,spt.getSplName()==null?"":spt.getSplName(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,LangLabel.getValue(cur_lang,spt.getSplStatus()),format,XLS_COULMN_WIDTH-10);
	        		addCell(sheet,j++,i,spt.getSplRepresentative()==null?"":spt.getSplRepresentative(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplEstablishedDate()==null?"":sdf.format(spt.getSplEstablishedDate()),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplRegisteredCapital()==null?"":spt.getSplRegisteredCapital(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplType()==null?"":spt.getSplType(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplAddress()==null?"":spt.getSplAddress(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplContact()==null?"":spt.getSplContact(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplTel()==null?"":spt.getSplTel(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplMobile()==null?"":spt.getSplMobile(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplEmail()==null?"":spt.getSplEmail(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplTotalStaff()==null?"0":spt.getSplTotalStaff()+"",format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplFullTimeInst()==null?"0":spt.getSplFullTimeInst()+"",format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplPartTimeInst()==null?"0":spt.getSplPartTimeInst()+"",format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplExpertise()==null?"":spt.getSplExpertise(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet,j++,i,spt.getSplCourse()==null?"":spt.getSplCourse(),format,XLS_COULMN_WIDTH);

	        		i++;
	        	}
	    	}
	        	//1024 1030
	        	String sheetName2 = LangLabel.getValue(cur_lang, SHEET2_NAME);
	        	WritableSheet sheet2 = wwbFile.createSheet(sheetName2, 2);
	        	i = 0;
	        	j = 0;
	        	
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_NAME),format2));
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_STATUS),format2));
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SPL_REPRESENTATIVE),format2));
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SCE_START_DATE),format2));
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SCE_END_DATE),format2));
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SCE_ITM_NAME),format2));
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SCE_DESC),format2));
	        	sheet2.addCell(new Label(j++,i,LangLabel.getValue(cur_lang, SCE_DPT),format2));
	        	
        	List<SupplierCooperationExperience> scelist = spDao.getAllSupplierCooperationExperienceDownload(con, 0,DEL);
        	if(scelist != null && scelist.size() > 0 )
        	{	
        		i++;
        		for(SupplierCooperationExperience sce : scelist){
        			j = 0;
    	        	
	        		addCell(sheet2,j++,i,sce.getSplName()==null?"":sce.getSplName(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet2,j++,i,LangLabel.getValue(cur_lang,sce.getSplStatus()),format,XLS_COULMN_WIDTH-10);
	        		addCell(sheet2,j++,i,sce.getSplRepresentative()==null?"":sce.getSplRepresentative(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet2,j++,i,sce.getSceStartDate()==null?"":sdf.format(sce.getSceStartDate()),format,XLS_COULMN_WIDTH);
	        		addCell(sheet2,j++,i,sce.getSceEndDate()==null?"":sdf.format(sce.getSceEndDate()),format,XLS_COULMN_WIDTH);
	        		addCell(sheet2,j++,i,sce.getSceItmName()==null?"":sce.getSceItmName(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet2,j++,i,sce.getSceDesc()==null?"":sce.getSceDesc(),format,XLS_COULMN_WIDTH);
	        		addCell(sheet2,j++,i,sce.getSceDpt()==null?"":sce.getSceDpt(),format,XLS_COULMN_WIDTH);
	        		
	        		i++;
        		}
        	}
	        	
	        wwbFile.write();
			} catch (WriteException e) {
				throw new cwException(e.getMessage());
			} finally {
				try {
					wwbFile.close();
				} catch (WriteException e) {
					throw new cwException(e.getMessage());
				}
	    	}
		
		return fin;
		
	}
	/**
	 * add sheet cell
	 * @param sheet
	 * @param columnNum
	 * @param rowNum
	 * @param value
	 * @param format
	 * @param columnWidth
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void addCell(WritableSheet sheet, int columnNum ,int rowNum,String value,WritableCellFormat format,int columnWidth) throws RowsExceededException, WriteException{
		sheet.setColumnView(columnNum , columnWidth );
		sheet.addCell(new Label(columnNum,rowNum,value,format));
	}
	/**
	 * add supplier cooperation experience
	 * @param con
	 * @param secString
	 * @param modParam
	 * @param prof
	 * @throws SQLException
	 */
	public void addBatchSce(Connection con ,SupplierModuleParam modParam,loginProfile prof) throws SQLException{
		String sceString = modParam.getSceString();
		if(sceString==null || "".equals(sceString)){
			return ;
		}
		JSONArray jsons = JSONArray.fromObject(sceString);
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.setRootClass(SupplierCooperationExperience.class);
		if(jsons!=null && jsons.size()>0){
			for(int i = 0;i<jsons.size();i++){
				JSONObject jobject = JSONObject.fromObject(jsons.get(i));
				JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" ,"yyyy-MM-dd HH:mm:ss.SSS"}));
				SupplierCooperationExperience  sce=	(SupplierCooperationExperience) JSONObject.toBean(jobject,jsonConfig);
				if(sce!=null){
					sce.setSceSplId(modParam.getSplId());
					if(sce.getSceUpdateDatetime()==null){
						sce.setSceUpdateDatetime(new Date());
					}
					//System.out.println(sce.getSceUpdateDatetime().toLocaleString());
					sce.setSceUpdateUsrId(prof.getUsr_id());
					spDao.insertSce(con, sce);
				}
			}
		}
	}
	
	/**
	 * delete all supplier cooperation experience
	 * @param con
	 * @throws SQLException 
	 */
	public void delAllSce(Connection con,Integer splId) throws SQLException{
		spDao.delAllSce(con,splId);
	}


	public String getAllSupplierCommentXml(Connection con, long spl_id,int page,int page_size) throws SQLException, IllegalArgumentException, IllegalAccessException {

		StringBuffer result = new StringBuffer();
		SupplierComment sp = null;
		List<SupplierComment> splist = spDao.getSupplierComment(con,spl_id);
		if (page == 0) {
            page = 1;
        }
        if (page_size == 0) {
            page_size = 10;
        }
        int start = page_size * (page-1);
		int count = 0; 
		result.append("<suppliers id=\"").append(spl_id).append("\">");
		result.append(getNavAsXML(con, spl_id));
		for (int i = 0; i < splist.size(); i++) {
			 if (count >= start && count < start+page_size) {
				 sp = splist.get(i);
				 result.append(XMLHelper.javaBeanToXML(sp));
			 }
			 count++;	
		}
		result.append("</suppliers>");
		cwPagination pagn = new cwPagination();
	    pagn.totalRec = count;
	    pagn.totalPage = (int)Math.ceil((float)count/page_size);
	    pagn.pageSize = page_size;
	    pagn.curPage = page;
	    pagn.sortCol = "";
	    pagn.sortOrder = "";
	    pagn.ts = null;
	    result.append(pagn.asXML());
		return result.toString();
	}

	
	public String getSceListXml(Connection con,Integer splId) throws IllegalArgumentException, IllegalAccessException, SQLException{
		StringBuffer result = new StringBuffer();
		SupplierCooperationExperience sce = null;
		List<SupplierCooperationExperience> scelist = spDao.getAllSupplierCooperationExperience(con, splId);

		result.append("<sce_list>");
		for (int i = 0; i < scelist.size(); i++) {
			sce = scelist.get(i);
			result.append(XMLHelper.javaBeanToXML(sce));
		}
		result.append("</sce_list>");
		//result.append(param.getCwPage().asXML());
		return result.toString();
	}
	
	
	/**
	 *  add all supplier main course
	 * @param con
	 * @param modParam
	 * @param prof
	 * @throws SQLException
	 */
	public void addBatchCourse(Connection con ,SupplierModuleParam modParam,loginProfile prof) throws SQLException{
		String smcString = modParam.getSmcString();
		if(smcString==null || "".equals(smcString)){
			return ;
		}
		JSONArray jsons = JSONArray.fromObject(smcString);
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.setRootClass(SupplierMainCourse.class);
		if(jsons!=null && jsons.size()>0){
			for(int i = 0; i<jsons.size() ;i++){
				SupplierMainCourse  smc = (SupplierMainCourse) JSONObject.toBean(JSONObject.fromObject(jsons.get(i)),jsonConfig);
				JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS"}));
				if(smc!=null){
					//System.out.println(smc.getSmcUpdateDatetime());
					smc.setSmcSplId(modParam.getSplId());
					smc.setSmcUpdateUsrId(prof.getUsr_id());
					if(smc.getSmcUpdateDatetime() == null){
						smc.setSmcUpdateDatetime(new Date());
					}
					spDao.insertCourse(con, smc);
				}
			}
		}
	}
	
	/**
	 * delete all supplier cooperation experience
	 * @param con
	 * @throws SQLException 
	 */
	public void delAllCourse(Connection con,Integer splId) throws SQLException{
		spDao.delAllCourse(con,splId);
	}
	
	public SupplierDao supplierDao(){
		return this.spDao;
	}
	
	/**
	 * get
	 * @param con
	 * @param splId
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public String getCourseListXml(Connection con,Integer splId) throws IllegalArgumentException, IllegalAccessException, SQLException{
		StringBuffer result = new StringBuffer();
		SupplierMainCourse smc = null;
		List<SupplierMainCourse> scelist = spDao.getAllSupplierMainCourse(con, splId);

		result.append("<course_list>");
		for (int i = 0; i < scelist.size(); i++) {
			smc = scelist.get(i);
			result.append(XMLHelper.javaBeanToXML(smc));
		}
		result.append("</course_list>");
		return result.toString();
	}
	
	
	
	public void insSupplierCommentXml(Connection con,SupplierModuleParam modParam) throws SQLException, IllegalArgumentException, IllegalAccessException, qdbException, cwException {

		StringBuffer result = new StringBuffer();
		SupplierComment supp_c = new SupplierComment();
		supp_c.setScm_ent_id(modParam.getScm_ent_id());
		supp_c.setScm_spl_id(modParam.getScm_spl_id());
		supp_c.setScm_design_score(modParam.getScm_design_score());
		supp_c.setScm_management_score(modParam.getScm_management_score());
		supp_c.setScm_teaching_score(modParam.getScm_teaching_score());
		supp_c.setScm_price_score(modParam.getScm_price_score());
		supp_c.setScm_score(modParam.getScm_score());
		supp_c.setScm_comment(modParam.getScm_comment());
		supp_c.setScm_update_datetime(cwSQL.getTime(con));
		supp_c.setScm_create_datetime(cwSQL.getTime(con));
		spDao.insSupplierComment(con,supp_c);
		
	
	}
	
	public String getNavAsXML(Connection con, long spl_id) throws SQLException{
//      try{
          StringBuffer result = new StringBuffer();
          result.append("<nav>").append(cwUtils.NEWL);
          Supplier supper= spDao.getSupplierListFieldsByID(con, spl_id);
          result.append("<supplier id=\"").append(supper.getSplId()).append("\" name =\"").append(cwUtils.esc4XML(supper.getSplName())).append("\"/>");
          result.append("</nav>");
          return result.toString();
//      }catch(qdbException e){
//          throw new SQLException(e.getMessage());
//      }
	}
	public String getSupplierCommentXml(Connection con, long spl_id,long ent_id) throws SQLException, IllegalArgumentException, IllegalAccessException {

		StringBuffer result = new StringBuffer();
		SupplierComment sp = null;
		sp = spDao.getSupplierComment(con,spl_id,ent_id);
		
		int count = 0; 
		result.append("<suppliers id=\"").append(spl_id).append("\">");
		result.append(getNavAsXML(con, spl_id));
		result.append(XMLHelper.javaBeanToXML(sp));
		result.append("</suppliers>");
		return result.toString();
	}
	
	public void upSupplierCommentXml(Connection con,SupplierModuleParam modParam) throws SQLException, IllegalArgumentException, IllegalAccessException, qdbException, cwException {

		StringBuffer result = new StringBuffer();
		SupplierComment supp_c = new SupplierComment();
		supp_c.setScm_ent_id(modParam.getScm_ent_id());
		supp_c.setScm_spl_id(modParam.getScm_spl_id());
		supp_c.setScm_design_score(modParam.getScm_design_score());
		supp_c.setScm_management_score(modParam.getScm_management_score());
		supp_c.setScm_teaching_score(modParam.getScm_teaching_score());
		supp_c.setScm_price_score(modParam.getScm_price_score());
		supp_c.setScm_score(modParam.getScm_score());
		supp_c.setScm_comment(modParam.getScm_comment());
		supp_c.setScm_update_datetime(cwSQL.getTime(con));
		spDao.updateSupplierComment(con,supp_c);
		
	
	}

}
