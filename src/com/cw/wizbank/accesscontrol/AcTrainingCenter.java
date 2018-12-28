/*
 * Created on 2007-5-11
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.accesscontrol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.security.AclFunction;

public class AcTrainingCenter extends AcInstance  {

    public AcTrainingCenter(Connection con) {
        super(con);
    }
    

    
    public boolean canMgtTc(long usr_ent_id, String rol_ext_id, long root_ent_id, long tcr_id) throws SQLException {
    	boolean result = false;
    	if((AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_TC_MAIN) && !AccessControlWZB.isRoleTcInd(rol_ext_id)) || (AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_TC_MAIN) && ViewTrainingCenter.isSuperTA(con, root_ent_id, usr_ent_id, rol_ext_id))) {
    		result = true;
    	} else {
    		Vector tcId = ViewTrainingCenter.getAllTcByOfficer(con, usr_ent_id);
    		if(tcId.contains(new Long(tcr_id))) {
    			result = true;
    		}
    	}
		return result;
	}
    public boolean canTaMgtTc(long usr_ent_id, long tcr_id) throws SQLException {
		boolean result = false;
		Vector tcId = ViewTrainingCenter.getAllTcByOfficer(con, usr_ent_id);
		if(tcId.contains(new Long(tcr_id))) {
			result = true;
		}
		return result;
    }
	public String hasTcInMgtTc(long usr_ent_id, long tcr_id, String current_role) throws SQLException {
		String code = "";
		if(AccessControlWZB.hasRolePrivilege( current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
			if(canTaMgtTc(usr_ent_id, tcr_id) || !AccessControlWZB.isRoleTcInd(current_role)) {
				code = dbObjective.CAN_MGT_OBJ;
			} else {
				code = dbObjective.OBJ_NOT_IN_TA_MGT_TC;
			}
		} else {
			code = dbObjective.CAN_MGT_OBJ;
		}
		return code;
	}
	public String hasObjInMgtTc(long usr_ent_id, long obj_id, String current_role) throws SQLException {
		String  code = "";
		if(AccessControlWZB.hasRolePrivilege( current_role,  AclFunction.FTN_AMD_TC_MAIN)) {
			if(hasObjInTaMgtTc(usr_ent_id, obj_id) || !AccessControlWZB.isRoleTcInd(current_role)) {
				code = dbObjective.CAN_MGT_OBJ;
			} else {
				code = dbObjective.OBJ_NOT_IN_TA_MGT_TC; 
			}
		} else{
			code = dbObjective.CAN_MGT_OBJ;
		}
		return code;
	}
	public String hasResInMgtTc(long usr_ent_id, long res_id, String current_role) throws SQLException {
		String  code = "";
		if(AccessControlWZB.hasRolePrivilege( current_role,  AclFunction.FTN_AMD_TC_MAIN) ) {
			if(hasResInTaMgtTc(usr_ent_id, res_id) || !AccessControlWZB.isRoleTcInd(current_role)) {
				code = dbResource.CAN_MGT_RES;
			} else {
				code = dbResource.RES_NOT_IN_TA_MGT_TC; 
			}
		} else {
			code = dbResource.CAN_MGT_RES;
		}
		return code;
	}
	public boolean hasObjInTaMgtTc(long usr_ent_id, long obj_id) throws SQLException {
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(tco_tcr_id) from Objective child ")
		   .append(" left join Objective ancestor on (")
		   .append(cwSQL.subFieldLocation("child.obj_ancester", "ancestor.obj_id", true))
		   .append(" and  ancestor.obj_tcr_id is not null) ")
		   .append(" inner join tcTrainingCenter on (tcr_id = ancestor.obj_tcr_id or tcr_id = child.obj_tcr_id) ")
		   .append(" left join tcRelation on (tcn_child_tcr_id = tcr_id) ")
		   .append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = tcn_ancestor or tco_tcr_id = tcr_id) ") 
		   .append(" where child.obj_id =? ")
		   .append(" and (tco_usr_ent_id = ? or child.obj_share_ind = 1)");
		 PreparedStatement stmt = con.prepareStatement(sql.toString());
		 int index = 1;
		 stmt.setLong(index++, obj_id);
		 stmt.setLong(index++, usr_ent_id);
		 ResultSet rs = stmt.executeQuery();
		 if(rs.next()) {
			long cnt = rs.getLong(1);
			if(cnt > 0) {
				result = true;
			}
		 }
		 stmt.close();
		 return result;
	}
	public boolean hasResInTaMgtTc(long usr_ent_id, long res_id) throws SQLException {
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(tco_tcr_id) from ResourceObjective ")
		   .append(" inner join Objective child on (child.obj_id = rob_obj_id) ")
		   .append(" left join Objective ancestor on (")
		   .append(cwSQL.subFieldLocation("child.obj_ancester", "ancestor.obj_id", true))
		   .append(" and  ancestor.obj_tcr_id is not null) ")
		   .append(" inner join tcTrainingCenter on (tcr_id = ancestor.obj_tcr_id or tcr_id = child.obj_tcr_id) ")
		   .append(" left join tcRelation on (tcn_child_tcr_id = tcr_id) ")
		   .append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = tcn_ancestor or tco_tcr_id = tcr_id) ") 
		   .append(" where rob_res_id =? ")
		   .append(" and (tco_usr_ent_id = ? or child.obj_share_ind = 1)");
		 PreparedStatement stmt = con.prepareStatement(sql.toString());
		 int index = 1;
		 stmt.setLong(index++, res_id);
		 stmt.setLong(index++, usr_ent_id);
		 ResultSet rs = stmt.executeQuery();
		 if(rs.next()) {
			long cnt = rs.getLong(1);
			if(cnt > 0) {
				result = true;
			}
		 }
		 stmt.close();
		 return result;
	}
	public boolean hasTaInTc(long root_ent_id, String rol_ext_id, long[] target_ent_id_lst, long tcr_id) throws SQLException {
		boolean result = false;
		Vector vec_usr = DbTrainingCenter.getTcTaOfficer(con, tcr_id, rol_ext_id);
		if(vec_usr != null && vec_usr.size() > 0 ) {
			for(int i =0; i< target_ent_id_lst.length; i++) {
				Long ent_id = new Long(target_ent_id_lst[i]);
				if(vec_usr.contains(ent_id)) {
					result = true;
					continue;
				} else {
					result = false;
					break;
				}
			
	    	}
		}
		return result;
	}
	public boolean hasInstrInTc(long root_ent_id, String rol_ext_id, long[] target_ent_id_lst, long tcr_id) throws SQLException {
		boolean result = false;
		Vector role_types = new Vector();
		role_types.add(rol_ext_id);
		Vector vec_usr = DbTrainingCenter.getMgtResoursUsrEntId(con, role_types, root_ent_id, tcr_id);
		if(vec_usr != null && vec_usr.size() > 0 ) {
			for(int i =0; i< target_ent_id_lst.length; i++) {
				Long ent_id = new Long(target_ent_id_lst[i]);
				if(vec_usr.contains(ent_id)) {
					result = true;
					continue;
				} else {
					result = false;
					break;
				}
			
	    	}
		}
		return result;
	}
	public boolean hasMgtObjUsrInTc(long root_ent_id, long[] target_ent_id_lst, long tcr_id) throws SQLException  {
		boolean result = false;
		AccessControlCore acCore = new AccessControlCore();
		
		String rol_ext_id =  acCore.getRoleExtId(con, root_ent_id, AccessControlWZB.ROL_STE_UID_INST);
		Vector role_types = new Vector();
		role_types.add(rol_ext_id);
		Vector vec_usr = DbTrainingCenter.getMgtResoursUsrEntId(con, role_types, root_ent_id, tcr_id);
		
		String rol_ext_id_ta  = acCore.getRoleExtId(con, root_ent_id, AccessControlWZB.ROL_STE_UID_TADM);
		Vector vec_ta = DbTrainingCenter.getTcOfficer(con, tcr_id, target_ent_id_lst);
		
		Vector target_vec = new Vector();
		if(vec_usr !=null && vec_usr.size() > 0) {
			for(int i = 0; i< vec_usr.size(); i++) {
				target_vec.add(vec_usr.get(i));
			}
		}
		if(vec_ta  !=null && vec_ta .size() > 0) {
			for(int i = 0; i< vec_ta .size(); i++) {
				target_vec.add(vec_ta .get(i));
			}
		}
		if(target_vec !=null && target_vec.size() > 0 ) {
			for(int i =0; i< target_ent_id_lst.length; i++) {
				Long ent_id = new Long(target_ent_id_lst[i]);
				if(target_vec.contains(ent_id)) {
					result = true;
					continue;
				} else {
					result = false;
					break;
				}
	    	}
		} 
    	return result;
    }
    public boolean canMgtTcForTp(long usr_ent_id, String rol_ext_id, long root_ent_id, long tcr_id) throws SQLException {
    	boolean result = false;

    	String sql = " select count(*) cnt from tcTrainingCenterOfficer"
    		 	   + " inner join tcTrainingCenter p on (tco_tcr_id = p.tcr_id and p.tcr_status = ?  and  p.tcr_ste_ent_id = ?)"
    		 	   + " inner join tcTrainingCenter c on (c.tcr_parent_tcr_id = p.tcr_id and c.tcr_status = ?  and  c.tcr_ste_ent_id = ? and c.tcr_id = ? )" 
    			   + " where tco_usr_ent_id = ? and tco_rol_ext_id = ? "; 
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(index++, root_ent_id);
    	stmt.setLong(index++, tcr_id);
    	stmt.setLong(index++, usr_ent_id);
    	stmt.setString(index++, rol_ext_id);
    	ResultSet rs = stmt.executeQuery();
    	if(rs.next()) {
    		if(rs.getLong("cnt") > 0) {
    			result = true;
    		}
    	}
    	stmt.close();
    	return result;
    }  
    
}
