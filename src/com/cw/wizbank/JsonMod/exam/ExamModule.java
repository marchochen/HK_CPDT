package com.cw.wizbank.JsonMod.exam;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.qdb.LoadTestScheduler;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class ExamModule extends ServletModule {
	
	ExamModuleParam modParam;
	public static final String MOD_NAME = "exam_module";
	
	public static final String PAUSE_EXAM_FAIL = "774"; //监考员暂停测验失败
	public static final String PAUSE_EXAM_SUCC = "779"; //监考员暂停测验成功
	public static final String EXAM_PAUSE_SUCC = "755"; //学员提示测验暂停了
	
	public static final String SEND_MSG_FAIL = "775"; //监考员发送信息失败
	public static final String SEND_MSG_SUCC = "781"; //监考员发送信息成功
	
	public static final String SUBMIT_EXAM_FAIL = "776"; //监考员提交测验失败
	public static final String EXAM_SUBMIT_SUCC = "777"; //学员提交测验成功
	public static final String EXAM_SUBMIT_SUCC_AS_ZERO = "778"; //学员提交测验并且置为零分成功
	public static final String SUBMIT_EXAM_SUCC = "782"; //监考员提交测验成功

	public ExamModule() {
		super();
		modParam = new ExamModuleParam();
		param = modParam;
	}
	
	public void process() throws SQLException, cwException, IOException {
		try {
			if(prof == null || prof.usr_ent_id == 0){
				throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
			}else{					
				if (modParam.getCmd().equalsIgnoreCase("get_exam_list") || modParam.getCmd().equalsIgnoreCase("get_exam_list_xml")) {
					String xml = aeItem.getExamItemListAsXml(con, prof.usr_ent_id, prof.current_role, modParam.getCwPage());
					resultXml = formatXML(xml, MOD_NAME);
				} 
				else if (modParam.getCmd().equalsIgnoreCase("get_exam_online_lst") || modParam.getCmd().equalsIgnoreCase("get_exam_online_lst_xml")) {
					String xml = ExamController.getExamInfo(con, modParam.getItm_id());
					resultXml = formatXML(xml, MOD_NAME);
				}
				else if (modParam.getCmd().equalsIgnoreCase("get_online_app_list")) {
					cwUtils.setContentType("text/xml", response, wizbini);
	            	String result = ExamController.getOnlineList(modParam.getItm_id());
	        		out.println(result);
				}
				else if (modParam.getCmd().equalsIgnoreCase("get_online_user_info") || modParam.getCmd().equalsIgnoreCase("get_online_user_info_xml")) {
					String result = ExamController.getUsrInfoAsXml(con, modParam.getItm_id(), modParam.getEnt_id_lst(), modParam.getIsPause(), modParam.getIsTerminate());
					resultXml = formatXML(result, MOD_NAME);
                }
				else if (modParam.getCmd().equalsIgnoreCase("send_msg_to_learner")) {
					String submitUsrLst = ExamController.addExamMessage(con, modParam.getItm_id(), modParam.getEnt_id_lst(), modParam.getMsg_body(), 
							modParam.getIsPause(), modParam.getIsTerminate(), modParam.getIsMarkAsZero());
					if (submitUsrLst.length() == 0) {
						sysMsg = getErrorMsg(SEND_MSG_SUCC, param.getUrl_success());
					} else {
						sysMsg = getErrorMsg(SEND_MSG_FAIL, submitUsrLst, param.getUrl_success());
					}
                }
				else if (modParam.getCmd().equalsIgnoreCase("get_exam_msg")) {
                    cwUtils.setContentType("text/xml", response, wizbini);
                    aeItem parent_item = aeItem.getItemByContentMod(con, modParam.getMod_id());
                	out.println(ExamController.getExamMessage(parent_item.itm_id, prof.usr_ent_id));
                }
				else if (modParam.getCmd().equalsIgnoreCase("submit_exam")) {
					String submitUsrLst = ExamController.addExamMessage(con, modParam.getItm_id(), modParam.getEnt_id_lst(), modParam.getMsg_body(), 
							modParam.getIsPause(), modParam.getIsTerminate(), modParam.getIsMarkAsZero());
					if (submitUsrLst.length() == 0) {
						sysMsg = getErrorMsg(SUBMIT_EXAM_SUCC, param.getUrl_success());
					} else {
						sysMsg = getErrorMsg(SUBMIT_EXAM_FAIL, submitUsrLst, param.getUrl_success());
					}
                }
				else if (modParam.getCmd().equalsIgnoreCase("pause_exam")) {
					String submitUsrLst = ExamController.addExamMessage(con, modParam.getItm_id(), modParam.getEnt_id_lst(), modParam.getMsg_body(), 
							modParam.getIsPause(), modParam.getIsTerminate(), modParam.getIsMarkAsZero());
					if (submitUsrLst.length() == 0) {
						sysMsg = getErrorMsg(PAUSE_EXAM_SUCC, param.getUrl_success());
					} else {
						sysMsg = getErrorMsg(PAUSE_EXAM_FAIL, submitUsrLst, param.getUrl_success());
					}
                }
				else if (modParam.getCmd().equalsIgnoreCase("show_pause_exam_msg")) {
					aeItem parent_item = aeItem.getItemByContentMod(con, modParam.getMod_id());
					ExamPassport pass = ExamController.getExamPassFromHash(parent_item.itm_id, prof.usr_ent_id);
					if (pass != null) {
						sysMsg = getErrorMsg(EXAM_PAUSE_SUCC, pass.pause_msg, param.getUrl_success());
					}
                }
				else if (modParam.getCmd().equalsIgnoreCase("release_pause_exam")) {
					ExamController.releasePause(modParam.getItm_id(), modParam.getEnt_id_lst());
					redirectUrl = param.getUrl_success();
                }
				else if (modParam.getCmd().equalsIgnoreCase("remove_from_online_list")) {
				    aeItem parent_item = aeItem.getItemByContentMod(con, modParam.getMod_id());
					ExamController.examEndByLearner(parent_item.itm_id, prof.usr_ent_id);
                } else if (modParam.getCmd().equalsIgnoreCase("start_test")) {
                    AcModule acMod = new AcModule(con);
                    if (!acMod.checkModifyPermission(prof, modParam.getMod_id())){
                        throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                    }
                    ExamController.startTest(con,prof, modParam.getMod_id(), qdbAction.tests_memory,wizbini.getFileUploadResDirAbs());
                    sysMsg = getErrorMsg("MOD020", param.getUrl_success());
                } else if (modParam.getCmd().equalsIgnoreCase("end_test")) {
                    AcModule acMod = new AcModule(con);
                    if (!acMod.checkModifyPermission(prof, modParam.getMod_id())){
                        throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                    }
                    ExamController.endTest(con, modParam.getMod_id());
                    sysMsg = getErrorMsg("MOD021", param.getUrl_success());
                }
                else if (modParam.getCmd().equalsIgnoreCase("reload_test_memory")) {
                    AcModule acMod = new AcModule(con);
                    if (!acMod.checkModifyPermission(prof, modParam.getMod_id())){
                        throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                    }
                    LoadTestScheduler.reLoadTest(con, wizbini, modParam.getMod_id());
                    sysMsg = getErrorMsg("HK064", param.getUrl_success());
                }
			}
		} catch (cwSysMessage sysExc) {
			throw new cwException(sysExc.getMessage(), sysExc);
		} catch (qdbException e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage(), e);
        } catch (qdbErrMessage e) {
        	CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage(), e);
        }
	}
}
