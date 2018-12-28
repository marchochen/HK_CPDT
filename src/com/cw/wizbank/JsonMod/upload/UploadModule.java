package com.cw.wizbank.JsonMod.upload;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.UploadListener;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class UploadModule extends ServletModule{
    UploadModuleParam modParam;
    public UploadModule() {
        super();
        modParam = new UploadModuleParam();
        param = modParam;
    }
    
    public void process() throws SQLException, IOException, cwException {
        try {
                if(this.prof == null || this.prof.usr_ent_id == 0){ // 若还是未登录
                    throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
                } else {
                    if (modParam.getCmd().equalsIgnoreCase("get_status")) {
                        UploadListener.FileUploadStats stats = (UploadListener.FileUploadStats) sess.getAttribute("FILE_UPLOAD_STATS");
                        if (stats != null) {
                            stats.calSpeed();
                        }
                        hasMetaAndSkin = false;
                        resultJson.put("upload", stats);
                    } else if (modParam.getCmd().equalsIgnoreCase("finish")) {
                        sess.removeAttribute("FILE_UPLOAD_STATS");
                        sess.removeAttribute("FILE_UPLOAD_LISTENER");
                        this.param = null;
                        return;
                    } else if (modParam.getCmd().equalsIgnoreCase("show_sysmsg")) {
                        cwSysMessage msg = null;
                        if ("GEN007".equalsIgnoreCase(modParam.getSysmsg_id())) {
                            msg = new cwSysMessage("GEN007", new Integer(static_env.INI_MAX_UPLOAD_SIZE).toString());
                        } else {
                            msg = new cwSysMessage(modParam.getSysmsg_id());
                        }
                        //this.sysMsg = getErrorMsg(modParam.getSysmsg_id(), modParam.getUrl_success());
                        if (modParam.getCos_id() > 0 && modParam.getNew_res_id() > 0) {
                            msgBox(MSG_STATUS, msg,
                                    modParam.getUrl_success(), response.getWriter(), modParam.getCos_id(), modParam.getNew_res_id(),
                                    prof, con, "INSERT");
                        } else {
                        	String status = request.getParameter("status");
                        	String resultTitle;
                        	if(MSG_ERROR.equals(status)){
                        		resultTitle = MSG_ERROR;
                        	}else{
                        		resultTitle = MSG_STATUS;
                        	}
                            msgBox(resultTitle, msg, modParam.getUrl_success(), out);
                        }
                        this.param = null;
                    }
                }
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e);
        }
    }
}
