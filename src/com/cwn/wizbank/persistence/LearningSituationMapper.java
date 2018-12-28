package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.LearningSituation;
import com.cwn.wizbank.utils.Page;

public interface LearningSituationMapper extends BaseMapper<LearningSituation> {
	
	LearningSituation selectLearningSituation(long usr_ent_id);
	
	LearningSituation selectLearningRankDetail(Map<String, Object> map);
	
	List<LearningSituation> selectLearningRankList(Page<LearningSituation> page);

	LearningSituation getlearningHistoryForMobile(long usr_ent_id);
	
	LearningSituation getUserLearnSituation(long usr_ent_id);
	
	//以下实时获取学习概况
	//获取个人概况总览
	LearningSituation selectDynamicLearningSituation(long usr_ent_id);
	//获取个人课程学习或者考试概况
	LearningSituation selectDynamicAeItemSituation(Map<String, Object> map);
	//获取个人关注概况
	LearningSituation selectDynamicAttentionSituation(long usr_ent_id);
	//获取个人赞概况
	LearningSituation selectDynamicPraiseSituation(long usr_ent_id);
	//获取个人收藏和分享概况
	LearningSituation selectDynamicCollectAndShareSituation(long usr_ent_id);
	//获取个人群组概况
	LearningSituation selectDynamicGroupSituation(long usr_ent_id);
	//获取个人问答概况
	LearningSituation selectDynamicKnowSituation(long usr_ent_id);
	//获取第一个回答我问题的人
	LearningSituation selectDynamicKnowSituationFirs(long usr_ent_id);
	//获取个人知识分享及浏览概况
	LearningSituation selectDynamicKnowledgeSituation(long usr_ent_id);
	//实时获取学习概况 end
}
