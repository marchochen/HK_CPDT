/*
 * for auto-enrol, override method(s) in parent class 
 */
package com.cw.wizbank.ae;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwXSL;

public class aeWorkFlowAutoEnrol extends aeWorkFlow {
	
	public aeWorkFlowAutoEnrol(String inXMLHeader) {
		super(inXMLHeader);
	}
	
    public String initStatus(String inRule, long status_id, long action_id) throws cwException{
        StringBuffer input  = new StringBuffer(1024);
        String output;
        input.append(xmlHeader).append("<status_init status_id=\"").append(status_id).append("\" action_id=\"").append(action_id).append("\">").append(inRule).append("</status_init>");
        output = cwXSL.processFromString(input.toString(), (String)xslHash.get("statusInitXSL2"));
        return output;
    }
    
}
