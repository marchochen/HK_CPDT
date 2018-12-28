package com.cw.wizbank.ae.db;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.sql.*;

public class DbItemCompetency {
    
    //table column 
    public long itc_itm_id;
    public long itc_skl_skb_id;
    public long itc_skl_level;
    public String itc_create_usr_id;
    public Timestamp itc_create_timestamp;
                
    
    /**
    Insert a record into asItemCompetency table
    pre-defined variable:
    <ul>
    <li>itc_itm_id</li>
    <li>itc_skl_skb_id</li>
    <li>itc_skl_level</li>
    <li>itc_create_usr_id</li>    
    */
    public void ins(Connection con)
        throws SQLException {
            
            
            Timestamp curTime = cwSQL.getTime(con);
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_item_competency);
            stmt.setLong(1, itc_itm_id);
            stmt.setLong(2, itc_skl_skb_id);
            stmt.setLong(3, itc_skl_level);
            stmt.setString(4, itc_create_usr_id);
            stmt.setTimestamp(5, curTime);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
        
        


    /**
    Insert records into asItemCompetency table
    @param Hashtable( skill id as the key , skill level as the value )
    pre-defined variable:
    <ul>
    <li>itc_itm_id</li>
    <li>itc_create_usr_id</li>    
    */
    public void ins(Connection con, Hashtable skillLevel)
        throws SQLException {
            
            
            Timestamp curTime = cwSQL.getTime(con);
            PreparedStatement stmt = null;
            Enumeration enumeration = skillLevel.keys();
            Long skillId;
            while( enumeration.hasMoreElements() ) {
                skillId = (Long)enumeration.nextElement();
                stmt = con.prepareStatement(SqlStatements.sql_ins_item_competency);
                stmt.setLong(1, itc_itm_id);
                stmt.setLong(2, skillId.longValue());
                stmt.setLong(3, ((Long)skillLevel.get(skillId)).longValue());
                stmt.setString(4, itc_create_usr_id);
                stmt.setTimestamp(5, curTime);
                stmt.executeUpdate();
            }
            
            if( stmt != null )
                stmt.close();
                
            return;
            
        }



        
    /**
    Get the skills id and skills level of the specified item id
    @return hashtable ( skill id as the key , skill level as the value )
    pre-defined variable:
    <ul>
    <li>itc_itm_id</li>    
    */
    public Hashtable getByItemId(Connection con)
        throws SQLException {
            

            Hashtable skillLevel = new Hashtable();
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_item_skills);
            stmt.setLong(1, itc_itm_id);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                
                skillLevel.put( new Long(rs.getLong("itc_skl_skb_id")), new Long(rs.getLong("itc_skl_level")) );
                
            }
            stmt.close();
            return skillLevel;
            
        }
        
 
        
    /**
    DELETE the item and skills relation
    @return number of relation deleted
    pre-defined variable:
    <ul>
    <li>itc_itm_id</li>
    */
    public int del(Connection con)
        throws SQLException {
            
            /*
            public static final String sql_del_item_skills_relation = 
            " DELETE FROM aeItemCompetency WHERE itc_itm_id = ? ";
            */
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_item_skills_relation);
            stmt.setLong(1, itc_itm_id);
            int count = stmt.executeUpdate();
            stmt.close();
            return count;
            
        }
        
    /**
    DELETE the item and skills relation of skill id
    @return number of relation deleted
    pre-defined variable:
    <ul>
    <li>itc_skl_skb_id</li>
    */
    public int delBySkillId(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_item_skill_relation_by_skill);
            stmt.setLong(1, itc_skl_skb_id);
            int count = stmt.executeUpdate();
            stmt.close();
            return count;
        }        
        
        
    /**
    Search items id by specifing skills id
    @param vector containing the skills id
    @param boolean value to indicate AND/OR condition ( true : AND, false : OR )
    @return vector containing the search result ( item id )
    */
    public Vector getBySkillsId(Connection con, Vector skillIdVec, boolean flag)
        throws SQLException {
            
            Vector itmIdVec = new Vector();
            if( skillIdVec.isEmpty() )
                return itmIdVec;
                
            String condition;
            if( flag ) {                
                condition = "AND";                
            } else {                
                condition = "OR";                
            }
            
            StringBuffer SQL = new StringBuffer();
            SQL.append(SqlStatements.sql_get_item_by_skills_id).append(" ( ")
               .append(" itc_skl_skb_id = ? ");
            
            for(int i=1; i< skillIdVec.size(); i++) {                
                SQL.append(condition).append(" itc_skl_skb_id = ? ");                
            }
            
            SQL.append(" ) ");
            
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            for(int i=0; i<skillIdVec.size(); i++)
                stmt.setLong(i+1, ((Long)skillIdVec.elementAt(i)).longValue());
            
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {                
                itmIdVec.addElement(new Long(rs.getLong("itc_itm_id")));                
            }
            
            stmt.close();
            return itmIdVec;
            
        }
    
    
    
 
    
    
    /**
    Get the item skills relation by specifing skills id
    @param vector containing skill id
    @return result set
    */
    public ResultSet getItmSkillRelation(Connection con, Vector skillIdVec)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement( SqlStatements.sql_get_item_skill_relation + cwUtils.vector2list(skillIdVec) );
            ResultSet rs = stmt.executeQuery();
            return rs;
                
        }
        
    
        
}