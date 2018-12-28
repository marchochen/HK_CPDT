package com.cw.wizbank.qdb;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;

public class qdbQuiz extends Object {
    /**
     * CLOB column
     * Table:       Interaction
     * Column:      int_xml_outcome     int_xml_explain
     * Nullable:    YES                 YES
     */
    // define the no. of residual questions not included in next generation
    static int QSEQ_LAST_N = 0;

    private String sqlGetQMinus_;  // helper SQL
    private String sqlGetQCount_;  // helper SQL

    private PreparedStatement stGetSubId_; // get subject id
    private PreparedStatement stGetChildObjList_;  // get a list of child objective given an obj_id
    private PreparedStatement stGetQMinus_; // get (Q-Ln)
    private PreparedStatement stGetQSeqCount_; // get count of current QSeq
    private PreparedStatement stGetQCount_; // get count of settotal questions for given subject
    private PreparedStatement stResetQSeq_; // reset QSeq table abd QPtr
    private PreparedStatement stInsQSeq_; // insert into QSeq table
    private PreparedStatement stGetQPtr_; // Get QPointer (subId)
    private PreparedStatement stSetQPtr_; // Set QPointer (subId, subId, order)
    private PreparedStatement stSetMaxPtr_; // Set Maximum QPointer (subId, subId, order)
    private PreparedStatement stGetQSeqCountLeft_; // Count how many left for QSeq according to QPtr(subId)

    private PreparedStatement stGetQuiz_; // Get Questions from QSeq (subId, start, end)

    private long subId_  = 0;
    private String sub_ = "";
    private boolean isAssObj = false;

    public qdbQuiz(Connection con, long subId, String sub)
        throws qdbException
    {

        // validate input param
        if( subId<=0)
            throw new qdbException("Invalid subject name or id.");

        subId_ = subId;
        sub_ = sub;


       //try {
            dbObjective dbobj = new dbObjective();
            dbobj.obj_id = subId_;
            dbobj.get(con);
            //if (dbobj.obj_type.equalsIgnoreCase(dbObjective.OBJ_TYPE_ASS)) {
            //    isAssObj = true;
            //}

        // prepare statements and get some info from db

        stGetSubId_ = null;
        /*
        con.prepareStatement(
            "SELECT obj_id "
         +  "  FROM Objective "
         +  " WHERE obj_desc = ? " );
        */

        stGetQSeqCount_ = null;
        /*con.prepareStatement(
           "SELECT  max(qse_order) "
         + "  FROM  QSequence "
         + " WHERE  qse_obj_id = ? ");
        */

        sqlGetQCount_ = "";

        stGetQCount_ = null;

        sqlGetQMinus_ = "" ;

        stGetQMinus_ = null;

        /*
        stResetQSeq_ = con.prepareStatement(
            " if not exists (select qpt_qse_obj_id "
          + "               from QPointer where qpt_qse_obj_id = ?) "
          + "   INSERT QPointer (qpt_qse_obj_id, qpt_qse_order_ptr) VALUES (?, 0) "
          + " if exists (select qpt_qse_obj_id from QPointer where qpt_qse_obj_id = ?) "
          + "   UPDATE QPointer set qpt_qse_order_ptr = 1 where qpt_qse_obj_id = ? "
          + " DELETE From QSequence "
          + "  WHERE qse_obj_id = ? " );
        */

        stInsQSeq_ = null;
        /*con.prepareStatement(
            " INSERT INTO QSequence "
          + " ( qse_obj_id "
          + "  ,qse_order  "
          + "  ,qse_que_res_id "
          + "  ,qse_obj_id_ass "
          + "  ,qse_obj_id_cat ) "
          + " VALUES "
          + " ( ?, ?, ?, ?, ? ) " );
        */


        stGetQPtr_ = null;
        /*con.prepareStatement(
            " SELECT qpt_qse_order_ptr "
          + "   FROM QPointer "
          + "  WHERE qpt_qse_obj_id = ? " )  ;
        */

        stSetQPtr_ = null;
        /*con.prepareStatement(
          " UPDATE QPointer set qpt_qse_order_ptr = ? "
          + " where qpt_qse_obj_id = ? " );
        */

        stSetMaxPtr_ = null;
        /*con.prepareStatement(
          " UPDATE QPointer set qpt_max_ptr = ? "
          + " where qpt_qse_obj_id = ? " );
        */

        stGetQSeqCountLeft_ = null;
        /*con.prepareStatement(
            " SELECT count(qse_order) "
          + "   FROM QSequence, QPointer "
          + "  WHERE qse_obj_id = ? "
          + "    AND qpt_qse_obj_id = qse_obj_id "
          + "    AND qse_order >= qpt_qse_order_ptr " );
        */

        // *JL* G3 changed to get category obj instead of ass obj
        stGetQuiz_ = null;
        /*con.prepareStatement(
            " SELECT res_id "
          + "       ,res_title "
          + "       ,res_difficulty "
          + "       ,res_usr_id_owner "
          + "       ,usr_display_bil "
          + "       ,res_status "
          + "       ,res_upd_date "
          + "       ,que_xml "
          + "       ,que_score "
          + "       ,int_xml_outcome "
          + "       ,int_xml_explain "
          + "       ,qse_obj_id_ass "
          + "       ,obj_desc "
          + "  FROM  Resources, Question, Interaction, QSequence, Objective, RegUser "
          + " WHERE res_id      = que_res_id "
          + "   AND res_id      = int_res_id "
          //+ "   AND res_usr_id_owner *= usr_id "
          + "   AND usr_id " + cwSQL.get_right_join(con) + " res_usr_id_owner "
          + "   AND int_order   = 1 "
          + "   AND res_id      = qse_que_res_id "
          //+ "   AND qse_obj_id_ass = obj_id "
          //+ "   AND qse_obj_id_cat *= obj_id "
          + "   AND obj_id " + cwSQL.get_right_join(con) +  " qse_obj_id_cat "
          + "   AND qse_obj_id  = ? "
          + "   AND qse_order   BETWEEN ? AND ? "  );
          */

/*      }
      catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }*/
    }
    public void setQOption(Connection con, dbQuestion dbque)
        throws qdbException
    {
        String options = "";
        if (dbque.res_lan != null && dbque.res_lan.length() > 0)
            options += "    AND res_lan = '" + dbque.res_lan + "' " ;
        if (dbque.res_difficulty >  0 && dbque.res_difficulty < 3)
            options += "    AND res_difficulty = " + dbque.res_difficulty ;
        if (dbque.res_privilege != null && dbque.res_privilege.length() > 0)
            options += "    AND res_privilege = '" + dbque.res_privilege + "' " ;
        if (dbque.que_type != null && dbque.que_type.length() > 0)
            options += "    AND que_type = '" + dbque.que_type + "' " ;
        if (dbque.que_score >  0 )
            options += "    AND que_score = " + dbque.que_score ;
        if (dbque.que_prog_lang != null ) {
            if (!dbque.que_prog_lang.equalsIgnoreCase(dbQuestion.QUE_LANG_ALL))
                options += "    AND que_prog_lang = '" + dbque.que_prog_lang + "'" ;
        }else
            options += "    AND que_prog_lang is null ";
        if (!dbque.que_media_ind)
            options += "    AND que_media_ind = 0 ";

         sqlGetQCount_ =
           "SELECT  count(res_id) "
         + "  FROM  Resources , Question "
         + "       ,ResourceObjective, ObjectiveRelation "
         + " WHERE      res_id = rob_res_id "
         + "   AND  res_id = que_res_id "
         + "   AND  res_status = 'ON'       "
         + "   AND  res_type   = 'QUE'      "
         + "   AND  res_res_id_root is null "    // not in the test
         + options                               // options list
         + "   AND  ore_obj_id = rob_obj_id ";


         sqlGetQMinus_ =
           "SELECT  res_id "
        + "        ,rob_obj_id "
        + " FROM    Resources , Question "
        + "        ,ResourceObjective, ObjectiveRelation "
        + " WHERE "
        + "   res_id NOT IN  "
        + "  (SELECT qse_que_res_id FROM QSequence "
        + "   WHERE qse_obj_id = ? "
        + "     AND qse_order BETWEEN ? AND ? ) "
        + "   AND   res_id = que_res_id "
        + "   AND   rob_res_id = res_id "
        + "   AND   res_status = 'ON' "
        + "   AND   res_type   = 'QUE' "         // JLAI 6/24/00: check res_type = 'QUE'
        + options
        + "   AND  ore_obj_id = rob_obj_id ";

    }


    public String getNextQuizInXML(Connection con, loginProfile prof, Timestamp startTime, int n, String template)
        throws qdbException
    {
        String result = "";

        // validate if there're enough questions left in QSeq for this quiz
        long qLeft = 0;

        qLeft = getQLeftCount(con);
        if( qLeft < n )
            throw new qdbException("Not enough questions left: " + qLeft);

        // Ok, everything is fine, format result
        // xml header
        result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL + dbUtils.NEWL;

        result += getQuizHeaderInXML(prof,startTime,n,template);

        result += getQuizContentInXML(con, n, prof) + dbUtils.NEWL;

        result += "</quiz>";

        return result;
    }

   private String getQuizHeaderInXML(loginProfile prof, Timestamp startTime, int n,  String template)
    {
        String result = "";

        result += "<quiz subject=\"" + sub_ + "\" size=\"" + n +  "\" template=\"" + template +  "\" timestamp=\"" + startTime + "\">" + dbUtils.NEWL;
        // author's information
        result += prof.asXML() + dbUtils.NEWL;

        result += "<key>" + startTime.getSeconds() + "</key>" + dbUtils.NEWL;

        return result;
    }

    // get next quiz, no of questions = n
    private String getQuizContentInXML(Connection con, int n, loginProfile prof)
        throws qdbException
    {
        try {
            // Get current QPtr
            long startNo=0;
            long endNo=0;
            PreparedStatement stmt = con.prepareStatement(
            " SELECT qpt_qse_order_ptr "
          + "   FROM QPointer "
          + "  WHERE qpt_qse_obj_id = ? " )  ;
            stmt.setLong(1, subId_);
            ResultSet rs1 = stmt.executeQuery();
            if(rs1.next())
                startNo = rs1.getLong(1);           
            stmt.close(); 

            //if (!(startNo>0))
            if (startNo<0)
                throw new qdbException("Failed to get current QPtr.");

            endNo = startNo + n - 1;
            // Get Quiz to generate xml
			stmt = con.prepareStatement(OuterJoinSqlStatements.qdbQuizGetQuizContentInXML());
//            stmt = con.prepareStatement(
//            " SELECT res_id "
//          + "       ,res_title "
//          + "       ,res_difficulty "
//          + "       ,res_usr_id_owner "
//          + "       ,usr_display_bil "
//          + "       ,res_status "
//          + "       ,res_upd_date "
//          + "       ,que_xml "
//          + "       ,que_score "
//          + "       ,int_xml_outcome "
//          + "       ,int_xml_explain "
//          + "       ,qse_obj_id_ass "
//          + "       ,obj_desc "
//          + "  FROM  Resources, Question, Interaction, QSequence, Objective, RegUser "
//          + " WHERE res_id      = que_res_id "
//          + "   AND res_id      = int_res_id "
//          //+ "   AND res_usr_id_owner *= usr_id "
//          + "   AND usr_id " + cwSQL.get_right_join(con) + " res_usr_id_owner "
//          + "   AND int_order   = 1 "
//          + "   AND res_id      = qse_que_res_id "
//          //+ "   AND qse_obj_id_ass = obj_id "
//          //+ "   AND qse_obj_id_cat *= obj_id "
//          + "   AND obj_id " + cwSQL.get_right_join(con) +  " qse_obj_id_cat "
//          + "   AND qse_obj_id  = ? "
//          + "   AND qse_order   BETWEEN ? AND ? "  );
            stmt.setLong(1, subId_);
            stmt.setLong(2, startNo);
            stmt.setLong(3, endNo);
            rs1 = stmt.executeQuery();

            String rstQLst = "";
            long     id;
            String  title="";
            String  lan="";
            int     diff;
            String  owner="";
            String  owner_display="";
            String  status;
            int     score;
            Timestamp updDate;
            String  qxml="";
            String  ixmlo="";
            String  ixmle="";
            long assObjId;
            String assObj="";

            String xmlOwner = "";
            if( !rs1.wasNull() )
                while(rs1.next())
                {
                    id = rs1.getLong("res_id");
                    title = rs1.getString("res_title");
                    lan = rs1.getString("res_lan");
                    diff = rs1.getInt("res_difficulty");
                    status = rs1.getString("res_status");
                    owner = rs1.getString("res_usr_id_owner");
                    owner_display = dbUtils.esc4XML(rs1.getString("usr_display_bil"));
                    updDate = rs1.getTimestamp("res_upd_date");
                    // for oracle clob
                    qxml = cwSQL.getClobValue(rs1, "que_xml");
//                    qxml = rs1.getString("que_xml");

                    score = rs1.getInt("que_score");

                    // << BEGIN for oracle migration!
                    //ixmlo = rs1.getString("int_xml_outcome");
                    //ixmle = rs1.getString("int_xml_explain");
                    ixmlo = cwSQL.getClobValue(rs1, "int_xml_outcome");
                    ixmle = cwSQL.getClobValue(rs1, "int_xml_explain");
                    // >> END


                    assObjId = rs1.getLong("qse_obj_id_ass");
                    assObj = rs1.getString("obj_desc");

                    // check owner
                    if(owner!=null && owner.length()>0)
                    {
                        /*
                        if (prof.sysRoles.contains("ADMIN"))
                        //if(dbRegUser.hasRole(con, owner, "ADMIN"))
                            xmlOwner = "admin";
                        else
                        */
                            xmlOwner = dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,owner));

                    }
                    if(owner_display==null)
                        owner_display="";

                    // format the question list
                    rstQLst += "<question id=\"" + id + "\" language=\"" + lan + "\" timestamp=\"" + updDate + "\" owner=\"" + xmlOwner
                    + "\" owner_display=\"" + owner_display + "\">" + dbUtils.NEWL;
                    rstQLst += "<header difficulty=\"" + diff + "\" status=\"" + status + "\">" + dbUtils.NEWL;
                    rstQLst += "<title>" + title + "</title>" + dbUtils.NEWL;
                    rstQLst += "<objective id=\"" + assObjId + "\">" + cwUtils.esc4XML(assObj) +  "</objective>" + dbUtils.NEWL;
                    rstQLst += "</header>" + dbUtils.NEWL;
                    rstQLst += qxml + dbUtils.NEWL;
                    rstQLst += ixmlo + dbUtils.NEWL;
                    rstQLst += "</question>" + dbUtils.NEWL + dbUtils.NEWL;
                }
            else
            {
            	stmt.close();
                throw new qdbException("Failed to get Questions.");
            }
            stmt.close();

            // Set QPtr
            stmt = con.prepareStatement(
          " UPDATE QPointer set qpt_qse_order_ptr = ? "
          + " where qpt_qse_obj_id = ? " );
            stmt.setLong(1, endNo+1);
            stmt.setLong(2, subId_);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                throw new qdbException("Failed to set new QPtr.");
            }

            return rstQLst;
       } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }

   }

     public void recycleQ(Connection con)
        throws qdbException, IOException
    {
        Vector qIDs = refreshQBuffer(con);
        qUnit[] qIDsWithOrder = shuffleQBuffer(qIDs);
        saveQBuffer(con, qIDsWithOrder);
    }

    private Vector refreshQBuffer(Connection con)
          throws qdbException, IOException
    {
        Vector result = new Vector();

        // select (Q-Ln)
        // get count of current QSeq table
        int qSeqCount = 0;
      try{
        PreparedStatement stmt = con.prepareStatement(
           "SELECT  max(qse_order) "
         + "  FROM  QSequence "
         + " WHERE  qse_obj_id = ? ");
        stmt.setLong(1, subId_);
        ResultSet rs = stmt.executeQuery();
        if (!rs.wasNull())
        {
            rs.next();
            qSeqCount  = rs.getInt(1);
        }
        // get count of total questions for the subject
        // get list of child obj given subId_
        stmt.close();

        String cobjs = "";

        // CL (17 Dec, 2001) : Retired
        /*
        if (isAssObj)
            cobjs = "  AND ore_obj_id = " + subId_;
        else {

            //cobjs = getSybObjs(subId_);
            cobjs = "   AND  ore_type = 'ASSTOSYB' "
                 +  "   AND  ore_obj_id_referenced IN "

                 //+  dbObjective.getChildSybId(con, subId_);
                 +
        }
        */

        String sql1 = sqlGetQCount_ + cobjs;
        //stGetQCount_ = con.prepareStatement(sql1);

        int qCount = 0;
        stmt =  con.prepareStatement(sql1);
        stmt.setLong(1, subId_);
        rs = stmt.executeQuery();
        if (!rs.wasNull())
        {
            rs.next();
            qCount  = rs.getInt(1);
        }
        else
        {
        	stmt.close();
            throw new qdbException("Failed to count no. of questions.");
        }
        stmt.close();

        // get (Q-Ln)
        int Ln = (qSeqCount==0) ? 0 : QSEQ_LAST_N;
        int newQSeqLen = qCount - Ln;
        if( newQSeqLen <=0 )
            throw new qdbException("Total no. of questions (" + qCount + ") < predefined residual value ("
            + QSEQ_LAST_N + ").");


        // prepare stGetQMinus_
        String sql2 = sqlGetQMinus_ + cobjs;
        //stGetQMinus_ = con.prepareStatement(sql2);

        // use a Hashtable to hold ass-cat obj mapping
        Hashtable tblAC = new Hashtable();

        long qId, qObjAss, qObjCat;
        String qObjCatStr = null;
        stmt = con.prepareStatement(sql2);
        stmt.setLong(1, subId_);
        stmt.setLong(2, qSeqCount-QSEQ_LAST_N+1);
        stmt.setLong(3, qSeqCount);
        rs = stmt.executeQuery();
        int i = 1;
        if (!rs.wasNull())
          while(rs.next())
          {
            qId     = rs.getLong("res_id");
            qObjAss = rs.getLong("rob_obj_id");
            // try to get Cat Obj Id from hashtable
            if (isAssObj) {
                qObjCat = qObjAss;
            }else {
                qObjCatStr = (String) tblAC.get(String.valueOf(qObjAss));
                if(qObjCatStr==null)
                {
                    qObjCat = getCatObjId(con, qObjAss, subId_);
                    tblAC.put(String.valueOf(qObjAss), String.valueOf(qObjCat));
                }
                else
                    qObjCat = Long.parseLong(qObjCatStr);
            }
            result.addElement(new qUnit(qId, qObjAss, qObjCat));
          }
        else
        {
        	stmt.close();
            throw new qdbException("Failed to get questions.");
        }
        stmt.close();
      } catch (SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

        return result;
    }

    // shuffle buffer
    private qUnit[] shuffleQBuffer(Vector v)
    {
        int qCount = v.size();
        qUnit[] result = new qUnit[qCount + 1];
        Random rGen = new Random();
        //Math m = new Math();
        float r = 0;
        int seq = 0;
        qUnit qu =null;
        long vId, vObjAss, vObjCat;
        int remainCount = 0;
        for(int i=1;i<=qCount;i++)
        {
          remainCount = v.size();
          // use seq = r * (Q-1)  then seq in 0 to Q-1
          r = rGen.nextFloat();
          r = r * (remainCount-1);
          seq = Math.round(r);
          // remove element from v at seq and set to the ith
          qu = (qUnit)v.elementAt(seq);
          vId = qu.id;
          vObjAss = qu.obj_id_ass;
          vObjCat = qu.obj_id_cat;
          v.removeElementAt(seq);
          result[i] = new qUnit(vId, vObjAss, vObjCat);
        }

        return result;
    }

    // write buffer
    private void saveQBuffer(Connection con, qUnit[] buff)
        throws qdbException
    {
        // delete existing records in QSeq
      try{
        boolean auto = con.getAutoCommit();
        con.setAutoCommit(true);

        //PreparedStatement stmt =

        PreparedStatement stmt = con.prepareStatement(
         "select qpt_qse_obj_id from QPointer where qpt_qse_obj_id = ? ");

        stmt.setLong(1, subId_);
        ResultSet rs = stmt.executeQuery();

        String SQL = new String();
        // if exist
        if (rs.next()) {
            SQL =
                 " UPDATE QPointer set qpt_qse_order_ptr = 1 where qpt_qse_obj_id = ? " ;

        }else {
            SQL =
                " INSERT QPointer (qpt_qse_obj_id, qpt_qse_order_ptr) VALUES (?, 0) ";
        }
        stmt.close();

        stmt = con.prepareStatement(SQL);
        stmt.setLong(1, subId_);
        int stmtResult=stmt.executeUpdate();
        stmt.close();
        if ( stmtResult!=1)                            
        {
            throw new qdbException("Failed to insert qpointer.");
        }
        

        stmt = con.prepareStatement(
                " DELETE From QSequence WHERE qse_obj_id = ? " );
        stmt.setLong(1, subId_);
        stmt.executeUpdate();
        stmt.close();

        int u = 0;
        // insert into QSeq
        stmt = con.prepareStatement(
            " INSERT INTO QSequence "
          + " ( qse_obj_id "
          + "  ,qse_order  "
          + "  ,qse_que_res_id "
          + "  ,qse_obj_id_ass "
          + "  ,qse_obj_id_cat ) "
          + " VALUES "
          + " ( ?, ?, ?, ?, ? ) " );
        for(int i=1;i<buff.length;i++)
        {
            if(buff[i]==null)
                break;
            stmt.setLong(1, subId_);
            stmt.setInt(2, i);
            stmt.setLong(3, buff[i].id);
            stmt.setLong(4, buff[i].obj_id_ass);
            stmt.setLong(5, buff[i].obj_id_cat);
            stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.setAutoCommit(auto);
                throw new qdbException("Failed to insert QSeq.");
            }
        }
        // set QPtr
        stmt = con.prepareStatement(
          " UPDATE QPointer set qpt_qse_order_ptr = ? "
          + " where qpt_qse_obj_id = ? " );
        stmt.setLong(1, 1);
        stmt.setLong(2, subId_);
        u = stmt.executeUpdate();
        stmt.close();
        if(u<=0)
        {
            con.setAutoCommit(auto);
            throw new qdbException("Failed to set QPtr.");
        }
        // set MaxPtr
        stmt = con.prepareStatement(
          " UPDATE QPointer set qpt_max_ptr = ? "
          + " where qpt_qse_obj_id = ? " );
        stmt.setLong(1, buff.length-1);
        stmt.setLong(2, subId_);
        u = stmt.executeUpdate();
        stmt.close();
        if(u<=0)
        {
            con.setAutoCommit(auto);
            throw new qdbException("Failed to set MaxPtr.");
        }

        con.setAutoCommit(auto);
      } catch (SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }
    }

    public long getQLeftCount(Connection con)
        throws qdbException
    {
       //long qLeft;
       long maxSeq, curPtr;
       long lResult;
       try {

        PreparedStatement stmt2 = con.prepareStatement(
            "select qpt_qse_order_ptr, qpt_max_ptr from QPointer where qpt_qse_obj_id = ? ");

        stmt2.setLong(1, subId_);

        ResultSet rs2 = stmt2.executeQuery();
        if(rs2.next()) {
            curPtr = rs2.getLong(1);
            maxSeq = rs2.getLong(2);
        } else
        {
        	stmt2.close();
            return 0;
        }
            //throw new qdbException("Failed to count Q left for the subject: maxSeq not found.");
        stmt2.close();

        if (maxSeq == 0)
            return maxSeq;
        else
            return maxSeq - curPtr + 1;

       } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }


  // get the category objective id under rootObjId for an assessment obj
    private long getCatObjId(Connection con, long assObjId, long rootObjId)
          throws qdbException, IOException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            " SELECT ore_obj_id_referenced S, " + assObjId + " S1, NULL+0 S2, NULL+0 S3, NULL+0 S4, NULL+0 S5, NULL+0 S6 "
           + "  FROM ObjectiveRelation "
           + " WHERE ore_obj_id = " + assObjId
           + "   AND ore_obj_id_referenced = " + rootObjId
           + " UNION "
           + " SELECT O1.obj_obj_id_parent S, ore_obj_id_referenced S1, " + assObjId + " S2, NULL+0 S3, NULL+0 S4, NULL+0 S5, NULL+0 S6 "
           + "  FROM ObjectiveRelation, Objective O1 "
           + " WHERE ore_obj_id = " + assObjId
           + "   AND ore_obj_id_referenced = O1.obj_id "
           + "   AND O1.obj_obj_id_parent = " + rootObjId
           + " UNION "
           + " SELECT O2.obj_obj_id_parent S, O1.obj_obj_id_parent S1, ore_obj_id_referenced S2, " + assObjId + " S3, NULL+0 S4, NULL+0 S5, NULL+0 S6 "
           + " FROM ObjectiveRelation, Objective O1, Objective O2 "
           + " WHERE ore_obj_id = " + assObjId
           + "   AND ore_obj_id_referenced = O1.obj_id "
           + "   AND O1.obj_obj_id_parent = O2.obj_id "
           + "   AND O2.obj_obj_id_parent = " + rootObjId
           + " UNION "
           + " SELECT O3.obj_obj_id_parent S, O2.obj_obj_id_parent S1, O1.obj_obj_id_parent S2, ore_obj_id_referenced S3, " + assObjId + " S4, NULL+0 S5, NULL+0 S6 "
           + " FROM ObjectiveRelation, Objective O1, Objective O2, Objective O3 "
           + " WHERE ore_obj_id = " + assObjId
           + "   AND ore_obj_id_referenced = O1.obj_id "
           + "   AND O1.obj_obj_id_parent = O2.obj_id "
           + "   AND O2.obj_obj_id_parent = O3.obj_id "
           + "   AND O3.obj_obj_id_parent = " + rootObjId
           + " UNION "
           + " SELECT O4.obj_obj_id_parent S, O3.obj_obj_id_parent S1, O2.obj_obj_id_parent S2, O1.obj_obj_id_parent S3, ore_obj_id_referenced S4, " + assObjId + " S5, NULL+0 S6 "
           + " FROM ObjectiveRelation, Objective O1, Objective O2, Objective O3, Objective O4 "
           + " WHERE ore_obj_id = " + assObjId
           + "   AND ore_obj_id_referenced = O1.obj_id "
           + "   AND O1.obj_obj_id_parent = O2.obj_id "
           + "   AND O2.obj_obj_id_parent = O3.obj_id "
           + "   AND O3.obj_obj_id_parent = O4.obj_id "
           + "   AND O4.obj_obj_id_parent = " + rootObjId
           + " UNION "
           + " SELECT O5.obj_obj_id_parent S, O4.obj_obj_id_parent S1, O3.obj_obj_id_parent S2, O2.obj_obj_id_parent S3, O1.obj_obj_id_parent S4, ore_obj_id_referenced S5, " + assObjId + " S6 "
           + " FROM ObjectiveRelation, Objective O1, Objective O2, Objective O3, Objective O4, Objective O5 "
           + " WHERE ore_obj_id = " + assObjId
           + "   AND ore_obj_id_referenced = O1.obj_id "
           + "   AND O1.obj_obj_id_parent = O2.obj_id "
           + "   AND O2.obj_obj_id_parent = O3.obj_id "
           + "   AND O3.obj_obj_id_parent = O4.obj_id "
           + "   AND O4.obj_obj_id_parent = O5.obj_id "
           + "   AND O5.obj_obj_id_parent = " + rootObjId  );

           ResultSet rs = stmt.executeQuery();
           long S1 = 0;
           if(rs.next())
              S1 = rs.getLong("S1");
           stmt.close();
           return S1;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

}

// data class to hold question id's
// *JL* add obj_id_cat for category display
class qUnit extends Object
{
    public long id;
    public long obj_id_ass;
    public long obj_id_cat;
    public qUnit(long i, long a, long c)
    {
        id = i;
        obj_id_ass = a;
        obj_id_cat = c;
    }
}