/*
 * Created on 2005-4-20
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.report;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SurveyQueData {
    

    // question attribute
    public String res_title;
    public long order;
    public long res_id;
    public String queType;
    public String que_xml;
    public String que_text;
    public String[] condition_text;
    // question stat
    int highScore;
    int lowScore = 0;
    int totalResponseCount;
    double avResponse;
    double avScore;
    double stDeviation;
    double answered;
    double unanswered;
    // choice attribute
    int[] score;
    int[] condition;
    int[] scoreOrder;
    // choice stat
    int[] conditionCount;
    double[] percentage = null;
    // answer for FB question
    ArrayList response_bil = new ArrayList();
    ArrayList response_bil_count = new ArrayList();

    public SurveyQueData() {
        
        //
        this.highScore = 0;
        this.lowScore = 0;
        this.totalResponseCount = 0;
        this.avResponse = -1;
        this.avScore = 0;
        this.stDeviation = 0;
        this.answered = 0;
        this.unanswered = 0;
    }

    /*
     * 
     * @author dixson
     *
     * @param xmlString        String with xml content
     * @return               
     * 
     */
    public void parseString(String xmlString) throws cwException {
        try {
            SAXParserFactory spFactory = SAXParserFactory.newInstance();
            spFactory.setValidating(false);
            SAXParser saxParser;
            saxParser = spFactory.newSAXParser();
            DefaultHandler mh = new myHandler();

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

    public void countResponse(String atm_response_bil, int response_count, boolean isXlsFile, boolean isShowAllFBAns) {
    	Vector responBilVec = cwUtils.splitToVecString(atm_response_bil, "[|]");
        for(Iterator iter = responBilVec.iterator(); iter.hasNext();) {
        	String responBil = (String) iter.next();
        	if (queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
        		int tem = 0;
                // this question not being attempted
                if (responBil != null) {
                	try{
                		tem = Integer.parseInt(responBil) ;
                	}catch(Exception e){
                		
                	}
                	if(tem > 0){
                	    if((Integer.parseInt(responBil)-1)<conditionCount.length)
                	    {
                	    	conditionCount[Integer.parseInt(responBil) - 1] += response_count;
                	    }
                	}
                }
            }
            else if (queType.equals(dbQuestion.QUE_TYPE_FILLBLANK)) {
                // this question not being attempted
                if (atm_response_bil != null) {
                    if (!isXlsFile && totalResponseCount < 10 || isShowAllFBAns){
                        for (int i = 0;i<response_count;i++) {
                            response_bil.add(responBil);
                        }
                    }
                }
            }
        }
        if(atm_response_bil != null) {
        	totalResponseCount += response_count;
        }
    }

    public void calculateStat(long responseCount) {
        double temp = 0;
        double pertemp = 0;

        if (queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
            // percentage of each choice
            for (int i = 0; i < percentage.length; i++) {
                if (totalResponseCount != 0) {
                    percentage[i] = (double)conditionCount[i] / totalResponseCount * 100;
                    temp += score[i] * conditionCount[i];
                }
                else {
                    percentage[i] = 0;
                }
            }

            // average score
            if (totalResponseCount != 0) {
                avScore = (double)temp / totalResponseCount;
            }
            else {
                avScore = 0;
            }

            // average response
            if (lowScore == highScore || avScore == 0) {
                avResponse = -1;
            }
            else {
                avResponse = (double) (avScore - lowScore) / (highScore - lowScore) * 100;
            }

            // standard deviation
            for (int j = 0; j < percentage.length; j++) {
                pertemp += Math.pow((double) (score[j] - avScore), 2) * (double)conditionCount[j];
            }
            if (totalResponseCount != 0) {
                stDeviation = (double)Math.sqrt(pertemp / totalResponseCount);
            }
            else {
                stDeviation = 0;
            }
        }
        // percentage of answered/unanswered
        if (responseCount > 0) {
            answered = (double)totalResponseCount / responseCount * 100;
            unanswered = (double)100 - answered;
        }
        else {
            answered = 0;
            unanswered = 0;
        }
    }

    public StringBuffer getAsXml(boolean isShowAllFBAns) {
        StringBuffer xmlList = new StringBuffer();
        
        xmlList.append("<que_stat res_id=\"").append(res_id).append("\" res_title=\"").append(cwUtils.esc4XML(res_title))
               .append("\" order=\"").append(order).append("\">")
               .append(que_xml);
        if (queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
            xmlList.append("<opt_stat>");
            for (int optionCount = 0; optionCount < condition.length; optionCount++) {
                xmlList.append("<opt id=\"").append(scoreOrder[optionCount] + 1)
                       .append("\" score=\"").append(score[scoreOrder[optionCount]])
                       .append("\" cnt=\"").append(conditionCount[scoreOrder[optionCount]])
                       .append("\" percent=\"").append(SurveyQueReport.formatDouble(percentage[scoreOrder[optionCount]], SurveyQueReport.DEC_POINT))
                       .append("\" percent_int=\"").append((int) (percentage[scoreOrder[optionCount]]))
                       .append("\"/>");
            }
            xmlList.append("</opt_stat>")
                   .append("<res_stat cnt=\"").append(totalResponseCount)
                   .append("\" avg=\"").append(SurveyQueReport.formatDouble(avScore, SurveyQueReport.DEC_POINT))
                   .append("\" sd=\"").append(SurveyQueReport.formatDouble(stDeviation, SurveyQueReport.DEC_POINT))
                   .append("\">")
                   .append("<answered percent=\"").append(SurveyQueReport.formatDouble(answered, SurveyQueReport.DEC_POINT))
                   .append("\" percent_int=\"").append((int)answered)
                   .append("\"/>")
                   .append("<unanswered percent=\"").append(SurveyQueReport.formatDouble(unanswered, SurveyQueReport.DEC_POINT))
                   .append("\" percent_int=\"").append((int)unanswered)
                   .append("\"/>");
            if (avResponse != -1) {
                xmlList.append("<avg_res percent=\"").append(SurveyQueReport.formatDouble(avResponse, SurveyQueReport.DEC_POINT))
                       .append("\" percent_int=\"").append((int)avResponse)
                       .append("\"/>");
            }
            xmlList.append("</res_stat>");
        }
        else 
        if (queType.equals(dbQuestion.QUE_TYPE_FILLBLANK)) {
            xmlList.append("<ans_stat view_all=\"").append(isShowAllFBAns).append("\">");
            int rowCount = 0;
            Iterator bil_count = response_bil.iterator();

            if (isShowAllFBAns) {
                while (bil_count.hasNext()) {
                    xmlList.append("<ans>").append(cwUtils.esc4XML((String)bil_count.next())).append("</ans>");
                }
            }
            else {
                while (bil_count.hasNext()) {
                    if (rowCount == 10) {
                        break;
                    }
                    xmlList.append("<ans>").append(cwUtils.esc4XML((String)bil_count.next())).append("</ans>");
                    rowCount++;
                }
            }
            xmlList.append("</ans_stat>")
                   .append("<res_stat cnt=\"").append(totalResponseCount)
                   .append("\">")
                   .append("<answered percent=\"").append(SurveyQueReport.formatDouble(answered, SurveyQueReport.DEC_POINT))
                   .append("\" ")
                   .append("percent_int=\"").append((int)answered)
                   .append("\"/>")
                   .append("<unanswered percent=\"").append(SurveyQueReport.formatDouble(unanswered, SurveyQueReport.DEC_POINT))
                   .append("\" percent_int=\"").append((int)unanswered).append("\"/>")
                   .append("</res_stat>");
        }
        xmlList.append("</que_stat>");

        return xmlList;
    }

    private class myHandler extends DefaultHandler {
        ArrayList scoreList;
        ArrayList choiceList;
        ArrayList queText;
        ArrayList conditionText;
        StringBuffer content = new StringBuffer();

        myHandler() {
            super();
            scoreList = new ArrayList();
            choiceList = new ArrayList();
            conditionText = new ArrayList();
        }

        public void characters(char[] ch, int start, int length) {
            content.append(ch, start, length);
        }

        /*
        * @param uri        name space name
        * @param localName  localname
        * @param qName      
        */
        public void startElement(String uri, String localName, String qname, Attributes atts) {
            if (qname.equals("outcome")) {
                highScore = Integer.parseInt(atts.getValue("score"));
            }
            if (qname.equals("feedback")) {
                scoreList.add(atts.getValue("score"));
                choiceList.add(atts.getValue("condition"));

            }
            if (qname.equals("interaction")) {
                que_text = content.toString();
            }
            if (qname.equals("option")) {
            }
            content.setLength(0);
        }

        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("outcome")) {
                if (queType.equals(dbQuestion.QUE_TYPE_MULTI)) {

                    int choiceCnt = scoreList.size();
                    score = new int[choiceCnt];
                    condition = new int[choiceCnt];
                    scoreOrder = new int[choiceCnt];
                    conditionCount = new int[choiceCnt];
                    percentage = new double[choiceCnt];
                    for (int choiceIdx = 0; choiceIdx < scoreList.size(); choiceIdx++) {
                        score[choiceIdx] = scoreList.get(choiceIdx) == null ? 0 : Integer.parseInt((String)scoreList.get(choiceIdx));
                        condition[choiceIdx] = Integer.parseInt((String)choiceList.get(choiceIdx));

                        int orderIdx = 0;
                        for (orderIdx = 0; orderIdx < choiceIdx; orderIdx++) {
                            if (score[choiceIdx] > score[scoreOrder[orderIdx]]) {
                                for (int shiftIdx = choiceIdx - 1; shiftIdx >= orderIdx; shiftIdx--) {
                                    scoreOrder[shiftIdx + 1] = scoreOrder[shiftIdx];
                                }
                                scoreOrder[orderIdx] = choiceIdx;
                                break;
                            }
                        }
                        if (orderIdx == choiceIdx) {
                            scoreOrder[orderIdx] = choiceIdx;
                        }
                    }
                    lowScore = score[scoreOrder[scoreOrder.length - 1]];
                }
            }
            if (qName.equals("option")) {
                conditionText.add(content.toString());
            }
            
            if (qName.equals("interaction")) {
                condition_text = new String[conditionText.size()];
                for (int i = 0;i<conditionText.size();i++) {
                    condition_text[i] = (String)conditionText.get(i);
                }
            }
            
        }
    }
}


    
