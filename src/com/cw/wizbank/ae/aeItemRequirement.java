package com.cw.wizbank.ae;

import com.cw.wizbank.ae.db.DbItemRequirement;
import com.cw.wizbank.ae.db.DbItemReqDueDate;
import com.cw.wizbank.ae.db.DbItemActn;
import com.cw.wizbank.ae.db.DbItemAttActn;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.ae.db.view.ViewItemRequirement;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.DbUserClassification;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.entity.IndustryCode;
import com.cw.wizbank.util.*;
import com.cw.wizbank.aicc.*;
import com.cwn.wizbank.utils.CommonLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;

public class aeItemRequirement {
    public static final String POSITIVE_ACTION = "POSITIVE";
    public static final String NEGATIVE_ACTION = "NEGATIVE";
    public static final String IAT_TYPE_IWA = "IWA";    // workflow action
    public static final String IAT_TYPE_IAA = "IAA";    // attendance action
    public static final String COND_TYPE_AICC_SCRIPT = "AICC_SCRIPT";
    public static final String COND_TYPE_STATEMENT = "STATEMENT";
    public static final String PREREQUISITE = "PREREQUISITE";
    public static final String EXEMPTION = "EXEMPTION";
    public static final String REQ_SUBTYPE_ENROLLMENT = "ENROLLMENT";
    public static final String REQ_SUBTYPE_COMPLETION = "COMPLETION";
    public static final String REQ_SUBTYPE_USER = "USER";
    public static final String REQ_SUBTYPE_COURSE = "COURSE";
    
    // user should pass either one exemption record
    public boolean checkUserExemption(Connection con, long usrEntId, long itmId) 
            throws cwException, cwSysMessage {

        try {
            boolean returnVal = false;
            AICCScriptInterpreter aiccInterpreter = new AICCScriptInterpreter();
            Vector result = new Vector();
            DbItemRequirement dbItmReq = new DbItemRequirement();
    		PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRequirementByItemId());
    		int index = 1;
    		stmt.setLong(index++, itmId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                dbItmReq.itrOrder               = rs.getLong("itr_order");
                dbItmReq.itrRequirementDueDate  = rs.getTimestamp("itr_requirement_due_date");
			    dbItmReq.itrConditionType		= rs.getString("itr_condition_type");
			    dbItmReq.itrConditionRule		= cwSQL.getClobValue(rs, "itr_condition_rule");
			    dbItmReq.itrRequirementType     = rs.getString("itr_requirement_type");
			    dbItmReq.itrRequirementSubtype  = rs.getString("itr_requirement_subtype");
			    dbItmReq.itrAppnFootnoteInd     = rs.getInt("itr_appn_footnote_ind");

                if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_AICC_SCRIPT)
                    && dbItmReq.itrRequirementType.equalsIgnoreCase(EXEMPTION)
                    && dbItmReq.itrRequirementSubtype.equalsIgnoreCase(REQ_SUBTYPE_USER)) {
                    
                    result.add(new Boolean(aiccInterpreter.interpret(con, usrEntId, dbItmReq.itrRequirementDueDate, dbItmReq.itrConditionRule)));
                }
            }
            stmt.close();
            
            if(result.size()>0) {
                if(result.contains(new Boolean(true))) {
                    returnVal = true;
                } else {
                    returnVal = false;
                }
            }
            return returnVal;

        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
                
    }
    
    
    public boolean checkUserExemptionExist(Connection con, long itmId) 
        throws cwException, cwSysMessage, SQLException {

            boolean returnVal = false;
            DbItemRequirement dbItmReq = new DbItemRequirement();
    		PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRequirementByItemId());
    		int index = 1;
    		stmt.setLong(index++, itmId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
			    dbItmReq.itrConditionType		= rs.getString("itr_condition_type");
			    dbItmReq.itrRequirementType     = rs.getString("itr_requirement_type");
			    dbItmReq.itrRequirementSubtype  = rs.getString("itr_requirement_subtype");
                if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_AICC_SCRIPT)
                    && dbItmReq.itrRequirementType.equalsIgnoreCase(EXEMPTION)
                    && dbItmReq.itrRequirementSubtype.equalsIgnoreCase(REQ_SUBTYPE_USER)) {
                        returnVal = true;
                        break;
                    }
            }
            stmt.close();
            return returnVal;
        }
    
    public boolean checkItemExemption(Connection con, long usrEntId, long itmId) 
            throws cwException, cwSysMessage {
        try {
            boolean returnVal = false;
            AICCScriptInterpreter aiccInterpreter = new AICCScriptInterpreter();
            Vector result = new Vector();
            DbItemRequirement dbItmReq = new DbItemRequirement();
    		PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRequirementByItemId());
    		int index = 1;
    		stmt.setLong(index++, itmId);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
                dbItmReq.itrOrder               = rs.getLong("itr_order");
                dbItmReq.itrRequirementDueDate  = rs.getTimestamp("itr_requirement_due_date");
			    dbItmReq.itrConditionType		= rs.getString("itr_condition_type");
			    dbItmReq.itrConditionRule		= cwSQL.getClobValue(rs, "itr_condition_rule");
			    dbItmReq.itrRequirementType     = rs.getString("itr_requirement_type");
			    dbItmReq.itrRequirementSubtype  = rs.getString("itr_requirement_subtype");
			    dbItmReq.itrAppnFootnoteInd     = rs.getInt("itr_appn_footnote_ind");

                if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_AICC_SCRIPT)
                    && dbItmReq.itrRequirementType.equalsIgnoreCase(EXEMPTION)
                    && dbItmReq.itrRequirementSubtype.equalsIgnoreCase(REQ_SUBTYPE_COURSE)
                    && dbItmReq.itrAppnFootnoteInd == 1) {
                    
                    result.add(new Boolean(aiccInterpreter.interpret(con, usrEntId, dbItmReq.itrRequirementDueDate, dbItmReq.itrConditionRule)));
                }
            }
            stmt.close();
            
            if(result.size()>0) {
                if(result.contains(new Boolean(false))) {
                    returnVal = false;
                } else {
                    returnVal = true;
                }
            }
            return returnVal;

        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }
    
    
    public boolean checkItemExemptionExist(Connection con, long itmId) 
        throws cwException, cwSysMessage, SQLException {

            boolean returnVal = false;
            DbItemRequirement dbItmReq = new DbItemRequirement();
    		PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRequirementByItemId());
    		int index = 1;
    		stmt.setLong(index++, itmId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
			    dbItmReq.itrConditionType		= rs.getString("itr_condition_type");
			    dbItmReq.itrRequirementType     = rs.getString("itr_requirement_type");
			    dbItmReq.itrRequirementSubtype  = rs.getString("itr_requirement_subtype");
			    dbItmReq.itrAppnFootnoteInd     = rs.getInt("itr_appn_footnote_ind");

                if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_AICC_SCRIPT)
                    && dbItmReq.itrRequirementType.equalsIgnoreCase(EXEMPTION)
                    && dbItmReq.itrRequirementSubtype.equalsIgnoreCase(REQ_SUBTYPE_COURSE)
                    && dbItmReq.itrAppnFootnoteInd == 1) {
                        returnVal = true;
                    }
            }
            stmt.close();
            return returnVal;
        }
    
    /**
    @param usrEntId user to check against the prerequisite
    @param itmId item to check against the prerequisite
    @return true/false
    */
    
    public boolean checkPrerequisite(Connection con, long usrEntId, long itmId) 
            throws cwException, cwSysMessage {
        return checkPrerequisite(con, usrEntId, itmId, null);
    }
    
    /**
    @param usrEntId user to check against the prerequisite
    @param itmId item to check against the prerequisite
    @return true/false
    */
    public boolean checkPrerequisite(Connection con, long usrEntId, long itmId, String subtype)
            throws cwException, cwSysMessage {
        return checkPrerequisite(con, usrEntId, itmId, subtype, 1);
    }
            
    /**
    @param usrEntId user to check against the prerequisite
    @param itmId item to check against the prerequisite
    @param subtype COMPLETION/ENROLLMENT type prerequisite
    @param footnote_ind COMPLETION/ENROLLMENT type prerequisite
    @return true/false
    */
    public boolean checkPrerequisite(Connection con, long usrEntId, long itmId, String subtype, int footnote_ind)
            throws cwException, cwSysMessage {
        try {
            boolean returnVal = true;

            AICCScriptInterpreter aiccInterpreter = new AICCScriptInterpreter();
            Vector result = new Vector();
            DbItemRequirement dbItmReq = new DbItemRequirement();
            String SQL = null;
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itmId;
            long parent_itm_id = ire.getParentItemId(con);

    		PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRequirementByItemId());
    		int index = 1;
            if(parent_itm_id > 0) {
                stmt.setLong(index++, parent_itm_id);
            } else {
                stmt.setLong(index++, itmId);
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                dbItmReq.itrOrder               = rs.getLong("itr_order");
                dbItmReq.itrRequirementDueDate  = rs.getTimestamp("itr_requirement_due_date");
			    dbItmReq.itrConditionType		= rs.getString("itr_condition_type");
			    dbItmReq.itrConditionRule		= cwSQL.getClobValue(rs, "itr_condition_rule");
			    dbItmReq.itrRequirementType     = rs.getString("itr_requirement_type");
			    dbItmReq.itrRequirementSubtype  = rs.getString("itr_requirement_subtype");
			    dbItmReq.itrAppnFootnoteInd     = rs.getInt("itr_appn_footnote_ind");
                if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_AICC_SCRIPT)
                    && dbItmReq.itrRequirementType.equalsIgnoreCase(PREREQUISITE)
                    && dbItmReq.itrAppnFootnoteInd == footnote_ind) {
    			    if(subtype == null) {
                        result.add(new Boolean(aiccInterpreter.interpret(con, usrEntId, dbItmReq.itrRequirementDueDate, dbItmReq.itrConditionRule)));
                    } else if(dbItmReq.itrRequirementSubtype.equalsIgnoreCase(subtype)) {
                        result.add(new Boolean(aiccInterpreter.interpret(con, usrEntId, dbItmReq.itrRequirementDueDate, dbItmReq.itrConditionRule)));
                    }
                }
            }
            stmt.close();
            
            if(result.size()>0) {
                if(result.contains(new Boolean(false))) {
                    returnVal = false;
                } else {
                    returnVal = true;
                }
            }
            
            return returnVal;

        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }
	public Hashtable checkPrerequisiteAndGetConditionRuleList(Connection con, long usrEntId, long itmId, String subtype, int footnote_ind)
			throws cwException, cwSysMessage {
		try {
			AICCScriptInterpreter aiccInterpreter = new AICCScriptInterpreter();
			Hashtable result = new Hashtable();
			DbItemRequirement dbItmReq = new DbItemRequirement();
			String SQL = null;
			aeItemRelation ire = new aeItemRelation();
			ire.ire_child_itm_id = itmId;
			long parent_itm_id = ire.getParentItemId(con);

			PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRequirementByItemId());
			int index = 1;
			if(parent_itm_id > 0) {
				stmt.setLong(index++, parent_itm_id);
			} else {
				stmt.setLong(index++, itmId);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				dbItmReq.itrOrder               = rs.getLong("itr_order");
				dbItmReq.itrRequirementDueDate  = rs.getTimestamp("itr_requirement_due_date");
				dbItmReq.itrConditionType		= rs.getString("itr_condition_type");
				dbItmReq.itrConditionRule		= cwSQL.getClobValue(rs, "itr_condition_rule");
				dbItmReq.itrRequirementType     = rs.getString("itr_requirement_type");
				dbItmReq.itrRequirementSubtype  = rs.getString("itr_requirement_subtype");
				dbItmReq.itrAppnFootnoteInd     = rs.getInt("itr_appn_footnote_ind");
				if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_AICC_SCRIPT)
					&& dbItmReq.itrRequirementType.equalsIgnoreCase(PREREQUISITE)
					&& dbItmReq.itrAppnFootnoteInd == footnote_ind) {
					List value = new ArrayList();
					if(subtype == null) {
						if (!aiccInterpreter.interpret(con, usrEntId, dbItmReq.itrRequirementDueDate, dbItmReq.itrConditionRule)){
							if (result.containsKey("false")){
								value = (List)result.get("false");
								value.add(dbItmReq.itrConditionRule);
							} else {
								value.add(dbItmReq.itrConditionRule);
							}
							result.put("false",value);
						}
					} else if(dbItmReq.itrRequirementSubtype.equalsIgnoreCase(subtype)) {
						if (!aiccInterpreter.interpret(con, usrEntId, dbItmReq.itrRequirementDueDate, dbItmReq.itrConditionRule)){
							if (result.containsKey("false")){
								value = (List)result.get("false");
								value.add(dbItmReq.itrConditionRule);
							} else {
								value.add(dbItmReq.itrConditionRule);
							}
							result.put("false",value);
						}
					}
				}
			}
			rs.close();
			stmt.close();
			return result;
		} catch (SQLException e) {
			throw new cwException("SQLException: " + e.getMessage());
		}
	}
    
    /** 
    * insert aeItemRequirement w/ action for attendance status
    */
    public void ins(Connection con, long itrItmId, long itrOrder, String itrRequirementType, 
                    String itrRequirementSubtype, String itrRequirementRestriction, 
                    Timestamp itrRequirementDueDate, int itrAppnFootnoteInd, 
                    String itrConditionType, String itrConditionRule,
                    String posIaaToAttStatus, String negIaaToAttStatus,
                    Timestamp itrProcExecuteTimestamp,String usr_id) 
            throws cwException, cwSysMessage {

        // insert itemaction for both positive & negative
        long posActnId = 0;
        long negActnId = 0;
        if(posIaaToAttStatus != null && posIaaToAttStatus.length() > 0) {
            posActnId = insItemAction(con, posIaaToAttStatus);
        }
        if(negIaaToAttStatus != null && negIaaToAttStatus.length() > 0) {
            negActnId = insItemAction(con, negIaaToAttStatus);
        }
        ins(con, itrItmId, itrOrder, itrRequirementType, 
            itrRequirementSubtype, itrRequirementRestriction, 
            itrRequirementDueDate, itrAppnFootnoteInd, 
            itrConditionType, itrConditionRule, posActnId, negActnId,
            itrProcExecuteTimestamp,usr_id);
    }

    /** 
    * update aeItemRequirement w/ action for attendance status
    */
    public boolean upd(Connection con, long itrItmId, long itrOrder, String itrRequirementType, 
                    String itrRequirementSubtype, String itrRequirementRestriction, 
                    Timestamp itrRequirementDueDate, int itrAppnFootnoteInd, 
                    String itrConditionType, String itrConditionRule,
                    String posIaaToAttStatus, String negIaaToAttStatus,
                    Timestamp itrProcExecuteTimestamp,String usr_id,Timestamp lastUpdTime) 
            throws cwException, cwSysMessage {

        // store the old positive and negative actionId
        long oldPosIatId = -1;
        long oldNegIatId = -1;
        try {
            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            dbItmReq.itrOrder = itrOrder;
            dbItmReq.get(con);
            
            if(!dbItmReq.itr_update_timestamp.equals(lastUpdTime)){
            	return false;
            }
            oldPosIatId = dbItmReq.itrPositiveIatId;
            oldNegIatId = dbItmReq.itrNegativeIatId;
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }

        // insert a new record for item action, both positive & negative
        long posActnId = 0;
        long negActnId = 0;
        if(posIaaToAttStatus != null && posIaaToAttStatus.length() > 0) {
            posActnId = insItemAction(con, posIaaToAttStatus);
        }
        if(negIaaToAttStatus != null && negIaaToAttStatus.length() > 0) {
            negActnId = insItemAction(con, negIaaToAttStatus);
        }
        upd(con, itrItmId, itrOrder, itrRequirementType, 
            itrRequirementSubtype, itrRequirementRestriction, 
            itrRequirementDueDate, itrAppnFootnoteInd, 
            itrConditionType, itrConditionRule, posActnId, negActnId,
            itrProcExecuteTimestamp,usr_id);

        // del the old itmAction, both positive & negative
        if(oldPosIatId > 0) {
            delItemAction(con, oldPosIatId);
        }
        if(oldNegIatId > 0) {
            delItemAction(con, oldNegIatId);
        }
        return true;
    }

    /**
    Update due date of a item requirement. 
    if the due date record does not exists, create one for it
    @param con Connection to database
    @param itrItmId item id of the course or run
    @param itrOrder order of the requirement
    @param itrRequirementDueDate due date to be saved
    */
    public void saveDueDate(Connection con, long itrItmId, long itrOrder
                           ,Timestamp itrRequirementDueDate) 
                           throws SQLException, cwSysMessage {
        
        aeItem itm = new aeItem();
        itm.itm_id = itrItmId;
        itm.getItem(con);

        long parent_itm_id = 0;
        if(itm.itm_run_ind) {
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itm.itm_id;
            parent_itm_id = ire.getParentItemId(con);
        }
        
        DbItemReqDueDate ird = new DbItemReqDueDate();
        ird.irdRequirementDueDate = itrRequirementDueDate;
        ird.irdItrOrder = itrOrder;
        ird.irdChildItmId = itrItmId;
        ird.irdItrItmId = (itm.itm_run_ind) ? parent_itm_id
                                            : itrItmId;
        ird.save(con);
        return;
    }


    /** 
    * update aeItemRequirement execute status 
    */
    public void updExecStatus(Connection con, long itrItmId, long itrOrder,
                    Timestamp itrProcExecuteTimestamp) 
            throws cwException, cwSysMessage {
        try {
            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            dbItmReq.itrOrder = itrOrder;
            dbItmReq.itrProcExecuteTimestamp = itrProcExecuteTimestamp;
            dbItmReq.updExecStatus(con);
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }

    /** 
    * delete aeItemRequirement
    */
    public void del(Connection con, long itrItmId) throws cwException, cwSysMessage {
        try {
            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            dbItmReq.delByItmId(con);
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e);
        }
    }

    /** 
    * delete aeItemRequirement
    */
    public void del(Connection con, long itrItmId, long itrOrder) throws cwException, cwSysMessage {
        try {
            // del itemRequirement
            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            dbItmReq.itrOrder = itrOrder;
            dbItmReq.get(con);
            long posIatId = dbItmReq.itrPositiveIatId;
            long negIatId = dbItmReq.itrNegativeIatId;
            dbItmReq.del(con);
            
            // del the corresponding itmAction, both positive & negative
            if(posIatId > 0) {
                delItemAction(con, posIatId);
            } 
            if(negIatId > 0) {
                delItemAction(con, negIatId);
            }
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
        
        return;
    }

    public String asXML(Connection con, long itrItmId, long itrOrder) throws cwException, qdbException, cwSysMessage {
        try {
            StringBuffer xmlBuffer = new StringBuffer();
            aeItem itm = new aeItem();
            itm.itm_id = itrItmId;
            itm.getItem(con);
            xmlBuffer.append("<item id=\"").append(itm.itm_id).append("\"")
                     .append(" title=\"").append(cwUtils.esc4XML(cwUtils.escNull(itm.itm_title))).append("\"")
                     .append(" itm_tcr_id=\"").append(itm.itm_tcr_id).append("\"")
                     .append(" create_run_ind=\"").append(itm.itm_create_run_ind).append("\"")
            		 .append(" itm_run_ind=\"").append(itm.itm_run_ind).append("\">");
            
            long parent_itm_id = 0;
            if(itm.itm_run_ind) {
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = itm.itm_id;
                parent_itm_id = ire.getParentItemId(con);
                xmlBuffer.append(aeItem.getNavAsXML(con, itm.itm_id));
            }

            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            dbItmReq.itrOrder = itrOrder;
            if(itm.itm_run_ind) {
                dbItmReq.getRun(con, parent_itm_id);
            } else {
                dbItmReq.get(con);
            }
            //dbItmReq.get(con);
            
            xmlBuffer.append(requirementAsXML(con, dbItmReq));
            xmlBuffer.append("</item>");

            return xmlBuffer.toString();
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }

    public String asXML(Connection con, long itrItmId) throws cwException, qdbException, cwSysMessage {
        try {
            StringBuffer xmlBuffer = new StringBuffer();
            aeItem itm = new aeItem();
            itm.itm_id = itrItmId;
            itm.getItem(con);
            xmlBuffer.append("<item id=\"").append(itm.itm_id).append("\"")
                     .append(" title=\"").append(cwUtils.esc4XML(cwUtils.escNull(itm.itm_title))).append("\"")
                     .append(" itm_tcr_id=\"").append(itm.itm_tcr_id).append("\"")
                     .append(" create_run_ind=\"").append(itm.itm_create_run_ind).append("\"")
            		 .append(" itm_run_ind=\"").append(itm.itm_run_ind).append("\">");

            long parent_itm_id = 0;
            if(itm.itm_run_ind) {
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = itm.itm_id;
                parent_itm_id = ire.getParentItemId(con);
                xmlBuffer.append(aeItem.getNavAsXML(con, itm.itm_id));
            }

            xmlBuffer.append("<requirement_set>");
            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            int index = 1;
            try {
                // the original is not good in oracle, cannot return a resultset
                // and close it without closing the statement (2003-06-30 kawai)
                //rs = (itm.itm_run_ind) ? dbItmReq.getByRunItmId(con, parent_itm_id)
                //                       : dbItmReq.getByItmId(con);
                if (itm.itm_run_ind) {
                    //rs = dbItmReq.getByRunItmId(con, parent_itm_id);
            		stmt = con.prepareStatement(OuterJoinSqlStatements.getRunRequirementByItemId());
            		stmt.setLong(index++, itrItmId);
                    stmt.setLong(index++, parent_itm_id);
                    rs = stmt.executeQuery();
                } else {
                    //rs = dbItmReq.getByItmId(con);
            		stmt = con.prepareStatement(OuterJoinSqlStatements.getItemRequirementByItemId());
            		stmt.setLong(index++, itrItmId);
                    rs = stmt.executeQuery();
                }
                while(rs.next()) {
                    dbItmReq.itrOrder                   = rs.getLong("itr_order");
			        dbItmReq.itrRequirementType		    = rs.getString("itr_requirement_type");
			        dbItmReq.itrRequirementSubtype		= rs.getString("itr_requirement_subtype");
			        dbItmReq.itrRequirementRestriction	= rs.getString("itr_requirement_restriction");
			        dbItmReq.itrRequirementDueDate		= rs.getTimestamp("itr_requirement_due_date");
			        dbItmReq.itrAppnFootnoteInd		    = rs.getInt("itr_appn_footnote_ind");
			        dbItmReq.itrConditionType			= rs.getString("itr_condition_type");
			        dbItmReq.itrConditionRule			= cwSQL.getClobValue(rs, "itr_condition_rule");
			        dbItmReq.itrPositiveIatId			= rs.getLong("itr_positive_iat_id");
			        dbItmReq.itrNegativeIatId			= rs.getLong("itr_negative_iat_id");
			        dbItmReq.itrProcExecuteTimestamp	= rs.getTimestamp("itr_proc_execute_timestamp");
			        dbItmReq.itr_update_timestamp = rs.getTimestamp("itr_update_timestamp");
			        dbItmReq.itr_update_usr_id = rs.getString("itr_update_usr_id");
                    xmlBuffer.append(requirementAsXML(con, dbItmReq));
                }
            } finally {
                if (stmt !=null) {
                    stmt.close();
                }
            }
            xmlBuffer.append("</requirement_set>");
            xmlBuffer.append("</item>");
            
            return xmlBuffer.toString();
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }

    // private method
    // insert aeItemRequirement w/ positive and negative iatId
    private void ins(Connection con, long itrItmId, long itrOrder, String itrRequirementType, 
                    String itrRequirementSubtype, String itrRequirementRestriction, 
                    Timestamp itrRequirementDueDate, int itrAppnFootnoteInd, 
                    String itrConditionType, String itrConditionRule,
                    long itrPositiveIatId, long itrNegativeIatId,
                    Timestamp itrProcExecuteTimestamp,String usr_id) 
            throws cwException, cwSysMessage {
        
        try {
            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            dbItmReq.itrOrder = itrOrder;
            dbItmReq.itrRequirementType = itrRequirementType;
            dbItmReq.itrRequirementSubtype = itrRequirementSubtype;
            dbItmReq.itrRequirementRestriction = itrRequirementRestriction;
            dbItmReq.itrRequirementDueDate = itrRequirementDueDate;
            dbItmReq.itrAppnFootnoteInd = itrAppnFootnoteInd;
            dbItmReq.itrConditionType = itrConditionType;
            dbItmReq.itrConditionRule = itrConditionRule;
            dbItmReq.itrPositiveIatId = itrPositiveIatId;
            dbItmReq.itrNegativeIatId = itrNegativeIatId;
            dbItmReq.itrProcExecuteTimestamp = itrProcExecuteTimestamp;
			dbItmReq.itr_create_usr_id = usr_id;
			dbItmReq.itr_update_usr_id = usr_id;
             
            dbItmReq.ins(con);
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
        
        return;
    }
    
    /** 
    * update aeItemRequirement w/ positive and negative iatId
    */
    private void upd(Connection con, long itrItmId, long itrOrder, String itrRequirementType, 
                    String itrRequirementSubtype, String itrRequirementRestriction, 
                    Timestamp itrRequirementDueDate, int itrAppnFootnoteInd, 
                    String itrConditionType, String itrConditionRule,
                    long itrPositiveIatId, long itrNegativeIatId,
                    Timestamp itrProcExecuteTimestamp,String usr_id) 
            throws cwException, cwSysMessage {
        
        try {
            DbItemRequirement dbItmReq = new DbItemRequirement();
            dbItmReq.itrItmId = itrItmId;
            dbItmReq.itrOrder = itrOrder;
            dbItmReq.itrRequirementType = itrRequirementType;
            dbItmReq.itrRequirementSubtype = itrRequirementSubtype;
            dbItmReq.itrRequirementRestriction = itrRequirementRestriction;
            dbItmReq.itrRequirementDueDate = itrRequirementDueDate;
            dbItmReq.itrAppnFootnoteInd = itrAppnFootnoteInd;
            dbItmReq.itrConditionType = itrConditionType;
            dbItmReq.itrConditionRule = itrConditionRule;
            dbItmReq.itrPositiveIatId = itrPositiveIatId;
            dbItmReq.itrNegativeIatId = itrNegativeIatId;
            dbItmReq.itrProcExecuteTimestamp = itrProcExecuteTimestamp;
            dbItmReq.itr_update_usr_id = usr_id;
            
            dbItmReq.upd(con);
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
        
        return;
    }
    
    private String requirementAsXML(Connection con, DbItemRequirement dbItmReq) throws cwException, qdbException, cwSysMessage,SQLException {
        StringBuffer xmlBuffer = new StringBuffer();
            
        xmlBuffer.append("<requirement order=\"" + dbItmReq.itrOrder 
        + "\" type=\"" + cwUtils.esc4XML(dbItmReq.itrRequirementType) 
        + "\" subtype=\"" + cwUtils.escNull(dbItmReq.itrRequirementSubtype) 
        + "\">");
        xmlBuffer.append("<last_updated usr_id=\"").append(dbItmReq.itr_update_usr_id)
                 .append("\" timestamp=\"").append(dbItmReq.itr_update_timestamp).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con,dbItmReq.itr_update_usr_id))).append("</last_updated>");
        if(dbItmReq.itrRequirementRestriction != null) {
            xmlBuffer.append("<restriction>" + cwUtils.esc4XML(dbItmReq.itrRequirementRestriction) + "</restriction>");
        } else {
            xmlBuffer.append("<restriction/>");
        }
        if(dbItmReq.itrRequirementDueDate != null) {
            xmlBuffer.append("<due_date>" + dbItmReq.itrRequirementDueDate + "</due_date>");
        } else {
            xmlBuffer.append("<due_date/>");
        }
        xmlBuffer.append("<appn_footnote_ind>" + dbItmReq.itrAppnFootnoteInd + "</appn_footnote_ind>");
        xmlBuffer.append("<condition type=\"" + cwUtils.esc4XML(dbItmReq.itrConditionType) + "\">");
        if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_AICC_SCRIPT)) {
            xmlBuffer.append("<rule>" + cwUtils.esc4XML(dbItmReq.itrConditionRule) + "</rule>");
            xmlBuffer.append(conditionRuleXML(con, dbItmReq.itrConditionRule));
        } else if(dbItmReq.itrConditionType.equalsIgnoreCase(COND_TYPE_STATEMENT)) {
            xmlBuffer.append("<rule>" + cwUtils.esc4XML(dbItmReq.itrConditionRule) + "</rule>");
        }
        xmlBuffer.append("</condition>");
        xmlBuffer.append("<action>");
        if(dbItmReq.itrPositiveIatId > 0) {
            xmlBuffer.append("<positive>" + itemActionAsXML(con, dbItmReq.itrPositiveIatId) + "</positive>");
        }
        if(dbItmReq.itrNegativeIatId > 0) {
            xmlBuffer.append("<negative>" + itemActionAsXML(con, dbItmReq.itrNegativeIatId) + "</negative>");
        }
        xmlBuffer.append("</action>");
        if(dbItmReq.itrProcExecuteTimestamp != null) {
            xmlBuffer.append("<proc_exec_timestamp>" + dbItmReq.itrProcExecuteTimestamp + "</proc_exec_timestamp>");
        } else {
            xmlBuffer.append("<proc_exec_timestamp/>");
        }
            
        xmlBuffer.append("</requirement>");
        
        return xmlBuffer.toString();
    }

    private String itemActionAsXML(Connection con, long iatId) throws cwException, cwSysMessage {
        try {
            DbItemActn dbItemActn = new DbItemActn();
            DbItemAttActn dbItemAttActn = new DbItemAttActn();
            dbItemActn.iatId = iatId;
            dbItemActn.get(con);
            
            if(dbItemActn.iatType.equalsIgnoreCase(IAT_TYPE_IWA)) {
                // to be implement for workflow action
            } else if (dbItemActn.iatType.equalsIgnoreCase(IAT_TYPE_IAA)) {
                dbItemAttActn.iaaIatId = iatId;
                dbItemAttActn.get(con);
            }

            StringBuffer xmlBuffer = new StringBuffer();
            xmlBuffer.append("<action type=\"" + dbItemActn.iatType + "\" id=\"" + dbItemActn.iatId + "\">");
            if(dbItemActn.iatType.equalsIgnoreCase(IAT_TYPE_IWA)) {
                // to be implement for workflow action
            } else if (dbItemActn.iatType.equalsIgnoreCase(IAT_TYPE_IAA)) {
                xmlBuffer.append("<to_att_status>" + cwUtils.esc4XML(dbItemAttActn.iaaToAttStatus) + "</to_att_status>");
            }
            xmlBuffer.append("</action>");
            
            return xmlBuffer.toString();
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }

    /**
    @return aeItemActn.iat_id
    */
    private long insItemAction(Connection con, String iaaToAttStatus) throws cwException, cwSysMessage {
        try {
            DbItemActn dbItemActn = new DbItemActn();
            dbItemActn.iatType = IAT_TYPE_IAA;
            dbItemActn.ins(con);
            
            DbItemAttActn dbItemAttActn = new DbItemAttActn();
            dbItemAttActn.iaaIatId = dbItemActn.iatId;
            dbItemAttActn.iaaToAttStatus = iaaToAttStatus;
            dbItemAttActn.ins(con);
            
            return dbItemActn.iatId;
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }

    private void delItemAction(Connection con, long iatId) throws cwException, cwSysMessage {
        try { 
            DbItemActn dbItemActn = new DbItemActn();
            dbItemActn.iatId = iatId;
            dbItemActn.get(con);

            if(dbItemActn.iatType.equalsIgnoreCase(IAT_TYPE_IWA)) {
                // to be implement for workflow action
            } else if (dbItemActn.iatType.equalsIgnoreCase(IAT_TYPE_IAA)) {
                DbItemAttActn dbItemAttActn = new DbItemAttActn();
                dbItemAttActn.iaaIatId = iatId;
                dbItemAttActn.del(con);
            }
            
            dbItemActn.del(con);
            return;
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }
    
    public String conditionRuleXML(Connection con, String condRule) throws cwException, qdbException, cwSysMessage {
        try {
            StringBuffer xmlBuffer = new StringBuffer();
            String operator = "";
            String[] operand = null;
            
            if(condRule.indexOf('&')>0) {
                operator = "AND";
                operand = cwUtils.splitToString(condRule, "&");
            } else if(condRule.indexOf('|')>0) {
                operator = "OR";
                operand = cwUtils.splitToString(condRule, "|");
            } else {
                operand = new String[1];
                operand[0] = condRule;
            }

            xmlBuffer.append("<rule_details>");
            xmlBuffer.append("<operator>" + cwUtils.esc4XML(operator) + "</operator>");
            for(int i=0; i<operand.length; i++) {
                String[] operandBlock = cwUtils.splitToString(operand[i], " ");
                for(int j=0; j<operandBlock.length; j++) {
                    if(operandBlock[j].indexOf("_")>0) {
                        String[] operandArray = cwUtils.splitToString(operandBlock[j], "_");
                        long operandId = Long.parseLong(operandArray[1]);
                        if (operandArray[0].equals("ITM")){
                            xmlBuffer.append("<item id=\"").append(operandId)
                                        .append("\" display_bil=\"").append(cwUtils.esc4XML(aeItem.getItemTitle(con, operandId)))
                                        .append("\" itm_code=\"").append(cwUtils.esc4XML(aeItem.getItemCode(con, operandId)))
                                        .append("\"/>");
                        }else{
                            String title = null;
                            if (operandArray[0].equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GROUP)){
                                Hashtable htName = dbUserGroup.getDisplayName(con, "(" + operandId + ")");
                                title = (String)htName.get(Long.toString(operandId));
                                CommonLog.debug("usg: " + title);
                                CommonLog.debug(htName.toString());
                            }else if (operandArray[0].equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GRADE)){
                                Hashtable htName = DbUserGrade.getDisplayName(con, "(" + operandId + ")");
                                title = (String)htName.get(Long.toString(operandId));
                            }else if (operandArray[0].equalsIgnoreCase(dbEntity.ENT_TYPE_INDUSTRY_CODE)){
                                IndustryCode idc = new IndustryCode();
                                idc.ent_id = operandId;
                                idc.getIndustryCode(con);
                                title = idc.name;
                            }else{
                                title = DbUserClassification.getDisplayBil(con, operandId);
                            }
                            
                            // user classification 
                            xmlBuffer.append("<entity id=\"").append(operandId)
                                    .append("\" type=\"").append(operandArray[0])
                                    .append("\" display_bil=\"").append(cwUtils.esc4XML(title))
                                    .append("\" />");
                        }
                    }
                }
            }
            xmlBuffer.append("</rule_details>");
            
            return xmlBuffer.toString();
        } catch (SQLException e) {
            throw new cwException("SQLException: " + e.getMessage());
        }
    }
    
    
       //add by Tim
    
   /* aeItemRequirement.java
    Check the codition rules of each application of the item
    Do the necessary action
    @param root_ent_id site id of the organization
    @upd_usr_id user id of the update user
    */
    public static void execAttendanceAction(Connection con, long action_id, long app_id, Hashtable statusHash, String upd_usr_id) throws SQLException, qdbException, cwSysMessage,cwException {
        long ats_id = 0;
        String toStatus = DbItemAttActn.getAttStatusTo(con, action_id);
        if (toStatus != null) {
            Integer toID = (Integer) statusHash.get(toStatus);
            if (toID != null) {
                ats_id = toID.intValue();
            }
        }
        
        if (ats_id > 0) {
            aeAttendance attd = new aeAttendance();
            attd.att_app_id = app_id;
            attd.att_ats_id = (int)ats_id;
            attd.save(con, null);
        }
        
    }
    
    /* aeItemRequirement.java
    Check the codition rules of each application of the item
    Do the necessary action
    @param root_ent_id site id of the organization
    @upd_usr_id user id of the update user
    */
    public static void doAction(Connection con, String upd_usr_id) throws cwException, cwSysMessage, qdbException, SQLException{
//System.out.println("doAction-----------------------------------");
        
        Timestamp curTime = cwSQL.getTime(con);
        Hashtable statusHash = aeAttendanceStatus.getAllCovStatus(con, 0);
        ViewItemRequirement v_ire = new ViewItemRequirement();
        aeApplication app = null;
        String action_type = null;
        long action_id;
        
//System.out.println("curTime = " + curTime);
        //IAA action
        Vector itemVec = v_ire.getOverdueItemReq(con, curTime, aeItemRequirement.IAT_TYPE_IAA);
//System.out.println("itemVec.size:" + itemVec.size());
        
        for (int i=0; i<itemVec.size();i++) {
            ViewItemRequirement vire = (ViewItemRequirement) itemVec.elementAt(i);
//System.out.println("vire.ire.itrItmId:" + vire.ire.itrItmId);
            Vector appVec = aeApplication.getApplicationByItmId(con, vire.ire.itrItmId, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
//System.out.println("appVec.size:" + appVec.size());
            for (int j=0;j<appVec.size();j++) {
                //Ask for AICC script to check if true or false
                app = (aeApplication) appVec.elementAt(j);
//System.out.println("app.app_id:" + app.app_id);
                
                if (vire.ire.itrConditionType.equals(aeItemRequirement.COND_TYPE_AICC_SCRIPT)) {
                    //if (checkAICCScript(app.app_ent_id, vire.ire.conditionRules)) { //
                    // to be rewrite for AICC class is ready!!!
                    AICCScriptInterpreter aiccInterpreter = new AICCScriptInterpreter();
//System.out.println("ent_id = " + app.app_ent_id);
//System.out.println("due date = " + vire.ire.itrRequirementDueDate);
//System.out.println("rule = " + vire.ire.itrConditionRule);
                    if(aiccInterpreter.interpret(con, app.app_ent_id, vire.ire.itrRequirementDueDate, vire.ire.itrConditionRule)){
                        action_id = vire.ire.itrPositiveIatId;
                        action_type = vire.pos_actn_type;
                    }else {
                        action_id = vire.ire.itrNegativeIatId;
                        action_type = vire.neg_actn_type;
                    }
                }else {
//System.out.println("Condtion type : " + vire.ire.itrConditionType + " not handled yet.");
                    throw new cwException("Condtion type : " + vire.ire.itrConditionType + " not handled yet.");
                }
//System.out.println("action_id:" + action_id);
//System.out.println("action_type:" + action_type);
                if(action_id > 0) {
//System.out.println("Execute action: " + action_id);
                    execAttendanceAction(con, action_id, app.app_id, statusHash, upd_usr_id);
                }
               
            }
            DbItemRequirement.updProcExceTimestamp(con, vire.ire.itrItmId, vire.ire.itrOrder, curTime);
        }
        
        
        //workflow action
        /*
        itemVec = v_ire.getOverdueItemReq(con, curTime, aeItemRequirement.IAT_TYPE_IWA);
        System.out.println("itemVec.size:" + itemVec.size());
        
        for (int i=0; i<itemVec.size();i++) {
            ViewItemRequirement vire = (ViewItemRequirement) itemVec.elementAt(i);
            System.out.println("vire.ire.itrItmId:" + vire.ire.itrItmId);
            Vector appVec = aeApplication.getApplicationByItmId(con, vire.ire.itrItmId, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
            for (int j=0;j<appVec.size();j++) {
                //Ask for AICC script to check if true or false
                app = (aeApplication) appVec.elementAt(j);
                System.out.println("app.app_id:" + app.app_id);
                
                if (vire.ire.itrConditionType.equals(aeItemRequirement.COND_TYPE_AICC_SCRIPT)) {
                    //if (checkAICCScript(app.app_ent_id, vire.ire.conditionRules)) { //
                    // to be rewrite for AICC class is ready!!!
                    AICCScriptInterpreter aiccInterpreter = new AICCScriptInterpreter();
                    if(aiccInterpreter.interpret(con, app.app_ent_id, vire.ire.itrRequirementDueDate, vire.ire.itrConditionRule)){
                    //if(true){
                        action_id = vire.ire.itrPositiveIatId;
                        action_type = vire.pos_actn_type;
                    }else {
                        action_id = vire.ire.itrNegativeIatId;
                        action_type = vire.neg_actn_type;
                    }
                }else {
                    System.out.println("Condtion type : " + vire.ire.itrConditionType + " not handled yet.");
                    throw new cwException("Condtion type : " + vire.ire.itrConditionType + " not handled yet.");
                }
                execAttendanceAction(con, action_id, app.app_id, statusHash, upd_usr_id);
            }
            DbItemRequirement.updProcExceTimestamp(con, vire.ire.itrItmId, vire.ire.itrOrder, curTime);
        }
        */
//System.out.println("endAction-----------------------------------");
        
        
    }
 
    
    
}