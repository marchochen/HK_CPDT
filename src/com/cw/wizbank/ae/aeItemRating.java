package com.cw.wizbank.ae;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.qdbException;

import com.cw.wizbank.ae.db.view.ViewItemRatingDefination;
import com.cw.wizbank.ae.db.DbItemRating;
import com.cw.wizbank.ae.db.DbItemRatingDefination;

public class aeItemRating{

    public static String getRatingQ(Connection con, loginProfile prof, long mod_id)
        throws cwException
    {
        try{
            Timestamp curTime = cwSQL.getTime(con);
            StringBuffer result = new StringBuffer();
            result.append("<item id=\"").append(dbCourse.getCosItemId(con, dbModule.getCosId(con, mod_id))).append("\" />").append(cwUtils.NEWL);
            result.append(ViewItemRatingDefination.getCol(con, prof.root_ent_id, ViewItemRatingDefination.Q_COL)).append(cwUtils.NEWL);

            return result.toString(); 
        } catch(qdbException e) {
            throw new cwException(e.toString());
        } catch(SQLException e) {
            throw new cwException(e.toString());
        }
    }


    public static void saveRating(Connection con, loginProfile prof, long itm_id, float rate) throws SQLException {
        saveRating(con, prof, itm_id, rate, prof.usr_ent_id);
        return;
    }
    
    public static void saveRating(Connection con, loginProfile prof, long itm_id, float rate, long ent_id) throws SQLException{
        DbItemRating irt = new DbItemRating();
        irt.irt_itm_id = itm_id;
        if( ent_id != 0 )
            irt.irt_ent_id = ent_id;
        else
            irt.irt_ent_id = prof.usr_ent_id;
        irt.irt_type = DbItemRating.RATING_TYPE_USR;
        irt.irt_rate = rate;
        irt.save(con, prof);
    }
    
    public static StringBuffer getRateLstAsXML(Connection con, long itm_id) throws SQLException{
        Vector vtRate = DbItemRating.getByItmId(con, itm_id, DbItemRating.RATING_TYPE_USR);        
        return null;        
    }

    public static StringBuffer getRateDefinationAsXML(Connection con, long root_ent_id) throws SQLException{
        StringBuffer result = new StringBuffer();
        result.append("<defination>").append(cwUtils.NEWL);
        result.append(ViewItemRatingDefination.getCol(con, root_ent_id, ViewItemRatingDefination.RANGE_COL));
        result.append(ViewItemRatingDefination.getCol(con, root_ent_id, ViewItemRatingDefination.Q_COL));
        result.append("</defination>").append(cwUtils.NEWL);
        return result;        
    }
    
    public static void saveRatingDefination(Connection con, long root_ent_id, String usr_id, String range_xml, String q_xml) throws SQLException{
        DbItemRatingDefination rateDef = new DbItemRatingDefination();
        rateDef.ird_range_xml = range_xml;
        rateDef.ird_q_xml = q_xml;
        rateDef.save(con, usr_id, root_ent_id);
    }
}