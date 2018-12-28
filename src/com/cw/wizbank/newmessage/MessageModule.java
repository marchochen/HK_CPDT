package com.cw.wizbank.newmessage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.newmessage.entity.*;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

public class MessageModule extends ServletModule {

	MessageModuleParam modParam;
	public static final String MOD_NAME = "message_module";
	public static final String FTN_MESSAGE_MGT = AclFunction.FTN_AMD_MESSAGE_TEMPLATE_MAIN;
	public static final String UPD_SUCCESS = "GEN003";
	public static final String UPD_FAIL = "GEN006";
	public static final String NOT_TO_VIEW = "1136";
	
	public MessageModule() {
		super();
		modParam = new MessageModuleParam();
		param = modParam;
	}
	
	public void process() throws SQLException, IOException, cwException {
		if (this.prof == null || this.prof.usr_ent_id == 0) {
			//发邮件
	        if( param.getCmd().equalsIgnoreCase("send_msg") ) {
	            MessageService.sendMessage(con, modParam.msg_id, static_env);
	            out.println("end");
	            return;
	        } else {
	
	            response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
	            
	        }
		} else {
			try {
				MessageService msgService = new MessageService();
				AccessControlWZB acl = new AccessControlWZB();
				// list
				if (param.getCmd().equalsIgnoreCase("get_msg_template_list") || param.getCmd().equalsIgnoreCase("get_msg_template_list_xml")) {
					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_MESSAGE_TEMPLATE_MAIN})) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
						return;
					}
					StringBuffer xml = new StringBuffer("");
					xml.append(msgService.getAllTemplate2Xml(con, modParam, prof));

					resultXml = formatXML(xml.toString(), MOD_NAME);
				} 
				// view
				else if (param.getCmd().equalsIgnoreCase("get_msg_template_view") || param.getCmd().equalsIgnoreCase("get_msg_template_view_xml")) {
					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_MESSAGE_TEMPLATE_MAIN})) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
						return;
					}
					String xml = "";
					if (modParam.getMtp_id() > 0) {
						xml = msgService.getTemplateDetailXML(con, modParam.getMtp_id(), ("message_template_upd.xsl").equalsIgnoreCase(modParam.getStylesheet()));
					} else {
						sysMsg = getErrorMsg(NOT_TO_VIEW, param.getUrl_failure());
						return;
					}
					resultXml = formatXML(xml, MOD_NAME);
				}
				// update template
				else if (param.getCmd().equalsIgnoreCase("upd_msg_template")) {

					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_MESSAGE_TEMPLATE_MAIN})) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
						return;
					}
					String saveDirPath = "";
					long mtpId = 0;
					if (modParam.getMtp_id() > 0) {
						mtpId = modParam.getMtp_id();
						if (MessageTemplate.isExist(con, mtpId)) {
							msgService.updateTemplate(con, wizbini, prof, modParam, multi);
							sysMsg = getErrorMsg(UPD_SUCCESS, param.getUrl_success());
						} else {
							sysMsg = getErrorMsg(UPD_FAIL, param.getUrl_failure());
							return;
						}
					}
					try {
						File dir = new File(saveDirPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						dbUtils.copyDir(tmpUploadPath, saveDirPath);
					} catch (qdbException e) {
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					}
					if (mtpId > 0) {
						modParam.setMtp_id(mtpId);
					}
				}

			}catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
	}
}
