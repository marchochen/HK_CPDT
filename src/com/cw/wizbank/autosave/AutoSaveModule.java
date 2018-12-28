package com.cw.wizbank.autosave;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class AutoSaveModule extends ServletModule {	
	private static final Object lock = new Object();

	public void process() throws IOException, cwException, SQLException, qdbErrMessage {
		HttpSession sess = request.getSession(false);
		if (sess == null || prof == null) {
			response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
		}
		AutoSaveReqParam urlp = new AutoSaveReqParam(request, clientEnc, static_env.ENCODING);
		if (bMultipart) {
			urlp.setMultiPart(multi);
		}
		urlp.common();
		PrintWriter out = response.getWriter();
		try {
			if (urlp.cmd.equalsIgnoreCase("auto_save")) {
				urlp.getAutoSaveParam();
				if (urlp.cur_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					urlp.cur_tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, urlp.cur_mod_id, prof.usr_ent_id);
				}
				if (urlp.cur_tkh_id != 0 && urlp.cur_tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND && DbTrackingHistory.getAppTrackingIDByCos(con, urlp.cur_tkh_id, prof.usr_ent_id, dbModule.getCosId(con, urlp.cur_mod_id), urlp.cur_mod_id) != 1){
					msgBoxTst(MSG_ERROR, new cwSysMessage("USR033"), urlp.url_failure, out);
	                return;
	            }

				Timestamp cur_time = cwSQL.getTime(con);
				if (urlp.save_by_usr){//学员自己保存时记录学习记录
                    dbModuleEvaluation dbmov = new dbModuleEvaluation();
                    dbmov.mov_cos_id = dbModule.getCosId(con, urlp.cur_mod_id);
                    dbmov.mov_ent_id = prof.usr_ent_id;
                    dbmov.mov_mod_id = urlp.cur_mod_id;
                    dbmov.mov_tkh_id = urlp.cur_tkh_id; 
                    dbmov.mov_create_usr_id = prof.usr_id;
                    dbmov.mov_update_usr_id = prof.usr_id;   
                    dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
                    dbmov.mov_score = "" + 0;
                    dbmov.attempt_counted = true;
                    try {
						dbResource dbres = new dbResource();
						dbres.res_id = dbmov.mov_mod_id;
						dbres.get(con);
						int totalTime = (int)(dbres.res_duration) * 60 - urlp.time_left;
						//由于学员端提交方式更改为异步，此处需要保证方法的执行顺序,避免产生错误的历史记录, by Kenry
						synchronized (lock){
							dbmov.get(con);
							dbmov.mod_time = totalTime - dbmov.mov_total_time;
							dbmov.save(con, prof);
							con.commit();
						}
                    } catch (cwSysMessage e) {
                        throw new cwException(e.getMessage());
                    }
                }
/*				Vector int_order = parseAnswer(urlp.responsebil_order_flag, 0);
				Vector response_bil = parseAnswer(urlp.responsebil_order_flag, 1);
				Vector dbFlag = parseAnswer(urlp.responsebil_order_flag, 2);
*/				
				dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
				dbpas.pasTkhId = urlp.cur_tkh_id;
				dbpas.pasResId = urlp.cur_mod_id;
				dbpas.pasTimeLeft = urlp.time_left;
				dbpas.pasFlag = urlp.flag;
				dbpas.pas_create_usr_id = prof.usr_id;
				dbpas.pas_update_usr_id = prof.usr_id;
				if (dbpas.chkforExist(con)) {
					dbpas.upd(con, cur_time);
				} else {
					dbpas.ins(con, cur_time);
				}
				con.commit();
				dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
				dbpsa.psaTkhId = urlp.cur_tkh_id;
				dbpsa.psaPgrResId = urlp.cur_mod_id;
				dbpsa.psaIntResId = urlp.cur_que_id;
				dbpsa.psa_create_usr_id = prof.usr_id;
				dbpsa.psa_update_usr_id = prof.usr_id;
				synchronized (this) {
					for (int i=1; i<=urlp.int_size; i++) {
						String cur_interaction = urlp.getCurInteraction(i);
						if(cur_interaction!=null){
							String[] answer = parseAnswer(cur_interaction);
							dbpsa.psaIntOrder = new Long(answer[0]).longValue();
							dbpsa.psaResponseBil = answer[1];
							String dbFlag = answer[2];
							if (dbFlag.equals(dbpsa.DB_FLAG_INS)) {
								dbpsa.ins(con, cur_time);
							} else if (dbFlag.equals(dbpsa.DB_FLAG_UPD)) {
								dbpsa.upd(con, cur_time);
							} else if (dbFlag.equals(dbpsa.DB_FLAG_DEL)){
								dbpsa.del(con);
							}
						}
					}
					if (!dbpsa.chkforExist(con) && dbpas.pasFlag != null && dbpas.pasFlag.equals("")) {
						dbpas.del(con);
					}
					con.commit();
				}
				/*for (int i=0; i<int_order.size(); i++) {
					dbpsa.psaIntOrder = new Long(int_order.elementAt(i).toString()).longValue();
					dbpsa.psaResponseBil = response_bil.elementAt(i).toString();
					if (dbFlag.elementAt(i).equals(dbpsa.DB_FLAG_INS)) {
						dbpsa.ins(con, cur_time);
					} else if (dbFlag.elementAt(i).equals(dbpsa.DB_FLAG_UPD)) {
						dbpsa.upd(con, cur_time);
					} else if (dbFlag.elementAt(i).equals(dbpsa.DB_FLAG_DEL)){
						dbpsa.del(con);
					}
				}*/
			}else if (urlp.cmd.equalsIgnoreCase("del_auto_save")){
				urlp.getAutoSaveParam();
				if (urlp.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					urlp.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, urlp.mod_id, prof.usr_ent_id);
				}
				if (urlp.tkh_id != 0 && urlp.tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND && DbTrackingHistory.getAppTrackingIDByCos(con, urlp.tkh_id, prof.usr_ent_id, dbModule.getCosId(con, urlp.mod_id), urlp.mod_id) != 1){
	                return;
	            }
				dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
				dbpas.pasTkhId = urlp.tkh_id;
				dbpas.pasResId = urlp.mod_id;
				dbpas.del(con);
				dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
				dbpsa.psaTkhId = urlp.tkh_id;
				dbpsa.psaPgrResId = urlp.mod_id;
				dbpsa.delAll(con);
				dbResourceContent.DelForDxt(con, urlp.mod_id, urlp.tkh_id);
				con.commit();
			}
			else {
				throw new qdbException("unknown command " + urlp.cmd);
			}
		}catch (qdbException e) {
			throw new cwException("GEN000");
		}
	}
	
	private String[] parseAnswer (String responsebilAndOrder) {
		String[] answer = new String[3];
		answer[0] = responsebilAndOrder.substring(0, responsebilAndOrder.indexOf("_"));
		answer[1] = responsebilAndOrder.substring(responsebilAndOrder.indexOf("_")+1, responsebilAndOrder.lastIndexOf("_"));
		answer[2] = responsebilAndOrder.substring(responsebilAndOrder.lastIndexOf("_")+1, responsebilAndOrder.length());
/*		String[] answer1 = cwUtils.splitToString(responsebilAndOrder, "^");
		for (int i=0; i<answer1.length; i++) {
			String[] answer2 = cwUtils.splitToString(answer1[i], "_");
			vec.add(answer2[order]);
		}*/
		return answer;
	}
	
    //message box only for process error when learner submit test.
    private void msgBoxTst(String title, cwSysMessage e, String url, PrintWriter out)throws IOException, cwException, SQLException {
        String encoding;
        if(prof == null)
            encoding = static_env.ENCODING;
        else
            encoding = prof.label_lan;
            
        String msg = e.getSystemMessage(encoding);

    	genMsgBoxTst(title, msg, url, out);
    }

    private void genMsgBoxTst(String title, String msg, String url, PrintWriter out)throws IOException, cwException{
        StringBuffer xml = new StringBuffer();
        
        boolean script = false;
        if(url!= null && url.trim().toLowerCase().startsWith("javascript:")){
            url = url.trim().substring("javascript:".length());
            script = true;
        }
        
        xml.append(cwUtils.xmlHeader);
        xml.append("<message>");
        xml.append("<is_tst_submit_err>true</is_tst_submit_err>");
        if(prof!=null) {
        	xml.append(prof.asXML());
        }
        xml.append("<title>").append(title).append("</title>");
        xml.append("<body>");
        xml.append("<text>").append(cwUtils.esc4XML(msg)).append("</text>"); 
        if(script){
            xml.append("<button url=\"").append(url).append("\"");
            xml.append( " script=\"" + script + "\"");
        }else{
            xml.append("<button url=\"").append(url).append("\"");
        }
        xml.append(">OK</button>");
        xml.append("</body>");
        xml.append("</message>");

        String xsl_root = (prof == null) ? null : prof.xsl_root;

        static_env.procXSLFile(xml.toString(), static_env.INI_XSL_MSGBOX, out, xsl_root);
    }
	
	
}
