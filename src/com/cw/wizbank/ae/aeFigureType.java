package com.cw.wizbank.ae;

import java.sql.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.DbFigureType;


public class aeFigureType {
    
    
    public static String getAllAsXML(Connection con, long owner_ent_id)
        throws SQLException {
            
            StringBuffer xml = new StringBuffer();
            xml.append("<figure_list>").append(cwUtils.NEWL);
            DbFigureType[] dbfigureTypes = DbFigureType.getAll(con, owner_ent_id);
            for (int i=0; i<dbfigureTypes.length; i++){
                xml.append("<figure ")
                   .append(" id=\"").append(dbfigureTypes[i].fgt_id).append("\" ")
                   .append(" type=\"").append(dbfigureTypes[i].fgt_type).append("\" ")
                   .append(" subtype=\"").append(cwUtils.escNull(dbfigureTypes[i].fgt_subtype)).append("\" ")
                   //.append(" code=\"").append(cwUtils.escNull(rs.getString("ict_code"))).append("\" ")
                   .append(">").append(cwUtils.NEWL)
                   .append(dbfigureTypes[i].fgt_xml)
                   .append("</figure>").append(cwUtils.NEWL);
            }
            xml.append("</figure_list>").append(cwUtils.NEWL);
            return xml.toString();
        }
    
    
}