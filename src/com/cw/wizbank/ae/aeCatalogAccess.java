package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;

public class aeCatalogAccess {
    private static final long CAC_ALL_ORGS = 0;
    
    static final String ENT_TYPE_USER = "USR";
    static final String ENT_TYPE_USER_GROUP = "USG";

    public long cac_ent_id;
    public long cac_cat_id;
    public String cac_create_usr_id;
    public Timestamp cac_create_timestamp;

    public String getAssignedSitesAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(512);
        PreparedStatement stmt = null;
        xmlBuf.append("<site_list>");
        aeCatalogAccess allOrgs = new aeCatalogAccess();
        allOrgs.cac_ent_id = CAC_ALL_ORGS;
        allOrgs.cac_cat_id = this.cac_cat_id;
        boolean allOrgsSelected = allOrgs.exists(con);
        xmlBuf.append("<site ent_id=\"").append(0).append("\"")
                .append(" selected=\"").append(allOrgsSelected).append("\">")
                .append("</site>");
        try {
            stmt = con.prepareStatement(OuterJoinSqlStatements.getAssignedSites());
            stmt.setLong(1, this.cac_cat_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                boolean selected = (rs.getLong("cac_id") > 0);
                xmlBuf.append("<site ent_id=\"").append(rs.getLong("ste_ent_id")).append("\"")
                      .append(" selected=\"").append(selected).append("\">")
                      .append("<name>").append(dbUtils.esc4XML(rs.getString("ste_name"))).append("</name>")
                      .append("</site>");
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        xmlBuf.append("</site_list>");
        return xmlBuf.toString();
    }

    //assume cac_cat_id is set
    public String getAssignEntityAsXML(Connection con, long owner_ent_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(800);

        //get the user groups have access right
        SQLBuf.append(" Select usg_ent_id AS ent_id, usg_display_bil AS display_bil, '").append(ENT_TYPE_USER_GROUP).append("' AS type, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" AS status ");
        SQLBuf.append(" From UserGroup, aeCatalogAccess ");
        SQLBuf.append(" Where ");
        SQLBuf.append(" cac_cat_id = ? ");
        SQLBuf.append(" And cac_ent_id = usg_ent_id ");
        SQLBuf.append(" And (usg_role <> ? or usg_role is null) ");
        SQLBuf.append(" And usg_ent_id not in ");
        SQLBuf.append(" (Select ern_child_ent_id from EntityRelation, userGroup where ");
        SQLBuf.append(" ern_ancestor_ent_id = usg_ent_id ");
        SQLBuf.append(" and usg_role = ? ");
        SQLBuf.append(" and usg_ent_id_root = ? ");
        SQLBuf.append(" AND ern_parent_ind = ?) ");

        SQLBuf.append(" union ");

        //get the users have access right
        SQLBuf.append(" Select usr_ent_id AS ent_id, usr_display_bil AS display_bil, '").append(ENT_TYPE_USER).append("' AS type, usr_status AS status ");
        SQLBuf.append(" From RegUser, aeCatalogAccess ");
        SQLBuf.append(" Where ");
        SQLBuf.append(" cac_cat_id = ? ");
        SQLBuf.append(" And cac_ent_id = usr_ent_id ");
        SQLBuf.append(" And usr_id not in ");
        SQLBuf.append(" (Select cat_create_usr_id from aeCatalog where ");
        SQLBuf.append(" cat_id = ?) ");
        SQLBuf.append(" order by type asc, display_bil asc ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, cac_cat_id);
        stmt.setString(2, dbUserGroup.USG_ROLE_SYSTEM);
        stmt.setString(3, dbUserGroup.USG_ROLE_SYSTEM);
        stmt.setLong(4, owner_ent_id);
        stmt.setBoolean(5, true);
        stmt.setLong(6, cac_cat_id);
        /* stmt.setString(8, dbUserGroup.USG_ROLE_SYSTEM);
        stmt.setLong(9, owner_ent_id);
        */
        /*stmt.setString(8, dbRegUser.USR_STATUS_DELETED);*/
        stmt.setLong(7, cac_cat_id);

        ResultSet rs = stmt.executeQuery();
        StringBuffer xmlBuf = new StringBuffer(2500);
        while(rs.next()) {
            xmlBuf.append("<entity ent_id=\"").append(rs.getLong("ent_id")).append("\"");
            xmlBuf.append(" type=\"").append(rs.getString("type")).append("\"");
            if(rs.getString("type").equalsIgnoreCase(ENT_TYPE_USER)) {
                xmlBuf.append(" status=\"").append(rs.getString("status")).append("\"");
            }
            xmlBuf.append(" name=\"").append(dbUtils.esc4XML(rs.getString("display_bil"))).append("\"/>");
            xmlBuf.append(dbUtils.NEWL);
        }
        stmt.close();
        return xmlBuf.toString();
    }

    public boolean exists(Connection con) throws SQLException {
        int count;
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(2500);
        SQLBuf.append(" Select count(*) From aeCatalogAccess ");
        SQLBuf.append(" Where cac_cat_id = ? ");
        if(cac_ent_id == CAC_ALL_ORGS) {
            SQLBuf.append(" And cac_ent_id is NULL ");
        } else {
            SQLBuf.append(" And cac_ent_id = ? ");
        }
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cac_cat_id);
        if(cac_ent_id != CAC_ALL_ORGS) {
            stmt.setLong(2, cac_ent_id);
        }
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            count = rs.getInt(1);
        else
            throw new SQLException(SQL + ". cac_ent_id = " + cac_ent_id + " cac_cat_id = " + cac_cat_id + " returns null result set");

        if(count == 1)
            result = true;
        else
            result = false;

        stmt.close();
        return result;
    }
/*
    public void upd(Connection con) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Update aeCatalogAccess Set ");
        SQLBuf.append(" cac_read_ind = ? ");
        SQLBuf.append(" ,cac_write_ind = ? ");
        SQLBuf.append(" Where cac_ent_id = ? ");
        SQLBuf.append(" And cac_cat_id = ? ");

        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setBoolean(1, cac_read_ind);
        stmt.setBoolean(2, cac_write_ind);
        stmt.setLong(3, cac_ent_id);
        stmt.setLong(4, cac_cat_id);

        stmt.executeUpdate();
        stmt.close();
    }
*/

    public void ins(Connection con) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Insert into aeCatalogAccess ");
        SQLBuf.append(" (cac_ent_id, cac_cat_id, cac_create_timestamp, cac_create_usr_id) ");
        SQLBuf.append(" Values ");
        SQLBuf.append(" (?, ?, ?, ?) ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        if(cac_ent_id == CAC_ALL_ORGS) {
            stmt.setNull(1, java.sql.Types.INTEGER);
        } else {
            stmt.setLong(1, cac_ent_id);
        }
        stmt.setLong(2, cac_cat_id);
        stmt.setTimestamp(3, cac_create_timestamp);
        stmt.setString(4, cac_create_usr_id);

        stmt.executeUpdate();
        stmt.close();
    }


/********* static functions **********/

    /*
    public static String prepareList(long[] ent_id, long cat_create_usr_ent_id) {
        StringBuffer listBuf = new StringBuffer(30);
        listBuf.append("(").append(cat_create_usr_ent_id);

        for(int i=0; i<ent_id.length; i++)
            listBuf.append(",").append(ent_id[i]);

        listBuf.append(")");
        String list = new String(listBuf);
        return list;
    }
    */

    public static void delNotInList(Connection con, String list, long[] ent_id, long cat_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        boolean delAllOrgs = true;
        for(int i=0; i<ent_id.length; i++) {
            if(ent_id[i] == CAC_ALL_ORGS) {
                delAllOrgs = false;
                break;
            }
        }

        SQLBuf.append(" Delete FROM aeCatalogAccess ");
        SQLBuf.append(" Where cac_cat_id = ? ");
        SQLBuf.append(" And (cac_ent_id not in ").append(list);
        if(delAllOrgs) {
            SQLBuf.append(" Or cac_ent_id is NULL) ");
        } else {
            SQLBuf.append(" And cac_ent_id is NOT NULL) ");
        }

        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        stmt.executeUpdate();
        stmt.close();
    }


    public static void delCat(Connection con, long cat_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Delete From aeCatalogAccess ");
        SQLBuf.append(" Where cac_cat_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        stmt.executeUpdate();
        stmt.close();
    }


    public static void delEnt(Connection con, long ent_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Delete From aeCatalogAccess ");
        if(ent_id == CAC_ALL_ORGS) {
            SQLBuf.append(" Where cac_ent_id is NULL ");
        } else {
            SQLBuf.append(" Where cac_ent_id = ? ");
        }
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        
        if(ent_id != CAC_ALL_ORGS) {
            stmt.setLong(1, ent_id);
        }
        stmt.executeUpdate();
        stmt.close();
    }

    public static void insInArray(Connection con, long[] ent_id, long cat_id,
                           String cac_create_usr_id, Timestamp cac_create_timestamp) throws SQLException {

        aeCatalogAccess acc;

        for(int i=0;i<ent_id.length;i++) {
            acc  = new aeCatalogAccess();
            acc.cac_ent_id = ent_id[i];
            acc.cac_cat_id = cat_id;
            acc.cac_create_timestamp = cac_create_timestamp;
            acc.cac_create_usr_id = cac_create_usr_id;
            if(!acc.exists(con))
                acc.ins(con);
        }

    }


    public static void updAccess(Connection con, long[] ent_id, long cat_create_usr_ent_id, long cat_id,
                                 String cac_create_usr_id, Timestamp cac_create_timestamp)
      throws SQLException {
        String list = aeUtils.prepareSQLList(ent_id, cat_create_usr_ent_id);
        delNotInList(con, list, ent_id, cat_id);
        //updInArray(con, ent_id, cat_id);
        insInArray(con, ent_id, cat_id, cac_create_usr_id, cac_create_timestamp);
        //delFalseFalse(con);
    }


    public static boolean hasAccessRight(Connection con, long cat_id, long[] ent_ids) throws SQLException {
        boolean result;
        int count;


        if(ent_ids != null) {
            String ent_ids_list = aeUtils.prepareSQLList(ent_ids);

            final String SQL = " Select count(*) "
                             + " From aeCatalog, aeCatalogAccess "
                             + " Where cat_id = ? "
                             + " And cac_cat_id = cat_id "
                             + " And (cac_ent_id in " + ent_ids_list + " Or cac_ent_id is NULL)";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cat_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            count = rs.getInt(1);
            else
                throw new SQLException(SQL + ". cat_id = " + cat_id + " return a null result set");

            if(count > 0)
                result = true;
            else
                result = false;

            stmt.close();
        }
        else
            result = false;

        return result;
    }


     public static boolean hasAccessRight(Connection con, long cat_id, long ent_id) throws SQLException {
        boolean result=false;

        final String SQL = " Select usr_display_bil as display_bil"
                          + " From aeCatalog, aeCatalogAccess, RegUser "
                          + " Where cat_id = ? "
                          + " And cac_cat_id = cat_id "
                          + " And (cac_ent_id = ? Or cac_ent_id is NULL) "
                          + " And cac_ent_id = usr_ent_id "
                          + " union "
                          + " Select usg_display_bil as display_bil "
                          + " From aeCatalog, aeCatalogAccess, UserGroup "
                          + " Where cat_id = ? "
                          + " And cac_cat_id = cat_id "
                          + " And (cac_ent_id = ? Or cac_ent_id is NULL) "
                          + " And cac_ent_id = usg_ent_id ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        stmt.setLong(2, ent_id);
        stmt.setLong(3, cat_id);
        stmt.setLong(4, ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            result = true;
        else
            result = false;
        stmt.close();
        return result;
    }
/*
    public static boolean hasWriteRight(Connection con, long cat_id, long[] ent_ids) throws SQLException {
        boolean result;
        int count;


        if(ent_ids != null) {
            String ent_ids_list = prepareList(ent_ids);

            final String SQL = " Select count(*) "
                             + " From aeCatalog, aeCatalogAccess "
                             + " Where cat_id = ? "
                             + " And cac_write_ind = ? "
                             + " And cac_cat_id = cat_id "
                             + " And cac_ent_id in " + ent_ids_list;

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cat_id);
            stmt.setBoolean(2, true);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            count = rs.getInt(1);
            else
                throw new SQLException(SQL + ". cat_id = " + cat_id + " return a null result set");

            if(count > 0)
                result = true;
            else
                result = false;

            stmt.close();
        }
        else
            result = false;

        return result;
    }

     public static boolean hasWriteRight(Connection con, long cat_id, long ent_id) throws SQLException {
        boolean result=false;

        final String SQL = " Select usr_display_bil as display_bil"
                          + " From aeCatalog, aeCatalogAccess, RegUser "
                          + " Where cat_id = ? "
                          + " And cac_cat_id = cat_id "
                          + " And cac_ent_id = ? "
                          + " And cac_write_ind = ? "
                          + " And cac_ent_id = usr_ent_id "
                          + " union "
                          + " Select usg_display_bil as display_bil "
                          + " From aeCatalog, aeCatalogAccess, UserGroup "
                          + " Where cat_id = ? "
                          + " And cac_cat_id = cat_id "
                          + " And cac_ent_id = ? "
                          + " And cac_write_ind = ? "
                          + " And cac_ent_id = usg_ent_id ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        stmt.setLong(2, ent_id);
        stmt.setBoolean(3, true);
        stmt.setLong(4, cat_id);
        stmt.setLong(5, ent_id);
        stmt.setBoolean(6, true);

        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            result = true;
        else
            result = false;
        stmt.close();
        return result;
    }
*/
     public static String[][] ancestorHasAccessRight(Connection con, long cat_id, long[] ent_ids) throws SQLException {
        String[][] Ancestors=null;
        Vector v = new Vector();
        Vector v2 = new Vector();
        long count;
        boolean result;

        if(ent_ids != null) {
            String ent_ids_list = aeUtils.prepareSQLList(ent_ids);

            String SQL = " Select usr_ent_id as ent_id, usr_display_bil as display_bil "
                       + " From aeCatalog, aeCatalogAccess, RegUser "
                       + " Where cat_id = ? "
                       + " And cac_cat_id = cat_id "
                       + " And (cac_ent_id in " + ent_ids_list + " Or cac_ent_id is NULL) "
                       + " And usr_ent_id = cac_ent_id "
                       + " union "
                       + " Select usg_ent_id as ent_id, usg_display_bil as display_bil "
                       + " From aeCatalog, aeCatalogAccess, UserGroup "
                       + " Where cat_id = ? "
                       + " And cac_cat_id = cat_id "
                       + " And (cac_ent_id in " + ent_ids_list + " Or cac_ent_id is NULL) "
                       + " And usg_ent_id = cac_ent_id "
                       + " Order by 2 asc ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cat_id);
            stmt.setLong(2, cat_id);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(rs.getString("display_bil"));
                v2.addElement(new Long(rs.getLong("ent_id")));
            }
            Ancestors = new String[v.size()][2];
            for(int i=0;i<v.size();i++) {
                Ancestors[i][0] = ((Long) v2.elementAt(i)).toString();
                Ancestors[i][1] = (String) v.elementAt(i);
            }
            stmt.close();
        }
        return Ancestors;
    }

/*
     public static String[][] ancestorHasWriteRight(Connection con, long cat_id, long[] ent_ids) throws SQLException {
        String[][] Ancestors=null;
        Vector v = new Vector();
        Vector v2 = new Vector();
        long count;
        boolean result;

        if(ent_ids != null) {
            String ent_ids_list = prepareList(ent_ids);

            String SQL = " Select usr_ent_id as ent_id, usr_display_bil as display_bil "
                       + " From aeCatalog, aeCatalogAccess, RegUser "
                       + " Where cat_id = ? "
                       + " And cac_cat_id = cat_id "
                       + " And cac_write_ind = ? "
                       + " And cac_ent_id in " + ent_ids_list
                       + " And usr_ent_id = cac_ent_id "
                       + " union "
                       + " Select usg_ent_id as ent_id, usg_display_bil as display_bil "
                       + " From aeCatalog, aeCatalogAccess, UserGroup "
                       + " Where cat_id = ? "
                       + " And cac_cat_id = cat_id "
                       + " And cac_write_ind = ? "
                       + " And cac_ent_id in " + ent_ids_list
                       + " And usg_ent_id = cac_ent_id "
                       + " Order by 2 asc ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cat_id);
            stmt.setBoolean(2, true);
            stmt.setLong(3, cat_id);
            stmt.setBoolean(4, true);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(rs.getString("display_bil"));
                v2.addElement(new Long(rs.getLong("ent_id")));
            }
            Ancestors = new String[v.size()][2];
            for(int i=0;i<v.size();i++) {
                Ancestors[i][0] = ((Long) v2.elementAt(i)).toString();
                Ancestors[i][1] = (String) v.elementAt(i);
            }
            stmt.close();
        }
        return Ancestors;
    }
*/
}