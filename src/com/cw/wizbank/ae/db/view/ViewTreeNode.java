package com.cw.wizbank.ae.db.view;

import java.util.Vector;
import java.sql.*;
import com.cw.wizbank.util.cwUtils;


public class ViewTreeNode{
    
    
    public Vector getTndChildId(Connection con, long v_tnd_id, Vector v_itm_id, Vector v_itm_type, boolean all_itm)
        throws SQLException {
            
            String SQL = " SELECT inp_itm_id "
                       + " FROM aeItemTreeNodePath ";
                       if( v_itm_type != null && !v_itm_type.isEmpty() )
                            SQL += " , aeItem ";
                   SQL += " WHERE inp_ancester LIKE ? ";
                       if( v_itm_id != null && !v_itm_id.isEmpty() )
                            SQL += " And inp_itm_id IN " + cwUtils.vector2list(v_itm_id);
                       if( v_itm_type != null && !v_itm_type.isEmpty() ) {
                            SQL += " And inp_itm_id = itm_id And itm_type In ( ? ";
                            for(int i=1; i<v_itm_type.size(); i++)
                                SQL += ", ? ";
                            SQL += " ) ";
                       }
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            if( all_itm )
                stmt.setString(index++, "% " + v_tnd_id + " %");
            else
                stmt.setString(index++, "% " + v_tnd_id + " ");
            if( v_itm_type != null && !v_itm_type.isEmpty() )
                for(int i=0; i<v_itm_type.size(); i++)
                    stmt.setString(index++, (String)v_itm_type.elementAt(i));
            ResultSet rs = stmt.executeQuery();
            Vector v_id = new Vector();
            while(rs.next())
                v_id.addElement(new Long(rs.getLong("inp_itm_id")));
                
            stmt.close();
            return v_id;
        }
    
}