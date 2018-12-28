package com.cw.wizbank.JsonMod.tcrTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class TcrTemplateLogic {
	private static TcrTemplateLogic tcrTemplateLogic = null;
	private final String tcr_module = "tcr_module";
	private final String tcr_module_left = "tcr_module_left";
	private final String tcr_module_centre = "tcr_module_centre";
	private final String tcr_module_right = "tcr_module_right";
	private final String tm_title_gb = "tm_title_gb";
	private final String tm_title_ch = "tm_title_ch";
	private final String tm_title_en = "tm_title_en";
	private final String tm_code = "tm_code";
	private final String left = "LEFT";
	private final String centre = "CENTRE";
	private final String right = "RIGHT";
	private final String deleted = "DELETED";
	private final String unAdded = "UNADDED";
	private final String added = "ADDED";
	private final String gb = "zh-cn";
	private final String ch = "zh-hk";
	private final String en = "en-us";
	private final String tcrModuleId = "tcrModuleId_";
	private final String x = "_x";
	private final String y = "_y";
	private final String template_infor = "template_infor";
	private final String QQ_CONSULTATION = "QQ_CONSULTATION";
	
	private final SAXReader saxReader = new SAXReader();
	private final Logger logger = Log4jFactory.createLogger(com.cw.wizbank.JsonMod.tcrTemplate.TcrTemplateLogic.class);
	private final SqlMapClientDataSource sqlMapCient = SQLMapClientFactory.getSqlMapClient();
	
	private static List left_tcrModule_id_lst = null;
	private static List centre_tcrModule_id_lst = null;
	private static List right_tcrModule_id_lst = null;
	
	private final String init_tcr_module_sql = "update TcrModule set tm_title_gb = ?, tm_title_ch = ?, tm_title_en = ?, tm_is_centre = ? where tm_code = ?";
	
	private final String get_all_tcr_without_template_sql = "select tcr_id from tcTrainingCenter where not exists (select tt_id from TcrTemplate where tt_tcr_id = tcr_id)";
	
	private final String inser_tcm_template_sql = "insert into TcrTemplate (tt_tcr_id, tt_create_timestamp, tt_create_usr_id, tt_update_timestamp, tt_update_usr_id) values (?, ?, ?, ?, ?)";
	
	private final String insert_tcr_template_module_sql = "insert into TcrTemplateModule (ttm_tm_id, ttm_tt_id, ttm_mod_x_value, ttm_mod_y_value, ttm_mod_status) values (?, ?, ?, ?, ?)";
	private final String get_qq_tm_sql = "select tm_id from tcrModule where tm_code=? ";
	
	private final String get_template_module_sql(String curLang){
		String start = "select ttm_id, ttm_tt_id, ttm_tm_id, ttm_mod_x_value, ttm_mod_y_value, tm_code,";
		String end = " from TcrTemplateModule inner join TcrModule on (ttm_tm_id = tm_id) where ttm_tt_id = ? and ttm_mod_status = ? and ttm_mod_x_value in (#) order by ttm_mod_y_value asc";
		String middle = " ";
		if(curLang.equals(gb)){
			middle = " tm_title_gb tm_title";
		}else if(curLang.equals(ch)){
			middle = " tm_title_ch tm_title";
		}else if(curLang.equals(en)){
			middle = " tm_title_en tm_title";
		}
		return start + middle + end;
	}
	
	private final String save_template_module_sql = "update TcrTemplateModule set ttm_mod_status = ?, ttm_mod_x_value = ?, ttm_mod_y_value = ? where ttm_tt_id = ? and ttm_tm_id = ?";

	
	private TcrTemplateLogic (){}
	
	public static TcrTemplateLogic getInstance(){
		if(tcrTemplateLogic == null){
			tcrTemplateLogic = new TcrTemplateLogic();
		}
		return tcrTemplateLogic;
	}
	
	public void init_all(Connection connection, String init_tcr_module_config_path){
		try {
			initModule(connection, init_tcr_module_config_path);
			initTcrTemplate(connection);
			add_new_module(connection);
		} catch (Exception e) {
			logger.error("init tcr template error.\n", e);
			throw new RuntimeException("init tcr template error.\n");
		}
	}
	
	/*
	 * 初始化模块
	 */
	private void initModule(Connection connection, String init_tcr_module_config_path)throws Exception{
		List params = new ArrayList();
		//Document document = saxReader.read(new InputStreamReader(new FileInputStream(new File(init_tcr_module_config_path)), "UTF-8"));
		Document document = saxReader.read(cwUtils.openUTF8FileStream(new File(init_tcr_module_config_path)));
		
		List tcr_module_left_elements = document.getRootElement().element(tcr_module_left).elements(tcr_module);
		List tcr_module_left_code_lst = new ArrayList();
		for (Iterator iterator = tcr_module_left_elements.iterator(); iterator.hasNext();) {
			Element element = (Element)iterator.next();
			params.add(new Object[]{
				element.attributeValue(tm_title_gb), element.attributeValue(tm_title_ch), element.attributeValue(tm_title_en),
				new Long(0), element.attributeValue(tm_code)});
			tcr_module_left_code_lst.add(element.attributeValue(tm_code));
		}
		List tcr_module_centre_elements = document.getRootElement().element(tcr_module_centre).elements(tcr_module);
		List tcr_module_centre_code_lst = new ArrayList();
		for (Iterator iterator = tcr_module_centre_elements.iterator(); iterator.hasNext();) {
			Element element = (Element)iterator.next();
			params.add(new Object[]{
				element.attributeValue(tm_title_gb), element.attributeValue(tm_title_ch), element.attributeValue(tm_title_en),
				new Long(1), element.attributeValue(tm_code)});
			tcr_module_centre_code_lst.add(element.attributeValue(tm_code));
		}
		List tcr_module_right_elements = document.getRootElement().element(tcr_module_right).elements(tcr_module);
		List tcr_module_right_code_lst = new ArrayList();
		for (Iterator iterator = tcr_module_right_elements.iterator(); iterator.hasNext();) {
			Element element = (Element)iterator.next();
			params.add(new Object[]{
				element.attributeValue(tm_title_gb), element.attributeValue(tm_title_ch), element.attributeValue(tm_title_en),
				new Long(0), element.attributeValue(tm_code)});
			tcr_module_right_code_lst.add(element.attributeValue(tm_code));
		}
		
		sqlMapCient.executeBatchUpdate(connection, init_tcr_module_sql, params);
		
		left_tcrModule_id_lst = sqlMapCient.getsingleColumnList(connection, "select tm_id from TcrModule where tm_code in (#) order by tm_id asc", 
				new Object[]{tcr_module_left_code_lst}, Long.class);
		centre_tcrModule_id_lst = sqlMapCient.getsingleColumnList(connection, "select tm_id from TcrModule where tm_code in (#) order by tm_id asc", 
				new Object[]{tcr_module_centre_code_lst}, Long.class);
		right_tcrModule_id_lst = sqlMapCient.getsingleColumnList(connection, "select tm_id from TcrModule where tm_code in (#) order by tm_id asc", 
				new Object[]{tcr_module_right_code_lst}, Long.class);
	}
	
	/*
	 * 初始化模板（为没有模板的培训中心建立模板）
	 */
	public void initTcrTemplate(Connection connection) throws SQLException{
		List tcr_id_lst = sqlMapCient.getsingleColumnList(connection, get_all_tcr_without_template_sql, new Object[]{}, Long.class);
		if(!tcr_id_lst.isEmpty()){
			Timestamp curTime = cwSQL.getTime(connection);
			/*List QQ_tm_id_list = sqlMapCient.getsingleColumnList(connection, get_qq_tm_sql, new Object[]{QQ_CONSULTATION}, Long.class);
			Long QQ_tm_id = (Long) QQ_tm_id_list.get(0);
			*/for (Iterator iterator = tcr_id_lst.iterator(); iterator.hasNext();) {
				Long tcr_id = (Long) iterator.next();
				//创建培训中心下的模板
				long tt_id = sqlMapCient.executeSave(connection, inser_tcm_template_sql,
						new Object[]{tcr_id, curTime, "slu3", curTime, "slu3"}, "TcrTemplate", "tt_id");
				//创建该培训中心下的模板模块
				List tempModuleParams = new ArrayList();
				for (int ln=0; ln<left_tcrModule_id_lst.size(); ln++) {
					Long tm_id = (Long) left_tcrModule_id_lst.get(ln);
					tempModuleParams.add(new Object[]{tm_id, new Long(tt_id), new Integer(0), new Integer(ln), new Integer(1)});
				}
				for(int cn=0; cn<centre_tcrModule_id_lst.size(); cn++){
					Long tm_id = (Long) centre_tcrModule_id_lst.get(cn);
					tempModuleParams.add(new Object[]{tm_id, new Long(tt_id), new Integer(1), new Integer(cn), new Integer(1)});
				}
				for(int rn=0; rn<right_tcrModule_id_lst.size(); rn++){
					Long tm_id = (Long)right_tcrModule_id_lst.get(rn);
					/*if(tm_id.equals(QQ_tm_id)){
						tempModuleParams.add(new Object[]{tm_id, new Long(tt_id), new Integer(2), new Integer(rn), new Integer(0)});
					}else{
						tempModuleParams.add(new Object[]{tm_id, new Long(tt_id), new Integer(2), new Integer(rn), new Integer(1)});
					}*/
					tempModuleParams.add(new Object[]{tm_id, new Long(tt_id), new Integer(2), new Integer(rn), new Integer(1)});
					
				}
				sqlMapCient.executeBatchUpdate(connection, insert_tcr_template_module_sql, tempModuleParams);
			}
		}
	}
	
	/*
	 * 添加新模块(为拥有模板的培训中心添加新模块)
	 */
	private void add_new_module(Connection connection){
		List unadded_tm_lst = sqlMapCient.getsingleColumnList(connection, "select tm_id from TcrModule inner join AdditivedTcrModule on (atm_tm_code = tm_code) where atm_added_ind = ? order by tm_id asc",
				new Object[]{unAdded}, Long.class);
		if(!unadded_tm_lst.isEmpty()){
			List tt_id_lst = sqlMapCient.getsingleColumnList(connection, "select tt_id from TcrTemplate where not exists (select ttm_id from Tcrtemplatemodule where ttm_tt_id = tt_id and ttm_tm_id in (#)) order by tt_id asc", 
					new Object[]{unadded_tm_lst}, Long.class);
			if(!tt_id_lst.isEmpty()){
				List tempModuleParams = new ArrayList();
				Map ttm_max_y_val_map = new HashMap();
				List ttm_max_y_vlaue_lst = sqlMapCient.getObjectList(connection, "select ttm_tt_id, ttm_mod_x_value, max(ttm_mod_y_value) ttm_max_y_value from Tcrtemplatemodule where ttm_tt_id in (#) group by ttm_tt_id, ttm_mod_x_value order by ttm_tt_id, ttm_mod_x_value asc", 
						new Object[]{tt_id_lst});
				for (Iterator iterator = ttm_max_y_vlaue_lst.iterator(); iterator.hasNext();) {
					Map ttmMap = (Map) iterator.next();
					ttm_max_y_val_map.put(String.valueOf(ttmMap.get("ttm_tt_id")) + "_" + String.valueOf(ttmMap.get("ttm_mod_x_value")), 
							new Long(String.valueOf(ttmMap.get("ttm_max_y_value"))));
				}
				for (Iterator iterator = tt_id_lst.iterator(); iterator.hasNext();) {
					Long tt_id = (Long) iterator.next();
					long max_left_y_value = ((Long)ttm_max_y_val_map.get(tt_id.longValue() + "_0")).longValue();
					long max_centre_y_value = ((Long)ttm_max_y_val_map.get(tt_id.longValue() + "_1")).longValue();
					long max_right_y_value = ((Long)ttm_max_y_val_map.get(tt_id.longValue() + "_2")).longValue();
					for (Iterator iterator2 = unadded_tm_lst.iterator(); iterator2.hasNext();) {
						Long tm_id = (Long) iterator2.next();
						Long atm_x_mod_value = new Long(0);
						Long atm_y_mod_value = new Long(0);
						if(left_tcrModule_id_lst.contains(tm_id)){
							atm_x_mod_value = new Long(0);
							atm_y_mod_value = new Long(++ max_left_y_value);
						}else if(centre_tcrModule_id_lst.contains(tm_id)){
							atm_x_mod_value = new Long(1);
							atm_y_mod_value = new Long(++ max_centre_y_value);
						}else if(right_tcrModule_id_lst.contains(tm_id)){
							atm_x_mod_value = new Long(2);
							atm_y_mod_value = new Long(++ max_right_y_value);
						}
						tempModuleParams.add(new Object[]{tm_id, tt_id, atm_x_mod_value, atm_y_mod_value, new Long(0)});
					}
				}
				sqlMapCient.executeBatchUpdate(connection, insert_tcr_template_module_sql, tempModuleParams);
			}
			sqlMapCient.executeUpdate(connection, "update AdditivedTcrModule set atm_added_ind = ? where atm_added_ind = ?", new Object[]{added, unAdded});
		}
	}
	/*
	 * 删除培训中心所对应的模板
	 */
	public void delete_tcr_template(Connection con, long tcr_id, final String HAS_RESOURCE_MSG) throws cwSysMessage{
		List dpt_id_lst = sqlMapCient.getsingleColumnList(con, "select dpt_id from DefinedProject where dpt_tcr_id = ?", new Object[]{new Long(tcr_id)}, Long.class);
		if(!dpt_id_lst.isEmpty()){
			throw new cwSysMessage(HAS_RESOURCE_MSG);
		}
		
		//删除模板模块
		Long tt_id = (Long) sqlMapCient.getsingleColumn(con, "select tt_id from TcrTemplate where tt_tcr_id = ?", new Object[]{new Long(tcr_id)}, Long.class);
		sqlMapCient.executeUpdate(con, "delete from TcrTemplateModule where ttm_tt_id = ?", new Object[]{tt_id});
		sqlMapCient.executeUpdate(con, "delete from TcrTemplate where tt_id = ?", new Object[]{tt_id});
		
	}
	
	/*
	 * 获取模板对应的模板模块列表
	 */
	public String getTcr_template_module_pre_xml_data(Connection con, Long tt_id, String cur_lang){
		StringBuffer xmlData = new StringBuffer();
		List leftModule = sqlMapCient.getObjectList(con, get_template_module_sql(cur_lang), new Object[]{tt_id, new Integer(1), new Object[]{new Integer(0)}});
		List centreModule = sqlMapCient.getObjectList(con, get_template_module_sql(cur_lang), new Object[]{tt_id, new Integer(1), new Object[]{new Integer(1)}});
		List rightModule =  sqlMapCient.getObjectList(con, get_template_module_sql(cur_lang), new Object[]{tt_id, new Integer(1), new Object[]{new Integer(2)}});
		List deletedModue = sqlMapCient.getObjectList(con, get_template_module_sql(cur_lang), new Object[]{tt_id, new Integer(0), new Object[]{new Integer(0), new Integer(1), new Integer(2)}});
		
		xmlData.append(sqlMapCient.getXmlDataFromObjectList(leftModule, left, tcr_module));
		xmlData.append(sqlMapCient.getXmlDataFromObjectList(centreModule, centre, tcr_module));
		xmlData.append(sqlMapCient.getXmlDataFromObjectList(rightModule, right, tcr_module));
		xmlData.append(sqlMapCient.getXmlDataFromObjectList(deletedModue, deleted, tcr_module));
		
		return xmlData.toString();
	}
	
	/*
	 * 保存调整好的模板模块
	 */
	public void saveTcrTemplate(Connection con, TcrTemplateParam tcrTemplateParam, HttpServletRequest request){
		Long tt_id = new Long(tcrTemplateParam.getTt_id());
		sqlMapCient.executeUpdate(con, "update TcrTemplateModule set ttm_mod_status = 0 where ttm_tt_id = ?", new Object[]{tt_id});
		
		List paramLst = new ArrayList();
		Enumeration enumeration = request.getParameterNames();
		while(enumeration.hasMoreElements()){
			String paramName = (String) enumeration.nextElement();
			if(paramName.startsWith(tcrModuleId)){
				Long tm_id = Long.valueOf(request.getParameter(paramName));
				Integer tm_x_value = Integer.valueOf(request.getParameter(tm_id + x));
				Integer tm_y_value = Integer.valueOf(request.getParameter(tm_id + y));
				paramLst.add(new Object[]{new Integer(1), tm_x_value, tm_y_value, tt_id, tm_id});
			}
		}
		sqlMapCient.executeBatchUpdate(con, save_template_module_sql, paramLst);
		sqlMapCient.executeUpdate(con, "update TcrTemplate set tt_dis_fun_navigation_ind = ? where tt_id = ?", 
				new Object[]{new Integer(tcrTemplateParam.getDis_fun_ind()), tt_id});
	}
	
	
	/*
	 * 预览调整好的模板模块
	 */
	public void getPrevewTableName(Connection con, TcrTemplateParam tcrTemplateParam, HttpServletRequest request) throws SQLException{
		boolean createTempTableSuccessfully = false;
		String temptableName = null;
		try {
			List paramLst = new ArrayList();
			Enumeration enumeration = request.getParameterNames();
			while(enumeration.hasMoreElements()){
				String paramName = (String) enumeration.nextElement();
				if(paramName.startsWith(tcrModuleId)){
					Long tm_id = Long.valueOf(request.getParameter(paramName));
					String tm_code = paramName.substring(tcrModuleId.length());
					Integer tm_x_value = Integer.valueOf(request.getParameter(tm_id + x));
					Integer tm_y_value = Integer.valueOf(request.getParameter(tm_id + y));
					paramLst.add(new Object[]{tm_code, tm_x_value, tm_y_value});
				}
			}
			temptableName = "temp_" + cwSQL.getTime(con).getTime();
			Hashtable nameType = new Hashtable();
			nameType.put("tm_code", cwSQL.COL_TYPE_STRING);
			nameType.put("tm_x_value", cwSQL.COL_TYPE_INTEGER);
			nameType.put("tm_y_value", cwSQL.COL_TYPE_INTEGER);
			Hashtable nameLength = new Hashtable();
			nameLength.put("tm_code", "100");
			nameLength.put("tm_x_value", "0");
			nameLength.put("tm_y_value", "0");
			
			createTempTableSuccessfully = cwSQL.createTempTable(con, temptableName, nameType, nameLength);
			String insert_sql = "insert into " + temptableName + "(tm_code, tm_x_value, tm_y_value) values (?, ?, ?)";
			sqlMapCient.executeBatchUpdate(con, insert_sql, paramLst);
			
			//获取模块信息
			String get_pre_module_sql = "select tm_code from " + temptableName + " where tm_x_value = ? order by tm_y_value asc";
			List leftModuleLst = sqlMapCient.getsingleColumnList(con, get_pre_module_sql, new Object[]{new Integer(0)}, String.class);
			List centreModuleLst = sqlMapCient.getsingleColumnList(con, get_pre_module_sql, new Object[]{new Integer(1)}, String.class);
			List rightModuleLst = sqlMapCient.getsingleColumnList(con, get_pre_module_sql, new Object[]{new Integer(2)}, String.class);
			
			Map templateInfor = new HashMap();
			templateInfor.put(left, leftModuleLst);
			templateInfor.put(centre, centreModuleLst);
			templateInfor.put(right, rightModuleLst);
			templateInfor.put("dis_fun_ind", new Integer(tcrTemplateParam.getDis_fun_ind()));
			
			request.getSession().setAttribute(template_infor, templateInfor);
			
		} catch (SQLException e) {
			logger.error("preview training centre template unsuccessfully.\n", e);
			throw e;
		}finally{
			if(createTempTableSuccessfully){
				cwSQL.dropTempTable(con, temptableName);
			}
		}
		
	}
	
}
