package com.cw.wizbank.upload;

import java.lang.Long;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.oroinc.text.perl.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.db.view.ViewUploadLog;
import com.cwn.wizbank.utils.CommonLog;

import javax.servlet.http.HttpSession;

// upload questions from a text file to wizBank (QDB)
// the text file is delimited and is probably exported from MS ExceL
// version 2.2 (2001.04.06 wai)
// update: use encoding from ini file, excel can no longer specify encoding in each record

public class UploadUtils {
    public static final String ENTERPRISE_START_TAG = "<enterprise>";
    public static final String WIZBANK_NAME_SPACE = "http://www.cyberwisdom.com";

    public static final String SMSG_ULG_INVALID_FILE            = "ULG001";
    public static final String SMSG_ULG_INVALID_HEDADER         = "ULG002";
    public static final String SMSG_ULG_INVALID_ULG_RECROD      = "ULG003";
    public static final String SMSG_ULG_INVALID_OBJECTIVE       = "ULG004";
    public static final String SMSG_ULG_FILE_UPLOAD_SUCCESS     = "ULG005";
    public static final String SMSG_ULG_SAVE_QUE_SUCCESS        = "ULG006";
    public static final String SMSG_ULG_INVALID_TIMESTAMP       = "ULG007";
    public static final String SMSG_ULG_SAVE_USER_SUCCESS       = "ULG008";
    public static final String SMSG_ULG_REQUIRED_COLUMN_MISSING = "ULG009";
    public static final String SMSG_ULG_DUPLICATE_COLUMN        = "ULG010";
    public static final String SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT = "ULG011";
    
    //Process 
    public final static String UPLOAD_PROCESS_IMPORT = "IMPORT";
    public final static String UPLOAD_PROCESS_EXPORT = "EXPORT";

    public final static String ACTION_TRIGGERED_BY_UI = "UI";
    public final static String ACTION_TRIGGERED_BY_BATCH = "BATCH";
    
    public final static String INVALID_COL_TYPE_UNRECOGNIZED = "UNRECOGNIZED";
    public final static String INVALID_COL_TYPE_DUPLICATE = "DUPLICATE";
    public final static String INVALID_COL_TYPE_MISSING = "MISSING";
    
    public static final String ENC_UTF = "UTF-8";
    public static final String QUE = "QUE";
    public static final String SUCCESS_FILE = "success.txt";
    public static final String FAILURE_FILE = "failure.txt";
    public static final String ERROR_FILE = "error.txt";
        
    // column delimiter used in the input file
    public static final char colDelimiter_ = '\t';
    // line delimiters used in the input file (only valid for MSDOS)
    public static final char lineDelimiter1_ = '\r';
    public static final char lineDelimiter2_ = '\n';

    public final static String YES = "Y";
    public final static String NO = "N";
    public final static String URI_PATH_SEPARATOR = "/";
    
    public static final String COMMA_DELIMITER = ",";
    public static final String MT_PLUS_DELIMITER = "+";
    private static final String err_empty_ = "non-empty value expected";
    private static final String err_digit_ = "digit expected:";
    private static final String err_value_ = "invalid value:";
    
    
	public final static String INVALID_ENCODING_XML = "<invalid_encoding/>";
	public final static String[] FILENAME_LIST = { UploadUtils.SUCCESS_FILE, UploadUtils.FAILURE_FILE, UploadUtils.ERROR_FILE};
	private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
	private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
	private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
	private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";    
	private final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
	
	//the format of date
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DF_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DF_DEFAULT = DF_YYYY_MM_DD_HH_MM;
    

    
    private static final String sql_get_usr_ =
          "SELECT usr_id, usr_ent_id  from RegUser WHERE usr_ste_usr_id= ? "
        + " AND usr_ste_ent_id = ? ";
    
    private static final String sql_check_valid_objective_ =
          "SELECT count(*) from objective , syllabus WHERE obj_id = ? "
        + " AND syl_id = obj_syl_id AND syl_ent_id_root = ? ";
    
    
    
    /* remove excessive double quotes in the string.
       since after exporting from EXCEL to delimited text file, there will be double
       quotes surrounding the whole string if there are commas and double quotes
       within the string.
       moreover, existing double quotes within the string will be repeated
       to distinguish them from the added opening and closing double quotes. */
    public static String escString(String inString) {
        char charDubQuo = '"';
        String workString = "";
        String tempString = "";
        int len = inString.length();
        int i = 0;
        
        if (len == 0) return inString;
        
        // check if there exists opening and closing double quotes
        if (inString.charAt(0) == charDubQuo && inString.charAt(len - 1) == charDubQuo) {
            // remove them
            workString = inString.substring(1, len - 1);
            while (true) {
                // check if there exists consecutive double qutoes
                i = workString.indexOf(charDubQuo, i);
                if (i > -1) {
                    if (workString.charAt(i + 1) == charDubQuo) {
                        // reduce every two double quotes to a single double quote
                        tempString = workString.substring(0, i + 1) + workString.substring(i + 2);
                        workString = tempString;
                    }
                    i++;
                } else //no more double quotes
                    break;
            }
        } else // no opening and closing double quotes
            workString = inString;
        
        return workString;
    }


	public static boolean containsNewLine(String val) {
		if( val == null || val.length() == 0 ) {
			return false;
		} else {
			if( val.indexOf('\n') != -1 ) {
				return true;
			} else {
				return false;
			}
		}
	}

    // data type of answer is string
    private static final String typeStr_ = "Text";
    // data type of answer is number
    private static final String typeNum_ = "Number";
    // check if it is a valid data type for a fill-in-the-blank answer
    public static String checkAnsType(String inString) throws cwException {
        if (inString.equalsIgnoreCase(typeStr_))
            return typeStr_;
        else if (inString.equalsIgnoreCase(typeNum_))
            return typeNum_;
        else
            throw new cwException(err_value_ + inString);
    }

    // convert the string into long
    public static long string2long(String inString) throws cwException {
        long outValue = 0;
        if (inString != null) {
            try {
                outValue = Long.parseLong(inString);
            } catch (NumberFormatException e) {
                throw new cwException(err_digit_ + inString);
            }
        }
        return outValue;
    }
    
    // convert the string into integer
    public static int string2int(String inString) throws cwException {
        if (inString == null) return 0;
        int outValue;
        try {
            outValue = Integer.parseInt(inString);
        } catch (NumberFormatException e) {
            throw new cwException("digit expected");
        }
        return outValue;
    }
    
    // convert the string into floating point number
    public static float string2float(String inString) throws cwException {
        if (inString == null) return 0;
        float outValue;
        try {
            Float numObj = new Float(inString);
            outValue = numObj.floatValue();
        } catch (NumberFormatException e) {
            throw new cwException("digit expected");
        }
        return outValue;
    }
    
    // possible answers
    private static final String strAns_ = "AaBbCcDdEe";
    /* check if it is a valid answer */
    public static int checkAns(String inString) throws cwException {
        if (inString.length() != 1 || strAns_.indexOf(inString) < 0)
                throw new cwException("invalid value");
        int ans = 0;
        
        switch(inString.charAt(0)) {
            case 'A':
            case 'a':
                ans = 1;
                break;
            case 'B':
            case 'b':
                ans = 2;
                break;
            case 'C':
            case 'c':
                ans = 3;
                break;
            case 'D':
            case 'd':
                ans = 4;
                break;
            case 'E':
            case 'e':
                ans = 5;
                break;
            default:
                ans = 0;
                
        }
        
        return ans;
    }
    
    // possible levels of difficulty
    private static final String strDiff_ = "123";
    /* check if it is a valid answer */
    public static String checkDiff(String inString) throws cwException {
        if (inString.length() == 1)
            if (strDiff_.indexOf(inString) >= 0)
                return inString;
        throw new cwException("invalid value");
    }
    
    // possible boolean
    private static final String strYesNo_ = "YyNn";
    /* check if it is a valid boolean */
    public static String checkYesNo(String inString) throws cwException {
        if (inString.length() == 1)
            if (strYesNo_.indexOf(inString) >= 0)
                return inString.toUpperCase();
        throw new cwException("invalid value");
    }

	public static String checkYesOrNo(String inString) throws cwException {
		if (inString.equalsIgnoreCase("Y") || inString.equalsIgnoreCase("N"))
				return inString.toUpperCase();
		throw new cwException("invalid value");
	}

    // possible boolean
    private static final String strYes_ = "Yy";
    /* check if it is a valid boolean */
    public static boolean checkBoolean(String inString) throws cwException {
        if (inString.length() == 1) {
            if (strYes_.indexOf(inString) >= 0)
                return true;
            else
                return false;
        }
        
        throw new cwException("invalid value");
    }
    
    // possible file extensions
    private static final String FileExt_[] = {".gif", ".jpg", ".swf"};
    // check if the filename has a recognized file extension
    public static String checkFileExt(String inString) throws cwException {
        if (inString == null)
            return inString;
        if (inString.length() > 0) {
            int period_idx = inString.lastIndexOf(".");
            if (period_idx >= 0) {
                String ext = inString.substring(period_idx);
                int i = 0;
                for (i = 0; i < FileExt_.length; i++)
                    if (ext.equalsIgnoreCase(FileExt_[i])) break;
                if (i < FileExt_.length)
                    return inString;
            }
        }
        throw new cwException("unknown file extension");
    }
    
    // valid online status
    private static final String strOnline_ = "ON";
    // valid offline status
    private static final String strOffline_ = "OFF";
    // check if it is a valid online/offline status
    public static String checkOnOff(String inString) throws cwException {
        if (inString.length() > 0) {
            if (inString.equalsIgnoreCase(strOnline_))
                return strOnline_;
            if (inString.equalsIgnoreCase(strOffline_))
                return strOffline_;
        }
        throw new cwException("invalid value");
    }
    
    // public folder specified in excel
    private static final String folderPublic_ = "PUBLIC";
    // public folder specified in database
    private static final String folderSysPublic_ = "CW";
    // personal folder specified in excel
    private static final String folderPer_ = "PERSONAL";
    // personal folder specified in excel
    private static final String folderSysPer_ = "AUTHOR";
    // check if it is a valid folder
    public static String checkFolder(String inString) throws cwException {
        if (inString.length() > 0) {
            if (inString.equalsIgnoreCase(folderPublic_))
                return folderSysPublic_;
            if (inString.equalsIgnoreCase(folderPer_))
                return folderSysPer_;
        }
        throw new cwException("invalid value");
    }

    public static String checkEmpty(String inString) throws cwException {
        if (inString.length() == 0)
            throw new cwException(err_empty_);
        else
            return inString;
    }
    
    // vertify the conversion between Unicode and the input encoding
    public static String checkEnc(String inString, String enc_) throws cwException {
        try {
            if (inString == null) return inString;
            String retStr = new String(inString.getBytes(enc_), enc_);
            if (inString.equals(retStr))
                return inString;
            else {
            	CommonLog.info(FrUnicodeTo(enc_, retStr));
                throw new cwException("encoding error");
            }
        } catch (IOException e) {
            throw new cwException(e.getMessage());
        }
    }
    
    public static String FrUnicodeTo(String toEnc, String inStr) throws IOException {
        if (inStr == null) return inStr;
        
        String outStr = "";
        PipedInputStream   pipeIn = new PipedInputStream();
        PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
        BufferedReader         in = new BufferedReader(new InputStreamReader(pipeIn));
        // OutputStreamWriter can convert strings from Unicode to any other encodings
        Writer                out = new OutputStreamWriter(pipeOut, toEnc);
        out.write(inStr);
        out.close();
        
        String tempStr = "", breakline = "";
        while ((tempStr = in.readLine()) != null) {
            outStr += breakline + tempStr;
            breakline = System.getProperty("line.separator");
        }
        in.close();
        return outStr;
    }
    
    public static void sysOut(String name , String value) {
    	CommonLog.info(name + " : " + value);
        //System.out.println(name + " : " + value);
    }
    
    /* check if the specified input file has already been processed before */
    /*
    public static void checkInfile(Connection con_, File infile) throws cwException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        
        try {
            if (st_eat_GetLog_ == null)
                st_eat_GetLog_ = con_.prepareStatement(sql_eat_GetLog_);
            
            stmt = st_eat_GetLog_;
            col = 1;
            stmt.setString(col++, infile.getAbsolutePath());
            stmt.setLong(col++, infile.lastModified());
            stmt.setLong(col++, infile.length());
            rs = stmt.executeQuery();
            if (rs.next()) {
                stmt.close();
                throw new cwException("Input file already processed.");
            }
            stmt.close();
        } catch (SQLException e) {
            throw new cwException("SQL Error:" + e.getMessage());
        }
    }
    */
    
    /* log the specified input file */
    /*
    public static void logInfile(Connection con_, File infile, int inreccnt) throws cwException {
        PreparedStatement stmt = null;
        int col = 0;
        int rowcount = 0;
        sysOut("Save Log", "SUCCESS");
        try {
            if (st_eat_InsLog_ == null)
                st_eat_InsLog_ = con_.prepareStatement(sql_eat_InsLog_);
            
            stmt = st_eat_InsLog_;
            col = 1;
            stmt.setString(col++, infile.getAbsolutePath());
            stmt.setLong(col++, infile.lastModified());
            stmt.setLong(col++, infile.length());
            stmt.setInt(col++, inreccnt);
            rowcount = stmt.executeUpdate();
            if (rowcount != 1) {
                stmt.close();
                throw new cwException("Failed to insert log record. (but raw questions are inserted)");
            }
            stmt.close();
        } catch (SQLException e) {
            throw new cwException("SQL Error:" + e.getMessage());
        }
    }
    */
    
    /* log the specified input file */
    public static void getUsr(Connection con_, dbRegUser usr) throws SQLException {
        PreparedStatement stmt = null;
            stmt = con_.prepareStatement(sql_get_usr_);
            stmt.setString(1, usr.usr_ste_usr_id);
            stmt.setLong(2, usr.usr_ste_ent_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usr.usr_id = rs.getString("usr_id");
                usr.usr_ent_id = rs.getLong("usr_ent_id");
            }
            stmt.close();
    }

    /* check if the specified input file has already been processed before */
    public static boolean validObjective(Connection con_, long site_id, long obj_id) throws SQLException {

        boolean bExist = false;

        PreparedStatement stmt = con_.prepareStatement(sql_check_valid_objective_);
        stmt.setLong(1, obj_id);
        stmt.setLong(2, site_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            if (rs.getInt(1) > 0 ) {
                bExist = true;    
            }
        }
        stmt.close();
        return bExist;
    }

    private static Random random = new Random();
    
    public static String genPosDigit(int digitLength){
        int randonInt; 
    //    Random random = new Random();
        randonInt = random.nextInt();
        if (randonInt<0){
            randonInt *= -1;
        }
        
        String strDigit = randonInt + "";
        
        if (strDigit.length() < digitLength){
            int numOfZeroToAdd = digitLength - strDigit.length();
            for (int i=0; i<numOfZeroToAdd; i++) {
                strDigit = "0".concat(strDigit);
            }
        }else if (strDigit.length() > digitLength){
            strDigit = strDigit.substring(0, digitLength);
        }else{
            // nothing to do
        }
        return strDigit;

    }
    
    
    // public "Correctness Logic" 
    private static final String MC_LOGIC_AND     = "AND";    
    private static final String MC_LOGIC_OR     = "OR";
    private static final String MC_LOGIC_SINGLE = "SINGLE";
    
    //check the "Correctness Logic"
    public static String checkCorrectnessLogic(String inString){
        if (inString.length() > 0) {
            if (inString.equalsIgnoreCase(MC_LOGIC_AND))
                return MC_LOGIC_AND;
            if (inString.equalsIgnoreCase(MC_LOGIC_OR))
                return MC_LOGIC_OR;
        }
        return MC_LOGIC_SINGLE;
    }
    


  public static int[] string2IntArray(String str, String delimiter)
  {
        str += delimiter;
        Vector vec = new Vector();
        int index = str.indexOf(delimiter, 0);
        String element;
        while (index > 0) {
            element = str.substring(0, index);
            if(element != null && element.trim().length() > 0)
                vec.addElement(element);
            str = str.substring(index + delimiter.length()); 
            index = str.indexOf(delimiter, 0); 
        }

        int[] ids = new int[vec.size()];
        for(int i=0; i<ids.length; i++)
            ids[i] = Integer.parseInt((String)vec.elementAt(i));

        return ids;
  }

    public void setLogFolder(qdbEnv static_env, String type, long id){
        
    }
    
    
    public static void createLogFolder(qdbEnv static_env, String type, long id){
        File fLogDir = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
        if( !fLogDir.exists() )
            fLogDir.mkdirs();

        fLogDir = new File(fLogDir, type);
        if( !fLogDir.exists() )
            fLogDir.mkdirs();

        fLogDir = new File(fLogDir, Long.toString(id));
        if( !fLogDir.exists() )
            fLogDir.mkdirs();

        return;
    }
 
    
    
    public static void saveUploadedFile(qdbEnv static_env, String type, long id, File srcFile)
        throws IOException {
            
            File fLogDir = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, type);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, Long.toString(id));
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fileSaveAs(srcFile, fLogDir);
            return;
        }

    
    public static void fileSaveAs(File file, File dir)
        throws IOException{
            
            RandomAccessFile f1 = f1 = new RandomAccessFile(file, "r");
            int bufsize = (int) f1.length();
            File nF = new File(dir, file.getName());
            if (nF.exists()) {
                nF.delete();
                nF = new File(dir, file.getName());
            }
            RandomAccessFile f2 = new RandomAccessFile(nF, "rw");
            byte buffer1[] = new byte [bufsize];
            f1.readFully(buffer1, 0, bufsize);
            f2.write(buffer1, 0, bufsize);
            f1.close();
            f2.close();
            return;
        }
    
    
    public void writeErrorMCLog(qdbEnv static_env, long ulg_id, String title, String obj_title, String reason)
        throws IOException {
            
            File fLogDir = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, QUE);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, Long.toString(ulg_id));
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            File errorFile = new File(fLogDir, ERROR_FILE);
            //write UTF8 BOM
            if(!errorFile.exists()){
	            FileOutputStream FWB = new FileOutputStream(errorFile.getAbsolutePath(), false);
	            FWB.write(cwUtils.BOM_UTF8);
	            FWB.flush();
	            FWB.close();
            }
            
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFile.getAbsolutePath(), true), ENC_UTF));
            
            String content = "Failed to add " + title + " to " + obj_title;
            out.write(content.toCharArray());
            out.newLine();
            out.flush();
            out.close();
            return;
        }

    
    
    public void writeSuccessMCLog(qdbEnv static_env, long ulg_id, String title, String obj_title)
        throws IOException {
            
            File fLogDir = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, QUE);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, Long.toString(ulg_id));
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            File errorFile = new File(fLogDir, SUCCESS_FILE);
            //write UTF8 BOM
            if(!errorFile.exists()){
	            FileOutputStream FWB = new FileOutputStream(errorFile.getAbsolutePath(), false);
	            FWB.write(cwUtils.BOM_UTF8);
	            FWB.flush();
	            FWB.close();
            }
            
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFile.getAbsolutePath(), true), ENC_UTF));
            
            String content = title + " added to " + obj_title;
            out.write(content.toCharArray());
            out.newLine();
            out.flush();
            out.close();
            return;
        }

    public void writeFailureMCLog(qdbEnv static_env, long ulg_id, String title, String obj_title)
        throws IOException {
            
            File fLogDir = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, QUE);
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            fLogDir = new File(fLogDir, Long.toString(ulg_id));
            if( !fLogDir.exists() )
                fLogDir.mkdirs();

            File errorFile = new File(fLogDir, FAILURE_FILE);
            //write UTF8 BOM
            if(!errorFile.exists()){
	            FileOutputStream FWB = new FileOutputStream(errorFile.getAbsolutePath(), false);
	            FWB.write(cwUtils.BOM_UTF8);
	            FWB.flush();
	            FWB.close();
            }
            
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFile.getAbsolutePath(), true), ENC_UTF));
            
            String content = "Failed to add " + title + " to " + obj_title;
            out.write(content.toCharArray());
            out.newLine();
            out.flush();
            out.close();
            return;
        }


    public static String readLogContent(qdbEnv static_env, long ulg_id, String filename)
        throws IOException {
            
            File logFile = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( logFile.exists() ){
                logFile = new File(logFile, QUE);
                if( logFile.exists() ) {
                    logFile = new File(logFile, Long.toString(ulg_id));
                    if( logFile.exists() ){
                        logFile = new File(logFile, filename);
                        if( logFile.exists() )
                            return (readFile(logFile)).toString();
                    }
                }
            }
            return null;
        }
 
    
    public static StringBuffer readFile(File file) throws IOException {
        if (!file.exists()){
            throw new IOException("file " + file.getPath() + " not exist");
        }
        
        StringBuffer output = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENC_UTF));
            String inline;
            while ((inline = in.readLine()) != null) {
                output.append(inline).append(cwUtils.NEWL);
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new IOException("file error:" + file.getPath() + " not found." + e.getMessage());
        } catch (IOException e) {
            throw new IOException("read file error:" + file.getPath() + " " + e.getMessage());
        }
        return output;
    }
    
public static String checkList(String inString, Vector v_possible_value) throws cwException {
        if (inString == null){
            return null;
        }

        if (v_possible_value.contains(inString)){
            return inString;
        }else{
            throw new cwException("invalid value: " + inString);
        }
    }
    
    public static String checkCovStatus(String inString) throws cwException {
        if (inString == null){
            return null;
        }else if (/*inString.equalsIgnoreCase("In Progress") || */inString.equalsIgnoreCase("In Progress")){
            return "I";
        }else if (inString.equalsIgnoreCase("Completed")){
            return "C";
        }else if (/*inString.equalsIgnoreCase("Failed") || */inString.equalsIgnoreCase("Failed")){
            return "F";
        /*}else if (inString.equalsIgnoreCase("No Show") || inString.equalsIgnoreCase("Absent")){
            return "N";
        }else if (inString.equalsIgnoreCase("Passed")){
            return "P";
                        */
		}else if (inString.equalsIgnoreCase("Withdrawn")){
			return "W";
        }else{ 
            throw new cwException("invalid completion status: " + inString);
        }
    }

    public static String checkDate(String inString) throws cwException {
        if (inString == null){
            return null;
        }
        try{
            Timestamp ts = Timestamp.valueOf(inString.replace('/', '-') + ":00");
//            return IMSUtils.convertTimestampToISO8601(ts); 
            return ts.toString();
        }catch(IllegalArgumentException e){
            throw new cwException("invalid date format: " + inString);
        }
    }

    public static String checkDecimal(String inString, float min, float max) throws cwException {
        if (inString == null){
            return null;
        }

        try{
            float outValue;
            Float numObj = new Float(inString);
            outValue = numObj.floatValue();
            if (outValue < min || outValue > max ){
                throw new cwException("digit: " + outValue + " must within the range: (" + min + "," + max + ")");
            }
            
            int dotIndex = inString.indexOf(".");
            // allow 1 decimal place only
            if ( dotIndex > -1 && dotIndex < (inString.length() -2)  ){
                throw new cwException("digit should round up to one decimal place.");

            }
            return outValue+"";
        } catch (NumberFormatException e) {
            throw new cwException("digit expected");
        }

    }

    public static boolean checkLength(String inString, int min, int max) throws cwException {
        boolean bValid = true;
        if (inString == null){
            if (min==0){
                bValid = true;
            }else{
                bValid = false;
            }
        }else{
            if (inString.length() >= min &&  inString.length() <= max){
                bValid = true;
            }else{
                bValid = false;
            }
        }
        return bValid;
    }

    public static String getXMLFromIMSFile(String sourceFile, String enc) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
        StringBuffer xmlBuf = new StringBuffer();
        String inline = null;
        while ((inline = in.readLine()) != null) {
            xmlBuf.append(inline);
        }
        String xml = xmlBuf.toString();
        
        int index = xml.toUpperCase().indexOf(ENTERPRISE_START_TAG.toUpperCase());
            
        xml = xml.substring(index);
        
        Perl5Util perl = new Perl5Util();
        if(perl.match("#cwn:#i", xml))
            xml = perl.substitute("s#cwn:##ig", xml);
        if(perl.match("#xmlns:cwn=\"" + WIZBANK_NAME_SPACE + "\"#i", xml))
            xml = perl.substitute("s#xmlns:cwn=\"" + WIZBANK_NAME_SPACE + "\"##ig", xml);
            
        return xml;
    }
    
    
    
	public static boolean isUnicodeFile(File file)
		throws IOException{
			boolean flag = false;
			
			FileInputStream fis = new FileInputStream(file);
			if( fis.read() == 255 && fis.read() == 254 ) {
				flag = true;
			}
			fis.close();
			return flag;
		}    
 
	public static long insUploadLog(Connection con, String usr_id, String ulg_desc, String srcFilename, qdbEnv static_env, String que_type)
		throws SQLException {

			DbUploadLog ulg = new DbUploadLog();
			ulg.ulg_type = DbUploadLog.TYPE_QUE;
			ulg.ulg_subtype = que_type;
			ulg.ulg_usr_id_owner = usr_id;
			ulg.ulg_desc = ulg_desc;
			ulg.ulg_file_name = srcFilename;
			ulg.ulg_create_datetime = cwSQL.getTime(con);
			ulg.ulg_upd_datetime = ulg.ulg_create_datetime;
			ulg.ulg_status = DbUploadLog.STATUS_PENDING;
			ulg.ulg_process = UploadUtils.UPLOAD_PROCESS_IMPORT;
			ulg.ulg_method = UploadUtils.ACTION_TRIGGERED_BY_UI;
			ulg.ins(con);
			UploadUtils.createLogFolder(static_env, UploadUtils.QUE, ulg.ulg_id);
			return ulg.ulg_id;

		}

	public static boolean checkDecimal(String inString, int decpt) throws cwException {
		if (inString == null){
			return false;
		}
		int dotIndex = inString.indexOf(".");
		if ( dotIndex > -1 && dotIndex < (inString.length() - (decpt + 1))  ){
			return false;
		}
		return true;
	}

	public static String invalidColXML(Vector invalidColHeader){
	    return invalidColXML(invalidColHeader, INVALID_COL_TYPE_UNRECOGNIZED);
	}

	public static String invalidColXML(Vector invalidColHeader, String type){
        
		StringBuffer xml = new StringBuffer(512);
		xml.append("<invalid_col_name_list type=\"").append(type).append("\">");
		for(int i=0; i<invalidColHeader.size(); i++)
			xml.append("<invalid_col_name>").append(invalidColHeader.elementAt(i)).append("</invalid_col_name>");
		xml.append("</invalid_col_name_list>");
		return xml.toString();
	}    

	public static String getResultXML(qdbEnv static_env, long ulg_id, Hashtable h_status_count)
		throws cwException, IOException {
            
			StringBuffer xml = new StringBuffer(1024);
			String logFolderUri = UploadUtils.URI_PATH_SEPARATOR
								+ static_env.LOG_FOLDER
								+ UploadUtils.URI_PATH_SEPARATOR
								+ UploadUtils.QUE
								+ UploadUtils.URI_PATH_SEPARATOR
								+ ulg_id
								+ UploadUtils.URI_PATH_SEPARATOR;
                                
			xml.append("<que_import id=\"").append(ulg_id).append("\">");
            
			xml.append("<success_entity>")
			   .append("<total>").append((Integer)h_status_count.get(RawQuestion.SUCCESS_CNT)).append("</total>")
			   .append("<log_file>")
			   .append("<filename>").append(UploadUtils.SUCCESS_FILE).append("</filename>")
			   .append("<uri>").append(logFolderUri + UploadUtils.SUCCESS_FILE).append("</uri>")
			   .append("</log_file>")
			   .append("</success_entity>");
            
			xml.append("<unsuccess_entity>")
			   .append("<total>").append((Integer)h_status_count.get(RawQuestion.FAILURE_CNT)).append("</total>")
			   .append("<log_file>")
			   .append("<filename>").append(UploadUtils.FAILURE_FILE).append("</filename>")
			   .append("<uri>").append(logFolderUri + UploadUtils.FAILURE_FILE).append("</uri>")
			   .append("</log_file>")
			   .append("</unsuccess_entity>");
            
			xml.append("<error>")
			   .append("<log_file>")
			   .append("<filename>").append(UploadUtils.ERROR_FILE).append("</filename>")
			   .append("<uri>").append(logFolderUri + UploadUtils.ERROR_FILE).append("</uri>")
			   .append("</log_file>")
			   .append("<reason>").append(cwUtils.esc4XML(UploadUtils.readLogContent(static_env, ulg_id, UploadUtils.ERROR_FILE))).append("</reason>")
			   .append("</error>");

			xml.append("</que_import>");
            
			return xml.toString();            
		}
 
 
	public static String getHistoryXML(Connection con, HttpSession sess, String que_type, String ulg_process, cwPagination cwPage, qdbEnv static_env)
		throws SQLException, cwException {
            
			StringBuffer xml = new StringBuffer(1024);
			xml.append("<que_log>");
			boolean useSess = false;
            
			if( cwPage.curPage == 0 ) cwPage.curPage = 1;
			if( cwPage.pageSize == 0 ) cwPage.pageSize = 10;
            
			int start = (cwPage.curPage - 1) * cwPage.pageSize + 1;
			int end = start + cwPage.pageSize - 1;
            
			Vector v_ulg_id = null;
			if( sess.getAttribute(SESS_LOG_HISTORY_TIMESTAMP) != null && ((Timestamp)sess.getAttribute(SESS_LOG_HISTORY_TIMESTAMP)).equals(cwPage.ts) ) {
				Vector v_id = (Vector)sess.getAttribute(SESS_LOG_HISTORY_LOG_ID);
				v_ulg_id = new Vector();
				for(int i=start-1; i<end && i<v_id.size(); i++)
					v_ulg_id.addElement((Long)v_id.elementAt(i));
				cwPage.totalPage = ((Integer)sess.getAttribute(SESS_LOG_HISTORY_TOTAL_PAGE)).intValue();
				cwPage.totalRec = ((Integer)sess.getAttribute(SESS_LOG_HISTORY_TOTAL_REC)).intValue();
				useSess = true;
			} else {
				cwPage.ts = cwSQL.getTime(con);
				sess.setAttribute(SESS_LOG_HISTORY_TIMESTAMP, cwPage.ts);
			}

			//ResultSet rs = ViewUploadLog.getHistory(con, ulg_process, UploadUtils.QUE, que_type, DbUploadLog.STATUS_COMPLETED, v_ulg_id, cwPage.sortCol, cwPage.sortOrder);
			ViewUploadLog[] log = ViewUploadLog.getHistory(con, ulg_process, UploadUtils.QUE, que_type, DbUploadLog.STATUS_COMPLETED, v_ulg_id, cwPage.sortCol, cwPage.sortOrder);


			v_ulg_id = new Vector();
			int count = 0;
			for(int i=0; i<log.length; i++) {
		
				count++;
				if( !useSess ) {
					v_ulg_id.addElement(new Long(log[i].ulg_id));
					if( count < start || count > end ){
						continue;
					}
				}
				xml.append("<record ")
				   .append(" id=\"").append(log[i].ulg_id).append("\" ")
				   .append(" timestamp=\"").append(log[i].ulg_create_datetime).append("\" ")
				   .append(" performer=\"").append(cwUtils.esc4XML(log[i].usr_display_bil)).append("\" ")
				   .append(" type=\"").append(log[i].ulg_type).append("\" ")
				   .append(" subtype=\"").append(log[i].ulg_subtype).append("\" ")
				   .append(" method=\"").append(log[i].ulg_method).append("\" ")
				   .append(" process=\"").append(log[i].ulg_process).append("\">");
                
				xml.append("<desc>").append(cwUtils.esc4XML(log[i].ulg_desc)).append("</desc>");
                
				xml.append("<uploaded_file>")
				   .append("<filename>").append(cwUtils.esc4XML(log[i].ulg_file_name)).append("</filename>")
				   .append("<uri>").append(getUploadedFileURI(log[i].ulg_id, log[i].ulg_file_name, static_env)).append("</uri>")
				   .append("</uploaded_file>");

				xml.append("<log_file_list>");
				Hashtable h_file_uri = getLogFilesURI(log[i].ulg_id, static_env);
				for(int j=0; j<3; j++){
					xml.append("<log_file ")
					   .append(" type=\"").append(LOG_LIST[j]).append("\">")
					   .append("<filename>").append(FILENAME_LIST[j]).append("</filename>")
					   .append("<uri>").append((String)h_file_uri.get(FILENAME_LIST[j])).append("</uri>")
					   .append("</log_file>");
				}
				xml.append("</log_file_list>");

				xml.append("</record>");
			}
            
			if( !useSess ) {
				sess.setAttribute(SESS_LOG_HISTORY_LOG_ID, v_ulg_id);
				cwPage.totalRec = v_ulg_id.size();
				cwPage.totalPage = (int)Math.ceil( (float)v_ulg_id.size() / cwPage.pageSize );
				sess.setAttribute(SESS_LOG_HISTORY_TOTAL_PAGE, new Integer(cwPage.totalPage));
				sess.setAttribute(SESS_LOG_HISTORY_TOTAL_REC, new Integer(cwPage.totalRec));
			}
            
			xml.append(cwPage.asXML());
			
			xml.append("</que_log>");
			return xml.toString();
            
		}

	private static String getUploadedFileURI(long ulg_id, String ulg_filename, qdbEnv static_env)
		throws cwException {

			String uri = "";
			File uploadedFile = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
			if( uploadedFile.exists() ) {
				uploadedFile = new File(uploadedFile, UploadUtils.QUE);
				if( uploadedFile.exists() ) {
					uploadedFile = new File(uploadedFile, Long.toString(ulg_id));
					if( uploadedFile.exists() ) {
						uploadedFile = new File(uploadedFile, ulg_filename);
						if( uploadedFile.exists() ) {
							uri = UploadUtils.URI_PATH_SEPARATOR + static_env.LOG_FOLDER
								+ UploadUtils.URI_PATH_SEPARATOR + UploadUtils.QUE
								+ UploadUtils.URI_PATH_SEPARATOR + ulg_id
								+ UploadUtils.URI_PATH_SEPARATOR + cwUtils.esc4XML(ulg_filename);
						}
					}
				}
			}
			return uri;

		}
	private static Hashtable getLogFilesURI(long ulg_id, qdbEnv static_env)
		throws cwException {
            
			Hashtable h_file_uri = new Hashtable();
			String uri = "";
			File logFileFolder = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
			if( logFileFolder.exists() ){
				logFileFolder = new File(logFileFolder, UploadUtils.QUE);
				if( logFileFolder.exists() ) {
					logFileFolder = new File(logFileFolder, Long.toString(ulg_id));
					if( logFileFolder.exists() ){
						for(int i=0; i<3; i++){
							File logFile = new File(logFileFolder, FILENAME_LIST[i]);
							if( logFile.exists() ){
                                        
								uri = UploadUtils.URI_PATH_SEPARATOR + static_env.LOG_FOLDER
									+ UploadUtils.URI_PATH_SEPARATOR + UploadUtils.QUE
									+ UploadUtils.URI_PATH_SEPARATOR + ulg_id
									+ UploadUtils.URI_PATH_SEPARATOR + FILENAME_LIST[i];
                                        
								h_file_uri.put(FILENAME_LIST[i], uri);
                                        
							} else {

								h_file_uri.put(FILENAME_LIST[i], "");

							}
						}
					}
				}
			}
            
			return h_file_uri;
            
		}
    
    public static String checkValidDate(String inString) throws cwException {
        if (inString == null){
            return null;
        }
        if(!isValidDate(inString, DF_YYYY_MM_DD_HH_MM)) {
            throw new cwException("invalid date format: " + inString);
        }
        return (inString.trim()).replace('/', '-') + ":00";
    }
    
    public static boolean isRowEmpty(String[] row) {
        for (int i = 0; i < row.length; i++) {
            if (row[i]==null||row[i].length() > 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
	 * check the string is the specified format of date
	 * @param inString
	 * @param dateFormat the format of date
	 * @return true if string is valid format of date
	 */
	public static boolean isValidDate(String inString, String dateFormat) {
		boolean isValid = false;
		if(inString != null) {
			if(dateFormat == null) {
				dateFormat = DF_DEFAULT;
			}
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		        sdf.setLenient(false);
		        sdf.parse(inString.trim().replace('/', '-'));
		        isValid = true;
			} catch(ParseException e) {
			}
		}
        return isValid;
	}
	
	/**
	 * convert the string to timestamp.
	 * <br>
	 * this method doesn't support the nanos field of Timestamp because of the limition of SimpleDateFormat class. 
	 * @param inString
	 * @param dateFormat
	 * @return
	 */
	public static Timestamp toTimestamp(String inString, String dateFormat) {
		Timestamp dateObj = null;
		if(inString != null) {
			if(dateFormat == null) {
				dateFormat = DF_DEFAULT;
			}
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		        sdf.setLenient(false);
		        dateObj = new Timestamp(sdf.parse(inString.trim().replace('/', '-')).getTime());
			} catch(ParseException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
    	return dateObj;
    }
}