package com.cwn.wizbank.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.UserPosition;
import com.cwn.wizbank.entity.UserPositionLrnItem;
import com.cwn.wizbank.entity.UserPositionLrnMap;
import com.cwn.wizbank.persistence.UserPositionMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.Page;

/**
 * service 实现
 */
@Service
public class UserPositionService extends BaseService<UserPosition> {

	@Autowired
	public UserPositionMapper userPositionMapper;
	@Autowired
	public UserPositionLrnMapService userPositionLrnMapService;
	@Autowired
	public UserPositionLrnItemService userPositionLrnItemService;
	@Autowired
	public UserPositionRelationService userPositionRelationService;
	@Autowired
	public AcRoleService acRoleService;
	public void addUserPositionMapAndItem(WizbiniLoader wizbini, loginProfile prof, UserPositionLrnMap userPositionLrnMap,
			MultipartFile image,String qid,String imgurl,int image_radio) {
		String url = "";
		boolean flag = true;
		userPositionLrnMap.setUpm_update_usr_id(prof.usr_ent_id);
		userPositionLrnMap.setUpm_create_usr_id(prof.usr_ent_id);
		userPositionLrnMap.setUpm_tcr_id(prof.my_top_tc_id);
		userPositionLrnMap.setUpm_upt_id(userPositionLrnMap.getUpt_id());
		String upm_img = System.currentTimeMillis() + "";
		if (image_radio == 2) {
			if (image != null) {
				String filename = image.getOriginalFilename();
				String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				upm_img += "." + type;

			} else {
				flag = false;
			}
		} else {
			
				
			imgurl = (imgurl.replace("/wizbank", "")).replaceAll("/", "\\\\");
			
			url = wizbini.getWebDocRoot() + imgurl;

			try {
				File file = new File(url);
				String filename = file.getName();
				String filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				upm_img += "." + filetype;
			} catch (Exception e) {
				flag = false;
			}

		}
		userPositionLrnMap.setUpm_img(upm_img);
		if (flag) {
			userPositionLrnMapService.add(userPositionLrnMap);
			long upm_id = userPositionLrnMap.getUpm_id();
			if (image_radio == 2) {

				if (image != null) {
					String saveDirPath = wizbini.getFileUploadPositionDirAbs() + dbUtils.SLASH + upm_id;
					dbUtils.delFiles(saveDirPath);
					File saveDir = new File(saveDirPath);
					if (!saveDir.exists()) {
						saveDir.mkdirs();
					}

					File targetFile = new File(saveDirPath, upm_img);
					try {
						image.transferTo(targetFile);
					} catch (Exception e) {
						CommonLog.error(e.getMessage(),e);
					}
				}
			} else {
				File file = new File(url);
				String basePath = wizbini.getFileUploadPositionDirAbs() + File.separator + upm_id + File.separator;
				File distPath = new File(basePath);
				if (!distPath.exists()) {
					distPath.mkdirs();
				}

				File distFile = new File(distPath, upm_img);
				/*if (distFile.exists()) {
					distFile.delete();
				}*/
				fileChannelCopy(file, distFile);
			}

			String[] courses = qid.split("\\~");
			for (int i = 0; i < courses.length; i++) {
				UserPositionLrnItem item = new UserPositionLrnItem();
				item.setUpi_upm_id(upm_id);
				item.setUpi_itm_id(Long.parseLong(courses[i]));
				userPositionLrnItemService.add(item);
			}
		}

	}

	public void add(UserPosition userPosition) {
		userPosition.setPfs_update_time(getDate());
		userPositionMapper.add(userPosition);
	}


	public void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
	}

	public void updateUserPositionMapAndItem(WizbiniLoader wizbini, loginProfile prof, UserPositionLrnMap userPositionLrnMap,
			MultipartFile image,String qid,String imgurl,int image_radio) {
		UserPositionLrnMap lrnMap = userPositionLrnMapService.get(userPositionLrnMap.getUpm_id());
		lrnMap.setUpm_upt_id(userPositionLrnMap.getUpt_id());
		lrnMap.setUpm_seq_no(userPositionLrnMap.getUpm_seq_no());
		String upmImg = System.currentTimeMillis() + "";
			String url = "";
			String _Img= "";
			if (image_radio==2) {
				if (image != null) {
					String filename = image.getOriginalFilename();
					String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
					upmImg += "." + type;
					_Img= "h1"+dbUtils.SLASH + upmImg;
					String saveDirPath = wizbini.getFileUploadPositionDirAbs() + dbUtils.SLASH + userPositionLrnMap.getUpm_id()+dbUtils.SLASH+"h1";
					//String saveDirPath = wizbini.getFileUploadPositionDirAbs() + dbUtils.SLASH + userPositionLrnMap.getUpm_id();
					dbUtils.delFiles(saveDirPath);
					File saveDir = new File(saveDirPath);
					if (!saveDir.exists()) {
						saveDir.mkdirs();
					}

					File targetFile = new File(saveDirPath, upmImg);
					try {
						image.transferTo(targetFile);
					} catch (Exception e) {
						CommonLog.error(e.getMessage(),e);
					}
				}
			} else {
				imgurl = (imgurl.replace("/wizbank", "")).replaceAll("/", "\\\\");
				url = wizbini.getWebDocRoot() + imgurl;
				try {
					File file = new File(url);
					if (file.exists()) {

						String filename = file.getName();
						String filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
						upmImg += "." + filetype;
						_Img= "h1"+dbUtils.SLASH + upmImg;
						String basePath = wizbini.getFileUploadPositionDirAbs() + File.separator + userPositionLrnMap.getUpm_id()+dbUtils.SLASH+"h1"
								+ File.separator;
						File distPath = new File(basePath);
						if (!distPath.exists()) {
							distPath.mkdirs();
						}

						File distFile = new File(distPath, upmImg);
						if (distFile.exists()) {
							distFile.delete();
						}
						fileChannelCopy(file, distFile);
					} else {
                    //文件不存在
					}
				} catch (Exception e) {
				}

		}
		lrnMap.setUpm_img(_Img);
		userPositionLrnMapService.update(lrnMap);
		userPositionLrnItemService.deleteByUpmId(userPositionLrnMap.getUpm_id());

		String[] courses = qid.split("\\~");
		for (int i = 0; i < courses.length; i++) {
			UserPositionLrnItem item = new UserPositionLrnItem();
			item.setUpi_upm_id(userPositionLrnMap.getUpm_id());
			item.setUpi_itm_id(Long.parseLong(courses[i]));
			userPositionLrnItemService.add(item);
		}
	}

	public void publishAndCancel(UserPosition userPosition, long usr_ent_id) {
		UserPositionLrnMap lrnMap = userPositionLrnMapService.get(userPosition.getUpm_id());
		lrnMap.setUpm_status(userPosition.getUpm_status());
		userPositionLrnMapService.update(lrnMap);
	}

	public void deleteUserPositionMapAndItem(long upm_id) {
		userPositionLrnMapService.delete(upm_id);
		userPositionLrnItemService.deleteByUpmId(upm_id);
	}
	public void batDeleteUserPositionMapAndItem(String ids) {
		String [] upmIds=ids.split(",");
		for (String upmId : upmIds) {
			deleteUserPositionMapAndItem(Long.parseLong(upmId));
		}
	}
	public Page<UserPosition> getPositionList(Page<UserPosition> page, long upt_id, long upc_id) {
		page.getParams().put("upt_id", upt_id);
		page.getParams().put("upc_id", upc_id);
		userPositionMapper.getPositionList(page);
		return page;
	}
	public UserPosition getByMapId(long upm_id){
		return userPositionMapper.getByMapId(upm_id);
	}
	public int batchdelete(String ids){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ids", ids);
		return userPositionMapper.batchdelete(map);
	}
	public void update(UserPosition userPosition,long usr_ent_id){
		UserPosition positon=get(userPosition.getUpt_id());
		userPosition.setUpt_tcr_id(positon.getUpt_tcr_id());
		userPosition.setPfs_update_time(getDate());
		userPosition.setPfs_update_usr_id(usr_ent_id);
		userPositionMapper.update(userPosition);
	}
	public int getCountById(String id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upt_upc_id", id);
		return userPositionMapper.getCountById(map);
	}
	public List<UserPosition> getList(long upt_upc_id,String ids){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upt_upc_id", upt_upc_id);
		map.put("ids", ids);
		return userPositionMapper.getList(map);
	}
	public boolean isExistTitle(String upt_title,long old_id,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upt_title", upt_title);
		map.put("old_id", old_id);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return userPositionMapper.isExistFormProp(map);
	}
	public boolean isExistCode(String upt_code,long old_id,boolean isTcIndependent,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upt_code", upt_code);
		map.put("old_id", old_id);
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return userPositionMapper.isExistFormProp(map);
	}
	public Page<UserPosition> getPositionMapPage(Page<UserPosition> page, long upt_id, long upc_id){
		page.getParams().put("upt_id", upt_id);
		page.getParams().put("upc_id", upc_id);
		userPositionMapper.getPositionMapPage(page);
		return page;
	}
	public void batchDeleteAndRelation(String ids){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ids", ids);
		userPositionRelationService.batchDel(ids);
		userPositionMapper.batchdelete(map);
	}
}