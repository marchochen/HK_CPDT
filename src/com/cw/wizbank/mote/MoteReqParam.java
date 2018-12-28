package com.cw.wizbank.mote;

import javax.servlet.ServletRequest;
//import java.io.UnsupportedEncodingException;

import com.oroinc.text.perl.*;

import java.sql.Timestamp;
import com.cw.wizbank.*;
//import com.cw.wizbank.db.*;
import com.cw.wizbank.util.*;

//import com.cw.wizbank.ae.aeItem;

public class MoteReqParam extends ReqParam {
    public int curPage;
    public String mote_status;
    public long itm_id;
    public String itm_type;
    public long[] imt_id_lst;
    String var;
    
    Mote mote = null;

    public MoteReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;  
            super.common();            
        
            String  var = req.getParameter("page");
            if (var!= null && var.length()!= 0 ){
                try{
                    curPage = Integer.parseInt(var);
                }catch(NumberFormatException e){
                    curPage = 0;    
                }
            }else{
                curPage = 0;
            }
    }
    
    public void mote(String clientEnc, String env_encoding){
        mote = new Mote();
        var = req.getParameter("mote_status");
        if (var != null && var.length() > 0) {
            mote_status = var;
            mote.status = var;
        }
        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try{
                itm_id = Long.parseLong(var);
            }catch (NumberFormatException e){
                itm_id = 0;    
            }
        }
        // for upd
        var = req.getParameter("itm_type");
        if (var != null && var.length() > 0) {
            itm_type = var;
        }

        // for insMoteDefault
        var = req.getParameter("imd_id");
        if (var != null && var.length() > 0) {
            try{
                mote.imd_id = Long.parseLong(var);
            }catch (NumberFormatException e){
                mote.imd_id = 0;    
            }
        }
        // for ins and upd
        var = req.getParameter("status");
        if (var != null && var.length() > 0) {
            mote.status = var;
        }
        try{
            var = req.getParameter("plan_xml");
            if (var != null && var.length() > 0) {
                mote.plan_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);

                Perl5Util perl = new Perl5Util();            
                mote.plan_xml = perl.substitute("s#&(?!amp;)#&amp;#ig", mote.plan_xml);
            }
        } catch (cwException e) {
            mote.plan_xml = "<mote_plan_desc></mote_plan_desc><mote_plan><resource_list></resource_list></mote_plan>";
        }


        var = req.getParameter("imt_id");
        if (var != null && var.length() > 0) {
            try{
                mote.imt_id = Long.parseLong(var);
            }catch (NumberFormatException e){
                mote.imd_id = 0;    
            }
        }
        var = req.getParameter("title");
        if (var != null && var.length() > 0) {
            mote.title = var;
        }
        var = req.getParameter("due_date");
        if (var != null && var.length() > 0) {
            mote.due_date = Timestamp.valueOf(var);
        }
        var = req.getParameter("eff_start_date");
        if (var != null && var.length() > 0) {
            mote.eff_start_date = Timestamp.valueOf(var);
        }
        var = req.getParameter("eff_end_date");
        if (var != null && var.length() > 0) {
            mote.eff_end_date = Timestamp.valueOf(var);
        }

        var = req.getParameter("level1_ind");
        if (var != null && var.length() > 0) {
            mote.level1_ind = Integer.parseInt(var);
        }
        var = req.getParameter("level2_ind");
        if (var != null && var.length() > 0) {
            mote.level2_ind = Integer.parseInt(var);
        }
        var = req.getParameter("level3_ind");
        if (var != null && var.length() > 0) {
            mote.level3_ind = Integer.parseInt(var);
        }
        var = req.getParameter("level4_ind");
        if (var != null && var.length() > 0) {
            mote.level4_ind = Integer.parseInt(var);
        }
        try{
            var = req.getParameter("attend_comment_xml");
            if (var != null && var.length() > 0) {
                mote.attend_comment_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.attend_comment_xml = "<comment></comment>";
        }
        
        var = req.getParameter("participant_target");
        if (var != null && var.length() > 0) {
            mote.participant_target = var;
        }

        var = req.getParameter("rating_target");
        if (var != null && var.length() > 0) {
            mote.rating_target = var;
        }else{
            mote.rating_target = null;
        }
        
        try{
            var = req.getParameter("rating_actual_xml");
            if (var != null && var.length() > 0) {
                mote.rating_actual_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.rating_actual_xml = "";
        }
        try{
            var = req.getParameter("rating_comment_xml");
            if (var != null && var.length() > 0) {
                mote.rating_comment_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.rating_comment_xml = "<comment></comment>";
        }

        var = req.getParameter("cost_target");
        if (var != null && var.length() > 0) {
            mote.cost_target = var;
        }else{
            mote.cost_target = null;
        }

        var = req.getParameter("cost_actual");
        if (var != null && var.length() > 0) {
            mote.cost_actual = var;
        }else{
            mote.cost_actual = null;
        }
        
        try{
            var = req.getParameter("cost_comment_xml");
            if (var != null && var.length() > 0) {
                mote.cost_comment_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.cost_comment_xml = "<comment></comment>";
        }
        
        var = req.getParameter("time_target");
        if (var != null && var.length() > 0) {
            mote.time_target = var;
        }else{
            mote.time_target = null;
        }

        var = req.getParameter("time_actual");
        if (var != null && var.length() > 0) {
            mote.time_actual = var;
        }else{
            mote.time_actual = null;
        }

        try{
            var = req.getParameter("time_comment_xml");
            if (var != null && var.length() > 0) {
                mote.time_comment_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.time_comment_xml = "<comment></comment>";
        }
        try{
            var = req.getParameter("comment_xml");
            if (var != null && var.length() > 0) {
                mote.comment_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.comment_xml = "<comment></comment>";
        }
        try{
            var = req.getParameter("attch1_xml");
            if (var != null && var.length() > 0) {
                    mote.attch1_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
                }
        } catch (cwException e) {
            mote.attch1_xml = "<attachment><resource_list></resource_list></attachment>";
        }
        try{
            var = req.getParameter("attch2_xml");
            if (var != null && var.length() > 0) {
                mote.attch2_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.attch2_xml = "<attachment><resource_list></resource_list></attachment>";
        }
        try{
            var = req.getParameter("attch3_xml");
            if (var != null && var.length() > 0) {
                mote.attch3_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.attch3_xml = "<attachment><resource_list></resource_list></attachment>";
        }
        try{
            var = req.getParameter("attch4_xml");
            if (var != null && var.length() > 0) {
                mote.attch4_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (cwException e) {
            mote.attch4_xml = "<attachment><resource_list></resource_list></attachment>";
        }
        
        // for del multi
        var = req.getParameter("imt_id_lst");
        if (var != null && var.length() > 0) {
            imt_id_lst = cwUtils.splitToLong(var, "~");
        }

    }

}