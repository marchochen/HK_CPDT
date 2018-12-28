package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.db.DbKmNodeAssignment;

public class ViewKmNodeAssignment {
    
    public long nam_ent_id;
    public String nam_display_bil;
    
    private final static String SQL_GET_NODE_ASSIGNED_ENTITY =
        " SELECT nam_ent_id, usr_display_bil AS DISP "
      + " From kmNodeAssignment, RegUser "
      + " WHERE nam_nod_id = ? AND usr_ent_id = nam_ent_id AND nam_type = ? " 
      + " UNION "
      + " SELECT nam_ent_id, usg_display_bil AS DISP "
      + " From kmNodeAssignment, UserGroup "
      + " WHERE nam_nod_id = ? AND usg_ent_id = nam_ent_id AND nam_type = ? " 
      + " ORDER BY DISP ";
    
    
    

    
    
    public static Vector getNodeAssignedEntity(Connection con, long nam_nod_id)
        throws SQLException {
            return getNodeAssignmentEntity(con, nam_nod_id, DbKmNodeAssignment.NODE_ASSIGNMENT_TYPE_ASSIGN);
        }

    public static Vector getNodeSelfAddEntity(Connection con, long nam_nod_id)
        throws SQLException {
            return getNodeAssignmentEntity(con, nam_nod_id, DbKmNodeAssignment.NODE_ASSIGNMENT_TYPE_SELFADD);
        }

    public static Vector getNodeAssignmentEntity(Connection con, long nam_nod_id, String nam_type)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_NODE_ASSIGNED_ENTITY);
            int index = 1;
            stmt.setLong(index++, nam_nod_id);
            stmt.setString(index++, nam_type);
            stmt.setLong(index++, nam_nod_id);
            stmt.setString(index++, nam_type);
            ResultSet rs = stmt.executeQuery();
            Vector v_nodeAss = new Vector();            
            while(rs.next()){
                ViewKmNodeAssignment viewNodeAss = new ViewKmNodeAssignment();
                viewNodeAss.nam_ent_id = rs.getLong("nam_ent_id");
                viewNodeAss.nam_display_bil = rs.getString("DISP");
                v_nodeAss.addElement(viewNodeAss);
            }
            stmt.close();
            return v_nodeAss;
        }
    
    
    public static ResultSet getMyWorkplace(Connection con, long usr_ent_id, String usrGroup)
        throws SQLException {

        String SQL_GET_MY_WORKPLACE = 
        " Select nam_nod_id, nam_type, fld_type, fld_title, fld_desc "
      + " From kmNodeAssignment, kmFolder "
      + " Where nam_nod_id = fld_nod_id "
      + " AND ( nam_ent_id = ? OR nam_ent_id IN " + usrGroup + " ) "
      + " Order by nam_type, fld_title ";
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_MY_WORKPLACE);
            int index = 1;
            stmt.setLong(index++, usr_ent_id);
            return stmt.executeQuery();

        }
    
}