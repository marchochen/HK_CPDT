package com.cw.wizbank.message;

import java.io.*;
import java.util.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbUtils;
import com.oroinc.text.perl.*;

public class xmlObj {

    private static final String ALL       =   "*";
    
    public NodeList elements = null;
    private NodeList matchedElements = null;
    private boolean flag;
    private Document document = null;
    //constructor, convert xml into elements
    public xmlObj(String xml) 
        throws IOException, cwException {

        DOMParser xmlParser = new DOMParser();
        try{
            xmlParser.parse(new InputSource(new StringReader(xml)));
        }catch( SAXException e ) {
            throw new cwException ("SAXException : " + e);
        }
        document = xmlParser.getDocument();
        elements = document.getElementsByTagName(ALL);
        flag = false;
        return;
    }
    
    /**
    * match the tag in the xml
    * @param tag name to match
    * @return boolean : true if tag found , false otherwise
    */
    public boolean matchTag(String tagName) {
        if(tagName.equalsIgnoreCase("\\")) {
            flag = false;
            matchedElements = null;
        }
        else {
            flag = true;
            Element element = null;
            for (int i = 0; i < elements.getLength(); i++) {
                element = (Element)elements.item(i);
                if( (element.getTagName()).equals(tagName) ) {
                    matchedElements = element.getChildNodes();
                    return true;
                }
            }
        }
        return false;
    }
    
    /*
        <tagName attName="xxx"/>
        return xxx
    */
    public String getAttributeValue(String tagName, String attName) {

        String value = null;
        boolean tagFound = false;
        Element element = null;
        NodeList elements = null;
        if(flag)
            elements = this.matchedElements;
        else
            elements = this.elements;
        if( elements == null ) {
        	return null;
        }
        for (int i = 0; i < elements.getLength(); i++) {
            element = (Element)elements.item(i);

            if( (element.getTagName()).equals(tagName) ) {
                tagFound = true;
                break;
            }
        }
        if( tagFound ) {
            value = element.getAttribute(attName);
            return dbUtils.unEscXML(value);
        } else 
            return null;
    
    }
    
    /*
    <matchedTag>
        <tagName attName="xxx"/>
        <tagName attName="yyy"/>
        <tagName attName="zzz"/>
    </matchedTag>        
        return xxx
    */
    public String[] getAttributeValues(String tagName, String attName) {

        Vector vec = new Vector();
        Element element = null;
        NodeList elements = null;

        if(flag)
            elements = this.matchedElements;
        else
            elements = this.elements;
        if( elements == null ) {
        	return new String[0];
        }
        for (int i = 0; i < elements.getLength(); i++) {
            if( (elements.item(i)).getNodeType() == Node.TEXT_NODE  )
                continue;
            element = (Element)elements.item(i);
            if( (element.getTagName()).equals(tagName) )
                vec.addElement(element.getAttribute(attName));

        }

        String[] values = new String[vec.size()];
        for(int i=0; i<values.length; i++)
            values[i] = dbUtils.unEscXML((String)vec.elementAt(i));
        return values;
    }
    
    
    
    /*
        <tagName>
            <attName>xxx</attName>
            <attName>yyy</attName>
            ...
            ...
            <attName>zzz</attName>
        </tagName>
        return {xxx, yyy, ..., ..., zzz}
    */
    public String[] getChildNodeValues(String tagName, String attName) {
        
        Vector vec = new Vector();
        Element element = null;
        Element subElement = null;
        NodeList elements = null;
        
        if(flag)
            elements = this.matchedElements;
        else
            elements = this.elements;
        if( elements == null ) {
        	return new String[0];
        }
        for (int i = 0; i < elements.getLength(); i++) {
            element = (Element)elements.item(i);
            if( (element.getTagName()).equals(tagName) ) {
                NodeList nodelist = element.getChildNodes();
                for(int j=0; j<nodelist.getLength(); j++)
                    if( (nodelist.item(j)).getNodeType() == Node.ELEMENT_NODE ) {
                        subElement = (Element)nodelist.item(j);
                        vec.addElement((subElement.getFirstChild()).getNodeValue());
                    }
            }
        }

        String[] str = new String[vec.size()];
        for(int i=0; i<str.length; i++) {            
            str[i] = dbUtils.unEscXML((String)vec.elementAt(i));
        }
        return str;
    }
    
    
    /*
        <tagName>xxx</tagName>
        return xxx
    */
    public String getNodeValue(String tagName) {
        
        String value = null;
        Node node = null;
        NodeList elements = null;
        if(flag)
            elements = this.matchedElements;
        else
            elements = this.elements;
        if( elements == null ) {
        	return null;
        }
        for (int i = 0; i < elements.getLength(); i++) {
            node = elements.item(i);
            if( (node.getNodeName()).equals(tagName) )
                break;
        }
        
        if( node.hasChildNodes() )
            value = (node.getFirstChild()).getNodeValue();

        return dbUtils.unEscXML(value);
    }

    public static String addMsgIdAttr(String xml, Hashtable recTable, Hashtable ccTable)
        throws cwException {
            return addMsgIdAttr(xml, recTable, ccTable, null);
        }
    
    
    public static String addMsgIdAttr(String xml, Hashtable recTable, Hashtable ccTable, Hashtable bccTable)
        throws cwException {

            DOMParser xmlParser = new DOMParser();

            try{
                xmlParser.parse(new InputSource(new StringReader(xml)));
            }catch( SAXException e ) {
                throw new cwException ("SAXException : " + e);
            }catch( IOException e ) {
                throw new cwException("IOException : " + e);
            }

            Document doc = xmlParser.getDocument();
            Node firstNode = doc.getFirstChild();
            NodeList nodelist = firstNode.getChildNodes();
            Element element;
            for(int i=0; i<nodelist.getLength(); i++) {
                if( (nodelist.item(i)).getNodeType() != Node.ELEMENT_NODE )
                    continue;
                element = (Element)nodelist.item(i);
                if( (element.getTagName()).equalsIgnoreCase("recipient") ) {
                    NodeList recNodeList = element.getChildNodes();
                    Element recElement;
                    String entId;
                    Long recId;

                    for(int j=0; j<recNodeList.getLength(); j++) {
                        if( (recNodeList.item(j)).getNodeType() != Node.ELEMENT_NODE )
                            continue;
                        
                        recElement = (Element)recNodeList.item(j);
                        entId = (String)recElement.getAttribute("ent_id");

                        if( (recId = (Long)recTable.get(entId)) != null )
                            recElement.setAttribute("rec_id", recId.toString());
                    }
                } else if( ccTable != null && (element.getTagName()).equalsIgnoreCase("carboncopy") ) {
                    NodeList ccNodeList = element.getChildNodes();
                    Element ccElement;
                    String entId;
                    Long recId;
                    for(int j=0; j<ccNodeList.getLength(); j++) {
                        if( (ccNodeList.item(j)).getNodeType() != Node.ELEMENT_NODE )
                            continue;

                        ccElement = (Element)ccNodeList.item(j);
                        entId = (String)ccElement.getAttribute("ent_id");
                        if( (recId = (Long)ccTable.get(entId)) != null )
                            ccElement.setAttribute("rec_id", recId.toString());
                    }
                } else if( bccTable != null && (element.getTagName()).equalsIgnoreCase("blindcarboncopy") ) {
                    NodeList bccNodeList = element.getChildNodes();
                    Element bccElement;
                    String entId;
                    Long recId;
                    for(int j=0; j<bccNodeList.getLength(); j++) {
                        if( (bccNodeList.item(j)).getNodeType() != Node.ELEMENT_NODE )
                            continue;

                        bccElement = (Element)bccNodeList.item(j);
                        entId = (String)bccElement.getAttribute("ent_id");
                        if( (recId = (Long)bccTable.get(entId)) != null )
                            bccElement.setAttribute("rec_id", recId.toString());
                    }
                }

            }
            StringBuffer buf = new StringBuffer();
            print(doc, buf);
            return buf.toString();
        }
    
    
    
    
    

    
    
    public static Hashtable processReturnEtray(String xml)
        throws cwException {

            Hashtable table = new Hashtable();
            
            DOMParser xmlParser = new DOMParser();

            try{
                xmlParser.parse(new InputSource(new StringReader(xml)));
            }catch( SAXException e ) {
                throw new cwException ("SAXException : " + e);
            }catch( IOException e ) {
                throw new cwException("IOException : " + e);
            }

            Document doc = xmlParser.getDocument();
            Node firstNode = doc.getFirstChild();

            while( !(firstNode.getNodeName()).equalsIgnoreCase("result") ) {
                firstNode = firstNode.getNextSibling();
            }
            
            if( (((Element)firstNode).getAttribute("status")).equalsIgnoreCase("Success") )
                return table;                
            else if((((Element)firstNode).getAttribute("status")).equalsIgnoreCase("Failure")) {
                NodeList nodelist = firstNode.getChildNodes();
                for(int i=0; i<nodelist.getLength(); i++) {
                    if( ((nodelist.item(i)).getNodeName()).equalsIgnoreCase("ErrorRecords") ) {
                        NodeList errorNodelist = (nodelist.item(i)).getChildNodes();
                        Node node;
                        for(int j=0; j<errorNodelist.getLength(); j++) {
                            node = errorNodelist.item(j);
                            if( (node.getNodeName()).equalsIgnoreCase("MessageID") ){
                                if( node.hasChildNodes() ) {
                                    String value = (node.getFirstChild()).getNodeValue();
                                    table.put(new Long(value), "N");
                                }
                            }
                        }
                    }                
                }
            }
            
            
            return table;
        }
    
    
    
    
    
    
    
    /** Prints the specified node, recursively. */
    public static void print(Node node, StringBuffer buf) {

        // is there anything to do?
        if ( node == null ) {
            return;
        }

        int type = node.getNodeType();
        switch ( type ) {
        // print document
        case Node.DOCUMENT_NODE: {
                NodeList children = node.getChildNodes();
                for ( int iChild = 0; iChild < children.getLength(); iChild++ ) {
                    print(children.item(iChild), buf);
                }
                break;
            }

            // print element with attributes
        case Node.ELEMENT_NODE: {
                buf.append('<');
                buf.append(node.getNodeName());
                Attr attrs[] = sortAttributes(node.getAttributes());
                for ( int i = 0; i < attrs.length; i++ ) {
                    Attr attr = attrs[i];
                    buf.append(' ');
                    buf.append(attr.getNodeName());
                    buf.append("=\"");
                    buf.append(unEscNL(cwUtils.esc4XML(attr.getNodeValue())));                    
                    buf.append('"');
                }
                buf.append('>');
                NodeList children = node.getChildNodes();
                if ( children != null ) {
                    int len = children.getLength();
                    for ( int i = 0; i < len; i++ ) {
                        print(children.item(i), buf);
                    }
                }
                break;
            }

        
        // handle entity reference nodes
        case Node.ENTITY_REFERENCE_NODE: {
                /*
                if ( canonical ) {
                    NodeList children = node.getChildNodes();
                    if ( children != null ) {
                        int len = children.getLength();
                        for ( int i = 0; i < len; i++ ) {
                            print(children.item(i));
                        }
                    }
                } else {*/
                    buf.append('&');
                    buf.append(node.getNodeName());
                    buf.append(';');
                //}
                break;
            }

        // print cdata sections
        case Node.CDATA_SECTION_NODE: {
                /*
                if ( canonical ) {
                    out.print(normalize(node.getNodeValue()));
                } else {*/
                    buf.append("<![CDATA[");
                    buf.append(cwUtils.esc4XML(node.getNodeValue()));
                    buf.append("]]>");
                //}
                break;
            }
        
        
            // print text
        case Node.TEXT_NODE: {
                buf.append(cwUtils.esc4XML(node.getNodeValue()));                
                break;
            }

            // print processing instruction
        case Node.PROCESSING_INSTRUCTION_NODE: {
                buf.append("<?");
                buf.append(node.getNodeName());
                String data = node.getNodeValue();
                if ( data != null && data.length() > 0 ) {
                    buf.append(' ');
                    buf.append(data);
                }
                buf.append("?>").append('\n');
                break;
            }
        }

        if ( type == Node.ELEMENT_NODE ) {
            buf.append("</");
            buf.append(node.getNodeName());
            buf.append('>');
        }

    }




    /** Returns a sorted list of attributes. */
    public static Attr[] sortAttributes(NamedNodeMap attrs) {

        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        for ( int i = 0; i < len; i++ ) {
            array[i] = (Attr)attrs.item(i);
        }
        for ( int i = 0; i < len - 1; i++ ) {
            String name  = array[i].getNodeName();
            int    index = i;
            for ( int j = i + 1; j < len; j++ ) {
                String curName = array[j].getNodeName();
                if ( curName.compareTo(name) < 0 ) {
                    name  = curName;
                    index = j;
                }
            }
            if ( index != i ) {
                Attr temp    = array[i];
                array[i]     = array[index];
                array[index] = temp;
            }
        }

        return(array);

    }

    
    
    
    
    public static String checkEtray(String eTrayMessage)
        throws cwException, IOException {
            
            xmlObj objXml = new xmlObj(eTrayMessage);
            objXml.matchTag("eTrays");
            String[] messageID = objXml.getAttributeValues("eTray", "MessageID");
            StringBuffer etray = new StringBuffer();
            
            if( messageID.length > 0 ) {
                
                if( (Long.parseLong(messageID[0]) % 2) == 1 ) {
                     etray.append("<result status=\"Success\"><AddCount>")
                          .append(messageID.length)
                          .append("</AddCount></result>");

                } else {
                    int count = 0;
                    for(int i=0; i< messageID.length; i++)
                        if( (Long.parseLong(messageID[i]) % 2) == 0 ) 
                            count++;

                    etray.append("<result status=\"Failure\">")
                         .append("<reason>No XML Message</reason>")
                         .append("<AddCount>").append( (messageID.length - count) ).append("</AddCount>")
                         .append("<ErrorRecords>");

                    for(int i=0; i<messageID.length; i++)
                        if( (Long.parseLong(messageID[i]) % 2) == 0 )
                            etray.append("<MessageID>").append(messageID[i]).append("</MessageID>");

                    etray.append("</ErrorRecords>")
                         .append("</result>");
                    
                }
                
            }
            
            return etray.toString();
        }
    

    public static String unEscNL(String in){
        if(in==null || in.length()==0)
            return in;
            
        String result="";
        Perl5Util perl = new Perl5Util();
        if(perl.match("#\n#i", in)){
            in = perl.substitute("s#\n#&\\#10;#ig", in);
        }
        return in;
    }    
}