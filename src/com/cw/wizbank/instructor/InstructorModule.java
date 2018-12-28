package com.cw.wizbank.instructor;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class InstructorModule extends ServletModule {
	InstructorReqParam modParam;

	public static final String MOD_NAME = "instructor";

	public static final String INSTR_ADD_SUCC = "GEN001"; // 记录已添加成功
	public static final String INSTR_DEL_SUCC = "GEN002"; // 记录已成功删除
	public static final String INSTR_UPD_SUCC = "GEN003"; // 记录已修改成功
	public static final String INSTR_DEL_ERROR = "1066"; // 记录被引用，不能删除

	public InstructorModule() {
		super();
		modParam = new InstructorReqParam();
		param = modParam;
	}

	public void process() throws SQLException, IOException, cwException {
		try {
			if (this.prof == null || this.prof.usr_ent_id == 0) { // 若还是未登录
				throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
			} else {
				InstructorManager im = new InstructorManager();
				if (modParam.getCmd().equalsIgnoreCase("ins_upd_prep") || modParam.getCmd().equalsIgnoreCase("ins_upd_prep_xml")) {
					String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
					metaXML += "<default_usr_icon>" + cwUtils.esc4XmlJs(dbRegUser.getDefaultUsrPhotoDir(wizbini, prof.root_id)) + "</default_usr_icon>";
					metaXML += im.getInstrPropertiesXml(con, prof);
					String xml = "";
					if (modParam.getIti_ent_id() > 0) {
						xml += im.getInstrXml(con, wizbini, prof, modParam.getIti_ent_id());
					}
					if (modParam.getRef_ent_id() > 0) {
						xml += im.getIntInstrInsXml(con, wizbini, prof, modParam.getRef_ent_id());
					}
					resultXml = formatXML(xml, metaXML, MOD_NAME);
				} else if (modParam.getCmd().equalsIgnoreCase("ins_upd_exec")) {
					long iti_ent_id = im.insOrUpdInstructor(con, wizbini, prof, modParam);
					if (modParam.isUpd_iti_img()) {
						
						
						int slash = modParam.getDefault_image().indexOf("\\");
						String slashType = "/";
						if(slash != -1)
						{
							slashType = "\\\\";
						}
						String[] originalPicDir = modParam.getDefault_image().split(slashType);
						String saveDirPath  = "";
						if(originalPicDir!= null && originalPicDir.length>=3)
						{
							saveDirPath = wizbini.getFileUploadUsrDirAbs() + dbUtils.SLASH + iti_ent_id+ dbUtils.SLASH + originalPicDir[1] ;
						}
						else
						{
							saveDirPath = wizbini.getFileUploadUsrDirAbs() + dbUtils.SLASH + iti_ent_id;
						}
						
						
				
						if (!modParam.isRemain_photo_ind()) {
							dbUtils.delFiles(saveDirPath);
							
							if("use_default_image".equalsIgnoreCase(modParam.getIti_img_select())){
                        		String defaultPath = static_env.DOC_ROOT+ dbUtils.SLASH +static_env.DEFAULT_IMGLIG + dbUtils.SLASH +static_env.INI_INSTRUCTOR_DIR_UPLOAD_URL+ dbUtils.SLASH+ modParam.getDefault_image();
                        		dbUtils.copyFile(defaultPath, saveDirPath);
                        	} else if (modParam.getIti_img() != null && modParam.getIti_img().length() > 0) {
								dbUtils.moveDir(tmpUploadPath, saveDirPath);
							}
						}
					}
					if (modParam.getIti_ent_id() > 0) {
						sysMsg = getErrorMsg(INSTR_UPD_SUCC, param.getUrl_success());
					} else {
						String url = cwUtils.substituteURL(cwUtils.substituteURL(param.getUrl_success(), iti_ent_id), iti_ent_id);
						param.setUrl_success(url);
						sysMsg = getErrorMsg(INSTR_ADD_SUCC, param.getUrl_success());
					}
				} else if (modParam.getCmd().equalsIgnoreCase("inst_list") || modParam.getCmd().equalsIgnoreCase("inst_list_xml")) {
					String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
					metaXML += "<default_usr_icon>" + cwUtils.esc4XmlJs(dbRegUser.getDefaultUsrPhotoDir(wizbini, prof.root_id)) + "</default_usr_icon>";
					metaXML += im.getInstrPropertiesXml(con, prof);

					String result = im.instructorSearchAsXml(con, modParam, prof.my_top_tc_id,wizbini.cfgSysSetupadv.isTcIndependent());
					resultXml = formatXML(result, metaXML, MOD_NAME);
				} else if (modParam.getCmd().equalsIgnoreCase("inst_recommend")) {
					long iti_ent_id = modParam.getIti_ent_id();
					if (iti_ent_id > 0) {
						try{
							InstructorDao.updateRecommend(con, iti_ent_id, modParam.getIti_recommend());
						} catch(Exception e) {
							sysMsg = getErrorMsg(e.getMessage(), param.getUrl_success());
						}
					} 
                	response.sendRedirect(modParam.getUrl_success());
                	return;
				}
				else if (modParam.getCmd().equalsIgnoreCase("del_instr")) {
					String title = InstructorDao.canDelInstructor(con, modParam.getIti_ent_id());
					if (cwUtils.isEmpty(title)) {
						InstructorDao.del(con, modParam.getIti_ent_id());
						sysMsg = getErrorMsg(INSTR_DEL_SUCC, param.getUrl_success());
					} else {
						//sysMsg = getErrorMsg(INSTR_DEL_ERROR, title, param.getUrl_failure());
						sysMsg = getErrorMsgByStatus(ServletModule.MSG_ERROR,INSTR_DEL_ERROR, title, param.getUrl_failure());
					}
				} else if (modParam.getCmd().equalsIgnoreCase("view_instr") || modParam.getCmd().equalsIgnoreCase("view_instr_xml")) {
					String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
					metaXML += "<default_usr_icon>" + cwUtils.esc4XmlJs(dbRegUser.getDefaultUsrPhotoDir(wizbini, prof.root_id)) + "</default_usr_icon>";
					metaXML += im.getInstrPropertiesXml(con, prof);
					String isExcludes = request.getParameter("isExcludes");
					if(isExcludes == null || !"true".equals(isExcludes)) isExcludes = "false";
					metaXML += "<isExcludes>" + isExcludes + "</isExcludes>";
					String xml = "";
					if (modParam.getIti_ent_id() > 0) {
						xml += im.getInstrXml(con, wizbini, prof, modParam.getIti_ent_id());
					}
					resultXml = formatXML(xml, metaXML, MOD_NAME);
				} else if (modParam.getCmd().equalsIgnoreCase("ins_comment_view") || modParam.getCmd().equalsIgnoreCase("ins_comment_view_xml")) {
					String result =null;
					try {
						result = im.getinstructorCommentAsXml(con, modParam.getItc_itm_id(), modParam.getItc_iti_ent_id(), modParam.getCur_page(), modParam.getPage_size());
					} catch (Exception e) {
							throw new cwException(e.getMessage());
					}
					resultXml = formatXML(result, MOD_NAME);
				} else if (modParam.getCmd().equalsIgnoreCase("ins_course_view") || modParam.getCmd().equalsIgnoreCase("ins_course_view_xml")) {
					String result = "";
					if (modParam.getItc_iti_ent_id() > 0) {
						result += im.getInstrXml(con, wizbini, prof, modParam.getItc_iti_ent_id());
						result += im.getinstructorCourseAsXml(con, modParam.getItc_iti_ent_id(), modParam.getCur_page(), modParam.getPage_size());
					}
					resultXml = formatXML(result, MOD_NAME);
				} else {
					throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
				}
			}
		} catch (SQLException e) {
			try {
				CommonLog.error(e.getMessage(),e);
				con.rollback();
			} catch (SQLException sqlEx) {
				out.println("SQL error: " + sqlEx.getMessage());
				CommonLog.error("SQL error: " + sqlEx.getMessage(),sqlEx);
			}
		} catch (qdbException e) {
			try {
				CommonLog.error(e.getMessage(),e);
				con.rollback();
			} catch (SQLException sqlEx) {
				CommonLog.error("SQL error: " + sqlEx.getMessage(),e);
				out.println("SQL error: " + sqlEx.getMessage());
			}
		} catch (qdbErrMessage e) {
			try {
				CommonLog.error(e.getMessage(),e);
				con.rollback();
			} catch (SQLException sqlEx) {
				out.println("SQL error: " + sqlEx.getMessage());
				CommonLog.error("SQL error: " + sqlEx.getMessage(),sqlEx);
			}
		}
	}
}