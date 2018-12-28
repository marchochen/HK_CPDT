package com.cwn.wizbank.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.JsonMod.Ann.bean.ReceiptBean;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Message;
import com.cwn.wizbank.persistence.CourseMapper;
import com.cwn.wizbank.persistence.MessageMapper;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.Page;
/**
 * service 实现
 */
@Service
public class MessageService extends BaseService<Message> {

	@Autowired
	MessageMapper messageMapper;
	
	@Autowired
	CourseMapper courseMapper;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;

	/**
	 * 获取公告
	 * @param usr_ent_id 当前用户
	 * @param tcr_id
	 *            培训中心id
	 * @param user_id
	 *            用户id
	 * @param page
	 * @return
	 */
	public Page<Message> pageMessage(long usr_ent_id,List<Long> tcr_ids, long root_ent_id, boolean isMobile, Page<Message> page, long new_duration) {
		
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("tcr_ids", tcr_ids);
		page.getParams().put("root_ent_id", root_ent_id);
		page.getParams().put("isMobile", isMobile);
		page.getParams().put("curTime", getDate());
		page.getParams().put("new_duration", new_duration);

		if (page.getSortname() == null) {
			page.setSortname("msg_upd_date");
			page.setSortorder("desc");
		}
		page.setResults(messageMapper.page(page));
		return page;
	}

	/**
	 * 获取公告
	 * 
	 * @param tcr_id
	 *            培训中心id
	 * @param user_id
	 *            用户id
	 * @param page
	 * @return
	 */
	public Page<Message> pageMessage(long usr_ent_id,long tcr_id, long root_ent_id, boolean isMobile, Page<Message> page, long new_duration) {
		List<Long> list = new ArrayList<Long>();
		if (tcr_id > 0) {
			list.add(tcr_id);
		}
		pageMessage(usr_ent_id,list, root_ent_id, isMobile, page, new_duration);
		return page;
	}
	/**
	 * 是否是最新的公告
	 * 
	 * @param curTime
	 *            当前时间
	 * @param targetTime
	 *            对象发布时间
	 * @param fixedDays
	 * @return
	 */
	public boolean isNew(Timestamp curTime, Timestamp targetTime, int fixedDays) {
		boolean isNewest = false;
		if (curTime != null && targetTime != null) {
			long timeDiff = curTime.getTime() - targetTime.getTime();
			long targetTimeDiff = fixedDays * 24 * 60 * 60 * 1000;
			if (timeDiff <= targetTimeDiff) {
				isNewest = true;
			}
		}
		return isNewest;
	}

	/**
	 * 获取班级公告列表
	 * @param itmId
	 * @param resId
	 * @return
	 */
	public List<Message> getMessageList(long itmId, long resId){
		Long dtResId = courseMapper.getResIdByItmId(itmId);
		List<Long> resIds = new ArrayList<Long>();
		resIds.add(resId);
		if(dtResId != null && resId != dtResId) {
			resIds.add(dtResId);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resIds", resIds);
		map.put("curTime", getDate());
		return messageMapper.getMessagesByResId(map);
	}
	
	/**
	 * 获取公告的最新条数
	 * @param tcrId
	 * @param prof
	 * @param beforeDay
	 * @return
	 */
	public int getNewCount(List<Long> tcrIdList,  loginProfile prof, int beforeDay, int isMobile){	
		//获取之前的时间
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tcrIds", tcrIdList);
		map.put("isMobile", isMobile);
		map.put("curTime", getDate());
		DateUtil dateUtil = new DateUtil();
		Date beforeDate = dateUtil.getSomeDaysBeforeAfter((Date)map.get("curTime"), -beforeDay);
		map.put("beforeDate", beforeDate);
		return messageMapper.getNewCount(map);
	}

	/**
	 * 公告回执
	 * @param usr_ent_id 
	 * @param msgId 
	 * @return
	 * @throws ParseException 
	 * **/
	public void insReceipt(long rec_ent_id, long rec_msg_id) throws ParseException {
		long rec_usg_id = messageMapper.getRecUsgId(rec_ent_id);
		ReceiptBean receipt = new ReceiptBean(rec_msg_id, rec_ent_id, rec_usg_id,getDate());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("msgId", rec_msg_id);
		map.put("usrId", rec_ent_id);
		Integer rec =  messageMapper.getReceipt(map);
		if(rec == null || rec < 1){
			messageMapper.insReceipt(receipt);
		}
	}

	/**公告是否有回执*/
	public boolean getReceipt(long msgId, long usr_ent_id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("msgId", msgId);
		map.put("usrId", usr_ent_id);
		return messageMapper.getReceipt(map) != null;
	}
	
	/**公告是否需要回执
	 * @return */
	public boolean getIsOrNotReceipt(long msgId){
		boolean IsOrNotReceipt = false;
		if(messageMapper.getIsOrNotReceipt(msgId) == null || messageMapper.getIsOrNotReceipt(msgId) ==0) IsOrNotReceipt = true ;
		return IsOrNotReceipt;
	}
	
	/**当公告不需要回执时,检查当前是否已阅读*/
	public boolean getNotReceipt(long msgId, long usr_ent_id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("msgId", msgId);
		map.put("usrId", usr_ent_id);
		return messageMapper.getNotReceipt(map) != null;
	}
	
	/**
	 * 获取用户未读公告
	 * @param tcrIdList
	 * @param prof
	 * @param beforeDay
	 * @param isMobile
	 * @return
	 */
	public int getUserNotReadCount(List<Long> tcrIdList, loginProfile prof, int isMobile) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("usr_ent_id", prof.getUsr_ent_id());
		map.put("tcrIds", tcrIdList);
		map.put("isMobile", isMobile);
		map.put("curTime", getDate());
		return messageMapper.getNotReadCountByUsrId(map);
	}
}