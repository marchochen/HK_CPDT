/***********************************************
    Outstanding items
    1) 
************************************************/

package com.cw.wizbank.ae;

import com.cw.wizbank.accesscontrol.AcCourse;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.instructor.InstructorCos;
import com.cw.wizbank.instructor.InstructorDao;
import com.cw.wizbank.personalization.PsnBiography;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;

import java.sql.*;
import java.util.*;



public class aeItemAccess {
    
    public static final String ACCESS_TYPE_ROLE = "ROLE";
    
    public long iac_itm_id;
    public long iac_ent_id;
    public String iac_access_type;
    public String iac_access_id;
    
    public static class ViewItemAccess {
        public long iac_ent_id;
        public String iac_access_id;
        public String usr_display_bil;
        public String usr_ste_usr_id;
        public String usr_tel_1;
    }
    
    public static class ViewItemAccessGroupByItem {
        public String iac_access_type;
        public String iac_access_id;
        public long iac_itm_id;
        public String itm_code;
        public String itm_title;
        public int accessCount;
    }
    
    public aeItemAccess() {
    }

    // Save the users list given an item id, access type and access id
    public void ins(Connection con) 
        throws cwException, SQLException
    {
        PreparedStatement stmt;
        
        stmt = con.prepareStatement(
              "INSERT INTO aeItemAccess "
            + "  (iac_itm_id ,iac_ent_id ,iac_access_type ,iac_access_id ) " 
            + " VALUES "
            + "  (?,?,?,?) ");
        
        stmt.setLong(1,iac_itm_id); 
        stmt.setLong(2,iac_ent_id);
        stmt.setString(3,iac_access_type);
        stmt.setString(4,iac_access_id);
        if (stmt.executeUpdate() != 1) {
            throw new cwException("Failed to insert an itemAccess.");
        }
        stmt.close();
        
    }

    public void ins(Connection con, long cos_res_id) throws SQLException, cwException {
        
        ins(con);
        AcCourse accos = new AcCourse(con);
        boolean read = true;
        boolean write = true;
        boolean exec = true;
        
        try {
            dbResourcePermission.save(con, cos_res_id, this.iac_ent_id , 
                                      this.iac_access_id, 
                                      read, write, exec);
        }
        catch(qdbException e) {
            throw new cwException (e.getMessage());
        }
        return;
    }

    // delete item access given an entity id 
    // return : number of row deleted
    public static int delByEntId(Connection con, long iac_ent_id)
        throws SQLException
    {
        PreparedStatement stmt;
        
        stmt = con.prepareStatement(
              "DELETE From aeItemAccess WHERE "
            + "     iac_ent_id = ? ");
        
        stmt.setLong(1,iac_ent_id);

        int rowCnt = stmt.executeUpdate();

        stmt.close();

        return rowCnt;
    }
    
    // delete item access given a item id 
    // return : number of row deleted
    public static int delByItemId(Connection con, long iac_itm_id)
        throws SQLException
    {
        PreparedStatement stmt;
        
        stmt = con.prepareStatement(
              "DELETE From aeItemAccess WHERE "
            + "     iac_itm_id = ? ");
        
        stmt.setLong(1,iac_itm_id);

        int rowCnt = stmt.executeUpdate();

        stmt.close();

        return rowCnt;
    }

    // delete item access given a item id and access type
    // return : number of row deleted
    public int delByTypeAndId(Connection con)
        throws SQLException
    {
        PreparedStatement stmt;
        
        stmt = con.prepareStatement(
              "DELETE From aeItemAccess WHERE "
            + "     iac_itm_id = ? "
            + " AND iac_access_type = ? "
            + " AND iac_access_id = ? ");
        
        stmt.setLong(1,iac_itm_id);
        stmt.setString(2,iac_access_type);
        stmt.setString(3,iac_access_id);

        int rowCnt = stmt.executeUpdate();

        stmt.close();

        return rowCnt;
        
    }

    /**
    Save the item access records<BR>
    Pre-define variable<BR>
    <ul>
    <li>iac_access_id
    </ul>
    @param qdb_ind save records in ResourcePermission as well if true
    */
    public int saveByRole(Connection con, long[] entIds, boolean qdb_ind)
        throws cwException, SQLException, cwSysMessage {

        this.iac_access_type = ACCESS_TYPE_ROLE;
        long cosId = 0;
        int rowCnt = 0;
        AcCourse accos = null;
        boolean read = false;
        boolean write = false;
        boolean exec = false;
        
        // Delete all existing record first
        aeItemAccess aeIac = new aeItemAccess();
        aeIac.iac_itm_id = iac_itm_id;
        aeIac.iac_access_type = iac_access_type;
        aeIac.iac_access_id = iac_access_id;
        aeIac.delByTypeAndId(con);

        if(qdb_ind) {
            accos = new AcCourse(con);
            cosId = aeItemAccess.getCosIdByItemId(con, this.iac_itm_id);
            read = true;
            write =true;
            exec = true;
            
            // Delete records that are not in enrollment table from ResourcePermission
            //dbResourcePermission.delCDNByResId(con, cosId);
            try {
                dbResourcePermission.delByRoleNResId(con, cosId, iac_access_id);
            }
            catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        }
        
        if(this.iac_access_id != null && this.iac_access_id.toUpperCase().startsWith("INSTR")){
            //取到已有学员给评论但现在被删除的讲师的id_vec
            Vector id_vec = InstructorDao.getCommentInsIDByItmId(con,iac_itm_id, entIds);
            if(id_vec != null && id_vec.size() > 0){
                //删除被删除的讲师在该班级下评论
                InstructorDao.delCommentByItmId( con,iac_itm_id,cwUtils.vec2longArray(id_vec)) ;
                for(int i = 0; i < id_vec.size(); i++){
                  //重算平均分
                    InstructorDao.updateInstructorScore( con,  ((Long)id_vec.elementAt(i)).longValue());
                }
            }
            //删除被删除的讲师在日程表中的设置
            aeItemLessonInstructor.delByItem( con, iac_itm_id, entIds);
        }
        
        AccessControlWZB acl = new AccessControlWZB();        
        for(int i=0; i<entIds.length; i++) {
            this.iac_ent_id = entIds[i];
            
            //check if the user really belongs to the role
            String[] roles = acl.getUserRoles(con, this.iac_ent_id);
            if(this.iac_access_id != null && this.iac_access_id.toUpperCase().startsWith("INSTR")){
                if(!InstructorDao.isInstructor( con ,this.iac_ent_id )){
                    throw new cwSysMessage(dbRegUser.MSG_USER_HAS_NO_SUCH_ROLE, this.iac_ent_id + ": " + this.iac_access_id);
                }
            }else if(roles == null || !dbUtils.strArrayContains(roles, this.iac_access_id)) {
            	this.iac_ent_id = (Long) dbRegUser.getEntSysUser(con).get("usr_ent_id");
                //throw new cwSysMessage(dbRegUser.MSG_USER_HAS_NO_SUCH_ROLE, this.iac_ent_id + ": " + this.iac_access_id);
            }
            ins(con);
            if(qdb_ind) {                
                //save resource permission if one of (read, write, exec) is true
                if(read == true || write == true || exec == true) {
                    try {
                        dbResourcePermission.save(con, cosId, this.iac_ent_id , 
                                                  this.iac_access_id, 
                                                  read, write, exec);
                    }
                    catch(qdbException e) {
                        throw new cwException (e.getMessage());
                    }
                }
            }
            rowCnt++;
        }
        return rowCnt;
    }
    
    // Save the users list given an item id, access type and access id
    /*
    public int saveByRole(Connection con, String[] ecdnIds, String[] ncdnIds, String[] nistIds) 
        throws cwException, SQLException
    {
        try {
            long cosId = aeItemAccess.getCosIdByItemId(con, iac_itm_id);
            iac_access_type = ACCESS_TYPE_ROLE;
            
            // Delete all course designer and istructor from ResourcePermission
            //dbResourcePermission.delCDNByResId(con, cosId);

            int rowCnt =0;
            iac_access_id = dbRegUser.ROLE_USR_ECDN;
            dbResourcePermission.delByRoleNResId(con, cosId, iac_access_id);
            rowCnt += saveByRoleAndId(con, ecdnIds, cosId);
            
            iac_access_id = dbRegUser.ROLE_USR_NCDN;
            dbResourcePermission.delByRoleNResId(con, cosId, iac_access_id);
            rowCnt += saveByRoleAndId(con, ncdnIds, cosId);
            
            iac_access_id = dbRegUser.ROLE_USR_NIST;
            dbResourcePermission.delByRoleNResId(con, cosId, iac_access_id);
            rowCnt += saveByRoleAndId(con, nistIds, cosId);

            return rowCnt;
        }catch (qdbException e) {
            throw new cwException (e.getMessage());
        }
    }
    private int saveByRoleAndId(Connection con, String[] entIds, long cosId)
        throws cwException, SQLException
    {
        try {
            aeItemAccess aeIac = new aeItemAccess();
            aeIac.iac_itm_id = iac_itm_id;
            aeIac.iac_access_type = iac_access_type;
            aeIac.iac_access_id = iac_access_id;
            
            // Delete all existing record first
            aeIac.delByTypeAndId(con);

            if (entIds == null || entIds.length ==0)
                return 0;

            // insert the new entry
            int rowCnt =0;
            AcResources acres = new AcResources(con);
            boolean read = acres.hasResPermissionRead(iac_access_id);
            boolean write = acres.hasResPermissionWrite(iac_access_id);
            boolean exec = acres.hasResPermissionExec(iac_access_id);
            
            for (int i=0;i<entIds.length;i++) {
                aeIac.iac_ent_id = Long.parseLong(entIds[i]);
                aeIac.ins(con);
                dbResourcePermission.save(con, cosId, aeIac.iac_ent_id , aeIac.iac_access_id, read, write, exec);
                rowCnt ++;
            }

            return rowCnt;
        }catch (qdbException e) {
            throw new cwException (e.getMessage());
        }
    
    }
    */
    
    // Use item id to get course id
    public static long getCosIdByItemId(Connection con, long itmId)
        throws cwException, SQLException
    {
        PreparedStatement stmt;
        ResultSet rs;
        
        stmt = con.prepareStatement(
            "SELECT cos_res_id FROM Course WHERE cos_itm_id = ? ");
            
        stmt.setLong(1,itmId);
        rs = stmt.executeQuery();
            
        long cosId = 0;
        if (rs.next()) {
            cosId = rs.getLong("cos_res_id");
        }else {
            throw new cwException("Failed to get course id.");
        }
        stmt.close();
     
        return cosId;    
    }
    
        // User item id to get course id
    public static long getItemIdByCosId(Connection con, long cosId)
        throws cwException, SQLException
    {
        PreparedStatement stmt;
        ResultSet rs;
        
        stmt = con.prepareStatement(
            "SELECT cos_itm_id FROM Course WHERE cos_res_id = ? ");
            
        stmt.setLong(1,cosId);
        rs = stmt.executeQuery();
            
        long itmId = 0;
        if (rs.next()) {
            itmId = rs.getLong("cos_res_id");
        }else {
            throw new cwException("Failed to get course id.");
        }
        stmt.close();
     
        return itmId;    
    }

    
    // get the list of user give item id, access_type and access_id 
    public String getEntityAsXML(Connection con, String[] accessIds, long tnd_id, long rootId)
        throws cwException, SQLException ,cwSysMessage
    {
            //aeItem aei = new aeItem();
            //aei.itm_id = iac_itm_id;
            //aei.get(con);
            StringBuffer xml = new StringBuffer();
            xml.append("<item_access>").append(cwUtils.NEWL);
            
            aeTreeNode tnd = new aeTreeNode();
            tnd.tnd_id = tnd_id;
            xml.append(tnd.getNavigatorAsXML(con));
            
            aeItem itm = new aeItem();
            itm.itm_id = iac_itm_id;
            itm.getItem(con);
            
            //xml.append(aei.contentAsXML(con));
            xml.append("<item id=\"").append(iac_itm_id).append("\"");
            xml.append(" code=\"").append(dbUtils.esc4XML(itm.itm_code)).append("\">").append(cwUtils.NEWL);
            xml.append("<title>").append(dbUtils.esc4XML(itm.itm_title)).append("</title>").append(cwUtils.NEWL);
            xml.append("</item>").append(cwUtils.NEWL);
            xml.append(getRoleAllEntityAsXML(con, accessIds, rootId));
            xml.append("</item_access>").append(cwUtils.NEWL);
            
            return xml.toString();
    }
    
    public StringBuffer getRoleAllEntityAsXML(Connection con, String[] accessIds, long rootId) throws cwException, SQLException {
        StringBuffer result = new StringBuffer(4000);
        
        for (int i=0;i<accessIds.length;i++) {
            result.append("<access type=\"").append(iac_access_type);
            result.append("\" id=\"").append(accessIds[i]).append("\">").append(cwUtils.NEWL);
            
            // ACCESS TYPE is ROLE
            if (iac_access_type.equalsIgnoreCase(ACCESS_TYPE_ROLE)) {
                result.append(AccessControlWZB.getRolXml(accessIds[i]));
                iac_access_id = accessIds[i];
                result.append(getEntityByRoleAsXML(con, rootId));
            }
            
            result.append("</access>").append(cwUtils.NEWL);
        }
        
        return result;
    }

    /**
    When create an item, make XML of item access for the user in profile 
    */
    public static String getCreateItemAccess(Connection con, long usr_ent_id, String rol_ext_id) 
        throws SQLException, cwException {
        StringBuffer result = new StringBuffer(1024);
        dbRegUser usr = new dbRegUser();
        usr.usr_ent_id = usr_ent_id;
        usr.ent_id = usr_ent_id;
        try {
            usr.getByEntId(con);
        }
        catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        result.append("<assigned_role_list>");        
        result.append("<role id=\"").append(rol_ext_id).append("\">");
        result.append("<entity id=\"").append(usr_ent_id).append("\"");
        result.append(" type=\"").append(dbEntity.ENT_TYPE_USER).append("\"");
        result.append(" display_bil=\"").append(dbUtils.esc4XML(usr.usr_display_bil)).append("\"/>");
        result.append("</role>");
        result.append("</assigned_role_list>");
        return result.toString();        
    }

    private static final String sql_get_assign_rol_list = 
        "Select iac_ent_id, iac_access_id, " +  cwSQL.replaceNull("usr_display_bil", "iti_name") + " as usr_display_bil, " +  cwSQL.replaceNull("usr_ste_usr_id", "iti_name") + " as usr_ste_usr_id,  " +  cwSQL.replaceNull("usr_tel_1", "iti_mobile") + " as usr_tel_1 " +
        "From aeItemAccess left join Instructorinf on(iti_ent_id = iac_ent_id ) left join RegUser on(usr_ent_id = iac_ent_id ) " +
        "Where iac_access_type = ? " + 
        "And iac_itm_id = ? " +
        "Order By iac_access_id, usr_display_bil asc ";

    // for backward compatible
    public StringBuffer getAssignedRoleList(Connection con) throws cwException, SQLException {
        return getAssignedRoleListAsXML(con, getAssignedRoleList(con, this.iac_itm_id));
    }
    
    public static ViewItemAccess[] getAssignedRoleList(Connection con, long itm_id) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(sql_get_assign_rol_list);
            stmt.setString(1, ACCESS_TYPE_ROLE);
            stmt.setLong(2, itm_id);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewItemAccess itmAcc = new ViewItemAccess();
                itmAcc.iac_ent_id = rs.getLong("iac_ent_id");
                itmAcc.iac_access_id = rs.getString("iac_access_id");
                itmAcc.usr_display_bil = rs.getString("usr_display_bil");
                itmAcc.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                itmAcc.usr_tel_1 = rs.getString("usr_tel_1");
                tempResult.addElement(itmAcc);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewItemAccess result[] = new ViewItemAccess[tempResult.size()];
        result = (ViewItemAccess[])tempResult.toArray(result);
        
        return result;
    }
    
    public StringBuffer getAssignedRoleListAsXML(Connection con, ViewItemAccess[] itmAcc) throws cwException, SQLException {
        StringBuffer result = new StringBuffer(1024);
        String prevAccId=null;
        String accId;
        long entId;
        String usrName;
        String usrTel_1;
        result.append("<assigned_role_list>");        
        int row_count = 0;
        for (int i = 0; i < itmAcc.length; i++) {
            row_count++;
            accId = itmAcc[i].iac_access_id;
            entId = itmAcc[i].iac_ent_id;
            usrName = itmAcc[i].usr_display_bil;
            usrTel_1 = itmAcc[i].usr_tel_1;
            if(prevAccId == null) {
                result.append("<role id=\"").append(accId).append("\">");
            }
            else if(!accId.equals(prevAccId)) {
                prevAccId = accId;
                result.append("</role>");
                result.append("<role id=\"").append(accId).append("\">");
            }
            result.append("<entity id=\"").append(entId).append("\"");
            result.append(" type=\"").append(dbEntity.ENT_TYPE_USER).append("\"");
            result.append(" usr_tel_1=\"").append(dbUtils.esc4XML(usrTel_1)).append("\" ");
            result.append(" biography=\"").append(PsnBiography.showBiography(con, entId)).append("\" ");
            result.append(" display_bil=\"").append(dbUtils.esc4XML(usrName)).append("\"/>");
            prevAccId = accId;
        }
        if(row_count > 0) {
            result.append("</role>");
        }
        result.append("</assigned_role_list>");
        return result;
    }

    private static final String sql_get_child_assign_rol_list = 
        " Select p.iac_ent_id, p.iac_access_id, " +  cwSQL.replaceNull("usr_display_bil", "iti_name") + " as usr_display_bil, " +  cwSQL.replaceNull("usr_tel_1", "iti_mobile") + " as usr_tel_1, 0 as selected From aeItemAccess p left join InstructorInf on(p.iac_ent_id = iti_ent_id) left join RegUser on(p.iac_ent_id = usr_ent_id) " +
        " Where p.iac_access_type = ? " +
        " And p.iac_itm_id = ? " +
        " And Not Exists (Select c.iac_ent_id from aeItemAccess c where c.iac_access_type = ? And c.iac_itm_id = ? " +
        "                 And c.iac_ent_id = p.iac_ent_id And c.iac_access_id = p.iac_access_id) " +
        " Union " +
        " Select p.iac_ent_id, p.iac_access_id,  " +  cwSQL.replaceNull("usr_display_bil", "iti_name") + " as usr_display_bil,  " +  cwSQL.replaceNull("usr_tel_1", "iti_mobile") + " as usr_tel_1, 1 as selected From aeItemAccess p  left join InstructorInf on(p.iac_ent_id = iti_ent_id)  left join RegUser on(p.iac_ent_id = usr_ent_id)  " +
        " Where p.iac_access_type = ? " +
        " And p.iac_itm_id = ? " +
        " And Exists (Select c.iac_ent_id from aeItemAccess c where c.iac_access_type = ? And c.iac_itm_id = ? " +
        "             And c.iac_ent_id = p.iac_ent_id And c.iac_access_id = p.iac_access_id) " +
        "Order By iac_access_id, usr_display_bil asc ";
    /** retired
    */
    public StringBuffer getRunAssignedRoleList(Connection con, long pItemId) 
        throws cwException, SQLException {
        return getChildAssignedRoleList(con, pItemId); 
    }

    public StringBuffer getChildAssignedRoleList(Connection con, long pItemId) 
        throws cwException, SQLException {
        StringBuffer result = new StringBuffer(1024);
        String prevAccId=null;
        String accId;
        long entId;
        String usrName;
        String tele;
        boolean selected;
        if(pItemId == 0) {
            aeItemRelation ire = new aeItemRelation();        
            //get parent item id
            ire.ire_child_itm_id = this.iac_itm_id;
            pItemId = ire.getParentItemId(con);
        }        
        result.append("<assigned_role_list>");        
        PreparedStatement stmt = con.prepareStatement(sql_get_child_assign_rol_list);
        int index = 1;
        stmt.setString(index++, ACCESS_TYPE_ROLE);
        stmt.setLong(index++, pItemId);
        stmt.setString(index++, ACCESS_TYPE_ROLE);
        stmt.setLong(index++, this.iac_itm_id);
        stmt.setString(index++, ACCESS_TYPE_ROLE);
        stmt.setLong(index++, pItemId);
        stmt.setString(index++, ACCESS_TYPE_ROLE);
        stmt.setLong(index++, this.iac_itm_id);
        ResultSet rs = stmt.executeQuery();
        int row_count = 0;
        while(rs.next()) {
            row_count++;
            accId = rs.getString("iac_access_id");
            entId = rs.getLong("iac_ent_id");
            usrName = rs.getString("usr_display_bil");
            tele = rs.getString("usr_tel_1");
            selected = rs.getBoolean("selected");
            if(prevAccId == null) {
                result.append("<role id=\"").append(accId).append("\">");
            }
            else if(!accId.equals(prevAccId)) {
                result.append("</role>");
                result.append("<role id=\"").append(accId).append("\">");
            }
            result.append("<entity id=\"").append(entId).append("\"");
            result.append(" type=\"").append(dbEntity.ENT_TYPE_USER).append("\"");
            result.append(" display_bil=\"").append(dbUtils.esc4XML(usrName)).append("\"");
            result.append(" tele=\"").append(dbUtils.esc4XML(tele)).append("\"");
            result.append(" selected=\"").append(selected).append("\"/>");
            prevAccId = accId;
        }
        rs.close();
        stmt.close();
        if(row_count > 0) {
            result.append("</role>");
        }
        result.append("</assigned_role_list>");
        return result;
    }
    
    // Get the users list given an item id, access type and access id
    public String getEntityByRoleAsXML(Connection con, long rootId) 
        throws cwException, SQLException
    {
        PreparedStatement stmt;
        ResultSet rs;
        
        StringBuffer allUsrXml = new StringBuffer();
        AccessControlWZB acl = new AccessControlWZB();
        long[] entIds = acl.containUsers(con, iac_access_id);
        if (entIds.length >0) {
            String ent_id_lst = new String("(");
            for (int i=0;i<entIds.length;i++) {
                if (i > 0) 
                    ent_id_lst += ",";
                ent_id_lst += entIds[i];
            }
            ent_id_lst += ")";
                        
            // Get the list of all users that are having the specified role
            stmt = con.prepareStatement(
                "SELECT distinct (usr_id) AS U_ID , usr_ste_usr_id AS U_SID, usr_ent_id AS U_EID "
                + "    ,usr_display_bil AS U_DISP "
                + "  FROM RegUser, UserGroup, EntityRelation WHERE "
                + "       usr_ent_id IN " + ent_id_lst 
                + "    AND usr_status = ? " 
                + "    AND ern_child_ent_id = usr_ent_id "
                + "    AND usg_ent_id= ern_ancestor_ent_id "
                + "    AND ( usg_ent_id = ? OR usg_ent_id_root = ? ) "
                + "    AND ern_parent_ind = ? ");
            
            stmt.setString(1, dbRegUser.USR_STATUS_OK);
            stmt.setLong(2, rootId);
            stmt.setLong(3, rootId);
            stmt.setBoolean(4, true);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                allUsrXml.append("<user id=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("U_SID"))))
                         .append("\" ent_id=\"").append(rs.getLong("U_EID"))
                         .append("\" display=\"").append(dbUtils.esc4XML(rs.getString("U_DISP")))
                         .append("\"/>").append(cwUtils.NEWL);
            }
            stmt.close();
        }
        
        StringBuffer gntUsrXml = new StringBuffer();
        // Get the list of users that have access 
        stmt = con.prepareStatement(
            "SELECT usr_id AS U_ID , usr_ste_usr_id AS U_SID, usr_ent_id AS U_EID "
            + "     ,usr_display_bil AS U_DISP FROM RegUser, aeItemAccess "
            + "  WHERE   iac_itm_id = ? " 
            + "    AND iac_access_type = ? "
            + "    AND iac_access_id = ? "
            + "    AND iac_ent_id = usr_ent_id ");

        stmt.setLong(1,iac_itm_id);
        stmt.setString(2,iac_access_type);
        stmt.setString(3,iac_access_id);
        rs = stmt.executeQuery();
        
        while (rs.next()) {
                gntUsrXml.append("<user id=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("U_SID"))))
                         .append("\" ent_id=\"").append(rs.getLong("U_EID"))
                         .append("\" display=\"").append(dbUtils.esc4XML(rs.getString("U_DISP")))
                         .append("\"/>").append(cwUtils.NEWL);
        }
        stmt.close();

        StringBuffer accXml = new StringBuffer();
        accXml.append("<Users>").append(cwUtils.NEWL);
        accXml.append("<All>").append(cwUtils.NEWL);
        accXml.append(allUsrXml);
        accXml.append("</All>").append(cwUtils.NEWL);
        accXml.append("<Granted>").append(cwUtils.NEWL);
        accXml.append(gntUsrXml);
        accXml.append("</Granted>").append(cwUtils.NEWL);
        accXml.append("</Users>").append(cwUtils.NEWL);
        
        return accXml.toString();
    }
 
    private static final String sql_get_respon_itm_id = 
        " Select iac_itm_id From aeItemAccess " +
        " Where iac_ent_id = ? " +
        " And iac_access_id = ? " +
        " And iac_access_type = ? ";
        
    public static Vector getResponItemAsVector(Connection con, long iac_ent_id
                                              ,String iac_access_id
                                              ,String iac_access_type) 
                                              throws SQLException {
        
        Vector v_itm_id = new Vector();
        PreparedStatement stmt = con.prepareStatement(sql_get_respon_itm_id);
        int index = 1;
        stmt.setLong(index++, iac_ent_id);
        stmt.setString(index++, iac_access_id);
        stmt.setString(index++, iac_access_type);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v_itm_id.addElement(new Long(rs.getLong("iac_itm_id")));
        }
        stmt.close();
        return v_itm_id;
    }
    
    private static final String sql_get_iac_by_itm = 
        " Select iac_access_type, iac_ent_id, iac_access_id From aeItemAccess " + 
        " Where iac_itm_id = ? ";
    /**
    Given the itm_id, get all the itam access records<BR>
    @return Vector of aeItemAccess
    */
    public static Vector<aeItemAccess> getItemAccessByItem(Connection con, long iac_itm_id) throws SQLException {
        Vector<aeItemAccess> v_iac = new Vector<aeItemAccess>();
        PreparedStatement stmt = con.prepareStatement(sql_get_iac_by_itm);
        stmt.setLong(1, iac_itm_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            aeItemAccess iac = new aeItemAccess();
            iac.iac_itm_id = iac_itm_id;
            iac.iac_ent_id = rs.getLong("iac_ent_id");
            iac.iac_access_type = rs.getString("iac_access_type");
            iac.iac_access_id = rs.getString("iac_access_id");
            v_iac.addElement(iac);
        }
        rs.close();
        stmt.close();
        return v_iac;
    }
    
    private static final String sql_get_parent_iac_by_itm = 
        " Select iac_access_type, iac_ent_id, iac_access_id From aeItemAccess, aeItem, aeItemRelation " +
        " Where iac_itm_id = itm_id " +
        " and itm_id = ire_parent_itm_id " +
        " and ire_child_itm_id = ? ";
    /**
    Given the itm_id, get all the itam access records of its parent<BR>
    @return Vector of aeItemAccess
    */
    public static Vector getParentItemAccessByItem(Connection con, long iac_itm_id) throws SQLException {
        Vector v_iac = new Vector();
        PreparedStatement stmt = con.prepareStatement(sql_get_parent_iac_by_itm);
        stmt.setLong(1, iac_itm_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            aeItemAccess iac = new aeItemAccess();
            iac.iac_itm_id = iac_itm_id;
            iac.iac_ent_id = rs.getLong("iac_ent_id");
            iac.iac_access_type = rs.getString("iac_access_type");
            iac.iac_access_id = rs.getString("iac_access_id");
            v_iac.addElement(iac);
        }
        rs.close();
        stmt.close();
        return v_iac;
    }
    
    private static final String sql_is_iac_exist =
        " Select iac_ent_id From aeItemAccess Where " +
        " iac_ent_id = ? " + 
        " And iac_itm_id = ? " +
        " And iac_access_id = ? " +
        " And iac_access_type = ? ";
    /**
    Check if this ItemAccess record exists<BR>
    Pre-define variable:
    <ul>
    <li>iac_ent_id
    <li>iac_itm_id
    <li>iac_access_id
    <li>iac_access_type
    </ul>
    */
    public boolean isExist(Connection con) throws SQLException {
        
        boolean result;
        PreparedStatement stmt = con.prepareStatement(sql_is_iac_exist);
        int index = 1;
        stmt.setLong(index++, this.iac_ent_id);
        stmt.setLong(index++, this.iac_itm_id);
        stmt.setString(index++, this.iac_access_id);
        stmt.setString(index++, this.iac_access_type);
        ResultSet rs = stmt.executeQuery();
        result = rs.next();
        stmt.close();
        return result;
    }
    
    private static final String SQL_UPD_IAC_SYN_TIMESTAMP = 
        " update aeItemAccess set iac_syn_timestamp = ? " +
        " where iac_ent_id = ? " +
        " and iac_itm_id = ? " +
        " and iac_access_id = ? ";
    
    /**
    Update this iac_syn_timestamp 
    Pre-define variables:
    iac_ent_id, iac_itm_id, iac_access_id
    */
    public void updSynTimestamp(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_UPD_IAC_SYN_TIMESTAMP);
            stmt.setTimestamp(1, cwSQL.getTime(con));
            stmt.setLong(2, this.iac_ent_id);
            stmt.setLong(3, this.iac_itm_id);
            stmt.setString(4, this.iac_access_id);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    private static final String SQL_DEL_NOT_SYN_IAC = 
        " select iac_itm_id, iac_ent_id, iac_access_id from aeItemAccess, aeItem " + 
        " where iac_access_type = ? " +
        " AND itm_type = ? " + 
        " AND itm_owner_ent_id = ? " + 
        " and (iac_syn_timestamp < ? OR iac_syn_timestamp IS NULL )" +
        " and iac_itm_id = itm_id ";
    /**
    Get not-in-syn aeItemAccess.
    @param con Connection to database
    @param synTimestamp start syn timestamp
    @param v_iac_access_id Vector of iac_access_id(String) to be deleted
    @param iac_access_type iac_access_type to be deleted
    */
    public static Vector getNotInSynIac(Connection con, Timestamp synTimestamp, String itemType, long root_ent_id, Boolean isRun,
                                      Vector v_iac_access_id, String iac_access_type) 
                                      throws SQLException, cwException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        //1st get the not-in-syn aeItemAccess into Vector v
        try {
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(SQL_DEL_NOT_SYN_IAC);
            for(int i=0; v_iac_access_id!=null && i<v_iac_access_id.size(); i++) {
                if(i==0) {
                    SQLBuf.append(" AND iac_access_id in (?,");
                }
                else if(i==v_iac_access_id.size()-1) {
                    SQLBuf.append("?)");
                }
                else {
                    SQLBuf.append("?,");
                }
            }
            if (isRun!=null)    SQLBuf.append(" and itm_run_ind = ? ");

            stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setString(index++, iac_access_type);
            stmt.setString(index++, itemType);
            stmt.setLong(index++, root_ent_id);
            stmt.setTimestamp(index++, synTimestamp);
            for(int i=0; v_iac_access_id!=null && i<v_iac_access_id.size(); i++) {
                stmt.setString(index++, (String)v_iac_access_id.elementAt(i));
            }
            if (isRun!=null)    stmt.setBoolean(index++, isRun.booleanValue());
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                aeItemAccess iac = new aeItemAccess();
                iac.iac_ent_id = rs.getLong("iac_ent_id");
                iac.iac_itm_id = rs.getLong("iac_itm_id");
                iac.iac_access_id = rs.getString("iac_access_id");
                v.addElement(iac);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }
    
    private static final String SQL_DEL_IAC = 
        " delete from aeItemAccess " +
        " where iac_ent_id = ? " +
        " and iac_itm_id = ? " +
        " and iac_access_id = ? ";
    /**
    Delete this aeItemAccess from database
    Pre-define variable: iac_ent_id, iac_itm_id, iac_access_id
    */
    public void del(Connection con) throws SQLException, cwException {
        PreparedStatement stmt = null;
        try {
//            long cosId;
            dbCourse cos = new dbCourse();
            cos.cos_itm_id = this.iac_itm_id;
            cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
            if(cos.cos_res_id != 0) {
                try {
                    dbResourcePermission.del(con, cos.cos_res_id, this.iac_ent_id, this.iac_access_id);
                }
                catch(qdbException e) {
                    throw new cwException(e.getMessage());
                }
            }
            stmt = con.prepareStatement(SQL_DEL_IAC);
            stmt.setLong(1, this.iac_ent_id);
            stmt.setLong(2, this.iac_itm_id);
            stmt.setString(3, this.iac_access_id);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }
    
    public static Vector getAccessEntVec(Connection con, String role, long itm_id) throws SQLException {
        
        Vector v_iac = aeItemAccess.getItemAccessByItem(con, itm_id);
//        if(v_iac == null || v_iac.size() == 0) {
//        //if no item access record found
//        //check it if it is a run and get the parent's item access
//            v_iac = aeItemAccess.getParentItemAccessByItem(con, itm_id);
//        }

        Vector v_ent_id = new Vector();
        Vector v_role = new Vector();

        for(int i=0; i < v_iac.size(); i++) {
            aeItemAccess iac = (aeItemAccess) v_iac.elementAt(i);
            if((iac.iac_access_type).equals(aeItemAccess.ACCESS_TYPE_ROLE)
                && role.equalsIgnoreCase(iac.iac_access_id)) {
                v_ent_id.addElement(new Long(iac.iac_ent_id));
            }
        }
        return v_ent_id;
    }

    private static final String SQL_GET_SOLE_ACCESS_ITEM 
        = " select a.iac_access_type, a.iac_access_id, a.iac_itm_id, itm_code, itm_title, count(*) cnt "
        + " from aeItemAccess a, aeItem "
        + " where a.iac_itm_id in (select b.iac_itm_id from aeItemAccess b where iac_ent_id = ? and b.iac_access_id = ?) "
        + " and itm_id = a.iac_itm_id "
        + " and a.iac_access_id = ? "
        + " group by a.iac_itm_id, itm_code, itm_title, a.iac_access_type, a.iac_access_id "
        + " having count(*) = ? "
        + " order by itm_code ";
    public static Vector getSoleAccessItem(Connection con, long iac_ent_id, String iac_access_id) throws SQLException {
        Vector v = new Vector();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_SOLE_ACCESS_ITEM);
            stmt.setLong(1, iac_ent_id);
            stmt.setString(2, iac_access_id);
            stmt.setString(3, iac_access_id);
            stmt.setInt(4, 1);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ViewItemAccessGroupByItem view = new ViewItemAccessGroupByItem();
                view.iac_access_type = rs.getString("iac_access_type");
                view.iac_access_id = rs.getString("iac_access_id");
                view.iac_itm_id = rs.getLong("iac_itm_id");
                view.itm_code = rs.getString("itm_code");
                view.itm_title = rs.getString("itm_title");
                view.accessCount = rs.getInt("cnt");
                v.addElement(view);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

	/**
	 * @param con Connection to database
	 * @param usr_ent_id entity id of the user
	 * @param rol_ext_id role external id of the user
	 * @param owner_ent_id entity id of the organization the user belongs to
	 * @return XML showing the assigned items of the user,role pair
	 */
	public static String getResponsibleItemAsXML(Connection con, long usr_ent_id, String rol_ext_id, long owner_ent_id, boolean tc_enabled , cwPagination  page) throws SQLException {
		StringBuffer xmlBuf = new StringBuffer();
		//Get the item type title from database
//		xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, owner_ent_id));

		//Prepare SQL for getting responsible items
		StringBuffer SQLBuf = new StringBuffer();
		SQLBuf.append(" select itm_type, itm_id as cos_itm_id, ")
			  .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG)).append(" as run_itm_id, ")
			  .append(" itm_title as cos_itm_title, ")
			  .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as run_itm_title, ")
			  .append(" itm_status, ")
                .append(" itm_eff_start_datetime, itm_eff_end_datetime, ")
                .append(" itm_integrated_ind, ")
                .append(" itm_blend_ind, itm_exam_ind, itm_ref_ind ")
                .append(" from aeItemAccess, aeItem ");
        SQLBuf.append(" where iac_ent_id = ? ")
                .append(" and iac_access_type = ? ")
                .append(" and iac_access_id = ? ")
                .append(" and itm_id = iac_itm_id ")
//                屏蔽考试
                .append(" and itm_exam_ind != 1 ")
                .append(" and itm_run_ind = ? ");
        SQLBuf.append(" union ")
			  .append(" select course.itm_type, course.itm_id as cos_itm_id, run.itm_id as run_itm_id, ")
			  .append(" course.itm_title as cos_itm_title, run.itm_title as run_itm_title, run.itm_status, ")
			  .append(" run.itm_eff_start_datetime, run.itm_eff_end_datetime, ")
			  .append(" course.itm_integrated_ind, ")
			  .append(" course.itm_blend_ind, course.itm_exam_ind, course.itm_ref_ind ")
			  .append(" from aeItemAccess, aeItem run, aeItemRelation, aeItem course ");
		SQLBuf.append(" where iac_ent_id = ? ")
			  .append(" and iac_access_type = ? ")
			  .append(" and iac_access_id = ? ")
                //                屏蔽考试
                .append(" and run.itm_exam_ind != 1 ")
			  .append(" and run.itm_id = iac_itm_id ")
			  .append(" and run.itm_run_ind = ? ")
			  .append(" and ire_child_itm_id = iac_itm_id ")
			  .append(" and ire_parent_itm_id = course.itm_id ");
//        SQLBuf.append(" union ")                
//              .append(" select itm_type, itm_id as cos_itm_id, ")
//              .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG)).append(" as run_itm_id, ")
//              .append(" itm_title as cos_itm_title, ")
//              .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as run_itm_title, ")
//              .append(" itm_status, ")
//              .append(" itm_eff_start_datetime, itm_eff_end_datetime, ")
//              .append(" itm_integrated_ind, ")
//              .append(" itm_blend_ind, itm_exam_ind, itm_ref_ind ")
//              .append(" from aeItem,aeItemLesson,aeItemLessonInstructor");
//        SQLBuf.append(" where itm_id = ils_itm_id")
//              .append(" and ils_id = ili_ils_id ")
//              .append(" and ili_usr_ent_id = ? ");
//        SQLBuf.append(" order by cos_itm_title asc, run_itm_title asc ");
//		
        
		//Get the item that is assigned to the user, role pair
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(SQLBuf.toString());
			int index = 1;
			Timestamp curTime = cwSQL.getTime(con);
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, ACCESS_TYPE_ROLE);
			stmt.setString(index++, rol_ext_id);
			stmt.setBoolean(index++, false);
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, ACCESS_TYPE_ROLE);
			stmt.setString(index++, rol_ext_id);
			stmt.setBoolean(index++, true);
//            stmt.setLong(index++,usr_ent_id);
			ResultSet rs = stmt.executeQuery();
			xmlBuf.append("<item_list>");
			List aeItems = new ArrayList();
			
			while(rs.next()) {
				
				Map item = new HashMap<String,Object>();
				item.put("itm_type", rs.getString("itm_type"));
				item.put("cos_itm_title", rs.getString("cos_itm_title"));
				item.put("run_itm_title", rs.getString("run_itm_title"));
				item.put("itm_status", rs.getString("itm_status"));
				item.put("itm_eff_start_datetime", rs.getTimestamp("itm_eff_start_datetime"));
				item.put("itm_eff_end_datetime", rs.getTimestamp("itm_eff_end_datetime"));
				item.put("itm_blend_ind", rs.getBoolean("itm_blend_ind"));
				item.put("itm_exam_ind", rs.getBoolean("itm_exam_ind"));
				item.put("itm_ref_ind", rs.getBoolean("itm_ref_ind"));
				item.put("itm_integrated_ind", rs.getBoolean("itm_integrated_ind"));
				item.put("cos_itm_id", rs.getLong("cos_itm_id"));
				item.put("run_itm_id", rs.getLong("run_itm_id"));
				aeItems.add(item);
				
			}
			if(aeItems.size()>0){
				//page control
				page.totalRec = aeItems.size();
				page.pageSize = 10;
				if(page.curPage==0 ){
					page.curPage =1;
				}
	            int start = page.pageSize * (page.curPage-1);
	            page.totalPage = (int)Math.ceil((float)page.totalRec/page.pageSize);
	            int count = 0;
	            for(int i=0; i< aeItems.size();i++) {
	                if (count >= start && count < start+page.pageSize) {
	                	Map item = (Map)aeItems.get(i);
	    				String itm_type = (String)item.get("itm_type");
	    				String cos_itm_title = (String)item.get("cos_itm_title");
	    				String run_itm_title = (String)item.get("run_itm_title");
	    				String itm_status = (String)item.get("itm_status");
	    				Timestamp itm_eff_start_datetime = (Timestamp)item.get("itm_eff_start_datetime");
	    				Timestamp itm_eff_end_datetime = (Timestamp)item.get("itm_eff_end_datetime");
	    				boolean itm_blend_ind = (Boolean)item.get("itm_blend_ind");
	    				boolean itm_exam_ind = (Boolean)item.get("itm_exam_ind");
	    				boolean itm_ref_ind = (Boolean)item.get("itm_ref_ind");
	    				boolean itm_integrated_ind = (Boolean)item.get("itm_integrated_ind");
	    				long cos_itm_id = (Long)item.get("cos_itm_id");
	    				long run_itm_id = (Long)item.get("run_itm_id");
	                    
	    				xmlBuf.append("<item id=\"").append(cos_itm_id).append("\"")
					      .append(" type=\"").append(itm_type).append("\"")
					      .append(" itm_integrated_ind=\"").append(itm_integrated_ind).append("\"")
					      .append(" dummy_type=\"").append(aeItemDummyType.getDummyItemType(itm_type, itm_blend_ind, itm_exam_ind, itm_ref_ind)).append("\">")
					      .append("<title>").append(cwUtils.esc4XML(cos_itm_title)).append("</title>");
						if(run_itm_id == 0) {
							xmlBuf.append("<status>").append(itm_status).append("</status>")
								  .append("<eff_start_datetime>").append(cwUtils.escNull(itm_eff_start_datetime)).append("</eff_start_datetime>")
								  .append("<eff_end_datetime>").append(cwUtils.escNull(itm_eff_end_datetime)).append("</eff_end_datetime>");
						} else {
							xmlBuf.append("<child_item id=\"").append(run_itm_id).append("\">")
								  .append("<title>").append(cwUtils.esc4XML(run_itm_title)).append("</title>")
								  .append("<status>").append(itm_status).append("</status>")
								  .append("<eff_start_datetime>").append(cwUtils.escNull(itm_eff_start_datetime)).append("</eff_start_datetime>")
								  .append("<eff_end_datetime>").append(cwUtils.escNull(itm_eff_end_datetime)).append("</eff_end_datetime>")
								  .append("</child_item>");
						}
						if(itm_integrated_ind) {
							xmlBuf.append("<resourse id=\"").append(dbCourse.getCosResId(con, cos_itm_id)).append("\"/>");
						}
						xmlBuf.append("</item>");
	                }
	                count++;
	            }
			}
			xmlBuf.append("</item_list>");
    		xmlBuf.append(page.asXML());
		} finally {
			if(stmt!=null) stmt.close();
		}
		return xmlBuf.toString();
	}

	public static void delByEntIdAndRole(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
		String sql = "DELETE From aeItemAccess WHERE iac_ent_id = ? AND iac_access_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, usr_ent_id);
		stmt.setString(index++, rol_ext_id);
		stmt.execute();
		stmt.close();
	}
	/**
	 * 获取讲师的课程
	 * @param con
	 * @param usr_ent_id
	 * @param rol_ext_id
	 * @return
	 * @throws SQLException
	 */
	public Vector<InstructorCos> getinstructorCourseAsXML(Connection con, long usr_ent_id) throws SQLException {
		StringBuffer xmlBuf = new StringBuffer();
		Vector<InstructorCos> v = new Vector<InstructorCos>();
		StringBuffer SQLBuf = new StringBuffer();
		SQLBuf.append(" select itm_type, itm_id as cos_itm_id, ")
			  .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG)).append(" as run_itm_id, ")
			  .append(" itm_title as cos_itm_title, ")
			  .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as run_itm_title, ")
			  .append(" itm_status, ")
			  .append(" itm_eff_start_datetime, itm_eff_end_datetime, ")
			  .append(" itm_integrated_ind, ")
			  .append(" itm_blend_ind, itm_exam_ind, itm_ref_ind ")
			  .append(" from aeItemAccess, aeItem ");
		SQLBuf.append(" where iac_ent_id = ? ")
			  .append(" and iac_access_type = ? ")
			  .append(" and iac_access_id in (select rol_ext_id from acRole where rol_ste_uid = 'INSTR') ")
			  .append(" and itm_id = iac_itm_id ")
			  .append(" and itm_run_ind = ? ");
	    SQLBuf.append(" union ")
			  .append(" select course.itm_type, course.itm_id as cos_itm_id, run.itm_id as run_itm_id, ")
			  .append(" course.itm_title as cos_itm_title, run.itm_title as run_itm_title, run.itm_status, ")
			  .append(" run.itm_eff_start_datetime, run.itm_eff_end_datetime, ")
			  .append(" course.itm_integrated_ind, ")
			  .append(" course.itm_blend_ind, course.itm_exam_ind, course.itm_ref_ind ")
			  .append(" from aeItemAccess, aeItem run, aeItemRelation, aeItem course ");
		SQLBuf.append(" where iac_ent_id = ? ")
			  .append(" and iac_access_type = ? ")
			  .append(" and iac_access_id in (select rol_ext_id from acRole where rol_ste_uid = 'INSTR') ")
			  .append(" and run.itm_id = iac_itm_id ")
			  .append(" and run.itm_run_ind = ? ")
			  .append(" and ire_child_itm_id = iac_itm_id ")
			  .append(" and ire_parent_itm_id = course.itm_id ");
		
//		
//        SQLBuf.append(" union ")                
//              .append(" select itm_type, itm_id as cos_itm_id, ")
//              .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG)).append(" as run_itm_id, ")
//              .append(" itm_title as cos_itm_title, ")
//              .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as run_itm_title, ")
//              .append(" itm_status, ")
//              .append(" itm_eff_start_datetime, itm_eff_end_datetime, ")
//              .append(" itm_integrated_ind, ")
//              .append(" itm_blend_ind, itm_exam_ind, itm_ref_ind ")
//              .append(" from aeItem,aeItemLesson,aeItemLessonInstructor");
//        SQLBuf.append(" where itm_id = ils_itm_id")
//              .append(" and ils_id = ili_ils_id ")
//              .append(" and ili_usr_ent_id = ? ");
//        SQLBuf.append(" order by cos_itm_title asc, run_itm_title asc ");
		
        
		//Get the item that is assigned to the user, role pair
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(SQLBuf.toString());
			int index = 1;
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, ACCESS_TYPE_ROLE);
			stmt.setBoolean(index++, false);
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, ACCESS_TYPE_ROLE);
			stmt.setBoolean(index++, true);
//            stmt.setLong(index++,usr_ent_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				InstructorCos ins_cos =new InstructorCos();
				ins_cos.setItm_type(rs.getString("itm_type"));
				ins_cos.setCos_itm_title(rs.getString("cos_itm_title")) ;
				ins_cos.setRun_itm_title(rs.getString("run_itm_title"));
				ins_cos.setItm_status(rs.getString("itm_status"));
				ins_cos.setItm_eff_start_datetime(rs.getTimestamp("itm_eff_start_datetime"));
				ins_cos.setItm_eff_end_datetime(rs.getTimestamp("itm_eff_end_datetime"));
				ins_cos.setItm_blend_ind(rs.getBoolean("itm_blend_ind"));
				ins_cos.setItm_exam_ind(rs.getBoolean("itm_exam_ind"));
				ins_cos.setItm_ref_ind(rs.getBoolean("itm_ref_ind"));
				ins_cos.setItm_integrated_ind(rs.getBoolean("itm_integrated_ind"));
				ins_cos.setCos_itm_id(rs.getLong("cos_itm_id"));
				ins_cos.setRun_itm_id(rs.getLong("run_itm_id"));
				v.add(ins_cos);
			}
		} finally {
			if(stmt!=null) stmt.close();
		}
		return v;
	}
}
