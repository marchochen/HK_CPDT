package com.cw.wizbank.course;

import javax.servlet.ServletRequest;

//import java.io.UnsupportedEncodingException;

import java.sql.Timestamp;
import java.util.*;
import java.io.*;

import com.cw.wizbank.*;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.ae.aeXMessage;
import com.cw.wizbank.db.DbCourseMeasurement;

public class CourseCriteriaReqParam extends ReqParam{
    public long ccr_id;
    public String type;
    public long cmr_id;
    public long mod_id;
    public long del_cmr_id;
    public int duration;
    public boolean has_duration;
    public int pass_score;
//    public float max_score;
    public boolean must_pass; 
    public boolean must_meet_all_cond;
    
//    public String relation_xml;
    public String status;
    public long cos_id;
    
    public float rate;
    public boolean is_contri_by_score;
    public int attendance_rate;
    public long itm_id;
    public String upd_method;
    public long cmt_id;
    public long[] cmr_lst;
    public String[] status_lst; 
    public float[] contri_rate_lst;
    public boolean[] is_contri_by_score_lst;
	public String cmt_title;
	public float cmt_max_score;
	public float cmt_pass_score;
	public float cmt_contri_rate;
	public long cos_res_id;
	public long mod_res_id;
	public long[] cmt_id_list;
	public float[] cmt_id_percent_list;
	public boolean re_evaluate_ind;
	public long cmt_id_del;
	public Timestamp upd_timestamp;
	public Timestamp[] upd_timestamp_list;
	public boolean upd_comp_date;
    String var;
    
//  for completion criteria
    public String pass_status;
    public String attend_status;
    public int ccr_pass_score;
    public int ccr_attendance_rate;
    public Timestamp ccr_update_timestamp;
    public Vector cmtOnlineVec;
    public Vector cmtScoringVec;
    public int online_item_count;
    public int scoring_item_count;
    public String offline_cond;
    public String recal;
    public String recal_date;
    public boolean is_new_cos;
    public String redirect_url;
    
    public CourseCriteriaReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;  
            super.common();
            String var = null;
    }
    
	public void attendance(String clientEnc, String env_encoding) {
			String var;
			var = req.getParameter("itm_id");
			if( var != null && var.length() > 0){
				try{
					itm_id = Integer.parseInt(var);
				}catch( NumberFormatException e ) {
					itm_id = 0 ;
				}
			}
			var = req.getParameter("ccr_id");
			if (var != null && var.length() > 0) {
				try {
					ccr_id = Integer.parseInt(var);
				} catch (NumberFormatException e) {
					ccr_id = 0;
				}
			}
			var = req.getParameter("cmt_id_del");
			if (var != null && var.length() > 0) {
				try {
					cmt_id_del = Integer.parseInt(var);
				} catch (NumberFormatException e) {
					cmt_id_del = 0;
				}
			}
			var = req.getParameter("cos_res_id");
			if (var != null && var.length() > 0) {
				try {
					cos_res_id = Integer.parseInt(var);
				} catch (NumberFormatException e) {
					cos_res_id = 0;
				}
			}
			var = req.getParameter("mod_res_id");
			if (var != null && var.length() > 0) {
				try {
					mod_res_id = Integer.parseInt(var);
				} catch (NumberFormatException e) {
					mod_res_id = 0;
				}
			}
			var = req.getParameter("cmt_id");
			if (var != null && var.length() > 0) {
				try {
					cmt_id = Integer.parseInt(var);
				} catch (NumberFormatException e) {
					cmt_id = 0;
				}
			}
			var = req.getParameter("cmr_id");
			if (var != null && var.length() > 0) {
				try {
					cmr_id = Integer.parseInt(var);
				} catch (NumberFormatException e) {
					cmr_id = 0;
				}
			}
			var = req.getParameter("cmt_title");
			try{
				var = req.getParameter("cmt_title");
				if( var != null && var.length() > 0){
					cmt_title = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
				}
			} catch (UnsupportedEncodingException e) {
				cmt_title = null;
			}
			var = req.getParameter("cmt_max_score");
			if (var != null && var.length() > 0) {
				try {
					cmt_max_score = Float.parseFloat(var);
				} catch (NumberFormatException e) {
					cmt_max_score = 0;
				}
			}
			var = req.getParameter("cmt_pass_score");
			if (var != null && var.length() > 0) {
				try {
					cmt_pass_score = Float.parseFloat(var);
				} catch (NumberFormatException e) {
					cmt_pass_score = 0;
				}
			}
			var = req.getParameter("cmt_contri_rate");
			if (var != null && var.length() > 0) {
				try {
					cmt_contri_rate = Float.parseFloat(var);
				} catch (NumberFormatException e) {
					cmt_contri_rate = 0;
				}
			}
			var = req.getParameter("re_evaluate_ind");
			if (var != null && var.length() > 0) {
				re_evaluate_ind = Boolean.valueOf(var).booleanValue();
			}else{
				re_evaluate_ind = false;
			}
			var = req.getParameter("upd_comp_date");
			if (var != null && var.length() > 0) {
				upd_comp_date = Boolean.valueOf(var).booleanValue();
			} else {
				upd_comp_date = false;
			}
			var = req.getParameter("cmt_id_list");
			if (var != null && var.length() != 0)
				cmt_id_list = String2long(split(var, "~"));
			else
				cmt_id_list = null;
			var = req.getParameter("cmt_id_percent_list");
			if (var != null && var.length() != 0)
				cmt_id_percent_list = Stringto2decPtfloat(split(var, "~"));
			else
				cmt_id_percent_list = null;
			if(cmt_id_list != null){
				upd_timestamp_list = new Timestamp[cmt_id_list.length];
				for(int i=0;i<cmt_id_list.length;i++){
					var = req.getParameter("upd_timestamp_"+cmt_id_list[i]);
					if (var != null && var.length() > 0) {
						upd_timestamp_list[i] = Timestamp.valueOf(var);
					}
					else
						upd_timestamp_list[i] = null;
				}
			}
			var = req.getParameter("upd_timestamp");
			if (var != null && var.length() > 0) {
				upd_timestamp = Timestamp.valueOf(var);
			}
			else
				upd_timestamp = null;
	}
    public void criteria(){
//        var = req.getParameter("relation_xml");
//        if (var != null && var.length() > 0) {
//            relation_xml = var;
//        }
        var = req.getParameter("ccr_id");
        if (var != null && var.length() > 0) {
            ccr_id = Long.parseLong(var);
        }
        
        var = req.getParameter("type");
        if (var != null && var.length() > 0) {
            type = var;
        }
        
        var = req.getParameter("cmr_id");
        if (var != null && var.length() > 0) {
            cmr_id = Long.parseLong(var);
        }
        var = req.getParameter("pass_score");
        if (var != null && var.length() > 0){
            pass_score = Integer.parseInt(var);
        }
//        var = req.getParameter("max_score");
//        if (var != null && var.length() > 0){
//            max_score = Float.valueOf(var).floatValue();    
//        }
        
        var = req.getParameter("duration");
        if (var != null && var.length() > 0) {
            duration = Integer.parseInt(var);
            has_duration = true;
//            if (duration>0){
//                upd_method = CourseCriteria.UPD_METHOD_DURATION; 
//            }else{
//                upd_method = CourseCriteria.UPD_METHOD_DATE; 
//            }
        }
        var = req.getParameter("must_pass");
        if (var != null && var.length() > 0) {
            must_pass = Boolean.valueOf(var).booleanValue();
        }else{
            must_pass = true;
        }
        var = req.getParameter("must_meet_all_cond");
        if (var != null && var.length() > 0) {
            must_meet_all_cond = Boolean.valueOf(var).booleanValue();
        }else{
            must_meet_all_cond = true;                
        }

        var = req.getParameter("mod_id");
        if (var != null && var.length() > 0) {
            mod_id = Long.parseLong(var);
        }

        var = req.getParameter("status");
        if (var != null && var.length() > 0) {
            status = var;
        }
        var = req.getParameter("cmr_lst");
        if (var != null && var.length() > 0) {
            cmr_lst = cwUtils.splitToLong(var, "~");
        }

        var = req.getParameter("status_lst");
        if (var != null && var.length() > 0) {
            status_lst = cwUtils.splitToString(var, "~");
        }
        var = req.getParameter("contri_rate_lst");
        if (var != null && var.length() > 0) {
            contri_rate_lst = cwUtils.splitToFloat(var, "~");
        }
        var = req.getParameter("is_contri_by_score_lst");
        if (var != null && var.length() > 0) {
            is_contri_by_score_lst = cwUtils.splitToBoolean(var, "~");
        }
        
        var = req.getParameter("cos_id");
        if (var != null && var.length() > 0) {
            cos_id = Long.parseLong(var);
        }


        var = req.getParameter("rate");
        if (var != null && var.length() > 0) {
            rate = Float.valueOf(var).floatValue();
        }

        var = req.getParameter("is_contri_by_score");
        if (var != null && var.length() > 0) {
            is_contri_by_score = Boolean.valueOf(var).booleanValue();
        }

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            itm_id = Long.parseLong(var);
        }

        var = req.getParameter("attendance_rate");
        if (var != null && var.length() > 0) {
            attendance_rate = Integer.parseInt(var);
        }

        var = req.getParameter("upd_method");
        if (var != null && var.length() > 0) {
            upd_method = var;
        }

        var = req.getParameter("del_cmr_id");
        if (var != null && var.length() > 0) {
            del_cmr_id = Long.parseLong(var);
        }
    }

    public String title;
    public Timestamp appn_date;
    public Timestamp last_date;
    public long ent_id;
    public String sender_id;
    public void notification(String clientEnc, String env_encoding) throws cwException{
        var = req.getParameter("sender_id");
        if (var != null && var.length() > 0) {
            sender_id = var;
        }

        var = req.getParameter("ent_id");
        if (var != null && var.length() > 0) {
            ent_id = Long.parseLong(var);
        }

        var = req.getParameter("title");
        if (var != null && var.length() > 0) 
            title = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            
        var = req.getParameter("appn_date");
        if (var != null && var.length() > 0) 
            appn_date = Timestamp.valueOf(var);

        var = req.getParameter("last_date");
        if (var != null && var.length() > 0) 
            last_date = Timestamp.valueOf(var);

    }
    public void test(){
//        Connection con, long mod_id, long cos_id, long ccr_id, long usr_ent_id, String status, long root_ent_id
    }
    
    
	public static long[] String2long(String[] var) {

		long[] result = new long[var.length];

		for(int i=0;i<result.length;i++)
			result[i] = Long.parseLong(var[i]);

		return result;
	}

	public static int[] String2int(String[] var) {

		int[] result = new int[var.length];

		for(int i=0;i<result.length;i++)
			result[i] = Integer.parseInt(var[i]);

		return result;
	}

	public static float[] Stringto2decPtfloat(String[] var) {

		float[] result = new float[var.length];

		for(int i=0;i<result.length;i++)
			result[i] = to2decPt(var[i]);

		return result;
	}
	public static float to2decPt(String var) {
		if(var.indexOf(".")!= -1) {
			if(var.length() > var.indexOf(".") + 3)
				return (Float.valueOf(var.substring(0, var.indexOf(".")+3))).floatValue();
		}

		return (Float.valueOf(var)).floatValue();
	}
    
    aeXMessage aeXmsg;
    long tkh_id;
    public void course_completion() throws cwException
    {
        aeXmsg = new aeXMessage();
        
        aeXmsg.sender_id = getStringParameter("sender_id");
        aeXmsg.id  = getLongParameter("id");
        aeXmsg.ent_id = getLongParameter("ent_ids");
        tkh_id  = getLongParameter("tkh_id");
    }
    
    public void getItemId() throws cwException{
        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            itm_id = Long.parseLong(var);
        }else{
            itm_id = 0;
        }
    }
    
    public void getIsNewCos() throws cwException{
		var = req.getParameter("is_new_cos");
		if (var != null && var.length() > 0) {
			is_new_cos = Boolean.valueOf(var).booleanValue();
		} else {
			is_new_cos = false;
		}
    }
    
    public void getRedirectUrl() throws cwException{
		var = req.getParameter("redirect_url");
		if (var != null && var.length() > 0) {
			redirect_url = var;
		} else {
			redirect_url = "";
		}
    }
    
    public void getCcrParam() throws cwException {

        var = req.getParameter("ccr_id");
        if (var != null && var.length() > 0) {
            try {
                ccr_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                ccr_id = 0;
            }
        } else {
            ccr_id = 0;
        }

        var = req.getParameter("mark_status");
        if (var != null && var.length() > 0) {
            pass_status = var;
        } else {
            pass_status = "";
        }

        var = req.getParameter("attend_status");
        if (var != null && var.length() > 0) {
            attend_status = var;
        } else {
            attend_status = "";
        }
        
        var = req.getParameter("pass_mark");
        if (var != null && var.length() > 0) {
            try {
                ccr_pass_score = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                ccr_pass_score = 0;
            }
        } else {
            ccr_pass_score = 0;
        }

        var = req.getParameter("pass_attend");
        if (var != null && var.length() > 0) {
            try {
                ccr_attendance_rate = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                ccr_attendance_rate = 0;
            }
        } else {
            ccr_attendance_rate = 0;
        }

        var = req.getParameter("offline_cond");
        if (var != null && var.length() > 0) {
            offline_cond = unicode(var);
        } else {
            offline_cond = "";
        }

        var = req.getParameter("upd_timestamp");
        if (var != null && var.length() > 0) {
            try {
                ccr_update_timestamp = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                ccr_update_timestamp = null;
            }
        } else {
            ccr_update_timestamp = null;
        }
    }
    
    public void getCosCondRequest() throws cwException {

        this.getCcrParam();

        var = req.getParameter("online_item_count");
        if (var != null && var.length() > 0) {
            try {
                online_item_count = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                online_item_count = 0;
            }
        } else {
            online_item_count = 0;
        }

        String param1, param2, param3;
        String desc, sel;
        long cmt_id;
        cmtOnlineVec = new Vector();
        for (int i = 1; i <= online_item_count; i++) {
            param1 = "o_item_desc_" + i;
            param2 = "o_item_cond_sel_" + i;
            param3 = "o_item_id_" + i;

            var = req.getParameter(param1);
            if (var != null && var.length() > 0) {
                desc = unicode(var);
            } else {
                desc = "";
            }

            var = req.getParameter(param2);
            if (var != null && var.length() > 0) {
                sel = var;
            } else {
                sel = "";
            }

            var = req.getParameter(param3);
            if (var != null && var.length() > 0) {
                try {
                    cmt_id = Long.parseLong(var);
                } catch (NumberFormatException e) {
                    cmt_id = 0;
                }
            } else {
                cmt_id = 0;
            }

            if ((desc != null && desc.length() > 0) || cmt_id > 0) {
                DbCourseMeasurement cmt = new DbCourseMeasurement();
                cmt.setCmt_title(desc);
                cmt.setCmt_id(cmt_id);
                cmt.setCmt_status_desc_option(sel);
                cmt.setCmt_ccr_id(ccr_id);
                cmtOnlineVec.addElement(cmt);
            }
        }

        var = req.getParameter("scoring_item_count");
        if (var != null && var.length() > 0) {
            try {
                scoring_item_count = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                scoring_item_count = 0;
            }
        } else {
            scoring_item_count = 0;
        }
        cmtScoringVec = new Vector();
        for (int i = 1; i <= scoring_item_count; i++) {
            param1 = "s_item_cond_sel_" + i;
            param2 = "s_item_id_" + i;

            var = req.getParameter(param1);
            if (var != null && var.length() > 0) {
                sel = var;
            } else {
                sel = "";
            }

            var = req.getParameter(param2);
            if (var != null && var.length() > 0) {
                try {
                    cmt_id = Long.parseLong(var);
                } catch (NumberFormatException e) {
                    cmt_id = 0;
                }
            } else {
                cmt_id = 0;
            }

            if (cmt_id != 0) {
                DbCourseMeasurement cmt = new DbCourseMeasurement();
                cmt = new DbCourseMeasurement();
                cmt.setCmt_id(cmt_id);
                cmt.setCmt_status_desc_option(sel);
                cmtScoringVec.addElement(cmt);
            }
        }
    }
    
    public void getRunCondRequest() throws cwException{
        
        this.getCcrParam();
        
        var = req.getParameter("online_item_count");
        if(var != null && var.length() > 0){
            try{
                online_item_count = Integer.parseInt(var);
            }catch(NumberFormatException e){
                online_item_count = 0;
            }
        }else{
            online_item_count = 0;
        }
        
        String temp = req.getParameter("recal_box");
        recal = "";
        recal_date = "";
        if (temp != null && temp.equalsIgnoreCase("YES")) {
        	recal = "on";
            if (req.getParameter("recal_date").equalsIgnoreCase("YES")) {
                recal_date = "on";
            }else {
            	recal_date = "off";
            }
        }
               
        String param1, param2, param3, param4, param5, param6;
        String desc, sel, cmr_status;
        long cmt_id, cmr_id, res_id;
        cmtOnlineVec = new Vector();
        for(int i = 1; i <= online_item_count; i++){
            param1 = "o_item_mod_sel_" + i;
            param2 = "o_item_mod_sel_" + i;
            param3 = "o_cmt_id_" + i;
            param4 = "o_cmr_id_" + i;
            param5 = "o_item_mod_sel_" + i;   
            param6 = "mod_pre_" + i + "_status";

            var = req.getParameter(param1);
            if(var != null && var.length() > 0){
                desc = unicode(var);
            }else{
                desc = "";
            }
            
            var = req.getParameter(param2);
            if(var != null && var.length() > 0){
                sel = var; 
            }else{
                sel = "";                   
            }
            
            var = req.getParameter(param3);
            if(var != null && var.length() > 0){
                try{
                    cmt_id = Long.parseLong(var);
                }catch(NumberFormatException e){
                    cmt_id = 0;
                }
            }else{
                cmt_id = 0;
            }
            
            var = req.getParameter(param4);
            if(var != null && var.length() > 0){
                try{
                    cmr_id = Long.parseLong(var);
                }catch(NumberFormatException e){
                    cmr_id = 0;
                }
            }else{
                cmr_id = 0;
            }
            
            var = req.getParameter(param5);
            if(var != null && var.length() > 0){
                try{
                    res_id = Long.parseLong(var);
                }catch(NumberFormatException e){
                    res_id = 0;
                }
            }else{
                res_id = 0;
            }
            
			var = req.getParameter(param6);
			if (var != null && var.length() > 0) {
				cmr_status = var;
			} else {
				cmr_status = "";
			}
			
            if((desc != null && desc.length() > 0) || cmt_id > 0){
                Hashtable hashcm = new Hashtable();
                hashcm.put("cmt_title", desc);
                hashcm.put("cmt_id", new Long(cmt_id));
                hashcm.put("cmr_id", new Long(cmr_id));
                hashcm.put("status_desc_option", sel);
                hashcm.put("ccr_id", new Long(ccr_id));
                hashcm.put("res_id", new Long(res_id));
                hashcm.put("cmr_status", cmr_status);
                cmtOnlineVec.addElement(hashcm);
            }
        }
        
        var = req.getParameter("scoring_item_count");
        if(var != null && var.length() > 0){
            try{
                scoring_item_count = Integer.parseInt(var);
            }catch(NumberFormatException e){
                scoring_item_count = 0;
            }
        }else{
            scoring_item_count = 0;
        }
        
        cmtScoringVec = new Vector();
        for(int i = 1; i <= scoring_item_count; i++){
            param1 = "s_item_cond_sel_" + i;
            param2 = "s_cmt_id_" + i;
            param3 = "s_cmr_id_" + i;
            
            var = req.getParameter(param1);
            if(var != null && var.length() > 0){
                sel = var;
            }else{
                sel = "";
            }
            
            var = req.getParameter(param2);
            if(var != null && var.length() > 0){
                try{
                    cmt_id = Long.parseLong(var);
                }catch(NumberFormatException e){
                    cmt_id = 0;
                }
            }else{
                cmt_id = 0;
            }
            
            var = req.getParameter(param3);
            if(var != null && var.length() > 0){
                try{
                    cmr_id = Long.parseLong(var);
                }catch(NumberFormatException e){
                    cmr_id = 0;
                }
            }else{
                cmr_id = 0;
            }
            
            if(cmt_id != 0){
                Hashtable hashcm = new Hashtable();
                hashcm.put("cmt_id", new Long(cmt_id));
                hashcm.put("cmr_id", new Long(cmr_id));
                hashcm.put("status_desc_option", sel);                
                cmtScoringVec.addElement(hashcm);
            }
        }
    }
}