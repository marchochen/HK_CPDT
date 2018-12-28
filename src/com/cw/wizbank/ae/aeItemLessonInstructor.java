/*
 * Created on 2004-10-18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.ae;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;


/**
 * @author christ
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class aeItemLessonInstructor {
    
    long ili_ils_id;
    long ili_usr_ent_id;
    String ili_create_usr_id;
    Timestamp ili_create_timestamp;
    
	public void ins(Connection con)
		throws SQLException, qdbException, cwSysMessage {
		if (exist(con)) {//The instructor is already the instructor of this lesson
			return;
		}
		//aeItemLesson.checkTimeConflict(con, ili_usr_ent_id, ili_ils_id);
		String sql =
				"insert into aeItemLessonInstructor(ili_ils_id,ili_usr_ent_id,ili_create_usr_id,ili_create_timestamp) "
                    +" values(?,?,?,?) ";
        PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, ili_ils_id);
		pstmt.setLong(2, ili_usr_ent_id);
        pstmt.setString(3,ili_create_usr_id);
        pstmt.setTimestamp(4,dbUtils.getTime(con));
        pstmt.executeUpdate();
        pstmt.close();
	}
    public void del(Connection con)
        throws SQLException{
            String sql = "DELETE FROM aeItemLessonInstructor WHERE ili_ils_id = ? and ili_usr_ent_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, ili_ils_id);
            pstmt.setLong(2, ili_usr_ent_id);
            pstmt.executeUpdate();
            pstmt.close();
    }
    
    public static void delByLesson(Connection con, long ili_ils_id)
        throws SQLException{
            String sql = "delete from aeItemLessonInstructor where ili_ils_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, ili_ils_id);
            pstmt.executeUpdate();
            pstmt.close();
        }


public static void delByItem(Connection con, long itm_id,long[] exclude_ins_entIds) throws SQLException {
    PreparedStatement stmt = null;
    try {
        String SQL= "delete from aeItemLessonInstructor where ili_ils_id in (select ils_id from aeItemLesson where ils_itm_id = ?)";
        if(exclude_ins_entIds != null && exclude_ins_entIds.length > 0){
            SQL +=" and ili_usr_ent_id not in "+ cwUtils.array2list(exclude_ins_entIds);
        }
        
        stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.executeUpdate();
    }finally {
        if(stmt!=null) stmt.close();
    }
}

   
    public static boolean isAnInstrOfAnItm(Connection con,long ili_usr_ent_id,long itm_id) throws SQLException, qdbException{
        String sql = "SELECT ili_ils_id FROM aeItemLessonInstructor WHERE ili_usr_ent_id = ? "                   + "AND ili_ils_id in (SELECT ils_id from aeItemLesson where ils_itm_id = ? )";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,ili_usr_ent_id);
        pstmt.setLong(2,itm_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
        	pstmt.close();
            return true;
        }else{
        	pstmt.close();
            return false;
        }
    }
	public boolean exist(Connection con) throws SQLException {
		String sql =
			"select * from aeItemLessonInstructor "
				+ "where ili_usr_ent_id = ? and ili_ils_id = ? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, ili_usr_ent_id);
		pstmt.setLong(2, ili_ils_id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			pstmt.close();
			return true;
		} else {
			pstmt.close();
			return false;
		}
	}
}
