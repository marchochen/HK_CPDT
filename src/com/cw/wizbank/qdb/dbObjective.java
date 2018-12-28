package com.cw.wizbank.qdb;

import java.io.File;
import java.sql.*;
import java.util.*;

//import com.cw.wizbank.db.DbObjective;
import com.cw.wizbank.JsonMod.studyMaterial.bean.MaterialBean;
import com.cw.wizbank.JsonMod.studyMaterial.bean.MaterialCatalogBean;
import com.cw.wizbank.JsonMod.studyMaterial.bean.MaterialSrhCriteriaBean;
import com.cw.wizbank.accesscontrol.*;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.quebank.quecontainer.DynamicQueContainer;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewObjectiveAccess;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.db.DbAcRole;
import com.cw.wizbank.db.DbObjectiveAccess;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.instructor.InstructorManager;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

public class dbObjective extends Object
{
//    static final String FCN_OBJ_MGT = "OBJ_MGT";
    public static final String ID_SEPARATOR_OPEN = " ";
    public static final String ID_SEPARATOR_CLOSE = " ";
    
    //public static final String OBJ_TYPE_ASS = "ASS";
    public static final String OBJ_TYPE_SYB = "SYB";
    public static final String OBJ_TYPE_SYS = "SYS";       // SYSTEM : LOST&FOUND
    //public static final String ORE_TYPE_ASSTOSYB = "ASSTOSYB";
    public static final String LOST_FOUND = "LOST&FOUND";
    public static final String OBJ_STATUS_OK = "OK";
    public static final String OBJ_STATUS_DELETED = "DELETED";
    
    public static final String DEFAULT_COL_SORT_1 = "obj_desc";
    public static final String DEFAULT_COL_SORT_2 = "obj_type";
    public static final String DEFAULT_COL_ORDER = "ASC";
    
    static final String NOT_ENOUGH_RIGHT_MSG = "OBJ001";    //"Only administrator can modify the Syllabus.";
    static final String HAS_CHILD_MSG = "OBJ002";    //Unable to delete Objective as there are objectives/resources under it
    static final String BEING_REFERENCED_MSG = "OBJ003";    //Unable to delete Objective as there are tests referencing it
    static final String OBJ_NOT_DELETED = "OBJ004";
    static final String OBJ_IN_RCY_BIN_DONE = "OBJ005";
    static final String RES_NOT_IN_OBJ = "OBJ006";
    static final String RES_TRASH_DONE = "OBJ007";
    static final String RES_PLASTE_PRI = "OBJ008";
    static final String RES_PLASTE_PUB = "OBJ009";
    static final String OBJ_PASTE_TO_CHILD = "OBJ010";      // Cannot paste objective to its child
    //static final String OBJ_DIFFERENT_SYL = "OBJ011";      // Cannot paste objective to another syllabus
	static final String OBJ_PASTE_TO_SAME_OBJ = "OBJ011";      // Cannot paste objective to another syllabus
	public static final String OBJ_NOT_IN_TA_MGT_TC = "OBJ012";
	public static final String OBJ_NOT_ADD_MGT_READER = "OBJ013";
	public static final String OBJ_NOT_ADD_MGT_AUTHER = "OBJ014";
	public static final String OBJ_NOT_ADD_MGT_OWNER = "OBJ015";
	
	public static final String CAN_MGT_OBJ ="CAN_MGT_OBJ";
	
	public static final String[] LRN_VIEW_RES_TYPE = {"GEN", "SCORM", "AICC", "NETGCOK"};
	public static final String[] LRN_VIEW_RES_SUBTYPE = {"WCT", "RES_SCO", "SSC", "RES_NETG_COK"};

    public long    obj_id;
    public long    obj_syl_id;
    public String  obj_type;
    public String  obj_desc;
    public long    obj_obj_id_parent;
    public String  obj_ancester;
    public String  obj_status;
    public String  access;
    public long    obj_tcr_id;
    public boolean obj_share_ind;
    public boolean share_mode = false;
     //	for top-level objective access
	  public long[] reader_ent_id_lst;
	  public long[] author_ent_id_lst;
	  public long[] owner_ent_id_lst;
	  
	public boolean lrn_can_view_ind;
    
    // for Aicc
    public String obj_title;
    public String obj_developer_id;
    public String obj_import_xml;
    
    public dbObjective() {;}
    
    public dbObjective(long obj_id) {
        this.obj_id = obj_id;
        return;
    }
    
    public long insAicc(Connection con, loginProfile prof)
        throws qdbException
    {
        try {
            long max_obj_id = dbObjective.getMaxId(con) + 1;
            
            PreparedStatement stmt = con.prepareStatement(                      
                    " INSERT INTO Objective "
                    + " (obj_id, "
                    + " obj_type, "
                    + " obj_desc, "
                    + " obj_obj_id_parent, "
                    + " obj_title, "
                    + " obj_developer_id, "
                    + " obj_import_xml, "
                    + " obj_status) "
                    + " VALUES (?,?,?,?,?,?,?,?) "); 

            stmt.setLong(1, max_obj_id);
            stmt.setString(2, obj_type);
            stmt.setString(3, obj_desc);
            if (obj_obj_id_parent == 0) {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            else {
                stmt.setLong(4, obj_obj_id_parent);
            }
            stmt.setString(5, obj_title);
            stmt.setString(6, obj_developer_id);
            stmt.setString(7, obj_import_xml);
            stmt.setString(8, obj_status);
            
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback(); 
                throw new qdbException("Failed to insert objective."); 
            }

            return max_obj_id;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }                
    }
    
    public void upd(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
        	//Recycle Bin is no longer used in Learning Resource Management
			/*
            if (obj_id == getLostFoundID(con, obj_id))  {
                throw new qdbErrMessage(NOT_ENOUGH_RIGHT_MSG);
            }
            */
        	int index = 1;
        	
        	StringBuffer sql = new StringBuffer(" UPDATE Objective SET obj_desc = ?,obj_share_ind= ? ");
        	if(obj_tcr_id > 0){
        		sql.append(",obj_tcr_id = ? ");
        	}
        	sql.append(" WHERE obj_id = ? ");
        	
            PreparedStatement stmt = con.prepareStatement(sql.toString()); 
                    
            stmt.setString(index++, obj_desc);
            stmt.setBoolean(index++, obj_share_ind);
            if(obj_tcr_id > 0){
            	stmt.setLong(index++, obj_tcr_id);
            }
            
            stmt.setLong(index++, obj_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback(); 
                throw new qdbException("Failed to update objective."); 
            }
            //修改子目录培训中心
            if(obj_tcr_id > 0){
            	updByParentId(con, obj_id, obj_tcr_id);
            }
            con.commit(); 
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

    /**
     * 修改子目录的培训中心
     * @param con
     * @param obj_id
     * @param obj_tcr_id
     * @return
     * @throws SQLException 
     */
    private void updByParentId(Connection con, long obj_id, long obj_tcr_id) throws SQLException{
    	PreparedStatement stmt = null;
        String sql = " UPDATE Objective SET obj_tcr_id = ? where obj_ancester like ? ";
        try {
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, obj_tcr_id);
            stmt.setString(index++, ("%"+obj_id+"%"));
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    public static long insLostFoundID(Connection con, long sylID)
        throws qdbException
    {
        try {
            long objId = dbObjective.getMaxId(con) + 1;
            
            PreparedStatement stmt = con.prepareStatement(
                      " INSERT INTO Objective "
                    + "    (obj_id, obj_syl_id, obj_type, obj_desc) " 
                    + " VALUES (?,?,?,?) ");

            stmt.setLong(1,objId);
            stmt.setLong(2,sylID);
            stmt.setString(3,OBJ_TYPE_SYS);
            stmt.setString(4,LOST_FOUND);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                throw new qdbException("Failed to insert lost and found id."); 
            }
            return objId;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
        
    }
    
    /*
    public static int getObjLevel(Connection con, long objId) 
        throws qdbException
    {
        try {
            
            int level = 0;
            long sybObjId = 0;
            
            PreparedStatement stmt =null;
            ResultSet rs = null;
            
            dbObjective dbobj = new dbObjective(); 
            dbobj.obj_id = objId; 
            dbobj.get(con);
            
            // Get the first SYB objective id
            
            if(dbobj.obj_type.equalsIgnoreCase(OBJ_TYPE_SYB)) {
                    sybObjId = dbobj.obj_id; 
            }else if(dbobj.obj_type.equalsIgnoreCase(OBJ_TYPE_ASS)) {
                    stmt = con.prepareStatement(
                        " SELECT ore_obj_id_referenced FROM " 
                        + "    ObjectiveRelation " 
                        + " WHERE "
                        + "        ore_obj_id = ? " );
                    
                    stmt.setLong(1,dbobj.obj_id); 
                    rs = stmt.executeQuery(); 
                    if (rs.next()) {
                        level ++;
                        sybObjId = rs.getLong("ore_obj_id_referenced"); 
                    }else {
                        throw new qdbException("Failed to get parent id.");
                    }
            }

            sybObjId = dbobj.obj_id;  

            String SQL = " SELECT obj_obj_id_parent FROM Objective " 
                        + "    WHERE obj_id = ? ";
            
            while (sybObjId > 0 && level < 100) {
                    level ++;
                    stmt = con.prepareStatement(SQL); 
                    stmt.setLong(1,sybObjId);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        sybObjId = rs.getLong("obj_obj_id_parent");
                    }else {
                        throw new qdbException("Failed to get parent id.");
                    }
            }
            stmt.close();
            return level;
         
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        } 
    }
    */
    
    // Recycle Bin is no longer used in Learning Resource Management
    /*
    public static long getLostFoundID(Connection con, long objId)
        throws qdbException
    {
        try {
            dbObjective dbobj = new dbObjective();
            dbobj.obj_id = objId;
            dbobj.get(con);
            
            PreparedStatement stmt = con.prepareStatement(
                  " SELECT obj_id FROM Objective " 
                + "    WHERE obj_syl_id = ? " 
                + "      and obj_desc = ? " 
                + "      and obj_type = ? ");
                
            stmt.setLong(1, dbobj.obj_syl_id);
            stmt.setString(2,LOST_FOUND);
            stmt.setString(3,OBJ_TYPE_SYS);
            
            ResultSet rs = stmt.executeQuery(); 
            long lf_id = 0; 
            if (rs.next()) {
                lf_id = rs.getLong("obj_id"); 
            }else {
                // insert a lost and found id
                lf_id = dbObjective.insLostFoundID(con, dbobj.obj_syl_id);
            }
            
            stmt.close();
            if (lf_id > 0) {
                return lf_id; 
            }else {
                throw new qdbException("Failed to get lost and found id."); 
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
	*/

    //check if this objective has child in Objective, ObjectiveRelation or ResourceObjective
    //pre-set var: obj_id
    
	public boolean hasChild(Connection con) throws SQLException {
		   boolean result;
		   if(hasSybChild(con))
			   result = true;
		   else {
			   //if(hasAssChild(con))
			   //    result = true;
			   //else
			   result = hasResChild(con);
		   }
		   return result;
	   }
	   
	   
    public boolean hasVisibleChild(Connection con, String[] types) throws SQLException {
    	//DbObjective dbObj = new DbObjective();
        boolean result;
        if(hasSybChild(con))
            result = true;
        else {
            //if(hasAssChild(con))
            //    result = true;
            //else
            result = hasDirectChildRes(con, types);
        }
        return result;
    }
    
    /*private boolean hasDirectChild(Connection con,String[] types) throws SQLException{
        StringBuffer sql =new StringBuffer("select * from ResourceObjective,Resources where rob_obj_id = ? and rob_res_id = res_id and res_res_id_root is null and res_type in (");
		int i ;
		for(i = 0;i<types.length-1;i++){
			sql.append(" '").append(types[i]).append("',");
		}
		sql.append(" '").append(types[i]).append("' )");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setLong(1, obj_id);
		ResultSet rs = stmt.executeQuery();
		boolean hasDirect = false;
		if(rs.next())
		    hasDirect = true;
		stmt.close();
		return hasDirect;
    }*/

    //check if this objective has child in ResourceObjective
    //pre-set var: obj_id
    private boolean hasResChild(Connection con) throws SQLException {
        final String SQL = " Select count(*) From ResourceObjective "
                         + " Where rob_obj_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, obj_id);
        ResultSet rs = stmt.executeQuery();
        long count = 0;
        boolean result;
        if(rs.next())
            count = rs.getLong(1);
        stmt.close();
        if(count>0)
            result = true;
        else
            result = false;
        return result;
    }

    //check if this objective has child in Objective
    //pre-set var: obj_id
    private boolean hasSybChild(Connection con) throws SQLException {
        final String SQL = " Select count(*) From Objective "
                         + " Where obj_obj_id_parent = ?  and obj_status = ?";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, obj_id);
        stmt.setString(2,OBJ_STATUS_OK);
        ResultSet rs = stmt.executeQuery();
        long count = 0;
        boolean result;
        if(rs.next())
            count = rs.getLong(1);
        stmt.close();
        if(count>0)
            result = true;
        else
            result = false;
        return result;
    }

    
    //check if this objective are being referneced in ResourceContent
    //pre-set var: obj_id
    private boolean hasResourceContent(Connection con) throws SQLException {
        final String SQL = " Select count(*) From ResourceContent "
                         + " Where rcn_obj_id_content = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, obj_id);
        ResultSet rs = stmt.executeQuery();
        long count = 0;
        boolean result;
        if(rs.next())
            count = rs.getLong(1);
        stmt.close();
        if(count>0)
            result = true;
        else
            result = false;
        return result;
    }

    //check if this objective are being referneced in ModuleSpec
    //pre-set var: obj_id
    public boolean hasModuleSpec(Connection con) throws SQLException {
        final String SQL = " Select count(*) From ModuleSpec "
                         + " Where msp_obj_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, obj_id);
        ResultSet rs = stmt.executeQuery();
        long count = 0;
        boolean result;
        if(rs.next())
            count = rs.getLong(1);
        stmt.close();
        if(count>0)
            result = true;
        else
            result = false;
        return result;
    }

    
    //delete a record in Objective
    //pre-set var: obj_id
    private void del(Connection con) throws SQLException {
        //delObjRelation(con);
        final String SQL = " Delete From Objective Where obj_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, obj_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void del(Connection con, loginProfile prof,String[] types)
        throws qdbException, qdbErrMessage
    {
		//DbObjective obj = new DbObjective();
		boolean hasPotentialRes;
		boolean hasPotentialObj;
        try { 

            if(hasVisibleChild(con,types)) {
                throw new qdbErrMessage(HAS_CHILD_MSG);
            }
            
			dbObjective.deleteObj(con,obj_id, hasPotentialRes(con) || hasPotentialObj(con) || hasModuleSpec(con));
            
           
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

    //paste resources on a resource from a preiously cut objective
    //pre-set var: nil
    //return the id of the message box should call
    public static String cutPasteObjRes(Connection con, loginProfile prof, long res_id
                                  ,long fr_obj_id, long to_obj_id)
      throws qdbException, qdbErrMessage, SQLException {
        try {
            String[] res_ids = new String[1];
            res_ids[0] = Long.toString(res_id);
            mvObjRes(con, prof, res_ids, fr_obj_id, to_obj_id);
            String privilege = dbResource.getResPrivilege(con, res_id);
            String id;
            if(privilege.equals(dbResource.RES_PRIV_CW))
                id = RES_PLASTE_PUB;
            else
                id = RES_PLASTE_PRI;
            return id;
        }
        catch(cwSysMessage e) {
            throw new qdbErrMessage(e.getId());
        }
    }


    //copy a list of resources to another objective
    //pre-set var: nil
    public static void copyPasteObjResLst(Connection con, loginProfile prof, String[] res_id_lst
                               ,long  to_obj_id, String privilege, String uploadDir) 
      throws qdbException, qdbErrMessage, cwSysMessage {
        try {
            String[] rob = new String[1];
            rob[0] = Long.toString(to_obj_id);
            Timestamp curTime = cwSQL.getTime(con);
            Hashtable resHash = dbResource.getResTypeHash(con, res_id_lst);
			Hashtable resSubTypeHash = dbResource.getResSubTypeHash(con, res_id_lst);
            dbQuestion dbque = null;
            dbResource dbres = null;
            Enumeration keys = resHash.keys();
            
            long resID = 0;
            long newID  = 0;
            String resType = null;
           
            while (keys.hasMoreElements()) {
            	boolean bCopyFile = true;
                resID = ((Long) keys.nextElement()).longValue();
                resType = (String) resHash.get(new Long(resID));
                if (resType.equalsIgnoreCase(dbResource.RES_TYPE_QUE)) {
					String resSubType = (String) resSubTypeHash.get(new Long(resID));
					if(resSubType.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
						DynamicScenarioQue dsc = new DynamicScenarioQue();
						dsc.res_id = resID;
						dsc.get(con);
						dsc.copyMySelf(con, prof, to_obj_id, uploadDir);
						bCopyFile = false;
					} else if(resSubType.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)) {
						FixedScenarioQue fsc = new FixedScenarioQue();
						fsc.res_id = resID;
						fsc.get(con);
						fsc.copyMySelf(con, prof, to_obj_id, uploadDir);
						bCopyFile = false;
					} else {
	                    dbque = new dbQuestion();
	                    dbque.que_res_id = resID;
	                    dbque.res_id = dbque.que_res_id;
	                    dbque.get(con);
	                    dbque.res_usr_id_owner = prof.usr_id;
	                    dbque.res_upd_user = prof.usr_id;
	                    dbque.res_upd_date = curTime;
	                    if (privilege!=null) {                      
	                        dbque.res_privilege = privilege;                    
	                    }
	                    dbque.ins(con, rob, prof, dbResource.RES_TYPE_QUE);
	                    newID = dbque.que_res_id;
					}
                }else {
                    dbres = new dbResource();
                    dbres.res_id = resID;
                    dbres.get(con);
                    dbres.res_usr_id_owner = prof.usr_id;
                    dbres.res_upd_user = prof.usr_id;
                    dbres.res_upd_date = curTime;
                    if (privilege!=null) {
                        dbres.res_privilege = privilege;                    
                    }
                    dbres.ins_res(con, rob, prof);
                    if (resType.equalsIgnoreCase(dbResource.RES_TYPE_SCORM)) {
                    	dbScormResource srs = new dbScormResource();
                    	srs.srs_res_id = resID;
                    	srs.get(con);
                    	srs.srs_res_id = dbres.res_id;
                    	srs.ins(con);
                    }
                    newID = dbres.res_id;
                }
				if(bCopyFile) {
	                dbUtils.copyMediaFrom(uploadDir, resID, newID);
				} 
            }
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    //copy a list of resources to another objective
    //pre-set var: nil
    public static void cutPasteObjResLst(Connection con, loginProfile prof, String[] res_id_lst
                               ,long fr_obj_id ,long  to_obj_id) 
      throws qdbException, qdbErrMessage {

        mvObjRes(con, prof, res_id_lst, fr_obj_id, to_obj_id);
    
    }
    
    
    //move the resources from one objective to another one
    //pre-set var: nil
    private static void mvObjRes(Connection con, loginProfile prof, String[] res_ids
                               ,long fr_obj_id, long to_obj_id) 
      throws qdbException, qdbErrMessage {
        try {
            String SQLlist = dbUtils.array2list(res_ids);
            StringBuffer SQLBuf = new StringBuffer(100);
            SQLBuf.append(" Update ResourceObjective Set ");
            SQLBuf.append(" rob_obj_id = ? ");
            SQLBuf.append(" Where rob_obj_id = ? ");
            SQLBuf.append(" And rob_res_id In ").append(SQLlist);
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, to_obj_id);
            stmt.setLong(2, fr_obj_id);
            stmt.executeUpdate();
            stmt.close();
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    //trash the input resources in recycle bin
    //pre-set var: nil
    public static void trashObjRes(Connection con, loginProfile prof, String[] res_ids) 
        throws qdbException, qdbErrMessage {
        try {
            /*
            //check resources permission
            
            for(int i=0;i<res_ids.length;i++) {
                dbResource res = new dbResource();
                res.res_id = Long.parseLong(res_ids[i]);
                res.checkResPermission(con, prof); 
            }
            */
            //check if all in recycle bin
            if(!isAllDeleted(con, res_ids))
                throw new qdbErrMessage(OBJ_NOT_DELETED);
            
            //trash the resources
            dbResource.removeResLst(con, res_ids, prof);
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
        catch(cwSysMessage se) {
            throw new qdbErrMessage(se.getId());
        }
    }

    //check if all resources are in recycle bin
    //pre-set var: nil
    private static boolean isAllDeleted(Connection con, String[] res_ids) throws SQLException {
        long count = 0;
        boolean result;
        String SQLlist = dbUtils.array2list(res_ids);
        StringBuffer SQLBuf = new StringBuffer(100);
        SQLBuf.append(" Select count(*) From ResourceObjective, Objective ");
        SQLBuf.append(" Where rob_obj_id = obj_id ");
        SQLBuf.append(" And rob_res_id In ").append(SQLlist);
        SQLBuf.append(" And obj_type <> ? ");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setString(1, OBJ_TYPE_SYS);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) 
            count = rs.getLong(1);
        stmt.close();
        if(count > 0)
            result = false;
        else
            result = true;
            
        return result;
    }

	public static void delRes(Connection con, long res_id, String strResFolder) throws qdbException, qdbErrMessage {
		try {
			StringBuffer sql1 = new StringBuffer();
			sql1.append("DELETE FROM ResourcePermission WHERE rpm_res_id = ?"); 
			PreparedStatement stmt1 = con.prepareStatement(sql1.toString());
			stmt1.setLong(1, res_id);
			stmt1.executeUpdate();
			stmt1.close();

			StringBuffer sql2 = new StringBuffer();
			sql2.append("DELETE FROM ResourceObjective WHERE rob_res_id = ?"); 
			PreparedStatement stmt2 = con.prepareStatement(sql2.toString());
			stmt2.setLong(1, res_id);
			stmt2.executeUpdate();
			stmt2.close();
			
			StringBuffer sql3 = new StringBuffer();
			sql3.append("DELETE FROM Interaction WHERE int_res_id = ?"); 
			PreparedStatement stmt3 = con.prepareStatement(sql3.toString());
			stmt3.setLong(1, res_id);
			stmt3.executeUpdate();
			stmt3.close();
			
			StringBuffer sql4 = new StringBuffer();
			sql4.append("DELETE FROM Question WHERE que_res_id = ?"); 
			PreparedStatement stmt4 = con.prepareStatement(sql4.toString());
			stmt4.setLong(1, res_id);
			stmt4.executeUpdate();
			stmt4.close();
			
			StringBuffer sql5 = new StringBuffer();
			sql5.append("DELETE FROM QueContainerSpec WHERE qcs_res_id = ?"); 
			PreparedStatement stmt5 = con.prepareStatement(sql5.toString());
			stmt5.setLong(1, res_id);
			stmt5.executeUpdate();
			stmt5.close();

			StringBuffer sql6 = new StringBuffer();
			sql6.append("DELETE FROM ResourceContent  WHERE rcn_res_id = ?"); 
			PreparedStatement stmt6 = con.prepareStatement(sql6.toString());
			stmt6.setLong(1, res_id);
			stmt6.executeUpdate();
			stmt6.close();
			
			StringBuffer sql7 = new StringBuffer();
			sql7.append("DELETE FROM Resources WHERE res_id = ?"); 
			PreparedStatement stmt7 = con.prepareStatement(sql7.toString());
			stmt7.setLong(1, res_id);
			stmt7.executeUpdate();
			stmt7.close();
			
			strResFolder = strResFolder + cwUtils.SLASH + res_id;
			File res = new File(strResFolder);
			if (res.exists()) {
				File[] list = res.listFiles();
				for (int i=0; i<list.length; i++) {
					list[i].delete();
				}
				res.delete();
			}
		}
		catch(SQLException e) {
			CommonLog.error(e.getMessage(),e);
			throw new qdbException(e.getMessage());
		} 
	}

    //put the resources into recycle bin
    //pre-set var: obj_id
    //Recycle Bin is not longer used in Learning Resource Management
    /*
    public void delObjRes(Connection con, loginProfile prof, String[] res_ids) throws qdbException, qdbErrMessage {
        try {
            //throw the resources in recycle bin
            long LF_ID = getLostFoundID(con,obj_id); 
            String SQLlist = dbUtils.array2list(res_ids);
            StringBuffer SQLBuf = new StringBuffer(100);
            SQLBuf.append(" Update ResourceObjective Set ");
            SQLBuf.append(" rob_obj_id = ? ");
            SQLBuf.append(" where rob_obj_id = ? ");
            SQLBuf.append(" And rob_res_id in ").append(SQLlist);
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, LF_ID);
            stmt.setLong(2, obj_id);
            int count = stmt.executeUpdate();
            stmt.close();
            if(count == 0) 
                throw new qdbErrMessage(RES_NOT_IN_OBJ, " " + obj_id);
            
            //change the resources to offline
            changeResStatus(con, res_ids, dbResource.RES_STATUS_OFF);
            //dbResource.setResPublic(con, true, res_ids, prof.root_ent_id, prof.usr_ent_id);
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
	*/

    private static void changeResStatus(Connection con, String[] res_ids, String status) throws SQLException {
        String SQLlist = dbUtils.array2list(res_ids);
        StringBuffer SQLBuf = new StringBuffer(100);
        SQLBuf.append(" Update Resources Set ");
        SQLBuf.append(" res_status = ? ");
        SQLBuf.append(" where res_id in ").append(SQLlist);
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setString(1,status);
        stmt.executeUpdate();
        stmt.close();
    }
    
    
    public void get(Connection con) 
        throws qdbException
    {   
        try {
            PreparedStatement stmt = con.prepareStatement(
                      " Select obj_id, "
                    + " obj_syl_id, "
                    + " obj_type, "
                    + " obj_desc, "
                    + " obj_obj_id_parent, "
                    + " obj_ancester, " 
                    + " obj_status, "
                    + " obj_tcr_id, "
                    + " obj_share_ind"
                    + " from Objective " 
                    + " where obj_id = ? ");
                    
            stmt.setLong(1, obj_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                obj_id          = rs.getLong("obj_id");
                obj_syl_id      = rs.getLong("obj_syl_id");        
                obj_type        = rs.getString("obj_type");        
                obj_desc    = rs.getString("obj_desc");
                obj_obj_id_parent   = rs.getLong("obj_obj_id_parent");
                obj_ancester = rs.getString("obj_ancester");
                obj_status = rs.getString("obj_status");
                obj_tcr_id = rs.getLong("obj_tcr_id");
                obj_share_ind = rs.getBoolean("obj_share_ind");
            }else {
            	stmt.close();
                throw new qdbException("No such objective found."); 
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
    
    
    // New version of Knowlege Manager
    /*
    Get the top most objectives for the user
    1. Objectives belonging to his/her organization
    2. Objectives belonging to the public organization
    */
    public static String getRootSybObj(Connection con,  String privilege, String res_lan, String[] types, String[] subtypes, loginProfile prof,String choice, boolean show_all, boolean tc_enabled,long tcr_id, cwPagination cwPage,boolean isTcIndependent)
        throws SQLException, qdbException, cwSysMessage, cwException, qdbErrMessage
    {
	    Vector sylVec = dbSyllabus.getSybVec(con, privilege, prof.root_ent_id);
		AcObjective acObj = new AcObjective(con);
        StringBuffer xmlBuf = new StringBuffer();
        StringBuffer sqlBuf = new StringBuffer();
        String[] tcr_id_array = null;
        
        xmlBuf.append(dbUtils.xmlHeader);
        xmlBuf.append("<objective_list>").append(dbUtils.NEWL);
		//xmlBuf.append("<folders>").append(choice).append("</folders>");
        xmlBuf.append("<meta>");
        xmlBuf.append(prof.asXML());
        xmlBuf.append("<tc_enabled>").append(tc_enabled).append("</tc_enabled>");
        xmlBuf.append("</meta>");
        cwTree tree = new cwTree();
        xmlBuf.append(tree.genNavTrainingCenterTree(con, prof, false));
        if(tc_enabled) {
        	if(!show_all && tcr_id == 0) {
    			tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
        	}
        	if (tcr_id == 0) {
        		show_all = true;
            }
            DbTrainingCenter objTc = DbTrainingCenter.getInstance(con, tcr_id);
            if(!show_all && tcr_id != 0) {
                AcTrainingCenter actc = new AcTrainingCenter(con);
        		boolean isSa = AccessControlWZB.isRoleTcInd(prof.current_role);
        		boolean isInstr = AccessControlWZB.isIstRole(prof.current_role);
        		if(isSa || isInstr) {
        			
        		} else {
        			if(AccessControlWZB.isRoleTcInd(prof.current_role)){
            			if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, tcr_id)) {
            				throw new qdbErrMessage(TrainingCenter.NO_PERMISSION_MSG);
            			} 
        			}
        		}
            }
            if(objTc != null) {
                xmlBuf.append("<training_center id =\"").append(objTc.getTcr_id()).append("\">");
                xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
                xmlBuf.append("</training_center>");        	
            }
        }
		xmlBuf.append("<folders>").append(choice).append("</folders>");
		
		StringBuffer order_by = new StringBuffer();
		if(show_all == false) {
			cwPage = null;
		}
		if(tc_enabled && show_all && cwPage != null) {
			if(cwPage.sortCol == null){
				cwPage.sortCol = DEFAULT_COL_SORT_1;
			}
			if(cwPage.sortOrder == null) {
				cwPage.sortOrder = DEFAULT_COL_ORDER;
			}
			if(cwPage.curPage == 0) {
				cwPage.curPage = 1;
			}
			if(cwPage.pageSize == 0) {
				cwPage.pageSize = 10;
			}
			order_by.append(cwPage.sortCol).append(" ").append(cwPage.sortOrder);
		} else {
			order_by.append(DEFAULT_COL_SORT_2).append(" ").append(DEFAULT_COL_ORDER)
			        .append(" , ").append(DEFAULT_COL_SORT_1).append(" ").append(DEFAULT_COL_ORDER);
		}
		
        if(tc_enabled && choice != null) {
        	sqlBuf = new StringBuffer();
        	AcTrainingCenter actc = new AcTrainingCenter(con);	
        	if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
                sqlBuf.append(" SELECT obj_id, obj_desc, obj_type, obj_tcr_id, tcr_title FROM Objective "); 
    			if(tcr_id > 0) {
    				sqlBuf.append(" inner join tcTrainingCenter on (obj_tcr_id = tcr_id ) ");
					sqlBuf.append(" Where 1=1 and tcr_id = ? and tcr_status = ? ");
				} else {
					sqlBuf.append(" INNER join (select distinct(child.tcr_id) as tcr_id, child.tcr_title as tcr_title from tcTrainingCenter ancestor ")
					      .append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) ")
					      .append(" Left join tcRelation on (tcn_ancestor = ancestor.tcr_id) ")
					      .append(" inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) ")
					      .append(" where tco_usr_ent_id =? ")
					      .append(" and child.tcr_status = ? ")
					      .append(" and ancestor.tcr_status = ?")
					      .append(" ) TC on (obj_tcr_id = tcr_id) ")
					      .append(" Where 1=1 ");
				}	
        	} else if (AccessControlWZB.isIstRole(prof.current_role)) {
        		sqlBuf.append(" select distinct obj_id,obj_type,obj_desc,obj_tcr_id, tcr_title from objective ")
        		      .append(" INNER JOIN tcTrainingCenter on (obj_tcr_id = tcr_id ) ");
        		if(tc_enabled ){
        			if(tcr_id >0) {
        			} else {
        				sqlBuf.append(" inner join tcTrainingCenterTargetEntity on (tce_tcr_id = tcr_id) ")
        				      .append(" inner join EntityRelation on (ern_ancestor_ent_id = tce_ent_id) ");
        			}
        		}
        		sqlBuf.append(" WHERE  1=1 and tcr_status = ? ");
        		if(tc_enabled ) {
        			if(tcr_id >0 ){
        				sqlBuf.append(" and obj_tcr_id = ? ");
        			} else {
        				sqlBuf.append(" and ern_type =? ")
        				   .append(" and ern_child_ent_id = ? ")
        				   .append(" AND ern_parent_ind = ? ");
        			}
        		}
        	} else {
                sqlBuf.append(" SELECT obj_id, obj_desc, obj_type, obj_tcr_id, tcr_title FROM Objective ") 
    	  		      .append(" Left Join tcTrainingCenter on (obj_tcr_id = tcr_id) ")
    	  		      .append(" WHERE 1=1 AND tcr_status = ? ");
		  		if(tc_enabled &&!show_all&& tcr_id > 0) {
		  			sqlBuf.append(" AND tcr_id = ? ");
		  		}
        	}
        } else {
            /*prepare to chang these code*/
            sqlBuf.append(" SELECT obj_id, obj_desc, obj_type, obj_tcr_id, tcr_title FROM Objective ") 
      	  		  .append(" Left Join tcTrainingCenter on (obj_tcr_id = tcr_id) ")
      	  		  .append(" WHERE 1=1 AND tcr_status = ? ");
        	if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
		          //选全部的时候，过滤掉多余跟本用户无关的培训中心
		            if(tc_enabled &&show_all){
		            	String tc_sql = "select tco_tcr_id from tcTrainingCenterOfficer where tco_usr_ent_id = ?  and tco_rol_ext_id = ? ";
		            	StringBuffer tcr_id_buff = new StringBuffer();
		            	PreparedStatement stmt = null;
		            	ResultSet rs = null;
		            	stmt = con.prepareStatement(tc_sql + "");
		            	stmt.setLong(1, prof.usr_ent_id);
		            	stmt.setString(2, prof.current_role);
		            	rs = stmt.executeQuery();
		                while (rs.next()) {
		                	tcr_id_buff.append(rs.getLong("tco_tcr_id"));
		                	tcr_id_buff.append(",");
		                }
		                if(tcr_id_buff.length() > 0){
		                	tcr_id_buff= new StringBuffer(tcr_id_buff.toString().substring(0,tcr_id_buff.length()-1));
		                	boolean flag = true;
		                	while(flag){
			                	tc_sql = "select tcr_id from tcTrainingCenter where tcr_parent_tcr_id in (" + tc_sql +")";
			                	stmt = con.prepareStatement(tc_sql);
			                	stmt.setLong(1, prof.usr_ent_id);
			                	stmt.setString(2, prof.current_role);
			                	rs = stmt.executeQuery();
			                	flag = false;
			                	while (rs.next()) {
			                		tcr_id_buff.append(",");
			                    	tcr_id_buff.append(rs.getLong("tcr_id"));
			                    	flag = true;
			                    }
		                	}
		                }
		                stmt.close();
		                if(tcr_id_buff.toString().length() > 0){
		                	tcr_id_array = tcr_id_buff.toString().split(",");
		                	sqlBuf.append(" AND tcr_id in (");
		                	for(int i=0; i<tcr_id_array.length; i++){
		                		sqlBuf.append("?");
		                		if(i == tcr_id_array.length-1) break;
		                		sqlBuf.append(",");
		                	}
		                	sqlBuf.append(") ");
		                }
		            }
            }
    		if(tc_enabled &&!show_all&& tcr_id > 0) {
    			sqlBuf.append(" AND tcr_id = ? ");
    		}
        }
        sqlBuf.append(" AND obj_syl_id = ? ")
        	  .append(" AND obj_status = ? ")
        	  .append(" AND obj_obj_id_parent is null ");
        
	    if(tcr_id == 0){
	    	sqlBuf.append(" or obj_tcr_id in ( select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? )");
	    }
        
        sqlBuf.append(" ORDER BY ").append(order_by.toString());
        PreparedStatement stmt = null;
        dbSyllabus syl = null;
        for (int i=0;i<sylVec.size();i++) {
            syl = (dbSyllabus) sylVec.elementAt(i);
            xmlBuf.append("<syllabus id=\"").append(syl.syl_id)
                  .append("\" privilege=\"").append(syl.syl_privilege)
                  .append("\" locale=\"").append(syl.syl_locale)
                  .append("\" root_ent_id=\"").append(syl.syl_ent_id_root)
                  .append("\">").append(dbUtils.NEWL);
            xmlBuf.append("<desc>").append(dbUtils.esc4XML(syl.syl_desc)).append("</desc>").append(dbUtils.NEWL);
            xmlBuf.append("<show_all>").append(show_all).append("</show_all>");	
            xmlBuf.append("<body>").append(dbUtils.NEWL);

            stmt = con.prepareStatement(sqlBuf.toString());
            int index = 1;
            if(tc_enabled && choice != null) {
            	AcTrainingCenter actc = new AcTrainingCenter(con);	
            	if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
            		if(tcr_id > 0) {
        				stmt.setLong(index++, tcr_id);
        				stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        			} else {
        				stmt.setLong(index++, prof.usr_ent_id);
        				stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        				stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        			}
            	} else if(AccessControlWZB.isIstRole(prof.current_role)){
        			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        			if(tcr_id > 0) {
        				stmt.setLong(index++, tcr_id);
    				} else {
    					stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
    					stmt.setLong(index++, prof.usr_ent_id);
    					stmt.setBoolean(index++, true);
    				}
            	}else {
    				stmt.setString(index++, DbTrainingCenter.STATUS_OK);
            		if(tcr_id > 0&&!show_all) {
            			stmt.setLong(index++, tcr_id);	
            		}
            	}
            } else {
				stmt.setString(index++, DbTrainingCenter.STATUS_OK);
				if(tc_enabled &&show_all && tcr_id_array!=null ){
						for(int i1 = 0; i1 < tcr_id_array.length; i1++){
							stmt.setLong(index++, Long.parseLong(tcr_id_array[i1]));
						}
				}
        		if(tcr_id > 0&&!show_all) {
        			stmt.setLong(index++, tcr_id);
        		}
            }
            stmt.setLong(index++, syl.syl_id);
            stmt.setString(index++, OBJ_STATUS_OK);
            
            if(tcr_id == 0) {
            	stmt.setLong(index++, tcr_id);
            }
            
            ResultSet rs = stmt.executeQuery();
            Vector objVec = new Vector();
            dbObjective obj = null;
            while (rs.next()) {
                obj = new dbObjective();
                obj.obj_id = rs.getLong("obj_id");
                obj.obj_type = rs.getString("obj_type");
                obj.obj_desc = rs.getString("obj_desc");
                obj.obj_tcr_id = rs.getLong("obj_tcr_id");
                objVec.addElement(obj);
            }
            stmt.close();
            if(choice == null){
				xmlBuf.append(dbObjective.getObjNodeAsXML(con, objVec, res_lan, types, subtypes, prof,null, tc_enabled, cwPage));
            }
            /*
            if(prof.current_role.startsWith(AcObjective.ADM)){
            	xmlBuf.append(dbObjective.getObjNodeAsXML(con, objVec, res_lan, types, prof,null));
            }
            /*if(acObj.hasObjPrivilege(prof.usr_ent_id,prof.current_role,AcObjective.FTN_OBJ_ADMIN)){
			    xmlBuf.append(dbObjective.getObjNodeAsXML(con, objVec, res_lan, types, prof,null));
            }*/
            else{
              //xmlBuf.append("<folders>").append(choice).append("</folders>");
              Map access = acObj.getTopAccess(objVec,prof.usr_ent_id);
              //Map access = new HashMap();
			  xmlBuf.append(dbObjective.getObjNodeAsXML(con, objVec, res_lan, types, subtypes, prof,access, tc_enabled, cwPage));
            }
            
            xmlBuf.append("</body>").append(dbUtils.NEWL);
            
            if(!isTcIndependent){//二级培训中心独立，不用查出共享文件夹
            	xmlBuf.append(dbObjective.getSharedObjAsXML(con, prof, res_lan, types, subtypes, syl.syl_id));
            }
            xmlBuf.append("<isTcIndependent>"+isTcIndependent+"</isTcIndependent>");
            
			if(true && show_all) {
				cwPage.totalRec = objVec.size();
				cwPage.totalPage = cwPage.totalRec/cwPage.pageSize;
				cwPage.ts = cwSQL.getTime(con);
				xmlBuf.append(cwPage.asXML());					
			}else{
				//不显示全部时，显示出总记录数
				if(null == cwPage){
					cwPage = new cwPagination();
				}
				cwPage.curPage = 1;
  			    cwPage.pageSize = objVec.size();
                cwPage.totalRec = objVec.size();
				cwPage.totalPage = 1;
				xmlBuf.append(cwPage.asXML());				
			}
            xmlBuf.append("</syllabus>").append(dbUtils.NEWL);
        }
		if(tc_enabled) {
			xmlBuf.append("<training_center_list>").append("").append("</training_center_list>");	
		}
        xmlBuf.append("</objective_list>").append(dbUtils.NEWL);
        
        return xmlBuf.toString();
    }
    
	public static String getMyRootSybObj(Connection con,  String privilege, String res_lan, String[] types, String[] subtypes, loginProfile prof,String choice, boolean show_all, boolean tc_enabled, long tcr_id, cwPagination cwPage)
			throws SQLException, qdbException, cwSysMessage, cwException, qdbErrMessage
		{
			Vector sylVec = dbSyllabus.getSybVec(con, privilege, prof.root_ent_id);
			AcObjective acObj = new AcObjective(con);
			StringBuffer xmlBuf = new StringBuffer();
			StringBuffer sqlBuf = new StringBuffer();

			xmlBuf.append(dbUtils.xmlHeader);
			xmlBuf.append("<objective_list>").append(dbUtils.NEWL);
			//xmlBuf.append("<folders>").append(choice).append("</folders>");
			xmlBuf.append("<meta>");
			xmlBuf.append(prof.asXML());
			xmlBuf.append("<tc_enabled>").append(tc_enabled).append("</tc_enabled>");
	        cwTree tree = new cwTree();
	        xmlBuf.append(tree.genNavTrainingCenterTree(con, prof, false));
			xmlBuf.append("</meta>");
	        if(tc_enabled) {
	        	if(!show_all && tcr_id == 0) {
	        		tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
	        	}
	            DbTrainingCenter objTc = DbTrainingCenter.getInstance(con, tcr_id);
	            if(!show_all && tcr_id !=0) {
		            AcTrainingCenter actc = new AcTrainingCenter(con);
	        		boolean isSa = AccessControlWZB.isSysAdminRole(prof.current_role);
	        		boolean isInstr = AccessControlWZB.isIstRole(prof.current_role);
	        		if(isSa || isInstr) {
	        			
	        		} else {
	        			if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, tcr_id)) {
	        				throw new qdbErrMessage(TrainingCenter.NO_PERMISSION_MSG);
	        			}            	
	        		}
	            }

	            if(objTc != null) {
	                xmlBuf.append("<training_center id =\"").append(objTc.getTcr_id()).append("\">");
	                xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
	                xmlBuf.append("</training_center>");        	
	            }
	        }   
			xmlBuf.append("<folders>").append(choice).append("</folders>");
			dbSyllabus syl = null;
			for (int i=0;i<sylVec.size();i++) {
				syl = (dbSyllabus) sylVec.elementAt(i);
				xmlBuf.append("<syllabus id=\"").append(syl.syl_id)
					  .append("\" privilege=\"").append(syl.syl_privilege)
					  .append("\" locale=\"").append(syl.syl_locale)
					  .append("\" root_ent_id=\"").append(syl.syl_ent_id_root)
					  .append("\">").append(dbUtils.NEWL);
				xmlBuf.append("<desc>").append(dbUtils.esc4XML(syl.syl_desc)).append("</desc>").append(dbUtils.NEWL);
				xmlBuf.append("<show_all>").append(show_all).append("</show_all>");	
				xmlBuf.append("<body>").append(dbUtils.NEWL);
				
				StringBuffer order_by = new StringBuffer();
				if(show_all == false) {
					cwPage = null;
				}
				if(tc_enabled && show_all) {
					if(cwPage.sortCol == null){
						cwPage.sortCol = DEFAULT_COL_SORT_1;
					}
					if(cwPage.sortOrder == null) {
						cwPage.sortOrder = DEFAULT_COL_ORDER;
					}
					if(cwPage.curPage == 0) {
						cwPage.curPage = 1;
					}
					if(cwPage.pageSize == 0) {
						cwPage.pageSize = 10;
					}
					order_by.append(cwPage.sortCol).append(" ").append(cwPage.sortOrder);
				} else {
					order_by.append(DEFAULT_COL_SORT_2).append(" ").append(DEFAULT_COL_ORDER)
					        .append(" , ").append(DEFAULT_COL_SORT_1).append(" ").append(DEFAULT_COL_ORDER);
				}
				Map myFolders = null;
				if(tc_enabled) {
					if(show_all) {
						tcr_id = 0;
					}
					if(prof.current_role.startsWith(AccessControlWZB.ROL_STE_UID_TADM)) {
						myFolders = acObj.getMyRootFoleders(prof.usr_ent_id,syl.syl_id, tc_enabled, tcr_id, order_by.toString());	
					} else if(prof.current_role.equalsIgnoreCase(AcObjective.INSTR)) {
						myFolders = acObj.getInstrRootFoleders(prof.usr_ent_id, syl.syl_id,tc_enabled, tcr_id, order_by.toString());
					}					
				} else {
					myFolders = acObj.getMyRootFoleders(prof.usr_ent_id,syl.syl_id, tc_enabled,tcr_id, order_by.toString());
				}

			    
				Vector objVec = (Vector)myFolders.get("objVec");
				/*if(prof.current_role.startsWith("ADM")){
					xmlBuf.append(dbObjective.getObjNodeAsXML(con, objVec, res_lan, types, prof,new HashMap()));
				}
				else{
				  Map access = acObj.getTopAccess(objVec,prof.usr_ent_id);
				  //Map access = new HashMap();*/
				//xmlBuf.append("<folders>").append(choice).append("</folders>");
				xmlBuf.append(dbObjective.getObjNodeAsXML(con, objVec, res_lan, types, subtypes, prof, myFolders,tc_enabled, cwPage));
				xmlBuf.append("</body>").append(dbUtils.NEWL);
				xmlBuf.append(dbObjective.getSharedObjAsXML(con, prof, res_lan, types, subtypes, syl.syl_id));
				if(tc_enabled && show_all) {
					cwPage.totalRec = objVec.size();
					cwPage.totalPage = cwPage.totalRec/cwPage.pageSize;
					cwPage.ts = cwSQL.getTime(con);
					xmlBuf.append(cwPage.asXML());					
				}
				xmlBuf.append("</syllabus>").append(dbUtils.NEWL);
            
			}
			if(tc_enabled) {
				xmlBuf.append("<training_center_list>").append("").append("</training_center_list>");	
			}
			xmlBuf.append("</objective_list>").append(dbUtils.NEWL);
        
			return xmlBuf.toString();
		}

    /*
    Get the xml of a vector objective, count the number of resources that the objective has.
    */
    private static String getObjNodeAsXML(Connection con, Vector objVec, String res_lan, String[] types, String[] subtypes, loginProfile profile,Map access, boolean tc_enabled, cwPagination page) 
        throws qdbException
    {
    	AcObjective acObj = new AcObjective(con);
        StringBuffer xmlBuf = new StringBuffer();
        if (objVec == null || objVec.size()==0) {
            return xmlBuf.toString();
        }

        dbObjective obj = null;
        Vector idVec = new Vector();
        for (int i=0;i<objVec.size();i++) {
            obj = (dbObjective) objVec.elementAt(i);
            idVec.addElement(new Long(obj.obj_id));
        }
        
        Hashtable countHash = dbResourceObjective.getResourceCount(con, idVec, res_lan, types, subtypes, dbResource.RES_PRIV_CW, profile.usr_id, null);
        Hashtable allCountHash = dbResourceObjective.getAllResourceCount(con, idVec, res_lan, types, subtypes, dbResource.RES_PRIV_CW, profile.usr_id);
        Hashtable ownCountHash = dbResourceObjective.getResourceCount(con, idVec, res_lan, types, subtypes, dbResource.RES_PRIV_AUTHOR, profile.usr_id, null);
        Hashtable objCountHash = dbObjective.getChildObjCount(con, idVec);
        
        Long Count_ = null;
        /*if(!topLevel){
        	xmlBuf.append("<access>").append("owner").append("</access>");
        }*/
        /*top level folder*/
        int init = 0;
        int size = 0;
        if(tc_enabled && page != null ){
        	init = (page.curPage-1)*page.pageSize;
        	if(objVec.size() > page.curPage*page.pageSize) {
        		size = page.curPage*page.pageSize;        		
        	} else {
        		size = objVec.size();
        	}
        } else {
        	init = 0;
        	size = objVec.size();
        }
        if(access != null){
          for (int j=init; j<size;j++) {
            obj = (dbObjective) objVec.elementAt(j);
            xmlBuf.append("<node id=\"").append(obj.obj_id)
                    .append("\" type=\"").append(obj.obj_type)
                    .append("\" count=\"");
            Count_ = (Long) countHash.get(new Long(obj.obj_id));
            if (Count_ == null) {
                Count_ = new Long(0);
            }
            xmlBuf.append(Count_.longValue());
            xmlBuf.append("\" own_count=\"");
            Count_ = (Long) ownCountHash.get(new Long(obj.obj_id));
            if (Count_ == null) {
                Count_ = new Long(0);
            }
            xmlBuf.append(Count_.longValue());
            xmlBuf.append("\" obj_count=\"");
            Count_ = (Long) objCountHash.get(new Long(obj.obj_id));
            if (Count_ == null) {
                Count_ = new Long(0);
            }
            xmlBuf.append(Count_.longValue());
            xmlBuf.append("\" all_res_count=\"");
            Count_ = (Long) allCountHash.get(new Long(obj.obj_id));
            if (Count_ == null) {
                Count_ = new Long(0);
            }
            xmlBuf.append(Count_.longValue());   
            /*
            if(profile.current_role.startsWith(AcObjective.adm)){
            	xmlBuf.append("\" access=\"").append(AcObjective.owner);
            }
            */
			if(access == null){
				xmlBuf.append("\" access=\"").append(AcObjective.OWNER);
			  }
            else{
               xmlBuf.append("\" access=\"").append(access.get(new Long(obj.obj_id)).toString());   
			   //xmlBuf.append("\" access=\"").append("owner");
            }     
            xmlBuf.append("\">");
            if(tc_enabled) {
            	if(obj.obj_tcr_id != 0 ) {
            		DbTrainingCenter tc = DbTrainingCenter.getInstance(con, obj.obj_tcr_id);
            		String tc_title = null;
            		if(tc != null) {
            			tc_title = tc.getTcr_title();
            		}
            		xmlBuf.append("<training_center id=\"").append(obj.obj_tcr_id).append("\" >")
            		.append(cwUtils.esc4XML(cwUtils.escNull(tc_title))).append("</training_center>");
            	}
            }
            xmlBuf.append("<obj_desc>").append(dbUtils.esc4XML(cwUtils.escNull(obj.obj_desc))).append("</obj_desc>");
			xmlBuf.append("</node>").append(dbUtils.NEWL);
          }
        }
        else{
        	//not the top level
			for (int j=init;j<size;j++) {
				obj = (dbObjective) objVec.elementAt(j);
				xmlBuf.append("<node id=\"").append(obj.obj_id)
						.append("\" type=\"").append(obj.obj_type)
						.append("\" count=\"");
				Count_ = (Long) countHash.get(new Long(obj.obj_id));
				if (Count_ == null) {
					Count_ = new Long(0);
				}
				xmlBuf.append(Count_.longValue());
				xmlBuf.append("\" own_count=\"");
				Count_ = (Long) ownCountHash.get(new Long(obj.obj_id));
				if (Count_ == null) {
					Count_ = new Long(0);
				}
				xmlBuf.append(Count_.longValue());
				xmlBuf.append("\" obj_count=\"");
				Count_ = (Long) objCountHash.get(new Long(obj.obj_id));
				if (Count_ == null) {
					Count_ = new Long(0);
				}
				xmlBuf.append(Count_.longValue());
				xmlBuf.append("\" all_res_count=\"");
				Count_ = (Long) allCountHash.get(new Long(obj.obj_id));
				if (Count_ == null) {
					Count_ = new Long(0);
				}
				xmlBuf.append(Count_.longValue());       
				xmlBuf.append("\">");
	            if(tc_enabled) {
	            	if(obj.obj_tcr_id != 0){
	            		DbTrainingCenter tc = DbTrainingCenter.getInstance(con, obj.obj_tcr_id);
	            		String tc_title = null;
	            		if(tc != null) {
	            			tc_title = tc.getTcr_title();
	            		}
	            		xmlBuf.append("<training_center id=\"").append(obj.obj_tcr_id).append("\" >")
	            		.append(cwUtils.esc4XML(cwUtils.escNull(tc_title))).append("</training_center>");
	            	}
	            } 
	            xmlBuf.append("<obj_desc>").append(dbUtils.esc4XML(cwUtils.escNull(obj.obj_desc))).append("</obj_desc>");
				xmlBuf.append("</node>").append(dbUtils.NEWL);
		  }
        }
        return xmlBuf.toString();
    }

    public static String getChildObjAsXML(Connection con, long obj_id, String res_lan, String[] types, String[] subtypes, loginProfile prof, boolean tc_enabled)
        throws SQLException, qdbException
    {
		AcObjective acObj = new AcObjective(con);
		//DbObjective dObj = new DbObjective();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT  obj_id, obj_desc, obj_type FROM Objective WHERE ")
              .append(" obj_obj_id_parent = ? and obj_status = ?")
              .append(" ORDER by obj_desc ");
             
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
        stmt.setLong(1, obj_id);
        stmt.setString(2,dbObjective.OBJ_STATUS_OK);
        ResultSet rs = stmt.executeQuery();
        
        StringBuffer bodyBuf = new StringBuffer();
        bodyBuf.append("<body>").append(dbUtils.NEWL);
        Vector objVec = new Vector();
        dbObjective obj = null;
        while(rs.next())
        {
            obj = new dbObjective();
            obj.obj_id = rs.getLong("obj_id");
            obj.obj_type = rs.getString("obj_type");
            obj.obj_desc = rs.getString("obj_desc");
            objVec.addElement(obj);
        }
        stmt.close();
		if(prof.current_role.startsWith(AcObjective.ADM)){
			bodyBuf.append("<access>").append(AcObjective.OWNER).append("</access>");
		}
		/*
		if(acObj.hasObjPrivilege(prof.usr_ent_id,prof.current_role,AcObjective.FTN_OBJ_ADMIN)){
			bodyBuf.append("<access>").append(AcObjective.owner).append("</access>");
		}
		*/
	    else{
		  String access = "";
		  if(dbObjective.isRootObj(con,obj_id)){
			 access = acObj.getObjectiveAccess(obj_id,prof.usr_ent_id,true);
		  }else{
		     access = acObj.getObjectiveAccess(obj_id,prof.usr_ent_id,false);
		  }
		  //bodyBuf.append("<folders>").append(choice).append("</folders>");
          bodyBuf.append("<access>").append(access).append("</access>");
		  //bodyBuf.append("<access>").append("owner").append("</access>");
		}
        bodyBuf.append(dbObjective.getObjNodeAsXML(con, objVec, res_lan, types, subtypes, prof,null,tc_enabled, null));
        bodyBuf.append("</body>").append(dbUtils.NEWL);

        return bodyBuf.toString();
    }
    
    public static String getObjLstAsXML(Connection con, loginProfile prof, long objId, long sylId, String res_lan, String[] types, String[] subtypes, boolean bShowChildXML,String choice,String curr_stylesheet,boolean show_all, boolean tc_enabled, boolean share_mode) throws qdbException, SQLException{
    	return getObjLstAsXML(con, prof, objId, sylId, res_lan, types, subtypes, bShowChildXML, choice, curr_stylesheet, show_all, tc_enabled, share_mode, null);
    }
    
    public static String getObjLstAsXML(Connection con, loginProfile prof, long objId, long sylId, String res_lan, String[] types, String[] subtypes, boolean bShowChildXML,String choice,String curr_stylesheet,boolean show_all, boolean tc_enabled, boolean share_mode,dbObjective dbobj )
        throws qdbException, SQLException
    {
		AcPageVariant acPageVariant = new AcPageVariant(con);
		AcObjective acObj = new AcObjective(con);
        dbObjective obj = new dbObjective();
        Hashtable xslQuestions=AcXslQuestion.getQuestions();
        obj.obj_id = objId;
        long resCount = 0;
        long allResCount = 0;
        long ownCount = 0;
        long objCount = 0;
        acPageVariant.ent_id = prof.usr_ent_id;
        acPageVariant.obj_id = objId;
        acPageVariant.rol_ext_id= prof.current_role;
		//acPageVariant.admAccess = acObj.hasAdminPrivilege(prof.usr_ent_id,prof.current_role);
        if (obj.obj_id > 0) {
            obj.get(con);
            Vector idVec = new Vector();
            idVec.addElement(new Long(obj.obj_id));
            Hashtable countHash = dbResourceObjective.getResourceCount(con, idVec, res_lan, types, subtypes, dbResource.RES_PRIV_CW, prof.usr_id, null);
            Hashtable allCountHash = dbResourceObjective.getAllResourceCount(con, idVec, res_lan, types, subtypes, dbResource.RES_PRIV_CW, prof.usr_id);
            Hashtable ownCountHash = dbResourceObjective.getResourceCount(con, idVec, res_lan, types, subtypes, dbResource.RES_PRIV_AUTHOR, prof.usr_id, null);
            Hashtable objCountHash = dbObjective.getChildObjCount(con, idVec);

            Long tCount = (Long) countHash.get(new Long(obj.obj_id));
            if (tCount != null) {
                resCount = tCount.longValue();
            }
            tCount = (Long) allCountHash.get(new Long(obj.obj_id));
            if (tCount != null) {
                allResCount = tCount.longValue();
            }
            tCount = (Long) ownCountHash.get(new Long(obj.obj_id));
            if (tCount != null) {
                ownCount = tCount.longValue();
            }
            tCount = (Long) objCountHash.get(new Long(obj.obj_id));
            if (tCount != null) {
                objCount = tCount.longValue();
            }
            
        }

        dbSyllabus syl = new dbSyllabus();
        if (obj.obj_syl_id > 0) {
            syl.syl_id = obj.obj_syl_id;    
        }else {
            syl.syl_id = sylId;
        }
        syl.get(con);


        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(dbUtils.xmlHeader);
        xmlBuf.append("<objective_list>").append(dbUtils.NEWL);
		//xmlBuf.append("<folders>").append(choice).append("</folders>");
        xmlBuf.append("<meta>");
        xmlBuf.append(prof.asXML());
        xmlBuf.append("<tc_enabled>").append(tc_enabled).append("</tc_enabled>");
        xmlBuf.append("<show_all>").append(show_all).append("</show_all>");
//        xmlBuf.append("<share_mode>").append(share_mode).append("</share_mode>");
        xmlBuf.append("<nlrn_cm_center_view>").append(true).append("</nlrn_cm_center_view>");
        if(tc_enabled) {
        	long obj_tcr_id = 0;
        	if(dbobj != null && dbobj.obj_tcr_id > 0){
        		obj_tcr_id = dbobj.obj_tcr_id;
        	}else{
        		obj_tcr_id = getObjTcrId(con, obj.obj_id);
        	}
        	 xmlBuf.append("<root_obj_tcr_id>").append(obj_tcr_id).append("</root_obj_tcr_id>");
        }
        xmlBuf.append("</meta>");
        //add the page variant
		String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(curr_stylesheet));
		xmlBuf.append(metaXML);
		xmlBuf.append("<share_mode>").append(share_mode).append("</share_mode>");
		xmlBuf.append("<folders>").append(choice).append("</folders>");
		xmlBuf.append("<role>");
        Vector role_vec = DbAcRole.getNormalRoleList(con, prof.root_ent_id);
        for(int i=0; i<role_vec.size(); i++){
        	xmlBuf.append("~");
        	xmlBuf.append(role_vec.get(i));
        }
        xmlBuf.append("</role>").append(dbUtils.NEWL);
        xmlBuf.append("<objective id=\"").append(obj.obj_id).append("\" type=\"")
                .append(obj.obj_type)
                .append("\" count=\"").append(resCount)
                .append("\" own_count=\"").append(ownCount)
                .append("\" obj_count=\"").append(objCount)
                .append("\" all_res_count=\"").append(allResCount)
                .append("\">").append(dbUtils.NEWL);

        xmlBuf.append("<desc>").append(dbUtils.esc4XML(obj.obj_desc)).append("</desc>").append(dbUtils.NEWL);
		xmlBuf.append("<shared>").append(obj.obj_share_ind).append("</shared>").append(dbUtils.NEWL);

        xmlBuf.append("<syllabus id=\"").append(syl.syl_id)
                  .append("\" privilege=\"").append(syl.syl_privilege)
                  .append("\" locale=\"").append(syl.syl_locale)
                  .append("\" root_ent_id=\"").append(syl.syl_ent_id_root)
                  .append("\">").append(dbUtils.NEWL);
        xmlBuf.append("<desc>").append(dbUtils.esc4XML(syl.syl_desc)).append("</desc>").append(dbUtils.NEWL)
              .append("</syllabus>").append(dbUtils.NEWL);
        

        if (obj.obj_id > 0) {
            xmlBuf.append(obj.getObjPathAsXML1(con,choice));
        }
        
        // Get the immediate child objectives
         if(bShowChildXML)
          {
             String bodyXml = dbObjective.getChildObjAsXML(con, objId, res_lan, types, subtypes, prof, tc_enabled);
             xmlBuf.append(bodyXml);
          }
        
		if(obj.obj_id == 0){
					xmlBuf.append("<access_control>");
					xmlBuf.append("<owner>");
					xmlBuf.append("<entity id=\"").append(prof.usr_ent_id)
						  .append("\" display_bil=\"").append(cwUtils.esc4XML(prof.usr_display_bil)).append("\"/>");
					xmlBuf.append("</owner>");
					xmlBuf.append("</access_control>");
				}

        xmlBuf.append("</objective>");
        if(tc_enabled) {
        	long tcr_id = 0;
        	if(dbobj != null && dbobj.obj_tcr_id > 0){
        		tcr_id = dbobj.obj_tcr_id;
        	}else{
        		tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
        	}
            DbTrainingCenter objTc = DbTrainingCenter.getInstance(con, tcr_id);
            if(objTc != null) {
                xmlBuf.append("<default_training_center id =\"").append(objTc.getTcr_id()).append("\">");
                xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
                xmlBuf.append("</default_training_center>");        	
            }
        }
        xmlBuf.append("</objective_list>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }
    public static long getObjTcrId(Connection con, long obj_id) throws SQLException, qdbException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ").append(cwSQL.replaceNull("child.obj_tcr_id","ancestor.obj_tcr_id")).append(" as obj_tcr_id ").append("from Objective child ")
		   .append(" left join Objective ancestor on ( ")
		   .append(cwSQL.subFieldLocation("child.obj_ancester", "ancestor.obj_id", true))
		   .append(" and  ancestor.obj_tcr_id is not null) ")
		   .append(" where child.obj_id =? ");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setLong(1, obj_id);
		ResultSet rs = stmt.executeQuery();
		long obj_tcr_id = 0;
		while(rs.next()) {
			obj_tcr_id = rs.getLong("obj_tcr_id");
		}
    	stmt.close();
    	return obj_tcr_id;
    }
    
    /*
     * 查看资源是否共享
     */
    public static int getObjIsShare(Connection con, long obj_id) throws SQLException, qdbException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select o.obj_share_ind from Objective o where o.obj_id = ?  ");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setLong(1, obj_id);
		ResultSet rs = stmt.executeQuery();
		int share = 0;
		while(rs.next()) {
			share = rs.getInt("obj_share_ind");
		}
    	stmt.close();
    	return share;
    }
    /*
    Get the path xml of the objective
    */
    
	public String getObjPathAsXML(Connection con)
		   throws SQLException
	   {
		   StringBuffer pathXml = new StringBuffer();
		   Hashtable pathHash = new Hashtable();
        
		   pathXml.append("<path>").append(dbUtils.NEWL);

		   if (obj_ancester != null && obj_ancester.length() > 0) {
			   Vector objVec = convert2vec(obj_ancester);
			   StringBuffer sqlBuf = new StringBuffer();
			   sqlBuf.append(" SELECT obj_id, obj_desc FROM ")
				   .append(" Objective WHERE ")
				   .append(" obj_id IN (").append(obj_ancester).append(") ")
				   .append(" AND obj_type = ? ");
                 
			   PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
			   stmt.setString(1, OBJ_TYPE_SYB);
			   ResultSet rs = stmt.executeQuery();
            
			   long i_id = 0;
			   String i_desc = null;
			   while(rs.next())
			   {
				   i_id = rs.getLong("obj_Id");
				   i_desc = dbUtils.esc4XML(rs.getString("obj_desc"));
				   StringBuffer nodeXml = new StringBuffer();

				   nodeXml.append("<node id=\"").append(i_id).append("\" type=\"")
						  .append(OBJ_TYPE_SYB).append("\">").append(i_desc)
						  .append("</node>").append(dbUtils.NEWL);
				   pathHash.put(new Long(i_id), nodeXml);                       
			   }
			   stmt.close();

			   for (int i=0;i<objVec.size();i++) {
				   Long objID_ = (Long) objVec.elementAt(i);
				   StringBuffer nodeXml = (StringBuffer) pathHash.get(objID_);
				   if (nodeXml != null) {
					   pathXml.append(nodeXml);
				   }
			   }
		   }
		   pathXml.append("</path>").append(dbUtils.NEWL);
		   return pathXml.toString();
        
	   }
	   
    public String getObjPathAsXML1(Connection con,String choice)
        throws SQLException
    {
        StringBuffer pathXml = new StringBuffer();
        Hashtable pathHash = new Hashtable();
        
        pathXml.append("<path>").append(dbUtils.NEWL);
		//pathXml.append("<folders>").append(choice).append("</folders>");

        if (obj_ancester != null && obj_ancester.length() > 0) {
            Vector objVec = convert2vec(obj_ancester);
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append(" SELECT obj_id, obj_desc FROM ")
                .append(" Objective WHERE ")
                .append(" obj_id IN (").append(obj_ancester).append(") ")
                .append(" AND obj_type = ? ");
                 
            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
            stmt.setString(1, OBJ_TYPE_SYB);
            ResultSet rs = stmt.executeQuery();
            
            long i_id = 0;
            String i_desc = null;
            while(rs.next())
            {
                i_id = rs.getLong("obj_Id");
                i_desc = dbUtils.esc4XML(rs.getString("obj_desc"));
                StringBuffer nodeXml = new StringBuffer();

                nodeXml.append("<node id=\"").append(i_id).append("\" type=\"")
                       .append(OBJ_TYPE_SYB).append("\">").append(i_desc)
                       .append("</node>").append(dbUtils.NEWL);
                pathHash.put(new Long(i_id), nodeXml);                       
            }
            stmt.close();

            for (int i=0;i<objVec.size();i++) {
                Long objID_ = (Long) objVec.elementAt(i);
                StringBuffer nodeXml = (StringBuffer) pathHash.get(objID_);
                if (nodeXml != null) {
                    pathXml.append(nodeXml);
                }
            }
        }
        pathXml.append("</path>").append(dbUtils.NEWL);
        return pathXml.toString();
        
    }
    /*
    public String getObjectiveAccess(Connection con,long objId,long entId ,int flag) throws SQLException{
    	String access = "";
    	if(flag == 0)
		 access = getRootObjAccess(con,objId,entId);
    	else{
    	   long rootId = getRootObjId (con,objId);
    	   access = getRootObjAccess(con,rootId,entId);
    	}
    	return access;
    }

    public String getResAccess(Connection con,long resId,long entId) throws SQLException{
    	String access = "";
    	long rootObjId = this.getResObjRootId(con,resId);
    	access = this.getRootObjAccess(con,rootObjId,entId);
    	return access;
    }
    
    protected String getRootObjAccess(Connection con,long rootId,long entId) throws SQLException{
    	StringBuffer sql = new StringBuffer();
    	sql.append("select * from objectiveAccess where oac_obj_id = ? and oac_ent_id = ?");
		PreparedStatement pst = con.prepareStatement(sql.toString());
		ResultSet rs =null;
		String access = "";
    	try{
		 pst.setLong(1,rootId);
		 pst.setLong(2,entId);
		 
		 rs = pst.executeQuery();
		 if(rs.next())
		    access = rs.getString("oac_access_type");
    	}catch(Exception e){
    		throw new SQLException(e.getMessage());
    	}finally{
    		if(rs != null){
    			try{
    				rs.close();
    			}catch(SQLException sqle){
    				;
    			}
    		}
			if(pst != null){
				try{
					pst.close();
				}catch(SQLException sqle){
								;
				}
			}
    	}
		
    	return access;
    }  
    
    public long getRootObjId(Connection con,long objId) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from objective where obj_id = ? ");
		PreparedStatement pst = con.prepareStatement(sql.toString());
		ResultSet rs =null;
		String ancester = "";
		long rootId;
		try{
			pst = con.prepareStatement(sql.toString());
			pst.setLong(1,objId);
			rs = pst.executeQuery();
			if(rs.next())
			   ancester = rs.getString("obj_ancester");
			Vector v = this.convert2vec(ancester);
			rootId = ((Long)v.get(0)).longValue();
		}catch(Exception e){
		  throw new SQLException(e.getMessage());
	    }finally{
		  if(rs != null){
		 try{
			 rs.close();
		 }catch(SQLException sqle){
			 ;
		    }
	     }
		 if(pst != null){
		 try{
			 pst.close();
		 }catch(SQLException sqle){
			 ;
		 }
	        }
       }
       return rootId;
    }
    
    public long getResObjRootId(Connection con,long resId) throws SQLException{
		long objId =0;
		long objPid =0;
		StringBuffer sql = new StringBuffer();
		sql.append("select ob.obj_id,ob.obj_obj_id_parent from resourceObjective rob,objective ob ")
		   .append("where rob_res_id = ? and rob_obj_id = obj_id");
		PreparedStatement pst = con.prepareStatement(sql.toString());
		ResultSet rs =null;
		try{
			pst = con.prepareStatement(sql.toString());
			pst.setLong(1,resId);
			rs = pst.executeQuery();
			if(rs.next()){
				objId = rs.getLong("obj_id");
				objPid = rs.getLong("obj_obj_id_parent");
			}
			if(objPid == 0){
				objId = this.getRootObjId(con,objId);
			}	  
		}catch(Exception e){
		   throw new SQLException(e.getMessage());
	     }finally{
		     if(rs != null){
			try{
				rs.close();
			}catch(SQLException sqle){
				;
			}
		 }
	    	if(pst != null){
			try{
				pst.close();
			}catch(SQLException sqle){
				;
			}
		}
	  }
    	return objId;
    }  */
    
    

    /*
    Convert the ancester list to a vecotr
    The ancester list is in the format " 23 , 34 , 56 "
    Must have space before and after the id for pattern matching
    */
    public static Vector convert2vec(String ancester)
    {
        Vector idVec = new Vector();
        if (ancester == null) 
            return idVec;
        int open_pos = 0;
        int close_pos = 0;
        String id_str = new String();
        while (ancester.indexOf(ID_SEPARATOR_OPEN) >= 0){
            open_pos = ancester.indexOf(ID_SEPARATOR_OPEN);
            close_pos = ancester.indexOf(ID_SEPARATOR_CLOSE, open_pos + 1);
            id_str = ancester.substring(open_pos + 1, close_pos);
            idVec.addElement(new Long(id_str.trim()));
            if (close_pos < ancester.length()) {
                ancester = ancester.substring(close_pos+1, ancester.length());
            }else {
                // finished
                ancester = new String();
            }
        }
        
        return idVec;
    }

    /*
    Get the ancester value of a list of objectives
    */
    public static Hashtable getObjAncester(Connection con, Vector objVec)
        throws SQLException
    {
        Hashtable objHash = new Hashtable();
        if (objVec == null || objVec.size() ==0) {
            return objHash;
        }
        
        String obj_lst = cwUtils.vector2list(objVec);

        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT obj_id, obj_ancester FROM ")
            .append(" Objective WHERE ")
            .append(" obj_id IN ").append(obj_lst);
                 
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
        ResultSet rs = stmt.executeQuery();
            
        while(rs.next())
        {
            Long objId = new Long(rs.getLong("obj_id"));
            String objAncester = rs.getString("obj_ancester");
            
            if (objAncester == null)
                objAncester = new String();
            
            objHash.put(objId, objAncester);
        }
        stmt.close();
        return objHash;
    }

    /*
    Get the ancester value of a list of objectives
    */
    public static String getObjAncester(Connection con, long objId)
        throws SQLException
    {
        
        Vector objVec = new Vector();
        Long OBJID = new Long(objId);
        objVec.addElement(OBJID);
        Hashtable objHash = dbObjective.getObjAncester(con, objVec);
        
        String ancester = (String) objHash.get(OBJID);
        return ancester;
    }

    /*
    Get the list of chidl objective
    */
    public Vector getChildsObjId(Connection con) throws SQLException
    {
        Vector objVec = new Vector();
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT obj_id FROM Objective WHERE obj_ancester  like ? ");
                 
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
    
        stmt.setString(1, "%" + ID_SEPARATOR_OPEN +  obj_id + ID_SEPARATOR_CLOSE + "%");
        ResultSet rs = stmt.executeQuery();
        while (rs.next() ) {
            objVec.addElement(new Long(rs.getLong("obj_id")));
        }
        stmt.close();
        return objVec;

    }

	public void updateObjectiveAccess(Connection con,loginProfile prof, boolean tc_enabled) throws SQLException, qdbErrMessage {
		ViewObjectiveAccess voa = new ViewObjectiveAccess();
		voa.delByObjId(con,this.obj_id);
		insObjectiveAccess(con, prof, tc_enabled); 
	}
	/**
	 * Insert access control records into database (ObjectiveAccess)
	 * @param con Connection to database
	 * @throws SQLException
	 * @throws qdbErrMessage 
	 */
	public void insObjectiveAccess(Connection con, loginProfile prof, boolean tc_enabled) throws SQLException, qdbErrMessage {
		
		//Insert reader records into ObjectiveAccess
		AcTrainingCenter acTc = new AcTrainingCenter(con);
		if(this.reader_ent_id_lst!=null){
			if(tc_enabled) {
				if(AccessControlWZB.isRoleTcInd(prof.current_role)) {
					if(!(this.reader_ent_id_lst.length == 1 && (this.reader_ent_id_lst[0] == 0 || this.reader_ent_id_lst[0]==-1))){
						if(!acTc.hasMgtObjUsrInTc(prof.root_ent_id,  this.reader_ent_id_lst, this.obj_tcr_id)) {
							throw new qdbErrMessage(OBJ_NOT_ADD_MGT_READER);
						}				
					} 
				}
			}
			for(int i=0; i<this.reader_ent_id_lst.length; i++) {
				if(this.reader_ent_id_lst[i] != -1){
					DbObjectiveAccess dboAccess = new DbObjectiveAccess();
					dboAccess.oac_ent_id = this.reader_ent_id_lst[i];
					dboAccess.oac_obj_id = this.obj_id;
					dboAccess.oac_access_type = AcObjective.READER;
					dboAccess.ins(con);
				}
			}
		}
		if(this.author_ent_id_lst!=null){
			if(tc_enabled) {
				if(!AccessControlWZB.isRoleTcInd(prof.current_role)) {
					if(!(this.author_ent_id_lst.length == 1 && (this.author_ent_id_lst[0] == 0 || this.author_ent_id_lst[0] == -1))){
						if(!acTc.hasMgtObjUsrInTc(prof.root_ent_id,  this.author_ent_id_lst, this.obj_tcr_id)) {
							throw new qdbErrMessage(OBJ_NOT_ADD_MGT_AUTHER);
						}				
					}
				}
			}
			for(int i=0; i<this.author_ent_id_lst.length; i++) {
				if(this.author_ent_id_lst[i] != -1){
					DbObjectiveAccess dboAccess = new DbObjectiveAccess();
					dboAccess.oac_ent_id = this.author_ent_id_lst[i];
					dboAccess.oac_obj_id = this.obj_id;
					dboAccess.oac_access_type = AcObjective.AUTHOR;
					dboAccess.ins(con);
				}
			}
		}
		if(this.owner_ent_id_lst!=null){
//			if(tc_enabled) {
//				if(!acTc.hasTcPrivilege(prof.usr_ent_id, prof.current_role, AcTrainingCenter.FTN_TC_MAIN_IN_ORG)) {
//					if(!acTc.hasMgtObjUsrInTc(prof.root_ent_id,  this.owner_ent_id_lst, this.obj_tcr_id)) {
//						throw new qdbErrMessage(OBJ_NOT_ADD_MGT_OWNER);
//					}				
//				}
//			}
			for(int i=0; i<this.owner_ent_id_lst.length; i++) {
				DbObjectiveAccess dboAccess = new DbObjectiveAccess();
				dboAccess.oac_ent_id = this.owner_ent_id_lst[i];
				dboAccess.oac_obj_id = this.obj_id;
				dboAccess.oac_access_type = AcObjective.OWNER;
				dboAccess.ins(con);
			}
		}
		//whether learner can view the resources in current directory
		if(this.lrn_can_view_ind ) {
			DbObjectiveAccess dboAccess = new DbObjectiveAccess();
			dboAccess.oac_ent_id = 0;
			dboAccess.oac_obj_id = this.obj_id;
			dboAccess.oac_access_type = AcObjective.LRN_VIEW;
			dboAccess.ins(con);
		}
	}
	
    // NEW : insert objective with obj_ancester
    public void ins (Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
        	WizbiniLoader wizbini = qdbAction.getWizbini();
            //if (!hasPermssion
            PreparedStatement stmt = con.prepareStatement(
                    " INSERT INTO Objective "
                    + " (obj_id, "
                    + " obj_syl_id, "
                    + " obj_type, "
                    + " obj_desc, "
                    + " obj_obj_id_parent, "
					+ " obj_ancester, "
					+ " obj_status, "
					+ " obj_tcr_id,obj_share_ind ) "
					+ " VALUES (?,?,?,?,?, ?,?,?,?) "); 

            int index = 0;
            obj_id = dbObjective.getMaxId(con) + 1;
            stmt.setLong(++index, obj_id);
            stmt.setLong(++index, obj_syl_id);
            stmt.setString(++index, OBJ_TYPE_SYB);
            stmt.setString(++index, obj_desc);
            if (obj_obj_id_parent == 0 ) {
                stmt.setNull(++index, java.sql.Types.INTEGER);
                stmt.setNull(++index, java.sql.Types.VARCHAR);
            }else {
                stmt.setLong(++index, obj_obj_id_parent);
                obj_ancester = dbObjective.getObjAncester(con, obj_obj_id_parent);
                if (obj_ancester == null || obj_ancester.length()==0) {
                    obj_ancester = new String();
                }else {
                    obj_ancester += ",";
                }
                
                obj_ancester += ID_SEPARATOR_OPEN + obj_obj_id_parent + ID_SEPARATOR_CLOSE;
                
                stmt.setString(++index, obj_ancester);
            }
			stmt.setString(++index,OBJ_STATUS_OK);
			
			if(obj_obj_id_parent != 0){
                dbObjective parent = new dbObjective();
                parent.obj_id = obj_obj_id_parent;
                parent.get(con);
                obj_tcr_id = parent.obj_tcr_id;
            }else{
                if(!wizbini.cfgTcEnabled) {
                    obj_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
                }
            }

            if(obj_tcr_id == 0){
                stmt.setNull(++index, java.sql.Types.INTEGER);
            }else{
                stmt.setLong(++index, obj_tcr_id);
            }

			
			stmt.setBoolean(++index, obj_share_ind);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback(); 
                throw new qdbException("Failed to insert objective."); 
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        } 
    }
    public void updObjTcrId(Connection con) throws SQLException  {
    	String sql = "Update Objective set obj_tcr_id = ?  where obj_id = ? ";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++, obj_tcr_id);
    	stmt.setLong(index++, obj_id);
    	int stmtResult = stmt.executeUpdate();
    	stmt.close();
    	if(stmtResult !=1) {
    		con.rollback();
    	}
    }
    /* Get the max objctive id */
    public static long getMaxId(Connection con)
        throws SQLException
    {
            PreparedStatement stmt = con.prepareStatement(
                      " SELECT MAX(obj_id) MAX_OBJ FROM Objective " ); 
            
            long max_obj_id = 0; 
            ResultSet rs = stmt.executeQuery(); 
            
            if(rs.next()) {
                max_obj_id = rs.getLong("MAX_OBJ"); 
            }
            stmt.close();
            return max_obj_id;
    }
    
	/*
		Paste an objective to another position
		*/
		public void paste(Connection con, long parent_id)
			throws SQLException, qdbErrMessage
		{
    	
			if(obj_id!=parent_id){
			  /*  if (parent_id != 0 && !dbObjective.sameSyllabus(con, obj_id, parent_id))
					throw new qdbErrMessage(OBJ_DIFFERENT_SYL);
			  */
				Vector childObj = getChildsObjId(con);
            
				if (childObj.contains(new Long(parent_id)))
					throw new qdbErrMessage(OBJ_PASTE_TO_CHILD);

				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append(" UPDATE Objective SET ")
					  .append(" obj_obj_id_parent = ? ")
					  .append(" ,obj_ancester = ? ")
					  .append("  WHERE obj_id = ? "); 

				PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
            
				if (parent_id == 0) {
					stmt.setNull(1, java.sql.Types.INTEGER);
					obj_ancester = null;
				}else {
					stmt.setLong(1,parent_id);
					obj_ancester = dbObjective.getObjAncester(con, parent_id);
					if (obj_ancester == null || obj_ancester.length()==0) {
						obj_ancester = new String();
					}else {
						obj_ancester += ",";
					}
                
					obj_ancester += ID_SEPARATOR_OPEN + parent_id + ID_SEPARATOR_CLOSE;
                
				}
            
				stmt.setString(2, obj_ancester);
				stmt.setLong(3,obj_id);
            
				stmt.executeUpdate();
				stmt.close();
            
				dbObjective.updChildAncester(con, obj_id, obj_ancester);
			}else
				throw new qdbErrMessage(OBJ_PASTE_TO_SAME_OBJ);
            
		}
    
    /*
    Paste an objective to another position
    public void paste(Connection con, long parent_id)
        throws SQLException, qdbErrMessage
    {
            if (parent_id != 0 && !dbObjective.sameSyllabus(con, obj_id, parent_id))
                throw new qdbErrMessage(OBJ_DIFFERENT_SYL);

            Vector childObj = getChildsObjId(con);
            
            if (childObj.contains(new Long(parent_id)))
                throw new qdbErrMessage(OBJ_PASTE_TO_CHILD);

            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append(" UPDATE Objective SET ")
                  .append(" obj_obj_id_parent = ? ")
                  .append(" ,obj_ancester = ? ")
                  .append("  WHERE obj_id = ? "); 

            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
            
            if (parent_id == 0) {
                stmt.setNull(1, java.sql.Types.INTEGER);
                obj_ancester = null;
            }else {
                stmt.setLong(1,parent_id);
                obj_ancester = dbObjective.getObjAncester(con, parent_id);
                if (obj_ancester == null || obj_ancester.length()==0) {
                    obj_ancester = new String();
                }else {
                    obj_ancester += ",";
                }
                
                obj_ancester += ID_SEPARATOR_OPEN + parent_id + ID_SEPARATOR_CLOSE;
                
            }
            
            stmt.setString(2, obj_ancester);
            stmt.setLong(3,obj_id);
            
            stmt.executeUpdate();
            stmt.close();
            
            dbObjective.updChildAncester(con, obj_id, obj_ancester);
            
    }
    */

    public static void updChildAncester(Connection con,long parent_id,String parent_ancester)
        throws SQLException
    {
           Vector immChildVec = dbObjective.getImmediateChild(con, parent_id);
           
           for (int i=0; i<immChildVec.size(); i++) {
                long objId = 0;
                String objAncester = null;
                objId = ((Long) immChildVec.elementAt(i)).longValue();
                if (parent_ancester == null) {
                    objAncester = new String();
                }else {
                    objAncester = parent_ancester + ",";
                }
                objAncester += ID_SEPARATOR_OPEN + parent_id + ID_SEPARATOR_CLOSE;
                updAncester(con, objId, objAncester);
                updChildAncester(con, objId, objAncester);
           }
    }        

    /*
    Get the list of immediate child of the objective (one level behind the current  objective)
    */
    public static Vector getImmediateChild(Connection con, long parent_id)
        throws SQLException
    {
            Vector childVec = new Vector();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append(" SELECT obj_id FROM Objective WHERE ")
                  .append(" obj_obj_id_parent = ? ")
                  .append(" AND obj_type = ? ");
            
            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
            stmt.setLong(1, parent_id);
            stmt.setString(2, OBJ_TYPE_SYB);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                childVec.addElement(new Long(rs.getLong("obj_id")));
            }
            stmt.close();
            
            return childVec;
    }        

    /*
    Update the ancester value of an objectives
    */
    public static void updAncester (Connection con, long objId, String objAncester)
        throws SQLException
    {

        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" UPDATE Objective SET obj_ancester  = ? ")
            .append(" WHERE obj_id = ? ");
                 
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
        stmt.setString(1, objAncester);
        stmt.setLong(2, objId);
        stmt.executeUpdate();
        stmt.close();

    }

    /*
    Update the ancester value of an objectives
    */
    public static boolean isPublicObjective (Connection con, long objId)
        throws SQLException
    {

        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT syl_privilege FROM Syllabus, Objective ")
              .append(" WHERE obj_syl_id = syl_id AND obj_id = ? ");
                 
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
        stmt.setLong(1, objId);
        boolean isPublic = false;
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            if (rs.getString("syl_privilege").equals(dbSyllabus.PUBLIC_PRIV)) {
                isPublic = true;
            }
        }
        stmt.close();
        
        return isPublic;
        
    }

    /*
    Update the ancester value of an objectives
    */
    public static boolean sameSyllabus (Connection con, long objId1, long objId2)
        throws SQLException
    {

        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append(" SELECT OBJ1.obj_syl_id SYL1, OBJ2.obj_syl_id SYL2 FROM")
              .append(" Objective OBJ1, Objective OBJ2 ")
              .append(" WHERE OBJ1.obj_id = ? AND OBJ2.obj_id = ? ");
                 
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString()); 
        stmt.setLong(1, objId1);
        stmt.setLong(2, objId2);
        boolean sameSyl = false;
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            if (rs.getLong("SYL1") == rs.getLong("SYL2")) {
                sameSyl = true;
            }
        }
        stmt.close();
        
        return sameSyl;
        
    }

    public static int getObjLevel(Connection con, long objId) 
        throws qdbException
    {
        try {
            int level=0;
            
            String ancester = dbObjective.getObjAncester(con, objId);
            if (ancester == null || ancester.length() ==0) {
                level = 1;
            }else {
                Vector ancesterVec = convert2vec(ancester);
                level = ancesterVec.size() + 1;
            }
            return level;
        }catch (SQLException e) {
            throw new qdbException (e.getMessage());
        }            
    }

    public String asXML(Connection con, loginProfile prof,String choice, boolean tc_enabled, boolean isTcIndependent)
        throws qdbException, SQLException
    {
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(dbUtils.xmlHeader);
        xmlBuf.append("<objective id=\"").append(obj_id).append("\" syl_id=\"")
              .append(obj_syl_id).append("\" type=\"").append(obj_type)
              .append("\" root_ent_id=\"").append(prof.root_ent_id)
              .append("\" obj_id_parent=\"").append(obj_obj_id_parent)
              .append("\" share_mode=\"").append(share_mode).append("\">")
              .append(this.getObjPathAsXML(con))
              .append(dbUtils.NEWL);
        
        //what the user choose on first page of resource management
        //xmlBuf.append("<folders>").append(choice).append("</folders>");
        // author's information
		xmlBuf.append("<meta>");
		xmlBuf.append(prof.asXML());
		xmlBuf.append("<nlrn_cm_center_view>").append(true).append("</nlrn_cm_center_view>");
		xmlBuf.append("<tc_enabled>").append(tc_enabled).append("</tc_enabled>");
		xmlBuf.append("</meta>");
		xmlBuf.append("<folders>").append(choice).append("</folders>");
        xmlBuf.append("<desc>").append(dbUtils.esc4XML(obj_desc)).append("</desc>").append(dbUtils.NEWL);
        xmlBuf.append("<shared>").append(obj_share_ind).append("</shared>").append(dbUtils.NEWL);
        xmlBuf.append("<role>");
        Vector role_vec = DbAcRole.getNormalRoleList(con, prof.root_ent_id);
        for(int i=0; i<role_vec.size(); i++){
        	xmlBuf.append("~");
        	xmlBuf.append(role_vec.get(i));
        }
        xmlBuf.append("</role>").append(dbUtils.NEWL);
        
		if(obj_obj_id_parent ==0){
			        ViewObjectiveAccess voa = new ViewObjectiveAccess();
					Vector readerVec = voa.getByObjNType(con,obj_id,AcObjective.READER);
					Vector authorVec = voa.getByObjNType(con,obj_id,AcObjective.AUTHOR);
					Vector ownerVec = voa.getByObjNType(con,obj_id,AcObjective.OWNER);
					Vector lrnViewVec = voa.getByObjNType(con,obj_id,AcObjective.LRN_VIEW);
					xmlBuf.append("<access_control>");
					xmlBuf.append("<reader>");
			/*
			 * if select none user
			 * */
			if( readerVec != null){
				for(int i=0;i<readerVec.size();i++) {
					DbObjectiveAccess oac = (DbObjectiveAccess) readerVec.elementAt(i);
					xmlBuf.append("<entity id=\"").append(oac.oac_ent_id).append("\"");
					if(oac.oac_ent_id!=0){
						xmlBuf.append(" display_bil=\"")
							  .append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con,oac.oac_ent_id)))
							  .append("\"");
					}
					xmlBuf.append("/>");
				}

			}
					xmlBuf.append("</reader>");
					xmlBuf.append("<author>");
			if( authorVec != null){
				for(int i=0;i<authorVec.size();i++){
					DbObjectiveAccess oac = (DbObjectiveAccess) authorVec.elementAt(i);
					xmlBuf.append("<entity id=\"").append(oac.oac_ent_id).append("\"");
					if(oac.oac_ent_id!=0){
						xmlBuf.append(" display_bil=\"")
							  .append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con,oac.oac_ent_id)))
							  .append("\"");
					}
					xmlBuf.append("/>");
				}

			}
					xmlBuf.append("</author>");
					xmlBuf.append("<owner>");
					for(int i=0;i<ownerVec.size();i++){
						DbObjectiveAccess oac = (DbObjectiveAccess) ownerVec.elementAt(i);
						xmlBuf.append("<entity id=\"").append(oac.oac_ent_id).append("\"");
						if(oac.oac_ent_id!=0){
							xmlBuf.append(" display_bil=\"")
								  .append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con,oac.oac_ent_id))).append("\"");
						}
						xmlBuf.append("/>");
					}
					xmlBuf.append("</owner>");
					if(tc_enabled){
						xmlBuf.append("<training_center id =\"").append(obj_tcr_id).append("\">");
						String tc_title = null;
						if(obj_tcr_id != 0) {
							DbTrainingCenter dbTc = DbTrainingCenter.getInstance(con, obj_tcr_id);
							tc_title = dbTc.getTcr_title();
						}
						xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(tc_title))).append("</title>");
						xmlBuf.append("</training_center>");
					}
					//whether learner can view the resources in current directory
					if(lrnViewVec.size() > 0) {
						xmlBuf.append("<lrn_view_ind>true</lrn_view_ind>");
					}
					xmlBuf.append("</access_control>");
				}
		xmlBuf.append("<isTcIndependent>").append(isTcIndependent).append("</isTcIndependent>");
        xmlBuf.append("</objective>").append(dbUtils.NEWL);
        return xmlBuf.toString();

    }

    // given an obj id, get a vector of child obj include itself
    public static Vector getSelfAndChildsObjId(Connection con, long objId)
        throws qdbException
    {
        try {
            dbObjective obj = new dbObjective(objId);
            Vector allVec = obj. getChildsObjId(con);
            allVec.addElement(new Long(objId));
            
            return allVec;
            
        } catch(SQLException e) {
               throw new qdbException("SQL Error: " + e.getMessage());
        }   
    }

    // given an obj id, get a list of child obj include itself
    public static String getSelfAndChildsObjIdLst(Connection con, long objId)
        throws qdbException
    {
        Vector allVec = dbObjective.getSelfAndChildsObjId(con, objId);
            
        String idLst = cwUtils.vector2list(allVec);
        return idLst;
            
    }


    // get the child objective count of an objective
    public static Hashtable getChildObjCount(Connection con, Vector idVec)
        throws qdbException
    {   
        try {
            Hashtable objCountHash = new Hashtable();
            if (idVec == null || idVec.size()==0) {
                return objCountHash;
            }
            
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append(" SELECT count(obj_id) OBJCNT ,  obj_obj_id_parent OBJID ")
                  .append(" FROM Objective ")
                  .append(" WHERE obj_obj_id_parent IN ").append(cwUtils.vector2list(idVec)).append(" and obj_status = ?")
                  .append(" GROUP BY obj_obj_id_parent ");

            PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
            stmt.setString(1,OBJ_STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                Long OBJID_ = new Long(rs.getLong("OBJID"));
                Long OBJCNT_ = new Long(rs.getLong("OBJCNT"));
                objCountHash.put(OBJID_, OBJCNT_);
            }
            stmt.close();

            return objCountHash;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
        
    }


    /**
    * Get the description of the objective in the id list
    * @param list of objective id eg. ( 1, 2 ,9 )
    * @return Hashtable, key:objective id, value:objective desc
    */
    public static Hashtable getObjAncesterDesc(Connection con, String obj_id_list)
        throws SQLException, cwException {
            
            String SQL = " SELECT obj_id, obj_desc "
                       + " FROM Objective "
                       + " WHERE obj_id IN " + obj_id_list;
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            Hashtable descTable = new Hashtable();
            while(rs.next())
                descTable.put(new Long(rs.getLong("obj_id")), rs.getString("obj_desc"));
            
            stmt.close();
            return descTable;
        }

    public static String getDesc(Connection con, long obj_id)
        throws SQLException{
            
            String SQL = " SELECT obj_desc From Objective "
                       + " WHERE obj_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, obj_id);
            ResultSet rs = stmt.executeQuery();
            String desc = "";
            if(rs.next())
                desc = rs.getString("obj_desc");
            stmt.close();
            return desc;
        }
 
 
		final static String SQL_GET_CAT_ID = " Select obj_id " +
			"From Objective " +
			"Where obj_type = ? " +
			"And obj_desc = ? ";
	
		/**
		 * Gets catalog id.
		 * @param con database connection
		 * @param catalogTitle catalog title(desc)
		 * @param parentId parent id
		 * @return a catalog id
		 * @throws SQLException
		 */
		public static long[] getCatalogId(
			Connection con,
			String catalogTitle,
			long[] parentId)
			throws SQLException {
			
			long cat_id[] = null;
			String SQL = SQL_GET_CAT_ID;
			if (parentId != null && parentId.length > 0) {
				String id_list = " ( ";
				for (int i = 0; i < parentId.length; i++) {
					id_list += parentId[i];
					if (i < parentId.length - 1) {
						id_list += ", ";
					}
				}
				id_list += " ) ";
				SQL += "And obj_obj_id_parent In " + id_list;
				
			}
			PreparedStatement stmt = con.prepareStatement(SQL);
			stmt.setString(1, dbObjective.OBJ_TYPE_SYB);
			stmt.setString(2, catalogTitle);
			ResultSet rs = stmt.executeQuery();
			Vector v_id = new Vector();
			while(rs.next()) {
				v_id.addElement(new Long(rs.getLong("obj_id")));
			}
			stmt.close();
			cat_id = new long[v_id.size()];
			for(int i=0; i<cat_id.length; i++) {
				cat_id[i] = ((Long) v_id.elementAt(i)).longValue();
			}
			return cat_id;
		}
		
		/*
			 * find out the root objective of a obj which is specified by client
			 */
			public static long getRootObjId(Connection con, long objId) throws SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("select obj_ancester from objective where obj_id = ? ");
				PreparedStatement pst = con.prepareStatement(sql.toString());
				ResultSet rs = null;
				String ancester = "";
				long rootId;
				try {
					pst = con.prepareStatement(sql.toString());
					pst.setLong(1, objId);
					rs = pst.executeQuery();
					if (rs.next())
						ancester = rs.getString(1);
					Vector v = dbObjective.convert2vec(ancester);
					rootId = ((Long) v.get(0)).longValue();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException sqle) {
							;
						}
					}
					if (pst != null) {
						try {
							pst.close();
						} catch (SQLException sqle) {
							;
						}
					}
				}
				return rootId;
			}
			
		/*
			 * find out the root objective of the resource
			 */
			public static long getResObjRootId(Connection con, long resId)
				throws SQLException {
				long objId = 0;
				long objPid = 0;
				StringBuffer sql = new StringBuffer();
				sql
					.append("select ob.obj_id,ob.obj_obj_id_parent from resourceObjective rob,objective ob ")
					.append("where rob_res_id = ? and rob_obj_id = obj_id");
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					pst = con.prepareStatement(sql.toString());
					pst.setLong(1, resId);
					rs = pst.executeQuery();
					if (rs.next()) {
						objId = rs.getLong("obj_id");
						objPid = rs.getLong("obj_obj_id_parent");
					}
					if (objPid != 0) {
						objId = dbObjective.getRootObjId(con, objId);
					}
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException sqle) {
							;
						}
					}
					if (pst != null) {
						try {
							pst.close();
						} catch (SQLException sqle) {
							;
						}
					}
				}
				return objId;
			}
			
			
		public static boolean isRootObj(Connection con, long objId) throws SQLException {
				boolean isRoot = false;
				long pid = 1;
				StringBuffer sql = new StringBuffer();
				sql.append("select obj_obj_id_parent from objective where obj_id = ? ");
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					pst = con.prepareStatement(sql.toString());
					pst.setLong(1, objId);
					rs = pst.executeQuery();
					if (rs.next()) {
						pid = rs.getLong(1);
					}
					if (pid == 0)
						isRoot = true;
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException sqle) {
							;
						}
					}
					if (pst != null) {
						try {
							pst.close();
						} catch (SQLException sqle) {
							;
						}
					}
				}
				return isRoot;

			}
			
		public static void deleteObj(Connection con, long objId, boolean flag)
				throws SQLException {
				ViewObjectiveAccess voa = new ViewObjectiveAccess();
				String del = "delete from objective where obj_id = ?";
				String softDel ="update objective set obj_status = '"+dbObjective.OBJ_STATUS_DELETED+"' where obj_id = ? ";
				//String delAccess = "delete from objectiveAccess where oac_obj_id = ?";
				PreparedStatement pst = null;
				//PreparedStatement pst1 = null;
				try {
					if (dbObjective.isRootObj(con, objId)) {
						//pst1 = con.prepareStatement(delAccess);
						//pst1.setLong(1, objId);
						//pst1.executeUpdate();
						voa.delByObjId(con,objId);
					}
					if (flag){
						pst = con.prepareStatement(softDel);
					}else{
						delResObjByObjID(con, objId);
						pst = con.prepareStatement(del);
					}
					pst.setLong(1, objId);
					pst.executeUpdate();
				} catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
					throw new SQLException(e.getMessage());
				} finally {
					if (pst != null) {
						try {
							pst.close();
						} catch (SQLException sqle) {
							;
						}
					}
				}
			}
		
			public static void deleteObjByParentID(Connection con, long Parent_id)
				throws SQLException {
				String del = "select obj_id from objective where obj_obj_id_parent = ?";
				//String delAccess = "delete from objectiveAccess where oac_obj_id = ?";
				PreparedStatement pst = null;
				ResultSet rs=null;
				//PreparedStatement pst1 = null;
				try {
					pst = con.prepareStatement(del);
					pst.setLong(1, Parent_id);
					CommonLog.info("obj_obj_id_parent="+Parent_id);
					rs=pst.executeQuery();
					while(rs.next()){
						long obj_id= rs.getLong("obj_id");
						deleteObjByParentID(con,obj_id);
						deleteObj(con,obj_id,false);
					}
					
				} catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
					throw new SQLException(e.getMessage());
				} finally {
					if (pst != null) {
						try {
							pst.close();
						} catch (SQLException sqle) {
							;
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException sqle) {
							CommonLog.error(sqle.getMessage(),sqle);;
						}
					}
				}
			}
			
			
		/*
			 * check if the objective has the potential resources.
			 */

			public boolean hasPotentialRes(Connection con)
				throws SQLException {
				boolean hasOtherRes = false;
				StringBuffer sql = new StringBuffer();
				int i;
				sql
					.append("select res_id from ResourceObjective,resources where rob_obj_id = ? and rob_res_id = res_id ")
					.append("and res_res_id_root is not null");
				/*for(i = 0;i<types.length-1;i++){
							sql.append(" '").append(types[i]).append("',");
				}
						sql.append(" '").append(types[i]).append("' )");*/
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					pst = con.prepareStatement(sql.toString());
					pst.setLong(1, this.obj_id);
					rs = pst.executeQuery();
					if (rs.next())
						hasOtherRes = true;
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				} finally {
					if (rs != null) {rs.close(); }
					if (pst != null) { pst.close();}
				}
				return hasOtherRes;
			}
			
			
		/*
		 * check if the objective has the direct resources which type is in "'QUE', 'GEN', 'AICC'".
		 */
		private boolean hasDirectChildRes(Connection con, String[] types) throws SQLException {
			StringBuffer sql =
				new StringBuffer("select res_id from ResourceObjective,Resources where rob_obj_id = ? and rob_res_id = res_id and res_res_id_root is null and res_type in (");
			int i;
			for (i = 0; i < types.length - 1; i++) {
				sql.append(" '").append(types[i]).append("',");
			}
			sql.append(" '").append(types[i]).append("' )");
			PreparedStatement stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, obj_id);
			ResultSet rs = stmt.executeQuery();
			boolean hasDirect = false;
			if (rs.next())
				hasDirect = true;
			stmt.close();
			return hasDirect;
		}
		
      
		public boolean hasPotentialObj(Connection con)
				throws SQLException {
				boolean hasOtherObj = false;
				StringBuffer sql = new StringBuffer();
				int i;
				sql.append("select obj_id from objective where obj_obj_id_parent = ? ");
				PreparedStatement pst = null;
				ResultSet rs = null;
				try {
					pst = con.prepareStatement(sql.toString());
					pst.setLong(1, this.obj_id);
					rs = pst.executeQuery();
					if (rs.next())
						hasOtherObj = true;
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				} finally {
					if (rs != null) { rs.close(); }
					if (pst != null) { pst.close(); }
				}
				return hasOtherObj;
			}
			//christ
//		public static Vector ancestors_vec = new Vector();
		public Vector getObjAncesters(Connection con)throws SQLException{
			Vector ancestors_vec = new Vector();
			long direct_parent_id = 0;
			String sql = new String();
			StringBuffer sqlBuf = new StringBuffer();
			sqlBuf.append("select obj_ancester,obj_desc obj_desc, obj_tcr_id from objective where obj_id = ? ");
			PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
			stmt.setLong(1,this.obj_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				String ancesters = rs.getString(1);
				String cur_desc = rs.getString(2);
	//			ancestors_vec.add(cur_desc);
				if (ancesters != null && ancesters.length() > 0) {
					sql = "select obj_desc from objective where obj_id in(" +ancesters+ ")order by " + cwSQL.getDbLenFunPattern(con,"obj_ancester");
					stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					while(rs.next()){
						ancestors_vec.add(rs.getString(1));
		 			}
	 			}
				ancestors_vec.add(cur_desc);
			}
			rs.close();
			stmt.close();
			return ancestors_vec;
		}
		public void outObjAncestorTreeXml(StringBuffer result,Vector obj_ancestors_vec){
			result.append("<objective_path>");
			for (int i = 0 ;i < obj_ancestors_vec.size(); i++) {
				result.append("<objective>").append(cwUtils.esc4XML((String)obj_ancestors_vec.elementAt(i))).append("</objective>");
			}
			result.append("</objective_path>");
		//return result;				
		}
        
    public void getAicc(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("Select obj_id, obj_syl_id, obj_type, obj_desc, obj_obj_id_parent, obj_title, obj_developer_id, obj_import_xml, obj_ancester, obj_status from Objective where obj_id = ? ");
            stmt.setLong(1, obj_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                obj_id = rs.getLong("obj_id");
                obj_syl_id = rs.getLong("obj_syl_id");
                obj_type = rs.getString("obj_type");
                obj_desc = rs.getString("obj_desc");
                obj_obj_id_parent = rs.getLong("obj_obj_id_parent");
                obj_title = rs.getString("obj_title");
                obj_developer_id = rs.getString("obj_developer_id");
                obj_import_xml = rs.getString("obj_import_xml");
                obj_ancester = rs.getString("obj_ancester");
                obj_status = rs.getString("obj_status");
            } else {
                throw new SQLException("No such objective found.");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    public void DeleteObjList(Connection con, String[] obj_id_lst, String[] types, loginProfile prof) throws SQLException, qdbErrMessage, qdbException {
    	Vector del_vec = new Vector();
    	Vector not_vec = new Vector();
    	if(obj_id_lst != null && obj_id_lst.length > 0) {
    		AcTrainingCenter acTc = new AcTrainingCenter(con);
    		for(int i = 0; i < obj_id_lst.length; i++) {
    			long obj_id = Long.parseLong(obj_id_lst[i]);
    			if(acTc.hasObjInMgtTc(prof.usr_ent_id,obj_id,prof.current_role).equals(CAN_MGT_OBJ)) {
    				del_vec.add(new Long(obj_id));
    			} else {
    				not_vec.add(new Long(obj_id));
    			}
    		}
    		
    	}
//    	if(not_vec.size() > 0) {
//    		throw new qdbErrMessage(OBJ_NOT_IN_TA_MGT_TC);
//    	}
        ViewObjectiveAccess voa = new ViewObjectiveAccess();
        AcObjective acObj = new AcObjective(con);
        if(del_vec.size() > 0) {
            for(int i = 0; i < del_vec.size(); i++) {
            	Long obj_id_ = (Long)del_vec.get(i);
                if (!true){
                	not_vec.add(obj_id_);
//                    throw new qdbErrMessage("OBJ001");
                } else {
                	try{
                        obj_id =obj_id_.longValue();
                        get(con);
                        voa.delByObjId(con, obj_id_.longValue());
                        del(con, prof, types);//From objective
                        con.commit();
                	} catch(qdbErrMessage e) {
                		not_vec.add(obj_id_);
                		con.rollback();
                	}
                	
                }
            }
        }
    	if(not_vec.size() > 0) {
    		throw new qdbErrMessage("OBJ016", getUndelOjbTitleAsStr(con, not_vec));
    	}
    }
	private static String getUndelOjbTitleAsStr(Connection con, Vector undelVec) throws SQLException {
		boolean isFirst = true;
		String sql = " select obj_desc from Objective where obj_id in " + cwUtils.vector2list(undelVec);
		PreparedStatement stmt = null;
		StringBuffer result = new StringBuffer();
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (!isFirst) {
					result.append(",");
				}
				result.append(cwUtils.esc4XML(rs.getString("obj_desc")));
				isFirst = false;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result.toString();
	}
	
	public static String getResTypeConditionOfLrn(String[] resSubtypeLst) {
		String conditionSql = " and (1=0";
		Vector resSubtypeVec = null;
		if(resSubtypeLst != null) {
			resSubtypeVec = cwUtils.String2vector(resSubtypeLst);
		}
		for(int resTypeIndex = 0; resTypeIndex < LRN_VIEW_RES_TYPE.length; resTypeIndex++) {
			String resSubType =  LRN_VIEW_RES_SUBTYPE[resTypeIndex];
			if(resSubtypeVec != null && resSubtypeVec.size() > 0 && resSubtypeVec.contains(resSubType)) {
				conditionSql += " or (res_type = '" + LRN_VIEW_RES_TYPE[resTypeIndex] 
				        + "' and res_subtype ='" + resSubType + "')";
			} else if(resSubtypeVec == null || resSubtypeVec.size() == 0){
				conditionSql += " or (res_type = '" + LRN_VIEW_RES_TYPE[resTypeIndex] 
						+ "' and res_subtype ='" + resSubType + "')";
			}
		}
		conditionSql += ") ";
		return conditionSql;
	}
	
	public static Vector getRootFolderListOfLrn(Connection con, WizbiniLoader wizbini, long rootEntId, long tcrId, String sort, String dir) throws SQLException {
		Vector rootFolderIdVec = new Vector();

		String sql = "select obj_id from objective";
		sql += " inner join objectiveAccess on (oac_obj_id = obj_id and oac_access_type = ?)";
		sql += " inner join Syllabus on (syl_id = obj_syl_id and syl_ent_id_root = ?)";
		if (tcrId != 0) {
			sql += " inner join tcTrainingCenter on (obj_tcr_id = tcr_id and tcr_status = ? and tcr_id = ?)";
		}
		sql += " where obj_status = ? and obj_type = ? ";
		if (wizbini != null && !wizbini.cfgSysSetupadv.isLrnShowHeaderTc()) {
			sql += " and obj_tcr_id <> 1 ";
		}
		if (dir == null || "".equals(dir)) {
			dir = "asc";
		}
		if (sort == null || "".equals(sort)) {
			sql += " order by obj_id " + dir;
		} else {
			sql += " order by " + sort + " " + dir;
		}

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, AcObjective.LRN_VIEW);
			stmt.setLong(index++, rootEntId);
			if (tcrId != 0) {
				stmt.setString(index++, DbTrainingCenter.STATUS_OK);
				stmt.setLong(index++, tcrId);
			}
			stmt.setString(index++, OBJ_STATUS_OK);
			stmt.setString(index++, OBJ_TYPE_SYB);

			rs = stmt.executeQuery();
			while (rs.next()) {
				rootFolderIdVec.addElement(new Long(rs.getLong("obj_id")));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return rootFolderIdVec;
	}
	
	public static final String INTERVAL_OBJ_ANCESTER = " , ";
	public static HashMap getSubCatIdListByIds(Connection con, Vector catIdVec) throws SQLException {
		HashMap relateMap = new HashMap();
		Vector subFolderIdVec = null;
		HashMap parentMap = new HashMap();
		
		if(catIdVec != null && catIdVec.size() > 0) {
			String sql = "select obj_id, obj_ancester from objective" +
						" where obj_status = ? and obj_type = ? " + getSubFolderConditionSql(catIdVec);
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = con.prepareStatement(sql);
				int index = 1;
				stmt.setString(index++, OBJ_STATUS_OK);
				stmt.setString(index++, OBJ_TYPE_SYB);
				rs = stmt.executeQuery();
				while(rs.next()) {
					if(subFolderIdVec == null) {
						subFolderIdVec = new Vector();
					}
					if(relateMap == null) {
						relateMap = new HashMap();
					}
					Long subFolderId = new Long(rs.getLong("obj_id"));
					subFolderIdVec.addElement(subFolderId);
					
					Long parentFolderId = null;
					String objAncester = rs.getString("obj_ancester");
					Vector objAncesterVec = cwUtils.splitToVecString(objAncester, INTERVAL_OBJ_ANCESTER);
					for(int ancesterIndex = 0; ancesterIndex < objAncesterVec.size(); ancesterIndex++) {
						Long ancesterId = Long.valueOf(((String)objAncesterVec.elementAt(ancesterIndex)).trim());
						if(catIdVec.contains(ancesterId)) {
							parentFolderId = ancesterId;
							break;
						}
					}
					parentMap.put(subFolderId, parentFolderId);
				}
			} finally {
				cwSQL.closePreparedStatement(stmt);
			}
		}
		relateMap.put(KEY_CAT_ID_LST, subFolderIdVec);
		relateMap.put(KEY_PARENT_CAT, parentMap);
		
		return relateMap;
	}

	public static final String KEY_CAT_ID_LST = "cat_id_lst";
	public static final String KEY_PARENT_CAT = "parent_cat";
	private static String getSubFolderConditionSql(Vector folderIdVec) {
		String conditionSql = "";
		if(folderIdVec != null && folderIdVec.size() > 0) {
			for(int folderIndex = 0; folderIndex < folderIdVec.size(); folderIndex++) {
				Long folderId = (Long)folderIdVec.elementAt(folderIndex);
				conditionSql += " obj_ancester like '% " + folderId.toString() + " %'";
				if(folderIndex < folderIdVec.size() - 1) {
					conditionSql += " or";
				}
			}
			conditionSql = " and (" + conditionSql + ")";
		} 
		return conditionSql;
	}
	
	private static final String SRH_KEY_TYPE_CODE = "CODE";
	private static final String SRH_KEY_TYPE_TITLE = "TITLE";
	private static final String SRH_KEY_TYPE_DESC = "DESC";
	private static final String[] SRH_KEY_TYPE_LST_DEFAULT = new String[]{"CODE", "TITLE", "DESC"};  
	public static String getSrhConditionSql(MaterialSrhCriteriaBean srhCriteriaBean) {
		String conditionSql = "";
		String srhKey = srhCriteriaBean.getSrh_key();
		String[] keyTypeLst = srhCriteriaBean.getKey_type_lst();
		String[] resSubtypeLst = srhCriteriaBean.getRes_subtype_lst();
		if(keyTypeLst == null) {
			keyTypeLst = SRH_KEY_TYPE_LST_DEFAULT;
		}
		if(srhKey != null) {
			for(int keyTypeIndex = 0; keyTypeIndex < keyTypeLst.length; keyTypeIndex++) {
				if(SRH_KEY_TYPE_CODE.equalsIgnoreCase(keyTypeLst[keyTypeIndex])) {
					conditionSql += " or res_id = " + getResIdByString(srhKey);
				} else if(SRH_KEY_TYPE_TITLE.equalsIgnoreCase(keyTypeLst[keyTypeIndex])) {
					conditionSql += " or res_title like ?";
				} else if(SRH_KEY_TYPE_DESC.equalsIgnoreCase(keyTypeLst[keyTypeIndex])) {
					conditionSql += " or res_desc like ?";
				}
			}
			conditionSql = " and (1=0 " + conditionSql + ")";
		}
		if(srhCriteriaBean.getDifficulty_lst() != null && srhCriteriaBean.getDifficulty_lst().length > 0) {
			conditionSql += " and res_difficulty in" + cwUtils.array2list(srhCriteriaBean.getDifficulty_lst());
		}
		conditionSql += getResTypeConditionOfLrn(resSubtypeLst);
		return conditionSql;
	}
	
	private static Vector getSrhConditionLst(MaterialSrhCriteriaBean srhCriteriaBean) {
		Vector srhConditionVec = new Vector();
		String srhKey = srhCriteriaBean.getSrh_key();
		String[] keyTypeLst = srhCriteriaBean.getKey_type_lst();
		if(keyTypeLst == null) {
			keyTypeLst = SRH_KEY_TYPE_LST_DEFAULT;
		}
		if(srhKey != null) {
			for(int keyTypeIndex = 0; keyTypeIndex < keyTypeLst.length; keyTypeIndex++) {
				if(SRH_KEY_TYPE_TITLE.equalsIgnoreCase(keyTypeLst[keyTypeIndex])
					|| SRH_KEY_TYPE_DESC.equalsIgnoreCase(keyTypeLst[keyTypeIndex])) {
					srhConditionVec.addElement("%" + srhKey + "%");
				} 
			}
		}
		return srhConditionVec;
	}
	
	private static long getResIdByString(String idStr) {
		long resId = 0;
		try {
			resId = Long.parseLong(idStr);
		} catch(NumberFormatException e) {
		}
		return resId;
	}
	
	public static Hashtable getResCountOfCatalogs(Connection con, Vector folderIdList, MaterialSrhCriteriaBean srhCriteriaBean) throws SQLException {
		Hashtable catResCountTab = new Hashtable();
		
		String sql = "select obj_id, count(rob_res_id) as obj_res_count from objective" +
				" inner join resourceObjective on (obj_id = rob_obj_id)" +
				" inner join resources on (rob_res_id = res_id and res_status = ?";
		if(srhCriteriaBean != null) {
			sql += getSrhConditionSql(srhCriteriaBean);
		} else {
			sql += getResTypeConditionOfLrn(null);
		}
		String objIdCondSql = "(0)";
		if(folderIdList != null && folderIdList.size() > 0) {
			objIdCondSql = cwUtils.vector2list(folderIdList);
		}
		sql += " ) where obj_status = ? and obj_id in " + objIdCondSql + " group by obj_id ";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, dbResource.RES_STATUS_ON);
			if(srhCriteriaBean != null) {
				Vector paramVec = getSrhConditionLst(srhCriteriaBean);
				for(Iterator paramIter = paramVec.iterator(); paramIter.hasNext();) {
					stmt.setString(index++, (String) paramIter.next());
				}
			}
			stmt.setString(index++, OBJ_STATUS_OK);
			rs = stmt.executeQuery();
			while (rs.next()) {
				catResCountTab.put(new Long(rs.getString("obj_id")), new Integer(rs.getString("obj_res_count")));
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		return catResCountTab;
	}
	
	public static final String KEY_RES_LIST = "res_list";
	public static final String KEY_RES_COUNT = "res_count";
	public static HashMap getResListOfFolders(Connection con, Vector folderIdList, MaterialSrhCriteriaBean srhCriteriaBean, int start, int pageSize) throws SQLException {
		HashMap resInfoMap = new HashMap();
		Vector resVec = new Vector();
		int resCount = 0;
		
		String sql = "select res_id, res_title, res_desc, res_type, res_subtype, res_difficulty, res_src_type, res_src_link from objective" +
				" inner join resourceObjective on (obj_id = rob_obj_id)" +
				" inner join resources on (rob_res_id = res_id and res_status = ?";
		if(srhCriteriaBean != null) {
			sql += getSrhConditionSql(srhCriteriaBean);
		} else {
			sql += getResTypeConditionOfLrn(null);
		}
		String objIdCondSql = "(0)";
		if(folderIdList != null && folderIdList.size() > 0) {
			objIdCondSql = cwUtils.vector2list(folderIdList);
		}
		
		sql += " ) where obj_status = ? and obj_id in " + objIdCondSql;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, dbResource.RES_STATUS_ON);
			if(srhCriteriaBean != null) {
				Vector paramVec = getSrhConditionLst(srhCriteriaBean);
				for(Iterator paramIter = paramVec.iterator(); paramIter.hasNext();) {
					stmt.setString(index++, (String) paramIter.next());
				}
			}
			stmt.setString(index++, OBJ_STATUS_OK);
			rs = stmt.executeQuery();
			while (rs.next()) {
				if(start <= resCount && resCount < start + pageSize) {
					MaterialBean materialBean = new MaterialBean();
					materialBean.setId(rs.getLong("res_id"));
					materialBean.setTitle(rs.getString("res_title"));
					materialBean.setType(rs.getString("res_type"));
					materialBean.setSubtype(rs.getString("res_subtype"));
					materialBean.setDesc(rs.getString("res_desc"));
					materialBean.setDifficulty(rs.getInt("res_difficulty"));
					materialBean.setSrc_type(rs.getString("res_src_type"));
					materialBean.setSrc_link(rs.getString("res_src_link"));
					resVec.add(materialBean);
				}
				resCount++;
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		resInfoMap.put(KEY_RES_LIST, resVec);
		resInfoMap.put(KEY_RES_COUNT, Integer.valueOf(resCount+""));
		
		return resInfoMap;
	}

	public static Vector getObjListByIds(Connection con, Vector objIdVec) throws SQLException {
		return getObjListByIds(con, objIdVec, null);
	}
	
	public static Vector getObjListByIds(Connection con, Vector objIdVec, String sortColumn) throws SQLException {
		Vector objVec = null;
		String objIdCondSql = "(0)";
		if(objIdVec != null && objIdVec.size() > 0) {
			objIdCondSql = cwUtils.vector2list(objIdVec);
		}
		
		String orderSql = " order by obj_desc, obj_obj_id_parent asc";
		if(sortColumn != null && !"".equals(sortColumn)) {
			orderSql = " order by " + sortColumn + " asc";
		}
		
		String sql = "select obj_id, obj_desc, obj_tcr_id, tcr_title from objective " +
				" left join tcTrainingCenter on (tcr_id = obj_tcr_id and tcr_status = ?)" +
				" where obj_status = ? and obj_id in " 
				+ objIdCondSql + orderSql;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
			stmt.setString(index++, OBJ_STATUS_OK);
			rs = stmt.executeQuery();
			while(rs.next()) {
				if(objVec == null) {
					objVec = new Vector();
				}
				MaterialCatalogBean catBean = new MaterialCatalogBean();
				catBean.setId(rs.getLong("obj_id"));
				catBean.setText(rs.getString("obj_desc"));
				catBean.setDesc(rs.getString("obj_desc"));
				catBean.setTcr_id(rs.getLong("obj_tcr_id"));
				catBean.setTcr_title(rs.getString("tcr_title"));
				objVec.add(catBean);
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		return objVec;
	}
	
	public static MaterialCatalogBean getParentObj(Connection con, long objId) throws SQLException {
		MaterialCatalogBean catBean = null;
		
		String sql = "select parent_obj.obj_id, parent_obj.obj_desc from objective child_obj" +
				" inner join objective parent_obj on (child_obj.obj_obj_id_parent = parent_obj.obj_id and parent_obj.obj_status = ?)" +
				" where child_obj.obj_status = ? and child_obj.obj_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, OBJ_STATUS_OK);
			stmt.setString(index++, OBJ_STATUS_OK);
			stmt.setLong(index++, objId);
			rs = stmt.executeQuery();
			while(rs.next()) {
				if(catBean == null) {
					catBean = new MaterialCatalogBean();
				}
				catBean.setId(rs.getLong("obj_id"));
				catBean.setDesc(rs.getString("obj_desc"));
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		return catBean;
	}
	
	public static Vector getChildOjbByObjId(Connection con, long objId) throws SQLException {
		long[] objIdLst = new long[]{objId};
		return getChildOjbByObjId(con, objIdLst);
	}
	
	public static Vector getChildOjbByObjId(Connection con, long[] objIds) throws SQLException {
		Vector childObjVec = new Vector();
		
		String condSql = "(0)";
		if(objIds != null && objIds.length > 0) {
			condSql = cwUtils.array2list(objIds);
		}
		String sql = "select obj_id, obj_desc, obj_type from objective where " +
				" obj_obj_id_parent in " + condSql +
				" and obj_status = ? order by obj_desc";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, OBJ_STATUS_OK);
			rs = stmt.executeQuery();
			while(rs.next()) {
				childObjVec.add(new Long(rs.getLong("obj_id")));
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		return childObjVec;
	}
	
	public static Vector getChildListByObjId(Connection con, long curObjId) throws SQLException {
		Vector objVec = new Vector();
		
		String sql = "select obj_id, obj_ancester from objective where obj_type = ? and obj_status = ? and obj_ancester like '% " + curObjId + " %' order by obj_desc";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, OBJ_TYPE_SYB);
			stmt.setString(index++, OBJ_STATUS_OK);
			rs = stmt.executeQuery();
			while(rs.next()) {
				dbObjective obj = new dbObjective();
				obj.obj_id = rs.getLong("obj_id");
				obj.obj_ancester = rs.getString("obj_ancester");
				objVec.addElement(obj);
			}
		}finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		return objVec;
	}

	public static HashMap getDescByObjIds(Connection con, Vector objIdVec) throws SQLException {
		HashMap descMap = new HashMap();
		String condSql = "(0)";
		if(objIdVec != null && objIdVec.size() > 0) {
			condSql = cwUtils.vector2list(objIdVec);
		}
		String sql = "select obj_id, obj_desc from objective where obj_type = ? and obj_status = ? and obj_id in" + condSql;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, OBJ_TYPE_SYB);
			stmt.setString(index++, OBJ_STATUS_OK);
			rs = stmt.executeQuery();
			while(rs.next()) {
				descMap.put(new Long(rs.getLong("obj_id")), rs.getString("obj_desc"));
			}
		}finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		return descMap;
	}

	private static final String sql_get_shared_obj_ids = "select obj_id,obj_desc,obj_type from objective where obj_type = ? and obj_share_ind = ? and obj_status=? and obj_syl_id = ? order by obj_desc";
	public static Vector getSharedObjIds(Connection con, long syb_id) throws SQLException {
	    PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector vec = new Vector();
        try {
            stmt = con.prepareStatement(sql_get_shared_obj_ids);
            int idx = 1;
            stmt.setString(idx++, OBJ_TYPE_SYB);
            stmt.setBoolean(idx++, true);
            stmt.setString(idx++, OBJ_STATUS_OK);
            stmt.setLong(idx++, syb_id);
            rs = stmt.executeQuery();
            while(rs.next()) {
                dbObjective obj = new dbObjective(rs.getLong(1));
                obj.obj_desc = rs.getString("obj_desc");
                obj.obj_type = rs.getString("obj_type");
                vec.add(obj);
            }
            return vec;
        }finally {
            cwSQL.closePreparedStatement(stmt);
        }
	}
	
	public static String getSharedObjAsXML(Connection con, loginProfile prof, String res_lan ,String[] types,String[] subtypes, long syb_id) throws SQLException, qdbException {
	    Vector vec = getSharedObjIds(con, syb_id);
	    StringBuffer str = new StringBuffer();
	    str.append("<shared_folders>");
	    str.append(dbObjective.getObjNodeAsXML(con, vec, res_lan, types, subtypes, prof,null, false, null));
	    str.append("</shared_folders>");
	    return str.toString();
	}
	
	/**
	 * 删除资源
	 * @param res_id 资源id 
	 */
	public static void delResources(Connection con, WizbiniLoader wizbini, long res_id) throws SQLException, qdbException, cwSysMessage, qdbErrMessage {
		dbResource myDbResource = new dbResource();
        myDbResource.res_id = res_id;
		myDbResource.get(con);
        if (myDbResource.res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_DAS)
            || myDbResource.res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_FAS)
            || myDbResource.res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)
            || myDbResource.res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
            DynamicQueContainer queContainer = new DynamicQueContainer();
            queContainer.res_id = myDbResource.res_id;
            queContainer.delAssessment(con, wizbini.getFileUploadResDirAbs());
        } else {
            if (myDbResource.res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_RES_SCO)) {
                dbScormResource srs = new dbScormResource();
                srs.srs_res_id = myDbResource.res_id;
                srs.del(con);
                Vector child_res = dbResourceContent.getChildResources(con, myDbResource.res_id);
                for (int j = 0; j < child_res.size(); j++) {
                    dbResource tmp_res = (dbResource) child_res.get(j);
                    dbModule mod = new dbModule();
                    mod.res_id = tmp_res.res_id;
                    mod.mod_res_id = tmp_res.res_id;
                    mod.res_upd_date = tmp_res.res_upd_date;
                    mod.del(con);
                }
            }
            delRes(con, res_id, wizbini.getFileUploadResDirAbs());
        }
	}
	
	/**
	 * 检测该资源目录是否残留着copy过的资源
	 * @param obj_id 资源目录id
	 */
	public static boolean hasCopyRes(Connection con, long obj_id) throws SQLException {
		String sql = " select res_id from resourceObjective inner join resources on res_id = rob_res_id "
					+ " where rob_obj_id = ? and res_res_id_root is not null ";
		PreparedStatement stmt = null;
    	ResultSet rs = null;
    	boolean hasCopyResInd = false;
    	try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, obj_id);
    		rs = stmt.executeQuery();
    		if(rs.next()){
    			hasCopyResInd = true;
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return hasCopyResInd;
	}
	
	/**
	 * 去掉资源目录与培训中心的关联
	 * @param obj_id 资源目录id
	 */
	public static void disassociatedObjAndTcr(Connection con, long obj_id) throws SQLException {
		String sql = " update objective set obj_tcr_id = null where obj_id = ? ";
		PreparedStatement stmt = null;
		try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, obj_id);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
	}
	
	public static void delResObjByObjID(Connection con, long obj_id) throws SQLException {
		String sql = " delete from ResourceObjective where rob_obj_id= ? ";
		PreparedStatement stmt = null;
		try{
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, obj_id);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.cleanUp(null, stmt);
    	}
	}
}