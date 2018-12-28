package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

/**
A database class to manage table kmLink
*/
public class DbKmLink extends DbKmNode{

    /* Possible values for lnk_type */
    public static final String LINK_TYPE_OBJECT = "OBJECT";
    public static final String LINK_TYPE_DOMAIN = "DOMAIN";
    public static final String LINK_TYPE_WORK = "WORK";
    
    public long     lnk_nod_id;
    public String   lnk_type;
    public String   lnk_title;
    public long     lnk_target_nod_id;
    
    private static final String SQL_INS_KMLINK  = " INSERT INTO kmLink ( " 
                                                + "  lnk_nod_id, lnk_type, "
                                                + "  lnk_title, lnk_target_nod_id " 
                                                + "  ) "
                                                + " VALUES (?,?,?,?) ";

    private static final String SQL_GET_KMLINK_BY_TARGET  = " SELECT lnk_nod_id FROM kmLink WHERE " 
                                                + "  lnk_target_nod_id = ? ";

    private static final String SQL_DEL_KMLINK  = " DELETE FROM kmLink WHERE " 
                                                + "  lnk_nod_id IN ";

    /**
    Inser a new link
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException, cwSysMessage {
        
        super.ins(con);
        lnk_nod_id = nod_id;

        PreparedStatement stmt = null;
        int code = 0;
        try {
            //try to insert 
            int index =1;
            stmt = con.prepareStatement(SQL_INS_KMLINK);
            stmt.setLong(index++, lnk_nod_id);
            stmt.setString(index++, lnk_type);
            stmt.setString(index++, lnk_title);
            stmt.setLong(index++, lnk_target_nod_id);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }

    /**
    Delete all the link that are targeted to the node
    @param the id of the targeted node
    @return the ROW COUNT of affected record
    */
    public static int delTarget(Connection con, long target_nod_id)
        throws SQLException {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            //try to insert 
            stmt = con.prepareStatement(SQL_GET_KMLINK_BY_TARGET);
            stmt.setLong(1, target_nod_id);
            Vector idVec = new Vector();
            ResultSet rs  = stmt.executeQuery();
            while (rs.next()) {
                idVec.addElement(new Long(rs.getLong("lnk_nod_id")));
            }
            rs.close();
            
            if (idVec.size() > 0) {
                String id_lst = cwUtils.vector2list(idVec);
                stmt = con.prepareStatement(SQL_DEL_KMLINK + id_lst);
                code = stmt.executeUpdate();
                DbKmNode.delAll(con, idVec);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }


}


