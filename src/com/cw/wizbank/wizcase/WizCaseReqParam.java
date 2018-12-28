package com.cw.wizbank.wizcase;

import javax.servlet.ServletRequest;

import java.util.*;

import com.cw.wizbank.*;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

import org.w3c.dom.*;
import org.apache.xerces.dom.DocumentImpl;

public class WizCaseReqParam extends ReqParam {

    public final static String DELIMITER            =   "~";
    public final static String DEFAULT_XML_DIRECTORY = "xml";
    public static final String SEPARATOR = ":_:_:";
    
    public WizCaseReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;
            
            Enumeration enumeration = req.getParameterNames();
            while( enumeration.hasMoreElements() ) {
                String name = (String) enumeration.nextElement();
                String[] values = req.getParameterValues(name);
                if( values != null )
                    for(int i=0; i<values.length; i++)
                    	CommonLog.info(name + " (" + i + "):" + values[i]);
            }
            
            //Get the module id from Session ID
            String session_id = req.getParameter("session_id");
            if( session_id == null || session_id.length() == 0 ) {
                throw new cwException("Failed to get session id, please launch the course again.");
            }else {
                Hashtable aiccData = parseSessionId(session_id);
                mod_id = ((Long)aiccData.get("mod_id")).longValue();
                cos_id = ((Long)aiccData.get("cos_id")).longValue();
                ent_id = ((Long)aiccData.get("ent_id")).longValue();
            }

        }

    public Vector blockIdVec;
    public Vector elementVec;
    public Vector pageBreakVec;

    public String next_type;
    public String formFilename;
    public String developer_id;    
    public String correct_form_id;    
    
    public String[] formsFilename;
    public String[] developersId;
    public String[] toc;

    //public String formName;
    public String formPage;
    public String formLineNum;

    public String default_xml_dir;

    //precompile word xsl
    String word_stylesheet;

    long mod_id;
    long cos_id;
    long ent_id;
    int trial;
    boolean cache;
    
    public void reset()
        throws cwException {
            
            default_xml_dir = getStringParameter("default_xml_dir");
            return;
            
    }
    
    
    public void nextStep()
        throws cwException {

            formPage = getStringParameter("form_page");
            
            formLineNum = getStringParameter("form_line_num");
            
            next_type = getStringParameter("next_type");

            formFilename = getStringParameter("form_filename");
            
            developer_id = getStringParameter("developer_id");      
            
            correct_form_id = getStringParameter("correct_form_id");
                        
            default_xml_dir = getStringParameter("default_xml_dir");
            
            trial = getIntParameter("trial");                        
            
            cache = getBooleanParameter("cache");
            
            String tocStr = getStringParameter("toc");
            toc = splitToString(tocStr, DELIMITER);

            Document doc= new DocumentImpl();

            blockIdVec = new Vector();
            pageBreakVec= new Vector();
            elementVec = new Vector();

            Element element;
            String[] name = null;
            String[] value = null;
            String[] blockId = null;            
            //int i = 1;
            
            String params = getStringParameter("params");
/*
            while( (blockId = getStringParameter("paramBlockId_" + i)) != null )
            {

                    blockIdVec.addElement(cwUtils.splitToLong(blockId, "s"));
                    element = doc.createElement("input");
                    
                    value = unicode(getStringParameter("paramValue_" + i));
                    name = unicode(getStringParameter("paramName_" + i));
                    
                    if( name != null )
                        element.setAttribute("name", name);
                    if( value != null )
                        element.appendChild(doc.createTextNode(value));
                    
                    if( name != null || value != null )
                        elementVec.addElement(element);
                    else
                        elementVec.addElement(null);
                    i++;
                }
*/                

            
            Character PARAM_DELIMITER        = new Character((char)17);
            Character PARAM_PAIR_DELIMITER   = new Character((char)16);
            //String PARAM_DELIMITER              =   "[~]";
            //String PARAM_PAIR_DELIMITER         =   "[|]";


            String[] paramString = splitToString(params,PARAM_DELIMITER.toString());
            String[] param;
            for(int i=0; i<paramString.length; i++) {
            
                if( paramString[i] == null || paramString[i].length() == 0)
                    continue;
                param = splitToString(paramString[i],PARAM_PAIR_DELIMITER.toString());
            
                    blockIdVec.addElement(cwUtils.splitToLong(param[0], "s"));
                    element = doc.createElement("input");
                                        
                    if( param[1] != null && param[1].length() > 0)
                        element.setAttribute("name", param[1]);
                    if( param[2] != null && param[2].length() > 0)
                        element.appendChild(doc.createTextNode(param[2]));
                    
                    if( param[3] != null && param[3].length() > 0)
                        pageBreakVec.addElement(param[3]);
                    else
                        pageBreakVec.addElement(null);
                    
                    if(  ( param[1] != null && param[1].length() > 0 ) 
                      || ( param[2] != null && param[2].length() > 0 ) )
                        elementVec.addElement(element);
                    else
                        elementVec.addElement(null);             

            }
            
            
            if( (word_stylesheet = getStringParameter("word_stylesheet")) == null )
                 word_stylesheet = "wizcase_word_ind.xsl";
            
            /*
            for(int i=0; i< 50; i++)
                System.out.println(" i = " + i + " : " + new Character((char)i));
            
            
            String asc = getStringParameter("acs");
            Character _PARAM_DELIMITER        = new Character((char)16);
            Character _PARAM_PAIR_DELIMITER   = new Character((char)17);
            System.out.println("delimiter 1 " + _PARAM_DELIMITER);
            System.out.println("delimiter 2 " + _PARAM_PAIR_DELIMITER);
            String[] splited = cwUtils.splitToString(asc, _PARAM_DELIMITER.toString());
            for(int i=0; i< splited.length;i++)
                System.out.println(" i " + i + " : " + splited[i]);
            */
            return;
        }
/*
    public void prevStep()
        throws cwException {

            next_type = getStringParameter("next_type");

            return;
        }
*/

    

    public void preview()
        throws cwException {
            
            String filename = getStringParameter("form_filename");
            String developerId = getStringParameter("developer_id");
            
            formsFilename = splitToString(filename, DELIMITER);
            
            developersId = splitToString(developerId, DELIMITER);            

            cache = getBooleanParameter("cache");            
            
            next_type = getStringParameter("next_type");
            
            if( (word_stylesheet = getStringParameter("word_stylesheet")) == null )
                 word_stylesheet = "wizcase_word_ind.xsl";
                 
            return;
        }



    public static String[] splitToString(String in, String delimiter) {
        
        String obj[] = null;
        if( in == null || in.length() == 0 ) {
            obj = new String[0];
            return obj;
        }
        
        Vector q = new Vector();
        int pos =0;
        pos = in.indexOf(delimiter);
        
        while (pos >= 0) {
            String val = new String();
            val = in.substring(0,pos); 
            q.addElement(val);
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }
        q.addElement(in);


        obj = new String[q.size()];
        for (int i=0; i<obj.length;i++) {
            obj[i] = (String)q.elementAt(i);
        }
        
        return obj;
    }



/*
public String convert2DeveloperId(String str){
    
    System.out.println("str = " + str);
    if( str == null || str.length() == 0 )
        return str;
        
    int length = str.indexOf(".");
    if(length == -1)
        return str;
    System.out.println("Length = " + length);
    return "WIZCASE"+str.substring(0,length);
    
    
}
*/
    
    
    public Hashtable parseSessionId(String sessID) 
        throws cwException{

        String[] lst = null;
        int error = 0;;
        Hashtable aiccData = new Hashtable();
        lst = splitToString(sessID, SEPARATOR);

        if (lst != null && lst.length > 4) {
            if (lst[0] == null || Long.parseLong(lst[0]) == 0 ||
                lst[1] == null || Long.parseLong(lst[1]) == 0 ||
                lst[2] == null || Long.parseLong(lst[2]) == 0 ||
                lst[3] == null || 
                lst[4] == null) {
                error = 3;
            } else { 
                aiccData.put("ent_id", new Long(lst[0]));
                aiccData.put("cos_id", new Long(lst[1]));
                aiccData.put("mod_id", new Long(lst[2]));
                aiccData.put("mod_vendor", lst[3]);
                aiccData.put("sess_id_time", lst[4]);
/*
                if (prof != null && Long.parseLong(lst[0]) != prof.usr_ent_id ) {
                    error = 3;   
                }
*/
            }
        } else {

            error = 3;   

        }
        if( error == 3 )
            throw new cwException("Failed to parse Session ID");
        return aiccData;
    }
     


}