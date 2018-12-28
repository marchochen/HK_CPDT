package com.cw.wizbank.report;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.io.StringReader;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cw.wizbank.util.cwXSL;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.DbUserClassification;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.DbObjectView;
import com.cw.wizbank.db.view.ViewCfCertificate;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.content.Survey;
import com.cwn.wizbank.utils.CommonLog;

import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.servlet.http.*;

public class ReportTemplate {
    
    
    public final static String USR_CURRENT_ = "USR_CURRENT_";
    public final static String OBJ_VIEW_CLOSE_TAG = "</object_view>";
    
	public static final int PAGE_SIZE_VIEW_PHOTO = 5;
	public static final int PAGE_SIZE_VIEW_RECORD = 10;
    
    public class XMLparser extends HandlerBase {
        Hashtable spec_pairs;
        
        XMLparser() {
            spec_pairs = new Hashtable();
        }
        
        public void startElement(String name, AttributeList inAttribList) throws SAXException {
            if (name.equals("data")) {
                String spec_name = inAttribList.getValue("name");
                String spec_value = inAttribList.getValue("value");

                Vector vec = (Vector)spec_pairs.get(spec_name);
                
                if (vec == null) {
                    vec = new Vector();
                }

                vec.addElement(spec_value);
                spec_pairs.put(spec_name, vec);
            }
        }
    }    
    
    static final boolean DEBUG = false;
/*
    public String getReportrrr(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id, String xml_prefix, String xml_suffix, String rsp_xml, String rte_type) throws SQLException, cwException, cwSysMessage {
        return getReportrrr(con, request, prof, rsp_id, rte_id, xml_prefix, xml_suffix, rsp_xml, rte_type, null);
    }
*/
    public String getReport(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id, String xml_prefix, String xml_suffix, String rsp_xml, String rte_type, String rsp_title) throws SQLException, cwException, cwSysMessage {

        StringBuffer result = new StringBuffer();
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        if (rsp_id <= 0 && rte_id <= 0) {
            data = spec.getReportTemplate(con, prof.root_ent_id, rte_type);
            data.rsp_xml = rsp_xml;
            data.rsp_title = rsp_title;
        } else if (rsp_id > 0) {
            data = spec.getTemplateAndSpec(con, rsp_id);
        } else {
            data = spec.getReportTemplate(con, rte_id);
            data.rsp_xml = rsp_xml;
            data.rsp_title = rsp_title;
        }
        result.append("<report_body>").append(cwUtils.NEWL);

        if (xml_prefix != null) {
            result.append(xml_prefix);
        }

        result.append("<meta>");
        CommonLog.debug("Report = " + rte_type);
        if (rte_type.equals(Report.COURSE)) {
            result.append(CourseReport.getMetaData(con, prof, null));
        } else if( rte_type.equals(Report.TARGET_LEARNER) ) {
            result.append(TargetLearnerReport.getMetaData(con, prof));
        } else if (rte_type.equals(Report.LEARNER) || rte_type.indexOf(Report.LEARNER) >= 0) {
            result.append(LearnerReport.getMetaData(con, prof,  null));
        } else if(rte_type.equals(Report.LEARNING_ACTIVITY_LRN)){
        	result.append(LearnerReport.getMetaData(con, prof,  null)); 
        } else if(rte_type.equals(Report.LEARNING_ACTIVITY_COS)){
        	result.append(LearnerReport.getMetaData(con, prof,  null)); 
        } else if(rte_type.equals(Report.LEARNING_ACTIVITY_BY_COS)){
        	result.append(LearnerReport.getMetaData(con, prof,  null)); 
        } else if(rte_type.equals(Report.MODULE)){
            result.append(LearnerReport.getMetaData(con, prof,  null));
        } else if (rte_type.startsWith(Report.SURVEY_PREFIX) ) {
            SurveyReport surveyreport = new SurveyReport(rte_type);
            result.append(surveyreport.getMetaData(con, prof,false,0, null));
        } else if (rte_type.startsWith(Report.MODULE_PREFIX)) {            
            result.append(ModuleReport.getMetaData(con, prof));            
        } else if (rte_type.startsWith(Report.GLOBAL_ENROLLMENT)) {            
            result.append(GlobalEnrollmentReport.getMetaData(con, prof));            
        } else if (rte_type.equals(Report.TRAIN_FEE_STAT)) {
        	result.append(aeItem.getItemTypeTitleByTrainType(con, prof.root_ent_id, new String[]{"COS", "EXAM"}));
        }
        
        
        result.append("</meta>");
/*        if (data.rte_meta_data_url != null) {
            result.append("<meta>").append(cwUtils.NEWL);
            StringBuffer url_buf = new StringBuffer();
            url_buf.append("http://").append(request.getServerName()).append(":").append(request.getServerPort()).append("/");            
            String[] args = cwUtils.splitToString(data.rte_meta_data_url, "?");            
            String value = aeUtils.urlRedirect(url_buf.toString() + args[0], args[1], request);

            if (value != null) {
                result.append(value).append("</meta>").append(cwUtils.NEWL);
            }
        }*/
//System.out.println("4");                
        result.append("<template id=\"").append(data.rte_id).append("\" type=\"").append(data.rte_type).append("\">").append(cwUtils.NEWL);               
        result.append(aeUtils.escNull(data.rte_title_xml)).append(cwUtils.NEWL);
        result.append("<xsl_list>").append(cwUtils.NEWL);
        result.append("<xsl type=\"get\">").append(cwUtils.esc4XML(aeUtils.escNull(data.rte_get_xsl))).append("</xsl>").append(cwUtils.NEWL);
        result.append("<xsl type=\"execute\">").append(cwUtils.esc4XML(aeUtils.escNull(data.rte_exe_xsl))).append("</xsl>").append(cwUtils.NEWL);
        result.append("<xsl type=\"download\">").append(cwUtils.esc4XML(aeUtils.escNull(data.rte_dl_xsl))).append("</xsl>").append(cwUtils.NEWL);
        result.append("</xsl_list>").append(cwUtils.NEWL);
        result.append("</template>").append(cwUtils.NEWL);
        result.append("<spec template_id=\"").append(data.rsp_rte_id).append("\" spec_id=\"").append(data.rsp_id).append("\" ent_id=\"").append(data.rsp_ent_id).append("\">").append(cwUtils.NEWL);
        result.append("<title>").append(cwUtils.esc4XML(aeUtils.escNull(data.rsp_title))).append("</title>").append(cwUtils.NEWL);
        result.append(aeUtils.escNull(data.rsp_xml)).append(cwUtils.NEWL);
        result.append("</spec>").append(cwUtils.NEWL);
//println("5");
//System.out.println("<><><><><><><><><> rsp_xml = " + data.rsp_xml);
//        if (rsp_id > 0) {
        if (data.rsp_xml != null) {
            result.append(getPresentaton(con, data.rsp_xml));
        }
        
        //DENNIS: get display options 
        result.append(getDisplayOption(con, prof.root_ent_id, rte_type));
        
        if (xml_suffix != null) {
            result.append(xml_suffix);
        }

        result.append("</report_body>").append(cwUtils.NEWL);
        return result.toString();
    }

    /**
    Get display option for the report template.
    Display option include <user>, <item>, <run> and <other>
    @param con Connection to database
    @param owner_ent_id site entity id
    @param rte_type report template type
    @return XML of report template display option, <display_option>...</display_option>
    */
    private String getDisplayOption(Connection con, long owner_ent_id, String rte_type) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        //get display option
        xmlBuf.append("<display_option>");
        if(rte_type != null) {
            String view_type = null;
            if( rte_type.equals(Report.TARGET_LEARNER) ){
                view_type = DbObjectView.OJV_TYPE_TARGET_LEARNER_REPORT;
            } else if (rte_type.equals(Report.LEARNING_ACTIVITY_LRN)) {
			    view_type = DbObjectView.OJV_TYPE_LEARNING_ACTIVITY_LRN;
            } else if (rte_type.equals(Report.LEARNING_ACTIVITY_COS)) {
            	view_type = DbObjectView.OJV_TYPE_LEARNING_ACTIVITY_COS;
            } else if(rte_type.equals(Report.MODULE)) {
                view_type = DbObjectView.OJV_TYPE_MODULE;
            } else if(rte_type.indexOf(Report.LEARNER) > -1) {
                view_type = DbObjectView.OJV_TYPE_LEARNER_REPORT;
            } else if(rte_type.indexOf(Report.COURSE) > -1) {
                view_type = DbObjectView.OJV_TYPE_COURSE_REPORT;
            } else if (rte_type.startsWith(Report.SURVEY_COS_PREFIX)){
                view_type = DbObjectView.OJV_TYPE_SURVEY_COURSE_REPORT;
            } else if (rte_type.startsWith(Report.EXAM_PAPER_STAT)){
                view_type = DbObjectView.OJV_TYPE_EXAM_PAPER_STAT;
            } else if (rte_type.startsWith(Report.TRAIN_FEE_STAT)){
                view_type = DbObjectView.OJV_TYPE_TRAIN_FEE_STAT;
            } else if (rte_type.startsWith(Report.TRAIN_COST_STAT)){
                view_type = DbObjectView.OJV_TYPE_TRAIN_COST_STAT;
            }
            if(view_type != null) {
                //get user display option
                DbObjectView userView = 
                    DbObjectView.getObjectView(con, owner_ent_id, 
                                               view_type, DbObjectView.OJV_SUBTYPE_USR);
                if(userView != null) {
                    xmlBuf.append("<user>")
                          .append(cwUtils.escNull(userView.getOptionXML()))
                          .append("</user>");
                }
                
                //get user classification display option
                DbObjectView userClassView = DbObjectView.getObjectView(con, owner_ent_id,
                                                                        view_type, DbObjectView.OJV_SUBTYPE_USR_CLASSIFY);
                if( userClassView != null ){
                    xmlBuf.append("<user_classify>")
                          .append(cwUtils.escNull(userClassView.getOptionXML()))
                          .append(cwUtils.escNull(userClassification(con, owner_ent_id)))
                          .append("</user_classify>");
                }
                //get item display option
                DbObjectView itemView = 
                    DbObjectView.getObjectView(con, owner_ent_id, 
                                               view_type, DbObjectView.OJV_SUBTYPE_ITM);
                if(itemView != null) {
                    xmlBuf.append("<item>")
                          .append(cwUtils.escNull(itemView.getOptionXML()))
                          .append("</item>");
                }
                //get run display option
                DbObjectView runView = 
                    DbObjectView.getObjectView(con, owner_ent_id, 
                                               view_type, DbObjectView.OJV_SUBTYPE_RUN);
                if(runView != null) {
                    xmlBuf.append("<run>")
                          .append(cwUtils.escNull(runView.getOptionXML()))
                          .append("</run>");
                }
                //get other display option
                DbObjectView otherView = 
                    DbObjectView.getObjectView(con, owner_ent_id, 
                                               view_type, DbObjectView.OJV_SUBTYPE_OTHER);
                if(otherView != null) {
                    xmlBuf.append("<other>")
                          .append(cwUtils.escNull(otherView.getOptionXML()))
                          .append("</other>");
                }
            }
        }
        xmlBuf.append("</display_option>");
        return xmlBuf.toString();
    }

    public String getPresentaton(Connection con, String spec_xml) throws SQLException , cwException, cwSysMessage{
        Hashtable spec_pairs = getSpecPairs(spec_xml);
        StringBuffer result = new StringBuffer();
        Vector spec_values;

        result.append("<presentation>").append(cwUtils.NEWL);

        if( spec_pairs.containsKey("itm_id") ) {              
            spec_values = (Vector)spec_pairs.get("itm_id");
            //String spec_value = (String)spec_values.elementAt(0);
            long itm_id_parent = LearningModuleReport.getItmIdParent(con,(Long.parseLong((String)spec_values.elementAt(0))));
            for(int i=0; i<spec_values.size(); i++){
                result.append("<data name=\"itm_id\" value=\"").append(spec_values.elementAt(i)).append("\" display=\"");
                result.append(cwUtils.esc4XML(aeItem.getItemTitle(con,Long.parseLong((String)spec_values.elementAt(i)))));
                result.append("\"");
                
                aeItem aeItm = new aeItem();
                aeItm.itm_id = Long.parseLong((String)spec_values.elementAt(i));
                result.append(" itm_code=\"")
                    .append(cwUtils.esc4XML(aeItm.getItemCode(con)))
                    .append("\"");
                result.append(" >");
                //itm_parent                    
                if(itm_id_parent != 0){
                    result.append("<parent value=\"").append(itm_id_parent).append("\" display=\"");
                    result.append(cwUtils.esc4XML(aeItem.getItemTitle(con,itm_id_parent)));
                    result.append("\"");
                    aeItm.itm_id = itm_id_parent;
                    result.append(" itm_code=\"")
                        .append(cwUtils.esc4XML(aeItm.getItemCode(con)))
                        .append("\"");
                
                    result.append(" />");
                }

                result.append("</data>");
            }
        }
        
        if( spec_pairs.containsKey("mod_itm_id") ) {   
        	spec_values = (Vector)spec_pairs.get("mod_itm_id");
        	long itm_id_parent = LearningModuleReport.getItmIdParent(con,(Long.parseLong((String)spec_values.elementAt(0))));
            for(int i=0; i<spec_values.size(); i++){
                result.append("<data name=\"mod_itm_id\" value=\"").append(spec_values.elementAt(i)).append("\" display=\"");
                result.append(cwUtils.esc4XML(aeItem.getItemTitle(con,Long.parseLong((String)spec_values.elementAt(i)))));
                result.append("\"");
                
                aeItem aeItm = new aeItem();
                aeItm.itm_id = Long.parseLong((String)spec_values.elementAt(i));
                result.append(" itm_code=\"")
                    .append(cwUtils.esc4XML(aeItm.getItemCode(con)))
                    .append("\"");
                result.append(" >");
                //itm_parent                    
                if(itm_id_parent != 0){
                    result.append("<parent value=\"").append(itm_id_parent).append("\" display=\"");
                    result.append(cwUtils.esc4XML(aeItem.getItemTitle(con,itm_id_parent)));
                    result.append("\"");
                    aeItm.itm_id = itm_id_parent;
                    result.append(" itm_code=\"")
                        .append(cwUtils.esc4XML(aeItm.getItemCode(con)))
                        .append("\"");
                
                    result.append(" />");
                }

                result.append("</data>");
            }
        }

        if (spec_pairs.containsKey("ent_id")) {
            spec_values = (Vector)spec_pairs.get("ent_id");

            StringBuffer ent_id_lst = new StringBuffer();

            ent_id_lst.append("(0");

            for (int i=0; i<spec_values.size(); i++) {
                ent_id_lst.append(", " + spec_values.elementAt(i));
            }

            ent_id_lst.append(")");

            Hashtable user_group_hash = dbUserGroup.getDisplayName(con, ent_id_lst.toString());
            Hashtable user_hash = dbRegUser.getDisplayName(con, ent_id_lst.toString()); 

            for (int i=0; i<spec_values.size(); i++) {
                String spec_value = (String)spec_values.elementAt(i);

                result.append("<data name=\"ent_id\" value=\"").append(spec_value).append("\" display=\"");
                
                if (user_group_hash.containsKey(spec_value)) {
                    result.append(cwUtils.esc4XML((String)user_group_hash.get(spec_value)));
                } else if (user_hash.containsKey(spec_value)) {
                    result.append(cwUtils.esc4XML((String)user_hash.get(spec_value)));
                }
                
                result.append("\"/>");
            }
        }
        
		if (spec_pairs.containsKey("usr_ent_id")) {
            spec_values = (Vector)spec_pairs.get("usr_ent_id");
            
            StringBuffer ent_id_lst = new StringBuffer();
            
            ent_id_lst.append("(0");
            
            for (int i=0; i<spec_values.size(); i++) {
                ent_id_lst.append(", " + spec_values.elementAt(i));
            }
            
            ent_id_lst.append(")");
            
            Hashtable user_group_hash = dbUserGroup.getDisplayName(con, ent_id_lst.toString());
            Hashtable user_hash = dbRegUser.getDisplayName(con, ent_id_lst.toString()); 
            
            for (int i=0; i<spec_values.size(); i++) {
                String spec_value = (String)spec_values.elementAt(i);
                
                result.append("<data name=\"usr_ent_id\" value=\"").append(spec_value).append("\" display=\"");
                
                if (user_group_hash.containsKey(spec_value)) {
                    result.append(cwUtils.esc4XML((String)user_group_hash.get(spec_value)));
                } else if (user_hash.containsKey(spec_value)) {
                    result.append(cwUtils.esc4XML((String)user_hash.get(spec_value)));
                }
                
                result.append("\"/>");
            }
        }
        
        if (spec_pairs.containsKey("usg_ent_id")) {
            spec_values = (Vector)spec_pairs.get("usg_ent_id");
            
            StringBuffer ent_id_lst = new StringBuffer();
            
            ent_id_lst.append("(0");
            
            for (int i=0; i<spec_values.size(); i++) {
                ent_id_lst.append(", " + spec_values.elementAt(i));
            }
            
            ent_id_lst.append(")");
            
            Hashtable user_group_hash = dbUserGroup.getDisplayName(con, ent_id_lst.toString());
            Hashtable user_hash = dbRegUser.getDisplayName(con, ent_id_lst.toString()); 
            
            for (int i=0; i<spec_values.size(); i++) {
                String spec_value = (String)spec_values.elementAt(i);
                
                result.append("<data name=\"usg_ent_id\" value=\"").append(spec_value).append("\" display=\"");
                
                if (user_group_hash.containsKey(spec_value)) {
                    result.append(cwUtils.esc4XML((String)user_group_hash.get(spec_value)));
                } else if (user_hash.containsKey(spec_value)) {
                    result.append(cwUtils.esc4XML((String)user_hash.get(spec_value)));
                }
                
                result.append("\"/>");
            }
        }

        if (spec_pairs.containsKey("ugr_ent_id")) {
            spec_values = (Vector)spec_pairs.get("ugr_ent_id");

            StringBuffer ugr_ent_id_lst = new StringBuffer();

            ugr_ent_id_lst.append("(0");

            for (int i=0; i<spec_values.size(); i++) {
                ugr_ent_id_lst.append(", " + spec_values.elementAt(i));
            }

            ugr_ent_id_lst.append(")");

            Hashtable grade_hash = DbUserGrade.getDisplayName(con, ugr_ent_id_lst.toString());

            for (int i=0; i<spec_values.size(); i++) {
                String spec_value = (String)spec_values.elementAt(i);

                result.append("<data name=\"ugr_ent_id\" value=\"").append(spec_value).append("\" display=\"");
                result.append(cwUtils.esc4XML((String)grade_hash.get(spec_value)));
                result.append("\"/>");
            }
        }        
        
        if (spec_pairs.containsKey("tnd_id")) {
            spec_values = (Vector)spec_pairs.get("tnd_id");

            StringBuffer tnd_id_lst = new StringBuffer();

            tnd_id_lst.append("(0");

            for (int i=0; i<spec_values.size(); i++) {
                tnd_id_lst.append(", " + spec_values.elementAt(i));
            }

            tnd_id_lst.append(")");

            Hashtable tnd_hash = aeTreeNode.getDisplayName(con, tnd_id_lst.toString());

            for (int i=0; i<spec_values.size(); i++) {
                String spec_value = (String)spec_values.elementAt(i);

                result.append("<data name=\"tnd_id\" value=\"").append(spec_value).append("\" display=\"");
                
                if (tnd_hash.containsKey(spec_value)) {
                    result.append(cwUtils.esc4XML((String)tnd_hash.get(spec_value)));
                }
                
                result.append("\"/>");
            }
        }
        if (spec_pairs.containsKey("mod_id")) {
            spec_values = (Vector) spec_pairs.get("mod_id");
            for (int i = 0; i < spec_values.size(); i++) {
                result.append("<data name=\"mod_id\" value=\"").append(spec_values.elementAt(i)).append("\" display=\"");
                result.append(cwUtils.esc4XML(dbResource.getResTitle(con, Long.parseLong((String) spec_values.elementAt(i)))));
                result.append("\"");
                result.append(" />");
            }
        
            String spec_value = (String)spec_values.elementAt(0);
            long mod_id = Long.parseLong(spec_value);
            String mod_title = dbResource.getResTitle(con, mod_id);
            
            result.append("<data name=\"mod_id\" value=\"").append(spec_value).append("\" display=\"").append(cwUtils.esc4XML(mod_title)).append("\">");
            Survey svy = new Survey();
            svy.res_id = mod_id;
            svy.mod_res_id = mod_id;
            try{
                svy.mod_type = dbModule.getResSubType(con,mod_id);
            }catch(qdbException e) {
                 throw new cwException(e.getMessage());
            }
            if(svy.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)){
                result.append("<survey>");
                result.append("<title>").append(cwUtils.esc4XML(mod_title)).append("</title>");
                result.append(svy.getSuqAsXML(con, null, new Vector(), new Hashtable()));
                result.append("</survey>");
            }
            result.append("</data>");
        }     
        
        if (spec_pairs.containsKey("tcr_id")) {
        	spec_values = (Vector)spec_pairs.get("tcr_id");
        	for (int i = 0; i < spec_values.size(); i++) { 
	        	result.append("<data name=\"tcr_id\" value=\"").append(spec_values.get(i)).append("\" display=\"");
	            result.append(cwUtils.esc4XML(DbTrainingCenter.getTcrTitle(con, Long.parseLong((String) spec_values.elementAt(i)))));
	            result.append("\"");
	            result.append(" />");
        	}
        }
        result.append("</presentation>").append(cwUtils.NEWL);
                
        return result.toString();
    }

    public Hashtable getSpecPairs(String spec_xml) {
        XMLparser myXMLparser = null;
        Hashtable spec_pairs = new Hashtable();
        
        try {
            StringReader in = new StringReader(spec_xml);
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            SAXParser saxParser  = saxFactory.newSAXParser();
            myXMLparser = new XMLparser();
            saxParser.parse(new InputSource(in), myXMLparser);
            in.close();
            
            spec_pairs = myXMLparser.spec_pairs;
        } catch (ParserConfigurationException e) {
            e.getMessage();
        } catch (SAXException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        }
        
        return spec_pairs;
    }

    public static String getAttendanceStatusList(Connection con, long root_ent_id) throws SQLException {
        StringBuffer result = new StringBuffer();
        
        result.append("<attendance_status_list>").append(cwUtils.NEWL);
        result.append(aeAttendanceStatus.statusAsXML(con, root_ent_id));
        result.append("</attendance_status_list>").append(cwUtils.NEWL);

        return result.toString();
    }    

    public static void println(String out) {
        if (DEBUG) {
        	CommonLog.debug(out);
        }
    }
    
    public static void println(double out) {
        if (DEBUG) {
        	CommonLog.debug(String.valueOf(out));
        }
    }    

    // added for certificate
    public static String getCertificateStatusList(Connection con) throws SQLException, cwSysMessage {
        StringBuffer result = new StringBuffer();

        ViewCfCertificate certification = new ViewCfCertificate(con);
        result.append(certification.getCertificationLstAsXML());

        return result.toString();
    }
    
    /**
    Render XML to echo searching parameters of a report specification
    @param con Connection to database
    @param request http server request
    @param prof loginProfile of the session
    @param rsp_id id of the report specification 
    @param rte_type report template type
    @return XML that echo searching parameters of the report specification
    */
    public String echoSearchParamAsXML(Connection con, HttpServletRequest request,
                                       loginProfile prof, long rsp_id, String rte_type) 
        throws SQLException, cwException, cwSysMessage {

        return getReport(con, request, prof, rsp_id, 0, null, null, null, rte_type, null);
    }

    /**
    Render XML to echo searching parameters as XML
    @param con Connection to database
    @param request http server request
    @param prof loginProfile of the session
    @param rte_type report template type
    @param spec_name String array of param names
    @param spec_value String array of param values
    @return XML to echo searching parameters as XML
    */
    public String echoSearchParamAsXML(Connection con, HttpServletRequest request,
                                       loginProfile prof, String rte_type,
                                       String[] spec_name, String[] spec_value) 
        throws SQLException, cwException, cwSysMessage {

        DbReportSpec dbSpec = Report.toSpec(0, null, spec_name, spec_value);
        return getReport(con, request, prof, 0, 0, null, null, dbSpec.rsp_xml, rte_type, null);
    }
    
    private static String XSL_ITM_RESULT_VIEW_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output omit-xml-declaration=\"yes\" indent=\"yes\" method=\"xml\"/><xsl:variable name=\"content_lst\">itm_content_lst</xsl:variable><xsl:variable name=\"item_access\">item_access</xsl:variable><xsl:template match=\"/report_template/template_view\"><template_view><xsl:apply-templates select=\"*\" mode=\"section\"/></template_view></xsl:template><xsl:template match=\"section\" mode=\"section\"><xsl:element name=\"{name()}\"><xsl:for-each select=\"@*\"><xsl:copy-of select=\".\"/></xsl:for-each><xsl:apply-templates select=\"*\" mode=\"field\"/></xsl:element></xsl:template><xsl:template match=\"*\" mode=\"field\"><xsl:variable name=\"in_data\"><xsl:call-template name=\"is_in_data_xml\"><xsl:with-param name=\"field_name\"><xsl:value-of select=\"name()\"/></xsl:with-param><xsl:with-param name=\"field_id\"><xsl:value-of select=\"@id\"/></xsl:with-param></xsl:call-template></xsl:variable><xsl:if test=\"$in_data!=''\"><xsl:copy-of select=\".\"/></xsl:if></xsl:template><xsl:template name=\"is_in_data_xml\"><xsl:param name=\"field_name\"/><xsl:param name=\"field_id\"/><xsl:for-each select=\"/report_template/data_list/data[@name = $content_lst]\"><xsl:if test=\"@value = $field_name or @value = concat($field_name, '_', $field_id)\">true</xsl:if></xsl:for-each></xsl:template></xsl:stylesheet>";
    private static String XSL_RUN_RESULT_VIEW_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output omit-xml-declaration=\"yes\" indent=\"yes\" method=\"xml\"/><xsl:variable name=\"content_lst\">run_content_lst</xsl:variable><xsl:variable name=\"item_access\">run_item_access</xsl:variable><xsl:template match=\"/report_template/template_view\"><template_view><xsl:apply-templates select=\"*\" mode=\"section\"/></template_view></xsl:template><xsl:template match=\"section\" mode=\"section\"><xsl:element name=\"{name()}\"><xsl:for-each select=\"@*\"><xsl:copy-of select=\".\"/></xsl:for-each><xsl:apply-templates select=\"*\" mode=\"field\"/></xsl:element></xsl:template><xsl:template match=\"*\" mode=\"field\"><xsl:variable name=\"in_data\"><xsl:call-template name=\"is_in_data_xml\"><xsl:with-param name=\"field_name\"><xsl:value-of select=\"name()\"/></xsl:with-param><xsl:with-param name=\"field_id\"><xsl:value-of select=\"@id\"/></xsl:with-param></xsl:call-template></xsl:variable><xsl:if test=\"$in_data!=''\"><xsl:copy-of select=\".\"/></xsl:if></xsl:template><xsl:template name=\"is_in_data_xml\"><xsl:param name=\"field_name\"/><xsl:param name=\"field_id\"/><xsl:for-each select=\"/report_template/data_list/data[@name = $content_lst]\"><xsl:if test=\"@value = $field_name or @value = concat($field_name, '_', $field_id)\">true</xsl:if></xsl:for-each></xsl:template></xsl:stylesheet>";
    private static String XSL_ITM_RESULT_VIEW_KEY = "XSL_ITM_RESULT_VIEW";
    private static String XSL_RUN_RESULT_VIEW_KEY = "XSL_RUN_RESULT_VIEW";
    
    /**
    Get Item Report Result View
    */
    String getItmResultView(Vector vData, String templateView) 
        throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String output;
        String dataXML = vector2DataXML("itm_content_lst", vData);
        
        input.append(cwUtils.xmlHeader)
             .append("<report_template>")
             .append(dataXML)
             .append(templateView)
             .append("</report_template>");
        output = cwXSL.processFromString(input.toString(), XSL_ITM_RESULT_VIEW_CONTENT);
        
        return output;
    }

    /**
    Get Run Report Result View
    */
    String getRunResultView(Vector vData, String templateView) 
        throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String output;
        String dataXML = vector2DataXML("run_content_lst", vData);
        
        input.append(cwUtils.xmlHeader)
             .append("<report_template>")
             .append(dataXML)
             .append(templateView)
             .append("</report_template>");
        output = cwXSL.processFromString(input.toString(), XSL_RUN_RESULT_VIEW_CONTENT);
        
        return output;
    }
    
    /**
    Render dataXML of a given type of data, (e.g. "content_lst", "itm_content_lst")
    @param dataName data name of the data in Vector
    @param v Vector of data in String format
    @return XML of <data_list>
    */
    private String vector2DataXML(String dataName, Vector v) {
        
        String dataXML = null;
        if(v!=null && v.size()>0) {
            StringBuffer xmlBuf = new StringBuffer(256);
            xmlBuf.append("<data_list>");
            for(int i=0; i<v.size(); i++) {
                xmlBuf.append("<data name=\"").append(dataName).append("\"")
                      .append(" value=\"").append((String)v.elementAt(i)).append("\"/>");
            }
            xmlBuf.append("</data_list>");
            dataXML = xmlBuf.toString();
        }
        return dataXML;
    }
 
    /**
    Create a DbTemplateView object for the input data and template view xml
    @param vContent_lst Vector to values from spec_name itm_content_lst/run_content_lst
    @param tvw_xml template view xml
    @return Object of DbTemplateView
    */
    DbTemplateView createTemplateView(Vector vContent_lst, String tvw_xml) {
        DbTemplateView tvw = null;
        if(tvw_xml != null) {
            tvw = new DbTemplateView();
            tvw.tvw_xml = tvw_xml;
            tvw.tvw_cat_ind = false;
            tvw.tvw_target_ind = false;
            tvw.tvw_cm_ind = false;
            tvw.tvw_mote_ind = false;
            tvw.tvw_itm_acc_ind = false;
            tvw.tvw_res_ind = false;
            tvw.tvw_rsv_ind = false;
            tvw.tvw_cost_center_ind = false;
            tvw.tvw_tcr_ind = false;
            for(int i=0; i<vContent_lst.size(); i++) {
                String data = (String)vContent_lst.elementAt(i);
                if(!tvw.tvw_cat_ind && data.equalsIgnoreCase("catalog")) {
                    tvw.tvw_cat_ind = true;
                } else if(!tvw.tvw_target_ind && data.equalsIgnoreCase("target_lrn")) {
                    tvw.tvw_target_ind = true;
                } else if(!tvw.tvw_cm_ind && data.equalsIgnoreCase("competency")) {
                    tvw.tvw_cm_ind = true;
                } else if(!tvw.tvw_mote_ind && data.indexOf("mote")>-1) {
                    tvw.tvw_mote_ind = true;
                } else if(!tvw.tvw_itm_acc_ind && data.indexOf("item_access")>-1) {
                    tvw.tvw_itm_acc_ind = true;
                } else if(!tvw.tvw_res_ind && data.indexOf("cos")>-1) {
                    tvw.tvw_res_ind = true;
                } else if(!tvw.tvw_rsv_ind && data.indexOf("rsv")>-1) {
                    tvw.tvw_rsv_ind = true;
                } else if(!tvw.tvw_cost_center_ind && data.indexOf("cost_center")>-1) {
                    tvw.tvw_cost_center_ind = true;
                } else if (!tvw.tvw_tcr_ind && data.indexOf("training_center") > -1) {
                	tvw.tvw_tcr_ind = true;
                }
            }
        }
        return tvw;
    }
    
    
    private String userClassification(Connection con, long root_ent_id)
        throws SQLException {
            
            Vector v_ucf_type = DbUserClassification.getAllUserClassificationTypeInOrg(con, root_ent_id, null);
            StringBuffer buf = new StringBuffer();
            buf.append("<classification>");
            for(int i=0; i<v_ucf_type.size(); i++){
                buf.append("<attribute>").append(USR_CURRENT_).append(v_ucf_type.elementAt(i)).append("</attribute>");
            }
            buf.append("</classification>");
            return buf.toString();
        }
    
}