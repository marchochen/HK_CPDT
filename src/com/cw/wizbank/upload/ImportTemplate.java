package com.cw.wizbank.upload;

import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.lang.Boolean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ImportTemplate {

	public static final String IMPDirName = "htm"+cwUtils.SLASH+"import_template";

	public static final String USER_ID_KEY = "user_id";

	public static final String LABEL_PREFIX = "wb_imp_";

	public final String TEMPLATENAMEPSUBFIX = ".xls";

	public final String ENROLLMENT_TEMPLATE_FILE_PREFIX = "wb_import_enrollment_template";

	public final String QUE_TF_TEMPLATE_FILE_PREFIX = "wb_import_que_tf_template";

	public final String QUE_MT_TEMPLATE_FILE_PREFIX = "wb_import_que_mt_template";

	public final String QUE_MC_TEMPLATE_FILE_PREFIX = "wb_import_que_mc_template";

	public final String QUE_FSC_TEMPLATE_FILE_PREFIX = "wb_import_que_fsc_template";

	public final String QUE_FB_TEMPLATE_FILE_PREFIX = "wb_import_que_fb_template";

	public final String QUE_ES_TEMPLATE_FILE_PREFIX = "wb_import_que_es_template";

	public final String QUE_ES2_TEMPLATE_FILE_PREFIX = "wb_import_que_es2_template";

	public final String QUE_DSC_TEMPLATE_FILE_PREFIX = "wb_import_que_dsc_template";

	public final String USER_PROF_TEMPLATE_FILE_PREFIX = "wb_import_user_profile_template";

	public final String DEL_USER_PROF_TEMPLATE_FILE_PREFIX = "wb_delete_user_profile_template";

	public final String TP_PLAN_TEMPLATE_FILE_PREFIX = "wb_import_tp_plan_template";
	
	public final String CREDITS_TEMPLATE_FILE_PREFIX = "wb_import_credits_template";
	
	public final String CPD_PROF_TEMPLATE_FILE_PREFIX = "wb_import_cpd_profile_template";

    public final String CPTD_AWARDED_HOURS_TEMPLATE = "import_cptd_awarded_hours_template";

	public static String[] LANGS = new String[] { LangLabel.LangCode_en_us, LangLabel.LangCode_zh_cn, LangLabel.LangCode_zh_hk };

	public static Map userProf = new HashMap();
	public static Map delProf = new HashMap();

	public static Map userProfVisiable = new HashMap();

	public static List sizeList = new ArrayList();

	public static Map maxLength = new HashMap();

	public static Map minLength = new HashMap();
	
	public static Map<String,Boolean> allowEmpty = new HashMap<String,Boolean>();
	
	public static Map<String, Boolean> TypeOfDate=new HashMap<String, Boolean>();
	
	public static Map<String, Boolean> TypeOfEmail=new HashMap<String, Boolean>();
	
	public static Map<String, Integer> dbMaxLength=new HashMap<String, Integer>();
	
	public static List<String> importTitle=new ArrayList<String>();
	public static List<String> delTitle=new ArrayList<String>();

	public static Integer groupMaxLevel=0;
	
	public static Integer gradeMaxLevel=0;
	//改回旧版，先注释掉
//	private static String[] group_level_code = { "group_code_level1", "group_code_level2", "group_code_level3", "group_code_level4", "group_code_level5", "group_code_level6",
//	"group_code_level7" };
//	private static String[] group_level_title = { "group_title_level1", "group_title_level2", "group_title_level3", "group_title_level4", "group_title_level5", "group_title_level6",
//	"group_title_level7" };
//	private static String[] grade_level_code = { "grade_code_level1", "grade_code_level2", "grade_code_level3" };
//	private static String[] grade_level_title = { "grade_title_level1", "grade_title_level2", "grade_title_level3" };
	private static String[] group_level_code = { "group_code_level1"};
	private static String[] grade_level_code = { "grade_code_level1"};
	
	public int fieldSize = 57;

	public void rebuild(File appRootDir, File webRootDir, File webConfigDir, PrintWriter out, Connection conn) {
		this.clear();
		this.genTplFile(appRootDir, webRootDir, webConfigDir, out, conn);
	}

	public void clear() {
		ImportTemplate.userProf = new HashMap();
		ImportTemplate.delProf = new HashMap();
		ImportTemplate.userProfVisiable = new HashMap();
		ImportTemplate.sizeList = new ArrayList();
		ImportTemplate.maxLength = new HashMap();
	}

	
	
	
	public void genTplFile(File appRootDir, File webRootDir, File webConfigDir, PrintWriter out, Connection conn) {
		String br = "<br>";
		String pre1 = "<pre>";
		String pre2 = "</pre>";
		if (out == null) {
			out = new PrintWriter(System.out, true);
			br = "";
			pre1 = "";
			pre2 = "";
		}
		CommonLog.info("ImportTemplate.genTplFile() file START..." + br);
		//out.println("ImportTemplate.genTplFile() file START..." + br);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = acSite.getSite(conn);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String site = rs.getString("ste_id");
				ImportTemplate.sizeList.add(site);

				Map actiMap = new HashMap();
				ImportTemplate.userProfVisiable.put(site, actiMap);

				Map fieldMaxlength = new HashMap();
				ImportTemplate.maxLength.put(site, fieldMaxlength);

				Map fieldMinlength = new HashMap();
				ImportTemplate.minLength.put(site, fieldMinlength);

				File profDir = new File(webConfigDir + cwUtils.SLASH + WizbiniLoader.dirPathOrg + cwUtils.SLASH + site);
				LinkedHashMap map = getImpUserProf(profDir, site);
				ImportTemplate.userProf.put(site, map);

				LinkedHashMap delMap = getDelUserProf(profDir, site);
				ImportTemplate.delProf.put(site, delMap);
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage() + br);
			CommonLog.error(pre1);
			CommonLog.error(e.getMessage());
			//out.println(e.getMessage() + br);
			//out.println(pre1);
			//e.printStackTrace(out);
			out.println(pre2);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				CommonLog.error(pre1);
				CommonLog.error(e.getMessage());
				//out.println(pre1);
				//e.printStackTrace(out);
				out.println(pre2);
			}
		}

		File templateDir = new File(webRootDir, ImportTemplate.IMPDirName);
		if (templateDir.isDirectory() && templateDir.exists()) {
			templateDir.delete();
		}
		templateDir.mkdir();

		if (ImportTemplate.sizeList != null && ImportTemplate.sizeList.size() > 0) {
			for (int i = 0; i < ImportTemplate.sizeList.size(); i++) {
				String site = (String) ImportTemplate.sizeList.get(i);

				try {
					genImpUserProf(new File(templateDir, site), site);
				} catch (Exception e) {
					out.println(e.getMessage());
					out.println(pre1);
					e.printStackTrace(out);
					CommonLog.error(e.getMessage(),e);
					out.println(pre2);
				}

				try {
					gendDelUserProf(new File(templateDir, site), site);
				} catch (Exception e) {
					out.println(e.getMessage());
					out.println(pre1);
					e.printStackTrace(out);
					CommonLog.error(e.getMessage(),e);
					out.println(pre2);
				}
				try {
					genImpEnrollmentFile(new File(templateDir, site), site);
				} catch (Exception e) {
					out.println(e.getMessage());
					out.println(pre1);
					e.printStackTrace(out);
					CommonLog.error(e.getMessage(),e);
					out.println(pre2);
				}
			}
		}

		// gen import template for que
		try {
			genImpQueMCFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}

		try {
			genImpQueTFFile(templateDir);
		}
		catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}

		try {
			genImpQueMTFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}

		try {
			genImpQueFSCFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}

		try {
			genImpQueFBFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}

		try {
			genImpQueDSCFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}

		try {
			genImpQueES2File(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}

		try {
			genImpQueESFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}
		
		try {
			genImpPlanFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}
		
		try {
			genImpCreditFile(templateDir);
			genImpCPDFile(templateDir);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(pre1);
			e.printStackTrace(out);
			CommonLog.error(e.getMessage(),e);
			out.println(pre2);
		}     
		
		try {
            genImpCPDAwardHoursFile(templateDir);
        } catch (Exception e) {
            out.println(e.getMessage());
            out.println(pre1);
            e.printStackTrace(out);
            CommonLog.error(e.getMessage(),e);
            out.println(pre2);
        }


		out.println("ImportTemplate.genTplFile() file END..." + br);
	}

	private void genImpCPDFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "label_core_cpt_d_management_222"), LangLabel.getValue(lang, "label_core_cpt_d_management_223")
					,LangLabel.getValue(lang, "label_core_cpt_d_management_224"), LangLabel.getValue(lang, "label_core_cpt_d_management_225") 
					,LangLabel.getValue(lang, "label_core_cpt_d_management_226"), LangLabel.getValue(lang, "label_core_cpt_d_management_227") 
					,LangLabel.getValue(lang, "label_core_cpt_d_management_228"), LangLabel.getValue(lang, "label_core_cpt_d_management_229") 
					};
			file = new File(webRootDir, this.CPD_PROF_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, title, null,null);
		}
		
	}

    private void genImpCPDAwardHoursFile(File webRootDir) throws Exception {
            File file = null;
            String[] title = new String[] { 
                    "User ID*",
                    "Course Code/Class Code*",
                    "License type*",
                    "CPT/D Group Code*",
                    "CPT/D Hours Award Date*",
                    "Core Hours Awarded*",
                    "Non-core Hours Awarded"
                    };
            file = new File(webRootDir, this.CPTD_AWARDED_HOURS_TEMPLATE + this.TEMPLATENAMEPSUBFIX);
            createXLSFile(file, title, null,null);
    }

	
	
	
	private void genImpUserProf(File fileDir, String site) throws Exception {
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		List<String> import_title=new ArrayList<String>();
		
		for(String label:importTitle){
			//用户来源和用户头像将不添加到导入模板中
			if(label.equalsIgnoreCase("extension_43")||label.equalsIgnoreCase("source")){
				continue;
			}
			if(label.equalsIgnoreCase("group")){
				for(int j=0;j<groupMaxLevel;j++){
					if(j==0){
						ImportTemplate.allowEmpty.put(group_level_code[j], false);
//						ImportTemplate.allowEmpty.put(group_level_title[j], false);
					}else{
						ImportTemplate.allowEmpty.put(group_level_code[j], true);
//						ImportTemplate.allowEmpty.put(group_level_title[j], true);
					}
					ImportTemplate.TypeOfDate.put(group_level_code[j], false);
//					ImportTemplate.TypeOfDate.put(group_level_title[j], false);
//					ImportTemplate.TypeOfEmail.put(group_level_title[j], false);
					ImportTemplate.TypeOfEmail.put(group_level_code[j], false);
					ImportTemplate.dbMaxLength.put(group_level_code[j], ImportTemplate.dbMaxLength.get(label));
//					ImportTemplate.dbMaxLength.put(group_level_title[j], ImportTemplate.dbMaxLength.get(label));
					import_title.add(group_level_code[j]);
//					import_title.add(group_level_title[j]);
					Map labelMap=(Map)ImportTemplate.userProf.get(site);
					Map langCodeMap = new HashMap();
					Map langTitleMap = new HashMap();
					for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
						String lang = ImportTemplate.LANGS[i];
						langCodeMap.put(lang, LangLabel.getValue(lang,group_level_code[j]));
//						langTitleMap.put(lang, LangLabel.getValue(lang,group_level_title[j]));
					}
					labelMap.put(group_level_code[j],langCodeMap);
//					labelMap.put(group_level_title[j],langTitleMap);
					
				}
			}else if(label.equalsIgnoreCase("grade")){
				for(int j=0;j<gradeMaxLevel;j++){
					if(j==0){
						ImportTemplate.allowEmpty.put(grade_level_code[j], true);
//						ImportTemplate.allowEmpty.put(grade_level_title[j], true);
					}else{
						ImportTemplate.allowEmpty.put(grade_level_code[j], true);
//						ImportTemplate.allowEmpty.put(grade_level_title[j], true);
					}
					ImportTemplate.TypeOfDate.put(grade_level_code[j], false);
//					ImportTemplate.TypeOfDate.put(grade_level_title[j], false);
					ImportTemplate.TypeOfEmail.put(grade_level_code[j], false);
//					ImportTemplate.TypeOfEmail.put(grade_level_title[j], false);
					ImportTemplate.dbMaxLength.put(grade_level_code[j], ImportTemplate.dbMaxLength.get(label));
//					ImportTemplate.dbMaxLength.put(grade_level_title[j], ImportTemplate.dbMaxLength.get(label));
					import_title.add(grade_level_code[j]);
//					import_title.add(grade_level_title[j]);
					Map labelMap=(Map)ImportTemplate.userProf.get(site);
					Map langCodeMap = new HashMap();
					Map langTitleMap = new HashMap();
					for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
						String lang = ImportTemplate.LANGS[i];
						langCodeMap.put(lang, LangLabel.getValue(lang,grade_level_code[j]));
//						langTitleMap.put(lang, LangLabel.getValue(lang,grade_level_title[j]));
					}
					labelMap.put(grade_level_code[j],langCodeMap);
//					labelMap.put(grade_level_title[j],langTitleMap);
				}
			}else{
				import_title.add(label);
			}
			
		}
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			String lang = ImportTemplate.LANGS[i];
			List<String> xls_title=new ArrayList<String>();
			for(String label:import_title){
				if(label.equalsIgnoreCase("group")){
					for(int j=0;j<groupMaxLevel;j++){
						xls_title.add(LangLabel.getValue(lang, group_level_code[j]));
//						xls_title.add(LangLabel.getValue(lang, group_level_title[j]));
					}
				}
				else if(label.equalsIgnoreCase("grade")){
					for(int j=0;j<gradeMaxLevel;j++){
						xls_title.add(LangLabel.getValue(lang, grade_level_code[j]));
//						xls_title.add(LangLabel.getValue(lang, grade_level_title[j]));
					}
				}else{
					String value = ImportTemplate.getUserLabel(label, lang,site,label);
					if(value.equals("岗位")){
						value="岗位编号";
					}else if(value.equals("崗位")){
						value="崗位編號";
					}	if(value.equals("角色")&&lang.equalsIgnoreCase("zh-cn")){
						value="角色代码";
					}else if(value.equals("角色")&&lang.equalsIgnoreCase("zh-hk")){
						value="角色代碼";
					}if(value.equals("最高報名審批用戶組")){
						value="最高報名審批用戶組編號";
					}else if(value.equals("最高报名审批用户组")){
						value="最高报名审批用户组编号";
					}	if(value.equals("下属部门")){
						value="下属部门编号";
					}else if(value.equals("管理部門")){
						value="管理部門編號";
					}		
					xls_title.add(value);
				}
			}
			String[] fakeData = new String[]{};	
			File file = new File(fileDir, this.USER_PROF_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, xls_title.toArray(new String[xls_title.size()]), null,fakeData);
		}
		importTitle=import_title;
	}

	private void gendDelUserProf(File fileDir, String site) throws Exception {
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		List<String> import_title=new ArrayList<String>();

		for(String label:delTitle){
			//用户来源和用户头像将不添加到导入模板中
			if(label.equalsIgnoreCase("extension_43")||label.equalsIgnoreCase("source")){
				continue;
			}
			if(label.equalsIgnoreCase("group")){
				for(int j=0;j<groupMaxLevel;j++){
					if(j==0){
						ImportTemplate.allowEmpty.put(group_level_code[j], false);
//						ImportTemplate.allowEmpty.put(group_level_title[j], false);
					}else{
						ImportTemplate.allowEmpty.put(group_level_code[j], true);
//						ImportTemplate.allowEmpty.put(group_level_title[j], true);
					}
					ImportTemplate.TypeOfDate.put(group_level_code[j], false);
//					ImportTemplate.TypeOfDate.put(group_level_title[j], false);
//					ImportTemplate.TypeOfEmail.put(group_level_title[j], false);
					ImportTemplate.TypeOfEmail.put(group_level_code[j], false);
					ImportTemplate.dbMaxLength.put(group_level_code[j], ImportTemplate.dbMaxLength.get(label));
//					ImportTemplate.dbMaxLength.put(group_level_title[j], ImportTemplate.dbMaxLength.get(label));
					import_title.add(group_level_code[j]);
//					import_title.add(group_level_title[j]);
					Map labelMap=(Map)ImportTemplate.delProf.get(site);
					Map langCodeMap = new HashMap();
					Map langTitleMap = new HashMap();
					for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
						String lang = ImportTemplate.LANGS[i];
						langCodeMap.put(lang, LangLabel.getValue(lang,group_level_code[j]));
//						langTitleMap.put(lang, LangLabel.getValue(lang,group_level_title[j]));
					}
					labelMap.put(group_level_code[j],langCodeMap);
//					labelMap.put(group_level_title[j],langTitleMap);

				}
			}else if(label.equalsIgnoreCase("grade")){
				for(int j=0;j<gradeMaxLevel;j++){
					if(j==0){
						ImportTemplate.allowEmpty.put(grade_level_code[j], true);
//						ImportTemplate.allowEmpty.put(grade_level_title[j], true);
					}else{
						ImportTemplate.allowEmpty.put(grade_level_code[j], true);
//						ImportTemplate.allowEmpty.put(grade_level_title[j], true);
					}
					ImportTemplate.TypeOfDate.put(grade_level_code[j], false);
//					ImportTemplate.TypeOfDate.put(grade_level_title[j], false);
					ImportTemplate.TypeOfEmail.put(grade_level_code[j], false);
//					ImportTemplate.TypeOfEmail.put(grade_level_title[j], false);
					ImportTemplate.dbMaxLength.put(grade_level_code[j], ImportTemplate.dbMaxLength.get(label));
//					ImportTemplate.dbMaxLength.put(grade_level_title[j], ImportTemplate.dbMaxLength.get(label));
					import_title.add(grade_level_code[j]);
//					import_title.add(grade_level_title[j]);
					Map labelMap=(Map)ImportTemplate.delProf.get(site);
					Map langCodeMap = new HashMap();
					Map langTitleMap = new HashMap();
					for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
						String lang = ImportTemplate.LANGS[i];
						langCodeMap.put(lang, LangLabel.getValue(lang,grade_level_code[j]));
//						langTitleMap.put(lang, LangLabel.getValue(lang,grade_level_title[j]));
					}
					labelMap.put(grade_level_code[j],langCodeMap);
//					labelMap.put(grade_level_title[j],langTitleMap);
				}
			}else{
				import_title.add(label);
			}

		}
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			String lang = ImportTemplate.LANGS[i];
			List<String> xls_title=new ArrayList<String>();
			for(String label:import_title){
				if(label.equalsIgnoreCase("group")){
					for(int j=0;j<groupMaxLevel;j++){
						xls_title.add(LangLabel.getValue(lang, group_level_code[j]));
//						xls_title.add(LangLabel.getValue(lang, group_level_title[j]));
					}
				}
				else if(label.equalsIgnoreCase("grade")){
					for(int j=0;j<gradeMaxLevel;j++){
						xls_title.add(LangLabel.getValue(lang, grade_level_code[j]));
//						xls_title.add(LangLabel.getValue(lang, grade_level_title[j]));
					}
				}else{
					String value = ImportTemplate.getUserLabel(label, lang,site,label);
					if(value.equals("岗位")){
						value="岗位编号";
					}else if(value.equals("崗位")){
						value="崗位編號";
					}	if(value.equals("角色")&&lang.equalsIgnoreCase("zh-cn")){
						value="角色代码";
					}else if(value.equals("角色")&&lang.equalsIgnoreCase("zh-hk")){
						value="角色代碼";
					}if(value.equals("最高報名審批用戶組")){
						value="最高報名審批用戶組編號";
					}else if(value.equals("最高报名审批用户组")){
						value="最高报名审批用户组编号";
					}	if(value.equals("下属部门")){
						value="下属部门编号";
					}else if(value.equals("管理部門")){
						value="管理部門編號";
					}
					xls_title.add(value);
				}
			}
			String[] fakeData = new String[]{};
			File file = new File(fileDir, this.DEL_USER_PROF_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, xls_title.toArray(new String[xls_title.size()]), null,fakeData);
		}
		delTitle=import_title;
	}

	public static String[] getUserLabelArray(String lang, String site) {
		String userID = "User ID", password = "Password", name = "Name", gender = "Gender", birth = "Date of Birth";
		String email = "Email", phone = "Phone", fax = "Fax", job = "Job Title", grade = "Grade Code";
		String group = "Group Code", superIDs = "Direct Supervisor IDs", join = "Join Date", role = "Role Codes", superGroup = "Supervised Group Codes";
		String apprGroup = "Highest Approval Group", source = "Source";
		String extra1 = "Extra 1", extra2 = "Extra 2", extra3 = "Extra 3", extra4 = "Extra 4", extra5 = "Extra 5";
		String extra6 = "Extra 6", extra7 = "Extra 7", extra8 = "Extra 8", extra9 = "Extra 9", extra10 = "Extra 10";
		String extraDt11 = "Extra Datetime 11", extraDt12 = "Extra Datetime 12", extraDt13 = "Extra Datetime 13", extraDt14 = "Extra Datetime 14", extraDt15 = "Extra Datetime 15";
		String extraDt16 = "Extra Datetime 16", extraDt17 = "Extra Datetime 17", extraDt18 = "Extra Datetime 18", extraDt19 = "Extra Datetime 19", extraDt20 = "Extra Datetime 20";
		String extraSlt21 = "Extra Singleoption 21", extraSlt22 = "Extra Singleoption 22", extraSlt23 = "Extra Singleoption 23", extraSlt24 = "Extra Singleoption 24", extraSlt25 = "Extra Singleoption 25";
		String extraSlt26 = "Extra Singleoption 26", extraSlt27 = "Extra Singleoption 27", extraSlt28 = "Extra Singleoption 28", extraSlt29 = "Extra Singleoption 29", extraSlt30 = "Extra Singleoption 30";
		String extraMpt31 = "Extra Multipleoption 31", extraMpt32 = "Extra Multipleoption 32", extraMpt33 = "Extra Multipleoption 33", extraMpt34 = "Extra Multipleoption 34", extraMpt35 = "Extra Multipleoption 35";
		String extraMpt36 = "Extra Multipleoption 36", extraMpt37 = "Extra Multipleoption 37", extraMpt38 = "Extra Multipleoption 38", extraMpt39 = "Extra Multipleoption 39", extraMpt40 = "Extra Multipleoption 40";
		String nickname = "nickname";


		userID = ImportTemplate.getUserLabel(ImportTemplate.USER_ID_KEY, lang, site, userID);
		password = ImportTemplate.getUserLabel("password", lang, site, password);
		name = ImportTemplate.getUserLabel("name", lang, site, name);
		nickname = ImportTemplate.getUserLabel("nickname", lang, site, nickname);
		gender = ImportTemplate.getUserLabel("gender", lang, site, gender);
		birth = ImportTemplate.getUserLabel("date_of_birth", lang, site, birth);

		email = ImportTemplate.getUserLabel("email", lang, site, email);
		phone = ImportTemplate.getUserLabel("phone", lang, site, phone);
		fax = ImportTemplate.getUserLabel("fax", lang, site, fax);
		job = ImportTemplate.getUserLabel("job_title", lang, site, job);
		grade = ImportTemplate.getUserLabel("grade", lang, site, grade);

		group = ImportTemplate.getUserLabel("group", lang, site, group);
		superIDs = ImportTemplate.getUserLabel("direct_supervisors", lang, site, superIDs);
		join = ImportTemplate.getUserLabel("join_date", lang, site, join);
		role = ImportTemplate.getUserLabel("role", lang, site, role);
		superGroup = ImportTemplate.getUserLabel("supervised_groups", lang, site, superGroup);

		apprGroup = ImportTemplate.getUserLabel("app_approval_usg_ent_id", lang, site, apprGroup);
		source = ImportTemplate.getUserLabel("source", lang, site, source);

		extra1 = ImportTemplate.getUserLabel("extension_1", lang, site, extra1);
		extra2 = ImportTemplate.getUserLabel("extension_2", lang, site, extra2);
		extra3 = ImportTemplate.getUserLabel("extension_3", lang, site, extra3);
		extra4 = ImportTemplate.getUserLabel("extension_4", lang, site, extra4);
		extra5 = ImportTemplate.getUserLabel("extension_5", lang, site, extra5);
		extra6 = ImportTemplate.getUserLabel("extension_6", lang, site, extra6);
		extra7 = ImportTemplate.getUserLabel("extension_7", lang, site, extra7);
		extra8 = ImportTemplate.getUserLabel("extension_8", lang, site, extra8);
		extra9 = ImportTemplate.getUserLabel("extension_9", lang, site, extra9);
		extra10 = ImportTemplate.getUserLabel("extension_10", lang, site, extra10);

		extraDt11 = ImportTemplate.getUserLabel("extension_11", lang, site, extraDt11);
		extraDt12 = ImportTemplate.getUserLabel("extension_12", lang, site, extraDt12);
		extraDt13 = ImportTemplate.getUserLabel("extension_13", lang, site, extraDt13);
		extraDt14 = ImportTemplate.getUserLabel("extension_14", lang, site, extraDt14);
		extraDt15 = ImportTemplate.getUserLabel("extension_15", lang, site, extraDt15);
		extraDt16 = ImportTemplate.getUserLabel("extension_16", lang, site, extraDt16);
		extraDt17 = ImportTemplate.getUserLabel("extension_17", lang, site, extraDt17);
		extraDt18 = ImportTemplate.getUserLabel("extension_18", lang, site, extraDt18);
		extraDt19 = ImportTemplate.getUserLabel("extension_19", lang, site, extraDt19);
		extraDt20 = ImportTemplate.getUserLabel("extension_20", lang, site, extraDt20);

		extraSlt21 = ImportTemplate.getUserLabel("extension_21", lang, site, extraSlt21);
		extraSlt22 = ImportTemplate.getUserLabel("extension_22", lang, site, extraSlt22);
		extraSlt23 = ImportTemplate.getUserLabel("extension_23", lang, site, extraSlt23);
		extraSlt24 = ImportTemplate.getUserLabel("extension_24", lang, site, extraSlt24);
		extraSlt25 = ImportTemplate.getUserLabel("extension_25", lang, site, extraSlt25);
		extraSlt26 = ImportTemplate.getUserLabel("extension_26", lang, site, extraSlt26);
		extraSlt27 = ImportTemplate.getUserLabel("extension_27", lang, site, extraSlt27);
		extraSlt28 = ImportTemplate.getUserLabel("extension_28", lang, site, extraSlt28);
		extraSlt29 = ImportTemplate.getUserLabel("extension_29", lang, site, extraSlt29);
		extraSlt30 = ImportTemplate.getUserLabel("extension_30", lang, site, extraSlt30);

		extraMpt31 = ImportTemplate.getUserLabel("extension_31", lang, site, extraMpt31);
		extraMpt32 = ImportTemplate.getUserLabel("extension_32", lang, site, extraMpt32);
		extraMpt33 = ImportTemplate.getUserLabel("extension_33", lang, site, extraMpt33);
		extraMpt34 = ImportTemplate.getUserLabel("extension_34", lang, site, extraMpt34);
		extraMpt35 = ImportTemplate.getUserLabel("extension_35", lang, site, extraMpt35);
		extraMpt36 = ImportTemplate.getUserLabel("extension_36", lang, site, extraMpt36);
		extraMpt37 = ImportTemplate.getUserLabel("extension_37", lang, site, extraMpt37);
		extraMpt38 = ImportTemplate.getUserLabel("extension_38", lang, site, extraMpt38);
		extraMpt39 = ImportTemplate.getUserLabel("extension_39", lang, site, extraMpt39);
		extraMpt40 = ImportTemplate.getUserLabel("extension_40", lang, site, extraMpt40);

		String[] labels = new String[] { userID, password, name, gender, birth, email, phone, fax, job, grade, group,
				superIDs, join, role,
				superGroup, apprGroup, source, extra1, extra2, extra3, extra4, extra5, extra6, extra7, extra8, extra9, extra10, extraDt11, extraDt12,
				extraDt13, extraDt14, extraDt15, extraDt16, extraDt17, extraDt18, extraDt19, extraDt20, extraSlt21, extraSlt22, extraSlt23,
				extraSlt24, extraSlt25, extraSlt26, extraSlt27, extraSlt28, extraSlt29, extraSlt30, extraMpt31, extraMpt32, extraMpt33, extraMpt34,
				extraMpt35, extraMpt36, extraMpt37, extraMpt38, extraMpt39, extraMpt40, nickname};
		return labels;
	}

	public static boolean[] getActiveLabelArray(String site) {
		boolean userID = true, password = true, name = true, gender = true, birth = true;
		boolean email = true, phone = true, fax = true, job = true, grade = true;
		boolean group = true, superIDs = true, join = true, role = true, superGroup = true;
		boolean apprGroup = true, source = true;
		boolean extra1 = true, extra2 = true, extra3 = true, extra4 = true, extra5 = true;
		boolean extra6 = true, extra7 = true, extra8 = true, extra9 = true, extra10 = true;
		boolean extraDt11 = true, extraDt12 = true, extraDt13 = true, extraDt14 = true, extraDt15 = true;
		boolean extraDt16 = true, extraDt17 = true, extraDt18 = true, extraDt19 = true, extraDt20 = true;
		boolean extraSlt21 = true, extraSlt22 = true, extraSlt23 = true, extraSlt24 = true, extraSlt25 = true;
		boolean extraSlt26 = true, extraSlt27 = true, extraSlt28 = true, extraSlt29 = true, extraSlt30 = true;
		boolean extraMpt31 = true, extraMpt32 = true, extraMpt33 = true, extraMpt34 = true, extraMpt35 = true;
		boolean extraMpt36 = true, extraMpt37 = true, extraMpt38 = true, extraMpt39 = true, extraMpt40 = true;
		boolean nickname = true;
		userID = ImportTemplate.getActiveLabel(ImportTemplate.USER_ID_KEY, site, userID);
		password = ImportTemplate.getActiveLabel("password", site, password);
		name = ImportTemplate.getActiveLabel("name", site, name);
		nickname = ImportTemplate.getActiveLabel("nickname", site, nickname);
		gender = ImportTemplate.getActiveLabel("gender", site, gender);
		birth = ImportTemplate.getActiveLabel("date_of_birth", site, birth);

		email = ImportTemplate.getActiveLabel("email", site, email);
		phone = ImportTemplate.getActiveLabel("phone", site, phone);
		fax = ImportTemplate.getActiveLabel("fax", site, fax);
		job = ImportTemplate.getActiveLabel("job_title", site, job);
		grade = ImportTemplate.getActiveLabel("grade", site, grade);

		group = ImportTemplate.getActiveLabel("group", site, group);
		superIDs = ImportTemplate.getActiveLabel("direct_supervisors", site, superIDs);
		join = ImportTemplate.getActiveLabel("join_date", site, join);
		role = ImportTemplate.getActiveLabel("role", site, role);
		superGroup = ImportTemplate.getActiveLabel("supervised_groups", site, superGroup);

		apprGroup = ImportTemplate.getActiveLabel("app_approval_usg_ent_id", site, apprGroup);
		source = ImportTemplate.getActiveLabel("source", site, source);

		extra1 = ImportTemplate.getActiveLabel("extension_1", site, extra1);
		extra2 = ImportTemplate.getActiveLabel("extension_2", site, extra2);
		extra3 = ImportTemplate.getActiveLabel("extension_3", site, extra3);
		extra4 = ImportTemplate.getActiveLabel("extension_4", site, extra4);
		extra5 = ImportTemplate.getActiveLabel("extension_5", site, extra5);
		extra6 = ImportTemplate.getActiveLabel("extension_6", site, extra6);
		extra7 = ImportTemplate.getActiveLabel("extension_7", site, extra7);
		extra8 = ImportTemplate.getActiveLabel("extension_8", site, extra8);
		extra9 = ImportTemplate.getActiveLabel("extension_9", site, extra9);
		extra10 = ImportTemplate.getActiveLabel("extension_10", site, extra10);

		extraDt11 = ImportTemplate.getActiveLabel("extension_11", site, extraDt11);
		extraDt12 = ImportTemplate.getActiveLabel("extension_12", site, extraDt12);
		extraDt13 = ImportTemplate.getActiveLabel("extension_13", site, extraDt13);
		extraDt14 = ImportTemplate.getActiveLabel("extension_14", site, extraDt14);
		extraDt15 = ImportTemplate.getActiveLabel("extension_15", site, extraDt15);
		extraDt16 = ImportTemplate.getActiveLabel("extension_16", site, extraDt16);
		extraDt17 = ImportTemplate.getActiveLabel("extension_17", site, extraDt17);
		extraDt18 = ImportTemplate.getActiveLabel("extension_18", site, extraDt18);
		extraDt19 = ImportTemplate.getActiveLabel("extension_19", site, extraDt19);
		extraDt20 = ImportTemplate.getActiveLabel("extension_20", site, extraDt20);

		extraSlt21 = ImportTemplate.getActiveLabel("extension_21", site, extraSlt21);
		extraSlt22 = ImportTemplate.getActiveLabel("extension_22", site, extraSlt22);
		extraSlt23 = ImportTemplate.getActiveLabel("extension_23", site, extraSlt23);
		extraSlt24 = ImportTemplate.getActiveLabel("extension_24", site, extraSlt24);
		extraSlt25 = ImportTemplate.getActiveLabel("extension_25", site, extraSlt25);
		extraSlt26 = ImportTemplate.getActiveLabel("extension_26", site, extraSlt26);
		extraSlt27 = ImportTemplate.getActiveLabel("extension_27", site, extraSlt27);
		extraSlt28 = ImportTemplate.getActiveLabel("extension_28", site, extraSlt28);
		extraSlt29 = ImportTemplate.getActiveLabel("extension_29", site, extraSlt29);
		extraSlt30 = ImportTemplate.getActiveLabel("extension_30", site, extraSlt30);

		extraMpt31 = ImportTemplate.getActiveLabel("extension_31", site, extraMpt31);
		extraMpt32 = ImportTemplate.getActiveLabel("extension_32", site, extraMpt32);
		extraMpt33 = ImportTemplate.getActiveLabel("extension_33", site, extraMpt33);
		extraMpt34 = ImportTemplate.getActiveLabel("extension_34", site, extraMpt34);
		extraMpt35 = ImportTemplate.getActiveLabel("extension_35", site, extraMpt35);
		extraMpt36 = ImportTemplate.getActiveLabel("extension_36", site, extraMpt36);
		extraMpt37 = ImportTemplate.getActiveLabel("extension_37", site, extraMpt37);
		extraMpt38 = ImportTemplate.getActiveLabel("extension_38", site, extraMpt38);
		extraMpt39 = ImportTemplate.getActiveLabel("extension_39", site, extraMpt39);
		extraMpt40 = ImportTemplate.getActiveLabel("extension_40", site, extraMpt40);


		boolean[] labels = new boolean[] { userID, password, name, gender, birth, email, phone,
				fax, job, grade, group, superIDs, join, role,
				superGroup, apprGroup, source, extra1, extra2, extra3, extra4, extra5, extra6, extra7, extra8, extra9, extra10, extraDt11, extraDt12,
				extraDt13, extraDt14, extraDt15, extraDt16, extraDt17, extraDt18, extraDt19, extraDt20, extraSlt21, extraSlt22, extraSlt23,
				extraSlt24, extraSlt25, extraSlt26, extraSlt27, extraSlt28, extraSlt29, extraSlt30, extraMpt31, extraMpt32, extraMpt33, extraMpt34,
				extraMpt35, extraMpt36, extraMpt37, extraMpt38, extraMpt39, extraMpt40, nickname };
		return labels;
	}

	// enrollment
	private void genImpEnrollmentFile(File webRootDir, String site) throws Exception {
		if (!webRootDir.exists()) {
			webRootDir.mkdir();
		}
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			String lang = ImportTemplate.LANGS[i];
			String userID = "User ID";
			userID = ImportTemplate.getUserLabel(ImportTemplate.USER_ID_KEY, lang, site, userID);
			String[] title = new String[] { userID, LangLabel.getValue(lang, "wb_imp_enr_enrollment_workflow"),
					LangLabel.getValue(lang, "wb_imp_enr_completion_status"), LangLabel.getValue(lang, "wb_imp_enr_completion_date"),
					LangLabel.getValue(lang, "wb_imp_enr_completion_remarks"), LangLabel.getValue(lang, "wb_imp_enr_score"),
					LangLabel.getValue(lang, "wb_imp_enr_attendance"), LangLabel.getValue(lang, "wb_imp_enr_send_mail") };
			File file = new File(webRootDir, this.ENROLLMENT_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, title, "Data",null);
		}
	}

	// que user prof
	public LinkedHashMap getImpUserProf(File appRootDir, String site) {
		File file = new File(appRootDir, WizbiniLoader.cfgFileUserManagement);

		LinkedHashMap fieldsmap = new LinkedHashMap();
		InputSource in;
		try {
			in = new InputSource(new InputStreamReader(cwUtils.openUTF8FileStream(file), cwUtils.ENC_UTF));
			DOMParser parser = new DOMParser();
			parser.parse(in);
			Document document = parser.getDocument();
			NodeList nodeList = document.getElementsByTagName("profile_attributes");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				NodeList nodeList2 = node.getChildNodes();
				int t = 0;
				for (int k = 0; k < nodeList2.getLength(); k++) {
					Node item = nodeList2.item(k);
					if (item.getNodeType() == Element.ELEMENT_NODE) {
						Map labelMap = new HashMap();
						String fieldName = item.getNodeName();

						String active = getNodeAttr(item, "active");
						Map map = (Map) ImportTemplate.userProfVisiable.get(site);
						if (active != null && active.equals("false")) {
							map.put(fieldName, "false");
						} else {
							map.put(fieldName, "true");
							ImportTemplate.importTitle.add(fieldName);
						}

						// get the field label
						NodeList labelList = item.getChildNodes();
						for (int j = 0; j < labelList.getLength(); j++) {
							Node label = labelList.item(j);
							if (label.getNodeType() == Element.ELEMENT_NODE && label.getNodeName().equals("label")) {
								String title = label.getFirstChild().getNodeValue();
								String lang = getNodeAttr(label, "xml:lang");
								labelMap.put(lang, title);
							}
						}
						fieldsmap.put(fieldName, labelMap);

						String minLength = getNodeAttr(item, "min_length");
						if (minLength != null && !minLength.equals("")) {
							Map lengthMap = (Map) ImportTemplate.minLength.get(site);
							lengthMap.put(fieldName, minLength);
						}

						//最大长度
						String dbLength=getNodeAttr(item, "dbMaxLength");
						if(dbLength != null && !dbLength.equals("")){
							ImportTemplate.dbMaxLength.put(fieldName, Integer.valueOf(dbLength));
						}else{
							ImportTemplate.dbMaxLength.put(fieldName, Integer.valueOf(50));
						}
						String maxLength = getNodeAttr(item, "max_length");
						if (maxLength != null && !maxLength.equals("")) {
							Map lengthMap = (Map) ImportTemplate.maxLength.get(site);
							ImportTemplate.dbMaxLength.put(fieldName, Integer.valueOf(maxLength));
							lengthMap.put(fieldName, maxLength);
						}

						//ture表示允许为空,False表示不能为空
						String allowEmpty=getNodeAttr(item, "allowEmpty");
						if(allowEmpty!=null && !allowEmpty.equals("")){
							ImportTemplate.allowEmpty.put(fieldName, Boolean.valueOf(allowEmpty));
						}else{
							ImportTemplate.allowEmpty.put(fieldName, true);
						}
						
						//true表示为时间,False表示不是时间类型那个
						String dateType=getNodeAttr(item, "type");
						if(dateType != null && !dateType.equals("") && dateType.equalsIgnoreCase("date")){
							ImportTemplate.TypeOfDate.put(fieldName, true);
						}else{
							ImportTemplate.TypeOfDate.put(fieldName, false);
						}
						//true表示为邮箱,False表示不是邮箱类型
						if(fieldName != null && !fieldName.equals("") && fieldName.equalsIgnoreCase("email")){
							ImportTemplate.TypeOfEmail.put(fieldName, true);
						}else{
							ImportTemplate.TypeOfEmail.put(fieldName, false);
						}
						//用户组最大级别
						String groupLevel=getNodeAttr(item, "groupLevel");
						if(fieldName != null && !fieldName.equals("") && fieldName.equalsIgnoreCase("group")
							&& groupLevel != null && !groupLevel.equals("")){
							ImportTemplate.groupMaxLevel=Integer.valueOf(groupLevel);
						}
						//职务最大级别
						String gradeLevel=getNodeAttr(item, "gradeLevel");
						if(fieldName != null && !fieldName.equals("") && fieldName.equalsIgnoreCase("grade")
							&& gradeLevel != null && !gradeLevel.equals("")){
							ImportTemplate.gradeMaxLevel=Integer.valueOf(gradeLevel);
						}
						t++;
					}
				}
			
			}
		} catch (UnsupportedEncodingException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (SAXException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
		}

		return fieldsmap;
	}

	// que user prof
	public LinkedHashMap getDelUserProf(File appRootDir, String site) {
		File file = new File(appRootDir, WizbiniLoader.cfgFileDelUserManagement);

		LinkedHashMap fieldsmap = new LinkedHashMap();
		InputSource in;
		try {
			in = new InputSource(new InputStreamReader(cwUtils.openUTF8FileStream(file), cwUtils.ENC_UTF));
			DOMParser parser = new DOMParser();
			parser.parse(in);
			Document document = parser.getDocument();
			NodeList nodeList = document.getElementsByTagName("profile_attributes");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				NodeList nodeList2 = node.getChildNodes();
				int t = 0;
				for (int k = 0; k < nodeList2.getLength(); k++) {
					Node item = nodeList2.item(k);
					if (item.getNodeType() == Element.ELEMENT_NODE) {
						Map labelMap = new HashMap();
						String fieldName = item.getNodeName();

						String active = getNodeAttr(item, "active");
						Map map = (Map) ImportTemplate.userProfVisiable.get(site);
						if (active != null && active.equals("false")) {
							map.put(fieldName, "false");
						} else {
							map.put(fieldName, "true");
							ImportTemplate.delTitle.add(fieldName);
						}

						// get the field label
						NodeList labelList = item.getChildNodes();
						for (int j = 0; j < labelList.getLength(); j++) {
							Node label = labelList.item(j);
							if (label.getNodeType() == Element.ELEMENT_NODE && label.getNodeName().equals("label")) {
								String title = label.getFirstChild().getNodeValue();
								String lang = getNodeAttr(label, "xml:lang");
								labelMap.put(lang, title);
							}
						}
						fieldsmap.put(fieldName, labelMap);

						String minLength = getNodeAttr(item, "min_length");
						if (minLength != null && !minLength.equals("")) {
							Map lengthMap = (Map) ImportTemplate.minLength.get(site);
							lengthMap.put(fieldName, minLength);
						}

						//最大长度
						String dbLength=getNodeAttr(item, "dbMaxLength");
						if(dbLength != null && !dbLength.equals("")){
							ImportTemplate.dbMaxLength.put(fieldName, Integer.valueOf(dbLength));
						}else{
							ImportTemplate.dbMaxLength.put(fieldName, Integer.valueOf(50));
						}
						String maxLength = getNodeAttr(item, "max_length");
						if (maxLength != null && !maxLength.equals("")) {
							Map lengthMap = (Map) ImportTemplate.maxLength.get(site);
							ImportTemplate.dbMaxLength.put(fieldName, Integer.valueOf(maxLength));
							lengthMap.put(fieldName, maxLength);
						}

						//ture表示允许为空,False表示不能为空
						String allowEmpty=getNodeAttr(item, "allowEmpty");
						if(allowEmpty!=null && !allowEmpty.equals("")){
							ImportTemplate.allowEmpty.put(fieldName, Boolean.valueOf(allowEmpty));
						}else{
							ImportTemplate.allowEmpty.put(fieldName, true);
						}

						//true表示为时间,False表示不是时间类型那个
						String dateType=getNodeAttr(item, "type");
						if(dateType != null && !dateType.equals("") && dateType.equalsIgnoreCase("date")){
							ImportTemplate.TypeOfDate.put(fieldName, true);
						}else{
							ImportTemplate.TypeOfDate.put(fieldName, false);
						}
						//true表示为邮箱,False表示不是邮箱类型
						if(fieldName != null && !fieldName.equals("") && fieldName.equalsIgnoreCase("email")){
							ImportTemplate.TypeOfEmail.put(fieldName, true);
						}else{
							ImportTemplate.TypeOfEmail.put(fieldName, false);
						}
						//用户组最大级别
						String groupLevel=getNodeAttr(item, "groupLevel");
						if(fieldName != null && !fieldName.equals("") && fieldName.equalsIgnoreCase("group")
								&& groupLevel != null && !groupLevel.equals("")){
							ImportTemplate.groupMaxLevel=Integer.valueOf(groupLevel);
						}
						//职务最大级别
						String gradeLevel=getNodeAttr(item, "gradeLevel");
						if(fieldName != null && !fieldName.equals("") && fieldName.equalsIgnoreCase("grade")
								&& gradeLevel != null && !gradeLevel.equals("")){
							ImportTemplate.gradeMaxLevel=Integer.valueOf(gradeLevel);
						}
						t++;
					}
				}

			}
		} catch (UnsupportedEncodingException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (SAXException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
		}

		return fieldsmap;
	}
	// que dsc
	private void genImpQueDSCFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_resource_id"),
					LangLabel.getValue(lang, "wb_imp_tem_title"), LangLabel.getValue(lang, "wb_imp_tem_question"),
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"),
					LangLabel.getValue(lang, "wb_imp_tem_difficulty"), LangLabel.getValue(lang, "wb_imp_tem_status"),
					LangLabel.getValue(lang, "wb_imp_tem_criteria"), LangLabel.getValue(lang, "wb_imp_tem_description"),
					LangLabel.getValue(lang, "wb_imp_tem_option_1"), LangLabel.getValue(lang, "wb_imp_tem_option_2"),
					LangLabel.getValue(lang, "wb_imp_tem_option_3"), LangLabel.getValue(lang, "wb_imp_tem_option_4"),
					LangLabel.getValue(lang, "wb_imp_tem_option_5"), LangLabel.getValue(lang, "wb_imp_tem_option_6"),
					LangLabel.getValue(lang, "wb_imp_tem_option_7"), LangLabel.getValue(lang, "wb_imp_tem_option_8"),
					LangLabel.getValue(lang, "wb_imp_tem_option_9"), LangLabel.getValue(lang, "wb_imp_tem_option_10"),
					LangLabel.getValue(lang, "wb_imp_tem_shuffle_option"), LangLabel.getValue(lang, "wb_imp_tem_answer"),
					LangLabel.getValue(lang, "wb_imp_tem_score") };
			file = new File(webRootDir, this.QUE_DSC_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, title, "Data",null);
		}
	}

	// que es
	private void genImpQueESFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_resource_id"),
					LangLabel.getValue(lang, "wb_imp_tem_title"), LangLabel.getValue(lang, "wb_imp_tem_question"),
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"), 
					LangLabel.getValue(lang, "wb_imp_tem_model_answer"),
					LangLabel.getValue(lang, "wb_imp_tem_score"), LangLabel.getValue(lang, "wb_imp_tem_difficulty"),
					LangLabel.getValue(lang, "wb_imp_tem_status") //LangLabel.getValue(lang, "wb_imp_tem_description"),
					//LangLabel.getValue(lang, "wb_imp_tem_submit_file") 
					};
			file = new File(webRootDir, this.QUE_ES_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			/*String[] esExample = new String[] {"1", "",
					"新增问答题示例", "大鲵的归属类别是？",
					"两栖类", "2", "3",
					"OFF"};*/
			String[] esExample = new String[] {};
			createXLSFile(file, title, null,esExample);
		}
	}

	// que es2
	private void genImpQueES2File(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_title"),
					LangLabel.getValue(lang, "wb_imp_tem_question"), 
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"),
					LangLabel.getValue(lang, "wb_imp_tem_model_answer"), LangLabel.getValue(lang, "wb_imp_tem_score"),
					LangLabel.getValue(lang, "wb_imp_tem_difficulty"), LangLabel.getValue(lang, "wb_imp_tem_status"),
					LangLabel.getValue(lang, "wb_imp_tem_description")
					//, LangLabel.getValue(lang, "wb_imp_tem_submit_file")
					};
			file = new File(webRootDir, this.QUE_ES2_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, title, null,null);
		}
	}

	// que fb
	private void genImpQueFBFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_resource_id"),
					LangLabel.getValue(lang, "wb_imp_tem_title"), LangLabel.getValue(lang, "wb_imp_tem_question"),
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_answer_1"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_1"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_2"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_2"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_3"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_3"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_4"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_4"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_5"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_5"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_6"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_6"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_7"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_7"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_8"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_8"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_9"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_9"), LangLabel.getValue(lang, "wb_imp_tem_blank_answer_10"),
					LangLabel.getValue(lang, "wb_imp_tem_blank_score_10"), LangLabel.getValue(lang, "wb_imp_tem_difficulty"),
					LangLabel.getValue(lang, "wb_imp_tem_status"), LangLabel.getValue(lang, "wb_imp_tem_description") };
			file = new File(webRootDir, this.QUE_FB_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			/*String[] mtExample = new String[] {"1", "",
					"新增填空题示例", "孔雀 的归属类别是[blank1]",
					"鸟类","2", "","", "","", "","", "","",
				    "","", "","", "","", "","", "","", "3",
					"OFF"};*/
			String[] mtExample = new String[] {};
			createXLSFile(file, title, null,mtExample);
		}
	}

	// que fsc
	private void genImpQueFSCFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_resource_id"),
					LangLabel.getValue(lang, "wb_imp_tem_title"), LangLabel.getValue(lang, "wb_imp_tem_question"),
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"), 
					LangLabel.getValue(lang, "wb_imp_tem_difficulty"), LangLabel.getValue(lang, "wb_imp_tem_status"),
					LangLabel.getValue(lang, "wb_imp_tem_description"), LangLabel.getValue(lang, "wb_imp_tem_option_1"),
					LangLabel.getValue(lang, "wb_imp_tem_option_2"), LangLabel.getValue(lang, "wb_imp_tem_option_3"),
					LangLabel.getValue(lang, "wb_imp_tem_option_4"), LangLabel.getValue(lang, "wb_imp_tem_option_5"),
					LangLabel.getValue(lang, "wb_imp_tem_option_6"), LangLabel.getValue(lang, "wb_imp_tem_option_7"),
					LangLabel.getValue(lang, "wb_imp_tem_option_8"), LangLabel.getValue(lang, "wb_imp_tem_option_9"),
					LangLabel.getValue(lang, "wb_imp_tem_option_10"), LangLabel.getValue(lang, "wb_imp_tem_shuffle_option"),
					LangLabel.getValue(lang, "wb_imp_tem_answer"), LangLabel.getValue(lang, "wb_imp_tem_score") };
			file = new File(webRootDir, this.QUE_FSC_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, title, "Data",null);
		}
	}

	// que mc
	private void genImpQueMCFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_resource_id"),
					LangLabel.getValue(lang, "wb_imp_tem_title"), LangLabel.getValue(lang, "wb_imp_tem_question"),
					LangLabel.getValue(lang, "wb_imp_tem_shuffle"), LangLabel.getValue(lang, "wb_imp_tem_answer"),
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"),
					LangLabel.getValue(lang, "wb_imp_tem_option_1"),
					LangLabel.getValue(lang, "wb_imp_tem_option_2"), LangLabel.getValue(lang, "wb_imp_tem_option_3"),
					LangLabel.getValue(lang, "wb_imp_tem_option_4"), LangLabel.getValue(lang, "wb_imp_tem_option_5"),
					LangLabel.getValue(lang, "wb_imp_tem_option_6"), LangLabel.getValue(lang, "wb_imp_tem_option_7"),
					LangLabel.getValue(lang, "wb_imp_tem_option_8"), LangLabel.getValue(lang, "wb_imp_tem_option_9"),
					LangLabel.getValue(lang, "wb_imp_tem_option_10"), LangLabel.getValue(lang, "wb_imp_tem_score"),
					LangLabel.getValue(lang, "wb_imp_tem_difficulty"), LangLabel.getValue(lang, "wb_imp_tem_status")};
			file = new File(webRootDir, this.QUE_MC_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			/*String[] esExample = new String[] {"1", "",
					"新增选择题示例", "燕窝中最珍贵的是？",
					"N", "3",
					"官燕", "毛燕",
					"血燕", "",
					"", "","", "","", "","2", "3",
					"OFF"};*/
			String[] esExample = new String[] {};
			createXLSFile(file, title, null,esExample);	
		}
	}

	// que mt
	private void genImpQueMTFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_resource_id"),
					LangLabel.getValue(lang, "wb_imp_tem_title"), LangLabel.getValue(lang, "wb_imp_tem_question"),
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"), 
					LangLabel.getValue(lang, "wb_imp_tem_scource_1"),
					LangLabel.getValue(lang, "wb_imp_tem_scource_2"), LangLabel.getValue(lang, "wb_imp_tem_scource_3"),
					LangLabel.getValue(lang, "wb_imp_tem_scource_4"), LangLabel.getValue(lang, "wb_imp_tem_scource_5"),
					LangLabel.getValue(lang, "wb_imp_tem_scource_6"), LangLabel.getValue(lang, "wb_imp_tem_scource_7"),
					LangLabel.getValue(lang, "wb_imp_tem_scource_8"), LangLabel.getValue(lang, "wb_imp_tem_scource_9"),
					LangLabel.getValue(lang, "wb_imp_tem_scource_10"), LangLabel.getValue(lang, "wb_imp_tem_target_1"),
					LangLabel.getValue(lang, "wb_imp_tem_target_2"), LangLabel.getValue(lang, "wb_imp_tem_target_3"),
					LangLabel.getValue(lang, "wb_imp_tem_target_4"), LangLabel.getValue(lang, "wb_imp_tem_target_5"),
					LangLabel.getValue(lang, "wb_imp_tem_target_6"), LangLabel.getValue(lang, "wb_imp_tem_target_7"),
					LangLabel.getValue(lang, "wb_imp_tem_target_8"), LangLabel.getValue(lang, "wb_imp_tem_target_9"),
					LangLabel.getValue(lang, "wb_imp_tem_target_10"), LangLabel.getValue(lang, "wb_imp_tem_answers"),
					LangLabel.getValue(lang, "wb_imp_tem_scores"), LangLabel.getValue(lang, "wb_imp_tem_difficulty"),
					LangLabel.getValue(lang, "wb_imp_tem_status") /*LangLabel.getValue(lang, "wb_imp_tem_description"),*/ };
			file = new File(webRootDir, this.QUE_MT_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			/*String[] mtExample = new String[] {"1", "",
					"新增配对题示例", "请找出下列动物的归属类别",
					"家蝇 ", "孔雀 ", "大鲵","", "","", "","", "","",
					"鸟类","两栖类", "昆虫","", "","", "","", "","",
				     "3,1,2","2,1,3", "3",
					"OFF"};*/
			String[] mtExample = new String[] {};
			createXLSFile(file, title, null,mtExample);
		}
	}

	// que tf
	private void genImpQueTFFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tem_folder_id"), LangLabel.getValue(lang, "wb_imp_tem_resource_id"),
					LangLabel.getValue(lang, "wb_imp_tem_title"), LangLabel.getValue(lang, "wb_imp_tem_question"),
					//LangLabel.getValue(lang, "wb_imp_tem_as_html"), 
					LangLabel.getValue(lang, "wb_imp_tem_answer"),
					LangLabel.getValue(lang, "wb_imp_tem_score"), LangLabel.getValue(lang, "wb_imp_tem_difficulty"),
					LangLabel.getValue(lang, "wb_imp_tem_status") /*LangLabel.getValue(lang, "wb_imp_tem_description"),*/ };
			file = new File(webRootDir, this.QUE_TF_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			/*String[] tfExample = new String[] {"1", "",
					"新增判断题示例", "大鲵属于两栖类？",
					"TRUE ",  "3", "3",
					"OFF"};*/
			String[] tfExample = new String[] {};
			createXLSFile(file, title, null,tfExample);
		}
	}
	
	// credit
	private void genImpCreditFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "lab_user_id"), LangLabel.getValue(lang, "820"),
					LangLabel.getValue(lang, "854"), LangLabel.getValue(lang, "lab_score") };
			file = new File(webRootDir, this.CREDITS_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, title, null,null);
		}
	}

	// create xls file
	protected void createXLSFile(File file, String[] title, String sheetName ,String[] fakeData) throws Exception {
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet((sheetName == null || sheetName.equals("")) ? "Sheet1" : sheetName, 0);
			if (title != null && title.length > 0) {
				for (int i = 0; i < title.length; i++) {
					String value = title[i];
					WritableFont font = new WritableFont(WritableFont.ARIAL, 9);
					font.setColour(Colour.BLACK);
					WritableCellFormat format = new WritableCellFormat(font);  //NumberFormats.TEXT   font
					format.setAlignment(Alignment.CENTRE);
					format.setBackground(Colour.GRAY_25);
					format.setBorder(Border.ALL, BorderLineStyle.THIN);
					format.setWrap(true);
					format.setShrinkToFit(true);
					Label label = new Label(i, 0, value, format);
					sheet.addCell(label);
					WritableCellFormat  wcf = new WritableCellFormat(NumberFormats.TEXT); 
					CellView cv = new CellView();
					cv.setFormat(wcf);
					cv.setSize(15*200);
					sheet.setColumnView(i,cv);
				}
			}
			if(null!=fakeData && fakeData.length!=0){
				for (int i = 0; i < fakeData.length; i++) {
					
					String value = fakeData[i];
					WritableFont font = new WritableFont(WritableFont.ARIAL, 9);
					font.setColour(Colour.BLACK);
					WritableCellFormat format = new WritableCellFormat(font);  //NumberFormats.TEXT   font
					format.setAlignment(Alignment.CENTRE);
					//format.setBackground(Colour.GRAY_25);
					//format.setBorder(Border.ALL, BorderLineStyle.THIN);
					format.setWrap(true);
					format.setShrinkToFit(true);
					
					Label label = new Label(i, 1, value, format);
					sheet.addCell(label);
					WritableCellFormat  wcf = new WritableCellFormat(NumberFormats.TEXT); 
					CellView cv = new CellView();
					cv.setFormat(wcf);
					cv.setSize(15*200);
					sheet.setColumnView(i,cv);
				}
			}
			workbook.write();
		} catch (Exception e) {
			throw e;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (WriteException e) {
					CommonLog.error(e.getMessage(),e);
				} catch (IOException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}

	protected static String getNodeAttr(Node node, String name) {
		String value = null;
		if (node.getNodeType() == Element.ELEMENT_NODE) {
			NamedNodeMap attrs = node.getAttributes();
			for (int j = 0; j < attrs.getLength(); j++) {
				Node attr = attrs.item(j);
				if (attr.getNodeName().equals(name)) {
					value = attr.getNodeValue();
				}
			}
		}
		return value;
	}

	public static String getLabelValue(String key, String lang, String defaultValue) {
		String value = LangLabel.getValue(lang, key);
		if (value == null || value.equals("")) {
			return defaultValue;
		}
		return value;
	}

	// user label
	public static String getUserLabel(String key, String lang, String site, String defaultValue) {
		String value = defaultValue;
		Map map = (Map) (ImportTemplate.userProf.get(site));
		if (map != null && map.containsKey(key)) {
			Map label = (Map) map.get(key);
			if (label != null && label.containsKey(lang)) {
				value = (String) label.get(lang);
				value = ((value == null) || value.equals("")) ? defaultValue : value;
			}
		}	
		return value;
	
	}

	public static boolean getActiveLabel(String key, String site, boolean defaultValue) {
		boolean value = defaultValue;

		Map map = (Map) (ImportTemplate.userProfVisiable.get(site));
		if (map != null) {
			String active = (String) map.get(key);
			if (active != null && active.equals("false")) {
				return false;
			}
		}
		return value;
	}

	public static String userProfToXML(String site) {
		String xml = "<user_label>";
		for (int i = 0; i < LANGS.length; i++) {
			String lang = LANGS[i];
			String[] labels = ImportTemplate.getUserLabelArray(lang, site);
			boolean[] activeArray = ImportTemplate.getActiveLabelArray(site);
			xml += "<" + lang + ">";
			for (int k = 0; k < labels.length; k++) {
				boolean active = activeArray[k];
				xml += "<field id=\"" + k + "\" active=\"" + active + "\">" + cwUtils.esc4XML(labels[k]) + "</field>";
			}
			xml += "</" + lang + ">";
		}
		xml += "</user_label>";

		xml += "<field_min_length>";
		Map minLengthMap = (Map) ImportTemplate.minLength.get(site);
		if ((minLengthMap != null) && (minLengthMap.size() > 0)) {
			Set keys = minLengthMap.keySet();
			if (keys != null && keys.size() > 0) {
				Iterator it = keys.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = (String) minLengthMap.get(key);
					xml += "<field name=\"" + key + "\">" + value + "</field>";
				}
			}
		}
		xml += "</field_min_length>";

		xml += "<field_max_length>";
		Map lengthMap = (Map) ImportTemplate.maxLength.get(site);
		if ((lengthMap != null) && (lengthMap.size() > 0)) {
			Set keys = lengthMap.keySet();
			if (keys != null && keys.size() > 0) {
				Iterator it = keys.iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = (String) lengthMap.get(key);
					xml += "<field name=\"" + key + "\">" + value + "</field>";
				}
			}
		}
		xml += "</field_max_length>";

		return xml;
	}

	public static String labelToXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<label>");
		for (int i = 0; i < LANGS.length; i++) {
			String lang = LANGS[i];
			sb.append("<").append(lang).append(">");
			if (LangLabel.allLangLabel != null) {
			    HashMap labels = (HashMap) LangLabel.allLangLabel.get(lang);
				if (labels != null) {
					Set keys = labels.keySet();
					Iterator it = keys.iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						if (key.startsWith(ImportTemplate.LABEL_PREFIX)) {
							String value = LangLabel.getValue(lang, key);
							sb.append("<field name=\"").append(key).append("\">").append(value).append("</field>");
						}
					}
				}
			}
			sb.append("</").append(lang).append(">");
		}
		sb.append("</label>");
		return sb.toString();
	}
	
	private void genImpPlanFile(File webRootDir) throws Exception {
		for (int i = 0; i < ImportTemplate.LANGS.length; i++) {
			File file = null;
			String lang = ImportTemplate.LANGS[i];
			String[] title = new String[] { LangLabel.getValue(lang, "wb_imp_tp_plan_date"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_name"), LangLabel.getValue(lang, "wb_imp_tp_plan_type"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_tnd"), LangLabel.getValue(lang, "wb_imp_tp_plan_intr"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_aim"), LangLabel.getValue(lang, "wb_imp_tp_plan_target"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_responser"), LangLabel.getValue(lang, "wb_imp_tp_plan_duration"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_ftf_start_time"), LangLabel.getValue(lang, "wb_imp_tp_plan_ftf_end_time"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_wb_start_time"), LangLabel.getValue(lang, "wb_imp_tp_plan_wb_end_time"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_lrn_count"), LangLabel.getValue(lang, "wb_imp_tp_plan_fee"),
					LangLabel.getValue(lang, "wb_imp_tp_plan_remark") };
			file = new File(webRootDir, this.TP_PLAN_TEMPLATE_FILE_PREFIX + "-" + lang + this.TEMPLATENAMEPSUBFIX);
			createXLSFile(file, title, null,null);
		}
	}
	
	
	//生成导入用户模板说明
	public static String templateDescription(String site){
		StringBuilder xml=new StringBuilder();
		
		xml.append("<import_label>");
		for (int i = 0; i < LANGS.length; i++) {
			String lang = LANGS[i];
			xml.append("<"+lang+">");
			for(String label:importTitle){
				String minlength="";
				String maxlength="";
				String allowEmprty="";
				String type="文本";
				String format="";
				String remarks="备注";
				Map min=(Map)minLength.get(site);
				if(min.get(label)==null||min.get(label).equals("")){
					minlength="0";
				}else{
					minlength=min.get(label).toString();
				}
				if(dbMaxLength.get(label)!=null&&!dbMaxLength.get(label).equals("")){
					maxlength=dbMaxLength.get(label).toString();
				}else{
					maxlength="80";
				}
				if(allowEmpty.get(label)){
					allowEmprty="0";
				}else{
					allowEmprty="1";
				}
				if (label.equals("group_code_level1")) {
					allowEmprty="1";
				}
				if (label.equals("group_title_level1")) {
					allowEmprty="1";
				}
				if (label.equals("grade_code_level1")) {
					allowEmprty="0";
				}
				if (label.equals("grade_title_level1")) {
					allowEmprty="0";
				}
				if(label.equals("password")){
					allowEmprty="0";
				}
				if(label.equals(ImportTemplate.getUserLabel(label, lang, site, label))){
					xml.append(labelToXml(label,LangLabel.getValue(lang, label),allowEmprty,minlength,maxlength,type,format,remarks));
				}else{
					xml.append(labelToXml(label,ImportTemplate.getUserLabel(label, lang, site, label),allowEmprty,minlength,maxlength,type,format,remarks));
				}
			}
			xml.append("</"+lang+">");
		}
		xml.append("</import_label>");
		return xml.toString();
	}
	
	
	private static String labelToXml(String id,String name,String allowEmpty,String minlength,String maxlength,String type,String format,String remarks){
		StringBuilder xml=new StringBuilder();
		String colon="\"";
		xml.append("<label name="+colon+name+colon+" ");
		xml.append("allowEmpty="+colon+allowEmpty+colon+" ");
		xml.append("id="+colon+id+colon+" ");
		xml.append("minlength="+colon+minlength+colon+" ");
		xml.append("maxlength="+colon+maxlength+colon+" ");
		xml.append("type="+colon+type+colon+" ");
		xml.append("format="+colon+format+colon+" ");
		xml.append("remarks="+colon+remarks+colon+" ");
		xml.append("></label>");
		return xml.toString();
	}
}

