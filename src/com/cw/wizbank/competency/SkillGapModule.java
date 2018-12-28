package com.cw.wizbank.competency;

import java.io.*;
import java.sql.*;

import javax.servlet.http.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.*;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.ae.aeItemCompetency;
import com.cwn.wizbank.utils.CommonLog;

public class SkillGapModule extends ServletModule {
    
    public static final String MODULENAME = "skillgap";
    
    public static final String SESS_SKILLGAP_XX    = "skillgap_xxx";
    
    public final static String SOURCE                   =   "source";
    public final static String TARGET                   =   "target";
    public final static String HASH_SKILL_ID_LIST       =   "SKILL_ID_LIST";

    public void process() throws SQLException, IOException, cwException {

        //String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);
        HttpSession sess = request.getSession(false);

        CompetencyReqParam urlp = null;

        urlp = new CompetencyReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            // if all command need authorized users
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            
            }else if (urlp.cmd.equalsIgnoreCase("GET_ENT_GAP") ||
                      urlp.cmd.equalsIgnoreCase("GET_ENT_GAP_XML")) {
               
                urlp.getSkillGap();
                CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                StringBuffer xmlBody = new StringBuffer();
                Timestamp curTime = cwSQL.getTime(con);
                xmlBody.append("<compare_items timestamp=\"").append(curTime).append("\">").append(cwUtils.NEWL);
                if( urlp.source_id > 0 )
                    xmlBody.append(gapAnalyzer.getEntityInfoAsXML(con, urlp.source_id, urlp.source_asm_id, urlp.source_date));
                if( urlp.target_id > 0 )
                    xmlBody.append(gapAnalyzer.getEntityInfoAsXML(con, urlp.target_id, urlp.target_asm_id, urlp.target_date));
                xmlBody.append("</compare_items>").append(cwUtils.NEWL);
                
                if( urlp.source_id > 0 && ( urlp.source_asm_id > 0 || urlp.source_date != null )
                &&  urlp.target_id > 0 && ( urlp.target_asm_id > 0 || urlp.target_date != null ) ) {
                    Timestamp source_resolved_timestamp;
                    if( urlp.source_asm_id > 0 )
                        source_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, urlp.source_asm_id);
                    else
                        source_resolved_timestamp = urlp.source_date;
                    
                    Timestamp target_resolved_timestamp;
                    if( urlp.target_asm_id > 0 )
                        target_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, urlp.target_asm_id);
                    else
                        target_resolved_timestamp = urlp.target_date;
                    
                    xmlBody.append(gapAnalyzer.cmpEntities(con, urlp.source_id, urlp.target_id, source_resolved_timestamp, target_resolved_timestamp, urlp.source_show_all, urlp.target_show_all, false));
                }
               
                String xml = formatXML(xmlBody.toString(), MODULENAME);
                
                if (urlp.cmd.equalsIgnoreCase("GET_ENT_GAP")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }

            }else if( urlp.cmd.equalsIgnoreCase("GET_ENT_PROF_GAP") ||
                      urlp.cmd.equalsIgnoreCase("GET_ENT_PROF_GAP_XML") ) {
                
                urlp.getSkillGap();
                CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                Timestamp curTime = cwSQL.getTime(con);
                StringBuffer xmlBody = new StringBuffer();
                xmlBody.append("<compare_items timestamp=\"").append(curTime).append("\">").append(cwUtils.NEWL);
                if( urlp.source_id > 0 )
                    xmlBody.append(gapAnalyzer.getEntityInfoAsXML(con, urlp.source_id, urlp.source_asm_id, urlp.source_date));
                xmlBody.append(gapAnalyzer.getCmpProfileInfoAsXML(con, urlp.target_id, prof.root_ent_id));
                xmlBody.append("</compare_items>").append(cwUtils.NEWL);
                
                if( urlp.source_id > 0 && ( urlp.source_asm_id > 0 || urlp.source_date != null )
                &&  urlp.target_id > 0 ) {
                    Timestamp source_resolved_timestamp;
                    if( urlp.source_asm_id > 0 )
                        source_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, urlp.source_asm_id);
                    else
                        source_resolved_timestamp = urlp.source_date;
                    xmlBody.append(gapAnalyzer.cmpEntitiesProfile(con, urlp.source_id, urlp.target_id, source_resolved_timestamp, urlp.source_show_all, urlp.target_show_all, urlp.show_course_recomm));
                }
                
                String xml = formatXML(xmlBody.toString(), MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("GET_ENT_PROF_GAP") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);                
                
            }else if( urlp.cmd.equalsIgnoreCase("get_prof_gap") ||
                      urlp.cmd.equalsIgnoreCase("get_prof_gap_xml") ) {
                                                
                    urlp.getSkillGap();
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    StringBuffer xmlBody = new StringBuffer();
                    xmlBody.append("<compare_items>").append(cwUtils.NEWL);
                    xmlBody.append(gapAnalyzer.getCmpProfileInfoAsXML(con, urlp.source_id, prof.root_ent_id));
                    xmlBody.append(gapAnalyzer.getCmpProfileInfoAsXML(con, urlp.target_id, prof.root_ent_id));
                    xmlBody.append("</compare_items>");
                    if( urlp.source_id > 0 && urlp.target_id > 0 )
                        xmlBody.append(gapAnalyzer.cmpProfile(con, urlp.source_id, urlp.target_id, urlp.source_show_all, urlp.target_show_all, false));
                    String xml = formatXML(xmlBody.toString(), MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("get_prof_gap") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);
            }else if( urlp.cmd.equalsIgnoreCase("get_skill_gap") ||
                      urlp.cmd.equalsIgnoreCase("get_skill_gap_xml") ) {
                    
                    urlp.getSkillGap();
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    String xml = gapAnalyzer.getSkillGap(con, prof.root_ent_id,
                                                         urlp.source_id, urlp.target_id, 
                                                         urlp.source_asm_id, urlp.target_asm_id,
                                                         urlp.source_type, urlp.target_type, 
                                                         urlp.source_date, urlp.target_date,
                                                         urlp.source_show_all, urlp.target_show_all,
                                                         urlp.show_course_recomm);
                                                  
                    xml = formatXML(xml, MODULENAME);                    
                    if( urlp.cmd.equalsIgnoreCase("get_skill_gap") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else    
                        static_env.outputXML(out, xml);
                        
            }else if( urlp.cmd.equalsIgnoreCase("view_skill_gap") ||
                      urlp.cmd.equalsIgnoreCase("view_skill_gap_xml") ) {
                    
                    urlp.viewSkillGap();
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    String xml = gapAnalyzer.getSkillGapAsXML(con, prof.root_ent_id,
                                                              urlp.source_id, urlp.target_id, 
                                                              urlp.source_asm_id, urlp.target_asm_id,
                                                              urlp.source_type, urlp.target_type, 
                                                              urlp.source_date, urlp.target_date,
                                                              urlp.source_show_all, urlp.target_show_all,
                                                              urlp.show_course_recomm, urlp.skl_id);
                                                  
                    xml = formatXML(xml, MODULENAME);                    
                    if( urlp.cmd.equalsIgnoreCase("view_skill_gap") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else    
                        static_env.outputXML(out, xml);
                        
            }else if( urlp.cmd.equalsIgnoreCase("get_item_skill") ||
                      urlp.cmd.equalsIgnoreCase("get_item_skill_xml") ) {
                
                    urlp.getItemSkill();
                    
                    aeItemCompetency itmSkill = new aeItemCompetency();
                    String xml = itmSkill.asXML(con, urlp.itm_id, "ASC", "skb_title");
                    
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("get_item_skill") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else    
                        static_env.outputXML(out, xml);                
                
            }else if( urlp.cmd.equalsIgnoreCase("view_usr_skill") || 
                      urlp.cmd.equalsIgnoreCase("view_usr_skill_xml") ) {

                    urlp.viewUsrSkill();
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    
                    String xml = new String();
                    Timestamp source_resolved_timestamp;
                    
                    if( urlp.source_id > 0 ) {                        
                        if( urlp.source_asm_id > 0 )
                            source_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, urlp.source_asm_id);
                        else
                            source_resolved_timestamp = urlp.source_date;                    
                    
                        xml = gapAnalyzer.viewUsrSkill(con, urlp.source_id, urlp.source_asm_id, source_resolved_timestamp);
                    }
                    
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("view_usr_skill") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else    
                        static_env.outputXML(out, xml);
            }else if( urlp.cmd.equalsIgnoreCase("view_ent_skill") || 
                      urlp.cmd.equalsIgnoreCase("view_ent_skill_xml") ) {

                urlp.viewEntSkill();
                CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                String xml = new String();
                Timestamp source_resolved_timestamp;
                    
                if( urlp.source_id > 0 ) {                        
                    if( urlp.source_asm_id > 0 )
                        source_resolved_timestamp = ViewCmAssessment.getResolvedDate(con, urlp.source_asm_id);
                    else
                        source_resolved_timestamp = urlp.source_date;                    
                    
                    xml = gapAnalyzer.getEntitySkillLevelAsXML(con, urlp.source_id, 
                                                              source_resolved_timestamp, 
                                                              urlp.source_asm_id, 
                                                              urlp.skl_id);
                }
                xml = formatXML(xml, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("view_ent_skill") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else    
                    static_env.outputXML(out, xml);
              }else if( urlp.cmd.equalsIgnoreCase("GET_RECOMMEND_ITM") || urlp.cmd.equalsIgnoreCase("GET_RECOMMEND_ITM_XML")) {                    
                    urlp.getRecommendItm();
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    String xml = gapAnalyzer.getRecommendItemAsXML(con, urlp.usr_ent_id, urlp.skl_id, prof.root_ent_id);
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("GET_RECOMMEND_ITM") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);
            }else {
                throw new cwSysMessage("GEN000");
            }

        }catch (cwSysMessage se) {
            try {
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
                 out.println("SQL error: " + sqle.getMessage());
                 CommonLog.error(sqle.getMessage(),sqle);
             }
        }
    }

}
