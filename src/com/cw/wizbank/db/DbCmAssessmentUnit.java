package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.cwException;
/**
A database class to manage table cmAssessmentUnit
*/
public class DbCmAssessmentUnit{
    
    public long         asu_asm_id;
    public long         asu_ent_id;
    public long         asu_attempt_nbr;
    public long         asu_weight;
    public long         asu_sks_skb_id;
    public String       asu_type;
    public boolean      asu_submit_ind;
//    public boolean      asu_notify_ind;
    
    private static final String COMMA       =   ",";
    private static final String RESOLVED    =   "RESOLVED";
    private static final String SELF        =   "SELF";
    
    private static final String SQL_RESOLVE_ASM = " Update cmAssessmentUnit "
                                                + " Set asu_sks_skb_id = ?, "
                                                + " asu_submit_ind = ? "
                                                + " where asu_asm_id = ? "
                                                + " and asu_type = ? ";

    private static String SQL_GET_OTHER_ASU_COUNT = " select count(*) "
                                              + " from cmAssessmentUnit "
                                              + " where asu_asm_id = ? "
                                              + " and asu_type not in (?, ?) ";

    private static String SQL_GET_OTHER_NOT_SUBMITTED_COUNT = " select count(*) "
                                                            + " from cmAssessmentUnit "
                                                            + " where asu_asm_id = ? "
                                                            + " and asu_type not in (?, ?) "
                                                            + " and asu_submit_ind = ? ";

    private static String SQL_SUBMIT_TST_ASM = " UPDATE cmAssessmentUnit SET "
                                  + "  asu_sks_skb_id = ?, "
                                  + "  asu_submit_ind = ? "
                                  + "  WHERE asu_asm_id = ? "
                                  //+ "  AND asu_ent_id = ? "
                                  + "  AND asu_attempt_nbr = ? ";    
    
    private static String SQL_INS_CMASSESSMENTUNIT = " INSERT INTO cmAssessmentUnit ( "
                                  + "  asu_asm_id, asu_ent_id, asu_attempt_nbr, "
                                  + "  asu_weight, asu_sks_skb_id, asu_type, asu_submit_ind ) "
                                  + " VALUES (?,?,?,?,?,?,?) ";

    private static String SQL_UPD_CMASSESSMENTUNIT = " UPDATE cmAssessmentUnit SET "
                                  + "  asu_weight = ?, asu_sks_skb_id = ?, asu_type = ?, asu_submit = ? "
                                  + " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_attempt_nbr = ? ";



    private static String SQL_DEL_CMASSESSMENTUNIT = " DELETE From cmAssessmentUnit "
                                  + " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_attempt_nbr = ? ";
 
    private static final String SQL_GET_ASSESSMENTUNIT = " SELECT asu_weight, asu_sks_skb_id, asu_type, asu_submit_ind "
                                                       + " FROM cmAssessmentUnit "
                                                       + " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_attempt_nbr = ? AND asu_type <> ? ";
                                                       
    /*
    private static final String SQL_GET_BY_TYPE = " SELECT AU1.asu_ent_id, AU1.asu_weight, AU1.asu_sks_skb_id "
                                                + " FROM cmAssessmentUnit AU1, "
                                                + "     ( SELECT asu_asm_id, asu_ent_id, MAX(asu_attempt_nbr) ATTEMPT "
                                                + "       FROM cmAssessmentUnit "
                                                + "       WHERE asu_asm_id = ? AND asu_type = ? "
                                                + "       GROUP BY asu_asm_id, asu_ent_id "
                                                + "     ) AU2 "
                                                + " WHERE AU1.asu_ent_id = AU2.asu_ent_id "
                                                + " AND   AU1.asu_attempt_nbr = AU2.ATTEMPT "
                                                + " AND   AU1.asu_asm_id = ? "
                                                + " AND   AU1.asu_type = ? ";
    */
    
    private static final String SQL_GET_BY_TYPE = " SELECT asu_ent_id, asu_weight, asu_sks_skb_id "
                                                + " FROM cmAssessmentUnit "
                                                + " WHERE asu_asm_id = ? "
                                                + " AND   asu_type = ? "
                                                + " AND   asu_submit_ind = ? ";
    
    
    private static final String SQL_GET_SKILL_SET_IDS = " SELECT asu_sks_skb_id "
                                                      + " FROM cmAssessmentUnit "
                                                      + " WHERE asu_asm_id = ? "
                                                      + " AND   asu_ent_id = ? "
                                                      + " AND   asu_attempt_nbr = ? ";
                                                      
    private static final String SQL_GET_SKILL_SET_ID = " SELECT asu_sks_skb_id "
                                                     + " FROM cmAssessmentUnit "
                                                     + " WHERE asu_asm_id = ? "
                                                     + " AND   asu_ent_id = ? "
                                                     + " AND   asu_attempt_nbr = ? ";

    private static final String SQL_GET_ASR_SKILL_SET_ID = " SELECT asu_sks_skb_id "
                                                     + " FROM cmAssessmentUnit "
                                                     + " WHERE asu_asm_id = ? "
                                                     + " AND   asu_ent_id = ? "
                                                     + " AND   asu_attempt_nbr = ? "
                                                     + " AND   asu_type <> ? ";
                                                     
                                                     

    private static final String SQL_GET_LATEST_SKILL_SET_ID = " SELECT asu_sks_skb_id "
                                                            + " FROM cmAssessmentUnit "
                                                            + " WHERE asu_asm_id = ? "
                                                            + " AND   asu_ent_id = ? "                                                     
                                                            + " ORDER BY asu_attempt_nbr DESC ";
    
    
    private static final String SQL_DEL_NOT_IN_LIST = " DELETE From cmAssessmentUnit "
                                                    + " WHERE asu_asm_id = ? "
                                                    + " AND asu_type <> ? "
                                                    + " AND asu_ent_id NOT IN ";

    private static final String SQL_DEL_NOT_IN_LIST_BY_ASU_TYPE = 
                                                      " DELETE From cmAssessmentUnit "
                                                    + " WHERE asu_asm_id = ? "
                                                    + " AND asu_type = ? "
                                                    + " AND asu_ent_id NOT IN ";
                                                                    
    private static final String SQL_CHECK_ASSESSMENTUNIT = " SELECT COUNT(*) AS total FROM cmAssessmentUnit "
                                                         + " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_type <> ? ";

    private static final String SQL_CHECK_ASSESSMENTUNIT_W_TYPE = " SELECT COUNT(*) AS total FROM cmAssessmentUnit "
                                                         + " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_type = ? ";

    private static final String SQL_NUMBER_OF_ATTEMPT = " SELECT asu_attempt_nbr FROM cmAssessmentUnit "
                                                      + " WHERE asu_asm_id = ? AND asu_ent_id = ? ";
 
    private static final String SQL_GET_RESOLVED_SCORE_SKS_ID = " SELECT asu_sks_skb_id FROM cmAssessmentUnit "
                                                              + " WHERE asu_asm_id = ? AND asu_type = ? AND asu_submit_ind = ? "; 

 /*
    private static final String SQL_NUMBER_OF_NOTIFY = " SELECT COUNT(*) total FROM cmAssessmentUnit "
                                                    + " WHERE asu_asm_id = ? AND asu_notify_ind = ? ";
 */                                                   
    private static final String SQL_NUMBER_OF_SUBMIT = " SELECT COUNT(*) total FROM cmAssessmentUnit "
                                                    + " WHERE asu_asm_id = ? AND asu_submit_ind = ? "
                                                    + " AND asu_type != ? ";
 
    private static final String SQL_GET_ASSESSMENT_UNITS_ID = " SELECT asu_ent_id FROM cmAssessmentUnit "
                                                        +   " WHERE asu_asm_id = ? AND asu_type != ? " ;
 
    private static final String SQL_UPD_SKILL_SET_ID = " UPDATE cmAssessmentUnit SET asu_sks_skb_id = ? "
                                   +  " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_attempt_nbr = ? AND asu_type != ? ";
                                   
    private static final String SQL_UPD_RESOLVED_ID = " UPDATE cmAssessmentUnit SET asu_submit_ind = ? , asu_sks_skb_id = ? , asu_ent_id = ? "
                                                    + " WHERE asu_asm_id = ? AND asu_type = ? ";

    private static final String SQL_UPD_SUBMIT_STATUS = " UPDATE cmAssessmentUnit SET asu_submit_ind = ? "
                                     + " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_attempt_nbr = ? AND asu_type != ? ";
                                     
    
    private static final String SQL_UPD_INFO = " UPDATE cmAssessmentUnit SET asu_weight = ?, asu_type = ? "
                                             + " WHERE asu_asm_id = ? AND asu_ent_id = ? AND asu_type <> ? ";
                                             
    private static final String SQL_DEL_BY_ASM_ASU_TYPE = " DELETE FROM cmAssessmentUnit "
                                                        + " WHERE asu_asm_id = ? "
                                                        + " AND asu_type = ? ";
    
    private static final String SQL_DEL_NOT_IN_BY_ASM_ASU_TYPE = 
                                                          " DELETE FROM cmAssessmentUnit "
                                                        + " WHERE asu_asm_id = ? "
                                                        + " AND asu_type = ? "
                                                        + " AND asu_ent_id not in ? ";

	private static final String SQL_GET_NUM_ASSESSEE_BY_TYPE = 
			"Select Count(*), asu_type " +
			"From cmAssessmentUnit " +
			"Where asu_asm_id = ? " +
			"Group By asu_type ";

    /**
    Inser a new competency AssessmentUnit
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException {
        int index =1;
        PreparedStatement stmt = con.prepareStatement(SQL_INS_CMASSESSMENTUNIT);
        stmt.setLong(index++, asu_asm_id);
        stmt.setLong(index++, asu_ent_id);
        stmt.setLong(index++, asu_attempt_nbr);
        stmt.setLong(index++, asu_weight);
        if (asu_sks_skb_id > 0) {
            stmt.setLong(index++, asu_sks_skb_id);
        }else {
            stmt.setNull(index++, java.sql.Types.INTEGER);
        }
        stmt.setString(index++, asu_type);
        stmt.setBoolean(index++, asu_submit_ind);
        //stmt.setBoolean(index++, asu_notify_ind);
        int code = stmt.executeUpdate();
        stmt.close();

        return code;
    }

    /**
    Update a specified competency AssessmentUnit from the database
    @return the row count for UPDATE
    */
    public int upd(Connection con)
        throws SQLException {
        int index =1;    
        PreparedStatement stmt = con.prepareStatement(SQL_UPD_CMASSESSMENTUNIT);
        stmt.setLong(index++, asu_weight);
        if (asu_sks_skb_id > 0) {
            stmt.setLong(index++, asu_sks_skb_id);
        }else {
            stmt.setNull(index++, java.sql.Types.INTEGER);
        }                
        stmt.setString(index++, asu_type);
//        stmt.setBoolean(index++, asu_ind);
        stmt.setLong(index++, asu_asm_id);
        stmt.setLong(index++, asu_ent_id);
        stmt.setLong(index++, asu_attempt_nbr);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }

    /**
    Delete a specified competency AssessmentUnit from the database
    @return the row count for DELETE    
    */
    public int del(Connection con)
        throws SQLException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_DEL_CMASSESSMENTUNIT);
        stmt.setLong(1, asu_asm_id);
        stmt.setLong(2, asu_ent_id);
        stmt.setLong(3, asu_attempt_nbr);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }
    
    
    /**
    Get detial of assessment unit
    */
    public void get(Connection con)
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_ASSESSMENTUNIT);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            stmt.setLong(3, asu_attempt_nbr);
            stmt.setString(4, RESOLVED);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                asu_weight = rs.getLong("asu_weight");
                asu_sks_skb_id = rs.getLong("asu_sks_skb_id");
                asu_submit_ind = rs.getBoolean("asu_submit_ind");               
                asu_type = rs.getString("asu_type");
            } else 
                throw new cwException("Assessor not exist usr_ent_id = " + asu_ent_id);
            
            stmt.close();
            return;
            
        }
    
    
    /**
    Get number of attempt of the AssessmentUnit on the Assessment    
    */
    public long getAttemptNumber(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_NUMBER_OF_ATTEMPT);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            ResultSet rs = stmt.executeQuery();
            long nbr = 0;
            if( rs.next() )
                nbr = rs.getLong("asu_attempt_nbr");
            stmt.close();
            return nbr;            
        }
        



    /**
    Delete AssessmentUnit not in list
    @param long array of the user entity id not to be deteted
    @return number of AssessmentUnit deleted
    */
    public int delNotInList(Connection con, long[] ids)
        throws SQLException {
            
            StringBuffer str = new StringBuffer().append("(0");            
            int count = 0;
            for(int i=0; i<ids.length; i++)
                str.append(COMMA).append(ids[i]);
            str.append(")");
            
            Vector vec = getSkillSetIdNotInList(con, ids);
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(SQL_DEL_NOT_IN_LIST + str);
                stmt.setLong(1, asu_asm_id);
                stmt.setString(2, RESOLVED);
                count = stmt.executeUpdate();
            } finally {
                if(stmt!= null) stmt.close();
            }            
            delSkillSetInVec(con, vec);
            
            return count;
        }

    /**
    Delete AssessmentUnit not in list
    @param long array of the user entity id not to be deteted
    @return number of AssessmentUnit deleted
    */
    public int delNotInListByAsuType(Connection con, long[] ids)
        throws SQLException {
            
            int count = 0;
            StringBuffer str = new StringBuffer().append("(0");            
            for(int i=0; i<ids.length; i++)
                str.append(COMMA).append(ids[i]);
            str.append(")");
            
            Vector vec = getSkillSetIdNotInListByAsuType(con, ids);
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(SQL_DEL_NOT_IN_LIST_BY_ASU_TYPE + str);
                stmt.setLong(1, asu_asm_id);
                stmt.setString(2, asu_type);
                count = stmt.executeUpdate();
            } finally {
                if(stmt!=null) stmt.close();
            }

            delSkillSetInVec(con, vec);
                        
            return count;
        }

    /**
    Delete SkillSet in the Vector 
    @param vec vector of Skill Set id to be deleted
    */
    private void delSkillSetInVec(Connection con, Vector vec)
        throws SQLException {
                        
            long[] sks_id_list = new long[vec.size()];
            for(int i=0; i<sks_id_list.length; i++)
                sks_id_list[i] = ((Long)vec.elementAt(i)).longValue();

            DbCmSkillSetCoverage dbSsc = new DbCmSkillSetCoverage();
            dbSsc.delAllSkills(con, sks_id_list);
            
            DbCmSkillSet dbSs = new DbCmSkillSet();
            dbSs.del(con, sks_id_list);
            
            return;
        }


    /**
    * Get the skill set id belong to the user not in list
    */
    public Vector getSkillSetIdNotInList(Connection con, long[] ids)
        throws SQLException{

            StringBuffer str = new StringBuffer().append("(0");            
            for(int i=0; i<ids.length; i++)
                str.append(COMMA).append(ids[i]);
            str.append(")");

            String SQL = "SELECT asu_sks_skb_id "
                       + " FROM cmAssessmentUnit "
                       + " WHERE asu_asm_id = ? "
                       + " AND asu_type <> ? "
                       + " AND asu_ent_id NOT IN " + str.toString();

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asu_asm_id);
            stmt.setString(2, RESOLVED);
            Vector sksIdVec = new Vector();
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                sksIdVec.addElement(new Long(rs.getLong("asu_sks_skb_id")));
            stmt.close();
            return sksIdVec;
        }

    /**
    * Get the skill set id belong to the user not in list
    */
    private Vector getSkillSetIdNotInListByAsuType(Connection con, long[] ids)
        throws SQLException{

            StringBuffer str = new StringBuffer().append("(0");            
            for(int i=0; i<ids.length; i++)
                str.append(COMMA).append(ids[i]);
            str.append(")");

            String SQL = "SELECT asu_sks_skb_id "
                       + " FROM cmAssessmentUnit "
                       + " WHERE asu_asm_id = ? "
                       + " AND asu_type = ? "
                       + " AND asu_ent_id NOT IN " + str.toString();

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asu_asm_id);
            stmt.setString(2, asu_type);
            Vector sksIdVec = new Vector();
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                sksIdVec.addElement(new Long(rs.getLong("asu_sks_skb_id")));
            stmt.close();
            return sksIdVec;
        }
        
    
    /**
    Get assessment unit by type    
    @return array of DbCmAssessmentUnit
    */    
    public DbCmAssessmentUnit[] getByType(Connection con)
        throws SQLException {
            
            Vector vec = new Vector();
            DbCmAssessmentUnit DbAu = new DbCmAssessmentUnit();
            DbAu.asu_asm_id = asu_asm_id;
            DbAu.asu_type = asu_type;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL_GET_BY_TYPE);
            stmt.setLong(1, asu_asm_id);
            stmt.setString(2, asu_type);
            stmt.setBoolean(3, true);
            
            rs = stmt.executeQuery();
            while( rs.next() ) {
                DbCmAssessmentUnit _DbAu = new DbCmAssessmentUnit();
                _DbAu.asu_asm_id = asu_asm_id;
                _DbAu.asu_type = asu_type;                
                _DbAu.asu_ent_id = rs.getLong("asu_ent_id");
                _DbAu.asu_weight = rs.getLong("asu_weight");
                _DbAu.asu_sks_skb_id = rs.getLong("asu_sks_skb_id");
                vec.addElement(_DbAu);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            }
            
            DbCmAssessmentUnit[] DbAuArray = new DbCmAssessmentUnit[vec.size()];
            for(int i=0; i<DbAuArray.length; i++)
                DbAuArray[i] = (DbCmAssessmentUnit)vec.elementAt(i);
            
            return DbAuArray;
        }

        
    /**
    Get Skill Set ID
    @return long of skill set id
    */
    public long getSkillSetId(Connection con)
        throws SQLException {                        
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_SKILL_SET_ID);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            stmt.setLong(3, asu_attempt_nbr);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                asu_sks_skb_id = rs.getLong("asu_sks_skb_id");
            else
                asu_sks_skb_id = 0;

            stmt.close();            
            return asu_sks_skb_id;
        }
        
        
    /**
    Get Skill Set ID
    @return long of skill set id
    */
    public long getASRSkillSetId(Connection con)
        throws SQLException {                        
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_ASR_SKILL_SET_ID);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            stmt.setLong(3, asu_attempt_nbr);
            stmt.setString(4, RESOLVED);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                asu_sks_skb_id = rs.getLong("asu_sks_skb_id");
            else
                asu_sks_skb_id = 0;

            stmt.close();            
            return asu_sks_skb_id;
        }
        
    /**
    Get Skill Set Id of rator
    @return Skill Set Id
    */
    public long getLstestSkillSetId(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_LATEST_SKILL_SET_ID);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                asu_sks_skb_id = rs.getLong("asu_sks_skb_id");
            else                
                asu_sks_skb_id = 0;
            stmt.close();
            return asu_sks_skb_id;
        }
        
    /**
    Get Skill Set IDs
    @return long[] of skill set id
    */
    public long[] getSkillSetIds(Connection con)
        throws SQLException {
            
            Vector idVec = new Vector();
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_SKILL_SET_IDS);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            stmt.setLong(3, asu_attempt_nbr);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ){
            	idVec.addElement(new Long(rs.getLong("asu_sks_skb_id")));
            }
            stmt.close();
            long[] ids = new long[idVec.size()];
            for(int i=0; i<ids.length; i++)
                ids[i] = ((Long)idVec.elementAt(i)).longValue();
    
            return ids;            
        }

    
    

    /**
    Check the AssessmentUnit picked for the Assessment or not
    @return boolean 
    */
    public boolean exist(Connection con)
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_CHECK_ASSESSMENTUNIT);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            stmt.setString(3, RESOLVED);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() ) {
                if( rs.getLong("total") > 0 )
                    flag = true;
            }else
                throw new cwException("Failed to check the assessment unit, entity id = " + asu_ent_id );
            stmt.close();
            return flag;
        }

    public boolean existWithType(Connection con)
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_CHECK_ASSESSMENTUNIT_W_TYPE);
            stmt.setLong(1, asu_asm_id);
            stmt.setLong(2, asu_ent_id);
            stmt.setString(3, asu_type);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() ) {
                if( rs.getLong("total") > 0 )
                    flag = true;
            }else
                throw new cwException("Failed to check the assessment unit, entity id = " + asu_ent_id );
            stmt.close();
            return flag;
        }
        
    /**
    Get number of notified/unnotified user
    @param flag ( true : count notified user, false : count unnotified user )
    @return row count of SELECT
    */
    public int numberOfNotify(Connection con, boolean flag)
        throws SQLException, cwException {
            return 0;
            /*
            PreparedStatement stmt = con.prepareStatement(SQL_NUMBER_OF_NOTIFY);            
            stmt.setLong(1, asu_asm_id);
            stmt.setBoolean(2, flag);
            ResultSet rs = stmt.executeQuery();
            int count;
            if( rs.next() )
                count = rs.getInt("total");
            else
                throw new cwException("Failed to check the notification of assessment, id = " + asu_asm_id );
            stmt.close();
            return count;
            */
        }
    
    

    /**
    Get number of submited/unsubmited user
    @param flag ( true : count submited user, false : count unsubmited user )
    @return row count of SELECT
    */
    public int numberOfSubmit(Connection con, boolean flag)
        throws SQLException, cwException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_NUMBER_OF_SUBMIT);
            stmt.setLong(1, asu_asm_id);
            stmt.setBoolean(2, flag);
            stmt.setString(3, RESOLVED);
            ResultSet rs = stmt.executeQuery();
            int count;
            if( rs.next() )
                count = rs.getInt("total");
            else
                throw new cwException("Failed to check the SUBMITION of assessment, id = " + asu_asm_id );
            stmt.close();
            return count;
        }
    

    /**
    Get the assessor entity id of the specified assessment id
    @return vector of assessor entity id
    */
    public Vector getAuEntityId(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_ASSESSMENT_UNITS_ID);
            stmt.setLong(1, asu_asm_id);
            stmt.setString(2, RESOLVED);
            ResultSet rs = stmt.executeQuery();
            Vector entId = new Vector();
            while( rs.next() )
                entId.addElement(new Long(rs.getLong("asu_ent_id")));
            
            stmt.close();
            return entId;
        }
    
        
    /**
    Update skill set id
    */
    public void updSkillSetId(Connection con)
        throws SQLException, cwException {

            PreparedStatement stmt = con.prepareStatement(SQL_UPD_SKILL_SET_ID);
            stmt.setLong(1, asu_sks_skb_id);
            stmt.setLong(2, asu_asm_id);
            stmt.setLong(3, asu_ent_id);
            stmt.setLong(4, asu_attempt_nbr);
            stmt.setString(5, RESOLVED);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Assessor not exist usr_ent_id = " + asu_ent_id);
                
            stmt.close();
            return;
        }


    /**
    Update skill set id and update the enttiy id of assessment unit
    @param long value of the entity id
    */
    public void updResolvedId(Connection con)
        throws SQLException, cwException {

            PreparedStatement stmt = con.prepareStatement(SQL_UPD_RESOLVED_ID);
            stmt.setBoolean(1, true);
            stmt.setLong(2, asu_sks_skb_id);
            stmt.setLong(3, asu_ent_id);
            stmt.setLong(4, asu_asm_id);
            stmt.setString(5, RESOLVED);
            if( stmt.executeUpdate() != 1 ){
				//throw new cwException("Assessor not exist usr_ent_id = " + asu_ent_id);
				this.asu_submit_ind = true;
				this.asu_type = RESOLVED;
				this.asu_weight = 1;
				ins(con);
            }
            stmt.close();
            return;
        }



    /**
    Update submit indicator
    */
    public void updSubmitStatus(Connection con)
        throws SQLException, cwException {

            PreparedStatement stmt = con.prepareStatement(SQL_UPD_SUBMIT_STATUS);
            stmt.setBoolean(1, asu_submit_ind);
            stmt.setLong(2, asu_asm_id);
            stmt.setLong(3, asu_ent_id);
            stmt.setLong(4, asu_attempt_nbr);
            stmt.setString(5, RESOLVED);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Assessor not exist usr_ent_id = " + asu_ent_id);
                
            stmt.close();
            return;            
        }

        
    /**
    Get skill set id which containing the resolved score of the assessment
    */
    public long getResolvedId(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_RESOLVED_SCORE_SKS_ID);
            stmt.setLong(1, asu_asm_id);
            stmt.setString(2, RESOLVED);
            stmt.setBoolean(3, true);
            ResultSet rs = stmt.executeQuery();
            long sks_id;
            if( rs.next() )
                sks_id = rs.getLong("asu_sks_skb_id");
            else
                sks_id = 0;
            
            stmt.close();
            return sks_id;
        }
        



    /**
    Update assessment unit weight and type
    */
    public void updAssessUnitInfo(Connection con)
        throws SQLException, cwException {
        
            PreparedStatement stmt = con.prepareStatement(SQL_UPD_INFO);
            stmt.setLong(1, asu_weight);
            stmt.setString(2, asu_type);
            stmt.setLong(3, asu_asm_id);
            stmt.setLong(4, asu_ent_id);
            stmt.setString(5, RESOLVED);
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Update Assessment Unit Error , asu_ent_id = " + asu_ent_id);
            stmt.close();
            return;        
        }

    public static void submitTestAssessment(Connection con, long asu_asm_id, 
                                            long asu_attempt_nbr, long asu_sks_skb_id) 
                                            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_SUBMIT_TST_ASM);
            stmt.setLong(1, asu_sks_skb_id);
            stmt.setBoolean(2, true);
            stmt.setLong(3, asu_asm_id);
            //stmt.setLong(4, asu_ent_id);
            stmt.setLong(4, asu_attempt_nbr);
            stmt.executeUpdate();            
        } finally {
            if(stmt!=null) stmt.close();
        }
        
    }

    public static void resolveAssessment(Connection con, long asu_asm_id, long asu_sks_skb_id) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_RESOLVE_ASM);
            stmt.setLong(1, asu_sks_skb_id);
            stmt.setBoolean(2, true);
            stmt.setLong(3, asu_asm_id);
            stmt.setString(4, RESOLVED);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
        
    /**
    Check if all "Other" ASU has been submitted.
    "Other" means all asu_type other than "RESOLVED" and "SELF"
    */
    public static boolean isAllOtherAsuSubmitted(Connection con, long asm_id) throws SQLException {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            stmt = con.prepareStatement(SQL_GET_OTHER_NOT_SUBMITTED_COUNT);
            stmt.setLong(1, asm_id);
            stmt.setString(2, RESOLVED);
            stmt.setString(3, SELF);
            stmt.setBoolean(4, false);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                result = (count == 0);
            } else {
                result = false;
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return result;
    }

    public static boolean hasOtherAsu(Connection con, long asm_id) throws SQLException {
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            stmt = con.prepareStatement(SQL_GET_OTHER_ASU_COUNT);
            stmt.setLong(1, asm_id);
            stmt.setString(2, RESOLVED);
            stmt.setString(3, SELF);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                result = (count > 0);
            } else {
                result = false;
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return result;
    }       

    /**
    Delete AssessmentUnit by asm_id and type
    @return number of AssessmentUnit deleted
    */
    public static int delByAsmAsuType(Connection con, long asm_id, String asu_type) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL_DEL_BY_ASM_ASU_TYPE);
        stmt.setLong(1, asm_id);
        stmt.setString(2, asu_type);
        int count = stmt.executeUpdate();
        stmt.close();
        return count;
    }

	public static Hashtable getAssesseeNumByType(Connection con, long asm_id)
		throws SQLException {
			
			Hashtable h_type_num = new Hashtable();
			PreparedStatement stmt = con.prepareStatement(SQL_GET_NUM_ASSESSEE_BY_TYPE);
			stmt.setLong(1, asm_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				h_type_num.put(rs.getString("asu_type"), new Integer(rs.getInt(1)));
			}
			stmt.close();
			return h_type_num;
		}


}
