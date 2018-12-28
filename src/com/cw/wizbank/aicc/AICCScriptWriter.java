package com.cw.wizbank.aicc;

/**
class for writing aicc script
*/
public class AICCScriptWriter {
    
    /*
    static final String _AND = " & ";
    static final String _OR = " | ";
    static final String _EQUAL = " = ";
    static final String _OPEN_PARENTHESIS = " ( ";
    static final String _CLOSE_PARENTHESIS = " ) ";
    */
    
    static final String AND = " " + AICCScriptInterpreter.AND + " "; 
    static final String OR = " " + AICCScriptInterpreter.OR + " ";
    static final String EQUAL = " " + AICCScriptInterpreter.EQUAL + " ";
    static final String OPEN_PARENTHESIS = " " + AICCScriptInterpreter.OPEN_PARENTHESIS + " ";
    static final String CLOSE_PARENTHESIS = " " + AICCScriptInterpreter.CLOSE_PARENTHESIS + " ";
    
    public static final String ELEMENT_TYPE_MODULE = AICCScriptInterpreter.ELEMENT_TYPE_MODULE;
    public static final String ELEMENT_TYPE_COURSE = AICCScriptInterpreter.ELEMENT_TYPE_COURSE;
    public static final String ELEMENT_TYPE_ITEM = AICCScriptInterpreter.ELEMENT_TYPE_ITEM;
    
    /**
    Write an AICC script to return true 
    if any one of the input element is "completed" or "passed"
    @param elementType element type of elementIdArray
    @param elementIdArray element id array
    @return String of AICC script
    */
    public String writeAnyOf(String[] element) {
        StringBuffer scriptBuf = new StringBuffer(128);
        int size = element.length;
        if(size > 0) {
            scriptBuf.append(OPEN_PARENTHESIS);
            for(int i=0; i<size; i++) {
                if(i > 0) {
                    scriptBuf.append(OR);
                }
                scriptBuf.append(element[i]);
            }
            scriptBuf.append(CLOSE_PARENTHESIS);
        }
        return scriptBuf.toString();
    }

    /*
    return String array as element on aicc condition 
    @param elementType element type of elementIdArray
    @param elementIdArray element id array
    */
    public String[] convertToElement(String elementType, long[] elementIdArray){
        String[] element = new String[elementIdArray.length];
        if(elementType == null) {
            elementType = "";
        }
        for (int i=0; i<element.length; i++){
            element[i] = elementType + elementIdArray[i];
        }
        return element;
    }

    /**
    Write an AICC script to return true 
    if any one of the input element is "completed" or "passed"
    @return String of AICC script
    */
    public String writeAllOf(String[] element) {
        StringBuffer scriptBuf = new StringBuffer(128);
        int size = element.length;
        if(size > 0) {
            scriptBuf.append(OPEN_PARENTHESIS);
            for(int i=0; i<size; i++) {
                if(i > 0) {
                    scriptBuf.append(AND);
                }
                scriptBuf.append(element[i]);
            }
            scriptBuf.append(CLOSE_PARENTHESIS);
        }
        return scriptBuf.toString();
    }

    

}