package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.competency.CmSkillSetManager;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.db.view.ViewEntityToTree.entityInfo;
import com.cw.wizbank.util.*;

/**
A database class to manage table cmSkillSet
*/
public class DbCmSkillSet {

    public static final String ASSESSMENT_SURVEY = "ASY";
    
    public long         sks_skb_id;
    public String       sks_type;
    public String       sks_title;
    public String       sks_xml;
    public long         sks_owner_ent_id;
    public String       sks_create_usr_id;
    public Timestamp    sks_create_timestamp;
    public String       sks_update_usr_id;
    public Timestamp    sks_update_timestamp;
    public long 		sks_ske_id;
    
    private static final String COMMA       =   ",";
    
    private static final String SQL_CHECK_LAST_UPD = " SELECT sks_skb_id FROM cmSkillSet "
                                     + " WHERE sks_skb_id = ?  ";

    private static final String SQL_INS_CMSKILLSET = " INSERT INTO cmSkillSet ( " 
                                      + " sks_type, sks_title, sks_xml, "
                                      + " sks_owner_ent_id, "
                                      + " sks_create_usr_id, sks_create_timestamp, "
                                      + " sks_update_usr_id, sks_update_timestamp ,sks_ske_id) " 
                                      + " VALUES (?,?,?,?,?,?,?,?,?) ";

    private static final String SQL_UPD_CMSKILLSET = " UPDATE cmSkillSet SET "
                                      + "  sks_title = ? , sks_xml = ? , "
                                      + "  sks_update_usr_id = ? , sks_update_timestamp = ? "
                                      + " WHERE sks_skb_id = ? ";

    private static final String SQL_DEL_CMSKILLSET = " DELETE From cmSkillSet "
                                      + " WHERE sks_skb_id IN  ";


    private static String SQL_GET = " SELECT sks_skb_id , sks_type , sks_title , sks_xml , "
                                +   " sks_owner_ent_id, sks_create_usr_id, sks_create_timestamp , "
                                +   " sks_update_usr_id, sks_update_timestamp "
                                +   " FROM cmSkillSet WHERE sks_skb_id = ? ";
                                
    private static final String SQL_GET_BY_TYPE = " SELECT sks_skb_id FROM cmSkillSet "
                                            +     " WHERE sks_type = ? ";

    private static final String SQL_UPD_COMMENT = " UPDATE cmSkillSet SET sks_xml = ? , "
                                                + " sks_update_usr_id = ? , sks_update_timestamp = ? "
                                                + " WHERE sks_skb_id = ? ";
    

    /**
    Inser a new skillset
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException, cwSysMessage {

        PreparedStatement stmt = null;
        int code = 0;
        
        try {
            //get database time
            if(sks_create_timestamp == null) {
                sks_create_timestamp = cwSQL.getTime(con);
                sks_update_timestamp = sks_create_timestamp;
            }
            sks_update_usr_id = sks_create_usr_id;
            //try to insert 
            stmt = con.prepareStatement(SQL_INS_CMSKILLSET, PreparedStatement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setString(index++, sks_type);
            stmt.setString(index++, sks_title);
            stmt.setString(index++, sks_xml);        
            stmt.setLong(index++, sks_owner_ent_id);
            stmt.setString(index++, sks_create_usr_id);
            stmt.setTimestamp(index++, sks_create_timestamp);
            stmt.setString(index++, sks_update_usr_id);
            stmt.setTimestamp(index++, sks_update_timestamp);
            stmt.setLong(index ++, sks_ske_id);
            code = stmt.executeUpdate();
            //get back the newly inserted sks_skb_id
            sks_skb_id = cwSQL.getAutoId(con, stmt, "cmSkillSet", "sks_skb_id");
        }
        finally {
            if(stmt!=null) stmt.close();
        }
        return code;
    }

    /**
    Update a specified skill from the database
    @return the row count for UPDATE
    */
    public int upd(Connection con)
        throws SQLException {




        PreparedStatement stmt = null;
        int code = 0;
        try {
            stmt = con.prepareStatement(SQL_UPD_CMSKILLSET);
            Timestamp curTime = cwSQL.getTime(con);
            int index =1;
            stmt.setString(index++, sks_title);
            stmt.setString(index++, sks_xml);
            stmt.setString(index++, sks_update_usr_id);
            stmt.setTimestamp(index++, curTime);
            stmt.setLong(index++, sks_skb_id);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return code;
    }

    /**
    Delete specified skill sets from the database
    @return the row count for DELETE    
    */
    public int del(Connection con, long[] ids)
        throws SQLException {
            
            if( ids == null || ids.length == 0 )
                return 0;
            
            StringBuffer idsStr = new StringBuffer().append("(0");
            for(int i=0; i<ids.length; i++)
                idsStr.append(COMMA).append(ids[i]);
            idsStr.append(")");
        
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_CMSKILLSET + idsStr.toString());   
            int code = stmt.executeUpdate();
            stmt.close();

            //super.delSkillBases(con, ids);
        
            return code;
    }
    
    /**
    Get specified skill set from database
    */
    public void get(Connection con)
        throws SQLException, cwSysMessage {
        
            PreparedStatement stmt = con.prepareStatement(SQL_GET);
            stmt.setLong(1, sks_skb_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                sks_type = rs.getString("sks_type");
                sks_title = rs.getString("sks_title");
                sks_xml = rs.getString("sks_xml");
                sks_owner_ent_id = rs.getLong("sks_owner_ent_id");
                sks_create_usr_id = rs.getString("sks_create_usr_id");
                sks_create_timestamp = rs.getTimestamp("sks_create_timestamp");
                sks_update_usr_id = rs.getString("sks_update_usr_id");
                sks_update_timestamp = rs.getTimestamp("sks_update_timestamp");
            } else {
                throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill base id = " + sks_skb_id);
            }
            stmt.close();
            return;
    }
        
    /**
    Get detial of the skill set 
    @return result set 
    */
    public ResultSet getSkillSet(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET);
            stmt.setLong(1, sks_skb_id);
            ResultSet rs = stmt.executeQuery();
            return rs;
        }
        
        
    /**
    Get all skill set id belong to the type specified
    @return vector of id in long
    */
    public Vector getByType(Connection con)
        throws SQLException {
            
            Vector vec = new Vector();            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_BY_TYPE);
            stmt.setString(1, sks_type);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() )
                vec.addElement(new Long(rs.getLong("sks_skb_id")));
            stmt.close();
            return vec;
            
        }
        
        
    /**
    Update skill set comment (sks_xml)
    */
    public void updComment(Connection con)
        throws SQLException, cwException {
        
        PreparedStatement stmt = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL_UPD_COMMENT);
            stmt.setString(1, sks_xml);
            stmt.setString(2, sks_update_usr_id);
            stmt.setTimestamp(3, curTime);
            stmt.setLong(4, sks_skb_id);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Skill Set not exist , id = " + sks_skb_id);
        } finally {
            if(stmt!=null) stmt.close();
        }
            return;        
        }        
 
        
        
    public boolean validUpdTimestamp(Connection con, Timestamp last_upd_timestamp)
        throws SQLException{
            
            PreparedStatement stmt = null;
            boolean flag = false;
            try {
                stmt = con.prepareStatement(SQL_CHECK_LAST_UPD);
                stmt.setLong(1, sks_skb_id);
                //stmt.setTimestamp(2, last_upd_timestamp);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    flag = true;
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
            return flag;
        }
        
    public void updTimestamp(Connection con)
        throws SQLException {
            
            if( sks_update_timestamp == null )
                sks_update_timestamp = cwSQL.getTime(con);
                
            String SQL = " UPDATE cmSkillSet "
                       + " SET sks_update_timestamp = ? "
                       + " WHERE sks_skb_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setTimestamp(1, sks_update_timestamp);
            stmt.setLong(2, sks_skb_id);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to update skill set timestamp, sks_skb_id = " + sks_skb_id);
            stmt.close();
            return;
            
        }


    public static Timestamp getUpdTimestamp(Connection con, long sks_id)
        throws SQLException{
            
            String SQL = " SELECT sks_update_timestamp FROM cmSkillSet where sks_skb_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, sks_id);
            ResultSet rs = stmt.executeQuery();
            Timestamp updTimestamp = null;
            if(rs.next())
                updTimestamp = rs.getTimestamp("sks_update_timestamp");
            else
                throw new SQLException("Failed to get skill update timestamp, sks_skb_id = " + sks_id);
            stmt.close();
            return updTimestamp;
        }
    
    /**
     * 岗位信息及用户与岗位的关系
     */
    public static void converUserPositionData(Connection con) throws SQLException {
    	insertUserPosition(con);
    	insertUserPositionRelation(con);
    	updateAeitemtargetruledetail(con);
    	getAndUpdAeItemTargetRule(con);
    }
    
    /**
     * 把旧的岗位信息复制到新的岗位数据表上
     */
    public static void insertUserPosition(Connection con) throws SQLException {
    	String sql = " insert into UserPosition select 'ske' + convert(varchar,sks_ske_id), sks_title, null, 1, substring(sks_update_usr_id, 4, Len(sks_update_usr_id)), sks_update_timestamp from cmSkillSet where sks_type = 'SKP' ";
    	if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
    		sql = " insert into UserPosition select UserPosition_seq.Nextval,'ske' || sks_ske_id, sks_title, null, 1, substr(sks_update_usr_id, 4), sks_update_timestamp from cmSkillSet where sks_type = 'SKP' ";
    	}
    	PreparedStatement stmt = null;
    	try{
        	stmt = con.prepareStatement(sql);
        	stmt.executeUpdate();
        } finally {
        	cwSQL.cleanUp(null, stmt);
        }
    }
    
    /**
     * 把旧的用户与岗位的关系复制到新的岗位关系数据表上
     */
    public static void insertUserPositionRelation(Connection con) throws SQLException {
    	String sql = " insert into UserPositionRelation select upt_id, uss_ent_id from RegUserSkillSet inner join UserPosition on upt_code = 'ske' + convert(varchar, uss_ske_id) ";
    	if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
    		sql = " insert into UserPositionRelation select upt_id, uss_ent_id from RegUserSkillSet inner join UserPosition on upt_code = 'ske' || uss_ske_id ";
    	}
    	PreparedStatement stmt = null;
    	try{
        	stmt = con.prepareStatement(sql);
        	stmt.executeUpdate();
        } finally {
        	cwSQL.cleanUp(null, stmt);
        }
    }
    
    /**
     * 把旧的用户与岗位的关系复制到新的岗位关系数据表上
     */
    public static void updateAeitemtargetruledetail(Connection con) throws SQLException {
    	String sql = " update aeitemtargetruledetail set ird_upt_id = (select upt_id from UserPosition where  upt_code = 'ske' + convert(varchar,ird_upt_id)) where ird_upt_id > 0 ";
    	if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
    		sql = " update aeitemtargetruledetail set ird_upt_id = (select upt_id from UserPosition where  upt_code = 'ske' || ird_upt_id) where ird_upt_id > 0 ";
    	}
    	PreparedStatement stmt = null;
    	try{
        	stmt = con.prepareStatement(sql);
        	stmt.executeUpdate();
        } finally {
        	cwSQL.cleanUp(null, stmt);
        }
    }
    
    /**
     * 获取课程目标学员中与旧岗位的管理 
     */
    public static void getAndUpdAeItemTargetRule(Connection con) throws SQLException {
    	String sql = " select itr_id, itr_upt_id from aeitemtargetrule where itr_upt_id > 0 ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		updateAeItemTargetRule(con, rs.getLong("itr_id"), rs.getString("itr_upt_id"));
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    /**
     * 更新课程目标岗位id
     * @param itr_id
     * @param itr_upt_id
     */
    public static void updateAeItemTargetRule(Connection con, long itr_id, String itr_upt_id) throws SQLException {
    	String[] upt_id_list = itr_upt_id.split(",");
    	String new_upt_id = "";
    	for(String upt_id : upt_id_list){
    		new_upt_id += getNewUptId(con, upt_id) + ",";
    	}
    	String sql = " update aeItemTargetRule set itr_upt_id = ? where itr_id = ? ";
    	PreparedStatement stmt = null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setString(1, new_upt_id.substring(0, new_upt_id.length() - 1));
        	stmt.setLong(2, itr_id);
        	stmt.executeUpdate();
        } finally {
        	cwSQL.cleanUp(null, stmt);
        }
    }
    
    /**
     * 获取新岗位数据表的id
     * @param itr_upt_id
     */
    public static String getNewUptId(Connection con, String upt_id) throws SQLException {
    	String sql = " select upt_id from RegUserSkillSet inner join UserPosition on upt_code = ? ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setString(1, "ske" + upt_id);
        	rs = stmt.executeQuery();
        	if(rs.next()){
        		upt_id = rs.getString("upt_id");
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
        return upt_id;
    }

}

