package com.cw.wizbank.content;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.oreilly.servlet.ServletUtils;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.cwException;
import  com.cw.wizbank.qdb.qdbException;

import java.util.Vector;

import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.accesscontrol.AccessControlReqParam;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;


public class CosEvaluationModule extends ServletModule {
    
    public static final String moduleName = "cosevaluation"; 
    ServletUtils sutils = new ServletUtils();

    public void process() throws SQLException, IOException, cwException {

        CosEvaluationReqParam urlp = null;
        urlp = new CosEvaluationReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        urlp.pagination();
        
        PrintWriter out = response.getWriter();

        try {
            // if all command need authorized users
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);

            } else if (urlp.cmd.equalsIgnoreCase("GET_COS_EVAL_LST") || urlp.cmd.equalsIgnoreCase("GET_COS_EVAL_LST_XML")) {
                
            	urlp.mod();
            	StringBuffer result = new StringBuffer();
            	Vector modTcrIds = new Vector();
				if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_COS_EVN_MAIN) 
						&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
					if (!ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id)) {
						msgBox(MSG_ERROR, new cwSysMessage("TC018"), urlp.url_success, out);    
						return;
					}
				}
    			if (urlp.mod_tcr_id <= 0) {
                	long tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
                	modTcrIds = DbTrainingCenter.getChildTc(con, tcr_id);
                	modTcrIds.add(tcr_id);
                } else {
                	modTcrIds.add(urlp.mod_tcr_id);
                }
                // div tree
    			cwTree tree = new cwTree();
    			String navTcTree;
    			try {
    				navTcTree = tree.genNavTrainingCenterTree(con, prof, false);
    				if(navTcTree != null){
    					result.append(navTcTree);
    				}
    			} catch (cwSysMessage e) {
    				CommonLog.error(e.getMessage(),e);
    			}
    			DbTrainingCenter dbTrainingCenter = DbTrainingCenter.getInstance(con, urlp.mod_tcr_id);
    			result.append("<cur_training_center id=\"").append(urlp.mod_tcr_id).append("\">");
    			if(dbTrainingCenter == null) {
    				result.append("<title/>");
    			} else {
    				result.append("<title>").append(cwUtils.esc4XML(dbTrainingCenter.getTcr_title())).append("</title>");
    			}
    			result.append("</cur_training_center>");
                result.append(CosEvaluation.getCosEvalLstAsXML(con, prof.root_ent_id, prof.usr_id,false,0, modTcrIds,urlp.cwPage.curPage,urlp.cwPage.pageSize));
                String resultXml = formatXML(result.toString() , moduleName);
                if(urlp.cmd.equalsIgnoreCase("GET_COS_EVAL_LST_XML"))
                    static_env.outputXML(out, resultXml);
                if(urlp.cmd.equalsIgnoreCase("GET_COS_EVAL_LST"))
                    generalAsHtml(resultXml, out , urlp.stylesheet);
                    
            } else {
                throw new cwException("Invalid Command || command retired");
            }
//        }catch (cwSysMessage se) {
//            try {
//                 con.rollback();
//                 msgBox(ServletModule.MSG_STATUS, con, se, prof, urlp.url_failure, out);
//             } catch (SQLException sqle) {
//                out.println("SQL error: " + sqle.getMessage());
//             }
        }catch (qdbException e) {
            CommonLog.error(e.getMessage(),e);
        }
    }
}

