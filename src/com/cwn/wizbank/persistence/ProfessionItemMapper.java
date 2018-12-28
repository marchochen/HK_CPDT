package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.ProfessionItem;
import com.cwn.wizbank.utils.Page;

public interface ProfessionItemMapper  extends BaseMapper<ProfessionItem>{
	
	
	public Long add(ProfessionItem professionItem); 
	public void deleteByPfsId(long id); 
	public ProfessionItem getPsiByPfsid(long id); 
	public List<ProfessionItem> pageItem(Page<ProfessionItem> page);
}
