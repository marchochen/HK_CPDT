package com.cw.wizbank.position;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.db.DbUserPosition;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class UserPositionModule extends ServletModule {
	String USER_POSTION = "user_postion";
	
	UserPositionReqParam modParam;
	
	public UserPositionModule() {
		super();
		modParam = new UserPositionReqParam();
		param = modParam;
	}
	
	public void process() throws IOException, cwException, SQLException {
		if (this.prof == null || this.prof.usr_ent_id == 0) {
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			try {
				if(param.getCmd().equalsIgnoreCase("get_position_list") 
						|| param.getCmd().equalsIgnoreCase("get_position_list_xml")) {
					DbUserPosition dbUpt = new DbUserPosition();
					String xml = dbUpt.getUptListXml(con, param.getCwPage(), null, prof, wizbini.cfgSysSetupadv.isTcIndependent());
					
	                String metaXML = dbUpt.getMetaXML(con, prof, wizbini, xslQuestions, param.getStylesheet());
	                metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id); 
	                
					xml = formatXML(xml, metaXML, USER_POSTION);
					if(param.getCmd().equalsIgnoreCase("get_position_list_xml")){
						static_env.outputXML((PrintWriter)out, xml);
					}else{
						generalAsHtml(xml, (PrintWriter)out, param.getStylesheet());
					}
				} else if(param.getCmd().equalsIgnoreCase("search_position_list")
						|| param.getCmd().equalsIgnoreCase("search_position_list_xml")){
					DbUserPosition dbUpt = new DbUserPosition();
					String xml = dbUpt.getUptListXml(con, param.getCwPage(), modParam.getSearch_info(), prof, wizbini.cfgSysSetupadv.isTcIndependent());
					
					String metaXML = dbUpt.getMetaXML(con, prof, wizbini, xslQuestions, param.getStylesheet());
					metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id); 
					
					xml = formatXML(xml, metaXML, USER_POSTION);
					if(param.getCmd().equalsIgnoreCase("search_position_list_xml")){
						static_env.outputXML((PrintWriter)out, xml);
					}else{
						generalAsHtml(xml, (PrintWriter)out, param.getStylesheet());
					}
				} else if(param.getCmd().equalsIgnoreCase("add_position")
						|| param.getCmd().equalsIgnoreCase("add_position_xml")){
					String xml = "<status>Add</status><tc_independent>"+ wizbini.cfgSysSetupadv.isTcIndependent() + "</tc_independent>";
					xml = formatXML(xml, "", USER_POSTION);
					if(param.getCmd().equalsIgnoreCase("search_position_list_xml")){
						static_env.outputXML((PrintWriter)out, xml);
					}else{
						generalAsHtml(xml, (PrintWriter)out, param.getStylesheet());
					}
				} else if(param.getCmd().equalsIgnoreCase("modify_position")
						|| param.getCmd().equalsIgnoreCase("modify_position_xml")){
					DbUserPosition dbUpt = new DbUserPosition();
					String xml = dbUpt.getUserPositionDetailed(con, modParam.getUpt_code());
					xml += "<tc_independent>" + wizbini.cfgSysSetupadv.isTcIndependent() + "</tc_independent>";
					xml = formatXML(xml, "", USER_POSTION);
					if(param.getCmd().equalsIgnoreCase("search_position_list_xml")){
						static_env.outputXML((PrintWriter)out, xml);
					}else{
						generalAsHtml(xml, (PrintWriter)out, param.getStylesheet());
					}
				} else if(param.getCmd().equalsIgnoreCase("insert_position")
						|| param.getCmd().equalsIgnoreCase("insert_position_xml")){
					try{
						DbUserPosition dbUpt = new DbUserPosition();
						if(dbUpt.checkUserPosition(con, modParam.getUpt_code())){
							throw new cwSysMessage("USR040");
						}
						if(modParam.getUpt_tcr_id() == null){
							modParam.setUpt_tcr_id(prof.my_top_tc_id + "");
						}
						dbUpt.insertUserPosition(con, modParam, prof.usr_ent_id);
						con.commit();
						cwSysMessage e = new cwSysMessage("USR039");
			            msgBox(MSG_STATUS, e, param.getUrl_success(), out);
					} catch(cwSysMessage e) {
						con.rollback();
						msgBox(MSG_ERROR, e, param.getUrl_failure(), out);
			            return;
					}
				} else if(param.getCmd().equalsIgnoreCase("update_position")
						|| param.getCmd().equalsIgnoreCase("update_position_xml")){
					try{
						DbUserPosition dbUpt = new DbUserPosition();
						if(!dbUpt.checkUserPosition(con, modParam.getUpt_code())){
							throw new cwSysMessage("USR043");
						}
						if(modParam.getUpt_tcr_id() == null){
							modParam.setUpt_tcr_id(prof.my_top_tc_id + "");
						}
						dbUpt.updateUserPosition(con, modParam, prof.usr_ent_id);
						con.commit();
						cwSysMessage e = new cwSysMessage("USR042");
			            msgBox(MSG_STATUS, e, param.getUrl_success(), out);
					} catch(cwSysMessage e) {
						con.rollback();
						msgBox(MSG_ERROR, e, param.getUrl_success(), out);
			            return;
					}
				} else if(param.getCmd().equalsIgnoreCase("get_eff_position")
						|| param.getCmd().equalsIgnoreCase("get_eff_position_xml")){
					DbUserPosition dbUpt = new DbUserPosition();
					String xml = "<upt_code_list>" + modParam.getUpt_code_list() + "</upt_code_list>" 
							+ dbUpt.getDelUptAffectUsrXml(con, modParam.getUpt_code_list().split("\\[\\|\\]"));
					
					String metaXML = dbUpt.getMetaXML(con, prof, wizbini, xslQuestions, param.getStylesheet());
	                metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id); 
	                
					xml = formatXML(xml, metaXML, USER_POSTION);
					if(param.getCmd().equalsIgnoreCase("get_eff_position_xml")){
						static_env.outputXML((PrintWriter)out, xml);
					}else{
						generalAsHtml(xml, (PrintWriter)out, param.getStylesheet());
					}
				} else if(param.getCmd().equalsIgnoreCase("del_position")
						|| param.getCmd().equalsIgnoreCase("del_position_xml")){
					try{
						DbUserPosition dbUpt = new DbUserPosition();
						String[] upt_code_list = modParam.getUpt_code_list().split("\\[\\|\\]");
						for(int i=0;i<upt_code_list.length;i++){
							if(!dbUpt.checkUserPosition(con, upt_code_list[i])){
								throw new cwSysMessage("USR044");
							}
						}
						
						dbUpt.deleteUserPositionRelation(con, upt_code_list);
						dbUpt.deleteUserPosition(con, upt_code_list);
						con.commit();
						cwSysMessage e = new cwSysMessage("USR041");
			            msgBox(MSG_STATUS, e, param.getUrl_success(), out);
					} catch(cwSysMessage e) {
						con.rollback();
						msgBox(MSG_ERROR, e, param.getUrl_success(), out);
			            return;
					}
				} else {
					throw new qdbException("unknown command " + param.getCmd());
				}
			}catch (qdbException e) {
				con.rollback();
				throw new cwException("GEN000");
			}
		}
	}
}
