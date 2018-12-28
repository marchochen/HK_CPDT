package com.cw.wizbank.JsonMod.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.qdb.TestMemory;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class ExamController {
	public static Map examInfoHash = new HashMap();
	public static Map usrExamHash = new HashMap();
	
	static final String ID_SEPARATOR = "~";
	static final String EXAM_SESS_LISTENER = "ExamSessListener";
	
	private static void putExamItem(Long itmId) {
		synchronized (examInfoHash) {
			if (!examInfoHash.containsKey(itmId)) {
				examInfoHash.put(itmId, new HashMap());
			}
		}
	}
	
	//向缓存中的Hash表插入记录
	public static void putExamInfo(Connection con, long usr_ent_id, long itm_id, String usr_display_bil) throws SQLException {
		Long itmId = new Long(itm_id);
		Long usrId = new Long(usr_ent_id);
		if (!examInfoHash.containsKey(itmId)) {
			putExamItem(itmId);
		}
		
		Map itemMsgHash = (Map)examInfoHash.get(itmId);
		if (!itemMsgHash.containsKey(usrId)) {
			ExamPassport pass = new ExamPassport();
			pass.setUsr_display_bil(usr_display_bil);
			pass.setUsr_ent_id(usr_ent_id);
			pass.setTest_itm_id(itm_id);
			pass.setUsg_display_bil(EntityFullPath.getInstance(con).getFullPath(con, dbEntityRelation.getParentUserGroup(con, usr_ent_id).ent_id));
			itemMsgHash.put(usrId, pass);
		}
		
		List itmLst = null;
		if (usrExamHash.containsKey(usrId)) {
			itmLst = (List)usrExamHash.get(usrId);
		} else {
			itmLst = new ArrayList();
			usrExamHash.put(usrId, itmLst);
		}
		if (!itmLst.contains(itmId)) {
			itmLst.add(itmId);
		}	
	}
	
	private static void clearUsrInfo(Map itemMsgHash, Long usrId){
		synchronized (itemMsgHash) {
			if (itemMsgHash.containsKey(usrId)) {
				itemMsgHash.remove(usrId);
			}
		}
	}
	
	private static void clearItemInfo(Map itemMsgHash, Long itmId){
		synchronized (examInfoHash) {
			if (examInfoHash.containsKey(itmId) && itemMsgHash.size() == 0) {
				examInfoHash.remove(itmId);
			}
		}
	}
	
	//学员提交或者关闭测验，清除对应缓存里面的信息
	public static void examEndByLearner(long itm_id, long usr_ent_id) {
		Long itmId = new Long(itm_id);
		Map itemMsgHash = (Map)examInfoHash.get(itmId);
		if (itemMsgHash != null) {
			Long usrId = new Long(usr_ent_id);
			if (itemMsgHash.containsKey(usrId)) {
				clearUsrInfo(itemMsgHash, usrId);
			}
			
			if (itemMsgHash.size() == 0) {
				clearItemInfo(itemMsgHash, itmId);
			}
		}
		
		Long usrId = new Long(usr_ent_id);
		List itmLst = (List)usrExamHash.get(usrId);
		if (itmLst != null) {
			itmLst.remove(itmId);
			if (itmLst.size() == 0) {
				usrExamHash.remove(usrId);
			}
		}
	}
	
	public static String getExamInfo(Connection con, long itm_id) throws SQLException, cwSysMessage {
		StringBuffer result = new StringBuffer();
		result.append("<exam_info itm_id=\"").append(itm_id).append("\">")
			  .append("<itm_title>").append(cwUtils.esc4XML(aeItem.getItemTitle(con, itm_id))).append("</itm_title>")
			  .append("<app_total>")
			  		.append(aeApplication.getApplicationByItmId(con, itm_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS).size())
			  .append("</app_total>")
			  .append("</exam_info>");
		return result.toString();
	}
	
	public static String getOnlineList(long itm_id) {
		StringBuffer result = new StringBuffer();
		result.append("<tst_usr_lst>");
		Map itemMsgHash = (Map)examInfoHash.get(new Long(itm_id));
		if (itemMsgHash != null) {
			synchronized (itemMsgHash) {
				Set itmSet = itemMsgHash.entrySet();
				Iterator iter = itmSet.iterator(); 
				String tag_name = null;
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry)iter.next();
					ExamPassport pass = (ExamPassport)entry.getValue();
					if (pass.isPause) {
						tag_name = "pause_usr_list";
					} else {
						tag_name = "exam_usr_list";
					}
					result.append("<" + tag_name + ">")
					  	  .append("<ent_id>").append(pass.getUsr_ent_id()).append("</ent_id>")
					  	  .append("<name>").append(cwUtils.esc4XML(pass.getUsr_display_bil())).append("</name>")
					  	  .append("<group>").append(cwUtils.esc4XML(pass.getUsg_display_bil())).append("</group>")
					  	  .append("<itm_id>").append(itm_id).append("</itm_id>")
					  	  .append("</" + tag_name + ">");
				}
			}
		}
		result.append("</tst_usr_lst>");
		return result.toString();
	}
	
	
	public static boolean getUsrStatus(long itm_id, long usr_ent_id) {
		boolean result = false;
		ExamPassport pass = getExamPassFromHash(itm_id, usr_ent_id);
		if (pass != null) {
			result = pass.isPause;
		}
		return result;
	}
	
	public static String getUsrInfoAsXml(Connection con, long itm_id, String ent_id_lst, boolean isPause, boolean isTerminate) throws SQLException {
		StringBuffer result = new StringBuffer();
		result.append("<itm_id>").append(itm_id).append("</itm_id>")
			  .append("<ent_id_lst>").append(ent_id_lst).append("</ent_id_lst>")
			  .append("<isTerminate>").append(isTerminate).append("</isTerminate>")
			  .append("<isPause>").append(isPause).append("</isPause>");
		long[] ent_lst = cwUtils.splitToLong(ent_id_lst, ID_SEPARATOR);
		for (int i = 0; i < ent_lst.length; i++) {
			result.append("<usr_display_bil>").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, ent_lst[i]))).append("</usr_display_bil>");
		}
		return result.toString();
	}

	public static String getExamMessage(long itm_id, long usr_ent_id) {
		String value = null;
		ExamPassport pass = getExamPassFromHash(itm_id, usr_ent_id);
		if (pass != null) {
			value = pass.getExamStatus();
		}
		return value;
	}
	
	/**
	 * 监考员发出指令(暂停考试、发送消息、提交考试)，返回已经退出考试操作失败的用户名list
	 */
	public static String addExamMessage(Connection con, long itm_id, String ent_id_lst, String msg_body, boolean isPause, 
			boolean isTerminate, boolean isMarkAsZero) throws SQLException {
		StringBuffer submitUsr = new StringBuffer();
		long[] ent_lst = cwUtils.splitToLong(ent_id_lst, ID_SEPARATOR);
		ExamPassport pass = null;
		int submitCnt = 0;
		for (int i = 0; i < ent_lst.length; i++) {
			pass = getExamPassFromHash(itm_id, ent_lst[i]);
			if (pass != null) {
				if (isPause) {
					pass.isPause = isPause;
					pass.pause_msg = msg_body;
				} else if(isTerminate) {
					pass.isTerminate = isPause;
					pass.terminate_msg = msg_body;
					pass.terminate_as_zero = isMarkAsZero;
				}
				pass.addMessage(msg_body, isPause, isTerminate, isMarkAsZero);
			} else {
				makeSubmitUsrAsXml(con, submitUsr, submitCnt, ent_lst[i]);
			}
		}
		return submitUsr.toString();
	}
	
	private static void makeSubmitUsrAsXml(Connection con, StringBuffer submitUsr, int submitCnt, long ent_id) throws SQLException {
		submitCnt++;
		if (submitCnt > 1) {
			submitUsr.append(", ");
			if (submitCnt % 2 == 0) {
				submitUsr.append("<br/>");
			}
		}
		submitUsr.append(dbRegUser.getDisplayBil(con, ent_id));
	}

	public static ExamPassport getExamPassFromHash(long itm_id, long usr_ent_id) {
		ExamPassport pass = null;
		Map itemMsgHash = (Map)examInfoHash.get(new Long(itm_id));
		if (itemMsgHash != null) {
			pass = (ExamPassport)itemMsgHash.get(new Long(usr_ent_id));
		}
		return pass;
	}
	
	public static boolean isPause(long itm_id, long usr_ent_id) {
		boolean isPause = false;
		ExamPassport pass = getExamPassFromHash(itm_id, usr_ent_id);
		if (pass != null) {
			isPause = pass.isPause;
		}
		return isPause;
	}
	
	public static void releasePause(long itm_id, String ent_id_lst) {
		long[] ent_lst = cwUtils.splitToLong(ent_id_lst, ID_SEPARATOR);
		ExamPassport pass = null;
		for (int i = 0; i < ent_lst.length; i++) {
			pass = getExamPassFromHash(itm_id, ent_lst[i]);
			if (pass != null) {
				if (pass.isLogin) {
					pass.isPause = false;
					pass.pause_msg = null;
				} else {
					examEndByLearner(itm_id, ent_lst[i]);
				}
			}
		}
	}
	
	public static void createExamSessListener(long usr_ent_id, HttpSession sess) {
		ExamSessionListener sessionListener = null;
		if (sess.getAttribute(EXAM_SESS_LISTENER) == null) {
			sessionListener = new ExamSessionListener(usr_ent_id);
			sess.setAttribute(EXAM_SESS_LISTENER, sessionListener);
		}
	}
	
	public static void startTest(Connection con, loginProfile prof, long mod_id, HashMap memory, String res_dir) throws SQLException, cwSysMessage, qdbException, cwException, qdbErrMessage {
	    dbModule dbmod = new dbModule();
	    dbmod.mod_res_id = mod_id;
	    dbmod.res_upd_user = prof.usr_id;
        dbmod.get(con);
        Long _mod_id = new Long(mod_id);
        memory.put(_mod_id, new TestMemory());
        TestMemory testMemory = (TestMemory)memory.get(_mod_id);
        if (testMemory.hs_tests_score.size() == 0) {
            ExportController controller = new ExportController();
            testMemory.beginSetTest(dbmod, prof.usr_id, res_dir, con, controller);
        }
        dbmod.setTstStartedStatus(con, 1);
	}
	
   public static void endTest(Connection con, long mod_id) throws SQLException, cwSysMessage, qdbException, cwException, IOException, qdbErrMessage {
       dbModule dbmod = new dbModule();
       dbmod.mod_res_id = mod_id;
       dbmod.get(con);
       dbmod.setTstStartedStatus(con, 0);
   }
}
