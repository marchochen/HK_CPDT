package com.cw.wizbank.content;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;

public class Evaluation extends Survey{
    
    public static String getPublicEvalLstAsXML(Connection con, long root_ent_id, boolean checkStatus, String usr_id, boolean isStudyGroupMod,long sgp_id)
        throws cwException, qdbException, SQLException
    {
    	Vector vtMod = dbModule.getPublicModLst(con, root_ent_id, dbModule.MOD_TYPE_EVN, checkStatus, isStudyGroupMod, sgp_id, null);	
        Vector vtModId = modLst2ModId(vtMod);
        if (vtModId.size()==0){
            vtModId.addElement(new Long(0));
        }
        Hashtable htAttemptNbr = dbProgress.totalAttemptNum(con, vtModId);
        Hashtable usrAttemptNbr = dbProgress.usrAttemptNum(con, vtModId, usr_id);
        StringBuffer xml = new StringBuffer();
        xml.append("<evaluation_list>")
           .append(dbModule.getModLstAsXML(con, vtMod, htAttemptNbr,0,0))
           .append("<self_attempt>");
        
        Enumeration keys = usrAttemptNbr.keys();
        while (keys.hasMoreElements()) {
            Long modID = (Long) keys.nextElement();
            Long attemptNum = (Long) usrAttemptNbr.get(modID);
            xml.append("<module id=\"").append(modID.longValue())
               .append("\" attempt_nbr=\"").append(attemptNum.longValue())
               .append("\"/>");
        }
        
        xml.append("</self_attempt>")
           .append("</evaluation_list>");
        
        return xml.toString();        
    }
    
    public static String getPublicQuestionEvalLstAsXML(Connection con, loginProfile prof, boolean isCheckStatus, long tcrId, cwPagination page, String title_code)
        throws cwException, qdbException, SQLException{
    	dbModule module = new dbModule();
    	module.mod_type = dbModule.MOD_TYPE_EVN;
    	module.tcr_id = tcrId;
    	Vector evalModuleVec = null;
    	if(module.tcr_id <= 0) {
    		if(!AccessControlWZB.isRoleTcInd(prof.current_role)&& 
    				AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_EVN_MAIN)){
    			evalModuleVec = module.getAllEvalModuleOfTrainer(con, prof.root_ent_id, isCheckStatus, dbRegUser.getEntSysUserId(con), page, title_code);
    		}else{
    			evalModuleVec = module.getAllEvalModuleOfTrainer(con, prof.root_ent_id, isCheckStatus, prof.usr_ent_id, page, title_code);
    		}
    	} else if( module.tcr_id > 0) {
    		if(!AccessControlWZB.isRoleTcInd(prof.current_role)&& 
    				AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_EVN_MAIN)){
    			evalModuleVec = module.getEvalModuleOfTrainerByTcrId(con, prof.root_ent_id, isCheckStatus, dbRegUser.getEntSysUserId(con), page, title_code);
    		}else{
    			evalModuleVec = module.getEvalModuleOfTrainerByTcrId(con, prof.root_ent_id, isCheckStatus, prof.usr_ent_id, page, title_code);
    		}
    	} 
        
        Vector vtModId = modLst2ModId(evalModuleVec);
        if (vtModId.size()==0){
            vtModId.addElement(new Long(0));
        }
        Hashtable htAttemptNbr = dbProgress.totalAttemptNum(con, vtModId);
        Hashtable usrAttemptNbr = dbProgress.usrAttemptNum(con, vtModId, prof.usr_id);
        StringBuffer xml = new StringBuffer();
        xml.append("<evaluation_list>")
           .append(dbModule.getModLstAsXML(con, evalModuleVec, htAttemptNbr, true,0,0))
           .append(page.asXML())
           .append("<self_attempt>");
        
        Enumeration keys = usrAttemptNbr.keys();
        while (keys.hasMoreElements()) {
            Long modID = (Long) keys.nextElement();
            Long attemptNum = (Long) usrAttemptNbr.get(modID);
            xml.append("<module id=\"").append(modID.longValue())
               .append("\" attempt_nbr=\"").append(attemptNum.longValue())
               .append("\"/>");
        }
        
        xml.append("</self_attempt>")
           .append("</evaluation_list>");
        
        // current training center
		DbTrainingCenter dbTrainingCenter = DbTrainingCenter.getInstance(con, tcrId);
		xml.append("<cur_training_center id=\"").append(tcrId).append("\">");
		if(dbTrainingCenter == null) {
			xml.append("<title/>");
		} else {
			xml.append("<title>").append(cwUtils.esc4XML(dbTrainingCenter.getTcr_title())).append("</title>");
		}
		xml.append("</cur_training_center>");
		if("&".equals(title_code)) {
			title_code += "amp;";
		}
		xml.append("<search_title>"+title_code+"</search_title>");
        return xml.toString();        
    }

    public static String getPublicEvalNotAttemptLstAsXML(Connection con, loginProfile prof, boolean isCheckStatus, long tcrId,cwPagination page)
	    throws cwException, qdbException, SQLException
	{
    	dbModule module = new dbModule();
    	module.mod_type = dbModule.MOD_TYPE_EVN;
    	module.tcr_id = tcrId;

    	Vector evalModuleVec = null;
    	if(module.tcr_id <= 0) {
    		evalModuleVec = module.getAllEvalModuleOfTrainer(con, prof.root_ent_id, isCheckStatus, prof.usr_ent_id, page,"");
    	} else if(module.tcr_id > 0) {
    		evalModuleVec = module.getEvalModuleOfTrainerByTcrId(con, prof.root_ent_id, isCheckStatus, prof.usr_ent_id, page,"");
    	} 
	    
	    Vector vtModId = modLst2ModId(evalModuleVec);
	    if (vtModId.size()==0){
	        vtModId.addElement(new Long(0));
	    }
	    Hashtable htAttemptNbr = dbProgress.totalAttemptNum(con, vtModId);
	    Hashtable usrAttemptNbr = dbProgress.usrAttemptNum(con, vtModId, prof.usr_id);
	    
	    for (int i = 0; i < evalModuleVec.size(); i++) {
	    	 dbModule mod = (dbModule) evalModuleVec.elementAt(i);
	    	 if (usrAttemptNbr.containsKey(new Long(mod.mod_res_id))) {
	    		 evalModuleVec.remove(i);
	    		 i--;
	    	 }
	    }
	    
	    StringBuffer xml = new StringBuffer();
	    xml.append("<evaluation_list>")
	       .append(dbModule.getModLstAsXML(con, evalModuleVec, htAttemptNbr,0,0))
	       .append("</evaluation_list>");
	    
	    return xml.toString();        
	}
    
    public static Vector modLst2ModId(Vector vtMod){
        Vector vtModId = new Vector();
        if(vtMod != null) {
	        for (int i=0; i<vtMod.size(); i++){
	            dbModule dbmod = (dbModule) vtMod.elementAt(i);                        
	            vtModId.addElement(new Long(dbmod.mod_res_id));
	        }
        }
        return vtModId;
    }
    
    public class DbModuleBean {
		private long id;
		private String title;
		private Timestamp eff_end_datetime;
		private String type;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Timestamp getEff_end_datetime() {
			return eff_end_datetime;
		}

		public void setEff_end_datetime(Timestamp eff_end_datetime) {
			this.eff_end_datetime = eff_end_datetime;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
    
    /*
     * get public evaluation list of learner home page.
     */
    public Vector getModJsonVec(Connection con, long root_ent_id, boolean checkStatus, String usr_id,long cur_usr_ent_id, long tcrId, int start, int end, WizbiniLoader wizbini) throws SQLException {
    	Vector modVec = dbModule.getPublicEvaluationList(con, root_ent_id, cur_usr_ent_id, checkStatus, false, 0, true,  wizbini);
		Vector modJsonVec = new Vector();
		if (modVec != null && modVec.size() > 0) {
			modJsonVec = new Vector();
			for (int modIndex = start; modIndex < modVec.size(); modIndex++) {
				if ((end > 0 && modIndex < end) || end <= 0) {
					dbModule mod = (dbModule) modVec.get(modIndex);
					DbModuleBean moduleBean = new DbModuleBean();
					moduleBean.setId(mod.mod_res_id);
					moduleBean.setTitle(mod.res_title);
					moduleBean.setType(mod.mod_type);
					moduleBean.setEff_end_datetime(mod.mod_eff_end_datetime);
					modJsonVec.addElement(moduleBean);
				}
			}
		}
    	
    	return modJsonVec;
    }
        
}