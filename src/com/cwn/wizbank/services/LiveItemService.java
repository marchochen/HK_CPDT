package com.cwn.wizbank.services;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.exception.ErrorException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.LiveItemMapper;
import com.cwn.wizbank.scheduled.LiveScheduler;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.FileUtils;
import com.cwn.wizbank.utils.ImagePath;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;

/**
 * @author bill.lai
 */
@Service
public class LiveItemService extends BaseService<LiveItem> {
	
	private static final Logger logger = Log4jFactory.createLogger(LiveItemService.class);
	
	@Autowired
	LiveItemMapper liveItemMapper;
	
	@Autowired
	LiveRecordsService liveRecordsService;
	
	@Autowired
	LiveQcloudService liveItemQcloudService;
	
	@Autowired
	LiveVhallService liveItemVhallService;
	
	@Autowired
	LiveGenseeService liveItemGenseeService;
	
	@Autowired
	EnterpriseInfoPortalService enterpriseInfoPortalService;
	
	/**
	 * 是否满员
	 * @param liveItem
	 * @param prof
	 * @param wizbini
	 * @return
	 * @throws MessageException 
	 */
	public boolean hasLiveExcess(LiveItem liveItem,loginProfile prof,WizbiniLoader wizbini) throws MessageException{
		boolean flag = false;
		
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent() && LiveItem.LIVE_MODE_TYPE_VHALL.equalsIgnoreCase(liveItem.getLv_mode_type())){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("top_tc_id", prof.my_top_tc_id);
			param.put("ent_id", prof.usr_ent_id);
			/*
			 * 企业最大并发数(eip_live_max_count == -1是没有相关的企业关联该用户所在的培训中心。
			 * 				eip_live_max_count == 0为未设置直播并发数（暂相当于未购买直播）)
			 */
			long eip_live_max_count = enterpriseInfoPortalService.getLiveMaxCountByTcrId(prof.my_top_tc_id);
			if(eip_live_max_count == -1){
				eip_live_max_count = 0;//当没有相关的企业关联该用户所在的培训中心时。可以无限制观看
			}
			//企业关联培训中心下的所有直播的在线人数总和对比
			long tcr_all_online_count = liveItemMapper.getTcrAllOnlineCount(param);
			//企业最大并发数与该企业关联培训中心下的所有直播的在线人数总和对比
			if(eip_live_max_count != 0 && eip_live_max_count <= tcr_all_online_count){
				flag = true; //直播观众已满，请稍后再尝试 
				return flag;
			}
		}
		
		//int num = this.getLiveOnlinePeople(wizbini, prof, liveItem.getLv_id());
		int num = this.getOnlineNumberSpare(liveItem,prof,wizbini);
		/*
		 * liveItem.getLv_people_num() == 0为未设置限制。
		 * (liveItem.getLv_people_num() > 0 && num <= liveItem.getLv_people_num()) 如果没有超过设置的限制
		 */
		if(liveItem.getLv_people_num() == 0 || (liveItem.getLv_people_num() > 0 && num < liveItem.getLv_people_num())){	 //加入观看记录 状态1为正在观看
			liveRecordsService.saveOrUpdate(liveItem.getLv_id(), prof.usr_ent_id, 1);
		}else{ //直播观众已满，请稍后再尝试 
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * 发布与取消发布
	 * @param lvId
	 * @param type
	 * @return 
	 */
	public LiveItem updateStatus(long lvId, String type,loginProfile prof){
		LiveItem liveItem = this.get(lvId);
		if(LiveItem.PUBLISH.equalsIgnoreCase(type)){
			liveItem.setLv_status(LiveItem.STATUS_ON);
		}else{
			liveItem.setLv_status(LiveItem.STATUS_OFF);
		}
		liveItem.setLv_upd_datetime(super.getDate());
		liveItem.setLv_upd_usr_id(Long.toString(prof.usr_ent_id));
		this.update(liveItem);
		
		return liveItem;
	}
	
	/**
	 * 查看回放
	 * @param lv_id
	 * @param prof
	 * @return
	 */
	public LiveItem getLiveItemById(long lv_id,loginProfile prof){
		LiveItem liveItem =  this.get(lv_id);
		if(liveItem != null){
			liveItem.setLv_enc_id(EncryptUtil.cwnEncrypt(liveItem.getLv_id()));
			ImageUtil.combineImagePath(liveItem);
			liveItem.setLv_url("http://e.vhall.com/webinar/inituser/"+liveItem.getLv_webinar_id()+"?email="+prof.usr_ste_usr_id+"@cyberwisdom.net&name="+prof.usr_display_bil);
		}
		return liveItem;
	}
	
	public LiveItem getLiveItemById(long lv_id){
		LiveItem liveItem =  this.get(lv_id);
		if(liveItem != null){
			liveItem.setLv_enc_id(EncryptUtil.cwnEncrypt(liveItem.getLv_id()));
			ImageUtil.combineImagePath(liveItem);
		}
		return liveItem;
	}
	
	public List<LiveItem> getLvItemList(Page<LiveItem> page,WizbiniLoader wizbini,loginProfile prof,String searchContent, String type,boolean isMobile,boolean isLearner) throws MessageException {
		return this.getLvItemList(page, wizbini, prof, searchContent, type, isMobile, isLearner,"");
	}
	
	/**
	 * 获取直播频道列表信息
	 * @param page
	 * @param type 
	 * @return
	 * @throws MessageException 
	 */
	public List<LiveItem> getLvItemList(Page<LiveItem> page,WizbiniLoader wizbini,loginProfile prof,String searchContent, String type,boolean isMobile,boolean isLearner,String live_mode_type) throws MessageException {
		
		if(!"".equalsIgnoreCase(live_mode_type.trim()) && !LiveItem.LIVE_MODE_TYPE_VHALL.equalsIgnoreCase(live_mode_type.trim()) && !LiveItem.LIVE_MODE_TYPE_QCLOUD.equalsIgnoreCase(live_mode_type.trim()) && !LiveItem.LIVE_MODE_TYPE_GENSEE.equalsIgnoreCase(live_mode_type.trim())){
			return null;
		}
		
		//替换掉单引号
		searchContent = searchContent.replace("'", "''06");
		page.getParams().put("searchContent", searchContent);
		page.getParams().put("isLearner", isLearner);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		page.getParams().put("isRoleTcInd", AccessControlWZB.isRoleTcInd(prof.current_role));
		
		//是否开启了相关供应商的权限
		Map<String, Object> map = getLiveModeAuth(prof, wizbini);
		String[] live_mode_types = null;
		boolean vflag = map.get("vhallLiveAuth") != null ? Boolean.parseBoolean(map.get("vhallLiveAuth").toString()) : false;
		boolean qflag = map.get("qcloudLiveAuth") != null ? Boolean.parseBoolean(map.get("qcloudLiveAuth").toString()) : false;
		boolean gflag = map.get("genseeLiveAuth") != null ? Boolean.parseBoolean(map.get("genseeLiveAuth").toString()) : false;
		if("".equalsIgnoreCase(live_mode_type)){
			String str = "";
			if(qflag){
				str += LiveItem.LIVE_MODE_TYPE_QCLOUD + ",";
			}
			
			if((vflag && isLearner) || ("".equals(str) && vflag)){
				str += LiveItem.LIVE_MODE_TYPE_VHALL + ",";
			}
			
			if((gflag && isLearner) || ("".equals(str) && gflag)){
				str += LiveItem.LIVE_MODE_TYPE_GENSEE + ",";
			}
			
			if(str.length() > 0){
				str = str.substring(0, str.length()-1);
			}
			if(str != null && !"".equals(str) && str.length() > 0){
				live_mode_types = str.split(",");
			}
		}else{
			if((LiveItem.LIVE_MODE_TYPE_VHALL.equalsIgnoreCase(live_mode_type) && vflag) || (LiveItem.LIVE_MODE_TYPE_QCLOUD.equalsIgnoreCase(live_mode_type) && qflag)  || (LiveItem.LIVE_MODE_TYPE_GENSEE.equalsIgnoreCase(live_mode_type) && gflag) ){
				live_mode_types = new String[]{live_mode_type};
			}else{
				return null;
			}
		}
		page.getParams().put("live_mode_type", live_mode_types);
		
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			page.getParams().put("top_tc_id", prof.my_top_tc_id);
		}
		
		//直播状态
		String[] sstr = null;
		if(!type.equals("") && type.indexOf(",") != -1){ //多个状态
			sstr = type.split(",");
		}else if(!type.equals("")){ //如果只是一个状态
			sstr = new String[]{type};
		}
		
		page.getParams().put("type", sstr);
		List<LiveItem> list = liveItemMapper.getLvItemList(page);
		if(list != null && list.size() > 0){
			for (LiveItem liveItem : list) {
				liveItem.setLv_enc_id(EncryptUtil.cwnEncrypt(liveItem.getLv_id()));
				ImageUtil.combineImagePath(liveItem);
				if(liveItem.getLv_type() == LiveItem.LIVE_IN){
					liveItem.setLv_onlineNum(this.getOnlineNumberSpare(liveItem,prof,wizbini));
				}else if(liveItem.getLv_type() == LiveItem.LIVE_OVER){
					liveItem.setLv_onlineNum(liveRecordsService.getLiveInvolvementTotal(liveItem.getLv_id()));
				}else{
					liveItem.setLv_onlineNum(0);
				}
			}
		}
		return list;
	}
	
	/**
	 * 添加直播
	 * @param liveItem
	 * @param wizbini
	 * @param prof
	 * @param image
	 * @param imgurl
	 * @param image_radio
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ErrorException
	 * @throws MessageException
	 */
	public void createLive(LiveItem liveItem, WizbiniLoader wizbini, loginProfile prof, MultipartFile image,String imgurl,int image_radio) throws IllegalStateException, IOException, ErrorException, MessageException {
		//调用第三方接口
		LiveService liveService = this.getService(liveItem.getLv_mode_type());
		liveService.getInitLang(prof);
		liveService.createLVBChannel(liveItem);			
		//保存直播图片
		saveImage(wizbini, liveItem, image, imgurl, image_radio);
		//操作人信息
		liveItem.setLv_create_datetime(super.getDate());
		liveItem.setLv_create_usr_id(Long.toString(prof.usr_ent_id));
		liveItem.setLv_upd_datetime(super.getDate());
		liveItem.setLv_upd_usr_id(Long.toString(prof.usr_ent_id));
		super.add(liveItem);
	}
	
	/**
	 * 更新直播
	 * @param liveItem
	 * @param wizbini
	 * @param prof
	 * @param image
	 * @param imgurl
	 * @param image_radio
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ErrorException
	 * @throws MessageException
	 */
	public void updateLive(LiveItem liveItem,long lv_id, WizbiniLoader wizbini, loginProfile prof, MultipartFile image,String imgurl,int image_radio) throws IllegalStateException, IOException, ErrorException, MessageException {
		liveItem.setLv_id(lv_id);
		//调用第三方接口
		LiveService liveService = this.getService(liveItem.getLv_mode_type());
		liveService.getInitLang(prof);
		liveItem = liveService.updateLive(liveItem);
		//保存直播图片
		saveImage(wizbini, liveItem, image, imgurl, image_radio);
		//操作人信息
		liveItem.setLv_upd_datetime(super.getDate());
		liveItem.setLv_upd_usr_id(Long.toString(prof.usr_ent_id));
		liveItemMapper.update(liveItem);
	}
	
	/**
	 * 删除直播
	 * @param live_mode_type 
	 * @throws ErrorException 
	 * @throws MessageException 
	 */
	public void deleteLive(long lv_id,loginProfile prof) throws ErrorException, MessageException{		
		LiveItem liveItem =  this.get(lv_id);
		//调用第三方接口
		LiveService liveService = this.getService(liveItem.getLv_mode_type());
		liveService.getInitLang(prof);
		if(liveService.deleteLive(liveItem)){
			//第三方删除成功后删除本地的记录
			this.delete(lv_id);
		}		
	}
	
	/**
	 * 获取第三方的service
	 * @param lv_mode_type
	 * @return
	 * @throws MessageException
	 */
	public LiveService getService(String lv_mode_type) throws MessageException{
		if(LiveItem.LIVE_MODE_TYPE_VHALL.equalsIgnoreCase(lv_mode_type)){
			return liveItemVhallService;
		}else if(LiveItem.LIVE_MODE_TYPE_QCLOUD.equalsIgnoreCase(lv_mode_type)){
			return liveItemQcloudService;
		}else if(LiveItem.LIVE_MODE_TYPE_GENSEE.equalsIgnoreCase(lv_mode_type)){
			return liveItemGenseeService;
		}else{
			throw new MessageException("label_core_live_management_54");
		}
	}
	
	/**
	 * 保存直播图片
	 * @param wizbini
	 * @param liveItem
	 * @param image
	 * @param imgurl
	 * @param image_radio
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void saveImage(WizbiniLoader wizbini,LiveItem liveItem, MultipartFile image,String imgurl,int image_radio) throws IllegalStateException, IOException{
		if(image_radio == 2 && image != null && image.getSize() > 0){
			String saveDirPath = wizbini.getFileUploadLiveDirAbs() + dbUtils.SLASH;
			//dbUtils.delFiles(saveDirPath);
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			String new_filename = System.currentTimeMillis() + "";
			String filename = image.getOriginalFilename();
			String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			new_filename +=  "." + type;
			liveItem.setLv_image(new_filename);

			File targetFile = new File(saveDirPath+new_filename);
			image.transferTo(targetFile);
			//this.setActiveImage(liveItem,targetFile);
			liveItem.setLv_image(new_filename);
		}else if(image_radio == 1){
			imgurl = (imgurl.replace("/wizbank", "")).replaceAll("/", "\\\\");
			String url = wizbini.getWebDocRoot() + imgurl;
			try {
				File file = new File(url);
				if (file.exists()) {
					
					String new_filename = System.currentTimeMillis() + "";
					String filename = file.getName();
					String filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
					new_filename += "." + filetype;
					String basePath = wizbini.getFileUploadLiveDirAbs() + dbUtils.SLASH;
					File distPath = new File(basePath);
					if (!distPath.exists()) {
						distPath.mkdirs();
					}
					File distFile = new File(distPath, new_filename);
					if (distFile.exists()) {
						distFile.delete();
					}
					FileUtils.fileChannelCopy(file, distFile);
					liveItem.setLv_image(new_filename);
				} else {
					logger.info("文件不存在");// 文件不存在
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}				
		}else{
			@SuppressWarnings("unused")
			File targetFile = new File(getLinkPre(ImagePath.liveImage));
			//this.setActiveImage(liveItem,targetFile);
		}
	}
	
	/**
	 * 获取我的直播列表
	 * @param page
	 * @param string
	 * @throws MessageException 
	 */
	public List<LiveItem> getMyLiveList(Page<LiveItem> page, loginProfile prof, WizbiniLoader wizbini) throws MessageException {
		page.getParams().put("usr_id", Long.toString(prof.usr_ent_id));
		
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			page.getParams().put("top_tc_id", prof.my_top_tc_id);
		}
		List<LiveItem> list = liveItemMapper.getLiveListByUsrId(page);
		if(list != null && list.size() > 0){
			for (LiveItem liveItem : list) {
				liveItem.setLv_enc_id(EncryptUtil.cwnEncrypt(liveItem.getLv_id()));
				ImageUtil.combineImagePath(liveItem);
				if(liveItem.getLv_type() == LiveItem.LIVE_IN){
					liveItem.setLv_onlineNum(this.getOnlineNumberSpare(liveItem,prof,wizbini));
				}else{
					liveItem.setLv_onlineNum(0);
				}
			}
		}		
		return list;
	}
	
	/**
	 * 获取观看人数·
	 * @param lv_id
	 * @return
	 */
	public int getLiveOnlinePeople(WizbiniLoader wizbini,loginProfile prof,long lv_id){
		Map<String, Object> param = new HashMap<String, Object>();
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			param.put("top_tc_id", prof.my_top_tc_id);
		}
		param.put("lv_id", lv_id);
		return liveItemMapper.getLiveOnlinePeople(param);
	}
	
	/**
	 * 密码验证
	 * @param lv_id
	 * @param password
	 */
	public boolean checkLivePwd(long lv_id, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lv_id", lv_id);
		map.put("password", password);
		
		boolean flag = liveItemMapper.checkLivePwd(map);	
		
		return flag;
	}
	
	/**
	 *  查询直播个数
	 * @param wizbini
	 * @param prof
	 * @return
	 */
	public int getCount(WizbiniLoader wizbini,loginProfile prof) {
		Map<String, Object> map = new HashMap<String, Object>();
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			map.put("top_tc_id", prof.my_top_tc_id);
		}
		map.put("usr_ent_id", prof.usr_ent_id);
		return liveItemMapper.getCount(map);
	}
	
	/**
	 * 处理链接地址
	 * @param link
	 * @return
	 */
	public String getLinkPre(String link) {
		if(link == null){
			link = Application.MAIL_SCHEDULER_DOMAIN;
		}
		else if(Application.MAIL_SCHEDULER_DOMAIN != null ){
			if(Application.MAIL_SCHEDULER_DOMAIN.endsWith("/") && link.startsWith("/")){
				link = Application.MAIL_SCHEDULER_DOMAIN + link.substring(1);
			}else if((Application.MAIL_SCHEDULER_DOMAIN.endsWith("/") && !link.startsWith("/")) 
					|| (!Application.MAIL_SCHEDULER_DOMAIN.endsWith("/") && link.startsWith("/"))){
				link = Application.MAIL_SCHEDULER_DOMAIN + link;
			}else{
				link = Application.MAIL_SCHEDULER_DOMAIN +"/"+link;
			}
		}
		return link;
	}
	
	/**
	 * 设置活动状态
	 * 1	int	否	直播进行中, 参加者可以进入观看直播
	 * 2	int	否	预约中 , 活动预约中,尚未开始
	 * 3	int	否	结束 , 活动已结束
	 * 4	int	否	录播已上线, 参加者可以观看录播回放
	 * @throws MessageException 
	 */
	public boolean setLiveState (LiveItem liveItem) throws MessageException {
		boolean flag = false;
		//得到直播状态
		int lv_type = getLiveState(liveItem);
		//如果是录播已上线或者已结束.
		if(lv_type == LiveItem.LIVE_ONLINE || lv_type == LiveItem.LIVE_OVER){
			if(lv_type == LiveItem.LIVE_ONLINE){
				lv_type = LiveItem.LIVE_OVER;
			}
			liveItem = this.get(liveItem.getLv_id());
			liveItem.setLv_type(lv_type);
			liveItemMapper.update(liveItem);
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 检查修改当前时间大于结束时间的直播活动
	 */
	public void checkLiveTimeout(){
		List<LiveItem> list= liveItemMapper.getLiveItemByTimeout();
		if(list != null && list.size() > 0){
			Date now_date = null;
			for (LiveItem liveItem : list) {
				now_date = new Date();
				//如果当前时间已大于活动的结束时间
				if(liveItem.getLv_end_datetime().getTime() < now_date.getTime()){
					liveItem.setLv_type(LiveItem.LIVE_OVER);
					liveItemMapper.update(liveItem);
				}
			}
		}
	}

	/**
	 * 获取直播模式的权限
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	public Map<String, Object> getLiveModeAuth(loginProfile prof, WizbiniLoader wizbini) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String live_mode = LiveItem.LIVE_MODE_TYPE_OTHER;
		
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			long eip_live_max_count = enterpriseInfoPortalService.getLiveMaxCountByTcrId(prof.my_top_tc_id);
			if(eip_live_max_count == 0 || eip_live_max_count == -1){
				map.put("hasVhallLiveCount", false);
			}else{
				map.put("hasVhallLiveCount", true);
			}
			
			boolean vflag = false;
			boolean qflag = false;
			boolean gflag = false;
			String[] live_modes = enterpriseInfoPortalService.getEipLiveMode(prof.my_top_tc_id);			
			if(live_modes != null && live_modes.length > 0){
				if(live_modes.length >0){
					for (String lm : live_modes) {
						if(LiveItem.LIVE_MODE_TYPE_GENSEE.equalsIgnoreCase(lm)){
							gflag = true;
							if(LiveItem.LIVE_MODE_TYPE_OTHER.equalsIgnoreCase(live_mode)){
								live_mode = LiveItem.LIVE_MODE_TYPE_GENSEE;
							}
						}else if(LiveItem.LIVE_MODE_TYPE_VHALL.equalsIgnoreCase(lm)){
							vflag = true;
							if(LiveItem.LIVE_MODE_TYPE_OTHER.equalsIgnoreCase(live_mode)){
								live_mode = LiveItem.LIVE_MODE_TYPE_VHALL;
							}
						}else if(LiveItem.LIVE_MODE_TYPE_QCLOUD.equalsIgnoreCase(lm)){
							qflag = true;
							live_mode = LiveItem.LIVE_MODE_TYPE_QCLOUD;
						}
					}
				}
			}
			
			//判断是否是admin
			if(prof.usr_status != null && prof.usr_status.equals(dbRegUser.USR_STATUS_SYS) && prof.usr_ste_usr_id.equals("admin")){
				vflag = true;
				qflag = true;
				gflag = true;
				//默认为供应商,如果开启了才会默认。不然还是没有任何权限
				if(wizbini.cfgSysSetupadv.getLive().getQcloud().isLivePublish()){
					live_mode = LiveItem.LIVE_MODE_TYPE_QCLOUD;
				}else  if(wizbini.cfgSysSetupadv.getLive().getVhall().isLivePublish()){
					live_mode = LiveItem.LIVE_MODE_TYPE_VHALL;
				}else  if(wizbini.cfgSysSetupadv.getLive().getGensee().isLivePublish()){
					live_mode = LiveItem.LIVE_MODE_TYPE_GENSEE;
				}
				
			}
			map.put("vhallLiveAuth", vflag);
			map.put("qcloudLiveAuth", qflag);
			map.put("genseeLiveAuth", gflag);
			
		}else{
			map.put("hasVhallLiveCount", true);
			
			if(wizbini.cfgSysSetupadv.getLive().getGensee().isLivePublish()){
				map.put("genseeLiveAuth", true);
				live_mode = LiveItem.LIVE_MODE_TYPE_GENSEE;
			}else{
				map.put("genseeLiveAuth", false);
			}
			if(wizbini.cfgSysSetupadv.getLive().getVhall().isLivePublish()){
				map.put("vhallLiveAuth", true);
				live_mode = LiveItem.LIVE_MODE_TYPE_VHALL;
			}else{
				map.put("vhallLiveAuth", false);
			}
			if(wizbini.cfgSysSetupadv.getLive().getQcloud().isLivePublish()){
				map.put("qcloudLiveAuth", true);
				live_mode = LiveItem.LIVE_MODE_TYPE_QCLOUD;
			}else{
				map.put("qcloudLiveAuth", false);
			}
		}
		map.put("liveMode", live_mode);
		return map;
	}
	
	/**
	 * 调用发起直播接口
	 * @param lv_id
	 * @param prof
	 * @return
	 * @throws ErrorException
	 * @throws MessageException
	 */
	public LiveItem startLive(long lv_id, loginProfile prof) throws ErrorException, MessageException{
		LiveItem liveItem =  this.getLiveItemById(lv_id);		
		LiveService liveService = this.getService(liveItem.getLv_mode_type());
		liveService.getInitLang(prof);
		liveItem = liveService.startLive(liveItem, prof);	
		
		//发起直播，将状态转变成 直播中-1
		liveItem.setLv_type(LiveItem.LIVE_IN); //2-预告 1-直播中 3-已结束
		liveItem.setLv_real_start_datetime(super.getDate());
		liveItem.setLv_had_live(true);
		liveItem.setLv_upd_datetime(super.getDate());
		liveItem.setLv_upd_usr_id(Long.toString(prof.usr_ent_id));
		
		liveItemMapper.update(liveItem);
		//加入线程里面。监听直播是否结束
		LiveScheduler.addLiveItemList(liveItem);
			
		return liveItem;
	}
	
	/**
	 * 调用结束直播接口
	 * @param lv_id
	 * @param prof
	 * @return 
	 * @throws ErrorException
	 * @throws MessageException
	 */
	public LiveItem stopLive(long lv_id, loginProfile prof) throws ErrorException, MessageException{
		
		LiveItem liveItem =  this.get(lv_id);
		if(LiveItem.LIVE_MODE_TYPE_GENSEE.equalsIgnoreCase(liveItem.getLv_mode_type())){
			return liveItem;
		}
		LiveService liveService = this.getService(liveItem.getLv_mode_type());
		liveService.getInitLang(prof);
		liveItem = liveService.stopLive(liveItem);
		liveItem.setLv_type(LiveItem.LIVE_OVER); //2-预告 1-直播中 3-已结束
		liveItem.setLv_upd_datetime(super.getDate());
		liveItem.setLv_upd_usr_id(Long.toString(prof.usr_ent_id));
		liveItemMapper.update(liveItem);
		
		return liveItem;
	}
	
	/**
	 * 获取当前在线人数
	 * @param liveItem
	 * @throws MessageException 
	 */
	public int getOnlineNumber(LiveItem liveItem,loginProfile prof) throws MessageException{
		LiveService liveService = this.getService(liveItem.getLv_mode_type());
		liveService.getInitLang(prof);
		return  liveService.getOnlineUsers(liveItem);
	}
	
	public int getOnlineNumberSpare(LiveItem liveItem,loginProfile prof,WizbiniLoader wizbini) throws MessageException{
		if(LiveItem.LIVE_MODE_TYPE_VHALL.equalsIgnoreCase(liveItem.getLv_mode_type())){
			return liveItemVhallService.getOnlineUsers(liveItem);
		}else if(LiveItem.LIVE_MODE_TYPE_GENSEE.equalsIgnoreCase(liveItem.getLv_mode_type())){
			return liveItem.getLv_gensee_online_user();
		}else{
			//该接口暂时还没有用
			//liveItemQcloudService.getOnlineUsers(liveItem);
			return this.getLiveOnlinePeople(wizbini, prof, liveItem.getLv_id());
		}
	}
	
	
	/**
	 * 获取活动状态
	 * @param targetFile 
	 * @throws MessageException 
	 * @throws ErrorException 
	 * 1	int	否	直播进行中, 参加者可以进入观看直播
	 * 2	int	否	预约中 , 活动预约中,尚未开始
	 * 3	int	否	结束 , 活动已结束
	 * 4	int	否	录播已上线, 参加者可以观看录播回放
	 */
	public int getLiveState (LiveItem liveItem) throws MessageException {
		LiveService liveService = this.getService(liveItem.getLv_mode_type());
		return liveService.getLiveState(liveItem);
	}

	/**
	 * 展示互动回调接口（获取人数，处理结束直播）
	 * @param classNo
	 * @param action
	 * @param totalusernum
	 */
	public void updateLiveByClassNo(String classNo, String action, String totalusernum) {
		LiveItem liveItem = liveItemMapper.getLiveByClassNo(classNo);
		if(liveItem != null){
			//展示互动的在线人数
			if(totalusernum != null && !"".equals(totalusernum)){
				int lv_gensee_online_user = Integer.parseInt(totalusernum);
				if(lv_gensee_online_user >= 0){
					liveItem.setLv_gensee_online_user(lv_gensee_online_user);
				}
			}
			//停止上课，将直播状态设置为已结束
			if(action != null && !"".equals(action) && "105".equals(action)){
				liveItem.setLv_type(LiveItem.LIVE_OVER);
				liveItem.setLv_real_end_datetime(super.getDate());
			}
			liveItemMapper.update(liveItem);
		}
	}
	
	/**
	 * 获取展示互动课件信息并设置回放地址
	 */
	public void getRecordInfo(){
		List<LiveItem> list= liveItemMapper.getLiveItemByModeType();
		if(list != null && list.size() > 0){
			Date now_date = null;
			for (LiveItem liveItem : list) {
				now_date = new Date();
				//如果当前时间已大于结束时间
				if(liveItem.getLv_real_end_datetime() != null && liveItem.getLv_real_end_datetime().getTime() < now_date.getTime()){
					liveItem = liveItemGenseeService.getRecordInfo(liveItem);
					liveItemMapper.update(liveItem);
				}
			}
		}
	}
	
}
