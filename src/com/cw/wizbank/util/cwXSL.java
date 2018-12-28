/*

public static void procAbsoluteXSLFile(String xml, String absoluteXSLFile, 
                        PrintWriter resultWriter, boolean debug, boolean comp_xsl)

    Process XSL transfromation combining with the XML stream
    Parameters : 
        xml -- string containing the xml
        absoluteXSLFile -- the absolute path of the xsl file
        resultWriter -- printwriter that output the generated HTM 
        debug -- debug mode
        comp_xsl -- the stylesheet is complied

public static void processFiles(String absoluteXMLFile, String absoluteXSLFile,
                       PrintWriter resultWriter, boolean debug, boolean comp_xsl)

    Process XSL transfromation combining with the XML file
    Parameters : 
        absoluteXMLFile -- the absolute path of the xml file
        absoluteXSLFile -- the absolute path of the xsl file
        resultWriter -- printwriter that output the generated HTM 
        debug -- debug mode
        comp_xsl -- the stylesheet is complied


*/

package com.cw.wizbank.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.cw.wizbank.JsonMod.commonBean.GoldenManBean;
import com.cw.wizbank.JsonMod.supervise.bean.StaffReportBean;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbEnv;
import com.cwn.wizbank.utils.CommonLog;

/**
A class to process xsl transformation
*/
public class cwXSL {
    
    /**
    Number of try on xsl transformation
    */
    private static final int MAX_TRY = 5;
    /**
    Cached XSL StylesheetRoot
    */
    private static Hashtable tbStylesheetRoot = new Hashtable();
    private static String GOLDENMAN_REG_GRADE="goldenman_reg_grade";
    
    
    /**
    Constructor
    */
    public cwXSL() {;}
    
    public static String GOLDENMAN ="goldenman";
    public static String GOLDENMANOPTIONS="goldenmanoptions";
    
    private static Hashtable goldenManHtml = new Hashtable();
	private static String goldenManTransferedXml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><goldenman><cur_usr label=\"";
	private static String goldenManTransferedXml2 = "\"/></goldenman>";

    /**
    Load specified xsl files to memory
    @param filename full path of the xsl processing configuration file
    */
    public void loadCache(String doc_root, String filename, File compiledXslHome) throws cwException
    {
    	CommonLog.info("Start loading xsl cache...");
        // Clear all the cache and free the memory.
        tbStylesheetRoot.clear();
        tbStylesheetRoot = new Hashtable();
        Runtime runT = Runtime.getRuntime();
        runT.gc();
        
        Vector xslVec = new Vector();
        Vector xslOrgNameVec = new Vector();
        boolean isCompiled = false;
        long memReserved = 32 * 1024 * 1024;         // Default : Reserv at least 32M
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            
            String line = in.readLine();
            String xslFolder = new String();
            String FOLDER = "FOLDER=";
            String COMPILED = "COMPILED=";
            String MEMORY = "MEMORY_RESERVED=";
            
            while (line != null) {
                line.trim();
                
                // skip all comments
                if (!line.startsWith("#") && line.length() > 0) {
                    // Check if the xsl files are compiled
                    if (line.startsWith(COMPILED)) {
                        String val = line.substring(line.indexOf(COMPILED) + COMPILED.length() , line.length());
                        if (val !=null && val.equalsIgnoreCase("TRUE"))
                            isCompiled = true;
                    }
                    // Get the minium memory 
                    else if (line.startsWith(MEMORY)) {
                        String val = line.substring(line.indexOf(MEMORY) + MEMORY.length() , line.length());
                        try {
                            memReserved = Integer.parseInt(val) * 1024 * 1024;
                        }catch (NumberFormatException e) {
                            // do nothing, use default
                        }                                
                    }
                    // Check the folder of the coming xsl files
                    else if (line.startsWith(FOLDER)) {
                        xslFolder = line.substring(line.indexOf(FOLDER) + FOLDER.length() , line.length());
                    // Add the xsl file to the list
                    }else {
                        String xslfile = new String(doc_root + cwUtils.SLASH + xslFolder + cwUtils.SLASH + line);
                        xslVec.addElement(xslfile);
                        xslOrgNameVec.addElement(xslFolder + cwUtils.SLASH + line);
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (IOException e) {
            throw new cwException("error in reading " + filename + ":" + e.getMessage());
        }
        CommonLog.info("no. of xsl file(s) to be loaded:" + xslVec.size());
        
        TransformerFactory tFactory = TransformerFactory.newInstance();
        for (int i=0; i<xslVec.size();i++) {
            // Not enough memory , skip the others xsl
            if (runT.freeMemory() < memReserved) {
                runT.gc();
                CommonLog.info("Memory : Garbage collected.");
                CommonLog.info("Memory : Total=" + runT.totalMemory()  + ", Reserved=" + memReserved + ", Free=" + runT.freeMemory());
                if (runT.freeMemory() < memReserved) {
                	CommonLog.info("not enough memory, stop loading any more xsl files");
                    break;
                }
            }
            File xslTextFile = new File((String)xslVec.elementAt(i));
            File xslCompiledFile = new File(compiledXslHome, (String)xslOrgNameVec.elementAt(i));
            compileXSL(tFactory, xslTextFile, xslCompiledFile, null, false);
            CommonLog.info("Memory : Total=" + runT.totalMemory()  + ", Reserved=" + memReserved + ", Free=" + runT.freeMemory());
        }
        
        CommonLog.info("no. of xsl file(s) loaded:" + tbStylesheetRoot.size());
        runT.gc();
        CommonLog.info("Memory : Garbage collected.");
        CommonLog.info("Memory : Total=" + runT.totalMemory()  + ", Reserved=" + memReserved + ", Free=" + runT.freeMemory());
        CommonLog.info("End loading xsl cache.");
    }
    
    /**
     * Load a single xsl file into cache
     */
    public boolean loadCacheSingle(String docRoot, String xslFolder, String xslFilename, File compiledXslHome, PrintWriter out, boolean force_reload_xsl)
        throws cwException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        File xslTextFile = new File(docRoot + cwUtils.SLASH + xslFolder + cwUtils.SLASH + xslFilename);
        File xslCompiledFile = new File(compiledXslHome, xslFolder + cwUtils.SLASH + xslFilename);
        return compileXSL(tFactory, xslTextFile, xslCompiledFile, out, force_reload_xsl);
    }
    private class errorListener implements ErrorListener{

    	public PrintWriter out;
    	
    	public errorListener(PrintWriter pwout){
    		if(pwout != null){
    			this.out = pwout;
    		} else {
    			this.out = new PrintWriter(System.out, true);
    		}
    	}
		public void warning(TransformerException exception) throws TransformerException {
			CommonLog.error(exception.getLocalizedMessage());
			throw exception;
		}

		public void error(TransformerException exception) throws TransformerException {
			CommonLog.error(exception.getLocalizedMessage());
			out.print(exception.getLocalizedMessage() + "<br>");
			throw exception;
		}

		public void fatalError(TransformerException exception) throws TransformerException {
			CommonLog.error(exception.getLocalizedMessage());
			throw exception;
		}
    	
    }
    private boolean compileXSL(TransformerFactory tFactory, File inTextObj, File inCompiledObj, PrintWriter out, boolean force_reload_xsl) {
    	boolean saved = true;
    	tFactory.setErrorListener(new errorListener(out));
        String textFileFullPath = inTextObj.getAbsolutePath();
        String BR = "<br>";
        if(out == null){
        	out = new PrintWriter(System.out, true);
        	BR = "";
        }
        try {
            Templates stylesheet = null;
//            if (inCompiledObj.exists() && inTextObj.exists()
//            		&& inCompiledObj.lastModified() > inTextObj.lastModified() && !force_reload_xsl) {
//                stylesheet = loadCompiledXSL(inCompiledObj);
//                out.println(inCompiledObj.getAbsolutePath() + " loaded. (NOT re-compiled)" + BR);
//            } else {
                stylesheet = tFactory.newTemplates(new StreamSource(inTextObj));
                saveCompiledXSL(inCompiledObj, stylesheet);
                out.println(textFileFullPath + " compiled." + BR);
//            }
            putHashValue(textFileFullPath, stylesheet);
        } catch (TransformerConfigurationException e) {
        	saved = false;
            out.println("error in doing compilation of " + textFileFullPath + ":" + e.getMessage() + BR);
        } catch (IOException e) {
        	saved = false;
            out.println("error in loading/saving compiled object of " + textFileFullPath + ":" + e.getMessage() + BR);
        }
        return saved;
    }
    
    private static Templates loadCompiledXSL(File inAbsoluteXSLFileName)
        throws IOException {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(inAbsoluteXSLFileName));
            Templates result = (Templates)in.readObject();
            in.close();
            return result;
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }
    
    private static void saveCompiledXSL(File outFile, Templates inXslObj)
        throws IOException {
        outFile.getParentFile().mkdirs();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));
        out.writeObject(inXslObj);
        out.flush();
        out.close();
    }
    
    /**
    Process xsl transformation
    @param xml string containing the xml
    @param absoluteXSLFile full path of the xsl file
    */
    public static void procAbsoluteXSLFile(String xml, String absoluteXSLFile, Writer resultWriter, String outputEnc, boolean debug, boolean comp_xsl, String xsl_log_file, boolean xsl_log_enabled)
            throws cwException
    {
        String cleanXML = cwUtils.escNull(xml);
        
        // To get the real path, original absoluteXSLFile may contains double slash e.g. "...\dir1\\dir2\..."
        File realXSLFile = new File(absoluteXSLFile);
        absoluteXSLFile = realXSLFile.getAbsolutePath();
        
        //Write the requesting xsl to a file for making pre-compiling xsl list
        if(xsl_log_enabled && xsl_log_file != null && xsl_log_file.length()>0) {
            PrintWriter xslLogWriter=null;
            try {
                File xslFile = new File(xsl_log_file);
                File xslDir = xslFile.getParentFile();
                if(!xslDir.exists()) {
                    xslDir.mkdir();
                }
                xslLogWriter = new PrintWriter(new BufferedWriter(new FileWriter(xsl_log_file, true)));
                xslLogWriter.println(absoluteXSLFile);
                xslLogWriter.close();
            } catch(IOException ioe) {
            }
            finally {
                if(xslLogWriter != null) {
                    xslLogWriter.close();
                }
            }
        }
        
        cwXSL.process(cleanXML, absoluteXSLFile, resultWriter, outputEnc, debug, comp_xsl);
    }

    /**
    Process xsl transformation
    @param absoluteXMLFile full path of the xml file
    @param absoluteXSLFile full path of the xsl file
    */
    public static void processFiles(String absoluteXMLFile, String absoluteXSLFile, Writer resultWriter, String outputEnc, boolean debug, boolean comp_xsl, String xsl_log_file, boolean xsl_log_enabled)
            throws cwException
    {
        if(absoluteXMLFile==null || absoluteXMLFile.length()==0)
            throw new cwException("Invalid xml file");

        File fXsl = new File(absoluteXMLFile);
        if(!fXsl.exists()) {
            String error = "XML file not found: "; 
            if (debug)
                error += fXsl.getName(); 

            throw new cwException(error);
        }

        String xmlStream = getFileContent(absoluteXMLFile);
        
        cwXSL.procAbsoluteXSLFile(xmlStream, absoluteXSLFile, resultWriter, outputEnc, debug, comp_xsl, xsl_log_file, xsl_log_enabled);

    }

    
    /**
    Process xsl transformation (core)
    */
    private static void process(String xmlStream, String absoluteXSLFile, Writer resultWriter, String outputEnc, boolean debug, boolean comp_xsl)
            throws cwException
    {
        try {
            Templates tplate = (Templates)getHashValue(absoluteXSLFile);
            Transformer tformer = null;
            
            if (tplate == null) {
                if (absoluteXSLFile == null || absoluteXSLFile.length() == 0) {
                    throw new cwException("Invalid stylesheet");
                }
                
                File fXsl = new File(absoluteXSLFile);
                if (!fXsl.exists()) {
                    String error = "Style sheet not found: "; 
                    if (debug) {
                        error += fXsl.getName(); 
                    }
                    throw new cwException(error);
                }else if(!fXsl.getCanonicalPath().toLowerCase().endsWith("xsl")){
                	String error = "Style sheet not found: "; 
                	throw new cwException(error);
                }
                if (comp_xsl) {
                    tplate = loadCompiledXSL(fXsl);
                    tformer = tplate.newTransformer();
                } else {
                    TransformerFactory tFactory = TransformerFactory.newInstance();
                    tformer = tFactory.newTransformer(new StreamSource(absoluteXSLFile));
                }
            } else {
                tformer = tplate.newTransformer();
            }
            
            // do not apply the servlet output writer directly to the transformer
            // since in weblogic 6.0, if the servlet is disconnected from the browser
            // the transformation will go crazy and be non-stop working
            // (2004-04-07 kawai)
            StringReader xmlR = new StringReader(xmlStream);
            StringWriter outW = new StringWriter();
            tformer.transform(new StreamSource(xmlR), new StreamResult(outW));
            xmlR.close();
            outW.close();
            resultWriter.write(outW.toString());
            resultWriter.flush();
        } catch (TransformerConfigurationException e) {
            throw new cwException(e.getMessage());
        } catch (TransformerException e) {
            throw new cwException(e.getMessage());
        } catch (IOException e) {
            throw new cwException(e.getMessage());
        }
    }
    
    private static Object getHashValue(String key) 
    {
        return tbStylesheetRoot.get(key);
    }
    
    private synchronized static void putHashValue(String key, Object value) 
    {   
        tbStylesheetRoot.put(key , value);
        return;
    }
    
    /**
    Read a xml file to a string
    @param filename full path of the xml file
    @return string containing the xml
    */
    public static String getFileContent(String filename) {
            String tempLine = "";
            
            StringBuffer strBufSrcFile = new StringBuffer();
 
            try {
                BufferedReader brIn = new BufferedReader(new InputStreamReader(new FileInputStream(filename), cwUtils.ENC_UTF));
                
                while((tempLine = brIn.readLine()) != null) {
                    strBufSrcFile.append(tempLine);
                    strBufSrcFile.append(cwUtils.NEWL);
                }
                
                brIn.close();                
            } catch(Exception e) {
                //System.out.println(e.toString());
            }
            return strBufSrcFile.toString();
    }
    
    public static String processFromString(String inXmlContent, String inXslContent)
        throws cwException {
        StringReader xmlR = new StringReader(inXmlContent);
        StringReader xslR = new StringReader(inXslContent);
        StringWriter outW = new StringWriter();
        
        Transformer tformer = null;
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tformer = tFactory.newTransformer(new StreamSource(xslR));
        } catch (TransformerConfigurationException e) {
            throw new cwException("error in preparing transformation:" + e.getMessage());
        }
        try {
            tformer.transform(new StreamSource(xmlR), new StreamResult(outW));
        } catch (TransformerException e) {
            throw new cwException("error in doing transformation:" + e.getMessage());
        }
        try {
            xmlR.close();
            xslR.close();
            outW.close();
        } catch (IOException e) {
            throw new cwException("error in transformation I/O:" + e.getMessage());
        }
        
        return outW.toString();
    }
    
    public static String processFromFile(String inXmlContent, String inXslFileName)
        throws cwException {
    	return processFromFileByParam(inXmlContent, inXslFileName, null, null);
    }
    
    public static String processFromFileByParam(String inXmlContent, String inXslFileName, List paramname, List paramvalue)
    throws cwException {
	    StringReader xmlR = new StringReader(inXmlContent);
	    StringWriter outW = new StringWriter();
	    
	    Transformer tformer = null;
	    try {
	        Templates tplate = (Templates)getHashValue(inXslFileName);
	        if (tplate == null) {
	            TransformerFactory tFactory = TransformerFactory.newInstance();
	            tformer = tFactory.newTransformer(new StreamSource(inXslFileName));
	        } else {
	            tformer = tplate.newTransformer();
	        }
	    } catch (TransformerConfigurationException e) {
	        throw new cwException("error in preparing transformation:" + e.getMessage());
	    }
	    try {
	    	if (paramname != null && paramvalue != null) {
	    		for (int i = 0; i < paramname.size(); i++) {
	    			tformer.setParameter((String) paramname.get(i), (String) paramvalue.get(i));
	    		}
	    	}
	        tformer.transform(new StreamSource(xmlR), new StreamResult(outW));
	    } catch (TransformerException e) {
	        throw new cwException("error in doing transformation:" + e.getMessage());
	    }
	    try {
	        xmlR.close();
	        outW.close();
	    } catch (IOException e) {
	        throw new cwException("error in transformation I/O:" + e.getMessage());
	    }
	    
	    return outW.toString();
	}
    
    public static String processFromStringByParam(String inXmlContent, String inXslContent, String parameter, String paramValue)
        throws cwException {
        StringReader xmlR = new StringReader(inXmlContent);
        StringReader xslR = new StringReader(inXslContent);
        StringWriter outW = new StringWriter();
        
        Transformer tformer = null;
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tformer = tFactory.newTransformer(new StreamSource(xslR));
        } catch (TransformerConfigurationException e) {
            throw new cwException("error in preparing transformation:" + e.getMessage());
        }
        try {
            tformer.setParameter(parameter,paramValue);
            tformer.transform(new StreamSource(xmlR), new StreamResult(outW));
        } catch (TransformerException e) {
            throw new cwException("error in doing transformation:" + e.getMessage());
        }
        try {
            xmlR.close();
            xslR.close();
            outW.close();
        } catch (IOException e) {
            throw new cwException("error in transformation I/O:" + e.getMessage());
        }
        
        return outW.toString();
    }
    
    public static void initGoldenManHtml(qdbEnv env) throws cwException {
    	String gldmanFileNamePrefix = "goldenman";
    	String gldmanFileNameSubfix = ".xsl";
        String goldenManXslDir = env.DOC_ROOT + dbUtils.SLASH + env.INI_XSL_HOME; 
        File labelDirObj = new File(goldenManXslDir);
        File[] labelFileList = labelDirObj.listFiles();
        for (int i = 0; labelFileList != null && i < labelFileList.length; i++) {
            if (labelFileList[i].isHidden() 
            		|| !labelFileList[i].isFile()
	                || !labelFileList[i].getName().startsWith(gldmanFileNamePrefix)
	                || !labelFileList[i].getName().endsWith(gldmanFileNameSubfix)
	                || labelFileList[i].getName().equals(gldmanFileNamePrefix + gldmanFileNameSubfix)) {
	            continue;
	        }
            String fileName = labelFileList[i].getName();
    		for(int j=0; j<LangLabel.allLangEncoding.size(); j++) {
    			StringWriter resultWriter = new StringWriter();
    			String encode = (String) LangLabel.allLangEncoding.get(j);
    			String goldenManTransferedXml = goldenManTransferedXml1 + encode + goldenManTransferedXml2;
    			cwXSL.procAbsoluteXSLFile(goldenManTransferedXml, labelFileList[i].getAbsolutePath(), resultWriter, env.ENCODING, env.DEBUG, env.COMP_XSL, env.INI_XSL_LOG, env.INI_XSL_LOG_ENABLED);
    			String html = resultWriter.toString();
    			html = html.substring(38);//remove the prefix string:<?xml version="1.0" encoding="UTF-8"?>
    			String key = genGoldenManHtmlKey(encode, fileName.substring(0, fileName.lastIndexOf(".")));
    			goldenManHtml.put(key, html);
    		}
        }
    }

    /**
     * @param encode: must be the encode string like Big5\GB2312\ISO-8859-1.
     */
    private static String genGoldenManHtmlKey(String encode, String name) {
    	return encode + "_" + name;
    }
    
    public static String getGoldenManHtml(String encode, String goldenman) {
		return getGoldenManHtml(encode,goldenman,0);
	}
    
	public static String getGoldenManHtml(String encode, String goldenman,long parent_id) {
		String html = (String) goldenManHtml.get(genGoldenManHtmlKey(encode, goldenman));
		if(parent_id!=0){
			html=html.replace("parent_tcr_id",String.valueOf(parent_id));
		}
		return html;
	}
	
    public static void getGoldenManHtml(String encode, String goldenmanParam, HashMap map) { 	
    	Vector gldmanName =cwUtils.splitToVecString(goldenmanParam, ","); 
    	int len = gldmanName.size();
    	Vector gmVc =new Vector();
    	if(len > 0) {
    		for(int i=0; i<len; i++) {
    			GoldenManBean gm= new GoldenManBean();
    			String name = (String)gldmanName.elementAt(i);
    			String html = (String) goldenManHtml.get(genGoldenManHtmlKey(encode, name));
    			gm.setName(name);
    			gm.setValue(html);
    			gmVc.add(gm);
    		}
    	}
    	map.put(GOLDENMAN, gmVc);  	
    }
    
   
    public static String getGoldenMan(String encode, String goldenmanParam) { 	
    	Vector gldmanName =cwUtils.splitToVecString(goldenmanParam, ","); 
    	int len = gldmanName.size();
    	if(len > 0) {
    		for(int i=0; i<len; i++) {
    			String name = (String)gldmanName.elementAt(i);
    			if(name.equalsIgnoreCase(GOLDENMAN_REG_GRADE))
    				return name;
    			}
    	}
    	return "";
    	
    	
    	}
   
    public static void outPutGoldManOption(HashMap map, Vector gmoVc){
    	if(gmoVc!=null && !gmoVc.isEmpty())
    		map.put(GOLDENMANOPTIONS, gmoVc);
    }
  
    
}
