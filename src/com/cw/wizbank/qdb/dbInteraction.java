package com.cw.wizbank.qdb;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class dbInteraction  implements Serializable
{

    public String INT_FB_TYPE_TEXT = "text";
    public String INT_FB_TYPE_NUMBER = "number";
    public String INT_SPACE_SEN_DEFAULT = "Y";
    public String INT_CASE_SEN_DEFAULT = "Y";
    public long     int_res_id;
    public int      int_order;
    public String   int_label;
    public String   int_xml_outcome;
    public String   int_xml_explain;
    public long     int_res_id_explain;
    public long     int_res_id_refer;

    public dbInteraction() {;}

    // given a question id, get a list of interaction
    public static Vector getQInteraction(Connection con, long int_res_id)
        throws qdbException
    {
        try {

          PreparedStatement stmt = con.prepareStatement(
            " SELECT int_res_id, int_label, int_order, " +
            " int_xml_outcome, int_xml_explain," +
            " int_res_id_explain, int_res_id_refer " +
            " From Interaction where int_res_id = ? order by int_order");

          stmt.setLong(1, int_res_id);
          ResultSet rs = stmt.executeQuery();
          Vector result = new Vector();
          dbInteraction intObj = null;

          while(rs.next())
          {
                intObj = new dbInteraction();
                intObj.int_res_id = rs.getLong("int_res_id");
                intObj.int_label = rs.getString("int_label");
                intObj.int_order = rs.getInt("int_order");
                //intObj.int_xml_outcome  = rs.getString("int_xml_outcome");
                //intObj.int_xml_explain  = rs.getString("int_xml_explain");
                intObj.int_xml_outcome  = cwSQL.getClobValue(rs, "int_xml_outcome");
                intObj.int_xml_explain  = cwSQL.getClobValue(rs, "int_xml_explain");
                intObj.int_res_id_explain   = rs.getLong("int_res_id_explain");
                intObj.int_res_id_refer     = rs.getLong("int_res_id_refer");

                result.addElement(intObj);
          }
          stmt.close();
          return result;
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // Mark each iteraction
    // String response : FB type of iteraction
    // String[] responses : MC | TF | MT which can have more than one response for each iteraction
    // core 4.6
    public dbProgressAttempt mark(String response, String[] responses, int score_multiplier, boolean flag, boolean negScore)
        throws qdbException
    {
    	return mark(response, responses, null, score_multiplier, flag, negScore, null);
    }

    public dbProgressAttempt mark(String response, String[] responses, String responseExt, int score_multiplier, boolean flag, 
    		boolean negScore, Hashtable intScore) throws qdbException
	{
	    dbProgressAttempt result = new dbProgressAttempt();
	
	    String xmlO = int_xml_outcome;
	    Vector feedback = null;
	    //get int type
	    String int_type = null;
	    String mc_logic = null;
	    int int_score = 0;
	    
	    if (intScore != null) {
	    	int_type = (String)intScore.get("int_type");	    	
		    int_score  = ((Integer)intScore.get("int_score")).intValue();
		    feedback = (Vector)intScore.get("feedback");
		    mc_logic = (String)intScore.get("mc_logic");
	    } else {
	    	int_type = getIntType();
	        try {
	            int_score =
	              Integer.parseInt(dbUtils.perl.substitute("s#<outcome\\s*order\\s*=\\s*\"([0-9]+)\"\\s*type\\s*=\\s*\"([A-Z]+)\"\\s*score\\s*=\\s*\"([0-9]+)\".*>#$3#i", xmlO));
	        } catch (NumberFormatException e) {
	            int_score = 0;
	        }
	        feedback = getFeedback();
	        //check the logic of the Fill in the Blank question
	        mc_logic = dbUtils.perl.substitute("s#.*logic\\s*=\\s*\"(\\w+)\"\\s*.*>#$1#i", xmlO);
	    }
	    // common stuff
	    result.atm_int_res_id   = int_res_id;
	    result.atm_int_order    = int_order;
	    result.atm_flag_ind     = flag;
	    result.atm_max_score    = int_score * score_multiplier;    
	    
	    Vector conds = (Vector) feedback.elementAt(0);
	    Vector scores = (Vector) feedback.elementAt(1);
	    Vector cases = (Vector) feedback.elementAt(2);
	    Vector spaces = (Vector) feedback.elementAt(3);
	    Vector types = (Vector) feedback.elementAt(4);
	
	    String cond_ = "";
	    long score_ = 0;
	    boolean case_ = false;
	    boolean space_ = false;
	    String type_ = "";
	
	    if(int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI) ||
	       int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_TRUEFALSE))
	    {
	        // check null for response
	        if((responses==null || responses.length==0) ||
	           (responses[0]==null || responses[0].length()==0))
	        {
	            result.atm_response_bil = null;
	            result.atm_correct_ind  =false;
	            result.atm_score        =0;
	            return result;
	        }
	        
	        result.atm_response_bil_ext = responseExt;
	
	        result.atm_response_bil = new String();
	        result.atm_correct_ind  =false;
	        result.atm_score        =0;
	
	        // Score if and only if all options are choosen.
	        if (mc_logic != null && mc_logic.equals(extendQue.MC_LOGIC_AND)) {
	            if (conds.size() == responses.length) {
	                // Check for each condition and check if the user answered correctly
	                for (int i=0;i<conds.size();i++) {
	                    result.atm_correct_ind = false;
	                    for (int j=0;j<responses.length;j++) {
	                        if(responses[j].equalsIgnoreCase((String)conds.elementAt(i)))
	                        {
	                            result.atm_correct_ind  =true;
	                        }
	                    }
	                    // If any condition are wrong, the whole question are treated as wrong
	                    if(!result.atm_correct_ind) {
	                        if (negScore){
	                            result.atm_score = -1 * result.atm_max_score;
	                        }
	                        break;
	                    }
	                }
	                // Answered all the correct answers.
	                if (result.atm_correct_ind) {
	                    result.atm_score = result.atm_max_score;
	                }
	            }
	
	            for (int i=0;i<responses.length;i++) {
	                if (i > 0)
	                    result.atm_response_bil += dbProgressAttempt.RESPONSE_DELIMITER;
	
	                result.atm_response_bil += responses[i];
	            }
	
	        }
	        // For example, the question has 4 options named A, B, C , D
	        // and A, B are the correct with 1 score for each
	        // - The user will get 1 score if he answer A or B only
	        // - The user will get 2 score if he answer A and B only
	        // - Ther user will get 0 score if he answer any C or D
	        else if (mc_logic != null && mc_logic.equals(extendQue.MC_LOGIC_OR)) {
	        	
	            if  (responses.length <= conds.size()) {
	            	int correct_count = 0;
	                // Check for each condition and check if the user answered correctly
	                for (int i=0;i<responses.length;i++) {
	                    result.atm_correct_ind = false;
	                    for (int j=0;j<conds.size();j++) {
	                        if(responses[i].equalsIgnoreCase((String)conds.elementAt(j)))
	                        {
	                        	correct_count ++;
	                            result.atm_correct_ind  =true;
	                            result.atm_score += ((Long) scores.elementAt(j)).longValue() * score_multiplier;
	                        }
	                    }
	                    // If any condition are wrong, the whole question are treated as wrong
	                    if(!result.atm_correct_ind) {
	                        if (negScore){
	                            result.atm_score = -1 * result.atm_max_score;
	                        }
	                        //break;
	                    }
	                }
	                if (!negScore && correct_count < responses.length) {
	                	result.atm_correct_ind = false;
	                	result.atm_score = 0;
	                }
	            }
	
	            for (int i=0;i<responses.length;i++) {
	                if (i != 0)
	                    result.atm_response_bil += dbProgressAttempt.RESPONSE_DELIMITER;
	                result.atm_response_bil += responses[i];
	            }
	        }else {
	            result.atm_response_bil = responses[0];
	            // Check for each condition and check if the user answered correctly
	            result.atm_correct_ind = false;
	            long tmp_score = 0;
	            for (int i=0;i<conds.size();i++) {
	                if(result.atm_response_bil.equalsIgnoreCase((String)conds.elementAt(i)))
	                {
	                    result.atm_correct_ind  =true;
	                    tmp_score = ((Long) scores.elementAt(i)).longValue();
	                    if (tmp_score > result.atm_score) {
	                        result.atm_score = tmp_score;
	                    }
	                    result.atm_score *= score_multiplier;
	                }
	            }
	            if (!result.atm_correct_ind){
	                if (negScore){
	                    result.atm_score = -1 * result.atm_max_score;
	                }
	            }
	        }
	    }else if(int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING))
	    {
	
	        if (responses == null || responses.length ==0
	            || responses[0] == null || responses[0].length() == 0) {
	
	                result.atm_response_bil = null;
	
	                if (conds.size() > 0)
	                    result.atm_correct_ind  =false;
	                else
	                    result.atm_correct_ind  =true;
	
	                result.atm_score        =0;
	
	                return result;
	        }
	
	        result.atm_response_bil = "";
	        result.atm_score        =0;
	
	        long score =0;
	
	        if (conds.size()==0)  {
	            result.atm_correct_ind = false;
	        }else {
	            result.atm_correct_ind = true;
	
	            for (int i=0;i<conds.size();i++) {
	                boolean correct_ind = false;
	
	                for (int j=0;j<responses.length;j++) {
	                        if (responses[j] != null && (String)conds.elementAt(i) !=null) {
	                            if (responses[j].equals((String)conds.elementAt(i))) {
	                                correct_ind = true;
	                                score += ((Long) scores.elementAt(i)).longValue();
	                            }
	                        }
	                }
	
	                if (!correct_ind){
	                    result.atm_correct_ind  =false;
	                    if (negScore){
	                        result.atm_score = -1 * result.atm_max_score;
	                    }
	
	                }
	            }
	        }
	
	        for (int i=0;i<responses.length;i++) {
	            if (i != 0)
	                result.atm_response_bil += dbProgressAttempt.RESPONSE_DELIMITER;
	
	            result.atm_response_bil += responses[i];
	        }
	
	        result.atm_score = score * score_multiplier;
	
	    }
	    else if(int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_FILLBLANK))
	    {
	         // check null for response
	        if (response == null || response.length() ==0) {
	
	                    result.atm_response_bil = null;
	                    result.atm_correct_ind  =false;
	                    result.atm_score        =0;
	                    return result;
	        }
	
	        long maxScore = 0 ;
	        long usr_score = 0;
	
	        result.atm_correct_ind  =false;
	        result.atm_response_bil = response;
	        result.atm_score = 0;
	        for (int i=0 ;i < conds.size();i ++) {
	            usr_score = 0;
	
	            cond_ = (String) conds.elementAt(i);
	            cond_ = dbUtils.unEscXML(cond_);
	
	            case_ = ((Boolean) cases.elementAt(i)).booleanValue();
	            space_ = ((Boolean) spaces.elementAt(i)).booleanValue();
	            type_ = (String) types.elementAt(i);
	            score_ = ((Long) scores.elementAt(i)).longValue();
	
	            if (type_.equalsIgnoreCase(INT_FB_TYPE_NUMBER)) {
	
	                boolean isVal = true;
	                double resp_val = 0;
	                double cond_val = 0;
	                try {
	                    resp_val = (new Double(response)).doubleValue();
	                    cond_val = (new Double(cond_)).doubleValue();
	                }catch (NumberFormatException e){
	                    isVal = false;
	                }
	                if (isVal && Math.abs(resp_val-cond_val) < 0.001) {
	                    result.atm_response_bil = cond_;
	                    result.atm_correct_ind  =true;
	                    usr_score        = score_;
	                }
	            }else {
	                String cond_val = null;
	                String response_val = null;
	                if (!space_) {
	                    cond_val = dbUtils.trimSpace(cond_);
	                    response_val = dbUtils.trimSpace(response);
	                }else {
	                    cond_val = new String(cond_);
	                    response_val = response;
	                }
	
	                if(!case_ && cond_val.equalsIgnoreCase(response_val)) {
	                    result.atm_response_bil = cond_;
	                    result.atm_correct_ind  =true;
	                    usr_score        = score_;
	                }else if(case_ && cond_val.equals(response_val)) {
	                    result.atm_response_bil = cond_;
	                    result.atm_correct_ind  =true;
	                    usr_score        = score_;
	                }else {
	                    result.atm_correct_ind  =false;
	                    if (negScore){
	                        usr_score        = -1 *  score_;
	                    }
	                }
	            }
	
	            if (usr_score < 0){
	                if (usr_score < maxScore )
	                    maxScore = usr_score;
	            }else{
	                if (usr_score > maxScore )
	                    maxScore = usr_score;
	            }
				if( result.atm_correct_ind ) {
					break;
				}                
	        }
	        result.atm_score = maxScore * score_multiplier;
	
	    } else if(int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) 
	    || int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
			// check null for response
			if (response == null || response.length() ==0) {
				result.atm_response_bil = null;
				result.atm_correct_ind  = false;
				result.atm_score        = -1;
				return result;
			}
			result.atm_correct_ind  = false;
			result.atm_response_bil = response;
			result.atm_score = -1;
	    }
	    else
	        throw new qdbException("Invalid interaction type: " + int_type);
	    return result;
	}    
    
    public String getIntType()
    {
        String xmlO = int_xml_outcome;

        String match = "<feedback";

        int indexS = xmlO.indexOf(match,0);

        if (indexS >=0)
            xmlO = xmlO.substring(0, indexS);

        //return dbUtils.perl.substitute("s#<outcome\\s*order\\s*=\\s*\"([0-9]+)\"\\s*.*type\\s*=\\s*\"([A-Z]+)\"\\s*.*>#$2#i", xmlO).trim();
        return this.getQuestionTypeFromTag(xmlO);
    }

    public Vector getFeedback()
        throws qdbException
    {
        Vector feedback = new Vector();

        Vector conds = new Vector();
        Vector scores = new Vector();
        Vector cases = new Vector();
        Vector types = new Vector();
        Vector spaces = new Vector();

        String xmlO = int_xml_outcome;

        String int_type = getIntType();

        int ind = 0;
        int ind2 = 0;
        String match = "<feedback";
        // dispatch by type

        String condition_, case_, score_, type_, space_ ;

        while(ind>=0)
        {
            ind = xmlO.indexOf(match);

            if(ind>=0)
            {
                xmlO = xmlO.substring(ind);

                ind2 = xmlO.indexOf(match, match.length());
                String matchStr = "";

                if (ind2 > 0) {
                    matchStr = xmlO.substring(0, ind2);
                    xmlO = xmlO.substring(ind2);
                } else {
                    matchStr = xmlO;
                    xmlO = "";
                }

                if (matchStr.indexOf("score") >=0) {

                    if (int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI) ||
                    	int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_TRUEFALSE) ||
                        int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING) ||
                        int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_TYPING) ) {
                        condition_ =
                            dbUtils.perl.substitute("s#.*condition\\s*=\\s*\"(.*)\"\\s*score.*#$1#i", matchStr);
                        score_ =
                            dbUtils.perl.substitute("s#.*score\\s*=\\s*\"([0-9]+)\".*#$1#i", matchStr);

                        case_ = "";
                        space_ = "";
                        type_ = "";

                    }else if (int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_FILLBLANK)) {

                        condition_ =
                            dbUtils.perl.substitute("s#.*condition\\s*=\\s*\"(.*)\"\\s*case.*#$1#i", matchStr);
                        score_ =
                            dbUtils.perl.substitute("s#.*score\\s*=\\s*\"([0-9]+)\".*#$1#i", matchStr);


                        case_ =
                            dbUtils.perl.substitute("s#.*case_sensitive\\s*=\\s*\"(\\w+)\".*#$1#i", matchStr);
                        space_ =
                            dbUtils.perl.substitute("s#.*space_sensitive\\s*=\\s*\"(\\w+)\".*#$1#i", matchStr);
                        type_ =
                            dbUtils.perl.substitute("s#.*type\\s*=\\s*\"(\\w+)\".*#$1#i", matchStr);


                    }else {
                        throw new qdbException("Failed to get condition.");
                    }

                    conds.addElement(condition_);
                    scores.addElement(new Long(score_));


                    if (condition_ ==null || score_ == null)
                        throw new qdbException("Failed to get condition.");

                    if (case_ == null || case_.equalsIgnoreCase("Y"))
                        cases.addElement(new Boolean(true));
                    else
                        cases.addElement(new Boolean(false));

                    if (space_ == null || space_.equalsIgnoreCase("Y"))
                        spaces.addElement(new Boolean(true));
                    else
                        spaces.addElement(new Boolean(false));

                    if(type_ != null && type_.length() > 0 && type_.equalsIgnoreCase(INT_FB_TYPE_NUMBER)) 
                    	types.addElement(INT_FB_TYPE_NUMBER);
                	else 
                		types.addElement(INT_FB_TYPE_TEXT);
                    
//                    if (type_ == null || type_.equalsIgnoreCase(INT_FB_TYPE_TEXT))
//                        types.addElement(INT_FB_TYPE_TEXT);
//                    else
//                        types.addElement(INT_FB_TYPE_NUMBER);

                }
            }
        }

        feedback.addElement(conds);
        feedback.addElement(scores);
        feedback.addElement(cases);
        feedback.addElement(spaces);
        feedback.addElement(types);
        return feedback;
    }
    
    
	private String getQuestionTypeFromTag(String tag){
		if ( tag == null || tag.length() == 0 ) {
			return "";
		}
		int start = tag.toLowerCase().indexOf("type=\"") + 6;
		int end = tag.toLowerCase().indexOf("\"", start + 1);
		if ( start >= end || start == -1 || end == -1 ) {
			return "";
		}
		return tag.substring(start, end);
	}
    
	// given a list of question id, get hashtable, with queId as key a list of interaction as value    
    public static Hashtable getInteractions(Connection con, Vector int_res_id)
        throws SQLException {
        Hashtable htQueInteraction = new Hashtable();
        if (int_res_id.size() == 0) {
            return htQueInteraction;
        }
        String colName = "tmp_res_id";
        String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, int_res_id, cwSQL.COL_TYPE_LONG);
        
        PreparedStatement stmt =
            con.prepareStatement(
                " SELECT int_res_id, int_label, int_order, "
                    + " int_xml_outcome, int_xml_explain,"
                    + " int_res_id_explain, int_res_id_refer "
                    + " From Interaction where int_res_id in "
                    + " (SELECT tmp_res_id FROM " + tableName + ")"
                    + " order by int_res_id, int_order");

        ResultSet rs = stmt.executeQuery();
        dbInteraction intObj = null;

        while (rs.next()) {
            intObj = new dbInteraction();
            intObj.int_res_id = rs.getLong("int_res_id");
            intObj.int_label = rs.getString("int_label");
            intObj.int_order = rs.getInt("int_order");
            intObj.int_xml_outcome =
                cwSQL.getClobValue(rs, "int_xml_outcome");
            intObj.int_xml_explain =
                cwSQL.getClobValue(rs, "int_xml_explain");
            intObj.int_res_id_explain = rs.getLong("int_res_id_explain");
            intObj.int_res_id_refer = rs.getLong("int_res_id_refer");
            Long L_int_res_id = new Long(intObj.int_res_id);
            Vector result = (Vector)htQueInteraction.get(L_int_res_id);
            if (result == null) {
                result = new Vector();
            }
            result.addElement(intObj);
            htQueInteraction.put(L_int_res_id, result);
        }
        stmt.close();
        cwSQL.dropTempTable(con, tableName);

        return htQueInteraction;
    }
}    