package com.cw.wizbank.JsonMod.role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbAcReportTemplate;
import com.cw.wizbank.db.DbAcRole;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.config.system.functionmap.FunctionListType;
import com.cw.wizbank.config.system.functionmap.FunctionType;

public class RoleManager {
    public Vector del_fail_rol;

    public String getRoleListAsXml(Connection con, RoleModuleParam param,loginProfile prof)
            throws SQLException, qdbException, cwSysMessage {
        DbAcRole db = new DbAcRole();
        Vector vec = db.getRoleList(con, param,prof);
        StringBuffer sb = new StringBuffer(1024);
        sb.append("<role_list>").append(dbUtils.NEWL);
        for (int i = 0; i < vec.size(); i++) {
            DbAcRole role = (DbAcRole) vec.get(i);
            sb.append(
                    "<role id=\"" + role.getRolId() + "\" ext_id=\""
                            + dbUtils.esc4XML(role.getRolExtId()) + "\" rol_creator=\""
                            + dbUtils.esc4XML(dbRegUser.getUserName(con, role.getRol_create_usr_id())) + "\" modified_date=\""
                            + role.getRol_update_timestamp()+ "\" rol_tc_ind=\""
                            + role.getRol_tc_ind()
                            + "\" rol_type=\"" + role.getRol_type()
                            + "\">")
                    .append(dbUtils.NEWL);
            sb.append("<role_title name=\"" + dbUtils.esc4XML(role.getRolTitle()) + "\"/>")
                    .append(dbUtils.NEWL);
            sb.append("</role>").append(dbUtils.NEWL);
        }
        sb.append(
                "<pagination page_size=\"" + param.getCwPage().pageSize
                        + "\" cur_page=\"" + param.getCwPage().curPage
                        + "\" total_rec=\"" + param.getCwPage().totalRec
                        + "\" sort_col=\"" + param.getCwPage().sortCol
                        + "\" sort_order=\"" + param.getCwPage().sortOrder + "\"/>")
                .append(dbUtils.NEWL);
        sb.append("</role_list>").append(dbUtils.NEWL);
        return sb.toString();
    }

    public String getFunctionByRoleAsXml(Connection con, long rol_id)
            throws SQLException {
        StringBuffer sb = new StringBuffer();
        DbAcRole db = new DbAcRole();
        Vector vec = db.getFunctionIdByRole(con, rol_id);
        sb.append("<function_list>").append(dbUtils.NEWL);
        for (int i = 0; i < vec.size(); i++) {
        	Vector func = (Vector)vec.get(i);
            sb.append("<function id=\"").append(func.get(1))
              .append("\" ext_id=\"").append(func.get(0)).append("\">").append(dbUtils.NEWL);
            sb.append("</function>").append(dbUtils.NEWL);
        }
        sb.append("</function_list>").append(dbUtils.NEWL);
        return sb.toString();
    }

    public String getRoleByIdAsXml(Connection con, long rol_id)
            throws SQLException {
        StringBuffer sb = new StringBuffer();
        DbAcRole role = DbAcRole.getRoleById(con, rol_id);
        sb.append(
                "<role id=\""
                        + role.getRolId()
                        + "\" ext_id=\""
                        + dbUtils.esc4XML(role.getRolExtId())
                        + "\" rol_tc_ind=\"" + role.getRol_tc_ind()
                        + "\">")
                .append(dbUtils.NEWL);
        sb.append("<role_title name=\"" + dbUtils.esc4XML(role.getRolTitle()) + "\"/>").append(
                dbUtils.NEWL);
        sb.append(getFunctionByRoleAsXml(con, rol_id));
        sb.append("</role>").append(dbUtils.NEWL);
        return sb.toString();
    }
    
    public String getRoleTitleByExtId(Connection con, String rol_ext_id)
            throws SQLException {
        DbAcRole role = DbAcRole.getRoleByExtId(con, rol_ext_id);
        return null==role?null:role.rol_title;
    }

    public boolean delRole(Connection con, long[] rol_id_lst)
            throws SQLException {
        boolean isDelRoleSuccess = true;
        del_fail_rol = new Vector();
        DbAcRole db = new DbAcRole();
        for (int i = 0; i < rol_id_lst.length; i++) {
            if (!db.isEntityRole(con, rol_id_lst[i]) && !db.isSystemRole(con, rol_id_lst[i])) {
                db.delRoleFunction(con, rol_id_lst[i], null);
                db.delRoleHomepageFunction(con, rol_id_lst[i]);
                db.delRoleRelationTrainningCentre(con, rol_id_lst[i]);
                db.delOtherFunction(con, rol_id_lst[i]);
                db.delRole(con, rol_id_lst[i]);
            } else {
                isDelRoleSuccess = false;
                String rol_title = DbAcRole.getRoleById(con, rol_id_lst[i]).getRolTitle();
                if (rol_title != null) {
                    del_fail_rol.add(rol_title);
                }
            }
        }
        return isDelRoleSuccess;
    }
    
    public boolean isOnlyRoleTitle(Connection con, RoleModuleParam param,loginProfile prof) throws SQLException{
        boolean isOnlyRoleTitle = true;
        DbAcRole db = new DbAcRole();
        if(!db.isOnlyRoleTitle(con, param.rol_title,prof)){
            isOnlyRoleTitle = false;
        }
        return  isOnlyRoleTitle;
    }
    
    public void addRole(Connection con, RoleModuleParam param,loginProfile prof, WizbiniLoader wizbini)
            throws SQLException {
        DbAcRole db = new DbAcRole();
        db.addRole(con, param,prof);
        param.rol_id = db.rol_id;
        String[] ftn_id_lst = cwUtils.splitToString(param.ftn_id_lst, "~");
        Vector vec_fun = getAllRelatedFunction(con,ftn_id_lst, wizbini);
        for (int i = 0; i < vec_fun.size(); i++) {
            db.addRoleFunction(con, param.rol_id, ((Long)vec_fun.get(i)).longValue(), param.getEnt_id());
        }
        if (db.rol_ext_id != null && db.rol_ext_id.length() > 0) {
            insAcHomePageFtn(con, db.rol_ext_id);
        }
        db.insReportACL(con, db.rol_ext_id, param.getCur_time(), prof.usr_id, prof.root_ent_id);
        LangLabel.setCustomizedRoleTitle(db.rol_ext_id, param.getRol_title());
    }
    
    public boolean isModifyOnlyRoleTitle(Connection con, RoleModuleParam param,loginProfile prof) throws SQLException{
        boolean isModifyOnlyRoleTitle = true;
        DbAcRole db = new DbAcRole();
        DbAcRole role = DbAcRole.getRoleById(con, param.rol_id);
        if(!param.rol_title .equalsIgnoreCase(role.getRolTitle()) ){
            if(!db.isOnlyRoleTitle(con, param.rol_title,prof)){
                isModifyOnlyRoleTitle = false;
            }
        }
        return  isModifyOnlyRoleTitle;
    }
    
    public boolean isSystemRole(Connection con, RoleModuleParam param)
            throws SQLException {
        boolean isSystemRole = true;
        DbAcRole db = new DbAcRole();
        if (!db.isSystemRole(con, param.rol_id)) {
            isSystemRole = false;
        }
        return isSystemRole;
    }

    public void modifyRole(Connection con, RoleModuleParam param,WizbiniLoader wizbini, loginProfile prof) throws SQLException{
        DbAcRole db = new DbAcRole();
        db.modifyRole(con, param, prof.usr_id);
        db.delRoleFunction(con, param.rol_id, null);
        db.delRoleHomepageFunction(con, param.rol_ext_id);
        db.delOtherFunction(con, param.rol_id);
        DbAcReportTemplate.delReportTemplateForRole(con, param.getRol_ext_id());
        String[] ftn_id_lst = cwUtils.splitToString(param.ftn_id_lst, "~");
        Vector vec = getAllRelatedFunction(con,ftn_id_lst,wizbini);
        for (int i = 0; i < vec.size(); i++) {
            db.addRoleFunction(con, param.rol_id, ((Long)vec.get(i)).longValue(), param.getEnt_id());
        }
        if (db.rol_ext_id != null && db.rol_ext_id.length() > 0) {
            insAcHomePageFtn(con, db.rol_ext_id);
        }
        LangLabel.setCustomizedRoleTitle(db.rol_ext_id, param.getRol_title());
        db.insReportACL(con, param.rol_ext_id, param.getCur_time(), prof.usr_id, prof.root_ent_id);
    }
    
    public Vector getAllRelatedFunction(Connection con, String[] ftn_id_lst, WizbiniLoader wizbini)
            throws SQLException {
        Vector vec = new Vector();
        for (int i = 0; i < ftn_id_lst.length; i++) {
            vec.add(new Long(getFtnIdByFtnExtId(con,ftn_id_lst[i])));
            if(getFunctionById(ftn_id_lst[i], wizbini) != null) {
                FunctionType ftn = (FunctionType)getFunctionById(ftn_id_lst[i], wizbini);
                List hidden_list = ftn.getHiddenFunctions();
                FunctionListType ftn_list_type = null;
                if(hidden_list != null) {
                    for(Iterator itr=hidden_list.iterator();itr.hasNext();){
                        ftn_list_type = (FunctionListType)itr.next();
                        List ftn_list = ftn_list_type.getFunction();
                        for(Iterator itr1=ftn_list.iterator();itr1.hasNext();){
                            FunctionType hidden_ftn = (FunctionType)itr1.next();
                            if(!vec.contains(hidden_ftn.getId())){
                                vec.add(new Long(getFtnIdByFtnExtId(con,hidden_ftn.getId())));
                            }
                        }
                    }
                }
            }
        }
        cwUtils.removeDuplicate(vec);
        return vec;
    }

    public FunctionType getFunctionById(String ftn_id, WizbiniLoader wizbini) {
        List ftn_list = wizbini.cfgSysFunctionMap.getHomepageFunction();
        FunctionType ftn = null;
        FunctionType my_ftn = null;
        for(Iterator itr=ftn_list.iterator();itr.hasNext();){
            ftn = (FunctionType)itr.next();
            if (ftn.getId().equals(ftn_id)) {
                my_ftn = ftn;
                break;
            } else {
                my_ftn = getFunctionById(ftn_id, ftn);
                if(my_ftn != null) {
                    break;
                }
            }
        }
        if(my_ftn != null) {
            return my_ftn;
        } else {
            List list = wizbini.cfgSysFunctionMap.getOtherFunctions();
            FunctionListType other_ftn_list_type = null;
            FunctionType other_ftn = null;
            for(Iterator itr=list.iterator();itr.hasNext();){
                other_ftn_list_type = (FunctionListType)itr.next();
                List other_ftn_list = other_ftn_list_type.getFunction();
                for(Iterator itr1=other_ftn_list.iterator();itr1.hasNext();){
                    other_ftn = (FunctionType)itr1.next();
                    if (other_ftn.getId().equals(ftn_id)) {
                        return other_ftn;
                    }
                }
            }
            return null;
        }
    }

    public FunctionType getFunctionById(String ftn_id, FunctionType cur_ftn) {
        FunctionListType ftn_list_type = null;
        List child_list = cur_ftn.getChildFunctions();
        for(Iterator itr=child_list.iterator();itr.hasNext();){
            ftn_list_type = (FunctionListType)itr.next();
            List ftn_list = ftn_list_type.getFunction();
            for(Iterator itr1=ftn_list.iterator();itr1.hasNext();){
                FunctionType child_ftn = (FunctionType)itr1.next();
                if(child_ftn.getId().equals(ftn_id)) {
                    return child_ftn;
                }
                if(child_ftn.getChildFunctions() != null && child_ftn.getChildFunctions().size() != 0){
                    FunctionType my_ftn = getFunctionById(ftn_id, child_ftn);
                    if(my_ftn != null){ 
                        return my_ftn;
                    }
                }
            }
        }
        return null;
    }
    
    private static final String sql_get_ftn_id = "select ftn_id from acFunction where ftn_ext_id = ?"; 
    public long getFtnIdByFtnExtId(Connection con,String ftn_ext_id) throws SQLException {
        long ftn_id = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt= con.prepareStatement(sql_get_ftn_id);
            pstmt.setString(1, ftn_ext_id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ftn_id = rs.getLong(1);
            }
        } finally {
            if (pstmt != null) pstmt.close();
        }
        return ftn_id;
    }
    
    private static final String sql_ins_ac_hom_ftn = "insert into acHomePage(Ac_Hom_Rol_Ext_Id,Ac_Hom_Ftn_Ext_Id,ac_hom_create_usr_id,Ac_Hom_Create_Timestamp)" + 
    		"select rol_ext_id,ftn_ext_id,rfn_create_usr_id,rfn_create_timestamp from acFunction,acRole,acRoleFunction " + 
    		"where rfn_ftn_id = ftn_id and rfn_rol_id = rol_id and rol_ext_id = ? and ftn_type = ?"; 
    public void insAcHomePageFtn(Connection con,String rol_ext_id) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt= con.prepareStatement(sql_ins_ac_hom_ftn);
            pstmt.setString(1, rol_ext_id);
            pstmt.setString(2, "HOMEPAGE");
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
}
