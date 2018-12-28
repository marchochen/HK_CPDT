package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.utils.Page;

public interface LiveItemMapper extends BaseMapper<LiveItem> {
	
	/**
	 * 获取直播频道列表
	 * @param page
	 */
	List<LiveItem> getLvItemList(Page<LiveItem> page);

	List<LiveItem> getLiveListByUsrId(Page<LiveItem> page);
	
	int getLiveOnlinePeople(Map<String, Object> map);

	boolean checkLivePwd(Map<String, Object> map);

	int getCount(Map<String, Object> map);

	long getTcrAllOnlineCount(Map<String, Object> param);

	List<LiveItem> getLiveItemByTimeout();

	LiveItem getLiveByClassNo(String lv_channel_id);

	List<LiveItem> getLiveItemByModeType();
	
}
