package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AeItemCPDItem;

public interface AeItemCPDItemMapper extends BaseMapper<AeItemCPDItem>{

	public List<AeItemCPDItem> getAeItemCPDItem(Map map);
	
	public int getCountItemByCtID(long ct_id);
	
	public AeItemCPDItem getByItmId(long ct_id);
	
	public int insertReturnID(AeItemCPDItem aeItemCPDItem);
	
	public void updChildCodeByItmId(AeItemCPDItem aeItemCPDItem);
}
