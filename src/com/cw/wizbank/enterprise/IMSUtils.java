package com.cw.wizbank.enterprise;
import java.lang.Long;
import java.sql.Timestamp;
import java.util.List;
import java.util.Calendar;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
/*
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
*/
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBException;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwIniFile;
import com.cwn.wizbank.utils.CommonLog;

import org.imsglobal.enterprise.*;

public class IMSUtils{
    public static final String SOURCE = "CW-WIZBANK";
    public static final String DATA_SOURCE = "wizbank_v3_5";
    public static final String SUCCESS_FILE = "success.txt";
    public static final String FAILURE_FILE = "failure.txt";
    public static final String CLEAN_FILE = "clean.txt";
    public static final String ERROR_FILE = "error.txt";
    public static final String ENC_UTF = "UTF-8";

    public static final int SAVE_CODE_INSERT = 1;
    public static final int SAVE_CODE_UPDATE = 2;
    public static final int SAVE_CODE_NOT_SAVE = 3;
    public static final int SAVE_CODE_RESET = 4;

    private static String headerCode =null;

    private static boolean debug = false;
    
//    public static String inifile;
    public static String logDir;
//    public static long siteId;
    
    public static void setDebug(boolean value){
        debug = value;
    }
    
    public static SourcedidType createSourcedid(String id) throws JAXBException {
        ObjectFactory objFactory = new ObjectFactory();
        SourcedidType sourcedid = objFactory.createSourcedidType();
        sourcedid.setSource(IMSEnterpriseApp.exportSource);
        sourcedid.setId(id);
        
        return sourcedid;                
    }

    public static MemberType createMemberObject(String memberID, String roleId, String subRole, String idtype) throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();

        MemberType member = objFactory.createMemberType();
        SourcedidType sourcedid = objFactory.createSourcedidType();
        sourcedid.setSource(IMSEnterpriseApp.exportSource);
        sourcedid.setId(memberID);
        member.setSourcedid(sourcedid);
        member.setIdtype(idtype);
                                
        List roleList = member.getRole();
        RoleType role = objFactory.createRoleType();
        role.setRoletype(roleId);
        if (subRole!=null){
            role.setSubrole(subRole);
        }
        role.setStatus("1");
        roleList.add(role);
        return member;
    }    
    
    public static GroupType createDummySuperviseGroup(String groupId, String superviseType) throws JAXBException{
        org.imsglobal.enterprise.ObjectFactory objFactory = new org.imsglobal.enterprise.ObjectFactory();
        String scheme;
        if (superviseType.equals("APPG")){
            scheme = "cwn approvinggroup";
        }else if (superviseType.equals("SUPV")){
            scheme = "cwn supervisegroup";
        }else{
            scheme = "cwn directsupervisegroup";
        }
        
        GroupType dummyGroup = objFactory.createGroupType();
        List sourcedidList = dummyGroup.getSourcedid();
        SourcedidType sourcedid = objFactory.createSourcedidType();
        sourcedid.setSource(IMSEnterpriseApp.exportSource);
        sourcedid.setId(groupId);
        sourcedidList.add(sourcedid);
        
        List groupTypeList = dummyGroup.getGrouptype();
        GroupType.GrouptypeType grouptype = objFactory.createGroupTypeGrouptypeType();
        grouptype.setScheme(scheme);
        
        List groupTypeValueList = grouptype.getTypevalue();
        TypevalueType typevalue = objFactory.createTypevalueType();
        typevalue.setLevel("1");
        typevalue.setValue(superviseType);
        groupTypeValueList.add(typevalue);
        
        groupTypeList.add(grouptype);
        
        DescriptionType description = objFactory.createDescriptionType();
        description.setShort(groupId);
        description.setLong(groupId);
        
        dummyGroup.setDescription(description);
        return dummyGroup;
   }

    public static void setLogDir(String destinLogDir){
        logDir = destinLogDir;
    }
    
    public static String getLogDir(String rootLogDir, long id){
        if( rootLogDir == null )
            logDir = "";
        else
            logDir = rootLogDir + File.separator;
        
        logDir += Long.toString(id);
        File fLogDir = new File(logDir);
        if( !fLogDir.exists() )
            fLogDir.mkdirs();
            
        return logDir;
    }
    
    public static String getLogDir(String rootLogDir, Timestamp curTime){
        if (rootLogDir==null){
            logDir="";
        }else{
            logDir=rootLogDir + File.separator;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(curTime);
        logDir += cal.get(Calendar.YEAR) + 
        "" + time2String(cal, Calendar.MONTH) + 
        time2String(cal, Calendar.DAY_OF_MONTH) + "-" + time2String(cal, Calendar.HOUR_OF_DAY) + time2String(cal, Calendar.MINUTE);
        File fLogDir =  new  File(logDir);
        if  (!fLogDir.exists()){
            fLogDir.mkdirs();
        }
        
        return logDir;
    }
    
    private static String time2String(Calendar cal, int field){
        String result = "";
        if (field == Calendar.MONTH){
            result = (cal.get(field) + 1) + "";
        }else{
            result = (cal.get(field)) + "";
        }

        while (result.length()<2){
            result = "0" + result;    
        }
        return result;
    }
    
    /**
    Append an Exception's Stack Trace to a file.
    @param filename full path of the output file
    @param ine Exception to be traced
    */
    public static void writeLog(String filename, Exception ine){
        try{
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logDir + File.separator + filename, true), ENC_UTF));
            if (debug){
                ine.printStackTrace(out);
                CommonLog.debug(ine.getMessage(),ine);
            }else{
                out.write(ine.getMessage());
                CommonLog.info(ine.getMessage(),ine);
            }
            out.close();
        }catch (Exception e){
            CommonLog.error("write file exception:" + e.getMessage(),e);
        }
    }
    
    /**
    Append a String content and an Exception's Stack Trace to a file.
    @param filename full path of the output file
    @param content String argument to be appended to file
    @param ine Exception to be traced
    */
    public static void writeLog(String filename, String content, Exception ine){
        try{
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logDir + File.separator + filename, true), ENC_UTF));
            out.write(content + System.getProperty("line.separator"));
            out.flush();
            if (debug){
                ine.printStackTrace(out);
            }else{
                out.write(ine.getMessage() + System.getProperty("line.separator"));
            }
            out.close();
        }catch (Exception e){
            System.err.println("write file exception:" + e.getMessage());
        }
    }

    /**
    Append a String content a file.
    @param filename full path of the output file
    @param content String argument to be appended to file
    */
    public static void writeLog(String filename, String content){
        try{
            String logFile = logDir + File.separator + filename;
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), ENC_UTF));
            out.write(content.toCharArray());
            out.newLine();
            out.flush();
            out.close();
        }catch (Exception e){
            CommonLog.error(e.getMessage(),e);
            System.err.println("write file exception:" + e.getMessage());
        }
    }
    
    /**
    * Get current date in YYYY-MM-DD format
    * @return return the date in a string 
    */
    public static String getFormatedCurrentDate(Connection con) throws SQLException {
        java.util.Date ts = new java.util.Date(cwSQL.getTime(con).getTime());
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        return (fm.format(ts)).toString();        
    }
    
    /**
    * Construct a timestamp by input a string in YYYY-MM-DD foramt
    */
    public static Timestamp constructTimestamp(String str) throws Exception{
	    Timestamp timestamp = null;
	    String[] val = cwUtils.splitToString(str, "-");
	    if(val == null || val.length != 3 )
		    throw new Exception();
	    else
		    try{
		        String _timestamp = val[0] + "-" + val[1] + "-" + val[2] + " 00:00:00.00";
			    timestamp = Timestamp.valueOf(_timestamp);
		    }catch(Exception e){
			    throw new Exception();
		    }
	    return timestamp;	

    }    
    
    /**
    Convert the input String of timestamp, in ISO8601 format, to a Java Timestamp format.
    Supported ISO8601-Compliant Timestamp by this method: YYYY-MM-DDThh:mm:ss.
    i.e. convert YYYY-MM-DDThh:mm:ss to YYYY-MM-DD hh:mm:ss
    Details please refer to http://www.w3.org/TR/NOTE-datetime .
    */
    public static String convertTimestampFromISO8601(String ISO8601Timestamp) {
        StringBuffer javaTimestamp = new StringBuffer(ISO8601Timestamp);
        int index = ISO8601Timestamp.indexOf('T');
        if(index > 0) {
            javaTimestamp.setCharAt(index, ' ');
        }
        return javaTimestamp.toString();
    }

    /**
    * Convert a timestamp in Java Timestamp format to ISO08601 format<br>
    * i.e. YYYY-MM-DD hh:mm:ss.fff to YYYY-MM-DDThh:mm:ss
    */
    public static String convertTimestampToISO8601(Timestamp timestamp){
        java.util.Date ts = new java.util.Date(timestamp.getTime());
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return (fm.format(ts)).toString();  
    }
    
    /**
    Convert the the input Hashtable's values to Vector
    @param h Hashtable to be converted
    @return A Vector of the values contained in input Hashtable argument
    */
    public static Vector hashValuesAsVector(Hashtable h) {
        Enumeration enumeration = h.keys();
        Vector v = new Vector();
        while(enumeration.hasMoreElements()) {
            v.addElement(h.get(enumeration.nextElement()));
        }
        return v;
    }
    
    public static boolean isErrorOccur(){
        boolean errorOccur = false;
        try{
                File failureLog = new File(logDir + File.separator + FAILURE_FILE);
                if (failureLog.exists())   errorOccur = true;
                
                File errorLog = new File(logDir + File.separator + ERROR_FILE);
                if (errorLog.exists())   errorOccur = true;
                
        }catch (Exception e){
            errorOccur = true;
        }
        return errorOccur;
    }
        
    public static String getAttachCode(cwIniFile ini){
        
    	String logAttachTypeStr = ini.getValue("SEND_MAIL_ATTACH_CONDITION");
    	String []logAttachType = cwUtils.splitToString(logAttachTypeStr, "~");
        StringBuffer attachCode = new StringBuffer();
        
        for(int i = 0; i < logAttachType.length; i++){
        	if(logAttachType[i] != null && logAttachType[i].equalsIgnoreCase("SUCCESS")){
	        	try{
	                File successLog = new File(logDir + File.separator + SUCCESS_FILE);
	                if (successLog.exists())   attachCode.append(getAttachCodeByFile(ini, successLog));
	            }catch (Exception e){
	                writeLog(FAILURE_FILE, e);    
	            }
        	} else if(logAttachType[i] != null && logAttachType[i].equalsIgnoreCase("FAILURE")){
                try{
                    File failureLog = new File(logDir + File.separator + FAILURE_FILE);
                    if (failureLog.exists())   attachCode.append(getAttachCodeByFile(ini, failureLog));
                }catch (Exception e){
                    writeLog(FAILURE_FILE, e);    
                }
        	} else if(logAttachType[i] != null && logAttachType[i].equalsIgnoreCase("ERROR")){
        		try{           
                    File errorLog = new File(logDir + File.separator + FAILURE_FILE);
                    if (errorLog.exists())   attachCode.append(getAttachCodeByFile(ini, errorLog));
                }catch (Exception e){
                    System.err.println("ERROR IN ATTACH ERROR_LOG");
                    writeLog(FAILURE_FILE, e);    
                }
        	} else if(logAttachType[i] != null && logAttachType[i].equalsIgnoreCase("CLEAN")){
        		try{            
                    File cleanLog = new File(logDir + File.separator + CLEAN_FILE);
                    if (cleanLog.exists())   attachCode.append(getAttachCodeByFile(ini, cleanLog));
                }catch (Exception e){
                    System.err.println("ERROR IN ATTACH ERROR_LOG");
                    writeLog(FAILURE_FILE, e);    
                }
        	}
        }
        return attachCode.toString();
    }
    
    public static StringBuffer getAttachCodeByFile(cwIniFile ini, File attachFile) throws IOException{
        StringBuffer attachCode = new StringBuffer();
        if (headerCode == null){
            headerCode = ini.getValue("MAIL_MIME_HEADER_CODE");   
        }
        attachCode.append(headerCode).append(cwUtils.NEWL);
        attachCode.append("Content-Type: text/plain;").append(cwUtils.NEWL);
        attachCode.append('\t').append("name=\"").append(attachFile.getName()).append("\"").append(cwUtils.NEWL);
        attachCode.append("Content-Transfer-Encoding: quoted-printable").append(cwUtils.NEWL);
        attachCode.append("Content-Disposition: attachment;").append(cwUtils.NEWL);
        attachCode.append('\t').append("filename=\"").append(attachFile.getName()).append("\"").append(cwUtils.NEWL);
        attachCode.append(cwUtils.NEWL);
        attachCode.append(readFile(attachFile));
        return attachCode;
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

        }

}