package com.cw.wizbank.km;

import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import com.oroinc.text.perl.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbAttachment;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.ae.aeTemplate;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.DbKmNode;
import com.cw.wizbank.db.DbKmObject;
import com.cw.wizbank.db.DbKmBaseObject;
import com.cw.wizbank.db.DbKmFolder;
import com.cw.wizbank.db.DbKmNodeAccess;
import com.cw.wizbank.db.DbKmNodeActnHistory;
import com.cw.wizbank.db.view.ViewKmNodeActn;
import com.cw.wizbank.db.view.ViewKmObject;
import com.cw.wizbank.db.view.ViewKmObjectType;
import com.cw.wizbank.db.view.ViewKmFolderManager;
import com.cw.wizbank.search.IndexFiles;
import com.cw.wizbank.search.Search;
import com.cwn.wizbank.utils.CommonLog;

public class KMObjectManager {


    /**
    Constant to indicate need to find publisgeh version
    */
    private static final String PUBLISHED_VERSION = "PUBLISHED";
    
    /**
    Constant to indicate need to find latest version
    */
    private static final String LATEST_VERSION = "LATEST";


    /**
    Token used to seperate object id, version and filename in index files
    */
    public static final String indexKeyToken = ":_:_:";

    /**
    Initial version of newly created kmObject
    */
    private static final String INITIAL_VERSION = "0.1";
    public static final String INITIAL_PUBLISH_VERSION = "1.0";
    
    
    /**
    static Hashtable constains Vectors of ViewKmObjectType
    with Long objects of site_id as keys
    */
    private static Hashtable hObjectType; 

    /**
    static Hashtable constains XML of objectTypes (as String)
    with Long objects of site_id as keys
    */
    private static Hashtable hObjectTypeXML;

    /**
    static Hashtable constains Hashtable of Vectors of ViewKmObjectType by NATURE
    with Long objects of site_id as keys
    */
    private static Hashtable hObjectTypeByNature; 

    /**
    static Hashtable constains Hashtable of XML of objectTypes (as String) by NATURE
    with Long objects of site_id as keys
    */
    private static Hashtable hObjectTypeByNatureXML;    
    
    /**
    Indicates if there any KMObjectManager loading hObjectType and hObjectTypeXML from database
    */
    private static boolean isLoading = false;
    
    /**
    Indicates how many KMObjectManagers are reading hObjectType and hObjectTypeXML 
    into myObjectTypes and myTypesXML
    */
    private static int readingCount = 0;

    /**
    Time to sleep when loading/reading hObjectType and hObjectTypeXML 
    */
    private static long sleepTime = 1000;

    /**
    Stores objects of ViewKmObjectType of this KMObjectManager's site 
    */
    private Vector vObjectType;
    
    /**
    Stores XML object types of this KMObjectManager's site 
    */
    private String objectTypeXML;

    /**
    Stores objects of ViewKmObjectType of this KMObjectManager's site (the key is the nature)
    */
    private Hashtable myObjectTypeByNature;
    
    /**
    Stores XML object types of this KMObjectManager's site (the key is the nature)
    */
    private Hashtable myObjectTypeByNatureXML;      
    
    /**
    Stores the site id of organization served by this KMObjectManager
    */
    public long owner_ent_id;
    
    /**
    Maximum number of matched objects returned after searching
    */
    protected static final int SEARCH_RESULT_LIMIT = 100;

    /**
    For Sub class
    */
    public String next_version;
    
    /**
    Get object type XML of the organization
    @return XML of all object types
    */
    public String getObjectTypeXML() {
        return this.objectTypeXML;
    }
    
    /**
    Get object type XML of the organization
    @return XML of all object types
    */
    public String getObjectTypeXML(String nature) {
        if( nature != null && nature.length() > 0 ) {
            return (String)myObjectTypeByNatureXML.get(nature);
        } else
            return getObjectTypeXML();
    }
    
    /**
    Constructure a KMObjectManager serving a given organization.
    @param con Connection to database
    @param owner_ent_id site id of organization
    */
    public KMObjectManager(Connection con, long owner_ent_id) throws SQLException {
        loadObjectTypes(con, false);
        loadMyObjectTypes(owner_ent_id);
        next_version = null;
    }
    
    /**
    Load this.vObjectType and this.objectTypeXML from static variable
    hObjectType and hObjectTypeXML
    @param owner_ent_id organization id of site to be served by this KMObjectManager
    */
    private void loadMyObjectTypes(long owner_ent_id) {
        //if someone is loading the static variables 
        //sleep sometime until no one is loading
        while(isLoading) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                
            }
        }
        readingCount++;
        this.owner_ent_id = owner_ent_id;
        Long siteId = new Long(owner_ent_id);
        Vector vTempObjectType = (Vector) hObjectType.get(siteId);
        this.vObjectType = new Vector();
        for(int i=0; i<vTempObjectType.size(); i++) {
            this.vObjectType.addElement(((ViewKmObjectType)vTempObjectType.elementAt(i)).deepClone());
        }
        this.objectTypeXML = new String((String) hObjectTypeXML.get(siteId));

        // deep clone is necessary when RELOAD is enabled in loadObjectTypes!
        this.myObjectTypeByNature = (Hashtable)hObjectTypeByNature.get(siteId);
        this.myObjectTypeByNatureXML = (Hashtable)hObjectTypeByNatureXML.get(siteId);
        readingCount--;
        return;
    }
    
    /**
    Load hObjectType and hObjectTypeXML from database
    @param con Connection to database
    @param reload_ind indicate if need to reload from database even 
           hObjectType and hObjectTypeXML has been loaded before
    */
    public static  void loadObjectTypes(Connection con, boolean reload_ind) throws SQLException {
        
        if(reload_ind || hObjectType == null) {
            //if someone is reading hObjectType and hObjectTypeXML
            //sleep sometime until no one is reading 
            while(readingCount > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    
                }
            }
            isLoading = true;
            //Vector contains all ObjectType in database
            Vector vViewObjectType = ViewKmObjectType.getAllObjectTypes(con);
            //Temporary Hashtable to store objectTypes
            Hashtable tempObjectType = new Hashtable();
            Hashtable tempObjectTypeXML = new Hashtable();
            //put the Object Types into Vector and temporary Hashtable
            for (int i=0; i<vViewObjectType.size(); i++) {
                ViewKmObjectType viewObjectType = (ViewKmObjectType) vViewObjectType.elementAt(i);
                Long siteId = new Long(viewObjectType.dbObjectType.oty_owner_ent_id);
                Vector v = (Vector) tempObjectType.get(siteId);
                if(v == null) {
                    v = new Vector();
                }
                v.addElement(viewObjectType);
                tempObjectType.put(siteId, v);
            }

            Enumeration eSiteId = null;
            
            //convert the each organization's Object Types into XML 
            //and store into Hashtable
            eSiteId = tempObjectType.keys();
            while(eSiteId.hasMoreElements()) {
                Long siteId = (Long) eSiteId.nextElement();
                Vector v = (Vector) tempObjectType.get(siteId);
                StringBuffer xmlBuf = new StringBuffer(256);
                xmlBuf.append("<object_type_list>");
                for(int i=0; i<v.size(); i++) {
                    xmlBuf.append("<object_type>")
                        .append(((ViewKmObjectType)v.elementAt(i)).dbObjectType.oty_code)
                        .append("</object_type>");
                }
                xmlBuf.append("</object_type_list>");
                tempObjectTypeXML.put(siteId, xmlBuf.toString());
            }
                
            //Set the temporary Hashtable to static Class variables
            hObjectType = tempObjectType;
            hObjectTypeXML = tempObjectTypeXML;

            Hashtable tempObjectTypeByNature = new Hashtable();
            eSiteId = tempObjectType.keys();
            while(eSiteId.hasMoreElements()) {
                Long siteId = (Long) eSiteId.nextElement();
                Hashtable hash = new Hashtable();
                Vector v = (Vector) tempObjectType.get(siteId);                
                for (int i=0; i<v.size(); i++) {
                    ViewKmObjectType objType = (ViewKmObjectType)v.elementAt(i);
                    String nature = objType.dbObjectType.oty_nature;

                    if (nature != null) {
                        Vector natureHash = (Vector)hash.get(nature);                    

                        if (natureHash == null) {
                            natureHash = new Vector();
                        }
                        
                        natureHash.addElement(objType);
                        hash.put(nature, natureHash);
                    }
                }
                
                tempObjectTypeByNature.put(siteId, hash);
            }

            Hashtable tempObjectTypeByNatureXML = new Hashtable();
            eSiteId = tempObjectTypeByNature.keys();
            while(eSiteId.hasMoreElements()) {
                Long siteId = (Long) eSiteId.nextElement();
                Hashtable hash = (Hashtable)tempObjectTypeByNature.get(siteId);

                Enumeration eNature = hash.keys();
                Hashtable tempHashByNature = new Hashtable();
                while(eNature.hasMoreElements()) {
                    String nature = (String)eNature.nextElement();                
                    Vector v = (Vector) hash.get(nature);
                    StringBuffer xmlBuf = new StringBuffer(256);
                    xmlBuf.append("<object_type_list>");
                    for(int i=0; i<v.size(); i++) {
                        xmlBuf.append("<object_type>")
                            .append(((ViewKmObjectType)v.elementAt(i)).dbObjectType.oty_code)
                            .append("</object_type>");
                    }
                    xmlBuf.append("</object_type_list>");
                    tempHashByNature.put(nature, xmlBuf.toString());
                }

                tempObjectTypeByNatureXML.put(siteId, tempHashByNature);
            }
            
            hObjectTypeByNature = tempObjectTypeByNature;
            hObjectTypeByNatureXML = tempObjectTypeByNatureXML;
            
            isLoading = false;
        }
        return;
    }

    public String getPublishXML(Connection con, long objId) throws SQLException, cwSysMessage {
        
        //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }
        ViewKmObject object = new ViewKmObject();
        object.getBasic(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }

        //check if the object is available for published
        if(!object.dbObject.obj_status.equals(DbKmObject.OBJ_STATUS_CHECKED_IN)) {
            throw new cwSysMessage(KMModule.SMSG_KM_ERR_CHECKED_OUT);
        }
        
        //get ancestor node list
        String ancestorXML = ViewKmFolderManager.getFolderAncestorAsXML(con, object.dbObject.obj_bob_nod_id);
        
        Vector vDomain = object.getParentDomain(con);
        StringBuffer xmlBuf = new StringBuffer(256);
        
        //get next published version
        xmlBuf.append("<next_version>")
              .append(getNextPublishedVersion(object.dbObject.obj_version))
              .append("</next_version>");
        
        //get domain node list
        xmlBuf.append("<domain_node_list>");
        for(int i=0; i<vDomain.size(); i++) {
            DbKmFolder folder = (DbKmFolder)vDomain.elementAt(i);
            xmlBuf.append("<node id=\"").append(folder.fld_nod_id).append("\">")
                  .append("<title>").append(cwUtils.esc4XML(folder.fld_title)).append("</title>")
                  .append("</node>");

        }
        xmlBuf.append("</domain_node_list>");
        
        return getObjectNodeAsXML(object.dbObject, ancestorXML, xmlBuf.toString());
    }
    
    /**
    Get XML for preparing insertion of kmObject
    @param con Connection to database
    @param env qdbEnv of wizBank
    @param oty_code ObjectType code
    @param tvw_id TemplateView id
    @param parent_nod_id parent WorkingFolder node id
    @return XML as String
    */
    public String getInsertXML(Connection con, qdbEnv env, String oty_code, 
                               String tvw_id, long parent_nod_id, String nature)  
                               throws SQLException, cwException {
        if (nature != null && nature.length() > 0) {
            this.vObjectType = (Vector)myObjectTypeByNature.get(nature);
            this.objectTypeXML = (String)this.myObjectTypeByNatureXML.get(nature);
        }

/*        if (nature == null || nature.length() == 0) {
            tplBuf.append(this.objectTypeXML);
        } else {
            if (this.myObjectTypeByNatureXML != null) {
                tplBuf.append(this.myObjectTypeByNatureXML.get(nature));
            }
        }*/

        //get the 1st object type code if not given
        if(oty_code == null || oty_code.length() == 0) {
            oty_code = ((ViewKmObjectType)vObjectType.elementAt(0)).dbObjectType.oty_code;
        }
                
        //get ancestor node list
        String ancestorXML = ViewKmFolderManager.getVirtualObjectAncestorAsXML(con, parent_nod_id);
        
        //get valued template
        String valuedTemplate = getValuedTemplate(con, env, null, oty_code, tvw_id);
        
        return getObjectNodeAsXML(null, ancestorXML, valuedTemplate);
    }


    /**
    Get XML for Valued Template
    @param con Connection to database
    @param object object to be formatted into valued template
    @param env qdbEnv of wizBank
    @param oty_code ObjectType code
    @param tvw_id TemplateView id
    @return XML as String
    */
    private String getValuedTemplate(Connection con, qdbEnv env, 
                                     ViewKmObject object, 
                                     String oty_code, String tvw_id) 
                                     throws SQLException, cwException {
        StringBuffer tplBuf = new StringBuffer(1024);
        DbKmObject dbObject = null;
        if(object!=null) {
            dbObject = object.dbObject;
        }

        //get object type template
        aeTemplate tpl = getObjectTypeTemplate(oty_code, aeTemplate.ITEM);
        //get template view
        DbTemplateView dbTplVi = new DbTemplateView();
        dbTplVi.tvw_tpl_id = tpl.tpl_id;
        dbTplVi.tvw_id = tvw_id;
        dbTplVi.get(con);
        
        tplBuf.append("<applyeasy>");
        //template view xml
        tplBuf.append(dbTplVi.tvw_xml);
        //template xml
        tplBuf.append(tpl.tpl_xml);
        //object xml
        if(dbObject != null) {
            if(oty_code != null && !dbObject.obj_type.equals(oty_code)) {
                Hashtable data = new Hashtable();
                data.put("obj_type",oty_code);
                dbObject.obj_xml = updateObjXML(dbObject.obj_xml, tpl, data);
            }
            tplBuf.append(dbObject.obj_xml);
        }
        //external info
        if(dbObject != null) {
            tplBuf.append("<km_create_datetime>")
	              .append("<datetime value=\"").append(dbObject.nod_create_timestamp).append("\"/>")
                  .append("</km_create_datetime>");

            tplBuf.append("<km_last_update_datetime>")
	              .append("<datetime value=\"").append(dbObject.obj_update_timestamp).append("\"/>")
                  .append("</km_last_update_datetime>");

            tplBuf.append("<km_version>")
	              .append("<version value=\"").append(dbObject.obj_version).append("\"/>")
                  .append("</km_version>");

            if (next_version == null) {
                next_version = getNextVersion(dbObject.obj_version);
            }

            tplBuf.append("<km_next_version>")
	              .append("<version value=\"").append(next_version).append("\"/>")
                  .append("</km_next_version>");
            
            tplBuf.append("<km_object_status>")
	              .append("<status value=\"").append(dbObject.obj_status).append("\"/>")
                  .append("</km_object_status>");

            if(object.updateUser != null) {
                tplBuf.append("<km_update_user>")
	                .append("<status value=\"").append(object.updateUser.usr_display_bil).append("\"/>")
                    .append("</km_update_user>");
            }
            
            if(dbTplVi.tvw_filesize_ind && object.vAttachment != null) {
                tplBuf.append("<filesize>");
                for(int i=0; i<object.vAttachment.size(); i++) {
                    String filename = ((dbAttachment)object.vAttachment.elementAt(i)).att_filename;
                    String filepath = env.INI_OBJ_DIR_UPLOAD + cwUtils.SLASH + 
                                    dbObject.obj_bob_nod_id + cwUtils.SLASH + 
                                    dbObject.obj_version + cwUtils.SLASH + 
                                    filename;
                    File file = new File(filepath);
                    long filelength = Math.round(file.length()/1024f);
                    if(filelength == 0) {
                        filelength = 1;
                    }
                    tplBuf.append("<file name=\"").append(cwUtils.esc4XML(filename)).append("\"")
                          .append(" size=\"").append(filelength + " KB").append("\"/>");
                }                
                tplBuf.append("</filesize>");
            }

            if(dbTplVi.tvw_km_published_version_ind) {
                String published_version = object.getPublishedVersion(con);
                if(published_version != null) {
                    tplBuf.append("<km_published_version>")
        	              .append("<version value=\"").append(published_version).append("\"/>")
                          .append("</km_published_version>");
                }
            }
            
            if(dbTplVi.tvw_km_domain_ind) {
                //get kmObject's immediate domains
                Vector vDomainId = object.getParentDomainId(con);
                tplBuf.append("<km_domain_node_list>");
                for(int i=0; i<vDomainId.size(); i++) {
                    tplBuf.append(ViewKmFolderManager.getVirtualObjectAncestorAsXML(con, ((Long)vDomainId.elementAt(i)).longValue()));
                }
                tplBuf.append("</km_domain_node_list>");
            }
        }
        tplBuf.append("<site id=\"").append(this.owner_ent_id).append("\"/>");
        tplBuf.append(this.objectTypeXML);
        
        tplBuf.append("</applyeasy>");
        return cwUtils.escCrLfForXml(aeUtils.transformXML(tplBuf.toString(), env.INI_XSL_VALTPL, env, null));
    }
    
    /**
    Get XML for an object node
    @param dbObject object to be formatted into XML
    @param xmlBodyHeader XML argument to be appended right after the opening tag <node>
    @param xmlBodyFooter XML argument to be appended right before the closing tag <node>
    @param nature: the nature of the object
    @return XML as String
    */
    public String getObjectNodeAsXML(DbKmObject dbObject, String xmlBodyHeader, 
                                      String xmlBodyFooter) {
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<node ");
        if(dbObject != null) {
            xmlBuf.append("id=\"").append(dbObject.obj_bob_nod_id).append("\"");
        }
        xmlBuf.append(" type=\"").append(DbKmNode.NODE_TYPE_OBJECT).append("\">");
        if(xmlBodyHeader != null) {
            xmlBuf.append(xmlBodyHeader);
        }
        if(dbObject != null) {
            xmlBuf.append("<version>").append(dbObject.obj_version).append("</version>")
                  .append("<title>").append(cwUtils.esc4XML(dbObject.obj_title)).append("</title>")
                  .append("<publish_ind>").append(dbObject.obj_publish_ind).append("</publish_ind>")
                  .append("<latest_ind>").append(dbObject.obj_latest_ind).append("</latest_ind>")
                  .append("<type>").append(dbObject.obj_type).append("</type>")
                  .append("<nature>").append(cwUtils.escNull(cwUtils.esc4XML(dbObject.obj_nature))).append("</nature>")
                  .append("<code>").append(cwUtils.escNull(cwUtils.esc4XML(dbObject.obj_code))).append("</code>")
                  .append("<desc>").append(cwUtils.esc4XML(dbObject.obj_desc)).append("</desc>")
                  .append("<status>").append(dbObject.obj_status).append("</status>")
                  .append("<keywords>").append(cwUtils.esc4XML(dbObject.obj_keywords)).append("</keywords>")
                  .append("<comment>").append(cwUtils.esc4XML(dbObject.obj_comment)).append("</comment>")
                  .append("<author>").append(cwUtils.esc4XML(dbObject.obj_author)).append("</author>")
                  .append("<last_update_timestamp>").append(dbObject.obj_update_timestamp).append("</last_update_timestamp>")
                  .append("<last_update_usr_id>").append(dbObject.obj_update_usr_id).append("</last_update_usr_id>")
                  .append("<last_update_user_display_bil>").append(dbObject.obj_update_usr_display_bil).append("</last_update_user_display_bil>")                  
                  .append("");
        }
        if(xmlBodyFooter != null) {
            xmlBuf.append(xmlBodyFooter);
        }
        xmlBuf.append("</node>");
        return xmlBuf.toString();
    }

    /**
    Get all templates of an object type
    @param oty_code object type code
    @return Vector of aeTemplate object 
    */
    public Vector getObjectTypeTemplate(String oty_code) {
        Vector v = null;        
        aeTemplate template = null;
        for(int i=0; i<this.vObjectType.size(); i++) {
            ViewKmObjectType viewObjectType = (ViewKmObjectType) vObjectType.elementAt(i);
            if(viewObjectType.dbObjectType.oty_code.equalsIgnoreCase(oty_code)) {
                v = viewObjectType.vTemplate;
            }
        }
        return v;
    }
    
    /**
    Get template of an object type, template type
    @param oty_code object type code
    @param tpl_type template type title
    @return aeTemplate object 
    */
    private aeTemplate getObjectTypeTemplate(String oty_code, String tpl_type) {        
        aeTemplate template = null;
        for(int i=0; i<this.vObjectType.size(); i++) {
            ViewKmObjectType viewObjectType = (ViewKmObjectType) vObjectType.elementAt(i);
            if(viewObjectType.dbObjectType.oty_code.equalsIgnoreCase(oty_code)) {
                for(int j=0; j<viewObjectType.vTemplate.size(); j++) {
                    aeTemplate tpl = (aeTemplate) viewObjectType.vTemplate.elementAt(j);
                    if(tpl.tpl_type.equalsIgnoreCase(tpl_type)) {
                        template = tpl;
                    }
                }
            }
        }
        return template;
    }
    
 


    /**
    Publish the object to domain.
    Synchronized this method to make it Thread safe
    @param con Connection to database
    @param env qdbEnv of wizBank
    @param prof loginProfile to wizBank
    @param manager KMObjectManager instance
    @param lastUpdTimestamp last update timestamp for concurrence control
    @param objId object node id 
    @param domain_id_list long array of domain id which the published object will be attached to
    */
    public static ViewKmObject synPublishObject(Connection con, qdbEnv env, loginProfile prof, 
                                                KMObjectManager manager,
                                                Timestamp lastUpdTimestamp, 
                                                long objId, long[] domain_id_list) 
                                                throws SQLException, cwSysMessage, cwException {

        return manager.publishObject(con, env, prof, 
                                     lastUpdTimestamp, 
                                     objId, domain_id_list);

    }
    
    /**
    Publish the object to domain
    @param con Connection to database
    @param env qdbEnv of wizBank
    @param prof loginProfile to wizBank
    @param lastUpdTimestamp last update timestamp for concurrence control
    @param objId object node id 
    @param domain_id_list long array of domain id which the published object will be attached to
    */
    private ViewKmObject publishObject(Connection con, qdbEnv env, loginProfile prof, 
                                       Timestamp lastUpdTimestamp, 
                                       long objId, long[] domain_id_list) 
                                       throws SQLException, cwSysMessage, cwException {
        
        //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }
        ViewKmObject object = new ViewKmObject();
        object.getWithTplNAtt(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }

        //check if the object is available for published
        if(!object.dbObject.obj_status.equals(DbKmObject.OBJ_STATUS_CHECKED_IN)) {
            throw new cwSysMessage(KMModule.SMSG_KM_ERR_CHECKED_OUT);
        }
        
        //get published version
        String lastPubVersion = object.getPublishedVersion(con);

        //get original published domain id
        Vector vOrgDomainIdList = null;
        if(lastPubVersion != null) {
            vOrgDomainIdList = object.getParentDomainId(con);
        }
        
        //get original published object
        ViewKmObject lastPubObject = null;
        if(lastPubVersion != null) {
            lastPubObject = new ViewKmObject();
            lastPubObject.getWithAtt(con, object.dbObject.obj_bob_nod_id, lastPubVersion);
        }

        //Prepare Vectors for new Vector
        Vector vObjColName = new Vector();
        Vector vObjColType = new Vector();
        Vector vObjColValue = new Vector();
        Vector vObjClobColName = new Vector();
        Vector vObjClobColValue = new Vector();
        Vector vTemplate = object.vTemplate;
        Vector vAttachment = object.vAttachment;
                
        vObjColName.addElement("obj_bob_nod_id");
        vObjColType.addElement(DbTable.COL_TYPE_LONG);
        vObjColValue.addElement(new Long(object.dbObject.obj_bob_nod_id));

        vObjColName.addElement("obj_version");
        vObjColType.addElement(DbTable.COL_TYPE_STRING);
        vObjColValue.addElement(getNextPublishedVersion(object.dbObject.obj_version));

        vObjColName.addElement("obj_publish_ind");
        vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
        vObjColValue.addElement(new Boolean(true));

        vObjColName.addElement("obj_latest_ind");
        vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
        vObjColValue.addElement(new Boolean(true));
        
        vObjColName.addElement("obj_type");
        vObjColType.addElement(DbTable.COL_TYPE_STRING);
        vObjColValue.addElement(object.dbObject.obj_type);

        if(object.dbObject.obj_title != null) {
            vObjColName.addElement("obj_title");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_title);
        }
        
        if(object.dbObject.obj_desc != null) {
            vObjColName.addElement("obj_desc");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_desc);
        }

        vObjColName.addElement("obj_status");
        vObjColType.addElement(DbTable.COL_TYPE_STRING);
        vObjColValue.addElement(object.dbObject.obj_status);

        if(object.dbObject.obj_keywords != null) {
            vObjColName.addElement("obj_keywords");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_keywords);
        }

        if(object.dbObject.obj_xml != null) {
            vObjClobColName.addElement("obj_xml");
            vObjClobColValue.addElement(object.dbObject.obj_xml);
        }

        if(object.dbObject.obj_comment != null) {
            vObjColName.addElement("obj_comment");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_comment);
        }

        if(object.dbObject.obj_author != null) {
            vObjColName.addElement("obj_author");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_author);
        }

        if(object.dbObject.obj_update_usr_id != null) {
            vObjColName.addElement("obj_update_usr_id");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(prof.usr_id);
        }
/*
        if( object.dbObject.obj_code != null ) {
            vObjColName.addElement("obj_code");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_code);            
        }
*/
        //Vector for Domain Id
        Vector vDomainIdList = null;
        if(domain_id_list != null) {
            vDomainIdList = new Vector();
            for(int i=0; i<domain_id_list.length; i++) {
                vDomainIdList.addElement(new Long(domain_id_list[i]));
            }
        }

        //insert new version
        ViewKmObject newVersion = ViewKmObject.synNewVersion(con, lastUpdTimestamp, object,
                                                             vObjColName, vObjColType, vObjColValue,
                                                             vObjClobColName, vObjClobColValue, 
                                                             vTemplate, vAttachment, vDomainIdList); 
        if(newVersion == null) {
            throw new cwSysMessage("GEN006");
        }
        
        //insert into action history 
        Timestamp curTime = null;
        String userDisplayBil = null;
        //1. insert removal history 
        if(vOrgDomainIdList != null) {
            for(int i=0; i<vOrgDomainIdList.size(); i++) {
                if(vDomainIdList==null || vDomainIdList.indexOf((Long)vOrgDomainIdList.elementAt(i)) < 0) {
                    if(curTime == null) curTime = cwSQL.getTime(con);
                    if(userDisplayBil == null) userDisplayBil = dbRegUser.getUserName(con,newVersion.dbObject.obj_update_usr_id);
                    ViewKmNodeActn action = new ViewKmNodeActn();
                    action.type = DbKmNodeActnHistory.TYPE_DOMAIN_DEL_PUB;
                    action.node_id = ((Long)vOrgDomainIdList.elementAt(i)).longValue();
                    action.modified_node_id = lastPubObject.dbObject.obj_bob_nod_id;
                    action.title = lastPubObject.dbObject.obj_title;
                    action.usr_id = newVersion.dbObject.obj_update_usr_id;
                    action.usr_display_bil = userDisplayBil;
                    action.update_timestamp = curTime;
                    action.version = lastPubObject.dbObject.obj_version;
                    KMSubscriptionManager.insAction(con, action);
                }
            }
        }
        //2. insert new publish history
        if(vDomainIdList!=null) {
            for(int i=0; i<vDomainIdList.size(); i++) {
                if(curTime == null) curTime = cwSQL.getTime(con);
                if(userDisplayBil == null) userDisplayBil = dbRegUser.getUserName(con,newVersion.dbObject.obj_update_usr_id);
                ViewKmNodeActn action = new ViewKmNodeActn();
                action.type = DbKmNodeActnHistory.TYPE_DOMAIN_NEW_PUB;
                action.node_id = ((Long)vDomainIdList.elementAt(i)).longValue();
                action.modified_node_id = newVersion.dbObject.obj_bob_nod_id;
                action.title = newVersion.dbObject.obj_title;
                action.usr_id = newVersion.dbObject.obj_update_usr_id;
                action.usr_display_bil = userDisplayBil;
                action.update_timestamp = curTime;
                action.version = newVersion.dbObject.obj_version;
                if(lastPubObject != null) {
                    action.title_org = lastPubObject.dbObject.obj_title;
                }
                action.comments = newVersion.dbObject.obj_comment;
                KMSubscriptionManager.insAction(con, action);
            }
        }
        
        //copy files
        String srcDir = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + object.dbObject.obj_bob_nod_id + dbUtils.SLASH + object.dbObject.obj_version;
        String desDir = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + newVersion.dbObject.obj_bob_nod_id + dbUtils.SLASH + newVersion.dbObject.obj_version;
        try {
            dbUtils.copyDir(srcDir, desDir);
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
        
        //remove old index
        if(lastPubVersion != null) {
            if(lastPubObject.vAttachment == null || lastPubObject.vAttachment.size() == 0) {
                String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                lastPubObject.dbObject.obj_version + indexKeyToken;                    
                removeIndex(env, indexKey);
            } else {
                for(int i=0; i<lastPubObject.vAttachment.size(); i++) {
                    String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                    lastPubObject.dbObject.obj_version + indexKeyToken + 
                                    ((dbAttachment)lastPubObject.vAttachment.elementAt(i)).att_filename;
                    removeIndex(env, indexKey);
                }
            }
        }

        //add new index
        String[] str_domain_id_list = null;
        if(domain_id_list != null) {
            str_domain_id_list = new String[domain_id_list.length];
            for(int j=0; j<domain_id_list.length; j++) {
                str_domain_id_list[j] = Long.toString(domain_id_list[j]);
            }
        }
        if(newVersion.vAttachment == null || newVersion.vAttachment.size() == 0) {
            //build index for searchEngine
            String indexKey = newVersion.dbObject.obj_bob_nod_id + indexKeyToken +
                            newVersion.dbObject.obj_version + indexKeyToken;

            bulidIndex(env, indexKey, null, newVersion, str_domain_id_list);
        } else {
            for(int i=0; i<newVersion.vAttachment.size(); i++) {
                //build index for searchEngine
                String indexKey = newVersion.dbObject.obj_bob_nod_id + indexKeyToken +
                                newVersion.dbObject.obj_version + indexKeyToken + 
                                ((dbAttachment)newVersion.vAttachment.elementAt(i)).att_filename;

                bulidIndex(env, indexKey, 
                          ((dbAttachment)newVersion.vAttachment.elementAt(i)).att_filename, 
                          newVersion, str_domain_id_list);
            }
        }
        return newVersion;
    }
    
    /**
    Check in object.
    @param con Connection to database
    @env qdbEnv of wizBank
    @param prof loginProfile of login user
    @param tmpUploadPath temp upload path of uploaded files
    @param vObjColName Vector of kmObject column names
    @param vObjColType Vector of kmObject column types
    @param vObjColValue Vector of kmObject column values
    @param vObjClobColName Vector of kmObject Clob column names
    @param vObjClobColValue Vector of kmObject Clob column values
    @param vFileName Vector of String of attachment file names
    @return View of the newly created Object
    */
    public ViewKmObject checkInObject(Connection con, qdbEnv env, 
                                      loginProfile prof, String tmpUploadPath,
                                      long objId, Timestamp lastUpdTimestamp,
                                      Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                                      Vector vObjClobColName, Vector vObjClobColValue, 
                                      Vector vFileName, String bob_code) 
                                      throws SQLException, cwSysMessage, cwException {

        //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }
        ViewKmObject object = new ViewKmObject();
        object.getWithAtt(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }

        //check if the object is available for check out
        if(!object.dbObject.obj_status.equals(DbKmObject.OBJ_STATUS_CHECKED_OUT) ||
           !object.dbObject.obj_update_usr_id.equals(prof.usr_id)) {
            throw new cwSysMessage(KMModule.SMSG_KM_ERR_NOT_CHECKED_OUT);
        }

        //set column values having business logics
        //set database fields
        if(vObjColName.indexOf("obj_bob_nod_id") < 0) {
            vObjColName.addElement("obj_bob_nod_id");
            vObjColType.addElement(DbTable.COL_TYPE_LONG);
            vObjColValue.addElement(new Long(object.dbObject.obj_bob_nod_id));
        }
        if(vObjColName.indexOf("obj_version") < 0) {
            vObjColName.addElement("obj_version");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(getNextVersion(object.dbObject.obj_version));
        }
        if(vObjColName.indexOf("obj_publish_ind") < 0) {
            vObjColName.addElement("obj_publish_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(false));
        }
        if(vObjColName.indexOf("obj_latest_ind") < 0) {
            vObjColName.addElement("obj_latest_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(true));
        }
        if(vObjColName.indexOf("obj_status") < 0) {
            vObjColName.addElement("obj_status");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(DbKmObject.OBJ_STATUS_CHECKED_IN);
        }
        if(vObjColName.indexOf("obj_update_usr_id") < 0) {
            vObjColName.addElement("obj_update_usr_id");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(prof.usr_id);
        }

        //get object type
        int index = vObjColName.indexOf("obj_type");
        String objectType = (String)vObjColValue.elementAt(index);
        
        //format Vector for aeTemplate
        Vector vTemplate = getObjectTypeTemplate(objectType);
        
        //format Vector for Attachment
        Vector vAttachment = null;
        if(vFileName != null && vFileName.size() > 0) {
            vAttachment = new Vector();
            for(int i=0; i<vFileName.size(); i++) {
                dbAttachment att = new dbAttachment();
                att.att_filename = (String)vFileName.elementAt(i);
                vAttachment.addElement(att);
            }
        } else {
            vAttachment = object.vAttachment;
        }
                
        //insert new version
        ViewKmObject newVersion = ViewKmObject.synNewVersion(con, lastUpdTimestamp, object,
                                                             vObjColName, vObjColType, vObjColValue,
                                                             vObjClobColName, vObjClobColValue, 
                                                             vTemplate, vAttachment, null); 
        if(newVersion == null) {
            throw new cwSysMessage("GEN006");
        }
        
        //copy files
        if(vFileName != null && vFileName.size() > 0) {
            String saveDirPath = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + 
                                 newVersion.dbObject.obj_bob_nod_id + dbUtils.SLASH + 
                                 newVersion.dbObject.obj_version;
            try {
                dbUtils.moveDir(tmpUploadPath, saveDirPath);
            } catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        } else {
            String srcDir = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + object.dbObject.obj_bob_nod_id + dbUtils.SLASH + object.dbObject.obj_version;
            String desDir = env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + newVersion.dbObject.obj_bob_nod_id + dbUtils.SLASH + newVersion.dbObject.obj_version;
            
            try {
                dbUtils.copyDir(srcDir, desDir);
            } catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        }

        //update kmBaseObject
        DbKmBaseObject baseObj = new DbKmBaseObject();
        baseObj.bob_nod_id = object.dbObject.obj_bob_nod_id;
        baseObj.bob_code = bob_code;
        baseObj.upd(con);        
        
        return newVersion;
    }
    
    
    /**
    Insert new kmObject.
    The newly create kmObject will contains Templates defined by kmObjectTypeTemplate
    @param con Connection to database
    @param prof loginProfile of login user
    @param vNodColName Vector of kmNode column names 
    @param vNodColType Vector of kmNode column types
    @param vNodColValue Vector of kmNode column values
    @param vObjColName Vector of kmObject column names
    @param vObjColType Vector of kmObject column types
    @param vObjColValue Vector of kmObject column values
    @param vObjClobColName Vector of kmObject Clob column names
    @param vObjClobColValue Vector of kmObject Clob column values
    @param vFileName Vector of String of attachment file names
    @return View of the newly created Object
    */
    public ViewKmObject insObject(Connection con, loginProfile prof,
                                  Vector vNodColName, Vector vNodColType, Vector vNodColValue,
                                  Vector vObjColName, Vector vObjColType, Vector vObjColValue,
                                  Vector vObjClobColName, Vector vObjClobColValue, 
                                  Vector vFileName, String bob_code) 
                                  throws SQLException, cwSysMessage, cwException {
    
        //get object type
        int index = vObjColName.indexOf("obj_type");
        String objectType = (String)vObjColValue.elementAt(index);
        
        //set column values having business logics
        if(vNodColName.indexOf("nod_create_usr_id") < 0) {
            vNodColName.addElement("nod_create_usr_id");
            vNodColType.addElement(DbTable.COL_TYPE_STRING);
            vNodColValue.addElement(prof.usr_id);
        }
        if(vNodColName.indexOf("nod_owner_ent_id") < 0) {
            vNodColName.addElement("nod_owner_ent_id");
            vNodColType.addElement(DbTable.COL_TYPE_LONG);
            vNodColValue.addElement(new Long(prof.root_ent_id));
        }
        if(vNodColName.indexOf("nod_acl_inherit_ind") < 0) {
            vNodColName.addElement("nod_acl_inherit_ind");
            vNodColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vNodColValue.addElement(new Boolean(true));
        }
        if(vObjColName.indexOf("obj_version") < 0) {
            vObjColName.addElement("obj_version");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(INITIAL_VERSION);
        }
        if(vObjColName.indexOf("obj_publish_ind") < 0) {
            vObjColName.addElement("obj_publish_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(false));
        }
        if(vObjColName.indexOf("obj_latest_ind") < 0) {
            vObjColName.addElement("obj_latest_ind");
            vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vObjColValue.addElement(new Boolean(true));
        }
        if(vObjColName.indexOf("obj_status") < 0) {
            vObjColName.addElement("obj_status");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(DbKmObject.OBJ_STATUS_CHECKED_IN);
        }
        if(vObjColName.indexOf("obj_update_usr_id") < 0) {
            vObjColName.addElement("obj_update_usr_id");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(prof.usr_id);
        }

        
        
        //format Vector for aeTemplate
        Vector vTemplate = getObjectTypeTemplate(objectType);
        
        //format Vector for Attachment
        Vector vAttachment = null;
        if(vFileName != null) {
            vAttachment = new Vector();
            for(int i=0; i<vFileName.size(); i++) {
                dbAttachment att = new dbAttachment();
                att.att_filename = (String)vFileName.elementAt(i);
                vAttachment.addElement(att);
            }
        }

        //insert the object into database
        ViewKmObject object = new ViewKmObject();
        object.ins(con, 
                vNodColName, vNodColType, vNodColValue,
                vObjColName, vObjColType, vObjColValue,
                vObjClobColName, vObjClobColValue, 
                vTemplate, vAttachment, bob_code);

        return object;
    }
    
    /*
    Get XML for viewing an object's latest version
    @param con Connection to database
    @param prof loginProfile of wizBank
    @param env qdbEnv of wizBank
    @param objId object node id being viewed
    @param oty_code object type used to view this object 
    @param tvw_id template view used to view this object
    @return XML view of the input object version
    public String getObjectLatestVersionAsXML(Connection con, loginProfile prof, qdbEnv env,
                                              long objId, 
                                              String oty_code, String tvw_id) 
                                              throws SQLException, cwSysMessage, cwException {
        
        return getObjectVersionAsXML(con, prof, env, objId, null, oty_code, tvw_id);
    }
    */
    
    /**
    Get XML for viewing an object version
    @param con Connection to database
    @param prof loginProfile of wizBank
    @param env qdbEnv of wizBank
    @param objId object node id being viewed
    @param version object node version being viewed
    @param oty_code object type used to view this object 
    @param tvw_id template view used to view this object
    @param parent_nod_id parent domain node id of the object for find ancestors
    @return XML view of the input object version
    */
    public String getObjectVersionAsXML(Connection con, loginProfile prof, qdbEnv env,
                                        long objId, String version,
                                        String oty_code, String tvw_id,
                                        long parent_nod_id) 
                                        throws SQLException, cwSysMessage, cwException {
        String nature = DbKmBaseObject.getNature(con, objId);
        if (nature != null && nature.length() > 0) {
            this.vObjectType = (Vector)myObjectTypeByNature.get(nature);
            this.objectTypeXML = (String)this.myObjectTypeByNatureXML.get(nature);
        }        

        try{  
            String inVersion = version;
            //get the version code 
            if(version == null || version.equalsIgnoreCase(PUBLISHED_VERSION)) {
                version = ViewKmObject.getPublishedVersion(con, objId);
                if(version == null) {
                    throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: published");
                }
            } else if(version.equalsIgnoreCase(LATEST_VERSION)) {
                version = ViewKmObject.getLatestVersion(con, objId);
                if(version == null) {
                    throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
                }
            }

            //get object details
            ViewKmObject object = new ViewKmObject();
            object.getAll(con, objId, version);
            
            if(inVersion == null || inVersion.equalsIgnoreCase(PUBLISHED_VERSION)) {
                //use the 1st parent domain node id 
                //to find the object's domain ancestors
                //if parent_nod_id is not given
                if(parent_nod_id <= 0) {
                    Vector vDomainId = object.getParentDomainId(con);
                    if(vDomainId!=null && vDomainId.size() > 0) {
                        parent_nod_id = ((Long)vDomainId.elementAt(0)).longValue();
                    }
                }
            } else {
                //if a version is given 
                //and it is not "PUBLISHED"
                //get the object's work folder ancestors
                parent_nod_id = object.dbObject.nod_parent_nod_id;
            }
            
            //get ancestor node list
            String ancestorXML = (parent_nod_id > 0) ?
                ViewKmFolderManager.getVirtualObjectAncestorAsXML(con, parent_nod_id) :
                new String(); 
            
            StringBuffer xmlBuf = new StringBuffer(1024);
            //get valued template
            if(oty_code == null || oty_code.length() == 0) {
                oty_code = object.dbObject.obj_type;
            }

            // get the Object Nature
            xmlBuf.append(getValuedTemplate(con, env, object, oty_code, tvw_id));
            
            //get attachment list XML
            xmlBuf.append(getAttachmentXML(object.vAttachment));
            return getObjectNodeAsXML(object.dbObject, ancestorXML, xmlBuf.toString());
        } catch (SQLException e){
        	CommonLog.error(e.getMessage(),e);
        }
        return null;
       
    }

    /**
    Get XML for viewing an object version's attachment only
    @param con Connection to database
    @param prof loginProfile of wizBank
    @param objId object node id being viewed
    @param version object node version being viewed
    @param parent_nod_id parent domain node id used to find ancestors
    @return XML view of the input object version
    */
    public String getObjectVersionAttAsXML(Connection con, loginProfile prof, 
                                           long objId, String version,
                                           long parent_nod_id) 
                                           throws SQLException, cwSysMessage, cwException {
        
        String inVersion = version;
        if(version == null || version.equalsIgnoreCase(PUBLISHED_VERSION)) {
            version = ViewKmObject.getPublishedVersion(con, objId);
            if(version == null) {
                throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: published");
            }
        } else if(version.equalsIgnoreCase(LATEST_VERSION)) {
            version = ViewKmObject.getLatestVersion(con, objId);
            if(version == null) {
                throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
            }
        }
        
        ViewKmObject object = new ViewKmObject();
        object.getWithAtt(con, objId, version);
        
        if(inVersion == null || inVersion.equalsIgnoreCase(PUBLISHED_VERSION)) {
            //use the 1st parent domain node id 
            //to find the object's domain ancestors
            //if parent_nod_id is not given
            if(parent_nod_id <= 0) {
                Vector vDomainId = object.getParentDomainId(con);
                if(vDomainId!=null && vDomainId.size() > 0) {
                    parent_nod_id = ((Long)vDomainId.elementAt(0)).longValue();
                }
            }
        } else {
            //if a version is given 
            //and it is not "PUBLISHED"
            //get the object's work folder ancestors
            parent_nod_id = object.dbObject.nod_parent_nod_id;
        }

        //get ancestor node list
        String ancestorXML = (parent_nod_id > 0) ?
            ViewKmFolderManager.getVirtualObjectAncestorAsXML(con, parent_nod_id) :
            new String(); 
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        //get attachment list XML
        xmlBuf.append(getAttachmentXML(object.vAttachment));
        
        return getObjectNodeAsXML(object.dbObject, ancestorXML, xmlBuf.toString());
    }
    
    /**
    Get XML for the input Vector of attachment
    @param vAttachment Vector of dbAttachment
    @return XML of <attachment_list>
    */
    private String getAttachmentXML(Vector vAttachment) {
        StringBuffer xmlBuf = new StringBuffer(512);
        if(vAttachment != null) {
    		xmlBuf.append("<attachment_list>");
            for(int i=0; i<vAttachment.size(); i++) {
                dbAttachment att = (dbAttachment) vAttachment.elementAt(i);
			    xmlBuf.append("<attachment id=\"").append(att.att_id).append("\">")
			          .append(cwUtils.esc4XML(att.att_filename)).append("</attachment>");
			}
		    xmlBuf.append("</attachment_list>");
        }
        return xmlBuf.toString();
    }
    

    public void keepCheckOutObject(Connection con, loginProfile prof, long objId )
        throws SQLException, cwSysMessage {
            
            DbKmObject kmObj = new DbKmObject();
            kmObj.obj_bob_nod_id = objId;
            kmObj.obj_version = ViewKmObject.getLatestVersion(con, objId);
            kmObj.getObjUpdTimestamp(con);
            checkOutObject(con, prof, objId, kmObj.obj_update_timestamp);
            return;
        }
    
    /**
    Check out the latest version of an object
    @param con Connection to database
    @param prof loginProfile to wizBank
    @param objId node id of the object to be checked out
    @param lastUpdTimestamp last update timestamp of object for concurrence control
    */
    public void checkOutObject(Connection con, loginProfile prof, long objId, 
                               Timestamp lastUpdTimestamp) 
                               throws SQLException, cwSysMessage {
        
        //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }
        ViewKmObject object = new ViewKmObject();
        object.getBasic(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }
        
        //check if the object is available for check out
        if(!object.dbObject.obj_status.equals(DbKmObject.OBJ_STATUS_CHECKED_IN)) {
            throw new cwSysMessage(KMModule.SMSG_KM_ERR_CHECKED_OUT);
        }
        
        //update object status to CHECKED_OUT with concurrence control
        int rowCount = object.updateObjStatus(con, DbKmObject.OBJ_STATUS_CHECKED_OUT, 
                                              prof.usr_id, lastUpdTimestamp);

        if(rowCount!=1) {
            throw new cwSysMessage("GEN006");
        }
        return;
    }
    
    /**
    Get XML for the version history of an object
    @param con Connection to database
    @param objId object node id
    @return XML showing the version history of an object
    */
    public String getObjectVersionHistAsXML(Connection con, long objId) 
        throws SQLException, cwSysMessage {
        
        String version = ViewKmObject.getLatestVersion(con, objId);
        
        ViewKmObject object = new ViewKmObject();
        object.getBasic(con, objId, version);
        
        //get ancestor node list
        String ancestorXML = 
            ViewKmFolderManager.getFolderAncestorAsXML(con, object.dbObject.nod_id);
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        
        //get version list XML
        xmlBuf.append(getVersionListAsXML(con, objId));
        
        return getObjectNodeAsXML(object.dbObject, ancestorXML, xmlBuf.toString());
    }
    
    /**
    Get XML for the version history of an object
    @param con Connection to database
    @param objId object node id
    @return XML showing the version history of an object
    */
    private String getVersionListAsXML(Connection con, long objId) throws SQLException {
        
        Vector vVersionList = ViewKmObject.getVersionList(con, objId);
        StringBuffer xmlBuf = new StringBuffer(512);
        Hashtable versionPublishHash = new Hashtable();
        String prevVersion = "";
        String currVersion = "";
        boolean isPrevPublish = false;
        // remarks: the order in vVersionList is descending
        for(int i=0; i<vVersionList.size(); i++) {
            ViewKmObject object = (ViewKmObject)vVersionList.elementAt(i);
            currVersion = object.dbObject.obj_version;
            if(!prevVersion.equalsIgnoreCase("") && isPrevPublish) {
                versionPublishHash.put(currVersion, prevVersion); 
                // as it is descending, curr map to prev (not prev map to curr)
            }
            isPrevPublish = object.dbObject.obj_publish_ind;
            prevVersion = currVersion;
        }
        
        xmlBuf.append("<version_list>");
        for(int i=0; i<vVersionList.size(); i++) {
            ViewKmObject object = (ViewKmObject)vVersionList.elementAt(i);
            xmlBuf.append("<version code=\"").append(object.dbObject.obj_version).append("\">");
            if(versionPublishHash.containsKey(object.dbObject.obj_version)) {
                String publishedToVersion = (String) versionPublishHash.get(object.dbObject.obj_version);
                xmlBuf.append("<published_to version=\"" + publishedToVersion + "\"/>");
            }
            xmlBuf.append("<published_version>").append(object.dbObject.obj_publish_ind).append("</published_version>")
                  .append("<comment>").append(cwUtils.esc4XML(object.dbObject.obj_comment)).append("</comment>")
                  .append("<last_update>")
                  .append("<datetime>").append(object.dbObject.obj_update_timestamp).append("</datetime>")
                  .append("<user id=\"").append(object.updateUser.usr_ent_id).append("\">")
                  .append(cwUtils.esc4XML(object.updateUser.usr_display_bil))
                  .append("</user>")
                  .append("</last_update>");
            xmlBuf.append("</version>");
        }
        xmlBuf.append("</version_list>");
        
        return xmlBuf.toString();
    }
    
    /**
    Delete an object and all of its versions
    @param con Connection to database
    @param env qdbEnv of wizBank
    @param objId object node id
    @param lastUpdTimestamp object last update timestamp for concurrence control
    */
    
    public void del(Connection con, qdbEnv env, long objId, Timestamp lastUpdTimestamp) 
        throws SQLException, cwSysMessage {
        try{
        //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }
        ViewKmObject object = new ViewKmObject();
        object.getWithAtt(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }
        //Get published version
        ViewKmObject publishedObject = null;
        if(!object.dbObject.obj_publish_ind) {
            String pubVersion = object.getPublishedVersion(con);
            if(pubVersion != null) {
                publishedObject = new ViewKmObject();
                publishedObject.getWithAtt(con, object.dbObject.obj_bob_nod_id, pubVersion);
            }
        } else {
            publishedObject = object;
        }
        
        int row = object.del(con, lastUpdTimestamp);
        if(row == 0) {
            throw new cwSysMessage("GEN006");
        }
        
        
        //remove old index
        if(publishedObject != null) {
            if(publishedObject.vAttachment == null || publishedObject.vAttachment.size() > 0) {
                String indexKey = publishedObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                  publishedObject.dbObject.obj_version + indexKeyToken;
                removeIndex(env, indexKey);                                  
            } else {
                for(int i=0; i<publishedObject.vAttachment.size(); i++) {
                    String indexKey = publishedObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                    publishedObject.dbObject.obj_version + indexKeyToken + 
                                    ((dbAttachment)publishedObject.vAttachment.elementAt(i)).att_filename;
                    removeIndex(env, indexKey);                    
                }
            }
        }
       
         } catch (SQLException e){
        	 CommonLog.error(e.getMessage(),e);
          }
        return;
    }
    
    
    public void markDel(Connection con, qdbEnv env, long objId, Timestamp lastUpdTimestamp, String usr_id) 
        throws SQLException, cwSysMessage, cwException {
        try{
      
        // from publish
      
       //get object's basic info latest version
        String version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }
        ViewKmObject object = new ViewKmObject();
        object.getWithTplNAtt(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }

        //check if the object is available for published
        if(!object.dbObject.obj_status.equals(DbKmObject.OBJ_STATUS_CHECKED_IN)) {
            throw new cwSysMessage(KMModule.SMSG_KM_ERR_CHECKED_OUT);
        }
        
        //get published version
        String lastPubVersion = object.getPublishedVersion(con);

        //get original published domain id
        Vector vOrgDomainIdList = null;
        if(lastPubVersion != null) {
            vOrgDomainIdList = object.getParentDomainId(con);
        }
        
        //get original published object
        ViewKmObject lastPubObject = null;
        if(lastPubVersion != null) {
            lastPubObject = new ViewKmObject();
            lastPubObject.getWithAtt(con, object.dbObject.obj_bob_nod_id, lastPubVersion);
        }

        //remove old index
        if(lastPubObject != null) {
            if(lastPubObject.vAttachment == null || lastPubObject.vAttachment.size() == 0) {
                String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                  lastPubObject.dbObject.obj_version + indexKeyToken;
                removeIndex(env, indexKey);                                  
            } else {
                for(int i=0; i<lastPubObject.vAttachment.size(); i++) {
                    String indexKey = lastPubObject.dbObject.obj_bob_nod_id + indexKeyToken +
                                    lastPubObject.dbObject.obj_version + indexKeyToken + 
                                    ((dbAttachment)lastPubObject.vAttachment.elementAt(i)).att_filename;
                    removeIndex(env, indexKey);                    
                }
            }
        }

        //Prepare Vectors for new Vector
        Vector vObjColName = new Vector();
        Vector vObjColType = new Vector();
        Vector vObjColValue = new Vector();
        Vector vObjClobColName = new Vector();
        Vector vObjClobColValue = new Vector();
        Vector vTemplate = object.vTemplate;
        Vector vAttachment = object.vAttachment;
                
        vObjColName.addElement("obj_bob_nod_id");
        vObjColType.addElement(DbTable.COL_TYPE_LONG);
        vObjColValue.addElement(new Long(object.dbObject.obj_bob_nod_id));

        vObjColName.addElement("obj_version");
        vObjColType.addElement(DbTable.COL_TYPE_STRING);
        vObjColValue.addElement(getNextPublishedVersion(object.dbObject.obj_version));

        vObjColName.addElement("obj_publish_ind");
        vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
        vObjColValue.addElement(new Boolean(true));

        vObjColName.addElement("obj_latest_ind");
        vObjColType.addElement(DbTable.COL_TYPE_BOOLEAN);
        vObjColValue.addElement(new Boolean(true));
        
        vObjColName.addElement("obj_type");
        vObjColType.addElement(DbTable.COL_TYPE_STRING);
        vObjColValue.addElement(object.dbObject.obj_type);

        if(object.dbObject.obj_title != null) {
            vObjColName.addElement("obj_title");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_title);
        }
        
        if(object.dbObject.obj_desc != null) {
            vObjColName.addElement("obj_desc");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_desc);
        }

        vObjColName.addElement("obj_status");
        vObjColType.addElement(DbTable.COL_TYPE_STRING);
        vObjColValue.addElement(DbKmObject.OBJ_STATUS_DELETE);

        if(object.dbObject.obj_keywords != null) {
            vObjColName.addElement("obj_keywords");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_keywords);
        }

        if(object.dbObject.obj_xml != null) {
            vObjClobColName.addElement("obj_xml");
            vObjClobColValue.addElement(object.dbObject.obj_xml);
        }

        if(object.dbObject.obj_comment != null) {
            vObjColName.addElement("obj_comment");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_comment);
        }

        if(object.dbObject.obj_author != null) {
            vObjColName.addElement("obj_author");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(object.dbObject.obj_author);
        }

        if(object.dbObject.obj_update_usr_id != null) {
            vObjColName.addElement("obj_update_usr_id");
            vObjColType.addElement(DbTable.COL_TYPE_STRING);
            vObjColValue.addElement(usr_id);
        }

        
        //insert new version
        ViewKmObject newVersion = ViewKmObject.synNewVersion(con, lastUpdTimestamp, object,
                                                             vObjColName, vObjColType, vObjColValue,
                                                             vObjClobColName, vObjClobColValue, 
                                                             vTemplate, vAttachment, null); 
       
/*        //get object's basic info latest version
       
        version = ViewKmObject.getLatestVersion(con, objId);
        if(version == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: latest");
        }
        object = new ViewKmObject();
        object.getWithAtt(con, objId, version);
        if(object.dbObject == null) {
            throw new cwSysMessage("GEN005", "Object node id: " + objId + ", version: " + version);
        }
        //Get published version
        ViewKmObject publishedObject = null;
        if(!object.dbObject.obj_publish_ind) {
            String pubVersion = object.getPublishedVersion(con);
            if(pubVersion != null) {
                publishedObject = new ViewKmObject();
                publishedObject.getWithAtt(con, object.dbObject.obj_bob_nod_id, pubVersion);
            }
        } else {
            publishedObject = object;
        }*/

        int row = object.markDel(con, lastUpdTimestamp, usr_id);
        /*
        if(row == 0) {
            throw new cwSysMessage("GEN006");
        }*/
        
        
        //update parent node to null;
        DbKmNode dbNode = new DbKmNode();
        dbNode.nod_parent_nod_id = 0;
        dbNode.nod_ancestor = null;
        dbNode.nod_id = objId;
        dbNode.updAncestor(con);
         } catch (SQLException e){
        	 CommonLog.error(e.getMessage(),e); 
          }
        return;
    }
    
    
    
    /**
    Get the xml of the search result
    @param sess HttpSession of the user
    @param usrGroupsList ancestor group list of the user (included his/her own enity id)
    @param obj_type object type
    @param node_id  domain/work folder id
    @param words    search criteria
    @param flag indicator of search including the deleted object
    @return xml of the search result
    */
    public static String getSearchResultAsXML(Connection con, HttpSession sess, String usrGroupsList, String obj_type, long node_id, String query_string, cwPagination cwPage, String DB_PATH, boolean flag)
        throws SQLException
    {
        boolean useSession = false;
        // All objects that matched the searching criteria
        Vector matchedVec = new Vector();
        Vector readableVec = null;
        
        Timestamp sess_time = (Timestamp) sess.getAttribute(KMModule.SESS_KM_SEARCH_RESULT_TS);
        if (cwPage.ts != null && sess_time != null && sess_time.equals(cwPage.ts)) {
                // use session 
                matchedVec = (Vector) sess.getAttribute(KMModule.SESS_KM_SEARCH_RESULT_VEC);
                if (matchedVec != null && matchedVec.size() > 0) {
                    useSession = true;
                    CommonLog.debug("Use session result.");
                }
        }

        if (!useSession) {
            // Get the nodes that are required to search and accessible to the user
            // domainSearchVec = XXXnode_id,
            // Get all the readable domains
            readableVec = DbKmNodeAccess.getReadableNode(con, usrGroupsList);
            Vector searchNodeVec = new Vector();
            if (node_id > 0) {
                searchNodeVec = ViewKmFolderManager.getAllChildFoldersID(con, node_id);
                searchNodeVec.addElement(new Long(node_id));
                Long nodeID;
                // Remove un-readable nodes
                for (int i=0;i<searchNodeVec.size();i++) {
                    nodeID = (Long) searchNodeVec.elementAt(i);
                    if (!readableVec.contains(nodeID)) {
                        searchNodeVec.removeElementAt(i);
                        i--;
                    }
                }
            }else {
                searchNodeVec = readableVec;
            }
            Search se  = new Search(DB_PATH);
            matchedVec = se.simple_search(obj_type,  query_string, searchNodeVec, SEARCH_RESULT_LIMIT);
            if (matchedVec == null) {
                matchedVec = new Vector();
            }
            if( !flag ) {
                ViewKmObject vObj = null;
                for(int i=matchedVec.size() - 1; i>-1; i--){
                    vObj = (ViewKmObject)matchedVec.elementAt(i);
                    if( vObj.dbObject.obj_delete_timestamp != null ){
                        matchedVec.removeElementAt(i);
                    }
                }
            }
            
            CommonLog.debug("No of objects found = " + matchedVec.size());            
        }
        
        
        return searchResultAsXML(con, sess, matchedVec, readableVec, usrGroupsList, useSession, cwPage);
    }

    
    
    /**
    Get the xml of the search result
    @param sess HttpSession of the user
    @param usrGroupsList ancestor group list of the user (included his/her own enity id)
    @param obj_title object title
    @param obj_author object author
    @param obj_type object type
    @param node_id_list  domain/work folder id list
    @param key_words    search criteria
    @param flag indicator of search including the deleted object
    @return xml of the search result
    */
    public static String getAdvSearchResultAsXML(Connection con, HttpSession sess, String usrGroupsList, String obj_title, String obj_author, String[] obj_type_list, String key_words, String call_num, long[] node_id_list, cwPagination cwPage, String DB_PATH, boolean flag)
        throws SQLException
    {
        boolean useSession = false;
        // All objects that matched the searching criteria
        Vector matchedVec = new Vector();
        Vector readableVec = null;
        
        Timestamp sess_time = (Timestamp) sess.getAttribute(KMModule.SESS_KM_SEARCH_RESULT_TS);
        if (cwPage.ts != null && sess_time != null && sess_time.equals(cwPage.ts)) {
                // use session 
                matchedVec = (Vector) sess.getAttribute(KMModule.SESS_KM_SEARCH_RESULT_VEC);
                if (matchedVec != null && matchedVec.size() > 0) {
                    useSession = true;
                    CommonLog.debug("Use session result.");
                }
        }
        
        if (!useSession) {
            // Get the nodes that are required to search and accessible to the user
            // domainSearchVec = XXXnode_id,
            // Get all the readable domains
            readableVec = DbKmNodeAccess.getReadableNode(con, usrGroupsList);
            Vector searchNodeVec = new Vector();
            if (node_id_list != null && node_id_list.length > 0) {
                                
                for(int i=0; i<node_id_list.length; i++){
                    searchNodeVec.addAll(ViewKmFolderManager.getAllChildFoldersID(con, node_id_list[i]));
                    searchNodeVec.addElement(new Long(node_id_list[i]));
                }
                Long nodeID;
                // Remove un-readable nodes
                for (int i=0;i<searchNodeVec.size();i++) {
                    nodeID = (Long) searchNodeVec.elementAt(i);
                    if (!readableVec.contains(nodeID)) {
                        searchNodeVec.removeElementAt(i);
                        i--;
                    }
                }
            }else {
                searchNodeVec = readableVec;
            }
            Search se  = new Search(DB_PATH);
            //matchedVec = se.simple_search(obj_type,  query_string, searchNodeVec, SEARCH_RESULT_LIMIT);
            matchedVec = se.adv_search(obj_title, obj_author, obj_type_list, key_words, call_num, searchNodeVec, SEARCH_RESULT_LIMIT);
            if (matchedVec == null) {
                matchedVec = new Vector();
            }
            CommonLog.debug("No of objects found = " + matchedVec.size());
            
            if( !flag ) {
                ViewKmObject vObj = null;
                for(int i=matchedVec.size() - 1; i>-1; i--){
                    vObj = (ViewKmObject)matchedVec.elementAt(i);
                    if( vObj.dbObject.obj_delete_timestamp != null ){
                        matchedVec.removeElementAt(i);
                    }
                }
            }
            CommonLog.debug("No of objects found = " + matchedVec.size());
        }
       
        return searchResultAsXML(con, sess, matchedVec, readableVec, usrGroupsList, useSession, cwPage);
    }
    
    private static String searchResultAsXML(Connection con, HttpSession sess, Vector matchedVec, Vector readableVec, String usrGroupsList, boolean useSession, cwPagination cwPage) 
        throws SQLException {
        
        Vector vObjVec = null;
        Hashtable domainsHash = new Hashtable();
        Vector domainsVec = new Vector();
        Hashtable ancestorXMLHash = null;
        ViewKmObject vObj = null;
        Vector objIdVec = new Vector();
        Vector publishedVec = null;
        Long objID = null;
        
        // Get the distinct domains of all those objects
        if (matchedVec.size() > 0) {
            // Get the objects' id of all the matched objects
            for (int i=0;i<matchedVec.size();i++) {
                vObj = (ViewKmObject) matchedVec.elementAt(i);
                objIdVec.addElement(new Long(vObj.dbObject.obj_bob_nod_id));
            }
            ViewKmFolderManager.getPublishedDomains(con, objIdVec, domainsHash, domainsVec);
            // Get the readable domains from the list
            if (readableVec == null) {
                readableVec = DbKmNodeAccess.getReadableNode(con, domainsVec, usrGroupsList);
            }
            ancestorXMLHash = ViewKmFolderManager.getFolderAncestorAsXML(con, domainsVec, true);

            // Rebuild not readable domain(s) of the object
            boolean bReadable = false;
            for (int i=0;i<matchedVec.size();i++) {
                vObj = (ViewKmObject) matchedVec.elementAt(i);
                objID = new Long(vObj.dbObject.obj_bob_nod_id);
                publishedVec = (Vector) domainsHash.get(objID);
                if (publishedVec != null) {
                    bReadable = false;
                    for(int j=0;j<publishedVec.size();j++) {
                        if (!readableVec.contains((Long) publishedVec.elementAt(j))) {
                            // Remove the unreadable domains of the object
                            publishedVec.removeElementAt(j);
                            j--;
                        }
                    }
                    domainsHash.put(objID, publishedVec);
                }
            }

        }

        if (!useSession) {
            cwPage.ts = cwSQL.getTime(con);
            sess.setAttribute(KMModule.SESS_KM_CHILD_OBJECTS_TS, cwPage.ts);
            sess.setAttribute(KMModule.SESS_KM_CHILD_OBJECTS_VEC, matchedVec);
        }
        
        cwPage.totalRec  = matchedVec.size();
        cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec/(float)cwPage.pageSize);
        int begin=(cwPage.curPage-1)*cwPage.pageSize;
        int end=begin+cwPage.pageSize;
        if(end>matchedVec.size()) {
            end = matchedVec.size();
        }

        StringBuffer xml = new StringBuffer();
        xml.append("<search_result_list>");
        xml.append(cwPage.asXML());
        Vector ancestorXMLVec = null;
        for (int i=0;i<matchedVec.size();i++) {
            vObj = (ViewKmObject) matchedVec.elementAt(i);
            objID = new Long(vObj.dbObject.obj_bob_nod_id);
            publishedVec = (Vector) domainsHash.get(objID);
            ancestorXMLVec = new Vector();
            if (publishedVec != null) {
                for (int j=0;j<publishedVec.size();j++) {
                    String ancestorXML = (String) ancestorXMLHash.get((Long)publishedVec.elementAt(j));
                    if (ancestorXML != null) {
                        ancestorXMLVec.addElement(ancestorXML);
                    }
                }
            }
            xml.append(KMFolderManager.objAsXML(vObj, ancestorXMLVec));
        }
        xml.append("</search_result_list>");
        return xml.toString();
    }    
    
    /**
    Get major version of a given object version
    @param an object version
    @return major version of the input argument
    */
    private String getMajorVersion(String version) {
        return version.substring(0, version.indexOf('.'));
    }

    /**
    Get minor version of a given object version
    @param an object version
    @return minor version of the input argument
    */
    private String getMinorVersion(String version) {
        return version.substring(version.indexOf('.') + 1);
    }

    /**
    Get next version of a given object version by incresing minor version by 1
    @param an object version
    @return next version of the input argument
    */
    private String getNextVersion(String version) {
        String major = getMajorVersion(version);
        String minor = getMinorVersion(version);
        return major + '.' + (Long.parseLong(minor) + 1);
    }

    /**
    Get next publiched version of a given object version by incresing major version by 1 and reset minor version to 0
    @param an object version
    @return next published version of the input argument
    */
    public String getNextPublishedVersion(String version) {
        
        String major = getMajorVersion(version);
        String minor = getMinorVersion(version);
        return (Long.parseLong(major)+1) + ".0";
    }

    /**
    Generate an itm_xml for this item based on its item tempalte and argument data.
    Pre-defined variable: itm_id (0 if you want to generate itm_xml for a new item),
                          itm_owner_ent_id,
                          itm_type
    @param con Connnection to database
    @param data Hashtable contains data of this item needed to be constructed into itm_xml. e.g. ("itm_title", "I am Item Title")
    @return itm_xml generated 
    */
    private String updateObjXML(String objXML, aeTemplate tpl, Hashtable data) throws SQLException, cwException {
        if(objXML == null || objXML.length() == 0) {
            objXML = "<detail></detail>";
        }
        Hashtable paramField=null;
        try {
            paramField = tpl.getParamNameFields();
        } catch(IOException e) {
            throw new cwException (e.getMessage());
        }
        String paramname;
        String fieldname;
        String fielddata;
        Enumeration key = paramField.keys();
        Perl5Util perl = new Perl5Util();
        String perlString = new String();
        while(key.hasMoreElements()) {
            paramname = (String) key.nextElement();
            fielddata = (String) data.get(paramname);
            fieldname = (String) paramField.get(paramname);
            if(fieldname != null && fielddata != null) {
                StringBuffer xmlBuf = new StringBuffer(128);
                xmlBuf.append("<").append(fieldname).append(">")
                    .append(fielddata)
                    .append("</").append(fieldname).append(">");
                perlString = "#<" + fieldname + ">(\\w|\\W)*</" + fieldname + ">#i";
                if(perl.match(perlString, objXML)) {
                    perlString = "s#<" + fieldname + ">(\\w|\\W)*</" + fieldname + ">#" + xmlBuf.toString() + "#i";
                } else {
                    perlString = "s#</detail>#" + xmlBuf.toString() + "</detail>#i";
                }
                objXML = perl.substitute(perlString, objXML);
            }
        }
        return objXML;
    }

    /**
    Get the parent workfolder node id if an object
    @param con Connection to database
    @param objId object node id
    @return parent workfolder node id of the object
    */
    public long getObjParentID(Connection con, long objId) throws SQLException {
        return DbKmNode.getParentID(con, objId);
    }


    public void bulidIndex(qdbEnv env, String indexKey, 
                            String filename, ViewKmObject object, 
                            String[] str_domain_id_list) 
        throws cwException {
        //build index for searchEngine
        IndexFiles indexFiles = new IndexFiles(env.SEARCH_DB_DIR);
        indexFiles.setKey(indexKey);

        indexFiles.setObjID(Long.toString(object.dbObject.obj_bob_nod_id));
        indexFiles.setVersion(object.dbObject.obj_version);
        indexFiles.setKeyword(object.dbObject.obj_keywords);
        indexFiles.setTitle(object.dbObject.obj_title);
        indexFiles.setDescription(object.dbObject.obj_desc);
        indexFiles.setTypeID(object.dbObject.obj_type);
        indexFiles.setAuthor(object.dbObject.obj_author);
        indexFiles.setCallNumber(object.dbObject.obj_code);
        try {
            indexFiles.setTemplateData(object.dbObject.obj_xml);
        } catch(Exception e) {
            throw new cwException("KMObjectManager.buildIndex: " + e.getMessage());
        }
        indexFiles.setModifiedDate(Long.toString((object.dbObject.obj_update_timestamp).getTime()));
        if(filename != null) {
            indexFiles.setFilename(filename);
        }

        if(str_domain_id_list != null) {
            indexFiles.setDomain(str_domain_id_list);
        }

        if (env.SEARCH_CONTENT_INDEXING) {
            indexFiles.indexDocument(true);
            if(filename != null) {
                indexFiles.setPath(env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + 
                                object.dbObject.obj_bob_nod_id + dbUtils.SLASH + 
                                object.dbObject.obj_version + dbUtils.SLASH + 
                                filename);
            }
            qdbAction.myIndexFilesScheduler.addIndex(indexFiles);
        }
        else {
            indexFiles.indexDocument(false);
            try {
                indexFiles.addIndex();
            } catch(FileNotFoundException fnfe) {
                throw new cwException("KMObjectManager.buildIndex: " + fnfe.getMessage());
            } catch(UnsupportedEncodingException uee) {
                throw new cwException("KMObjectManager.buildIndex: " + uee.getMessage());
            } catch(IOException ioe) {
                throw new cwException("KMObjectManager.buildIndex: " + ioe.getMessage());
            } 
        }
    }

    public void removeIndex(qdbEnv env, String indexKey) {
        //remove index for searchEngine
        IndexFiles indexFiles = new IndexFiles(env.SEARCH_DB_DIR);                                      
        indexFiles.setKey(indexKey);

        if (env.SEARCH_CONTENT_INDEXING) {
            qdbAction.myIndexFilesScheduler.removeIndex(indexFiles);
        }
        else {
            indexFiles.removeIndex();
        }
    }

    public void moveObject(Connection con, long obj_bob_nod_id, long parent_nod_id) throws cwSysMessage, SQLException {
        DbKmNode node = new DbKmNode();
        node.nod_id = obj_bob_nod_id;
        node.get(con);
        long old_nod_parent_nod_id = node.nod_parent_nod_id;

        node.nod_parent_nod_id = parent_nod_id;
        node.updAncestor(con);

        //update old folder object count
        Vector vFolderId = new Vector();
        vFolderId.addElement(new Long(old_nod_parent_nod_id));
        ViewKmObject viewObject = new ViewKmObject();
        viewObject.updateFolderObjCount(con, -1, vFolderId);

        //update new folder object count
        vFolderId = new Vector();
        vFolderId.addElement(new Long(parent_nod_id));
        viewObject.updateFolderObjCount(con, 1, vFolderId);
    }
}