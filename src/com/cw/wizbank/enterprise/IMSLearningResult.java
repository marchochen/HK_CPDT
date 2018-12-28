package com.cw.wizbank.enterprise;

import java.lang.Long;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.Timestamp;
import javax.xml.bind.*;

import org.imsglobal.enterprise.*;
/*
import org.imsglobal.enterprise.Membership;
import org.imsglobal.enterprise.Member;
import org.imsglobal.enterprise.Sourcedid;
import org.imsglobal.enterprise.Role;
import org.imsglobal.enterprise.Finalresult;
import org.imsglobal.enterprise.Values;
import org.imsglobal.enterprise.Comments;
*/
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeApplication;
//import com.cw.wizbank.ae.db.view.ViewItemAttendance;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.view.ViewCourseEvaluationAttendance;

/**
* IMSLearningResult extends the Membership object for storing the learner result.
*/
public class IMSLearningResult
{
    /*
    * Content of NOT ATTEMPTED COMMENT
    */
    public static String NOT_ATTEMPTED_COMMENT = null;
    /*
    * Content of INCOMPLETE COMMENT
    */    
    public static String INCOMPLETE_COMMENT = null;
    /*
    * Content of COMPLETED COMMENT
    */    
    public static String COMPLETED_COMMENT = null;
    /*
    * Content of PASSED COMMENT
    */    
    public static String PASSED_COMMENT = null;
    /*
    * Content of FAILED COMMENT
    */
    public static String FAILED_COMMENT = null;
    /*
    * Content of WITHDRAW COMMENT
    */
    public static String WITHDRAW_COMMENT = null;
    
    public static String[] exportEnrollmentStatus = null;

    /**
    * Itm id of the course
    */
    public long itm_id;
    /*
    * Course id of the course
    */
    public String cos_id;
    
    private MembershipType _membership;
    
    public IMSLearningResult() throws JAXBException{
        ObjectFactory objFactory = new ObjectFactory();

        _membership = objFactory.createMembership();
    }
    
    /**
    * Get user result of the specified course from database<br>
    * Set properties of the Membership object<br>
    * Construct a member for each record and appending it into membership
    * @param startTime If startTime is specified, 
    *        only the learner attended to the course 
    *        after the specified time will be added to the result
    * @param endTime If endTime is specified, 
    *        only the learner attended to the course 
    *        before the specified time will be added to the result    
    * @param bEnrollmentOnly 
    *       export enrollment only, do not export result (exportEnrollmentStatus must be supplied)
    *       return true if has valid member(s)
    */
    public MembershipType get(Connection con, Timestamp startTime, Timestamp endTime, boolean bEnrollmentOnly) throws SQLException, cwException , JAXBException{
        // set uid        
        aeItem itm = new aeItem();
        itm.itm_id = this.itm_id;
        boolean run_ind = itm.getRunInd(con); 
        
        SourcedidType _Sourcedid = IMSUtils.createSourcedid(cos_id);
        _membership.setSourcedid(_Sourcedid);

        List _member = _membership.getMember();         
        Vector resultVec = new Vector();
        if (!bEnrollmentOnly){
            resultVec = ViewCourseEvaluationAttendance.getUserResult(con, itm_id, run_ind, startTime, endTime);  
        }
        Hashtable htEnrollList = null;
        aeApplication app = new aeApplication();
        if (exportEnrollmentStatus != null){
            htEnrollList = app.getAppProcessStatus(con, itm_id, exportEnrollmentStatus, startTime, endTime);
        }
        
        
        String comment = null;
        if (resultVec.size()==0 && (htEnrollList == null || (htEnrollList.size() ==0))){
            return null;    
        }else{
            ViewCourseEvaluationAttendance viewCea = null;
            for(int i=0; i<resultVec.size(); i++) {
                viewCea = (ViewCourseEvaluationAttendance)resultVec.elementAt(i);
                Hashtable htEnrollStatus = null;
                if (htEnrollList!=null){
                    Long appId = new Long(viewCea.app_id);
                    htEnrollStatus = (Hashtable)htEnrollList.remove(appId);
                }
                _member.add(createMember(viewCea, htEnrollStatus));            
            }
            if (htEnrollList!=null){
                Enumeration enumeration = htEnrollList.keys();
                while(enumeration.hasMoreElements()) {
                    _member.add(createMember(null, (Hashtable)htEnrollList.get(enumeration.nextElement())));
                }
            }
            return _membership;
        }
    }



    /**
    * Construct a member object with the user result
    * @param usr_id user login id
    * @param result attendance status of the user in the course
    * @param comments comment of the user in the current attendance status
    * @return Member 
    */
                                        
    private MemberType createMember(ViewCourseEvaluationAttendance viewCea, Hashtable htEnrollStatus) throws cwException, JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        org.imsglobal.enterprise.cwn.ObjectFactory cwnobjFactory = new org.imsglobal.enterprise.cwn.ObjectFactory();

        MemberType member = objFactory.createMemberType();
        
        String usr_ste_usr_id = null;
        if (viewCea!=null){
            usr_ste_usr_id = viewCea.usr_ste_usr_id;
        }else if (htEnrollStatus!=null){
            usr_ste_usr_id = (String)htEnrollStatus.get("usr_ste_usr_id");
        }else{
            throw new cwException("empty result");
        }
        SourcedidType sourcedid = IMSUtils.createSourcedid(usr_ste_usr_id);
        member.setSourcedid(sourcedid);
        //1 indicate the member is a person
        member.setIdtype("1");

        RoleType role = objFactory.createRoleType();
        //01 indicate learner or student
        role.setRoletype("01");
        //1 indicate active user
        role.setStatus("1");
        
        List finalresult_list = role.getFinalresult();
        
        if (htEnrollStatus!=null){
            FinalresultType finalresult = objFactory.createFinalresultType();
            finalresult.setMode(IMSApplication.RESULT_MODE_ENROLLMENT_STATUS);
                    
            ValuesType values = objFactory.createValuesType();
            values.setValuetype("0");
            List list = values.getList();
            for (int i=0; i<exportEnrollmentStatus.length; i++){
                list.add(exportEnrollmentStatus[i]);
            }
            
            finalresult.setValues(values);
            finalresult.setResult((String)htEnrollStatus.get("app_process_status"));
            finalresult_list.add(finalresult);
        }
        if (viewCea!=null){
            if (viewCea.att_rate!=null){
                FinalresultType finalresult = objFactory.createFinalresultType();
                finalresult.setMode(IMSApplication.RESULT_MODE_ATTENDANCE_RATE);
                        
                ValuesType values = objFactory.createValuesType();

                //1 indicate value bound by min and max 
                values.setValuetype("1");
                values.setMin("0");
                values.setMax("100");
                
                finalresult.setValues(values);
                finalresult.setResult(viewCea.att_rate);
                finalresult_list.add(finalresult);
            }
            if (viewCea.cov_score!=null){
                FinalresultType finalresult = objFactory.createFinalresultType();
                finalresult.setMode(IMSApplication.RESULT_MODE_ASSESSMENT_RESULT);
                        
                ValuesType values = objFactory.createValuesType();
                //0 indicate list of specific codes for result
                values.setValuetype("1");
                values.setMin("0");
                values.setMax("100");
                
                finalresult.setResult(viewCea.cov_score);

                if (viewCea.cov_comment!=null){
                    CommentsType comments = objFactory.createCommentsType();
                    comments.setValue(cwUtils.esc4XML(viewCea.cov_comment));            
                    finalresult.setComments(comments);
                }
                finalresult_list.add(finalresult);
            }

            // completion status
            Finalresult finalresult = objFactory.createFinalresult();
            
            finalresult.setMode(IMSApplication.RESULT_MODE_COMPLETION_STATUS);
                        
            ValuesType values = objFactory.createValuesType();
            //0 indicate list of specific codes for result
            values.setValuetype("0");
            List list = values.getList();
            list.add("N");
            list.add("I");
            list.add("C");
            list.add("P");
            list.add("F");
                
            finalresult.setValues(values);
            
            finalresult.setResult(viewCea.cov_status);
            
            String att_remark;
            if (viewCea.att_remark!=null){
                att_remark = viewCea.att_remark;
            }else{
                att_remark = getComment(viewCea.cov_status);
            }
            if (att_remark!=null){
                CommentsType comments = objFactory.createCommentsType();
                comments.setValue(cwUtils.esc4XML(att_remark));            
                finalresult.setComments(comments);
            }
            
            finalresult_list.add(finalresult);
            
            if (viewCea.att_timestamp!=null || viewCea.cov_last_acc_datetime!=null || viewCea.cov_total_time>0){
                org.imsglobal.enterprise.ExtensionType extension = objFactory.createExtensionType();
                org.imsglobal.enterprise.cwn.ExtensionType cwnExtension = cwnobjFactory.createExtensionType();
                if (viewCea.att_timestamp!=null)
                    cwnExtension.setCompletiondate(viewCea.att_timestamp.toString());
                if (viewCea.cov_last_acc_datetime!=null)
                    cwnExtension.setLastaccessdate(viewCea.cov_last_acc_datetime.toString());
                if (viewCea.cov_total_time>0)
                    cwnExtension.setTimespent(viewCea.cov_total_time);
                
                extension.setExtension(cwnExtension);
                role.setExtension(extension);
            }
        }
        
        List role_list = member.getRole();
        role_list.add(role);
        return member;
    }
    
    
        
        
    /**
    * Get the result comment accroding to the status
    * @param status User result in the course
    */
    private String getComment(String status){
        
        if( status.equalsIgnoreCase("N") )
            return NOT_ATTEMPTED_COMMENT;
        else if( status.equalsIgnoreCase("I") )
            return INCOMPLETE_COMMENT;
        else if( status.equalsIgnoreCase("C") )
            return COMPLETED_COMMENT;
        else if( status.equalsIgnoreCase("P") )
            return PASSED_COMMENT;
        else if( status.equalsIgnoreCase("F") )
            return FAILED_COMMENT;
        else if( status.equalsIgnoreCase("W") )
            return WITHDRAW_COMMENT;
        else 
            return NOT_ATTEMPTED_COMMENT;
    }
    
    public static void setExportEnrollmentStatusList(String statusList){
        if (statusList != null){
            exportEnrollmentStatus = cwUtils.splitToString(statusList, "~");
        }
    }
    
    /**
    * Set not attempted comment
    * @param comment Content of the comment
    */
    public static void setNotAttemptedComment(String comment){
        NOT_ATTEMPTED_COMMENT = comment;
        return;
    }

    /**
    * Set incompleted comment
    * @param comment Content of the comment
    */    
    public static void setIncompleteComment(String comment){
        INCOMPLETE_COMMENT = comment;
        return;
    }
    
    /**
    * Set completed comment
    * @param comment Content of the comment
    */    
    public static void setCompletedComment(String comment){
        COMPLETED_COMMENT = comment;
        return;
    }
    
    /**
    * Set passed comment
    * @param comment Content of the comment
    */    
    public static void setPassedComment(String comment){
        PASSED_COMMENT = comment;
        return;
    }
    
    /**
    * Set failed comment
    * @param comment Content of the comment
    */    
    public static void setFailedComment(String comment){
        FAILED_COMMENT = comment;
        return;
    }
    
    public static void setWithdrawComment(String comment){
        WITHDRAW_COMMENT = comment;
        return;
    }
}
