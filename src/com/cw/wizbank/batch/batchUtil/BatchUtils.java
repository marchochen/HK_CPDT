package com.cw.wizbank.batch.batchUtil;

import java.io.*;
import java.sql.*;

import javax.naming.NamingException;

import com.cw.wizbank.util.*;

import com.cw.wizbank.accesscontrol.acSite; 

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.config.WizbiniLoader;

public class BatchUtils{
  
    public static Connection openDB(String iniFile) 
        throws cwException, IOException, NamingException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        WizbiniLoader wizbini = new WizbiniLoader(iniFile);
        cwSQL sqlCon = new cwSQL();
        sqlCon.setParam(wizbini);
        Connection con = sqlCon.openDB(true);
        return con;
    }
    //depricated
    public static long getSiteId(String iniFile) 
        throws cwException, IOException
    {
        cwIniFile ini = new cwIniFile(iniFile);
        return Long.parseLong(ini.getValue("SITEID"));
    }
    //depricated
    
    //depricated ... DO NOT USE 
    public static loginProfile getProf(Connection con, long siteId, String iniFile) throws cwException, IOException{
        cwIniFile ini = new cwIniFile(iniFile);
        return getProf(con, siteId, ini); 
    }
        
    //depricated ... DO NOT USE 
    public static loginProfile getProf(Connection con, long siteId, cwIniFile ini) throws cwException, IOException{
        return getProf(con, siteId, ini, null);
    }

    public static loginProfile getProf(Connection con, long siteId, cwIniFile ini, WizbiniLoader wizbini) throws cwException, IOException{
        try{
            String wbId = ini.getValue("WBID");
            String wbPwd = ini.getValue("WBPWD");
        
            loginProfile prof = new loginProfile();
            acSite site = new acSite();
            site.ste_ent_id = siteId;
            String code = dbRegUser.login(con, prof, wbId, wbPwd, site, true, wizbini);
            if (code.equals(dbRegUser.CODE_LOGIN_SUCCESS)){
                // ok    
            }else{
                System.err.println("login failure");
                throw new cwException("login failure");    
            }
            return prof;
        }catch(qdbException e){
            throw new cwException(e.getMessage());    
        }
    }
}