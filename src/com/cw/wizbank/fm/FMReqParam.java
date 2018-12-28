package    com.cw.wizbank.fm;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.utils.CommonLog;

public class FMReqParam extends ReqParam {
    private static final boolean DEBUG        = true;
    public    final static String    DELIMITER    = "~";

    /**
     * Pagination and sorting parameters
     */
    public Timestamp    pagetime = null;
    public int            cur_page;
    public int            pagesize;

    // for login profile
    loginProfile prof = null;

    // for show    reservation    details    with linked    fs
    //variables    for    db manipulate
    public int rsv_id;
    public String rsv_purpose;
    public String rsv_desc;
    public String rsv_ent_id;
    public int rsv_participant_no;
    public int rsv_main_fac_id;

    public String rsv_remark;
    public String rsv_person;
    public String rsv_status;

    public Timestamp rsv_cancel_timestamp;
    public String rsv_cancel_usr_id;
    public String rsv_cancel_reason;

    public int rsv_owner_ent_id;
    public Timestamp rsv_create_timestamp;
    public String rsv_create_usr_id;
    public Timestamp rsv_upd_timestamp;
    public String rsv_upd_usr_id;
    public String rsv_upd_time;

    // for facilitySchedule
    public int fsh_fac_id;
    public Timestamp fsh_start_time;
    public Timestamp fsh_upd_time;
    // for check_facilitySchedule conflict
    public String fac_act;

    // for facilityScheduleList
    public String[]    fsh_fac_id_lst;
    public String[]    fsh_start_time_lst;
    public String[]    fsh_upd_timestamp_lst;

    // for Cancel,facilitySchedule and reservation
    public String cancel_type;
    public String cancel_reason;

    // for facility thumbnail
    private boolean bFileUploaded = false;

    public FMReqParam(ServletRequest inReq,    String clientEnc_, String encoding_)
        throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_;
        this.encoding =    encoding_;

        if (this.DEBUG) {
            //Print    submited param
            Enumeration    enumeration = req.getParameterNames();
            while( enumeration.hasMoreElements() )    {
                String name    = (String) enumeration.nextElement();
                String[] values    = req.getParameterValues(name);
                if(    values != null )
                    for(int    i=0; i<values.length; i++)
                    	CommonLog.debug(name    + "    (" + i + "):" +    values[i]);
            }
        }
    }

    /**
     * set the login profile
     */
    public void setProfile(loginProfile profile) throws cwException {
        this.prof = profile;
    }

    /**
     * get the parameter of current user
     */
    public void    getCurrentUser(Hashtable param) throws cwException {
        param.put("user_id", prof.usr_id);
        param.put("user_ent_id", new Integer(new Long(prof.usr_ent_id).intValue()));
        param.put("owner_ent_id", new Integer(new Long(prof.root_ent_id).intValue()));

        return;
    }

    /**
     * get the parameter for facility insertion
     */
    public void    getFacilityInsterion(Hashtable parameter) throws cwException {
        if (parameter == null)
            parameter = new Hashtable();

        this.getCurrentUser(parameter);
        parameter.put("fac_type", new Integer(getIntParameter("fac_type")));
        this.getFacilityInfo(parameter);

        return;
    }

    /**
     * get the parameter for facility update
     */
    public void    getFacilityUpdate(Hashtable parameter) throws cwException {
        if (parameter == null)
            parameter = new Hashtable();

        this.getCurrentUser(parameter);
        parameter.put("fac_id", new Integer(getIntParameter("fac_id")));
        parameter.put("fac_upd_timestamp", getTimestampParameter("fac_upd_timestamp"));
        this.getFacilityInfo(parameter);

        return;
    }

    /**
     * get the parameter for facility deletion
     * 
     */
    public void    getFacilityDeletion(Hashtable parameter) throws cwException {
        if (parameter == null)
            parameter = new Hashtable();

        this.getCurrentUser(parameter);
        parameter.put("fac_id", new Integer(getIntParameter("fac_id")));
        
        String reqTime = req.getParameter("fac_upd_timestamp");
        if(reqTime.length() == 22){ reqTime += "0"; }else if(reqTime.length() == 21){ reqTime += "00"; }else if(reqTime.length() == 19){ reqTime += ".000"; }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp time = null;
		try { time = new Timestamp(format.parse(reqTime).getTime()); } catch (ParseException e) {  
			CommonLog.error(e.getMessage(),e);
		}
        parameter.put("fac_upd_timestamp", time);

        return;
    }

    /**
     * get the parameter for facility getting
     */
    public void    getFacilityGet(Hashtable parameter) throws cwException {
        if (parameter == null)
            parameter = new Hashtable();

        this.getCurrentUser(parameter);
        parameter.put("fac_id", new Integer(getIntParameter("fac_id")));

        return;
    }

    /**
     * get the parameter for facility list
     */
    public void    getFacilityList(Hashtable parameter) throws cwException {
        if (parameter == null)
            parameter = new Hashtable();

        this.getCurrentUser(parameter);
        try{
            parameter.put("fac_type", new Integer(getIntParameter("fac_type")));
        }catch(Exception e){
            parameter.put("fac_type", java.lang.Integer.MIN_VALUE);
        }
    }

    /**
     * get the parameter for reservation record list
     */
    public void    getRsvRecordList(Hashtable parameter) throws cwException {
        if (parameter == null)
            parameter = new Hashtable();

        this.getCurrentUser(parameter);
        /**
         * parameter
         * add the key with prefix "param_" for FE
         */
        parameter.put("start_date", getTimestampParameter("start_date"));
        parameter.put("end_date", getTimestampParameter("end_date"));
        if (getStringParameter("own_type") != null)
            parameter.put("own_type", getStringParameter("own_type"));
        // fac_id list
        parameter.put("fac_id", getStrArrayParameter("fac_id", this.DELIMITER));
        parameter.put("param_fac_id", getStringParameter("fac_id"));
        // status list
        if (getStrArrayParameter("status", this.DELIMITER) != null) {
            parameter.put("status", getStrArrayParameter("status", this.DELIMITER));
            parameter.put("param_status", getStringParameter("status"));
        }
        // download the reservation record
        if (getStringParameter("download") != null) {
            parameter.put("download", new Integer(getIntParameter("download")));
            parameter.put("filename", getStringParameter("filename"));
        }
    }

    /**
     * get the parameter for calendar
     */
    public void    getCalendar(Hashtable parameter) throws cwException {
        if (parameter == null)
            parameter = new Hashtable();

        this.getCurrentUser(parameter);
        parameter.put("start_date", getTimestampParameter("start_date"));
        parameter.put("end_date", getTimestampParameter("end_date"));
        // fac_id list
        if (getStrArrayParameter("fac_id", this.DELIMITER) != null)
            parameter.put("fac_id", getStrArrayParameter("fac_id", this.DELIMITER));
        if (getStrArrayParameter("ext_fac_id", this.DELIMITER) != null)
            parameter.put("ext_fac_id", getStrArrayParameter("ext_fac_id", this.DELIMITER));
    }
    /**
     * get reservation ID
     * @throws cwException
     */
    public void    getRsvID() throws cwException {
        rsv_id = getIntParameter("rsv_id");
    }
    /**
     * get reservation update timestamp
     * @throws cwException
     */
    public void    getRsvUpdTime()    throws cwException {
        rsv_upd_time =    getStringParameter("rsv_upd_timestamp");
        rsv_upd_timestamp =    getTimestampParameter("rsv_upd_timestamp");
    }
    /**
     * get facilitySchedule list
     * @throws cwException
     */
    public void    getFshList() throws    cwException    {
        fsh_fac_id_lst = getStrArrayParameter("fsh_fac_id",    DELIMITER);
        fsh_start_time_lst = getStrArrayParameter("fsh_start_time",    DELIMITER);
        fsh_upd_timestamp_lst =    getStrArrayParameter("fsh_upd_timestamp", DELIMITER);

    }
    /**
     * get cancel information
     * @throws cwException
     */

    public void    getCancelType()    throws cwException {
        //
        cancel_type    = unicode(getStringParameter("cancel_type"));
        cancel_reason =    unicode(getStringParameter("cancel_reason"));
    }
    /**
     * get add fsh action:reserve|search|check;
     * for check facility availability and conflict
     * @throws cwException
     */
    public void    getFshAct()    throws cwException {
        fac_act    = getStringParameter("fac_act");
    }

    private void getFacilityInfo(Hashtable parameter) throws cwException {
        parameter.put("fac_title", unicode(getStringParameter("fac_title").trim()));
        parameter.put("status", getStringParameter("status"));    // available/unavailable
        if (getStringParameter("fac_desc") != null)
            parameter.put("fac_desc", unicode(getStringParameter("fac_desc").trim()));
        if (getStringParameter("fac_remarks") != null)
            parameter.put("fac_remarks", unicode(getStringParameter("fac_remarks").trim()));

        // update for facility thumbnail
//        if (getStringParameter("fac_url") != null)
//            parameter.put("fac_url", unicode(getStringParameter("fac_url").trim()));
        if (this.getUploadedFileName("fac_url") != null)
            parameter.put("fac_url", unicode(this.getUploadedFileName("fac_url").trim()));
        else
            if (getStringParameter("fac_url") != null)
                parameter.put("fac_url", unicode(getStringParameter("fac_url").trim()));

        if (getStringParameter("fac_url_type") != null)
            parameter.put("fac_url_type", unicode(getStringParameter("fac_url_type")));
        if (getStringParameter("fac_add_xml") != null)
            parameter.put("fac_add_xml", unicode(getStringParameter("fac_add_xml").trim()));
        
        if (getDoubleParameter("fac_fee") > 0) {
        	parameter.put("fac_fee", new Double(getDoubleParameter("fac_fee")));
        }

    }

    /**
     * for facility thumbnail
     * @param bFileUploaded_:   whether the uploaded file is ok
     */
    public void setFileUploaded(boolean bFileUploaded_) {
        this.bFileUploaded = bFileUploaded_;
        return;
    }

    protected String getUploadedFileName(String paraName) throws cwException {
        String urlName = null;
        Enumeration files = multi.getFileNames();
        if (files.hasMoreElements()) {
            String name = (String)files.nextElement();
            urlName = multi.getFilesystemName(name);
        }
//        try {
//            if (bMultipart && bFileUploaded && multi.getFilesystemName(paraName) != null) {
//                urlName = multi.getFilesystemName(paraName);
//            } else {
//                if (getStringParameter(paraName) != null) {
//                    urlName = dbUtils.unicodeFrom(getStringParameter(paraName),
//                                                  clientEnc, encoding, bMultipart);
//                }
//            }
//        } catch (IOException e) {
//            throw new cwException(e.getMessage());
//        }

        return urlName;
    }

/* DENNIS BEGIN */
    public String action;
    public Timestamp fsh_start_date;
    public Timestamp fsh_end_date;
    public Timestamp fsh_end_time;
    public int[] int_fac_id_lst;
    public Timestamp[] ts_fsh_start_time_lst;
    public Timestamp[] ts_fsh_end_time_lst;
    public Timestamp[] ts_fsh_date_lst;
    public Timestamp[] ts_fsh_upd_timestamp_lst;
    public int int_rsv_ent_id;
    public String[] fsh_status_lst;

    public void updRsv() throws cwException {
        rsv_id = getIntParameter("rsv_id");
        rsv_purpose = unicode(getStringParameter("rsv_purpose"));
        rsv_desc = unicode(getStringParameter("rsv_desc"));
        int_rsv_ent_id = getIntParameter("rsv_ent_id");
        rsv_participant_no = getIntParameter("rsv_participant_no");
        rsv_main_fac_id = getIntParameter("rsv_main_fac_id");
        rsv_upd_timestamp = getTimestampParameter("rsv_upd_timestamp");
        int_fac_id_lst = getIntArrayParameter("fsh_fac_id", DELIMITER);
        ts_fsh_start_time_lst = getTimestampArrayParameter("fsh_start_time", DELIMITER);
        ts_fsh_upd_timestamp_lst = getTimestampArrayParameter("fsh_upd_timestamp", DELIMITER);
        fsh_status_lst = getStrArrayParameter("fsh_status", DELIMITER);
    }

    public void insRsv() throws cwException {
        rsv_purpose = unicode(getStringParameter("rsv_purpose"));
        rsv_desc = unicode(getStringParameter("rsv_desc"));
        int_rsv_ent_id = getIntParameter("rsv_ent_id");
        rsv_participant_no = getIntParameter("rsv_participant_no");
        rsv_main_fac_id = getIntParameter("rsv_main_fac_id");
        int_fac_id_lst = getIntArrayParameter("fsh_fac_id", DELIMITER);
        ts_fsh_start_time_lst = getTimestampArrayParameter("fsh_start_time", DELIMITER);
        ts_fsh_upd_timestamp_lst = getTimestampArrayParameter("fsh_upd_timestamp", DELIMITER);
        fsh_status_lst = getStrArrayParameter("fsh_status", DELIMITER);
    }

    public void checkFshConflict() throws cwException {
        action = getStringParameter("act");
        int_fac_id_lst = getIntArrayParameter("fsh_fac_id", DELIMITER);
        ts_fsh_date_lst = getTimestampArrayParameter("fsh_date", DELIMITER);
        ts_fsh_start_time_lst = getTimestampArrayParameter("fsh_start_time", DELIMITER);
        ts_fsh_end_time_lst = getTimestampArrayParameter("fsh_end_time", DELIMITER);
        rsv_id = getIntParameter("rsv_id");
        CommonLog.debug(" ReqParam: rsv_id = " + rsv_id);
        return;
    }

    public void checkFacAvail() throws cwException {
        action = getStringParameter("act");
        int_fac_id_lst = getIntArrayParameter("fac_id", DELIMITER);
        fsh_start_date = getTimestampParameter("start_date");
        fsh_end_date = getTimestampParameter("end_date");
        fsh_start_time = getTimeParameter("start_time");
        fsh_end_time = getTimeParameter("end_time");
        rsv_id = getIntParameter("rsv_id");
        CommonLog.debug(" ReqParam: rsv_id = " + rsv_id);
        return;
    }

    /**
    Get the time HH:MM into a Timestamp by given a garbage date and nano
    */
    private Timestamp getTimeParameter(String paraname) throws cwException {
        String temp_date = "2002-01-01 ";
        String temp_nano = ":000";
        return Timestamp.valueOf(temp_date + getStringParameter(paraname) + temp_nano);
    }

    private Timestamp[] getTimestampArrayParameter(String paraname, String separator) throws cwException {

        String[] strArray = getStrArrayParameter(paraname, separator);
        Timestamp[] tsArray = null;
        if(strArray != null) {
            tsArray = new Timestamp[strArray.length];
            for(int i=0; i<strArray.length; i++) {
                tsArray[i] = Timestamp.valueOf(strArray[i]);
            }
        }
        return tsArray;
    }

    private int[] getIntArrayParameter(String paraname, String separator) throws cwException {

        String[] strArray = getStrArrayParameter(paraname, separator);
        int[] intArray = null;
        if(strArray != null) {
            intArray = new int[strArray.length];
            for(int i=0; i<strArray.length; i++) {
                intArray[i] = Integer.parseInt(strArray[i]);
            }
        }
        return intArray;
    }
/* DENNIS END */
}