package com.cw.wizbank.db;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import com.cw.wizbank.db.sql.*;

import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.EntityFullPath;


public class DbUserClassification extends dbEntity{
    
    public static final String UCF_TYPE_ROOT = "ROOT";

    public long ucf_ent_id;
    public String ucf_display_bil;
    public long ucf_ent_id_root;
    public String ucf_type;                  
    public int ucf_seq_no;

    public DbUserClassification(){
    }
    
    public void ins(Connection con)  throws qdbException, qdbErrMessage {
        try{
            super.ins(con);

            // if ok.
            ucf_ent_id = ent_id;

            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_user_classification);
            stmt.setLong(1, ucf_ent_id);
            stmt.setString(2, ucf_display_bil);
            stmt.setLong(3, ucf_ent_id_root);
            stmt.setString(4, ucf_type);
            stmt.setInt(5, ucf_seq_no);
                                    
            if (stmt.executeUpdate()!= 1) {
            	stmt.close();
                con.rollback();
                throw new SQLException("Failed to insert User Classification.");
            }
            stmt.close();
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static String getDisplayBil(Connection con, long ucf_ent_id) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("SELECT ucf_display_bil FROM UserClassification WHERE ucf_ent_id = ? ");
        stmt.setLong(1, ucf_ent_id);
        ResultSet rs = stmt.executeQuery();
        String display_bil = null;
        if (rs.next()){
            display_bil = rs.getString("ucf_display_bil");
        }
        rs.close();
        stmt.close();
        return display_bil;
    }
    
/**
@param usr_id create_usr_id in EntityRelation 
@param type identify related record in update the EntityRelation group name and path
*/
    public void updDesc(Connection con, String usr_id, String type) throws SQLException, qdbErrMessage, qdbException{
            ent_id = ucf_ent_id;
            super.upd(con);

            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_desc_user_classification);
            stmt.setString(1, ucf_display_bil);
            stmt.setLong(2, ucf_ent_id);
            
            if(stmt.executeUpdate() != 1 )
                {
                    // update fails, rollback
            		stmt.close();
                    con.rollback();
                    throw new SQLException("Fails to update user classifcation desc.");
                }
            stmt.close();
            con.commit();
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            entityfullpath.updateChildFullPath(con, ucf_ent_id, ucf_display_bil, dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC);
            return;
    }
  
    // get EntId by ste ugr id and the organisation
    // predefined ent_ste_uid, ent_type, ucf_ent_id_root
    public boolean getBySteUid(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_user_classification_by_ste_uid_n_type);
        
        stmt.setString(1, ent_ste_uid);
        stmt.setString(2, ent_type);
        stmt.setLong(3, ucf_ent_id_root);
                                
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            ucf_ent_id = rs.getLong("ucf_ent_id");
            ucf_display_bil = rs.getString("ucf_display_bil");
            ucf_type = rs.getString("ucf_type");
            ucf_seq_no = rs.getInt("ucf_seq_no");
        }
        stmt.close();
        return true;
    }
/*
    public Vector search(Connection con, String s_display_bil) throws SQLException{
        Vector vtGrade = new Vector();
        ugr_display_bil = "%" + s_display_bil + "%";

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_usergrade_by_display);
        stmt.setString(1, ugr_display_bil);
        stmt.setLong(2, ugr_ent_id_root);
                                
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            DbUserGrade grade = new DbUserGrade();
            grade.ugr_ent_id = rs.getLong("ugr_ent_id");
            grade.ugr_display_bil = rs.getString("ugr_display_bil");
            grade.ugr_ent_id_root = rs.getLong("ugr_ent_id_root");
            grade.ugr_type = rs.getString("ugr_type");
            vtGrade.addElement(grade); 
        }
        stmt.close();
        return vtGrade;
    }
*/  
/*
    public static long getGradeRoot(Connection con, long siteId) throws SQLException{
        long gradeRoot = 0;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_grade_root);
        stmt.setLong(1, siteId);
                                    
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            gradeRoot = rs.getLong("ugr_ent_id");
        }else{
            throw new SQLException("no grade root!!");    
        }
        stmt.close();
        return gradeRoot;
    }
*/
/*
    public static Hashtable getDisplayName(Connection con, String ent_id_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("SELECT ugr_ent_id, ugr_display_bil FROM UserGrade WHERE ugr_ent_id IN " + ent_id_lst);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            hash.put(rs.getString("ugr_ent_id"), rs.getString("ugr_display_bil"));   
        }
        
        stmt.close();
        
        return hash;
    }
*/
/*
    public static boolean checkExist(Connection con, long ent_id) throws SQLException{
        boolean bExist = false;
        PreparedStatement stmt = con.prepareStatement("SELECT ugr_ent_id FROM UserGrade WHERE ugr_ent_id = ? ");
        stmt.setLong(1, ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            bExist = true;
        }else{
            bExist = false;    
        }
        return bExist;
    }
*/    
    // get all possible User Classification in the org
    // @param empty, return with type as key, ent_id as value
    public static Vector getAllUserClassificationTypeInOrg(Connection con, long siteId, Hashtable htRoot) throws SQLException{
        Vector vtEntType = new Vector();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_all_user_classification_type_in_org);
        stmt.setLong(1, siteId);
        stmt.setString(2, UCF_TYPE_ROOT);
        ResultSet rs = stmt.executeQuery();
        
        String type;
        while (rs.next()) {
            type = rs.getString("ent_type");
            vtEntType.addElement(type);
            if (htRoot!=null && !htRoot.containsKey(type)){
                htRoot.put(type, new Long(rs.getLong("ent_id")));                
            }
        }       
        stmt.close();
        return vtEntType;
    }
}