package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.vo.AeItemVo;
import com.cwn.wizbank.utils.Page;


public interface AeItemMapper extends BaseMapper<AeItem>{

	/**
	 * 获取父类
	 * @param itmId
	 * @return
	 */
	AeItem getParent(long itmId);
	
	/**
	 * 有权限判断的获取
	 * @param itmId
	 * @return
	 */
	AeItem getAuthority(long itmId);

	/**
	 * 获取子类课程
	 * @param itmId
	 * @return
	 */
	List<AeItem> getChildrens(Map<String,Object> map);

	/**
	 * 学习页面，课程目录
	 * @param page
	 * @return
	 */
	List<AeItem> getCatalogCourse(Page<AeItem> page);
	
	/**
	 * 排行版
	 * @param page
	 * @return
	 */
	List<AeItem> getAeitemRank(Page<AeItem> page);
	/**
	 * 获取培训中心下的子课程
	 * @param map
	 * @return
	 */
	List<AeItem> getIntegratedChildren(Map<String,Object> map);
	
	/**
	 * 获取公开课的列表
	 * @param page
	 * @return
	 */
	List<AeItem> getOpens(Page<AeItem> page);
	
	
	/**
	 * 获取我的学习地图路径
	 * @param map
	 * @return
	 */
	List<AeItem> getLearningMap(Map<String,Object> map);
	
	
	
	AeItem getOpenPermission(Map<String,Object> map);
	
	
	AeItem getPermission(Page page);

	/**
	 * 获取最热课程
	 * @param page
	 */
	List<AeItem> getHotCourse(Page<AeItem> page);

	int getCatalogCount(Map<String, Object> map);
	
	/**
	 * 检测是否有该课程的学习权限
	 * @param page
	 */
	List<AeItem> checkAeItemCompetence(Page<AeItem> page);

	List<AeItem> pageAdmin(Page<AeItem> page);
	

	List<AeItem> pageInstrCos(Page<AeItem> page);
	

	
	long getItemCount(int itm_exam_ind);
	
	List<AeItem> getAeItemByPositionMap(Page<AeItem> page);
	
	List<AeItem> getAeItemBySpecialPage(Page<AeItem> page);
	
	List<AeItemVo> getAeItemByProfessionLrnItemPage(Map<String,Object> map);
	
	List<AeItem> pageAdminCourse(Page<AeItem> page);

	/**
	 * 获取培训管理员可管理的课程的数量
	 * @param usrEntId 用户Id
	 * @param isOpen 是否为公开课
	 * @param isExam 是否是考试
	 * @param itemType 课程类型
	 * @return
	 */
	long getAeItemTotalCountForTA(Map<String, Object> params);
	
	/**
	 * 获取指定课程下子课程id集合
	 */
	List getChItemIDList(Map<String, Object> params);
	
    AeItem checkAeitemExist(String itmCode);
    
    AeItem getParentAeitem(@Param("childItmId") String childItmId);
	
	
}