package com.cw.wizbank.JsonMod.definedProject;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwSQL;


public class DefinedProjectLogic {
	private final Logger logger = Log4jFactory.createLogger(com.cw.wizbank.JsonMod.definedProject.DefinedProjectLogic.class);
	private final SqlMapClientDataSource sqlMapClient = SQLMapClientFactory.getSqlMapClient();
	private final cwTree tree = new cwTree();
	private final String defined_project = "DEFINED_PROJECT_";
	private final String left = "LEFT";
	private static DefinedProjectLogic definedProjectLogic = null;
	
	private final String addProjectSql = new StringBuffer()
	.append("insert into DefinedProject (dpt_tcr_id, dpt_code, dpt_title, dpt_status, dpt_way, dpt_create_timestamp, dpt_create_usr_id, dpt_update_timestamp, dpt_update_usr_id)")
	.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?)").toString();
	
	private final String updatreProjectSql = new StringBuffer()
	.append("update DefinedProject set dpt_tcr_id = ?, dpt_code = ?, dpt_title = ?, dpt_status = ?, dpt_way = ?, dpt_update_timestamp = ?, dpt_update_usr_id = ?")
	.append(" where dpt_id = ?").toString();
	
	private final String deleteProjectSql = "delete from DefinedProject where dpt_id = ?";
	
	private final String addLinkSql = new StringBuffer()
	.append("insert into projectLink (pjl_dpt_id, pjl_code, pjl_title, pjl_status, pjl_url, pjl_create_timestamp, pjl_create_usr_id, pjl_update_timestamp, pjl_update_usr_id)")
	.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?)").toString();
	
	private final String updateLinkSql = new StringBuffer()
	.append("update projectLink set pjl_code = ?, pjl_title = ?, pjl_status = ?, pjl_url = ?, pjl_update_timestamp = ?, pjl_update_usr_id = ?")
	.append(" where pjl_id = ?").toString();
	
	private final String deleteLinkSql = "delete from projectLink where pjl_id = ?";
	
	private final String inser_tcr_module_sql = "insert into TcrModule (tm_tcr_id, tm_code, tm_title_gb, tm_title_ch, tm_title_en, tm_is_centre) values (?, ?, ?, ?, ?, ?)";
	
	private final String select_temp_module_sql = "select ttm_tt_id tt_id, count(ttm_id) ttm_id_count from TcrTemplateModule inner join TcrTemplate on (ttm_tt_id = tt_id) inner join tcTrainingCenter on (tcr_id = tt_tcr_id) where tcr_id = ? and ttm_mod_x_value = ? group by ttm_tt_id";
	
	private final String insert_tcr_template_module_sql = "insert into TcrTemplateModule (ttm_tm_id, ttm_tt_id, ttm_mod_x_value, ttm_mod_y_value, ttm_mod_status) values (?, ?, ?, ?, ?)";
	
	private final String getTrainingMannagerProjectSql = new StringBuffer().append("select * from DefinedProject inner join")
	.append(" (select distinct tcr_id from tcTrainingCenter,tcRelation,tcTrainingCenterOfficer where tcr_status = ?")
	.append(" and tco_usr_ent_id = ? and tco_rol_ext_id = ? and ( tcr_id = tco_tcr_id or (tcr_id = tcn_child_tcr_id and")
	.append(" tcn_ancestor = tco_tcr_id))) mana_tcr_tab on (mana_tcr_tab.tcr_id = dpt_tcr_id)").toString();
	
	private final String defin_pro_infors_oracle_sql = "select tm_code, dpt_way, tt_id, tm_id from DefinedProject inner join TcrTemplate on (tt_tcr_id = dpt_tcr_id) inner join TcrModule on (('DEFINED_PROJECT_' || dpt_code) = tm_code) where dpt_id = ?";
	private final String defin_pro_infors_sqlServer_sql = "select tm_code, dpt_way, tt_id, tm_id from DefinedProject inner join TcrTemplate on (tt_tcr_id = dpt_tcr_id) inner join TcrModule on (('DEFINED_PROJECT_' + dpt_code) = tm_code) where dpt_id = ?";

	
	private DefinedProjectLogic (){}
	public static DefinedProjectLogic getInstance(){
		if(definedProjectLogic == null){
			definedProjectLogic = new DefinedProjectLogic();
		}
		return definedProjectLogic;
	}
	
	public String get_project_lst_xml_data(Connection con, DefinedProjectParam definedProjectParam, loginProfile prof, HttpServletRequest request){
		StringBuffer dataXml = new StringBuffer();
		try {
			long tcr_id = definedProjectParam.getTcr_id();
			Map atris = new HashMap();
			atris.put("sort_col", (definedProjectParam.getSort_col() == null || definedProjectParam.getSort_col().equals("") ? "dpt_code" : definedProjectParam.getSort_col()));
			String sort_order = definedProjectParam.getSort_order();
			atris.put("sort_order", (sort_order == null || sort_order.equals("") || sort_order.equals("desc")) ? "asc" : "desc");
			if (tcr_id < 1) {
				if(prof.current_role.startsWith(AccessControlWZB.ROL_STE_UID_ADM)){
					dataXml.append(sqlMapClient.getPaginationDataAdapterToxmlData(con, "select * from DefinedProject", new Object[]{}, "projectList", "project", request));
				}else{
					dataXml.append(sqlMapClient.getPaginationDataAdapterToxmlData(con, getTrainingMannagerProjectSql, 
							new Object[]{acSite.STATUS_OK, new Long(prof.usr_ent_id), AcObjective.TADM}, "projectList", "project", request));
				}
				atris.put("is_show_all", "true");
				atris.put("tcr_id", new Integer(0));
			} else {
				dataXml.append(sqlMapClient.getPaginationDataAdapterToxmlData(con, "select * from DefinedProject where dpt_tcr_id = ?", 
						new Object[]{new Long(tcr_id)}, "projectList", "project", request));
				atris.put("is_show_all", "false");
				atris.put("tcr_id", new Long(tcr_id));
				
			}
			
			dataXml.append(sqlMapClient.getXmlDataFromMap(atris, "tcrProj"));
			
			// 获取培训中心树形机构
			String nav_tree = tree.genNavTrainingCenterTree(con, prof, false);
			if (nav_tree != null) {
				dataXml.append(nav_tree);
			}

			if (tcr_id != 0) {
				dataXml.append(TrainingCenter.getTcrAsXml(con, tcr_id,
						prof.root_ent_id));
			}
			return dataXml.toString();
		} catch (Exception e) {
			logger.error("get project list xml data error.\n", e);
			throw new RuntimeException("get project list xml data error.\n");
		}
	}
	
	public String dis_oprating_project_pre(Connection con, DefinedProjectParam definedProjectParam, loginProfile prof){
		long tcr_id = definedProjectParam.getTcr_id();
		String oprating = definedProjectParam.getOprating();
		if(oprating.equals("ADD")){
			String tcr_infor = tcr_id > 0 ? sqlMapClient.getObjectAdapterToXmlData(con, "select tcr_id, tcr_title from tcTrainingCenter where tcr_id = ?", 
			new Object[]{new Long(tcr_id)}, "tcTrainingCenter") : sqlMapClient.getObjectAdapterToXmlData(con, 
					"select tcr_id, tcr_title from tcTrainingCenter inner join tcTrainingCenterOfficer on (tcr_status = ? and tco_tcr_id = tcr_id) where tco_usr_ent_id = ?", 
					new Object[]{acSite.STATUS_OK, new Long(prof.usr_ent_id)}, "tcTrainingCenter");
			return "<project></project>" + "<oprating>ADD</oprating>" + tcr_infor;
		}else if(oprating.equals("UPD")){
			String tcr_infor = sqlMapClient.getObjectAdapterToXmlData(con, "select tcr_id, tcr_title from tcTrainingCenter inner join DefinedProject on (dpt_tcr_id = tcr_id) where dpt_id = ?", 
					new Object[]{new Long(definedProjectParam.getDpt_id())}, "tcTrainingCenter");
			String dataXml = sqlMapClient.getObjectAdapterToXmlData(con, "select * from DefinedProject where dpt_id = ?", 
					new Object[]{new Long(definedProjectParam.getDpt_id())}, "project");
			return dataXml + "<oprating>UPD</oprating>" + tcr_infor;
		}else{
			return "";
		}
	}
	
	public void oprating_project_exe(Connection con, DefinedProjectParam definedProjectParam, loginProfile prof)throws Exception{
		String oprating = definedProjectParam.getOprating();
		Timestamp curDatetime = cwSQL.getTime(con);
		if(oprating.equals("ADD")){
			saveDefinedProjectModule(con, definedProjectParam);
			Object[] params = new Object[]{
					new Long(definedProjectParam.getTcr_id()), definedProjectParam.getDpt_code(), definedProjectParam.getDpt_title(),
					definedProjectParam.getDpt_status(), definedProjectParam.getDpt_way(), curDatetime, prof.usr_id, curDatetime, prof.usr_id
			};
			sqlMapClient.executeUpdate(con, addProjectSql, params);
		}else if(oprating.equals("UPD")){
			deletedeinedProjectModule(con, definedProjectParam);
			saveDefinedProjectModule(con, definedProjectParam);
			Object[] params = new Object[]{
				new Long(definedProjectParam.getTcr_id()), definedProjectParam.getDpt_code(), definedProjectParam.getDpt_title(),
				definedProjectParam.getDpt_status(), definedProjectParam.getDpt_way(), curDatetime, prof.usr_id, new Long(definedProjectParam.getDpt_id())	
			};
			sqlMapClient.executeUpdate(con, updatreProjectSql, params);
		}else if(oprating.equals("DEL")){
			deletedeinedProjectModule(con, definedProjectParam);
			sqlMapClient.executeUpdate(con, deleteProjectSql, new Object[]{new Long(definedProjectParam.getDpt_id())});
		}else{
			throw new RuntimeException("the oprating " + oprating + " is not defineded.");
		}
	}
	
	public String get_project_link_lst_xml_data(Connection con, DefinedProjectParam definedProjectParam, loginProfile prof, HttpServletRequest request){
		StringBuffer xmlData = new StringBuffer();
		
		long dpt_id = definedProjectParam.getDpt_id();
		long tcr_id = definedProjectParam.getTcr_id();
		xmlData.append("<infors dpt_id=\""+dpt_id+"\" tcr_id=\""+tcr_id+"\"></infors>");
		xmlData.append(sqlMapClient.getObjectListAdapterToxmlData(con, "select * from projectLink where pjl_dpt_id = ?",
				new Object[]{new Long(dpt_id)}, "linkLst", "link"));
		
		return xmlData.toString();
	}
	
	public String dis_oprating_link_pre(Connection con, DefinedProjectParam definedProjectParam, loginProfile prof){
		String oprating = definedProjectParam.getOprating();
		long dpt_id = definedProjectParam.getDpt_id();
		long pjl_id = definedProjectParam.getPjl_id();
		long tcr_id = definedProjectParam.getTcr_id();
		if(oprating.equals("ADD")){
			return "<link pjl_url = \"http://\"></link>" + "<oprating>ADD</oprating>" + "<project dpt_id = \""+ dpt_id +"\" tcr_id = \""+tcr_id+"\"></project>";
		}else if(oprating.equals("UPD")){
			return "<oprating>UPD</oprating>" + sqlMapClient.getObjectAdapterToXmlData(con, "select * from projectLink where pjl_id = ?",
					new Object[]{new Long(pjl_id)}, "link") + "<project dpt_id = \""+ dpt_id +"\" tcr_id = \""+tcr_id+"\"></project>";
		}else{
			return "";
		}
	}
	
	public void oprating_link_exe(Connection con, DefinedProjectParam definedProjectParam, loginProfile prof)throws Exception{
		String oprating = definedProjectParam.getOprating();
		Timestamp curDatetime = cwSQL.getTime(con);
		if(oprating.equals("ADD")){
			Object[] params = new Object[]{
				new Long(definedProjectParam.getDpt_id()), definedProjectParam.getPjl_code(), definedProjectParam.getPjl_title(),
				definedProjectParam.getPjl_status(), definedProjectParam.getPjl_url(), curDatetime, prof.usr_id, curDatetime, prof.usr_id
			};
			sqlMapClient.executeUpdate(con, addLinkSql, params);
		}else if(oprating.equals("UPD")){
			Object[] params = new Object[]{
				definedProjectParam.getPjl_code(), definedProjectParam.getPjl_title(), definedProjectParam.getPjl_status(),
				definedProjectParam.getPjl_url(), curDatetime, prof.usr_id, new Long(definedProjectParam.getPjl_id())
			};
			sqlMapClient.executeUpdate(con, updateLinkSql, params);
		}else if(oprating.equals("DEL")){
			sqlMapClient.executeUpdate(con, deleteLinkSql, new Object[]{new Long(definedProjectParam.getPjl_id())});
		}else{
			throw new RuntimeException("the oprating " + oprating + " is not defineded.");
		}
		
	}
	
	private void saveDefinedProjectModule(Connection con, DefinedProjectParam definedProjectParam){
		//插入自定义项目模块
		long tm_id = sqlMapClient.executeSave(con, inser_tcr_module_sql, new Object[]{new Long(definedProjectParam.getTcr_id()), 
		    (defined_project + definedProjectParam.getDpt_code()), definedProjectParam.getDpt_title(), definedProjectParam.getDpt_title(),
		    definedProjectParam.getDpt_title(), new Integer(0)}, "TcrModule", "tm_id");
		//插入自定义项目模板模块
		Integer mod_x_value = definedProjectParam.getDpt_way().equals(left) ? new Integer(0) : new Integer(2);
		Map templateInfors = sqlMapClient.getObject(con, select_temp_module_sql, new Object[]{new Long(definedProjectParam.getTcr_id()), mod_x_value});
		sqlMapClient.executeUpdate(con, insert_tcr_template_module_sql, 
				new Object[]{new Long(tm_id), Integer.valueOf(String.valueOf(templateInfors.get("tt_id"))), mod_x_value,
				new Integer(String.valueOf(templateInfors.get("ttm_id_count"))), new Integer(1)});
	}
	
	private void deletedeinedProjectModule(Connection con, DefinedProjectParam definedProjectParam){
		long dpt_id = definedProjectParam.getDpt_id();
		String dbproduct = cwSQL.getDbProductName();
		Map defin_pro_infors = sqlMapClient.getObject(con, dbproduct.indexOf(cwSQL.ProductName_ORACLE) >= 0 ? defin_pro_infors_oracle_sql : defin_pro_infors_sqlServer_sql,new Object[]{new Long(dpt_id)});
		String tm_code = String.valueOf(defin_pro_infors.get("tm_code"));
		Integer dpt_way = left.equals(String.valueOf(defin_pro_infors.get("dpt_way"))) ? new Integer(0) : new Integer(2);
		Long tt_id = Long.valueOf(String.valueOf(defin_pro_infors.get("tt_id")));
		Long tm_id = Long.valueOf(String.valueOf(defin_pro_infors.get("tm_id")));
		
		//删除模板模块 
		sqlMapClient.executeUpdate(con, "delete from TcrTemplateModule where ttm_tt_id = ? and ttm_tm_id = ?", new Object[]{tt_id, tm_id});
		List ttm_id_lst = sqlMapClient.getsingleColumnList(con, "select ttm_id from TcrTemplateModule where ttm_tt_id = ? and ttm_mod_x_value = ? order by ttm_mod_y_value asc", new Object[]{tt_id, dpt_way}, Long.class);
		//重新排序模板模块
		List paramsList = new ArrayList();
		for(int n=0; n<ttm_id_lst.size(); n++){
			paramsList.add(new Object[]{new Integer(n), ttm_id_lst.get(n)});
		}
		sqlMapClient.executeBatchUpdate(con, "update TcrTemplateModule set ttm_mod_y_value = ? where ttm_id = ?", paramsList);
		//删除模块
		sqlMapClient.executeUpdate(con, "delete from TcrModule where tm_code = ?", new Object[]{tm_code});
	}

}
