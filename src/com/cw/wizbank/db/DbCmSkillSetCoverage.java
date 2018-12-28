package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

/**
A database class to manage table cmSkillCoverage
*/
public class DbCmSkillSetCoverage {
    
    public long         ssc_sks_skb_id;     //skill set id
    public long         ssc_skb_id;         //skill base id
    public float        ssc_level;
    public long         ssc_priority;
    public String       ssc_xml;
    //public String       ssc_response;
    //public boolean      ssc_xml_ind;        //override the xml in cmSkill
    //public boolean      ssc_comment_ind;    //Used in Survey , provide comment field of the question
    
    //variable not for database
    public String       skb_type;
    public long         level;
    public boolean 		bAnswered;
    
    private static final String COMMA       =   ",";
    
    private static final String SQL_INS = " INSERT INTO cmSkillSetCoverage ( " 
                                  + "  ssc_sks_skb_id, ssc_skb_id, " 
                                  + "  ssc_level, ssc_priority, "
                                  //+ "  ssc_xml, ssc_response, "
                                  + "  ssc_xml ) "
                                  + " VALUES (?,?, ?,?, ?) ";


    private static final String SQL_UPD = " UPDATE cmSkillSetCoverage "
                                        + " SET ssc_level = ?, ssc_priority = ?, "
                                        + " ssc_xml = ? "
                                        + " WHERE ssc_sks_skb_id = ? AND ssc_skb_id = ? ";
                                  
    private static final String SQL_DEL = " DELETE From cmSkillSetCoverage "
                                  + " WHERE ssc_sks_skb_id = ? AND ssc_skb_id = ? ";

    private static final String SQL_DEL_SKILLS = " DELETE From cmSkillSetCoverage "
                                  + " WHERE ssc_sks_skb_id = ? AND ssc_skb_id IN ";

    private static final String SQL_DEL_NOT_IN_LIST = " DELETE From cmSkillSetCoverage "
                                    + " WHERE ssc_sks_skb_id = ? AND ssc_skb_id NOT IN ";

    private static final String SQL_DEL_ALL_SKILLS = " DELETE From cmSkillSetCoverage "
                                  + " WHERE ssc_sks_skb_id IN ";

    private static final String SQL_CHECK_SKILL_EXIST = " SELECT count(*) FROM cmSkillSetCoverage "
                                  + " WHERE ssc_skb_id = ? ";

    private static final String SQL_SKILL_EXIST = " SELECT COUNT(*) FROM cmSkillSetCoverage "
                                    + " WHERE ssc_sks_skb_id = ? AND ssc_skb_id = ? ";

    private static final String SQL_GET_MAX_PRIORITY = " SELECT MAX(ssc_priority) FROM cmSkillSetCoverage "
                                    + " WHERE ssc_sks_skb_id = ? "
                                    + " GROUP BY ssc_sks_skb_id ";
    
    private static final String SQL_GET_SKILLS_ID = " SELECT ssc_skb_id FROM cmSkillSetCoverage "
                                            +   " WHERE ssc_sks_skb_id = ? ";

    private static final String SQL_GET = " SELECT ssc_level, ssc_priority, ssc_xml "
                                        + " FROM cmSkillSetCoverage "
                                        + " WHERE ssc_sks_skb_id = ? AND ssc_skb_id = ? ";
    
    private static final String SQL_GET_SKS_SKT_ID="select sks_ske_id from cmSkillSet  where sks_skb_id in  ";



    /**
    Inser a new skill coverage
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_INS);
        int index = 1;
        stmt.setLong(index++, ssc_sks_skb_id);
        stmt.setLong(index++, ssc_skb_id);
        if (this.bAnswered) {
            stmt.setFloat(index++, ssc_level);
        }else {
            stmt.setNull(index++, java.sql.Types.FLOAT);
        }
        if (ssc_priority > 0) {
            stmt.setLong(index++, ssc_priority);
        }else {
            stmt.setNull(index++, java.sql.Types.FLOAT);
        }
        stmt.setString(index++, ssc_xml);
        //stmt.setBoolean(index++, ssc_comment_ind);
        int code = stmt.executeUpdate();
        stmt.close();

        return code;
    }


    /**
    Get detial of the Skill Set Coverage
    */
    public void get(Connection con)
        throws SQLException, cwSysMessage {
            PreparedStatement stmt = con.prepareStatement(SQL_GET);            
            stmt.setLong(1, ssc_sks_skb_id);
            stmt.setLong(2, ssc_skb_id);
            ResultSet rs = stmt.executeQuery();
			String tmp_level = null;
            if( rs.next() ) {
            	tmp_level = rs.getString("ssc_level");
            	if( tmp_level != null ) {
            		bAnswered = true;
            	} else {
            		bAnswered = false;
            	}
            	ssc_level = rs.getFloat("ssc_level");            		
                ssc_priority = rs.getLong("ssc_priority");
                ssc_xml = rs.getString("ssc_xml");
                CommonLog.debug(ssc_level + " : " + ssc_priority + " : " + ssc_xml);
            } 
            stmt.close();
            return;
        }


    /**
    Update ssc_level and ssc_priority of a skill coverage
    @return the row count for UPDATE
    */
    public int upd(Connection con)
        throws SQLException {

        PreparedStatement stmt = con.prepareStatement(SQL_UPD);
        int index = 1;
        if (this.bAnswered) {
            stmt.setFloat(index++, ssc_level);
        }else {
            stmt.setNull(index++, java.sql.Types.FLOAT);
        }
        if (ssc_priority > 0) {
            stmt.setLong(index++, ssc_priority);
        }else {
            stmt.setNull(index++, java.sql.Types.INTEGER);
        }
                    
        stmt.setString(index++, ssc_xml);
        stmt.setLong(index++, ssc_sks_skb_id);
        stmt.setLong(index++, ssc_skb_id);
        int code = stmt.executeUpdate();
        stmt.close();

        return code;
    }

    
    
    /**
     * 不用
    Delete a specified skill from the database
    @return the row count for DELETE    
    */
    public int del(Connection con)
        throws SQLException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_DEL);
        stmt.setLong(1, ssc_sks_skb_id);
        stmt.setLong(2, ssc_skb_id);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }
    
    public String getSksSkeId(Connection con, long[] ids) throws SQLException{
    	  if( ids == null || ids.length == 0 )
              return "";
          
          StringBuffer idsStr = new StringBuffer().append("(");
          for(int i=0; i<ids.length; i++){
        	  if(i!=0){
        		  idsStr.append(COMMA);
        	  }
        	  idsStr.append(ids[i]);        	  
          }
          idsStr.append(")");
          PreparedStatement stmt = con.prepareStatement(SQL_GET_SKS_SKT_ID+idsStr);
          StringBuffer skeIdStr = new StringBuffer().append("(");
    	  ResultSet rs=stmt.executeQuery();
    	  StringBuffer idStr =new StringBuffer();
    	  while(rs.next()){
    		  idStr.append(COMMA).append(rs.getLong("sks_ske_id"));
    	  }
    	  if(idStr.length()>0){
    		  idStr=idStr.deleteCharAt(0);
    	  }
    	  skeIdStr.append(idStr);
    	  skeIdStr.append(")");
    	  if(stmt!=null)stmt.close();
		return skeIdStr.toString() ;
    }

    /**
     * 不用此方法
    Delete a specified skill from the database
    @return the row count for DELETE    
    */
    public int delSkills(Connection con, long[] ids)
        throws SQLException {
            
            if( ids == null || ids.length == 0 )
                return 0;
            
            StringBuffer idsStr = new StringBuffer().append("(0");
            for(int i=0; i<ids.length; i++)
                idsStr.append(COMMA).append(ids[i]);
            idsStr.append(")");
            
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_SKILLS + idsStr);
            stmt.setLong(1, ssc_sks_skb_id);
            int count = stmt.executeUpdate();
            stmt.close();
            return count;
        }
        
    /**
    Delete the skill not int the list
    @param ids long array of the skill base id
    @return the row count of the DELETE
    */
    public int delNotInList(Connection con, long[] ids)
        throws SQLException {
            
            StringBuffer str = new StringBuffer().append("(0");
            for(int i=0; i<ids.length; i++)
                str.append(COMMA).append(ids[i]);
            str.append(")");
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_NOT_IN_LIST + str);
            stmt.setLong(1, ssc_sks_skb_id);
            int count = stmt.executeUpdate();
            stmt.close();
            return count;        
        }
        
        
    /**
    Delete all the skills belong to the skill set specified
    @return the row count for DELETE    
    */
    public int delAllSkills(Connection con, long[] ids)
        throws SQLException {
            
            if( ids == null || ids.length == 0 )
                return 0;
            
            StringBuffer idsStr = new StringBuffer().append("(0");
            for(int i=0; i<ids.length; i++)
                idsStr.append(COMMA).append(ids[i]);
            idsStr.append(")");
            
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_ALL_SKILLS + idsStr.toString());
            int code = stmt.executeUpdate();
            stmt.close();
            return code;
    }


    /**
    Check if a skill exist in any skillset
    @return true if used , false otherwise
    */
    public static boolean checkSkillExist(Connection con, long skill_id)
        throws SQLException, cwException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_CHECK_SKILL_EXIST);
        stmt.setLong(1, skill_id);

        boolean bExist = false;
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            if (rs.getInt(1) > 0) {
                bExist = true;
            }
        }else {
            throw new cwException("Failed to check the usage of a skill.");
        }
        stmt.close();
        
        return bExist;
    }



    /** Check a skill in a specified skill set
    return true if exist , false otherwise
    */

    public boolean exist(Connection con)
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_SKILL_EXIST);
            stmt.setLong(1, ssc_sks_skb_id);
            stmt.setLong(2, ssc_skb_id);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() ) {
                if( rs.getInt(1) > 0 )
                    flag = true;
            } else {
                throw new cwException("Failed to check the existing of a skill.");
            }
            stmt.close();
            return flag;
        }

    /**
    Get the Max. priority of the skill in the skill set
    */
    public long getMaxPriority(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_MAX_PRIORITY);
            stmt.setLong(1, ssc_sks_skb_id);
            ResultSet rs = stmt.executeQuery();            
            long priority = 0;
            if( rs.next() )
                priority = rs.getLong(1);
            
            stmt.close();
            return priority;
        }
        
    /**
    Get skill id which belong to the specified skill set 
    @return vector containg the required skill base id
    */
    public Vector getSkillBaseId(Connection con)
        throws SQLException {
            
            Vector vec = new Vector();
            PreparedStatement stmt = con.prepareStatement(SQL_GET_SKILLS_ID);
            stmt.setLong(1, ssc_sks_skb_id);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() )
                vec.addElement(new Long(rs.getLong("ssc_skb_id")));
            stmt.close();
            return vec;
            
        }

}
