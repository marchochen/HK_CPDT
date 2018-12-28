package com.cw.wizbank.enterprise;

import java.lang.Long;
import java.sql.*;
import java.util.List;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.util.*;
import com.cw.wizbank.entity.IndustryCode;
import com.cw.wizbank.db.DbEntity;
import com.cw.wizbank.db.DbEntityRelation;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.DbUserClassification;
import com.cw.wizbank.db.view.ViewEntityRelation;

import com.cwn.wizbank.utils.CommonLog;

/*
import org.imsglobal.enterprise.Person;
import org.imsglobal.enterprise.Tel;
import org.imsglobal.enterprise.N;
import org.imsglobal.enterprise.Name;
import org.imsglobal.enterprise.Systemrole;
import org.imsglobal.enterprise.Partname;
import org.imsglobal.enterprise.Extension;
*/
import org.imsglobal.enterprise.*;

public class IMSPerson
{
//    private static final String SYS_ROLE_DELIMITER = ",";
//    private static final String SYS_ROLE_MAP_DELIMITER = ":";

    private static final String PARTNAME_TYPE_INITIAL = "Initials";
    private static final String TEL_TYPE_FAX_CODE = "2";
    private static final String TEL_TYPE_FAX = "Fax";
    private static final String USERID_TYPE_ADDITIONALID = "AdditionalId";
    private static Hashtable htUserAttributeRoot = null;
    
//    private static Long DEFAULT_GRADE_ENT_ID = null;
//    private static final Hashtable SYSTEM_ROLE_MAP = new Hashtable();
//    private static String DEFAULT_SYS_ROLE = null;
    private PersonType _person;
    private long wbEntId;
    private String sourceDIDID;
    private String usr_source = null;
    
//    private List _Sourcedid;
//    private List _Userid;
//    private List _Tel;
//    private List _Institutionrole;
    
    /**
    initialize variable DEFAULT_SYS_ROLE by a SYSTEM ROLE mapping string
    @param con
    @param site id
    @param SYSTEM ROLE mapping string
    */
    /*
    public static void initRole(Connection con, long siteId, String sysRoleMap) throws cwException, SQLException{
        // set default role
        DEFAULT_SYS_ROLE = AccessControlWZB.getDefaultRoleBySite(con, siteId);
        // set Role map
        setSystemRoleMap(sysRoleMap);
    }
    */
    public IMSPerson() throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new ObjectFactory();
        // create an empty group
        _person = objFactory.createPerson();
    }
    
    public PersonType getPerson(){
        return _person; 
    }
    
    public IMSPerson(PersonType person){
        _person = person;
//        setRecstatus(person.getRecstatus());
//        setComments(person.getComments());
//        _Sourcedid = person.getSourcedid();
//        _Userid = person.getUserid();
//        setName(person.getName());
//        setDemographics(person.getDemographics());
//        setEmail(person.getEmail());
//        setUrl(person.getUrl());
//        _Tel = person.getTel();
//        setAdr(person.getAdr());
//        setPhoto(person.getPhoto());
//        setSystemrole(person.getSystemrole());
//        _Institutionrole = person.getInstitutionrole();
//        setDatasource(person.getDatasource());
//        setExtension(person.getExtension());
    }
    /** 
    get sourceDIDID
    set sourceDIDID if sourceDIDID not yet set
    @return sourceDIDID
    */
    public String getSourceDIDID() {
        if (this.sourceDIDID==null){
            this.sourceDIDID = ((SourcedidType) _person.getSourcedid().iterator().next()).getId();
        }
        return this.sourceDIDID;
    }

    public String getUsrSource() {
        if (this.usr_source==null){
            this.usr_source = ((SourcedidType) _person.getSourcedid().iterator().next()).getSource();
        }
        return this.usr_source;
    }

    /**
    save data from the IMSPerson to db
    */
    public int save(Connection con, long siteId, Long defaultGradeId, String initialUserStatus, loginProfile wbProfile, String ins_usr_msg_title, boolean encrytePwd) throws cwException{
        try{
            int save_code; 
            init(con, siteId);
                
            dbRegUser dbusr = new dbRegUser();
            if (wbEntId>0){
                dbusr.ent_id = wbEntId;
                dbusr.usr_ent_id = wbEntId;
                dbusr.get(con);
                AccessControlWZB acl = new AccessControlWZB();
//                dbusr.usr_roles = acl.getUserRoles(con, wbEntId);

                if (dbusr.usr_status.equals(dbRegUser.USR_STATUS_DELETED)){
                    recover(con, dbusr, siteId, initialUserStatus);
                }
		//If the we need to have encryption but the user already has a password, 
		//just keep the old password and do not need encryption
                encrytePwd = encrytePwd && (dbusr.usr_pwd == null);
                
                setField(con, dbusr, siteId, wbProfile.usr_id);
                dbusr.upd_usr_id = wbProfile.usr_id;
                if (this.getUsrSource() != null) {
                    dbusr.usr_source = getUsrSource();
                }else {
                    dbusr.usr_source = IMSEnterpriseApp.importSource;
                }
                dbusr.upd(con, dbusr.upd_usr_id, encrytePwd);
                save_code = IMSUtils.SAVE_CODE_UPDATE;
            }else{
                setField(con, dbusr, siteId, wbProfile.usr_id);
                dbusr.usr_status = initialUserStatus;
                dbusr.ent_syn_ind = true;
                dbusr.usr_syn_rol_ind = true;
                dbusr.upd_usr_id = wbProfile.usr_id;
                if (this.getUsrSource() != null) {
                    dbusr.usr_source = getUsrSource();
                }else {
                    dbusr.usr_source = IMSEnterpriseApp.importSource;
                }
                dbusr.ins(con, siteId, wbProfile.usr_id, ins_usr_msg_title, encrytePwd);
                dbusr.saveEntityRelation(con, dbusr.usr_attribute_ent_ids, dbusr.usr_attribute_relation_types, false, wbProfile.usr_id);
                this.wbEntId = dbusr.usr_ent_id;
                save_code = IMSUtils.SAVE_CODE_INSERT;
            }
            
            if (defaultGradeId!=null){
                checkUserGrade(con, this.wbEntId, defaultGradeId.longValue(), wbProfile.usr_id);
            }
            
            DbEntity dbEnt = new DbEntity();
            dbEnt.ent_id = this.wbEntId;
            dbEnt.updSynDate(con);
            return save_code;
        }catch (Exception e){
            CommonLog.error(e.getMessage(),e);
            throw new cwException("Error in " + sourceDIDID + " " + e.getMessage());
        }
    }        
    
    /**
    initialise the User Entity ID by using the sourceId
    @param CONNECTION
    @param site id
    @param usr_source
    */
    private void init(Connection con, long siteId) throws qdbException{
        dbRegUser dbusr = new dbRegUser();
        //get the user whose stuats is ok first.if not exist,then get the user who with same usr_id but diffent status.
        long temp_usr_ent_id = dbusr.getEntIdStatusOk(con, getSourceDIDID(), siteId);
        if (temp_usr_ent_id <= 0) {
        	temp_usr_ent_id = dbusr.getEntId(con, getSourceDIDID(), siteId);
        }
        this.wbEntId = temp_usr_ent_id;
    }

    /**
    if IMSPerson have grade tag
    set userGrade in dbRegUser object 
    insert the usergrade if a grade is not exist
    */
    private void setUserAttribute(Connection con, dbRegUser usr, long siteId, String usr_id) throws qdbException, qdbErrMessage, cwException, cwSysMessage, SQLException{
        String tmpStr;
        ExtensionType extension = (org.imsglobal.enterprise.ExtensionType)_person.getExtension();
        if (extension!=null){
            org.imsglobal.enterprise.cwn.ExtensionType cwnExtension = (org.imsglobal.enterprise.cwn.ExtensionType)extension.getExtension();
            if (cwnExtension!=null){
                Vector vtUsrAttrIds = new Vector();
                Vector vtUsrAttrRelation = new Vector();
                org.imsglobal.enterprise.cwn.ExtensionType.ClassificationListType ucf_list = cwnExtension.getClassificationList();
                org.imsglobal.enterprise.cwn.ExtensionType.GradeType grade = cwnExtension.getGrade();
                List posList = cwnExtension.getPosition();
                if (htUserAttributeRoot == null && (grade!=null || (posList!=null && posList.size() > 0) || ucf_list !=null )){
                    htUserAttributeRoot = new Hashtable();
                    dbRegUser.getAllUserAttributeInOrg(con, siteId, htUserAttributeRoot);
                }
                if (grade!=null && (usr.usr_not_syn_gpm_type == null || usr.usr_not_syn_gpm_type.indexOf(DbEntity.ENT_TYPE_USER_GRADE) < 0 )){
                    String gradeSourceId = grade.getId();
                    if (gradeSourceId!=null && gradeSourceId.length() != 0){
                        // ins or upd Grade 
                        DbUserGrade dbGrade = new DbUserGrade();
                        dbGrade.ent_ste_uid        = gradeSourceId;
                        dbGrade.ugr_ent_id_root   = siteId;
                        if("Unspecified".equals(dbGrade.ent_ste_uid)){
                        	dbGrade = dbGrade.getDefaultGrade(con, siteId);
                        }else{
                        	dbGrade.getBySteUid(con);
                        }
                        if (dbGrade.ugr_ent_id>0){             
                            dbGrade.ent_upd_date = dbEntity.getUpdDate(con, dbGrade.ugr_ent_id);
                            tmpStr= grade.getValue();
                            if (!dbGrade.ugr_display_bil.equals(tmpStr)){
                                dbGrade.ugr_display_bil = tmpStr;
                                dbGrade.updDesc(con, usr_id);
                            }
                        }else{
                            dbGrade.ugr_display_bil = grade.getValue();
                            dbGrade.ins(con);
                            ViewEntityRelation view = new ViewEntityRelation();
                            view.groupEntId = ((Long)htUserAttributeRoot.get(DbEntity.ENT_TYPE_USER_GRADE)).longValue();
                            view.memberEntId = dbGrade.ugr_ent_id;
                            view.relationType = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
                            view.insEntityRelation(con, usr_id);
                        }
                        vtUsrAttrIds.addElement(new Long(dbGrade.ugr_ent_id));
                        vtUsrAttrRelation.addElement(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
                    }
                }
                
                if ((posList!=null && posList.size() > 0) && (usr.usr_not_syn_gpm_type == null || usr.usr_not_syn_gpm_type.indexOf(DbEntity.ENT_TYPE_INDUSTRY_CODE) < 0)){
                    for (Iterator i = posList.iterator(); i.hasNext();) {
                        org.imsglobal.enterprise.cwn.ExtensionType.PositionType position =   
                            (org.imsglobal.enterprise.cwn.ExtensionType.PositionType) i.next();

                        String positionSourceId = position.getId();
                        if (positionSourceId!=null && positionSourceId.length() != 0){
                            // ins or upd Industry Code
                            IndustryCode industry = new IndustryCode();
                            industry.ent_ste_uid        = positionSourceId;
                            industry.rootEntId   = siteId;
                            industry.getBySteUid(con);
                            if (industry.ent_id>0){
                                tmpStr= position.getValue();
//                                if (!industry.name.equals(tmpStr)){ 
//                                    DbIndustryCode dbIndCode = new DbIndustryCode();
                                    //update Industry Code
//                                    dbIndCode.idc_ent_id = industry.ent_id;
//                                    dbIndCode.idc_display_bil = tmpStr;
//                                    dbIndCode.upd(con, usr_id);
//                                }
                            }else{
                                industry.name = position.getValue();
                                industry.parentEntId = ((Long)htUserAttributeRoot.get(DbEntity.ENT_TYPE_INDUSTRY_CODE)).longValue();
                                industry.addIndustryCode(con, usr_id);
                            }
                            vtUsrAttrIds.addElement(new Long(industry.ent_id));
                            vtUsrAttrRelation.addElement(dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC);
                        }
                    }
                }
                
//                multiple classification
                if (ucf_list!=null){
                    List l_ucf = ucf_list.getClassification();
                    for (Iterator i = l_ucf.iterator(); i.hasNext(); ) {
                        org.imsglobal.enterprise.cwn.ExtensionType.ClassificationListType.ClassificationType ucf = (org.imsglobal.enterprise.cwn.ExtensionType.ClassificationListType.ClassificationType)i.next();
                        String ucfSourceId = ucf.getId();
                        if (ucfSourceId!=null && ucfSourceId.length() != 0){
                            String type = ucf.getType();
                            if (htUserAttributeRoot.containsKey(type)){
                                if (usr.usr_not_syn_gpm_type == null || usr.usr_not_syn_gpm_type.indexOf(type) < 0){
                                    DbUserClassification dbUcf = new DbUserClassification();
                                    dbUcf.ent_ste_uid        = ucfSourceId;
                                    dbUcf.ent_type           = type;
                                    dbUcf.ucf_ent_id_root = siteId;

                                    dbUcf.getBySteUid(con);
                                    if (dbUcf.ucf_ent_id>0){
                                        tmpStr= ucf.getValue();
                                        if (!dbUcf.ucf_display_bil.equals(tmpStr)){
                                            dbUcf.ucf_display_bil = tmpStr;
                                            dbUcf.updDesc(con, usr_id, type);
                                        }
                                    }else{
                                        dbUcf.ucf_display_bil = ucf.getValue();
                                        dbUcf.ins(con);
                                        ViewEntityRelation view = new ViewEntityRelation();
                                        view.groupEntId = ((Long)htUserAttributeRoot.get(type)).longValue();
                                        view.memberEntId = dbUcf.ucf_ent_id;
                                        view.relationType = type + dbEntityRelation.ERN_TYPE_PARENT + type;
                                        view.insEntityRelation(con, usr_id);
                                    }
                                    vtUsrAttrIds.addElement(new Long(dbUcf.ucf_ent_id));
                                    vtUsrAttrRelation.addElement(DbEntity.ENT_TYPE_USER + dbEntityRelation.ERN_TYPE_CURRENT + type);
                                }else{
                                	CommonLog.info(type + " update immuned");
                                }
                            }else{
                                throw new cwException("INVALID USER CLASSIFICATION TYPE: " + type);
                            }
                        }
                    }
                }
                if (vtUsrAttrIds.size()>0){
                    usr.usr_attribute_ent_ids = cwUtils.vec2longArray(vtUsrAttrIds);

                }
                if (vtUsrAttrRelation.size()>0){
                    usr.usr_attribute_relation_types = new String[vtUsrAttrRelation.size()];
                    for(int i=0; i<vtUsrAttrRelation.size(); i++) {
                        usr.usr_attribute_relation_types[i] = (String)vtUsrAttrRelation.elementAt(i);
                    }
                }

            }
        }
    }
    
    // set user grade if user haven't got a grade
    private void checkUserGrade(Connection con, long usr_ent_id, long defaultGradeId, String usr_id) throws SQLException{
//        Vector vtUsrLst = new Vector();
//        vtUsrLst.addElement(usr_ent_id);
//        Hashtable htGrade = DbGroupMember.getGradeMember(con, vtUsrLst); 
        long usrGradeId = DbUserGrade.getGradeEntId(con, usr_ent_id); 
        if (!(usrGradeId>0)){
            DbEntityRelation dber = new DbEntityRelation();
            dber.ern_ancestor_ent_id = defaultGradeId;
            dber.ern_child_ent_id = usr_ent_id;
            dber.ern_type = DbEntityRelation.ERN_TYPE_USR_CURRENT_UGR;
            dber.insEr(con, usr_id);
        }
    }
    
    /**
    set Data to dbRegUser object
    @param dbRegUser object to store the data
    */
	private void setField(
		Connection con,
		dbRegUser dbusr,
		long siteId,
		String usr_id)
		throws qdbException, SQLException, cwException, cwSysMessage, qdbErrMessage {
		String tmpStr;
		dbusr.usr_ste_usr_id = sourceDIDID;
		if (dbusr.usr_ste_usr_id == null
			|| dbusr.usr_ste_usr_id.length() == 0) {
			throw new cwException("User SourceDID ID must not empty.");
		}

		List l_userId = _person.getUserid();
		if (l_userId != null && l_userId.size() > 0) {
			UseridType userid = (UseridType) l_userId.iterator().next();
			tmpStr = userid.getPassword();
			// dont update pwd when the password already defined
			if (tmpStr != null && dbusr.usr_pwd == null) {
				dbusr.usr_pwd = tmpStr;
			}
		}
		NameType name = (NameType) _person.getName();
		dbusr.usr_full_name_bil = name.getFn();
		dbusr.usr_display_bil = name.getFn();
		if (dbusr.usr_full_name_bil == null
			|| dbusr.usr_full_name_bil.length() == 0) {
			throw new cwException("User Full Name must not empty.");
		}

		tmpStr = name.getNickname();
		if (tmpStr != null && tmpStr.length() != 0) {
			dbusr.nickname = tmpStr;
			dbusr.usr_nickname = tmpStr;
		}

		NType n = (NType) name.getN();
		if (n != null) {
			tmpStr = n.getFamily();
			if (tmpStr != null)
				dbusr.usr_last_name_bil = tmpStr;
			tmpStr = n.getGiven();
			if (tmpStr != null)
				dbusr.usr_first_name_bil = tmpStr;

			List partnameList = n.getPartname();
			for (Iterator i = partnameList.iterator(); i.hasNext();) {
				PartnameType partname = (PartnameType) i.next();
				if (partname
					.getPartnametype()
					.equalsIgnoreCase(PARTNAME_TYPE_INITIAL)) {
					dbusr.usr_initial_name_bil = partname.getValue();
					break;
				}
			}
		}

		DemographicsType demographics =
			(DemographicsType) _person.getDemographics();
		if (demographics != null) {
			tmpStr = demographics.getGender();
			if (tmpStr != null) {
				if (tmpStr.equals("1"))
					dbusr.usr_gender = "F";
				else if (tmpStr.equals("2"))
					dbusr.usr_gender = "M";
				else
					dbusr.usr_gender = null;
			}
			tmpStr = demographics.getBday();
			if (tmpStr != null) {
				if (tmpStr.length() == 0) {
					dbusr.usr_bday = null;
				} else {
					dbusr.usr_bday =
						Timestamp.valueOf(
							IMSUtils.convertTimestampFromISO8601(tmpStr));
				}
			}
		}
		tmpStr = _person.getEmail();
		if (tmpStr != null)
			dbusr.usr_email = tmpStr;

		List telList = _person.getTel();
		int tel_cnt = 0;
		int tel_max_cnt = 2;
		for (Iterator i = telList.iterator(); i.hasNext();) {
			TelType tel = (TelType) i.next();
			if (tel.getTeltype().equals(TEL_TYPE_FAX_CODE)
				|| tel.getTeltype().equals(TEL_TYPE_FAX)) {
				dbusr.usr_fax_1 = tel.getValue();
			} else {
				tel_cnt++;
				if (tel_cnt == 1)
					dbusr.usr_tel_1 = tel.getValue();
				else if (tel_cnt == 2)
					dbusr.usr_tel_2 = tel.getValue();
				else
					break;
			}
		}
		/*
		Systemrole sysrole = getSystemrole();
		if (sysrole!=null){
		    String role = sysrole.getSystemroletype();
		    Vector tmpRole = new Vector();
		    if (dbusr.usr_roles!=null){
		        for (int i=0; i<dbusr.usr_roles.length;i++){
		            // not a import role
		            if (!SYSTEM_ROLE_MAP.containsValue(dbusr.usr_roles[i])){
		                tmpRole.addElement(dbusr.usr_roles[i]);
		            }
		        }
		    }
		    // plus role in xml
		    String importRole = (String)SYSTEM_ROLE_MAP.get(role);
		    if (importRole==null){
		        tmpRole.addElement(DEFAULT_SYS_ROLE);
		    }else{
		        tmpRole.addElement(importRole);
		    }
		    dbusr.usr_roles = aeUtils.vec2StringArray(tmpRole);
		}else{
		    // for insert new user
		    if (dbusr.usr_roles == null){
		        dbusr.usr_roles = new String[1];
		        dbusr.usr_roles[0] = DEFAULT_SYS_ROLE;
		    }
		}
		*/
		AdrType adr = (AdrType) _person.getAdr();
		if (adr != null) {
			tmpStr = "";
			List l_street = adr.getStreet();
			for (Iterator i = l_street.iterator(); i.hasNext();) {
				tmpStr += (String) i.next() + '\n';
			}
			if (tmpStr.length() > 0) {
				dbusr.usr_address_bil = tmpStr;
			}
		}

		setUserAttribute(con, dbusr, siteId, usr_id);

		org.imsglobal.enterprise.ExtensionType extension =
			(org.imsglobal.enterprise.ExtensionType) _person.getExtension();
		if (extension != null) {
			org.imsglobal.enterprise.cwn.ExtensionType cwnExtension =
				(org.imsglobal.enterprise.cwn.ExtensionType) extension
					.getExtension();
			if (cwnExtension != null) {
				dbusr.usr_job_title = cwnExtension.getJobTitle();
				tmpStr = cwnExtension.getJoinDate();
				if (tmpStr != null) {
					if (tmpStr.length() == 0) {
						dbusr.usr_join_datetime = null;
					} else {
						dbusr.usr_join_datetime =
							Timestamp.valueOf(
								IMSUtils.convertTimestampFromISO8601(tmpStr));
					}
				}

				List extList = cwnExtension.getExt();
				for (Iterator i = extList.iterator(); i.hasNext();) {
					org.imsglobal.enterprise.cwn.ExtensionType.ExtType cwnExt =
						(org.imsglobal.enterprise.cwn.ExtensionType.ExtType) i
							.next();
					String extType = cwnExt.getExttype();
					if (extType != null) {
						if (extType.equals("1")) {
							dbusr.usr_extra_1 = cwnExt.getValue();
						} else if (extType.equals("2")) {
							dbusr.usr_extra_2 = cwnExt.getValue();
						} else if (extType.equals("3")) {
							dbusr.usr_extra_3 = cwnExt.getValue();
						} else if (extType.equals("4")) {
							dbusr.usr_extra_4 = cwnExt.getValue();
						} else if (extType.equals("5")) {
							dbusr.usr_extra_5 = cwnExt.getValue();
						} else if (extType.equals("6")) {
							dbusr.usr_extra_6 = cwnExt.getValue();
						} else if (extType.equals("7")) {
							dbusr.usr_extra_7 = cwnExt.getValue();
						} else if (extType.equals("8")) {
							dbusr.usr_extra_8 = cwnExt.getValue();
						} else if (extType.equals("9")) {
							dbusr.usr_extra_9 = cwnExt.getValue();
						} else if (extType.equals("10")) {
							dbusr.usr_extra_10 = cwnExt.getValue();
						} else if (extType.equals("11")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_11 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("12")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_12 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("13")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_13 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("14")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_14 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("15")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_15 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("16")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_16 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("17")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_17 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("18")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_18 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("19")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_19 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("20")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_datetime_20 =
									Timestamp.valueOf(
										IMSUtils.convertTimestampFromISO8601(
											temp));
							}
						} else if (extType.equals("21")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_21 = temp;
							}
						} else if (extType.equals("22")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_22 = temp;
							}
						} else if (extType.equals("23")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_23 = temp;
							}
						} else if (extType.equals("24")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_24 = temp;
							}
						} else if (extType.equals("25")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_25 = temp;
							}
						} else if (extType.equals("26")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_26 = temp;
							}
						} else if (extType.equals("27")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_27 = temp;
							}
						} else if (extType.equals("28")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_28 = temp;
							}
						} else if (extType.equals("29")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_29 = temp;
							}
						} else if (extType.equals("30")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_singleoption_30 = temp;
							}
						} else if (extType.equals("31")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_31 = temp;
							}
						} else if (extType.equals("32")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_32 = temp;
							}
						} else if (extType.equals("33")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_33 = temp;
							}
						} else if (extType.equals("34")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_34 = temp;
							}
						} else if (extType.equals("35")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_35 = temp;
							}
						} else if (extType.equals("36")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_36 = temp;
							}
						} else if (extType.equals("37")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_37 = temp;
							}
						} else if (extType.equals("38")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_38 = temp;
							}
						} else if (extType.equals("39")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_39 = temp;
							}
						} else if (extType.equals("40")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.usr_extra_multipleoption_40 = temp;
							}
						} else if (extType.equals("41")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.urx_extra_41 = temp;
							}
						} else if (extType.equals("42")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.urx_extra_42 = temp;
							}
						} else if (extType.equals("43")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.urx_extra_43 = temp;
							}
						} else if (extType.equals("44")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.urx_extra_44 = temp;
							}
						} else if (extType.equals("45")) {
							String temp = cwnExt.getValue();
							if (temp != null && temp.length() > 0) {
								dbusr.urx_extra_45 = temp;
							}
						}
					}
				}
			}
		}
    }
          
    /**
    recover a wbUser from deleted status to initial status
    @param com
    @param dbReguser object to be recover.
    @param siteId
    */
    private void recover(Connection con, dbRegUser usr, long siteId, String initialUserStatus)
        throws  qdbErrMessage,  qdbException, SQLException  {
            usr.unDelete(con);
            usr.changeStatus(con,initialUserStatus); 
    }
    /**
    initialise SYSTEM_ROLE_MAP for further use.
    @param SYSTEM ROLE mapping string
    */
    /*
    private static void setSystemRoleMap(String sysRoleMap) throws cwException{
        if (sysRoleMap==null || sysRoleMap.length() == 0){
            throw new cwException("SYS_ROLE_MAP value absent in inifile.");    
        }
//        System.out.println(sysRoleMap);
        Vector vtRole = cwUtils.splitToVecString(sysRoleMap, SYS_ROLE_DELIMITER);
        for(int i=0; i<vtRole.size();i++){
//            System.out.println(vtRole.elementAt(i));
            
            String[] tmpArr = cwUtils.splitToString((String)vtRole.elementAt(i), SYS_ROLE_MAP_DELIMITER);
            
            if (tmpArr.length==2){
//                System.out.println(tmpArr[0]);
//                System.out.println(tmpArr[1]);
            
                SYSTEM_ROLE_MAP.put(tmpArr[0], tmpArr[1]);
            }else{
                throw new cwException("SYS_ROLE_MAP value is invalid, syntax as [SysAdmin:ADM_1,User:NLRN_1,Administrator:TADM_1]");
            }
        }
    }
    */
    public PersonType createPerson(Connection con, long usr_ent_id, Hashtable htUserGrade) throws qdbException, JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        org.imsglobal.enterprise.cwn.ObjectFactory cwnobjFactory = new org.imsglobal.enterprise.cwn.ObjectFactory();
        String tmpStr = null;

        dbRegUser usr = new dbRegUser();
        usr.usr_ent_id = usr_ent_id;
        usr.ent_id = usr_ent_id;
        usr.get(con);
        
        if (_person == null)
            _person = objFactory.createPerson();

        List sourcedidList = _person.getSourcedid();
        SourcedidType sourcedid = objFactory.createSourcedidType();
        sourcedid.setSource(IMSEnterpriseApp.exportSource);
        sourcedid.setId(usr.usr_ste_usr_id);
        sourcedidList.add(sourcedid);
        
        if (usr.usr_pwd != null){
            UseridType userid = objFactory.createUseridType();
            userid.setPassword(usr.usr_pwd);
            List useridList = _person.getUserid();
            useridList.add(userid);
        }
        _person.setEmail(usr.usr_email);
        NameType name = objFactory.createNameType();
        name.setFn(usr.usr_display_bil);
        if (usr.usr_nickname != null && usr.usr_nickname.length() > 0) {
        	name.setNickname(usr.usr_nickname);
        }
        _person.setName(name);
        
        DemographicsType demographics = objFactory.createDemographicsType();
        if (usr.usr_bday!=null)
            demographics.setBday(IMSUtils.convertTimestampToISO8601(usr.usr_bday));
        else
            demographics.setBday("");
            
        if (usr.usr_gender!=null && usr.usr_gender.equalsIgnoreCase("M"))
            demographics.setGender("2");
        else if (usr.usr_gender!=null && usr.usr_gender.equalsIgnoreCase("F"))
            demographics.setGender("1");
        else            
            demographics.setGender("");
        _person.setDemographics(demographics);

        List telList = _person.getTel();
        TelType tel1 = objFactory.createTelType();
        tmpStr = usr.usr_tel_1;
        if (tmpStr==null) 
            tmpStr = "";
        tel1.setTeltype("1");
        tel1.setValue(tmpStr);            
        telList.add(tel1);

        TelType tel2 = objFactory.createTelType();
        tmpStr = usr.usr_fax_1;
        if (tmpStr==null) 
            tmpStr = "";
        tel2.setTeltype("2");
        tel2.setValue(tmpStr);            
        telList.add(tel2);

        org.imsglobal.enterprise.ExtensionType extension = objFactory.createExtensionType();
        org.imsglobal.enterprise.cwn.ExtensionType cwnExtension = cwnobjFactory.createExtensionType();
            
        DbUserGrade ugr = (DbUserGrade)htUserGrade.get(new Long(usr_ent_id));
        if (ugr!=null){
            org.imsglobal.enterprise.cwn.ExtensionType.GradeType cwngrade = cwnobjFactory.createExtensionTypeGradeType();
            cwngrade.setValue(ugr.ugr_display_bil); 
            cwngrade.setId(ugr.ent_ste_uid); 
            cwnExtension.setGrade(cwngrade);
        }
        cwnExtension.setJobTitle(usr.usr_job_title);
        if (usr.usr_join_datetime!=null)
            cwnExtension.setJoinDate(IMSUtils.convertTimestampToISO8601(usr.usr_join_datetime));
        else
            cwnExtension.setJoinDate("");

        List l_cwnExt = cwnExtension.getExt();
        org.imsglobal.enterprise.cwn.ExtensionType.ExtType cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_1;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("1"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_2;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("2"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_3;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("3"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_4;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("4"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_5;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("5"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_6;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("6"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_7;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("7"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_8;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("8"); 
        l_cwnExt.add(cwnext);

        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_9;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("9"); 
        l_cwnExt.add(cwnext);
        
        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.usr_extra_10;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("10"); 
        l_cwnExt.add(cwnext);
        
        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.urx_extra_41;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("41"); 
        l_cwnExt.add(cwnext);
        
        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.urx_extra_42;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("42"); 
        l_cwnExt.add(cwnext);
        
        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.urx_extra_43;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("43"); 
        l_cwnExt.add(cwnext);
        
        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.urx_extra_44;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("44"); 
        l_cwnExt.add(cwnext);
        
        cwnext = cwnobjFactory.createExtensionTypeExtType();
        tmpStr = usr.urx_extra_45;
        if (tmpStr==null) 
            tmpStr = "";
        cwnext.setValue(tmpStr); 
        cwnext.setExttype("45"); 
        l_cwnExt.add(cwnext);
        
        extension.setExtension(cwnExtension);
        _person.setExtension(extension);
        return _person;
    }
    
    
}
