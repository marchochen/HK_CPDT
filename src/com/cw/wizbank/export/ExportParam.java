/*
 * Created on 2006-2-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.export;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.oreilly.servlet.ServletUtils;

public class ExportParam extends ReqParam {
    public final static String DELIMITER = "~";
    public String[] res_tnd_id_lst;
    public String window_name;
    public String search_owner;
    public Timestamp search_update_time_before;
    public Timestamp search_update_time_after;
    public Timestamp search_create_time_before;
    public Timestamp search_create_time_after;
    public String[] difficulty;
    public String search_status;
    public String search_desc;
    public String search_title;
    public String[] search_title_lst;
    public String[] search_desc_lst;
    public long search_id_before;
    public long search_id_after;
    public long search_id;
    public boolean include_sub;
    public String que_type;

    public ExportParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;
            super.common();
            String var = null;
    }

    public void exportQue() throws UnsupportedEncodingException, cwException {
        que_type = getStringParameter("s_que_type");
        include_sub = getBooleanParameter("s_include_sub");
        search_id = getLongParameter("search_id");
        search_id_after = getLongParameter("search_id_after");
        search_id_before = getLongParameter("search_id_before");
        
        search_title = getStringParameter("search_title");
        if (search_title != null && search_title.length() > 0) {
            search_title_lst = ServletUtils.split(dbUtils.unicodeFrom(search_title, clientEnc, encoding), " " );
        }
        search_desc = getStringParameter("search_desc");
        if (search_desc != null && search_desc.length() > 0) {
            search_desc_lst = ServletUtils.split(dbUtils.unicodeFrom(search_desc, clientEnc, encoding), " " );
        }
        search_status = getStringParameter("search_status");
        difficulty = getStrArrayParameter("search_diff_lst", DELIMITER);
        search_create_time_after = getTimestampParameter("search_create_time_after");
        search_create_time_before = getTimestampParameter("search_create_time_before");
        search_update_time_after = getTimestampParameter("search_update_time_after");
        search_update_time_before = getTimestampParameter("search_update_time_before");
        search_owner = dbUtils.unicodeFrom(getStringParameter("search_owner"), clientEnc, encoding);
        window_name = getStringParameter("window_name");
        res_tnd_id_lst = getStrArrayParameter("s_res_tnd_id", DELIMITER);
    }
}
