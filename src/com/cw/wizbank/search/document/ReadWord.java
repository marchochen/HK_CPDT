// Description:	This is a quick Bridge2Java Microsoft Word demo.  This demo brings up Word, writes "This is a test", 
//              bolds the second word, waits one second, then quits.
//
// Setup:  None
//
// Author:  Bill Phillips
//

package com.cw.wizbank.search.document;

import java.io.*;

import com.cwn.wizbank.utils.CommonLog;

import Word.*;

public class ReadWord
{
    static int stringBufferSize = 32768;

    public String getContent(String file_path) {
        StringBuffer strBufSrcFile = new StringBuffer(stringBufferSize);
        file_path = (new File(file_path)).getAbsolutePath();

        Application app;
        Documents docs;
        Document doc;
        Range range;
        Words words;
        try
        {
            // Initialize the Java2Com Environment
            com.ibm.bridge2java.OleEnvironment.Initialize();

            // Create a new application
            app = new Application();

            // Make the application visible
            app.set_Visible(false);

            // Get all documents
            docs = app.get_Documents();

            // Add a blank document
            doc = docs.Add();

	        range = doc.get_Content();
	        range.InsertFile(file_path);
            for (int i=0; i<doc.get_Paragraphs().get_Count(); i++) {
                range = doc.get_Paragraphs().Item(i+1).get_Range();
                strBufSrcFile.append(range.get_Text());
            }

            // Close the workbook without saving
            doc.Close(new Boolean("false"));
  
            // Quit the application
            app.Quit();
            
            String result = strBufSrcFile.toString();
            return result;
            
        } catch (com.ibm.bridge2java.ComException e)
        {
        	CommonLog.error( "COM Exception:"+Long.toHexString((e.getHResult())));
        	CommonLog.error(e.getMessage(),e );
        } catch (Exception e)
        {
        	CommonLog.error(e.getMessage(),e );
        } finally
        {
            app = null;
            com.ibm.bridge2java.OleEnvironment.UnInitialize();
            
            return strBufSrcFile.toString();
        }        
    }
    
    public static void main(java.lang.String[] args) {
        ReadWord myReadWord = new ReadWord();
        String result = "";
        result = myReadWord.getContent(args[0]);
        //java.lang.System.out.println("result 2:" + result);
    }
}
