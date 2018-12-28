package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.utils.Page;

public interface KbCatalogMapper extends BaseMapper<KbCatalog> {

	public List<KbCatalog> selectPage(Page<KbCatalog> page);

	public List<Map<String, Object>> jsonList(@Param("filter") String filter, @Param("usr_ent_id") long usr_ent_id, @Param("current_role") String current_role);

	public List<KbCatalog> selectCatByTcr(Map<String, Object> map);

	public void publish(KbCatalog kbCatalog);

	public boolean isExist(KbCatalog kbCatalog);

	public Long getTmpCatalogByTopTcr(long my_top_tc_id);

	public void insertTmpCatalogInTopTcr(long my_top_tc_id);

	public boolean hasApprovedKb(KbCatalog kbCatalog);
	
	

}