package com.cw.wizbank.wizcase;

import java.io.*;
import java.sql.*;
import java.util.Enumeration;

import javax.servlet.http.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.cw.wizbank.*;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class WizCaseModule extends ServletModule {
    long startTime;
    long endTime;

    public static String EMPTY_STRING =   "";

    public void process()
        throws IOException, cwException, SQLException{
            
            HttpSession sess = request.getSession(false);

            WizCaseReqParam urlp = null;

            urlp = new WizCaseReqParam(request, clientEnc, static_env.ENCODING);
            urlp.common();            

            PrintWriter out = response.getWriter();

            try {
                if( urlp.cmd.equalsIgnoreCase("alive") ) {
                    
CommonLog.debug("User Alive");
                    response.sendRedirect(urlp.url_success);

                }else if( urlp.cmd.equalsIgnoreCase("reset") ) {

                    urlp.reset();
                    dbUtils db = new dbUtils(con, urlp.ent_id, urlp.cos_id, urlp.mod_id, urlp.cache, sess);
startTime = System.currentTimeMillis();
                    db.reset();
endTime = System.currentTimeMillis();
CommonLog.debug("Reset (" + urlp.ent_id + ") : " + (endTime-startTime));
                    sess = request.getSession(true);
                    Enumeration sessObjNameList = sess.getAttributeNames();
                    while (sessObjNameList.hasMoreElements()) {
                        String sessObjName = (String)sessObjNameList.nextElement();
                        if (sessObjName.startsWith("SESS_WIZCASE")) {
                            sess.removeAttribute(sessObjName);
                        }
                    }
                    con.commit();

                    //Delete all pre-compiled word documents
                    String xmlDir = null;
                    if( urlp.default_xml_dir != null ) {
                        xmlDir = urlp.default_xml_dir;
                        sess.setAttribute("SESS_WIZCASE_XML_DIR", xmlDir);
                    } else
                        xmlDir = (String)sess.getAttribute("SESS_WIZCASE_XML_DIR");

                    if( xmlDir == null )
                        throw new cwException("Sever error : xml directory not specified.");
startTime = System.currentTimeMillis();
                    File wordDir  = new File( static_env.DOC_ROOT + cwUtils.SLASH + static_env.WIZCASE_TEMP_DIR  );
                    if( wordDir.exists() ) {
                        File modWordDir = new File(wordDir, xmlDir);
                        if( modWordDir.exists() ) {
                            File userWordDir = new File(modWordDir, Long.toString(urlp.ent_id));
                            if( userWordDir.exists() ) {                                
                                File[] files = userWordDir.listFiles();
                                for(int i=0; i<files.length; i++)
                                    files[i].delete();
                            }
                        }
                    }
endTime = System.currentTimeMillis();
CommonLog.debug("Delete Files (" + urlp.ent_id + ") : " + (endTime-startTime));
                    response.sendRedirect(urlp.url_success);
                    
                }else if( urlp.cmd.equalsIgnoreCase("next_step") || urlp.cmd.equalsIgnoreCase("next_step_xml") ) {                    
                    
                    urlp.nextStep();
                    sess = request.getSession(true);
                    WizCase wizCase = new WizCase();
                    dbUtils db = new dbUtils(con, urlp.ent_id, urlp.cos_id, urlp.mod_id, urlp.cache, sess);
                    Node userNode = null;
                    Node formNode;
                    Node tocNode;
                    //boolean trial = false;
                    String formFilename = null;

                    if( urlp.formFilename != null && urlp.formFilename.length() > 0 ) {
                        formFilename = urlp.formFilename;
                        //sess.setAttribute("SESS_WIZCASE_FORM_NAME", formFilename);                        
                        //sess.setAttribute("SESS_WIZCASE_FORM_NAME", urlp.developer_id);                    
                    } else
                        throw new cwException("Please specify which form of xml .");
                        
                    if( urlp.developer_id != null && urlp.developer_id.length() > 0 ) {                    
                        sess.setAttribute("SESS_WIZCASE_FORM_NAME", urlp.developer_id);
                    }
                                        
                    if( urlp.toc.length > 0 ) {
                        startTime = System.currentTimeMillis();
                        tocNode = wizCase.constructToc(urlp.toc, sess);                        
                        endTime = System.currentTimeMillis();
CommonLog.debug("Construct TOC (" + urlp.ent_id + ") : " + (endTime-startTime));
                        //db.putToDb(db.TOC, null, tocNode);
                    } else {
                        //tocNode = (Node)sess.getAttribute("SESS_WIZCASE_TOC_XML");
                        startTime = System.currentTimeMillis();
                        tocNode = db.getFromDb(db.TOC, null);
                        endTime = System.currentTimeMillis();
CommonLog.debug("Get TOC FROM DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                    }
                        
                    if( tocNode != null ) {
                        ((Element)tocNode).setAttribute("cur_form", formFilename);
                        String[] attrName = { "page", "line_num" };
                        String[] attrValue = { urlp.formPage, urlp.formLineNum };
                        startTime = System.currentTimeMillis();
                        wizCase.updateToc(tocNode, formFilename, attrName, attrValue);
                        //sess.setAttribute("SESS_WIZCASE_TOC_XML", tocNode);
                        db.putToDb(db.TOC, null, tocNode);                   
                        endTime = System.currentTimeMillis();
CommonLog.debug("Put TO DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                    }


/*
                    if(urlp.next_type != null && urlp.next_type.equalsIgnoreCase("CHECK") ) {
                        trial = true;
                    }
*/
                    
                    boolean fromDb;
                    
                    if( urlp.blockIdVec.size() > 0 ) {
                        fromDb = false;
                        startTime = System.currentTimeMillis();
                        userNode = wizCase.constructXML(urlp.blockIdVec, urlp.elementVec, urlp.pageBreakVec/*, trial*/);
                        endTime = System.currentTimeMillis();
CommonLog.debug("Construct XML (" + urlp.ent_id + ") : " + (endTime-startTime));
                        //sess.setAttribute("SESS_WIZCASE_XML" + formFilename , userNode);
                        //db.putToDb(db.CONTENT_PAGE, urlp.developer_id, userNode);
                    } else {
                        //userNode = (Node)sess.getAttribute("SESS_WIZCASE_XML" + formFilename);
startTime = System.currentTimeMillis();
                        userNode = db.getFromDb(db.CONTENT_PAGE, urlp.developer_id);
endTime = System.currentTimeMillis();
CommonLog.debug("Get USER NODE FROM DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        fromDb = true;
                    }

                    if( userNode != null && urlp.correct_form_id == null) {
                        if( urlp.trial > 0 ) {
startTime = System.currentTimeMillis();
CommonLog.debug("Update " + urlp.developer_id + " ======================================== ");
                            ((Element)userNode).setAttribute("trial", Integer.toString(urlp.trial));
                            db.putToDb(db.CONTENT_PAGE, urlp.developer_id, userNode);
endTime = System.currentTimeMillis();
CommonLog.debug("Put USER Node To DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        }else if( !fromDb ) {
startTime = System.currentTimeMillis();
                            db.putToDb(db.CONTENT_PAGE, urlp.developer_id, userNode);
endTime = System.currentTimeMillis();
CommonLog.debug("PUT User NOde TO DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        }
                    } else if( urlp.correct_form_id != null && urlp.trial > 0 ){
CommonLog.debug("Update " + urlp.correct_form_id + " ==================================== ");                        
                        Node _userNode = db.getFromDb(db.CONTENT_PAGE, urlp.correct_form_id);
                        ((Element)_userNode).setAttribute("trial", Integer.toString(urlp.trial));
                        db.putToDb(db.CONTENT_PAGE, urlp.correct_form_id, _userNode);
                        
                    }

/*                    
                    
                    
                    if( userNode != null ) {
                        //String trial = (String)sess.getAttribute("SESS_WIZCASE_ANSWER_CHECKED" + formFilename);
                        if( trial != null && trial.equalsIgnoreCase("Y") )
                            ((Element)userNode).setAttribute("trial", "Y");
                        else
                            ((Element)userNode).setAttribute("trial", "N");
                    }    
                    if(urlp.next_type != null && urlp.next_type.equalsIgnoreCase("CHECK") ) {
                        sess.setAttribute("SESS_WIZCASE_ANSWER_CHECKED" + formFilename, "Y");
                        
                    }
                    
*/                    
                    String xmlDir = null;
                    try{                        
                        if( urlp.default_xml_dir != null ) {
                            xmlDir = urlp.default_xml_dir;
                            sess.setAttribute("SESS_WIZCASE_XML_DIR", xmlDir);
                        } else
                            xmlDir = (String)sess.getAttribute("SESS_WIZCASE_XML_DIR");
                        if( xmlDir == null )
                            throw new cwException("Sever error : xml directory not specified.");
                        formFilename = static_env.DOC_ROOT + cwUtils.SLASH + static_env.WIZCASE_XML_HOME + cwUtils.SLASH + xmlDir + cwUtils.SLASH + formFilename;
                        startTime = System.currentTimeMillis();
                        formNode = xmlUtils.parseXMLFile(new File(formFilename));
                        endTime = System.currentTimeMillis();
CommonLog.debug("Parse XML (" + urlp.ent_id + ") : " + (endTime-startTime));
                    } catch ( SAXException e ) {
CommonLog.error("Parse xml file error : " + e.getMessage());
                        throw new cwException("Parse xml file error : " + e);
                    } catch ( IOException e ) {
                        throw new cwException(e.getMessage());
                    }


                    StringBuffer xml = new StringBuffer();
                    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
startTime = System.currentTimeMillis();
                    xml.append(wizCase.genXml( formNode, userNode, urlp.next_type, tocNode, xmlDir));
endTime = System.currentTimeMillis();
CommonLog.debug("Gen XML (" + urlp.ent_id + ") : " + (endTime-startTime));

                    
//Pre Compile Word File  ===================================================================                    
                    if(urlp.next_type != null &&  ( urlp.next_type.equalsIgnoreCase("CHECK") || urlp.next_type.equalsIgnoreCase("ANSWER") ) ) {
                        String XSLFILE = static_env.DOC_ROOT + cwUtils.SLASH + static_env.INI_XSL_HOME + cwUtils.SLASH + urlp.word_stylesheet;
                        String result = null;
                        startTime = System.currentTimeMillis();
                        result = cwXSL.processFromFile(xml.toString(), XSLFILE);
                        endTime = System.currentTimeMillis();
                        CommonLog.debug("Transform By XSL (" + urlp.ent_id + ") : " + (endTime-startTime));                            
                        //sess.setAttribute("SESS_WIZCASE_WORD" + formFilename, result);
                        //put pre-compile word doc into file, not ready
                        File wordDir  = new File( static_env.DOC_ROOT + cwUtils.SLASH + static_env.WIZCASE_TEMP_DIR  );
                        if( !wordDir.exists() )
                            wordDir.mkdir();

                        File modWordDir = new File(wordDir, xmlDir);
                        if( !modWordDir.exists() )
                            modWordDir.mkdir();

                        File userWordDir = new File(modWordDir, Long.toString(urlp.ent_id));
                        if( !userWordDir.exists() )
                            userWordDir.mkdir();
startTime = System.currentTimeMillis();
                        File wordFile = new File( userWordDir, urlp.developer_id + ".doc");
                        BufferedWriter bWriter = new BufferedWriter( new FileWriter(wordFile) );
                        bWriter.write(result);
                        bWriter.flush();
endTime = System.currentTimeMillis();
CommonLog.debug("Put TO FILE (" + urlp.ent_id + ") : " + (endTime-startTime));
                        
                    }
//=========================================================================================
                    if( urlp.cmd.equalsIgnoreCase("next_step") ) {

                        generalAsHtml(xml.toString(), out, urlp.stylesheet);

                    } else {                        
                        response.setContentType("text/xml; charset=" + static_env.ENCODING);
                        out.print(xml.toString());
                    }

                } else if( urlp.cmd.equalsIgnoreCase("prev_step") || urlp.cmd.equalsIgnoreCase("prev_step_xml") ) {

                    urlp.nextStep();
                    WizCase wizCase = new WizCase();
                    Node userNode = null;
                    Node formNode;
                    Node tocNode;
                    String formFilename = null;

                    dbUtils db = new dbUtils(con, urlp.ent_id, urlp.cos_id, urlp.mod_id, urlp.cache, sess);

                    sess = request.getSession(true);                        
                        
                    if( urlp.formFilename != null && urlp.formFilename.length() > 0 ) 
                        formFilename = urlp.formFilename;                        
                    else
                        throw new cwException("Please specify which form of xml .");

                    boolean fromDb;
                    if( urlp.blockIdVec.size() > 0 ) {  
                        startTime = System.currentTimeMillis();
                        userNode = wizCase.constructXML(urlp.blockIdVec, urlp.elementVec, urlp.pageBreakVec);
endTime = System.currentTimeMillis();
CommonLog.debug("Construct XML (" + urlp.ent_id + ") : " + (endTime-startTime));
                        fromDb = false;
                        //String prevFilename = (String)sess.getAttribute("SESS_WIZCASE_FORM_NAME");
                        //sess.setAttribute("SESS_WIZCASE_XML" + prevFilename , userNode); 
                        //db.putToDb(db.CONTENT_PAGE, prevFilename, userNode);
                    } else {
                        //userNode = (Node)sess.getAttribute("SESS_WIZCASE_XML" + formFilename);
                        //userNode = db.getFromDb(db.CONTENT_PAGE, urlp.developer_id);
startTime = System.currentTimeMillis();
                        userNode = db.getFromDb(db.CONTENT_PAGE, urlp.developer_id);
endTime = System.currentTimeMillis();
CommonLog.debug("Get User NODE FRom DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        fromDb = true;
                    }

                        
                    if( userNode != null ) {
                        if( urlp.trial > 0 ) {
startTime = System.currentTimeMillis();
                            ((Element)userNode).setAttribute("trial", Integer.toString(urlp.trial));
                            db.putToDb(db.CONTENT_PAGE, urlp.developer_id, userNode);
endTime = System.currentTimeMillis();
CommonLog.debug("PUT USER NODE TO DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        }else if( !fromDb ) {
startTime = System.currentTimeMillis();
                            db.putToDb(db.CONTENT_PAGE, urlp.developer_id, userNode);
endTime = System.currentTimeMillis();
CommonLog.debug("PUT USER NODE TO DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        }
                    }
                    
                    
                    /*                        

                    if( userNode != null ) {
                        String trial = (String)sess.getAttribute("SESS_WIZCASE_ANSWER_CHECKED");
                        if( trial != null && trial.equalsIgnoreCase("Y") )
                            ((Element)userNode).setAttribute("trial", "Y");
                        else
                            ((Element)userNode).setAttribute("trial", "N");
                    }
                    */
                    
                    //if( (tocNode = (Node)sess.getAttribute("SESS_WIZCASE_TOC_XML")) != null ) {
                    if( (tocNode = db.getFromDb(db.TOC, null)) != null ) {
                        ((Element)tocNode).setAttribute("cur_form", formFilename);
                        String[] attrName = { "page", "line_num" };
                        String[] attrValue = { urlp.formPage, urlp.formLineNum };
startTime = System.currentTimeMillis();
                        wizCase.updateToc(tocNode, formFilename, attrName, attrValue);
                        //sess.setAttribute("SESS_WIZCASE_TOC_XML", tocNode);
                        db.putToDb(db.TOC, null, tocNode);
endTime = System.currentTimeMillis();
CommonLog.debug("UPDATE TOC AND PUT TO DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        
                    }

                    String xmlDir = null;
                    try{                        
                        if( urlp.default_xml_dir != null ) {
                            xmlDir = urlp.default_xml_dir;
                            sess.setAttribute("SESS_WIZCASE_XML_DIR", xmlDir);
                        } else
                            xmlDir = (String)sess.getAttribute("SESS_WIZCASE_XML_DIR");
                        if( xmlDir == null )
                            throw new cwException("Sever error : xml directory not specified.");
                        
                        formFilename = static_env.DOC_ROOT + cwUtils.SLASH + static_env.WIZCASE_XML_HOME + cwUtils.SLASH + xmlDir + cwUtils.SLASH + formFilename;
                        startTime = System.currentTimeMillis();
                        formNode = xmlUtils.parseXMLFile(new File(formFilename));
                        endTime = System.currentTimeMillis();
                        CommonLog.debug("Parse XML (" + urlp.ent_id + ") : " + (endTime-startTime));                        
                    } catch ( SAXException e ) {
                        throw new cwException("Parse xml file error : " + e);
                    } catch ( IOException e ) {
                        throw new cwException(e.getMessage());
                    }

                    if( urlp.url_success != null )
                        response.sendRedirect(urlp.url_success);                    
                    
                    
                    StringBuffer xml = new StringBuffer();
                    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
                    xml.append(wizCase.genXml( formNode, userNode, urlp.next_type, tocNode, xmlDir));
                                        
                    if( urlp.cmd.equalsIgnoreCase("prev_step") ) {
startTime = System.currentTimeMillis();                        
                        generalAsHtml(xml.toString(), out, urlp.stylesheet);
endTime = System.currentTimeMillis();
CommonLog.debug("Transform By XSL (" + urlp.ent_id + ") : " + (endTime-startTime));
                    } else {
                        response.setContentType("text/xml; charset=" + static_env.ENCODING);
                        out.print(xml.toString());
                    }

                } else if( urlp.cmd.equalsIgnoreCase("preview") || urlp.cmd.equalsIgnoreCase("preview_xml") ) {
                    
                    urlp.preview();
                    dbUtils db = new dbUtils(con, urlp.ent_id, urlp.cos_id, urlp.mod_id, urlp.cache, sess);
                    WizCase wizCase = new WizCase();
                    int NUM_FILENAME = urlp.developersId.length;                    Node[] formsNode = new Node[NUM_FILENAME];
                    Node[] usersNode = new Node[NUM_FILENAME];
                    Node tocNode;
                    String xmlDir = null;
                    String XSLFILE = static_env.DOC_ROOT + cwUtils.SLASH + static_env.INI_XSL_HOME + cwUtils.SLASH + urlp.word_stylesheet;
                    StringBuffer wordPreview = new StringBuffer();
                    wordPreview.append("<html><body>");
                    try{

                        if( urlp.default_xml_dir != null ) {
                            xmlDir = urlp.default_xml_dir;
                            sess.setAttribute("SESS_WIZCASE_XML_DIR", xmlDir);
                        } else
                            xmlDir = (String)sess.getAttribute("SESS_WIZCASE_XML_DIR");
                        if( xmlDir == null )
                            throw new cwException("Sever error : xml directory not specified.");
                        
                        String formFilename;
                        sess = request.getSession(true);
                        //tocNode = (Node)sess.getAttribute("SESS_WIZCASE_TOC_XML");
                        //tocNode = db.getFromDb(db.TOC, null);
startTime = System.currentTimeMillis();                        
                        tocNode = db.getFromDb(db.CONTENT_PAGE, "WIZCASETOC");
endTime = System.currentTimeMillis();
CommonLog.debug("Get TOC FROM DB (" + urlp.ent_id + ") : " + (endTime-startTime));
                        
                        StringBuffer _temp = new StringBuffer();
startTime = System.currentTimeMillis();                        
                        for(int i=0; i<NUM_FILENAME; i++) {
                            
                            //formFilename = static_env.DOC_ROOT + cwUtils.SLASH + static_env.WIZCASE_XML_HOME + cwUtils.SLASH + xmlDir + cwUtils.SLASH + urlp.formsFilename[i];
                            //usersNode[i] = (Node)sess.getAttribute("SESS_WIZCASE_XML" + formFilename);
                            //formFilename = static_env.DOC_ROOT + cwUtils.SLASH + static_env.WIZCASE_XML_HOME + cwUtils.SLASH + formFilename;
                            //formsNode[i] = xmlUtils.parseXMLFile(new File(formFilename));

                            /*
                            if( sess.getAttribute("SESS_WIZCASE_WORD" + formFilename) != null ) { //check the pre-compile word exist ??
                                wordPreview.append(sess.getAttribute("SESS_WIZCASE_WORD" + formFilename));                                
                            }else if ( urlp.formsFilename[i].equalsIgnoreCase("index.xml") || urlp.formsFilename[i].equalsIgnoreCase("toc.xml") ) {
                                formsNode[i] = xmlUtils.parseXMLFile(new File(formFilename));
                                _temp = new StringBuffer();
                                xmlUtils.print(formsNode[i], _temp);                                
                                wordPreview.append(xmlUtils.XSLTransform(_temp.toString(), XSLFILE, "UTF-8"));
                            }
                            */
                            File userWordFile = new File( static_env.DOC_ROOT + cwUtils.SLASH 
                                                       + static_env.WIZCASE_TEMP_DIR + cwUtils.SLASH 
                                                       + xmlDir + cwUtils.SLASH 
                                                       + Long.toString(urlp.ent_id) + cwUtils.SLASH 
                                                       //+ WizCase.getFilename(urlp.formsFilename[i],"doc") );
                                                       + urlp.developersId[i] + ".doc");
                            if( userWordFile.exists() ) {

                                BufferedReader bReader = new BufferedReader(new FileReader(userWordFile));
                                String lineStr = bReader.readLine();

                                while( lineStr != null ) {
                                    wordPreview.append(lineStr);
                                    lineStr = bReader.readLine();

                                }
                            } else if( urlp.formsFilename[i].equalsIgnoreCase("wizcase_index.xsl") || urlp.formsFilename[i].equalsIgnoreCase("wizcase_toc.xsl") ) {
                                formFilename = static_env.DOC_ROOT + cwUtils.SLASH + static_env.WIZCASE_XML_HOME + cwUtils.SLASH + xmlDir + cwUtils.SLASH + urlp.formsFilename[i];
                                formsNode[i] = xmlUtils.parseXMLFile(new File(formFilename));
                                _temp = new StringBuffer();
                                xmlUtils.print(formsNode[i], _temp);
                                wordPreview.append(cwXSL.processFromFile(_temp.toString(), XSLFILE));
                            }
                        }
endTime = System.currentTimeMillis();
CommonLog.debug("GEn WORD (" + urlp.ent_id + ") : " + (endTime-startTime));
                    
                    }catch( SAXException e ) {
                        throw new cwException("Parse xml file error , file = " + "");
                    }
                    
                    wordPreview.append("</body></html>");                    
                    //StringBuffer xml = new StringBuffer();
                    //xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
                    
//startTime = System.currentTimeMillis();
                    //xml.append(wizCase.combineXml( formsNode, usersNode, tocNode ));
//endTime = System.currentTimeMillis();
//System.out.println("Combine XML Time = " + (endTime - startTime));

                    
/*
File _tempFile = new File( "test.xml" );
BufferedWriter _bWriter = new BufferedWriter( new FileWriter(_tempFile) );
_bWriter.write(xml.toString());
_bWriter.flush();
*/
                    
                    if( urlp.cmd.equalsIgnoreCase("preview") ) {
                        if( urlp.next_type != null && urlp.next_type.equalsIgnoreCase("WORD") ) {

                            try{
                            //String TEMP_PATH = "temp_wizcase";
                            //String XSLFILE = static_env.DOC_ROOT + cwUtils.SLASH + static_env.INI_XSL_HOME + cwUtils.SLASH + urlp.stylesheet;
//startTime = System.currentTimeMillis();
//comment for testing; don't delete it         String result = xmlUtils.XSLTransform(xml.toString(), XSLFILE, "UTF-8");
//endTime = System.currentTimeMillis();
//System.out.println("Process XSL Time = " + (endTime - startTime));
                            /*
                            long timestamp = System.currentTimeMillis();
                            File tempPath = new File( static_env.DOC_ROOT + cwUtils.SLASH + TEMP_PATH );
                            if( !tempPath.exists() )
                                tempPath.mkdir();
                            int hour = 5;
                            int count = wizCase.delFileBefore(tempPath, timestamp, 1000 * 60 * 60 * hour);
                            File tempFile = new File( tempPath, timestamp + ".doc");
                            */
                            File userWordFile = new File( static_env.DOC_ROOT + cwUtils.SLASH
                                                        + static_env.WIZCASE_TEMP_DIR + cwUtils.SLASH
                                                        + xmlDir + cwUtils.SLASH
                                                        + Long.toString(urlp.ent_id) + cwUtils.SLASH
                                                        + "preview.doc");
startTime = System.currentTimeMillis();
                            BufferedWriter bWriter = new BufferedWriter( new FileWriter(userWordFile) );
                            
                            bWriter.write(wordPreview.toString());
                            bWriter.flush();
                            //response.setContentType ("application/ms-word; charset=" + static_env.ENCODING);
                            //response.setHeader ("Content-Disposition", "inline; filename=\"report.doc\"");
                            //out.println(result);
                            response.sendRedirect("../" + static_env.WIZCASE_TEMP_DIR
                                                  + "/" + xmlDir + "/" + Long.toString(urlp.ent_id) + "/preview.doc");
                            
                            } catch( Exception e ) {
                            	CommonLog.error( e.getMessage() ,e);
                                out.println( e.getMessage() );
                            }

                        } else {
                            //generalAsHtml(xml.toString(), out, urlp.stylesheet);
                        }
                    } else {
                        response.setContentType("text/xml; charset=" + static_env.ENCODING);
                        //out.print(xml.toString());
                    }
                    
                } else if( urlp.cmd.equalsIgnoreCase("clear_sess") ){
                    sess = request.getSession(true);
                    Enumeration sessObjNameList = sess.getAttributeNames();
                    while (sessObjNameList.hasMoreElements()) {
                        String sessObjName = (String)sessObjNameList.nextElement();
                        if (sessObjName.startsWith("SESS_WIZCASE")) {
                            sess.removeAttribute(sessObjName);
                        }
                    }
                    response.sendRedirect(urlp.url_success); 
                } else {
                    throw new cwSysMessage("GEN000");
                }

            } catch( cwSysMessage se ) {
                  try{                      
                      msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
                  }catch( SQLException sqle ) {                      
                      out.print(sqle);
                  }
             }

        }

}