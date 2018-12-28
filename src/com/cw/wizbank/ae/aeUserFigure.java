package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.*;

import com.cw.wizbank.ae.db.view.ViewFigure;

public class aeUserFigure{
    
    public final String EMPTY       =   "EMPTY";
    
    public String getUserFigureAsXML(Connection con, long att_id)
        throws SQLException {
            
            StringBuffer xml = new StringBuffer();
            xml.append("<figure_list>").append(cwUtils.NEWL);
            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getUserFigureDetail(con, att_id);            
            for (int i=0; i<figureDtls.length; i++){
                xml.append("<figure ")
                   .append(" fig_id=\"").append(figureDtls[i].fig_id).append("\" ")
                   .append(" fig_value=\"").append(figureDtls[i].fig_value).append("\" ")                   
                   .append(" fgt_id=\"").append(figureDtls[i].fgt_id).append("\" ")
                   .append(" fgt_type=\"").append(cwUtils.esc4XML(figureDtls[i].fgt_type)).append("\" ")
                   .append(" fgt_subtype=\"").append(cwUtils.esc4XML(cwUtils.escNull(figureDtls[i].fgt_subtype))).append("\" ")
                   .append(" />").append(cwUtils.NEWL);
            }
            xml.append("</figure_list>").append(cwUtils.NEWL);
            return xml.toString();
        }
    
    
    
    
    
    
    public String getDetailAsXML(Connection con, long itm_id, long app_id)
        throws SQLException{
            
            Vector itmIdVec = new Vector();
            itmIdVec.addElement(new Long(itm_id));
            StringBuffer xml = new StringBuffer();
            
            DbFigure[] figures = ViewFigure.getUserFigureValue(con, app_id);
            Hashtable creditTable = new Hashtable();
            Long fgt_id;
            Vector vec;
            for (int i=0; i<figures.length; i++){
                vec = new Vector();
                vec.addElement(new Long(figures[i].fig_id));
                vec.addElement(new Float(figures[i].fig_value));
                fgt_id = new Long(figures[i].fig_fgt_id);
                creditTable.put(fgt_id, vec);
            }
            
            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getFigureDetail(con, itmIdVec, null, null, null, null);
            xml.append("<figure_value_list>").append(cwUtils.NEWL);
            Hashtable h_ats_multiplier;
            Long ats_id;
            Enumeration enumeration;
            for (int i=0; i<figureDtls.length; i++){
                fgt_id = new Long(figureDtls[i].fgt_id);
                xml.append("<figure_value ");
                if( creditTable.containsKey(fgt_id) ) {
                    vec = (Vector)creditTable.get(fgt_id);
                    xml.append(" fig_id=\"").append(vec.elementAt(0)).append("\" ");
                    xml.append(" fig_value=\"").append(vec.elementAt(1)).append("\" ");
                }
   
                xml.append(" itm_fig_value=\"").append(figureDtls[i].fig_value).append("\" ")
                   .append(" fgt_id=\"").append(figureDtls[i].fgt_id).append("\" ")
                   .append(" fgt_type=\"").append(cwUtils.esc4XML(figureDtls[i].fgt_type)).append("\" ")
                   .append(" fgt_subtype=\"").append(cwUtils.esc4XML(cwUtils.escNull(figureDtls[i].fgt_subtype))).append("\" ")
                   .append(" >").append(cwUtils.NEWL)
                   .append( figureDtls[i].fgt_xml);
                
                xml.append("<default_value>");
                h_ats_multiplier = DbAttendanceFigureType.getByFigureType(con, figureDtls[i].fgt_id);
                enumeration = h_ats_multiplier.keys();
                while(enumeration.hasMoreElements()){
                    ats_id = (Long)enumeration.nextElement();
                    xml.append("<value ats_id=\"").append(ats_id).append("\" ")
                       .append(" multiplier=\"").append(h_ats_multiplier.get(ats_id)).append("\">")
                       .append( ((Float)h_ats_multiplier.get(ats_id)).floatValue() * figureDtls[i].fig_value)
                       .append("</value>");
                }
                xml.append("</default_value>");
                xml.append("</figure_value>").append(cwUtils.NEWL);
            }
            xml.append("</figure_value_list>").append(cwUtils.NEWL);
            
            return xml.toString();
        }



    public void updUserFigure(Connection con, loginProfile prof, long usr_ent_id, long itm_id, long[] fgt_id_list, String[] fig_value_list)
        throws SQLException {
            long app_id = aeApplication.getAppId(con, itm_id, usr_ent_id, false);
            updUserFigure(con, prof, app_id, fgt_id_list, fig_value_list);
            return;
        }
    
    public void updUserFigure(Connection con, loginProfile prof, long app_id, long[] fgt_id_list, String[] fig_value_list)
        throws SQLException{
            
            if( fgt_id_list == null )
                return;
                
            long fig_id;
            Hashtable idTable = new Hashtable();
            DbFigure[] figures = ViewFigure.getUserFigureValue(con, app_id);
            for (int i=0; i<figures.length; i++){
                idTable.put(new Long(figures[i].fig_fgt_id), new Long(figures[i].fig_id));
            }
            Timestamp curTime = cwSQL.getTime(con);
            
            DbFigure dbFig = new DbFigure();
            DbUserFigure dbUfg = new DbUserFigure();
            Vector delId = new Vector();
            for(int i=0; i<fgt_id_list.length; i++){
                if( idTable.containsKey(new Long(fgt_id_list[i])) ) {
                    if( !fig_value_list[i].equalsIgnoreCase(EMPTY) ) {
                        dbFig.fig_id = ((Long)idTable.get(new Long(fgt_id_list[i]))).longValue();
                        dbFig.fig_value = (Float.valueOf(fig_value_list[i])).floatValue();
                        dbFig.updValue(con);
                    } else {
                        delId.addElement(idTable.get(new Long(fgt_id_list[i])));
                    }
                }else if( !fig_value_list[i].equalsIgnoreCase(EMPTY) ) {
                    dbFig.fig_fgt_id = fgt_id_list[i];
                    dbFig.fig_value = (Float.valueOf(fig_value_list[i])).floatValue();
                    dbFig.fig_update_usr_id = prof.usr_id;
                    dbFig.fig_update_timestamp = curTime;
                    dbFig.fig_create_usr_id = prof.usr_id;
                    dbFig.fig_create_timestamp = curTime;
                    dbFig.ins(con);

                    dbUfg.ufg_att_app_id = app_id;
                    dbUfg.ufg_fig_id = dbFig.fig_id;
                    dbUfg.ins(con);
                }
            }
            

            if( delId.size() > 0 ) {
                dbUfg.ufg_att_app_id = app_id;
                dbUfg.del(con, delId);
                dbFig.del(con, delId);
            }
            
            return;
        }
        
    public void updMultiUserFigure(Connection con, loginProfile prof, long itm_id, int ats_id, long[] app_id_lst)
        throws SQLException, cwException {
            
            if( app_id_lst == null )
                return;
                
            aeAttendanceStatus ats = new aeAttendanceStatus();
            ats.ats_id = ats_id;
            ats.get(con);
            
            Hashtable creditTable = aeItemFigure.getItemFigureValue(con, itm_id);
            long[] fgt_id_list = new long[creditTable.size()];
            String[] fig_value_list = new String[creditTable.size()];
            Enumeration enumeration = creditTable.keys();
            Long fig_fgt_id;
            int i=0;
            DbAttendanceFigureType dbAfg = new DbAttendanceFigureType();
            dbAfg.afg_ats_id = ats_id;
            while(enumeration.hasMoreElements()){
                fig_fgt_id = (Long)enumeration.nextElement();
                fgt_id_list[i] = fig_fgt_id.longValue();
                dbAfg.afg_fgt_id = fgt_id_list[i];
                fig_value_list[i] = (new Float(((Float)creditTable.get(fig_fgt_id)).floatValue() * dbAfg.getMultiplier(con))).toString();
                i++;
            }
                
            for(i=0; i<app_id_lst.length; i++)
                updUserFigure(con, prof, app_id_lst[i], fgt_id_list, fig_value_list);

            return;
        }


    public static void delByAppn(Connection con, long app_id)
        throws SQLException {
            DbFigure[] figures = ViewFigure.getUserFigureValue(con, app_id);
            Vector v_fig_id = new Vector();
            for (int i=0; i<figures.length; i++)
                v_fig_id.addElement(new Long(figures[i].fig_id));
                
            DbUserFigure.delByAppn(con, app_id);
            DbFigure.del(con, v_fig_id);
            return;
        }

    public void delAll(Connection con, long itm_id)
        throws SQLException {
            
            Vector v_fig_id = ViewFigure.getUserFigureIdByItemId(con, itm_id, 0);
            DbUserFigure dbUfg = new DbUserFigure();
            dbUfg.del(con, v_fig_id);
            if (v_fig_id != null && v_fig_id.size() > 0) {
                DbFigure.del(con, v_fig_id);
            }
        }


    public Vector getUserFigureValueId(Connection con, long itm_id, long fgt_id)
        throws SQLException, cwException {
            
            return ViewFigure.getUserFigureIdByItemId(con, itm_id, fgt_id);

        }




    public void updRelatedFigure(Connection con, long ats_id, long app_id, String upd_usr_id)
        throws SQLException {
            
            DbFigure[] figures = ViewFigure.getUserFigureValue(con, app_id);
            Hashtable h_fig = new Hashtable();
            for (int i=0; i<figures.length; i++){
                h_fig.put(new Long(figures[i].fig_id), new Long(figures[i].fig_fgt_id));
            }
            
            Timestamp cur_time = cwSQL.getTime(con);
            Enumeration enumeration = h_fig.keys();
            DbFigure dbFig = new DbFigure();
            DbAttendanceFigureType dbAfg = new DbAttendanceFigureType();
            dbAfg.afg_ats_id = ats_id;
            while(enumeration.hasMoreElements()){
                dbFig.fig_id = ((Long)enumeration.nextElement()).longValue();
                dbFig.get(con);
                
                dbAfg.afg_fgt_id = ((Long)h_fig.get(new Long(dbFig.fig_id))).longValue();
                
                dbFig.fig_update_usr_id = upd_usr_id;
                dbFig.fig_update_timestamp = cur_time;
                dbFig.fig_value = dbAfg.getMultiplier(con);
                dbFig.upd(con);
            }
            return;
        }

    public void insUserFigure(Connection con, long itm_id, Vector v_app_id, String usr_id, long fgt_id)
        throws SQLException {
            aeAttendance att = new aeAttendance();
            Hashtable h_app_ats = att.getAppnAtsHash(con, v_app_id);
            Enumeration enumeration = h_app_ats.keys();
            long app_id;
            long ats_id;
            while(enumeration.hasMoreElements()){
                app_id = ((Long)enumeration.nextElement()).longValue();
                ats_id = ((Long)h_app_ats.get(new Long(app_id))).longValue();
                insRelatedFigure(con, itm_id, ats_id, app_id, usr_id, fgt_id);
            }
            return;
        }


    public void insRelatedFigure(Connection con, long itm_id, long ats_id, long app_id, String create_usr_id)
        throws SQLException {
            insRelatedFigure(con, itm_id, ats_id, app_id, create_usr_id, 0);
            return;
        }
    public void insRelatedFigure(Connection con, long itm_id, long ats_id, long app_id, String create_usr_id, long fgt_id)
        throws SQLException {
            
            DbFigure[] figures = ViewFigure.getItemFigure(con, itm_id, fgt_id);
            Hashtable h_fgt_value = new Hashtable();
            for (int i=0; i<figures.length; i++){
                h_fgt_value.put(new Long(figures[i].fig_fgt_id), new Float(figures[i].fig_value));
            }

            Timestamp cur_time = cwSQL.getTime(con);

            DbUserFigure dbUfg = new DbUserFigure();
            dbUfg.ufg_att_app_id = app_id;

            DbAttendanceFigureType dbAfg = new DbAttendanceFigureType();
            dbAfg.afg_ats_id = ats_id;
            
            DbFigure dbFig = new DbFigure();
            Enumeration enumeration = h_fgt_value.keys();
            while(enumeration.hasMoreElements()){

                dbAfg.afg_fgt_id = ((Long)enumeration.nextElement()).longValue();
                
                dbFig.fig_fgt_id = dbAfg.afg_fgt_id;
                dbFig.fig_create_usr_id = create_usr_id;
                dbFig.fig_update_usr_id = create_usr_id;
                dbFig.fig_create_timestamp = cur_time;
                dbFig.fig_update_timestamp = cur_time;
                dbFig.fig_value = dbAfg.getMultiplier(con) * ((Float)h_fgt_value.get(new Long(dbAfg.afg_fgt_id))).floatValue();
                
                dbFig.ins(con);

                dbUfg.ufg_fig_id = dbFig.fig_id;
                dbUfg.ins(con);
            }
            
            
        }


    public void delNotInList(Connection con, Vector v_itm_id, Vector v_fgt_id)
        throws SQLException, cwException {
            
            if( v_itm_id == null || v_itm_id.isEmpty() || v_fgt_id == null || v_fgt_id.isEmpty() )
                return;
            Vector v_fig_id = ViewFigure.getNotInListUserFigId(con, v_itm_id, v_fgt_id);
            DbUserFigure.del(con, v_fig_id);
            DbFigure.del(con, v_fig_id);
            return;
        }

}