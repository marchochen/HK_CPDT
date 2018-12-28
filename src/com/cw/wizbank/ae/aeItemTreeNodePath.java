package com.cw.wizbank.ae;

import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import com.cw.wizbank.util.*;

public class aeItemTreeNodePath {
    public long inp_itm_id;
    public String inp_full_path;
    public String inp_ancester;

    private static String MULTIPLE_TREENODE_SEPARATOR = "[|]";
    private static String PATH_SEPARATOR = "/";
    private static String ANCESTER_SEPARATOR = ",";
    

    // save full path
    public void save(Connection con) throws SQLException{
        if (isExist(con)){
            del(con);
        }
        ins(con);
    }

    // get full path by itm_id 
    public static String getFullPath(Connection con, long itm_id) throws SQLException{
        String fullPath;
        String sql_get_item_treenode_path = "SELECT inp_full_path FROM aeItemTreeNodePath WHERE inp_itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_item_treenode_path);
        stmt.setLong(1, itm_id);
        
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            fullPath = rs.getString("inp_full_path");
        }else{
            fullPath = null;
        }
        stmt.close();
        return fullPath;
    }
    
    public void delAllPath(Connection con) throws SQLException{
        String sql_del_all_item_treenode_path = "DELETE FROM aeItemTreeNodePath ";
        PreparedStatement stmt = con.prepareStatement(sql_del_all_item_treenode_path);
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void updateAllPath(Connection con) throws SQLException, cwException{
        aeTreeNode tnd = new aeTreeNode();
        Hashtable htTreeNode = tnd.getAllParentTreeNode(con);
        Hashtable htItem = tnd.getAllItemTreeNode(con);
        
        Enumeration enumeration = htItem.keys();
        while (enumeration.hasMoreElements()) {
            aeItemTreeNodePath itemTreeNodePath = new aeItemTreeNodePath();
            Long itmId = (Long)enumeration.nextElement();
            itemTreeNodePath.inp_itm_id = itmId.longValue();
            itemTreeNodePath.generateFullPath(con, htTreeNode, itemTreeNodePath.inp_itm_id, (Vector)htItem.get(itmId));
            
            itemTreeNodePath.save(con);
        }
    }
    
    public void getPath(Connection con, Hashtable htTreeNode, long tndId, StringBuffer path, StringBuffer ancester) throws cwException{
        aeTreeNode tnd = (aeTreeNode)htTreeNode.get(new Long(tndId));
        if (tnd == null){
            throw new cwException("TreeNode not found, tndId: " + tndId);
        }
//        path = tnd.tnd_title;
        if (tnd.tnd_parent_tnd_id !=0){
            getPath(con, htTreeNode, tnd.tnd_parent_tnd_id, path, ancester);
            path.append(PATH_SEPARATOR).append(tnd.tnd_title);
            ancester.append(ANCESTER_SEPARATOR).append(" ").append(tnd.tnd_id).append(" ");
        }else{
            path.append(tnd.tnd_title);
            ancester.append(" ").append(tnd.tnd_id).append(" ");
        }
        return;        
    }
    public void generateFullPath(Connection con, Hashtable htTreeNode, long itm_id, Vector vtItmParentTnd) throws cwException{
        StringBuffer fullPath = new StringBuffer();
        StringBuffer fullAncester = new StringBuffer();
        for (int i=0; i<vtItmParentTnd.size(); i++){
            if (i!=0)   {
                fullPath.append(MULTIPLE_TREENODE_SEPARATOR);
                fullAncester.append(MULTIPLE_TREENODE_SEPARATOR);
            }
            getPath(con, htTreeNode, ((Long)vtItmParentTnd.elementAt(i)).longValue(), fullPath, fullAncester);
        }
        inp_full_path = fullPath.toString();
        inp_ancester = fullAncester.toString();
        
        return;
    }
    
    

    private void ins(Connection con) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        String sql_ins_item_treenode_path = "INSERT INTO aeItemTreeNodePath (inp_itm_id, inp_full_path, inp_ancester, inp_create_timestamp) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql_ins_item_treenode_path);
        stmt.setLong(1, inp_itm_id);
        stmt.setString(2, inp_full_path);
        stmt.setString(3, inp_ancester);
        stmt.setTimestamp(4, curTime);
        
        if (stmt.executeUpdate()!= 1) {
            con.rollback();
            throw new SQLException("Failed to insert ItemTreeNodePath for itm:" + inp_itm_id);
        }
        stmt.close();
    }
    
    // check exist by itm_id
    private boolean isExist(Connection con) throws SQLException{
        String sql_get_item_treenode_path_cnt = "SELECT count(*) as cnt FROM aeItemTreeNodePath WHERE inp_itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_item_treenode_path_cnt);
        stmt.setLong(1, inp_itm_id);
        
        ResultSet rs = stmt.executeQuery();
        int cnt = 0; 
        if(rs.next()) {
            cnt = rs.getInt("cnt");
        }
        stmt.close();
        if (cnt > 0){
            return true;
        }else{
            return false;
        }                                    
    }
    
    // del by itm id
    private void del(Connection con) throws SQLException{
        String sql_del_item_treenode_path = "DELETE FROM aeItemTreeNodePath WHERE inp_itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_del_item_treenode_path);
        stmt.setLong(1, inp_itm_id);
        
        if (stmt.executeUpdate()!= 1) {
            con.rollback();
            throw new SQLException("Failed to delete ItemTreeNodePath for itm:" + inp_itm_id);
        }        
        stmt.close();
    }

}
    