package com.cw.wizbank.JsonMod.eip;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.JsonMod.eip.bean.EnterpriseDynamicPriSetBean;
import com.cw.wizbank.JsonMod.eip.bean.EnterpriseInfoPortalBean;
import com.cw.wizbank.JsonMod.eip.dao.EnterpriseInfoPortalDao;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class EnterpriseInfoPortal {
	
	public static final String FTN_EIP_MAIN = "EIP_MAIN";
	public static final String EIP_ADD_SUCC = "GEN001"; //记录已添加成功
	public static final String EIP_DEL_SUCC = "GEN002"; //记录已成功删除
	public static final String EIP_UPD_SUCC = "GEN003"; //记录已修改成功
	public static final String EIP_ADD_FAIL = "795"; //记录已存在 
	public static final String EIP_UPD_FAIL = "GEN006"; //该记录已被其他用户修改
	public static final String EIP_CODE_EXIST = "EIP001";//企业编号已存在
	public static final String EIP_TCR_EXIST = "EIP002";//所选培训中心已被其他企业关联
	public static final String EIP_NAME_EXIST = "EIP003";//企业名称已存在
	public static final String EIP_DOMAIN_EXIST = "EIP004";//企业域名已存在
	public static final String EIP_OVER_ACCOUNT_LIMITED = "EIP005";//企业租用帐号数目已达到
	public static final String EIP_USER_ID_EXIST = "EIP006";//企业内部用户名已存在
	public static final String EIP_OVER_ACCOUNT_LIMITED_REGISTER = "EIP007";//企业租用帐号数目已达到, 如需注册， 请联系管理员
	public static final String EIP_NO_PERMISSION = "ACL002";//没有足够权限
	
	public static final String EIP_BUY_SUCC = "LN052"; //购买成功
	public static final String EIP_BUY_FAIL = "LN053"; //购买失败
	/**
	 * 返回企业列表xml
	 * @param con
	 * @param cwPage
	 * @return
	 * @throws SQLException
	 */
	public String getAllEIPAsXml(Connection con, cwPagination cwPage) throws SQLException {
		StringBuffer result = new StringBuffer();
		EnterpriseInfoPortalBean eipBean = null;
		Vector eipVec = EnterpriseInfoPortalDao.getAllEIP(con, cwPage);
		result.append("<eip_list>");
		for (int i=0; i<eipVec.size(); i++) {
			eipBean = (EnterpriseInfoPortalBean)eipVec.get(i);
			result.append(asXml(eipBean));
		}
		result.append("</eip_list>");
		result.append(cwPage.asXML());
		return result.toString();
	}

	/**
	 * 根据企业BEAN构造xml
	 * @param eipBean
	 * @return
	 * @throws SQLException
	 */
	public String asXml(EnterpriseInfoPortalBean eipBean) throws SQLException {
		if(eipBean == null) return null;
		StringBuffer xml = new StringBuffer();
		xml.append("<eip id=\"").append(eipBean.getEip_id())
		   .append("\" code=\"").append(cwUtils.esc4XML(eipBean.getEip_code()))
		   .append("\" name=\"").append(cwUtils.esc4XML(eipBean.getEip_name()))
		   .append("\" tcr_id=\"").append(eipBean.getEip_tcr_id())
		   .append("\" tcr_title=\"").append(cwUtils.esc4XML(eipBean.getTcr_title()))
		   .append("\" account_num=\"").append(eipBean.getEip_account_num())
		   .append("\" account_used=\"").append(eipBean.getAccount_used())
		   .append("\" account_leaving=\"").append(eipBean.getEip_account_num() - eipBean.getAccount_used() > 0 ? eipBean.getEip_account_num() - eipBean.getAccount_used() : 0)
		   .append("\" status=\"").append(eipBean.getEip_status())
		   .append("\" domain=\"").append(cwUtils.esc4XML(eipBean.getEip_domain()))
		   .append("\" update_user=\"").append(cwUtils.esc4XML(eipBean.getEip_update_display_bil()))
		   .append("\" update_timestamp=\"").append(eipBean.getEip_update_timestamp())
		   .append("\" peak_count=\"").append(String.valueOf(eipBean.getPeak_count()) == null?0:eipBean.getPeak_count())
		   .append("\" eip_max_peak_count=\"").append(String.valueOf(eipBean.getEip_max_peak_count()) == null?0:eipBean.getEip_max_peak_count())
		   .append("\" eip_live_max_count=\"").append(String.valueOf(eipBean.getEip_live_max_count()) == null?0:eipBean.getEip_live_max_count())
		   .append("\" eip_live_qcloud_secretid=\"").append(eipBean.getEip_live_qcloud_secretid())
		   .append("\" eip_live_qcloud_secretkey=\"").append(eipBean.getEip_live_qcloud_secretkey())
		   .append("\"/>");
			xml.append("<eip_live_mode>");
			if(eipBean.getEip_live_mode() != null && eipBean.getEip_live_mode().length() > 0 ){
				String[] s = eipBean.getEip_live_mode().split(",");
				if(s.length > 1){
					for (String str : s) {
						xml.append("<eip_live_mode_option>").append(str).append("</eip_live_mode_option>");
					}
				}else{
					xml.append("<eip_live_mode_option>").append(s[0]).append("</eip_live_mode_option>");
				}
			}
			xml.append("</eip_live_mode>");
		return xml.toString();
	}
	
	/**
	 * 返回企业信息xml（包括其关联的培训中心信息）
	 * @param con
	 * @param eip_id
	 * @param root_ent_id
	 * @return
	 * @throws SQLException
	 */
	public String getEIPAsXml(Connection con, long eip_id, long root_ent_id) throws SQLException {
		EnterpriseInfoPortalBean eipBean = EnterpriseInfoPortalDao.getEIPByID(con, eip_id);
		if(eipBean == null) return "";
		return asXml(eipBean) + getEIPTcDetailXml(con, eipBean.getEip_tcr_id(), root_ent_id);
	}
	
	/**
	 * 返回企业关联的培训中心详细信息xml
	 * @param con
	 * @param tcr_id
	 * @param root_ent_id
	 * @return
	 * @throws SQLException
	 */
	public String getEIPTcDetailXml(Connection con, long tcr_id, long root_ent_id) throws SQLException{
		DbTrainingCenter dbTc = new DbTrainingCenter(tcr_id);
		TrainingCenter tc = new TrainingCenter();
		return tc.getTcInsPrep(con, root_ent_id, dbTc, true);
	}
	
	/**
	 * 创建企业
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @return
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public long insEIP(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException, cwSysMessage{
		checkExist(con, modParam);
		long eipId = EnterpriseInfoPortalDao.ins(con, modParam, usr_id);
		//复制邮件模板
		MessageService msgService = new MessageService();
		msgService.coptyTemplates( con, ViewTrainingCenter.getRootTcId(con), modParam.getEip_tcr_id()) ;
		return eipId;
	}
	
	/**
	 * 修改企业
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public void updEIP(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException, cwSysMessage{
		checkExist(con, modParam);
		EnterpriseInfoPortalDao.checkTimeStamp(con, modParam.getEip_id(), modParam.getEip_update_timestamp());
		EnterpriseInfoPortalDao.upd(con, modParam, usr_id);
	}
	
	/**
	 * 发布/取消发布 企业
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public void setEIPStatus(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException, cwSysMessage{
		EnterpriseInfoPortalDao.checkTimeStamp(con, modParam.getEip_id(), modParam.getEip_update_timestamp());
		EnterpriseInfoPortalDao.setStatus(con, modParam, usr_id);
	}
	
	/**
	 * 删除企业
	 * @param con
	 * @param modParam
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public void delEIP(Connection con, EIPModuleParam modParam) throws SQLException, cwSysMessage{
		EnterpriseInfoPortalDao.checkTimeStamp(con, modParam.getEip_id(), modParam.getEip_update_timestamp());
		EnterpriseInfoPortalDao.del(con, modParam.getEip_id());
	}
	
	/**
	 * 检查企业相关信息
	 * @param con
	 * @param modParam
	 * @throws cwSysMessage
	 * @throws SQLException
	 */
	private void checkExist(Connection con, EIPModuleParam modParam) throws cwSysMessage, SQLException{
		EnterpriseInfoPortalDao.checkEIPCodeExist(con, modParam.getEip_id(), modParam.getEip_code());
		EnterpriseInfoPortalDao.checkEIPNameExist(con, modParam.getEip_id(), modParam.getEip_name());
		EnterpriseInfoPortalDao.checkTcrExist(con, modParam.getEip_id(), modParam.getEip_tcr_id());
		if(modParam.getEip_domain() != null && modParam.getEip_domain().length() > 0){
			EnterpriseInfoPortalDao.checkEIPDomainExist(con, modParam.getEip_id(), modParam.getEip_domain());
		}
	}
	
	/**
	 * 培训管理员首页，获取默认培训中心的企业
	 * @param con
	 * @param prof
	 * @return
	 * @throws SQLException
	 */
	public static String getEIPAsXmlForLoginTadm(Connection con, loginProfile prof) throws SQLException{
		long tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
		long eip_id = EnterpriseInfoPortalDao.getEIPByTcrID(con, tcr_id);
		String xml = "<eip id=\""+eip_id+"\" tcr_id=\""+tcr_id+"\"/>";
		return xml;
	}
	
	/**
	 * 设置登陆背景
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	/*
	public void setEIPLoginBg(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException, cwSysMessage{
		EnterpriseInfoPortalDao.checkTimeStamp(con, modParam.getEip_id(), modParam.getEip_update_timestamp());
		EnterpriseInfoPortalDao.setLoginBg(con, modParam, usr_id);
	}
	*/
	/**
	 * 根据用户组或用户 检查企业的租用帐号数目是否已经达到限制
	 * @param con
	 * @param groupEntId
	 * @param userEntId
	 * @param add_num
	 * @param updUserGroup
	 * @throws cwSysMessage
	 * @throws SQLException
	 * @throws qdbErrMessage 
	 */
	public static void checkEIPAccountLimited(Connection con, long groupEntId, long userEntId, int add_num, boolean updUserGroup) throws SQLException, qdbErrMessage{				
		if(updUserGroup){
			//修改用户组的情况
			Vector usgVec = dbUserGroup.getUserParentEntIds(con, userEntId);
        	long oldGroupEntId = (Long)usgVec.elementAt(0);
			if(oldGroupEntId != groupEntId){
    			long old_eip = EnterpriseInfoPortalDao.getEIPByUserOrGroup(con, oldGroupEntId, 0);
    			long new_eip = EnterpriseInfoPortalDao.getEIPByUserOrGroup(con, groupEntId, 0);
    			if(new_eip > 0 && old_eip != new_eip){
    				EnterpriseInfoPortalDao.checkAccountLimited(con, groupEntId, userEntId, add_num);
    			}
    		}
		}else{
			//增加用户，恢复或粘贴用户
			EnterpriseInfoPortalDao.checkAccountLimited(con, groupEntId, userEntId, add_num);
		}		
	}
	
	/**
	 * 查询用户组id
	 * @param usr_attribute_relation_types
	 * @param usr_attribute_ent_ids
	 * @return
	 */
	public static long getUserParentGroupId(String[] usr_attribute_relation_types, long[] usr_attribute_ent_ids){		
		long groupEntId = 0;
		if (usr_attribute_relation_types != null && usr_attribute_relation_types.length > 0) { 		
	    	for (int i = 0; i < usr_attribute_relation_types.length; i++) {
	        	String ernType = usr_attribute_relation_types[i];
	        	
	        	if (dbEntityRelation.ERN_TYPE_USR_PARENT_USG.equalsIgnoreCase(ernType)) {
	        		groupEntId = usr_attribute_ent_ids[i];
	        		break;
	        	}
	        }
    	}
		return groupEntId;
	}
	
	/**
	 * 检查用户名是否已经存在
	 * @param con
	 * @param userId
	 * @param groupEntId
	 * @param userEntId
	 * @param siteId
	 * @throws cwSysMessage
	 * @throws SQLException
	 * @throws cwException
	 * @throws qdbErrMessage
	 */
	public static void checkEIPUsrId(Connection con, String userId, long groupEntId, long userEntId, long siteId) throws qdbErrMessage, SQLException, cwException{
		long eip_id = EnterpriseInfoPortalDao.getEIPByUserOrGroup(con, groupEntId, userEntId);
		
		if(eip_id > 0){
			//企业用户
			EnterpriseInfoPortalDao.checkEIPUsrIdExist(con, eip_id, userId, userEntId, siteId);
		}else{
			//非企业用户
			dbRegUser user = new dbRegUser();
			user.usr_ste_usr_id = userId;
			user.usr_ent_id = userEntId;
			if (user.checkSiteUsrIdExist(con, siteId)) {
				throw new qdbErrMessage("USR001", user.usr_ste_usr_id);
			}
		}
	}
	
	/**
	 * 根据域名查询企业信息
	 * @param con
	 * @param domain
	 * @return
	 * @throws SQLException
	 */
	public String getEIPAsXMLByDomain(Connection con, String domain) throws SQLException{
		EnterpriseInfoPortalBean eipBean = EnterpriseInfoPortalDao.getEIPByDomain(con, domain);
		return asXml(eipBean);
		
	}
	
	/**
	 * 查询所有企业的名称与培训中心ID
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static Vector getSelectEIPNameANDEITTcrID(Connection con)throws SQLException{
		
		return EnterpriseInfoPortalDao.getSelectEipAll(con);
	}
	
	/**
	 * 返回企业用户信息是否共享xml（包括其关联的培训中心信息）
	 * @param con
	 * @param eip_id
	 * @param root_ent_id
	 * @return
	 * @throws SQLException
	 */
	public String getDynamicPriAsXml(Connection con, long trc_id, long root_ent_id) throws SQLException {
		EnterpriseDynamicPriSetBean eipDpBean = EnterpriseInfoPortalDao.getDynamicPri(con, trc_id);
		StringBuffer result = new StringBuffer();
		result.append("<eip tcr_id =\"").append(trc_id).append("\">");
		if(eipDpBean !=null && eipDpBean.getEip_dps_tcr_id()>0){
			result.append("<dps_tcr_id>").append(eipDpBean.getEip_dps_tcr_id()).append("</dps_tcr_id>");
			result.append("<dps_share_usr_inf_ind>").append(eipDpBean.getEip_dps_share_usr_inf_ind()).append("</dps_share_usr_inf_ind>");
			result.append("<dps_upd_date_time>").append(eipDpBean.getEip_dps_upd_date_time()).append("</dps_upd_date_time>");
			result.append("<dps_upd_usr_id>").append(eipDpBean.getEip_dps_upd_usr_id()).append("</dps_upd_usr_id>");
		}
		result.append("</eip>");
		return result.toString();
	}
	
	/**
	 * 插入或更新 企业用户信息共享
	 * @param con
	 * @param modParam
	 * @param usr_id
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public void setDynamicPri(Connection con, EIPModuleParam modParam, String usr_id) throws SQLException, cwSysMessage{
		EnterpriseDynamicPriSetBean eipDpBean = EnterpriseInfoPortalDao.getDynamicPri(con, modParam.getEip_tcr_id());
		if(eipDpBean !=null && eipDpBean.getEip_dps_tcr_id()>0){
			EnterpriseInfoPortalDao.ins_updDynamicPr(con,modParam,usr_id,false);
		}else
			EnterpriseInfoPortalDao.ins_updDynamicPr(con,modParam,usr_id,true);
	}
}
