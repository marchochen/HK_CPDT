package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.UserPasswordHistory;


public interface UserPasswordHistoryMapper extends BaseMapper<UserPasswordHistory>{

	List<UserPasswordHistory> selectListByUsrEntId(long usr_ent_id);

}
