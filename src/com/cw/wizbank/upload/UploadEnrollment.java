package com.cw.wizbank.upload;

import java.lang.Long;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.zip.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.enterprise.*;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.db.view.ViewFigure;
import com.cw.wizbank.ae.db.DbFigureType;
import com.cwn.wizbank.utils.CommonLog;

import org.imsglobal.enterprise.*;

import javax.xml.bind.*;

/////////////
import java.io.File;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Workbook;
import jxl.Sheet;
import jxl.read.biff.BiffException;

// upload user from a text file to wizBank (QDB)
// the text file is tab delimited and is probably exported from MS ExceL
// update: use encoding from ini file, excel can no longer specify encoding in each record

public class UploadEnrollment{
    public static final String ENTERPRISE_START_TAG = "<enterprise>";
    
    public static final String WIZBANK_NAME_SPACE = "http://www.wizbank.com";
    public static final String FROM_UPLOAD = "UPLOAD";
    public static final int COL_CNT = 8;
    
    // global connection object
    public Connection con_;
            
    public String enc_;

    public String iniFile_;
//    public roleList cwIni;
    public cwIniFile cwIni; 
    // site id of the organization
    public long site_id_;
    
    public loginProfile prof_;
    // upload record count in the file
    public int upload_count;
    
    // column delimiter used in the input file
    private static final char colDelimiter_ = '\t';
    // line delimiters used in the input file (only valid for MSDOS)
    private static final char lineDelimiter1_ = '\r';
    private static final char lineDelimiter2_ = '\n';
    private static final String data_sheet_name = "Data";
    // a non-null string of zero length
    private static final String nonNullStr_ = "";
    // system line break string
    private static final String NEWL_ = System.getProperty("line.separator");
    public static final String CREDIT_VALUE_MAX = Integer.MAX_VALUE + "";
    
    private boolean bool_enrollment_distinct_check_enable_ = true;
    
    private Vector validItemVec = new Vector();
    private Vector unknownColNameVec = new Vector();
    
    private Vector sortedTimestampVec = new Vector(); //Used to sorted the enrollment record in the vector
    
    public Hashtable h_status_count; 
    
    public Hashtable htParseResult = null;
    public static String PARSE_SUCCESS = "SUCCESS";
    public static String PARSE_FAILURE_MISSING_REQUIRED_FIELD = "MISSING_REQUIRED_FIELD";
    public static String PARSE_FAILURE_USER_NOT_EXIST = "USER_NOT_EXIST";
    public static String PARSE_FAILURE_USER_NOT_TARGET = "USER_NOT_TARGET";
    public static String PARSE_FAILURE_ITEM_NOT_EXIST = "ITEM_NOT_EXIST";
    public static String PARSE_FAILURE_ITEM_NOT_ENROLL_LEVEL = "ITEM_NOT_ENROLL_LEVEL";
    public static String PARSE_FAILURE_ITEM_NOT_INCHARGE = "ITEM_NOT_INCHARGE";
    public static String PARSE_FAILURE_INVALID_COLUMN = "INVALID_COLUMN";
    public static String PARSE_FAILURE_DUPLICATED_ENROLLMENT = "DUPLICATED_ENROLLMENT";
    public static String PARSE_FAILURE_INVALID_ENROLLMENT_STATUS = "INVALID_ENROLLMENT_STATUS";
    public static String PARSE_FAILURE_INVALID_COMPLETION_STATUS = "INVALID_COMPLETION_STATUS";
    public static String PARSE_FAILURE_INVALID_COMPLETION_DATE = "INVALID_COMPLETION_DATE";
    public static String PARSE_FAILURE_ENROLLMENT_N_COMPLETION_STATUS_NOT_MATCH = "ENROLLMENT_N_COMPLETION_STATUS_NOT_MATCH";
    public static String PARSE_FAILURE_INVALID_CREDIT_TYPE = "INVALID_CREDIT_TYPE";
    public static String PARSE_FAILURE_INVALID_DATE_FORMAT = "INVALID_DATE_FORMAT";
    public static String PARSE_FAILURE_INVALID_DATE_ORDER = "INVALID_DATE_ORDER";
    public static String PARSE_FAILURE_INVALID_NUMBERIC_FORMAT = "INVALID_NUMBERIC_FORMAT";
    public static String PARSE_FAILURE_OTHERS = "OTHERS";
    
    //for clp upload enrollment 
    public static String PARSE_FAILURE_INVALID_USER_STATUS = "INVALID_USER_STATUS";
    public static String PARSE_FAILURE_INVALID_USER_ROLE = "INVALID_USER_ROLE";
    public static String RECORD_EXIST = "RECORD_HAD_EXIST";
    public static String INVALIDSHEET = "INVALIDSHEET";
    public static String MAXCOMMENT = "MAXCOMMENT";
    
    boolean invalidSheet = false;
    // recognized column names in input file, must be exactly the same names
    private String colName_[] = {
        "User ID", 	
       // "Learning Solution Code", 
        "Enrollment Status", 
       // "Enrollment Date", 	
        "Completion Status",
        "Completion Date",
        "Completion Remarks",
        "Score",
        "Attendance",
        "Enrollment Workflow", 
        "Send Mail",
        "Metric 1",
        "Metric 2",
        "Metric 3",
        "Metric 4",
        "Metric 5",
        };
        
    public static class EnrollmentRecord {
        String usr_id = null;
        String itm_code = null;
        String enrollment_status = null;
        String enrollment_date = null;
        String completion_status = null;
        String completion_date = null;
        String completion_comment = null;
        String assessment_result = null;
        String attendance = null;
        String enrollment_workflow = null;
        String send_mail = null;
        String figure_value_1 = null;
        String figure_value_2 = null;
        String figure_value_3 = null;
        String figure_value_4 = null;
        String figure_value_5 = null;
        
        boolean parseOK = true;
        boolean missingRequiredField = false;
        int invalidColumnNum = 0;
        boolean userNotExist = false;
        boolean userNotTarget = false;
        boolean itemNotExist = false;
        boolean itemNotEnrollLevel = false;
        boolean itemNotIncharge = false;
        boolean duplicatedEnrollment = false;
        boolean invalidEnrollmentStatus = false;
        boolean invalidCompletionStatus = false;
        boolean invalidCompletionDate = false;
        boolean invalidDateFormat = false;
        boolean invalidDateOrder = false;
        boolean invalidNumbericFormat = false;
        boolean invalidCreditType = false;
        boolean enrollNCompleteStatusNotMatch = false;
       
        
        //for clp
        boolean invalidUserStatus = false;
        boolean invalidUserRole = false;
        boolean recordHadExist = false;
        boolean MaxComment = false;
        
        String[] itemEnrollmentStatus = null;
        String[] itemAdmittedEnrollmentStatus = null;
        String[] itemInProgressEnrollmentStatus = null;
        Vector v_itemCreditSeqId = null;
        
        int lineNo = -1;
    }

    // default value of each column if that column is left blank in the input file
    // (nonNullStr_ means that the column does not provide default value and does not allow null)
    private String[] colDefValue_ = null;
    
    private static final String colInsDefValue_[] = {
        nonNullStr_, //nonNullStr_, 
        null, /*null,*/ "In Progress", null, null, null, null, nonNullStr_, "Y", null, null, null, null, null};

    // maximum number of columns supported in the input file
    private int maxCol_ = colName_.length;
    // array indicating which db columns have been specified in the input file
    // (index of entry indicates which column of the input file)
    // (value of entry indicates whether this column has been appearing in the input file)
    private boolean colUsed_[] = new boolean[maxCol_];
    // array matching columns in input file to columns in db table
    // (index of entry indicates which column of the input file)
    // (value of entry indicates the corresponding column in db table)
    private static int inColMatch_[] = null;

    // boolean for parsing the input file only
    private static boolean parseONLY_ = true;
//    public static final String UPLOAD_TYPE_INS = "ins";
//    public static final String UPLOAD_TYPE_UPD = "upd";
    
    private static final String SOURCE = "wizBank";
    
    private String uploadType;
    private String logDir_;
    Hashtable ht_creditType = null;
//    private Hashtable htAppStatusList;
//    private Hashtable htCreditTypeList;

//    private Hashtable htRoleTypeID = null;
    
    public UploadEnrollment(Connection con, String encoding, String iniFile, String logDir, loginProfile prof, String upload_type) throws cwException, IOException, SQLException{
        // set the label 
        this.colName_ = this.getLabelArray(prof);
        this.maxCol_ = this.colName_.length;
        this.colUsed_ = new boolean[maxCol_];
        
    	con_ = con;
        enc_ = encoding;
        iniFile_ = iniFile;
        cwIni = new cwIniFile(iniFile);
        prof_ = prof;
        site_id_ = prof.root_ent_id;
        
        colDefValue_ = colInsDefValue_;
        
        if (upload_type != null && upload_type.equalsIgnoreCase(IMSApplication.IMPORT_RULE_INSERT)){
            bool_enrollment_distinct_check_enable_ = true;    
        }else{
            bool_enrollment_distinct_check_enable_ = false;
        }
        this.uploadType = upload_type;
        logDir_ = IMSUtils.getLogDir(logDir, cwSQL.getTime(con_));
        htParseResult = new Hashtable();
        htParseResult.put(PARSE_SUCCESS, new Vector());
        htParseResult.put(PARSE_FAILURE_MISSING_REQUIRED_FIELD, new Hashtable());
        htParseResult.put(PARSE_FAILURE_USER_NOT_EXIST , new Vector());
        htParseResult.put(PARSE_FAILURE_USER_NOT_TARGET , new Vector());
        htParseResult.put(PARSE_FAILURE_ITEM_NOT_EXIST, new Vector());
        htParseResult.put(PARSE_FAILURE_ITEM_NOT_ENROLL_LEVEL, new Vector());
        htParseResult.put(PARSE_FAILURE_ITEM_NOT_INCHARGE, new Vector());
        // special case, use Hashtable to store invalid column, line num as key, column num as value
        htParseResult.put(PARSE_FAILURE_INVALID_COLUMN, new Hashtable());
        htParseResult.put(PARSE_FAILURE_DUPLICATED_ENROLLMENT, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_ENROLLMENT_STATUS, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_COMPLETION_STATUS, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_COMPLETION_DATE, new Vector());
        htParseResult.put(PARSE_FAILURE_ENROLLMENT_N_COMPLETION_STATUS_NOT_MATCH, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_CREDIT_TYPE, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_DATE_FORMAT, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_DATE_ORDER, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_NUMBERIC_FORMAT, new Vector());
        htParseResult.put(PARSE_FAILURE_OTHERS, new Vector());
        
        //for clp upload enrollment
        htParseResult.put(PARSE_FAILURE_INVALID_USER_STATUS, new Vector());
        htParseResult.put(PARSE_FAILURE_INVALID_USER_ROLE, new Vector());
        htParseResult.put(RECORD_EXIST, new Vector());
        htParseResult.put(MAXCOMMENT, new Vector());
//        initFinalResultList();
    }
/*    
    private void initFinalResultList() {
        site_id_
    }
  */  
    /*
    *   parse the file and generate a vector of ims obj to build the enterprise standard xml
    */
    public Vector parseFile(Connection con, String sourceFile,
            long cur_usr_ent_id, String current_role, long itm_id)
            throws cwException, cwSysMessage, SQLException {
        // public Vector parseFile(File sourceFile, long cur_usr_ent_id, String
        // current_role, long itm_id) throws cwException, cwSysMessage,
        // SQLException {

        try {
            // if( !cwUtils.isValidEncodedFile(sourceFile, enc_)) {
            // throw new cwSysMessage("GEN008");
            // }
            aeItem item = new aeItem();
            item.itm_id = itm_id;
            item.getItem(con);

            Vector vtEnrollment = new Vector();
            File inputWorkbook = new File(sourceFile);
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(data_sheet_name);
            if (sheet != null) {
                Cell cell[] = sheet.getRow(0);

                // BufferedReader in = new BufferedReader(new
                // InputStreamReader(new FileInputStream(sourceFile), enc_));
                // first line of input file, should be column labels and must
                // exist
                // String inline = in.readLine();
                // if (inline == null) {
                // in.close();
                // throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
                // }

                try {
                    parseCol(cell);
                } catch (Exception e) {
                	CommonLog.error(e.getMessage(),e);
                    throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_HEDADER);
                }

                String col_value = null;
                EnrollmentRecord enroll = null;
                int incount = 0, okcount = 0;
                // The first line is header row
                int linenum = 1;

                Timestamp curTime = new Timestamp(System.currentTimeMillis());
                Calendar cal = Calendar.getInstance();
                cal.setTime(curTime);
                int row = sheet.getRows();

                // while ((temp = in.read()) != -1) {
                for (int i = 1; i < row; i++) {
                    linenum++;
                    enroll = new EnrollmentRecord();
                    cell = sheet.getRow(i);
                    //过滤空行
                    String[] tem_row_array = new String[cell.length];
                    for (int colidx = 0; colidx < cell.length; colidx++) {
                        String temp_value = cell[colidx].getContents();
                        if(temp_value != null){
                            tem_row_array[colidx] = temp_value;
                        }else{
                            tem_row_array[colidx] = "";
                        }
                    }
                    if(UploadUtils.isRowEmpty(tem_row_array)){
                        continue;
                    }
                      
                    // column break?
                    // read in each character and reconstruct each field
                    int incolcnt =  cell.length;
                    for (int colidx = 0; colidx < COL_CNT; colidx++) {
                        if(colidx < incolcnt){
                            col_value = cell[colidx].getContents();
                            
                            if (cell[colidx].getType() == CellType.DATE) 
                            { 
                              DateCell dc = (DateCell) cell[colidx]; 
                              TimeZone gmtZone = TimeZone.getTimeZone("GMT");
                              SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                              format.setTimeZone(gmtZone);
                              col_value = format.format(dc.getDate());        
                            } 




                        }else{
                            col_value = "";
                        }
                        
                        if (enroll.parseOK) {
                            if(colidx == 5 && item.itm_integrated_ind){
                                
                            }else{
                                parseField(col_value, linenum, colidx, enroll);
                            }
                            
                            if(enroll.enrollment_workflow != null && enroll.enrollment_workflow.equalsIgnoreCase("Y")){
                                //enroll.enrollment_date = curTime.toString();
                                parseField("", linenum, 2, enroll);
                                //enroll.completion_status = "I";
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    // enrollment date get the current time
                    //enroll.enrollment_date = curTime.toString();
                    enroll.itm_code = item.itm_code;
                    // line break?
                    // there is a second character for line break (for MSDOS
                    // only)
                    incount++;
                    enroll.lineNo = linenum + 1;
                    if (enroll.parseOK) {
                        finalCheck(enroll, cur_usr_ent_id, current_role,
                                vtEnrollment, item);
                    }
                    if (enroll.parseOK) {
                        okcount++;
                        //int index = getVecPosition(enroll);
                        //vtEnrollment.add(index, enroll);
                        vtEnrollment.add(enroll);
                    } else {
                        Vector v_tmp;
                        Hashtable ht_tmp;
                        // failVec.addElement(new Long(linenum));
                        if (enroll.missingRequiredField) {
                            
                            ht_tmp = (Hashtable) htParseResult
                            .get(PARSE_FAILURE_MISSING_REQUIRED_FIELD);
                    ht_tmp.put(new Long(linenum), new Long(
                            enroll.invalidColumnNum));
                    
                            //v_tmp = (Vector) htParseResult
                                   // .get(PARSE_FAILURE_MISSING_REQUIRED_FIELD);
                           // v_tmp.addElement(new Long(linenum));
                        } else if (enroll.userNotExist) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_USER_NOT_EXIST);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.userNotTarget) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_USER_NOT_TARGET);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.itemNotExist) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_ITEM_NOT_EXIST);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.itemNotEnrollLevel) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_ITEM_NOT_ENROLL_LEVEL);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.itemNotIncharge) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_ITEM_NOT_INCHARGE);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidColumnNum > 0) {
                            ht_tmp = (Hashtable) htParseResult
                                    .get(PARSE_FAILURE_INVALID_COLUMN);
                            ht_tmp.put(new Long(linenum), new Long(
                                    enroll.invalidColumnNum));
                        } else if (enroll.duplicatedEnrollment) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_DUPLICATED_ENROLLMENT);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidEnrollmentStatus) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_ENROLLMENT_STATUS);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidCompletionStatus) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_COMPLETION_STATUS);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidCompletionDate) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_COMPLETION_DATE);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.enrollNCompleteStatusNotMatch) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_ENROLLMENT_N_COMPLETION_STATUS_NOT_MATCH);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidCreditType) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_CREDIT_TYPE);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidDateFormat) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_DATE_FORMAT);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidDateOrder) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_DATE_ORDER);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidNumbericFormat) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_NUMBERIC_FORMAT);
                            v_tmp.addElement(new Long(linenum));
                        }
                        // for clp
                        else if (enroll.invalidUserRole) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_USER_ROLE);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.invalidUserStatus) {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_INVALID_USER_STATUS);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.recordHadExist) {
                            v_tmp = (Vector) htParseResult.get(RECORD_EXIST);
                            v_tmp.addElement(new Long(linenum));
                        } else if (enroll.MaxComment) {
                            v_tmp = (Vector) htParseResult
                            .get(MAXCOMMENT);
                            v_tmp.addElement(new Long(linenum));
                        }

                        else {
                            v_tmp = (Vector) htParseResult
                                    .get(PARSE_FAILURE_OTHERS);
                            v_tmp.addElement(new Long(linenum));
                        }
                    }

                    // enroll = new EnrollmentRecord();

                }
            } else {
                invalidSheet = true;
            }
            w.close();                        
            return vtEnrollment;
        } catch (BiffException e) {
            throw new cwSysMessage("GEN009");
            // throw new cwException("file error:" + e.getMessage() + " not
            // found.");
        } catch (IOException e) {
            throw new cwException("read file error:" + e.getMessage());
        }
    }
    
    public boolean genIMSEnterpriseXML(Vector vtEnrollmentRecord, String xmlFilename) throws SQLException, JAXBException, cwSysMessage{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        org.imsglobal.enterprise.cwn.ObjectFactory cwnobjFactory = new org.imsglobal.enterprise.cwn.ObjectFactory();

        

        String xml = null;
        List myMembershipList = null;
        List myMemberList = null;
        List myRoleList = null;
        List myFinalResultList = null;
        SourcedidType mySourcedid = null;
        MemberType myMember = null;
        RoleType myRole = null;
        org.imsglobal.enterprise.ExtensionType myExtension = null;
        org.imsglobal.enterprise.cwn.ExtensionType cwnExtension = null;
        	    
        Enterprise myEnterprise = objFactory.createEnterprise();

        IMSEnterprise myIMSEnterprise = new IMSEnterprise(myEnterprise);
        

        PropertiesType properties = objFactory.createPropertiesType();
        properties.setDatasource(SOURCE);
        try {
            properties.setDatetime(cwSQL.getTime(con_).toString());
        } catch (SQLException e) {
        	CommonLog.error(e.getMessage(),e);
        }
        myEnterprise.setProperties(properties);
        
        myMembershipList = myEnterprise.getMembership();
        
//        IMSLearningResult.setNotAttemptedComment(cwIni.getValue("NOT_ATTEMPTED_COMMENT"));
//        IMSLearningResult.setIncompleteComment(cwIni.getValue("INCOMPLETED_COMMENT"));
//        IMSLearningResult.setCompletedComment(cwIni.getValue("COMPLETED_COMMENT"));
//        IMSLearningResult.setPassedComment(cwIni.getValue("PASSED_COMMENT"));
//        IMSLearningResult.setFailedComment(cwIni.getValue("FAILED_COMMENT"));
        
        // itm_code as key
        //Hashtable htApplication = new Hashtable();
        for (int i=0; i<vtEnrollmentRecord.size(); i++){
            EnrollmentRecord enroll = (EnrollmentRecord) vtEnrollmentRecord.elementAt(i);
            CommonLog.debug(enroll.usr_id+":"+enroll.enrollment_workflow);
            IMSApplication imsApplication = null;
            //imsApplication = (IMSApplication)htApplication.get(enroll.itm_code);
            //if (imsApplication == null){
                imsApplication = new IMSApplication();
                MembershipType appMembership = imsApplication.getMembership();
                mySourcedid = IMSUtils.createSourcedid(enroll.itm_code);
               // mySourcedid = IMSUtils.createSourcedid(item.itm_code);
                appMembership.setSourcedid(mySourcedid);
                //htApplication.put(enroll.itm_code, imsApplication);
            //}
            myMemberList = appMembership.getMember();
            myMember = objFactory.createMemberType();
            myMemberList.add(myMember);
            mySourcedid = IMSUtils.createSourcedid(enroll.usr_id);
            myMember.setSourcedid(mySourcedid);
            //1 indicate the member is a person
            myMember.setIdtype("1");

            myRoleList = myMember.getRole();
            myRole = objFactory.createRoleType();
            myRoleList.add(myRole);
            //01 indicate learner or student
            myRole.setRoletype("01");
            //1 indicate active user
            myRole.setStatus("1");
            
            myFinalResultList = myRole.getFinalresult();
//          if (enroll.enrollment_status != null){
            if (colUsed_[2]){
                myFinalResultList.add(genEnrollmentStatusFinalResult(enroll.enrollment_status, enroll.itemEnrollmentStatus));
            }  
            
//            if (enroll.completion_status!=null || enroll.completion_comment != null){
            
            if (enroll.completion_status!=null){
                myFinalResultList.add(genCompletionStatusFinalResult(enroll.completion_status, enroll.completion_comment));
            } else if (enroll.enrollment_workflow != null){
                myFinalResultList.add(genCompletionStatusFinalResult(aeAttendanceStatus.STATUS_TYPE_PROGRESS, enroll.completion_comment));                             
            }                
//            if (enroll.assessment_result!=null ){
            if (enroll.completion_status!=null && colUsed_[7]){
                myFinalResultList.add(genAssResultFinalResult(enroll.assessment_result));
            }
//            if (enroll.attendance_rate!=null ){
            if (enroll.completion_status!=null && colUsed_[8]){
                myFinalResultList.add(genAttRateFinalResult(enroll.attendance));
            }
            if (enroll.figure_value_1 != null){
//            if (colUsed_[9]){
                myFinalResultList.add(genCreditFinalResult(1, enroll.figure_value_1));
            }
            if (enroll.figure_value_2 != null){
//            if (colUsed_[10]){
                myFinalResultList.add(genCreditFinalResult(2, enroll.figure_value_2));
            }
            if (enroll.figure_value_3 != null){
//              if (colUsed_[11]){
                myFinalResultList.add(genCreditFinalResult(3, enroll.figure_value_3));
            }
            if (enroll.figure_value_4 != null){
//            if (colUsed_[14]){
                myFinalResultList.add(genCreditFinalResult(4, enroll.figure_value_4));
            }
            if (enroll.figure_value_5 != null){
//            if (colUsed_[15]){
                myFinalResultList.add(genCreditFinalResult(5, enroll.figure_value_5));
            }
            //add two extension: enrollmentworkflow and sendmail
            if (enroll.completion_date !=null || enroll.enrollment_date !=null || enroll.enrollment_workflow != null || enroll.send_mail != null){
//            if (colUsed_[5] || colUsed_[3]){
                myExtension = objFactory.createExtensionType();
                cwnExtension = cwnobjFactory.createExtensionType();
                if (enroll.completion_date !=null)
//                if (colUsed_[5])
                    cwnExtension.setCompletiondate(IMSUtils.convertTimestampToISO8601(Timestamp.valueOf(enroll.completion_date)));
                if (enroll.enrollment_date !=null)
//                if (colUsed_[3])
                    cwnExtension.setEnrollmentdate(IMSUtils.convertTimestampToISO8601(Timestamp.valueOf(enroll.enrollment_date)));
                if (enroll.enrollment_workflow !=null)
//                if (colUsed_[3])
                    cwnExtension.setEnrollmentworkflow(enroll.enrollment_workflow);
                if (enroll.send_mail !=null)
//                if (colUsed_[3])
                    cwnExtension.setSendmail(enroll.send_mail);
                myExtension.setExtension(cwnExtension);
                myRole.setExtension(myExtension);
            }
            myMembershipList.add(appMembership);
        }
        
        //Enumeration enumeration = htApplication.elements(); 
        //int k=0;
        //while(enumeration.hasMoreElements()) {
        //    myMembershipList.add(enumeration.nextElement());
        //}

        try{
            OutputStream fOut = new FileOutputStream(xmlFilename);
	        try {
                JAXBContext jc = JAXBContext.newInstance(IMSEnterpriseApp.allSchemaContext, this.getClass().getClassLoader());
                Marshaller m = jc.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.marshal(myEnterprise, fOut);
	        } finally {
	            fOut.close();
	            return false;
	        }	
        }catch (FileNotFoundException e){
        	CommonLog.error(e.getMessage(),e);
        }catch (IOException e){
        	CommonLog.error(e.getMessage(),e);
        }

        return true;
    }
    
    public String cookEnrollment(String xmlFilename, qdbEnv static_env, long ilg_id, WizbiniLoader wizbini, long itm_id, loginProfile prof) throws IOException, cwException, JAXBException, cwSysMessage, SQLException{
        String xml = null;
        // do the user upload
        JAXBContext jc = JAXBContext.newInstance(IMSEnterpriseApp.allSchemaContext, this.getClass().getClassLoader());
        Unmarshaller unmar = jc.createUnmarshaller();
        unmar.setValidating(true);
            
	    // Unmarshal the xml file to an Enterprise object
        Enterprise _enterprise = (Enterprise)unmar.unmarshal(new File(xmlFilename));
        IMSEnterprise imsEnterprise = new IMSEnterprise(_enterprise);

        File logFileDir = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
        if(!logFileDir.exists()) {
                logFileDir.mkdir();
        }
        logFileDir = new File(logFileDir, IMSLog.FOLDER_IMSLOG);
        if( !logFileDir.exists() )
                logFileDir.mkdir();
        IMSUtils.getLogDir(logFileDir.getAbsolutePath(), ilg_id);
            
        cwIniFile cwIni = new cwIniFile(iniFile_);
//        loginProfile wbProfile = BatchUtils.getProf(con_, site_id_, cwIni, wizbini);        
        Hashtable h_result = imsEnterprise.updDB(con_, cwIni, prof_, site_id_, IMSEnterprise.TYPE_RESULT, uploadType, true, FROM_UPLOAD);
        h_status_count = (Hashtable)h_result.get(IMSEnterprise.TYPE_USER_COURSE);
        xml = IMSLog.getResultXML(static_env, ilg_id, (Hashtable)h_result.get(IMSEnterprise.TYPE_USER_COURSE));
        xml = xml +  aeItem.getItemXMLForNav (con_,itm_id);
        xml = xml +  aeItem.genItemActionNavXML(con_, itm_id, prof);
        return xml;
    }
    
    // parse the column line and get the sequence of the columns in the input file
    // comparing to that of the db table, such that the column order of the input file
    // does not have to follow exactly the order in the db table
    
    //private void parseCol(String colLine) throws cwException {Sheet sheet
    private void parseCol(Cell cell[]) throws cwException {

        //colLine = colLine + colDelimiter_;
            
            // initialize the column validating stuff

        //int incolcnt = countToken(colLine, colDelimiter_);
        boolean statusOK = true;
        int incolcnt =  cell.length;
        if(incolcnt < COL_CNT){
            statusOK = false;
        }
            inColMatch_ = new int[incolcnt];
            for (int k = 0; k < incolcnt; k++)
                inColMatch_[k] = -1;
            for (int k = 0; k < maxCol_; k++)
                colUsed_[k] = false;

            
        int thisColIdx = 0, colCnt = 0;
            String colName;
            
            // extract all the column names specified in the column line
        for (int k = 0; k < cell.length; k++) {
                // next column name exists, get it
                colCnt++;
           // colName = colLine.substring(i, j);
            colName = cell[k].getContents();
                // validate this column name
                if (colName.length() == 0) {
                	CommonLog.info("blank column name at column (" + colCnt + "). upload will be continued but all data from this column will be ignored.");
                } else {
                    // check if this column name is one of the standard column names
                    for (thisColIdx = 0; thisColIdx < maxCol_; thisColIdx++)
                        if (colName.equals(colName_[thisColIdx]))
                            break;

                    if (thisColIdx >= maxCol_) {
                    	CommonLog.info("unknown column name \"" + colName + "\" at column (" + colCnt + "). upload will be stopped.");
                        unknownColNameVec.addElement(colName);
                        // ignore this non-standard column
                        continue;
//                        statusOK = false;
                    } else if (colUsed_[thisColIdx]) {
                    	CommonLog.info("duplicated column name \"" + colName + "\" at column (" + colCnt + "). upload will be stopped.");
                        statusOK = false;
                    } else {
                        // save the order
                        inColMatch_[colCnt - 1] = thisColIdx;
                        // and indicate that it has appeared
                        colUsed_[thisColIdx] = true;
                        //initFileFormat(thisColIdx);
                    }
                }
            }

            // check if there lacks any non nullable columns (without default value)
       // for (int k = 0; k < maxCol_; k++) {incolcnt
            for (int k = 0; k < maxCol_; k++) {
                if (!colUsed_[k] && colDefValue_[k] != null && colDefValue_[k].equals(nonNullStr_)) {
                	CommonLog.info("column \"" + colName_[k] + "\" must be supplied. upload will be stopped.");
                    statusOK = false;
                }
            }
      
            
            if (!statusOK) throw new cwException("parse column names failed.");
            CommonLog.info("column names processed.");

    }
    
    public StringBuffer getUsedColAsXML(){
        StringBuffer xml = new StringBuffer();
        xml.append("<used_column>");
        for (int k = 0; k < inColMatch_.length; k++){
            if (inColMatch_[k] >= 0){
                xml.append("<column id=\"" + inColMatch_[k] + "\">").append(colName_[inColMatch_[k]]).append("</column>");                
            }
        }
        xml.append("</used_column>");
        return xml;
    } 
    
    // count the number of tokens within the input line
    private static int countToken(String inString, char inDelimiter) {
        int i = 0, j = 0, cnt = 0;
        if (inString == null) return cnt;
        while ((j = inString.indexOf(inDelimiter, i)) >= 0) {
            cnt++;
            i = j + 1;
        }
        return cnt;
    }
    
    // validate an input field according to the order in its row, and
    // depending on the matched column order, assign the field value to corresponding field in the construct
    private void parseField(String fieldvalue, int lineNo, int colIdx, EnrollmentRecord enroll) {
        try {
            if (inColMatch_[colIdx] == -1) return;

            fieldvalue = UploadUtils.escString(fieldvalue.trim());
            // get the default value of this column
            String defValue = colDefValue_[inColMatch_[colIdx]];
            // check if it is from a non-nullable column
            if (defValue != null && defValue.equals(nonNullStr_) && fieldvalue.length() == 0) {
                enroll.missingRequiredField = true;
                throw new cwException("non-null string expected");
            }    
            // give the default value if the input field is empty
            if (fieldvalue.length() == 0)
                fieldvalue = defValue;
            // save this field
            
            putField(fieldvalue, enroll, inColMatch_[colIdx], getCompletionStatusList());
        } catch (Exception e) {
            enroll.parseOK = false;
            CommonLog.error("error at column (" + (colIdx + 1) + "):" + e.getMessage() + ".");
            enroll.invalidColumnNum = colIdx + 1;
            CommonLog.error(e.getMessage(),e);
        }
    }
    
    private void finalCheck(EnrollmentRecord enroll, long cur_usr_ent_id, String current_role, Vector vtEnrollment, aeItem item) throws SQLException, cwException{
        try {
            Timestamp curTime = new Timestamp(System.currentTimeMillis());
            AccessControlWZB acl = new AccessControlWZB();
            aeItem itm = new aeItem();
            itm.itm_code = enroll.itm_code;
            itm.itm_owner_ent_id = site_id_;
            long itm_id = itm.getItemId(con_);
            long usr_ent_id = dbRegUser.getEntId(con_, enroll.usr_id, site_id_); 
            //long usr_ent_id = dbRegUser.getEntId( con_,  enroll.usr_id);
            
            /*
            for (int i = 0; i < vtEnrollment.size(); i++) {
                EnrollmentRecord temp_enroll = (EnrollmentRecord)vtEnrollment.get(i);
                if(temp_enroll.login_id.endsWith(enroll.login_id)){
                    enroll.parseOK = false;
                    enroll.recordHadExist = true;
                    return;
                }
            }
            */
            if (usr_ent_id == 0){
                enroll.parseOK = false;
                enroll.userNotExist = true;
                return;
            } else {
                /*
                // check if the user is Target Learner of this Item
                if (itm_id != 0){
                    if (!aeItem.isTargetedLearner(con_, usr_ent_id, itm_id, false)){
                        enroll.parseOK = false;
                        enroll.userNotTarget = true;
                        return;
                    }
                }
                */
                
            }
            usr_ent_id = dbRegUser.isActive(con_, enroll.usr_id, site_id_);
            if(usr_ent_id == 0){
                enroll.parseOK = false;
                enroll.invalidUserStatus = true;
                return;
            }else if(!AccessControlWZB.hasRole(con_, usr_ent_id, AccessControlWZB.ROL_EXT_ID_NLRN)){
                enroll.parseOK = false;
                enroll.invalidUserRole = true;
            }
            if (itm_id == 0){
                enroll.parseOK = false;
                enroll.itemNotExist = true;
                return;
            }else{
                itm.get(con_);
                if (itm.itm_create_run_ind){
                enroll.parseOK = false;
                enroll.itemNotEnrollLevel = true;
                return;
                }
                //check if the current_role is LCA
                /*
                if (acl.hasUserPrivilege(con_, cur_usr_ent_id, current_role, AcItem.FTN_ITM_MGT_IN_TCR)){
                    AcItem aci = new AcItem(con_);
                    //check if the item is incharged by the LCA
                    if (!aci.hasItmInChargeTcrPrivilege(itm_id, itm.itm_owner_ent_id, cur_usr_ent_id, current_role)){
                        enroll.parseOK = false;
                        enroll.itemNotIncharge = true;
                        return;                 
                    }
                }
                */
                enroll.itemEnrollmentStatus = aeQueueManager.getAllProcessStatus(con_, itm_id);
                enroll.itemAdmittedEnrollmentStatus = aeQueueManager.getProcessStatus(con_, itm_id, aeApplication.ADMITTED);
                String[] pending_status = aeQueueManager.getProcessStatus(con_, itm_id, aeApplication.PENDING);
                String[] waiting_status = aeQueueManager.getProcessStatus(con_, itm_id, aeApplication.WAITING);
                enroll.itemInProgressEnrollmentStatus = new String[pending_status.length + waiting_status.length];
                int i=0;
                // add pending_status to itemAdmittedEnrollmentStatus
                while (i<pending_status.length){
                    enroll.itemInProgressEnrollmentStatus[i] = pending_status[i];
                    i++;
                }
                // add waiting_status to itemAdmittedEnrollmentStatus
                while (i<enroll.itemInProgressEnrollmentStatus.length){
                    enroll.itemInProgressEnrollmentStatus[i] = waiting_status[i-pending_status.length];
                    i++;
                }
                
                enroll.v_itemCreditSeqId = getCreditSeqId(con_, itm_id);
                validItemVec.addElement(itm);
            }
            // not throw exception for not exist user / item   
            /*
            if (bool_enrollment_distinct_check_enable_ == true) {
            // if exist. it is not insert!
                if (aeApplication.isExist(con_, itm_id, usr_ent_id)){
                    enroll.parseOK = false;
                    enroll.duplicatedEnrollment = true;
                    return; 
                } 
            }
            */
            // check if the enrollment status is valid
            if (enroll.enrollment_status != null){
                if (!cwUtils.strArrayContains(enroll.itemEnrollmentStatus, enroll.enrollment_status)){
                    enroll.parseOK = false;
                    enroll.invalidEnrollmentStatus = true;
                    return; 
                } 
            }
            // must have conmpletion status of the enrollment record has completion date, completion_comment,  cov_score, att_rate, or any credit type
            if (enroll.completion_status == null){
                
                if (enroll.completion_date != null || enroll.completion_comment != null || enroll.assessment_result != null 
                    || enroll.attendance != null || enroll.figure_value_1 != null || enroll.figure_value_2 != null 
                    || enroll.figure_value_3 != null || enroll.figure_value_4 != null || enroll.figure_value_5 != null )
                {
                    enroll.parseOK = false;
                    enroll.invalidCompletionStatus = true;
                }
            }else{
                // enrollment system status must be "admitted" when the completion status have value
                //enrollment status is not null
                if (enroll.enrollment_status != null){
                if (!cwUtils.strArrayContains(enroll.itemAdmittedEnrollmentStatus, enroll.enrollment_status)){
//                    System.out.println("not in '" + cwUtils.array2list(enroll.itemAdmittedEnrollmentStatus) + "'");
                    enroll.parseOK = false;
                    enroll.enrollNCompleteStatusNotMatch = true;
                    }
                }
            }
            long lastAppId = aeApplication.getLatestSesAppId(con_, itm_id, usr_ent_id);
            if (lastAppId > 0){
                if(!item.itm_retake_ind ) {                    //if has retake conflict, return false
                    enroll.parseOK = false;
                    enroll.duplicatedEnrollment = true;
                    return;
                }
                if(!IMSApplication.isEnrollable( con_,  usr_ent_id,   item,  lastAppId,  enroll.enrollment_workflow, enroll.completion_status)){  
                        enroll.parseOK = false;
                        enroll.duplicatedEnrollment = true;
                        return;
                }
            }
            
            // check the user is admitted to other run or not if upload a in-process attendance or application
            if( ( enroll.completion_status != null && enroll.completion_status.equalsIgnoreCase("I") ) ||
                ( enroll.enrollment_status != null && cwUtils.strArrayContains(enroll.itemInProgressEnrollmentStatus, enroll.enrollment_status)) ){
                if(enroll.completion_date != null){
                    enroll.parseOK = false;
                    enroll.invalidCompletionDate = true;
                    return;
                }
                /*
                aeItemRelation aeIre = new aeItemRelation();
                aeIre.ire_child_itm_id = itm_id;
                long parent_itm_id = aeIre.getParentItemId(con_);
//System.out.println(usr_ent_id + " : " +  parent_itm_id + " : " +  itm_id + " : " +  aeApplication.getAppId(con_, parent_itm_id, usr_ent_id, true, itm_id));
                if( aeApplication.getAppId(con_, parent_itm_id, usr_ent_id, true, itm_id) > 0 ) {
                    enroll.parseOK = false;
                    enroll.duplicatedEnrollment = true;
                    return; 
                }
                */
            } else {
                if (enroll.completion_date == null || enroll.completion_date.length() == 0){
                    enroll.completion_date = curTime.toString();
                }
            }             
//            if (colUsed_[9] || colUsed_[10] || colUsed_[11] || colUsed_[12] || colUsed_[13] ){
//                checkCreditType(enroll);
//            }
          if (enroll.figure_value_1 != null){
                if (!enroll.v_itemCreditSeqId.contains(new Long(1))){
                    enroll.parseOK = false;
                    enroll.invalidCreditType = true;
                    return; 
                } 
            }
            if (enroll.figure_value_2 != null){
                if (!enroll.v_itemCreditSeqId.contains(new Long(2))){
                    enroll.parseOK = false;
                    enroll.invalidCreditType = true;
                    return; 
                } 
            }
            if (enroll.figure_value_3 != null){
                if (!enroll.v_itemCreditSeqId.contains(new Long(3))){
                    enroll.parseOK = false;
                    enroll.invalidCreditType = true;
                    return; 
                } 
            }
            if (enroll.figure_value_4 != null){
                if (!enroll.v_itemCreditSeqId.contains(new Long(4))){
                    enroll.parseOK = false;
                    enroll.invalidCreditType = true;
                    return; 
                } 
            }
            if (enroll.figure_value_5 != null){
                if (!enroll.v_itemCreditSeqId.contains(new Long(5))){
                    enroll.parseOK = false;
                    enroll.invalidCreditType = true;
                    return; 
                } 
            }
            // for the enrollment date get the default current time ,do not to check the it with the completion date
            /*
            if( enroll.enrollment_date != null && enroll.completion_date != null ) {
                if( Timestamp.valueOf(enroll.enrollment_date).after(Timestamp.valueOf(enroll.completion_date)) ) {
                    enroll.parseOK = false;
                    enroll.invalidDateOrder = true;
                    return;
                }
            }
            */
            if( enroll.completion_comment != null && enroll.completion_comment.length() > 200 ) {
                    enroll.parseOK = false;
                    enroll.MaxComment = true;
                    return;
            }
        }catch (Exception e) {
        	CommonLog.error(e.getMessage(),e);
            enroll.parseOK = false;
        }                     
                  
    }
    
    /*
    *   this Year == betwen 00 & 99
    *   
    */
    
    private void putField(String inValue, EnrollmentRecord enroll, int inColIdx, Vector v_completion_status) throws cwException {
        try {
            Timestamp curTime = new Timestamp(System.currentTimeMillis());
            switch (inColIdx) {
                case 0:
                    enroll.usr_id = inValue;
                    break;
                //case 1:
                   // enroll.itm_code = inValue;
                   // break;
                case 1:
//                    enroll.enrollment_status = UploadUtils.checkList(inValue, v_enrollment_status);
                    enroll.enrollment_status = inValue;
                    break;
                    /* delete the enrollment date 
                case 2:
                    enroll.enrollment_date = UploadUtils.isValidDate(inValue);
                    if (enroll.enrollment_date == null || enroll.enrollment_date.length() == 0){
                        enroll.enrollment_date = curTime.toString();
                    }
//                    enroll.enrollment_date = inValue;
                    break;
                    */
                case 2:
                    enroll.completion_status = UploadUtils.checkCovStatus(inValue);
                    break;
                case 3:
                    enroll.completion_date = UploadUtils.checkValidDate(inValue);
//                    enroll.completion_date = inValue;
                    break;
                case 4:
                    enroll.completion_comment = UploadUtils.checkEnc(inValue, enc_);
                    break;
                case 5:
                    enroll.assessment_result = UploadUtils.checkDecimal(inValue, 0 ,100);
                    break;
                case 6:
                    enroll.attendance = UploadUtils.checkDecimal(inValue, 0 ,100);
                    break;
//                case 9:
//                    enroll.figure_type_1 = inValue;
//                    break;
                //enrollment workflow
                case 7:
                    enroll.enrollment_workflow = UploadUtils.checkYesOrNo(inValue);
                    break;
                //send mail
                case 8:
                    enroll.send_mail = UploadUtils.checkYesOrNo(inValue);;
                    break;
                case 9:
                    enroll.figure_value_1 = UploadUtils.checkDecimal(inValue, 0, Integer.MAX_VALUE);
                    break;
//                case 11:
//                    enroll.figure_type_2 = inValue;
//                    break;
                case 10:
                    enroll.figure_value_2 = UploadUtils.checkDecimal(inValue, 0, Integer.MAX_VALUE);
                    break;
//                case 13:
//                    enroll.figure_type_3 = inValue;
//                    break;
                case 11:
                    enroll.figure_value_3 = UploadUtils.checkDecimal(inValue, 0, Integer.MAX_VALUE);
                    break;
//                case 15:
//                    enroll.figure_type_4 = inValue;
//                    break;
                case 12:
                    enroll.figure_value_4 = UploadUtils.checkDecimal(inValue, 0, Integer.MAX_VALUE);
                    break;
//                case 17:
//                    enroll.figure_type_5 = inValue;
//                    break;
                case 13:
                    enroll.figure_value_5 = UploadUtils.checkDecimal(inValue, 0, Integer.MAX_VALUE);
                    break;
                default:
                    break;
            }
        }catch (Exception e) {
            throw new cwException("Failed to parse field.:" + e.getMessage());
        }
    }

    public static boolean isAlphanumeric(String input) {        
        return isAlphanumeric(input, null);
    }
    
    public static boolean isAlphanumeric(String input, String excludeCharacter) {        
        for (int i=0; i<input.length(); i++) {
            boolean boolIsExcluded = false;
            
            if (excludeCharacter != null && excludeCharacter.length() > 0) {
                for (int j=0; j<excludeCharacter.length(); j++) {
                    if (input.charAt(i) == excludeCharacter.charAt(j)) {
                        boolIsExcluded = true;
                    }
                }
            }
            
            if (boolIsExcluded == true) {
                continue;
            }
            
            if (Character.isLetterOrDigit(input.charAt(i)) == false) {
                return false;
            }
        }
        
        return true;
    }
    
    
    /*
    private boolean checkExtra(String extra, int maxSize, UserRecord user) {
        if (extra != null) {
            if (extra.length() > maxSize) {
                enroll.parseOK = false;
                return false;
            }
        }
        
        return true;
    }
    */
    private FinalresultType genEnrollmentStatusFinalResult(String enrollment_status, String[] s_enrollment_status) throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();

        FinalresultType finalresult = objFactory.createFinalresultType();
        finalresult.setMode(IMSApplication.RESULT_MODE_ENROLLMENT_STATUS);
        ValuesType values = objFactory.createValuesType();
        //0 indicate list of specific codes for result
        values.setValuetype("0");
        List valueList = values.getList();
        for (int i=0; i<s_enrollment_status.length; i++){
            valueList.add(s_enrollment_status[i]);
        }
        finalresult.setValues(values);
        finalresult.setResult(enrollment_status);
        return finalresult;
    }
    private FinalresultType genCompletionStatusFinalResult(String completion_status, String completion_comment) throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();

        FinalresultType finalresult = objFactory.createFinalresultType();
        finalresult.setMode(IMSApplication.RESULT_MODE_COMPLETION_STATUS);
        if (completion_status!=null){
            Vector v_completion_status = getCompletionStatusList();
            ValuesType values = objFactory.createValuesType();
            //0 indicate list of specific codes for result
            values.setValuetype("0");
            List valueList = values.getList();
            for (int i=0; i<v_completion_status.size(); i++){
                valueList.add(v_completion_status.elementAt(i));
            }
            finalresult.setValues(values);
            finalresult.setResult(completion_status);
        }
        if (completion_comment != null){
            CommentsType comments = objFactory.createCommentsType();
           // comments.setValue(cwUtils.esc4XML(completion_comment));
            comments.setValue(completion_comment);
            finalresult.setComments(comments);
        }
        return finalresult;
    }
    private FinalresultType genAssResultFinalResult(String assessment_result) throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();

        FinalresultType finalresult = objFactory.createFinalresultType();
        finalresult.setMode(IMSApplication.RESULT_MODE_ASSESSMENT_RESULT);
        ValuesType values = objFactory.createValuesType();
        values.setValuetype("1");
        values.setMin("0");
        values.setMax("100");
        finalresult.setValues(values);
        finalresult.setResult(assessment_result);
        return finalresult;
    }
    private FinalresultType genAttRateFinalResult(String attendance_rate) throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();

        FinalresultType finalresult = objFactory.createFinalresultType();
        finalresult.setMode(IMSApplication.RESULT_MODE_ATTENDANCE_RATE);
        ValuesType values = objFactory.createValuesType();
        values = objFactory.createValuesType();
        values.setValuetype("1");
        values.setMin("0");
        values.setMax("100");
        finalresult.setValues(values);
        finalresult.setResult(attendance_rate);
        return finalresult;        
    }
    private FinalresultType genCreditFinalResult(long seqId, String result) throws SQLException, JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();

        if (ht_creditType == null){
            ht_creditType = getAllCreditType();
        }
        String creditType = (String)ht_creditType.get(new Long(seqId));
        
        FinalresultType finalresult = objFactory.createFinalresultType();
        
        finalresult.setMode(IMSApplication.RESULT_MODE_ACCREDITATION_PREFIX + creditType);
        ValuesType values = objFactory.createValuesType();
        values.setValuetype("1");
        values.setMin("0");
        values.setMax(CREDIT_VALUE_MAX);
            
        finalresult.setValues(values);
        finalresult.setResult(result);
        return finalresult;
    }
    
    // return hashtable , seq id (long) as key, concatenate type + subtype as value
    private Hashtable getAllCreditType() throws SQLException{
        Hashtable ht_creditType = new Hashtable();
        DbFigureType[] figType = DbFigureType.getAll(con_, site_id_);
        
        String type;
        String subtype;
        for (int i = 0; i < figType.length; i++) {
            subtype = figType[i].fgt_subtype;
            type = cwUtils.escNull(figType[i].fgt_type);
            if (subtype!=null)
                type += IMSApplication.RESULT_MODE_ACCREDITATION_SEPARATOR + subtype;
                
            ht_creditType.put(new Long(figType[i].fgt_seq_id), type);
        }
        return ht_creditType;
    }
    
    private static Vector getCompletionStatusList(){
        Vector v_completion_status = new Vector();        
        v_completion_status.addElement("N");
        v_completion_status.addElement("I");
        v_completion_status.addElement("C");
        v_completion_status.addElement("P");
        v_completion_status.addElement("F");
        return v_completion_status;
    }
    
    public String getZipFile() throws IOException, FileNotFoundException, ZipException, cwSysMessage, cwException{
        try{
            File logDir = new File(logDir_); 
            String[] logFiles = logDir.list();
            String zipFileName = logDir.getAbsolutePath() + dbUtils.SLASH  + "EnrollLog.zip";
            dbUtils.makeZip(zipFileName, logDir.getAbsolutePath(), logFiles, false);
//            File fZipFile = new File(zipFileName);
            return "../resource/temp" + dbUtils.SLASH + logDir.getName() + dbUtils.SLASH + "EnrollLog.zip";
        }catch (qdbErrMessage e){
            throw new cwException("com.cw.wizbank.upload.uploadEnrollment.getZipFile: Error, " + e.getMessage());
        }
    }
 
    public StringBuffer uploadPreview(Connection con, File xmlFile, long itm_id, long root_ent_id, loginProfile prof) throws IOException, SQLException ,cwSysMessage{
        StringBuffer xml = new StringBuffer();
        xml.append("<upload_enrollment type=\"").append(uploadType).append("\">");
        if(!invalidSheet){
            xml.append(getUsedColAsXML());

            // total failed lines
            /*
            xml.append("<failed_lines>").append(cwUtils.NEWL);
            for (int i=0;i<failVec.size();i++) {
                xml.append("<line num=\"").append(((Long) failVec.elementAt(i)).toString())
                .append("\"/>").append(cwUtils.NEWL);
            }
            xml.append("</failed_lines>").append(cwUtils.NEWL);
            */
            //total uploaded users
            xml.append(getAttendanceStatusList( con,  root_ent_id));
            if(this.upload_count >= 0) {
                xml.append("<record_count>").append(this.upload_count).append("</record_count>");
            }
            Enumeration enumeration = htParseResult.keys(); 
    
            xml.append("<failure_list>").append(cwUtils.NEWL);
 
            while(enumeration.hasMoreElements()) {
                String failure_type = (String)enumeration.nextElement();
                if (failure_type.equals(PARSE_FAILURE_INVALID_COLUMN) || failure_type.equals(PARSE_FAILURE_MISSING_REQUIRED_FIELD)){
                    Hashtable ht_tmp = (Hashtable)htParseResult.get(failure_type);
                    if (ht_tmp.size()>0){
                        xml.append("<failure type=\"").append(failure_type).append("\">").append(cwUtils.NEWL);
                        Enumeration enum_line = ht_tmp.keys(); 
                        while(enum_line.hasMoreElements()) {
                            Long lineNum = (Long)enum_line.nextElement();
                            xml.append("<line>").append(lineNum);
                            xml.append("<column>").append(ht_tmp.get(lineNum)).append("</column>").append("</line>").append(cwUtils.NEWL);
                        }
                        xml.append("</failure>").append(cwUtils.NEWL);
                    }
                }else{
                    Vector v_tmp = (Vector)htParseResult.get(failure_type);                                        
                    if (v_tmp.size()>0){
                        xml.append("<failure type=\"").append(failure_type).append("\">").append(cwUtils.NEWL);
                        for (int i=0; i<v_tmp.size(); i++){
                            xml.append("<line>").append(v_tmp.elementAt(i)).append("</line>").append(cwUtils.NEWL);
                        }            
                        xml.append("</failure>").append(cwUtils.NEWL);
                    }
                }
            }
            xml.append("</failure_list>").append(cwUtils.NEWL);
        }else{
            xml.append("<failure_list>").append(cwUtils.NEWL);
            xml.append("<failure type=\"").append(INVALIDSHEET).append("\">").append(cwUtils.NEWL);
            xml.append("<line>").append("0").append("</line>").append(cwUtils.NEWL);
            xml.append("</failure>").append(cwUtils.NEWL);
            xml.append("</failure_list>").append(cwUtils.NEWL);
        }
       
        
        xml.append(getItemCodeReference(con));
        xml.append(unknownColNameXML());
        xml.append(getCreditTypeAsXML());
        xml.append(UploadUtils.getXMLFromIMSFile(xmlFile.getAbsolutePath(), enc_));
        xml.append(aeItem.genItemActionNavXML(con, itm_id, prof));
        xml.append("</upload_enrollment>");
        xml.append(aeItem.getItemXMLForNav ( con, itm_id ));
        return xml;
    }
    
    public String unknownColNameXML(){
        
        StringBuffer xml = new StringBuffer(512);
        xml.append("<unknown_col_list>");
        for(int i=0; i<unknownColNameVec.size(); i++)
            xml.append("<unknown_col>").append(unknownColNameVec.elementAt(i)).append("</unknown_col>");
        xml.append("</unknown_col_list>");
       
        return xml.toString();
    }
    
    private String getItemCodeReference(Connection con)
        throws SQLException{
            
            aeItemRelation aeIre = new aeItemRelation();
            Vector v_item_id = getItemId(validItemVec);
            Hashtable h_item_id = aeIre.getParentNChildId(con, v_item_id);
            Vector v_all_item_id = new Vector();
            v_all_item_id.addAll(v_item_id);
            Enumeration enumeration = h_item_id.elements();
            while(enumeration.hasMoreElements())
                v_all_item_id.addElement(enumeration.nextElement());
            
            aeItem aeItm = new aeItem();
            Hashtable h_item_title = aeItm.getItemTitle(con, v_all_item_id);
            
            
            StringBuffer xml = new StringBuffer(1024);
            Vector printedItemId = new Vector();
            xml.append("<item_code_ref_list>");
            for(int i=0; i<validItemVec.size(); i++){
                aeItm = (aeItem)validItemVec.elementAt(i);
                Long childId = new Long(aeItm.itm_id);
                if( printedItemId.indexOf( childId ) == -1 ) {
                    printedItemId.addElement( childId );
                    xml.append("<item_code_ref>")
                        .append("<code>").append(cwUtils.esc4XML(aeItm.itm_code)).append("</code>")
                        .append("<title>").append(cwUtils.esc4XML((String)h_item_title.get(childId))).append("</title>");
                    if( h_item_id.containsKey(childId) ){
                            xml.append("<parent_title>").append( cwUtils.esc4XML((String) h_item_title.get( (Long)h_item_id.get(childId) ) )).append("</parent_title>");
                    }
                    xml.append("</item_code_ref>");
                }
            }
            xml.append("</item_code_ref_list>");
            return xml.toString();
        }
    
    private Vector getItemId(Vector v_itm){
        Vector v_itm_id = new Vector();
        if( v_itm == null || v_itm.isEmpty() )
            return v_itm_id;
        for(int i=0; i<v_itm.size(); i++)
            v_itm_id.addElement(new Long( ((aeItem)v_itm.elementAt(i)).itm_id ));
        return v_itm_id;
    }
    
    public Vector getCreditSeqId(Connection con, long itm_id) throws SQLException{
        Vector v_credit_seq_id = new Vector();
        DbFigureType[] figType = ViewFigure.getItemFigureDetail(con, itm_id, 0);
        String type; 
        for (int i = 0; i < figType.length; i++) {
            v_credit_seq_id.addElement(new Long(figType[i].fgt_seq_id));
        }
        return v_credit_seq_id;
    }
 /*
    private void checkCreditType(EnrollmentRecord enroll){
        // check if the output figure is a valid figure in that item
        boolean validCreditType = false;
        validCreditType = enroll.v_itemCreditSeqId.contains(new Long(1));
        if (enroll.figure_value_1 != null){
            if (!validCreditType){
                enroll.parseOK = false;
                enroll.invalidCreditType = true;
                return; 
            } 
        }else{
            // reset the credit value of the item if the item original contain the credittype
            if (validCreditType){
                enroll.figure_value_1 = "0";
            }
        }
        validCreditType = enroll.v_itemCreditSeqId.contains(new Long(2));
        if (enroll.figure_value_2 != null){
            if (!validCreditType){
                enroll.parseOK = false;
                enroll.invalidCreditType = true;
                return; 
            } 
        }else{
            if (validCreditType){
                enroll.figure_value_2 = "0";
            }
        }
        validCreditType = enroll.v_itemCreditSeqId.contains(new Long(3));
        if (enroll.figure_value_3 != null){
            if (!validCreditType){
                enroll.parseOK = false;
                enroll.invalidCreditType = true;
                return; 
            } 
        }else{
            if (validCreditType){
                enroll.figure_value_3 = "0";
            }
        }
        validCreditType = enroll.v_itemCreditSeqId.contains(new Long(4));
        if (enroll.figure_value_4 != null){
            if (!validCreditType){
                enroll.parseOK = false;
                enroll.invalidCreditType = true;
                return; 
            } 
        }else{
            if (validCreditType){
                enroll.figure_value_4 = "0";
            }
        }
        validCreditType = enroll.v_itemCreditSeqId.contains(new Long(5));
        if (enroll.figure_value_5 != null){
            if (!validCreditType){
                enroll.parseOK = false;
                enroll.invalidCreditType = true;
                return; 
            } 
        }else{
            if (validCreditType){
                enroll.figure_value_5 = "0";
            }
        }
    }        
   */ 
    private StringBuffer getCreditTypeAsXML() throws SQLException{
        if (ht_creditType == null){
            ht_creditType = getAllCreditType();
        }
        StringBuffer xml = new StringBuffer();
        Long seqId;
        String type;
        
        xml.append("<credit_type_list>").append(cwUtils.NEWL);
        Enumeration enumeration = ht_creditType.keys(); 
        while(enumeration.hasMoreElements()) {
            seqId = (Long)enumeration.nextElement();
            type = (String)ht_creditType.get(seqId);
            xml.append("<credit_type seq_id=\"").append(seqId).append("\" type=\"").append(type).append("\" />").append(cwUtils.NEWL);
        }
        xml.append("</credit_type_list>");
        return xml;
    } 
    /*
    private int getVecPosition(EnrollmentRecord enroll){
            for(int i=0; i<sortedTimestampVec.size(); i++) {
                if( (Timestamp.valueOf(enroll.enrollment_date)).before( (Timestamp)sortedTimestampVec.elementAt(i) ) ){
                    sortedTimestampVec.add(i, Timestamp.valueOf( enroll.enrollment_date ));
                    return i;
                }
            }
            sortedTimestampVec.addElement( Timestamp.valueOf(enroll.enrollment_date) );
            return sortedTimestampVec.size() - 1;
    }
    
    */
    public  String getAttendanceStatusList(Connection con, long root_ent_id) throws SQLException {
        StringBuffer result = new StringBuffer();
        
        result.append("<attendance_status_list>").append(cwUtils.NEWL);
        result.append(aeAttendanceStatus.statusAsXML(con, root_ent_id));
        result.append("</attendance_status_list>").append(cwUtils.NEWL);

        return result.toString();
    } 

	private String[] getLabelArray(loginProfile prof) {
		String userID = "User ID", enrollmentStatus = "Enrollment Status";
		String completionStatus = "Completion Status", completionDate = "Completion Date", completionRemarks = "Completion Remarks";
		String score = "Score", attendance = "Attendance", enrollmentWorkflow = "Enrollment Workflow", sendMail = "Send Mail";
		String metric1 = "Metric 1", metric2 = "Metric 2", metric3 = "Metric 3", metric4 = "Metric 4", metric5 = "Metric 5";

		String lang = prof.cur_lan;  
		String site = prof.root_id;
		userID = ImportTemplate.getUserLabel(ImportTemplate.USER_ID_KEY, lang, site, userID);
		completionStatus = ImportTemplate.getLabelValue("wb_imp_enr_completion_status", lang, completionStatus);
		completionDate = ImportTemplate.getLabelValue("wb_imp_enr_completion_date", lang, completionDate);
		completionRemarks = ImportTemplate.getLabelValue("wb_imp_enr_completion_remarks", lang, completionRemarks);
		score = ImportTemplate.getLabelValue("wb_imp_enr_score", lang, score);
		attendance = ImportTemplate.getLabelValue("wb_imp_enr_attendance", lang, attendance);
		enrollmentWorkflow = ImportTemplate.getLabelValue("wb_imp_enr_enrollment_workflow", lang, enrollmentWorkflow);
		enrollmentStatus = ImportTemplate.getLabelValue("wb_imp_enr_enrollment_status", lang, enrollmentStatus);
		sendMail = ImportTemplate.getLabelValue("wb_imp_enr_send_mail", lang, sendMail);

		String[] labels = new String[] { userID, enrollmentStatus, completionStatus, completionDate, completionRemarks, score, attendance,
				enrollmentWorkflow, sendMail, metric1, metric2, metric3, metric4, metric5 };
		return labels;
	}
}