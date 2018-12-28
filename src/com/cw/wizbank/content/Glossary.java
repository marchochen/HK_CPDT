package com.cw.wizbank.content;

import com.cw.wizbank.db.DbCtGlossary;
import com.cw.wizbank.qdb.*;
import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;
import javax.servlet.http.*;

public class Glossary {

    public final static String[] LETTER = { "A", "B", "C", "D", "E", "F", "G", "H", "I", 
                                            "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
                                            "S", "T", "U", "V", "W", "X", "Y", "Z", "OTHER" };

    public final static String COMMA = ",";
    public final static String COLIN = ":";
    public final static String GLO_INDEX = "GLO_INDEX";                                        
                                            
    public Connection con;    
    public DbCtGlossary dbGlossary;
    public dbResource dbRes;
    public dbModule dbMod;
    
    public long glo_res_id;
    public long glo_id;
    public String glo_keyword;
    public String glo_definition;
    public Timestamp glo_create_timestamp;
    public String glo_create_usr_id;
    public Timestamp glo_update_timestamp;
    public String glo_update_usr_id;
           
    public Glossary() { 
    
        dbRes = new dbResource();
        dbMod = new dbModule();
        return;
        
    }
    
    // insert a keyword with a definition into glossary
    public void ins(Connection con, DbCtGlossary dbGlossary, loginProfile prof) 
        throws SQLException, cwException, cwSysMessage { 
                

            //check permissiom
            try{
                dbMod.mod_res_id = dbGlossary.glo_res_id;
                dbMod.get(con);
//                dbMod.checkModifyPermission(con, prof);            
                dbMod.checkTimeStamp(con);
            }catch(qdbException e) {
                throw new cwException(e.toString());
            } catch(qdbErrMessage e) {
                throw new cwSysMessage(e.toString());
            } 


            Timestamp curTime = null;
            try{
                curTime = dbUtils.getTime(con);
            }catch(qdbException e) {
                throw new cwException("Get TimeStamp Error : " + e);
            }
            dbGlossary.glo_create_usr_id = prof.usr_id;
            dbGlossary.glo_create_timestamp = curTime;
            dbGlossary.glo_update_usr_id = prof.usr_id;
            dbGlossary.glo_update_timestamp = curTime;
            dbGlossary.ins(con);
            
            // update the resource table of update user and time
            try{
                dbRes.res_upd_user = prof.usr_id;
                dbRes.res_id = dbGlossary.glo_res_id;
                dbRes.updateTimeStamp(con, curTime);
            }catch(qdbException e) {
                throw new cwException("Update Resource Timestamp Error : " + e);
            }
            return;
        
        }
    
    
    // update the keyword with a definition
    public void upd(Connection con, DbCtGlossary dbGlossary, loginProfile prof)  
        throws SQLException, cwException, cwSysMessage { 
                    
            dbRes.res_id = dbGlossary.getResId(con);
            dbMod.mod_res_id = dbRes.res_id;
            
            //check permissiom
            try{                
                dbMod.get(con);
//                dbMod.checkModifyPermission(con, prof);            
                dbMod.checkTimeStamp(con);
            }catch(qdbException e) {
                throw new cwException(e.toString());
            } catch(qdbErrMessage e) {
                throw new cwSysMessage(e.toString());
            }           

            
            Timestamp curTime = null;
            try{
                curTime = dbUtils.getTime(con);
            }catch(qdbException e) {
                throw new cwException("Get TimeStamp Error!");
            }
                        
            dbGlossary.glo_update_usr_id = prof.usr_id;
            dbGlossary.glo_update_timestamp = curTime;
            dbGlossary.upd(con);
        
            try{
                dbRes.res_upd_user = prof.usr_id;
                dbRes.updateTimeStamp(con, curTime);
            }catch(qdbException e) {
                throw new cwException("Update Resource Timestamp Error!");
            }
            return;

        }
    
    
    // delete a keyword and definition
    public void del(Connection con, DbCtGlossary dbGlossary, loginProfile prof)  
        throws SQLException, cwException, cwSysMessage { 
            

            dbRes.res_id = dbGlossary.getResId(con);
            dbMod.mod_res_id = dbRes.res_id;
            
            //check permissiom
            try{                
                dbMod.get(con);
//                dbMod.checkModifyPermission(con, prof);            
                dbMod.checkTimeStamp(con);
            }catch(qdbException e) {
                throw new cwException(e.toString());
            } catch(qdbErrMessage e) {
                throw new cwSysMessage(e.toString());
            }        

        
            Timestamp curTime = null;
            try{
                curTime = dbUtils.getTime(con);
            }catch(qdbException e) {
                throw new cwException("Get TimeStamp Error!");
            }      
        
            try{
                dbRes.res_upd_user = prof.usr_id;                
                dbRes.updateTimeStamp(con, curTime);
            }catch(qdbException e) {
                throw new cwException("Update Resource Timestamp Error!");
            }
        
            dbGlossary.del(con);
        
            return;        
        }
        
    
    
    
    // return the index and content of keywords
    // index : the letter list which contains a number on keyword in each letter
    // content : all keywords of the specified letter
    public String asXML(Connection con, DbCtGlossary dbGlossary, loginProfile prof, String letter, Timestamp indexTimestamp, HttpSession sess, long tkh_id)  
        throws SQLException, cwException {
            
            Timestamp lastUpdate = null;

            try{
                dbRes.res_id = dbGlossary.glo_res_id;
                lastUpdate = dbRes.getUpdateTimeStamp(con);
            }catch(qdbException e) {
                throw new cwException("Get Resource Update Timestamp Error!");
            }

            StringBuffer xml = new StringBuffer();
            
            xml.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" ");
            xml.append(" standalone=\"no\" ?>").append(dbUtils.NEWL);
            
            xml.append("<glossary res_id=\"").append(dbGlossary.glo_res_id).append("\" tkh_id=\"").append(tkh_id).append("\" >");
            xml.append(dbUtils.NEWL).append(prof.asXML()).append(dbUtils.NEWL);
            xml.append("<index timestamp=\"").append(lastUpdate).append("\">").append(dbUtils.NEWL);
            
            // if the glossary have been modified, get the new index from database
            // and store the new index in session
            if( indexTimestamp == null || !lastUpdate.equals(indexTimestamp) ) {
                Hashtable indexTable = dbGlossary.getIndex(con);
                xml.append(constructIndex(indexTable,sess));
            }
            else {
            // otherwise get from sess    
                xml.append(getIndex(sess));
            }
            
            xml.append("</index>").append(dbUtils.NEWL);
            
            // if a letter is specified, list all keyword belonged to it
            // otherwise only show the index without content part
            if(letter != null) {
                // get the required glossary ids from session
                long[] glo_ids = getIdsList(letter,sess);
                if(glo_ids != null) {
                    Vector contentVec = dbGlossary.getContent(glo_ids,con);
                    if( inArray(LETTER,letter) )
                        xml.append("<content initial=\"").append(letter).append("\">").append(dbUtils.NEWL);
                    else
                        xml.append("<content initial=\"OTHER\">").append(dbUtils.NEWL);
                    xml.append(constructContent(contentVec));
                    xml.append("</content>").append(dbUtils.NEWL);
                }
            }
            xml.append("</glossary>");
            return xml.toString();
        }
    

    
    
    // return the keyword and definition
    public String asXML(Connection con, DbCtGlossary dbGlossary, loginProfile prof)  
        throws SQLException, cwException {
            
            StringBuffer xml = new StringBuffer();            
            
            xml.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" ");
            xml.append(" standalone=\"no\" ?>").append(dbUtils.NEWL);
            
            xml.append("<glossary id=\"").append(dbGlossary.glo_id).append("\" >");
            
            xml.append(dbUtils.NEWL).append(prof.asXML()).append(dbUtils.NEWL);
            
            long[] glo_ids = new long[1];
            glo_ids[0] = dbGlossary.glo_id;
            if(glo_ids != null) {
                Vector contentVec = dbGlossary.getContent(glo_ids,con);    
                xml.append("<content>").append(dbUtils.NEWL);
                xml.append(constructContent(contentVec));
                xml.append("</content>").append(dbUtils.NEWL);
            }            
            
            xml.append("</glossary>");            
            return xml.toString();
        }
    
    
    
    // construct a index from the data stored in the session
    public String getIndex(HttpSession sess) {
        StringBuffer xml = new StringBuffer();
        String indexList = (String)sess.getAttribute(GLO_INDEX);
        String[] st = dbUtils.split(indexList,COLIN);
        StringTokenizer sub_st = null;
        int count = 0;
        
        for( int i = 0; i < st.length; i++ ) {
            sub_st = new StringTokenizer(st[i],COMMA);
            count = sub_st.countTokens();
            if( count > 0 ) {
                xml.append("<initial char=\"").append(LETTER[i]).append("\" ");
                xml.append(" count=\"").append(count).append("\"/>");                                
                xml.append(dbUtils.NEWL);                
            }            
        }
        return xml.toString();
    }
    
    
    // get the glossary ids of the specified letter from session
    public long[] getIdsList(String _letter, HttpSession sess) {
                
        String indexList = (String)sess.getAttribute(GLO_INDEX);
        String[] st = dbUtils.split(indexList,COLIN);
        String[] idStrList;
        String idStr = "";
        idStr = st[26];
        for( int i = 0; i < 26; i++ ) {
            if( _letter.compareTo(LETTER[i]) == 0 ) {
                idStr = st[i];
                break;
            }
        }
        idStrList = dbUtils.split(idStr,COMMA);
        if(idStrList != null) {
            long[] idLongList = new long[idStrList.length];
            for(int i = 0; i < idStrList.length; i++)
                idLongList[i] = Long.parseLong(idStrList[i]);        
            return idLongList;
        }
        else
            return null;
    }
    
    
    // construct a new index by getting a data from database and store it in the session
    // comma and colin to be the delimiter of glossary id and letter respectively in the string
    public String constructIndex(Hashtable indexTable, HttpSession sess) {
        StringBuffer xml = new StringBuffer();
        StringBuffer indexList = new StringBuffer();
        Vector[] letter = new Vector[27];
        for(int i=0; i<27; i++) {
            letter[i] = (Vector)indexTable.get(LETTER[i]);
            if( !letter[i].isEmpty() ) {
                xml.append("<initial char=\"").append(LETTER[i]).append("\" ");
                xml.append(" count=\"").append(letter[i].size()).append("\"/>");                                
                xml.append(dbUtils.NEWL);
                
                for(int j=0; j<letter[i].size(); j++) {
                    indexList.append((Long)letter[i].elementAt(j));
                    if( j == letter[i].size()-1 )
                        indexList.append(COLIN);
                    else
                        indexList.append(COMMA);                    
                }                
            }
            else
                indexList.append(COLIN);
        }
        
        sess.setAttribute(GLO_INDEX,indexList.toString());
        return xml.toString();
    }
    
    
    // Construct a content with a keyword and it's definition
    public String constructContent(Vector contentVec) {
        Vector keyVec = (Vector)contentVec.elementAt(0);
        Vector defVec = (Vector)contentVec.elementAt(1);
        Vector idVec = (Vector)contentVec.elementAt(2);
        StringBuffer xml = new StringBuffer();
        
        for( int i = 0; i < keyVec.size(); i++ ) {
            xml.append("<key word=\"").append(dbUtils.esc4XML( (String)keyVec.elementAt(i) )).append("\" ");
            xml.append(" id=\"").append((Long)idVec.elementAt(i)).append("\" >");
            xml.append(dbUtils.esc4XML( (String)defVec.elementAt(i) ));
            xml.append("</key>").append(dbUtils.NEWL);
        }
        return xml.toString();
    }
    
    
    // check the item in the array list
    public boolean inArray(String[] array, String item) {
        for(int i=0; i<array.length; i++) 
            if(array[i].equalsIgnoreCase(item))
                return true;
        
        return false;        
    }
    
}

