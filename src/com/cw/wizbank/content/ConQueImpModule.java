package com.cw.wizbank.content;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.upload.ConESQue;
import com.cw.wizbank.upload.ConMCQue;
import com.cw.wizbank.upload.UploadReqParam;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class ConQueImpModule extends ServletModule{
	ContentQueImpReqParam modParam;
	public static final String SESS_UPLOADED_SRC_FILE = "src_file";
	public static final String MOD_NAME = "content";

	public ConQueImpModule() {
		super();
		modParam = new ContentQueImpReqParam();
		param = modParam;
	}

	public void process() throws SQLException, IOException, cwException{
		UploadReqParam urlp = null;
        urlp = new UploadReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();

        PrintWriter out = response.getWriter();
		if (this.prof == null || this.prof.usr_ent_id == 0) { // 若还是未登录
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else {
			if (modParam.getCmd().equalsIgnoreCase("get_upload_res_prep") || modParam.getCmd().equalsIgnoreCase("get_upload_res_prep_xml")) {
                StringBuffer xml = new StringBuffer();
                xml.append("<obj id=\"").append(modParam.getQue_obj_id()).append("\"/>");
                xml.append("<que_type name=\"").append(modParam.getQue_type()).append("\"/>");
                xml.append("<mod_type name=\"").append(modParam.getMod_type()).append("\"/>");
                xml.append("<upload_res>")
                .append("<res_upload_limit>")
                .append("<meta>")
                .append(prof.asXML())
                .append("<tc_enabled>").append(wizbini.cfgTcEnabled).append("</tc_enabled>")
                .append("</meta>")
                .append("</res_upload_limit>")
                .append("</upload_res>");
                resultXml = formatXML(xml.toString(), MOD_NAME);
            }else if( modParam.getCmd().equalsIgnoreCase("upload_que") || urlp.cmd.equalsIgnoreCase("upload_que_xml") ) {
				urlp.uploadQue();
				String xml = null;
				File srcFile = new File(tmpUploadPath, urlp.src_filename);
				sess.setAttribute(SESS_UPLOADED_SRC_FILE, srcFile);
				try{
					if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI) ){
						ConMCQue mcq = new ConMCQue(con, static_env, prof);
						try {
							xml = mcq.uploadQue(srcFile, urlp.upload_desc, urlp.allow_update, wizbini,modParam.getQue_obj_id(),modParam.getMod_type());
						} catch (qdbException e) {
							CommonLog.error(e.getMessage(),e);
						}
					}else if( urlp.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)) {                    	
						ConESQue essay = new ConESQue(con, static_env, prof);
						xml = essay.uploadQue(srcFile, urlp.upload_desc, dbQuestion.QUE_TYPE_ESSAY, urlp.allow_update, wizbini,modParam.getQue_obj_id());
					} 
					con.commit();
				}catch(IOException e) {
					xml = "<invalid_file/>";
				} catch (cwSysMessage e) {
					CommonLog.error(e.getMessage(),e);
					//超过200行提示
					msgBox(MSG_ERROR, e, urlp.url_failure, out);
				}

				xml = formatXML(xml, "Upload");
				if (urlp.cmd.equalsIgnoreCase("upload_que")) {
					generalAsHtml(xml, out, urlp.stylesheet);
				}else {
					static_env.outputXML(out, xml);
				}
				return;
				
			} else if( modParam.getCmd().equalsIgnoreCase("cook_que") || modParam.getCmd().equalsIgnoreCase("cook_que_xml") ) {

                urlp.cookQue();
                DbUploadLog ulg = new DbUploadLog();
                ulg.ulg_id = urlp.ulg_id;
                boolean bExist = ulg.get(con);
                
                try {
					if (!bExist)
					    throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_ULG_RECROD);

					if (!ulg.ulg_status.equals(DbUploadLog.STATUS_PENDING)) {
					    throw new cwSysMessage("GEN000");
					}
				} catch (cwSysMessage e) {
					CommonLog.error(e.getMessage(),e);
				}
                
                String xml = null;
				if (ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI)) {
					ConMCQue mcq = new ConMCQue(con, static_env, prof);
					xml = mcq.save2DB(ulg, prof, wizbini.cfgTcEnabled);                        
				} else if( ulg.ulg_subtype.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)) {
					ConESQue essay = new ConESQue(con, static_env, prof);
					xml = essay.save2DB(ulg, prof, wizbini.cfgTcEnabled);
				} 
                con.commit();
                UploadUtils.saveUploadedFile(static_env, UploadUtils.QUE, ulg.ulg_id, (File)sess.getAttribute(SESS_UPLOADED_SRC_FILE));
                sess.removeAttribute(SESS_UPLOADED_SRC_FILE);
                
                xml = formatXML(xml, "Upload");
                if (urlp.cmd.equalsIgnoreCase("cook_que")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }

                return;
			} else {

				throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
			}
		}
	}
}
