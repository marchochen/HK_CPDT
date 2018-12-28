package com.cw.wizbank.systemSetting;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.ErrorMsg;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

public class SystemSettingModule extends ServletModule {
	public final static String MOD_NAME = "system_setting";

	private SystemSettingReqParam modParam;

	public SystemSettingModule() {
		super();
		modParam = new SystemSettingReqParam();
		param = modParam;
	}

	public void process() throws IOException, cwException, SQLException {
		if (this.prof == null || this.prof.usr_ent_id == 0) {
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			String cmd = param.getCmd();
			//type 可选值为sys或者WeChat，分别是系统设置和绑定微信
			String type = request.getParameter("type");
			try {
				if (cmd.equalsIgnoreCase("prep") || cmd.equalsIgnoreCase("prep_xml")) {
					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_SYS_SETTING_MAIN)) {
						throw new cwSysMessage("ACL002");
					}
					SystemSetting ss = new SystemSetting();
					resultXml = formatXML(ss.getAsXml(con,type,wizbini,prof), MOD_NAME);
				} else if (cmd.equalsIgnoreCase("exec")) {
					AccessControlWZB acWzb = new AccessControlWZB();
					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_SYS_SETTING_MAIN)) {
						throw new cwSysMessage("ACL002");
					}
					SystemSetting ss = new SystemSetting();
					if(modParam.i_doc_view_preview_IP != null && modParam.i_doc_view_preview_IP.length() > 0)
					{
						modParam.i_doc_view_preview_IP = modParam.i_doc_view_preview_IP.trim();
					}
					if(modParam.i_doc_view_host != null && modParam.i_doc_view_host.length() > 0)
					{
						modParam.i_doc_view_host = modParam.i_doc_view_host.trim();
					}
					if(modParam.i_doc_view_preview_host != null && modParam.i_doc_view_preview_host.length() > 0)
					{
						modParam.i_doc_view_preview_host = modParam.i_doc_view_preview_host.trim();
					}
					ss.update(con, prof, modParam,type);
					sysMsg = new ErrorMsg();
					sysMsg.setMessage(new cwSysMessage("GEN003").getSystemMessage(prof.label_lan));
				} else {
					throw new qdbException("unknown command " + param.getCmd());
				}
			} catch (cwSysMessage e) {
				con.rollback();

				CommonLog.error(e.getMessage(),e);
			} catch (qdbException e) {
				con.rollback();
				throw new cwException("GEN000");
			}
		}
	}
}
