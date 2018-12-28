package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.ItemTargetLrnDetail;
import com.cwn.wizbank.persistence.ItemTargetLrnDetailMapper;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class ItemTargetLrnDetailService extends BaseService<ItemTargetLrnDetail> {

	@Autowired
	ItemTargetLrnDetailMapper itemTargetLrnDetailMapper;
	
	@Autowired
	AeApplicationService aeApplicationService;
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	SnsValuationLogService snsValuationLogService;
	
	@Autowired
	SnsCommentService snsCommentService;
	
	@Autowired
	SnsCountService snsCountService;
	/**
	 * 推荐类型 ：  职级推荐，部门推荐，上司推荐
	 * @param userEntId
	 * @param selectType 推荐类型
	 * @param isCompulsory 是否必修 1是，0 否
	 * @param itemType   课程类型： 面授课程，网上课程，项目式课程
	 * @param page
	 * @return
	 */
	public Page<ItemTargetLrnDetail> recommend(long userEntId, String curLang,
		Page<ItemTargetLrnDetail> page){
		page.getParams().put("userEntId", userEntId);
		List<ItemTargetLrnDetail> list = itemTargetLrnDetailMapper.getRecommend(page);
		for(ItemTargetLrnDetail itd : list) {
			
			if(itd.getApp() != null) {
				if(StringUtils.isNotEmpty(itd.getApp().getApp_status())) {
					itd.getApp().setApp_status(CwnUtil.getAppStatusStr(itd.getApp().getApp_status(), curLang));
				}
			}
			AeItem parent = null;
			if(itd.getItem() != null) {
				AeItem itm = itd.getItem();
				if(itm.getItm_run_ind() != null && itm.getItm_run_ind() == 1) {
					parent = aeItemService.getParent(itm.getItm_id());
					if(parent != null){
						itm.setParent(parent);
					}
				}
				itm.setSnsCount(snsCountService.getByTargetInfo(itm.getItm_id(), SNS.MODULE_COURSE));	
				itm.setUserLike(snsValuationLogService.getByUserId(itm.getItm_id(), userEntId, SNS.MODULE_COURSE, 0));
				itm.setCnt_comment_count(snsCommentService.getCommentCount(itm.getItm_id(), SNS.MODULE_COURSE));
				
				if(parent != null){
					ImageUtil.combineImagePath(parent);
					itm.setItm_icon(parent.getItm_icon());//如果是班级的时候，就显示班级对应课程的图片
				}else{
					ImageUtil.combineImagePath(itm);
				}
			}
		}
		
		page.setResults(list);
			
		return page;
	}
	
	/**
	 * 管理员推荐（公司推荐）
	 * @param page
	 * @return
	 */
	public Page<ItemTargetLrnDetail> adminRecommend(long userEntId,
			Page<ItemTargetLrnDetail> page){
		
		return page;
	}

	public void setItemTargetLrnDetailMapper(ItemTargetLrnDetailMapper itemTargetLrnDetailMapper){
		this.itemTargetLrnDetailMapper = itemTargetLrnDetailMapper;
	}

	public ItemTargetLrnDetail getByUserItemId(Long itmId, long userEntId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userEntId", userEntId);
		map.put("itmId", itmId);
		return itemTargetLrnDetailMapper.getByUserItemId(map);
	}
}