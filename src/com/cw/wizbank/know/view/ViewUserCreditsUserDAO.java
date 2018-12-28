package com.cw.wizbank.know.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.JsonMod.know.bean.CreditsChartsBean;
import com.cw.wizbank.qdb.dbRegUser;

/**
 * @author DeanChen .
 */
public class ViewUserCreditsUserDAO {

    private static final int KNOW_CREDITS_CHARTS_NUM = 10;

    private static final String GET_KNOW_CREDITS_CHARTS = " "
            + " SELECT usr_ent_id, usr_nickname, usr_display_bil, uct_zd_total FROM RegUser, userCredits "
            + " WHERE usr_ent_id = uct_ent_id AND usr_status = ? and usr_ste_ent_id = ?"
            + " ORDER BY uct_zd_total DESC, usr_nickname asc ";

    /**
     * to get the charts of know credits. <BR>
     * 获取知道的积分排行榜
     * 
     * @param con
     * @param steEntId
     * @return
     * @throws SQLException
     */
    public static Vector getKnowCreditsCharts(Connection con, long steEntId) throws SQLException {
        Vector creditsChartsVec = new Vector();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(GET_KNOW_CREDITS_CHARTS);
            int index = 1;
            stmt.setString(index++, dbRegUser.USR_STATUS_OK);
            stmt.setLong(index++, steEntId);
            rs = stmt.executeQuery();

            int knowCount = 0;
            String name = null;
            while (rs.next() && knowCount < KNOW_CREDITS_CHARTS_NUM) {
                knowCount++;
                CreditsChartsBean chartsBean = new CreditsChartsBean();
                chartsBean.setOrder(knowCount);
                chartsBean.setUsr_ent_id(rs.getLong("usr_ent_id"));
                
                name = rs.getString("usr_nickname");
                if (name == null || name.length() == 0) {
                	name = rs.getString("usr_display_bil");
                }
                chartsBean.setUsr_nickname(name);
                chartsBean.setZd_total(rs.getInt("uct_zd_total"));
                creditsChartsVec.addElement(chartsBean);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return creditsChartsVec;
    }
    
    public static float getTotalByEntId(Connection con, long uctEntId) throws SQLException {
    	float total = 0;
    	
    	String sql = "select sum(ucd_total) as total from userCreditsDetail" +
    			" inner join creditsType on (cty_id = ucd_cty_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))" +
    			" where ucd_ent_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, uctEntId);

			rs = stmt.executeQuery();
			if (rs.next()) {
				total = rs.getFloat("total");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return total;
	}

}
