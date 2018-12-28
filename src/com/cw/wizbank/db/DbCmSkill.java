package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.competency.CmSkillSetManager;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.db.view.ViewCmSkillCoverage;

/**
A database class to manage table cmSkill
*/
public class DbCmSkill extends DbCmSkillBase {

    /* Possible values for skl_type */
    //public static final String SKL_TYPE_SKILL = "SKILL";
    //public static final String SKL_TYPE_COMPOSITE_SKILL = "COMPOSITE_SKILL";
    
    public long         skl_skb_id;
    //public String       skl_title;
    //public String       skl_description;
    //public long         skl_ssl_id;
    //public long         skl_parent_skl_skb_id;
    public String       skl_xml;
    public String       skl_type;
    //public String       skl_ancestor;
    public String       skl_derive_rule;
    //public long         skl_owner_ent_id;
    //public String       skl_create_usr_id;
    //public Timestamp    skl_create_timestamp;
    //public String       skl_update_usr_id;
    //public Timestamp    skl_update_timestamp;
    //public String       skl_delete_usr_id;
    //public Timestamp    skl_delete_timestamp;
    public boolean		skl_rating_ind;
    
    
    
    public String       assesseeQ;
    public String       assessorQ;

    
    private static final String SQL_CHECK_HAS_CHILD = " Select count(skl_skb_id) From cmSkill "
                                                    + " Where skl_parent_skl_skb_id = ? "
                                                    + " And skl_delete_timestamp is null ";
    
    

	private final static String SQL_INS_CMSKILL = 
		"Insert Into cmSkill ( " +
		"skl_skb_id, " +
		"skl_type, " +
		"skl_xml, " +
		"skl_derive_rule, " +
		"skl_rating_ind ) " +
		"Values ( ?, ?, ?, ?, ? ) ";

	private static final String SQL_GET_CMSKILL =
		"Select " +
		"skl_skb_id, " +
		"skl_type, " +
		"skl_xml, " +
		"skl_derive_rule, " +
		"skl_rating_ind " +
		"From cmSkill " +
		"Where skl_skb_id = ? ";


	private final static String SQL_UPD_CMSKILL = 
		"Update cmSkill Set " +
		"skl_type = ?, " +
		"skl_xml = ?, " +
		"skl_derive_rule = ?, " +
		"skl_rating_ind = ? " +
		"Where skl_id = ?";

                                  
    private static final String SQL_UPD_PARENT = " UPDATE cmSkill SET "
                                               + "  skl_parent_skl_skb_id = ?, "
                                               + "  skl_update_usr_id = ?, "
                                               + "  skl_update_timestamp = ? "
                                               + " WHERE skl_skb_id = ? ";
                                  
                                                        

	






    /**
    Inser a new skill
    @return the row count for INSERT    
    */
	public int ins(Connection con)
		throws SQLException, cwException, cwSysMessage {
		
			long ske_id=DbCmSkillEntity.ins(con, this.skl_type);
			this.skb_ske_id=ske_id;
			DbCmSkillBase.ins(con, this);	
			PreparedStatement stmt = con.prepareStatement(SQL_INS_CMSKILL);
			
			stmt.setLong(1, this.skb_id);
			
			stmt.setString(2, this.skl_type);
			
			if( this.skl_xml != null ) {
				stmt.setString(3, this.skl_xml);
			} else {
				stmt.setNull(3, java.sql.Types.VARCHAR);
			}
			
			if( this.skl_derive_rule != null ){
				stmt.setString(4, this.skl_derive_rule);
			} else {
				stmt.setNull(4, java.sql.Types.VARCHAR);
			}
			
			stmt.setBoolean(5, this.skl_rating_ind);
			
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ){
				stmt.close();
				throw new cwException("Failed to inset a skill");
			}
			stmt.close();
			
			updAncestor(con);
			
			return count;
		}
	
    /**
    Get all field of the specified skill from the database
    */
    public void get(Connection con)
    	throws SQLException, cwSysMessage {
    		this.skb_id = this.skl_skb_id;
    		this.get(con, false);
    		return;
    	}
    public void get(Connection con, boolean bLoadScale)
    	throws SQLException, cwSysMessage{
			this.skb_id = this.skl_skb_id;
    		super.get(con, bLoadScale);
    		
    		PreparedStatement stmt = con.prepareStatement(SQL_GET_CMSKILL);
    		stmt.setLong(1, this.skb_id);
    		ResultSet rs = stmt.executeQuery();
    		if(rs.next()){
    			this.skl_skb_id = rs.getLong("skl_skb_id");
    			this.skl_type = rs.getString("skl_type");
    			this.skl_xml = rs.getString("skl_xml");
    			this.skl_derive_rule = rs.getString("skl_derive_rule");
    			this.skl_rating_ind = rs.getBoolean("skl_rating_ind");
    		} else {
    			stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill id = " + this.skb_id);	
    		}
    		stmt.close();
    		return;
    	}
    

    /**
    Update a specified skill from the database
    @return the row count for UPDATE    
    */
	public int upd(Connection con)
		throws SQLException, cwSysMessage{
			super.upd(con);
			
			PreparedStatement stmt = con.prepareStatement(SQL_UPD_CMSKILL);
			int index = 1;
			stmt.setString(index++, this.skl_type);
			stmt.setString(index++, this.skl_xml);
			stmt.setString(index++, this.skl_derive_rule);
			stmt.setBoolean(index++, this.skl_rating_ind);
			stmt.setLong(index++, this.skl_skb_id);
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ){
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill id = " + this.skl_skb_id);
			}
 			stmt.close();
 			return count;
		}

    
    
    /**
    Update a specified skill from the database
    @return the row count for UPDATE    
    */
    /*
    public int updParent(Connection con)
        throws SQLException {

        PreparedStatement stmt=null;
        int code=0;
        try {
            //super.upd(con);
            stmt = con.prepareStatement(SQL_UPD_PARENT);
            Timestamp curTime = cwSQL.getTime(con);
            if (skl_parent_skl_skb_id > 0) {
                stmt.setLong(1, skl_parent_skl_skb_id);
            }else {
                stmt.setNull(1, java.sql.Types.NULL);
            }
            stmt.setString(2, skl_update_usr_id);
            stmt.setTimestamp(3, curTime);
            stmt.setLong(4, skl_skb_id);
            
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return code;
    }
    */

    /**
    Delete a specified skill from the database
    @return the row count for DELETE    
    */
    public int del(Connection con, String delete_usr_id)
        throws SQLException, cwSysMessage {
            
        this.skb_delete_usr_id = delete_usr_id;
        int count = super.softDel(con);        
        return count;
        
    }

	public int delChild(Connection con, String delete_usr_id)
		throws SQLException, cwSysMessage {

			this.skb_delete_usr_id = delete_usr_id;
			int count = super.softDelChild(con); 
			return count;

		}


    public String asXML()
    {
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<skill id=\"").append(this.skb_id).append("\" scale_id=\"")
              .append(this.skb_ssl_id).append("\" parent_id=\"").append(this.skb_parent_skb_id)
              .append("\" owner_ent_id=\"").append(this.skb_owner_ent_id).append("\">").append(cwUtils.NEWL)
              .append("<title>").append(cwUtils.esc4XML(this.skb_title)).append("</title>").append(cwUtils.NEWL)
              .append("<desc>").append(cwUtils.esc4XML(this.skb_description)).append("</desc>").append(cwUtils.NEWL)
              .append("<create usr_id=\"").append(this.skb_create_usr_id)
              .append("\" timestamp=\"").append(this.skb_create_timestamp).append("\"/>").append(cwUtils.NEWL)
              .append("<update usr_id=\"").append(this.skb_update_usr_id)
              .append("\" timestamp=\"").append(this.skb_update_timestamp).append("\"/>").append(cwUtils.NEWL)
              .append(this.skl_xml).append(cwUtils.NEWL)
              .append("</skill>").append(cwUtils.NEWL);
        return xmlBuf.toString();
    }





    public String formatSkillXML()
    {
        if (assesseeQ == null)
            assesseeQ = new String();
        if (assessorQ == null)
            assessorQ = new String();
            
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<body>").append(cwUtils.NEWL)
              .append("<question>").append(cwUtils.NEWL)
              .append("<assessee>").append(cwUtils.esc4XML(assesseeQ)).append("</assessee>").append(cwUtils.NEWL)
              .append("<assessor>").append(cwUtils.esc4XML(assessorQ)).append("</assessor>").append(cwUtils.NEWL)
              .append("</question>").append(cwUtils.NEWL)
              .append("</body>").append(cwUtils.NEWL);
 
        return xmlBuf.toString();
    }
    
        
        


        



    
    public Vector getSkillChild(Connection con)
    	throws SQLException {
    		
    		Vector v_skill = new Vector();
    		StringBuffer SQLBuf = new StringBuffer(SqlStatements.SQL_CM_SKILL_GET_DETAIL);
    		if( this.skb_id > 0 ){
				SQLBuf.append(" And skb_parent_skb_id = ? ");    		
    		} else {
    			SQLBuf.append(" And skb_parent_skb_id Is Null ");
    		}
			SQLBuf.append(" And skb_delete_usr_id Is Null ");
    		SQLBuf.append(" Order By skb_order, skb_title ASC ");
    		PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
    		if( this.skb_id > 0 ){
    			stmt.setLong(1, this.skb_id);
    		}
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next()){
						
    			DbCmSkill child = new DbCmSkill();
    			child.skb_id = rs.getLong("skb_id");
                child.skl_skb_id = child.skb_id;
    			child.skb_title = rs.getString("skb_title");
    			child.skb_description = rs.getString("skb_description");
    			child.skb_ssl_id = rs.getLong("skb_ssl_id");
    			child.skb_parent_skb_id = rs.getLong("skb_parent_skb_id");
    			child.skl_xml = rs.getString("skl_xml");
    			child.skb_type = rs.getString("skb_type");
    			child.skl_type = child.skb_type;
    			child.skb_ancestor = rs.getString("skb_ancestor");
    			child.skl_derive_rule = rs.getString("skl_derive_rule");
    			child.skb_owner_ent_id = rs.getLong("skb_owner_ent_id");
    			child.skb_create_usr_id = rs.getString("skb_create_usr_id");
    			child.skb_create_timestamp = rs.getTimestamp("skb_create_timestamp");
    			child.skb_update_usr_id = rs.getString("skb_update_usr_id");
    			child.skb_update_timestamp = rs.getTimestamp("skb_update_timestamp");
    			child.skl_rating_ind = rs.getBoolean("skl_rating_ind");
    			v_skill.addElement(child);
    		}
    		stmt.close();
    		return v_skill;
    	}
    
    
	public static Vector getSkillFromList(Connection con, long[] skb_id_list)
		throws SQLException {

			Vector v_skill = new Vector();
			if( skb_id_list != null && skb_id_list.length > 0 ) {
				StringBuffer SQLBuf = new StringBuffer(SqlStatements.SQL_CM_SKILL_GET_DETAIL);
				SQLBuf.append(" And skb_id In ")
					  .append(cwUtils.array2list(skb_id_list))
					  .append(" Order By skb_type, skb_title ");
				PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					DbCmSkill child = new DbCmSkill();
					child.skb_id = rs.getLong("skb_id");
					child.skb_title = rs.getString("skb_title");
					child.skb_description = rs.getString("skb_description");
					child.skb_ssl_id = rs.getLong("skb_ssl_id");
					child.skb_parent_skb_id = rs.getLong("skb_parent_skb_id");
					child.skl_xml = rs.getString("skl_xml");
					child.skb_type = rs.getString("skb_type");
					child.skl_type = child.skb_type;
					child.skb_ancestor = rs.getString("skb_ancestor");
					child.skl_derive_rule = rs.getString("skl_dervie_rule");
					child.skb_owner_ent_id = rs.getLong("skb_owner_ent_id");
					child.skb_create_usr_id = rs.getString("skb_create_usr_id");
					child.skb_create_timestamp = rs.getTimestamp("skb_create_timestamp");
					child.skb_update_usr_id = rs.getString("skb_update_usr_id");
					child.skb_update_timestamp = rs.getTimestamp("skb_update_timestamp");
					child.skb_delete_usr_id = rs.getString("skb_delete_usr_id");
					child.skb_delete_timestamp = rs.getTimestamp("skb_delete_timestamp");
					child.skl_rating_ind = rs.getBoolean("skl_rating_ind");
					v_skill.addElement(child);
				}
				stmt.close();
			}
			return v_skill;
		}


    


	public void updAncestor(Connection con) throws SQLException, cwException, cwSysMessage {

		if ( this.skb_parent_skb_id <= 0 ){
			
			// it's a top level skill
			this.skb_ancestor = null;
			
		}else{
			
			String parent_ancestor = DbCmSkillBase.getAncestor(con, this.skb_parent_skb_id);            
			if( parent_ancestor == null || parent_ancestor.length() == 0 ){
				this.skb_ancestor = " " + this.skb_parent_skb_id + " ";
			} else {
				this.skb_ancestor = parent_ancestor + "," + " " + this.skb_parent_skb_id + " ";
			}
			DbCmSkillBase.updAncestor(con, this.skb_id, this.skb_ancestor);
		}
        return;
	}


    

    /**
    Check if this skill has any child
    Pre-define variable: skb_id
    */
    public boolean hasChild(Connection con) 
    	throws SQLException {
    	
	    	Hashtable h_skb_id_status = DbCmSkillBase.getChildNStatusHash(con, this.skb_id);
	    	Enumeration enumeration = h_skb_id_status.keys();
	    	while(enumeration.hasMoreElements()){
	    		if( ((Boolean)h_skb_id_status.get((Long)enumeration.nextElement())).booleanValue() ){
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	
	
	public String XML(){
		StringBuffer xml = new StringBuffer();
		xml.append("<competency_skill ")
			.append(" skb_id=\"").append(this.skb_id).append("\" ")
			.append(" skb_create_timestamp=\"").append(this.skb_create_timestamp).append("\" ")
			.append(" skb_create_usr_id=\"").append(this.skb_create_usr_id).append("\" ")
			.append(" skb_delete_timestamp=\"").append(this.skb_delete_timestamp).append("\" ")
			.append(" skb_delete_usr_id=\"").append(this.skb_delete_usr_id).append("\" ")
			.append(" skb_order=\"").append(this.skb_order).append("\" ")
			.append(" skb_owner_ent_id=\"").append(this.skb_owner_ent_id).append("\" ")
			.append(" skb_parent_skb_id=\"").append(this.skb_parent_skb_id).append("\" ")
			.append(" skb_ssl_id=\"").append(this.skb_ssl_id).append("\" ")
			.append(" skb_update_timestamp=\"").append(this.skb_update_timestamp).append("\" ")
			.append(" skb_update_usr_id=\"").append(this.skb_update_usr_id).append("\" ")
			.append(" skl_rating_ind=\"").append(this.skl_rating_ind).append("\" ")
			.append(">")
			.append("<skb_title>").append(cwUtils.esc4XML(this.skb_title)).append("</skb_title>")
			.append("<skb_description>").append(cwUtils.esc4XML(this.skb_description)).append("</skb_description>")
			.append("<skl_derive_rule>").append(cwUtils.esc4XML(this.skl_derive_rule)).append("</skl_derive_rule>");
			if( this.skl_xml != null && this.skl_xml.length() > 0 ){
				xml.append(this.skl_xml);
			}
			if( this.cmScale != null ) {
				xml.append("<scale id=\"").append(this.cmScale.ssl_id).append("\">")
					.append("<ssl_title>").append(cwUtils.esc4XML(this.cmScale.ssl_title)).append("</ssl_title>")
					.append("</scale>");				
			}
		xml.append("</competency_skill>");
		return xml.toString();
	}
	
	
			
			
	public void updCompetency(Connection con)
		throws SQLException, cwSysMessage {
			
			if( this.skb_update_timestamp == null ){
				this.skb_update_timestamp = cwSQL.getTime(con);
			}
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_UPD_COMPETENCY);
			int index = 1;
			stmt.setString(index++, this.skb_title);
			stmt.setString(index++, this.skb_description);
			stmt.setString(index++, this.skb_update_usr_id);
			stmt.setTimestamp(index++, this.skb_update_timestamp);
			stmt.setLong(index++, this.skb_id);
			if( stmt.executeUpdate() != 1 ){
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill id = " + this.skb_id);
			}
			index = 1;
			stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_UPD_RATING_IND);
			stmt.setBoolean(index++, this.skl_rating_ind);
			stmt.setLong(index++, this.skb_id);
			if( stmt.executeUpdate() != 1 ) {
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill id = " + this.skb_id);
			}			
			stmt.close();
			return;
		}
	
	
	public void updBehavior(Connection con)
		throws SQLException, cwSysMessage {
			if( this.skb_update_timestamp == null ){
				this.skb_update_timestamp = cwSQL.getTime(con);
			}
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_BASE_UPD_BEHAVIOR);
			int index = 1;
			stmt.setString(index++, this.skb_title);
			stmt.setString(index++, this.skb_description);
			stmt.setLong(index++, this.skb_order);
			stmt.setString(index++, this.skb_update_usr_id);
			stmt.setTimestamp(index++, this.skb_update_timestamp);
			stmt.setLong(index++, this.skb_id);
			if( stmt.executeUpdate() != 1 ){
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill id = " + this.skb_id);
			}
			stmt.close();
			
			index = 1;
			stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_UPD_RATING_IND);
			stmt.setBoolean(index++, this.skl_rating_ind);
			stmt.setLong(index++, this.skb_id);
			if( stmt.executeUpdate() != 1 ) {
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill id = " + this.skb_id);
			}
			stmt.close();
			return;
		}
	

	
	
}

