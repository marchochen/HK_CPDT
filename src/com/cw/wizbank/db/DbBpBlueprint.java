/*
 * Created on 2004-9-24
 */
package com.cw.wizbank.db;

import java.sql.*;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;

public class DbBpBlueprint {
    //Table columns
    public long blp_ste_ent_id;

    public String blp_src_type;

    public String blp_source;

    public String blp_path;

    public Timestamp blp_create_timestamp;

    public String blp_create_usr_id;

    public Timestamp blp_update_timestamp;

    public String blp_update_usr_id;

    public String blp_update_usr_name;
    /**
     * Constructor,Set the Ent_ID
     * 
     * @param ent_id
     *            for acSite
     */
    public DbBpBlueprint(long ent_id) {
        blp_ste_ent_id = ent_id;
    }

    public int Insert(Connection con) throws SQLException {
        int affected;
        blp_create_timestamp = cwSQL.getTime(con);
        blp_update_timestamp = blp_create_timestamp;
        PreparedStatement stmt = con
                .prepareStatement(SqlStatements.sql_ins_blp);
        stmt.setLong(1, blp_ste_ent_id);
        stmt.setString(2, blp_src_type);
        stmt.setString(3, blp_source);
        stmt.setString(4, blp_path);
        stmt.setTimestamp(5, blp_create_timestamp);
        stmt.setString(6, blp_create_usr_id);
        stmt.setTimestamp(7, blp_update_timestamp);
        stmt.setString(8, blp_update_usr_id);
        affected = stmt.executeUpdate();
        if (affected != 1) {
            con.rollback();
            stmt.close();
            throw new SQLException("Failed to Create Blueprint!");
        }
        stmt.close();
        return affected;
    }

    public boolean get(Connection con) throws SQLException {
        boolean Result = false;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_blp);
        stmt.setLong(1, blp_ste_ent_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            this.blp_src_type = rs.getString("blp_src_type");
            this.blp_path = rs.getString("blp_path");
            this.blp_source = rs.getString("blp_source");
            this.blp_create_timestamp = rs.getTimestamp("blp_create_timestamp");
            this.blp_create_usr_id = rs.getString("blp_create_usr_id");
            this.blp_update_timestamp = rs.getTimestamp("blp_update_timestamp");
            this.blp_update_usr_id = rs.getString("blp_update_usr_id");
            this.blp_update_usr_name = rs.getString("usr_display_bil");
            Result = true;
        }
        else{
            this.blp_src_type = "";
            this.blp_path = "";
            this.blp_source = "";
            this.blp_create_timestamp = null;
            this.blp_create_usr_id = "";
            this.blp_update_timestamp = null;
            this.blp_update_usr_id = "";
            this.blp_update_usr_name = "";
        }
        stmt.close();
        return Result;
    }

    public int update(Connection con) throws SQLException, cwSysMessage {
        int affected;
        if (!blp_update_timestamp.equals(getUpdateTimestamp(con))){
            throw new cwSysMessage("Failed to Update BluePrint");
        }else{
	        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_blp);
	        stmt.setString(1, blp_src_type);
	        stmt.setString(2, blp_source);
	        stmt.setString(3, blp_path);
	        stmt.setTimestamp(4, cwSQL.getTime(con));
	        stmt.setString(5, blp_update_usr_id);
	        stmt.setLong(6, blp_ste_ent_id);
	        affected = stmt.executeUpdate();
	        if (affected != 1) {
	            con.rollback();
	            stmt.close();
	            throw new cwSysMessage("Failed to Update BluePrint");
	        }
	        stmt.close();
        }
        return affected;
    }
    public int delete(Connection con) throws SQLException, cwSysMessage {
        int affected;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_blp);
        stmt.setLong(1,blp_ste_ent_id);
        affected = stmt.executeUpdate();
        if (affected != 1) {
            con.rollback();
            stmt.close();
            throw new cwSysMessage("Failed to delete BluePrint blp_sttte_ent_id:"+ blp_ste_ent_id);
        }
        stmt.close();
        return affected;
    }
    
    public Timestamp getUpdateTimestamp(Connection con) throws SQLException, cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_updateTimestamp);
        stmt.setLong(1,blp_ste_ent_id);
        ResultSet rs = stmt.executeQuery();
        Timestamp updTimestamp = null;
        if(rs.next())
            updTimestamp = rs.getTimestamp("blp_update_timestamp");
        else
            throw new SQLException("Failed to get blueprint update timestamp, blp_ste_ent_id = " + blp_ste_ent_id);
        stmt.close();
        return updTimestamp;
    }
    
    public boolean checkExist(Connection con) throws SQLException, cwSysMessage{
        boolean exist = false;
        String Sqlstatement = "select count(*) as countx from bpblueprint where blp_ste_ent_id=?";
        PreparedStatement stmt = con.prepareStatement(Sqlstatement);
        stmt.setLong(1,blp_ste_ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            if(rs.getInt("countx") == 1)
                exist = true;
        }
        else
            throw new SQLException("Failed to select table bpblueprint");
        stmt.close();
        return exist;    
    }
    public String asXML(){
        StringBuffer xmlStr = new StringBuffer();
        xmlStr.append("<content type=\"").append(blp_src_type).append("\">")
        	  .append("<source>").append(blp_source).append("</source>")
        	  .append("<path>").append(blp_path).append("</path>")
        	  .append("<last_update>")
        	  .append("<user id=\"").append(blp_create_usr_id).append("\" ent=\"").append(blp_ste_ent_id).append("\">")
        	  .append("<display_bil>").append(cwUtils.esc4XML(blp_update_usr_name)).append("</display_bil></user>")
        	  .append("<timestamp>").append(blp_update_timestamp).append("</timestamp>")
        	  .append("</last_update></content>");
        return cwUtils.escNull(xmlStr.toString());
    }
}