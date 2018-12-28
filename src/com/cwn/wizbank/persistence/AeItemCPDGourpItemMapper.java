package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AeItemCPDGourpItem;

public interface AeItemCPDGourpItemMapper extends BaseMapper<AeItemCPDGourpItem>{

	public List<AeItemCPDGourpItem> getAeItemCPDGourpItem (Map map);
  
	public int getCountItemByCgID(long cg_id);
}
