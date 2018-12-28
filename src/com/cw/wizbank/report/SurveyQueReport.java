/*
 * Created on 2005-4-25
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.view.ViewSurveyQueReport;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;


/**
 * @author dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SurveyQueReport {
    static final String DELIMITER = "~";
    static final String SPEC_KEY_MOD_ID = "mod_id";
    static final String SPEC_KEY_ITM_ID = "itm_id";
    static final String SPEC_KEY_TND_ID = "tnd_id";
    static final String SPEC_KEY_ITM_TYPE = "itm_type";
    static final String SPEC_KEY_START_DATETIME = "att_create_start_datetime";
    static final String SPEC_KEY_END_DATETIME = "att_create_end_datetime";
    static final String SPEC_KEY_RSP_TITLE = "rsp_title";
    static final String SPEC_VALUE_EMPTY = "";
    static final int DEC_POINT = 1;
	
    public static String formatDouble(double var, int dec) {
        BigDecimal bb = new BigDecimal(var);
        BigDecimal cc = bb.setScale(dec, BigDecimal.ROUND_DOWN);
        return cc.toString();
    }

    public static double formatNum(double var, int dec) {
        BigDecimal bb = new BigDecimal(var);
        BigDecimal cc = bb.setScale(dec, BigDecimal.ROUND_DOWN);
        return Double.parseDouble(cc.toString());
    }

    private Connection con;
    
    public class SurveyQueRptParam {

        //user info
        public String cur_lan;
        public long root_ent_id;
        //selection param
        
        public long que_id;
        public long mod_res_id;
        public long[] itm_id;
        public long[] tnd_id;
        public String[] itm_dummy_type = null;
        public Timestamp period_from = null;
        public Timestamp period_to = null;
        public String[] question_type;
        public String rsp_title;
//        public String itmTypeString = null;
        public boolean isShowAllFBAns;
        public boolean isXlsFile;
        public boolean answer_for_course;
        public boolean answer_for_lrn_course;
        public boolean all_cos_ind;
        //system info
        public String xlsFilePath;
        public String encoding;
    }

    public SurveyQueReport(Connection inCon) {
        this.con = inCon;
    }

    public Hashtable getInfo(SurveyQueRptParam param, ArrayList templateList) throws cwException, SQLException {
        //is empty
        Hashtable hashData = new Hashtable();
/*        if (param.itm_type != null) {
            StringBuffer itmtype = new StringBuffer();
            itmtype.append("(");
            for (int i = 0; i < param.itm_type.length; i++) {
                itmtype.append("'").append(param.itm_type[i]).append("'");
                if (i != param.itm_type.length - 1) {
                    itmtype.append(",");
                }
            }
            itmtype.append(")");
            param.itmTypeString = itmtype.toString();

        }*/

        Vector tableVec = new Vector();
        if (param.itm_id != null) {
            for (int i = 0; i < param.itm_id.length; i++) {
                tableVec.add(new Long(param.itm_id[i]));
            }
        }
        
        String userItem = null;
        if (tableVec != null) {
            userItem = cwSQL.createSimpleTemptable(con, ViewSurveyQueReport.TEMP_TAB_COLUMN_NAME, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, userItem, tableVec, cwSQL.COL_TYPE_LONG);
        }
        hashData.put("userItem", userItem);

        ViewSurveyQueReport ssr = new ViewSurveyQueReport(con);
        templateList = ssr.getTemplateQue(param.mod_res_id);
        hashData.put("templateList", templateList);

        String userSpecifiedItem = ssr.getUserSpecifiedItem(param);
        hashData.put("userSpecifiedItem", userSpecifiedItem);

        long responseCount = ssr.getResponseCount(userSpecifiedItem, param);
        hashData.put("responseCount", String.valueOf(responseCount));

        long enrolledCount = ssr.getEnrolledCount(userSpecifiedItem, param);
        hashData.put("enrolledCount", String.valueOf(enrolledCount));

        double responseRate;
        if (enrolledCount != 0) {
            responseRate = (double)responseCount / enrolledCount * 100;
        }
        else {
            responseRate = 0;
        }
        hashData.put("responseRate", String.valueOf(responseRate));
        StringBuffer xmlList = new StringBuffer();
        return hashData;
    }

    /*
     * return a xls file path
     * 
     */
    public String getSurveyQueStatXls(SurveyQueRptParam param, boolean tc_enabled) throws FileNotFoundException, SQLException, qdbErrMessage, IOException, cwException {
        ArrayList templateList = null;
        String temp = new String();

        Hashtable hashData = getInfo(param, templateList);
        templateList = (ArrayList)hashData.get("templateList");
        SurveyQueReportXls surveyQueRptXls = new SurveyQueReportXls(con, param);
        ViewSurveyQueReport vsqr = new ViewSurveyQueReport(con);

        long responseCount = Long.parseLong((String)hashData.get("responseCount"));
        long enrolledCount = Long.parseLong((String)hashData.get("enrolledCount"));
        double responseRate = Double.parseDouble((String)hashData.get("responseRate"));

        vsqr.getDetail(templateList, param, (String)hashData.get("userSpecifiedItem"), responseCount);
        //getSuvetQueReport need a tempTable(userItem) to call getReportHead()

        String fileName = surveyQueRptXls.outputSurveyQueReport((String)hashData.get("userItem"), (String)hashData.get("userSpecifiedItem"), (double)responseCount, (double)enrolledCount, responseRate, templateList, tc_enabled);
        cwSQL.dropTempTable(con, (String)hashData.get("userSpecifiedItem"));
        return fileName;
    }

    public String[] getSurveyQueStatXml(SurveyQueRptParam param, boolean tc_enabled) throws cwException, SQLException, IOException {
        ArrayList templateList = null;
        String temp[] = new String[1];
        Hashtable hashData = getInfo(param, templateList);
        ViewSurveyQueReport vsqr = new ViewSurveyQueReport(con);

        templateList = (ArrayList)hashData.get("templateList");
        long responseCount = Long.parseLong((String)hashData.get("responseCount"));
        long enrolledCount = Long.parseLong((String)hashData.get("enrolledCount"));
        double responseRate = Double.parseDouble((String)hashData.get("responseRate"));
        StringBuffer xmlList = new StringBuffer();
        vsqr.getDetail(templateList, param, (String)hashData.get("userSpecifiedItem"), responseCount);
        cwSQL.dropTempTable(con, (String)hashData.get("userSpecifiedItem"));

        xmlList = new StringBuffer();
        xmlList.append("<report_list>");

        if (param.que_id > 0) {
            SurveyQueData gx = (SurveyQueData)templateList.get((int)param.que_id - 1);
            xmlList.append("<que_stat_list>");
            xmlList.append(gx.getAsXml(param.isShowAllFBAns));
        }
        else {
            //if Personal Report Templates has a name then put it in result's title.
            if (param.rsp_title != null) {
                xmlList.append("<rsp_title>").append(cwUtils.esc4XML(param.rsp_title)).append("</rsp_title>");
            }
            xmlList.append(getRptHeadToStr(vsqr.getRptHead((String)hashData.get("userItem"), param), param, tc_enabled)).append("<gen_stat lrn_cnt=\"").append(enrolledCount).append("\" res_cnt= \"").append(responseCount).append("\" res_rate=\"").append(formatDouble(responseRate, DEC_POINT)).append("\"/>");
            xmlList.append("<que_stat_list>");
            java.util.Iterator obj = templateList.iterator();
            while (obj.hasNext()) {
                SurveyQueData gx = (SurveyQueData)obj.next();
                // Bug 17679 - 管理端：培训报告->課程評估問卷報告（數據以題目分組）->执行，报告结果中的问卷题目显示代码 
                gx.que_xml = cwUtils.unescHTML(gx.que_xml);
                // 有些题目会缺失<html>
                gx.que_xml = gx.que_xml.replaceAll("<body><html>", "<body>").replaceAll("</html>", "").replace("<body>", "<body><html>").replace("<interaction", "</html><interaction");
                xmlList.append(gx.getAsXml(param.isShowAllFBAns));
            }
        }

        xmlList.append("</que_stat_list></report_list>");
        temp[0] = xmlList.toString();
        return temp;
    }


    /**
     * @param map
     * @return
     */
    private StringBuffer getRptHeadToStr(ViewSurveyQueReport.HeadData headData,SurveyQueRptParam param, boolean tc_enabled) {
        StringBuffer headXml = new StringBuffer();
        headXml.append("<report_head>");
        Iterator obj;
        if(tc_enabled) {
        	headXml.append("<all_cos_ind>").append(param.all_cos_ind).append("</all_cos_ind>");
        	headXml.append("<answer_for_course>").append(param.answer_for_course).append("</answer_for_course>");
        	headXml.append("<answer_for_lrn_course>").append(param.answer_for_lrn_course).append("</answer_for_lrn_course>");
        }
        if (!headData.cos_title.isEmpty()) {
        	if(!tc_enabled || !param.all_cos_ind) {
                headXml.append("<itm_title_list>");
                obj = headData.cos_title.iterator();
                while (obj.hasNext()) {
                    headXml.append("<itm_title>").append(cwUtils.esc4XML((String)obj.next())).append("</itm_title>");
                }
                headXml.append("</itm_title_list>");
        	}
        }

        if (!headData.cos_catalog.isEmpty()) {
            headXml.append("<tnd_title_list>");
            obj = headData.cos_catalog.iterator();
            while (obj.hasNext()) {
                headXml.append("<tnd_title>").append(cwUtils.esc4XML((String)obj.next())).append("</tnd_title>");
            }
            headXml.append("</tnd_title_list>");
        }

        if (headData.mod_title != null) {
            headXml.append("<mod_title>").append(cwUtils.esc4XML(headData.mod_title)).append("</mod_title>");
        }

        if (headData.ity_title_xml != null) {
            headXml.append("<item_type_list>").append(headData.ity_title_xml).append("</item_type_list>");
        }

        if (headData.period_from != null || headData.period_to != null) {
            headXml.append("<period ");
            if (headData.period_from != null) {
                headXml.append("from=\"").append(headData.period_from).append("\"");
            }
            if (headData.period_to != null) {
                headXml.append(" to=\"").append(headData.period_to).append("\"");
            }
            headXml.append("/>");
        }

        headXml.append("<rte_info ");
        headXml.append("rte_id=\"").append(headData.rte_id).append("\" rte_get_xsl=\"").append(headData.rte_get_xsl).append("\" rte_exe_xsl=\"").append(headData.rte_exe_xsl).append("\" rte_dl_xsl=\"").append(headData.rte_dl_xsl).append("\"");
        headXml.append("/>").append("</report_head>");

        return headXml;
    }

    public SurveyQueRptParam getParamBySpec(loginProfile prof, WizbiniLoader wizbini, String[] spec_name, String[] spec_value, long que_id, boolean isShowAllFBAns, boolean isXlsFile, String curLan, String tempPath, String encoding) throws SQLException {
        SurveyQueRptParam param = new SurveyQueRptParam();
        for (int pam_cnt = 0; pam_cnt < spec_name.length; pam_cnt++) {
            if(wizbini.cfgTcEnabled) {
            	if(spec_name[pam_cnt].equals("answer_for_course")) {
            		if(spec_value[pam_cnt].equals("1")) {
            			param.answer_for_course  = true;
            		}
            		continue;
            	}
            	if(spec_name[pam_cnt].equals("answer_for_lrn_course")) {
            		if(spec_value[pam_cnt].equals("1")) {
            			param.answer_for_lrn_course  = true;
            		}
              		continue;
        	    }
            }
            if (spec_name[pam_cnt].equals(SPEC_KEY_TND_ID)) {
                if (spec_value[pam_cnt].equals(SPEC_VALUE_EMPTY)) {
                    param.tnd_id = null;
                }
                else {
                    param.tnd_id = cwUtils.splitToLong(spec_value[pam_cnt], DELIMITER);
                }
                continue;
            }

            if (spec_name[pam_cnt].equals(SPEC_KEY_MOD_ID)) {
                param.mod_res_id = Long.parseLong(spec_value[pam_cnt]);
                continue;
            }

            // itm_type start end

            if (spec_name[pam_cnt].equals(SPEC_KEY_ITM_ID)) {
                if (spec_value[pam_cnt].equals(SPEC_VALUE_EMPTY)) {
                    param.itm_id = null;
                }
                else {
                    param.itm_id = cwUtils.splitToLong(spec_value[pam_cnt], DELIMITER);
                }
                continue;
            }

            if (spec_name[pam_cnt].equals(SPEC_KEY_ITM_TYPE)) {
                if (spec_value[pam_cnt].equals(SPEC_VALUE_EMPTY)) {
                    param.itm_dummy_type = null;
                }
                else {
                    param.itm_dummy_type = cwUtils.splitToString(spec_value[pam_cnt], DELIMITER);
                }
                continue;
            }

            if (spec_name[pam_cnt].equals(SPEC_KEY_START_DATETIME)) {
                if (spec_value[pam_cnt].equals(SPEC_VALUE_EMPTY)) {
                    param.period_from = null;

                }
                else {
                    param.period_from = Timestamp.valueOf(spec_value[pam_cnt]);
                }
                continue;
            }

            if (spec_name[pam_cnt].equals(SPEC_KEY_END_DATETIME)) {
                if (spec_value[pam_cnt].equals(SPEC_VALUE_EMPTY)) {
                    param.period_to = null;
                }
                else {
                    param.period_to = Timestamp.valueOf(spec_value[pam_cnt]);
                }
                continue;
            }

            param.que_id = que_id;
            param.cur_lan = curLan;
            param.isShowAllFBAns = isShowAllFBAns;
            param.isXlsFile = isXlsFile;
            param.xlsFilePath = tempPath;
            param.encoding = encoding;
        }
        if(wizbini.cfgTcEnabled) {
            if(param.itm_id ==null && param.tnd_id == null) {
            	param.all_cos_ind = true;
            }
        	if(param.all_cos_ind) {
        		Vector vec = new Vector();
        		if((param.answer_for_course && param.answer_for_lrn_course )||(!param.answer_for_course && !param.answer_for_lrn_course )) {
        			vec = ViewLearnerReport.getAllCos(con, prof.usr_ent_id, prof.root_ent_id);
        		} else if (param.answer_for_course && !param.answer_for_lrn_course) {
        			vec = ViewLearnerReport.getMyRspCos(con, prof.usr_ent_id, prof.root_ent_id);
        		} else if (!param.answer_for_course && param.answer_for_lrn_course) {
        			vec = ViewLearnerReport.getMyRspLrnEnrollCos(con, prof.usr_ent_id, prof.root_ent_id);
        		}
        		if(vec.size() == 0) {
        			param.itm_id  = new long[1];
        			param.itm_id[0]  = 0;
        		} else {
        			param.itm_id  = new long[vec.size()];
        			for(int i=0; i<vec.size(); i++) {
        				param.itm_id[i] = ((Long)vec.elementAt(i)).longValue();
        			}
        		}
        	}
        }
        return param;
    }

    public SurveyQueRptParam getParamByRsp(loginProfile prof, WizbiniLoader wizbini, long rsp_id, SurveyReport report, long que_id, boolean isShowAllFBAns, boolean isXlsFile, String curLan, String xlsFilePath, String encoding) throws SQLException, cwException {
        // get the spec name and value pair by rsp_id
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
        Hashtable spec_pairs = report.getSpecPairs(data.rsp_xml);
        
        // extract spec values
        SurveyQueRptParam param = new SurveyQueRptParam();
        Vector spec_values;
        if(wizbini.cfgTcEnabled) {
            boolean all_cos_ind = false;
            boolean answer_for_course = false;
            boolean answer_for_lrn_course = false;
  
        	if(spec_pairs.containsKey("answer_for_course")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_course");
        		if(((String)spec_values.get(0)).equals("1")) {
        			answer_for_course  = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_lrn_course")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_lrn_course");
        		if(((String)spec_values.get(0)).equals("1")) {
        			answer_for_lrn_course  = true;
        		}
    	    }
            if(!spec_pairs.containsKey("tnd_id") && !spec_pairs.containsKey("itm_id")) {
            	all_cos_ind = true;
            }
            if(all_cos_ind &&  spec_pairs.containsKey("answer_for_course") && spec_pairs.containsKey("answer_for_lrn_course")) {
            	Vector vec = new Vector();
            	if((answer_for_course && answer_for_lrn_course )||(!answer_for_course && !answer_for_lrn_course )) {
            		vec = ViewLearnerReport.getAllCos(con, prof.usr_ent_id, prof.root_ent_id);
            	} else if (answer_for_course && !answer_for_lrn_course) {
            		vec = ViewLearnerReport.getMyRspCos(con, prof.usr_ent_id, prof.root_ent_id);
            	} else if (!answer_for_course && answer_for_lrn_course) {
            		vec = ViewLearnerReport.getMyRspLrnEnrollCos(con, prof.usr_ent_id, prof.root_ent_id);
            	}
            	if(!all_cos_ind) {
            		if(vec.size() ==0) {
            			param.tnd_id = new long[1];
            			param.tnd_id[0] = 0;
            		} else {
            			param.tnd_id = new long[vec.size()];
            			for(int i=0; i<vec.size(); i++) {
            				param.tnd_id[i] = ((Long)vec.elementAt(i)).longValue();
            			}            		
            		}
            	}
            }
            param.all_cos_ind = all_cos_ind;
            param.answer_for_course = answer_for_course;
            param.answer_for_lrn_course = answer_for_lrn_course;
        }
        if (spec_pairs.containsKey(SPEC_KEY_TND_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_TND_ID);

            param.tnd_id = new long[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                param.tnd_id[i] = Long.parseLong((String)spec_values.elementAt(i));
            }
        }

        if (spec_pairs.containsKey(SPEC_KEY_ITM_TYPE)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_TYPE);

            param.itm_dummy_type = new String[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                param.itm_dummy_type[i] = (String)spec_values.elementAt(i);
            }
        }

        if (spec_pairs.containsKey(SPEC_KEY_START_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_START_DATETIME);

            param.period_from = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        if (spec_pairs.containsKey(SPEC_KEY_END_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_END_DATETIME);

            param.period_to = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        if (spec_pairs.containsKey(SPEC_KEY_MOD_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_MOD_ID);
            param.mod_res_id = Long.parseLong((String)spec_values.elementAt(0));
        }

        if (spec_pairs.containsKey(SPEC_KEY_ITM_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_ID);
            param.itm_id = new long[spec_values.size()];
            for (int i = 0; i < spec_values.size(); i++) {
                param.itm_id[i] = Long.parseLong((String)spec_values.elementAt(i));
            }

        }

        param.isShowAllFBAns = isShowAllFBAns;
        param.isXlsFile = isXlsFile;
        param.rsp_title = data.rsp_title;
        param.cur_lan = curLan;
        param.que_id = que_id;
        param.xlsFilePath = xlsFilePath;
        param.encoding = encoding;
        return param;
    }

    public String[] getReportByRsp(loginProfile prof, WizbiniLoader wizbini,long rsp_id, SurveyReport report, long que_id, boolean isShowAllFBAns, boolean isXlsFile, String curLan, String encoding, long root_ent_id) throws cwException, SQLException, IOException {
        SurveyQueRptParam param;
        param = getParamByRsp(prof, wizbini,rsp_id, report, que_id, isShowAllFBAns, isXlsFile, curLan, null, encoding);
        param.root_ent_id = root_ent_id;
        return getSurveyQueStatXml(param, wizbini.cfgTcEnabled);
    }

    public String[] getReportBySpec(loginProfile prof, WizbiniLoader wizbini,String[] spec_name, String[] spec_value, long que_id, boolean isShowAllFBAns, boolean isXlsFile, String curLan, String encoding, long root_ent_id) throws cwException, SQLException, IOException {
        SurveyQueRptParam param;
        param = getParamBySpec(prof, wizbini, spec_name, spec_value, que_id, isShowAllFBAns, isXlsFile, curLan, null, encoding);
        param.root_ent_id = root_ent_id;
        return getSurveyQueStatXml(param, wizbini.cfgTcEnabled);
    }

    public String getXlsFileByRsp(loginProfile prof, WizbiniLoader wizbini,long rsp_id, SurveyReport report, long que_id, boolean isShowAllFBAns, boolean isXlsFile, String curLan, String xlsFilePath, String encoding, long root_ent_id) throws FileNotFoundException, SQLException, qdbErrMessage, IOException, cwException {
        SurveyQueRptParam param;
        param = getParamByRsp(prof, wizbini,rsp_id, report, que_id, isShowAllFBAns, isXlsFile, curLan, xlsFilePath, encoding);
        param.root_ent_id = root_ent_id;
        return getSurveyQueStatXls(param, wizbini.cfgTcEnabled);
    }

    public String getXlsFileBySpec(loginProfile prof, WizbiniLoader wizbini,String[] spec_name, String[] spec_value, long que_id, boolean isShowAllFBAns, boolean isXlsFile, String curLan, String tempPath, String encoding, long root_ent_id) throws FileNotFoundException, SQLException, qdbErrMessage, IOException, cwException {
        SurveyQueRptParam param;
        param = getParamBySpec(prof, wizbini, spec_name, spec_value, que_id, isShowAllFBAns, isXlsFile, curLan, tempPath, encoding);
        param.root_ent_id = root_ent_id;
        return getSurveyQueStatXls(param, wizbini.cfgTcEnabled);
    }
}
