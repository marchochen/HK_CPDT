package com.cw.wizbank.JsonMod.supervise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import com.cw.wizbank.JsonMod.Pagination;
import com.cw.wizbank.JsonMod.commonBean.GoldenManOptionBean;
import com.cw.wizbank.JsonMod.commonBean.JsonTreeBean;
import com.cw.wizbank.JsonMod.commonBean.JsonTreeRootBean;
import com.cw.wizbank.JsonMod.commonBean.OptionBean;
import com.cw.wizbank.JsonMod.supervise.bean.AppBean;
import com.cw.wizbank.JsonMod.supervise.bean.ApprBean;
import com.cw.wizbank.JsonMod.supervise.bean.GroupBean;
import com.cw.wizbank.JsonMod.supervise.bean.StaffBean;
import com.cw.wizbank.JsonMod.supervise.bean.StaffReportBean;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.WzbJsonValueProcessors;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class Supervise {
	public static final long STAFF_TYPE_DIRECT_STAFF=-2;
	public static final long STAFF_TYPE_GROUP_STAFF=-3;
	public static final long STAFF_TYPE_ALL_STAFF=-1;
	public static final String SQL_DIRECT_STAFF="  select usr_ent_id ,usr_ste_usr_id,usr_display_bil,ern_ancestor_ent_id as group_id"
												+"  from RegUser "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
												+ ", SuperviseTargetEntity "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
												+ "  ,entityrelation "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
												+" where spt_source_usr_ent_id = ? and spt_target_ent_id = usr_ent_id  and spt_type = ?  and usr_status = ?  "
												+" and  ern_type='USR_PARENT_USG' and ern_child_ent_id=usr_ent_id and ern_parent_ind=1 "
												+" and spt_eff_start_datetime <= ? and spt_eff_end_datetime >= ?";

	public static final String SQL_GROUP_STAFF=" select distinct usr_ent_id ,usr_ste_usr_id,usr_display_bil ,enr2.ern_ancestor_ent_id as group_id"
										    +" from RegUser "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
										    + ", EntityRelation enr1 "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
										    + ",EntityRelation enr2"+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
										    +" where enr1.ern_child_ent_id = usr_ent_id and usr_status =?  "
										    +" and enr2.ern_type='USR_PARENT_USG' and enr2.ern_child_ent_id=usr_ent_id and enr2.ern_parent_ind=1 " ;


	/**
	 * 我的下属树的顶层部分
	 * @return
	 * @throws SQLException
	 */
	public JsonTreeRootBean getMyStaffTreeRoot(Connection con,long usr_ent_id, String cur_lan) throws SQLException{
		JsonTreeRootBean dirStaff=new JsonTreeRootBean();
		dirStaff.setId(STAFF_TYPE_DIRECT_STAFF);
		dirStaff.setText("DIRECT_STAFF");
		dirStaff.setLeaf(true);
		JsonTreeRootBean groupStaff=new JsonTreeRootBean();
		groupStaff.setId(STAFF_TYPE_GROUP_STAFF);
		groupStaff.setText("GROUP_STAFF");
		groupStaff.setLeaf(false);
		Vector grpVc =getDirSuperviseGroup(con, usr_ent_id, cur_lan);
		groupStaff.setChildren(grpVc);

		JsonTreeRootBean allStaff=new JsonTreeRootBean();
		Vector staffVc =new Vector();
		staffVc.add(dirStaff);
		staffVc.add(groupStaff);
		allStaff.setId(STAFF_TYPE_ALL_STAFF);
		allStaff.setText("allStaff");
		allStaff.setLeaf(false);
		allStaff.setChildren(staffVc);
		return allStaff;
	}
	/**
	 * 获取第一级用户组
	 * @param con
	 * @param spt_source_usr_ent_id
	 * @return
	 * @throws SQLException
	 */
	 public Vector getDirSuperviseGroup(Connection con, long spt_source_usr_ent_id, String cur_lan) throws SQLException {
		 String sql=" select usg_ent_id,usg_display_bil,usg_role from SuperviseTargetEntity,usergroup" +
		 		" where spt_source_usr_ent_id=? and usg_ent_id= spt_target_ent_id";

		 Vector v = new Vector();
	     PreparedStatement stmt = null;
	     stmt = con.prepareStatement(sql);
	     stmt.setLong(1, spt_source_usr_ent_id);
	     ResultSet rs = stmt.executeQuery();
         while(rs.next()) {
        	 JsonTreeBean jtree =new JsonTreeBean();
        	 jtree.setText(rs.getString("usg_display_bil"));
        	 jtree.setId(rs.getLong("usg_ent_id"));
        	 jtree.setUsg_role(rs.getString("usg_role"));
        	 jtree.setCur_lan(cur_lan);
        	 v.add(jtree);
         }
         if(stmt!=null)stmt.close();
		return v;
	 }

	 /**
	  * 获取用户组上下级关系
	  * @param con
	  * @return
	  * @throws SQLException
	  */
	 public Hashtable getDirGroupHS(Connection con)throws SQLException{
		 String sql="select  ern_child_ent_id,ern_ancestor_ent_id from entityrelation where ern_type='USG_PARENT_USG' and ern_parent_ind=1 and ern_order <>1";
		 PreparedStatement stmt = null;
	     stmt = con.prepareStatement(sql);
	     ResultSet rs = stmt.executeQuery();
	     Hashtable hs=new Hashtable();
         while(rs.next()) {
        	 hs.put(String.valueOf(rs.getLong("ern_child_ent_id")), String.valueOf(rs.getLong("ern_ancestor_ent_id")));
         }
         cwSQL.cleanUp(rs, stmt);
		return hs;
	 }

    /**
     * Get a SQL segment of the supervisor's supervised groups as
     * @param con Connection to database
     * @param spt_source_usr_ent_id  user entity id of the supervisor
     * @return SQL segment
     * @throws SQLException
     */
    private static String getAncesterPatternSQL(Connection con, long spt_source_usr_ent_id) throws SQLException {
        long[] grp_ent_ids = ViewSuperviseTargetEntity.getActiveSupervisedGroupEntId(con, spt_source_usr_ent_id);
        StringBuffer SQLBuf = new StringBuffer();
        if(grp_ent_ids == null || grp_ent_ids.length == 0) {
            //to ensure no result will be returned as the user does not have any supervised group
            SQLBuf.append(" and 1=2 ");
        } else {
            SQLBuf.append(" and (");
            for(int i=0 ;i<grp_ent_ids.length; i++) {
                if(i > 0) {
                    SQLBuf.append(" or ");
                }
                SQLBuf.append(" enr1.ern_ancestor_ent_id = ").append(grp_ent_ids[i]);
            }
            SQLBuf.append(" )");
        }
        return SQLBuf.toString();
    }

    /**
     * 获取所有下属
     * @param con
     * @param spt_source_usr_ent_id
     * @param param
     * @return
     * @throws SQLException
     */
	 public Vector getAllStaff(Connection con, long spt_source_usr_ent_id,SuperviseModuleParam param) throws SQLException {
		  StringBuffer SQLBuf = new StringBuffer(512);
	        SQLBuf.append(SQL_DIRECT_STAFF);
	        SQLBuf.append(" union ");
	        SQLBuf.append(SQL_GROUP_STAFF)
	              .append(getAncesterPatternSQL(con, spt_source_usr_ent_id));
	        if(param.getSort()!=null){
	        	SQLBuf.append(" order by ").append(param.getSort()).append(" ").append(param.getDir());
	        }else{
	        	SQLBuf.append(" order by usr_ste_usr_id");
	        }
	        PreparedStatement stmt = null;
	        Vector v = new Vector();
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            stmt.setString(6, dbRegUser.USR_STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            int count =0;
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            while(rs.next()) {
        		if(count>=param.getStart() &&count<(param.getLimit()+param.getStart())){
        			StaffBean staff=new StaffBean();
            		staff.setUsr_ent_id(rs.getLong("usr_ent_id"));
            		staff.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
            		staff.setUsr_display_bil(rs.getString("usr_display_bil"));
            		String groupStr=entityfullpath.getFullPath(con, rs.getLong("group_id"));
            		groupStr=groupStr.replaceAll("/", ">");
            		staff.setGroup(groupStr);
            		v.addElement(staff);
        		}
        		count++;
            }
            param.setTotal_rec(count);
            if(stmt!=null) stmt.close();
	        return v;
	 }

	 /**
	  * 获取列表第一个用户信息
	  * @param con
	  * @param staff_vc
	  * @return
	  * @throws SQLException
	  */
	 public StaffBean getFistStaff(Connection con,Vector staff_vc,loginProfile prof, WizbiniLoader wizbini) throws SQLException{
		 StaffBean staff=null;
		 if(staff_vc!=null&&staff_vc.size()>0){
			 staff= (StaffBean) staff_vc.elementAt(0);
			 String sql =" select urx_extra_43, ugr_display_bil from reguser ,reguserextension,entityrelation,usergrade"
				 +" where usr_ent_id = urx_usr_ent_id and ern_type= ? and ern_parent_ind=1"
				 +" and ern_child_ent_id=? and ern_ancestor_ent_id=ugr_ent_id"
				 +" and ern_child_ent_id=usr_ent_id";
			 PreparedStatement stmt= null;
			 try {
				 stmt = con.prepareStatement(sql);
				 stmt.setString(1, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
				 stmt.setLong(2, staff.getUsr_ent_id());
				 ResultSet rs = stmt.executeQuery();
				 if(rs.next()){
					 staff.setUsr_photo(wizbini.getUserPhotoPath(prof, staff.getUsr_ent_id(), rs.getString("urx_extra_43")));
					 staff.setUgr_display_bil(rs.getString("ugr_display_bil"));
				 }
			 } finally {
				 if (stmt != null) stmt.close();
			 }
		 }
		 return staff;
	 }

	 /**
	  * 获取直属下属
	  * @param con
	  * @param spt_source_usr_ent_id
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public Vector getDirectStaff(Connection con, long spt_source_usr_ent_id ,SuperviseModuleParam param) throws SQLException{
		 StringBuffer SQLBuf = new StringBuffer(512);
	        SQLBuf.append(SQL_DIRECT_STAFF);
	        if(param.getSort()!=null){
	        	SQLBuf.append(" order by ").append(param.getSort()).append(" ").append(param.getDir());
	        }else{
	        	SQLBuf.append(" order by usr_ste_usr_id");
	        }
	        PreparedStatement stmt = null;
	        Vector v = new Vector();
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            ResultSet rs = stmt.executeQuery();
            int count =0;
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            while(rs.next()) {
        		if(count>=param.getStart() &&count<(param.getLimit()+param.getStart())){
        			StaffBean staff=new StaffBean();
            		staff.setUsr_ent_id(rs.getLong("usr_ent_id"));
            		staff.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
            		staff.setUsr_display_bil(rs.getString("usr_display_bil"));
            		String groupStr=entityfullpath.getFullPath(con, rs.getLong("group_id"));
            		groupStr=groupStr.replaceAll("/", ">");
            		staff.setGroup(groupStr);
            		v.addElement(staff);
        		}
        		count++;
            }
            param.setTotal_rec(count);
            if(stmt!=null) stmt.close();
	        return v;
	 }

	 /**
	  * 获取所有的用户组下的下属
	  * @param con
	  * @param spt_source_usr_ent_id
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public Vector getAllGroupStaff(Connection con, long spt_source_usr_ent_id ,SuperviseModuleParam param) throws SQLException{
		 StringBuffer SQLBuf= new StringBuffer();
			  SQLBuf.append(SQL_GROUP_STAFF)
			   		.append(getAncesterPatternSQL(con, spt_source_usr_ent_id));
			if(param.getSort()!=null){
				SQLBuf.append(" order by ").append(param.getSort()).append(" ").append(param.getDir());
			}else{
				SQLBuf.append(" order by usr_ste_usr_id");
			}
			PreparedStatement stmt = null;
			Vector v = new Vector();
			stmt = con.prepareStatement(SQLBuf.toString());
			stmt.setString(1, dbRegUser.USR_STATUS_OK);
			ResultSet rs = stmt.executeQuery();
            int count =0;
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            while(rs.next()) {
        		if(count>=param.getStart() &&count<(param.getLimit()+param.getStart())){
        			StaffBean staff=new StaffBean();
            		staff.setUsr_ent_id(rs.getLong("usr_ent_id"));
            		staff.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
            		staff.setUsr_display_bil(rs.getString("usr_display_bil"));
            		String groupStr=entityfullpath.getFullPath(con, rs.getLong("group_id"));
            		groupStr=groupStr.replaceAll("/", ">");
            		staff.setGroup(groupStr);
            		v.addElement(staff);
        		}
        		count++;
            }
            param.setTotal_rec(count);
            if(stmt!=null) stmt.close();
	        return v;
	 }

	 /**
	  * 获取某个用户组下的下属
	  * @param con
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public Vector getGroupStaff(Connection con, SuperviseModuleParam param)throws SQLException{
		 StringBuffer SQLBuf= new StringBuffer();
		  SQLBuf.append(SQL_GROUP_STAFF)
		  		.append(" and enr1.ern_ancestor_ent_id=? ");
		if(param.getSort()!=null){
			SQLBuf.append(" order by ").append(param.getSort()).append(" ").append(param.getDir());
		}else{
			SQLBuf.append(" order by usr_ste_usr_id");
		}
		PreparedStatement stmt = null;
		Vector v = new Vector();
		stmt = con.prepareStatement(SQLBuf.toString());
		stmt.setString(1, dbRegUser.USR_STATUS_OK);
		stmt.setLong(2, param.getGroup_id());
		ResultSet rs = stmt.executeQuery();
        int count =0;
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        while(rs.next()) {
    		if(count>=param.getStart() &&count<(param.getLimit()+param.getStart())){
    			StaffBean staff=new StaffBean();
        		staff.setUsr_ent_id(rs.getLong("usr_ent_id"));
        		staff.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
        		staff.setUsr_display_bil(rs.getString("usr_display_bil"));
        		String groupStr=entityfullpath.getFullPath(con, rs.getLong("group_id"));
        		groupStr=groupStr.replaceAll("/", ">");
        		staff.setGroup(groupStr);
        		v.addElement(staff);
    		}
    		count++;
        }
        param.setTotal_rec(count);
        if(stmt!=null) stmt.close();
        return v;
	 }

	 /**
	  * 获取下级用户组
	  * @param con
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public Vector getGroupbyGroup(Connection con,SuperviseModuleParam param)throws SQLException{
		 String sql="select usg_ent_id,usg_display_bil from entityrelation, usergroup where ern_type='USG_PARENT_USG'"
			 +" and ern_ancestor_ent_id=? and ern_parent_ind=1 and ern_child_ent_id=usg_ent_id";
		 PreparedStatement stmt = null;
		 Vector v = new Vector();
		 stmt = con.prepareStatement(sql);
		 stmt.setLong(1, param.getGroup_id());
		 ResultSet rs = stmt.executeQuery();
		 Hashtable grphs=getDirGroupHS(con);
         while(rs.next()) {
        	 GroupBean group=new GroupBean();
        	 group.setUsg_ent_id(rs.getLong("usg_ent_id"));
        	 group.setUsg_display_bil(rs.getString("usg_display_bil"));
        	 if(grphs.containsValue(String.valueOf(rs.getLong("usg_ent_id")))){
        		 group.setHasChild(true);
        	 }else{
        		 group.setHasChild(false);
        	 }
        	 v.add(group);
         }
		 if(stmt!=null) stmt.close();
	     return v;
	 }

	 /**
	  * 根据下属类型查询下属
	  * @param con
	  * @param spt_source_usr_ent_id
	  * @param param
	  * @param staffMap
	  * @throws SQLException
	 * @throws cwException
	  */
	 public void getStaffByType(Connection con, long spt_source_usr_ent_id ,SuperviseModuleParam param,HashMap staffMap,loginProfile prof, WizbiniLoader wizbini) throws SQLException, cwException{
		 if(param.getGroup_id()==STAFF_TYPE_DIRECT_STAFF){
			 	Vector dir_staff=getDirectStaff(con, spt_source_usr_ent_id,param);
				staffMap.put("staff_lst", dir_staff);
				staffMap.put("staff", getFistStaff(con, dir_staff, prof, wizbini));
				staffMap.put("total", String.valueOf(param.getTotal_rec()));
		 }else if(param.getGroup_id()==STAFF_TYPE_GROUP_STAFF){
				Vector allGroupStaff= getAllGroupStaff(con, spt_source_usr_ent_id, param);
				staffMap.put("staff_lst", allGroupStaff);
				staffMap.put("staff", getFistStaff(con, allGroupStaff, prof, wizbini));
				staffMap.put("total", String.valueOf(param.getTotal_rec()));
		 }else if(param.getGroup_id()==STAFF_TYPE_ALL_STAFF){
			 	Vector allStaff = getAllStaff(con, spt_source_usr_ent_id, param);
			 	staffMap.put("staff_lst", allStaff);
				staffMap.put("staff", getFistStaff(con, allStaff, prof, wizbini));
				staffMap.put("total", String.valueOf(param.getTotal_rec()));
		 }else{
			Vector groupStaff=getGroupStaff(con, param);
			staffMap.put("staff_lst", groupStaff);
			staffMap.put("staff", getFistStaff(con, groupStaff, prof, wizbini));
			staffMap.put("total", String.valueOf(param.getTotal_rec()));
		 }

	 }

	 /**
	  * 获取某个下属的信息
	  * @param con
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public StaffBean getStaffInfo(Connection con,SuperviseModuleParam param  ,loginProfile prof, WizbiniLoader wizbini) throws SQLException{
		 String sql="select usr_ent_id,usr_ste_usr_id,usr_display_bil,urx_extra_43,ugr_display_bil,usge.ern_ancestor_ent_id as group_id"
			 +" from reguser,reguserextension,entityrelation usge,entityrelation ugre,usergrade"
			 +" where usr_ent_id=urx_usr_ent_id and usr_ent_id=usge.ern_child_ent_id and usr_ent_id=ugre.ern_child_ent_id and usr_ent_id=?"
			 +" and usge.ern_type=? and usge.ern_parent_ind=1"
			 +" and ugre.ern_type=? and ugre.ern_parent_ind=1 and ugre.ern_ancestor_ent_id=ugr_ent_id";
		 PreparedStatement stmt= null;
		 try {
			 stmt = con.prepareStatement(sql);
			 stmt.setLong(1, param.getUsr_ent_id());
			 stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			 stmt.setString(3, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			 ResultSet rs = stmt.executeQuery();
			 StaffBean staff= new StaffBean();
			 EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
			 if(rs.next()){
				 staff.setUsr_ent_id(rs.getLong("usr_ent_id"));
				 staff.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
				 staff.setUsr_display_bil(rs.getString("usr_display_bil"));
				 staff.setUsr_photo(wizbini.getUserPhotoPath(prof, rs.getLong("usr_ent_id"), rs.getString("urx_extra_43")));
				 staff.setUgr_display_bil(rs.getString("ugr_display_bil"));
				 String groupStr=entityfullpath.getFullPath(con, rs.getLong("group_id"));
	     		 groupStr=groupStr.replaceAll("/", ">");
	     		 staff.setGroup(groupStr);
			 }
			 return staff;
		 } finally {
			 if (stmt != null) stmt.close();
		 }
	 }

	 /**
	  * 搜索下属
	  * @param con
	  * @param spt_source_usr_ent_id
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public Vector searchStaff(Connection con, long spt_source_usr_ent_id ,SuperviseModuleParam param) throws SQLException{
		 StringBuffer SQLBuf = new StringBuffer(512);
		 if(STAFF_TYPE_DIRECT_STAFF==param.getGroup_id()){
			 SQLBuf.append(SQL_DIRECT_STAFF)
			 .append(" AND (usr_ste_usr_id LIKE ? OR usr_display_bil LIKE ?) ");
		 }else if(STAFF_TYPE_GROUP_STAFF==param.getGroup_id()){
			 SQLBuf.append(SQL_GROUP_STAFF)
			 .append(" AND (usr_ste_usr_id LIKE ? OR usr_display_bil LIKE ?) ");
			  SQLBuf.append(getAncesterPatternSQL(con, spt_source_usr_ent_id));
		 }else if(param.getGroup_id()>0){
			 SQLBuf.append(SQL_GROUP_STAFF)
			 .append(" AND (usr_ste_usr_id LIKE ? OR usr_display_bil LIKE ?) ");
			 SQLBuf.append("  and enr1.ern_ancestor_ent_id=?");
		 }else{
			 SQLBuf.append(SQL_DIRECT_STAFF)
			 .append(" AND (usr_ste_usr_id LIKE ? OR usr_display_bil LIKE ?) ");
			 SQLBuf.append(" union ");
			 SQLBuf.append(SQL_GROUP_STAFF)
			 .append(" AND (usr_ste_usr_id LIKE ? OR usr_display_bil LIKE ?) ");
			 SQLBuf.append(getAncesterPatternSQL(con, spt_source_usr_ent_id));
		 }

        if(param.getSort()!=null){
        	SQLBuf.append(" order by ").append(param.getSort()).append(" ").append(param.getDir());
        }else{
        	SQLBuf.append(" order by usr_ste_usr_id");
        }
         PreparedStatement stmt = null;
         Vector v = new Vector();
         Timestamp curTime = cwSQL.getTime(con);
         stmt = con.prepareStatement(SQLBuf.toString());

         if(STAFF_TYPE_DIRECT_STAFF==param.getGroup_id()){
        	 stmt.setLong(1, spt_source_usr_ent_id);
        	 stmt.setString(2, "DIRECT_SUPERVISE");
        	 stmt.setString(3, dbRegUser.USR_STATUS_OK);
        	 stmt.setTimestamp(4, curTime);
        	 stmt.setTimestamp(5, curTime);
        	 stmt.setString(6, "%"+param.getSearch_staff_str()+"%");
        	 stmt.setString(7, "%"+param.getSearch_staff_str()+"%");
         }else if(STAFF_TYPE_GROUP_STAFF==param.getGroup_id()){
        	 stmt.setString(1, dbRegUser.USR_STATUS_OK);
        	 stmt.setString(2, "%"+param.getSearch_staff_str()+"%");
        	 stmt.setString(3, "%"+param.getSearch_staff_str()+"%");
         }else if(param.getGroup_id() >0){
        	 stmt.setString(1, dbRegUser.USR_STATUS_OK);
        	 stmt.setString(2, "%"+param.getSearch_staff_str()+"%");
        	 stmt.setString(3, "%"+param.getSearch_staff_str()+"%");
        	 stmt.setLong(4, param.getGroup_id());
         }else{
        	 stmt.setLong(1, spt_source_usr_ent_id);
        	 stmt.setString(2, "DIRECT_SUPERVISE");
        	 stmt.setString(3, dbRegUser.USR_STATUS_OK);
        	 stmt.setTimestamp(4, curTime);
        	 stmt.setTimestamp(5, curTime);
        	 stmt.setString(6, "%"+param.getSearch_staff_str()+"%");
        	 stmt.setString(7, "%"+param.getSearch_staff_str()+"%");
        	 stmt.setString(8, dbRegUser.USR_STATUS_OK);
        	 stmt.setString(9, "%"+param.getSearch_staff_str()+"%");
        	 stmt.setString(10, "%"+param.getSearch_staff_str()+"%");
         }
         ResultSet rs = stmt.executeQuery();
         int count =0;
         EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
         while(rs.next()) {
     		if(count>=param.getStart() &&count<(param.getLimit()+param.getStart())){
     			StaffBean staff=new StaffBean();
        		staff.setUsr_ent_id(rs.getLong("usr_ent_id"));
        		staff.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
        		staff.setUsr_display_bil(rs.getString("usr_display_bil"));
        		String groupStr=entityfullpath.getFullPath(con,rs.getLong("group_id"));
        		groupStr=groupStr.replaceAll("/", ">");
        		staff.setGroup(groupStr);
         		v.addElement(staff);
     		}
     		count++;
         }
         param.setTotal_rec(count);
         if(stmt!=null) stmt.close();
	     return v;
	 }

	 public String searchStaffByXML(Connection con, Vector staffVec, int page, int pagesize) throws SQLException {
		 StringBuffer result = new StringBuffer();
		 Timestamp cur_time = cwSQL.getTime(con);
		 StaffBean staff = null;
		 int total = staffVec.size();
		 if (pagesize == 0) {
             pagesize = dbUserGroup.USG_SEARCH_PAGE_SIZE;
         }
         if (page == 0) {
             page = 1;
         }
		 int start = ((page-1) * pagesize) + 1;
	     int end = page * pagesize;
		 result.append("<group_member_list>");
		 result.append("<search_in_mystaff>true</search_in_mystaff>");
		 result.append("<search time=\"").append(cur_time).append("\" cur_page=\"").append(page).append("\" page_size=\"").append(pagesize).append("\" total=\"").append(total)
   	  		   .append("\"/>").append(dbUtils.NEWL);
		 dbRegUser regUser = new dbRegUser();
		 for (int i = start - 1; i < end && i < total; i++) {
			 staff = (StaffBean)staffVec.get(i);
			 regUser.usr_ent_id = staff.getUsr_ent_id();
			 result.append("<user id=\"").append(cwUtils.esc4XML(staff.getUsr_ste_usr_id()))
			 	       .append("\" ent_id=\"").append(staff.getUsr_ent_id()).append("\">")
			 	   .append("<name display_name=\"").append(cwUtils.esc4XML(staff.getUsr_display_bil())).append("\"/>")
			 	   .append("<full_path>").append(staff.getGroup()).append("</full_path>")
			 	   .append(regUser.getEntityAttributesAsXML(con))
			 	   .append("</user>");
		 }
		 result.append("</group_member_list>");
		 return result.toString();
	 }

	 /**
	  * 将搜索条件转成Json字符串
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public String getSpcJson(SuperviseModuleParam param) throws SQLException{
		 StaffReportBean rpt=new StaffReportBean();
		 rpt.setS_usg_ent_id_lst(param.getS_usg_ent_id_lst());
		 String usr_lst=param.getEnt_id_str();
		 Vector usr_vc=new Vector();
		 if(usr_lst!=null&&usr_lst.length()>0){
			 Vector usr_id=cwUtils.splitToVecString(usr_lst, "~");
			 String usr_name=param.getUsr_name_str();
			 Vector usr_name_vc=cwUtils.splitToVecString(usr_name, "~");
			 for(int i=0; i< usr_id.size(); i++){
				 OptionBean option =new OptionBean();
				 option.setId(Long.parseLong((String)usr_id.elementAt(i)));
				 option.setText((String)usr_name_vc.elementAt(i));
				 usr_vc.add(option);
			 }
			 GoldenManOptionBean gmUsr =new GoldenManOptionBean();
			 gmUsr.setName("goldenman_mystaff_user");
			 gmUsr.setValue(usr_vc);
			 rpt.setGmUsrOption(gmUsr);
		 }
		 String itm_lst=param.getItm_id_str();
		 Vector itm_vc =new Vector();
		 if(itm_lst !=null &&itm_lst.length()>0){
			 Vector itm_id= cwUtils.splitToVecString(itm_lst, "~");
			 String itm_title=param.getItm_title_str();
			 Vector itm_title_vc=cwUtils.splitToVecString(itm_title, "~");
			 for(int i=0; i< itm_id.size(); i++){
				 OptionBean option =new OptionBean();
				 option.setId(Long.parseLong((String)itm_id.elementAt(i)));
				 option.setText((String)itm_title_vc.elementAt(i));
				 itm_vc.add(option);
			 }
			 GoldenManOptionBean gmItm =new GoldenManOptionBean();
			 gmItm.setName("goldenman_mystaff_item");
			 gmItm.setValue(itm_vc);
			 rpt.setGmItmOption(gmItm);
		 }

		 String tnd_lst=param.getTnd_id_str();
		 Vector tnd_vc =new Vector();
		 if(tnd_lst !=null &&tnd_lst.length()>0){
			 Vector tnd_id= cwUtils.splitToVecString(tnd_lst, "~");
			 String tnd_title=param.getTnd_title_str();
			 Vector tnd_title_vc=cwUtils.splitToVecString(tnd_title, "~");
			 for(int i=0; i< tnd_id.size(); i++){

				 OptionBean option =new OptionBean();
				 option.setId(Long.parseLong((String)tnd_id.elementAt(i)));
				 option.setText((String)tnd_title_vc.elementAt(i));
				 tnd_vc.add(option);
			 }
			 GoldenManOptionBean gmTnd =new GoldenManOptionBean();
			 gmTnd.setName("goldenman_mystaff_treenode");
			 gmTnd.setValue(tnd_vc);
			 rpt.setGmTndOption(gmTnd);
		 }
		 rpt.setAtt_create_start_datetime(param.getAtt_create_start_datetime());
		 rpt.setAtt_create_end_datetime(param.getAtt_create_end_datetime());
		 rpt.setAts_id(param.getAts_id());
		 JsonConfig jsonConfig= new JsonConfig();
		 jsonConfig.registerJsonValueProcessor(Timestamp.class, new WzbJsonValueProcessors.DateJsonValueProcessor());
		jsonConfig.setJsonPropertyFilter( new PropertyFilter(){  //属性过滤
	           public boolean apply( Object source, String name, Object value ) {
	              if(value==null){
	                 return true;
	              }
	              return false;
	           }
			});
		 return JSONObject.fromObject(rpt,jsonConfig).toString();
	 }

	 /**
	  * 保存报告模板
	  * @param con
	  * @param prof
	  * @param param
	  * @throws SQLException
	  */
	 public boolean saveStaffRptSpc(Connection con, loginProfile prof ,SuperviseModuleParam param) throws SQLException{
		 boolean result = false;
		 String spcJson= getSpcJson(param);
		 PreparedStatement stmt = null;
		 ResultSet rs = null;
		 String sql_get_rte_id = "select rte_id from ReportTemplate where rte_type='STAFF' and rte_owner_ent_id = ?";
		 long rte_id = 0;
		 try {
			 stmt = con.prepareStatement(sql_get_rte_id);
			 stmt.setLong(1, prof.root_ent_id);
			 rs = stmt.executeQuery();
			 if (rs.next())
				 rte_id = rs.getLong(1);
		 } finally {
			 if (stmt != null)	stmt.close();
			 rs = null;
		 }
		 if (rte_id > 0) {
			 String sql="insert into ReportSpec (rsp_rte_id ,rsp_ent_id ,rsp_title,  rsp_create_usr_id, rsp_create_timestamp, rsp_upd_usr_id, rsp_upd_timestamp, rsp_content)"
				 +" values( ? ,? ,?,  ?, ?, ?, ? ,?)";
			 try {
				 stmt = con.prepareStatement(sql);
				 int index =1;
				 Timestamp curTime = cwSQL.getTime(con);
				 stmt.setLong(index++, rte_id);
				 stmt.setLong(index++, prof.usr_ent_id);
				 stmt.setString(index++, param.getRspTitle());
				 stmt.setString(index++, prof.usr_ste_usr_id);
				 stmt.setTimestamp(index++,curTime);
				 stmt.setString(index++, prof.usr_ste_usr_id);
				 stmt.setTimestamp(index++,curTime);
				 stmt.setString(index++, spcJson);
				 if (stmt.executeUpdate() == 1){
					 result = true;
				 }
			 } finally {
				 if (stmt != null) stmt.close();
			 }
		 }
		 return result;
	 }

	 /**
	  * 修改报告模板
	  * @param con
	  * @param prof
	  * @param param
	  * @throws SQLException
	  */
	 public boolean updStaffRptSpc(Connection con, loginProfile prof ,SuperviseModuleParam param) throws SQLException{
		 String spcJson= getSpcJson(param);
		 String sql="update ReportSpec set rsp_title=?, rsp_content= ?,rsp_upd_timestamp=?,rsp_upd_usr_id=? where rsp_id=?";
		 Timestamp cur_time= cwSQL.getTime(con);
		 PreparedStatement stmt = con.prepareStatement(sql);
		 int index =1;
		 stmt.setString(index++, param.getRspTitle());
		 stmt.setString(index++, spcJson);
		 stmt.setTimestamp(index++, cur_time);
		 stmt.setString(index++, prof.usr_ste_usr_id);
		 stmt.setInt(index++, param.getRsp_id());
		 int cut=stmt.executeUpdate();
		 if(stmt !=null) stmt.close();
		 if(cut>0)
			 return true;
		 else
			 return false;
	 }

	 /**
	  * 删除报告模板
	  * @param con
	  * @param param
	  * @throws SQLException
	  */
	 public boolean delStaffRptSpc(Connection con, SuperviseModuleParam param) throws SQLException{
		 String sql="delete ReportSpec where rsp_id=?";
		 PreparedStatement stmt = con.prepareStatement(sql);
		 stmt.setInt(1, param.getRsp_id());
		 int cut=stmt.executeUpdate();
		 if(stmt !=null) stmt.close();
		 if(cut>0)
			 return true;
		 else
			 return false;
	 }

	 /**
	  * 获取报告模板的列表
	  * @param con
	  * @param prof
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public HashMap getRptSpecLstByTypeAndUsr(Connection con, loginProfile prof , SuperviseModuleParam param) throws SQLException{
		 String sql ="SELECT rsp_id, rsp_rte_id, rsp_ent_id, rsp_title, rsp_upd_timestamp,rsp_content, rte_type, rte_exe_xsl, rte_dl_xsl"
			 		+" FROM ReportSpec INNER JOIN ReportTemplate ON rte_id = rsp_rte_id"
			 		+" WHERE rsp_ent_id = ? AND  rte_type = ?";
		 if(param.getSort() !=null){
			 sql=sql+"  ORDER BY "+param.getSort()+" "+param.getDir();
		 }else{
			 sql=sql+" ORDER BY rsp_upd_timestamp DESC";
		 }
		 PreparedStatement stmt = con.prepareStatement(sql);
		 int index =1;
		 stmt.setLong(index++, prof.usr_ent_id);
		 stmt.setString(index++, "STAFF");
		 ResultSet rs = stmt.executeQuery();
		 Vector v= new Vector();
		 while(rs.next()){
			 StaffReportBean rpt=new StaffReportBean();
			 JSONObject jobj=JSONObject.fromObject(cwSQL.getClobValue(rs, "rsp_content"));
			 if(jobj.containsKey("att_create_start_datetime")){
				 Timestamp temptime=Timestamp.valueOf((String)jobj.get("att_create_start_datetime"));
				 rpt.setAtt_create_start_datetime(temptime);
			 }
			 if(jobj.containsKey("att_create_end_datetime")){
				 Timestamp temptime=Timestamp.valueOf(jobj.getString("att_create_end_datetime"));
				 rpt.setAtt_create_end_datetime(temptime);
			 }
			 if(jobj.containsKey("ats_id")){
				 rpt.setAts_id(jobj.getInt("ats_id"));
			 }
			 if(jobj.containsKey("s_usg_ent_id_lst")){
				 rpt.setS_usg_ent_id_lst(jobj.getString("s_usg_ent_id_lst"));
			 }
			 if(jobj.containsKey("gmUsrOption")){
				 JSONObject obj=JSONObject.fromObject(jobj.get("gmUsrOption"));
				 GoldenManOptionBean option =(GoldenManOptionBean)JSONObject.toBean(obj,GoldenManOptionBean.class);
				 rpt.setGmUsrOption(option);
			 }
			 if(jobj.containsKey("gmItmOption")){

				 JSONObject obj=JSONObject.fromObject(jobj.get("gmItmOption"));
				 GoldenManOptionBean option =(GoldenManOptionBean)JSONObject.toBean(obj,GoldenManOptionBean.class);
				 rpt.setGmItmOption(option);
			 }
			 if(jobj.containsKey("gmTndOption")){
				 JSONObject obj=JSONObject.fromObject(jobj.get("gmTndOption"));
				 GoldenManOptionBean option =(GoldenManOptionBean)JSONObject.toBean(obj,GoldenManOptionBean.class);
				 rpt.setGmTndOption(option);
			 }
			 rpt.setRsp_id(rs.getInt("rsp_id"));
			 rpt.setRsp_title(rs.getString("rsp_title"));
			 rpt.setRte_type(rs.getString("rte_type"));
			 rpt.setRsp_upd_timestamp(rs.getTimestamp("rsp_upd_timestamp"));
			 rpt.setRte_exe_xsl(rs.getString("rte_exe_xsl"));
			 v.add(rpt);
		 }
		 HashMap spcMap= new HashMap();
		 spcMap.put("spec_lst", v);
		 if(stmt !=null) stmt.close();
		 return spcMap;
	 }

	 /**
	  * 获取报告模板的详细信息
	  * @param con
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public StaffReportBean getSpecInfo(Connection con, long rsp_id) throws SQLException{
		 String sql="select rsp_id,rsp_rte_id,rsp_ent_id,rsp_title,rsp_upd_timestamp,rsp_content,rte_type,rte_exe_xsl,rte_dl_xsl" +
		 		"  from ReportSpec,ReportTemplate where rsp_rte_id=rte_id and rsp_id=?";
		 PreparedStatement stmt = con.prepareStatement(sql);
		 stmt.setLong(1, rsp_id);
		 ResultSet rs = stmt.executeQuery();
		 StaffReportBean spec = new StaffReportBean();
		 if(rs.next()){
			 JSONObject jobj=JSONObject.fromObject(cwSQL.getClobValue(rs, "rsp_content"));
			 if(jobj.containsKey("att_create_start_datetime")){
				 Timestamp temptime=Timestamp.valueOf((String)jobj.get("att_create_start_datetime"));
				 spec.setAtt_create_start_datetime(temptime);
			 }
			 if(jobj.containsKey("att_create_end_datetime")){
				 Timestamp temptime=Timestamp.valueOf(jobj.getString("att_create_end_datetime"));
				 spec.setAtt_create_end_datetime(temptime);
			 }
			 if(jobj.containsKey("ats_id")){
				 spec.setAts_id(jobj.getInt("ats_id"));
			 }
			 if(jobj.containsKey("s_usg_ent_id_lst")){
				 spec.setS_usg_ent_id_lst(jobj.getString("s_usg_ent_id_lst"));
			 }
			 if(jobj.containsKey("gmUsrOption")){
				 JSONObject obj=JSONObject.fromObject(jobj.get("gmUsrOption"));
				 GoldenManOptionBean option =(GoldenManOptionBean)JSONObject.toBean(obj,GoldenManOptionBean.class);
				 spec.setGmUsrOption(option);
			 }
			 if(jobj.containsKey("gmItmOption")){

				 JSONObject obj=JSONObject.fromObject(jobj.get("gmItmOption"));
				 GoldenManOptionBean option =(GoldenManOptionBean)JSONObject.toBean(obj,GoldenManOptionBean.class);
				 spec.setGmItmOption(option);
			 }
			 if(jobj.containsKey("gmTndOption")){
				 JSONObject obj=JSONObject.fromObject(jobj.get("gmTndOption"));
				 GoldenManOptionBean option =(GoldenManOptionBean)JSONObject.toBean(obj,GoldenManOptionBean.class);
				 spec.setGmTndOption(option);
			 }
			 spec.setRsp_id(rs.getInt("rsp_id"));
			 spec.setRsp_title(rs.getString("rsp_title"));
			 spec.setRte_type(rs.getString("rte_type"));
			 spec.setRsp_upd_timestamp(rs.getTimestamp("rsp_upd_timestamp"));
			 spec.setRte_exe_xsl(rs.getString("rte_exe_xsl"));
			 spec.setRsp_title_noescape(spec.getRsp_title());
		 }
		 if(stmt !=null) stmt.close();
		 return spec;
	 }

	 /**
	  * 查询未处理的报名请求
	  * @param con
	  * @param prof
	  * @param param
	  * @param hs
	  * @return
	  * @throws SQLException
	  */
	 public String getApp_pend(Connection con, loginProfile prof ,SuperviseModuleParam param,Vector appVc) throws SQLException{
		 String sql = "select app_id, app_itm_id, "
			 +" app_create_timestamp, "	//申请时间
			 +" app_upd_timestamp, "
			 +" usr_ent_id, "
			 +" usr_ste_usr_id,usr_display_bil,  "//用户名和全名
			 + cwSQL.replaceNull("pitm.itm_title", "citm.itm_title")+" itm_title, "	//课程名称
			 +" citm.itm_title c_itm_title,pitm.itm_title p_itm_title,  "
			 +" citm.itm_content_eff_start_datetime AS itm_content_start_date, " 			//课程开始时间
			 +" citm.itm_content_eff_end_datetime AS itm_content_end_date, " //课程结束时间
			 +"	citm.itm_eff_start_datetime AS itm_start_date, "
			 +" citm.itm_eff_end_datetime AS itm_end_date, "
			 +" citm.itm_blend_ind "
			 +" FROM aeAppnApprovalList"
			 +" INNER JOIN regUser ON (aal_app_ent_id = usr_ent_id and aal_usr_ent_id=?)"//当前用户ID
			 +" INNER JOIN aeApplication ON (app_id = aal_app_id)"
			 +" INNER JOIN aeItem citm ON (citm.itm_id = app_itm_id AND citm.itm_status='ON') "
			 +" LEFT JOIN aeItemRelation ON (ire_child_itm_id = app_itm_id) "
			 +" LEFT JOIN aeItem pitm ON (ire_parent_itm_id = pitm.itm_id )"
			 +" WHERE app_status = ?"
			 +" AND aal_status = ?"
			 +" AND citm.itm_status = 'ON' "
			 +" AND (pitm.itm_status = 'ON' OR pitm.itm_status IS NULL) ";
		 if(param.getSort() == null) {
			 param.setSort("app_create_timestamp");
			 param.setDir("DESC");
		 }
		 if(param.getSort() !=null){
			 sql=sql+" order by "+param.getSort()+" "+param.getDir();
		 }
		 PreparedStatement stmt = con.prepareStatement(sql);
		 stmt.setLong(1, prof.usr_ent_id);
		 stmt.setString(2, aeApplication.PENDING);
		 stmt.setString(3, DbAppnApprovalList.STATUS_PENDING);
		 ResultSet rs = stmt.executeQuery();
		 int count=0;
		 StringBuffer appBuf = new StringBuffer();
		 while(rs.next()){
			 if(count>=param.getStart() && count<(param.getLimit()+param.getStart())){
				 AppBean app= new AppBean();
				 app.setApp_id(rs.getInt("app_id"));
				 appBuf.append(",").append(app.getApp_id());
				 String c_itm_title = rs.getString("c_itm_title");
				 String p_itm_title = rs.getString("p_itm_title");
				 if(p_itm_title==null ||p_itm_title.length()==0){
					 app.setItm_title(c_itm_title);
				 }else{
					 app.setItm_title(p_itm_title+" ("+c_itm_title+")");
				 }
				 app.setUsr_ent_id(rs.getLong("usr_ent_id"));
				 app.setUsr_display_bil(rs.getString("usr_display_bil"));
				 app.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
				 boolean blend_ind = rs.getBoolean("itm_blend_ind");
				 Timestamp itm_content_start_date = rs.getTimestamp("itm_content_start_date");
				 Timestamp itm_start_date = rs.getTimestamp("itm_start_date");
				 if(blend_ind){
					 Timestamp start_time= itm_content_start_date.before(itm_start_date)?itm_content_start_date:itm_start_date;
					 app.setItm_start_date(start_time);
				 }else{
					 app.setItm_start_date(rs.getTimestamp("itm_start_date"));
				 }
				 //app.setItm_end_date(rs.getTimestamp("itm_end_date"));
				 app.setApp_create_timestamp(rs.getTimestamp("app_create_timestamp"));
				 app.setApp_upd_timestamp(rs.getTimestamp("app_upd_timestamp"));
				 appVc.add(app);
			 }
			 count ++;
		 }
		param.setTotal_rec(count);
		Pagination page = new Pagination(param.getTotal_rec(),param.getStart(),param.getLimit(),param.getSort(),param.getDir());
		param.setPage(page);
		if(stmt !=null) stmt.close();
		if(appBuf !=null && appBuf.length()>0)
			appBuf=appBuf.deleteCharAt(0);
		return appBuf.toString();
	 }

	 /**
	  * 查询未处理的报名请求中的审批历程
	  * @param con
	  * @param prof
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public void getApp_pend_ApprLst(Connection con, loginProfile prof ,SuperviseModuleParam param, Hashtable appHash,String app_id_str) throws SQLException{

		 String sql="SELECT pend.aal_app_id,usr_display_bil,hist.aal_action_timestamp, hist.aal_action_taken" +
		 		" FROM aeAppnApprovalList pend" +
		 		" INNER JOIN aeAppnApprovalList hist ON (pend.aal_app_id = hist.aal_app_id AND hist.aal_usr_ent_id = hist.aal_action_taker_usr_ent_id AND pend.aal_status = 'PENDING' AND hist.aal_status = 'HISTORY')" +
		 		" INNER JOIN regUser ON usr_ent_id = hist.aal_usr_ent_id" +
		 		" WHERE pend.aal_usr_ent_id = ? AND pend.aal_app_id IN ( "+app_id_str+ ")" +
		 		" ORDER BY pend.aal_app_id ASC,hist.aal_action_timestamp ASC";
		 PreparedStatement stmt = con.prepareStatement(sql);
		 stmt.setLong(1, prof.usr_ent_id);
		 ResultSet rs = stmt.executeQuery();
		 while(rs.next()){
			 ApprBean appr =new ApprBean();
			 int appn_id = rs.getInt("aal_app_id");
			 appr.setUsr_display_bil(rs.getString("usr_display_bil"));
			 appr.setAal_action_timestamp(rs.getTimestamp("aal_action_timestamp"));
			 appr.setAction_taken(rs.getString("aal_action_taken"));
			 int recCntWithSameAppId = 1;
			 Hashtable recHash = new Hashtable();
			 if(appHash.get(new Integer(appn_id)) != null) {
				 recHash = (Hashtable)appHash.get(new Integer(appn_id));
				 recCntWithSameAppId = recHash.size();
				 recHash.put(new Integer(recCntWithSameAppId + 1), appr);
				 appHash.put(new Integer(appn_id), recHash);
			 } else {
				 recHash.put(new Integer(recCntWithSameAppId), appr);
				 appHash.put(new Integer(appn_id), recHash);
			 }
		 }
		 cwSQL.cleanUp(rs, stmt);
	 }

	 /**
	  * 匹配未处理报名请求中的各项信息
	  * @param con
	  * @param prof
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public Vector getAllApp_pendInfo(Connection con, loginProfile prof ,SuperviseModuleParam param) throws SQLException{
		 Hashtable apprHs=new Hashtable();
		 Vector recVc= new Vector();
		 Vector appVc= new Vector();
		 String app_id_str=getApp_pend(con, prof, param,appVc);
		 if(appVc !=null && appVc.size()>0){
			 getApp_pend_ApprLst(con, prof, param, apprHs, app_id_str);
			 Iterator iter=appVc.iterator();
			 while(iter.hasNext()){
				 AppBean app=(AppBean)iter.next();
				 Integer obj_id =new Integer (app.getApp_id());
				 Vector apprVc= new Vector();
				 if(apprHs !=null && apprHs.size()>0 && apprHs.containsKey(obj_id)){
					 Hashtable recHash = (Hashtable) apprHs.get(obj_id);
					 Enumeration recEnum = recHash.keys();
					 while (recEnum.hasMoreElements()){
						 Integer cutId= (Integer)recEnum.nextElement();
						 apprVc.add((ApprBean)recHash.get(cutId));
					 }
				 }
				 app.setAppr_lst(apprVc);
				 recVc.add(app);
			 }
		 }
		 return recVc;
	 }

	 /**
	  * 查询已处理的报名请求
	  * @param con
	  * @param prof
	  * @param param
	  * @param appHs
	  * @return
	  * @throws SQLException
	  */
	 public String getApp_Approval(Connection con, loginProfile prof ,SuperviseModuleParam param,Vector appVc) throws SQLException{
		 String sql = "select app_id, app_itm_id, "
			 +" usr_ent_id,  "
			 +" usr_ste_usr_id,usr_display_bil,  "//用户名和全名
			 +" app_create_timestamp, "//申请时间
			 + cwSQL.replaceNull("pitm.itm_title", "citm.itm_title") +" itm_title, "//课程名称
			 +" citm.itm_title c_itm_title,pitm.itm_title p_itm_title,  "
			 +" citm.itm_content_eff_start_datetime itm_content_start_date, " 			//课程开始时间
			 +" citm.itm_content_eff_end_datetime itm_content_end_date, " //课程结束时间
			 +"	citm.itm_eff_start_datetime itm_start_date, "
			 +" citm.itm_eff_end_datetime itm_end_date, "
			 +" citm.itm_blend_ind "
			 +" FROM aeAppnApprovalList"
			 +" INNER JOIN regUser ON (aal_app_ent_id = usr_ent_id and aal_usr_ent_id=?)"//当前用户ID
			 +" INNER JOIN aeApplication ON (app_id = aal_app_id)"
			 +" INNER JOIN aeItem citm ON (citm.itm_id = app_itm_id AND citm.itm_status='ON') "
			 +" LEFT JOIN aeItemRelation ON (ire_child_itm_id = app_itm_id )"
			 +" LEFT JOIN aeItem pitm ON (ire_parent_itm_id = pitm.itm_id )"
			 +" LEFT JOIN aeAttendance ON (att_app_id = app_id )"
			 +" WHERE "
			 +" aal_status =?"
			 +" AND aal_usr_ent_id = aal_action_taker_usr_ent_id "
			 +" AND citm.itm_status = 'ON' "
			 +" AND (pitm.itm_status = 'ON' OR pitm.itm_status IS NULL) ";
		 if(param.getSort() == null) {
			 param.setSort("app_create_timestamp");
			 param.setDir("DESC");
		 }
		 if(param.getSort() !=null){
			 sql=sql+" order by "+param.getSort()+" "+param.getDir();
		 }
		 PreparedStatement stmt = con.prepareStatement(sql);
		 stmt.setLong(1, prof.usr_ent_id);
		 stmt.setString(2, DbAppnApprovalList.STATUS_HISTORY);
		 ResultSet rs = stmt.executeQuery();
		 int count=0;
		 StringBuffer appBuf = new StringBuffer();
		 while(rs.next()){
			 if (count >= param.getStart() && count < (param.getLimit() + param.getStart())) {
				AppBean app = new AppBean();
				app.setUsr_ent_id(rs.getLong("usr_ent_id"));
				app.setApp_id(rs.getInt("app_id"));
				appBuf.append(",").append(app.getApp_id());
				String c_itm_title = rs.getString("c_itm_title");
				String p_itm_title = rs.getString("p_itm_title");
				if (p_itm_title == null || p_itm_title.length() == 0) {
					app.setItm_title(c_itm_title);
				} else {
					app.setItm_title(p_itm_title + " (" + c_itm_title + ")");
				}
				boolean blend_ind = rs.getBoolean("itm_blend_ind");
				Timestamp itm_content_start_date = rs.getTimestamp("itm_content_start_date");
				app.setItm_start_date(rs.getTimestamp("itm_start_date"));
				//app.setItm_end_date(rs.getTimestamp("itm_end_date"));
				Timestamp itm_start_date = rs.getTimestamp("itm_start_date");
				if (blend_ind) {
					Timestamp start_time = itm_content_start_date.before(itm_start_date) ? itm_content_start_date : itm_start_date;
					app.setItm_start_date(start_time);
				} else {
					app.setItm_start_date(itm_start_date);
				}
				app.setUsr_display_bil(rs.getString("usr_display_bil"));
				app.setUsr_ste_usr_id(rs.getString("usr_ste_usr_id"));
				app.setApp_create_timestamp(rs.getTimestamp("app_create_timestamp"));
				appVc.add(app);
			}
			 count ++;
		 }
		param.setTotal_rec(count);
		Pagination page = new Pagination(param.getTotal_rec(),param.getStart(),param.getLimit(),param.getSort(),param.getDir());
		param.setPage(page);
		if(stmt !=null) stmt.close();
		if(appBuf !=null && appBuf.length()>0)
			appBuf=appBuf.deleteCharAt(0);
		return appBuf.toString();
	 }

	 /**
	  * 查询已处理的报名请求中的审批历程
	  * @param con
	  * @param prof
	  * @param param
	  * @param app_id_str
	  * @param apprHs
	  * @throws SQLException
	  */
	 public void getApp_Approval_ApprLst(Connection con, loginProfile prof ,SuperviseModuleParam param,String app_id_str, Hashtable apprHs) throws SQLException{
		 String sql=" SELECT hist_my.aal_app_id,usr_display_bil,hist.aal_action_timestamp, hist.aal_action_taken"
					+" FROM aeAppnApprovalList hist_my"
					+" INNER JOIN aeAppnApprovalList hist ON ( hist_my.aal_app_id = hist.aal_app_id AND hist.aal_usr_ent_id = hist.aal_action_taker_usr_ent_id"
					+" AND hist_my.aal_usr_ent_id = hist_my.aal_action_taker_usr_ent_id AND hist_my.aal_status = ? AND hist.aal_status = ?)"
					+" INNER JOIN regUser ON usr_ent_id = hist.aal_usr_ent_id"
					+" WHERE hist_my.aal_usr_ent_id = ? AND hist_my.aal_app_id IN ("+app_id_str+")";

		 PreparedStatement stmt = con.prepareStatement(sql);
		 stmt.setString(1, DbAppnApprovalList.STATUS_HISTORY);
		 stmt.setString(2, DbAppnApprovalList.STATUS_HISTORY);
		 stmt.setLong(3, prof.usr_ent_id);
		 ResultSet rs = stmt.executeQuery();
		 while(rs.next()){
			 ApprBean appr =new ApprBean();
			 int appn_id = rs.getInt("aal_app_id");
			 appr.setUsr_display_bil(rs.getString("usr_display_bil"));
			 appr.setAal_action_timestamp(rs.getTimestamp("aal_action_timestamp"));
			 appr.setAction_taken(rs.getString("aal_action_taken"));
			 int recCntWithSameAppId = 1;
			 Hashtable recHash = new Hashtable();
			 if(apprHs.get(new Integer(appn_id)) != null) {
				 recHash = (Hashtable)apprHs.get(new Integer(appn_id));
				 recCntWithSameAppId = recHash.size();
				 recHash.put(new Integer(recCntWithSameAppId + 1), appr);
				 apprHs.put(new Integer(appn_id), recHash);
			 } else {
				 recHash.put(new Integer(recCntWithSameAppId), appr);
				 apprHs.put(new Integer(appn_id), recHash);
			 }
		 }
		 cwSQL.cleanUp(rs, stmt);
	 }

	 /**
	  * 查询已处理报名请求中的下一个审批者
	  * @param con
	  * @param prof
	  * @param param
	  * @param app_id_str
	  * @param nAppHash
	  * @throws SQLException
	  */
	 public void getNext_Appr(Connection con, loginProfile prof ,SuperviseModuleParam param,String app_id_str, Hashtable nAppHash) throws SQLException{
		 String sql="SELECT pend.aal_app_id, usr_display_bil"
		 +" FROM aeAppnApprovalList pend"
		 +" INNER JOIN aeAppnApprovalList hist ON (pend.aal_app_id = hist.aal_app_id"
		 +" AND hist.aal_usr_ent_id = hist.aal_action_taker_usr_ent_id AND pend.aal_status = ? AND hist.aal_status = ?)"
		 +" INNER JOIN regUser ON usr_ent_id = pend.aal_usr_ent_id WHERE hist.aal_usr_ent_id = ?  AND hist.aal_app_id IN ("+app_id_str+")";
		 PreparedStatement stmt = con.prepareStatement(sql);
		 stmt.setString(1, DbAppnApprovalList.STATUS_PENDING);
		 stmt.setString(2, DbAppnApprovalList.STATUS_HISTORY);
		 stmt.setLong(3, prof.usr_ent_id);
		 ResultSet rs = stmt.executeQuery();
		 while(rs.next()){
			 int app_id=rs.getInt("aal_app_id");
			 Vector usrVc= new Vector();
			 usrVc.add(rs.getString("usr_display_bil"));
			 int recCntWithSameAppId = 1;
			 Hashtable usrHs = new Hashtable();
			 if(nAppHash.get(new Integer(app_id)) !=null){
				 usrHs=(Hashtable)nAppHash.get(new Integer(app_id));
				 recCntWithSameAppId=usrHs.size();
				 usrHs.put(new Integer(recCntWithSameAppId+1), usrVc);
				 nAppHash.put(new Integer(app_id), usrHs);
			 }else{
				 usrHs.put(new Integer(recCntWithSameAppId), usrVc);
				 nAppHash.put(new Integer(app_id), usrHs);
			 }
		 }
		 cwSQL.cleanUp(rs, stmt);
	 }

	 /**
	  * 匹配未处理报名请求中的各项信息
	  * @param con
	  * @param prof
	  * @param param
	  * @return
	  * @throws SQLException
	  */
	 public Vector getAllApp_ApprovalInfo(Connection con, loginProfile prof ,SuperviseModuleParam param) throws SQLException{
		 Hashtable apprHs= new Hashtable();
		 Hashtable nAppHash= new Hashtable();
		 Vector appVc =new Vector();
		 String app_id_str=getApp_Approval(con, prof, param, appVc);
		 Vector recVc= new Vector();
		 if(appVc !=null && appVc.size()>0){
			 getApp_Approval_ApprLst(con, prof, param, app_id_str, apprHs);
			 getNext_Appr(con, prof, param, app_id_str, nAppHash);
			 Iterator iter=appVc.iterator();
			 while(iter.hasNext()) {
				 AppBean app=(AppBean)iter.next();
				 Integer obj_id = new Integer(app.getApp_id());
				 Vector apprVc= new Vector();
				 if(apprHs !=null && apprHs.size()>0 && apprHs.containsKey(obj_id)){
					 Hashtable recHash = (Hashtable) apprHs.get(obj_id);
					 Enumeration recEnum = recHash.keys();
					 while (recEnum.hasMoreElements()){
						 Integer cutId= (Integer)recEnum.nextElement();
						 apprVc.add((ApprBean)recHash.get(cutId));
					 }
				 }
				 Vector nAppVc =new Vector();
				 if(nAppHash !=null && nAppHash.size()>0 && nAppHash.containsKey(obj_id)){
					 Hashtable recHash = (Hashtable) nAppHash.get(obj_id);
					 Enumeration recEnum = recHash.keys();
					 while (recEnum.hasMoreElements()){
						 Integer cutId= (Integer)recEnum.nextElement();
						 nAppVc.addAll((Vector)recHash.get(cutId));
					 }
				 }
				 app.setAppr_lst(apprVc);
				 app.setNextApp(nAppVc);
				 recVc.add(app);
			 }
		 }
		return recVc;
	 }

	 /**
	  * GoldenMan的选项值
	  * @param report
	  * @return
	  */
	 public Vector getGoldenManOption(StaffReportBean report){
		 Vector gmVc =new Vector();
		 gmVc.add(report.getGmUsrOption());
		 gmVc.add(report.getGmItmOption());
		 gmVc.add(report.getGmTndOption());
		 return gmVc;
	 }



	 public static boolean hasStaff(Connection con, long usr_ent_id) {
		boolean result = false;
		try {
			Supervise supervise = new Supervise();
			SuperviseModuleParam param = new SuperviseModuleParam();
			Vector staff = supervise.getDirectStaff(con, usr_ent_id, param);
			if(staff!=null && staff.size()>0){
				result = true;
			}else{
				staff = supervise.getAllGroupStaff(con, usr_ent_id, param);
				if(staff!=null && staff.size()>0){
					result = true;
				}
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
		return result;
	}
}
