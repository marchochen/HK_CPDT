package com.cw.wizbank.db;


import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.io.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.sql.*;

public class DbCtReference {
    /**
     * CLOB column
     * Table:       ctReference
     * Column:      ref_description
     * Nullable:    YES
     */
    public int ref_id;
    public int ref_res_id;
    public String ref_type;
    public String ref_title;
    public String ref_description;
    public String ref_url;
    public String ref_create_usr_id;
    public String ref_update_usr_id;
    public Timestamp ref_create_timestamp;
    public Timestamp ref_update_timestamp;

    public DbCtReference() {
    }

    static public Vector getModuleReferenceList(Connection con, int mod_id) throws IOException, SQLException {
        int temp_ref_id;
        Hashtable htReference = null;
        DbCtReference myDbCtReference = null;
        Vector vtReferenceList = new Vector();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.get_reference_lst_sql(con));

        // set the values for prepared statements
        stmt.setInt(1, mod_id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            myDbCtReference = new DbCtReference();
            myDbCtReference.ref_id = rs.getInt("ref_id");
            myDbCtReference.ref_res_id = rs.getInt("ref_res_id");
            myDbCtReference.ref_type = rs.getString("ref_type");
            myDbCtReference.ref_title = rs.getString("ref_title");
            myDbCtReference.ref_description = cwSQL.getClobValue(rs, "ref_description");
            myDbCtReference.ref_url = rs.getString("ref_url");
            myDbCtReference.ref_create_usr_id = rs.getString("ref_create_usr_id");
            myDbCtReference.ref_create_timestamp = rs.getTimestamp("ref_create_timestamp");
            myDbCtReference.ref_update_usr_id = rs.getString("ref_update_usr_id");
            myDbCtReference.ref_update_timestamp = rs.getTimestamp("ref_update_timestamp");

            vtReferenceList.addElement(myDbCtReference);
        }

        stmt.close();

        return vtReferenceList;
    }

    public void get(Connection con) throws qdbException, cwSysMessage {
        try {
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_reference_get);

            // set the values for prepared statements
            stmt.setInt(1, ref_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                ref_res_id = rs.getInt("ref_res_id");
                ref_type = rs.getString("ref_type");
                ref_title = rs.getString("ref_title");
                ref_description = cwSQL.getClobValue(rs, "ref_description");
                ref_url = rs.getString("ref_url");
                ref_create_usr_id = rs.getString("ref_create_usr_id");
                ref_create_timestamp = rs.getTimestamp("ref_create_timestamp");
                ref_update_usr_id = rs.getString("ref_update_usr_id");
                ref_update_timestamp = rs.getTimestamp("ref_update_timestamp");
            }
            else
                //throw new qdbException( "No data for resource. id = " + res_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Reference ID = " + ref_id);

            stmt.close();
        } catch(SQLException e) {
             throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void ins(Connection con) throws qdbException {
        try {
            // << BEGIN for oracle migration!
            PreparedStatement stmt = con.prepareStatement(
            "INSERT INTO ctReference "  //changed resources
            + " ( ref_res_id "
            + " , ref_type "
            + " , ref_title "
            //+ " , ref_description "
            + " , ref_url "
            + " , ref_create_usr_id "
            + " , ref_create_timestamp "
            + " , ref_update_usr_id "
            + " , ref_update_timestamp "
            + " ) values "
            //+ " ( ?, ?, ?, " + cwSQL.getClobNull(con) + ", ?, ?, ?, ?, ? )"
            + " ( ?, ?, ?, ?, ?, ?, ?, ? )"
            , PreparedStatement.RETURN_GENERATED_KEYS);
            // >> END

            Timestamp curTime = dbUtils.getTime(con);
            if(ref_update_timestamp == null) {
                ref_update_timestamp = curTime;
            }
            if(ref_create_timestamp == null) {
                ref_create_timestamp = curTime;
            }

            stmt.setInt(1, ref_res_id);
            stmt.setString(2, ref_type);
            stmt.setString(3, ref_title);
            stmt.setString(4, ref_url);
            stmt.setString(5, ref_create_usr_id);
            stmt.setTimestamp(6, ref_create_timestamp);
            stmt.setString(7, ref_update_usr_id);
            stmt.setTimestamp(8, ref_update_timestamp);

            /* insert */
            if(stmt.executeUpdate() != 1 ) {
                throw new qdbException("Fails to insert Resource");
            }
            else {
                ref_id =(int) cwSQL.getAutoId(con, stmt, "ctReference", "ref_id");
                stmt.close();

                updReferenceDesc(con, ref_id, ref_description);
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return;
    }

    public void upd(Connection con) throws qdbException {
        try {

            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_reference_upd);

            Timestamp curTime = dbUtils.getTime(con);
            if(ref_update_timestamp == null) {
                ref_update_timestamp = curTime;
            }
            if(ref_create_timestamp == null) {
                ref_create_timestamp = curTime;
            }

            stmt.setString(1, ref_title);
            stmt.setString(2, ref_url);
            stmt.setString(3, ref_update_usr_id);
            stmt.setTimestamp(4, ref_update_timestamp);
            stmt.setInt(5, ref_id);

            /* update */
            if(stmt.executeUpdate() != 1 ) {
                throw new qdbException("Fails to update Resource");
            }

            stmt.close();

            updReferenceDesc(con, ref_id, ref_description);

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return;

    }

    public void delete(Connection con) throws qdbException {
        try {

            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_reference_del);

            stmt.setInt(1, ref_id);

            /* update */
            if(stmt.executeUpdate() != 1 ) {
                throw new qdbException("Fails to delete Resource");
            }

            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return;

    }

	// del the keyword and definition
	public void delByResId(Connection con) throws SQLException, cwException {
		String DbCtGlossary_DEL = " DELETE FROM ctReference WHERE ref_res_id = ? ";
		PreparedStatement stmt = con.prepareStatement(DbCtGlossary_DEL);
		stmt.setLong(1, this.ref_res_id);
		stmt.executeUpdate();
		stmt.close();
		return;
	}

    public static void updReferenceDesc(Connection con, int ref_id, String val) throws qdbException {
        try {
            // << BEGIN for oracle migration!
/*            PreparedStatement stmt = con.prepareStatement(
                " UPDATE ctReference SET ref_description = " + cwSQL.getClobNull(con)
                + " WHERE ref_id = ? ");

            stmt.setInt(1, ref_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement(
                      " SELECT ref_description FROM ctReference "
                    + "     WHERE ref_id = ? FOR UPDATE "
                    ,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );

            stmt.setInt(1, ref_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                cwSQL.setClobValue(con, rs, "ref_description", val);
                rs.updateRow();
            }

            stmt.close();
*/
            // Update ref_description
            // construct the condition
            String condition = "ref_id = " + ref_id;
            // construct the column & value
            String[] columnName = new String[1];
            String[] columnValue = new String[1];
            columnName[0] = "ref_description";
            columnValue[0] = val;
            cwSQL.updateClobFields(con, "ctReference", columnName, columnValue, condition);
            // >> END

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
}