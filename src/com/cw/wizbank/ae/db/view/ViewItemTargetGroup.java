package com.cw.wizbank.ae.db.view;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.db.DbCmSkillBase;

/**
Business Layer for Targeted Groups.
Target Groups are a set of (industry code, user group, user grade)
*/
public class ViewItemTargetGroup{

    public long itemId;

    public long targetGroupId;

    public String targetType;

    public long[] targetEntIds;
    
    public String applyMethod;

    public String[] targetEntNames;

    public String[] targetEntTypes;

    private Vector v_targetEntIds;

    private Vector v_targetEntNames;

    private Vector v_targetEntTypes;


    public static final String TARGETED_PREVIEW = DbItemTargetRuleDetail.ITE_TYPE_TARGETED_PREVIEW;
    // added for cost center
    public static final String COST_CENTER = "COST_CENTER";
    public static final String SQL_PARAM_CUR_TIME = "CUR_TIME";

    /*
    public static String getTargetGroupsLrnNumAsXML(Connection con, ViewItemTargetGroup[] viTgps) throws SQLException {

        Vector v_targetLrn = getTargetGroupsLrn(con, viTgps);
        return targetLrnNumAsXML(v_targetLrn.size());
    }

    public static String getTargetGroupsLrnNumAsXML(Connection con, long itemId) throws SQLException {

        Vector v_targetLrn = getTargetGroupsLrn(con, itemId);
        return targetLrnNumAsXML(v_targetLrn.size());
    }
    */

    public static String targetLrnNumAsXML(long num) {

        StringBuffer xmlBuf = new StringBuffer(128);
        xmlBuf.append("<targeted_lrn_num>");
        xmlBuf.append("<num value=\"").append(num).append("\" />");
        xmlBuf.append("</targeted_lrn_num>");
        return xmlBuf.toString();
    }


    public static String getTargetGroupsLrnSQL(Connection con, ViewItemTargetGroup[] viTgps, Vector v_param_column) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(1024);
        if (v_param_column==null){
            v_param_column = new Vector();
        }
        //for each target group, find out its targeted learners
        for(int i=0; i<viTgps.length; i++) {
            if(i != 0) {
                SQLBuf.append(" Union ");
            }
            SQLBuf.append(viTgps[i].getTargetGroupLrnSQL(v_param_column));
        }
        return SQLBuf.toString();
    }

    /**
    get target learner ent_id of the input target groups
    */
    public static Vector getTargetGroupsLrn(Connection con, ViewItemTargetGroup[] viTgps) throws SQLException {

        Vector v_param_column = new Vector();
        Vector v_ent_id = new Vector(); //usr_ent_id of targeted learner
        String SQL = getTargetGroupsLrnSQL(con, viTgps, v_param_column);
        /*
        Vector v_list = new Vector();   //usr_ent_id of users in each target group
        */

        //for each target group, find out its targeted learners
        /*
        for(int i=0; i<viTgps.length; i++) {
            Vector v_temp = viTgps[i].getTargetGroupLrn(con);
            v_temp.addElement(new Long(0));
            v_list.addElement(cwUtils.vector2list(v_temp));
        }
        */
//System.out.println("SQL = " + SQLBuf.toString());
        //union all the users in each target group
        /*if(v_list.size() > 0) {
            StringBuffer SQLBuf = new StringBuffer(1024);
            SQLBuf.append("Select usr_ent_id From RegUser ");
            for(int i=0; i<v_list.size(); i++) {
                if(i == 0) {
                    SQLBuf.append(" Where usr_ent_id in ").append((String)v_list.elementAt(i));
                }
                else {
                    SQLBuf.append(" Or usr_ent_id in ").append((String)v_list.elementAt(i));
                }
            }
            */
        if(SQL != null && SQL.length() > 0) {
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v_ent_id.addElement(new Long(rs.getLong("usr_ent_id")));
            }
            rs.close();
            stmt.close();
        }
        /*}*/
        return v_ent_id;
    }

    /**
    get target learner ent_id of an item
    */
    public static Vector getTargetGroupsLrn(Connection con, long itemId, String type, String applyMethod) throws SQLException {

        ViewItemTargetGroup[] viTgps = getTargetGroups(con, itemId, false, type);
        return getTargetGroupsLrn(con, viTgps);
    }
    
    /**
    get target learner ent_id of an item
    */
    public static Vector getTargetGroupsLrn(Connection con, long itemId, String type) throws SQLException {

        ViewItemTargetGroup[] viTgps = getTargetGroups(con, itemId, false, type);
        return getTargetGroupsLrn(con, viTgps);
    }

    /**
    get target learner ent_id of an item
    */
    public static String getTargetGroupsLrnSQL(Connection con, long itemId, String type, Vector v_param_column) throws SQLException {

        ViewItemTargetGroup[] viTgps = getTargetGroups(con, itemId, false, type);
        return getTargetGroupsLrnSQL(con, viTgps, v_param_column);
    }

    /**
    Get a SQL that will find out usr_ent_id of this target group
    */
    private String getTargetGroupLrnSQL(Vector v_param_column) {
        /*
        Vector v_list = new Vector();
        Vector v_ent_id = new Vector();
        */
        Vector v_subSQL = new Vector();
        String ern_type=null;
        long ancestor;
        for(int i=0; i<this.targetEntIds.length; i++) {
            StringBuffer subSQLBuf = new StringBuffer(512);
            ancestor = this.targetEntIds[i];
            if(this.targetEntTypes[i].equals(dbEntity.ENT_TYPE_INDUSTRY_CODE)) {
            	ern_type = dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC;
            }
            else if(this.targetEntTypes[i].equals(dbEntity.ENT_TYPE_USER_GRADE)) {
            	ern_type = dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR;
            }
            else if(this.targetEntTypes[i].equals(dbEntity.ENT_TYPE_USER_GROUP)) {
            	ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
            }
            subSQLBuf.append(" (Select distinct ern_child_ent_id From EntityRelation ")
                  .append(" Where ern_type = '").append(ern_type).append("' ")
                  .append(" And ern_ancestor_ent_id = ").append(ancestor).append(")");
                  
            /*
            //if one of the target entity contains no user, no need to processed
            if(v_EntMemberUser.size() > 0) {
                v_list.addElement(cwUtils.vector2list(v_EntMemberUser));
            }
            else {
                return v_ent_id;
            }
            */
            v_subSQL.addElement(subSQLBuf.toString());
            v_param_column.addElement(SQL_PARAM_CUR_TIME);
        }

        //intersect all users in each target entity
        StringBuffer SQLBuf = new StringBuffer(1024);
        SQLBuf.append(" Select usr_ent_id From RegUser ");
        SQLBuf.append(" WHERE usr_status = '").append(dbRegUser.USR_STATUS_OK).append("' ");
        /*
        SQLBuf.append(" WHERE usr_status = ? ");
        for(int i=0; i<v_list.size(); i++) {
            SQLBuf.append(" AND usr_ent_id in ").append((String)v_list.elementAt(i));
        }
        */
        for(int i=0; i<v_subSQL.size(); i++) {
            SQLBuf.append(" AND usr_ent_id in ").append((String)v_subSQL.elementAt(i));
        }
        /*
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setString(1, dbRegUser.USR_STATUS_OK);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v_ent_id.addElement(new Long(rs.getLong("usr_ent_id")));
        }
        stmt.close();
        return v_ent_id;
        */
        //System.out.println("subSQL = " + SQLBuf.toString());
        return SQLBuf.toString();
    }

    /**
    get the target groups (usergroup, industry code, usergrade) of an item as XML
    */
    public static String getTargetGroupsAsXML(Connection con, long itemId) throws SQLException {

        ViewItemTargetGroup[] viTgps = getTargetGroups(con, itemId, true, null);
        return targetGroupsAsXML(viTgps);
    }
    
    /**
    get the target groups (usergroup, industry code, usergrade) of an item as XML
    */
    public static String getTargetGroupsAsXML(Connection con, long itemId, String applyMethod) throws SQLException {

        ViewItemTargetGroup[] viTgps = getTargetGroups(con, itemId, true, null);
        return targetGroupsAsXML(viTgps);
    }

    /**
    get the target groups (usergroup, industry code, usergrade) of an item as XML
    */
    public static String targetGroupsAsXML(ViewItemTargetGroup[] viTgps) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<targeted_lrn>");
        xmlBuf.append(GroupListAsXML(viTgps));
        xmlBuf.append("</targeted_lrn>");
        return xmlBuf.toString();
    }

    
    public static String compTargetGroupsAsXML(ViewItemTargetGroup[] viTgps) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<comp_targeted_lrn>");
        xmlBuf.append(GroupListAsXML(viTgps));
        xmlBuf.append("</comp_targeted_lrn>");
        return xmlBuf.toString();
    }

    /**
    get the target groups (usergroup, industry code, usergrade) of an item
    @param getDisplayBil get ent_display_bil into targetEntNames[] if true
    */
    public static ViewItemTargetGroup[] getTargetGroups(Connection con, long itemId, boolean getDisplayBil, String type) throws SQLException {
        ViewItemTargetGroup[] viTgps;
		aeItem itm = new aeItem();
		itm.itm_id = itemId;
		itm.getRunInd(con);
		
		long ird_itm_id = 0;
		if(itm.itm_run_ind) {
			itm.itm_target_enrol_type = aeItem.getTargetEnolType(con, itemId);
			if(itm.itm_target_enrol_type == null) {
				viTgps = new ViewItemTargetGroup[0];
				return viTgps;
			} else if(itm.itm_target_enrol_type.equalsIgnoreCase(aeItem.ITM_TARGET_ENROLL_TYPE_ASSIGNED)){
				ird_itm_id = itemId;
			} else if(itm.itm_target_enrol_type.equalsIgnoreCase(aeItem.ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER)) {
				aeItemRelation ire = new aeItemRelation();
				ire.ire_child_itm_id = itm.itm_id;
				ire.getParentItemId(con);
				ird_itm_id= ire.ire_parent_itm_id;
				type = DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER;
			}
		} else {
			ird_itm_id = itemId;
		} 
    	
        String sql_get_item_target_groups =
            " SELECT ird_itm_id, ird_group_id, usg.ent_type usg_type, ird_grade_id, ugr.ent_type ugr_type, ird_type, " +
            " ird_upt_id, upt_title, 'SKILL' skill_type" + 
            " From Entity usg, Entity ugr, aeItemTargetRuleDetail " +
            " left join UserPosition on (upt_id = ird_upt_id)" +
     
            " WHERE usg.ent_id = ird_group_id and ugr.ent_id = ird_grade_id " +
            " AND ird_itm_id = ? ";

        if (type == null) {
            sql_get_item_target_groups += " AND (ird_type = ? OR ird_type = ? )";
        } else {
            sql_get_item_target_groups += " AND ird_type = ? ";
        }
        Vector v = new Vector();
        ViewItemTargetGroup viTgp=null;
    
        PreparedStatement stmt = con.prepareStatement(sql_get_item_target_groups);

        int index = 1;
        //stmt.setString(index++, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL);
       // stmt.setString(index++, DbCmSkillBase.COMPETENCY_GROUP);
        stmt.setLong(index++, ird_itm_id);
        if (type == null) {
            stmt.setString(index++, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
            stmt.setString(index++, DbItemTargetRuleDetail.IRD_TYPE_TARGET_ENROLLMENT);
        } else {
        	stmt.setString(index++, type);
        }
        long groupId;
        long gradeId;
        String targetType;
        String usg_type;
        String ugr_type;
        String skill_type;
        
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            groupId = rs.getLong("ird_group_id");
            gradeId = rs.getLong("ird_grade_id");
            targetType = rs.getString("ird_type");
            usg_type =rs.getString("usg_type");
            ugr_type =rs.getString("ugr_type");
            skill_type =rs.getString("skill_type");

            viTgp = new ViewItemTargetGroup();
            viTgp.targetType = targetType;
            viTgp.v_targetEntIds = new Vector();
            viTgp.v_targetEntTypes = new Vector();
            viTgp.v_targetEntNames = new Vector();
            
            viTgp.v_targetEntTypes.addElement(usg_type);
            viTgp.v_targetEntIds.addElement(new Long(groupId));
            
            viTgp.v_targetEntTypes.addElement(ugr_type);
            viTgp.v_targetEntIds.addElement(new Long(gradeId));
            
            viTgp.v_targetEntTypes.addElement(rs.getString("skill_type"));
            viTgp.v_targetEntIds.addElement(new Long(rs.getLong("ird_upt_id")));
            
            if(getDisplayBil) {
            	String fullPath = "";
            	if(usg_type != null&& usg_type.equals(dbEntity.ENT_TYPE_USER_GROUP) ) {
            		fullPath = getEntityFullPath(con, groupId, usg_type);
            		if(fullPath == null || fullPath.length()==0){
            			fullPath = getEntityName(con, groupId, usg_type);
            		}
            	}
            	viTgp.v_targetEntNames.addElement(fullPath);
            	fullPath = "";
            	if(ugr_type != null && ugr_type.equals(dbEntity.ENT_TYPE_USER_GRADE)) {
            		fullPath = getEntityName(con, gradeId, ugr_type);
            	}
            	viTgp.v_targetEntNames.addElement(fullPath);
            	
            	fullPath =rs.getString("upt_title");
           
            	viTgp.v_targetEntNames.addElement(fullPath);
            } else {
                viTgp.v_targetEntNames.addElement("");
                viTgp.v_targetEntNames.addElement("");
                viTgp.v_targetEntNames.addElement("");
            }
            viTgp.convertEntityIds();
            viTgp.convertEntityTypes();
            viTgp.convertEntityNames();
            v.addElement(viTgp);
        }

        //convert the Vector v to array and return
        viTgps = new ViewItemTargetGroup[v.size()];
        for(int i=0; i<v.size(); i++) {
            viTgps[i] = (ViewItemTargetGroup) v.elementAt(i);
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
            stmt.close();
        }
        return ent_name;
    }

    private static String getEntityFullPath(Connection con, long ent_id, String ent_type) throws SQLException {
        String ent_name;
        String type = null;
        if (ent_type == null) {
			ent_name = null;
		} else {
            if(ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
            }
            else if(ent_type.equals(dbEntity.ENT_TYPE_USER_GROUP)) {
                type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
            }
            else if(ent_type.equals(dbEntity.ENT_TYPE_INDUSTRY_CODE)) {
                type =dbEntityRelation.ERN_TYPE_IDC_PARENT_IDC;
            }
            else if(ent_type.equals(dbEntity.ENT_TYPE_USER_GRADE)) {
                type =dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
            }
			ent_name = dbEntityRelation.getFullPath(con, ent_id, type);
		}
        return ent_name;
    }

    /**
    Search Item which targeted entity matches with any combination of ancesters
    of v_v_Entity, which is a Vector of 3 Vectors (Group, Grade, Industry), and itself.
    @param h_v_Entity, Hashtable of 3 Vectors of ent_id in order of v_Group, v_Grade, v_Industry
                       e.g. if v_Grade is null or empty, will not consider grade in searching
                       keys of Hashtable should be
                       (dbEntity.ENT_TYPE_USER_GROUP, dbEntity.ENT_TYPE_USER_GRADE, dbEntity.ENT_TYPE_INDUSTRY_CODE)
    @param array_itm_apply_method, itm_apply_method of item to be search, if given
    @param targetType, TARGETED_LEARNER or TARGETED_ENROLMENT
    @param v_itm_id, searched out item id will be added to this Vector

    */
    public static void searchItem(Connection con, Hashtable h_v_Entity,
                                  String[] array_itm_apply_method,
                                  String targetType, Vector v_itm_id)
                                  throws SQLException {
        // prepare the result Vector if not given
        if(v_itm_id == null) {
            v_itm_id = new Vector();
        }
        //get Vectors of Entity from Hashtable
        if(h_v_Entity != null) {
            Vector v_Group = (Vector) h_v_Entity.get(dbEntity.ENT_TYPE_USER_GROUP);
            Vector v_Grade = (Vector) h_v_Entity.get(dbEntity.ENT_TYPE_USER_GRADE);
            String groupAncester = null;
            String gradeAncester = null;
            
            boolean hasGroupAncester = false;
            boolean hasGradeAncester = false;
            
            //get ancesters of group(the group's ancestor is the same)
            if(v_Group != null && v_Group.size() > 0) {
            	hasGroupAncester = true;
                groupAncester = "(" + dbEntityRelation.getAncestorListSql(((Long) v_Group.elementAt(0)).longValue(), dbEntityRelation.ERN_TYPE_USG_PARENT_USG) + ")";
            }
            //get ancesters of grade(the grade's ancestor is the same)
            if(v_Grade != null && v_Grade.size() > 0) {
            	hasGradeAncester = true;
                gradeAncester = "(" + dbEntityRelation.getAncestorListSql(((Long) v_Grade.elementAt(0)).longValue(), dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR) + ")";

            }
            //make the SQL
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(" SELECT distinct itm_id FROM aeItemTargetRuleDetail, aeItem ");
            SQLBuf.append(" WHERE itm_id = ird_itm_id ");
            SQLBuf.append(" and itm_deprecated_ind = ? ");
            SQLBuf.append(" and ird_type = ? ");
            SQLBuf.append(" and itm_access_type = ? ");
            if(hasGroupAncester){
            	SQLBuf.append(" and ( ird_group_id in ").append(groupAncester);
            	for(int i=0; i<v_Group.size(); i++) {
            		SQLBuf.append(" or ird_group_id = ").append(((Long) v_Group.elementAt(i)).longValue());
                }
            	SQLBuf.append(" )");
            }
            if(hasGradeAncester){
            	SQLBuf.append(" and (ird_grade_id in ").append(gradeAncester);
            	for(int i=0; i<v_Grade.size(); i++) {
            		SQLBuf.append(" or ird_grade_id = ").append(((Long) v_Grade.elementAt(i)).longValue());
                }
            	SQLBuf.append(" )");
            }
            else {
            	SQLBuf.append(" AND ird_group_id in (0) and ird_grade_id in (0)");
            }
            if (array_itm_apply_method != null && array_itm_apply_method.length > 0) {
                SQLBuf.append(" and itm_apply_method in ").append(cwUtils.array2list(array_itm_apply_method));
            }
            //execute and add into v_itm_id
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setBoolean(index++, false);
            stmt.setString(index++, targetType);
            stmt.setString(index++, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Long tempL = new Long(rs.getLong("itm_id"));
                if(!v_itm_id.contains(tempL)) {
                    v_itm_id.addElement(tempL);
                }
            }
            stmt.close();
        }
        return;
    }

    /**
    Search items hits the searching criteria -- groups of the (IDC, USR, UGR)<BR>
    groups is delimited by "," e.g. {"1,3,2","5,7,74"}
    */
    public static void searchItem(Connection con, String[] groups, Vector v_itm_id, String targetType)
        throws SQLException {

        if(groups != null) {
            if(v_itm_id == null) {
                v_itm_id = new Vector();
            }
            long[] ent_ids;
            ViewItemTargetGroup viTgp = new ViewItemTargetGroup();
            viTgp.targetType = targetType;
            for(int i=0; i<groups.length; i++) {
                viTgp.targetEntIds = cwUtils.splitToLong(groups[i], ",");
                viTgp.searchItem(con, v_itm_id);
            }
        }
        return;
    }

    /**
    Search items hits the searching criteria -- groups of the (IDC, USR, UGR)<BR>
    Will search for ancesters of each enity in group, And itm_deprecated_ind = false only<BR>
    Pre-define variables:
    <ul>
    <li>targetEntId
    <li>targetType
    <li>itemId (if itemId != 0, search if the itemId matches with the targetEntId and targetEntType.)
    </ul>
    */
    public void searchItem(Connection con, Vector v_itm_id) throws SQLException {
        searchItem(con, v_itm_id, null);
    }

    /**
    Search items hits the searching criteria -- groups of the (IDC, USR, UGR)<BR>
    Will search for ancesters of each enity in group, And itm_deprecated_ind = false only<BR>
    Pre-define variables:
    <ul>
    <li>targetEntId
    <li>targetType
    <li>itemId (if itemId != 0, search if the itemId matches with the targetEntId and targetEntType.)
    </ul>
    */
    public void searchItem(Connection con, Vector v_itm_id, String[] array_itm_apply_method) throws SQLException {
        if(this.targetEntIds != null) {
            try {
                if(v_itm_id == null) {
                    v_itm_id = new Vector();
                }
                //get ent_type of each targetEntIds and their ancester
                dbEntity dbEnt = new dbEntity();
                Hashtable h_v_entity  = new Hashtable();
                for(int i=0; i<this.targetEntIds.length; i++) {
                    dbEnt.ent_id = this.targetEntIds[i];
                    dbEnt.get(con);
                    Vector v = new Vector();
                    v.add(new Long(dbEnt.ent_id));
                    h_v_entity.put(dbEnt.ent_type, v);
                }
                searchItem(con, h_v_entity, null, this.targetType, v_itm_id);
            }
            catch(qdbException e) {
                throw new SQLException(e.getMessage());
            }
        }
        return;
    }

    /**
     * get the cost center groups (usergroup) of an item as XML
     * @param con
     * @param itemId
     * @return
     * @throws SQLException
     */
    public static String getCostCenterGroupsAsXML(Connection con, long itemId) throws SQLException {

        ViewItemTargetGroup[] viTgps = getTargetGroups(con, itemId, true, COST_CENTER);
        return targetGroupsAsXML(viTgps);
    }

    /**
     * get the target groups (usergroup, industry code, usergrade) of an item as XML
     */
    public static String GroupListAsXML(ViewItemTargetGroup[] viTgps) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<target_list>");
        for(int i=0; i<viTgps.length; i++) {
            xmlBuf.append("<target>");
            for(int j=0; j<viTgps[i].targetEntIds.length; j++) {
                xmlBuf.append("<entity id=\"").append(viTgps[i].targetEntIds[j]).append("\"");
                xmlBuf.append(" type=\"").append(viTgps[i].targetEntTypes[j]).append("\"");
                xmlBuf.append(" display_bil=\"").append(cwUtils.esc4XML(viTgps[i].targetEntNames[j])).append("\"/>");
            }
            xmlBuf.append("</target>");
        }
        xmlBuf.append("</target_list>");
        return xmlBuf.toString();
    }
}