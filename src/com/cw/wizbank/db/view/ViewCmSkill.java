package com.cw.wizbank.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.db.DbCmSkill;
import com.cw.wizbank.db.DbCmSkillLevel;
import com.cw.wizbank.db.DbCmSkillScale;
import com.cw.wizbank.db.DbCmSkillSet;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
ViewCmSkill is an class to deal with skill management
*/
public class ViewCmSkill {
    
    //public DbCmSkillTreeNode node       = null;
    public DbCmSkill skill              = null;
    public DbCmSkillScale skillScale    = null;
    public Vector skillLevelVec         = null;
    
    public static class ViewItemSkill {
        public long skb_id;
        public String skb_title;
        public long itc_skl_level;
    }

    private static final String SQL_GET_ALL_SHARED_SCALE = 
              " SELECT ssl_id,  count(sle_level) level_count,  ssl_title, "
            + "    ssl_update_timestamp FROM cmSkillScale, cmSkillLevel "
            + " WHERE ssl_owner_ent_id = ? "
            + "    AND ssl_share_ind = ? "
            + "    AND sle_ssl_id = ssl_id "
            + "    AND ssl_delete_timestamp is null ";

    private static final String SQL_CHECK_SHARED_SCALE_BY_SKILL = 
              " SELECT ssl_share_ind "
            + "    FROM cmSkillScale, cmSkillBase "
            + " WHERE skb_ssl_id = ssl_id "
            + "    AND skb_id = ? ";

    
    private static final String SQL_SIMPLE_SKILL_SEARCH =
        " SELECT DISTINCT skb_id, skb_title, skb_description, skb_type, skb_create_usr_id, skb_create_timestamp, skb_update_usr_id, skb_update_timestamp "
      + " FROM cmSkillBase, cmSkillLevel "
      + " WHERE "
      + " skb_ssl_id = sle_ssl_id "
      + " AND skb_owner_ent_id = ? "
 	  + " AND ( lower(skb_title) like ? OR lower(skb_description) like ? OR lower(sle_label) like ? OR lower(sle_description) like ? ) "
      + " AND skb_delete_timestamp IS NULL "//filter the deleted skill
      + " ORDER BY skb_type, skb_title ";

    private static final String SQL_SIMPLE_SCALE_SEARCH = 
       " SELECT DISTINCT ssl_id, ssl_title, ssl_share_ind, TOTAL "
    +  " FROM cmSkillScale, cmSkillLevel , "
    +  " ( SELECT sle_ssl_id, COUNT(sle_ssl_id) TOTAL "
    +  "   FROM cmSkillLevel GROUP BY sle_ssl_id ) skillLevel "
    +  " WHERE ssl_id = skillLevel.sle_ssl_id AND cmSkillLevel.sle_ssl_id = skillLevel.sle_ssl_id "
    +  " AND ssl_owner_ent_id = ? "
	+  " AND ( lower(ssl_title) like ? OR lower(cmSkillLevel.sle_level) like ? OR lower(cmSkillLevel.sle_label) like ? ) "
    +  " ORDER BY ";
     
     

    private static String SQL_GET_BY_ID_VEC =
      " SELECT skb_id, skb_title, skb_description, max_level "
    + " FROM cmSkillBase, ( SELECT sle_ssl_id, max(sle_level) max_level "
    + "                 FROM cmSkillLevel GROUP BY sle_ssl_id ) skillLevel "
    + " WHERE skb_ssl_id = skillLevel.sle_ssl_id AND skb_id IN ";
    
    
    private static String SQL_GET_ITEM_SKILLS = 
      " SELECT skb_id, skb_title, itc_skl_level "
    + " FROM cmSkillBase, cmSkill, aeItemCompetency "
    + " WHERE itc_itm_id = ? "
    + " AND skb_id = skl_skb_id "
    + " AND skl_skb_id = itc_skl_skb_id "
    + " ORDER BY ";


    /**
    Get all details information of a skill
    @param skl_id id of the skill
    @return a Resultset containing all the necessary field
    */
    public void getSkill(Connection con, long skl_id) 
        throws SQLException , cwSysMessage {
        
        skill = new DbCmSkill();
        skill.skl_skb_id = skl_id;
        skill.skb_id = skl_id;
        skill.get(con);
    }

    /**
    Get all details information of a skill
    @param scale_id id of the skill
    */
    public void getScale(Connection con, long scale_id) 
        throws SQLException , cwSysMessage {
        skillScale = new DbCmSkillScale();
        skillScale.ssl_id = scale_id;
        skillScale.get(con);
        
        DbCmSkillLevel skillLevel = new DbCmSkillLevel();
        skillLevel.sle_ssl_id = scale_id;
        skillLevelVec = skillLevel.getById(con);
    }

    /*
    Get the child of a node
    @param parent_id node id of the skill tree node
    @param owner_ent_id entity id of the organization
    @return a Vector of child nodes

    public Vector getChildNodes(Connection con, long parent_id, long owner_ent_id) 
        throws SQLException, cwException {
        
        StringBuffer SQLBuf = new StringBuffer();
        Vector nodeVec = new Vector();
            
        SQLBuf.append(SQL_GET_NODES);
        
        // a parent is given
        if (parent_id > 0) {
                  SQLBuf.append("   AND stn_stn_skb_id_parent = ").append(parent_id);
        // root node
        }else {
                  SQLBuf.append("   AND stn_stn_skb_id_parent is null ");
        }
        
        SQLBuf.append(" ORDER BY stn_title ASC ");
            
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, owner_ent_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            DbCmSkillTreeNode node = new DbCmSkillTreeNode();
            node.stn_skb_id = rs.getLong("stn_skb_id");
            node.stn_stn_skb_id_parent = rs.getLong("stn_stn_skb_id_parent");
            node.stn_title = rs.getString("stn_title");
            node.skb_owner_ent_id = rs.getLong("skb_owner_ent_id");
            node.skb_create_usr_id = rs.getString("skb_create_usr_id");
            node.skb_create_timestamp = rs.getTimestamp("skb_create_timestamp");
            node.skb_update_usr_id = rs.getString("skb_update_usr_id");
            node.skb_update_timestamp = rs.getTimestamp("skb_update_timestamp");
            nodeVec.addElement(node);
        }
        stmt.close();
        return nodeVec;
    }
    */
    
    
    /**
    Get the information of a an scale
    @return xml of the scale
    */
    public String getScaleAsXML(Connection con, long scale_id) 
        throws SQLException, cwSysMessage, cwException {
        
        getScale(con, scale_id);
        
        boolean isUsed = DbCmSkill.isUsedScale(con, scale_id);
        StringBuffer xmlBuf = new StringBuffer(1024);
        
        xmlBuf.append("<scale id=\"").append(skillScale.ssl_id)
              .append("\" shared=\"").append(skillScale.ssl_share_ind)
              .append("\" used=\"").append(isUsed).append("\">").append(cwUtils.NEWL);
        xmlBuf.append("<title>").append(cwUtils.esc4XML(skillScale.ssl_title)).append("</title>").append(cwUtils.NEWL);
        xmlBuf.append("<create usr_id=\"").append(skillScale.ssl_create_usr_id).append("\" timestamp=\"")
                .append(skillScale.ssl_create_timestamp).append("\"/>").append(cwUtils.NEWL);
        xmlBuf.append("<update usr_id=\"").append(skillScale.ssl_update_usr_id).append("\" timestamp=\"")
                .append(skillScale.ssl_update_timestamp).append("\"/>").append(cwUtils.NEWL);
        xmlBuf.append("<levels>").append(cwUtils.NEWL);
        for(int i=0; i < skillLevelVec.size(); i++) {
            DbCmSkillLevel sl = (DbCmSkillLevel) skillLevelVec.elementAt(i);
            xmlBuf.append(sl.asXML());
        }
        xmlBuf.append("</levels>").append(cwUtils.NEWL);
        xmlBuf.append("</scale>").append(cwUtils.NEWL);
        
        return xmlBuf.toString();
        
    }

    /**
    Get the information of a skill
    @return xml of the skill
    */
    public String getSkillAsXML(Connection con, long skill_id, long scale_id) 
        throws SQLException, cwSysMessage, cwException {

        getSkill(con, skill_id);
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<skill id=\"").append(skill.skl_skb_id).append("\"")
              .append(" type=\"").append(cwUtils.escNull(skill.skb_type)).append("\"")
              .append(">").append(cwUtils.NEWL);
        xmlBuf.append("<title>").append(cwUtils.esc4XML(skill.skb_title)).append("</title>").append(cwUtils.NEWL);
        xmlBuf.append("<desc>").append(cwUtils.esc4XML(skill.skb_description)).append("</desc>").append(cwUtils.NEWL);
        xmlBuf.append("<create usr_id=\"").append(skill.skb_create_usr_id).append("\" timestamp=\"")
                .append(skill.skb_create_timestamp).append("\"/>").append(cwUtils.NEWL);
        xmlBuf.append("<update usr_id=\"").append(skill.skb_update_usr_id).append("\" timestamp=\"")
                .append(skill.skb_update_timestamp).append("\"/>").append(cwUtils.NEWL);

        Long l_ancestor;
        String ancestor_title;
        Hashtable htAncestor = skill.getAncestorTitle(con);
        xmlBuf.append("<nav>").append(cwUtils.NEWL);
        if (skill.skb_ancestor!=null && skill.skb_ancestor.length()>0){
                long[] ancestors = cwUtils.splitToLong(skill.skb_ancestor.trim(), " , ");
                for (int i=0; i<ancestors.length; i++){
                    l_ancestor = new Long(ancestors[i]);
                    ancestor_title = (String)htAncestor.get(l_ancestor);
                    xmlBuf.append("<skill id=\"").append(l_ancestor).append("\">").append(cwUtils.NEWL);
                    xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(ancestor_title))).append("</title>").append(cwUtils.NEWL);
                    xmlBuf.append("</skill>").append(cwUtils.NEWL);
                }
        }
        xmlBuf.append("</nav>").append(cwUtils.NEWL);

        if( scale_id > 0 ) {
            xmlBuf.append(getScaleAsXML(con, scale_id));
        }
        else if (skill.skb_ssl_id > 0) {
            xmlBuf.append(getScaleAsXML(con, skill.skb_ssl_id));
        }
        xmlBuf.append("</skill>").append(cwUtils.NEWL);

        return xmlBuf.toString();
        
    }
   
   
    /**
    Insert a new scale <br>
    Preset object(s) : skillScale, skillLevelVec
    */
    public void insScale(Connection con, String usr_id, long owner_ent_id)
        throws SQLException, cwSysMessage {

        Timestamp curTime = cwSQL.getTime(con);

        skillScale.ssl_owner_ent_id = owner_ent_id;
        skillScale.ssl_create_usr_id = usr_id;
        skillScale.ssl_create_timestamp = curTime;
        skillScale.ssl_update_usr_id = usr_id;
        skillScale.ssl_update_timestamp = curTime;
        
        skillScale.ins(con);
        
        for (int i=0;i<skillLevelVec.size();i++) {
            DbCmSkillLevel skillLevel = (DbCmSkillLevel) skillLevelVec.elementAt(i);
            skillLevel.sle_ssl_id = skillScale.ssl_id;
            skillLevel.ins(con);
        }
    }

    /**
    Delete a scale<BR>
    Preset object(s) : skillScale
    */
    public void delScale(Connection con)
        throws SQLException, cwSysMessage {
        
        // Delete all levels 
        DbCmSkillLevel skillLevel = new DbCmSkillLevel();
        skillLevel.sle_ssl_id = skillScale.ssl_id;
        skillLevel.delAllLevel(con);
        
        // Delete the scale
        skillScale.del(con);

    }

    /**
    Update a new scale<BR>
    Preset object(s) : skillScale, skillLevelVec
    */
    public void updScale(Connection con, String usr_id)
        throws SQLException, cwSysMessage {

        // Delete all levels 
        DbCmSkillLevel skillLevel = new DbCmSkillLevel();
        skillLevel.sle_ssl_id = skillScale.ssl_id;
        skillLevel.delAllLevel(con);

        // Update skill scale
        Timestamp curTime = cwSQL.getTime(con);
        skillScale.ssl_update_usr_id = usr_id;
        skillScale.ssl_update_timestamp = curTime;
        skillScale.upd(con);

        // Insert the new  levels
        for (int i=0;i<skillLevelVec.size();i++) {
            skillLevel = (DbCmSkillLevel) skillLevelVec.elementAt(i);

            skillLevel.sle_ssl_id = skillScale.ssl_id;
            skillLevel.ins(con);
        }
    }

    /**
    Update a new scale without modifying the level <BR>
    Preset object(s) : skillScale, skillLevelVec
    */
    public void updScaleByLevel(Connection con, String usr_id)
        throws SQLException, cwSysMessage {

        // Update skill scale
        Timestamp curTime = cwSQL.getTime(con);
        skillScale.ssl_update_usr_id = usr_id;
        skillScale.ssl_update_timestamp = curTime;
        skillScale.upd(con);
        
        // Update the level
        for (int i=0;i<skillLevelVec.size();i++) {
            DbCmSkillLevel skillLevel = (DbCmSkillLevel) skillLevelVec.elementAt(i);
            skillLevel.sle_ssl_id = skillScale.ssl_id;
            skillLevel.upd(con);
        }
    }

    /**
    Insert a new skill <BR>
    Preset object(s) : skill
    */
    public void insSkill(Connection con, String usr_id, long owner_ent_id)
        throws SQLException, cwSysMessage, cwException {

        // Insert the skill
        Timestamp curTime = cwSQL.getTime(con);
        skill.skb_owner_ent_id = owner_ent_id;
        skill.skb_create_usr_id = usr_id;
        skill.skb_create_timestamp = curTime;
        skill.skb_update_usr_id = usr_id;
        skill.skb_update_timestamp = curTime;

        skill.ins(con);
        return;
    }

    /**
    Update skill
    */
    public void updSkill(Connection con, String usr_id, long owner_ent_id) 
        throws SQLException, cwSysMessage, cwException {
        
        Timestamp curTime = cwSQL.getTime(con);
        //update the skill        
        skill.skb_update_usr_id = usr_id;
        skill.skb_update_timestamp = curTime;
        skill.upd(con);
        return;
    }
    
    /**
    Check if a node is empty
    @param node_id node id of the skill tree node
    @return boolean true if empty , false otherwise
    */
    public static boolean isEmptyNode(Connection con, long node_id) 
        throws SQLException, cwException {

        boolean isEmpty = true;
        StringBuffer SQLBuf = new StringBuffer();
            
        SQLBuf.append(" SELECT COUNT(*) FROM cmSkillTreeNode WHERE ")
              .append(" stn_stn_skb_id_parent = ? ");

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, node_id);
            rs = stmt.executeQuery();

        if (rs.next()) {
            if (rs.getInt(1) > 0) {
                isEmpty = false;
            }
        }else {
            throw new cwException("Failed to check the child of a node.");
        }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        if (isEmpty) {
            SQLBuf.setLength(0);
            SQLBuf.append(" SELECT COUNT(*) FROM cmSkill WHERE ")
                .append(" skl_stn_skb_id = ? ");
            try {
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, node_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    isEmpty = false;
                }
            }else {
                throw new cwException("Failed to check the child of a node.");
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        
        return isEmpty;
        
    }

    /**
    Check if a skill is using share scale or not
    @param skill_id id of the skill
    @return boolean true if using share scale , false otherwise
    */
    public static boolean isUsingSharedScale(Connection con, long skill_id) 
        throws SQLException, cwException {

        boolean useShare = true;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL_CHECK_SHARED_SCALE_BY_SKILL);
        stmt.setLong(1, skill_id);
            rs = stmt.executeQuery();

        if (rs.next()) {
            useShare = rs.getBoolean("ssl_share_ind");
        }else {
            throw new cwException("Failed to check if the skill is using standard scale.");
        }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return useShare;
        
    }


    /**
    Get all the shared skill scale from belonging to an organization
    @return a Vector of scales
    */
    public ResultSet getSharedScale(Connection con, long owner_ent_id, String order_by, String sort_by)
            throws SQLException {
        return getSharedScale(con, owner_ent_id, order_by, sort_by, null);
    }                

    /**
    Get all the shared skill scale from belonging to an organization
    @param matchIdVec vector storing the scale id criteria
    @return a Vector of scales
    */
    public ResultSet getSharedScale(Connection con, long owner_ent_id, String order_by, String sort_by, Vector matchIdVec)
        throws SQLException {
            
        Vector scaleVec = new Vector();
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(SQL_GET_ALL_SHARED_SCALE);
        
        if (matchIdVec !=null && matchIdVec.size() > 0) {
            SQLBuf.append(" AND ssl_id IN ").append(cwUtils.vector2list(matchIdVec));
        }
        
        SQLBuf.append(" GROUP BY ssl_id, ssl_title, ssl_update_timestamp ");
        if (order_by == null)
            order_by = " ssl_title ";
        
        SQLBuf.append(" ORDER BY ").append(order_by).append(" ").append(sort_by);
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, owner_ent_id);
        stmt.setBoolean(2, true);
        
        
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /*
    public static void do_id_test(Connection con) 
        throws SQLException, cwException {
            PreparedStatement stmt = con.prepareStatement(
                " Insert into testabc (abc_name) values (?) "
                + " Select max(abc_id) from testabc ");

            for (int i=0;i <2 ; i++) {

            stmt.setString(1, "" + System.currentTimeMillis());
            stmt.execute();

            if (stmt.getUpdateCount() == 1) {
                stmt.getMoreResults();
                ResultSet rs = stmt.getResultSet();
                rs.next();
                System.out.println(rs.getLong(1));
            }
        }
        /*
        CallableStatement cs = con.prepareCall(
            "begin insert into tableabc (abc_name) values ('abc') "
            + " select abc_id into ?; end; ");

        cs.registerOutParameter( 1, Types.INTEGER );
        int num = cs.executeUpdate();
        int returned = cs.getInt( 1 );
        System.out.println("returned = " + returned);
        ///
        
     }            
    */
    
    
    /**
    Get Skill Set(Skill Profile) As XML
    */
    //Lun : Assume skill set contains 1 level ??
    public String getSkillSetAsXML(Connection con, long sks_skb_id)
        throws SQLException, cwSysMessage {
            
            StringBuffer xml = new StringBuffer();
            
            DbCmSkillSet Ss = new DbCmSkillSet();
            Ss.sks_skb_id = sks_skb_id;
            ResultSet rs = Ss.getSkillSet(con);
            if( !rs.next() )
                throw new cwSysMessage("Failed to get skill set id = " + sks_skb_id );
            
            xml.append("<skill_set id=\"").append(rs.getLong("sks_skb_id")).append("\" ")
               .append(" last_upd_timestamp=\"").append(rs.getTimestamp("sks_update_timestamp")).append("\" ")
               .append(" type=\"").append(rs.getString("sks_type")).append("\" ")
               .append(" title=\"").append(cwUtils.esc4XML(rs.getString("sks_title"))).append("\">")
               .append(cwUtils.NEWL);

            ViewCmSkillCoverage Vsc = new ViewCmSkillCoverage();
            rs = Vsc.getSkillCoverage(con, sks_skb_id);                        

            DbCmSkill skill = new DbCmSkill();
            DbCmSkillLevel skillLevel = new DbCmSkillLevel();
            
            // loop the result set to get the skill id for searching the skill path            
            Vector skbIdVec = new Vector();
            Hashtable skillPath = null;
            while( rs.next() )
                skbIdVec.addElement(new Long(rs.getLong("ssc_skb_id")));
            if( !skbIdVec.isEmpty() ) 
                skillPath = getSkillPath(con, skbIdVec);
            rs.close();
            rs = Vsc.getSkillCoverage(con, sks_skb_id);  
//            rs.beforeFirst();
            
            
            while( rs.next() ) {
                xml.append("<set_coverage level=\"")
                   .append(rs.getFloat("ssc_level")).append("\" ")
                   .append(" priority=\"").append(rs.getLong("ssc_priority")).append("\">");
                skill.skl_skb_id = rs.getLong("ssc_skb_id");
                skill.skb_id = skill.skl_skb_id;
                skill.get(con);
                if( skillPath != null )
                    skill.skb_title = cwUtils.esc4XML(getSkillPathTitle(skillPath, skill.skl_skb_id) + skill.skb_title);
                xml.append(skill.asXML());
                skillLevel.sle_ssl_id = rs.getLong("ssl_id");
                Vector vec = skillLevel.getById(con);

                StringBuffer temp = new StringBuffer(512);
                long max_level = 0;
                for(int i=0; i<vec.size(); i++) {
                    DbCmSkillLevel level = (DbCmSkillLevel)vec.elementAt(i);
                    temp.append(level.asXML());
                    if(max_level < level.sle_level) {
                        max_level = level.sle_level;
                    }
                }

                xml.append("<scale max_level=\"").append(max_level).append("\" >")
                   .append(cwUtils.NEWL)
                   .append(temp.toString());


                xml.append("</scale>").append(cwUtils.NEWL);
                xml.append("</set_coverage>").append(cwUtils.NEWL);
            }
            
            xml.append("</skill_set>").append(cwUtils.NEWL);
            rs.close();
            return xml.toString();
        }
    
    
    /**
    Get skill and max. level 
    @return resultset
    */
    public static ResultSet getSkillsLevel(Connection con, Vector idVec)
        throws SQLException, cwException {
            
            StringBuffer SQL = new StringBuffer();
			SQL.append(OuterJoinSqlStatements.viewCmSkillGetSkillsLevel());
//            SQL.append(" SELECT skb_id, skb_title, ")
//               .append(" skb_description, max(sle_level)total, skb_type ")
//               .append(" FROM cmSkillBase, cmSkillLevel ")
//               .append(" WHERE sle_ssl_id ").append(cwSQL.get_right_join(con)).append(" skb_ssl_id ");

            if( idVec != null && idVec.size() > 0 )
                SQL.append(" AND skb_id IN ").append(cwUtils.vector2list(idVec));
           
            SQL.append(" GROUP BY skb_id, skb_title, ")
               .append(" skb_description, sle_ssl_id, skb_type ")
               .append(" ORDER BY skb_title ");

            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            ResultSet rs = stmt.executeQuery();
            return rs;
            
        }
    
    /**
    Simple search
    Search skill title, skill description, skill level label , skill level description containing specified text
    @return Result Set of Skill Info
    */
    public static ResultSet simpleSkillSearch(Connection con, long root_id, String sort_by, String search_text)
        throws SQLException {
            
            String likeText = "%" + search_text + "%";
            PreparedStatement stmt = con.prepareStatement(SQL_SIMPLE_SKILL_SEARCH + sort_by);
            stmt.setLong(1, root_id);
			stmt.setString(2, likeText.toLowerCase());
			stmt.setString(3, likeText.toLowerCase());
			stmt.setString(4, likeText.toLowerCase());
			stmt.setString(5, likeText.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs;
            
        }
        
    /**
    Advance Search    
    @return ResultSet of skill info
    */
    public static ResultSet advSkillSearch(Connection con, long root_id, String sort_by, String skb_title,
                                           String skb_description, String sle_label, String sle_description,
                                           int sle_level)
        throws SQLException{
            
            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT DISTINCT skb_id, skb_title, skb_description, skb_type, ")
               .append(" skb_create_usr_id, skb_create_timestamp, ")
               .append(" skb_update_usr_id, skb_update_timestamp ")
               .append(" FROM cmSkillBase, cmSkillLevel ")
               .append(" WHERE skb_ssl_id = sle_ssl_id ")
               .append(" AND skb_owner_ent_id = ? ")
               .append(" AND skb_delete_timestamp IS NULL ");//filter the deleted skill
               
            if( skb_title != null && skb_title.length() > 0 )
				SQL.append(" AND lower(skb_title) like ? ");
            if( skb_description != null && skb_description.length() > 0 )
                SQL.append(" AND lower(skb_description) like ? ");
            if( sle_label != null && sle_label.length() > 0 )
                SQL.append(" AND lower(sle_label) like ? ");
            if( sle_description != null && sle_description.length() > 0 )
                SQL.append(" AND lower(sle_description) like ? ");

            if( sle_level != Integer.MIN_VALUE ){
                SQL.append(" AND ( SELECT COUNT(sle_level) ")
                   .append(" FROM cmSkillLevel where sle_ssl_id = skb_ssl_id ")
                   .append(" GROUP BY sle_ssl_id ) = ? ");
            }
            SQL.append(" ORDER BY skb_type, skb_title ").append(sort_by);
            
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            int index = 1;
            stmt.setLong(index++, root_id);
            if( skb_title != null && skb_title.length() > 0 )
                stmt.setString(index++, "%" + skb_title.toLowerCase() + "%");
                
            if( skb_description != null && skb_description.length() > 0 )
                stmt.setString(index++, "%" + skb_description.toLowerCase() + "%");
                
            if( sle_label != null && sle_label.length() > 0 )
                stmt.setString(index++, "%" + sle_label.toLowerCase() + "%");
                
            if( sle_description != null && sle_description.length() > 0 )
                stmt.setString(index++, "%" + sle_description.toLowerCase() + "%");
                
            if( sle_level != Integer.MIN_VALUE )
                stmt.setInt(index++, sle_level);

            ResultSet rs = stmt.executeQuery();
            return rs;
        }
        
 
    /**
    Get skill scale by specified scale id
    @param return skill scale
    */
    public static ResultSet getScaleByIds(Connection con, String sort_by, String order_by, Vector selectedIdVec)
        throws SQLException {
            
            String SQL = " SELECT ssl_id, ssl_title, ssl_share_ind, TOTAL "
                       + " FROM cmSkillScale, "
                       + " ( SELECT sle_ssl_id, COUNT(sle_ssl_id) TOTAL "
                       + "   FROM cmSkillLevel WHERE sle_ssl_id IN " + cwUtils.vector2list(selectedIdVec)
                       + "   GROUP BY sle_ssl_id ) skillLevel "
                       + " WHERE ssl_id = sle_ssl_id AND ssl_id IN " + cwUtils.vector2list(selectedIdVec)
                       + " ORDER BY " + order_by + " " + sort_by;
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            return rs;            
        }
 
 
    /**
    Simple Search
    Search Scale title, skill level label , skill description containing specifie text
    @return Result set of the skill scale
    */
    public static ResultSet simpleScaleSearch(Connection con, String sort_by, String order_by, long root_id, String search_text)
        throws SQLException {
         
            PreparedStatement stmt = con.prepareStatement(SQL_SIMPLE_SCALE_SEARCH + order_by + " " + sort_by);
            stmt.setLong(1, root_id);
			stmt.setString(2, "%" + search_text.toLowerCase() + "%");
			stmt.setString(3, "%" + search_text.toLowerCase() + "%");
			stmt.setString(4, "%" + search_text.toLowerCase() + "%");
            
            ResultSet rs = stmt.executeQuery();
            
            return rs;
        }
        

    /**
    Advance Search    
    @return Result set of the skill scale
    */
    public static ResultSet advScaleSearch(Connection con, String sort_by, String order_by, long root_id, 
                                           String ssl_title, String ssl_share, int sle_level, 
                                           String sle_label, String sle_description )
        throws SQLException {
                    
            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT DISTINCT ssl_id, ssl_title, ssl_share_ind, TOTAL ")
               .append(" FROM cmSkillScale, cmSkillLevel, ")
               .append(" ( SELECT sle_ssl_id, COUNT(sle_ssl_id) TOTAL ")
               .append("   FROM cmSkillLevel ")
               .append("   GROUP BY sle_ssl_id " );
            if( sle_level != Integer.MIN_VALUE )
                SQL.append(" HAVING COUNT(sle_level) = ? ");
                
            SQL.append(" ) skillLevel ")
               .append(" WHERE ssl_id = skillLevel.sle_ssl_id ")
               .append(" AND cmSkillLevel.sle_ssl_id = skillLevel.sle_ssl_id " )
               .append(" AND ssl_owner_ent_id = ? ");
            
            
            
            if( ssl_title != null && ssl_title.length() > 0 )
                SQL.append(" AND lower(ssl_title) like ? ");
            
            if( ssl_share != null )
                SQL.append(" AND ssl_share_ind = ? ");
                
            if( sle_label != null && sle_label.length() > 0 )
                SQL.append(" AND lower(cmSkillLevel.sle_label) like ? ");
            if( sle_description != null && sle_description.length() > 0 )
                SQL.append(" AND lower(cmSkillLevel.sle_description) like ? ");
                
            SQL.append(" ORDER BY ").append(order_by).append(" ").append(sort_by);
               
            
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            int index = 1;
            if( sle_level != Integer.MIN_VALUE )
                stmt.setInt(index++, sle_level);
            stmt.setLong(index++, root_id);
            if( ssl_title != null && ssl_title.length() > 0 )
                stmt.setString(index++, "%" + ssl_title.toLowerCase() + "%");
            if( ssl_share != null ) {
                if( ssl_share.equalsIgnoreCase("TRUE") )
                    stmt.setBoolean(index++, true);
                else
                    stmt.setBoolean(index++, false);
            }
            if( sle_label != null && sle_label.length() > 0 )
                stmt.setString(index++, "%" + sle_label.toLowerCase() + "%");
            if( sle_description != null && sle_description.length() > 0 )
                stmt.setString(index++, "%" + sle_description.toLowerCase() + "%");                
            
            ResultSet rs = stmt.executeQuery();
        
            return rs;

        }
        
        
        
    /**
    Get the specified list of skill from the database
    */
    public static ResultSet getByIds(Connection con, String sort_by, Vector idVec)
        throws SQLException {
        
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(SQL_GET_BY_ID_VEC)
              .append(cwUtils.vector2list(idVec))
              .append(" ORDER BY skb_title ")
              .append(sort_by);
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
        
        

        
        
        
    /**
    Get the skill path of the skills
    @param vector of the skills id
    @return hashtable skill/node id as the key, SkillTreeNode(parent of the skill/node) as the element
    */
    public static Hashtable getSkillPath(Connection con, Vector skbIdVec)
        throws SQLException {
    //DENNIS: TO BE ENHANCED
          
            /*
            String SQL = " SELECT skl_skb_id, skl_stn_skb_id ,stn_title, stn_stn_skb_id_parent "
                       + " FROM cmSkill, cmSkillTreeNode " 
                       + " WHERE skl_stn_skb_id = stn_skb_id "
                       + " AND skl_skb_id IN " + cwUtils.vector2list(skbIdVec);
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            DbCmSkillTreeNode skillNode = null;
            Hashtable skillPath = new Hashtable();
            Vector stnIdVec = new Vector();
            Long skb_id;
            while( rs.next() ) {
                skillNode = new DbCmSkillTreeNode();
                skillNode.stn_skb_id = rs.getLong("skl_stn_skb_id");
                skillNode.stn_title = rs.getString("stn_title");
                skillNode.stn_stn_skb_id_parent = rs.getLong("stn_stn_skb_id_parent");
                skb_id = new Long(rs.getString("skl_skb_id"));
                skillPath.put(skb_id, skillNode);
                stnIdVec.addElement(new Long(skillNode.stn_stn_skb_id_parent));
            }
            
            SQL = " SELECT stn_skb_id, stn_title, stn_stn_skb_id_parent "
                + " FROM cmSkillTreeNode "
                + " WHERE stn_skb_id IN " + cwUtils.vector2list(stnIdVec);
            
            while( !stnIdVec.isEmpty() ) {
                
                stnIdVec = new Vector();
                stmt = con.prepareStatement(SQL);
                rs = stmt.executeQuery();
                while( rs.next() ) {                
                    skillNode = new DbCmSkillTreeNode();
                    skillNode.stn_skb_id = rs.getLong("stn_skb_id");
                    skillNode.stn_title = rs.getString("stn_title");
                    skb_id = new Long(rs.getLong("stn_stn_skb_id_parent"));
                    skillPath.put(new Long(skillNode.stn_skb_id), skillNode);
                    if( skb_id.longValue() > 0 )
                        stnIdVec.addElement(skb_id);
                }
                
            }

            return skillPath;
            */            
            return null;
        }


        
    public static String getSkillPathTitle(Hashtable skillPath, long skb_id)
        throws SQLException {
            //DENNIS: TO BE ENHANCED
            StringBuffer pathTitle = new StringBuffer();
            /*
            DbCmSkillTreeNode skillNode = (DbCmSkillTreeNode)skillPath.get(new Long(skb_id));
            pathTitle.append(skillNode.stn_title).append(" > ");
            String title;
            while( skillNode.stn_stn_skb_id_parent > 0 ) {
                skillNode = (DbCmSkillTreeNode)skillPath.get(new Long(skillNode.stn_stn_skb_id_parent));
                title = skillNode.stn_title + " > ";
                pathTitle.insert(0, title);
            }
            */            
            return pathTitle.toString();
        }
        
        

    /**
    Get item skill relation
    @param long value of item id
    @return result set 
    */
    public ViewItemSkill[] getItemSkills(Connection con, long itm_id, String sort_by, String order_by)
        throws SQLException {
        
        String SQL = SQL_GET_ITEM_SKILLS + order_by + " " + sort_by;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewItemSkill itmSkill = new ViewItemSkill();
                itmSkill.skb_id = rs.getLong("skb_id");
                itmSkill.skb_title = rs.getString("skb_title");
                itmSkill.itc_skl_level = rs.getLong("itc_skl_level");
                tempResult.addElement(itmSkill);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewItemSkill result[] = new ViewItemSkill[tempResult.size()];
        result = (ViewItemSkill[])tempResult.toArray(result);
        
        return result;
    }

    /**
    Get a Vector of ViewCmSkill to store the shared scales in orgainization.
    Will also get levels of scales in scale_id_list 
    */
    public static Vector getSharedScale(Connection con, long owner_ent_id, long[] scale_id_list) 
        throws SQLException {
            
        PreparedStatement stmt = null;
        Vector v = new Vector();
        if(scale_id_list == null || scale_id_list.length == 0) {
            scale_id_list = new long[1];
            scale_id_list[0] = 0;
        }
        String sql_scale_id_list = cwUtils.array2list(scale_id_list);
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(" Select ssl_id, ssl_title, ssl_share_ind, ssl_owner_ent_id, ")
              .append(" ssl_create_usr_id, ssl_create_timestamp, ")
              .append(" ssl_update_usr_id, ssl_update_timestamp, ")
              .append(" 1 as selected_ind ")
              .append(" From cmSkillScale ")
              .append(" Where ssl_owner_ent_id = ? ")
              .append(" And ssl_share_ind = ? ")
              .append(" And ssl_delete_timestamp is null ")
              .append(" And ssl_id in ").append(sql_scale_id_list)
              .append(" Union ")
              .append(" Select ssl_id, ssl_title, ssl_share_ind, ssl_owner_ent_id, ")
              .append(" ssl_create_usr_id, ssl_create_timestamp, ")
              .append(" ssl_update_usr_id, ssl_update_timestamp, ")
              .append(" 0 as selected_ind ")
              .append(" From cmSkillScale ")
              .append(" Where ssl_owner_ent_id = ? ")
              .append(" And ssl_share_ind = ? ")
              .append(" And ssl_delete_timestamp is null ")
              .append(" And ssl_id not in ").append(sql_scale_id_list)
              .append(" Order By ssl_title asc ");
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, owner_ent_id);
            stmt.setBoolean(2, true);
            stmt.setLong(3, owner_ent_id);
            stmt.setBoolean(4, true);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ViewCmSkill scale = new ViewCmSkill();
                scale.skillScale = new DbCmSkillScale();
                scale.skillLevelVec = null;
                scale.skillScale.ssl_id = rs.getLong("ssl_id");
                scale.skillScale.ssl_title = rs.getString("ssl_title");
                scale.skillScale.ssl_share_ind = rs.getBoolean("ssl_share_ind");
                scale.skillScale.ssl_owner_ent_id = rs.getLong("ssl_owner_ent_id");
                scale.skillScale.ssl_create_usr_id = rs.getString("ssl_create_usr_id");
                scale.skillScale.ssl_create_timestamp = rs.getTimestamp("ssl_create_timestamp");
                scale.skillScale.ssl_update_usr_id = rs.getString("ssl_update_usr_id");
                scale.skillScale.ssl_update_timestamp = rs.getTimestamp("ssl_update_timestamp");
                boolean selected_ind = rs.getBoolean("selected_ind");
                if(selected_ind) {
                    scale.skillLevelVec = new Vector();
                    DbCmSkillLevel skillLevel = new DbCmSkillLevel();
                    skillLevel.sle_ssl_id = scale.skillScale.ssl_id;
                    scale.skillLevelVec = skillLevel.getById(con);
                }
                v.addElement(scale);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }
    
}
