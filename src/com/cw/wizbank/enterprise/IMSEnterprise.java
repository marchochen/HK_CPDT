package com.cw.wizbank.enterprise;

import java.lang.Long;
import java.sql.*;
import java.util.Vector;
import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.xml.bind.*;

import org.imsglobal.enterprise.*;

/*
import org.imsglobal.enterprise.Enterprise;
import org.imsglobal.enterprise.Properties;
import org.imsglobal.enterprise.Sourcedid;
import org.imsglobal.enterprise.Person;
import org.imsglobal.enterprise.Group;
import org.imsglobal.enterprise.Membership;
import org.imsglobal.enterprise.Member;
import org.imsglobal.enterprise.Role;
*/
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.view.ViewItemRelation;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbEntity;
import com.cw.wizbank.db.DbEntityRelation;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbMailSender;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;


public class IMSEnterprise
{
    public static final String ACTION_IMPORT = "IMPORT";
    public static final String ACTION_EXPORT = "EXPORT";

    public static final String TYPE_USER = "USER";
    public static final String TYPE_USER_GROUP = "USER_GROUP";
    public static final String TYPE_USER_COURSE = "USER_COURSE";
    public static final String TYPE_COURSE = "COURSE";
    public static final String TYPE_RESULT = "RESULT";
    public static final String TYPE_COURSE_RESULT = "COURSE_RESULT";
    public static final String TYPE_COURSE_ENROLLMENT = "COURSE_ENROLLMENT";
    
    public static final String STATUS_SUCCESS   =   "SUCCESS";
    public static final String STATUS_UNSUCCESS =   "UNSUCCESS";
    
    public static final String TYPE_REMARK_USER="IMS_LOG_USER";
    public static final String TYPE_REMARK_ENROLL="IMS_LOG_ENROLLMENT";
    public static final String TYPE_REMARK_COURCE="IMS_LOG_COURCE";
    
    public Timestamp synTimestamp;
    private Enterprise _enterprise;
    private CleanWorkList cleanWorkList = new CleanWorkList();
    
    private class CleanWorkList{
        boolean cleanUpPerson = false;
        boolean cleanUpUsergroup = false;
        Vector vtCleanUpGroupMemberType = new Vector();
        boolean cleanUpItem = false;
        boolean cleanUpItemAccess = false;
        boolean cleanUpApplication = false;
//        boolean cleanUpCatalog = false;
        boolean cleanUpUsrRoleTargetEntity = false;
        boolean cleanUpTreeNode = false;
    }

    public IMSEnterprise(Enterprise enterprise){
        this._enterprise = enterprise;        
    }
    
    public IMSEnterprise() throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _enterprise = objFactory.createEnterprise();
    }
    
    public Enterprise getEnterprise(){
        return _enterprise;
    } 
    /**
    contain a list of SOURCEDIDID of failure person
    */
    private Vector blackListPerson=new Vector();
    /**
    contain a list of SOURCEDIDID of failure group
    */
    private Vector blackListGroup       = new Vector();
    private Vector approvalGroup        = new Vector();
    private Vector endorseGroup         = new Vector();
    private Vector directSuperviseGroup= new Vector();
    private Vector superviseGroup       = new Vector();

    /*
    *   IMPORT
    */

    public Hashtable updDB(Connection con, cwIniFile ini, loginProfile wbProfile, long siteId, String type, String actionRule, boolean encrytePwd, String from) {
        Hashtable h_status_count = null;
            try{
                DbEntity dbEnt = new DbEntity();
                dbEnt.ent_id = siteId;
                dbEnt.updSynDate(con);
                synTimestamp = dbEnt.ent_syn_date;

                h_status_count = new Hashtable();
                h_status_count.put(TYPE_USER, updPersons(con, ini, wbProfile, type, encrytePwd));
                h_status_count.put(TYPE_USER_GROUP, updGroups(con, ini, wbProfile, type));
                h_status_count.put(TYPE_USER_COURSE, updMemberships(con, ini, wbProfile, type, actionRule, from));

            }catch (cwException e){
                // error in updGroup?
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, e);
            }catch (SQLException e){
                // error in updSynDate
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_420") + "：" , e);
            }
             return h_status_count;
    }

    private Hashtable updPersons(Connection con, cwIniFile ini, loginProfile wbProfile, String type, boolean encrytePwd){
        boolean bSuccess = true;
        Hashtable h_status_count = new Hashtable();
        int success_entity = 0;
        int unsuccess_entity = 0;
        try{
//            IMSPerson.initRole(con, wbProfile.root_ent_id, ini.getValue("SYS_ROLE_MAP"));

            List _Person = _enterprise.getPerson();
            String sourceDIDID;
//            String strGradeId = ini.getValue("DEFAULT_GRADE_ID");
            String ins_usr_msg_title = ini.getValue("INS_USR_MSG_TITLE");
            String initialUserStatus = ini.getValue("IMPORT_USER_STATUS");
            if (initialUserStatus == null ){
                initialUserStatus = dbRegUser.USR_STATUS_OK;
            }else if (!initialUserStatus.equals(dbRegUser.USR_STATUS_PENDING) && !initialUserStatus.equals(dbRegUser.USR_STATUS_OK)){
                // invalid USR_STATUS
                initialUserStatus = dbRegUser.USR_STATUS_OK;
            }
                
            Long L_default_grade = null;

            
            if (_Person.size()> 0 ){
                DbUserGrade defaultGrade = DbUserGrade.getDefaultGrade(con, wbProfile.root_ent_id);
                L_default_grade = new Long(defaultGrade.ugr_ent_id);
            }
            
            int save_code; 
            String str_log;
            for (Iterator i = _Person.iterator(); i.hasNext(); ) {
                IMSPerson imsperson = new IMSPerson((PersonType)i.next());
                sourceDIDID = imsperson.getSourceDIDID();
                try{
                    save_code = imsperson.save(con, wbProfile.root_ent_id, L_default_grade, initialUserStatus, wbProfile, ins_usr_msg_title, encrytePwd);
                    success_entity++;
                    con.commit();
                    if (save_code == IMSUtils.SAVE_CODE_UPDATE){
                        str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_440") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_441");
                    }
                    else if (save_code == IMSUtils.SAVE_CODE_INSERT){
                        str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_440") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_442");
                    }else{
                        str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_440") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_443");
                    }
                    IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, str_log);

                }catch(Exception e){
                    // write log
                    bSuccess = false;
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_444") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_445") , e);
                    blackListPerson.addElement(sourceDIDID);
                    try{
                        unsuccess_entity++;
                        con.rollback();
                    }catch (SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_446") + "： " + sourceDIDID , e);
                    }
                }
            }
        }catch(Exception e){
            // error in initRole
            bSuccess = false;
            CommonLog.error(e.getMessage(),e);
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_447"),  e);
        }
        if (type.equalsIgnoreCase(TYPE_USER_GROUP)){
            cleanWorkList.cleanUpPerson = bSuccess;
        }
        h_status_count.put(STATUS_SUCCESS, new Integer(success_entity));
        h_status_count.put(STATUS_UNSUCCESS, new Integer(unsuccess_entity));
        return h_status_count;
        
    }

    private Hashtable updGroups(Connection con, cwIniFile ini, loginProfile wbProfile, String type)
        throws cwException {
        Hashtable h_status_count = new Hashtable();
        int success_entity = 0;
        int unsuccess_entity = 0;

        List _Group = _enterprise.getGroup();
        if (type.equalsIgnoreCase(TYPE_USER_GROUP)){
            boolean bSuccess = true;
            String sourceDIDID;
            int save_code;
            for (Iterator i = _Group.iterator(); i.hasNext(); ) {
                GroupType group = (GroupType)i.next();
                IMSUserGroup imsUsg = new IMSUserGroup(group);
                sourceDIDID = imsUsg.getSourceDIDID();
                String usgType = IMSUserGroup.getUsergroupType(group);
                if (usgType == null){
                    bSuccess = false;
                    blackListGroup.addElement(sourceDIDID);
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_448") + sourceDIDID + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_445"));
                }else if (usgType.equals(IMSUserGroup.USG_TYPE_USERGROUP)){
                    try {
                        save_code = imsUsg.save(con, wbProfile);
                        success_entity++;
                        con.commit();
                        String str_log;
                        if (save_code == IMSUtils.SAVE_CODE_UPDATE){
                            str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_289") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_441");
                        }else if (save_code == IMSUtils.SAVE_CODE_INSERT){
                            str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_289") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_442");
                        }else{
                            str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_289") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_443");
                        }

                        IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, str_log);
                    } catch(Exception e) {
                        bSuccess = false;
                        blackListGroup.addElement(sourceDIDID);
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_448") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_445"), e);
                        try{
                            unsuccess_entity++;
                            con.rollback();
                        }catch (SQLException sqle){
                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_449") + "： " + sourceDIDID , e);
                        }
                    }
                }else if (usgType.equals(IMSUserGroup.USG_TYPE_APPROVAL)){
                    approvalGroup.addElement(sourceDIDID);
                }else if (usgType.equals(IMSUserGroup.USG_TYPE_ENDORSE)){
                    endorseGroup.addElement(sourceDIDID);
                }else if (usgType.equals(IMSUserGroup.USG_TYPE_SUPERVISE)){
                    superviseGroup.addElement(sourceDIDID);
                }else if (usgType.equals(IMSUserGroup.USG_TYPE_DIRECT_SUPERVISE)){
                    directSuperviseGroup.addElement(sourceDIDID);
                }else {
                    bSuccess = false;
                    blackListGroup.addElement(sourceDIDID);
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_448") + "：" + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_450") +"：" + usgType);
                }
            }
            cleanWorkList.cleanUpUsergroup = bSuccess;
        }else if (type.equalsIgnoreCase(TYPE_USER_COURSE) || type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT) || type.equalsIgnoreCase(TYPE_COURSE)){
            boolean isItem = false;
            boolean isTreeNode = false;
            //boolean
            cleanWorkList.cleanUpItem = true;
            cleanWorkList.cleanUpTreeNode = true;
            String sourceDIDID;
            GroupType _group = null;
            Vector groupVec = new Vector();
            
            for (Iterator i = _Group.iterator(); i.hasNext(); ) {
                _group = (GroupType)i.next();
                if((isItem = IMSItem.IMSvalidate(_group))){
                    IMSItem imsItm = new IMSItem(_group);
                    sourceDIDID = imsItm.getSourceDIDID();
                    try {
                        imsItm.save(con, ini.getValue("DEFAULT_IMPORT_ITEM_TYPE"), wbProfile);
                        con.commit();
                        success_entity++;
                        IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, sourceDIDID);
                    } catch(Exception e) {
                        cleanWorkList.cleanUpItem = false;
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_451") , e);
                        blackListGroup.addElement(sourceDIDID);
                        try{
                        	unsuccess_entity++;
                            con.rollback();
                        }catch (SQLException sqle){
                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_452") + "：" , e);
                        }
                    }
                }else if((isTreeNode = IMSTreeNode.IMSvalidate(_group))){
                    groupVec.addElement(_group);
                }else {
                    //black list the group if it is not an item nor treenode
                    sourceDIDID = ((SourcedidType) _group.getSourcedid().iterator().next()).getId();
                    blackListGroup.addElement(sourceDIDID);
                    cleanWorkList.cleanUpItem = false;
                    cleanWorkList.cleanUpTreeNode = false;
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_453") + "：" + sourceDIDID);
                }
            }
            //Clean TreeNode if there are no unknow group appear and there exists at least 1 treenode appears
            cleanWorkList.cleanUpTreeNode = (cleanWorkList.cleanUpTreeNode && groupVec.size() > 0);
            for(int j=0;j<groupVec.size();j++){
                String[] catalogItemType = cwUtils.splitToString(ini.getValue("CATALOG_ITEM_TYPE"),"~");
                if( catalogItemType == null || catalogItemType.length == 0 || catalogItemType[0].equalsIgnoreCase("ALL"))
                    catalogItemType = IMSTreeNode.getAllWbItemType(con, wbProfile.root_ent_id);
                IMSTreeNode imsTnd = new IMSTreeNode((GroupType)groupVec.elementAt(j), synTimestamp, catalogItemType);
                sourceDIDID = imsTnd.getSourceDIDID();
                try{
                    if( imsTnd.save(con, wbProfile, blackListGroup) )
                        IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, sourceDIDID);
                    else
                        cleanWorkList.cleanUpTreeNode = false;    
                    con.commit();

                }catch(Exception e){
                    cleanWorkList.cleanUpTreeNode = false;
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_454") , e);
                    blackListGroup.addElement(sourceDIDID);
                    try{
                        con.rollback();;
                    }catch(SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_455") , e);
                    }
                }
            }
        }else if (type.equalsIgnoreCase(TYPE_RESULT)){            
            // nothing
        }else{
            throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_421") + "： " + type);
        }
        h_status_count.put(STATUS_SUCCESS, new Integer(success_entity));
        h_status_count.put(STATUS_UNSUCCESS,  new Integer(unsuccess_entity));
        return h_status_count;
        
    }
    private Hashtable updMemberships(Connection con, cwIniFile ini, loginProfile wbProfile, String type, String actionRule, String from) throws cwException, SQLException{
        Hashtable h_status_count = new Hashtable();
        int success_entity = 0;
        int unsuccess_entity = 0;
        int save_code;
        String str_log;

        if (type.equalsIgnoreCase(TYPE_USER_GROUP)){
            String sourceDIDID="";
            String memberSourceDIDID="";
            boolean bRoleSuccess = true;
            boolean bGroupMemberSuccess = true;
            IMSUsrRoleTargetEntity.initRole(con, wbProfile.root_ent_id);
            
            for (Iterator i = _enterprise.getMembership().iterator(); i.hasNext(); ) {
                try{
                    MembershipType membership = (MembershipType)i.next();
                    sourceDIDID = membership.getSourcedid().getId();
                    if (blackListGroup.contains(sourceDIDID)) {
                        throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_422") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                    }else if (approvalGroup.contains(sourceDIDID) || endorseGroup.contains(sourceDIDID)){
                        String usgType = null;
                        if (approvalGroup.contains(sourceDIDID)){
                            usgType = IMSUserGroup.USG_TYPE_APPROVAL;
                        }else if (endorseGroup.contains(sourceDIDID)){
                            usgType = IMSUserGroup.USG_TYPE_ENDORSE;
                        }
                        List member_list = membership.getMember();
                        // list of user : usr ste usr id (string) 
                        Vector vtApprovalList = new Vector();
                        // list of user (Member object) with special role in the approval list
                        Vector vtApproverMemberList = new Vector();
                        Vector vtUsgType = new Vector();
                        // list of role object for the corresponsing vtApproverList
                        Vector vtRoleList = new Vector();    
                        // prepare vector
                        for (Iterator j = member_list.iterator(); j.hasNext(); ){
                            MemberType member = (MemberType)j.next();
                            List role_list = member.getRole();
                            for (Iterator k = role_list.iterator(); k.hasNext();){
                                RoleType role = (RoleType)k.next();
                                if (role.getRoletype().equals(IMSGroupMember.ROLE_TYPE_MEMBER_CODE)
                                    || role.getRoletype().equalsIgnoreCase(IMSGroupMember.ROLE_TYPE_MEMBER))
                                {   
                                    vtApprovalList.add(member.getSourcedid().getId());
                                }else{
                                    vtApproverMemberList.add(member);
                                    vtUsgType.add(usgType);
                                    vtRoleList.add(role);    
                                }
                            }
                        }
                        // start execute 
                        MemberType tmpMember;
                        for (int k=0; k<vtApproverMemberList.size(); k++){
                            // approver is in failure list
                            tmpMember = (MemberType)vtApproverMemberList.elementAt(k);
                            if (blackListPerson.contains(tmpMember.getSourcedid().getId())){
                                throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_424") + " " + tmpMember.getSourcedid().getId() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                            }else{
                                for (int j=0; j<vtApprovalList.size(); j++){
                                    IMSUsrRoleTargetEntity imsTargetEnt = new IMSUsrRoleTargetEntity(membership, tmpMember, (RoleType)vtRoleList.elementAt(k), (String)vtUsgType.elementAt(k));
                                    imsTargetEnt.sourceDIDID = (String)vtApprovalList.elementAt(j);

                                    try{
                                        if (blackListPerson.contains(imsTargetEnt.sourceDIDID)){
                                            throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_424") + " " + imsTargetEnt.sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                        }
                                        imsTargetEnt.save(con, wbProfile);
                                        success_entity++;
                                        con.commit();
                                        IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, tmpMember.getSourcedid().getId() + "&" + imsTargetEnt.sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_456"));
                                    }catch (Exception e){
                                        bRoleSuccess = false;
                                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, tmpMember.getSourcedid().getId() + "&" + imsTargetEnt.sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_457"),  e);
                                        unsuccess_entity++;
                                        try{
                                            con.rollback();
                                        }catch (SQLException sqle){
                                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_458") + "：" + tmpMember.getSourcedid().getId() + "&" + imsTargetEnt.sourceDIDID , e);
                                        }
                                    }
                                }                        
                            }
                        }
                    }else if (superviseGroup.contains(sourceDIDID) || directSuperviseGroup.contains(sourceDIDID)){
                        String usgType = null;
                        if (superviseGroup.contains(sourceDIDID)){
                            usgType = IMSUserGroup.USG_TYPE_SUPERVISE;
                        }else if (directSuperviseGroup.contains(sourceDIDID)){
                            usgType = IMSUserGroup.USG_TYPE_DIRECT_SUPERVISE;
                        }
                        IMSSuperviseTargetEntity imsSup = new IMSSuperviseTargetEntity(membership, usgType, blackListPerson, blackListGroup);
                        imsSup.save(con, wbProfile);

                        success_entity+=imsSup.success_cnt;
                        unsuccess_entity+=imsSup.unsuccess_cnt;
                    }else {
                        // assume normal group
                        List member_list = membership.getMember();
                        
                        for (Iterator j = member_list.iterator(); j.hasNext(); ){
                            MemberType member = (MemberType)j.next();
                            List role_list = member.getRole();
                            for (Iterator k = role_list.iterator(); k.hasNext();){
                                RoleType role = (RoleType)k.next();
                                if (role.getRoletype().equals(IMSGroupMember.ROLE_TYPE_MEMBER_CODE)
                                    || role.getRoletype().equalsIgnoreCase(IMSGroupMember.ROLE_TYPE_MEMBER))
                                {
                                    if(role.getSubrole() != null && role.getSubrole().equals(IMSGroupMember.ROLE_SUBTYPE_APPROVAL_MEMBER)){
                                        try{
                                            // save into usr_appn_approval_usg_ent_id
                                            IMSUsrAppnApprovalGroup imsuaag = new IMSUsrAppnApprovalGroup(membership, member, role);
                                            sourceDIDID = imsuaag.getSourceDIDID();
                                            memberSourceDIDID = imsuaag.getMemberSourceDIDID();
                                            if (blackListPerson.contains(memberSourceDIDID)){
                                                throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_424") + " " + memberSourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                            }
                                            save_code = imsuaag.save(con, wbProfile.root_ent_id, wbProfile);
                                            Vector vt_tmp = new Vector();
                                            vt_tmp.addElement(sourceDIDID);
                                            Hashtable ht_displayName = dbUserGroup.getDisplayName(con, wbProfile.root_ent_id, vt_tmp);
                                            String tmpDisplayName = (String)ht_displayName.get(sourceDIDID);
                                            str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_408") + " " + memberSourceDIDID;
                                            if (tmpDisplayName!=null){
                                                str_log += " - " + (String)ht_displayName.get(sourceDIDID);
                                            }
                                            if (save_code == IMSUtils.SAVE_CODE_UPDATE){
                                                str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_441");
                                            }else if (save_code == IMSUtils.SAVE_CODE_INSERT){
                                                str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_442");
                                            }else if (save_code == IMSUtils.SAVE_CODE_NOT_SAVE){
                                                str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_459");
                                            }else if (save_code == IMSUtils.SAVE_CODE_RESET){
                                                str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_460");
                                            }else{
                                                str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_443");
                                            }

                                            IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, str_log);
                                            success_entity++;
                                            con.commit();
                                        }catch (Exception e){
                                            bGroupMemberSuccess = false;
                                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, sourceDIDID + "&" + memberSourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_405") , e);
                                            unsuccess_entity++;
                                            try{
                                                con.rollback();
                                            }catch (SQLException sqle){
                                                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_406") + "：" + sourceDIDID + "&" + memberSourceDIDID , e);
                                            }
                                        }
                                    }else{
                                       // normal user to group relation
                                        try{
                                            IMSGroupMember imsGpm = new IMSGroupMember(membership, member, role);
                                            sourceDIDID = imsGpm.getSourceDIDID();
                                            memberSourceDIDID = imsGpm.getMemberSourceDIDID();
                                            if (blackListGroup.contains(memberSourceDIDID) || blackListPerson.contains(memberSourceDIDID)){
                                                throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_424") + "" + memberSourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                            }
                                            if (!approvalGroup.contains(memberSourceDIDID) && !endorseGroup.contains(memberSourceDIDID) || !superviseGroup.contains(memberSourceDIDID)){
                                                save_code = imsGpm.save(con, wbProfile.root_ent_id, wbProfile);
                                                //重新初始化EntityFullPath
                                                EntityFullPath.getInstance(con).enclose(con, 0);
                                                Vector vt_tmp = new Vector();
                                                vt_tmp.addElement(sourceDIDID);
                                                Hashtable ht_displayName = dbUserGroup.getDisplayName(con, wbProfile.root_ent_id, vt_tmp);                                            
                                                str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_407") + " " + memberSourceDIDID + " - " + (String)ht_displayName.get(sourceDIDID);
                                                if (save_code == IMSUtils.SAVE_CODE_UPDATE){
                                                    str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_441");
                                                }else if (save_code == IMSUtils.SAVE_CODE_INSERT){
                                                    str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_442");
                                                }else if (save_code == IMSUtils.SAVE_CODE_NOT_SAVE){
                                                    str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_459");
                                                }else{
                                                    str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_443");
                                                }

                                                IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, str_log);
                                                success_entity++;
                                                con.commit();
                                            }
                                        }catch (Exception e){
                                            bGroupMemberSuccess = false;
                                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, sourceDIDID + "&" + memberSourceDIDID + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_405") , e);
                                            unsuccess_entity++;
                                            try{
                                                con.rollback();
                                            }catch (SQLException sqle){
                                                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_405") + "：" + sourceDIDID + "&" + memberSourceDIDID , e);
                                            }
                                        }
                                    }
                                }else{
                                    try{
                                        IMSUsrRoleTargetEntity imsTargetEnt = new IMSUsrRoleTargetEntity(membership, member, role, IMSUserGroup.USG_TYPE_USERGROUP);
                                        sourceDIDID = imsTargetEnt.getSourceDIDID();
                                        memberSourceDIDID = imsTargetEnt.getMemberSourceDIDID();
                                        if (blackListGroup.contains(memberSourceDIDID) || blackListPerson.contains(memberSourceDIDID)){
                                            throw new cwException( LabelContent.get(wbProfile.cur_lan, "label_core_training_management_424") + " " + memberSourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                        }
                                        save_code = imsTargetEnt.save(con, wbProfile);
                                        success_entity++;
                                        con.commit();
                                        if (save_code == IMSUtils.SAVE_CODE_UPDATE){
                                            str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_413") + " " + memberSourceDIDID + " - " + role.getRoletype() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_441");
                                        }else if (save_code == IMSUtils.SAVE_CODE_INSERT){
                                            str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_413") + " " + memberSourceDIDID + " - " + role.getRoletype() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_442");
                                        }else {
                                            str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_413") + " " + memberSourceDIDID + " - " + role.getRoletype() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_443");
                                        }
                                        if (!sourceDIDID.equalsIgnoreCase("ROOT"))
                                            str_log += " IN GROUP " + sourceDIDID;

                                        str_log += ".";
                                        IMSUtils.writeLog(IMSUtils.SUCCESS_FILE,  str_log);
                                    }catch (Exception e){
                                        bRoleSuccess = false;
                                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_409") + " " + memberSourceDIDID + " - " + role.getRoletype() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_410") + " " + sourceDIDID,  e);
                                        unsuccess_entity++;
                                        try{
                                            con.rollback();
                                        }catch (SQLException sqle){
                                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_411") + " " + memberSourceDIDID + " - " + role.getRoletype() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_410") + " " + sourceDIDID, e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch(cwException e){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_412") + "：" + e);
                }
            }
            if (bGroupMemberSuccess){
                cleanWorkList.vtCleanUpGroupMemberType.addElement(DbEntityRelation.ERN_TYPE_USG_PARENT_USG);
                cleanWorkList.vtCleanUpGroupMemberType.addElement(DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            }
            cleanWorkList.cleanUpUsrRoleTargetEntity=bRoleSuccess;
        }else if (type.equalsIgnoreCase(TYPE_USER_COURSE) || type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT)|| type.equalsIgnoreCase(TYPE_RESULT)) {
            String sourceDIDID="";
            String memberSourceDIDID="";
            boolean bItemAccessSuccess = true;
            boolean bApplicationSuccess = true;

            for (Iterator i = _enterprise.getMembership().iterator(); i.hasNext(); ) {
                MembershipType membership = (MembershipType)i.next();
                List member_list = membership.getMember();
                for (Iterator j = member_list.iterator(); j.hasNext(); ){
                    MemberType member = (MemberType)j.next();
                    List role_list = member.getRole();
                    for (Iterator k = role_list.iterator(); k.hasNext();){
                        RoleType role = (RoleType)k.next();
                        if (role.getRoletype().equals(IMSApplication.ROLE_TYPE_LEARNER_CODE)
                            || role.getRoletype().equalsIgnoreCase(IMSApplication.ROLE_TYPE_LEARNER))
                        {
                            try {
                                IMSApplication imsApp = new IMSApplication(membership, member, role, actionRule);
                                sourceDIDID = imsApp.getSourceDIDID();
                                memberSourceDIDID = imsApp.getMemberSourceDIDID();
                                if (blackListGroup.contains(sourceDIDID)) {
                                    throw new cwException( LabelContent.get(wbProfile.cur_lan, "label_core_training_management_422") + " " + sourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                }else if (blackListGroup.contains(memberSourceDIDID) || blackListPerson.contains(memberSourceDIDID)){
                                    throw new cwException( LabelContent.get(wbProfile.cur_lan, "label_core_training_management_424") + " " + memberSourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                }
                                save_code = imsApp.save(con, wbProfile, from);
                                success_entity++;
                                con.commit();
                                String[] title = ViewItemRelation.getItemRelationTitle(con, wbProfile.root_ent_id, sourceDIDID);
                                
                                str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_414") + " " + memberSourceDIDID + " - " + title[0];
                                if (title[1] != null){
                                    str_log += "(" + title[1] + ")";
                                }
                                if (save_code == IMSUtils.SAVE_CODE_UPDATE){
                                    str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_441");
                                }else if (save_code == IMSUtils.SAVE_CODE_INSERT){
                                    str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_442");
                                }else{
                                    str_log += " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_443");
                                }
                                IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, str_log);

                            }catch (Exception e){
                                bApplicationSuccess = false;
                                String[] title = ViewItemRelation.getItemRelationTitle(con, wbProfile.root_ent_id, sourceDIDID);
                                str_log = LabelContent.get(wbProfile.cur_lan, "label_core_training_management_415") + " " + memberSourceDIDID + " - " + title[0];
                                if (title[1] != null){
                                    str_log += "(" + title[1] + ")";
                                }
                                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, str_log, e);
                                try{
                                    unsuccess_entity++;
                                    con.rollback();
                                }catch (SQLException sqle){
                                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_416")+ "：" + str_log, e);
                                }
                            }
                        }else{
                            try {
                                IMSItemAccess imsItmAccess = new IMSItemAccess(membership, member, role);
                                sourceDIDID = imsItmAccess.getSourceDIDID();
                                memberSourceDIDID = imsItmAccess.getMemberSourceDIDID();
                                if (blackListGroup.contains(sourceDIDID)) {
                                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_422") + " " + sourceDIDID + " HAS BEEN FAILURE IN IMPORT." + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                }else if (blackListGroup.contains(memberSourceDIDID) || blackListPerson.contains(memberSourceDIDID)){
                                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_424") + " " + memberSourceDIDID + " HAS BEEN FAILURE IN IMPORT." + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_423"));
                                }
                                imsItmAccess.save(con, wbProfile);
                                success_entity++;
                                con.commit();
                                IMSUtils.writeLog(IMSUtils.SUCCESS_FILE, sourceDIDID+ "&" + memberSourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_417"));
                            }catch (Exception e){
                                bItemAccessSuccess = false;
                                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, sourceDIDID + "&" + memberSourceDIDID + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_418") , e);
                                unsuccess_entity++;
                                try{
                                    con.rollback();
                                }catch (SQLException sqle){
                                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, LabelContent.get(wbProfile.cur_lan, "label_core_training_management_419") + "：" + sourceDIDID + "&" + memberSourceDIDID , e);
                                }
                            }
                        }
                    }
                }
            }
            cleanWorkList.cleanUpItemAccess = bItemAccessSuccess;
            cleanWorkList.cleanUpApplication = bApplicationSuccess;

        }else if (type.equalsIgnoreCase(TYPE_COURSE)){
            // nothing to do
        }else {
            throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_421") + "：" + type);
        }
        h_status_count.put(STATUS_SUCCESS, new Integer(success_entity));
        h_status_count.put(STATUS_UNSUCCESS, new Integer(unsuccess_entity));
        return h_status_count;

    }

    /**
    clean up not-syn record, cleanup process depends on what have been imported.
    @param Connection
    @param CwIniFile object
    @param loginProfile object
    @param siteId
     * @throws SQLException 
    */
    public void cleanUp(Connection con, cwIniFile ini, loginProfile wbProfile, long siteId) throws SQLException{
        IMSCleanUp imsCleanUp = new IMSCleanUp(synTimestamp);
        Timestamp deleteTime = cwSQL.getTime(con);
        if (cleanWorkList.cleanUpPerson){
            try{
                imsCleanUp.cleanUpPerson(con, siteId, wbProfile, deleteTime);
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanPerson:" , e);
            }
        }
        for (int i=0; i<cleanWorkList.vtCleanUpGroupMemberType.size(); i++){
            try{
                imsCleanUp.cleanUpEntityRelation(con, siteId, (String)cleanWorkList.vtCleanUpGroupMemberType.elementAt(i), wbProfile.usr_id, deleteTime);
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanEntiryRelation:" , e);
            }
        }
        if (cleanWorkList.cleanUpUsergroup){
            try{
                imsCleanUp.cleanUpUsergroup(con, siteId, wbProfile);
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanUpUsergroup:" , e);
            }
        }
        if (cleanWorkList.cleanUpItem){
            String action_on_absent_item = ini.getValue("ACTION_ON_ABSENT_ITEM");
            try{
                imsCleanUp.cleanUpItem(con, wbProfile, cwUtils.splitToString(ini.getValue("IMPORT_ITEM_TYPE_LIST"), "~"), action_on_absent_item);
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanUpItem:" , e);
            }
        }
        if (cleanWorkList.cleanUpItemAccess){
            try{
                imsCleanUp.cleanUpItemAccess(con, wbProfile, cwUtils.splitToString(ini.getValue("IMPORT_ITEM_TYPE_LIST"), "~"));
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanUpItemAccess:" , e);
            }
        }
        if (cleanWorkList.cleanUpApplication){
            try{
                imsCleanUp.cleanUpApplication(con, wbProfile, cwUtils.splitToString(ini.getValue("IMPORT_ITEM_TYPE_LIST"), "~"));
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanUpApplication:" , e);
            }
        }
//        if (cleanWorkList.cleanUpCatalog){
//            try{
//                imsCleanUp.cleanUpCatalog(con, siteId);
//            }catch(Exception e){
//                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanUpCatalog:" , e);
//            }
//        }
        if (cleanWorkList.cleanUpUsrRoleTargetEntity){
            try{
                imsCleanUp.cleanUpUsrRoleTargetEntity(con, siteId, cwUtils.splitToString(ini.getValue("IMPORT_ROLE_LIST"), "~"));
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN cleanUpUsrRole:" , e);
            }
            try{
                imsCleanUp.attachPersonDefaultRole(con, siteId);
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN attachPersonToDefaultRole:" , e);
            }
        }

        if (cleanWorkList.cleanUpPerson){
            try{
                imsCleanUp.attachPerson(con, siteId, wbProfile);
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN attachPerson:" , e);
            }
//            try{
//                imsCleanUp.attachUserGrade(con, siteId, wbProfile);
//            }catch(Exception e){
//                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN attachUserGrade:" , e);
//            }
        }
        if (cleanWorkList.cleanUpUsergroup){
            try{
                imsCleanUp.attachUsergroup(con, siteId, wbProfile);
            }catch(Exception e){
                CommonLog.error(e.getMessage(),e);
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN attachUsergroup:" , e);
            }
        }

        if( cleanWorkList.cleanUpTreeNode ) {
            try{
                imsCleanUp.cleanUpTreeNode(con, siteId, this.blackListGroup);
            }catch(Exception e){
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ERROR IN clean tree node:" , e);
            }
        }
    }
    /*
    * Set properties of the enterprise<br>
    * Get all specified type of items and its enroled user<br>
    * Appending the record into the _Membership object
    * @param cwIni iniFile object of the config file
    * @param startTime If startTime is specified,
    *        only the learner attended to the course
    *        after the specified time will be added to the result
    * @param endTime If endTime is specified,
    *        only the learner attended to the course
    *        before the specified time will be added to the result        */

    public void get(Connection con, loginProfile wbProfile, cwIniFile cwIni, Timestamp startTime, Timestamp endTime, String type) 
        throws SQLException, cwException , qdbException, JAXBException{
            
            if (type.equalsIgnoreCase(TYPE_USER_GROUP)){
                getUserGroup(con, wbProfile, cwIni, startTime, endTime);
            }else{
                getCourseResult(con, wbProfile, cwIni, startTime, endTime, type);
            }
            return;
        }
        
    private void getUserGroup(Connection con, loginProfile wbProfile, cwIniFile cwIni, Timestamp startTime, Timestamp endTime) throws SQLException, cwException , qdbException, JAXBException{
        ObjectFactory objFactory = new ObjectFactory();

        PropertiesType properties = objFactory.createPropertiesType();
        properties.setDatasource(IMSEnterpriseApp.exportSource);
        properties.setDatetime(IMSUtils.convertTimestampToISO8601(cwSQL.getTime(con)));
        _enterprise.setProperties(properties);
        List l_person = _enterprise.getPerson();
        List l_group = _enterprise.getGroup();
        
        Vector vtEntId = dbRegUser.getUpdatedUser(con, wbProfile.root_ent_id, startTime, endTime, cwIni.getValue("DATA_SOURCE"));
        Hashtable htGroup = new Hashtable(); // usg_ent_id as key, group as content
        Hashtable htMembership = new Hashtable();   // parent sourcedid as key
        
        Hashtable htUserGrade = dbRegUser.getGradeByUsrEntIds(con, vtEntId);

        for (int i=0; i<vtEntId.size(); i++){
            long usrEntId = ((Long) vtEntId.elementAt(i)).longValue();
            IMSPerson imsperson = new IMSPerson();
            PersonType person = imsperson.createPerson(con, usrEntId, htUserGrade);
            l_person.add(person);
            
            
            dbUserGroup parentGroup = DbEntityRelation.getParentUserGroup(con, usrEntId);
            if (parentGroup!=null){
//                System.out.println("from usr: " + parentGroup.usg_display_bil + ", " + parentGroup.usg_name + ", "+ parentGroup.usg_desc);
                IMSGroupMember imsGpm = new IMSGroupMember();
                IMSUserGroup imsUserGroup = new IMSUserGroup();
                imsGpm.setGroupMemberMembership(wbProfile.root_ent_id, parentGroup, imsperson.getSourceDIDID(), "1", htMembership);
                imsUserGroup.addGroupNItsParentGroup(con, wbProfile.root_ent_id, parentGroup, htGroup, htMembership);
            }else{
            	CommonLog.info("usr no group: " + usrEntId);
            }
        }
        // add usr role
        IMSUsrRoleTargetEntity imsUsrRoleTargetEntity = new IMSUsrRoleTargetEntity();
        imsUsrRoleTargetEntity.setRoleMembership(con, wbProfile.root_ent_id, vtEntId, htMembership);

        // add user supervised group
        IMSSuperviseTargetEntity imsSuperviseTargetEntity = new IMSSuperviseTargetEntity();
        imsSuperviseTargetEntity.setDirectSupverisorMembership(con, wbProfile.root_ent_id, vtEntId, htGroup, htMembership);
        imsSuperviseTargetEntity.setSupverisedGroupMembership(con, wbProfile.root_ent_id, vtEntId, htGroup, htMembership);
        
        // add user appn approval group
        IMSUsrAppnApprovalGroup imsUsrAppnApprovalGroup = new IMSUsrAppnApprovalGroup();
        imsUsrAppnApprovalGroup.setAppApprovalGroupMembership(con, vtEntId, htMembership);
        
        Vector vtUserGroup = dbUserGroup.getUpdatedUserGroup(con, wbProfile.root_ent_id, startTime, endTime);
        for (int i=0; i<vtUserGroup.size(); i++){
            dbUserGroup usg = (dbUserGroup)vtUserGroup.elementAt(i);
//            System.out.println("from updated group: " + usg.usg_display_bil + ", " + usg.usg_name + ", " + usg.usg_desc);

            IMSUserGroup imsUserGroup = new IMSUserGroup();
            imsUserGroup.addGroupNItsParentGroup(con, wbProfile.root_ent_id, (dbUserGroup)vtUserGroup.elementAt(i), htGroup, htMembership);
        }
        
        List _Group = _enterprise.getGroup();
        Enumeration e_group = htGroup.elements();
        while(e_group.hasMoreElements()) {
            GroupType group = (GroupType) e_group.nextElement();                       
            _Group.add(group);
        }
        
        List _Membership = _enterprise.getMembership();
        Enumeration e_membership = htMembership.elements();
        while(e_membership.hasMoreElements()) {
            MembershipType membership = (MembershipType) e_membership.nextElement();                       
            _Membership.add(membership);
        }
    }
    
    private void getCourseResult(Connection con, loginProfile wbProfile, cwIniFile cwIni, Timestamp startTime, Timestamp endTime, String type) throws SQLException, cwException , JAXBException{
        ObjectFactory objFactory = new ObjectFactory();

        PropertiesType properties = objFactory.createPropertiesType();
        properties.setDatasource(cwIni.getValue("DATA_SOURCE"));
        properties.setDatetime(IMSUtils.convertTimestampToISO8601(cwSQL.getTime(con)));
        _enterprise.setProperties(properties);

        String[] item_type = null;
        String val = cwIni.getValue("EXPORT_ITEM_TYPE");
        if( val != null && val.length() > 0 ) {
            if( val.equalsIgnoreCase("ALL") ) {
                long site_id = Long.parseLong( cwIni.getValue("SITEID") );
                item_type = IMSTreeNode.getAllWbItemType(con, site_id);
            }else
                item_type = cwUtils.splitToString(val, "~");
        }         
        Vector vtExportItmLst = new Vector();
        Vector vtItmLst = getAllItem(con, item_type);
        // itm_id as key
        Hashtable htMembership = new Hashtable();
                
        if (type.equalsIgnoreCase(TYPE_COURSE) || type.equalsIgnoreCase(TYPE_COURSE_RESULT) || type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT)){
            for (int i=0; i<item_type.length; i++){
                vtExportItmLst.addAll(aeItem.getUpdatedItem(con, item_type[i], wbProfile.root_ent_id, startTime, endTime));
            }
        }
                
        if (type.equalsIgnoreCase(TYPE_RESULT) || type.equalsIgnoreCase(TYPE_COURSE_RESULT) || type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT) ){
            IMSLearningResult.setExportEnrollmentStatusList(cwIni.getValue("EXPORT_ENROLLMENT_STATUS_LIST"));
            IMSLearningResult.setNotAttemptedComment(cwIni.getValue("NOT_ATTEMPTED_COMMENT"));
            IMSLearningResult.setIncompleteComment(cwIni.getValue("INCOMPLETED_COMMENT"));
            IMSLearningResult.setCompletedComment(cwIni.getValue("COMPLETED_COMMENT"));
            IMSLearningResult.setPassedComment(cwIni.getValue("PASSED_COMMENT"));
            IMSLearningResult.setFailedComment(cwIni.getValue("FAILED_COMMENT"));
            IMSLearningResult.setWithdrawComment(cwIni.getValue("WITHDRAW_COMMENT"));
            boolean bExportEnrollmentOnly = false;
            if (type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT)){
                bExportEnrollmentOnly = true;
            }
            for (int i=0; i<vtItmLst.size(); i++){
                aeItem aeItm = (aeItem)vtItmLst.elementAt(i);
                IMSLearningResult imsLearningResult = new IMSLearningResult();
                imsLearningResult.itm_id = aeItm.itm_id;
                imsLearningResult.cos_id = aeItm.itm_code;
                        
                MembershipType resultMbship = imsLearningResult.get(con, startTime, endTime, bExportEnrollmentOnly);
                if (resultMbship != null){
                    Long itmId = new Long(aeItm.itm_id);
                    MembershipType membership = (MembershipType)htMembership.get(new Long(aeItm.itm_id));
                    if (membership==null){
                        membership = resultMbship;
                    }else{
                        List l_member = membership.getMember();
                        l_member.addAll(resultMbship.getMember());
                    }
                    if (!vtExportItmLst.contains(itmId)){
                        vtExportItmLst.add(itmId);
                    }
                    htMembership.put(itmId, membership);
                }
            }
        }
        Vector vtUpdateItemFrCat = aeTreeNode.getUpdatedItemTreeNode(con, startTime, endTime, item_type);
        for (int i=0; i<vtUpdateItemFrCat.size(); i++){
            if (!vtExportItmLst.contains((Long)vtUpdateItemFrCat.elementAt(i))){
                vtExportItmLst.add((Long)vtUpdateItemFrCat.elementAt(i));
            }
        }
        // export group
        List _Group = _enterprise.getGroup();
        IMSItem.setExportExtension(cwIni.getValue("EXPORT_ITEM_EXTENSION"));
        if (type.equalsIgnoreCase(TYPE_COURSE) || type.equalsIgnoreCase(TYPE_COURSE_RESULT) || type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT)){
            for (int i=0; i<vtExportItmLst.size(); i++){
    //                aeItem itm = (aeItem)vtItmLst.elementAt(i);
                long itm_id = ((Long)vtExportItmLst.elementAt(i)).longValue();
                try{
                    try{
                        GroupType group = getItem(con, itm_id);
                        _Group.add(group);
                    }catch(cwSysMessage syse){
                        throw new cwException(syse.getSystemMessage(wbProfile.label_lan));    
                    }
                }catch(Exception e){
                        // todotodotodotodo
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "FAILURE IN GETTING ITEM, ID:" + itm_id, e);
                }
            }
        }
        // export catalog
        if (type.equalsIgnoreCase(TYPE_COURSE) || type.equalsIgnoreCase(TYPE_COURSE_RESULT) || type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT)){
            Hashtable htTreeNode = aeTreeNode.getUpdatedTreeNode(con, startTime, endTime, vtExportItmLst);
            Enumeration e_treenode = htTreeNode.keys();
                    
            while(e_treenode.hasMoreElements()) {
                Long tndId = (Long) e_treenode.nextElement();
                try{
                    IMSTreeNode imstreenode = new IMSTreeNode(tndId.longValue(), (Vector) htTreeNode.get(tndId));
                    GroupType tndGroup = imstreenode.get(con);
                    _Group.add(tndGroup);
                }catch(Exception e){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "FAILURE IN GETTING TREE NODE, ID:" + tndId, e);
                }
            }
        }
        // export role in item
        if (type.equalsIgnoreCase(TYPE_COURSE) || type.equalsIgnoreCase(TYPE_COURSE_RESULT) || type.equalsIgnoreCase(TYPE_COURSE_ENROLLMENT)){
            for (int i=0; i<vtExportItmLst.size(); i++){
                long itm_id = ((Long)vtExportItmLst.elementAt(i)).longValue();
                try{
                    IMSItemAccess imsItemAccsss = new IMSItemAccess();
                    imsItemAccsss.setWbItemId(itm_id);
                    MembershipType accessMbship = imsItemAccsss.get(con, wbProfile.root_ent_id);
                    if (accessMbship != null){
                        Long itmId = new Long(itm_id);
                        MembershipType membership = (MembershipType)htMembership.get(itmId);
                        if (membership==null){
                            membership = accessMbship;
                        }else{
                            List l_member = membership.getMember();
                            l_member.addAll(accessMbship.getMember());
                        }
                        htMembership.put(itmId, membership);
                    }
                }catch(Exception e){
                        // todotodotodotodo
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "FAILURE IN GETTING ITEM, ID:" + itm_id, e);
                }
            }
        }         
        List _Membership = _enterprise.getMembership();
        Enumeration e_membership = htMembership.elements();
        while(e_membership.hasMoreElements()) {
            MembershipType membership = (MembershipType) e_membership.nextElement();                       
            _Membership.add(membership);
        }
        return;
    }
    
    private GroupType getItem(Connection con, long itm_id) 
        throws SQLException, cwException, cwSysMessage , JAXBException{
            IMSItem imsItem = new IMSItem();
            imsItem.setWbItemId(itm_id);
            return imsItem.get(con);
        }

    /*
    * Set result comment for different type of status<br>
    * Get all specified type of items and its enroled user<br>
    * Appending the record into the _Membership object
    * @param vtItmLst vector of aeItem object which contain itm_id and itm_code
    * @param cwIni iniFile object of the config file
    * @param startTime If startTime is specified,
    *        only the learner attended to the course
    *        after the specified time will be added to the result
    * @param endTime If endTime is specified,
    *        only the learner attended to the course
    *        before the specified time will be added to the result
    */
    /*
    private Membership getResult(Connection con, aeItem aeItm, Timestamp startTime, Timestamp endTime) 
        throws SQLException, cwException {
            return imsLearningResult;
        }
        */

    /**
    * Get all items by specified item type<br>
    * Get all type of items if item_type is null
    * @param item_type storing the item type to be get in stirng array
    * @return The vector of aeItem object which contain itm_id and itm_code
    */
    private Vector getAllItem(Connection con, String[] item_type) throws SQLException{
        aeItem aeItm = new aeItem();
        return aeItm.getItemListByType(con, item_type);
    }

    /**
    send email to list of recipient , attach the log and status
    @param vector recipient email address
    */
    public void sendNotify(cwIniFile ini, String action, String[] recipientList, String template){
        qdbMailSender sender = new qdbMailSender(ini.getValue("MAILSERVER"));
        if(template == null || "".equals(template)) {
        	template = ini.getValue("MAIL_NOTIFY_INTEGRATION_TPL");  
        }
        String[] contents = new String[2];
        StringBuffer message = new StringBuffer(1024);
        String messageNEWL = "<BR>"+ cwUtils.NEWL;        
        if (action.equalsIgnoreCase(ACTION_IMPORT)){
            if (cleanWorkList.cleanUpPerson)        
                message.append("Person(s) is imported successfully.").append(messageNEWL);            
            if (cleanWorkList.cleanUpUsergroup)    
                message.append("Usergroup(s) is imported successfully.").append(messageNEWL);            
            if (cleanWorkList.vtCleanUpGroupMemberType.contains(DbEntityRelation.ERN_TYPE_USG_PARENT_USG))
                message.append("Usergroup to Usergroup relationship(s) is imported successfully.").append(messageNEWL);            
            if (cleanWorkList.vtCleanUpGroupMemberType.contains(DbEntityRelation.ERN_TYPE_USR_PARENT_USG))
                message.append("User to Usergroup relationship(s) imported successfully.").append(messageNEWL);            
            if (cleanWorkList.cleanUpUsrRoleTargetEntity)    
                message.append("User Role(s) is imported successfully.").append(messageNEWL);            
            if (cleanWorkList.cleanUpItem)    
                message.append("Course(s) is imported successfully.").append(messageNEWL);            
            if (cleanWorkList.cleanUpItemAccess)    
                message.append("Course Administrative Role(s) is imported successfully.").append(messageNEWL);            
            if (cleanWorkList.cleanUpApplication)    
                message.append("Enrollment(s) is imported successfully.").append(messageNEWL);            
    //        if (cleanWorkList.cleanUpCatalog)    
    //            message.append("Catalog(s) is imported successfully.").append(messageNEWL);            
            if (cleanWorkList.cleanUpTreeNode)    
                message.append("Catalog to course relation(s) is imported successfully.").append(messageNEWL);            
        }else{
            message.append("Data are exported successfully.").append(messageNEWL);            
        }        
        if (IMSUtils.isErrorOccur()){
            message.append("Error Occur in the integration. Please check the attached log file.").append(messageNEWL);            
        }
        
/*
        String filePath = folderName + File.separator + CheckNReplace.ERROR_FILE;
        File log = new File(filePath);
        if (log.exists()){ 
            status = "TERMINATED";    
            attchStr.append(getAttchMIMEHeader(ini, CheckNReplace.ERROR_FILE));
            attchStr.append(readFile(filePath));
        }

        if (status == null){
            if (fromStatus){
                status = "SUCCESS";    
            }else{
                status = "FAILURE";    
            }
        }

        CheckNReplace check = new CheckNReplace(folderName, separateLog, null);

        filePath = folderName + File.separator + check.SUCCESS_LOG;
        log = new File(filePath);
        if (log.exists()){
            attchStr.append(getAttchMIMEHeader(ini, check.SUCCESS_LOG));
            attchStr.append(readFile(filePath));
        }
        
        if (!check.FAILURE_LOG.equals(check.SUCCESS_LOG)){
            filePath = folderName + File.separator + check.FAILURE_LOG;
            log = new File(filePath);
            if (log.exists()){
                attchStr.append(getAttchMIMEHeader(ini, check.FAILURE_LOG));
                attchStr.append(readFile(filePath));
            }
        }

        filePath = folderName + File.separator + CheckNReplace.CLEAN_LOG;
        log = new File(filePath);
        if (log.exists()){
            if ("SUCCESS".equals(status)){
                status = status ;    
            }
            attchStr.append(getAttchMIMEHeader(ini, CheckNReplace.CLEAN_LOG));
            attchStr.append(readFile(filePath));
        }
        */
        contents[0] = message.toString();
        contents[1] = IMSUtils.getAttachCode(ini);
        for (int i=0; i<recipientList.length; i++){
            String recipient = recipientList[i];
            try{
                sender.sendWithTemplate(recipient, ini.getValue("CW_ADMIN_EMAIL"), template, contents);
            }catch (Exception e){
                System.err.println("ERROR IN SEND NOTIFICATION TO:" + recipient);    
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE,  e);
            }
        }
        return;
    }
    
}
