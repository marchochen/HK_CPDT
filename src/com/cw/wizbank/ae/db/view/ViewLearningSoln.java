package com.cw.wizbank.ae.db.view;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.ae.db.DbLearningSoln;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cwn.wizbank.utils.CommonLog;

public class ViewLearningSoln {
    public static final String ITEM_ID = "ITEM_ID";
    public static final String ITEM_TITLE = "ITEM_TITLE";
    public static final String APPN_ID = "APPN_ID";
    public static final String APPN_STATUS = "APPN_STATUS";
    public static final String APPN_PROCESS_STATUS = "APPN_PROCESS_STATUS";
    public static final String APPN_TIMESTAMP = "APPN_TIMESTAMP";
    public static final String APPN_TKH_ID = "APP_TKH_ID";
    public static final String ATTN_TIMESTAMP = "ATTN_TIMESTAMP";
    public static final String ATTN_CREATE_TIMESTAMP = "ATTN_CREATE_TIMESTAMP";
    public static final String ATTN_ATTEND_IND = "ATTN_ATTEND_IND";
    public static final String ATTN_ATTEND_STATUS_ID = "ATTN_ATTEND_STATUS_ID";
    public static final String ATTN_TYPE = "ATTN_TYPE";
    public static final String ITEM_EFF_START_DATETIME = "ITEM_EFF_START_DATETIME";
    public static final String ITEM_EFF_END_DATETIME = "ITEM_EFF_END_DATETIME";
    public static final String ITEM_CONTENT_EFF_START_DATETIME = "ITEM_CONTENT_EFF_START_DATETIME";
    public static final String ITEM_CONTENT_EFF_END_DATETIME = "ITEM_CONTENT_EFF_END_DATETIME";
    public static final String ITEM_CONTENT_EFF_DURATION = "ITEM_CONTENT_EFF_DURATION";
    private static final String RESOLVED = "RESOLVED";
	public static class MyCourses {
		public long pitm_id;
		public long itm_id;
		public String itm_title;
		public String pitm_title;
		public String itm_type;
		public Timestamp itm_content_eff_start_datetime;
		public Timestamp itm_content_eff_end_datetime;
		public int itm_content_eff_duration;
		public long app_tkh_id;
		public String app_status;
		public String app_process_status;
		public String ats_type;
		public String att_ats_id;
		public Timestamp att_create_timestamp;
		public long cov_cos_id;
		public float cov_total_time;
		public Timestamp cov_last_acc_datetime;
		public long pitm_tcr_id;
		public long itm_tcr_id;
		public String itm_tcr_title;
		public String pitm_tcr_title;
	}

    public static Hashtable getApplicationAndAttendance(Connection con, long usr_ent_id, Vector itm_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer lst = new StringBuffer();

        lst.append("(0");

        for (int i=0; i<itm_lst.size(); i++) {
            lst.append(", " + itm_lst.elementAt(i));
        }

        lst.append(")");

        stmt = con.prepareStatement(OuterJoinSqlStatements.getApplicationAndAttendance(lst.toString()));
        stmt.setLong(1, usr_ent_id);
        stmt.setLong(2, usr_ent_id);
        rs = stmt.executeQuery();

        while (rs.next()) {
            Hashtable info = new Hashtable();
            Long itm_id = new Long(rs.getLong("itm_id"));
            long app_itm_id = rs.getLong("app_itm_id");
            long app_id = rs.getLong("app_id");
            String app_status = rs.getString("app_status");
            String app_process_status = rs.getString("app_process_status");
            String app_create_timestamp = rs.getString("app_create_timestamp");
            long ats_attend_ind = rs.getLong("ats_attend_ind");
            String att_ats_id = rs.getString("att_ats_id");
            String ats_type = rs.getString("ats_type");
            String att_timestamp = rs.getString("att_timestamp");
            //add by tim
            Timestamp att_create_timestamp = rs.getTimestamp("att_create_timestamp");

            Vector v_info = (Vector)hash.get(itm_id);

            CommonLog.debug("itm_id" + itm_id + "att_create_timestamp " + att_create_timestamp);
            if (att_create_timestamp != null) {
               info.put(ATTN_CREATE_TIMESTAMP, att_create_timestamp);
            }

            if (app_itm_id != 0) {
                info.put(ITEM_ID, new Long(app_itm_id));
            }

            if (app_id != 0) {
                info.put(APPN_ID, new Long(app_id));
            }

            if (app_status != null) {
                info.put(APPN_STATUS, app_status);
            }

            if (app_process_status != null) {
                info.put(APPN_PROCESS_STATUS, app_process_status);
            }

            if (app_create_timestamp != null) {
                info.put(APPN_TIMESTAMP, app_create_timestamp);
            }

            if (ats_attend_ind == 1) {
                info.put(ATTN_ATTEND_IND, "YES");
            } else {
                info.put(ATTN_ATTEND_IND, "NO");
            }

            if (att_ats_id != null) {
                info.put(ATTN_ATTEND_STATUS_ID, att_ats_id);
            }

            if (ats_type != null) {
                info.put(ATTN_TYPE, ats_type);
            }

            if (att_timestamp != null) {
                info.put(ATTN_TIMESTAMP, att_timestamp);
            }

            if (v_info == null) {
                v_info = new Vector();
            }

            v_info.addElement(info);
            hash.put(itm_id, v_info);
        }

        stmt.close();

        return hash;
    }

    public static Hashtable getApplicationAndAttendance(Connection con, long usr_ent_id) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer lst = new StringBuffer();

        stmt = con.prepareStatement(OuterJoinSqlStatements.getApplicationAndAttendance(null, null));
        stmt.setLong(1, usr_ent_id);
        stmt.setString(2, aeApplication.ADMITTED);
        stmt.setString(3, aeApplication.PENDING);
        stmt.setString(4, aeApplication.WAITING);
        stmt.setLong(5, usr_ent_id);
        stmt.setString(6, aeApplication.ADMITTED);
        stmt.setString(7, aeApplication.PENDING);
        stmt.setString(8, aeApplication.WAITING);
        rs = stmt.executeQuery();

        while (rs.next()) {
            Hashtable info = new Hashtable();
            long parent_itm_id = rs.getLong("ire_parent_itm_id");
            Long app_itm_id = new Long(rs.getLong("app_itm_id"));
            long app_id = rs.getLong("app_id");
            String app_status = rs.getString("app_status");
            String app_process_status = rs.getString("app_process_status");
            String app_create_timestamp = rs.getString("app_create_timestamp");
            long ats_attend_ind = rs.getLong("ats_attend_ind");
            String att_ats_id = rs.getString("att_ats_id");
            String ats_type = rs.getString("ats_type");
            String att_timestamp = rs.getString("att_timestamp");
            String itm_eff_start_datetime = rs.getString("itm_eff_start_datetime");
            String itm_eff_end_datetime = rs.getString("itm_eff_end_datetime");
            String itm_title = rs.getString("itm_title");
            long app_tkh_id = rs.getLong("app_tkh_id");
            //add by tim
            Timestamp att_create_timestamp = rs.getTimestamp("att_create_timestamp");
            Timestamp itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
            Timestamp itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
            Integer itm_content_eff_duration = new Integer(rs.getInt("itm_content_eff_duration"));
        
            info.put(ITEM_ID, app_itm_id);
            info.put(APPN_ID, new Long(app_id));

            if (att_create_timestamp != null) {
                info.put(ATTN_CREATE_TIMESTAMP, att_create_timestamp);
            }

            if (app_status != null) {
                info.put(APPN_STATUS, app_status);
            }

            if (app_process_status != null) {
                info.put(APPN_PROCESS_STATUS, app_process_status);
            }

            if (app_create_timestamp != null) {
                info.put(APPN_TIMESTAMP, app_create_timestamp);
            }

            if (ats_attend_ind == 1) {
                info.put(ATTN_ATTEND_IND, "YES");
            } else {
                info.put(ATTN_ATTEND_IND, "NO");
            }

            if (att_ats_id != null) {
                info.put(ATTN_ATTEND_STATUS_ID, att_ats_id);
            }

            if (ats_type != null) {
                info.put(ATTN_TYPE, ats_type);
            }

            if (att_timestamp != null) {
                info.put(ATTN_TIMESTAMP, att_timestamp);
            }

            if (itm_eff_start_datetime != null) {
                info.put(ITEM_EFF_START_DATETIME, itm_eff_start_datetime);
            }

            if (itm_eff_end_datetime != null) {
                info.put(ITEM_EFF_END_DATETIME, itm_eff_end_datetime);
            }

	        if (itm_content_eff_start_datetime != null) {
                info.put(ITEM_CONTENT_EFF_START_DATETIME, itm_content_eff_start_datetime);
            }

            if (itm_content_eff_end_datetime != null) {
                info.put(ITEM_CONTENT_EFF_END_DATETIME, itm_content_eff_end_datetime);
            }
            
            if (itm_content_eff_duration != null) {
                info.put(ITEM_CONTENT_EFF_DURATION, itm_content_eff_duration);
            }

            if (itm_title != null) {
                info.put(ITEM_TITLE, itm_title);
            }

            Long parent_id = null;

            if (parent_itm_id != 0) {
                parent_id = new Long(parent_itm_id);
            } else {
                parent_id = app_itm_id;
            }

            if (app_tkh_id != 0) {
               info.put(APPN_TKH_ID, new Long(app_tkh_id)); 
            }

            Vector v_info = (Vector)hash.get(parent_id);

            if (v_info == null) {
                v_info = new Vector();
            }

            v_info.addElement(info);
            hash.put(parent_id, v_info);
        }

        stmt.close();

        return hash;
    }
    
    public static Vector getMyCourses(Connection con, long usr_ent_id, Vector item_type) throws SQLException {
    	Vector vec = new Vector();
    	String sql = " SELECT " + cwSQL.replaceNull("pitm.itm_id", "itm.itm_id") +    		" pitm_id, itm.itm_id, " + cwSQL.replaceNull("pitm.itm_title", "itm.itm_title") +    		" pitm_title,itm.itm_title,itm.itm_type,itm.itm_content_eff_start_datetime,itm.itm_content_eff_end_datetime,itm.itm_content_eff_duration,app_tkh_id,app_status,app_process_status,ats_type,att_ats_id,att_create_timestamp,cov_cos_id,cov_total_time,cov_last_acc_datetime" +
    		" ,pitm.itm_tcr_id pitm_tcr_id, itm.itm_tcr_id itm_tcr_id, " +
    		 cwSQL.replaceNull("ptc.tcr_title", "tc.tcr_title") + " pitm_tcr_title, tc.tcr_title itm_tcr_title " +
    		" FROM aeApplication " +    		" INNER JOIN aeItem itm on app_itm_id = itm_id and itm_status = ?" +    		" LEFT JOIN aeAttendance ON att_app_id = app_id" +    		" LEFT JOIN aeAttendanceStatus ON ats_id = att_ats_id" +    		" LEFT JOIN CourseEvaluation ON cov_tkh_id = app_tkh_id" +    		" LEFT JOIN aeItemRelation ON ire_child_itm_id = app_itm_id" +    		" LEFT JOIN aeItem pitm ON ire_parent_itm_id = pitm.itm_id" +
    		" LEFT JOIN tcTrainingCenter ptc ON pitm.itm_tcr_id = ptc.tcr_id "+
    		" LEFT JOIN tcTrainingCenter tc ON itm.itm_tcr_id = tc.tcr_id "+    		" WHERE (pitm.itm_status = ? or pitm.itm_status is null)" +    		" AND app_status in (?,?,?)" +    		" AND app_ent_id = ?" +    		//" AND (ats_type in (?, ?) OR ats_type is null)";
    		//Exclude ATTEND record
			" AND (ats_type in (?) OR ats_type is null)";
    	if (item_type != null && (item_type.size()==1)) {
    		sql += " AND itm.itm_type = ? ";    	}    		    	sql += " ORDER BY att_create_timestamp";
    	int idx = 1;
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(idx++, aeItem.ITM_STATUS_ON);
		stmt.setString(idx++, aeItem.ITM_STATUS_ON);
		stmt.setString(idx++, aeApplication.ADMITTED);
		stmt.setString(idx++, aeApplication.PENDING);
		stmt.setString(idx++, aeApplication.WAITING);
		stmt.setLong(idx++, usr_ent_id);
		stmt.setString(idx++, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
//		stmt.setString(idx++, aeAttendanceStatus.STATUS_TYPE_ATTEND);	
		if (item_type != null && (item_type.size()==1)) {
			stmt.setString(idx++, item_type.elementAt(0).toString());	
		}
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			MyCourses mc = new MyCourses();
			mc.pitm_id = rs.getLong("pitm_id");
			mc.itm_id = rs.getLong("itm_id");
			mc.pitm_tcr_id = rs.getLong("pitm_tcr_id");
			mc.itm_tcr_id = rs.getLong("itm_tcr_id");
			mc.pitm_tcr_title = rs.getString("pitm_tcr_title");
			mc.itm_tcr_title = rs.getString("itm_tcr_title");
			mc.pitm_title = rs.getString("pitm_title");
			mc.itm_title = rs.getString("itm_title");
			mc.itm_type = rs.getString("itm_type");
			mc.itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
			mc.itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
			mc.itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
			mc.app_tkh_id = rs.getLong("app_tkh_id");
			mc.app_status = rs.getString("app_status");
			mc.app_process_status = rs.getString("app_process_status");
			mc.ats_type = rs.getString("ats_type");
			mc.att_ats_id = rs.getString("att_ats_id");
			mc.att_create_timestamp = rs.getTimestamp("att_create_timestamp");
			mc.cov_cos_id = rs.getLong("cov_cos_id");
			mc.cov_total_time = rs.getFloat("cov_total_time");
			mc.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
			vec.add(mc);
		}
		stmt.close();
    	return vec;
    }

    public static void getItemInfo(Connection con, Vector itm_lst, Hashtable hash, Vector vec) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer item_lst = new StringBuffer();

        item_lst.append("(0");

        for (int i=0; i<itm_lst.size(); i++) {
            item_lst.append(", " + itm_lst.elementAt(i));
        }

        item_lst.append(")");

        stmt = con.prepareStatement(OuterJoinSqlStatements.getItemInfo(item_lst.toString()));
        rs = stmt.executeQuery();

        while (rs.next()) {
            aeItem item = new aeItem();
            item.itm_id = rs.getLong("itm_id");
            item.itm_code = rs.getString("itm_code");
            item.itm_title = rs.getString("itm_title");
            item.itm_type = rs.getString("itm_type");
            item.itm_tcr_id = rs.getLong("itm_tcr_id");
            item.itm_tcr_title = rs.getString("itm_tcr_title");
            item.cos_res_id = rs.getLong("cos_res_id");
            item.itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
            item.itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
            item.itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
            item.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            item.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            item.itm_apply_method = rs.getString("itm_apply_method");
            item.itm_unit = rs.getFloat("itm_unit");
            item.itm_status = rs.getString("itm_status");
            item.itm_run_ind = rs.getBoolean("itm_run_ind");
            item.itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
            item.itm_life_status = rs.getString("itm_life_status");

            hash.put(new Long(item.itm_id), item);

            if (! vec.contains(new Long(item.itm_id))) {
                vec.addElement(new Long(item.itm_id));
            }
        }

        stmt.close();
    }

    public static String getPeriodXML(Connection con, long root_ent_id) throws SQLException, cwException {
        PreparedStatement stmt;
        ResultSet rs;
        String period_xml;

        stmt = con.prepareStatement(SqlStatements.sql_get_period_xml);
        stmt.setLong(1, root_ent_id);
        rs = stmt.executeQuery();

        if (rs.next()) {
            period_xml = cwSQL.getClobValue(rs, "snt_xml");
        } else {
//            throw new cwException("com.cw.wizbank.ae.db.ViewLearningSolnTemplate.getPeriodXML: Fail to retrieve aeLearningSolntemplate from DB");
            period_xml = null;
        }
        rs.close();
        stmt.close();
        return period_xml;
    }

/*    public static Hashtable getItemVersionInfo(Connection con, Enumeration enum_soln_info) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer lst = new StringBuffer();
        StringBuffer xml = new StringBuffer();

        lst.append("(0");

        while (enum_soln_info.hasMoreElements()) {
            lst.append(", " + enum_soln_info.nextElement());
        }

        lst.append(")");
        xml.append("select item1.itm_id prev, item2.itm_id cur from aeItem as item1, aeItem as item2 where item1.itm_id in ").append(lst.toString()).append(" AND item1.itm_code = item2.itm_code AND item2.itm_deprecated_ind = 0");
        stmt = con.prepareStatement(xml.toString());
        rs = stmt.executeQuery();

        while (rs.next()) {
            hash.put(new Long(rs.getLong("prev")), new Long(rs.getLong("cur")));
        }

        stmt.close();

        return hash;
    }*/

    public static Hashtable getMyTrainingNeeds(Connection con, long usr_ent_id, int threshold) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        Hashtable hash = new Hashtable();
        stmt = con.prepareStatement(SqlStatements.sql_get_my_training_needs);
        stmt.setString(1, RESOLVED);
        stmt.setString(2, RESOLVED);
        stmt.setLong(3, usr_ent_id);
        rs = stmt.executeQuery();

        while (rs.next()) {
            if(rs.getFloat("average") > threshold) {
                if(hash.containsKey(new Long(rs.getLong("itm_id")))) {
                    ArrayList arrayList = (ArrayList) hash.get(new Long(rs.getLong("itm_id")));
                    arrayList.add(new Long(rs.getLong("skl_skb_id")));
                    
                    hash.put(new Long(rs.getLong("itm_id")), arrayList);
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new Long(rs.getLong("skl_skb_id")));
                    hash.put(new Long(rs.getLong("itm_id")), arrayList);
                }
            }
        }
        stmt.close();

        return hash;
    }

    public static Hashtable getMyLeanringSoln(Connection con, long usr_ent_id, long ent_id) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        Hashtable hash = new Hashtable();
		String groupAncesterSql = dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		String gradeAncesterSql = dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
		
		int index = 1;
        if( ent_id != 0 ) {
            stmt = con.prepareStatement(SqlStatements.getMyLearningSoln(con, groupAncesterSql, gradeAncesterSql));
            stmt.setLong(index++, usr_ent_id);
            stmt.setString(index++, "% " + ent_id + " %");
            stmt.setString(index++, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, "% " + ent_id + " %");
			stmt.setString(index++, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
        } else {
            stmt = con.prepareStatement(SqlStatements.getMyLearningSolnNonPeer(con, groupAncesterSql, gradeAncesterSql));
            stmt.setLong(index++, usr_ent_id);
            stmt.setString(index++, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
        }

        rs = stmt.executeQuery();

        while (rs.next()) {
            hash.put(new Long(rs.getLong("itm_id")), new Long(rs.getLong("lsn_period_id")));
        }

        stmt.close();

        return hash;
    }

    /*
    Get the enity id of the user who created the record
    */
    public static Vector checkSelfInitiated(Connection con, long usr_ent_id, long ent_id) throws SQLException {
        return checkSelfInitiated(con, usr_ent_id, ent_id, true);
    }    
            
    public static Vector checkSelfInitiated(Connection con, long usr_ent_id, long ent_id, boolean status) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        Vector vtItem = new Vector();

        if( ent_id != 0 ) {
            stmt = con.prepareStatement(SqlStatements.sql_get_learning_soln_created_by);
            stmt.setLong(1, usr_ent_id);
            stmt.setString(2, "% " + ent_id + " %");
            stmt.setBoolean(3, false);
            stmt.setString(4, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
        }else {
            stmt = con.prepareStatement(SqlStatements.sql_get_learning_soln_created_by_non_peer);
            stmt.setLong(1, usr_ent_id);
            stmt.setBoolean(2, false);
            stmt.setString(3, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
        }
        rs = stmt.executeQuery();
        while (rs.next()) {
            if (status == true) {
                Boolean isSelf = new Boolean(false);
                if (usr_ent_id == rs.getLong("usr_ent_id"))  {
                    vtItem.addElement(new Long(rs.getLong("itm_id")));
                }
            }
            else {
                Boolean notSelf = new Boolean(false);
                if (usr_ent_id != rs.getLong("usr_ent_id"))  {
                    vtItem.addElement(new Long(rs.getLong("itm_id")));
                }
            }
        }
        
        stmt.close();
        
        return vtItem;
    }


    public static long getLearningSoln(Connection con, long usr_ent_id, long ent_id, long itm_id) throws SQLException {
        return getLearningSoln(con, usr_ent_id, null, ent_id, itm_id, 0);
    }
    
    public static long getLearningSoln(Connection con, long usr_ent_id, String usr_id, long ent_id, long itm_id, long upd_usr_ent_id) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        long soln_itm_id = 0;
        
        String query_suffix = "";
        if (upd_usr_ent_id != 0) {
            if (usr_ent_id == upd_usr_ent_id) { 
                // get the learning solution that is added by the learner himself
                query_suffix += " AND lsn_create_usr_id = ? ";
            }
            else {
                // get the learning solution that is recommended by others
                query_suffix += " AND lsn_create_usr_id <> ? ";
            }
        }
        
        if( ent_id != 0 ) {
            stmt = con.prepareStatement(SqlStatements.sql_get_learning_soln + query_suffix);
            stmt.setLong(1, itm_id);
            stmt.setLong(2, usr_ent_id);
            stmt.setString(3, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
            stmt.setString(4, "% " + ent_id + " %");
            if (upd_usr_ent_id != 0) {
                stmt.setString(5, usr_id);
            }
        } else {
            stmt = con.prepareStatement(SqlStatements.sql_get_learning_soln_non_peer + query_suffix);
            stmt.setLong(1, itm_id);
            stmt.setLong(2, usr_ent_id);
            stmt.setString(3, DbLearningSoln.LSN_TYPE_LEARNING_PLAN);
            if (upd_usr_ent_id != 0) {
                stmt.setString(4, usr_id);
            }
        }
        rs = stmt.executeQuery();
        
        if (rs.next()) {
            soln_itm_id = rs.getLong("lsn_itm_id");
        }
        rs.close();
        stmt.close();
        
        return soln_itm_id;
    }

    public static long getParentItemId(Connection con, long itm_id) throws SQLException {
        long parent_id = 0;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_parent_itm_id);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            parent_id = rs.getLong("ire_parent_itm_id");
        }
        rs.close();
        stmt.close();

        return parent_id;
    }

    public static Vector getPgmItemIds(Connection con, long pgm_itm_id) throws SQLException {
        Vector vec = new Vector();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_pgm_itm_ids);
        stmt.setLong(1, pgm_itm_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            vec.addElement(new Long(rs.getLong("pdt_itm_id")));
        }

        stmt.close();

        return vec;
    }

    /*
    Get the earliest class/course that can be enrolled
    */
    public static Hashtable getEnrollmentInfo(Connection con, Vector itm_lst, boolean hasOffReadPrivilege, Timestamp curTime) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer lst = new StringBuffer();

        lst.append("(0");

        for (int i=0; i<itm_lst.size(); i++) {
            lst.append(", " + itm_lst.elementAt(i));
        }

        lst.append(")");

        stmt = con.prepareStatement(SqlStatements.getEnrollmentInfo(lst.toString(), hasOffReadPrivilege));
        int index = 1;
        stmt.setBoolean(index++, true);     // apply_ind
        stmt.setBoolean(index++, false);    // apply_ind
        stmt.setBoolean(index++, true);     // create_run_ind
        rs = stmt.executeQuery();

        while (rs.next()) {
            long course_id = rs.getLong("COS_ID");
            aeItem classItm = new aeItem();
            classItm.itm_id = rs.getLong("CLASS_ID");
            classItm.itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
            classItm.itm_appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
            classItm.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            classItm.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            // No class
            if (classItm.itm_id == 0) {
                classItm.itm_id = course_id;
                if ((classItm.itm_appn_end_datetime == null || curTime.before(classItm.itm_appn_end_datetime)) &&
                    (classItm.itm_eff_end_datetime == null || curTime.before(classItm.itm_eff_end_datetime))) {
                    hash.put(new Long(course_id), classItm);
                }
            // Has run
            }else {
                // No avaliable run yet
                if (hash.get(new Long(course_id)) == null) {
                    if ((classItm.itm_appn_end_datetime == null || curTime.before(classItm.itm_appn_end_datetime))  &&
                        (classItm.itm_eff_end_datetime == null || curTime.before(classItm.itm_eff_end_datetime))) {
                        hash.put(new Long(course_id), classItm);
                    }
                }
            }
        }

        stmt.close();

        return hash;
    }

}