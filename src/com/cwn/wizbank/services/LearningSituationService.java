package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.LearningSituation;
import com.cwn.wizbank.persistence.LearningSituationMapper;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;

@Service
public class LearningSituationService extends BaseService<LearningSituation> {

	@Autowired
	LearningSituationMapper learningSituationMapper;
	
	public void setLearningSituationMapper(LearningSituationMapper learningSituationMapper){
		this.learningSituationMapper = learningSituationMapper;
	}
	
	/**
	 * 获取个人学习概况信息
	 * @param usr_ent_id 用户id
	 * @return
	 */
	public LearningSituation getLearningSituation(long usr_ent_id){
		return learningSituationMapper.selectLearningSituation(usr_ent_id);
	}
	
	/**
	 * 获取实时个人学习概况信息
	 * @param usr_ent_id 用户id
	 * @return
	 */
	public LearningSituation getDynamicLearningSituation(long usr_ent_id){
		LearningSituation learningSituation = null;
		//获取个人概况总览
		learningSituation = learningSituationMapper.selectDynamicLearningSituation(usr_ent_id);
		
		if(learningSituation == null){
			learningSituation = new LearningSituation();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usr_ent_id", usr_ent_id);
		//获取个人课程学习概况
		map.put("itm_exam_ind", false);
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicAeItemSituation(map));
		//获取个人考试概况
		map.put("itm_exam_ind", true);
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicAeItemSituation(map));
		//获取个人关注概况
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicAttentionSituation(usr_ent_id));
		//获取个人赞概况
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicPraiseSituation(usr_ent_id));
		//获取个人收藏和分享概况
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicCollectAndShareSituation(usr_ent_id));
		//获取个人群组概况
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicGroupSituation(usr_ent_id));
		//获取个人问答概况
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicKnowSituation(usr_ent_id));
		//获取第一个回答我问题的人
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicKnowSituationFirs(usr_ent_id));
		//获取个人知识分享及浏览概况
		learningSituation.setLearningSituationValue(learningSituation, learningSituationMapper.selectDynamicKnowledgeSituation(usr_ent_id));
		
		learningSituation.setDefaultZero(learningSituation);
		//System.out.println(learningSituation);
		
		if(learningSituation.ls_ent_id == null){
			return null;
		}	
		return learningSituation;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LearningSituation getLearningRankDetail(Page page){
		LearningSituation learningSituation = learningSituationMapper.selectLearningRankDetail(page.getParams());
		if (learningSituation != null && learningSituation.getUser() != null) {
			ImageUtil.combineImagePath(learningSituation.getUser());
		}
		return learningSituation;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getLearningRankList(Page page){
		List<LearningSituation> list = learningSituationMapper.selectLearningRankList(page);
		for( LearningSituation learningSituation : list){
			ImageUtil.combineImagePath(learningSituation.getUser());
		}
		page.setResults(list);
		return page;			
	}

	/**
	 * 移动端获取用户学习足迹信息
	 * @param usr_ent_id
	 * @return
	 */
	public LearningSituation getlearningHistoryForMobile(long usr_ent_id) {
		return learningSituationMapper.getlearningHistoryForMobile(usr_ent_id);
	}
	
	
	/**
	 * 获取个人学分
	 * @param usr_ent_id 用户id
	 * @return
	 */
	public float getUserLearnCredits(long usr_ent_id){
		LearningSituation learningSituation = learningSituationMapper.selectLearningSituation(usr_ent_id);
		return learningSituation != null && learningSituation.getLs_learn_credit() > 0 ?  CwnUtil.formatNumber(learningSituation.getLs_learn_credit(), 2) : 0;
	}
}
