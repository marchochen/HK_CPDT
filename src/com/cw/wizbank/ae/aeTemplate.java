package com.cw.wizbank.ae;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class aeTemplate {
    public long      tpl_id;
    public long      tpl_ttp_id;
    public String    tpl_title;
    public String    tpl_xml;
    public long      tpl_owner_ent_id;
    public boolean   tpl_approval_ind;    
    public Timestamp tpl_create_timestamp;
    public String    tpl_create_usr_id;
    public Timestamp tpl_upd_timestamp;
    public String    tpl_upd_usr_id;

    //non-database field
    //store the tpl_ttp_id's logical values (e.g. WORKFLOW, APPNFORM, ITEM)
    public String    tpl_type;
    
    public final static String WORKFLOW = "WORKFLOW";
    public final static String APPNFORM = "APPNFORM";
    public final static String PAYMENT  = "PAYMENT";
    public static final String ITEM = "ITEM";
    public static final String OPTION_SRC_TYPE_CODE_TABLE = "code_table";

    static final String GET_RAW_TEMPLATE_SQL = " select tpl_id, tpl_xml "
                                             + " from aeItemTemplate, aeItem, aeTemplateType, aeTemplate "
                                             + " where itm_id = itp_itm_id "
                                             + " and ttp_id = itp_ttp_id "
                                             + " and tpl_id = itp_tpl_id "
                                             + " and ttp_title = ? "
                                             + " and itm_type = ? ";

    static final String GET_RAW_TEMPLATE_BY_TPL_TYPE_SQL = " select tpl_id, tpl_xml "
                                             + " from aeItemTemplate, aeItem, aeTemplateType, aeTemplate "
                                             + " where itm_id = itp_itm_id "
                                             + " and ttp_id = itp_ttp_id "
                                             + " and tpl_id = itp_tpl_id "
                                             + " and ttp_title = ? ";
   
    static final String GET_RAW_TEMPLATE_FROM_ID_SQL = " Select tpl_xml "
                                                     + " From aeTemplate "
                                                     + " Where tpl_id = ? ";

    /**
    Perform a deep clone on this aeTemplate
    @return a new image of this aeTemplate
    */
    public aeTemplate deepClone() {
        aeTemplate imageTpl = new aeTemplate();
        imageTpl.tpl_id = this.tpl_id;
        imageTpl.tpl_ttp_id = this.tpl_ttp_id;
        imageTpl.tpl_title = this.tpl_title;
        imageTpl.tpl_xml = this.tpl_xml;
        imageTpl.tpl_owner_ent_id = this.tpl_owner_ent_id;
        imageTpl.tpl_approval_ind = this.tpl_approval_ind;
        imageTpl.tpl_create_timestamp = this.tpl_create_timestamp;
        imageTpl.tpl_create_usr_id = this.tpl_create_usr_id;
        imageTpl.tpl_upd_timestamp = this.tpl_upd_timestamp;
        imageTpl.tpl_upd_usr_id = this.tpl_upd_usr_id;
        imageTpl.tpl_type = this.tpl_type;
        return imageTpl;
    }
    
    public String IdTitleAsXML(String ttp_title, boolean showXML, String tplXML) {
        StringBuffer xmlBuf = new StringBuffer(500);
        xmlBuf.append("<template type=\"").append(ttp_title).append("\" tpl_id=\"").append(tpl_id).append("\" title=\"").append(tpl_title).append("\">").append(dbUtils.NEWL);
        if(showXML) {
            xmlBuf.append(tplXML).append(dbUtils.NEWL);
        }
        xmlBuf.append("</template>").append(dbUtils.NEWL);
        String xml = new String(xmlBuf);
        return xml;
    }
    
    
    public void get(Connection con)
        throws SQLException
    {
        PreparedStatement stmt;
        ResultSet rs;

        stmt = con.prepareStatement("SELECT * FROM aeTemplate WHERE tpl_id = ?");
        stmt.setLong(1, tpl_id);
        rs = stmt.executeQuery();
                
        if (rs.next()) {
            tpl_ttp_id = rs.getLong("tpl_ttp_id");
            tpl_title = rs.getString("tpl_title");
                
            //tpl_xml = rs.getString("tpl_xml");
            tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
                
            tpl_owner_ent_id = rs.getLong("tpl_owner_ent_id");
            tpl_approval_ind = rs.getBoolean("tpl_approval_ind");
            
            tpl_create_timestamp = rs.getTimestamp("tpl_create_timestamp");
            tpl_create_usr_id = rs.getString("tpl_create_usr_id");
            tpl_upd_timestamp = rs.getTimestamp("tpl_upd_timestamp");
            tpl_upd_usr_id = rs.getString("tpl_upd_usr_id");
        } else {
            //throw new qdbException("qdb.ae.aeTemplate.get: Fail to retrieve template from DB");
        }
        rs.close();        
        stmt.close();
       
    }
    
    //given an item type and template type
    //return aeTemplate.tpl_xml directly from DB 
    //return "" if no tpl_xml found
    public String getRawTemplate(Connection con, String itm_type, String ttp_title, long owner_ent_id) 
      throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_RAW_TEMPLATE_SQL);
        stmt.setString(1, ttp_title);
        stmt.setString(2, itm_type);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            tpl_id = rs.getLong("tpl_id");
            tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
        }
        else {
            //if no template found, get the 1st template
            tpl_id = getFirstTplId(con, ttp_title, owner_ent_id, "asc");
            tpl_xml = getRawTemplate(con);
        }
        stmt.close();
        return(tpl_xml);
    }

    //given the tpl_id
    //return aeTemplate.tpl_xml directly from DB 
    //return "" if no tpl_xml found
    public String getRawTemplate(Connection con) 
      throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_RAW_TEMPLATE_FROM_ID_SQL);
        stmt.setLong(1, tpl_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
        }
        else {
            tpl_xml = "";
        }
        stmt.close();
        return(tpl_xml);
    }
    
    
    public static long getFirstTplId(Connection con, String ttp_title, long owner_ent_id, String sortOrder) 
        throws SQLException {
        
        long tpl_id = 0;
        String tpl_title = null;
        StringBuffer SQLBuf = new StringBuffer(2500);
        SQLBuf.append(" Select tpl_id, tpl_title From aeTemplate, aeTemplateType ");
        SQLBuf.append(" Where ttp_id = tpl_ttp_id ");
        SQLBuf.append(" And ttp_title = ? ");
        SQLBuf.append(" And tpl_owner_ent_id = ? ");
        SQLBuf.append(" Order by tpl_id ").append(sortOrder);
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ttp_title);
        stmt.setLong(2, owner_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            tpl_title = rs.getString("tpl_title");
            tpl_id = rs.getLong("tpl_id");
        }
        else
            throw new SQLException("No " + ttp_title + " template defined in database");
        
        stmt.close();
        
        return tpl_id;
    }
    
    public static String getFirstRawTpl(Connection con, String ttp_title, long owner_ent_id, String sortOrder) 
        throws SQLException {
        
        String tpl_xml = null;
        StringBuffer SQLBuf = new StringBuffer(2500);
        SQLBuf.append(" Select tpl_xml From aeTemplate, aeTemplateType ");
        SQLBuf.append(" Where ttp_id = tpl_ttp_id ");
        SQLBuf.append(" And ttp_title = ? ");
        SQLBuf.append(" And tpl_owner_ent_id = ? ");
        SQLBuf.append(" Order by tpl_id ").append(sortOrder);
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ttp_title);
        stmt.setLong(2, owner_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
        }
        else
            throw new SQLException("No " + ttp_title + " template defined in database");
        
        stmt.close();        
        return tpl_xml;
    }
    
    
    public static String getAllTemplateAsXML(Connection con, long owner_ent_id, 
                                             long tnd_id, long tpl_id, String ttp_title, qdbEnv inEnv) 
      throws SQLException, cwException  ,cwSysMessage{
        
        aeTreeNode parentNode = new aeTreeNode();
        parentNode.tnd_id = tnd_id;
        if(parentNode.tnd_id != 0)
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);

        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<template_list type=\"").append(ttp_title).append("\"");
        xmlBuf.append(" parent_tnd_id=\"").append(aeUtils.escZero(parentNode.tnd_id)).append("\">").append(dbUtils.NEWL);

        xmlBuf.append(parentNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
        
        
        StringBuffer SQLBuf = new StringBuffer(2500);
        SQLBuf.append(" Select tpl_id, tpl_title, tpl_xml From aeTemplate, aeTemplateType ");
        SQLBuf.append(" Where ttp_id = tpl_ttp_id ");
        SQLBuf.append(" And ttp_title = ? ");
        SQLBuf.append(" And tpl_owner_ent_id = ? ");
        SQLBuf.append(" Order by tpl_title asc");
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ttp_title);
        stmt.setLong(2, owner_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        xmlBuf.append("<templates cur_tpl_id=\"").append(tpl_id).append("\">").append(dbUtils.NEWL);
        while(rs.next()) {
            aeTemplate tpl = new aeTemplate();
            tpl.tpl_id = rs.getLong("tpl_id");
            tpl.tpl_title = rs.getString("tpl_title");
            tpl.tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
            
            StringBuffer tplBuf = new StringBuffer(2000);
            tplBuf.append("<applyeasy>").append(dbUtils.NEWL);
            tplBuf.append(tpl.tpl_xml).append(dbUtils.NEWL);
            tplBuf.append("</applyeasy>");
            String tplXML = aeUtils.transformXML(tplBuf.toString(), inEnv.INI_XSL_VALTPL, inEnv, null);
            
            xmlBuf.append(tpl.IdTitleAsXML(ttp_title,(tpl.tpl_id == tpl_id),tplXML));
        }
        stmt.close();
        xmlBuf.append("</templates>").append(dbUtils.NEWL);
        xmlBuf.append("</template_list>");
        String xml = new String(xmlBuf);
        return xml;
    }    

    /**
    Get the (ParamName, FieldName) pairs into a Hashtable.
    Pre-defined variable: tpl_xml
    @return Hashtable contains (ParamName, FieldName) pairs. e.g. ("itm_title", "field02")
                                                                    ("field03", "field03") is no paramname
    */
    public Hashtable getParamNameFields() throws IOException, cwException {
        Hashtable h = new Hashtable(); //Hashtable to be returned
        DOMParser xmlParser = new DOMParser(); //get XML parser
        try {
            xmlParser.parse(new InputSource(new StringReader(this.tpl_xml)));
        } catch(SAXException e) {
            throw new cwException("SAXException at aeTemplate.getParamNameFields(): " + e.getMessage());
        }
        Document document = xmlParser.getDocument(); //get parsed XML document
        Node root = document.getFirstChild(); //get root node
        NodeList nodelist = root.getChildNodes(); //get child nodes under root ("field01", field02", ...)
        Node node; //child node
        String nodeName; //child node name
        String paramName; //child node's paramname
        //for each child node, if it has paramname
        //store the values into Hashtable
        for(int i=0; i<nodelist.getLength(); i++) {
            node = nodelist.item(i);
            nodeName = node.getNodeName();
            NamedNodeMap nodeAtt = node.getAttributes();
            if (nodeAtt != null) {
                Node att = nodeAtt.getNamedItem("paramname");
                if (att != null) {
                    paramName = att.getNodeValue();
                    if(paramName != null) {
                        h.put(paramName, nodeName);
                    }
                }else{
                    h.put(nodeName, nodeName);
                }
            }
        }
        return h;
    }
    
    /**
    Get the field that have options, ie field type = checkbox, radio or select
    Pre-defined variable: tpl_id
    @return Hashtable contains (Fieldname, Hashtable of options) pairs. Hashtable of option contains (value , id pair)
    */
    public void getOptionFields(Connection con, Hashtable htCheckbox, Hashtable htRadio, Hashtable htSelect) throws SQLException, IOException, cwException {
        DbTemplateView dbTplVi = new DbTemplateView();
        dbTplVi.tvw_tpl_id = tpl_id;
        dbTplVi.tvw_id = "DETAIL_VIEW";
        dbTplVi.get(con);
        String tvw_xml = dbTplVi.tvw_xml;
        
        Hashtable h = new Hashtable(); //Hashtable to be returned
        DOMParser xmlParser = new DOMParser(); //get XML parser
        try {
            xmlParser.parse(new InputSource(new StringReader(tvw_xml)));
        } catch(SAXException e) {
            throw new cwException("SAXException at aeTemplate.getOptionFields(): " + e.getMessage());
        }
        Document document = xmlParser.getDocument(); //get parsed XML document
        Node root = document.getFirstChild(); //get root node
        NodeList sectionlist = root.getChildNodes(); //get child nodes under roo, section 1 , section 2
        
        //for each child node, if it's fieldtype is 'CHECKBOX', 'RADIO' or 'SELECT'
        //store the values into Hashtable
        for(int i=0; i<sectionlist.getLength(); i++) {
            Node section = sectionlist.item(i);
            NodeList nodelist = section.getChildNodes(); // field01, field02                                        
            for(int j=0; j<nodelist.getLength(); j++) {
                Node node = nodelist.item(j);
                String nodeName = node.getNodeName();
                NamedNodeMap nodeAtt = node.getAttributes();
                if (nodeAtt != null) {
                    Node att = nodeAtt.getNamedItem("type");
                    if (att!=null){
                        String fieldType = att.getNodeValue();
                        if (fieldType.equalsIgnoreCase("CHECKBOX") || fieldType.equalsIgnoreCase("RADIO") || fieldType.equalsIgnoreCase("SELECT") || fieldType.equalsIgnoreCase("radio_bonus")){
                            Hashtable htOption = new Hashtable();
                            NodeList optionlist = node.getChildNodes(); // option in field01, field02
                            for(int k=0; k<optionlist.getLength(); k++) {
                                Node option = optionlist.item(k);
                                String optionName = option.getNodeName();
                                
                                if (optionName.equals(nodeName)){
                                    NamedNodeMap optionAtt = option.getAttributes();
                                    if (optionAtt != null) {
                                        Node attValue = optionAtt.getNamedItem("value");
                                        Node attID = optionAtt.getNamedItem("id");
                                        String value = null;
                                        String id = null;
                                        if (attValue!=null)
                                            value = attValue.getNodeValue();
                                        if (attID!=null)
                                            id = attID.getNodeValue();
                                        if (value!=null && id!=null)
                                            htOption.put(value, id);
                                    }
                                }
                            }
                            if (fieldType.equalsIgnoreCase("CHECKBOX")){
                                htCheckbox.put(nodeName, htOption);
                            }else if (fieldType.equalsIgnoreCase("RADIO")||fieldType.equalsIgnoreCase("radio_bonus")){
                                htRadio.put(nodeName, htOption);
                            }else if (fieldType.equalsIgnoreCase("SELECT")){
                                htSelect.put(nodeName, htOption);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
    Parse the template xml and return a Hashtable of (option_src, option_src_type)
    @return Hashtable of (option_src, option_src_type)
    */
    public Hashtable getOptionSrc() throws IOException, cwException {
        Hashtable h = new Hashtable();
        DOMParser xmlParser = new DOMParser(); //get XML parser
        try {
            xmlParser.parse(new InputSource(new StringReader(this.tpl_xml)));
        } catch(SAXException e) {
            throw new cwException("SAXException at aeTemplate.getOptionSrc(): " + e.getMessage());
        }
        Document document = xmlParser.getDocument(); //get parsed XML document
        Node root = document.getFirstChild(); //get root node
        NodeList nodelist = root.getChildNodes(); //get child nodes under root ("field01", field02", ...)
        //for each child node, if it has option_src_type and option_src
        //store the values into Hashtable
        for(int i=0; i<nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            /*nodeName = node.getNodeName();*/
            NamedNodeMap nodeAtt = node.getAttributes();
            if (nodeAtt != null) {
                Node att_option_src = nodeAtt.getNamedItem("option_src");
                Node att_option_src_type = nodeAtt.getNamedItem("option_src_type");
                if (att_option_src != null && att_option_src_type != null) {
                    String option_src = att_option_src.getNodeValue();
                    String option_src_type = att_option_src_type.getNodeValue();
                    if(option_src != null && option_src_type != null) {
                        h.put(option_src, option_src_type);
                    }
                }
            }
        }
        return h;
    }

    public static Vector getTemplateByTtpTtle(Connection con, String ttp_title) 
        throws SQLException {
        
        Vector result = new Vector();
        StringBuffer SQLBuf = new StringBuffer(2500);
        //select ttp_id, tpl_id From aeTemplateType, aeTemplate where ttp_id = tpl_ttp_id and ttp_title = 'WORKFLOW'
        SQLBuf.append("select ttp_id, tpl_id From aeTemplateType, aeTemplate where ");
        SQLBuf.append(" ttp_id = tpl_ttp_id and ttp_title = ?");
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ttp_title);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            result.addElement(new Long(rs.getLong("tpl_id")));
            result.addElement(new Long(rs.getLong("ttp_id")));
        }
        
        stmt.close();
        
        return result;
    }
 
    public static int getTplIdByItmId(Connection con, String ttp_title,long itm_id) 
        throws SQLException {
        
        Vector result = new Vector();
        StringBuffer SQLBuf = new StringBuffer(2500);
        //select ttp_id, tpl_id From aeTemplateType, aeTemplate where ttp_id = tpl_ttp_id and ttp_title = 'WORKFLOW'
        String SQL = "select itp_tpl_id From aeTemplateType, aeTemplate, aeItemTemplate where ttp_id = tpl_ttp_id " +
            "and ttp_title = ? and itp_ttp_id = ttp_id and itp_tpl_id = tpl_id and itp_itm_id = ?";
        /*
        SQLBuf.append("select ttp_id, tpl_id From aeTemplateType, aeTemplate where ");
        SQLBuf.append(" ttp_id = tpl_ttp_id and ttp_title = ?");
        String SQL = new String(SQLBuf);
        */
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ttp_title);
        stmt.setLong(2, itm_id);
        ResultSet rs = stmt.executeQuery();
        
        int tpl_id = 0;
        if(rs.next()) {
            tpl_id = rs.getInt("itp_tpl_id");
        }
        
        stmt.close();
        return tpl_id;
    }
 
    
    public static String getTplXml(Connection con, String tpl_title, long owner_ent_id) 
            throws SQLException {
            
            String tpl_xml = "";
            StringBuffer SQLBuf = new StringBuffer(2500);
            SQLBuf.append(" Select tpl_xml From aeTemplate ");
            SQLBuf.append(" Where ");
            SQLBuf.append(" tpl_title = ? ");
            SQLBuf.append(" And tpl_owner_ent_id = ? ");
            String SQL = new String(SQLBuf);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, tpl_title);
            stmt.setLong(2, owner_ent_id);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()) {
                tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
            }
            else
                throw new SQLException("No " + tpl_title + " template defined in database");
            
            stmt.close();
            
            return tpl_xml;
        }
    
    public void getOptionFields_(Connection con, String tpl_xml, Hashtable htCheckbox, Hashtable htRadio, Hashtable htSelect) throws SQLException, IOException, cwException {

          Hashtable h = new Hashtable(); //Hashtable to be returned
          DOMParser xmlParser = new DOMParser(); //get XML parser
          try {
              xmlParser.parse(new InputSource(new StringReader(tpl_xml)));
          } catch(SAXException e) {
              throw new cwException("SAXException at aeTemplate.getOptionFields(): " + e.getMessage());
          }
          Document document = xmlParser.getDocument(); //get parsed XML document
          Node sectionlist = document.getFirstChild(); //get root node
          //NodeList sectionlist = root.getChildNodes(); //get child nodes under roo, section 1 , section 2
          
          //for each child node, if it's fieldtype is 'CHECKBOX', 'RADIO' or 'SELECT'
          //store the values into Hashtable
          //for(int i=0; i<sectionlist.getLength(); i++) {
              //Node section = sectionlist.item(i);
              NodeList nodelist = sectionlist.getChildNodes(); // field01, field02                                        
              for(int j=0; j<nodelist.getLength(); j++) {
                  Node node = nodelist.item(j);
                  String nodeName = node.getNodeName();
                  NamedNodeMap nodeAtt = node.getAttributes();
                  if (nodeAtt != null) {
                      Node att = nodeAtt.getNamedItem("type");
                      if (att!=null){
                          String fieldType = att.getNodeValue();
                          if (fieldType.equalsIgnoreCase("CHECKBOX") || fieldType.equalsIgnoreCase("RADIO") || fieldType.equalsIgnoreCase("SELECT")|| fieldType.equalsIgnoreCase("radio_bonus")){
                              Hashtable htOption = new Hashtable();
                              NodeList optionlist = node.getChildNodes(); // option in field01, field02
                              for(int k=0; k<optionlist.getLength(); k++) {
                                  Node option = optionlist.item(k);
                                  String optionName = option.getNodeName();
                                  
                                  if (optionName.equals(nodeName)){
                                      NamedNodeMap optionAtt = option.getAttributes();
                                      if (optionAtt != null) {
                                          Node attValue = optionAtt.getNamedItem("value");
                                          Node attID = optionAtt.getNamedItem("id");
                                          String value = null;
                                          String id = null;
                                          if (attValue!=null)
                                              value = attValue.getNodeValue();
                                          if (attID!=null)
                                              id = attID.getNodeValue();
                                          if (value!=null && id!=null)
                                              htOption.put(value, id);
                                      }
                                  }
                              }
                              if (fieldType.equalsIgnoreCase("CHECKBOX")){
                                  htCheckbox.put(nodeName, htOption);
                              }else if (fieldType.equalsIgnoreCase("RADIO")||fieldType.equalsIgnoreCase("radio_bonus")){
                                  htRadio.put(nodeName, htOption);
                              }else if (fieldType.equalsIgnoreCase("SELECT")){
                                  htSelect.put(nodeName, htOption);
                              }
                          }
                      }
                  }
              }
         // }
      }
}