package com.cw.wizbank.cert;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AccessControlReqParam;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.instructor.InstructorDao;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

public class CertificateModule extends ServletModule {

	CertificateParam modParam;
	public static final String MOD_NAME = "certificate_mod";
	public static final String FSLINK_ADD_SUCC = "GEN001"; // 记录已添加成功
	public static final String FSLINK_DEL_SUCC = "GEN002"; // 记录已成功删除
	public static final String FSLINK_UPD_SUCC = "GEN003"; // 记录已修改成功
	public static final String FTN_CERT_MGT = "CERTIFICATE_MAIN";

	public CertificateModule() {
		super();
		modParam = new CertificateParam();
		param = modParam;
	}

	/**
	 * E-Certificate Management
	 * */
	public void process() throws SQLException, IOException, cwException {
		if (this.prof == null || this.prof.usr_ent_id == 0) { // 若还是未登录
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			Certificate cer = new Certificate();
	
			// e-certificate list
			if (modParam.getCmd().equalsIgnoreCase("get_certificate_list") || modParam.getCmd().equalsIgnoreCase("get_certificate_list_xml")) {
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CERTIFICATE_MAIN) ) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				
				if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_CERTIFICATE_MAIN) 
						&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
					if (AccessControlWZB.isRoleTcInd(prof.current_role) && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id)) {
						msgBox(MSG_ERROR, new cwSysMessage("TC017"), param.getUrl_success(), out);    
						return;
					}
				}
				
				// tree
				cwTree tree = new cwTree();
				String navTcTree = null;
				try {
					navTcTree = tree.genNavTrainingCenterTree(con, prof, false);
				} catch (cwSysMessage e) {
					CommonLog.error(e.getMessage(),e);
				}
				long cert_tc_id = modParam.getCert_tc_id();
				if (cert_tc_id < 0) {
					cert_tc_id = ViewTrainingCenter.getDefaultTc(con, prof);
				}

				String xml = cer.getAllCertitfcateAsXml(con,prof, false, cert_tc_id,modParam.getCur_page(),modParam.getPage_size());
				if (navTcTree != null) {
					xml += navTcTree;
				}
				resultXml = formatXML(xml, MOD_NAME);
			}
			// Add/edit 'e-Certificate' button prep
			else if (modParam.getCmd().equalsIgnoreCase("ins_upd_cert_prep") || modParam.getCmd().equalsIgnoreCase("ins_upd_cert_prep_xml")) {
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CERTIFICATE_MAIN) ) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				String xml = null;
				if (modParam.getCert_id() > 0) {// update
					String dirName = wizbini.cfgSysSetupadv.getFileUpload().getCertDir().getUrl();
					String saveDirPath = cwUtils.replaceSlashToHttp(cwUtils.getFileURL(dirName)+ modParam.getCert_id());
					xml = cer.getCertInfoXml(con, modParam, saveDirPath, prof);
				} else {
					StringBuffer tcXml = new StringBuffer();
					tcXml.append("<certInfo/>");
					if(modParam.getCert_tc_id() > 0) {
						String certTcXml = cer.getTcInfoByTcid(con, modParam.getCert_tc_id());
						tcXml.append(certTcXml);
					}
					xml = tcXml.toString();
				}
				resultXml = formatXML(xml, MOD_NAME);
			}
			// ins
			else if (modParam.getCmd().equalsIgnoreCase("ins_cert") || modParam.getCmd().equalsIgnoreCase("ins_cert_xml")) {
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CERTIFICATE_MAIN) ) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				//如果已经存在了cert_core
				if(cer.hasICertExist(con, modParam,wizbini.cfgSysSetupadv.isTcIndependent())){
					msgBox(MSG_ERROR, new cwSysMessage("lab_has_cert_core"), param.getUrl_failure(), out);	
					return;
				}
				String saveDirPath = "";
				long cert_id_;
				try {
    				if (modParam.getCert_id() > 0) {// upd
    					cert_id_ = modParam.getCert_id();
    					CertificateDao certDAO = new CertificateDao();
    					if (certDAO.isCertaeItem(con, modParam.getCert_id()))// 已被引用
    					{
    						if (modParam.getMod_status_ind().equalsIgnoreCase("OFF")) {
    							sysMsg = getErrorMsgByStatus(ServletModule.MSG_ERROR, "932", param.getUrl_failure());
    							return;
    						}
                            CertificateBean cert = CertificateDao.getCertByID(con, modParam.getCert_id());
    						if(modParam.getCert_tc_id()!=cert.getCfc_tcr_id()){
    							sysMsg = getErrorMsg("937", param.getUrl_failure());
    							return;
    						}
    					}
    					cer.updCert(con, prof, modParam);
    					saveDirPath = wizbini.getFileCertImgDirAbs() + dbUtils.SLASH + modParam.getCert_id();
    					sysMsg = getErrorMsg(FSLINK_UPD_SUCC, param.getUrl_success());
    				} else {// ins
    					long cert_id = cer.addCertcate(con, prof, modParam);
    					cert_id_ = cert_id;
    					if (cert_id > 0) {
    						sysMsg = getErrorMsg(FSLINK_ADD_SUCC, param.getUrl_success());
    					}
    					saveDirPath = wizbini.getFileCertImgDirAbs() + dbUtils.SLASH + cert_id;
    				}
				
				
					dbUtils.copyDir(tmpUploadPath, saveDirPath);
				} catch (qdbException e) {
					throw new cwException(e.getMessage());
				}catch (ParseException pe) {
                    throw new cwException(pe.getMessage());
                }
			} else if (modParam.getCmd().equalsIgnoreCase("del_cert") || modParam.getCmd().equalsIgnoreCase("del_cert_xml")) {
                
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CERTIFICATE_MAIN) ) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				CertificateDao certDAO = new CertificateDao();
//				if (certDAO.isCertStatus(con, modParam.getCert_id())) {
//					sysMsg = getErrorMsg("930", param.getUrl_failure());
//					return;
//				}
				String title = certDAO.canDelCertificate(con, modParam.getCert_id());
				if (certDAO.isCertaeItem(con, modParam.getCert_id()) && !cwUtils.isEmpty(title)) {
					//sysMsg = getErrorMsg("931", param.getUrl_failure());
					sysMsg = getErrorMsgByStatus(ServletModule.MSG_ERROR,"lab_aeitem_has_cert", title, param.getUrl_failure());
					return;
				}
				cer.delCert(con, prof, modParam);
				sysMsg = getErrorMsg(FSLINK_DEL_SUCC, param.getUrl_success());
			} else if (modParam.getCmd().equalsIgnoreCase("preview_cert") || modParam.getCmd().equalsIgnoreCase("download_cert")) {
//				if (!acl.hasCertPrivilege(con, prof.usr_ent_id, prof.current_role, FTN_CERT_MGT)) {
//					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
//					return;
//				}
				response.sendRedirect("../servlet/cert?cert_id=" + modParam.getCert_id() + 
						"&itm_id=" + modParam.getItm_id() + "&tkh_id=" + modParam.getTkh_id() 
						+ "&lan=" + prof.cur_lan);
			} else if (modParam.getCmd().equalsIgnoreCase("search_certificate_list") || modParam.getCmd().equalsIgnoreCase("search_certificate_list_xml")) {
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CERTIFICATE_MAIN) ) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				// tree
				cwTree tree = new cwTree();
				String navTcTree = null;
				try {
					navTcTree = tree.genNavTrainingCenterTree(con, prof, false);
				} catch (cwSysMessage e) {
					CommonLog.error(e.getMessage(),e);
				}
				long cert_tc_id = modParam.getCert_tc_id();
				if (cert_tc_id < 0) {
					cert_tc_id = ViewTrainingCenter.getDefaultTc(con, prof);
				}

				String xml = cer.getsearchCertitfcateAsXml(con,prof, modParam.getCert_status_sear(), modParam.getCur_page(),modParam.getPage_size(),cert_tc_id,modParam.getCert_core(),modParam.getCert_title());
				if (navTcTree != null) {
					xml += navTcTree;
				}
				resultXml = formatXML(xml, MOD_NAME);
			}	else {
				throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
			}
		}
	}

}
