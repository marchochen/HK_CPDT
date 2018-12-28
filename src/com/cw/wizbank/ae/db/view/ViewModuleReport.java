package com.cw.wizbank.ae.db.view;

import java.util.*;
import java.sql.*;

import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbModule;

import com.cw.wizbank.util.*;

public class ViewModuleReport{

    /*
    Get the list of enrolled learner to a course and grouped by usergroup
    @param cos_id course id
    */
    private static final String sql_get_grouped_learner = 
        " Select usg_ent_id, usg_display_bil, usr_id, usr_ent_id, usr_display_bil "
        + " from EntityRelation, RegUser, Enrolment, UserGroup "
        + " where ern_child_ent_id = enr_ent_id "
        + " and usr_ent_id = ern_child_ent_id "
        + " and usg_ent_id = ern_ancestor_ent_id and enr_res_id = ? "
        + " order by usg_display_bil, usr_display_bil ";
    
    
    public static Vector getGroupedLeaner(Connection con, long cos_id) throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement(sql_get_grouped_learner);
        stmt.setLong(1, cos_id);
        long prv_usg_ent_id = 0;
        long cur_usg_ent_id = 0;
        ResultSet rs = stmt.executeQuery();
         
        Vector allGroupVec = new Vector();
        Hashtable groupHash = null;
        dbUserGroup usg = null;
        Vector usrVec = null;
        
        while (rs.next()){
            cur_usg_ent_id = rs.getLong("usg_ent_id");
            if (cur_usg_ent_id != prv_usg_ent_id) {
                if (prv_usg_ent_id > 0) {
                    groupHash.put("GROUP", usg);
                    groupHash.put("USER", usrVec);
                    allGroupVec.addElement(groupHash);
                }
                usg = new dbUserGroup();
                usg.usg_ent_id = cur_usg_ent_id;
                usg.usg_display_bil = rs.getString("usg_display_bil");
                usrVec = new Vector();
                groupHash = new Hashtable();
            }
            dbRegUser usr = new dbRegUser();
            usr.usr_id  = rs.getString("usr_id");
            usr.usr_ent_id = rs.getLong("usr_ent_id");
            usr.usr_display_bil = rs.getString("usr_display_bil");
            usrVec.addElement(usr);
            prv_usg_ent_id = cur_usg_ent_id;
        }
        if (prv_usg_ent_id > 0) {
            groupHash.put("GROUP", usg);
            groupHash.put("USER", usrVec);
            allGroupVec.addElement(groupHash);
        }

        stmt.close();
        return allGroupVec;
    }
    
    /*
    Get the list of enrolled learner to the given course(s)
    @param cos_id_lst the list of course id
    */
    private static final String sql_get_enrolled_learner = 
        " Select distinct(usr_ent_id), usr_id, usr_display_bil from RegUser, Enrolment where "
        + " enr_ent_id = usr_ent_id  ";
    
    public static Vector getEnrolledLearner(Connection con, long cos_id) throws SQLException
    {
        Vector cosVec = new Vector();
        cosVec.addElement(new Long(cos_id));
        return getEnrolledLearner(con, cosVec);
    }
    
    public static Vector getEnrolledLearner(Connection con, Vector cosVec) throws SQLException
    {
        Vector usrVec = new Vector();
        
        if (cosVec == null || cosVec.size() ==0) {
            return usrVec;
        }

        String sql = sql_get_enrolled_learner 
            + " AND enr_res_id IN " + cwUtils.vector2list(cosVec)
            + " Order by usr_display_bil ";

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            dbRegUser usr = new dbRegUser();
            usr.usr_id  = rs.getString("usr_id");
            usr.usr_ent_id = rs.getLong("usr_ent_id");
            usr.usr_display_bil = rs.getString("usr_display_bil");
            usrVec.addElement(usr);
        }

        stmt.close();
        return usrVec;
    }

    public static Hashtable getEnrolledLearnerHash(Connection con, long cos_id) throws SQLException
    {
        Hashtable usrHash = new Hashtable();
        String sql = sql_get_enrolled_learner 
            + " AND enr_res_id = ? "
            + " Order by usr_display_bil ";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, cos_id);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            dbRegUser usr = new dbRegUser();
            usr.usr_id  = rs.getString("usr_id");
            usr.usr_ent_id = rs.getLong("usr_ent_id");
            usr.usr_display_bil = rs.getString("usr_display_bil");
            usrHash.put(usr.usr_id, usr);
        }

        stmt.close();
        return usrHash;
    }

    /**
     * @deprecated (2003-07-30 kawai)
     * if this is recovered, please modify it to not returning a ResultSet
     */
    /*
    Get the list of modules beloging to the given list of course
    @param cos_id_lst the list of course id
    */
    /*
    private static final String sql_get_module_list = 
        " Select cos_res_id AS CID, LCOS.res_title AS CTITLE, mod_res_id AS MID, LMOD.res_title AS MTITLE "
        + " From ResourceContent, Module, Resources LMOD, Resources LCOS, Course "
        + " Where mod_res_id = rcn_res_id_content And mod_res_id = LMOD.res_id "
        + " And cos_res_id = rcn_res_id And cos_res_id = LCOS.res_id ";
    public static ResultSet getModuleList(Connection con, long[] cos_id_lst, String[] mod_type_lst) throws SQLException
    {
        String sql = sql_get_module_list;
        sql += " And mod_type IN " + cwUtils.array2list(mod_type_lst)
            + " And rcn_res_id IN " + cwUtils.array2list(cos_id_lst)
            + " Order by mod_eff_start_datetime desc ";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        return rs;
    }
    */
    
    /*
    Get the score of the learner in each interaction from the progress attempt table
    @param cos_id 
    @param mod_id
    @param attempt_nbr
    */
    private static final String sql_get_usr_progress_attempt = 
        " Select atm_pgr_usr_id, atm_int_res_id, atm_order, sum(atm_score) AS USRSCORE, sum(atm_max_score) AS MAXSCORE "
     +  " From ProgressAttempt, RegUser, Enrolment, ResourceContent  "
     +  " WHERE atm_pgr_attempt_nbr= ? AND atm_pgr_res_id = ? "
     +  " AND atm_pgr_res_id = rcn_res_id_content "
     +  " AND enr_res_id = rcn_res_id "
     +  " AND atm_pgr_usr_id = usr_id "
     +  " AND usr_ent_id = enr_ent_id "
     +  " AND enr_res_id = ? "
     +  " GROUP by atm_pgr_usr_id, atm_int_res_id, atm_order "
     +  " Order by atm_pgr_usr_id, atm_order ";
     
    public static Hashtable getLearnerProgressAttempt(Connection con, long cos_id, long mod_id, long attempt_nbr) throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement(sql_get_usr_progress_attempt);
        stmt.setLong(1, attempt_nbr);
        stmt.setLong(2, mod_id);
        stmt.setLong(3, cos_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable usrHash = new Hashtable();
        Vector atmVec = null;
        String prv_usr_id = null;
        String cur_usr_id = null;
        
        while (rs.next()){
            cur_usr_id = rs.getString("atm_pgr_usr_id");
            if (prv_usr_id == null || !cur_usr_id.equals(prv_usr_id)) {
                if (prv_usr_id != null) {
                    usrHash.put(prv_usr_id, atmVec);
                }
                atmVec = new Vector();
            }
            
            dbProgressAttempt atm = new dbProgressAttempt();
            atm.atm_pgr_usr_id = rs.getString("atm_pgr_usr_id");
            atm.atm_int_res_id = rs.getLong("atm_int_res_id");
            atm.atm_score = rs.getLong("USRSCORE");
            atm.atm_max_score = rs.getLong("MAXSCORE");
            atm.atm_order = rs.getLong("atm_order");
            atmVec.addElement(atm);
            prv_usr_id = cur_usr_id;
        }
        if (prv_usr_id != null) {
            usrHash.put(prv_usr_id, atmVec);
        }
        
        stmt.close();
        
        return usrHash;
    }
    
    /*
    Get the score of the learner in a list of module from the progress table
    @param mod_id_lst the list of module id
    @param attempt_nbr
    */
    private static final String sql_get_usr_mod_progress = 
        " Select pgr_res_id, pgr_usr_id, pgr_score, pgr_max_score, pgr_complete_datetime from Progress "
     +  " WHERE pgr_attempt_nbr= ? ";
     
    public static Hashtable getLearnerProgress(Connection con, long[] mod_id_lst, long attempt_nbr) throws SQLException
    {
        Hashtable modHash = new Hashtable();
        
        if (mod_id_lst == null || mod_id_lst.length ==0) {
            return modHash;
        }
        
        String sql = sql_get_usr_mod_progress 
            + " And pgr_res_id IN " + cwUtils.array2list(mod_id_lst)
            + " Order by pgr_res_id, pgr_usr_id ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, attempt_nbr);
        ResultSet rs = stmt.executeQuery();
        Hashtable usrHash = null;
        
        long prv_mod_id = 0;
        long cur_mod_id = 0;
        
        while (rs.next()){
            cur_mod_id = rs.getLong("pgr_res_id");
            if (cur_mod_id != prv_mod_id) {
                if (prv_mod_id > 0) {
                    modHash.put(new Long(prv_mod_id), usrHash);
                }
                usrHash = new Hashtable();
            }
            dbProgress pgr = new dbProgress();
            pgr.pgr_usr_id = rs.getString("pgr_usr_id");
            pgr.pgr_score = rs.getFloat("pgr_score");
            pgr.pgr_max_score = rs.getFloat("pgr_max_score");
            pgr.pgr_complete_datetime = rs.getTimestamp("pgr_complete_datetime");
            usrHash.put(pgr.pgr_usr_id, pgr);
            prv_mod_id = cur_mod_id;
        }

        if (prv_mod_id > 0) {
            modHash.put(new Long(prv_mod_id), usrHash);
        }
        
        stmt.close();
        return modHash;
    }
    
    /*
    Get the distinct course id of which the modules belonging to
    @param mod_id_lst the list of module id
    */
    private static final String sql_get_distinct_course = 
        " Select distinct(rcn_res_id) AS COSID From ResourceContent ";
     
    public static Vector getDistinctCourse(Connection con, long[] mod_id_lst) throws SQLException
    {
        Vector courseVec = new Vector();
        
        if (mod_id_lst == null || mod_id_lst.length ==0) {
            return courseVec;
        }
        
        String sql = sql_get_distinct_course 
            + " Where rcn_res_id_content IN " + cwUtils.array2list(mod_id_lst);
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            courseVec.addElement(new Long(rs.getLong("COSID")));
        }
        
        stmt.close();
        return courseVec;
    }

    /*
    Get the course id of the each module
    @param mod_id_lst the list of module id
    */
    private static final String sql_get_module_course = 
        " Select rcn_res_id_content , rcn_res_id From ResourceContent ";
     
    public static Hashtable getModuleCourse(Connection con, long[] mod_id_lst) throws SQLException
    {
        Hashtable modHash = new Hashtable();
        
        if (mod_id_lst == null || mod_id_lst.length ==0) {
            return modHash;
        }
        
        String sql = sql_get_module_course 
            + " Where rcn_res_id_content IN " + cwUtils.array2list(mod_id_lst);
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            Long modID = new Long(rs.getLong("rcn_res_id_content"));
            Long cosID = new Long(rs.getLong("rcn_res_id"));
            modHash.put(modID, cosID);
        }
        
        stmt.close();
        return modHash;
    }
 
    /*
    Get the basic info of the modules
    @param mod_id_lst the list of module id
    */
    private static final String sql_get_module_info = 
        " Select res_id, res_title From Resources ";
     
    public static Hashtable getModuleInfo(Connection con, long[] mod_id_lst) throws SQLException
    {
        Hashtable modHash = new Hashtable();
        
        if (mod_id_lst == null || mod_id_lst.length ==0) {
            return modHash;
        }
        
        String sql = sql_get_module_info 
            + " Where res_id IN " + cwUtils.array2list(mod_id_lst);
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            dbModule dbmod = new dbModule();
            dbmod.mod_res_id = rs.getLong("res_id");
            dbmod.res_title = rs.getString("res_title");
            modHash.put(new Long(dbmod.mod_res_id), dbmod);
        }
        
        stmt.close();
        return modHash;
    }
 
    /*
    Get the course assigned to the user
    @param itmVec the vector of  items which is assigned to the user
    */
    private static final String sql_get_assignerd_course = 
        " Select cos_res_id, res_title From Resources, Course "
        + " Where res_id = cos_res_id " ;
     
    public static String getAssignedCourse(Connection con, Vector itmVec) throws SQLException
    {
        StringBuffer xml = new StringBuffer();
        
        if (itmVec == null || itmVec.size() ==0) {
            return xml.toString();
        }
        
        String sql = sql_get_assignerd_course 
            + " And cos_itm_id IN " + cwUtils.vector2list(itmVec)
            + " Order by res_title ";

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            xml.append("<course id=\"").append(rs.getLong("cos_res_id"))
               .append("\" title=\"").append(cwUtils.esc4XML(rs.getString("res_title")))
               .append("\"/>");
        }
        
        stmt.close();
        return xml.toString();
    }
 
    
}