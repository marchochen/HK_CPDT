package com.cw.wizbank.report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.cw.wizbank.ae.db.ViewEvnSurveyQueReport;
import com.cw.wizbank.ae.db.view.ViewSurveyQueReport;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class EvnSurveyQueReport {

	static final String SPEC_KEY_MOD_ID = "mod_id";
	
	private Connection con;

	public EvnSurveyQueReport(Connection inCon) {
		this.con = inCon;
	}
	
	public class EvnSurveyQueRptParam {

        //user info
        public String cur_lan;
        public long root_ent_id;
        //selection param
        
        public long mod_res_id;
        public String rsp_title;
        public boolean isShowAllFBAns;
        public boolean isXlsFile;
        //system info
        public String xlsFilePath;
        public String encoding;
    }

	public String[] getReportBySpec(loginProfile prof, WizbiniLoader wizbini, String[] spec_name, String[] spec_value, boolean isShowAllFBAns,
			String curLan, String encoding, long root_ent_id) throws cwException, SQLException,
			IOException {
		EvnSurveyQueRptParam param = null;
		param = getEvnParamBySpec(spec_name, spec_value, curLan, isShowAllFBAns, encoding);
		param.root_ent_id = root_ent_id;
		return getEvnSurveyQueStatXml(param);
	}
	
	public String getXlsFileByRsp(loginProfile prof, WizbiniLoader wizbini, String[] spec_name, String[] spec_value, boolean isShowAllFBAns,
			String curLan, String encoding, long root_ent_id, String xlsFilePath) throws cwException, SQLException,
			IOException, qdbErrMessage {
		EvnSurveyQueRptParam param;
        param = getEvnParamBySpec(spec_name, spec_value, curLan, isShowAllFBAns, encoding);
        param.root_ent_id = root_ent_id;
        param.xlsFilePath = xlsFilePath;
        return getEvnSurveyQueStatXls(param);
    }

	/**
	 * to get parameter list of evaluation survey.
	 * 
	 * @param spec_name
	 * @param spec_value
	 * @param curLan
	 * @param isShowAllFBAns
	 * @param encoding
	 * @return
	 */
	public EvnSurveyQueRptParam getEvnParamBySpec(String[] spec_name, String[] spec_value, String curLan, boolean isShowAllFBAns, String encoding) {
		EvnSurveyQueRptParam param = new EvnSurveyQueRptParam();
		for (int paramIndex = 0; paramIndex < spec_name.length; paramIndex++) {
			// mod_id
			if (spec_name[paramIndex].equals(SPEC_KEY_MOD_ID)) {
				param.mod_res_id = Long.parseLong(spec_value[paramIndex]);
				break;
			}
		}
		param.cur_lan = curLan;
		param.isShowAllFBAns = isShowAllFBAns;
		param.encoding = encoding;
		return param;
	}
	
	public String[] getEvnSurveyQueStatXml(EvnSurveyQueRptParam param) throws cwException, SQLException, IOException {
        String resultXml[] = new String[1];
        
        ArrayList templateList = null;
        Hashtable hashData = getEvnSurveyInfo(param, templateList);
        
        templateList = (ArrayList)hashData.get("templateList");
        long responseCount = Long.parseLong((String)hashData.get("responseCount"));
        long enrolledCount = Long.parseLong((String)hashData.get("enrolledCount"));
        double responseRate = Double.parseDouble((String)hashData.get("responseRate"));
        
        //get report content by question
        ViewEvnSurveyQueReport viewEvnSurveyQueRpt = new ViewEvnSurveyQueReport(con);
        viewEvnSurveyQueRpt.getDetail(templateList, param.mod_res_id, responseCount, param.isShowAllFBAns);
        
        StringBuffer xmlList = new StringBuffer();
        xmlList.append("<report_list>");
        xmlList.append(getRptHeadToStr(viewEvnSurveyQueRpt.getRptHead(param.mod_res_id)));
        xmlList.append("<gen_stat lrn_cnt=\"").append(enrolledCount).append("\" res_cnt= \"").append(responseCount)
        	.append("\" res_rate=\"").append(SurveyQueReport.formatDouble(responseRate, SurveyQueReport.DEC_POINT)).append("\"/>");
        
        xmlList.append("<que_stat_list>");
        java.util.Iterator obj = templateList.iterator();
        while (obj.hasNext()) {
            SurveyQueData gx = (SurveyQueData)obj.next();
            xmlList.append(gx.getAsXml(param.isShowAllFBAns));
        }
        xmlList.append("</que_stat_list></report_list>");
        
        resultXml[0] = xmlList.toString();
		return resultXml;
    }
	
	public String getEvnSurveyQueStatXls(EvnSurveyQueRptParam param) throws FileNotFoundException, SQLException, qdbErrMessage, IOException, cwException {
        ArrayList templateList = null;

        Hashtable hashData = getEvnSurveyInfo(param, templateList);
        templateList = (ArrayList)hashData.get("templateList");
        EvnSurveyQueReportXls surveyQueRptXls = new EvnSurveyQueReportXls(con, param);
        ViewEvnSurveyQueReport viewEvnSurveyRpt = new ViewEvnSurveyQueReport(con);

        long responseCount = Long.parseLong((String)hashData.get("responseCount"));
        long enrolledCount = Long.parseLong((String)hashData.get("enrolledCount"));
        double responseRate = Double.parseDouble((String)hashData.get("responseRate"));

        viewEvnSurveyRpt.getDetail(templateList, param.mod_res_id, responseCount, param.isShowAllFBAns);

        String fileName = surveyQueRptXls.outputSurveyQueReport((double)responseCount, (double)enrolledCount, responseRate, templateList);
        return fileName;
    }
	
	public Hashtable getEvnSurveyInfo(EvnSurveyQueRptParam param, ArrayList templateList) throws cwException, SQLException {
    	Hashtable hashData = new Hashtable();
    	ViewSurveyQueReport ssr = new ViewSurveyQueReport(con);
        templateList = ssr.getTemplateQue(param.mod_res_id);
        hashData.put("templateList", templateList);
    	
        //the count of target user
        long modId = param.mod_res_id;
    	ViewEvnSurveyQueReport viewEvnSurveyQueRpt = new ViewEvnSurveyQueReport(con);
        long targetUserCount = viewEvnSurveyQueRpt.getTargetUserCountByMod(modId);
        hashData.put("enrolledCount", String.valueOf(targetUserCount));
        
        //the count of user who has responsed the survey.
        long responseCount = viewEvnSurveyQueRpt.getReponseCount(modId);
        hashData.put("responseCount", String.valueOf(responseCount));
        
        double responseRate;
		if (targetUserCount != 0) {
			responseRate = (double) responseCount / targetUserCount * 100;
		}
        else {
            responseRate = 0;
        }
        hashData.put("responseRate", String.valueOf(responseRate));
        
    	return hashData;
    }
	
	public String getRptHeadToStr(ViewEvnSurveyQueReport.HeadData headData) {
		StringBuffer headXml = new StringBuffer();
		headXml.append("<report_head>");
		
		//module title
		String modTitle = headData.mod_title;
		if (modTitle != null) {
			headXml.append("<mod_title>").append(cwUtils.esc4XML(modTitle)).append("</mod_title>");
		}
		headXml.append("</report_head>");

		return headXml.toString();
	}
	
	
}
