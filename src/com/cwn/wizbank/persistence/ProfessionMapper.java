package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.Profession;
import com.cwn.wizbank.utils.Page;

public interface ProfessionMapper  extends BaseMapper<Profession>{
	
	public List<Profession> getAllPfsList();
	
	public Long getCurUserUgrId(Map<String, Object> map);

	public String getUgrTitleByUgrId(Long ugr_ent_id);
	
	public List<Profession> getAllPfsPage(Page<Profession> page);
	
	public Long add(Profession profession);
	
	public List<Profession> getAllProfessionPage(Page<Profession> page);
	
	public void updateStatus(Map<String, Object> map);
	
	public List<Profession> getAll(Map<String, Object> map);
	
	public boolean isExistFormProp(Map<String, Object> map);
	
	public int getAffectedPfs(Long id);
}
