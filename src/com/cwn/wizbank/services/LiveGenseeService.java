package com.cwn.wizbank.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
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
public class LiveGenseeService extends BaseService<LiveItem> implements LiveService {
	
	private static final Logger logger = Log4jFactory.createLogger(LiveItemService.class);
	
	private static final String CREATEURL = "training/room/created";
	
	private static final String UPDATEURL = "training/room/modify";
	
	private static final String DELETEURL = "training/room/deleted";
	
	private static final String GETCORDURL = "training/courseware/list";
	
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
		String key = WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getGensee().getLiveAccount().trim();
		String pwd = WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getGensee().getLivePassword().trim();
		
		try {
			pwd = EncryptUtil.decrypt(pwd, new StringBuffer(key).reverse().toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		params.add(new BasicNameValuePair("loginName", key));
		params.add(new BasicNameValuePair("password", pwd));
		return params;
	}
	
	/**
	 * 拼路径
	 * @param url
	 * @return
	 */
	public String getUrl(String url){
		//http://{站点域名}/integration/site/{接口路径}
		return WizbiniLoader.getInstance().cfgSysSetupadv.getLive().getGensee().getLiveGenseeUrl().trim() + "/integration/site/" + url;
	}
	
	/**
	 * 调用添加直播接口
	 * @param lvItem
	 * @throws ErrorException 
	 */
	public LiveItem createLVBChannel(LiveItem lvItem) throws ErrorException {			
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("subject", lvItem.getLv_title().trim()));
		params.add(new BasicNameValuePair("description", lvItem.getLv_desc().trim()));
		params.add(new BasicNameValuePair("startDate", Long.toString(lvItem.getLv_start_datetime().getTime()).trim()));
		params.add(new BasicNameValuePair("invalidDate", Long.toString(lvItem.getLv_end_datetime().getTime()).trim()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(getUrl(CREATEURL),params);
		logger.info("调用展示互动创建直播接口返回结果：" + jobj.toString());
		
		if(jobj != null &&  jobj.optString("code").equals("0")){
			lvItem.setLv_channel_id(jobj.optString("id"));
			lvItem.setLv_student_token(jobj.optString("studentToken"));
			lvItem.setLv_teacher_token(jobj.optString("teacherToken"));
			lvItem.setLv_student_client_token(jobj.optString("studentClientToken"));
			lvItem.setLv_teacher_join_url(jobj.optString("teacherJoinUrl"));
			lvItem.setLv_student_join_url(jobj.optString("studentJoinUrl"));
			
			lvItem.setLv_type(LiveItem.LIVE_BOOKING); //设置成未发布
			lvItem.setLv_pwd(Integer.toString(new Random().nextInt(999999))); //直播密码
			lvItem.setLv_real_start_datetime(lvItem.getLv_start_datetime());
			lvItem.setLv_status(LiveItem.STATUS_OFF);
		}else{
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_86") + jobj != null ? jobj.optString("message") : "");
		}		
		return lvItem;
	}
	
	/**
	 * 调用更新直播接口
	 * @param lvItem
	 * @return 
	 * @throws ErrorException 
	 */
	public LiveItem updateLive(LiveItem liveItem) throws ErrorException {
		
		LiveItem db_lvItem =  this.get(liveItem.getLv_id());
		
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("id", db_lvItem.getLv_channel_id()));
		params.add(new BasicNameValuePair("subject", liveItem.getLv_title().trim()));
		params.add(new BasicNameValuePair("description", liveItem.getLv_desc().trim()));
		params.add(new BasicNameValuePair("startDate", Long.toString(liveItem.getLv_start_datetime().getTime()).trim()));
		params.add(new BasicNameValuePair("invalidDate", Long.toString(liveItem.getLv_end_datetime().getTime()).trim()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(getUrl(UPDATEURL),params);
		logger.info("调用展示互动更新直播接口返回结果：" + jobj.toString());
		
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
		}else{
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_87") + jobj != null ? jobj.optString("message") : "");
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
		params.add(new BasicNameValuePair("roomId", liveItem.getLv_channel_id()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(getUrl(DELETEURL),params);
		logger.info("调用展示互动删除直播接口返回结果：" + jobj.toString());
		
		if(jobj != null && jobj.optString("code").equals("0")){	
			return true;
		}else{
			throw new ErrorException(LabelContent.get(curLang, "label_core_live_management_97") + jobj != null ? jobj.optString("message") : "");
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
		return liveItem;
	}
	
	/**
	 * 调用结束直播接口
	 * @param lvItem
	 * @throws ErrorException 
	 */
	public LiveItem stopLive(LiveItem liveItem){
		return liveItem;
	}
	
	/**
	 * 获取课堂录制下的所有课件
	 * @return 
	 * @throws ErrorException 
	 */
	public LiveItem getRecordInfo(LiveItem liveItem) {
		List<NameValuePair> params = getConfig();
		params.add(new BasicNameValuePair("roomId", liveItem.getLv_channel_id()));
		
		JSONObject jobj = LocalHttpClient.HttpGetResponseUrl(getUrl(GETCORDURL),params);
		logger.info("调用展示互动获取课堂录制下的所有课件接口返回结果：" + jobj.toString());
		
		if(jobj != null &&  jobj.optString("code").equals("0")){
			JSONArray ja = jobj.optJSONArray("coursewares");
			if(ja != null){
				JSONObject jo = ja.getJSONObject(0);
				if(jo != null && jo.optString("url") != null){
					liveItem.setLv_gensee_record_url( jo.optString("url"));
				}
			}else{
				liveItem.setLv_gensee_record_url("false");
			}
		}
		return liveItem;
	}
	
	
	/**
	 * 获取当前在线人数
	 * @param lvItem
	 * @throws ErrorException 
	 */
	public int getOnlineUsers(LiveItem liveItem){		
		return liveItem.getLv_gensee_online_user();
	}
	
	/**
	 * 获取活动状态
	 * 1	int	否	直播进行中, 参加者可以进入观看直播
	 * 2	int	否	预约中 , 活动预约中,尚未开始
	 * 3	int	否	结束 , 活动已结束
	 * 4	int	否	录播已上线, 参加者可以观看录播回放
	 */
	public int getLiveState (LiveItem liveItem){
		return liveItem.getLv_type();
	}
	
}
