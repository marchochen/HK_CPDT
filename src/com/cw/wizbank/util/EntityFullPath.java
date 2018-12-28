package com.cw.wizbank.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.qdbEnv;
import com.cwn.wizbank.utils.CommonLog;

/**
 * this class loads all userGroup and userGrade FullPath&EntityName into memory
 * fullpath contain itself
 * ancestorLst not contain itself
 * (harvey 2007-12-12)
 */
public class EntityFullPath {
	
	private static EntityFullPath entityfullpath;//系统中唯一的EntityFullPath实例
    private Hashtable entityfullPathHt;//保存所有的EntityFullPathDetail
    
    private final static String SEPARATOR = qdbEnv.GPM_FULL_PATH_SEPARATOR;
    
    private final String sql_get_usergroup = "select ern_child_ent_id, ern_order, ern_ancestor_ent_id, ern_ancestor_ent_id, ern_parent_ind, ern_type "
    	+ " ,ancestor.usg_display_bil ancestor_name, child.usg_display_bil child_name, ancestor.usg_role ancestor_role"
		+ " from EntityRelation, userGroup ancestor, userGroup child" 
		+ " where ancestor.usg_ent_id = ern_ancestor_ent_id and child.usg_ent_id = ern_child_ent_id and ern_type = ?";
    private final String UNION_ALL = " union all ";
	private final String sql_get_usergrade = " select ern_child_ent_id, ern_order, ern_ancestor_ent_id, ern_ancestor_ent_id, ern_parent_ind, ern_type "
		+ " ,ancestor.ugr_display_bil ancestor_name, child.ugr_display_bil child_name, ancestor.ugr_type ancestor_role"
		+ " from EntityRelation, userGrade ancestor, userGrade child" 
		+ " where ancestor.ugr_ent_id = ern_ancestor_ent_id and child.ugr_ent_id = ern_child_ent_id and ern_type = ?";
	private final String ORDER_BY = " order by ern_child_ent_id,ern_order, ern_type";
    
	/**
	 * 初始化
	 * @param con
	 * @throws SQLException
	 */
	private EntityFullPath(Connection con) throws SQLException{
		CommonLog.info("FullPath init start...");
		entityfullPathHt = new Hashtable();
		enclose(con, 0);
		CommonLog.info("FullPath size = " + entityfullPathHt.size());
		CommonLog.info("FullPath init end.");
	}
	/**
	 * 通过该方法取得系统中唯一一个 EntityFullPath 实例化对象
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static EntityFullPath getInstance(Connection con) throws SQLException{
		if(entityfullpath == null){
			entityfullpath = new EntityFullPath(con);
		}
		return entityfullpath;
	}
	/**
	 * 保存单个Group/Grade的相关信息
	 * @author harveytan
	 *
	 */
	public class EntityFullPathDetail{
		public long ent_id;// Group/Grade ID
		public String ent_name;// Group/Grade 名称
		public String fullPathXML;// Group/Grade fullpath
		public Vector childTable;//保存Group/Grade的第一级子Group/Grade

	}
	/**
	 * 初始化用户组/职级关系对象FullPath
	 * @param con
	 * @param ent_id 系统启动初始化时为0，新增用户组/职级时为新增的ent_id
	 * @throws SQLException
	 */
    public void enclose(Connection con, long ent_id) throws SQLException {
    	
    	//ent_id==0,先清空entityfullPathHt
    	if(ent_id == 0){
    		entityfullPathHt.clear();
    	}
    	
    	if(entityfullPathHt == null){
    		entityfullPathHt = new Hashtable();
    	}
    	String get_sql = sql_get_usergroup;
    	if(ent_id > 0){
    		get_sql += " and ern_child_ent_id = ?";
    	}
    	get_sql += UNION_ALL;
    	get_sql += sql_get_usergrade;
    	if(ent_id > 0){
    		get_sql += " and ern_child_ent_id = ?";
    	}
    	get_sql += ORDER_BY;
        PreparedStatement stmt = con.prepareStatement(get_sql);
        int index = 1;
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
        if(ent_id > 0){
        	stmt.setLong(index++, ent_id);
    	}
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
        if(ent_id > 0){
        	stmt.setLong(index++, ent_id);
    	}
        ResultSet rs = stmt.executeQuery();
        String fullpathxml = "";
    	boolean newENT = true;
        while(rs.next()){
        	
        	long child_ent_id = rs.getLong("ern_child_ent_id");
        	long ancestor_ent_id = rs.getLong("ern_ancestor_ent_id");
        	boolean parent_ind = rs.getBoolean("ern_parent_ind");
        	String ancestor_display_bil = rs.getString("ancestor_name");
        	String child_display_bil = rs.getString("child_name");
        	String ancestor_role = rs.getString("ancestor_role");
        	//如果ancestor为Null(如 根目录)则给个空的对象
        	if(entityfullPathHt.get(new Long(ancestor_ent_id)) == null){
        		EntityFullPathDetail fpdroot = new EntityFullPathDetail();
        		fpdroot.ent_id = ancestor_ent_id;
        		fpdroot.ent_name = ancestor_display_bil;
        		fpdroot.fullPathXML = "";
        		fpdroot.childTable = new Vector();
        		entityfullPathHt.put(new Long(ancestor_ent_id),fpdroot);
        	}
        	
        	if(newENT){
        		//根目录不显示在fullPath中
        		if(ancestor_role == null || !ancestor_role.equals("ROOT")){
	        		fullpathxml = ancestor_display_bil;
	        		newENT = false;
	        	}
        	} else {
        		fullpathxml += SEPARATOR + ancestor_display_bil;
        	}
        	if(parent_ind){
        		//如果是根目录下的第一级
        		if(ancestor_role != null && ancestor_role.equals("ROOT")){
        			fullpathxml = child_display_bil;
        		} else {
        			//fullpath包含child的name
        			fullpathxml += SEPARATOR + child_display_bil;
        		}
        		EntityFullPathDetail fpd = new EntityFullPathDetail();
        		fpd.ent_id = child_ent_id;
        		fpd.ent_name = child_display_bil;
    			fpd.fullPathXML = fullpathxml;
    			fpd.childTable = new Vector();
    			entityfullPathHt.put(new Long(child_ent_id), fpd);
    			//把当前对象添加到其父对象的childTable中
    			setThisToParent(ancestor_ent_id, fpd);
    			
    			newENT = true;
        	}
        	
        }
        
        if(stmt != null) stmt.close();
    }
    /**
     * 设置父对象的直接下级
     * @param ancestor_ent_id 操作对象
     * @param fpd 直接下级的Detail
     */
    private void setThisToParent(long ancestor_ent_id, EntityFullPathDetail fpd){
    	EntityFullPathDetail fpdParent = (EntityFullPathDetail)entityfullPathHt.get(new Long(ancestor_ent_id));
    	if(fpdParent != null && fpdParent.childTable != null){
    		fpdParent.childTable.add(fpd);
    	}
    }
    /**
     * 更新子用户组/职级的fullpath(例 : 当修改了用户组/职级的名称时)
     * @param ent_id
     * @param ent_display_bil
     * @throws SQLException 
     */
    public void updateChildFullPath(Connection con, long ent_id, String ent_display_bil, String ern_type) throws SQLException{
    	if(entityfullPathHt != null){
    		EntityFullPathDetail fpd = (EntityFullPathDetail)entityfullPathHt.get(new Long(ent_id));
    		dbEntityRelation dbEr = new dbEntityRelation();
    		dbEr.ern_child_ent_id = ent_id;
    		dbEr.ern_type = ern_type;
    		Vector ancestorId = dbEr.getParentId(con);
    		String ancestorFullPathXml =  this.getFullPath(con, ((Long)ancestorId.elementAt(0)).longValue());
    		fpd.ent_name = ent_display_bil;
    		if(ancestorFullPathXml != null && ancestorFullPathXml.length() > 0){
    			fpd.fullPathXML = ancestorFullPathXml + SEPARATOR + fpd.ent_name;
    		} else {
    			fpd.fullPathXML = fpd.ent_name;
    		}
    		_updateChildPath(fpd.childTable, fpd.fullPathXML);
    	}
    }
    private void _updateChildPath(Vector childTable, String full_path){
    	for(int i = 0; i < childTable.size(); i++){
			EntityFullPathDetail fpdChild = (EntityFullPathDetail)childTable.elementAt(i);
			if(full_path != null && full_path.length() > 0){
				fpdChild.fullPathXML = full_path + SEPARATOR + fpdChild.ent_name;
			} else {
				fpdChild.fullPathXML = fpdChild.ent_name;
			}
			_updateChildPath(fpdChild.childTable, fpdChild.fullPathXML);
		}
    }
    /**
     * 查询fullpath
     * @param ent_id
     * @return
     */
	public String getFullPath(Connection con, long ent_id) {
		String outValue = null;
		if (entityfullPathHt != null) {
			EntityFullPathDetail fpd = (EntityFullPathDetail) entityfullPathHt.get(new Long(ent_id));
			if (fpd != null) {
				outValue = fpd.fullPathXML;
			}
		}
		try {
			if (outValue == null && con != null && ent_id > 0 && !con.isClosed()) {
				enclose(con, ent_id);
				if (entityfullPathHt != null) {
					EntityFullPathDetail fpd = (EntityFullPathDetail) entityfullPathHt.get(new Long(ent_id));
					if (fpd != null) {
						outValue = fpd.fullPathXML;
					}
				}
			}
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		}
		if (outValue == null) {
			outValue = "--";
		}
		return outValue;
	}
    
    /**
     * 查询对象的名称(only for userGroup & userGrade)
     * @param ent_id
     * @return String
     */
    public String getEntityName(Connection con, long ent_id){
    	String name = "";
    	if (entityfullPathHt != null) {
            EntityFullPathDetail fpd = (EntityFullPathDetail) entityfullPathHt.get(new Long(ent_id));
            if (fpd != null) {
            	name = fpd.ent_name;
            }
        }
		try {
			if ((name == null || name.equals("")) && con != null && ent_id > 0 && !con.isClosed()) {

				enclose(con, ent_id);

				if (entityfullPathHt != null) {
					EntityFullPathDetail fpd = (EntityFullPathDetail) entityfullPathHt.get(new Long(ent_id));
					if (fpd != null) {
						name = fpd.ent_name;
					}
				}
			}
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		}
    	return name;
    }
}
