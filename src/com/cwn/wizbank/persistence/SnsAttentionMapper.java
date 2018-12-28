package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SnsAttention;
import com.cwn.wizbank.utils.Page;


public interface SnsAttentionMapper extends BaseMapper<SnsAttention>{

	List<RegUser> selectUserList(Page<RegUser> page);
	
	void delete(SnsAttention snsAttention);
	
	List<RegUser> findUserList(Page<RegUser> page);
	
	long selectSnsAttentionTotal(SnsAttention snsAttention);
	
}