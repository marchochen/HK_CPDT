
package com.cw.wizbank.report;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.TrainFeeStatReport.ItmData;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

import jxl.write.WriteException;

public  class TrainFeeStatExporter extends ReportExporter  {
    
	private static final String XML_NODE_COST = "cost";
	private static final String XML_NODE_LABEL = "label";
	private static final String XML_PROPERTY_TYPE = "type";
	private static final String XML_PROPERTY_LANG = "xml:lang";
	
    public TrainFeeStatExporter(Connection incon, ExportController inController) {
    		super(incon, inController);
    }
    
    static final String DEFAULT_SORT_ORDER = "ASC";
    
    
    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
    	aeItem item = new aeItem();
    	String xml_prefix = item.getItemCostAttribute(wizbini,prof.root_id);
    	
		TrainFeeStatReport report = new TrainFeeStatReport();
		Map reportHash = report.getDataHash(con, specData.sortCol, specData.sortOrder, specData.tcr_id, specData.start_datetime, 
										specData.end_datetime, specData.dummy_type, specData.train_scope);
		Map dataHash = (Map)reportHash.get(TrainFeeStatReport.DATA_HASH);
		Map summaryHash = (Map)reportHash.get(TrainFeeStatReport.SUMMARY_HASH);
		Map totalHash = (Map)reportHash.get(TrainFeeStatReport.TOTAL_HASH);
		List orderLst = (List)reportHash.get(TrainFeeStatReport.ORDER_LST);
		
		controller.setTotalRow(dataHash.size() + 5);
		controller.next();

		TrainFeeStatExportHelper rptBuilder = new TrainFeeStatExportHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit);
		rptBuilder.writeCondition(con, specData);
		
		controller.next();
		
		Map costXmlHash = parseItmCostXml(specData.cur_lang, xml_prefix);
		controller.next();
		//write report summary
		rptBuilder.writeSummaryData(summaryHash, totalHash, costXmlHash, specData.cur_lang);
		controller.next();
		
		
		rptBuilder.writeTableHead(specData.cur_lang, costXmlHash);
		controller.next();

		ItmData itmData = null;
		Long itmId = null;
		for (int i = 0; i < orderLst.size() && !controller.isCancelled(); i++) {
			itmId = (Long)orderLst.get(i);
			itmData = (ItmData)dataHash.get(itmId);
			rptBuilder.writeData(specData.cur_lang, itmData, (List)costXmlHash.get("typeOrder"));
			controller.next();
		}
		
		if (!controller.isCancelled()) {
            controller.setFile(rptBuilder.finalizeFile());
        }
        
    }
	
    //列出所有培训费用项，过滤掉 type 相同的项
	private Map parseItmCostXml(String cur_lang, String itmCostXml) throws IOException, qdbException {
		Map costXmlHash = new HashMap();
		try {
			List typeOrder = new ArrayList();
			costXmlHash.put("typeOrder", typeOrder);
			
			DOMParser xmlParser = new DOMParser();
			xmlParser.parse(new InputSource(new StringReader(itmCostXml)));
			Document document = xmlParser.getDocument();
			Element root = document.getDocumentElement();
			NodeList costList = root.getElementsByTagName(XML_NODE_COST);
			NodeList labelList = null;
			Element data_node = null;
			String type = null;
			int i, j;
			for (i = 0; i < costList.getLength(); i++) {
				data_node = (Element)costList.item(i);
				type = data_node.getAttribute(XML_PROPERTY_TYPE);
				if (!typeOrder.contains(type)) {
					typeOrder.add(type);
					labelList = data_node.getElementsByTagName(XML_NODE_LABEL);
					for (j = 0; j < labelList.getLength(); j++) {
						data_node = (Element)labelList.item(j);
						if (cur_lang.equals(data_node.getAttribute(XML_PROPERTY_LANG))) {
							costXmlHash.put(type, data_node.getFirstChild().getNodeValue());
						}
					}
				}
			}
		} catch (SAXException e) {
			throw new qdbException(e);
		}
		return costXmlHash;
	}

	public String getItmCostAsXml(String cur_lang, String itmCostXml) throws IOException, qdbException {
		Map costXmlHash = parseItmCostXml(cur_lang, itmCostXml);
		List typeOrder = (List)costXmlHash.get("typeOrder");
		StringBuffer xml = new StringBuffer();
		xml.append("<item_cost>");
		String type = null;
		for (int i = 0; i < typeOrder.size(); i++) {
			type = (String)typeOrder.get(i);
			xml.append("<cost type=\"").append(type).append("\">").append(cwUtils.esc4XML((String)costXmlHash.get(type))).append("</cost>");
		}
		xml.append("</item_cost>");
		return xml.toString();
	}
}
