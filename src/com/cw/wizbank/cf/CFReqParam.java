package com.cw.wizbank.cf;

import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.utils.CommonLog;

public class CFReqParam extends ReqParam {
    private static final boolean DEBUG        = true;
    public    final static String    DELIMITER    = "~";

    /**
     * Pagination and sorting parameters
     */
    public Timestamp    pagetime = null;
    public int            page;
    public int            page_size;

    // for login profile
    loginProfile prof = null;

    // for certificate
    public String status;
    public String order_by;
    public String sort_by;
    public int ctf_id;

    public int cfn_ent_id;
    public String ctf_id_lst[];
    public String cfn_ent_id_lst[];
    public String cfn_ctf_id_lst[];

    public CFReqParam(ServletRequest inReq,    String clientEnc_, String encoding_)
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
     *
     * @throws cwException
     */
    public void    getCtf() throws    cwException    {
        status = unicode(getStringParameter("status"));
        page_size = getIntParameter("page_size");
        page = getIntParameter("page");
        order_by = unicode(getStringParameter("order_by"));
        sort_by = unicode(getStringParameter("sort_by"));
    }
    public void    getCfn() throws    cwException    {
        status = unicode(getStringParameter("status"));
        page_size = getIntParameter("page_size");
        page = getIntParameter("page");
        order_by = unicode(getStringParameter("order_by"));
        sort_by = unicode(getStringParameter("sort_by"));
        ctf_id = getIntParameter("ctf_id");
    }
    public void    updCtf() throws    cwException    {
        status = unicode(getStringParameter("status"));
        ctf_id_lst = getStrArrayParameter("ctf_id_lst",    DELIMITER);

    }
    public void    updCfn() throws    cwException    {
        status = unicode(getStringParameter("status"));
        cfn_ctf_id_lst = getStrArrayParameter("cfn_ctf_id_lst",    DELIMITER);
        cfn_ent_id_lst = getStrArrayParameter("cfn_ent_id_lst", DELIMITER);

    }

}