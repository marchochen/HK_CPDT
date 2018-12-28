package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.LiveRecords;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.persistence.LiveRecordsMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;

@Service
public class LiveRecordsService extends BaseService<LiveRecords> {    
    
	@Autowired
	LiveRecordsMapper liveRecordsMapper;
	
	@Autowired
	RegUserService regUserService;
	
	/**
	 * 添加与更新
	 * @param lv_id
	 * @param usr_ent_id
	 * @param lr_status
	 */
	public void saveOrUpdate(long lv_id,long usr_ent_id,int lr_status){
		LiveRecords lr = this.getLiveRecordsByUsrAndLvId(lv_id,Long.toString(usr_ent_id));
		if(lr == null){
			lr = new LiveRecords();
			lr.setLr_live_id(lv_id);
			lr.setLr_usr_id(Long.toString(usr_ent_id));
			lr.setLr_status(lr_status);
			lr.setLr_create_time(super.getDate());
			liveRecordsMapper.insert(lr);
		}else{
			lr.setLr_status(lr_status);
			liveRecordsMapper.update(lr);
		}		
	}
	
	/**
	 * 根据直播ID与用户ID查询数据
	 * @param lv_id
	 * @param usr_ent_id
	 * @return
	 */
	public LiveRecords getLiveRecordsByUsrAndLvId(long lv_id, String usr_ent_id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lr_live_id", lv_id);
		map.put("lr_usr_id", usr_ent_id);
		LiveRecords liveRecords = null;
		List<LiveRecords> list = liveRecordsMapper.getLiveRecordsByUsrAndLvId(map);
		if(list != null && list.size() > 0){
			if(list.get(0) != null){
				liveRecords = list.get(0);
			}
		}
		return liveRecords;
	}
	
	/**
	 * 获取直播观看用户记录
	 * @param page
	 * @param lv_id
	 * @return
	 */
	public List<LiveRecords> getLiveOnlineUser(Page<LiveRecords> page,long lv_id){
		page.getParams().put("lv_id", lv_id);
		List<LiveRecords> lrlist =  liveRecordsMapper.getLiveOnlineUser(page);
		for (LiveRecords liveRecords : lrlist) {
			liveRecords.setRegUser((RegUser) ImageUtil.combineImagePath(regUserService.get(Long.parseLong(liveRecords.getLr_usr_id()))));
		}
		
		return lrlist;
	}
	
	public int getLiveInvolvementTotal(long lv_id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lv_id", lv_id);
		return liveRecordsMapper.getLiveInvolvementTotal(map);
	}

}
