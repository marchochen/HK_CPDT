package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DbItemTargetLrnDetail {
    public long itd_itm_id ;
    public long itd_usr_ent_id ;
    public int itd_group_ind ;  //用户组关键维度
    public int itd_grade_ind ; //职级关键维度
    public int itd_position_ind ; //岗位关键
    public int  itd_compulsory_ind ;   //是否为必修
    public int itd_fromprofession = 0;//规则来源于职级地图    0否1是2是并且课程中设置职级关键维度
    public int itd_fromposition = 0;//规则来源于岗位地图   0否1是2是并且课程中设置岗位关键维度
    
    
    public static void delByItem(Connection con, long itd_itm_id) throws SQLException {
        String sql = "delete from itemTargetLrnDetail where itd_itm_id =? " ;
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, itd_itm_id);
        stmt.executeUpdate();
        if(stmt != null) {
            stmt.close();
        }
    }
    
    public static void delByUsr(Connection con, long itd_usr_ent_id) throws SQLException {
        String sql = "delete from itemTargetLrnDetail where itd_usr_ent_id =? " ;
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, itd_usr_ent_id);
        stmt.executeUpdate();
        if(stmt != null) {
            stmt.close();
        }
    }
    
    
    public static void delAll(Connection con) throws SQLException {
        String sql = "truncate table itemTargetLrnDetail  " ;
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.executeUpdate();
        if(stmt != null) {
            stmt.close();
        }
    }
    
    
    public void AddBatch(Connection con, Map<String,DbItemTargetLrnDetail> list) throws SQLException {
        if(list.size() > 0){
            PreparedStatement stmt = con.prepareStatement("INSERT INTO itemTargetLrnDetail (itd_itm_id, itd_usr_ent_id, itd_group_ind, itd_grade_ind, itd_position_ind, itd_compulsory_ind, itd_fromprofession, itd_fromposition) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            
            for (Entry<String, DbItemTargetLrnDetail> model : list.entrySet()) {
            	int index = 1;
                stmt.setLong(index++, model.getValue().itd_itm_id);
                stmt.setLong(index++, model.getValue().itd_usr_ent_id);
                stmt.setLong(index++, model.getValue().itd_group_ind);
                stmt.setLong(index++, model.getValue().itd_grade_ind);
                stmt.setLong(index++, model.getValue().itd_position_ind);
                stmt.setLong(index++, model.getValue().itd_compulsory_ind);
                stmt.setLong(index++, model.getValue().itd_fromprofession);
                stmt.setLong(index++, model.getValue().itd_fromposition);
                stmt.addBatch();
			}
            
            stmt.executeBatch();
            if(stmt != null) {
                stmt.close();
            }
        }

       
    }

}
