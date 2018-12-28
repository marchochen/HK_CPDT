package com.cw.wizbank.course;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.content.Glossary;
import com.cw.wizbank.content.Reference;
import com.cw.wizbank.db.DbCtGlossary;
import com.cw.wizbank.db.DbCtReference;
import com.cw.wizbank.qdb.dbAssignment;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbFaq;
import com.cw.wizbank.qdb.dbForum;
import com.cw.wizbank.qdb.dbForumMessage;
import com.cw.wizbank.qdb.dbForumTopic;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleSpec;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;

public class CourseContentUtils {
    //Transformer cos_structure_xml xsl
    private static final String cos_structureXSL = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"xml\" omit-xml-declaration=\"yes\"/><xsl:param name=\"element_to_copy\"></xsl:param><xsl:template match=\"tableofcontents\"><xsl:copy-of select=\"item[@identifier=$element_to_copy]\"/></xsl:template><xsl:template match=\"item\"><xsl:for-each select=\"//item[@identifier]\"><xsl:value-of select=\"@identifier\"/><xsl:text>,</xsl:text></xsl:for-each><xsl:for-each select=\"//item[@identifierref]\"><xsl:value-of select=\"@identifierref\"/><xsl:text>,</xsl:text></xsl:for-each></xsl:template></xsl:stylesheet>";
    
    /**
     * duplicate all content of a course to another course (probably a class)
     * and retain parent relation
     * @param con
     * @param parent_itm_id the course from which content is to be duplicated
     * @param child_itm_id the course to receive the duplicated content 
     * @param prof profile of the current user
     */
    public static Hashtable[] propagateCourseContent(Connection con, long parent_itm_id, long child_itm_id, loginProfile prof, boolean data_trans, String resource_folder )
    throws SQLException, qdbException, cwException, cwSysMessage, qdbErrMessage, IOException, CloneNotSupportedException {
            
    return propagateCourseContentForBatch(con, parent_itm_id, child_itm_id, 0, 0, prof, data_trans, resource_folder , null); 

    }
    /**
         * duplicate all content of a course to another course (probably a class)
         * and retain parent relation
         * @param con
         * @param parent_itm_id the course from which content is to be duplicated
         * @param child_itm_id the course to receive the duplicated content
         * @param parent_cos_res_id the course id from which content is to be duplicated     
         * @param child_cos_res_id the course to receive the duplicated content    
         * @param prof profile of the current user
         * @param vtParentModId vector of mod id to be duplicated 
         * 
         */
                                                                                
    public static Hashtable[] propagateCourseContentForBatch(Connection con, long parent_itm_id, long child_itm_id, long parent_cos_res_id, long child_cos_res_id, loginProfile prof, boolean data_trans, String resource_folder , Vector vtParentMod)
        throws SQLException, qdbException, cwException, cwSysMessage, qdbErrMessage, IOException , CloneNotSupportedException{

        if (parent_cos_res_id == 0){
            parent_cos_res_id = dbCourse.getCosResId(con, parent_itm_id);
        }
        if (child_cos_res_id == 0){
            child_cos_res_id = dbCourse.getCosResId(con, child_itm_id);
        }
        if (vtParentMod == null){
            vtParentMod = dbResourceContent.getCourseModule(con, parent_cos_res_id);
        }

        Vector vtNewModId = new Vector();

        dbCourse dbChildCos = new dbCourse(child_cos_res_id);
        Timestamp curTime = cwSQL.getTime(con);
        Hashtable[] htContentIds = copyMod(con, vtParentMod, dbChildCos, data_trans, false, prof, resource_folder, vtNewModId, null, null);
        dbCourse dbParentCos = new dbCourse(parent_cos_res_id);
        dbParentCos.get(con);
        dbChildCos.updCosStructureFromParent(con, dbParentCos.cos_structure_xml);

        ModulePrerequisiteManagement modPreManagement = new ModulePrerequisiteManagement();
        modPreManagement.copyCosModPrerequisite(con, vtNewModId, prof.usr_id, curTime);
        aeItem aeitem = new aeItem();
        aeitem.genCmtFromParent(con, parent_itm_id, child_itm_id, prof, data_trans);
        aeitem.genLessonFromParent(con, parent_itm_id, child_itm_id, prof);
        return htContentIds;
    }
    
    private static boolean disFolder(long parent_res_id, long ch_res_id, String resource_folder) throws IOException {
        File folder = (new File(resource_folder));
        if (folder != null && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                File res_folder = files[i];
                String foldername = res_folder.getName();
                long res_id = 0;
                try {
                    res_id = Long.parseLong(foldername.trim());
                } catch (Exception e) {
                    res_id = 0;
                }
                if (res_id == parent_res_id) {
                    CourseContentUtils.copyFolder(resource_folder + parent_res_id, resource_folder + ch_res_id);
                }
            }
        }
        return true;
    }
    
    private static void copyFolder(String oldPath, String newPath) throws IOException {
        (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
        File a = new File(oldPath);
        String[] file = a.list();
        File temp = null;
        for (int i = 0; i < file.length; i++) {
            if (oldPath.endsWith(File.separator)) {
                temp = new File(oldPath + file[i]);
            } else {
                temp = new File(oldPath + File.separator + file[i]);
            }
            if (temp.isFile()) {
                FileInputStream input = new FileInputStream(temp);
                FileOutputStream output = new FileOutputStream(newPath + "/"
                        + (temp.getName()).toString());
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = input.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                output.flush();
                output.close();
                input.close();
            }
            if (temp.isDirectory()) {// 如果是子文件夹
                copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
            }
        }
    }  
    
    private static void tansForm(Connection con, long parent_res_id, long res_id)
            throws SQLException, qdbException, qdbErrMessage, cwException {
        List all_topic = dbForumTopic.getAllTopicByResID(con, parent_res_id);
        for (int i = 0; i < all_topic.size(); i++) {
            dbForumTopic topic = (dbForumTopic) all_topic.get(i);
            loginProfile prof = new loginProfile();
            prof.usr_id = topic.fto_usr_id;
            long old_fto_id = topic.fto_id;
            topic.fto_res_id = res_id;     
            topic.ins(con, prof, null);
            Hashtable fmg_id_map = new Hashtable();
            List all_fmg = dbForumMessage.getTopicMgs(con, old_fto_id);
            for (int j = 0; j < all_fmg.size(); j++) {
                dbForumMessage fmg = (dbForumMessage) all_fmg.get(j);
                prof.usr_id = fmg.fmg_usr_id;
                long old_fmg_id = fmg.fmg_id;
                fmg.fmg_fto_id = topic.fto_id;
                fmg.fmg_fto_res_id = res_id;
                if (fmg.fmg_fmg_id_parent != 0
                        && fmg_id_map.get(new Long(fmg.fmg_fmg_id_parent)) != null) {
                    fmg.fmg_fmg_id_parent = ((Long) fmg_id_map.get(new Long(
                            fmg.fmg_fmg_id_parent))).longValue();
                    fmg.fmg_fmg_fto_id_parent = topic.fto_id;
                    fmg.fmg_fmg_fto_res_id_parent = res_id;

                } else {
                    fmg.fmg_fmg_id_parent = 0;
                }
                fmg.ins(con, prof);
                fmg_id_map.put(new Long(old_fmg_id), new Long(fmg.fmg_id));
            }
        }
    }

    private static Hashtable[] copyMod(Connection con, Vector vtSrcMod, dbCourse tagCos, boolean data_trans, boolean isClone, loginProfile prof, String uploadDir, Vector vtNewModId, Hashtable hashModId, Hashtable hashObjId) 
    throws qdbException, qdbErrMessage, cwException, cwSysMessage, IOException, SQLException, CloneNotSupportedException {
        Timestamp curTime = cwSQL.getTime(con);

        // parent mod id as key, new mod_id as value
        Hashtable htModId = new Hashtable();
        // parent mod id as key, hashtable of old_obj_id - new obj_id as value  
        Hashtable htObjId = new Hashtable();
        if (vtNewModId == null) {
            vtNewModId = new Vector();
        }

        long new_mod_id;

        for (int i = 0; i < vtSrcMod.size(); i++) {
            Hashtable htModObjId = null;
            dbModule dbmod = (dbModule) ((dbModule) vtSrcMod.get(i)).clone();
            String parent_mod_status = dbmod.res_status;
            long parent_mod_res_id = dbmod.mod_res_id;
            float mod_max_score = dbmod.mod_max_score;

            // 需要重设的属性
            dbmod.res_create_date = curTime;
            dbmod.res_upd_date = curTime;
            dbmod.res_upd_user = prof.usr_id;
            new_mod_id = 0;
            dbmod.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
            dbmod.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
            dbmod.res_status = dbModule.RES_STATUS_OFF;
            if (isClone) {
                dbmod.mod_mod_res_id_parent = 0;
            } else {
                if (hashModId != null) {
                    dbmod.mod_mod_res_id_parent = ((Long) hashModId.get(new Long(parent_mod_res_id))).longValue();
                } else {
                    dbmod.mod_mod_res_id_parent = parent_mod_res_id;
                }
            }

            if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_ASS)) {
                dbAssignment dbass = new dbAssignment();
                dbass.ass_res_id = dbmod.mod_res_id;
                dbass.get(con);
                dbass.res_upd_date = curTime;
                dbass.res_upd_user = prof.usr_id;
                if (data_trans) {
                    dbass.mod_in_eff_start_datetime = dbmod.mod_eff_start_datetime;
                    dbass.mod_in_eff_end_datetime = dbmod.mod_eff_end_datetime;
                } else {
                    dbass.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
                    dbass.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
                }
                if (isClone) {
                    // 导入后需要重设的属性
                    dbass.ass_due_datetime = null;
                    dbass.ass_due_date_day = 0;
                    dbass.res_status = dbModule.RES_STATUS_OFF;
                    dbass.mod_mod_res_id_parent = 0;
                } else {
                    if (hashModId != null) {
                        dbass.mod_mod_res_id_parent = ((Long) hashModId.get(new Long(parent_mod_res_id))).longValue();
                    } else {
                        dbass.mod_mod_res_id_parent = parent_mod_res_id;
                    }
                }
                if (data_trans) {
                    dbass.res_create_date = dbmod.res_create_date;
                } else {
                    dbass.res_create_date = curTime;
                }
                new_mod_id = tagCos.insAssignment(con, dbass, null, prof, null, null);
            } else if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_AICC_U)
                    || dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_NETG_COK)
                    || dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SCO)) {
                htModObjId = new Hashtable();
                dbmod.getAicc(con);
                dbmod.res_create_date = curTime;
                dbmod.res_upd_date = curTime;
                dbmod.res_upd_user = prof.usr_id;
                if (isClone) {
                    dbmod.res_status = dbModule.RES_STATUS_OFF;
                } else {
                    if (hashModId != null) {
                        dbmod.mod_mod_res_id_parent = ((Long) hashModId.get(new Long(parent_mod_res_id))).longValue();
                    } else {
                        dbmod.mod_mod_res_id_parent = parent_mod_res_id;
                    }
                }
                new_mod_id = tagCos.insModule(con, dbmod, null, prof);
                dbmod.updAicc(con, prof, dbmod.mod_core_vendor, dbmod.mod_password, dbmod.mod_import_xml, dbmod.mod_time_limit_action, dbmod.mod_web_launch, dbmod.mod_vendor, dbmod.res_desc, dbmod.mod_aicc_version);
                // copy Objective records and ResourceObjective records
                if (!dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SCO)) {
                    dbObjective objtv = new dbObjective();
                    long[] int_mod_id_list = new long[] { new_mod_id };
                    long[] int_obj_id_list = new long[1];
                    Vector vt = dbResourceObjective.getObjId(con, parent_mod_res_id);
                    Vector vtObjId = null;
                    if (!isClone && hashObjId != null) {
                        Hashtable hsObjId = (Hashtable) hashObjId.get(new Long(parent_mod_res_id));
                        Enumeration e_ht_obj_id = hsObjId.elements();
                        vtObjId = new Vector();
                        while (e_ht_obj_id.hasMoreElements()) {
                            vtObjId.addElement(e_ht_obj_id.nextElement());
                        }
                    }
                    for (int j = 0; j < vt.size(); j++) {
                        dbResourceObjective resObjtv = (dbResourceObjective) vt.get(j);
                        objtv.obj_id = resObjtv.rob_obj_id;
                        objtv.getAicc(con);
                        if (isClone) {
                            objtv.obj_obj_id_parent = 0;
                        } else {
                            if (hashObjId != null) {
                                objtv.obj_obj_id_parent = ((Long) vtObjId.get((vt.size() - 1) - j)).longValue();
                            } else {
                                objtv.obj_obj_id_parent = objtv.obj_id;
                            }
                        }
                        int_obj_id_list[0] = objtv.insAicc(con, prof);
                        resObjtv.insResObj(con, int_mod_id_list, int_obj_id_list);
                        if (objtv.obj_obj_id_parent == 0) {
                            htModObjId.put(new Long(j), new Long(int_obj_id_list[0]));
                        } else {
                            htModObjId.put(new Long(objtv.obj_obj_id_parent), new Long(int_obj_id_list[0]));
                        }
                    }
                }
                
                if(tagCos.cos_max_normal == 0){
                    tagCos.updAiccCos(con,prof,null,tagCos.cos_aicc_version,tagCos.cos_vendor, -1, false,false,false);
                }
                
            } else if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_TST)
                     || dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)
                     || dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)) {
                if (!(dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SVY) //没调用同一个调查问卷模板
                && dbmod.mod_mod_id_root != 0
                && !dbModule.checkModRootIdUniqueInCos(con, tagCos.cos_res_id, dbmod.mod_mod_id_root))) {
                    if (isClone) {
                        if ((dbmod.mod_type).equalsIgnoreCase(dbModule.MOD_TYPE_SVY)) {
                            Vector vtModId;
                            if (dbmod.mod_mod_id_root != 0) {
                                vtModId = dbModule.dumpModNQ(con, dbmod.mod_mod_id_root, dbmod.res_title, uploadDir, prof, null, true);
                            } else {
                                vtModId = dbModule.dumpModNQ(con, parent_mod_res_id, dbmod.res_title, uploadDir, prof, null, false);
                            }
                            new_mod_id = ((Long) vtModId.get(0)).longValue();
                            dbResourceContent resCon = new dbResourceContent();

                            resCon.rcn_res_id = tagCos.cos_res_id;
                            resCon.rcn_res_id_content = ((Long) vtModId.get(0)).longValue();
                            resCon.rcn_obj_id_content = 0;

                            if (!resCon.ins(con)) {
                                con.rollback();
                                throw new cwException("Failed to add a Course.");
                            }
                        } else {
                            new_mod_id = tagCos.insModule(con, dbmod, null, prof);
                            String asm_type;
                            if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)) {
                                Vector vtModSpec = dbModuleSpec.getByResId(con, parent_mod_res_id);
                                for (int j = 0; j < vtModSpec.size(); j++) {
                                    dbModuleSpec dbModSpec = (dbModuleSpec) vtModSpec.get(j);
                                    dbModSpec.msp_res_id = new_mod_id;
                                    dbModSpec.save(con, true);
                                }
                            } else {
                                asm_type = dbResource.RES_SUBTYPE_FAS;
                                dbmod.mod_res_id = new_mod_id;
                                dbmod.selectAssessment(con, prof, parent_mod_res_id, asm_type, uploadDir, null);
                                //插入mod_id与资源目录的记录
                                dbResourceObjective resObjtv = new dbResourceObjective();
                                Vector vt = dbResourceObjective.getObjId(con, parent_mod_res_id);
                                long[] int_mod_id_list = new long[] { new_mod_id };
                                long[] lstObjID = new long[vt.size()];
                                for (int n = 0; n < vt.size(); n++) {
                                    lstObjID[n] = ((dbResourceObjective) vt.get(n)).rob_obj_id;
                                }
                                resObjtv.insResObj(con, int_mod_id_list, lstObjID);
                            }
                        }
                    } else {
                        new_mod_id = tagCos.insModule(con, dbmod, null, prof);
                        // copy ResourceContent records
                        Vector vtrcn = dbResourceContent.getChildAss(con, dbmod.mod_mod_res_id_parent);
                        for (int m = 0; m < vtrcn.size(); m++) {
                            dbResourceContent rcn = (dbResourceContent) vtrcn.get(m);
                            rcn.rcn_rcn_res_id_parent = rcn.rcn_res_id;
                            rcn.rcn_rcn_sub_nbr_parent = rcn.rcn_sub_nbr;
                            rcn.rcn_res_id = new_mod_id;
                            rcn.ins(con);
                        }
                        // copy ResourceObjective records
                        dbResourceObjective resObjtv = new dbResourceObjective();
                        Vector vt = dbResourceObjective.getObjId(con, dbmod.mod_mod_res_id_parent);
                        long[] int_mod_id_list = new long[] { new_mod_id };
                        long[] lstObjID = new long[vt.size()];
                        for (int n = 0; n < vt.size(); n++) {
                            lstObjID[n] = ((dbResourceObjective) vt.get(n)).rob_obj_id;
                        }
                        resObjtv.insResObj(con, int_mod_id_list, lstObjID);
                    }
                    //dbmod.updMaxScore(con);
                }
            } else if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_FOR)) {
                dbForum dbforum = new dbForum(dbmod);
                dbforum.dbFaqTopic = dbForumTopic.getAllTopicByResID(con, parent_mod_res_id);
                new_mod_id = tagCos.insModule(con, dbmod, null, prof);
                if (data_trans) {
                    CourseContentUtils.tansForm(con, parent_mod_res_id, new_mod_id);
                } else {
                    if (dbforum.dbFaqTopic != null && dbforum.dbFaqTopic.size() > 0) {
                        for (int k = 0; k < dbforum.dbFaqTopic.size(); k++) {
                            dbForumTopic dbFor = (dbForumTopic) dbforum.dbFaqTopic.get(k);
                            dbFor.fto_res_id = new_mod_id;
                            dbFor.fto_usr_id = prof.usr_id;
                            dbFor.fto_create_datetime = curTime;
                            dbFor.fto_last_post_datetime = null;
                            dbFor.ins(con, prof, null);
                        }
                    }
                }
            } else if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_FAQ)) {
                dbFaq dbfag = new dbFaq(dbmod);
                dbfag.dbFqTopic = dbForumTopic.getAllTopicByResID(con, parent_mod_res_id);
                new_mod_id = tagCos.insModule(con, dbmod, null, prof);
                if (data_trans) {
                    CourseContentUtils.tansForm(con, parent_mod_res_id, new_mod_id);
                } else {
                    if (dbfag.dbFqTopic != null && dbfag.dbFqTopic.size() > 0) {
                        for (int k = 0; k < dbfag.dbFqTopic.size(); k++) {
                            dbForumTopic dbFor = (dbForumTopic) dbfag.dbFqTopic.get(k);
                            dbFor.fto_res_id = new_mod_id;
                            dbFor.fto_usr_id = prof.usr_id;
                            dbFor.fto_create_datetime = curTime;
                            dbFor.fto_last_post_datetime = null;
                            dbFor.ins(con, prof, null);
                        }
                    }
                }
            } else if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_REF) && isClone) {
                new_mod_id = tagCos.insModule(con, dbmod, null, prof);
                dbmod.mod_res_id = new_mod_id;
                Vector vtRef = DbCtReference.getModuleReferenceList(con, (int) parent_mod_res_id);
                DbCtReference dbRef = new DbCtReference();
                for (int x = 0; x < vtRef.size(); x++) {
                    dbRef = (DbCtReference) vtRef.get(x);
                    dbRef.ref_res_id = (int) new_mod_id;
                    Reference myReference = new Reference(dbRef, dbmod);
                    myReference.insReference(con, prof);
                }
            } else if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_GLO) && isClone) {
                new_mod_id = tagCos.insModule(con, dbmod, null, prof);
                dbmod.mod_res_id = new_mod_id;
                Vector vtGlo = DbCtGlossary.getGloList(con, (int) parent_mod_res_id);
                Glossary glo = new Glossary();
                DbCtGlossary dbglo = new DbCtGlossary();
                for (int y = 0; y < vtGlo.size(); y++) {
                    dbglo = (DbCtGlossary) vtGlo.get(y);
                    dbglo.glo_res_id = new_mod_id;
                    glo.ins(con, dbglo, prof);
                }
            } else {
                new_mod_id = tagCos.insModule(con, dbmod, null, prof);
            }
            vtNewModId.addElement(new Long(new_mod_id));
            htModId.put(new Long(parent_mod_res_id), new Long(new_mod_id));
            if (htModObjId != null && htModObjId.size() > 0) {
                htObjId.put(new Long(parent_mod_res_id), htModObjId);
            }
            if (new_mod_id != 0) {
                dbmod.mod_res_id = new_mod_id;
                dbmod.res_id = new_mod_id;
                if (data_trans) { // reset the parent mod status
                    String[] res_id_lst = { String.valueOf(new_mod_id)};
                    dbResource.updResStatus(con, res_id_lst, parent_mod_status, prof);
                    dbModule child_mod = new dbModule();
                    child_mod.mod_res_id = new_mod_id;
                    child_mod.mod_type = dbmod.mod_type;
                    child_mod.updMaxScore(con);
                }
                if (uploadDir != null && uploadDir.length() > 1) {
                    if (!uploadDir.endsWith(  cwUtils.SLASH  )) {
                        uploadDir = uploadDir   + cwUtils.SLASH  ;
                    }
                    CourseContentUtils.disFolder(parent_mod_res_id, new_mod_id, uploadDir);
                }
            } else {
                dbmod.mod_res_id = 0;
                dbmod.res_id = 0;
            }
        }
        return new Hashtable[] { htModId, htObjId };
    }
    
    public static StringBuffer cloneCourseContent(Connection con, long src_itm_id, long tag_cos_id, String[] mod_id_list, loginProfile prof, String uploadDir) throws qdbException, qdbErrMessage, cwException, cwSysMessage, IOException, SQLException, CloneNotSupportedException {
        StringBuffer title = new StringBuffer();
        String newXml;

        long clo_src_cos_res_id = dbCourse.getCosResId(con, src_itm_id);
        String cos_xml = dbCourse.getCosStructureAsXML(con, clo_src_cos_res_id);

        dbCourse targetdbCos = new dbCourse(tag_cos_id);
        targetdbCos.get(con);
        Vector vtChildCosResId = targetdbCos.getChildCosResId(con);

        Vector[] allModId = splitCosStrtItmIDAndRef(mod_id_list);
        Vector vtCloModId = allModId[0];
        Vector vtCloFdrId = allModId[1];
        Hashtable[] hashtable = new Hashtable[2];
        Hashtable hashModId = new Hashtable();
        Hashtable hashObjId = new Hashtable();
        //复制选中的模块
        if (vtCloModId.size() > 0) {
            Vector vtCloModInfor = getModObj(con, vtCloModId);
            hashtable = copyMod(con, vtCloModInfor, targetdbCos, false, true, prof, uploadDir, null, null, null);
            hashModId = hashtable[0];
            hashObjId = hashtable[1];
            for (int k = 0; k < vtCloModInfor.size(); k++) {
                long id = ((dbModule) vtCloModInfor.get(k)).mod_res_id;
                long newId = ((Long) hashModId.get(new Long(id))).longValue();
                if (newId == 0) {
                    if (title.length() > 0) {
                        title.append(",").append(((dbModule) vtCloModInfor.get(k)).res_title);
                    } else {
                        title.append(((dbModule) vtCloModInfor.get(k)).res_title);
                    }
                }
            }
            newXml = generateCosStrtItmXML(targetdbCos.cos_structure_xml, vtCloModInfor, hashModId) + "</tableofcontents>";
            if (targetdbCos.cos_structure_xml == null) {
                targetdbCos.cos_structure_xml = "<tableofcontents identifier=\"TOC1\" title=\"" + cwUtils.esc4XML(targetdbCos.res_title) + "\">" + newXml;
            } else {
                targetdbCos.cos_structure_xml = dbUtils.subsitute(targetdbCos.cos_structure_xml, "</tableofcontents>", newXml);
            }
            targetdbCos.cos_structure_json=qdbAction.static_env.transformXML(targetdbCos.cos_structure_xml.replaceAll("&quot;", " "), "cos_structure_json_js.xsl",null);
            targetdbCos.updCosStructure(con);
            if(targetdbCos.cos_max_normal == 0){
            	targetdbCos.updAiccCos(con,prof,null,targetdbCos.cos_aicc_version,targetdbCos.cos_vendor, -1, false,false,false);
            }
            Vector vtParentId = new Vector();
            for (int j = 0; j < vtChildCosResId.size(); j++) {
                dbCourse childdbCos = (dbCourse) vtChildCosResId.get(j);
                childdbCos.get(con);
                hashtable = copyMod(con, vtCloModInfor, childdbCos, false, false, prof, uploadDir, null, hashModId, hashObjId);
                Hashtable hashChildModId = hashtable[0];
                newXml = generateCosStrtItmXML(childdbCos.cos_structure_xml, vtCloModInfor, hashChildModId) + "</tableofcontents>";
                if (childdbCos.cos_structure_xml != null) {
                    childdbCos.cos_structure_xml = dbUtils.subsitute(childdbCos.cos_structure_xml, "</tableofcontents>", newXml);
                } else {
                    childdbCos.cos_structure_xml = "<tableofcontents identifier=\"TOC1\" title=\"" + cwUtils.esc4XML(childdbCos.res_title) + "\">" + newXml;
                }
                targetdbCos.cos_structure_json=qdbAction.static_env.transformXML(targetdbCos.cos_structure_xml.replaceAll("&quot;", " "), "cos_structure_json_js.xsl",null);
                childdbCos.updCosStructure(con);
                if(childdbCos.cos_max_normal == 0){
                	childdbCos.updAiccCos(con,prof,null,childdbCos.cos_aicc_version,childdbCos.cos_vendor, -1, false,false,false);
                }
            }
        }
        //复制选中的模块夹
        if (vtCloFdrId.size() > 0) {
            String cos_structure_xmlFDR;
            Vector vtCloIdent = new Vector();
            Vector vtParentId = new Vector();
            for (int i = 0; i < vtCloFdrId.size(); i++) {
                String folderValue = (String) vtCloFdrId.get(i);
                cos_structure_xmlFDR = cwXSL.processFromStringByParam(cos_xml, cos_structureXSL, "element_to_copy", folderValue);
                Vector[] vtAll = splitCosStrtItmIDAndRef(getCosStrtItmIDAndRef(cos_structure_xmlFDR));
                vtCloModId = vtAll[0];
                vtCloIdent = vtAll[1];
                Vector vtCloModInfor = getModObj(con, vtCloModId);
                hashtable = copyMod(con, vtCloModInfor, targetdbCos, false, true, prof, uploadDir, null, null, null);
                hashModId = hashtable[0];
                hashObjId = hashtable[1];
                for (int k = 0; k < vtCloModInfor.size(); k++) {
                    long id = ((dbModule) vtCloModInfor.get(k)).mod_res_id;
                    long newId = ((Long) hashModId.get(new Long(id))).longValue();
                    if (newId == 0) {
                        if (title.length() > 0) {
                            title.append(",").append(((dbModule) vtCloModInfor.get(k)).res_title);
                        } else {
                            title.append(((dbModule) vtCloModInfor.get(k)).res_title);
                        }
                    }
                }
                
                newXml = replaceCosStrtItmIDAndRef(targetdbCos.cos_structure_xml, cos_structure_xmlFDR, vtCloModId, vtCloIdent, hashModId) + "</tableofcontents>";
                if (targetdbCos.cos_structure_xml == null) {
                    targetdbCos.cos_structure_xml = "<tableofcontents identifier=\"TOC1\" title=\"" + cwUtils.esc4XML(targetdbCos.res_title) + "\">" + newXml;
                } else {
                    targetdbCos.cos_structure_xml = dbUtils.subsitute(targetdbCos.cos_structure_xml, "</tableofcontents>", newXml);
                }
                targetdbCos.cos_structure_json=qdbAction.static_env.transformXML(targetdbCos.cos_structure_xml.replaceAll("&quot;", " "), "cos_structure_json_js.xsl",null);
                targetdbCos.updCosStructure(con);
                if(targetdbCos.cos_max_normal == 0){
                	targetdbCos.updAiccCos(con,prof,null,targetdbCos.cos_aicc_version,targetdbCos.cos_vendor, -1, false,false,false);
                }
                for (int j = 0; j < vtChildCosResId.size(); j++) {
                    dbCourse childdbCos = (dbCourse) vtChildCosResId.get(j);
                    childdbCos.get(con);
                    hashtable = copyMod(con, vtCloModInfor, childdbCos, false, false, prof, uploadDir, null, hashModId, hashObjId);
                    Hashtable hashChildModId = hashtable[0];
                    newXml = replaceCosStrtItmIDAndRef(targetdbCos.cos_structure_xml, cos_structure_xmlFDR, vtCloModId, vtCloIdent, hashChildModId) + "</tableofcontents>";
                    if (childdbCos.cos_structure_xml != null) {
                        childdbCos.cos_structure_xml = dbUtils.subsitute(childdbCos.cos_structure_xml, "</tableofcontents>", newXml);
                    } else {
                        childdbCos.cos_structure_xml = "<tableofcontents identifier=\"TOC1\" title=\"" + cwUtils.esc4XML(childdbCos.res_title) + "\">" + newXml;
                    }
                    targetdbCos.cos_structure_json=qdbAction.static_env.transformXML(targetdbCos.cos_structure_xml.replaceAll("&quot;", " "), "cos_structure_json_js.xsl", null);
                    childdbCos.updCosStructure(con);
                    if(childdbCos.cos_max_normal == 0){
                    	childdbCos.updAiccCos(con,prof,null,childdbCos.cos_aicc_version,childdbCos.cos_vendor, -1, false,false,false);
                    }
                }
            }
        }

        return title;
    }
    
    //得到要拷贝模块的信息
    public static Vector getModObj(Connection con, Vector vtModId) throws qdbException, cwSysMessage {
        Vector vtModObj = new Vector();
        for (int i = 0; i < vtModId.size(); i++) {
            dbModule dbmod = new dbModule();
            dbmod.mod_res_id = ((Long) vtModId.get(i)).longValue();
            dbmod.get(con);
            dbmod.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
            dbmod.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
            if(dbmod.mod_type.equals(dbModule.MOD_TYPE_FOR)){
                dbForum dbforum = new dbForum(dbmod);
                dbforum.dbFaqTopic = dbForumTopic.getAllTopicByResID(con, ((Long) vtModId.get(i)).longValue());
                dbforum.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
                dbforum.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
                vtModObj.addElement(dbforum);
            }else if(dbmod.mod_type.equals(dbModule.MOD_TYPE_FAQ)){
                dbFaq dbfag = new dbFaq(dbmod);
                dbfag.dbFqTopic = dbForumTopic.getAllTopicByResID(con, ((Long) vtModId.get(i)).longValue());
                dbfag.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
                dbfag.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
                vtModObj.addElement(dbfag);
            }else{
                vtModObj.addElement(dbmod);
            }
        }
        return vtModObj;
    }
    
    //新增模块的XML
    private static String generateCosStrtItmXML(String structure_xml, Vector vtNewMod, Hashtable hashModId) {
        StringBuffer cont_xml = new StringBuffer();
        long identifier = getCosStrtMaxID(structure_xml) + 1;
        for (int i = 0; i < vtNewMod.size(); i++) {
            long id = ((dbModule) vtNewMod.get(i)).mod_res_id;
            long newId = ((Long)hashModId.get(new Long (id))).longValue(); 
            if (newId != 0) {
                cont_xml.append("<item identifier=\"ITEM").append(identifier++);
                cont_xml.append("\" identifierref=\"").append(newId);
                cont_xml.append("\" title=\"").append(cwUtils.esc4XML(((dbModule) vtNewMod.get(i)).res_title));
                cont_xml.append("\" >");
                cont_xml.append("<itemtype>").append(dbResource.RES_TYPE_MOD).append("</itemtype>");
                cont_xml.append("<restype>").append(((dbModule) vtNewMod.get(i)).mod_type);
                cont_xml.append("</restype>");
                cont_xml.append("</item>");
            }
        }
        return cont_xml.toString();
    }
        
    //得到最大的identifier的值
    private static long getCosStrtMaxID(String structure_xml) {
        int maxId = 0;
        int curId = 0;
        int left = 0;
        int right = 0;
        String s;
        int id;
        if (structure_xml != null) {
            while (curId < structure_xml.length() - 1) {
                left = structure_xml.indexOf(" identifier=\"ITEM", curId);
                if (left < 0) {
                    break;
                }
                right = structure_xml.indexOf("\"", left + 17);
                s = structure_xml.substring(left + 17, right);
                id = Integer.parseInt(s);
                if (id > maxId) {
                    maxId = id;
                }
                curId = right + 1;
            }
        }
        return maxId;
    }

    //解析xml得到mod_res_id 和 identifier       
    private static String[] getCosStrtItmIDAndRef(String structure_xml) throws cwException {
        String str1 = cwXSL.processFromString(structure_xml, cos_structureXSL);
        StringTokenizer st = new StringTokenizer(str1, ",");
        String[] result = new String[st.countTokens()];
        for (int i = 0; i < result.length; i++) {
            result[i] = st.nextToken();
        }
        return result;
    }
    
    //分开mod_res_id  identifier
    private static Vector[] splitCosStrtItmIDAndRef(String[] id) {
        Vector[] vtAll = new Vector[]{new Vector(), new Vector()};
        for (int i = 0; i < id.length; i++) {
            if (!id[i].equals("")) {
                if (id[i].startsWith("ITEM")) {
                    vtAll[1].addElement(id[i]);
                } else {
                    vtAll[0].addElement(new Long(id[i]));
                }
            }
        }
        return vtAll;
    }
    
    //去掉没有复制的item
    private static String removeCosStrtItm(String str, long id) {
        int idStation = str.indexOf("identifierref=\"" + id);
        int itemStarStation = str.lastIndexOf("<item ", idStation);
        int itemEndStation = str.indexOf("</item>", idStation) + 7;
        String frontStr = str.substring(0, itemStarStation);
        String offsideStr = str.substring(itemEndStation);
        return frontStr + offsideStr;
    }
    
    //替换模块夹的mod_id和Identifier
    private static String replaceCosStrtItmIDAndRef(String structure_xml, String frdStructure_xml, Vector vtSrcModID, Vector Identifier, Hashtable hashModId) {
        long identifier = getCosStrtMaxID(structure_xml) + 1;
        for (int i = 0; i < vtSrcModID.size(); i++) {
            long id = ((Long) vtSrcModID.get(i)).longValue();
            long newId = ((Long) hashModId.get(new Long(id))).longValue();
            if (newId == 0) {
                frdStructure_xml = removeCosStrtItm(frdStructure_xml, ((Long) vtSrcModID.get(i)).longValue());
            } else {
                String ModIdOld = " identifierref=\"" + ((Long) vtSrcModID.get(i)).longValue() + "\"";
                String ModIdNew = " identifierref=\"" + newId + "\"";
                frdStructure_xml = dbUtils.subsitute(frdStructure_xml, ModIdOld, ModIdNew);
            }
        }
        frdStructure_xml = dbUtils.subsitute(frdStructure_xml, "\"ITEM", "\"AITEM");
        for (int j = 0; j < Identifier.size(); j++) {
            String str = "A" + (String) Identifier.get(j);
            if (frdStructure_xml.lastIndexOf("\"" + str + "\"") != -1) {
                String oldIdentifier = " identifier=\"A" + Identifier.get(j) + "\"";
                String newIdentifier = " identifier=\"ITEM" + identifier++ +"\"";
                frdStructure_xml = dbUtils.subsitute(frdStructure_xml, oldIdentifier, newIdentifier);
            }
        }
        return frdStructure_xml;
    }
}
