package com.cw.wizbank.enterprise;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Iterator;
import java.util.Hashtable;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;

import com.cw.wizbank.db.DbEntity;

import javax.xml.bind.JAXBException;

import org.imsglobal.enterprise.*;
/*
import org.imsglobal.enterprise.Group;
import org.imsglobal.enterprise.Description;
import org.imsglobal.enterprise.Sourcedid;
import org.imsglobal.enterprise.Grouptype;
import org.imsglobal.enterprise.Typevalue;
*/
public class IMSUserGroup
{
    /**
    for internal ref
    */
    public static final String ROOT_UID             = "ROOT";
    public static final String USG_TYPE_USERGROUP   = "USG";
    public static final String USG_TYPE_APPROVAL    = "APPG";
    public static final String USG_TYPE_ENDORSE     = "ENDG";
    public static final String USG_TYPE_SUPERVISE   = "SUPV";
    public static final String USG_TYPE_DIRECT_SUPERVISE   = "DSPV";

    /**
    Value of usergroup in grouptype.scheme
    */
    private static final String GROUP_TYPE_SCHEME_USERGROUP = "cwn usergroup";
    private static final String GROUP_TYPE_SCHEME_APPROVAL  = "cwn approvinggroup";
    private static final String GROUP_TYPE_SCHEME_ENDORSE   = "cwn endorsinggroup";
    private static final String GROUP_TYPE_SCHEME_SUPERVISE = "cwn supervisegroup";
    private static final String GROUP_TYPE_SCHEME_DIRECT_SUPERVISE = "cwn directsupervisegroup";

    /**
    Value of usergroup in grouptype.level
    */
    private static final String GROUP_TYPE_LEVEL_USERGROUP  = "1";
    private static final String GROUP_TYPE_LEVEL_APPROVAL   = "1";
    private static final String GROUP_TYPE_LEVEL_ENDORSE    = "1";
    private static final String GROUP_TYPE_LEVEL_SUPERVISE  = "1";
    private static final String GROUP_TYPE_LEVEL_DIRECT_SUPERVISE  = "1";

    /**
    Value of usergroup in grouptype
    */
    private static final String GROUP_TYPE_CONTENT_USERGROUP    = "USG";
    private static final String GROUP_TYPE_CONTENT_APPROVAL     = "APPG";
    private static final String GROUP_TYPE_CONTENT_ENDORSE      = "ENDG";
    private static final String GROUP_TYPE_CONTENT_SUPERVISE    = "SUPV";
    private static final String GROUP_TYPE_CONTENT_DIRECT_SUPERVISE    = "DSPV";

    private GroupType _group;
    /**
    Value of this Usergroup Object Entity ID in wbDB
    */
    private long wbEntId;
    /**
    Value of this object sourceDIDID, must initialise in constructor
    */
    private String sourceDIDID;
    private String usr_source;
//    private List _Sourcedid;
//    private List _Grouptype;
//    private List _Relationship;

    /**
    Default Constructor.
    */
    public IMSUserGroup() throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _group = objFactory.createGroupType();
    }
    
    public GroupType getGroup(){
        return _group;
    }
    
    /**
    Construct a new IMSUserGroup that contains data currently contained in the Group argument.
    @param group a Group object with data
    */
    public IMSUserGroup(GroupType group){
//        setRecstatus(group.getRecstatus());
//        setComments(group.getComments());
//        setDescription(group.getDescription());
//        setOrg(group.getOrg());
//        setTimeframe(group.getTimeframe());
//        setEnrollcontrol(group.getEnrollcontrol());
//        setEmail(group.getEmail());
//        setUrl(group.getUrl());
//        setDatasource(group.getDatasource());
//        setExtension(group.getExtension());
        _group = group;
//        this._Sourcedid = group.getSourcedid();
//        this._Grouptype = group.getGrouptype();
//        this._Relationship = group.getRelationship();
        getSourceDIDID();
        return;
    } 
    // override method 
//    public List getSourcedid(){
//        if (_Sourcedid == null)     _Sourcedid = super.getSourcedid();
//        return _Sourcedid; 
//    }
//    public List getGrouptype(){
//        if (_Grouptype == null)     _Grouptype = super.getGrouptype();
//        return _Grouptype; 
//    }
//    public List getRelationship(){
//        if (_Relationship == null)     _Relationship = super.getRelationship();
//        return _Relationship; 
//    }

    /**
    Check if the input Group is an IMSUsergroup
    @return type of the input Group , return null if the inout group is invalid
    */
    public static String getUsergroupType(GroupType group) {
        String usgType = null;
        List l_grouptype = group.getGrouptype();
        if(l_grouptype == null || l_grouptype.size() == 0) {
            //no <grouptype> found, assume it's a usergroup
            usgType = USG_TYPE_USERGROUP;
        } else {
            //assume typevalue and scheme are incorrect,
            //return the type if one set of typevalue, scheme is found to match with one of the scheme.
            for (Iterator i = l_grouptype.iterator(); i.hasNext(); ){
                GroupType.GrouptypeType grouptype = (GroupType.GrouptypeType)i.next();
                String scheme = grouptype.getScheme();
                List l_typevalue = grouptype.getTypevalue();
                for (Iterator j = l_typevalue.iterator(); j.hasNext(); ){
                    TypevalueType typevalue = (TypevalueType)j.next();
                    String typevalueLevel = typevalue.getLevel();
                    String typevalueContent = typevalue.getValue();
                    try {
                        if (scheme.equals(GROUP_TYPE_SCHEME_USERGROUP)){
                            if (typevalueLevel.equals(GROUP_TYPE_LEVEL_USERGROUP) 
                            && typevalueContent.equals(GROUP_TYPE_CONTENT_USERGROUP)){
                                usgType = USG_TYPE_USERGROUP;
                                break;
                            }
                        }else if (scheme.equals(GROUP_TYPE_SCHEME_APPROVAL)){
                            if (typevalueLevel.equals(GROUP_TYPE_LEVEL_APPROVAL) 
                            && typevalueContent.equals(GROUP_TYPE_CONTENT_APPROVAL)){
                                usgType = USG_TYPE_APPROVAL;
                                break;
                            }
                        }else if (scheme.equals(GROUP_TYPE_SCHEME_ENDORSE)){
                            if (typevalueLevel.equals(GROUP_TYPE_LEVEL_ENDORSE) 
                            && typevalueContent.equals(GROUP_TYPE_CONTENT_ENDORSE)){
                                usgType = USG_TYPE_ENDORSE;
                                break;
                            }
                        }else if (scheme.equals(GROUP_TYPE_SCHEME_SUPERVISE)){
                            if (typevalueLevel.equals(GROUP_TYPE_LEVEL_SUPERVISE) 
                            && typevalueContent.equals(GROUP_TYPE_CONTENT_SUPERVISE)){
                                usgType = USG_TYPE_SUPERVISE;
                                break;
                            }
                        }else if (scheme.equals(GROUP_TYPE_SCHEME_DIRECT_SUPERVISE)){
                            if (typevalueLevel.equals(GROUP_TYPE_LEVEL_DIRECT_SUPERVISE) 
                            && typevalueContent.equals(GROUP_TYPE_CONTENT_DIRECT_SUPERVISE)){
                                usgType = USG_TYPE_DIRECT_SUPERVISE;
                                break;
                            }
                        }
                    } catch(NullPointerException e) {
                        continue;
                    }
                }
                if(usgType != null) break;
            }
        }
        return usgType;
    }
    
    /**
    save the data from the IMSUsergroup object to db
    */
    public int save(Connection con, loginProfile wbProfile) throws cwException{
        try{
            int save_code;
            init(con, wbProfile.root_ent_id);
            // do not update root, and do not update synTimestamp (otherwise overwrite start syn date)
            if (sourceDIDID.equals(ROOT_UID)){
                save_code = IMSUtils.SAVE_CODE_UPDATE;
                return save_code;
            }
            dbUserGroup dbusg = new dbUserGroup();
            if (wbEntId>0){
                dbusg.usg_ent_id = wbEntId;
                dbusg.ent_id = wbEntId;
                dbusg.get(con);
                setField(dbusg);
                dbusg.upd(con, wbProfile);
                save_code = IMSUtils.SAVE_CODE_UPDATE;
            }else{
                setField(dbusg);
                dbusg.usg_ent_id_root = wbProfile.root_ent_id;
                dbusg.ent_syn_ind = true;
//                dbusg.ent_syn_rol_ind = true;
                dbusg.ins(con);
                wbEntId= dbusg.usg_ent_id;
                save_code = IMSUtils.SAVE_CODE_INSERT;
            }

            DbEntity dbEnt = new DbEntity();
            dbEnt.ent_id = wbEntId;
            dbEnt.updSynDate(con);
            return save_code;
        }catch (Exception e){
            throw new cwException("Error in " + sourceDIDID + " " + e.getMessage());
        }
    }        
    
    /** 
    get sourceDIDID
    set sourceDIDID if sourceDIDID not yet set
    @return sourceDIDID
    */
    public String getSourceDIDID() {
        if (this.sourceDIDID==null){
            this.sourceDIDID = ((SourcedidType) _group.getSourcedid().iterator().next()).getId();
        }
        return this.sourceDIDID;
    }
    
    public String getSource() {
        if (this.usr_source==null) {
            this.usr_source = ((SourcedidType) _group.getSourcedid().iterator().next()).getSource();
        }
        return this.usr_source;
    }
    
    /**
    initialise the usergroup Entity ID by using the sourceId
    @param CONNECTION
    @param site id
    */
    private void init(Connection con, long siteId) throws qdbException{
        if (sourceDIDID.equals(ROOT_UID)){
            this.wbEntId = siteId;        
        }else{
            dbUserGroup dbUsrGrp = new dbUserGroup();
            dbUsrGrp.ent_ste_uid        = sourceDIDID;
            dbUsrGrp.usg_ent_id_root    = siteId;
            dbUsrGrp.getEntIdBySteUid(con);
            this.wbEntId = dbUsrGrp.usg_ent_id;
        }
    }
    /**
    set data from IMSUsergroup to wb dbUserGroup Object
    */
    private void setField(dbUserGroup dbusg){
        dbusg.ent_ste_uid        = sourceDIDID;
        DescriptionType _Description = (DescriptionType)_group.getDescription();
        dbusg.usg_display_bil = _Description.getShort();
        
        String tmpStr = _Description.getLong();
        if (tmpStr != null){
            dbusg.usg_name = tmpStr;
        }
        tmpStr = _Description.getFull();
        if (tmpStr != null){
            dbusg.usg_desc = tmpStr;
        }
    }        
    
    public GroupType convertToGroup(long root_ent_id, dbUserGroup usergroup) throws JAXBException{
        if (root_ent_id==usergroup.usg_ent_id){
            usergroup.ent_ste_uid = IMSUserGroup.ROOT_UID;
        }
        ObjectFactory objFactory = new ObjectFactory();
        GroupType group = objFactory.createGroupType();
        
        List sourcedidList = group.getSourcedid();
        SourcedidType sourcedid = objFactory.createSourcedidType();
        sourcedid.setSource(IMSEnterpriseApp.exportSource);
        sourcedid.setId(usergroup.ent_ste_uid);
        sourcedidList.add(sourcedid);
        
        List groupTypeList = group.getGrouptype();
        GroupType.GrouptypeType grouptype = objFactory.createGroupTypeGrouptypeType();
        grouptype.setScheme(GROUP_TYPE_SCHEME_USERGROUP);
        
        List groupTypeValueList = grouptype.getTypevalue();
        TypevalueType typevalue = objFactory.createTypevalueType();
        typevalue.setLevel(GROUP_TYPE_LEVEL_USERGROUP);
        typevalue.setValue(GROUP_TYPE_CONTENT_USERGROUP);
        groupTypeValueList.add(typevalue);
        
        groupTypeList.add(grouptype);
        
        DescriptionType description = objFactory.createDescriptionType();
        description.setShort(usergroup.usg_display_bil);
        description.setLong(usergroup.usg_name);
        description.setFull(usergroup.usg_desc);
        
        group.setDescription(description);
        return group;
    } 
    
    public void addGroupNItsParentGroup(Connection con, long root_ent_id, dbUserGroup usergroup, Hashtable htGroup, Hashtable htMembership) throws JAXBException, SQLException{
        if (htGroup.get(usergroup.ent_ste_uid)==null){
            GroupType group = convertToGroup(root_ent_id, usergroup);
            htGroup.put(usergroup.ent_ste_uid, group);
            
            dbUserGroup parentGroup = dbEntityRelation.getParentUserGroup(con, usergroup.usg_ent_id);
            if (parentGroup!=null){
                IMSGroupMember imsGpm = new IMSGroupMember();
                imsGpm.setGroupMemberMembership(root_ent_id, parentGroup, usergroup.ent_ste_uid, "2", htMembership);
                addGroupNItsParentGroup(con, root_ent_id, parentGroup, htGroup, htMembership);
            }
        }
    }

}
