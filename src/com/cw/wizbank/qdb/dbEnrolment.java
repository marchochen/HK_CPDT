package com.cw.wizbank.qdb;

import java.sql.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AccessControlWZB;

public class dbEnrolment
{
    public long enr_ent_id;
    public long enr_res_id; 
    public String enr_status; 
    public Timestamp enr_create_timestamp;
    public String enr_create_usr_id;
    
    public dbEnrolment() {;}
    
    /**
    Insert this enrolment record into Enrolment table<BR>
    Pre-define varibale:
    <ul>
    <li>enr_ent_id
    <li>enr_res_id
    <li>enr_status
    </ul>
    */
    public void ins(Connection con, String usr_id, Timestamp cur_time, long root_ent_id)
        throws SQLException, qdbException
    {
        boolean result;
        
        long lrnrSteEntId = dbRegUser.getSiteEntId(con, usr_id);        
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        this.enr_create_timestamp = cur_time;
        this.enr_create_usr_id = usr_id;

        PreparedStatement stmt = con.prepareStatement(
            "     (SELECT enr_res_id FROM Enrolment WHERE " 
            + "         enr_ent_id = ?  AND enr_res_id = ? ) ");
        int index = 1;
        stmt.setLong(index++, this.enr_ent_id);
        stmt.setLong(index++, this.enr_res_id);
                
        ResultSet rs = stmt.executeQuery(); 
        // no record exists
        if (!rs.next())  {
            stmt = con.prepareStatement(       
            " INSERT  INTO Enrolment (enr_ent_id, enr_res_id, enr_status, enr_create_usr_id, enr_create_timestamp ) "
            + "       VALUES (?, ? ,?, ?, ?) " );

            index = 1;
            stmt.setLong(index++, this.enr_ent_id);
            stmt.setLong(index++, this.enr_res_id);
            stmt.setString(index++, this.enr_status);
            stmt.setString(index++, this.enr_create_usr_id);
            stmt.setTimestamp(index++, this.enr_create_timestamp);
            stmt.executeUpdate();
            String[] lrn_role_lst = {AccessControlWZB.ROL_EXT_ID_NLRN};
            AcResources acres = new AcResources(con);
            for(int i=0; i<lrn_role_lst.length; i++) {
                String lrn_role = lrn_role_lst[i];
                boolean read = acres.hasResPermissionRead(lrn_role);
                boolean write = acres.hasResPermissionWrite(lrn_role);
                boolean exec = acres.hasResPermissionExec(lrn_role);
                dbResourcePermission.save(con,this.enr_res_id,this.enr_ent_id,lrn_role,read,write,exec);
            }
            //dbResourcePermission.assign(con, this.enr_res_id, this.enr_ent_id, dbResourcePermission.RIGHT_READ, true);
            //dbResourcePermission.assign(con, this.enr_res_id, this.enr_ent_id, dbResourcePermission.RIGHT_EXECUTE, true);
        }
        stmt.close();
        return;
    }

    public static boolean enrol(Connection con, long entId, long resId, loginProfile prof)
        throws qdbException
    {
    	boolean result = false;
        try {  
            long lrnrSteEntId = dbRegUser.getSiteEntId(con, entId);
            PreparedStatement stmt1 = con.prepareStatement(
               "     (SELECT enr_res_id FROM Enrolment WHERE " 
             + "         enr_ent_id = ?  AND enr_res_id = ? ) ");
            stmt1.setLong(1,entId);
            stmt1.setLong(2,resId);
            ResultSet rs1 = stmt1.executeQuery();
            // no record exists
            if (!rs1.next())  {
            	dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = entId;
                PreparedStatement stmt2 = con.prepareStatement(
                " INSERT  INTO Enrolment (enr_ent_id, enr_res_id, enr_status, enr_create_timestamp, enr_create_usr_id ) "
                + "       VALUES (?, ? ,?, ?, ?) " );

                stmt2.setLong(1,entId);
                stmt2.setLong(2,resId);
                stmt2.setString(3,dbCourse.COS_ENROLL_OK);
                stmt2.setTimestamp(4, cwSQL.getTime(con));
                stmt2.setString(5, usr.getUserId(con));
                int stmtResult = stmt2.executeUpdate();
                if ( stmtResult!=1)
                {
                    con.rollback();
                    throw new qdbException("Failed to insert enrolment.");
                } else {
                	result = true;
                }
                String[] lrn_role_lst = {AccessControlWZB.ROL_EXT_ID_NLRN};
                AcResources acres = new AcResources(con);
                for(int i=0; i<lrn_role_lst.length; i++) {
                    String lrn_role = lrn_role_lst[i];
                    boolean read = acres.hasResPermissionRead(lrn_role);
                    boolean write = acres.hasResPermissionWrite(lrn_role);
                    boolean exec = acres.hasResPermissionExec(lrn_role);
                    dbResourcePermission.save(con,resId,entId,lrn_role,read,write,exec);
                }
                //dbResourcePermission.assign(con,cos_res_id,entId, dbResourcePermission.RIGHT_READ, true);
                //dbResourcePermission.assign(con,cos_res_id,entId, dbResourcePermission.RIGHT_EXECUTE, true);
                stmt2.close();
            }
            stmt1.close();
            return result;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public static int delAllByEntId(Connection con, long entId)
        throws qdbException
    {
        try {  
            
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From Enrolment WHERE "
                + "     enr_ent_id = ? " ); 
            
            stmt.setLong(1, entId);
            int cnt = stmt.executeUpdate();
            stmt.close();
            return cnt;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
 
    private static final String sql_get_enrol = 
        " Select enr_status, enr_create_usr_id, enr_create_timestamp " +
        " From Enrolment Where enr_ent_id = ? And enr_res_id = ? ";
    /**
    Get the Enrolment record from database<BR>
    Pre-define variable:
    <ul>
    <li>enr_ent_id
    <li>enr_res_id
    </ul>
    @return true if enrolment is found, false if not found
    */
    public boolean get(Connection con) throws SQLException {
        
        boolean found;
        PreparedStatement stmt = con.prepareStatement(sql_get_enrol);
        int index = 1;
        stmt.setLong(index++, this.enr_ent_id);
        stmt.setLong(index++, this.enr_res_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.enr_status = rs.getString("enr_status");
            this.enr_create_usr_id = rs.getString("enr_create_usr_id");
            this.enr_create_timestamp = rs.getTimestamp("enr_create_timestamp");
            found = true;
        }
        else {
            found = false;
        }
        stmt.close();
        return found;
    }
    
}