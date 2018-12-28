package com.cwn.wizbank.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserPasswordHistory;
import com.cwn.wizbank.persistence.UserPasswordHistoryMapper;


/**
 * 
 * 用户历史密码保存服务
 * @author andrew.xiao
 *
 */
@Service
public class UserPasswordHistoryService extends BaseService<UserPasswordHistory>{

	@Autowired
	UserPasswordHistoryMapper passwordHistoryMapper;
	
	/**
	 * 判断用户@param usr_ent_id的新密码@param newPassword是否在最近的rangeCount已经存在了
	 * @param usr_ent_id
	 * @param pASSWORD_POLICY_COMPARE_COUNT
	 * @param newPassword
	 * @return
	 */
	public boolean isExistPwd(long usr_ent_id,
			int rangeCount, String newPassword) {
		
		List<UserPasswordHistory> list = this.getUserPasswordList(usr_ent_id);
		if(list==null || list.size() == 0){
			return false;
		}
		
		for(int i=0;i<list.size()&&i<rangeCount;i++){
			if(newPassword.equals(list.get(i).getUph_pwd())){
				return true;
			}
		}
		return false;
	}

	private List<UserPasswordHistory> getUserPasswordList(long usr_ent_id) {
		return passwordHistoryMapper.selectListByUsrEntId(usr_ent_id);
	}
	
}
