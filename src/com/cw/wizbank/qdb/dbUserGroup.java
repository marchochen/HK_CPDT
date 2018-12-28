package com.cw.wizbank.qdb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.accesscontrol.AcUserGroup;
import com.cw.wizbank.accesscontrol.AccessControlManager;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeCatalogAccess;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemLesson;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbRoleTargetEntity;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;

public class dbUserGroup extends dbEntity implements Serializable
{
    public static final String USG_ROLE_ROOT       = "ROOT";
    public static final String USG_ROLE_SYSTEM     = "SYSTEM";

    // Retire soon
//    public static final String USG_ROLE_ADMIN      = "ADMIN";
//    public static final String USG_ROLE_TEACHER    = "TEACHER";
//    public static final String USG_ROLE_STUDENT    = "STUDENT";


    public final static String COMMA               = ",";
/*
    public static final String GPM_TYPE_USR_PARENT_USG = "USR_PARENT_USG";
    public static final String GPM_TYPE_USG_PARENT_USG = "USG_PARENT_USG";
    public static final String GPM_TYPE_USR_CURRENT_UGR = "USR_CURRENT_UGR";
    public static final String GPM_TYPE_UGR_PARENT_UGR = "UGR_PARENT_UGR";
    public static final String GPM_TYPE_IDC_PARENT_IDC = "IDC_PARENT_IDC";
    public static final String GPM_TYPE_USR_INTEREST_IDC = "USR_INTEREST_IDC";
    public static final String GPM_TYPE_USR_FOCUS_IDC = "USR_FOCUS_IDC";
*/
    // session keys
//    public static final String USG_SEARCH_ENT_ID    = "usg_search_ent_id";
    public static final String USG_SEARCH_TIMESTAMP = "usg_search_timestamp";
    public static final String USG_SEARCH_ENT_LIST  = "usg_search_ent_list";
    public static final String USG_SEARCH_SORT_BY = "usg_search_sort_by";
    public static final String USG_SEARCH_ORDER_BY  = "usg_search_order_by";

    public static final int USG_SEARCH_PAGE_SIZE = 10;

    public static final String[] USG_SEARCH_SORT_ORDER = {"usr_display_bil", "usr_signup_date","usr_ste_usr_id"};

    public static final String LOST_FOUND       = "RESIGNED";

    public String   usg_id;
    public String   transaction;

    public long    usg_ent_id;
    public String  usg_code;
    public String  usg_name;
    public String  usg_display_bil;
    public String  usg_level;
    public String  usg_usr_id_admin;
    public String  usg_role;
    public long    usg_ent_id_root;
    public String  usg_desc;

    // search component
    public String s_usr_id;
    public String s_usr_email;
    public String s_usr_email_2;
    public String s_usr_initial_name_bil;
    public String s_usr_last_name_bil;
    public String s_usr_first_name_bil;
    public String s_usr_display_bil;
    public String s_usr_nickname;
    public String s_usr_gender;
    public Timestamp s_usr_bday_fr;
    public Timestamp s_usr_bday_to;
    public Timestamp s_usr_jday_fr;
    public Timestamp s_usr_jday_to;
    public String s_usr_source;
    public Vector s_ext_col_names;
    public Vector s_ext_col_types;
    public Vector s_ext_col_values;
//    public String s_usr_bplace_bil;
    public String s_usr_hkid;
    public String s_usr_tel;
    public String s_usr_fax;
    public String s_usr_address_bil;
    public String s_usr_postal_code_bil;
    public String[] s_role_types;
    public String s_ftn_ext_id;
    public String s_rol_ext_id;
    public String s_instr;
    public String s_itm_code;
    public String s_itm_title;
    public boolean s_is_whole_word_match_itm_title = false;
    public String s_ils_title;
    public boolean s_is_whole_word_match_ils_title = false;
    public int ils_id=0;
    public boolean remove_ils_instr=false;
    public String[] s_status;
    public long s_idc_fcs;
    public long s_idc_int;
    public long s_grade;
    public Timestamp s_timestamp;
    public String s_sort_by;
    public String s_order_by;
    public long itm_id = 0;
    public long s_itm_id;
    public String s_search_enrolled;
    public long s_target_usr_ent_id;
    public long s_appr_ent_id;
    public String s_appr_rol_ext_id;
    public String[] s_usg_ent_id_lst;
    public String s_usr_id_display_bil;
    public String s_usr_job_title;
    public long s_tcr_id;
    //add for new user_search ---- start
    //by richard
    //if null use original logic
    public String[] usr_srh_col_lst;
    public String[] usr_srh_value_lst;
    //add for new user_search ---- end

    // for department budget
    public int usg_budget = -1;
    public boolean filter_user_group_ind;
    public boolean tc_enabled;

    public static final String MY_APPROVAL_GROUP = "my_approval";  //indicate i want to search my approval groups in user search
    private static final String SESS_USG_ENT_LST = "sess_usg_ent_lst_";
    private static final String HASH_GROUP_ID = "group_id_";
    private static final String HASH_TIMESTAMP = "timestamp_";
    private static final String HASH_ENT_ID_VEC = "ent_id_vec_";
    private static final String HASH_TOTAL      = "total_";

    public static final String[] defSearchStatus = {dbRegUser.USR_STATUS_OK};


    public dbUserGroup() {;}

    /**
     * updated for department budget - Emily, 20020828
     * @param con
     * @throws qdbException
     */
    public void get(Connection con) throws qdbException {
        try {
            ent_id = usg_ent_id;

            super.get(con);
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT"
                    + "  usg_code"
                    + ", usg_name"
                    + ", usg_display_bil"
                    // for department level
                    + ", usg_budget"
                    + ", usg_level"
                    + ", usg_usr_id_admin"
                    + ", usg_role"
                    + ", usg_ent_id_root"
                    + ", usg_desc "
                    + "FROM UserGroup where usg_ent_id = ? ");

            stmt.setLong(1, usg_ent_id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usg_code        = rs.getString("usg_code");
                usg_name        = rs.getString("usg_name");
                usg_display_bil = rs.getString("usg_display_bil");
                // for department level
                this.usg_budget = rs.getInt("usg_budget");
                usg_level       = rs.getString("usg_level");
                usg_usr_id_admin = rs.getString("usg_usr_id_admin");
                usg_role        = rs.getString("usg_role");
                usg_ent_id_root = rs.getLong("usg_ent_id_root");
                usg_desc = rs.getString("usg_desc");
            } else {
                stmt.close();
                throw new qdbException("Failed to get group information.");
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public long getEntIdBySteUid(Connection con)
            throws qdbException
    {
        try{
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT "
                    + " usg_ent_id "
                    + " FROM UserGroup, Entity "
                    + " where ent_id = usg_ent_id "
                    + " AND ent_ste_uid = ? "
                    + " AND (usg_ent_id_root = ? or usg_ent_id = ? )"
                    + " AND ent_delete_usr_id IS NULL "
                    + " AND ent_delete_timestamp IS NULL ");

            stmt.setString(1, ent_ste_uid);
            stmt.setLong(2, usg_ent_id_root);
            stmt.setLong(3, usg_ent_id_root);

            ResultSet rs = stmt.executeQuery();

            if( rs.next()) {
                usg_ent_id = rs.getLong("usg_ent_id");
            }else {
//                throw new qdbException("Failed to get group information.");
            }
            stmt.close();
//            get(con);
            return usg_ent_id;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void ins(Connection con, loginProfile prof, long parent_id)
            throws qdbException, qdbErrMessage
    {
        try {
            ins(con, parent_id, prof.usr_id);
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    /************************************************/
    // ins(con, parent_id)  is divided into ins(con) and then insGroup(con, parent_id) by kim
    public void ins(Connection con, long parent_id, String usr_id)
            throws qdbException, qdbErrMessage, SQLException
    {
        this.ins(con);
        insEntityRelation(con, parent_id, usr_id);
        con.commit();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        entityfullpath.enclose(con, this.usg_ent_id);
    }

    /**
     * updated for department budget - Emily, 20020828
     * @param con
     * @throws qdbException
     * @throws qdbErrMessage
     */
    public void ins(Connection con) throws qdbException, qdbErrMessage {
        try {
            // calls dbEntity.ins()
            super.ins(con);
            // if ok.
            usg_ent_id = ent_id;
            if (checkUIdDuplicate(con, usg_ent_id_root)) {
                throw new qdbErrMessage("USG008", ent_ste_uid);
            }

            String sqlStr = null;
            sqlStr = "INSERT INTO UserGroup"
                   + " (usg_ent_id, usg_code, usg_name, usg_display_bil, usg_desc, ";
            if (this.usg_budget > 0) {
                sqlStr += "usg_budget, ";
            }
            sqlStr += "usg_level, usg_usr_id_admin, usg_role, usg_ent_id_root) "
                    + "VALUES (?, ?, ?, ?, ?, ";
            if (this.usg_budget > 0) {
                sqlStr += "?, ";
            }
            sqlStr += "?, NULL, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(sqlStr);

            int i = 1;
            stmt.setLong(i++, usg_ent_id);
            stmt.setString(i++, usg_code);
            stmt.setString(i++, usg_name);
            stmt.setString(i++, usg_display_bil);
            stmt.setString(i++, usg_desc);
            // for department budget
            if (this.usg_budget > 0) {
                stmt.setInt(i++, this.usg_budget);
            }
            stmt.setString(i++, usg_level);
            stmt.setString(i++, usg_role);
            stmt.setLong(i++, usg_ent_id_root);

            int stmtResult = stmt.executeUpdate();

            stmt.close();
            if (stmtResult != 1) {
                throw new qdbException("Failed to insert user group.");
            }
        } catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    public void insEntityRelation(Connection con, long ancestor_id, String usr_id) throws qdbException, SQLException{

    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_ancestor_ent_id = ancestor_id;
    	dbEr.ern_child_ent_id = usg_ent_id;
    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
    	dbEr.insEr(con, usr_id);

    }
    /************************************************/

    /**
     * updated for department budget - Emily, 20020828
     * @param con
     * @param prof
     * @throws qdbException
     * @throws qdbErrMessage
     */
    public void upd(Connection con, loginProfile prof)
            throws qdbException, qdbErrMessage {
        try {
            String oldDisplayName = (String)(dbUserGroup.getDisplayName(con, cwUtils.array2list( new long[]{usg_ent_id}))).get(new Long(usg_ent_id).toString());


            if (checkUIdDuplicate(con, prof.root_ent_id)) {
                throw new qdbErrMessage("USG008", ent_ste_uid);
            }

            super.checkTimeStamp(con);

            String sqlStr = null;
            sqlStr = "UPDATE UserGroup SET"
                   + " usg_code = ?,"
                   + " usg_name= ?,"
                   + " usg_display_bil= ?,"
                   + " usg_desc = ?,";
            if (this.usg_budget > 0) {
                sqlStr += " usg_budget = ?,";
            }
            sqlStr += " usg_level = ?,"
                    + " usg_role = ? "
                    + "WHERE usg_ent_id = ?";

            PreparedStatement stmt = con.prepareStatement(sqlStr);

            int i = 1;

            stmt.setString(i++, usg_code);
            stmt.setString(i++, usg_name);
            stmt.setString(i++, usg_display_bil);
            stmt.setString(i++, usg_desc);
            if (this.usg_budget > 0) {
                stmt.setInt(i++, this.usg_budget);
            }
            stmt.setString(i++, usg_level);
            stmt.setString(i++, usg_role);
            stmt.setLong(i++, usg_ent_id);

            int stmtResult = stmt.executeUpdate();
            stmt.close();

            if (stmtResult!=1) {
                // update fails
                throw new qdbException("Fails to update user group record.");
            }

            // update the timestamp of the entity

            super.upd(con);

            super.updUid(con);

            if (!usg_display_bil.equals(oldDisplayName)){
            	con.commit();
            	EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            	entityfullpath.updateChildFullPath(con, usg_ent_id, usg_display_bil, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return;
    }

    public void paste(Connection con, loginProfile prof, long parent_id)
            throws qdbException, qdbErrMessage, cwException
    {
        try {
            String ern_type;
            dbEntity dbent = new dbEntity();
            dbent.ent_id = usg_ent_id;
            dbent.getById(con);
            if (dbent.ent_type.equalsIgnoreCase(ENT_TYPE_USER_GROUP)) {
            	ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
                if (isAncester( con, parent_id, usg_ent_id))
                    // Looping
                    throw new qdbErrMessage("USG003");
            }
            else {
            	ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
            }

            PreparedStatement stmt0 = con.prepareStatement("select usr_ste_usr_id, usr_status "
            		+ " ,usr_app_approval_usg_ent_id, ent_delete_timestamp "
            		+ " from RegUser, Entity "
            		+ " where usr_ent_id = ent_id and usr_ent_id = ? and usr_ste_usr_id is not null");
            stmt0.setLong(1, usg_ent_id);
            ResultSet rs0 = stmt0.executeQuery();
            String usr_ste_usr_id = null;
            String usr_status = null;
            long usr_app_approval_usg_ent_id = 0;
            Timestamp deleteTime = null;

            if (rs0.next()) {
                usr_ste_usr_id = rs0.getString("usr_ste_usr_id");
                usr_status = rs0.getString("usr_status");
                usr_app_approval_usg_ent_id = rs0.getLong("usr_app_approval_usg_ent_id");
                deleteTime = rs0.getTimestamp("ent_delete_timestamp");
            }

            stmt0.close();

            if (usr_ste_usr_id == null) {
                throw new qdbErrMessage("USG009");
            }
            //check the user's Highest Approval Group is in the gpm_ancester of the new group
           // if(usr_app_approval_usg_ent_id > 0 && !dbUserGroup.isAncester(con,parent_id,usr_app_approval_usg_ent_id)){
           // 	throw new qdbErrMessage("GEN000");
            //}
            if(usr_status!=null && usr_status.equalsIgnoreCase(dbRegUser.USR_STATUS_DELETED)) {
                dbRegUser regUser = new dbRegUser();
                regUser.usr_ste_usr_id = usr_ste_usr_id;
                if (regUser.checkSiteUsrIdExist(con, prof.root_ent_id)) {
                    throw new qdbErrMessage("USR001", regUser.usr_ste_usr_id);
                }
            }

            PreparedStatement stmt = con.prepareStatement(
                    " SELECT ern_ancestor_ent_id from EntityRelation "
                    + " where ern_ancestor_ent_id = ? and ern_child_ent_id = ? and ern_type = ? "
                    + " and ern_parent_ind = ? ");

            stmt.setLong(1, parent_id);
            stmt.setLong(2, usg_ent_id);
            stmt.setString(3, ern_type);
            stmt.setBoolean(4, true);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                dbEntityRelation dbEr = new dbEntityRelation();
                dbEr.ern_ancestor_ent_id = parent_id;
                dbEr.ern_child_ent_id = usg_ent_id;
                dbEr.ern_type = ern_type;
                dbEr.insEr(con, prof.usr_id);

                // recover the delete ugr/idc record
                if(usr_status.equalsIgnoreCase(dbRegUser.USR_STATUS_DELETED)) {
                	dbEr.restoreEntityRelation(con, prof.usr_id, usg_ent_id, deleteTime, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
                	dbEr.restoreEntityRelation(con, prof.usr_id, usg_ent_id, deleteTime, dbEntityRelation.ERN_TYPE_USR_INTEREST_IDC);
                	dbEr.restoreEntityRelation(con, prof.usr_id, usg_ent_id, deleteTime, dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC);
                }

            }
            stmt.close();
            dbent.unDelete(con);
            // check if the entity is user
            // if so, set the user status to OK
            PreparedStatement stmt2 = con.prepareStatement(
                    " SELECT count(*) from RegUser "
                    + " where usr_ent_id = ? ");

            stmt2.setLong(1, usg_ent_id);

            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) {
                int cnt = rs2.getInt(1);
                if (cnt == 1) {
                    dbRegUser dbusr = new dbRegUser();
                    dbusr.ent_id = usg_ent_id;
                    dbusr.usr_ent_id = usg_ent_id;
                    dbusr.usr_ste_ent_id = prof.root_ent_id;
                    dbusr.changeStatus_Cut(con,dbRegUser.USR_STATUS_OK);
                }
            }else {
                stmt2.close();
                throw new qdbException("Failed to check user record.");
            }
            stmt2.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // retired, replace by soft-delete entity
    public static long getLostFoundID(Connection con, long rootEntId)
            throws qdbException
    {
        try {
            /*
            dbUserGroup dbusg = new dbUserGroup();
            dbusg.usg_ent_id = entId;
            dbusg.get(con);

            if (dbusg.usg_ent_id_root <= 0 && dbusg.usg_role.equalsIgnoreCase(USG_ROLE_ROOT)) {
                dbusg.usg_ent_id_root = entId;
            }
            */
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT usg_ent_id from UserGroup WHERE "
                    + " usg_role = ? and usg_ent_id_root = ?" );

            stmt.setString(1,USG_ROLE_SYSTEM);
            //stmt.setLong(2,dbusg.usg_ent_id_root);
            stmt.setLong(2,rootEntId);

            ResultSet rs = stmt.executeQuery();
            long usgEntId = 0;
            if (rs.next()) {
                usgEntId = rs.getLong("usg_ent_id");
            }else {
                stmt.close();
                throw new qdbException("Fails to get lost&found id.");
            }
            stmt.close();
            return usgEntId;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public static boolean isPulicAdmin(Connection con, long rootId)
            throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT usg_display_bil from UserGroup WHERE "
                    + "  usg_ent_id = ?" );

            stmt.setLong(1,rootId);

            ResultSet rs = stmt.executeQuery();
            boolean isPublic = false;
            if (rs.next()) {
                String val = rs.getString("usg_display_bil");

                if (val.equalsIgnoreCase(dbResource.RES_PRIV_CW))
                    isPublic = true;
            }else {
                stmt.close();
                throw new qdbException("Fails to check organization.");
            }
            stmt.close();
            return isPublic;
        }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    //look at EntityRelation to check if this group is parent of other groups/users
    //pre-set var: usg_ent_id
    private boolean hasChild(Connection con) throws SQLException {
        final String SQL = " Select count(*) From EntityRelation "
                         + " Where ern_ancestor_ent_id = ? "
                         + " And (ern_type = ? or ern_type = ?)";
        long cnt = 0;
        boolean result;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_ent_id);
        stmt.setString(2, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
        stmt.setString(3, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            cnt = rs.getLong(1);

        stmt.close();
        if(cnt>0)
            result = true;
        else
            result = false;
        return result;
    }

    public static void trashGroups(Connection con, loginProfile prof, long[] usg_ent_ids
    		, Timestamp[] usg_timestamps, boolean tcEnable ,boolean saveLog , String objectActionType)
            throws qdbErrMessage,cwException, SQLException {
        try {
            int size = (usg_ent_ids.length<usg_timestamps.length ? usg_ent_ids.length : usg_timestamps.length);
            dbUserGroup usg;
            for(int i=0;i<size;i++) {
                usg = new dbUserGroup();
                usg.usg_ent_id = usg_ent_ids[i];
     			 AcUserGroup ug = new AcUserGroup(con);
    			 if(!ug.canManageGroup(prof, usg.usg_ent_id, tcEnable)) {
    				 throw new qdbErrMessage("USG002");
    			 }
                usg.ent_id = usg_ent_ids[i];
                usg.ent_upd_date = usg_timestamps[i];
                usg.get(con);
				ObjectActionLog log = new ObjectActionLog(usg.usg_ent_id, 
						usg.ent_ste_uid,
						usg.usg_display_bil,
						ObjectActionLog.OBJECT_TYPE_GRP,
						ObjectActionLog.OBJECT_ACTION_DEL,
						objectActionType,
						prof.getUsr_ent_id(),
						prof.getUsr_last_login_date(),
						prof.getIp()
				);
                usg.delGroup(con, true, true, prof.usr_id);
                if(saveLog){
    				SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                }
                
            }
        }
        catch(qdbException qdbe) {
            throw new cwException(qdbe.getMessage());
        }
    }


    //physical del an userGroup
    //pre-set var: usg_ent_id, ent_id, ent_upd_date
    // from integration batch, no need to check hasChild  and timestamp
    public void delGroup(Connection con, boolean checkChild, boolean checkTimestamp, String ent_delete_usr_id) throws qdbException, qdbErrMessage {
        try {
            //check EntityRelation to see if the group has any child
        	Timestamp deleteTime = cwSQL.getTime(con);
            if(checkChild){
                if (hasChild(con)){
                    throw new qdbErrMessage("USG004");
                }
            }else{
                delEntityRelationAsAncestor(con, ent_delete_usr_id);
            }

            if (checkTimestamp)
                super.checkTimeStamp(con);

            //del all catalog access record
            aeCatalogAccess.delEnt(con, usg_ent_id);

            // delete permission assigned to the user group
            dbResourcePermission.delAllByEntId(con, usg_ent_id);

            //del records in EntityRelation that the group is as member
            delEntityRelationAsChild(con, ent_delete_usr_id, deleteTime);

            // del record in user role target group, this group as target group
            DbRoleTargetEntity dbrte = new DbRoleTargetEntity();
            dbrte.rte_ent_id = usg_ent_id;
            dbrte.del(con, false);

            //del userGroup
            delUserGroup(con, ent_delete_usr_id, deleteTime);

            //del Entity
            super.del(con, ent_delete_usr_id, deleteTime);
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    //del UserGroup record
    //pre-set var: usg_ent_id
    private void delUserGroup(Connection con, String usr_id, Timestamp deleteTime) throws SQLException {

    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_ancestor_ent_id = usg_ent_id;
    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
    	dbEr.delAsAncestor(con, usr_id);

    	dbEr.ern_child_ent_id = usg_ent_id;
    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
    	dbEr.delAsChild(con, usr_id, deleteTime);
    }

    //delete EntityRelation records where this group is as member
    //pre-set var: usg_ent_id
    public void delEntityRelationAsChild(Connection con, String usr_id, Timestamp deleteTime) throws SQLException {

        dbEntityRelation dbEr = new dbEntityRelation();
        dbEr.ern_child_ent_id = usg_ent_id;
    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
    	dbEr.delAsChild(con, usr_id, deleteTime);
    }

    //delete EntityRelation records where this group is as parent
    //pre-set var: usg_ent_id
    private void delEntityRelationAsAncestor(Connection con, String usr_id) throws SQLException {

    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_ancestor_ent_id = usg_ent_id;
    	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
    	dbEr.delAsAncestor(con, usr_id);
    }

    public void del(Connection con, loginProfile prof, long parent_id)
            throws  qdbException, qdbErrMessage
    {
        try{
            //super.checkAdminRole(con,admin_id);
            super.checkTimeStamp(con);

            get(con);
// for soft-delete
//            long lost_found_id = dbUserGroup.getLostFoundID(con,prof.root_ent_id);

            PreparedStatement stmt = con.prepareStatement(
                    " SELECT count(*) from EntityRelation WHERE "
// for soft-delete
//                    + " ern_child_ent_id = ? and ern_ancestor_ent_id != ? " );
                    + " ern_child_ent_id = ? ");

            stmt.setLong(1, usg_ent_id);
// for soft-delete
//            stmt.setLong(2, lost_found_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                int cnt = rs.getInt(1);
                if (cnt == 1) {
                    super.upd(con);
                }else if (cnt <= 0) {
                    // delete fails
                    stmt.close();
                    throw new qdbException("No such member exists.");
                }
            }else {
                stmt.close();
                throw new qdbException("Fails to get group information. ");
            }
            stmt.close();

            dbEntityRelation dbEr = new dbEntityRelation();
            dbEr.ern_ancestor_ent_id = parent_id;
            dbEr.ern_child_ent_id = usg_ent_id;
            dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
            dbEr.delAsChild(con, prof.usr_id, null);

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String getChildListAsXML(Connection con, HttpSession sess, loginProfile prof, int cur_page, int pagesize, Timestamp pagetime,
    		boolean showDetails, WizbiniLoader wizbini)
            throws cwException, SQLException, qdbException, cwSysMessage, qdbErrMessage
    {
        //if prof is an apporver having more than one approval group
        //create a virtual root for the user
        boolean virtualRoot = false;
        DbRoleTargetEntity[] apprGroups=null;
        DbRoleTargetEntity[] apprUsers=null;

        if (usg_ent_id == 0) {
            apprGroups =
                ViewRoleTargetGroup.getRoleDirectTargetUserGroup(con ,prof.usr_ent_id ,prof.current_role);
            
                if(apprGroups.length==0) {
                    virtualRoot = false;
                    usg_ent_id = prof.root_ent_id;
                } else if (apprGroups.length == 1) {
                    virtualRoot = false;
                    usg_ent_id = apprGroups[0].rte_ent_id;
                } else {
                    virtualRoot = true;
                    usg_ent_id = 0;
                }


        }
        StringBuffer xmlGroup = new StringBuffer();
        StringBuffer xmlUser = new StringBuffer();
        Hashtable curData = null;
        Timestamp new_pagetime = null;
        int total = 0;

        if(!virtualRoot) {
            //get child user and user group if having a true root
            get(con);

            if(wizbini.cfgTcEnabled && AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_USR_INFO)
            		&& !ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role)
            		&& usg_ent_id == prof.root_ent_id
            		&& AccessControlWZB.isRoleTcInd(prof.current_role)  ) {
            	Vector vec = getTopLevelGroupId(con, prof.usr_ent_id);
            	if(vec != null && vec.size() == 1 && ((Long)vec.elementAt(0)).longValue() == prof.root_ent_id) {
            		getChildGroupAsXML(con, xmlGroup,prof);
            	} else {
            		childGroupInMgtAsXML(con, vec, xmlGroup);
            		if(xmlGroup.toString().equals("")) {
            			throw new qdbErrMessage("USG010");
            		}
            	}
            } else {
            	getChildGroupAsXML(con, xmlGroup , prof);
            }
            //查询所请求的页数是否存在，不存在返回最大页数
            cur_page = checkUserIsStill(con, sess, cur_page, pagesize, pagetime);
            curData = getChildUserAsXML(con, xmlUser, sess, cur_page, pagesize, pagetime, showDetails);
        } else {
            //get direct approval groups and attach to the virtual root
            if(apprGroups!=null && apprGroups.length>0) {
                long[] apprGroupEntIds = new long[apprGroups.length];
                for(int i=0; i<apprGroups.length; i++) {
                    apprGroupEntIds[i] = apprGroups[i].rte_ent_id;
                }
                getChildGroupAsXML(con, xmlGroup, apprGroupEntIds);
            }
            //get direct approval users and attach to the virtual root
            apprUsers =
                ViewRoleTargetGroup.getRoleDirectTargetUser(con
                                                           ,prof.usr_ent_id
                                                           ,prof.current_role);
            if(apprUsers!=null && apprUsers.length>0) {
                long[] apprUserEntIds = new long[apprUsers.length];
                for(int i=0; i<apprUsers.length; i++) {
                    apprUserEntIds[i] = apprUsers[i].rte_ent_id;
                }
                curData = getChildUserAsXML(con, xmlUser, cur_page, pagesize, showDetails, apprUserEntIds);
            }
        }
        if(curData!=null) {
            new_pagetime = (Timestamp) curData.get(HASH_TIMESTAMP);
            total = ((Integer) curData.get(HASH_TOTAL)).intValue();
        } else {
            new_pagetime = cwSQL.getTime(con);
            total = 0;
        }
        StringBuffer result = new StringBuffer(2048);
        //result.append(dbUtils.xmlHeader);
        result.append("<filter_user_group_ind>").append(filter_user_group_ind).append("</filter_user_group_ind>");
        result.append("<group_member_list id=\"").append(usg_ent_id)
            .append("\" grp_role=\"").append(usg_role)
            .append("\" syn_ind=\"").append(ent_syn_ind)
//            .append("\" syn_rol_ind=\"").append(ent_syn_rol_ind)
            .append("\" timestamp=\"").append(ent_upd_date);
        if(ils_id!=0){
            result.append("\" ils_id=\"").append(ils_id)
                  .append("\" itm_id=\"").append(itm_id)
                  .append("\" itm_title=\"").append(cwUtils.esc4XML(aeItem.getItemTitle(con,aeItemLesson.getIlsItmId(con,ils_id))))
                  .append("\" itm_tcr_id=\"").append(s_tcr_id);
        }
        result.append("\">").append(dbUtils.NEWL);
        //result.append(prof.asXML()).append(dbUtils.NEWL);

        // get the ancestor's id and name
        Vector ancestorTable = dbEntityRelation.getGroupAncestorList2Vc(con, usg_ent_id, false);

        result.append("<ancestor_node_list>").append(dbUtils.NEWL);
        // discard the first element of gpm_ancester which should be the self node
        // while the gpm_full_path should be started at the first node
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        for (int i=1; i<ancestorTable.size(); i++) {
        	long ancestorId = ((Long)ancestorTable.elementAt(i)).longValue();
            AccessControlWZB acl = new AccessControlWZB();
            AcUserGroup acusg = new AcUserGroup(con);
            if(!wizbini.cfgTcEnabled || (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_USR_INFO_MGT) && !AccessControlWZB.isRoleTcInd(prof.current_role))
            	|| (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_USR_INFO_MGT) && acusg.canManageGroup(prof, ancestorId, wizbini.cfgTcEnabled ) 
            	&& AccessControlWZB.isRoleTcInd(prof.current_role) )) {
            	result.append("<node id=\"").append(ancestorId).append("\">").append(dbUtils.NEWL);
            	result.append("<title>").append(dbUtils.esc4XML(entityfullpath.getEntityName(con, ancestorId))).append("</title>").append(dbUtils.NEWL);
            	result.append("</node>").append(dbUtils.NEWL);
            }
        }
        result.append("</ancestor_node_list>").append(dbUtils.NEWL);

        //get the group supervisors
        result.append(getSupervisorsAsXML(con));

        result.append("<desc>").append(dbUtils.esc4XML(usg_display_bil)).append("</desc>").append(dbUtils.NEWL);
        result.append("<long_desc>").append(dbUtils.esc4XML(usg_desc)).append("</long_desc>").append(dbUtils.NEWL);
        result.append("<uid>").append(dbUtils.esc4XML(ent_ste_uid)).append("</uid>").append(dbUtils.NEWL);
        result.append("<group cur_page=\"").append(cur_page).append("\" pagesize=\"").append(pagesize)
                .append("\" total=\"").append(total)
                .append("\" sort_col=\"").append(s_order_by)
                .append("\" timestamp=\"").append(new_pagetime).append("\"/>").append(dbUtils.NEWL);

        result.append(xmlGroup.toString());
        if(ent_ste_uid!=null && usg_role!=null && ent_ste_uid.equalsIgnoreCase(USG_ROLE_ROOT)&&usg_role.equalsIgnoreCase(USG_ROLE_ROOT)) {
            result.append("<entity id=\"\" type=\"USG\" role=\"SYSTEM\" timestamp=\"\" display_bil=\"" + cwUtils.esc4XML("LOST&FOUND") + "\" budget=\"\" />");
        }
        result.append(DbUserGrade.getDefaultGradeAsXML(con, prof.root_ent_id));

        result.append(xmlUser.toString());

        //get all role desc
        result.append(dbUtils.getAllRoleAsXML(con,"all_role_list", prof.root_ent_id));
        result.append(dbUtils.getAuthRoleAsXML(con, prof));
//		result.append(dbUtils.DisableRolesXml(con, prof.root_ent_id));
        result.append(dbRegUser.getAllUserAttributeInOrgAsXML(con, prof.root_ent_id));
        if(!virtualRoot) {
            //get approver list
            result.append(getApproverForInsert(con, prof.root_ent_id));
        }

        result.append("</group_member_list>").append(dbUtils.NEWL);
        return result.toString();

    }

    public String getDelMemberListAsXML(Connection con, HttpSession sess, loginProfile prof, int cur_page, int pagesize, Timestamp pagetime, boolean showDetails, boolean tcEnable)
            throws qdbException, SQLException, cwException, qdbErrMessage
    {
        StringBuffer xmlUser = new StringBuffer();
        Hashtable curData = null;
        Timestamp new_pagetime = null;
        int total = 0;

        //检查session中的分页删除用户是否还在      	
        cur_page = checkDelUserIsStill(con, sess, cur_page, pagesize, pagetime, prof, tcEnable);
        curData = getDelUserAsXML(con, xmlUser, sess, cur_page, pagesize, pagetime, showDetails, prof, tcEnable);

        if(curData!=null) {
            new_pagetime = (Timestamp) curData.get(HASH_TIMESTAMP);
            total = ((Integer) curData.get(HASH_TOTAL)).intValue();
        } else {
            new_pagetime = cwSQL.getTime(con);
            total = 0;
        }
        StringBuffer result = new StringBuffer(2048);
        result.append("<group_member_list id=\"0\" grp_role=\"SYSTEM\">").append(dbUtils.NEWL);
        result.append("<desc>").append(cwUtils.esc4XML("LOST&FOUND")).append("</desc>").append(dbUtils.NEWL);
        result.append("<long_desc/>");
        result.append("<uid/>");
        result.append("<group cur_page=\"").append(cur_page).append("\" pagesize=\"").append(pagesize)
                .append("\" total=\"").append(total)
                .append("\" timestamp=\"").append(new_pagetime).append("\"/>").append(dbUtils.NEWL);

        result.append(xmlUser.toString());

        //get all role desc
        result.append(dbUtils.getAllRoleAsXML(con,"all_role_list", prof.root_ent_id));

        result.append("</group_member_list>").append(dbUtils.NEWL);
        return result.toString();

    }

    /*
    public String getMemberListAsXML(Connection con, HttpSession sess, loginProfile prof, int cur_page, int pagesize, Timestamp pagetime)
        throws qdbException, SQLException
    {
        if (usg_ent_id == 0)
            usg_ent_id = prof.root_ent_id;

        get(con);

        StringBuffer xmlGroup = new StringBuffer();
        StringBuffer xmlUser = new StringBuffer();
        getChildGroupAsXML(con, xmlGroup);
        Hashtable curData = getChildUserAsXML(con, xmlUser, sess, cur_page, pagesize, pagetime);
        Timestamp new_pagetime = (Timestamp) curData.get(HASH_TIMESTAMP);
        int total = ((Integer) curData.get(HASH_TOTAL)).intValue();

        StringBuffer result = new StringBuffer(2048);
        result.append(dbUtils.xmlHeader);
        result.append("<group_member_list id=\"").append(usg_ent_id).append("\" grp_role=\"").append(usg_role).append("\" timestamp=\"").append(ent_upd_date).append("\">").append(dbUtils.NEWL);
        result.append(prof.asXML()).append(dbUtils.NEWL);
        result.append("<desc>").append(dbUtils.esc4XML(usg_display_bil)).append("</desc>").append(dbUtils.NEWL);
        result.append("<group cur_page=\"").append(cur_page).append("\" pagesize=\"").append(pagesize)
                .append("\" total=\"").append(total)
                .append("\" timestamp=\"").append(new_pagetime).append("\"/>").append(dbUtils.NEWL);

        result.append(xmlGroup.toString());
        result.append(xmlUser.toString());

        result.append(dbUtils.getAllRoleAsXML(con));

        result.append("</group_member_list>").append(dbUtils.NEWL);
        return result.toString();

    }
    */
    // Same as getMemberListAsXML, but not contains xml header and profile
    public String getMemberListXML(Connection con, HttpSession sess, loginProfile prof, int cur_page, int pagesize, Timestamp pagetime)
            throws qdbException, SQLException, cwException
    {
        if (usg_ent_id == 0)
            usg_ent_id = prof.root_ent_id;

        get(con);

        StringBuffer xmlGroup = new StringBuffer();
        StringBuffer xmlUser = new StringBuffer();
        getChildGroupAsXML(con, xmlGroup,prof);
        Hashtable curData = getChildUserAsXML(con, xmlUser, sess, cur_page, pagesize, pagetime, false);
        Timestamp new_pagetime = (Timestamp) curData.get(HASH_TIMESTAMP);
        int total = ((Integer) curData.get(HASH_TOTAL)).intValue();

        StringBuffer result = new StringBuffer(2048);
        //result.append(dbUtils.xmlHeader);
        result.append("<group_member_list id=\"").append(usg_ent_id).append("\" grp_role=\"").append(usg_role).append("\" timestamp=\"").append(ent_upd_date).append("\">").append(dbUtils.NEWL);
        //result.append(prof.asXML()).append(dbUtils.NEWL);
        result.append("<desc>").append(dbUtils.esc4XML(usg_display_bil)).append("</desc>").append(dbUtils.NEWL);
        result.append("<group cur_page=\"").append(cur_page).append("\" pagesize=\"").append(pagesize)
                .append("\" total=\"").append(total)
                .append("\" timestamp=\"").append(new_pagetime).append("\"/>").append(dbUtils.NEWL);

        result.append(xmlGroup.toString());
        result.append(xmlUser.toString());

        result.append("</group_member_list>").append(dbUtils.NEWL);
        return result.toString();

    }

    public String getUserGroupXML(Connection con, loginProfile prof)
        throws qdbException, SQLException, cwException {
        if (usg_ent_id == 0)
            usg_ent_id = prof.root_ent_id;

        get(con);

        StringBuffer result = new StringBuffer();
        result.append("<group id=\"").append(usg_ent_id)
            .append("\" grp_role=\"").append(usg_role)
            .append("\">").append(dbUtils.NEWL);
        result.append("<desc>").append(dbUtils.esc4XML(usg_display_bil)).append("</desc>").append(dbUtils.NEWL);
        result.append("<long_desc>").append(dbUtils.esc4XML(usg_desc)).append("</long_desc>").append(dbUtils.NEWL);
        result.append("<uid>").append(dbUtils.esc4XML(ent_ste_uid)).append("</uid>");
        result.append("</group>").append(dbUtils.NEWL);

        return result.toString();
    }

    public Hashtable getDelUserAsXML(Connection con, StringBuffer xmlUser, HttpSession sess, int cur_page, int pagesize, Timestamp pagetime, boolean showDetails, loginProfile prof, boolean tcEnable)
        throws qdbException, cwException, SQLException, qdbErrMessage
    {
        int start = ((cur_page -1) * pagesize) + 1;
        int end = cur_page * pagesize;

        Hashtable data = null;
        boolean useSess = false;
        Timestamp sess_pagetime = null;
        if (sess !=null) {
            data = (Hashtable) sess.getAttribute(SESS_USG_ENT_LST);
            if (data !=null) {
                sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);
                if (pagetime != null && sess_pagetime != null
                    && sess_pagetime.equals(pagetime)) {
                    useSess = true;
                }
            }
        }

        String SESS_SQL = new String();
        Vector sessIdVec = null;
        if (useSess) {
            String ent_id_lst = new String();
            sessIdVec = (Vector)data.get(HASH_ENT_ID_VEC);
            for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
                if (i!=start) {
                    ent_id_lst += ",";
                }
                ent_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
            }

            if (ent_id_lst.length()==0)
                ent_id_lst = "-1";

            SESS_SQL = " AND usr_ent_id IN (" + ent_id_lst + ")";
        }


        String SQL =
            " SELECT distinct usr_ent_id AS MEID, usr_id AS MUID, usr_ste_usr_id AS MUSID, "
          + "     usr_display_bil AS MUDISP, usr_pwd AS MUPWD , usr_last_name_bil AS MULNAME, usr_first_name_bil AS MUFNAME , usr_class_number AS MUCLASSNUM , usr_email AS MUEMAIL , "
          + "     ent_upd_date AS MDATE ";

       
        boolean isTa = AccessControlWZB.isRoleTcInd(prof.current_role);
        if(tcEnable && isTa) {
        	Vector vec = getTopLevelGroupId(con, prof.usr_ent_id);
        	if(vec != null && vec.size() > 0) {
        		String usg_id_lst = cwUtils.vector2list(vec);
        		SQL += " FROM RegUser, Entity, EntityRelationHistory, UserGroup WHERE usg_ent_id in " + usg_id_lst +
        		" AND erh_ancestor_ent_id=usg_ent_id" +
        		" AND erh_child_ent_id = usr_ent_id" +
        		" AND erh_type = ?" +
        		" AND erh_end_timestamp = ent_delete_timestamp" + //EntityRelationHistory中可能有多条记录，需要确定最后一条
        		" AND usr_ent_id = ent_id" +
        		" AND usr_status = ?" +
        		" AND ent_delete_usr_id IS NOT NULL" +
        		" AND ent_delete_timestamp IS NOT NULL" +
        		" AND usr_ste_ent_id = ?";
        	} else {
        		throw new qdbErrMessage("USG010");
        	}
        } else {
            SQL += "  FROM RegUser, Entity  "
                + " WHERE ent_id = usr_ent_id "
                + "   AND usr_status = ? "
                + "   AND ent_delete_usr_id IS NOT NULL "
                + "   AND ent_delete_timestamp IS NOT NULL "
                + "   AND usr_ste_ent_id = ? ";
        }
        if(useSess) {
            SQL += SESS_SQL;
        }

        SQL += "   ORDER BY MUDISP ";

        try {
            long MEID;
            String MUID, MUSID, MUDISP;
            String MUPWD, MULNAME, MUFNAME, MUCLASSNUM, MUEMAIL;
            Timestamp MDATE;
            Vector curIdVec = new Vector();
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(SQL);
                int index = 1;
                if(tcEnable && isTa) {
                	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                }
                stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
                stmt.setLong(index++, prof.root_ent_id);
                ResultSet rs = stmt.executeQuery();
                getChildUserAsXML(con, rs, xmlUser, start, end, useSess, showDetails, curIdVec);
            } finally {
                if(stmt!=null) stmt.close();
            }
            int total = 0;
            Hashtable curData = new Hashtable();

            if (useSess) {
                total = sessIdVec.size();
                curData.put(HASH_TIMESTAMP, pagetime);
                curData.put(HASH_TOTAL, new Integer(total));
            }else {
                Timestamp curTime = cwSQL.getTime(con);
                total = curIdVec.size();
                curData.put(HASH_TIMESTAMP, curTime);
                curData.put(HASH_ENT_ID_VEC, curIdVec);
                curData.put(HASH_TOTAL, new Integer(total));
                sess.setAttribute(SESS_USG_ENT_LST, curData);
            }
            return curData;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    //检查session中分页的用户是否还在,不存在就将其从session中去除
    public int checkDelUserIsStill(Connection con, HttpSession sess, int cur_page, int pagesize, Timestamp pagetime, loginProfile prof, boolean tcEnable)
            throws qdbException, cwException, SQLException, qdbErrMessage
        {
            int start = ((cur_page -1) * pagesize) + 1;
            int end = cur_page * pagesize;

            int isStillNum = 0; 
            Hashtable data = null;
            boolean useSess = false;
            Timestamp sess_pagetime = null;
            if (sess !=null) {
                data = (Hashtable) sess.getAttribute(SESS_USG_ENT_LST);
                if (data !=null) {
                    sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);
                    if (pagetime != null && sess_pagetime != null
                        && sess_pagetime.equals(pagetime)) {
                        useSess = true;
                    }
                } else {
                	return cur_page;
                }
            }

            Vector<Long> pageIdVec = new Vector<Long>();
            Vector<Long> isStillIdVec = new Vector<Long>();
            String SESS_SQL = new String();
            Vector sessIdVec = null;
            if (useSess) {
                String ent_id_lst = new String();
                sessIdVec = (Vector)data.get(HASH_ENT_ID_VEC);
                
                if(sessIdVec != null && sessIdVec.size() != 0){            	
    		       	int new_cur_page = (sessIdVec.size() % pagesize == 0) ? sessIdVec.size()/pagesize : sessIdVec.size()/pagesize + 1;
    		       	if(new_cur_page < cur_page){
    		       		cur_page = new_cur_page;
    		       	}
               }
               start = ((cur_page -1) * pagesize) + 1;
               end = cur_page * pagesize;
               
                for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
                    if (i!=start) {
                        ent_id_lst += ",";
                    }
                    ent_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
                    pageIdVec.add(((Long) sessIdVec.elementAt(i-1)).longValue());
                }

                if (ent_id_lst.length()==0)
                    ent_id_lst = "-1";

                SESS_SQL = " AND usr_ent_id IN (" + ent_id_lst + ")";
            } else {
            	return cur_page;
            }


            String SQL = " SELECT distinct usr_ent_id AS MEID ";

            boolean isTa = AccessControlWZB.isRoleTcInd(prof.current_role);
            if(tcEnable && isTa) {
            	Vector vec = getTopLevelGroupId(con, prof.usr_ent_id);
            	if(vec != null && vec.size() > 0) {
            		String usg_id_lst = cwUtils.vector2list(vec);
            		SQL += " FROM RegUser, Entity, EntityRelationHistory, UserGroup WHERE usg_ent_id in " + usg_id_lst +
            		" AND erh_ancestor_ent_id=usg_ent_id" +
            		" AND erh_child_ent_id = usr_ent_id" +
            		" AND erh_type = ?" +
            		" AND erh_end_timestamp = ent_delete_timestamp" + //EntityRelationHistory中可能有多条记录，需要确定最后一条
            		" AND usr_ent_id = ent_id" +
            		" AND usr_status = ?" +
            		" AND ent_delete_usr_id IS NOT NULL" +
            		" AND ent_delete_timestamp IS NOT NULL" +
            		" AND usr_ste_ent_id = ?";
            	} else {
            		throw new qdbErrMessage("USG010");
            	}
            } else {
                SQL += "  FROM RegUser, Entity  "
                    + " WHERE ent_id = usr_ent_id "
                    + "   AND usr_status = ? "
                    + "   AND ent_delete_usr_id IS NOT NULL "
                    + "   AND ent_delete_timestamp IS NOT NULL "
                    + "   AND usr_ste_ent_id = ? ";
            }
            if(useSess) {
                SQL += SESS_SQL;
            }

            try {
                PreparedStatement stmt = null;
                try {
                    stmt = con.prepareStatement(SQL);
                    int index = 1;
                    if(tcEnable && isTa) {
                    	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                    }
                    stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
                    stmt.setLong(index++, prof.root_ent_id);
                    ResultSet rs = stmt.executeQuery();

                    while(rs.next()){
                 	   isStillIdVec.add((Long)rs.getLong("MEID"));
                    }
                    
                    for(Long entId : pageIdVec){
                 	   if(!isStillIdVec.contains(entId)){                 		   
                 		   sessIdVec.remove(entId);
                 		  isStillNum++;
                 	   }
                    }
                    
                    if(isStillNum == 0){
                    	return cur_page;
                    }
                    if(sessIdVec != null && sessIdVec.size() != 0){
                    	
                    	int new_cur_page = (sessIdVec.size() % pagesize == 0) ? sessIdVec.size()/pagesize : sessIdVec.size()/pagesize + 1;
                    	if(new_cur_page < cur_page){
                    		cur_page = new_cur_page;
                    	}
                    	//当删除用户时分页用户数量重新统计
                		
                		int newTotal = sessIdVec.size();
                		data.put(HASH_TIMESTAMP, pagetime);
                		data.put(HASH_ENT_ID_VEC, sessIdVec);
                		data.put(HASH_TOTAL, new Integer(newTotal));
                		sess.setAttribute(SESS_USG_ENT_LST, data);  
                    		
                    }
                    
                } finally {
                    if(stmt!=null) stmt.close();
                }

                return cur_page;

            } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
            }
        }
 
    //检查session中分页的用户是否还在,不存在就将其从session中去除,返回新的当前页
   public int checkUserIsStill(Connection con, HttpSession sess, int cur_page, int pagesize, Timestamp pagetime)
            throws qdbException, cwException
   {
       int start = ((cur_page -1) * pagesize) + 1;
       int end = cur_page * pagesize;

       int isStillNum = 0;
       Hashtable data = null;
       boolean useSess = false;
       Timestamp sess_pagetime = null;
       if (sess !=null) {
           data = (Hashtable) sess.getAttribute(SESS_USG_ENT_LST);
           if (data !=null) {
               sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);
               if (pagetime != null && sess_pagetime != null
                   && sess_pagetime.equals(pagetime)) {
                   useSess = true;
               }
           } else {
        	   return cur_page;
           }
       }

       String SESS_SQL = new String();
       Vector sessIdVec = null;
       Vector<Long> pageIdVec = new Vector<Long>();
       Vector<Long> isStillIdVec = new Vector<Long>();
       if (useSess) {
           String ent_id_lst = new String();
           sessIdVec = (Vector)data.get(HASH_ENT_ID_VEC);
           
           if(sessIdVec != null && sessIdVec.size() != 0){            	
		       	int new_cur_page = (sessIdVec.size() % pagesize == 0) ? sessIdVec.size()/pagesize : sessIdVec.size()/pagesize + 1;
		       	if(new_cur_page < cur_page){
		       		cur_page = new_cur_page;
		       	}
           }
           start = ((cur_page -1) * pagesize) + 1;
           end = cur_page * pagesize;
           
           for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
               if (i!=start) {
                   ent_id_lst += ",";
               }
               ent_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
               pageIdVec.add(((Long) sessIdVec.elementAt(i-1)).longValue());
           }

           if (ent_id_lst.length()==0)
               ent_id_lst = "-1";

           SESS_SQL = " AND ern_child_ent_id IN (" + ent_id_lst + ")";
       } else {
    	   return cur_page;
       }
       String SQL =
           " SELECT ern_child_ent_id AS MEID "
         + "  FROM EntityRelation, RegUser, Entity  "
         + " WHERE ern_ancestor_ent_id = ? "
         + "   AND ern_child_ent_id = usr_ent_id "
         + "   AND ent_id = usr_ent_id "
         + "   AND usr_status IN (?, ?) "
         + "   AND ent_delete_usr_id IS NULL "
         + "   AND ent_delete_timestamp IS NULL "
         + "   AND ern_parent_ind = ? ";
       if(useSess) {
           SQL += SESS_SQL;
       }

       
       try {
           PreparedStatement stmt = null;
           try {
               stmt = con.prepareStatement(SQL);
               stmt.setLong(1, usg_ent_id);
               stmt.setString(2, dbRegUser.USR_STATUS_OK);
               stmt.setString(3, dbRegUser.USR_STATUS_DELETED);
               stmt.setBoolean(4, true);
               ResultSet rs = stmt.executeQuery();
               
               while(rs.next()){
            	   isStillIdVec.add((Long)rs.getLong("MEID"));
               }
               
               for(Long entId : pageIdVec){
            	   if(!isStillIdVec.contains(entId)){
            		   
            		   sessIdVec.remove(entId);
            		   isStillNum++;
            	   }
               }
               
               if(isStillNum == 0){
            	   return cur_page;
               }
            if(sessIdVec != null && sessIdVec.size() != 0){            	
            	int new_cur_page = (sessIdVec.size() % pagesize == 0) ? sessIdVec.size()/pagesize : sessIdVec.size()/pagesize + 1;
            	if(new_cur_page < cur_page){
            		cur_page = new_cur_page;
            	}
            	//当删除用户时分页用户数量重新统计           		
        		int newTotal = sessIdVec.size();
        		data.put(HASH_TIMESTAMP, pagetime);
        		data.put(HASH_ENT_ID_VEC, sessIdVec);
        		data.put(HASH_TOTAL, new Integer(newTotal));
        		sess.setAttribute(SESS_USG_ENT_LST, data);   
               
            }
           } finally {
               if(stmt!=null) stmt.close();
           } 
           
           return cur_page;
       
       }catch(SQLException e) {
               throw new qdbException("SQL Error: " + e.getMessage());
           }
       
	   
   }
    public Hashtable getChildUserAsXML(Connection con, StringBuffer xmlUser, HttpSession sess, int cur_page, int pagesize, Timestamp pagetime, boolean showDetails)
        throws qdbException, cwException
    {
        int start = ((cur_page -1) * pagesize) + 1;
        int end = cur_page * pagesize;

        Hashtable data = null;
        boolean useSess = false;
        Timestamp sess_pagetime = null;
        if (sess !=null) {
            data = (Hashtable) sess.getAttribute(SESS_USG_ENT_LST);
            if (data !=null) {
                sess_pagetime = (Timestamp) data.get(HASH_TIMESTAMP);
                if (pagetime != null && sess_pagetime != null
                    && sess_pagetime.equals(pagetime)) {
                    useSess = true;
                }
            }
        }

        String SESS_SQL = new String();
        Vector sessIdVec = null;
        if (useSess) {
            String ent_id_lst = new String();
            sessIdVec = (Vector)data.get(HASH_ENT_ID_VEC);
            for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
                if (i!=start) {
                    ent_id_lst += ",";
                }
                ent_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
            }

            if (ent_id_lst.length()==0)
                ent_id_lst = "-1";

            SESS_SQL = " AND ern_child_ent_id IN (" + ent_id_lst + ")";
        }
        String SQL =
            " SELECT ern_child_ent_id AS MEID, usr_id AS MUID, usr_ste_usr_id AS MUSID, "
          + "     usr_display_bil AS MUDISP, usr_pwd AS MUPWD , usr_last_name_bil AS MULNAME, usr_first_name_bil AS MUFNAME , usr_class_number AS MUCLASSNUM , usr_email AS MUEMAIL , "
          + "     ent_upd_date AS MDATE "
          + "  FROM EntityRelation, RegUser, Entity  "
          + " WHERE ern_ancestor_ent_id = ? "
          + "   AND ern_child_ent_id = usr_ent_id "
          + "   AND ent_id = usr_ent_id "
          + "   AND usr_status IN (?, ?) "
          + "   AND ent_delete_usr_id IS NULL "
          + "   AND ent_delete_timestamp IS NULL "
          + "   AND ern_parent_ind = ? ";
        if(useSess) {
            SQL += SESS_SQL;
        }

        SQL += "   ORDER BY MDATE  ";
        if(s_order_by!=null&&s_order_by.length()!=0&&!"undefined".equals(s_order_by)){
        	SQL+=s_order_by;
        }

        try {
            long MEID;
            String MUID, MUSID, MUDISP;
            String MUPWD, MULNAME, MUFNAME, MUCLASSNUM, MUEMAIL;
            Timestamp MDATE;
            Vector curIdVec = new Vector();
            Timestamp curTime = cwSQL.getTime(con);
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(SQL);
                stmt.setLong(1, usg_ent_id);
                stmt.setString(2, dbRegUser.USR_STATUS_OK);
                stmt.setString(3, dbRegUser.USR_STATUS_DELETED);
                stmt.setBoolean(4, true);
                ResultSet rs = stmt.executeQuery();
                getChildUserAsXML(con, rs, xmlUser, start, end, useSess, showDetails, curIdVec);
            } finally {
                if(stmt!=null) stmt.close();
            }
            int total = 0;
            Hashtable curData = new Hashtable();

            if (useSess) {
                total = sessIdVec.size();
                curData.put(HASH_TIMESTAMP, pagetime);
                curData.put(HASH_TOTAL, new Integer(total));
            }else {
                total = curIdVec.size();
                curData.put(HASH_TIMESTAMP, curTime);
                curData.put(HASH_ENT_ID_VEC, curIdVec);
                curData.put(HASH_TOTAL, new Integer(total));
                sess.setAttribute(SESS_USG_ENT_LST, curData);
            }
            return curData;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public Hashtable getChildUserAsXML(Connection con, StringBuffer xmlUser, int cur_page, int pagesize, boolean showDetails, long[] usrEntIds)
        throws SQLException, cwException {
        int start = ((cur_page -1) * pagesize) + 1;
        int end = cur_page * pagesize;

        Hashtable data = null;
        String ent_id_lst = new String();
        for (int i=start ; i<= usrEntIds.length && (i <= end);i++) {
            if (i!=start) {
                ent_id_lst += ",";
            }
            ent_id_lst += usrEntIds[i-1];
        }
        if (ent_id_lst.length()==0)
            ent_id_lst = "-1";

        String SQL =
            " SELECT usr_ent_id AS MEID, usr_id AS MUID, usr_ste_usr_id AS MUSID, "
          + "     usr_display_bil AS MUDISP, usr_pwd AS MUPWD , usr_last_name_bil AS MULNAME, usr_first_name_bil AS MUFNAME , usr_class_number AS MUCLASSNUM , usr_email AS MUEMAIL , "
          + "     ent_upd_date AS MDATE "
          + "  FROM RegUser, Entity  "
          + " WHERE "
          + "   ent_id = usr_ent_id "
          + "   AND usr_status IN (?, ?) "
          + "   AND usr_ent_id IN (" + ent_id_lst + ") "
          + "   AND ent_delete_usr_id IS NULL "
          + "   AND ent_delete_timestamp IS NULL "
          + "   ORDER BY MUDISP ";

        PreparedStatement stmt = null;
        Vector curIdVec = new Vector();
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setString(1, dbRegUser.USR_STATUS_OK);
            stmt.setString(2, dbRegUser.USR_STATUS_DELETED);
            ResultSet rs = stmt.executeQuery();
            getChildUserAsXML(con, rs, xmlUser, start, end, true, showDetails, curIdVec);
        }
        finally {
            if(stmt != null) stmt.close();
        }
        int total = 0;
        Hashtable curData = new Hashtable();
        Timestamp curTime = cwSQL.getTime(con);
        total = usrEntIds.length;
        curData.put(HASH_TIMESTAMP, curTime);
        curData.put(HASH_TOTAL, new Integer(total));
        return curData;
    }

    private void getChildUserAsXML(Connection con, ResultSet rs, StringBuffer xmlUser, int start, int end, boolean allUser, boolean showDetails, Vector curIdVec)
        throws SQLException, cwException {

        long MEID;
        String MUID, MUSID, MUDISP;
        String MUPWD, MULNAME, MUFNAME, MUCLASSNUM, MUEMAIL;
        Timestamp MDATE;
        int cnt=0;

        while(rs.next())
        {
            cnt ++;
            MEID = rs.getLong("MEID");

            if (allUser || (cnt >= start && cnt <= end)) {
                MUID = rs.getString("MUID");
                MUSID = rs.getString("MUSID");
                MUDISP = rs.getString("MUDISP");

                MUPWD = rs.getString("MUPWD");
                MULNAME = rs.getString("MULNAME");
                MUFNAME = rs.getString("MUFNAME");
                MUCLASSNUM = rs.getString("MUCLASSNUM");
                MUEMAIL = rs.getString("MUEMAIL");

                MDATE = rs.getTimestamp("MDATE");

                if (showDetails){
                    dbRegUser dbusr = new dbRegUser();
                    dbusr.usr_id = MUID;
                    dbusr.usr_ent_id = MEID;
                    dbusr.ent_id = MEID;
                    dbusr.usr_ste_usr_id = MUSID;
                    dbusr.usr_display_bil = MUDISP;
                    dbusr.usr_pwd = MUPWD;
                    dbusr.usr_last_name_bil = MULNAME;
                    dbusr.usr_first_name_bil = MUFNAME;
                    dbusr.usr_class_number = MUCLASSNUM;
                    dbusr.usr_email = MUEMAIL;

                    xmlUser.append(dbusr.getUserShortXML(con, false, true, true));
                }else{
                    xmlUser.append("<entity id=\"").append(MEID).append("\" type=\"").append(dbEntity.ENT_TYPE_USER).append("\" ");
                    xmlUser.append("usr_id=\"").append(dbUtils.esc4XML(MUSID)).append("\" ");

                    if (showDetails){
                        xmlUser.append("pwd=\"").append(MUPWD).append("\" ");
                        xmlUser.append("last_name=\"").append(dbUtils.esc4XML(MULNAME)).append("\" ");
                        xmlUser.append("first_name=\"").append(dbUtils.esc4XML(MUFNAME)).append("\" ");
                    }
                    xmlUser.append("timestamp=\"").append(MDATE).append("\" ");
                    xmlUser.append("display_bil=\"").append(dbUtils.esc4XML(MUDISP)).append("\">");


                    //handle multiple user role
                    String[] roles = dbUtils.getUserRoles(con, MEID);
                    xmlUser.append("<role_list>").append(dbUtils.NEWL);
                    if(roles != null) {
                        for(int i=0; i<roles.length; i++) {
                            xmlUser.append("<role id=\"").append(roles[i]).append("\"/>").append(dbUtils.NEWL);
                        }
                    }
                    xmlUser.append("</role_list>").append(dbUtils.NEWL);

                    xmlUser.append("</entity>").append(dbUtils.NEWL);
                }
            }
            curIdVec.addElement(new Long(MEID));
        }
        return;
    }

    private void getChildGroupAsXML(ResultSet rs, StringBuffer xmlGroup)
        throws SQLException {

        long MEID;
        String MUDISP, MGPROLE;
        Timestamp MDATE;
        int budget;
        while(rs.next())
        {
            MEID = rs.getLong("MEID");
            MUDISP = rs.getString("MUDISP");
            MGPROLE = rs.getString("MGPROLE");
            MDATE = rs.getTimestamp("MDATE");
            // for department budget
            budget = rs.getInt("usg_budget");

            xmlGroup.append("<entity id=\"").append(MEID);
            xmlGroup.append("\" type=\"").append(dbEntity.ENT_TYPE_USER_GROUP);
            if (MGPROLE!=null) {
                xmlGroup.append("\" role=\"").append(MGPROLE);
            }
            xmlGroup.append("\" timestamp=\"").append(MDATE);
            xmlGroup.append("\" display_bil=\"").append(dbUtils.esc4XML(MUDISP));
            xmlGroup.append("\" budget=\"").append(budget).append("\"/>");
            //xmlGroup.append(">").append(dbUtils.esc4XML(MUDISP)).append("</entity>").append(dbUtils.NEWL);
        }
        return;
    }

    private void getChildGroupAsXML(Connection con, StringBuffer xmlGroup, long[] usgEntIds)
        throws SQLException {
        String SQL =
            "SELECT"
          + " usg_ent_id AS MEID, usg_display_bil AS MUDISP,"
          + " usg_role AS MGPROLE, ent_upd_date AS MDATE,"
        // for department budget
          + " usg_budget "
          + "  FROM UserGroup, Entity "
          + " WHERE ent_id = usg_ent_id "
          + " AND ent_delete_usr_id IS NULL "
          + " AND ent_delete_timestamp IS NULL "
          + " AND usg_ent_id in ";

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL
                                        + cwUtils.array2list(usgEntIds)
                                        + " ORDER BY MUDISP ");
            ResultSet rs = stmt.executeQuery();
            getChildGroupAsXML(rs, xmlGroup);
        } finally {
            stmt.close();
        }
        return;
    }

    /**
     * updated for department budget - Emily, 20020828
     * @param con
     * @param xmlGroup
     * @throws qdbException
     * @throws SQLException 
     */
    public void getChildGroupAsXML(Connection con, StringBuffer xmlGroup ,  loginProfile prof)
        throws qdbException, SQLException
    {
    	long rootTcId = ViewTrainingCenter.getRootTcId(con);
    	String SQL =
            "SELECT"
          + " ern_child_ent_id AS MEID, usg_display_bil AS MUDISP,"
          + " usg_role AS MGPROLE, ent_upd_date AS MDATE,"
          + " usg_budget "
          + "  FROM EntityRelation, UserGroup, Entity "
          + " WHERE "
          + "   ern_child_ent_id = usg_ent_id "
          + "   AND ent_id = usg_ent_id "
          + "   AND ent_delete_usr_id IS NULL "
          + "   AND ent_delete_timestamp IS NULL "
    	  + "   AND ern_parent_ind = ?";
    	if(prof.root_ent_id != usg_ent_id){
    		SQL += "   AND ern_ancestor_ent_id = ?";
    	}else{
    		if(rootTcId==prof.my_top_tc_id){
        		SQL += "   AND ern_ancestor_ent_id in (  "
                        + "	 	select tce_ent_id  from tcTrainingCenterTargetEntity  where tce_tcr_id = ?"
                        + "	) ";
    		}else{
        		SQL += "   AND ern_child_ent_id in (  "
                        + "	 	select tce_ent_id  from tcTrainingCenterTargetEntity  where tce_tcr_id = ?"
                        + "	) ";
    		}

    	}
    	SQL +=  "  ORDER BY MUDISP ";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setBoolean(1, true);
            if(prof.root_ent_id != usg_ent_id){
            	 stmt.setLong(2, usg_ent_id);
            }else{
            	stmt.setLong(2, prof.my_top_tc_id);
            }
            ResultSet rs = stmt.executeQuery();
            getChildGroupAsXML(rs, xmlGroup);
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        } finally {
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se) {
                throw new qdbException("SQL Error: " + se.getMessage());
            }
        }
    }

    public static Vector getMemberListFromCos(Connection con, long cos_res_id)
            throws qdbException
    {

        Vector memberIds = new Vector();
        String SQL =
                " SELECT enr_ent_id AS EID, ent_type AS ETYPE "
                +   "  FROM Enrolment, Entity "
                +   " WHERE enr_res_id = ? "
                +   "   AND enr_ent_id = ent_id "
                +   "   AND ent_delete_usr_id IS NULL "
                +   "   AND ent_delete_timestamp IS NULL ";

        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cos_res_id);
            ResultSet rs = stmt.executeQuery();
            String ETYPE;
            Long EID;

            while(rs.next())
            {
                EID = new Long(rs.getLong("EID"));
                ETYPE = rs.getString("ETYPE");

                if (ETYPE.equalsIgnoreCase(ENT_TYPE_USER)) {
                    memberIds.addElement(EID);
                }
            }
            stmt.close();

            return memberIds;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public static Vector getChildGroupVec(Connection con, long parentId)
            throws qdbException
    {
        String DEF_TYPE = dbEntity.ENT_TYPE_USER_GROUP;
        return getMemberGroupVec(con, parentId, DEF_TYPE);
    }

    public static Vector getMemberGroupVec(Connection con, long parentId, String childType)
            throws qdbException
    {
        Vector memberIds = new Vector();
        String SQL =
            "SELECT"
        	+ " ern_child_ent_id AS MEM_ID"
        	+ " FROM EntityRelation, Entity"
        	+ " WHERE"
        	+ " ern_ancestor_ent_id = ?"
        	+ " AND ern_parent_ind = ?"
        	+ " AND ent_type = ?"
        	+ " AND ern_child_ent_id = ent_id"
        	+ " AND ent_delete_usr_id IS NULL"
        	+ " AND ent_delete_timestamp IS NULL";

        getMemberGroupID(con, SQL, parentId, memberIds, childType);

        return memberIds;
    }

    private static void getMemberGroupID(Connection con, String SQL, long parentId, Vector memberIds)
            throws qdbException
    {
        String DEF_TYPE = dbEntity.ENT_TYPE_USER_GROUP;
        getMemberGroupID(con, SQL, parentId, memberIds, DEF_TYPE);
    }

    private static void getMemberGroupID(Connection con, String SQL, long parentId, Vector memberIds, String childType)
            throws qdbException
    {
        // execute the query to get the immediate parent ids into the parent and ancestor vectors
        Vector parentIds = new Vector();
        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, parentId);
            stmt.setBoolean(2, true);
            stmt.setString(3, childType);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {

                Long memberEntId = new Long(rs.getLong("MEM_ID"));
                memberIds.addElement(memberEntId);
                parentIds.addElement(memberEntId);

            }
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        // do recursion for every immediate parent id
        for (int i = 0; i < parentIds.size(); i++) {
            getMemberGroupID(con, SQL, ((Long)parentIds.elementAt(i)).longValue(), memberIds);
        }
    }

    public static Vector traceParentID(Connection con, long entId)
            throws qdbException
    {
        dbEntity ent = new dbEntity();
        ent.ent_id = entId;
        ent.get(con);
        return traceParentID(con, entId, ent.ent_type);
    }

    public static Vector traceParentID(Connection con, long entId, String entType)
        throws qdbException
    {
        try {
            dbEntityRelation dbEr = new dbEntityRelation();
            dbEr.ern_child_ent_id = entId;
            if (entType.equals(dbEntity.ENT_TYPE_USER)) {
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
            }else {
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
            }
            Vector ancesterIds = dbEr.getAncestorList2Vc(con, false);
            return ancesterIds;
        }catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    /*
        Description: Check if the ancesterId is the ancester of entId
        Return: True if ancesterId is an ancester of entId or the same as entId; false if otheriwise
    */
    public static boolean isAncester(Connection con, long entId, long ancesterId)
            throws qdbException
    {
        // match an ancester
        if (entId == ancesterId) {
            return true;
        }

        Vector parentIds = new Vector();
        boolean blnBingo = false;
        String SQL =
                "SELECT "
                + "   ern_ancestor_ent_id PARENT_ID "
                + "FROM "
                + "   EntityRelation "
                + "WHERE  "
                + "   ern_child_ent_id = ? "
                + "AND "
                + "   (ern_type = ? Or ern_type = ? ) "
                + " AND ern_parent_ind = ? ";

        // execute the query to get the immediate parent ids in parent vector
        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, entId);
            stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setString(3, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
            stmt.setBoolean(4, true);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Long parentEntId = new Long(rs.getLong("PARENT_ID"));
                parentIds.addElement(parentEntId);
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        // do recursion for every immediate parent id
        for (int i = 0; i < parentIds.size(); i++) {
            if(isAncester(con, ((Long)parentIds.elementAt(i)).longValue(), ancesterId)) {
                return true;
            }
        }
        // return overall findings
        return false;
    }

    public String asXML(Connection con)
            throws qdbException
    {
        // format xml
        String result = "";
        result += "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL + dbUtils.NEWL;
        result += "<group id=\"" + usg_ent_id + "\">"  + dbUtils.NEWL;
        result += "<display>" + usg_display_bil + "</display>" + dbUtils.NEWL;
        result += "<admin>" + usg_usr_id_admin + "</admin>" + dbUtils.NEWL;
        result += "<role>" + usg_role + "</role>" + dbUtils.NEWL;
        result += "<root>" + usg_ent_id_root + "</root>" + dbUtils.NEWL;
        result += "</group>" + dbUtils.NEWL ;

        return result;
    }

    public static String getUserXML(Connection con, long ent_id, StringBuffer usrId)
            throws qdbException
    {
        try{
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT usr_id, usr_last_name_bil, usr_first_name_bil, " +
                    "        usr_display_bil, usg_display_bil " +
                    " FROM EntityRelation, RegUser, UserGroup " +
                    " WHERE usg_ent_id = ern_ancestor_ent_id " +
                    " AND usr_ent_id = ern_child_ent_id " +
                    " AND usr_ent_id= ? " +
                    " AND ern_parent_ind = ? ");

            stmt.setLong(1, ent_id);
            stmt.setBoolean(2, true);

            ResultSet rs = stmt.executeQuery();
            String xmlBody = "";

            rs = stmt.executeQuery();

            if (rs.next()) {
                usrId.setLength(0);
                usrId.append(rs.getString("usr_id"));
                String group_name = rs.getString("usg_display_bil");
                String last_name = rs.getString("usr_last_name_bil");
                String first_name = rs.getString("usr_first_name_bil");
                String display = rs.getString("usr_display_bil");

                xmlBody += "<entity ent_id=\"" + ent_id + "\" ";
                xmlBody += "first_name=\"" + dbUtils.esc4XML(first_name) + "\" ";
                xmlBody += "last_name=\"" + dbUtils.esc4XML(last_name) + "\" ";
                xmlBody += "group_name=\"" + dbUtils.esc4XML(group_name) + "\" ";
                xmlBody += ">" + dbUtils.esc4XML(display) + "</entity>" + dbUtils.NEWL;
            }  else {
                stmt.close();
                throw new qdbException("Failed to get group information.");
            }
            stmt.close();

            return xmlBody;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    //public static String getUserList(Connection con, String[] ent_id_lst)
    public static Vector getUserVec(Connection con, String[] ent_id_lst)
            throws qdbException, SQLException
    {
        Vector memberIds = new Vector();
		String SQL = OuterJoinSqlStatements.dbUserGroupGetUserVec();
//        String SQL =
//                " SELECT ent_type, usr_id FROM Entity, RegUser "
//                + " WHERE ent_id = ? "
//                + " AND ent_delete_usr_id IS NULL "
//                + " AND ent_delete_timestamp IS NULL "
//                + " AND usr_ent_id " + cwSQL.get_right_join(con) + " ent_id ";

        PreparedStatement stmt;
        ResultSet rs;
        String type;
        //dbRegUser user;

        for (int i=0; i<ent_id_lst.length;i++) {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, Long.parseLong(ent_id_lst[i]));
            rs = stmt.executeQuery();

            if (rs.next()) {
                type = rs.getString("ent_type");

                if (type.equals(ENT_TYPE_USER)) {
                    String usr_id = rs.getString("usr_id");
                    memberIds.addElement(usr_id);
                    //user = new dbRegUser();
                    //user.usr_ent_id = Long.parseLong(ent_id_lst[i]);
                    //memberIds.addElement(user.getUserId(con));
                } else {
                    Vector tmpVec = getUserVec(con, Long.parseLong(ent_id_lst[i]));
                    dbUtils.appendVec(memberIds, tmpVec);
                }
            }
            stmt.close();
        }

        //String memberLst = dbUtils.vecStr2list(memberIds);
        //return memberLst;

        return memberIds;

    }

    public static Vector getUserVec(Connection con, long parentId)
            throws qdbException
    {
        Vector memberIds = new Vector();
		String SQL = OuterJoinSqlStatements.dbUserGroupGetUserVec2();
		getUsersID(con, SQL, parentId, memberIds);

		return memberIds;
    }

    private static void getUsersID(Connection con, String SQL, long parentId, Vector memberIds)
            throws qdbException
    {
        // execute the query to get the immediate parent ids into the parent and ancestor vectors
        Vector parentIds = new Vector();
        try {
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, parentId);
            stmt.setBoolean(2, true);
            stmt.setLong(3, parentId);

            ResultSet rs = stmt.executeQuery();
            //dbRegUser user;
            String usr_id = null;
            while(rs.next()) {
                Long memberEntId = new Long(rs.getLong("MEM_ID"));
                //user = new dbRegUser();
                //user.usr_ent_id = memberEntId.longValue();
                //String usr_id = user.getUserId(con);
                String type = rs.getString("MEM_TYPE");
                if (type.equalsIgnoreCase(ENT_TYPE_USER)) {
                    usr_id = rs.getString("USR_ID");
                    if (!memberIds.contains(usr_id)) {
                        memberIds.addElement(usr_id);
                    }
                    // Usergroup
                }else {
                    if (!parentIds.contains(memberEntId)) {
                        parentIds.addElement(memberEntId);
                    }
                }
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        // do recursion for every immediate parent id
        for (int i = 0; i < parentIds.size(); i++) {
            getUsersID(con, SQL, ((Long)parentIds.elementAt(i)).longValue(), memberIds);
        }
    }

    public String getSuspenseUserXML(Connection con, HttpSession sess, loginProfile prof, int page, int max_trial)
        throws qdbErrMessage, SQLException, qdbException, cwException {

        return getSuspenseUserXML(con, sess, prof, page, USG_SEARCH_PAGE_SIZE);
    }


    public String getSuspenseUserXML(Connection con, HttpSession sess, loginProfile prof, int page, int pagesize, int max_trial,String userCode)
            throws qdbErrMessage, SQLException, qdbException, cwException
    {
        /*
        if (usg_ent_id == 0 && (s_usg_ent_id_lst == null || s_usg_ent_id_lst.length == 0)
            &&  dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
                //Approver role searchs  its  own  approval  group  if searching  group
                //is  not  specified
                s_usg_ent_id_lst = new String[] {MY_APPROVAL_GROUP};
                usg_ent_id = prof.root_ent_id;

        }
        */
        if(usg_ent_id == 0) {
            usg_ent_id = prof.root_ent_id;
        }
        if (pagesize == 0) {
            pagesize = USG_SEARCH_PAGE_SIZE;
        }
        if (page == 0) {
            page = 1;
        }

        get(con);

        StringBuffer result = new StringBuffer();
        StringBuffer searchMatch = new StringBuffer();
        StringBuffer sortSQL = new StringBuffer();
        StringBuffer SQL = new StringBuffer();
        PreparedStatement stmt;
        ResultSet rs;
        Vector usr_lst = new Vector();
        String order_by;
		String[] s = new String[2];

        Vector temp_lst = new Vector();
        boolean fromSession;
        boolean sortValid = false;
        Timestamp cur_time = dbUtils.getTime(con);
        int index = 1;
        int count = 0;

        Timestamp sess_timestamp = (Timestamp)sess.getAttribute(USG_SEARCH_TIMESTAMP);

        int start = ((page-1) * pagesize) + 1;
        int end = page * pagesize;

        String cond_sql = new String();
        if (sess_timestamp != null && sess_timestamp.equals(s_timestamp)) {
            usr_lst = (Vector)sess.getAttribute(USG_SEARCH_ENT_LIST);
            String ent_id_lst = null;


            if (ent_id_lst.length()==0){
				ent_id_lst = "-1";
            }else{
				s = cwSQL.getSQLClause(con,"tem_usr_ent_id",cwSQL.COL_TYPE_LONG,usr_lst,0);
				ent_id_lst = s[0];
            }


            cond_sql += " AND usr_ent_id IN  (" + ent_id_lst + ")";

            fromSession = true;
        } else {
            // Get all its child usergroup
            //Vector grpVec = getMemberGroupVec(con, usg_ent_id);
            // and the group itself
            //grpVec.addElement(new Long(usg_ent_id));

            //take in one more param: s_usg_ent_id_lst to support multiple USG search
            //if this param is not given, use old method to search one USG only by this.usg_ent_id
            if(s_usg_ent_id_lst != null && s_usg_ent_id_lst.length > 0) {
                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_PARENT_USG' AND (";
                boolean first = true;


                for(int i=0; i<s_usg_ent_id_lst.length; i++) {
                    if(s_usg_ent_id_lst[i] != null) {
                        Supervisor sup = null;

                        if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_STAFF)) {
                            if(sup == null) {
                                sup = new Supervisor(con, prof.usr_ent_id);
                            }
                            Vector vStaff = sup.getStaffEntIdVector(con);
                            if(!first && vStaff.size()>0) {
								s = cwSQL.getSQLClause(con,"tmp_gpm_ent_id",cwSQL.COL_TYPE_LONG,vStaff,0);
								cond_sql += " OR ern_child_ent_id IN ( "+s[0]+" )";
                            }
                        } else if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_DIRECT_STAFF)) {
                            if(sup == null) {
                                sup = new Supervisor(con, prof.usr_ent_id);
                            }
                            Vector vStaff = sup.getDirectStaffEntIdVector(con);
                            if(!first && vStaff.size()>0) {
								s = cwSQL.getSQLClause(con,"tmp_gpm_ent_id",cwSQL.COL_TYPE_LONG,vStaff,0);
								cond_sql += " OR ern_child_ent_id IN ( "+s[0]+" )";
                            }

                        }
                        else {
                            if(!first) {
                                cond_sql += " OR ";
                            }
                            cond_sql += " ern_ancestor_ent_id = " + s_usg_ent_id_lst[i];
                            first = false;
                        }
                    }
                }
                if(first) {
                    cond_sql += " ern_ancestor_ent_id = " + prof.root_ent_id;
                }
                cond_sql += " )) ";
            }
            else {
                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_PARENT_USG' AND ern_ancestor_ent_id = " + usg_ent_id + ") ";
            }

            if (s_idc_int != 0){
                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_INTEREST_IDC' AND (ern_ancestor_ent_id = " + s_idc_int + ") ";

            }
            if (s_idc_fcs != 0){
                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_FOCUS_IDC' AND (ern_ancestor_ent_id = " + s_idc_fcs + ") ";
            }
            if (s_grade != 0){
                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_CURRENT_UGR' AND (ern_ancestor_ent_id = " + s_grade + ") ";
            }
            if (s_role_types != null){
                if (s_role_types.length > 0){
                    cond_sql += " AND (";
                }
                for (int i=0; i<s_role_types.length; i++){
                    AccessControlWZB acl = new AccessControlWZB();
                    cond_sql += acl.getEntityByRoleExistsSQL(con, s_role_types[i], "usr_ent_id");
                    cond_sql += " OR ";
                }
                cond_sql += " usr_ent_id = 0 )";
            }
            if (s_status == null || s_status.length == 0){
                s_status = defSearchStatus;
            }
            cond_sql += " AND usr_status IN " + cwUtils.array2list(s_status).toUpperCase() + " ";
//			if (!(this.ils_id > 0)) {
				if (s_itm_id != 0) {
					cond_sql += " AND ent_id ";

					if (s_search_enrolled != null
						&& s_search_enrolled.equalsIgnoreCase("0")) {
						cond_sql += " NOT ";
					}

					//                cond_sql += " IN (SELECT app_ent_id FROM aeApplication WHERE (app_itm_id = " + s_itm_id + " OR app_itm_id IN (SELECT ire_child_itm_id FROM aeItemRelation WHERE ire_parent_itm_id = " + s_itm_id + " )) AND (app_status NOT IN ('REJECTED', 'WITHDRAWN'))) ";
					cond_sql += " IN ("
						+ OuterJoinSqlStatements.searchEnrolledEndIds(
							s_itm_id)
						+ " )";
				}
//			}

            fromSession = false;
        }

        StringBuffer rsCol = new StringBuffer("usr_id,usr_ent_id,usr_pwd,usr_email,usr_email_2,usr_full_name_bil,usr_initial_name_bil,usr_last_name_bil,usr_first_name_bil,usr_display_bil,usr_gender,usr_bday,usr_hkid,usr_other_id_no,usr_other_id_type,usr_tel_1,usr_tel_2,usr_fax_1,usr_country_bil,usr_postal_code_bil,usr_state_bil,usr_address_bil,usr_class,usr_class_number,usr_signup_date,usr_last_login_date,usr_status,usr_upd_date,usr_ste_ent_id,usr_ste_usr_id,usr_extra_1,usr_cost_center,usr_extra_2,usr_extra_3,usr_extra_4,usr_extra_5,usr_extra_6,usr_extra_7,usr_extra_8,usr_extra_9,usr_extra_10,usr_approve_usr_id,usr_approve_timestamp,usr_approve_reason, usr_other_id_no, usr_hkid, usr_join_datetime, usr_job_title ");
        StringBuffer dbTable = new StringBuffer("RegUser, Entity ");
        StringBuffer tableCond = new StringBuffer("ent_id = usr_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ");
		StringBuffer acSiteCond = new StringBuffer(" AND usr_login_trial >= ? ");

        SQL.append("SELECT ").append(rsCol).append(" FROM ");
        SQL.append(dbTable);
        SQL.append(" WHERE ").append(tableCond);
        SQL.append(acSiteCond);
        SQL.append(cond_sql);
        if(userCode!=null&&userCode!=""){
        	SQL.append(" AND (usr_display_bil like '%"+userCode+"%' or usr_ste_usr_id like '%"+userCode+"%')");
        }
        //
        int columnCount = 0;
        ResultSetMetaData rsmd = null;
        int colType;
        if (! fromSession) {
            if ((usr_srh_col_lst ==null)&&(usr_srh_value_lst ==null)){
                //no these params,old logic

                if (s_usr_address_bil != null)
                    SQL.append(" AND lower(usr_address_bil) LIKE ? ");

                if (s_usr_postal_code_bil != null)
                    SQL.append(" AND lower(usr_postal_code_bil) LIKE ? ");

            }else{
                //new logics
                //two params below should be mapped one to one;colomn name -- colomn value
                if ((usr_srh_col_lst.length != usr_srh_value_lst.length)){
                    throw new cwException("The parameters are not valid!");
                }
                if (usr_srh_col_lst.length >0){

                    StringBuffer sqlStrBuf= new StringBuffer("select ");

                    StringBuffer strTmp = new StringBuffer(1024);
                    for (int i=0;i<usr_srh_col_lst.length;i++){
                        //usr_bday's type is datetime;
                        if (usr_srh_col_lst[i].startsWith("usr_bday")){
                            sqlStrBuf.append("usr_bday").append(", ");
                        }else if (usr_srh_col_lst[i].startsWith("usr_join_date")){
                            sqlStrBuf.append("usr_join_datetime").append(", ");
                        }else{
                            sqlStrBuf.append(usr_srh_col_lst[i]).append(", ");
                        }
                    }
                    sqlStrBuf.delete(sqlStrBuf.length()-2,sqlStrBuf.length());
                    //sql never get rows to upgrade the performance;
                    String sql1 = sqlStrBuf.append(" from RegUser where 1=2").toString();

                    PreparedStatement stmt1 = con.prepareStatement(sql1);

                    ResultSet rs1 = stmt1.executeQuery();
                    rsmd = rs1.getMetaData();
                    columnCount = rsmd.getColumnCount();

                    colType = 0;
                    StringBuffer sqlConditionBuf = new StringBuffer();
                    for(int i = 1 ;i <= columnCount; i++) {
                        colType = rsmd.getColumnType(i);

                        if (colType == java.sql.Types.TIMESTAMP){
                            if (usr_srh_col_lst[i-1].equals("usr_bday_fr")) {
                                sqlConditionBuf.append(" and usr_bday >= ?");
                            }
                            if (usr_srh_col_lst[i-1].equals("usr_bday_to")) {
                                sqlConditionBuf.append(" and usr_bday <= ?");
                            }
                            if (usr_srh_col_lst[i-1].equals("usr_join_date_fr")) {
                                sqlConditionBuf.append(" and usr_join_datetime >= ?");
                            }
                            if (usr_srh_col_lst[i-1].equals("usr_join_date_to")) {
                                sqlConditionBuf.append(" and usr_join_datetime <= ?");
                            }
                        }else{
                            sqlConditionBuf.append(" and lower(").append(usr_srh_col_lst[i-1]).append(") like ?");
                        }
                    }
                    SQL.append(sqlConditionBuf);
                    stmt1.close();
                }
            }
        }

        SQL.append(" ORDER BY ");

        if (s_order_by != null && s_order_by.equalsIgnoreCase("DESC")) {
            order_by = " DESC, ";
            s_order_by = "DESC";
        } else {
            order_by = " ASC, ";
            s_order_by = "ASC";
        }

        for (int i=0; i<USG_SEARCH_SORT_ORDER.length; i++) {
            if (USG_SEARCH_SORT_ORDER[i].equalsIgnoreCase(s_sort_by)) {
                SQL.append(USG_SEARCH_SORT_ORDER[i]).append(order_by);
                sortValid = true;
            } else {
                sortSQL.append(USG_SEARCH_SORT_ORDER[i]).append(order_by);
            }
        }

        if (! sortValid)
            s_sort_by = USG_SEARCH_SORT_ORDER[0];

        SQL.append(sortSQL.toString());
        SQL.append("usr_ent_id");
        stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(index++, max_trial);

        if (! fromSession) {
            if (s_role_types != null) {
                for (int i=0; i<s_role_types.length; i++){
                    stmt.setTimestamp(index++, cur_time);
                    stmt.setTimestamp(index++, cur_time);
                }
            }
            if ((usr_srh_col_lst ==null)&&(usr_srh_value_lst ==null)){
                //no these params,old logic

                if (s_usr_bday_to != null)
                    stmt.setTimestamp(index++, s_usr_bday_to);


                if (s_usr_postal_code_bil != null)
                    stmt.setString(index++, '%' + s_usr_postal_code_bil.toLowerCase() + '%');

            }else {
                //new logics
                if (usr_srh_col_lst.length >0){
                    for(int i = 1 ;i <= columnCount; i++) {
                        colType = rsmd.getColumnType(i);
                        //usr_bday's type is datetime;
                        if (colType == java.sql.Types.TIMESTAMP){
                            stmt.setTimestamp(index++,Timestamp.valueOf(usr_srh_value_lst[i-1]));
                        }else{
                            //all other TYPE be set to String;none int ntext(clob in Oracle) in where clause
                            stmt.setString(index++,"%"+usr_srh_value_lst[i-1].trim().toLowerCase()+"%");
                        }
                    }
                }
            }
        }
        rs = stmt.executeQuery();
        while (rs.next()) {
            count ++;
            dbRegUser tempUsr = new dbRegUser();
            if (fromSession || (count >= start && count <= end)) {
                tempUsr.usr_id = rs.getString("usr_id");
                tempUsr.usr_ent_id = rs.getLong("usr_ent_id");
                tempUsr.ent_id = tempUsr.usr_ent_id;
                tempUsr.usr_pwd = rs.getString("usr_pwd");
                tempUsr.usr_email = rs.getString("usr_email");
                tempUsr.usr_email_2 = rs.getString("usr_email_2");
                tempUsr.usr_full_name_bil = rs.getString("usr_full_name_bil");
                tempUsr.usr_initial_name_bil = rs.getString("usr_initial_name_bil");
                tempUsr.usr_last_name_bil = rs.getString("usr_last_name_bil");
                tempUsr.usr_first_name_bil = rs.getString("usr_first_name_bil");
                tempUsr.usr_display_bil = rs.getString("usr_display_bil");
                tempUsr.usr_gender = rs.getString("usr_gender");
                tempUsr.usr_bday = rs.getTimestamp("usr_bday");
                tempUsr.usr_hkid = rs.getString("usr_hkid");
                tempUsr.usr_other_id_no = rs.getString("usr_other_id_no");
                tempUsr.usr_other_id_type = rs.getString("usr_other_id_type");
                tempUsr.usr_tel_1 = rs.getString("usr_tel_1");
                tempUsr.usr_tel_2 = rs.getString("usr_tel_2");
                tempUsr.usr_fax_1 = rs.getString("usr_fax_1");
                tempUsr.usr_country_bil = rs.getString("usr_country_bil");
                tempUsr.usr_postal_code_bil = rs.getString("usr_postal_code_bil");
                tempUsr.usr_state_bil = rs.getString("usr_state_bil");
                tempUsr.usr_address_bil = rs.getString("usr_address_bil");
                tempUsr.usr_class = rs.getString("usr_class");
                tempUsr.usr_class_number = rs.getString("usr_class_number");
                tempUsr.usr_signup_date = rs.getTimestamp("usr_signup_date");
                tempUsr.usr_last_login_date = rs.getTimestamp("usr_last_login_date");
                tempUsr.usr_status = rs.getString("usr_status");
                tempUsr.usr_upd_date = rs.getTimestamp("usr_upd_date");
                tempUsr.usr_ste_ent_id = rs.getLong("usr_ste_ent_id");
                tempUsr.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                tempUsr.usr_extra_1 = rs.getString("usr_extra_1");
                tempUsr.usr_cost_center = rs.getString("usr_cost_center");
                tempUsr.usr_extra_2 = rs.getString("usr_extra_2");
                tempUsr.usr_extra_3 = rs.getString("usr_extra_3");
                tempUsr.usr_extra_4 = rs.getString("usr_extra_4");
                tempUsr.usr_extra_5 = rs.getString("usr_extra_5");
                tempUsr.usr_extra_6 = rs.getString("usr_extra_6");
                tempUsr.usr_extra_7 = rs.getString("usr_extra_7");
                tempUsr.usr_extra_8 = rs.getString("usr_extra_8");
                tempUsr.usr_extra_9 = rs.getString("usr_extra_9");
                tempUsr.usr_extra_10 = rs.getString("usr_extra_10");
                tempUsr.usr_approve_usr_id = rs.getString("usr_approve_usr_id");
                tempUsr.usr_approve_timestamp = rs.getTimestamp("usr_approve_timestamp");
                tempUsr.usr_approve_reason = rs.getString("usr_approve_reason");
                tempUsr.usr_hkid = rs.getString("usr_hkid");
                tempUsr.usr_other_id_no = rs.getString("usr_other_id_no");
                searchMatch.append(tempUsr.getUserShortXML(con, false, true, false,(tempUsr.usr_status != null && tempUsr.usr_status.equals(dbRegUser.USR_STATUS_PENDING))));
            }

            temp_lst.addElement(new Long(rs.getLong("usr_ent_id")));
        }

        int total = 0;
        if (! fromSession) {
            total = temp_lst.size();
            sess.setAttribute(USG_SEARCH_TIMESTAMP, cur_time);
            sess.setAttribute(USG_SEARCH_ENT_LIST, temp_lst);

        } else {
            total = usr_lst.size();
            cur_time = sess_timestamp;
        }

        stmt.close();
        if(s[1] != null)
		    cwSQL.dropTempTable(con,s[1]);
        //result.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
        result.append("<suspense_user_list id=\"").append(usg_ent_id).append("\" grp_role=\"").append(usg_role).append("\" timestamp=\"").append(ent_upd_date).append("\">").append(dbUtils.NEWL);
        //result.append(prof.asXML()).append(dbUtils.NEWL).append("<desc>").append(dbUtils.esc4XML(usg_display_bil)).append("</desc>").append(dbUtils.NEWL);
        result.append("<search time=\"").append(cur_time).append("\" cur_page=\"").append(page).append("\" page_size=\"").append(pagesize)
        	.append("\" total=\"").append(total).append("\" sort_by=\"").append(s_sort_by).append("\" order_by=\"").append(s_order_by)
        	.append("\" search_code=\"").append(userCode).append("\"/>")
        	.append(dbUtils.NEWL);
        result.append(searchMatch.toString());
        result.append("</suspense_user_list>").append(dbUtils.NEWL);

        return result.toString();
    }

    public String searchEntListAsXML(Connection con, HttpSession sess, loginProfile prof, int page)
        throws qdbErrMessage, cwException, SQLException, qdbException, cwSysMessage {

        return searchEntListAsXML(con, sess, prof, page, USG_SEARCH_PAGE_SIZE);
    }


    public String searchEntListAsXML(Connection con, HttpSession sess, loginProfile prof, int page, int pagesize)
            throws qdbErrMessage, cwException, SQLException, qdbException, cwSysMessage
    {
        //WaiLun : search users used in notify
        //if (! prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG);

        boolean searchLostFound = false;
		s_search_enrolled = "1";
        if(cwUtils.array2list(s_status).toUpperCase().indexOf(dbRegUser.USR_STATUS_DELETED)>0) {
            searchLostFound = true;
        }

        if ((s_usg_ent_id_lst == null || s_usg_ent_id_lst.length == 0)
            &&  dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
                //Approver role searchs  its  own  approval  group  if searching group
                //is  not  specified
                s_usg_ent_id_lst = new String[] {MY_APPROVAL_GROUP};
        }

        if(usg_ent_id == 0) {
            usg_ent_id = prof.root_ent_id;
        }
        if (pagesize == 0) {
            pagesize = USG_SEARCH_PAGE_SIZE;
        }
        if (page == 0) {
            page = 1;
        }
        if(!searchLostFound) {
            get(con);
        }

        StringBuffer result = new StringBuffer();
        StringBuffer searchMatch = new StringBuffer();
        StringBuffer sortSQL = new StringBuffer();
        StringBuffer SQL = new StringBuffer();
        PreparedStatement stmt;
        ResultSet rs;
        Vector usr_lst = new Vector();
        String order_by;
		String[] s = new String[2];
		String tableName = null;
        Vector temp_lst = new Vector();
        boolean fromSession;
        boolean sortValid = false;
        Timestamp cur_time = dbUtils.getTime(con);
        int index = 1;
        int count = 0;

        Timestamp sess_timestamp = (Timestamp)sess.getAttribute(USG_SEARCH_TIMESTAMP);
        String sess_sort_by = (String) sess.getAttribute(USG_SEARCH_SORT_BY);
        String sess_order_by = (String) sess.getAttribute(USG_SEARCH_ORDER_BY);

        int start = ((page-1) * pagesize) + 1;
        int end = page * pagesize;

        String cond_sql = new String();
        String group_sql = new String();
        Vector v_sql_param = new Vector();
        if ((sess_timestamp != null && sess_timestamp.equals(s_timestamp)) &&
            (sess_sort_by != null && ((s_sort_by != null && sess_sort_by.equals(s_sort_by)) || s_sort_by == null)) &&
            (sess_order_by != null && ((s_order_by != null && sess_order_by.equals(s_order_by)) || s_order_by == null))) {

            usr_lst = (Vector)sess.getAttribute(USG_SEARCH_ENT_LIST);
            Vector tmp_ent_id_lst = new Vector();
            String ent_id_lst;
            for (int i=start ; i<= usr_lst.size() && (i <= end);i++) {
//                if (i!=start) {
//                    ent_id_lst += ",";
//                }
//                ent_id_lst += ((Long) usr_lst.elementAt(i-1)).longValue();
				tmp_ent_id_lst.add(usr_lst.elementAt(i-1));
            }

            if (tmp_ent_id_lst.size()==0){
				ent_id_lst = "-1";
            } else{
			    s = cwSQL.getSQLClause(con,"tem_usr_ent_id",cwSQL.COL_TYPE_LONG,tmp_ent_id_lst,0);
			    ent_id_lst = s[0];
		    }


            cond_sql += " AND usr_ent_id IN  (" + ent_id_lst + ")";
            fromSession = true;
        } else {
            // Get all its child usergroup
            //Vector grpVec = getMemberGroupVec(con, usg_ent_id);
            // and the group itself
            //grpVec.addElement(new Long(usg_ent_id));

            //take in one more param: s_usg_ent_id_lst to support multiple USG search
            //if this param is not given, use old method to search one USG only by this.usg_ent_id
        	if(searchLostFound){
        		if(s_usg_ent_id_lst != null && s_usg_ent_id_lst.length > 0) {
	                group_sql += " AND usr_ent_id IN (SELECT erh_child_ent_id FROM EntityRelationHistory where erh_type = 'USR_PARENT_USG' AND (";
	                boolean first = true;
	                for(int i=0; i<s_usg_ent_id_lst.length; i++) {
	                    if(s_usg_ent_id_lst[i] != null) {
	                        Supervisor sup = null;
	
	                        if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_STAFF)) {
	                            if(sup == null) {
	                                sup = new Supervisor(con, prof.usr_ent_id);
	                            }
	                            Vector vStaff = sup.getStaffEntIdVector(con);
	                            if(!first) {
	                                group_sql += " OR ";
	                            }
	                            first = false;
	                            if(vStaff.size() > 0) {
									s = cwSQL.getSQLClause(con,"tmp_gpm_ent_id",cwSQL.COL_TYPE_LONG,vStaff,0);
								    group_sql += " erh_child_ent_id IN ( "+s[0]+" )";
	                            } else {
	                                group_sql += " erh_child_ent_id = -1 ";
	                            }
	                        } else if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_DIRECT_STAFF)) {
	                            if(sup == null) {
	                                sup = new Supervisor(con, prof.usr_ent_id);
	                            }
	                            Vector vStaff = sup.getDirectStaffEntIdVector(con);
	                            if(!first) {
	                                group_sql += " OR ";
	                            }
	                            first = false;
	                            if(vStaff.size() > 0) {
									s = cwSQL.getSQLClause(con,"tmp_gpm_ent_id",cwSQL.COL_TYPE_LONG,vStaff,0);
									group_sql += " erh_child_ent_id IN ( "+s[0]+" )";
	                            } else {
	                                group_sql += " erh_child_ent_id = -1 ";
	                            }
	                        }else if(s_usg_ent_id_lst[i].equals(MY_APPROVAL_GROUP)) {
	                            ViewRoleTargetGroup[] viTgps = ViewRoleTargetGroup.getTargetGroups(con, prof.usr_ent_id, prof.current_role, false, true);
	                            for(int j=0; j<viTgps.length; j++) {
	                                if(!first) {
	                                    group_sql += " OR ";
	                                }
	                                group_sql += " erh_ancestor_ent_id =" + viTgps[j].targetEntIds[0];
	                                group_sql += " OR erh_child_ent_id = " + viTgps[j].targetEntIds[0];
	
	                                first = false;
	                            }
	                        }
	                        else {
	                            if(!first) {
	                                group_sql += " OR ";
	                            }
	                            first = false;
	                            group_sql += " erh_ancestor_ent_id = " + s_usg_ent_id_lst[i];
	                        }
	                    }
	                }
	                if(!first) {
	                    group_sql += " )) ";
	                }
	            }
	            else {
	            	group_sql += " AND usr_ent_id IN (SELECT erh_child_ent_id FROM EntityRelationHistory where erh_type = 'USR_PARENT_USG' AND erh_ancestor_ent_id = " + usg_ent_id + ") ";
	            }
        		
        		if (s_idc_int != 0){
	                cond_sql += " AND usr_ent_id IN (SELECT erh_child_ent_id FROM EntityRelationHistory where erh_type = 'USR_INTEREST_IDC' AND erh_ancestor_ent_id = " + s_idc_int + ") ";
	            }
	            if (s_idc_fcs != 0){
	                cond_sql += " AND usr_ent_id IN (SELECT erh_child_ent_id FROM EntityRelationHistory where erh_type = 'USR_FOCUS_IDC' AND erh_ancestor_ent_id = " + s_idc_fcs + ") ";
	            }
	            if (s_grade != 0){
	                cond_sql += " AND usr_ent_id IN (SELECT erh_child_ent_id FROM EntityRelationHistory where erh_type = 'USR_CURRENT_UGR' AND erh_ancestor_ent_id = " + s_grade + ") ";
	            }
        	} else {
	        	if(s_usg_ent_id_lst != null && s_usg_ent_id_lst.length > 0) {
	                group_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_PARENT_USG' AND (";
	                boolean first = true;
	                for(int i=0; i<s_usg_ent_id_lst.length; i++) {
	                    if(s_usg_ent_id_lst[i] != null) {
	                        Supervisor sup = null;
	
	                        if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_STAFF)) {
	                            if(sup == null) {
	                                sup = new Supervisor(con, prof.usr_ent_id);
	                            }
	                            Vector vStaff = sup.getStaffEntIdVector(con);
	                            if(!first) {
	                                group_sql += " OR ";
	                            }
	                            first = false;
	                            if(vStaff.size() > 0) {
									s = cwSQL.getSQLClause(con,"tmp_gpm_ent_id",cwSQL.COL_TYPE_LONG,vStaff,0);
								    group_sql += " ern_child_ent_id IN ( "+s[0]+" )";
	                            } else {
	                                group_sql += " ern_child_ent_id = -1 ";
	                            }
	                        } else if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_DIRECT_STAFF)) {
	                            if(sup == null) {
	                                sup = new Supervisor(con, prof.usr_ent_id);
	                            }
	                            Vector vStaff = sup.getDirectStaffEntIdVector(con);
	                            if(!first) {
	                                group_sql += " OR ";
	                            }
	                            first = false;
	                            if(vStaff.size() > 0) {
									s = cwSQL.getSQLClause(con,"tmp_gpm_ent_id",cwSQL.COL_TYPE_LONG,vStaff,0);
									group_sql += " ern_child_ent_id IN ( "+s[0]+" )";
	                            } else {
	                                group_sql += " ern_child_ent_id = -1 ";
	                            }
	                        }else if(s_usg_ent_id_lst[i].equals(MY_APPROVAL_GROUP)) {
	                            ViewRoleTargetGroup[] viTgps = ViewRoleTargetGroup.getTargetGroups(con, prof.usr_ent_id, prof.current_role, false, true);
	                            for(int j=0; j<viTgps.length; j++) {
	                                if(!first) {
	                                    group_sql += " OR ";
	                                }
	                                group_sql += " ern_ancestor_ent_id =" + viTgps[j].targetEntIds[0];
	                                group_sql += " OR ern_child_ent_id = " + viTgps[j].targetEntIds[0];
	
	                                first = false;
	                            }
	                        }
	                        else {
	                            if(!first) {
	                                group_sql += " OR ";
	                            }
	                            first = false;
	                            group_sql += " ern_ancestor_ent_id = " + s_usg_ent_id_lst[i];
	                        }
	                    }
	                }
	                if(!first) {
	                    group_sql += " )) ";
	                }
	            }
	            else {
	            	group_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_PARENT_USG' AND ern_ancestor_ent_id = " + usg_ent_id + ") ";
	            }
        	
	            if (s_idc_int != 0){
	                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_INTEREST_IDC' AND ern_ancestor_ent_id = " + s_idc_int + ") ";
	            }
	            if (s_idc_fcs != 0){
	                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_FOCUS_IDC' AND ern_ancestor_ent_id = " + s_idc_fcs + ") ";
	            }
	            if (s_grade != 0){
	                cond_sql += " AND usr_ent_id IN (SELECT ern_child_ent_id FROM EntityRelation where ern_type = 'USR_CURRENT_UGR' AND ern_ancestor_ent_id = " + s_grade + ") ";
	            }
        	}

            /**
             * ONLY search users with role 'NLRN' when the s_itm_id specified
             * added by Emily, on Sept. 29, 2003
             */
            if (s_itm_id != 0 && s_role_types == null) {
                AccessControlManager aclMgr = new AccessControlManager();
                String[] rol_lst = aclMgr.getAllRoles(con);
                Vector vec_rol_lst = new Vector();
                for (int j = 0; j < rol_lst.length; j++) {
                    if (AccessControlWZB.isLrnRole(rol_lst[j])) {
                        vec_rol_lst.addElement(rol_lst[j]);
                    }
                }
                // keep the roles with 'ITM_APPLY'
                s_role_types = new String[vec_rol_lst.size()];
                s_role_types = (String[])vec_rol_lst.toArray(s_role_types);
            }

		

            if (s_role_types != null && s_role_types.length > 0){
                if (s_role_types.length > 0){
                    cond_sql += " AND (";
                }
                for (int i=0; i<s_role_types.length; i++){
                    AccessControlWZB acl = new AccessControlWZB();
                    cond_sql += acl.getEntityByRoleExistsSQL(con, s_role_types[i], "usr_ent_id");
                    cond_sql += " OR ";
                }
                cond_sql += " usr_ent_id = 0 )";
            }

            if (s_rol_ext_id != null && s_rol_ext_id.length() > 0){
            	if(s_tcr_id > 0 && tc_enabled){
            		Vector role_types = new Vector();
            		role_types.add(s_rol_ext_id);
            		Vector usr_id_vec = DbTrainingCenter.getMgtResoursUsrEntId(con, role_types, prof.root_ent_id, s_tcr_id);
            		tableName = cwSQL.createSimpleTemptable(con, "tmp_ent_ids", cwSQL.COL_TYPE_LONG, 0);
            		cwSQL.insertSimpleTempTable(con, tableName, usr_id_vec, cwSQL.COL_TYPE_LONG);
            		cond_sql +=" AND usr_ent_id IN ( Select * From " + tableName +")";
            	}else{
            		AccessControlWZB acl = new AccessControlWZB();
            		cond_sql += " AND usr_ent_id IN (" + acl.getEntityByRoleSQL(con, s_rol_ext_id) + " ) ";
            	}
            }
            
			if (s_instr != null && s_instr.length() > 0) {
				cond_sql += " and not exists ( ";
				cond_sql += " select erl_ent_id from acEntityRole, acRole where erl_rol_id = rol_id and erl_ent_id = usr_ent_id and rol_ste_uid = 'INSTR' ";
				cond_sql += " ) ";
			}

            if (s_tcr_id > 0 && s_role_types != null && tc_enabled) {
            	Vector tcRole = new Vector();
            	Vector notTcRole = new Vector();
            	AccessControlWZB acWzb = new AccessControlWZB();
            	for(int i=0; i<s_role_types.length; i++) {
            		boolean isTcRole = acWzb.isTcOfficerRole(con, s_role_types[i]);
            		if(isTcRole) {
            			tcRole.add(s_role_types[i]);
            		} else {
            			notTcRole.add(s_role_types[i]);
            		}
            	}
            	cond_sql += " AND (  usr_ent_id = 0 OR ";
            	if(tcRole != null && tcRole.size() > 0) {
            		cond_sql += "  usr_ent_id IN (select tco_usr_ent_id from tcTrainingCenterOfficer where tco_tcr_id = " + s_tcr_id
            		+ " and tco_rol_ext_id in (";
            		for (int i=0; i<tcRole.size(); i++) {
            			cond_sql += "'" +(String)tcRole.get(i) + "'";
            			if(i<tcRole.size()-1) {
            				cond_sql += ",";
            			}
            		}
            		cond_sql +="))";
            	}
            	if(tcRole != null && tcRole.size() > 0 && notTcRole !=null && notTcRole.size() > 0) {
            		cond_sql += " OR ";
            	}
                if(notTcRole !=null && notTcRole.size() > 0) {
                	Vector usr_id_vec = DbTrainingCenter.getMgtResoursUsrEntId(con, notTcRole, prof.root_ent_id, s_tcr_id);
                	tableName = cwSQL.createSimpleTemptable(con, "tmp_ent_ids", cwSQL.COL_TYPE_LONG, 0);
                	cwSQL.insertSimpleTempTable(con, tableName, usr_id_vec, cwSQL.COL_TYPE_LONG);
                	cond_sql +=" usr_ent_id IN ( Select * From " + tableName +")";
                }
                cond_sql +="  )";
            }

            if (s_status == null || s_status.length == 0){
                s_status = defSearchStatus;
            }
            cond_sql += " AND usr_status IN " + cwUtils.array2list(s_status).toUpperCase() + " ";

            //except lrn in this itm,find all others
//            if(!(this.ils_id>0)){
            if (s_itm_id != 0) {
                //cond_sql += " AND ent_id ";

                //if (s_search_enrolled != null && s_search_enrolled.equalsIgnoreCase("0")) {
                    //cond_sql += " NOT ";
                //}


                //cond_sql += " IN (" + OuterJoinSqlStatements.searchEnrolledEndIds(con, s_itm_id) + " )";


                //if (s_search_enrolled != null && s_search_enrolled.equalsIgnoreCase("0")) {
                    //if( !aeItem.isItemRetakable(con, s_itm_id) ) {
                        cond_sql += " AND ent_id NOT IN ( "
                                 + "Select app_ent_id From aeApplication, aeAttendance ,aeAttendanceStatus Where ( app_itm_id = " + s_itm_id + " Or app_itm_id IN ( SELECT ire_child_itm_id FROM aeItemRelation WHERE ire_parent_itm_id = "+ s_itm_id +" )) And app_id = att_app_id  "
                                 + " And ats_id = att_ats_id And ats_type = 'PROGRESS'  "
                                 + " union select  app_ent_id from aeApplication where lower(app_status) = 'pending' and app_itm_id = " + s_itm_id + " ) ";
                    //}
                //}
            }
//            }
        if(this.ils_id>0){
            if(this.s_itm_code!=null && this.s_itm_code.length()>0){
                cond_sql += " AND usr_ent_id IN( "
                         +  "select iac_ent_id from aeItemAccess where iac_access_id ='INSTR_1' "
                         +  "and iac_itm_id = (select itm_id from aeItem where itm_code="
                         +  "'"+s_itm_code+"'"
                         +  " ) "
                         +  " ) ";
            }
            if(this.s_itm_title!=null && this.s_itm_title.length()>0){
                cond_sql += " AND ( usr_ent_id IN( "
                         +  "select iac_ent_id from aeItemAccess where iac_access_id ='INSTR_1' "
                         +  "and iac_itm_id in (select itm_id from aeItem where itm_title ";
                if(this.s_is_whole_word_match_itm_title){
                    cond_sql += " = ? ";
                }else{
                    cond_sql += "like ? ";
                }

                cond_sql += " )) ";
            }

            if(this.s_ils_title!=null && this.s_ils_title.length()>0){
                if(this.s_itm_title!=null && this.s_itm_title.length()>0){
                    cond_sql += " or ";
                }else{
                    cond_sql += " AND ";
                }
                cond_sql += "usr_ent_id IN( "
                         +  "select ili_usr_ent_id from aeItemLessonInstructor where ili_ils_id in ("
                         +  "select ils_id from aeItemLesson where ils_title ";

                if(this.s_is_whole_word_match_ils_title){
                    cond_sql += " = ? ";
                }else{
                    cond_sql += "like ? ";
                }
                if(this.s_itm_title!=null && this.s_itm_title.length()>0){
                    cond_sql += " ))) ";
                }else{
                    cond_sql += " )) ";
                }
            }else{
                if(this.s_itm_title!=null && this.s_itm_title.length()>0){
                cond_sql += " ) ";
                }
             }
            if(this.remove_ils_instr){
                cond_sql += "AND usr_ent_id IN(select ili_usr_ent_id from aeItemLessonInstructor where "
                +  "ili_ils_id= "
                +  ils_id
                +  " ) ";
            }else{
                cond_sql += "AND usr_ent_id NOT IN(select ili_usr_ent_id from aeItemLessonInstructor where "
                         +  "ili_ils_id= "
                         +  ils_id
                         +  " ) ";
            }
         }

            //Search used belong to the approver specified
            if(this.s_appr_ent_id > 0 && this.s_appr_rol_ext_id != null && this.s_appr_rol_ext_id.length() > 0 ) {

                cond_sql += " AND usr_ent_id IN ( "
                         + ViewRoleTargetGroup.getTargetGroupsLrnSQL(con, this.s_appr_ent_id, this.s_appr_rol_ext_id, false, v_sql_param)
                         + " ) ";

            }
            fromSession = false;
        }

        //search all extension field
        if (s_ext_col_names != null && s_ext_col_names.size()>0) {
            for (int i = 0;i < s_ext_col_names.size();i++) {
                String att_name = (String)s_ext_col_names.get(i);
                int startPos = att_name.indexOf("extension_");
                if (startPos != -1) {
                    int extPosition = Integer.valueOf(att_name.substring(startPos+10, att_name.indexOf("_", startPos+10))).intValue();
                    if (s_ext_col_types.get(i).equals(DbTable.COL_TYPE_STRING)) {
                        String sql = null;
                        if (extPosition >0 && extPosition<=10 && ((String)s_ext_col_values.get(i)).length() > 0) {
                            sql = " AND usr_extra_" + extPosition + " LIKE ? ";
                        } else if (extPosition > 20 && extPosition <= 30 && ((String)s_ext_col_values.get(i)).length() > 0) {
                            sql = " AND urx_extra_singleoption_" + extPosition + " = ? ";
                        } else if (extPosition > 30 && extPosition <= 40 && ((String[])s_ext_col_values.get(i)).length > 0) {
                            StringBuffer temp = new StringBuffer();
                            String[] value = (String[])s_ext_col_values.get(i);
                            if (value!=null && value.length >0) {
                                for( int j = 0;j < value.length; j++) {
                                    if (j==0) {
                                        temp.append(" AND urx_extra_multipleoption_").append(extPosition).append(" LIKE '% ").append(value[j]).append(" %'");
                                    }else {
                                        temp.append(" OR urx_extra_multipleoption_").append(extPosition).append(" LIKE '% ").append(value[j]).append(" %'");
                                    }
                                }
                            }
                            sql = temp.toString();
                        }
                        if (sql!=null) {
                            cond_sql += sql;
                        }
                    }
                    if (s_ext_col_types.get(i).equals(DbTable.COL_TYPE_TIMESTAMP)) {
                        String sql = null;
                        if (s_ext_col_values.get(i) != "") {
                            if (att_name.endsWith("_fr")) {
                                sql = " AND urx_extra_datetime_" + extPosition + " >= ? ";
                            } else{
                                sql = " AND urx_extra_datetime_" + extPosition + " <= ? ";
                            }
                        }
                        if (sql!=null) {
                            cond_sql += sql;
                        }
                    }
                }
            }
        }
        StringBuffer rsCol = new StringBuffer("usr_id,usr_ent_id,usr_pwd,usr_email,usr_email_2,usr_full_name_bil,usr_initial_name_bil,usr_last_name_bil,usr_first_name_bil,usr_nickname,usr_display_bil,usr_gender,usr_bday,usr_hkid,usr_other_id_no,usr_other_id_type,usr_tel_1,usr_tel_2,usr_fax_1,usr_country_bil,usr_postal_code_bil,usr_state_bil,usr_address_bil,usr_class,usr_class_number,usr_signup_date,usr_last_login_date,usr_status,usr_upd_date,usr_ste_ent_id,usr_ste_usr_id,usr_extra_1,usr_cost_center,usr_extra_2,usr_extra_3,usr_extra_4,usr_extra_5,usr_extra_6,usr_extra_7,usr_extra_8,usr_extra_9,usr_extra_10,usr_approve_usr_id,usr_approve_timestamp,usr_approve_reason");
        rsCol.append(",urx_extra_datetime_11,urx_extra_datetime_12,urx_extra_datetime_13,urx_extra_datetime_14,urx_extra_datetime_15,urx_extra_datetime_16,urx_extra_datetime_17,urx_extra_datetime_18,urx_extra_datetime_19,urx_extra_datetime_20")
             .append(",urx_extra_singleoption_21,urx_extra_singleoption_22,urx_extra_singleoption_23,urx_extra_singleoption_24,urx_extra_singleoption_25,urx_extra_singleoption_26,urx_extra_singleoption_27,urx_extra_singleoption_28,urx_extra_singleoption_29,urx_extra_singleoption_30")
             .append(",urx_extra_multipleoption_31,urx_extra_multipleoption_32,urx_extra_multipleoption_33,urx_extra_multipleoption_34,urx_extra_multipleoption_35,urx_extra_multipleoption_36,urx_extra_multipleoption_37,urx_extra_multipleoption_38,urx_extra_multipleoption_39,urx_extra_multipleoption_40");
        StringBuffer dbTable = new StringBuffer("RegUser, Entity, RegUserExtension");
        if(searchLostFound){
        	dbTable.append(", EntityRelationHistory ");
        } else {
        	dbTable.append(", EntityRelation ");
        }
        StringBuffer tableCond = new StringBuffer("ent_id = usr_ent_id  And urx_usr_ent_id = usr_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL AND usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?");
        if(searchLostFound) {
            tableCond = new StringBuffer("ent_id = usr_ent_id AND usr_ent_id = urx_usr_ent_id AND ent_delete_usr_id IS NOT NULL AND ent_delete_timestamp IS NOT NULL AND usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp");
        }
        cond_sql = group_sql + cond_sql;
        SQL.append("SELECT ").append(rsCol).append(" FROM ");
        SQL.append(dbTable);
        SQL.append(" WHERE ").append(tableCond);
        SQL.append(cond_sql);
        int columnCount = 0;
        ResultSetMetaData rsmd = null;
        PreparedStatement stmt1 = null;
        int colType;
        if (! fromSession) {
            if ((usr_srh_col_lst ==null)&&(usr_srh_value_lst ==null)){
                //no these params,old logic
                if (s_usr_id != null)
                    //change usr_id to usr_ste_usr_id
                    //SQL.append(" AND usr_id LIKE ? ");
                    SQL.append(" AND lower(usr_ste_usr_id) LIKE ? ");

                if (s_usr_job_title != null)
                    //change usr_id to usr_ste_usr_id
                    //SQL.append(" AND usr_id LIKE ? ");
                    SQL.append(" AND lower(usr_job_title) LIKE ? ");

                if (s_usr_email != null)
                    SQL.append(" AND lower(usr_email) LIKE ? ");

                if (s_usr_email_2 != null)
                    SQL.append(" AND lower(usr_email_2) LIKE ? ");

                if (s_usr_initial_name_bil != null)
                    SQL.append(" AND lower(usr_initial_name_bil) LIKE ? ");

                if (s_usr_last_name_bil != null)
                    SQL.append(" AND lower(usr_last_name_bil) LIKE ? ");

                if (s_usr_first_name_bil != null)
                    SQL.append(" AND lower(usr_first_name_bil) LIKE ? ");

                if (s_usr_display_bil != null)
                    SQL.append(" AND lower(usr_display_bil) LIKE ? " );

                if (s_usr_nickname != null)
                    SQL.append(" AND lower(usr_nickname) LIKE ? " );

                if (s_usr_id_display_bil != null)
                    SQL.append(" AND (lower(usr_display_bil) LIKE ? OR lower(usr_ste_usr_id) LIKE ?) ");

                if (s_usr_gender != null)
                    SQL.append(" AND usr_gender = ? ");

                if (s_usr_bday_fr != null)
                    SQL.append(" AND usr_bday >= ? ");

                if (s_usr_bday_to != null)
                    SQL.append(" AND usr_bday <= ? ");

                if (s_usr_jday_fr != null)
                    SQL.append(" AND usr_join_datetime >= ? ");

                if (s_usr_jday_to != null)
                    SQL.append(" AND usr_join_datetime <= ? ");

//            if (s_usr_bplace_bil != null)
//                SQL.append(" AND usr_bplace_bil LIKE ? ");

                if (s_usr_hkid != null)
                    SQL.append(" AND lower(usr_hkid) LIKE ? ");

                if (s_usr_tel != null)
                    SQL.append(" AND (lower(usr_tel_1) LIKE ? OR lower(usr_tel_2) LIKE ?) ");

                if (s_usr_fax != null)
                    SQL.append(" AND lower(usr_fax_1) LIKE ? ");

                if (s_usr_address_bil != null)
                    SQL.append(" AND lower(usr_address_bil) LIKE ? ");

                if (s_usr_postal_code_bil != null)
                    SQL.append(" AND lower(usr_postal_code_bil) LIKE ? ");

//            if (s_role_type != null)
//                SQL.append(" AND rol_ext_id = ? ");

            }else{
                //new logics
                //two params below should be mapped one to one;colomn name -- colomn value
                if ((usr_srh_col_lst.length != usr_srh_value_lst.length)){
                    throw new cwException("The parameters are not valid!");
                }
                if (usr_srh_col_lst.length >0){

                    StringBuffer sqlStrBuf= new StringBuffer("select ");

                    StringBuffer strTmp = new StringBuffer(1024);
                    for (int i=0;i<usr_srh_col_lst.length;i++){
                        //usr_bday's type is datetime;
                        if (usr_srh_col_lst[i].startsWith("usr_bday")){
                            sqlStrBuf.append("usr_bday").append(", ");
                        }else if (usr_srh_col_lst[i].startsWith("usr_join_date")){
                            sqlStrBuf.append("usr_join_datetime").append(", ");
                        }else{
                            sqlStrBuf.append(usr_srh_col_lst[i]).append(", ");
                        }
                    }
                    sqlStrBuf.delete(sqlStrBuf.length()-2,sqlStrBuf.length());
                    //sql never get rows to upgrade the performance;
                    String sql1 = sqlStrBuf.append(" from RegUser, RegUserExtension where 1=2 and usr_ent_id = urx_usr_ent_id ").toString();

                    stmt1 = con.prepareStatement(sql1);
                    ResultSet rs1 = stmt1.executeQuery();
                    rsmd = rs1.getMetaData();
                    columnCount = rsmd.getColumnCount();

                    colType = 0;
                    StringBuffer sqlConditionBuf = new StringBuffer();
                    for(int i = 1 ;i <= columnCount; i++) {
                        colType = rsmd.getColumnType(i);
                        //usr_bday's type is datetime;>= and <= not like
                        if (colType == java.sql.Types.TIMESTAMP){
                            if (usr_srh_col_lst[i-1].equals("usr_bday_fr")) {
                                sqlConditionBuf.append(" and usr_bday >= ?");
                            }
                            if (usr_srh_col_lst[i-1].equals("usr_bday_to")) {
                                sqlConditionBuf.append(" and usr_bday <= ?");
                            }
                            if (usr_srh_col_lst[i-1].equals("usr_join_date_fr")) {
                                sqlConditionBuf.append(" and usr_join_datetime >= ?");
                            }
                            if (usr_srh_col_lst[i-1].equals("usr_join_date_to")) {
                                sqlConditionBuf.append(" and usr_join_datetime <= ?");
                            }

                        }else{
                            //all other TYPE be set to 'like'
                            sqlConditionBuf.append(" and lower(").append(usr_srh_col_lst[i-1]).append(") like ?");
                        }
                    }
                    SQL.append(sqlConditionBuf);
                }
            }
        }
        //add new search ------end

        SQL.append(" ORDER BY ");

        if (s_order_by != null && s_order_by.equalsIgnoreCase("DESC")) {
            order_by = " DESC, ";
            s_order_by = "DESC";
        } else {
            order_by = " ASC, ";
            s_order_by = "ASC";
        }

        for (int i=0; i<USG_SEARCH_SORT_ORDER.length; i++) {
            if (USG_SEARCH_SORT_ORDER[i].equalsIgnoreCase(s_sort_by)) {
                SQL.append(USG_SEARCH_SORT_ORDER[i]).append(order_by);
                sortValid = true;
            } else {
                //sortSQL.append(USG_SEARCH_SORT_ORDER[i]).append(order_by);
            }
        }

        if(s_sort_by!= null && s_sort_by.equalsIgnoreCase("USG_DISPLAY_BIL")) {
            sortValid = true;
        }

        if (! sortValid) {
            s_sort_by = USG_SEARCH_SORT_ORDER[0];
            sortSQL.append(USG_SEARCH_SORT_ORDER[0]).append(order_by);
        }

        SQL.append(sortSQL.toString());
        SQL.append("usr_ent_id");
        stmt = con.prepareStatement(SQL.toString());
        if(!searchLostFound) {
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
        } else {
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
        }

        if (! fromSession) {
            if (s_role_types != null) {
                for (int i=0; i<s_role_types.length; i++){
                    stmt.setTimestamp(index++, cur_time);
                    stmt.setTimestamp(index++, cur_time);
                }
            }

            if (s_rol_ext_id != null && s_rol_ext_id.length() > 0) {
            	if(!(s_tcr_id > 0 && tc_enabled)){
            		stmt.setTimestamp(index++, cur_time);
                    stmt.setTimestamp(index++, cur_time);
            	}
            }

            for (int i=0; i<v_sql_param.size(); i++){
                if (((String)v_sql_param.elementAt(i)).equals(ViewRoleTargetGroup.SQL_PARAM_CUR_TIME)){
                    stmt.setBoolean(index++, true);
                }
            }


            if (s_ext_col_names != null && s_ext_col_names.size()>0) {
                for (int i = 0;i < s_ext_col_names.size();i++) {
                    String att_name = (String)s_ext_col_names.get(i);
                    int startPos = att_name.indexOf("extension_");
                    if (startPos != -1) {
                        int extPosition = Integer.valueOf(att_name.substring(startPos+10, att_name.indexOf("_", startPos+10))).intValue();
                        if (s_ext_col_types.get(i).equals(DbTable.COL_TYPE_STRING)) {
                            String sql = null;
                            if (extPosition >0 && extPosition<=10 && ((String)s_ext_col_values.get(i)).length() > 0) {
                                stmt.setString(index++,'%' + ((String)s_ext_col_values.get(i)).toLowerCase()+ '%');
                            } else if (extPosition > 20 && extPosition <= 30 && ((String)s_ext_col_values.get(i)).length() > 0) {
                                stmt.setString(index++, ((String)s_ext_col_values.get(i)).toLowerCase());
                            }
                        }
                        if (s_ext_col_types.get(i).equals(DbTable.COL_TYPE_TIMESTAMP)) {
                            String sql = null;
                            if (s_ext_col_values.get(i) != "") {
                                if (att_name.endsWith("_from")) {
                                    stmt.setTimestamp(index++, (Timestamp)s_ext_col_values.get(i));
                                } else{
                                    stmt.setTimestamp(index++, (Timestamp)s_ext_col_values.get(i));
                                }
                            }
                        }
                    }
                }
            }





            if ((usr_srh_col_lst ==null)&&(usr_srh_value_lst ==null)){
                //no these params,old logic
                if (s_usr_id != null)
                    stmt.setString(index++, '%' + s_usr_id.toLowerCase() + '%');

                if (s_usr_job_title != null)
                    stmt.setString(index++, '%' + s_usr_job_title.toLowerCase() + '%');

                if (s_usr_email != null)
                    stmt.setString(index++, '%' + s_usr_email.toLowerCase() + '%');

                if (s_usr_email_2 != null)
                    stmt.setString(index++, '%' + s_usr_email_2.toLowerCase() + '%');

                if (s_usr_initial_name_bil != null)
                    stmt.setString(index++, '%' + s_usr_initial_name_bil.toLowerCase() + '%');

                if (s_usr_last_name_bil != null)
                    stmt.setString(index++, '%' + s_usr_last_name_bil.toLowerCase() + '%');

                if (s_usr_first_name_bil != null)
                    stmt.setString(index++, '%' + s_usr_first_name_bil.toLowerCase() + '%');

                if (s_usr_display_bil != null)
                    stmt.setString(index++, '%' + s_usr_display_bil.toLowerCase() + '%');

                if (s_usr_nickname != null)
                    stmt.setString(index++, '%' + s_usr_nickname.toLowerCase() + '%');

                if (s_usr_id_display_bil != null) {
                    stmt.setString(index++, '%' + s_usr_id_display_bil.toLowerCase() + '%');
                    stmt.setString(index++, '%' + s_usr_id_display_bil.toLowerCase() + '%');
                }

                if (s_usr_gender != null)
                    stmt.setString(index++, s_usr_gender);

                if (s_usr_bday_fr != null)
                    stmt.setTimestamp(index++, s_usr_bday_fr);

                if (s_usr_bday_to != null)
                    stmt.setTimestamp(index++, s_usr_bday_to);

                if (s_usr_jday_fr != null)
                    stmt.setTimestamp(index++, s_usr_jday_fr);

                if (s_usr_jday_to != null)
                    stmt.setTimestamp(index++, s_usr_jday_to);

//            if (s_usr_bplace_bil != null)
//                stmt.setString(index++, '%' + s_usr_bplace_bil + '%');

                if (s_usr_hkid != null)
                    stmt.setString(index++, '%' + s_usr_hkid.toLowerCase() + '%');

                if (s_usr_tel != null) {
                    stmt.setString(index++, '%' + s_usr_tel.toLowerCase() + '%');
                    stmt.setString(index++, '%' + s_usr_tel.toLowerCase() + '%');
                }

                if (s_usr_fax != null)
                    stmt.setString(index++, '%' + s_usr_fax.toLowerCase() + '%');

                if (s_usr_address_bil != null)
                    stmt.setString(index++, '%' + s_usr_address_bil.toLowerCase() + '%');

                if (s_usr_postal_code_bil != null)
                    stmt.setString(index++, '%' + s_usr_postal_code_bil.toLowerCase() + '%');

//            if (s_role_type != null)
//                stmt.setString(index++, s_role_type);
            }else {
                //new logics
                if (usr_srh_col_lst.length >0){
                    for(int i = 1 ;i <= columnCount; i++) {
                        colType = rsmd.getColumnType(i);
                        //usr_bday's type is datetime;
                        if (colType == java.sql.Types.TIMESTAMP){
                            stmt.setTimestamp(index++,Timestamp.valueOf(usr_srh_value_lst[i-1]));
                        }else{
                            //all other TYPE be set to String;none int ntext(clob in Oracle) in where clause
                            stmt.setString(index++,"%"+usr_srh_value_lst[i-1].trim().toLowerCase()+"%");
                        }
                    }
                }
            }
            if (stmt1 != null) stmt1.close();
        }

        if(this.s_itm_title!=null && this.s_itm_title.length()>0){
            if(this.s_is_whole_word_match_itm_title){
                stmt.setString(index++,this.s_itm_title.toLowerCase());
            }else {
                stmt.setString(index++, "%" + this.s_itm_title.toLowerCase() + "%");
            }

        }

        if(this.s_ils_title!=null && this.s_ils_title.length()>0){
            if(this.s_is_whole_word_match_ils_title){
                stmt.setString(index++,this.s_ils_title.toLowerCase());
            }else {
                stmt.setString(index++, "%" + this.s_ils_title.toLowerCase() + "%");
            }
        }
        rs = stmt.executeQuery();
        while (rs.next()) {
            count ++;
            dbRegUser tempUsr = new dbRegUser();
            if (fromSession || (count >= start && count <= end)) {
                tempUsr.usr_id = rs.getString("usr_id");
                tempUsr.usr_ent_id = rs.getLong("usr_ent_id");
                tempUsr.ent_id = tempUsr.usr_ent_id;
                tempUsr.usr_pwd = rs.getString("usr_pwd");
                tempUsr.usr_email = rs.getString("usr_email");
                tempUsr.usr_email_2 = rs.getString("usr_email_2");
                tempUsr.usr_full_name_bil = rs.getString("usr_full_name_bil");
                tempUsr.usr_initial_name_bil = rs.getString("usr_initial_name_bil");
                tempUsr.usr_last_name_bil = rs.getString("usr_last_name_bil");
                tempUsr.usr_first_name_bil = rs.getString("usr_first_name_bil");
                tempUsr.usr_display_bil = rs.getString("usr_display_bil");
                tempUsr.usr_nickname = rs.getString("usr_nickname");
                tempUsr.usr_gender = rs.getString("usr_gender");
                tempUsr.usr_bday = rs.getTimestamp("usr_bday");
                tempUsr.usr_hkid = rs.getString("usr_hkid");
                tempUsr.usr_other_id_no = rs.getString("usr_other_id_no");
                tempUsr.usr_other_id_type = rs.getString("usr_other_id_type");
                tempUsr.usr_tel_1 = rs.getString("usr_tel_1");
                tempUsr.usr_tel_2 = rs.getString("usr_tel_2");
                tempUsr.usr_fax_1 = rs.getString("usr_fax_1");
                tempUsr.usr_country_bil = rs.getString("usr_country_bil");
                tempUsr.usr_postal_code_bil = rs.getString("usr_postal_code_bil");
                tempUsr.usr_state_bil = rs.getString("usr_state_bil");
                tempUsr.usr_address_bil = rs.getString("usr_address_bil");
                tempUsr.usr_class = rs.getString("usr_class");
                tempUsr.usr_class_number = rs.getString("usr_class_number");
                tempUsr.usr_signup_date = rs.getTimestamp("usr_signup_date");
                tempUsr.usr_last_login_date = rs.getTimestamp("usr_last_login_date");
                tempUsr.usr_status = rs.getString("usr_status");
                tempUsr.usr_upd_date = rs.getTimestamp("usr_upd_date");
                tempUsr.usr_ste_ent_id = rs.getLong("usr_ste_ent_id");
                tempUsr.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                tempUsr.usr_extra_1 = rs.getString("usr_extra_1");
                tempUsr.usr_cost_center = rs.getString("usr_cost_center");
                tempUsr.usr_extra_2 = rs.getString("usr_extra_2");
                tempUsr.usr_extra_3 = rs.getString("usr_extra_3");
                tempUsr.usr_extra_4 = rs.getString("usr_extra_4");
                tempUsr.usr_extra_5 = rs.getString("usr_extra_5");
                tempUsr.usr_extra_6 = rs.getString("usr_extra_6");
                tempUsr.usr_extra_7 = rs.getString("usr_extra_7");
                tempUsr.usr_extra_8 = rs.getString("usr_extra_8");
                tempUsr.usr_extra_9 = rs.getString("usr_extra_9");
                tempUsr.usr_extra_10 = rs.getString("usr_extra_10");
                tempUsr.usr_extra_datetime_11 = rs.getTimestamp("urx_extra_datetime_11");
                tempUsr.usr_extra_datetime_12 = rs.getTimestamp("urx_extra_datetime_12");
                tempUsr.usr_extra_datetime_13 = rs.getTimestamp("urx_extra_datetime_13");
                tempUsr.usr_extra_datetime_14 = rs.getTimestamp("urx_extra_datetime_14");
                tempUsr.usr_extra_datetime_15 = rs.getTimestamp("urx_extra_datetime_15");
                tempUsr.usr_extra_datetime_16 = rs.getTimestamp("urx_extra_datetime_16");
                tempUsr.usr_extra_datetime_17 = rs.getTimestamp("urx_extra_datetime_17");
                tempUsr.usr_extra_datetime_18 = rs.getTimestamp("urx_extra_datetime_18");
                tempUsr.usr_extra_datetime_19 = rs.getTimestamp("urx_extra_datetime_19");
                tempUsr.usr_extra_datetime_20 = rs.getTimestamp("urx_extra_datetime_20");
                tempUsr.usr_extra_singleoption_21 = rs.getString("urx_extra_singleoption_21");
                tempUsr.usr_extra_singleoption_22 = rs.getString("urx_extra_singleoption_22");
                tempUsr.usr_extra_singleoption_23 = rs.getString("urx_extra_singleoption_23");
                tempUsr.usr_extra_singleoption_24 = rs.getString("urx_extra_singleoption_24");
                tempUsr.usr_extra_singleoption_25 = rs.getString("urx_extra_singleoption_25");
                tempUsr.usr_extra_singleoption_26 = rs.getString("urx_extra_singleoption_26");
                tempUsr.usr_extra_singleoption_27 = rs.getString("urx_extra_singleoption_27");
                tempUsr.usr_extra_singleoption_28 = rs.getString("urx_extra_singleoption_28");
                tempUsr.usr_extra_singleoption_29 = rs.getString("urx_extra_singleoption_29");
                tempUsr.usr_extra_singleoption_30 = rs.getString("urx_extra_singleoption_30");
                tempUsr.usr_extra_multipleoption_31 = rs.getString("urx_extra_multipleoption_31");
                tempUsr.usr_extra_multipleoption_32 = rs.getString("urx_extra_multipleoption_32");
                tempUsr.usr_extra_multipleoption_33 = rs.getString("urx_extra_multipleoption_33");
                tempUsr.usr_extra_multipleoption_34 = rs.getString("urx_extra_multipleoption_34");
                tempUsr.usr_extra_multipleoption_35 = rs.getString("urx_extra_multipleoption_35");
                tempUsr.usr_extra_multipleoption_36 = rs.getString("urx_extra_multipleoption_36");
                tempUsr.usr_extra_multipleoption_37 = rs.getString("urx_extra_multipleoption_37");
                tempUsr.usr_extra_multipleoption_38 = rs.getString("urx_extra_multipleoption_38");
                tempUsr.usr_extra_multipleoption_39 = rs.getString("urx_extra_multipleoption_39");
                tempUsr.usr_extra_multipleoption_40 = rs.getString("urx_extra_multipleoption_40");
                tempUsr.usr_approve_usr_id = rs.getString("usr_approve_usr_id");
                tempUsr.usr_approve_timestamp = rs.getTimestamp("usr_approve_timestamp");
                tempUsr.usr_approve_reason = rs.getString("usr_approve_reason");
                searchMatch.append(tempUsr.getUserShortXML(con, false, true, false,(tempUsr.usr_status != null && tempUsr.usr_status.equals(dbRegUser.USR_STATUS_PENDING))));
            }

            temp_lst.addElement(new Long(rs.getLong("usr_ent_id")));
        }

        int total = 0;
        if (! fromSession) {
            total = temp_lst.size();
            sess.setAttribute(USG_SEARCH_TIMESTAMP, cur_time);
            sess.setAttribute(USG_SEARCH_ENT_LIST, temp_lst);
            sess.setAttribute(USG_SEARCH_SORT_BY, s_sort_by);
            sess.setAttribute(USG_SEARCH_ORDER_BY, s_order_by);
        } else {
            total = usr_lst.size();
            cur_time = sess_timestamp;
        }

        stmt.close();
        if(s[1] != null) {
 		   cwSQL.dropTempTable(con,s[1]);
        }
        if(tableName !=null) {
        	 cwSQL.dropTempTable(con, tableName);
        }
        //result.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
        result.append("<group_member_list id=\"").append(usg_ent_id).append("\" grp_role=\"").append(usg_role).append("\" timestamp=\"").append(ent_upd_date);
        if(ils_id!=0){
            result.append("\" ils_id=\"").append(ils_id)
                  .append("\" itm_id=\"").append(itm_id)
                  .append("\" itm_title=\"").append(cwUtils.esc4XML(aeItem.getItemTitle(con,aeItemLesson.getIlsItmId(con,ils_id))))
                  .append("\" itm_tcr_id=\"").append(s_tcr_id);
        }
        result.append("\">").append(dbUtils.NEWL);
        //result.append(prof.asXML()).append(dbUtils.NEWL).append("<desc>").append(dbUtils.esc4XML(usg_display_bil)).append("</desc>").append(dbUtils.NEWL);
        result.append("<desc>").append(dbUtils.esc4XML(usg_display_bil)).append("</desc>").append(dbUtils.NEWL);

        // get the ancestor's id and name
        Vector ancestorTable = dbEntityRelation.getGroupAncestorList2Vc(con, usg_ent_id, false);

        result.append("<ancestor_node_list>").append(dbUtils.NEWL);
        // discard the first element of gpm_ancester which should be the self node
        // while the gpm_full_path should be started at the first node
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        for (int i=1; i<ancestorTable.size(); i++) {
        	long ancestorId = ((Long)ancestorTable.elementAt(i)).longValue();
            result.append("<node id=\"").append(ancestorId).append("\">").append(dbUtils.NEWL);
            result.append("<title>").append(dbUtils.esc4XML(entityfullpath.getEntityName(con, ancestorId))).append("</title>").append(dbUtils.NEWL);
            result.append("</node>").append(dbUtils.NEWL);
        }
        result.append("</ancestor_node_list>").append(dbUtils.NEWL);

        result.append("<search time=\"").append(cur_time).append("\" cur_page=\"").append(page).append("\" page_size=\"").append(pagesize).append("\" total=\"").append(total)
        	  .append("\" s_role_types=\"").append("").append("\" sort_by=\"").append(s_sort_by).append("\" order_by=\"").append(s_order_by)
        	  .append("\" s_usr_id_display_bil=\"").append(dbUtils.esc4XML(s_usr_id_display_bil)).append("\"/>").append(dbUtils.NEWL);
        result.append(searchMatch.toString());
        result.append("</group_member_list>").append(dbUtils.NEWL);

        return result.toString();
    }

    // Check if a group exist (check by ent_ste_uid) and create if not exist
    // pre-set value : usg_display_bil, usg_ent_id_root, ent_ste_uid
    public long checkAndCreateGroup(Connection con, long parent_id, String usr_id)
            throws SQLException , qdbErrMessage , qdbException {

        usg_ent_id = getEntIdBySteUid(con);

        if (usg_ent_id == 0) {
            ins(con, parent_id, usr_id);
        }

        return usg_ent_id;
    }


    public static Vector getUserEntityIdVec(Connection con, Vector entId)
            throws SQLException, qdbException {

        return getUserEntityIdVec(con, entId, false);

    }

    public static Vector getUserEntityIdVec(Connection con, Vector entId, boolean checkStatus)
        throws SQLException, qdbException {

            StringBuffer idStr = new StringBuffer().append("(0");
            for(int i=0; i<entId.size(); i++)
                idStr.append(COMMA).append((Long)entId.elementAt(i));
            idStr.append(")");

            String SQL = " SELECT ern_child_ent_id "
                       + " FROM EntityRelation, Entity "
                       + " , RegUser ";
            SQL+= " WHERE ern_child_ent_id = ent_id"
                                            + " AND ent_type = ? "
                                            + " AND usr_status <> ? "
                                            + " AND ern_ancestor_ent_id IN "
                                            + idStr
                                            + " AND ern_child_ent_id = usr_ent_id "
                                            + " AND ent_delete_usr_id IS NULL "
                                            + " AND ent_delete_timestamp IS NULL "
                                            + " AND ern_parent_ind = ? ";

            if(checkStatus)
                SQL += " AND usr_status <> ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, "USR");
            stmt.setString(2, dbRegUser.USR_STATUS_PENDING);
            stmt.setBoolean(3, true);
            if(checkStatus)
                stmt.setString(4, dbRegUser.USR_STATUS_DELETED);
            ResultSet rs = stmt.executeQuery();
            Vector idVec = new Vector();
            while( rs.next() )
                idVec.addElement(new Long(rs.getLong("ern_child_ent_id")));

            stmt.close();
            return idVec;
        }


    // construct a long array of entity id
    public static long[] constructEntId(Connection con, String ent_id_str)
            throws SQLException, qdbException {

        if( ent_id_str == null || ent_id_str.length() == 0 )
            return new long[0];

        long[] entIds = dbUtils.string2LongArray(ent_id_str, "~");

        Vector idVec = returnIdVec(con, entIds);
        Vector usrEntIdVec = (Vector)idVec.elementAt(0);
        Vector grpEntIdVec = (Vector)idVec.elementAt(1);

        // add user entity into vecor
        Vector entIdVec = new Vector();
        for(int i=0; i<usrEntIdVec.size(); i++)
            entIdVec.addElement(usrEntIdVec.elementAt(i));

        /*
        Vector allGroups = new Vector();
        for(int i=0; i<grpEntIdVec.size(); i++) {
            allGroups.addElement(grpEntIdVec.elementAt(i));
            Vector subGroups = getMemberGroupVec(con, ((Long)grpEntIdVec.elementAt(i)).longValue());
            for(int j=0; j<subGroups.size(); j++)
                allGroups.addElement(subGroups.elementAt(j));
        }
        */

        Vector allGroups = new Vector();
        allGroups.addAll(grpEntIdVec);
        allGroups.addAll(getAllSubGroupVec(con, grpEntIdVec));
        dbUtils.removeDuplicate(allGroups);

        Vector usrIdVec = getUserEntityIdVec(con, allGroups, true);
        for(int i=0; i<usrIdVec.size(); i++)
            entIdVec.addElement(usrIdVec.elementAt(i));

        dbUtils.removeDuplicate(entIdVec);


        long[] ent_id = new long[entIdVec.size()];
        for(int i=0; i<ent_id.length; i++)
            ent_id[i] = ((Long)entIdVec.elementAt(i)).longValue();

        return ent_id;

    }


    // return a vector of 2 elements
    // element 0 : user entity id vector, element 1 : group entity id vector
    public static Vector returnIdVec(Connection con, long[] ids)
            throws SQLException, qdbException {

        Vector idsVec = new Vector();
        Vector usrEntIdVec = new Vector();
        Vector grpEntIdVec = new Vector();

        StringBuffer idStr = new StringBuffer().append("( 0");
        for(int i=0; i<ids.length; i++)
            idStr.append(COMMA).append(ids[i]);
        idStr.append(")");

        String dbUserGroup_GET_USER_ID_GROUP_ID = " SELECT ent_id, ent_type "
                + " FROM Entity "
                + " WHERE ent_id IN "
                + idStr.toString()
                + " AND ent_delete_usr_id IS NULL "
                + " AND ent_delete_timestamp IS NULL ";

        PreparedStatement stmt = con.prepareStatement(dbUserGroup_GET_USER_ID_GROUP_ID);
        ResultSet rs = stmt.executeQuery();
        String type;
        while( rs.next() ) {
            type = rs.getString("ent_type");
            if( type.equalsIgnoreCase("USR") )
                usrEntIdVec.addElement(new Long(rs.getLong("ent_id")));
            else if( type.equalsIgnoreCase("USG") )
                grpEntIdVec.addElement(new Long(rs.getLong("ent_id")));
        }
        idsVec.addElement(usrEntIdVec);
        idsVec.addElement(grpEntIdVec);
        stmt.close();
        return idsVec;
    }


    /**
     * Get all subgroups
     */
    public static Vector getAllSubGroupVec(Connection con, Vector vec)
            throws SQLException {

        if( vec == null || vec.size() == 0 )
            return new Vector();

        String SQL = " SELECT ern_child_ent_id From EntityRelation "
                   + " WHERE ( ern_ancestor_ent_id = ? ";
        for(int i=1; i<vec.size(); i++)
            SQL += " OR ern_ancestor_ent_id = ? ";
        SQL += " ) ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        for(int i=0; i<vec.size(); i++)
            stmt.setLong(i+1, ((Long)vec.elementAt(i)).longValue());

        ResultSet rs = stmt.executeQuery();
        Vector usgIdVec = new Vector();
        while(rs.next())
            usgIdVec.addElement(new Long(rs.getLong("ern_child_ent_id")));
        stmt.close();
        return usgIdVec;
    }



    public static String getEntityXml(Connection con, Vector vec)
            throws SQLException, cwException {
        String COMMA = ",";
        StringBuffer xml = new StringBuffer();
        StringBuffer str = new StringBuffer().append("(0");
        for(int i=0; i<vec.size(); i++)
            str.append(COMMA).append(vec.elementAt(i));
        str.append(")");

        String dbUserGroup_GET_USR_ENTITY_DETIAL = " SELECT usr_ste_usr_id, usr_ent_id, usr_display_bil"
                + " FROM RegUser "
                + " WHERE usr_ent_id IN "
                + str.toString();

        PreparedStatement stmt = con.prepareStatement(dbUserGroup_GET_USR_ENTITY_DETIAL);
        ResultSet rs = stmt.executeQuery();
        long ent_id;
        //String type;
        String usr_id;
        String display_name;
        while( rs.next() ) {
            ent_id = rs.getLong("usr_ent_id");
            usr_id = rs.getString("usr_ste_usr_id");
            //type = rs.getString("ent_type");
            display_name = rs.getString("usr_display_bil");

            xml.append("<entity id=\"").append(ent_id).append("\" ");
            xml.append(" type=\"USR\" ");
            xml.append(" display_name=\"").append(dbUtils.esc4XML(display_name)).append("\">");
            xml.append(usr_id).append("</entity>").append(dbUtils.NEWL);

        }
        stmt.close();


        String dbUserGroup_GET_USG_ENTITY_DETIAL = " SELECT usg_ent_id, usg_display_bil "
                + " FROM UserGroup "
                + " WHERE usg_ent_id IN "
                + str.toString();

        stmt = con.prepareStatement(dbUserGroup_GET_USG_ENTITY_DETIAL);
        rs = stmt.executeQuery();
        while( rs.next() ) {
            ent_id = rs.getLong("usg_ent_id");
            display_name = rs.getString("usg_display_bil");

            xml.append("<entity id=\"").append(ent_id).append("\" ");
            xml.append(" type=\"USG\" >");
            xml.append(dbUtils.esc4XML(display_name)).append("</entity>").append(dbUtils.NEWL);

        }

        stmt.close();
        return xml.toString();
    }


    // Count number of user under the specified groups
    public static long getTotalUser(Connection con, Vector groupIdVec)
            throws SQLException, cwException {

        String SQL = " SELECT COUNT(ern_child_ent_id) "
                   + " FROM EntityRelation, Entity  "
                   + " WHERE ern_ancestor_ent_id IN "
                   + cwUtils.vector2list(groupIdVec)
                   + " AND ern_child_ent_id = ent_id "
                   + " AND ent_type = ? "
                   + " AND ent_delete_usr_id IS NULL "
                   + " AND ent_delete_timestamp IS NULL "
                   + " AND ern_parent_ind = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, "USR");
        stmt.setBoolean(2, true);
        ResultSet rs = stmt.executeQuery();
        long total;
        if( rs.next() )
            total = rs.getLong(1);
        else
        {
            stmt.close();
            throw new cwException(" Error with the group id = " + cwUtils.vector2list(groupIdVec) );
        }

        rs.close();
        stmt.close();
        return total;

    }

    public static Vector getUserParentEntIds(Connection con, long ent_id) throws SQLException {
        Vector vec = new Vector();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_my_user_group);
        stmt.setLong(1, ent_id);
        stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(3, true);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            vec.addElement(new Long(rs.getLong("ern_ancestor_ent_id")));
        }

        stmt.close();

        return vec;
    }



    /**
    Get the user immediate parent group title of the specified users entity id
    @param vector of user entity id
    @return hastable, key : user entity id, value : vector containg group title
    */
    public static Hashtable getUserGroupTitle(Connection con, Vector entIdVec)
            throws SQLException, cwException {

        StringBuffer entIdList = new StringBuffer().append("(0");
        for(int i=0; i<entIdVec.size(); i++)
            entIdList.append(",").append(entIdVec.elementAt(i));
        entIdList.append(")");

        String SQL = " SELECT usr_ent_id , usg_display_bil "
                   + " FROM UserGroup, EntityRelation, RegUser "
                   + " WHERE usg_ent_id = ern_ancestor_ent_id "
                   + " AND usr_ent_id = ern_child_ent_id "
                   + " AND usr_ent_id IN "
                   + entIdList.toString()
                   + " AND ern_parent_ind = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setBoolean(1, true);
        ResultSet rs = stmt.executeQuery();
        Hashtable userGroupTable = new Hashtable();
        Vector titleVec;
        Long usr_ent_id;
        while( rs.next() ) {
            usr_ent_id = new Long(rs.getLong("usr_ent_id"));
            if( userGroupTable.get(usr_ent_id)!= null )
                titleVec = (Vector)userGroupTable.get(usr_ent_id);
            else
                titleVec = new Vector();
            titleVec.addElement(rs.getString("usg_display_bil"));
            userGroupTable.put(usr_ent_id, titleVec);
        }
        stmt.close();
        return userGroupTable;
    }

    public static Hashtable getDisplayName(Connection con, String ent_id_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("SELECT usg_ent_id, usg_display_bil FROM UserGroup WHERE usg_ent_id IN " + ent_id_lst);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            hash.put(rs.getString("usg_ent_id"), rs.getString("usg_display_bil"));
        }
        rs.close();
        stmt.close();

        return hash;
    }

    public static String getDisplayBil(Connection con, long usg_ent_id) throws SQLException {
        String usgDisplayBil = "";
        PreparedStatement stmt = con.prepareStatement("SELECT usg_display_bil FROM UserGroup WHERE usg_ent_id = " + usg_ent_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            usgDisplayBil = rs.getString("usg_display_bil");
        }

        stmt.close();

        return usgDisplayBil;
    }

    public static Vector getAllGroup(Connection con, long rootEntId) throws SQLException{

		PreparedStatement stmt = con.prepareStatement(
			"SELECT usg_ent_id " +
			"FROM UserGroup, Entity " +
			"WHERE usg_ent_id_root = ? " +
			"And ent_id = usg_ent_id " +
			"And ent_delete_usr_id is null ");
        stmt.setLong(1, rootEntId);

        Vector vtGroup = new Vector();
        ResultSet rs = stmt.executeQuery();

        long tempGroupId;
        while (rs.next()) {
            tempGroupId = rs.getLong("usg_ent_id");
            vtGroup.addElement(new Long(tempGroupId));
        }
        stmt.close();
        return vtGroup;
    }

    /*
    *   for update, ins
    *   check unique
    *   check for those ent_id != target group
    *   pls specific usg_ent_id (usg_ent_id may be zero where insert)
    *
    */
    public boolean checkUIdDuplicate(Connection con, long site_id)
            throws qdbException
    {
        try {
            if (ent_ste_uid==null){
                return false;
            }

            PreparedStatement stmt = con.prepareStatement(
                    "  SELECT count(*) FROM Usergroup, Entity "
                    +  "  WHERE ent_ste_uid = ? "
                    +  "  AND usg_ent_id_root = ? "
                    +  "  AND usg_ent_id != ? "
                    +  "  AND usg_ent_id = ent_id "
                    +  "  AND ent_delete_usr_id IS NULL "
                    +  "  AND ent_delete_timestamp IS NULL ");


            stmt.setString(1, ent_ste_uid);
            stmt.setLong(2, site_id);
            stmt.setLong(3, usg_ent_id);

            ResultSet rs = stmt.executeQuery();
            boolean bExist = false;
            if(rs.next()) {
                if (rs.getInt(1) > 0)
                    bExist = true;
            }else {
                stmt.close();
                throw new qdbException("Failed to get group info.");
            }

            stmt.close();
            return bExist;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    private String getApproverForInsert(Connection con, long root_ent_id)
        throws SQLException, cwException {

        StringBuffer xmlBuf = new StringBuffer(256);
        //get approver roles in org
        Vector vTemp = dbUtils.getOrgApprRole(con, root_ent_id);
        Vector vApprRole = (Vector)vTemp.elementAt(0);
        Vector vApprRoleType = (Vector)vTemp.elementAt(1);
        //for each role
        xmlBuf.append("<user_approver_list>");
        int size = vApprRole.size();
        for(int i=0; i<size; i++) {
            String apprRole = (String)vApprRole.elementAt(i);
            String apprRoleType = (String)vApprRoleType.elementAt(i);
            xmlBuf.append("<approver_list role_id=\"").append(apprRole).append("\"")
                    .append(" target_ent_type=\"").append(apprRoleType).append("\">");
            xmlBuf.append("</approver_list>");
        }
        xmlBuf.append("</user_approver_list>");
        return xmlBuf.toString();
    }

    public static Hashtable getDisplayName(Connection con, long root_ent_id) throws SQLException{
        Hashtable htDisplayName = new Hashtable();
        String SQL = "SELECT ent_ste_uid, usg_display_bil FROM Entity, Usergroup WHERE usg_ent_id_root = ? AND ent_ste_uid IS NOT NULL "
                    + " AND ent_id = usg_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, root_ent_id);
            rs = stmt.executeQuery();
            while (rs.next()){
                htDisplayName.put(rs.getString("ent_ste_uid"), rs.getString("usg_display_bil"));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return htDisplayName;
    }

    public static Hashtable getDisplayName(Connection con, long root_ent_id,  Vector v_ent_ste_uid) throws SQLException{
        Hashtable htDisplayName = new Hashtable();
        if (v_ent_ste_uid.size() == 0){
            return htDisplayName;
        }
        StringBuffer ent_uid_list = new StringBuffer();
        ent_uid_list.append("(");
        for (int i=0; i<v_ent_ste_uid.size(); i++){
            if (i!=0){
                ent_uid_list.append(", ");
            }
            ent_uid_list.append("'" + v_ent_ste_uid.elementAt(i) + "'");
        }
        ent_uid_list.append(")");

        String SQL = "SELECT ent_ste_uid, usg_display_bil FROM Entity, Usergroup WHERE usg_ent_id_root = ? AND ent_ste_uid IN " + ent_uid_list.toString()
                    + " AND ent_id = usg_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL);
        stmt.setLong(1, root_ent_id);
            rs = stmt.executeQuery();
        while (rs.next()){
            htDisplayName.put(rs.getString("ent_ste_uid"), rs.getString("usg_display_bil"));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return htDisplayName;
    }

    public String getAncestorUsgXML(Connection con, loginProfile prof, boolean tcEnable) throws qdbException, SQLException {
        get(con);
        StringBuffer xmlBuffer = new StringBuffer();

        Vector ancestorIds = dbEntityRelation.getGroupAncestorList2Vc(con, usg_ent_id, false);
        xmlBuffer.append("<ancestor_node_list>").append(dbUtils.NEWL);
        AcUserGroup acug = new AcUserGroup(con);
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        for(int i=1; i<ancestorIds.size(); i++) {
        	long ancestorId = ((Long) ancestorIds.elementAt(i)).longValue();
            if(acug.canManageGroup(prof, ancestorId, tcEnable)) {

            	xmlBuffer.append("<node id=\"").append(ancestorId).append("\">").append(dbUtils.NEWL);
            	xmlBuffer.append("<title>").append(dbUtils.esc4XML(entityfullpath.getEntityName(con, ancestorId))).append("</title>").append(dbUtils.NEWL);
            	xmlBuffer.append("</node>").append(dbUtils.NEWL);
            }
        }
        xmlBuffer.append("<node id=\"").append(usg_ent_id).append("\">").append(dbUtils.NEWL);
        xmlBuffer.append("<title>").append(dbUtils.esc4XML(usg_display_bil)).append("</title>").append(dbUtils.NEWL);
        xmlBuffer.append("</node>").append(dbUtils.NEWL);

        xmlBuffer.append("</ancestor_node_list>").append(dbUtils.NEWL);

        return xmlBuffer.toString();
    }

    public static Vector getUpdatedUserGroup(Connection con, long root_ent_id, Timestamp startTime, Timestamp endTime) throws SQLException{
        Vector vtUserGroup = new Vector();
        String sql = "SELECT usg_display_bil, ent_ste_uid, usg_ent_id, usg_name , usg_desc FROM UserGroup, Entity WHERE (usg_ent_id_root = ? or usg_ent_id = ? ) and (usg_role is null or usg_role <> ?) and ent_id = usg_ent_id and ent_delete_timestamp is null ";

        if (startTime != null)
            sql += " and ent_upd_date > ? ";
        if (endTime != null)
            sql += " and ent_upd_date <= ? ";

        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, root_ent_id);
        stmt.setLong(index++, root_ent_id);
        stmt.setString(index++, USG_ROLE_SYSTEM);

        if (startTime != null)
            stmt.setTimestamp(index++, startTime);
        if (endTime != null)
            stmt.setTimestamp(index++, endTime);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            dbUserGroup usg = new dbUserGroup();
            usg.usg_ent_id = rs.getLong("usg_ent_id");
            usg.ent_id = usg.usg_ent_id;
            usg.ent_ste_uid = rs.getString("ent_ste_uid");
            usg.usg_display_bil = rs.getString("usg_display_bil");
            usg.usg_name = rs.getString("usg_name");
            usg.usg_desc = rs.getString("usg_desc");
            vtUserGroup.addElement(usg);
        }
        stmt.close();
        return vtUserGroup;
    }

    //get all the user groups for the officer(usr_ent_id), no matter the group is the top level or not.
    public static Vector getAllTargetGroupIdForOfficer(Connection con, long usr_ent_id) throws SQLException {
    	Vector targetEntity = new Vector();
	    StringBuffer sb = new StringBuffer();
		sb.append(" select distinct ern_child_ent_id")
	    	.append(" From tcTrainingCenterOfficer, tcTrainingCenterTargetEntity, tcTrainingCenter, Entity, EntityRelation")
	    	.append(" where tco_usr_ent_id = ?")
	    	.append(" and tco_tcr_id = tcr_id")
	    	.append(" and tcr_status = ?")
	    	.append(" and tce_tcr_id = tcr_id")
	    	.append(" and (tce_ent_id = ern_child_ent_id or")
	    	.append(" (ern_type = ? and ern_ancestor_ent_id = tce_ent_id)) ")
	    	.append(" and tce_ent_id = ent_id")
	    	.append(" and ent_delete_usr_id IS NULL")
	    	.append(" and ent_delete_timestamp IS NULL");
	    PreparedStatement stmt = null;
	    try {
	        stmt = con.prepareStatement(sb.toString());
	        int index = 1;
	        stmt.setLong(index++, usr_ent_id);
	        stmt.setString(index++, DbTrainingCenter.STATUS_OK);
	        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next()) {
	        	targetEntity.add(new Long(rs.getLong("ern_child_ent_id")));
	        }
	    } finally {
	        if(stmt!=null) {
	        	stmt.close();
	        }
	    }
	    return targetEntity;
    }
    
    public static Vector getAllGroupId(Connection con) throws SQLException {
    	Vector targetEntity = new Vector();
	    StringBuffer sb = new StringBuffer();
		sb.append(" select ent_id from usergroup ,entity where ent_id = usg_ent_id and ent_delete_timestamp is null");
	    	
	    PreparedStatement stmt = null;
	    try {
	        stmt = con.prepareStatement(sb.toString());
	       
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next()) {
	        	targetEntity.add(new Long(rs.getLong("ent_id")));
	        }
	    } finally {
	        if(stmt!=null) {
	        	stmt.close();
	        }
	    }
	    return targetEntity;
    }

    /*
    public static Vector getTopLevelGroupId(Connection con, long usr_ent_id) throws qdbException, SQLException {
    	Vector targetEntity = getAllTargetGroupIdForOfficer(con, usr_ent_id);
    	if(targetEntity != null && targetEntity.size() > 1) {
			Vector toRemove = new Vector();
			for (int i = 0; i < targetEntity.size(); i++) {
				Long cur_usg_id = (Long)targetEntity.elementAt(i);
				if (toRemove.contains(cur_usg_id)) {
					continue;
				}
				Vector sub_group = getChildGroupVec(con, cur_usg_id.longValue());
				for(int j=0; j<sub_group.size(); j++) {
					Long temp = (Long)sub_group.elementAt(j);
					if(targetEntity.contains(temp)){
						toRemove.add(temp);
					}
				}
			}
			for(int i=0; i<toRemove.size(); i++) {
				targetEntity.remove((Long)toRemove.elementAt(i));
			}
    	}
	    return targetEntity;
	}*/
    
    //获得可以管理的第一层用户组
    public static Vector getTopLevelGroupId(Connection con, long usr_ent_id) throws qdbException, SQLException {
        Vector targetEntity = new Vector();
        String SQL = "{call sp_get_top_level_group_id(?)}";
        
        String SQL1  ="select distinct tce_ent_id From tcTrainingCenterOfficer,                                "+
        "   tcTrainingCenterTargetEntity tp,                                                                            "+
        "   tcTrainingCenter,                                                                                        "+
        "   Entity                                                                                                  "+
        "   where tco_usr_ent_id = ? and tco_tcr_id = tcr_id                                               "+
        "   and tcr_status = 'OK' and tce_tcr_id = tcr_id                                                            "+
        "   and tce_ent_id = ent_id and ent_delete_usr_id IS NULL and ent_delete_timestamp IS NULL                   "+
        "   and  not exists(                                                                                         "+
        "       select ern_child_ent_id from EntityRelation tem where ern_type = 'USG_PARENT_USG' and                "+
        "       tp.tce_ent_id = tem.ern_child_ent_id and                                                       "+
        "       exists(                                                                                              "+
        "       select distinct ern_child_ent_id From tcTrainingCenterOfficer,                                       "+
        "       tcTrainingCenterTargetEntity,                                                                        "+
        "       tcTrainingCenter,                                                                                    "+
        "       Entity,                                                                                              "+
        "       EntityRelation ern                                                                                   "+
        "       where tco_usr_ent_id = ? and tco_tcr_id = tcr_id                                           "+
        "        and tcr_status = 'OK' and tce_tcr_id = tcr_id                                                       "+
        "        and tce_ent_id = ern_child_ent_id                                                                   "+
        "        and tce_ent_id = ent_id and ent_delete_usr_id IS NULL and ent_delete_timestamp IS NULL              "+
        "        and tem.ern_ancestor_ent_id = ern.ern_child_ent_id                                                  "+
        "       )                                                                                                    "+
        "                                                                                                            "+
        "  ) ";
        PreparedStatement stmt = null;
        try {
            
            
            
            if( cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())){
                stmt = con.prepareStatement(SQL);
                int index = 1;
                stmt.setLong(index++, usr_ent_id);
            }else{
                stmt = con.prepareStatement(SQL1);
                int index = 1;
                stmt.setLong(index++, usr_ent_id);
                stmt.setLong(index++, usr_ent_id);
            }
            
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                targetEntity.add(new Long(rs.getLong("tce_ent_id")));
            }       
        } finally {
            if(stmt!=null) {
                stmt.close();
            }
        }
        return targetEntity;
	}
    
    private void childGroupInMgtAsXML(Connection con, Vector targetEntiyId, StringBuffer xmlGroup) throws SQLException {
    	if(targetEntiyId != null && targetEntiyId.size() > 0) {
    		String usg_id_lst = cwUtils.vector2list(targetEntiyId);
    		StringBuffer sql = new StringBuffer();
    		sql.append(" select usg_ent_id AS MEID, usg_display_bil AS MUDISP, usg_role AS MGPROLE, ent_upd_date AS MDATE, usg_budget")
    		.append(" from UserGroup, Entity")
    		.append(" where usg_ent_id in")
    		.append(usg_id_lst)
    		.append(" and usg_ent_id = ent_id")
    		.append(" and ent_delete_usr_id IS NULL")
    		.append(" and ent_delete_timestamp IS NULL")
    		.append(" order by usg_display_bil");
    		PreparedStatement stmt = null;
    		try{
        		stmt = con.prepareStatement(sql.toString());
        		ResultSet rs = stmt.executeQuery();
        		getChildGroupAsXML(rs, xmlGroup);
        	 } finally {
             if (stmt != null) {
                 stmt.close();
             }
         }
    	}
    }

    public static Vector getUsgEntIdByUser(Connection con, long usr_ent_id, String rol_ext_id, boolean tcEnable) throws SQLException, qdbException {
    	Vector vec = null;
        if(tcEnable && AccessControlWZB.hasRolePrivilege(rol_ext_id, AclFunction.FTN_AMD_USR_INFO_MGT)) {
        	vec = getTopLevelGroupId(con, usr_ent_id);
        }
    	return vec;
    }

    public static Vector getAllowMaitainUsgByEntId(Connection con, long usr_ent_id, long root_ent_id) throws SQLException {
    	Vector vec = new Vector();
		StringBuffer sql = new StringBuffer();
		PreparedStatement stmt = null;
		try{
    		sql.append(" select distinct tce_ent_id  as usg_ent_id")
    			.append(" from tcTrainingCenterOfficer ,tcTrainingCenterTargetEntity, tcTrainingCenter")
    			.append(" where tco_usr_ent_id = ?")
    			.append(" and tco_tcr_id = tce_tcr_id")
    			.append(" and tce_tcr_id = tcr_id")
    			.append(" and tcr_ste_ent_id = ?")
    			.append(" and tcr_status = ?")
    			.append(" and tcr_user_mgt_ind = 1")
    			.append(" union")
    			.append(" select ern_child_ent_id as usg_ent_id")
    			.append(" from tcTrainingCenterOfficer, tcTrainingCenter, tcTrainingCenterTargetEntity")
    			.append(" left join EntityRelation on (")
    			.append(" ern_ancestor_ent_id = tce_ent_id ")
    			.append(" and ern_type = ? )")
    			.append(" where tco_usr_ent_id = ?")
    			.append(" and tco_tcr_id = tce_tcr_id")
    			.append(" and tce_tcr_id = tcr_id")
    			.append(" and tcr_ste_ent_id = ?")
    			.append(" and tcr_status = ?")
    			.append(" and tcr_user_mgt_ind = 1")
    			.append(" and ern_child_ent_id is not null")
    			.append(" and ern_child_ent_id not in(")
    			.append(" select distinct tce_ent_id as usg_ent_id")
    			.append(" from tcTrainingCenterOfficer, tcTrainingCenter, tcTrainingCenterTargetEntity")
    			.append(" where tco_usr_ent_id = ?")
    			.append(" and tco_tcr_id = tce_tcr_id")
    			.append(" and tce_tcr_id = tcr_id")
    			.append(" and tcr_ste_ent_id = ?")
    			.append(" and tcr_status = ?")
    			.append(" and tcr_user_mgt_ind = 0)");
    		
    		stmt = con.prepareStatement(sql.toString());
    		int index = 1;
    		stmt.setLong(index++, usr_ent_id);
    		stmt.setLong(index++, root_ent_id);
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		stmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
    		stmt.setLong(index++, usr_ent_id);
    		stmt.setLong(index++, root_ent_id);
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		stmt.setLong(index++, usr_ent_id);
    		stmt.setLong(index++, root_ent_id);
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()) {
    			vec.add(new Long(rs.getLong("usg_ent_id")));
    		}
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    	return vec;
    }

    /**
     * 判断用户组是否存在
     * @param con
     * @param ent_id 用户组的usg_ent_id
     * @return 用户组是否存在(true 存在 false 不存在)
     * @throws SQLException
     */
    public static boolean isUsgExists(Connection con, long ent_id)
    	throws SQLException {

    	Timestamp delTime = dbEntity.getEntityDeleteTimestamp(con, ent_id);

    	boolean flag = false;
    	if (delTime == null) {
    		flag = true;
    	}

    	return flag;
    }

    public static String[] getUserGroup(Connection con, String code) throws SQLException {

        String obj[] = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "select usg_ent_id,ent_ste_uid from userGroup,entity";
            sql += " where usg_ent_id = ent_id";
            sql += " and ent_ste_uid = ? and ent_delete_timestamp is null ";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                obj = new String[2];
                obj[0] = rs.getString("usg_ent_id");
                obj[1] = rs.getString("ent_ste_uid");
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return obj;
    }
    
    public static long getMaxUsgEntId(Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int usg_ent_id = 0;
        try {
            String SQL = "SELECT MAX(usg_ent_id) FROM UserGroup";
            pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();
          
            if (rs.next()) {
                usg_ent_id = rs.getInt(1);
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    	return usg_ent_id;
    }
    //得到子用户组编号
    public static Vector getEntSteIdVec(Connection con, long tcrId) throws SQLException{
    	 PreparedStatement pstmt = null;
		 Vector entSteIdVec = null;
	     try {
	    	 StringBuffer sb = new StringBuffer();
	    	 sb.append("select distinct ent_ste_uid from EntityRelation,Entity ")
	    	   .append(" where (ern_ancestor_ent_id in ( " )
	    	   .append("select tce_ent_id from tcTrainingCenterTargetEntity where tce_tcr_id = ?)")   
	    	   .append(" or ern_child_ent_id in(select tce_ent_id from tcTrainingCenterTargetEntity where tce_tcr_id = ?))")
	    	   .append(" and ern_type = ? and ern_child_ent_id = ent_id and ent_type = ? ");
	         pstmt = con.prepareStatement(sb.toString());
	         int index = 1;
	         pstmt.setLong(index++, tcrId);
	         pstmt.setLong(index++, tcrId);
	         pstmt.setString(index++, dbEntityRelation.ERN_TYPE_USG_PARENT_USG);
	         pstmt.setString(index++, dbEntity.ENT_TYPE_USER_GROUP);
	         ResultSet rs = pstmt.executeQuery();
	         entSteIdVec = new Vector();
	         while (rs.next()) {
	        	 entSteIdVec.add(rs.getString("ent_ste_uid"));
	         }
	     } finally {
	         if (pstmt != null) {
	             pstmt.close();
	         }
	     }
	 	return entSteIdVec;
    }
}
