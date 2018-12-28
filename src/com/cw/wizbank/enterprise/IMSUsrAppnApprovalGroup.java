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
import com.cw.wizbank.util.*;

public class IMSUsrAppnApprovalGroup
{
    // reset the usr_appn_approval_group when user attach to this group
    public static final String GROUP_SOURCEDID_DUMMY_RESET_APPN_APPROVAL_GROUP = "DUMMY_RESET_APPN_APPROVAL_GROUP";
    private static final long WB_ENT_ID_DUMMY_RESET = java.lang.Long.MAX_VALUE;
    
    private MembershipType _membership;
    private MemberType member;
    private RoleType role;
    private String sourceDIDID;
    private String memberSourceDIDID;
     /**
    Value of app approval usergroup ent id
    */
    private long wbEntId;
    /**
    Value of ent id of member
    */
    private long wbMemberEntId;

    /**
    Default Constructor
    */
    public IMSUsrAppnApprovalGroup() throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _membership = objFactory.createMembershipType();
    }
    
    public MembershipType getMembership(){
        return _membership;
    }
    /**
    Construct a new IMSGroupMember that contains Data currently contained in the membership argument.
    And stores member and role as its properties.
    @param membership a Membership object with data
    @param member a Member object with data
    @param role a Role object with data
    */
    public IMSUsrAppnApprovalGroup(MembershipType membership, MemberType member, RoleType role){
        this._membership = membership;
//        setComments(membership.getComments());
//        setSourcedid(membership.getSourcedid());
        this.member = member;
        this.role = role;
        getSourceDIDID();
        getMemberSourceDIDID();
    }
    
    /**
    save groupmeber to db
    */
    public int save(Connection con, long siteId, loginProfile wbProfile) throws cwException, qdbException, SQLException {
        int code;
        init(con, siteId);
        if (wbEntId<=0){
            throw new cwException("PARENT " + sourceDIDID + " WAS NOT FOUND WHILE IMPORTING RELATIONSHIP.");
        }
        if (wbMemberEntId<=0){
            throw new cwException("MEMBER " + memberSourceDIDID + " WAS NOT FOUND WHILE IMPORTING RELATIONSHIP.");
        }
        
        dbRegUser usr = new dbRegUser();
        usr.usr_ent_id = wbMemberEntId;
        usr.getAppApprovalUsgEntId(con);
        long old_app_approval_usg_ent_id = usr.usr_app_approval_usg_ent_id;
        if (wbEntId == WB_ENT_ID_DUMMY_RESET){
            usr.usr_app_approval_usg_ent_id = 0;
        }else{
            usr.usr_app_approval_usg_ent_id = wbEntId;
        }
        usr.updAppApprovalUsgEntId(con);
        if (usr.usr_app_approval_usg_ent_id ==0){
            code = IMSUtils.SAVE_CODE_RESET;            
        }else{
            if (old_app_approval_usg_ent_id == 0)
                code = IMSUtils.SAVE_CODE_INSERT;            
            else
                code = IMSUtils.SAVE_CODE_UPDATE;            
        }
        return code;
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
    initialise Usergroup ent id, member ent id, type
    */
    private void init(Connection con, long siteId) throws qdbException{
        if (sourceDIDID.equals(GROUP_SOURCEDID_DUMMY_RESET_APPN_APPROVAL_GROUP)){
            this.wbEntId = WB_ENT_ID_DUMMY_RESET;                    
        }else{
            dbUserGroup dbUsrGrp = new dbUserGroup();
            dbUsrGrp.ent_ste_uid        = sourceDIDID;
            dbUsrGrp.usg_ent_id_root    = siteId;
            dbUsrGrp.getEntIdBySteUid(con);
            this.wbEntId = dbUsrGrp.usg_ent_id;
        }
        
        dbRegUser memusr = new dbRegUser();
        long temp_ent_id = memusr.getEntIdStatusOk(con, memberSourceDIDID, siteId);
        if (temp_ent_id <= 0) {
        	temp_ent_id = memusr.getEntId(con, memberSourceDIDID, siteId);
        }
        this.wbMemberEntId = temp_ent_id;
    }

    public void setAppApprovalGroupMembership(Connection con, Vector vtUsrEntId, Hashtable htMembership) throws JAXBException, SQLException{
        if (vtUsrEntId.size()==0){
            return;
        }
        Hashtable htUserAppApprovalGroup = dbRegUser.getUserAppApprovalGroup(con, vtUsrEntId); 
        Enumeration e_user = htUserAppApprovalGroup.keys();
        while(e_user.hasMoreElements()) {
            String usr_ste_usr_id = (String) e_user.nextElement();
            String group_code = (String)htUserAppApprovalGroup.get(usr_ste_usr_id);
            if (group_code == ""){
                group_code = GROUP_SOURCEDID_DUMMY_RESET_APPN_APPROVAL_GROUP;
            }
            MembershipType membership = (MembershipType)htMembership.get(group_code);
            if (membership==null){
                ObjectFactory objFactory = new ObjectFactory();
                membership = objFactory.createMembershipType();
                SourcedidType sourcedid = IMSUtils.createSourcedid(group_code);
                membership.setSourcedid(sourcedid);
            }
            List l_member = membership.getMember();
            // user as a approval member in the group_supervisors_group
            member = IMSUtils.createMemberObject(usr_ste_usr_id, "04", IMSGroupMember.ROLE_SUBTYPE_APPROVAL_MEMBER, "1");
            l_member.add(member);
            htMembership.put(group_code, membership);
        }
    }

}

