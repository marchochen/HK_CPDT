package com.cw.wizbank.accesscontrol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.cache.wizbCacheManager;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleService;
import com.cwn.wizbank.web.WzbApplicationContext;

public class AccessControlWZB extends AccessControlManager {

    public static final String ROL_EXT_ID = AccessControlCore.ROL_EXT_ID;
    public static final String ROL_XML = AccessControlCore.ROL_XML;
    public static final String ROL_AUTH_LEVEL = AccessControlCore.ROL_AUTH_LEVEL;
    public static final String ROL_STATUS_OK = AccessControlCore.ROL_STATUS_OK;
    public static final String ROL_STATUS_HIDDEN = AccessControlCore.ROL_STATUS_HIDDEN;
    public static final String ROL_TARGET_ENT_TYPE_USR = AccessControlCore.ROL_TARGET_ENT_TYPE_USR;
    public static final String ROL_TARGET_ENT_TYPE_USG = AccessControlCore.ROL_TARGET_ENT_TYPE_USG;
    public static final String ROL_TARGET_ENT_TYPE_USR_OR_USG = AccessControlCore.ROL_TARGET_ENT_TYPE_USR_OR_USG;
    public static final String ROL_STE_UID_TADM = "TADM";
    public static final String ROL_STE_UID_LRNR = "LRNR";
    public static final String ROL_STE_UID_INST = "INSTR";
    public static final String ROL_STE_UID_ADM ="ADM";
    public static final String ROL_EXT_ID_NLRN ="NLRN_1";
    
    public static class ViewEntityRole {
        public String rol_ext_id;
        public Timestamp erl_eff_start_datetime;
        public Timestamp erl_eff_end_datetime;
    }

    

    /**
    check if user, role pair has privilege on a function<BR>
          当前角色是有该操作权限
    */
    public static boolean hasRolePrivilege(String rol_ext_id, String ftn_ext_id)  {
        boolean result;
        result = ((AclService) WzbApplicationContext.getBean("aclService")).hasAnyPermission(rol_ext_id, ftn_ext_id);
        return result;
    }
    
    /**
    check if user, role pair has privilege on a function<BR>
          当前角色是有该操作权限
    */
    public static boolean hasRolePrivilege(String rol_ext_id, String[] ftn_ext_id)  {
        boolean result;
        result = ((AclService) WzbApplicationContext.getBean("aclService")).hasAnyPermission(rol_ext_id, ftn_ext_id);
        return result;
    }
    
    
    
    /**
    check if Role is relate to TC<BR>
           角色的权限是否与中心关联
    */
    public static boolean isRoleTcInd(String rol_ext_id) {
        boolean result;
        result = ((AcRoleService) WzbApplicationContext.getBean("acRoleService")).isRoleTcInd(rol_ext_id);
        return result;
    }
    
    /**
    is instructor roles of organization<BR>
            是否为讲师角色
    */
    public static boolean isIstRole(String rol_ext_id) {
    	return((AclService) WzbApplicationContext.getBean("aclService")).hasAnyPermission(rol_ext_id, AclFunction.FTN_AMD_TEACHING_COURSE_LIST);
    }
    
    /**
    is sysadmin roles of organization<BR>
    */
    public static boolean isSysAdminRole(String rol_ext_id) {
    	return((AclService) WzbApplicationContext.getBean("aclService")).hasAnyPermission(rol_ext_id, AclFunction.FTN_AMD_SYS_SETTING_MAIN)
    	&& ((AclService) WzbApplicationContext.getBean("aclService")).hasAnyPermission(rol_ext_id, AclFunction.FTN_AMD_SYS_ROLE_MAIN);
    }
    
    /**
     is learner roles of organization<BR>
             是否为学员角色
    */
    public static boolean isLrnRole(String rol_ext_id) throws SQLException {
    	return((AclService) WzbApplicationContext.getBean("aclService")).hasAnyPermission(rol_ext_id, AclFunction.FTN_LRN_LEARNING_SIGNUP);
    }
    
    
    /**
            用户是否有对应的角色是否为学员角色
   */
    public static boolean hasRole(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
    	AccessControlWZB acc =  new AccessControlWZB();
        long rol_id = acc.getRoleID(con, rol_ext_id);
        return acc.aclCore.hasEntityRole(con, usr_ent_id, rol_id);
    }
    
    
    /**
	    是否开放CPD功能
	*/
	public static boolean hasCPDFunction() throws SQLException {
		  Map<String, Object> map =new HashMap<String, Object>();
		  map.put("code", AclFunction.FTN_AMD_CPT_D_MGT);
	      return ((AclService) WzbApplicationContext.getBean("aclService")).hasCPDFunction(map);
	}
    


    //assign an user to a role
    public void assignUser2Role(Connection con, long usr_ent_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);

        aclCore.assignEntity2Role(con, usr_ent_id, rol_id, startDate, endDate);
    }
    public void updEntityRoleSynTimestamp(Connection con, long usr_ent_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException{
        long rol_id = getRoleID(con, rol_ext_id);
        aclCore.updEntityRoleSynTimestamp(con, usr_ent_id, rol_id, startDate, endDate);
    }
    
    // for backward compatible, dont use this method again
    public void rmUserFromRole(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
        rmUserFromRole(con, usr_ent_id, rol_ext_id, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
    }

    //remove an user from a role
    public void rmUserFromRole(Connection con, long usr_ent_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);

        aclCore.rmEntityFromRole(con, usr_ent_id, rol_id, startDate, endDate);
    }


    //get the entities that a role contains
    public long[] containUsers(Connection con, String rol_ext_id) throws SQLException {
        long[] EntitiesID;
        long rol_id = getRoleID(con, rol_ext_id);

        EntitiesID = aclCore.containEntities(con, rol_id);
        return EntitiesID;
    }


  



    //get the roles that an user belongs to
    public String [] getUserRoles(Connection con, long usr_ent_id) throws SQLException {
        return aclCore.getRoles(con, usr_ent_id, false);
        /*
        String[] Roles;
        long[] RolesID;

        RolesID = aclCore.getRoles(con, usr_ent_id, false);
        Roles = RoleID2ExtID(con, RolesID);
        return Roles;
        */
    }

    //get the roles that an user belongs to
    public String [] getUserRolesCanLogin(Connection con, long usr_ent_id) throws SQLException {
        return aclCore.getRoles(con, usr_ent_id, true);
        /*
        String[] Roles;
        long[] RolesID;

        RolesID = aclCore.getRoles(con, usr_ent_id, true);
        Roles = RoleID2ExtID(con, RolesID);
        return Roles;
        */
    }

    //check if the user has a active role
    public boolean hasUserRole(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);
        return aclCore.hasEntityRole(con, usr_ent_id, rol_id);
    }
    // check if user has this record
    public boolean hasUserRole(Connection con, long usr_ent_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);
        return aclCore.hasEntityRole(con, usr_ent_id, rol_id, startDate, endDate);
    }

/*
    boolean isEntityRoleRelationExist(Connection con, long ent_id, long rol_id, boolean bCheckEff) throws SQLException {
        boolean bExist = isEntityRoleRelationExist(con, ent_id, rol_id);
        if (bExist && bCheckEff){
            if (getRoleTargetEntInd(con, rol_id)){
                                                
            }
        }
        return bExist; 
    }
*/    
    //remove user from all role
    public void rmUserRoles(Connection con, long usr_ent_id) throws SQLException {
        aclCore.rmEntityRoles(con, usr_ent_id);
    }

    /*
    //return an array of ftn_ext_id that a user has
    public String [] getUserFunctions(Connection con, long usr_ent_id) throws SQLException {
        String[] Functions;
        long[] FunctionsID;

        FunctionsID = aclCore.getFunctions(con, usr_ent_id);
        Functions = FunctionID2ExtID(con, FunctionsID);
        return Functions;
    }
    */


    //get the role_xml of the input role
    public String getFunctionDesc(Connection con, String ftn_ext_id) throws SQLException {

//        final String SQL=
//            " Select ftn_xml From acFunction where ftn_ext_id = ? ";
//        String ftn_xml;
//
//        PreparedStatement stmt = con.prepareStatement(SQL);
//        stmt.setString(1, ftn_ext_id);
//        ResultSet rs = stmt.executeQuery();
//
//        if(rs.next()) {
//            // for oracle clob
//            ftn_xml = cwSQL.getClobValue(rs, "ftn_xml");
//        }
//        else {
//            ftn_xml = "";
//        }
//        stmt.close();
        return "";
    }

    //get the role_xml of the input role
    public String getRoleURL(Connection con, String rol_ext_id) throws SQLException {

        final String SQL=
            " Select rol_url_home From acRole where rol_ext_id = ? ";
        String rol_url_home;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, rol_ext_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            rol_url_home = rs.getString("rol_url_home");
        }
        else {
            rol_url_home = "";
        }
        stmt.close();
        return rol_url_home;
    }

   

    public boolean getRoleTargetEntInd(Connection con, String rol_ext_id) throws SQLException {

        final String SQL=
            " Select rol_target_ent_type From acRole where rol_ext_id = ? ";
        String rol_target_ent_type;
        boolean rol_target_ent_ind;
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, rol_ext_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            rol_target_ent_type = rs.getString("rol_target_ent_type");
            rol_target_ent_ind = (rol_target_ent_type != null);
        }
        else {
            rol_target_ent_ind = false;
        }
        stmt.close();
        return rol_target_ent_ind;
    }
    
    public String[] getRolesInReport(Connection con, long root_ent_id) throws SQLException {

        final String SQL=
            " Select rol_ext_id From acRole where rol_report_ind = ? AND rol_ste_ent_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setBoolean(1,true);
        stmt.setLong(2,root_ent_id);

        ResultSet rs = stmt.executeQuery();

        Vector vtRole = new Vector();
        while (rs.next()) {
            String roles = rs.getString("rol_ext_id");
            vtRole.addElement(roles);
        }
        stmt.close();
        String[] Roles = Vector2String(vtRole);
        return Roles;
    }

    //get the role_xml and rol_skin_root of the input role
    public String[] getRoleDescNSkinRootNHomeURL(Connection con, String rol_ext_id) throws SQLException {

        final String SQL=
            " Select rol_xml, rol_url_home From acRole"+ cwSQL.noLockTable() + " where rol_ext_id = ? ";
        String rol_xml;
//        String rol_skin_root;
        String rol_url_home;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, rol_ext_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
        	rol_xml = getRolXml(rol_ext_id);
//            rol_skin_root = rs.getString("rol_skin_root");
            rol_url_home = rs.getString("rol_url_home");
        }
        else {
            rol_xml = "";
//            rol_skin_root = "";
            rol_url_home = "";
        }

        stmt.close();

        String[] str_array = new String[2];
        str_array[0] = rol_xml;
//        str_array[1] = rol_skin_root;
        str_array[1] = rol_url_home;

        return str_array;
    }

    private static final String SQL_GET_ROLE 
        = " Select rol_ext_id, rol_xml "
        + " From acRole " 
        + " WHERE rol_ste_ent_id = ? "
        + " AND rol_status = ? "
        + " AND rol_ste_uid = ? ";

    public Hashtable getRole(Connection con, long ste_ent_id, String rol_ste_uid) throws SQLException {
        Hashtable h = new Hashtable();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement(SQL_GET_ROLE);
            stmt.setLong(1, ste_ent_id);
            stmt.setString(2, ROL_STATUS_OK);
            stmt.setString(3, rol_ste_uid);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String rol_ext_id = rs.getString("rol_ext_id");
                String rol_xml = getRolXml(rol_ext_id);
                if(rol_ext_id != null) {
                    h.put("rol_ext_id", rol_ext_id);
                }
                if(rol_xml != null) {
                    h.put("rol_xml", rol_xml);
                }
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return h;
    }

    
    //get the hashtable containing 2 vectors
    //one is rol_ext_id
    //one is rol_xml
    private static final String ALL_ROLE_DESC_CACHE = "ALL_ROLE_DESC_CACHE";
    private static final String ALL_ROLE_DESC_CACHE_NOLRN = "ALL_ROLE_DESC_CACHE_NOLRN";
    public Hashtable getAllRoleDesc(Connection con, long ste_ent_id, boolean filtLrn) throws SQLException {
        HashMap cache = new HashMap();
        if (cache.get(new Long(ste_ent_id)) == null) {
            Vector v_rol_ext_id = new Vector();
            Vector v_rol_xml = new Vector();
            Vector v_rol_auth_level = new Vector();
            Hashtable h_result = new Hashtable();
    
            String SQL=
                " Select rol_ext_id, rol_auth_level, rol_xml, rol_title From acRole WHERE rol_ste_ent_id = ? AND rol_status = ?";
            if(filtLrn) {
            	SQL += " and rol_ste_uid <> 'LRNR'";
            }
            SQL += " ORDER BY rol_seq_id";
    		
    
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, ste_ent_id);
            stmt.setString(2, ROL_STATUS_OK);
            ResultSet rs = stmt.executeQuery();
    
            while(rs.next()) {
                v_rol_ext_id.addElement(rs.getString("rol_ext_id"));
                v_rol_auth_level.addElement(new Long(rs.getLong("rol_auth_level")));
                v_rol_xml.addElement(getRolXml(rs.getString("rol_ext_id"),rs.getString("rol_title")));
            }
            stmt.close();
            h_result.put(ROL_EXT_ID, v_rol_ext_id);
            h_result.put(ROL_XML, v_rol_xml);
            h_result.put(ROL_AUTH_LEVEL, v_rol_auth_level);
            cache.put(new Long(ste_ent_id), h_result);
            return h_result;
        } else {
            return (Hashtable) cache.get(new Long(ste_ent_id));
        }
    }

    //get all roles with uid
    // key - rol_ste_uid / rol_ext_id
    //return hashtable rol_ste_uid as key, rol_ext_id as value
    public Hashtable getAllRoleUid(Connection con, long ste_ent_id, String key) throws SQLException, cwException {
        Hashtable h_result = new Hashtable();

        if (!key.equals("rol_ext_id") && !key.equals("rol_ste_uid")){
            throw new cwException("invalid key in AccessControlWZB.getAllRoleUid.");            
        }            
        final String SQL=
            " Select rol_ext_id, rol_ste_uid From acRole WHERE rol_ste_ent_id = ? AND rol_ste_uid IS NOT NULL ORDER BY rol_seq_id ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ste_ent_id);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            if (key.equals("rol_ext_id")){
                h_result.put(rs.getString("rol_ext_id"), rs.getString("rol_ste_uid"));
            }else if (key.equals("rol_ste_uid")){
            h_result.put(rs.getString("rol_ste_uid"), rs.getString("rol_ext_id"));
            }
        }
        rs.close();
        stmt.close();
        return h_result;
    }

    // get role by target entity and uid not null
/*
    public Vector getImportRoleByTargetEntInd(Connection con, long ste_ent_id, boolean ) throws SQLException {
        Hashtable h_result = new Hashtable();

        final String SQL=
            " Select rol_ext_id, rol_ste_uid From acRole WHERE rol_ste_ent_id = ? AND rol_ste_uid IS NOT NULL ORDER BY rol_seq_id ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ste_ent_id);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            h_result.put(rs.getString("rol_ste_uid"), rs.getString("rol_ext_id"));
        }
        stmt.close();
        return h_result;
    }
*/
    public static String getDefaultRoleBySite(Connection con, long ste_ent_id)throws SQLException{
        final String SQL=
            " Select rol_ext_id From acRole WHERE rol_ste_ent_id = ? AND rol_ste_default_ind = 1 ORDER BY rol_seq_id ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ste_ent_id);
        ResultSet rs = stmt.executeQuery();
        String r_id = null;
        if (rs.next()){
        	r_id = rs.getString("rol_ext_id");
        }
        stmt.close();
        return r_id;
    }

    public AccessControlCore.ViewSynEntityRole[] getNotSynEntityRole(Connection con, String rol_ext_id, Timestamp synTimestamp) throws SQLException{
        long rol_id = getRoleID(con, rol_ext_id);
        return aclCore.getNotSynEntityRole(con, rol_id, synTimestamp);
    }

    public Hashtable getNoRoleUsr(Connection con) throws SQLException{
        return aclCore.getNoRoleUsr(con);
    }

    public ViewEntityRole[] getUserRoleList(Connection con, long usr_ent_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" Select rol_ext_id, erl_eff_start_datetime, erl_eff_end_datetime From acEntityRole, acRole ")
              .append(" Where erl_ent_id = ? ")
              .append(" And rol_id = erl_rol_id ");
        SQLBuf.append(" Order By rol_seq_id ");
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, usr_ent_id);        
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewEntityRole entRole = new ViewEntityRole();
                entRole.rol_ext_id = rs.getString("rol_ext_id");
                entRole.erl_eff_start_datetime = rs.getTimestamp("erl_eff_start_datetime");
                entRole.erl_eff_end_datetime = rs.getTimestamp("erl_eff_end_datetime");
                tempResult.addElement(entRole);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewEntityRole result[] = new ViewEntityRole[tempResult.size()];
        result = (ViewEntityRole[])tempResult.toArray(result);
        
        return result;
    }
    
    private static final String SQL_GET_ROLE_BY_TARGET_TYPE = 
        " Select rol_ext_id, rol_target_ent_type From acRole Where " +
        " rol_ste_ent_id = ? " +
        " And rol_target_ent_type in ";
    /**
    get the roles by the input target entity types
    @param con Connection to database
    @param root_ent_id organization entity id
    @parma target_ent_types String array of target types
    @return Vector contains 2 Vectors in it. The 1st one is the role external id and 
                                             the 2nd one id the role target type
    */
    public Vector getRolesByTargetEntType(Connection con, long root_ent_id, String[] target_ent_types) 
        throws SQLException {
        
        Vector vRole = new Vector();
        Vector vType = new Vector();
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(SQL_GET_ROLE_BY_TARGET_TYPE)
              .append(cwUtils.array2list(target_ent_types));
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, root_ent_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                vRole.addElement(rs.getString("rol_ext_id"));
                vType.addElement(rs.getString("rol_target_ent_type"));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        v.addElement(vRole);
        v.addElement(vType);
        return v;
    }

    private static final String SQL_GET_ORG_APPR_ROLE = 
        " Select rol_ext_id, rol_target_ent_type From acRole Where " +
        " rol_ste_ent_id = ? " +
        " And rol_target_ent_type is not null ";
    /**
    get the roles by the input target entity types
    @param con Connection to database
    @param root_ent_id organization entity id
    @return Vector contains 2 Vectors in it. The 1st one is the role external id and 
                                             the 2nd one id the role target type
    */
    public Vector getOrgApprRole(Connection con, long root_ent_id) 
        throws SQLException {
        
        Vector vRole = new Vector();
        Vector vType = new Vector();
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(SQL_GET_ORG_APPR_ROLE);
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, root_ent_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                vRole.addElement(rs.getString("rol_ext_id"));
                vType.addElement(rs.getString("rol_target_ent_type"));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        v.addElement(vRole);
        v.addElement(vType);
        return v;
    }

    private static final String SQL_GET_ROLE_BY_TARGET_IND =
        " select rol_ext_id from acRole, acEntityRole " +
        " where rol_id = erl_rol_id " +
        " and erl_ent_id = ? " +
        " and rol_target_ent_type is not null " + 
        " AND erl_eff_start_datetime <= ? " +
        " AND erl_eff_end_datetime >= ? ";

    public String[] getUserRolesByTargetInd(Connection con, long usr_ent_id) throws SQLException {
        String[] roles = null;
        PreparedStatement stmt = null;
        Vector v = new Vector();
        Timestamp curTime = cwSQL.getTime(con);
        try {
            stmt = con.prepareStatement(SQL_GET_ROLE_BY_TARGET_IND);
            stmt.setLong(1, usr_ent_id);
            stmt.setTimestamp(2, curTime);
            stmt.setTimestamp(3, curTime);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(rs.getString("rol_ext_id"));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        roles = new String[v.size()];
        for(int i=0; i<v.size(); i++) {
            roles[i] = (String)v.elementAt(i);
        }
        return roles;
    }

    /**
     * add to cache approver's status
     * @param con
     * @return
     * @throws SQLException
     */
    public Vector getAllRole(Connection con) throws SQLException {
        Vector rol_ext_id_list = new Vector();

        final String sqlStr = "SELECT rol_ext_id FROM acRole WHERE rol_status = ? ORDER BY rol_seq_id ";

        PreparedStatement stmt = con.prepareStatement(sqlStr);
        stmt.setString(1, ROL_STATUS_OK);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            rol_ext_id_list.addElement(rs.getString("rol_ext_id"));
        }
        rs.close();
        stmt.close();

        return rol_ext_id_list;
    }


    private static final String SQL_GET_USER_BY_FUNCTION = 
        " select erl_ent_id from acFunction, acRoleFunction, acEntityRole " +
        " where ftn_ext_id = ? " +
        " and ftn_id = rfn_ftn_id " +
        " and rfn_rol_id = erl_rol_id ";
    //get array of usr_ent_id that have the input acFunction entry
    public long[] getUserByFunction(Connection con, String ftn_ext_id) throws SQLException {
        PreparedStatement stmt = null;
        long[] EntityIDs;
        try {
            stmt = con.prepareStatement(SQL_GET_USER_BY_FUNCTION);
            stmt.setString(1, ftn_ext_id);
            ResultSet rs = stmt.executeQuery();
            Vector v = new Vector();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong("erl_ent_id")));
            }
            EntityIDs = new long[v.size()];
            int size = v.size();
            for(int i=0; i<size; i++) {
                EntityIDs[i] = ((Long)v.elementAt(i)).longValue();
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return EntityIDs;        
    }

	public long getRoleAuthLevel(Connection con, long root_ent_id, String rol_ext_id)
		throws SQLException, cwSysMessage {
			return super.getRoleAuthLevel(con, root_ent_id, rol_ext_id);			
		}

	public long getRoleMinAuthLevel(Connection con, long root_ent_id, String[] rol_ext_id)
		throws SQLException, cwSysMessage {
			return super.getRoleMinAuthLevel(con, root_ent_id, rol_ext_id);
		}

    public String getRoleLabel(String rol_ext_id, String labelEncoding) {
    	String rol_label = "lab_rol_" + rol_ext_id.substring(0, rol_ext_id.indexOf("_"));
		return LangLabel.getValue(labelEncoding, rol_label);
    }
    
    String ctRolSql = "select rol_ext_id,rol_xml ,rol_title from acRole where rol_tc_ind = 1 and rol_ste_ent_id = ? order by rol_id";
    public Map getTcOfficerRole(Connection con,long ste_id){
    	Map map = new HashMap();
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	String rol_desc = null;
    	try{
    		pst = con.prepareStatement(ctRolSql);
    		pst.setLong(1,ste_id);
    		rs = pst.executeQuery();
    		while(rs.next()){
    			rol_desc = getRolXml(rs.getString("rol_ext_id"),rs.getString("rol_title"));
    			map.put(rs.getString("rol_ext_id"),rol_desc);
    		}
    	}catch(SQLException e){
    		throw new RuntimeException("System error: "+e.getMessage());
    	}finally{
    		cwSQL.closeResultSet(rs);
    		cwSQL.closePreparedStatement(pst);
    	}
    	return map;
    }
    
    public List getTcOfficerRoles(Connection con,long ste_id){
    	List list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(ctRolSql);
			pst.setLong(1,ste_id);
			rs = pst.executeQuery();
			while(rs.next()){
				list.add(rs.getString("rol_ext_id"));
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return list;
    }
    
    private static final String SQL_IS_TC_OFFICER_ROLE
        = " Select rol_tc_ind From acRole where rol_ext_id = ? ";
    
    public boolean isTcOfficerRole(Connection con, String rol_ext_id) throws SQLException {
        boolean b=false;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_IS_TC_OFFICER_ROLE);
            stmt.setString(1, rol_ext_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                b = rs.getBoolean("rol_tc_ind");
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return b;
    }


    
    

    //change all rol_xml by dean 2008-03-25
    public static String getRolXml(String rol_ext_id) {
    	String rol_xml = "<role id=\"" + cwUtils.escNull(rol_ext_id)+"\"/>";
    	return rol_xml;
    }
    
    public static String getRolXml(String rol_ext_id,String name) {
    	String rol_xml = "<role id=\"" + cwUtils.escNull(rol_ext_id) + "\" name=\""+cwUtils.esc4XML(cwUtils.escNull(name))+ "\" title=\""+cwUtils.esc4XML(cwUtils.escNull(name))+"\"/>";;
    	return rol_xml;
    }
    
  
   
}