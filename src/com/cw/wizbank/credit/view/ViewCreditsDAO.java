package com.cw.wizbank.credit.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.JsonMod.credit.bean.CreditDetailBean;
import com.cw.wizbank.JsonMod.credit.bean.CreditRankBean;
import com.cw.wizbank.credit.dao.CreditsTypeDAO;
import com.cw.wizbank.credit.db.UserCreditsDB;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author DeanChen
 * 
 */
public class ViewCreditsDAO {

    private static final String GET_CREDITS_BY_ENT_ID = " SELECT SUM(ucd_total) AS credits_total "
            + " FROM creditsType "
            + " INNER JOIN userCreditsDetail ON (cty_id = ucd_cty_id)"
            + " WHERE cty_deleted_ind = ? "
            + "   AND cty_relation_type = ? "
            + "   AND ucd_ent_id = ?  ";

    /**
     * to get total credits of user about know.
     * 
     * @param con
     * @param userEntId
     * @return the total credits of user
     * @throws SQLException
     */
    public static int getCreditsByEntId(Connection con, long userEntId)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int creditsTotal = 0;
        try {
            stmt = con.prepareStatement(GET_CREDITS_BY_ENT_ID);
            int index = 1;
            stmt.setBoolean(index++, false);
            stmt.setString(index++, Credit.CTY_KNOW);
            stmt.setLong(index++, userEntId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                creditsTotal = rs.getInt("credits_total");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return creditsTotal;
    }

    private static final String GET_USER_CREDITS_LIST = "select usr_ent_id, sum(ucd_total) total, max(ucd_update_timestamp) as ucd_update_timestamp from userCreditsDetail, creditsType, reguser "
            + " where ucd_cty_id = cty_id and ucd_ent_id = usr_ent_id and usr_status <> ? "
            + " and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0) and cty_relation_total_ind = 1 "
            + " group by usr_ent_id ";

    /**
     * to get credits of all user.
     * 
     * @param con
     * @return v
     * @throws SQLException
     */
    public static Vector getUserCreditsList(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector userCreditsVec = new Vector();
        try {
            stmt = con.prepareStatement(GET_USER_CREDITS_LIST);
            int index = 1;
            stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);

            Timestamp curTime = cwSQL.getTime(con);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UserCreditsDB userCreditsDb = new UserCreditsDB();
                userCreditsDb.setUct_ent_id(rs.getInt("usr_ent_id"));
                userCreditsDb.setUct_total(rs.getFloat("total"));
                userCreditsDb.setUct_update_timestmp(rs.getTimestamp("ucd_update_timestamp"));
                userCreditsVec.addElement(userCreditsDb);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return userCreditsVec;
    }

    private static final String GET_TOTAL_CREDIT_BY_ENT_ID = "select sum(ucd_total) as usr_total_credits from userCreditsDetail"
            + " inner join creditsType on (ucd_cty_id = cty_id and cty_deleted_ind = ? and cty_relation_type = ? )"
            + " where ucd_ent_id = ? ";
    
    public static float getTotalCreditByEntId(Connection con, int userEntId) throws SQLException {
        float totalCredit = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_TOTAL_CREDIT_BY_ENT_ID);
            int index = 1;
            stmt.setBoolean(index++, false);
            stmt.setString(index++, CreditsTypeDAO.CTY_KNOW);
            stmt.setInt(index++, userEntId);
            rs = stmt.executeQuery();
            if(rs.next()) {
                totalCredit = rs.getFloat("usr_total_credits");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return totalCredit;
    }
    
    public static final String GET_CREDIT_RANK_LST = "select usr_ent_id, usr_ste_usr_id, usr_display_bil, ern_ancestor_ent_id, uct_total, uct_update_timestamp from userCredits "
			+ " inner join regUser on (usr_ent_id = uct_ent_id and usr_status = ?) "
			+ " inner join entityRelation on (ern_child_ent_id = usr_ent_id and ern_type = ? and ern_parent_ind = ?)" 
			+ " order by uct_total desc, usr_ent_id";
    
    public static final String MAP_KEY_LIST = "list";
    public static final String MAP_KEY_TOTAL = "total";
    public static final String MAP_KEY_UPDATE_TIME = "update_time";
    public static Map getCreditRank(Connection con, int start, int pageSize) throws SQLException {
    	Map creditRankMap = new HashMap();
    	Vector creditRankVec = new Vector();
    	int count = 0;
    	Timestamp updateTime = null; 
    	
    	PreparedStatement stmt = null;
        ResultSet rs = null;
long cur_time = 0;
cur_time = System.currentTimeMillis();
        EntityFullPath entityPath = EntityFullPath.getInstance(con);
CommonLog.debug("entityPath: " + (System.currentTimeMillis() - cur_time));
cur_time = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement(GET_CREDIT_RANK_LST);
            int index = 1;
            stmt.setString(index++, dbRegUser.USR_STATUS_OK);
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
            rs = stmt.executeQuery();
            while(rs.next()) {
            	if (count >= start && count < (start + pageSize)) {
            		CreditRankBean rankBean = new CreditRankBean();
    				rankBean.setSort_id(count+1);
    				rankBean.setUsr_ent_id(rs.getLong("usr_ent_id"));
    				rankBean.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
    				rankBean.setUsr_display_bil(rs.getString("usr_display_bil"));
    				long usgEntId = rs.getLong("ern_ancestor_ent_id");
    				rankBean.setUsg_display_bil(entityPath.getEntityName(con, usgEntId));
    				rankBean.setUct_total(rs.getFloat("uct_total"));
    				creditRankVec.add(rankBean);
    				if(updateTime == null) {
    					updateTime = rs.getTimestamp("uct_update_timestamp");
    				}
            	}
            	count++;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
//System.out.println("GET_CREDIT_RANK_LST: " + (System.currentTimeMillis() - cur_time));        
        creditRankMap.put(MAP_KEY_LIST, creditRankVec);
        creditRankMap.put(MAP_KEY_TOTAL, new Integer(count));
        creditRankMap.put(MAP_KEY_UPDATE_TIME, updateTime);
        
    	return creditRankMap;
    }
    
    public static Map getTopCreditRank(Connection con, int top_size) throws SQLException {
    	Map creditRankMap = new HashMap();
    	Vector creditRankVec = new Vector();
    	int count = 1;    	
    	PreparedStatement stmt = null;
        ResultSet rs = null;
long cur_time = 0;
cur_time = System.currentTimeMillis();
		String get_credit_rank_top = " select uct_ent_id, uct_total, usr_display_bil from userCredits, Reguser " 
			+ " where usr_ent_id = uct_ent_id and usr_status = ?"
			+ " order by uct_total desc, usr_display_bil";			
		try {
            stmt = con.prepareStatement(get_credit_rank_top);
            int index = 1;
            stmt.setString(index++, dbRegUser.USR_STATUS_OK);
            rs = stmt.executeQuery();
            while(rs.next()) {
            	if (count <= top_size) {
	            	CreditRankBean rankBean = new CreditRankBean();
					rankBean.setSort_id(count++);
					rankBean.setUsr_ent_id(rs.getLong("uct_ent_id"));
					rankBean.setUsr_display_bil(dbRegUser.getDisplayBil(con, rankBean.getUsr_ent_id()));
					rankBean.setUct_total(rs.getFloat("uct_total"));
					creditRankVec.add(rankBean);
            	}else{
            		break;
            	}
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
//System.out.println("GET_CREDIT_RANK_LST: " + (System.currentTimeMillis() - cur_time));        
        creditRankMap.put(MAP_KEY_LIST, creditRankVec);        
    	return creditRankMap;
    }
    
    public static final String MAP_KEY_MY_CREDIT = "my_credit";
    public static final String MAP_KEY_MY_CREDIT_SORT = "my_credit_sort";
    public static Map getMyCreditDetail(Connection con, long usrEntId) throws SQLException {
    	Map myCreditRankMap = new HashMap();
    	int myCreditSort = 0; 
    	
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_CREDIT_RANK_LST);
            int index = 1;
            stmt.setString(index++, dbRegUser.USR_STATUS_OK);
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
            rs = stmt.executeQuery();
            while(rs.next()) {
            	myCreditSort++;
         		if(rs.getLong("usr_ent_id") == usrEntId){
         			myCreditRankMap.put(MAP_KEY_MY_CREDIT, new Float(rs.getInt("uct_total")));
         			myCreditRankMap.put(MAP_KEY_MY_CREDIT_SORT, new Integer(myCreditSort));
         			break;
         		}
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return myCreditRankMap;
    }
    
    public static Map getMyCreditHistory(Connection con, long usrEntId, int start, int pageSize) throws SQLException {
    	Map creditDetailMap = new HashMap();
    	Vector creditHistVec = new Vector();
    	int count = 0;
    	String sql = "select cty_code, ucl_point,cty_manual_ind,cty_deduction_ind, ucl_relation_type,itm_title,ucl_create_timestamp from userCreditsDetailLog " + 
    				" inner join reguser on (ucl_usr_ent_id = usr_ent_id and usr_ent_id = ?) " + 
    				" inner join creditsType on (cty_id = ucl_bpt_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0)) " + 
    				" left join aeItem on (itm_id = ucl_source_id and ucl_relation_type = ?)" +
    				" order by ucl_create_timestamp desc";
    	
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, usrEntId);
            stmt.setString(index++, Credit.CTY_COURSE);
            rs = stmt.executeQuery();
            
            while(rs.next()) {
            	if(start <= count && count < start + pageSize) {
	            	CreditDetailBean creditBean = new CreditDetailBean();
	            	creditBean.setCty_code(rs.getString("cty_code"));
	            	creditBean.setCty_deduction_ind(rs.getBoolean("cty_deduction_ind"));
	            	creditBean.setCty_manual_ind(rs.getBoolean("cty_manual_ind"));
	            	creditBean.setSource(rs.getString("itm_title"));
	            	String type = rs.getString("ucl_relation_type");
	            	if(Credit.CTY_COURSE.equalsIgnoreCase(type)) {
	            		creditBean.setType(Credit.TRAINNING_SCORE);
	            	} else if(Credit.INTEGRAL_EMPTY.equals(rs.getString("cty_code"))) {
						creditBean.setType(Credit.EMPTY_SCORE);
					} else {
	            		creditBean.setType(Credit.ACTIVITY_SCORE);
	            	}
            		creditBean.setUcl_create_timestamp(rs.getTimestamp("ucl_create_timestamp"));
            		creditBean.setUcl_point(cwUtils.roundingFloat(rs.getFloat("ucl_point"),2));
            		creditHistVec.add(creditBean);
            	}
            	count++;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        creditDetailMap.put(MAP_KEY_LIST, creditHistVec);
        creditDetailMap.put(MAP_KEY_TOTAL, new Integer(count));
        
    	return creditDetailMap;
    }
    
    public static String MAP_KEY_TRAINNING_SCORE = "trainning_score";
    public static String MAP_KEY_ACTIVITY_SCORE = "activity_score";
    public static Map getCreditTotalByType(Connection con, long userEntId) throws SQLException {
    	Map histMap = new HashMap();
    	float trainingScore = 0;
    	float activityScore = 0;
    	String sql = "select ucl_relation_type, sum(ucl_point) total from userCreditsDetailLog" +
    				" inner join creditsType on (cty_id = ucl_bpt_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))" + 
    				" where ucl_usr_ent_id = ? group by ucl_relation_type";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, userEntId);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	if(Credit.CTY_COURSE.equals(rs.getString("ucl_relation_type"))) {
            		trainingScore += cwUtils.roundingFloat(rs.getFloat("total"), 2);;
            	} else {
            		activityScore += cwUtils.roundingFloat(rs.getFloat("total"), 2);;
            	}
            }
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
        histMap.put(MAP_KEY_TRAINNING_SCORE, new Float(trainingScore));
        histMap.put(MAP_KEY_ACTIVITY_SCORE, new Float(activityScore));
        return histMap;
    }
    
	/**
	 * to get credits of all user.
	 * 
	 * @param con
	 * @return v
	 * @throws SQLException
	 */
	public static float getUserCredits(Connection con,long ent_id,String type) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		float credits =0;
		String sql="select usr_ent_id, sum(ucd_total) total from userCreditsDetail, creditsType, reguser ";
		sql+="where ucd_cty_id = cty_id and ucd_ent_id = usr_ent_id and usr_status <> ? ";
		sql+=" and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0) and cty_relation_total_ind = 1 and usr_ent_id=?";
		//活动积分
		if(null != type && type.equals("activity")){
			sql+=" and (cty_relation_type != 'COS' OR cty_relation_type is NULL)";
		}else if(null != type && type.equals("train")){ //培训积分
			sql+=" and cty_relation_type = 'COS'";
		}
		sql+=" group by usr_ent_id ";
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
			stmt.setLong(index++, ent_id);
			Timestamp curTime = cwSQL.getTime(con);
			rs = stmt.executeQuery();
			while (rs.next()) {
				credits = rs.getFloat("total");
			}
		} finally {
			if (stmt != null) {
				cwSQL.cleanUp(rs, stmt);
			}
		}
		return credits;
	}
    
}
