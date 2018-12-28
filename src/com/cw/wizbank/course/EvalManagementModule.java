/**
 * Servlet API for:"Evaluation Management"
 * @author Christ Qiu
 * @e-mail:qjqyx@avl.com.cn
 * Servlet to control flow of cmd 
 */
package com.cw.wizbank.course;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.ae.aeAttendance;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemLesson;
import com.cw.wizbank.ae.aeItemLessonQianDao;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbMeasurementEvaluation;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class EvalManagementModule extends ServletModule {
	private static final String DEFAULT_ENC = "UnicodeLittle";
	public static final String moduleName = "evalmanagement";
	//  ServletUtils sutils = new ServletUtils();
	public void process() throws SQLException, IOException, cwException, qdbErrMessage {
		if (prof == null) {
			//current login not exist! so you have to Login first
			response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
		}
		EvalManagementReqParam urlp =
			new EvalManagementReqParam(request, clientEnc, static_env.ENCODING);
		PrintWriter out = response.getWriter();
		if (bMultipart) {
			urlp.setMultiPart(multi);
		}
		urlp.common();
		HttpSession sess = request.getSession(true);
		if (urlp.cmd != null) {
			try {
				String xml = "";
				if (urlp.cmd.equalsIgnoreCase("get_scoring_itm_marking_lst")
					|| urlp.cmd.equalsIgnoreCase("get_scoring_itm_marking_lst_xml")) {
                    urlp.pagination();
                    urlp.getCmtId();
					xml = urlp.evalmgt.scoringItmDetail2XML(con, urlp);
					
					long itm_id = DbCourseCriteria.getCcrItmIdByCmtId(con,urlp.cmt_id);
                    xml += aeItem.genItemActionNavXML(con, itm_id, prof);
					xml = formatXML(xml, "", moduleName);
					if (urlp.cmd.equalsIgnoreCase("get_scoring_itm_marking_lst_xml")) {
						static_env.outputXML(out, xml);
						return;
					}
				} else if (urlp.cmd.equalsIgnoreCase("reset")) {
					urlp.getCmtId();
					
					DbMeasurementEvaluation me = new DbMeasurementEvaluation();
					me.resetScore(con, urlp.cmt_id, prof);
					con.commit();
					xml = formatXML(xml, "", moduleName);
					msgBox(MSG_STATUS,new cwSysMessage("GEN003"),urlp.url_success,out);
					return;
				} else if (urlp.cmd.equalsIgnoreCase("edit")||urlp.cmd.equalsIgnoreCase("edit_xml")) {
					DbMeasurementEvaluation MeasureEval = urlp.get4UpdScore();
					urlp.getCmtId();
					StringBuffer xmlBuf = new StringBuffer();
					dbRegUser usr = new dbRegUser();
					usr.usr_ent_id = Long.parseLong(urlp.getLrnEntId());
					xmlBuf.append("<eval_item lrn_ent_id=\"").append(usr.usr_ent_id)
							.append("\" cmt_id=\"").append(urlp.cmt_id)
							.append("\" app_id=\"").append(MeasureEval.app_id)
							.append("\" cmt_tkh_id=\"").append(MeasureEval.getMtv_tkh_id())
							.append("\" cmt_score=\"").append(MeasureEval.getMtv_score())
							.append("\" cmt_max_score=\"").append(MeasureEval.getCmt_max_score())
							.append("\" lrn_name=\"")
							.append(dbUtils.esc4XML(usr.getDisplayBil(con))).append("\"/>");
					long itm_id = DbCourseCriteria.getCcrItmIdByCmtId(con,urlp.cmt_id);
					xmlBuf.append(aeItem.genItemActionNavXML(con, itm_id, prof).toString());
					xml = formatXML(xmlBuf.toString(),"", moduleName);
					
                    if(urlp.cmd.equalsIgnoreCase("edit_xml")){
                        static_env.outputXML(out, xml);
                        return;
                    }
				} else if (urlp.cmd.equalsIgnoreCase("upd_score")) {
                    
					DbMeasurementEvaluation MeasureEval = urlp.get4UpdScore();
                    boolean has_score;
                    float mtv_score = MeasureEval.getMtv_score();
                    if(mtv_score == ReqParam.FLOAT_PARAMETER_NOT_FOUND){
                        has_score = false;
                    }else{
                        has_score = true;
                    }
                    MeasureEval.updScore(con,has_score,prof);
					con.commit();
					xml = formatXML(xml, "", moduleName);
					msgBox(
						MSG_STATUS,
						new cwSysMessage("GEN003"),
						urlp.url_success,
						out);
					return;
				} else if (urlp.cmd.equalsIgnoreCase("export_mark")) {
                    StringBuffer xmlBuf = new StringBuffer();
                    urlp.pagination();
                    //urlp.getItmId();
                    urlp.getAllCmtId(request);
                    if(urlp.all_cmt_id!=null){
                        for(int i=0;i<urlp.all_cmt_id.length;i++){
                            if(urlp.all_cmt_id[i]!="" && urlp.all_cmt_id[i]!=null){
                                urlp.cmt_id = Long.parseLong(urlp.all_cmt_id[i]);
                                xmlBuf.append(urlp.evalmgt.scoringItmDetail2XML(con, urlp));
                            }
                        }
                    }
                    xml = formatXML(xmlBuf.toString(), "", moduleName);
                    response.setHeader("Cache-Control", ""); 
                    response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition","attachment; filename=score.xls;");
                    cwUtils.setContentType("application/vnd.ms-excel",response,wizbini);
				} else if(urlp.cmd.equalsIgnoreCase("import_mark_preview")
                        ||urlp.cmd.equalsIgnoreCase("import_mark_preview_xml")
                        ||urlp.cmd.equalsIgnoreCase("exec_import_mark")){
                        Vector import_Cols = new Vector();
                        if(!urlp.cmd.equalsIgnoreCase("exec_import_mark")){
                            Vector vec_new_file_name = (Vector)sess.getAttribute("NEW_FILENAME");
                            String new_file_name = null;
                            if(vec_new_file_name!=null){
                                new_file_name = vec_new_file_name.elementAt(0).toString(); 
                            }
                            File srcFile = new File(tmpUploadPath,new_file_name);
                            if (!srcFile.exists()){
                                throw new cwSysMessage(
                                    UploadAttdRate.SMSG_ULG_INVALID_FILE);
                            }
                            String delimiter = new String("\t");
                            import_Cols = cwUtils.splitFileToVector(srcFile,DEFAULT_ENC,delimiter);
                            sess.removeAttribute("NEW_FILENAME");
                            sess.removeAttribute("ORIGINAL_FILENAME");
                            urlp.evalmgt.validateRecords(con,import_Cols);
                            //sess.setAttribute("TEMPUPLOADPATH",tmpUploadPath);
                            sess.setAttribute("IMPORT_COLS",import_Cols);
                        }
                    if(urlp.cmd.equalsIgnoreCase("exec_import_mark")){
                        //TODO
                        import_Cols = (Vector)sess.getAttribute("IMPORT_COLS");
                        urlp.evalmgt.importRecords(con,import_Cols,prof);
                        sess.removeAttribute("IMPORT_COLS");
                        con.commit();
                        msgBox(
                            MSG_STATUS,
                            new cwSysMessage("CND004"),
                            urlp.url_success,
                            out);
                        return;
                    }else{
                        String temp = urlp.evalmgt.genPreviewXml(import_Cols);
                        xml = formatXML(temp,"", moduleName);
                    }
                    if(urlp.cmd.equalsIgnoreCase("import_mark_preview_xml")){
                        static_env.outputXML(out, xml);
                        return;                        
                    }
				} else if (urlp.cmd.equalsIgnoreCase("import_mark")) {
                    
                    

				} else if (urlp.cmd.equalsIgnoreCase("import_mark_prep")) {
                    StringBuffer xmlBuf = new StringBuffer();
                    urlp.pagination();
                    urlp.getAllCmtId(request);
                    if(urlp.all_cmt_id!=null){
                        for(int i=0;i<urlp.all_cmt_id.length;i++){
                            urlp.cmt_id = Long.parseLong(urlp.all_cmt_id[i]);
                            xmlBuf.append(urlp.evalmgt.scoringItmDetail2XML(con, urlp));
                        }
                    }
                    xml = formatXML(xmlBuf.toString(), "", moduleName);
                } else if (
					urlp.cmd.equalsIgnoreCase("upload_attdrate_prep")
						|| urlp.cmd.equalsIgnoreCase("upload_attdrate_prep_xml")) {
					StringBuffer xmlBuf = new StringBuffer("");
					xmlBuf.append("<itm_id>");
					String itm_id = request.getParameter("itm_id");
					xmlBuf.append(itm_id);
					xmlBuf.append("</itm_id>");
					String finalxml = formatXML(xmlBuf.toString(), moduleName);

					if (urlp.cmd.equalsIgnoreCase("upload_attdrate_prep"))
						generalAsHtml(finalxml, out, urlp.stylesheet);
					else
						static_env.outputXML(out, finalxml);
					return;
				} else if (
					urlp.cmd.equalsIgnoreCase("upload_AttdRate")
						|| urlp.cmd.equalsIgnoreCase("upload_AttdRate_xml")) {
					if (!bMultipart) {
						throw new cwSysMessage("GEN000");
					}
					urlp.uploadAttdRate();
					String newFileName = urlp.src_filename;
					long itm_id = urlp.itm_id;
					File srcFile = new File(tmpUploadPath, newFileName);
					if (!srcFile.exists())
						throw new cwSysMessage(
							UploadAttdRate.SMSG_ULG_INVALID_FILE);
					UploadAttdRate upload =
						new UploadAttdRate(
							con,
							prof,
							itm_id,
							srcFile,
							DEFAULT_ENC);
					String outXML = upload.updatePreview();
					sess.setAttribute(
						UploadAttdRate.SESS_UPLOADED_SRC_FILE,
						srcFile);

					String finalxml = formatXML(outXML, "attdance_rate");
					if (urlp.cmd.equalsIgnoreCase("upload_attdrate"))
						generalAsHtml(finalxml, out, urlp.stylesheet);
					else
						static_env.outputXML(out, finalxml);
					return;
				} else if (urlp.cmd.equalsIgnoreCase("cook_attdrate")) {
					urlp.uploadAttdRate();
					File srcFile =
						(File) sess.getAttribute(
							UploadAttdRate.SESS_UPLOADED_SRC_FILE);
					UploadAttdRate upload =
						new UploadAttdRate(
							con,
							prof,
							urlp.itm_id,
							srcFile,
							DEFAULT_ENC);
					upload.cook();
					msgBox(
						UploadAttdRate.MSG_STATUS,
						new cwSysMessage(
							UploadAttdRate.MSG_SAVE_ATTRATE_SUCCESS, "Attendance"),
						urlp.url_success,
						out);
					return;
				} else if (
					urlp.cmd.equals("attd_rate_lst")
						|| urlp.cmd.equals("attd_rate_lst_xml")) {
					urlp.pagination();
					urlp.attendance(clientEnc, static_env.ENCODING);
					if (urlp.download) {
						urlp.cwPage.pageSize = Integer.MAX_VALUE;
					}
					String result = new String("");
					try {
						result =
							aeAttendance.processStatus(
								con,
								sess,
								prof.root_ent_id,
								urlp.itm_id,
								urlp.att_status,
								urlp.cwPage,
								urlp.show_approval_ent_only,
								prof.usr_ent_id,
								prof.current_role,
								wizbini,null,false,urlp.user_code);
					} catch (qdbException e) {
						throw new cwException(e.getMessage());
					}
					String metaXML = "";
					if (prof != null) {
						AcPageVariant acPageVariant = new AcPageVariant(con);
						acPageVariant.instance_id = urlp.itm_id;
						acPageVariant.ent_owner_ent_id = prof.root_ent_id;
						acPageVariant.ent_id = prof.usr_ent_id;
						acPageVariant.rol_ext_id = prof.current_role;
						metaXML =
							acPageVariant.answerPageVariantAsXML(
								(String[]) xslQuestions.get(urlp.stylesheet));
					}
					metaXML
						+= dbRegUser.getUserAttributeInfoXML(
							wizbini,
							prof.root_id);
					

					result += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
					
					result +="<current_role>"+prof.current_role+"</current_role>";
					
					result =
						formatXML(
							cwUtils.escNull(result),
							metaXML,
							moduleName,
							urlp.stylesheet);
					if (urlp.cmd.equals("attd_rate_lst_xml")) {
						out.println(result);
					} else {
						if (urlp.download) {
                            response.setHeader("Cache-Control", ""); 
                            response.setHeader("Pragma", ""); 
							response.setHeader("Content-Disposition","attachment; filename=attendance_report.xls;");
							cwUtils.setContentType("application/vnd.ms-excel",response,wizbini);
						}
						generalAsHtml(result, out, urlp.stylesheet);
					}
					return;
				} else if (
					urlp.cmd.equalsIgnoreCase("prep_upd_multi_att_rate")
						|| urlp.cmd.equalsIgnoreCase(
							"prep_upd_multi_att_rate_xml")) {
					urlp.updAttdRate(clientEnc, static_env.ENCODING);
					String att_rate_status = 
						request.getParameter("att_rate_status");
					StringBuffer xmlBuf = new StringBuffer("");
					xmlBuf.append("<item id=\"").append(urlp.itm_id).append("\"/>");
					xmlBuf.append("<att_rate_status>").append(att_rate_status)
                            .append("</att_rate_status>");
					String result =
						formatXML(
							cwUtils.escNull(xmlBuf.toString()),
							moduleName);
					if (urlp.cmd.equals("prep_upd_multi_att_rate_xml")) {
						out.println(result);
					} else {
						generalAsHtml(result, out, urlp.stylesheet);

					}
					return;
				} else if (urlp.cmd.equalsIgnoreCase("upd_multi_att_rate")) {
					urlp.updAttdRate(clientEnc, static_env.ENCODING);
					aeAttendance att = new aeAttendance();
					aeAttendance.msgSubject[0] = urlp.msg_subject_1;
					aeAttendance.msgSubject[1] = urlp.msg_subject_2;
					aeAttendance.msgSubject[2] = urlp.msg_subject_3;
					att.updMultiRate(
						con,
						prof,
						urlp.app_id_lst,
						urlp.att_rate,
						urlp.att_rate_remark,
						urlp.itm_id);
					con.commit();
					response.sendRedirect(urlp.url_success);
					return;
				}else if (urlp.cmd.equals("ae_get_qiandao_lst")|| urlp.cmd.equals("ae_get_qiandao_lst_xml")) {
					urlp.pagination();
					urlp.attendance(clientEnc, static_env.ENCODING);
					if (urlp.download) {
						urlp.cwPage.pageSize = Integer.MAX_VALUE;
					}
					String result = new String("");
					aeItem item = new aeItem();
					item.itm_id = urlp.itm_id;
			        item.getItem(con);
			        result +="<qiandao_lst>";
			        result += item.contentAsXML(con, null, prof.root_ent_id);
					result += aeItem.getNavAsXML(con, urlp.itm_id);
					result += "<select_ils_id>" + urlp.ils_id + "</select_ils_id>";
					try {
						aeItemLesson ils = new aeItemLesson();
						result += ils.getAeItemLessonXML(con, urlp.itm_id);
						
						aeItemLessonQianDao ils_qd = new aeItemLessonQianDao();
						result += ils_qd.get_qiandao_lst(con, urlp.itm_id, urlp.ils_id, urlp.cwPage);
						
					} catch (SQLException e) {
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					}
					String metaXML = "";
					if (prof != null) {
						AcPageVariant acPageVariant = new AcPageVariant(con);
						acPageVariant.instance_id = urlp.itm_id;
						acPageVariant.ent_owner_ent_id = prof.root_ent_id;
						acPageVariant.ent_id = prof.usr_ent_id;
						acPageVariant.rol_ext_id = prof.current_role;
						metaXML =acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
					}

					result += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
			        result +="</qiandao_lst>";
					result =formatXML(cwUtils.escNull(result),metaXML,moduleName,urlp.stylesheet);

					if (urlp.cmd.equals("ae_get_qiandao_lst_xml")) {
						out.println(result);
					} else {
						generalAsHtml(result, out, urlp.stylesheet);
					}
					return;
				} 
				generalAsHtml(xml, out, urlp.stylesheet);
			} catch (cwSysMessage se) {
				try {
					con.rollback();
					msgBox(ServletModule.MSG_ERROR, se, urlp.url_failure, out);
				} catch (SQLException sqle) {
					out.println("SQL error: " + sqle.getMessage());
				}
			}catch(qdbException qe) {
					  try {
						out.println("Server error: "+qe.getMessage());
						CommonLog.error("Server error: "+qe.getMessage(),qe);
						con.rollback();
					  } catch (SQLException sqle) {
						out.println("SQL error: " + sqle.getMessage());
						CommonLog.error("SQL error: " + sqle.getMessage(),sqle);
					  }
			}
		} else {
			return;
		}
	}
}
