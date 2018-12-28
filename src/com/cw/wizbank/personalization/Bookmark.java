package com.cw.wizbank.personalization;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.DbBookmark;
import com.cw.wizbank.util.*;


public class Bookmark {
    
    public Bookmark() {
                
    }  
    /**
    *   all bookmark of that user
    */
    public String bookmarkAsXML(Connection con, loginProfile prof, DbBookmark dbBoo)
        throws qdbException, cwSysMessage
    {
        dbModule myDbModule = null;
        dbCourse myDbCourse = null;
        StringBuffer xmlBuffer = new StringBuffer();
        Timestamp curTime = dbUtils.getTime(con);
       
        dbBoo.boo_ent_id = (int)prof.usr_ent_id;
        Vector bookmarkVec = dbBoo.getAll(con);
        xmlBuffer.append(cwUtils.xmlHeader).append(cwUtils.NEWL);
        xmlBuffer.append("<bookmark_lst>").append(cwUtils.NEWL);
        xmlBuffer.append(prof.asXML()).append(cwUtils.NEWL);

        for (int i=0;i<bookmarkVec.size();i++) {
            DbBookmark boo = (DbBookmark) bookmarkVec.elementAt(i); 

            myDbModule = new dbModule();
            myDbModule.res_id = boo.boo_res_id;
            myDbModule.mod_res_id = boo.boo_res_id;
            try {
                myDbModule.get(con);
            } catch (qdbException e){
                myDbModule.res_id  = -1;
                myDbModule.mod_res_id  = -1;
            } catch (cwSysMessage e) {
                myDbModule.res_id  = -1;
                myDbModule.mod_res_id  = -1;
            }
            
            myDbCourse = new dbCourse();
            myDbCourse.res_id = dbModule.getCosId(con, myDbModule.mod_res_id);
            myDbCourse.cos_res_id = myDbCourse.res_id;
            try {
                myDbCourse.get(con);
            } catch (qdbException e){
                myDbCourse.res_id  = -1;
                myDbCourse.cos_res_id  = -1;
            } catch (cwSysMessage e) {
                myDbCourse.res_id  = -1;
                myDbCourse.cos_res_id  = -1;
            }

            xmlBuffer.append("<bookmark id=\"");
            xmlBuffer.append(boo.boo_id);
            xmlBuffer.append("\" moduleID=\"");
            xmlBuffer.append(boo.boo_res_id);
            xmlBuffer.append("\" courseID=\"");
            xmlBuffer.append(myDbCourse.cos_res_id);
            xmlBuffer.append("\" modType=\"");
            xmlBuffer.append(myDbModule.res_subtype);
            xmlBuffer.append("\" title=\"");
            if (boo.boo_title!=null){
                xmlBuffer.append(dbUtils.esc4XML(boo.boo_title));
            }    
            xmlBuffer.append("\" url=\"");
            if (boo.boo_url!=null){
                xmlBuffer.append(dbUtils.esc4XML(boo.boo_url));
            }    
            xmlBuffer.append("\" createDatetime=\"");
            xmlBuffer.append(boo.boo_create_timestamp);
            xmlBuffer.append("\" >");
            xmlBuffer.append(cwUtils.NEWL);
            
            xmlBuffer.append("<course_status exist=\"");
            if (myDbCourse.cos_res_id == -1) {
                xmlBuffer.append("false");
                xmlBuffer.append("\" status=\"");
                xmlBuffer.append("N/A");
                xmlBuffer.append("\" effective=\"");
                xmlBuffer.append("N/A");
            }
            else {
                xmlBuffer.append("true");
                xmlBuffer.append("\" status=\"");
                xmlBuffer.append(myDbCourse.res_status);
                xmlBuffer.append("\" effective=\"");
                if (myDbCourse.cos_eff_start_datetime == null && myDbCourse.cos_eff_end_datetime == null) {
                    xmlBuffer.append("true");
                }
                else if (myDbCourse.cos_eff_start_datetime == null) {
                    if (curTime.before(myDbCourse.cos_eff_end_datetime) == true) {
                        xmlBuffer.append("true");
                    }
                    else {
                        xmlBuffer.append("false");
                    }
                }
                else if (myDbCourse.cos_eff_end_datetime == null) {
                    if (curTime.after(myDbCourse.cos_eff_start_datetime) == true) {
                        xmlBuffer.append("true");
                    }
                    else {
                        xmlBuffer.append("false");
                    }
                }     
                else if (curTime.before(myDbCourse.cos_eff_end_datetime) == true && curTime.after(myDbCourse.cos_eff_start_datetime) == true) {
                    xmlBuffer.append("true");
                }
                else {
                    xmlBuffer.append("false");
                }
            }
            xmlBuffer.append("\" />");
            xmlBuffer.append(cwUtils.NEWL);
            
            xmlBuffer.append("<module_status exist=\"");
            if (myDbModule.mod_res_id == -1) {
                xmlBuffer.append("false");
                xmlBuffer.append("\" status=\"");
                xmlBuffer.append("N/A");
                xmlBuffer.append("\" effective=\"");
                xmlBuffer.append("N/A");
            }
            else {
                xmlBuffer.append("true");
                xmlBuffer.append("\" status=\"");
                xmlBuffer.append(myDbModule.res_status);
                xmlBuffer.append("\" effective=\"");
                if (myDbModule.mod_eff_start_datetime == null && myDbModule.mod_eff_end_datetime == null) {
                    xmlBuffer.append("true");
                }
                else if (myDbModule.mod_eff_start_datetime == null) {
                    if (curTime.before(myDbModule.mod_eff_end_datetime) == true) {
                        xmlBuffer.append("true");
                    }
                    else {
                        xmlBuffer.append("false");
                    }
                }
                else if (myDbModule.mod_eff_end_datetime == null) {
                    if (curTime.after(myDbModule.mod_eff_start_datetime) == true) {
                        xmlBuffer.append("true");
                    }
                    else {
                        xmlBuffer.append("false");
                    }
                }     
                else if (curTime.before(myDbModule.mod_eff_end_datetime) == true && curTime.after(myDbModule.mod_eff_start_datetime) == true) {
                    xmlBuffer.append("true");
                }
                else {
                    xmlBuffer.append("false");
                }
            }
            xmlBuffer.append("\" />");
            xmlBuffer.append(cwUtils.NEWL);
            
            xmlBuffer.append("</bookmark>");
            xmlBuffer.append(cwUtils.NEWL);
        }                  
        xmlBuffer.append("</bookmark_lst>");
        return xmlBuffer.toString();
    }    

    public void insBookmark(Connection con, loginProfile prof, DbBookmark dbBoo) throws qdbException{
        try{
            dbBoo.boo_ent_id = (int)prof.usr_ent_id;
            dbBoo.boo_create_timestamp = cwSQL.getTime(con);
            dbBoo.ins(con);
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }                  
    }   
    
    public void delMultiBookmark(Connection con, loginProfile prof, String[] booLst, DbBookmark dbBoo) throws qdbException{
        dbBoo.boo_ent_id = (int)prof.usr_ent_id;
        dbBoo.delMultiBookmark(con, booLst);
    }

}