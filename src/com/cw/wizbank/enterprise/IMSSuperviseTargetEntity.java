package com.cw.wizbank.enterprise;

import java.lang.Long;
import java.sql.*;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;

import javax.xml.bind.*;

import org.imsglobal.enterprise.*;

import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
//import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;
import com.cwn.wizbank.utils.CommonLog;

public class IMSSuperviseTargetEntity
{
    private MembershipType _membership;
    
    /**
    default constructor
    */
    public IMSSuperviseTargetEntity() throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _membership = objFactory.createMembership();
    }

    public String parentUsgType = null;
    // spt_source_usr_ent_id, normally one source user in one supervise group
    private Vector wbEntIds = new Vector();
    private Vector wbUsrIds = new Vector();
    
    /**
    Value of approver ent id
    */
    private Vector superviseEntIds = new Vector();
    private Vector superviseSourceIds = new Vector();
    // 1 as person, 2 as group
    private Vector superviseTypes = new Vector();
    
    private Vector blackListPerson;
    private Vector blackListGroup;
    

    public IMSSuperviseTargetEntity(MembershipType membership, String parentUsgType, Vector blackListPerson, Vector blackListGroup){
        _membership = membership;
        this.parentUsgType = parentUsgType;
        this.blackListPerson = blackListPerson;
        this.blackListGroup = blackListGroup;
    }

    private void init(Connection con, long siteId) throws cwException, qdbException{
        if (parentUsgType==null){
            throw new cwException("INVALID USG_TYPE:" + parentUsgType);
        }
        List l_member = _membership.getMember();
        for (Iterator j = l_member.iterator(); j.hasNext(); ){
            MemberType member = (MemberType)j.next();
            List role_list = member.getRole();
            for (Iterator k = role_list.iterator(); k.hasNext();){
                RoleType role = (RoleType)k.next();
                if (role.getRoletype().equals(IMSGroupMember.ROLE_TYPE_MEMBER_CODE)){
                    if (parentUsgType.equals(IMSUserGroup.USG_TYPE_DIRECT_SUPERVISE)){
                        wbUsrIds.addElement(member.getSourcedid().getId());
                    }else if (parentUsgType.equals(IMSUserGroup.USG_TYPE_SUPERVISE)){
                        superviseSourceIds.addElement(member.getSourcedid().getId());
                        superviseTypes.addElement(member.getIdtype());
                    }else{
                        throw new cwException("IMSSuperviseTargetEntity.init invalid usg type: " + parentUsgType);
                    }
                    
                }else if (role.getRoletype().equals(IMSGroupMember.ROLE_TYPE_MANAGER_CODE)){
                    if (parentUsgType.equals(IMSUserGroup.USG_TYPE_DIRECT_SUPERVISE)){
                        superviseSourceIds.addElement(member.getSourcedid().getId());
                        superviseTypes.addElement(member.getIdtype());
                    }else if (parentUsgType.equals(IMSUserGroup.USG_TYPE_SUPERVISE)){
                        wbUsrIds.addElement(member.getSourcedid().getId());
                    }else{
                        throw new cwException("IMSSuperviseTargetEntity.init invalid usg type: " + parentUsgType);
                    }
                }
            }
        }
        
        // init ent_id
        for (int i=0; i<wbUsrIds.size(); i++){
            if (blackListPerson.contains((String)wbUsrIds.elementAt(i))){
                throw new cwException((String)wbUsrIds.elementAt(i) + " HAS BEEN FAILURE IN IMPORT. ITS SUPERVISE ATTRIBUTE WOULD NOT BE IMPORTED ");
            }
            dbRegUser dbusr = new dbRegUser();
            long tmpUsrEntId = dbusr.getEntIdStatusOk(con, (String)wbUsrIds.elementAt(i), siteId);
            if (tmpUsrEntId <= 0) {
                tmpUsrEntId = dbusr.getEntId(con, (String)wbUsrIds.elementAt(i), siteId);
            }
            if (tmpUsrEntId <=0){
                throw new cwException((String)wbUsrIds.elementAt(i) + " WAS NOT FOUND WHILE IMPORTING SUPERVISE ATTRIBUTE.");
            }
            wbEntIds.addElement(new Long(tmpUsrEntId));
        }
            
        for (int i=0; i<superviseSourceIds.size(); i++){
            if (((String)superviseTypes.elementAt(i)).equals("1")){
                if (blackListPerson.contains((String)superviseSourceIds.elementAt(i))){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, (String)superviseSourceIds.elementAt(i) + " HAS BEEN FAILURE IN IMPORT. ITS SUPERVISE RELATION WITH " + wbUsrIds.elementAt(0) + "WOULD NOT BE SAVED");
                    superviseSourceIds.removeElementAt(i);
                    superviseTypes.removeElementAt(i);
                } else{
                    dbRegUser dbusr = new dbRegUser();
                    long tmpUsrEntId = dbusr.getEntId(con, (String)superviseSourceIds.elementAt(i), siteId);
                    if (tmpUsrEntId <=0){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, (String)superviseSourceIds.elementAt(i) + " WAS NOT FOUND WHILE IMPORTING SUPERVISE ATTRIBUTE OF " + wbUsrIds.elementAt(0));
                        superviseSourceIds.removeElementAt(i);
                        superviseTypes.removeElementAt(i);
                    }else{
                        superviseEntIds.addElement(new Long(tmpUsrEntId));
                    }
                }
            }else{
                if (blackListGroup.contains((String)superviseSourceIds.elementAt(i))){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, (String)superviseSourceIds.elementAt(i) + " HAS BEEN FAILURE IN IMPORT. ITS SUPERVISE RELATION WOULD NOT BE SAVED");
                    superviseSourceIds.removeElementAt(i);
                    superviseTypes.removeElementAt(i);
                }else{ 
                    dbUserGroup dbgrp        = new dbUserGroup();
                    dbgrp.ent_ste_uid        = (String)superviseSourceIds.elementAt(i);
                    dbgrp.usg_ent_id_root    = siteId;
                    dbgrp.getEntIdBySteUid(con);
                    if (dbgrp.usg_ent_id <=0){                    
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, (String)superviseSourceIds.elementAt(i) + " WAS NOT FOUND WHILE IMPORTING SUPERVISE ATTRIBUTE OF " + wbUsrIds.elementAt(0));
                        superviseSourceIds.removeElementAt(i);
                        superviseTypes.removeElementAt(i);
                    }else{
                        superviseEntIds.addElement(new Long(dbgrp.usg_ent_id));
                    }
                }
            }
        }

    }
    /**
    save data from the IMSSuperviseTargetEntity to db
    */
    public int success_cnt = 0;
    public int unsuccess_cnt = 0;
    
    public void save(Connection con, loginProfile wbProfile) {
        try{
            String supervise_type;
            init(con, wbProfile.root_ent_id);
            if (parentUsgType.equals(IMSUserGroup.USG_TYPE_SUPERVISE)){
                supervise_type = "SUPERVISE";
            }else{
                supervise_type = "DIRECT_SUPERVISE";
            }
            if (wbEntIds.size() == 0 ){
                return;
            }
            String[] str_supervise_ent_ids = new String[superviseEntIds.size()];
            for (int k=0; k<superviseEntIds.size(); k++){
                str_supervise_ent_ids[k] = ((Long)superviseEntIds.elementAt(k)).longValue() + "";
            }
            for (int i=0; i<wbEntIds.size(); i++){
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = ((Long)wbEntIds.elementAt(i)).longValue();
                usr.saveSuperviseTarget(con, str_supervise_ent_ids, null, null, supervise_type, wbProfile.usr_id); 
                con.commit();
                IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, wbUsrIds.elementAt(i) + " SUPERVISE ATTRIBUTE SAVED.");
                success_cnt++;
            }
        }catch (Exception e){
            CommonLog.error(e.getMessage(),e);
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, wbUsrIds.elementAt(0) + " SUPERVISE ATTRIBUTE FAILED TO BE SAVED.",  e);
            unsuccess_cnt++;
        }
    }

    public void setDirectSupverisorMembership(Connection con, long root_ent_id, Vector vtUsrEntId, Hashtable htGroup, Hashtable htMembership) throws JAXBException, SQLException{
        if (vtUsrEntId.size()==0){
            return;
        }
        
        Hashtable htUsrDSupervisor = ViewSuperviseTargetEntity.getDirectSupervisorByUsrEntIds(con, vtUsrEntId);
        ObjectFactory objFactory = new ObjectFactory();

        Enumeration e_user = htUsrDSupervisor.keys();
        while(e_user.hasMoreElements()) {
            String usr_ste_usr_id = (String) e_user.nextElement(); 
            String dummy_group_id = "dummy_direct_supervise_group" + usr_ste_usr_id;
            GroupType directSuperviseGroup = IMSUtils.createDummySuperviseGroup(dummy_group_id,"DSPV");
            htGroup.put(dummy_group_id, directSuperviseGroup);            
            
            MembershipType membership = objFactory.createMembershipType();
            SourcedidType sourcedid = IMSUtils.createSourcedid(dummy_group_id);
            membership.setSourcedid(sourcedid);

            List l_member = membership.getMember();         
            MemberType member = IMSUtils.createMemberObject(usr_ste_usr_id, IMSGroupMember.ROLE_TYPE_MEMBER_CODE, null, "1");
            l_member.add(member);
            
            Vector vtDSupervisor = (Vector)htUsrDSupervisor.get(usr_ste_usr_id);
            for (int i=0; i<vtDSupervisor.size(); i++){
                String direct_supervisor = (String)vtDSupervisor.elementAt(i);
                if (direct_supervisor!=""){
                    l_member.add(IMSUtils.createMemberObject((String)vtDSupervisor.elementAt(i), IMSGroupMember.ROLE_TYPE_MANAGER_CODE, null, "1"));            
                }
            }
            htMembership.put(dummy_group_id, membership);            
        }
    }
    
    public void setSupverisedGroupMembership(Connection con, long root_ent_id, Vector vtUsrEntId, Hashtable htGroup, Hashtable htMembership) throws JAXBException, SQLException{
        Hashtable htUsrSupervisedGrp = ViewSuperviseTargetEntity.getSupervisedGroupByUsrEntIds(con, vtUsrEntId);
        ObjectFactory objFactory = new ObjectFactory();

        Enumeration e_user = htUsrSupervisedGrp.keys();
        while(e_user.hasMoreElements()) {
            String usr_ste_usr_id = (String) e_user.nextElement(); 
            String dummy_group_id = "dummy_supervise_group" + usr_ste_usr_id;
            GroupType superviseGroup = IMSUtils.createDummySuperviseGroup(dummy_group_id,"SUPV");
            htGroup.put(dummy_group_id, superviseGroup);            
            
            MembershipType membership = objFactory.createMembershipType();
            SourcedidType sourcedid = IMSUtils.createSourcedid(dummy_group_id);
            membership.setSourcedid(sourcedid);

            List l_member = membership.getMember();         
            MemberType member = IMSUtils.createMemberObject(usr_ste_usr_id, IMSGroupMember.ROLE_TYPE_MANAGER_CODE, null, "1");
            l_member.add(member);
            
            Vector vtSupervisedGrp = (Vector)htUsrSupervisedGrp.get(usr_ste_usr_id);
            for (int i=0; i<vtSupervisedGrp.size(); i++){
                String groupCode = (String)vtSupervisedGrp.elementAt(i);
                if (groupCode != ""){
                    l_member.add(IMSUtils.createMemberObject(groupCode, IMSGroupMember.ROLE_TYPE_MEMBER_CODE, null, "2"));            
                }
            }
            htMembership.put(dummy_group_id, membership);            
        }        
    }

}