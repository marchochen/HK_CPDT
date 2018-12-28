package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.db.view.ViewObjectiveAccess;
import com.cwn.wizbank.security.AclFunction;

public class AcObjective extends AcInstance {
//    public static final String FTN_OBJ_MGT = "OBJ_MGT";
//	public static final String FTN_OBJ_MAIN = "LRN_RES_MAIN";
//	public static final String FTN_OBJ_ADMIN = "LRN_RES_ADMIN";
	//public static final String ADM_ACCESS = "OWNER";
	public static final String READER = "READER";
	public static final String AUTHOR = "AUTHOR";
	public static final String OWNER = "OWNER";
	//to allow that learner view resources in current directory(only teaching material,SCORM,AICC,NETG can be viewed)
	public static final String LRN_VIEW = "LRN_VIEW";
	public static final String ADM = "ADM_1";
	public static final String TADM = "TADM_1";
	public static final String INSTR = "INSTR_1";
	public static final int EDIT_OPER = 1;
	public static final int APPOINT_OPER = 2;
	
	//for label
	public static final String LABEL_ROL_TADM = "lab_rol_TADM";

    public AcObjective(Connection con) {
        super(con);
    }
  

	public boolean hasAdminPrivilege(long ent_id, String rol_ext_id) 
		throws SQLException {
                
		return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_RES_MAIN) ;
	}
 
 
    
    public boolean hasManagePrivilege(long ent_id,long obj_id,String rol_ext_id) throws SQLException{
		boolean canEdit = false;
	 
    	return  AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_RES_MAIN) ;
    }
    
	public boolean hasAppointPrivilege(long ent_id,long obj_id,String rol_ext_id) throws SQLException{
		return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_RES_MAIN) ;
	}
    
    private boolean hasObjAccess(long ent_id,long obj_id,int type) throws SQLException{
    	boolean hasAccess = false;
    	String access = getObjectiveAccess(obj_id,ent_id);
    	if(type == EDIT_OPER){
    	    if(OWNER.equalsIgnoreCase(access) || AUTHOR.equalsIgnoreCase(access))
    	          hasAccess = true;
    	}else if (type == APPOINT_OPER){
    		if(OWNER.equalsIgnoreCase(access))
    		      hasAccess = true;
    	}
    	return hasAccess;
    }

	
    
	/*
	 * get the access of the objective
	 */
		public String getObjectiveAccess(long objId,long entId ,boolean flag) throws SQLException{
			String access = "";
			ViewObjectiveAccess doa =new ViewObjectiveAccess();
			//DbObjective obj = new DbObjective();
			if(flag)
			   access = doa.getRootObjAccess(con,objId,entId);
			else{
			   long rootId = dbObjective.getRootObjId(con,objId);
			   access = doa.getRootObjAccess(con,rootId,entId);
			}
			return access;
		}
		
		
	public String getObjectiveAccess(long objId,long entId) throws SQLException{
		String access = "";
		//DbObjective obj = new DbObjective();
		boolean isRoot = dbObjective.isRootObj(con,objId);
		access = getObjectiveAccess(objId,entId,isRoot);
		return access;
	}
		
	
	/*
	 * get the access of all top folders
	 */	
		public Map getTopAccess(Vector objVec,long entId) throws SQLException{
			Map access = new HashMap();
			int size = objVec.size();
			boolean root = true;
			try{
			  for(int i=0;i<size;i++){
				dbObjective dbo = (dbObjective)objVec.get(i);
				 String objAccess = this.getObjectiveAccess(dbo.obj_id,entId,root);
				 access.put(new Long(dbo.obj_id),objAccess);
			  }
			}catch(Exception e){
				throw new SQLException(e.getMessage());
			}
			  return access;
		}
		
		public Map getMyRootFoleders(long entId,long sylId,boolean tc_enabled, long tcr_id,  String order_by) throws SQLException {
			ViewObjectiveAccess doa =new ViewObjectiveAccess();
			return doa.getAccessFolders(con,entId,sylId, tc_enabled,tcr_id, order_by);
		}
		public Map getInstrRootFoleders(long entId,long sylId,boolean tc_enabled, long tcr_id, String order_by) throws SQLException {
			ViewObjectiveAccess doa =new ViewObjectiveAccess();
			return doa.getInstrAccessFolders(con,entId,sylId, tc_enabled, tcr_id, order_by);
		}

}
     
            
            
