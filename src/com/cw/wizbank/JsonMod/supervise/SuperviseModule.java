package com.cw.wizbank.JsonMod.supervise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import net.sf.json.JSONArray;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.supervise.bean.StaffReportBean;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;

public class SuperviseModule extends ServletModule {

	SuperviseModuleParam modParam;
	public static final String MOD_NAME = "supervise_module";
	
	public SuperviseModule() {
		super();
		modParam = new SuperviseModuleParam();
		param = modParam;
	}

	public void process() throws SQLException, IOException, cwException {
		
		try {
				if(prof==null||prof.usr_ent_id==0){
					throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
				}else{					
						
					if (modParam.getCmd().equalsIgnoreCase("get_my_staff")) {	
						Supervise spv=new Supervise();
						HashMap staffMap=new HashMap();
						Vector all_staff=spv.getAllStaff(con, prof.usr_ent_id,modParam);		
						staffMap.put("group_lst", spv.getMyStaffTreeRoot(con,prof.usr_ent_id, prof.cur_lan));
						staffMap.put("staff_lst", all_staff);
						staffMap.put("total", new Integer(modParam.getTotal_rec()));
						staffMap.put("staff", spv.getFistStaff(con, all_staff, prof, wizbini));
						resultJson.put("my_staff", staffMap);
						 
					}else if(modParam.getCmd().equalsIgnoreCase("get_staff_tree")){
						cwTree tree =new cwTree();
						tree.isJsonTree=true;
						tree.node_id=modParam.getGroup_id();
						tree.tree_type=cwTree.USER_GROUP;
						tree.get_direct_supervise=false;
						tree.get_supervise_group=false;
						String group_lst=tree.userGroup2Tree(con, prof);
						JSONArray array= JSONArray.fromObject(group_lst, defJsonConfig);
						hasMetaAndSkin=false;
						out.print(array.toString());
						
					}else if(modParam.getCmd().equalsIgnoreCase("get_staff_lst")){
						Supervise spv=new Supervise();
						HashMap staffMap=new HashMap();
						spv.getStaffByType(con, prof.usr_ent_id, modParam, staffMap,prof,wizbini);	
						resultJson.put("my_staff", staffMap);
						
					}else if(modParam.getCmd().equalsIgnoreCase("get_staff_info")){
						Supervise spv=new Supervise();
						HashMap staffMap=new HashMap();
						staffMap.put("staff", spv.getStaffInfo(con, modParam,prof , wizbini));
						resultJson.put("my_staff", staffMap);
						
					}else if(modParam.getCmd().equalsIgnoreCase("search_staff") || modParam.getCmd().equalsIgnoreCase("search_staff_xml")){
						HashMap staffMap=new HashMap();
						Supervise spv=new Supervise();
						Vector Staff=spv.searchStaff(con, prof.usr_ent_id, modParam);
						if (modParam.getReturn_xml()) {
							StringBuffer metaXMLBuf = new StringBuffer(1024);
			                metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id));
							resultXml = formatXML(spv.searchStaffByXML(con, Staff, modParam.getCur_page(), modParam.getPage_size()), metaXMLBuf.toString(), MOD_NAME);
						} else {
							staffMap.put("staff_lst", Staff);
							staffMap.put("staff", spv.getFistStaff(con, Staff,prof, wizbini));
							staffMap.put("total", new Integer(modParam.getTotal_rec()));
							resultJson.put("my_staff", staffMap);
						}
					}else if(modParam.getCmd().equalsIgnoreCase("ins_staff_rpt_spec")){
						Supervise spv=new Supervise();
						boolean isAdd=spv.saveStaffRptSpc(con, prof, modParam);
						if(isAdd){
							redirectUrl = modParam.getUrl_success();
						}else{
							redirectUrl = modParam.getUrl_failure();
						}
						
					}else if(modParam.getCmd().equalsIgnoreCase("upd_staff_rpt_spec")){
						Supervise spv=new Supervise();
						boolean isUpd=spv.updStaffRptSpc(con, prof, modParam);
						if(isUpd){
							redirectUrl = modParam.getUrl_success();
						}else{
							redirectUrl = modParam.getUrl_failure();
						}
						
						
					}else if(modParam.getCmd().equalsIgnoreCase("del_staff_rpt_spec")){
						Supervise spv=new Supervise();
						boolean isDel=spv.delStaffRptSpc(con,  modParam);
						hasMetaAndSkin=false;
						if(isDel){
							redirectUrl = modParam.getUrl_success();
						}else{
							redirectUrl = modParam.getUrl_failure();
						}
						
					}else if(modParam.getCmd().equalsIgnoreCase("get_staff_rpt_lst")){
						Supervise spv=new Supervise();
						HashMap spcMap= new HashMap();
						spcMap=spv.getRptSpecLstByTypeAndUsr(con, prof, modParam);
						resultJson.put("staff_rpt", spcMap);
						cwXSL.getGoldenManHtml(prof.label_lan, modParam.getGoldenman_param(), resultJson);
					}else if(modParam.getCmd().equalsIgnoreCase("get_staff_rpt_tpl")){
						Supervise spv=new Supervise();
						StaffReportBean report= spv.getSpecInfo(con, modParam.getRsp_id());
						Vector gmVc =spv.getGoldenManOption(report);
						cwXSL.getGoldenManHtml(prof.label_lan, modParam.getGoldenman_param(), resultJson);
						cwXSL.outPutGoldManOption(resultJson, gmVc);
						resultJson.put("staff_rpt",report);
						JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.supervise.bean.StaffReportBean.class, "rsp_title_noescape", defJsonConfig);
					}else if(modParam.getCmd().equalsIgnoreCase("get_app_pend")){
						Supervise spv=new Supervise();
						Vector app_lst=spv.getAllApp_pendInfo(con, prof, modParam);
						HashMap appMap = new HashMap();
						appMap.put("app_lst",app_lst);
						appMap.put("page", modParam.getPage());
						resultJson.put("app_pend", appMap);
						aeQueueManager.convertWorkflowToJson(con, prof.root_ent_id, resultJson);
						
					}else if(modParam.getCmd().equalsIgnoreCase("get_app_approval")){
						Supervise spv=new Supervise();
						Vector app_lst=spv.getAllApp_ApprovalInfo(con, prof, modParam);
						HashMap appMap = new HashMap();
						appMap.put("app_lst",app_lst);
						appMap.put("page", modParam.getPage());
						resultJson.put("app_approval", appMap);
						
					} else {
						throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
					}
				}
		} catch (qdbException e) {
			throw new cwException(e.getMessage(), e);
		}
	}	
}
