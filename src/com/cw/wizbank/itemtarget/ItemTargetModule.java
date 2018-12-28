package com.cw.wizbank.itemtarget;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.aeItem ;
import com.cwn.wizbank.utils.CommonLog;

public class ItemTargetModule extends ServletModule {	
	private static String ITEM_TARGET_MODULE = "item_target";
	
	public void process() throws IOException, cwException, SQLException {
		HttpSession sess = request.getSession(false);
		if (sess == null || prof == null) {
			response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
		}
		ItemTargetReqParam urlp = new ItemTargetReqParam(request, clientEnc, static_env.ENCODING);
		if (bMultipart) {
			urlp.setMultiPart(multi);
		}
		urlp.common();
		PrintWriter out = response.getWriter();
		try {
			if (urlp.cmd.equalsIgnoreCase("get_rule_lst") 
					|| urlp.cmd.equalsIgnoreCase("get_rule_lst_xml")) {
				urlp.getItemTargetParam();
				AcItem acitm = new AcItem(con);
				if(!acitm.hasUpdPrivilege(urlp.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
					throw new cwSysMessage("ACL002");
				}
				ManageItemTarget.chkPermission(con, urlp.itm_id, urlp.itm_target_type);
				String xml = ManageItemTarget.getItemTargetRuleLstXML(con, urlp, prof.root_ent_id);
				xml += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
				xml +="<is_new_cos>"+urlp.is_new_cos+"</is_new_cos>";
	  	        String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
	  	        
				xml = formatXML(xml, metaXML, ITEM_TARGET_MODULE);
				if(urlp.cmd.equalsIgnoreCase("get_rule_lst_xml")){
					static_env.outputXML((PrintWriter)out, xml);
				}else{
					generalAsHtml(xml, (PrintWriter)out, urlp.stylesheet);
				}
			} else if(urlp.cmd.equalsIgnoreCase("set_rule_prev") 
					|| urlp.cmd.equalsIgnoreCase("set_rule_prev_xml")) {
				urlp.getItemTargetParam();
				AcItem acitm = new AcItem(con);
				if(!acitm.hasUpdPrivilege(urlp.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
					throw new cwSysMessage("ACL002");
				}
				ManageItemTarget.chkPermission(con, urlp.itm_id, urlp.itm_target_type);
				String xml = ManageItemTarget.setItemTargetRulePrevXML(con, urlp, prof.root_ent_id);
	  	        String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
	  	        xml += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
				xml +="<is_new_cos>"+urlp.is_new_cos+"</is_new_cos>";
				xml = formatXML(xml, metaXML, ITEM_TARGET_MODULE);
				if(urlp.cmd.equalsIgnoreCase("set_rule_prev_xml")){
					static_env.outputXML((PrintWriter)out, xml);
				}else{
					generalAsHtml(xml, (PrintWriter)out, urlp.stylesheet);
				}
			} else if(urlp.cmd.equalsIgnoreCase("set_rule_exec")) {
				urlp.getItemTargetParam();
				AcItem acitm = new AcItem(con);
				if(!acitm.hasUpdPrivilege(urlp.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
					throw new cwSysMessage("ACL002");
				}
				// 检测选定的用户组是否存在
				if (urlp.target_group_lst != null && urlp.target_group_lst.length() > 0) {
					long[] usgIds = cwUtils.splitToLong(urlp.target_group_lst, ItemTargetReqParam.deli);
					if (usgIds != null && usgIds.length > 0 && !dbRegUser.isUsgExists(con, usgIds)) {
						throw new cwSysMessage("USG011");
					}
				}
				ManageItemTarget.chkPermission(con, urlp.itm_id, urlp.itm_target_type);
				/*if(urlp.rule_id > 0 && !ManageItemTarget.checkUpdTimestamp(con, urlp.rule_id, urlp.timestamp)) {
					throw new cwSysMessage("GEN006");
				}*/
				ManageItemTarget.saveTargetRule(con, urlp, prof);
				//更新课程目标学员的中间表
                ManageItemTarget.setTargetCache( con,  urlp.itm_id, true);
				con.commit();

				//清空table
				aeItem.usrTargetItmLstHS.clear() ;
				CommonLog.info("set_rule_exec  >>>>>>>>>>>>>>>>>>>          "+aeItem.usrTargetItmLstHS.isEmpty()) ;

				cwSysMessage sms = new cwSysMessage("GEN003");
				msgBox(MSG_STATUS, sms, urlp.url_success, out);
			} else if(urlp.cmd.equalsIgnoreCase("preview_rule") 
					|| urlp.cmd.equalsIgnoreCase("preview_rule_xml")) {
				urlp.getItemTargetParam();
				String xml = ManageItemTarget.getTargetUserLstXML(con, urlp);
	  	        String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
				xml = formatXML(xml, metaXML, ITEM_TARGET_MODULE);
				if(urlp.cmd.equalsIgnoreCase("preview_rule_xml")){
					static_env.outputXML((PrintWriter)out, xml);
				}else{
					generalAsHtml(xml, (PrintWriter)out, urlp.stylesheet);
				}
			} else if(urlp.cmd.equalsIgnoreCase("del_rule")) {
				urlp.getItemTargetParam();
				AcItem acitm = new AcItem(con);
				if(!acitm.hasUpdPrivilege(urlp.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
					throw new cwSysMessage("ACL002");
				}
				/*if(urlp.rule_id > 0 && !ManageItemTarget.checkUpdTimestamp(con, urlp.rule_id, urlp.timestamp)) {
					throw new cwSysMessage("GEN006");
				}*/
				ManageItemTarget.delTargetRuleById(con, urlp.rule_id);
				//更新课程目标学员的中间表
                ManageItemTarget.setTargetCache( con,  urlp.itm_id, true);
				con.commit();
				//清空table
				aeItem.usrTargetItmLstHS.clear() ;
				CommonLog.info("del_rule >>>>>>>>>>>>>>>>>>>          "+aeItem.usrTargetItmLstHS.isEmpty()) ;
				cwSysMessage sms = new cwSysMessage("GEN002");
				msgBox(MSG_STATUS, sms, urlp.url_success, out);
			} else if (urlp.cmd.equals("change_target_enroll_type")) {
				urlp.getItemTargetParam();
				AcItem acitm = new AcItem(con);
				if(!acitm.hasUpdPrivilege(urlp.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
					throw new cwSysMessage("ACL002");
				}
				ManageItemTarget.chkPermission(con, urlp.itm_id, urlp.itm_target_type);
				if(!ManageItemTarget.checkTargetEnrolType(con, urlp)) {
					throw new cwSysMessage("GEN006");
				}
				ManageItemTarget.changeTargetEnrolRule(con, urlp, prof);
				con.commit();
				response.sendRedirect(urlp.url_success);
			}
			else {
				throw new qdbException("unknown command " + urlp.cmd);
			}
		}catch (qdbException e) {
			con.rollback();
			throw new cwException("GEN000");
		} catch (cwSysMessage e) {
			con.rollback();
			cwSysMessage sms = new cwSysMessage(e.getId());
			msgBox(MSG_ERROR, sms, urlp.url_failure, out);
		}
	}
	
	
}
