package com.cw.wizbank.wizcase;

import java.io.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import com.cw.wizbank.util.*;

public class xmlUtils {

    public xmlUtils() { ; }

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
                    buf.append(cwUtils.esc4XML(attr.getNodeValue()));                    
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





    /**
    Convert the invalid chatacter in the XML
    @param String
    @return converted string
    */
    public static String normalize(String s) {
        StringBuffer str = new StringBuffer();

        int len = (s != null) ? s.length() : 0;
        for ( int i = 0; i < len; i++ ) {
            char ch = s.charAt(i);
            switch ( ch ) {
            case '<': {
                    str.append("&lt;");
                    break;
                }
            case '>': {
                    str.append("&gt;");
                    break;
                }
            case '&': {
                    str.append("&amp;");
                    break;
                }
            case '"': {
                    str.append("&quot;");
                    break;
                }

            case '\r':
            case '\n': {
                    //if ( canonical ) {
                        str.append("&#");
                        str.append(Integer.toString(ch));
                        str.append(';');
                        break;
                    //}
                }

            default: {
                    str.append(ch);
                }
            }
        }

        return(str.toString());

    }

    public static Node parseXMLString(String xml)
        throws SAXException, IOException {

            Node node;
            DOMParser xmlParser = new DOMParser();
            xmlParser.parse(new InputSource(new StringReader(xml)));
            Document doc = xmlParser.getDocument();
            doc = xmlParser.getDocument();
            return doc.getFirstChild();
            
        }



    /**
     * Parse the xml file and return a XML node
     * @param xml file
     * @return XML node
     */
    public static Node parseXMLFile(File xmlFile)
        throws IOException, FileNotFoundException, SAXException {

        if (!xmlFile.exists())
            throw new IOException(xmlFile + " file not exist");
        
        Node node;
        //DOMParserWrapper parser = (DOMParserWrapper)Class.forName(parserWrapperName).newInstance();
        DOMParser xmlParser = new DOMParser();
        xmlParser.parse(new InputSource(new BufferedReader(new FileReader(xmlFile))));
        Document doc = xmlParser.getDocument();
        doc = xmlParser.getDocument();
        
        /*
        NodeList nodeList = doc.getChildNodes();        
        for(int i=0; i<nodeList.getLength(); i++) {
            node = nodeList.item(i);
            System.out.println(node.getNodeType());
        }
        */

        node = doc.getFirstChild();
        while( node.getNodeType() == Node.COMMENT_NODE ) {
            node = node.getNextSibling();
        }
        return node;

    }
}