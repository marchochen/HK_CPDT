package com.cw.wizbank.wizcase;

import java.io.*;
import java.sql.*;

import javax.servlet.http.*;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class dbUtils {
    
    public final static int CONTENT_PAGE    =   1;
    public final static int TOC             =   2;
    
    long cos_id;
    long mod_id;
    long obj_id;
    long usr_ent_id;
    int attempt_nbr;
    Connection con;
    boolean cacheData;
    HttpSession sess;

    public dbUtils(Connection con, long usr_ent_id, long cos_id, long mod_id, boolean cacheData, HttpSession sess)
        throws SQLException{
            this.con = con;
            this.usr_ent_id = usr_ent_id;
            this.mod_id = mod_id;
            this.cos_id = cos_id;
            this.cacheData = cacheData;
            this.sess = sess;
            this.attempt_nbr = 1;
            return;
        }


    public void putToDb(int type, String developer_id, Node xml)
        throws SQLException {

            //Convert the xml node to plain text format
            StringBuffer xmlText = new StringBuffer();
            xmlUtils.print(xml, xmlText);
            
            if( cacheData ) {
                if( type == CONTENT_PAGE )
                    sess.setAttribute("SESS_WIZCASE_CONTENT_PAGE_" + developer_id, xml);
                else if( type == TOC )
                    sess.setAttribute("SESS_WIZCASE_TOC", xml);
            }
            
            if( type == CONTENT_PAGE )
                obj_id = getObjId(developer_id);       
            
            if( recordExist(type) ) {
                //UPD                
                //upd(type, xmlText.toString());
                if( cacheData ) {
                    updThread t = new updThread();
                    t.type = type;
                    t.xmlText = xmlText.toString();
                    new Thread(t).start();
                }else {
                    upd(type, xmlText.toString());
                }
            } else {
                //INS
                //ins(type, xmlText.toString());
                if( cacheData ) {
                    insThread t = new insThread();
                    t.type = type;
                    t.xmlText = xmlText.toString();
                    new Thread(t).start();
                }else {
                    ins(type, xmlText.toString());
                }
            }
            con.commit();
            return;

        }



    public Node getFromDb(int type, String developer_id)
        throws SQLException, IOException {

            try{
                if( cacheData ){
                    if( type == CONTENT_PAGE ) {
                        if( sess.getAttribute("SESS_WIZCASE_CONTENT_PAGE_" + developer_id) != null )
                            return (Node)sess.getAttribute("SESS_WIZCASE_CONTENT_PAGE_" + developer_id);
                    }else if( type == TOC ) {
                        if( sess.getAttribute("SESS_WIZCASE_TOC") != null )
                            return (Node)sess.getAttribute("SESS_WIZCASE_TOC");
                    }   
                }
                
                if( type == CONTENT_PAGE )
                    obj_id = getObjId(developer_id); 

                String xmlText = get(type);
                if( xmlText == null ) {
                    return null;                
                }else {
                    return xmlUtils.parseXMLString(xmlText);
                }
            }catch( SAXException e ) {
                throw new SQLException("Failed to Parse the String get from DB : " + e.getMessage());
            }
        }
    
    
    
    
    
    public boolean recordExist(int type)
        throws SQLException {
            
            int count = 0;
            if( type == CONTENT_PAGE ) {
                String SQL = " SELECT COUNT(apm_ent_id) "
                           + " FROM Accomplishment "
                           + " WHERE apm_ent_id = ? "
                           + " AND apm_obj_id = ? "
                           + " AND apm_attempt_nbr = ? ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, usr_ent_id);
                stmt.setLong(2, obj_id);
                stmt.setLong(3, attempt_nbr);
                ResultSet rs = stmt.executeQuery();                
                if( rs.next() )
                    count = rs.getInt(1);
                stmt.close();
            } else if( type == TOC ) {
                
                String SQL = " SELECT COUNT(mov_ent_id) "
                           + " FROM ModuleEvaluation "
                           + " WHERE mov_cos_id = ? "
                           + " AND mov_mod_id = ? "
                           + " AND mov_ent_id = ? ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, cos_id);
                stmt.setLong(2, mod_id);
                stmt.setLong(3, usr_ent_id);
                ResultSet rs = stmt.executeQuery();
                if( rs.next() )
                    count = rs.getInt(1);
                stmt.close();
                
            }
            
            if( count == 0 )
                return false;
            else
                return true;

        }

        
        
    public String get(int type)
        throws SQLException {
            
            String xmlText = null;
            if( type == CONTENT_PAGE ) {
CommonLog.debug("Get Content Page From Db : " + obj_id);
                String SQL = " SELECT apm_data_xml " 
                           + " FROM Accomplishment "
                           + " WHERE apm_ent_id = ? "
                           + " AND apm_obj_id = ? "
                           + " AND apm_attempt_nbr = ? ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, usr_ent_id);
                stmt.setLong(2, obj_id);
                stmt.setInt(3, attempt_nbr);

                ResultSet rs = stmt.executeQuery();
                if( rs.next() )
                    xmlText = rs.getString("apm_data_xml");
                stmt.close();                        
            } else if( type == TOC ) {
CommonLog.debug("Get TOC From Db");
                String SQL = " SELECT mov_data_xml "
                           + " FROM ModuleEvaluation "
                           + " WHERE mov_cos_id = ? "
                           + " AND mov_mod_id = ? "
                           + " AND mov_ent_id = ? ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, cos_id);
                stmt.setLong(2, mod_id);
                stmt.setLong(3, usr_ent_id);
                ResultSet rs = stmt.executeQuery();
                if( rs.next() )
                    xmlText = rs.getString("mov_data_xml");
                stmt.close();
                
            }
            
            return xmlText;
        }
        

    public void ins(int type, String xmlText)
        throws SQLException {

            if( type == CONTENT_PAGE ) {
CommonLog.debug("Insert Content_page to Db : " + obj_id);
                String SQL = " INSERT INTO Accomplishment "
                           + " ( apm_ent_id, apm_obj_id, apm_attempt_nbr, apm_data_xml ) "
                           + " VALUES(?, ?, ?, ?) ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, usr_ent_id);
                stmt.setLong(2, obj_id);
                stmt.setLong(3, attempt_nbr);
                stmt.setString(4, xmlText);
                if( stmt.executeUpdate() != 1 )
                    throw new SQLException("Failed to insert a record, obj_id = " + obj_id);
                stmt.close();
                
            } else if( type == TOC ) {
CommonLog.debug("Insert a TOC to DB");
                String SQL = " INSERT INTO ModuleEvaluation "
                           + " (mov_cos_id, mov_ent_id, mov_mod_id, mov_last_acc_datetime, mov_total_time, mov_status, mov_data_xml) " 
                           + " VALUES(?, ?, ?, ?, ?, ?, ?) ";
                           
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, cos_id);
                stmt.setLong(2, usr_ent_id);
                stmt.setLong(3, mod_id);
                stmt.setTimestamp(4, cwSQL.getTime(con));
                stmt.setLong(5, 0);
                stmt.setString(6, "I");
                stmt.setString(7, xmlText);
                if( stmt.executeUpdate() != 1 )
                    throw new SQLException("Failed to insert a record, mod_id = " + mod_id);
                stmt.close();
            }
            
            return;   
        }


    public void upd(int type, String xmlText)
        throws SQLException {

            if( type == CONTENT_PAGE ) {
CommonLog.debug("UPDATE Content_page : " + obj_id);
                String SQL = " UPDATE Accomplishment SET apm_data_xml = ? "
                           + " WHERE apm_ent_id = ? "
                           + " AND apm_obj_id = ? "
                           + " AND apm_attempt_nbr = ? ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setString(1, xmlText);
                stmt.setLong(2, usr_ent_id);
                stmt.setLong(3, obj_id);
                stmt.setLong(4, attempt_nbr);
                if( stmt.executeUpdate() != 1 )
                    throw new SQLException("Failed to update record, obj_id = " + obj_id);
                stmt.close();

            } else if( type == TOC ) {
CommonLog.debug("UPDATE TOC");
                String SQL = " UPDATE ModuleEvaluation SET mov_data_xml = ? " 
                           + " WHERE mov_cos_id = ? "
                           + " AND mov_mod_id = ? "
                           + " AND mov_ent_id = ? ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setString(1, xmlText);
                stmt.setLong(2, cos_id);
                stmt.setLong(3, mod_id);
                stmt.setLong(4, usr_ent_id);
                if( stmt.executeUpdate() != 1 )
                    throw new SQLException("Failed to update record, mod_id = " + mod_id);
                stmt.close();
                
            }
            return;            
        }





    public long getObjId(String developer_id)
        throws SQLException {
            
            String SQL = " SELECT obj_id "
                       + " FROM ResourceObjective , Objective "
                       + " WHERE rob_res_id = ? "
                       + " AND rob_obj_id = obj_id "
                       + " AND obj_developer_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mod_id);
            stmt.setString(2, developer_id);
            ResultSet rs = stmt.executeQuery();
            long id;
            if(rs.next())
                id = rs.getLong("obj_id");
            else
                throw new SQLException("Failed to get the objective id, developer_id = " + developer_id);
                
            stmt.close();
            return id;
        }



    public int getAttemptNbr()
        throws SQLException {
            /*
            String SQL = " SELECT apm_attempt_nbr, apm_status "
                       + " FROM Accomplishment, ResourceObjective "
                       + " WHERE apm_ent_id = ? "
                       + " AND rob_obj_id = apm_obj_id "
                       + " AND rob_res_id = ? "
                       + " GROUP by apm_attempt_nbr, apm_status "
                       + " ORDER BY apm_attempt_nbr DESC ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, usr_ent_id);
            stmt.setLong(2, mod_id);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            if(rs.next()){
                String status = rs.getString("apm_status");
                count = rs.getInt("apm_attempt_nbr");
                if(status != null && status.equalsIgnoreCase("C"))
                    count++;
            } else 
                count++;
            stmt.close();
            */
            
            String SQL = " SELECT MAX(apm_attempt_nbr) "
                       + " FROM Accomplishment , ResourceObjective "
                       + " WHERE apm_ent_id = ? "
                       + " AND apm_obj_id = apm_obj_id "
                       + " AND rob_res_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, usr_ent_id);
            stmt.setLong(2, mod_id);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            if(rs.next())
                count = rs.getInt(1);
            stmt.close();
CommonLog.debug("Count = " + count);
            return count;
            
        }



    public void reset()
        throws SQLException {
            
            String SQL = " UPDATE Accomplishment "
                       + " SET apm_data_xml = ? "
                       + " WHERE apm_obj_id IN "
                       + " ( SELECT rob_obj_id "
                       + "   FROM ResourceObjective "
                       + "   WHERE rob_res_id = ? ) "
                       + " AND apm_ent_id = ? "
                       + " AND apm_attempt_nbr = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, null);
            stmt.setLong(2, mod_id);
            stmt.setLong(3, usr_ent_id);
            stmt.setLong(4, attempt_nbr);
            stmt.executeUpdate();
            stmt.close();
            return;
            /*
            String SQL = " UPDATE Accomplishment "
                       + " SET apm_status = ? "
                       + " WHERE apm_obj_id IN "
                       + " ( SELECT rob_obj_id "
                       + "   FROM ResourceObjective "
                       + "   WHERE rob_res_id = ? ) "
                       + " AND apm_ent_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, "C");
            stmt.setLong(2, mod_id);
            stmt.setLong(3, usr_ent_id);
            stmt.executeUpdate();
            stmt.close();
            attempt_nbr++;
            return;
            */
            /*
            attempt_nbr++;// = getAttemptNbr() + 1;
            
            String SQL = " SELECT rob_obj_id FROM ResourceObjective "
                       + " WHERE rob_res_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mod_id);            
            ResultSet rs = stmt.executeQuery();
            Vector objIdVec = new Vector();
            while(rs.next())
                objIdVec.addElement(new Long(rs.getLong("rob_obj_id")));
            stmt.close();
System.out.println("mod id = " + mod_id + " , objId Size = " + objIdVec.size());
            
            SQL = " INSERT INTO Accomplishment ( apm_ent_id, apm_obj_id, apm_attempt_nbr ) "
                  + " VALUES ( ?, ?, ? ) ";
            
            for(int i=0; i<objIdVec.size(); i++){
                stmt = con.prepareStatement(SQL);
                stmt.setLong(1, usr_ent_id);
                stmt.setLong(2, ((Long)objIdVec.elementAt(i)).longValue());
                stmt.setLong(3, attempt_nbr);
                if( stmt.executeUpdate() != 1 )
                    throw new SQLException("Failed to insert a accomplishment record");
                stmt.close();
            }            
            
            
            SQL = " UPDATE ModuleEvaluation SET mov_data_xml = ? "
                + " WHERE mov_mod_id = ? AND mov_ent_id = ? AND mov_cos_id = ? ";
                
            stmt = con.prepareStatement(SQL);
            stmt.setString(1, null);
            stmt.setLong(2, mod_id);
            stmt.setLong(3, usr_ent_id);
            stmt.setLong(4, cos_id);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to update ModuleEvaluation record");
            stmt.close();
            return;
        }
        */
        }
        
        
        
        
    private class updThread implements Runnable {
        int type;
        String xmlText;
        public void run() {
            try{
                if( type == CONTENT_PAGE ) {
CommonLog.debug("UPDATE Content_page : " + obj_id);
                    String SQL = " UPDATE Accomplishment SET apm_data_xml = ? "
                            + " WHERE apm_ent_id = ? "
                            + " AND apm_obj_id = ? "
                            + " AND apm_attempt_nbr = ? ";                
                    PreparedStatement stmt = con.prepareStatement(SQL);
                    stmt.setString(1, xmlText);
                    stmt.setLong(2, usr_ent_id);
                    stmt.setLong(3, obj_id);
                    stmt.setLong(4, attempt_nbr);
                    if( stmt.executeUpdate() != 1 )
                        throw new SQLException("Failed to update record, obj_id = " + obj_id);
                    stmt.close();
                } else if( type == TOC ) {
CommonLog.debug("UPDATE TOC");
                    String SQL = " UPDATE ModuleEvaluation SET mov_data_xml = ? " 
                            + " WHERE mov_cos_id = ? "
                            + " AND mov_mod_id = ? "
                            + " AND mov_ent_id = ? ";
                    PreparedStatement stmt = con.prepareStatement(SQL);
                    stmt.setString(1, xmlText);
                    stmt.setLong(2, cos_id);
                    stmt.setLong(3, mod_id);
                    stmt.setLong(4, usr_ent_id);
                    if( stmt.executeUpdate() != 1 )
                        throw new SQLException("Failed to update record, mod_id = " + mod_id);
                    stmt.close();
                }
                return;           
            }catch( SQLException e ) {
            	CommonLog.error("Failed to execute SQL upd statement");
            }
        }

    }
    
    
    private class insThread implements Runnable {
        int type;
        String xmlText;
        public void run(){
            try{
                if( type == CONTENT_PAGE ) {
CommonLog.debug("Insert Content_page to Db : " + obj_id);
                    String SQL = " INSERT INTO Accomplishment "
                            + " ( apm_ent_id, apm_obj_id, apm_attempt_nbr, apm_data_xml ) "
                            + " VALUES(?, ?, ?, ?) ";
                    
                    PreparedStatement stmt = con.prepareStatement(SQL);
                    stmt.setLong(1, usr_ent_id);
                    stmt.setLong(2, obj_id);
                    stmt.setLong(3, attempt_nbr);
                    stmt.setString(4, xmlText);
                    if( stmt.executeUpdate() != 1 )
                        throw new SQLException("Failed to insert a record, obj_id = " + obj_id);
                    stmt.close();
                    
                } else if( type == TOC ) {
CommonLog.debug("Insert a TOC to DB");
                    String SQL = " INSERT INTO ModuleEvaluation "
                            + " (mov_cos_id, mov_ent_id, mov_mod_id, mov_last_acc_datetime, mov_total_time, mov_status, mov_data_xml) " 
                            + " VALUES(?, ?, ?, ?, ?, ?, ?) ";
                               
                    PreparedStatement stmt = con.prepareStatement(SQL);
                    stmt.setLong(1, cos_id);
                    stmt.setLong(2, usr_ent_id);
                    stmt.setLong(3, mod_id);
                    stmt.setTimestamp(4, cwSQL.getTime(con));
                    stmt.setLong(5, 0);
                    stmt.setString(6, "I");
                    stmt.setString(7, xmlText);
                    if( stmt.executeUpdate() != 1 )
                        throw new SQLException("Failed to insert a record, mod_id = " + mod_id);
                    stmt.close();
                }
                return;
            }catch( SQLException e ) {
            	CommonLog.error("Failed to execute SQL ins statement");
            }
        }
    }

}