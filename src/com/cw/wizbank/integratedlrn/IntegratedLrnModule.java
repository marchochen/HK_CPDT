package com.cw.wizbank.integratedlrn;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.db.DbIntegCourseCriteria;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class IntegratedLrnModule  extends ServletModule{
	public static final String MOD_NAME = "integrated_learning";

	IntegratedLrnParam modParam;
	public IntegratedLrnModule() {
	    super();
	    modParam = new IntegratedLrnParam();
	    param = modParam;
	}

	public void process() throws cwException, SQLException, IOException {
		try {
            if (prof == null || prof.usr_ent_id == 0) {
            	throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
            } else {
            	AcItem acItm = new AcItem(con);
            	if(!acItm.hasIntgMgtPrivilege(prof.usr_ent_id, prof.current_role)) {
            		throw new cwSysMessage("ACL002");
            	}
            	if (modParam.getCmd().equalsIgnoreCase("get_course_list")
            			||modParam.getCmd().equalsIgnoreCase("get_course_list_xml")){
            		if(!aeItem.isIntegratedItem(con, modParam.getItm_id())) {
            			throw new cwSysMessage("AEQM05");
            		}
            		String cosXml = IntegratedLrn.getCourseListXml(con, modParam);
            		cosXml += aeItem.genItemActionNavXML(con, modParam.getItm_id(), prof);
            		
            		resultXml = formatXML(cosXml, MOD_NAME);
            	} else if(modParam.getCmd().equalsIgnoreCase("set_criteria")) {
            		Timestamp t = DbIntegCourseCriteria.getUpdTimestamp(con, modParam.getItm_id());
            		if(modParam.getIcc_update_timestamp() != null 
            				&& (t == null || !modParam.getIcc_update_timestamp().equals(t))) {
            			throw new cwSysMessage("GEN006");
            		}
            		if(!aeItem.isIntegratedItem(con, modParam.getItm_id())) {
            			throw new cwSysMessage("AEQM05");
            		}
            		IntegratedLrn.setCriteria(con, modParam, prof);
            		sysMsg = getErrorMsg("GEN003", modParam.getUrl_success());
            	} else if(modParam.getCmd().equalsIgnoreCase("set_consisted_course_prev")
            			||modParam.getCmd().equalsIgnoreCase("set_consisted_course_prev_xml")) {
            		if(!aeItem.isIntegratedItem(con, modParam.getItm_id())) {
            			throw new cwSysMessage("AEQM05");
            		}
            		String conXml = IntegratedLrn.getConditionXml(con, modParam);
            		conXml += aeItem.genItemActionNavXML(con, modParam.getItm_id(), prof);
            		resultXml = formatXML(conXml, MOD_NAME);
            	} else if(modParam.getCmd().equalsIgnoreCase("set_consisted_course_exec")) {
            		IntegratedLrn.setCondition(con, modParam, prof);
            		String msg_code = "GEN001";
            		if(modParam.getIcd_id() > 0) {
            			msg_code = "GEN003";
            		}
            		sysMsg = getErrorMsg(msg_code, modParam.getUrl_success());
            	} else if(modParam.getCmd().equalsIgnoreCase("del_condition")){
            		IntegratedLrn.delCondition(con, modParam);
            		sysMsg = getErrorMsg("GEN002", modParam.getUrl_success());
            	} else {
                	throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
                }
            }
		} catch (cwSysMessage e) {
			try {
                con.rollback();
                msgBox(ServletModule.MSG_ERROR, e, modParam.getUrl_failure(),out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
        }
	}
}
