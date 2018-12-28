package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.*;


/**
A database class to manage table cmAssessment
*/
public class DbCmAssessment{
    
    public long         asm_id;
    public long         asm_ent_id;
    public String       asm_title;
    public long         asm_sks_skb_id;
    public String       asm_status;
    public Timestamp    asm_review_start_datetime;
    public Timestamp    asm_review_end_datetime;
    public Timestamp    asm_eff_start_datetime;
    public Timestamp    asm_eff_end_datetime;
    public String       asm_create_usr_id;
    public Timestamp    asm_create_timestamp;
    public String       asm_update_usr_id;
    public Timestamp    asm_update_timestamp;
    public String       asm_type;
    public long         asm_mod_res_id;
    public String       asm_marking_scheme_xml;
    public boolean      asm_auto_resolved_ind;
    
    private static String PREPARED      =   "PREPARED";
    
    public static final String ASM_TYPE_SVY   =   "SVY";
    public static final String ASM_TYPE_TST   =   "TST";
    
    private static String SQL_INS_CMASSESSMENT = " INSERT INTO cmAssessment ( "
                                  + "  asm_ent_id, asm_sks_skb_id, asm_title, asm_status, "
                                  + "  asm_review_start_datetime, asm_review_end_datetime, "
                                  + "  asm_eff_start_datetime, asm_eff_end_datetime, "
                                  + "  asm_create_usr_id, asm_create_timestamp, "
                                  + "  asm_update_usr_id, asm_update_timestamp, "
                                  + "  asm_type, asm_auto_resolved_ind) "
                                  + " VALUES (?,?,?,?, ?,?, ?,?, ?,?, ?,?, ?,?) ";
                                  
    private static String SQL_UPD_CMASSESSMENT = " UPDATE cmAssessment SET "
                                  + "  asm_ent_id = ?, asm_sks_skb_id = ?, "
                                  + "  asm_title = ? , "
                                  + "  asm_review_start_datetime = ? , asm_review_end_datetime = ?, "
                                  + "  asm_eff_start_datetime = ?, asm_eff_end_datetime = ?, "
                                  + "  asm_update_usr_id = ?, asm_update_timestamp = ?, "
                                  + "  asm_auto_resolved_ind = ? "
                                  + "  WHERE asm_id = ? ";

    private static String SQL_DEL_CMASSESSMENT = " DELETE From cmAssessment "
                                  + " WHERE asm_id = ? ";

    private static final String SQL_GET_SKILL_SET_ID = " SELECT asm_sks_skb_id "
                                                     + " FROM cmAssessment "
                                                     + " WHERE asm_id = ? ";

    private static final String SQL_GET = " SELECT asm_ent_id, asm_title, asm_sks_skb_id, asm_status, "
                                         + " asm_review_start_datetime, asm_review_end_datetime, "
                                         + " asm_eff_start_datetime, asm_eff_end_datetime, "
                                         + " asm_create_usr_id, asm_create_timestamp, "
                                         + " asm_update_usr_id, asm_update_timestamp, "
                                         + " asm_auto_resolved_ind "                                         
                                         + " FROM cmAssessment "
                                         + " WHERE asm_id = ? ";

    private static final String SQL_GET_SVY_ID = " SELECT asm_sks_skb_id FROM cmAssessment "
                                            + " WHERE asm_id = ? " ;

    private static final String SQL_GET_BY_MOD = " SELECT asm_id, asm_ent_id, asm_title, asm_sks_skb_id, asm_status, "
                                         + " asm_review_start_datetime, asm_review_end_datetime, "
                                         + " asm_eff_start_datetime, asm_eff_end_datetime, "
                                         + " asm_create_usr_id, asm_create_timestamp, "
                                         + " asm_update_usr_id, asm_update_timestamp, "
                                         + " asm_type, asm_marking_scheme_xml "
                                         + " FROM cmAssessment "
                                         + " WHERE asm_mod_res_id = ? " ;

    private static final String SQL_CHECK_SURVEY = " SELECT COUNT(*) FROM cmAssessment "
                                            +   " WHERE asm_sks_skb_id = ? ";

    private static final String SQL_UPD_STATUS = " UPDATE cmAssessment SET asm_status = ? "
                                                + " WHERE asm_id = ? ";
    
    private static final String SQL_GET_STATUS = " SELECT asm_status FROM cmAssessment "
                                                + " WHERE asm_id = ? ";
    
    
    /**
    Inser a new competency assessment
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException, cwSysMessage {
        int index =1;
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SQL_INS_CMASSESSMENT,Statement.RETURN_GENERATED_KEYS);
        stmt.setLong(index++, asm_ent_id);
        stmt.setLong(index++, asm_sks_skb_id);
        stmt.setString(index++, asm_title);
        stmt.setString(index++, PREPARED);
        stmt.setTimestamp(index++, asm_review_start_datetime);
        stmt.setTimestamp(index++, asm_review_end_datetime);
        stmt.setTimestamp(index++, asm_eff_start_datetime);
        stmt.setTimestamp(index++, asm_eff_end_datetime);
        stmt.setString(index++, asm_create_usr_id);
        stmt.setTimestamp(index++, curTime);
        stmt.setString(index++, asm_create_usr_id);
        stmt.setTimestamp(index++, curTime);
        stmt.setString(index++, ASM_TYPE_SVY);
        stmt.setBoolean(index++, asm_auto_resolved_ind);
        
        int code = stmt.executeUpdate();
        asm_id = cwSQL.getAutoId(con, stmt, "cmAssessment", "asm_id");
        stmt.close();
        
        return code;
    }

    /**
    Update a specified competency assessment from the database
    @return the row count for UPDATE
    */
    public int upd(Connection con)
        throws SQLException {
        int index =1;
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SQL_UPD_CMASSESSMENT);
        stmt.setLong(index++, asm_ent_id);
        stmt.setLong(index++, asm_sks_skb_id);
        stmt.setString(index++, asm_title);
        /*
        if( asm_status == null )
            stmt.setNull(index++, java.sql.Types.NULL);
        else
            stmt.setString(index++, asm_status);
            */
        stmt.setTimestamp(index++, asm_review_start_datetime);
        stmt.setTimestamp(index++, asm_review_end_datetime);
        stmt.setTimestamp(index++, asm_eff_start_datetime);
        stmt.setTimestamp(index++, asm_eff_end_datetime);
        stmt.setString(index++, asm_update_usr_id);
        stmt.setTimestamp(index++, curTime);
        stmt.setBoolean(index++, asm_auto_resolved_ind);
        stmt.setLong(index++, asm_id);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }

    /**
    Delete a specified competency assessment from the database
    @return the row count for DELETE
    */
    public int del(Connection con)
        throws SQLException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_DEL_CMASSESSMENT);
        stmt.setLong(1, asm_id);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }
    
    
    /**
    Get the Skill Set Id of the Assessment
    @return Skill Set Id
    */
    /*
    public long getSkillSetId(Connection con)
        throws SQLException, cwSysMessage {
            
            long sks_id = 0;
            PreparedStatement stmt = con.prepareStatement(SQL_GET_SKILL_SET_ID);
            stmt.setLong(1, asm_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                sks_id = rs.getLong("asm_sks_skb_id");
            else
                throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " assessment id = " + asm_id);
            stmt.close();
            return sks_id;            
        }
    */
    
    /**
    Get the Assessment detial
    */
    public void get(Connection con)
        throws SQLException, cwSysMessage {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL_GET);
            stmt.setLong(1, asm_id);
            rs = stmt.executeQuery();
            if( rs.next() ) {
                asm_ent_id              =   rs.getLong("asm_ent_id");
                asm_title               =   rs.getString("asm_title");
                asm_sks_skb_id          =   rs.getLong("asm_sks_skb_id");
                asm_review_start_datetime = rs.getTimestamp("asm_review_start_datetime");
                asm_review_end_datetime =   rs.getTimestamp("asm_review_end_datetime");
                asm_eff_start_datetime  =   rs.getTimestamp("asm_eff_start_datetime");
                asm_eff_end_datetime    =   rs.getTimestamp("asm_eff_end_datetime");
                asm_create_usr_id       =   rs.getString("asm_create_usr_id");
                asm_create_timestamp    =   rs.getTimestamp("asm_create_timestamp");
                asm_update_usr_id       =   rs.getString("asm_update_usr_id");
                asm_update_timestamp    =   rs.getTimestamp("asm_update_timestamp");
                asm_status              =   rs.getString("asm_status");
                asm_auto_resolved_ind   =   rs.getBoolean("asm_auto_resolved_ind");
            }
            else 
                throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " assessment id = " + asm_id);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
            
            return;
        }
        
    /**
    Check the survey is picked by the assessment
    @return boolean
    */
    public static boolean checkSurvey(Connection con, long sks_skb_id)  
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_CHECK_SURVEY);
            stmt.setLong(1, sks_skb_id);            
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() ) {
                if( rs.getLong(1) > 0 )
                    flag = true;
            }else {
                throw new cwException("Failed to check the existing of a survey, id = " + sks_skb_id );
            }
            stmt.close();
            return flag;
        }
        

    /**
    Get the survey id of this assessment by it's mod_res_id
    @return long value of the survey id
    */
    public void getByMod(Connection con, long mod_res_id)
        throws SQLException, cwException{

        this.asm_mod_res_id = mod_res_id;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_BY_MOD);
            stmt.setLong(1, this.asm_mod_res_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                asm_id                  =   rs.getLong("asm_id");
                asm_ent_id              =   rs.getLong("asm_ent_id");
                asm_title               =   rs.getString("asm_title");
                asm_sks_skb_id          =   rs.getLong("asm_sks_skb_id");
                asm_review_start_datetime = rs.getTimestamp("asm_review_start_datetime");
                asm_review_end_datetime =   rs.getTimestamp("asm_review_end_datetime");
                asm_eff_start_datetime  =   rs.getTimestamp("asm_eff_start_datetime");
                asm_eff_end_datetime    =   rs.getTimestamp("asm_eff_end_datetime");
                asm_create_usr_id       =   rs.getString("asm_create_usr_id");
                asm_create_timestamp    =   rs.getTimestamp("asm_create_timestamp");
                asm_update_usr_id       =   rs.getString("asm_update_usr_id");
                asm_update_timestamp    =   rs.getTimestamp("asm_update_timestamp");
                asm_status              =   rs.getString("asm_status");
                asm_type                =   rs.getString("asm_type");
                asm_marking_scheme_xml  =   cwSQL.getClobValue(rs, "asm_marking_scheme_xml");
            }
            else
                throw new cwException("Failed to get the survey of assessment by module, mod_res_id = " + this.asm_mod_res_id);
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }
        
    /**
    Get the selected survey id of the assessment
    @return long value of the survey id
    */
    public long getSurveyId(Connection con)
        throws SQLException, cwException{
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_SVY_ID);
            stmt.setLong(1, asm_id);
            ResultSet rs = stmt.executeQuery();
            long sks_skb_id;
            if( rs.next() )
                sks_skb_id = rs.getLong("asm_sks_skb_id");
            else
                throw new cwException("Failed to get the survey of assessment, id = " + asm_id );
            stmt.close();
            return sks_skb_id;
        }
        
    /**
    Update assessment status
    */
    public void updStatus(Connection con)
        throws SQLException, cwException {

            PreparedStatement stmt = con.prepareStatement(SQL_UPD_STATUS);
            stmt.setString(1, asm_status);
            stmt.setLong(2, asm_id);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Assessment not exists, id = " + asm_id);
            else
                stmt.close();        
        }
 

    /**
    Get assessment status
    @param String of the assessment status
    */
    public static String getStatus(Connection con, long id)
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_STATUS);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            String status = new String();
            if( rs.next() )
                status = rs.getString("asm_status");
            else
                throw new cwException("Assessment not exist, id = " + id);
                
            stmt.close();
            if( status == null )
                status = new String();
            return status;
        }

    public boolean validUpdTimestap(Connection con, Timestamp last_upd_timestamp)
        throws SQLException{
            
            String SQL = " SELECT COUNT(asm_id) FROM cmAssessment "
                       + " WHERE asm_id = ? AND asm_update_timestamp = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            stmt.setTimestamp(2, asm_update_timestamp);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;            
            if(rs.next())
                flag = true;
            stmt.close();
            return flag;
        }

        
        
    public void updTimestamp(Connection con)
        throws SQLException {
            
            if( asm_update_timestamp == null )
                asm_update_timestamp = cwSQL.getTime(con);
                
            String SQL = " UPDATE cmAssessment "
                       + " SET asm_update_timestamp = ? "
                       + " WHERE asm_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setTimestamp(1, asm_update_timestamp);
            stmt.setLong(2, asm_id);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to update assessment timestamp, asm_id = " + asm_id);
            stmt.close();
            return;
            
        }
}
