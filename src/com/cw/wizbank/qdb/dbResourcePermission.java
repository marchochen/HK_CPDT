package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;


public class dbResourcePermission
{
    public static final String RIGHT_READ = "read";
    public static final String RIGHT_WRITE = "write";
    public static final String RIGHT_EXECUTE = "execute";
    
    public static final long SHARE_RES = 0;
    public static final long ALL_RES_ID = 0;
    public static final String ALL_RES_TYPE = "(0)";

    public static final String NO_RIGHT_READ_MSG = "RPM001";    //"You don't have permission to read the record.";
    public static final String NO_RIGHT_WRITE_MSG = "RPM002";   //"You don't have permission to modify the record.";
    public static final String NO_RIGHT_EXECUTE_MSG = "RPM003";  //"You don't have permission to attempt the module.";
    public static final String NO_RIGHT_INS_MSG = "RPM005"; //"You don't have permission to create module
        
    public long         rpm_res_id;
    public long         rpm_ent_id;
    public String       rpm_rol_ext_id;
    public boolean      rpm_read;
    public boolean      rpm_write;
    public boolean      rpm_execute;
    
    public dbResourcePermission() {;} 
    
    public static void save(Connection con, long resId, String usrId, String rol_ext_id, boolean _read, boolean _write, boolean _execute) 
        throws qdbException
    {
        long entId = dbRegUser.getEntId(con,usrId);
        
        save(con, resId, entId, rol_ext_id, _read, _write, _execute);
    }
    
    public static void save(Connection con, long resId, long entId, String rol_ext_id, boolean _read, boolean _write, boolean _execute) 
        throws qdbException
    {
        try{
            String SQL = "";
            boolean recExist = false;
            
            PreparedStatement stmt = con.prepareStatement(
                " SELECT count(rpm_res_id) FROM ResourcePermission " 
                + "  WHERE rpm_res_id = ? and rpm_ent_id = ? and rpm_rol_ext_id = ? ");

            stmt.setLong(1, resId);
            stmt.setLong(2, entId);
            stmt.setString(3, rol_ext_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    recExist = true;        
                }
            }
            rs.close();
            stmt.close();
            
            if (!recExist) {
                SQL =  "      INSERT INTO ResourcePermission "
                    + "         (rpm_read "
                    + "         ,rpm_write "
                    + "         ,rpm_execute "
                    + "         ,rpm_res_id "
                    + "         ,rpm_ent_id "
                    + "         ,rpm_rol_ext_id) "
                    + "      VALUES (?,?,?,?,?,?) ";
            }else {
                SQL = " UPDATE ResourcePermission SET "
                    + "          rpm_read = ? "
                    + "          ,rpm_write = ? "
                    + "          ,rpm_execute = ? "
                    + "      WHERE rpm_res_id = ? and rpm_ent_id = ? and rpm_rol_ext_id = ? ";
            }
            
            stmt = con.prepareStatement(SQL);
            stmt.setBoolean(1, _read);
            stmt.setBoolean(2, _write);
            stmt.setBoolean(3, _execute);
            stmt.setLong(4, resId);
            stmt.setLong(5, entId);
            stmt.setString(6, rol_ext_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                throw new qdbException("Failed to update resourcepermission."); 
            }          
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static void del(Connection con, long resId, String usrId, String rol_ext_id) 
        throws qdbException
    {
        long entId = dbRegUser.getEntId(con,usrId);
        
        del(con, resId, entId, rol_ext_id);
    }
    
    public static void del(Connection con, long resId, long entId, String rol_ext_id) 
        throws qdbException
    {
        try{
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? "
                + "        AND rpm_ent_id = ? " 
                + "        AND rpm_rol_ext_id = ? ");

            stmt.setLong(1, resId);
            stmt.setLong(2, entId);
            stmt.setString(3, rol_ext_id);
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static void del(Connection con, long resId, String usrId) 
        throws qdbException
    {
        long entId = dbRegUser.getEntId(con,usrId);
        
        del(con, resId, entId);
    }
    
    public static void del(Connection con, long resId, long entId) 
        throws qdbException
    {
        try{
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? "
                + "        AND rpm_ent_id = ? " );

            stmt.setLong(1, resId);
            stmt.setLong(2, entId);
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static void delByRoleNResId(Connection con, long resId, String rol_ext_id)
        throws qdbException
    {
        try{
            // Delete all users from the reosource permission except 
            // those enrolled in the course
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? "
                + "        AND rpm_rol_ext_id = ? ");

            stmt.setLong(1, resId);
            stmt.setString(2, rol_ext_id);

            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static void delUserRoleIsNull(Connection con, long resId)
        throws qdbException
    {
        try{
            // Delete all users from the reosource permission except 
            // those enrolled in the course
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? "
                + "        AND rpm_rol_ext_id is null "
                + "        AND EXISTS (SELECT usr_ent_id FROM RegUser "
                + "                    WHERE usr_ent_id = rpm_ent_id) ");

            stmt.setLong(1, resId);
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static void delRoleIsNull(Connection con, long resId)
        throws qdbException
    {
        try{
            // Delete all users from the reosource permission except 
            // those enrolled in the course
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? "
                + "        AND rpm_rol_ext_id is null ");

            stmt.setLong(1, resId);
            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    /*
    public static void delCDNByResId(Connection con, long resId)
        throws qdbException
    {
        try{
            // Delete all users from the reosource permission except 
            // those enrolled in the course
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? "
                + "        AND rpm_ent_id NOT IN "
                + "   (SELECT enr_ent_id FROM ENROLMENT " 
                + "      WHERE enr_res_id = ? ) ");

            stmt.setLong(1, resId);
            stmt.setLong(2, resId);

            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    private static final String sql_del_rpm_by_rol_res = 
                          " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? "
                + "        AND rpm_ent_id IN "
                + "   (SELECT enr_ent_id FROM ENROLMENT " 
                + "      WHERE enr_res_id = ? ) "
                + "        AND rpm_ent_id IN "
                + "   (SELECT erl_ent_id FROM acEntityRole, acRole "
                + "        WHERE rol_ext_id = ? AND rol_id = erl_rol_id )";

    public static void delRoleByResId(Connection con, long resId, String rol_ext_id)
        throws qdbException
    {
        try{
            // Delete all users from the reosource permission who
            // are not enrolled in the course and have the input role
            PreparedStatement stmt = con.prepareStatement(sql_del_rpm_by_rol_res);
            stmt.setLong(1, resId);
            stmt.setLong(2, resId);
            stmt.setString(3, rol_ext_id);

            stmt.executeUpdate();
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    */
    
    public static void assign(Connection con, long resId, long entId, String rol_ext_id, String right_type, boolean val) 
        throws qdbException
    {
        try{
            String SQL = ""; 
            
            PreparedStatement stmt = con.prepareStatement(
                 "  SELECT rpm_read, rpm_write, rpm_execute FROM " 
                +"    ResourcePermission WHERE " 
                +"  rpm_res_id = ? AND rpm_ent_id = ? AND " 
                +"  rpm_rol_ext_id = ? "); 
            stmt.setLong(1, resId);
            stmt.setLong(2, entId);
            stmt.setString(3, rol_ext_id);
            
            boolean _read = false; 
            boolean _write = false;
            boolean _execute = false; 
                    
                
            ResultSet rs = stmt.executeQuery(); 
            if (rs.next()) {
                _read = rs.getBoolean("rpm_read"); 
                _write = rs.getBoolean("rpm_write"); 
                _execute = rs.getBoolean("rpm_execute"); 
                
                if (right_type.equalsIgnoreCase(RIGHT_READ)) {
                    _read = val;     
                }else if (right_type.equalsIgnoreCase(RIGHT_WRITE)) {
                    _write = val; 
                }else {
                    _execute = val; 
                }
                
                if (_read == false && _write == false && _execute == false)
                    del(con, resId, entId);
                else {
                    SQL = " UPDATE ResourcePermission SET " 
                       + "      rpm_read = ? " 
                       + "     ,rpm_write = ? " 
                       + "     ,rpm_execute = ? " 
                       + "  WHERE rpm_res_id = ? " 
                       + "    AND rpm_ent_id = ? " 
                       + "    AND rpm_rol_ext_id = ? "; 

                    PreparedStatement stmt1 = con.prepareStatement(SQL); 
                    stmt1.setBoolean(1,_read);
                    stmt1.setBoolean(2,_write);
                    stmt1.setBoolean(3,_execute);
                    stmt1.setLong(4,resId);
                    stmt1.setLong(5,entId);
                    stmt1.setString(6,rol_ext_id);
                    int stmtResult=stmt1.executeUpdate();
                    stmt1.close();
                    if ( stmtResult!=1)                            
                    {
                        con.rollback();
                        throw new qdbException("Failed to update resource permission." ); 
                    }
                      
                }
                
            }else if (val){
                SQL = " INSERT INTO ResourcePermission "
                   + " (rpm_res_id, rpm_ent_id, rpm_rol_ext_id, rpm_read, rpm_write, rpm_execute) "
                   + " VALUES " 
                   + " (?,?,?,?,?,?) " ; 
                
                if (right_type.equalsIgnoreCase(RIGHT_READ)) {
                    _read = val;     
                }else if (right_type.equalsIgnoreCase(RIGHT_WRITE)) {
                    _write = val; 
                }else {
                    _execute = val; 
                }
    
                PreparedStatement stmt2 = con.prepareStatement(SQL); 
                stmt2.setLong(1,resId);
                stmt2.setLong(2,entId);
                stmt2.setString(3, rol_ext_id);
                stmt2.setBoolean(3,_read);
                stmt2.setBoolean(4,_write);
                stmt2.setBoolean(5,_execute);
                int stmtResult=stmt2.executeUpdate();
                stmt2.close();
                if ( stmtResult!=1)
                {                
                    con.rollback();
                    throw new qdbException("Failed to insert resource permission."); 
                }                
            }
            
            stmt.close();
                        
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static void delAll(Connection con, long resId) 
        throws qdbException
    {
        try{
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From ResourcePermission  WHERE "
                + "            rpm_res_id = ? " );
                
            stmt.setLong(1, resId);

            stmt.executeUpdate(); 
            stmt.close();            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static String aclAsXML(Connection con, String res_type, loginProfile prof)
        throws qdbException
    {
        Vector resIdVec = new Vector(); 
        resIdVec.addElement(new Long(ALL_RES_ID)); 
        return (aclAsXML(con,resIdVec, res_type, prof)); 
    }
    
    public static String aclAsXML(Connection con, long resId, loginProfile prof)
        throws qdbException
    {
        Vector resIdVec = new Vector(); 
        resIdVec.addElement(new Long(resId)); 
        return (aclAsXML(con,resIdVec, ALL_RES_TYPE, prof));
    }
    
    public static String aclAsXML(Connection con, Vector resIdVec, loginProfile prof)
        throws qdbException
    {
        String xml = "";
        if (resIdVec.size() ==0) 
            return xml;
        else 
            return (aclAsXML(con,resIdVec, ALL_RES_TYPE, prof));
    }
    
    public static String aclAsXML(Connection con, Vector resIds, String res_type, loginProfile prof)
        throws qdbException
    {
        String result = "";
        
        Vector rpmVec =  getResPermission(con, resIds, res_type, prof);
        
        for(int i=0;i<rpmVec.size();i++) {
            dbResourcePermission dbrpm = (dbResourcePermission) rpmVec.elementAt(i); 
        
            result += "<acl id=\"" +  dbrpm.rpm_res_id 
                    + "\" ent_id=\"" + dbrpm.rpm_ent_id
                    + "\" read=\"" + dbrpm.rpm_read
                    + "\" write=\"" + dbrpm.rpm_write
                    + "\" execute=\"" + dbrpm.rpm_execute
                    + "\" />" + dbUtils.NEWL;
           
        }
        result += dbUtils.NEWL;
        
        return (result);
    }


    public static boolean hasPermission(Connection con,long resId, loginProfile prof, String right)
        throws qdbException
    {
        try {
            PreparedStatement stmt = null ; 
            
            /*
            if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) {
                
                    stmt = con.prepareStatement(
                        " SELECT res_usr_id_owner, res_privilege FROM Resources " 
                    +"   WHERE res_id = ? ");

                    stmt.setLong(1,resId);
                    ResultSet rs = stmt.executeQuery(); 
                    if(rs.next())  {
                        if (rs.getString("res_privilege").equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
                            long rootId = dbRegUser.getRootGpId(con, rs.getString("res_usr_id_owner")); 
                            if (rootId == prof.root_ent_id) 
                                return true;
                        }
                    }else {
                        con.rollback(); 
                        throw new qdbException("Failed to get resource id = " + resId); 
                    }
                    stmt.close();
            }
            */
            
            String gpIds = prof.usrGroupsList();

            String SQL = "  SELECT rpm_read, rpm_write, rpm_execute "
                      + "    FROM ResourcePermission "
                      + "    WHERE rpm_res_id = ?  AND "
                      + "          rpm_ent_id IN " + gpIds + " AND"
                      + "          (rpm_rol_ext_id = ? OR rpm_rol_ext_id is null)";
            
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1,resId);
            stmt.setString(2,prof.current_role);
            
            ResultSet rs = stmt.executeQuery();
            
            
            boolean _read       = false;
            boolean _write      = false;
            boolean _execute    = false;
            
            while(rs.next()) {
                if (rs.getBoolean("rpm_read"))
                    _read = true;
                if (rs.getBoolean("rpm_write"))
                    _write = true;
                if (rs.getBoolean("rpm_execute"))
                    _execute = true;
            }
            
            stmt.close();
            
            if (right.equalsIgnoreCase(RIGHT_READ)) {
                return (_read);
            }else if (right.equalsIgnoreCase(RIGHT_WRITE)) {
                return (_write);
            }else if (right.equalsIgnoreCase(RIGHT_EXECUTE)) {
                return (_execute);
            }else 
                return false;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }

    }

    public static String getResourceList(Connection con, String res_type, loginProfile prof)
        throws qdbException
    {
        String result = "";
        Vector rpmVec = getResPermission(con, res_type, prof); 
        result = convertId2List(rpmVec);
        
        return(result);
    }

    
    // Check all the resource that a user have permission
    public static Vector getResPermission(Connection con, String res_type, loginProfile prof)
        throws qdbException
    {
        if (res_type == null || res_type.length() ==0)
            return (new Vector());
            
        else {
            Vector resIdVec = new Vector(); 
            resIdVec.addElement(new Long(ALL_RES_ID)); 
            return(getResPermission(con, resIdVec, res_type,prof));
        }
    }
    
    // Check the permission of a resource 
    public static Vector getResPermission(Connection con, long resId, loginProfile prof)
        throws qdbException
    {
        Vector resIdVec = new Vector(); 
        resIdVec.addElement(new Long(resId)); 
        return(getResPermission(con, resIdVec , ALL_RES_TYPE, prof));
    }
    
    // Check the permission of a  list of resource 
    public static Vector getResPermission(Connection con, Vector resIdVec, loginProfile prof)
        throws qdbException
    {
        if (resIdVec.size() ==0) 
            return (new Vector());
        else 
            return(getResPermission(con, resIdVec , ALL_RES_TYPE, prof));
    }
    
    public static Vector getResPermission(Connection con, Vector resIdVec, String res_type, loginProfile prof)
        throws qdbException
    {
        try{
            
            if (resIdVec.size() ==0)
                return (new Vector());
                
            if (res_type.equalsIgnoreCase(ALL_RES_TYPE) && 
                (((Long) resIdVec.elementAt(0)).longValue() == ALL_RES_ID))
                return (new Vector());

            String gpIds = prof.usrGroupsList();
            
            Vector resIdV = new Vector();
            Vector readV = new Vector();
            Vector writeV = new Vector();
            Vector executeV = new Vector();
            
            
            String SQL = "" ; 
            SQL =  " SELECT rpm_res_id, rpm_read, rpm_write, rpm_execute "
              + "    FROM ResourcePermission "
              + "       WHERE rpm_ent_id IN " + gpIds 
              + "       AND (rpm_rol_ext_id is null OR rpm_rol_ext_id = ?) ";
            
            // given resource id
            String tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
            
            if (resIdVec.size() > 1 ||
               (((Long) resIdVec.elementAt(0)).longValue() != ALL_RES_ID)) {
                //String resIds = dbUtils.vec2list(resIdVec);
                //SQL += "  AND rpm_res_id IN " + resIds ;
                
                cwSQL.insertSimpleTempTable(con, tableName, resIdVec, cwSQL.COL_TYPE_LONG);
                SQL += " AND rpm_res_id IN ( SELECT tmp_res_id FROM " + tableName + " ) " ;

            }
            
            // given resource type
            if (!res_type.equalsIgnoreCase(ALL_RES_TYPE)) {
              SQL +=  "        AND rpm_res_id IN  " 
                  + "          (select res_id FROM Resources "
                   + "                  WHERE res_type = '" + res_type + "') " ; 
            }
            
            SQL += "  order by rpm_res_id "; 
            
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, prof.current_role);              
            Vector result = new Vector(); 
            ResultSet rs = stmt.executeQuery(); 
            while(rs.next()) {
                Long _resId  = new Long(rs.getLong("rpm_res_id")); 
                Boolean _read  = new Boolean(rs.getBoolean("rpm_read")); 
                Boolean _write  = new Boolean(rs.getBoolean("rpm_write")); 
                Boolean _execute  = new Boolean(rs.getBoolean("rpm_execute")); 
                int index = resIdV.indexOf(_resId);
                if (index >= 0) {
                    if (_read.booleanValue())
                        readV.setElementAt(_read, index);
                    if (_write.booleanValue())
                        writeV.setElementAt(_write, index);
                    if (_execute.booleanValue())
                        executeV.setElementAt(_execute, index);
                }else {
                    resIdV.addElement(_resId); 
                    readV.addElement(_read); 
                    writeV.addElement(_write); 
                    executeV.addElement(_execute); 
                }
            }
            
            stmt.close();
            
            cwSQL.dropTempTable(con,tableName);
            
            if (((Long) resIdVec.elementAt(0)).longValue() != ALL_RES_ID) {
                int index = -1; 
                for (int i=0;i< resIdVec.size();i++) {
                    dbResourcePermission dbrpm = new dbResourcePermission();
                    dbrpm.rpm_res_id = ((Long) resIdVec.elementAt(i)).longValue();
                    dbrpm.rpm_ent_id = prof.usr_ent_id;
                    
                    index = resIdV.indexOf(resIdVec.elementAt(i)); 
                    if (index >= 0)  {
                        dbrpm.rpm_read = ((Boolean) readV.elementAt(index)).booleanValue();
                        dbrpm.rpm_write = ((Boolean) writeV.elementAt(index)).booleanValue();
                        dbrpm.rpm_execute = ((Boolean) executeV.elementAt(index)).booleanValue();
                    }else {
                        dbrpm.rpm_read = false; 
                        dbrpm.rpm_write = false;
                        dbrpm.rpm_execute = false; 
                    }
                    result.addElement(dbrpm); 
                }
            }else {
                for (int i=0;i< resIdV.size();i++) {
                    dbResourcePermission dbrpm = new dbResourcePermission();
                    dbrpm.rpm_res_id = ((Long) resIdV.elementAt(i)).longValue();
                    dbrpm.rpm_ent_id = prof.usr_ent_id;
                    
                    dbrpm.rpm_read = ((Boolean) readV.elementAt(i)).booleanValue();
                    dbrpm.rpm_write = ((Boolean) writeV.elementAt(i)).booleanValue();
                    dbrpm.rpm_execute = ((Boolean) executeV.elementAt(i)).booleanValue();

                    result.addElement(dbrpm); 
                }
            }
            return result ; 
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static Vector getModeratorLst(Connection con, long resId)
        throws qdbException
    {
        try{
            Vector vtEnt = new Vector();
            
            String SQL = "" ; 
            SQL =  " SELECT rpm_ent_id "
              + "    FROM ResourcePermission, entity "
              + "       WHERE rpm_res_id = ? " 
              + "       AND ent_id = rpm_ent_id "
              + "       AND ent_delete_usr_id IS NULL "
              + "       AND ent_delete_timestamp IS NULL "
              + "       AND ent_type = ? "
              + "       AND (rpm_rol_ext_id is null ) "
              + "  order by rpm_res_id "; 
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, resId);              
            stmt.setString(2, dbEntity.ENT_TYPE_USER);              
            Vector result = new Vector(); 
            ResultSet rs = stmt.executeQuery(); 
            while(rs.next()) {
                vtEnt.addElement(new Long(rs.getLong("rpm_ent_id")));
            }
            stmt.close();
            
            return vtEnt ; 
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static String convertId2List(Vector rpmVec)
    {
        String result = "(0" ; 
        for (int i=0;i< rpmVec.size();i++) 
        {
            dbResourcePermission dbrpm = (dbResourcePermission) rpmVec.elementAt(i); 
            result += ", " +  dbrpm.rpm_res_id; 
        }
        result += ")";
        
        return result ; 
    }

    public static String asXML(Vector rpmVec)
    {
        String result = "";
        for (int i=0;i< rpmVec.size();i++) 
        {
            dbResourcePermission dbrpm = (dbResourcePermission) rpmVec.elementAt(i); 
            result += "<acl id=\"" +  dbrpm.rpm_res_id 
                    + "\" ent_id=\"" + dbrpm.rpm_ent_id
                    + "\" read=\"" + dbrpm.rpm_read
                    + "\" write=\"" + dbrpm.rpm_write
                    + "\" execute=\"" + dbrpm.rpm_execute
                    + "\" />" + dbUtils.NEWL;
        }
        
        return result ; 
    }
    
    public static int delAllByEntId(Connection con, long entId)
        throws qdbException
    {
        try {  
            
            PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ResourcePermission WHERE "
                + "     rpm_ent_id = ? " ); 
            
            stmt.setLong(1, entId);
            int cnt = stmt.executeUpdate();
            stmt.close();
            return cnt;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    
    /**
    * Get entity id list which have permission on the resource
    * @param database connection
    * @param resource id
    * @param read permission, < 0 not check
    * @param write permission, < 0 not check
    * @param exec permission, < 0 not check
    * @return Vector of entity id
    */
    public Vector getPermissionEntityList(Connection con, long res_id, int read, int write, int exec)
        throws SQLException, cwException {
            
            String SQL = " SELECT rpm_ent_id "
                       + " FROM ResourcePermission "
                       + " WHERE rpm_res_id = ? ";
                       if( read >= 0 )
                        SQL += " AND rpm_read = ? ";
                       if( write >= 0 )
                        SQL += " AND rpm_write = ? ";
                       if( exec >= 0 )
                        SQL += " AND execute = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, res_id);
            if( read == 0 )
                stmt.setBoolean(index++, false);
            else if( read == 1 )
                stmt.setBoolean(index++, true);
            if( write == 0 )
                stmt.setBoolean(index++, false);
            else if( write == 1 )
                stmt.setBoolean(index++, true);
            if( exec ==0 )
                stmt.setBoolean(index++, false);
            else if ( exec == 1 )
                stmt.setBoolean(index++, true);
            
            ResultSet rs = stmt.executeQuery();
            Vector entIdVec = new Vector();
            while( rs.next() ) {
                entIdVec.addElement(new Long(rs.getLong("rpm_ent_id")));
            }
            stmt.close();
            return entIdVec;
        }


    /**
    * Remove user group all permission from the resource
    * @param user group entity id
    */
    public void delGroup(Connection con, Vector grpIdVec)
        throws SQLException, cwException {
            
            String SQL = " DELETE FROM ResourcePermission "
                       + " WHERE rpm_res_id = ? "
                       + " AND rpm_ent_id IN " + cwUtils.vector2list(grpIdVec);
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, rpm_res_id);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }

}