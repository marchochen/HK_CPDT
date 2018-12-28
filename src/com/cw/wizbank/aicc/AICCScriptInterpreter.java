package com.cw.wizbank.aicc;

import java.sql.*;
import java.util.*;
import java.text.StringCharacterIterator;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbEntity;

/**
class for writing aicc script
*/
public class AICCScriptInterpreter {
    
    /* AICC Script operator */
    static final char AND = '&';
    static final char OR = '|';
    static final char EQUAL = '=';
    static final char OPEN_PARENTHESIS = '(';
    static final char CLOSE_PARENTHESIS = ')';
    
    /* AICC Status */
    static final char AICC_STATUS_C = 'C';
    static final char AICC_STATUS_P = 'P';
    static final char AICC_STATUS_N = 'N';
    static final char AICC_STATUS_F = 'F';
    static final char AICC_STATUS_I = 'I';
    static final String AICC_STATUS_COMPLETED = "COMPLETED";
    static final String AICC_STATUS_PASSED = "PASSED";
    static final String AICC_STATUS_NOT_STARTED = "NOT STARTED";
    static final String AICC_STATUS_FAILED = "FAILED";
    static final String AICC_STATUS_INCOMPLETE = "INCOMPLETE";
    
    /* Element type */
    public static final String ELEMENT_TYPE_MODULE = "MOD_";
    public static final String ELEMENT_TYPE_COURSE = "COS_";
    public static final String ELEMENT_TYPE_ITEM = "ITM_";
    public static final String ELEMENT_TYPE_USERGROUP = "USG_";
    public static final String ELEMENT_TYPE_USERGRADE = "UGR_";
    public static final String ELEMENT_TYPE_INDUSTRY = "IDC_";
    // NOTICE, ELEMENT_TYPE_USER_CLASSIFICATION CAN BE CLASS1, CLASS2 ETC, SO USE CLASS AS PREFIX
    public static final String ELEMENT_TYPE_USER_CLASSIFICATION = "CLASS";


    /* class variable */
    private long usr_ent_id = 0;
    private Timestamp due_date = null;
    private String aiccScript = null;
    private Connection con = null;

    /**
    Parse the AICC Script and interpret if the script is true or false
    @param con Connection to database
    @param usr_ent_id wizBank user entity id
    @param due_datetime due date of the AICC Script
    @param aiccScript AICC Script to be interpreted
    @return if the AICC Script is true or false
    */
    public boolean interpret(Connection con, long usr_ent_id, Timestamp due_date, String aiccScript) 
        throws SQLException, cwException {
        
        if(!validateInputUsrEntId(usr_ent_id)) {
            throw new cwException("user entity id should be larger than 0");
        }
        if(!validateInputAICCScript(aiccScript)) {
            throw new cwException("AICC Script should not be null");
        }
        this.con = con;
        this.usr_ent_id = usr_ent_id;
        this.due_date = due_date;
        this.aiccScript = aiccScript;
        
        return interpret(this.aiccScript);
    }
    
    /**
    Parse the AICC Script and interpret if the script is true or false recursively
    @param aiccScript AICC Script to be interpreted
    @return if the AICC Script is true or false
    */
    private boolean interpret(String aiccScript) throws SQLException {
        //System.out.println("aiccScript = " + aiccScript);
        aiccScript = aiccScript.trim();
        if(aiccScript.indexOf(AND) == -1 
            && aiccScript.indexOf(OR) == -1 
            && aiccScript.indexOf(OPEN_PARENTHESIS) == -1
            && aiccScript.indexOf(CLOSE_PARENTHESIS) == -1) {
            
            //System.out.println("Element ! : " + aiccScript);
            //DENNIS: TO BE ENHANCED
            /*boolean b= (Boolean.valueOf(aiccScript.trim())).booleanValue();*/
            //System.out.println(" a result = " + b);
            return interpretElementScript(aiccScript);
            //return false;
            
        } else {
            String[] childs = getScriptChild(aiccScript);
            
            //for(int i=0; i<childs.length; i++) {
            //    System.out.println("childs["+i+"]" + childs[i]);
            //}
            
            boolean[] bArray = new boolean[childs.length];
            for(int i=0; i<childs.length; i+=2) {
                bArray[i] = interpret(childs[i]);
            }
            boolean b=false;
            for(int i=0; i<childs.length; i+=2) {
                if(i==0) {
                    b = bArray[i];
                } else {
                    String op = childs[i-1].trim();
                    if(op.equals(AND+"")) {
                        b = b && bArray[i];
                    } else if(op.equals(OR+"")) {
                        b = b || bArray[i];
                    }
                }
                if(i<childs.length-1) {
                    String next_op = childs[i+1].trim();
                    if(b == true && next_op.equals(OR+"")) {
                        break;
                    } else if(b == false && next_op.equals(AND+"")) {
                        break;
                    }
                }
            }
            //System.out.println("result = " + b);
            return b;
        }
    }

    /**
    Parse the AICC Script and interpret if the script is true or false recursively.
    The input AICC Script should contain only one element in it
    @param aiccScript AICC Script to be interpreted
    @return if the AICC Script is true or false
    */
    private boolean interpretElementScript(String aiccScript) 
        throws SQLException {
        aiccScript = aiccScript.trim();
        long id = getElementId(aiccScript);
        char aicc_status = getAICCStatus(aiccScript);
        
        boolean b = false;
        if(aiccScript.startsWith(ELEMENT_TYPE_ITEM)) {
            b = interpretItemScript(id, aicc_status);
        }else if (aiccScript.startsWith(ELEMENT_TYPE_USER_CLASSIFICATION) 
                || aiccScript.startsWith(ELEMENT_TYPE_USERGROUP)
                || aiccScript.startsWith(ELEMENT_TYPE_USERGRADE) 
                || aiccScript.startsWith(ELEMENT_TYPE_INDUSTRY)){
            String type = getElementType(aiccScript);
            b = interpretEntScript(type, id);
        } else if(aiccScript.startsWith(ELEMENT_TYPE_COURSE)) {
            //To Be Developed
        } else if(aiccScript.startsWith(ELEMENT_TYPE_MODULE)) {
            //To Be Developed
        }
        return b;
    }

    /**
    Parse the AICC Script and interpret if the script is true or false recursively.
    The input AICC Script should contain only one item element in it
    @param aiccScript AICC Script to be interpreted
    @return if the AICC Script is true or false
    */
    private boolean interpretItemScript(long itm_id, char aicc_status) 
        throws SQLException {
        boolean b = false;
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.getCreateRunInd(this.con);
        Vector v = null;
        if(itm.itm_create_run_ind) {
            v = aeApplication.getRunAppIdByCovStatus(this.con, this.usr_ent_id, itm_id, this.due_date, aicc_status+"");
            b = (v.size() > 0);
        } else {
            v = aeApplication.getAppIdByCovStatus(this.con, this.usr_ent_id, itm_id, this.due_date, aicc_status+"");
            b = (v.size() > 0);
        }
        return b;
    }
    /**
    return true if the user is under the corresponsing ent_id 
    */

    private boolean interpretEntScript(String ent_type, long ent_id) 
        throws SQLException {
        boolean b ;
        if (this.usr_ent_id == ent_id){
            b = true;
        }else{
        	dbEntityRelation dbEr = new dbEntityRelation();
        	dbEr.ern_child_ent_id = this.usr_ent_id;
            if (ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GROUP)){
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;    
            }else if (ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GRADE)){
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR;    
            }else if (ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_INDUSTRY_CODE)){
            	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC;    
            }else{
            	dbEr.ern_type = dbEntity.ENT_TYPE_USER + dbEntityRelation.ERN_TYPE_CURRENT + ent_type;
            }
            Vector ancestorLst = dbEr.getAncestorList2Vc(con, false);
            if (ancestorLst == null || ancestorLst.size() == 0){
                b = false;
            }else{
                if (ancestorLst.contains(new Long(ent_id))){
                    b = true;
                }else{
                    b = false;
                }
            }
        }
        return b;
    }


    /**
    Parse the AICC Script and get the element type.
    The input AICC Script should contain only one element in it
    @param aiccScript AICC Script to be interpreted
    @return element id in the AICC Script 
    */
    private String getElementType(String aiccScript) {
        String type = null;
        int underscoreIndex = aiccScript.indexOf('_');
        if(underscoreIndex > -1) {
            type = aiccScript.substring(0, underscoreIndex);
        }
        return type;
    }
    
    /**
    Parse the AICC Script and get the element id.
    The input AICC Script should contain only one element in it
    @param aiccScript AICC Script to be interpreted
    @return element id in the AICC Script 
    */
    private long getElementId(String aiccScript) {
        long id = 0;
        int underscoreIndex = aiccScript.indexOf('_');
        if(underscoreIndex > -1) {
            id = Long.parseLong(aiccScript.substring(underscoreIndex+1));
        }
        return id;
    }

    /**
    Parse the AICC Script and get the status.
    The input AICC Script should contain only one element in it
    @param aiccScript AICC Script to be interpreted
    @return AICC Status in the AICC Script 
    */
    private char getAICCStatus(String aiccScript) {
        int equalIndex = aiccScript.indexOf(EQUAL);
        char aicc_status;
        if(equalIndex > -1 && equalIndex < aiccScript.length()) {
            String status = aiccScript.substring(equalIndex+1);
            if(status.length() == 1) {
                aicc_status = status.charAt(0);
            } else if(status.equalsIgnoreCase(AICC_STATUS_COMPLETED) 
                || status.equalsIgnoreCase(AICC_STATUS_PASSED)){
                aicc_status = AICC_STATUS_C;
            } else if(status.equalsIgnoreCase(AICC_STATUS_FAILED)) {
                aicc_status = AICC_STATUS_F;
            } else if(status.equalsIgnoreCase(AICC_STATUS_INCOMPLETE)) {
                aicc_status = AICC_STATUS_I;
            } else if(status.equalsIgnoreCase(AICC_STATUS_NOT_STARTED)) {
                aicc_status = AICC_STATUS_N;
            } else {
                aicc_status = AICC_STATUS_C;
            }
        } else {
            aicc_status = AICC_STATUS_C;
        }
        if(aicc_status == AICC_STATUS_P) {
            aicc_status = AICC_STATUS_C;
        }
        return aicc_status;
    }

    /**
    Breakdown the AICC Script child into String array.
    @param aiccScript AICC Script to be interpreted
    @return String array containing child of the script
    */
    private String[] getScriptChild(String aiccScript) {
        aiccScript = aiccScript.trim();
        int open_parenthesis_cnt = 0;
        StringCharacterIterator aiccScriptItr = new StringCharacterIterator(aiccScript);
        StringBuffer childBuf = new StringBuffer(128);
        Vector v = new Vector();
        for(char c=aiccScriptItr.first(); true; c=aiccScriptItr.next()) {
            //System.out.println("c = " + c);
            if(c == OPEN_PARENTHESIS) {
                open_parenthesis_cnt++;
                if(open_parenthesis_cnt > 1) {
                    childBuf.append(c);
                }
            } else if(c == CLOSE_PARENTHESIS) {
                open_parenthesis_cnt--;
                if(open_parenthesis_cnt == 0) {
                    v.addElement(childBuf.toString());
                    childBuf = new StringBuffer(128);
                } else {
                    childBuf.append(c);
                }
            } else if(c == AND || c == OR) {
                if(open_parenthesis_cnt == 0) {
                    String child = childBuf.toString();
                    if(!child.trim().equals("")) {
                        v.addElement(child);
                    }
                    v.addElement(c+"");
                    childBuf = new StringBuffer(128);
                } else {
                    childBuf.append(c);
                }
            } else if(c == StringCharacterIterator.DONE) {
                String child = childBuf.toString();
                if(!child.trim().equals("")) {
                    v.addElement(child);
                }
                break;
            } else {
                childBuf.append(c);
            }
        }
        
        return toStringArray(v);
    }
    
    private boolean validateInputUsrEntId(long usr_ent_id) {
        return usr_ent_id > 0;
    }

    private boolean validateInputAICCScript(String aiccScript) {
        return aiccScript != null;
    }

    private String[] toStringArray(Vector v) {
        String[] str = new String[v.size()];
        int size = v.size();
        for(int i=0; i<size; i++) {
            str[i] = (String)v.elementAt(i);
            //System.out.println("str[" + i + "] = " + str[i]);
        }
        return str;
    }
}