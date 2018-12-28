/*
 * Created on 2003/5/19
 */
package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 *
 */
public class DbCmSkillBase {
	
	/* Constant String */
	public static final String COMPETENCY_GROUP = "GROUP";
	public static final String COMPETENCY_COMPOSITE_SKILL = "COMPOSITE_SKILL";
	public static final String COMPETENCY_SKILL = "SKILL";
	/*Class instance*/
	public DbCmSkillScale cmScale = null;
	
	/* Database Field */
	public long skb_id;
	public String skb_type;
	public String skb_title;
	public String skb_description;
	public long skb_owner_ent_id;
	public long skb_parent_skb_id;
	public String skb_ancestor;
	public long skb_ssl_id;
	public long skb_order;
	public String skb_create_usr_id;
	public Timestamp skb_create_timestamp;
	public String skb_update_usr_id;
	public Timestamp skb_update_timestamp;
	public String skb_delete_usr_id;
	public Timestamp skb_delete_timestamp;
	public long skb_ske_id;
	
	/**
	 * Insert skill base record into database
	 * @param con database connection
	 * @param dbSkillBase  
	 * @return row count
	 * @throws SQLException
	 * @throws cwException
	 */
	public static int ins(Connection con, DbCmSkillBase dbSkillBase) 
		throws SQLException, cwException {
			
			if( dbSkillBase.skb_create_timestamp == null || dbSkillBase.skb_update_timestamp == null){
				dbSkillBase.skb_create_timestamp = cwSQL.getTime(con);
				dbSkillBase.skb_update_timestamp = dbSkillBase.skb_create_timestamp;
			}
			
			if( dbSkillBase.skb_update_usr_id == null ){
				dbSkillBase.skb_update_usr_id = dbSkillBase.skb_create_usr_id;
			}
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_INS, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, dbSkillBase.skb_type);
			
			if( dbSkillBase.skb_title != null ) {
				stmt.setString(2, dbSkillBase.skb_title);				
			} else {
				stmt.setNull(2, java.sql.Types.VARCHAR);
			}
			
			if( dbSkillBase.skb_description != null ){
				stmt.setString(3, dbSkillBase.skb_description);				
			} else {
				stmt.setNull(3, java.sql.Types.VARCHAR);
			}
			
			stmt.setLong(4, dbSkillBase.skb_owner_ent_id);
			
			if( dbSkillBase.skb_parent_skb_id > 0 ){
				stmt.setLong(5, dbSkillBase.skb_parent_skb_id);				
			} else {
				stmt.setNull(5, java.sql.Types.INTEGER);
			}
			
			if( dbSkillBase.skb_ancestor != null ){
				stmt.setString(6, dbSkillBase.skb_ancestor);
			} else {
				stmt.setNull(6, java.sql.Types.VARCHAR);
			}
			
			stmt.setLong(7, dbSkillBase.skb_ssl_id);
			
			if( dbSkillBase.skb_order > 0 ){
				stmt.setLong(8, dbSkillBase.skb_order);				
			} else {
				stmt.setNull(8, java.sql.Types.INTEGER);
			}
			
			stmt.setString(9, dbSkillBase.skb_create_usr_id);
			
			stmt.setTimestamp(10, dbSkillBase.skb_create_timestamp);
			
			stmt.setString(11, dbSkillBase.skb_update_usr_id);
			
			stmt.setTimestamp(12, dbSkillBase.skb_update_timestamp);
			
			stmt.setNull(13, java.sql.Types.VARCHAR);
			
			stmt.setNull(14, java.sql.Types.TIMESTAMP);
			stmt.setLong(15, dbSkillBase.skb_ske_id);
			
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ){
				stmt.close();
				throw new cwException("Failed to insert a skill base");
			}
			dbSkillBase.skb_id = cwSQL.getAutoId(con, stmt, "cmSkillBase", "skb_id");
			stmt.close();
			return count;
		}
	
	/**
	 * Update skill base database record
	 * @param con database connection
	 * @return row count
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public int upd(Connection con)
		throws SQLException, cwSysMessage {

			if( this.skb_update_timestamp == null ) {
				this.skb_update_timestamp = cwSQL.getTime(con);
			}
			int index = 1;
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_UPD);
			stmt.setString(index++, this.skb_type);
			if( this.skb_title != null ) {
				stmt.setString(index++, this.skb_title);
			} else {
				stmt.setNull(index++, java.sql.Types.VARCHAR);
			}
			
			if( this.skb_description != null ){
				stmt.setString(index++, this.skb_description);
			} else {
				stmt.setNull(index++, java.sql.Types.VARCHAR);
			}
			
			stmt.setLong(index++, this.skb_owner_ent_id);
			
			if( this.skb_parent_skb_id > 0 ) {
				stmt.setLong(index++, this.skb_parent_skb_id);	
			} else {
				stmt.setNull(index++, java.sql.Types.INTEGER);
			}
			
			if( this.skb_ancestor != null ) {
				stmt.setString(index++, this.skb_ancestor);				
			} else {
				stmt.setNull(index++, java.sql.Types.VARCHAR);
			}
			
			stmt.setLong(index++, this.skb_ssl_id);
			
			if( this.skb_order > 0 ){
				stmt.setLong(index++, this.skb_order);
			} else {
				stmt.setNull(index++, java.sql.Types.INTEGER);
			}
			
			stmt.setString(index++, this.skb_update_usr_id);
			stmt.setTimestamp(index++, this.skb_update_timestamp);
			stmt.setLong(index++, this.skb_id);
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ){
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill base id = " + this.skb_id);
			}
			stmt.close();
			return count;
		}
	
	
	
	

	
	/**
	 * Populate the data from database to DbCmSkillBase object
	 * @param con database connection
	 * @param bLoadScale Load the skill base selected scale
	 * @throws SQLException
	 * @throws cwException
	 */
	public void get(Connection con, boolean bLoadScale)
		throws SQLException, cwSysMessage {
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_ALL);
			stmt.setLong(1, this.skb_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				this.skb_type = rs.getString("skb_type");
				this.skb_title = rs.getString("skb_title");
				this.skb_description = rs.getString("skb_description");
				this.skb_owner_ent_id = rs.getLong("skb_owner_ent_id");
				this.skb_parent_skb_id = rs.getLong("skb_parent_skb_id");
				this.skb_ancestor = rs.getString("skb_ancestor");
				this.skb_ssl_id = rs.getLong("skb_ssl_id");
				this.skb_create_usr_id = rs.getString("skb_create_usr_id");
				this.skb_create_timestamp = rs.getTimestamp("skb_create_timestamp");
				this.skb_update_usr_id = rs.getString("skb_update_usr_id");
				this.skb_update_timestamp = rs.getTimestamp("skb_update_timestamp");
				this.skb_delete_usr_id = rs.getString("skb_delete_usr_id");
				this.skb_delete_timestamp = rs.getTimestamp("skb_delete_timestamp");
			} else {
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill base id = " + this.skb_id);
			}
			stmt.close();
			
			if( bLoadScale ){
				this.cmScale = new DbCmSkillScale();
				this.cmScale.ssl_id = this.skb_ssl_id;
				this.cmScale.get(con);
			}			
			return;
		}
	
	
	/**
	 * Get the skill base ancester
	 * @param con database connection
	 * @param skb_id Skill base id
	 * @return ancestor list with comma as delimiter
	 * @throws SQLException
	 */
	public static String getAncestor(Connection con, long skb_id)
		throws SQLException {
			
			String ancestor = null;
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_ANCESTOR);
			stmt.setLong(1, skb_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				ancestor = rs.getString("skb_ancestor");
			}
			stmt.close();
			return ancestor;
		}
	
	/**
	 * Update the ancestor
	 * @param con database connection
	 * @param skb_id skill base id
	 * @param skb_ancestor skill ancestor list
	 * @throws SQLException
	 * @throws cwException
	 */
	public static void updAncestor(Connection con, long skb_id, String skb_ancestor)
		throws SQLException, cwException {
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_UPDATE_ANCESTOR);
			stmt.setString(1, skb_ancestor);
			stmt.setLong(2, skb_id);
			if( stmt.executeUpdate() != 1 ){
				stmt.close();
				throw new cwException("Failed to update ancestor");
			}
			stmt.close();
			return;
		}
	
	/**
	 * Get the child skill base id with existence status.
	 * @param con
	 * @param skb_id
	 * @return Hasttable of id as key, status as value(false if soft-deleted)
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public static Hashtable getChildNStatusHash(Connection con, long skb_id)
		throws SQLException{
			
			Hashtable h_skb_id_status = new Hashtable();
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_CHILD_ID);
			stmt.setLong(1, skb_id);
			ResultSet rs = stmt.executeQuery();
			boolean flag;
			while(rs.next()){
				//if skb_delete_usr_id not null, the skill base is soft-deleted
				if( rs.getString("skb_delete_usr_id") != null ){
					flag = false;
				} else {
					flag = true;
				}
				h_skb_id_status.put(new Long(rs.getLong("skb_id")), new Boolean(flag));
			}
			stmt.close();
			return h_skb_id_status;
		}
	
	
	public int softDelChild(Connection con)
		throws SQLException, cwSysMessage {
			
			
			if( this.skb_delete_timestamp == null ){
				this.skb_delete_timestamp = cwSQL.getTime(con);
			}
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_SOFT_DELETE_CHILD);
			int index = 1;
			stmt.setString(index++, this.skb_delete_usr_id);
			stmt.setTimestamp(index++, this.skb_delete_timestamp);
			stmt.setNull(index++, java.sql.Types.INTEGER);
			stmt.setLong(index++, this.skb_id);
			int count = stmt.executeUpdate();
			stmt.close();
			return count; 
		}
	
	/**
	 * Set delete user id and timestamp
	 * @param con database connection
	 * @return row count
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public int softDel(Connection con)
		throws SQLException, cwSysMessage{
			if( this.skb_delete_timestamp == null ){
				this.skb_delete_timestamp = cwSQL.getTime(con);
			}
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_SOFT_DEL);
			stmt.setString(1, this.skb_delete_usr_id);
			stmt.setTimestamp(2, this.skb_delete_timestamp);
			stmt.setNull(3, java.sql.Types.INTEGER);
			stmt.setLong(4, this.skb_id);
			
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ) {
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill base id = " + this.skb_id);
			}
			stmt.close();
			return count;
		}
	/**
	 * Physically delete the record from database
	 * @param con database connection
	 * @return row count
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public int del(Connection con)
		throws SQLException, cwSysMessage{
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_DEL);
			stmt.setLong(1, this.skb_id);
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ) {
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill base id = " + this.skb_id);
			}
			stmt.close();
			return count;
		}
	
	/**
	 * Get the scale id assigned to the skill base
	 * @param con database connection
	 * @param skb_id skill base id
	 * @return scale id
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public long getScaleId(Connection con)
		throws SQLException, cwSysMessage{
			
			this.skb_ssl_id = DbCmSkillBase.getScaleId(con, this.skb_id);
			return this.skb_ssl_id; 
		}

	public static long getScaleId(Connection con, long skb_id)
		throws SQLException, cwSysMessage{
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_SCALE_ID);
			stmt.setLong(1, skb_id);
			ResultSet rs = stmt.executeQuery();
			long ssl_id;
			if(rs.next()){
				ssl_id = rs.getLong("skb_ssl_id");
			} else {
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill base id = " + skb_id);
			}
			stmt.close();
			return ssl_id;
		}
	
	/**
	 * Check the scale is used by undeleted skill base 
	 * @param con database connection
	 * @param ssl_id scale id
	 * @return ture if scale is used by skill base 
	 * @throws SQLException
	 * @throws cwException
	 */
	public static boolean isUsedScale(Connection con, long ssl_id)
		throws SQLException, cwException {
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_SKILL_ID_BY_SCALE_ID);
			stmt.setLong(1, ssl_id);
			ResultSet rs = stmt.executeQuery();
			boolean flag = false;
			if(rs.next()){
				flag = true;
			}
			stmt.close();
			return flag;
		}

	/**
	 * Check the scale is used by soft-deleted skill base
	 * @param con database connection
	 * @param scale_id scale id
	 * @return true if scale is used by soft-deleted base
	 * @throws SQLException
	 * @throws cwException
	 */
	public static boolean isScaleUsedByDeletedSkill(Connection con, long ssl_id)
		throws SQLException, cwException {
			
			boolean flag = false;
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_SOFT_DELETED_SKILL_ID_BY_SCALE_ID);
			stmt.setLong(1, ssl_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				flag = true;
			}
			stmt.close();
			return flag;
		}

	/**
	 * Get the result set of skill base ordered by skb_title
	 * @param con database connection
	 * @param sort_order sort order of the result set (asc|desc)
	 * @param v_skb_id Skill base id to be selected in the database
	 * @return result set of the skill base
	 * @throws SQLException
	 */
	public static ResultSet getByIds(Connection con, String sort_order, Vector v_skb_id)
		throws SQLException {
        
        if( sort_order == null || sort_order.length() == 0 ){
        	sort_order = "asc";
        }
		StringBuffer SQLBuf = new StringBuffer();
		SQLBuf.append(SqlStatements.SQL_CM_SKILL_BASE_GET_BY_ID_VEC)
			  .append(cwUtils.vector2list(v_skb_id))
			  .append(" ORDER BY skb_title ")
			  .append(sort_order);
			          
		PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
		ResultSet rs = stmt.executeQuery();
		return rs;
		
	}

	/**
	 * Get the skill base id and title
	 * @param con database connection 
	 * @param v_skb_id Vector of skill base id to be selected from database
	 * @return Return a hashtable of skill base id as key and skill base title as the value
	 * @throws SQLException
	 */
	public static Hashtable getSkillsTitle(Connection con, Vector v_skb_id)
		throws SQLException {
			
			Hashtable h_skb_id_title = new Hashtable();
			if( v_skb_id != null && !v_skb_id.isEmpty() ){
				PreparedStatement stmt = con.prepareStatement(
								SqlStatements.SQL_CM_SKILL_BASE_GET_SKILL_LIST_TITLE 
								+ cwUtils.vector2list(v_skb_id));
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					h_skb_id_title.put(new Long(rs.getLong("skb_id")), rs.getString("skb_title"));
				}
				stmt.close();
			}
			return h_skb_id_title;
		}

	
	/**
	 * Get ancestor skill base title
	 * @param con database connection
	 * @return Hashtable of ancester id as key and corresponding title as value
	 * @throws SQLException
	 */
	public Hashtable getAncestorTitle(Connection con)
		throws SQLException {
			
			Hashtable h_ancestor_title = new Hashtable();
			if( this.skb_ancestor != null && this.skb_ancestor.length() > 0 ){
				h_ancestor_title = DbCmSkillBase.getSkillsTitle(con, cwUtils.splitToVec(this.skb_ancestor.trim(), " , "));
			}

			return h_ancestor_title;  

		}

	/**
	 * Check is the skill base is existed (skb_delete_usr_id is null)
	 * @param con database connection
	 * @return true if skill base id exist and it's delete user id is null 
	 * @throws SQLException
	 */
	public boolean checkExist(Connection con)
		throws SQLException{
			
			boolean flag = false;
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_CHECK_EXISTENCE);
			stmt.setLong(1, this.skb_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				flag = true;
			}
			stmt.close();
			return flag;

		}


	/**
	 * Check the user submitted update timestamp of skill base 
	 * is equal to update timestamp of skill base in database 
	 * @param con database connection
	 * @return true if submitted update timestamp is equal to update timestamp in database
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public boolean equalsTimestamp(Connection con)
		throws SQLException, cwSysMessage {
		
		return true;

	}
	
	/**
	 * Check the user submitted scale id of the skill base 
	 * is equal to the scale id of the skill base in database 
	 * @param con database connection
	 * @return true if submitted update timestamp is equal to update timestamp in database
	 * @throws SQLException
	 * @throws cwSysMessage
	 */	
	public boolean equlasScaleId(Connection con)
		throws SQLException, cwSysMessage {
			
			if( this.skb_ssl_id == DbCmSkillBase.getScaleId(con, this.skb_id) ) {
				return true;
			} else {
				return false;
			}

		}

	/**
	 * Get Child skill id with rating_ind is true
	 * @param con database connection
	 * @return Vector of child skill base id
	 * @throws SQLException
	 */
	public Vector getChildRatingSkillId(Connection con) 
		throws SQLException {
			
			Vector v_skb_id = new Vector();
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_GET_CHILD_SKILL_ID_BY_RATING_IND);
			stmt.setLong(1, this.skb_id);
			stmt.setBoolean(2, true);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				v_skb_id.addElement(new Long(rs.getLong("skb_id")));
			}
			stmt.close();
			return v_skb_id;
		}

	/**
	 * Get Child skill id with rating_ind is false
	 * @param con database connection
	 * @return Vector of child skill base id
	 * @throws SQLException
	 */
	public Vector getChildNonRatingSkillId(Connection con) 
		throws SQLException {
			
			Vector v_skb_id = new Vector();
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_GET_CHILD_SKILL_ID_BY_RATING_IND);
			stmt.setLong(1, this.skb_id);
			stmt.setBoolean(2, false);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				v_skb_id.addElement(new Long(rs.getLong("skb_id")));
			}
			stmt.close();
			return v_skb_id;
		}
	
	
	/**
	 * Get Child elementary skill id
	 * @param con database connection
	 * @return Vector of child skill base id
	 * @throws SQLException
	 */
	public Vector getChildElementarySkillId(Connection con) 
		throws SQLException {
			
			Vector v_skb_id = new Vector();
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_GET_CHILD_SKILL_ID_BY_TYPE);
			stmt.setLong(1, this.skb_id);
			stmt.setString(2, DbCmSkillBase.COMPETENCY_SKILL);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				v_skb_id.addElement(new Long(rs.getLong("skb_id")));
			}
			stmt.close();
			return v_skb_id;
			
		}
	
	/**
	 * Get Child Composite skill id
	 * @param con database connection
	 * @return Vector of child skill base id
	 * @throws SQLException
	 */
	public Vector getChildCompositeSkillId(Connection con)
		throws SQLException {
			
			Vector v_skb_id = new Vector();
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_GET_CHILD_SKILL_ID_BY_TYPE);
			stmt.setLong(1, this.skb_id);
			stmt.setString(2, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				v_skb_id.addElement(new Long(rs.getLong("skb_id")));
			}
			stmt.close();
			return v_skb_id;
			
		}
		
	/**
	 * Update all child scale
	 * @param con database connection
	 * @throws SQLException
	 */
	public void updSuccessorScale(Connection con) 
		throws SQLException{
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_UPD_SUCCESSOR_SCALE);
			int index = 1;
			stmt.setLong(index++, this.skb_ssl_id);
			stmt.setString(index++, "% " + this.skb_id + " %");
			stmt.executeUpdate();
			stmt.close();
			return;
		}
		
	/**
	 * Check the skill base title is existed under the specified parent
	 * @param con database connection
	 * @param skb_title Skill Base Title
	 * @param skb_type Skill Base type to be checked
	 * @param skb_id excluded skill base id
	 * @param skb_owner_ent_id skill base owner entity id
	 * @param skb_parent_skb_id SKill base parent skill base id
	 * @return ture if title existed in the specified parent
	 * @throws SQLException
	 */
	public static boolean isTitleExist(Connection con, String skb_title, String skb_type, long skb_id, long skb_owner_ent_id, long skb_parent_skb_id)
		throws SQLException {
			
			boolean flag = false;
			StringBuffer SQLBuf = new StringBuffer(256);
			SQLBuf.append(SqlStatements.SQL_CM_SKILL_BASE_CHECK_TITLE_AND_TYPE);
			if( skb_id > 0 ) {
				SQLBuf.append(" And skb_id <> ? ");
			}
			if( skb_parent_skb_id > 0 ) {
				SQLBuf.append(" And skb_parent_skb_id = ? ");
			}
			PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
			int index = 1;
			stmt.setString(index++, skb_title);
			stmt.setString(index++, skb_type);
			stmt.setLong(index++, skb_owner_ent_id);
			if( skb_id > 0 ) {
				stmt.setLong(index++, skb_id);
			}
			if( skb_parent_skb_id > 0 ){
				stmt.setLong(index++, skb_parent_skb_id);
			}
			ResultSet rs = stmt.executeQuery();
			if( rs.next() ){
				if( rs.getLong(1) > 0 ){
					flag = true;
				}
			}
			stmt.close();
			return flag;
		}
	
	
	/**
	 * Construct ancestor path in Xml format
	 * @param con database connection
	 * @param skb_id skill base id
	 * @return ancestor in xml format
	 * @throws SQLException
	 * @throws cwSysMessage
	 */	
	public static String getAncestorXml(Connection con, long skb_id)
		throws SQLException, cwSysMessage {
 		String ancestor_list = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
    		stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_ANCESTOR_ID_LIST);
			stmt.setLong(1, skb_id);
    		rs = stmt.executeQuery();
			if(rs.next()){
				ancestor_list = rs.getString("skb_ancestor");
			}else{
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill base id = " + skb_id);
    		}
        } finally {
            if (stmt != null) {
                stmt.close();
            }
			}
			
			return getAncestorXml(con, ancestor_list);

		}
	
	/**
	 * Construct ancestor path in Xml format
	 * @param con database connection
	 * @param skb_ancestor ancestor list with comma as delimiter
	 * @return ancestor in xml format
	 * @throws SQLException
	 */
	public static String getAncestorXml(Connection con, String skb_ancestor)
		throws SQLException {
			
			StringBuffer xml = new StringBuffer();
			xml.append("<comp_path>");
			if( skb_ancestor != null && skb_ancestor.length() > 0 ) {
				
				PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_SKILL_LIST_TITLE + "(" + skb_ancestor + ")");
				ResultSet rs = stmt.executeQuery();
				Hashtable h_id_title = new Hashtable();
				while(rs.next()){
					h_id_title.put( new Long(rs.getLong("skb_id")), rs.getString("skb_title") );
				}
				stmt.close();
				
				long[] skb_id_list = cwUtils.splitToLong(skb_ancestor, ",");
				for(int i=0; i<skb_id_list.length; i++){
					xml.append("<comp skb_id=\"").append(skb_id_list[i]).append("\">")
						.append("<title>").append(cwUtils.esc4XML((String)h_id_title.get(new Long(skb_id_list[i])))).append("</title>")
						.append("</comp>");
				}
				
			}
			
			xml.append("</comp_path>");
			return xml.toString();

		}

	
	/**
	 * Get skill child id and desc
	 * @param con
	 * @return Vector of skill base object
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public Vector getChildDesc(Connection con)
		throws SQLException, cwSysMessage {
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_GET_CHILD_DESC);
			stmt.setLong(1, this.skb_id);
			ResultSet rs = stmt.executeQuery();
			Vector v_skb = new Vector();
			while(rs.next()){
				DbCmSkillBase skillBase = new DbCmSkillBase();
				skillBase.skb_id = rs.getLong("skb_id");
				skillBase.skb_description = rs.getString("skb_description");
				v_skb.addElement(skillBase);
			}
			stmt.close();
			return v_skb;
		}

	
	public static Vector getSkillBaseType(Connection con, Vector v_skb_id)
		throws SQLException, cwException {

			if( v_skb_id.isEmpty() ){
				return new Vector();
			}
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_GET_TYPE_LIST 
														+ cwUtils.vector2list(v_skb_id));
			ResultSet rs = stmt.executeQuery();
			Hashtable h_id_type = new Hashtable();
			while(rs.next()){
				h_id_type.put(new Long(rs.getLong("skb_id")), rs.getString("skb_type"));
			}
			stmt.close();
			Vector v_id_type = new Vector();
			for(int i=0; i<v_skb_id.size(); i++){
				v_id_type.addElement(h_id_type.get((Long)v_skb_id.elementAt(i)));
			}
			return v_id_type;
		}
	

}