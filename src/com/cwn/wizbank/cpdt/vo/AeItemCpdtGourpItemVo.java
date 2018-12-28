package com.cwn.wizbank.cpdt.vo;

import com.cwn.wizbank.cpdt.entity.AeItemCpdtGourpItem;
import com.cwn.wizbank.cpdt.entity.AeItemCpdtItem;

public class AeItemCpdtGourpItemVo extends AeItemCpdtGourpItem {

	private static final long serialVersionUID = 8764869037207624115L;

	private AeItemCpdtItem aeItemCpdtItem;

	public AeItemCpdtItem getAeItemCpdtItem() {
		return aeItemCpdtItem;
	}

	public void setAeItemCpdtItem(AeItemCpdtItem aeItemCpdtItem) {
		this.aeItemCpdtItem = aeItemCpdtItem;
	}
	
	
	
}
