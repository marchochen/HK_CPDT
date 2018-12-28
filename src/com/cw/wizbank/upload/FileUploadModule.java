/*
 * FileUploadModule.java
 * 
 * Created on Aug 29, 2011
 */
package com.cw.wizbank.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * 
 * @author elvea
 */
public class FileUploadModule extends ServletModule {
	public void process() throws SQLException, IOException, cwException {
		FileUploadReqParam urlp = new FileUploadReqParam(request, clientEnc, static_env.ENCODING);

		if (bMultipart) {
			urlp.setMultiPart(multi);
		}

		urlp.common();

		try {
			HttpSession sess = request.getSession(false);
			if (sess == null || prof == null) {
				response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
			} else {
				if (urlp.cmd.equalsIgnoreCase("upload_file")) {
					urlp.upload();

					String filename = urlp.imgFile;
					File targetFile = new File(wizbini.getFileEditorDirAbs(), filename);
					File sourceFile = new File(tmpUploadPath, filename);
					File targetDir = new File(wizbini.getFileEditorDirAbs());
					if (!targetDir.exists()) {
						targetDir.mkdirs();
					}
					dbUtils.copyFile(sourceFile, targetFile);

					String fileFullPath = cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl()) + filename;

					JSONObject obj = new JSONObject();
					obj.put("error", 0);
					obj.put("url", fileFullPath);
					out.println(obj.toString());
					return;
				} else if (urlp.cmd.equalsIgnoreCase("upload_sound")) {
					String filename = new Date().getTime() + ".wav";
					File f = new File("D:/", filename);
					ServletInputStream in = request.getInputStream();
					OutputStream os = new FileOutputStream(f);
					int b = 0;
					while (b != -1) {
						in.available();
						b = in.read();
						if (b != -1) {
							os.write(b);
						}
					}
					os.close();
					CommonLog.debug(filename);
					out.print(filename);
					return;
				}
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException(e.getMessage());
		}
	}
}