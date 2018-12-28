package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class CreditReport extends ReportTemplate {
	public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) throws cwException,
			SQLException, cwException, Exception, IOException {
		return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, Report.CREDIT, null);
	}
	
	public String getReportXML(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id, String rsp_title,
			String[] spec_name, String[] spec_value, boolean tc_enabled) throws SQLException, cwException, cwSysMessage {
		String reportxml = "";
		Hashtable spec_pairs;
		String rsp_xml;
		if (rsp_id > 0) {
			ViewReportSpec spec = new ViewReportSpec();
			ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
			rsp_xml = data.rsp_xml;
			spec_pairs = this.getSpecPairs(data.rsp_xml);

		} else {
			DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);
			rsp_xml = rpt_spec.rsp_xml;
			spec_pairs = this.getSpecPairs(rpt_spec.rsp_xml);
		}
		Credit creditManager = new Credit();
		String xml = creditManager.getRptXml(con, spec_pairs, tc_enabled, prof.root_ent_id, prof.usr_ent_id);
		if (rsp_id > 0) {
			reportxml = getReport(con, request, prof, rsp_id, rte_id, null, xml, null, Report.CREDIT, null); 
		} else {
			reportxml = getReport(con, request, prof, 0, rte_id, null, xml, rsp_xml, Report.CREDIT, rsp_title);
		}
		return reportxml;
	}
	
}
