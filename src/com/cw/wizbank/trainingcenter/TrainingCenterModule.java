/*
 * Created on 2004-9-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.trainingcenter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.xml.sax.SAXException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.know.dao.KnowCatalogDAO;
import com.cw.wizbank.know.db.KnowCatalogDB;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.exception.cwMessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.LabelContent;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TrainingCenterModule extends ServletModule {
	private static String TCMODULE = "tc_module";
	private static String USER_MANAGER = "user_manager";
	private static String END_XML = "_xml";
	private final static String SMSG_DEL_MSG = "GEN002";
	private final static String SMSG_UPD_MSG = "GEN003";
	private final static String SMSG_INS_MSG = "GEN004";
	final static String SMSG_UPDED_MSG = "GEN006";
	
	public void process() throws SQLException, cwException, IOException, qdbException, cwSysMessage, qdbErrMessage, cwMessageException {
		if (prof == null) {
			response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
		}
		PrintWriter out = response.getWriter();
		TrainingCenterReqParam urlp = new TrainingCenterReqParam(request, clientEnc, static_env.ENCODING);
		HttpSession sess = request.getSession(true);
		String profile = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
		if (bMultipart) {
			urlp.setMultiPart(multi);
		}
		urlp.common();
		try {
			if (urlp.cmd == null) {
				throw new cwException("Invalid Command");
			} else if (urlp.cmd.equalsIgnoreCase("tc_lst")
					|| urlp.cmd.equalsIgnoreCase("tc_lst_xml")) {
				TrainingCenter tc = new TrainingCenter();
				String dataXML = tc.getEffTcLst(con, prof.root_ent_id,  wizbini);
				dataXML = formatXML(dataXML, null, TCMODULE);
				outPutView(out, urlp, dataXML);
			} else if (urlp.cmd.equalsIgnoreCase("tc_all_lst")
					|| urlp.cmd.equalsIgnoreCase("tc_eff_lst_xml")) {
				TrainingCenter tc = new TrainingCenter();
				String dataXML = tc.getAllTcrLst(con, prof.root_ent_id);
				dataXML = formatXML(dataXML, null, TCMODULE);
				outPutView(out, urlp, dataXML);
			} else if (urlp.cmd.equalsIgnoreCase("tc_details")
					|| urlp.cmd.equalsIgnoreCase("tc_details_xml")
					|| urlp.cmd.equalsIgnoreCase("upd_tc_prep")
					|| urlp.cmd.equalsIgnoreCase("upd_tc_prep_xml")) {
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				TrainingCenter tc = new TrainingCenter();
				String dataXML = null;
				urlp.oneCenterId(prof);
				if(urlp.getObj() != null && urlp.getObj().tcr_id != 0) {
					if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)
						&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
						throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
					}
				}
				dataXML = tc.getTcDetailData(con, urlp.getObj(), prof, urlp);
				dataXML = formatXML(dataXML, profile, TCMODULE);
				outPutView(out, urlp, dataXML);
			} else if (urlp.cmd.equalsIgnoreCase("add_tc_prep")
					|| urlp.cmd.equalsIgnoreCase("add_tc_prep_xml")) {
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				TrainingCenter tc = new TrainingCenter();
				String dataXML = null;
				urlp.oneCenterId(prof);
				
				if(AccessControlWZB.isRoleTcInd(prof.current_role)){
					if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
						throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
					}
				}

				dataXML = tc.getTcInsPrep(con, prof.root_ent_id, urlp.getObj(),	false);
				dataXML = formatXML(dataXML, profile, TCMODULE);
				outPutView(out, urlp, dataXML);
			} else if (urlp.cmd.equalsIgnoreCase("add_tc_exe")
					|| urlp.cmd.equalsIgnoreCase("add_tc_ins_xml")) {
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				TrainingCenter tc = new TrainingCenter();
				urlp.addUpdNewTc(prof, false);
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.getObj().parent_tcr_id)) {
					urlp.url_failure = urlp.url_failure1;
					throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
				}
				if (urlp.getOfficers() == null) {
					throw new cwException("System error: please return");
				}
				DbTrainingCenter dbtc = tc.addNewTc(con, urlp);
				
				//LN模式  为新增的培训中心添加问答管理的临时目录
				if(WizbiniLoader.getInstance().cfgSysSetupadv.isTcIndependent()){
					KnowCatalogDAO  kc = new KnowCatalogDAO();
					KnowCatalogDB catalog = new KnowCatalogDB();
					catalog.setKca_tcr_id((int)dbtc.getTcr_id());
					catalog.setKca_title(LabelContent.get(prof.cur_lan, "lab_kb_unclassified"));
					catalog.setKca_type(KnowCatalogDAO.KCA_TYPE_CATALOG);
					catalog.setKca_public_ind(true);
					catalog.setKca_create_usr_id(prof.getUsr_ent_id()+"");
					catalog.setKca_update_usr_id(prof.getUsr_ent_id()+"");
					int knowCatalogId =  kc.ins(con, catalog);
				}
				
				con.commit();
				
				ObjectActionLog log = new ObjectActionLog(dbtc.getTcr_id(), 
						dbtc.getTcr_code(),
						dbtc.getTcr_title(),
						ObjectActionLog.OBJECT_TYPE_TC,
						ObjectActionLog.OBJECT_ACTION_ADD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						prof.getUsr_ent_id(),
						prof.getUsr_last_login_date(),
						prof.getIp()
				);
				SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
				cwSysMessage sms = new cwSysMessage(SMSG_INS_MSG);
				msgBox(MSG_STATUS, sms, urlp.url_success, out);
			} else if (urlp.cmd.equalsIgnoreCase("upd_tc_exe")) {
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				TrainingCenter tc = new TrainingCenter();
				DbTrainingCenter dbtc = urlp.addUpdNewTc(prof, true);
				long[] usg_lst = cwUtils.splitToLong((String)urlp.getParams().get("usg_lst"), TrainingCenterReqParam.deli);
				if (usg_lst != null && usg_lst.length > 0
						&& !dbRegUser.isUsgExists(con, usg_lst)) {
					throw new cwSysMessage("USG011");
				}
				if(AccessControlWZB.isRoleTcInd(prof.current_role)){
					if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.getObj().tcr_id)) {
						urlp.url_failure = urlp.url_failure1;
						throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
					}
				}

				String resultXML = tc.updTc(con, urlp, true);
				if (resultXML != null) {
					con.rollback();
					Vector resultVec = new Vector();
					resultVec.addElement(LangLabel.getValue(prof.label_lan, AcObjective.LABEL_ROL_TADM));
					resultVec.addElement(resultXML);
					cwSysMessage e = new cwSysMessage("USR030", resultVec);
					msgBox(MSG_ERROR, e, urlp.url_failure, out);
				} else {
					con.commit();
					
					ObjectActionLog log = new ObjectActionLog(dbtc.getTcr_id(), 
							dbtc.getTcr_code(),
							dbtc.getTcr_title(),
							ObjectActionLog.OBJECT_TYPE_TC,
							ObjectActionLog.OBJECT_ACTION_UPD,
							ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
							prof.getUsr_ent_id(),
							prof.getUsr_last_login_date(),
							prof.getIp()
					);
					SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
					cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
					msgBox(MSG_STATUS, sms, urlp.url_success, out);
				}
			} else if (urlp.cmd.equalsIgnoreCase("confirm_upd_tc_exe")) {
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				TrainingCenter tc = new TrainingCenter();
				urlp.addUpdNewTc(prof, true);
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
				}
				String resultXML = tc.updTc(con, urlp, false);
				con.commit();
				cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
				msgBox(MSG_STATUS, sms, urlp.url_success, out);
			} else if (urlp.cmd.equalsIgnoreCase("tc_del")) {
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				TrainingCenter tc = new TrainingCenter();
				urlp.oneCenterId(prof);
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
				}
				DbTrainingCenter dbtc = urlp.getObj();
				dbtc.get(con);
				urlp.getObj().setTcr_update_usr_id(prof.usr_id);
				tc.deleteTcData(con, urlp.getObj(), wizbini, prof);
				con.commit();
				ObjectActionLog log = new ObjectActionLog(dbtc.getTcr_id(), 
						dbtc.getTcr_code(),
						dbtc.getTcr_title(),
						ObjectActionLog.OBJECT_TYPE_TC,
						ObjectActionLog.OBJECT_ACTION_DEL,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						prof.getUsr_ent_id(),
						prof.getUsr_last_login_date(),
						prof.getIp()
				);
				SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
				cwSysMessage sms = new cwSysMessage(SMSG_DEL_MSG);
				msgBox(MSG_STATUS, sms, urlp.getUrl_suc(), out);
			} else if (urlp.cmd.equalsIgnoreCase("tc_officer_lst")
					|| urlp.cmd.equalsIgnoreCase("tc_officer_lst_xml")) {
				TrainingCenter tc = new TrainingCenter();
				urlp.oneCenterId(prof);
				urlp.pagination();
				String dataXML = tc.tcRelateOfficers(con, urlp, prof.root_ent_id);
				dataXML = formatXML(dataXML, null, USER_MANAGER);
				outPutView(out, urlp, dataXML);
			} else if (urlp.cmd.equalsIgnoreCase("set_ta_prep")
					|| urlp.cmd.equalsIgnoreCase("set_ta_prep_xml")) {
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				urlp.oneCenterId(prof);
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
				}
				StringBuffer sb = new StringBuffer();
				TrainingCenter tc = new TrainingCenter();
				sb.append(tc.getTcInsPrep(con, prof.root_ent_id, urlp.getObj(),	true));
				String dataXML = formatXML(sb.toString(), profile, TCMODULE);
				outPutView(out, urlp, dataXML);
			} else if (urlp.cmd.equalsIgnoreCase("set_ta_exec")) {
				TrainingCenter tc = new TrainingCenter();
				urlp.addUpdNewTc(prof, true);
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.getObj().getTcr_id())) {
					urlp.url_failure = urlp.url_failure1;
					throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
				}
				String resultXML = tc.setTa(con, urlp, prof.usr_id);
				if (resultXML != null) {
					con.rollback();
					Vector resultVec = new Vector();
					resultVec.addElement(LangLabel.getValue(prof.label_lan, AcObjective.LABEL_ROL_TADM));
					resultVec.addElement(resultXML);
					cwSysMessage e = new cwSysMessage("USR030", resultVec);
					msgBox(MSG_ERROR, e, urlp.url_failure, out);
				} else {
					con.commit();
					cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
					msgBox(MSG_STATUS, sms, urlp.url_success, out);
				}
			} else if (urlp.cmd.equalsIgnoreCase("set_tc_style_prep") || urlp.cmd.equalsIgnoreCase("set_tc_style_prep_xml")) {
				urlp.oneCenterId(prof);
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				if (!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
				}
				String result = TrainingCenter.getTcStyleXml(con, prof, urlp, wizbini);
				String dataXML = formatXML(result, profile, TCMODULE);
				outPutView(out, urlp, dataXML);
			} else if (urlp.cmd.equalsIgnoreCase("set_tc_style_exec")) {
				urlp.oneCenterId(prof);
				AcTrainingCenter actc = new AcTrainingCenter(con);
				if (!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
					throw new cwSysMessage("ACL002");
				}
				if (!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage(TrainingCenter.NO_PERMISSION_MSG);
				}
				if (bMultipart) {
					String path = getTcStyleDir(urlp.style_lang, urlp.tcr_id);
					procUploadedFiles(path, tmpUploadPath);
				}
				TrainingCenter.setTcStyle(urlp, wizbini);
				cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
				msgBox(MSG_STATUS, sms, urlp.url_success, out);
			}
		} catch (cwSysMessage e) {
			cwSysMessage sms = new cwSysMessage(e.getId());
			msgBox(MSG_ERROR, sms, urlp.url_failure, out);
		}  catch (cwMessageException e) {
			cwMessageException sms = new cwMessageException(e.getId());
			msgBox(MSG_ERROR, sms, urlp.url_failure, out);
		} catch (SAXException saxe) {
			con.rollback();
			throw new IOException(saxe.getMessage());
		}
	}

	private void outPutView(Writer out, TrainingCenterReqParam urlp, String xml) throws cwException {
		if (urlp.cmd.endsWith(END_XML)) {
			static_env.outputXML((PrintWriter) out, xml);
		} else {
			generalAsHtml(xml, (PrintWriter) out, urlp.stylesheet);
		}
	}
	
	private String getTcStyleDir(String lang, long tcr_id) {
		String path = wizbini.getAppnRoot() + cwUtils.SLASH + wizbini.cfgSysSetupadv.getTcStyle().getCssDir();
		if (!path.endsWith(cwUtils.SLASH)) {
			path += cwUtils.SLASH;
		}
		path += tcr_id + cwUtils.SLASH + lang + cwUtils.SLASH;
		return path;
	}

	private void procUploadedFiles(String saveDirPath, String tmpSaveDirPath) throws cwException, IOException {
		try {
			if (!saveDirPath.endsWith(cwUtils.SLASH)) {
				saveDirPath += cwUtils.SLASH;
			}
			if (!tmpSaveDirPath.endsWith(cwUtils.SLASH)) {
				tmpSaveDirPath += cwUtils.SLASH;
			}
			dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
		} catch (qdbException e) {
			throw new cwException(e.getMessage());
		}
	}
}