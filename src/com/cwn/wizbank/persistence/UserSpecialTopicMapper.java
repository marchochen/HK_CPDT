package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserSpecialTopic;
import com.cwn.wizbank.utils.Page;

public interface UserSpecialTopicMapper  extends BaseMapper<UserSpecialTopic>{
	
	public Long add(UserSpecialTopic userSpecialTopic);
	public List<UserSpecialTopic> getSpecialTopicPage(Page<UserSpecialTopic> page);
    public void updateStatus(Map<String,Object> map);
    public int getCount(Map<String,Object> map);
    public void updateHits(long ust_id);
	public List<UserSpecialTopic> getUserSpecialTopicListIndex(Map<String,Object> map);
	/**
	 * 获取培训中心【tcrId】（注：包括子中心）下所有专题的数量
	 * @param tcrId
	 * @return
	 */
	public long getTotalCountByTcrIdAndChild(long tcrId);
	public List<UserSpecialTopic> getSpecialTopicFrontPage(Page<UserSpecialTopic> page);
	public int getSpecialListTotalCount(Map<String,Object> map);
	public boolean isExistFormSpec(Map<String,Object> map);
}
