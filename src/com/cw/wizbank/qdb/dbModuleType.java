package com.cw.wizbank.qdb;

import java.util.*;

public class dbModuleType{
    
    public static Hashtable moduleType2StatusPair  = null;
    public static Hashtable moduleType2StatusXML = null;
    public static String moduleTypeXML = null;
    public static Vector hasScoreModType = null;
    public static final String MOD_TYPE_SVY     =   "SVY";
    public static final String MOD_TYPE_EAS     =   "EAS"; 

    public static final String MOD_TYPE_TST     = "TST";
    public static final String MOD_TYPE_DXT     = "DXT";
    public static final String MOD_TYPE_STX     = "STX";
    public static final String MOD_TYPE_EXC     = "EXC";
    public static final String MOD_TYPE_ASS     = "ASS";
    public static final String MOD_TYPE_CHT    = "CHT";   //Type : Chatroom
    public static final String MOD_TYPE_VCR     = "VCR";
    public static final String MOD_TYPE_FOR     = "FOR";
    public static final String MOD_TYPE_FAQ     = "FAQ";   //Type : Chatroom
    public static final String MOD_TYPE_AICC_AU = "AICC_AU";
    public static final String MOD_TYPE_NETG_COK = "NETG_COK";
    public static final String MOD_TYPE_SCO = "SCO";
    
    public static final String MOD_TYPE_VST = "VST";
    public static final String MOD_TYPE_EXM = "EXM";
    public static final String MOD_TYPE_ORI = "ORI";
    public static final String MOD_TYPE_REF = "REF";
    public static final String MOD_TYPE_GLO = "GLO";
    
    public static final String MOD_TYPE_GAG_W   = "GAG_W";
    public static final String MOD_TYPE_GAG     = "GAG";
    public static final String MOD_TYPE_LCT_W   = "LCT_W";
    public static final String MOD_TYPE_LCT     = "LCT";
    public static final String MOD_TYPE_TUT_W   = "TUT_W";
    public static final String MOD_TYPE_TUT     = "TUT";
    public static final String MOD_TYPE_RDG_W   = "RDG_W";
    public static final String MOD_TYPE_RDG     = "RDG";
    public static final String MOD_TYPE_EXP_W   = "EXP_W";
    public static final String MOD_TYPE_EXP     = "EXP";
    public static final String MOD_TYPE_VOD_W   = "VOD_W";
    public static final String MOD_TYPE_VOD     = "VOD";

    public class moduleStatus{
        String status;
        boolean isCriteria;
        moduleStatus(String status, boolean isCriteria){
            this.status = status;
            this.isCriteria = isCriteria;
        }
        String asXML(){
            StringBuffer buf = new StringBuffer();
            buf.append("<status id=\"").append(status)
                .append("\" iscriteria=\"").append(isCriteria)
                .append("\"/>");
            return buf.toString();                
        }

    }
    
    public dbModuleType(){
        
        Vector statusVector = null;
        
        moduleType2StatusPair = new Hashtable();
        moduleType2StatusXML = new Hashtable();
        
        hasScoreModType = new Vector();
        //Put Survey status adn status XML to corresponding hashtable
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("C", true));
//        statusVector.addElement(new moduleStatus("I", false));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_SVY, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_SVY, genStatusXML(MOD_TYPE_SVY, false, statusVector));
        statusVector = null;
        
        //Put EAS status adn status XML to corresponding hashtable
        hasScoreModType.addElement(MOD_TYPE_EAS);
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("P", true));
        statusVector.addElement(new moduleStatus("N", false));
        statusVector.addElement(new moduleStatus("F", false));
        moduleType2StatusPair.put(MOD_TYPE_EAS, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_EAS, genStatusXML(MOD_TYPE_EAS, false, statusVector));
        statusVector = null;

        // put gag wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_GAG_W, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_GAG_W, genStatusXML(MOD_TYPE_GAG, true, statusVector));
        statusVector = null;
        
        
        // put gag not wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_GAG, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_GAG, genStatusXML(MOD_TYPE_GAG, false, statusVector));
        statusVector = null;
        
        
        // put LCT wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_LCT_W, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_LCT_W, genStatusXML(MOD_TYPE_LCT, true, statusVector));
        statusVector = null;
        
        
        // put LCT not wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_LCT, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_LCT, genStatusXML(MOD_TYPE_LCT, false, statusVector));
        statusVector = null;


        // put TUT wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_TUT_W, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_TUT_W, genStatusXML(MOD_TYPE_TUT, true, statusVector));
        statusVector = null;
        
        
        // put TUT not wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_TUT, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_TUT, genStatusXML(MOD_TYPE_TUT, false, statusVector));
        statusVector = null;


        // put RDG wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_RDG_W, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_RDG_W, genStatusXML(MOD_TYPE_RDG, true, statusVector));
        statusVector = null;

        
        // put RDG not wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_RDG, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_RDG, genStatusXML(MOD_TYPE_RDG, false, statusVector));
        statusVector = null;
        
        
        // put EXP wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_EXP_W, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_EXP_W, genStatusXML(MOD_TYPE_EXP, true, statusVector));
        statusVector = null;
        
        
        // put EXP not wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_EXP, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_EXP, genStatusXML(MOD_TYPE_EXP, false, statusVector));
        statusVector = null;
        
        
        // put VOD wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_VOD_W, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_VOD_W, genStatusXML(MOD_TYPE_VOD, true, statusVector));
        statusVector = null;
        
        
        // put VOD not wizpack
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_VOD, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_VOD, genStatusXML(MOD_TYPE_VOD, false, statusVector));
        statusVector = null;
        
        
        // put TST
        hasScoreModType.addElement(MOD_TYPE_TST);
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", false));
        statusVector.addElement(new moduleStatus("P", true));
        statusVector.addElement(new moduleStatus("N", false));
        statusVector.addElement(new moduleStatus("F", false));
        moduleType2StatusPair.put(MOD_TYPE_TST, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_TST, genStatusXML(MOD_TYPE_TST, false, statusVector));
        statusVector = null;
        
        
        // put DXT
        hasScoreModType.addElement(MOD_TYPE_DXT);
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", false));
        statusVector.addElement(new moduleStatus("P", true));
        statusVector.addElement(new moduleStatus("N", false));
        statusVector.addElement(new moduleStatus("F", false));
        moduleType2StatusPair.put(MOD_TYPE_DXT, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_DXT, genStatusXML(MOD_TYPE_DXT, false, statusVector));
        statusVector = null;        
        
        
        // put STX
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", false));
        statusVector.addElement(new moduleStatus("P", true));
        statusVector.addElement(new moduleStatus("N", false));
        statusVector.addElement(new moduleStatus("F", false));
        moduleType2StatusPair.put(MOD_TYPE_STX, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_STX, genStatusXML(MOD_TYPE_STX, false, statusVector));
        statusVector = null;        


        // put EXC
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_EXC, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_EXC, genStatusXML(MOD_TYPE_EXC, false, statusVector));
        statusVector = null;        
        
        
        // put ASS
        hasScoreModType.addElement(MOD_TYPE_ASS);
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", false));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_ASS, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_ASS, genStatusXML(MOD_TYPE_ASS, false, statusVector));
        statusVector = null;        


        // put VCR
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_VCR, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_VCR, genStatusXML(MOD_TYPE_VCR, false, statusVector));
        statusVector = null;        


        // put FOR
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_FOR, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_FOR, genStatusXML(MOD_TYPE_FOR, false, statusVector));
        statusVector = null;        


        // put CHT
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_CHT, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_CHT, genStatusXML(MOD_TYPE_CHT, false, statusVector));
        statusVector = null;        

        
        // put FAQ
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_FAQ, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_FAQ, genStatusXML(MOD_TYPE_FAQ, false, statusVector));
        statusVector = null;        

        
        // put VST
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_VST, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_VST, genStatusXML(MOD_TYPE_VST, false, statusVector));
        statusVector = null;        

        
        // put EXM
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_EXM, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_EXM, genStatusXML(MOD_TYPE_EXM, false, statusVector));
        statusVector = null;        

        
        // put ORI
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_ORI, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_ORI, genStatusXML(MOD_TYPE_ORI, false, statusVector));
        statusVector = null;        

        
        // put REF
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_REF, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_REF, genStatusXML(MOD_TYPE_REF, false, statusVector));
        statusVector = null;        

        
        // put GLO
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("N", false));
        moduleType2StatusPair.put(MOD_TYPE_GLO, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_GLO, genStatusXML(MOD_TYPE_GLO, false, statusVector));
        statusVector = null;
        
        
        // put AICC_AU
        hasScoreModType.addElement(MOD_TYPE_AICC_AU);
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("P", true));
        statusVector.addElement(new moduleStatus("N", false));
        statusVector.addElement(new moduleStatus("F", false));
        statusVector.addElement(new moduleStatus("B", false));
        moduleType2StatusPair.put(MOD_TYPE_AICC_AU, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_AICC_AU, genStatusXML(MOD_TYPE_AICC_AU, false, statusVector));
        statusVector = null;   
        
        
        // put NETG_COK
        hasScoreModType.addElement(MOD_TYPE_NETG_COK);
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", false));
        statusVector.addElement(new moduleStatus("P", false));
        statusVector.addElement(new moduleStatus("N", false));
        statusVector.addElement(new moduleStatus("F", false));
        moduleType2StatusPair.put(MOD_TYPE_NETG_COK, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_NETG_COK, genStatusXML(MOD_TYPE_NETG_COK, false, statusVector));
        statusVector = null;        


        // put sco
        statusVector = new Vector();
        statusVector.addElement(new moduleStatus("I", true));
        statusVector.addElement(new moduleStatus("C", true));
        statusVector.addElement(new moduleStatus("P", true));
        statusVector.addElement(new moduleStatus("N", false));
        statusVector.addElement(new moduleStatus("F", false));
        statusVector.addElement(new moduleStatus("B", false));
        moduleType2StatusPair.put(MOD_TYPE_SCO, statusVector);
        moduleType2StatusXML.put(MOD_TYPE_SCO, genStatusXML(MOD_TYPE_SCO, false, statusVector));
        statusVector = null;        
        
        
        moduleTypeXML = getAllModuleStatusXML();
        return;

    }
    
    
    private String genStatusXML(String modType, boolean isWizpack, Vector statusVec){
        
        StringBuffer buf = new StringBuffer(512);
        boolean isWizpacker = false;
        buf.append("<module type=\"").append(modType).append("\" wizpack_ind=\"").append(isWizpack).append("\">");

        String modType_map_str = modType;
        if (isWizpack){
            modType_map_str += "_W";
        }
        buf.append("<module_type_detail hasScore=\"").append(hasScoreModType.contains(modType_map_str)).append("\" />"); 

        buf.append("<module_status_list>");
        for(int i=0; i<statusVec.size(); i++)
            buf.append(((moduleStatus)statusVec.elementAt(i)).asXML());
        buf.append("</module_status_list>")
           .append("</module>");

        return buf.toString();
    
    }
    // will store in a static variable moduleTypeXML
    public static String getAllModuleStatusXML(){
        if (moduleTypeXML==null){
            StringBuffer buf = new StringBuffer();
            buf.append("<module_type_reference_data>");
            Enumeration enumeration = moduleType2StatusXML.elements();
            while (enumeration.hasMoreElements()) {
                buf.append((String)enumeration.nextElement());
            }
            buf.append("</module_type_reference_data>");
            moduleTypeXML = buf.toString();
        }
        return moduleTypeXML; 
    }
    
    public static Vector getStatusVector(String muduleName){
        Vector temp = (Vector)moduleType2StatusPair.get(muduleName);
        Vector statusVec = new Vector();
        for(int i=0; i < temp.size(); i++)
            statusVec.addElement(((moduleStatus)temp.elementAt(i)).status);
        return statusVec; 
    }
    
}