package com.cwn.wizbank.services;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.UserSpecialExpert;
import com.cwn.wizbank.entity.UserSpecialItem;
import com.cwn.wizbank.entity.UserSpecialTopic;
import com.cwn.wizbank.persistence.UserSpecialTopicMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.FileUtils;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.StringUtils;

/**
 * 专题培训service 实现
 */
@Service
public class UserSpecialTopicService extends BaseService<UserSpecialTopic> {
	public static Logger logger = LoggerFactory.getLogger(LabelContent.class);
	@Autowired
	UserSpecialTopicMapper userSpecialTopicMapper;

	@Autowired
	UserSpecialItemService userSpecialItemService;

	@Autowired
	UserSpecialExpertService userSpecialExpertService;

	@Autowired
	AcRoleService acRoleService;
	public void add(UserSpecialTopic userSpecialTopic) {
		userSpecialTopic.setUst_create_time(getDate());
		userSpecialTopic.setUst_update_time(getDate());
		userSpecialTopicMapper.add(userSpecialTopic);
	}

	public void addSpecialTopic(WizbiniLoader wizbini, loginProfile prof, UserSpecialTopic userSpecialTopic,
			MultipartFile image, String qid, String expert_ids, String imgurl, int image_radio) {
		String url = "";
		String _img="";
		boolean flag = true;

		String ust_img = System.currentTimeMillis() + "";
		if (image_radio == 2) {
			if (image != null) {
				String filename = image.getOriginalFilename();
				String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				ust_img += "." + type;
				_img="h2"+dbUtils.SLASH+ust_img;

			} else {
				flag = false;
			}
		} else {

			imgurl = (imgurl.replace("/wizbank", "")).replaceAll("/", "\\\\");
			String os = System.getProperty("os.name");  
			if(!os.toLowerCase().startsWith("win")){  
				imgurl = imgurl.replaceAll("\\\\", "/");
			}
			url = wizbini.getWebDocRoot() + imgurl;
			CommonLog.info("url:"+url);

			try {
				File file = new File(url);
				String filename = file.getName();
				String filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				ust_img += "." + filetype;
				_img="h2"+dbUtils.SLASH+ust_img;
			} catch (Exception e) {
				flag = false;
			}

		}
		userSpecialTopic.setUst_img(_img);
		userSpecialTopic.setUst_tcr_id(prof.my_top_tc_id);
		userSpecialTopic.setUst_create_usr_id(prof.usr_ent_id);
		if (flag) {
			add(userSpecialTopic);
			long ust_id = userSpecialTopic.getUst_id();
			if (image_radio == 2) {

				if (image != null) {
					String saveDirPath = wizbini.getFileUploadPositionDirAbs() + dbUtils.SLASH + ust_id+dbUtils.SLASH+"h2";
					dbUtils.delFiles(saveDirPath);
					File saveDir = new File(saveDirPath);
					if (!saveDir.exists()) {
						saveDir.mkdirs();
					}

					File targetFile = new File(saveDirPath, ust_img);
					try {
						image.transferTo(targetFile);
					} catch (Exception e) {
						CommonLog.error(e.getMessage(),e);
					}
				}
			} else {
				File file = new File(url);
				String basePath = wizbini.getFileUploadPositionDirAbs() + File.separator + ust_id+dbUtils.SLASH+"h2" + File.separator;
				File distPath = new File(basePath);
				if (!distPath.exists()) {
					distPath.mkdirs();
				}

				File distFile = new File(distPath, ust_img);
				if (distFile.exists()) {
					distFile.delete();
				}
				FileUtils.fileChannelCopy(file, distFile);
			}

			String[] courses = qid.split("\\~");
			for (int i = 0; i < courses.length; i++) {
				UserSpecialItem item = new UserSpecialItem();
				item.setUst_utc_id(ust_id);
				item.setUsi_itm_id(Long.parseLong(courses[i]));
				userSpecialItemService.add(item);
			}
			if (!StringUtils.isEmpty(expert_ids)) {
				String[] eptIds = expert_ids.split(",");
				for (int i = 0; i < eptIds.length; i++) {
					UserSpecialExpert expert = new UserSpecialExpert();
					expert.setUse_ent_id(Long.parseLong(eptIds[i]));
					expert.setUse_ust_id(ust_id);
					userSpecialExpertService.add(expert);
				}
			}
		}

	}

	public void updateSpecialTopic(WizbiniLoader wizbini, loginProfile prof, UserSpecialTopic userSpecialTopic,
			MultipartFile image, String qid, String expert_ids, String imgurl, int image_radio) {
		
		long ust_id = userSpecialTopic.getUst_id();
		UserSpecialTopic specialTopic = userSpecialTopicMapper.get(ust_id);
		specialTopic.setUst_title(userSpecialTopic.getUst_title());
		specialTopic.setUst_summary(userSpecialTopic.getUst_summary());
		specialTopic.setUst_content(userSpecialTopic.getUst_content());
		specialTopic.setUst_showindex(userSpecialTopic.getUst_showindex());
		specialTopic.setUst_status(userSpecialTopic.getUst_status());
		specialTopic.setUst_update_usr_id(prof.usr_ent_id);
		specialTopic.setUst_update_time(getDate());
		String url = "";
		String ust_img = System.currentTimeMillis() + "";
		String _Img="";

		if (image_radio==2) {
			if (image != null) {
				String filename = image.getOriginalFilename();
				String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				ust_img += "." + type;
				_Img="h2"+dbUtils.SLASH+ust_img;
				String saveDirPath = wizbini.getFileUploadPositionDirAbs() + dbUtils.SLASH + ust_id+dbUtils.SLASH+"h2";
				//String saveDirPath = wizbini.getFileUploadPositionDirAbs() + dbUtils.SLASH + ust_id;
				dbUtils.delFiles(saveDirPath);
				File saveDir = new File(saveDirPath);
				if (!saveDir.exists()) {
					saveDir.mkdirs();
				}
				File targetFile = new File(saveDirPath, ust_img);
				try {
					image.transferTo(targetFile);
				} catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		} else {
			imgurl = (imgurl.replace("/wizbank", "")).replaceAll("/", "\\\\");
			String os = System.getProperty("os.name");  
			if(!os.toLowerCase().startsWith("win")){  
				imgurl = imgurl.replaceAll("\\\\", "/");
			} 
			url = wizbini.getWebDocRoot() + imgurl;
			CommonLog.info("url:"+url);
			try {
				File file = new File(url);
				if (file.exists()) {

					String filename = file.getName();
					String filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
					ust_img += "." + filetype;
					_Img="h2"+dbUtils.SLASH+ust_img;
					String basePath = wizbini.getFileUploadPositionDirAbs() + File.separator + ust_id +dbUtils.SLASH+"h2"+ File.separator;
					//String basePath = wizbini.getFileUploadPositionDirAbs() + File.separator + ust_id + File.separator;
					File distPath = new File(basePath);
					if (!distPath.exists()) {
						distPath.mkdirs();
					}
					File distFile = new File(distPath, ust_img);
					if (distFile.exists()) {
						distFile.delete();
					}
					FileUtils.fileChannelCopy(file, distFile);
				} else {
					CommonLog.info("文件不存在");
					CommonLog.info("点击修改 不修改图片时，Linux下无法读取文件，就继续采用旧文件");
					ust_img = file.getName();
					// 文件不存在
				}
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}

		}
		specialTopic.setUst_img(_Img);
		userSpecialTopicMapper.update(specialTopic);
		userSpecialItemService.deleteByUstId(ust_id);
		userSpecialExpertService.deleteByUstId(ust_id);
		String[] courses = qid.split("\\~");
		for (int i = 0; i < courses.length; i++) {
			UserSpecialItem item = new UserSpecialItem();
			item.setUst_utc_id(ust_id);
			item.setUsi_itm_id(Long.parseLong(courses[i]));
			userSpecialItemService.add(item);
		}
		if (!StringUtils.isEmpty(expert_ids)) {
			String[] eptIds = expert_ids.split(",");
			for (int i = 0; i < eptIds.length; i++) {
				UserSpecialExpert expert = new UserSpecialExpert();
				expert.setUse_ent_id(Long.parseLong(eptIds[i]));
				expert.setUse_ust_id(ust_id);
				userSpecialExpertService.add(expert);
			}
		}

	}

	/**
	 * 用户顶层培训中心id
	 * @param top_tcr_id
	 *   用户角色
	 * @param rolExtId
	 * @param page
	 * @return
	 */
	public Page<UserSpecialTopic> getSpecialTopicList(Page<UserSpecialTopic> page) {
		List<UserSpecialTopic> list = userSpecialTopicMapper.getSpecialTopicPage(page);
		for (UserSpecialTopic UserSpecialTopic : list) {
			ImageUtil.combineImagePath(UserSpecialTopic);
				String Ust_title=UserSpecialTopic.getUst_title().trim();
				UserSpecialTopic.setUst_title(Ust_title);
		}
		return page;
	}
	public Page<UserSpecialTopic> getSpecialTopicFrontList(Page<UserSpecialTopic> page) {
		
		Map<String,Object> map=new HashMap<String, Object>();
		if(null != page.getParams().get("top_tcr_id")){
            map.put("top_tcr_id", page.getParams().get("top_tcr_id"));
            }
		map.put("ust_status", page.getParams().get("ust_status"));
		map.put("ust_showindex", page.getParams().get("ust_showindex"));
		int page_size = userSpecialTopicMapper.getCount(map);
		if(page_size > 0){
			page.setPageSize(page_size);
		}
		
		List<UserSpecialTopic> list = userSpecialTopicMapper.getSpecialTopicFrontPage(page);
		
		for (UserSpecialTopic UserSpecialTopic : list) {
			ImageUtil.combineImagePath(UserSpecialTopic);
		}
		return page;
	}
	public void publishAndCancel(long ust_id, int ust_status) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ust_status", ust_status);
		map.put("ust_id", ust_id);
		map.put("ust_update_time",getDate());
		userSpecialTopicMapper.updateStatus(map);
	}

	public void deleteSpecialTopic(long ust_id) {
		userSpecialExpertService.deleteByUstId(ust_id);
		userSpecialItemService.deleteByUstId(ust_id);
		userSpecialTopicMapper.delete(ust_id);
	}
	public int getCountByStatus(WizbiniLoader wizbini, loginProfile prof,int ust_status){
		Map<String,Object> map=new HashMap<String, Object>();
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
            map.put("top_tcr_id", prof.my_top_tc_id);
            }
		            map.put("ust_status", ust_status);
		return userSpecialTopicMapper.getCount(map);
	}
	public void updateHits(long ust_id){
		userSpecialTopicMapper.updateHits(ust_id);
	}
	
	/**
	 * 获取推送到首页的专题
	 * @return
	 */
	public List<UserSpecialTopic> getUserSpecialTopicListIndex(boolean flag,long top_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		if(flag){
			map.put("top_tcr_id", top_tcr_id);
		}
		return userSpecialTopicMapper.getUserSpecialTopicListIndex(map);
	}
	
	/**
	 * 获取培训中心【tcrId】（注：包括子中心）下所有专题的数量
	 * @param tcrId
	 * @return
	 */
	public long getTotalCountByTcrIdAndChild(long tcrId){
		return userSpecialTopicMapper.getTotalCountByTcrIdAndChild(tcrId);
	}
	
	public void batPublishAndCancel(String ids,int ust_status){
		String [] ustIds= ids.split(",");
		for (String ustId : ustIds) {
			publishAndCancel(Long.parseLong(ustId),ust_status);
		}
	}
	public void batDeleteSpecialTopic(String ids) {
		String [] ustIds=ids.split(",");
		for (String ustId : ustIds) {
			deleteSpecialTopic(Long.parseLong(ustId));
		}
	}

	/**
	 * 移动端获取已发布专题培训列表个数
	 * @param page
	 */
	public int getSpecialListTotalCount(boolean isTcIndependent,long top_tcr_id) {
		Map<String,Object> map=new HashMap<String, Object>();
		if(isTcIndependent){
            map.put("top_tcr_id", top_tcr_id);
        }  
		return userSpecialTopicMapper.getSpecialListTotalCount(map);
	}

	/**
	 * 检查是否存在ust_title
	 * @param ust_title
	 * @param old_value
	 * @param tcIndependent
	 * @param my_top_tc_id
	 * @return
	 */
	public boolean isExistTitle(String ust_title, String old_value,
			boolean istcIndependent, long my_top_tc_id) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ust_title", ust_title);
		map.put("old_value", old_value);
		if(istcIndependent){
            map.put("top_tcr_id", my_top_tc_id);
        }  
		return userSpecialTopicMapper.isExistFormSpec(map);
	}
}