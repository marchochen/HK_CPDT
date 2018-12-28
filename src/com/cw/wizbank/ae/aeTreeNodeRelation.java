package com.cw.wizbank.ae;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Vector;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;

public class aeTreeNodeRelation {
	public static final String TNR_TYPE_TND_PARENT_TND="TND_PARENT_TND";
	public static final String TNR_TYPE_ITEM_PARENT_TND="ITEM_PARENT_TND";
	
	public Vector getFatherTnds(Connection con,long father_tnd_id) throws SQLException{
		String sql="select tnr_ancestor_tnd_id from aeTreeNodeRelation where tnr_type=? and tnr_child_tnd_id=? order by tnr_order asc" ;
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, TNR_TYPE_TND_PARENT_TND);
		stmt.setLong(2, father_tnd_id);
		ResultSet rs = stmt.executeQuery();
		Vector tndVc= new Vector();
		while(rs.next()){
			tndVc.add(new Long(rs.getLong("tnr_ancestor_tnd_id")));
		}
		if(stmt !=null )stmt.close();
		return tndVc;
	}
	
	public boolean ins(Connection con,String usr_ste_usr_id, String tnr_type, Vector tndVc, long cur_tnd_id) throws SQLException{
		//删除已经存在的记录
		delTnr(con, tnr_type, cur_tnd_id);
				
		String sql="insert into aeTreeNodeRelation(tnr_child_tnd_id ,tnr_ancestor_tnd_id,tnr_type,tnr_order ,tnr_parent_ind ,tnr_remain_on_syn ,tnr_create_timestamp,tnr_create_usr_id )" +
				" values(?,?,?,?,?,?,?,?)" ;
		Iterator iter= tndVc.iterator();
		Timestamp cur_time=cwSQL.getTime(con);
		int tndSize =tndVc.size();
		int order=1;
		while(iter.hasNext()){
			Long temp_tnd=(Long)iter.next();
			int tnd_id=temp_tnd.intValue();
			PreparedStatement stmt = con.prepareStatement(sql);
			int index =1;
			stmt.setLong(index++,cur_tnd_id);
			stmt.setInt(index++, tnd_id);
			stmt.setString(index++, tnr_type);
			stmt.setInt(index++, order++);
			if(order==tndSize+1){
				stmt.setBoolean(index++, true);
			}else{
				stmt.setBoolean(index++, false);
			}
			stmt.setInt(index++, 0);
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, usr_ste_usr_id);
		    int stmtResult=stmt.executeUpdate();
	        
	        if(stmt != null) stmt.close();
	        if (stmtResult!=1)                            
	        {
	            con.rollback();
	            throw new SQLException("Failed to insert aeTreeNodeRelation.");
	        }
		}
		return true;
	}
	
	public void delTnr(Connection con, String tnr_type, long cur_tnd_id) throws SQLException{
		String sql="delete from aeTreeNodeRelation where tnr_child_tnd_id=? and tnr_type=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, cur_tnd_id);
		stmt.setString(2, tnr_type);
		stmt.executeUpdate();
		if(stmt !=null) stmt.close();
	}
	
	public static void delTnrByTnd(Connection con, long cur_tnd_id) throws SQLException{
		String sql="delete from aeTreeNodeRelation where tnr_child_tnd_id=? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, cur_tnd_id);
		stmt.executeUpdate();
		if(stmt !=null) stmt.close();
	}
	
	//更换顶层目录培训中心时，目录的aeTreeNodeRelation关系不变，只需删除和课程的关系;
	public static void delTnrByCata(Connection con, long root_ent_id, long cat_id) throws SQLException {
		String sql ="delete  from aeTreeNodeRelation where tnr_child_tnd_id in (select tnd_id from aeTreeNode where tnd_type = 'ITEM' and tnd_cat_id = ? and tnd_owner_ent_id = ?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, cat_id);
		stmt.setLong(index++, root_ent_id);
		stmt.executeUpdate();
		if (stmt != null)
			stmt.close();
	}
	
	public void insTnr(Connection con,String usr_ste_usr_id, String tnr_type,long cur_tnd_id, long tnd_parent_tnd_id) throws SQLException{
			long father_tnd_id= tnd_parent_tnd_id;		
			Vector tndVc=getFatherTnds(con, father_tnd_id);		
			tndVc.add(new Long(father_tnd_id));
			if(cur_tnd_id !=0)
			ins(con, usr_ste_usr_id, tnr_type, tndVc, cur_tnd_id);
	}
	
	/**
	 * 删除目录的aeTreeNodeRelation关系
	 * @param cat_id 目录id
	 */
	public static void delTnrByCatId(Connection con, long cat_id) throws SQLException {
		String sql = " delete aeTreeNodeRelation where tnr_ancestor_tnd_id in (select tnd_id from aeTreeNode where tnd_cat_id = ?) ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, cat_id);
		stmt.executeUpdate();
		if (stmt != null)
			stmt.close();
	}
	
}
