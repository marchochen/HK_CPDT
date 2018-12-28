package com.cw.wizbank.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.db.DbCmSkillBase;
import com.cw.wizbank.db.DbCmSkillSet;
import com.cw.wizbank.db.DbCmSkillSetCoverage;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.util.cwUtils;


/**
ViewCmSkillSet is an class to deal with SkillSet Management
*/
public class ViewCmSkillCoverage {
    
    private static final String SKILL_SET       =   "SKILL_SET";
    private static final String COMMA           =   ",";
    /**
    class variables
    */
    public DbCmSkillSet skillSet    = null;
    public Vector sscVec            = null;
    
	private static String SQL_DEL_BY_SKILL_ID_N_TYPE = "DELETE FROM cmSkillSetCoverage WHERE ssc_skb_id = ? AND ssc_sks_skb_id IN (SELECT sks_skb_id FROM cmSkillSet WHERE sks_type = ? )";

    private static final String SQL_GET_SKILL_BY_TYPE = " select skl_skb_id, ssc_level "
                                                      + " from cmSkillSetCoverage, cmSkill "
                                                      + " where ssc_sks_skb_id = ? "
                                                      + " and ssc_skb_id = skl_skb_id "
                                                      + " and skl_type = ? ";

    private static final String SQL_GET_RATING_SKILL = 
        " select skl_skb_id skillId" +
        " from cmSkillSetCoverage, cmSkill " +
        " where ssc_skb_id = skl_skb_id " +
        " and ssc_sks_skb_id = ? " +
        " and skl_rating_ind = ? " +
        " union " +
        " select skill.skb_id skillId" +
        " from cmSkillSetCoverage, cmSkill composite, cmSkillBase skill " +
        " where ssc_skb_id = composite.skl_skb_id " +
        " and ssc_sks_skb_id = ? " +
        " and skl_rating_ind = ? " +
        " and skill.skb_parent_skb_id = composite.skl_skb_id ";
    
    /**
    Get all the skill belonging to a skillset
    @param sks_id must be provided
    @return a Resultset containing all the necessary field
    */
    public ResultSet getSkillCoverage(Connection con, long sks_id) 
        throws SQLException {
        
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" SELECT ssc_skb_id, ssc_level, ssc_priority, ")
              .append("    skb_title, skb_description, ")
              .append("    sks_create_usr_id, sks_create_timestamp, ")
              .append("    sks_update_usr_id, sks_create_timestamp, ")
              .append("    ssl_id, ssl_title, ")
              .append("    ssl_create_usr_id, ssl_create_timestamp, ")
              .append("    ssl_update_usr_id, ssl_create_timestamp  ")
              .append(" FROM cmSkillSetCoverage, cmSkillBase, cmSkill, cmSkillScale, cmSkillSet ")
              .append(" WHERE ssl_id = skb_ssl_id ")
              .append("   AND skb_id = skl_skb_id " )
              .append("   AND skl_skb_id = ssc_skb_id " )
              .append("   AND ssc_sks_skb_id = ? ")
              .append("   AND sks_skb_id = ssc_sks_skb_id ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, sks_id);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    
    /**
    Get the basic information of the skill profile
    @param order_by ( 1 - number of skill, 2 - skill set id, 3 - skill set title )
    @return a ResultSet containing skill set id, skill set title and number of skill
    */
    public ResultSet getSkillSetInfo(Connection con, String sks_type, long[] id, int order_by, String sort_by, long root_id)
        throws SQLException {
            
            StringBuffer str = new StringBuffer();
            if( id != null && id.length > 0 ) {
                str.append("(0");
                for(int i=0; i<id.length; i++)
                    str.append(COMMA).append(id[i]);
                str.append(")");    
            }
            
            StringBuffer SQL = new StringBuffer();
			SQL.append(OuterJoinSqlStatements.viewCmSkillCoverageGetSkillSetInfo());
            
            if( root_id != 0 ) 
                SQL.append(" AND sks_owner_ent_id = ? ");
            
            if( id != null && id.length > 0 )
                SQL.append(" AND sks_skb_id IN ").append(str);
                
            SQL.append(" GROUP BY sks_skb_id, sks_title ")
               .append(" ORDER BY " ) 
               .append(order_by)
               .append(" ").append(sort_by);
        
            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            stmt.setString(1, sks_type);
            if( root_id != 0 )
                stmt.setLong(2, root_id);
            ResultSet rs = stmt.executeQuery();
            return rs;            
        }
    

    /**
    Get the skill ids in a skill set and the skills are for rating (skl_rating_ind = 1)
    @param con Connection to database
    @param sks_id Skill Set id
    @return long array of skill ids that skl_rating_ind = 1
    */
    public long[] getRatingSkill(Connection con, long sks_id) throws SQLException {
        long[] skillIdArray = null;
        PreparedStatement stmt = null;
        try {
            Vector vSkillId = new Vector();
            stmt = con.prepareStatement(SQL_GET_RATING_SKILL);
            stmt.setLong(1, sks_id);
            stmt.setBoolean(2, true);
            stmt.setLong(3, sks_id);
            stmt.setBoolean(4, false);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                vSkillId.addElement(new Long(rs.getLong("skillId")));
            }
            skillIdArray = new long[vSkillId.size()];
            for (int i=0; i<vSkillId.size(); i++) {
                skillIdArray[i] = ((Long)vSkillId.elementAt(i)).longValue();
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return skillIdArray;
    }
    
    
    
    

            
    /**
    Get all the skills belonging to a skillset
    @param sks_id must be provided
    @return array of DbCmSkillSetCoverage
    */
    public DbCmSkillSetCoverage[] getAllSkillsSetCoverage(Connection con, long sks_id)
        throws SQLException {
            
            Vector classVec = new Vector();
            getSkillCoverage(con, sks_id, 0, classVec, 0);
            
            DbCmSkillSetCoverage[] DbSsc = new DbCmSkillSetCoverage[classVec.size()];
            for(int i=0; i<DbSsc.length; i++) {
                DbSsc[i] = new DbCmSkillSetCoverage();
                DbSsc[i] = (DbCmSkillSetCoverage)classVec.elementAt(i);
            }
            
            return DbSsc;
        }
    
    public void getSkillCoverage(Connection con, long sks_id, long level, Vector classVec, int index) 
        throws SQLException {        
        StringBuffer ViewCmSkillCoverage_GET_SKILL_COVERAGE = new StringBuffer();
        ViewCmSkillCoverage_GET_SKILL_COVERAGE.append(" SELECT ssc_skb_id, ssc_level, ssc_xml, ssc_priority ")
                                              .append(" FROM cmSkillSetCoverage ")
                                              .append(" WHERE ssc_sks_skb_id = ? ")

                                              .append(" ORDER BY ssc_priority asc");
        
        Vector thisSetVec = new Vector();
        Vector thisSetTypeVec = new Vector();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(ViewCmSkillCoverage_GET_SKILL_COVERAGE.toString());
            stmt.setLong(1, sks_id);
            rs = stmt.executeQuery();
        
        String tmp_level = null;
        while( rs.next() ) {
            DbCmSkillSetCoverage _SCclass = new DbCmSkillSetCoverage();
            _SCclass.ssc_sks_skb_id = sks_id;
            _SCclass.ssc_skb_id = rs.getLong("ssc_skb_id");
            _SCclass.ssc_level = rs.getFloat("ssc_level");
            tmp_level = rs.getString("ssc_level");
            if( tmp_level != null ){
            	_SCclass.bAnswered = true;
            } else {
            	_SCclass.bAnswered = false;
            }
            _SCclass.ssc_priority = rs.getLong("ssc_priority");
            _SCclass.ssc_xml = rs.getString("ssc_xml");
            _SCclass.skb_type = DbCmSkillBase.COMPETENCY_SKILL;
            _SCclass.level = level + 1;            
            thisSetVec.addElement(_SCclass);
            _SCclass = null;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        for(int i=0; i<thisSetVec.size(); i++) {

            if( ((DbCmSkillSetCoverage)thisSetVec.elementAt(i)).skb_type.equalsIgnoreCase(SKILL_SET)){

                getSkillCoverage(con, ((DbCmSkillSetCoverage)thisSetVec.elementAt(i)).ssc_skb_id, ((DbCmSkillSetCoverage)thisSetVec.elementAt(i)).level, thisSetVec, i);
                i += thisSetVec.size()-1;

            }
        }
        if( index < classVec.size() ) {
            classVec.addAll(index + 1, thisSetVec);
        }else{
            classVec.addAll(index, thisSetVec);
        }
        
        return;
    }
    
    
    
    
    
    
    
    /**
    Get all the skillset belonging to an organization
    @param owner_ent_id entity id of the organization
    @return a Resultset containing all the necessary field
    */
    public ResultSet getSkillProfileByOwnerEntId(Connection con, long owner_ent_id, String type) 
        throws SQLException {
        
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" SELECT sks_id, sks_title, sks_create_usr_id  ")
              .append("    count(ssc_skb_id) FROM ")
              .append(" FROM cmSkillSetCoverage, cmSkillSet ")
              .append(" WHERE ssc_sks_id = sks_id ")
              .append("   AND sks_owner_ent_id = ? ")
              .append("   AND sks_type = ? ")
              .append(" GROUP BY sks_id, sks_title, sks_create_usr_id ")
              .append(" ORDER BY sks_title ASC ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, owner_ent_id);
        stmt.setString(2, type);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    
    
    
    
    /**
    Get the related information of the skills and the specified skill set
    @param long value of skill set id
    @param vector with long element ( get the item only on the vector, null get all items )
    @param resultset 
    */
    public static ResultSet getRelation(Connection con, long sks_id, Vector idVec)
        throws SQLException {
            
            String SQL_GET_RELATION = " SELECT ssc_skb_id, ssc_level, ssc_priority, max_level, "
                                    + " skb_title, skb_description "
                                    + " FROM cmSkillSetCoverage , cmSkill , cmSkillBase, "
                                    + " ( SELECT sle_ssl_id, MAX(sle_level) max_level "
                                    + "   FROM cmSkillLevel GROUP BY sle_ssl_id ) skillLevel "
                                    + " WHERE skb_ssl_id = skillLevel.sle_ssl_id "
                                    + " AND skb_id = skl_skb_id "
                                    + " AND ssc_skb_id = skl_skb_id AND ssc_sks_skb_id = ? ";
            if( idVec != null )
                SQL_GET_RELATION += " AND ssc_skb_id IN " + cwUtils.vector2list(idVec);
            
            SQL_GET_RELATION += " ORDER BY ssc_priority DESC ";
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_RELATION);
            stmt.setLong(1, sks_id);
            ResultSet rs = stmt.executeQuery();
            return rs;            
        }
    
    
    /**
    Get target level of the skill belong to the specified skill set in Hashtable
    @param long value of the skill set id
    @param vector with long element ( get the item only on the vector, null get all items )
    @param hashtable
    */
    public static Hashtable getRelationHash(Connection con, long sks_id, Vector idVec)
        throws SQLException {
            
            Hashtable skillLevelHash = new Hashtable();
            String SQL_GET_RELATION = " SELECT ssc_skb_id, ssc_level "                                    
                                    + " FROM cmSkillSetCoverage "
                                    + " WHERE ssc_sks_skb_id = ? ";
            if( idVec != null && !idVec.isEmpty())
                SQL_GET_RELATION += " AND ssc_skb_id IN " + cwUtils.vector2list(idVec);
                        
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_RELATION);
            stmt.setLong(1, sks_id);
            ResultSet rs = stmt.executeQuery();
            Long skillId;
            Float skillLevel;
            while( rs.next() ) {
                skillId = new Long(rs.getLong("ssc_skb_id"));
                skillLevel = new Float(rs.getFloat("ssc_level"));
                skillLevelHash.put(skillId, skillLevel);
            }
            stmt.close();
            return skillLevelHash;
        }
    
    /**
    Insert a new skillset <BR>
    Preset object(s) : skillSet
    @param owner_ent_id entity id of the organization
    @return a Resultset containing all the necessary field
    */
    /*
    public void insSkillSet(Connection con, String usr_id, long owner_ent_id) 
        throws SQLException, cwSysMessage {
        
        Timestamp curTime = cwSQL.getTime(con);
        skillSet.skb_owner_ent_id = owner_ent_id;
        skillSet.skb_create_usr_id = usr_id;
        skillSet.skb_create_timestamp = curTime;
        skillSet.skb_update_usr_id = usr_id;
        skillSet.skb_update_timestamp = curTime;
        skillSet.ins(con);

    }
    */
    /**
    Delete a skillset
    Preset object(s) : skillSet    
    */
    /*
    public void delSkillSet(Connection con) 
        throws SQLException {
        
        DbCmSkillSetCoverage ssc = new DbCmSkillSetCoverage();
        ssc.ssc_sks_id = skillSet.sks_id;
        ssc.delAllSkill(con);
        
        skillSet.del(con);

    }
    */
    /**
    Update a skillset
    Preset object(s) : skillSet    
    */
    /*
    public void updSkillSet(Connection con, String usr_id) 
        throws SQLException {
        
        skillSet.sks_update_usr_id = usr_id;
        skillSet.sks_update_timestamp = cwSQL.getTime(con);
        
        skillSet.upd(con);

    }
    */
    /**
    add skill to skillset
    Preset object(s) : skillSet, sscVec
    */
    /*
    public void addSkills(Connection con, String usr_id) 
        throws SQLException {
        
        for (int i=0; i < sscVec.size();i++) {
            DbCmSkillSetCoverage ssc = new DbCmSkillSetCoverage();
            ssc.ssc_sks_id = skillSet.sks_id;
            ssc.ins(con);
        }
        
    }
    */
    /**
    delete skill from skillset
    Preset object(s) : skillSet, sscVec
    */
    /*
    public void removeSkills(Connection con, String usr_id) 
        throws SQLException {
        
        for (int i=0; i < sscVec.size();i++) {
            DbCmSkillSetCoverage ssc = new DbCmSkillSetCoverage();
            ssc.ssc_sks_id = skillSet.sks_id;
            ssc.del(con);
        }
    }
    */
    /**
    update skill level and priority from skillset
    Preset object(s) : skillSet, sscVec
    */
    /*
    public void updSkills(Connection con, String usr_id) 
        throws SQLException {
        
        for (int i=0; i < sscVec.size();i++) {
            DbCmSkillSetCoverage ssc = new DbCmSkillSetCoverage();
            ssc.ssc_sks_id = skillSet.sks_id;
            ssc.upd(con);
        }
    }
    */
    
    /*
        physically delete skill for the specific skill set type
    */
    public static int removeSkills(Connection con, long skill_id, String type) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SQL_DEL_BY_SKILL_ID_N_TYPE);
        stmt.setLong(1, skill_id);
        stmt.setString(2, type);
        
        int code = stmt.executeUpdate();
        stmt.close();
        return code;                        
    }

    public static Vector[] getSkillSetSkillByType(Connection con, long skillSetId, String skillType) throws SQLException {
        Vector vSkillId = new Vector();
        Vector vLevel = new Vector();
        PreparedStatement stmt = null;
        Vector[] v = new Vector[2];
        try {
            stmt = con.prepareStatement(SQL_GET_SKILL_BY_TYPE);
            stmt.setLong(1, skillSetId);
            stmt.setString(2, skillType);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                vSkillId.addElement(new Long(rs.getLong("skl_skb_id")));
                vLevel.addElement(new Float(rs.getFloat("ssc_level")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        v[0] = vSkillId;
        v[1] = vLevel;
        return v;
    }
    
    public static void getSkillSetSkillHs(Connection con, String setIdStr, Hashtable skillHs) throws SQLException {
    	String sql="select ssc_sks_skb_id,ssc_skb_id from cmSkillSetCoverage where ssc_sks_skb_id in ("+setIdStr+") order by ssc_sks_skb_id";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt=con.prepareStatement(sql);
        rs = stmt.executeQuery();
        int rsCnt=1;
        while(rs.next()){
        	long set_id = rs.getLong("ssc_sks_skb_id");
        	long skill_id= rs.getLong("ssc_skb_id");
        	Long setIdObj =new Long(set_id);
        	Hashtable setHs = new Hashtable();
        	if(skillHs.containsKey(setIdObj)){
        		setHs =(Hashtable) skillHs.get(setIdObj);
        		rsCnt=setHs.size();
        		setHs.put(new Long(rsCnt+1),  new Long (skill_id));
        		skillHs.put(setIdObj, setHs);
        	}else{
        		setHs.put(new Long(rsCnt), new Long (skill_id));
        		skillHs.put(setIdObj, setHs);
        	}        	
        }
        stmt.close();
    }
    
}
