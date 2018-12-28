/*
 * Created on 2005-4-18
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.ae.db.view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.report.SurveyQueData;
import com.cw.wizbank.report.SurveyQueReport;
import com.cw.wizbank.report.SurveyQueReportXls;
import com.cw.wizbank.report.SurveyReport;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewSurveyQueReport {
    private Connection con;
    static final boolean BOOLEAN_ITY_RUN = false;
    public ViewSurveyQueReport(Connection inCon) {
        this.con = inCon;
    }
    public class HeadData {
        public ArrayList cos_catalog = new ArrayList();
        public ArrayList cos_title = new ArrayList();
        public String mod_title;
        public String itm_type;
        public String ity_title_xml;
        public String period_from;
        public String period_to;
        public String rte_id;
        public String rte_get_xsl;
        public String rte_exe_xsl;
        public String rte_dl_xsl;
    }

public static final String TEMP_TAB_COLUMN_NAME= "item_id";
    public static final String TND_TYPE_ITEM = "ITEM";
    public static final String TND_TYPE_NORMAL = "NORMAL";
    /**
     * @parm mod_res_id     Report template resources id
     * 
     * @return vector with res_id,res_title and so on
     */

    static final String sql_get_svy_rpt_tpl = 
      "select rcn_order, rcn_res_id_content, que_type, que_xml, int_xml_outcome, res_que.res_title " 
    + "from Resources res_tpt, ResourceContent, Question, Interaction, Resources res_que " 
    + "where res_tpt.res_id = ? " 
    + "and res_tpt.res_id = rcn_res_id " 
    + "and rcn_res_id_content = que_res_id " 
    + "and que_res_id = int_res_id "
    + "and rcn_res_id_content = res_que.res_id "
    + "order by rcn_order ";

    /*
     * @param mod_res_id     module id
     * @param con            sql connection
     */
    public ArrayList getTemplateQue(long mod_res_id) throws cwException, SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_get_svy_rpt_tpl);
        stmt.setLong(1, mod_res_id);
        ResultSet rs = stmt.executeQuery();
        ArrayList al = new ArrayList();
        String regEx_html="<[^>]+>";
        while (rs.next()) {
            SurveyQueData gx = new SurveyQueData();
            gx.res_title = rs.getString("res_title");
            gx.order = rs.getLong("rcn_order");
            gx.res_id = rs.getLong("rcn_res_id_content");
            gx.queType = rs.getString("que_type");
            gx.que_xml = cwSQL.getClobValue(rs, "que_xml");
            gx.parseString(cwSQL.getClobValue(rs, "int_xml_outcome"));
            gx.parseString(gx.que_xml);
            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(gx.que_text);
            gx.que_text = m_html.replaceAll("");
            Pattern p_html_n=Pattern.compile("(\r\n|\r|\n|\n\r)"); 
            Matcher m_html_n = p_html_n.matcher(gx.que_text);
            gx.que_text = m_html_n.replaceAll("");
            al.add(gx);
        }
        stmt.close();
        return al;
    }

    /**
     * @param itm_id           course id
     * @param tnd_id           treeNode id
     * @param itm_dummy_type         
     * @param question_type    mc or fb
     * @param itm_dummy_type         course type(SELFSTUDY and so on)
     * @param period_from,period_to    time of begining and ending of a test
     * @param mod_res_id       module id
     * @param tempVec          data about templeate
     * 
     */
    public void getDetail(ArrayList tempList,
                          SurveyQueReport.SurveyQueRptParam param,
                          String userSpecifiedItem,
                          long responseCount)
    					  throws SQLException, IOException {
        StringBuffer sql = new StringBuffer();
        sql.append("select atm_order, atm_response_bil, count(*) response_count from ProgressAttempt, Progress, aeApplication, aeAttendance ")
        .append(" where atm_pgr_res_id in ( ")
        .append(" select mod_res_id from Module ")
        .append(" where mod_mod_id_root = ").append(param.mod_res_id)
        .append(" and mod_res_id in ( ")
        .append(" select rcn_res_id_content from ResourceContent ")
        .append(" where rcn_res_id in ( ")
        .append(" select cos_res_id from Course, aeItem ")
        .append(" where cos_itm_id = itm_id ")
        .append(" and itm_id in (").append("select item_id from ").append(userSpecifiedItem).append(")")
        .append(aeItemDummyType.genSqlByItemDummyType(param.itm_dummy_type,null,true))
        .append(")))")
        .append(" and atm_order in ( ")
        .append(" select rcn_order ")
        .append(" from Resources, ResourceContent, Question, Interaction ")
        .append(" where res_id = ").append(param.mod_res_id)
        .append(" and res_id = rcn_res_id ")
        .append(" and rcn_res_id_content = que_res_id ")
        .append(" and que_res_id = int_res_id ) ")
        .append(" and atm_pgr_usr_id = pgr_usr_id ")
        .append(" and atm_pgr_res_id = pgr_res_id ")
        .append(" and atm_pgr_attempt_nbr = pgr_attempt_nbr ")
        .append(" and atm_tkh_id = pgr_tkh_id ")
        .append(" and pgr_tkh_id = app_tkh_id")
        .append(" and app_id = att_app_id");
        if (param.period_from != null) {
            sql.append(" and att_create_timestamp >= ?");
        }
        if (param.period_to != null) {
            sql.append(" and att_create_timestamp <= ?");
        }
        if (param.que_id > 0) {
            sql.append(" and atm_order = ?");
        }
        sql.append(" group by atm_order, atm_response_bil ").append(" order by atm_order");
        PreparedStatement stmt = con.prepareStatement(sql.toString());

        int idx = 1;
        if (param.period_from != null) {
            stmt.setTimestamp(idx++, param.period_from);
        }
        if (param.period_to != null) {
            stmt.setTimestamp(idx++, param.period_to);
        }
        if (param.que_id > 0) {
            stmt.setLong(idx++, param.que_id);
        }

        ResultSet rs = stmt.executeQuery();
        int preOrder = 0;
        SurveyQueData gx = null;
        int FBRowCount = 0;
        boolean showAll = param.isShowAllFBAns || param.isXlsFile;
        while (rs.next()) {
            int atm_order = rs.getInt("atm_order");
            String atm_response_bil = rs.getString("atm_response_bil");
            int response_count = rs.getInt("response_count");

            gx = (SurveyQueData)tempList.get(atm_order - 1);
            
            gx.countResponse(atm_response_bil, response_count, param.isXlsFile, param.isShowAllFBAns);

            if (atm_order != preOrder && preOrder != 0) {
                gx = (SurveyQueData)tempList.get(preOrder - 1);
                gx.calculateStat(responseCount);
            }
            preOrder = atm_order;
        }
        stmt.close();
        if (preOrder!=0){
            gx = (SurveyQueData)tempList.get(preOrder - 1);
            gx.calculateStat(responseCount);
        }            
    }

    public String getUserSpecifiedItem(SurveyQueReport.SurveyQueRptParam param) throws SQLException {
        // the set to hold all the courses satisfying the report criteria
        Vector itemSet = new Vector();
        
        // get courses according to tnd_id and itm_type
        int tndLen = 0;
        if (param.tnd_id != null) {
            tndLen = param.tnd_id.length;
        }
        for (int i = 0; i < tndLen; i++) {
            getNodeItems(param.tnd_id[i],param.itm_dummy_type, itemSet);
        }
        
        // get courses according to itm_id
        if (param.itm_id != null) {
            for (int i = 0; i < param.itm_id.length; i++) {
                itemSet.add(new Long(param.itm_id[i]));
            }
        }
        
        // get classes of the courses and select only the courses/classes that use the specified survey template
        if (itemSet != null) {
            String tmpTabName = null;
            tmpTabName = cwSQL.createSimpleTemptable(con, TEMP_TAB_COLUMN_NAME, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tmpTabName, itemSet, cwSQL.COL_TYPE_LONG);
            
            MYSQLDbHelper mysqlDbHelper = null;
 		   boolean isMysql = false;
 			if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
 				mysqlDbHelper = new MYSQLDbHelper();
 				isMysql = true;
 			}
 			 String physicalTableName = null;
 			if(isMysql){
 				physicalTableName = mysqlDbHelper.tempTable2Physical(con, tmpTabName);
			}
            
            StringBuffer sqlItem = new StringBuffer();
            sqlItem.append("select ire_child_itm_id ")
                   .append("from aeItemRelation, Course ")
                   .append("where ire_parent_itm_id in (select item_id from ").append((isMysql==true?physicalTableName:tmpTabName)).append(")")
                   .append("and cos_itm_id = ire_child_itm_id ")
                   .append("and cos_res_id in(")
                   .append("select rcn_res_id from ResourceContent ")
                   .append("where rcn_res_id_content in(")
                   .append("select mod_res_id from Module ")
                   .append("where mod_mod_id_root = ?))")
                   .append("union ")
                   .append("select itm_id from aeItem, Course ")
                   .append("where itm_id in (select item_id from ").append(tmpTabName).append(")")
                   .append("and itm_id = cos_itm_id ")
                   .append("and cos_res_id in(")
                   .append("select rcn_res_id from ResourceContent ")
                   .append("where rcn_res_id_content in(")
                   .append("select mod_res_id from Module ")
                   .append("where mod_mod_id_root = ?))");
            PreparedStatement stmt = con.prepareStatement(sqlItem.toString());
            int idx = 1;
            stmt.setLong(idx++, param.mod_res_id);
            stmt.setLong(idx++, param.mod_res_id);
            ResultSet rs = stmt.executeQuery();
            
            itemSet.clear();
            while (rs.next()) {
                itemSet.add(new Long(rs.getLong(1)));
            }
            stmt.close();
            
            cwSQL.dropTempTable(con, tmpTabName);
            if(isMysql){
				mysqlDbHelper.dropTable(con, physicalTableName);
			}
        }

        // if both course catalog and course are not specified, get courses using the specified survey template 
        if (param.tnd_id == null && param.itm_id == null) {
            StringBuffer sqll = new StringBuffer();
            sqll.append("select itm_id from aeItem, Course ")
                .append("where itm_id = cos_itm_id ")
                .append("and cos_res_id in(")
                .append("select rcn_res_id from ResourceContent ")
                .append("where rcn_res_id_content in(")
                .append("select mod_res_id from Module ")
                .append("where mod_mod_id_root = ?))");
            sqll.append(aeItemDummyType.genSqlByItemDummyType(param.itm_dummy_type,null,true));
            PreparedStatement stmt = con.prepareStatement(sqll.toString());
            stmt.setLong(1, param.mod_res_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                itemSet.add(new Long(rs.getLong("itm_id")));
            }
            stmt.close();
        }
        
        String tmpTab = null;
        tmpTab = cwSQL.createSimpleTemptable(con, TEMP_TAB_COLUMN_NAME, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tmpTab, itemSet, cwSQL.COL_TYPE_LONG);
        return tmpTab;
    }
    
    private void getNodeItems(long tnd_id, String[] itm_type, List itemSet) throws SQLException {
        String type;
        long id;

        StringBuffer sql = OuterJoinSqlStatements.getNodeItemsSql(itm_type);
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int idx = 1;
            id = rs.getLong(idx++);
            type = rs.getString(idx++);

            if (type.equals(TND_TYPE_ITEM)) {
                itemSet.add(new Long(id));
            }
            else {
                getNodeItems(id, itm_type, itemSet);
            }
        }
        stmt.close();
    }

    /**
     * @param connection
     * @param mod_res_id    module id
     * @return      count
     */
    public long getEnrolledCount(String userSpecifiedItem, SurveyQueReport.SurveyQueRptParam param) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from aeAttendance, aeItem, Course ")
        .append("where att_itm_id = itm_id ")
        .append("and itm_id = cos_itm_id ")
        .append("and cos_res_id in ")
        .append("(select cos_res_id from Course ")
        .append("where cos_itm_id in (")
        .append("select ").append(TEMP_TAB_COLUMN_NAME).append(" from ").append(userSpecifiedItem).append(")")
        .append(aeItemDummyType.genSqlByItemDummyType(param.itm_dummy_type,null,true)).append(")");
        if (param.period_from != null) {
            sql.append(" and att_create_timestamp >= ?");
        }
        if (param.period_to != null) {
            sql.append(" and att_create_timestamp <= ?");
        }

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int idx = 1;
        if (param.period_from != null) {
            stmt.setTimestamp(idx++, param.period_from);
        }
        if (param.period_to != null) {
            stmt.setTimestamp(idx++, param.period_to);
        }

        ResultSet rs = stmt.executeQuery();
        long count = 0;
        if (rs.next()) {
            count = rs.getLong(1);
        }
        stmt.close();
        
        return count;
    }

    /**
     * @param connection
     * @param mod_res_id GetSurveyReportData.SurveyQueStatParam
     * @return
     */
    public long getResponseCount(String userSpecifiedItem, SurveyQueReport.SurveyQueRptParam param) throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from ResourceContent, Progress, aeApplication, aeAttendance ")
        .append("where rcn_res_id in ")
        .append("(select cos_res_id from Course,aeItem where cos_itm_id = itm_id and cos_itm_id in (")
        .append("select ").append(TEMP_TAB_COLUMN_NAME).append(" from ").append(userSpecifiedItem).append(")")
        .append(aeItemDummyType.genSqlByItemDummyType(param.itm_dummy_type,null,true)).append(")")
        .append(" and rcn_res_id_content in ( ")
        .append(" select mod_res_id from Module where mod_mod_id_root = ? )")
        .append(" and rcn_res_id_content = pgr_res_id ")
        .append(" and pgr_tkh_id = app_tkh_id")
        .append(" and app_id = att_app_id");
        if (param.period_from != null) {
            sql.append(" and att_create_timestamp >= ? ");
        }
        if (param.period_to != null) {
            sql.append(" and att_create_timestamp <= ?");
        }

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int idx = 1;
        stmt.setLong(idx++, param.mod_res_id);
        if (param.period_from != null) {
            stmt.setTimestamp(idx++, param.period_from);
        }
        if (param.period_to != null) {
            stmt.setTimestamp(idx++, param.period_to);
        }

        ResultSet rs = stmt.executeQuery();
        long count = 0;
        if (rs.next()) {
            count = rs.getLong(1);
        }
        stmt.close();

        return count;
    }

    /**
     * 
     */
    public HeadData getRptHead(String userTable, SurveyQueReport.SurveyQueRptParam param) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        HeadData headData = new HeadData();
        //itm list starts here
        if (userTable != null) {
            StringBuffer getItmTitle = new StringBuffer();
            getItmTitle.append("select itm_title from aeItem where itm_id in (")
                       .append("select ").append(TEMP_TAB_COLUMN_NAME).append(" from ").append(userTable).append(")");
            stmt = con.prepareStatement(getItmTitle.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                headData.cos_title.add(rs.getString("itm_title"));
            }
            stmt.close();
            cwSQL.dropTempTable(con, userTable);
        }

        //tnd list starts here	
        Vector tndidVec = new Vector();
        if (param.tnd_id!=null) {
            for (int i =0;i<param.tnd_id.length;i++) {
                tndidVec.add(new Long(param.tnd_id[i]));
            }
        }
            
        String userItem = null;
        if (tndidVec != null) {
            userItem = cwSQL.createSimpleTemptable(con, ViewSurveyQueReport.TEMP_TAB_COLUMN_NAME, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, userItem, tndidVec, cwSQL.COL_TYPE_LONG);
        }

        StringBuffer getTndTitle = new StringBuffer();
        getTndTitle.append("select tnd_title from aeTreeNode where tnd_id in (")
                   .append("select ").append(TEMP_TAB_COLUMN_NAME).append(" from ").append(userItem).append(")");

        stmt = con.prepareStatement(getTndTitle.toString());
        rs = stmt.executeQuery();
        while (rs.next()) {
            headData.cos_catalog.add(rs.getString("tnd_title"));
        }
        stmt.close();

        //get mod_title starts here
        StringBuffer getModTitle = new StringBuffer();
        getModTitle.append("select res_title from Resources where res_id = ").append(param.mod_res_id); // use parameter!
        stmt = con.prepareStatement(getModTitle.toString());
        rs = stmt.executeQuery();
        String mod_title = null;
        while (rs.next()) {
            headData.mod_title = rs.getString("res_title");
        }
        stmt.close();
        
        //get itm_type starts here
        if (param.itm_dummy_type != null) {
/*            StringBuffer type = new StringBuffer();
            for (int i = 0;i <param.itm_type.length;i++) {
                type.append(param.itm_type[i]);
                if (i!=param.itm_type.length-1) {
                    type.append(", ");
                }
            }
            if (!type.toString().equals("")) {
                headData.itm_type = type.toString();
            }

            StringBuffer ity_title_xml = new StringBuffer();
            StringBuffer getItmType = new StringBuffer();
            getItmType.append("select ity_id,ity_blend_ind,ity_exam_ind,ity_ref_ind from aeItemType where ity_id in ").append(param.itmTypeString)
            .append(" and ity_run_ind = ? ")
            .append(" and ity_owner_ent_id = ? "); // setBoolean(idx, false)
            stmt = con.prepareStatement(getItmType.toString());
            stmt.setBoolean(1,BOOLEAN_ITY_RUN);
            stmt.setLong(2,param.root_ent_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ity_title_xml.append(DbItemType.getItemTypeXml(rs.getString("ity_id"), rs.getBoolean("ity_blend_ind"),rs.getBoolean("ity_exam_ind"),rs.getBoolean("ity_ref_ind")));
            }
            headData.ity_title_xml = ity_title_xml.toString();
 			*/
        	StringBuffer type = new StringBuffer();
            for (int i = 0;i <param.itm_dummy_type.length;i++) {
                type.append(param.itm_dummy_type[i]);
                if (i!=param.itm_dummy_type.length-1) {
                    type.append(", ");
                }
            }
            if (!type.toString().equals("")) {
                headData.itm_type = type.toString();
            }
            
        	
            StringBuffer typexml = new StringBuffer();
            for(int i = 0; i < param.itm_dummy_type.length ; i++) {
            	typexml.append("<item_type id=\"" + cwUtils.escNull(param.itm_dummy_type[i]) +"\"" + " dummy_type=\"" + param.itm_dummy_type[i] + "\"/>");
            }
            headData.ity_title_xml = typexml.toString();
        }

        if (param.period_from != null || param.period_to != null) {
            if (param.period_from != null) {
                headData.period_from = param.period_from.toString();
            }
            if (param.period_to != null) {
                headData.period_to = param.period_to.toString();
            }
        }

        //        rte_type = 'SURVEY_QUE_GRP'
        String getRteInfo = "select rte_id, rte_get_xsl, rte_exe_xsl, rte_dl_xsl from ReportTemplate where rte_type = ?";
        stmt = con.prepareStatement(getRteInfo);
        stmt.setString(1, SurveyReport.RTE_TYPE_SURVEY_QUE_GRP);
        rs = stmt.executeQuery();
        while (rs.next()) {
            headData.rte_id = rs.getString("rte_id");
            headData.rte_get_xsl = rs.getString("rte_get_xsl");
            headData.rte_exe_xsl = rs.getString("rte_exe_xsl");
            headData.rte_dl_xsl = rs.getString("rte_dl_xsl");
        }
        stmt.close();
        return headData;
    }
    
    public int getXslFBAns(SurveyQueReport.SurveyQueRptParam param, String userSpecifiedItem, int que_id, SurveyQueReportXls sqrx) 
    	throws SQLException, IOException, RowsExceededException, WriteException {
    	//the count of answer
    	int ansCount = 0;
    	
    	StringBuffer sql = new StringBuffer();
        sql.append("select atm_order, atm_response_bil, count(*) response_count from ProgressAttempt, Progress, aeApplication, aeAttendance ")
        .append(" where atm_pgr_res_id in ( ")
        .append(" select mod_res_id from Module ")
        .append(" where mod_mod_id_root = ").append(param.mod_res_id)
        .append(" and mod_res_id in ( ")
        .append(" select rcn_res_id_content from ResourceContent ")
        .append(" where rcn_res_id in ( ")
        .append(" select cos_res_id from Course, aeItem ")
        .append(" where cos_itm_id = itm_id ")
        .append(" and itm_id in (").append("select item_id from ").append(userSpecifiedItem).append(")")
        .append(")))")
        .append(" and atm_order in ( ")
        .append(" select rcn_order ")
        .append(" from Resources, ResourceContent, Question, Interaction ")
        .append(" where res_id = ").append(param.mod_res_id)
        .append(" and res_id = rcn_res_id ")
        .append(" and rcn_res_id_content = que_res_id ")
        .append(" and que_res_id = int_res_id ) ")
        .append(" and atm_pgr_usr_id = pgr_usr_id ")
        .append(" and atm_pgr_res_id = pgr_res_id ")
        .append(" and atm_pgr_attempt_nbr = pgr_attempt_nbr ")
        .append(" and atm_tkh_id = pgr_tkh_id ")
        .append(" and pgr_tkh_id = app_tkh_id")
        .append(" and app_id = att_app_id");
        if (param.period_from != null) {
            sql.append(" and att_create_timestamp >= ?");
        }
        if (param.period_to != null) {
            sql.append(" and att_create_timestamp <= ?");
        }
        sql.append(" and atm_order = ?");
        sql.append(" group by atm_order, atm_response_bil ").append(" order by atm_order");
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
			stmt = con.prepareStatement(sql.toString());

			int idx = 1;
			if (param.period_from != null) {
				stmt.setTimestamp(idx++, param.period_from);
			}
			if (param.period_to != null) {
				stmt.setTimestamp(idx++, param.period_to);
			}
			if (param.que_id > 0) {
				stmt.setLong(idx++, param.que_id);
			}

			rs = stmt.executeQuery();

			int rowNum = 1;
			while (rs.next()) {
				if (rs.getString("atm_response_bil") != null) {
					int response_count = rs.getInt("response_count");
					String response_bil = rs.getString("atm_response_bil");
					for (int i = 0; i < response_count; i++) {
						if (rowNum == 1) {
							sqrx.setFBAns(response_bil, true);
						} else {
							sqrx.setFBAns(response_bil, false);
						}
					}
					ansCount += response_count;
					rowNum++;
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
        
        return ansCount;
    }

    
}
