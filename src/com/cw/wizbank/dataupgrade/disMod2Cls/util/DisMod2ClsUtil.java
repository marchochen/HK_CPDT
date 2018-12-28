/*
 * Created on 2005-10-10
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.dataupgrade.disMod2Cls.util;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwXSL;
import com.cwn.wizbank.utils.CommonLog;



/**
 * @author randy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DisMod2ClsUtil {
	public static final String ENC_UTF = "UTF-8";

   

   
   public static void writeLog(String logFile, String content,Exception ine){
	  try{   
		  PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), ENC_UTF));
		  if(content!=null){
			out.write(content + System.getProperty("line.separator"));
			 out.flush();
		  }
		  ine.printStackTrace(out);
		  out.close();
	  }catch (IOException e){
		  System.err.println("write file exception:" + e.getMessage());
	  }
  }
  
   public static void writeLog(String logFile, String content){
         try{
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), ENC_UTF));
             out.write(content.toCharArray());
             out.newLine();
             out.flush();
             out.close();
         }catch (IOException e){
             CommonLog.error(e.getMessage(),e);
             System.err.println("write file exception:" + e.getMessage());
         }
     }
	 
	public static String replaceString(String sourse,String to,String from)throws IOException{
		int index=sourse.indexOf(from);
		if(index>=0){
			String temp=sourse.substring(0,index)+to+sourse.substring(index+from.length(),sourse.length());
			sourse=temp;
		}
		return sourse;
	}
    
    
    public static String transformXML(String inXML,String xsl_file) throws cwException,IOException {
     
            StringWriter outXML = new StringWriter(2048);
            cwXSL.procAbsoluteXSLFile(inXML, xsl_file, outXML, "ISO-8859-1",true, false, null,false);
            outXML.close();
            return outXML.toString();
       
   }
    
	
}
