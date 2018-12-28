package com.cw.wizbank.qdb;

import java.io.IOException;
import java.io.StringReader;
import java.sql.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cw.wizbank.util.*;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.db.view.ViewResources;
import com.cw.wizbank.quebank.quecontainer.DynamicQueContainer;
import com.cw.wizbank.quebank.quecontainer.FixedQueContainer;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.report.LearnerRptExporter;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.ContextPath;

public class dbQuestion extends dbResource {
    /**
     * CLOB column
     * Table:       Interaction
     * Column:      int_xml_outcome     int_xml_explain
     * Nullable:    YES                 YES
     */
    public static final String QUE_TYPE_MULTI      = "MC";
    public static final String QUE_TYPE_FILLBLANK  = "FB";
    public static final String QUE_TYPE_TRUEFALSE  = "TF";
    public static final String QUE_TYPE_MATCHING   = "MT";
    public static final String QUE_TYPE_TYPING     = "TP";
    public static final String QUE_TYPE_ESSAY     = "ES";
	public static final String QUE_TYPE_ESSAY_2      = "ES2";

    static final String QUE_LANG_HTML  = "HTML";
    static final String QUE_LANG_WML   = "WML";
    static final String QUE_LANG_TEXT   = "TEXT";
    static final String QUE_LANG_ALL   = "ALL";

    public long   que_res_id;
    public String que_xml;
    public int    que_score;
    public String que_type;
    public int    que_int_count;
    public String que_prog_lang;
    public boolean que_media_ind;
	public boolean que_attempted = false;
	public boolean que_submit_file_ind;
    public boolean que_sc_shuffle = false;
    
    //for delete question of public evaluation
    public boolean deleted_attempted_que_ind = false; 

    public static final String SMESG_QUE_USED_BY_OTHERS = "QUE001";
    public static final String SMESG_QUE_ATTEMPTED      = "QUE002";

    // add Vector of interactions
    public Vector ints;
    
    // 用于存放情景题的子题目对象 - 以Map的形式存放
    public Vector sub_que_vec;
    public int qct_allow_shuffle_ind;
    //动态情景题的抽题条件的id
    public List qcs_id;
    public int qcs_qcount;
    public int qcs_score;
    //public 

    public dbQuestion() {
    	ints = new Vector();
    	sub_que_vec = new Vector();
    	qcs_id = new ArrayList();
    }

    public void get(Connection con)
            throws qdbException, cwSysMessage
    {
        try {
            res_id = que_res_id;
            super.get(con);

            PreparedStatement stmt = con.prepareStatement(
            "SELECT  "
            + " que_xml, "
            + " que_score, "
            + " que_type, "
            + " que_int_count, "
            + " que_prog_lang, "
            + " que_media_ind "
            + " que_media_ind, que_submit_file_ind "
            + " from Question "
            + " where que_res_id =?");

            // set the values for prepared statements
            stmt.setLong(1, que_res_id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                //que_xml = rs.getString("que_xml");
                que_xml         = cwSQL.getClobValue(rs, "que_xml");
                que_score       = rs.getInt("que_score");
                que_type        = rs.getString("que_type");
                que_int_count   = rs.getInt("que_int_count");
                que_prog_lang   = rs.getString("que_prog_lang");
                que_media_ind   = rs.getBoolean("que_media_ind");
				que_submit_file_ind = rs.getBoolean("que_submit_file_ind");
            }
            else {
            	stmt.close();
                throw new qdbException( "No data for module. id = " + res_id );
            }

            // JLAI 12/11/99:
            ints = dbInteraction.getQInteraction(con, que_res_id);

            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String asXML(Connection con, long order) throws qdbException, cwSysMessage {
        return asXML(con, order, false);
    }
    public String asXML(Connection con, long order, boolean isShuffleMCQue)
        throws qdbException ,cwSysMessage
    {
        String result = "";
        // format the question list
        result = "<question id=\""+ res_id + "\" order=\"" + order ;
        result += "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date +"\">" + dbUtils.NEWL;

        result += xmlContent(con, isShuffleMCQue);

        result += "</question>" + dbUtils.NEWL;

        return result;
    }


    // given a question id, get the score of that question
    public static int getScore(Connection con, long resId)
        throws qdbException
    {
        try {

          PreparedStatement stmt = con.prepareStatement(
            " SELECT que_score from Question where que_res_id = ? " );

          stmt.setLong(1, resId);
          ResultSet rs = stmt.executeQuery();
          int result = 0;
          if (rs.next()) {
            result = rs.getInt("que_score");

          }else {
        	stmt.close();
            throw new qdbException("Cannot get the score of the question.");
          }
          stmt.close();
          return result;
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void ins(Connection con, String[] robs, loginProfile prof, String res_type)
        throws qdbException, qdbErrMessage, SQLException
    {
         // Only Expert admin, admin, expert course designer can insert public resource
        //if (res_privilege.equalsIgnoreCase(RES_PRIV_CW)) {
        //    checkPublicResPermission(con, prof);
        //}


        ins(con,robs, res_type);
        /*AcResources acres = new AcResources(con);
        boolean read = acres.hasResPermissionRead(prof.current_role);
        boolean write = acres.hasResPermissionWrite(prof.current_role);
        boolean exec = acres.hasResPermissionExec(prof.current_role);
        dbResourcePermission.save(con,que_res_id,prof.usr_id,prof.current_role,read,write,exec);*/
        //dbResourcePermission.save(con,que_res_id,prof.usr_id,true,true,false);

         // Public Folder
       /* if (res_privilege.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
            // CW Admin Group
            // open resource for all
            //if(prof.isPublic)
            if (dbObjective.isPublicObjective(con, Long.parseLong(robs[0])))
                dbResourcePermission.save(con,que_res_id,0,null,true,false,false);
            // open resource for those within the organization
            else
                dbResourcePermission.save(con,que_res_id,prof.root_ent_id,null,true,false,false);
        }*/
    }

    // insert a question without inserting the acl
    public void ins(Connection con, String[] robs, String res_type)
        throws qdbException
    {

         try {

            this.res_type = res_type;
            res_subtype = que_type;

            // calls dbResource.ins()
            super.ins(con);
            // if ok.
            que_res_id = res_id;

            String html = "><html>";
            boolean bHTML = false;

            if (que_xml != null && que_xml.indexOf(html) >= 0 )
                bHTML = true;

            if (bHTML)
                que_prog_lang = QUE_LANG_HTML;

            boolean bObject = false;

            String object = "<object type";
            if (que_xml != null && que_xml.indexOf(object) >= 0 )
                bObject = true;

            if (bObject)
                que_media_ind = true;

            String clobNull = cwSQL.getClobNull();

            PreparedStatement stmt = con.prepareStatement(
            "INSERT INTO Question "
            + " ( que_res_id  "
            + " , que_xml "
            + " , que_score "
            + " , que_type "
            + " , que_int_count "
            + " , que_prog_lang "
            + " , que_media_ind , que_submit_file_ind ) VALUES "
            + " ( ?," + clobNull + ", ?, ?, ? , ?, ?, ?) ");

            PreparedStatement stmt3 = con.prepareStatement(
            "INSERT INTO Interaction "
            + " ( int_res_id "
            + " , int_order "
            + " , int_label "
            // << BEGIN for oracle migration!
            //+ " , int_xml_outcome "
            //+ " , int_xml_explain "
            + " , int_res_id_explain "
            + " , int_res_id_refer ) VALUES "
            //+ " ( ?, ?, ?," + clobNull + "," + clobNull + ", NULL, NULL ) ");
            + " ( ?, ?, ?, NULL, NULL ) ");
            // >> END

            PreparedStatement stmt4 = con.prepareStatement(
            "INSERT INTO ResourceObjective "
            + " ( rob_res_id "
            + " , rob_obj_id ) VALUES "
            + " ( ?, ? ) ");

            // insert question and interactions
            stmt.setLong(1, que_res_id);
            //stmt.setString(2, que_xml);
            stmt.setInt(2, que_score);
            stmt.setString(3, que_type);
            stmt.setInt(4, que_int_count);
            stmt.setString(5, que_prog_lang);
            stmt.setBoolean(6, que_media_ind);
			if( que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)
			||  que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2) ) {
				stmt.setBoolean(7, que_submit_file_ind);
			}else{
				stmt.setBoolean(7, false);
			}            
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1){
            	stmt3.close();
            	stmt4.close();
                con.rollback();
                throw new qdbException("Fails to insert Question");
            }
            else
            {
                // Update que_xml
                // for oracle clob
                String conditions = "que_res_id = " + que_res_id;
                String tableName = "Question";
                String[] colName = {"que_xml"};
                String[] colValue = {que_xml};
                cwSQL.updateClobFields(con, tableName, colName, colValue, conditions);

                // insert interactions
                for(int i=0; i<ints.size();i++) {
                    dbInteraction intObj = (dbInteraction) ints.elementAt(i);

                    stmt3.setLong(1, que_res_id);
                    stmt3.setInt(2, intObj.int_order);
                    stmt3.setString(3, intObj.int_label);
                    //stmt3.setString(4, intObj.int_xml_outcome);
                    //stmt3.setString(5, intObj.int_xml_explain);

                    if(stmt3.executeUpdate() != 1 ) {
                        // insert fails, rollback
                        stmt3.close();
                        stmt4.close();
                        con.rollback();
                        throw new qdbException("Fails to insert Interaction");
                    }

                    // Update int_xml_outcome, int_xml_explain
                    // construct the condition
                    String condition = "int_res_id = " + que_res_id +
                                       " AND int_order = " + intObj.int_order;
                    // construct the column & value
                    String[] columnName = new String[2];
                    String[] columnValue = new String[2];
                    columnName[0] = "int_xml_outcome";
                    columnValue[0] = intObj.int_xml_outcome;
                    columnName[1] = "int_xml_explain";
                    columnValue[1] = intObj.int_xml_explain;
                    cwSQL.updateClobFields(con, "Interaction", columnName, columnValue, condition);
                }  // for
                stmt3.close();
                // insert Resource Objective, NOTE that index starts from 0
                if (robs[0]!=null && robs.length > 0){
                    // for Survey question, no resourceObjective
                    if (!res_type.equals(dbResource.RES_TYPE_SUQ)){
                            for(int j=0;j<robs.length;j++) {
                                if (robs[j] == null) break;
                                stmt4.setLong(1, que_res_id);
                                stmt4.setLong(2, Long.parseLong(robs[j]));
                                if(stmt4.executeUpdate() != 1 ) {
                                // insert fails, rollback
                                    stmt4.close();
                                    con.rollback();
                                    throw new qdbException("Fails to insert ResourceObjective");
                                }
                            }  // for
                             // con.commit() at the caller function
                            stmt4.close();
                            return;
                    // for survey question, add resource Content
                    }else{
                        long modId = Long.parseLong(robs[0]);
                        updModIdTest(con, modId, que_res_id);

                        dbResourceContent resCon = new dbResourceContent();
                        resCon.rcn_res_id = modId;
                        resCon.rcn_res_id_content = que_res_id;
                        resCon.rcn_obj_id_content = 0;
                        if (!resCon.ins(con)) {
                        	stmt4.close();
                            con.rollback();
                            throw new qdbException("Failed to add survey question.");
                        }
                    }
                }
                //insert into ResourceContent if necessary
                if (this.res_mod_res_id_test > 0
                    && !res_type.equals(dbResource.RES_TYPE_SUQ)) {
                    dbResourceContent rcn = new dbResourceContent();
                    rcn.rcn_res_id = this.res_mod_res_id_test;
                    rcn.rcn_res_id_content = this.que_res_id;
                    //assume the question either has one objective or no objective
                    rcn.rcn_obj_id_content =
                        (robs != null
                            && robs[0] != null
                            && robs[0].length() > 0)
                            ? Long.parseLong(robs[0])
                            : 0;
                    rcn.rcn_score_multiplier = 1;
                    rcn.ins(con);

                    //update the score of the container if it is a question
                    try {
                        dbQuestion parentQue = new dbQuestion();
                        parentQue.que_res_id = this.res_mod_res_id_test;
                        parentQue.res_id = this.res_mod_res_id_test;
                        parentQue.get(con);
                        parentQue.updateTimeStamp(con);
                        if (parentQue.que_type.equals(RES_SUBTYPE_FSC)) {
                            parentQue.que_score += this.que_score;
                            parentQue.res_upd_user = this.res_upd_user;
                            parentQue.updScore(con);
                            udpModMaxScore(con, parentQue.que_res_id);
                        }
                    } catch (qdbException qdbe) {
                        //parent is not a question
                    } catch (cwSysMessage se) {
                        //parent is not a question
                    }
                }
            }
            if(stmt3 != null){
            	stmt3.close();
            }
            if(stmt4 != null){
            	stmt4.close();
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void updModIdTest(Connection con, long modId, long queId) throws qdbException{
        try{
            PreparedStatement stmt1 = con.prepareStatement(
                " UPDATE Resources "
            +  "   SET res_mod_res_id_test = ? "
            +  "  WHERE res_id  = ? " );

            stmt1.setLong(1,modId);
            stmt1.setLong(2,queId);

            if (stmt1.executeUpdate() != 1) {
                con.rollback();
                throw new qdbException("Failed to update the resouce.");
            }
            stmt1.close();
            return;
        }catch (SQLException e){
            throw new qdbException("SQL error:" + e.getMessage());
        }

    }

	// checkTimestamp
    public void upd(Connection con, String[] robs, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage {
        super.checkTimeStamp(con);
        res_upd_user = prof.usr_id;
        upd(con, robs);
    }

    public void upd(Connection con, String[] robs)
        throws qdbException, cwSysMessage {
        try {
            res_id = que_res_id;

            // Check whether the question belongs to a module
            dbQuestion dbque = new dbQuestion();
            dbque.que_res_id = que_res_id;
            dbque.get(con);

            res_subtype = que_type;
            this.res_type = dbque.res_type;
            super.upd(con);

            if(isQueAttempted(con))
                throw new cwSysMessage(SMESG_QUE_ATTEMPTED);

            String clobNull = cwSQL.getClobNull();

            String updSQL =
             " UPDATE Question "
            + "   SET "
            + "       que_xml  = " + clobNull
            + "     , que_score    = ? "
            + "     , que_type     = ? "
            + "     , que_int_count = ? "
            + "     , que_prog_lang = ? "
            + "     , que_media_ind = ? "
			+ " 	, que_submit_file_ind = ? "
            + " WHERE que_res_id = ? ";

            String  int_xmlo = "";
            String  int_xmle = "";

            boolean bHTML = false;
            boolean bObject = false;
            String html = "><html>";
            String object = "<object type";

            if (que_xml != null && que_xml.indexOf(html) >= 0 )
                bHTML = true;

            if (que_xml != null && que_xml.indexOf(object) >= 0 )
                bObject = true;

            if (bHTML)
                que_prog_lang = QUE_LANG_HTML;

            if (bObject)
                que_media_ind = true;


            PreparedStatement stmtU1 = con.prepareStatement(updSQL);

            // statement not used, please find a suitable place to close it
            // if this object is to be reused (2003-08-04 kawai)
            //PreparedStatement stmtC3 = con.prepareStatement(
            //"SELECT res_res_id_root FROM  Resources "
            //+ " WHERE res_id = ? " );

            PreparedStatement stmtU3 = con.prepareStatement(
            "DELETE From ResourceObjective "
            + " WHERE rob_res_id = ? " );

            PreparedStatement stmtU4 = con.prepareStatement(
            "INSERT INTO ResourceObjective "
            + " ( rob_res_id "
            + " , rob_obj_id ) VALUES "
            + " ( ?, ? ) ");


            // begin update
            //stmtU1.setString    (1, que_xml);
            stmtU1.setInt       (1, que_score);
            stmtU1.setString    (2, que_type);
            stmtU1.setInt       (3, que_int_count);
            stmtU1.setString    (4, que_prog_lang);
            stmtU1.setBoolean   (5, que_media_ind);
			if( que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) 
			||  que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
				stmtU1.setBoolean(6, que_submit_file_ind);
			}else {
				stmtU1.setNull(6, java.sql.Types.INTEGER);
			}
			stmtU1.setLong      (7, que_res_id);

            //int[] runRst = stmtU1.executeBatch();
            //if( runRst.length != 2 || runRst[0] != 1 || runRst[1] != 1)
            int u = stmtU1.executeUpdate();
            if( u != 1 )
            {
                // update fails, rollback
            	stmtU1.close();
                con.rollback();
                throw new qdbException("Fails to update Resource & Question.");
            }
            stmtU1.close();
            // Update que_xml
            // for oracle clob
            String conditions = "que_res_id = " + que_res_id;
            String tableName = "Question";
            String[] colName = {"que_xml"};
            String[] colValue = {que_xml};
            cwSQL.updateClobFields(con, tableName, colName, colValue, conditions);

//            stmtU1 = con.prepareStatement(
//                    " SELECT que_xml FROM Question "
//                + "     WHERE que_res_id = ? FOR UPDATE ",
//                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
//            stmtU1.setLong(1, que_res_id);
//            ResultSet rs = stmtU1.executeQuery();
//            if (rs.next())
//            {
//                cwSQL.setClobValue(con, rs, "que_xml", que_xml);
//                rs.updateRow();
//            }
//            stmtU1.close();



            // process interactions

            // Delete all the interation first because the number of iteraction may changed
            PreparedStatement stmtUI = con.prepareStatement(
            "DELETE From Interaction "
            + " WHERE int_res_id = ? " );

            stmtUI.setLong(1,que_res_id);
            stmtUI.executeUpdate();
            stmtUI.close();

            // Insert the new iteraction
            stmtUI = con.prepareStatement(
                "INSERT INTO Interaction "
                + " ( int_res_id "
                + " , int_order "
                + " , int_label "
                // << BEGIN for oracle migration!
                //+ " , int_xml_outcome "
                //+ " , int_xml_explain "
                + " , int_res_id_explain "
                + " , int_res_id_refer ) VALUES "
                //+ " ( ?, ?, ?," + clobNull + "," + clobNull +", NULL, NULL ) "
                + " ( ?, ?, ?, NULL, NULL ) "
                // >> END
            );

            dbInteraction di = new dbInteraction();
            for(int i=0; i<ints.size(); i++)
            {

                dbInteraction intObj = (dbInteraction) ints.elementAt(i);

                stmtUI.setLong(1, que_res_id);
                stmtUI.setInt(2, intObj.int_order);
                stmtUI.setString(3, intObj.int_label);
                //stmtUI.setString(4, intObj.int_xml_outcome);
                //stmtUI.setString(5, intObj.int_xml_explain);

                if(stmtUI.executeUpdate() != 1 ) {
                    // insert fails, rollback
                    stmtUI.close();
                    con.rollback();
                    throw new qdbException("Fails to insert Interaction");
                }

                // << BEGIN for oracle migration!
/*                PreparedStatement stmt = con.prepareStatement(
                    " SELECT int_xml_outcome, int_xml_explain FROM Interaction "
                + "     WHERE int_res_id = ? and int_order = ? FOR UPDATE ",
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
                stmt.setLong(1, que_res_id);
                stmt.setInt(2, intObj.int_order);

                ResultSet crs = stmt.executeQuery();
                if (crs.next())
                {
                    cwSQL.setClobValue(con, crs, "int_xml_outcome", intObj.int_xml_outcome);
                    cwSQL.setClobValue(con, crs, "int_xml_explain", intObj.int_xml_explain);
                    crs.updateRow();
                }
                stmt.close();
*/
                // Update int_xml_outcome, int_xml_explain
                // construct the condition
                String condition = "int_res_id = " + que_res_id +
                                   " AND int_order = " + intObj.int_order;
                // construct the column & value
                String[] columnName = new String[2];
                String[] columnValue = new String[2];
                columnName[0] = "int_xml_outcome";
                columnValue[0] = intObj.int_xml_outcome;
                columnName[1] = "int_xml_explain";
                columnValue[1] = intObj.int_xml_explain;
                cwSQL.updateClobFields(con, "Interaction", columnName, columnValue, condition);
            }
            stmtUI.close();

            // Check whether the question is the root
			// Modify the max score of the module / Container
			if(dbque.res_mod_res_id_test > 0) {
				String parentType = getResType(con, dbque.res_mod_res_id_test);
				if(parentType.equals(RES_TYPE_MOD)) {
				
                    dbModule dbmod = new dbModule();
                    dbmod.mod_res_id = dbque.res_mod_res_id_test;
                    dbmod.updMaxScore(con);
                    dbmod.updMaxScoreForChild(con);
                } else if (parentType.equals(RES_TYPE_QUE)) {
                    dbQuestion parentQue = new dbQuestion();
                    parentQue.que_res_id = dbque.res_mod_res_id_test;
                    parentQue.res_id = dbque.res_mod_res_id_test;
                    parentQue.res_upd_user = this.res_upd_user;
                    parentQue.get(con);
                    parentQue.updateTimeStamp(con);
                    if (parentQue.que_type.equals(RES_SUBTYPE_FSC)
                        && this.que_score != dbque.que_score) {

                        parentQue.que_score =
                            parentQue.que_score
                                + this.que_score
                                - dbque.que_score;
                        parentQue.updScore(con);
                        udpModMaxScore(con, parentQue.que_res_id);
                    }
                }
            }

            // Maintain interactions
            // dont' need to update resource objective for survey question
            if (robs[0]!=null && robs[0].length()>0 && Long.parseLong(robs[0]) > 0) {
                boolean needUpdRobs = true;
                if ((dbque.res_mod_res_id_test > 0)) {
                    String parentType = getResType(con, dbque.res_mod_res_id_test);
                    if(parentType.equalsIgnoreCase(RES_TYPE_QUE)){
                        dbQuestion parentQue = new dbQuestion();
                        parentQue.que_res_id = dbque.res_mod_res_id_test;
                        parentQue.res_id = dbque.res_mod_res_id_test;
                        parentQue.get(con);
                        if (parentQue.que_type.equals(dbQuestion.RES_SUBTYPE_FSC)
                        		|| parentQue.que_type.equals(dbQuestion.RES_SUBTYPE_DSC)) {
                        	needUpdRobs = false;
                        }
                    }
                }
                if (needUpdRobs) {
                    stmtU3.setLong(1, que_res_id);
                    stmtU3.executeUpdate();

                    for(int i=0;i<robs.length;i++)
                    {
                        if( robs[i]==null )
                            break;
                        stmtU4.setLong(1, que_res_id);
                        stmtU4.setLong(2, Long.parseLong(robs[i]));
                        if(stmtU4.executeUpdate() != 1 )
                        {
                            // update fails, rollback
                            con.rollback();
                            throw new qdbException("Fails to insert ResourceObjective.");
                        }
                    }
                }
            }
            
            //insert into QueContainer res_subtype
            if (dbque.res_subtype.equals(dbResource.RES_SUBTYPE_FSC) || dbque.res_subtype.equals(dbResource.RES_SUBTYPE_DSC)) {
                ViewQueContainer queContainer = new ViewQueContainer();
                queContainer.res_id = dbque.res_id;
                if (this.que_sc_shuffle) {
                    queContainer.qct_allow_shuffle_ind = 1;
                }
                queContainer.upd(con);
            }

            stmtU3.close();
            stmtU4.close();

            return;

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }
   }

    public static void udpModMaxScore(Connection con, long que_id) throws SQLException, qdbException, cwSysMessage {
        String sql = "select res_id, res_type from resources, resourcecontent " +                     " where rcn_res_id = res_id" +
                     " and rcn_res_id_content = ?";        
        long mod_res_id = 0;
        String res_type = null;
        PreparedStatement stmt = null;
        try {
            int idx = 1;
            stmt = con.prepareStatement(sql);
            stmt.setLong(idx++, que_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mod_res_id = rs.getLong("res_id");
                res_type = rs.getString("res_type");
                if (res_type.equals("MOD")) {
                    dbModule dbmod = new dbModule();
                    dbmod.mod_res_id = mod_res_id;
                    dbmod.updMaxScore(con);
                    dbmod.updMaxScoreForChild(con);
                }
            }
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public static void updQueLstStatus(Connection con, loginProfile prof, String[] que_id_lst, String status)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        try {
            for(int i =0;i< que_id_lst.length;i++) {
                dbQuestion dbque = new dbQuestion();
                dbque.que_res_id = Long.parseLong(que_id_lst[i]);
                dbque.res_id = dbque.que_res_id;
                dbque.res_status = status;
                dbque.res_upd_user = prof.usr_id;

                /*dbque.checkResPermission(con, prof);*/
                //if (!dbResourcePermission.hasPermission(con, dbque.que_res_id, prof,
                //                        dbResourcePermission.RIGHT_WRITE))
                //throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);

                dbque.updateStatus(con);
            }
            con.commit();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
   }

   public static void removeQueLst(Connection con, loginProfile prof, String[] que_id_lst)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        try {

            for(int i =0;i< que_id_lst.length;i++) {
                dbQuestion dbque = new dbQuestion();
                dbque.que_res_id = Long.parseLong(que_id_lst[i]);
                dbque.res_upd_user = prof.usr_id;

                /*dbque.checkResPermission(con, prof);*/
                //if (!dbResourcePermission.hasPermission(con, dbque.que_res_id, prof,
                //                        dbResourcePermission.RIGHT_WRITE))
                //throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);

                dbque.del(con);
            }
            con.commit();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
   }
   /*
    public void updateStatus(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        //try {
            if (!dbResourcePermission.hasPermission(con, que_res_id, prof,
                                    dbResourcePermission.RIGHT_WRITE))
            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            super.updateStatus(con);
        //} catch(SQLException e) {
        //    throw new qdbException("SQL Error: " + e.getMessage());
        //}
    }
    */
    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        res_id = que_res_id;
        res_upd_user = prof.usr_id ;
        /*
        dbQuestion dbque = new dbQuestion();
        dbque.que_res_id = que_res_id;
        dbque.get(con);
        res_type = dbque.res_type;

        // check User Right
        if (!res_type.equals(dbResource.RES_TYPE_SUQ)){
                    checkResPermission(con, prof);
        }
        */
        //if (!dbResourcePermission.hasPermission(con, que_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}

        super.checkTimeStamp(con);

        del(con);
    }

    public void del(Connection con)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try  {
            dbQuestion dbque = new dbQuestion();
            dbque.que_res_id = que_res_id;
            dbque.get(con);
            res_type = dbque.res_type;

            res_id = que_res_id;

            // delete access control list
            dbResourcePermission.delAll(con,que_res_id);

            if(!deleted_attempted_que_ind) {
	            // check if any stat exists
	            PreparedStatement stmt2 = con.prepareStatement(
	               " SELECT count(*) "
	            +  "   FROM ProgressAttempt "
	            +  "  WHERE atm_int_res_id = ? " );
	
	            // check if QSequence exists
	            PreparedStatement stmt3 = con.prepareStatement(
	                "SELECT count(qse_que_res_id) "
	            + "  FROM QSequence WHERE qse_que_res_id = ? " );
	
	            stmt2.setLong(1, que_res_id);
	            ResultSet rs2 = stmt2.executeQuery();
	            boolean bNoChildOk = false;
	            boolean bNotUsed = false;
	            if(rs2.next())
	            {
	                int cnt = rs2.getInt(1);
	                if (cnt==0)
	                    bNoChildOk = true;
	            }
	            stmt2.close();
	
	            stmt3.setLong(1, que_res_id);
	            ResultSet rs3 = stmt3.executeQuery();
	            if(rs3.next())
	            {
	                int cnt = rs3.getInt(1);
	                if (cnt==0)
	                    bNotUsed = true;
	            }
	            stmt3.close();
	
	            if(!bNoChildOk || !bNotUsed)
	                throw new cwSysMessage(SMESG_QUE_ATTEMPTED);
            }
/*
            // check if the question was duplicated
            PreparedStatement stmt4 = con.prepareStatement(
               "  SELECT count(*) "
            +  "  FROM Resources WHERE res_res_id_root = ? " );

            stmt4.setLong(1, que_res_id);
            ResultSet rs4 = stmt4.executeQuery();
            if(rs4.next())
            {
                int cnt = rs4.getInt(1);
                if (cnt!=0) {
                    //Question used by some other authors.
                    throw new qdbErrMessage(SMESG_QUE_USED_BY_OTHERS);
                }
            }
            stmt4.close();
*/

//if module contine FSC or DSC question,del it's content first
            if (dbque.que_type.equals(dbResource.RES_SUBTYPE_FSC)) {
                delSCContent(con);
            }else if (dbque.que_type.equals(dbResource.RES_SUBTYPE_DSC)) {
                delSCContent(con);
            }

            String sql_del_to_parent_relation = null;

            if (!res_type.equals(dbResource.RES_TYPE_SUQ)){
                sql_del_to_parent_relation = "DELETE From ResourceObjective where rob_res_id = ? ";
            }else{
                sql_del_to_parent_relation = "DELETE From ResourceContent where rcn_res_id_content = ? ";
            }

            PreparedStatement stmtD1 = con.prepareStatement(sql_del_to_parent_relation);

            stmtD1.setLong(1, que_res_id);

            // No resource objective record for those duplicated questions
            stmtD1.executeUpdate();
            stmtD1.close();

            if(deleted_attempted_que_ind) {
            	dbProgressAttempt.delByResId(con, que_res_id);
            }
            
            PreparedStatement stmtD2 = con.prepareStatement(
                " DELETE From Interaction where int_res_id = ? " );

            stmtD2.setLong(1, que_res_id);
            stmtD2.executeUpdate();
            stmtD2.close();

            stmtD2 = con.prepareStatement(
                    " DELETE From Question where que_res_id = ? " );

            stmtD2.setLong(1, que_res_id);
            stmtD2.executeUpdate();
            stmtD2.close();

            // Modify the max score of the module / Container
            if (dbque.res_mod_res_id_test > 0) {
                String parentType = getResType(con, dbque.res_mod_res_id_test);
                if (parentType.equals(RES_TYPE_MOD)) {
                    // do not understand why need not to update module's max score
                } else if (parentType.equals(RES_TYPE_QUE)||parentType.equals(RES_SUBTYPE_FSC)) {
                    dbQuestion parentQue = new dbQuestion();
                    parentQue.que_res_id = dbque.res_mod_res_id_test;
                    parentQue.res_id = dbque.res_mod_res_id_test;
                    parentQue.res_upd_user = this.res_upd_user;
                    parentQue.get(con);
                    parentQue.updateTimeStamp(con);
                    if (parentQue.que_type.equals(RES_SUBTYPE_FSC)) {
                        parentQue.que_score =
                            parentQue.que_score - dbque.que_score;
                        parentQue.updScore(con);
                        udpModMaxScore(con, parentQue.que_res_id);
                    }
                }
            }
            super.del(con);
            // con.commit() at the caller function
            return;

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

    }

    private void delSCContent(Connection con) throws SQLException, qdbException {
        String sql_del_sc_que_interaction = "delete from Interaction where int_res_id in (select res_id from resources where res_mod_res_id_test = ? )";
        PreparedStatement stmt = con.prepareStatement(sql_del_sc_que_interaction);
        stmt.setLong(1, que_res_id);
        stmt.executeUpdate();
        stmt.close();

        String sql_del_sc_que_question = "delete from Question where que_res_id in (select res_id from resources where res_mod_res_id_test = ? )";
        PreparedStatement stmt1 = con.prepareStatement(sql_del_sc_que_question);
        stmt1.setLong(1, que_res_id);
        stmt1.executeUpdate();
        stmt1.close();

        String sql_del_sc_que_obj = "delete from ResourceObjective where rob_res_id in (select res_id from resources where res_mod_res_id_test = ? )";
        PreparedStatement stmt2 = con.prepareStatement(sql_del_sc_que_obj);
        stmt2.setLong(1, que_res_id);
        stmt2.executeUpdate();
        stmt2.close();

        String sql_del_sc_que_res = "delete from Resources where res_id in (select res_id from resources where res_mod_res_id_test = ? )";
        PreparedStatement stmt3 = con.prepareStatement(sql_del_sc_que_res);
        stmt3.setLong(1, que_res_id);
        stmt3.executeUpdate();
        stmt3.close();
        
        String sql_del_sc_que = "delete from ResourceContent where rcn_res_id = ? ";
        PreparedStatement stmt4 = con.prepareStatement(sql_del_sc_que);
        stmt4.setLong(1, que_res_id);
        stmt4.executeUpdate();
        stmt4.close();

        String sql_del_dsc_spec = "DELETE FROM QueContainerSpec WHERE qcs_res_id = ?";
        PreparedStatement stmt5 = con.prepareStatement(sql_del_dsc_spec);
        stmt5.setLong(1, que_res_id);
        stmt5.executeUpdate();
        stmt5.close();
    }

    public String queAsXML(Connection con, loginProfile prof, String mod_type,String sylesheet)
        throws qdbException ,cwSysMessage, SQLException {
            return queAsXML(con, prof, mod_type, true,sylesheet);
        }

    public String queAsXML(Connection con, loginProfile prof, String mod_type, boolean xmlHeader,String curr_stylesheet)
        throws qdbException ,cwSysMessage, SQLException
    {
        // for now write out the Q part and I part:
        StringBuffer result = new StringBuffer();
		AcPageVariant acPageVariant = new AcPageVariant(con);
		AcObjective acObj = new AcObjective(con);
		dbObjective dbobj=new dbObjective();
		long objId = dbObjective.getResObjRootId(con,res_id);
		
		acPageVariant.ent_id = prof.usr_ent_id;
		acPageVariant.obj_id = objId;
		dbobj.obj_id=objId;
		try{
			dbobj.get(con);
		} catch(qdbException e) {
			CommonLog.error("no result!");
		}
		
		dbSyllabus syl = new dbSyllabus();
        if (dbobj.obj_syl_id > 0) {
            syl.syl_id = dbobj.obj_syl_id;   
            syl.get(con);
        }
		acPageVariant.rol_ext_id= prof.current_role;
		Hashtable xslQuestions=AcXslQuestion.getQuestions();
		//acPageVariant.admAccess = acObj.hasAdminPrivilege(prof.usr_ent_id,prof.current_role);
        String ATTEMPTED = "";
        try{
            PreparedStatement stmt = con.prepareStatement(
            "SELECT COUNT(atm_int_res_id) "
            + "  FROM ProgressAttempt where atm_int_res_id = ? "  );
            stmt.setLong(1, que_res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                int cnt = rs.getInt(1);

                if (cnt!=0)
                    ATTEMPTED = RES_ATTEMPTED_TRUE;
                else
                    ATTEMPTED = RES_ATTEMPTED_FALSE;
            }
            else
                throw new qdbException("Failed to get progress record.");

            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        String xmlOwner = "";
        if(res_usr_id_owner!=null && res_usr_id_owner.length()>0)
            xmlOwner = dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,res_usr_id_owner));

        // xml header
        if( xmlHeader )
        result.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL).append(dbUtils.NEWL);
        //result += "<!DOCTYPE questions SYSTEM 'c:\\temp\\question.dtd'>";

        // Q part,
        // format question header
        result.append("<question id=\"").append(que_res_id).append("\" language=\"").append(res_lan).append("\" mod_type=\"").append(mod_type).append("\" last_modified=\"").append(res_upd_user).append("\" timestamp=\"").append(res_upd_date);
        result.append("\" owner=\"").append(xmlOwner).append("\" attempted=\"").append(ATTEMPTED).append("\">").append(dbUtils.NEWL);
        // author's information
        
		if (prof!=null){
	        result.append(prof.asXML()).append(dbUtils.NEWL);
	        //page variant
			String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(curr_stylesheet));
	        result.append(metaXML);
	       
	        if (res_mod_res_id_test > 0){
	        	result.append(dbResourcePermission.aclAsXML(con,res_mod_res_id_test,prof));
	        }
	        else
	            result.append(dbResourcePermission.aclAsXML(con,res_id,prof));
		}
		
        result.append(xmlContent(con));
        
        result.append("<context_path>"+ ContextPath.getContextPath() +"</context_path>");
      	result.append("<shared>").append(dbobj.obj_share_ind).append("</shared>").append(dbUtils.NEWL);
              result.append("<syllabus id=\"").append(syl.syl_id)
                        .append("\" privilege=\"").append(syl.syl_privilege)
                        .append("\" locale=\"").append(syl.syl_locale)
                        .append("\" root_ent_id=\"").append(syl.syl_ent_id_root)
                        .append("\">").append(dbUtils.NEWL);
              result.append("<desc>").append(dbUtils.esc4XML(syl.syl_desc)).append("</desc>").append(dbUtils.NEWL)
                    .append("</syllabus>").append(dbUtils.NEWL);
        result.append("</question>").append(dbUtils.NEWL);

        
        
        return result.toString();
    }

    private String xmlContent(Connection con) throws qdbException, cwSysMessage {
        return xmlContent(con, false);
    }
    private String xmlContent(Connection con, boolean isShuffleMCQue) throws qdbException, cwSysMessage
    {
        String result= "";

		result += "<header difficulty=\"" + res_difficulty + "\" duration=\"" + res_duration
			 + "\" privilege=\"" + res_privilege + "\" status=\"" + res_status 
			 + "\" type=\"" + que_type + "\" language=\"" + res_lan + "\" score=\"" + que_score + "\"";
		 if( que_submit_file_ind ) {
			 result += " que_submit_file_ind=\"Y\" ";
		 }else{
			 result += " que_submit_file_ind=\"N\" ";
		 }
		 result += " >" + dbUtils.NEWL;
		 String text = "";
		
		 if(que_type.equalsIgnoreCase("FB")){   //问卷调查的问答题
			 /*
        	 String xmlStr =que_xml;
        	 org.dom4j.Document document;
             try {
                 document = DocumentHelper.parseText(xmlStr);
                 text = document.getStringValue();
             } catch (DocumentException e) {
                 e.printStackTrace();
             }
             
             */
        }else if(que_type.equalsIgnoreCase("MC")){   //问卷调查的选择题
        	 /*
        	String xml = que_xml;
        	 org.dom4j.Document doc;
             try {
            	 org.dom4j.Element et = null;  
                 doc = (org.dom4j.Document) DocumentHelper.parseText(xml);
                 org.dom4j.Element root =  doc.getRootElement();  
                 List<Element> jiedian = root.elements();  
                 for (int a = 0; a < jiedian.size(); a++) {
                     et = (org.dom4j.Element) jiedian.get(a);
                     if(et.getName().equalsIgnoreCase("html")){
                    	 text=et.getTextTrim();
                     }
                 }
             } catch (DocumentException e) {
                 e.printStackTrace();
             }  
             */
        	 
        	
        }
		 /*
		 result += "<title>" + dbUtils.esc4XML(text) + "</title>" + dbUtils.NEWL;
         result += "<desc>" + dbUtils.esc4XML(res_desc) + "</desc>" + dbUtils.NEWL;
         */
		// 根据题目ID查询题目的标题和简介
		Map<String,String> resMap = this.getSubjectInfo(con, que_res_id);
     	result += "<title>" + dbUtils.esc4XML(resMap.get("res_title")) + "</title>" + dbUtils.NEWL;
        result += "<desc>" + dbUtils.esc4XML(resMap.get("res_desc")) + "</desc>" + dbUtils.NEWL;

        
        String objXmlTemp = super.objAsXML(con);
        if (objXmlTemp.length() == 0) {
            result += super.scQueObjAsXML(con);
        } else {
            result += objXmlTemp;
        }
        result += "</header>" + dbUtils.NEWL + dbUtils.NEWL;

        if (!(que_type.equals(dbResource.RES_SUBTYPE_FSC) || que_type.equals(dbResource.RES_SUBTYPE_DSC))) {
            // format body
            Hashtable body = parseQueXml(que_xml);
            if (isShuffleMCQue && body.containsKey("isShuffle") && body.get("isShuffle").equals("Y")) {
                String bodyContent = que_xml.substring(0, que_xml.indexOf("<option"));
                result += rebuildQueXml(bodyContent, (ArrayList)body.get("optionText"));
                result += que_xml.substring(que_xml.indexOf("</interaction>"));
            } else {
                result += que_xml + dbUtils.NEWL;
            }
        }
        else {
            result += que_xml + dbUtils.NEWL;
        }
        //creation details
        dbRegUser creator = new dbRegUser();
        creator.get(con, res_usr_id_owner);
        result += "<creation>";
        result += "<user id=\"" + creator.usr_ste_usr_id + "\"";
        result += " ent_id=\"" + creator.usr_ent_id + "\">";
        result += "<display_bil>" + cwUtils.esc4XML(creator.usr_display_bil) + "</display_bil>";
        result += "</user>";
        result += "<timestamp>" + this.res_create_date + "</timestamp>";
        result += "</creation>";
        
        //last update details
        dbRegUser lastAuthor = new dbRegUser();
        lastAuthor.get(con, this.res_upd_user);
        result += "<last_update>";
        result += "<user id=\"" + lastAuthor.usr_ste_usr_id + "\"";
        result += " ent_id=\"" + lastAuthor.usr_ent_id + "\">";
        result += "<display_bil>" + cwUtils.esc4XML(lastAuthor.usr_display_bil) + "</display_bil>";
        result += "</user>";
        result += "<timestamp>" + this.res_upd_date + "</timestamp>";
        result += "</last_update>";

        Vector intObj = ints;
        String oxml = "";
        String exml = "";

        dbInteraction dbi = new dbInteraction();
        for (int i=0;i<intObj.size();i++) {
            dbi  = (dbInteraction) intObj.elementAt(i);
            oxml += "<label>" + dbi.int_label + "</label>";
            oxml += dbi.int_xml_outcome;
            exml += dbi.int_xml_explain;
        }

        result += oxml + dbUtils.NEWL;

        if (exml==null) {
            exml = "";
        }
        result += exml + dbUtils.NEWL;

        return result;
    }
    
    /**
     * ‘课程评估创建-题目管理’根据题目ID查询题目的标题和简介
     * @param con 
     * @param res_id
     * @return 结果
     * @throws qdbException
     */
    private Map<String,String> getSubjectInfo(Connection con, long res_id) throws qdbException {
    	PreparedStatement stmt = null;
    	String sql = "select res_title,res_desc from Resources where res_id= ? ";
        try {
        	stmt = con.prepareStatement(sql);
        	stmt.setLong(1, res_id);
        	ResultSet rs = stmt.executeQuery();
        	
        	Map<String,String> resMap = new HashMap<String,String>();

            while (rs.next()) {
            	resMap.put("res_title", rs.getString("res_title"));
            	resMap.put("res_desc", rs.getString("res_desc"));
            }
            
            return resMap;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        } finally {
        	 try {
				stmt.close();
			} catch (SQLException e) {
				throw new qdbException("SQL Error: " + e.getMessage());
			}
        }
    }
    
    /**
     * @param que_xml
     */
    private Hashtable parseQueXml(String que_xml) throws qdbException {
        Hashtable bodyContent = new Hashtable();
        ArrayList optionList = new ArrayList();
        ArrayList orgOptionList = new ArrayList();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document docXml = null;
        try {
            builder = factory.newDocumentBuilder();
            if (que_xml != null && que_xml.length() > 0) {
                StringReader rd = new StringReader(que_xml);
                InputSource in = new InputSource(rd);
                docXml = builder.parse(in);
            }
        }
        catch (ParserConfigurationException e) {
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        }
        catch (SAXException e) {
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        }
        catch (IOException e) {
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        }
        Element rootElement = docXml.getDocumentElement();
        NodeList nodeList = rootElement.getChildNodes();

        NodeList interaction = rootElement.getElementsByTagName("interaction");
        Element interActionEle = (Element)interaction.item(0);
        if (!interActionEle.getAttributeNode("type").getValue().equals(QUE_TYPE_MULTI)) {
            return bodyContent;
        }
        String shuffle = interActionEle.getAttributeNode("shuffle").getValue();
        NodeList options = interaction.item(0).getChildNodes();
        String nodeValue = null;
        for (int i = 0; i < options.getLength(); i++) {
            StringBuffer optionContent = new StringBuffer();
            Node option = options.item(i);
            Element optionEle = (Element)options.item(i);
            if (getNodeByName(option.getChildNodes(), "html") != null) {
                optionContent.append("<option id=\"").append(i + 1).append("\"><html>").append(cwUtils.esc4XML(option.getFirstChild().getFirstChild().getNodeValue())).append("</html>");
                if (getNodeByName(option.getChildNodes(), "object") != null) {
        			NodeList object = optionEle.getElementsByTagName("object");
        			Element objectEle = (Element)object.item(0);
        			optionContent.append("<object type =\"").append(cwUtils.esc4XML(objectEle.getAttribute("type")));
        			optionContent.append("\" data=\"").append(cwUtils.esc4XML(objectEle.getAttribute("data"))).append("\"/>");
        		}
        		optionContent.append("</option>");
                nodeValue = optionContent.toString();
            }
            else {
		optionContent.append("<option id=\"").append(i + 1).append("\">").append(cwUtils.esc4XML(option.getFirstChild().getNodeValue()));
		if (getNodeByName(option.getChildNodes(), "object") != null) {
			NodeList object = optionEle.getElementsByTagName("object");
			Element objectEle = (Element)object.item(0);
			optionContent.append("<object type =\"").append(cwUtils.esc4XML(objectEle.getAttribute("type")));
			optionContent.append("\" data=\"").append(cwUtils.esc4XML(objectEle.getAttribute("data"))).append("\"/>");
		}
		optionContent.append("</option>");
                String nodeName = option.getFirstChild().getNodeName();
                nodeValue = optionContent.toString();
            }
            optionList.add(nodeValue);
            orgOptionList.add(nodeValue);
        }
        bodyContent.put("isShuffle", shuffle);
        bodyContent.put("optionText", optionList);
        bodyContent.put("orgOptionText", orgOptionList);
        return bodyContent;
    }

    /**
     * @param nodeList
     * @param string
     * @return
     */
    private Node getNodeByName(NodeList nodeList, String nodeName) {
        Node result = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals(nodeName)) {
                result = nodeList.item(i);
                break;
            }
        }
        return result;
    }

    /**
     * @param list
     * @param string
     * @param list2
     */
    private String rebuildQueXml(String bodyText, ArrayList optionList) {
        StringBuffer body = new StringBuffer();
        Random randomIndex = new Random();
        body.append(bodyText);
        int count = optionList.size();
        String option_text; 
        String answer;
        for (int i = 0; i < count; i++) {
            int index = randomIndex.nextInt(optionList.size());
            option_text = (String)optionList.get(index);
            answer = option_text.substring(option_text.indexOf("\">") + 2, option_text.indexOf("</option>"));
            body.append(option_text.substring(0, option_text.indexOf("\">") + 2))
                .append(answer).append("</option>");
            optionList.remove(index);
        }
        return body.toString();
    }

    public String passThruAsXML(Connection con, loginProfile prof, long objId, String mod_type) throws qdbException, SQLException, cwSysMessage {
        if(mod_type != null) {
	    	if (mod_type.equals(dbResource.RES_SUBTYPE_DSC) || mod_type.equals(dbResource.RES_SUBTYPE_FSC)) {
	            //if a module include this sc question has been Visited,sub question can't be add
	            if (DynamicScenarioQue.mod_is_visited(con, res_mod_res_id_test)) {
	                throw new cwSysMessage("MOD004");
	            }
	        }
        }

        String result;
        result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;


        result += "<question_form type=\"" + que_type + "\" language=\"" + res_lan + "\" mod_type=\"" + mod_type + "\">" + dbUtils.NEWL;
        // author's information
        result += prof.asXML() + dbUtils.NEWL;
       
        if (objId > 0) {
        	 dbObjective dbobj=new dbObjective();
             dbobj.obj_id=objId;
             try{
            	 dbobj.get(con);
             } catch(qdbException e){
            	 CommonLog.error("no result!");
             }
            result += "<objective id=\"" + dbobj.obj_id + "\">"+
            		   "<desc>"+cwUtils.esc4XML(dbobj.obj_desc)+"</desc>"+
            		   dbobj.getObjPathAsXML(con)+
            		   "</objective>" + 
            		   dbUtils.NEWL;
        }
        if (this.res_mod_res_id_test > 0) {
            dbResource parentRes = new dbResource();
            parentRes.res_id = this.res_mod_res_id_test;
            parentRes.get(con);
            result += "<container id=\"" + parentRes.res_id + "\"" + " type=\"" + parentRes.res_type + "\"" + " subtype=\"" + parentRes.res_subtype + "\"" + "></container>";
        }

        // hardcode
        result += "<privilege>" + dbUtils.NEWL;
        result += "<option label=\"Personal\">AUTHOR</option>" + dbUtils.NEWL;
        result += "<option label=\"CyberWisdom\">CW</option>" + dbUtils.NEWL;
        result += "</privilege>" + dbUtils.NEWL;
        // end
    	result += "<context_path>"+ ContextPath.getContextPath() +"</context_path>";
        result += "</question_form>" + dbUtils.NEWL;

        return result;
    }

    public static long insTestQ(Connection con, long modId, long queId, long objId, String regUser) throws qdbException, cwSysMessage {
        return insTestQ(con, modId, queId, objId, regUser, null, null, true);
    }

    public static long insTestQ(Connection con, long modId, long queId, long objId, String regUser, String uploadDir, loginProfile prof, boolean boolWithChildObj) throws qdbException, cwSysMessage {
        try {
            dbQuestion dbque = new dbQuestion();
            dbque.que_res_id = queId;
            dbque.res_id = queId;
            dbque.get(con);

            if (prof != null && uploadDir != null && dbque.res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)) {
                FixedScenarioQue myParentFSC = new FixedScenarioQue();
                myParentFSC.res_id = queId;
                myParentFSC.get(con);
                //if fixed scenario question has no subquestion then this fsc can not be added
                if (myParentFSC.getChildQueId(con).size() <= 0) {
                    dbque.que_res_id = -1;
                    String res_str = "\"" + myParentFSC.res_title + "(" + myParentFSC.res_id + ")" + "\"";
                    throw new cwSysMessage("MSP003", res_str, "subquestion not enough");
                } else {
                    FixedScenarioQue myNewFSC = null;
                    try {
                        myNewFSC = myParentFSC.cloneMySelf(con, prof, objId, uploadDir, boolWithChildObj);
                    } catch (qdbErrMessage e) {
                        throw new qdbException(e.toString());
                    }

                    dbque.que_res_id = myNewFSC.res_id;
                    dbque.res_id = myNewFSC.res_id;
                }
            } else if (prof != null && uploadDir != null && dbque.res_subtype.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                DynamicScenarioQue myParentDSC = new DynamicScenarioQue();
                myParentDSC.res_id = queId;
                myParentDSC.get(con);
                if (myParentDSC.getChildQueId(con).size() <= 0) {
                    dbque.que_res_id = -1;
                    String res_str = "\"" + myParentDSC.res_title + "(" + myParentDSC.res_id + ")" + "\"";
                    throw new cwSysMessage("MSP003", res_str, "subquestion not enough");
                } else {
                    DynamicScenarioQue myNewDSC = null;
                    try {
                        myNewDSC = myParentDSC.cloneMySelf(con, prof, objId, uploadDir, boolWithChildObj);
                    } catch (qdbErrMessage e) {
                        throw new qdbException(e.toString());
                    }

                    dbque.que_res_id = myNewDSC.res_id;
                    dbque.res_id = myNewDSC.res_id;
                }
            } else {

                dbque.res_upd_user = regUser;
                dbque.res_usr_id_owner = regUser;
                dbque.res_privilege = RES_PRIV_AUTHOR;

                // empty resource objective
                String[] robs = new String[1];
                // objId is 0 for survey type of question
                if (objId > 0) {
                    robs[0] = Long.toString(objId);
                }
                dbque.ins(con, robs, dbque.res_type);
            }

            PreparedStatement stmt1 = con.prepareStatement(" UPDATE Resources " + "   SET res_res_id_root = ? , " + "       res_mod_res_id_test = ? " + "  WHERE res_id  = ? ");

            stmt1.setLong(1, queId);
            stmt1.setLong(2, modId);
            stmt1.setLong(3, dbque.res_id);

            if (stmt1.executeUpdate() != 1) {
                con.rollback();
                throw new qdbException("Failed to update the resouce.");
            }
            stmt1.close();
            return dbque.que_res_id;

        } catch (SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
            }
        }

	//parent_id > 0 is checking scenario sub-question.
	public static long duplicateDynQ(Connection con, long queId, String uploadDir, Vector isNewVec, long parent_id, boolean checkExisting )
        throws qdbException, cwSysMessage
    {
        try {
            dbQuestion dbque  = new dbQuestion();
            dbQuestion dbque2  = new dbQuestion();

            dbque.que_res_id = queId;
            dbque.res_id = queId;
            dbque.get(con);

            dbque2.que_res_id = queId;
            dbque2.res_id = queId;
            dbque2.get(con);
			Vector robVec = new Vector();
			//Do not update ResourceContent And add ResourceObjective same as parent.
			if( parent_id > 0 ) {
				dbque2.res_mod_res_id_test = 0;
				robVec = dbResourceObjective.getObjId(con, parent_id);
			} else {
				robVec = dbResourceObjective.getObjId(con, queId);
			}

            String[] robs = new String[1];
//            Vector robVec = dbResourceObjective.getObjId(con, queId);
            int i ;
            for(i=0;i<robVec.size(); i++) {
                robs[i] = Long.toString(((dbResourceObjective) robVec.elementAt(i)).rob_obj_id);

            }

            PreparedStatement stmt = con.prepareStatement(
                " SELECT res_id  FROM Resources "
            +  "   WHERE res_res_id_root = ? "
            +  "       and res_mod_res_id_test is null "
            +  "       and res_upd_date = ?  " );

            stmt.setLong(1,dbque.res_id);
            stmt.setTimestamp(2,dbque.res_upd_date);

            ResultSet rs = stmt.executeQuery();
            long newQueId = 0;
			if (rs.next() && checkExisting ) {
                newQueId = rs.getLong("res_id");
				if( isNewVec != null){
					isNewVec.addElement(new Boolean(false)); 
				}
            }else {

                dbque2.ins(con,robs, dbResource.RES_TYPE_QUE);

                PreparedStatement stmt1 = con.prepareStatement(
                    " UPDATE Resources "
                +  "   SET res_res_id_root = ? , "
                +  "       res_upd_date = ? "
                +  "  WHERE res_id  = ? " );

                stmt1.setLong(1,dbque.res_id);
                stmt1.setTimestamp(2,dbque.res_upd_date);
                stmt1.setLong(3,dbque2.que_res_id);

                if (stmt1.executeUpdate() != 1) {
                    stmt1.close();
                    con.rollback();
                    throw new qdbException("Failed to update the resouce.");
                }
                stmt1.close();
                dbUtils.copyMediaFrom(uploadDir, dbque.res_id, dbque2.res_id);

                 newQueId = dbque2.que_res_id ;
				if( isNewVec != null ) {
				   isNewVec.addElement(new Boolean(true));                 	
				}

            }
            stmt.close();
            return newQueId;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static long mirrorQue(Connection con, dbQuestion dbque, String[] robs, String uploadDir)
        throws qdbException, cwSysMessage
    {
        try {
            long srcId = dbque.que_res_id;

            dbque.res_res_id_root = dbque.res_id;
            dbque.ins(con, robs, dbResource.RES_TYPE_QUE);
            long newId = dbque.que_res_id;

            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Resources "
            +  "   SET res_res_id_root = ?  "
            +  "  WHERE res_id  = ? " );

            stmt.setLong(1,dbque.res_res_id_root);
            stmt.setLong(2,newId);

            if (stmt.executeUpdate() != 1) {
                con.rollback();
                throw new qdbException("Failed to duplicate question.");
            }
            stmt.close();

            dbUtils.copyMediaFrom(uploadDir, srcId, newId);

            return newId;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    private static final String SQL_IS_QUE_ATTEMPTED
        = " SELECT count(*) "
        +  "   FROM ProgressAttempt "
        +  "  WHERE atm_int_res_id = ? ";
    public boolean isQueAttempted(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        boolean result = true;
        try {
            stmt = con.prepareStatement(SQL_IS_QUE_ATTEMPTED);
            stmt.setLong(1, que_res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                if (rs.getInt(1) == 0) {
                    result = false;
                } 
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return result;
    }

	private static final String sql_upd_que_score 
		   = " Update Question set que_score = ? "
		   + " Where que_res_id = ? ";

    public void updScore(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql_upd_que_score);
            stmt.setLong(1, this.que_score);
            stmt.setLong(2, this.que_res_id);
            stmt.execute();
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return;
    }


    /**
    	* Get container question id. If not a scenario child question, return 0.
    	* @param con
    	* @return Question id.
    	* @throws SQLException
    	*/
    public long getContainerQueId(Connection con) throws SQLException {
        String SQL = "Select rcn_res_id " + "From ResourceContent, Question " + "Where rcn_res_id_content = ? " + "And rcn_res_id = que_res_id";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, this.que_res_id);
        ResultSet rs = stmt.executeQuery();
        long parent_que_id = 0;
        if (rs.next()) {
            parent_que_id = rs.getLong("rcn_res_id");
        }
        stmt.close();
        return parent_que_id;
    }

    //for sc_question self copy
    public boolean updQueContainerOrder(Connection con, long container_id, long new_que_id, long newQueOrder) throws SQLException {
        String SQL = "update ResourceContent set rcn_order = ? where rcn_res_id = ? and rcn_res_id_content = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        boolean success = true;
        int index = 1;
        stmt.setLong(index++, newQueOrder);
        stmt.setLong(index++, container_id);
        stmt.setLong(index++, new_que_id);
        if(stmt.executeUpdate() != 1) {
            success = false;
        }
        stmt.close();
        return success;
    }

    public static boolean queIDExist(Connection con, long que_res_id, String que_type) throws SQLException {
        String sql = "SELECT que_res_id from Question where que_res_id = ? AND que_type = ? ";        boolean idExist = false;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, que_res_id);
            stmt.setString(2, que_type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idExist = true;
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return idExist;
    }

    public static boolean subQueIDExist(Connection con, long que_res_id, String que_type) throws SQLException {
        String sql = "select count(*) from question, resources sub_res, resources parent_res " +                    " where que_res_id = sub_res.res_id " +                    " and sub_res.res_mod_res_id_test = parent_res.res_id " +                    " and (parent_res.res_subtype = ? or parent_res.res_subtype = ? ) " +
                    " and que_res_id = ? " +                    " and que_type = ? ";

        boolean idExist = false;
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(sql);
        try {
            int index = 1;
            stmt.setString(index++, RES_SUBTYPE_FSC);
            stmt.setString(index++, RES_SUBTYPE_DSC);
            stmt.setLong(index++, que_res_id);
            stmt.setString(index++, que_type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    idExist = true;
                }
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return idExist;
    }

//when import a question,if it has a parent,check if the parent's id is same as the old one.
    public boolean isSameParent(Connection con, long new_parent_id) throws SQLException {
        boolean sameParent = false;
        String sql = "select res_mod_res_id_test from resources where res_id = ? ";
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(sql);
        try {
            int index = 1;
            stmt.setLong(index++, this.que_res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getLong(1) == new_parent_id) {
                    sameParent = true;
                }
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return sameParent;
    }

    public static boolean isQueInTst(Connection con, long res_id) throws SQLException {
        boolean inTst = false;
        String sql = "select res_subtype from Resources where res_id = (select res_mod_res_id_test from Resources where res_id = ? )";
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(sql);
        try {
            int index = 1;
            stmt.setLong(index++, res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String parentType = rs.getString(1);
                if (parentType.equals(dbResource.RES_SUBTYPE_FAS) 
                    || parentType.equals(dbModule.MOD_TYPE_TST)) {
                        inTst = true;
                    }
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return inTst;
    }

    public static boolean IsQueInSc(Connection con, long res_id) throws SQLException {
        boolean inSc = false;
        String sql = " select res_id from resources where res_id = (select rcn_res_id from resourceContent where rcn_res_id_content = ?) " +                     " and (res_subtype = ? or res_subtype = ? )";
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(sql);
        try {
            int index = 1;
            stmt.setLong(index++, res_id);
            stmt.setString(index++, RES_SUBTYPE_FSC);
            stmt.setString(index++, RES_SUBTYPE_FSC);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                inSc = true;
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return inSc;
    }
    
    /*
    public static void delTestQ(Connection con, long queId, String regUser)
        throws qdbException , qdbErrMessage
    {
        try {
            // get the timestamp of the record
            PreparedStatement stmt = con.prepareStatement(
                " SELECT res_upd_date "
            +  "   FROM Resources "
            +  "  WHERE res_id = ? " );

            stmt.setLong(1,queId);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                dbQuestion dbque = new dbQuestion();
                dbque.que_res_id = queId;
                dbque.res_id = queId;
                dbque.res_upd_user = regUser;
                dbque.res_upd_date = rs.getTimestamp("res_upd_date");
                dbque.del(con);
            }
            return;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    */
    

    
    public static String getQueAsXML_test(Connection con, String uploadDir, Vector qArray, Vector resIdVec,String mod_type, boolean isShuffleMCQue, long tkh_id, long mod_id, ExportController controller)
    throws SQLException,qdbException ,cwSysMessage, qdbErrMessage
    {
        PreparedStatement stmt = null;
        StringBuffer qList = new StringBuffer("");
        Hashtable ht_order = new Hashtable();
        try {
            
            dbQuestion dbq ;
            Hashtable HS_Ques = new Hashtable();
            long QueId ;
            StringBuffer temp_sql = new StringBuffer("(");
            for(int i = 0; i < qArray.size(); i++){
            	ht_order.put(qArray.elementAt(i), new Long(i + 1));
                temp_sql.append(qArray.elementAt(i).toString()).append(",");
            }
            temp_sql.append("0)");
            
            StringBuffer SQL = new StringBuffer();
            SQL.append("select ");
            SQL.append(" curQue.res_id, curQue.res_lan ,curQue.res_title ,curQue.res_desc ,curQue.res_type ,curQue.res_subtype ,curQue.res_annotation ,curQue.res_format ,curQue.res_difficulty ,curQue.res_duration ,curQue.res_privilege ,curQue.res_status ,curQue.res_usr_id_owner ,curQue.res_create_date ,curQue.res_tpl_name ,curQue.res_res_id_root ,curQue.res_mod_res_id_test ,curQue.res_upd_user ,curQue.res_upd_date ,curQue.res_src_type ,curQue.res_src_link, curQue.res_instructor_name, curQue.res_instructor_organization, ");
            SQL.append(" Question.que_xml, Question.que_score, Question.que_type, Question.que_int_count, Question.que_prog_lang, Question.que_media_ind, Question.que_submit_file_ind,");
            SQL.append(" Interaction.int_res_id, Interaction.int_label, Interaction.int_order, Interaction.int_xml_outcome,  Interaction.int_xml_explain, Interaction.int_res_id_explain, Interaction.int_res_id_refer,");
            SQL.append(" childQue.res_id as res_id_child");
            SQL.append(" from Resources curQue inner join Question on (curQue.res_id = que_res_id)");
            SQL.append(" left join Interaction on (curQue.res_id = int_res_id)");
            SQL.append(" left join Resources childQue on (curQue.res_id = childQue.res_res_id_root and childQue.res_mod_res_id_test is null and childQue.res_upd_date = curQue.res_upd_date)");
            SQL.append(" where curQue.res_id in ").append(temp_sql.toString());
            //SQL.append(" order by Question.que_type, curQue.res_id, Interaction.int_order");
            stmt = con.prepareStatement(SQL.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QueId = rs.getLong("res_id");
                Long QueId_obj = new Long(QueId);
                if(!HS_Ques.containsKey(QueId_obj)){
                    dbq = new dbQuestion();
                    HS_Ques.put(QueId_obj,dbq);
                    dbq.res_id = QueId;
                    dbq.que_res_id = QueId;
                    dbq.res_lan = rs.getString("res_lan");
                    dbq.res_title = rs.getString("res_title");
                    dbq.res_desc = rs.getString("res_desc");
                    dbq.res_type = rs.getString("res_type");
                    dbq.res_subtype = rs.getString("res_subtype");
                    dbq.res_annotation = rs.getString("res_annotation");
                    dbq.res_format = rs.getString("res_format");
                    dbq.res_difficulty = rs.getInt("res_difficulty");
                    dbq.res_duration = rs.getFloat("res_duration");
                    dbq.res_privilege = rs.getString("res_privilege");
                    dbq.res_status = rs.getString("res_status");
                    dbq.res_usr_id_owner = rs.getString("res_usr_id_owner");
                    dbq.res_create_date = rs.getTimestamp("res_create_date");
                    dbq.res_tpl_name = rs.getString("res_tpl_name");
                   // dbq.res_res_id_root = rs.getLong("curQue.res_res_id_root");
                    dbq.res_mod_res_id_test = mod_id;
                    dbq.res_upd_user = rs.getString("res_upd_user");
                    dbq.res_upd_date = rs.getTimestamp("res_upd_date");
                    dbq.res_src_type = rs.getString("res_src_type");
                    dbq.res_src_link = rs.getString("res_src_link");
                    dbq.res_instructor_name = rs.getString("res_instructor_name");
                    dbq.res_instructor_organization = rs.getString("res_instructor_organization");
                    dbq.que_xml         = cwSQL.getClobValue(rs, "que_xml");
                    dbq.que_score       = rs.getInt("que_score");
                    dbq.que_type        = rs.getString("que_type");
                    dbq.que_int_count   = rs.getInt("que_int_count");
                    dbq.que_prog_lang   = rs.getString("que_prog_lang");
                    dbq.que_media_ind   = rs.getBoolean("que_media_ind");
                    dbq.que_submit_file_ind = rs.getBoolean("que_submit_file_ind");
                    dbq.res_res_id_child= rs.getLong("res_id_child");
                   
                }else{
                    dbq = (dbQuestion)HS_Ques.get(QueId_obj);
                }
                if( !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) && !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC) ) {
                    dbInteraction intObj = new dbInteraction();
                    intObj.int_res_id = rs.getLong("int_res_id");
                    intObj.int_label = rs.getString("int_label");
                    intObj.int_order = rs.getInt("int_order");
                    intObj.int_xml_outcome  = cwSQL.getClobValue(rs, "int_xml_outcome");
                    intObj.int_xml_explain  = cwSQL.getClobValue(rs, "int_xml_explain");
                    intObj.int_res_id_explain   = rs.getLong("int_res_id_explain");
                    intObj.int_res_id_refer     = rs.getLong("int_res_id_refer");
                    dbq.ints.add(intObj);
                }
            }
            Enumeration iter_Ques = HS_Ques.keys();
            int cnt = qArray.size();
            // in order to make sure the progress reach 100% only when this cmd ends,
            // the TotalRow is set as cnt+1 and the last row is to be added at the end of this cmd
            // (2007-03-12 kawai)
            if (controller != null) {
            	controller.setTotalRow(cnt + 1);
            }
            for(int i1 = 0; i1 < qArray.size() && (controller == null ||(controller != null && !controller.isCancelled())); i1++){
                //dbq = (dbQuestion)HS_Ques.get(iter_Ques.nextElement());
                dbq = (dbQuestion)HS_Ques.get(qArray.elementAt(i1));
                if (dbq != null) {
	                boolean is_new = false;
	                long order = ((Long)ht_order.get(new Long(dbq.res_id))).longValue();
	                long tmpId = dbq.res_id;
	                if(dbq.res_res_id_child == 0 && mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)){
	                	
	                    Vector robVec = new Vector();
	                    //Do not update ResourceContent And add ResourceObjective same as parent.
	                    robVec = dbResourceObjective.getObjId(con, tmpId); 
	                    String[] robs = new String[1];
	//                    Vector robVec = dbResourceObjective.getObjId(con, queId);
	                    int i ;
	                    for(i=0;i<robVec.size(); i++) {
	                        robs[i] = Long.toString(((dbResourceObjective) robVec.elementAt(i)).rob_obj_id);
	
	                    }
	                    is_new = true;
	                    dbQuestion dbq2 = dbq;
	                    dbq2.ins(con, robs, dbResource.RES_TYPE_QUE);
	                    
	                    // for auto-save
	                    dbResourceContent resCon = new dbResourceContent();
	                    resCon.rcn_res_id = mod_id;
	                    resCon.rcn_res_id_content = dbq2.que_res_id;
	                    resCon.rcn_obj_id_content = 0;
	                    resCon.rcn_score_multiplier = 1;
	                    resCon.rcn_rcn_res_id_parent = 0;
	                    resCon.rcn_rcn_sub_nbr_parent = 0;
	                    resCon.rcn_tkh_id = tkh_id;
	                    resCon.ins(con);
	                    
	                    PreparedStatement stmt1 = con.prepareStatement(
	                        " UPDATE Resources "
	                    +  "   SET res_res_id_root = ? , "
	                    +  "       res_upd_date = ? "
	                    +  "  WHERE res_id  = ? " );
	        
	                    stmt1.setLong(1,tmpId);
	                    stmt1.setTimestamp(2,dbq.res_upd_date);
	                    stmt1.setLong(3,dbq2.que_res_id);
	        
	                    if (stmt1.executeUpdate() != 1) {
	                        stmt1.close();
	                        con.rollback();
	                        throw new qdbException("Failed to update the resouce.");
	                    }
	                    stmt1.close();
	                    if(uploadDir != null)
	                        dbUtils.copyMediaFrom(uploadDir, tmpId, dbq2.res_id);        
	                    dbq = dbq2;
	                }else if(mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT) && (mod_id != 0)){
	                	// mod_id != 0 : 已经被复制过的 非情景题 或 情景题的主题目（即非子题目）
	                    dbq.res_res_id_root = dbq.res_id ;
	                    dbq.res_id = dbq.res_res_id_child;
	                    
	                    // for auto-save
	                    dbResourceContent resCon = new dbResourceContent();
	                    resCon.rcn_res_id = mod_id;
	                    resCon.rcn_res_id_content = dbq.res_id;
	                    resCon.rcn_obj_id_content = 0;
	                    resCon.rcn_score_multiplier = 1;
	                    resCon.rcn_rcn_res_id_parent = 0;
	                    resCon.rcn_rcn_sub_nbr_parent = 0;
	                    resCon.rcn_tkh_id = tkh_id;
	                    resCon.ins(con);
	                }
	                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	                if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) ) {
	                    FixedScenarioQue fsq = new FixedScenarioQue();
	                    Vector v_que_id = null; 
	                    long _que_res_id;
	                    fsq.res_id = dbq.res_id;
	                    fsq.get(con);
	                    if(mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)){
	                        if(is_new){
	                            Vector _v_que_id = new Vector();
	                            FixedScenarioQue srcFsq = new FixedScenarioQue();
	                            srcFsq.res_id = tmpId;
	                            v_que_id = srcFsq.getChildQueId(con);
	                            if (v_que_id.size() == 0) {
	                                throw new qdbErrMessage("MSP002");
	                            }
	                            if( fsq.qct_allow_shuffle_ind == 1 ) {
	                                v_que_id = cwUtils.randomVec(v_que_id);
	                            }
	                            for(int j=0; j<v_que_id.size(); j++){
	                                 _que_res_id = dbQuestion.duplicateDynQ(con, ((Long) v_que_id.elementAt(j)).longValue(), uploadDir, null, fsq.res_id, false);                      
	                                _v_que_id.addElement(new Long(_que_res_id));
	                                dbResourceContent _rcn = new dbResourceContent();
	                                _rcn.rcn_res_id = fsq.res_id;
	                                _rcn.rcn_res_id_content = _que_res_id;
	                                _rcn.rcn_order = (j+1);
	                                _rcn.rcn_sub_nbr = 0;//rcn.rcn_order;
	                                _rcn.rcn_score_multiplier = 1;
	                                _rcn.ins(con);
	                            }
	                            v_que_id = _v_que_id;
	                        } else {
	                            v_que_id = fsq.getChildQueId(con);
	                            if (v_que_id.size() == 0) {
	                                throw new qdbErrMessage("MSP002");
	                            }
	                        }
	                        FixedQueContainer fqc = new FixedQueContainer();
	                        fqc.res_id = tmpId;
	                        fqc.get(con);
	                        if( fqc.qct_allow_shuffle_ind == 1) {
	                            v_que_id = cwUtils.randomVec(v_que_id);                 
	                        }
	                        qList.append(fsq.asXMLinTest_test(con, order, v_que_id, null, isShuffleMCQue,uploadDir,resIdVec, tkh_id));
	                    }else{
	                        v_que_id = fsq.getChildQueId(con);
	                        if (v_que_id.size() == 0) {
	                            throw new qdbErrMessage("MSP002");
	                        }
	                        if( fsq.qct_allow_shuffle_ind == 1) {
	                            v_que_id = cwUtils.randomVec(v_que_id);
	                        }
	                        qList.append(fsq.asXMLinTest_test(con, order, v_que_id, null, isShuffleMCQue,uploadDir,resIdVec, tkh_id));
	                    }
	                } else if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
	                    DynamicScenarioQue dsq = new DynamicScenarioQue();
	                    DynamicScenarioQue srcDsq = new DynamicScenarioQue();
	                    Vector v_que_id = null;
	                    dsq.res_id = dbq.res_id;
	                    dsq.get(con);
	
	                    //get DSC content que ids
	                    srcDsq.res_id = tmpId;                    
	                    v_que_id = srcDsq.getChildQueId(con);
	        			
	                    if (v_que_id.size() == 0) {
	                        throw new qdbErrMessage("MSP002");
	                    }
	                    if( mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)) {    
	                        Vector _v_que_id = new Vector();
	                        Hashtable h_contained_que = dbResourceContent.getChildQueRoot(con, dsq.res_id);
	                        for(int j=0; j<v_que_id.size(); j++){
	                            if( !h_contained_que.containsKey((Long)v_que_id.elementAt(j))) {
	                                long _que_res_id = 0;
	                                    _que_res_id = dbQuestion.duplicateDynQ(con, ((Long) v_que_id.elementAt(j)).longValue(), uploadDir, null, dsq.res_id, false);
	                                _v_que_id.addElement(new Long(_que_res_id));
	                                dbResourceContent _rcn = new dbResourceContent();
	                                _rcn.rcn_res_id = dsq.res_id;
	                                _rcn.rcn_res_id_content = _que_res_id;
	                                _rcn.rcn_order = (j+1);
	                                _rcn.rcn_sub_nbr = 0;//rcn.rcn_order;
	                                _rcn.rcn_score_multiplier = 1;
	                                _rcn.ins(con);
	                                //_v_que_id.addElement(new Long(_que_res_id));
	                            } else {
	        	                    // for auto-save
	                                dbResourceContent _rcn = new dbResourceContent();
	                                _rcn.rcn_res_id = dsq.res_id;
	                                _rcn.rcn_res_id_content = ((Long)v_que_id.elementAt(j)).longValue();
	                                _rcn.rcn_order = (j+1);
	                                _rcn.rcn_sub_nbr = 0;
	                                _rcn.rcn_score_multiplier = 1;
	                                _rcn.rcn_tkh_id = tkh_id;
	                                _rcn.ins(con);
	                                _v_que_id.addElement(h_contained_que.get((Long)v_que_id.elementAt(j)));
	                            }
	                        }
	                        v_que_id = _v_que_id;
	                        DynamicQueContainer dqc = new DynamicQueContainer();
	                        dqc.res_id = tmpId;
	                        dqc.get(con);
	                        if( dqc.qct_allow_shuffle_ind == 1 ) {
	                            v_que_id = cwUtils.randomVec(v_que_id);                 
	                        }
	                    } else {
	            			dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
	            			dbpas.pasTkhId = tkh_id;
	            			dbpas.pasResId = mod_id;
	            			if (!dbpas.chkforExist(con)) {
	                            //保存动态情景题
	                            dbResourceContent resCon = new dbResourceContent();
    	                        resCon.rcn_res_id = mod_id;
    	                        resCon.rcn_res_id_content = dbq.res_id;
    	                        resCon.rcn_obj_id_content = 0;
    	                        resCon.rcn_score_multiplier = 1;
    	                        resCon.rcn_rcn_res_id_parent = 0;
    	                        resCon.rcn_rcn_sub_nbr_parent = 0;
    	                        resCon.rcn_tkh_id = tkh_id;
    	                        resCon.ins(con);
		                        for (int k=0; k<v_que_id.size(); k++) {
		                        	//保存当前动态情景题所抽出的题目
		                        	resCon.rcn_res_id = dbq.res_id;
		                        	resCon.rcn_res_id_content = ((Long)v_que_id.elementAt(k)).longValue();
		                        	resCon.ins(con);
		                        }
	            			} else {//还原
	            				v_que_id = srcDsq.getChildQueIdforRestore(con, dbq.res_id, tkh_id);
	            			}                    	
	                    }
	                    qList.append(dsq.asXMLinTest_test(con, order, v_que_id, null, isShuffleMCQue,uploadDir,resIdVec, tkh_id));
	                } else {
	                    qList.append(dbq.asXML_test(con, order,isShuffleMCQue, tkh_id));
	                }
	                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	                resIdVec.addElement(new Long(dbq.que_res_id));
	                if (controller != null) {
	                	controller.next();
	                }
	            }else {
	            	cnt--;
	                // in order to make sure the progress reach 100% only when this cmd ends,
	                // the TotalRow is set as cnt+1 and the last row is to be added at the end of this cmd
	                // (2007-03-12 kawai)
	                if (controller != null) {
	                	controller.setTotalRow(cnt + 1);
	                }
	            }
            } 
        }catch(SQLException e) {
               throw new qdbException("SQL Error: " + e.getMessage());
        }
        finally {
            if(stmt!=null) {stmt.close();}
        }
        return qList.toString();
    }
    
    public  Hashtable get_test(Connection con, Vector q_Vec)
    throws SQLException,qdbException ,cwSysMessage
    {
        PreparedStatement stmt = null;
        Hashtable HS_Ques = new Hashtable();
        try {
            
            dbQuestion dbq ;
            long QueId ;
            dbResourceContent rcn = null;
            StringBuffer temp_sql = new StringBuffer("(");
            for(int i = 0; i < q_Vec.size(); i++){
                rcn = (dbResourceContent) q_Vec.elementAt(i);
                temp_sql.append(rcn.rcn_res_id_content).append(",");
            }
            temp_sql.append("0)");
            
            StringBuffer SQL = new StringBuffer();
            SQL.append("select ");
            SQL.append(" curQue.res_id, curQue.res_lan ,curQue.res_title ,curQue.res_desc ,curQue.res_type ,curQue.res_subtype ,curQue.res_annotation ,curQue.res_format ,curQue.res_difficulty ,curQue.res_duration ,curQue.res_privilege ,curQue.res_status ,curQue.res_usr_id_owner ,curQue.res_create_date ,curQue.res_tpl_name ,curQue.res_res_id_root ,curQue.res_mod_res_id_test ,curQue.res_upd_user ,curQue.res_upd_date ,curQue.res_src_type ,curQue.res_src_link, res_instructor_name, res_instructor_organization, ");
            SQL.append(" Question.que_xml, Question.que_score, Question.que_type, Question.que_int_count, Question.que_prog_lang, Question.que_media_ind, Question.que_submit_file_ind,");
            SQL.append(" Interaction.int_res_id, Interaction.int_label, Interaction.int_order, Interaction.int_xml_outcome,  Interaction.int_xml_explain, Interaction.int_res_id_explain, Interaction.int_res_id_refer");
            //SQL.append(", rootQue.res_id as res_id_root");
            SQL.append(" from Resources curQue inner join Question on (curQue.res_id = que_res_id)");
            SQL.append(" inner join Interaction on (curQue.res_id = int_res_id)");
           // SQL.append(" left join Resources rootQue on (curQue.res_id = rootQue.res_res_id_root)");
            SQL.append(" where curQue.res_id in ").append(temp_sql.toString());
            SQL.append(" order by Question.que_type, curQue.res_id, Interaction.int_order");
            stmt = con.prepareStatement(SQL.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QueId = rs.getLong("res_id");
                Long QueId_obj = new Long(QueId);
                if(!HS_Ques.containsKey(QueId_obj)){
                    dbq = new dbQuestion();
                    HS_Ques.put(QueId_obj,dbq);
                    dbq.res_id = QueId;
                    dbq.que_res_id = QueId;
                    dbq.res_lan = rs.getString("res_lan");
                    dbq.res_title = rs.getString("res_title");
                    dbq.res_desc = rs.getString("res_desc");
                    dbq.res_type = rs.getString("res_type");
                    dbq.res_subtype = rs.getString("res_subtype");
                    dbq.res_annotation = rs.getString("res_annotation");
                    dbq.res_format = rs.getString("res_format");
                    dbq.res_difficulty = rs.getInt("res_difficulty");
                    dbq.res_duration = rs.getFloat("res_duration");
                    dbq.res_privilege = rs.getString("res_privilege");
                    dbq.res_status = rs.getString("res_status");
                    dbq.res_usr_id_owner = rs.getString("res_usr_id_owner");
                    dbq.res_create_date = rs.getTimestamp("res_create_date");
                    dbq.res_tpl_name = rs.getString("res_tpl_name");
                    dbq.res_res_id_root = rs.getLong("res_res_id_root");
                    dbq.res_mod_res_id_test = rs.getLong("res_mod_res_id_test");
                    dbq.res_upd_user = rs.getString("res_upd_user");
                    dbq.res_upd_date = rs.getTimestamp("res_upd_date");
                    dbq.res_src_type = rs.getString("res_src_type");
                    dbq.res_src_link = rs.getString("res_src_link");
                    dbq.res_instructor_name = rs.getString("res_instructor_name");
                    dbq.res_instructor_organization = rs.getString("res_instructor_organization");
                    dbq.que_xml         = cwSQL.getClobValue(rs, "que_xml");
                    dbq.que_score       = rs.getInt("que_score");
                    dbq.que_type        = rs.getString("que_type");
                    dbq.que_int_count   = rs.getInt("que_int_count");
                    dbq.que_prog_lang   = rs.getString("que_prog_lang");
                    dbq.que_media_ind   = rs.getBoolean("que_media_ind");
                    dbq.que_submit_file_ind = rs.getBoolean("que_submit_file_ind");
                   // dbq.res_res_id_root= rs.getLong("res_id_root");
                   
                }else{
                    dbq = (dbQuestion)HS_Ques.get(QueId_obj);
                }
                dbInteraction intObj = new dbInteraction();
                intObj.int_res_id = rs.getLong("int_res_id");
                intObj.int_label = rs.getString("int_label");
                intObj.int_order = rs.getInt("int_order");
                intObj.int_xml_outcome  = cwSQL.getClobValue(rs, "int_xml_outcome");
                intObj.int_xml_explain  = cwSQL.getClobValue(rs, "int_xml_explain");
                intObj.int_res_id_explain   = rs.getLong("int_res_id_explain");
                intObj.int_res_id_refer     = rs.getLong("int_res_id_refer");
                dbq.ints.add(intObj);
            }
          
        }catch(SQLException e) {
               throw new qdbException("SQL Error: " + e.getMessage());
        }
        finally {
            if(stmt!=null) {stmt.close();}
        }
        return HS_Ques;
    }
    
    public String asXML_test( Connection con, long order,boolean isShuffleMCQue, long tkh_id)
    throws qdbException ,cwSysMessage
    {
        String result = "";
        // format the question list
        result = "<question id=\""+ res_id + "\" order=\"" + order ;
        result += "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date +"\">" + dbUtils.NEWL;
    
        result += xmlContent_test(con, isShuffleMCQue);
    
        result += "</question>" + dbUtils.NEWL;
        
        try {
        	if (res_mod_res_id_test != 0) {
        		result += ContentRestoreXML(con, order, tkh_id);
        	}
        } catch (SQLException e) {
        	throw new qdbException(e.getMessage());
        }
        return result;
    }
    
    private String xmlContent_test(Connection con,boolean isShuffleMCQue)
    throws qdbException ,cwSysMessage
    {
        String result= "";
    
        result += "<header difficulty=\"" + res_difficulty + "\" duration=\"" + res_duration
             + "\" privilege=\"" + res_privilege + "\" status=\"" + res_status 
             + "\" type=\"" + que_type + "\" language=\"" + res_lan + "\" score=\"" + que_score + "\"";
         if( que_submit_file_ind ) {
             result += " que_submit_file_ind=\"Y\" ";
         }else{
             result += " que_submit_file_ind=\"N\" ";
         }
         result += " >" + dbUtils.NEWL;
    
    
        result += "<title>" + dbUtils.esc4XML(res_title) + "</title>" + dbUtils.NEWL;
        result += "<desc>" + dbUtils.esc4XML(res_desc) + "</desc>" + dbUtils.NEWL;
      
        result += "</header>" + dbUtils.NEWL + dbUtils.NEWL;
    
        // format body
        if (!(que_type.equals(dbResource.RES_SUBTYPE_FSC) || que_type.equals(dbResource.RES_SUBTYPE_DSC))) {
            // format body
            Hashtable body = parseQueXml(que_xml);
            if (isShuffleMCQue && body.containsKey("isShuffle") && body.get("isShuffle").equals("Y")) {
                String bodyContent = que_xml.substring(0, que_xml.indexOf("<option"));
                result += rebuildQueXml(bodyContent, (ArrayList)body.get("optionText"));
                result += que_xml.substring(que_xml.indexOf("</interaction>"));
            } else {
                result += que_xml + dbUtils.NEWL;
            }
        }
        else {
            result += que_xml + dbUtils.NEWL;
        }
        //result += que_xml + dbUtils.NEWL;
    
       
        Vector intObj = ints;
        String oxml = "";
        String exml = "";
    
        dbInteraction dbi = new dbInteraction();
        for (int i=0;i<intObj.size();i++) {
            dbi  = (dbInteraction) intObj.elementAt(i);
            oxml += "<label>" + dbi.int_label + "</label>";
            oxml += dbi.int_xml_outcome;
            exml += dbi.int_xml_explain;
        }
    
        result += oxml + dbUtils.NEWL;
    
        if (exml==null) {
            exml = "";
        }
        result += exml + dbUtils.NEWL;
    
        return result;
    }
    
    private String ContentRestoreXML(Connection con, long order, long tkh_id) throws SQLException {
    	StringBuffer result= new StringBuffer();
    	dbResource dbres = new dbResource();
    	dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
    	dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
    	dbpas.pasTkhId = tkh_id;
    	dbres.res_id = res_mod_res_id_test;
    	if (tkh_id != 0) {
	    		String restype = dbres.getResType(con);
	    		if (restype != null && restype.equalsIgnoreCase(dbResource.RES_TYPE_MOD)) {
		    		dbpas.pasResId = res_mod_res_id_test;
		    		dbpsa.psaPgrResId = res_mod_res_id_test;
	    		} else {
	    			dbpas.pasResId = dbres.getResModResId(con);
	    			dbpsa.psaPgrResId = dbpas.pasResId;
	    		}
    	}
    	dbpsa.psaTkhId = tkh_id;
    	dbpsa.psaIntResId = res_id;
    	Vector vec = dbpsa.get(con);
    	if (vec != null && vec.size()>0) {
    		result.append("<restore>");
    		result.append("<question id=\"").append(res_id).append("\" order=\"").append(order).append("\" type=\"").append(que_type).append("\">");
    		for (int i=0; i<vec.size(); i++) {
    			dbpsa = (dbProgressAttemptSaveAnswer)vec.elementAt(i);
    			result.append("<interaction order=\"").append(dbpsa.psaIntOrder).append("\" pas_response_bil=\"").append(cwUtils.esc4XML(dbpsa.psaResponseBil)).append("\">");
    			if (que_type.equals(QUE_TYPE_MULTI) || que_type.equals(QUE_TYPE_MATCHING)) {
		    		String[] answer = cwUtils.splitToString(dbpsa.psaResponseBil, "~");
		    		for (int j=0; j<answer.length; j++) {
		    			result.append("<value>").append(cwUtils.esc4XML(cwUtils.escNull(answer[j]))).append("</value>");
		    		}
    			} else {
    				result.append("<value>").append(cwUtils.esc4XML(dbpsa.psaResponseBil)).append("</value>");
    			}
	    		result.append("</interaction>");
    		}
    		result.append("</question>");
    		result.append("</restore>");
    	}
    	return result.toString();
    }
    
    public static String getQueAsXMLforRestoredDyn(Connection con, String uploadDir, Vector qArray, Vector resIdVec,String mod_type, boolean isShuffleMCQue, long tkh_id, long mod_id, ExportController controller) throws qdbException, cwSysMessage, qdbErrMessage, SQLException {
        PreparedStatement stmt = null;
        StringBuffer qList = new StringBuffer();
        try {
            
            dbQuestion dbq ;
            Hashtable HS_Ques = new Hashtable();
            Hashtable ht_order = new Hashtable();
            long QueId ;
            StringBuffer temp_sql = new StringBuffer("(");
            for(int i = 0; i < qArray.size(); i++){
            	ht_order.put(qArray.elementAt(i), new Long(i + 1));
                temp_sql.append(qArray.elementAt(i).toString()).append(",");
            }
            temp_sql.append("0)");
            
            StringBuffer SQL = new StringBuffer();
            SQL.append("select ");
            SQL.append(" curQue.res_id, curQue.res_lan ,curQue.res_title ,curQue.res_desc ,curQue.res_type ,curQue.res_subtype ,curQue.res_annotation ,curQue.res_format ,curQue.res_difficulty ,curQue.res_duration ,curQue.res_privilege ,curQue.res_status ,curQue.res_usr_id_owner ,curQue.res_create_date ,curQue.res_tpl_name ,curQue.res_res_id_root ,curQue.res_mod_res_id_test ,curQue.res_upd_user ,curQue.res_upd_date ,curQue.res_src_type ,curQue.res_src_link, curQue.res_instructor_name, curQue.res_instructor_organization, ");
            SQL.append(" Question.que_xml, Question.que_score, Question.que_type, Question.que_int_count, Question.que_prog_lang, Question.que_media_ind, Question.que_submit_file_ind,");
            SQL.append(" Interaction.int_res_id, Interaction.int_label, Interaction.int_order, Interaction.int_xml_outcome,  Interaction.int_xml_explain, Interaction.int_res_id_explain, Interaction.int_res_id_refer,");
            SQL.append(" childQue.res_id as res_id_child");
            SQL.append(" from Resources curQue inner join Question on (curQue.res_id = que_res_id)");
            SQL.append(" left join Interaction on (curQue.res_id = int_res_id)");
            SQL.append(" left join Resources childQue on (curQue.res_id = childQue.res_res_id_root and childQue.res_mod_res_id_test is null and childQue.res_upd_date = curQue.res_upd_date)");
            SQL.append(" where curQue.res_id in ").append(temp_sql.toString());
            stmt = con.prepareStatement(SQL.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QueId = rs.getLong("res_id");
                Long QueId_obj = new Long(QueId);
                if(!HS_Ques.containsKey(QueId_obj)){
                    dbq = new dbQuestion();
                    HS_Ques.put(QueId_obj,dbq);
                    dbq.res_id = QueId;
                    dbq.que_res_id = QueId;
                    dbq.res_lan = rs.getString("res_lan");
                    dbq.res_title = rs.getString("res_title");
                    dbq.res_desc = rs.getString("res_desc");
                    dbq.res_type = rs.getString("res_type");
                    dbq.res_subtype = rs.getString("res_subtype");
                    dbq.res_annotation = rs.getString("res_annotation");
                    dbq.res_format = rs.getString("res_format");
                    dbq.res_difficulty = rs.getInt("res_difficulty");
                    dbq.res_duration = rs.getFloat("res_duration");
                    dbq.res_privilege = rs.getString("res_privilege");
                    dbq.res_status = rs.getString("res_status");
                    dbq.res_usr_id_owner = rs.getString("res_usr_id_owner");
                    dbq.res_create_date = rs.getTimestamp("res_create_date");
                    dbq.res_tpl_name = rs.getString("res_tpl_name");
                    dbq.res_mod_res_id_test = mod_id;
                    dbq.res_upd_user = rs.getString("res_upd_user");
                    dbq.res_upd_date = rs.getTimestamp("res_upd_date");
                    dbq.res_src_type = rs.getString("res_src_type");
                    dbq.res_src_link = rs.getString("res_src_link");
                    dbq.res_instructor_name = rs.getString("res_instructor_name");
                    dbq.res_instructor_organization = rs.getString("res_instructor_organization");
                    dbq.que_xml         = cwSQL.getClobValue(rs, "que_xml");
                    dbq.que_score       = rs.getInt("que_score");
                    dbq.que_type        = rs.getString("que_type");
                    dbq.que_int_count   = rs.getInt("que_int_count");
                    dbq.que_prog_lang   = rs.getString("que_prog_lang");
                    dbq.que_media_ind   = rs.getBoolean("que_media_ind");
                    dbq.que_submit_file_ind = rs.getBoolean("que_submit_file_ind");
                    dbq.res_res_id_child= rs.getLong("res_id_child");
                   
                }else{
                    dbq = (dbQuestion)HS_Ques.get(QueId_obj);
                }
                if( !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) && !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC) ) {
                    dbInteraction intObj = new dbInteraction();
                    intObj.int_res_id = rs.getLong("int_res_id");
                    intObj.int_label = rs.getString("int_label");
                    intObj.int_order = rs.getInt("int_order");
                    intObj.int_xml_outcome  = cwSQL.getClobValue(rs, "int_xml_outcome");
                    intObj.int_xml_explain  = cwSQL.getClobValue(rs, "int_xml_explain");
                    intObj.int_res_id_explain   = rs.getLong("int_res_id_explain");
                    intObj.int_res_id_refer     = rs.getLong("int_res_id_refer");
                    dbq.ints.add(intObj);
                }
            }
            int cnt = qArray.size();
            // in order to make sure the progress reach 100% only when this cmd ends,
            // the TotalRow is set as cnt+1 and the last row is to be added at the end of this cmd
            // (2007-03-12 kawai)
            if (controller != null) {
            	controller.setTotalRow(cnt + 1);
            }
            for(int i1 = 0; i1 < qArray.size() && (controller == null ||(controller != null && !controller.isCancelled())); i1++){
            	dbq = (dbQuestion)HS_Ques.get(qArray.elementAt(i1));
            	if (dbq != null) {
            		long order = ((Long)ht_order.get(new Long(dbq.res_id))).longValue();
	                if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) ||  dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC) ) {
	                    FixedScenarioQue fsq = new FixedScenarioQue();
	                    Vector v_que_id = null; 
	                    fsq.res_id = dbq.res_id;
	                    fsq.get(con);
	                    if(dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)) {
	                    	v_que_id = fsq.getChildQueId(con);
	                    } else {
	                    	ViewResources vr = new ViewResources();
	                    	v_que_id = vr.getChildQueIdforDynRestore(con,dbq.res_id, tkh_id);
	                    }
	                    /*if (v_que_id.size() == 0) {
	                        throw new qdbErrMessage("MSP002");
	                    }*/
	                    qList.append(fsq.asXMLinTest_testforDyn(con, order, v_que_id, null, isShuffleMCQue,uploadDir,resIdVec, tkh_id, mod_id));
	                }else {
	                    qList.append(dbq.asXML_test(con, order,isShuffleMCQue, tkh_id));
	                }
	                resIdVec.addElement(new Long(dbq.que_res_id));
	                if (controller != null) {
	                	controller.next();
	                }
            	} else {
            		cnt--;
	                // in order to make sure the progress reach 100% only when this cmd ends,
	                // the TotalRow is set as cnt+1 and the last row is to be added at the end of this cmd
	                // (2007-03-12 kawai)
	                if (controller != null) {
	                	controller.setTotalRow(cnt + 1);
	                }
            	}
            } 
        }catch(SQLException e) {
               throw new qdbException("SQL Error: " + e.getMessage());
        }
        finally {
            if(stmt!=null) {stmt.close();}
        }
        return qList.toString();
    }
    
    public static String getQueXML_test(Hashtable HS_Ques, Vector testId, long mod_res_id, String mod_type, 
    		long tkh_id, String uploadDir, Connection con, boolean restoreQue, boolean isShuffleMCQue, 
    		Vector resIdVec, boolean isSubQue, ExportController controller) 
    		throws qdbException, cwSysMessage, SQLException, cwException
    {	
        int cnt = testId.size();
        dbQuestion dbq = null;     
        Hashtable ht_order = new Hashtable();       
        StringBuffer qList = new StringBuffer("");
        //为所有题目排序
        for (int i = 0;i < cnt; i++) {
        	ht_order.put(testId.get(i), new Long(i + 1));
        }
        
        // in order to make sure the progress reach 100% only when this cmd ends,
        // the TotalRow is set as cnt+1 and the last row is to be added at the end of this cmd
        if (controller != null) {
            if(controller.currentRow > 0 && controller.getTotalRow() > 0){
                cnt =  controller.currentRow + cnt + 1;
                int currentRow = (int) ((controller.currentRow / controller.getTotalRow()) * cnt);
                controller.setTotalRow(cnt);
                controller.currentRow  = currentRow;
            }else{
                controller.setTotalRow(cnt + 1);  
            }
        }
        for(int i1 = 0; i1 < testId.size() && (controller == null ||(controller != null && !controller.isCancelled())); i1++){
        	dbq = (dbQuestion)HS_Ques.get(testId.get(i1));
            if (dbq != null) {
                long order = ((Long)ht_order.get(new Long(dbq.res_id))).longValue();
                //动态测验
                if (!restoreQue && mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)) {
                	//为auto-save保存测验与题目的关系或主题目与子题目的关系到ResourceContent表中
                	dbResourceContent resCon = new dbResourceContent();
                	if (isSubQue) {//子题目
                		resCon.rcn_res_id = mod_res_id;
                		resCon.rcn_res_id_content = dbq.que_res_id;
                		resCon.rcn_order = (i1 + 1);
                		resCon.rcn_sub_nbr = 0;//rcn.rcn_order;
                		resCon.rcn_score_multiplier = 1;
                		resCon.rcn_tkh_id = tkh_id;
                		resCon.ins(con);
                	} else {//主题目               		
                        resCon.rcn_res_id = mod_res_id;
                        resCon.rcn_res_id_content = dbq.que_res_id;
                        resCon.rcn_obj_id_content = 0;
                        resCon.rcn_score_multiplier = 1;
                        resCon.rcn_rcn_res_id_parent = 0;
                        resCon.rcn_rcn_sub_nbr_parent = 0;
                        resCon.rcn_tkh_id = tkh_id;
                        resCon.ins(con);
                	}               
                }     
                //静态情景题
                if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) ) {
                	Hashtable subQue = (Hashtable)dbq.sub_que_vec.get(0);
        			Vector v_que_id = new Vector();
        			v_que_id.addAll((Vector)subQue.get("QUE_ORDER"));
        			//是否打乱子题目的顺序      	
            		if (dbq.qct_allow_shuffle_ind == 1) {		          			
            			v_que_id = cwUtils.randomVec(v_que_id);          			
            		}     		
                	qList.append(FixedScenarioQue.asXMLinTest_test(dbq, subQue, v_que_id, order, tkh_id, uploadDir,
                			con, restoreQue, isShuffleMCQue, resIdVec, mod_type)); 
                }
                //动态情景题
                else if(dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                	Hashtable subQue = (Hashtable)dbq.sub_que_vec.get(0);
                	Vector v_que_id = new Vector();
                	if (!restoreQue) {
                		List subScenQue = null;
                		List allSubQueId = (Vector)subQue.get("QUE_ORDER");
                		Vector temp = null;
                		int qcount;
                		//遍历每个条件提取相应数目的题目
                		for (int i = 0; i < allSubQueId.size(); i++) {
                			subScenQue = (List)allSubQueId.get(i);
                			//抽题数
                			qcount = ((Integer)subScenQue.get(0)).intValue();
                			//符合该条件的所有题目id
                			temp = (Vector)subScenQue.get(1);
                			//根据题目数量随机抽取子题目
                			v_que_id.addAll(cwUtils.randomDrawFromVec((Vector)temp.clone(), qcount));
                		}
                		/*
                 	       如果是静态测验的话就把动态情景题的所有主题目与子题目的关系保存起来
                 	       如果是动态测验的话每题的关系在本API的开头都会保存起来所以不用在这里另外再保存
                 	    */
	                 	if (mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_TST)) {
	                 		dbResourceContent resCon = new dbResourceContent();                 		
	                        resCon.rcn_res_id = mod_res_id;
	                        resCon.rcn_res_id_content = dbq.que_res_id;
	                        resCon.rcn_obj_id_content = 0;
	                        resCon.rcn_score_multiplier = 1;
	                        resCon.rcn_rcn_res_id_parent = 0;
	                        resCon.rcn_rcn_sub_nbr_parent = 0;
	                        resCon.rcn_tkh_id = tkh_id;
	                        resCon.ins(con);
	                        for(int i = 0; i < v_que_id.size(); i++){
	                        	//保存当前动态情景题所抽出的题目
	                        	resCon.rcn_res_id = dbq.que_res_id;
	                        	resCon.rcn_res_id_content = ((Long)v_que_id.get(i)).longValue();
	                        	resCon.ins(con);
	                        }
	                 	}
                	} else if (mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_TST)) {
                		v_que_id = dbResourceContent.getDynQueId(con, dbq.que_res_id, tkh_id);
                	} else {
                		v_que_id.addAll((Vector)subQue.get("QUE_ORDER"));
                	}
                	
                	qList.append(FixedScenarioQue.asXMLinTest_test(dbq, subQue, v_que_id, order, tkh_id, uploadDir,
                			con, restoreQue, isShuffleMCQue, resIdVec, mod_type));
                } else {
                    qList.append(dbq.asXML_test(con, order,isShuffleMCQue, tkh_id));
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                resIdVec.addElement(new Long(dbq.que_res_id));
                if (controller != null) {
                	controller.next();
                }
            }else {
            	cnt--;
                // in order to make sure the progress reach 100% only when this cmd ends,
                // the TotalRow is set as cnt+1 and the last row is to be added at the end of this cmd
                if (controller != null) {
                	controller.setTotalRow(cnt + 1);
                }
            }
        } 
        if (controller != null) {
            controller.currentRow  = controller.getTotalRow() - 1;
        }
    	return qList.toString();
    }
    
    public static Vector getQueForRestore_test(Connection con, String mod_type, long mod_res_id, long tkh_id,Vector vec_res_id_lst) 
	throws SQLException, qdbException 
{
    Vector vec_test_score = new Vector();
    //用来存放所有题目的顺序 － 用sql语句查出来的顺序来存放
    Vector queOrder = new Vector();
	//存放所有题目
	Hashtable HS_Ques = new Hashtable();
	//存放选项
	Hashtable hs_int_score = new Hashtable();
	long QueId;
	long ScenarioQueId;
	Hashtable subQues = null;  	
	PreparedStatement stmt = null;
	dbQuestion dbq;
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT p_res.res_id as p_res_id, p_rc.rcn_order as p_rcn_order, p_res.res_lan as p_res_lan, ")
	   .append("p_res.res_subtype as p_res_subtype, p_res.res_upd_date as p_res_upd_date, p_res.res_difficulty as p_res_difficulty, ")
	   .append("p_res.res_duration as p_res_duration, p_res.res_privilege as p_res_privilege, ")
	   .append("p_res.res_status as p_res_status, p_res.res_title as p_res_title, p_res.res_desc as p_res_desc, ")
	   .append("p_res.res_type as p_res_type, p_res.res_usr_id_owner as p_res_usr_id_owner, p_res.res_create_date as p_res_create_date, ")
	   .append("p_res.res_tpl_name as p_res_tpl_name, p_res.res_res_id_root as p_res_res_id_root, p_res.res_upd_user as p_res_upd_user, ")
	   .append("p_res.res_src_type as p_res_src_type, p_res.res_src_link as p_res_src_link, p_res.res_instructor_name as p_res_instructor_name, ")
	   .append("p_res.res_instructor_organization as p_res_instructor_organization, p_que.que_int_count as p_que_int_count, ")
	   .append("p_res.res_annotation as p_res_annotation, p_res.res_format as p_res_format, ")
	   .append("p_que.que_prog_lang as p_que_prog_lang, p_que.que_media_ind as p_que_media_ind, ")
	   .append("p_que.que_xml as p_que_xml, p_que.que_score as p_que_score, p_que.que_type as p_que_type, ")
	   .append("p_que.que_submit_file_ind as p_que_submit_file_ind, p_int.int_label as p_int_label, ")
	   .append("p_int.int_xml_outcome as p_int_xml_outcome, p_int.int_xml_explain as p_int_xml_explain, p_int.int_order as p_int_order, ")
	   .append("p_int.int_res_id_explain as p_int_res_id_explain, p_int.int_res_id_refer as p_int_res_id_refer, p_int.int_res_id as p_int_res_id, ");
	sql.append("c_res.res_id as c_res_id, c_rc.rcn_order as c_rcn_order, c_res.res_lan as c_res_lan, ")
	   .append("c_res.res_subtype as c_res_subtype, c_res.res_upd_date as c_res_upd_date, c_res.res_difficulty as c_res_difficulty, ")
	   .append("c_res.res_duration as c_res_duration, c_res.res_privilege as c_res_privilege, ")
	   .append("c_res.res_status as c_res_status, c_res.res_title as c_res_title, c_res.res_desc as c_res_desc, ")
	   .append("c_res.res_type as c_res_type, c_res.res_usr_id_owner as c_res_usr_id_owner, c_res.res_create_date as c_res_create_date, ")
	   .append("c_res.res_tpl_name as c_res_tpl_name, c_res.res_res_id_root as c_res_res_id_root, c_res.res_upd_user as c_res_upd_user, ")
	   .append("c_res.res_src_type as c_res_src_type, c_res.res_src_link as c_res_src_link, c_res.res_instructor_name as c_res_instructor_name, ")
	   .append("c_res.res_instructor_organization as c_res_instructor_organization, c_que.que_int_count as c_que_int_count, ")
	   .append("c_res.res_annotation as c_res_annotation, c_res.res_format as c_res_format, ")
	   .append("c_que.que_prog_lang as c_que_prog_lang, c_que.que_media_ind as c_que_media_ind, ")
	   .append("c_que.que_xml as c_que_xml, c_que.que_score as c_que_score, c_que.que_type as c_que_type, ")
	   .append("c_que.que_submit_file_ind as c_que_submit_file_ind, c_int.int_label as c_int_label, ")
	   .append("c_int.int_xml_outcome as c_int_xml_outcome, c_int.int_xml_explain as c_int_xml_explain, c_int.int_order as c_int_order, ")
	   .append("c_int.int_res_id_explain as c_int_res_id_explain, c_int.int_res_id_refer as c_int_res_id_refer, c_int.int_res_id as c_int_res_id, ")
	   .append("qct_allow_shuffle_ind ");
	sql.append("FROM Resources p_res ")
	   .append("inner join ResourceContent p_rc on ")
	   .append("(p_rc.rcn_res_id = ? and p_rc.rcn_tkh_id = ? and p_res.res_id = p_rc.rcn_res_id_content) ")
	   .append("inner join Question p_que on (p_res.res_id = p_que.que_res_id) ")
	   .append("left join Interaction p_int on (p_res.res_id = p_int.int_res_id) ")
	   .append("left join QueContainer on (qct_res_id = p_res.res_id) ")
	   .append("left join ResourceContent c_rc on ")
	   .append("(c_rc.rcn_res_id = p_res.res_id and ((p_que.que_type = 'FSC' and c_rc.rcn_tkh_id = ?) or ")
       .append("(p_que.que_type = 'DSC' and c_rc.rcn_tkh_id = ?)))")
       .append("left join Resources c_res on (c_res.res_id = c_rc.rcn_res_id_content and c_res.res_status = 'ON') ")
       .append("left join Question c_que on (c_que.que_res_id = c_res.res_id) ")
       .append("left join Interaction c_int on (c_int.int_res_id = c_res.res_id) ");
    sql.append("WHERE p_res.res_status = 'ON' ")
       .append("order by p_rc.rcn_order, c_rc.rcn_order");
    try {
        stmt = con.prepareStatement(sql.toString());
        int index = 1;
        stmt.setLong(index++, mod_res_id);
        if (mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)) {
        	stmt.setLong(index++, tkh_id);
        	stmt.setLong(index++, -1);
        	stmt.setLong(index++, tkh_id);
        } else {
        	stmt.setLong(index++, -1);
        	stmt.setLong(index++, -1);
        	stmt.setLong(index++, tkh_id);
        }
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            QueId = rs.getLong("p_res_id");
            Long QueId_obj = new Long(QueId);     
            if(!HS_Ques.containsKey(QueId_obj)){
            	queOrder.add(QueId_obj);
            	vec_res_id_lst.add(QueId_obj);
                dbq = new dbQuestion();
                HS_Ques.put(QueId_obj,dbq);
                dbq.res_id = QueId;
 	            dbq.que_res_id = QueId;
 	            dbq.res_lan = rs.getString("p_res_lan");
 	            dbq.res_title = rs.getString("p_res_title");
 	            dbq.res_desc = rs.getString("p_res_desc");
 	            dbq.res_type = rs.getString("p_res_type");
	            dbq.res_subtype = rs.getString("p_res_subtype");
	            dbq.res_annotation = rs.getString("p_res_annotation");
		        dbq.res_format = rs.getString("p_res_format");
 	            dbq.res_difficulty = rs.getInt("p_res_difficulty");
 	            dbq.res_duration = rs.getFloat("p_res_duration");
 	            dbq.res_privilege = rs.getString("p_res_privilege");
 	            dbq.res_status = rs.getString("p_res_status");
 	            dbq.res_usr_id_owner = rs.getString("p_res_usr_id_owner");
		        dbq.res_create_date = rs.getTimestamp("p_res_create_date");
		        dbq.res_tpl_name = rs.getString("p_res_tpl_name");
		        dbq.res_res_id_root = rs.getLong("p_res_res_id_root");
 	            dbq.res_mod_res_id_test = mod_res_id;
 	            dbq.res_upd_user = rs.getString("p_res_upd_user");
 	            dbq.res_upd_date = rs.getTimestamp("p_res_upd_date");
 	            dbq.res_src_type = rs.getString("p_res_src_type");
		        dbq.res_src_link = rs.getString("p_res_src_link");
		        dbq.res_instructor_name = rs.getString("p_res_instructor_name");
		        dbq.res_instructor_organization = rs.getString("p_res_instructor_organization");
 	            dbq.que_xml = cwSQL.getClobValue(rs, "p_que_xml");
 	            dbq.que_score = rs.getInt("p_que_score");
 	            dbq.que_type = rs.getString("p_que_type");
 	            dbq.que_int_count = rs.getInt("p_que_int_count");
		        dbq.que_prog_lang = rs.getString("p_que_prog_lang");
		        dbq.que_media_ind = rs.getBoolean("p_que_media_ind");
 	            dbq.que_submit_file_ind = rs.getBoolean("p_que_submit_file_ind");
 	            dbq.qct_allow_shuffle_ind = rs.getInt("qct_allow_shuffle_ind");
 		    }else{
 	            dbq = (dbQuestion)HS_Ques.get(QueId_obj);
 	        }
            if(!dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) && !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
 	           dbInteraction intObj = new dbInteraction();
 	           intObj.int_res_id = rs.getLong("p_int_res_id");
 	           intObj.int_label = rs.getString("p_int_label");
 	           intObj.int_order = rs.getInt("p_int_order");
 	           intObj.int_xml_outcome = cwSQL.getClobValue(rs, "p_int_xml_outcome");
 	           intObj.int_xml_explain = cwSQL.getClobValue(rs, "p_int_xml_explain");
 	           intObj.int_res_id_explain = rs.getLong("p_int_res_id_explain");
	           intObj.int_res_id_refer = rs.getLong("p_int_res_id_refer");
 	           dbq.ints.add(intObj);
 	           dbModule.setIntScore(intObj, hs_int_score);
 	        }
            ScenarioQueId = rs.getLong("c_res_id");	
 	        Long scenQueId_obj = new Long(ScenarioQueId);
 	        if(dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) || 
 	    		   dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
 	    	    if (dbq.sub_que_vec.size() > 0) {
 	    		    subQues = (Hashtable)dbq.sub_que_vec.get(0);   
 	    	    } else {
 	    		    subQues = new Hashtable();
 	    		    Vector subQueOrder = new Vector();
 	    		    //题目顺序
 	    		    subQues.put("QUE_ORDER", subQueOrder);
 	    		    dbq.sub_que_vec.add(subQues);
 	    	    }
    		    dbQuestion subQbq = null;   		    
    		    if(!subQues.containsKey(scenQueId_obj)){	
    		    	((Vector)subQues.get("QUE_ORDER")).add(scenQueId_obj);
	    	 	    subQbq = new dbQuestion();
    			    subQues.put(scenQueId_obj, subQbq);
    			    subQbq.res_id = ScenarioQueId;
    			    subQbq.que_res_id = ScenarioQueId;
    			    subQbq.res_lan = rs.getString("c_res_lan");
	    			subQbq.res_title = rs.getString("c_res_title");
	    			subQbq.res_desc = rs.getString("c_res_desc");
	    			subQbq.res_type = rs.getString("c_res_type");
	    			subQbq.res_subtype = rs.getString("c_res_subtype");
	    			subQbq.res_annotation = rs.getString("c_res_annotation");
	    			subQbq.res_format = rs.getString("c_res_format");
	    			subQbq.res_difficulty = rs.getInt("c_res_difficulty");
	    			subQbq.res_duration = rs.getFloat("c_res_duration");
	    			subQbq.res_privilege = rs.getString("c_res_privilege");
	    			subQbq.res_status = rs.getString("c_res_status");
	    			subQbq.res_usr_id_owner = rs.getString("c_res_usr_id_owner");
	    			subQbq.res_create_date = rs.getTimestamp("c_res_create_date");
	    			subQbq.res_tpl_name = rs.getString("c_res_tpl_name");
	    			subQbq.res_res_id_root = rs.getLong("c_res_res_id_root");
	    			subQbq.res_mod_res_id_test = mod_res_id;
	    			subQbq.res_upd_user = rs.getString("c_res_upd_user");
	    			subQbq.res_upd_date = rs.getTimestamp("c_res_upd_date");
	    			subQbq.res_src_type = rs.getString("c_res_src_type");
	    			subQbq.res_src_link = rs.getString("c_res_src_link");
	    			subQbq.res_instructor_name = rs.getString("c_res_instructor_name");
	    			subQbq.res_instructor_organization = rs.getString("c_res_instructor_organization");
	    			subQbq.que_xml = cwSQL.getClobValue(rs, "c_que_xml");
	    			subQbq.que_score = rs.getInt("c_que_score");
	    			subQbq.que_type = rs.getString("c_que_type");
	    			subQbq.que_int_count = rs.getInt("c_que_int_count");
	    			subQbq.que_prog_lang = rs.getString("c_que_prog_lang");
	    			subQbq.que_media_ind = rs.getBoolean("c_que_media_ind");
	    			subQbq.que_submit_file_ind = rs.getBoolean("c_que_submit_file_ind");
	    			subQbq.qct_allow_shuffle_ind = rs.getInt("qct_allow_shuffle_ind");
    		    } else {
    			    subQbq = (dbQuestion)subQues.get(scenQueId_obj);
    		    }	    
    	        if(!subQbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) && 
    	    		    !subQbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC) ) {
    	            dbInteraction subIntObj = new dbInteraction();
    	            subIntObj.int_res_id = rs.getLong("c_int_res_id");
	    	        subIntObj.int_label = rs.getString("c_int_label");
	    	        subIntObj.int_order = rs.getInt("c_int_order");
	    	        subIntObj.int_xml_outcome = cwSQL.getClobValue(rs, "c_int_xml_outcome");
	    	        subIntObj.int_xml_explain = cwSQL.getClobValue(rs, "c_int_xml_explain");
	    	        subIntObj.int_res_id_explain = rs.getLong("c_int_res_id_explain");
	    	        subIntObj.int_res_id_refer = rs.getLong("c_int_res_id_refer");
    	            subQbq.ints.add(subIntObj);
    	            dbModule.setIntScore(subIntObj, hs_int_score);
    	       }
 	       }        
       }
    } catch(SQLException e) {
           throw new qdbException("SQL Error: " + e.getMessage());
    }
    finally {
        if(stmt != null) {
        	stmt.close();
        }
    }
    //题目顺序
    HS_Ques.put("QUE_ORDER", queOrder);
    vec_test_score.add(HS_Ques);
	    vec_test_score.add(hs_int_score);
	    return vec_test_score;
}
    
    public static ArrayList parseQuesXmlToHash(String que_xml) throws qdbException {
        Hashtable bodyContent = new Hashtable();
        ArrayList optionList = new ArrayList();
        ArrayList orgOptionList = new ArrayList();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document docXml = null;
        try {
            builder = factory.newDocumentBuilder();
            if (que_xml != null && que_xml.length() > 0) {
                StringReader rd = new StringReader(que_xml);
                InputSource in = new InputSource(rd);
                docXml = builder.parse(in);
            }
        } catch (ParserConfigurationException e) {
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        } catch (SAXException e) {
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        } catch (IOException e) {
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        }
        Element rootElement = docXml.getDocumentElement();
        NodeList nodeList = rootElement.getChildNodes();
     //   System.out.println(que_xml);
        ArrayList que_list = new ArrayList();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nod = nodeList.item(i);
           // System.out.println(nod.getNodeName());
            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("question")) {
                HashMap que_map = new HashMap();
                que_list.add(que_map);
                Element nod_e = (Element) nod;
                que_map.put("que_id", nod_e.getAttribute("id"));

                NodeList que_d = nod.getChildNodes();
                for (int j = 0; j < que_d.getLength(); j++) {
                    Node child_nod = que_d.item(j);
                    if (child_nod.getNodeName() != null && child_nod.getNodeName().equalsIgnoreCase("header")) {

                        for (int k = 0; k < child_nod.getAttributes().getLength(); k++) {
                            if (child_nod.getAttributes().item(k).getNodeName() != null && child_nod.getAttributes().item(k).getNodeName().equalsIgnoreCase("type")) {
                                que_map.put("subtype", child_nod.getAttributes().item(k).getNodeValue());
                            }
                            if (child_nod.getAttributes().item(k).getNodeName() != null && child_nod.getAttributes().item(k).getNodeName().equalsIgnoreCase("score")) {
                                que_map.put("score", child_nod.getAttributes().item(k).getNodeValue());
                            }
                        }
                    } else if (child_nod.getNodeName() != null && child_nod.getNodeName().equalsIgnoreCase("body")) {

                        NodeList body_d = child_nod.getChildNodes();
                        for (int k = 0; k < body_d.getLength(); k++) {

                            // 取body text
                            if (body_d.item(k).getNodeName() != null && body_d.item(k).getNodeName().equalsIgnoreCase("#text")) {
                                que_map.put("title", body_d.item(k).getTextContent());
                            }
                            
                            if (body_d.item(k).getNodeName() != null && body_d.item(k).getNodeName().equalsIgnoreCase("html")) {
                                que_map.put("title", body_d.item(k).getTextContent());
                            }

                            if (body_d.item(k).getNodeName() != null && body_d.item(k).getNodeName().equalsIgnoreCase("interaction")) {
                                Node interaction_nod = body_d.item(k);

                                for (int l = 0; l < interaction_nod.getAttributes().getLength(); l++) {
                                    if (interaction_nod.getAttributes().item(l).getNodeName() != null
                                            && interaction_nod.getAttributes().item(l).getNodeName().equalsIgnoreCase("logic")) {
                                        que_map.put("logic", interaction_nod.getAttributes().item(l).getNodeValue());
                                    }
                                }
                                NodeList options = body_d.item(k).getChildNodes();
                                ArrayList option_list = new ArrayList();
                                que_map.put("option_list", option_list);
                                for (int l = 0; l < options.getLength(); l++) {
                                    if (options.item(l).getNodeName() != null && options.item(l).getNodeName().equalsIgnoreCase("option")) {
                                        HashMap opt_map = new HashMap();
                                        Element option = (Element) options.item(l);
                                        opt_map.put("opt_id", option.getAttribute("id"));
                                        opt_map.put("title", options.item(l).getTextContent());
                                        option_list.add(opt_map);
                                    }
                                }
                            }
                        }
                    }
                    
                    else if (child_nod.getNodeName() != null && child_nod.getNodeName().equalsIgnoreCase("outcome")) {

                        NodeList outcome_d = child_nod.getChildNodes();
                        String cre_condition ="";
                        for (int k = 0; k < outcome_d.getLength(); k++) {

                            if (outcome_d.item(k).getNodeName() != null && outcome_d.item(k).getNodeName().equalsIgnoreCase("feedback")) {
                                Node outcome_nod = outcome_d.item(k);
                                 String condition ="";
                                 String score = "";
                                for (int l = 0; l < outcome_nod.getAttributes().getLength(); l++) {
                                    if (outcome_nod.getAttributes().item(l).getNodeName() != null&& outcome_nod.getAttributes().item(l).getNodeName().equalsIgnoreCase("condition")) {
                                        condition = outcome_nod.getAttributes().item(l).getNodeValue();
                                    }
                                    if (outcome_nod.getAttributes().item(l).getNodeName() != null&& outcome_nod.getAttributes().item(l).getNodeName().equalsIgnoreCase("score")) {
                                        score = outcome_nod.getAttributes().item(l).getNodeValue();
                                    }
                                }
                                if(score != null && score.length() >0){
                                    cre_condition = cre_condition + condition +"~";
                                }
                            }
                        }
                        if(cre_condition.endsWith("~")){
                            cre_condition = cre_condition.substring(0,cre_condition.lastIndexOf("~") );
                        }
                        que_map.put("correct_answer", cre_condition);
                    }
                }
            }
        }
        return que_list;
    }
   
}