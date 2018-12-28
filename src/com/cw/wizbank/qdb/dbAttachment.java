package com.cw.wizbank.qdb;

import java.sql.*;
import com.cw.wizbank.util.cwSQL;

public class dbAttachment {
    /**
     * CLOB column
     * Table:       Attachment
     * Column:      att_desc
     * Nullable:    YES
     */
    public static final String ATT_TYPE_STUDENT = "STUDENT";
    public static final String ATT_TYPE_TEACHER = "TEACHER";
    public static final String ATT_TYPE_KMOBJECT = "KMOBJECT";

    public long att_id;
    public String att_type;
    public String att_filename;
    public String att_desc;
    public long att_att_id_parent;

    public dbAttachment() {
        super();
    }


    //Constructor, input the att_id and read the other attributes from database
    public dbAttachment(Connection con, long id) throws qdbException {
        try {
            att_id = id;

            String SQL = "Select att_type, att_filename, att_desc, att_att_id_parent ";
            SQL += "From Attachment ";
            SQL += "Where att_id = ?";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, att_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next() == true) {
                att_type = rs.getString(1);
                att_filename = rs.getString(2);
                att_desc = cwSQL.getClobValue(rs, "att_desc");
                att_att_id_parent = rs.getLong(4);
            }
            else
                {
                    stmt.close();
                throw new qdbException("Record not found for att_id " + att_id );
        }
                stmt.close();
	    }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }


    public void delInLst(Connection con, String idLst) throws qdbException {
        try {
            String SQL = "Delete from Attachment "
                       + " Where att_id in " + idLst;

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
                stmt.close();
        }
        catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    //insert a record into Attachment
    public long ins(Connection con) throws qdbException {
        try {
            String SQL = "";
            PreparedStatement stmt;

            int count = 1;
            // << BEGIN for oracle migration!
            if(att_att_id_parent != 0) {
                SQL = "Insert into Attachment ";
                SQL += "(att_type, att_filename, att_att_id_parent) ";
                //SQL += "(att_type, att_filename, att_desc, att_att_id_parent) ";
                SQL += "values (?, ?, ?) ";

                stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(count++, att_type);
                stmt.setString(count++, att_filename);
                //stmt.setString(count++, att_desc);
                stmt.setLong(count++, att_att_id_parent);
            }
            else
            {
                SQL = "Insert into Attachment ";
                SQL += "(att_type, att_filename) ";
                //SQL += "(att_type, att_filename, att_desc) ";
                SQL += "values (?, ?) ";

                stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(count++, att_type);
                stmt.setString(count++, att_filename);
                //stmt.setString(count++, att_desc);
            }

//add stmt.close()
//modified by Lear
            int stmtResult=stmt.executeUpdate();
            if ( stmtResult!=1) {
                con.rollback();
                stmt.close();
                throw new qdbException("Insert into Attachment fail.");
            }

            att_id = cwSQL.getAutoId(con, stmt, "Attachment", "att_id");  //get the att_id of the newly inserted attachment

            // Update att_desc
            // construct the condition
            String condition = "att_id = " + att_id;
            // construct the column & value
            String[] columnName = new String[1];
            String[] columnValue = new String[1];
            columnName[0] = "att_desc";
            columnValue[0] = att_desc;
            cwSQL.updateClobFields(con, "Attachment", columnName, columnValue, condition);
            // >> END
            stmt.close();
            return att_id;
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }


    //delete a record from Attachment
    public void del(Connection con) throws qdbException {
        try {
            String SQL = "Delete From Attachment ";
            SQL += "Where att_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, att_id);

//add stmt.close()
//modified by Lear
                int stmtResult=stmt.executeUpdate();
                if ( stmtResult!=1) {
                    stmt.close();
                con.rollback();
                throw new qdbException("Not only one record deleted from Attachment for att_id = " + att_id);
            }
        }
        catch (SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }
}