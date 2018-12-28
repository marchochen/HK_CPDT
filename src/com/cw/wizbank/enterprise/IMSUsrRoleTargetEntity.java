package com.cw.wizbank.enterprise;

import java.sql.*;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.xml.bind.*;
import org.imsglobal.enterprise.*;

import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.db.DbRoleTargetEntity;
import com.cw.wizbank.accesscontrol.AccessControlWZB;

public class IMSUsrRoleTargetEntity
{
//    private List _Member;
    private MemberType member;
    private RoleType role;
    public String sourceDIDID;
    private String memberSourceDIDID;

    private static long curSiteId;
    // IMS Role as key, extId as value
    private static Hashtable htRole;
    // IMS Role as key, extId as value
    private static Vector vtHasTargetEntRole;
    
    private MembershipType _membership;
    public static void initRole(Connection con, long siteId) throws SQLException, cwException{
        AccessControlWZB acl = new AccessControlWZB();
        htRole = acl.getAllRoleUid(con, siteId, "rol_ste_uid");
        vtHasTargetEntRole = dbUtils.getRoleByTargetEntInd(con, siteId, true);
        curSiteId = siteId;   
    }
    
    public static Hashtable initRoleType(Connection con, long siteId) throws SQLException, cwException{
        AccessControlWZB acl = new AccessControlWZB();
        Hashtable htRole = acl.getAllRoleUid(con, siteId, "rol_ste_uid");
        Hashtable htRoleType = new Hashtable();

        Enumeration enumeration = htRole.keys();
        while(enumeration.hasMoreElements()) {
            String cur_role = (String)enumeration.nextElement();
            if (cur_role.equals("LRNR")){
                htRoleType.put(cur_role, "01");
            }else if (cur_role.equals("TADM")){
                htRoleType.put(cur_role, "07");
            }else if (cur_role.equals("SADM")){
                htRoleType.put(cur_role, "07");
            }else if (cur_role.equals("GADM")){
                htRoleType.put(cur_role, "07");
            }else{
                htRoleType.put(cur_role, "07");
            }
        }
        return htRoleType;
    }

    /**
    Value of target ent id
    */
    private long wbEntId;
    /**
    Value of approver ent id
    */
    private long wbMemberEntId;

    /**
    initialise ROLE_MANAGER for further use.
    @param role_ext_id of manager
    */
    /**
    default constructor
    */
    public IMSUsrRoleTargetEntity() throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _membership = objFactory.createMembership();
    }
    // override getMember method
//    public List getMember() {
//        return this._Member;
//    }

    public String parentUsgType = null;

    /**
    Construct a new IMSUsrRoleTargetEntity that contains Data currently contained in the membership argument.
    And stores member and role as its properties.
    @param membership a Membership object with data
    @param member a Member object with data
    @param role a Role object with data
    */
    public IMSUsrRoleTargetEntity(MembershipType membership, MemberType member, RoleType role, String parentUsgType){
        _membership = membership;
        this.parentUsgType = parentUsgType;
//        setComments(membership.getComments());
//        setSourcedid(membership.getSourcedid());
        this.member = member;
        this.role = role;
        getSourceDIDID();
    }
/*    public IMSUsrRoleTargetEntity(Membership membership, Member member, Role role, String parentUsgType, String targetUserSourceDIDID){
        this.sourceDIDID = targetUserSourceDIDID;
        new IMSUsrRoleTargetEntity(membership, member, role, parentUsgType);
    }
    */
    
/*
    public IMSUsrRoleTargetEntity(Membership membership, String parentUsgType){
        super();
        this.parentUsgType = parentUsgType;
        setComments(membership.getComments());
        setSourcedid(membership.getSourcedid());
        _Member = membership.getMember();
        getSourceDIDID();
    }
*/    
    /**
    save data from the IMSUsrRoleTargetEntity to db
    */
    public int save(Connection con, loginProfile wbProfile) throws cwException, qdbException, SQLException{
        init(con, wbProfile.root_ent_id);
        int save_code;
        if (wbEntId<=0){
            throw new cwException("PARENT " + sourceDIDID + " WAS NOT FOUND WHILE IMPORTING RELATIONSHIP.");
        }else if (wbMemberEntId<=0){
            throw new cwException("MEMBER " + memberSourceDIDID + " WAS NOT FOUND WHILE IMPORTING RELATIONSHIP.");
        }else{
//            DbEntity ent = new DbEntity();
//            ent.ent_id = wbMemberEntId;
//            ent.get(con); 
            if (curSiteId!=wbProfile.root_ent_id){
                initRole(con, wbProfile.root_ent_id); 
            }
            int role_save_code;
            int target_group_save_code;
            dbRegUser usr = new dbRegUser();
            usr.usr_ent_id = wbMemberEntId;
            usr.getUsrSynInfo(con);
            if (usr.usr_syn_rol_ind){            
                String rolExtId;
                String subRole = role.getSubrole();
                if (subRole!=null)  
                    rolExtId = (String)htRole.get(subRole);
                else
                    rolExtId = (String)htRole.get(role.getRoletype());

                if (rolExtId==null){
                    throw new cwException("UNKNOWN ROLE IN WIZBANK: " + subRole + ", " + role.getRoletype() + " WHILE IMPORTING RELATIONSHIP.");
                }else{
                    Timestamp startDate = null;
                    Timestamp endDate = null;
                    String sTemp;
                    if (role.getTimeframe()!=null){
                        if(role.getTimeframe().getBegin() != null) {
                            //set startDate
                            sTemp = role.getTimeframe().getBegin().getValue();
                            if (sTemp != null && sTemp.length() > 0){
                                startDate = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sTemp));
                            }
                        }
                        if (role.getTimeframe().getEnd() != null){
                            sTemp = role.getTimeframe().getEnd().getValue();
                            if (sTemp != null && sTemp.length() > 0){
                                endDate = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sTemp));
                            }
                        }
                    }
                    // init start end date
                    if (startDate==null){
                        startDate = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                    }
                    if (endDate==null){
                        endDate = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
                    }
                    AccessControlWZB acl = new AccessControlWZB();
                    if(!acl.hasUserRole(con, wbMemberEntId, rolExtId, startDate, endDate)) {
                        acl.assignUser2Role(con, wbMemberEntId, rolExtId, startDate, endDate);
                        role_save_code = IMSUtils.SAVE_CODE_INSERT;
                    }else{
                        role_save_code = IMSUtils.SAVE_CODE_UPDATE;
                    }
                    acl.updEntityRoleSynTimestamp(con, wbMemberEntId, rolExtId, startDate, endDate);
                    
                    if (vtHasTargetEntRole.contains(rolExtId)){
                        boolean bExist;
                        DbRoleTargetEntity dbRte = new DbRoleTargetEntity();
                        dbRte.rte_usr_ent_id = wbMemberEntId;
                        dbRte.rte_rol_ext_id = rolExtId;
                        dbRte.rte_ent_id = wbEntId;
                        dbRte.rte_eff_start_datetime = startDate;
                        dbRte.rte_eff_end_datetime = endDate;
                        bExist = dbRte.checkExist(con);
                        
                        Timestamp curTime = cwSQL.getTime(con);
                        if (!bExist){
                            ViewRoleTargetGroup vrte = new ViewRoleTargetGroup();
                            vrte.usrEntId = wbMemberEntId;
                            vrte.rolExtId = rolExtId;
                            vrte.targetEntIds = new long[] {wbEntId};
                            vrte.targetEffStart = dbRte.rte_eff_start_datetime;
                            vrte.targetEffEnd = dbRte.rte_eff_end_datetime;
                            vrte.insTargetGroup(con, wbProfile.usr_id, curTime); 
                            target_group_save_code = IMSUtils.SAVE_CODE_INSERT;                     
                        }else{
                            target_group_save_code = IMSUtils.SAVE_CODE_UPDATE;                     
                        }
                        dbRte.updSynTimestamp(con); 
                    }else{
                        target_group_save_code = IMSUtils.SAVE_CODE_UPDATE;                     
                    }
                }
                if (role_save_code == IMSUtils.SAVE_CODE_INSERT || target_group_save_code == IMSUtils.SAVE_CODE_INSERT ){
                    save_code = IMSUtils.SAVE_CODE_INSERT;
                }else{
                    save_code = IMSUtils.SAVE_CODE_UPDATE;
                }
            }else{
                save_code = IMSUtils.SAVE_CODE_NOT_SAVE;                               
            }
        }     
        return save_code;
    }
    /** 
    get memberSourceDIDID
    set memberSourceDIDID if memberSourceDIDID not yet set
    @return memberSourceDIDID
    */    
    public String getMemberSourceDIDID(){
        if (this.memberSourceDIDID==null){
            this.memberSourceDIDID = member.getSourcedid().getId();
        }
        return this.memberSourceDIDID; 
    }
    /** 
    get sourceDIDID
    set sourceDIDID if sourceDIDID not yet set
    @return sourceDIDID
    */    
    public String getSourceDIDID() {
        if (this.sourceDIDID==null){
            this.sourceDIDID = _membership.getSourcedid().getId();
        }
        return this.sourceDIDID;
    }
    
    /**
    initialise the Usergroup ent id and approver ent id 
    */
    private void init(Connection con, long siteId) throws qdbException, cwException{
        if (parentUsgType==null){
            throw new cwException("INVALID USG_TYPE:" + parentUsgType);
        }
        if (parentUsgType.equals(IMSUserGroup.USG_TYPE_USERGROUP) || parentUsgType.equals(IMSUserGroup.USG_TYPE_APPROVAL)){
            if (sourceDIDID.equals(IMSUserGroup.ROOT_UID)){
                this.wbEntId = siteId;        
            }else{
                dbUserGroup dbUsrGrp = new dbUserGroup();
                dbUsrGrp.ent_ste_uid        = sourceDIDID;
                dbUsrGrp.usg_ent_id_root    = siteId;
                dbUsrGrp.getEntIdBySteUid(con);
                this.wbEntId = dbUsrGrp.usg_ent_id;
            }        
        }
        if ((parentUsgType.equals(IMSUserGroup.USG_TYPE_APPROVAL) && this.wbEntId == 0) || parentUsgType.equals(IMSUserGroup.USG_TYPE_ENDORSE)){
            dbRegUser dbusr = new dbRegUser();
            long temp_ent_id = dbusr.getEntIdStatusOk(con, sourceDIDID, siteId);
            if (temp_ent_id <= 0) {
            	temp_ent_id = dbusr.getEntId(con, sourceDIDID, siteId);
            }
            this.wbEntId = temp_ent_id;
        }

        dbRegUser dbusr = new dbRegUser();
        long temp_ent_id = dbusr.getEntIdStatusOk(con, member.getSourcedid().getId(), siteId);
        if (temp_ent_id <= 0) {
        	temp_ent_id = dbusr.getEntId(con, memberSourceDIDID, siteId);
        }
        this.wbMemberEntId = temp_ent_id;
    }
    
    public void setRoleMembership(Connection con, long root_ent_id, Vector vtUsrEntId, Hashtable htMembership) throws JAXBException, SQLException , cwException{
        if (vtUsrEntId.size()==0){
            return;
        }
        
        Hashtable htRoleUid = initRoleType(con, root_ent_id);
        Hashtable htUsrRoleUid = dbRegUser.getUserRolesUid(con, vtUsrEntId);
        ObjectFactory objFactory = new ObjectFactory();

        MembershipType rootMembership = (MembershipType)htMembership.get(IMSUserGroup.ROOT_UID);
        // if parent already exist. add the relation only
        if (rootMembership == null){
            rootMembership = objFactory.createMembershipType();
            SourcedidType sourcedid = IMSUtils.createSourcedid(IMSUserGroup.ROOT_UID);
            rootMembership.setSourcedid(sourcedid);
        }
        List l_member = rootMembership.getMember();         
        Enumeration e_user = htUsrRoleUid.keys();
        while(e_user.hasMoreElements()) {
            String usr_ste_usr_id = (String) e_user.nextElement();                       
            MemberType member = objFactory.createMemberType();
            SourcedidType sourcedid = objFactory.createSourcedidType();
            sourcedid.setSource(IMSEnterpriseApp.exportSource);
            sourcedid.setId(usr_ste_usr_id);
            member.setSourcedid(sourcedid);
            member.setIdtype("1");
                                
            List roleList = member.getRole();
            Vector vtUserRole = (Vector)htUsrRoleUid.get(usr_ste_usr_id);
            for (int i=0; i<vtUserRole.size(); i++){
                String rol_uid = (String) vtUserRole.elementAt(i);
                RoleType role = createRoleObject((String)htRoleUid.get(rol_uid), rol_uid);
                roleList.add(role);
            }
            l_member.add(member);
        }
        htMembership.put(IMSUserGroup.ROOT_UID, rootMembership);

    }
    
    public RoleType createRoleObject(String roleId, String subRole) throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        RoleType role = objFactory.createRoleType();
        role.setRoletype(roleId);
        if (subRole!=null){
            role.setSubrole(subRole);
        }
        role.setStatus("1");
        return role;
    }
}
