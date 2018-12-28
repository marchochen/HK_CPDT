package com.cw.wizbank.qdb;

import java.sql.*;
//import com.ibm.tspaces.*;
import com.cw.wizbank.util.cwSysMessage;

public class dbChat extends dbModule {
    
    //public static final String CHATROOM_START = "Chatroom started, cannot delete it!";
    //public static final String CHATROOM_CANNOT_UPDATE ="Chatroom started, cannot modify starting time!";
    
    public String tsHost;
    public int tsPort;
    public int roomPort;
    public long room_res_id;
    public boolean room_start = false;


    /*
    private ConfigTuple config;
    private TupleSpace lobbyTS;
    private TupleSpace roomTS;
    */
    private Timestamp room_original_start_time;
    private String room_original_status;
    

    public dbChat() {
        super();
        //config = new ConfigTuple();
        //config.setOption(ConfigTuple.PERSISTENCE, Boolean.FALSE );
        //config.setOption(ConfigTuple.FIFO, Boolean.TRUE );
        //tsHost = "wailun.wizq.net";
        //tsPort = 7900;
    }

    public void initialize(dbModule dbmod, String _tsHost, int _tsPort, int _roomPort, int _wwwPort) {
        tsHost = _tsHost;
        tsPort = _tsPort;
        roomPort = _roomPort;
        room_res_id = dbmod.mod_res_id;

        
        mod_res_id = dbmod.mod_res_id;
        mod_type = dbmod.mod_type;
        mod_max_score = dbmod.mod_max_score;
        mod_pass_score = dbmod.mod_pass_score;
        mod_instruct = dbmod.mod_instruct;
        mod_max_attempt = dbmod.mod_max_attempt;
        mod_max_usr_attempt = 1;
        mod_score_ind = dbmod.mod_score_ind;
        mod_score_reset = dbmod.mod_score_reset;
        mod_in_eff_start_datetime = dbmod.mod_in_eff_start_datetime;
        mod_in_eff_end_datetime = dbmod.mod_in_eff_end_datetime;
        mod_usr_id_instructor = dbmod.mod_usr_id_instructor;
        mod_tshost = tsHost;
        mod_tsport = tsPort;
        mod_wwwport = _wwwPort;
        tkh_id = dbmod.tkh_id;
        
        res_id = dbmod.res_id;
        res_lan = dbmod.res_lan;
        res_title = dbmod.res_title;
        res_desc = dbmod.res_desc;
        res_type = RES_TYPE_MOD; // override
        res_subtype = dbmod.res_subtype; // override
        res_annotation = dbmod.res_annotation;
        res_format = dbmod.res_format;
        res_difficulty = dbmod.res_difficulty;
        res_privilege = dbmod.res_privilege;
        res_usr_id_owner = dbmod.res_usr_id_owner;
        res_tpl_name = dbmod.res_tpl_name;
        res_mod_res_id_test = dbmod.res_mod_res_id_test;
        res_status = dbmod.res_status;
        res_upd_user = dbmod.res_upd_user;
        res_upd_date = dbmod.res_upd_date;
        res_src_type = dbmod.res_src_type;
        res_src_link = dbmod.res_src_link;
        //res_url = dbmod.res_url;
        //res_filename = dbmod.res_filename;
        
        res_instructor_name = dbmod.res_instructor_name;
        res_instructor_organization = dbmod.res_instructor_organization;
        
    }


    public void ins(Connection con, loginProfile prof) 
        throws qdbException
        {
            super.ins(con, prof);
            mod_res_id = res_id;
            room_res_id = res_id;
            //insThread t = new insThread();
            //new Thread(t).start();
        }
    
    public void get(Connection con) 
        throws qdbException
        {   
            try{                                
                mod_res_id = room_res_id;
                super.get(con);
                //TupleSpace.cleanup();
            }catch(Exception e) {
                throw new qdbException("Get Error: " + e.getMessage()); 
            }                
        }

    public void upd(Connection con, loginProfile prof, boolean isChangeDate)
        throws qdbException, qdbErrMessage ,cwSysMessage
        {

                if( roomStarted(con, room_res_id) ) {
                    if( !(room_original_start_time.equals(mod_in_eff_start_datetime)) 
                    ||  res_status.equalsIgnoreCase("OFF") ) {
                        //chatroom_start = true;
                        //return;
                        String modSubType = dbResource.getResSubType(con, room_res_id);
                        
                        if(modSubType.equalsIgnoreCase("CHT"))
                            throw new qdbErrMessage("CHT001");
                        else
                            throw new qdbErrMessage("CHT002");
                    }
                }

                super.upd(con, prof, isChangeDate);
                
                //updThread t = new updThread();
                //new Thread(t).start();
        }
    
    public void del(Connection con, loginProfile prof) 
        throws qdbException, qdbErrMessage ,cwSysMessage
        {
            
                if( roomStarted(con, room_res_id) ) {
                   String modSubType = dbResource.getResSubType(con, room_res_id);

                   if(modSubType.equalsIgnoreCase("CHT"))
                       throw new qdbErrMessage("CHT001");
                    else
                       throw new qdbErrMessage("CHT002");
                }
                super.del(con, prof);
                
                //delThread t = new delThread();
                //new Thread(t).start();
        }
    
    
    public boolean roomStarted(Connection _con, long _resId) 
        throws qdbException ,cwSysMessage
        {            
            try{                
                Timestamp curTime = dbUtils.getTime(_con);                
                PreparedStatement stmt = _con.prepareStatement(
                    " SELECT mod_eff_start_datetime, mod_eff_end_datetime, res_status "
                +   " FROM Module , Resources "
                +   " WHERE res_id = mod_res_id AND mod_res_id = ? " );

                stmt.setLong(1, _resId);
                ResultSet rs = stmt.executeQuery();                
                if(rs.next())
                {   room_original_status = rs.getString("res_status");
                    //System.out.println("res_id " + _resId + " res_status : " + chat_original_status);
                    room_original_start_time = rs.getTimestamp("mod_eff_start_datetime");                    
                    if( curTime.after(rs.getTimestamp("mod_eff_start_datetime")) 
                    &&  curTime.before(rs.getTimestamp("mod_eff_end_datetime"))
                    &&  room_original_status.equalsIgnoreCase("ON") ) 
                    {
                        stmt.close();
                        return true;                    
                    }
                    else
                    {
                        stmt.close();
                        return false;
                    }

                }
                else
                {
                    stmt.close();
                    //throw new qdbException( "No data for resource. id = " + _resId);
                    throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Resource ID = " + res_id );
                }
            }catch(SQLException e) {
                 throw new qdbException("SQL Error: " + e.getMessage()); 
            }
        }
        
        
        /*
        private class insThread implements Runnable {
            public void run() {
                try{
                    lobbyTS = new TupleSpace("Lobby", tsHost, tsPort, config);
                    Tuple Template = new Tuple( new Long(room_res_id), tsHost, new Integer(roomPort), 
                                                mod_in_eff_start_datetime, mod_in_eff_end_datetime, 
                                                res_status, mod_type);
                    lobbyTS.write(Template);
                    lobbyTS = null;
                    TupleSpace.cleanup();
                }catch(TupleSpaceException e) {
                    return;
                }
            }
        }
        */
        
        
        /*
        private class updThread implements Runnable {
            public void run() {
                try{
                    if(TupleSpace.exists("Lobby", tsHost, tsPort)) {
                        lobbyTS = new TupleSpace("Lobby", tsHost, tsPort, config);
                        Tuple Template = new Tuple( new Long(room_res_id),                  //  resId
                                                    tsHost, new Integer(roomPort),          //  TSserver Host Name & Port
                                                    Field.makeField("java.sql.Timestamp"),  //  Start Time
                                                    Field.makeField("java.sql.Timestamp"),  //  End Time
                                                    Field.makeField("java.lang.String"),    //  Status
                                                    Field.makeField("java.lang.String")     //  module type
                                                    );  

                        //  If start time or end time modified, update the TupleSpace
                        Tuple tupleSet = lobbyTS.read(Template);
                        if( !(((Timestamp)tupleSet.getField(3).getValue()).equals(mod_in_eff_start_datetime))
                        ||  !(((Timestamp)tupleSet.getField(4).getValue()).equals(mod_in_eff_end_datetime)) 
                        ||  !(((String)tupleSet.getField(5).getValue()).equalsIgnoreCase(res_status)) ) {

                            Tuple Replacer = new Tuple( new Long(mod_res_id),
                                                        tsHost, new Integer(roomPort),
                                                        mod_in_eff_start_datetime,
                                                        mod_in_eff_end_datetime, 
                                                        res_status,
                                                        mod_type
                                                      );

                            lobbyTS.update(Template, Replacer);
                        }

                        lobbyTS = null;
                        TupleSpace.cleanup();
                    }        
                }catch(TupleSpaceException e) {
                    return;
                }            
            }
        }
        */
        /*
        private class delThread implements Runnable {
            public void run() {
                try{                                
                    lobbyTS = new TupleSpace("Lobby", tsHost, tsPort, config);
                    Tuple Template = new Tuple( new Long(mod_res_id),                   //  resId
                                                tsHost, new Integer(roomPort),          //  TSserver Host Name & Port
                                                Field.makeField("java.sql.Timestamp"),  //  Start Time
                                                Field.makeField("java.sql.Timestamp"),  //  End Time
                                                Field.makeField("java.lang.String"),    //  status
                                                Field.makeField("java.lang.String")     //  module type
                                              );
                    lobbyTS.delete(Template);
                    lobbyTS = null;
                    TupleSpace.cleanup();
                }catch(TupleSpaceException e) {
                    return;
                }                
            }
        }
        */
        
}
    /*
    public String asXML(Connection con, loginProfile prof, String dpo_view)
        throws qdbException
    {
            String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<module id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date +"\">" + dbUtils.NEWL;

            // author's information
            result += prof.asXML() + dbUtils.NEWL;

            result += dbResourcePermission.aclAsXML(con,res_id,prof);
            // Module Header 
            result += getModHeader(con, prof);
            result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id);
            //result += getEventAsXML(con);
            result += getDisplayOption(con, dpo_view);
            result += "<body>" + dbUtils.NEWL;

            result += "</body>" + dbUtils.NEWL;
            result += "</module>"; 
            
            return result;
    }   
    */

        
        
    