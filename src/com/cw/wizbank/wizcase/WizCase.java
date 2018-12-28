package com.cw.wizbank.wizcase;

import org.w3c.dom.*;
import org.apache.xerces.dom.DocumentImpl;
import javax.servlet.http.*;
import java.util.Vector;
import com.cw.wizbank.util.*;
import java.io.*;

public class WizCase {

    public WizCase() { ;         //$$ wizCase1.move(0,0);
}

    /**
     * Construct the XML
     * @param vector containing long array of block id
     * @param vector containing XML element
     * @param flag to indicate it checked answer or not
     * @return XML NODE
     */
    public Node constructXML(Vector blockIdVec, Vector elementVec, Vector pageBreakVec) {

        Document doc = new DocumentImpl();
        Element root = doc.createElement("answer");
        root.setAttribute("type", "user");
/*        
        if( flag )
            root.setAttribute("trial", "Y");
        else
            root.setAttribute("trial", "N");
*/            
        long[] blockId;
        Element element = null;
        Element newElement = null;
        Element parent = null;
        Node node;
        boolean blockExist = false;
        for(int i=0; i<blockIdVec.size(); i++) {
            blockId = (long[])blockIdVec.elementAt(i);
            element = (Element)root.getFirstChild();
            parent = root;
            for(int j=0; j<blockId.length; j++) {
                blockExist = false;
                while( element != null ) {
                    if( element.hasAttribute("id") ) {
                        if( (element.getAttribute("id")).equalsIgnoreCase(Long.toString(blockId[j])) ) {
                            parent = element;
                        if( j < blockId.length - 1 )
                            element = (Element)element.getFirstChild();
                            blockExist = true;
                            break;
                      }
                  }
                  if( !blockExist && element.getNextSibling() != null ) {
                      element = (Element)element.getNextSibling();
                  } else {
                      element = null;
                  }
              }

              if( !blockExist ) {
                  newElement = doc.createElement("block");
                  newElement.setAttribute("id", Long.toString(blockId[j]));
                  if( pageBreakVec.elementAt(i) != null )
                    newElement.setAttribute("page_break", (String)pageBreakVec.elementAt(i));
                  parent.appendChild(newElement);
                  parent = newElement;
                  element = null;
              }
          }
          //if( !((String)elementVec.elementAt(i)).equalsIgnoreCase(WizCaseModule.EMPTY_STRING) ) {
          if( elementVec.elementAt(i) != null ) {
            node = doc.importNode((Element)elementVec.elementAt(i), true);
            if( element != null )
              element.appendChild(node);
            else if( newElement != null )
              newElement.appendChild(node);
          }
        }
        return root;
    }



    /**
    Generate a string of xml, concate 3 nodes and append the type node as the first child of the root
    @param form node
    @param user answer node ( optional )
    @param type    
    @param toc node
    @return string of xml
    */
    public String genXml(Node formNode, Node userNode, String type, Node tocNode, String cur_dir) {

        Document doc= new DocumentImpl();
        Node root = doc.createElement("wizcase");

        Element element = doc.createElement("cur_type");
        element.appendChild(doc.createTextNode(type));
        root.appendChild(element);
        
        element = doc.createElement("cur_dir");
        element.appendChild(doc.createTextNode(cur_dir));
        root.appendChild(element);

        /*
        XML object can only append the node have the same document owner,
        so import it before append it
        */

        NodeList nodelist = formNode.getChildNodes();
        for(int i=0; i<nodelist.getLength(); i++) {
            root.appendChild(doc.importNode(nodelist.item(i), true));
        }

        if( userNode != null ) {            
            root.appendChild( doc.importNode(userNode, true) );
        }

        if( tocNode != null ) {
            root.appendChild( doc.importNode(tocNode, true) );   
        }

        StringBuffer buf = new StringBuffer();
        xmlUtils.print(root, buf);
        return buf.toString();
        

    }


    /**
    Combine a pair of form and user answer to form a large xml
    @param form nodes
    @param user answer ndoes
    @param toc node
    @return string of combined xml
    */
    
    public String combineXml(Node[] formsNode, Node[] usersNode, Node tocNode) {
        
        Document doc= new DocumentImpl();
        Node root = doc.createElement("wizcase");
        Element element;
        NodeList nodelist;
        
        for(int i=0; i<formsNode.length; i++) {
            element = doc.createElement("document");
            element.setAttribute("index", Integer.toString(i+1));
            root.appendChild(element);
            
            nodelist = formsNode[i].getChildNodes();
            for(int j=0; j<nodelist.getLength(); j++) {
                if( ((nodelist.item(j)).getNodeName()).equalsIgnoreCase("answer") )
                    continue;
                element.appendChild( doc.importNode(nodelist.item(j), true) );
            }

            if( usersNode[i] != null ) {                
                element.appendChild( doc.importNode(usersNode[i], true) );
            }

        }
        
        if( tocNode != null )
            root.appendChild( doc.importNode(tocNode, true) );
        
        StringBuffer buf = new StringBuffer();
        xmlUtils.print(root, buf);
        return buf.toString();
        
    }




    /**
    Construct TOC XML with specified sequence
    @param String array of filename 
    @param sess to get the page of filename
    @return Node
    */
    public Node constructToc(String[] filename, HttpSession sess) {
        
        String DELIMITER = "[|]";
        Document doc= new DocumentImpl();
        Node root = doc.createElement("toc");
        Element element, subElement;
        String page;
        String[] subForm;
        
        for(int i=0; i<filename.length; i++) {
            
            subForm = cwUtils.splitToString(filename[i], DELIMITER);
            
            element = doc.createElement("form");
            element.setAttribute("name", subForm[0]);
            element.setAttribute("order", Integer.toString(i+1));
            //if( (page = (String)sess.getAttribute("SESS_WIZCASE_FORM_PAGE" + filename)) != null ) 
            //    element.setAttribute("page", page);
            
            for(int j=1; j<subForm.length; j++) {
                subElement = doc.createElement("form");
                subElement.setAttribute("name", subForm[j]);
                subElement.setAttribute("order", Integer.toString(j));
                element.appendChild(subElement);
            //    if( (page = (String)updateToc.getValue("SESS_WIZCASE_FORM_PAGE" + filename)) != null ) 
            //        subElement.setAttribute("page", page);            
            }
            
            root.appendChild(element);
        }
        
        return root;
    }


    /**
    Update page number of the form in the toc xml
    @param Node of toc
    //@param sess to get the page number of the form
    @param name of the form to be updated
    @param page number of the selected form
    @return updated Node
    */
    public boolean updateToc(Node toc, String formName, String[] attrName, String[] attrValue) {
        
        NodeList nodelist = toc.getChildNodes();        
        Element element;
        for(int i=0; i<nodelist.getLength(); i++) {
            element = (Element)nodelist.item(i);
            if( (element.getAttribute("name")).equalsIgnoreCase(formName) ) {
                for(int k=0; k<attrName.length; k++)
                    if( attrValue[k] != null )
                        element.setAttribute(attrName[k], attrValue[k]);
                return true;
            } else {
                if( updateToc(element, formName, attrName, attrValue) )
                    return true;
            }
        }
        
        return false;
    }



    /**
    Delete files which last modification date before the timestamp specified in the directory
    @param file path
    @param timestamp to determine which file to be deleted
    @param long milliseconds before the timestamp of file to be deleted
    */
    public int delFileBefore(File filePath, long timestamp, long period)
        throws IOException {

            int count = 0;
            File[] files = filePath.listFiles();
            for(int i=0; i<files.length; i++) {
                if( files[i].lastModified() < (timestamp - period) ) {
                    if(files[i].delete())
                        count++;
                }
            }
            
            return count;
        }

    //{{DECLARE_CONTROLS
    //}}
    
	public static String getFilename(String str, String type){	    	    
	    return str.substring(7, str.length()) + "." + type;	    	
	}    
}