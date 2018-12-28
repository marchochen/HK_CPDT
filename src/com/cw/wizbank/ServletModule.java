package com.cw.wizbank;

import javax.servlet.http.*;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import java.sql.*;
import java.util.*;
import java.io.*;

// Utils classes
import com.oreilly.servlet.*;
import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.JsonMod.ErrorMsg;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.personalization.LangListType;
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.config.organization.personalization.SkinListType;
import com.cw.wizbank.config.organization.personalization.impl.SkinTypeImpl;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cwn.wizbank.exception.cwMessageException;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;

/**
An abstract class to process HTTP servlet request<BR>
*/
public abstract class ServletModule extends Object {
    /**
    HTTP servlet request
    */
    protected HttpServletRequest        request;
    /**
    HTTP servlet response
    */
    protected HttpServletResponse       response;
    /**
    login profile which stored user's informatioin
    */
    protected loginProfile              prof;
    /**
    database connection
    */
    protected Connection                con;
    /**
    wizBank environment variables from INI file
    */
    protected qdbEnv                    static_env;
    /**
    encoding of the client from request.getCharacterEncoding
    */
    protected String                    clientEnc;
    /**
    whether the request is multipart
    */
    protected boolean                   bMultipart = false;
    /**
    object using com.oreilly.servlet.* package to handle multipart request
    */
    protected MultipartRequest          multi;
    /**
    directory for the temporary file upload
    */
    protected String                    tmpUploadPath;

    /**
    Page Variants
    */
    public static Hashtable xslQuestions;

    /**
    message header of the dialog box : STATUS
    */
    public static String                MSG_STATUS="STATUS";
    /**
    message header of the dialog box : INFO
    */
    public static String                MSG_INFO="INFO";
    /**
    message header of the dialog box : ERROR
    */
    public static String                MSG_ERROR="ERROR";
    
    /**
    meta tag name of xml, used in format xml
    */
    private static final String META_TAG = "meta";
    
    private static final String DEAFAULT_ICON= "default_usr_icon";
    
    /**
    SESSION name for mutlipart object, converting uploaded file to a system generated filename
    */
    private static final String SESS_UPLOAD_ORIGINAL_FILENAME = "ORIGINAL_FILENAME";
    private static final String SESS_UPLOAD_NEW_FILENAME = "NEW_FILENAME";
    
    // using xml in place of wizb.ini for system parameters
    protected static WizbiniLoader wizbini = null;
    
    //for jsonMod
    protected  JsonConfig defJsonConfig=null;
    protected  String pageName;
    protected  HashMap MetaMap;
    protected  BaseParam param;
    
    /**
     HttpSession
     */
    protected HttpSession sess;
    
    /**
     用于输出数据到客户端的writer
     */
    protected PrintWriter out;
    
    /**
     用于输出json
     */
    protected HashMap resultJson;
    
    /**
     *用于判断json中是否有"meta""skin"节点，有些页面不需要"meta""skin"节点，则在对应的cmd中置此值为false。
     *true:输出json 
	 *false:不输出
     */
    protected boolean hasMetaAndSkin = true;
    
    /**
     * 用于判断页面是否需要跳转，需要跳转的话把跳转的url赋值给此变量，而不要直接使用response.sendRedirect()方法
     */
    protected String redirectUrl ;
    
    /**
     * 用于管理员页面，把最终生成的xml赋值给此变量，以便直接输出xml或xml+xsl=html给前台
     */
    protected String resultXml;
    
    /**
     * 用于出正确的状态的system message，在页面需要出正确状态的system message时给此变量赋值
     */
    protected ErrorMsg sysMsg;
    
    // for output message by ajax or normal request
    protected boolean is_ajax_request = false;
    
    protected String encoding;


    public static final String REQ_TYPE_XMLHTTPREQUEST = "XMLHttpRequest";
    
    public ServletModule() {
    	this.resultJson = new HashMap();
    }
    
    /**
    initiate the basic class variables
    */
    public void setParams(HttpServletRequest request_, HttpServletResponse response_
            ,loginProfile prof_, Connection con_, qdbEnv static_env_, String clientEnc_, Hashtable xslQuestions_, WizbiniLoader wizbini_) throws IOException {
        request      = request_;
        response     = response_;
        prof         = prof_;
        con          = con_;
        static_env   = static_env_;
        clientEnc    = clientEnc_;
        xslQuestions = xslQuestions_;
        wizbini      = wizbini_;
        sess         = request.getSession();
		out          = response.getWriter();
		if(prof == null){
			encoding = static_env.ENCODING;
		} else{
			if( prof.label_lan.equalsIgnoreCase("Big5") 
					||  prof.label_lan.equalsIgnoreCase("GB2312") 
					|| prof.label_lan.equalsIgnoreCase("ISO-8859-1")){
				encoding = prof.label_lan;
			}else{
				encoding ="ISO-8859-1";
				//throw new IOException(" Param Error");
			}
		}
            
    }

    /**
    initiate the multipart variables
    */
    public void setMultipartParams(MultipartRequest multi_, String tmpUploadPath_) {
        bMultipart      = true;
        multi           = multi_;
        tmpUploadPath   = tmpUploadPath_;
    }
    /**
     *  配置json输出的相关信息
     * @param pageName_
     */
    public void setJsonConfig(String pageName_ ){
    	JsonConfig jsonConfig = new JsonConfig();
    	JsonHelper.setDefaultJsonconfig(jsonConfig);
    	defJsonConfig=jsonConfig;
    	pageName=pageName_;
    	setMetaMap();
    }
    
    /*
    set the type of request, such as ajax or normal request
     */
    public void setRequestType(){
        String reqType = request.getHeader("X-Requested-With");
        if(REQ_TYPE_XMLHTTPREQUEST.equalsIgnoreCase(reqType)) {
            this.is_ajax_request = true;
        }
    }
    
    /**
    should be overrided
     * @throws cwMessageException 
    */
    public void process() throws SQLException, cwException, IOException, qdbException, cwSysMessage, qdbErrMessage, cwMessageException {
    }
    

    /**
    Process XSL transformation
    */
    protected void generalAsHtml(String xmlOL, PrintWriter out, String stylesheet) throws cwException  {
        if(stylesheet == null || stylesheet.length() == 0)
        throw new cwException("Invalid stylesheet");

        String xsl_root = (prof == null) ? null : prof.xsl_root;
        static_env.procXSLFile(xmlOL, stylesheet, out, xsl_root);
    }    

    /**
    Generate a wizBank dialog box with given csSysMessage
    */
    protected void msgBox(String title, cwSysMessage e, String url, PrintWriter out) throws IOException, cwException, SQLException  {
            String encoding;
            if(prof == null)
                encoding = static_env.ENCODING;
            else
                encoding = prof.label_lan;
                
            String msg = e.getSystemMessage(encoding);
            
            if (this.is_ajax_request) {
            	response.addHeader(cwUtils.RESPONSE_HEADER_SYSTEM_MESSAGE, "true");
                HashMap resultMap = new HashMap();
                ErrorMsg errMsg = new ErrorMsg(title, e.getId(), "", url, msg);
                resultMap.put("errMsg", errMsg);
                resultMap.put("success",new Boolean( false));
                resultMap.put("label", getMsgLabel());
                JsonHelper.disableEsc4Json("message", defJsonConfig);
                JsonHelper.writeJson(resultMap, defJsonConfig, out);
            } else {
                genMsgBox(title, msg, url, out);
            }
    }
    
    /**
    Generate a wizBank dialog box with given cwMessageException
    */
    protected void msgBox(String title, cwMessageException e, String url, PrintWriter out) throws IOException, cwException, SQLException  {
            String encoding;
            String curLang;
            if(prof == null){
            	curLang = LabelContent.LangCode_zh_cn;
    			encoding = static_env.ENCODING;
            } else{
            	encoding = prof.label_lan;
            	curLang = prof.cur_lan;
            }
                
            String msg = e.getSystemMessage(encoding,curLang);
            
            if (this.is_ajax_request) {
            	response.addHeader(cwUtils.RESPONSE_HEADER_SYSTEM_MESSAGE, "true");
                HashMap resultMap = new HashMap();
                ErrorMsg errMsg = new ErrorMsg(title, "", "", url, msg);
                resultMap.put("errMsg", errMsg);
                resultMap.put("success",new Boolean( false));
                resultMap.put("label", getMsgLabel());
                JsonHelper.disableEsc4Json("message", defJsonConfig);
                JsonHelper.writeJson(resultMap, defJsonConfig, out);
            } else {
                genMsgBox(title, msg, url, out);
            }
    }
    
    protected void msgBox(String title, cwSysMessage e, String url, PrintWriter out, long mod_id, boolean is_inner) throws IOException, cwException, SQLException  {
        String encoding;
        if(prof == null)
            encoding = static_env.ENCODING;
        else
            encoding = prof.label_lan;
            
        String msg = e.getSystemMessage(encoding);
        if (mod_id > 0){
            genMsgBox(title, msg, url, out, mod_id, is_inner);
        }else{
            genMsgBox(title, msg, url, out);
        }
}  
    
    /**
      Generate a wizBank dialog box with given ErrorMsg
     */
    protected void msgBox(String title, ErrorMsg errMsg, String url, PrintWriter out) throws IOException, cwException{
        if (this.is_ajax_request) {
        	response.addHeader(cwUtils.RESPONSE_HEADER_SYSTEM_MESSAGE, "true");
            HashMap resultMap = new HashMap();
            resultMap.put("errMsg", errMsg);
            resultMap.put("success",new Boolean(false));
            resultMap.put("label", getMsgLabel());
            JsonHelper.disableEsc4Json("message", defJsonConfig);
            JsonHelper.writeJson(resultMap, defJsonConfig, out);
        } else {
            genMsgBox(title, errMsg.getMessage(), url, out);
        }
    }
    
    protected HashMap getMsgLabel() {
    	String cur_lan = prof != null ? prof.cur_lan : wizbini.cfgSysSkinList.getDefaultLang();
    	HashMap label = new HashMap();
        label.put(MSG_STATUS, LangLabel.getValue(cur_lan, "645"/*"lab_status"*/));
        label.put(MSG_INFO, LangLabel.getValue(cur_lan, "645"));
        label.put(MSG_ERROR, LangLabel.getValue(cur_lan, "646"));
        label.put("OK", LangLabel.getValue(cur_lan, "329"));
        return label;
    }
    
    protected void jsonMsgBox(String code){
    	cwSysMessage e = new cwSysMessage(code);
    	 String encoding;
         if(prof == null)
             encoding = static_env.ENCODING;
         else
             encoding = prof.label_lan;
         String msg = e.getSystemMessage(encoding);
    }
    /**
    Generate a wizBank dialog box with given message
    */
    protected void genMsgBox(String title, String msg, String url, PrintWriter out) throws IOException, cwException  {
            StringBuffer xml = new StringBuffer();
            xml.append(cwUtils.xmlHeader).append(cwUtils.NEWL);
            xml.append("<message>").append(cwUtils.NEWL);
            if(prof!=null)
                xml.append(prof.asXML()).append(cwUtils.NEWL);
            xml.append("<title>").append(title).append("</title>").append(cwUtils.NEWL);
            xml.append("<body>").append(cwUtils.NEWL);
            xml.append("<text>").append(cwUtils.esc4XML(msg)).append("</text>").append(cwUtils.NEWL); 
            xml.append("<button url=\"").append(cwUtils.esc4XML(url)).append("\"");
            xml.append(">OK</button>").append(cwUtils.NEWL);
            xml.append("</body>").append(cwUtils.NEWL);
            xml.append("</message>").append(cwUtils.NEWL);

            String xsl_root = (prof == null) ? null : prof.xsl_root;

            static_env.procXSLFile(xml.toString(), static_env.INI_XSL_MSGBOX, out, xsl_root);
    }

    protected void genMsgBox(String title, String msg, String url, PrintWriter out, long mod_id, boolean is_inner) throws IOException, cwException  {
        StringBuffer xml = new StringBuffer();
        
        xml.append(cwUtils.xmlHeader).append(cwUtils.NEWL);
        xml.append("<message>").append(cwUtils.NEWL);
        if(prof!=null)
            xml.append(prof.asXML()).append(cwUtils.NEWL);
        xml.append("<title>").append(title).append("</title>").append(cwUtils.NEWL);
        xml.append("<body>").append(cwUtils.NEWL);
        xml.append("<text>").append(cwUtils.esc4XML(msg)).append("</text>").append(cwUtils.NEWL); 
        xml.append("<button url=\"").append(cwUtils.esc4XML(url)).append("\"");
        xml.append(">OK</button>").append(cwUtils.NEWL);
        xml.append("</body>").append(cwUtils.NEWL);
        xml.append("<new_sco_mod_id>").append(mod_id).append("</new_sco_mod_id>");
        xml.append("<is_inner>").append(is_inner).append("</is_inner>");
        xml.append("</message>").append(cwUtils.NEWL);

        String xsl_root = (prof == null) ? null : prof.xsl_root;

        static_env.procXSLFile(xml.toString(), static_env.INI_XSL_MSGBOX, out, xsl_root);
    }
    /**
     * Generate a wizBank dialog box with given message
     */
    protected void genMsgBox(String title, String msg, String url, PrintWriter out, loginProfile prof, Connection con) throws IOException, cwException  {
            StringBuffer xml = new StringBuffer();
            
            xml.append(cwUtils.xmlHeader).append(cwUtils.NEWL);
            xml.append("<message>").append(cwUtils.NEWL);
            if(prof!=null)
                xml.append(prof.asXML()).append(cwUtils.NEWL);
            xml.append("<title>").append(title).append("</title>").append(cwUtils.NEWL);
            xml.append("<body>").append(cwUtils.NEWL);
            xml.append("<text>").append(cwUtils.esc4XML(msg)).append("</text>").append(cwUtils.NEWL); 
            xml.append("<button url=\"").append(cwUtils.esc4XML(url)).append("\"");
            xml.append(">OK</button>").append(cwUtils.NEWL);
            xml.append("</body>").append(cwUtils.NEWL);
            xml.append("</message>").append(cwUtils.NEWL);

            String xsl_root = (prof == null) ? null : prof.xsl_root;

            static_env.procXSLFile(xml.toString(), static_env.INI_XSL_MSGBOX, out, xsl_root);
    }

    protected void msgBox(String title, cwSysMessage e, String url, PrintWriter out, long cos_id, long mod_id, loginProfile prof, Connection con, String action) throws IOException, cwException, SQLException, qdbException, cwSysMessage  {
        String encoding;
        if(prof == null)
            encoding = static_env.ENCODING;
        else
            encoding = prof.label_lan;
            
        String msg = e.getSystemMessage(encoding);
        if(msg==null || msg.equals("") || msg.startsWith("!!!")){
            msg = e.getId();
        }
        String modXML = new String();
        dbCourse dbcos = new dbCourse();
        dbcos.cos_res_id = cos_id;
        dbcos.get(con);
        dbModule mod = new dbModule();
        mod.mod_res_id = mod_id;
        mod.get(con);
        modXML = "<course id=\"" + cos_id + "\" timestamp=\"" + dbcos.res_upd_date + "\"/>" + dbUtils.NEWL;
        modXML += "<module id=\"" + mod_id + "\" subtype=\"" + mod.mod_type ;
        modXML += "\" action=\"" + action + "\">" ;
        modXML += dbUtils.esc4XML(mod.res_title) + "</module>" + dbUtils.NEWL;
        
        if (mod_id > 0){
            genMsgBox(title, msg, url, out, mod_id,  prof, con, modXML);
        }else{
            genMsgBox(title, msg, url, out, prof, con);
        }
    }
    
    protected void genMsgBox(String title, String msg, String url, PrintWriter out, long mod_id, loginProfile prof, Connection con, String modXML) throws IOException, cwException  {
        StringBuffer xml = new StringBuffer();
        
        xml.append(cwUtils.xmlHeader).append(cwUtils.NEWL);
        xml.append("<message>").append(cwUtils.NEWL);
        xml.append(modXML);
        if(prof!=null)
            xml.append(prof.asXML()).append(cwUtils.NEWL);
        xml.append("<title>").append(title).append("</title>").append(cwUtils.NEWL);
        xml.append("<body>").append(cwUtils.NEWL);
        xml.append("<text>").append(cwUtils.esc4XML(msg)).append("</text>").append(cwUtils.NEWL); 
        xml.append("<button url=\"").append(cwUtils.esc4XML(url)).append("\"");
        xml.append(">OK</button>").append(cwUtils.NEWL);
        xml.append("</body>").append(cwUtils.NEWL);
        xml.append("<new_sco_mod_id>").append(mod_id).append("</new_sco_mod_id>");
        xml.append("</message>").append(cwUtils.NEWL);

        String xsl_root = (prof == null) ? null : prof.xsl_root;

        static_env.procXSLFile(xml.toString(), static_env.INI_XSL_MSGBOX, out, xsl_root);
    }
    
    /**
    Include a module specific open-end tag and user profile xml with the input data XML
    @param in input data XML
    @param moduleName start, end root tag (e.g. "home_page")
    @return an XML contain <cur_usr> and the input data XML
     * @throws SQLException 
    */
    protected String formatXML(String in, String moduleName) throws IOException, SQLException{
        StringBuffer outBuf = new StringBuffer(2500);

        outBuf.append(dbUtils.xmlHeader).append(dbUtils.NEWL);    
        outBuf.append("<").append(moduleName).append(">").append(dbUtils.NEWL);
        outBuf.append("<meta>").append(dbUtils.NEWL);
        if(prof!=null)
            outBuf.append(prof.asXML()).append(dbUtils.NEWL);
        outBuf.append("<tc_enabled>").append(wizbini.cfgTcEnabled).append("</tc_enabled>");
        outBuf.append("</meta>").append(dbUtils.NEWL);
        outBuf.append(in).append(dbUtils.NEWL);
        outBuf.append("</").append(moduleName).append(">");
        
        String out = new String(outBuf);
        return out;
    }

    /**
    Include a module specific open-end tag and user profile xml with the input data XML
    @param dataXML input data XML
    @param metaXML meta data XML
    @param moduleName start, end root tag (e.g. "home_page")
    @param stylesheet find the corresponding xml labal attach to the meta
    @return an XML contain <cur_usr> and the input data XML
    */
    protected String formatXML(String dataXML, String metaXML, String moduleName) throws IOException, SQLException{
        return formatXML(dataXML, metaXML, moduleName, null);
    }
    protected String formatXML(String dataXML, String metaXML, String moduleName, String stylesheet) throws IOException, SQLException{
        StringBuffer outBuf = new StringBuffer(2500);

        outBuf.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
        outBuf.append("<").append(moduleName).append(">").append(dbUtils.NEWL);
        outBuf.append("<meta>").append(dbUtils.NEWL);
        if(prof!=null)
            outBuf.append(prof.asXML()).append(dbUtils.NEWL);
        if(metaXML!=null)
            outBuf.append(metaXML).append(dbUtils.NEWL);
        if( stylesheet != null && stylesheet.length() > 0 )
          outBuf.append(cwXMLLabel.get(stylesheet, prof.xsl_root, prof.label_lan)).append(dbUtils.NEWL);            
        outBuf.append("<tc_enabled>").append(wizbini.cfgTcEnabled).append("</tc_enabled>");
        outBuf.append("<tc_independent>").append(wizbini.cfgSysSetupadv.isTcIndependent() && ViewTrainingCenter.hasManageTcInd(con, prof.usr_ent_id)).append("</tc_independent>");
        outBuf.append("</meta>").append(dbUtils.NEWL);
        outBuf.append(dataXML).append(dbUtils.NEWL);
        outBuf.append("</").append(moduleName).append(">");
        
        String out = outBuf.toString();
        return out;
    }

    /**
    Include a module specific open-end tag and user profile xml
    */
    protected void displayMessage(String message, PrintWriter out)
        throws cwSysMessage {

        if (static_env.DEBUG) {
            out.println(message);
        }else {
            throw new cwSysMessage("GEN000");
        }
    } 
    
    /*
    To get the new filename of the uploaded file
    @param mediaFile orginal filename of the uploaded file
    @return system generated filename of the uploaded file
    */
    protected String getNewFilename(String mediaFile, HttpSession sess) {
        if (sess == null) {
            return mediaFile;
        }
        
        Vector original_filename = (Vector)sess.getAttribute(SESS_UPLOAD_ORIGINAL_FILENAME);
/*
if (original_filename != null) {
    for (int i=0;i<original_filename.size();i++) {
        System.out.println("Orginal " + i + " > " + (String) original_filename.elementAt(i));
    }        
}else {
    System.out.println("Original file is null.");            
}
*/
        Vector new_filename = (Vector)sess.getAttribute(SESS_UPLOAD_NEW_FILENAME);
/*
if (new_filename != null ) {
    for (int j=0;j<new_filename.size();j++) {
        System.out.println("NEW " + j + " > " + (String) original_filename.elementAt(j));
    }        
}else {
    System.out.println("NEW file is null.");            
}        
*/
        if (original_filename == null || new_filename == null) {
            return mediaFile;
        }
        
        int filenameIndex = original_filename.lastIndexOf(mediaFile);
           if(filenameIndex != -1)
               return (String)new_filename.elementAt(filenameIndex);        
        return mediaFile;
    }
    
	/**
	 * 根据keys获取session
	 * @param sess HttpSession.
	 * @param keys The session keys need to add to output json string. The keys need to be a <code>String</code>.
	 * @return
	 */
    protected HashMap getSession(HttpSession sess, ArrayList keys) {
		HashMap map = new HashMap();
		for (int i = 0; i < keys.size(); i++) {
			map.put(keys.get(i), sess.getAttribute((String) keys.get(i)));
		}
		return map;
	}
    /**
     * 获取meta信息
     *Get Meta 
     */
    protected void setMetaMap() {
    	MetaMap=new HashMap();
    	
		if(prof!=null){
			MetaMap.put("ent_id", new Long(prof.usr_ent_id));
			MetaMap.put("id", prof.usr_ste_usr_id);
			MetaMap.put("display_bil", prof.usr_display_bil);
			MetaMap.put("usr_id", prof.usr_id);
			MetaMap.put("usr_ste_usr_id", prof.usr_ste_usr_id);
			MetaMap.put("curLan",prof.cur_lan);
			MetaMap.put("label", prof.label_lan);
//			MetaMap.put("skin", "slate");
			MetaMap.put("encoding", prof.encoding);
			UserManagement usr=(UserManagement)wizbini.cfgOrgUserManagement.get(this.prof.root_id);
			String defaultUserPhoto=wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getName()+"/"+usr.getUserProfile().getProfileAttributes().getExtension43().getValue();
			MetaMap.put(DEAFAULT_ICON, defaultUserPhoto);
			//map.put("style", "slate");
			MetaMap.put("role_url_home", prof.role_url_home);
			MetaMap.put("role", prof.current_role); 
			//add function list
			MetaMap.put("ftn_lst", JSONArray.fromObject(prof.usr_ftn_lst));
			MetaMap.put("sso_login", new Boolean(prof.sso_login));
			MetaMap.put("root_ent_id", new Long(prof.root_ent_id));
			MetaMap.put("skin", getCurSkin());
			MetaMap.put("cur_skin", prof.current_role_skin_root);
			MetaMap.put("sys_user_info", dbRegUser.getSysUserInfo(con, prof.root_ent_id));
			long tcr_id = 0;
			try {
				Course cos = new Course();
				Vector tcrVec = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
				tcr_id = ((TCBean)tcrVec.elementAt(0)).getTcr_id();
			} catch (Exception e) {
				CommonLog.error("get user's tc failed");
			}
			MetaMap.put("tcr_id", new Long(tcr_id));
			MetaMap.put("has_staff", new Boolean(prof.hasStaff));
		}
	}
    /**
     * 获取当前skin的json
     */
    private Hashtable getCurSkin() {
		Hashtable ht = new Hashtable();
		Personalization pl = (Personalization) wizbini.cfgOrgPersonalization.get(prof.root_id);
		SkinListType st = pl.getSkinList();
		if (st.isLangSelectable()) {
			Iterator skin = st.getSkin().iterator();
			while (skin.hasNext()) {
				SkinTypeImpl sti = (SkinTypeImpl) skin.next();
				if (sti.getId() == prof.current_role_skin_root) {
					ht.put("lang_list", ((LangListType) sti.getLangList()).getLang());
					break;
				}
			}
		} else {
			ht = null;
		}
		return ht;
	}

    public void setParam(BaseParam param) {
    	this.param = param;
    }

	public BaseParam getParam() {
		return param;
	}
	
	public void dispatchDirection() throws cwException, IOException, SQLException{
		if(resultJson !=null && resultJson.size() > 0) {
			if(hasMetaAndSkin){
				resultJson.put("meta", MetaMap);			
			}
			JsonHelper.writeJson(resultJson, defJsonConfig, out);
		} else if(redirectUrl != null && redirectUrl.length() > 0) {
			if(is_ajax_request){
				cwUtils.sendRedirect(response, redirectUrl);
			} else {
				 if(!acSite.getOpenRedirect(wizbini.cfgSysSetupadv.getOpenRedirectWhiteList().getRedirect(),redirectUrl)){
                	response.sendRedirect(redirectUrl);
                }else{
                	out.print(acSite.DOMAIN_FORBIDDEN);
                }
			}
		} else if(resultXml != null && resultXml.length() > 0) {
			if (param.getCmd().toLowerCase().endsWith("_xml")) {
				out.println(resultXml);
			} else {
				generalAsHtml(resultXml, out, param.getStylesheet());
			}
		} else if(sysMsg != null) {
			if( sysMsg.getStatus()!= null && (MSG_ERROR.equals(sysMsg.getStatus()))){
				msgBox(sysMsg.getStatus(), sysMsg, param.getUrl_failure(), out);
			} else {
				msgBox(MSG_STATUS, sysMsg, param.getUrl_success(), out);
			}
		}
	}
	
	public ErrorMsg getErrorMsg(String label, String url){
		
		cwSysMessage msg = new cwSysMessage(label);
		ErrorMsg error = new ErrorMsg(ServletModule.MSG_STATUS, "", "", url,msg.getSystemMessage(encoding));
		return error;
		
	}
	
	public ErrorMsg getErrorMsg(String label, String data, String url){
		
		cwSysMessage msg = new cwSysMessage(label, data);
		ErrorMsg error = new ErrorMsg(ServletModule.MSG_STATUS, "", "", url,msg.getSystemMessage(encoding));
		return error;
		
	}
	
	public ErrorMsg getErrorMsgByStatus(String status, String label, String url){

		cwSysMessage msg = new cwSysMessage(label);
		ErrorMsg error = new ErrorMsg(status, "", "", url,msg.getSystemMessage(encoding));
		return error;
		
	}
	
	public ErrorMsg getErrorMsgByStatus(String status, String label, String data, String url){
		
		cwSysMessage msg = new cwSysMessage(label, data);
		ErrorMsg error = new ErrorMsg(status, "", "", url,msg.getSystemMessage(encoding));
		return error;
		
	}
	
}
