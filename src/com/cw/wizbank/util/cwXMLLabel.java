package com.cw.wizbank.util;

import java.io.*;

public class cwXMLLabel{
    
    public final static String LABEL_FILE_TYPE  =   "xml";
    public final static String LABEL_PATH       =   "label";
    
    public static String DOC_ROOT         =   null;
    public static String INI_XSL_HOME     =   null;
    public static String DEFAULT_ENC      =   null;
    
    /*
    public static String get(String xsl_filename, loginProfile prof)
        throws IOException, cwException {
            
            String xsl_root = (prof == null) ? null : prof.xsl_root;
            
            String encoding = (prof == null) ? null : prof.lan_label;
            
            return get(xsl_filename, xsl_root, encoding);
            
        }
    */    
    public static String get(String xsl_filename, String xsl_root, String encoding)
        throws IOException {
            File xmlFile = getXMLFile(xsl_filename, xsl_root, encoding);
            if( xmlFile == null )
                xmlFile = getXMLFile(xsl_filename, INI_XSL_HOME, encoding);
            
            if( xmlFile == null )
            //    throw new cwException("Failed to get xml label, xsl_filename = " + xsl_filename + " , xsl_root = " + xsl_root + " , encoding = " + encoding);
                return "";

            String xml = readLabel(xmlFile, encoding);
            return xml;
                
        }
        

    private static String readLabel(File xmlFile, String encoding)
        throws IOException{
            StringBuffer xml = new StringBuffer();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(xmlFile), encoding));
            String buf = null;
            while( (buf = bReader.readLine()) != null )
                xml.append(buf).append(cwUtils.NEWL);
            bReader.close();
            return xml.toString();
        }
        
    private static File getXMLFile(String xsl_filename, String xsl_root, String encoding)
        throws IOException {
            File xmlFile = null;
            File xmlPath = new File(DOC_ROOT,xsl_root);
          
            if( !xmlPath.exists() )
                return null;
            else {
                xmlPath = new File(xmlPath, LABEL_PATH);

                if( !xmlPath.exists() )
                    return null;
                else {
                    xmlPath = new File(xmlPath, encoding);

                    if( !xmlPath.exists() )
                        return null;
                    else {
                        xmlFile = new File(xmlPath, getXMLFilename(xsl_filename));

                        if( !xmlFile.exists() )
                            return null;                       
                    }
                }
            }
            return xmlFile;            
        }
        
        
    private static String getXMLFilename(String xsl_filename)
        throws IOException{
            
            if( xsl_filename == null || xsl_filename.length() == 0 )
                throw new IOException("Invalid xsl filename , filename = " + xsl_filename);
            
            int index = xsl_filename.lastIndexOf(".") + 1;
            return xsl_filename.substring(0, index) + LABEL_FILE_TYPE;
        }
}