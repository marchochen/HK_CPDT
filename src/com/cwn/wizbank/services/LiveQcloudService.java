package com.cwn.wizbank.services;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.exception.ErrorException;
import com.cwn.wizbank.utils.LabelContent;
import com.qcloud.QcloudApiModuleCenter;
import com.qcloud.Module.Base;
import com.qcloud.Module.Live;
import com.qcloud.Utilities.Json.JSONArray;
import com.qcloud.Utilities.Json.JSONObject;

/**
 * @author bill.lai
 */
@Service
public class LiveQcloudService  extends BaseService<LiveItem> implements LiveService {
	
	private static final Logger logger = Log4jFactory.createLogger(LiveItemService.class);
	
	@Autowired
	EnterpriseInfoPortalService enterpriseInfoPortalService;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	protected String curLang = null;	
	public void getInitLang(loginProfile prof) {
		this.curLang = prof.cur_lan;
	}
	
	/**
	 * 公共请求参数
	 * @param module
	 * @return
	 */
	private QcloudApiModuleCenter getConfig(Base module,long tcr_id){
		TreeMap<String, Object> config = new TreeMap<String, Object>();
		
		String secretid = WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getQcloud().getSecretId().trim();
		String secretkey = WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getQcloud().getSecretKey().trim();
		
		//如果是ln模式
		if(WizbiniLoader.getInstance().cfgSysSetupadv.isTcIndependent()){
			tcr_id = tcTrainingCenterService.getTopTwoTrainingCenterBytcrId(tcr_id);
			Map<String, Object> liveSecret = enterpriseInfoPortalService.getEipLiveSecretInfo(tcr_id);
			if(liveSecret.get("SecretId") != null){
				secretid = liveSecret.get("SecretId").toString();
			}else{
				secretid = "";
			}
			if(liveSecret.get("SecretKey") != null){
				secretkey = liveSecret.get("SecretKey").toString();
			}else{
				secretkey = "";
			}
		}
		
		config.put("SecretId", secretid);
		config.put("SecretKey", secretkey);
		/* 请求方法类型 POST、GET */
		config.put("RequestMethod", WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getQcloud().getRequestMethod().trim());
		/* 区域参数，可选: gz:广州; sh:上海; hk:香港; ca:北美;等。 */
		config.put("DefaultRegion", WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getQcloud().getRegion().trim());
		/*
		 * 你将要使用接口所在的模块，可以从 官网->云api文档->XXXX接口->接口描述->域名
		 * 中获取，比如域名：cvm.api.qcloud.com，module就是new Cvm()。
		 */
		QcloudApiModuleCenter qamc = new QcloudApiModuleCenter(module,config);
		return qamc;
	}
	
	/**
	 * 调用添加直播接口
	 * @return 
	 * @throws ErrorException 
	 */
	public LiveItem createLVBChannel(LiveItem liveItem) throws ErrorException  {
		
		QcloudApiModuleCenter module = this.getConfig(new Live(),liveItem.getLv_tcr_id());
		
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		/* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
		params.put("channelName", liveItem.getLv_title());
		params.put("channelDescribe", liveItem.getLv_desc());
		params.put("outputSourceType", 3);
		params.put("sourceList.1.name", "cwn-video-2017");
		params.put("sourceList.1.type", 1);
//		非必填	 string	 接收方播放器密码
//		params.put("playerPassword", "");
//		非必填	 array 	输出码率。注：参数数组，0表示原始码率；10表示550码率(即标准)；20表示900码率(即高清)。如需设置码率，0是必填
//		params.put("outputRate", liveItem.getLv_desc());
//		非必填		int		水印ID
//		params.put("watermarkId", 66666);
		
		String result = null;
		try {
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			result = module.call("CreateLVBChannel", params);
			logger.info("调用腾讯云创建直播接口返回结果：" + result);
			JSONObject jobj = new JSONObject(result);
			
			if(jobj != null && jobj.optString("code").equals("0")){
				liveItem.setLv_channel_id(jobj.optString("channel_id"));
				liveItem.setLv_type(LiveItem.LIVE_BOOKING); //设置成未发布
				liveItem.setLv_pwd(Integer.toString(new Random().nextInt(999999))); //直播密码
				liveItem.setLv_real_start_datetime(liveItem.getLv_start_datetime());
				liveItem.setLv_status(LiveItem.STATUS_OFF);
				
				JSONObject cjobj = new JSONObject(jobj.optString("channelInfo"));
				liveItem.setLv_upstream_address(cjobj.optString("upstream_address"));
				
				JSONArray cdjobj = cjobj.optJSONArray("downstream_address");
				JSONObject cdjobj_1 = cdjobj.getJSONObject(0);
				
				liveItem.setLv_hls_downstream_address(cdjobj_1.optString("hls_downstream_address"));
				liveItem.setLv_rtmp_downstream_address(cdjobj_1.optString("rtmp_downstream_address"));
				liveItem.setLv_flv_downstream_address(cdjobj_1.optString("flv_downstream_address"));
				
			}else if(jobj != null && jobj.optString("code").equals("4104") || jobj.optString("code").equals("4100")){
				throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_85"));
			} else{
				throw new ErrorException( jobj != null ? jobj.optString("message") : "");
			}
		} catch (Exception e) {
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_86") +e.getMessage());
		}
		return liveItem;
	}
	
	/**
	 * 调用更新直播接口
	 * @return 
	 */
	public LiveItem updateLive(LiveItem liveItem) throws ErrorException {
		LiveItem db_lvItem =  this.get(liveItem.getLv_id());
		
		//需要先关闭才能编辑
		stopLVBChannel(db_lvItem);
		
		QcloudApiModuleCenter module = this.getConfig(new Live(), liveItem.getLv_tcr_id());
		
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		/* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
		params.put("channelName", liveItem.getLv_title());
		params.put("channelDescribe", liveItem.getLv_desc());
		params.put("channelId", db_lvItem.getLv_channel_id());
		
		String result = null;
		try {
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			result = module.call("ModifyLVBChannel", params);
			logger.info("调用腾讯云更新直播接口返回结果：" + result);
			JSONObject jobj = new JSONObject(result);
			if(jobj != null && jobj.optString("code").equals("0")){
				
				db_lvItem.setLv_title(liveItem.getLv_title());
				db_lvItem.setLv_desc((liveItem.getLv_desc()));
				db_lvItem.setLv_start_datetime(liveItem.getLv_start_datetime());
				db_lvItem.setLv_end_datetime(liveItem.getLv_end_datetime());
				db_lvItem.setLv_real_start_datetime(liveItem.getLv_start_datetime());
				db_lvItem.setLv_image(liveItem.getLv_image());
				db_lvItem.setLv_tcr_id(liveItem.getLv_tcr_id());
				db_lvItem.setLv_need_pwd(liveItem.getLv_need_pwd());
				db_lvItem.setLv_people_num(liveItem.getLv_people_num());
				
				//更新完后要启动直播频道
				startLVBChannel(db_lvItem);
			}else if(jobj != null && jobj.optString("code").equals("4104") || jobj.optString("code").equals("4100")){
				throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_85"));
			} else{
				throw new ErrorException( jobj != null ? jobj.optString("message") : "");
			}
		} catch (Exception e) {
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_87")+e.getMessage());
		}
		return db_lvItem;		
	}
	
	/**
	 * 调用删除直播频道
	 * @throws ErrorException 
	 */
	public boolean deleteLive(LiveItem liveItem) throws ErrorException{
		
		//需要先关闭才能编辑
		stopLVBChannel(liveItem);
		
		QcloudApiModuleCenter module = this.getConfig(new Live(), liveItem.getLv_tcr_id());
		
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		/* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
		params.put("channelIds.1", liveItem.getLv_channel_id());
		
		try {
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			String result = module.call("DeleteLVBChannel", params);
			logger.info("调用腾讯云删除直播接口返回结果：" + result);
			JSONObject jobj = new JSONObject(result);
			if(jobj != null && jobj.optString("code").equals("0")){
				return true;
			}else if(jobj != null && jobj.optString("code").equals("4104") || jobj.optString("code").equals("4100")){
				throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_85"));
			} else{
				throw new ErrorException( jobj != null ? jobj.optString("message") : "");
			}
		} catch (Exception e) {
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_97") + e.getMessage());
		}
	}
	
	/**
	 * 调用发起直播接口
	 * @param lvItem
	 * @param prof 
	 * @return
	 * @throws ErrorException 
	 */
	public LiveItem startLive(LiveItem liveItem, loginProfile prof){
		return liveItem;
	}
	
	/**
	 * 调用结束直播接口
	 * @param lvItem
	 * @throws ErrorException 
	 */
	public LiveItem stopLive(LiveItem liveItem) throws ErrorException {
		this.stopLVBChannel(liveItem);
		return liveItem;
		
	}
	
	/**
	 * 调用批量停止直播频道
	 * @throws ErrorException 
	 */
	public boolean stopLVBChannel(LiveItem liveItem) throws ErrorException{
		
		QcloudApiModuleCenter module = this.getConfig(new Live(), liveItem.getLv_tcr_id());
		
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		/* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
		params.put("channelIds.1", liveItem.getLv_channel_id());
		
		String result = null;
		try {
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			result = module.call("StopLVBChannel", params);
			logger.info("调用腾讯云批量停止直播接口返回结果：" + result);
			JSONObject jobj = new JSONObject(result);
			if(jobj != null && jobj.optString("code").equals("0")){
				return true;
			}else if(jobj != null && jobj.optString("code").equals("4104") || jobj.optString("code").equals("4100")){
				throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_85"));
			} else{
				throw new ErrorException( jobj != null ? jobj.optString("message") : "");
			}
		} catch (Exception e) {
			throw new ErrorException(e.getMessage());
		}
	}
	
	/**
	 * 调用批量启用直播频道
	 * @throws ErrorException 
	 */
	public boolean startLVBChannel(LiveItem liveItem) throws ErrorException{
		
		QcloudApiModuleCenter module = this.getConfig(new Live(), liveItem.getLv_tcr_id());
		
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		/* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
		params.put("channelIds.1", liveItem.getLv_channel_id());
		
		String result = null;
		try {
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			result = module.call("StartLVBChannel", params);
			logger.info("调用腾讯云批量启用直播接口返回结果：" + result);
			JSONObject jobj = new JSONObject(result);
			if(jobj != null && jobj.optString("code").equals("0")){
				return true;
			}else if(jobj != null && jobj.optString("code").equals("4104") || jobj.optString("code").equals("4100")){
				throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_85"));
			} else{
				throw new ErrorException(jobj != null? jobj.optString("message"):"");
			}
		} catch (Exception e) {
			throw new ErrorException(e.getMessage());
		}
	}
	
	/**
	 * 查询直播频道详情
	 */
	 public LiveItem getDescribeLVBChannel(LiveItem liveItem){
		
		QcloudApiModuleCenter module = this.getConfig(new Live(), liveItem.getLv_tcr_id());
		 
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		/* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
		params.put("channelId", liveItem.getLv_channel_id());
		
		String result = null;
		try {
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			result = module.call("DescribeLVBChannel", params);
			logger.info("调用腾讯云查询直播详情接口返回结果：" + result);
			JSONObject jobj = new JSONObject(result);
			if(jobj != null && jobj.optString("code").equals("0")){
				
				JSONArray channelInfo = jobj.getJSONArray("channelInfo");
				JSONObject cijobj = channelInfo.getJSONObject(0);
				//0	无输入流 	1	直播中 2	异常 3	关闭
				int lv_type = Integer.parseInt(cijobj.optString("channel_status"));
				
				if(lv_type == 0){
					liveItem.setLv_type(LiveItem.LIVE_BOOKING);
				}else if(lv_type == 1){
					liveItem.setLv_type(lv_type);
				}else{
					liveItem.setLv_type(LiveItem.LIVE_OVER);
				}
				
			}
		} catch (Exception e) {
		}
		
		return liveItem;
	}
	 
	 /**
	 * 查询直播频道当前并发收看数
	 */
	 public int getOnlineUsers(LiveItem liveItem){
		
		 QcloudApiModuleCenter module = this.getConfig(new Live(), liveItem.getLv_tcr_id());
		 
		TreeMap<String, Object> params = new TreeMap<String, Object>();
		/* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
		params.put("channelIds.n", liveItem.getLv_channel_id());
		
		String result = null;
		try {
			/* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
			result = module.call("DescribeLVBOnlineUsers", params);
			logger.info("调用腾讯云查询直播频道当前并发收看数接口返回结果：" + result);
			JSONObject jobj = new JSONObject(result);
			if(jobj != null && jobj.optString("code").equals("0")){				
				JSONArray list = jobj.optJSONArray("list");
				if(list != null){
					JSONObject listjobj = list.getJSONObject(0);					
					return listjobj.optInt("online");
				}				
			}
		} catch (Exception e) {
		}
		
		return 0;
	}

	 /**
	 * 获取活动状态
	 * 1	int	否	直播进行中, 参加者可以进入观看直播
	 * 2	int	否	预约中 , 活动预约中,尚未开始
	 * 3	int	否	结束 , 活动已结束
	 * 4	int	否	录播已上线, 参加者可以观看录播回放
	 */
	 public int getLiveState (LiveItem liveItem){
		 liveItem = getDescribeLVBChannel(liveItem);
		 if(liveItem != null){
			 return liveItem.getLv_type();
		 }
		 return 0;
	 }
}
