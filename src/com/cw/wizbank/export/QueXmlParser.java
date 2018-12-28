/*
 * Created on 2006-2-23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.export;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class QueXmlParser {
    public String[] condition_text;
    public int[] scoreOrder;
    public String[] condition;
    public int[] score;
    public String queType;
    public int highScore;
    public String que_text;
    public String shuffle;
    public String explain;
    public String asHtml = "N";
    public Vector fbQueText;
    public Vector mt_source;
    public Vector mt_target;
    public boolean is_first_que_txt;
    public String sc_que_text;
    public boolean sc_que_text_has_value = false;
    
    public void parseString(String xmlString, String que_type) throws cwException {
        try {
            SAXParserFactory spFactory = SAXParserFactory.newInstance();
            spFactory.setValidating(false);
            SAXParser saxParser;
            saxParser = spFactory.newSAXParser();
            DefaultHandler mh = new myHandler();
            queType = que_type;
            
            //for mt question only get the 
            is_first_que_txt = true;

            if (xmlString != null && xmlString.length() > 0) {
                StringReader rd = new StringReader(xmlString);
                InputSource in = new InputSource(rd);
                saxParser.parse(in, mh);
            }
        }
        catch (ParserConfigurationException e) {
        	CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }
        catch (SAXException e) {
        	CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }
        catch (IOException e) {
        	CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }

    }

    private class myHandler extends DefaultHandler {
        ArrayList scoreList;
        ArrayList choiceList;
        ArrayList conditionText;
        Vector fbqt;
        Vector mtTarget;
        Vector mtSource;
        Vector mc_opts;
        StringBuffer content = new StringBuffer();

        myHandler() {
            super();
            scoreList = new ArrayList();
            choiceList = new ArrayList();
            conditionText = new ArrayList();
            fbqt = new Vector();
            mtTarget = new Vector();
            mtSource = new Vector();
            mc_opts = new Vector();
        }

        public void characters(char[] ch, int start, int length) {
            content.append(ch, start, length);
        }

        public void startElement(String uri, String localName, String qname, Attributes atts) {
            if (qname.equals("outcome")) {
                String hiScore = atts.getValue("score");
                if (hiScore != null && hiScore.length() > 0) {
                    highScore = Integer.parseInt(hiScore);
                }
            }
            if (qname.equals("feedback")) {
                String score = atts.getValue("score");
                String condition = atts.getValue("condition");
                scoreList.add(score);
                choiceList.add(condition);
            }
            if (qname.equals("html")) {
                asHtml = "Y";
            }
            if (qname.equals("interaction")) {
                if (content.toString().length() > 0) {
                    sc_que_text = content.toString();
                    sc_que_text_has_value = true;
                }
                if (is_first_que_txt) {
                    que_text = content.toString();
                    is_first_que_txt = false;
                }
                shuffle = atts.getValue("shuffle");
                //for fb quesiont
                if (content.length() > 0) {
                    fbqt.add(content.toString());
                }
                fbqt.add("");
            }
            if (qname.equals("option")) {
            }
            
            if (qname.equals("source")) {
                mt_target = mtTarget;
            }
            
            if (qname.equals("object")) {
                sc_que_text = content.toString();
                sc_que_text_has_value = true;
                
                if (queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
                	if (sc_que_text_has_value && sc_que_text != null && sc_que_text.length() > 0) {
                		mc_opts.add(sc_que_text);
                	}
                }
            }
            content.setLength(0);
        }

        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("outcome")) {
                int choiceCnt = scoreList.size();
                score = new int[choiceCnt];
                condition = new String[choiceCnt];
                for (int choiceIdx = 0; choiceIdx < scoreList.size(); choiceIdx++) {

                    String temp = (String)scoreList.get(choiceIdx);
                    if (temp != null && temp.length() > 0) {
                        score[choiceIdx] = Integer.parseInt((String)scoreList.get(choiceIdx));
                    }
                    else {
                        score[choiceIdx] = 0;
                    }
                    condition[choiceIdx] = (String)choiceList.get(choiceIdx);
                }
            }
            
            if (qName.equals("feedback")) {
            }
            if (qName.equals("option")) {
            	if (content.toString().length() > 0) {
            		conditionText.add(content.toString());
            	} else {
            		if (queType.equals(dbQuestion.QUE_TYPE_MULTI) && mc_opts.size() > 0 && mc_opts.get(0) != null) {
                    	conditionText.add(mc_opts.get(0).toString());
                    	mc_opts.remove(0);
                    } else {
                    	conditionText.add(content.toString());
                    }
            	}
            }
            
            if (qName.equals("interaction")) {
                condition_text = new String[conditionText.size()];
                for (int i = 0;i<conditionText.size();i++) {
                    condition_text[i] = (String)conditionText.get(i);
                }
                if (content.toString().length() > 0) {
                    mtTarget.add(content.toString());
                }
            }
            if (qName.equals("explanation") && explain == null) {
                if (content.length() > 0) {
                    explain = content.toString();
                }
            }
            
            if (qName.equals("rationale") && explain == null) {
                if (content.length() > 0) {
                    explain = content.toString();
                }
            }
            
            // for fb type question text build
            if (qName.equals("body")) {
                if (queType.equals(dbQuestion.RES_SUBTYPE_FSC) || queType.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                    if (!sc_que_text_has_value) {
                        sc_que_text = content.toString();
                    }
                    sc_que_text_has_value = false;
                }
                if (content.toString().length() > 0) {
                    fbqt.add(content.toString());
                }
                fbQueText = fbqt;
            }
            
            // for mt type question 
            if (qName.equals("item")) {
                if (content.toString().length() > 0) {
                    mtSource.add(content.toString());
                }
            }
            
            if (qName.equals("source")) {
                mt_source = mtSource;
            }
        }
    }

}
