package com.cw.wizbank.JsonMod.tcrTemplate;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import com.cw.wizbank.JsonMod.tcrCommon.TcrModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.utils.CommonLog;

public class TcrTemplateModule extends TcrModule {
	private final String tcrTemplate = "tcrTemplate";
	private final String ispreView = "isPreView";
	private final String tcrTemplatePreView = "../app/home?" + ispreView + "=true&tcr_id=";

	private TcrTemplateLogic tcrTemplateLogic = TcrTemplateLogic.getInstance();

	private TcrTemplateParam tcrTemplateParam;


	public TcrTemplateModule() {
		super(null, com.cw.wizbank.JsonMod.tcrTemplate.TcrTemplateParam.class,
				new String[]{"closeWindows", "dis_preView_tempalte_pre"},
				new String[]{"get_revert_deleted_module"});
		this.tcrTemplateParam = (TcrTemplateParam) this.tcrParam;
	}

	/*
	 * 显示调整模板模块页面
	 */
	public void get_tcr_template_module() {
		try {
			Long tcr_id = new Long(tcrTemplateParam.getTcr_id());
			Map tcrTemMap = sqlMapClient.getObject(con, "select tt_id, tt_dis_fun_navigation_ind dis_fun_ind from TcrTemplate where tt_tcr_id = ?", new Object[]{tcr_id});
			Long tt_id = Long.valueOf(String.valueOf(tcrTemMap.get("tt_id")));
			int dis_fun_ind = Integer.valueOf(String.valueOf(tcrTemMap.get("dis_fun_ind"))).intValue();

			dataXml.append("<tcr_template tcr_id = \"" + tcr_id.longValue() + "\" tt_id = \"" + tt_id.longValue() + "\" dis_fun_ind = \"" + dis_fun_ind + "\"/>");
			dataXml.append(tcrTemplateLogic.getTcr_template_module_pre_xml_data(con, tt_id, prof.cur_lan));

			resultXml = formatXML(dataXml.toString(), tcrTemplate);
		} catch (Exception e) {
			logger.error("method get_tcr_template_module() excuete error.", e);
			throw new RuntimeException(
					"method get_tcr_template_module() execute error.");
		}

	}

	/*
	 * 显示添加删除模块界面
	 */
	public void get_revert_deleted_module() {
		try {
			String metaXML = "";
			resultXml = formatXML("<revertDeletedModule></revertDeletedModule>", metaXML, tcrTemplate);
		} catch (Exception e) {
			logger.error("method get_revert_deleted_module() excuete error.", e);
			throw new RuntimeException("method get_revert_deleted_module() execute error.");
		}
	}

	/*
	 * 保存调整好的培训中心模板
	 */
	public void save_tcr_template() throws IOException, cwException, SQLException{
		cwSysMessage suc_message = new cwSysMessage("tcr_tem_01");
		cwSysMessage fail_message = new cwSysMessage("tcr_tem_02");
		try {
			 tcrTemplateLogic.saveTcrTemplate(con, tcrTemplateParam, request);
			 msgBox(MSG_STATUS, suc_message, tcrTemplateParam.getUrl_success(), out);
		} catch (Exception e) {
			isTransactionException = true;
			logger.error("method save_tcr_template execute error.\n", e);
			msgBox(MSG_STATUS, fail_message, tcrTemplateParam.getUrl_failure(), out);
		}

	}


	/*
	 * 预览调整好的培训中心模板
	 */
	public void dis_preView_tempalte_pre() throws IOException, SQLException{
		resultXml = formatXML("", tcrTemplate);
	}

	public void preview_tcr_template() throws SQLException{
		tcrTemplateLogic.getPrevewTableName(con, tcrTemplateParam, request);
		redirectUrl = tcrTemplatePreView + tcrTemplateParam.getTcr_id();
	}

	public void closeWindows() throws IOException{
		PrintWriter writer = response.getWriter();
		try {
			writer.println("<html>");
			writer.println("<head></head>");
			writer.println("<body onload=\"window.close();\"></body>");
			writer.println("</html>");
			writer.flush();
		} catch (RuntimeException e) {
			CommonLog.error(e.getMessage(),e);
		}finally{
			if(writer != null){
				writer.close();
			}
		}
	}
}
