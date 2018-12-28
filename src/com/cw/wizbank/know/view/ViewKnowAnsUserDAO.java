package com.cw.wizbank.know.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.JsonMod.know.bean.KnowAnswerBean;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSQL;

/**
 * @author DeanChen
 * 
 */
public class ViewKnowAnsUserDAO {

    // for pagination
    public int page_start_num = 0;

    public int page_end_num = 10;
    
    public ViewKnowAnsUserDAO(int pageStartNum, int pageSize) {
        if (pageSize == 0) {
            pageSize = 10;
        }
        page_start_num = pageStartNum + 1;
        page_end_num = pageStartNum + pageSize;
    }

    private static final String GET_ANS_LST_BY_QUE_ID = " SELECT ans_que_id,ans_id,ans_right_ind,ans_vote_total,ans_vote_for,ans_vote_down, "
            + " ans_content,ans_create_ent_id, ans_refer_content, ans_create_timestamp, "
            + " usr_display_bil, usr_nickname, usr_ent_id, urx_extra_43 "
            + " FROM knowAnswer ,RegUser, ReguserExtension "
            + " WHERE ans_create_ent_id = usr_ent_id and usr_ent_id = urx_usr_ent_id" 
            + " AND ans_status = ? "
            + " AND ans_right_ind = ? "
            + " AND ans_que_id = ? " + " ORDER BY ans_create_timestamp ASC ";

    public int getAnsListByQueId(Connection con, Vector ansVec, int queId,
            boolean isRightAns, String uploadUsrDirAbs, String defaultUserPhotoPath) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int recTotal = 0;
        try {
            stmt = con.prepareStatement(GET_ANS_LST_BY_QUE_ID);
            int index = 1;
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setBoolean(index++, isRightAns);
            stmt.setInt(index++, queId);
            rs = stmt.executeQuery();
            String name = null;
            while (rs.next()) {
                KnowAnswerBean ansBean = new KnowAnswerBean();
				ansBean.setAns_que_id(rs.getInt("ans_que_id"));
				ansBean.setAns_id(rs.getInt("ans_id"));
				ansBean.setAns_right_ind(rs.getBoolean("ans_right_ind"));
				ansBean.setAns_vote_total(rs.getInt("ans_vote_total"));
				ansBean.setAns_vote_for(rs.getInt("ans_vote_for"));
				ansBean.setAns_vote_down(rs.getInt("ans_vote_down"));
				ansBean.setAns_content(cwSQL.getClobValue(rs, "ans_content"));
				ansBean.setAns_refer_content(rs.getString("ans_refer_content"));
				int creatorID = rs.getInt("ans_create_ent_id");
				ansBean.setAns_create_ent_id(creatorID);
				ansBean.setAns_create_timestamp(rs.getTimestamp("ans_create_timestamp"));
				
				name = rs.getString("usr_nickname");
				if (name == null || name.length() == 0) {
					name = rs.getString("usr_display_bil");
				}
				ansBean.setUsr_nickname(name);
				ansBean.setUsr_ent_id(rs.getLong("usr_ent_id"));

				String usrPhotoPath = null;
				String usrPhotoName = rs.getString("urx_extra_43");
				if (usrPhotoName != null && !"".equals(usrPhotoName)) {
					usrPhotoPath = uploadUsrDirAbs + "/" + rs.getLong("usr_ent_id") + "/" + usrPhotoName;
				} else {
					usrPhotoPath = uploadUsrDirAbs + "/" + defaultUserPhotoPath;
				}
				ansBean.setUsr_photo(usrPhotoPath);
				ansVec.addElement(ansBean);

				recTotal++;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return recTotal;
    }

}
