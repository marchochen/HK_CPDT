package com.cw.wizbank.enterprise;

import java.sql.*;
import java.util.List;
import java.util.Hashtable;

import javax.xml.bind.*;
import org.imsglobal.enterprise.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.db.DbEntity;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.db.DbEntityRelation;
import com.cw.wizbank.db.DbRegUser;

public class IMSGroupMember
{
    public static final String ROLE_TYPE_MEMBER_CODE = "04";
    public static final String ROLE_TYPE_MEMBER = "Member";
    public static final String ROLE_TYPE_MANAGER_CODE = "05";

    public static final String ROLE_SUBTYPE_APPROVAL_MEMBER = "APPN_APPROVAL_MEMBER";
    
    private MembershipType _membership;
    private MemberType member;
    private RoleType role;
    private String sourceDIDID;
    private String memberSourceDIDID;

    /**
    Value of usergroup ent id
    */
    private long wbEntId;
    /**
    Value of ent id of member
    */
    private long wbMemberEntId;
    /**
    Value of EntityRelation type, eg USR_PARENT_USG, USG_PARENT_USG
    */
    private String wbType;

    /**
    Default Constructor
    */
    public IMSGroupMember() throws JAXBException{
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
    public IMSGroupMember(MembershipType membership, MemberType member, RoleType role){
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
            // CHECK IS DELETE WHEN SAVE USER TO GROUP RELATION
        if (wbType.equals(DbEntityRelation.ERN_TYPE_USR_PARENT_USG)){
            DbRegUser usr = new DbRegUser();
            usr.usr_ent_id = wbMemberEntId;
            if (usr.isDeleted(con)){
                throw new cwException("MEMBER " + memberSourceDIDID + " WAS IN DELETED STATE WHILE IMPORTING RELATIONSHIP.");
            }
            usr.getUsrSynInfo(con);
            if (usr.usr_not_syn_gpm_type == null || usr.usr_not_syn_gpm_type.indexOf(DbEntity.ENT_TYPE_USER_GROUP) < 0 ){
                code = saveGroupMember(con, wbProfile);
            }else{
                code = IMSUtils.SAVE_CODE_NOT_SAVE;
            }
        }else{
            code = saveGroupMember(con, wbProfile);
        }
        return code;
    }
    
    private int saveGroupMember(Connection con, loginProfile wbProfile) throws SQLException{
            int code;
            Timestamp cur_time = cwSQL.getTime(con);
            DbEntityRelation dber = new DbEntityRelation();
            dber.ern_ancestor_ent_id = wbEntId;
            dber.ern_child_ent_id = wbMemberEntId;
            dber.ern_type = wbType;
            dber.ern_syn_timestamp = cur_time;
            if(dber.checkExist(con)){
            	dber.updSynDate(con);
                code = IMSUtils.SAVE_CODE_UPDATE;
            }else{
            	//delete original entityrelation
            	dber.delAsChild(con, wbProfile.usr_id, cur_time);
            	dber.insEr(con, wbProfile.usr_id);
                code = IMSUtils.SAVE_CODE_INSERT;
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
        if (sourceDIDID.equals(IMSUserGroup.ROOT_UID)){
            this.wbEntId = siteId;                    
        }else{
            dbUserGroup dbUsrGrp = new dbUserGroup();
            dbUsrGrp.ent_ste_uid        = sourceDIDID;
            dbUsrGrp.usg_ent_id_root    = siteId;
            dbUsrGrp.getEntIdBySteUid(con);
            this.wbEntId = dbUsrGrp.usg_ent_id;
        }
        
        if (member.getIdtype().equals("1")){
            dbRegUser memusr = new dbRegUser();
            long temp_ent_id = memusr.getEntIdStatusOk(con, memberSourceDIDID, siteId);
            if (temp_ent_id <= 0) {
            	temp_ent_id = memusr.getEntId(con, memberSourceDIDID, siteId);
            }
            this.wbMemberEntId = temp_ent_id;
            this.wbType = DbEntityRelation.ERN_TYPE_USR_PARENT_USG;
        }else{
            dbUserGroup memUsrGrp = new dbUserGroup();
            memUsrGrp.ent_ste_uid        = memberSourceDIDID;
            memUsrGrp.usg_ent_id_root    = siteId;
            memUsrGrp.getEntIdBySteUid(con);
            this.wbMemberEntId = memUsrGrp.usg_ent_id;
            this.wbType = DbEntityRelation.ERN_TYPE_USG_PARENT_USG;
        }
    }
    
    public void setGroupMemberMembership(long root_ent_id, dbUserGroup parentGroup, String memberSourcedid, String idType, Hashtable htMembership) throws JAXBException{
        if (root_ent_id==parentGroup.usg_ent_id){
            parentGroup.ent_ste_uid = IMSUserGroup.ROOT_UID;
        }
        MembershipType membership = (MembershipType)htMembership.get(parentGroup.ent_ste_uid);
        // if parent already exist. add the relation only
        if (membership == null){
            ObjectFactory objFactory = new ObjectFactory();
            membership = objFactory.createMembershipType();
            SourcedidType sourcedid = IMSUtils.createSourcedid(parentGroup.ent_ste_uid);
            membership.setSourcedid(sourcedid);
        }
        List l_member = membership.getMember();         
        MemberType member = IMSUtils.createMemberObject(memberSourcedid, ROLE_TYPE_MEMBER_CODE, null, idType);
        l_member.add(member);
        
        htMembership.put(parentGroup.ent_ste_uid, membership);                        
    }

}
