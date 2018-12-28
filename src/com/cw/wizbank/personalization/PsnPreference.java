package com.cw.wizbank.personalization;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.config.organization.personalization.SkinType;
import com.cw.wizbank.db.DbPsnPreference;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class PsnPreference {
    
    public PsnPreference() {
                
    }  
    
    // return string array , array[0] = skin, array[1] = label
    public static String[] getPreferenceByEntId(Connection con, long ent_id, String rol_ext_id, Personalization orgPersonalization, String usr_id, String login_lan) throws SQLException{
        String[] preference = new String[3];
        DbPsnPreference dbpfr = new DbPsnPreference();
        dbpfr.pfr_ent_id = ent_id;
        boolean hasRecord = dbpfr.get(con);
        String lang;
        
        preference[0] = dbpfr.pfr_skin_id;
        if (preference[0]==null){
            String default_skin_by_role = getDefaultSkinByRole(orgPersonalization, rol_ext_id);
            if (default_skin_by_role!=null){
                preference[0] = default_skin_by_role;
            }else{
                preference[0] = orgPersonalization.getSkinList().getDefaultId(); 
            }
        }
        lang = dbpfr.pfr_lang;
        if (lang==null){
        	if(login_lan != null && usr_id != null) {
        		lang = login_lan;
        		dbpfr.pfr_lang = login_lan;
    			dbpfr.ins(con, usr_id);
        	} else {
        		lang = orgPersonalization.getSkinList().getDefaultLang(); 
        	}
        }
        preference[1] = cwUtils.langToLabel(lang);
        preference[2] = lang;
//        System.out.println("user: " + ent_id + " skin: " + preference[0] + " lang: " + preference[1]);
        return preference;        
    }
    
    private static String getDefaultSkinByRole(Personalization orgPersonalization, String rol_ext_id){
        List l_skin = orgPersonalization.getSkinList().getSkin();
        for (Iterator i = l_skin.iterator(); i.hasNext(); ){
            SkinType skin = (SkinType)i.next();
            if (skin.getRoleList() != null){
                List l_role = skin.getRoleList().getRole();
                for (Iterator j = l_role.iterator(); j.hasNext(); ){
                    String role = (String)j.next();
                    if (role.equals(rol_ext_id)){
                        return skin.getId();
                    }
                }
            }
        }
        return null;
    }

    public void savePreference(Connection con, long ent_id, String skin_id, String lang, String update_usr_id, String rol_ext_id, long major_tc_id) throws SQLException{
        DbPsnPreference dbpfr = new DbPsnPreference();
        dbpfr.pfr_ent_id = ent_id;
        boolean hasRecord = dbpfr.get(con);
        
        if (hasRecord){
            dbpfr.pfr_skin_id = skin_id;
            dbpfr.pfr_lang = lang;
            dbpfr.upd(con, update_usr_id);                        
        }else{
            dbpfr.pfr_skin_id = skin_id;
            dbpfr.pfr_lang = lang;
            dbpfr.ins(con, update_usr_id);                        
        }
        
        Timestamp curTime = cwSQL.getTime(con);
        if(major_tc_id > 0) {
        	ViewTrainingCenter.setMajorTcrId(con, ent_id, rol_ext_id, update_usr_id, major_tc_id, curTime);
        } else if(major_tc_id == 0){
        	ViewTrainingCenter.delMajorTcrId(con, ent_id, rol_ext_id, update_usr_id, curTime);
        }
    }
    
    public void savePreerenceLang(Connection con, long entId, String lang, String updateUsrId) throws SQLException {
		DbPsnPreference dbpfr = new DbPsnPreference();
		dbpfr.pfr_ent_id = entId;
		boolean isExist = dbpfr.get(con);

		if (isExist) {
			dbpfr.pfr_lang = lang;
			dbpfr.upd(con, updateUsrId);
		} else {
			dbpfr.pfr_lang = lang;
			dbpfr.ins(con, updateUsrId);
		}
	}
    
    public void delPreference(Connection con, long ent_id, String rol_ext_id, String upd_usr_id) throws SQLException{
        DbPsnPreference dbpfr = new DbPsnPreference();
        dbpfr.pfr_ent_id = ent_id;
        dbpfr.del(con);
        
        Timestamp curTime = cwSQL.getTime(con);
        ViewTrainingCenter.delMajorTcrId(con, ent_id, rol_ext_id, upd_usr_id, curTime);
    }
    
    
    public String getPreferenceListAsXML(Personalization orgPersonalization, WizbiniLoader wizbini, com.cw.wizbank.config.system.skinlist.SkinList sysSkinList) throws SQLException, cwException{
        String xml;
        List ltSysSkin = sysSkinList.getSkin();
        Hashtable htSkinName = new Hashtable();
        for (Iterator i = ltSysSkin.iterator(); i.hasNext(); ){
            com.cw.wizbank.config.system.skinlist.SkinType sysSkin = (com.cw.wizbank.config.system.skinlist.SkinType)i.next();
            htSkinName.put(sysSkin.getId(), sysSkin.getName());
        }                

        List ltOrgSkin = orgPersonalization.getSkinList().getSkin();
        for (Iterator i = ltOrgSkin.iterator(); i.hasNext(); ){
            com.cw.wizbank.config.organization.personalization.SkinType orgSkin = (com.cw.wizbank.config.organization.personalization.SkinType)i.next();
            if (orgSkin.getName()==null){
                orgSkin.setName((String)htSkinName.get(orgSkin.getId()));
            }
        }                
        
        StringWriter writer = new StringWriter(); 
        wizbini.marshal(orgPersonalization, writer);
        xml = writer.toString();
        xml = xml.substring(xml.indexOf("<", 2));

        return xml;
    }
    
    public String getMajorTcXML (Connection con, long usr_ent_id, String rol_ext_id, boolean tcEnable) throws SQLException {
    	StringBuffer xml = new StringBuffer();
        boolean isTa = AccessControlWZB.isRoleTcInd(rol_ext_id);
        if(tcEnable && isTa) {
        	long major_tc_id = ViewTrainingCenter.getMajorTcrId(con, usr_ent_id, rol_ext_id);
        	if(major_tc_id > 0) {
        		xml.append("<major_tc id=\"").append(major_tc_id).append("\"/>");
        	}
        	ViewTrainingCenter viewTCR = new ViewTrainingCenter();
        	List lTcr = viewTCR.getTrainingCenterByOfficer(con, usr_ent_id, rol_ext_id, false);
        	xml.append("<tc_lst>");
        	if(lTcr != null) {
        		for(int i=0; i<lTcr.size(); i++) {
        			DbTrainingCenter dbTc = (DbTrainingCenter)lTcr.get(i);
        			xml.append("<tc id=\"").append(dbTc.tcr_id).append("\">")
        			.append("<name>").append(cwUtils.esc4XML(dbTc.tcr_title)).append("</name>")
        			.append("</tc>");
        		}
        	}
        	xml.append("</tc_lst>");
        }
    	return xml.toString();
    }

}