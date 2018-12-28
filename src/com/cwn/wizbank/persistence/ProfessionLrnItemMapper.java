package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.ProfessionLrnItem;
import com.cwn.wizbank.entity.vo.GradeNodesVo;

public interface ProfessionLrnItemMapper  extends BaseMapper<ProfessionLrnItem>{
	
	
	public Long add(ProfessionLrnItem professionLrnItem); 
	public void deleteByPfsId(long id); 
	public List<ProfessionLrnItem> getPsiByPfsid(long id); 
	public List<Long> getGradeIdsByPfsId(long id); 
	public List<ProfessionLrnItem> getItemByGradeId(Map<String,Object> map); 
	public List<GradeNodesVo> getItemFrontByPfsId(long id); 
}
