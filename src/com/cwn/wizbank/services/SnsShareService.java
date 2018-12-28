package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.SnsShare;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.SnsShareMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.RequestStatus;
/**
 * service 实现
 */
@Service
public class SnsShareService extends BaseService<SnsShare> {

	@Autowired
	SnsShareMapper snsShareMapper;

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	SnsCountService snsCountService;

	@Autowired
	AeApplicationMapper aeApplicationMapper;


	/**
	 * 分享操作
	 * @throws Exception
	 */
	public void share(Model model, long userEntId, WizbiniLoader wizbini, long targetId, String module, String note, long tkhId, String curLang) throws Exception {

/*			if(SNS.MODULE_COURSE.equals(module) && tkhId != -1){
			AeApplication app = null;
			if(tkhId < 1){
				 app = aeApplicationService.getMaxAppByUser(userEntId, targetId);
			} else {
				 app = aeApplicationMapper.getByTkhId(tkhId);
			}
			if(app != null){
				if(!AeApplication.APP_STATUS_ADMITTED.equals(app.getApp_status())){
					throw new MessageException(LabelContent.get(curLang, "error_msg_need_app"));
				}
			} else {
				throw new MessageException(LabelContent.get(curLang, "error_msg_need_app"));
			}
		}*/

		if (this.getByUserId(targetId, userEntId, module) == null) {
			SnsShare share = new SnsShare();

			share.setS_sha_target_id(targetId);
			share.setS_sha_title(cwUtils.isEmpty(note) ? "" : note);
			share.setS_sha_uid(userEntId);
			share.setS_sha_url("");
			share.setS_sha_module(module);
			share.setS_sha_create_datetime(getDate());

			snsShareMapper.insert(share);

			// 更新统计信息
			long count = snsCountService.updateShareCount(targetId, module, userEntId, true);
			model.addAttribute("count", count);
			// 分享课程 ， 文章后，发布动态
			/*if(SNS.MODULE_COURSE.equals(module)
					|| SNS.MODULE_ARTICLE.equals(module) || SNS.MODULE_KNOWLEDGE.equals(module)
					){
				snsDoingService.add(targetId, 0, userEntId, 0, SNS.DOING_ACTION_SHARE, share.getS_sha_id(), module, "", 0);
			}*/

			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);

		} else {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.EXISTS);
		}
	}


	public void setSnsShareMapper(SnsShareMapper snsShareMapper) {
		this.snsShareMapper = snsShareMapper;
	}


	public SnsShare getByUserId(long targetId, long userEntId, String module) {
		SnsShare share = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetId", targetId);
		map.put("userEntId", userEntId);
		map.put("module", module);
		try{
			share =  this.snsShareMapper.getByUserId(map);
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
			// 由于页面上快速点击多次，可能导致同一个用户在一个对像下会有多条记录，在这种情况下会出错，所以先删除出错的数据，再重新取数据
			this.snsShareMapper.delErrorData(map);
			share =  this.snsShareMapper.getByUserId(map);
		}
		return  share;
	}
}