package com.cw.wizbank.upload;

import java.lang.Long;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.*;

import javax.xml.bind.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import com.oroinc.text.perl.*;

import org.imsglobal.enterprise.*;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.DbUserGroup;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.enterprise.*;
import com.cwn.wizbank.utils.CommonLog;


// upload user from a text file to wizBank (QDB)
// the text file is tab delimited and is probably exported from MS ExceL
// update: use encoding from ini file, excel can no longer specify encoding in each record

public class UploadUser{
    public static final String ENTERPRISE_START_TAG = "<enterprise>";
    public static final String WIZBANK_NAME_SPACE = "http://www.cyberwisdom.net";
    
    private static final String REC_STATUS_ADD = "1";
    private static final String REC_STATUS_UPDATE = "2";
    
    public static final String ROOT_GRP_ID = "ROOT";
    
    private static final char COL_DELIMITER = '\t';
    
    // global connection object
    public Connection con_;
    // the length of the generate user login id (first two char is the generate year)
    public static final int steUsrIdLength = 8;
    private static final int steUsrPwdLength = 6;    

    public String enc_;

    public String iniFile_;
    
    // site id of the organization
    public long site_id_;
    
    public loginProfile prof_;
    
    // column delimiter used in the input file
    private static final char colDelimiter_ = '\t';
    // line delimiters used in the input file (only valid for MSDOS)
    private static final char lineDelimiter1_ = '\r';
    private static final char lineDelimiter2_ = '\n';
    // a non-null string of zero length
    private static final String nonNullStr_ = "";
    // system line break string
    private static final String NEWL_ = System.getProperty("line.separator");

    private int default_grade_id_ = -1;
    private boolean bool_user_pwd_enable_ = true;
    private boolean bool_user_distinct_check_enable_ = true;

    //store the number of user to be unloaded
    private int numberOfUsers = -1;

    // store invalid line num
    private Vector failVec = new Vector();
    private Vector missingFieldFailVec = new Vector();
    private Vector missingFieldNameVec = new Vector();
    private Vector duplicatedLoginIdFailVec = new Vector();
    private Vector invalidGroupCodeVec = new Vector();
    private Vector invalidRoleVec = new Vector();
//    private Vector invalidBDayFailVec = new Vector();
//    private Vector invalidJoinDayFailVec = new Vector();
    private Vector othersFailVec = new Vector();
//    private Vector invalidGenderVec = new Vector();
    private Vector noGrpCodeNameVec = new Vector();
    private Vector invalidGradeCodeVec = new Vector();
    private Vector invalidDirectSupervisorId = new Vector();
    private Vector invalidSupervisedGroupCode = new Vector();
    private Vector invalidGroupSupervisorGroup = new Vector();
    private Vector heldByPendingAppn = new Vector();
    private Vector heldByPendingApprovalAppn = new Vector();
    private Vector invalidField = new Vector();
    private Vector invalidFieldName = new Vector();
    private Vector emptyPasswordVec = new Vector();
    //Used to cache the data for checking on each upload process
    public Vector validGroupCode = new Vector();
    public Hashtable htGroupId = new Hashtable();
    private Vector invalidGroupCode = new Vector();

    public Hashtable htValidGradeCode = new Hashtable();
    private Vector invalidGradeCode = new Vector();

    public Vector validUserId = new Vector();
    public Vector importUserId = new Vector();

    private Vector validRole = new Vector();
    private Vector invalidRole = new Vector();
   
    // reserved role type 
    private String role_type_supervise = "05";
    
    private Hashtable htRole;
    private Hashtable htRoleUid;
	
	public Hashtable h_status_count;
	
    private Vector unknownColNameVec = new Vector();
    // recognized column names in input file, must be exactly the same names
    public String StdColName_[] = {
        //user fields in import file
        "User ID", 	
        "Password",
        "Name", 
        "Gender",
        "Date of Birth",
        
        "Email",
        "Phone",
        "Fax",
        "Job Title",
        "Grade Code",
        
        "Group Code",
        "Direct Supervisor IDs",
        "Join Date",
        "Role Codes",     // map to role id
        "Supervised Group Codes",
        "Highest Approval Group",
        
        "Source",
        
        "Extra 1",
        "Extra 2",
        "Extra 3",
        "Extra 4",
        "Extra 5",
        
        "Extra 6",
        "Extra 7",
        "Extra 8",
        "Extra 9",
        "Extra 10",
        
        "Extra Datetime 11",
        "Extra Datetime 12",
        "Extra Datetime 13",
        "Extra Datetime 14",
        "Extra Datetime 15",
        "Extra Datetime 16",
        "Extra Datetime 17",
        "Extra Datetime 18",
        "Extra Datetime 19",
        "Extra Datetime 20",
        	
        "Extra Singleoption 21",
        "Extra Singleoption 22",
        "Extra Singleoption 23",
        "Extra Singleoption 24",
        "Extra Singleoption 25",
        "Extra Singleoption 26",
    	"Extra Singleoption 27",
    	"Extra Singleoption 28",
    	"Extra Singleoption 29",
    	"Extra Singleoption 30",

        "Extra Multipleoption 31",
        "Extra Multipleoption 32",
        "Extra Multipleoption 33",
        "Extra Multipleoption 34",
        "Extra Multipleoption 35",
        "Extra Multipleoption 36",
        "Extra Multipleoption 37",
        "Extra Multipleoption 38",
        "Extra Multipleoption 39",
        "Extra Multipleoption 40",
        "nickname",
        };
        
    public static class UserRecord {
        // user fields in import file
        String recStatus = null;
        String id = null;               // "User ID", 	
        String password = null;         // "Password",
        String fn = null;               // "Name", 
        String nickname = null;               // "nickname", 
        String gender = null;           // "Gender",
        String bday = null;             // "Date of Birth",
        
        String email = null;            // "Email",
        String phone = null;            // "Phone",
        String fax = null;              // "Fax",
        String job_title = null;        // "Job Title",
        String gradeCode = null;        // "Grade Code",

        String groupCode = null;        // "Group Code",
        String direct_supervisor_ids = null;    // "Direct Supervisor IDs",
        String join_date = null;        // "Join Date",   
        String role_id = null;          // "Role", 
        String supervised_group_ids = null;     // "Supervised Group Codes",
        String group_supervisors_group = null; //"Group Supervisors under this group for Approval"
        
        String usr_source = "wizBank";
        
        String extra_1 = null;          // "Extra 1",
        String extra_2 = null;          // "Extra 2",
        String extra_3 = null;          // "Extra 3",
        String extra_4 = null;          // "Extra 4",
        String extra_5 = null;          // "Extra 5",
        
        String extra_6 = null;          // "Extra 6",
        String extra_7 = null;          // "Extra 7",
        String extra_8 = null;          // "Extra 8",
        String extra_9 = null;          // "Extra 9",
        String extra_10 = null;         // "Extra 10",

        String extra_datetime_11 = null;    //"Extra Datetime 11"
        String extra_datetime_12 = null;    //"Extra Datetime 12"
        String extra_datetime_13 = null;    //"Extra Datetime 13"
        String extra_datetime_14 = null;    //"Extra Datetime 14"
        String extra_datetime_15 = null;    //"Extra Datetime 15"
        
        String extra_datetime_16 = null;    //"Extra Datetime 16"
        String extra_datetime_17 = null;    //"Extra Datetime 17"
        String extra_datetime_18 = null;    //"Extra Datetime 18"
        String extra_datetime_19 = null;    //"Extra Datetime 19"
        String extra_datetime_20 = null;    //"Extra Datetime 20"
		
		/*user extension for single choice*/
		String extra_singleoption_21 = null;
		String extra_singleoption_22 = null;
		String extra_singleoption_23 = null;
		String extra_singleoption_24 = null;
		String extra_singleoption_25 = null;
		String extra_singleoption_26 = null;
		String extra_singleoption_27 = null;
		String extra_singleoption_28 = null;
		String extra_singleoption_29 = null;
		String extra_singleoption_30 = null;
		/*user extension for multiple choice*/
		String extra_multipleoption_31 = null;
		String extra_multipleoption_32 = null;
		String extra_multipleoption_33 = null;
		String extra_multipleoption_34 = null;
		String extra_multipleoption_35 = null;
		String extra_multipleoption_36 = null;
		String extra_multipleoption_37 = null;
		String extra_multipleoption_38 = null;
		String extra_multipleoption_39 = null;
		String extra_multipleoption_40 = null;
        // internal use
        long groupId;
        long appnApprovalGroupId;
        DbUserGrade userGrade = null;
        String[] roleIds;
        String[] roleSubIds;
        String[] directSupervisorIds;
        String[] superviseGroupIds;
        boolean parseOK = true;
        String missingField = null;
        boolean duplicatedLoginID = false;
        boolean emptyPassword = false;
//        boolean invalidJoinDay = false;
        boolean invalidGroupCode = false;
        boolean invalidRole = false;
//        boolean invalidGender = false;
        boolean invalidGradeCode = false;
        boolean invalidSupervisedGroupCode = false;
        boolean invalidDirectSupervisorId = false;
        boolean invalidGroupSupervisorGroup = false;
        boolean noGrpNameCode = false;
        String invalidFieldName = null;
        boolean heldByPendingAppn = false;
        boolean heldByPendingApprovalAppn = false;
        int lineNo = -1;
    }

    // default value of each column if that column is left blank in the input file
    // (nonNullStr_ means that the column does not provide default value and does not allow null)
    private String[] StdColDefValue_ = null;
    
    private static final String StdColInsDefValue_[] = {
        nonNullStr_, null, nonNullStr_, null, null, 
        null, null, null, null, nonNullStr_, 
        nonNullStr_, null, null, nonNullStr_, null, null, null,
        null, null, null, null, null, 
        null, null, null, null, null, 
        null, null, null, null, null, 
        null, null, null, null, null, 
        null, null, null, null, null, 
        null, null, null, null, null, 
        null, null, null, null, null, 
        null, null, null, null, null, 
        nonNullStr_, 
        };

    // maximum number of columns supported in the input file
    private  int maxStdCol_ = StdColName_.length;
    // array indicating which db columns have been specified in the input file
    // (index of entry indicates which column of the input file)
    // (value of entry indicates whether this column has been appearing in the input file)
    private boolean StdColUsed_[] = new boolean[maxStdCol_];
    // array matching columns in input file to columns in db table
    // (index of entry indicates which column of the input file)
    // (value of entry indicates the corresponding column in db table)
    private static int inColMatch_[] = null;

    // boolean for parsing the input file only
    private static boolean parseONLY_ = true;
    public static final String UPLOAD_TYPE_INS = "ins";
    public static final String UPLOAD_TYPE_UPD = "upd";
    
    private static final String SOURCE = "wizBank";
    
    private String uploadType;
    
    public UploadUser(Connection con, String encoding, String iniFile, loginProfile prof, int default_grade_id, boolean bool_user_pwd_enable, boolean bool_user_distinct_check_enable) throws cwException, SQLException {
    	// set the label
		this.StdColName_ = this.getLabelArray(prof);
		this.maxStdCol_ = this.StdColName_.length;
		this.StdColUsed_ = new boolean[maxStdCol_];
    	
    	con_ = con;
        enc_ = encoding;
        iniFile_ = iniFile;
        prof_ = prof;
        site_id_ = prof.root_ent_id;
        StdColDefValue_ = StdColInsDefValue_;
        
        default_grade_id_ = default_grade_id;
        
        bool_user_pwd_enable_ = bool_user_pwd_enable;
        bool_user_distinct_check_enable_ = bool_user_distinct_check_enable;        
        
        AccessControlWZB acl = new AccessControlWZB();
        Vector authRolId = dbUtils.getAuthRole(con, prof);
        htRole = acl.getAllRoleUid(con, site_id_, "rol_ext_id");
        htRoleUid = new Hashtable();

        Enumeration enumeration = htRole.keys();
        while(enumeration.hasMoreElements()) {
            String cur_role = (String)enumeration.nextElement();
            if (authRolId.contains(cur_role)){
                if ("LRNR".equals(htRole.get(cur_role))){
                    htRoleUid.put(htRole.get(cur_role), "01");
                }else if ("INSTR".equals(htRole.get(cur_role))){
                    htRoleUid.put(htRole.get(cur_role), "07");
                }else if ("SADM".equals(htRole.get(cur_role))){
                    htRoleUid.put(htRole.get(cur_role), "07");
                }else if ("GADM".equals(htRole.get(cur_role))){
                    htRoleUid.put(htRole.get(cur_role), "07");
                }else if ("TADM".equals(htRole.get(cur_role))){
                    htRoleUid.put(htRole.get(cur_role), "07");
                }else if ("EXA".equals(htRole.get(cur_role))){
                    htRoleUid.put(htRole.get(cur_role), "07");
                }          
            }
        }

    }
    
    static public String getXML(String sourceFile, String enc) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
        String xml = "";
        String inline = null;
        while ((inline = in.readLine()) != null) {
            xml += inline;
        }
        
        int index = xml.toUpperCase().indexOf(ENTERPRISE_START_TAG.toUpperCase());
            
        xml = xml.substring(index);
        
        Perl5Util perl = new Perl5Util();
        if(perl.match("#ns1:#i", xml))
            xml = perl.substitute("s#ns1:##ig", xml);
        if(perl.match("#xmlns:cwn=\"" + WIZBANK_NAME_SPACE + "\"#i", xml))
            xml = perl.substitute("s#xmlns:ns1=\"" + WIZBANK_NAME_SPACE + "\"##ig", xml);
            
        return xml;
    }
    
    
    /*
    *   parse the file and generate a vector of ims obj to build the enterprise standard xml
    */
    public Vector parseFile(File sourceFile, WizbiniLoader wizbini) throws cwException, cwSysMessage, SQLException {
        Workbook workBook = null;
        Sheet sheet = null;
        WorkbookSettings wbSetting = new WorkbookSettings();
        wbSetting.setIgnoreBlanks(true);
        try {
        	//读取Excel文件 start
        	workBook = Workbook.getWorkbook(sourceFile, wbSetting);
        	StringBuffer sb = new StringBuffer();
        	sheet = workBook.getSheet(0);
        	if(sheet == null) {
        		throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
        	} else {
        		String[] head_column = readLineXls(sheet, 0, sheet.getColumns(), false);
        		String[] row = null;
        		int rowNum = 0;
        		int rowLimit = 999999999;
        		for(int i = 0; i < sheet.getRows(); i++) {
        			row = readLineXls(sheet, rowNum++, head_column.length, true);
        			if(UploadUtils.isRowEmpty(row)) {
        				continue;
        			}
        			if(rowNum > rowLimit) {
        				throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(rowLimit).toString());
        			}
        			for(int j = 0; j < row.length; j++) {
        				sb.append(row[j]);
        				if(j != row.length - 1) {
        					sb.append(colDelimiter_);
        				}
        				if(j == row.length - 1) {
        					sb.append(lineDelimiter1_);
        					sb.append(lineDelimiter2_);
        				}
        			}
        		}
        	}
        	//读取Excel文件end
        	
//			if( !cwUtils.isValidEncodedFile(sourceFile, cwUtils.ENC_UNICODE_LITTLE) ) {
//				throw new cwSysMessage("GEN008");
//			}
            BufferedReader in = new BufferedReader(new StringReader(sb.toString()));

            // first line of input file, should be column labels and must exist
            String inline = in.readLine();
            
            if (inline == null) {
                in.close();
                throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_FILE);
            }

//            try {
                parseCol(inline);
//            }catch (Exception e) {
                //System.out.println(e);
//                e.printStackTrace(System.out);
//                System.out.println(e);
//                throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_HEDADER);
//            }
            
            int temp;
            char inChar;
            StringBuffer term = new StringBuffer();
            int colidx = 0;
            Vector vtUserRecord = new Vector();
            UserRecord user = new UserRecord();
            int incount = 0, okcount = 0;
            // The first line is header row
            int linenum = 1;
            // read in each character and reconstruct each field
            // when a field delimiter is found, validate that field
            // when a line delimiter is found, save the record into database
            Timestamp curTime = new Timestamp(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(curTime);
            String year = cal.get(Calendar.YEAR) + "";
            year = year.substring(2);
            
            while ((temp = in.read()) != -1) {
                inChar = (char)temp;
                
                // column break?
                if (inChar == colDelimiter_ || inChar == lineDelimiter1_) {
                    if (user.parseOK) {
                        parseField(term.toString(), colidx, user);
                    }
                    if (user.parseOK){
                        importUserId.addElement(user.id);
                    }
                    term.setLength(0);
                    colidx++;
                    // line break?
                    if (inChar == lineDelimiter1_) {
                        // there is a second character for line break (for MSDOS only)
                        temp = in.read();
                        inChar = (char)temp;
                        incount++;
                        linenum++;
                        user.lineNo = linenum;
                        if (user.parseOK){
                            finalCheck(year, user, wizbini);
                        }
                        if (user.parseOK) {
                            okcount++;
                            vtUserRecord.addElement(user); 
                        }else {
                            failVec.addElement(new Long(linenum));
                            if (user.missingField != null ){
                                missingFieldFailVec.addElement(new Long(linenum));                                
                                missingFieldNameVec.addElement(user.missingField);
                            }else if (user.duplicatedLoginID == true) {
                                duplicatedLoginIdFailVec.addElement(new Long(linenum));                                
                            }else if (user.emptyPassword){
                                emptyPasswordVec.addElement(new Long(linenum));
                            }
                            else if( user.invalidGroupCode ){
                                invalidGroupCodeVec.addElement(new Long(linenum));
                            }
                            else if ( user.invalidRole ) {
                                invalidRoleVec.addElement(new Long(linenum));
                            }
                            else if( user.noGrpNameCode ){
                                noGrpCodeNameVec.addElement(new Long(linenum));
                            }
                            else if( user.invalidGradeCode ){
                                invalidGradeCodeVec.addElement(new Long(linenum));
                            }
                            else if( user.invalidSupervisedGroupCode ){
                                invalidSupervisedGroupCode.addElement(new Long(linenum));
                            }
                            else if( user.invalidGroupSupervisorGroup){
                                invalidGroupSupervisorGroup.addElement(new Long(linenum));
                            }
                            else if( user.heldByPendingAppn){
                                heldByPendingAppn.addElement(new Long(linenum));
                            }
                            else if( user.heldByPendingApprovalAppn){
                                heldByPendingApprovalAppn.addElement(new Long(linenum));
                            }
                            else if( user.invalidFieldName != null ){
                                invalidField.addElement(new Long(linenum));
                                invalidFieldName.addElement(user.invalidFieldName);
                            }
                            else {
                                othersFailVec.addElement(new Long(linenum));                                
                            }
                        }
                        colidx = 0;
                        user = new UserRecord();
                    }
                } else {
                    // delimiter not yet reached, keep on constructing the field
                    if (inChar == lineDelimiter2_) {
                        term.append(NEWL_);
                    }else {
                        term.append(inChar);
                    }
                    
                }
            }
            in.close();
            
            //reset
            UploadModule.ADD_USER_NUM = 0;
            
            // check supervisor id after all users parsed
            for (int i=0; i<vtUserRecord.size(); i++){
                user = (UserRecord) vtUserRecord.elementAt(i);
                if (user.parseOK){
                    if (!checkSupervisorIds(user)){
                        failVec.addElement(new Long(user.lineNo));
                        okcount--;  
                        invalidDirectSupervisorId.addElement(new Long(user.lineNo));
                        vtUserRecord.removeElementAt(i); 
                    }
                    if(user.recStatus == REC_STATUS_ADD){
                    	UploadModule.ADD_USER_NUM++ ;
                    }
                }
            }
            this.numberOfUsers = vtUserRecord.size();
            return vtUserRecord;
        } catch (BiffException e) {
            throw new cwSysMessage("GEN009");
        } catch (FileNotFoundException e) {
            throw new cwException("file error:" + e.getMessage() + " not found.");
        } catch (IOException e) {
            throw new cwException("read file error:" + e.getMessage());
        }
    }
    
    private String[] readLineXls(Sheet sheet, int rowNum, int columnNum, boolean emptyToNull) {
    	Vector tempColumn = new Vector();
    	Cell cell = null;
    	String content = null;
    	for (int pos = 0;pos < columnNum; pos++) {
    		cell = sheet.getCell(pos, rowNum);
    		content = cell.getContents();
    		if (content != null && content.length() > 0) {
    			tempColumn.addElement(content);
    		} else if (emptyToNull) {
    			tempColumn.addElement("");
    		}
    	}
    	return cwUtils.vec2strArray(tempColumn);
    }
    
    public boolean checkSupervisorIds(UserRecord user) throws cwException{
        boolean bValid = true;
        if( user.direct_supervisor_ids != null && user.direct_supervisor_ids.length() > 0 ){
            user.directSupervisorIds = cwUtils.splitToString(user.direct_supervisor_ids, ",", true);
            for (int i=0; i<user.directSupervisorIds.length; i++){
                dbRegUser tmpUsr = new dbRegUser();
                tmpUsr.usr_ste_usr_id = user.directSupervisorIds[i];
                if( !validUserId.contains(user.directSupervisorIds[i]) &&  !importUserId.contains(user.directSupervisorIds[i])) {
                    if (tmpUsr.checkSiteUsrIdExist(con_, site_id_)){
                        validUserId.addElement(tmpUsr.usr_ste_usr_id);
                    }else{
                        bValid = false;
                        user.invalidDirectSupervisorId = true;
                        user.parseOK = false;
                    }
                }
            }
        }
        return bValid;
    }
    
    public boolean genIMSEnterpriseXML(Vector vtUserRecord, String xmlFilename) throws JAXBException, FileNotFoundException, IOException {
long startTime = System.currentTimeMillis();
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        org.imsglobal.enterprise.cwn.ObjectFactory cwnobjFactory = new org.imsglobal.enterprise.cwn.ObjectFactory();

        String xml = null;
        List myPersonList = null;
        List myMembershipList = null;
        List myMemberList = null;
        List myRoleList = null;
        List myTelList = null;
        List myGroupList = null;
        List myGroupTypeList = null;
        List myGroupTypeValueList = null;
        List myUseridList = null;
        IMSPerson myIMSPerson = null;
        IMSGroupMember myIMSGroupMember = null;
        List mySourcedidList = null;
        SourcedidType mySourcedid = null;
        MemberType myMember = null;
        RoleType myRole = null;
        TelType myTel1 = null;
        TelType myTel2 = null;
        org.imsglobal.enterprise.cwn.ExtensionType.ExtType cwnext = null;
        
        Enterprise myEnterprise = objFactory.createEnterprise();
                
        PropertiesType properties = objFactory.createPropertiesType();
        properties.setDatasource(SOURCE);
        try {
            properties.setDatetime(dbUtils.getTime(con_).toString());
        } catch (qdbException e) {
        	CommonLog.error(e.getMessage(),e);
        }
        myEnterprise.setProperties(properties);
        
        myGroupList = myEnterprise.getGroup();
        myPersonList = myEnterprise.getPerson();
        myMembershipList = myEnterprise.getMembership();
        
        for (int i=0; i<vtUserRecord.size(); i++) {
            UserRecord myUserRecord = (UserRecord)vtUserRecord.elementAt(i);
        
            // for person
            myIMSPerson = new IMSPerson();
            PersonType person = myIMSPerson.getPerson();
            person.setRecstatus(myUserRecord.recStatus);
            mySourcedidList = person.getSourcedid();
            mySourcedid = objFactory.createSourcedidType();
            mySourcedid.setSource(myUserRecord.usr_source);
            mySourcedid.setId(myUserRecord.id);
            mySourcedidList.add(mySourcedid);
            
            // generate password when "bool_user_pwd_enable_" and password not imported
            if (myUserRecord.password == null && bool_user_pwd_enable_ ){
                int password = 0;
                while (true) {
                    password+=(new Random()).nextInt();
                    if (password >= Math.pow(10, steUsrPwdLength-1)) {
                        break;
                    }
                }
                myUserRecord.password = Integer.toString(password).substring(0, steUsrPwdLength);
            }
            
            if (myUserRecord.password != null ) {
                UseridType myUserid = objFactory.createUseridType();
                myUserid.setPassword(myUserRecord.password);
                myUseridList = person.getUserid();
                myUseridList.add(myUserid);
            }
            
            if (myUserRecord.email != null) {
                person.setEmail(myUserRecord.email);
            }else if (StdColUsed_[5]){
                person.setEmail("");
            }
            
            NameType myName = objFactory.createNameType();
            myName.setFn(myUserRecord.fn);
            myName.setNickname(myUserRecord.nickname);
            
            person.setName(myName);
            
            if (myUserRecord.bday != null  || StdColUsed_[4] 
                || myUserRecord.gender != null || StdColUsed_[3] ) {
                DemographicsType myDemographics = objFactory.createDemographicsType();
                if( myUserRecord.bday != null ){
                    myDemographics.setBday(myUserRecord.bday);
                }else if (StdColUsed_[4]){
                    myDemographics.setGender("");
                }
                if( myUserRecord.gender != null ){
                    myDemographics.setGender(myUserRecord.gender);
                }else if (StdColUsed_[3]){
                    myDemographics.setGender("");
                }
                person.setDemographics(myDemographics);
            }
            
            if (myUserRecord.phone != null || StdColUsed_[6] 
                || myUserRecord.fax != null || StdColUsed_[7] ) {
                myTelList = person.getTel();
                if (myUserRecord.phone != null || StdColUsed_[6]) {
                    String tmpStr = myUserRecord.phone;
                    if (tmpStr==null){
                        tmpStr = "";
                    }                    
                    myTel1 = objFactory.createTelType();
                    myTel1.setTeltype("1");
                    myTel1.setValue(tmpStr);            
                    myTelList.add(myTel1);
                }
                if (myUserRecord.fax != null || StdColUsed_[7]) {
                    String tmpStr = myUserRecord.fax;
                    if (tmpStr==null){
                        tmpStr = "";
                    }
                    myTel2 = objFactory.createTelType();
                    myTel2.setTeltype("2");
                    myTel2.setValue(tmpStr);            
                    myTelList.add(myTel2);
                }
            }
            
    	    if (myUserRecord.userGrade !=null || 
        	    myUserRecord.job_title !=null || StdColUsed_[8] || 
        	    myUserRecord.join_date !=null || StdColUsed_[12] || 
    	        myUserRecord.extra_1 != null || StdColUsed_[16] || 
    	        myUserRecord.extra_2 != null || StdColUsed_[17] || 
    	        myUserRecord.extra_3 != null || StdColUsed_[18] || 
    	        myUserRecord.extra_4 != null || StdColUsed_[19] || 
    	        myUserRecord.extra_5 != null || StdColUsed_[20] || 
    	        myUserRecord.extra_6 != null || StdColUsed_[21] || 
    	        myUserRecord.extra_7 != null || StdColUsed_[22] || 
    	        myUserRecord.extra_8 != null || StdColUsed_[23] || 
    	        myUserRecord.extra_9 != null || StdColUsed_[24] || 
    	        myUserRecord.extra_10 != null || StdColUsed_[25] 
    	        ) 
    	    {
	            org.imsglobal.enterprise.ExtensionType extension = objFactory.createExtensionType();
        	    org.imsglobal.enterprise.cwn.ExtensionType cwnExtension = cwnobjFactory.createExtensionType();
            
                if (myUserRecord.userGrade!=null){
	                org.imsglobal.enterprise.cwn.ExtensionType.GradeType cwngrade = cwnobjFactory.createExtensionTypeGradeType();
	                cwngrade.setValue(myUserRecord.userGrade.ugr_display_bil); 
        	        cwngrade.setId(myUserRecord.gradeCode); 
	                cwnExtension.setGrade(cwngrade);
                }

                if (myUserRecord.job_title!=null || StdColUsed_[8]){
                    String tmpStr = myUserRecord.job_title;
                    if (tmpStr == null) tmpStr = "";
                    cwnExtension.setJobTitle(tmpStr);
                }
                if (myUserRecord.join_date!=null || StdColUsed_[12]){
                    String tmpStr = myUserRecord.join_date;
                    if (tmpStr == null)         tmpStr = "";
                    cwnExtension.setJoinDate(tmpStr);
                }
                
	            List l_cwnExt = cwnExtension.getExt();

        	    if (myUserRecord.extra_1 != null || StdColUsed_[16]) {
        	        String tmpStr = myUserRecord.extra_1;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("1"); 
                	l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_2 != null || StdColUsed_[17]) {
        	        String tmpStr = myUserRecord.extra_2;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("2"); 
                	l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_3 != null || StdColUsed_[18]) {
        	        String tmpStr = myUserRecord.extra_3;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("3"); 
                	l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_4 != null || StdColUsed_[19]) {
        	        String tmpStr = myUserRecord.extra_4;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("4"); 
                	l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_5 != null || StdColUsed_[20]) {
        	        String tmpStr = myUserRecord.extra_5;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("5"); 
                	l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_6 != null || StdColUsed_[21]) {
        	        String tmpStr = myUserRecord.extra_6;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("6"); 
                	l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_7 != null || StdColUsed_[22]) {
        	        String tmpStr = myUserRecord.extra_7;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("7"); 
                	l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_8 != null || StdColUsed_[23]) {
        	        String tmpStr = myUserRecord.extra_8;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("8"); 
                	l_cwnExt.add(cwnext);
	            }
	            if (myUserRecord.extra_9 != null || StdColUsed_[24]) {
        	        String tmpStr = myUserRecord.extra_9;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
	                cwnext.setExttype("9"); 
        	        l_cwnExt.add(cwnext);
	            }
        	    if (myUserRecord.extra_10 != null || StdColUsed_[25]) {
        	        String tmpStr = myUserRecord.extra_10;
        	        if (tmpStr==null)   { tmpStr= "";}
                	cwnext = cwnobjFactory.createExtensionTypeExtType();
	                cwnext.setValue(tmpStr); 
        	        cwnext.setExttype("10"); 
                	l_cwnExt.add(cwnext);
	            }
                if (myUserRecord.extra_datetime_11 != null || StdColUsed_[26]) {
                    String tmpStr = myUserRecord.extra_datetime_11;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("11"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_12 != null || StdColUsed_[27]) {
                    String tmpStr = myUserRecord.extra_datetime_12;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("12"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_13 != null || StdColUsed_[28]) {
                    String tmpStr = myUserRecord.extra_datetime_13;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("13"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_14 != null || StdColUsed_[29]) {
                    String tmpStr = myUserRecord.extra_datetime_14;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("14"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_15 != null || StdColUsed_[30]) {
                    String tmpStr = myUserRecord.extra_datetime_15;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("15"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_16 != null || StdColUsed_[31]) {
                    String tmpStr = myUserRecord.extra_datetime_16;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("16"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_17 != null || StdColUsed_[32]) {
                    String tmpStr = myUserRecord.extra_datetime_17;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("17"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_18 != null || StdColUsed_[33]) {
                    String tmpStr = myUserRecord.extra_datetime_18;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("18"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_19 != null || StdColUsed_[34]) {
                    String tmpStr = myUserRecord.extra_datetime_19;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("19"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_datetime_20 != null || StdColUsed_[35]) {
                    String tmpStr = myUserRecord.extra_datetime_20;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("20"); 
                    l_cwnExt.add(cwnext);
                }

 
                if (myUserRecord.extra_singleoption_21 != null || StdColUsed_[36]) {
                    String tmpStr = myUserRecord.extra_singleoption_21;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("21"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_22 != null || StdColUsed_[37]) {
                    String tmpStr = myUserRecord.extra_singleoption_22;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("22"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_23 != null || StdColUsed_[38]) {
                    String tmpStr = myUserRecord.extra_singleoption_23;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("23"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_24 != null || StdColUsed_[39]) {
                    String tmpStr = myUserRecord.extra_singleoption_24;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("24"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_25 != null || StdColUsed_[40]) {
                    String tmpStr = myUserRecord.extra_singleoption_25;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("25"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_26 != null || StdColUsed_[41]) {
                    String tmpStr = myUserRecord.extra_singleoption_26;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("26"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_27 != null || StdColUsed_[42]) {
                    String tmpStr = myUserRecord.extra_singleoption_27;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("27"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_28 != null || StdColUsed_[43]) {
                    String tmpStr = myUserRecord.extra_singleoption_28;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("28"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_29 != null || StdColUsed_[44]) {
                    String tmpStr = myUserRecord.extra_singleoption_29;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("29"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_singleoption_30 != null || StdColUsed_[45]) {
                    String tmpStr = myUserRecord.extra_singleoption_30;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("30"); 
                    l_cwnExt.add(cwnext);
                }

                if (myUserRecord.extra_multipleoption_31 != null || StdColUsed_[46]) {
                    String tmpStr = myUserRecord.extra_multipleoption_31;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("31"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_32 != null || StdColUsed_[47]) {
                    String tmpStr = myUserRecord.extra_multipleoption_32;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("32"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_33 != null || StdColUsed_[48]) {
                    String tmpStr = myUserRecord.extra_multipleoption_33;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("33"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_34 != null || StdColUsed_[49]) {
                    String tmpStr = myUserRecord.extra_multipleoption_34;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("34"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_35 != null || StdColUsed_[50]) {
                    String tmpStr = myUserRecord.extra_multipleoption_35;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("35"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_36 != null || StdColUsed_[51]) {
                    String tmpStr = myUserRecord.extra_multipleoption_36;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("36"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_37 != null || StdColUsed_[52]) {
                    String tmpStr = myUserRecord.extra_multipleoption_37;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("37"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_38 != null || StdColUsed_[53]) {
                    String tmpStr = myUserRecord.extra_multipleoption_38;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("38"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_39 != null || StdColUsed_[54]) {
                    String tmpStr = myUserRecord.extra_multipleoption_39;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("39"); 
                    l_cwnExt.add(cwnext);
                }
                if (myUserRecord.extra_multipleoption_40 != null || StdColUsed_[55]) {
                    String tmpStr = myUserRecord.extra_multipleoption_40;
                    if (tmpStr==null)   { tmpStr= "";}
                    cwnext = cwnobjFactory.createExtensionTypeExtType();
                    cwnext.setValue(tmpStr); 
                    cwnext.setExttype("40"); 
                    l_cwnExt.add(cwnext);
                }
                extension.setExtension(cwnExtension);
                person.setExtension(extension);
	        }
                
            myPersonList.add(person);            
            
            // for direct supervisor 
            if (StdColUsed_[11]) {
                String dummy_group_id = "dummy_direct_supervise_group" + myUserRecord.id;
                GroupType directSuperviseGroup = IMSUtils.createDummySuperviseGroup(dummy_group_id,"DSPV");
                myGroupList.add(directSuperviseGroup);

                myIMSGroupMember = new IMSGroupMember();
                MembershipType superviseMembership = myIMSGroupMember.getMembership();

                mySourcedid = objFactory.createSourcedidType();
                mySourcedid.setSource(SOURCE);
                mySourcedid.setId(dummy_group_id);
                superviseMembership.setSourcedid(mySourcedid);  
                
                myMemberList = superviseMembership.getMember();
                // import user as a normal member in the dummy group
                myMember = IMSUtils.createMemberObject(myUserRecord.id, "04", null, "1");
                myMemberList.add(myMember);            
                
                // add the direct supervisor 
                for (int k=0; myUserRecord.directSupervisorIds!= null &&  k< myUserRecord.directSupervisorIds.length ; k++){
                    myMember = IMSUtils.createMemberObject(myUserRecord.directSupervisorIds[k], role_type_supervise, null, "1");
                    myMemberList.add(myMember);            
                }
                myMembershipList.add(superviseMembership);
            }
            // FOR SUPERVISED GROUP
            if (StdColUsed_[14]) {
                String dummy_group_id = "dummy_supervise_group" + myUserRecord.id;
                GroupType superviseGroup = IMSUtils.createDummySuperviseGroup(dummy_group_id,"SUPV");
                myGroupList.add(superviseGroup);

                myIMSGroupMember = new IMSGroupMember();
                MembershipType superviseMembership = myIMSGroupMember.getMembership();

                mySourcedid = objFactory.createSourcedidType();
                mySourcedid.setSource(SOURCE);
                mySourcedid.setId(dummy_group_id);
                superviseMembership.setSourcedid(mySourcedid);  
                
                myMemberList = superviseMembership.getMember();
                // import user as a supervised member in the dummy group
                myMember = IMSUtils.createMemberObject(myUserRecord.id, role_type_supervise, null, "1");
                myMemberList.add(myMember);            
                
                // add the supervised group
                for (int k=0; myUserRecord.superviseGroupIds!=null && k< myUserRecord.superviseGroupIds.length ; k++){
                    // supervised group as member in the duumy group
                    myMember = IMSUtils.createMemberObject(myUserRecord.superviseGroupIds[k], "04", null, "2");
                    myMemberList.add(myMember);            
                }
                myMembershipList.add(superviseMembership);
                
            }
            // FOR approval group
            if (StdColUsed_[15]) {
                myIMSGroupMember = new IMSGroupMember();
                MembershipType membership = myIMSGroupMember.getMembership();

                mySourcedid = objFactory.createSourcedidType();
                mySourcedid.setSource(SOURCE);
                if (myUserRecord.group_supervisors_group!=null){
                    mySourcedid.setId(myUserRecord.group_supervisors_group);
                }else{
                    // to reset the appn approval group
                    mySourcedid.setId(IMSUsrAppnApprovalGroup.GROUP_SOURCEDID_DUMMY_RESET_APPN_APPROVAL_GROUP);
                }
                membership.setSourcedid(mySourcedid);  
                
                myMemberList = membership.getMember();
                // import user as a approval member in the group_supervisors_group
                myMember = IMSUtils.createMemberObject(myUserRecord.id, "04", IMSGroupMember.ROLE_SUBTYPE_APPROVAL_MEMBER, "1");
                myMemberList.add(myMember);
                
                myMembershipList.add(membership);
            }

        }
        
        HashSet groupCodeSet = new HashSet();
        for (int i=0; i<vtUserRecord.size(); i++) {
            UserRecord myUserRecord = (UserRecord)vtUserRecord.elementAt(i);
            if (myUserRecord.groupCode != null) {
                groupCodeSet.add(myUserRecord.groupCode);
            }
        }
        
        Object[] groupCodeList = groupCodeSet.toArray();
        for (int i=0; i<groupCodeList.length; i++) {
            myIMSGroupMember = new IMSGroupMember();
            MembershipType entityrelation = myIMSGroupMember.getMembership();
            mySourcedid = objFactory.createSourcedidType();
            mySourcedid.setSource(SOURCE);
            mySourcedid.setId((String)groupCodeList[i]);
            entityrelation.setSourcedid(mySourcedid);            
            myMemberList = entityrelation.getMember();
            for (int j=0; j<vtUserRecord.size(); j++) {
                UserRecord myUserRecord = (UserRecord)vtUserRecord.elementAt(j);
                if ((myUserRecord.groupCode != null && myUserRecord.groupCode.equalsIgnoreCase((String)groupCodeList[i])) ) {
                    myMember = IMSUtils.createMemberObject(myUserRecord.id, "04", null, "1");
                    myMemberList.add(myMember);            
                }
                else {
                    continue;
                }
            }
            myMembershipList.add(entityrelation);
        }
        
        // set the default role to the learner
        myIMSGroupMember = new IMSGroupMember();
        MembershipType entityrelation = myIMSGroupMember.getMembership();
        mySourcedid = objFactory.createSourcedidType();
        mySourcedid.setSource(SOURCE);
        mySourcedid.setId(ROOT_GRP_ID);
        entityrelation.setSourcedid(mySourcedid);            
        myMemberList = entityrelation.getMember();
        for (int j=0; j<vtUserRecord.size(); j++) {
            UserRecord myUserRecord = (UserRecord)vtUserRecord.elementAt(j);
            myMember = objFactory.createMemberType();
            mySourcedid = objFactory.createSourcedidType();
            mySourcedid.setSource(SOURCE);
            mySourcedid.setId(myUserRecord.id);
            myMember.setSourcedid(mySourcedid);
                    
            myMember.setIdtype("1");

            for (int k=0; myUserRecord.roleIds!=null && k<myUserRecord.roleIds.length; k++){
                myRoleList = myMember.getRole();
                myRole = objFactory.createRoleType();
                myRole.setRoletype(myUserRecord.roleIds[k]);
                myRole.setSubrole(myUserRecord.roleSubIds[k]);
                myRole.setStatus("1");
                myRoleList.add(myRole);
            }
            myMemberList.add(myMember);            
        }
        myMembershipList.add(entityrelation);

        OutputStream fOut = null;
        try {
            fOut = new FileOutputStream(xmlFilename);
            JAXBContext jc = JAXBContext.newInstance(IMSEnterpriseApp.allSchemaContext, this.getClass().getClassLoader());
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(myEnterprise, fOut);
        } finally {
            if(fOut!=null) fOut.close();
        }
long endTime = System.currentTimeMillis();
CommonLog.debug("total marshal time = " + (endTime - startTime));
        return true;
    }
    
    public String cookUser(String xmlFilename, qdbEnv static_env, long ilg_id, Vector v_used_col, WizbiniLoader wizbini, String cook_usr_id) throws IOException, SQLException, cwException, JAXBException{
        
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
    	Hashtable h_result = imsEnterprise.updDB(con_, cwIni, prof_, site_id_, IMSEnterprise.TYPE_USER_GROUP, null, true, null);
    	
    	this.h_status_count = (Hashtable)h_result.get(IMSEnterprise.TYPE_USER);
    	
        IMSCleanUp imsCleanUp = new IMSCleanUp(imsEnterprise.synTimestamp);
        if (v_used_col.contains("Group Code")){
            imsCleanUp.cleanUpEntityRelation(con_, site_id_, dbEntityRelation.ERN_TYPE_USR_PARENT_USG, cook_usr_id, null);
        }
        if (v_used_col.contains("Role Codes") ){
            String[] syn_role_ext_id = cwUtils.splitToString(cwIni.getValue("IMPORT_ROLE_LIST"), "~");
            for (int i=0; i<syn_role_ext_id.length; i++){
                imsCleanUp.cleanUpUserRole(con_, syn_role_ext_id[i]);
            }
        }
        xml = IMSLog.getResultXML(static_env, ilg_id, (Hashtable)h_result.get(IMSEnterprise.TYPE_USER));

        return xml;
    }
    
    // parse the column line and get the sequence of the columns in the input file
    // comparing to that of the db table, such that the column order of the input file
    // does not have to follow exactly the order in the db table
    private void parseCol(String colLine) throws cwException, cwSysMessage {
        
            colLine = colLine + colDelimiter_;
            
            // initialize the column validating stuff
            int incolcnt = countToken(colLine, colDelimiter_);
            inColMatch_ = new int[incolcnt];
            for (int k = 0; k < incolcnt; k++)
                inColMatch_[k] = -1;
            for (int k = 0; k < maxStdCol_; k++)
                StdColUsed_[k] = false;

            int i = 0, j = 0, thisColIdx = 0, colCnt = 0;
            String colName;
            
            // extract all the column names specified in the column line
            while ((j = colLine.indexOf(colDelimiter_, i)) > -1) {
                // next column name exists, get it
                colCnt++;
                colName = colLine.substring(i, j);
                i = j + 1;
                // validate this column name
                if (colName.length() == 0) {
                	CommonLog.info("blank column name at column (" + colCnt + "). upload will be continued but all data from this column will be ignored.");
                } else {
                    // check if this column name is one of the standard column names
                    for (thisColIdx = 0; thisColIdx < maxStdCol_; thisColIdx++)
                        if (colName.equals(StdColName_[thisColIdx]))
                            break;
                            
                    if (thisColIdx >= maxStdCol_) {
                    	CommonLog.info("unknown column name \"" + colName + "\" at column (" + colCnt + "). upload will be stopped.");
                        unknownColNameVec.addElement(colName);
                        // ignore this non-standard column
                        continue;
//                        statusOK = false;
                    } else if (StdColUsed_[thisColIdx]) {
                    	CommonLog.info("duplicated column name \"" + colName + "\" at column (" + colCnt + "). upload will be stopped.");
                        Vector v_data = new Vector();
                        v_data.addElement(colName);
                        v_data.addElement(colCnt + "");
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_DUPLICATE_COLUMN, v_data);
                    } else {
                        // save the order
                        inColMatch_[colCnt - 1] = thisColIdx;
                        // and indicate that it has appeared
                        StdColUsed_[thisColIdx] = true;
                        //initFileFormat(thisColIdx);
                    }
                }
            }
            
            // check if there lacks any non nullable columns (without default value)
            for (int k = 0; k < maxStdCol_; k++) {
                if (!StdColUsed_[k] && StdColDefValue_[k] != null && StdColDefValue_[k].equals(nonNullStr_)) {
                	CommonLog.info("column \"" + StdColName_[k] + "\" must be supplied. upload will be stopped.");
                    throw new cwSysMessage(UploadUtils.SMSG_ULG_REQUIRED_COLUMN_MISSING, StdColName_[k]);
                }
            }
            CommonLog.info("column names processed.");
    }
    
    public Vector getUsedCol(){
        Vector v_used_col = new Vector();
        for (int k=0; k<inColMatch_.length; k++){
            if (inColMatch_[k] >= 0){
                v_used_col.addElement(StdColName_[inColMatch_[k]]);
            }
        }
        return v_used_col;
    }
    public static StringBuffer getUsedColAsXML(Vector v_used_col){
        StringBuffer xml = new StringBuffer();
        xml.append("<used_column>");
        for (int k= 0; k<v_used_col.size(); k++){
            xml.append("<column>").append(v_used_col.elementAt(k)).append("</column>");                
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
    private void parseField(String fieldvalue, int colIdx, UserRecord user) {
        try {
        	CommonLog.debug(fieldvalue);
            if (inColMatch_[colIdx] == -1) return;

            fieldvalue = UploadUtils.escString(fieldvalue.trim());
            
            //add leading and trailing spaces for usr_multipleoption_31 to usr_multipleoption_41 
            if(fieldvalue != null && fieldvalue.length() > 0 && inColMatch_[colIdx] >= 46 && inColMatch_[colIdx] <= 55) {
                if(fieldvalue.charAt(0) != ' ') {
                    fieldvalue = " " + fieldvalue;
                }
                if(fieldvalue.charAt(fieldvalue.length()-1) != ' ') {
                    fieldvalue = fieldvalue + " ";
                }
            }
            
            // get the default value of this column
            String defValue = StdColDefValue_[inColMatch_[colIdx]];
            // check if it is from a non-nullable column
            if (defValue != null && defValue.equals(nonNullStr_) && fieldvalue.length() == 0) {
                user.missingField = StdColName_[inColMatch_[colIdx]];
                user.parseOK = false;
            }else{    
                // give the default value if the input field is empty
                if (fieldvalue.length() == 0)
                    fieldvalue = defValue;
                // save this field
                user.parseOK = putField(fieldvalue, user, inColMatch_[colIdx]);
            }
        } catch (Exception e) {
            user.parseOK = false;
            CommonLog.error("error at column (" + (colIdx + 1) + "):" + e.getMessage() + ".");
        }
    }
    
    private void finalCheck(String year, UserRecord user, WizbiniLoader wizbini) throws SQLException, cwException{
        try {
            //System.out.println("fillFields");
            
            if (user.bday != null) {
                try {
                    Timestamp myBDay = Timestamp.valueOf(user.bday.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
//                    user.invalidBDay = true;
                    user.invalidFieldName =  StdColName_[4];
                }        
                user.bday = user.bday.trim() + "T00:00:00";
            }

            if (user.join_date != null) {
                try {
                    Timestamp myJoinDay = Timestamp.valueOf(user.join_date.trim() + " 00:00:00");
                } catch (Exception e) {
                	CommonLog.error(user.join_date);
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[12];
                }        
                user.join_date = user.join_date.trim() + "T00:00:00";
            }
            
            dbRegUser dbusr = new dbRegUser();
            dbusr.usr_ste_usr_id = user.id;
            if (dbusr.checkSiteUsrIdExist(con_, site_id_, wizbini.cfgSysSetupadv.isTcIndependent())){
                user.recStatus = REC_STATUS_UPDATE;
                if (bool_user_distinct_check_enable_){
                    // if exist. it is not insert!
                    user.parseOK = false;
                    user.duplicatedLoginID = true;
                }
            }else{
                user.recStatus = REC_STATUS_ADD;
            }
            
            if (user.id!=null){
                if (!dbRegUser.validateUserId(user.id)){
                    user.parseOK = false;
                    user.invalidFieldName = StdColName_[0];
                }
            }

            if (user.password!=null){
                if (!dbRegUser.validatePassword(user.password)){
                    user.parseOK = false;
                    user.invalidFieldName = StdColName_[1];
                }
            }else{
                // for insert , must have password
                if (user.recStatus.equals(REC_STATUS_ADD)){
                    user.parseOK = false;
                    user.emptyPassword = true;
                }
            }
            
            if( user.groupCode != null ){
				if (!validateGroupCode(user.groupCode)) {
					user.invalidGroupCode = true;
					user.parseOK = false;
				} else {
					// 二级培训中心独立，且用户是更新状态
					if (wizbini.cfgSysSetupadv.isTcIndependent() && REC_STATUS_UPDATE.equalsIgnoreCase(user.recStatus)) {
						// 1.得到用户二级培训中心ID
						long usr_ent_id = dbRegUser.getEntId(con_, dbusr.usr_ste_usr_id, 1);
						long tcrId = ViewTrainingCenter.getTopTc(con_, usr_ent_id,wizbini.cfgSysSetupadv.isTcIndependent());
						Vector entSteUidVec = null;
						if (tcrId != 0) {
							entSteUidVec = dbUserGroup.getEntSteIdVec(con_, tcrId);
							// 更改的用户组是否在同一培训中心下
							if (tcrId != 1 && entSteUidVec.contains(user.groupCode)) {
								user.groupId = ((Long) htGroupId.get(user.groupCode)).longValue();
							}else{
								user.duplicatedLoginID = true;
								user.parseOK = false;
							}
						}
					} else {
						user.groupId = ((Long) htGroupId.get(user.groupCode)).longValue();
					}
				}
			}
            if( user.gradeCode != null ){
            	//
                if( !htValidGradeCode.containsKey(user.gradeCode) ) {
                    if( invalidGradeCode.indexOf(user.gradeCode) == -1 ) {
                        DbUserGrade dbUgr = new DbUserGrade();
                        dbUgr.ent_ste_uid = user.gradeCode;
                        dbUgr.ugr_ent_id_root = site_id_;
                        boolean bValidGrade = dbUgr.getBySteUid(con_);
                        if (!bValidGrade){
                        	if("Unspecified".equals(dbUgr.ent_ste_uid)){
                       		 	htValidGradeCode.put(user.gradeCode, dbUgr);
                                user.userGrade =dbUgr.getDefaultGrade(con_, site_id_);
	                       	}else{
	                       		user.invalidGradeCode = true;
	                       		user.parseOK = false;
	                       		invalidGradeCode.addElement(user.gradeCode);
	                       	}
                        }else{
                            htValidGradeCode.put(user.gradeCode, dbUgr);
                            user.userGrade = dbUgr;
                        }                                
                    } else {
                        user.invalidGradeCode = true;
                        user.parseOK = false;
                    }
                }else{
                    user.userGrade = (DbUserGrade)htValidGradeCode.get(user.gradeCode);
                }
            }

            if( user.supervised_group_ids != null && user.supervised_group_ids.length() > 0 ){
                user.superviseGroupIds = cwUtils.splitToString(user.supervised_group_ids, ",", true);
                for (int i=0; i<user.superviseGroupIds.length; i++){
                    if (!validateGroupCode(user.superviseGroupIds[i])){
                        user.invalidSupervisedGroupCode = true;
                        user.parseOK = false;
                    }
                }
            }
            
            if( user.role_id != null && user.role_id.length() > 0 ) {
                String[] tmpRoles = cwUtils.splitToString(user.role_id, ",", true);
                user.roleIds = new String[tmpRoles.length];
                user.roleSubIds = tmpRoles;
                for (int i=0; i<tmpRoles.length; i++){
                    if( !htRoleUid.containsKey(tmpRoles[i])) {
                        user.invalidRole = true;
                        user.parseOK = false;
                    }else{
                        user.roleIds[i] = (String)htRoleUid.get(tmpRoles[i]);
                        user.roleSubIds[i] = tmpRoles[i];
                    }
                }
            }
            
            if( user.gender != null ) {
                if( user.gender.equalsIgnoreCase("M") )
                    user.gender = "2";
                else if( user.gender.equalsIgnoreCase("F") )
                    user.gender = "1";
                else {
                    user.invalidFieldName = StdColName_[3];
                    user.parseOK = false;
                }
            }
            
            if( user.groupCode == null ) {
                user.parseOK = false;
                user.noGrpNameCode = true;
            }
            if( user.group_supervisors_group != null ){
                if (!validateGroupCode(user.group_supervisors_group)){
                    user.invalidGroupSupervisorGroup = true;
                    user.parseOK = false;
                }else{
                    // check if the appn approval group is an ancestor of the user's group
                    user.appnApprovalGroupId = ((Long)htGroupId.get(user.group_supervisors_group)).longValue();

                    dbEntityRelation dber = new dbEntityRelation();
                    dber.ern_ancestor_ent_id = user.appnApprovalGroupId;
                    dber.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
                    Vector successorId = dber.getSuccessorID(con_);
                    if (user.groupId == user.appnApprovalGroupId || successorId.contains(new Long(user.groupId))){
                        // pass
                    }else{
                    	CommonLog.info("not successor");
                        user.invalidGroupSupervisorGroup = true;
                        user.parseOK = false;
                    }
                }
            }
            if (user.extra_datetime_11 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_11.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[26];
                }        
                user.extra_datetime_11 = user.extra_datetime_11.trim() + "T00:00:00";
            }
            if (user.extra_datetime_12 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_12.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[27];
                }        
                user.extra_datetime_12 = user.extra_datetime_12.trim() + "T00:00:00";
            }
            if (user.extra_datetime_13 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_13.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[28];
                }        
                user.extra_datetime_13 = user.extra_datetime_13.trim() + "T00:00:00";
            }
            if (user.extra_datetime_14 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_14.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[29];
                }        
                user.extra_datetime_14 = user.extra_datetime_14.trim() + "T00:00:00";
            }
            if (user.extra_datetime_15 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_15.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[30];
                }        
                user.extra_datetime_15 = user.extra_datetime_15.trim() + "T00:00:00";
            }
            if (user.extra_datetime_16 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_16.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[31];
                }        
                user.extra_datetime_16 = user.extra_datetime_16.trim() + "T00:00:00";
            }
            if (user.extra_datetime_17 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_17.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[32];
                }        
                user.extra_datetime_17 = user.extra_datetime_17.trim() + "T00:00:00";
            }
            if (user.extra_datetime_18 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_18.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[33];
                }        
                user.extra_datetime_18 = user.extra_datetime_18.trim() + "T00:00:00";
            }
            if (user.extra_datetime_19 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_19.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[34];
                }        
                user.extra_datetime_19 = user.extra_datetime_19.trim() + "T00:00:00";
            }
            if (user.extra_datetime_20 != null) {
                try {
                    Timestamp ts = Timestamp.valueOf(user.extra_datetime_20.trim() + " 00:00:00");
                } catch (Exception e) {
                    user.parseOK = false;
                    user.invalidFieldName =  StdColName_[35];
                }        
                user.extra_datetime_20 = user.extra_datetime_20.trim() + "T00:00:00";
            }
        	
			/*user extension for single choice*/
/*
			if (user.extra_singleoption_21!= null) {
				//TODO
			}
			
			
			if(user.extra_multipleoption_31!=null && user.extra_multipleoption_31.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_31, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_31=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_32!=null && user.extra_multipleoption_32.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_32, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_32=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_33!=null && user.extra_multipleoption_33.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_33, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_33=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_34!=null && user.extra_multipleoption_34.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_34, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_34=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_35!=null && user.extra_multipleoption_35.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_35, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_35=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_36!=null && user.extra_multipleoption_36.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_36, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_36=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_37!=null && user.extra_multipleoption_37.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_37, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_37=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_38!=null && user.extra_multipleoption_38.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_38, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_38=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_39!=null && user.extra_multipleoption_39.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_39, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_39=optsBuf.toString();                
			}			
			if(user.extra_multipleoption_40!=null && user.extra_multipleoption_40.length() > 0) {
				//split & trim
                String[] opts = cwUtils.splitToString(user.extra_multipleoption_40, ",", true);
                StringBuffer optsBuf = new StringBuffer();
                optsBuf.append(" ");
                for (int i=0;i<opts.length-1;i++){
                    optsBuf.append(opts[i]).append(" , ");                    
                }
                optsBuf.append(opts[opts.length-1]).append(" ");
                user.extra_multipleoption_40=optsBuf.toString();                
			}
*/			
            if (user.parseOK){
                validate4AppnApprovalList(user);
            }
        }catch (Exception e) {
        	CommonLog.error(e.getMessage(),e);
            user.parseOK = false;
        }                     
    }
    
    private boolean putField(String inValue, UserRecord user, int inStdColIdx) throws cwException {
        try {
            boolean bValid = true;
            switch (inStdColIdx) {
                case 0:
                    if (UploadUtils.checkLength(inValue, 0, 20)){
                        user.id = inValue;
                        // duplicate id in same import file
                        if (importUserId.contains(user.id)){
                            user.duplicatedLoginID = true;
                            bValid = false;
                        }
                    }else{
                        user.invalidFieldName =  StdColName_[0];
                        bValid = false;
                    }
                    break;
                case 1:
                    if (UploadUtils.checkLength(inValue, 0, 20)){
                        user.password = inValue;
                    }else{
                        user.invalidFieldName =  StdColName_[1];
                        bValid = false;
                    }
                    break;
                case 2:
                    if (UploadUtils.checkLength(inValue, 0, 255)){
                        user.fn = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[2];
                        bValid = false;
                    }
                    break;
                case 3:
                    user.gender = inValue;
                    break;
                case 4:
                    if (inValue != null && inValue.length() > 0) {
                        if(!UploadUtils.isValidDate(inValue, UploadUtils.DF_YYYY_MM_DD)){
                        	user.invalidFieldName =  StdColName_[4];
                            bValid = false;
                        } else {
                        	user.bday = inValue.replace('/', '-');
                        }
                    }
                    else {
                        user.bday = null;
                    }
                    break;
                case 5:
                    if (UploadUtils.checkLength(inValue, 0, 255)){
                        user.email = inValue;
                    }else{
                        user.invalidFieldName =  StdColName_[5];
                        bValid = false;
                    }
                    break;
                case 6:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.phone = inValue;
                    }else{
                        user.invalidFieldName =  StdColName_[6];
                        bValid = false;
                    }
                    break;
                case 7:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.fax = inValue;
                    }else{
                        user.invalidFieldName =  StdColName_[7];
                        bValid = false;
                    }
                    break;
                case 8:
                    if (UploadUtils.checkLength(inValue, 0, 255)){
                        user.job_title = inValue;
                    }else{
                        user.invalidFieldName =  StdColName_[8];
                        bValid = false;
                    }
                    break;
                case 9:
                    user.gradeCode = inValue;
                    break;
                case 10:
                    user.groupCode = inValue;
                    break;
                case 11:
                    user.direct_supervisor_ids = inValue;
                    break;
                case 12:
                    if (inValue != null && inValue.length() > 0) {
                        if(!UploadUtils.isValidDate(inValue, UploadUtils.DF_YYYY_MM_DD)){
                        	user.invalidFieldName =  StdColName_[12];
                            bValid = false;
                        } else {
                        	user.join_date = inValue.replace('/', '-');
                        }
                    }
                    else {
                        user.join_date = null;
                    }
                    break;
                case 13:
                    user.role_id = inValue;
                    break;
                case 14:
                    user.supervised_group_ids = inValue;
                    break;
                case 15:
                    user.group_supervisors_group = inValue;
                    break;
                case 16:
                    if (inValue != null && inValue.length() > 0) {
                        user.usr_source = inValue;
                    } else {
                        user.usr_source = "wizBank";
                    }
                    break;
                case 17:
                    if (UploadUtils.checkLength(inValue, 0, 255)){
                        user.extra_1 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[16];
                        bValid = false;
                    }
                    break;
                case 18:
                    if (UploadUtils.checkLength(inValue, 0, 255)){
                        user.extra_2 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[17];
                        bValid = false;
                    }
                    break;
                case 19:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_singleoption_21 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[18];
                        bValid = false;
                    }
                    break;
                case 20:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_multipleoption_31 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[19];
                        bValid = false;
                    }
                    break;
                case 21:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_5 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[20];
                        bValid = false;
                    }
                    break;
                case 22:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_6 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[21];
                        bValid = false;
                    }
                    break;
                case 23:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_7 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[22];
                        bValid = false;
                    }
                    break;
                case 24:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_8 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[23];
                        bValid = false;
                    }
                    break;
                case 25:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_9 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[24];
                        bValid = false;
                    }
                    break;
                case 26:
                    if (UploadUtils.checkLength(inValue, 0, 50)){
                        user.extra_10 = UploadUtils.checkEnc(inValue, enc_);
                    }else{
                        user.invalidFieldName =  StdColName_[25];
                        bValid = false;
                    }
                    break;
                case 27:
                    if (inValue != null && inValue.length() > 0) {
                        if(!UploadUtils.isValidDate(inValue, UploadUtils.DF_YYYY_MM_DD)){
                        	user.invalidFieldName =  StdColName_[27];
                            bValid = false;
                        } else {
                        	user.extra_datetime_11 = inValue.replace('/', '-');
                        }
                    }
                    else {
                        user.extra_datetime_11 = null;
                    }
                    break;
                case 28:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_12 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_12 = null;
                    }
                    break;
                case 29:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_13 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_13 = null;
                    }
                    break;
                case 30:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_14 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_14 = null;
                    }
                    break;
                case 31:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_15 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_15 = null;
                    }
                    break;
                case 32:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_16 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_16 = null;
                    }
                    break;
                case 33:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_17 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_17 = null;
                    }
                    break;
                case 34:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_18 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_18 = null;
                    }
                    break;
                case 35:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_19 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_19 = null;
                    }
                    break;
                case 36:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_datetime_20 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_datetime_20 = null;
                    }
                    break;
                    
                case 37:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_21 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_21 = null;
                    }
                    break;
                case 38:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_22 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_22 = null;
                    }
                    break;
                case 39:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_23 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_23 = null;
                    }
                    break;
                case 40:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_24 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_24 = null;
                    }
                    break;
                case 41:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_25 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_25 = null;
                    }
                    break;
                case 42:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_26 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_26 = null;
                    }
                    break;
                case 43:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_27 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_27 = null;
                    }
                    break;
                case 44:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_28 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_28 = null;
                    }
                    break;
                case 45:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_29 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_29 = null;
                    }
                    break;
                case 46:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_singleoption_30 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_singleoption_30 = null;
                    }
                    break;
                    
                case 47:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_31 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_31 = null;
                    }
                    break;
                case 48:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_32 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_32 = null;
                    }
                    break;
                case 49:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_33 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_33 = null;
                    }
                    break;
                case 50:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_34 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_34 = null;
                    }
                    break;
                case 51:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_35 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_35 = null;
                    }
                    break;
                case 52:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_36 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_36 = null;
                    }
                    break;
                case 53:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_37 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_37 = null;
                    }
                    break;
                case 54:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_38 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_38 = null;
                    }
                    break;
                case 55:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_39 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_39 = null;
                    }
                    break;
                case 56:
                    if (inValue != null && inValue.length() > 0) {
                        user.extra_multipleoption_40 = inValue.replace('/', '-');
                    }
                    else {
                        user.extra_multipleoption_40 = null;
                    }
                    break;
                case 57:
                    if (UploadUtils.checkLength(inValue, 0, 20)){
                        user.nickname = inValue;
                    }else{
                        user.invalidFieldName =  StdColName_[57];
                        bValid = false;
                    }
                    break;
                   
                default:
                    break;
            }
            return bValid;
        }catch (Exception e) {
            throw new cwException("Failed to parse field.");
        }
    }
    
    public String unknownColNameXML(){
        
        StringBuffer xml = new StringBuffer(512);
        xml.append("<unknown_col_list>");
        for(int i=0; i<unknownColNameVec.size(); i++)
            xml.append("<unknown_col>").append(unknownColNameVec.elementAt(i)).append("</unknown_col>");
        xml.append("</unknown_col_list>");
        return xml.toString();
    }
    
    public String getGroupCodeReference(Connection con)
        throws SQLException{
            Hashtable htDisplayName = null;
            
            if(validGroupCode.size() == 0) {
                htDisplayName = dbUserGroup.getDisplayName(con, site_id_);
            } else {
                htDisplayName = dbUserGroup.getDisplayName(con, site_id_, validGroupCode);
            }
            
            StringBuffer xml = new StringBuffer(1024);
            xml.append("<group_code_ref_list>");
            String code; 
            Enumeration keys = htDisplayName.keys();
            while(keys.hasMoreElements()) {
                code = (String) keys.nextElement();
                xml.append("<group_code_ref>")
                    .append("<code>").append(cwUtils.esc4XML(code)).append("</code>")
                    .append("<title>").append(cwUtils.esc4XML((String)htDisplayName.get(code))).append("</title>");
                xml.append("</group_code_ref>");
            }
            /*
            for(int i=0; i<validGroupCode.size(); i++){
                code = (String) validGroupCode.elementAt(i);
                xml.append("<group_code_ref>")
                    .append("<code>").append(cwUtils.esc4XML(code)).append("</code>")
                    .append("<title>").append(cwUtils.esc4XML((String)htDisplayName.get(code))).append("</title>");
                xml.append("</group_code_ref>");
            }
            */
            xml.append("</group_code_ref_list>");
            return xml.toString();
        }

    public String getGradeCodeReference(Connection con)
        throws SQLException{
            
            Enumeration enumeration = htValidGradeCode.keys();

            StringBuffer xml = new StringBuffer(1024);
            xml.append("<grade_code_ref_list>");
            String code; 
            DbUserGrade dbUgr; 
            while(enumeration.hasMoreElements()) {
                dbUgr = (DbUserGrade)htValidGradeCode.get(enumeration.nextElement());
                xml.append("<grade_code_ref>")
                    .append("<code>").append(cwUtils.esc4XML(dbUgr.ent_ste_uid)).append("</code>")
                    .append("<title>").append(cwUtils.esc4XML(dbUgr.ugr_display_bil)).append("</title>");
                xml.append("</grade_code_ref>");
            }
            xml.append("</grade_code_ref_list>");
            return xml.toString();
        }
 
    public boolean validateGroupCode(String code) throws SQLException, qdbException{
        boolean bValid;
        if( validGroupCode.indexOf(code) == -1 ) {
            if( invalidGroupCode.indexOf(code) == -1 ) {
                dbUserGroup dbUsrGrp = new dbUserGroup();
                dbUsrGrp.ent_ste_uid        = code;
                dbUsrGrp.usg_ent_id_root    = site_id_;
                dbUsrGrp.getEntIdBySteUid(con_);
                if( dbUsrGrp.usg_ent_id == 0  ) {
                    invalidGroupCode.addElement(code);
                    bValid = false;
                } else {
                    validGroupCode.addElement(code);
//                    System.out.println("code: " + code + " "+ dbUsrGrp.usg_ent_id);
                    htGroupId.put(code, new Long(dbUsrGrp.usg_ent_id));
                    bValid = true;
                }
            } else {
                bValid = false;
            }
        }else{
            bValid = true;
        }
        return bValid;
    }
    
    public StringBuffer uploadPreview(Connection con, String tmpFile, String enc, WizbiniLoader wizbini)
    	throws SQLException, IOException{
        StringBuffer xml = new StringBuffer();
        xml.append("<upload_user>");
        //标题列生成XML
        xml.append(getUsedColAsXML(this.prof_));
        xml.append(getGroupCodeReference(con));
        xml.append(getGradeCodeReference(con));

        //total uploaded users
        if(this.numberOfUsers >= 0) {
            xml.append("<user_count>").append(this.numberOfUsers).append("</user_count>");
        }

        // total failed lines
        xml.append("<failed_lines>").append(cwUtils.NEWL);
        for (int i=0;i<failVec.size();i++) {
            xml.append("<line num=\"").append(((Long) failVec.elementAt(i)).toString())
            .append("\"/>").append(cwUtils.NEWL);
        }
        xml.append("</failed_lines>").append(cwUtils.NEWL);
        
        // for missing field
        xml.append("<missing_field_lines>").append(cwUtils.NEWL);
        for (int i=0;i<missingFieldFailVec.size();i++) {
            xml.append("<line num=\"").append((Long)missingFieldFailVec.elementAt(i)).append("\" field=\"").append((String)missingFieldNameVec.elementAt(i)).append("\"/>");
        }
        xml.append("</missing_field_lines>").append(cwUtils.NEWL);

        // for duplicated login ID
        xml.append("<duplicated_id_lines>").append(cwUtils.NEWL);
        for (int i=0;i<duplicatedLoginIdFailVec.size();i++) {
            xml.append("<line num=\"").append(((Long) duplicatedLoginIdFailVec.elementAt(i)).toString())
            .append("\"/>").append(cwUtils.NEWL);
        }
        xml.append("</duplicated_id_lines>").append(cwUtils.NEWL);
        // for invalid password
        xml.append("<empty_password_lines>").append(cwUtils.NEWL);
        for (int i=0;i<emptyPasswordVec.size();i++) {
            xml.append("<line num=\"").append(((Long) emptyPasswordVec.elementAt(i)).toString())
            .append("\"/>").append(cwUtils.NEWL);
        }
        xml.append("</empty_password_lines>").append(cwUtils.NEWL);

//        xml.append("<invalid_joindate_lines>").append(cwUtils.NEWL);
//        for (int i=0;i<invalidJoinDayFailVec.size();i++) {
//            xml.append("<line num=\"").append(((Long) invalidJoinDayFailVec.elementAt(i)).toString())
//            .append("\"/>").append(cwUtils.NEWL);
//        }
//        xml.append("</invalid_joindate_lines>").append(cwUtils.NEWL);
                    
        //for invalid group code
        xml.append("<invalid_groupcode_lines>").append(cwUtils.NEWL);
        for(int i=0; i<invalidGroupCodeVec.size(); i++)
            xml.append("<line num=\"").append((Long)invalidGroupCodeVec.elementAt(i)).append("\"/>");
        xml.append("</invalid_groupcode_lines>");   
        // for invalid grade code                    
        xml.append("<invalid_gradecode_lines>").append(cwUtils.NEWL);
        for(int i=0; i<invalidGradeCodeVec.size(); i++)
            xml.append("<line num=\"").append((Long)invalidGradeCodeVec.elementAt(i)).append("\"/>");
        xml.append("</invalid_gradecode_lines>");   

        //for invalid role
        xml.append("<invalid_role_lines>").append(cwUtils.NEWL);
        for(int i=0; i<invalidRoleVec.size(); i++)
            xml.append("<line num=\"").append((Long)invalidRoleVec.elementAt(i)).append("\"/>");
        xml.append("</invalid_role_lines>");   

        // for invalid gender
//        xml.append("<invalid_gender>").append(cwUtils.NEWL);
//        for(int i=0; i<invalidGenderVec.size(); i++)
//            xml.append("<line num=\"").append((Long)invalidGenderVec.elementAt(i)).append("\"/>");
//        xml.append("</invalid_gender>");

        //No Group Code 
        xml.append("<no_group_specified>").append(cwUtils.NEWL);
        for(int i=0; i<noGrpCodeNameVec.size(); i++)
            xml.append("<line num=\"").append((Long)noGrpCodeNameVec.elementAt(i)).append("\"/>");
        xml.append("</no_group_specified>");

        xml.append("<invalid_supervise_groupcode_lines>").append(cwUtils.NEWL);
        for(int i=0; i<invalidSupervisedGroupCode.size(); i++)
            xml.append("<line num=\"").append((Long)invalidSupervisedGroupCode.elementAt(i)).append("\"/>");
        xml.append("</invalid_supervise_groupcode_lines>");
                    
        xml.append("<invalid_direct_supervisor_id_lines>").append(cwUtils.NEWL);
        for(int i=0; i<invalidDirectSupervisorId.size(); i++)
            xml.append("<line num=\"").append((Long)invalidDirectSupervisorId.elementAt(i)).append("\"/>");
        xml.append("</invalid_direct_supervisor_id_lines>");

        xml.append("<invalid_group_supervisor_group_lines>").append(cwUtils.NEWL);
        for(int i=0; i<invalidGroupSupervisorGroup.size(); i++)
            xml.append("<line num=\"").append((Long)invalidGroupSupervisorGroup.elementAt(i)).append("\"/>");
        xml.append("</invalid_group_supervisor_group_lines>");

        xml.append("<held_by_pending_appn>").append(cwUtils.NEWL);
        for(int i=0; i<heldByPendingAppn.size(); i++)
            xml.append("<line num=\"").append((Long)heldByPendingAppn.elementAt(i)).append("\"/>");
        xml.append("</held_by_pending_appn>");

        xml.append("<held_by_pending_approval_appn>").append(cwUtils.NEWL);
        for(int i=0; i<heldByPendingApprovalAppn.size(); i++)
            xml.append("<line num=\"").append((Long)heldByPendingApprovalAppn.elementAt(i)).append("\"/>");
        xml.append("</held_by_pending_approval_appn>");

        xml.append("<invalid_field_lines>").append(cwUtils.NEWL);
        for(int i=0; i<invalidField.size(); i++)
            xml.append("<line num=\"").append((Long)invalidField.elementAt(i)).append("\" field=\"").append((String)invalidFieldName.elementAt(i)).append("\"/>");
        xml.append("</invalid_field_lines>");

        //Invalid Column Name
        xml.append(unknownColNameXML());
                    
        // for others
        xml.append("<others_failed_lines>").append(cwUtils.NEWL);
        for (int i=0;i<othersFailVec.size();i++) {
            xml.append("<line num=\"").append(((Long) othersFailVec.elementAt(i)).toString())
            .append("\"/>").append(cwUtils.NEWL);
        }
        xml.append("</others_failed_lines>").append(cwUtils.NEWL);

        xml.append(getXML(tmpFile, enc));

		xml.append("<spawn_threshold>" + wizbini.cfgSysSetupadv.getUserBatchUpload().getSpawnThreshold() + "</spawn_threshold>");
		
        xml.append("</upload_user>");
        return xml;
    }
    
   private boolean validate4AppnApprovalList(UserRecord user) throws qdbException, SQLException{
	   CommonLog.info("validate2AppnApprovalList: " + user.id);
        long usrEntId = dbRegUser.getEntId(con_, user.id, site_id_);
        if (usrEntId == 0){
            // new user, no further checking
        	CommonLog.info("insert user. passed");
            return true;
        }else{
            boolean hasPendingAppn = DbAppnApprovalList.hasPendingAppn(con_, usrEntId);
            if (hasPendingAppn){
                // user cannot change its group, approval_group, direct supervisor
                // check group
                Vector tmpId = new Vector();
                tmpId.addElement(new Long(usrEntId));
                dbEntityRelation dbEr = new dbEntityRelation();
                dbEr.ern_child_ent_id = usrEntId;
                dbEr.ern_type = dbEntityRelation.ERN_TYPE_USG_PARENT_USG;
                Vector vtGroupId = dbEr.getParentId(con_);
                
                if (vtGroupId.size() != 1){
                	CommonLog.info("cur group problem");
                    user.heldByPendingAppn = true;
                    user.parseOK = false;
                    return false;
                }
                long curGroupId = ((Long)vtGroupId.elementAt(0)).longValue();
                CommonLog.info("curgroup:" + curGroupId + " targetGroup:" + user.groupId);
                if (curGroupId != user.groupId){
                	CommonLog.info("group changed");

                    user.heldByPendingAppn = true;
                    user.parseOK = false;
                    return false;
                }else{
                	CommonLog.info("group passed, no changed");
                }
                // check approval group
                if (StdColUsed_[15]){
                    dbRegUser dbusr = new dbRegUser();
                    dbusr.usr_ent_id = usrEntId;
                    dbusr.getAppApprovalUsgEntId(con_);
                    if (dbusr.usr_app_approval_usg_ent_id != user.appnApprovalGroupId){
                    	CommonLog.info("approval group changed");
                        user.heldByPendingAppn = true;
                        user.parseOK = false;
                        return false;
                    }else{
                    	CommonLog.info("approval group passed, no changed");
                    }
                }else{
                	CommonLog.info("approval group not import, no checking");
                }
                // check direct supervisor
                if (StdColUsed_[11]){
                    ViewSuperviseTargetEntity.ViewDirectSupervisor spvr[] = ViewSuperviseTargetEntity.getDirectSupervisor(con_, usrEntId);
                    user.directSupervisorIds = cwUtils.splitToString(user.direct_supervisor_ids, ",", true);
                    if (spvr == null || user.directSupervisorIds==null){
                        if ((spvr == null || spvr.length==0) && (user.directSupervisorIds==null || user.directSupervisorIds.length==0)){
                            // both empty, passed
                        	CommonLog.info("direct supervisor passed, both empty");
                        }else{
                        	CommonLog.info("direct supervisor change from/to empty");
                            user.heldByPendingAppn = true;
                            user.parseOK = false;
                            return false;
                        }
                    }else{
                        if (user.directSupervisorIds.length == spvr.length){
                            // if import direct superivisor set is same as the set in db
                            for (int i=0; i<spvr.length; i++){
                                if (!cwUtils.strArrayContains(user.directSupervisorIds, spvr[i].usr_ste_usr_id)){
                                	CommonLog.info("direct supervisor changed");
                                    user.heldByPendingAppn = true;
                                    user.parseOK = false;
                                    return false;
                                }
                            }                                                            
                            CommonLog.info("direct supervisor not change");
                        }else{
                        	CommonLog.info("direct supervisor size not equal");
                            user.heldByPendingAppn = true;
                            user.parseOK = false;
                            return false;
                        }
                    }
                }else{
                	CommonLog.info("direct supervisor not import, no checking");
                }
            } else{
            	CommonLog.info("no pending application");
            }  
            boolean hasPendingApprovalAppn = DbAppnApprovalList.hasPendingApprovalAppn(con_, usrEntId);
            // user cannot change his/her supervised group if he/she has pending approval records
            if (hasPendingApprovalAppn){
                // check supervised group
                if (StdColUsed_[14]){
                    ViewSuperviseTargetEntity.ViewSupervisedGroup sgrp[] = ViewSuperviseTargetEntity.getSupervisedGroup(con_, usrEntId);
                    if (sgrp == null || user.superviseGroupIds==null){
                        if ((sgrp == null || sgrp.length==0) && (user.superviseGroupIds==null || user.superviseGroupIds.length==0)){
                            // both empty, passed
                        	CommonLog.info("supervised group both empty");
                        }else{
                        	CommonLog.info("supervised group from/to empty");
                            user.heldByPendingApprovalAppn = true;
                            user.parseOK = false;
                            return false;
                        }
                    }else{
                        if (user.superviseGroupIds.length == sgrp.length){
                            // if import superivised group set is same as the set in db
                            for (int i=0; i<sgrp.length; i++){
                                if (!cwUtils.strArrayContains(user.superviseGroupIds, sgrp[i].ent_ste_uid)){
                                	CommonLog.info("supervised group changed");
                                    user.heldByPendingApprovalAppn = true;
                                    user.parseOK = false;
                                    return false;
                                }
                            }                                                            
                            CommonLog.info("supervised group set equal");
                        }else{
                        	CommonLog.info("supervised group size not equal");
                            user.heldByPendingApprovalAppn = true;
                            user.parseOK = false;
                            return false;
                        }
                    }
                }else{
                	CommonLog.info("supervised group not import, no checking");
                }            
            }else{
            	CommonLog.info("no pending approval application");
            }
            return true;
        }
    }
   
	public String[] getLabelArray(loginProfile prof) {
		return ImportTemplate.getUserLabelArray(prof.cur_lan, prof.root_id);
	}
	
	public static StringBuffer getUsedColAsXML(loginProfile prof) {
		StringBuffer xml = new StringBuffer();
		xml.append("<used_column>");
		String lang = prof.cur_lan;
		String site = prof.root_id;
		String label[] = ImportTemplate.getUserLabelArray(lang, site);
		boolean[] active = ImportTemplate.getActiveLabelArray(site);
		for (int i = 0; i < label.length; i++) {
			xml.append("<column id=\"" + i + "\" active=\"" + active[i] + "\">").append(label[i]).append("</column>");
		}
		xml.append("</used_column>");
		return xml;
	}
    
}