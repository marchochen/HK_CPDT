package com.cw.wizbank.search.document;

import com.kizna.html.HTMLParser; 
import com.kizna.html.HTMLNode; 
import com.kizna.html.HTMLStringNode; 
import java.util.Enumeration; 

public class HTMLStringFilter { 
  private static int stringBufferSize = 32768;
  private final static String EMPTY = " ";
  private HTMLParser parser; 
  public HTMLStringFilter(String someURL) { 
    buildMyParser(someURL); 
  } 
  public void buildMyParser(String someURL) { 
    parser = new HTMLParser(someURL);   // Create the parser object 
    parser.registerScanners();      // Register the standard scanners 
  } 
  public String getText() { 
    StringBuffer strBufSrcFile = new StringBuffer(stringBufferSize);
    HTMLNode node; 
    // Run through an enumeration of html elements, and pick up  
    // only those that are plain string. 
    for (Enumeration e=parser.elements();e.hasMoreElements();) { 
      node = (HTMLNode)e.nextElement();    // Cast the element to HTMLNode 

      if (node instanceof HTMLStringNode) { 
        // Node is a plain string  
        // Cast it to an HTMLStringNode 
        HTMLStringNode stringNode = (HTMLStringNode)node; 
        // Retrieve the data from the object and print it 
        // System.out.println(stringNode.getText()); 
        strBufSrcFile.append(stringNode.getText()).append(EMPTY);
      }
    }
    
    return strBufSrcFile.toString();
  } 
  public static void main(String [] args) { 
    HTMLStringFilter filter = new HTMLStringFilter(args[0]); 
    //System.out.println("Extracting plain text elements from "+args[0]+"..."); 
    //System.out.println(filter.getText());
  } 
}
