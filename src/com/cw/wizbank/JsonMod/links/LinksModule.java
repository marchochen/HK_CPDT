
package com.cw.wizbank.JsonMod.links;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.links.dao.FriendshipLinkDAO;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;

/**
 * 友情链接、宣传栏广告
 */
public class LinksModule extends ServletModule {
	
	LinksModuleParam modParam;
	public static final String MOD_NAME = "links_module";	
	public static final String FSLINK_ADD_SUCC = "GEN001"; //记录已添加成功
	public static final String FSLINK_DEL_SUCC = "GEN002"; //记录已成功删除
	public static final String FSLINK_UPD_SUCC = "GEN003"; //记录已修改成功
	public static final String FSLINK_ADD_FAIL = "795"; //记录已存在 
	public static final String FSLINK_UPD_FAIL = "GEN006"; //该记录已被其他用户修改
	public LinksModule() {
		super();
		modParam = new LinksModuleParam();
		param = modParam;
	}
	
	public void process() throws SQLException, IOException, cwException {
		if(this.prof == null || this.prof.usr_ent_id == 0) {	// 若还是未登录
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			Link link = new Link();
			if (modParam.getCmd().equalsIgnoreCase("get_fslinks_list") || modParam.getCmd().equalsIgnoreCase("get_fslinks_list_xml")) {
				String xml = link.getAllFslinksAsXml(con, modParam.getCwPage(), false);
				resultXml = formatXML(xml, MOD_NAME);
			}else if (modParam.getCmd().equalsIgnoreCase("get_fslink_prep") || modParam.getCmd().equalsIgnoreCase("get_fslink_prep_xml")) {
				 String xml="";						
				 if(modParam.getFsl_id() !=0){
					 xml= link.getFsLinkInfoXml(con, modParam);
				 }else{
					 xml="<link/>";
				 }
				 resultXml = formatXML(xml, MOD_NAME);
			}else if(modParam.getCmd().equalsIgnoreCase("ins_link")){
				FriendshipLinkDAO linkDao= new FriendshipLinkDAO();
				AccessControlWZB acWZB = new AccessControlWZB();
//				if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)) {
//					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
//					return;
//				} 
				if(linkDao.isLinkTitleExist(con, modParam.getFsl_title(), 0)){
					sysMsg = getErrorMsg(FSLINK_ADD_FAIL, param.getUrl_failure());
					return;
				}
				long cur_fsl_id = link.addLink(con, prof, modParam);	
				if (cur_fsl_id > 0) {
					sysMsg = getErrorMsg(FSLINK_ADD_SUCC, param.getUrl_success());
				}
			}else if(modParam.getCmd().equalsIgnoreCase("upd_link")){
				FriendshipLinkDAO linkDao= new FriendshipLinkDAO();
				AccessControlWZB acWZB = new AccessControlWZB();
//				if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)) {
//					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
//					return;
//				} 
				if(!linkDao.isLinkIdExist(con, modParam.getFsl_id(), modParam.getFsl_update_timestamp())){
					sysMsg = getErrorMsg("GEN006", param.getUrl_failure());
					return;
				}
				if(linkDao.isLinkTitleExist(con, modParam.getFsl_title(), modParam.getFsl_id())){
					sysMsg = getErrorMsg(FSLINK_ADD_FAIL, param.getUrl_failure());
					return;
				}
				link.updLink(con, prof, modParam);	
				sysMsg = getErrorMsg(FSLINK_UPD_SUCC, param.getUrl_success());
			}else if (modParam.getCmd().equalsIgnoreCase("del_link")) {
				AccessControlWZB acWZB = new AccessControlWZB();
//				if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)) {
//					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
//					return;
//				} 
				FriendshipLinkDAO linkDAO= null ;
				long[] fsl_id_array = cwUtils.splitToLong(modParam.getFsl_id_lst(), "~");
				Timestamp[] fsl_update_timestamp_array = dbUtils.string2timestamp(cwUtils.splitToString(modParam.getFsl_update_timestamp_lst(), "~"));
				for (int i=0; i<fsl_id_array.length; i++) {
					linkDAO = new FriendshipLinkDAO();
					if(!linkDAO.isLinkIdExist(con, fsl_id_array[i], fsl_update_timestamp_array[i])){
						sysMsg = getErrorMsg("GEN006", param.getUrl_failure());
						return;
					}
					FriendshipLinkDAO.del(con, fsl_id_array[i]);
					sysMsg = getErrorMsg(FSLINK_DEL_SUCC, param.getUrl_success());
				}
		      }else {
				throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
			}
		} 
	}
}