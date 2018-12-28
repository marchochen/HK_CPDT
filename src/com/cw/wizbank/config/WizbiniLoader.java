package com.cw.wizbank.config;

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.cw.wizbank.config.organization.usermanagement.impl.UserManagementImpl;
import com.cw.wizbank.config.system.db.Db;
import com.cw.wizbank.config.system.functionmap.FunctionMap;
import com.cw.wizbank.config.system.scheduledtask.ScheduledTask;
import com.cw.wizbank.config.system.setup.Setup;
import com.cw.wizbank.config.system.setupadv.Setupadv;
import com.cw.wizbank.config.system.skinlist.SkinList;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.ContextPath;
import net.sf.json.JSONArray;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * class for loading configuration files in xml format and
 * act as an entry point to access configuration parameters
 * 2003.05.20 kawai
 */
public class WizbiniLoader {
    // constants to be used in this class only
    
    // indicate whether to validate the input configuration file during unmarshal or not
    private static final boolean b_validateXML = true;
    // schema context for JAXB
    private static final String allSchemaContext = "com.cw.wizbank.config.system.setup"
                                                 + ":com.cw.wizbank.config.system.setupadv"
                                                 + ":com.cw.wizbank.config.system.skinlist"
                                                 + ":com.cw.wizbank.config.system.db"
                                                 + ":com.cw.wizbank.config.organization.personalization"
                                                 + ":com.cw.wizbank.config.organization.recordid"
                                                 + ":com.cw.wizbank.config.organization.usermanagement"
                                                 + ":com.cw.wizbank.config.organization.sso"
                                                 + ":com.cw.wizbank.config.organization.learningplan"
                                                 + ":com.cw.wizbank.config.organization.itemcostmgt"
                                                 + ":com.cw.wizbank.config.organization.exportcols"
                                                 + ":com.cw.wizbank.config.system.scheduledtask"
                                                 + ":com.cw.wizbank.config.system.functionmap"
                                                 ;
    // dirPath*: directory name of file location of configuration files and their schema
    public static final String dirPathCfg = "config";
    public static final String dirPathSys = "system";
    public static final String dirPathOrg = "organization";
    // cfgFile*: filename of configuration files
    public static final String cfgFileSetup = "setup.xml";
    public static final String cfgFileSetupadv = "setupadv.xml";
    public static final String cfgFileSkinList = "skin_list.xml";
    public static final String cfgFileDb = "db.xml";
    public static final String cfgFilePersonalization = "personalization.xml";
    public static final String cfgFileRecordId = "record_id.xml";
    public static final String cfgFileUserManagement = "user_management.xml";
    public static final String cfgFileDelUserManagement = "user_del_management.xml";
    private static final String cfgFileSSO = "sso.xml";
    private static final String cfgFileLearningPlan = "learning_plan.xml";
    public static final String cfgFileScheduledTask = "scheduled_task.xml";
    private static final String cfgFileItemCostMgt = "item_cost_management.xml";
    private static final String cfgFileExportCols = "export_cols.xml";
    private static final String cfgFileFunctionMap = "function_map.xml";
    
    private static final String cfgFileLog4j = "log4j.properties";
    private static final String cfgFileDeveloper = "developer.properties";
    
//    private static final String user_managment_xml="test.xml";
    
   
    // constants to be used for other classes
    
    // servlet context parameter name of application root
    public static final String SCXT_APPN_ROOT = "com.cw.wizbank.config.applicationRoot";
    // servlet context parameter name of document root
    public static final String SCXT_DOC_ROOT = "com.cw.wizbank.config.documentRoot";
    // servlet context parameter name of static env. object
    public static final String SCXT_STATIC_ENV = "com.cw.wizbank.config.staticEnv";
    public static final String SCXT_WIZBINI = "com.cw.wizbank.config.wizbini";
    
    // the singleton
    private static WizbiniLoader wizbini;
    
    
    public static String skinJsonStr=null;
    
    // parameter to be initialized in the constructor
    
    // application root - used to locate configuration files
    private String appnRoot;
    // web document root - used to locate www files
    private String webDocRoot;
    // WEB-INF
    private String webInfRoot;
    private String webConfigRoot;
    private String fontRoot;
    
    // derived parameter to be accessed by accessor method only
    private String xslCacheFileAbs;
    private String fileUploadResDirAbs;
    private String fileCertImgDirAbs;
    private String fileUploadSupplierDirAbs;
    
    private String fileUploadItmDirAbs;
    private String fileUploadMsgDirAbs;
    private String fileEditorDirAbs;
    private String filePosterDirAbs;
    
    public String getFileUploadUsrDirAbs() {
		return fileUploadUsrDirAbs;
	}
	public void setFileUploadUsrDirAbs(String fileUploadUsrDirAbs) {
		this.fileUploadUsrDirAbs = fileUploadUsrDirAbs;
	}
	public void setFileUploadItmDirAbs(String fileUploadItmDirAbs) {
		this.fileUploadItmDirAbs = fileUploadItmDirAbs;
	}
	public void setFileUploadItmMsgAbs(String fileUploadMsgDirAbs) {
		this.fileUploadMsgDirAbs = fileUploadMsgDirAbs;
	}

	private String fileUploadUsrDirAbs;
    private String fileUploadSgpResDisAbs;
    private String fileUploadFacDirAbs;
    private String fileUploadObjDirAbs;
    private String fileUploadTmpDirAbs;
    private String fileUploadAnnDirAbs;
    private String fileUploadArticleDirAbs;
    private String fileUploadGroupDirAbs;
    private String fileUploadAttachmentDirAbs;
    private String fileUploadPositionDirAbs;
    private String fileUpdXmlDirAbs;
    private String kmSearchDirAbs;
    private String systemLogDirAbs;
    private String wizcaseHomeDirAbs;
    private String wizcaseTempDirAbs;
    private String fileUploadLiveDirAbs;
    
    
    //image size 
    private int itmImageWidth;
    private int itmImageHeight;
    private int usrImageWidth;
    private int usrImageHeight;
   
  

    // cfg*: accessor object for corresponding configuration file
    public Setup cfgSysSetup;
    public Setupadv cfgSysSetupadv;
    public SkinList cfgSysSkinList;
    public Db cfgSysDb;
    public ScheduledTask cfgSysScheduledTask;
    public Hashtable cfgOrgPersonalization;
    public Hashtable cfgOrgRecordId;
    public Hashtable cfgOrgUserManagement;
    public Hashtable cfgOrgSSO;
    public Hashtable cfgOrgLearningPlan;
    public Hashtable cfgOrgItemCostMgt;
    public Hashtable cfgOrgExportCols;
    public String cfgFileLog4jDir;
    public String cfgFileDeveloperDir;
    public Hashtable cfgOrgUserManagementJson;
    public FunctionMap cfgSysFunctionMap;
    
    // filter words for know
    public Vector filterWordsVec;
    
    // the vote duration(days) after question selected right answer
    public int zdVoteDuration;
    
    // tc grade management enabled
    public boolean cfgTcEnabled;
    
    public OpenOfficeConnection openOfficeConnection;
    
    public boolean show_login_header_ind;
    public boolean show_all_footer_ind;
    
    // accessor method for derived parameters
    public String getAppnRoot() {
        return this.appnRoot;
    }
    public String getWebDocRoot() {
    	return this.webDocRoot;
    }
    public String getWebInfRoot() {
    	return this.webInfRoot;
    }
    public String getWebConfigRoot() {
        return this.webConfigRoot;
    }
    public String getXslCacheFileAbs() {
        return this.xslCacheFileAbs;
    }
    public String getFileUploadResDirAbs() {
        return this.fileUploadResDirAbs;
    }
    
    public String getFileCertImgDirAbs() {
        return this.fileCertImgDirAbs;
    }
    public String getFileUploadItmDirAbs() {
        return this.fileUploadItmDirAbs;
    }
    public String getFileUploadMsgDirAbs() {
    	return this.fileUploadMsgDirAbs;
    }
    public String getFileUploadSupplierDirAbs() {
		return fileUploadSupplierDirAbs;
	}
	public void setFileUploadSupplierDirAbs(String fileUploadSupplierDirAbs) {
		this.fileUploadSupplierDirAbs = fileUploadSupplierDirAbs;
	}
	public String getFileUploadSgpResDisAbs(){
    	return this.fileUploadSgpResDisAbs;
    }
    public String getFileEditorDirAbs() {
		return this.fileEditorDirAbs;
	}
    public String getFilePosterDirAbs(){
    	return this.filePosterDirAbs;
    }
    public String getFileUploadFacDirAbs() {
        return this.fileUploadFacDirAbs;
    }
    public String getFileUploadObjDirAbs() {
        return this.fileUploadObjDirAbs;
    }
    public String getFileUploadTmpDirAbs() {
        return this.fileUploadTmpDirAbs;
    }
    public String getKmSearchDirAbs(){
    	return this.kmSearchDirAbs;
    }
    public String getSystemLogDirAbs(){
    	return this.systemLogDirAbs;
    }
    public String getWizcaseHomeDirAbs(){
    	return this.wizcaseHomeDirAbs;
    }
    public String getWizcaseTempAbs(){
    	return this.wizcaseTempDirAbs;
    }
    public String getCfgFileLog4jDir(){
        return this.cfgFileLog4jDir;
    }
    public String getCfgFileDeveloperDir(){
        return this.cfgFileDeveloperDir;
    }  
    public String getFileUpdXmlDirAbs(){
        return this.fileUpdXmlDirAbs;
    }
	public String getFileUploadAnnDirAbs() {
		return fileUploadAnnDirAbs;
	}
	
	public String getFileUploadArticleDirAbs() {
		return fileUploadArticleDirAbs;
	}
	
	
	public int getItmImageHeight() {
		return itmImageHeight;
	}
	public int getItmImageWidth() {
		return itmImageWidth;
	}
	public int getUsrImageHeight() {
		return usrImageHeight;
	}
	public int getUsrImageWidth() {
		return usrImageWidth;
	}
	public String getFileUploadGroupDirAbs() {
		return fileUploadGroupDirAbs;
	}
	public String getFileUploadAttachmentDirAbs() {
		return fileUploadAttachmentDirAbs;
	}
	public void setFileUploadGroupDirAbs(String fileUploadGroupDirAbs) {
		this.fileUploadGroupDirAbs = fileUploadGroupDirAbs;
	}
	
	public String getFileUploadLiveDirAbs() {
		return fileUploadLiveDirAbs;
	}
	
	public void setFileUploadLiveDirAbs(String fileUploadLiveDirAbs) {
		this.fileUploadLiveDirAbs = fileUploadLiveDirAbs;
	}
	
	
	public String getFileUploadPositionDirAbs() {
		return fileUploadPositionDirAbs;
	}
	public void setFileUploadPositionDirAbs(String fileUploadPositionDirAbs) {
		this.fileUploadPositionDirAbs = fileUploadPositionDirAbs;
	}

	private int newestAnnDuration;
	public int getNewestAnnDuration() {
		return newestAnnDuration;
	}
	
	/**
     * constructor for batch, DO NOT receive doc root
     * read config files and DO NOT INIT path related to doc root
     */
    public WizbiniLoader(String inAppnRoot) throws cwException {
    	this(inAppnRoot, null); 
	}

    /**
     * constructor to read in all known configuration files
     * and initialize to accessor objects
     */
	public WizbiniLoader(String inAppnRoot, String inWebDocRoot) throws cwException {
		// remove any leading and trailing spaces and trailing path separator
		inAppnRoot = inAppnRoot.trim();
		if (inAppnRoot.endsWith(cwUtils.SLASH)) {
			this.appnRoot = inAppnRoot.substring(0, inAppnRoot.length() - 1);
		} else {
			this.appnRoot = inAppnRoot;
		}
		if (inWebDocRoot != null) {
			inWebDocRoot = inWebDocRoot.trim();
			if (inWebDocRoot.endsWith(cwUtils.SLASH)) {
				this.webDocRoot = inWebDocRoot.substring(0, inWebDocRoot.length() - 1);
			} else {
				this.webDocRoot = inWebDocRoot;
			}
		}
		reload();
	}
    
	public static WizbiniLoader getInstance() {
    	return wizbini;
    }
	
    /**
     * return the single instance of this class
     * if an instance is not created yet, it will be created from the ServletConfig
     */
    public static WizbiniLoader getInstance(ServletConfig sConfig) throws cwException {
    	return getInstance(sConfig.getServletContext());
    }
    public static WizbiniLoader getInstance(FilterConfig fConfig) throws cwException {
    	return getInstance(fConfig.getServletContext());
    }
    public static WizbiniLoader getInstance(ServletContext context) throws cwException {
        // initialize the wizbini loader of configuration files
        if (wizbini == null) {
            String docRoot = context.getInitParameter(WizbiniLoader.SCXT_DOC_ROOT);
            CommonLog.info(WizbiniLoader.SCXT_DOC_ROOT + "=" + docRoot);
            if (docRoot == null || docRoot.length() == 0) {
                docRoot = context.getRealPath("/");
                CommonLog.info("config.getServletContext().getRealPath(\"/\")=" + docRoot);
                if (docRoot == null || docRoot.length() == 0) {
                    throw new cwException("Document root not found.");
                }
            }
                
            String appnRoot = context.getInitParameter(WizbiniLoader.SCXT_APPN_ROOT);
            CommonLog.info(WizbiniLoader.SCXT_APPN_ROOT + "=" + appnRoot);
            if (appnRoot == null || appnRoot.length() == 0) {
                appnRoot = docRoot;
                if (appnRoot == null || appnRoot.length() == 0) {
                    throw new cwException("Application root not found.");
                }
                CommonLog.info("documentRoot.getParent()=" + appnRoot);
            }
            
            wizbini = new WizbiniLoader(appnRoot, docRoot);
        }
        
        return wizbini;
    }
    
    public void reload() throws cwException {
        // override application server jaxp properties (special handling for weblogic 6.0)
        if (System.getProperty("javax.xml.parsers.SAXParserFactory") != null) {
            System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        }
        if (System.getProperty("javax.xml.parsers.DocumentBuilderFactory") != null) {
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        }
        
		// set up directory of configuration files
		webInfRoot = webDocRoot + cwUtils.SLASH + "WEB-INF";
		webConfigRoot = webDocRoot + cwUtils.SLASH + "WEB-INF" + cwUtils.SLASH + dirPathCfg;
		File docRootCfg = new File(webInfRoot, dirPathCfg);
		File docRootSys = new File(docRootCfg, dirPathSys);
		File docRootOrg = new File(docRootCfg, dirPathOrg);

		// mark the current configuration file which is being processed
		String curCfgFile = null;
        
        try {
            // standard work for initializing JAXB
            JAXBContext jc = JAXBContext.newInstance(allSchemaContext, this.getClass().getClassLoader());
            Unmarshaller unmar = jc.createUnmarshaller();
            unmar.setValidating(b_validateXML);
            
            // system parameters
            curCfgFile = cfgFileSetup;
            this.cfgSysSetup    = (Setup)unmar.unmarshal(new File(docRootSys, cfgFileSetup));
            curCfgFile = cfgFileSetupadv;
            this.cfgSysSetupadv = (Setupadv)unmar.unmarshal(new File(docRootSys, cfgFileSetupadv));
            curCfgFile = cfgFileSkinList;
            this.cfgSysSkinList = (SkinList)unmar.unmarshal(new File(docRootSys, cfgFileSkinList));
            curCfgFile = cfgFileDb;
            this.cfgSysDb = (Db)unmar.unmarshal(new File(docRootSys, cfgFileDb));
            this.cfgSysScheduledTask = (ScheduledTask) unmar.unmarshal(new File(docRootSys, cfgFileScheduledTask));
            
            curCfgFile = cfgFileFunctionMap;
            this.cfgSysFunctionMap = (FunctionMap)unmar.unmarshal(new File(docRootSys, cfgFileFunctionMap));
            // organization parameters
            
            // get a list of all available organization id
            // (should be directory names, ignore non-directory)
            String orgId[] = docRootOrg.list();
            
            if (this.cfgOrgPersonalization == null) {
                this.cfgOrgPersonalization = new Hashtable();
            } else {
                this.cfgOrgPersonalization.clear();
            }
            if (this.cfgOrgRecordId == null) {
                this.cfgOrgRecordId = new Hashtable();
            } else {
                this.cfgOrgRecordId.clear();
            }
            if (this.cfgOrgUserManagement == null) {
                this.cfgOrgUserManagement = new Hashtable();
            } else {
                this.cfgOrgUserManagement.clear();
            }
			if (this.cfgOrgUserManagementJson == null) {
				this.cfgOrgUserManagementJson = new Hashtable();
			} else {
				this.cfgOrgUserManagementJson.clear();
			}
            if (this.cfgOrgSSO == null) {
                this.cfgOrgSSO = new Hashtable();
            } else {
                this.cfgOrgSSO.clear();
            }
            if (this.cfgOrgLearningPlan == null) {
                this.cfgOrgLearningPlan = new Hashtable();
            } else {
                this.cfgOrgLearningPlan.clear();
            }
			if(this.cfgOrgItemCostMgt == null){
						   this.cfgOrgItemCostMgt = new Hashtable();
			}else{
						   this.cfgOrgItemCostMgt.clear();
			}
			if (this.cfgOrgExportCols == null) {
				this.cfgOrgExportCols = new Hashtable();
			} else {
				this.cfgOrgExportCols.clear();
			}

			 // orgDir = null;
            for (int i = 0; i < orgId.length; i++) {
				//skip the directory ".svn"
				//as it is the SVN config folder
            	if(orgId[i].equals(".svn")) {
            		continue;
            	}
            	File orgDir = new File(docRootOrg, orgId[i]);
                if (orgDir.isDirectory()) {
                    curCfgFile = orgId[i] + " " + cfgFilePersonalization;
                    this.cfgOrgPersonalization.put(
                        orgId[i], unmar.unmarshal(
                            new File(orgDir, cfgFilePersonalization)
                        )
                    );
                    curCfgFile = orgId[i] + " " + cfgFileRecordId;
                  //File temp=new File(orgDir, this.cfgFileRecordId);
                    this.cfgOrgRecordId.put(
                        orgId[i], unmar.unmarshal(
                            new File(orgDir, cfgFileRecordId)
                        )
                    );
                    curCfgFile = orgId[i] + " " + cfgFileUserManagement;
                    this.cfgOrgUserManagement.put(
                        orgId[i], unmar.unmarshal(
                            new File(orgDir, cfgFileUserManagement)
                        )
                    );
                    curCfgFile = orgId[i] + " " + cfgFileSSO;
                    this.cfgOrgSSO.put(
                        orgId[i], unmar.unmarshal(
                            new File(orgDir, cfgFileSSO)
                        )
                    );
                    curCfgFile = orgId[i] + " " + cfgFileLearningPlan;
                    this.cfgOrgLearningPlan.put(
                        orgId[i], unmar.unmarshal(
                            new File(orgDir, cfgFileLearningPlan)
                        )
                    );
                    curCfgFile = orgId[i] + " " + cfgFileItemCostMgt;
                    this.cfgOrgItemCostMgt.put(orgId[i], unmar.unmarshal(new File(orgDir, cfgFileItemCostMgt)));
                    
                    curCfgFile = orgId[i] + " " + cfgFileExportCols;
                    this.cfgOrgExportCols.put(orgId[i], unmar.unmarshal(new File(orgDir, cfgFileExportCols)));
                    
                    if (this.webDocRoot !=null){
                    	this.cfgOrgUserManagementJson.put(orgId[i],transUsrMgtXmlToJson(docRootOrg + cwUtils.SLASH + orgId[i] + cwUtils.SLASH + cfgFileUserManagement
                    			,this.webDocRoot + cwUtils.SLASH + cfgSysSetup.getXslStylesheet().getHome() + cwUtils.SLASH + "user_management_tojson.xsl"));
                    }
                }
            }
            // derived parameters - parameters that need to further 
            // "derive" from the value in the configuration file
            StringBuffer tempStr = new StringBuffer();
            tempStr.append(webConfigRoot).append(cwUtils.SLASH).append(cfgSysSetup.getXslStylesheet().getCacheFile());
            this.xslCacheFileAbs = tempStr.toString();
            if (this.webDocRoot !=null){
	            this.fileEditorDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getEditorDir().isRelative()
						 , this.cfgSysSetupadv.getFileUpload().getEditorDir().getName());
	            this.filePosterDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getPosterDir().isRelative()
						 , this.cfgSysSetupadv.getFileUpload().getPosterDir().getName());
	            this.fileUploadResDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getResDir().isRelative()
	                                                ,this.cfgSysSetupadv.getFileUpload().getResDir().getName());
	            this.fileCertImgDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getCertDir().isRelative()
                        ,this.cfgSysSetupadv.getFileUpload().getCertDir().getName());
	            this.fileUploadSgpResDisAbs=deriveDir(this.cfgSysSetupadv.getFileUpload().getSgpResDir().isRelative()
	            									,this.cfgSysSetupadv.getFileUpload().getSgpResDir().getName());      	
	            this.fileUploadItmDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getItmDir().isRelative()
	                                                ,this.cfgSysSetupadv.getFileUpload().getItmDir().getName());
	            this.fileUploadMsgDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getMsgDir().isRelative()
	            									,this.cfgSysSetupadv.getFileUpload().getMsgDir().getName());
	            this.fileUploadUsrDirAbs=deriveDir(this.cfgSysSetupadv.getFileUpload().getUsrDir().isRelative()
	            									,this.cfgSysSetupadv.getFileUpload().getUsrDir().getName());
	            this.fileUploadFacDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getFacDir().isRelative()
	                                                ,this.cfgSysSetupadv.getFileUpload().getFacDir().getName());
	            this.fileUploadObjDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getObjDir().isRelative()
	                                                ,this.cfgSysSetupadv.getFileUpload().getObjDir().getName());
	            this.fileUploadTmpDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getTmpDir().isRelative()
	                                                ,this.cfgSysSetupadv.getFileUpload().getTmpDir().getName());
	            this.fileUploadSupplierDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getSupplierDir().isRelative()
                        							,this.cfgSysSetupadv.getFileUpload().getSupplierDir().getName());
	            this.fileUploadAnnDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getAnnDir().isRelative()
                        							,this.cfgSysSetupadv.getFileUpload().getAnnDir().getName());
	            
	            this.fileUploadArticleDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getArticleDir().isRelative()
						,this.cfgSysSetupadv.getFileUpload().getArticleDir().getName());
	            
				this.kmSearchDirAbs = deriveDir(this.cfgSysSetupadv.getKmIndexingThread().getSearchDbDir().isRelative()
													,this.cfgSysSetupadv.getKmIndexingThread().getSearchDbDir().getName());
				this.systemLogDirAbs = deriveDir(this.cfgSysSetupadv.getLogDir().isRelative()
	   												,this.cfgSysSetupadv.getLogDir().getName());
	   			this.wizcaseHomeDirAbs = deriveDir(this.cfgSysSetupadv.getWizcase().getXmlHomeDir().isRelative()
	   												,this.cfgSysSetupadv.getWizcase().getXmlHomeDir().getName());
	   			this.wizcaseTempDirAbs = deriveDir(this.cfgSysSetupadv.getWizcase().getTempDir().isRelative()
	   												,this.cfgSysSetupadv.getWizcase().getTempDir().getName());
	   			this.fileUploadGroupDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getGroupDir().isRelative()
	   					,this.cfgSysSetupadv.getFileUpload().getGroupDir().getName());
	   			this.fileUploadLiveDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getLiveDir().isRelative()
						,this.cfgSysSetupadv.getFileUpload().getLiveDir().getName());
	   			this.fileUploadPositionDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getPositionDir().isRelative()
	   					,this.cfgSysSetupadv.getFileUpload().getPositionDir().getName());
	            this.fileUploadAttachmentDirAbs = deriveDir(this.cfgSysSetupadv.getFileUpload().getAttachmentDir().isRelative()
						,this.cfgSysSetupadv.getFileUpload().getAttachmentDir().getName());
                this.cfgFileLog4jDir = webConfigRoot + cwUtils.SLASH + cfgFileLog4j;
                this.cfgFileDeveloperDir = webConfigRoot + cwUtils.SLASH + cfgFileDeveloper;
                
                // set filter words for know 
                String filterWordsAbs = this.cfgSysSetupadv.getFilterWordsFile();
                filterWordsAbs =  webConfigRoot + cwUtils.SLASH + filterWordsAbs;
                this.filterWordsVec = WordsFilter.getFilterWordsVec(filterWordsAbs);
                
                this.zdVoteDuration = this.cfgSysSetupadv.getZdVoteDuration();
                
                this.newestAnnDuration = this.cfgSysSetupadv.getNewestDuration();
            }
            this.fileUpdXmlDirAbs = this.cfgSysSetupadv.getUpdxmlFile();
            this.fileUpdXmlDirAbs = webConfigRoot + cwUtils.SLASH + this.fileUpdXmlDirAbs;
            
            this.cfgTcEnabled = this.cfgSysSetupadv.isTcEnabled();
            
            this.itmImageWidth = this.cfgSysSetupadv.getImageSize().getItmImg().getWidth();
            this.itmImageHeight = this.cfgSysSetupadv.getImageSize().getItmImg().getHeight();
            this.usrImageWidth = this.cfgSysSetupadv.getImageSize().getUsrImg().getWidth();
            this.usrImageHeight = this.cfgSysSetupadv.getImageSize().getUsrImg().getHeight();
        } catch (JAXBException e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException("Error in loading wizbini(" + curCfgFile + "):" + e.getMessage() + "<br>" + e.toString());
        }
        
    }
    
    
    /**
     * 		To getTheUserInforTemplate for Cache
     * @throws cwException 
     * 
     */
    private Hashtable transUsrMgtXmlToJson(String xmlTemplateFileName,String XslFilename)throws  cwException
    {

    	Hashtable tab = new Hashtable();
    	File xmlFile = new File(xmlTemplateFileName);
    	BufferedReader reader = null;
		try {
			if (xmlFile.exists()) {
				String line;
				StringBuffer buf = new StringBuffer();
				reader = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(xmlFile), cwUtils.ENC_UTF));
				while ((line = reader.readLine()) != null) {
					buf.append(line);
				}
				List lang_list = new ArrayList();
				lang_list.add(LangLabel.LangCode_en_us);
				lang_list.add(LangLabel.LangCode_zh_cn);
				lang_list.add(LangLabel.LangCode_zh_hk);
				for (int i = 0; i < lang_list.size(); i++) {
					List paramname = new ArrayList();
					List paramvalue = new ArrayList();
					paramname.add("lang");
					paramvalue.add(lang_list.get(i));
					String json = cwXSL.processFromFileByParam(buf.toString(), XslFilename, paramname, paramvalue);
					JSONArray jsonObj = JSONArray.fromObject(json);
					Object obj = JSONArray.toArray(jsonObj);
					if (obj != null) {
						tab.put(lang_list.get(i), obj);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new cwException("Error in transfer [" + cfgFileUserManagement + "] to json:" + ex.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
		return tab;

    }
    /**
     * to reconstruct the object into xml
     */
    public void marshal(java.lang.Object obj, java.io.Writer out) throws cwException {
        try {
            JAXBContext jc = JAXBContext.newInstance(allSchemaContext, this.getClass().getClassLoader());
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(obj, out);
        } catch (JAXBException e) {
            throw new cwException("Error in reconstructing xml from unmarshalled object:" + e.getMessage());
        }
    }
    
    /**
     * derive the absolute directory path
     */
    private String deriveDir(boolean isRelative, String inDirName) {
        String outDirName = null;
        if (isRelative) {
            StringBuffer tempStr = new StringBuffer();
            tempStr.append(this.webDocRoot).append(cwUtils.SLASH).append(inDirName);
            outDirName = tempStr.toString();
        } else {
            outDirName = inDirName;
        }
        return outDirName;
    }
    
    public Object getUsrMgtJson(String root_id, String cur_lang) {
    	return ((Hashtable) cfgOrgUserManagementJson.get(root_id)).get(cur_lang);
    }
    
    public String getUserPhotoPath(loginProfile prof,long usr_ent_id, String fileName){
    	String UserPhotoPath="";
    	String uploadUsrDirAbs =this.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl();
    	//default user photo
    	UserManagementImpl userManagement = (UserManagementImpl) this.cfgOrgUserManagement.get(prof.root_id);
    	String defaultUserPhotoPath = userManagement.getUserProfile().getProfileAttributes().getExtension43().getValue();
		if(fileName != null && !"".equals(fileName)) {
			UserPhotoPath = ContextPath.getContextPath() + "/" + uploadUsrDirAbs +"/" + usr_ent_id + "/" + fileName;
		} else {
			UserPhotoPath = ContextPath.getContextPath() + "/" + uploadUsrDirAbs +"/"+ defaultUserPhotoPath;
		}
		return UserPhotoPath;
    }
    
    public String getFunctionMapXml() throws cwException {
        StringWriter writer = new StringWriter();
        marshal(cfgSysFunctionMap, writer);
        String function_map_xml = writer.toString();
        function_map_xml = function_map_xml.substring(function_map_xml.indexOf("?>") + 3);
        return function_map_xml;
    }
    
	public String getFontRoot() {
		return this.appnRoot+"/WEB-INF/fonts";
	}

}
