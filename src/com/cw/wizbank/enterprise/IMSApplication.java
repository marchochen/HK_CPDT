package com.cw.wizbank.enterprise;

//import standard java classes
import java.lang.Long;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import java.io.IOException;

import javax.xml.bind.*;

//import generated IMS classes
import org.imsglobal.enterprise.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeAttendance;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeAppnActnHistory;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUserFigure;//import com.cw.wizbank.ae.aeUserAccreditation;
import com.cw.wizbank.ae.db.DbFigureType;//import com.cw.wizbank.ae.db.DbItemCredit;
import com.cw.wizbank.ae.db.DbFigure;
import com.cw.wizbank.ae.db.view.ViewFigure;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;


/**
IMSApplication represents a special kind of Membership and this
kind of Membership is represented as aeApplication in wizBank.
<BR>
IMSApplication has 2 constructors.
The default constructor IMSApplication() is preserved for the usage of importing XML
data into IMSApplication directly. And thus you can use getMember() to retrieve child node info.
The 2nd constructor, IMSApplication(Membership, Member, Role) does not import XML itself.
it takes in Membership, Member and Role objects as it's properties. Thus you cannot use
getMember() to retrieve child node info. However you can use getAppMember() and getAppRole()
to retrieve data passed into constructor.
*/
public class IMSApplication
{
    public static final String ROLE_TYPE_LEARNER_CODE = "01";
    public static final String ROLE_TYPE_LEARNER = "Learner";
    
    public static final String RESULT_MODE_COMPLETION_STATUS = "Completion Status";
    public static final String RESULT_MODE_ASSESSMENT_RESULT = "Assessment Result";
    public static final String RESULT_MODE_ATTENDANCE_RATE = "Attendance Rate";
    public static final String RESULT_MODE_ENROLLMENT_STATUS = "Enrollment Status";
    public static final String RESULT_MODE_ACCREDITATION_PREFIX = "Accreditation-";
    public static final String RESULT_MODE_ACCREDITATION_SEPARATOR = "-";

    public static final String IMPORT_RULE_INSERT = "INSERT";
    public static final String IMPORT_RULE_UPDATE = "UPDATE";
    public static final String IMPORT_RULE_INSERT_N_UPDATE = "INSERT_N_UPDATE";
    public static final String IMPORT_RULE_INSERT_ON_DUPLICATE = "INSERT_ON_DUPLICATE";
    
    public static final String IMPORT_RULE_DEFAULT = IMPORT_RULE_INSERT_N_UPDATE;
    
    public Hashtable htAttendanceStatusList = null;
    private MembershipType _membership;
    private MemberType appMember;
    private RoleType appRole;
    private loginProfile wbProfile;
    private long wbItemId;
    private long wbEntityId;
    private boolean isSession;
    private long wbAppItemId;
    private long wbAppId;
    private String sourceDIDID;
    private String memberSourceDIDID;
    private String importRule;
    
    private static Hashtable htCreditId = new Hashtable();
    /**
    Default Constructor
    */
//    public IMSApplication() {
//        super();
//    }
    public IMSApplication() throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        // create an empty group
        _membership = objFactory.createMembership();
    }   

    public MembershipType getMembership(){
        return _membership;
    }
    /**
    Construct a new IMSApplication that contains Comments and Sourcedid currently contained in the membership argument.
    And stores appMemeber and appRole as its properties.
    @param membership a Membership object with data
    @param appMember a Member object with data
    @param appRole a Role object with data
    */
    public IMSApplication(MembershipType membership, MemberType appMember, RoleType appRole, String importRule){
        this._membership = membership;
//        setComments(membership.getComments());
//        setSourcedid(membership.getSourcedid());
        this.appMember = appMember;
        this.appRole = appRole;
        this.importRule = importRule;
        if (this.importRule == null){
            this.importRule = IMPORT_RULE_DEFAULT;
        }
    }
    
    public static boolean validateActionRule(String actionRule){
        boolean bValid;
        if (actionRule.equalsIgnoreCase(IMPORT_RULE_INSERT)){
            bValid = true;
        }else if (actionRule.equalsIgnoreCase(IMPORT_RULE_UPDATE)){
            bValid = true;
        }else if (actionRule.equalsIgnoreCase(IMPORT_RULE_INSERT_N_UPDATE)){
            bValid = true;
        }else if (actionRule.equalsIgnoreCase(IMPORT_RULE_INSERT_ON_DUPLICATE)){
            bValid = true;
        }else{
            bValid = false;
        }
        return bValid;
    }
    

    /** 
    get memberSourceDIDID
    set memberSourceDIDID if memberSourceDIDID not yet set
    @return memberSourceDIDID
    */    
    public String getMemberSourceDIDID(){
        if (this.memberSourceDIDID==null){
            this.memberSourceDIDID = appMember.getSourcedid().getId();
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
    Get appMember object 
    @return appMember
    */
    public MemberType getAppMember() {
        return this.appMember;
    }
    
    /**
    Get appRole object
    @return appRole
    */
    public RoleType getAppRole() {
        return this.appRole;
    }

    /**
    Get wizBank's aeItem.itm_id represented by this IMSApplication from database
    @param con Connection to database
    @param wbSiteId wizBank organization's site id
    @return wizBank's aeItem.itm_id represented by this IMSApplication, or 0 if no matching aeItem is found
    */
    public long getWbItemId(Connection con, long wbSiteId) throws SQLException {
        this.wbItemId = 0;
        aeItem itm = new aeItem();
        itm.itm_code = _membership.getSourcedid().getId();
        itm.itm_owner_ent_id = wbSiteId;
        this.wbItemId = itm.getItemId(con);
        return this.wbItemId;
    } 

    /**
    Get wizBank's RegUser.usr_ent_id represented by this IMSApplication.appMember from database
    @param con Connection to database
    @param wbSiteId wizBank organization's site id
    @return wizBank's RegUser.usr_ent_id represented by this IMSApplication.appMember, or 0 if no matching RegUser is found
    */
    public long getWbUserEntId(Connection con, long wbSiteId) throws SQLException, qdbException {
        this.wbEntityId = 0;
        dbRegUser dbusr = new dbRegUser();
        this.wbEntityId = dbusr.getEntId(con, this.appMember.getSourcedid().getId(), wbSiteId);      
        //this.wbEntityId = dbRegUser.getEntId( con, this.appMember.getSourcedid().getId());
        return this.wbEntityId ;
    } 

    /**
    Save this Application Membership to wizBank's aeApplication.
    This method will use 2 algorithms to perform the save action.
    If this membership comes from directly import from XML, it will save all underlying membership to aeApplication.
    Else if this membership comes from appMember and appcRole, save that relation to aeApplication.
    @param con Connection to database.
    @param wbProfile wizBank's loginProfile who perform this save action.
    */
    public int save(Connection con, loginProfile wbProfile, String from)
        throws SQLException, cwException, cwSysMessage, IOException, qdbException {
        int code = IMSUtils.SAVE_CODE_UPDATE;            
        //setup class property
        this.wbProfile = wbProfile;
        
        //check if this Membership if from XML or appMember, appRole
        if(this.appMember !=null && this.appRole != null) {
            code = save(con, from);
        } else if(_membership.getMember() != null) {
            List l_member = _membership.getMember();
            for (Iterator j = l_member.iterator(); j.hasNext(); ){
                MemberType member = (MemberType)j.next();
                List l_role = member.getRole();
                for (Iterator k = l_role.iterator(); k.hasNext();){
                    RoleType role = (RoleType)k.next();
                    IMSApplication imsApplication = new IMSApplication(_membership, member, role, importRule);
                    code = imsApplication.save(con, this.wbProfile, from);
                }
            }
        }
        return code;
    }

    /**
    Save this Application Membership to wizBank's aeApplication
    @param con Connection to database
    */
    private int save(Connection con, String from) 
        throws SQLException, cwException, qdbException, cwSysMessage, IOException {
        int code = IMSUtils.SAVE_CODE_INSERT;                            
        //check if this membership is active
        if(this.appRole.getStatus() != null && this.appRole.getStatus().equals("1")) {
            //get item id
            if(getWbItemId(con, this.wbProfile.root_ent_id) == 0) {
                throw new cwException ("IMSApplication.update：" + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_425"));
            }
            //get user entity id
            if(getWbUserEntId(con, this.wbProfile.root_ent_id) == 0) {
                throw new cwException("IMSApplication.update： " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_426"));
            }
            aeItem itm = new aeItem();
            itm.itm_id = this.wbItemId;
            /*isSession = itm.getSessionInd(con);
            if (isSession){
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = this.wbItemId;
                this.wbAppItemId = ire.getParentItemId(con);
            }else{
                this.wbAppItemId = this.wbItemId;
            }
            //insert into aeApplication if it does not exist
            if (!isSession){
                aeQueueManager qman = new aeQueueManager();
                long lastAppId = aeApplication.getLatestAppId(con, this.wbItemId, this.wbEntityId);
                long tkh_id = 0;
                aeApplication app = null;
                String toStatus = getEnrollmentStatus();
                //check enrollment workflow indicator.(vincent)
                if (toStatus == null || toStatus.length() == 0){
                    String sEnrollmentWorkflow = getEnrollmentWorkflow();
                    if (sEnrollmentWorkflow.equalsIgnoreCase("Y")){
                        qman.auto_enroll_ind = false;
                    } else {
                        qman.auto_enroll_ind = true;
                    }
                }
                String completionStatus = getCompletionStatus();
                checkStatus(con, toStatus, completionStatus);

                if (importRule.equalsIgnoreCase(IMPORT_RULE_INSERT)){
                    if (lastAppId>0){
                        throw new cwException("FAILURE IN IMPORT APPLICATION. APPLICATION ALREADY EXIST");
                    }else{
                        app = qman.insApplication(con, null, this.wbEntityId, this.wbAppItemId, this.wbProfile, 0, 0, 0, 0, toStatus, toStatus, false, tkh_id);
                    }
                }else if (importRule.equalsIgnoreCase(IMPORT_RULE_UPDATE)){
                    if (lastAppId==0){
                        throw new cwException("FAILURE IN UPDATE APPLICATION. APPLICATION DOES NOT EXIST");
                    }else{
                        app = new aeApplication();
                        app.app_id = lastAppId;
                        app.get(con);
                    }
                }else if (importRule.equalsIgnoreCase(IMPORT_RULE_INSERT_N_UPDATE)){
                    if (lastAppId > 0){
                        app = new aeApplication();
                        app.app_id = lastAppId;
                        app.get(con);
                        if (toStatus!=null && !toStatus.equals(app.app_process_status)){
                            // delete latest application
                            app.del(con);
                            app = qman.insApplication(con, null, this.wbEntityId, this.wbAppItemId, this.wbProfile, 0, 0, 0, 0, toStatus, toStatus, false, app.app_tkh_id);
                        }else{
                            // no changes needed. nothing to do
                        }                        
                        code = IMSUtils.SAVE_CODE_UPDATE;                            
                    }else{
                        app = qman.insApplication(con, null, this.wbEntityId, this.wbAppItemId, this.wbProfile, 0, 0, 0, 0, toStatus, toStatus, false, tkh_id);
                    }
                }

                this.wbAppId = app.app_id; 
            
                //update aeApplication.app_syn_date
                qman.updAppnSynDate(con, this.wbEntityId, this.wbItemId, cwSQL.getTime(con));
            }*/
            this.wbAppItemId = this.wbItemId;
            aeQueueManager qman = new aeQueueManager();
            long lastAppId = aeApplication.getLatestAppId(con, this.wbItemId, this.wbEntityId);
            long tkh_id = 0;
            aeApplication app = null;
            String toStatus = getEnrollmentStatus();
            String sSendMail = null;
            String sEnrollmentWorkflow = null;
            org.imsglobal.enterprise.ExtensionType extension = (org.imsglobal.enterprise.ExtensionType)this.appRole.getExtension();
            if (extension!=null){
                org.imsglobal.enterprise.cwn.ExtensionType cwnextension = (org.imsglobal.enterprise.cwn.ExtensionType) extension.getExtension();
                if (cwnextension != null){
                    //get send mail and enrollment workflow indicators
                    sSendMail = cwnextension.getSendmail();
                    sEnrollmentWorkflow = cwnextension.getEnrollmentworkflow();
                }
            }
            if (toStatus == null || toStatus.length() == 0){
                if (sEnrollmentWorkflow.equalsIgnoreCase("Y")){
                    qman.auto_enroll_ind = false;
                } else {
                    qman.auto_enroll_ind = true;
                    if (sSendMail.equalsIgnoreCase("N")){
                        qman.send_mail_ind = false;
                    }
                }
            }
            String completionStatus = getCompletionStatus();
            checkStatus(con, toStatus, completionStatus);

            if (importRule.equalsIgnoreCase(IMPORT_RULE_INSERT)){
                itm.get(con);
 
                if (!isEnrollable( con, this.wbEntityId, itm, lastAppId, sEnrollmentWorkflow, completionStatus)){
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_427"));
                }else{
                 	 if(itm.itm_app_approval_type == null || itm.itm_app_approval_type.length() == 0 || qman.auto_enroll_ind) {
                 		app = qman.insAppNoWorkflow(con, null, this.wbEntityId, this.wbAppItemId, null, from, this.wbProfile, itm);
                 	 } else {
                 		 app = qman.insApplication(con, null, this.wbEntityId, this.wbAppItemId, this.wbProfile, 0, 0, 0, 0, toStatus, toStatus, false, tkh_id, from);
                 	 }
                }
            }else if (importRule.equalsIgnoreCase(IMPORT_RULE_UPDATE)){
                if (lastAppId==0){
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_428"));
                }else{
                    app = new aeApplication();
                    app.app_id = lastAppId;
                    app.get(con);
                }
            }else if (importRule.equalsIgnoreCase(IMPORT_RULE_INSERT_N_UPDATE)){
                if (lastAppId > 0){
                    app = new aeApplication();
                    app.app_id = lastAppId;
                    app.get(con);
                    if (toStatus!=null && !toStatus.equals(app.app_process_status)){
                        // delete latest application
                        app.del(con);
                        app = qman.insApplication(con, null, this.wbEntityId, this.wbAppItemId, this.wbProfile, 0, 0, 0, 0, toStatus, toStatus, false, app.app_tkh_id, from);
                    }else{
                        // no changes needed. nothing to do
                    }                        
                    code = IMSUtils.SAVE_CODE_UPDATE;                            
                }else{
                    app = qman.insApplication(con, null, this.wbEntityId, this.wbAppItemId, this.wbProfile, 0, 0, 0, 0, toStatus, toStatus, false, tkh_id, from);
                }
            }

            this.wbAppId = app.app_id; 
            
            //update aeApplication.app_syn_date
            qman.updAppnSynDate(con, this.wbEntityId, this.wbItemId, cwSQL.getTime(con));
            app.get(con);
            //save enrollment date
            if (extension!=null){
                org.imsglobal.enterprise.cwn.ExtensionType cwnextension = (org.imsglobal.enterprise.cwn.ExtensionType) extension.getExtension();
                String sEnrollmentDate = cwnextension.getEnrollmentdate();
                if(cwnextension.getEnrollmentworkflow().equalsIgnoreCase("Y")){
                    sEnrollmentDate = null;
                }
                if (sEnrollmentDate!=null){
                    Timestamp enrollmentDate = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sEnrollmentDate));
                        aeAppnActnHistory.updUpdateTimestamp(con, this.wbAppId, wbProfile.usr_id, enrollmentDate);
                    
                }
            }
            if (app.app_status.equalsIgnoreCase(aeApplication.ADMITTED)){
            List l_result = this.appRole.getFinalresult();
            if (l_result !=null && l_result.size() > 0){
                for (Iterator j = l_result.iterator(); j.hasNext(); ){
                    FinalresultType finalresult = (FinalresultType)j.next();
                    saveResult(con, finalresult);
                }
            }
            
            if (extension!=null){
                org.imsglobal.enterprise.cwn.ExtensionType cwnextension = (org.imsglobal.enterprise.cwn.ExtensionType) extension.getExtension();
                if (cwnextension != null){
                    saveExtension(con, cwnextension);            
                    }
                }
            }
        }
        
        return code;
    }
    
    private String getEnrollmentStatus(){
        String toStatus = null;
        List l_result = this.appRole.getFinalresult();
        if (l_result !=null && l_result.size() > 0){
            for (Iterator j = l_result.iterator(); j.hasNext(); ){
                FinalresultType finalresult = (FinalresultType)j.next();
                String mode = finalresult.getMode();
                if (mode!=null){
                    if (mode.equalsIgnoreCase(RESULT_MODE_ENROLLMENT_STATUS)){
                        toStatus = finalresult.getResult();
                    }
                }
            }
        }
        return toStatus;
    }

    // if the record has attribute (attendance, courseEvaluation) that the user must be admitted in the course
    private boolean hasCourseResult(){
        boolean bCourseResult = false;
        
        List l_result = appRole.getFinalresult();
        if (l_result !=null && l_result.size() > 0){
            for (Iterator j = l_result.iterator(); j.hasNext(); ){
                FinalresultType finalresult = (FinalresultType)j.next();
                String mode = finalresult.getMode();
                if (mode!=null){
                    if (mode.equalsIgnoreCase(RESULT_MODE_COMPLETION_STATUS) 
                        || mode.equalsIgnoreCase(RESULT_MODE_ASSESSMENT_RESULT) 
                        || mode.equalsIgnoreCase(RESULT_MODE_ATTENDANCE_RATE) 
                        || mode.startsWith(RESULT_MODE_ACCREDITATION_PREFIX) ){
                        bCourseResult = true;                       
                        return bCourseResult;
                    }
                }
            }
        }
        
        org.imsglobal.enterprise.ExtensionType extension = (org.imsglobal.enterprise.ExtensionType) appRole.getExtension();
        if (extension!=null){
            org.imsglobal.enterprise.cwn.ExtensionType cwnextension = (org.imsglobal.enterprise.cwn.ExtensionType)extension.getExtension();
            if (cwnextension!=null){
                String sCompletionDate = cwnextension.getCompletiondate();
                if (sCompletionDate!=null)  
                    return true;
                String sLastAccessDate = cwnextension.getLastaccessdate();
                if (sLastAccessDate!=null)
                    return true;
                float timeSpent = cwnextension.getTimespent();
                if (timeSpent!=0)
                    return true;
            }
        }
        return bCourseResult;
    }
    
    private String getCompletionStatus(){
        String completionStatus = null;
        List l_result = this.appRole.getFinalresult();
        if (l_result !=null && l_result.size() > 0){
            for (Iterator j = l_result.iterator(); j.hasNext(); ){
                FinalresultType finalresult = (FinalresultType)j.next();
                String mode = finalresult.getMode();
                if (mode!=null){
                    if (mode.equalsIgnoreCase(RESULT_MODE_COMPLETION_STATUS)){
                        completionStatus = finalresult.getResult();
                    }
                }
            }
        }
        return completionStatus;
    }
    
    private void checkStatus(Connection con, String enrollmentStatus, String completionStatus) throws qdbException, IOException, cwException, SQLException{
        // enrollment system status must be "admitted" when has course result
        if (enrollmentStatus!=null && hasCourseResult()){
            String[] admittedEnrollmentStatus = aeQueueManager.getProcessStatus(con, this.wbAppItemId, aeApplication.ADMITTED);

            if (!cwUtils.strArrayContains(admittedEnrollmentStatus, enrollmentStatus)){
                throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_429") + "：" + enrollmentStatus + " " +LabelContent.get(wbProfile.cur_lan, "label_core_training_management_430") + "：" + completionStatus);
            }
        }
                
        Vector inProgressEnrollmentStatus = new Vector();
        String[] pending_status = aeQueueManager.getProcessStatus(con, this.wbAppItemId, aeApplication.PENDING);
        String[] waiting_status = aeQueueManager.getProcessStatus(con, wbAppItemId, aeApplication.WAITING);
                
        // add pending_status to inProgressEnrollmentStatus
        for (int i=0; i<pending_status.length; i++){
            inProgressEnrollmentStatus.addElement(pending_status[i]);
        }
        for (int i=0; i<waiting_status.length; i++){
            inProgressEnrollmentStatus.addElement(waiting_status[i]);
        }

        // Multiple Enrollment Checking
        // to avoid multiple in-progress status enrollment/attendance in same course 
        if( ( completionStatus != null && completionStatus.equalsIgnoreCase("I") )
            || ( enrollmentStatus != null && inProgressEnrollmentStatus.indexOf(enrollmentStatus) != -1 ) ) {
            //randy modify
            // do not need check enrollment in other class of the course
            /*
            aeItemRelation aeIre = new aeItemRelation();
            aeIre.ire_child_itm_id = this.wbItemId;
            long parent_itm_id = aeIre.getParentItemId(con);
            */
            if( aeApplication.getAppId(con, /*parent_itm_id*/this.wbItemId, this.wbEntityId, true, this.wbItemId) > 0 ) {
                throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_431"));
            }
        }
    }
    
    private void saveResult(Connection con, FinalresultType finalresult) throws cwSysMessage, qdbException, cwException, SQLException{
        String mode = finalresult.getMode();
        if (mode!=null){
            if (mode.equalsIgnoreCase(RESULT_MODE_COMPLETION_STATUS)){
                if (htAttendanceStatusList==null){
                    htAttendanceStatusList = aeAttendanceStatus.getAllCovStatus(con, wbProfile.root_ent_id); 
                }
                String result = finalresult.getResult();
                CommentsType comments = (CommentsType)finalresult.getComments();
                String remark = (comments==null)? null : comments.getValue();
                if (htAttendanceStatusList.containsKey(result)){
//                    long app_id = aeApplication.getLatestAppId(con, this.wbAppItemId, this.wbEntityId);
//                    for (int i=0; i<vtAppId.size(); i++){
                        aeAttendance att = new aeAttendance();
                        att.att_app_id = this.wbAppId;
                        att.att_itm_id = this.wbItemId;
                        att.get(con);

                        att.att_ats_id = ((Integer)htAttendanceStatusList.get(result)).intValue();
                        att.att_remark = remark;
                        att.att_update_usr_id = wbProfile.usr_id;
                        att.save(con, wbProfile.usr_id);
//                    }
                }else{
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_432") + "：" + result);
                }
            }else if (mode.equalsIgnoreCase(RESULT_MODE_ASSESSMENT_RESULT)){
                String result = finalresult.getResult();
                CommentsType comments = (CommentsType)finalresult.getComments();
                String comment = (comments==null)? null : comments.getValue();
                try{
                    if (result != null){
                        result = new Float(result).toString();
                    }
                }catch(NumberFormatException e){
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_433") + " " + RESULT_MODE_ASSESSMENT_RESULT + "：" + result);    
                }            
                aeItem itm = new aeItem();
                itm.itm_id = this.wbItemId;
                itm.getItem(con);
                long cos_id = itm.getResId(con); 
                
//                Vector vtAppId = aeApplication.getAllAppId(con, this.wbAppItemId, this.wbEntityId);
//                for (int i=0; i<vtAppId.size(); i++){
                    aeAttendance att = new aeAttendance();
//                    att.att_app_id = ((Long)vtAppId.elementAt(i)).longValue();
                    att.att_app_id = this.wbAppId;
                    att.att_itm_id = this.wbItemId;
                    boolean hasRecord = att.get(con);
                    if (!hasRecord){
                        att.att_ats_id = ((Integer)htAttendanceStatusList.get("I")).intValue();
                    }
                    att.save(con, wbProfile.usr_id);
//                }
                // must have courseEvaluation record after "save attendance"
                
                long tkh_id = aeApplication.getTkhId(con, this.wbAppId); 

//                for (int i=0; i<vtTkhId.size(); i++){
                    dbCourseEvaluation cov = new dbCourseEvaluation();
                    cov.cov_ent_id = this.wbEntityId;
                    cov.cov_cos_id = cos_id;
//                    cov.cov_tkh_id = ((Long)vtTkhId.elementAt(i)).longValue();
                    cov.cov_tkh_id = tkh_id;
                    cov.get(con);
                    cov.cov_score = result;
                    if (comment!=null)
                        cov.cov_comment = comment;

                    cov.upd(con);
//                }
                
                
            }else if (mode.equalsIgnoreCase(RESULT_MODE_ATTENDANCE_RATE)){
                String result = finalresult.getResult();
                try{
                    if (result != null){
                        result = new Float(result).toString();
                    }
                }catch(NumberFormatException e){
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_433") + " " + RESULT_MODE_ATTENDANCE_RATE + "：" + result);    
                }            
//                Vector vtAppId = aeApplication.getAllAppId(con, this.wbAppItemId, this.wbEntityId);
//                for (int i=0; i<vtAppId.size(); i++){
                    aeAttendance att = new aeAttendance();
//                    att.att_app_id = ((Long)vtAppId.elementAt(i)).longValue();
                    att.att_app_id = this.wbAppId;
                    att.att_itm_id = this.wbItemId;
                    att.get(con);
                    att.att_rate = result;
                    att.att_update_usr_id = wbProfile.usr_id;
                    att.save(con, wbProfile.usr_id);
//                }
            }else if (mode.startsWith(RESULT_MODE_ACCREDITATION_PREFIX)){
                mode = mode.substring(RESULT_MODE_ACCREDITATION_PREFIX.length());
                String[] creditTypes = cwUtils.splitToString(mode, RESULT_MODE_ACCREDITATION_SEPARATOR);
                long fgt_id;
                String creditType;
                String creditSubType;
                
                if (creditTypes.length==1){
                    creditType = creditTypes[0];                                        
                    creditSubType = null;
                }else if (creditTypes.length==2){
                    creditType = creditTypes[0];                                        
                    creditSubType = creditTypes[1];
                }else{
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_434") + "：" + finalresult.getMode() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_435") + "：" + this.wbItemId + " ," + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_436") + "：" + this.wbEntityId);
                }
                if (!htCreditId.containsKey(mode)){
                    fgt_id = DbFigureType.getIdByType(con, wbProfile.root_ent_id, creditType, creditSubType); 
                    htCreditId.put(mode, new Long(fgt_id));
                }else{
                    fgt_id = ((Long)htCreditId.get(mode)).longValue();
                }
                // check if the credit type is defined in that item 
                DbFigure[] figures = ViewFigure.getItemFigure(con, wbItemId, fgt_id);
                if (figures.length == 0){
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_437") + ": " + creditType + ", " + creditSubType + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_435") + "：" + this.wbItemId + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_436") + "：" + this.wbEntityId);
                }

                String result; 
                try{
                    result = new Float(finalresult.getResult()).toString();
                }catch(NumberFormatException e){
                    throw new cwException(LabelContent.get(wbProfile.cur_lan, "label_core_training_management_438") + " " + finalresult.getMode() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_439") + "：" + finalresult.getResult() + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_435") + "：" + this.wbItemId + " " + LabelContent.get(wbProfile.cur_lan, "label_core_training_management_436") + "：" + this.wbEntityId);    
                }            
                /*
                aeUserAccreditation aeUad = new aeUserAccreditation();
                aeUad.updUserAccreditation(con, wbProfile, this.wbEntityId, this.wbItemId, new long[]{ict_id}, new String[]{result});
                */
                aeUserFigure aeUfg = new aeUserFigure();
                aeUfg.updUserFigure(con, wbProfile, this.wbAppId, new long[]{fgt_id}, new String[]{result});
            }else if (mode.equalsIgnoreCase(RESULT_MODE_ENROLLMENT_STATUS)){
                // done 
            }else{
                System.err.println("unknown finalresult mode:" + mode);
            }
        }
    }
    
    private void saveExtension(Connection con, org.imsglobal.enterprise.cwn.ExtensionType cwnextension) throws qdbException, SQLException{
        String sCompletionDate = cwnextension.getCompletiondate();
        String sEnrollmentDate = cwnextension.getEnrollmentdate();
        String sLastAccessDate = cwnextension.getLastaccessdate();
        float timeSpent = cwnextension.getTimespent();

        if (sCompletionDate!=null){
            // here, completion date means att_timestamp and may be cov.completion_date if the status is completed
            Timestamp completionDate = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sCompletionDate));
            aeAttendance att = new aeAttendance();
            att.att_app_id = this.wbAppId;
            att.att_itm_id = this.wbItemId;
            att.att_timestamp = completionDate;
            att.updAttTimestamp(con, wbProfile.usr_id);
        }
        if (sCompletionDate!=null || sLastAccessDate!=null || timeSpent>0){
        	CommonLog.debug("save extension");
            aeItem itm = new aeItem();
            itm.itm_id = this.wbItemId;

            dbCourseEvaluation cov = new dbCourseEvaluation();
            cov.cov_ent_id = this.wbEntityId;
            cov.cov_cos_id = itm.getResId(con); 
            cov.getTkhId(con);
            
            boolean hasRecord = cov.get(con);
            if (hasRecord){
                if (sCompletionDate!= null && cov.cov_status.equals("C")){
                    cov.cov_complete_datetime = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sCompletionDate));                
                }
                if (sLastAccessDate!=null){
                    cov.cov_last_acc_datetime = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sLastAccessDate));                
                }
                if (timeSpent>0){
                    cov.cov_total_time = timeSpent;
                }
                cov.upd(con);
            }
        }
        if(cwnextension.getEnrollmentworkflow().equalsIgnoreCase("Y")){
            sEnrollmentDate = null;
        }
        if (sEnrollmentDate!=null){
            Timestamp enrollmentDate = Timestamp.valueOf(IMSUtils.convertTimestampFromISO8601(sEnrollmentDate));
                aeAppnActnHistory.updUpdateTimestamp(con, this.wbAppId, wbProfile.usr_id, enrollmentDate);
        }
    }
    
    public static boolean isEnrollable(Connection con, long usr_ent_id, aeItem  item, long app_id, String enrollment_workflow, String completion_status)
    throws SQLException, cwException {

        Timestamp cur_time = cwSQL.getTime(con);
        String xml = null;
        boolean enrollable = false;
        //Check item attribute
       
            aeQueueManager qm = new aeQueueManager();
            List retake_itm_lst = qm.hasRetakeConflict(con,item.itm_id, usr_ent_id);
            if(!item.itm_retake_ind && retake_itm_lst.size() != 0) {                    //if has retake conflict, return false
                enrollable = false;
            } else {
                aeApplication app = new aeApplication();
                app.app_id = app_id;//aeApplication.getLatestApplicationId(con, item.itm_id, usr_ent_id);
                if( app.app_id == 0 ) {
                    //learner has not applied this course yet
                    enrollable = true;
                } else {
                    try{
                        app.get(con);
                    }catch(qdbException e){
                        throw new cwException(e.getMessage());
                    }

                    String ats_type = app.getAttandanceStatus(con);
                    if( ats_type == null ) {
                        if( app.app_status.equalsIgnoreCase(aeApplication.REJECTED) || app.app_status.equalsIgnoreCase(aeApplication.WITHDRAWN)) {
                            //the applicaiton is cancelled
                            enrollable = true;
                        } else {
                            //the application is pending or waiting
                            enrollable = false;
                        }
                    } else {
                        if( ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_PROGRESS) ){
                            //the application is admitted and in progress
                            enrollable = false;
                        } else {
                            //the application has completed
                            enrollable = true;
                        }
                    }
                }
            }
            if(!enrollable){
                if(enrollment_workflow.equals("Y")){
                    enrollable = false;
                }else{
                    if(item.itm_retake_ind && (completion_status.equals("C") || completion_status.equals("F") || completion_status.equals("N") || completion_status.equals("W"))){
                        enrollable = true;
                    }else{
                        enrollable = false;
                    }
                }
            }
            
        
        return enrollable;
    }
}
