package com.cw.wizbank.ae.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;

public class ViewItemRelation{
    public static String[] getItemRelationTitle(Connection con, long ste_ent_id, String itm_code)
        throws SQLException {
        String[] title = new String[2];
        PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRelation());
        int idx = 1;
        stmt.setLong(idx++, ste_ent_id);
        stmt.setString(idx++, itm_code);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            title[0] = rs.getString("C_ITM_TITLE");
            title[1] = rs.getString("P_ITM_TITLE");
        }
        stmt.close();
        return title;
    }
}
