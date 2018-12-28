package com.cw.wizbank.ae.db.view;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.report.LearnerCosReport;
import com.cw.wizbank.report.LearnerLrnReport;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.report.LearnerRptExporter;




public class ViewLearnerReport implements Serializable{
    public static String VEC_DATA = "VEC_DATA";
    public static String TOTAL_UNITS = "TOTAL_UNITS";
    public static String MODULE_COUNT = "MODULE_COUNT";

    public class Data implements Serializable{
        public Data() { ; }
        public long t_id;

        public long usr_ent_id;
        public String usr_ste_usr_id;
        public String usr_first_name_bil;
        public String usr_last_name_bil;
        public String usr_display_bil;
        public String usr_email;
        public String usr_tel_1;
        public String group_name;
        public String grade_name;
        public long app_id;
        public Timestamp app_create_timestamp;
        public String app_status;
        public String app_process_status;
        public long app_tkh_id;
        public String t_title;
        public float t_unit;
        public String t_code;
        public String t_type;
        public Timestamp t_eff_start_datetime;
        public Timestamp t_eff_end_datetime;
        public String t_apply_method;
        public long itm_id;
        public String itm_title;
        public Timestamp itm_eff_start_datetime;
        public Timestamp itm_eff_end_datetime;
        public Timestamp att_timestamp;
        public Timestamp att_create_timestamp;
        public long att_ats_id;
        public String att_remark;
        public String att_rate;
        public long cos_res_id;
        public Timestamp cov_last_acc_datetime;
        public Timestamp cov_commence_datetime;
        public float cov_total_time;
        public String cov_score;
        public String cov_max_score;
        public String cov_status;
        public String cov_comment;
        public Timestamp cov_complete_datetime;
        public int totalAttempt;
        public long tcr_id;
        public String tcr_title;
        public boolean itm_blend_ind;
        public boolean itm_exam_ind;
        public boolean itm_ref_ind;
        public String itm_dummy_type;
    }

    public Hashtable search(Connection con, cwPagination page, String[] ent_id_lst, Vector itm_lst_vec, Vector ats_lst, Vector ats_attend_vec, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, String col_order, long usr_ent_id, long root_ent_id,boolean tc_enabled, boolean isMyStaff,String lrn_scope_sql
            ,boolean is_realTime_view_rpt) throws SQLException {
        String entIdTableName = null;
        String entIdColName = null;
        Vector usr_ent_ids = null;
        
        String itmIdTableName = null;
        String itmIdColName = null;
        // create temp table name of itm_id & ent_id
        if (ent_id_lst != null && ent_id_lst.length > 0) {
            Vector in_ent_ids = new Vector();
            for (int i = 0;i < ent_id_lst.length;i++) {
                in_ent_ids.addElement(new Long(ent_id_lst[i]));
            }
            Vector ent_id_vec = new Vector();
            usr_ent_ids = LearnerRptExporter.getUserEntId(con, ent_id_vec, in_ent_ids);
            entIdColName = "usr_ent_id";
            entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);
        }
        if (itm_lst_vec != null) {
            itmIdColName = "tmp_app_itm_id";
            itmIdTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
        }

        // insert data to temp table
        if (entIdTableName != null) {
            cwSQL.insertSimpleTempTable(con, entIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
        }
        if (entIdTableName == null && lrn_scope_sql != null) {
            entIdColName = "usr_ent_id";
            entIdTableName = cwSQL.createSimpleTemptable(con,entIdColName, cwSQL.COL_TYPE_LONG, 0, lrn_scope_sql);
        }
        Vector vec = new Vector();
        if (itmIdTableName != null) {
            itmIdColName = "tmp_app_itm_id";
            cwSQL.insertSimpleTempTable(con, itmIdTableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);
        }
        PreparedStatement stmt = LearnerRptExporter.getRptStmt(con, ats_lst, col_order, itmIdTableName, itmIdColName, entIdTableName, entIdColName, att_create_start_datetime, att_create_end_datetime,usr_ent_id,root_ent_id, tc_enabled,isMyStaff,is_realTime_view_rpt);
        ResultSet rs = stmt.executeQuery();
        Hashtable hash = handleData(con, rs, ats_lst, ats_attend_vec, false, tc_enabled);

        if (itm_lst_vec != null) {
            cwSQL.dropTempTable(con, itmIdTableName);
        }
        
        if (entIdTableName != null) {
            cwSQL.dropTempTable(con, entIdTableName);            
        }
        
        stmt.close();
        return hash;
    }
    
    private Hashtable handleData(Connection con, ResultSet rs, Vector ats_lst, Vector ats_attend_vec, boolean isLrnHist, boolean tc_enabled) throws SQLException {
        Vector vec = new Vector();
        Hashtable hash = new Hashtable();
        float total_units = 0;
        Vector cosIdVec = new Vector();
        int i =0;        
        while (rs.next()) {
            Data d = new Data();

            d.usr_ent_id = rs.getLong("usr_ent_id");
            d.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            d.usr_display_bil = rs.getString("usr_display_bil");
            d.usr_email = rs.getString("usr_email");
            d.usr_tel_1 = rs.getString("usr_tel_1");
            d.app_id = rs.getLong("app_id");
            d.app_tkh_id = rs.getLong("app_tkh_id");
            d.t_id = rs.getLong("t_id");
            d.t_title = rs.getString("t_title");            
            d.t_code = rs.getString("t_code");
            d.t_type = rs.getString("t_type");
            d.itm_id = rs.getLong("itm_id");
            d.itm_title = rs.getString("itm_title");
            if(isLrnHist || tc_enabled) {
            	d.tcr_id = rs.getLong("tcr_id");
            	d.tcr_title = rs.getString("tcr_title");	
            }
            d.att_timestamp = rs.getTimestamp("att_timestamp");
            d.att_create_timestamp = rs.getTimestamp("att_create_timestamp");
            d.att_ats_id = rs.getLong("att_ats_id");
            d.cov_score = rs.getString("cov_score");
            d.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
            d.cov_commence_datetime = rs.getTimestamp("cov_commence_datetime");
            d.cov_total_time = rs.getFloat("cov_total_time");
            d.cos_res_id = rs.getLong("cos_res_id");
            d.itm_blend_ind = rs.getBoolean("itm_blend_ind");
            d.itm_exam_ind = rs.getBoolean("itm_exam_ind");
            d.itm_ref_ind = rs.getBoolean("itm_ref_ind");
            if (ats_lst == null ||
                ats_lst.contains((new Long(d.att_ats_id)).toString())) {
//                || (d.att_ats_id == 0 && ats_include_progress)) {
                vec.addElement(d);
                total_units += d.t_unit;
                if( !cosIdVec.contains(new Long(d.cos_res_id)) )
                    cosIdVec.addElement(new Long(d.cos_res_id));
            }
            else{
            	vec.addElement(d);
            }
            i++;
            
        }
        Hashtable modCountTable = dbResourceContent.getModuleCount(con, cosIdVec);
        hash.put(MODULE_COUNT, modCountTable);
        
        hash.put(VEC_DATA, vec);
        return hash;
    }
    public Hashtable searchLrnSolnHistory(Connection con, cwPagination page, String[] ent_id_lst, String[] ugr_ent_id_lst, Vector itm_lst_vec, String itm_title, int itm_title_partial_ind, Vector ats_lst, Vector ats_attend_vec, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, String col_order, String[] app_status_lst, long root_ent_id) throws SQLException {
        String entIdTableName = null;
        String entIdColName = null;
        Vector usr_ent_ids = null;
        String itmIdTableName = null;
        String itmIdColName = null;
        String usr_ent_id_lst = null;
        long usr_ent_id =0;
        // create temp table name of itm_id & ent_id
        if (ent_id_lst != null && ent_id_lst.length > 0) {
            Vector in_ent_ids = new Vector();
            usr_ent_id = Long.parseLong(ent_id_lst[0]);
            for (int i = 0;i < ent_id_lst.length;i++) {
                in_ent_ids.addElement(new Long(ent_id_lst[i]));
            }
            Vector ent_id_vec = new Vector();
            if(in_ent_ids !=null) {
                if(in_ent_ids.size() >10){
                    usr_ent_ids = LearnerRptExporter.getUserEntId(con, ent_id_vec, in_ent_ids);
                    entIdColName = "usr_ent_id";
                    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);            	
                } else {
                	usr_ent_id_lst = cwUtils.vector2list(in_ent_ids);
                }            	
            }
        }
        if (itm_lst_vec != null) {
            itmIdColName = "tmp_app_itm_id";
            itmIdTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
        }

        // insert data to temp table
        if (entIdTableName != null) {
            cwSQL.insertSimpleTempTable(con, entIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
        }
        if (itmIdTableName != null) {
            itmIdColName = "tmp_app_itm_id";
            cwSQL.insertSimpleTempTable(con, itmIdTableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);
        }
        PreparedStatement stmt = LearnerRptExporter.getLrnSoln(con, ats_lst, col_order, itmIdTableName, itmIdColName, false, att_create_start_datetime, att_create_end_datetime, root_ent_id, usr_ent_id_lst, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable hash = handleData(con, rs, ats_lst, ats_attend_vec, true, false);
        if (itm_lst_vec != null) {
            cwSQL.dropTempTable(con, itmIdTableName);
        }
        
        if (entIdTableName != null) {
            cwSQL.dropTempTable(con, entIdTableName);            
        }
        
        stmt.close();
        return hash;
    }
    
    //get dir nav
    public static String getCatNav(Connection con, long itm_id) throws SQLException, cwSysMessage {
    	Vector tempCatNavStr = getCatVec(con, itm_id);
        StringBuffer catNavStr = new StringBuffer();
        for (int i = 0; i < tempCatNavStr.size(); i++) {
            catNavStr.append(tempCatNavStr.get(i));
            if (i != tempCatNavStr.size() - 1) {
                catNavStr.append("\n");
            }
        }
        return catNavStr.toString();
    }
    
    public static Vector getCatVec(Connection con, long itm_id) throws SQLException, cwSysMessage {
    	Vector tempCatNavStr = new Vector();
        String[] catNav = null;
        String sql = "SELECT CATA.tnd_id AS CATID from aeTreeNode CATA, aeTreeNode ITM WHERE CATA.tnd_id = ITM.tnd_parent_tnd_id AND ITM.tnd_itm_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            aeTreeNode node = new aeTreeNode();
            node.tnd_id = rs.getLong("CATID");
            node.get(con);
            aeTreeNode[] treeNodes = node.getNavigatorTreeNode(con);
            tempCatNavStr.add(getCatNavStr(treeNodes));
        }
        rs.close();
        stmt.close();
        return tempCatNavStr;
    }

    private static String getCatNavStr(aeTreeNode[] treeNodes) {
        StringBuffer catNavStr = new StringBuffer();
        for (int i = 0;i < treeNodes.length; i++) {
            catNavStr.append(treeNodes[i].tnd_title);
            if (i != treeNodes.length - 1) {
                catNavStr.append(" > ");
            }
        }
        return catNavStr.toString();
    }
    public static Vector getAllCos(Connection con, long usr_ent_id, long root_ent_id) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append(OuterJoinSqlStatements.getMyRspLrnEnrollCosSQL(con)).append(" Union ").append(OuterJoinSqlStatements.getAllItemByTaMgtTc() + " and itm_type != 'AUDIOVIDEO'");
    	PreparedStatement stmt = con.prepareStatement(sql.toString());
    	int index = 1;
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    	
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
     	stmt.setLong(index++, root_ent_id);
     	
    	ResultSet rs = stmt.executeQuery();
    	Vector vec = new Vector();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("itm_id")));
    	}
    	stmt.close();
    	return vec;
    }
    public static Vector getMyRspCos(Connection con, long usr_ent_id, long root_ent_id) throws SQLException {
    	String sql = OuterJoinSqlStatements.getAllItemByTaMgtTc() + " and itm_type != 'AUDIOVIDEO'";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(index++, root_ent_id);
    	
    	ResultSet rs = stmt.executeQuery();
    	Vector vec = new Vector();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("itm_id")));
    	}
    	stmt.close();
    	return vec;
    }
    public static Vector getMyRspLrnEnrollCos(Connection con, long usr_ent_id, long root_ent_id) throws SQLException {
    	String sql = OuterJoinSqlStatements.getMyRspLrnEnrollCosSQL(con);
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    	
    	ResultSet rs = stmt.executeQuery();
    	Vector vec = new Vector();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("itm_id")));
    	}
    	stmt.close();
    	return vec;
    }
    public static Vector getAllLrn(Connection con, long usr_ent_id, long root_ent_id) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append(OuterJoinSqlStatements.getMyRspCosEnrollLrnSQL(con, true)).append(" Union ").append(OuterJoinSqlStatements.getAllUsrByTaMgtTc(true));
    	PreparedStatement stmt = con.prepareStatement(sql.toString());
    	int index = 1;
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(index++, root_ent_id);
    	
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    	ResultSet rs = stmt.executeQuery();
    	Vector vec = new Vector();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("usr_ent_id")));
    	}
    	stmt.close();
    	return vec;
    }
    
    public static String getAllLrnSql(String tmp_col_name, long usr_ent_id, long root_ent_id) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append(OuterJoinSqlStatements.getMyRspCosEnrollLrnSQL(tmp_col_name,false,usr_ent_id,root_ent_id))
           .append(" union all ")
           .append(OuterJoinSqlStatements.getAllUsrByTaMgtTc(tmp_col_name,false,usr_ent_id, root_ent_id));
        return sql.toString();
        /*
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        stmt.setLong(index++, usr_ent_id);
        stmt.setLong(index++, root_ent_id);
        stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        stmt.setLong(index++, root_ent_id);
        
        stmt.setLong(index++, usr_ent_id);
        stmt.setLong(index++, root_ent_id);
        stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        ResultSet rs = stmt.executeQuery();
        Vector vec = new Vector();
        while(rs.next()) {
            vec.add(new Long(rs.getLong("usr_ent_id")));
        }
        stmt.close();
        return vec;*/
    }
    
    public static Vector getMyRspLrn(Connection con, long usr_ent_id, long root_ent_id) throws SQLException {
    	String sql = OuterJoinSqlStatements.getAllUsrByTaMgtTc(true);
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    	
    	ResultSet rs = stmt.executeQuery();
    	Vector vec = new Vector();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("usr_ent_id")));
    	}
    	stmt.close();
    	return vec;
    }
    
    public static String getMyRspLrnSql(String tmp_col_name,long usr_ent_id, long root_ent_id) throws SQLException {
        return OuterJoinSqlStatements.getAllUsrByTaMgtTc(tmp_col_name,false,usr_ent_id,root_ent_id);
    }
    
    public static Vector getMyRspCosEnrollLrn(Connection con, long usr_ent_id, long root_ent_id) throws SQLException {
    	String sql = OuterJoinSqlStatements.getMyRspCosEnrollLrnSQL(con, true);
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
     	stmt.setLong(index++, root_ent_id);
    	
    	ResultSet rs = stmt.executeQuery();
    	Vector vec = new Vector();
    	while(rs.next()) {
    		vec.add(new Long(rs.getLong("usr_ent_id")));
    	}
    	stmt.close();
    	return vec;
    }
    
    public static String getMyRspCosEnrollLrn(String tmp_col_name,long usr_ent_id, long root_ent_id) throws SQLException {
        return OuterJoinSqlStatements.getMyRspCosEnrollLrnSQL(tmp_col_name,true,usr_ent_id,root_ent_id);
    }
    
    public static String getMyRspLrnAndMyRspCosStmt(Connection con,String usrColName, String itmColName, long usr_ent_id, long root_ent_id) throws SQLException {
    	Vector vec_lrn = getMyRspLrn(con, usr_ent_id, root_ent_id);
    	Vector vec_cos = getMyRspCos(con, usr_ent_id, root_ent_id);
    	String lrnTableName = null;
    	String lrnColName = null;
    	String cosTableName = null;
    	String cosColName = null;
    	if(vec_lrn !=null && vec_lrn.size() >0) {
            lrnColName = "tmp_usr_ent_id";
            lrnTableName = cwSQL.createSimpleTemptable(con, lrnColName, cwSQL.COL_TYPE_LONG, 0);
    	}
    	if(vec_cos !=null && vec_cos.size() >0) {
            cosColName = "tmp_itm_id";
            cosTableName = cwSQL.createSimpleTemptable(con, cosColName, cwSQL.COL_TYPE_LONG, 0);    		
    	}
    	if(vec_lrn !=null && vec_lrn.size() >0 && lrnTableName != null) {
    		cwSQL.insertSimpleTempTable(con, lrnTableName, vec_lrn, cwSQL.COL_TYPE_LONG);
    	}
    	if(vec_cos !=null && vec_cos.size() >0 && cosTableName != null) {
    		cwSQL.insertSimpleTempTable(con, cosTableName, vec_cos, cwSQL.COL_TYPE_LONG);
    	}
    	StringBuffer sql = new StringBuffer();
    	if(( usrColName != null || itmColName != null )&&(lrnTableName != null || cosTableName != null )) {
    		sql.append(" AND ( ");
    		if (lrnTableName != null && usrColName != null) {
    			sql.append(usrColName).append(" in ( ").append("select * from ").append(lrnTableName).append(") ");
    		}
    		if (lrnTableName != null && usrColName != null && cosTableName != null && itmColName != null) {
    			sql.append(" OR ");
    		}
    		if(cosTableName != null && itmColName != null) {
    			sql.append(itmColName).append(" in ( ").append(" select * from ").append(cosTableName).append(") ");    		
    		}
    		sql.append(" ) ");
    	}
    	return sql.toString();
    }
}
