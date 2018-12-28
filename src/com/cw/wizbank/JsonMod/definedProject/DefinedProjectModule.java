package com.cw.wizbank.JsonMod.definedProject;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.JsonMod.tcrCommon.TcrModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.AclFunction;

public class DefinedProjectModule extends TcrModule{
	private final String xslRootElemtnStr = "DefinedProject";
	private final DefinedProjectLogic definedProjectLogic = DefinedProjectLogic.getInstance();
	private DefinedProjectParam definedProjectParam;

	public DefinedProjectModule() {
		super(AclFunction.FTN_TEMP, DefinedProjectParam.class, new String[]{},
				new String[]{"get_project_lst", "dis_oprating_pre"});
		this.definedProjectParam = (DefinedProjectParam) tcrParam;
		// TODO Auto-generated constructor stub
	}
	
	public void get_project_lst(){
		try {
			resultXml = formatXML(definedProjectLogic.get_project_lst_xml_data(con, definedProjectParam, prof, request), xslRootElemtnStr);
		} catch (Exception e) {
			logger.error("method get_project_lst() excuete error.", e);
			throw new RuntimeException(
					"method get_project_lst() execute error.");
		}
	}
	
	public void dis_oprating_pre(){
		try {
			resultXml = formatXML(definedProjectLogic.dis_oprating_project_pre(con, definedProjectParam, prof), xslRootElemtnStr);
		} catch (Exception e) {
			logger.error("method get_project_lst() excuete error.", e);
			throw new RuntimeException(
					"method get_project_lst() execute error.");
		}
	}
	
	public void oprating_defined_project_exe() throws IOException, cwException, SQLException{
		String oprating = definedProjectParam.getOprating();
		cwSysMessage suc_message = null;
		cwSysMessage fail_message = null;
		try{
			 if(oprating.equals("ADD")){
				 String the_dpt_title = (String) sqlMapClient.getsingleColumn(con, "select dpt_title from DefinedProject where dpt_code = ?", 
						 new Object[]{definedProjectParam.getDpt_code()}, String.class);
				 if(the_dpt_title != null){
					 fail_message = new cwSysMessage("de_pro_08", new String[]{String.valueOf(definedProjectParam.getDpt_code())});
					 msgBox(MSG_STATUS, fail_message, definedProjectParam.getUrl_failure(), out);
					 return;
				 }
				 suc_message = new cwSysMessage("de_pro_01");
			 }else if(oprating.equals("UPD")){
				 String the_dpt_title = (String) sqlMapClient.getsingleColumn(con, "select dpt_title from DefinedProject where dpt_code = ? and dpt_id != ?", 
						 new Object[]{definedProjectParam.getDpt_code(), new Long(definedProjectParam.getDpt_id())}, String.class);
				 if(the_dpt_title != null){
					 fail_message = new cwSysMessage("de_pro_08", new String[]{String.valueOf(definedProjectParam.getDpt_code())});
					 msgBox(MSG_STATUS, fail_message, definedProjectParam.getUrl_failure(), out);
					 return;
				 }
				 suc_message = new cwSysMessage("de_pro_04");
			 }else if(oprating.equals("DEL")){
				 Long linkCount = (Long) sqlMapClient.getsingleColumn(con, "select count(pjl_id) pjl_count from projectLink where pjl_dpt_id = ?",
						 new Object[]{new Long(definedProjectParam.getDpt_id())}, Long.class);
				 if(linkCount.longValue() > 0){
					 fail_message = new cwSysMessage("de_pro_03", new String[]{String.valueOf(linkCount)});
					 msgBox(MSG_STATUS, fail_message, definedProjectParam.getUrl_failure(), out);
					 return;
				 }
				 suc_message = new cwSysMessage("de_pro_06");	
			 }
			 
			 definedProjectLogic.oprating_project_exe(con, definedProjectParam, prof);
			 
			 msgBox(MSG_STATUS, suc_message, definedProjectParam.getUrl_success(), out);
		}catch(Exception e){
			isTransactionException = true;
			logger.error("method oprating_defined_project_exe execute error.\n", e);
			if(oprating.equals("ADD")){
				 fail_message = new cwSysMessage("de_pro_02");	
			 }else if(oprating.equals("UPD")){
				 fail_message = new cwSysMessage("de_pro_05");
			 }else if(oprating.equals("DEL")){
				 fail_message = new cwSysMessage("de_pro_07");
			 }
			msgBox(MSG_STATUS, fail_message, definedProjectParam.getUrl_failure(), out);
		}
	}
	
	/*
	 * 自定义项目链接
	 */
	public void get_project_link_lst(){
		try {
			resultXml = formatXML(definedProjectLogic.get_project_link_lst_xml_data(con, definedProjectParam, prof, request), xslRootElemtnStr);
		} catch (Exception e) {
			logger.error("method get_project_link_lst() excuete error.", e);
			throw new RuntimeException("method get_project_link_lst() execute error.");
		}
	}
	
	public void dis_oprating_project_link_pre(){
		try {
			resultXml = formatXML(definedProjectLogic.dis_oprating_link_pre(con, definedProjectParam, prof), xslRootElemtnStr);
		} catch (Exception e) {
			logger.error("method dis_oprating_project_pre() excuete error.", e);
			throw new RuntimeException(
					"method dis_oprating_project_pre() execute error.");
		}
	}
	
	public void oprating_project_link_exe() throws IOException, cwException, SQLException{
		String oprating = definedProjectParam.getOprating();
		cwSysMessage suc_message = null;
		cwSysMessage fail_message = null;
		try{
			 if(oprating.equals("ADD")){
				 String the_pjl_title = (String) sqlMapClient.getsingleColumn(con, "select pjl_title from projectLink where pjl_code = ?", 
						 new Object[]{definedProjectParam.getPjl_code()}, String.class);
				 if(the_pjl_title != null){
					 fail_message = new cwSysMessage("de_pro_08", new String[]{String.valueOf(definedProjectParam.getPjl_code())});
					 msgBox(MSG_STATUS, fail_message, definedProjectParam.getUrl_failure(), out);
					 return;
				 }
				 suc_message = new cwSysMessage("de_pro_09");
			 }else if(oprating.equals("UPD")){
				 String the_pjl_title = (String) sqlMapClient.getsingleColumn(con, "select pjl_title from projectLink where pjl_code = ? and pjl_id != ?", 
						 new Object[]{definedProjectParam.getPjl_code(), new Long(definedProjectParam.getPjl_id())}, String.class);
				 if(the_pjl_title != null){
					 fail_message = new cwSysMessage("de_pro_08", new String[]{String.valueOf(definedProjectParam.getPjl_code())});
					 msgBox(MSG_STATUS, fail_message, definedProjectParam.getUrl_failure(), out);
					 return;
				 }
				 suc_message = new cwSysMessage("de_pro_11");
			 }else if(oprating.equals("DEL")){
				 suc_message = new cwSysMessage("de_pro_13");	
			 }
			 
			 definedProjectLogic.oprating_link_exe(con, definedProjectParam, prof);
			 
			 msgBox(MSG_STATUS, suc_message, definedProjectParam.getUrl_success(), out);
		}catch(Exception e){
			isTransactionException = true;
			logger.error("method oprating_defined_project_exe execute error.\n", e);
			if(oprating.equals("ADD")){
				 fail_message = new cwSysMessage("de_pro_10");	
			 }else if(oprating.equals("UPD")){
				 fail_message = new cwSysMessage("de_pro_12");
			 }else if(oprating.equals("DEL")){
				 fail_message = new cwSysMessage("de_pro_14");
			 }
			msgBox(MSG_STATUS, fail_message, definedProjectParam.getUrl_failure(), out);
		}
	}

}
