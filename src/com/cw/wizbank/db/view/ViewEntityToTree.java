package com.cw.wizbank.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.JsonMod.commonBean.JsonTreeBean;
import com.cw.wizbank.JsonMod.supervise.Supervise;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.competency.CmSkillSetManager;
import com.cw.wizbank.db.DbCmSkillBase;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class ViewEntityToTree {
    public ViewEntityToTree() {
        ;
    }

    public class entityInfo {
        public long ent_id;
        public String title;
        public String type;
        public String fullPath;
        public String has_child;
        public String node_type;

        public entityInfo() {
            ;
        }
    }

    public Vector entityListAsVector(Connection con, loginProfile prof, long ent_id, String groupType, boolean notIncludeUser, String ftn_ext_id, long usr_ent_id, String rol_ext_id, long root_ent_id, boolean tcEnableInd, boolean filter_user_group, boolean show_bil_nickname,String  search_rol_ext_id)
        throws SQLException{
        Vector ent_lst = new Vector();
        StringBuffer sql = new StringBuffer();
		Vector vec = new Vector();
        boolean isRoleTcInd = AccessControlWZB.isRoleTcInd( rol_ext_id) ;
        boolean isGroupType = false;

        if (groupType.equalsIgnoreCase("GRADE")) {
            sql.append("select ent_id AS id, ugr_display_bil AS title, ent_type AS type, ugr_seq_no AS seq_no from EntityRelation, UserGrade, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and ugr_ent_id = ern_child_ent_id and ent_id = ugr_ent_id and ent_delete_usr_id IS NULL and ent_delete_timestamp IS NULL ");
//            loginProfile prof = new loginProfile();
//            prof.usr_ent_id = usr_ent_id;
//            prof.current_role = rol_ext_id;
//            prof.root_ent_id = root_ent_id;
            
            
            String tcrListStr = "";
//            if (!qdbAction.wizbini.cfgSysSetupadv.isTcIndependent() || prof.current_role.equals("ADM_1")
//                    || ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role)) {
//
//            } else {
//                Vector canMgtTopTcrList = ViewTrainingCenter.getTopLevelTCList(con, prof);
//                if (canMgtTopTcrList != null && canMgtTopTcrList.size() > 0) {
//                    tcrListStr = cwUtils.vector2list(canMgtTopTcrList);
//                } else {
//                    tcrListStr = "(0)";
//                }
//
//                sql.append(" AND ugr_tcr_id in ").append(tcrListStr);
//            }
            
            sql.append(" AND (ugr_tcr_id in (").append("select tcn_child_tcr_id from tcRelation inner join tcTrainingCenter on (tcr_id = tcn_child_tcr_id) where tcn_ancestor = "+prof.my_top_tc_id+" and tcr_status = 'OK' ").append(") or ugr_tcr_id ="+prof.my_top_tc_id+" or ugr_default_ind = 1 )");
            
            
            
            if (! notIncludeUser) {
                sql.append(" union select ent_id AS id, usr_display_bil AS title, ent_type AS type, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER) + " AS seq_no from RegUser, EntityRelation, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and usr_ent_id = ern_child_ent_id and ent_id = usr_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
            }

            sql.append(" order by type, seq_no, title");
        } else if (groupType.equalsIgnoreCase("INDUSTRY")) {
            sql.append("select ent_id AS id, idc_display_bil AS title, ent_type AS type from EntityRelation, IndustryCode, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and idc_ent_id = ern_child_ent_id and ent_id = idc_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");

            if (! notIncludeUser) {
                sql.append(" union select ent_id AS id, usr_display_bil AS title, ent_type AS type from RegUser, EntityRelation, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and usr_ent_id = ern_child_ent_id and ent_id = usr_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
            }

            sql.append(" order by type, title");
        } else if (groupType.equalsIgnoreCase("USER_GROUP") || groupType.equalsIgnoreCase(cwTree.MY_STAFF)){
        	if(filter_user_group && tcEnableInd && isRoleTcInd && ent_id == root_ent_id){
        		try {
        			vec = dbUserGroup.getTopLevelGroupId(con, usr_ent_id);
        		} catch (qdbException e) {
            		CommonLog.error(e.getMessage(),e);
            	}
//            	if(vec != null && vec.size() == 1 && ((Long)vec.elementAt(0)).longValue() == root_ent_id) {
        		if(vec != null && vec.contains(root_ent_id)) {
            		sql.append("select ent_id AS id, usg_display_bil AS title, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as nickname, ent_type AS type from EntityRelation, UserGroup, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and usg_ent_id = ern_child_ent_id and ent_id = usg_ent_id and (usg_role is null or usg_role <> 'SYSTEM') AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
            	} else {
            		isGroupType = true;
            		if(vec !=null && vec.size()==0) {
            			vec.add(new Long(0));
            		}
            		String usg_id_lst = cwUtils.vector2list(vec);		
            		sql.append(" select ent_id AS id, usg_display_bil AS title, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as nickname, ent_type AS type ")
            		.append(" from UserGroup,EntityRelation,Entity")
            		.append(" where usg_ent_id in")
            		.append(usg_id_lst)
            		.append(" and usg_ent_id = ent_id")
            		.append(" and ent_delete_usr_id IS NULL")
            		.append(" and ent_delete_timestamp IS NULL")
            		.append(" and usg_ent_id = ern_child_ent_id AND ern_type = ? and ern_parent_ind = ? ");            		
            	}
        	} else {
        		sql.append("select ent_id AS id, usg_display_bil AS title, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as nickname, ent_type AS type from EntityRelation, UserGroup, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and usg_ent_id = ern_child_ent_id and ent_id = usg_ent_id and (usg_role is null or usg_role <> 'SYSTEM') AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
        	}

            if (! notIncludeUser) {
				if(ftn_ext_id!=null && ftn_ext_id.length()>0) {
					sql.append(" union ")
					   .append(" select ent_id AS id, usr_display_bil AS title, usr_nickname as nickname, ent_type AS type ")
					   .append(" from RegUser, EntityRelation, Entity, acEntityRole, acRole, acRoleFunction, acFunction ")
					   .append(" where ern_ancestor_ent_id = ? ")
					   .append(" and usr_ent_id = ern_child_ent_id ")
					   .append(" and ern_parent_ind = ? ")
					   .append(" and ent_id = usr_ent_id ")
					   .append(" and usr_status = 'OK' ")
					   .append(" AND ent_delete_usr_id IS NULL ")
					   .append(" AND ent_delete_timestamp IS NULL ")
					   .append(" AND usr_ent_id = erl_ent_id ")
					   .append(" AND erl_rol_id = rol_id ")
                       .append(" AND rol_id = rfn_rol_id ")
					   .append(" AND rfn_ftn_id = ftn_id ")
					   .append(" AND ftn_ext_id = '").append(ftn_ext_id).append("' ");
				} else if (search_rol_ext_id != null && search_rol_ext_id.length() > 0) {
					sql.append(" union ")
					   .append(" select ent_id AS id, usr_display_bil AS title, usr_nickname as nickname, ent_type AS type ")
                       .append(" from RegUser, EntityRelation, Entity, acEntityRole, acRole ")
					   .append(" where ern_ancestor_ent_id = ? ")
					   .append(" and usr_ent_id = ern_child_ent_id ")
					   .append(" and ern_parent_ind = ? ")
					   .append(" and ent_id = usr_ent_id ")
					   .append(" and usr_status = 'OK' ")
					   .append(" AND ent_delete_usr_id IS NULL ")
					   .append(" AND ent_delete_timestamp IS NULL ")
					   .append(" AND usr_ent_id = erl_ent_id ")
					   .append(" AND erl_rol_id = rol_id ")
					   .append(" AND rol_ext_id = '").append(search_rol_ext_id).append("' ");
				} else {
					sql.append(" union select ent_id AS id, usr_display_bil AS title, usr_nickname as nickname, ent_type AS type from RegUser, EntityRelation, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and usr_ent_id = ern_child_ent_id and ent_id = usr_ent_id and usr_status = 'OK' AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
				}
            }

            sql.append(" order by type, title");
        } else {
            // USER CLASSIFICATION
            sql.append("select ent_id AS id, ucf_display_bil AS title, ent_type AS type, ucf_seq_no AS seq_no from EntityRelation, UserClassification, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and ucf_ent_id = ern_child_ent_id and ent_id = ucf_ent_id and ent_type = '" + groupType + "' AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");

            if (! notIncludeUser) {
                sql.append(" union select ent_id AS id, usr_display_bil AS title, ent_type AS type, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER) + " AS seq_no from RegUser, EntityRelation, Entity where ern_ancestor_ent_id = ? and ern_parent_ind = ? and usr_ent_id = ern_child_ent_id and ent_id = usr_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
            }

            sql.append(" order by type, seq_no, title");
        }

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        
        if(!groupType.equalsIgnoreCase("USER_GROUP") 
        	|| !filter_user_group
        	|| !tcEnableInd 
        	|| !isRoleTcInd 
        	|| ent_id != root_ent_id
//        	|| (vec != null && vec.size() == 1 && ((Long)vec.elementAt(0)).longValue() == root_ent_id)) {
        	|| (vec != null && vec.contains(root_ent_id))) {
        	stmt.setLong(index++, ent_id);
        	stmt.setBoolean(index++, true);
        }
        if (isGroupType){
        	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
        	stmt.setBoolean(index++, true);
        }
        if (! notIncludeUser) {
        	stmt.setLong(index++, ent_id);
        	stmt.setBoolean(index++, true);
        }

        ResultSet rs = stmt.executeQuery();
        entityInfo info;
        String nickname = null;
        while (rs.next()) {
            info = new entityInfo();
            info.ent_id = rs.getLong("id");
            info.title = rs.getString("title");
            if (show_bil_nickname) {
            	nickname = rs.getString("nickname");
	            if (nickname != null && nickname.length() > 0) {
	            	info.title += "(" + nickname + ")";
	            }
            }
            info.type = rs.getString("type");
            if(groupType.equalsIgnoreCase("USER_GROUP") && notIncludeUser) {
            	EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
                info.fullPath = entityfullpath.getFullPath(con, info.ent_id);
            }
            ent_lst.addElement(info);
//System.out.println("### " + info.ent_id);
        }

        stmt.close();
        return ent_lst;
    }
    
    public Vector getDirSuperviseGroup(Connection con, long spt_source_usr_ent_id, String cur_lan) throws SQLException  {
    	Vector ent_lst = new Vector();
    	Supervise spv = new Supervise();
    	Vector v = spv.getDirSuperviseGroup(con, spt_source_usr_ent_id, cur_lan);
		entityInfo info;
		JsonTreeBean jtree;
		for (int i = 0; i < v.size(); i++) {
			jtree = (JsonTreeBean)v.get(i);
			info = new entityInfo();
			info.ent_id = jtree.getId();
            info.title = jtree.getText();
            info.type = dbEntity.ENT_TYPE_USER_GROUP;
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            info.fullPath = entityfullpath.getFullPath(con, info.ent_id);
            ent_lst.add(info);
		}
	    return ent_lst; 
    }
    
    public boolean hasDirSuperviseGroup(Connection con, long spt_source_usr_ent_id) throws SQLException  {
    	String sql = " select spt_target_ent_id cnt from SuperviseTargetEntity "
 				   + " where spt_source_usr_ent_id = ? ";
 			 
		boolean result = false;
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, spt_source_usr_ent_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
		}
	    return result; 
    }

    public static Vector entityNoChildAndTypeInfo(Connection con, long[] ent_id_lst, String groupType, boolean notIncludeUser, String ftn_ext_id)
    throws SQLException {

		Vector v = new Vector();
	    String tableName = null;
	    String innerTmpTableSql = null;
	    
	    StringBuffer sql = new StringBuffer();
	    StringBuffer lst = new StringBuffer();
	    
	    if (ent_id_lst == null || ent_id_lst.length == 0) {
	    	return v;
	    }else if(ent_id_lst.length > 1000){//oracle中查询语句IN关键字范围不能大于1000，可用临时表进行表连接
	    	tableName = cwSQL.createSimpleTemptable(con, "tmp_ent_id", cwSQL.COL_TYPE_LONG, 0);
	    	cwSQL.insertSimpleTempTable(con, tableName, cwUtils.long2vector(ent_id_lst), cwSQL.COL_TYPE_LONG);
	    	innerTmpTableSql = " inner join " + tableName + " on (parent.ern_child_ent_id = tmp_ent_id) ";
	    }else{
	    	lst.append("(0 ");
	        for (int i=0; i<ent_id_lst.length; i++) {
	            lst.append(", ").append(ent_id_lst[i]);
	        }
	        lst.append(")");
	    }
	
	    if (notIncludeUser) {
	        sql.append("select distinct parent.ern_child_ent_id ")
	        	.append(" from EntityRelation parent ");
	        if(tableName != null && innerTmpTableSql != null){
	        	sql.append(innerTmpTableSql)
	        	   .append(" where parent.ern_parent_ind = ? ");
	        }else{
	        	sql.append(" where parent.ern_parent_ind = ? and parent.ern_child_ent_id in ")
				   .append(lst.toString());
	        }
			sql.append(" and not exists (")
				.append(" select parent.ern_child_ent_id ")
				.append(" from EntityRelation child, Entity ")
				.append(" where parent.ern_parent_ind = ? and child.ern_ancestor_ent_id = parent.ern_child_ent_id and child.ern_child_ent_id = ent_id") 
				.append(" and ent_type <> 'USR' AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL")
				.append(")");
	    } else {
	    	if(ftn_ext_id != null && ftn_ext_id.length() > 0) {
				sql.append(" select distinct parent.ern_child_ent_id ")
				   .append(" from EntityRelation parent "); 
				if(tableName != null && innerTmpTableSql != null){
	            	sql.append(innerTmpTableSql)
	            	   .append(" where parent.ern_parent_ind = ? ");
	            }else{
	            	sql.append(" where parent.ern_parent_ind = ? and parent.ern_child_ent_id in ")
					   .append(lst.toString());
	            }
				sql.append(" and not exists ( ")
				   .append(" select parent.ern_child_ent_id ") 
				   .append(" from EntityRelation child, Entity ")
				   .append(" where child.ern_parent_ind = ? ") 
				   .append(" AND child.ern_child_ent_id = ent_id ")
				   .append(" AND child.ern_ancestor_ent_id = parent.ern_child_ent_id ")
				   .append(" AND ent_type <> '").append(dbEntity.ENT_TYPE_USER).append("' ")
				   .append(" union ")
				   .append(" select parent.ern_child_ent_id ")
				   .append(" from EntityRelation child, RegUser, acEntityRole, acRole, acRoleFunction, acFunction ")
				   .append(" where child.ern_parent_ind = ? ") 
				   .append(" AND child.ern_child_ent_id = usr_ent_id ")
				   .append(" AND child.ern_ancestor_ent_id = parent.ern_child_ent_id ")
				   .append(" AND usr_ent_id = erl_ent_id ")
				   .append(" AND erl_rol_id = rol_id ")
				   .append(" AND rol_id = rfn_rol_id ")
				   .append(" AND rfn_ftn_id = ftn_id ")
				   .append(" AND ftn_ext_id = '").append(ftn_ext_id).append("' ")
				   .append(" ) ");
	
	    	} else {
	    		sql.append("select distinct parent.ern_child_ent_id ")
	        	   .append(" from EntityRelation parent ");
	        	if(tableName != null && innerTmpTableSql != null){
		            	sql.append(innerTmpTableSql)
		            	   .append(" where parent.ern_parent_ind = ? ");
		            }else{
		            	sql.append(" where parent.ern_parent_ind = ? and parent.ern_child_ent_id in ")
						   .append(lst.toString());
		            }
				sql.append(" and not exists (")
				   .append(" select parent.ern_child_ent_id ")
				   .append(" from EntityRelation child ")
				   .append(" where ern_parent_ind = ? AND child.ern_ancestor_ent_id = parent.ern_child_ent_id ")
				   .append(")");
	    	}
	    }
	    
	    int index = 1;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try{
	        stmt = con.prepareStatement(sql.toString());
	        stmt.setBoolean(index++, true);
	        stmt.setBoolean(index++, true);
	        if(!notIncludeUser){
		        if(ftn_ext_id != null && ftn_ext_id.length() > 0) {
		        	stmt.setBoolean(index++, true);
		        }
	        }
	        rs = stmt.executeQuery();
	        while (rs.next()) {
	            v.addElement(rs.getString("ern_child_ent_id"));
	        }
	    }finally{
	    	cwSQL.cleanUp(rs, stmt);
	    	if(tableName != null){
	        	cwSQL.dropTempTable(con, tableName); 
	        }
	    }
	    return v;
    }
    public static long getRootId(Connection con, String tree_type, long org_ent_id)
        throws SQLException{
        String sql;
        long id;

        if (tree_type.equalsIgnoreCase("INDUSTRY")) {
        	sql = "select idc_ent_id AS id from IndustryCode where idc_ent_id_root = ? and idc_type = 'ROOT'";
        } else if (tree_type.equalsIgnoreCase("GRADE")) {
            sql = "select ugr_ent_id AS id from UserGrade where ugr_ent_id_root = ? and ugr_type = 'ROOT'";
        } else if (tree_type.equalsIgnoreCase("USER_GROUP")){
            return org_ent_id;
        }else{
            // user classification
            sql = "SELECT ent_id AS id FROM UserClassification, Entity WHERE ucf_ent_id = ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL AND ucf_ent_id_root = ? and ucf_type = 'ROOT' and ent_type = '" + tree_type + "'";
        }

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, org_ent_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            id = rs.getLong("id");
        } else {
            id = 0;
        }

        stmt.close();
        return id;
    }

    public static String getUsersFromGroup(Connection con, String node_id_lst, String node_type_lst) throws SQLException, cwException, qdbException{
        StringBuffer result = new StringBuffer();
        Vector node_id_vec  = cwUtils.splitToVec(node_id_lst, "~");
        Vector node_type_vec = cwUtils.splitToVecString(node_type_lst, "~");
        StringBuffer ent_id_buf = new StringBuffer();
        Vector usr_ent_id_vec = new Vector();

        if (node_id_vec.size() != node_type_vec.size()) {
            throw new cwException("# of ids doesn't match with # of types");
        }

        for (int i=0; i<node_id_vec.size(); i++) {
            if (((String)node_type_vec.elementAt(i)).equals("USR")) {
                usr_ent_id_vec.addElement((Long)node_id_vec.elementAt(i));
            } else {
                ent_id_buf.append((Long)node_id_vec.elementAt(i)).append("~");
            }
        }
        long[] ent_id_array = dbUserGroup.constructEntId(con, ent_id_buf.toString());
CommonLog.debug("ent_id_array = " + ent_id_array);
        for (int i=0; i<ent_id_array.length; i++) {
            usr_ent_id_vec.addElement(new Long(ent_id_array[i]));
        }

        cwUtils.removeDuplicate(usr_ent_id_vec);
CommonLog.debug("<><><>" + cwUtils.vector2list(usr_ent_id_vec));

        PreparedStatement stmt = con.prepareStatement("select usr_ent_id, usr_display_bil from RegUser where usr_ent_id in " + cwUtils.vector2list(usr_ent_id_vec) + " ORDER BY usr_display_bil");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            result.append(rs.getLong("usr_ent_id")).append(":_:_:").append(rs.getString("usr_display_bil")).append(":_:_:USR:_:_:");
        }

        stmt.close();

        return result.toString();
    }

    public static String[] getTargetEntity(Connection con, long root_ent_id) throws SQLException, cwException {
        String lst = null;
        PreparedStatement stmt = con.prepareStatement("select ste_targeted_entity_lst from acSite where ste_ent_id = ?");
        stmt.setLong(1, root_ent_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            lst = rs.getString("ste_targeted_entity_lst");
        } else {
            throw new cwException("ViewEntityToTree.getTargetEntity: Can't retrieve inforation from acSite where root_ent_id = " + root_ent_id);
        }

        stmt.close();

        return cwUtils.splitToString(lst, "~");
    }
    
    public static Vector getExemptEntity(Connection con, long root_ent_id) throws SQLException, cwException {
        Vector vtEntity = new Vector();
        PreparedStatement stmt = con.prepareStatement("SELECT uct_type FROM UserClassificationType WHERE uct_ent_id_root = ? AND uct_exemption_ind = ? ");
        stmt.setLong(1, root_ent_id);
        stmt.setBoolean(2, true);
        
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            vtEntity.addElement(rs.getString("uct_type"));
        } 
        stmt.close();
        if (vtEntity.size() == 0){
            throw new cwException("Cannot get classification type for exemption");
        }
        return vtEntity;
    }
    
    public Vector getTcUsgAsVector(Connection con, long tcr_id) throws SQLException, qdbException {
		Vector ent_lst = new Vector();
		String str = "select ern_child_ent_id from tcTrainingCenterTargetEntity, EntityRelation where tce_tcr_id = ? and tce_ent_id = ern_child_ent_id and ern_type = ? and ern_parent_ind =? ";
		PreparedStatement stmt = con.prepareStatement(str);
		stmt.setLong(1, tcr_id);
		stmt.setString(2, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
		stmt.setBoolean(3, true);
		ResultSet rs = stmt.executeQuery();
		Vector vec = new Vector();
		while(rs.next()) {
			vec.add(new Long(rs.getLong("ern_child_ent_id")));
		}
    	if(vec != null && vec.size() > 0) {
			Vector toRemove = new Vector();
			for (int i = 0; i < vec.size(); i++) {
				Long cur_usg_id = (Long)vec.elementAt(i);
				if (toRemove.contains(cur_usg_id)) {
					continue;
				}
				Vector sub_group = dbUserGroup.getChildGroupVec(con, cur_usg_id.longValue());
				for(int j=0; j<sub_group.size(); j++) {
					Long temp = (Long)sub_group.elementAt(j);
					if(vec.contains(temp)){
						toRemove.add(temp);
					}
				}
			}
			for(int i=0; i<toRemove.size(); i++) {
				vec.remove((Long)toRemove.elementAt(i));
			}
    	}
    	if(vec != null && vec.size() > 0) {
    		String usg_id_lst = cwUtils.vector2list(vec);	    		
			StringBuffer sql = new StringBuffer();
			sql.append(" select ent_id AS id, usg_display_bil AS title, ent_type AS type ")
				.append(" from UserGroup, Entity")
				.append(" where usg_ent_id in")
				.append(usg_id_lst)
				.append(" and usg_ent_id = ent_id")
				.append(" and ent_delete_usr_id IS NULL")
				.append(" and ent_delete_timestamp IS NULL")
				.append(" order by type, title");
			stmt = con.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			entityInfo info;
			while (rs.next()) {
				info = new entityInfo();
				info.ent_id = rs.getLong("id");
				info.title = rs.getString("title");
				info.type = rs.getString("type");
				ent_lst.addElement(info);
			}
    	}		
		stmt.close();
		return ent_lst;
	}
    
	public Vector getSubTcInfo(Connection con, long node_id, loginProfile prof, boolean isShow4TaInAnno) throws SQLException {
		Vector vec = new Vector();
		boolean isLearner = AccessControlWZB.isLrnRole( prof.current_role);
		boolean isInstr = AccessControlWZB.isLrnRole( prof.current_role);
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct cur.tcr_id AS tcr_id, cur.tcr_title AS tcr_title, 'tc' AS tcr_type, child.tcr_id AS sub_tcr_id ")
			.append(" from tcTrainingCenter cur");
		//filter for learner and instructor
		if(isLearner || isInstr || isShow4TaInAnno){
			sql.append("  inner join tcTrainingCenterTargetEntity cur_tce on (cur_tce.tce_tcr_id = cur.tcr_id) ")
				.append(" left join ( ")
				.append(" select tcr_parent_tcr_id, tcr_id from tcTrainingCenter ")
				.append(" inner join tcTrainingCenterTargetEntity on tce_tcr_id = tcr_id ")
				.append(" where tcr_ste_ent_id = ? ")
				.append(" and tcr_status = ? ")
				.append(" and tce_ent_id in ").append(prof.usrGroupsList()).append(" ) child on (cur.tcr_id = child.tcr_parent_tcr_id) ");
		} else {
			sql.append(" left join tcTrainingCenter child on (cur.tcr_id = child.tcr_parent_tcr_id and child.tcr_ste_ent_id = ? and child.tcr_status = ?)");
		}
		sql.append(" where cur.tcr_parent_tcr_id = ?")
			.append(" and cur.tcr_ste_ent_id = ?")
			.append(" and cur.tcr_status = ?");
		//filter for learner or instructor
		if(isLearner || isInstr || isShow4TaInAnno){
			sql.append(" and cur_tce.tce_ent_id in ").append(prof.usrGroupsList());
		}
		sql.append(" order by tcr_title, tcr_id");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		stmt.setLong(index++, node_id);
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		entityInfo info;
		long temp_tcr_id = 0;
		while (rs.next()) {
			info = new entityInfo();
			if(temp_tcr_id == 0 || temp_tcr_id != rs.getLong("tcr_id")) {
				temp_tcr_id = rs.getLong("tcr_id");
				info.ent_id = temp_tcr_id;
				info.title = rs.getString("tcr_title");
				info.type = rs.getString("tcr_type");
				if(rs.getString("sub_tcr_id") != null) {
					info.has_child = "YES";
				} else {
					info.has_child = "NO";
				}
				vec.addElement(info);			
			}			
		}
        stmt.close();
		return vec;
	}
	
	public Vector getTopTcInfo(Connection con, loginProfile prof, boolean includeCatalog, boolean includeObjective, long sgp_id, boolean noacl) throws SQLException {
		Vector tcIdLst = new Vector();
		AcTrainingCenter actc = new AcTrainingCenter(con);
		long sup_tcr_id = prof.my_top_tc_id;
		if(sup_tcr_id < 1){
			sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
		}
		boolean rolTcInd = AccessControlWZB.isRoleTcInd( prof.current_role);
		if (!rolTcInd || noacl) {
			tcIdLst.add(new Long(sup_tcr_id)); 
		} else if(rolTcInd) {
			tcIdLst = ViewTrainingCenter.getTopLevelTCList(con, prof);
		}
		Vector vec = new Vector();
		if(tcIdLst != null && tcIdLst.size()>0) {
			String tc_id = cwUtils.vector2list(tcIdLst);
			StringBuffer sql = new StringBuffer();
			sql.append(" select cur.tcr_id AS tcr_id, cur.tcr_title AS tcr_title, 'tc' AS tcr_type, c.tcr_id AS sub_tcr_id");
			if(includeCatalog) {
				sql.append(", cat_id");
			} else if(includeObjective) {
				
				sql.append(", obj_id AS obj_id_1");
			}
			sql.append(" from tcTrainingCenter cur")
				.append(" left join tcTrainingCenter c on (cur.tcr_id = c.tcr_parent_tcr_id and c.tcr_ste_ent_id = ? and c.tcr_status = ?) ");
			if(includeCatalog) {
				sql.append(" left join aeCatalog on (cur.tcr_id = cat_tcr_id)");
			} else if(includeObjective) {
//				if(isInstr) {
//					sql.append(" inner join tcTrainingCenterTargetEntity on (tce_ent_id in").append(prof.usrGroupsList()).append("and tce_tcr_id = c.tcr_id)");
//				}
				sql.append(" left join Objective on (cur.tcr_id = obj_tcr_id)");
//				if(rolTcInd) {
//					sql.append(" left join ObjectiveAccess on (oac_obj_id = obj_id and (oac_ent_id = ? or oac_ent_id is null))");
//				}
			}
			sql.append(" where cur.tcr_id in ")
				.append(tc_id)
				.append(" and cur.tcr_status = ?")
				.append(" order by tcr_title, tcr_id");
			PreparedStatement stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setLong(index++, prof.root_ent_id);
			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
//			if(includeObjective && rolTcInd) {
//				stmt.setLong(index++, prof.usr_ent_id);
//			}
			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
			ResultSet rs = stmt.executeQuery();
			entityInfo info;
			long temp_tcr_id = 0;
			while (rs.next()) {
				info = new entityInfo();
				if(temp_tcr_id == 0 || temp_tcr_id != rs.getLong("tcr_id")) {
					temp_tcr_id = rs.getLong("tcr_id");
					info.ent_id = temp_tcr_id;
					info.title = rs.getString("tcr_title");
					info.type = rs.getString("tcr_type");
					if(includeCatalog || includeObjective) {
						info.node_type = cwTree.NODE_TYPE_TC;
						if(includeCatalog) {
							if(rs.getString("sub_tcr_id") == null && rs.getString("cat_id") == null) {
								info.has_child = "NO";
							} else {
								info.has_child = "YES";
							}
						} else if(includeObjective) {
							if(rs.getString("sub_tcr_id") == null && rs.getString("obj_id_1") == null) {
								info.has_child = "NO";
							} else {
								info.has_child = "YES";
							}
						}
					} else {
						if(rs.getString("sub_tcr_id") != null) {
							info.has_child = "YES";
						} else {
							info.has_child = "NO";
						}
					}
					vec.addElement(info);			
				}			
			}		
            stmt.close();
		}
		return vec;
	}
	
	//1. if this API is used for a learner or a instructor, get super tc and sub tc which are the user belong to;
	//2. if this API is used for a learner in Learning Catalog's Advanced Search, get catalog besides 1 (isShowForLrnInCatalog is for this);
	//3. if this API is used for a TA to see Announcements from homepage, the same as 1 (isShow4TaInAnno is for this);
	//4. is this API is used for a TA or SA as normal, get super tc and sub tc which are the user can manage.
	public Vector getSuperTcInfo(Connection con, loginProfile prof, boolean isLrn, boolean isInstr, boolean isShowForLrnInCatalog, boolean isShow4TaInAnno) throws SQLException {
		Vector vec = new Vector();
		StringBuffer sql = new StringBuffer();
		sql.append(" select cur.tcr_id AS tcr_id, cur.tcr_title AS tcr_title, 'tc' AS tcr_type, ");
		if(isLrn  || isShow4TaInAnno) {
			sql.append("child.child_end_id ");
		} else {
			sql.append(" child.tcr_id ");
		}
		sql.append(" AS sub_tcr_id")
			.append(" from tcTrainingCenter cur");
		if(isLrn  || isShow4TaInAnno) {
			sql.append(" left join (")
				.append(" select tcr_parent_tcr_id as parent_tcr_id, tcr_id as child_end_id from tcTrainingCenter ")
				.append(" inner join tcTrainingCenterTargetEntity on tce_tcr_id = tcr_id ")
				.append(" where tcr_ste_ent_id = ?")
				.append(" and tcr_status = ? ")
				.append(" and tce_ent_id in").append(prof.usrGroupsList());
			if(isShowForLrnInCatalog) {
				sql.append(" union")
					.append(" select cat_tcr_id as parent_tcr_id, cat_id as child_end_id")
					.append(" from aeCatalog")
					.append(" where cat_status = ?")
					.append(" and cat_owner_ent_id = ?");
			}
			sql.append(" )child on (cur.tcr_id = child.parent_tcr_id)");
		} else {
			sql.append(" left join tcTrainingCenter child on (cur.tcr_id = child.tcr_parent_tcr_id and child.tcr_ste_ent_id = ? and child.tcr_status = ?) ");
		}
		sql.append(" where cur.tcr_ste_ent_id = ? ")
			.append(" and cur.tcr_status = ?");
		if(qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()){
			sql.append(" and  cur.tcr_id = ? ");
		}else{
			sql.append(" and cur.tcr_parent_tcr_id is null");
		}
		sql.append(" order by tcr_title");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		if(isShowForLrnInCatalog) {
			stmt.setString(index++, aeCatalog.CAT_STATUS_ON);
			stmt.setLong(index++, prof.root_ent_id);
		}
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		if(qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()){
			stmt.setLong(index++, prof.my_top_tc_id);
		}
		ResultSet rs = stmt.executeQuery();
		entityInfo info;
		long temp_tcr_id = 0;
		while (rs.next()) {
			info = new entityInfo();
			if(temp_tcr_id == 0 || temp_tcr_id != rs.getLong("tcr_id")) {
				temp_tcr_id = rs.getLong("tcr_id");
				info.ent_id = temp_tcr_id;
				info.title = rs.getString("tcr_title");
				info.type = rs.getString("tcr_type");
				info.node_type = cwTree.NODE_TYPE_TC;
				if(rs.getString("sub_tcr_id") != null) {
					info.has_child = "YES";
				} else {
					info.has_child = "NO";
				}
				vec.addElement(info);			
			}			
		}			
        stmt.close();
		return vec;
	}
	
	public Vector getMyTcInfo(Connection con, loginProfile prof, boolean includeCatalog, boolean includeObjective, long sgp_id) throws SQLException {
        Vector tcIdLst = new Vector();
        AcTrainingCenter actc = new AcTrainingCenter(con);
        
        long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
		if(!AccessControlWZB.isSysAdminRole( prof.current_role)){
			sup_tcr_id = prof.my_top_tc_id;
		}
//		boolean isInstr = AccessControlWZB.isLrnRole( prof.current_role);
		
//        boolean isSa = ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role);
        //boolean isSa = true;
        if (!AccessControlWZB.isRoleTcInd(prof.current_role)) {
            tcIdLst.add(new Long(sup_tcr_id));
        } else {

            tcIdLst = ViewTrainingCenter.getTopLevelTCList(con, prof);              
        }
        Vector vec = new Vector();
        if(tcIdLst != null && tcIdLst.size()>0) {
            String tc_id = cwUtils.vector2list(tcIdLst);
            StringBuffer sql = new StringBuffer();
            sql.append(" select cur.tcr_id AS tcr_id, cur.tcr_title AS tcr_title, 'tc' AS tcr_type, c.tcr_id AS sub_tcr_id");
            if(includeCatalog) {
                sql.append(", cat_id");
            } else if(includeObjective) {
    
                    sql.append(", obj_id AS obj_id_1");
            }
            sql.append(" from tcTrainingCenter cur")
                .append(" left join tcTrainingCenter c on (cur.tcr_id = c.tcr_parent_tcr_id and c.tcr_ste_ent_id = ? and c.tcr_status = ?) ");
            if(includeCatalog) {
                sql.append(" left join aeCatalog on (cur.tcr_id = cat_tcr_id)");
            } else if(includeObjective) {
                
                sql.append(" left join Objective on (cur.tcr_id = obj_tcr_id)");
                
            }
            sql.append(" where cur.tcr_id in ")
                .append(tc_id)
                .append(" and cur.tcr_status = ?")
                .append(" order by tcr_title, tcr_id");
            PreparedStatement stmt = con.prepareStatement(sql.toString());
            int index = 1;
            stmt.setLong(index++, prof.root_ent_id);
            stmt.setString(index++, DbTrainingCenter.STATUS_OK);
            
            stmt.setString(index++, DbTrainingCenter.STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            entityInfo info;
            long temp_tcr_id = 0;
            while (rs.next()) {
                info = new entityInfo();
                if(temp_tcr_id == 0 || temp_tcr_id != rs.getLong("tcr_id")) {
                    temp_tcr_id = rs.getLong("tcr_id");
                    info.ent_id = temp_tcr_id;
                    info.title = rs.getString("tcr_title");
                    info.type = rs.getString("tcr_type");
                    if(includeCatalog || includeObjective) {
                        info.node_type = cwTree.NODE_TYPE_TC;
                        if(includeCatalog) {
                            if(rs.getString("sub_tcr_id") == null && rs.getString("cat_id") == null) {
                                info.has_child = "NO";
                            } else {
                                info.has_child = "YES";
                            }
                        } else if(includeObjective) {
                            if(rs.getString("sub_tcr_id") == null && rs.getString("obj_id_1") == null) {
                                info.has_child = "NO";
                            } else {
                                info.has_child = "YES";
                            }
                        }
                    } else {
                        if(rs.getString("sub_tcr_id") != null) {
                            info.has_child = "YES";
                        } else {
                            info.has_child = "NO";
                        }
                    }
                    vec.addElement(info);           
                }           
            }           
            stmt.close();
        }
        return vec;
    }
	
	public Vector getSubTcAndCatalogInfo(Connection con, long node_id, loginProfile prof, boolean cat_pub_ind, boolean includeItem, long itm_id) throws SQLException {
		Vector vec = new Vector();
    	boolean isLrn = AccessControlWZB.isLrnRole(prof.current_role);
		StringBuffer sql = new StringBuffer();
		sql.append(" select parent.tcr_id AS ent_id, parent.tcr_title AS ent_title, 'tc_catalog' AS type, '")
			.append(cwTree.NODE_TYPE_TC)
			.append("' AS node_type ,")
			.append(cwSQL.replaceNull("child.tcr_id", "cat_id"))
			.append(" as sub_ent_id ")
			.append(" from tcTrainingCenter parent");
		if(!includeItem && isLrn) {
			sql.append(" inner join tcTrainingCenterTargetEntity parent_tce on (parent_tce.tce_tcr_id = parent.tcr_id) ")
				.append(" left join ( ")
				.append(" select tcr_parent_tcr_id, tcr_id from tcTrainingCenter ")
				.append(" inner join tcTrainingCenterTargetEntity on tce_tcr_id = tcr_id ")
				.append(" where tcr_ste_ent_id = ? ")
				.append(" and tcr_status = ? ")
				.append(" and tce_ent_id in ").append(prof.usrGroupsList()).append(" ) child on (parent.tcr_id = child.tcr_parent_tcr_id) ");
		}else {
			sql.append(" left join tcTrainingCenter child on (parent.tcr_id = child.tcr_parent_tcr_id and child.tcr_ste_ent_id = ? and child.tcr_status = ?)");
		}
		sql.append(" left join aeCatalog on (cat_tcr_id = parent.tcr_id");
		if(!includeItem && isLrn) {
			sql.append(" and cat_status = ?");
		}
		sql.append(") where parent.tcr_parent_tcr_id = ?")
			.append(" and parent.tcr_ste_ent_id = ?")
			.append(" and parent.tcr_status = ?");
		if(!includeItem && isLrn) {
			sql.append(" and parent_tce.tce_ent_id in ").append(prof.usrGroupsList());
		}
		if(itm_id > 0) {
			sql.append(" and (parent.tcr_id = (select itm_tcr_id from aeItem where itm_id=?)")
				.append(" or")
				.append(" parent.tcr_id in (select tcn_ancestor from tcRelation, aeItem where tcn_child_tcr_id = itm_tcr_id and itm_id = ?))");
		}
		sql.append(" union")
			.append(" select p.tnd_id AS ent_id, cat_title AS ent_title, 'tc_catalog' AS type, '")
			.append(cwTree.NODE_TYPE_CATALOG)
			.append("' AS node_type, c.tnd_id AS sub_ent_id")
			.append(" from tcTrainingCenter, aeCatalog, aeTreeNode p")
			.append(" left join aeTreeNode c on(");
		if(!includeItem) {
			sql.append(" c.tnd_type <> ? and");
			if(isLrn) {
				sql.append(" c.tnd_status = ? and");
			}
		}
		sql.append(" c.tnd_parent_tnd_id = p.tnd_id)")
			.append(" where tcr_id = ?")
			.append(" and tcr_ste_ent_id = ?")
			.append(" and tcr_status = ?")
			.append(" and tcr_id = cat_tcr_id")
			.append(" and p.tnd_cat_id = cat_id")
			.append(" and p.tnd_parent_tnd_id is null")
			.append(" and p.tnd_type = ?  ")
			.append(" and cat_public_ind = ?");
		if(!includeItem && isLrn) {
			sql.append(" and cat_status = ?");
		}
		sql.append(" order by node_type desc, ent_title, ent_id");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		if(!includeItem && isLrn) {
			stmt.setString(index++, aeCatalog.CAT_STATUS_ON);
		}
		stmt.setLong(index++, node_id);
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		if(itm_id > 0) {
			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, itm_id);
		}
		if(!includeItem) {
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			if(isLrn) {
				stmt.setString(index++, aeTreeNode.TND_STATUS_ON);
			}
		}
		stmt.setLong(index++, node_id);
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		stmt.setString(index++, aeTreeNode.TND_TYPE_CAT);
		stmt.setBoolean(index++, cat_pub_ind);
		if(!includeItem && isLrn) {
			stmt.setString(index++, aeCatalog.CAT_STATUS_ON);
		}
		ResultSet rs = stmt.executeQuery();
		entityInfo info;
		long temp_ent_id = 0;
		while(rs.next()) {
			info = new entityInfo();
			if(temp_ent_id == 0 || temp_ent_id != rs.getLong("ent_id")) {
				temp_ent_id = rs.getLong("ent_id");
				info.ent_id = temp_ent_id;
				info.title = rs.getString("ent_title");
				info.type = rs.getString("type");
				info.node_type = rs.getString("node_type");
				if(rs.getString("sub_ent_id") != null) {
					info.has_child = "YES";
				} else {
					info.has_child = "NO";
				}
				vec.addElement(info);			
			}	
		}
        stmt.close();
		return vec;
	}
	
	public Vector getChildAndItemNode(Connection con, long node_id, loginProfile prof, boolean includeItem, boolean filtIntgItem, boolean integratedInd) throws SQLException {
		return getChildAndItemNode(con, node_id, prof, includeItem, false, false, filtIntgItem, integratedInd,null);
	}
	public Vector getChildAndItemNode(Connection con, long node_id, loginProfile prof, boolean includeItem, boolean filterRun, boolean isGetItemAndRun, boolean filtIntgItem, String[] itm_type) throws SQLException {
	   return this.getChildAndItemNode(con, node_id, prof, includeItem, filterRun, isGetItemAndRun, filtIntgItem, false, itm_type);
	}
	public Vector getChildAndItemNode(Connection con, long node_id, loginProfile prof, boolean includeItem, boolean filterRun, boolean isGetItemAndRun, boolean filtIntgItem, boolean integratedInd) throws SQLException{
		return this.getChildAndItemNode(con, node_id, prof, includeItem, filterRun, isGetItemAndRun, filtIntgItem, integratedInd, null);
	}
	/**
	 * to get child nodes that contain catalog and item.
	 * @param con
	 * @param node_id
	 * @param prof
	 * @param includeItem Whether the child nodes contain item
	 * @param filterRun Whether the course has created class
	 * @param isGetItemAndRun Whether it get the SELFSTUDY course and the class list of CLASSROOM course 
	 * @param integratedInd 是否是项目式培训
	 * @return list of child nodes
	 * @throws SQLException
	 */
	public Vector getChildAndItemNode(Connection con, long node_id, loginProfile prof, boolean includeItem, boolean filterRun, boolean isGetItemAndRun, boolean filtIntgItem, boolean integratedInd,String[] itm_type) throws SQLException {
		Vector vec = new Vector();
    	boolean isLrn = AccessControlWZB.isLrnRole( prof.current_role);
		StringBuffer sql = new StringBuffer();
		sql.append("select p.tnd_id AS ent_id, p.tnd_title AS ent_title, p.tnd_type AS type, c.tnd_id AS sub_ent_id, 1 AS flag, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as ent_parent_itm_title ")
			.append(" from aeTreeNode p")
			.append(" left join aeTreeNode c on (");
		if(!includeItem) {
			sql.append(" c.tnd_type <> ? and ");
			if(isLrn) {
				sql.append(" c.tnd_status = ? and");
			}
		} else if(filtIntgItem) {
			sql.append(" c.tnd_type <> ? and ");
		}
		sql.append(" c.tnd_parent_tnd_id = p.tnd_id)")
			.append(" where p.tnd_parent_tnd_id = ?")
			.append(" and p.tnd_type <> ?");
		if(filtIntgItem) {
			sql.append(" union")
			.append(" select p.tnd_id AS ent_id, p.tnd_title AS ent_title, p.tnd_type AS type, itm_id AS sub_ent_id, 1 AS flag, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as ent_parent_itm_title ")
			.append(" from aeTreeNode p")
			.append(" left join aeTreeNode c on (c.tnd_type = ? and c.tnd_parent_tnd_id = p.tnd_id)")
			.append(" left join aeItem on(itm_id = c.tnd_itm_id and itm_integrated_ind = 0)")
			.append(" where p.tnd_parent_tnd_id = ?")
			.append(" and p.tnd_type <> ?");
		}
		if(includeItem && !isGetItemAndRun) {
			sql.append(" union")
				.append(" select tnd_itm_id AS ent_id, tnd_title AS ent_title, tnd_type AS type, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER)).append(" as sub_ent_id, 2 AS flag ")
				.append(" , '' as ent_parent_itm_title")
				.append(" from aeTreeNode, aeItem ")
				.append(" where tnd_parent_tnd_id = ?")
				.append(" and itm_type != 'AUDIOVIDEO'")
				.append(" and tnd_type = ? and tnd_itm_id = itm_id").append(filterRun ? " and itm_create_run_ind = 1 " : "")
				.append(filtIntgItem ? " and itm_integrated_ind = 0" : "")
				.append(integratedInd ? " and itm_ref_ind = 0 ": "");
				
			if(itm_type != null && itm_type.length > 0) {
				String itmType = "";
				for(String str : itm_type) {
					itmType += "'" + str + "',";
				}
				if(itmType.length()>0) {
					itmType = itmType.substring(0,itmType.length()-1);
				}
				sql.append(" and itm_type in (" + itmType + " )");
			//	sql.append(" and itm_exam_ind <> 1");
				sql.append(" order by flag, ent_title, ent_id");
			}
			if(filtIntgItem && !integratedInd) {
				if(itm_type == null || itm_type.length == 0) {
					sql.append(" order by sub_ent_id desc");
				} else {
					sql.append(", sub_ent_id desc");
				}
			}
			//add itm_type condition
			//union class list
		} else if(includeItem && isGetItemAndRun) {
			// selfstudy course
			sql.append(" union")
				.append(" select tnd_itm_id AS ent_id, tnd_title AS ent_title, tnd_type AS type, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER)).append(" as sub_ent_id, 2 AS flag ")
				.append(" , "+cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)+" as ent_parent_itm_title")
				.append(" from aeTreeNode, aeItem ")
				.append(" where tnd_parent_tnd_id = ?")
				.append(" and tnd_type = ? and tnd_itm_id = itm_id and itm_type in(?, ?,?)");
			// class of classroom
			sql.append(" union")
				.append(" select c.itm_id AS ent_id, c.itm_title AS ent_title, tnd_type AS type, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER)).append(" as sub_ent_id, 2 AS flag ")
				.append(" , p.itm_title as ent_parent_itm_title")
				.append(" from aeTreeNode")
				.append(" inner join aeItem p on (p.itm_id = tnd_itm_id and p.itm_id = tnd_itm_id and p.itm_type = ? and p.itm_create_run_ind = 1)")
				.append(" inner join aeItemRelation on (ire_parent_itm_id = p.itm_id)")
				.append(" inner join aeItem c on (c.itm_id = ire_child_itm_id )")
				.append(" where tnd_parent_tnd_id = ? and tnd_type = ? ");
			//sort result
			sql.append(" order by flag, ent_title, ent_id");
		} else if(isLrn){
			sql.append(" and p.tnd_status = ?");
		}
		PreparedStatement stmt = null;
		if(integratedInd){
			stmt = con.prepareStatement(" select ent_id, ent_title, type, (case when count(sub_ent_id) = 0 then null else count(sub_ent_id) end) as sub_ent_id, flag, ent_parent_itm_title from ( " + sql.toString() + " ) a group by ent_id, ent_title,type,flag,ent_parent_itm_title order by ent_id,type, sub_ent_id ");
		} else {
			stmt = con.prepareStatement(sql.toString());
		}
		int index = 1;
		if(!includeItem) {
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			if(isLrn) {
				stmt.setString(index++, aeTreeNode.TND_STATUS_ON);
			}
		} else if(filtIntgItem) {
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
		}
		stmt.setLong(index++, node_id);
		stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
		if(filtIntgItem) {
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			stmt.setLong(index++, node_id);
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
		}
		if(includeItem && !isGetItemAndRun) {
			stmt.setLong(index++, node_id);
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
		} else if(includeItem && isGetItemAndRun) {
			stmt.setLong(index++, node_id);
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			stmt.setString(index++, aeItem.ITM_TYPE_SELFSTUDY);
			stmt.setString(index++, aeItem.ITM_TYPE_INTEGRATED);
			stmt.setString(index++, aeItem.ITM_TYPE_VIDEO);
			stmt.setString(index++, aeItem.ITM_TYPE_CLASSROOM);
			stmt.setLong(index++, node_id);
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
		} else if(isLrn){
			stmt.setString(index++, aeTreeNode.TND_STATUS_ON);
		}
		ResultSet rs = stmt.executeQuery();
		entityInfo info;
		long temp_ent_id = 0;
		while(rs.next()) {
			info = new entityInfo();
			if(temp_ent_id == 0 || temp_ent_id != rs.getLong("ent_id")) {
				temp_ent_id = rs.getLong("ent_id");
				info.ent_id = temp_ent_id;
				String parentItmTitle = rs.getString("ent_parent_itm_title");
				if(isGetItemAndRun && parentItmTitle != null && !"".equals(parentItmTitle)) {
					info.title = rs.getString("ent_title") + "(" + parentItmTitle + ")";
				} else {
					info.title = rs.getString("ent_title");
				}
				info.type = rs.getString("type");
				info.node_type = "";//means the node is under the catalog
				if(rs.getString("sub_ent_id") != null) {
					info.has_child = "YES";
				} else {
					info.has_child = "NO";
				}
				vec.addElement(info);			
			}	
		}
        stmt.close();
		return vec;
	}

	public Vector getSubTcAndObjectiveInfo(Connection con, long sylId, long node_id, loginProfile prof) throws SQLException {
		boolean isSa =  ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role);
		Vector vec = new Vector();
		StringBuffer sql = new StringBuffer();
		sql.append(" select parent.tcr_id AS ent_id, parent.tcr_title AS ent_title, 'tc_objective' AS type, '")
			.append(cwTree.NODE_TYPE_TC)
			.append("' AS node_type ,")
			.append(cwSQL.replaceNull("child.tcr_id", "obj_id"))
			.append(" as sub_ent_id ")
			.append(" from tcTrainingCenter parent");
		if(isSa) {
			sql.append(" left join Objective on (obj_tcr_id = parent.tcr_id and obj_status = ? and obj_syl_id = ?)");
		} else {
			sql.append(" left join (")
				.append(" select obj_tcr_id, obj_id from Objective ")
				.append(" inner join ObjectiveAccess on(oac_obj_id = obj_id and (oac_ent_id = ? or oac_ent_id is null))")
				.append(" where obj_status = ? and obj_syl_id = ?")
				.append(" ) obj on(parent.tcr_id = obj_tcr_id)");
		}
		sql.append(" left join tcTrainingCenter child on (parent.tcr_id = child.tcr_parent_tcr_id and child.tcr_ste_ent_id = ? and child.tcr_status = ?)");
		sql.append(" where parent.tcr_parent_tcr_id = ?")
			.append(" and parent.tcr_ste_ent_id = ?")
			.append(" and parent.tcr_status = ?");
		
		sql.append(" union");
		
		sql.append(" select p.obj_id AS ent_id, p.obj_desc AS ent_title, 'tc_objective' AS type, '")
			.append(cwTree.NODE_TYPE_OBJECTIVE)
			.append("' AS node_type, c.obj_id AS sub_ent_id")
			.append(" from tcTrainingCenter, Objective p ")
			.append(" left join Objective c on (p.obj_id = c.obj_obj_id_parent and c.obj_status = ? and c.obj_syl_id = ? ) ");
//		if(!isSa) {
//			sql.append(" inner join ObjectiveAccess  on (oac_obj_id = p.obj_id and (oac_ent_id = ? or oac_ent_id is null))");
//		}
		sql.append(" where tcr_id = ?")
			.append(" and tcr_ste_ent_id = ?")
			.append(" and tcr_status = ?")
			.append(" and tcr_id = p.obj_tcr_id")
			.append(" and p.obj_status = ?")
			.append(" and p.obj_syl_id = ? ")
			.append(" and p.obj_ancester is null ")
			.append(" order by node_type desc, ent_title, ent_id");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		if(isSa) {
			stmt.setString(index++, dbObjective.OBJ_STATUS_OK);
			stmt.setLong(index++, sylId);
		} else {
			stmt.setLong(index++, prof.usr_ent_id);
			stmt.setString(index++, dbObjective.OBJ_STATUS_OK);
			stmt.setLong(index++, sylId);
		}
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		stmt.setLong(index++, node_id);
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		
		stmt.setString(index++, dbObjective.OBJ_STATUS_OK);
		stmt.setLong(index++, sylId);
//		if(!isSa) {
//			stmt.setLong(index++, prof.usr_ent_id);
//		}
		stmt.setLong(index++, node_id);
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		stmt.setString(index++, dbObjective.OBJ_STATUS_OK);
		stmt.setLong(index++, sylId);	
		ResultSet rs = stmt.executeQuery();
		entityInfo info;
		long temp_ent_id = 0;
		String temp_ent_type = "";
		while(rs.next()) {
			info = new entityInfo();
			if((temp_ent_id == 0 && temp_ent_type.equalsIgnoreCase("")) || (temp_ent_id != rs.getLong("ent_id") || !temp_ent_type.equalsIgnoreCase(rs.getString("node_type")))) {
				temp_ent_id = rs.getLong("ent_id");
				temp_ent_type = rs.getString("node_type");
				info.ent_id = temp_ent_id;
				info.title = rs.getString("ent_title");
				info.type = rs.getString("type");
				info.node_type = temp_ent_type;
				if(rs.getString("sub_ent_id") != null) {
					info.has_child = "YES";
				} else {
					info.has_child = "NO";
				}
				vec.addElement(info);			
			}	
		}
        stmt.close();
		return vec;
	}
	
	public Vector getChildObjectiveNode(Connection con, long sylId, long node_id, long root_ent_id) throws SQLException {
		Vector vec = new Vector();
		StringBuffer sql = new StringBuffer();
		sql.append("select p.obj_id AS ent_id, p.obj_desc AS ent_title, 'tc_objective' AS type, '")
			.append(cwTree.NODE_TYPE_OBJECTIVE)
			.append("' AS node_type , c.obj_id AS sub_ent_id")
			.append(" from Objective p")
			.append(" left join Objective c on (c.obj_obj_id_parent = p.obj_id and c.obj_syl_id = ? and c.obj_status = ?)")
			.append(" where p.obj_obj_id_parent = ?")
			.append(" and p.obj_syl_id = ?")
			.append(" and p.obj_status = ?")
			.append(" order by ent_title, ent_id");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, sylId);
		stmt.setString(index++, dbObjective.OBJ_STATUS_OK);
		stmt.setLong(index++, node_id);
		stmt.setLong(index++, sylId);
		stmt.setString(index++, dbObjective.OBJ_STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		entityInfo info;
		long temp_ent_id = 0;
		while(rs.next()) {
			info = new entityInfo();
			if(temp_ent_id == 0 || temp_ent_id != rs.getLong("ent_id")) {
				temp_ent_id = rs.getLong("ent_id");
				info.ent_id = temp_ent_id;
				info.title = rs.getString("ent_title");
				info.type = rs.getString("type");
				info.node_type = rs.getString("node_type");
				if(rs.getString("sub_ent_id") != null) {
					info.has_child = "YES";
				} else {
					info.has_child = "NO";
				}
				vec.addElement(info);			
			}	
		}
        stmt.close();
		return vec;
	}
    public Vector getAllSkillSet(Connection con) throws SQLException{
    	String sql = "select sks_skb_id, sks_type, sks_title, sks_ske_id From cmSkillSet where sks_type = ? ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt=con.prepareStatement(sql);
        stmt.setString(1, CmSkillSetManager.SKILL_PROFILE);
        rs = stmt.executeQuery();
        Vector setVc =new Vector();
        StringBuffer idBuf =new StringBuffer();
        while(rs.next()){
        	entityInfo ent_info =new entityInfo();
        	ent_info.ent_id =rs.getLong("sks_ske_id");
        	ent_info.type = rs.getString("sks_type");
        	ent_info.title = rs.getString("sks_title");
        	ent_info.has_child ="YES";
        	setVc.add(ent_info);
        }
        stmt.close();
		return setVc;	
    }
    
    public Vector getAllSkillGroup(Connection con) throws SQLException{
    	String sql = "select parent.skb_title parent_skb_title,sk.skb_ske_id,sk.skb_title" +
    			" From cmSkillBase sk, cmSkillBase parent" +
    			" where sk.skb_type = ?" +
    			" and sk.skb_delete_timestamp is null " +
    			" and sk.skb_parent_skb_id = parent.skb_id" +
    			" and parent.skb_type = ? ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt=con.prepareStatement(sql);
        stmt.setString(1, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL);
        stmt.setString(2, DbCmSkillBase.COMPETENCY_GROUP);
        rs = stmt.executeQuery();
        Vector skillVc =new Vector();
        while(rs.next()){
        	entityInfo ent_info =new entityInfo();
        	ent_info.ent_id =rs.getLong("skb_ske_id");
        	ent_info.type = DbCmSkillBase.COMPETENCY_GROUP;
        	ent_info.title = rs.getString("parent_skb_title")+"/"+rs.getString("skb_title");
        	ent_info.has_child="NO";
        	skillVc.add(ent_info);
        }
        stmt.close();
        return skillVc;
    }

    public Vector getSkillBySet(Connection con, long ske_id) throws SQLException{
    	String sql="select parent.skb_title parent_skb_title, sk.skb_ske_id, sk.skb_title" +
    			" From cmSkillSet ,cmSkillSetCoverage, cmSkillBase sk, cmSkillBase parent" +
    			" where sks_skb_id = ssc_sks_skb_id and sks_type = ?  " +
    			" and ssc_skb_id = sk.skb_id and sk.skb_type = ?" +
    			" and sk.skb_delete_timestamp is null and sk.skb_parent_skb_id = parent.skb_id" +
    			" and parent.skb_type = ? and sks_ske_id = ?  ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt=con.prepareStatement(sql);
        stmt.setString(1, CmSkillSetManager.SKILL_PROFILE);
        stmt.setString(2, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL);      
        stmt.setString(3, DbCmSkillBase.COMPETENCY_GROUP);
        stmt.setLong(4, ske_id);
        rs = stmt.executeQuery();
        Vector skillVc =new Vector();
        StringBuffer idBuf =new StringBuffer();
        while(rs.next()){
        	entityInfo ent_info =new entityInfo();
        	ent_info.ent_id =rs.getLong("skb_ske_id");
        	ent_info.type = DbCmSkillBase.COMPETENCY_GROUP;
        	ent_info.title = rs.getString("parent_skb_title")+"/"+rs.getString("skb_title");
        	ent_info.has_child="NO";
        	skillVc.add(ent_info);
        }
        stmt.close();
        return skillVc;

    }
    
    public Vector getTopTcByItem(Connection con, loginProfile prof, String type ) throws SQLException {
    	Vector vec = new Vector();
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select tcr_id, tcr_code, tcr_title, 'tc' as tcr_type, tcn_child_tcr_id as sub_tcr_id")
    		.append(" from tcTrainingCenter")
    		.append(" left join tcRelation on (tcn_ancestor = tcr_id)")
    		.append(" where tcr_ste_ent_id = ?")
    		.append(" and tcr_status = ?")
    		.append(" and tcr_parent_tcr_id is null");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		entityInfo info;
		long temp_tcr_id = 0;
		while (rs.next()) {
			info = new entityInfo();
			if(temp_tcr_id == 0 || temp_tcr_id != rs.getLong("tcr_id")) {
				temp_tcr_id = rs.getLong("tcr_id");
				info.ent_id = temp_tcr_id;
				info.title = rs.getString("tcr_title");
				info.type = rs.getString("tcr_type");
				info.node_type = cwTree.NODE_TYPE_TC;
				if(rs.getString("sub_tcr_id") != null) {
					info.has_child = "YES";
				} else {
                    if(type != null && type.equalsIgnoreCase(cwTree.TC_CATALOG_ITEM)){
                        info.has_child = "YES";
                    }else{
					info.has_child = "NO";
                    }
				}
				vec.addElement(info);			
			}
		}			
        stmt.close();
		return vec;
    }
}