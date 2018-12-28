/*
 * Created on 2004-9-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cw.wizbank.blp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.db.DbBpBlueprint;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author terry
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class BlueprintModule extends ServletModule {
    public static final String MODULENAME = "blp";

    /**
     * When file is uploaded.if the file is OK,then true;
     */
    private boolean bFileUploaded = false;

    public void process() throws SQLException, IOException, cwException {
        HttpSession sess = request.getSession(false);
        BlueprintReqParam urlp = null;
        urlp = new BlueprintReqParam(request, clientEnc, static_env.ENCODING);
        urlp.setProfile(super.prof);
        String UploadPath = null;
		if(prof!=null){
			 UploadPath = wizbini.getWebDocRoot() +   cwUtils.SLASH + "blueprint" + cwUtils.SLASH +  + urlp.prof.root_ent_id  + cwUtils.SLASH ;
		}
        //if request contains file,upload and check,and place to the UploadPath
        if (bMultipart) {
            urlp.setMultiPart(multi);
            procUploadedFiles(this.tmpUploadPath, urlp);
            urlp.setFileUploaded(this.bFileUploaded);
        }
        urlp.common();
        urlp.getRequestType();
        PrintWriter out = response.getWriter();
        try {
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            } else if (urlp.cmd.equalsIgnoreCase("open")
                    || urlp.cmd.equalsIgnoreCase("open_xml")) {
                DbBpBlueprint blp = new DbBpBlueprint(urlp.prof.root_ent_id);
                if (blp.checkExist(con)) {
                    blp.get(con);
                    if(!blp.blp_path.equalsIgnoreCase("")){
	                    if (blp.blp_src_type.equalsIgnoreCase("url"))
	                        response.sendRedirect(blp.blp_path);
	                    else
	                        response.sendRedirect("../blueprint/" + urlp.prof.root_ent_id + "/"
	                                + blp.blp_path);
                    } else {
                        cwSysMessage tmp = new cwSysMessage("BLP001");
                        msgBox(ServletModule.MSG_STATUS,tmp,"javascript:window.close();",out);
                    }
                } else {
                    cwSysMessage tmp = new cwSysMessage("BLP001");
                    msgBox(ServletModule.MSG_STATUS,tmp,"javascript:window.close();",out);
                }
            } else if (urlp.cmd.equalsIgnoreCase("view")
                    || urlp.cmd.equalsIgnoreCase("view_xml")) {
                DbBpBlueprint blp = new DbBpBlueprint(urlp.prof.root_ent_id);
                blp.get(con);
                String xml = formatXML(blp.asXML(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("view")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("modify")
                    || urlp.cmd.equalsIgnoreCase("modify_xml")) {
                DbBpBlueprint blp = new DbBpBlueprint(urlp.prof.root_ent_id);
                blp.get(con);
                con.commit();
                String xml = formatXML(blp.asXML(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("modify")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("save")
                    || urlp.cmd.equalsIgnoreCase("save_xml")) {
                DbBpBlueprint blp = new DbBpBlueprint(urlp.prof.root_ent_id);
                urlp.getRequestType();
                String url_succeed = urlp.getRequestSucceedURL();
                int success = 0;
                if (urlp.requestType.equalsIgnoreCase("1")) {
                    //url
                    blp.blp_src_type = "URL";
                    blp.blp_path = urlp.getRequestURL();
                    blp.blp_source = "";
                    blp.blp_update_usr_id = String
                            .valueOf(urlp.prof.usr_id);
                    if (blp.checkExist(con)) {
                        blp.blp_update_timestamp = Timestamp.valueOf(urlp
                                .getUpdateTimestamp());
                        success = blp.update(con);
                    } else {
                        blp.blp_create_usr_id = String
                                .valueOf(urlp.prof.usr_id);
                        success = blp.Insert(con);
                    }
                } else if (urlp.requestType.equalsIgnoreCase("2")) {
                    //normal file
                    blp.blp_src_type = "FILE";
                    blp.blp_path = urlp.getRequestFilename();
                    blp.blp_source = "";
                    blp.blp_update_usr_id = String
                            .valueOf(urlp.prof.usr_id);
                    if (blp.checkExist(con)) {
                        blp.blp_update_timestamp = Timestamp.valueOf(urlp
                                .getUpdateTimestamp());
                        success = blp.update(con);
                    } else {
                        blp.blp_create_usr_id = String
                                .valueOf(urlp.prof.usr_id);
                        success = blp.Insert(con);
                    }
                    delCurrentFile(UploadPath);
                    getUploadFile(this.tmpUploadPath, UploadPath, UploadPath
                            + blp.blp_path);
                } else if (urlp.requestType.equalsIgnoreCase("3")) {
                    //zip file
                    blp.blp_src_type = "ZIPFILE";
                    blp.blp_source = urlp.getRequestFilename();
                    blp.blp_path = urlp.getRequestZipIndex();
                    blp.blp_update_usr_id = String
                            .valueOf(urlp.prof.usr_id);
                    if (blp.checkExist(con)) {
                        blp.blp_update_timestamp = Timestamp.valueOf(urlp
                                .getUpdateTimestamp());
                        success = blp.update(con);
                    } else {
                        blp.blp_create_usr_id = String
                                .valueOf(urlp.prof.usr_id);
                        success = blp.Insert(con);
                    }
                    delCurrentFile(UploadPath);
                    getUploadFile(this.tmpUploadPath, UploadPath, UploadPath
                            + blp.blp_source);
					try {                          
                    	dbUtils.unzip(UploadPath + blp.blp_source, UploadPath
                            .substring(0, UploadPath.length() - 1));
					} catch (FileNotFoundException e) {
						throw new cwSysMessage("ULG001");
					} catch (IOException e) {
						throw new cwSysMessage("MOD009");
					}
                } else if (urlp.requestType.equalsIgnoreCase("4")) {
                    success = 1;
                } else if (urlp.requestType.equalsIgnoreCase("5")) {
                    blp.get(con);
                    blp.blp_src_type = "ZIPFILE";
                    blp.blp_path = urlp.getRequestZipIndex2();
                    blp.blp_update_usr_id = urlp.prof.usr_id;
                    if (blp.checkExist(con)) {
                        blp.blp_update_timestamp = Timestamp.valueOf(urlp
                                .getUpdateTimestamp());
                        success = blp.update(con);
                    } else {
                        blp.blp_create_usr_id = String
                                .valueOf(urlp.prof.usr_id);
                        success = blp.Insert(con);
                    }
                } else if (urlp.requestType.equalsIgnoreCase("6")) {
                    if(blp.checkExist(con)){                   
                        blp.get(con);
                        if (blp.blp_src_type.equalsIgnoreCase("URL")) {
                        	/*
							* [2007/01/31 Chris Chan] 
							* Problem: This update is not compatible with Oracle because update an empty string to 
							* src_type will be regarded as setting to NULL to the field, and this violates the field 
							* definition of NOT NULL in bpblueprint table.
							* Resolution: Simply remove the record from table.

                            blp.blp_src_type = "";
                            blp.blp_path = "";
                            blp.blp_update_usr_id = urlp.prof.usr_id;
                            blp.blp_update_timestamp = Timestamp.valueOf(urlp.getUpdateTimestamp());
                            success = blp.update(con);
                            */
                            success = blp.delete(con);
                        } else {
                            String saveDir = "";
                            delCurrentFile(UploadPath);
                            /*
							* [2007/01/31 Chris Chan] 
							* Problem: This update is not compatible with Oracle because update an empty string to 
							* src_type will be regarded as setting to NULL to the field, and this violates the field 
							* definition of NOT NULL in bpblueprint table.
							* Resolution: Simply remove the record from table.

                            blp.blp_src_type = "";
                            blp.blp_path = "";
                            blp.blp_update_usr_id = urlp.prof.usr_id;
                            blp.blp_update_timestamp = Timestamp.valueOf(urlp.getUpdateTimestamp());
                            success = blp.update(con);
                            */
                            success = blp.delete(con);
                        }
                    }else{
                        success = 1;
                    }
                }
                if (success == 1) {
                    blp.get(con);
                    con.commit();
                    String xml = formatXML(blp.asXML(), MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("save")) {
                    	response.sendRedirect(url_succeed);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                } else {
                    con.rollback();
                    /*
                    cwSysMessage sms = new cwSysMessage("Failed");
                    msgBox(MSG_STATUS,sms,"",out);
                    */
                    blp.get(con);
                    String xml = formatXML(blp.asXML(), MODULENAME);
                    generalAsHtml(xml, out, "lrn_blp_view.xsl");
                }
            }
        } catch (cwSysMessage e) {
			try    {
				con.rollback();
				msgBox(ServletModule.MSG_STATUS, e, urlp.url_failure, out);
			} catch (SQLException sqlEx) {
				out.println("SQL error: " + sqlEx.getMessage());
			}    
        }
    }

    /**
     * save the upload file in tempdir,and check the file
     * 
     * @param tmpSaveDirPath
     * @param urlp
     * @throws cwException
     */
    private void procUploadedFiles(String tmpSaveDirPath, BlueprintReqParam urlp)
            throws cwException {
        try {
            // added to check file uploaded or not....
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                String fileName = multi.getFilesystemName(name);
                if (fileName != null && fileName.length() > 0) {
                    File fileUploaded = new File(tmpSaveDirPath, fileName);
                    FileInputStream fis = new FileInputStream(fileUploaded);
                    if (fis.read() != -1) {
                        this.bFileUploaded = true;
                    }
                    fis.close();
                }
            }
        } catch (IOException e) {
        	CommonLog.error(e.getMessage(),e);
        }
    }

    /**
     * move the file from tmpSaveDirPath to saveDirPath
     * 
     * @param tmpSaveDirPath
     *            temp directory
     * @param saveDirPath
     * @throws cwException
     */
    private void getUploadFile(String tmpSaveDirPath, String saveDirPath,
            String orgName) throws cwException {
        try {
            if (bFileUploaded) {
                dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
                File fileDir = new File(saveDirPath);
                if (fileDir.isDirectory()) {
                    String files[] = fileDir.list();
                    if (files.length == 1) {
                        File tmpFile = new File(fileDir.getPath()  + cwUtils.SLASH +  files[0]);
                        File dest = new File(orgName);
                        tmpFile.renameTo(dest);
                    }
                }
            }
        } catch (qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    private void delCurrentFile(String saveDir) {
        try {
            dbUtils.delDir(saveDir);
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
        }
    }
}