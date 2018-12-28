package com.cw.wizbank.db;

import java.util.Vector;
import java.sql.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.db.sql.SqlStatements;

public class DbRoleTargetEntity{

    /**
    database field
    */
    public long rte_usr_ent_id;

    /**
    database field
    */
    public String rte_rol_ext_id;

    /**
    database field
    */
    public long rte_group_id;

    /**
    database field
    */
    public long rte_ent_id;
    
    /**
    database field
    */
    public Timestamp rte_syn_timestamp;
    /**
    database field
    */
    public Timestamp rte_eff_start_datetime;
    /**
    database field
    */
    public Timestamp rte_eff_end_datetime;

    /**
    database field
    */
    public Timestamp rte_create_timestamp;

    /**
    database field
    */
    public String rte_create_usr_id;
    
    public static class ViewSynRoleTargetEntity {
        public String user_uid;
        public String group_uid;
        public long rte_usr_ent_id;
        public String rte_rol_ext_id;
        public long rte_group_id;
        public long rte_ent_id;
        public Timestamp rte_eff_start_datetime;
        public Timestamp rte_eff_end_datetime;
    }
    
    /**
    delete from usrRoleTargetEntity according to 
    rte_usr_ent_id, rte_rol_ext_id, rte_group_id, rte_ent_id
    */
    public void del(Connection con) throws SQLException {
        del(con, false);
    }
    /**
    delete from usrRoleTargetEntity according to 
    rte_usr_ent_id, rte_rol_ext_id, rte_group_id, rte_ent_id
    can depend on rte_eff_start_datetime, rte_eff_end_datetime or not
    */
    public void del(Connection con, boolean byEffDate) throws SQLException {
        
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append("Delete From usrRoleTargetEntity Where 1 = 1 ");
        if(this.rte_usr_ent_id != 0) {
            SQLBuf.append(" And rte_usr_ent_id = ? ");
        }        
        if(this.rte_rol_ext_id != null) {
            SQLBuf.append(" And rte_rol_ext_id = ? ");
        }        
        if(this.rte_group_id != 0) {
            SQLBuf.append(" And rte_group_id = ? ");
        }        
        if(this.rte_ent_id != 0) {
            SQLBuf.append(" And rte_ent_id = ? ");
        }        
        if (byEffDate){
            if(this.rte_eff_start_datetime != null) {
                SQLBuf.append(" And rte_eff_start_datetime = ? ");
            }else{
                SQLBuf.append(" And rte_eff_start_datetime IS NULL ");
            }        
            if(this.rte_eff_end_datetime != null) {
                SQLBuf.append(" And rte_eff_end_datetime = ? ");
            }else{
                SQLBuf.append(" And rte_eff_end_datetime IS NULL ");
            }        
        }        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        if(this.rte_usr_ent_id != 0) {
            stmt.setLong(index++, this.rte_usr_ent_id);
        }        
        if(this.rte_rol_ext_id != null) {
            stmt.setString(index++, this.rte_rol_ext_id);
        }        
        if(this.rte_group_id != 0) {
            stmt.setLong(index++, this.rte_group_id);
        }        
        if(this.rte_ent_id != 0) {
            stmt.setLong(index++, this.rte_ent_id);
        }
        if (byEffDate){
            if(this.rte_eff_start_datetime != null) {
                stmt.setTimestamp(index++, this.rte_eff_start_datetime);
            }
            if(this.rte_eff_end_datetime != null) {
                stmt.setTimestamp(index++, this.rte_eff_end_datetime);
            }        
        }        
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    public void ins(Connection con) throws SQLException {
        
        if(this.rte_create_timestamp == null) {
            this.rte_create_timestamp = cwSQL.getTime(con);
        }
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_role_target_entity);
        stmt.setLong(1, this.rte_usr_ent_id);
        stmt.setString(2, this.rte_rol_ext_id);
        stmt.setLong(3, this.rte_group_id);
        stmt.setLong(4, this.rte_ent_id);
        stmt.setTimestamp(5, this.rte_syn_timestamp);
        stmt.setTimestamp(6, this.rte_eff_start_datetime);
        stmt.setTimestamp(7, this.rte_eff_end_datetime);
        stmt.setTimestamp(8, this.rte_create_timestamp);
        stmt.setString(9, this.rte_create_usr_id);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
    Get the max(group_id) of the user, role pair<BR>
    pre-defined variable:
    <ul>
    <li>rte_usr_ent_id
    <li>rte_rol_ext_id
    </ul>
    */
    public long getMaxItemTargetGroupId(Connection con) throws SQLException {
        
        long max;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_max_role_target_entity_group_id);
        stmt.setLong(1, this.rte_usr_ent_id);
        stmt.setString(2, this.rte_rol_ext_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            max = rs.getLong("m");
        }
        else {
            max = 0;
        }
        stmt.close();
        return max;
    }
    
    /**
    Get the effective target entity id(s) of the user, role pair <BR>
    pre-defined variable:
    <ul>
    <li>rte_usr_ent_id
    <li>rte_rol_ext_id
    </ul>
    */
    
    public Vector getRoleTargetEntityIds(Connection con) throws SQLException {
        Timestamp curTime = cwSQL.getTime(con);
        Vector entVec = new Vector();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_role_target_entity_ids);
        stmt.setLong(1, this.rte_usr_ent_id);
        stmt.setString(2, this.rte_rol_ext_id);
        stmt.setTimestamp(3, curTime);
        stmt.setTimestamp(4, curTime);
        
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            Long entId = new Long(rs.getLong("rte_ent_id"));
            if (!entVec.contains(entId)){
                entVec.addElement(entId);
            }
        }
        stmt.close();
        return entVec;
    }
    
    /**
    Check the existence of the target entity pair with the user, role pair, target entity id, and effective start , end date<BR>
    pre-defined variable:
    <ul>
    <li>rte_usr_ent_id
    <li>rte_rol_ext_id
    <li>rte_ent_id
    <li>rte_eff_start_datetime
    <li>rte_eff_end_datetime
    </ul>
    */
    public boolean checkExist(Connection con) throws SQLException{
        boolean bExist;
        String sql = SqlStatements.sql_check_role_target_entity;
        if (rte_eff_start_datetime != null)
            sql += " AND rte_eff_start_datetime = ? ";
        else
            sql += " AND rte_eff_start_datetime IS NULL ";
            
        if (rte_eff_end_datetime != null)
            sql += " AND rte_eff_end_datetime = ? ";
        else            
            sql += " AND rte_eff_end_datetime IS NULL ";

        PreparedStatement stmt = con.prepareStatement(sql);
        int col=1;
        stmt.setLong(col++, this.rte_usr_ent_id);
        stmt.setString(col++, this.rte_rol_ext_id);
        stmt.setLong(col++, this.rte_ent_id);
        
        if (rte_eff_start_datetime != null)
            stmt.setTimestamp(col++, this.rte_eff_start_datetime);
        if (rte_eff_end_datetime != null)
            stmt.setTimestamp(col++, this.rte_eff_end_datetime);
                                    
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            bExist = true;
        }else{
            bExist = false;
        }
        stmt.close();
        return bExist;
    }
    
    /**
    Update the syn date of the record with the user, role pair, target entity id<BR>
    pre-defined variable:
    <ul>
    <li>rte_usr_ent_id
    <li>rte_rol_ext_id
    <li>rte_ent_id
    <li>rte_eff_start_datetime
    <li>rte_eff_end_datetime
    
    </ul>
    */
    public void updSynTimestamp(Connection con) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        String sql = SqlStatements.upd_role_target_entity_syn_timestamp;
        if (rte_eff_start_datetime != null)            
            sql += " AND rte_eff_start_datetime = ? ";    
        else
            sql += " AND rte_eff_start_datetime IS NULL ";    
        if (rte_eff_end_datetime != null)
            sql += " AND rte_eff_end_datetime = ? ";    
        else
            sql += " AND rte_eff_end_datetime IS NULL ";    

        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setTimestamp(index++, curTime);
        stmt.setLong(index++, this.rte_usr_ent_id);
        stmt.setString(index++, this.rte_rol_ext_id);
        stmt.setLong(index++, this.rte_ent_id);

        if (rte_eff_start_datetime!=null)
            stmt.setTimestamp(index++, rte_eff_start_datetime);
        if (rte_eff_end_datetime!=null)
            stmt.setTimestamp(index++, rte_eff_end_datetime);

        stmt.executeUpdate();
        stmt.close();
    }
    
    /**
    Get a list of not-in-syn usergroup (not system role)
    @param con Connection to database
    @param synTimestamp start syn timestamp 
    @param site id
    @return Result set with column USER_UID, GROUP_UID, usr_ent_id, ent_id , rte_rol_ext_id, rte_group_id
    */
    public static ViewSynRoleTargetEntity[] getNotInSynUsrRoleTargetEntity(Connection con, Timestamp synTimestamp, long siteId, String roleExtId) 
        throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_not_syn_role_target_entity);
            int col = 1;
            stmt.setLong(col++, siteId);
            stmt.setString(col++, DbRegUser.USR_STATUS_OK);
            stmt.setString(col++, DbRegUser.USR_STATUS_PENDING);
            stmt.setTimestamp(col++, synTimestamp);
            stmt.setBoolean(col++, true);
            stmt.setBoolean(col++, true);
            stmt.setTimestamp(col++, synTimestamp);
            stmt.setString(col++, roleExtId);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while(rs.next()){
                ViewSynRoleTargetEntity synRte = new ViewSynRoleTargetEntity();
                synRte.user_uid = rs.getString("USER_UID");
                synRte.group_uid = rs.getString("GROUP_UID");
                synRte.rte_usr_ent_id = rs.getLong("usr_ent_id");
                synRte.rte_rol_ext_id = rs.getString("rte_rol_ext_id");
                synRte.rte_group_id = rs.getLong("rte_group_id");
                synRte.rte_ent_id = rs.getLong("ent_id");
                synRte.rte_eff_start_datetime = rs.getTimestamp("rte_eff_start_datetime");
                synRte.rte_eff_end_datetime = rs.getTimestamp("rte_eff_end_datetime");
                tempResult.addElement(synRte);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewSynRoleTargetEntity result[] = new ViewSynRoleTargetEntity[tempResult.size()];
        result = (ViewSynRoleTargetEntity[])tempResult.toArray(result);
        
        return result;
    }
}