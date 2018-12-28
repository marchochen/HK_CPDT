package com.cw.wizbank.db.view;

import java.util.Vector;
import java.sql.*;
//import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.DbRoleTargetEntity;


/**
Business Layer for Targeted Groups. 
Target Groups are a set of (industry code, user group, user grade)
*/
public class ViewRoleTargetGroup{

    public static final String SQL_PARAM_CUR_TIME = "CUR_TIME";
    
    public long usrEntId;
    
    public String rolExtId;
    
    public long targetGroupId;
    
    public long[] targetEntIds;
    public Timestamp targetEffStart;
    public Timestamp targetEffEnd;

    public String[] targetEntNames;
    
    public String[] targetEntTypes;

    private Vector v_targetEntIds;

    private Vector v_targetEntNames;
    
    private Vector v_targetEntTypes;

    
    /**
    Delete all the target entities of the input entity(subordinate) and approver role
    */
    public static void delBySubordinate(Connection con, String rolExtId, long entId) 
        throws SQLException {
        
        DbRoleTargetEntity dbRte = new DbRoleTargetEntity();
        dbRte.rte_ent_id = entId;
        dbRte.rte_rol_ext_id = rolExtId;
        dbRte.del(con, false);
        return;
    }
    
    /**
    Delete all the target entities by usrEntId and rolExtId
    */
    public static void delByUserRole(Connection con, long usrEntId, String rolExtId) 
        throws SQLException {
        
        DbRoleTargetEntity dbRte = new DbRoleTargetEntity();
        dbRte.rte_usr_ent_id = usrEntId;
        dbRte.rte_rol_ext_id = rolExtId;
        dbRte.del(con, false);
        return;
    }

    /**
    @deprecated
    */
    public static void insTargetGroups(Connection con, long usrEntId, String rolExtId, 
                                       String[] groups, String createUserId, 
                                       Timestamp createTimestamp) 
                                       throws SQLException {
        Timestamp[] tmpArrStart = new Timestamp[groups.length];
        Timestamp[] tmpArrEnd = new Timestamp[groups.length];
        
        for (int i=0; i<groups.length; i++){
            tmpArrStart[i] = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);                                                        
            tmpArrEnd[i] = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
        }        
        insTargetGroups(con, usrEntId, rolExtId, groups, tmpArrStart, tmpArrEnd, createUserId, createTimestamp); 
    }

    /**
    Insert a list of groups into usrRoleTargetEntity<BR>
    @param groups String array is a list of targeted groups, e.g. {"1,3,2","5,7,74"}
    @param effStart array of size = no of group pairs
    */
    public static void insTargetGroups(Connection con, long usrEntId, String rolExtId, 
                                       String[] groups, Timestamp[] effStart, Timestamp[] effEnd, String createUserId, 
                                       Timestamp createTimestamp) 
                                       throws SQLException {
        if(groups != null) {
            long[] ent_ids;
            ViewRoleTargetGroup viTgp = new ViewRoleTargetGroup();
            viTgp.usrEntId = usrEntId;
            viTgp.rolExtId = rolExtId;
            for(int i=0; i<groups.length; i++) {
                //viTgp.targetEntIds = dbUtils.string2long(dbUtils.split(groups[i], ","));
                viTgp.targetEntIds = cwUtils.splitToLong(groups[i], ",");
                viTgp.targetEffStart = effStart[i];
                viTgp.targetEffEnd = effEnd[i];
                viTgp.insTargetGroup(con, createUserId, createTimestamp);
            }
        }
        return;
    }

    /**
    Insert a target entity group into usrRoleTargetEntity<BR>
    Pre-defined variable:
    <ul>
    <li>usrEntId
    <li>rolExtId
    <li>targetEntIds
    <li>targetEffStart
    <li>targetEffEnd
    </ul>
    */
    public void insTargetGroup(Connection con, String createUserId, Timestamp createTimestamp) 
        throws SQLException {

        DbRoleTargetEntity dbRte;
        dbRte = new DbRoleTargetEntity();
        dbRte.rte_usr_ent_id = usrEntId;
        dbRte.rte_rol_ext_id = rolExtId;
        dbRte.rte_create_usr_id = createUserId;
        dbRte.rte_create_timestamp = createTimestamp;
        dbRte.rte_eff_start_datetime = targetEffStart;
        dbRte.rte_eff_end_datetime = targetEffEnd;
        dbRte.rte_group_id = dbRte.getMaxItemTargetGroupId(con) + 1;
        for(int i=0; i<this.targetEntIds.length; i++) {
            if(this.targetEntIds[i] != 0) {
                dbRte.rte_ent_id = this.targetEntIds[i];
                dbRte.ins(con);
            }
        }
    }

    /**
    Get number of active targeted learners of a set of target groups<BR>
    */
    public static String getTargetGroupsLrnNumAsXML(Connection con, ViewRoleTargetGroup[] viTgps) throws SQLException {
        
        Vector v_targetLrn = getTargetGroupsLrn(con, viTgps);
        return targetLrnNumAsXML(v_targetLrn.size());
    }

    public static String getTargetGroupsLrnNumAsXML(Connection con, long usrEntId, String rolExtId) throws SQLException {
        Vector v_targetLrn = getTargetGroupsLrn(con, usrEntId, rolExtId, true);
        return targetLrnNumAsXML(v_targetLrn.size());
    }

    private static String targetLrnNumAsXML(long num) {
        
        StringBuffer xmlBuf = new StringBuffer(128);
        xmlBuf.append("<targeted_lrn_num>");
        xmlBuf.append("<num value=\"").append(num).append("\" />"); 
        xmlBuf.append("</targeted_lrn_num>");
        return xmlBuf.toString();
    }

    //maintain a column list to list out what param to be set before execute
    public static String getTargetGroupsLrnSQL(Connection con, ViewRoleTargetGroup[] viTgps, Vector v_param_column) throws SQLException {
        if (v_param_column== null){
            v_param_column = new Vector();
        }
        StringBuffer SQLBuf = new StringBuffer(1024);
        StringBuffer tempSQLBuf = new StringBuffer(1024);
        //for each target group, find out its targeted learners
        for(int i=0; i<viTgps.length; i++) {
            if(i != 0) {
                tempSQLBuf.append(" Union ");
            }
            tempSQLBuf.append(viTgps[i].getTargetGroupLrnSQL(v_param_column));
        }
        if(tempSQLBuf.length() > 0) {
            SQLBuf.append(" Select usr_ent_id From RegUser, EntityRelation, UserGrade ")
                  .append(" Where usr_ent_id = ern_child_ent_id ")
                  .append(" AND ern_parent_ind = ? ")
                  .append(" And ern_type = '").append(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR).append("' ")
                  .append(" And ern_ancestor_ent_id = ugr_ent_id ")
                  .append(" And ugr_type is null ")
                  .append(" And usr_ent_id in (").append(tempSQLBuf.toString()).append(")");
            v_param_column.addElement(SQL_PARAM_CUR_TIME);                    
        }
        return SQLBuf.toString();
    }

    /**
    get target learner ent_id of the input target groups
    */
    public static Vector getTargetGroupsLrn(Connection con, ViewRoleTargetGroup[] viTgps) throws SQLException {
        
        Vector v_ent_id = new Vector(); //usr_ent_id of targeted learner
        Vector v_param_column = new Vector();
        //for each target group, find out its targeted learners
        String SQL = getTargetGroupsLrnSQL(con, viTgps, v_param_column);

        if(SQL != null && SQL.length() > 0) {
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            for (int i=0; i<v_param_column.size(); i++){
                if (((String)v_param_column.elementAt(i)).equals(SQL_PARAM_CUR_TIME)){
                    stmt.setBoolean(index++, true);
                }
            }                        
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v_ent_id.addElement(new Long(rs.getLong("usr_ent_id")));
            }
            rs.close();
            stmt.close();
        }
        return v_ent_id;
    }

    /*
    get target learner ent_id of the input target groups
    public static Vector getTargetGroupsLrn(Connection con, ViewRoleTargetGroup[] viTgps) throws SQLException {
        
        Vector v_ent_id = new Vector(); //usr_ent_id of targeted learner
        Vector v_list = new Vector();   //usr_ent_id of users in each target group
        
        //for each target group, find out its targeted learners
        for(int i=0; i<viTgps.length; i++) {
            Vector v_temp = viTgps[i].getTargetGroupLrn(con);
            v_temp.addElement(new Long(0));
            v_list.addElement(cwUtils.vector2list(v_temp));
        }

        //union all the users in each target group
        if(v_list.size() > 0) {
            StringBuffer SQLBuf = new StringBuffer(256);
            SQLBuf.append("Select usr_ent_id From RegUser ");
            for(int i=0; i<v_list.size(); i++) {
                if(i == 0) {
                    SQLBuf.append(" Where usr_ent_id in ").append((String)v_list.elementAt(i));
                }
                else {
                    SQLBuf.append(" Or usr_ent_id in ").append((String)v_list.elementAt(i));
                }
            }
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v_ent_id.addElement(new Long(rs.getLong("usr_ent_id")));
            }
            stmt.close();
        }
        
        // remove manager or above from the vector
        if (v_ent_id.size() > 0) {
            PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.removeManager(con, cwUtils.vector2list(v_ent_id)));
            ResultSet rs = stmt.executeQuery();
            v_ent_id = new Vector();
            while(rs.next()) {
                v_ent_id.addElement(new Long(rs.getLong("usr_ent_id")));
            }
            stmt.close();
        }
        
        return v_ent_id;
    }
*/    
    /**
    get active target learner ent_id of a user, role pair
    */
    public static Vector getTargetGroupsLrn(Connection con, long usrEntId, String rolExtId, boolean getDisplayBil) throws SQLException {
        
        ViewRoleTargetGroup[] viTgps = getTargetGroups(con, usrEntId, rolExtId, getDisplayBil, true);
        return getTargetGroupsLrn(con, viTgps);
    }
    
    /**
    get active target learner ent_id of a user, role pair as  sql
    */
    public static String getTargetGroupsLrnSQL(Connection con, long usrEntId, String rolExtId, boolean getDisplayBil, Vector v_param_column) throws SQLException {
        
        ViewRoleTargetGroup[] viTgps = getTargetGroups(con, usrEntId, rolExtId, getDisplayBil, true);
        return getTargetGroupsLrnSQL(con, viTgps, v_param_column);
    }

    /**
    Get a SQL that will find out targeted learners' usr_ent_id of this targeted group
    maintain a column list to list out what param to be set before execute
    */
    private String getTargetGroupLrnSQL(Vector v_param_column) {
        Vector v_subSQL = new Vector();
        String ern_type=null;
        long gpm_group;
        for(int i=0; i<this.targetEntIds.length; i++) {
            
            //get member users of each target entity
            StringBuffer subSQLBuf = new StringBuffer(512);
            gpm_group = this.targetEntIds[i];
            if(this.targetEntTypes[i].equals(dbEntity.ENT_TYPE_USER)) {
                subSQLBuf.append(" (").append(this.targetEntIds[i]).append(") ");
            }else{
                if(this.targetEntTypes[i].equals(dbEntity.ENT_TYPE_INDUSTRY_CODE)) {
                	ern_type = dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC;
                }
                else if(this.targetEntTypes[i].equals(dbEntity.ENT_TYPE_USER_GRADE)) {
                	ern_type = dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR;
                }
                else if(this.targetEntTypes[i].equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                	ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                }
                subSQLBuf.append(" (Select ern_child_ent_id From EntityRelation ")
                    .append(" Where ern_type = '").append(ern_type).append("' ")
                    .append(" AND AND ern_parent_ind = ? ")
                    .append(" And ern_ancestor_ent_id =").append(gpm_group).append(") ");
                    
                v_param_column.addElement(SQL_PARAM_CUR_TIME);                    
            }
            v_subSQL.addElement(subSQLBuf.toString());
        }
        
        StringBuffer SQLBuf = new StringBuffer(1024);
        SQLBuf.append(" Select usr_ent_id From RegUser ")
              .append(" WHERE usr_status = '").append(dbRegUser.USR_STATUS_OK).append("' ");
        for(int i=0; i<v_subSQL.size(); i++) {
            SQLBuf.append(" AND usr_ent_id in ").append((String)v_subSQL.elementAt(i));
        }
        return SQLBuf.toString();
    }
    /**
    get the active target groups (usergroup, industry code, usergrade) of a user, role pair as XML
    */
    public static String getTargetGroupsAsXML(Connection con, long usrEntId, String rolExtId) throws SQLException {
        return getTargetGroupsAsXML(con, usrEntId, rolExtId, true);
    }

    /**
    get the target groups (usergroup, industry code, usergrade) of a user, role pair as XML
    */
    public static String getTargetGroupsAsXML(Connection con, long usrEntId, String rolExtId, boolean bCheckEff) throws SQLException {
        ViewRoleTargetGroup[] viTgps = getTargetGroups(con, usrEntId, rolExtId, true, bCheckEff);
        return targetGroupsAsXML(viTgps);
    }

    /**
    get the target groups (usergroup, industry code, usergrade) of an item as XML
    */
    public static String targetGroupsAsXML(ViewRoleTargetGroup[] viTgps) throws SQLException {
        
        StringBuffer xmlBuf = new StringBuffer(512);
        for(int i=0; i<viTgps.length; i++) {
            xmlBuf.append("<target_group>");
            for(int j=0; j<viTgps[i].targetEntIds.length; j++) {
                xmlBuf.append("<entity id=\"").append(viTgps[i].targetEntIds[j]).append("\"");
                xmlBuf.append(" type=\"").append(viTgps[i].targetEntTypes[j]).append("\"");
                xmlBuf.append(" eff_start_date=\"").append(dbUtils.isMinTimestamp(viTgps[i].targetEffStart) ? dbUtils.IMMEDIATE : viTgps[i].targetEffStart.toString()).append("\"");
                xmlBuf.append(" eff_end_date=\"").append(dbUtils.isMaxTimestamp(viTgps[i].targetEffEnd) ? dbUtils.UNLIMITED : viTgps[i].targetEffEnd.toString()).append("\"");
                xmlBuf.append(" display_bil=\"").append(cwUtils.esc4XML(viTgps[i].targetEntNames[j])).append("\"/>");
            }
            xmlBuf.append("</target_group>");
        }
        return xmlBuf.toString();
    }

    /**
    get the active target groups (usergroup, industry code, usergrade) of a user, role pair
    */
    public static ViewRoleTargetGroup[] getTargetGroups(Connection con, long usrEntId, String rolExtId, boolean getDisplayBil) throws SQLException {
        return getTargetGroups(con, usrEntId, rolExtId, getDisplayBil, true);
    }

    /**
    get the target groups (usergroup, industry code, usergrade) of a user, role pair
    */
    public static ViewRoleTargetGroup[] getTargetGroups(Connection con, long usrEntId, String rolExtId, boolean getDisplayBil, boolean bCheckEff) throws SQLException {
        String additionalCondition = "";
        if (bCheckEff){
            additionalCondition = "AND (rte_eff_start_datetime is null OR rte_eff_start_datetime <= ? ) "
                            + "AND (rte_eff_end_datetime is null OR rte_eff_end_datetime >= ? ) ";
        }
        final String sql_get_item_target_groups = 
            "SELECT rte_ent_id, ent_type, rte_group_id , rte_eff_start_datetime, rte_eff_end_datetime From usrRoleTargetEntity, Entity " +
            "WHERE ent_id = rte_ent_id " +
            "AND rte_usr_ent_id = ? " +
            "AND rte_rol_ext_id = ? " + 
            " AND ent_delete_usr_id IS NULL " + 
            " AND ent_delete_timestamp IS NULL " +
            additionalCondition +
            "ORDER BY rte_group_id ";

        Vector v = new Vector();
        ViewRoleTargetGroup viTgp=null;
        ViewRoleTargetGroup[] viTgps;
        long prevGroupId=0;
        long groupId;
        long entityId;
        Timestamp effStart;
        Timestamp effEnd;
        String entityType;
        PreparedStatement stmt = con.prepareStatement(sql_get_item_target_groups);
        int index = 1;
        stmt.setLong(index++, usrEntId);
        stmt.setString(index++, rolExtId);
        if (bCheckEff){
            Timestamp curTime = cwSQL.getTime(con);
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);
        }

        ResultSet rs = stmt.executeQuery();
        int rc = 0;
        while(rs.next()) {
            entityId = rs.getLong("rte_ent_id");
            entityType = rs.getString("ent_type");
            groupId = rs.getLong("rte_group_id");
            effStart = rs.getTimestamp("rte_eff_start_datetime");
            effEnd = rs.getTimestamp("rte_eff_end_datetime");

            if(prevGroupId != groupId) {
                if(rc != 0) {
                    //if is not the 1st record, add the target group to vector
                    viTgp.convertEntityIds();
                    viTgp.convertEntityTypes();
                    viTgp.convertEntityNames();
                    v.addElement(viTgp);
                }
                //new the next target group
                prevGroupId = groupId;
                viTgp = new ViewRoleTargetGroup();
                viTgp.v_targetEntIds = new Vector();
                viTgp.v_targetEntTypes = new Vector();
                viTgp.v_targetEntNames = new Vector();
                viTgp.targetEffStart = effStart;
                viTgp.targetEffEnd = effEnd;
            }
            //put the result into the target group
            viTgp.v_targetEntTypes.addElement(entityType);
            viTgp.v_targetEntIds.addElement(new Long(entityId));
            if(getDisplayBil) {
                viTgp.v_targetEntNames.addElement(getEntityName(con, entityId, entityType));
            }
            rc++;
        }
        //add the last group
        if(rc > 0) {
            viTgp.convertEntityIds();
            viTgp.convertEntityTypes();
            viTgp.convertEntityNames();
            v.addElement(viTgp);
        }
        
        //convert the Vector v to array and return
        viTgps = new ViewRoleTargetGroup[v.size()];
        for(int i=0; i<v.size(); i++) {
            viTgps[i] = (ViewRoleTargetGroup) v.elementAt(i);
        }
        rs.close();
        stmt.close();
        return viTgps;
    }

    private void convertEntityTypes() {
        
        targetEntTypes = new String[v_targetEntTypes.size()];
        for(int i=0; i<v_targetEntTypes.size(); i++) {
            targetEntTypes[i] = (String)v_targetEntTypes.elementAt(i);
        }
        return;
    }

    private void convertEntityIds() {
        
        targetEntIds = new long[v_targetEntIds.size()];
        for(int i=0; i<v_targetEntIds.size(); i++) {
            targetEntIds[i] = ((Long)v_targetEntIds.elementAt(i)).longValue();
        }
        return;
    }

    private void convertEntityNames() {
        
        targetEntNames = new String[v_targetEntNames.size()];
        for(int i=0; i<v_targetEntNames.size(); i++) {
            targetEntNames[i] = (String)v_targetEntNames.elementAt(i);
        }
        return;
    }
    
    private static final String sql_reguser = "SELECT usr_display_bil AS name FROM RegUser WHERE usr_ent_id = ? ";
    private static final String sql_usergroup = "SELECT usg_display_bil AS name FROM UserGroup WHERE usg_ent_id = ? ";
    private static final String sql_industrycode = "SELECT idc_display_bil AS name FROM IndustryCode WHERE idc_ent_id = ? ";
    private static final String sql_usergrade = "SELECT ugr_display_bil AS name FROM UserGrade WHERE ugr_ent_id = ? ";
    /**
    Get the display_bil from database
    */
    private static String getEntityName(Connection con, long ent_id, String ent_type) throws SQLException {
        
        String ent_name;
        String sql;
        if(ent_type == null) {
            ent_name = null;
        }
        else {
            if(ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                sql = sql_reguser;
            }
            else if(ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                sql = sql_usergroup;
            }
            else if(ent_type.equals(dbEntity.ENT_TYPE_INDUSTRY_CODE)) {
                sql = sql_industrycode;
            }
            else if(ent_type.equals(dbEntity.ENT_TYPE_USER_GRADE)) {
                sql = sql_usergrade;
            }
            else {
                sql = null;
            }

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, ent_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                ent_name = rs.getString("name");
            }
            else {
                ent_name = null;
            }
            rs.close();
            stmt.close();
        }
        return ent_name;
    }

    /**
    Search user hits the searching criteria -- active groups of the (IDC, USR, UGR), <BR>
    Will NOT search for ancesters of each enity in group 
    */
    public Vector searchDirectUser(Connection con, Vector v_itm_id) throws SQLException {
        
        if(this.targetEntIds != null) {
            if(v_itm_id == null) {
                v_itm_id = new Vector();
            }
                
            //make the SQL
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(" select m.rte_usr_ent_id from usrRoleTargetEntity m, RegUser r ");
            SQLBuf.append(" where rte_ent_id = ? ");
            SQLBuf.append(" and usr_ent_id = rte_usr_ent_id ");
            SQLBuf.append(" and usr_status = ? ");
            SQLBuf.append(" AND (rte_eff_start_datetime IS NULL OR rte_eff_start_datetime <= ?) ");
            SQLBuf.append(" AND (rte_eff_end_datetime IS NULL OR rte_eff_end_datetime >= ? )");
                
            if(this.rolExtId != null) {
                SQLBuf.append(" and rte_rol_ext_id = ? ");
            }
            for(int i=1; i<this.targetEntIds.length; i++) {
                SQLBuf.append(" and exists (select a.rte_group_id from usrRoleTargetEntity a where a.rte_rol_ext_id = m.rte_rol_ext_id and a.rte_usr_ent_id = m.rte_usr_ent_id and a.rte_group_id = m.rte_group_id and a.rte_rol_ext_id = m.rte_rol_ext_id and a.rte_eff_start_datetime = m.rte_eff_start_datetime and a.rte_eff_end_datetime = m.rte_eff_end_datetime and a.rte_ent_id = ? ");
            }                
            SQLBuf.append(" and ? = (Select count(*) from usrRoleTargetEntity a where a.rte_rol_ext_id = m.rte_rol_ext_id and a.rte_usr_ent_id = m.rte_usr_ent_id and a.rte_group_id = m.rte_group_id and a.rte_eff_start_datetime = m.rte_eff_start_datetime and a.rte_eff_end_datetime = m.rte_eff_end_datetime )");
            //execute and add into v_itm_id
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            Timestamp curTime = cwSQL.getTime(con);
            stmt.setLong(index++, this.targetEntIds[0]);
            stmt.setString(index++, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);                
            if(this.rolExtId != null) {
                stmt.setString(index++, this.rolExtId);
            }
            for(int i=1; i<this.targetEntIds.length; i++) {
                stmt.setLong(index++,this.targetEntIds[i]);
            }
            stmt.setLong(index++, this.targetEntIds.length);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Long tempL = new Long(rs.getLong("rte_usr_ent_id"));
                if(!v_itm_id.contains(tempL)) {
                    v_itm_id.addElement(tempL);
                }
            }
            stmt.close();
        }
        return v_itm_id;
    }

    /**
    get the user, role pair's directly attached users
    @param con Connection to database
    @param usrEntId user entity id
    @param rolExtId role external id
    @return array of DbRoleTargetEntity which are target users
    */
    public static DbRoleTargetEntity[] getRoleDirectTargetUser(Connection con, long usrEntId, String rolExtId) 
        throws SQLException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_TARGET_USER);
            stmt.setLong(1, usrEntId);
            stmt.setString(2, rolExtId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                DbRoleTargetEntity rolusr = new DbRoleTargetEntity();
                rolusr.rte_usr_ent_id = rs.getLong("rte_usr_ent_id");
                rolusr.rte_rol_ext_id = rs.getString("rte_rol_ext_id");
                rolusr.rte_group_id = rs.getLong("rte_group_id");
                rolusr.rte_ent_id = rs.getLong("rte_ent_id");
                rolusr.rte_create_timestamp = rs.getTimestamp("rte_create_timestamp");
                rolusr.rte_create_usr_id = rs.getString("rte_create_usr_id");
                v.addElement(rolusr);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        //convert the Vector v to array and return
        DbRoleTargetEntity[] rolusrList = new DbRoleTargetEntity[v.size()];
        for(int i=0; i<v.size(); i++) {
            rolusrList[i] = (DbRoleTargetEntity) v.elementAt(i);
        }
        return rolusrList;
    }

    /**
    get the user, role pair's directly attached user groups
    @param con Connection to database
    @param usrEntId user entity id
    @param rolExtId role external id
    @return array of DbRoleTargetEntity which are target user groups
    */
    public static DbRoleTargetEntity[] getRoleDirectTargetUserGroup(Connection con, long usrEntId, String rolExtId) 
        throws SQLException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_TARGET_USER_GROUP);
            stmt.setLong(1, usrEntId);
            stmt.setString(2, rolExtId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                DbRoleTargetEntity rolusg = new DbRoleTargetEntity();
                rolusg.rte_usr_ent_id = rs.getLong("rte_usr_ent_id");
                rolusg.rte_rol_ext_id = rs.getString("rte_rol_ext_id");
                rolusg.rte_group_id = rs.getLong("rte_group_id");
                rolusg.rte_ent_id = rs.getLong("rte_ent_id");
                rolusg.rte_create_timestamp = rs.getTimestamp("rte_create_timestamp");
                rolusg.rte_create_usr_id = rs.getString("rte_create_usr_id");
                v.addElement(rolusg);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        //convert the Vector v to array and return
        DbRoleTargetEntity[] rolusgList = new DbRoleTargetEntity[v.size()];
        for(int i=0; i<v.size(); i++) {
            rolusgList[i] = (DbRoleTargetEntity) v.elementAt(i);
        }
        return rolusgList;
    }

/*
    private static final String sql_get_usr_without_target_group_by_role =     
        "SELECT erl_ent_id, usr_ste_usr_id FROM acEntityRole, acRole, RegUser " + 
        "WHERE rol_ext_id = ? " + 
        "AND erl_ent_id NOT IN " +
        "(SELECT rte_usr_ent_id FROM usrRoleTargetEntity WHERE rte_rol_ext_id = ?) " + 
        "AND rol_id = erl_rol_id " + 
        "AND usr_ent_id = erl_ent_id " +
        "AND (rte_eff_start_datetime is null OR rte_eff_start_datetime <= ? ) " +
        "AND (rte_eff_end_datetime is null OR rte_eff_end_datetime >= ? ) ";
*/
    /**
    get user list with specific role without active target group
    used to find user with approver role but no active approval group
    @return Hashtable of user ent id as key, usr_ste_usr_id as value
    */
    /*
    public static Hashtable checkWithNoTargetGroupByRole(Connection con, String rol_ext_id) throws SQLException{
        Hashtable htUsr = new Hashtable();
        PreparedStatement stmt = null;
        try{
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(sql_get_usr_without_target_group_by_role);
            int index = 1;
            stmt.setString(index++, rol_ext_id);
            stmt.setString(index++, rol_ext_id);
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);

            ResultSet rs = stmt.executeQuery();   
            while (rs.next()){
                htUsr.put(new Long(rs.getLong("erl_ent_id")), rs.getString("usr_ste_usr_id"));
            }
        }finally{
            if (stmt!=null)     stmt.close();
        }
        return htUsr;
    }
    */
}