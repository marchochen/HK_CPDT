package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.db.sql.*;

public class DbAssessmentEvaluation{
    public long asv_ent_id;
    public long asv_mod_id;
    public int asv_correct_cnt;
    public int asv_incorrect_cnt;
    public int asv_giveup_cnt;
    
    public void ins(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_assessment_evaluation);
        int index = 1;
        stmt.setLong(index++, asv_ent_id);
        stmt.setLong(index++, asv_mod_id);
        stmt.setInt(index++, asv_correct_cnt);
        stmt.setInt(index++, asv_incorrect_cnt);
        stmt.setInt(index++, asv_giveup_cnt);
        stmt.executeUpdate();
        stmt.close();
    }

    public void upd(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_assessment_evaluation);
        int index = 1;
        stmt.setInt(index++, asv_correct_cnt);
        stmt.setInt(index++, asv_incorrect_cnt);
        stmt.setInt(index++, asv_giveup_cnt);
        stmt.setLong(index++, asv_ent_id);
        stmt.setLong(index++, asv_mod_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public boolean get(Connection con) throws SQLException{
        boolean hasRecord = false;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_assessment_evaluation);
        stmt.setLong(1, asv_ent_id);
        stmt.setLong(2, asv_mod_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            asv_correct_cnt = rs.getInt("asv_correct_cnt");
            asv_incorrect_cnt = rs.getInt("asv_incorrect_cnt");
            asv_giveup_cnt = rs.getInt("asv_giveup_cnt");
            hasRecord = true;
        }else{
            hasRecord = false;
        }
        stmt.close();
        return hasRecord;
    }
    
    public void save(Connection con, long ent_id, long mod_id, int correct_delta, int incorrect_delta, int giveup_delta) throws SQLException{
        asv_ent_id = ent_id;
        asv_mod_id = mod_id;
        
        boolean hasRecord = get(con);
        if (hasRecord){
            asv_correct_cnt     += correct_delta;
            asv_incorrect_cnt   += incorrect_delta;
            asv_giveup_cnt      += giveup_delta;
            upd(con);    
        }else{
            asv_correct_cnt     = correct_delta;
            asv_incorrect_cnt   = incorrect_delta;
            asv_giveup_cnt      = giveup_delta;
            ins(con);    
        }
    }
    
    
    
}