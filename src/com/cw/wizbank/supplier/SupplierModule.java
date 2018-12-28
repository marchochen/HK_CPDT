/**
 * 
 */
package com.cw.wizbank.supplier;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

/**
 * 
 * @author Leon.li 2013-5-24 4:41:20 message Supplier modules
 */
public class SupplierModule extends ServletModule {
	
	SupplierModuleParam modParam;

	public static final String MOD_NAME = "supplier_module";

	public static final String ADD_SUCCESS = "GEN001";
	public static final String DEL_SUCCESS = "GEN002";
	public static final String UPD_SUCCESS = "GEN003";
	public static final String ADD_FAIL = "1033";
	public static final String UPD_FAIL = "GEN006";
	public static final String NOT_TO_VIEW = "1034";

	public static final int ADD_COURSE_EXPERIENCE_LENGTH = 2;
	
	public static final String DOWN_LIST_NAME = "1022";

	public SupplierModule() {
		super();
		modParam = new SupplierModuleParam();
		param = modParam;
	}

	@Override
	public void process() throws SQLException, IOException, cwException {
       
		if (this.prof == null || this.prof.usr_ent_id == 0) {
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			SupplierService spService = new SupplierService();
			AccessControlWZB acl = new AccessControlWZB();
			//list
			if (param.getCmd().equalsIgnoreCase("get_supplier_list")
					|| param.getCmd().equalsIgnoreCase("get_supplier_list_xml")) {
/*				if (!acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, FTN_SUPPLIER_MGT)) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}*/
				
				StringBuffer xml = new StringBuffer("");
				try {
					modParam.setSpl_tcr_id(prof.my_top_tc_id);
					xml.append(spService.getAllSupplier2Xml(con, modParam ,false));
				} catch (Exception e) {
					 CommonLog.error(e.getMessage(),e);
					throw new cwException(e.getMessage());
				} 
				//To provide XML data page
				xml.append(spService.getStatusXmlString(modParam.getSplStatus(),this.prof));
				if(modParam.getSearch_text()!=null&&modParam.getSearch_text().length()>0){
					xml.append("<search_text>"+cwUtils.esc4XML(modParam.getSearch_text())+"</search_text>");
				}
				resultXml = formatXML(xml.toString(), MOD_NAME);
				
			// view
			}  else if (param.getCmd().equalsIgnoreCase("get_supplier_view")
					|| param.getCmd().equalsIgnoreCase("get_supplier_view_xml")) {

/*				if (!acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, FTN_SUPPLIER_MGT)) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}*/
				
				String xml = "";
				if (modParam.getSplId() > 0) {
					//not exists
					if(!spService.isExists(con, modParam.getSplId())){
						sysMsg = getErrorMsg(NOT_TO_VIEW, param.getUrl_failure());
						return;
					}
					try {
						String dirName = wizbini.cfgSysSetupadv.getFileUpload().getSupplierDir().getUrl();
						String saveDirPath = cwUtils.replaceSlashToHttp(cwUtils.getFileURL(dirName)+ modParam.getSplId());

						xml = spService.getSupplierXml2View(con, modParam,saveDirPath);
						xml += spService.getSceListXml(con,modParam.getSplId());
						xml += spService.getCourseListXml(con, modParam.getSplId());

					} catch (Exception e) {
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					} 
				}else{
					sysMsg = getErrorMsg(NOT_TO_VIEW, param.getUrl_failure());
					return;
				}
				resultXml = formatXML(xml, MOD_NAME);

			//delete by id
			} else if (param.getCmd().equalsIgnoreCase("supplier_del_id")
					|| param.getCmd().equalsIgnoreCase("supplier_del_id_xml")) {

				if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_SUPPLIER_MAIN})) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				
				if (modParam.getSplId() > 0) {
					 spService.delSupplierById(con, modParam);
					 sysMsg = getErrorMsg(DEL_SUCCESS, param.getUrl_success());
					 return;
				}else{
					sysMsg = getErrorMsg(NOT_TO_VIEW, param.getUrl_failure());
					return;
				}

			// before add
			}else if (param.getCmd().equalsIgnoreCase("get_supplier_prep")
					|| param.getCmd().equalsIgnoreCase("get_supplier_prep_xml")) {
				
				if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_SUPPLIER_MAIN})) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				String xml = "";	
				String courseXML = "";
				String exprienceXML = "";
				if (modParam.getSplId() > 0) {					
						String dirName = wizbini.cfgSysSetupadv.getFileUpload().getSupplierDir().getUrl();
						String saveDirPath = cwUtils.replaceSlashToHttp(cwUtils.getFileURL(dirName)+ modParam.getSplId());
						try {
							xml = spService.getSupplier2ListXml(con, modParam,saveDirPath);
							 courseXML = spService.getCourseListXml(con,modParam.getSplId());
							 exprienceXML = spService.getSceListXml(con,modParam.getSplId());
						} catch (Exception e) {	
							 CommonLog.error(e.getMessage(),e);
							throw new cwException(e.getMessage());
						} 
				} else {
					xml = "<supplier/>";
				}
				// if none , then to empty node
				if ("<sce_list></sce_list>".equals(exprienceXML)||"".equals(exprienceXML)) {
					exprienceXML = "<sce_list>";
					for (int i = 0; i < ADD_COURSE_EXPERIENCE_LENGTH; i++) {
						exprienceXML += "<supplier_cooperation_experience/>";
					}
					exprienceXML += "</sce_list>";
				}				
				if ("<course_list></course_list>".equals(courseXML)||"".equals(courseXML)) {
					courseXML = "<course_list>";
					for (int i = 0; i < ADD_COURSE_EXPERIENCE_LENGTH; i++) {
						courseXML += "<supplier_main_course/>";
					}
					courseXML += "</course_list>";
				}
				xml += exprienceXML;
				xml += courseXML;
				//To provide XML data page
				xml += spService.getStatusXmlString(modParam.getSplStatus(),this.prof);		
				resultXml = formatXML(xml, MOD_NAME);
				
			// to add
			} else if (param.getCmd().equalsIgnoreCase("add_supplier")) {
				
				if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_SUPPLIER_MAIN})) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
				 String saveDirPath = "";
				 long splId = 0;
				if (modParam.getSplId() > 0) {		//修改\
					splId = modParam.getSplId();
					if(spService.isExists(con, modParam.getSplId())){
						try{
							spService.update(con, prof, modParam);
						}catch(Exception e){
							 //sysMsg = getErrorMsg(UPD_FAIL,param.getUrl_failure());
							 CommonLog.error(e.getMessage(),e);
							 throw new cwException(e.getMessage());
						}
						sysMsg = getErrorMsg(UPD_SUCCESS, param.getUrl_success());
					}else{
						//not exists
						sysMsg = getErrorMsg(UPD_FAIL, param.getUrl_failure());
						return;
					}
					saveDirPath = wizbini.getFileUploadSupplierDirAbs()+ dbUtils.SLASH + modParam.getSplId();
				// to insert
				} else {
					try {
						splId = spService.add(con, prof, modParam);
					} catch (Exception e) {
						//sysMsg = getErrorMsg(ADD_FAIL, param.getUrl_failure());
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					}
					if (splId > 0) {
						sysMsg = getErrorMsg(ADD_SUCCESS,param.getUrl_success());
					}
					saveDirPath = wizbini.getFileUploadSupplierDirAbs() + dbUtils.SLASH + splId;
				}
				try {
					File dir = new File(saveDirPath);
					if(!dir.exists()){
						dir.mkdirs();
					}
					dbUtils.copyDir(tmpUploadPath, saveDirPath);
				} catch (qdbException e) {
					CommonLog.error(e.getMessage(),e);
					throw new cwException(e.getMessage());
				}	
				//oprate SupplierCooperationExperience
				if(splId > 0){
					
					modParam.setSplId((int)splId);
	
					// add experience
					spService.delAllSce(con,(int)splId);
					spService.addBatchSce(con,modParam, prof);
					
					// add course
					spService.delAllCourse(con,(int)splId);
					spService.addBatchCourse(con, modParam, prof);
				}
				
				// to download
			} else if(param.getCmd().equalsIgnoreCase("down_supplier_list")){
				
				if (!AccessControlWZB.hasRolePrivilege(prof.current_role, new String []{AclFunction.FTN_AMD_SUPPLIER_MAIN})) {
					sysMsg = getErrorMsg("ACL002", param.getUrl_failure());
					return;
				}
	
		    	String tempDir = wizbini.getFileUploadTmpDirAbs();
		    	String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
		    	String fileName = "supplier_list.xls";//LangLabel.getValue(prof.cur_lan, DOWN_LIST_NAME)+".xls";
		    	String filePath = wizbini.getWebDocRoot() + dbUtils.SLASH ;
		    	//String filePath = wizbini.getWebDocRoot() + dbUtils.SLASH + urlp.template_url;
		    	
		    	File file = spService.download(con, fileName, filePath, tempDir, tempDirName, prof.usr_ent_id, prof.cur_lan,DOWN_LIST_NAME);
		    	if(file!=null&&file.exists()){	    		
	/*	            String filename = file.getName();
		            InputStream fis = new BufferedInputStream(new FileInputStream(file));
		            byte[] buffer = new byte[fis.available()];
		            fis.read(buffer);
		            fis.close();
		            response.reset();
		            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(),this.clientEnc));
		            response.addHeader("Content-Length", "" + file.length());
		            OutputStream outer = new BufferedOutputStream(response.getOutputStream());
		            response.setContentType("application/octet-stream");
		            outer.write(buffer);
		            outer.flush();
		            outer.close();*/
		    		response.sendRedirect("../" + tempDirName + "/" +fileName);
		    	}else{
					sysMsg = getErrorMsg(NOT_TO_VIEW, param.getUrl_failure());
					return;
		    	}
		    	
		    //power by kevin	
			} else if (param.getCmd().equalsIgnoreCase("get_supplierr_comment_list")
					|| param.getCmd().equalsIgnoreCase("get_supplierr_comment_list_xml")) {
				
				String result ="";
				try {
					result = spService.getAllSupplierCommentXml(con, modParam.getSpl_id(),modParam.getCur_page(),modParam.getPage_size());
				} catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
					throw new cwException(e.getMessage());
				} 
				resultXml = formatXML(result, MOD_NAME);
				
			// view
			} else if (param.getCmd().equalsIgnoreCase("supplierr_comment_add")
					|| param.getCmd().equalsIgnoreCase("supplierr_comment_add_xml")) {
				
				
				if(modParam.getSpl_id()>0){
					try {
						spService.insSupplierCommentXml(con,modParam);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					}
					sysMsg = getErrorMsg(ADD_SUCCESS,
							param.getUrl_success());
				}else{
					sysMsg = getErrorMsg(ADD_FAIL,
							param.getUrl_failure());
				}
				
			// view
			}else if (param.getCmd().equalsIgnoreCase("supplierr_comment_up")
					|| param.getCmd().equalsIgnoreCase("supplierr_comment_up_xml")) {
				
				
				if(modParam.getSpl_id()>0){
					try {
						spService.upSupplierCommentXml(con,modParam);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					}
					sysMsg = getErrorMsg(ADD_SUCCESS,
							param.getUrl_success());
				}else{
					sysMsg = getErrorMsg(ADD_FAIL,
							param.getUrl_failure());
				}
				
			}else if (param.getCmd().equalsIgnoreCase("supplierr_comment_add_prep")
					|| param.getCmd().equalsIgnoreCase("supplierr_comment_add_prep_xml")) {
				String result ="";
				if(modParam.getSpl_id()>0){
					try {
						result +=spService.getSupplierCommentXml(con, modParam.getSpl_id(), modParam.getScm_ent_id());
					} catch (Exception e) {
						CommonLog.error(e.getMessage(),e);
						throw new cwException(e.getMessage());
					} 
				}
				resultXml = formatXML(result, MOD_NAME);
				
			}else {
				throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD
						+ param.getCmd());
			}
		}
	}
	

}
