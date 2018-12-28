package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.UserCredits;
import com.cwn.wizbank.entity.UserCreditsDetailLog;
import com.cwn.wizbank.utils.Page;

public interface UserCreditsMapper extends BaseMapper<UserCredits>{
	
	List<UserCredits> selectRankList(Page<UserCredits> page);
	
	UserCredits selectUserCreditAndRank(UserCredits userCredits);
	
	List<UserCreditsDetailLog> selectUserCreditDetailList(Page<UserCreditsDetailLog> page);
	
	UserCreditsDetailLog selectUserTotalCredits(UserCreditsDetailLog userCreditsDetailLog);
	
}