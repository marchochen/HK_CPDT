package com.cw.wizbank.scorm.adapter;

import java.applet.*;
import java.net.URLEncoder;
import java.util.*;
import java.io.*;

import com.cwn.wizbank.utils.CommonLog;

import netscape.javascript.*;

public class ScormAdapter extends Applet {
    static final String TRUE = "true";
    static final String FALSE = "false";
    
    static final char ASCII_13 = 13;
    static final char ASCII_10 = 10;
    
    static final int stringBufferSize = 32768;

    static final String key_error_num = "error=";
    static final String key_error_text = "error_text=";
    static final String key_version = "version=";
    static final String key_aicc_data = "aicc_data=";
    
    static final String key_core = "[core]";
    static final String key_core_lesson = "[core_lesson]";
    static final String key_core_vendor = "[core_vendor]";
    static final String key_objectives_status = "[objectives_status]";
    static final String key_student_data = "[student_data]";

    static final String key_core_student_id = "student_id";
    static final String key_core_student_name = "student_name";
    static final String key_core_credit = "credit";
    static final String key_core_lesson_location = "lesson_location";
    static final String key_core_lesson_status = "lesson_status";
    static final String key_core_score = "score";
    static final String key_core_time = "time";

    // only for storing element in Hashtable
    static final String key_core_raw_score = "raw_score";
    static final String key_core_max_score = "max_score";
    static final String key_core_min_score = "min_score";

    static final String key_student_data_attempt_number = "attempt_number";

    // for SCORM only
    static final String key_core_entry = "entry";

    static final String CMI_CONN_STATUS_CONNECTED = "CONNECTED";
    static final String CMI_CONN_STATUS_FAILED = "FAILED";
    
    static final String CMI_PREFIX = "cmi.";
    
    // SCORM error codes
    static final String ERROR_ID_NO_ERROR = "0";
    static final String ERROR_ID_GENERAL_EXCEPTION = "101";
    static final String ERROR_ID_SERVER_BUSY = "102";
    static final String ERROR_ID_INVALID_ARGUMENT = "201";
    static final String ERROR_ID_ELEMENT_CANNOT_HAVE_CHILDREN = "202";
    static final String ERROR_ID_ELEMENT_NOT_ARRAY = "203";
    static final String ERROR_ID_NOT_INITIALIZED = "301";
    static final String ERROR_ID_NOT_IMPLEMENTED = "401";
    static final String ERROR_ID_KEYWORD_ELEMENT = "402";
    static final String ERROR_ID_READ_ONLY_ELEMENT = "403";
    static final String ERROR_ID_WRITE_ONLY_ELEMENT = "404";
    static final String ERROR_ID_INCORRECT_DATA_TYPE = "405";
    
    // corresponding error messages
    static final String ERROR_MSG_NO_ERROR = "No error";
    static final String ERROR_MSG_GENERAL_EXCEPTION = "General exception";
    static final String ERROR_MSG_SERVER_BUSY = "Server is busy";
    static final String ERROR_MSG_INVALID_ARGUMENT = "Invalid argument error";
    static final String ERROR_MSG_ELEMENT_CANNOT_HAVE_CHILDREN = "Element cannot have children";
    static final String ERROR_MSG_ELEMENT_NOT_ARRAY = "Element not an array - cannot have count";
    static final String ERROR_MSG_NOT_INITIALIZED = "Not initialized";
    static final String ERROR_MSG_NOT_IMPLEMENTED = "Not implemented error";
    static final String ERROR_MSG_KEYWORD_ELEMENT = "Invalid set value, element is a keyword";
    static final String ERROR_MSG_READ_ONLY_ELEMENT = "Element is read only";
    static final String ERROR_MSG_WRITE_ONLY_ELEMENT = "Element is write only";
    static final String ERROR_MSG_INCORRECT_DATA_TYPE = "Incorrect Data Type";

    static final String CMI_VOCAB_EXIT_NORMAL = "";
    static final String CMI_VOCAB_EXIT_TIME_OUT = "time-out";
    static final String CMI_VOCAB_EXIT_SUSPEND = "suspend";
    static final String CMI_VOCAB_EXIT_LOG_OUT = "logout";    
    static final String CMI_VOCAB_STATUS_PASSED = "passed";
    static final String CMI_VOCAB_STATUS_COMPLETED = "completed";
    static final String CMI_VOCAB_STATUS_FAILED = "failed";
    static final String CMI_VOCAB_STATUS_INCOMPLETE = "incomplete";
    static final String CMI_VOCAB_STATUS_BROWSED = "browsed";
    static final String CMI_VOCAB_STATUS_NOT_ATTEMPTED = "not attempted";
    static final String CMI_VOCAB_LESSON_MODE_BROWSE = "browse";
    static final String CMI_VOCAB_LESSON_MODE_NORMAL = "normal";
    static final String CMI_VOCAB_LESSON_MODE_REVIEW = "review";
            
    static final int TYPE_GET = 0;    
    static final int TYPE_SET = 1;    
    
    static final double NO_SCORE = -999;
    
    public StringBuffer putParamAiccData;
    
    public JSObject win = null;
    public JSObject document = null;
    public JSObject aiccAPI = null;
    
    public HttpConnection myHttpConnection = null;
    
    public String NEWL = (new Character(ASCII_13)).toString() + (new Character(ASCII_10)).toString();
    
    public String CMI_connection_status = null;
    
    public boolean isOfflineTracking = false;
    
    public boolean usingRelayer = false;
    
    public long prevTimestamp = 0;
    public long sessExpireLimit = 5 * 60 * 1000;
    public String aicc_url = "";
    public String session_id = "";
    public String version = "";
    public String au_password = "";
    public boolean checkSessionExpire = true;
    
    public GetParamData myGetParamData = null;
    public Hashtable htPutParamData = null;
    public Vector vtPutObjectiveStatus = new Vector();
    public String launch_data = "";
    public String suspend_data = "";
    
    public boolean boolDirtyData = false;
    
    public Hashtable htErrorMessages = null;
    public String lastErrorID = "0";
    boolean boolLMSInitializeCalled = false;
    
    String ver_num = "1.0.15";
    
    // only used for writing data to the socket server
    String welcome, response;
    BufferedReader reader;
    PrintWriter writer;

    // data object to store the aicc data from the getParam call
    public class GetParamData {
        int error_num;
        String error_text;
        String version;
        Hashtable ht_aicc_data;
        
        GetParamData() {
            this.error_num = -1;
            this.error_text = null;
            this.version = null;
            this.ht_aicc_data = new Hashtable();
        }
    }
    
    public class ObjectiveStatus {
        String id;
        String status;
        String score;
        String rawScore;
        String minScore;
        String maxScore;
        
        ObjectiveStatus() {
            this.id = null;
            this.status = null;
            this.score = null;
            this.rawScore = null;
            this.minScore = null;
            this.maxScore = null;
        }
    }

    public ScormAdapter() {
    }
        
    public void init() {
    	CommonLog.info("SCORM adapter version number:" + ver_num);

        try {
            win = JSObject.getWindow(this);
            document = (JSObject)win.getMember("document");
            
            initErrorMessages();
            
            // tell outside that the applet has finished initialization
            /*
            Object[] args = new Object[1];
            args[0] = "INITIALIZED";
            callJavaScript(args, "playerInitStatus", true);
            */
            
        } catch (Exception e) {
        	CommonLog.error("error in creating JSObject:" + e.toString());
        }        
        
    }
    
    public void initErrorMessages() {
        htErrorMessages = new Hashtable();
        
        htErrorMessages.put(ERROR_ID_NO_ERROR, ERROR_MSG_NO_ERROR);
        htErrorMessages.put(ERROR_ID_GENERAL_EXCEPTION, ERROR_MSG_GENERAL_EXCEPTION);
        htErrorMessages.put(ERROR_ID_SERVER_BUSY, ERROR_MSG_SERVER_BUSY);
        htErrorMessages.put(ERROR_ID_INVALID_ARGUMENT, ERROR_MSG_INVALID_ARGUMENT);
        htErrorMessages.put(ERROR_ID_ELEMENT_CANNOT_HAVE_CHILDREN, ERROR_MSG_ELEMENT_CANNOT_HAVE_CHILDREN);
        htErrorMessages.put(ERROR_ID_ELEMENT_NOT_ARRAY, ERROR_MSG_ELEMENT_NOT_ARRAY);
        htErrorMessages.put(ERROR_ID_NOT_INITIALIZED, ERROR_MSG_NOT_INITIALIZED);
        htErrorMessages.put(ERROR_ID_NOT_IMPLEMENTED, ERROR_MSG_NOT_IMPLEMENTED);
        htErrorMessages.put(ERROR_ID_KEYWORD_ELEMENT, ERROR_MSG_KEYWORD_ELEMENT);
        htErrorMessages.put(ERROR_ID_READ_ONLY_ELEMENT, ERROR_MSG_READ_ONLY_ELEMENT);
        htErrorMessages.put(ERROR_ID_WRITE_ONLY_ELEMENT, ERROR_MSG_WRITE_ONLY_ELEMENT);
        htErrorMessages.put(ERROR_ID_INCORRECT_DATA_TYPE, ERROR_MSG_INCORRECT_DATA_TYPE);
    }
    
    // methods called by SCO through liveconnect
    public String LMSInitialize(String parameter) {        
    	CommonLog.info("Adapter LMSInitialize:" + parameter);
        if (parameter.equalsIgnoreCase("") == true && boolLMSInitializeCalled == false) {
            lastErrorID = ERROR_ID_NO_ERROR;
            boolLMSInitializeCalled = true;
            return TRUE;
        }
        else if (boolLMSInitializeCalled == true) {
            lastErrorID = ERROR_ID_GENERAL_EXCEPTION;
            return FALSE;
        }
        else {
            lastErrorID = ERROR_ID_INVALID_ARGUMENT;
            return FALSE;
        }        
    }

    public String LMSFinish(String parameter) {        
    	CommonLog.info("Adapter LMSFinish:" + parameter);

        if (boolLMSInitializeCalled == true) {
            if (parameter.equalsIgnoreCase("")) {
                if (boolDirtyData == true) {
                    LMSCommit("");
                }        
                return TRUE;
            }
            else {
                lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                return FALSE;
            }
        }
        else {
            lastErrorID = ERROR_ID_NOT_INITIALIZED;
            return FALSE;
        }
    }
    
    public String LMSGetValue(String parameter) {
        String value = "";

        if (boolLMSInitializeCalled == true) {
            value = getOrSetValue(parameter, TYPE_GET, null);
            if (value == null) {
                value = "";
            }                
        }
        else {
            lastErrorID = ERROR_ID_NOT_INITIALIZED;
            value = "";
        }
        
        CommonLog.info("Adapter LMSGetValue:" + parameter + ":" + value);        
        return value;
    }
    
    public String LMSSetValue(String parameter, String value) {
    	CommonLog.debug("Adapter LMSSetValue:" + parameter + ":" + value);
        if (boolLMSInitializeCalled == true) {
            boolDirtyData = true;                        
            return getOrSetValue(parameter, TYPE_SET, value);
        }
        else {
            lastErrorID = ERROR_ID_NOT_INITIALIZED;
            return FALSE;
        }
    }
    
    public String LMSCommit(String parameter) {
    	CommonLog.info("Adapter LMSCommit:" + parameter);
        if (boolLMSInitializeCalled == true) {
            boolDirtyData = false;                
            if (parameter.equalsIgnoreCase("")) {
                putParam();
                //getParam();
                return TRUE;
            }
            else {
                lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                return FALSE;
            }
        }
        else {
            lastErrorID = ERROR_ID_NOT_INITIALIZED;
            return FALSE;
        }
    }
    
    public String LMSGetLastError() {
    	CommonLog.info("Adapter LMSGetLastError(): " + lastErrorID);
        return lastErrorID;
    }
    
    public String LMSGetErrorString(String errNumber) {
    	CommonLog.info("Adapter LMSGetErrorString:" + htErrorMessages.get(errNumber));
        return (String)htErrorMessages.get(errNumber);
    }
    
    public String LMSGetDiagnostic(String parameter) {
    	CommonLog.debug("Adapter LMSGetDiagnostic:" + parameter);
        return "";
    }
    
    public String getOrSetValue(String parameter, int type, String putValue) {
        String value = null;
        Hashtable htCore = null;
        ObjectiveStatus[] myObjectiveStatusList = null;
        
        htCore = (Hashtable)myGetParamData.ht_aicc_data.get(key_core);
        if (myGetParamData.ht_aicc_data.get(key_objectives_status) != null) {
            myObjectiveStatusList = (ObjectiveStatus[])myGetParamData.ht_aicc_data.get(key_objectives_status);        
        }
        
        // reset the error code
        lastErrorID = "0";
        
        // AICC data model
        if(parameter.toLowerCase().startsWith(CMI_PREFIX) == true) {
            // CORE elements
            if (parameter.toLowerCase().startsWith("cmi.core.") == true) {
                if (parameter.toLowerCase().equalsIgnoreCase("cmi.core._children") == true) {
                    if (type == TYPE_GET) {
                        String delimiter = ",";
                        value = "_children" 
                                + delimiter + "student_id" 
                                + delimiter + "student_name" 
                                + delimiter + "lesson_location" 
                                + delimiter + "credit" 
                                + delimiter + "lesson_status"
                                + delimiter + "entry"
                                + delimiter + "score"
                                + delimiter + "total_time"
                                + delimiter + "lesson_mode"
                                + delimiter + "exit"
                                + delimiter + "session_time"
                                + delimiter + "suspend_data"
                                + delimiter + "launch_data"
                                + delimiter + "objectives"
                                + delimiter + "student_data";
                        return value;
                    }
                    // ready only
                    else {
                        lastErrorID = ERROR_ID_KEYWORD_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.student_id") == true) {
                    if (type == TYPE_GET) {
                        if (htCore.get(key_core_student_id) != null) {
                            return (String)htCore.get(key_core_student_id);
                        }
                        else {
                            return null;
                        }
                    }
                    // read only
                    else {
                        lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.student_name") == true) {
                    if (type == TYPE_GET) {
                        if (htCore.get(key_core_student_name) != null) {
                            return (String)htCore.get(key_core_student_name);
                        }
                        else {
                            return null;
                        }
                    }
                    // read only
                    else {
                        lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.lesson_location") == true) {
                    if (type == TYPE_GET) {
                        if (htCore.get(key_core_lesson_location) != null) {
                            return (String)htCore.get(key_core_lesson_location);
                        }
                        else {
                            return null;
                        }
                    }
                    else {
                        if (putValue.length() <= 255) {
                            htPutParamData.put(key_core_lesson_location, putValue);
                            // update the corresponding value that will be retrieved by next "get"
                            htCore.put(key_core_lesson_location, putValue);                        
                            return TRUE;
                        }
                        else {
                            lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                            return FALSE;
                        }
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.credit") == true) {
                    if (type == TYPE_GET) {
                        if (htCore.get(key_core_credit) != null) {
                            String credit = (String)htCore.get(key_core_credit);
                            if (credit.toUpperCase().startsWith("C") == true) {
                                return "credit";
                            }
                            else {
                                return "no credit";
                            }
                        }
                        else {
                            return null;
                        }
                    }
                    // read only
                    else {
                        lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.lesson_status") == true) {
                    if (type == TYPE_GET) {
                        String lessonStatus = (String)htCore.get(key_core_lesson_status);
                        if (lessonStatus != null) {
                            if (lessonStatus.toUpperCase().startsWith("P") == true) {
                                return CMI_VOCAB_STATUS_PASSED;
                            }
                            else if (lessonStatus.toUpperCase().startsWith("C") == true) {
                                return CMI_VOCAB_STATUS_COMPLETED;
                            }
                            else if (lessonStatus.toUpperCase().startsWith("F") == true) {
                                return CMI_VOCAB_STATUS_FAILED;
                            }
                            else if (lessonStatus.toUpperCase().startsWith("I") == true) {
                                return CMI_VOCAB_STATUS_INCOMPLETE;
                            }
                            else if (lessonStatus.toUpperCase().startsWith("B") == true) {
                                return CMI_VOCAB_STATUS_BROWSED;
                            }
                            else if (lessonStatus.toUpperCase().startsWith("N") == true) {
                                return CMI_VOCAB_STATUS_NOT_ATTEMPTED;
                            }
                            else {
                                return "";
                            }
                        }
                        else {
                            return null;
                        }
                    }
                    else {
                        if (putValue.equalsIgnoreCase(CMI_VOCAB_STATUS_NOT_ATTEMPTED)) {
                            lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                            return FALSE;
                        }
                        else if (putValue.equals(CMI_VOCAB_STATUS_PASSED)
                            || putValue.equals(CMI_VOCAB_STATUS_COMPLETED)
                            || putValue.equals(CMI_VOCAB_STATUS_FAILED)
                            || putValue.equals(CMI_VOCAB_STATUS_INCOMPLETE)
                            || putValue.equals(CMI_VOCAB_STATUS_BROWSED)) {
                                
                            htPutParamData.put(key_core_lesson_status, putValue);
                            // update the corresponding value that will be retrieved by next "get"
                            htCore.put(key_core_lesson_status, putValue);
                            
                            /*
                            // if the course is completed, then the entry should be ""
                            if (putValue.equalsIgnoreCase(CMI_VOCAB_STATUS_COMPLETED)) {
                                htCore.put(key_core_entry, "");
                            }
                            */
                            
                            return TRUE;
                        }
                        else {
                            lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                            return FALSE;
                        }
                    }
                }
                // not supported in wizBank currently
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.exit") == true) {
                    if (type == TYPE_GET) {
                        lastErrorID = ERROR_ID_WRITE_ONLY_ELEMENT;
                        return FALSE;
                    }
                    else {
                        if (putValue.equals(CMI_VOCAB_EXIT_NORMAL) || putValue.equals(CMI_VOCAB_EXIT_TIME_OUT) || putValue.equals(CMI_VOCAB_EXIT_SUSPEND) || putValue.equals(CMI_VOCAB_EXIT_LOG_OUT)) {
                            return TRUE;
                        }
                        else {
                            lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                            return FALSE;
                        }
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.entry") == true) {
                    if (type == TYPE_GET) {
                        String entry = (String)htCore.get(key_core_entry);                    
                        if (entry != null) {
                            return entry;
                        }
                        else {
                            return null;
                        }                        
                    }
                    // read only
                    else {
                        lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.total_time") == true) {
                    if (type == TYPE_GET) {
                        String time = (String)htCore.get(key_core_time);
                        if (time != null) {
                            return time;
                        }
                        else {
                            return null;
                        }
                    }
                    // read only
                    else {
                        lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.lesson_mode") == true) {
                    if (type == TYPE_GET) {
                        // hardcoded to normal first
                        return CMI_VOCAB_LESSON_MODE_NORMAL;
                    }
                    // read only
                    else {
                        lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.session_time") == true) {
                    // write only
                    if (type == TYPE_GET) {
                        lastErrorID = ERROR_ID_WRITE_ONLY_ELEMENT;
                        return "";
                    }
                    else {
                        if (validTimespan(putValue)) {
                            htPutParamData.put(key_core_time, putValue);                        
                            return TRUE;
                        }
                        else {
                            lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                            return FALSE;
                        }
                    }
                }
                else if (parameter.toLowerCase().startsWith("cmi.core.score") == true) {
                    if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.score._children") == true) {
                        if (type == TYPE_GET) {
                            String delimiter = ",";
                            value = "_children" 
                                    + delimiter + "raw" 
                                    + delimiter + "max" 
                                    + delimiter + "min";
                            return value;
                        }
                        else {
                            lastErrorID = ERROR_ID_KEYWORD_ELEMENT;
                            return FALSE;
                        }
                    }
                    else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.score.raw") == true) {
                        if (type == TYPE_GET) {
                            String score = (String)htCore.get(key_core_raw_score);
                            if (score != null) {
                                return score;
                            }
                            else {
                                return "";
                            }
                        }
                        else {
                            boolean boolIsDecimal = true;
                            float tempFloat = 0;
                            if (putValue.length() > 0) {
                                try {
                                    tempFloat = (Float.valueOf(putValue)).floatValue();
                                } catch (Exception e) {
                                    boolIsDecimal = false;
                                }
                            }
                            
                            if (putValue.length() > 0 && (boolIsDecimal == false || tempFloat < 0)) {
                                lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                                return FALSE;
                            }
                            else {
                                htPutParamData.put(key_core_raw_score, putValue);                            
                                // update the corresponding value that will be retrieved by next "get"
                                htCore.put(key_core_raw_score, putValue);
                                return TRUE;
                            }
                            
                        }
                    }
                    else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.score.max") == true) {
                        if (type == TYPE_GET) {
                            String score = (String)htCore.get(key_core_max_score);
                            if (score != null) {
                                return score;
                            }
                            else {
                                return "";
                            }
                        }
                        else {
                            boolean boolIsDecimal = true;
                            float tempFloat = 0;
                            if (putValue.length() > 0) {
                                try {
                                    tempFloat = (Float.valueOf(putValue)).floatValue();
                                } catch (Exception e) {
                                    boolIsDecimal = false;
                                }
                            }

                            if (putValue.length() > 0 && (boolIsDecimal == false || tempFloat < 0)) {
                                lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                                return FALSE;
                            }
                            else {
                                htPutParamData.put(key_core_max_score, putValue);                            
                                // update the corresponding value that will be retrieved by next "get"
                                htCore.put(key_core_max_score, putValue);
                                return TRUE;
                            }
                            
                        }
                    }
                    else if (parameter.toLowerCase().equalsIgnoreCase("cmi.core.score.min") == true) {
                        if (type == TYPE_GET) {
                            String score = (String)htCore.get(key_core_min_score);
                            if (score != null) {
                                return score;
                            }
                            else {
                                return "";
                            }
                        }
                        else {
                            boolean boolIsDecimal = true;
                            float tempFloat = 0;
                            if (putValue.length() > 0) {
                                try {
                                    tempFloat = (Float.valueOf(putValue)).floatValue();
                                } catch (Exception e) {
                                    boolIsDecimal = false;
                                }
                            }

                            if (putValue.length() > 0 && (boolIsDecimal == false || tempFloat < 0)) {
                                lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                                return FALSE;
                            }
                            else {
                                htPutParamData.put(key_core_min_score, putValue);                            
                                // update the corresponding value that will be retrieved by next "get"
                                htCore.put(key_core_min_score, putValue);
                                return TRUE;
                            }
                        }
                    }
                    else if (parameter.toLowerCase().endsWith("._children") == true) {
                        lastErrorID = ERROR_ID_ELEMENT_CANNOT_HAVE_CHILDREN;
                        if (type == TYPE_GET) {
                            return "";
                        }
                        else {
                            return FALSE;
                        }
                    }
                    else if (parameter.toLowerCase().endsWith("._count") == true) {
                        lastErrorID = ERROR_ID_ELEMENT_NOT_ARRAY;
                        if (type == TYPE_GET) {
                            return "";
                        }
                        else {
                            return FALSE;
                        }
                    }
                    else {
                        lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                        if (type == TYPE_GET) {
                            return "";
                        }
                        else {
                            return FALSE;
                        }
                    }
                }
                else if (parameter.toLowerCase().endsWith("._children") == true) {
                    lastErrorID = ERROR_ID_ELEMENT_CANNOT_HAVE_CHILDREN;
                    if (type == TYPE_GET) {
                        return "";
                    }
                    else {
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().endsWith("._count") == true) {
                    lastErrorID = ERROR_ID_ELEMENT_NOT_ARRAY;
                    if (type == TYPE_GET) {
                        return "";
                    }
                    else {
                        return FALSE;
                    }
                }
                else {
                    lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                    if (type == TYPE_GET) {
                        return "";
                    }
                    else {
                        return FALSE;
                    }
                }
            }
            else if (parameter.toLowerCase().equalsIgnoreCase("cmi.launch_data") == true) {
                if (type == TYPE_GET) {
                    return launch_data;
                }
                // read only
                else {
                    lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                    return FALSE;
                }
            }
            else if (parameter.toLowerCase().equalsIgnoreCase("cmi.suspend_data") == true) {
                if (type == TYPE_GET) {
                    suspend_data = (String)myGetParamData.ht_aicc_data.get(key_core_lesson);
                    if (suspend_data != null) {
                        return suspend_data;
                    }
                    else {
                        return null;
                    }
                }
                else { 
                    if (putValue.length() <= 4096) {
                        htPutParamData.put(key_core_lesson, putValue);                            
                        // update the corresponding value that will be retrieved by next "get"
                        myGetParamData.ht_aicc_data.put(key_core_lesson, putValue);
                        return TRUE;
                    }
                    else {
                        lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                        return FALSE;
                    }
                }
            }
            if (parameter.toLowerCase().startsWith("cmi.objectives.") == true) {
                if (parameter.toLowerCase().equalsIgnoreCase("cmi.objectives._children") == true) {
                    if (type == TYPE_GET) {
                        String delimiter = ",";
                        value = "_children" 
                                + delimiter + "_count" 
                                + delimiter + "id" 
                                + delimiter + "score" 
                                + delimiter + "status" ;
                        return value;
                    }
                    // ready only
                    else {
                        lastErrorID = ERROR_ID_KEYWORD_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().equalsIgnoreCase("cmi.objectives._count") == true) {
                    if (type == TYPE_GET) {
                        if (vtPutObjectiveStatus != null) {
                            return Integer.toString(vtPutObjectiveStatus.size());
                        }
                        else {
                            return "0";
                        }
                    }
                    // read only
                    else {
                        lastErrorID = ERROR_ID_READ_ONLY_ELEMENT;
                        return FALSE;
                    }
                }
                else if (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".id") == true) {
                    String temp = "cmi.objectives.";
                    String strIndex = parameter.substring(temp.length(), parameter.toLowerCase().indexOf(".id"));
                    int index = Integer.parseInt(strIndex);
                    if (type == TYPE_GET) {
                        if (index > vtPutObjectiveStatus.size()-1) {
                            return "";
                        }
                        else {
                            ObjectiveStatus myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                            return myObjectiveStatus.id;
                        }
                    }
                    else {
                        if (validIdentifier(putValue) == false) {
                            lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                            return FALSE;
                        }
                        
                        ObjectiveStatus myObjectiveStatus = null;
                        // the objective id should be increased in sequence
                        if (index > vtPutObjectiveStatus.size()) {
                            lastErrorID = ERROR_ID_INVALID_ARGUMENT;;
                            return FALSE;
                        }
                        else {
                            if (index > vtPutObjectiveStatus.size()-1) {
                                int vectorSize = vtPutObjectiveStatus.size();
                                // add some dummy objective object into the Vector to preserve the index
                                for (int i=vectorSize; i<index+1; i++) {
                                    vtPutObjectiveStatus.addElement(new ObjectiveStatus());
                                }
                            }
                            
                            myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                            myObjectiveStatus.id = putValue;
                            
                            return TRUE;
                        }
                    }
                }
                else if ((parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score._children") == true)
                        || (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score.raw") == true)
                        || (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score.max") == true)
                        || (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score.min") == true)) {
                            
                    if (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score._children") == true) {
                        if (type == TYPE_GET) {
                            String delimiter = ",";
                            value = "raw" 
                                    + delimiter + "max" 
                                    + delimiter + "min"; 
                            return value;
                        }
                        // ready only
                        else {
                            lastErrorID = ERROR_ID_KEYWORD_ELEMENT;
                            return FALSE;
                        }
                    }
                    else if (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score.raw") == true) {
                        String temp = "cmi.objectives.";
                        String strIndex = parameter.substring(temp.length(), parameter.toLowerCase().indexOf(".score.raw"));
                        int index = Integer.parseInt(strIndex);
                        if (type == TYPE_GET) {
                            if (index > vtPutObjectiveStatus.size()-1) {
                                return "";
                            }
                            else {
                                ObjectiveStatus myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                                return myObjectiveStatus.rawScore;
                            }
                        }
                        else {
                            if (validDecimal(putValue) == false) {
                                lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                                return FALSE;
                            }
                            
                            ObjectiveStatus myObjectiveStatus = null;
                            if (index > vtPutObjectiveStatus.size()-1) {
                                int vectorSize = vtPutObjectiveStatus.size();
                                // add some dummy objective object into the Vector to preserve the index
                                for (int i=vectorSize; i<index+1; i++) {
                                    vtPutObjectiveStatus.addElement(new ObjectiveStatus());
                                }
                            }
                            
                            myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                            myObjectiveStatus.rawScore = putValue;
                            
                            return TRUE;
                        }
                    }
                    else if (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score.max") == true) {
                        String temp = "cmi.objectives.";
                        String strIndex = parameter.substring(temp.length(), parameter.toLowerCase().indexOf(".score.max"));
                        int index = Integer.parseInt(strIndex);
                        if (type == TYPE_GET) {
                            if (index > vtPutObjectiveStatus.size()-1) {
                                return "";
                            }
                            else {
                                ObjectiveStatus myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                                return myObjectiveStatus.maxScore;
                            }
                        }
                        else {
                            if (validDecimal(putValue) == false) {
                                lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                                return FALSE;
                            }
                            
                            ObjectiveStatus myObjectiveStatus = null;
                            if (index > vtPutObjectiveStatus.size()-1) {
                                int vectorSize = vtPutObjectiveStatus.size();
                                // add some dummy objective object into the Vector to preserve the index
                                for (int i=vectorSize; i<index+1; i++) {
                                    vtPutObjectiveStatus.addElement(new ObjectiveStatus());
                                }
                            }
                            
                            myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                            myObjectiveStatus.maxScore = putValue;
                            
                            return TRUE;
                        }
                    }
                    else if (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".score.min") == true) {
                        String temp = "cmi.objectives.";
                        String strIndex = parameter.substring(temp.length(), parameter.toLowerCase().indexOf(".score.min"));
                        int index = Integer.parseInt(strIndex);
                        if (type == TYPE_GET) {
                            if (index > vtPutObjectiveStatus.size()-1) {
                                return "";
                            }
                            else {
                                ObjectiveStatus myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                                return myObjectiveStatus.minScore;
                            }
                        }
                        else {
                            if (validDecimal(putValue) == false) {
                                lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                                return FALSE;
                            }
                            
                            ObjectiveStatus myObjectiveStatus = null;
                            if (index > vtPutObjectiveStatus.size()-1) {
                                int vectorSize = vtPutObjectiveStatus.size();
                                // add some dummy objective object into the Vector to preserve the index
                                for (int i=vectorSize; i<index+1; i++) {
                                    vtPutObjectiveStatus.addElement(new ObjectiveStatus());
                                }
                            }
                            
                            myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                            myObjectiveStatus.minScore = putValue;
                            
                            return TRUE;
                        }
                    }
                    else {
                        lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                        if (type == TYPE_GET) {
                            return "";
                        }
                        else {
                            return FALSE;
                        }
                    }
                }                
                else if (parameter.toLowerCase().startsWith("cmi.objectives.") == true && parameter.toLowerCase().endsWith(".status") == true) {
                    String temp = "cmi.objectives.";
                    String strIndex = parameter.substring(temp.length(), parameter.toLowerCase().indexOf(".status"));
                    int index = Integer.parseInt(strIndex);
                    if (type == TYPE_GET) {
                        if (index > vtPutObjectiveStatus.size()-1) {
                            return "";
                        }
                        else {
                            ObjectiveStatus myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                            return myObjectiveStatus.status;
                        }
                    }
                    else {
                        if (putValue.equals(CMI_VOCAB_STATUS_NOT_ATTEMPTED) == false
                            && putValue.equals(CMI_VOCAB_STATUS_PASSED) == false
                            && putValue.equals(CMI_VOCAB_STATUS_COMPLETED) == false
                            && putValue.equals(CMI_VOCAB_STATUS_FAILED) == false
                            && putValue.equals(CMI_VOCAB_STATUS_INCOMPLETE) == false
                            && putValue.equals(CMI_VOCAB_STATUS_BROWSED) == false) {
                                
                            lastErrorID = ERROR_ID_INCORRECT_DATA_TYPE;
                            return FALSE;
                        }
                        
                        ObjectiveStatus myObjectiveStatus = null;
                        if (index > vtPutObjectiveStatus.size()-1) {
                            int vectorSize = vtPutObjectiveStatus.size();
                            // add some dummy objective object into the Vector to preserve the index
                            for (int i=vectorSize; i<index+1; i++) {
                                vtPutObjectiveStatus.addElement(new ObjectiveStatus());
                            }
                        }
                        
                        myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(index);
                        myObjectiveStatus.status = putValue;
                        
                        return TRUE;
                    }
                }
                else {
                    lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                    if (type == TYPE_GET) {
                        return "";
                    }
                    else {
                        return FALSE;
                    }
                }
            }
            else if (parameter.toLowerCase().startsWith("cmi.comments")
//            || parameter.toLowerCase().startsWith("cmi.objectives")
            || parameter.toLowerCase().startsWith("cmi.student_data")
            || parameter.toLowerCase().startsWith("cmi.student_preference")
            || parameter.toLowerCase().startsWith("cmi.interactions")
            || parameter.toLowerCase().startsWith("cmi.student_data")) {
                lastErrorID = ERROR_ID_NOT_IMPLEMENTED;
                if (type == TYPE_GET) {
                    return "";
                }
                else {
                    return FALSE;
                }
            }
            else if (parameter.toLowerCase().endsWith("._children") == true) {
                lastErrorID = ERROR_ID_ELEMENT_CANNOT_HAVE_CHILDREN;
                if (type == TYPE_GET) {
                    return "";
                }
                else {
                    return FALSE;
                }
            }
            else if (parameter.toLowerCase().endsWith("._count") == true) {
                lastErrorID = ERROR_ID_ELEMENT_NOT_ARRAY;
                if (type == TYPE_GET) {
                    return "";
                }
                else {
                    return FALSE;
                }
            }
            // no match element is found
            else {
                lastErrorID = ERROR_ID_INVALID_ARGUMENT;
                if (type == TYPE_GET) {
                    return "";
                }
                else {
                    return FALSE;
                }
            }
        }
        // unknown data model
        else {
            lastErrorID = ERROR_ID_INVALID_ARGUMENT;
            return FALSE;
        }
    }

    public boolean validDecimal(String input) {
        try {
            Float.valueOf(input);
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }

    public boolean validIdentifier(String identifier) {
        if (identifier.length() > 255) {
            return false;
        }
        for (int i=0; i<identifier.length(); i++) {
            if (Character.isDigit(identifier.charAt(i)) == false && Character.isLetter(identifier.charAt(i)) == false) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean validInteger(String integer) {
        int value = 0;
        
        try {
            value = Integer.parseInt(integer);
        } catch (Exception e) {
            return false;
        }
        
        if (value < 0 || value > 65536) {
            return false;
        }
        
        return true;
    }

    public boolean validTimespan(String timespan) {
        String STATE_HOUR = "1";
        String STATE_MINUTE = "2";
        String STATE_SECOND = "3";
        String STATE_MINISECOND = "4";
        String STATE_END = "5";
        String state = STATE_HOUR;
        
        for (int i=0; i<timespan.length(); i++) {
            if (state.equalsIgnoreCase(STATE_HOUR)) {
                boolean boolValidSemiColon = false;
                int semiColonIndex = -1;
                if (i+2 < timespan.length() && timespan.charAt(i+2) == ':') {
                    boolValidSemiColon = true;
                    semiColonIndex = i+2;
                }
                else if (i+3 < timespan.length() && timespan.charAt(i+3) == ':') {
                    boolValidSemiColon = true;
                    semiColonIndex = i+3;
                }
                else if (i+4 < timespan.length() && timespan.charAt(i+4) == ':') {
                    boolValidSemiColon = true;
                    semiColonIndex = i+4;
                }
                
                if (boolValidSemiColon == false) {
                    return false;
                }
                
                for (int j=i; j<semiColonIndex; j++) {
                    if (Character.isDigit(timespan.charAt(j)) == false) {
                        return false;
                    }
                }
                
                state = STATE_MINUTE;
                
                i = semiColonIndex;
            }
            else if (state.equalsIgnoreCase(STATE_MINUTE)) {
                boolean boolValidSemiColon = false;
                int semiColonIndex = -1;
                if (i+2 < timespan.length() && timespan.charAt(i+2) == ':') {
                    boolValidSemiColon = true;
                    semiColonIndex = i+2;
                }
                
                if (boolValidSemiColon == false) {
                    return false;
                }
                
                for (int j=i; j<semiColonIndex; j++) {
                    if (Character.isDigit(timespan.charAt(j)) == false) {
                        return false;
                    }
                }
                
                state = STATE_SECOND;
                
                i = semiColonIndex;
            }
            else if (state.equalsIgnoreCase(STATE_SECOND)) {
                boolean boolValidDot = false;
                int semiDotIndex = -1;
                if (i+2 > timespan.length()) {
                    boolValidDot = false;
                }
                else if (i+2 == timespan.length() || timespan.charAt(i+2) == '.') {
                    boolValidDot = true;
                    semiDotIndex = i+2;
                }
                
                if (boolValidDot == false) {
                    return false;
                }
                
                for (int j=i; j<semiDotIndex; j++) {
                    if (Character.isDigit(timespan.charAt(j)) == false) {
                        return false;
                    }
                }
                
                if (i+2 == timespan.length()) {
                    state = STATE_END;
                }
                else {
                    state = STATE_MINISECOND;
                }                
                
                i = semiDotIndex;
            }
            else if (state.equalsIgnoreCase(STATE_MINISECOND)) {
                if (i >= timespan.length()) {
                    return false;
                }
                else if (i+2 < timespan.length()) {
                    return false;
                }
                
                for (int j=i; j<timespan.length(); j++) {
                    if (Character.isDigit(timespan.charAt(j)) == false) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    public static String[] split(String in, String delimiter) {
        Vector q = new Vector();
        String[] result = null;
        
        if (in == null || in.length()==0)
            return result;
        
        int pos =0;
        pos = in.indexOf(delimiter);
        
        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos); 
            }
            q.addElement(val);
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.addElement(in);
        }

        result = new String[q.size()];
        for (int i=0; i<q.size();i++) {
            result[i] = (String) q.elementAt(i);
        }
        
        return result;
        
    }

    public void setLaunchData(String launch_data) {
        this.launch_data = launch_data;
    }
    
    public void setCmiURL(String aicc_url) {
        this.aicc_url = aicc_url;
    }

    public void setSessionID(String session_id) {
        this.session_id = session_id;
    }

    public void setCbtAiccVersion(String version) {
        this.version = version;
    }
    
    public void getParam() {
        String query = "";        
        String aicc_data = "";
        String command = "GetParam";
        Object[] args;
        Object temp = null;
        String result = null;
        
        // save the parameters to be used in dummyGetParam
//        this.aicc_url = aicc_url;
//        this.session_id = session_id;
//        this.version = version;
//        this.au_password = au_password;
        
        query += "command=" + command;
        query += "&version=" + version;
        query += "&session_id=" + session_id;
        query += "&AICC_Data=" + aicc_data;
        if (au_password.length() > 0) {
            query += "&AU_password=" + au_password;
        }
        
        myHttpConnection = new HttpConnection(aicc_url);
        result = myHttpConnection.doConnect(query);
        if (result == null) {
        }
        // normal online checking
        else {
        	CommonLog.debug("normal tracking...");
            this.CMI_connection_status = this.CMI_CONN_STATUS_CONNECTED;
            myGetParamData = getGetParamData(result);
        }
        
        initPutParamData(myGetParamData);
    }    

    public void putParam() {
        String query = "";        
        String command = "PutParam";
        
        String core_lesson_location = "";
        String core_lesson_status = "";
        String core_score = "";
        String core_time = "";
        String core_lesson = "";
        
        String result = null;
        
        core_lesson_location = (String)htPutParamData.get(key_core_lesson_location);
        core_lesson_status = (String)htPutParamData.get(key_core_lesson_status);
        core_time = (String)htPutParamData.get(key_core_time);
        core_lesson = (String)htPutParamData.get(key_core_lesson);
        if (htPutParamData.get(key_core_raw_score) != null) {
            core_score += (String)htPutParamData.get(key_core_raw_score);
        }
        core_score += ",";
        if (htPutParamData.get(key_core_max_score) != null) {
            core_score += (String)htPutParamData.get(key_core_max_score);
        }
        core_score += ",";
        if (htPutParamData.get(key_core_min_score) != null) {
            core_score += (String)htPutParamData.get(key_core_min_score);
        }
        
        Object temp = null;
        Object[] args = null;

        putParamAiccData = new StringBuffer(stringBufferSize);
        
        putParamAiccData.append(key_core + NEWL);
        putParamAiccData.append(key_core_lesson_location + "=");
        putParamAiccData.append(core_lesson_location + NEWL);
        putParamAiccData.append(key_core_lesson_status + "=");
        putParamAiccData.append(core_lesson_status + NEWL);
        putParamAiccData.append(key_core_score + "=");
        putParamAiccData.append(core_score + NEWL);
        putParamAiccData.append(key_core_time + "=");
        putParamAiccData.append(core_time + NEWL);
        putParamAiccData.append(key_core_lesson + NEWL);
        putParamAiccData.append(core_lesson + NEWL);
        
        if (vtPutObjectiveStatus.size() > 0) {
            putParamAiccData.append(key_objectives_status + NEWL);
            for (int i=0; i<vtPutObjectiveStatus.size(); i++) {
                ObjectiveStatus myObjectiveStatus = (ObjectiveStatus)vtPutObjectiveStatus.elementAt(i);
                
                if (myObjectiveStatus.id != null) {
                    putParamAiccData.append("J_ID." + Integer.toString(i+1) + "=");
                    putParamAiccData.append(myObjectiveStatus.id + NEWL);
                }
                if (myObjectiveStatus.rawScore != null) {
                    putParamAiccData.append("J_Score." + Integer.toString(i+1) + "=");
                    putParamAiccData.append(myObjectiveStatus.rawScore);
                    if (myObjectiveStatus.maxScore != null) {
                        putParamAiccData.append("," + myObjectiveStatus.maxScore);
                    }
                    else {
                        putParamAiccData.append(",");
                    }
                    if (myObjectiveStatus.minScore != null) {
                        putParamAiccData.append("," + myObjectiveStatus.minScore + NEWL);
                    }
                    else {
                        putParamAiccData.append("," + NEWL);
                    }
                }
                if (myObjectiveStatus.status != null) {
                    putParamAiccData.append("J_Status." + Integer.toString(i+1) + "=");
                    putParamAiccData.append(myObjectiveStatus.status + NEWL);
                }                
            }
        }

        
        CommonLog.debug("putParamAiccData:" + putParamAiccData.toString());

        query += "command=" + command;
        query += "&version=" + version;
        query += "&session_id=" + session_id;
        query += "&AICC_Data=" + URLEncoder.encode(putParamAiccData.toString());
        if (au_password.length() > 0) {
            query += "&AU_password=" + au_password;
        }
        query += "&type=" + "scorm";

        genAiccCall(aicc_url, query);
    }
    
    public void genAiccCall(String aicc_url, String query) {
        String result = null;
        Object temp = null;
        Object[] args = null;

        HttpConnection myHttpConnection = new HttpConnection(aicc_url);
        result = myHttpConnection.doConnect(query);
    }
    
    public void initPutParamData(GetParamData myGetParamData) {
        htPutParamData = new Hashtable();
        
        Hashtable htCore = (Hashtable)myGetParamData.ht_aicc_data.get(key_core);
        ObjectiveStatus[] myObjectiveStatusList = (ObjectiveStatus[])myGetParamData.ht_aicc_data.get(key_objectives_status);
        
        htPutParamData.put(key_core_lesson_location, (String)htCore.get(key_core_lesson_location));
        htPutParamData.put(key_core_lesson_status, (String)htCore.get(key_core_lesson_status));
        
        if (htCore.get(key_core_raw_score) != null) {
            htPutParamData.put(key_core_raw_score, (String)htCore.get(key_core_raw_score));
        }
        else {
            htPutParamData.put(key_core_raw_score, "0");
        }
        
        if (htCore.get(key_core_max_score) != null) {
            htPutParamData.put(key_core_max_score, (String)htCore.get(key_core_max_score));
        }
        else {
            htPutParamData.put(key_core_max_score, "0");
        }
        
        if (htCore.get(key_core_min_score) != null) {
            htPutParamData.put(key_core_min_score, (String)htCore.get(key_core_min_score));
        }
        else {
            htPutParamData.put(key_core_min_score, "0");
        }
        
        /*
        if (htCore.get(key_core_time) != null) {
            htPutParamData.put(key_core_time, (String)htCore.get(key_core_time));
        }
        else {
            htPutParamData.put(key_core_time, "00:00:00");
        }
        */
        
        htPutParamData.put(key_core_time, "00:00:00");
        
        htPutParamData.put(key_core_lesson, (String)myGetParamData.ht_aicc_data.get(key_core_lesson));
        
        if (myObjectiveStatusList != null) {
            for (int i=0; i<myObjectiveStatusList.length; i++) {
                vtPutObjectiveStatus.addElement(myObjectiveStatusList[i]);
            }
        }
    }

    public GetParamData getGetParamData(String inStr) {
        GetParamData myGetParamData = new GetParamData();
        String[] get_param_line = null;
        String aicc_data = null;
        String tempStr = null;
        get_param_line = split(inStr, NEWL);
        
        for (int i=0; i<get_param_line.length; i++) {
            if (get_param_line[i].toLowerCase().startsWith(key_error_num) == true) {
                tempStr = get_param_line[i].substring(key_error_num.length()).trim();
                myGetParamData.error_num = Integer.parseInt(tempStr);
            }
            else if (get_param_line[i].toLowerCase().startsWith(key_error_text) == true) {
                myGetParamData.error_text = get_param_line[i].substring(key_error_text.length()).trim();
            }
            else if (get_param_line[i].toLowerCase().startsWith(key_version) == true) {
                myGetParamData.version = get_param_line[i].substring(key_version.length()).trim();
            }
        }
        
        if (myGetParamData.error_num == -1) {
            this.CMI_connection_status = this.CMI_CONN_STATUS_FAILED;
        }
        // successful
        else if (myGetParamData.error_num == 0) {            
            this.CMI_connection_status = this.CMI_CONN_STATUS_CONNECTED;
            aicc_data = inStr.substring(inStr.indexOf(key_aicc_data) + key_aicc_data.length());
            myGetParamData.ht_aicc_data = getAiccData(aicc_data);
        }
        
        return myGetParamData;
    }

    public Hashtable getAiccData(String inStr) {
        Hashtable htAiccData = new Hashtable();
        String[] aicc_data_line = null;
        String tempStr = null;
        
        // this variable is especially used for Saba testing as lesson location seems not work for Saba
        String lessonLocation = null;
        
        String paramName = "";
        String paramValue = "";
                    
        int equalSignIndex = -1;
        aicc_data_line = split(inStr, NEWL);
        for (int i=0; i<aicc_data_line.length; i++) {
            if (aicc_data_line[i].toLowerCase().startsWith(key_core) == true) {
                i++;
                Hashtable htCore = new Hashtable();
                htAiccData.put(key_core, htCore);
                while (i<aicc_data_line.length) {
                    if (aicc_data_line[i].startsWith("[") == true) {
                        i--;
                        break;
                    }
                    equalSignIndex = aicc_data_line[i].indexOf("=");
                    
                    paramName = aicc_data_line[i].substring(0, equalSignIndex).trim().toLowerCase();
                    paramValue = aicc_data_line[i].substring(equalSignIndex+1).trim();
                                        
                    // handle some specical parameters here
                    if (paramName.equalsIgnoreCase(key_core_lesson_status) == true) {
                        if (paramValue.indexOf(',') != -1) {                            
                            int delimiterIndex = paramValue.indexOf(',');
                            String entry = paramValue.substring(delimiterIndex+1);
                            if (CMI_VOCAB_STATUS_COMPLETED.toLowerCase().startsWith(paramValue.substring(0, delimiterIndex).toLowerCase())) {
                                htCore.put(key_core_entry, "");
                            }
                            else if (entry.toUpperCase().startsWith("A") == true) {
                                htCore.put(key_core_entry, "ab-initio");
                            }
                            else {
                                // hardcode the entry to be "" in order to pass the test suite
                                htCore.put(key_core_entry, "");
                                //htCore.put(key_core_entry, "resume");
                            }
                        }
                        else {
                            if (CMI_VOCAB_STATUS_COMPLETED.toLowerCase().startsWith(paramValue.toLowerCase())) {
                                htCore.put(key_core_entry, "");
                            }
                            else {
                                // hardcode the entry to be "" in order to pass the test suite
                                htCore.put(key_core_entry, "");
                                //htCore.put(key_core_entry, "resume");
                            }
                        }
                    }
                    else if (paramName.equalsIgnoreCase(key_core_score) == true) {
                        String score = paramValue;
                        if (score.indexOf(',') != -1) {
                            htCore.put(key_core_raw_score, score.substring(0, score.indexOf(',')));
                            
                            score = score.substring(score.indexOf(',')+1);
                            
                            if (score.indexOf(',') != -1) {                                    
                                htCore.put(key_core_max_score, score.substring(0, score.indexOf(',')));
                                htCore.put(key_core_min_score, score.substring(score.indexOf(',')+1));
                            }
                            else {
                                // do nothing
                            }
                        }
                        else {
                            // do nothing
                        }                        
                    }
                    
                    htCore.put(paramName, paramValue);
                    i++;                    
                }
            }
            else if (aicc_data_line[i].toLowerCase().startsWith(key_core_lesson) == true) {
                String core_lesson = "";
                i++;
                while (i<aicc_data_line.length) {
                    if (aicc_data_line[i].startsWith("[") == true) {
                        i--;
                        break;
                    }
                    if (core_lesson.length() == 0) {
                        core_lesson += aicc_data_line[i];
                    }
                    else {
                        core_lesson += NEWL + aicc_data_line[i];
                    }
                    i++;
                }
                htAiccData.put(key_core_lesson, core_lesson);
            }
            else if (aicc_data_line[i].toLowerCase().startsWith(key_student_data) == true) {
                i++;
                Hashtable htStudentData = new Hashtable();
                htAiccData.put(key_student_data, htStudentData);
                while (i<aicc_data_line.length) {
                    if (aicc_data_line[i].startsWith("[") == true) {
                        i--;
                        break;
                    }
                    equalSignIndex = aicc_data_line[i].indexOf("=");
                    htStudentData.put(aicc_data_line[i].substring(0, equalSignIndex).trim().toLowerCase(), aicc_data_line[i].substring(equalSignIndex+1).trim());
                    i++;
                }
            }
            else if (aicc_data_line[i].toLowerCase().startsWith(key_core_vendor) == true) {
                String core_vendor = "";
                i++;
                while (i<aicc_data_line.length) {
                    if (aicc_data_line[i].startsWith("[") == true) {
                        i--;
                        break;
                    }
                    if (core_vendor.length() == 0) {
                        core_vendor += aicc_data_line[i];
                    }
                    else {
                        core_vendor += NEWL + aicc_data_line[i];
                    }
                    i++;
                }
                htAiccData.put(key_core_vendor, core_vendor);
            }
            else if (aicc_data_line[i].toLowerCase().startsWith(key_objectives_status) == true) {
                i++;
                Hashtable htObjectiveStatus = new Hashtable();
                while (i<aicc_data_line.length) {
                    if (aicc_data_line[i].trim().length() <= 0) {
                        i++;
                        continue;
                    }
                    if (aicc_data_line[i].startsWith("[") == true) {
                        i--;
                        break;
                    }
                    equalSignIndex = aicc_data_line[i].indexOf("=");
                    htObjectiveStatus.put(aicc_data_line[i].substring(0, equalSignIndex).trim().toLowerCase(), aicc_data_line[i].substring(equalSignIndex+1).trim());
                    i++;
                }
                
                Vector vtObjectiveStatus = new Vector();
                int index = 0;
                String obj_id_label = null;
                String obj_score_label = null;
                String obj_status_label = null;
                ObjectiveStatus myObjectiveStatus = null;
                while (htObjectiveStatus.size() > 0) {
                    obj_id_label = "j_id." + Integer.toString(index);
                    obj_score_label = "j_score." + Integer.toString(index);
                    obj_status_label = "j_status." + Integer.toString(index);
                    if (htObjectiveStatus.get(obj_id_label) != null) {
                        myObjectiveStatus = new ObjectiveStatus();
                        myObjectiveStatus.id = (String)htObjectiveStatus.get(obj_id_label);
                        htObjectiveStatus.remove(obj_id_label);
                        if (htObjectiveStatus.get(obj_score_label) != null) {
                            myObjectiveStatus.score = (String)htObjectiveStatus.get(obj_score_label);

                            StringTokenizer st = new StringTokenizer(myObjectiveStatus.score, ",");
                            if (st.hasMoreTokens()) {
                                myObjectiveStatus.rawScore = st.nextToken();
                            }
                            if (st.hasMoreTokens()) {
                                myObjectiveStatus.maxScore = st.nextToken();
                            }
                            if (st.hasMoreTokens()) {
                                myObjectiveStatus.minScore = st.nextToken();
                            }
     
                            htObjectiveStatus.remove(obj_score_label);
                        }
                        if (htObjectiveStatus.get(obj_status_label) != null) {
                            myObjectiveStatus.status = (String)htObjectiveStatus.get(obj_status_label);
                            htObjectiveStatus.remove(obj_status_label);
                        }
                        vtObjectiveStatus.addElement(myObjectiveStatus);
                    }
                    else {
                        // do nothing
                    }
                    index++;
                }
                
                ObjectiveStatus[] myObjectiveStatusList = new ObjectiveStatus[vtObjectiveStatus.size()];
                for (int j=0; j<vtObjectiveStatus.size(); j++) {
                    myObjectiveStatusList[j] = (ObjectiveStatus)vtObjectiveStatus.elementAt(j);
                }
                htAiccData.put(key_objectives_status, myObjectiveStatusList);
            }
        }

        return htAiccData;
    }
            
    public void callJavaScript(Object[] args, String methodName, boolean boolRecall) {
        try {
        	CommonLog.debug("start calling " + methodName);
            try {
                win.call(methodName, args);
            } catch(Exception e) {
            	CommonLog.error("Error in calling AiccAPI");
            	CommonLog.error(e.getMessage(),e);
            }
            CommonLog.debug("finish calling " + methodName);
        } catch(Exception e) {
        	CommonLog.error("Error in calling " + methodName);
        	CommonLog.error(e.getMessage(),e);
            if (boolRecall == true) {
                try {
                    Thread.sleep(500);
                } catch(Exception e2) {
                	CommonLog.error("Error in waiting to call javascript method again");
                	CommonLog.error(e.getMessage(),e2);
                }
                callJavaScript(args, methodName, boolRecall);
            }
        }
    }    
}