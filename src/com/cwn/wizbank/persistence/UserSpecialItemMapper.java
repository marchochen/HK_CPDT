package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.UserSpecialItem;

public interface UserSpecialItemMapper  extends BaseMapper<UserSpecialItem>{
	
	public Long add(UserSpecialItem userSpecialItem);
	public void deleteByUstId(long id);
	public List<UserSpecialItem> getItemByUstId(long ust_utc_id);
}
