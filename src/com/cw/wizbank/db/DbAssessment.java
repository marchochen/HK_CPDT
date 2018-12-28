package com.cw.wizbank.db;

import java.sql.*;

import com.cw.wizbank.util.*;

public class DbAssessment {


    public long asm_res_id;
    public int asm_que_num;
    public String asm_que_src;
    public int asm_que_duration;
    public int asm_round_duration;
    public boolean asm_que_hint_ind;
    public boolean asm_neg_score_ind;
    public boolean asm_save_score_ind;
    public int asm_trump_num;
    public int asm_trump_multiplier;
    public int asm_diff_multiplier;
    public Timestamp asm_daily_start_datetime;
    public Timestamp asm_daily_end_datetime;
    public boolean asm_score_dec_ind;
    public boolean asm_show_ans_ind;
    public String asm_type;
    
    //MultiPlayer Only
    public String asm_mode;
    public String asm_rom_host;
    public int asm_rom_port;
    public int asm_www_port;
    public boolean asm_chat_ind;




    public void ins(Connection con)
        throws SQLException, cwException {
            
            String SQL = " INSERT INTO Assessment "
                       + " ( asm_res_id, asm_que_num, asm_que_src, asm_que_duration, asm_round_duration, "
                       + "   asm_que_hint_ind, asm_neg_score_ind, asm_save_score_ind, "
                       + "   asm_trump_num, asm_trump_multiplier, asm_diff_multiplier, "
                       + "   asm_daily_start_datetime, asm_daily_end_datetime, asm_score_dec_ind, "
                       + "   asm_show_ans_ind, asm_type, asm_mode, asm_rom_host, asm_rom_port, asm_www_port, asm_chat_ind ) "
                       + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_res_id);
            stmt.setInt(2, asm_que_num);
            stmt.setString(3, asm_que_src);
            stmt.setInt(4, asm_que_duration);
            stmt.setInt(5, asm_round_duration);
            stmt.setBoolean(6, asm_que_hint_ind);
            stmt.setBoolean(7, asm_neg_score_ind);
            stmt.setBoolean(8, asm_save_score_ind);
            stmt.setInt(9, asm_trump_num);
            stmt.setInt(10, asm_trump_multiplier);
            stmt.setInt(11, asm_diff_multiplier);
            stmt.setTimestamp(12, asm_daily_start_datetime);
            stmt.setTimestamp(13, asm_daily_end_datetime);
            stmt.setBoolean(14, asm_score_dec_ind);
            stmt.setBoolean(15, asm_show_ans_ind);
            stmt.setString(16, asm_type);
            stmt.setString(17, asm_mode);
            stmt.setString(18, asm_rom_host);
            stmt.setInt(19, asm_rom_port);
            stmt.setInt(20, asm_www_port);
            stmt.setBoolean(21, asm_chat_ind);
            
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to insert assessment. ");
            stmt.close();
            return;
            
        }



    public void upd(Connection con)
        throws SQLException, cwException {
            
            String SQL = " UPDATE Assessment "
                       + " SET asm_que_num = ?, "
                       + " asm_que_src = ?, "
                       + " asm_que_duration = ?, "
                       + " asm_round_duration = ?, "
                       + " asm_que_hint_ind = ?, "
                       + " asm_neg_score_ind = ?, "
                       + " asm_save_score_ind = ?, "
                       + " asm_trump_num = ?, "
                       + " asm_trump_multiplier = ?, "
                       + " asm_diff_multiplier = ?, "
                       + " asm_daily_start_datetime = ?, "
                       + " asm_daily_end_datetime = ?, "
                       + " asm_score_dec_ind = ?, "
                       + " asm_show_ans_ind = ?, "
                       + " asm_type = ?, "
                       + " asm_mode = ?, " 
                       + " asm_rom_host = ?, "
                       + " asm_rom_port = ?, "
                       + " asm_chat_ind = ? "
                       + " WHERE asm_res_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setInt(1, asm_que_num);
            stmt.setString(2, asm_que_src);
            stmt.setInt(3, asm_que_duration);
            stmt.setInt(4, asm_round_duration);
            stmt.setBoolean(5, asm_que_hint_ind);
            stmt.setBoolean(6, asm_neg_score_ind);
            stmt.setBoolean(7, asm_save_score_ind);
            stmt.setInt(8, asm_trump_num);
            stmt.setInt(9, asm_trump_multiplier);
            stmt.setInt(10, asm_diff_multiplier);
            stmt.setTimestamp(11, asm_daily_start_datetime);
            stmt.setTimestamp(12, asm_daily_end_datetime);
            stmt.setBoolean(13, asm_score_dec_ind);
            stmt.setBoolean(14, asm_show_ans_ind);
            stmt.setString(15, asm_type);
            stmt.setString(16, asm_mode);
            stmt.setString(17, asm_rom_host);
            stmt.setInt(18, asm_rom_port);
            stmt.setBoolean(19, asm_chat_ind);
            stmt.setLong(20, asm_res_id);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update module, id = " + asm_res_id );
            stmt.close();    
            return;
        }


    public void del(Connection con)
        throws SQLException, cwException {
            
            String SQL = " DELETE FROM Assessment WHERE asm_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_res_id);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to delete assessment, id = " + asm_res_id);
            stmt.close();
            return;
            
        }



    public void get(Connection con)
        throws SQLException, cwException {
            
            String SQL = " SELECT asm_que_num, asm_que_src, asm_que_duration, asm_round_duration, "
                       + "   asm_que_hint_ind, asm_neg_score_ind, asm_save_score_ind, "
                       + "   asm_trump_num, asm_trump_multiplier, asm_diff_multiplier, "
                       + "   asm_daily_start_datetime, asm_daily_end_datetime, asm_score_dec_ind, "
                       + "   asm_show_ans_ind, asm_type, asm_mode, asm_rom_host, asm_rom_port, asm_www_port, asm_chat_ind "
                       + " FROM Assessment WHERE asm_res_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_res_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                asm_que_num = rs.getInt("asm_que_num");
                asm_que_src = rs.getString("asm_que_src");
                asm_que_duration = rs.getInt("asm_que_duration");
                asm_round_duration = rs.getInt("asm_round_duration");
                asm_que_hint_ind = rs.getBoolean("asm_que_hint_ind");
                asm_neg_score_ind = rs.getBoolean("asm_neg_score_ind");
                asm_save_score_ind = rs.getBoolean("asm_save_score_ind");
                asm_trump_num = rs.getInt("asm_trump_num");
                asm_trump_multiplier = rs.getInt("asm_trump_multiplier");
                asm_diff_multiplier = rs.getInt("asm_diff_multiplier");
                asm_daily_start_datetime = rs.getTimestamp("asm_daily_start_datetime");
                asm_daily_end_datetime = rs.getTimestamp("asm_daily_end_datetime");
                asm_score_dec_ind = rs.getBoolean("asm_score_dec_ind");
                asm_show_ans_ind = rs.getBoolean("asm_show_ans_ind");
                asm_type = (rs.getString("asm_type")).trim();
                asm_mode = (rs.getString("asm_mode")).trim();                
                asm_rom_host = rs.getString("asm_rom_host");
                if( asm_rom_host != null )
                    asm_rom_host = asm_rom_host.trim();
                else
                    asm_rom_host = new String();
                asm_rom_port = rs.getInt("asm_rom_port");
                asm_www_port = rs.getInt("asm_www_port");
                asm_chat_ind = rs.getBoolean("asm_chat_ind");                
            } else
                throw new cwException("Failed to get assessment, res_id = " + asm_res_id);
                
            stmt.close();
            return;
        }


    public static String getMode(Connection con, long res_id)
        throws SQLException, cwException {
            
            String SQL = " SELECT asm_mode FROM Assessment WHERE asm_res_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();            
            String mode;
            if( rs.next() )
                mode = (rs.getString("asm_mode")).trim();
            else
                throw new cwException("Failed to get assessment mode, res_id = " + res_id );
                
            stmt.close();
            return mode;
        }

    public void updAsmMode(Connection con)
        throws SQLException, cwException {
            
            String SQL = " UPDATE Assessment SET asm_mode = ? WHERE asm_res_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, asm_mode);
            stmt.setLong(2, asm_res_id);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update assessment mode.");
            stmt.close();
            return;
        }

}