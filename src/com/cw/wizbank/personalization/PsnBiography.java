package com.cw.wizbank.personalization;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.DbPsnBiography;

public class PsnBiography {
    public static final String BIOGRPHY_OPTION_DELIMITER = "~";

    public void saveBiography(Connection con, long ent_id, String option_lst, String self_desc, String update_usr_id) throws SQLException{
        //record will be deleted when option is empty
        DbPsnBiography dbpbg = new DbPsnBiography();
        dbpbg.pbg_ent_id = ent_id;
        dbpbg.pbg_option = option_lst;
        dbpbg.pbg_self_desc = self_desc;

        if (DbPsnBiography.isExist(con, ent_id)){
            dbpbg.upd(con, update_usr_id);
        }else{
            dbpbg.ins(con, update_usr_id);
        }
    }
    
    public StringBuffer getBiographyAsXML(Connection con, long ent_id) throws SQLException{
        //record will be deleted when option is empty
        DbPsnBiography dbpbg = new DbPsnBiography();
        boolean bRecord = dbpbg.getBiographyByEntId(con, ent_id);
        
        StringBuffer xml = new StringBuffer();
        xml.append("<user_biography>").append(cwUtils.NEWL);
        if (bRecord){
            xml.append("<option_list>").append(cwUtils.NEWL);
            if (dbpbg.pbg_option != null && dbpbg.pbg_option.length() > 0){
                Vector vtOption = cwUtils.splitToVecString(dbpbg.pbg_option, BIOGRPHY_OPTION_DELIMITER);
                for (int i=0; i<vtOption.size(); i++){
                    xml.append("<option>").append((String)vtOption.elementAt(i)).append("</option>").append(cwUtils.NEWL);
                }        
            }
            xml.append("</option_list>").append(cwUtils.NEWL);
            xml.append("<self_desc>").append(cwUtils.esc4XML(cwUtils.escNull(dbpbg.pbg_self_desc))).append("</self_desc>");
        }
        xml.append("</user_biography>").append(cwUtils.NEWL);
        return xml;        
    }

    public static boolean showBiography(Connection con, long usr_ent_id) throws SQLException{
        boolean bShow;
        DbPsnBiography dbpbg = new DbPsnBiography();
        boolean bRecord = dbpbg.getBiographyByEntId(con, usr_ent_id);
        if (bRecord){
            if (dbpbg.pbg_option!= null && dbpbg.pbg_option.length() > 0 ){
                bShow = true;
            }else{
                bShow = false;
            }
        }else{
            bShow = false;
        }
        return bShow;
    }
}