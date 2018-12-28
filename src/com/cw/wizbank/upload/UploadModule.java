package com.cw.wizbank.upload;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.dataMigrate.imp.Del;
import com.cw.wizbank.dataMigrate.imp.DelUser;
import com.cw.wizbank.dataMigrate.imp.Imp;
import com.cw.wizbank.dataMigrate.imp.ImpUser;
import com.cw.wizbank.dataMigrate.imp.bean.ImportObject;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.scheduled.DeleteUserScheduler;
import com.cwn.wizbank.scheduled.ImportUserFromFile;
import com.cwn.wizbank.utils.CommonLog;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

//import com.cw.wizbank.dataMigrate.imp.ImpUser;

public class UploadModule extends ServletModule {

    public static final String MODULENAME = "Upload";
    
    public static final String SESS_UPLOAD_ID = "upload_id";
    public static final String SESS_UPLOAD_FAILED_LINE = "upload_failed_line";
    public static final String SESS_UPLOAD_DUPLICATED_LOGIN_ID_FAILED_LINE = "upload_duplicated_login_id_failed_line";
    public static final String SESS_UPLOAD_INVALID_BDAY_FAILED_LINE = "upload_invalid_bday_failed_line";
    public static final String SESS_UPLOAD_INVALID_GROUP_CODE_FAILED_LINE = "upload_invalid_group_code_failed_line";
    public static final String SESS_UPLOAD_INVALID_GRADE_CODE_FAILED_LINE = "upload_invalid_grade_code_failed_line";
    public static final String SESS_UPLOAD_INVALID_ROLE_FAILED_LINE = "upload_invalid_role_fail_line";
    public static final String SESS_UPLOAD_INVALID_ADDITIONAL_ID_LINE = "upload_invalid_additional_id_fail_line";
    public static final String SESS_UPLOAD_INVALID_GENDER_LINE = "upload_invalid_gender_fail_line";
    public static final String SESS_UPLOAD_OTHERS_FAILED_LINE = "upload_others_failed_line";
    public static final String SESS_UPLOAD_INVALID_COL_NAME = "upload_invalid_col_name";
    public static final String SESS_UPLOAD_NO_GROUP_CODE_NAME = "upload_no_grp_code_name_fail_line";
    public static final String SESS_UPLOAD_VALID_GROUP_CODE = "upload_valid_group_code";
    public static final String SESS_UPLOAD_VALID_GRADE_CODE = "upload_valid_grade_code";
    public static final String SESS_UPLOAD_SUCCESSFUL_CNT = "upload_successful_count";
    public static final String SESS_UPLOAD_FILEFORMAT = "upload_file_format";
    public static final String SESS_UPLOAD_LOG_RECORD = "import_log_record";
    // in vector, instead of xml format

    public static final String SESS_UPLOAD_COL = "upload_col";
    
    public static final String SESS_USR_TMP_FILE = "usr_tmp_file";
    public static final String SESS_UPLOADED_SRC_FILE = "src_file";
    public static final String SESS_UPLOADED_DESC = "upload_desc";
    public static final String SESS_TRY_TIMESTAMP = "try_timestamp";   
    public static Timestamp SYSTEM_TRY_TIMESTAMP = new Timestamp(0);
	private final static String DEFAULT_ENC = "UnicodeLittle";
    public static final String SESS_SUCCESS_USR_CNT = "success_upload_cnt";
    public static final String SESS_FAILURE_USR_CNT = "failure_upload_cnt";
    public static int ADD_USER_NUM = 0;
    
    public void process() throws SQLException, IOException, cwException{
        UploadReqParam urlp = null;
        urlp = new UploadReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();

        PrintWriter out = response.getWriter();
        try {
            HttpSession sess = request.getSession(false);
            // if all command need authorized users
            if (sess == null || prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
//                response.sendRedirect(url_relogin);
            } else {

                if( urlp.cmd.equalsIgnoreCase("upload_que") || urlp.cmd.equalsIgnoreCase("upload_que_xml") ) {
					urlp.uploadQue();
					String xml = null;
					File srcFile = new File(tmpUploadPath, urlp.src_filename);
					sess.setAttribute(SESS_UPLOADED_SRC_FILE, srcFile);
					try{
						if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI) ){
							MCQue mcq = new MCQue(con, static_env, prof);
							xml = mcq.uploadQue(srcFile, urlp.upload_desc, urlp.allow_update, wizbini);
						} else if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_FILLBLANK)) {
							FBQue fbq = new FBQue(con, static_env, prof);
							xml = fbq.uploadQue(srcFile, urlp.upload_desc, urlp.allow_update, wizbini);
						} else if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)) {                    	
							ESQue essay = new ESQue(con, static_env, prof);
							xml = essay.uploadQue(srcFile, urlp.upload_desc, dbQuestion.QUE_TYPE_ESSAY, urlp.allow_update, wizbini);
						} else if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
							ESQue essay = new ESQue(con, static_env, prof);
							xml = essay.uploadQue(srcFile, urlp.upload_desc, dbQuestion.QUE_TYPE_ESSAY_2, urlp.allow_update, wizbini);
						} else if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING) ){
							MTQue mtq = new MTQue(con, static_env, prof);
							xml = mtq.uploadQue(srcFile, urlp.upload_desc, urlp.allow_update, wizbini);
                        } else if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_TRUEFALSE ) ){
                            TFQue tfq = new TFQue(con, static_env, prof);
                            xml = tfq.uploadQue(srcFile, urlp.upload_desc, urlp.allow_update, wizbini);
                        } else if( urlp.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_FSC ) ){
                            SCQue fscq = new SCQue(con, static_env, prof);
                            xml = fscq.uploadQue(srcFile, urlp.upload_desc, urlp.allow_update, dbQuestion.RES_SUBTYPE_FSC, wizbini);
                        } else if( urlp.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_DSC ) ){
                            SCQue dscq = new SCQue(con, static_env, prof);
                            xml = dscq.uploadQue(srcFile, urlp.upload_desc, urlp.allow_update, dbQuestion.RES_SUBTYPE_DSC, wizbini);
                        }
						con.commit();
					}catch(IOException e) {
						xml = "<invalid_file/>";
					}
                    
					xml = formatXML(xml, MODULENAME);
					if (urlp.cmd.equalsIgnoreCase("upload_que")) {
						generalAsHtml(xml, out, urlp.stylesheet);
					}else {
						static_env.outputXML(out, xml);
					}
					return;
				}
                else if (urlp.cmd.equalsIgnoreCase("get_upload_res_prep") || urlp.cmd.equalsIgnoreCase("get_upload_res_prep_xml")) {
                    StringBuffer xml = new StringBuffer();
                    xml.append("<upload_res>")
                    .append("<res_upload_limit>")
                    .append("<meta>")
                    .append(prof.asXML())
                    .append("<tc_enabled>").append(wizbini.cfgTcEnabled).append("</tc_enabled>")
                    .append("</meta>")
                    .append(wizbini.cfgSysSetupadv.getResBatchUpload().getMaxUploadCount())
                    .append("</res_upload_limit>")
                    .append("</upload_res>");
                    
                    if (urlp.cmd.equalsIgnoreCase("get_upload_res_prep")) {
                        generalAsHtml(xml.toString(), out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml.toString());
                    }
                    return;
                }

                 else if( urlp.cmd.equalsIgnoreCase("cook_que") || urlp.cmd.equalsIgnoreCase("cook_que_xml") ) {

                    urlp.cookQue();
                    DbUploadLog ulg = new DbUploadLog();
                    ulg.ulg_id = urlp.ulg_id;
                    boolean bExist = ulg.get(con);
                    
                    if (!bExist)
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_ULG_RECROD);

                    if (!ulg.ulg_status.equals(DbUploadLog.STATUS_PENDING)) {
                        throw new cwSysMessage("GEN000");
                    }
                    
                    String xml = null;
					if (ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI)) {
						MCQue mcq = new MCQue(con, static_env, prof);
						xml = mcq.save2DB(ulg, prof, wizbini.cfgTcEnabled);                        
					} else if( ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_FILLBLANK)) {
						FBQue fbq = new FBQue(con, static_env, prof);
						xml = fbq.save2DB(ulg, prof, wizbini.cfgTcEnabled);
					} else if( ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)) {
						ESQue essay = new ESQue(con, static_env, prof);
						xml = essay.save2DB(ulg, prof, wizbini.cfgTcEnabled);
					} else if( ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
						ESQue essay = new ESQue(con, static_env, prof);
						xml = essay.save2DB(ulg, prof, wizbini.cfgTcEnabled);
					} else if (ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING)) {
						MTQue mtq = new MTQue(con, static_env, prof);
						xml = mtq.save2DB(ulg, prof, wizbini.cfgTcEnabled);
					} else if (ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_TRUEFALSE )) {
						TFQue tfq = new TFQue(con, static_env, prof);
						xml = tfq.save2DB(ulg, prof, wizbini.cfgTcEnabled);                        
                    } else if( ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_FSC ) ){
                        SCQue fscq = new SCQue(con, static_env, prof);
                        xml = fscq.save2DB(ulg, prof, dbQuestion.RES_SUBTYPE_FSC, wizbini.cfgTcEnabled);
                    } else if( ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_DSC ) ){
                        SCQue dscq = new SCQue(con, static_env, prof);
                        xml = dscq.save2DB(ulg, prof, dbQuestion.RES_SUBTYPE_DSC, wizbini.cfgTcEnabled);
                    }
                    con.commit();
                    //msgBox(ServletModule.MSG_STATUS, new cwSysMessage(UploadUtils.SMSG_ULG_SAVE_QUE_SUCCESS, Integer.toString(cnt)), urlp.url_success, out);
                    UploadUtils.saveUploadedFile(static_env, UploadUtils.QUE, ulg.ulg_id, (File)sess.getAttribute(SESS_UPLOADED_SRC_FILE));
                    sess.removeAttribute(SESS_UPLOADED_SRC_FILE);
                    
                    xml = formatXML(xml, MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("cook_que")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }

                    return;
                } 
                else if( urlp.cmd.equalsIgnoreCase("cancel_cook_que") ) {

                    urlp.cookQue();
                    DbUploadLog ulg = new DbUploadLog();
                    ulg.ulg_id = urlp.ulg_id;
                    boolean bExist = ulg.get(con);
                    
                    if (!bExist)
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_ULG_RECROD);

                    if (!ulg.ulg_status.equals(DbUploadLog.STATUS_PENDING)) {
                        throw new cwSysMessage("GEN000");
                    }

                    RawQuestion.cancelUpload(con, urlp.ulg_id);

                    con.commit();
                    response.sendRedirect(urlp.url_success);

                } 

                else if(urlp.cmd.equalsIgnoreCase("get_que_log_history") || urlp.cmd.equalsIgnoreCase("get_que_log_history_xml") ) {
                    
					urlp.getQueLogHistory();
					sess = request.getSession(true);
					String xml = formatXML(UploadUtils.getHistoryXML(con, sess, urlp.que_type, urlp.log_process, urlp.cwPage, static_env), "que_log");
					if( urlp.cmd.equalsIgnoreCase("get_que_log_history") )
						generalAsHtml(xml, out, urlp.stylesheet);
					else
						static_env.outputXML(out, xml);


                }                   
                else if( urlp.cmd.equalsIgnoreCase("upload_user_prep") || urlp.cmd.equalsIgnoreCase("upload_user_prep_xml") ) {
                	StringBuffer xml_ = new StringBuffer();
                	xml_.append("<max_upload_count>" + wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount() + "</max_upload_count>");
					xml_.append("<spawn_threshold>" + wizbini.cfgSysSetupadv.getUserBatchUpload().getSpawnThreshold() + "</spawn_threshold>");                   
                    String xml = formatXML(xml_.toString(), MODULENAME);

                    if( urlp.cmd.equalsIgnoreCase("upload_user_prep") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);	
                }
                else if( urlp.cmd.equalsIgnoreCase("upload_del_user_prep") || urlp.cmd.equalsIgnoreCase("upload_del_user_prep_xml") ) {
                    StringBuffer xml_ = new StringBuffer();
                    xml_.append("<max_upload_count>" + wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount() + "</max_upload_count>");
                    xml_.append("<spawn_threshold>" + wizbini.cfgSysSetupadv.getUserBatchUpload().getSpawnThreshold() + "</spawn_threshold>");
                    String xml = formatXML(xml_.toString(), MODULENAME);

                    if( urlp.cmd.equalsIgnoreCase("upload_del_user_prep") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);
                }

                else if( urlp.cmd.equalsIgnoreCase("upload_user") || urlp.cmd.equalsIgnoreCase("upload_user_xml") ) {
                    if (!bMultipart) {
                        throw new cwSysMessage("GEN000");
                    }
                    //上传预览视图
                    int previewSize = 20;
                    urlp.uploadUser();

                    String newFileName = urlp.src_filename;
                    File srcFile = new File(tmpUploadPath, newFileName);
                    if (!srcFile.exists())
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);

                    Timestamp curTime = cwSQL.getTime(con);
                    SYSTEM_TRY_TIMESTAMP = curTime;

                    //UploadUser uploadUser = new UploadUser(con, static_env.ENCODING, static_env.INI_PATH, prof, static_env.DEFAULT_GRADE_ID, static_env.UPLOAD_USER_PWD_ENABLE, static_env.UPLOAD_USER_DISTINCT_CHECK_ENABLE); 
//                    UploadUser uploadUser = new UploadUser(con, DEFAULT_ENC, static_env.INI_PATH, prof, static_env.DEFAULT_GRADE_ID, static_env.UPLOAD_USER_PWD_ENABLE, !urlp.dbIlg.ilg_dup_data_update_ind); 
//
//                    
//                    //解析文件
//                   Vector vtUserRecord = uploadUser.parseFile(srcFile, wizbini);
//                    
//                    //获取允许用户的最大数
//                    String allowUserMaxSize = null;
//                   /*
//                    if(!"".equals(allowUserMaxSize.trim())){
//                    	int maxSize = Integer.parseInt(allowUserMaxSize);
//                    	int totalNum = dbRegUser.getUserTotalNum(con);
//                    	if(ADD_USER_NUM > 0 && (maxSize <= totalNum || maxSize < (totalNum + ADD_USER_NUM))){
//                    		int userNum = maxSize - totalNum;
//                    		if(userNum > 0){
//                    			throw new cwSysMessage("USG013", String.valueOf(maxSize - totalNum));
//                    		}else {
//                    			throw new cwSysMessage("USG015", String.valueOf(maxSize - totalNum));
//                    		}
//                    	}
//                    }*/
//                    
//                    	//获取允许用户的最大数
//                       EnterpriseInfoPortalBean eipBean=null;
//                       if(wizbini.cfgSysSetupadv.isTcIndependent())
//                         eipBean=EnterpriseInfoPortalDao.getEipByTcrID(con, prof.my_top_tc_id);       
//                    	if(eipBean==null||prof.current_role.startsWith("ADM") || !wizbini.cfgSysSetupadv.isTcIndependent() || prof.my_top_tc_id ==DbTrainingCenter.getSuperTcId ( con, prof.root_ent_id) ) {
//                        	allowUserMaxSize = wizbini.cfgSysSetup.getAllowUserMaxSize();
//                        } else {
//                            
//            				allowUserMaxSize = "" + eipBean.getEip_account_num();
//                        }      
//
//                    	//如果允许用户最大数不等于空
//            			if (!"".equals(allowUserMaxSize.trim())) {
//            				int maxSize = Integer.parseInt(allowUserMaxSize);
//                         	long totalNum = 0;
//                        	if(eipBean==null||prof.current_role.startsWith("ADM") || !wizbini.cfgSysSetupadv.isTcIndependent() || prof.my_top_tc_id ==DbTrainingCenter.getSuperTcId ( con, prof.root_ent_id) ) {
//                        		totalNum = dbRegUser.getUserTotalNum(con);
//                        	} else {
//                        		totalNum = eipBean.getAccount_used();
//                        	}
//                        	if(ADD_USER_NUM > 0 && (maxSize <= totalNum || maxSize < (totalNum + ADD_USER_NUM))){
//                        		long userNum = maxSize - totalNum;
//                        		if(userNum > 0 && ADD_USER_NUM > userNum){
//                        			throw new cwSysMessage("USG013", String.valueOf(maxSize - totalNum));
//                        		} else{
//                                    if(eipBean!=null&&!prof.current_role.startsWith("ADM") && wizbini.cfgSysSetupadv.isTcIndependent() && prof.my_top_tc_id !=DbTrainingCenter.getSuperTcId ( con, prof.root_ent_id)) {
//                                    	String cur_lan = prof.cur_lan;
//                                    	String message = LangLabel.getValue(cur_lan, "LN005");
//                                    	message	+= "(" + LangLabel.getValue(cur_lan, "LN006");
//                                    	message	+= "/"+ LangLabel.getValue(cur_lan, "LN007");
//                                    	message	+= "):"+ allowUserMaxSize +"("+eipBean.getAccount_used()+"/"+ userNum +")";
//                            			throw new cwSysMessage("LN326" , message);
//                                    } else
//                        			throw new cwSysMessage("USG015", String.valueOf(maxSize - totalNum));
//                        		}
//                        	}
//            			}
//        				
//            			
//            		//获取上传限制用户最大数
//                    int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
//                    //如果大于上传用户最大数
//                    if (vtUserRecord.size() > maxUploadCount){
//                        throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(maxUploadCount).toString());
//                    }
//                    Vector vtPreviewUserRecord = new Vector();
//                   
//                   //如果获取文件的记录大于视图的记录数，则取当前设置的显示视图的数量
//                    if(vtUserRecord.size() > previewSize) {
//                        for(int i=0; i<previewSize; i++) {
//                            vtPreviewUserRecord.addElement(vtUserRecord.elementAt(i));
//                        }
//                    }
//
//                    //临时文件
//                    int tmpFilename = (new Random()).nextInt();
//                    if (tmpFilename < 0){
//                        tmpFilename *= -1;    
//                    }
//                    //设置一个临时xml
//                    String strTmpFilename = tmpFilename + ".xml";
//                    //设置一个临时xml
//                    String strTmpPreviewFilename = tmpFilename + "_preview.xml";
//                    //在上传文件目录生成一个临时的XML
//                    File tmpFile = new File(wizbini.getFileUploadTmpDirAbs(), strTmpFilename);
//                    uploadUser.genIMSEnterpriseXML(vtUserRecord, tmpFile.getAbsolutePath());
//
//                    File tmpPreviewFile = null;
//                    if(vtUserRecord.size() > previewSize) {
//                        tmpPreviewFile = new File(wizbini.getFileUploadTmpDirAbs(), strTmpPreviewFilename);
//                        uploadUser.genIMSEnterpriseXML(vtPreviewUserRecord, tmpPreviewFile.getAbsolutePath());
//                    } else {
//                        tmpPreviewFile = tmpFile;
//                    }
//
//

//
//                    StringBuffer xml = uploadUser.uploadPreview(con, tmpPreviewFile.getAbsolutePath(), static_env.ENCODING, wizbini);
                    Imp imp = new ImpUser(con, wizbini, tmpUploadPath + cwUtils.SLASH, srcFile.getAbsolutePath(), urlp.usr_pwd_need_change_ind, urlp.identical_usr_no_import, urlp.oldusr_pwd_need_update_ind);
                    String xml = "";
                    Vector<ImportObject> record = new Vector<ImportObject>();
                    try {
                        Imp.ImportStatus importStatus = imp.initLog(prof);
                        record = imp.parseFile(srcFile, importStatus, prof);
                        xml = imp.readLogToXml(srcFile, static_env, importStatus, urlp.usr_pwd_need_change_ind, urlp.identical_usr_no_import, urlp.oldusr_pwd_need_update_ind);
                    } catch (Exception e) {
                        CommonLog.error(e.getMessage(), e);
                        throw new cwSysMessage(e.getMessage());
                    }
                    //获取上传限制用户最大数
                    int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
                    //如果大于上传用户最大数
                    if (record.size() > maxUploadCount) {
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(maxUploadCount).toString());
                    }
                    String final_xml = formatXML(xml, MODULENAME);
                    sess.setAttribute(SESS_TRY_TIMESTAMP, curTime);
//                  sess.setAttribute(SESS_UPLOAD_COL, uploadUser.getUsedCol());
//                  sess.setAttribute(SESS_USR_TMP_FILE, tmpFile);
                    //把检查的通过的数量设置在Session里面
//                  sess.setAttribute(SESS_UPLOAD_SUCCESSFUL_CNT, new Integer(record.size()));
//                  sess.setAttribute(SESS_UPLOAD_LOG_RECORD, urlp.dbIlg);
                    //把数据源文件设置在Session里面
                    sess.setAttribute(SESS_UPLOADED_SRC_FILE, srcFile);
                    sess.setAttribute(SESS_UPLOADED_DESC, urlp.dbIlg.ilg_desc);
                    if (urlp.cmd.equalsIgnoreCase("upload_user")) {
                        generalAsHtml(final_xml, out, urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, final_xml);
                    }
                }
                    else if( urlp.cmd.equalsIgnoreCase("upload_del_user") || urlp.cmd.equalsIgnoreCase("upload_del_user_xml") ) {
                        if (!bMultipart) {
                            throw new cwSysMessage("GEN000");
                        }
                        //上传预览视图
                        int previewSize = 20;
                        urlp.uploadUser();

                        String newFileName = urlp.src_filename;
                        File srcFile = new File (tmpUploadPath, newFileName);
                        if (!srcFile.exists())
                            throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);

                        Timestamp curTime = cwSQL.getTime(con);
                        SYSTEM_TRY_TIMESTAMP = curTime;

                        Del del=new DelUser(con,wizbini,tmpUploadPath+cwUtils.SLASH,srcFile.getAbsolutePath());
                        String xml="";
                        Vector<ImportObject> record=new Vector<ImportObject>();
                        try{
                            Del.ImportStatus importStatus=del.initLog(prof);
                            record=del.parseFile(srcFile,importStatus,prof);
                            xml=del.readLogToXml(srcFile,static_env,importStatus,urlp.usr_pwd_need_change_ind,urlp.identical_usr_no_import,urlp.oldusr_pwd_need_update_ind);
                        }catch(Exception e){
                            CommonLog.error(e.getMessage(),e);
                            throw new cwSysMessage(e.getMessage());
                        }
                        //获取上传限制用户最大数
                        int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
                        //如果大于上传用户最大数
                        if (record.size() > maxUploadCount){
                            throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(maxUploadCount).toString());
                        }
                        String final_xml = formatXML(xml, MODULENAME);
                        sess.setAttribute(SESS_TRY_TIMESTAMP, curTime);
//                  sess.setAttribute(SESS_UPLOAD_COL, uploadUser.getUsedCol());
//                  sess.setAttribute(SESS_USR_TMP_FILE, tmpFile);
                        //把检查的通过的数量设置在Session里面
//                  sess.setAttribute(SESS_UPLOAD_SUCCESSFUL_CNT, new Integer(record.size()));
//                  sess.setAttribute(SESS_UPLOAD_LOG_RECORD, urlp.dbIlg);
                        //把数据源文件设置在Session里面
                        sess.setAttribute(SESS_UPLOADED_SRC_FILE, srcFile);
                        sess.setAttribute(SESS_UPLOADED_DESC, urlp.dbIlg.ilg_desc);
                        if (urlp.cmd.equalsIgnoreCase("upload_del_user")) {
                            generalAsHtml(final_xml, out, urlp.stylesheet);
                        }else {
                            static_env.outputXML(out, final_xml);
                        }
                }else if( urlp.cmd.equalsIgnoreCase("upload_enrollment") || urlp.cmd.equalsIgnoreCase("upload_enrollment_xml")) {
                    
                    if (!bMultipart) {
                        throw new cwSysMessage("GEN000");
                    }
                    urlp.uploadEnrollment();
                    int previewSize = wizbini.cfgSysSetupadv.getEnrollmentBatchUpload().getPreviewSize();
                    int maxUploadCount = wizbini.cfgSysSetupadv.getEnrollmentBatchUpload().getMaxUploadCount();

                    String newFileName = getNewFilename(urlp.src_filename, sess);
                    File srcFile = new File (tmpUploadPath, newFileName);
                    if (!srcFile.exists())
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);

                    Timestamp curTime = cwSQL.getTime(con);
                    SYSTEM_TRY_TIMESTAMP = curTime;

                    UploadEnrollment uploadEnrollment = new UploadEnrollment(con, DEFAULT_ENC, static_env.INI_PATH, static_env.INI_DIR_UPLOAD_TMP, prof, urlp.upload_type); 
                    Vector vtEnrollment = uploadEnrollment.parseFile(con, srcFile.getAbsolutePath(), prof.usr_ent_id, prof.current_role, urlp.itm_id);
                    if (vtEnrollment.size() > maxUploadCount){
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(maxUploadCount).toString());
                    }
                    uploadEnrollment.upload_count = vtEnrollment.size();
                    Vector vtPreviewEnrollRecord = new Vector();
                    if(vtEnrollment.size() > previewSize) {
                        for(int i=0; i<previewSize; i++) {
                            vtPreviewEnrollRecord.addElement(vtEnrollment.elementAt(i));
                        }
                    }
                    int tmpFilename = (new Random()).nextInt();
                    if (tmpFilename < 0){
                        tmpFilename *= -1;    
                    }
                    String strTmpFilename = tmpFilename + ".xml";
                    String strTmpPreviewFilename = tmpFilename + "_preview.xml";

                    File tmpFile = new File(static_env.INI_DIR_UPLOAD_TMP, strTmpFilename);
                    uploadEnrollment.genIMSEnterpriseXML(vtEnrollment, tmpFile.getAbsolutePath());

                    File tmpPreviewFile = null;
                    if(vtEnrollment.size() > previewSize) {
                        tmpPreviewFile = new File(wizbini.getFileUploadTmpDirAbs(), strTmpPreviewFilename);
                        uploadEnrollment.genIMSEnterpriseXML(vtPreviewEnrollRecord, tmpPreviewFile.getAbsolutePath());
                    } else {
                        tmpPreviewFile = tmpFile;
                    }

                    sess.setAttribute(SESS_TRY_TIMESTAMP, curTime);
                    sess.setAttribute(SESS_USR_TMP_FILE, tmpFile);
                    sess.setAttribute(SESS_UPLOADED_SRC_FILE, srcFile);
                    sess.setAttribute(SESS_UPLOAD_LOG_RECORD, urlp.dbIlg);
                    sess.setAttribute(SESS_UPLOAD_SUCCESSFUL_CNT, new Integer(vtEnrollment.size()));
                    
                    String xml = formatXML(uploadEnrollment.uploadPreview(con, tmpPreviewFile, urlp.itm_id, prof.root_ent_id,prof).toString(), MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("upload_enrollment")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }

//                    response.sendRedirect(urlp.url_success);
//                    String zipFilePath = uploadEnrollment.getZipFile();
//                    response.sendRedirect(cwUtils.getRealPath(request, zipFilePath));                  
                }else if( urlp.cmd.equalsIgnoreCase("preview_all_enrollment") || urlp.cmd.equalsIgnoreCase("preview_all_enrollment_xml")) {
                    urlp.uploadUser();
                               
                    File tmpFile = (File)sess.getAttribute(SESS_USR_TMP_FILE);
                    if (tmpFile == null){
                        throw new cwException("can't find the import file");    
                    }
                    UploadEnrollment uploadEnrollment = new UploadEnrollment(con, DEFAULT_ENC, static_env.INI_PATH, static_env.INI_DIR_UPLOAD_TMP, prof, urlp.upload_type); 
                    
                    String xml = formatXML(uploadEnrollment.uploadPreview(con, tmpFile, urlp.itm_id, prof.root_ent_id, prof).toString(), MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("preview_all_enrollment")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }

                }else if( urlp.cmd.equalsIgnoreCase("cook_enrollment") ) {
         
                    urlp.uploadEnrollment();

                    Timestamp sess_try_timestamp = (Timestamp)sess.getAttribute(SESS_TRY_TIMESTAMP);
                    if (!sess_try_timestamp.equals(SYSTEM_TRY_TIMESTAMP)){
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_TIMESTAMP);
                    }                    
                    File tmpFile = (File)sess.getAttribute(SESS_USR_TMP_FILE);
                    if (tmpFile == null){
                        throw new cwException("can't find the import file");    
                    }
                    int successful_cnt = ((Integer)sess.getAttribute(SESS_UPLOAD_SUCCESSFUL_CNT)).intValue();
                    int spawnThreshold = wizbini.cfgSysSetupadv.getEnrollmentBatchUpload().getSpawnThreshold();
                    if (successful_cnt > spawnThreshold) {
                        UploadEnrollmentThread importThread = 
                            new UploadEnrollmentThread(wizbini,sess, static_env, prof,
                                (File) sess.getAttribute(SESS_UPLOADED_SRC_FILE),
                                tmpFile, urlp.itm_id,  urlp.upload_type, con);
                        
                        StringBuffer xml = importThread.getXML(con);
                        String final_xml = formatXML(xml.toString(), "ims_log");
                        
                        if (urlp.cmd.equalsIgnoreCase("cook_enrollment")) {
                            generalAsHtml(final_xml, out, urlp.stylesheet);
                        } else {
                            static_env.outputXML(out, final_xml);
                        }
                        importThread.start();
                        
                    }else{
                    DbIMSLog dbIlg = (DbIMSLog)sess.getAttribute(SESS_UPLOAD_LOG_RECORD);
                    dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_ENROLLMENT;
                    dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
                        dbIlg.ilg_target_id = IMSLog.ITM + urlp.itm_id;
                        IMSLog ilg = new IMSLog();
                    ilg.ins(con, prof, dbIlg);
                    ilg.saveUploadedFile(static_env, dbIlg.ilg_id, (File)sess.getAttribute(SESS_UPLOADED_SRC_FILE));

                    UploadEnrollment uploadEnrollment = new UploadEnrollment(con, static_env.ENCODING, static_env.INI_PATH, static_env.INI_DIR_UPLOAD_TMP, prof, urlp.upload_type); 
                        String xml = uploadEnrollment.cookEnrollment(tmpFile.getAbsolutePath(), static_env, dbIlg.ilg_id, wizbini, urlp.itm_id,prof);
                    String final_xml = formatXML(xml.toString(), "ims_log");
                        
                    if (urlp.cmd.equalsIgnoreCase("cook_enrollment")) {
                        generalAsHtml(final_xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, final_xml);
                        }
                    }
                }else if( urlp.cmd.equalsIgnoreCase("preview_all_user") || urlp.cmd.equalsIgnoreCase("preview_all_user_xml")) {
                    urlp.uploadUser();
                    
                    Timestamp sess_try_timestamp = (Timestamp)sess.getAttribute(SESS_TRY_TIMESTAMP);

                    Vector v_used_col = (Vector)sess.getAttribute(SESS_UPLOAD_COL);

                    if (sess_try_timestamp == null || !sess_try_timestamp.equals(SYSTEM_TRY_TIMESTAMP)
                    || v_used_col == null ){
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_TIMESTAMP);
                    }                    
                    File tmpFile = (File)sess.getAttribute(SESS_USR_TMP_FILE);
                    if (tmpFile == null){
                        throw new cwException("can't find the import file");    
                    }
                    UploadUser uploadUser = new UploadUser(con, static_env.ENCODING, static_env.INI_PATH, prof, static_env.DEFAULT_GRADE_ID, static_env.UPLOAD_USER_PWD_ENABLE, !urlp.dbIlg.ilg_dup_data_update_ind); 
                    StringBuffer xml = uploadUser.uploadPreview(con, tmpFile.getAbsolutePath(), static_env.ENCODING, wizbini);
                    String final_xml = formatXML(xml.toString(), MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("preview_all_user")) {
                        generalAsHtml(final_xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, final_xml);
                    }

                }else if( urlp.cmd.equalsIgnoreCase("cook_user") || urlp.cmd.equalsIgnoreCase("cook_user_xml") ) {
//long startTime = System.currentTimeMillis();
//                    urlp.uploadUser();
//
//                    Timestamp sess_try_timestamp = (Timestamp)sess.getAttribute(SESS_TRY_TIMESTAMP);
//
//                    Vector v_used_col = (Vector)sess.getAttribute(SESS_UPLOAD_COL);
//
////                    if (sess_try_timestamp == null || !sess_try_timestamp.equals(SYSTEM_TRY_TIMESTAMP)
////                    || v_used_col == null ){
////                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_TIMESTAMP);
////                    }                    
//                    File sourceFile = (File) sess.getAttribute(SESS_UPLOADED_SRC_FILE);
//                    if (sourceFile == null){
//                        throw new cwException("can't find the import file");    
//                    }
//
//                    int successful_cnt = ((Integer)sess.getAttribute(SESS_UPLOAD_SUCCESSFUL_CNT)).intValue();
//                    int spawnThreshold = wizbini.cfgSysSetupadv.getUserBatchUpload().getSpawnThreshold();
//
//                    
//                    //需要同步的条数超过配置文件的设置的条数就使用线程进行同步
//                    if (successful_cnt > spawnThreshold) {
//                    	UploadUserThread importThread = 
//                    		new UploadUserThread(wizbini,sess, static_env, prof,
//                    				sourceFile,
//								tmpFile, v_used_col, con);
//						
//						StringBuffer xml = importThread.getXML();
//						String final_xml = formatXML(xml.toString(), "ims_log");
//						
//						if (urlp.cmd.equalsIgnoreCase("cook_user")) {
//							generalAsHtml(final_xml, out, urlp.stylesheet);
//						} else {
//							static_env.outputXML(out, final_xml);
//						}
//						importThread.start();
//                    }
//                    else {
//	                    DbIMSLog dbIlg = (DbIMSLog)sess.getAttribute(SESS_UPLOAD_LOG_RECORD);
//	                    dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_USER;
//	                    dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
//	                    IMSLog ilg = new IMSLog();
//	                    ilg.ins(con, prof, dbIlg);
//	                    ilg.saveUploadedFile(static_env, dbIlg.ilg_id, (File)sess.getAttribute(SESS_UPLOADED_SRC_FILE));
//	                    UploadUser uploadUser = new UploadUser(con, static_env.ENCODING, static_env.INI_PATH, prof, static_env.DEFAULT_GRADE_ID, static_env.UPLOAD_USER_PWD_ENABLE, !dbIlg.ilg_dup_data_update_ind);
//	                    String xml = uploadUser.cookUser(tmpFile.getAbsolutePath(), static_env, dbIlg.ilg_id, v_used_col, wizbini, prof.usr_id);
        
                		//获取是否新用户登陆需要修改密码
                		urlp.cookQue();
                		
                		//获取上传的文件
                    	File uploadFile=(File)sess.getAttribute(SESS_UPLOADED_SRC_FILE);
                    	if(uploadFile==null){
                    		throw new cwSysMessage("can_not_find_file");
                    	}
                    	File toFolder=new File(static_env.DOC_ROOT+cwUtils.SLASH+static_env.LOG_FOLDER+cwUtils.SLASH+IMSLog.FOLDER_IMSLOG);
                    	if(!toFolder.exists()){
                    		toFolder.mkdir();
                    	}
                    	File toFile=new File(toFolder,uploadFile.getName());
                    	//设置参数到同步线程
                    	ImportUserFromFile.identicalUsrNoImport.put(prof, urlp.identical_usr_no_import);
                    	ImportUserFromFile.oldusrPwdNeedUpdateInd.put(prof, urlp.oldusr_pwd_need_update_ind);
                    	ImportUserFromFile.usrPwdNeedChangeInd.put(prof,urlp.usr_pwd_need_change_ind);
                    	ImportUserFromFile.uploadFile.put(prof,toFile.getName());
                    	ImportUserFromFile.uploadDesc.put(prof, sess.getAttribute(SESS_UPLOADED_DESC)==null?"":sess.getAttribute(SESS_UPLOADED_DESC).toString());
                    	sess.removeAttribute(SESS_UPLOADED_SRC_FILE);
                    	sess.removeAttribute(SESS_UPLOADED_DESC);
                    	//将上传文件剪切到日志的文件夹下
                    	dbUtils.moveFile(uploadFile.getPath(), toFile.getPath());	
                    	
                    	String xml="<data_import></data_import>";
	                    String final_xml = formatXML(xml.toString(), "ims_log");
	
	                    if (urlp.cmd.equalsIgnoreCase("cook_user")) {
	                        generalAsHtml(final_xml, out, urlp.stylesheet);
	                    }else {
	                        static_env.outputXML(out, final_xml);
	                    }
               }
                else if( urlp.cmd.equalsIgnoreCase("del_user") || urlp.cmd.equalsIgnoreCase("del_user_xml") ) {

                    //获取上传的文件
                    File uploadFile=(File)sess.getAttribute(SESS_UPLOADED_SRC_FILE);
                    if(uploadFile==null){
                        throw new cwSysMessage("can_not_find_file");
                    }
                    File toFolder=new File(static_env.DOC_ROOT+cwUtils.SLASH+static_env.LOG_FOLDER+cwUtils.SLASH+IMSLog.FOLDER_DEL_IMSLOG);
                    if(!toFolder.exists()){
                        toFolder.mkdir();
                    }
                    File toFile=new File(toFolder,uploadFile.getName());
                    //设置参数到同步线程
                    DeleteUserScheduler.uploadFile.put(prof,toFile.getName());
                    DeleteUserScheduler.uploadDesc.put(prof, sess.getAttribute(SESS_UPLOADED_DESC)==null?"":sess.getAttribute(SESS_UPLOADED_DESC).toString());
                    sess.removeAttribute(SESS_UPLOADED_SRC_FILE);
                    sess.removeAttribute(SESS_UPLOADED_DESC);
                    //将上传文件剪切到日志的文件夹下
                    dbUtils.moveFile(uploadFile.getPath(), toFile.getPath());

                    String xml="<data_import></data_import>";
                    String final_xml = formatXML(xml.toString(), "ims_log");

                    if (urlp.cmd.equalsIgnoreCase("del_user")) {
                        generalAsHtml(final_xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, final_xml);
                    }
                }
                else if( urlp.cmd.equalsIgnoreCase("test_random") ) {
                    urlp.random();

                    String random = UploadUtils.genPosDigit(6);
                    static_env.outputXML(out, random);


                } else if(urlp.cmd.equalsIgnoreCase("get_log_history") || urlp.cmd.equalsIgnoreCase("get_log_history_xml") ) {
                    
                    urlp.getLogHistory();
                    IMSLog ilg = new IMSLog();
                    sess = request.getSession(true);
                    String xml = formatXML(ilg.getHistoryXML(con, sess, static_env, null, urlp.log_type, urlp.cwPage, urlp.itm_id, prof), "ims_log");
                    if( urlp.cmd.equalsIgnoreCase("get_log_history") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);

                }
                else if(urlp.cmd.equalsIgnoreCase("get_del_log_history") || urlp.cmd.equalsIgnoreCase("get_del_log_history_xml") ) {

                    urlp.getLogHistory();
                    IMSLog ilg = new IMSLog();
                    IMSLog.DEL=true;
                    sess = request.getSession(true);
                    String xml = formatXML(ilg.getHistoryXML(con, sess, static_env, null, urlp.log_type, urlp.cwPage, urlp.itm_id, prof), "ims_log");
                    IMSLog.DEL=false;
                    if( urlp.cmd.equalsIgnoreCase("get_del_log_history") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);

                }else if (urlp.cmd.equals("get_instr") || urlp.cmd.equalsIgnoreCase("get_instr_xml")) {
                	urlp.getInstr();               	
                	if (urlp.instr_type == null || urlp.instr_type.equals("")) {
                		throw new cwException("Invalid instr type.");
                	}
                	                	
                	String xml = dbUtils.xmlHeader + "<Upload>" + ((prof != null) ? prof.asXML() : "") 
                			+ "<isTcIndependent>" + wizbini.cfgSysSetupadv.isTcIndependent() + "</isTcIndependent>"
                	+ "<instr type=\"" + urlp.instr_type+ "\" mod_type=\"" + urlp.mod_type+ "\" cur_lang=\"" + prof.cur_lan+ "\"/>" ;
                    if (urlp.instr_type.equals("enrol") || urlp.instr_type.equals("credit")) {
                    	xml += ImportTemplate.userProfToXML(prof.root_id);
                    }
                    if( urlp.instr_type.equals("user") ){
                    	xml += ImportTemplate.templateDescription(prof.root_id);
                    }
                    if (!urlp.equals("user")) {
                    	xml += ImportTemplate.labelToXML();
                    }
                    xml += "</Upload>";
                	if (urlp.cmd.equalsIgnoreCase("get_instr")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                } else if (urlp.cmd.equals("get_del_instr") || urlp.cmd.equalsIgnoreCase("get_del_instr_xml")) {
                    urlp.getInstr();
                    if (urlp.instr_type == null || urlp.instr_type.equals("")) {
                        throw new cwException("Invalid instr type.");
                    }

                    String xml = dbUtils.xmlHeader + "<Upload>" + ((prof != null) ? prof.asXML() : "")
                            + "<isTcIndependent>" + wizbini.cfgSysSetupadv.isTcIndependent() + "</isTcIndependent>"
                            + "<instr type=\"" + urlp.instr_type+ "\" mod_type=\"" + urlp.mod_type+ "\" cur_lang=\"" + prof.cur_lan+ "\"/>" ;
                    if (urlp.instr_type.equals("enrol") || urlp.instr_type.equals("credit")) {
                        xml += ImportTemplate.userProfToXML(prof.root_id);
                    }
                    if( urlp.instr_type.equals("user") ){
                        xml += ImportTemplate.templateDescription(prof.root_id);
                    }
                    if (!urlp.equals("user")) {
                        xml += ImportTemplate.labelToXML();
                    }
                    xml += "</Upload>";
                    if (urlp.cmd.equalsIgnoreCase("get_del_instr")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                } else if( urlp.cmd.equalsIgnoreCase("announ_upload_file") ) {
            		//dbUtils.copyDir(tmpUploadPath, "www\\announ");
            		
            		//get temporary path of web address
            		int tempDirPathLenght = wizbini.getFileUploadTmpDirAbs().length();
            		String uploadedFileDir = tmpUploadPath.substring(tempDirPathLenght + 1);
            	
            		urlp.uploadEnrollment();
            		String fileFullPath = "../" + wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName() + "/" + uploadedFileDir + "/" + urlp.upload_file;
            		String xml = "<file type=\""+urlp.upload_type+"\" name=\""+urlp.src_filename+"\" path=\""+ fileFullPath +"\"/>";
            		xml = formatXML(xml, "announ");
            		generalAsHtml(xml, out, urlp.stylesheet);
                } else if(urlp.cmd.equalsIgnoreCase("upload_user_credits_prep") || urlp.cmd.equalsIgnoreCase("upload_user_credits_prep_xml")) {
                    	StringBuffer xml_ = new StringBuffer();
                    	xml_.append("<max_upload_count>" + wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount() + "</max_upload_count>");                   
                        String xml = formatXML(xml_.toString(), MODULENAME);

                        if( urlp.cmd.equalsIgnoreCase("upload_user_credits_prep") )
                            generalAsHtml(xml, out, urlp.stylesheet);
                        else
                            static_env.outputXML(out, xml);
                } else if (urlp.cmd.equalsIgnoreCase("get_credit_tpl")) {
                	urlp.getCreditTpl(); 

                	String tempDir = wizbini.getFileUploadTmpDirAbs();
                	String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
                	String tplFileName = "credit_import_template.xls";
                	String tpl_path = wizbini.getWebDocRoot() + dbUtils.SLASH + urlp.template_url;
                	Credit credit = new Credit();
                	credit.getCreditImpTpl(con, tplFileName, tpl_path, tempDir, tempDirName, prof.usr_ent_id, prof.cur_lan, prof.my_top_tc_id);
                	
                	String webFilePath = "../" + tempDirName + "/" + prof.usr_ent_id + "/" + tplFileName;
                	response.sendRedirect(webFilePath);
                } else if( urlp.cmd.equalsIgnoreCase("upload_credit") || urlp.cmd.equalsIgnoreCase("upload_credit_xml") ) {
                    if (!bMultipart) {
                        throw new cwSysMessage("GEN000");
                    }
                    urlp.uploadCredit();
                    
                    String newFileName = urlp.src_filename;
                    File srcFile = new File (tmpUploadPath, newFileName);
                    if (!srcFile.exists())
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);

                    int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
                    
                    Credit credit = new Credit();
                    Map result = credit.upLoadBonusPoint(con, srcFile.getAbsolutePath(), maxUploadCount, prof.usr_id, prof.usr_ent_id, prof.root_ent_id);
                    String xml = (String)result.get("xml");
                    xml = formatXML(xml, "uploadpoint");
                    
                    int succ_count = ((Integer)result.get("succ_count")).intValue();
                    if (succ_count > 0) {
    		        	urlp.dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_CREDIT;
    		        	urlp.dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
                        IMSLog ilg = new IMSLog();
                        ilg.ins(con, prof, urlp.dbIlg);
                        ilg.saveUploadedFile(static_env, urlp.dbIlg.ilg_id, srcFile);
    		        }
                    if (urlp.cmd.equalsIgnoreCase("upload_credit")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }	
                } else {
                    throw new cwException("Invalid command.");
                }
 
            }
            
        } catch (JAXBException jaxbe) {
            CommonLog.error(jaxbe.getMessage(),jaxbe);
            throw new cwException (jaxbe.getMessage());
        } catch (FileNotFoundException fe) {
            CommonLog.error(fe.getMessage(),fe);
            throw new cwException (fe.getMessage());
        } catch (cwSysMessage se) {
            try {
                 con.rollback();
                 msgBox(ServletModule.MSG_ERROR, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
                 out.println("SQL error: " + sqle.getMessage());
             }
        }

    }
    

}

