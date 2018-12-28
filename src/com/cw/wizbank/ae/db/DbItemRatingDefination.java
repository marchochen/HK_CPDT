package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.ae.db.view.ViewItemRatingDefination;
import com.cw.wizbank.accesscontrol.acSite;

public class DbItemRatingDefination{

    public long ird_id;
    public String ird_range_xml;
    public String ird_q_xml;
    public Timestamp ird_update_timestamp;
    public String ird_update_usr_id;
    public boolean ird_default_ind;

    public void save(Connection con, String usr_id, long root_ent_id) throws SQLException{
            Timestamp curTime = cwSQL.getTime(con);
            ird_update_timestamp = curTime;
            ird_update_usr_id = usr_id;
            ird_id = ViewItemRatingDefination.getIdBySite(con, root_ent_id);
            ird_default_ind = false;
            if (ird_id == 0){
                ins(con);
                ird_id = getIdByUpdTimestamp(con);
                acSite.updRateDefId(con, root_ent_id, ird_id);
            }else{
                upd(con);
            }
    }

    public void ins(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_itm_rate_def, PreparedStatement.RETURN_GENERATED_KEYS);
		int para = 1;
		stmt.setString(para++, ird_range_xml);
		// stmt.setString(para++, ird_q_xml); to insert the reference for Oracle
		stmt.setBoolean(para++, ird_default_ind);
		stmt.setTimestamp(para++, ird_update_timestamp);
		stmt.setString(para++, ird_update_usr_id);
		if (stmt.executeUpdate() != 1) {
			throw new SQLException("Failed to insert ItemRatingDefination.");
		}
		// get the update condition for imt_id (oracle)
		long ird_id = cwSQL.getAutoId(con, stmt, "aeItemRatingDefination", "ird_id");
		if (ird_id > 0) {
			// update the reference for Oracle
			String columnName[] = { "ird_q_xml" };
			String columnValue[] = { ird_q_xml };
			String condition = "ird_id = " + ird_id;
			cwSQL.updateClobFields(con, "aeItemRatingDefination", columnName, columnValue, condition);
		}
		// end update
		stmt.close();
	}

        public long getIdByUpdTimestamp(Connection con) throws SQLException{
            long Id;
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_by_upd_ts);
            stmt.setTimestamp(1, ird_update_timestamp);
            stmt.setString(2, ird_update_usr_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                Id = rs.getLong(1);
            }else{
                throw new SQLException("no revalent item rateing defination ID ");
            }
            stmt.close();
            return Id;
        }
        public void upd(Connection con) throws SQLException{
                PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_itm_rate_def);

                int para = 1;
                stmt.setString(para++, ird_range_xml);
                // stmt.setString(para++, ird_q_xml);
                stmt.setTimestamp(para++, ird_update_timestamp);
                stmt.setString(para++, ird_update_usr_id);
                stmt.setLong(para++, ird_id);

                if (stmt.executeUpdate()!= 1) {
                    throw new SQLException("Failed to update ItemRatingDefination.");
                }

                //update the reference for Oracle
            //    Hashtable fields = new Hashtable();
            //    fields.put("ird_q_xml", ird_q_xml);
                        String columnName[]={"ird_q_xml"};
                        String columnValue[]={ird_q_xml};

                String condition = "ird_id = " + ird_id;
                cwSQL.updateClobFields(con, "aeItemRatingDefination", columnName,columnValue, condition);
           stmt.close();
        }

}
