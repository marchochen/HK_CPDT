package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.*;
import com.cw.wizbank.ae.db.view.ViewFigure;


public class aeFigure {
    
    public final static String IAD001 = "IAD001";
    
    
    /**
    * Insert Figure and Item Figure Record into database    
    */
    public void ins(Connection con, String usr_id, long itm_id, DbFigure dbFig)
        throws SQLException, cwException, cwSysMessage {


            Timestamp curTime = cwSQL.getTime(con);
            dbFig.fig_update_usr_id = usr_id;
            dbFig.fig_update_timestamp = curTime;
            dbFig.fig_create_usr_id = usr_id;
            dbFig.fig_create_timestamp = curTime;
            dbFig.ins(con);
            
            DbItemFigure dbIfg = new DbItemFigure();
            dbIfg.ifg_itm_id = itm_id;
            dbIfg.ifg_fig_id = dbFig.fig_id;
            dbIfg.ins(con);
            
            return;
        
        }
    
    
    
    
    /**
    * Update Figure 
    */
    public void upd(Connection con, loginProfile prof, DbFigure dbFig)
        throws SQLException, cwException {
            
            Timestamp curTime = cwSQL.getTime(con);
            dbFig.fig_update_usr_id = prof.usr_id;
            dbFig.fig_update_timestamp = curTime;
            dbFig.upd(con);
            
            return;
            
        }
    





    /**
    * Delete all Figure Values of the item
    */
    public void delAll(Connection con, long itm_id)
        throws SQLException, cwException {
            long[] fig_id_lst = getFigureId(con, itm_id, 0);
            Vector vec = new Vector();
            if( fig_id_lst != null && fig_id_lst.length > 0 ) {
                for(int i=0; i<fig_id_lst.length; i++)
                    vec.addElement(new Long(fig_id_lst[i]));
                del(con, itm_id, vec);
            }
            return;
        }
    







    /**
    * Delete figure and item figure
    */
    public void del(Connection con, long itm_id, Vector v_fig_id)
        throws SQLException, cwException{
            
            if( v_fig_id != null && v_fig_id.size() > 0 ) {
                DbItemFigure dbIfg = new DbItemFigure();
                dbIfg.ifg_itm_id = itm_id;
                dbIfg.del(con, v_fig_id);
                DbFigure.del(con, v_fig_id);
            }
            return;
        }








    /**
    * Insert related item(run) figure
    */
    public void insRelatedItemFigure(Connection con, loginProfile prof, long itm_id, DbFigure dbFig )
        throws SQLException, cwException, cwSysMessage {

            DbFigure _dbFig = new DbFigure();
            _dbFig.fig_fgt_id = dbFig.fig_fgt_id;
            _dbFig.fig_value = dbFig.fig_value;

            // Get all runs belong to the item
            aeItem run;
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            Vector vec;
            try{
                vec = itm.getRunItemAsVector(con, false, false, null);
            }catch(qdbException e) {
                throw new cwException("Failed to get run items : " + e.getMessage());
            }catch(cwSysMessage e) {
                throw new cwException("Failed to get run items : " + e.getMessage());
            }


            for(int i=0; i<vec.size(); i++) {
                run = (aeItem)vec.elementAt(i);
                ins(con, prof.usr_id, run.itm_id, _dbFig);
            }
            
            return;
        }












    
    /**
    * Update related item(run) figure
    */
    public void updRelatedItemFigure(Connection con, loginProfile prof, long itm_id, DbFigure dbFig)
        throws SQLException, cwException, cwSysMessage {

            DbFigure _dbFig = new DbFigure();
            // Get the origianl value of item credit
            _dbFig.fig_id = dbFig.fig_id;
            _dbFig.get(con);
            _dbFig = new DbFigure();
            _dbFig.fig_fgt_id = dbFig.fig_fgt_id;
            _dbFig.fig_value = dbFig.fig_value;

            // Get all runs belong to the item
            aeItem run = null;
            long[] id_lst = null;
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            Vector vec = null;
            
            try{
                vec = itm.getRunItemAsVector(con, false, false, null);
            }catch(qdbException e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }catch(cwSysMessage e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }
            
            Vector v_fig = new Vector();
            DbItemFigure dbIfg = new DbItemFigure();

            if( vec == null || vec.isEmpty() )
                return;
            
            for(int i=0; i<vec.size(); i++) {
                run = (aeItem)vec.elementAt(i);
                //Assume run will not contain more than one credit value of each cedit type
                id_lst = getFigureId(con, run.itm_id, dbFig.fig_fgt_id);
                if( id_lst != null && id_lst.length > 0 ) {
                    _dbFig.fig_id = id_lst[0];
                    upd(con, prof, _dbFig);
                } else 
                    ins(con, prof.usr_id, run.itm_id, _dbFig);
            }
            return;
        }
    
    
    
    
    
    
    
    /**
    * Inherent parent item figure
    */
    public void inherentParentFigure(Connection con, String usr_id, long parent_id, aeItem itm)
        throws SQLException, cwException, cwSysMessage {

            Vector itmVec = new Vector();
            itmVec.addElement(new Long(parent_id));
            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getFigureDetail(con, itmVec, null, null, null, null);
            DbFigure dbFig = new DbFigure();
            for (int i=0; i<figureDtls.length; i++){
                dbFig.fig_fgt_id = figureDtls[i].fgt_id;
                dbFig.fig_value = figureDtls[i].fig_value;
                ins(con, usr_id, itm.itm_id, dbFig);
            }
            return;

        }
    
    
    
    
    
    
    
    /**
    * Delete related item(run) figure by figure id
    */
    public void delChildFigure(Connection con, long itm_id, Vector v_fig_id)
        throws SQLException, cwException {

            //Get all runs belong to the item
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            Vector v_run_itm = null;
            try{
                v_run_itm = itm.getRunItemAsVector(con, false, false, null);
            }catch(qdbException e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }catch(cwSysMessage e) {
                throw new cwException("Failed to get item : " + e.getMessage());
            }

            DbFigure dbFig = new DbFigure();
            DbItemFigure dbIfg = new DbItemFigure();
            aeUserFigure aeUfg = new aeUserFigure();
            DbUserFigure dbUfg = new DbUserFigure();
            
            Vector v_del_fig_id = new Vector();
            Vector v_ufg_fgt_id = null;
            
            long fig_id;
            aeItem run = null;
            long[] fig_id_lst = null;
            
            for(int i=0; i<v_fig_id.size(); i++) {
                dbFig.fig_id = ((Long)v_fig_id.elementAt(i)).longValue();
                dbFig.get(con);
                
                for(int j=0; j<v_run_itm.size(); j++) {
                    run = (aeItem)v_run_itm.elementAt(j);
                    fig_id_lst = getFigureId(con, run.itm_id, dbFig.fig_fgt_id);
                    if( fig_id_lst != null && fig_id_lst.length > 0 ) {
                        dbIfg.ifg_itm_id = run.itm_id;
                        dbIfg.ifg_fig_id = fig_id_lst[0];
                        dbIfg.del(con);

                        //Add Item Accreditation Value Id to vector for batch delete
                        v_del_fig_id.addElement(new Long(dbIfg.ifg_fig_id));

                        //Remove User Figure
                        //Each User not more than one figure type value in each item
                        //dbUfg.ufg_itm_id = run.itm_id;
                        v_ufg_fgt_id = ViewFigure.getUserFigureIdByItemId(con, run.itm_id, dbFig.fig_fgt_id);
                        dbUfg.del(con, v_ufg_fgt_id);
                        v_del_fig_id.addAll(v_del_fig_id);
                    }
                }
            }

            if( v_del_fig_id.size() > 0 ) {
                DbFigure.del(con, v_del_fig_id);
            }

            return;
        }
    

    
    /**
    *Get all figure(s) id belong to the specified item and figure type
    */
    public long[] getFigureId(Connection con, long itm_id, long fgt_id)
        throws SQLException, cwException {
            
            Vector v_fig_id = ViewFigure.getUserFigureIdByItemId(con, itm_id, fgt_id);
            long[] fig_id_lst = null;
            if( v_fig_id.size() > 0 ) {
                fig_id_lst = new long[v_fig_id.size()];
                for(int i=0; i<fig_id_lst.length; i++)
                    fig_id_lst[i] = ((Long)v_fig_id.elementAt(i)).longValue();
            }
            
            return fig_id_lst;
            
        }
    
    
    
    
    
    
    /**
    * Get the item crediations as XML
    */
    public String getDetailAsXML(Connection con, long itm_id, String[] fgt_type, String[] fgt_subtype, String sortCol, String sortOrder)
        throws SQLException {

            Vector v_itm_id = new Vector();
            v_itm_id.addElement(new Long(itm_id));
            StringBuffer xml = new StringBuffer();

            xml.append("<figure_list>").append(cwUtils.NEWL);

            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getFigureDetail(con, v_itm_id, fgt_type, fgt_subtype, sortCol, sortOrder);
            for (int i=0; i<figureDtls.length; i++){

                xml.append("<figure ")
                   .append(" fig_id=\"").append(figureDtls[i].fig_id).append("\" ")
                   .append(" fig_value=\"").append(figureDtls[i].fig_value).append("\" ")
                   .append(" fgt_type=\"").append(figureDtls[i].fgt_type).append("\" ")
                   .append(" fgt_subtype=\"").append(cwUtils.escNull(figureDtls[i].fgt_subtype)).append("\" ")
                   .append(" >").append(cwUtils.NEWL)
                   .append(figureDtls[i].fgt_xml)
                   .append("</figure>").append(cwUtils.NEWL);
                
            }
            xml.append("</figure_list>").append(cwUtils.NEWL);
            return xml.toString();
        }
    




    /**
    * Get a item accreditation 
    */ 
    public String asXML(Connection con, long fig_id)
        throws SQLException, cwException {
            
            StringBuffer xml = new StringBuffer();
            ViewFigure.ViewFigureDetail[] figureDtls = ViewFigure.getFigureDetail(con, fig_id);
            if (figureDtls.length > 0){
                
                xml.append("<figure ")
                   .append(" fig_id=\"").append(figureDtls[0].fig_id).append("\" ")
                   .append(" fig_value=\"").append(figureDtls[0].fig_value).append("\" ")
                   .append(" fgt_type=\"").append(figureDtls[0].fgt_type).append("\" ")
                   .append(" fgt_subtype=\"").append(cwUtils.escNull(figureDtls[0].fgt_subtype)).append("\" ")
                   .append(" >").append(cwUtils.NEWL)
                   .append(figureDtls[0].fgt_xml)
                   .append("</figure>");

            } else 
                throw new cwException("Failed to get figure, fig_id = " + fig_id);
            return xml.toString();
        }
    
    
    
    
    
    
    /**
    * Get item figure for itm template
    */
    public static String getItemFigureXML(Connection con, long itm_id, long root_ent_id)
        throws SQLException {
            
            StringBuffer xml = new StringBuffer();
            Hashtable figureValueHash = getFigureValue(con, itm_id);
            DbFigureType[] figureTypes = DbFigureType.getAll(con, root_ent_id);
            for (int i=0; i<figureTypes.length; i++){
                xml.append("<figure id=\"").append(figureTypes[i].fgt_id).append("\">").append(cwUtils.NEWL);
                if( figureValueHash.containsKey(new Long(figureTypes[i].fgt_id)) )
                    xml.append("<num value=\"").append(figureValueHash.get(new Long(figureTypes[i].fgt_id))).append("\"/>").append(cwUtils.NEWL);
                xml.append(figureTypes[i].fgt_xml)
                   .append("</figure>").append(cwUtils.NEWL);
            }
            return xml.toString();
        }
                
/*                
    public static boolean checkAccreditationPeriod(Timestamp eff_start, Timestamp eff_end, Timestamp itm_start, Timestamp itm_end)
         {
            if( itm_start == null || itm_end == null )
                return true;
            
            long effStart = eff_start.getTime();
            long effEnd = eff_end.getTime();
            long itmStart = itm_start.getTime();
            long itmEnd = itm_end.getTime();
            if( itmStart <= effStart ) {
                
                if( itmEnd > effStart ) {
                    if( (itmEnd-effStart) > (effStart-itmStart) )
                        return true;
                }
            } else if( itmStart >= effStart && itmStart <= effEnd ) {
                if( itmEnd <= effEnd )
                    return true;
                
                else if( (effEnd-itmStart) >= (itmEnd-effEnd) )
                    return true;

            }
            
            return false;
        }
*/



    /**
    *Get all figure id and its value belongs to the specified item
    *frunction used for find run figure only, because run contain only 1 value of each item figure
    *but course contains more than 1 value for each figure
    *@return Hashtable figure id as key and figure value as element
    */
    public static Hashtable getFigureValue(Connection con, long itm_id)
        throws SQLException{
            
            DbFigure[] figures = ViewFigure.getItemFigure(con, itm_id, 0);
            Hashtable figureTable = new Hashtable();
            for (int i=0; i<figures.length; i++)
                figureTable.put(new Long(figures[i].fig_id), new Float(figures[i].fig_value));
            
            return figureTable;
            
        }
    
    
    
    
    /**
    * Copy the item figure from parent to itself
    */
    public void insParentFigure(Connection con, loginProfile prof, long parent_itm_id, long self_itm_id)
        throws SQLException, cwException, cwSysMessage {
            DbFigure[] figures = ViewFigure.getAllItemFigure(con, parent_itm_id);
            for (int i=0; i<figures.length; i++){
                ins(con, prof.usr_id, self_itm_id, figures[i]);
            }
            return;
        }

}
