package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.SnsCollect;
import com.cwn.wizbank.entity.SnsCount;
import com.cwn.wizbank.entity.SnsShare;
import com.cwn.wizbank.entity.SnsValuationLog;
import com.cwn.wizbank.persistence.SnsCountMapper;

/**
 * service 实现
 */
@Service
public class SnsCountService extends BaseService<SnsCount> {

	@Autowired
	SnsCountMapper snsCountMapper;
	
	@Autowired
	SnsShareService snsShareService;
	
	@Autowired
	SnsValuationLogService snsValuationLogService;
	
	@Autowired
	SnsCollectService snsCollectService;
	
	public SnsCount getByTargetInfo(long targetId, String module){
		Map<String,Object> map = new HashMap<String,Object>();
		SnsCount count = null;
		map.put("targetId", targetId);
		map.put("module", module);
		map.put("isComment", 0);
		try{
			count = snsCountMapper.getByTargetInfo(map);
		}catch(Exception e){
			// 由于页面上快速点击多次，可能导致同一个用户在一个对像下会有多条记录，在这种情况下会出错，所以先删除出错的数据，再重新取数据
			snsCountMapper.delErrorData(map);
			count = snsCountMapper.getByTargetInfo(map);
		}
		return count;
		 
	}
	
	public SnsCount getByTargetInfo(long targetId, String module, int isComment){
		Map<String,Object> map = new HashMap<String,Object>();
		SnsCount count = null;
		map.put("targetId", targetId);
		map.put("module", module);
		map.put("isComment", isComment);
		try{
			count = snsCountMapper.getByTargetInfo(map);
		}catch(Exception e){
			// 由于页面上快速点击多次，可能导致同一个用户在一个对像下会有多条记录，在这种情况下会出错，所以先删除出错的数据，再重新取数据
			snsCountMapper.delErrorData(map);
			count = snsCountMapper.getByTargetInfo(map);
		}
		return count;
	}
	
	

	public long updateShareCount(long targetId, String module, long userId, boolean isAdd) throws Exception {

		SnsCount snsCount = getByTargetInfo(targetId, module, 0);
		
		if(snsCount == null && !isAdd){
			return 0;
			//throw new Exception("没有记录，不可以取消 ："+ targetId);
		}
		int count = 1;

		if (snsCount == null) {
			snsCount = new SnsCount();
			
			snsCount.setS_cnt_target_id((int) targetId);
			snsCount.setS_cnt_share_count(count);
			snsCount.setS_cnt_create_usr_id((int) userId);
			snsCount.setS_cnt_module(module);
		} else {
			if(snsCount.getS_cnt_share_count() == null){
				snsCount.setS_cnt_share_count(0);
			}
			if(isAdd){
				count = snsCount.getS_cnt_share_count() + count;
			} else {
				count = snsCount.getS_cnt_share_count() - count;
			}
			snsCount.setS_cnt_share_count(count);
			snsCount.setS_cnt_update_usr_id((int) userId);
		}
		saveOrupdate(snsCount);
		return count;
	}

	public long updateCollectCount(long targetId, String module, long userId, boolean isAdd) throws Exception {
		SnsCount snsCount = getByTargetInfo(targetId, module, 0);
		if(snsCount == null && !isAdd){
			return 0;
			//throw new Exception("没有记录，不可以取消 ："+ targetId);
		}
		int count = 1;
		if (snsCount == null) {
			snsCount = new SnsCount();
			snsCount.setS_cnt_target_id((int) targetId);
			snsCount.setS_cnt_collect_count(count);
			snsCount.setS_cnt_create_usr_id((int) userId);
			snsCount.setS_cnt_module(module);
		} else {
			if(snsCount.getS_cnt_collect_count() == null){
				snsCount.setS_cnt_collect_count(0);
			}
			if(isAdd){
				count = snsCount.getS_cnt_collect_count() + count;
			} else {
				count = snsCount.getS_cnt_collect_count() - count;
			}
			snsCount.setS_cnt_collect_count(count);
			snsCount.setS_cnt_update_usr_id((int) userId);
		}
		saveOrupdate(snsCount);
		return count;
	}

	/**
	 * 更新赞的统计
	 * 
	 * @param targetId
	 * @throws Exception 
	 */
	public long updateLikeCount(long targetId, String module, int isComment, long userId, boolean isAdd) throws Exception {
		SnsCount snsCount = getByTargetInfo(targetId, module, isComment);
		if(snsCount == null && !isAdd){
			return 0;
			//throw new MessageException("没有记录，不可以取消 ："+ targetId);
		}
		int count = 1;
		if (snsCount == null) {
			snsCount = new SnsCount();
			snsCount.setS_cnt_target_id((int) targetId);
			snsCount.setS_cnt_like_count(count);
			snsCount.setS_cnt_create_usr_id((int) userId);
			snsCount.setS_cnt_module(module);
		} else {
			if(snsCount.getS_cnt_like_count() == null){
				snsCount.setS_cnt_like_count(0);
			}
			if(isAdd){
				count = snsCount.getS_cnt_like_count() + count;
			} else {
				count = snsCount.getS_cnt_like_count() - count;
			}
			snsCount.setS_cnt_like_count(count);
			snsCount.setS_cnt_update_usr_id((int) userId);
		}
		snsCount.setS_cnt_is_comment(isComment);
		saveOrupdate(snsCount);

		return count;
	}

	/**
	 * 更新
	 * 
	 * @param snsCount
	 */
	public void saveOrupdate(SnsCount snsCount) {
		if (snsCount != null && snsCount.getS_cnt_id() != null
				&& snsCount.getS_cnt_id() > 0) {
			snsCount.setS_cnt_update_time(getDate());
			snsCountMapper.update(snsCount);
		} else {
			snsCount.setS_cnt_create_time(getDate());
			snsCountMapper.insert(snsCount);
		}
	}
	
	/**
	 * 按类型删除对象
	 * @param targetId
	 * @param module
	 */
	public void deleteRecord(Long targetId, String module, int isComment){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetId", targetId);
		map.put("module", module);
		map.put("isComment", isComment);
		snsCountMapper.deleteRecord(map);
	}
	
	
	/**
	 * 获取用户赞分享收藏课程的记录
	 * @param targetId
	 * @param userEntId
	 * @return
	 */
	public Map<String,Object> getUserSnsDetail(long targetId, long userEntId, String module){
		Map<String, Object> map = new HashMap<String, Object>();
		SnsShare share = snsShareService.getByUserId(targetId, userEntId, module);
		SnsValuationLog	snsValuationLog = snsValuationLogService.getByUserId(targetId, userEntId, module, 0);
		SnsCollect collect = snsCollectService.getByUserId(targetId, userEntId, module);
		map.put("share", share);
		map.put("like", snsValuationLog);
		map.put("collect", collect);
		return map;
	}

}