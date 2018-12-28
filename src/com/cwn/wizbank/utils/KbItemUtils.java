package com.cwn.wizbank.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class KbItemUtils {

	public static String getFileName(WizbiniLoader wizbini, loginProfile prof, MultipartFile file) throws cwException, IOException {
		String filename = null;
		String path = null;
		String savaDirPath = wizbini.getFileUploadUsrDirAbs() + dbUtils.SLASH + prof.usr_ent_id;
		if (file != null && !file.isEmpty()) {
			filename = file.getOriginalFilename();
			dbUtils.delFiles(savaDirPath);

			File saveDir = new File(savaDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}
			java.util.Calendar rightNow = java.util.Calendar.getInstance();

			String _filename = Long.toString((rightNow.getTime()).getTime());
			String _type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			_filename = _filename + "." + _type;

			File targetFile = new File(savaDirPath, _filename);
			try {
				file.transferTo(targetFile);
			} catch (IllegalStateException e) {
				CommonLog.error(e.getMessage(),e);
			} catch (IOException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
		path = savaDirPath.substring(savaDirPath.indexOf("www"+cwUtils.SLASH) + 4);
		File Dir = new File(savaDirPath);
		File[] files = Dir.listFiles();
		if (files != null && files.length > 0) {
			filename = files[0].getName();
		}
		String saveFile = savaDirPath + dbUtils.SLASH + filename;
		cwUtils.resizeImage(saveFile, wizbini.getUsrImageWidth(), wizbini.getUsrImageHeight());
		path = path + dbUtils.SLASH + filename;
		path = path.replace("\\", "/");
		return path;
	}

}
