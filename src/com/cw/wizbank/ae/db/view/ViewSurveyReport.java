package com.cw.wizbank.ae.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.db.DbEntityRelation;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwSQL;

public class ViewSurveyReport {
    public static class ViewProgressStat {
        public int total_cnt;
        public float total_score;
    }
    public static class ViewProgressAttemptStat {
        public int rcn_order;
        public long atm_int_res_id;
        public String atm_response_bil;
        public int total_cnt;
        public int total_score;
    }
    
    public static ViewProgressStat getProgressStat(Connection con, Timestamp start_datetime, Timestamp end_datetime, long mod_id, long[] ent_id_lst, long[] ugr_ent_id_lst) throws SQLException {    
        StringBuffer sqlAddCond = null;
        if (ent_id_lst!=null && ent_id_lst.length>0){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND usr_ent_id IN (");
            sqlAddCond.append(com.cw.wizbank.db.sql.SqlStatements.getUserInGroupSQL(ent_id_lst, DbEntityRelation.ERN_TYPE_USR_PARENT_USG));
            sqlAddCond.append(" )");
        }
        if (ugr_ent_id_lst!=null && ugr_ent_id_lst.length>0){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND usr_ent_id IN (");
            sqlAddCond.append(com.cw.wizbank.db.sql.SqlStatements.getUserInGroupSQL(ent_id_lst, DbEntityRelation.ERN_TYPE_USR_CURRENT_UGR));
            sqlAddCond.append(" )");
        }
        
        if (start_datetime!=null){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND pgr_complete_datetime > ? ");
        }
        if (end_datetime!=null){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND pgr_complete_datetime < ? ");
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ViewProgressStat pgrStat = null;
        try {
            stmt = con.prepareStatement(com.cw.wizbank.ae.db.sql.SqlStatements.getModProgressStatSQL(sqlAddCond));
            int index = 1;
            stmt.setString(index++, dbProgress.PGR_STATUS_OK);
            stmt.setLong(index++, mod_id);
            if (ent_id_lst!=null && ent_id_lst.length>0){
                stmt.setBoolean(index++, true);
            }
            if (ugr_ent_id_lst!=null && ugr_ent_id_lst.length>0){
                stmt.setBoolean(index++, true);
            }
            if (start_datetime!=null) {
                stmt.setTimestamp(index++, start_datetime);
            }
            if (end_datetime!=null) {
                stmt.setTimestamp(index++, end_datetime);
            }
            rs = stmt.executeQuery();
            pgrStat = new ViewProgressStat();
            if (rs.next()) {
                pgrStat.total_cnt = rs.getInt("TOTAL_CNT");
                pgrStat.total_score = rs.getFloat("TOTAL_SCORE");
            } else {
                pgrStat.total_cnt = 0;
                pgrStat.total_score = 0;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return pgrStat;
    }

    public static ViewProgressAttemptStat[] getAttemptStat(Connection con, Timestamp start_datetime, Timestamp end_datetime, long mod_id, long[] ent_id_lst, long[] ugr_ent_id_lst,boolean[] course_types) throws SQLException{
        StringBuffer sqlAddCond = null;
        if (ent_id_lst!=null && ent_id_lst.length>0){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND usr_ent_id IN (");
            sqlAddCond.append(com.cw.wizbank.db.sql.SqlStatements.getUserInGroupSQL(ent_id_lst, DbEntityRelation.ERN_TYPE_USR_PARENT_USG));
            sqlAddCond.append(" )");
        }
        if (ugr_ent_id_lst!=null && ugr_ent_id_lst.length>0){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND usr_ent_id IN (");
            sqlAddCond.append(com.cw.wizbank.db.sql.SqlStatements.getUserInGroupSQL(ent_id_lst, DbEntityRelation.ERN_TYPE_USR_CURRENT_UGR));
            sqlAddCond.append(" )");
        }
        if (start_datetime!=null){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND pgr_complete_datetime > ? ");
        }
        if (end_datetime!=null){
            if (sqlAddCond == null) sqlAddCond = new StringBuffer();
            sqlAddCond.append(" AND pgr_complete_datetime < ? ");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
        	boolean answer_for_course = false;
        	boolean answer_for_lrn_course = false;
        	if(course_types != null &&  course_types.length > 1){
        		for(int i=0; i<course_types.length;i++){
        			if(i==0){
        				answer_for_course = course_types[i];
        			}
        			if(i==1){
        				answer_for_lrn_course = course_types[i];
        			}
        		}
        	}
            stmt = con.prepareStatement(com.cw.wizbank.ae.db.sql.SqlStatements.getModAttemptStatSQL(sqlAddCond,answer_for_course,answer_for_lrn_course));
            int index = 1; 
            stmt.setString(index++, dbProgress.PGR_STATUS_OK);
            stmt.setLong(index++, mod_id);
            stmt.setString(index++, dbQuestion.QUE_TYPE_MULTI);
            
            if (ent_id_lst!=null && ent_id_lst.length>0){
                stmt.setBoolean(index++, true);
            }
            if (ugr_ent_id_lst!=null && ugr_ent_id_lst.length>0){
                stmt.setBoolean(index++, true);
            }
            if (start_datetime!=null) {
                stmt.setTimestamp(index++, start_datetime);
            }
            if (end_datetime!=null) {
                stmt.setTimestamp(index++, end_datetime);
            }
            if(!answer_for_course && answer_for_lrn_course) {
            	stmt.setLong(index++, mod_id);
            }
            rs = stmt.executeQuery();        
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewProgressAttemptStat pgrAtmStat = new ViewProgressAttemptStat();
                pgrAtmStat.rcn_order = rs.getInt("rcn_order");
                pgrAtmStat.atm_int_res_id = rs.getLong("atm_int_res_id");
                pgrAtmStat.atm_response_bil = rs.getString("atm_response_bil");
                pgrAtmStat.total_cnt = rs.getInt("R_COUNT");
                pgrAtmStat.total_score = rs.getInt("SUM_SCORE");
                tempResult.addElement(pgrAtmStat);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewProgressAttemptStat result[] = new ViewProgressAttemptStat[tempResult.size()];
        result = (ViewProgressAttemptStat[])tempResult.toArray(result);
        
        return result;            
    }
    
    // search course level item that contains module from module_template, itm_id as key, mod id as value
  
	public static Hashtable searchItmMyModRoot(Connection con, Vector itm_lst_vec, long mod_id,Timestamp start_datetime, Timestamp end_datetime) throws SQLException{
		   Hashtable tempHtResult = new Hashtable();
		   Vector child_mod_id_vector= new Vector();
		   ViewSurveyReport.getChildModleIdList(con,mod_id, child_mod_id_vector, false);
		   //System.out.println(child_mod_id_vector.size());
		   String sql = com.cw.wizbank.ae.db.sql.SqlStatements.sql_get_itm_by_mod_root;		  
		   String TableName = null;
		   if(child_mod_id_vector.size()>0){
			   String ColName = "child_mod_id_ids";
			   TableName = cwSQL.createSimpleTemptable(con, ColName, cwSQL.COL_TYPE_LONG, 0);
				if (TableName != null) {
					cwSQL.insertSimpleTempTable(con, TableName, child_mod_id_vector, cwSQL.COL_TYPE_STRING);
				}
				
			  sql += " AND mod_res_id in (select "+ColName+" from "+TableName+")";  
		   }else{
			   sql += " AND mod_res_id in (0) ";             
		   }
		   PreparedStatement stmt = con.prepareStatement(sql);
		   ResultSet rs = stmt.executeQuery();        
		   while(rs.next()){
			     tempHtResult.put(new Long(rs.getLong("cos_itm_id")), new Long(rs.getLong("mod_res_id")));
		   }
		   stmt.close();
		   if(TableName != null ){
			   cwSQL.dropTempTable(con, TableName);
		   }
		   return ViewSurveyReport.searchResultItm( con, itm_lst_vec, tempHtResult, start_datetime, end_datetime);
	   }
    
	  //get the module's children and put the children's id into Vector child_mod_id_vector if the module is not a leaf node
	   //Can also judge this node whether a leaf node
	 public static boolean  getChildModleIdList(Connection con,long mod_id,Vector child_mod_id_vector,boolean only_leaf) throws SQLException{
		    boolean is_leaf=true;
		    Vector temp_id_vector=new Vector(); //
		    // String  sql_get_child_mod_list_by_root_id=" select mod_res_id from Modle where mod_mod_id_root = ? and mod_type= ?";
		    PreparedStatement stmt = con.prepareStatement(com.cw.wizbank.ae.db.sql.SqlStatements.sql_get_child_mod_list_by_root_id);
		    stmt.setLong(1, mod_id);

		    ResultSet rs = stmt.executeQuery();  
		    while(rs.next()){
		        temp_id_vector.addElement(rs.getString("mod_res_id"));              
		    }
		    stmt.close();
		    if(temp_id_vector.size()>0){
			   is_leaf=false;
			   for(int i=0;i<temp_id_vector.size();i++){
			       if(!ViewSurveyReport.getChildModleIdList(con,Long.parseLong(temp_id_vector.get(i).toString()), child_mod_id_vector, only_leaf)){
				       if(!only_leaf) child_mod_id_vector.addElement(temp_id_vector.get(i).toString());
				   }else{
				        child_mod_id_vector.addElement(temp_id_vector.get(i).toString());
				   }   
			    }
		    }
		   return is_leaf;
	   }  

	public static Hashtable searchResultItm(Connection con, Vector itm_lst_vec, Hashtable tempHtResult,Timestamp start_datetime, Timestamp end_datetime) throws SQLException{
		   Hashtable htResult = new Hashtable();
		   StringBuffer tempSql=new StringBuffer();
		   Enumeration enum_itm = tempHtResult.keys();
		   Vector tempItm_lst_vec=new Vector();
		   while (enum_itm.hasMoreElements()) {
               tempItm_lst_vec.addElement((Long)enum_itm.nextElement());
		   }
		   
		   MYSQLDbHelper mysqlDbHelper = null;
		   boolean isMysql = false;
			if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
				mysqlDbHelper = new MYSQLDbHelper();
				isMysql = true;
			}
		   
		   String c_colName = "c_itm_id";
		   String c_tableName = null;
		   String physicalTableCtableName = null;
		   if(tempItm_lst_vec != null){
			   c_tableName = cwSQL.createSimpleTemptable(con, c_colName, cwSQL.COL_TYPE_LONG, 0);
		       cwSQL.insertSimpleTempTable(con, c_tableName, tempItm_lst_vec, cwSQL.COL_TYPE_LONG);
		       if(isMysql){
		    	   physicalTableCtableName = mysqlDbHelper.tempTable2Physical(con, c_tableName);
				}
		   }
		   
		   String p_colName = "p_itm_id";
		   String p_tableName = null;
		   String physicalTablePtableName = null;
		   if(itm_lst_vec != null){
			   p_tableName = cwSQL.createSimpleTemptable(con, p_colName, cwSQL.COL_TYPE_LONG, 0);
		       cwSQL.insertSimpleTempTable(con, p_tableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);
		       if(isMysql){
		    	   physicalTablePtableName = mysqlDbHelper.tempTable2Physical(con, p_tableName);
				}
		   }
		   
		   StringBuffer sql=new StringBuffer();
		   sql=sql.append("select itm_id from aeItem ");
			if(tempItm_lst_vec != null && c_tableName != null){
		   		sql.append(" inner join "+(isMysql==true?physicalTableCtableName:c_tableName)+" on (itm_id = "+c_colName+")");
		   	}
		    if (itm_lst_vec!=null && p_tableName != null){
			   sql.append(" inner join "+(isMysql==true?physicalTablePtableName:p_tableName)+" on (itm_id = "+p_colName+")");
	         }   
		    sql.append(" where itm_create_run_ind = 0 and itm_run_ind= 0 and itm_apply_ind = 1 ");
		  
	       sql.append(" Union ");
		   sql=sql.append("select itm_id from aeItem ");
		   sql.append(" inner join aeItemRelation on (itm_id = ire_child_itm_id)");
			if (tempItm_lst_vec != null && c_tableName != null) {
				sql.append(" inner join " + (isMysql==true?physicalTableCtableName:c_tableName) + " on (itm_id = " + c_colName + ")");
			}
			if (itm_lst_vec != null && p_tableName != null) {
				sql.append(" inner join " + (isMysql==true?physicalTablePtableName:p_tableName) + " on (ire_parent_itm_id = " + p_colName + ")");
			}
		   sql.append(" where itm_create_run_ind= 0 and itm_run_ind= 1 and itm_apply_ind = 1 ");
		   if (start_datetime != null || end_datetime != null) {		   	    
			    if (start_datetime != null){
					sql.append("  AND itm_eff_start_datetime <= '").append(start_datetime).append("'");	
			    }
			    if (end_datetime != null){
			        sql.append("  AND itm_eff_end_datetime >= '").append(end_datetime).append("'");	
		        }
											
		   }
	
		   PreparedStatement stmt = con.prepareStatement(sql.toString());
		   ResultSet rs = stmt.executeQuery();  
		   while (rs.next()){
			     long itm_id=rs.getLong("itm_id");
			     if(tempHtResult.containsKey(new Long(itm_id))){
					htResult.put(new Long(itm_id),tempHtResult.get(new Long(itm_id)));				
			     }
		   }
		   stmt.close();
		   if(p_tableName != null ){
			   cwSQL.dropTempTable(con, p_tableName);
			   if(isMysql){
					mysqlDbHelper.dropTable(con, physicalTablePtableName);
				}
		   }
		   if(c_tableName != null ){
			   cwSQL.dropTempTable(con, c_tableName);
			   if(isMysql){
					mysqlDbHelper.dropTable(con, physicalTableCtableName);
				}
		   }
		   return htResult;
	   }  
  
 }
