package com.cw.wizbank.content;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import com.oreilly.servlet.ServletUtils;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.accesscontrol.AcMessage;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;


public class EvaluationModule extends ServletModule {
    
    public static final String moduleName = "evaluation"; 
    ServletUtils sutils = new ServletUtils();
    
    public void process() throws SQLException, IOException, cwException {

        EvaluationReqParam urlp = null;

        urlp = new EvaluationReqParam(request, clientEnc, static_env.ENCODING);

        String title_code = "";
        if (request.getParameter("title_code") != null ){
        	title_code = request.getParameter("title_code").toString();
        };
        
        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            // if all command need authorized users
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);

            } else if (urlp.cmd.equalsIgnoreCase("GET_PUBLIC_EVAL_LST") || urlp.cmd.equalsIgnoreCase("GET_PUBLIC_EVAL_LST_XML")) {
                urlp.get_public_eval_lst();
                urlp.pagination();
                String navTcTree = null;
                cwTree tree = new cwTree();
 
                boolean hasMgtPrivilege = AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_EVN_MAIN);
                if (wizbini.cfgTcEnabled) {
					if (hasMgtPrivilege) {
						// if admin has no effect training center. throw a
						// system message
						
						if (!ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id) && AccessControlWZB.isRoleTcInd(prof.current_role)) {
							cwSysMessage e = new cwSysMessage("TC012");
							msgBox(MSG_ERROR, e, urlp.url_failure, out);
							return;
						}
						
						navTcTree = tree.genNavTrainingCenterTree(con, prof, false);
						// tcr_id < 0, get default training center
						// tcr_id = 0, all training center
						if(urlp.tcr_id < 0) {
		                	urlp.tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
		                }
					} else  {
						navTcTree = tree.genNavTrainingCenterTree(con, prof, false);
					}
				} 
                
                if(hasMgtPrivilege) {
                	urlp.show_effective_ind = false;
                } else {
                	urlp.show_effective_ind = true;
                }
                
                String result = Evaluation.getPublicQuestionEvalLstAsXML(con, prof, urlp.show_effective_ind, urlp.tcr_id, urlp.cwPage ,title_code);
                //the xml of training center tree
                if(navTcTree != null) {
                	result += navTcTree;
                }
                
                result = formatXML(result , moduleName);
                if(urlp.cmd.equalsIgnoreCase("GET_PUBLIC_EVAL_LST_XML"))
                    static_env.outputXML(out, result);
                if(urlp.cmd.equalsIgnoreCase("GET_PUBLIC_EVAL_LST"))
                    generalAsHtml(result, out , urlp.stylesheet);
                    
            } else if (urlp.cmd.equalsIgnoreCase("DL_EVAL_RPT") ||
                urlp.cmd.equalsIgnoreCase("DL_EVAL_RPT_XML")) {
                    
              try{                 
                
                urlp.report();

                Survey svy = new Survey();
                svy.mod_res_id = urlp.mod_id;
                
                StringBuffer result = new StringBuffer(formatXML(svy.getSurveyReport(con, prof, null, null, null,null, false, null) , "question_report"));

                if(urlp.cmd.equalsIgnoreCase("DL_EVAL_RPT_XML"))
                   static_env.outputXML(out, result.toString());
                if(urlp.cmd.equalsIgnoreCase("DL_EVAL_RPT")){
                    response.setHeader("Cache-Control", ""); 
                    response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition", "attachment; filename=evaluation_report" + urlp.mod_id + ".csv;"); 
                    cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }
              } catch(qdbException e) {
                    throw new cwException(e.toString());
              }

            } else {
                throw new cwException("Invalid Command || command retired");
            }
        }catch (cwSysMessage se) {
            try {
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
                CommonLog.error("SQL error: " + sqle.getMessage(),sqle);
             }
        }catch (qdbException e) {
            CommonLog.error(e.getMessage(),e);
        }
    }
}

