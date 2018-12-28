package com.cw.wizbank.netg;

import java.sql.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import javax.servlet.http.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.importcos.ImportCos;

public class NETgTracking {
    static public String NETG_CDF_PARAM_NAME = "NAME=";
    static public String NETG_CDF_PARAM_DESC = "DESC=";
    static public String NETG_CDF_PARAM_DURA = "DURA=";
    static public String NETG_CDF_PARAM_EXEC = "EXEC=";
    static public String NETG_CDF_PARAM_COOKIE_NAME = "OPTINSTALLPARM=";
    static public String NETG_CDF_PARAM_COMP = "COMP=";

    static public String NETG_CDF_PARAM_HOUR = "HOURS";
    static public String NETG_CDF_PARAM_MIN = "MINUTES";
    static public String NETG_CDF_PARAM_SEC = "SECONDS";
    
    public dbModule myDbModule;
    public ViewNETgTracking myViewNETgTracking;

    public NETgTracking() {
        myViewNETgTracking = new ViewNETgTracking();
    		//$$ NETgTracking1.move(0,0);
    }

    public String getCookieName(Connection con, long mod_id) throws SQLException, qdbException, cwSysMessage {
        return myViewNETgTracking.getCookieName(con, mod_id);
    }

    public Cookie getCookie(Connection con, long cos_id, long mod_id, long ent_id, String domain, String home, long tkh_id, WizbiniLoader wizbini, loginProfile prof) throws SQLException, qdbException, cwSysMessage, IOException {
        String cookieValue = null;
        String prevCookieValue = null;
        String cookieName = null;
                
        Vector vtSkipElement = new Vector();

        vtSkipElement.addElement("Duration");
        cookieValue = URLEncoder.encode("Duration=0");
        cookieValue += URLEncoder.encode(";") + "%20";
        if (domain != null) {
            vtSkipElement.addElement("svmdom");
            cookieValue += URLEncoder.encode("svmdom=" + domain);
            cookieValue += URLEncoder.encode(";") + "%20";
        }
        /*
        vtSkipElement.addElement("wsvmtmp");
        cookieValue += URLEncoder.encode("wsvmtmp=0");
        cookieValue += URLEncoder.encode(";") + "%20";
        */
        
        vtSkipElement.addElement("home");
        cookieValue += URLEncoder.encode("home=" + home);
        cookieValue += URLEncoder.encode(";") + "%20";
                
        vtSkipElement.addElement("SessionGSF");
                
        if(cos_id > 0) {
        	cookieName = getCookieName(con, mod_id);
        } else {
        	String fileName = "";
        	BufferedReader in = null;
        	File infoFile = new File(wizbini.getFileUploadResDirAbs() + dbUtils.SLASH + mod_id + cwUtils.SLASH + "aicc_info.txt");
            in = new BufferedReader(new InputStreamReader(new FileInputStream(infoFile)));    
            fileName = in.readLine().trim();
            in.close();
            String cdfPath = wizbini.getFileUploadResDirAbs() + dbUtils.SLASH + mod_id + cwUtils.SLASH + fileName;
            ImportCos myImportCos = new ImportCos();
            Vector vtParentObj = new Vector();
            dbCourse myDbCourse = new dbCourse();
            dbModule myDbModule = new dbModule();
			myImportCos.importNETgCookie(con, prof, domain, myDbCourse, myDbModule, cdfPath, vtParentObj, true);
			cookieName = myImportCos.cookieName;
        }
        prevCookieValue = myViewNETgTracking.getLastCookie(con, cos_id, mod_id, ent_id, tkh_id);
                
        if (prevCookieValue == null || prevCookieValue.trim().length() <= 0) {
        }
        else {
            Hashtable prevCookieList = new Hashtable();
            String tempString = null;
                    
            // build the hashtable
            StringTokenizer st = new StringTokenizer(prevCookieValue, "; ");
            while (st.hasMoreTokens()) {
                tempString = st.nextToken();
                prevCookieList.put(tempString.substring(0, tempString.indexOf('=')), tempString.substring(tempString.indexOf('=')+1));
            }
                    
            // construct the cookie
            String key = null;
            Enumeration myEnumeration = prevCookieList.keys();
            while (myEnumeration.hasMoreElements()) {
                key = (String)myEnumeration.nextElement();
                boolean boolSkip = false;
                for (int i=0; i<vtSkipElement.size(); i++) {
                    if (key.equalsIgnoreCase((String)vtSkipElement.elementAt(i)) == true) {
                        boolSkip = true;
                        break;
                    }
                }
                if (boolSkip == true) {
                    continue;
                }
                        
                cookieValue += URLEncoder.encode(key + "=" + prevCookieList.get(key));
                cookieValue += URLEncoder.encode(";") + "%20";
            }

            // add the last delimiter
            //cookieValue += URLEncoder.encode(";") + "%20";
        }
                
        Cookie myCookie = new Cookie(cookieName, cookieValue);
        if (domain != null) {
            myCookie.setDomain(domain);
        }

        return myCookie;
    }
    
    public boolean cosCompleted(Connection con, long cos_id, long mod_id, long ent_id, long tkh_id) throws SQLException {
        boolean result = false;
        int attemptedPostAss = 0;
        Vector vtPostAssObjID = new Vector();
        
        vtPostAssObjID = myViewNETgTracking.getPostAssObjID(con, mod_id);
        attemptedPostAss = myViewNETgTracking.getAttemptedObjCount(con, ent_id, vtPostAssObjID, tkh_id);
                
        if (attemptedPostAss == vtPostAssObjID.size()) {
            result = true;
        }
        else {
            result = false;
        }
/*        
System.out.println("vtPostAssObjID.size():" + vtPostAssObjID.size());     
for (int i=0; i<vtPostAssObjID.size(); i++) {
    System.out.println(i + ":" + vtPostAssObjID.elementAt(i));
}
System.out.println("attemptedPostAss:" + attemptedPostAss);     
*/
                        
        return result;
    }


    public int processCookie(Connection con, long cos_id, long mod_id, long ent_id, String cookie_value, int pass_score, long tkh_id, StringBuffer status) throws SQLException, cwSysMessage, cwException, qdbException {
        int duration = 0;
        if (!myViewNETgTracking.setCookie(con, cos_id, mod_id, ent_id, cookie_value, tkh_id)) {
            throw new cwSysMessage("PGR009", "Unable to save cookie value to ModuleEvaluation: cos_id=" + cos_id + ";mod_id=" + mod_id + ";ent_id=" + ent_id + ";tkh_id=" + tkh_id);
        } else {
            String tempString = null;
            String sessionGSF = null;
//            int duration = 0;
            String cosStatus = null;
            int cosScore = -1;
            float lastScore = -1;
            StringTokenizer st = null;
            Hashtable prevCookieList = new Hashtable();
            st = new StringTokenizer(cookie_value, "; ");
            while (st.hasMoreTokens()) {
                tempString = st.nextToken();
                prevCookieList.put(tempString.substring(0, tempString.indexOf('=')), tempString.substring(tempString.indexOf('=')+1));
            }
                    
            // construct the cookie
            String key = null;
            Enumeration myEnumeration = prevCookieList.keys();
            while (myEnumeration.hasMoreElements()) {
                key = (String)myEnumeration.nextElement();
                boolean boolSkip = false;
                if (key.equalsIgnoreCase("SessionGSF") == true) {
                    sessionGSF = (String)prevCookieList.get(key);
                }
                else if (key.equalsIgnoreCase("Duration") == true) {
                    duration = Integer.parseInt((String)prevCookieList.get(key));
                }
            }
            
            /*
            if (duration > 0) {
                if (myViewNETgTracking.updDuration(con, cos_id, mod_id, ent_id, duration) == false) {
                    return false;
                }
            }
            */
            
            if (sessionGSF != null) {
                String elementID = null;
                String value = null;
                int separatorIndex = 0;
                boolean boolScore = true;
                
                st = new StringTokenizer(sessionGSF, ",");
                while (st.hasMoreTokens()) {
                    boolScore = true;
                    tempString = st.nextToken();
                    separatorIndex = tempString.indexOf(':');
                    elementID = tempString.substring(0, separatorIndex).trim();
                    value = tempString.substring(separatorIndex+1).trim();
                    
                    for (int i=0; i<value.length(); i++) {
                        if (Character.isDigit(value.charAt(i)) == false) {
                            boolScore = false;
                            break;
                        }
                    }
                    
                    // the overall course info.
                    if (elementID.equalsIgnoreCase("00000000")) {
                        if (boolScore) {
                            cosScore = Integer.parseInt(value);
                        }
                        else {
                            cosStatus = value;
                        }                        
                    }
                    // assessment info.
                    else {
                        int obj_id = myViewNETgTracking.getObjID(con, mod_id, elementID);
                        if (boolScore) {
                            if (myViewNETgTracking.insObjTracking(con, (int)ent_id, obj_id, Integer.parseInt(value), null, tkh_id) == false) {
                                throw new cwSysMessage("PGR009", "Unable to insert into Accomplishment (with score): ent_id="+ent_id+";obj_id="+obj_id+";value="+value+";tkh_id="+tkh_id);
                            }
                        }
                        else {
                            if (myViewNETgTracking.insObjTracking(con, (int)ent_id, obj_id, -1, value, tkh_id) == false) {
                                throw new cwSysMessage("PGR009", "Unable to insert into Accomplishment (without score): ent_id="+ent_id+";obj_id="+obj_id+";value=-1;tkh_id="+tkh_id);
                            }
                        }
                    }
                }
            }
            
            String oldStatus = ViewNETgTracking.getNETgStatus(con, cos_id, mod_id, ent_id, tkh_id);
            String lastStatus = dbModuleEvaluationHistory.everStatus(con,mod_id,tkh_id,dbAiccPath.STATUS_PASSED,dbAiccPath.STATUS_COMPLETE);
            if (cosCompleted(con, cos_id, mod_id, ent_id, tkh_id) == true) {
                // cos status will always be zero once the course has been launched
                cosStatus = "F";
              
                // set the status
                if (cosScore >= pass_score) {
                    cosStatus = "C";
                }else {
                    if(dbAiccPath.STATUS_COMPLETE.equalsIgnoreCase(oldStatus) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(oldStatus)){
                        //keep the oldStatus
                        cosStatus = null;
                    }else{
                        if(lastStatus != null){
                            cosStatus = lastStatus;
                        }
                    }
                }
         
                
                // set the score
                String everMaxScore = dbModuleEvaluationHistory.getEverMaxScore(con,mod_id,tkh_id);
                lastScore = ViewNETgTracking.getNETgScore(con, cos_id, mod_id, ent_id, tkh_id);
                if(everMaxScore != null) {
                    float max = Float.valueOf(everMaxScore).floatValue();
                    if (lastScore > 0) {
                        // don't overwrite the score unless the current is higher than the last score
                        if (cosScore <= lastScore) {
                            if(lastScore < max){
                                cosScore = new Float(everMaxScore).intValue();
                            }else{
                                cosScore = -1;
                            }
                         }else {
                            if(cosScore < max){
                                cosScore = new Float(everMaxScore).intValue();
                            }
                        }
                    }else {
                        if(lastScore < max){
                            cosScore = new Float(everMaxScore).intValue();
                        }
                    }
                }
            }else {
                // set the status to "I"
                cosStatus = "I";
                
                // set the score to 0
                cosScore = 0;
            }            
                            
            // set cosScore = -1 if you don't want to update the score field
            // set cosStatus = null if you don't want to update the status field
            Timestamp cur_time = cwSQL.getTime(con);
            if (myViewNETgTracking.updTracking(con, cos_id, mod_id, ent_id, cosScore, cosStatus, duration, tkh_id, cur_time) == false) {
                throw new cwSysMessage("PGR009", "Unable to update ModuleEvluation: cos_id="+cos_id+";mod_id="+mod_id+";ent_id="+ent_id+";cosScore="+cosScore+";cosStatus="+cosStatus+";duration="+duration+";tkh_id="+tkh_id+";cur_time="+cur_time);
            }

            // insert module evaluation history
            dbModuleEvaluationHistory dbModEvalHist = new dbModuleEvaluationHistory();
            dbModEvalHist.mvh_ent_id = ent_id;
            dbModEvalHist.mvh_mod_id = mod_id;
            dbModEvalHist.mvh_last_acc_datetime = cur_time;
            dbModEvalHist.mvh_tkh_id = tkh_id;
            dbModEvalHist.mvh_ele_loc = null;
            dbModEvalHist.mvh_total_time = duration;
            dbModEvalHist.mvh_total_attempt = 1;
            dbModEvalHist.mvh_status = cosStatus;
            if (cosScore > -1) {
                dbModEvalHist.mvh_score = Integer.toString(cosScore);
            }
            else {
                dbModEvalHist.mvh_score = Float.toString(lastScore);
            }
            dbRegUser myDbRegUser = new dbRegUser();
            myDbRegUser.usr_ent_id = ent_id;
            dbModEvalHist.mvh_create_usr_id = myDbRegUser.getUserId(con);
            dbModEvalHist.mvh_create_timestamp = cur_time;
            dbModEvalHist.ins(con);
            
            
            // update the cos evaluation
            if (cos_id > 0){
                dbCourseEvaluation dbcov = new dbCourseEvaluation();
                dbcov.cov_cos_id = cos_id;
                dbcov.cov_ent_id = ent_id;
                dbcov.cov_tkh_id = tkh_id;
                dbcov.save(con);
            }
            status.append(cosStatus);

        }
        return duration;
    }
	//{{DECLARE_CONTROLS
	//}}
}