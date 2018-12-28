package com.cw.wizbank.db.view;

import java.io.Serializable;
import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbEntityRelation;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.sql.SqlStatements;

public class ViewModuleEvaluation implements Serializable {

    public String mov_status;
    public Timestamp pgr_complete_datetime;
    public Timestamp mov_update_timestamp;
    public long tkh_id;
    public String usr_id;
    public long usr_ent_id;
    public String usr_display_bil;
    //add By Tim on 15-10-2002
    public String score;
    public String grade;
    public String pgr_status;

    /**
     * Get a vecotr containing users module evaluation record on the specified module
     * if user not attempted to the module, mov_status of the user will be null or N
     * @param mod_id module id
     * @param mov_status a module evaluation status of the records to be get, null for all status
     */
    public Vector getByModuleAsVector(Connection con, long mod_id, cwPagination cwPage, String mov_status, long[] ent_id_lst, long[] ugr_ent_id_lst, Timestamp startDate, Timestamp endDate, boolean byEnrollment)
        throws SQLException, cwException {
        	Vector validAppTkhIds = getValidAppTkhIds(con, mod_id);
            StringBuffer sql = new StringBuffer();
            
            // main query statement
            if (byEnrollment){
                sql.append(OuterJoinSqlStatements.getModuleEvaluationUserListByEnrollmentSQL(con));
            }else{                           
                sql.append(OuterJoinSqlStatements.getModuleEvaluationUserListSQL());
            }
            // additional conditions
            if (mov_status != null && mov_status.length() > 0) {
                if (mov_status.equalsIgnoreCase("N")) {
                    sql.append("AND ( mov_status = ? OR mov_status is null ) ");
                } else {
                    sql.append("AND mov_status = ? ");
                }
            }
            if (ent_id_lst != null && ent_id_lst.length > 0) {
                sql.append(" AND usr_ent_id IN (");
                sql.append(SqlStatements.getUserInGroupSQL(ent_id_lst, DbEntityRelation.ERN_TYPE_USR_PARENT_USG));
                sql.append(" ) ");
            }
            if (ugr_ent_id_lst != null && ugr_ent_id_lst.length > 0) {
                sql.append(" AND usr_ent_id IN (");
                sql.append(SqlStatements.getUserInGroupSQL(ugr_ent_id_lst, DbEntityRelation.ERN_TYPE_USR_CURRENT_UGR));
                sql.append(" ) ");
            }
            if (startDate != null){
                sql.append(" AND pgr_complete_datetime  > ? ");
            }
            if (endDate != null){
                sql.append(" AND pgr_complete_datetime  < ? ");
            }
            sql.append("AND usr_status = 'OK'");
            sql.append("ORDER BY ").append(cwPage.sortCol).append(" ").append(cwPage.sortOrder);
            
            int index = 1;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Vector resultVec = null;
            try {
                stmt = con.prepareStatement(sql.toString());
                stmt.setLong(index++, mod_id);
                if (mov_status != null && mov_status.length() > 0) {
                    stmt.setString(index++, mov_status);
                }
                if (ent_id_lst != null && ent_id_lst.length > 0) {
                    stmt.setBoolean(index++, true);
                }
                if (ugr_ent_id_lst != null && ugr_ent_id_lst.length > 0) {
                    stmt.setBoolean(index++, true);
                }
                if (startDate != null){
                    stmt.setTimestamp(index++, startDate);
                }
                if (endDate != null){
                    stmt.setTimestamp(index++, endDate);
                }            
                rs = stmt.executeQuery();
                
                resultVec = new Vector();
                while (rs.next()) {
                	if(validAppTkhIds.contains(new Long(rs.getLong("tkh_id")))) {
	                    ViewModuleEvaluation viewMov = new ViewModuleEvaluation();
	                    viewMov.usr_ent_id = rs.getLong("usr_ent_id");
	                    viewMov.usr_id = rs.getString("usr_id");
	                    viewMov.usr_display_bil = rs.getString("usr_display_bil");
	                    viewMov.mov_status = rs.getString("mov_status");
	                    viewMov.mov_update_timestamp = rs.getTimestamp("mov_update_timestamp");
	                    viewMov.score = rs.getString("mov_score");
	                    viewMov.tkh_id = rs.getLong("tkh_id");
	                    viewMov.grade = rs.getString("pgr_grade");
	                    viewMov.pgr_status = rs.getString("pgr_status");
	                    viewMov.pgr_complete_datetime = rs.getTimestamp("pgr_complete_datetime");
	                    
						resultVec.addElement(viewMov);
                    }
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
            
            return resultVec;
        }

	public static Vector getValidAppTkhIds(Connection con, long mod_id) throws SQLException {
		Vector validAppTkhIds = new Vector();
		String SQL = " SELECT app_tkh_id FROM resourceContent, course, aeItemRelation, aeApplication " +
			" WHERE rcn_res_id_content = ? " +
			" AND rcn_res_id = cos_res_id " +
			" AND ire_parent_itm_id = cos_itm_id " +
			" AND ire_child_itm_id = app_itm_id " +
			" UNION " +
			" SELECT app_tkh_id FROM resourceContent, course, aeApplication " +
			" WHERE rcn_res_id_content = ? " +
			" AND rcn_res_id = cos_res_id " +
			" AND cos_itm_id = app_itm_id ";
		int index = 1;

		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setLong(index++, mod_id);
		stmt.setLong(index++, mod_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			validAppTkhIds.add(new Long(rs.getLong("app_tkh_id")));
		}
		rs.close();
		stmt.close();
		
		return validAppTkhIds;
	}

    /**
    * Get user count on the module in the specified mov_status
    * @param mod_id module id
    * @param v_mov_status string in vector of the specified mov_status
    */
    public static int getStatusCountByModule(Connection con, long mod_id, Vector v_mov_status)
        throws SQLException {
			Vector validAppTkhIds = getValidAppTkhIds(con, mod_id);
            String SQL = null;
            PreparedStatement stmt = null;
            int index = 1;
            //if( v_mov_status == null || v_mov_status.isEmpty() || v_mov_status.indexOf("N") != -1 ){
/*
                Vector v_itm_id = null;
                long itm_id = getModuleItemId(con, mod_id);
                if( getCreateRunInd(con, itm_id) ) {
                    v_itm_id = getChildItemId(con, itm_id);
                    v_itm_id.addElement(new Long(itm_id));
                }else{
                    v_itm_id = new Vector();
                    v_itm_id.addElement(new Long(itm_id));
                }
*/
                SQL = OuterJoinSqlStatements.getModuleEvaluationUserCountSQL2(/*v_itm_id,*/ v_mov_status);
                stmt = con.prepareStatement(SQL);
                stmt.setLong(index++, mod_id);
                stmt.setLong(index++, mod_id);
                //stmt.setString(index++, "ADMITTED");
                if(v_mov_status != null && !v_mov_status.isEmpty())
                    for(int i=0; i<v_mov_status.size(); i++)
                        stmt.setString(index++, (String)v_mov_status.elementAt(i));
/*
            }else{
                SQL = getModuleEvaluationUserCountSQL(con, v_mov_status);
                stmt = con.prepareStatement(SQL);
                stmt.setLong(index++, mod_id);
                if(v_mov_status != null && !v_mov_status.isEmpty())
                    for(int i=0; i<v_mov_status.size(); i++)
                        stmt.setString(index++, (String)v_mov_status.elementAt(i));
            }
*/
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            //测评结果-评估问卷详细页面统计(已完成、为完成状态的统计)
            while(rs.next()) {
				long tkhId = rs.getLong("tkh_id");
				//过滤掉回收站用户
				String userStatusSql = "select usr_status from RegUser,TrackingHistory where usr_ent_id = tkh_usr_ent_id and tkh_id = ?";
                PreparedStatement stmt_user = con.prepareStatement(userStatusSql);
                stmt_user.setLong(1,tkhId);
                ResultSet rs_user = stmt_user.executeQuery();
                String usr_status = null;
                while (rs_user.next()){
                    usr_status = rs_user.getString("usr_status");
                }
                rs_user.close();
                stmt_user.close();
				if(validAppTkhIds.contains(new Long(tkhId)) && usr_status.equals("OK")) {
					count++;
				}
            }
            stmt.close();
            return count;
        }


/*
    //mov_status cannot be empty
    public static String getModuleEvaluationUserCountSQL(Connection con, Vector mov_status)
        throws SQLException {
            StringBuffer sqlStr = new StringBuffer(512);
            sqlStr.append(" SELECT COUNT(mov_ent_id) ")
                  .append(" FROM ModuleEvaluation ")
                  .append(" WHERE mov_mod_id = ? ")
                  .append(" AND mov_status IN ( ? ");
            for(int i=1; i<mov_status.size(); i++)
                sqlStr.append(", ?");
            sqlStr.append(" ) ");
            return sqlStr.toString();
        }
*/

    private static long getModuleItemId(Connection con, long mod_id)
        throws SQLException {

        String SQL = " SELECT cos_itm_id "
                   + " FROM ResourceContent, Course "
                   + " WHERE rcn_res_id_content = ? "
                   + " AND rcn_res_id = cos_res_id ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, mod_id);
        ResultSet rs = stmt.executeQuery();
        long itm_id = 0;
        if(rs.next())
            itm_id = rs.getLong("cos_itm_id");
        else
            throw new SQLException("Failed to get module item id, mod_id = " + mod_id);
        stmt.close();
        return itm_id;

    }


    private static boolean getCreateRunInd(Connection con, long itm_id) throws SQLException {

        final String sql_get_create_run_ind = "Select itm_create_run_ind from aeItem where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_create_run_ind);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        boolean flag = false;
        if(rs.next()) {
            flag = rs.getBoolean("itm_create_run_ind");
        }
        stmt.close();
        return flag;
    }




    private static Vector getChildItemId(Connection con, long itm_id)
        throws SQLException {
        Vector itemId = new Vector();
        Long childItemId;
        final String GET_CHILD_ITM_ID_SQL = " Select ire_child_itm_id  From aeItemRelation "
                                          + " Where ire_parent_itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(GET_CHILD_ITM_ID_SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            childItemId = new Long(rs.getLong("ire_child_itm_id"));
            itemId.addElement(childItemId);
        }
        stmt.close();
        return itemId;
    }


}