package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;

public class dbEntity extends Object
{
    public static final String ENT_TYPE_USER = "USR";
    public static final String ENT_TYPE_USER_GROUP = "USG";
    public static final String ENT_TYPE_INDUSTRY_CODE = "IDC";
    public static final String ENT_TYPE_USER_GRADE = "UGR";
    public static final String ENT_TYPE_USER_EXT_GROUP_1 = "EG1";
    public static final String ENT_TYPE_INSTR = "INSTR";
    
    static final String INVALID_TIMESTAMP_MSG = "USG001"; //"Record modified by other user." ; 
    
    public long        ent_id;
    public String      ent_type;
    public Timestamp   ent_upd_date;
    public boolean     ent_syn_ind = false;
//    public boolean     ent_syn_rol_ind = true;
    public Timestamp   ent_syn_date;
    public String      ent_ste_uid;
    public Timestamp   ent_delete_timestamp;
    
    public dbEntity() {;}
    
    public void ins(Connection con)
        throws qdbException, qdbErrMessage
    {
        try {  
            if(ent_type==null || ent_type.length()==0)
                ent_type = ENT_TYPE_USER_GROUP;
            
            ent_upd_date = dbUtils.getTime(con);
            
            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO Entity "
                + " (ent_type, ent_ste_uid, ent_upd_date, ent_syn_ind )"
                + " VALUES (?,?, ?, ?) ",  PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, ent_type);
            stmt.setString(2, ent_ste_uid);
            stmt.setTimestamp(3,ent_upd_date); 
            stmt.setBoolean(4,ent_syn_ind); 
            if( stmt.executeUpdate()!= 1)
            {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to insert new entity.");
            }
            ent_id = cwSQL.getAutoId(con, stmt, "Entity", "ent_id");
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
        return;         
    }
    
    public void get(Connection con)
        throws qdbException
    {
        try {  
            PreparedStatement stmt = con.prepareStatement(
                " SELECT ent_type, ent_ste_uid, ent_upd_date, ent_syn_ind,ent_delete_timestamp FROM Entity "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                + " WHERE ent_id = ? ");
            
            stmt.setLong(1, ent_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next())
            {
                ent_type = rs.getString("ent_type"); 
                ent_ste_uid = rs.getString("ent_ste_uid");
                ent_upd_date = rs.getTimestamp("ent_upd_date"); 
                ent_syn_ind = rs.getBoolean("ent_syn_ind");
                ent_delete_timestamp=rs.getTimestamp("ent_delete_timestamp");
             
            } else {
            	if(rs!=null)rs.close();
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to get entity.");
            }
            rs.close();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
        return;         
    }

    // keep for compatibility
    public void getById(Connection con)
        throws qdbException
    {
        try {  
            PreparedStatement stmt = con.prepareStatement(
                " SELECT ent_type, ent_ste_uid, ent_upd_date, ent_syn_ind, ent_delete_timestamp FROM Entity "
                + " WHERE ent_id = ? ");
            
            stmt.setLong(1, ent_id);
            
            ResultSet rs = stmt.executeQuery();
            if( rs.next())
            {
                ent_type = rs.getString("ent_type"); 
                ent_ste_uid = rs.getString("ent_ste_uid");
                ent_upd_date = rs.getTimestamp("ent_upd_date"); 
                ent_syn_ind = rs.getBoolean("ent_syn_ind");
                ent_delete_timestamp = rs.getTimestamp("ent_delete_timestamp");
            }else {
            	if(rs!=null)rs.close();
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to get entity.");
            }
            rs.close();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
        return;         
    }

    public void upd(Connection con)
        throws qdbException, qdbErrMessage
    {
        try {
            
            ent_upd_date = dbUtils.getTime(con);
            
            PreparedStatement stmt = con.prepareStatement(
                "UPDATE Entity SET "
                + " ent_syn_ind = ?,  "
                + " ent_upd_date = ? "
                + " WHERE ent_id = ? ") ;
            
            stmt.setBoolean(1,ent_syn_ind);
            stmt.setTimestamp(2,ent_upd_date);
            stmt.setLong(3,ent_id);

            /* update */
            if(stmt.executeUpdate() != 1 )
            {
                // update fails, rollback
                stmt.close();
                con.rollback();
                throw new qdbException("Fails to update user record.");
            }
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
      
        return;
    }

	public void upd2(Connection con)
			throws qdbException, qdbErrMessage
	{
		try {
        
			ent_upd_date = dbUtils.getTime(con);
        
			PreparedStatement stmt = con.prepareStatement(
				"UPDATE Entity SET "
				+ " ent_syn_ind = ?, "
				+ " ent_upd_date = ?, "
				+ " ent_ste_uid = ? "
				+ " WHERE ent_id = ? ") ;
        
			stmt.setBoolean(1,ent_syn_ind);
			stmt.setTimestamp(2,ent_upd_date);
			stmt.setString(3, ent_ste_uid);
			stmt.setLong(4,ent_id);

			/* update */
			if(stmt.executeUpdate() != 1 )
			{
				// update fails, rollback
				stmt.close();
				con.rollback();
				throw new qdbException("Fails to update user record.");
			}
			stmt.close();

		} catch(SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage()); 
		}
  
		return;
	}
		
    public void unDelete(Connection con) throws qdbException, qdbErrMessage {
        try { 
            ent_upd_date = dbUtils.getTime(con);
            
            PreparedStatement stmt = con.prepareStatement(
                "UPDATE Entity SET "
                + " ent_delete_usr_id = ? , "
                + " ent_delete_timestamp = ?, "
                + " ent_upd_date = ? "
                + " WHERE ent_id = ? ");
            
            stmt.setNull(1,java.sql.Types.VARCHAR);
            stmt.setNull(2, java.sql.Types.TIMESTAMP);
            stmt.setTimestamp(3,ent_upd_date);
            stmt.setLong(4,ent_id);

            if(stmt.executeUpdate() != 1 ) {
                stmt.close();
                con.rollback();
                throw new qdbException("Fails to undelete user record.");
            }
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
      
        return;
    }

    public void updUid(Connection con)
        throws qdbException, qdbErrMessage
    {
        try {
            
            ent_upd_date = dbUtils.getTime(con);
            
            PreparedStatement stmt = con.prepareStatement(
                "UPDATE Entity SET "
                + " ent_ste_uid = ? , "
                + " ent_upd_date = ? "
                + " WHERE ent_id = ? ") ;
            
            stmt.setString(1,ent_ste_uid);
            stmt.setTimestamp(2,ent_upd_date);
            stmt.setLong(3,ent_id);

            /* update */
            if(stmt.executeUpdate() != 1 )
            {
                // update fails, rollback
                stmt.close();
                con.rollback();
                throw new qdbException("Fails to update user record.");
            }
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
      
        return;
    }

    public void del(Connection con, String ent_delete_usr_id, Timestamp deleteTime)
        throws qdbException
    {
        try {
            if(deleteTime == null)
            	deleteTime = cwSQL.getTime(con);
//            PreparedStatement stmt = con.prepareStatement("DELETE From Entity where ent_id= ? " );
            PreparedStatement stmt = con.prepareStatement("Update Entity set ent_delete_usr_id = ?, ent_delete_timestamp = ? where ent_id = ? ");
            stmt.setString(1, ent_delete_usr_id);
            stmt.setTimestamp(2, deleteTime);
            stmt.setLong(3, ent_id);
            
            if( stmt.executeUpdate()!= 1)
            {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to delete Entity.");
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
        return;    
    }
    
    public void checkTimeStamp(Connection con) // check last upd time first
        throws qdbException, qdbErrMessage
    {
       try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT ent_upd_date from Entity where ent_id = ? " );
         
            stmt.setLong(1, ent_id);
            ResultSet rs = stmt.executeQuery();
            boolean bTSOk = false;
            if(rs.next())
            {

                Timestamp t = rs.getTimestamp(1);
                Timestamp tTmp = t;
                tTmp.setNanos(ent_upd_date.getNanos());
                if(tTmp.equals(ent_upd_date))
                    bTSOk = true;
            }
            stmt.close();
            if(!bTSOk) {
                con.rollback();

                throw new qdbErrMessage(INVALID_TIMESTAMP_MSG);
            }
            
        }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    

    /*
    public void checkAdminRole(Connection con, String usrId) // check whether the user have the right
        throws qdbException, qdbErrMessage
    {

        if (!dbRegUser.hasRole(con, usrId, dbUserGroup.USG_ROLE_ADMIN)) {
            //Only administrator can modify user / group record.
            throw new qdbErrMessage("USG002");
        }
        
        return;        
    }
    */
    
    public static Vector getUserGroupEntityId(Connection con, Vector entIdVec)
        throws SQLException {
            
            String SQL = " SELECT ent_id FROM ENTITY "
                       + " WHERE ent_type = ? "
                       + " AND ent_delete_usr_id IS NULL "
                       + " AND ent_delete_timestamp IS NULL "
                       + " AND ent_id IN " + dbUtils.vec2list(entIdVec);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, ENT_TYPE_USER_GROUP);
            ResultSet rs = stmt.executeQuery();
            Vector vec = new Vector();
            while(rs.next())
                vec.addElement(new Long(rs.getLong("ent_id")));
            stmt.close();
            return vec;
        }

    public static String getEntityType(Connection con, long entId) throws SQLException/*, cwSysMessage*/ {
        String retValue = "";

        String SQL = " SELECT ent_type FROM ENTITY "
                    + " WHERE ent_id = ? ";
            
        int index = 1;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(index++, entId);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            retValue = rs.getString("ent_type");
        } else {
// keep api unchange 
//            throw new cwSysMessage("GEN005", "Entity id: " + entId);
            ;
        }
            
        stmt.close();
        return retValue;
    }
    
public boolean checkSteUidExist(Connection con, String uid)
        throws SQLException {
            
            String SQL = " SELECT ent_id FROM Entity "
                       + " WHERE ent_ste_uid = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() )
                flag = true;
            stmt.close();
            return flag;
        }    

    public String getSupervisorsAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(128);
        ViewSuperviseTargetEntity supervise = new ViewSuperviseTargetEntity();
        Vector vSupervisor = supervise.getGroupSupervisors(con, this.ent_id);
        xmlBuf.append("<supervisor_list>");
        for(int i=0; i<vSupervisor.size(); i++) {
            ViewSuperviseTargetEntity.ViewDirectSupervisor supervisor = (ViewSuperviseTargetEntity.ViewDirectSupervisor) vSupervisor.elementAt(i);
            xmlBuf.append("<supervisor ent_id=\"").append(supervisor.usr_ent_id).append("\"")
                  .append(" usr_id=\"").append(supervisor.usr_ste_usr_id).append("\">")
                  .append("<display_bil>").append(cwUtils.esc4XML(supervisor.usr_display_bil)).append("</display_bil>")
                  .append("</supervisor>");
        }
        xmlBuf.append("</supervisor_list>");
        return xmlBuf.toString();
    }
    
    public static Timestamp getEntityDeleteTimestamp(Connection con, long ent_id) throws SQLException {
        Timestamp deleteTime = null;
        String SQL = " SELECT ent_delete_timestamp FROM Entity "
                   + " WHERE ent_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ent_id);
        ResultSet rs = stmt.executeQuery();
        if( rs.next() )
        	deleteTime = rs.getTimestamp("ent_delete_timestamp");
        stmt.close();
        return deleteTime;
    }
    
    public static Timestamp getUpdDate(Connection con, long entId) throws SQLException {
    	Timestamp lastUpdDate = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
			stmt = con.prepareStatement(" select ent_upd_date from entity where ent_id = ? ");
			stmt.setLong(1, entId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				lastUpdDate = rs.getTimestamp("ent_upd_date");
			}
			stmt.close();
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return lastUpdDate;
	}

	public void upd3(Connection con) throws qdbException, qdbErrMessage {
		try {

			PreparedStatement stmt = con.prepareStatement("UPDATE Entity SET "
					+ " ent_ste_uid = ? " + " WHERE ent_id = ? ");

			stmt.setString(1, ent_ste_uid);
			stmt.setLong(2, ent_id);

			/* update */
			if (stmt.executeUpdate() != 1) {
				// update fails, rollback
				stmt.close();
				con.rollback();
				throw new qdbException("Fails to update user record.");
			}
			stmt.close();

		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}

		return;
	}
}