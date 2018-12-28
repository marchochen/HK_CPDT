package com.cw.wizbank.ae.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;

public class ViewCourseReport {
    public class Data {
        public Data() { ; }
        public long itm_id;
        public String itm_title;
        public String itm_code;
        public String itm_version_code;
        public String itm_type;
        public long itm_capacity;
        public long itm_min_capacity;
        public float itm_unit;
        public Timestamp itm_eff_start_datetime;
        public Timestamp itm_eff_end_datetime;
        public String itm_status;
        public String itm_life_status;
        public String itm_apply_method;
        public String itm_person_in_charge;
        public String itm_imd_id;
        public String itm_cancellation_type;
        public long itm_create_run_ind;
		public long itm_run_ind;
        public long cos_res_id;
        public String itm_dummy_type;
        // added for REPORT_VIEW
        public String itm_xml;
    }

    public class Mote {
        public Mote() { ; }
        public long imt_id;
        public String imt_rating_target;
        public String imt_cost_target;
        public String imt_cost_actual;
        public long imt_level1_ind;
        public long imt_level2_ind;
        public long imt_level3_ind;
        public long imt_level4_ind;
        public String imt_status;
        public String imt_participant_target;
        public String imt_rating_actual_xml;
    }

    public class MoteDefault {
        public MoteDefault() { ; }
        public long imd_id;
        public long imd_level1_ind;
        public long imd_level2_ind;
        public long imd_level3_ind;
        public long imd_level4_ind;
    }
    
    public class Attendance {
        public Attendance() { ; }
//        public long num_of_admitted;
//        public Vector ats_attend_lst;
        public Hashtable ats_attendance;
    }
    
    public class ItemAccess {
        public ItemAccess() { ; }
        public long usr_ent_id;
        public String iac_access_id;
        public String usr_id;
        public String usr_first_name_bil;
        public String usr_last_name_bil;
        public String usr_display_bil;
    }
    
    public Vector search(Connection con, cwPagination page, long root_ent_id, String itm_code, Vector itm_lst_vec, String itm_title, int itm_title_partial_ind, String[] itm_type, int show_run_ind, int show_old_version, String[] content, String col_order) throws SQLException {
        Vector vec = new Vector();
        StringBuffer sql = new StringBuffer();
        String tableName = null;
        String colName = "tmp_itm_id";

        if (itm_lst_vec != null) {
            tableName = com.cw.wizbank.util.cwSQL.createSimpleTemptable(con, "tmp_itm_id", com.cw.wizbank.util.cwSQL.COL_TYPE_LONG, 0);
            com.cw.wizbank.util.cwSQL.insertSimpleTempTable(con, tableName, itm_lst_vec, com.cw.wizbank.util.cwSQL.COL_TYPE_LONG);
//            sql.append(" AND itm_id IN (SELECT tmp_itm_id FROM " + tableName + ") ");
        }

        sql.append(OuterJoinSqlStatements.courseReportSearch(tableName, colName));
        sql.append(" itm_owner_ent_id = ").append(root_ent_id).append(" AND  itm_session_ind = 0 ");
        sql.append(" AND (itm_life_status is null OR itm_life_status = '").append(aeItem.ITM_LIFE_STATUS_DISCONTINUE).append("') ");
        
        String ItemTitleToSet = null;
        if (itm_title != null) {
			sql.append(" AND lower(itm_title) LIKE ? ");
            
            if (itm_title_partial_ind == 1) {
                ItemTitleToSet = "%" + itm_title + "%";
            } else {
                ItemTitleToSet = itm_title;
            }
        }
        
        if (show_old_version == 0 ){
            sql.append(" AND itm_deprecated_ind = 0 ");
        }

        if (itm_type != null && itm_type.length != 0) {
        	sql.append(" and ( (").append(aeItemDummyType.genSqlByItemDummyType(itm_type[0], null, false)).append(") ");
//            sql.append(" AND (itm_type LIKE '" + itm_type[0] + "' ");
            
            for (int i=1; i<itm_type.length; i++) {
//                sql.append(" OR itm_type LIKE '" + itm_type[i] + "' ");
            	sql.append(" or (").append(aeItemDummyType.genSqlByItemDummyType(itm_type[i], null, false)).append(") ");
            }

            sql.append(") ");
        }
      
        sql.append(col_order);
        PreparedStatement stmt = con.prepareStatement(sql.toString());        

        // set all statement variables
        int idx = 1;
        if (itm_title != null) {
			stmt.setString(idx++, ItemTitleToSet.toLowerCase());
        }
        
        ResultSet rs = stmt.executeQuery();
        handleData(con, rs, vec);


        if (itm_lst_vec != null) {
            cwSQL.dropTempTable(con, tableName);
        }
        
        stmt.close();
        
        return vec;
    }

    private void handleData(Connection con, ResultSet rs, Vector vec) throws SQLException {
        while (rs.next()) {
            Data d = new Data();

            d.itm_id = rs.getLong("itm_id");
            d.itm_title = rs.getString("itm_title");
            d.itm_code = rs.getString("itm_code");
            d.itm_version_code = rs.getString("itm_version_code");
            d.itm_type = rs.getString("itm_type");
            d.itm_capacity = rs.getLong("itm_capacity");
            d.itm_min_capacity = rs.getLong("itm_min_capacity");
            d.itm_unit = rs.getFloat("itm_unit");
            d.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            d.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            d.itm_status = rs.getString("itm_status");
            d.itm_life_status = rs.getString("itm_life_status");
            d.itm_apply_method = rs.getString("itm_apply_method");
            d.itm_person_in_charge = rs.getString("itm_person_in_charge");
            d.itm_imd_id = rs.getString("itm_imd_id");
            d.itm_run_ind = rs.getLong("itm_run_ind");
			d.itm_create_run_ind = rs.getLong("itm_create_run_ind");
            d.cos_res_id = rs.getLong("cos_res_id");
            d.itm_dummy_type = aeItemDummyType.getDummyItemType(d.itm_type, rs.getBoolean("itm_blend_ind"), rs.getBoolean("itm_exam_ind"), rs.getBoolean("itm_ref_ind"));
            // added for REPORT_VIEW
            d.itm_xml = cwSQL.getClobValue(rs, "itm_xml");

            vec.addElement(d);            
        }
    }

/* not ready
    public Vector reorder(Connection con, cwPagination page, Vector v_data, String col_order) throws SQLException {
        Vector vec = new Vector();

        if (v_data != null && v_data.size() != 0) {
            Vector itm_vec = new Vector();

            for (int i=0; i<v_data.size(); i++) {
                Data d = (Data)v_data.elementAt(i);
                itm_vec.addElement(new Long(d.itm_id));
            }
System.out.println(OuterJoinSqlStatements.learnerReportReorder(con, cwUtils.vector2list(app_vec), col_order));
            PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.learnerReportReorder(con, cwUtils.vector2list(app_vec), col_order));
            ResultSet rs = stmt.executeQuery();
            handleData(rs, vec, null, false);
            stmt.close();
        }
        
        return vec;
    }*/
    
    public Vector searchRun(Connection con, Vector itm_lst_vec, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Hashtable hash) throws SQLException {
        Vector vec = new Vector();
        String colName = "tmp_itm_id";
        String tableName = com.cw.wizbank.util.cwSQL.createSimpleTemptable(con, colName, com.cw.wizbank.util.cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);                
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT item1.itm_id parent, item2.itm_id, item2.itm_title, item2.itm_code, item2.itm_version_code, item2.itm_type, item2.itm_capacity, item2.itm_min_capacity, item2.itm_unit, item2.itm_eff_start_datetime, item2.itm_eff_end_datetime, item2.itm_status, item2.itm_life_status, item2.itm_apply_method, item2.itm_person_in_charge, item2.itm_imd_id, item2.itm_cancellation_type ");
        sql.append("FROM aeItem item1, aeItem item2, aeItemRelation, ").append(tableName).append(" WHERE ");
        sql.append(" item1.itm_id = ").append(colName).append(" AND ");
        sql.append(" ire_parent_itm_id = item1.itm_id AND ire_child_itm_id = item2.itm_id ");
        sql.append(" ORDER BY item2.itm_eff_start_datetime, item2.itm_title ");
        
        PreparedStatement stmt = con.prepareStatement(sql.toString());        
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Data d = new Data();
            Long parent_itm_id = new Long(rs.getLong("parent")); 
            Vector run_lst = (Vector)hash.get(parent_itm_id);
            
            d.itm_id = rs.getLong("itm_id");
            d.itm_title = rs.getString("itm_title");
            d.itm_code = rs.getString("itm_code");
            d.itm_version_code = rs.getString("itm_version_code");
            d.itm_type = rs.getString("itm_type");
            d.itm_capacity = rs.getLong("itm_capacity");
            d.itm_min_capacity = rs.getLong("itm_min_capacity");
            d.itm_unit = rs.getFloat("itm_unit");
            d.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            d.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            d.itm_status = rs.getString("itm_status");
            d.itm_life_status = rs.getString("itm_life_status");
            d.itm_apply_method = rs.getString("itm_apply_method");
            d.itm_person_in_charge = rs.getString("itm_person_in_charge");
            d.itm_imd_id = rs.getString("itm_imd_id");
            d.itm_cancellation_type = rs.getString("itm_cancellation_type");
//            d.itm_create_run_ind = rs.getLong("itm_create_run_ind");

            if (run_lst == null) {
                run_lst = new Vector();
            }

            run_lst.addElement(d);
            hash.put(parent_itm_id, run_lst);
            vec.addElement(new Long(d.itm_id));
        }

        cwSQL.dropTempTable(con, tableName);
        stmt.close();
        
        return vec;
    }
    
    public Hashtable searchMoteWithoutRun(Connection con, Vector itm_lst_vec) throws SQLException {
        Hashtable hash = new Hashtable();
        String colName = "tmp_itm_id";
        String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT itm_id, imd_id, imd_level1_ind, imd_level2_ind, imd_level3_ind, imd_level4_ind ");
        sql.append("FROM aeItemMoteDefault, aeItem, ").append(tableName).append(" WHERE ");
        sql.append(" itm_id = ").append(colName).append(" AND ");        
        sql.append(" itm_imd_id = imd_id");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            MoteDefault m = new MoteDefault();
            Long itm_id = new Long(rs.getLong("itm_id"));
            m.imd_id = rs.getLong("imd_id");
            m.imd_level1_ind = rs.getLong("imd_level1_ind");
            m.imd_level2_ind = rs.getLong("imd_level2_ind");
            m.imd_level3_ind = rs.getLong("imd_level3_ind");
            m.imd_level4_ind = rs.getLong("imd_level4_ind");
            hash.put(itm_id, m);
        }

        cwSQL.dropTempTable(con, tableName);
        stmt.close();
        
        return hash;
    }

    public Hashtable searchMoteWithRun(Connection con, Vector itm_lst_vec) throws SQLException {
        Hashtable hash = new Hashtable();
        String colName = "tmp_itm_id";
        String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT itm_id, imt_id, imt_rating_target, imt_cost_target, imt_cost_actual, imt_level1_ind, imt_level2_ind, imt_level3_ind, imt_level4_ind, imt_status, imt_participant_target ");
        sql.append("FROM aeItemMote, aeItem, ").append(tableName).append(" WHERE ");
        sql.append(" itm_id = ").append(colName).append(" AND ");        
        sql.append(" itm_imd_id = imt_imd_id");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Mote m = new Mote();
            Long itm_id = new Long(rs.getLong("itm_id"));
            m.imt_id = rs.getLong("imt_id");
            m.imt_rating_target = rs.getString("imt_rating_target");
            m.imt_cost_target = rs.getString("imt_cost_target");
            m.imt_cost_actual = rs.getString("imt_cost_actual");
            m.imt_level1_ind = rs.getLong("imt_level1_ind");
            m.imt_level2_ind = rs.getLong("imt_level2_ind");
            m.imt_level3_ind = rs.getLong("imt_level3_ind");
            m.imt_level4_ind = rs.getLong("imt_level4_ind");
            m.imt_status = rs.getString("imt_status");
            m.imt_participant_target = rs.getString("imt_participant_target");

            PreparedStatement stmt1 = con.prepareStatement("SELECT imt_rating_actual_xml FROM aeItemMote WHERE imt_id = ?");
            stmt1.setLong(1, m.imt_id);
            ResultSet rs1 = stmt1.executeQuery();

            if (rs1.next()) {
                m.imt_rating_actual_xml = cwSQL.getClobValue(rs1, "imt_rating_actual_xml");
            }
            
            stmt1.close();
            hash.put(itm_id, m);
        }

        cwSQL.dropTempTable(con, tableName);
        stmt.close();
        
        return hash;
    }

/*    public Attendance searchAttendance(Connection con, long itm_id, Timestamp app_start_upd_timestamp, Timestamp app_end_upd_timestamp, Vector ats_attended_ind_vec) throws SQLException {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT att_ats_id, count(att_ats_id) AS cnt ");
        sql.append("FROM aeApplication, aeAttendance, aeAttendanceStatus ");
        sql.append("WHERE app_itm_id = ").append(itm_id).append(" AND att_app_id = app_id AND att_ats_id = ats_id ");

        if (app_start_upd_timestamp != null) {
            sql.append("AND att_update_timestamp >= ? ");
        }
        
        if (app_end_upd_timestamp != null) {
            sql.append("AND att_update_timestamp <= ? ");
        }
        
        sql.append("GROUP BY att_ats_id");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;

        if (app_start_upd_timestamp != null) {
            stmt.setTimestamp(index++, app_start_upd_timestamp);
        }
        
        if (app_end_upd_timestamp != null) {
            stmt.setTimestamp(index++, app_end_upd_timestamp);
        }
        
        ResultSet rs = stmt.executeQuery();
        Attendance a = new Attendance();
        a.ats_attendance = new Hashtable();

        while (rs.next()) {
            a.ats_attendance.put(new Long(rs.getLong("att_ats_id")), new Long(rs.getLong("cnt")));
        }
        
        stmt.close();

        return a;
    }*/

    /**
    Old API keep for backward compatibility
    @deprecated
    */
    public Hashtable searchAttendance(Connection con, Vector itm_lst_vec, Timestamp app_start_upd_timestamp, Timestamp app_end_upd_timestamp, Vector ats_attended_ind_vec) throws SQLException {
        return searchAttendance(con, itm_lst_vec, app_start_upd_timestamp, app_end_upd_timestamp, null, ats_attended_ind_vec);
    }

    public Hashtable searchAttendance(Connection con, Vector itm_lst_vec, Timestamp app_start_upd_timestamp, Timestamp app_end_upd_timestamp, String app_datetime_restriction, Vector ats_attended_ind_vec) throws SQLException {
        Hashtable hash = new Hashtable();
        StringBuffer sql = new StringBuffer();
        String colName = "tmp_itm_id";
        String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);                

        if(app_datetime_restriction == null || app_datetime_restriction.length() == 0) {
            app_datetime_restriction = "att_create_timestamp";
        }

        sql.append("SELECT att_ats_id, app_itm_id, count(att_ats_id) AS cnt ");
        sql.append("FROM aeApplication, aeAttendance, aeAttendanceStatus, ").append(tableName);
        sql.append(" WHERE app_itm_id = ").append(colName).append(" AND att_app_id = app_id AND att_itm_id = app_itm_id AND att_ats_id = ats_id ");

        if (app_start_upd_timestamp != null) {
            sql.append("AND ").append(app_datetime_restriction).append(" >= ? ");
        }
        
        if (app_end_upd_timestamp != null) {
            sql.append("AND ").append(app_datetime_restriction).append(" <= ? ");
        }
        
        sql.append("GROUP BY app_itm_id, att_ats_id");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;

        if (app_start_upd_timestamp != null) {
            stmt.setTimestamp(index++, app_start_upd_timestamp);
        }
        
        if (app_end_upd_timestamp != null) {
            stmt.setTimestamp(index++, app_end_upd_timestamp);
        }
        
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Long att_ats_id = new Long(rs.getLong("att_ats_id"));
            Long itm_id = new Long (rs.getLong("app_itm_id"));
            long count = rs.getLong("cnt");
            Attendance a = (Attendance)hash.get(itm_id);
            
            if (a == null) {
                a = new Attendance();
                a.ats_attendance = new Hashtable();
            } else if (a.ats_attendance == null) {
                a.ats_attendance = new Hashtable();
            }
            
            a.ats_attendance.put(att_ats_id, new Long(count));
            hash.put(itm_id, a);
        }
        
/*        if (ats_attended_ind_vec != null) {
            for (int i=0; i<ats_attended_ind_vec.size(); i++) {
                Long ats_id = (Long)ats_attended_ind_vec.elementAt(i);

                if (a.ats_attendance.containsKey(ats_id)) {
                    a.num_of_admitted += ((Long)a.ats_attendance.get(ats_id)).longValue();
                }
            }
        }*/

        cwSQL.dropTempTable(con, tableName);
        stmt.close();

        return hash;
    }
        
    public Vector getAttendedIndAtsId(Connection con, long root_ent_id) throws SQLException {
        Vector vec = new Vector();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ats_id FROM aeAttendanceStatus ");
        sql.append("WHERE ats_attend_ind = 1 and (ats_ent_id_root = ").append(root_ent_id).append(" or ats_default_ind = 1) ");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            vec.addElement(new Long(rs.getLong("ats_id")));
        }
        
        stmt.close();
        
        return vec;
    }
    
    public Hashtable searchItemAccess(Connection con, Vector itm_lst_vec) throws SQLException {
        Hashtable hash = new Hashtable();
        StringBuffer sql = new StringBuffer();
        String colName = "tmp_itm_id";
        String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);                

        sql.append("SELECT iac_itm_id, usr_ent_id, iac_access_id, usr_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil ");
        sql.append("FROM aeItemAccess, RegUser, ").append(tableName);
        sql.append(" WHERE iac_itm_id = ").append(colName);
        //sql.append(" AND (iac_access_id LIKE 'CMAN' OR iac_access_id LIKE 'PCON') ");
        sql.append(" AND iac_ent_id = usr_ent_id");

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();        

        while (rs.next()) {
            Long itm_id = new Long(rs.getLong("iac_itm_id"));            
            ItemAccess access = new ItemAccess();
            Vector access_vec = (Vector)hash.get(itm_id);

            if (access_vec == null) {
                access_vec = new Vector();
            }

            access.usr_ent_id = rs.getLong("usr_ent_id");
            access.iac_access_id = rs.getString("iac_access_id");
            access.usr_id = rs.getString("usr_id");
            access.usr_first_name_bil = rs.getString("usr_first_name_bil");
            access.usr_last_name_bil = rs.getString("usr_last_name_bil");
            access.usr_display_bil = rs.getString("usr_display_bil");
            access_vec.addElement(access);
            hash.put(itm_id, access_vec);
        }

        cwSQL.dropTempTable(con, tableName);
        stmt.close();
        
        return hash;
    }
    
/*    public Vector searchItemAccess(Connection con, long run_id) throws SQLException {
        Vector vec = new Vector();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT usr_ent_id, iac_access_id, usr_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil ");
        sql.append("FROM aeItemAccess, RegUser ");
        sql.append("WHERE iac_itm_id = ").append(run_id);
        //sql.append(" AND (iac_access_id LIKE 'CMAN' OR iac_access_id LIKE 'PCON') ");
        sql.append(" AND iac_ent_id = usr_ent_id");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();        

        while (rs.next()) {
            ItemAccess access = new ItemAccess();
            
            access.usr_ent_id = rs.getLong("usr_ent_id");
            access.iac_access_id = rs.getString("iac_access_id");
            access.usr_id = rs.getString("usr_id");
            access.usr_first_name_bil = rs.getString("usr_first_name_bil");
            access.usr_last_name_bil = rs.getString("usr_last_name_bil");
            access.usr_display_bil = rs.getString("usr_display_bil");
            vec.addElement(access);
        }

        stmt.close();
        
        return vec;
    }    */
}