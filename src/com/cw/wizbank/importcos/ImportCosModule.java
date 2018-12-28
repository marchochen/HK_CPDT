package com.cw.wizbank.importcos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.Cookie;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.qdb.CourseValidator;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;


public class ImportCosModule extends ServletModule
{

static Hashtable prevCookieList = null;    
    
    public ImportCosModule() {;		//$$ importCosModule1.move(0,0);
}
    
    public void process() throws SQLException, cwException, IOException {
    	CommonLog.info("IN ImportCosModule");
        
        if (prof ==null)
        	CommonLog.info("login profile is null.");
        else 
        	CommonLog.info("loginProfile  > usr_id :" + prof.usr_id);
        // get output stream for normal content to client
        PrintWriter out = response.getWriter();
        
        ImportCosReqParam urlp = null;
        urlp = new ImportCosReqParam(bMultipart, request, multi, clientEnc, static_env.ENCODING);
        urlp.import_cos_info();

        // service processing starts here
        try {
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            }
            else if (urlp.cmd == null) {
                throw new cwException("invalid command");

            }
            else if (urlp.cmd.toUpperCase().startsWith("IMPORT_SCORM_1_1")) {
            	CommonLog.info("importing scorm courses...");
                String csfPath = tmpUploadPath + dbUtils.SLASH + urlp.csfFileName;
                ImportCos myImportCos = new ImportCos();
                myImportCos.importScorm(con, prof, urlp.cos_id, csfPath);
                
                con.commit();
                // imported the course successfully
                msgBox(MSG_STATUS, new cwSysMessage("SCO001"), urlp.url_success, out);                   
            }            
            else if (urlp.cmd.toUpperCase().startsWith("IMPORT_SCORM_")) {
//            	else if (urlp.cmd.toUpperCase().startsWith("IMPORT_SCORM_1_2")) {
            	CommonLog.info("importing scorm courses...");
                String imsmanifestPath = null;
                boolean is_scorm_2004 = false;
                String manifestFileUrl = "";
                if (urlp.src_res_id > 0) {
                	dbResource res = new dbResource();
                	res.res_id = urlp.src_res_id;
                	res.get(con);
                	if (res.res_sco_version.equals("2004")) is_scorm_2004 = true;
                	File infoFile = new File(wizbini.getFileUploadResDirAbs() + dbUtils.SLASH + urlp.src_res_id + cwUtils.SLASH + "aicc_info.txt");
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new InputStreamReader(new FileInputStream(infoFile)));
                    } catch(Exception e) {
                        throw new IOException(e.toString());
                    }
                    String fileName = in.readLine().trim();
                    in.close();
                	imsmanifestPath = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + urlp.src_res_id + cwUtils.SLASH + fileName;
                } else {
                	imsmanifestPath = tmpUploadPath + dbUtils.SLASH + urlp.imsmanifestFileName;
                }

                    
                String validMsg = CourseValidator.validScormFile(imsmanifestPath);
                if(!validMsg.equals("")) {
                	String xml = dbUtils.xmlHeader + "<errors>" + ((prof != null) ? prof.asXML() : "")  + validMsg + "</errors>";
                	generalAsHtml(xml, out, "course_error.xsl");	
                	return;
                }
                
                dbCourse myDbCourse = new dbCourse();
                myDbCourse.cos_res_id = urlp.cos_id;
                myDbCourse.res_id = urlp.cos_id;
             
                myDbCourse.res_title = "SCORM";
                myDbCourse.get(con);
                
                    
                	
                ImportCos myImportCos = new ImportCos();
                if (urlp.cmd.toUpperCase().startsWith("IMPORT_SCORM_2004") || is_scorm_2004) {
                	myImportCos.importScorm2004(con, prof, urlp.cos_id, imsmanifestPath, urlp.cosUrlPrefix, false, false, false, wizbini, myDbCourse);
                } else {
                	myImportCos.importScorm1_2(con, prof, urlp.cos_id, imsmanifestPath, urlp.cosUrlPrefix, false, false, false,myDbCourse);
                }
                
                //更新是否发布到移动端手机与移动端的显示方式
                dbModule dbmod_ = new dbModule();
                dbmod_.mod_res_id = myImportCos.new_mod_id;
                dbmod_.mod_mobile_ind = urlp.mod_mobile_ind;
                dbmod_.mod_test_style = urlp.mod_test_style;
                dbmod_.updateMobileInd(con);
                
                // DEAN:面授课程为统一内容时，需要在班级中也添加对应的资源
                dbCourse dbcos = new dbCourse(urlp.cos_id);
                dbcos.get(con);
                Vector vtCosResId = dbcos.getChildCosResId(con);
                List resIdListInserted = myImportCos.getResIdListInserted();
                for(int i= 0;i<vtCosResId.size();i++){
                	for (int j = 0; j < resIdListInserted.size(); j++) {
                		long modResId = ((Long)resIdListInserted.get(j)).longValue();
                		
                		dbModule dbmod = new dbModule();
                		dbmod.mod_res_id = modResId;
                		dbmod.getAllAicc(con);
                		
                		dbmod.mod_mod_res_id_parent = modResId;
                		((dbCourse)vtCosResId.get(i)).insModule(con,dbmod,null,prof);
                		dbmod.updAicc(con, prof, "", "", "", dbmod.mod_time_limit_action, dbmod.mod_web_launch, "", dbmod.res_desc, "2.2");
                	}
	                ((dbCourse)vtCosResId.get(i)).updAiccCos(con, prof, "", "2.2", "", -1, true, true, true);
	                ((dbCourse)vtCosResId.get(i)).updCosStructureFromParent(con, dbcos.cos_structure_xml);
                }
                con.commit();
                //response.addCookie(new Cookie("mod_id", String.valueOf(myImportCos.new_mod_id)));
                // imported the course successfully
                //msgBox(MSG_STATUS, new cwSysMessage("SCO001"), urlp.url_success, out);
                msgBox(MSG_STATUS, new cwSysMessage("SCO001"), urlp.url_success, out,myImportCos.new_mod_id,urlp.is_inner);                
            }
            else {
                // do nothing
            }
        } catch (cwSysMessage e) {
             try {
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, e, urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
             }             
        } catch (qdbException e) {
             try {
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, new cwSysMessage(e.toString()), urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
             }             
        } catch (qdbErrMessage e) {
             try {
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, new cwSysMessage(e.toString()), urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
             }             
        }
    }
	//{{DECLARE_CONTROLS
	//}}
}