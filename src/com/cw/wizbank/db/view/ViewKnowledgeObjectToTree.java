package com.cw.wizbank.db.view;

import java.util.Vector;
import java.sql.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cwn.wizbank.utils.CommonLog;

public class ViewKnowledgeObjectToTree {
    
    public class objectInfo {
        public long id;
        public String title;
        public String type;
    }
    
    public long getSyllabusId(Connection con, long org_ent_id) throws SQLException {
        long sylId = 0;
        PreparedStatement stmt = con.prepareStatement(" Select syl_id from Syllabus where syl_ent_id_root = ? ");
        stmt.setLong(1, org_ent_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            sylId = rs.getLong("syl_id");
        }
        
        stmt.close();
        return sylId;
    }
    /*
    public Vector syllabusListAsVector(Connection con, long org_ent_id) throws SQLException {
        Vector syl_lst = new Vector();
        PreparedStatement stmt = con.prepareStatement("select syl_id, syl_desc, 'SYB_ROOT' type from Syllabus where syl_privilege = 'PUBLIC' union select syl_id, syl_desc, 'SYB_ROOT' type from Syllabus where syl_ent_id_root = ? order by syl_desc");
//System.out.println("select syl_id, syl_desc, 'SYB_ROOT' type from Syllabus where syl_locale = 'HK' and syl_privilege = 'CW' union select syl_id, syl_desc, 'SYB_ROOT' type from Syllabus where syl_ent_id_root = ? order by syl_desc");
        stmt.setLong(1, org_ent_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            objectInfo info = new objectInfo();
            info.id = rs.getLong("syl_id");
            info.title = rs.getString("syl_desc");
            info.type = rs.getString("type");
            syl_lst.addElement(info);
        }
        
        stmt.close();
        return syl_lst;
    }
    */
    /*
    public Vector syllabusHasChild(Connection con, Vector lst) throws SQLException {
        Vector syl_lst = new Vector();
        StringBuffer str = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        
        str.append("(0");
        
        for (int i=0; i<lst.size(); i++) {
            str.append(", ").append(((objectInfo)lst.elementAt(i)).id);   
        }
        
        str.append(")");
        sql.append("select distinct syl_id from Syllabus, Objective where syl_id in ").append(str.toString()).append(" and obj_syl_id = syl_id order by syl_id");
//System.out.println(sql.toString());
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            for (int i=0; i<lst.size(); i++) {
                long id = rs.getLong("syl_id");
                objectInfo info = (objectInfo)lst.elementAt(i);
                
                if (id == info.id) {
                    syl_lst.addElement(info);
                }
            }
        }
        
        stmt.close();
        return syl_lst;
    }
    */
    
    public Vector syllabusRootListAsVector(Connection con, long syl_id, long usr_ent_id, String rol_ext_id, long my_top_tcr_id, boolean tc_enabled,boolean isTcIndependent) throws SQLException {
        Vector obj_lst = new Vector();
		PreparedStatement stmt = null;
        AcObjective acObj = new AcObjective(con);
        boolean isAdmin = acObj.hasAdminPrivilege(usr_ent_id, rol_ext_id);
        
        String shareIndWhere = " 1 = 1 ";
        if(isTcIndependent){//如果LRN模式，不显示共享文件夹
        	shareIndWhere = " obj_share_ind = 0 ";
        }
        
		if(isAdmin && !tc_enabled) {
			StringBuffer sql =  new StringBuffer("select obj_id, obj_desc, obj_type from Objective where obj_syl_id = ? and obj_obj_id_parent is null and obj_type = 'SYB'");
			sql.append(" and "+shareIndWhere);
			sql.append(" order by obj_desc ");
			stmt = con.prepareStatement(sql.toString());
		} else {
			
			//限制文件夹在当前用户可管理培训中心下的 sql where 条件
			String tcrWhere  = " 1=1 ";
			if(AccessControlWZB.isRoleTcInd(rol_ext_id)){
				tcrWhere = tcrWhere +  " and (tcr_id in ( select child.tcn_child_tcr_id " +
		    				" from tcTrainingCenterOfficer, tcTrainingCenter parent, tcRelation child "+
						    " where tco_tcr_id = parent.tcr_id " + 
						    " and parent.tcr_status = 'OK' " + 
						    " and parent.tcr_id = child.tcn_ancestor " + 
						    " and tco_usr_ent_id = ? " + 
						    " ) " + 
						    " or tcr_id in( " + 
						    " select tco_tcr_id from tcTrainingCenterOfficer , tcTrainingCenter  " + 
						    " where  tco_tcr_id = tcr_id and tco_usr_ent_id = ? )) ";
			
			}
			tcrWhere = tcrWhere + " and obj_tcr_id in ( select tcr_id  from tcTrainingCenter where tcr_id = ? or tcr_id in(select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ))";
			
			
			if(tc_enabled) {
				StringBuffer sql = new StringBuffer();
//				if(isAdmin) {
					sql.append(" select distinct obj_id,obj_type,obj_desc from objectiveAccess ")
					   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
					   .append(" inner join tcTrainingCenter on (obj_tcr_id = tcr_id)")
					   .append(" where "+tcrWhere)
					   .append(" and " +shareIndWhere)
					   .append(" and  tcr_status = ? ")
					   .append(" and obj_syl_id = ?   ORDER BY obj_desc ASC");					
//				} else {
//					sql.append(" select distinct obj_id,obj_type,obj_desc from objectiveAccess ")
//					   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
//					   .append(" inner join tcTrainingCenter on (obj_tcr_id = tcr_id)")
//					   .append(" where "+tcrWhere)
//					   .append(" and " +shareIndWhere)
//					   .append(" and  tcr_status = ? ")
//					   .append(" and obj_syl_id = ?  ORDER BY obj_desc ASC");
//				}
				stmt = con.prepareStatement(sql.toString());
			} else {
				stmt = con.prepareStatement("select distinct obj_id, obj_type, obj_desc from ObjectiveAccess, Objective where obj_syl_id = ? and oac_obj_id = obj_id and (oac_ent_id = ? or oac_ent_id is null)  order by obj_desc ASC ");	
			}
		}
		int index = 1;
		if(isAdmin && !tc_enabled) {
			 stmt.setLong(index++, syl_id);
		} else {
        	if(tc_enabled) {
        		if(AccessControlWZB.isRoleTcInd(rol_ext_id)){
	        		stmt.setLong(index++, usr_ent_id);
	        		stmt.setLong(index++, usr_ent_id);
        		}
        		stmt.setLong(index++, my_top_tcr_id);
        		stmt.setLong(index++, my_top_tcr_id);
    			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        	}
			stmt.setLong(index++, syl_id);
		}
		
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            objectInfo info = new objectInfo();
            info.id = rs.getLong("obj_id");
            info.title = rs.getString("obj_desc");
            info.type = rs.getString("obj_type");
            obj_lst.addElement(info);
        }
        
        stmt.close();
        return obj_lst;
    }
    
    public Vector objectiveListAsVector(Connection con, long obj_id) throws SQLException {
        Vector obj_lst = new Vector();
        PreparedStatement stmt = con.prepareStatement("select obj_id, obj_desc, obj_type from Objective where obj_obj_id_parent = ? order by obj_desc ");
        
//System.out.println("select obj_id, obj_desc, obj_type from Objective where obj_obj_id_parent = ? union select obj_id, obj_desc, obj_type from Objective, ObjectiveRelation where ore_obj_id_referenced = ? and obj_id = ore_obj_id order by obj_desc");
//System.out.println(">>> " + obj_id);
        stmt.setLong(1, obj_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            objectInfo info = new objectInfo();
            info.id = rs.getLong("obj_id");
            info.title = rs.getString("obj_desc");
            info.type = rs.getString("obj_type");
            obj_lst.addElement(info);
        }
        
        stmt.close();
        return obj_lst;
    }
    
    public Vector objectiveSharedListAsVector(Connection con, long root_ent_id) throws SQLException {
        Vector obj_lst = new Vector();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("select obj_id,obj_desc,obj_type from objective,syllabus where obj_type = ? and obj_share_ind = ? and obj_status=? and obj_syl_id = syl_id and syl_ent_id_root =? order by obj_desc");
            int idx = 1;
            stmt.setString(idx++, dbObjective.OBJ_TYPE_SYB);
            stmt.setBoolean(idx++, true);
            stmt.setString(idx++, dbObjective.OBJ_STATUS_OK);
            stmt.setLong(idx++, root_ent_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                objectInfo info = new objectInfo();
                info.id = rs.getLong("obj_id");
                info.title = rs.getString("obj_desc");
                info.type = rs.getString("obj_type");
                obj_lst.addElement(info);
            }
            return obj_lst;
        } finally {
            if (stmt != null) stmt.close();
        }        
    }
    
    public Vector resourceListAsVector(Connection con, long obj_id, String gpIds) throws SQLException {
        Vector res_lst = new Vector();
        StringBuffer sql = new StringBuffer();
        
        sql.append("select distinct res_id, res_title, res_type from ResourceObjective, Resources, ResourcePermission where rob_obj_id = ? and res_id = rob_res_id and res_res_id_root is null and rpm_res_id = res_id and rpm_ent_id in ").append(gpIds).append(" and rpm_read = 1 order by res_title");
//        sql.append("select distinct res_id, res_title, res_type from ResourceObjective, Resources, ResourcePermission where rob_obj_id = ? and res_id = rob_res_id and res_privilege = 'CW' and res_res_id_root is null and rpm_res_id = res_id and rpm_ent_id in ").append(gpIds).append(" and rpm_read = 1 order by res_type");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
//System.out.println(sql.toString());
//System.out.println(">>> " + obj_id);
        stmt.setLong(1, obj_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            objectInfo info = new objectInfo();
            info.id = rs.getLong("res_id");
            info.title = rs.getString("res_title");
            info.type = rs.getString("res_type");
            res_lst.addElement(info);
        }
        
        stmt.close();
        return res_lst;
    }

    public Vector getObjectiveByOrg(Connection con, long ent_id_root) throws SQLException {
        Vector objVec = new Vector();
        StringBuffer sql = new StringBuffer();
        
        sql.append("select obj_id from Objective, Syllabus where obj_syl_id = syl_id and  syl_ent_id_root = ? order by obj_id ");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, ent_id_root);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            objVec.addElement(new Long(rs.getLong("obj_id")));
        }
        
        stmt.close();
        return objVec;
    }

    public void addResourcetoVector(Connection con, Vector objVec, String gpIds, Vector res_id_vec) throws SQLException {
        if (objVec == null || objVec.size() == 0) {
            return;
        }
        
        StringBuffer sql = new StringBuffer();
        String objLst = cwUtils.vector2list(objVec);
        sql.append("select distinct res_id from ResourceObjective, Resources, ResourcePermission ")
           .append(" where rob_obj_id IN ").append(objLst)
           .append(" and res_id = rob_res_id and res_res_id_root is null and rpm_res_id = res_id and rpm_ent_id in ")
           .append(gpIds).append(" and rpm_read = 1 order by res_id ");
        PreparedStatement stmt = con.prepareStatement(sql.toString());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            res_id_vec.addElement(new Long(rs.getLong("res_id")));
        }
        
        stmt.close();

    }
        
        
    public Vector objectiveHasChild(Connection con, Vector lst, String gpIds, boolean bShowRes) throws SQLException {
        Vector obj_lst = new Vector();
        if (lst == null || lst.size() ==0) {
            return obj_lst;
        }
        
        StringBuffer syb_lst = new StringBuffer();
        StringBuffer sql;
        PreparedStatement stmt = null;
        ResultSet rs;
        
        syb_lst.append("(0");
        
        for (int i=0; i<lst.size(); i++) {
            objectInfo info = (objectInfo)lst.elementAt(i);

            syb_lst.append(", ").append(info.id);
        }
        
        syb_lst.append(")");

        sql = new StringBuffer();
        sql.append("select obj_id from Objective where obj_id in ").append(syb_lst.toString()).append(" and obj_id IN (select obj_obj_id_parent from Objective )");

        stmt = con.prepareStatement(sql.toString());
        rs = stmt.executeQuery();

        while (rs.next()) {
            for (int i=0; i<lst.size(); i++) {
                long id = rs.getLong("obj_id");
                objectInfo info = (objectInfo)lst.elementAt(i);
                        
                if (id == info.id) {
                    obj_lst.addElement(info);
                }
            }

        }
        stmt.close();

        if (bShowRes) {
            sql = new StringBuffer();
            sql.append("select obj_id from Objective where obj_id in ")
            .append(syb_lst.toString())
            .append(" and obj_id IN (select distinct rob_obj_id from ResourceObjective, Resources, ResourcePermission where res_id = rob_res_id and res_res_id_root is null and rpm_res_id = res_id and rpm_ent_id in ").append(gpIds).append(" and rpm_read = 1 )");

            stmt = con.prepareStatement(sql.toString());
            rs = stmt.executeQuery();

            while (rs.next()) {
                for (int i=0; i<lst.size(); i++) {
                    long id = rs.getLong("obj_id");
                    objectInfo info = (objectInfo)lst.elementAt(i);
                        
                    if (id == info.id && !obj_lst.contains(info)) {
                        obj_lst.addElement(info);
                    }
                }
            }
            stmt.close();
        }
        
        return obj_lst;
    }

    public String getObjects(Connection con, String node_id_lst, String node_type_lst, long root_ent_id, String gIds) throws SQLException, cwException {

        StringBuffer result = new StringBuffer();
        Vector node_id_vec  = cwUtils.splitToVec(node_id_lst, "~");
        Vector node_type_vec = cwUtils.splitToVecString(node_type_lst, "~");
        Vector res_id_vec = new Vector();
        
        if (node_id_vec.size() != node_type_vec.size()) {
            throw new cwException("# of ids doesn't match with # of types");
        }

        for (int i=0; i<node_id_vec.size(); i++) {
            getObjectsHelper(con, ((Long)node_id_vec.elementAt(i)).longValue(), (String)node_type_vec.elementAt(i), res_id_vec, root_ent_id, gIds);
        }

        cwUtils.removeDuplicate(res_id_vec);

        if (res_id_vec != null && res_id_vec.size() > 0) {
            PreparedStatement stmt = con.prepareStatement("select res_id, res_title, res_type from Resources where res_id in " + cwUtils.vector2list(res_id_vec) + " order by res_title");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                result.append(rs.getLong("res_id")).append(":_:_:").append(rs.getString("res_title")).append(":_:_:").append(rs.getString("res_type")).append(":_:_:");
            }
            stmt.close();
        }else {
        
        }
        
        return result.toString();
    }

    public void getObjectsHelper(Connection con, long node_id, String node_type, Vector res_id_vec, long root_ent_id, String gIds) throws SQLException, cwException {
        objectInfo info = null;
        Vector objVec = null;

CommonLog.debug("getObjectsHelper , node_id = " + node_id + ", node_type = " + node_type);

        if (node_type.equals("ROOT_0_0_0")) {
            objVec = getObjectiveByOrg(con, root_ent_id);
            addResourcetoVector(con, objVec, gIds, res_id_vec);
        }else if (node_type.equals("SYB")){
            try {
                objVec = dbObjective.getSelfAndChildsObjId(con, node_id);
            }catch (Exception e) {
                throw new cwException(e.getMessage());
            }
            addResourcetoVector(con, objVec, gIds, res_id_vec);
        }else {
            res_id_vec.addElement(new Long(node_id));
        }
        
    }
    /**
     * Update:15th,June,2004      
	 * @param con 	  :Connect to the database
	 * @param node_id :Obj_id
	 * @return        :objInfo.Include obj_id,obj_desc,obj_type
	 * @throws SQLException if there are some exceptions occur.
	 */

    public  objectInfo getObjectInfo(Connection con,long node_id) throws SQLException {
        
    	PreparedStatement stmt = con.prepareStatement("select obj_desc, obj_type from Objective where obj_id = ? ");
    	stmt.setLong(1,node_id);
    	ResultSet rs = stmt.executeQuery();
		objectInfo objInfo = new objectInfo();
    	if(rs.next()){
    		
    		objInfo.id = node_id;
    		objInfo.title = rs.getString("obj_desc");
    		objInfo.type = rs.getString("obj_type");
    	}
    	stmt.close();
		return objInfo;
    }    
}