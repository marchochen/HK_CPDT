package com.cw.wizbank.search;

import java.io.*;
import java.util.Vector;
import java.sql.Timestamp;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.analysis.cn.*;

import com.cw.wizbank.db.DbKmObject;
import com.cw.wizbank.db.view.ViewKmObject;
import com.cw.wizbank.qdb.dbAttachment;
import com.cwn.wizbank.utils.CommonLog;

public class Search {
    String indexPath = null;
    
    public Search (String indexPath) {
        this.indexPath = indexPath;
    }
    
    public Vector adv_search(String obj_title, String obj_author, String[] obj_type_list, String key_words, String call_num, Vector vtAccessibleDomain, int limit) {
        Searcher searcher = null;
        String domainList = null;
        Hits hits = null;        
        
        try {
            if (IndexReader.indexExists(new File(indexPath)) == false) {
                return (new Vector());
            }
            
            searcher = new IndexSearcher(indexPath);
            for (int i=0; i<vtAccessibleDomain.size(); i++) {
                if (domainList == null) {
                    domainList = IndexFiles.DOMAIN + ((Long)vtAccessibleDomain.elementAt(i)).toString();
                }
                else {
                    domainList += " OR " + IndexFiles.DOMAIN + ((Long)vtAccessibleDomain.elementAt(i)).toString();
                }
            }
            
            String temp_query = "";
            /*
            if (obj_type != null) {
                temp_query += IndexFiles.TYPE_ID + ":" + obj_type + " AND ";
            }
            */
            if( obj_type_list != null && obj_type_list.length > 0 ){
                temp_query += "(" + IndexFiles.TYPE_ID + ":" + obj_type_list[0];
                for(int i=1; i<obj_type_list.length; i++)
                    temp_query += " OR " + IndexFiles.TYPE_ID + ":\"" + obj_type_list[i] + "\"";
                temp_query += " ) AND ";
            }
            
            temp_query  +=  "  ( 1 ";
            if( obj_title != null && obj_title.length() > 0 ) {
                temp_query += "  OR (" + IndexFiles.TITLE + ":\"" + obj_title + "\")  ";
            }
            
            if( obj_author != null && obj_author.length() > 0 ) {
                temp_query += " OR  (" + IndexFiles.AUTHOR + ":\"" + obj_author + "\")  ";
            }

            if( call_num != null && call_num.length() > 0 ) {
                temp_query += "  OR (" + IndexFiles.CALL_NUMBER + ":\"" + call_num + "\")  ";
            }

            if( key_words != null && key_words.length() > 0 ) {
                temp_query += "  OR ( "
                           + IndexFiles.BODY + ":\"" + key_words + "\""
                           + " OR " 
                           + IndexFiles.KEYWORD + ":\"" + key_words + "\""
                           + " ) ";
            }
            temp_query  +=  "  ) AND ";
            temp_query += "(" + domainList + ")";
//System.out.println("temp_query:" + temp_query);
            /*
            temp_query += "(" + IndexFiles.BODY + ":\"" + query_string + "\" 
                         OR " + IndexFiles.KEYWORD + ":\"" + query_string + "\" 
                         OR " + IndexFiles.TEMPLATE_DATA + ":\"" + query_string  + "\") 
                        AND (" + domainList + ")";
            */
            
            Query query = QueryParser.parse(temp_query, IndexFiles.DOMAIN_ID, new ChineseAnalyzer());
            hits = searcher.search(query);
//System.out.println("temp_query:" + temp_query);
            return search(hits, limit);
        }catch(Exception e){
        	CommonLog.error(e.getMessage(),e);
        }
        return (new Vector());
    }
    
    public Vector simple_search(String obj_type, String query_string, Vector vtAccessibleDomain, int limit) {
        Searcher searcher = null;
        String domainList = null;
        Hits hits = null;        
        
        try {
            if (IndexReader.indexExists(new File(indexPath)) == false) {
                return (new Vector());
            }
            
            searcher = new IndexSearcher(indexPath);
            for (int i=0; i<vtAccessibleDomain.size(); i++) {
                if (domainList == null) {
                    domainList = IndexFiles.DOMAIN + ((Long)vtAccessibleDomain.elementAt(i)).toString();
                }
                else {
                    domainList += " OR " + IndexFiles.DOMAIN + ((Long)vtAccessibleDomain.elementAt(i)).toString();
                }
            }
            
            String temp_query = "";
            if (obj_type != null) {
                temp_query += IndexFiles.TYPE_ID + ":" + obj_type + " AND ";
            }
            
            temp_query += "(" + IndexFiles.BODY + ":\"" + query_string + "\" OR " + IndexFiles.KEYWORD + ":\"" + query_string + "\" OR " + IndexFiles.TEMPLATE_DATA + ":\"" + query_string  + "\") AND (" + domainList + ")";
            
            Query query = QueryParser.parse(temp_query, IndexFiles.DOMAIN_ID, new ChineseAnalyzer());
            hits = searcher.search(query);
//System.out.println("temp_query:" + temp_query);
            return search(hits, limit);
        }catch(Exception e){
        	CommonLog.error(e.getMessage(),e);
        }
        return (new Vector());
    }
            
    private Vector search(Hits hits, int limit){             
        try{
            
            Vector vtViewKmObject = new Vector();            

            if (limit > hits.length()) {
                limit = hits.length();
            }
                                    
            for (int i=0; i<limit; i++) {
                
                ViewKmObject object = new ViewKmObject();
                
                // a known bug that the search score will be a very large number in some search criteria
                if (Float.isNaN(hits.score(i)) == true || hits.score(i) > 1) {
                    object.hits_score = 1;
                }
                else {
                    object.hits_score = hits.score(i);
                }
                
                object.dbObject = new DbKmObject();
                
                if (hits.doc(i).get(IndexFiles.OBJ_ID) != null) {
                    object.dbObject.obj_bob_nod_id = Long.parseLong(hits.doc(i).get(IndexFiles.OBJ_ID));
                }
                object.dbObject.obj_version = hits.doc(i).get(IndexFiles.VERSION_NUMBER);
                object.dbObject.obj_type = hits.doc(i).get(IndexFiles.TYPE_ID);
                object.dbObject.obj_title = hits.doc(i).get(IndexFiles.TITLE);
                object.dbObject.obj_desc = hits.doc(i).get(IndexFiles.DESCRIPTION);
                object.dbObject.obj_keywords = hits.doc(i).get(IndexFiles.KEYWORD);
                object.dbObject.obj_author = hits.doc(i).get(IndexFiles.AUTHOR);
                object.dbObject.obj_code = hits.doc(i).get(IndexFiles.CALL_NUMBER);
                if (hits.doc(i).get(IndexFiles.MODIFIED_DATE) != null) {
                    object.dbObject.obj_update_timestamp = new Timestamp(Long.parseLong(hits.doc(i).get(IndexFiles.MODIFIED_DATE)));
                }
                
                dbAttachment myDbAttachment= new dbAttachment();
                myDbAttachment.att_filename = hits.doc(i).get(IndexFiles.FILENAME);
                object.vAttachment = new Vector();
                object.vAttachment.addElement(myDbAttachment);
                
                vtViewKmObject.addElement(object);
            }
            
            return vtViewKmObject;
        } catch (Exception e) {
        	CommonLog.error(e.getMessage(),e);
        }        
        
        return (new Vector());
    }

  public static void main(String[] args) throws Exception {
    String indexPath = args[0], queryString = args[1];

    Search mySearch = new Search(args[0]);
    Vector vtAccessibleDomain = new Vector();
    for (int i=3; i<args.length-1; i++) {
        vtAccessibleDomain.addElement(new Long(args[i]));
    }
    Vector vtViewKmObject = mySearch.simple_search(args[1], args[2], vtAccessibleDomain, Integer.parseInt(args[args.length-1]));
    
    for (int i=0; i<vtViewKmObject.size(); i++) {
        ViewKmObject object = (ViewKmObject)vtViewKmObject.elementAt(i);
        /*
        System.out.println(object.hits_score);
        System.out.println(object.dbObject.obj_bob_nod_id);
        System.out.println(object.dbObject.obj_version);
        System.out.println(object.dbObject.obj_type);
        System.out.println(object.dbObject.obj_title);
        System.out.println(object.dbObject.obj_desc);
        System.out.println(object.dbObject.obj_keywords);
        System.out.println(object.dbObject.obj_author);
        System.out.println(object.dbObject.obj_update_timestamp);
        */
        dbAttachment myDbAttachment= (dbAttachment)object.vAttachment.elementAt(0);
        System.out.println(myDbAttachment.att_filename);
    }

  }
}