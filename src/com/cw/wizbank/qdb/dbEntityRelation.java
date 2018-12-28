package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;

public class dbEntityRelation{

    public static final String ERN_TYPE_USR_PARENT_USG = "USR_PARENT_USG";
    public static final String ERN_TYPE_USG_PARENT_USG = "USG_PARENT_USG";
    public static final String ERN_TYPE_USR_CURRENT_UGR = "USR_CURRENT_UGR";
    public static final String ERN_TYPE_UGR_PARENT_UGR = "UGR_PARENT_UGR";
    public static final String ERN_TYPE_IDC_PARENT_IDC = "IDC_PARENT_IDC";
    public static final String ERN_TYPE_USR_INTEREST_IDC = "USR_INTEREST_IDC";
    public static final String ERN_TYPE_USR_FOCUS_IDC = "USR_FOCUS_IDC";
    
    //for userClassification to userClassification relation
    public static final String ERN_TYPE_PARENT = "_PARENT_";
    // for user to userClassification 
    public static final String ERN_TYPE_CURRENT = "_CURRENT_";

    public long ern_child_ent_id;
    public long ern_ancestor_ent_id;
    public int ern_order;//最顶层的祖先为1，依次增加
    public String ern_type;
    public boolean ern_parent_ind;//1 = ern_ancestor_ent_id是ern_child_ent_id的上一级；0 = 非直接parent关系
    public Timestamp ern_syn_timestamp;
    public boolean ern_remain_on_syn;
    public Timestamp ern_create_timestamp;
    public String ern_create_usr_id;
    
    private String userRelation;    //relation type between entity(e.g. usg) and user
    private String childRelation;  //relation type between entity(e.g. usg) and entity(e.g. usg)
    
    /**
     * 没有特别作用，节俭代码
     * @param rs
     * @return
     * @throws SQLException
     */
    private dbEntityRelation getdbEntityRelationFromResultSet(ResultSet rs) throws SQLException{
    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_ancestor_ent_id = rs.getLong("ern_ancestor_ent_id");
    	dbEr.ern_child_ent_id = rs.getLong("ern_child_ent_id");
    	dbEr.ern_order = rs.getInt("ern_order");
    	dbEr.ern_type = rs.getString("ern_type");
    	dbEr.ern_parent_ind = rs.getBoolean("ern_parent_ind");
    	dbEr.ern_create_timestamp = rs.getTimestamp("ern_create_timestamp");
		
		return dbEr;
    }
    /**
     * 查询EntityRelation
     * @param con
     * @param isParent
     * @return
     * @throws SQLException
     */
    public boolean get(Connection con, boolean isParent) throws SQLException{
        boolean result = false;
        String sql = "SELECT * FROM EntityRelation WHERE ern_ancestor_ent_id = ? AND ern_child_ent_id = ? and ern_type = ?";
        if(isParent){
        	sql += " and ern_parent_ind = ? ";
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, ern_ancestor_ent_id);
        stmt.setLong(index++, ern_child_ent_id);
        stmt.setString(index++, ern_type);
        if(isParent){
        	stmt.setBoolean(index++, true);
        }
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
        	ern_ancestor_ent_id = rs.getLong("ern_ancestor_ent_id");
        	ern_child_ent_id = rs.getLong("ern_child_ent_id");
        	ern_order = rs.getInt("ern_order");
        	ern_type = rs.getString("ern_type");
        	ern_parent_ind = rs.getBoolean("ern_parent_ind");
        	ern_syn_timestamp = rs.getTimestamp("ern_syn_timestamp");
        	ern_remain_on_syn = rs.getBoolean("ern_remain_on_syn");
        	ern_create_timestamp = rs.getTimestamp("ern_create_timestamp");
        	ern_create_usr_id = rs.getString("ern_create_usr_id");
            
            result = true;
        }else{
            result = false;    
        }
        stmt.close();
        return result;
    }
    /**
     * check 给定条件的EntityRelation是否存在
     * @param con
     * @return
     * @throws SQLException
     */
    public boolean checkExist(Connection con) throws SQLException{
        return get(con, true);
    }
    /**
     * 新增EntityRelation记录，自动查找父以上的关系插入
     * @param con
     * @param createUsrId
     * @return boolean
     * @throws SQLException
     */
    public boolean insEr(Connection con, String createUsrId) throws SQLException{
    	//如果存在原来的纪录需要删除
    	Timestamp curTime = cwSQL.getTime(con);
    	if(this.ern_create_timestamp == null){
    		this.ern_create_timestamp = curTime;
    	}
    	this.delAsChild(con, createUsrId, this.ern_create_timestamp);
    	//首先增加上级的上级
    	String ancestorType = this.ern_type;
    	if(this.ern_type.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR)){
    		ancestorType = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
    	} else if(this.ern_type.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)){
    		ancestorType = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
    	} else {
    		//System.out.println("----------------" + this.ern_type + "--");
    	}
    	Vector Ancestors = getAncestors(con, ancestorType, this.ern_ancestor_ent_id);
		int order = 0;
    	for(int i = 0; i < Ancestors.size(); i++){
    		dbEntityRelation ancestor = (dbEntityRelation)Ancestors.elementAt(i);
    		dbEntityRelation dbEr= new dbEntityRelation();
    		dbEr.ern_child_ent_id = this.ern_child_ent_id;
    		dbEr.ern_type = this.ern_type;
    		dbEr.ern_syn_timestamp = this.ern_syn_timestamp;
    		dbEr.ern_remain_on_syn = this.ern_remain_on_syn;
    		dbEr.ern_ancestor_ent_id = ancestor.ern_ancestor_ent_id;
    		dbEr.ern_order = ancestor.ern_order;
    		dbEr.ern_parent_ind = false;
    		dbEr.ins(con, createUsrId);
    		//计算order
    		order++;
    	}
    	//然后增加直接上级的纪录
		this.ern_order = order + 1;
		this.ern_parent_ind = true;
        return this.ins(con, createUsrId);
    }
    /**
     * 新增单条EntityRelation记录
     * @param con
     * @param createUsrId
     * @return
     * @throws SQLException
     */
    public boolean ins(Connection con, String createUsrId) throws SQLException{

    	if(this.ern_create_timestamp == null){
	        Timestamp curTime = cwSQL.getTime(con);
	        this.ern_create_timestamp = curTime;
    	}
    	if(this.ern_create_usr_id == null || this.ern_create_usr_id.length() == 0){
    		this.ern_create_usr_id = createUsrId;
    	}
        
        String insSql = "INSERT INTO EntityRelation (ern_ancestor_ent_id"
        											+", ern_child_ent_id"
        											+", ern_type"
        											+", ern_order"
        											+", ern_parent_ind"
        											+", ern_syn_timestamp"
        											+", ern_remain_on_syn"
        											+", ern_create_usr_id"
        											+", ern_create_timestamp"
        											+") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(insSql);
        int index = 1;
        stmt.setLong(index++, ern_ancestor_ent_id);
        stmt.setLong(index++, ern_child_ent_id);
        stmt.setString(index++, ern_type);
        stmt.setInt(index++, ern_order);
        stmt.setBoolean(index++, ern_parent_ind);
        stmt.setTimestamp(index++, ern_syn_timestamp);
        stmt.setBoolean(index++, ern_remain_on_syn);
        stmt.setString(index++, ern_create_usr_id);
        stmt.setTimestamp(index++, ern_create_timestamp);

        int stmtResult=stmt.executeUpdate();
        
        if(stmt != null) stmt.close();
        if (stmtResult!=1)                            
        {
            con.rollback();
            throw new SQLException("Failed to insert EntityRelation.");
        }
        return true;
    }
    /**
     * 查询EntityRelation记录
     * @param con
     * @param ern_type
     * @param ancestor_ent_id 为0则查询父记录
     * @param child_ent_id 为0则查询子记录
     * @return
     * @throws SQLException
     */
    private Vector getEntityRelations(Connection con, String ern_type, long ancestor_ent_id, long child_ent_id) throws SQLException {
    	Vector vc = new Vector();
    	String sql = "select * from EntityRelation where ern_type=? ";
    	if(ancestor_ent_id > 0){
    		sql += " and ern_ancestor_ent_id =?";
    	}
    	if(child_ent_id > 0){
    		sql += " and ern_child_ent_id =?";
    	}
    	if(ancestor_ent_id ==0 && child_ent_id ==0){
    		sql += " and 1=2";
    	}
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setString(index++, ern_type);
    	if(ancestor_ent_id > 0){
    		stmt.setLong(index++, ancestor_ent_id);
    	}
    	if(child_ent_id > 0){
    		stmt.setLong(index++, child_ent_id);
    	}
    	ResultSet rs = stmt.executeQuery();
    	while(rs.next()){
    		dbEntityRelation dbEr = getdbEntityRelationFromResultSet(rs);
    		vc.addElement(dbEr);
    	}
    	
    	if(stmt != null) stmt.close();
    	return vc;
    }
    /**
     * 查询EntityRelationHistory记录
     * @param con
     * @param erh_child_ent_id
     * @param endTime 如果是取已删除用户关系则endTime必须与Entity的ent_delete_timestamp一致
     * @param ern_type
     * @return
     * @throws SQLException
     */
    public Vector getEntityRelationHistorys(Connection con, long erh_child_ent_id, Timestamp endTime, String ern_type) throws SQLException {
    	Vector vc = new Vector();
    	String sql = "select * from EntityRelationHistory where erh_child_ent_id=?";
    	if(ern_type != null && ern_type.length() >0){
    		sql += " and erh_type = ?";
    	}
    	if(endTime != null){
    		sql += " and erh_end_timestamp=? ";
    	} else {
    		sql += " and erh_end_timestamp = (select max(erh_end_timestamp) from EntityRelationHistory where erh_child_ent_id=?)";
    	}
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++, erh_child_ent_id);
    	if(ern_type != null && ern_type.length() >0){
    		stmt.setString(index++, ern_type);
    	}
    	if(endTime != null){
    		stmt.setTimestamp(index++, endTime);
    	} else {
    		stmt.setLong(index++, erh_child_ent_id);
    	}
    	ResultSet rs = stmt.executeQuery();
    	while(rs.next()){
    		dbEntityRelationHistory dbErh = new dbEntityRelationHistory();
    		dbErh.erh_ancestor_ent_id = rs.getLong("erh_ancestor_ent_id");
    		dbErh.erh_child_ent_id = rs.getLong("erh_child_ent_id");
    		dbErh.erh_order = rs.getInt("erh_order");
    		dbErh.erh_parent_ind = rs.getBoolean("erh_parent_ind");
    		dbErh.erh_type = rs.getString("erh_type");
    		vc.addElement(dbErh);
    	}
    	
    	if(stmt != null) stmt.close();
    	return vc;
    }
    /**
     * get all Ancestors
     * @param con
     * @param ern_type
     * @param child_ent_id
     * @return
     * @throws SQLException
     */
    public Vector getAncestors(Connection con, String ern_type, long child_ent_id) throws SQLException {
    	//this.ern_ancestor_ent_id = 0;
    	Vector vc = getEntityRelations(con, ern_type, 0, child_ent_id);
    	return vc;
    }
    /**
     * get all Childs
     * @param con
     * @param ern_type
     * @param ancestor_ent_id
     * @return
     * @throws SQLException
     */
    public Vector getChilds(Connection con, String ern_type, long ancestor_ent_id) throws SQLException {
    	//this.ern_child_ent_id = 0;
    	Vector vc = getEntityRelations(con, ern_type, ancestor_ent_id, 0);
    	return vc;
    }
    /**
     * delete EntityRelation as Child
     * @param con
     * @param usr_id
     * @param deleteReal USG_USG & UGR_UGR will delete from EntityRelation,but USR_USG & USR_UGR won't. 
     * @throws SQLException
     */
    public void delAsChild(Connection con, String usr_id, Timestamp endTime) throws SQLException{
    	Vector ancestors = getAncestors(con, ern_type, ern_child_ent_id);
    	//删除已有的历史记录
    	if(ancestors != null && ancestors.size()>0){
    		dbEntityRelationHistory.delAll(con, ern_child_ent_id, ern_type);
    	}
    	moveToHistory(con, usr_id, ancestors, endTime);
        
        final String sql = "Delete From EntityRelation where ern_type=? and ern_child_ent_id=?";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setString(index++, ern_type);
		stmt.setLong(index++, ern_child_ent_id);
        stmt.executeUpdate();
        
        if(stmt != null) stmt.close();
    }
    /**
     * delete the links to its children
     * @param con
     * @param usr_id
     * @throws SQLException
     */
    public void delAsAncestor(Connection con, String usr_id) throws SQLException{
    	//this.ern_child_ent_id = 0;
    	initRelationType();
    	Vector ancestors = new Vector();
    	ancestors.addAll(getChilds(con, this.userRelation, ern_ancestor_ent_id));
    	ancestors.addAll(getChilds(con, this.childRelation, ern_ancestor_ent_id));
    	moveToHistory(con, usr_id, ancestors, null);
    	final String del_ancestor = "delete from entityRelation"
									+ " where (ern_type = ? or ern_type = ?)"
									+ " and ern_ancestor_ent_id=? ";
    	
    	PreparedStatement stmt = con.prepareStatement(del_ancestor);
        int index = 1;
        stmt.setString(index++, this.userRelation);
        stmt.setString(index++, this.childRelation);
        stmt.setLong(index++, ern_ancestor_ent_id);
        stmt.executeUpdate();
        
        if(stmt != null) stmt.close();
    }
    /**
     * 作为子对象删除其所有EntityRelation记录(用于删除用户时)
     * @param con
     * @param usr_id
     * @param delTime 必须与Entity的ent_delete_timestamp一致
     * @throws SQLException
     */
    public void delAllEntityRelationAsChild(Connection con, String usr_id, Timestamp delTime) throws SQLException{
    	Vector allAncestors = new Vector();
    	Vector ancetor_type = new Vector();
        final String sql_get = "SELECT EntityRelation.* FROM EntityRelation WHERE ern_child_ent_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(sql_get);
        stmt.setLong(1, this.ern_child_ent_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
        	dbEntityRelation dber = getdbEntityRelationFromResultSet(rs);
        	allAncestors.addElement(dber);
        	if(!ancetor_type.contains(rs.getString("ern_type"))){
        		ancetor_type.add(rs.getString("ern_type"));
        	}
        }
        //删除已有的历史记录
        if(ancetor_type != null && ancetor_type.size()>0){
        	for(int i=0; i<ancetor_type.size(); i++){
            	dbEntityRelationHistory.delAll(con, ern_child_ent_id, (String)ancetor_type.get(i));
        	}
        }
    	
        moveToHistory(con, usr_id, allAncestors, delTime);
        stmt.close();
        final String sql_del = "Delete From EntityRelation where ern_child_ent_id=?";
        
        stmt = con.prepareStatement(sql_del);
		stmt.setLong(1, ern_child_ent_id);
        stmt.executeUpdate();
        
        if(stmt != null) stmt.close();
    }
    /**
     * 把EntityRelation中的记录备份到EntityRelationHistory中
     * @param con
     * @param usr_Id
     * @param EntityRelationVc 从EntityRelation中删除的记录
     * @param updTime
     * @throws SQLException
     */
    private void moveToHistory(Connection con, String usr_Id, Vector EntityRelationVc, Timestamp endTime) throws SQLException{
    	if(endTime == null){
    		endTime = cwSQL.getTime(con);
    	}
    	for(int i = 0; i < EntityRelationVc.size(); i++){
    		dbEntityRelation dbEr = (dbEntityRelation)EntityRelationVc.elementAt(i);
    		dbEntityRelationHistory dbErh = new dbEntityRelationHistory();
    		dbErh.erh_ancestor_ent_id = dbEr.ern_ancestor_ent_id;
    		dbErh.erh_child_ent_id = dbEr.ern_child_ent_id;
    		dbErh.erh_order = dbEr.ern_order;
    		dbErh.erh_start_timestamp = dbEr.ern_create_timestamp;
    		dbErh.erh_parent_ind = dbEr.ern_parent_ind;
    		dbErh.erh_type = dbEr.ern_type;
    		dbErh.erh_end_timestamp = endTime;
    		dbErh.erh_create_timestamp = endTime;//
    		dbErh.erh_create_usr_id = usr_Id;
    		dbErh.ins(con);
    	}
    }
    /**
     * 把EntityRelationHistory中的记录恢复到EntityRelation中
     * @param con
     * @param usr_Id
     * @param EntityRelationHistoryVc 从EntityRelationHistory表中恢复的记录
     * @throws SQLException
     */
    private void moveToEntityRelation(Connection con, String usr_Id, Vector EntityRelationHistoryVc) throws SQLException{
    	Timestamp curTime = cwSQL.getTime(con);
    	for(int i = 0; i < EntityRelationHistoryVc.size(); i++){
    		dbEntityRelationHistory dbErh = (dbEntityRelationHistory)EntityRelationHistoryVc.elementAt(i);
    		dbEntityRelation dbEr = new dbEntityRelation();
    		dbEr.ern_ancestor_ent_id = dbErh.erh_ancestor_ent_id;
    		dbEr.ern_child_ent_id = dbErh.erh_child_ent_id;
    		dbEr.ern_order = dbErh.erh_order;
    		dbEr.ern_type = dbErh.erh_type;
    		dbEr.ern_parent_ind = dbErh.erh_parent_ind;
    		dbEr.ern_syn_timestamp = curTime;//暂时只能恢复用户，所以ern_syn_timestamp默认给当前时间
    		dbEr.ins(con, usr_Id);
    	}
    }
    public void restoreEntityRelation(Connection con, String restore_usr_id, long usr_ent_id, Timestamp endTime, String ern_type) throws SQLException{
    	Vector history = getEntityRelationHistorys(con, usr_ent_id, endTime, ern_type);
    	moveToEntityRelation(con, restore_usr_id, history);
    }
    /**
     * 查询对象的上一级
     * @param con
     * @return
     * @throws SQLException
     */
    public Vector getParentId(Connection con) throws SQLException{
        initRelationType();
        final String sql_get_ancesters = 
	            "SELECT ern_ancestor_ent_id PARENT_ID "
	            + "FROM EntityRelation "
	            + "WHERE ern_child_ent_id = ? "
	            + "AND (ern_type = ? Or ern_type = ?)"
	            + "AND ern_parent_ind = ?";
        // execute the query to get the immediate parent ids into the parent and ancestor vectors
        Vector parentIds = new Vector();
        PreparedStatement stmt = con.prepareStatement(sql_get_ancesters);
        stmt.setLong(1, this.ern_child_ent_id);
        stmt.setString(2, this.userRelation);
        stmt.setString(3, this.childRelation);
        stmt.setBoolean(4, true);
          
        ResultSet rs = stmt.executeQuery();        
        while(rs.next()) {
            Long parentEntId = new Long(rs.getLong("PARENT_ID"));
            parentIds.addElement(parentEntId);
        }
        
        if(stmt != null) stmt.close();
        return parentIds;
    }
    private void initRelationType() {
        if(this.ern_type.equals(ERN_TYPE_USR_FOCUS_IDC)) {
            this.userRelation = ERN_TYPE_USR_FOCUS_IDC;
            this.childRelation = ERN_TYPE_IDC_PARENT_IDC;
        }
        else if(this.ern_type.equals(ERN_TYPE_USR_INTEREST_IDC)) {
            this.userRelation = ERN_TYPE_USR_INTEREST_IDC;
            this.childRelation = ERN_TYPE_IDC_PARENT_IDC;
        }
        else if(this.ern_type.equals(ERN_TYPE_IDC_PARENT_IDC)) {
            this.userRelation = ERN_TYPE_USR_FOCUS_IDC;
            this.childRelation = ERN_TYPE_IDC_PARENT_IDC;
        }
        else if(this.ern_type.equals(ERN_TYPE_USR_CURRENT_UGR)) {
            this.userRelation = ERN_TYPE_USR_CURRENT_UGR;
            this.childRelation = ERN_TYPE_UGR_PARENT_UGR;
        }
        else if(this.ern_type.equals(ERN_TYPE_UGR_PARENT_UGR)) {
            this.userRelation = ERN_TYPE_USR_CURRENT_UGR;
            this.childRelation = ERN_TYPE_UGR_PARENT_UGR;
        }
        else if(this.ern_type.equals(ERN_TYPE_USR_PARENT_USG)) {
            this.userRelation = ERN_TYPE_USR_PARENT_USG;
            this.childRelation = ERN_TYPE_USG_PARENT_USG;
        }
        else if(this.ern_type.equals(ERN_TYPE_USG_PARENT_USG)) {
            this.userRelation = ERN_TYPE_USR_PARENT_USG;
            this.childRelation = ERN_TYPE_USG_PARENT_USG;
        }
        return;
    }
    /**
     * 查询父用户组
     * @param con
     * @param ent_id
     * @return
     * @throws SQLException
     */
    public static dbUserGroup getParentUserGroup(Connection con, long ent_id) throws SQLException{
        dbUserGroup usg = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT usg_display_bil, usg_name, usg_desc, ent_ste_uid, usg_ent_id ");
        sql.append(" FROM UserGroup, Entity, EntityRelation ");
        sql.append(" WHERE (ern_type = ? OR ern_type = ? )");
        sql.append(" AND ern_child_ent_id = ? ");
        sql.append(" AND ern_ancestor_ent_id = usg_ent_id AND usg_ent_id = ent_id ");
        sql.append(" AND ern_parent_ind = ? "); 
        
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setString(1, ERN_TYPE_USR_PARENT_USG);
        stmt.setString(2, ERN_TYPE_USG_PARENT_USG);
        stmt.setLong(3, ent_id);
        stmt.setBoolean(4, true);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            usg = new dbUserGroup(); 
            usg.usg_ent_id = rs.getLong("usg_ent_id");
            usg.ent_id = usg.usg_ent_id;
            usg.ent_ste_uid = rs.getString("ent_ste_uid");
            usg.usg_display_bil = rs.getString("usg_display_bil");
            usg.usg_name = rs.getString("usg_name");
            usg.usg_desc = rs.getString("usg_desc");
        }            
        stmt.close();
        return usg;
    }
    
    public Vector sortUserList(Connection con, Vector v_ent_id, String sort_col, String sort_order) throws SQLException {

        if( v_ent_id == null || v_ent_id.isEmpty() )
            return new Vector();
            
        if( sort_col == null )
            sort_col = "usr_display_bil";
        if( sort_order == null )
            sort_order = "asc";
            
        String SQL = " SELECT ern_child_ent_id "
                   + " FROM EntityRelation , RegUser"
                   + " WHERE ern_type = ? "
                   + " AND ern_child_ent_id IN " + cwUtils.vector2list(v_ent_id)
                   + " AND ern_child_ent_id = usr_ent_id "
                   + " AND ern_parent_ind = ? "
                   + " ORDER BY " + sort_col + " " + sort_order;
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(2, true);
        ResultSet rs = stmt.executeQuery();
        Vector v = new Vector();
        while(rs.next()){
            v.addElement(new Long(rs.getLong("ern_child_ent_id")));
        }
        stmt.close();
        return v;
    }
    /**
     * 查询用户所在用户组的fullpath
     * @param con
     * @param v_ent_id
     * @return Hashtable
     * @throws SQLException
     */
    public Hashtable getUserGroupFullPathHash(Connection con, Vector v_ent_id) throws SQLException {
        
        String SQL = " SELECT DISTINCT ern_child_ent_id, ern_ancestor_ent_id "
                   + " FROM EntityRelation "
                   + " WHERE ern_type = ? "
                   + " AND ern_parent_ind = ? "
                   + " AND ern_child_ent_id IN " + cwUtils.vector2list(v_ent_id);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(2, true);
        ResultSet rs = stmt.executeQuery();
        Hashtable h = new Hashtable();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        while(rs.next()){
        	long child_id = rs.getLong("ern_child_ent_id");
        	long ancestor_id = rs.getLong("ern_ancestor_ent_id");
        	String fullpathStr = entityfullpath.getFullPath(con, ancestor_id);
            h.put(new Long(child_id), fullpathStr);
        }
        stmt.close();
        return h;
    }
    /**
     * 查询下一级
     * @param con
     * @return
     * @throws SQLException
     */
    public Vector getChildUser(Connection con) throws SQLException {
        initRelationType();
        return getSuccessorID(con, this.userRelation);
    }
    public Vector getSuccessorID(Connection con) throws SQLException {

        initRelationType();
        return getSuccessorID(con, this.ern_type);
    }
    private Vector getSuccessorID(Connection con, String successorType) throws SQLException {

    	final String sql_get_successor = 
            " Select ern_child_ent_id From EntityRelation "
    		+ " Where ern_type = ? "
    		+ " And ern_ancestor_ent_id = ? ";
        Vector v_ent_id = new Vector();
        PreparedStatement stmt = con.prepareStatement(sql_get_successor);
        int index = 1;
        stmt.setString(index++, successorType);
        stmt.setLong(index++,this.ern_ancestor_ent_id);
        ResultSet rs = stmt.executeQuery();
        Long tempL;
        while(rs.next()) {
            tempL = new Long(rs.getLong("ern_child_ent_id"));
            if(!v_ent_id.contains(tempL)) {
                v_ent_id.addElement(tempL);
            }
        }
        stmt.close();
        return v_ent_id;
    }
    //*******************************************************
    //  *******************************************************getFullPath
    public static String getFullPath(Connection con, long ernEntIdChild) throws SQLException{
        return getFullPath(con, ernEntIdChild, (WizbiniLoader) null);
    }
    public static String getFullPath(Connection con, long ernEntIdChild, WizbiniLoader wizbini) throws SQLException{
        String result = "";
        String siteName = null;
        String SQL = " SELECT ste_name, ern_ancestor_ent_id, erh_ancestor_ent_id FROM RegUser "
        		+ " inner join acSite on (usr_ste_ent_id = ste_ent_id) "
                + " inner join Entity on (usr_ent_id = ent_id) "
                + " left join EntityRelation "
                + " on (usr_ent_id = ern_child_ent_id and ern_type = ? and ern_parent_ind = ?) "
                + " left join EntityRelationHistory "
                + " on (usr_ent_id = erh_child_ent_id and erh_type = ? and erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp) "
                + " WHERE usr_ent_id = ?";
        PreparedStatement stmt = con.prepareStatement(SQL);
        
        int index = 1;
        stmt.setString(index++, ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setLong(index++, ernEntIdChild);
                                    
        ResultSet rs = stmt.executeQuery();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        if (rs.next()){
        	long ancestor_id = rs.getLong("ern_ancestor_ent_id");
        	if(ancestor_id == 0){
        		ancestor_id = rs.getLong("erh_ancestor_ent_id");
        	}
            result = entityfullpath.getFullPath(con, ancestor_id);
            siteName = rs.getString("ste_name");
        }
        stmt.close();

        if (wizbini!=null && wizbini.cfgSysSetupadv.getOrganization().isMultiple()){
            result = siteName + wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator() +result;
        }

        return result;
    }    

	public static String getFullPath(Connection con, long ernEntIdChild, String ent_type) throws SQLException {
		String result = "";
		String SQL = " SELECT  ern_child_ent_id,ern_ancestor_ent_id FROM EntityRelation " 
		+ " WHERE ern_child_ent_id = ? and ern_type = ? and ern_parent_ind = ?";
		PreparedStatement stmt = con.prepareStatement(SQL);

		int index = 1;
		stmt.setLong(index++, ernEntIdChild);
		stmt.setString(index++, ent_type);
		stmt.setBoolean(index++, true);
		ResultSet rs = stmt.executeQuery();
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		if (rs.next()) {
			// 用户的fullPath是其所在用户组/职级的fullpath
			if (ent_type.startsWith(dbEntity.ENT_TYPE_USER)) {
				long ancestor_id = rs.getLong("ern_ancestor_ent_id");
				result = entityfullpath.getFullPath(con, ancestor_id);
			} else {
				long child_id = rs.getLong("ern_child_ent_id");
				result = entityfullpath.getFullPath(con, child_id);
			}
		}

		stmt.close();
		return result;
	}
    //*******************************************************************
    //*********************************************getAncestorLst
    public static String getAncestorListSql(long ern_child_ent_id, String ern_type){
    	
    	String sql = " Select ern_ancestor_ent_id From EntityRelation "
		        + " Where ern_type = '" + ern_type
		        + "' And ern_child_ent_id =  " + ern_child_ent_id;
    	return sql;
    }
    public Vector getAncestorList2Vc(Connection con, boolean includeSelf) throws SQLException {
    	
    	Vector vc = new Vector();
    	String sql = " Select ern_ancestor_ent_id From EntityRelation "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
	        + " Where ern_child_ent_id = ? "
	        + " And ern_type = ? "
	        + " order by ern_order";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, this.ern_child_ent_id);
        stmt.setString(index++, this.ern_type);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
        	long ent_id = rs.getLong("ern_ancestor_ent_id");
        	vc.add(new Long(ent_id));
        }
        
        if(includeSelf){
        	vc.add(new Long(this.ern_child_ent_id));
        }
        stmt.close();
        return vc;
    }
    public static Vector getGroupAncestorList2Vc(Connection con, long ern_child_ent_id, boolean includeSelf) throws SQLException{
    	Vector vc = new Vector();
    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_child_ent_id = ern_child_ent_id;
    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
    	vc = dbEr.getAncestorList2Vc(con, includeSelf);
    	return vc;
    }
    /**
     * Get user current group/grade/industry ancester in vector format store in a hashtable
     */
    public Hashtable getUserAncester(Connection con, long usr_ent_id) throws SQLException {
        final String sql_get_ancester = 
            "SELECT ern_child_ent_id,ern_ancestor_ent_id FROM EntityRelation, Entity "
	        	+ " WHERE ern_ancestor_ent_id = ent_id and ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL"
	        	+ " AND ern_child_ent_id = ? AND ern_type = ? ";

        final String UserAncesterDimension[] = {
                                                ERN_TYPE_USR_CURRENT_UGR
                                               ,ERN_TYPE_USR_FOCUS_IDC
                                               ,ERN_TYPE_USR_PARENT_USG
                                               };
        
        // object to be returned, containing ancesters of different dimensions
        Hashtable UserAncester = new Hashtable();
        // to store all distinct ancesters
        Vector vec_ancester[] = new Vector[UserAncesterDimension.length];
        for (int i = 0; i < UserAncesterDimension.length; i++) {
            PreparedStatement stmt = con.prepareStatement(sql_get_ancester);
            int idx = 1;
            stmt.setLong(idx++, usr_ent_id);
            stmt.setString(idx++, UserAncesterDimension[i]);
            ResultSet rs = stmt.executeQuery();
            vec_ancester[i] = new Vector();
            while (rs.next()) {
            	//ensure this : if(UserAncesterDimension[i].startsWith(dbEntity.ENT_TYPE_USER)){
            		vec_ancester[i].addElement(new Long(rs.getLong("ern_ancestor_ent_id")));
            	//}
            }
            
            if (vec_ancester[i].size() > 0) {
                UserAncester.put(UserAncesterDimension[i], vec_ancester[i]);
            }
            stmt.close();
        }
        
        return UserAncester;
    }
    /*
    The temp table contains all the usr_ent_id
    */
    public static Hashtable getGroupEntIdRelation(Connection con, String tempTableName, String colName) throws SQLException {
        
        Hashtable hash = new Hashtable();
//        PreparedStatement stmt = con.prepareStatement("select distinct ern_child_ent_id, usg_ent_id from UserGroup, EntityRelation, " + tempTableName + " where ern_child_ent_id = " + colName + " and ern_ancestor_ent_id = usg_ent_id and ern_parent_ind=? ");
        String sql = "select distinct " + colName + ", usg_ent_id" 
        		+ " from " + tempTableName 
        		+ " left join EntityRelation on (ern_child_ent_id = " + colName + " and ern_parent_ind = ? and ern_type = 'USR_PARENT_USG')"
        		+ " left join UserGroup on (usg_ent_id = ern_ancestor_ent_id) ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setBoolean(1, true);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            hash.put(rs.getLong(colName), rs.getLong("usg_ent_id"));
        }
        
        stmt.close();
        return hash;
    }
    public static Hashtable getIdcRelation(Connection con, String tempTableName, String colName) throws SQLException {
        
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("select distinct ern_child_ent_id, idc_display_bil from IndustryCode, EntityRelation, " + tempTableName + " where ern_child_ent_id = " + colName + " and ern_ancestor_ent_id = idc_ent_id and ern_parent_ind=?");
        stmt.setBoolean(1, true);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Long ent_id = new Long(rs.getLong("ern_child_ent_id"));
            String bil = rs.getString("idc_display_bil");
            Vector vec = (Vector)hash.get(ent_id);
            if (vec == null) {
                vec = new Vector();
            }
            vec.addElement(bil);
            hash.put(ent_id, vec);
        }
        
        stmt.close();
        return hash;
    }    
    /*
    The temp table contains all the usr_ent_id
    */
    public static Hashtable getGradeRelation(Connection con, String tempTableName, String colName) throws SQLException {
        
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("select ern_child_ent_id, ugr_display_bil from UserGrade, EntityRelation, " + tempTableName + " where ern_child_ent_id = " + colName + " and ern_ancestor_ent_id = ugr_ent_id and ern_parent_ind=? ");
        stmt.setBoolean(1, true);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            hash.put(rs.getLong("ern_child_ent_id"), rs.getString("ugr_display_bil"));
        }
        
        stmt.close();
        return hash;
    }

    public static Hashtable getClassRelation(Connection con, String tempTableName, String colName, String ern_type) throws SQLException {
        
        Hashtable hash = new Hashtable();
        String SQL = " SELECT ern_child_ent_id, ucf_display_bil "
                   + " FROM UserClassification, EntityRelation, " + tempTableName 
                   + " WHERE ern_child_ent_id = " + colName
                   + " AND ern_ancestor_ent_id = ucf_ent_id "
                   + " AND ern_type = ? and ern_parent_ind = ?";
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setString(index++, ern_type);
        stmt.setBoolean(index++, true);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Long ent_id = new Long(rs.getLong("ern_child_ent_id"));
            String bil = rs.getString("ucf_display_bil");
            Vector vec = (Vector)hash.get(ent_id);
            
            if (vec == null) {
                vec = new Vector();
            }
            
            vec.addElement(bil);
            hash.put(ent_id, vec);
        }
        
        stmt.close();
        return hash;
    }
    /**
     * 因为性能以及实际使用的原因暂时只查询用户的记录
     * @param con
     * @return
     * @throws SQLException
     */
    public Vector getLatestRecord(Connection con) throws SQLException {
        Vector ernVector = new Vector();
        String sql_ern = "SELECT ern_ancestor_ent_id, ern_child_ent_id, ern_type FROM EntityRelation where ern_child_ent_id = ? and ern_type = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_ern);
        stmt.setLong(1, ern_child_ent_id);
        stmt.setString(2, ern_type);
        //stmt.setBoolean(3, true);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
        	dbEntityRelation dbErn = new dbEntityRelation();
        	dbErn.ern_ancestor_ent_id = rs.getLong("ern_ancestor_ent_id");
        	dbErn.ern_child_ent_id = rs.getLong("ern_child_ent_id");
        	dbErn.ern_type = rs.getString("ern_type");
        	ernVector.addElement(dbErn);
        }
        stmt.close();
        //get the deleted one
        if(ernVector == null || ernVector.size() == 0){
        	String sql_erh = "SELECT erh_ancestor_ent_id, erh_child_ent_id, erh_type FROM EntityRelationHistory, Entity where erh_child_ent_id = ent_id and erh_end_timestamp = ent_delete_timestamp and ent_delete_timestamp is not null and erh_child_ent_id = ? and erh_type = ? ";
            PreparedStatement stmt2 = con.prepareStatement(sql_erh);
            stmt2.setLong(1, ern_child_ent_id);
            stmt2.setString(2, ern_type);
           // stmt2.setBoolean(3, true);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()){
            	dbEntityRelation dbErn2 = new dbEntityRelation();
            	dbErn2.ern_ancestor_ent_id = rs2.getLong("erh_ancestor_ent_id");
            	dbErn2.ern_child_ent_id = rs2.getLong("erh_child_ent_id");
            	dbErn2.ern_type = rs2.getString("erh_type");
            	ernVector.addElement(dbErn2);
            }
            stmt2.close();
        }
        return ernVector;
    }
    
    public static String getUsgDisplayBilByUsrEntId(Connection con, long usr_ent_id) throws SQLException {
    	String result = null;
        String sql = " SELECT usg_display_bil "
        	       + " FROM UserGroup, entityRelation "
        	       + " where ern_parent_ind = ? "
        	       + "   AND ern_type = ? "
        	       + "   AND ern_ancestor_ent_id = usg_ent_id "
        	       + "   AND ern_child_ent_id = ? ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        stmt.setBoolean(index++, true);
	        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
	        stmt.setLong(index++, usr_ent_id);
	        
	        rs = stmt.executeQuery();
	        if (rs.next()) {
	        	result = rs.getString("usg_display_bil");
	        }
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }

        return result;
	}
    

    public static long getParentId(Connection con, long child_ent_id ,String type) throws SQLException{
        String SQL = 
               "SELECT ern_ancestor_ent_id PARENT_ID "
               + "FROM EntityRelation "
               + "WHERE ern_child_ent_id = ? "
               + "AND ern_type = ? "
               + "AND ern_parent_ind = ?";
       // execute the query to get the immediate parent ids into the parent and ancestor vectors
       long parentId = 0;
       PreparedStatement stmt = con.prepareStatement(SQL);
       stmt.setLong(1, child_ent_id);
       stmt.setString(2, type);
       stmt.setBoolean(3, true);
         
       ResultSet rs = stmt.executeQuery();        
       if(rs.next()) {
           parentId = rs.getLong("PARENT_ID");
       }
       
       if(stmt != null) stmt.close();
       return parentId;
   }
    
    
    public static Vector getChild(Connection con, long ent_id, String[] types)throws SQLException{
		String sql = "";
		sql += " select ern_child_ent_id, ern_ancestor_ent_id, ern_type,ern_syn_timestamp, ern_order from entityRelation ";
		sql += " 	where ern_child_ent_id in(select ern_child_ent_id from entityRelation where ern_ancestor_ent_id = ?)";
		sql += " 		and ern_parent_ind = 1  " ;
		if(types != null && types.length > 0){
			sql += "and ern_type in(";
			for(int i = 0; i < types.length; i++){
				sql += "?";
				if(i < (types.length-1)){
					sql += ",";
				}
			}
			sql += ")";
		}
		sql += " order by ern_order ";

		Vector child_vec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, ent_id);
			if(types != null && types.length > 0){
				for(int i = 0; i < types.length; i++){
					stmt.setString(index++, types[i]);
				}
			}
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				dbEntityRelation dbEr = new dbEntityRelation();
		    	dbEr.ern_child_ent_id = rs.getLong("ern_child_ent_id");
		    	dbEr.ern_ancestor_ent_id = rs.getLong("ern_ancestor_ent_id");
		    	dbEr.ern_type = rs.getString("ern_type");
		    	dbEr.ern_syn_timestamp = rs.getTimestamp("ern_syn_timestamp");
		    	child_vec.add(dbEr);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return  child_vec;
	}
} 