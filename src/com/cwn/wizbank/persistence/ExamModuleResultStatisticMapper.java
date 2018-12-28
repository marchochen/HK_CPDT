package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.vo.ExamModuleResultStatistic;


public interface ExamModuleResultStatisticMapper extends BaseMapper<ExamModuleResultStatistic>{

	/**
	 * 通过模块id获取报名该模块对应课程的人数
	 * @param mod_id
	 * @return
	 */
	int selectAeApplicationCount(long mod_id);

	/**
	 * 根据模块id获取提交的总次数和提交的人数
	 * @param mod_id
	 * @return
	 */
	Map<String, Number> selectCommitCountInfo(long mod_id);

	/**
	 * 获取模块信息，包括 mod_max_score（试卷总分）,mod_pass_score（通过分数的百分比）
	 * @param mod_id
	 * @return
	 */
	Map<String, Number> selectModuleInfo(long mod_id);

	/**
	 * 查询Progress列表，获取模块用户每次提交的情况列表
	 */
	List<Map<String, Object>> selectcommitInfoList(long mod_id);

	/**
	 * 查询Resources表，获取模块标题
	 * @param mod_id
	 * @return
	 */
	String selectModuleTitle(long mod_id);

	/**
	 * 获取课程信息
	 * @param mod_id
	 * @return
	 */
	Map<String, Object> selectItemInfo(long mod_id);

	String selectItemParentTitle(long itm_id);
	
}