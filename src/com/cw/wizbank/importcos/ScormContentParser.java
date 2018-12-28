/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999, 2000 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package com.cw.wizbank.importcos;                    

import java.io.*;
import java.sql.*;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.SAXException;

import com.cw.wizbank.util.*;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.*;
import com.cwn.wizbank.utils.CommonLog;

/**
 * A sample DOM filter. This sample program illustrates how to
 * use the Document#getElementsByTagName() method to quickly 
 * and easily locate elements by name.
 *
 * @version $Id: ScormContentParser.java,v 1.6 2000/06/23 05:23:07 jeffreyr Exp $
 */
public class ScormContentParser {

    //
    // Constants
    //

    /** Default parser name. */
    private static final String 
    DEFAULT_PARSER_NAME = "dom.wrappers.DOMParser";
    
    // for SCORM 1.1 CSF
    private static final String ROOT_ELEMENT_NAME = "content";
    private static final String BLOCK_ELEMENT_NAME = "block";
    private static final String SCO_ELEMENT_NAME = "sco";
    private static final String IDENT_ELEMENT_NAME = "identification";
    private static final String TITLE_ELEMENT_NAME = "title";
    private static final String DESC_ELEMENT_NAME = "description";
    private static final String LAUNCH_ELEMENT_NAME = "launch";
    private static final String LOCATION_ELEMENT_NAME = "location";
    private static final String MASTERY_SCORE_ELEMENT_NAME = "masteryScore";
    private static final String TIME_LIMIT_ELEMENT_NAME = "timeLimit";
    private static final String MAX_TIME_ALLOWED_ELEMENT_NAME = "maxTimeAllowed";
    private static final String TIME_LIMIT_ACTION_ELEMENT_NAME = "timeLimitAction";
    
    // for SCORM 1.2 Imsmanifest
    private static final String MANIFEST_ELEMENT_NAME = "manifest";
    private static final String ORGANIZATIONS_ELEMENT_NAME = "organizations";
    private static final String ORGANIZATION_ELEMENT_NAME = "organization";
    private static final String ITEM_ELEMENT_NAME = "item";
    private static final String RESOURCES_ELEMENT_NAME = "resources";
    private static final String RESOURCE_ELEMENT_NAME = "resource";
    private static final String IDENTIFIER_ATTRIBUTE_NAME = "identifier";
    private static final String DEFAULT_ORG_ATTRIBUTE_NAME = "default";
    private static final String IDENTIFIERREF_ATTRIBUTE_NAME = "identifierref";
    private static final String ITEMPARAMETERS_ATTRIBUTE_NAME = "parameters";
    private static final String RESOURCES_XML_BASE_ATTRIBUTE_NAME = "xml:base";
    private static final String SCORM_1_2_LOCATION_ATTRIBUTE_NAME = "href";
    private static final String SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME = "adlcp:masteryScore";
    private static final String SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME = "adlcp:maxTimeAllowed";
    private static final String SCORM_1_2_TIME_LIMIT_ACTION_ELEMENT_NAME = "adlcp:timeLimitAction";
    private static final String SCORM_1_2_DATA_FROM_LMS = "adlcp:datafromlms";
    private static final String MANIFEST_IDENTIFIER = "MANIFEST_IDENTIFIER";

    private static boolean setValidation    = false; //defaults
    private static boolean setNameSpaces    = true;
    private static boolean setSchemaSupport = true;
    private static boolean setDeferredDOM   = true;

	private final static String DEFAULT_ENC = "UnicodeLittle";
    private String cosStructureXML = "";
    
    private Connection con = null;
    private loginProfile prof = null;
    private dbCourse myDbCourse = null;
    
    private boolean boolRootBlock = true;
    private int numOfItem = 0;
    
    private String defaultOrganization = "";
    private String prevManifestIdentifier = "";
    public long mod_id;
    public long first_mod_id = 0;
    String manifestFileUrl ="";
    public String sco_version;
    
    public boolean fromzip = false;
    
    public static final String SCORM_VERSION_1_2 = "1.2";
    public static final String SCORM_VERSION_2004 = "2004";
    
    public String first_mod_title = "";
    /**
     * used for mark the node as the starter of an multi-sco scorm.
     * the mark is constructed as "scoroot_13" . 13 is the first mod id while import the multi-sco scorm.
     */
    public static final String SCOROOT = "scoroot_";
    public static final String STARTMODID = "startmodid";
    
    /**
     * 课程或班级插入的模块ID(res_id)列表
     */
    protected List res_id_list_inserted = null;
    
    //
    // Public static methods
    //
    public ScormContentParser(Connection con, loginProfile prof, dbCourse myDbCourse) {
        this.con = con;
        this.prof = prof;
        this.myDbCourse = myDbCourse;
    }

    /** parse the content of imsmanifest file 
     * @throws cwException */
    public void parseImsmanifest(String uri, String cosUrlPrefix, boolean notCourse, WizbiniLoader wizbini ) throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage, cwException {
        try {                        
            //InputSource in = new InputSource(new FileInputStream(new File(uri)));
			/*
			if( !cwUtils.isUnicodeLittleFile(new File(uri)) ) {
				throw new cwSysMessage("GEN008");
			}
			*/
			InputSource in = new InputSource(new InputStreamReader(cwUtils.openUTF8FileStream(new File(uri)), cwUtils.ENC_UTF));
            // Use a DOMParser from Xerces so we get a complete DOM from the document
            DOMParser parser = new DOMParser();
            parser.parse(in);
            Document document = parser.getDocument();

            // get the root element
            Element element = document.getDocumentElement();
            
            boolRootBlock = true;
            Vector vtOrganization = new Vector();
            Vector vtResources = new Vector();
            prevManifestIdentifier = element.getAttributes().getNamedItem(IDENTIFIER_ATTRIBUTE_NAME).getNodeValue();
            imsParseChildElements(vtOrganization, vtResources, vtOrganization, vtResources, (Node)element, (Node)element);
            
            int position = 0;
            // start constructing the TOC
            if (myDbCourse.cos_structure_xml == null || myDbCourse.cos_structure_xml.equals("")){
                cosStructureXML += "<tableofcontents identifier=\"";
                cosStructureXML += "TOC1";
                cosStructureXML += "\" title=\"";
                cosStructureXML += dbUtils.esc4XML(myDbCourse.res_title);
                cosStructureXML += "\">" + dbUtils.NEWL;
                position = cosStructureXML.length();
            }else{
            	position = myDbCourse.cos_structure_xml.indexOf("</tableofcontents>");
            	cosStructureXML += myDbCourse.cos_structure_xml.substring(0, position);
            }
//            if( SCORM_VERSION_2004.equals(sco_version)){
//                if(vtOrganization.size() > 0)
//                    processOrganization2004((Vector)vtOrganization.elementAt(0), vtResources, cosUrlPrefix, notCourse, uri, wizbini );
//            }else{
                for (int i=0; i<vtOrganization.size(); i++) {
                  //add a virtual folder for the multi-sco
                    processOrganizationVector((Vector)vtOrganization.elementAt(i), vtResources, cosUrlPrefix, notCourse, uri, wizbini );
                }
           // }
           /* if (!notCourse && numOfItem > 1) {
                cosStructureXML = cosStructureXML.substring(0, position) + "<item identifier=\""+SCOROOT+first_mod_id + "\" title=\"" + dbUtils.esc4XML(first_mod_title) + "\"><itemtype>FDR</itemtype>"
                                    + cosStructureXML.substring(position);
                cosStructureXML += "</item>";
            }*/
            // end the consturction of TOC
            cosStructureXML += "</tableofcontents>";                

            myDbCourse.cos_structure_xml = cosStructureXML;
//System.out.println("myDbCourse.cos_structure_xm:" + myDbCourse.cos_structure_xml);    
            if (con != null) {
                if (!notCourse) {
    	            myDbCourse.updCosStructureNoTimestampCheck(con, prof);
    	            myDbCourse.updAiccCos(con, prof, "", "2.2", "", -1, true, true, true);
                } else {
                	dbScormResource srs = new dbScormResource();
                	srs.srs_res_id = myDbCourse.cos_res_id;
                	srs.srs_structure_xml = cosStructureXML;
                	srs.srs_aicc_version = "2.2";
                	srs.srs_vendor = "";
                	srs.srs_max_normal = -1;
                	srs.ins(con);
                }
            }
        } catch (SAXException e) {
            throw new qdbException(e.getMessage());
        }
    }
    
    public void processOrganizationVector(Vector vtChildItem, Vector vtResources, String cosUrlPrefix, boolean notCourse,String manifestFileUri,WizbiniLoader wizbini ) throws IOException, SQLException, qdbException, qdbErrMessage {
        Hashtable htChildItem = (Hashtable)vtChildItem.elementAt(0);
        
/*        
System.out.println(IDENTIFIER_ATTRIBUTE_NAME + ":" + htChildItem.get(IDENTIFIER_ATTRIBUTE_));
System.out.println(IDENTIFIERREF_ATTRIBUTE_NAME + ":" + htChildItem.get(IDENTIFIERREF_ATTRIBUTE_NAME));
System.out.println(TITLE_ELEMENT_NAME + ":" + htChildItem.get(TITLE_ELEMENT_NAME));
System.out.println(SCORM_1_2_TIME_LIMIT_ACTION_ELEMENT_NAME + ":" + htChildItem.get(SCORM_1_2_TIME_LIMIT_ACTION_ELEMENT_NAME));
        if (htChildItem.get(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME) != null) {
System.out.println(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME + ":" + ((Integer)htChildItem.get(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME)).intValue());
        }
        if (htChildItem.get(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME) != null) {
System.out.println(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME + ":" + ((Double)htChildItem.get(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME)).doubleValue());
        }
System.out.println(SCORM_1_2_DATA_FROM_LMS + ":" + htChildItem.get(SCORM_1_2_DATA_FROM_LMS));
*/
        
        numOfItem++;
        
        // folder
        if (htChildItem.get(IDENTIFIERREF_ATTRIBUTE_NAME) == null) {
            cosStructureXML += "<item identifier=\"ITEM" + Integer.toString(numOfItem) + "\" title=\"" + dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)) + "\">";
            cosStructureXML += dbUtils.NEWL; 
            cosStructureXML += "<itemtype>FDR</itemtype>";
            cosStructureXML += dbUtils.NEWL; 
        }
        // leaf node
        else {
            String location = null;
            String identifier = htChildItem.get(IDENTIFIER_ATTRIBUTE_NAME).toString() ;
            
            int mod_id = -1;
            for (int i=0; i<vtResources.size(); i++) {
                Hashtable htResource = (Hashtable)vtResources.elementAt(i);
                if (htChildItem.get(IDENTIFIERREF_ATTRIBUTE_NAME).equals(htResource.get(IDENTIFIER_ATTRIBUTE_NAME)) == true
                    || htChildItem.get(IDENTIFIERREF_ATTRIBUTE_NAME).equals(htResource.get(MANIFEST_IDENTIFIER)) == true) {
                    /*
                    if (cosUrlPrefix != null && cosUrlPrefix.length() > 0) {
                        location = cosUrlPrefix + (String)htResource.get(SCORM_1_2_LOCATION_ATTRIBUTE_NAME);
                    }
                    else {
                        location = (String)htResource.get(SCORM_1_2_LOCATION_ATTRIBUTE_NAME);
                    }
                    */
                    location = (String)htResource.get(SCORM_1_2_LOCATION_ATTRIBUTE_NAME);               
                    break;
                }
            }
            //add parameter attribute got from manifest file
//            if (SCORM_VERSION_2004.equals(sco_version)) {
//            	String parameters = (String) htChildItem.get(ITEMPARAMETERS_ATTRIBUTE_NAME);
//            	if (parameters != null && parameters.startsWith("?")) {
//            		location += parameters;
//            	}
//            }
            dbModule myDbModule = new dbModule();
            // set the default language to "ISO-8859-1" and difficulty to normal
            myDbModule.res_lan = "ISO-8859-1";
            myDbModule.res_difficulty = 2;
            
            if (htChildItem.get(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME) != null) {
                myDbModule.res_duration = ((Integer)htChildItem.get(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME)).intValue();
            }
            myDbModule.res_title = (String)htChildItem.get(TITLE_ELEMENT_NAME);
            myDbModule.res_type = "MOD";
            myDbModule.res_sco_version = sco_version;
            if (htChildItem.get(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME) != null) {
                myDbModule.mod_pass_score = (float)((Double)htChildItem.get(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME)).doubleValue();
                if(myDbModule.mod_pass_score > 0){
                	if(myDbModule.mod_pass_score > 100){
                		myDbModule.mod_max_score = myDbModule.mod_pass_score;
                	}else{
                		myDbModule.mod_max_score = 100;
                	}
                	
                }
            }
            if (con != null) {
                String isEnrollmentRelated = myDbCourse.getIsEnrollmentRelated(con);
                if (isEnrollmentRelated.equals(dbResource.ENROLLMENT_RELATED_ALL)) {
                    myDbModule.res_status = dbResource.RES_STATUS_ON;
                } else {
                    myDbModule.res_status = dbResource.RES_STATUS_OFF;
                }
                if (!notCourse) {
    	            myDbModule.mod_type = "SCO";
    	//            myDbModule.mod_type = "AICC_AU";
    	            myDbModule.res_subtype = myDbModule.mod_type;
    	            myDbModule.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
    	            myDbModule.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
    	            myDbModule.res_src_type = "MANIFEST_FILE";
    	            myDbModule.res_src_link = location;
    	            myDbModule.res_upd_user = prof.usr_id;      
    	            myDbModule.res_usr_id_owner = prof.usr_id;
    	
    	            mod_id = (int)myDbCourse.insModule(con, myDbModule, null, prof);
    	            if( SCORM_VERSION_2004.equals(sco_version)&& manifestFileUri != null && manifestFileUri.length() >0 && wizbini != null ){
    	                if(first_mod_id == 0){
        	                File file = new File(manifestFileUri);
        	                String file_name = "SCOM_MOD_"+mod_id+"_"+file.getName();
        	                File file_folder = new File(wizbini.getFileUploadResDirAbs());
        	                if(!file_folder.exists()){
        	                    file_folder.mkdirs();
        	                }
        	                File des_file = new File(wizbini.getFileUploadResDirAbs() ,file_name);
        	                dbUtils.copyFile( file,des_file);
                            String url = wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl();
                            manifestFileUrl = cwUtils.getFileURL(url)  + file_name;
                            
                            
                            
    	                }
                        
                        if (first_mod_id == 0) {
                            first_mod_id = mod_id;
                        }
                        String tepm = manifestFileUrl;
                        String parameters = (String) htChildItem.get(ITEMPARAMETERS_ATTRIBUTE_NAME);
                        if (parameters != null && parameters.startsWith("?")) {
                            tepm += parameters;
                        }
                        
                        myDbCourse.updSrcLing( con, mod_id, tepm,first_mod_id,identifier) ;
                        location  = manifestFileUrl;
                    }
    	            this.setResIdListInserted(mod_id);
    	            this.mod_id = mod_id;
    	            myDbModule.updAicc(con, prof, "", "", "", (String)htChildItem.get(SCORM_1_2_TIME_LIMIT_ACTION_ELEMENT_NAME), (String)htChildItem.get(SCORM_1_2_DATA_FROM_LMS), "", (String)htChildItem.get(TITLE_ELEMENT_NAME), "2.2");
                }else{
                    if( SCORM_VERSION_2004.equals(sco_version)&& manifestFileUri != null && manifestFileUri.length() >0 && wizbini != null ){
                          File file = new File(manifestFileUri);
                          String file_name = "SCOM_MOD_"+myDbCourse.cos_res_id+"_"+file.getName();
                          File des_file = new File(wizbini.getFileUploadResDirAbs() ,file_name);
                          dbUtils.copyFile( file,des_file);
                          String url = wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl();
                          String manifestFileUrl = cwUtils.getFileURL(url)  + file_name;
                          location = manifestFileUrl;
                          myDbCourse.updSrcLing( con, myDbCourse.cos_res_id, manifestFileUrl,0,null) ;
                    }
                  
              }
            }
            /*if (first_mod_id == 0) {
                first_mod_id = mod_id;
            }*/
                    
            cosStructureXML += "<item identifier=\"ITEM" + Integer.toString(numOfItem) + "\" identifierref=\"" + Integer.toString((int)mod_id) + "\" title=\"" + dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)) + "\">";
            cosStructureXML += dbUtils.NEWL; 
            cosStructureXML += "<itemtype>MOD</itemtype>";
            cosStructureXML += dbUtils.NEWL; 
            cosStructureXML += "<restype>" + myDbModule.mod_type + "</restype>";
            cosStructureXML += dbUtils.NEWL; 
            if (notCourse) {
            	cosStructureXML += "<src_link>" + location + "</src_link>" + dbUtils.NEWL;
            }
            cosStructureXML += "</item>";
            cosStructureXML += dbUtils.NEWL; 
        }
        if (numOfItem == 1) {
            first_mod_title = (String)htChildItem.get(TITLE_ELEMENT_NAME);
        }
//System.out.println("start --- dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)):" + dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)));            
//System.out.println("total:" + vtChildItem.size());            
                    
        for (int j=1; j<vtChildItem.size(); j++) {
//System.out.println("index:" + j + " of " + dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)));            
            processOrganizationVector((Vector)vtChildItem.elementAt(j), vtResources, cosUrlPrefix, notCourse, manifestFileUri, wizbini);
        }
//System.out.println("end --- dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)):" + dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)));                        
        
        // folder
        if (htChildItem.get(IDENTIFIERREF_ATTRIBUTE_NAME) == null) {
            cosStructureXML += "</item>";
            cosStructureXML += dbUtils.NEWL; 
        }
        
    }
    
    
//    public void processOrganization2004(Vector vtChildItem, Vector vtResources, String cosUrlPrefix, boolean notCourse,String manifestFileUri,WizbiniLoader wizbini ) throws IOException, SQLException, qdbException, qdbErrMessage {
//        Hashtable htChildItem = (Hashtable)vtChildItem.elementAt(0);
//        
//
//        
//        numOfItem++;
//    
//     
//            
//            int mod_id = -1;
//         
//          String location = "";
//            dbModule myDbModule = new dbModule();
//            // set the default language to "ISO-8859-1" and difficulty to normal
//            myDbModule.res_lan = "ISO-8859-1";
//            myDbModule.res_difficulty = 2;
//            
//            if (htChildItem.get(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME) != null) {
//                myDbModule.res_duration = ((Integer)htChildItem.get(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME)).intValue();
//            }
//            myDbModule.res_title = (String)htChildItem.get(TITLE_ELEMENT_NAME);
//            myDbModule.res_type = "MOD";
//            myDbModule.res_sco_version = sco_version;
//            if (htChildItem.get(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME) != null) {
//                myDbModule.mod_pass_score = (float)((Double)htChildItem.get(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME)).doubleValue();
//            }
//            if (con != null) {
//                String isEnrollmentRelated = myDbCourse.getIsEnrollmentRelated(con);
//                if (isEnrollmentRelated.equals(dbResource.ENROLLMENT_RELATED_ALL)) {
//                    myDbModule.res_status = dbResource.RES_STATUS_ON;
//                } else {
//                    myDbModule.res_status = dbResource.RES_STATUS_OFF;
//                }
//                if (!notCourse) {
//                    myDbModule.mod_type = "SCO";
//        //            myDbModule.mod_type = "AICC_AU";
//                    myDbModule.res_subtype = myDbModule.mod_type;
//                    myDbModule.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
//                    myDbModule.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
//                    myDbModule.res_src_type = "MANIFEST_FILE";
//                    myDbModule.res_src_link = "";
//                    myDbModule.res_upd_user = prof.usr_id;      
//                    myDbModule.res_usr_id_owner = prof.usr_id;
//        
//                    mod_id = (int)myDbCourse.insModule(con, myDbModule, null, prof);
//               
//                    File file = new File(manifestFileUri);
//                    
//                    String file_name = "SCOM_MOD_"+mod_id+"_"+file.getName();
//                    File des_file = new File(wizbini.getFileUploadResDirAbs() ,file_name);
//                    dbUtils.copyFile( file,des_file);
//                    String url = wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl();
//                    String manifestFileUrl = cwUtils.getFileURL(url)  + file_name;
//                    
//                    String parameters = (String) htChildItem.get(ITEMPARAMETERS_ATTRIBUTE_NAME);
//                    if (parameters != null && parameters.startsWith("?")) {
//                        manifestFileUrl += parameters;
//                    }
//                    location = manifestFileUrl;
//                    myDbCourse.updSrcLing( con, mod_id, manifestFileUrl) ;
//                   
//                    this.setResIdListInserted(mod_id);
//                    this.mod_id = mod_id;
//                    myDbModule.updAicc(con, prof, "", "", "", (String)htChildItem.get(SCORM_1_2_TIME_LIMIT_ACTION_ELEMENT_NAME), (String)htChildItem.get(SCORM_1_2_DATA_FROM_LMS), "", (String)htChildItem.get(TITLE_ELEMENT_NAME), "2.2");
//                }else{
//                    
//                    File file = new File(manifestFileUri);
//                    String file_name = "SCOM_MOD_"+myDbCourse.cos_res_id+"_"+file.getName();
//                    File des_file = new File(wizbini.getFileUploadResDirAbs() ,file_name);
//                    dbUtils.copyFile( file,des_file);
//                    String url = wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl();
//                    String manifestFileUrl = cwUtils.getFileURL(url)  + file_name;
//                    
//                    String parameters = (String) htChildItem.get(ITEMPARAMETERS_ATTRIBUTE_NAME);
//                    if (parameters != null && parameters.startsWith("?")) {
//                        manifestFileUrl += parameters;
//                    }
//                    
//                    location = manifestFileUrl;
//                    myDbCourse.updSrcLing( con, myDbCourse.cos_res_id, manifestFileUrl) ;
//                    
//                }
//            }
//            if (first_mod_id == 0) {
//                first_mod_id = mod_id;
//            }
//                    
//            cosStructureXML += "<item identifier=\"ITEM" + Integer.toString(numOfItem) + "\" identifierref=\"" + Integer.toString((int)mod_id) + "\" title=\"" + dbUtils.esc4XML((String)htChildItem.get(TITLE_ELEMENT_NAME)) + "\">";
//            cosStructureXML += dbUtils.NEWL; 
//            cosStructureXML += "<itemtype>MOD</itemtype>";
//            cosStructureXML += dbUtils.NEWL; 
//            cosStructureXML += "<restype>" + myDbModule.mod_type + "</restype>";
//            cosStructureXML += dbUtils.NEWL; 
//            if (notCourse) {
//                cosStructureXML += "<src_link>" + location + "</src_link>" + dbUtils.NEWL;
//            }
//            cosStructureXML += "</item>";
//            cosStructureXML += dbUtils.NEWL; 
//
//        if (numOfItem == 1) {
//            first_mod_title = (String)htChildItem.get(TITLE_ELEMENT_NAME);
//        }
//          
//   
//        // folder
////        if (htChildItem.get(IDENTIFIERREF_ATTRIBUTE_NAME) == null) {
////            cosStructureXML += "</item>";
////            cosStructureXML += dbUtils.NEWL; 
////        }
//        
//    }
    
    /**
     * 将课程或班级的模块ID，添加到成员变量中
     * @param resId
     */
    private void setResIdListInserted(long resId) {
    	if(this.res_id_list_inserted == null) {
    		this.res_id_list_inserted = new ArrayList();
    	}
    	this.res_id_list_inserted.add(new Long(resId));
    }
    
    /**
     * 获取已添加的模块ID列表 
     */
    protected List getResIdListInserted() {
    	return this.res_id_list_inserted;
    }
    
    private void imsParseChildElements(Vector vtOrganization, Vector vtResources, Vector vtParentItem, Vector vtParentResource, Node parent, Node root) throws IOException, SQLException, qdbException, qdbErrMessage {
        String identifier = "";
        String identifierref = "";
        String xml_base = "";

        String title = "";
        String desc = "";
        String launchURL = "";
        double masteryScore = 0.0;
        String timeLimitAction = "";
        String maxTimeAllowed = "";
        int intMaxTimeAllowedInMin = 0;
        
        boolean boolOrganizations = false;
        boolean boolOrganization = false;
        boolean boolResources = false;
        boolean boolItem = false;
        boolean boolResource = false;
        boolean boolManifest = false;
        
        // is there anything to do?
        if (parent == null) {
            return;
        }

        NodeList elements = parent.getChildNodes();
        
        int elementCount = elements.getLength();
        for (int i = 0; i < elementCount; i++) {
            // default parent item
            Vector vtNewItem = vtParentItem;
            
            // initializes the variables
            boolManifest = false;
            boolOrganizations = false;
            boolOrganization = false;
            boolResources = false;
            boolItem = false;
            boolResource = false;
            
            Node element = (Node)elements.item(i);
            if (element.getNodeName().equalsIgnoreCase(MANIFEST_ELEMENT_NAME) == true) {
                //System.out.println("Organization~~~~~~~");
                boolManifest = true;
            }
            else if (element.getNodeName().equalsIgnoreCase(ORGANIZATIONS_ELEMENT_NAME) == true) {
                //System.out.println("Organization~~~~~~~");
                boolOrganizations = true;
            }
            else if (element.getNodeName().equalsIgnoreCase(ORGANIZATION_ELEMENT_NAME) == true) {
                //System.out.println("Organization~~~~~~~");
                boolOrganization = true;
            }
            else if (element.getNodeName().equalsIgnoreCase(RESOURCES_ELEMENT_NAME) == true) {
                //System.out.println("Resources~~~~~~~");
                boolResources = true;
            }
            else if (element.getNodeName().equalsIgnoreCase(ITEM_ELEMENT_NAME) == true) {
                //System.out.println("Item~~~~~~~");
                boolItem = true;
            }
            else if (element.getNodeName().equalsIgnoreCase(RESOURCE_ELEMENT_NAME) == true) {
                //System.out.println("Resource~~~~~~~");
                boolResource = true;
            }
            else {
                // do nothing
                //System.out.println("Un-matched~~~~~~~");
            }
            
            NodeList childElements = element.getChildNodes();
            int childElementCount = childElements.getLength();
                
            Node tempElement = element;

            if (boolManifest) {
                prevManifestIdentifier = tempElement.getAttributes().getNamedItem(IDENTIFIER_ATTRIBUTE_NAME).getNodeValue();
            }
            else if (boolOrganizations) {
                if (tempElement.getAttributes().getNamedItem(DEFAULT_ORG_ATTRIBUTE_NAME) != null) {
                    defaultOrganization = tempElement.getAttributes().getNamedItem(DEFAULT_ORG_ATTRIBUTE_NAME).getNodeValue();
                }
            }
            else if (boolOrganization) {
                identifier = tempElement.getAttributes().getNamedItem(IDENTIFIER_ATTRIBUTE_NAME).getNodeValue();
//                if( SCORM_VERSION_2004.equals(sco_version)){
//                    vtNewItem = new Vector();
//                    vtParentItem.addElement(vtNewItem);
//                        
//                    Hashtable htNewItem = new Hashtable();
//                    vtNewItem.addElement(htNewItem);
//                    htNewItem.put(IDENTIFIER_ATTRIBUTE_NAME, identifier);
//                    NodeList childChildElements = tempElement.getChildNodes();
//                    int childChildElementCount = childChildElements.getLength();
//                    for (int k = 0; k < childChildElementCount; k++) {
//                        Node tempElement2 = (Node)childChildElements.item(k);                            
//                        if (tempElement2.getNodeName().equalsIgnoreCase(TITLE_ELEMENT_NAME) == true) {
//                            if (tempElement2.getChildNodes().getLength() >= 1) {
//                                title = tempElement2.getFirstChild().getNodeValue();
//                                if (title != null) {
//                                    title = normalize(title);
//                                }
//                            }
//                            else {
//                                title = "";
//                            }
//                                    
//                            htNewItem.put(TITLE_ELEMENT_NAME, title);
//                            //System.out.println("title:" + title);
//                        }
//                    }
//                    
//                }else{
                    if (defaultOrganization == null || defaultOrganization.equalsIgnoreCase("") == true || identifier.equals(defaultOrganization) == true) {
                        // matched organization, continue the looping
                    }
                    else {
                        // ignore the um-matched organization
                        continue;
                    }
               // }
            }
            else if (boolItem) {
                vtNewItem = new Vector();
                vtParentItem.addElement(vtNewItem);
                    
                Hashtable htNewItem = new Hashtable();
                vtNewItem.addElement(htNewItem);
                    
                identifier = tempElement.getAttributes().getNamedItem(IDENTIFIER_ATTRIBUTE_NAME).getNodeValue();
//ystem.out.println("identifier:" + identifier);                
                htNewItem.put(IDENTIFIER_ATTRIBUTE_NAME, identifier);
                    
                if (tempElement.getAttributes().getNamedItem(IDENTIFIERREF_ATTRIBUTE_NAME) == null) {
                    identifierref = null;
                }
                else {
                    identifierref = tempElement.getAttributes().getNamedItem(IDENTIFIERREF_ATTRIBUTE_NAME).getNodeValue();
                }
//System.out.println("identifierref:" + identifierref);
                if (identifierref != null) {
                    htNewItem.put(IDENTIFIERREF_ATTRIBUTE_NAME, identifierref);
                }
                    
                if (tempElement.getAttributes().getNamedItem(ITEMPARAMETERS_ATTRIBUTE_NAME) != null) {
                	htNewItem.put(ITEMPARAMETERS_ATTRIBUTE_NAME, tempElement.getAttributes().getNamedItem(ITEMPARAMETERS_ATTRIBUTE_NAME).getNodeValue());
                }
                NodeList childChildElements = tempElement.getChildNodes();
                int childChildElementCount = childChildElements.getLength();
                for (int k = 0; k < childChildElementCount; k++) {
                    Node tempElement2 = (Node)childChildElements.item(k);                            
                    if (tempElement2.getNodeName().equalsIgnoreCase(TITLE_ELEMENT_NAME) == true) {
                        if (tempElement2.getChildNodes().getLength() >= 1) {
                            title = tempElement2.getFirstChild().getNodeValue();
                            if (title != null) {
                                title = normalize(title);
                            }
                        }
                        else {
                            title = "";
                        }
                                
                        htNewItem.put(TITLE_ELEMENT_NAME, title);
                        //System.out.println("title:" + title);
                    }
                    else if (tempElement2.getNodeName().equalsIgnoreCase(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME) == true) {
                        if (tempElement2.getChildNodes().getLength() >= 1) {
                            maxTimeAllowed = tempElement2.getFirstChild().getNodeValue();
                            if (maxTimeAllowed != null) {
                                maxTimeAllowed = normalize(maxTimeAllowed);
                            }
                        }
                        else {
                            maxTimeAllowed = "";
                        }

                        if (maxTimeAllowed != null && maxTimeAllowed.length() > 0) {
                            int hr = 0;
                            int min = 0;
                            int sec = 0;
                            String strTime = null;
                            StringTokenizer stTime = new StringTokenizer(maxTimeAllowed,":.");
                                    
                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            hr = Integer.parseInt(strTime);
                                    
                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            min = Integer.parseInt(strTime);

                            // ignore the decimal second
                            strTime = stTime.nextToken();
                            strTime = strTime.trim();
                            sec = Integer.parseInt(strTime);
                                    
                            intMaxTimeAllowedInMin = hr*60 + min + sec/60;                            
                        }
                        else {
                            intMaxTimeAllowedInMin = 0;
                        }
                                
                        htNewItem.put(SCORM_1_2_MAX_TIME_ALLOWED_ELEMENT_NAME, new Integer(intMaxTimeAllowedInMin));
                        //System.out.println("maxTimeAllowed:" + maxTimeAllowed);
                        //System.out.println("intMaxTimeAllowedInMin:" + intMaxTimeAllowedInMin);
                    }
                    else if (tempElement2.getNodeName().equalsIgnoreCase(SCORM_1_2_TIME_LIMIT_ACTION_ELEMENT_NAME) == true) {
                        if (tempElement2.getChildNodes().getLength() >= 1) {
                            timeLimitAction = tempElement2.getFirstChild().getNodeValue();
                            if (timeLimitAction != null) {
                                timeLimitAction = normalize(timeLimitAction);
                            }
                        }
                        else {
                            timeLimitAction = "";
                        }
                                                            
                        htNewItem.put(SCORM_1_2_TIME_LIMIT_ACTION_ELEMENT_NAME, timeLimitAction);
                        //System.out.println("timeLimitAction:" + timeLimitAction);
                    }
                    else if (tempElement2.getNodeName().equalsIgnoreCase(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME) == true) {
                        String strMasteryScore = null;
                            
                        if (tempElement2.getChildNodes().getLength() >= 1) {
                            strMasteryScore = tempElement2.getFirstChild().getNodeValue();
                            if (strMasteryScore != null) {
                                strMasteryScore = normalize(strMasteryScore);
                            }
                        }
                        else {
                            strMasteryScore = "";
                        }
                            
                        if (strMasteryScore != null && strMasteryScore.length() > 0) {
                            masteryScore = Double.parseDouble(strMasteryScore);                                
                        }
                        else {
                            masteryScore = 0.0;
                        }
                                
                        htNewItem.put(SCORM_1_2_MASTERY_SCORE_ELEMENT_NAME, new Double(masteryScore));
                        //System.out.println("masteryScore:" + masteryScore);
                    }
                    else if (tempElement2.getNodeName().equalsIgnoreCase(SCORM_1_2_DATA_FROM_LMS) == true) {
                        String dataFromLMS = "";
                        
                        if (tempElement2.getChildNodes().getLength() >= 1) {
                            dataFromLMS = tempElement2.getFirstChild().getNodeValue();
                        }
                        else {
                            dataFromLMS = "";
                        }
                                                            
                        htNewItem.put(SCORM_1_2_DATA_FROM_LMS, dataFromLMS);
                        //System.out.println("dataFromLMS:" + dataFromLMS);
                    }
                }
                    
            }
            else if (boolResource) {
                Hashtable htNewResource = new Hashtable();
                vtResources.addElement(htNewResource);
                
                identifier = tempElement.getAttributes().getNamedItem(IDENTIFIER_ATTRIBUTE_NAME).getNodeValue();
                htNewResource.put(IDENTIFIER_ATTRIBUTE_NAME, identifier);

                htNewResource.put(MANIFEST_IDENTIFIER, prevManifestIdentifier);
                
                if ( tempElement.getAttributes().getNamedItem(IDENTIFIER_ATTRIBUTE_NAME) != null) {
                	xml_base =  tempElement.getAttributes().getNamedItem(IDENTIFIER_ATTRIBUTE_NAME).getNodeValue();
                	if (xml_base != null) {
                		htNewResource.put(RESOURCES_XML_BASE_ATTRIBUTE_NAME, xml_base);
                	}
                }
                
                if (tempElement.getAttributes().getNamedItem(SCORM_1_2_LOCATION_ATTRIBUTE_NAME) == null) {
                    launchURL = null;
                }
                else {
                    launchURL = tempElement.getAttributes().getNamedItem(SCORM_1_2_LOCATION_ATTRIBUTE_NAME).getNodeValue();
                }
                
                if (launchURL != null) {
                    htNewResource.put(SCORM_1_2_LOCATION_ATTRIBUTE_NAME, launchURL);
                }
            }
                
            imsParseChildElements(vtOrganization, vtResources, vtNewItem, vtResources, tempElement, root);
            
        }
    }
    

    /** parese the content of CSF file */
    public void parseCSF(String uri) throws qdbErrMessage{

        try {                        
            //InputSource in = new InputSource(new FileInputStream(new File(uri)));
            /*
			if( !cwUtils.isUnicodeLittleFile(new File(uri)) ) {
				throw new qdbErrMessage("GEN008");
			} 
			*/           
			InputSource in = new InputSource(new InputStreamReader(new FileInputStream(new File(uri)), cwUtils.ENC_UTF));
            // Use a DOMParser from Xerces so we get a complete DOM from the document
            DOMParser parser = new DOMParser();
            parser.parse(in);
            Document document = parser.getDocument();

            // get the root element
            Element element = document.getDocumentElement();
            
            boolRootBlock = true;
            csfParseChildElements((Node)element);
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
        }
    }
    
    private void csfParseChildElements(Node parent) throws IOException, SQLException, qdbException, qdbErrMessage, cwException {
        boolean boolSCO = false;
        boolean boolBlock = false;
        String title = "";
        String desc = "";
        String launchURL = "";
        double masteryScore = 0.0;
        String timeLimitAction = "";
        String maxTimeAllowed = null;
        int intMaxTimeAllowedInMin = 0;

        // is there anything to do?
        if (parent == null) {
            return;
        }

        NodeList elements = parent.getChildNodes();
        int elementCount = elements.getLength();
        for (int i = 0; i < elementCount; i++) {
            // initializes the variables
            boolSCO = false;
            boolBlock = false;
            
            title = "";
            desc = "";
            launchURL = "";
            masteryScore = 0.0;
            timeLimitAction = "";
            maxTimeAllowed = null;
            intMaxTimeAllowedInMin = 0;
            
            Node element = (Node)elements.item(i);
            if (element.getNodeName().equalsIgnoreCase(BLOCK_ELEMENT_NAME) == true) {
                //System.out.println("Block~~~~~~~");
                boolBlock = true;
                numOfItem++;
            }
            else if (element.getNodeName().equalsIgnoreCase(SCO_ELEMENT_NAME) == true) {
                //System.out.println("SCO~~~~~~~");
                boolSCO = true;
                numOfItem++;
            }
            
            if (boolSCO == true || boolBlock == true) {
                NodeList childElements = element.getChildNodes();
                int childElementCount = childElements.getLength();
                
                for (int j = 0; j < childElementCount; j++) {
                    Node tempElement = (Node)childElements.item(j);
                    if (tempElement.getNodeName().equalsIgnoreCase(IDENT_ELEMENT_NAME) == true) {
                        NodeList childChildElements = tempElement.getChildNodes();
                        int childChildElementCount = childChildElements.getLength();
                        for (int k = 0; k < childChildElementCount; k++) {
                            Node tempElement2 = (Node)childChildElements.item(k);                            
                            if (tempElement2.getNodeName().equalsIgnoreCase(TITLE_ELEMENT_NAME) == true) {
                                if (tempElement2.getChildNodes().getLength() >= 1) {
                                    title = tempElement2.getFirstChild().getNodeValue();
                                    if (title != null) {
                                        title = normalize(title);
                                    }
                                }
                                else {
                                    title = "";
                                }
                                
                                //System.out.println("title:" + title);
                            }
                            if (tempElement2.getNodeName().equalsIgnoreCase(DESC_ELEMENT_NAME) == true) {
                                if (tempElement2.getChildNodes().getLength() >= 1) {
                                    desc = tempElement2.getFirstChild().getNodeValue();
                                    if (desc != null) {
                                        desc = normalize(desc);
                                    }
                                }
                                else {
                                    desc = "";
                                }
                                
                                //System.out.println("desc:" + desc);
                            }
                        }
                    }
                    
                    if (boolSCO == true) {
                        // get the launch url
                        if (tempElement.getNodeName().equalsIgnoreCase(LAUNCH_ELEMENT_NAME) == true) {
                            Node launchLocNode = retrieveChildNode(tempElement, LOCATION_ELEMENT_NAME);
                            if (launchLocNode.getChildNodes().getLength() >= 1) {
                                launchURL = launchLocNode.getFirstChild().getNodeValue();
                                if (launchURL != null) {
                                    launchURL = normalize(launchURL);
                                }
                                else {
                                    launchURL = "";
                                }
                                //System.out.println("launchURL:" + launchURL);
                            }
                        }
                        
                        // get the mastery score
                        if (tempElement.getNodeName().equalsIgnoreCase(MASTERY_SCORE_ELEMENT_NAME) == true) {
                            if (tempElement.getChildNodes().getLength() >= 1) {
                                String strScore = "";
                                strScore = tempElement.getFirstChild().getNodeValue();
                                if (strScore != null) {
                                    masteryScore = Double.parseDouble(strScore);
                                }
                                else {
                                    masteryScore = 0.0;
                                }
                                //System.out.println("masteryScore:" + masteryScore);
                            }
                        }

                        // get the time limit elements
                        if (tempElement.getNodeName().equalsIgnoreCase(TIME_LIMIT_ELEMENT_NAME) == true) {
                            Node maxTimeAllowedNode = retrieveChildNode(tempElement, MAX_TIME_ALLOWED_ELEMENT_NAME);
                            if (maxTimeAllowedNode.getChildNodes().getLength() >= 1) {
                                maxTimeAllowed = maxTimeAllowedNode.getFirstChild().getNodeValue();
                                
                                int hr = 0;
                                int min = 0;
                                int sec = 0;
                                String strTime = null;
                                StringTokenizer stTime = new StringTokenizer(maxTimeAllowed,":.");
                                
                                strTime = stTime.nextToken();
                                strTime = strTime.trim();
                                hr = Integer.parseInt(strTime);
                                
                                strTime = stTime.nextToken();
                                strTime = strTime.trim();
                                min = Integer.parseInt(strTime);

                                // ignore the decimal second
                                strTime = stTime.nextToken();
                                strTime = strTime.trim();
                                sec = Integer.parseInt(strTime);
                                
                                intMaxTimeAllowedInMin = hr*60 + min + sec/60;
                            }
                            Node timeLimitActionNode = retrieveChildNode(tempElement, TIME_LIMIT_ACTION_ELEMENT_NAME);
                            if (timeLimitActionNode.getChildNodes().getLength() >= 1) {
                                timeLimitAction = timeLimitActionNode.getFirstChild().getNodeValue();
                            }
                        }                        
                    }
                }
                
                if (boolBlock == true) {
                    boolean boolWithinRootBlock = false;
                    
                    // construct the starting tag
                    if (boolRootBlock == true) {
                        cosStructureXML += "<tableofcontents identifier=\"";
                        cosStructureXML += "TOC1";
                        cosStructureXML += "\" title=\"";
                        cosStructureXML += dbUtils.esc4XML(myDbCourse.res_title);
                        cosStructureXML += "\">" + dbUtils.NEWL;                        
                        
                        boolRootBlock = false;
                        boolWithinRootBlock = true;
                    }
                    else {
                        cosStructureXML += "<item identifier=\"ITEM" + Integer.toString(numOfItem) + "\" title=\"" + dbUtils.esc4XML(title) + "\">";
                        cosStructureXML += dbUtils.NEWL; 
                        cosStructureXML += "<itemtype>FDR</itemtype>";
                        cosStructureXML += dbUtils.NEWL; 
                    }
                    
                    csfParseChildElements(element);
                    
                    // construct the ending tag
                    if (boolWithinRootBlock == true) {
                        cosStructureXML += "</tableofcontents>";
                        myDbCourse.cos_structure_xml = cosStructureXML;
                        myDbCourse.updCosStructureNoTimestampCheck(con, prof);
                    }
                    else {
                        cosStructureXML += "</item>";
                        cosStructureXML += dbUtils.NEWL; 
                    }
                }
                else {
                    dbModule myDbModule = new dbModule();
                    myDbModule.res_duration = intMaxTimeAllowedInMin;
                    myDbModule.res_title = title;
                    myDbModule.res_type = "MOD";
                    myDbModule.mod_pass_score = (float)masteryScore;
                    if(myDbModule.mod_pass_score > 0){
                    	if(myDbModule.mod_pass_score > 100){
                    		myDbModule.mod_max_score = myDbModule.mod_pass_score;
                    	}else{
                    		myDbModule.mod_max_score = 100;
                    	}
                    	
                    }
                    myDbModule.res_status = "ON";
                    myDbModule.mod_type = "SCORM_SCO_1_1";
                    myDbModule.res_subtype = "SCORM_SCO_1_1";
                    myDbModule.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
                    myDbModule.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
                    myDbModule.res_src_type = "MANIFEST_FILE";
                    myDbModule.res_src_link = launchURL;
                    myDbModule.res_upd_user = prof.usr_id;      
                    myDbModule.res_usr_id_owner = prof.usr_id;
                        
                    long mod_id = myDbCourse.insModule(con, myDbModule, null, prof);
                    //System.out.println("add module with module id:" + mod_id);
                    
                    cosStructureXML += "<item identifier=\"ITEM" + Integer.toString(numOfItem) + "\" identifierref=\"" + Integer.toString((int)mod_id) + "\" title=\"" + dbUtils.esc4XML(title) + "\">";
                    cosStructureXML += dbUtils.NEWL; 
                    cosStructureXML += "<itemtype>MOD</itemtype>";
                    cosStructureXML += dbUtils.NEWL; 
                    cosStructureXML += "</item>";
                    cosStructureXML += dbUtils.NEWL; 
                }
            }            
        }
    }
    
    public Node retrieveChildNode(Node parent, String tagName) {
        NodeList elements = parent.getChildNodes();
        int elementCount = elements.getLength();
        for (int i = 0; i < elementCount; i++) {
            Node element = (Node)elements.item(i);
            if (element.getNodeName().equalsIgnoreCase(tagName) == true) {
                return element;
            }
        }
        
        return null;
    }

    /** Normalizes the given string. */
    private String normalize(String s) {
        StringBuffer str = new StringBuffer();

        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '<': {
                    str.append("&lt;");
                    break;
                }
            case '>': {
                    str.append("&gt;");
                    break;
                }
            case '&': {
                    str.append("&amp;");
                    break;
                }
            case '"': {
                    str.append("&quot;");
                    break;
                }
            case '\r':
            case '\n': {
                    str.append("&#");
                    str.append(Integer.toString(ch));
                    str.append(';');
                    break;
                }
            default: {
                    str.append(ch);
                }
            }
        }

        return str.toString();

    } // normalize(String):String
    
    /**
     * Set scorm version
     * @param ver Scorm version , available value :  {@link #SCORM_VERSION_1_2} , {@link #SCORM_VERSION_2004}
     */
    public void setScormVerion(String ver) {
    	this.sco_version = ver;
    }

    public static void main(String[] args) throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage, cwException {
        dbCourse cos = new dbCourse();
        cos.cos_structure_xml = "<tableofcontents identifier=\"TOC1\" title=\"test01\"><item identifier=\"ITEM1\" identifierref=\"141\" title=\"我的课程方案11\"><itemtype>MOD</itemtype><restype>SCO</restype></item><item identifier=\"ITEM1\" identifierref=\"142\" title=\"我的课程方案2\"><itemtype>MOD</itemtype><restype>SCO</restype></item><item identifier=\"ITEM1\" identifierref=\"144\" title=\"我的课程方案\"><itemtype>MOD</itemtype><restype>SCO</restype></item><item identifier=\"ITEM2\" identifierref=\"145\" title=\"aicctest\"><itemtype>MOD</itemtype><restype>AICC_AU</restype></item><item identifier=\"ITEM3\" identifierref=\"161\" title=\"test\"><itemtype>MOD</itemtype><restype>RDG</restype></item></tableofcontents>";
        ScormContentParser sc = new ScormContentParser(null, null, cos);
        String uri = "d:\\AICC\\imsmanifest.xml";
        sc.parseImsmanifest(uri, null, false, null);
        System.out.println(sc.cosStructureXML);
    }
} // class ScormContentParser

