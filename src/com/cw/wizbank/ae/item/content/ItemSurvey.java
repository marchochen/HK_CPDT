package com.cw.wizbank.ae.item.content;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.view.*;

public class ItemSurvey{
    /*
    public static String ItmRateAsXML(Connection con, long itm_id, long mod_id) throws qdbException, cwSysMessage, cwException {
        try{
            StringBuffer xmlBuf = new StringBuffer();
            aeItem cos = new aeItem();
            
            cos.itm_id = itm_id;
            cos.getItem(con);
            
            String cosXML = cos.contentAsXML(con);
            String targetXML = getCosRatingAsXML(con, itm_id, ViewMoteRpt.TARGET_OVERALL_RATING);
            String actualXML = getCosRatingAsXML(con, itm_id, ViewMoteRpt.ACTUAL_OVERALL_RATING);

            xmlBuf.append(cosXML).append(cwUtils.NEWL);
            xmlBuf.append("<run_lst>").append(cwUtils.NEWL);
            xmlBuf.append(getRunLstAsXML(con, itm_id)).append(cwUtils.NEWL);
            xmlBuf.append("</run_lst>").append(cwUtils.NEWL);
            xmlBuf.append(targetXML).append(cwUtils.NEWL);
            xmlBuf.append(actualXML).append(cwUtils.NEWL);

            xmlBuf.append(getItmModLstAsXML(con, itm_id, mod_id, dbModule.MOD_TYPE_EVN));

            return cwUtils.escNull(xmlBuf.toString());   
        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
        }                  
    }
    */
    /*
    private static String getCosRatingAsXML(Connection con, long itm_id, String rate_type) throws cwException{
        
        Vector vtRate = ViewMoteRpt.getCosRating(con, itm_id, rate_type);        
        DbItemRating myItemRate = null;

        StringBuffer xmlBuf = new StringBuffer();        

        xmlBuf.append("<").append(rate_type).append(">");
        for (int i=0; i<vtRate.size(); i++){
            myItemRate = (DbItemRating)vtRate.elementAt(i);
            xmlBuf.append("<item itm_id=\"");
            xmlBuf.append(myItemRate.irt_itm_id);
            xmlBuf.append("\" rate=\"");
            xmlBuf.append(myItemRate.irt_rate);
            xmlBuf.append("\" />");
        }
        xmlBuf.append("</").append(rate_type).append(">");

        return xmlBuf.toString();
    }
    */
    /*
    public static String getRunLstAsXML(Connection con, long itm_id) throws cwException{
        Vector runLst = ViewSurvey.getRunLstEvn(con, itm_id);        
        ViewMoteRpt.Item myItem = null;
        StringBuffer xmlBuf = new StringBuffer();
        
        for (int i=0; i<runLst.size(); i++){
            myItem = (ViewMoteRpt.Item) runLst.elementAt(i);            
            xmlBuf.append("<item itm_id=\"");
            xmlBuf.append(myItem.itm_id);
            xmlBuf.append("\" itm_title=\"");
            xmlBuf.append(myItem.itm_title);
            xmlBuf.append("\" evn_count=\"");
            xmlBuf.append(myItem.count);
            xmlBuf.append("\" />");
        }
                
        return xmlBuf.toString();
    }
    */

    // 
    public static String getCleanItmModLstAsXML(Connection con, long itm_id, long mod_id, String mod_type) throws cwException, SQLException{
        Vector modLst  = null;
        Vector usrId = new Vector();
        modLst = ViewSurvey.getItmModLst(con, itm_id, mod_id, mod_type, usrId, null);    
        
        if (modLst.size() == 0){
            return "";    
        }

        Vector resLst = new Vector();
        dbModule myMod = null;

        for (int i=0; i<modLst.size(); i++){
            myMod = (dbModule) modLst.elementAt(i);            
            resLst.addElement(new Long(myMod.res_id));
        }

        StringBuffer xmlBuf = new StringBuffer();
        String tmp_eff_end = null;
        for (int i=0; i<modLst.size(); i++){
            myMod = (dbModule) modLst.elementAt(i);            
            xmlBuf.append("<survey id=\"");
            xmlBuf.append(myMod.res_id);
            xmlBuf.append("\" title=\"");
            xmlBuf.append(myMod.res_title);
            xmlBuf.append("\" eff_start_datetime=\"");
            xmlBuf.append(myMod.mod_eff_start_datetime);
            xmlBuf.append("\" eff_end_datetime=\"");
            
            //check if the end_datetime need to be converted to "UNLIMITED"
            if(myMod.mod_eff_end_datetime != null){
                if(dbUtils.isMaxTimestamp(myMod.mod_eff_end_datetime) == true){
                    tmp_eff_end = cwUtils.UNLIMITED; //convert to String to "UNLIMITED"
                }
                else{
                    tmp_eff_end = myMod.mod_eff_end_datetime.toString();
                }
            }
            xmlBuf.append(tmp_eff_end);
            xmlBuf.append("</survey>");
        }
                
        return xmlBuf.toString();
    }

}