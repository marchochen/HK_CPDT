package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.util.cwSQL;

public class dbSyllabus 
{
    public static final String PUBLIC_PRIV = "PUBLIC";
    public static final String AUTHOR_PRIV = "AUTHOR";
    
//    public static final String FCN_SYB_MGT = "SYB_MGT";
    public static final String NO_RIGHT_MGT = "SYB001";
    
    public long syl_id; 
    public String syl_desc;
    public String syl_locale;
    public String syl_privilege; 
    public long syl_ent_id_root; 

    public dbSyllabus() {;}

    
    
    
    //check if there are any objective other than Recycle bin under this Syllabus
    //pre-set var: syl_id
    private boolean hasChild(Connection con) throws SQLException {
        long count = 0;
        boolean result;
        final String SQL = " Select count(*) from Objective "
                         + "   where obj_syl_id = ? and obj_type != ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, syl_id);
        stmt.setString(2, dbObjective.OBJ_TYPE_SYS);
        
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            count = rs.getLong(1);
        stmt.close();
        if(count > 0)
            result = true;
        else
            result = false;
        
        return result;        
    }

    //get the objective id of the  Recycle bin under this Syllabus
    //pre-set var: syl_id
    private long getLostFoundID(Connection con) throws SQLException {
        long count = 0;
        boolean result;
        final String SQL = " Select obj_id from Objective "
                         + "   where obj_syl_id = ? and obj_type = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, syl_id);
        stmt.setString(2, dbObjective.OBJ_TYPE_SYS);
        
        ResultSet rs = stmt.executeQuery();
        long lf_obj_id = 0;
        if(rs.next())
            lf_obj_id = rs.getLong(1);
        
        stmt.close();
        
        return lf_obj_id;
    }

    
    public void delSyllabus(Connection con, loginProfile prof) throws qdbException, qdbErrMessage {
        try {
//            AccessControlWZB acl = new AccessControlWZB();
//            if (!acl.hasUserPrivilege(con, prof.current_role, FCN_SYB_MGT)) {
//                // No right to maintain Syllabus
//                throw new qdbErrMessage(NO_RIGHT_MGT);
//            }
            if(hasChild(con))
                throw new qdbErrMessage("SYB002");
    
            long lf_obj_id = getLostFoundID(con);
            if (lf_obj_id > 0) {
                dbObjective dbobj = new dbObjective();
                dbobj.obj_id = lf_obj_id;
                if (dbobj.hasChild(con)) {
                    throw new qdbErrMessage("SYB002");
                }
                
                String SQL = "Delete From Objective where obj_id = ? ";

                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, lf_obj_id);
                int rowAffected = stmt.executeUpdate();
                stmt.close();
                if(rowAffected != 1) {
                    throw new qdbException("Failed to delete Recycle Bin " + lf_obj_id);
                }

            }
            
            String SQL = " Delete From Syllabus "
                       + " Where syl_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, syl_id);
            int rowAffected = stmt.executeUpdate();
            stmt.close();
            if(rowAffected != 1) {
                throw new qdbException("Failed to delete Syllabus " + syl_id);
            }
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    
    
   
    
    public String getSybAsXML(Connection con, loginProfile prof) throws qdbException {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<syllabus_list>" + dbUtils.NEWL;
        result += prof.asXML() + dbUtils.NEWL;

        result += "<header>" + dbUtils.NEWL;
        result += "<locale>" + syl_locale + "</locale>" + dbUtils.NEWL;
        result += "</header>" + dbUtils.NEWL;

        String bodyXML = "";

        String SQL = " SELECT syl_id             SID  "
          + "                ,syl_privilege      PRIV "
          + "                ,syl_ent_id_root    EIR  "
          + "                ,syl_desc           DESP "
          + " FROM Syllabus WHERE " 
          + " syl_id = ? ";
        
        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, syl_id);

            ResultSet rs = stmt.executeQuery();
            long sid,eir;      
            String desc = ""; 
            String priv= "";
            String cur_desc = "";
            if(rs.next())
            {
                sid = rs.getLong("SID");
                eir = rs.getLong("EIR");
                desc = rs.getString("DESP");
                priv = rs.getString("PRIV");
                
//                if( !cur_desc.equalsIgnoreCase(desc) )
//                {
                    if (!cur_desc.equalsIgnoreCase(""))   
                        bodyXML += "</syllabus>" + dbUtils.NEWL ; 
                    
                    cur_desc = desc;
                    bodyXML += "<syllabus id=\"" + sid + "\" privilege=\"" + priv + "\" root_ent_id=\"" + eir + "\">" + dbUtils.NEWL;
                    bodyXML += "<desc>" + dbUtils.esc4XML(desc) + "</desc>" +  dbUtils.NEWL;
//                }
            }
              
            if (bodyXML.length()>0)
                bodyXML += "</syllabus>" + dbUtils.NEWL;
                
            result += "<body>" + dbUtils.NEWL;
            result += bodyXML;
            result += "</body>" + dbUtils.NEWL;
              
            result += "</syllabus_list>" + dbUtils.NEWL;
              
            stmt.close();
            return result;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static String getSybListAsXML(Connection con, String locale, loginProfile prof)
        throws qdbException
    {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<syllabus_list>" + dbUtils.NEWL;
        result += prof.asXML() + dbUtils.NEWL;
                
        result += "<header>" + dbUtils.NEWL;
        result += "<locale>" + locale + "</locale>" + dbUtils.NEWL;
        result += "</header>" + dbUtils.NEWL;

        String bodyXML = "";

        String SQL = " SELECT DISTINCT syl_id             SID  "
          + "                ,syl_privilege      PRIV "
          + "                ,syl_ent_id_root    EIR  "
          + "                ,syl_desc           DESP "
          + " FROM Syllabus WHERE " ; 
          
       if (locale != null && locale.length() > 0) {
         SQL += " syl_locale = '" + locale + "' AND ";
       }

        SQL += " syl_privilege = ? OR syl_ent_id_root = ? order by DESP ASC ";
/*        String mySQL = SQL 
                + " syl_privilege = ? " 
                + " UNION " 
                + SQL 
                + " syl_ent_id_root = ? "
                + " order by DESP ASC " ; */
        
      try {
          PreparedStatement stmt = con.prepareStatement(SQL);
          stmt.setString(1, dbResource.RES_PRIV_CW);
          stmt.setLong(2, prof.root_ent_id);
          
          ResultSet rs = stmt.executeQuery();
          long sid,eir;      
          String desc = ""; 
          String priv= "";
          String cur_desc = "";
          while(rs.next())
          {
            sid = rs.getLong("SID");
            eir = rs.getLong("EIR");
            desc = rs.getString("DESP");
            priv = rs.getString("PRIV");
            
//            if( !cur_desc.equalsIgnoreCase(desc) )
//            {
                if (!cur_desc.equalsIgnoreCase(""))   
                    bodyXML += "</syllabus>" + dbUtils.NEWL ; 
                
                cur_desc = desc;
                bodyXML += "<syllabus id=\"" + sid + "\" privilege=\"" + priv + "\" root_ent_id=\"" + eir + "\">" + dbUtils.NEWL;
                bodyXML += "<desc>" + dbUtils.esc4XML(desc) + "</desc>" +  dbUtils.NEWL;
//            }
          }
          
          if (bodyXML.length()>0)
            bodyXML += "</syllabus>" + dbUtils.NEWL;
            
          result += "<body>" + dbUtils.NEWL;
          result += bodyXML;
          result += "</body>" + dbUtils.NEWL;
          
          result += "</syllabus_list>" + dbUtils.NEWL;
          
          stmt.close();
          return result;
      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage()); 
      }
    }
    
    // New version of Knowlege Manager
    /*
    Get the top most syllabus for the user
    1. Syllabus belonging to his/her organization
    2. Syllabus belonging to the public organization
    */
    public static Vector getSybVec(Connection con, String privilege, long root_ent_id)
        throws SQLException
    {
        Vector sylVec = new Vector();
        StringBuffer sqlBuf = new StringBuffer();
        
        sqlBuf.append(" SELECT syl_id, syl_desc, syl_locale, syl_privilege, ")
              .append(" syl_ent_id_root FROM Syllabus WHERE ");
        
        if (privilege != null && privilege.equals(PUBLIC_PRIV)) {
            sqlBuf.append(" syl_privilege = '").append(PUBLIC_PRIV).append("' ");
        }else {
            sqlBuf.append(" syl_ent_id_root = ").append(root_ent_id);
        }
        
        sqlBuf.append(" ORDER BY syl_desc ");

        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            dbSyllabus syl = new dbSyllabus();
            syl.syl_id = rs.getLong("syl_id");
            syl.syl_desc = rs.getString("syl_desc");
            syl.syl_locale = rs.getString("syl_locale");
            syl.syl_privilege = rs.getString("syl_privilege");
            syl.syl_ent_id_root = rs.getLong("syl_ent_id_root");
            sylVec.addElement(syl);
        }
        stmt.close();
        return sylVec;

    }

    public void get(Connection con) throws qdbException {

        String SQL = " SELECT syl_privilege  "
          + "                ,syl_desc "
          + "                ,syl_locale "
          + "                ,syl_ent_id_root "
          + " FROM Syllabus WHERE " 
          + " syl_id = ? ";

        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, syl_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                syl_privilege = rs.getString("syl_privilege");
                syl_desc = rs.getString("syl_desc");
                syl_ent_id_root = rs.getLong("syl_ent_id_root");
                syl_locale = rs.getString("syl_locale");
                
            }else {
            	stmt.close();
                throw new qdbException ("Failed to get Syllabus.");
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

}
