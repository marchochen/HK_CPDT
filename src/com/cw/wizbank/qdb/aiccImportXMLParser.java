package com.cw.wizbank.qdb;

import org.xml.sax.AttributeList; 
import org.xml.sax.HandlerBase; 
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException; 
import org.xml.sax.InputSource;

//import com.ibm.xml.parsers.SAXParser; 
import org.apache.xerces.parsers.SAXParser;

import java.io.*;
import java.util.*;

/** 
 * aiccImportXMLParser.java 
 * This sample program illustrates how to use a SAX parser.  It 
 * parses a document and writes the documentˇs contents back to  
 * standard output.  
 */ 

public class aiccImportXMLParser
  extends HandlerBase  
{ 
    
  static String _COS_IMPORT_XML = "COS_IMPORT_XML";
  static String _MOD_IMPORT_XML = "MOD_IMPORT_XML";
  static String _OBJ_IMPORT_XML = "OBJ_IMPORT_XML";

  private String import_xml_type = null;
  
  private Hashtable htCosImportXML = null;
  private Hashtable htModImportXML = null;
  private Hashtable htObjImportXML = null;
  
  private Stack stackCosStruct = null;
  
  boolean boolWithinCosStructure = false;
  boolean boolWithinDescriptor = false;
  
  public aiccImportXMLParser(String type) {
    import_xml_type = type;
    if (import_xml_type.compareTo(_COS_IMPORT_XML) == 0) {
        htCosImportXML = new Hashtable();
    }
    else if (import_xml_type.compareTo(_MOD_IMPORT_XML) == 0) {
        htModImportXML = new Hashtable();
    }
    else if (import_xml_type.compareTo(_OBJ_IMPORT_XML) == 0) {
        htObjImportXML = new Hashtable();
    }
  }

  public Hashtable parseXML(String xmlText) 
  { 
    try 
    {  
        InputSource isXML = new InputSource(new ByteArrayInputStream(xmlText.getBytes(dbUtils.ENC_UTF)));
        parseURI(isXML);
        if (import_xml_type.compareTo(_COS_IMPORT_XML) == 0) {
            return htCosImportXML;
        }
        else if (import_xml_type.compareTo(_MOD_IMPORT_XML) == 0) {
            return htModImportXML;
        }
        else if (import_xml_type.compareTo(_OBJ_IMPORT_XML) == 0) {
            return htObjImportXML;
        }
        else {
            return null;
        }
    } 
    catch (Exception e) 
    { 
      System.err.println(e); 
      return null;
    } 
  }

  public Hashtable parseXML(Reader readerXML) 
  { 
    try 
    {  
        InputSource isXML = new InputSource(readerXML);
        parseURI(isXML);
        if (import_xml_type.compareTo(_COS_IMPORT_XML) == 0) {
            return htCosImportXML;
        }
        else if (import_xml_type.compareTo(_MOD_IMPORT_XML) == 0) {
            return htModImportXML;
        }
        else if (import_xml_type.compareTo(_OBJ_IMPORT_XML) == 0) {
            return htObjImportXML;
        }
        else {
            return null;
        }
    } 
    catch (Exception e) 
    { 
      System.err.println(e);
      return null;
    }    
  }

  public void parseXML(InputStream istreamXML) 
  { 
    try 
    {  
      InputSource isXML = new InputSource(istreamXML);
      parseURI(isXML);
    } 
    catch (Exception e) 
    { 
      System.err.println(e); 
    } 
  }
    
  public void parseURI(InputSource isXML) 
  { 
    SAXParser parser = new SAXParser(); 
    parser.setDocumentHandler(this); 
    parser.setErrorHandler(this); 
    try 
    {  
      parser.parse(isXML); 
    } 
    catch (Exception e) 
    { 
      System.err.println(e); 
    } 
  } 
     
  /** Processing instruction. */ 
  public void processingInstruction(String target, String data)  
  { 
//    System.out.print("<?"); 
//    System.out.print(target); 
    if (data != null && data.length() > 0) 
    { 
//      System.out.print(' '); 
//      System.out.print(data); 
    } 
//    System.out.print("?>"); 
  }  

  /** Start document. */ 
  public void startDocument()  
  { 
//    System.out.println("<?xml version=\"1.0\"?>"); 
  } 

  /** Start element. */ 
  public void startElement(String name, AttributeList attrs)  
  { 
    Hashtable htCurrTable = null;
    Hashtable vtCurrTable = null;
    
//    System.out.print("<"); 
//    System.out.print(name); 
    
    if (attrs != null) {         
        if (import_xml_type.compareTo(_COS_IMPORT_XML) == 0) {
            if (name.equalsIgnoreCase("course_gen_description") == true) {
                Hashtable htCourseGenDesc = new Hashtable();
                htCosImportXML.put("course_gen_description", htCourseGenDesc);
            }
            if (name.equalsIgnoreCase("course") == true) {
                Hashtable htCourseGenDesc = (Hashtable)htCosImportXML.get("course_gen_description");
                Hashtable htCourse = new Hashtable();
                htCourseGenDesc.put("course", htCourse);
                htCurrTable = htCourse;
            }
            if (name.equalsIgnoreCase("course_behavior") == true) {
                Hashtable htCourseGenDesc = (Hashtable)htCosImportXML.get("course_gen_description");
                Hashtable htCourseBehavior = new Hashtable();
                htCourseGenDesc.put("course_behavior", htCourseBehavior);
                htCurrTable = htCourseBehavior;
            }
            if (name.equalsIgnoreCase("course_description") == true) {
                Hashtable htCourseGenDesc = (Hashtable)htCosImportXML.get("course_gen_description");
                Hashtable htCourseDescription = new Hashtable();
                htCourseGenDesc.put("course_description", htCourseDescription);
                htCurrTable = htCourseDescription;
            }
            if (name.equalsIgnoreCase("course_structure") == true) {
                boolWithinCosStructure = true;
                stackCosStruct = new Stack();
                
                Hashtable htCourseStruct = new Hashtable();
                htCosImportXML.put("course_structure", htCourseStruct);                
            }            
            if (name.equalsIgnoreCase("descriptor") == true) {
                boolWithinDescriptor = true;
                
                Hashtable htDescriptor = new Hashtable();
                htCosImportXML.put("descriptor", htDescriptor);                
            }            
            if (name.equalsIgnoreCase("element") == true) {
                if (boolWithinCosStructure == true) {
                    Vector vtChildSystemID = null;
                    String elementSystemID = null;
                    String parentSystemID = null;
                    Hashtable htCourseStruct = (Hashtable)htCosImportXML.get("course_structure");
                    elementSystemID = attrs.getValue("system_id");
                    if (stackCosStruct.isEmpty() == false) {
                        parentSystemID = (String)stackCosStruct.peek();
                        vtChildSystemID = (Vector)htCourseStruct.get(parentSystemID);
                        vtChildSystemID.add(elementSystemID);
                    }
                    
                    Vector vtNewChildSystemID = new Vector();
                    htCourseStruct.put(elementSystemID, vtNewChildSystemID);
                    
                    // put the name into the stack
                    stackCosStruct.push(elementSystemID);
                }
                if (boolWithinDescriptor == true) {
                    Hashtable htDescriptor = (Hashtable)htCosImportXML.get("descriptor");
                    Hashtable htDescriptorRecord = new Hashtable();
                    htDescriptor.put(attrs.getValue("system_id"), htDescriptorRecord);
                    htCurrTable = htDescriptorRecord;
                }
            }
        }
        else if (import_xml_type.compareTo(_MOD_IMPORT_XML) == 0) {
        }
        else if (import_xml_type.compareTo(_OBJ_IMPORT_XML) == 0) {
        }
        
        int len = attrs.getLength(); 
        for (int i = 0; i < len; i++) { 
//            System.out.print(" "); 
//            System.out.print(attrs.getName(i)); 
//            System.out.print("=\""); 
//            System.out.print(attrs.getValue(i)); 
//            System.out.print("\""); 
            
            String attrsName = attrs.getName(i);
            String attrsValue = attrs.getValue(i);
            if (import_xml_type.compareTo(_COS_IMPORT_XML) == 0) {
                if (name.equalsIgnoreCase("course") == true || name.equalsIgnoreCase("course_behavior") == true || name.equalsIgnoreCase("course_description") == true) {
                    htCurrTable.put(attrsName.toLowerCase(), attrsValue);
                }
                if (name.equalsIgnoreCase("element") == true) {
                    if (boolWithinCosStructure == true) {
                        // do nothing
                    }
                    if (boolWithinDescriptor == true) {
                        htCurrTable.put(attrsName.toLowerCase(), attrsValue);
                    }
                }
            }
            else if (import_xml_type.compareTo(_MOD_IMPORT_XML) == 0) {
                if (name.equalsIgnoreCase("assignable_unit") == true) {
                    htModImportXML.put(attrsName.toLowerCase(), attrsValue);
                }
            }
            else if (import_xml_type.compareTo(_OBJ_IMPORT_XML) == 0) {
            }
        }         
    } 
//    System.out.print(">"); 
  }  

  /** Characters. */ 
  public void characters(char ch[], int start, int length)  
  { 
    if (import_xml_type.compareTo(_COS_IMPORT_XML) == 0) {
    }
    else if (import_xml_type.compareTo(_MOD_IMPORT_XML) == 0) {
    }
    else if (import_xml_type.compareTo(_OBJ_IMPORT_XML) == 0) {
    }
//    System.out.print(new String(ch, start, length)); 
  }  

  /** Ignorable whitespace. */ 
  public void ignorableWhitespace(char ch[], int start, int length)  
  { 
    characters(ch, start, length); 
  }  

  /** End element. */ 
  public void endElement(String name)  
  { 
    if (import_xml_type.compareTo(_COS_IMPORT_XML) == 0) {
        if (name.equalsIgnoreCase("course_structure") == true) {
            boolWithinCosStructure = false;
        }
        if (name.equalsIgnoreCase("descriptor") == true) {
            boolWithinDescriptor = false;
        }
        if (name.equalsIgnoreCase("element") == true) {
            if (boolWithinCosStructure == true) {
                if (stackCosStruct.empty() == false) {
                    stackCosStruct.pop();
                }
            }
            if (boolWithinDescriptor == true) {
                // do nothing
            }
        }
    }
    else if (import_xml_type.compareTo(_MOD_IMPORT_XML) == 0) {
    }
    else if (import_xml_type.compareTo(_OBJ_IMPORT_XML) == 0) {
    }
//    System.out.print("</"); 
//    System.out.print(name); 
//    System.out.print(">"); 
  }  

  /** End document. */ 
  public void endDocument()  
  { 
  }  

  // 
  // ErrorHandler methods 
  // 

  /** Warning. */ 
  public void warning(SAXParseException ex)  
  { 
    System.err.println("[Warning] "+ 
                       getLocationString(ex)+": "+ 
                       ex.getMessage()); 
  } 

  /** Error. */ 
  public void error(SAXParseException ex)  
  { 
    System.err.println("[Error] "+ 
                       getLocationString(ex)+": "+ 
                       ex.getMessage()); 
  } 

  /** Fatal error. */ 
  public void fatalError(SAXParseException ex)  
    throws SAXException  
  { 
    System.err.println("[Fatal Error] "+ 
                       getLocationString(ex)+": "+ 
                       ex.getMessage()); 
    throw ex; 
  } 

  /** Returns a string of the location. */ 
  private String getLocationString(SAXParseException ex)  
  { 
    StringBuffer str = new StringBuffer(); 

    String systemId = ex.getSystemId(); 
    if (systemId != null) 
    { 
      int index = systemId.lastIndexOf('/'); 
      if (index != -1) 
        systemId = systemId.substring(index + 1); 
      str.append(systemId); 
    } 
    str.append(':'); 
    str.append(ex.getLineNumber()); 
    str.append(':'); 
    str.append(ex.getColumnNumber()); 

    return str.toString(); 
  }  
}