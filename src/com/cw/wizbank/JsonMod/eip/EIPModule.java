package com.cw.wizbank.JsonMod.eip;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.eip.dao.EnterpriseInfoPortalDao;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;

/**
 * 企业信息门户
 */
public class EIPModule extends ServletModule {

	EIPModuleParam modParam;
	public static final String MOD_NAME = "eip_module";	
	
	public static final float EIP_PRICE = 100f;
	
	
	public EIPModule() {
		super();
		modParam = new EIPModuleParam();
		param = modParam;
	}
	
	public void process() throws IOException, cwException, SQLException, qdbException, qdbErrMessage{
		try{
			if(this.prof == null || this.prof.usr_ent_id == 0) {	// 若还是未登录
				throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
			} else {
				EnterpriseInfoPortal eip = new EnterpriseInfoPortal();
				if (modParam.getCmd().equalsIgnoreCase("get_eip_list") || modParam.getCmd().equalsIgnoreCase("get_eip_list_xml")) {
					if (!isAdmin() || !AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_EIP_MAIN})) {
						throw new cwSysMessage("ACL002");
					}	
					String xml = eip.getAllEIPAsXml(con, modParam.getCwPage());
					resultXml = formatXML(xml, MOD_NAME);
				} else if(modParam.getCmd().equalsIgnoreCase("ins_upd_eip_prep") || modParam.getCmd().equalsIgnoreCase("ins_upd_eip_prep_xml")){

					boolean hasUpdPrivilege = isAdmin() && AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_EIP_MAIN});
	
					String xml = "<hasUpdPrivilege>"+hasUpdPrivilege+"</hasUpdPrivilege>";
					if(modParam.getEip_id() != 0){
						xml += eip.getEIPAsXml(con, modParam.getEip_id(), prof.root_ent_id);
					 }else{
						 xml += "<eip/>";
					 }
					 resultXml = formatXML(xml, MOD_NAME);
					 
				} else if(modParam.getCmd().equalsIgnoreCase("ins_eip_exec") || modParam.getCmd().equalsIgnoreCase("ins_eip_exec_xml")){
					if (!isAdmin() || !AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_EIP_MAIN})) {
						throw new cwSysMessage("ACL002");
					}
					//判断租用账户数设置的值是否小于已经存在的用户
					EnterpriseInfoPortalDao.checkInsertOrUpdateEIPAccountNum(con, modParam.getEip_tcr_id(), modParam.getEip_account_num());
					/*
					if(bMultipart && multi.getFilesystemName("login_bg_file") != null){
						modParam.setEip_login_bg(multi.getFilesystemName("login_bg_file"));
					}
					*/
					long newEIPId = eip.insEIP(con, modParam, prof.usr_id);
					
					con.commit();
					sysMsg = getErrorMsg(EnterpriseInfoPortal.EIP_ADD_SUCC, param.getUrl_success());					 
				} else if(modParam.getCmd().equalsIgnoreCase("upd_eip_exec") || modParam.getCmd().equalsIgnoreCase("upd_eip_exec_xml")){
					if (!isAdmin() || !AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_EIP_MAIN})) {
						throw new cwSysMessage("ACL002");
					}	
					//判断租用账户数设置的值是否小于已经存在的用户
					EnterpriseInfoPortalDao.checkInsertOrUpdateEIPAccountNum(con, modParam.getEip_tcr_id(), modParam.getEip_account_num());
					/*
					if(bMultipart && multi.getFilesystemName("login_bg_file") != null){
						modParam.setEip_login_bg(multi.getFilesystemName("login_bg_file"));
					}*/
					eip.updEIP(con, modParam, prof.usr_id);		
					con.commit();
					sysMsg = getErrorMsg(EnterpriseInfoPortal.EIP_UPD_SUCC, param.getUrl_success());					 
				} else if(modParam.getCmd().equalsIgnoreCase("set_status") || modParam.getCmd().equalsIgnoreCase("set_status_xml")){
					if (!isAdmin() || !AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_EIP_MAIN})) {
						throw new cwSysMessage("ACL002");
					}	
					eip.setEIPStatus(con, modParam, prof.usr_id);
					con.commit();
					sysMsg = getErrorMsg(EnterpriseInfoPortal.EIP_UPD_SUCC, param.getUrl_success());					 
				} else if(modParam.getCmd().equalsIgnoreCase("del_eip") || modParam.getCmd().equalsIgnoreCase("del_eip_xml")){
					if (!isAdmin() || !AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_EIP_MAIN})) {
						throw new cwSysMessage("ACL002");
					}	
					eip.delEIP(con, modParam);
					con.commit();
					sysMsg = getErrorMsg(EnterpriseInfoPortal.EIP_DEL_SUCC, param.getUrl_success());					 
				} else if(modParam.getCmd().equalsIgnoreCase("set_login_bg") || modParam.getCmd().equalsIgnoreCase("set_login_bg_xml")){
					/*
					if(bMultipart && multi.getFilesystemName("login_bg_file") != null){
						modParam.setEip_login_bg(multi.getFilesystemName("login_bg_file"));
					}
					if(bMultipart && multi.getFilesystemName("mobile_login_bg_file") != null){
						modParam.setEip_mobile_login_bg(multi.getFilesystemName("mobile_login_bg_file"));
					}*/
								
					//eip.setEIPLoginBg(con, modParam, prof.usr_id);

					con.commit();
					sysMsg = getErrorMsg(EnterpriseInfoPortal.EIP_UPD_SUCC, param.getUrl_success());					 

				} else if(modParam.getCmd().equalsIgnoreCase("ins_upd_dynamicpri") || modParam.getCmd().equalsIgnoreCase("ins_upd_dynamicpri_xml")){
					eip.setDynamicPri(con, modParam, prof.usr_id);
					con.commit();
					sysMsg = getErrorMsg(EnterpriseInfoPortal.EIP_UPD_SUCC, param.getUrl_success());					 

				} else if(modParam.getCmd().equalsIgnoreCase("empty_data")){
					String message = EnterpriseInfoPortal.EIP_NO_PERMISSION;
					if(prof.common_role_id.equalsIgnoreCase("ADM")){
						EIPEmptyData eipEmptyData = new EIPEmptyData();
						eipEmptyData.emptyTcrData(con, wizbini, prof, modParam.getEip_tcr_id());
						EnterpriseInfoPortalDao.updateEmptyDataUser(con, prof.usr_id, modParam.getEip_id());
						con.commit();
						message = EnterpriseInfoPortal.EIP_DEL_SUCC;
					}
					sysMsg = getErrorMsg(message, param.getUrl_success());
				} else {
					throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
				}
			} 
		} catch (cwSysMessage e) {
			try {
				con.rollback();
				msgBox(ServletModule.MSG_ERROR, e, modParam.getUrl_failure(), out);
			} catch (SQLException sqle) {
				out.println("SQL error: " + sqle.getMessage());
			}
		}
	}
	
	
	
	/**
	 * 判断当前用户是否为"admin"
	 * @return
	 */
	private boolean isAdmin(){
		boolean isAdmin = false;
		if(prof.usr_status != null && prof.usr_status.equals(dbRegUser.USR_STATUS_SYS) && prof.usr_ste_usr_id.equals("admin")){
			isAdmin = true;
		}
		return isAdmin;
	}
}
