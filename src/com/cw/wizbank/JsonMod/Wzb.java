package com.cw.wizbank.JsonMod;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.user.UserModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class Wzb extends ServletModule {
    
    public static final String SAVE_SEARCH = "save_search";
    
    public static final String KEY_SEARCH_ID_PREFIX = "SEARCH_ID_";
    
    public static final String PARAM_SEARCH_ID = "search_id";
    
    WzbParam modParam;
    
    public Wzb() {
        super();
        modParam = new WzbParam();
        param = modParam;
    }

    public void process() throws SQLException, IOException, cwException {
		if (prof == null || prof.usr_ent_id == 0) {// not login
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			if (modParam.getCmd().equalsIgnoreCase(SAVE_SEARCH)) {
				// save search Criteria and get search_id
				String keySearchId = setSrhCriteria(sess);
				// redirect url
				redirectUrl = constructUrl(modParam.getUrl_success(), keySearchId);
			} else if (modParam.getCmd().equalsIgnoreCase("get_prof")) {
				resultJson.put(UserModule.PROF_ATTR_LST, wizbini.getUsrMgtJson(prof.root_id, prof.cur_lan));
			} else {
				throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
			}
		}
	}
    
    private String setSrhCriteria(HttpSession sess) throws SQLException {
    	HashMap paramMap = new HashMap();
		paramMap.putAll((Map)request.getParameterMap());
        long curTime = cwSQL.getTime(con).getTime();
        String keySearchId = KEY_SEARCH_ID_PREFIX + curTime;
        //save map of parameters in session 
        sess.setAttribute(keySearchId, paramMap);
        
        return keySearchId;
    }
    
    private String constructUrl(String url, String keySearchId) throws cwException {
        String successUrl = url;
        successUrl = cwUtils.getRealPath(request, successUrl);
        
        // add param search_id to url
        String paramStr = "";
        int queryIndex = successUrl.indexOf("?");
        if(queryIndex == -1) {
            paramStr += "?";
        } else {
            paramStr += "&";
        }
        paramStr += PARAM_SEARCH_ID +"=" + keySearchId;
        successUrl += paramStr;
        
        return successUrl;
    }
    
}
