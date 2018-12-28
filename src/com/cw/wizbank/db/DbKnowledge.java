package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.util.cwSQL;

public class DbKnowledge {
	
	/**
	 * 根据知识目录id获取知识id
	 * @param kbc_id 知识目录id
	 */
	public static List<Long> getKbiIdByKbcId(Connection con, long kbc_id) throws SQLException {
		String sql = " select kic_kbi_id from kb_item_cat where kic_kbc_id = ? ";
		List<Long> kbi_id_list = new ArrayList<Long>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, kbc_id);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			kbi_id_list.add(rs.getLong("kic_kbi_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return kbi_id_list;
	}
	
	/**
	 * 根据知识目录id获取知识附件id
	 * @param kbc_id 知识目录id
	 */
	public static List<Long> getKbaIdByKbcId(Connection con, long kbc_id) throws SQLException {
		String sql = " select kia_kba_id from kb_item_attachment where kia_kbi_id in (select kic_kbi_id from kb_item_cat where kic_kbc_id = ?) ";
		List<Long> kba_id_list = new ArrayList<Long>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, kbc_id);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			kba_id_list.add(rs.getLong("kia_kba_id"));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return kba_id_list;
	}
	
	/**
	 * 删除知识目录
	 * @param kbc_id 知识目录id
	 */
	public static void delKbCatalog(Connection con, long kbc_id) throws SQLException {
		String sql = " delete kb_catalog where kbc_id = ? ";
		PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, kbc_id);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    	}
	}

	/**
	 * 删除知识
	 * @param kbi_id 知识id
	 */
	public static void delKbItem(Connection con, long kbi_id) throws SQLException {
		String sql = " delete kb_item where kbi_id  = ? ";
		PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, kbi_id);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    	}
	}
	
	/**
	 * 根据知识目id录删除知识与目录的关联
	 * @param kbc_id 知识目录id
	 */
	public static void delKbItemCat(Connection con, long kbc_id) throws SQLException {
		String sql = " delete kb_item_cat where kic_kbc_id = ? ";
		PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, kbc_id);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    	}
	}
	
	/**
	 * 删除知识附件
	 * @param kba_id 知识附件id
	 */
	public static void delKbAttachment(Connection con, long kba_id) throws SQLException {
		String sql = " delete kb_attachment where kba_id = ? ";
		PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, kba_id);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    	}
	}
	
	/**
	 * 根据知识id删除知识与附件的关联
	 * @param kbi_id 知识id
	 */
	public static void delKbItemAttachment(Connection con, long kbi_id) throws SQLException {
		String sql = " delete kb_item_attachment where kia_kbi_id = ? ";
		PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, kbi_id);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    	}
	}
	
	/**
	 * 根据知识标签
	 * @param tag_id 知识标签id
	 */
	public static void delTag(Connection con, long tag_id) throws SQLException {
		String sql = " delete Tag where tag_id = ? ";
		PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, tag_id);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    	}
	}
	
	/**
	 * 根据知识标签id删除知识与标签的关联
	 * @param tag_id 知识标签id
	 */
	public static void delKbItemTag(Connection con, long tag_id) throws SQLException {
		String sql = " delete kb_item_tag where kit_tag_id = ? ";
		PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, tag_id);
    		stmt.executeUpdate();
    	} finally {
    		if(stmt != null){
    			stmt.close();
    		}
    	}
	}
	
}
