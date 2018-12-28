package com.cwn.wizbank.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.batch.encrypt.EncryptUtil;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.exception.ErrorException;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.LocalHttpClient;

/**
 * @author bill.lai
 */
@Service
public class LiveVhallService extends BaseService<LiveItem> implements LiveService {
	
	private static final Logger logger = Log4jFactory.createLogger(LiveItemService.class);
	
	private static final String CREATEURL = "http://e.vhall.com/api/vhallapi/v2/webinar/create";
	
	private static final String UPDATEURL = "http://e.vhall.com/api/vhallapi/v2/webinar/update";
	
	private static final String STARTLIVEURL = "http://e.vhall.com/api/vhallapi/v2/webinar/start";
	
	private static final String DELETEURL = "http://e.vhall.com/api/vhallapi/v2/webinar/delete";
	
	private static final String STOPURL = "http://e.vhall.com/api/vhallapi/v2/webinar/stop";
	
	private static final String ONLINENUMBERURL = "http://e.vhall.com/api/vhallapi/v2/webinar/current-online-number";
	
	private static final String ACTIVEIMAGEURL = "http://e.vhall.com/api/vhallapi/v2/webinar/activeimage";
	
	private static final String CREATERECORDURL = "http://e.vhall.com/api/vhallapi/v2/record/create";
	
	private static final String GETSTATEURL = "http://e.vhall.com/api/vhallapi/v2/webinar/state";
	
	private static final String DEFAULTRECORDURL = "http://e.vhall.com/api/vhallapi/v2/record/default";
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	@Autowired
	LivePropertiesServices propertiesServices;
	
	protected String curLang = null;	
	public void getInitLang(loginProfile prof){
		this.curLang = prof.cur_lan;
	}
	
	/**
	 * 公共请求参数
	 * @return
	 */
	public List<NameValuePair> getConfig(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String key = WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getVhall().getLiveAccount().trim();
		String pwd = WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getVhall().getLivePassword().trim();
		
		try {
			pwd = EncryptUtil.decrypt(pwd, new StringBuffer(key).reverse().toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		params.add(new BasicNameValuePair("auth_type", propertiesServices.getAuth_type().trim()));
		params.add(new BasicNameValuePair("account", key));
		params.add(new BasicNameValuePair("password", pwd));
		return params;
	}
	
	/**
	 * 调用添加直播接口
	 * @param lvItem
	 * @param wizbini
	 * @param prof
	 * @param image
	 * @return 
	 * @throws ErrorException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws Exception
	 */
	public LiveItem createLVBChannel(LiveItem lvItem) throws ErrorException {			
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("subject", lvItem.getLv_title().trim()));
		params.add(new BasicNameValuePair("introduction", lvItem.getLv_desc().trim()));
		params.add(new BasicNameValuePair("start_time", Long.toString(lvItem.getLv_start_datetime().getTime()/1000).trim()));
		params.add(new BasicNameValuePair("type", propertiesServices.getType().trim()));
		params.add(new BasicNameValuePair("layout", propertiesServices.getLayout().trim()));
		params.add(new BasicNameValuePair("auto_record", propertiesServices.getAuto_record().trim()));
//		params.add(new BasicNameValuePair("is_chat", propertiesServices.getIs_chat().trim()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(CREATEURL,params);
		logger.info("调用微吼创建直播接口返回结果：" + jobj.toString());
		
		if(jobj != null && jobj.optString("code").equals("200")){
			lvItem.setLv_webinar_id(jobj.optLong("data"));
			lvItem.setLv_type(LiveItem.LIVE_BOOKING); //设置成未发布
			lvItem.setLv_pwd(Integer.toString(new Random().nextInt(999999))); //直播密码
			lvItem.setLv_real_start_datetime(lvItem.getLv_start_datetime());
			lvItem.setLv_status(LiveItem.STATUS_OFF);
		}else{
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_86") + jobj != null ? jobj.optString("msg") : "");
		}		
		return lvItem;
	}
	
	/**
	 * 调用更新直播接口
	 * @param lvItem
	 * @param wizbini
	 * @param prof
	 * @param image
	 * @return 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws ErrorException 
	 * @throws Exception
	 */
	public LiveItem updateLive(LiveItem liveItem) throws ErrorException {
		
		LiveItem db_lvItem =  this.get(liveItem.getLv_id());
		
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(db_lvItem.getLv_webinar_id())));
		params.add(new BasicNameValuePair("subject", liveItem.getLv_title()));
		params.add(new BasicNameValuePair("start_time", Long.toString(liveItem.getLv_start_datetime().getTime()/1000).trim()));
		params.add(new BasicNameValuePair("introduction", liveItem.getLv_desc()));
		params.add(new BasicNameValuePair("type", propertiesServices.getType().trim()));
		params.add(new BasicNameValuePair("is_open", propertiesServices.getIs_open().trim()));
		params.add(new BasicNameValuePair("layout", propertiesServices.getLayout().trim()));
//		params.add(new BasicNameValuePair("is_chat", propertiesServices.getIs_chat().trim()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(UPDATEURL,params);
		logger.info("调用微吼更新直播接口返回结果：" + jobj.toString());
		
		if(jobj != null && jobj.optString("code").equals("200")){
			db_lvItem.setLv_title(liveItem.getLv_title());
			db_lvItem.setLv_desc((liveItem.getLv_desc()));
			db_lvItem.setLv_start_datetime(liveItem.getLv_start_datetime());
			db_lvItem.setLv_end_datetime(liveItem.getLv_end_datetime());
			db_lvItem.setLv_real_start_datetime(liveItem.getLv_start_datetime());
			db_lvItem.setLv_image(liveItem.getLv_image());
			db_lvItem.setLv_tcr_id(liveItem.getLv_tcr_id());
			db_lvItem.setLv_need_pwd(liveItem.getLv_need_pwd());
			db_lvItem.setLv_people_num(liveItem.getLv_people_num());
		}else{
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_87") + jobj != null ? jobj.optString("msg") : "");
		}
		return db_lvItem;
	}
	
	
	/**
	 * 调用删除直播接口
	 * @param lvItem
	 * @return 
	 * @throws ErrorException 
	 */
	public boolean deleteLive(LiveItem liveItem) throws ErrorException{		
		
		List<NameValuePair> params = this.getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(liveItem.getLv_webinar_id())));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(DELETEURL,params);
		logger.info("调用微吼删除直播接口返回结果：" + jobj.toString());
		
		if(jobj != null && jobj.optString("code").equals("200")){			
			return true;
		}else{
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_97") + jobj != null ? jobj.optString("msg") : "");
		}
	}
	
	/**
	 * 调用更新直播接口
	 * @throws ErrorException 
	 */
	public void updateLive(LiveItem lvItem,loginProfile prof) throws ErrorException{
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(lvItem.getLv_webinar_id())));
		params.add(new BasicNameValuePair("subject", lvItem.getLv_title()));
		params.add(new BasicNameValuePair("start_time", Long.toString(lvItem.getLv_start_datetime().getTime()/1000)));
		params.add(new BasicNameValuePair("host", prof.usr_display_bil));	
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(UPDATEURL,params);
		logger.info("调用微吼更新直播接口返回结果：" + jobj.toString());
		if(jobj != null && !jobj.optString("code").equals("200")){
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_87") + jobj != null ? jobj.optString("msg") : "");
		}
	}
	
	/**
	 * 调用发起直播接口
	 * @param lvItem
	 * @param prof 
	 * @return
	 * @throws ErrorException 
	 */
	public LiveItem startLive(LiveItem liveItem, loginProfile prof) throws ErrorException{
		//更新主持人的名称
		this.updateLive(liveItem,prof);
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(liveItem.getLv_webinar_id())));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(STARTLIVEURL,params);
		logger.info("调用微吼发起直播接口返回结果：" + jobj.toString());
		
		if(jobj != null && jobj.optString("code").equals("200")){
			liveItem.setLv_url(jobj.optString("data"));
		}else{
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_98")+jobj != null ? jobj.optString("msg") : "");
		}
		return liveItem;
	}
	
	/**
	 * 调用结束直播接口
	 * @param lvItem
	 * @throws ErrorException 
	 */
	public LiveItem stopLive(LiveItem liveItem){
		
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(liveItem.getLv_webinar_id())));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(STOPURL,params);
		logger.info("调用微吼结束直播接口返回结果：" + jobj.toString());
		
		if(jobj != null && !jobj.optString("code").equals("200")){				
		}
		liveItem = this.createRecord(liveItem);
		return liveItem;
	}
	
	/**
	 * 生成回放
	 * @param targetFile 
	 * @return 
	 * @throws ErrorException 
	 */
	public LiveItem createRecord (LiveItem liveItem) {
		
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(liveItem.getLv_webinar_id()).trim()));
		params.add(new BasicNameValuePair("subject", liveItem.getLv_title().trim()));//生成回放名称
		params.add(new BasicNameValuePair("type", "0"));//默认0， 0表示按开始结束具体时间戳生成，1表示按开始结束秒数来生成
		params.add(new BasicNameValuePair("start_time", Long.toString(liveItem.getLv_real_start_datetime().getTime()/1000).trim()));//当type=0时，必填，表示回放开始时间戳
		params.add(new BasicNameValuePair("end_time", Long.toString(new Date().getTime()/1000).trim()));//当type=0时，必填，表示回放结束时间戳
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(CREATERECORDURL,params);
		logger.info("调用微吼生成回放接口返回结果：" + jobj.toString());
		
		if(jobj != null && jobj.optString("code").equals("200")){
			//得到回放ID
			liveItem.setLv_record_id(jobj.optLong("data"));
			this.defaultRecord(liveItem);
		}
		return liveItem;
	}
	
	/**
	 * 将回放设置为默认活动回放
	 * @throws ErrorException 
	 */
	public void defaultRecord (LiveItem lvItem){
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("record_id", Long.toString(lvItem.getLv_record_id()).trim()));
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(DEFAULTRECORDURL,params);
		logger.info("调用微吼设置为默认回放接口返回结果：" + jobj.toString());
		if(jobj != null && !jobj.optString("code").equals("200")){
		}
	}
	
	/**
	 * 获取当前在线人数
	 * @param lvItem
	 * @throws ErrorException 
	 */
	public int getOnlineUsers(LiveItem lvItem){		
		List<NameValuePair> params = getConfig();
		String webinar_id = Long.toString(lvItem.getLv_webinar_id());
		params.add(new BasicNameValuePair("webinar_id", webinar_id));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(ONLINENUMBERURL,params);
		logger.info("调用微吼获取当前在线人数接口返回结果：" + jobj.toString());
		if(jobj != null && jobj.optString("code").equals("200")){
			map.put(webinar_id, jobj.optInt("data"));
			return jobj.optInt("data");
		}
		if(map.get(webinar_id) != null){
			return Integer.parseInt(map.get(webinar_id).toString());
		}
		return 0;
	}
	
	/**
	 * 设置直播活动封面
	 * @param targetFile 
	 * @throws ErrorException 
	 */
	public void setActiveImage (LiveItem lvItem, File targetFile) throws ErrorException{
		
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(lvItem.getLv_webinar_id())));
		params.add(new BasicNameValuePair("image", targetFile.toString()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(ACTIVEIMAGEURL,params);
		logger.info("调用微吼设置直播活动封面接口返回结果：" + jobj.toString());
		if(jobj != null && !jobj.optString("code").equals("200")){
			throw new ErrorException(jobj != null ? jobj.optString("msg") : "");
		}
	}
	
	/**
	 * 获取活动状态
	 * 1	int	否	直播进行中, 参加者可以进入观看直播
	 * 2	int	否	预约中 , 活动预约中,尚未开始
	 * 3	int	否	结束 , 活动已结束
	 * 4	int	否	录播已上线, 参加者可以观看录播回放
	 */
	public int getLiveState (LiveItem lvItem){
		int lv_type = 0;
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("webinar_id", Long.toString(lvItem.getLv_webinar_id()).trim()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(GETSTATEURL,params);
		logger.info("调用微吼查询直播状态接口返回结果：" + jobj.toString());
		if(jobj != null && jobj.optString("code").equals("200")){
			//得到直播状态
			lv_type = (int) jobj.optLong("data");
		}
		return lv_type;
	}
	
}
