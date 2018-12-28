package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.cw.wizbank.JsonMod.role.RoleModuleParam;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class DbAcRole {
	public long rol_id;

	public String rol_ext_id;

	private boolean rol_tc_ind;

	public String rol_title;

	private String rol_xml;
	
	private String rol_type;

	private Timestamp rol_create_timestamp;
	
	private String rol_create_usr_id;
	
	private Timestamp rol_update_timestamp;

	private String rol_url_home = "servlet/qdbAction?cmd=home&stylesheet=home.xsl";

	private static final String GET_ROLE_LIST = "select rol_id, rol_ext_id, rol_type,rol_create_timestamp, rol_tc_ind,rol_title,rol_create_usr_id,rol_update_timestamp from acRole where rol_ste_ent_id = ? and rol_status = 'OK'";

	private static final String DELETE_ROLE = "delete from acRole where rol_id = ?";

	private static final String DEL_MUTI_ROLE_FUNCTION = "delete from acRoleFunction where rfn_rol_id = ?";

	private static final String DEL_ONE_ROLE_FUNCTION = "delete from acRoleFunction where rfn_rol_id = ? and rfn_ftn_id = ?";
	
	private static final String DEL_MUTI_ROLE_HOMEPAGE_FUNCTION_BY_EXT_ID = "delete from acHomePage where ac_hom_rol_ext_id = ?";
	
	private static final String DEL_MUTI_ROLE_HOMEPAGE_FUNCTION_BY_ID = "delete from acHomePage where ac_hom_rol_ext_id = (select rol_ext_id from acRole where rol_id = ?)";
	
	private static final String DEL_ROLE_RALATION_TRANING_CENTRE = "delete from tcTrainingCenterOfficer where exists (select rol_id from acrole where rol_ext_id = tco_rol_ext_id and rol_id = ?)";

	private static final String GET_ROLE_BY_ID = "select rol_id, rol_ext_id, rol_tc_ind, rol_title from acRole where rol_id = ?";
	
	private static final String GET_ROLE_BY_EXT_ID = "select rol_id, rol_ext_id, rol_tc_ind, rol_title from acRole where rol_ext_id = ?";

	private static final String IS_ENTITY_ROLE = "select count(*) from acEntityRole where erl_rol_id = ?";

	private static final String GET_ROLE_ID_BY_TITLE = "select rol_id from acRole where rol_title = ?";
	
	private static final String GET_SUPER_ADMIN_ROLE_EXT_ID = "select rol_ext_id from acRole where rol_tc_ind = ? and rol_type = ? and rol_status = ? ";

	private static final String ADD_ROLE = "INSERT INTO acRole(rol_ext_id, rol_seq_id, rol_ste_ent_id, rol_url_home, rol_create_timestamp, rol_xml, rol_ste_default_ind, rol_report_ind, rol_skin_root, rol_status, rol_ste_uid, rol_target_ent_type, rol_auth_level, rol_tc_ind, rol_title, rol_type, rol_create_usr_id, rol_update_timestamp, rol_update_usr_id)\r\n" + 
			"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String ADD_ROLE_FUNCTION = "insert into acRoleFunction(rfn_rol_id,rfn_ftn_id,rfn_create_timestamp,rfn_create_usr_id) values(?,?,?,?)";

	private static final String MODIFY_ROLE = "update acRole set rol_update_timestamp = ? , rol_tc_ind = ? , rol_title = ?, rol_xml = ?, rol_update_usr_id = ?"
			+ " where rol_id = ?";

	private static final String GET_FUNCTION_ID_BY_ROLE = "select rfn_ftn_id,ftn_ext_id from acRoleFunction,acFunction where rfn_ftn_id=ftn_id and rfn_rol_id = ?";

	private static final String IS_ONLY_ROLE_TITLE = "select count(*) from acRole where rol_title = ? and rol_ste_ent_id = ?";
	
	private static final String IS_SYSTEM_ROLE = "select count(*) from acRole where rol_id = ? and rol_type = 'SYSTEM'";
	
	private static final String IS_NORMAL_ROLE = "select count(*) from acRole where rol_ext_id = ? and rol_type = ?";

	private static final String GET_MAX_ROLE_ID = "select max(rol_id) from acRole";
	
	private static final String IS_ROLE_FUNCTION = "select count(*) from acRoleFunction where rfn_rol_id = ? and rfn_ftn_id = ?";
	
	public static final String ROL_TYPE_NORMAL = "NORMAL";
	public static final String ROL_TYPE_SYSTEM = "SYSTEM";
	public static final String ROL_STATUS_OK = "OK";
	
	public void addRole(Connection con, RoleModuleParam param, loginProfile prof) throws SQLException {
		int index = 1;
		PreparedStatement stmt = con.prepareStatement(ADD_ROLE, PreparedStatement.RETURN_GENERATED_KEYS);
		rol_ext_id = "ROL"+DbAcRole.getMaxRoleId(con);
		stmt.setString(index++, rol_ext_id);
		stmt.setInt(index++, 0);
		stmt.setLong(index++, prof.root_ent_id);
		stmt.setString(index++, rol_url_home);
		stmt.setTimestamp(index++, param.getCur_time());
		rol_xml = DbAcRole.getRoleXml(rol_ext_id, param.getRol_title(), cwUtils.LANGS);
		stmt.setString(index++, rol_xml);
		stmt.setInt(index++, 0);
		stmt.setInt(index++, 0);
		stmt.setString(index++, "skin1");
		stmt.setString(index++, "OK");
		stmt.setString(index++, null);
		stmt.setString(index++, null);
		stmt.setInt(index++, 3);
		stmt.setBoolean(index++, param.getRol_tc_ind());
		stmt.setString(index++, param.getRol_title());
		stmt.setString(index++, "NORMAL");
		stmt.setString(index++, prof.usr_id);
		stmt.setTimestamp(index++, param.getCur_time());
		stmt.setString(index++, prof.usr_id);
		stmt.executeUpdate();
		rol_id = cwSQL.getAutoId(con, stmt, "acRole", "rol_id");
		stmt.close();
	}

	public long getRoleIdByTitle(Connection con, RoleModuleParam param)
			throws SQLException {
		PreparedStatement stmt = con.prepareStatement(GET_ROLE_ID_BY_TITLE);
		stmt.setString(1, param.getRol_title());
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			rol_id = rs.getLong("rol_id");
		}
		stmt.close();
		return rol_id;
	}
	
	public static String getSuperAdminRole(Connection con) throws SQLException {
		String rol_ext_id = "";
		PreparedStatement stmt = con.prepareStatement(GET_SUPER_ADMIN_ROLE_EXT_ID);
		int index =1;
		stmt.setBoolean(index++, true);
		stmt.setString(index++, ROL_TYPE_SYSTEM);
		stmt.setString(index++, ROL_STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			rol_ext_id = rs.getString("rol_ext_id");
		}
		stmt.close();
		return rol_ext_id;
	}

	public void addRoleFunction(Connection con, long rol_id, long ftn_id, int usr_ent_id)
			throws SQLException {
	    if (rol_id > 0 && ftn_id > 0) {
    		PreparedStatement stmt = con.prepareStatement(ADD_ROLE_FUNCTION);
    		stmt.setLong(1, rol_id);
    		stmt.setLong(2, ftn_id);
    		stmt.setTimestamp(3, cwSQL.getTime(con));
    		stmt.setInt(4, usr_ent_id);
    		stmt.executeUpdate();
    		stmt.close();
	    }
	}


	public void modifyRole(Connection con, RoleModuleParam param, String upd_usr_id)
			throws SQLException {
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(MODIFY_ROLE);
		int index = 1 ;
		stmt.setTimestamp(index++, cwSQL.getTime(con));
		stmt.setBoolean(index++, param.getRol_tc_ind());
		stmt.setString(index++, param.getRol_title());
		DbAcRole role = DbAcRole.getRoleById(con, param.getRol_id());
		this.rol_ext_id = role.rol_ext_id;
		rol_xml = DbAcRole.getRoleXml(role.rol_ext_id, param.getRol_title(), cwUtils.LANGS);
		stmt.setString(index++, rol_xml);
		stmt.setString(index++, upd_usr_id);
		stmt.setLong(index++, param.getRol_id());
		stmt.executeUpdate();
		stmt.close();
	}
	
	public static Vector getNormalRoleList(Connection con, long root_ent_id) throws SQLException {
		Vector result = new Vector();
		String sql = GET_ROLE_LIST + " and rol_type = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, root_ent_id);
		stmt.setString(2, ROL_TYPE_NORMAL);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			result.addElement(rs.getString("rol_ext_id"));
		}
		stmt.close();
		return result;
	}

	public Vector getRoleList(Connection con, RoleModuleParam param,loginProfile prof)
			throws SQLException {
		Vector vec = new Vector();
		if (param.getCwPage().sortCol == null || param.getCwPage().sortCol.length() == 0) {
			param.getCwPage().sortCol = "rol_title";
		}

		if (param.getCwPage().sortOrder == null
				|| param.getCwPage().sortOrder.length() == 0) {
			param.getCwPage().sortOrder = "asc";
		}

		String sql = GET_ROLE_LIST + " order by " + param.getCwPage().sortCol + "  "
				+ param.getCwPage().sortOrder;
     
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, prof.root_ent_id);
		ResultSet rs = stmt.executeQuery();
		int count = 1;
		if (param.getCwPage().pageSize <= 0) {
			param.getCwPage().pageSize = Integer.MAX_VALUE;
			// page.pageSize = cwPagination.defaultPageSize;
		}

		if (param.getCwPage().curPage <= 0) {
			param.getCwPage().curPage = 1;
		}
		while (rs.next()) {
			if ((count > (param.getCwPage().curPage - 1) * param.getCwPage().pageSize)
					&& (count <= param.getCwPage().curPage * param.getCwPage().pageSize)) {
				DbAcRole ar = new DbAcRole();
				ar.rol_id = rs.getInt("rol_id");
				ar.rol_ext_id = rs.getString("rol_ext_id");
				ar.rol_create_timestamp = rs.getTimestamp("rol_create_timestamp");
				ar.rol_tc_ind = rs.getBoolean("rol_tc_ind");
				ar.rol_title = rs.getString("rol_title");
				ar.rol_create_usr_id = rs.getString("rol_create_usr_id");
				ar.rol_type = rs.getString("rol_type");
				ar.rol_update_timestamp = rs.getTimestamp("rol_update_timestamp");
				vec.addElement(ar);
			}
			param.getCwPage().totalRec++;
			count++;
		}
		stmt.close();
		param.getCwPage().totalPage = (int) Math.ceil((float) param.getCwPage().totalRec
				/ (float) param.getCwPage().pageSize);
		return vec;
	}

	public static DbAcRole getRoleById(Connection con, long rol_id)
			throws SQLException {
		PreparedStatement stmt = con.prepareStatement(GET_ROLE_BY_ID);
		stmt.setLong(1, rol_id);
		ResultSet rs = stmt.executeQuery();
		DbAcRole ar = new DbAcRole();
		while (rs.next()) {
			ar.rol_id = rs.getLong("rol_id");
			ar.rol_ext_id = rs.getString("rol_ext_id");
			ar.rol_tc_ind = rs.getBoolean("rol_tc_ind");
			ar.rol_title = rs.getString("rol_title");
		}
		stmt.close();
		return ar;
	}
	
	public static DbAcRole getRoleByExtId(Connection con, String rol_id)
			throws SQLException {
		PreparedStatement stmt = con.prepareStatement(GET_ROLE_BY_EXT_ID);
		stmt.setString(1, rol_id);
		ResultSet rs = stmt.executeQuery();
		DbAcRole ar = new DbAcRole();
		while (rs.next()) {
			ar.rol_id = rs.getLong("rol_id");
			ar.rol_ext_id = rs.getString("rol_ext_id");
			ar.rol_tc_ind = rs.getBoolean("rol_tc_ind");
			ar.rol_title = rs.getString("rol_title");
		}
		stmt.close();
		return ar;
	}

	// delete a role from table acRole
	public void delRole(Connection con, long rol_id) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(DELETE_ROLE);
		stmt.setLong(1, rol_id);
		stmt.executeUpdate();
		stmt.close();
	}

	// delete role-function mapping from table acRoleFunction
	public void delRoleFunction(Connection con, long rol_id, String ftn_id)
			throws SQLException {
		PreparedStatement stmt = null;
		if (ftn_id == null) {
			stmt = con.prepareStatement(DEL_MUTI_ROLE_FUNCTION);
			stmt.setLong(1, rol_id);
		} else {
			stmt = con.prepareStatement(DEL_ONE_ROLE_FUNCTION);
			stmt.setLong(1, rol_id);
			stmt.setString(2, ftn_id);
		}
		stmt.executeUpdate();
		stmt.close();
	}
	
	   // delete role-function mapping from table acHomepage
    public void delRoleHomepageFunction(Connection con, String rol_ext_id)
            throws SQLException {
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(DEL_MUTI_ROLE_HOMEPAGE_FUNCTION_BY_EXT_ID);
        stmt.setString(1, rol_ext_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
    // delete role-function mapping from table acHomepage
    public void delRoleHomepageFunction(Connection con, long rol_id)
            throws SQLException {
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(DEL_MUTI_ROLE_HOMEPAGE_FUNCTION_BY_ID);
        stmt.setLong(1, rol_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void delRoleRelationTrainningCentre(Connection con, long rol_id)
                 throws SQLException{
         PreparedStatement stmt = con.prepareStatement(DEL_ROLE_RALATION_TRANING_CENTRE);
         stmt.setLong(1, rol_id);
         stmt.executeUpdate();
         stmt.close();
    }

	public boolean isEntityRole(Connection con, long rol_id)
			throws SQLException {
		int count = 1;
		PreparedStatement stmt = con.prepareStatement(IS_ENTITY_ROLE);
		stmt.setLong(1, rol_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
		}
		stmt.close();
		return count != 0 ? true : false;
	}

	public Vector getFunctionIdByRole(Connection con, long rol_id)
			throws SQLException {
		Vector vec = new Vector();
		PreparedStatement stmt = con.prepareStatement(GET_FUNCTION_ID_BY_ROLE);
		stmt.setLong(1, rol_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
		    Vector v = new Vector();
			v.addElement(rs.getString("rfn_ftn_id"));
			v.addElement(rs.getString("ftn_ext_id"));
			vec.addElement(v);
		}
		stmt.close();
		return vec;
	}

    public boolean isOnlyRoleTitle(Connection con, String rol_title,
            loginProfile prof) throws SQLException {
        int count = 1;
        PreparedStatement stmt = con.prepareStatement(IS_ONLY_ROLE_TITLE);
        stmt.setString(1, rol_title);
        stmt.setLong(2, prof.root_ent_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            count = rs.getInt(1);
        }
        stmt.close();
        return count == 0 ? true : false;
    }

	
	public boolean isSystemRole(Connection con, long rol_id)
			throws SQLException {
		int count = 1;
		PreparedStatement stmt = con.prepareStatement(IS_SYSTEM_ROLE);
		stmt.setLong(1, rol_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
		}
		stmt.close();
		return count != 0 ? true : false;
	}
	
	public static boolean isNormalRole(Connection con, String rol_ext_id)
			throws SQLException {
		int count = 1;
		PreparedStatement stmt = con.prepareStatement(IS_NORMAL_ROLE);
		stmt.setString(1, rol_ext_id);
		stmt.setString(2, ROL_TYPE_NORMAL);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
		}
		stmt.close();
		return count != 0 ? true : false;
	}
	
	public static String getRoleXml(String ext_id, String title, String[] langs) {
		String xml = "";
		if (langs != null){
			xml += "<role id=\"" + cwUtils.esc4XML(ext_id) + "\">";
			for(int i=0;i<langs.length;i++) {
				xml += "<desc lan=\"" + langs[i] + "\" name=\"" + cwUtils.esc4XML(title) + "\"/>";
			}
			xml += "</role>";
		}
		return xml;
	}
	
	public static long getMaxRoleId(Connection con)
			throws SQLException {
		long max_rol_id = 0;
		PreparedStatement stmt = con.prepareStatement(GET_MAX_ROLE_ID);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			max_rol_id = rs.getLong(1);
		}
		stmt.close();
		return max_rol_id;
} 
	
	public boolean isRoleFunction(Connection con, long rol_id,String ftn_ext_id)
	throws SQLException {
		int count = 1;
		PreparedStatement stmt = con.prepareStatement(IS_ROLE_FUNCTION);
		stmt.setLong(1, rol_id);
		stmt.setString(2, ftn_ext_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
		}
		stmt.close();
		return count != 0 ? true : false;
	}
	
	public void delOtherFunction(Connection con, long rol_id) throws SQLException {
	    if (DbAcReportTemplate.checkExist(con, rol_id)) {
	        DbAcReportTemplate.delReportTemplateForRole(con, rol_id);
	    }
	}

   public void insReportACL(Connection con, String rol_ext_id, Timestamp cur_time, String upd_usr_id, long root_ent_id) throws SQLException {
      
            DbAcReportTemplate.insReportForRole(con, rol_ext_id, cur_time, upd_usr_id, root_ent_id);
       
    }
   
   private static final String sql_get_roles_can_login = "select rol_ext_id, rol_type,rol_title,rol_seq_id From acRole "
       + "where exists (select * from acEntityRole where erl_ent_id = ? and erl_eff_start_datetime <= ? and erl_eff_end_datetime >= ? "
       + "and rol_id = erl_rol_id) and rol_status = ? order by rol_seq_id";

   // need get title for user defined roles
   public static List getRolesCanLogin(Connection con, long ent_id,
           Timestamp cur_time) throws SQLException {
       List lst = new ArrayList();
       PreparedStatement stmt = con.prepareStatement(sql_get_roles_can_login);
       stmt.setLong(1, ent_id);
       stmt.setTimestamp(2, cur_time);
       stmt.setTimestamp(3, cur_time);
       stmt.setString(4, DbAcRole.ROL_STATUS_OK);
       ResultSet rs = stmt.executeQuery();
       while (rs.next()) {
           DbAcRole rol = new DbAcRole();
           rol.rol_ext_id = rs.getString("rol_ext_id");
           rol.setRol_type(rs.getString("rol_type"));
           rol.setRolTitle(rs.getString("rol_title"));
           lst.add(rol);
       }
       stmt.close();
       return lst;
   }
   
   public static String getUsrName(Connection con, long ent_id) throws SQLException {
       String name = "";
       String sql = "select usr_ste_usr_id from RegUser where usr_ent_id = ?";
       PreparedStatement stmt = con.prepareStatement(sql);
       stmt.setLong(1, ent_id);
       ResultSet rs = stmt.executeQuery();
       while (rs.next()) {
           name = rs.getString("usr_ste_usr_id");
       }
       stmt.close();
       return name;
   }
		
	public Timestamp getRolcreateTimestamp() {
		return rol_create_timestamp;
	}

	public void setRolcreateTimestamp(Timestamp rol_create_timestamp) {
		this.rol_create_timestamp = rol_create_timestamp;
	}

	public String getRolExtId() {
		return rol_ext_id;
	}

	public long getRolId() {
		return rol_id;
	}

	public void setRolId(long rol_id) {
		this.rol_id = rol_id;
	}

	public String getRolTitle() {
		return rol_title;
	}

	public void setRolTitle(String rol_title) {
		this.rol_title = rol_title;
	}

	public boolean getRol_tc_ind() {
		return rol_tc_ind;
	}

	public void setRol_tc_ind(boolean rol_tc_ind) {
		this.rol_tc_ind = rol_tc_ind;
	}

	public String getRol_create_usr_id() {
		return rol_create_usr_id;
	}

	public void setRol_create_usr_id(String rol_create_usr_id) {
		this.rol_create_usr_id = rol_create_usr_id;
	}

	public Timestamp getRol_update_timestamp() {
		return rol_update_timestamp;
	}

	public void setRol_update_timestamp(Timestamp rol_update_timestamp) {
		this.rol_update_timestamp = rol_update_timestamp;
	}

    public String getRol_type() {
        return rol_type;
    }

    public void setRol_type(String rolType) {
        rol_type = rolType;
    }
    
    
    public static String getRoleExtId(Connection con, String rol_ste_uid, long root_id) throws SQLException {
        String rol_ext_id = null;
        PreparedStatement stmt = con.prepareStatement("select rol_ext_id from acrole where rol_ste_uid= ? and rol_ste_ent_id = ? and rol_status = ?");
        int index =1;
        stmt.setString(index++, rol_ste_uid);
        stmt.setLong(index++, root_id);
        stmt.setString(index++, ROL_STATUS_OK);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            rol_ext_id = rs.getString("rol_ext_id");
        }
        stmt.close();
        return rol_ext_id;
    }
}