package com.cw.wizbank.JsonMod.credit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.credit.dao.CreditsTypeDAO;
import com.cw.wizbank.credit.view.ViewCreditsDAO;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;

public class CreditModule extends ServletModule {
	public static final String MOD_NAME  = "credit";	

	public static final String CREDIT_ADD_SUCC = "GEN001"; //记录已添加成功
	public static final String CREDIT_DEL_SUCC = "GEN002"; //记录已成功删除
	public static final String CREDIT_UPD_SUCC = "GEN003"; //记录已修改成功
	public static final String CREDIT_ADD_FAIL = "858"; //记录已存在 
	public static final String CREDIT_UPD_FAIL = "GEN006"; //该记录已被其他用户修改
    public static final String MSG_ERROR="ERROR";//ERROR参数
	CreditModuleParam modParam = null;
	
	public CreditModule() {
		super();
		modParam = new CreditModuleParam();
		param = modParam; 
	}
	
	public void process() throws cwException, IOException, SQLException {
		CreditModuleAccess modAccess = new CreditModuleAccess(wizbini, prof, con, modParam);
		modAccess.process();
		if(modParam.getCmd().equalsIgnoreCase("GET_CREDIT_MAIN") || 
				modParam.getCmd().equalsIgnoreCase("GET_CREDIT_MAIN_XML") ) {
			StringBuffer xml = new StringBuffer();
			AccessControlWZB acWZB = new AccessControlWZB();
			boolean credit_setting_main = false;
			boolean credit_other_main = false;
			
			
			if ( AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_CREDIT_SETTING_MAIN}) ) {
				credit_setting_main = true;
 
				// 不是独立中心 只有是系统管理员才可以
				// 是独立中心 那么都可以管 不一定是系统管理员
				// 也就是说  只有当  不是独立中心 并且不是系统管理员的话 才为false
				if(!wizbini.cfgSysSetupadv.isTcIndependent()&& !AccessControlWZB.isSysAdminRole(prof.current_role))
				{
					credit_setting_main = false;
				}	
	 
			}
			

			if (AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_CREDIT_SETTING_MAIN})) {
				credit_other_main = true;
			 
			} 
			xml.append("<credit_setting_main>").append(credit_setting_main).append("</credit_setting_main>");
			xml.append("<credit_other_main>").append(credit_other_main).append("</credit_other_main>");
			resultXml = formatXML(xml.toString(), MOD_NAME);
			
		} else if(modParam.getCmd().equalsIgnoreCase("GET_AUTO_CREDIT") || 
				modParam.getCmd().equalsIgnoreCase("GET_AUTO_CREDIT_XML") ) {
			Credit credit = new Credit();
			String result = credit.getAutoPointSettingAsXml(con,prof.my_top_tc_id);
			resultXml = formatXML(result, MOD_NAME);
			
		}else if(modParam.getCmd().equalsIgnoreCase("AUTO_BP_SETTING_UPD")) {
			AccessControlWZB acWZB = new AccessControlWZB();
			if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_CREDIT_SETTING_MAIN})) {
				sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
				return;
			} 
			Credit credit = new Credit();
			credit.updateAutoPointSetting(prof, modParam, con,prof.my_top_tc_id);
			sysMsg = getErrorMsg(CREDIT_UPD_SUCC, param.getUrl_success());
			
		}else if(modParam.getCmd().equalsIgnoreCase("GET_MANUAL_CREDIT") ||
				modParam.getCmd().equalsIgnoreCase("GET_MANUAL_CREDIT_XML")) {
			Credit credit = new Credit();
			String result = credit.getManualBonusXMl(con, modParam.getCty_deduction_ind(),prof.my_top_tc_id,modParam.getSort_order());
			resultXml = formatXML(result, MOD_NAME);
			
		}else if(modParam.getCmd().equalsIgnoreCase("INS_MANUAL_POINT")) {
			AccessControlWZB acWZB = new AccessControlWZB();
//			if (!acWZB.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, Credit.FTN_CREDIT_OTHER_MAIN)) {
//				sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
//				return;
//			} 
			Credit credit = new Credit();
            if(CreditsTypeDAO.isCreditPointTypeExist( con,  modParam.getCty_code(),prof.my_top_tc_id)){
            	sysMsg = getErrorMsgByStatus(MSG_ERROR, CREDIT_ADD_FAIL, param.getUrl_failure());
                return; 
            }
            credit.insManualPoint(con, prof.usr_id, modParam,prof.my_top_tc_id);
			sysMsg = getErrorMsg(CREDIT_ADD_SUCC, param.getUrl_success());
			
		}else if(modParam.getCmd().equalsIgnoreCase("DEL_MANUAL_POINT")) {
			AccessControlWZB acWZB = new AccessControlWZB();
//			if (!acWZB.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, Credit.FTN_CREDIT_OTHER_MAIN)) {
//				sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
//				return;
//			} 
			Credit credit = new Credit();
            credit.softDelManualPoint(con, prof.usr_id, modParam);
			sysMsg = getErrorMsg(CREDIT_DEL_SUCC, param.getUrl_success());
			
		}else if(modParam.getCmd().equalsIgnoreCase("SEARCH_MANUAL_POINT_LST")||
				modParam.getCmd().equalsIgnoreCase("SEARCH_MANUAL_POINT_LST_XML")) {
			AccessControlWZB acWZB = new AccessControlWZB();
//			if (!acWZB.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, Credit.FTN_CREDIT_OTHER_MAIN)) {
//				sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
//				return;
//			} 
			long tcr_id= 0;
			if(Credit.INTEGRAL_EMPTY.equals(modParam.getCty_set_type())){
			
				Credit credit = new Credit();
				
				long top_tcr_id=ViewTrainingCenter.getRootTcId(con);
				String result_no_deduction = credit.getManualBonusXMl(con, true, top_tcr_id,"asc",Credit.INTEGRAL_EMPTY);
				//String result_deduction = credit.getManualBonusXMl(con, false, prof.my_top_tc_id,"asc");
				String result = result_no_deduction;
				resultXml = formatXML(result, MOD_NAME);
			}else{
				
				Credit credit = new Credit();
				String result_no_deduction = credit.getManualBonusXMl(con, true, prof.my_top_tc_id,"asc");
				String result_deduction = credit.getManualBonusXMl(con, false, prof.my_top_tc_id,"asc");
				String result = result_no_deduction + result_deduction;
				resultXml = formatXML(result, MOD_NAME);
			}
		}else if(modParam.getCmd().equalsIgnoreCase("SET_LEARNER_POINT_EXEC")) {
			Credit credit = new Credit();
			credit.setLrnjifen(con, modParam, prof.usr_id);
			sysMsg = getErrorMsg(CREDIT_UPD_SUCC, param.getUrl_success());
		
		}else if(modParam.getCmd().equalsIgnoreCase("get_credit_rank")) {
			Map rankListMap = ViewCreditsDAO.getCreditRank(con, modParam.getStart(), modParam.getLimit());
			Map myCreditDetail = ViewCreditsDAO.getMyCreditDetail(con, prof.usr_ent_id);
			Map rankMap = new HashMap();
			rankMap.put("credit_rank_lst", rankListMap.get(ViewCreditsDAO.MAP_KEY_LIST));
			rankMap.put("total", rankListMap.get(ViewCreditsDAO.MAP_KEY_TOTAL));
			rankMap.put("update_time", rankListMap.get(ViewCreditsDAO.MAP_KEY_UPDATE_TIME));
			rankMap.put("my_credit", myCreditDetail.get(ViewCreditsDAO.MAP_KEY_MY_CREDIT));
			rankMap.put("my_credit_sort", myCreditDetail.get(ViewCreditsDAO.MAP_KEY_MY_CREDIT_SORT));
			resultJson.put("credit_rank", rankMap);
	
		}else if(modParam.getCmd().equalsIgnoreCase("get_credit_history")) {
			Map creditHistMap = ViewCreditsDAO.getMyCreditHistory(con, prof.usr_ent_id, modParam.getStart(), modParam.getLimit());
			Map creditTotal = ViewCreditsDAO.getCreditTotalByType(con, prof.usr_ent_id);
			Map myCreditMap = new HashMap();
			myCreditMap.put("credit_lst", creditHistMap.get(ViewCreditsDAO.MAP_KEY_LIST));
			myCreditMap.put("total", creditHistMap.get(ViewCreditsDAO.MAP_KEY_TOTAL));
			myCreditMap.put("training_score", creditTotal.get(ViewCreditsDAO.MAP_KEY_TRAINNING_SCORE));
			myCreditMap.put("activity_score", creditTotal.get(ViewCreditsDAO.MAP_KEY_ACTIVITY_SCORE));
			resultJson.put("my_credit", myCreditMap);
		
		}else if(modParam.getCmd().equalsIgnoreCase("GET_ATTEND_USER_LST")||
				modParam.getCmd().equalsIgnoreCase("GET_ATTEND_USER_LST_XML")) {
			AccessControlWZB acWZB = new AccessControlWZB();
			/*if (!acWZB.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, Credit.FTN_CREDIT_OTHER_MAIN)) {
				sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
				return;
			} */
			Credit credit = new Credit();
			String result = credit.getAttendUser(con, modParam);
			resultXml = formatXML(result, MOD_NAME);
		
		}else if(modParam.getCmd().equalsIgnoreCase("SET_ATTEND_USR_CREDIT")) {
			Credit credit = new Credit();
			credit.setAttendUserCredit(con, modParam, prof.usr_id);
			sysMsg = getErrorMsg(CREDIT_UPD_SUCC, param.getUrl_success());
		
		}else {
				throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
			}
		}
}