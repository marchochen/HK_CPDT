package com.cwn.wizbank.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserSpecialItem;
import com.cwn.wizbank.persistence.UserSpecialItemMapper;

/**
 * 专题对应课程service 实现
 */
@Service
public class UserSpecialItemService extends BaseService<UserSpecialItem> {

	@Autowired
	UserSpecialItemMapper userSpecialItemMapper;

	public void add(UserSpecialItem userSpecialItem) {
		userSpecialItemMapper.add(userSpecialItem);
	}

	public void deleteByUstId(long ust_utc_id) {
		userSpecialItemMapper.deleteByUstId(ust_utc_id);
	}

	public List<UserSpecialItem> getItemByUstId(long ust_utc_id) {
		return userSpecialItemMapper.getItemByUstId(ust_utc_id);
	}
}