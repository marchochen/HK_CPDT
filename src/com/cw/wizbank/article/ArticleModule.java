/**
 * 
 */
package com.cw.wizbank.article;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AccessControlReqParam;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.EncryptUtil;

/**
 * 
 * @author Leon.li 2013-5-24 4:41:20 message article modules
 */
public class ArticleModule extends ServletModule {

	ArticleModuleParam modParam;

	public static final String MOD_NAME = "article_module";
	public static final String FTN_article_MGT = "ARTICLE_MAIN";

	public static final String ADD_SUCCESS = "GEN001";
	public static final String DEL_SUCCESS = "GEN002";
	public static final String UPD_SUCCESS = "GEN003";
	
	//public static final String ADD_FAIL = "1033";
	public static final String UPD_FAIL = "GEN006";
	public static final String NOT_TO_VIEW = "1136";

	public static final int ADD_COURSE_EXPERIENCE_LENGTH = 2;

	public static final String DOWN_LIST_NAME = "1022";

	public ArticleModule() {
		super();
		modParam = new ArticleModuleParam();
		param = modParam;
	}

	@Override
	public void process() throws SQLException, IOException, cwException {
		if (this.prof == null || this.prof.usr_ent_id == 0) {
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			try {
				ArticleService artService = new ArticleService();
				// list
				if (param.getCmd().equalsIgnoreCase("get_article_list") || param.getCmd().equalsIgnoreCase("get_article_list_xml")) {
					
					if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN) 
							&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
						AccessControlReqParam urlp = new AccessControlReqParam(request, clientEnc, static_env.ENCODING);
						if (!ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id)) {
							msgBox(MSG_ERROR, new cwSysMessage("TC019"), urlp.url_success, out);    
							return;
						}
					}
					
					StringBuffer xml = new StringBuffer("");
					xml.append(artService.getAllarticle2Xml(con, modParam, prof, false));
					String nav_tree = null;
					cwTree tree = new cwTree();
					if (wizbini.cfgTcEnabled) {
						if (nav_tree == null) {
							nav_tree = tree.genNavTrainingCenterTree(con, prof, false);
						}
					}
					ArticleDao articleDao = new ArticleDao();
					xml.append("<TypeExist>").append(articleDao.checkArticleTypeExist( con, prof)).append("</TypeExist>");
					xml.append("<art_type>").append(modParam.getArt_type()).append("</art_type>");
					xml.append(articleDao.getArticleTypeXML(con, prof, modParam, true));
					xml.append("<art_keywords>").append(cwUtils.esc4XML(modParam.getArt_keywords())).append("</art_keywords>");
					xml.append(TrainingCenter.getTcrAsXml(con, prof.my_top_tc_id, prof.root_ent_id));
					xml.append(nav_tree);
		            if(wizbini.cfgTcEnabled) {
		               /// long tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
		            	long tcr_id = modParam.getArt_tcr_id();
		            	if (tcr_id == -1) {
		        	    	if (!prof.isLrnRole) {
		        	    		tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
		        	    	} else if (prof.isLrnRole) {
		        	    		tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
		        	    	}
		                }
		                DbTrainingCenter objTc = DbTrainingCenter.getInstance(con, tcr_id);
		                if(objTc != null) {
		                	StringBuffer xmlBuf = new StringBuffer();
		                    xmlBuf.append("<default_training_center id =\"").append(objTc.getTcr_id()).append("\">");
		                    xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
		                    xmlBuf.append("</default_training_center>");
		                    xml.append(xmlBuf.toString());
		                }
		            }
					// To provide XML data page
					resultXml = formatXML(xml.toString(), MOD_NAME);
					// view
				} else if (param.getCmd().equalsIgnoreCase("get_article_view") || param.getCmd().equalsIgnoreCase("get_article_view_xml")) {

					String xml = "";
					if (modParam.getArt_id() > 0) {
						xml = artService.getDetailXML(con, modParam.getArt_id());
					} else {
						// not exists
						sysMsg = getErrorMsg(NOT_TO_VIEW, param.getUrl_failure());
						return;
					}
					resultXml = formatXML(xml, MOD_NAME);
					// delete by id
				} else if (param.getCmd().equalsIgnoreCase("article_del_id") || param.getCmd().equalsIgnoreCase("article_del_id_xml")) {

					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN)) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
						return;
					}
					if (!"".equals(modParam.getArt_id_str())) {
						
						//记录重要功能操作日志  （在批量删除的时候需拆分id）
						List<ObjectActionLog> delList = new ArrayList<ObjectActionLog>();
						String[] artIds = modParam.getArt_id_str().split(",");
						for(int i = 0;i < artIds.length ; i++){
							String title = artService.getTitleById(con, Long.valueOf(artIds[i]));
							ObjectActionLog log = new ObjectActionLog(Long.valueOf(artIds[i]), 
									null,
									title,
									ObjectActionLog.OBJECT_TYPE_INFO,
									ObjectActionLog.OBJECT_ACTION_DEL,
									ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
									prof.getUsr_ent_id(),
									prof.getUsr_last_login_date(),
									prof.getIp()
							);
							delList.add(log);
						}
						artService.delArticleById(con, modParam.getArt_id_str());
						for(ObjectActionLog log : delList){
							SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
						}
						sysMsg = getErrorMsg(DEL_SUCCESS, param.getUrl_success());
							String saveDirPath = wizbini.getFileUploadArticleDirAbs()+ dbUtils.SLASH + modParam.getArt_id();
							File dir = new File(saveDirPath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							dbUtils.delFiles(saveDirPath);

						return;
					} else {
						sysMsg = getErrorMsg(NOT_TO_VIEW, param.getUrl_failure());
						return;
					}
					// before add
				} else if (param.getCmd().equalsIgnoreCase("get_article_prep") || param.getCmd().equalsIgnoreCase("get_article_prep_xml")) {

					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN)) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
						return;
					}
					String xml = "";
                    if(modParam.getEncryp_tart_id() != null && !modParam.getEncryp_tart_id().equals(""))
                    {
                    	modParam.setArt_id((int)EncryptUtil.cwnDecrypt(modParam.getEncryp_tart_id()));
                    }
					
					if (modParam.getArt_id() > 0) {
						xml = artService.getDetailXML(con, modParam.getArt_id());
			            if(wizbini.cfgTcEnabled) {
			                long tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
			                DbTrainingCenter objTc = DbTrainingCenter.getInstance(con, tcr_id);
			                if(objTc != null) {
			                	StringBuffer xmlBuf = new StringBuffer();
			                    xmlBuf.append("<default_training_center id =\"").append(objTc.getTcr_id()).append("\">");
			                    xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
			                    xmlBuf.append("</default_training_center>");
			                    xml += xmlBuf.toString();
			                }
			            }
					} else {
						xml = "<article/>";
						//把我的喜好的默認培訓中心找出來
				        boolean isTa = AccessControlWZB.isRoleTcInd(prof.current_role);
				        if(wizbini.cfgTcEnabled && isTa) {
				        	//long tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
				        	Long tcr_id = modParam.getTcr_id();
			                DbTrainingCenter objTc = DbTrainingCenter.getInstance(con, tcr_id);
			                if(objTc != null) {
			                	StringBuffer xmlBuf = new StringBuffer();
			                    xmlBuf.append("<default_training_center id =\"").append(objTc.getTcr_id()).append("\">");
			                    xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
			                    xmlBuf.append("</default_training_center>");
			                    xml += xmlBuf.toString();
			                }
				        }
					}
					ArticleDao articleDao = new ArticleDao();
					xml += articleDao.getArticleTypeXML(con, prof,modParam, true);
					
					resultXml = formatXML(xml, MOD_NAME);
					// to add
				} else if (param.getCmd().equalsIgnoreCase("add_article")) {

					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN)) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
				
						return;
					}
					String saveDirPath = "";
					
					if(null != modParam.getArt_content()){
						modParam.setArt_content(cwUtils.esc4Json(modParam.getArt_content()));
					}
					if(null != modParam.getAty_title()){
						modParam.setAty_title(cwUtils.esc4Json(modParam.getAty_title()));
					}
                    if(null != modParam.getArt_introduction()){
                    	modParam.setArt_introduction(cwUtils.esc4Json(modParam.getArt_introduction()));
					}

					int slash = modParam.getDefault_image().indexOf("\\");
					String slashType = "/";
					if(slash != -1)
					{
						slashType = "\\\\";
					}
					String[] originalPicDir = modParam.getDefault_image().split(slashType);
		 
				
					
					
					long artId = 0;
					if (modParam.getArt_id() > 0) { // 修改\
						artId = modParam.getArt_id();
						if (artService.isExists(con, modParam.getArt_id())) {
							artService.update(con, prof, modParam);
							ObjectActionLog log = new ObjectActionLog( (long)modParam.getArt_id(), 
									null,
									modParam.getArt_title(),
									ObjectActionLog.OBJECT_TYPE_INFO,
									ObjectActionLog.OBJECT_ACTION_UPD,
									ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
									prof.getUsr_ent_id(),
									prof.getUsr_last_login_date(),
									prof.getIp()
							);
							SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
							sysMsg = getErrorMsg(UPD_SUCCESS, param.getUrl_success());
						} else {
							// not exists
							sysMsg = getErrorMsg(UPD_FAIL, param.getUrl_failure());
							return;
						}	 
					} else {
						artId = artService.add(con, prof, modParam);
						if (artId > 0) {
							sysMsg = getErrorMsg(ADD_SUCCESS, param.getUrl_success());
							ObjectActionLog log = new ObjectActionLog(artId, 
									null,
									modParam.getArt_title(),
									ObjectActionLog.OBJECT_TYPE_INFO,
									ObjectActionLog.OBJECT_ACTION_ADD,
									ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
									prof.getUsr_ent_id(),
									prof.getUsr_last_login_date(),
									prof.getIp()
							);
							SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
						}	 
					}
					
					if(originalPicDir!= null && originalPicDir.length>=3)
					{
						saveDirPath = wizbini.getFileUploadArticleDirAbs() + dbUtils.SLASH + artId+ dbUtils.SLASH + originalPicDir[1] ;
					}
					else
					{
						saveDirPath = wizbini.getFileUploadArticleDirAbs() + dbUtils.SLASH + artId;
					}
 
					
					try {
						File dir = new File(saveDirPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						if(!"msg_icon_remain".equals(modParam.getArt_icon_select())){
							dbUtils.delFiles(saveDirPath);
						}						
						if("msg_icon_default".equals(modParam.getArt_icon_select())){
							String defaultPath = static_env.DOC_ROOT+ dbUtils.SLASH +static_env.DEFAULT_IMGLIG + dbUtils.SLASH +static_env.INI_ARC_DIR_UPLOAD_URL+ dbUtils.SLASH + modParam.getDefault_image();
							dbUtils.copyFile(defaultPath, saveDirPath);
						} else {
							dbUtils.copyDir(tmpUploadPath, saveDirPath);
						}						 
					 dbUtils.copyDir(tmpUploadPath, saveDirPath);
					} catch (qdbException e) {
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					}
					// oprate articleCooperationExperience
					if (artId > 0) {
						modParam.setArt_id((int) artId);
					}
					// to download
				} else if(param.getCmd().equalsIgnoreCase("get_article_type_maintain")){
					ArticleDao articleDao = new ArticleDao();
					String xml = articleDao.getArticleTypeXML(con, prof, modParam, false);
					resultXml = formatXML(xml, MOD_NAME);
				} else if(param.getCmd().equalsIgnoreCase("get_article_type_prep")){
					
					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN)) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
						return;
					}
					
					String xml = "";
					if(modParam.getEncryp_aty_id() != null && !modParam.getEncryp_aty_id().equals(""))
                    {
                    	modParam.setAty_id((int)EncryptUtil.cwnDecrypt(modParam.getEncryp_aty_id()));
                    }
					
					ArticleDao articleDao = new ArticleDao();
					if (modParam.getAty_id() > 0) {
						xml = articleDao.getArticleTypeDetail(con, modParam.getAty_id());
			            if(wizbini.cfgTcEnabled) {
			                long tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
			                DbTrainingCenter objTc = DbTrainingCenter.getInstance(con, tcr_id);
			                if(objTc != null) {
			                	StringBuffer xmlBuf = new StringBuffer();
			                    xmlBuf.append("<default_training_center id =\"").append(objTc.getTcr_id()).append("\">");
			                    xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
			                    xmlBuf.append("</default_training_center>");
			                    xml += xmlBuf.toString();
			                }
			            }
					} else {
						xml = "<aty/>";
					}
					// Bug 17729 - 资讯类型含有【&】这个符合时，点击修改，显示空白页
					xml = cwUtils.perl.substitute("s#&#&amp;#ig", xml);
					resultXml = formatXML(xml, MOD_NAME);
				} else if(param.getCmd().equalsIgnoreCase("add_article_type")){
					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN)) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
						return;
					}
					ArticleDao articleDao = new ArticleDao();
					if (modParam.getAty_id() > 0) { // 修改
						if (!articleDao.checkArticleTypeExist(con, prof, modParam.aty_title, modParam.getAty_id())) {
							articleDao.updateArticleType(con, prof, modParam);
							sysMsg = getErrorMsg(UPD_SUCCESS, param.getUrl_success());
						} else {
							// not exists
							sysMsg = getErrorMsg(UPD_FAIL, param.getUrl_failure());
							return;
						}
					} else { //
						if(!articleDao.checkArticleTypeExist(con, prof, modParam.aty_title, 0)){
							articleDao.insertArticleType(con, prof, modParam);
							sysMsg = getErrorMsg(ADD_SUCCESS, param.getUrl_success());
							
						}else{
                        	sysMsg = getErrorMsgByStatus("ERROR", "lan9", param.getUrl_failure());
                        	return;
						}
					}
				} else if(param.getCmd().equalsIgnoreCase("del_article_type")){
					if (!AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ARTICLE_MAIN)) {
						sysMsg = getErrorMsgByStatus(ServletModule.MSG_ERROR,"ACL002", param.getUrl_failure());
						return;
					}
					if (!"".equals(modParam.getAty_id_list())) {
						ArticleDao articleDao = new ArticleDao();
						if(articleDao.isArticleTypeUse(con, modParam.getAty_id_list())){
							sysMsg = getErrorMsgByStatus(ServletModule.MSG_ERROR,"lan10", param.getUrl_failure());
							return;
						}
						articleDao.deleteArticleType(con, modParam.getAty_id_list());
						sysMsg = getErrorMsg(DEL_SUCCESS, param.getUrl_success());
						return;
					} else {
						sysMsg = getErrorMsgByStatus(ServletModule.MSG_ERROR,NOT_TO_VIEW,  param.getUrl_failure());
						return;
					}
				} else {
					throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + param.getCmd());
				}
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
	}

}
