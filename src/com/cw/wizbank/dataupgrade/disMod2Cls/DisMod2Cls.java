/*
 * Created on 2005-10-10
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.dataupgrade.disMod2Cls;

import java.sql.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.batch.batchUtil.*;
import com.cw.wizbank.dataupgrade.disMod2Cls.util.DisMod2ClsUtil;
import com.cw.wizbank.qdb.loginProfile;

import java.util.List;

import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;

import com.cw.wizbank.accesscontrol.AcCourse;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.course.CourseContentUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author randy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DisMod2Cls {

    // public cwIniFile inifile;
    public Connection con;

    public String log_file = "..\\log\\log.txt";

    public String resourse_folder = null;

    public loginProfile prof = new loginProfile();

    public boolean autoCommit = false;

    public static String lrnRolExtId = "NLRN_1";

    private void init(String ini) throws Exception {
        // 读入配置文件的设置参数
        cwIniFile inifile = new cwIniFile(ini);

        if (inifile.getValue("LOG_FILE") != null
                && !inifile.getValue("LOG_FILE").equals("")) {
            log_file = inifile.getValue("LOG_FILE");
        }
        resourse_folder = inifile.getValue("RESOURSE_FOLDER");

        if (inifile.getValue("TADM_USR_ID") != null
                && !inifile.getValue("TADM_USR_ID").equals("")) {
            prof.usr_id = inifile.getValue("TADM_USR_ID");
        } else {
            prof.usr_id = "s1u3";
        }
        if (inifile.getValue("TADM_USR_ENT_ID") != null
                && !inifile.getValue("TADM_USR_ENT_ID").equals("")) {
            prof.usr_ent_id = Long.valueOf(inifile.getValue("TADM_USR_ENT_ID"))
                    .longValue();
        } else {
            prof.usr_ent_id = 3;
        }
        if (inifile.getValue("ROOT_ENT_ID") != null
                && !inifile.getValue("ROOT_ENT_ID").equals("")) {
            prof.root_ent_id = Long.valueOf(inifile.getValue("ROOT_ENT_ID"))
                    .longValue();
        } else {
            prof.root_ent_id = 1;
        }
        if (inifile.getValue("LABEL_LAN") != null
                && !inifile.getValue("LABEL_LAN").equals("")) {
            prof.label_lan = inifile.getValue("LABEL_LAN");
        } else {
            prof.label_lan = "ISO-8859-1";
        }
        prof.current_role = "TADM_1";
        /*
         * prof.usr_id = "s1u3"; prof.root_ent_id = 1; prof.usr_ent_id = 3;
         * prof.current_role = "TADM_1"; prof.label_lan = "ISO-8859-1";
         */
        /*if (inifile.getValue("AUTO_COMMIT") != null
                && !inifile.getValue("AUTO_COMMIT").equals("")) {
            autoCommit = new Boolean(inifile.getValue("AUTO_COMMIT")).booleanValue();
        } else {
            autoCommit = false;
        }
        System.out.println("Auto Commit:"+ autoCommit);*/
    }


    public static void main(String[] args) {
        DisMod2Cls dis_mod = new DisMod2Cls();
        try {
        	int arg_index = 0;
            String ini_file = args[arg_index++];
            dis_mod.init(ini_file);
            dis_mod.con = BatchUtils.openDB(args[arg_index++]);
            dis_mod.log_file = args[arg_index++];
            dis_mod.resourse_folder = args[arg_index++];

            Timestamp start_time = cwSQL.getTime(dis_mod.con);
            boolean bSuccess = dis_mod.upd(dis_mod.con, dis_mod.prof, dis_mod.resourse_folder);

            Timestamp end_time = cwSQL.getTime(dis_mod.con);
            DisMod2ClsUtil.writeLog(dis_mod.log_file, "Finished!! Started At: " + start_time + "\t Ended At: " + end_time + "\n"
                        + "Total time used: " + ((end_time.getTime() - start_time.getTime()) / 1000 / 60) + " minutes");

        } catch (cwException e) {
            CommonLog.error(e.getMessage(),e);
            DisMod2ClsUtil.writeLog(dis_mod.log_file, "", e);
        } catch (SQLException e) {
            CommonLog.error(e.getMessage(),e);
            DisMod2ClsUtil.writeLog(dis_mod.log_file, "", e);
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            DisMod2ClsUtil.writeLog(dis_mod.log_file, "", e);
        }
    }

    public boolean upd(Connection con, loginProfile prof, String resourse_folder)
            throws SQLException, cwSysMessage, qdbException, cwException,
            qdbErrMessage, IOException {
        List itm_id_list = getAllOffLineCousrItem(con);
        int totalRecord = itm_id_list.size();
        int totalApp = getOffLineCousrItemApplicationCnt(con);
        int totalClass = getOffLineCousrItemClassCnt(con);
        DisMod2ClsUtil.writeLog(log_file, "Total Course:" + totalRecord + ", Total Class: " + totalClass + ", Total Application: " + totalApp);

        int i=1;

        Iterator it = itm_id_list.iterator();
        long t2;
        long t0 = System.currentTimeMillis();
        long t1 = t0;
        int total_app_cnt = 0;
        int app_cnt_on_course = 0;
        int total_class = 0;

        while (it.hasNext()) {
          app_cnt_on_course = 0;
          long parent_itm_id = ((Long) it.next()).longValue();
          try{
            long parent_cos_id = dbCourse.getCosResId(con, parent_itm_id);
            Vector vtParentMod = dbResourceContent.getCourseModule(con, parent_cos_id);

            // 把离线课程的内容设置为course level
            setItemContent(con, parent_itm_id);
            List ch_itm_id_list = getChidlItem(con, parent_itm_id);
            Iterator ch_it = ch_itm_id_list.iterator();
            while (ch_it.hasNext()) {
                long ch_itm_id = ((Long) ch_it.next()).longValue();
                aeItem ch_itm = new aeItem();
                ch_itm.itm_id = ch_itm_id;
                ch_itm.get(con);
                if (ch_itm != null) {
                    long child_cos_id = insCourse(con, prof, ch_itm);
                    // 把所有的离线课堂的内容设置为course level
                    setItemContent(con, ch_itm_id);
                    // dis//向班级分发在线模块

                    Hashtable[] htContentIds = null;

                    // do nothing when the parent is empty too
                    if (vtParentMod.size() == 0){
                        htContentIds = new Hashtable[] {new Hashtable(), new Hashtable()};
                    }else{
                        htContentIds = CourseContentUtils.propagateCourseContentForBatch(con,
                            parent_itm_id, ch_itm_id, parent_cos_id, child_cos_id, prof, true,
                            resourse_folder, vtParentMod);
                    }

                    // 向班级分发结训条件(不需要)
                    // ch_itm.genCmtFromParent( con, parent_itm_id, ch_itm_id,
                    // prof);
                    // 更新学员跟踪记录
                    int appSize = updLrnRecord(con, ch_itm_id, child_cos_id, parent_cos_id, htContentIds);
                    app_cnt_on_course += appSize;
                    total_app_cnt += appSize;
                }
            }
            total_class += ch_itm_id_list.size();
            t2 = System.currentTimeMillis();
            DisMod2ClsUtil.writeLog(log_file, "Finish Updating Item ID: " +  parent_itm_id + " - " + ch_itm_id_list.size() + " classes, " + app_cnt_on_course + " appn. " + ((long) (t2 - t1) / 1000 ) + " sec" + " \t\t " + vtParentMod.size() + " \t\t " + i + "/" + totalRecord + " courses \t" + total_class + "/" + totalClass+ " class \t " + total_app_cnt + "/" + totalApp + " appn \t Total Time used: " + ((long) (t2 - t0) / 1000 / 60) + " mins");
            con.commit();
            t1 = t2;
            i++;
          }catch (Exception e){
              con.rollback();
              CommonLog.error(e.getMessage(),e);
              i++;
              DisMod2ClsUtil.writeLog(log_file, "Error in itm: " + parent_itm_id, e);
          }
        }
        File resource_folder = (new File(resourse_folder));
        // 更新存放作业的附件的文件夹
        updateResourceFolder(con, resource_folder);
        // 删除空的文件夹
        // clearEmptyFolder(resource_folder);
        return true;
    }

    public long insCourse(Connection con, loginProfile prof, aeItem ch_itm) throws qdbException, SQLException {
        dbResource res = new dbResource();
        res.res_lan = prof.label_lan;
        res.res_title = ch_itm.itm_title;
        res.res_usr_id_owner = prof.usr_id;
        res.res_upd_user = prof.usr_id;
        res.res_create_date = ch_itm.itm_create_timestamp;
        res.res_upd_date = ch_itm.itm_upd_timestamp;
        res.res_status = ch_itm.itm_status;
        res.res_type = dbCourse.RES_TYPE_COS;
        res.ins(con);

        PreparedStatement stmt = con.prepareStatement(
            "INSERT INTO Course "
            + " ( cos_lic_key, cos_res_id, cos_itm_id ) "
            + " VALUES (?,?,?)" );

        stmt.setString(1, null);
        stmt.setLong(2, res.res_id);
        stmt.setLong(3, ch_itm.itm_id);

        int stmtResult=stmt.executeUpdate();
        stmt.close();
        if ( stmtResult!=1)
        {
            con.rollback();
            throw new qdbException("Failed to add a Course.");
        }
        else
        {
           // insert into Access Control Table
           AcCourse accos = new AcCourse(con);
           boolean read = true;
           boolean write = true;
           boolean exec = false;
           createResourcePermission(con,res.res_id,prof.usr_ent_id, prof.current_role, read,write,exec);
            return res.res_id;
           // commit() at qdbAction.java
        }
    }

    public static void createResourcePermission(Connection con, long resId, long entId, String rol_ext_id, boolean _read, boolean _write, boolean _execute)
            throws qdbException
        {
            try{
                String SQL = "";
                boolean recExist = false;

                SQL =  "      INSERT INTO ResourcePermission "
                        + "         (rpm_read "
                        + "         ,rpm_write "
                        + "         ,rpm_execute "
                        + "         ,rpm_res_id "
                        + "         ,rpm_ent_id "
                        + "         ,rpm_rol_ext_id) "
                        + "      VALUES (?,?,?,?,?,?) ";


                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setBoolean(1, _read);
                stmt.setBoolean(2, _write);
                stmt.setBoolean(3, _execute);
                stmt.setLong(4, resId);
                stmt.setLong(5, entId);
                stmt.setString(6, rol_ext_id);
                int stmtResult=stmt.executeUpdate();
                stmt.close();
                if ( stmtResult!=1)
                {
                    con.rollback();
                    throw new qdbException("Failed to update resourcepermission.");
                }

            } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
            }
        }

    private int updLrnRecord(Connection con, long itm_id, long cos_id,
            long parent_cos_id, Hashtable htContentIds[]) throws SQLException, cwException, qdbException {
//        List tkh_id_list = getItemTKHID(con, itm_id);
//        Iterator tkh_it = tkh_id_list.iterator();

        Vector vtTkhId = getItemTkhIdVec(con, itm_id);
        String sql_in_clause = null;
        String tmpTable = null;
        if (vtTkhId.size() == 0){
            return vtTkhId.size();
//        }else if (vtTkhId.size() < 1000){
//            sql_in_clause = cwUtils.vector2list(vtTkhId);
        }else{
            String colName = "tkh_id";
            tmpTable = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tmpTable, vtTkhId, cwSQL.COL_TYPE_LONG);
            sql_in_clause = "(select tkh_id from " + tmpTable + ")";
        }

            batchUpdEnrolmentExe(con, itm_id, cos_id, parent_cos_id);
            batchUpdTrackingHistoryExe(con, itm_id, cos_id, parent_cos_id, sql_in_clause);
            batchUpdCourseEvaluationExe(con, itm_id, cos_id, parent_cos_id, sql_in_clause);
            if (!htContentIds[0].isEmpty()){
                batchUpdModuleEvaluationExe(con, itm_id, cos_id, htContentIds[0], sql_in_clause);
                if (!htContentIds[1].isEmpty()){
                    batchUpdAccomplishmentExe(con, itm_id, cos_id, htContentIds[1], sql_in_clause);
                }
                batchExecUpdateProgress(con, itm_id, cos_id, htContentIds[0], sql_in_clause);
            }

            if (tmpTable != null){
                cwSQL.dropTempTable(con, tmpTable);
            }

        return vtTkhId.size();
    }

    private List getAllOffLineCousrItem(Connection con) throws SQLException {

        String sql = "select itm_id from aeItem where itm_create_run_ind = ? and itm_content_def is null order by itm_id";
        // content only
//        String sql = "select itm_id from aeItem , Course where itm_create_run_ind =? and itm_content_def is null and cos_itm_id = itm_id and cos_res_id in (select rcn_res_id  from resourcecontent where rcn_res_id = cos_res_id )";

        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            List itm_list = new ArrayList();
            pstmt.setByte(1, Byte.parseByte("1"));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                itm_list.add(new Long(rs.getLong("itm_id")));
            }
            return itm_list;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private int getOffLineCousrItemClassCnt(Connection con) throws SQLException {
            String sql = "Select count(*) cnt from aeitemrelation , aeitem "
                + " where itm_create_run_ind = ? and itm_content_def is null "
                + " and ire_parent_itm_id = itm_id ";

            PreparedStatement pstmt = null;
            try {
                pstmt = con.prepareStatement(sql);
                pstmt.setBoolean(1, true);
                ResultSet rs = pstmt.executeQuery();
                int cnt = 0;
                if (rs.next()) {
                    cnt = rs.getInt("cnt");
                }
                return cnt;
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
            }
        }

    private int getOffLineCousrItemApplicationCnt(Connection con) throws SQLException {
        String sql = "Select count(*) cnt from aeApplication , aeitemrelation , aeitem " +            "where itm_create_run_ind = ? and itm_content_def is null " +            "and app_status = ? and app_itm_id = ire_child_itm_id and ire_parent_itm_id = itm_id";

        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setBoolean(1, true);
            pstmt.setString(2, aeApplication.ADMITTED);
            ResultSet rs = pstmt.executeQuery();
            int cnt = 0;
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
            return cnt;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private List getChidlItem(Connection con, long parent_itm_id)
            throws SQLException {
        String sql = "select ire_child_itm_id from aeItemRelation where ire_parent_itm_id =? ";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            List ch_itm_list = new ArrayList();
            pstmt.setLong(1, parent_itm_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ch_itm_list.add(new Long(rs.getLong("ire_child_itm_id")));
            }
            return ch_itm_list;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private List getItemTKHID(Connection con, long itm_id) throws SQLException {
        String sql = "select distinct app_tkh_id from aeApplication where app_tkh_id is not null and app_itm_id =? ";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            List tkh_id_list = new ArrayList();
            pstmt.setLong(1, itm_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tkh_id_list.add(new Long(rs.getLong("app_tkh_id")));
            }
            return tkh_id_list;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private int insertTkdIdIntoSimpleTempTable(Connection con, String tableName, long itm_id)
           throws SQLException
       {
           StringBuffer SQL = new StringBuffer();

           SQL.append("INSERT INTO ").append(tableName)
            .append(" select app_tkh_id from aeApplication where app_tkh_id is not null and app_itm_id = ? ");

           PreparedStatement stmt = con.prepareStatement(SQL.toString());

            stmt.setLong(1, itm_id);
            int cnt = stmt.executeUpdate();
           stmt.close();

           return cnt;
       }


    private Vector getItemTkhIdVec(Connection con, long itm_id) throws SQLException {
           String sql = "select app_tkh_id from aeApplication where app_tkh_id is not null and app_itm_id =? ";
           PreparedStatement pstmt = null;
           try {
               pstmt = con.prepareStatement(sql);
               Vector vtTkhId = new Vector();
               pstmt.setLong(1, itm_id);
               ResultSet rs = pstmt.executeQuery();
               while (rs.next()) {
                   vtTkhId.add(new Long(rs.getLong("app_tkh_id")));
               }
               return vtTkhId;
           } finally {
               if (pstmt != null) {
                   pstmt.close();
               }
           }
       }
    private List getModIDList(Connection con, long tkh_id) throws SQLException {
        String sql = "select distinct mov_mod_id from ModuleEvaluation where mov_tkh_id =? ";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            List mod_id_list = new ArrayList();
            pstmt.setLong(1, tkh_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                mod_id_list.add(new Long(rs.getLong("mov_mod_id")));
            }
            return mod_id_list;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private long map2ModChild(Connection con, long parent_mod_id, long cos_id)
            throws SQLException, cwException {
        String sql = "select mod_res_id from ResourceContent, Module where rcn_res_id = ? and rcn_res_id_content = mod_res_id and mod_mod_res_id_parent = ?";
        PreparedStatement stmt = null;
        long ch_mod_id = 0;
        try {
            stmt = con.prepareStatement(sql);
            ResultSet rs = null;
            stmt.setLong(1, cos_id);
            stmt.setLong(2, parent_mod_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ch_mod_id = rs.getLong("mod_res_id");
            } else {
                throw new cwException("can not find the child of module:"
                        + parent_mod_id);
            }

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return ch_mod_id;
    }


    private void batchUpdEnrolmentExe(Connection con, long itm_id, long cos_id,
               long parent_cos_id) throws SQLException, qdbException {
            batchUnenroll(con, parent_cos_id);
            batchEnroll(con, cos_id, itm_id, prof.usr_id);
       }

    private void batchUpdateAppnEnrolRelationExe(Connection con, long app_itm_id,
                long cos_id, long parent_cos_id) throws SQLException {
            PreparedStatement stmt = null;
            try {
                String sql = "update aeAppnEnrolRelation set aer_res_id = ? where aer_res_id = ? and aer_app_id = (select app_id from aeApplication where app_itm_id = ? and app_id = aer_app_id and app_tkh_id <> 0)";
                stmt = con.prepareStatement(sql);
                stmt.setLong(1, cos_id);
                stmt.setLong(2, parent_cos_id);
                stmt.setLong(3, app_itm_id);
                stmt.executeUpdate();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }


    private void batchUpdTrackingHistoryExe(Connection con, long itm_id, long cos_id, long parent_cos_id, String sql_in_clause)
                throws SQLException {
            PreparedStatement stmt = null;
            try {
                String sql = "update TrackingHistory  set tkh_cos_res_id = ? where tkh_id in " + sql_in_clause ;
                stmt = con.prepareStatement(sql);
                stmt.setLong(1, cos_id);
                stmt.executeUpdate();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }


    private void batchUpdCourseEvaluationExe(Connection con, long itm_id, long cos_id, long parent_cos_id, String sql_in_clause)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "update CourseEvaluation set cov_cos_id = ? where cov_tkh_id in " + sql_in_clause;
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, cos_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    private void batchUpdModuleEvaluationExe(Connection con, long itm_id, long cos_id, Hashtable htModIdMap, String sql_in_clause)
                throws SQLException, cwException {
            PreparedStatement stmt = null;
            try {
                Enumeration e_mod_id = htModIdMap.keys();
                Long L_parent_mod_id;
                long parent_mod_id;
                long new_mod_id;
                for ( ; e_mod_id.hasMoreElements();) {
                    L_parent_mod_id = (Long) e_mod_id.nextElement();
                    parent_mod_id = L_parent_mod_id.longValue();
                    new_mod_id = ((Long) htModIdMap.get(L_parent_mod_id)).longValue();

                    String sql = "update ModuleEvaluation set mov_cos_id = ? , mov_mod_id = ? where mov_mod_id = ? and mov_tkh_id in " + sql_in_clause;
                    stmt = con.prepareStatement(sql);
                    stmt.setLong(1, cos_id);
                    stmt.setLong(2, new_mod_id);
                    stmt.setLong(3, parent_mod_id);
                    stmt.executeUpdate();
                    batchUpdModuleEvaluationHistoryExe(con, itm_id, parent_mod_id,
                    new_mod_id, sql_in_clause);
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }


    private void batchUpdModuleEvaluationHistoryExe(Connection con, long itm_id,
                long parent_mod_id, long ch_mod_id, String sql_in_clause) throws SQLException {
            PreparedStatement stmt = null;
            try {
                String sql = "update ModuleEvaluationhistory set mvh_mod_id = ? where mvh_mod_id = ? and mvh_tkh_id in " + sql_in_clause;
                stmt = con.prepareStatement(sql);
                stmt.setLong(1, ch_mod_id);
                stmt.setLong(2, parent_mod_id);
                stmt.executeUpdate();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
    private long map2ObjChild(Connection con, long parent_obj_id, long cos_id)
            throws SQLException, cwException {
        String sql = "select obj_id from ResourceContent,Objective,ResourceObjective where  rcn_res_id = ? and obj_obj_id_parent = ? and obj_id = rob_obj_id and rcn_res_id_content = rob_res_id ";
        PreparedStatement stmt = null;
        long ch_obj_id = 0;
        try {
            stmt = con.prepareStatement(sql);
            ResultSet rs = null;
            stmt.setLong(1, cos_id);
            stmt.setLong(2, parent_obj_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ch_obj_id = rs.getLong("obj_id");
            } else {
                throw new cwException("can not find the child of objective:"
                        + parent_obj_id);
            }

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return ch_obj_id;
    }

    private List getObjIDList(Connection con, long tkh_id) throws SQLException {
        String sql = "select distinct apm_obj_id from accomplishment where apm_tkh_id =? ";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            List obj_id_list = new ArrayList();
            pstmt.setLong(1, tkh_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                obj_id_list.add(new Long(rs.getLong("apm_obj_id")));
            }
            return obj_id_list;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }



    private void batchUpdAccomplishmentExe(Connection con, long itm_id, long cos_id, Hashtable htObjId, String sql_in_clause)
            throws SQLException, cwException {
        PreparedStatement stmt = null;
        try {
            Enumeration e_ht_obj_id = htObjId.elements();
            String sql = "update accomplishment set apm_obj_id = ? where apm_obj_id = ? and apm_tkh_id in " + sql_in_clause;
            stmt = con.prepareStatement(sql);
            for ( ; e_ht_obj_id.hasMoreElements();) {
                Hashtable htModObjid = (Hashtable) e_ht_obj_id.nextElement();
                Enumeration e_obj_id = htModObjid.keys();
                for ( ; e_obj_id.hasMoreElements();) {
                    Long L_parent_obj_id = (Long) e_obj_id.nextElement();
                    stmt.setLong(1, ((Long)htModObjid.get(L_parent_obj_id)).longValue());
                    stmt.setLong(2, L_parent_obj_id.longValue());
                    stmt.executeUpdate();
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private List getResIDList(Connection con, long tkh_id) throws SQLException {
        String sql = "select distinct pgr_res_id from progress where pgr_tkh_id =? ";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            List res_id_list = new ArrayList();
            pstmt.setLong(1, tkh_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                res_id_list.add(new Long(rs.getLong("pgr_res_id")));
            }
            return res_id_list;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private void updProgressExe(Connection con, long tkh_id, long cos_id)
            throws SQLException, cwException {
        PreparedStatement stmt = null;
        try {
            String sql = "update Progress set pgr_res_id = ? where pgr_tkh_id = ? and pgr_res_id = ? ";
            stmt = con.prepareStatement(sql);
            List res_id_list = getResIDList(con, tkh_id);
            Iterator res_it = res_id_list.iterator();
            while (res_it.hasNext()) {
                long parent_res_id = ((Long) res_it.next()).longValue();
                long ch_res_id = map2ModChild(con, parent_res_id, cos_id);
                // map2ModChild( con, parent_res_id, cos_id);
                stmt.setLong(1, ch_res_id);
                stmt.setLong(2, tkh_id);
                stmt.setLong(3, parent_res_id);
                stmt.executeUpdate();
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    private boolean batchExecUpdateProgress(Connection con, long itm_id, long cos_id, Hashtable htModIdMap, String sql_in_clause)
            throws SQLException, cwException {

        Vector all_atm = new Vector();
        Vector all_pat = new Vector();

        // update progress
        Enumeration e_mod_id = htModIdMap.keys();
        Long L_parent_mod_id;
        long parent_mod_id;
        long new_mod_id;
        String sqlPgr = "update Progress set pgr_res_id = ? where pgr_res_id = ? and pgr_tkh_id in " + sql_in_clause;
        PreparedStatement stmtPgr = con.prepareStatement(sqlPgr);

        String sqlAtm = "update ProgressAttempt set atm_pgr_res_id = ? where atm_pgr_res_id = ? and atm_tkh_id in " + sql_in_clause;
        PreparedStatement stmtAtm = con.prepareStatement(sqlAtm);

        String sqlPat = "update ProgressAttachment set pat_prg_res_id = ? where pat_prg_res_id = ? and pat_tkh_id in " + sql_in_clause;
        PreparedStatement stmtPat = con.prepareStatement(sqlPat);

        for ( ; e_mod_id.hasMoreElements();) {
            L_parent_mod_id = (Long) e_mod_id.nextElement();
            parent_mod_id = L_parent_mod_id.longValue();
            new_mod_id = ((Long) htModIdMap.get(L_parent_mod_id)).longValue();
            stmtPgr.setLong(1, new_mod_id);
            stmtPgr.setLong(2, parent_mod_id);
            stmtPgr.executeUpdate();

            stmtAtm.setLong(1, new_mod_id);
            stmtAtm.setLong(2, parent_mod_id);
            stmtAtm.executeUpdate();

            stmtPat.setLong(1, new_mod_id);
            stmtPat.setLong(2, parent_mod_id);
            stmtPat.executeUpdate();

        }

        if (stmtPgr != null)
            stmtPgr.close();

        if (stmtAtm != null)
            stmtAtm.close();

        if (stmtPat != null)
            stmtPat.close();

        return true;
    }

    private boolean updateResourceFolder(Connection con, File folder)
            throws SQLException {
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
                if (res_id > 0 && res_folder.isDirectory()) {
                    File[] sub_folders = res_folder.listFiles();
                    List tkh_id_list = getItemTKHID(con, getItmIDByResID(con,
                            res_id));
                    for (int j = 0; j < sub_folders.length; j++) {
                        File sub_folder = sub_folders[j];
                        if (sub_folder.isDirectory()) {
                            File[] lrn_sub_folders = sub_folder.listFiles();
                            for (int k = 0; k < lrn_sub_folders.length; k++) {
                                File lru_sub_folder = lrn_sub_folders[j];
                                if (lru_sub_folder.isDirectory()) {
                                    String lrn_folder_name = lru_sub_folder
                                            .getName();
                                    if (!isExitLrnFolder(lrn_folder_name,
                                            tkh_id_list)) {
                                        lru_sub_folder.delete();
                                    }
                                }
                            }
                            if (sub_folder.listFiles().length < 1) {
                                sub_folder.delete();
                            }
                        }
                    }
                    // DELETE EMPTY FOLDER
                    // if (res_folder.listFiles().length < 1) {
                    // res_folder.delete();
                    // }
                }
            }
        }
        return true;
    }

    private long getItmIDByResID(Connection con, long res_id)
            throws SQLException {
        String sql = "select cos_itm_id from course,ResourceContent where rcn_res_id_content = ? and rcn_res_id = cos_res_id";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            long itm_id = 0;
            pstmt.setLong(1, res_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                itm_id = rs.getLong("cos_itm_id");
                //
            }
            return itm_id;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    private boolean isExitLrnFolder(String lrn_folder_name, List tkh_id_list) {
        for (int i = 0; i < tkh_id_list.size(); i++) {
            String end_folder_name = "_" + tkh_id_list.get(i).toString();
            if (lrn_folder_name.trim().endsWith(end_folder_name)) {
                return true;
            }
        }
        return false;
    }

    private void clearEmptyFolder(File folder) {
        if (folder.isDirectory()) {
            File sub_folders[] = folder.listFiles();
            for (int i = 0; i < sub_folders.length; i++) {
                clearEmptyFolder(sub_folders[i]);
            }
            if (folder.listFiles().length < 1) {
                folder.delete();
            }
        }
    }

    private void setItemContent(Connection con, long itm_id)
            throws SQLException {
        String sql = "update aeItem set itm_content_def = ? where itm_id =? ";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, aeItem.PARENT);
            pstmt.setLong(2, itm_id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }



    private void batchEnroll(Connection con, long cos_res_id, long itm_id, String createUsrId)
                throws SQLException, qdbException {

                PreparedStatement stmt = con
                        .prepareStatement("INSERT INTO Enrolment "
                                + " (enr_res_id, enr_ent_id, enr_status, enr_create_timestamp, enr_create_usr_id ) "
                                + " select ?, app_ent_id , ?, min(app_create_timestamp), ? from aeApplication where app_itm_id = ? and app_status = ? group by app_ent_id ");                 stmt.setLong(1, cos_res_id);
                stmt.setString(2, dbCourse.COS_ENROLL_OK);
                stmt.setString(3, createUsrId);
                stmt.setLong(4, itm_id);
                stmt.setString(5, aeApplication.ADMITTED);

                int stmtResult = stmt.executeUpdate();
                stmt.close();
//                long t2 = System.currentTimeMillis();
//                System.out.println("insert Time used: " + ((long) (t2 - t1) / 1000 ) + " sec");

            // insert access control list
//            String[] lrn_role_lst = dbUtils.getOrgLrnRole(con, lrnrSteEntId);
                boolean read = true;
                boolean write = false;
                boolean exec = false;
                createResourcePermissionForApplicant(con, cos_res_id, itm_id);
        }


        private void createResourcePermissionForApplicant(Connection con, long cos_res_id, long itm_id) throws SQLException{
            PreparedStatement stmt = con
                .prepareStatement("      INSERT INTO ResourcePermission "
                + "         (rpm_read "
                + "         ,rpm_write "
                + "         ,rpm_execute "
                + "         ,rpm_res_id "
                + "         ,rpm_ent_id "
                + "         ,rpm_rol_ext_id) "
                + " select distinct ?, ?, ?, ?, app_ent_id , ? from aeApplication where app_itm_id = ? and app_status = ?");

            stmt.setBoolean(1, true);
            stmt.setBoolean(2, false);
            stmt.setBoolean(3, true);
            stmt.setLong(4, cos_res_id);
            stmt.setString(5, lrnRolExtId);
            stmt.setLong(6, itm_id);
            stmt.setString(7, aeApplication.ADMITTED);
            stmt.executeUpdate();
            stmt.close();
        }




    private void batchUnenroll(Connection con, long cos_res_id) throws SQLException, qdbException {
        PreparedStatement stmt = con.prepareStatement("DELETE FROM Enrolment "
                + " WHERE enr_res_id = ? ");

        stmt.setLong(1, cos_res_id);
        stmt.executeUpdate();
        stmt.close();

        dbResourcePermission.delByRoleNResId(con, cos_res_id, lrnRolExtId);
    }

}
