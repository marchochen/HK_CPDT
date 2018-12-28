/*
 * Created on 2005-9-15
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

import jxl.write.WriteException;

/**
 * @author dixson
 */
public class LearnerRptExporter extends ReportExporter{
	
	public boolean is_realTime_view_rpt;
    public LearnerRptExporter(Connection incon, ExportController inController) {
        super(incon, inController);
    }

    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
        StringBuffer str_buf = new StringBuffer();
        Vector itm_id_vec = null;
        
        //get all itm include classroom
        if (specData.itm_id_lst != null ) {
        	itm_id_vec = new Vector();
        	if(specData.all_cos_ind && wizbini.cfgTcEnabled) {
                for (int i = 0; i < specData.itm_id_lst.length; i++) {
                    itm_id_vec.addElement(new Long(specData.itm_id_lst[i]));
                }        		
        	} else {
        		aeItem itm = null;
        		Vector childItm;
        		for (int i = 0; i < specData.itm_id_lst.length; i++) {
        			itm = new aeItem();
        			itm.itm_id = specData.itm_id_lst[i];
        			if (itm.getCreateRunInd(con)) {
        				childItm = aeItemRelation.getChildItemId(con, specData.itm_id_lst[i]);
        				for (int k = 0; k < childItm.size(); k++) {
        					itm_id_vec.addElement((Long)childItm.elementAt(k));
        				}
        			}
        			itm_id_vec.addElement(new Long(specData.itm_id_lst[i]));
        		}
        	}
        } else if (specData.tnd_id_lst != null) {
            itm_id_vec = new Vector();
            aeTreeNode.getItemsFromNode(con, specData.tnd_id_lst, itm_id_vec);
        }

        Vector ats_lst = null;
        if (specData.ats_id_lst != null) {
            ats_lst = new Vector();

            for (int i = 0; i < specData.ats_id_lst.length; i++) {
                ats_lst.addElement(specData.ats_id_lst[i]);
            }
        }

        String str = null;
        String sort_sql = null;
        if (specData.sortOrder != null) {
            for (int i = 0; i < DEFAULT_SORT_COL_ORDER.length; i++) {
                if (DEFAULT_SORT_COL_ORDER[i].equals(specData.sortCol)) {
                    str = specData.sortCol + " " + specData.sortOrder;
                }
                else {
                    str_buf.append(DEFAULT_SORT_COL_ORDER[i]).append(" ").append(specData.sortOrder).append(", ");
                }
            }
            StringBuffer col_buf = new StringBuffer();
            col_buf.append(" ORDER BY ");

            if (str != null) {
                col_buf.append(str).append(", ");
            }
            else {
                specData.sortCol = DEFAULT_SORT_COL_ORDER[0];
            }
            col_buf.append(str_buf.toString());
            col_buf.append(TEMP_COL).append(" ").append(specData.sortOrder);
            sort_sql = col_buf.toString();
        }

        if(!wizbini.cfgTcEnabled && (specData.ent_id_lst == null || specData.ent_id_lst.length == 0)
            && dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
                specData.ent_id_lst = getApprGroups(prof);
        }

        String itmIdTableName = null;
        String itmIdColName = null;
        String entIdTableName = null;
        String entIdColName = null;
        Vector usr_ent_ids = null;

        //insr itm_id to temp table
        if (itm_id_vec != null) {
            itmIdColName = "tmp_app_itm_id";
            itmIdTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
        }
        Vector ent_id_vec = new Vector();

        if (specData.ent_id_lst != null && specData.ent_id_lst.length > 0) {
        	Vector empty_vec = new Vector();
            entIdColName = "usr_ent_id";
            for (int i = 0;i < specData.ent_id_lst.length;i++) {
                empty_vec.addElement(new Long(specData.ent_id_lst[i]));
            }
            if(specData.all_user_ind && wizbini.cfgTcEnabled) {
            	usr_ent_ids = empty_vec;
            } else {
            	usr_ent_ids = getUserEntId(con, ent_id_vec, empty_vec);
            }
            entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);
        }
        
        if (itmIdTableName != null) {
            cwSQL.insertSimpleTempTable(con, itmIdTableName, itm_id_vec, cwSQL.COL_TYPE_LONG);
        }
        if (entIdTableName != null) {
        	cwSQL.insertSimpleTempTable(con, entIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
        }
        if (entIdTableName == null && specData.lrn_scope_sql != null) {
            entIdColName = "usr_ent_id";
            entIdTableName = cwSQL.createSimpleTemptable(con,entIdColName, cwSQL.COL_TYPE_LONG, 0, specData.lrn_scope_sql);
        }

        //sql of count and detail data.
        PreparedStatement stmt = getRptStmt(con, ats_lst, sort_sql, itmIdTableName, itmIdColName, entIdTableName, entIdColName, specData.att_create_start_datetime, specData.att_create_end_datetime, prof.usr_ent_id,prof.root_ent_id, wizbini.cfgTcEnabled, specData.isMyStaff,is_realTime_view_rpt);
        ResultSet result = stmt.executeQuery();
        Vector dataVec = new Vector();
        Vector usr_ent_vec = new Vector();
        while (result.next()) {
        	resultData resData = new resultData();
        	resData.usr_ent_id = result.getLong("usr_ent_id");
            resData.usr_ste_usr_id = result.getString("usr_ste_usr_id");
            resData.usr_display_bil = result.getString("usr_display_bil");
            resData.usr_email = result.getString("usr_email");
            resData.usr_tel_1 = result.getString("usr_tel_1");
            resData.app_tkh_id = result.getInt("app_tkh_id");
            resData.att_create_timestamp = result.getTimestamp("att_create_timestamp");
            resData.att_ats_id = LearnerRptExportHelper.getAtsTitle(result.getLong("att_ats_id"), specData.cur_lang);
            resData.att_timestamp = result.getTimestamp("att_timestamp");
            resData.cov_commence_datetime = result.getTimestamp("cov_commence_datetime");
            resData.cov_last_acc_datetime = result.getTimestamp("cov_last_acc_datetime");
            resData.cov_total_time = result.getFloat("cov_total_time");
            resData.cov_score = result.getDouble("cov_score");
            resData.itm_type = LangLabel.getValue(specData.cur_lang, result.getString("t_type"));
            resData.itm_dummy_type = aeItemDummyType.getItemLabelByDummyType(specData.cur_lang, aeItemDummyType.getDummyItemType(result.getString("t_type"), result.getBoolean("itm_blend_ind"), result.getBoolean("itm_exam_ind"), result.getBoolean("itm_ref_ind")));
            resData.itm_id = result.getLong("t_id");
            resData.itm_code = result.getString("t_code");
            resData.itm_title = result.getString("t_title");
            if(wizbini.cfgTcEnabled) {
            	resData.tcr_title = result.getString("tcr_title");
            }
            dataVec.add(resData);
            if (!usr_ent_vec.contains(new Long(resData.usr_ent_id))) {
                usr_ent_vec.addElement(new Long(resData.usr_ent_id));
            }
        }
        if (itmIdTableName != null) {
            cwSQL.dropTempTable(con, itmIdTableName);
        }
        if (entIdTableName != null) {
            cwSQL.dropTempTable(con, entIdTableName);
        }
        result.close();
        stmt.close();
        
        if (dataVec != null && dataVec.size() > 0) {
        	controller.setTotalRow(dataVec.size());
//            if (dataVec.size() > specData.process_unit) {
//                stmt.setFetchSize(specData.process_unit);
//            }
            LearnerRptExportHelper rptBuilder = new LearnerRptExportHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit,wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getUrl());
            rptBuilder.writeCondition(con, specData);
            rptBuilder.writeTableHead(specData.rpt_content, specData.cur_lang, um);
            Hashtable usr_group_hash = new Hashtable();
            Hashtable usr_grade_hash = new Hashtable();
            if (usr_ent_vec.size() != 0) {
                // Use temp table instead of using IN (1,2,3,.....,XXXX)
                String colName = "tmp_usr_ent_id";
                String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
                cwSQL.insertSimpleTempTable(con, tableName, usr_ent_vec, cwSQL.COL_TYPE_LONG);
                usr_group_hash = dbEntityRelation.getGroupEntIdRelation(con, tableName, colName);
                usr_grade_hash = dbEntityRelation.getGradeRelation(con, tableName, colName);
                cwSQL.dropTempTable(con, tableName);
            }
            EntityFullPath full_path = EntityFullPath.getInstance(con);
            for (int i = 0; i < dataVec.size() && !controller.isCancelled(); i++) {
            	resultData resData = (resultData)dataVec.get(i);
            	long usg_ent_id = (Long)usr_group_hash.get(resData.usr_ent_id);
            	resData.group_name = full_path.getFullPath(con, usg_ent_id);
            	resData.grade_name = (String)usr_grade_hash.get(resData.usr_ent_id);
                resData.att_times = dbModuleEvaluation.getTotalAttemptByTkhId(con, resData.app_tkh_id);
                resData.catNav = ViewLearnerReport.getCatNav(con, resData.itm_id);
                
                rptBuilder.writeData(resData, specData.rpt_content, specData.cur_lang);
                controller.next();
            }
            if (!controller.isCancelled()) {
                controller.setFile(rptBuilder.finalizeFile());
            }
        }
    }

    public static PreparedStatement getRptStmt(Connection con, Vector ats_lst, String sort_sql, String itmIdTableName, String itmIdColName, String entIdTableName, String entIdColName, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime,long usr_ent_id, long root_ent_id, boolean tc_entabled,boolean isMyStaff,boolean is_realTime_view_rpt) throws SQLException {
        StringBuffer sql = new StringBuffer();

        sql.append("select usr_ste_usr_id, usr_ent_id, usr_display_bil, usr_email, usr_tel_1, ")
            .append("lar_app_id app_id, lar_tkh_id app_tkh_id, lar_att_create_timestamp att_create_timestamp, ")
            .append("lar_att_ats_id att_ats_id, lar_att_timestamp att_timestamp, lar_cov_commence_datetime cov_commence_datetime, lar_cov_last_acc_datetime cov_last_acc_datetime, lar_cov_total_time cov_total_time, lar_cov_score cov_score, ")
            .append(cwSQL.replaceNull("parentItem.itm_type", "childItem.itm_type")).append(" as t_type,")
            .append(cwSQL.replaceNull("parentItem.itm_id", "childItem.itm_id")).append(" as t_id,")
            .append(cwSQL.replaceNull("parentItem.itm_code", "childItem.itm_code")).append(" as t_code,")
            .append(cwSQL.replaceNull("parentItem.itm_title", "childItem.itm_title")).append(" as t_title,")
            .append(cwSQL.replaceNull("parentItem.itm_blend_ind", "childItem.itm_blend_ind")).append(" as itm_blend_ind,")
            .append(cwSQL.replaceNull("parentItem.itm_exam_ind", "childItem.itm_exam_ind")).append(" as itm_exam_ind,")
            .append(cwSQL.replaceNull("parentItem.itm_ref_ind", "childItem.itm_ref_ind")).append(" as itm_ref_ind,")
//            .append(" parentItem.itm_blend_ind,parentItem.itm_exam_ind,parentItem.itm_ref_ind,")
            .append(" childItem.itm_id, childItem.itm_title, lar_cov_cos_id as cos_res_id ");
        if(tc_entabled) {
        	sql.append(" ,tcr_id, tcr_title ");
        }
        sql.append("from ");
        if(is_realTime_view_rpt){
        	sql.append(" view_lrn_activity_group ");
        }else {
        	sql.append(" lrnActivityReport");
		}
        sql.append(" inner join RegUser on (lar_usr_ent_id = usr_ent_id and usr_ste_ent_id = ?) ")
	        .append(" inner join aeItem childItem on (lar_c_itm_id = childItem.itm_id)")
	        .append(" left join aeItemRelation on (childItem.itm_id = ire_child_itm_id)")
	        .append(" left join aeItem parentItem on (ire_parent_itm_id = parentItem.itm_id)");
        if(tc_entabled) {
        	sql.append(" inner join tcTrainingCenter on (").append(cwSQL.replaceNull("parentItem.itm_tcr_id", "childItem.itm_tcr_id")).append("  = tcr_id )")
        	   .append(" where tcr_ste_ent_id =?  ")
        	   .append(" and tcr_status = ? ");
        }
        if (itmIdTableName != null && itmIdColName != null) {
            sql.append(" and lar_c_itm_id in (")
	            .append("select ").append(itmIdColName)
	            .append(" from ").append(itmIdTableName).append(")");
        }
        if (entIdTableName != null && entIdColName != null) {
            sql.append(" and usr_ent_id in ( ").append("select ").append(entIdColName).append(" from ").append(entIdTableName).append(")");
        }
        if (att_create_start_datetime != null) {
            sql.append(" and lar_att_create_timestamp >= ? ");            
        }
        if (att_create_end_datetime != null) {
            sql.append(" and lar_att_create_timestamp <= ? ");
        }
        if (ats_lst == null || ats_lst.size() == 0) {
            sql.append(" AND lar_att_ats_id <> 2 ");
        } else {
            sql.append(" AND lar_att_ats_id IN ");
            for (int i = 0; i < ats_lst.size(); i++) {
                if (i == 0) {
                    sql.append("(").append(ats_lst.elementAt(i));
                } else {
                    sql.append(",").append(ats_lst.elementAt(i));
                }
            }
            sql.append(") ");
        }
        if (sort_sql != null) {
            sql.append(sort_sql);
        }
        
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        stmt.setLong(index++, root_ent_id);
        if(tc_entabled) {
        	stmt.setLong(index++, root_ent_id);
        	stmt.setString(index++, DbTrainingCenter.STATUS_OK);
        }
        if (att_create_start_datetime != null) {
            stmt.setTimestamp(index++, att_create_start_datetime);
        }
        if (att_create_end_datetime != null) {
            stmt.setTimestamp(index++, att_create_end_datetime);
        }
        
        return stmt;
    }
    public static PreparedStatement getLrnSoln(Connection con, Vector ats_lst, String sort_sql, String itmIdTableName, String itmIdColName,  boolean isCount, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, long root_ent_id, String usr_ent_id_lst, long usr_ent_id) throws SQLException {
        StringBuffer sql = new StringBuffer();
        if (isCount) {
            sql.append("select count(*) as rpt_count ");
        } else {
            sql.append("select 'usr' as usr_ste_usr_id,  " );
            sql.append(usr_ent_id);
            sql.append(" as usr_ent_id, 'usr' as usr_display_bil, 'usr' as usr_email, 'usr' as usr_tel_1, ")
            .append(" app_id, app_tkh_id, att_create_timestamp, ")
            .append("att_ats_id, att_timestamp, cov_commence_datetime, cov_last_acc_datetime, cov_total_time, cov_score, ")
            .append(cwSQL.replaceNull("parentItem.itm_type", "childItem.itm_type")).append(" as t_type,")
            .append(cwSQL.replaceNull("parentItem.itm_id", "childItem.itm_id")).append(" as t_id,")
            .append(cwSQL.replaceNull("parentItem.itm_code", "childItem.itm_code")).append(" as t_code,")
            .append(cwSQL.replaceNull("parentItem.itm_title", "childItem.itm_title")).append(" as t_title,")
            .append(" parentItem.itm_blend_ind,parentItem.itm_exam_ind,parentitem.itm_ref_ind,")
            .append(" childItem.itm_id, childItem.itm_title, cov_cos_id as cos_res_id, ")
            .append(" tcr_id, tcr_title ");
        }
        sql.append(" from aeApplication")
           .append(" inner join aeAttendance on ( app_id = att_app_id ");
        if (att_create_start_datetime != null) {
            sql.append(" and att_create_timestamp >= ? ");            
        }
        if (att_create_end_datetime != null) {
            sql.append(" and att_create_timestamp <= ? ");
        }
        sql.append(") ")
           .append("inner join aeAttendanceStatus on (att_ats_id = ats_id ");
        if (ats_lst == null || ats_lst.size() == 0) {
            sql.append(" AND ats_type <> 'PROGRESS' ");
        } else {
            sql.append(" AND att_ats_id IN ");
            for (int i = 0; i < ats_lst.size(); i++) {
                if(ats_lst.elementAt(i) != null) {
                    if (i == 0) {
                        sql.append("(").append(ats_lst.elementAt(i));
                    } else {
                        sql.append(",").append(ats_lst.elementAt(i));
                    }
                }
            }
            sql.append(") ");
        }
        sql.append(") ")
           .append(" inner join aeItem childItem on (app_itm_id = childItem.itm_id)")
           .append(" inner join CourseEvaluation on (app_tkh_id = cov_tkh_id)")
           .append(" left join aeItemRelation on (childItem.itm_id = ire_child_itm_id)")
           .append(" left join aeItem parentItem on (ire_parent_itm_id = parentItem.itm_id )")
           .append(" left join tcTrainingCenter on (childItem.itm_tcr_id = tcr_id ) ");
    	sql.append(" where childItem.itm_status = ?  ")
    	  .append(" AND ").append(cwSQL.replaceNull("parentItem.itm_status", "childItem.itm_status")).append(" = ?");
        if (usr_ent_id_lst !=null) {
        	sql.append(" and app_ent_id in ").append(usr_ent_id_lst);
        }
        if (itmIdTableName != null && itmIdColName != null) {
            sql.append(" and app_itm_id in (")
            .append("select ").append(itmIdColName)
            .append(" from ").append(itmIdTableName).append(")");
        }
    
        if (sort_sql != null && !isCount) {
            sql.append(sort_sql);
        }
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        if (att_create_start_datetime != null) {
            stmt.setTimestamp(index++, att_create_start_datetime);
        }
        if (att_create_end_datetime != null) {
            stmt.setTimestamp(index++, att_create_end_datetime);
        }
        stmt.setString(index++, aeItem.ITM_STATUS_ON);
        stmt.setString(index++, aeItem.ITM_STATUS_ON);
        return stmt;
    }
}
