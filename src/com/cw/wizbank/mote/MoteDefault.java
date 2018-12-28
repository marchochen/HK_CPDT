package com.cw.wizbank.mote;

import java.sql.*;

import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;
public class MoteDefault implements Cloneable {
    public long imd_id;
    public Timestamp due_date;
    public String plan_xml;
    public int level1_ind;
    public int level2_ind;
    public int level3_ind;
    public int level4_ind;
    public String rating_target;
    public String cost_target;
    public String time_target;
    public Timestamp create_timestamp;
    public String create_usr_id;
    public Timestamp upd_timestamp;
    public String upd_usr_id;
    public String participant_target;

    public StringBuffer defaultValueAsXML(){
        StringBuffer result = new StringBuffer(512);
        result.append("<mote_participant_target>").append(cwUtils.NEWL);
        result.append("<participant_target value=\"").append(cwUtils.escNull(participant_target)).append("\" />").append(cwUtils.NEWL);
        result.append("</mote_participant_target>").append(cwUtils.NEWL);
        result.append("<mote_cost_target>").append(cwUtils.NEWL);
        result.append("<cost_target value=\"").append(cwUtils.escNull(cost_target)).append("\" />").append(cwUtils.NEWL);
        result.append("</mote_cost_target>").append(cwUtils.NEWL);
        result.append("<mote_time_target>").append(cwUtils.NEWL);
        result.append("<time_target value=\"").append(cwUtils.escNull(time_target)).append("\" />").append(cwUtils.NEWL);
        result.append("</mote_time_target>").append(cwUtils.NEWL);
        result.append("<mote_due_date>").append(cwUtils.NEWL);
        result.append("<date value=\"").append(cwUtils.escNull(due_date)).append("\" />").append(cwUtils.NEWL);
        result.append("</mote_due_date>").append(cwUtils.NEWL);
        result.append("<mote_target_rating>").append(cwUtils.NEWL);
        result.append("<rating_target value=\"").append(cwUtils.escNull(rating_target)).append("\" />").append(cwUtils.NEWL);
        result.append("</mote_target_rating>").append(cwUtils.NEWL);
        result.append("<mote_level>").append(cwUtils.NEWL);
        result.append("<mote_level_list>").append(cwUtils.NEWL);
        result.append("<mote_level id=\"1\" selected=\"").append((level1_ind == 1)?"true":"false").append("\"/>").append(cwUtils.NEWL);
        result.append("<mote_level id=\"2\" selected=\"").append((level2_ind == 1)?"true":"false").append("\"/>").append(cwUtils.NEWL);
        result.append("<mote_level id=\"3\" selected=\"").append((level3_ind == 1)?"true":"false").append("\"/>").append(cwUtils.NEWL);
        result.append("<mote_level id=\"4\" selected=\"").append((level4_ind == 1)?"true":"false").append("\"/>").append(cwUtils.NEWL);
        result.append("</mote_level_list>").append(cwUtils.NEWL);
        result.append("</mote_level>").append(cwUtils.NEWL);
        result.append(cwUtils.escNull(plan_xml)).append(cwUtils.NEWL);
        return result;
    }

    public static StringBuffer getInsSQL(){
        StringBuffer SQL = new StringBuffer();
        SQL.append("INSERT INTO aeItemMoteDefault (");
        SQL.append(" imd_due_date , ");
        //SQL.append(" imd_plan_xml , ");
        SQL.append(" imd_level1_ind , ");
        SQL.append(" imd_level2_ind , ");
        SQL.append(" imd_level3_ind , ");
        SQL.append(" imd_level4_ind , ");
        SQL.append(" imd_participant_target , ");
        SQL.append(" imd_rating_target , ");
        SQL.append(" imd_cost_target , ");
        SQL.append(" imd_time_target , ");
        SQL.append(" imd_create_timestamp , ");
        SQL.append(" imd_create_usr_id , ");
        SQL.append(" imd_upd_timestamp , ");
        SQL.append(" imd_upd_usr_id ");
        SQL.append(" ) VALUES ");
        SQL.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
        CommonLog.debug("moteDefault L74:" + SQL.length());

        return SQL;
    }

    public void ins(Connection con) throws SQLException{
        ins(con, getInsSQL().toString());
    }

    public void ins(Connection con, String SQL) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);

		int col = 1;
		stmt.setTimestamp(col++, due_date);
		// stmt.setString(col++, plan_xml);
		stmt.setInt(col++, level1_ind);
		stmt.setInt(col++, level2_ind);
		stmt.setInt(col++, level3_ind);
		stmt.setInt(col++, level4_ind);
		stmt.setString(col++, participant_target);
		stmt.setString(col++, rating_target);
		stmt.setString(col++, cost_target);
		stmt.setString(col++, time_target);
		stmt.setTimestamp(col++, create_timestamp);
		stmt.setString(col++, create_usr_id);
		stmt.setTimestamp(col++, upd_timestamp);
		stmt.setString(col++, upd_usr_id);
		stmt.executeUpdate();

		// start update
		// get the update condition for imt_id (oracle)

		imd_id = cwSQL.getAutoId(con, stmt, "aeItemMoteDefault", "imd_id");
		if (imd_id > 0) {
			// update the reference for Oracle
			String columnName[] = { "imd_plan_xml" };
			String columnValue[] = { plan_xml };

			String condition = "imd_id = " + imd_id;
			cwSQL.updateClobFields(con, "aeItemMoteDefault", columnName, columnValue, condition);
		}
		// end
		stmt.close();
    }

    public static String getUpdSQL(){

        StringBuffer SQL = new StringBuffer(300);

        SQL.append("UPDATE aeItemMoteDefault SET ");
        // SQL.append(" imd_plan_xml = ? , ");
        SQL.append(" imd_due_date = ? , ");
        SQL.append(" imd_level1_ind = ?, ");
        SQL.append(" imd_level2_ind = ?, ");
        SQL.append(" imd_level3_ind = ? , ");
        SQL.append(" imd_level4_ind = ? , ");
        SQL.append(" imd_participant_target = ?, ");
        SQL.append(" imd_rating_target = ?, ");
        SQL.append(" imd_cost_target = ? , ");
        SQL.append(" imd_time_target = ? , ");
        SQL.append(" imd_upd_timestamp = ? , ");
        SQL.append(" imd_upd_usr_id = ? ");
        SQL.append(" WHERE imd_id = ? ");
//        System.out.println("moteDefault L121:" + SQL.length());

        return SQL.toString();
    }

    public void upd(Connection con) throws SQLException{
        upd(con, getUpdSQL());
    }

    public void upd(Connection con, String SQL) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SQL);
            int col=1;
          //stmt.setString(col++, plan_xml);
            stmt.setTimestamp(col++, due_date);
            stmt.setInt(col++, level1_ind);
            stmt.setInt(col++, level2_ind);
            stmt.setInt(col++, level3_ind);
            stmt.setInt(col++, level4_ind);
            stmt.setString(col++, participant_target);
            stmt.setString(col++, rating_target);
            stmt.setString(col++, cost_target);
            stmt.setString(col++, time_target);
            stmt.setTimestamp(col++, upd_timestamp);
            stmt.setString(col++, upd_usr_id);

            stmt.setLong(col++, imd_id);
            stmt.executeUpdate();

      //update the reference for Oracle
      //  Hashtable fields = new Hashtable();
      //  fields.put("imd_plan_xml", plan_xml);
        String columnName[]={"imd_plan_xml"};
        String columnValue[]={plan_xml};

        String condition = "imd_id = " + imd_id;
        cwSQL.updateClobFields(con, "aeItemMoteDefault",columnName,columnValue, condition);



            stmt.close();
    }

    public void get(Connection con) throws SQLException{
        StringBuffer SQL = new StringBuffer(300);

        SQL.append("SELECT imd_due_date , ");
        SQL.append(" imd_plan_xml , ");
        SQL.append(" imd_level1_ind , ");
        SQL.append(" imd_level2_ind , ");
        SQL.append(" imd_level3_ind , ");
        SQL.append(" imd_level4_ind  , ");
        SQL.append(" imd_participant_target , ");
        SQL.append(" imd_rating_target , ");
        SQL.append(" imd_cost_target  , ");
        SQL.append(" imd_time_target  , ");
        SQL.append(" imd_create_timestamp  , ");
        SQL.append(" imd_create_usr_id , ");
        SQL.append(" imd_upd_timestamp  , ");
        SQL.append(" imd_upd_usr_id  ");
        SQL.append(" FROM aeItemMoteDefault ");
        SQL.append(" WHERE imd_id = ?  ");

        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(1, imd_id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()){
            int col = 1;
            due_date = rs.getTimestamp(col++);
            //plan_xml = rs.getString(col++);
            //get the reference from oracle
            plan_xml = cwSQL.getClobValue(rs,"imd_plan_xml");
            col++;
            level1_ind = rs.getInt(col++);
            level2_ind = rs.getInt(col++);
            level3_ind = rs.getInt(col++);
            level4_ind = rs.getInt(col++);
            participant_target = rs.getString(col++);
            rating_target = rs.getString(col++);
            cost_target = rs.getString(col++);
            time_target = rs.getString(col++);
            create_timestamp = rs.getTimestamp(col++);
            create_usr_id = rs.getString(col++);
            upd_timestamp = rs.getTimestamp(col++);
            upd_usr_id = rs.getString(col++);
        }
        stmt.close();
    }



    public long getMaxId(Connection con, String SQL, Timestamp upd_timestamp) throws SQLException{
        long max_id;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setTimestamp(1, upd_timestamp);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()){
            max_id = rs.getLong("MAX_ID");
        }else{
            throw new SQLException("no max id according to the upd_timestamp");
        }

        stmt.close();
        return max_id;
    }

    public void del(Connection con)throws SQLException{
        del(con, DEL_SQL);
    }

    public static String DEL_SQL = " Delete FROM aeItemMoteDefault WHERE imd_id = ? ";

    public void del(Connection con, String SQL) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, imd_id);
        stmt.executeUpdate();
        stmt.close();
    }
}





