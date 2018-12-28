package com.cw.wizbank.JsonMod.courseware;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.UploadListener;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * 用于处理与公告有关的信息
 */
public class CoursewareModule extends ServletModule {
    CoursewareModuleParam modParam;

    public CoursewareModule() {
        super();
        modParam = new CoursewareModuleParam();
        param = modParam;
    }

    public void process() throws SQLException, IOException, cwException {
        try {
            if (this.prof == null || this.prof.usr_ent_id == 0) {
                throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
            } else {
                if (modParam.getCmd().equalsIgnoreCase("ins_mod_aicc")) {
                    Courseware cosw = new Courseware();
                    cosw.insAiccMod(con, sess, prof, wizbini, static_env, out, modParam, tmpUploadPath);
                    con.commit();
                } else if(modParam.getCmd().equalsIgnoreCase("ins_res_aicc")) {
                    Courseware cosw = new Courseware();
                    cosw.insAiccRes(con, sess, prof, wizbini, static_env, out, modParam, tmpUploadPath);
                    con.commit();
                } else if (modParam.getCmd().equalsIgnoreCase("upd_res_aicc")){
                    Courseware cosw = new Courseware();
                    cosw.updAiccRes(con, sess, prof, wizbini, static_env, out, modParam, tmpUploadPath);
                    con.commit();
                } else if (modParam.getCmd().equalsIgnoreCase("ins_mod_scorm")
                        || modParam.getCmd().equalsIgnoreCase("ins_res_scorm")
                        || modParam.getCmd().equalsIgnoreCase("upd_res_scorm")){
                    Courseware cosw = new Courseware();
                    cosw.insScorm(con, sess, prof, wizbini, static_env, response, modParam, tmpUploadPath);
                    con.commit();
                } else if (modParam.getCmd().equalsIgnoreCase("upload_offline_pkg")){
                    Courseware cosw = new Courseware();
                    cosw.uploadOfflinePackage(con, sess, wizbini, modParam, tmpUploadPath);
                    con.commit();
                } else {
                    throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
                }
            }
        } catch (Exception e) {
            UploadListener listener = (UploadListener) sess.getAttribute("FILE_UPLOAD_LISTENER");
            if (listener != null) {
                listener.error("MOD009");
            }
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage(), e);
        }
    }
}
