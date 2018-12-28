package com.cw.wizbank.JsonMod.courseware;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.importcos.ImportCos;
import com.cw.wizbank.importcos.ScormContentParser;
import com.cw.wizbank.qdb.CourseValidator;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.UploadListener;
import com.cw.wizbank.scorm.ZipManager;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;


public class Courseware {
    public final static String MANIFEST_FILE_NAME = "imsmanifest.xml";
    String[] AICC_FILE_EXT = new String[]{"AU","CRS","CST","DES","ORT"};
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
    
	public final static String FILE_UPLOAD_TYPE_CP = "cp";

    public AiccCourseStructureFile getAiccCourseStructureFile(String dir) {
        HashMap map = getAiccFileNames(dir);
        return new AiccCourseStructureFile(dir,map);
    }
    
    public HashMap getAiccFileNames(String dir) {
        int count = 0;
        HashMap map = new HashMap();
        File f = new File(dir);
        if (!f.isDirectory()) return null;
     
        String[] s = f.list();
        for (int i=0;i < s.length; i++) {
            if (s[i].indexOf(".") > 0) {
                String[] tmp = s[i].split("\\.");
                String ext = tmp[tmp.length-1];
                for (int j = 0; j < AICC_FILE_EXT.length; j++) {
                    if (AICC_FILE_EXT[j].equalsIgnoreCase(ext)) {
                        map.put(AICC_FILE_EXT[j], s[i]);
                        count++;
                    }
                }
            }
        }
        return map;
    }
    
    public String getScormMainefestFilePath(String dir) {
        String filepath = null;
        File f = new File(dir);
        if (!f.isDirectory()) return null;
     
        String[] s = f.list();
        for (int i=0;i < s.length; i++) {
            if (s[i].equalsIgnoreCase(MANIFEST_FILE_NAME)) {
                filepath = f.getAbsolutePath() + cwUtils.SLASH + s[i];
            }
        }
        return filepath;
    }
    
    public void insAiccMod(Connection con,HttpSession sess,loginProfile prof, WizbiniLoader wizbini, qdbEnv static_env, PrintWriter out,CoursewareModuleParam modParam, String tmpUploadPath) throws Exception {
        UploadListener listener = (UploadListener) sess.getAttribute("FILE_UPLOAD_LISTENER");
        String tempfile = tmpUploadPath + cwUtils.SLASH + modParam.getAicc_zip();
        String data_path = sdf.format(modParam.getCur_time());

        String content_folder = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getName();
        boolean isRelative =  wizbini.cfgSysSetupadv.getFileUpload().getContentDir().isRelative();
        String url = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getUrl();

        String extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + "content" + cwUtils.SLASH + data_path;

        if(!isRelative){
            if(content_folder.endsWith(cwUtils.SLASH)){
                extractPath = content_folder+data_path;
            }else{
                extractPath = content_folder+ cwUtils.SLASH + data_path;
            }

//            path = url;
        }else{

            if(content_folder.endsWith(cwUtils.SLASH)){
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + data_path;
            }else{
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + cwUtils.SLASH + data_path;
            }

           // url = "../" + url;
        }
        url = cwUtils.getFileURL(url);


        ZipManager.releaseZipToFile(tempfile, extractPath, listener.getFileUploadStats());
        ZipManager.delZipFile(tempfile);
        ZipManager.delZipFile(tmpUploadPath);
        
        AiccCourseStructureFile acsf = getAiccCourseStructureFile(extractPath);
        if (!checkAiccFileNames(acsf)) {
            listener.error("AICC01");
            return; 
        }
        
        String domain = modParam.getDomain();
        long modId = 0;
        dbModule dbmod = new dbModule();
        dbCourse dbcos = new dbCourse();
        dbmod.res_title = modParam.getMod_title();
        dbmod.res_desc = modParam.getMod_desc();
        dbmod.mod_type = modParam.getMod_subtype();
        dbmod.res_subtype = modParam.getMod_subtype();
        dbmod.mod_in_eff_start_datetime = modParam.getMod_eff_start_datetime();
        dbmod.mod_in_eff_end_datetime = modParam.getMod_eff_end_datetime();
        dbmod.res_status = modParam.getMod_status();
        dbcos.cos_res_id = modParam.getCourse_id();
        dbcos.res_id = dbcos.cos_res_id;
        dbcos.res_upd_user = prof.usr_id;
        dbmod.res_privilege = modParam.getRes_privilege();
        dbmod.mod_mobile_ind = modParam.getMod_mobile_ind();
        Vector vtCosResId = dbcos.getChildCosResId(con);
        Vector vtNewModId = new Vector();

        // for valid file 
        String validMsg = CourseValidator.validAiccCourse(acsf.crs_file, acsf.au_file, acsf.des_file, acsf.cst_file, acsf.ort_file).trim();                  
        if (!validMsg.equals("")) {
//            String xml = dbUtils.xmlHeader + "<errors>" + ((prof != null) ? prof.asXML() : "") + validMsg + "</errors>";
//            static_env.procXSLFile(xml, "course_error.xsl", out, null);
            listener.error("SCO002");
            return; 
        } else {
            // set upd user
            dbmod.res_upd_user = prof.usr_id;
            dbmod.res_usr_id_owner = prof.usr_id;
            Vector vtParentObj = new Vector();
            if (acsf.ort_file != null && acsf.ort_file.exists()) {
                modId = dbcos.insAiccAu(con, dbmod, domain, prof, acsf.crs_file.getAbsolutePath(), acsf.cst_file.getAbsolutePath(), acsf.des_file.getAbsolutePath(), acsf.au_file.getAbsolutePath(), acsf.ort_file.getAbsolutePath(), vtParentObj, true,url);
            } else {
                modId = dbcos.insAiccAu(con, dbmod, domain, prof, acsf.crs_file.getAbsolutePath(), acsf.cst_file.getAbsolutePath(), acsf.des_file.getAbsolutePath(), acsf.au_file.getAbsolutePath(), null, vtParentObj, true,url);
            }
            
            dbmod.mod_mod_res_id_parent = modId;
            for (int i = 0; i < vtCosResId.size(); i++) {
                long clsModId;
                if (acsf.ort_file != null && acsf.ort_file.exists()) {
                    clsModId = ((dbCourse) vtCosResId.get(i)).insAiccAu(con, dbmod, domain, prof, acsf.crs_file.getAbsolutePath(), acsf.cst_file.getAbsolutePath(), acsf.des_file.getAbsolutePath(), acsf.au_file.getAbsolutePath(), acsf.ort_file.getAbsolutePath(), vtParentObj, true,url);
                } else {
                    clsModId = ((dbCourse) vtCosResId.get(i)).insAiccAu(con, dbmod, domain, prof, acsf.crs_file.getAbsolutePath(), acsf.cst_file.getAbsolutePath(), acsf.des_file.getAbsolutePath(), acsf.au_file.getAbsolutePath(), null, vtParentObj, true,url);
                }
                vtNewModId.addElement(new Long(clsModId));
            }
        }
        dbResource.modifyAiccAuFile(acsf.au_file, dbmod.res_src_link);
        String cos_structure_xml = modParam.getCourse_struct_xml_1();
        // insert successful
        cos_structure_xml = dbUtils.subsitute(cos_structure_xml, "$mod_id_" , Long.toString(modId));
        cos_structure_xml = dbUtils.subsitute(cos_structure_xml, "$mod_title_" , dbUtils.esc4XML(dbmod.res_title));
        cos_structure_xml = dbUtils.subsitute(cos_structure_xml, "$mod_type_" , dbmod.mod_type);
        dbcos.cos_structure_xml = cos_structure_xml;
        dbcos.cos_structure_json=static_env.transformXML(dbcos.cos_structure_xml.replaceAll("&quot;", " "), "cos_structure_json_js.xsl", null);
        dbcos.updCosStructure(con);
        
        for (int i = 0; i < vtCosResId.size(); i++) {
            dbCourse cosObj = (dbCourse) vtCosResId.get(i);
            cosObj.res_upd_user = prof.usr_id;
            cosObj.updCosStructureFromParent(con, dbcos.cos_structure_xml);
        }
        listener.setNewResId(dbmod.mod_res_id);
        listener.finish();
        listener.returnMsg("GEN001");
    }
    
    public void insAiccRes(Connection con,HttpSession sess,loginProfile prof, WizbiniLoader wizbini, qdbEnv static_env, PrintWriter out,CoursewareModuleParam modParam, String tmpUploadPath) throws Exception {
        UploadListener listener = (UploadListener) sess.getAttribute("FILE_UPLOAD_LISTENER");
        String tempfile = tmpUploadPath + cwUtils.SLASH + modParam.getAicc_zip();
        String data_path = sdf.format(modParam.getCur_time());
        String content_folder = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getName();
        boolean isRelative =  wizbini.cfgSysSetupadv.getFileUpload().getContentDir().isRelative();
        String url = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getUrl();

        String extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + "content" + cwUtils.SLASH + data_path;
//        String path = "../" + url;

        if(!isRelative){
            if(content_folder.endsWith(cwUtils.SLASH)){
                extractPath = content_folder+data_path;
            }else{
                extractPath = content_folder+ cwUtils.SLASH + data_path;
            }

//            path = url;
        }else{

            if(content_folder.endsWith(cwUtils.SLASH)){
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + data_path;
            }else{
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + cwUtils.SLASH + data_path;
            }

           // url = "../" + url;
        }
        url = cwUtils.getFileURL(url);

        ZipManager.releaseZipToFile(tempfile, extractPath, listener.getFileUploadStats());
        ZipManager.delZipFile(tempfile);
        ZipManager.delZipFile(tmpUploadPath);

        AiccCourseStructureFile acsf = getAiccCourseStructureFile(extractPath);
        if (!checkAiccFileNames(acsf)) {
            listener.error("AICC01");
            return; 
        }
        dbObjective dbobj = new dbObjective();
        dbobj.obj_id = modParam.getObj_id();
        if (wizbini.cfgTcEnabled) {
            AcTrainingCenter acTc = new AcTrainingCenter(con);
            String code = acTc.hasObjInMgtTc(prof.usr_ent_id, dbobj.obj_id, prof.current_role);
            if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
                throw new qdbErrMessage(code);
            }
        }
        AcResources acRes = new AcResources(con);
        if (!acRes.hasManagePrivilege(prof.usr_ent_id, dbobj.obj_id, prof.current_role)) {
            throw new qdbErrMessage("RES003");
        }
        // set upd user
        dbResource dbres = new dbResource();
        dbres.res_title = modParam.getRes_title();
        dbres.res_desc = modParam.getRes_desc();
        dbres.res_type = modParam.getRes_type();
        dbres.res_subtype = modParam.getRes_subtype();
        dbres.res_upd_user = prof.usr_id;
        dbres.res_status = modParam.getRes_status();
        dbres.res_usr_id_owner = prof.usr_id;
        dbres.res_src_type = modParam.getRes_src_type();
        dbres.res_difficulty = modParam.getRes_difficulty();
        dbres.res_privilege = modParam.getRes_privilege();

        // read the aicc files and get the required info. before creating the resource
        String[] robs = new String[] { modParam.getObj_id() + "" };

        // for valid file
        String validMsg = CourseValidator.validAiccCourse(acsf.crs_file, acsf.au_file, acsf.des_file, acsf.cst_file, acsf.ort_file).trim();
        if (!validMsg.equals("")) {
//            String xml = dbUtils.xmlHeader + "<errors>" + ((prof != null) ? prof.asXML() : "") + validMsg + "</errors>";
//            static_env.procXSLFile(xml, "course_error.xsl", out, null);
            listener.error("SCO002");
            return;
        } else {
            dbres.ins_ssc(con, robs, prof, acsf.des_file.getAbsolutePath(), acsf.au_file.getAbsolutePath());
            String link = transSrcLink(wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl(), dbres.res_src_link, dbres.res_id);
            dbres.updResSrcLink(con, dbres.res_id, link);
            dbResource.modifyAiccAuFile(acsf.au_file, link);
        }
        String saveDirPath = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + dbres.res_id;
        File f = new File(wizbini.getFileUploadResDirAbs() + dbUtils.SLASH + Long.toString(dbres.res_id));
        if (!f.exists())
            f.mkdir();
        dbUtils.copyDir(extractPath, saveDirPath);
        dbUtils.delDir(extractPath);
        String courseName = acsf.des.substring(0, acsf.des.indexOf("."));
        dbres.create_SSC_info_file(wizbini.getFileUploadResDirAbs(), courseName);
        listener.finish();
        listener.returnMsg("GEN001");
    }
    
    public void updAiccRes(Connection con,HttpSession sess,loginProfile prof, WizbiniLoader wizbini, qdbEnv static_env, PrintWriter out,CoursewareModuleParam modParam, String tmpUploadPath) throws Exception {
        UploadListener listener = (UploadListener) sess.getAttribute("FILE_UPLOAD_LISTENER");
        String tempfile = tmpUploadPath + cwUtils.SLASH + modParam.getAicc_zip();
        String data_path = sdf.format(modParam.getCur_time());
        String content_folder = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getName();
        boolean isRelative =  wizbini.cfgSysSetupadv.getFileUpload().getContentDir().isRelative();

        String url = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getUrl();

        String extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + "content" + cwUtils.SLASH + data_path;
//        String path = "../" + url;

        if(!isRelative){
            if(content_folder.endsWith(cwUtils.SLASH)){
                extractPath = content_folder+data_path;
            }else{
                extractPath = content_folder+ cwUtils.SLASH + data_path;
            }

//            path = url;
        }else{

            if(content_folder.endsWith(cwUtils.SLASH)){
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + data_path;
            }else{
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + cwUtils.SLASH + data_path;
            }

            //url = "../" + url;
        }
        url = cwUtils.getFileURL(url);
        AcResources acRes = new AcResources(con);

        dbResource dbres = new dbResource();
        dbres.res_id = modParam.getRes_id();
        dbres.res_title = modParam.getRes_title();
        dbres.res_desc = modParam.getRes_desc();
        dbres.res_type = modParam.getRes_type();
        dbres.res_status = modParam.getRes_status();
        dbres.res_upd_date = modParam.getRes_timestamp();
        dbres.res_subtype = modParam.getRes_subtype();
        dbres.res_upd_user = prof.usr_id;
        dbres.res_usr_id_owner = prof.usr_id;
        dbres.res_src_type = modParam.getRes_src_type();
        dbres.res_difficulty = modParam.getRes_difficulty();
        dbres.res_privilege = modParam.getRes_privilege();
        
        if(wizbini.cfgTcEnabled) {
            AcTrainingCenter acTc = new AcTrainingCenter(con);
            String code = acTc.hasResInMgtTc(prof.usr_ent_id, dbres.res_id, prof.current_role);
            if(!code.equals(dbResource.CAN_MGT_RES)) {
                throw new qdbErrMessage(code);
            }
        }
        if (!acRes.hasResPrivilege(prof.usr_ent_id,dbres.res_id, prof.current_role)){
            throw new qdbErrMessage("RES002");
        }
        ZipManager.releaseZipToFile(tempfile, extractPath, listener.getFileUploadStats());
        ZipManager.delZipFile(tempfile);
        ZipManager.delZipFile(tmpUploadPath);

        AiccCourseStructureFile acsf = getAiccCourseStructureFile(extractPath);
        if (!checkAiccFileNames(acsf)) {
            listener.error("AICC01");
            return; 
        }
        
        String validMsg = CourseValidator.validAiccCourse(acsf.crs_file, acsf.au_file,acsf.des_file,acsf.cst_file, acsf.ort_file).trim();
        if (!validMsg.equals("")) {
//            String xml = dbUtils.xmlHeader + "<errors>" + ((prof != null) ? prof.asXML() : "") + validMsg + "</errors>";
//            static_env.procXSLFile(xml, "course_error.xsl", out, null);
            listener.error("SCO002");
            return; 
        }
        dbres.upd_ssc(con, prof, acsf.des_file.getAbsolutePath(), acsf.au_file.getAbsolutePath());
        String link = transSrcLink(wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl(), dbres.res_src_link, dbres.res_id);
        dbres.updResSrcLink(con, dbres.res_id, link);
        dbResource.modifyAiccAuFile(acsf.au_file, link);

        String saveDirPath = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + dbres.res_id;
        File f = new File(saveDirPath);
        if (!f.exists())
            f.delete();
        dbUtils.copyDir(extractPath, saveDirPath);
        dbUtils.delDir(extractPath);
        
        if (acsf.crs != null) {
            String courseName = acsf.des.substring(0, acsf.des.indexOf("."));
            dbres.create_SSC_info_file(wizbini.getFileUploadResDirAbs(), courseName);
        }
        listener.finish();
        listener.returnMsg("GEN001");
    }
    
    public void insScorm(Connection con,HttpSession sess,loginProfile prof, WizbiniLoader wizbini, qdbEnv static_env, HttpServletResponse response,CoursewareModuleParam modParam, String tmpUploadPath) throws Exception {
        
        long course_id = 0;
        String cmd = "";
        String[] robs = {};
        long obj_id = 0;
        dbResource dbres = new dbResource();
        dbres.res_upd_user = prof.usr_id;
        course_id = modParam.getCos_id();
        cmd = modParam.getCmd();
        obj_id = modParam.getObj_id();
        robs = new String[]{obj_id + ""};
        dbres.res_id = modParam.getRes_id();
        dbres.res_lan = modParam.getRes_lan();
        dbres.res_type = modParam.getRes_type();
        dbres.res_subtype = modParam.getRes_subtype();
        dbres.res_cnt_subtype = modParam.getRes_cnt_subtype();
        dbres.res_title = modParam.getRes_title();
        dbres.res_desc = modParam.getRes_desc();
        dbres.res_instructor_name = modParam.getRes_instructor_name();
        dbres.res_instructor_organization = modParam.getRes_instructor_organization();
        dbres.res_format = modParam.getRes_format();
        dbres.res_difficulty = modParam.getRes_difficulty();
        dbres.res_privilege = modParam.getRes_privilege();
        dbres.res_status = modParam.getRes_status();
        dbres.res_src_type = modParam.getRes_src_type();
        dbres.res_duration = modParam.getRes_duration();
        dbres.res_upd_date = modParam.getRes_timestamp();
        dbres.res_usr_id_owner = prof.usr_id;
        dbres.res_sco_version = modParam.getSco_ver();

        UploadListener listener = (UploadListener) sess.getAttribute("FILE_UPLOAD_LISTENER");
        String tempfile = tmpUploadPath + cwUtils.SLASH + modParam.getAicc_zip();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String data_path = sdf.format(modParam.getCur_time());
        String extractPath = null;
        String path = null;
        String content_folder = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getName();
        boolean isRelative =  wizbini.cfgSysSetupadv.getFileUpload().getContentDir().isRelative();
        String url = wizbini.cfgSysSetupadv.getFileUpload().getContentDir().getUrl();



        if(!isRelative){
            if(content_folder.endsWith(cwUtils.SLASH )){
                extractPath = content_folder + data_path;
            }else{
                extractPath = content_folder + cwUtils.SLASH + data_path;
            }
//            path = url;
        }else{
            if(content_folder.endsWith(cwUtils.SLASH )){
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + data_path;
            }else{
                extractPath  = wizbini.getWebDocRoot() + cwUtils.SLASH + content_folder + cwUtils.SLASH + data_path;
            }


//            path = "../" + url;
        }
        path = cwUtils.getFileURL(url);
        
        if (dbres.res_sco_version != null && dbres.res_sco_version.equalsIgnoreCase(ScormContentParser.SCORM_VERSION_2004)){
        	if(isRelative){
        		 path = "../" + path;
        	}
        }
        if (obj_id > 0) {
            if(wizbini.cfgTcEnabled) {
                AcTrainingCenter acTc = new AcTrainingCenter(con);
                String code = acTc.hasObjInMgtTc(prof.usr_ent_id, obj_id, prof.current_role);
                if(!code.equals(dbObjective.CAN_MGT_OBJ)) {
                    throw new qdbErrMessage(code);
                }
            }
            AcResources acRes = new AcResources(con);
            if(!acRes.hasManagePrivilege(prof.usr_ent_id,obj_id, prof.current_role)){
                throw new qdbErrMessage("RES003");
            }
            if (!dbResource.isExistObective(con, obj_id)) {
                throw new qdbErrMessage(dbResource.RES_FOLDER_NOT_EXIST);
            }
        }
        String manifestFilePath = ZipManager.releaseZipToFile(tempfile, extractPath, listener.getFileUploadStats()); //解压zip文件
        if (manifestFilePath == null) {
            listener.error("SCO002");
            return;
        }
        ZipManager.delZipFile(tempfile); //删除临时上传的文件
        ZipManager.delZipFile(tmpUploadPath);
        ZipManager.modiListFilePath(extractPath, manifestFilePath, data_path, path ); //修改清单文件相关路径
        long new_mod_id = 0;
        if(cmd.equalsIgnoreCase("ins_mod_scorm")){
            new_mod_id = importCourse(con, prof, (int) course_id, extractPath + cwUtils.SLASH + manifestFilePath, dbres.res_sco_version, true, wizbini, modParam);  //根据清单文件导入课件
            
            listener.setNewResId(new_mod_id);
            listener.returnMsg("SCO001");
        }else if(cmd.equalsIgnoreCase("ins_res_scorm") || cmd.equalsIgnoreCase("upd_res_scorm")){
            if(dbres.res_subtype.equalsIgnoreCase("RES_SCO")){
                if(cmd.equalsIgnoreCase("ins_res_scorm")){
                    dbres.ins_res2(con, robs, prof);
                    dbres.ins_res_scorm(con, prof, dbres.res_id, extractPath + cwUtils.SLASH + manifestFilePath, null, true, wizbini);
                    listener.setNewResId(dbres.res_id);
                    listener.returnMsg("GEN001");
                }else if(cmd.equalsIgnoreCase("upd_res_scorm")){
                    dbres.upd_res_scorm(con, prof, dbres.res_id, extractPath + cwUtils.SLASH + manifestFilePath, null, true, wizbini);
                    dbres.upd_res(con, prof, false);
                    listener.returnMsg("GEN003");
                }
                String destination = wizbini.getFileUploadResDirAbs() + dbUtils.SLASH + dbres.res_id;
                dbUtils.copyFile(extractPath + cwUtils.SLASH + manifestFilePath, destination);
                dbres.create_SSC_info_file(wizbini.getFileUploadResDirAbs(), ZipManager.MANIFEST_FILE_NAME);
            }
        }
        listener.finish();
    }
    /**
     * 上传课件包
     * @param con
     * @param sess
     * @param wizbini
     * @param modParam
     * @param tmpUploadPath
     * @throws Exception
     */
    public void uploadOfflinePackage(Connection con,HttpSession sess, WizbiniLoader wizbini, CoursewareModuleParam modParam, String tmpUploadPath) throws Exception {
        
        long itm_id = modParam.getCos_id();
        UploadListener listener = (UploadListener) sess.getAttribute("FILE_UPLOAD_LISTENER");
        //如果上传文件临时路径为空或文件名为空则提示错误
        if(tmpUploadPath == null || tmpUploadPath.length() <=0 || modParam.getAicc_zip() == null || modParam.getAicc_zip().length() <= 0){
        	listener.error("PKG001");
        }
        //创建一个上传文件临时路径的实例
        String tempfilePath = tmpUploadPath + cwUtils.SLASH + modParam.getAicc_zip();
        File tempfile = new File(tempfilePath);
        //创建上传文件要保存的路径
        String extractPath = wizbini.getAppnRoot() + cwUtils.SLASH + "www" + cwUtils.SLASH + FILE_UPLOAD_TYPE_CP + cwUtils.SLASH + itm_id;
        File extractPathDir = new File(extractPath);
		//检测并创建
		dbUtils.delDir(extractPath);
		extractPathDir.mkdirs();
		
		
        String old_file_name = tempfile.getName();//取得上传文件名
        long t = System.currentTimeMillis();
		String new_file_name = Long.toString(t) + "." + FILE_UPLOAD_TYPE_CP;
		File newfilePath = new File(extractPath + cwUtils.SLASH + new_file_name);//创建一个不重复的新文件名
		tempfile.renameTo(newfilePath);//剪切并重命名
		//保存到数据库中
		aeItem itm = new aeItem();
		itm.updateOfflinePkg(con, itm_id, new_file_name, old_file_name);
		listener.returnMsg("PKG002");
        listener.finish();
    }

    private long importCourse(Connection con, loginProfile prof, int cos_id, String imsmanifestPath, String sco_version, boolean fromzip, WizbiniLoader wizbini, CoursewareModuleParam modParam) throws IOException, SQLException,
            qdbException, qdbErrMessage, cwSysMessage, cwException {
        ImportCos myImportCos = new ImportCos();
        //for scorm 2004 
        
        dbCourse myDbCourse = new dbCourse();
        myDbCourse.cos_res_id = cos_id;
        myDbCourse.res_id = cos_id;

        myDbCourse.res_title = "SCORM";
        myDbCourse.get(con);
        
            
        if (sco_version.equalsIgnoreCase(ScormContentParser.SCORM_VERSION_2004)) {
        	myImportCos.importScorm2004(con, prof, cos_id, imsmanifestPath, null, false, false, fromzip, wizbini, myDbCourse);
        } else {
        	myImportCos.importScorm1_2(con, prof, cos_id, imsmanifestPath, null, false, false, fromzip, myDbCourse);
        }
        dbModule dbmod_ = new dbModule();
        dbmod_.mod_res_id = myImportCos.new_mod_id;
        dbmod_.mod_mobile_ind = modParam.getMod_mobile_ind();
        dbmod_.mod_test_style = modParam.getMod_test_style();
        dbmod_.updateMobileInd(con);
        dbCourse dbcos = new dbCourse(cos_id);
        dbcos.get(con);
        Vector vtCosResId = dbcos.getChildCosResId(con);
        List<Long> resIdListInserted = myImportCos.getResIdListInserted();
        for (int i = 0; i < vtCosResId.size(); i++) {
            for (int j = 0; j < resIdListInserted.size(); j++) {
            	long modResId = ((Long)resIdListInserted.get(j)).longValue();
            	
                dbModule dbmod = new dbModule();
                dbmod.mod_res_id = modResId;
                dbmod.getAllAicc(con);
                
                dbmod.mod_mod_res_id_parent = modResId;
                ((dbCourse) vtCosResId.get(i)).insModule(con, dbmod, null, prof);
                dbmod.updAicc(con, prof, "", "", "",
                        dbmod.mod_time_limit_action, dbmod.mod_web_launch, "",
                        dbmod.res_desc, "2.2");
            }
            ((dbCourse) vtCosResId.get(i)).updAiccCos(con, prof, "", "2.2", "", -1, true, true, true);
            ((dbCourse) vtCosResId.get(i)).updCosStructureFromParent(con,dbcos.cos_structure_xml);
        }
        return myImportCos.new_mod_id;
    }
    
    public String transSrcLink(String res_dir, String src_link, long res_id) {
        if (src_link.toLowerCase().startsWith("http://")) {
            return src_link;
        }
        src_link = src_link.replaceAll("\\\\", "/");
        String new_src_link = "";
        if (src_link.startsWith("/") || src_link.startsWith(".")) {
            new_src_link = cwUtils.getFileURL(res_dir) + res_id + src_link.substring(src_link.indexOf("/"));
        } else {
            new_src_link = cwUtils.getFileURL(res_dir) + res_id + "/" + src_link;
        }
        return new_src_link;
    }
    
    public boolean checkAiccFileNames(AiccCourseStructureFile acsf) {
        if ((acsf.crs == null || acsf.crs.length() == 0)
                || (acsf.cst == null || acsf.cst.length() == 0) 
                || (acsf.des == null || acsf.des.length() == 0) 
                || (acsf.au == null || acsf.au.length() == 0)) {
            return false;
        }
        return true;
    }
    
    public static class AiccCourseStructureFile {
        public String crs;

        public String cst;

        public String des;

        public String au;

        public String ort;

        public File crs_file;

        public File cst_file;

        public File des_file;

        public File au_file;

        public File ort_file;

        public AiccCourseStructureFile(String uplod_path, HashMap file_map) {
            crs = (String) file_map.get("CRS");
            if (crs != null) crs_file = new File(uplod_path + cwUtils.SLASH + crs);
            cst = (String) file_map.get("CST");
            if (cst != null) cst_file = new File(uplod_path + cwUtils.SLASH + cst);
            des = (String) file_map.get("DES");
            if (des != null) des_file = new File(uplod_path + cwUtils.SLASH + des);
            au = (String) file_map.get("AU");
            if (au != null) au_file = new File(uplod_path + cwUtils.SLASH + au);
            ort = (String) file_map.get("ORT");
            if (ort != null) ort_file = new File(uplod_path + cwUtils.SLASH + ort);
        }
    }
}

