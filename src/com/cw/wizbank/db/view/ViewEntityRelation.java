package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.db.DbIndustryCode;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
view the relation of Entity--EntityRelation--Entity
*/
public class ViewEntityRelation {
    
    
    public static final String GPM_TYPE_USR_PARENT_USG   = dbEntityRelation.ERN_TYPE_USR_PARENT_USG  ;
    public static final String GPM_TYPE_USG_PARENT_USG   = dbEntityRelation.ERN_TYPE_USG_PARENT_USG  ;
    public static final String GPM_TYPE_USR_CURRENT_UGR  = dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR ;
    public static final String GPM_TYPE_UGR_PARENT_UGR   = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR  ;
    public static final String GPM_TYPE_IDC_PARENT_IDC   = dbEntityRelation.ERN_TYPE_IDC_PARENT_IDC  ;
    public static final String GPM_TYPE_USR_INTEREST_IDC = dbEntityRelation.ERN_TYPE_USR_INTEREST_IDC;
    public static final String GPM_TYPE_USR_FOCUS_IDC    = dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC   ;

    public String groupName;
    
    public long groupEntId;
    
    public String groupEntType;
    
    // for usg role
    public String groupRole;
    
    public String memberName;
    
    public long memberEntId;
    
    public String memberEntType;
    
    public long rootEntId;
    
    public String relationType;
    
    public Timestamp synDate;
    
    public String ancestorList;

    private String userRelation;
    
    private String memberRelation;

    /*
    public static final long ALL_MEMBERS = 0;
    
    public static final long ALL_GROUPS = 0;
    
    public static final String ALL_TYPES = "ENR_ALL_TYPES";
    */
    
    /**
    searching depth in getting tree of entities according to EntityRelation
    */
    public long depth;

    //Industry Code related methods

    
    /**
    delete all records in EntityRelation based on memberEntId and the parent entity is an Industry Code<BR>
    pre-define variable:<BR>
    <ul>
    <li>memberEntId
    <li>relationType (can be null)
    </ul>
    */
    public void clearUserIndustryCode(Connection con, String user_id) throws SQLException {
        dbEntityRelation dbEr = new dbEntityRelation();
        dbEr.ern_child_ent_id = this.memberEntId;
        dbEr.ern_type = this.relationType;
        dbEr.delAsChild(con, user_id, null);
        return;
    }
    
    /**
    get a Vector of member industry codes <BR>
    pre-define variable: 
    <ul>
    <li>groupEntId
    </ul>
    */
    public Vector getMemberIndustryCode(Connection con)
        throws SQLException {
    
        Vector v = new Vector();
        DbIndustryCode dbIdc;
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(" Select idc_ent_id, idc_display_bil, idc_ent_id_root ");
        SQLBuf.append(" From IndustryCode, EntityRelation ");
        SQLBuf.append(" Where idc_ent_id = ern_child_ent_id ");
        SQLBuf.append(" And ern_ancestor_ent_id = ? ");
        SQLBuf.append(" AND ern_parent_ind = ? ");
        SQLBuf.append(" Order by idc_display_bil asc ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, groupEntId);
        stmt.setBoolean(index++, true);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            dbIdc = new DbIndustryCode();
            dbIdc.idc_ent_id = rs.getLong("idc_ent_id");
            dbIdc.idc_display_bil = rs.getString("idc_display_bil");
            dbIdc.idc_ent_id_root = rs.getLong("idc_ent_id_root");
            v.addElement(dbIdc);
        }
        stmt.close();
        return v;
    }
    /**
    insert a record into EntityRelation<BR>
    pre-define variable:<BR>
    <ul>
    <li>memberEntId</li>
    <li>groupEntId</li>
    <li>relationType</li> 
    </ul>
    */
    public void insEntityRelation(Connection con, String usr_id) throws SQLException {
        
        dbEntityRelation dbEr = new dbEntityRelation();
        dbEr.ern_child_ent_id = this.memberEntId;
        dbEr.ern_ancestor_ent_id = this.groupEntId;
        dbEr.ern_type = this.relationType;
        dbEr.ern_syn_timestamp = null;
        dbEr.insEr(con, usr_id);
        return;
        /*
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_groupmember);
        int index = 1;
        stmt.setLong(index++, this.groupEntId);
        stmt.setLong(index++, this.memberEntId);
        stmt.setString(index++, this.relationType);
        stmt.setTimestamp(index++, this.synDate);
        stmt.executeUpdate();
        stmt.close();
        return;
        */
    }
    
    /**
    check if the relation exist in database<BR>
    pre-define variables:<BR>
    <ul>
    <li>groupEntId</li>
    <li>memberEntId</li>
    <li>relationType</li>
    </ul>
    */
    public boolean isRelationExist(Connection con) throws SQLException {
        
    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_child_ent_id = this.memberEntId;
    	dbEr.ern_ancestor_ent_id = this.groupEntId;
    	dbEr.ern_type = this.relationType;
        return dbEr.checkExist(con);
        
        /*
        boolean result;        
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_check_groupmember);
        int index = 1;
        stmt.setLong(index++, this.groupEntId);
        stmt.setLong(index++, this.memberEntId);
        stmt.setString(index++, this.relationType);
        stmt.setTimestamp(index++, curTime);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            result = true;
        }
        else {
            result = false;
        }
        stmt.close();
        return result;
        */
    }

    /**
    check if an entity is other's parent in EntityRelation<BR>
    pre-define variables:<BR>
    <ul>
    <li>groupEntId
    </ul>
    */
    public boolean hasMember(Connection con) throws SQLException {
        
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(" Select ern_ancestor_ent_id from EntityRelation ");
        SQLBuf.append(" Where ern_ancestor_ent_id = ? ");
        SQLBuf.append(" AND ern_parent_ind = ? ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, groupEntId);
        stmt.setBoolean(index++, true);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            result = true;
        }
        else {
            result = false;
        }
        stmt.close();
        return result;
    }
    
    /**
    get a vector of parent IndustyCode ViewEntityRelation of a given ent_id<BR>
    pre-define variables:<BR>
    <ul>
    <li>memberEntId
    </ul>
    */
    public Vector getParentIndustryCode(Connection con) throws SQLException {
        
        ViewEntityRelation vienr;
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(" Select idc_ent_id, idc_display_bil, idc_ent_id_root, ern_type ");
        SQLBuf.append(" From IndustryCode, EntityRelation ");
        SQLBuf.append(" Where idc_ent_id = ern_ancestor_ent_id ");
        SQLBuf.append(" And ern_child_ent_id = ? ");
        SQLBuf.append(" And ern_parent_ind = ? ");
        SQLBuf.append(" Order By ern_type asc, idc_display_bil asc ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, memberEntId);
        stmt.setBoolean(index++, true);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            
            vienr = new ViewEntityRelation();
            vienr.groupEntId = rs.getLong("idc_ent_id");
            vienr.groupName = rs.getString("idc_display_bil");
            vienr.rootEntId = rs.getLong("idc_ent_id_root");
            vienr.relationType = rs.getString("ern_type");
            vienr.memberEntId = memberEntId;
            v.add(vienr);
        }
        stmt.close();
        return v;
    }

    /**
    get a vector of parent UserGrade ViewEntityRelation of a given ent_id<BR>
    pre-define variables:<BR>
    <ul>
    <li>memberEntId
    </ul>
    */
    public Vector getParentUserGrade(Connection con) throws SQLException {
        
        ViewEntityRelation vienr;
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(" Select ugr_ent_id, ugr_display_bil, ugr_ent_id_root, ern_type ");
        SQLBuf.append(" From UserGrade, EntityRelation ");
        SQLBuf.append(" Where ugr_ent_id = ern_ancestor_ent_id ");
        SQLBuf.append(" And ern_child_ent_id = ? ");
        SQLBuf.append(" And ern_parent_ind = ? ");
        SQLBuf.append(" Order By ern_type asc, ugr_display_bil asc ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, memberEntId);
        stmt.setBoolean(index++, true);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            
            vienr = new ViewEntityRelation();
            vienr.groupEntId = rs.getLong("ugr_ent_id");
            vienr.groupName = rs.getString("ugr_display_bil");
            vienr.rootEntId = rs.getLong("ugr_ent_id_root");
            vienr.relationType = rs.getString("ern_type");
            vienr.memberEntId = memberEntId;
            v.add(vienr);
        }
        stmt.close();
        return v;
    }

    /**
    get a vector of parent UserClassification ViewEntityRelation with the defined group type of a given ent_id<BR>
    pre-define variables:<BR>
    <ul>
    <li>memberEntId, GroupEntType
    </ul>
    */
    public Vector getParentUserClassification(Connection con) throws SQLException {
        
        ViewEntityRelation vienr;
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(" Select ucf_ent_id, ucf_display_bil, ucf_ent_id_root, ern_type ");
        SQLBuf.append(" From UserClassification, EntityRelation, Entity ");
        SQLBuf.append(" Where ");
        SQLBuf.append(" ern_child_ent_id = ? ");
        SQLBuf.append(" ern_parent_ind = ? ");
        SQLBuf.append(" And ent_type = ? ");
        SQLBuf.append(" AND ent_delete_usr_id IS NULL ");
        SQLBuf.append(" AND ent_delete_timestamp IS NULL ");
        SQLBuf.append(" And ern_ancestor_ent_id = ent_id ");
        SQLBuf.append(" And ucf_ent_id = ern_ancestor_ent_id ");
        SQLBuf.append(" Order By ern_type asc, ucf_seq_no asc ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, memberEntId);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, groupEntType);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            vienr = new ViewEntityRelation();
            vienr.groupEntId = rs.getLong("ucf_ent_id");
            vienr.groupName = rs.getString("ucf_display_bil");
            vienr.rootEntId = rs.getLong("ucf_ent_id_root");
            vienr.relationType = rs.getString("ern_type");
            vienr.memberEntId = memberEntId;
            v.add(vienr);
        }
        stmt.close();
        return v;
    }

    /**
    get a vector of parent UserGroup ViewEntityRelation of a given ent_id<BR>
    pre-define variables:<BR>
    <ul>
    <li>memberEntId
    </ul>
    */
    public Vector getParentUserGroup(Connection con) throws SQLException {
        
        ViewEntityRelation vienr;
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(" Select usg_ent_id, usg_display_bil, usg_role, usg_ent_id_root, ern_type ");
        SQLBuf.append(" From UserGroup, EntityRelation, regUser ");
        SQLBuf.append(" Where usg_ent_id = ern_ancestor_ent_id ");
        SQLBuf.append(" And usr_ent_id = ern_child_ent_id ");
        SQLBuf.append(" And ern_child_ent_id = ? ");
        SQLBuf.append(" And ern_parent_ind = ? ");
        SQLBuf.append(" Order By ern_type asc, usg_display_bil asc ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, memberEntId);
        stmt.setBoolean(index++, true);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            
            vienr = new ViewEntityRelation();
            vienr.groupEntId = rs.getLong("usg_ent_id");
            vienr.groupName = rs.getString("usg_display_bil");
            vienr.groupRole = rs.getString("usg_role");
            vienr.rootEntId = rs.getLong("usg_ent_id_root");
            vienr.relationType = rs.getString("ern_type");
            vienr.memberEntId = memberEntId;
            v.add(vienr);
        }
        stmt.close();
        return v;
    }
    
    /**
     * 获取多个用户组下面的所有用户ID
     * @param con
     * @param usgEntIdVec
     * @param containDeletedUser 是否包括已删除的用户
     * @return
     * @throws SQLException
     */
    public static Vector getUserIdsByUsgEntIds(Connection con, long[] usgEntIds, boolean containDeletedUser, boolean is_detail) throws SQLException {
    	Vector userIdVec = new Vector();
    	
    	String usgEntIdsSql = null;
    	String tableName = null;
    	boolean isMysql = false;
    	String physicalTableName = null;
    	MYSQLDbHelper mysqlDbHelper = null;
    	if(usgEntIds != null && usgEntIds.length > 0) {
    		if(is_detail) {
	    		tableName = cwSQL.createSimpleTemptable(con, "tmp_usg_ent_id", cwSQL.COL_TYPE_LONG, 0);
				cwSQL.insertSimpleTempTable(con, tableName, cwUtils.long2vector(usgEntIds), cwSQL.COL_TYPE_LONG);
				
		 			if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
		 				mysqlDbHelper = new MYSQLDbHelper();
		 				isMysql = true;
		 			}
		 			if(isMysql){
		 				physicalTableName = mysqlDbHelper.tempTable2Physical(con, tableName);
					}
				
				usgEntIdsSql = "( select tmp_usg_ent_id from " + (isMysql==true?physicalTableName:tableName) + " )";
    		} else {
    			usgEntIdsSql = cwUtils.array2list(usgEntIds);
    		}
    	}
    	String sql = "select distinct ern_child_ent_id child_id from EntityRelation where ern_type=?";
    	if(usgEntIdsSql != null) {
    		sql += " and ern_ancestor_ent_id in " + usgEntIdsSql;
    	}
    	if(containDeletedUser) {
    		sql += " union"
    			+  " select erh_child_ent_id child_id from EntityRelationHistory "
    			+  " inner join entity on (ent_id = erh_child_ent_id and erh_end_timestamp = ent_delete_timestamp)"
    			+  " where erh_type = ?";
    		if(usgEntIdsSql != null) {
        		sql += " and erh_ancestor_ent_id in " + usgEntIdsSql;
        	}
    	}
    	
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		int index = 1;
    		stmt.setString(index++, GPM_TYPE_USR_PARENT_USG);
    		if(containDeletedUser) { 
    			stmt.setString(index++, GPM_TYPE_USR_PARENT_USG);
    		}
    		rs = stmt.executeQuery();
    		while(rs.next()) {
    			userIdVec.addElement(new Long(rs.getLong("child_id")));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    		if (tableName != null) {
                cwSQL.dropTempTable(con, tableName);
                if(isMysql){
					mysqlDbHelper.dropTable(con, physicalTableName);
				}
            }
    	}
    	
    	return userIdVec;
    }
    
    /**
     * 判断目标学员是否该培训管理员所负责的学员
     * @param usr_ent_id 培训管理员id
     * @param target_ent_id 目标学员id
     * @throws SQLException
     */
    public static boolean isChargeUser(Connection con, long usr_ent_id, long target_ent_id) throws SQLException {
    	boolean isChargeUser = false;
    	String sql = " SELECT ern_child_ent_id "
    		       + " FROM tcTrainingCenterOfficer "
    		       + " INNER JOIN tcTrainingCenterTargetEntity ON (tce_tcr_id = tco_tcr_id) "
    		       + " INNER JOIN entityRelation ON (tce_ent_id = ern_ancestor_ent_id AND ern_child_ent_id = ?) "
    		       + " WHERE tco_usr_ent_id = ? ";
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		int index = 1;
    		stmt.setLong(index++, target_ent_id);
    		stmt.setLong(index++, usr_ent_id);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			isChargeUser = true;
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	
    	return isChargeUser;
    }
}