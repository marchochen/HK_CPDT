package com.cw.wizbank.ae.db.view;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.db.DbEntityRelation;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cwn.wizbank.entity.AcRole;

public class ViewAppnApprovalList {
    
    public long aal_usr_ent_id;
    public String aal_approval_role;
    public long itm_id;
    public String itm_title;
    public long run_itm_id;
    public String run_itm_title;
        
    public long app_id;
    public long app_ent_id;
    public String app_process_status;
    public Timestamp app_upd_timestamp;
    public String appt_display_bil;  // display bil of the applicant
    public String appt_full_path; 
    public Timestamp aal_create_timestamp;
    
    public String aal_action_taken;
    public long actr_ent_id;
    public String actr_display_bil;
    public String actr_role;
    public Timestamp aal_action_timestamp;
        
    public Vector getPendingApprovalList(Connection con, long usr_ent_id, String sortCol, String sortOrder, String acRole) throws SQLException{
        Vector vtApprovalList = new Vector();
        
		String sql = SqlStatements.getPendingApprovalListSQL(con, acRole) + " ORDER BY " + sortCol + " " + sortOrder;
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setBoolean(index++, false);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbAppnApprovalList.STATUS_PENDING);
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){
        	stmt.setString(index++, acRole);
        }
        stmt.setLong(index++, usr_ent_id);
        
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbAppnApprovalList.STATUS_PENDING);
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){
        	stmt.setString(index++, acRole);
        }
        stmt.setLong(index++, usr_ent_id);
        
        ResultSet rs = stmt.executeQuery();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        while (rs.next()){
        	long usg_id = rs.getLong("ern_usg_id");
        	if(usg_id == 0){
        		usg_id = rs.getLong("erh_usg_id");
        	}
            ViewAppnApprovalList data = new ViewAppnApprovalList();
            data.aal_usr_ent_id = rs.getLong("aal_usr_ent_id");
            data.aal_approval_role = rs.getString("aal_approval_role");
            data.itm_id = rs.getLong("itm_id");
            data.itm_title = rs.getString ("itm_title");
            data.run_itm_id = rs.getLong("run_itm_id");
            data.run_itm_title = rs.getString ("run_itm_title");
            data.app_id = rs.getLong("app_id");
            data.app_ent_id = rs.getLong("app_ent_id");
            data.app_process_status = rs.getString ("app_process_status");
            data.app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
            data.appt_display_bil = rs.getString("appt_display_bil");
            data.appt_full_path = entityfullpath.getFullPath(con, usg_id);
            data.aal_create_timestamp = rs.getTimestamp("aal_create_timestamp");
            vtApprovalList.addElement(data);
        }
        stmt.close();
        return vtApprovalList;
    }
    
    public Vector getApprovalHistoryList(Connection con, long usr_ent_id, String sortCol, String sortOrder, String acRole) throws SQLException{
        Vector vtApprovalList = new Vector();
        
		String sql = SqlStatements.getApprovalHistoryListSQL(con,acRole) + "ORDER BY " + sortCol + " " + sortOrder;
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setBoolean(index++, false);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbAppnApprovalList.STATUS_HISTORY);
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){
        	stmt.setString(index++, acRole);
        }
        stmt.setLong(index++, usr_ent_id);
        
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, DbAppnApprovalList.STATUS_HISTORY);
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){
        	stmt.setString(index++, acRole);
        }
        stmt.setLong(index++, usr_ent_id);
        
        ResultSet rs = stmt.executeQuery();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        while (rs.next()){
        	long usg_id = rs.getLong("ern_usg_id");
        	if(usg_id == 0){
        		usg_id = rs.getLong("erh_usg_id");
        	}
            ViewAppnApprovalList data = new ViewAppnApprovalList();
            data.aal_usr_ent_id = rs.getLong("aal_usr_ent_id");
            data.aal_approval_role = rs.getString("aal_approval_role");
            data.itm_id = rs.getLong("itm_id");
            data.itm_title = rs.getString ("itm_title");
            data.run_itm_id = rs.getLong("run_itm_id");
            data.run_itm_title = rs.getString ("run_itm_title");
            data.app_id = rs.getLong("app_id");
            data.app_ent_id = rs.getLong("app_ent_id");
            data.app_process_status = rs.getString ("app_process_status");
            data.app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
            data.appt_display_bil = rs.getString("appt_display_bil");
            data.appt_full_path = entityfullpath.getFullPath(con, usg_id);
            data.aal_create_timestamp = rs.getTimestamp("aal_create_timestamp");
            data.aal_action_taken = rs.getString("aal_action_taken");
            data.actr_ent_id = rs.getLong("actr_ent_id");
            data.actr_display_bil = rs.getString("actr_display_bil");
            data.actr_role = rs.getString("actr_role");
            data.aal_action_timestamp = rs.getTimestamp("aal_action_timestamp");            
            vtApprovalList.addElement(data);
        }
        stmt.close();
        return vtApprovalList;
    }

}
