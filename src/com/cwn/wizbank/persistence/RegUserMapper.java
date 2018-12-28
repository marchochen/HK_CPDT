package com.cwn.wizbank.persistence;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.vo.UserVo;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;


public interface RegUserMapper extends BaseMapper<RegUser>{

	RegUser selectUserDetail(RegUser regUser);
	
	void register(RegUser regUser);
	
	RegUser checkUserExist(RegUser regUser);
	
	Long checkUserNameCount(RegUser regUser);
	
	Timestamp selectLastLoginTime(long userEntId);

	RegUser getByUsrId(String id);
	
	RegUser getByUsrSteUsrId(String usr_ste_id);
	
	List<UserVo> getUserList(Params params);
	
	boolean isUserExist(String usrSteUsrId);

	RegUser getByLoginId(String usr_login_id);

    RegUser getUserBySteId(String usrSteUsrId);
	
	void delByLoginId(String usrSteUsrId);
	
	List<RegUser> getInstructors(Page<RegUser> page);
	
	List<RegUser> getUserByIds(List<String> list);
	
    List<RegUser> findsuperviseForCpdExcel(Map<String, Object> map);
	
	String getLoginId(Long userEntId);

	RegUser getInstructor(long id);
	void changeStatus(Map<String, Object> map	);


	long getActiveUserCount();

	List<RegUser> findUserList(Page<RegUser> page);

	long getAllUserCount();


	void updateForDel(Map<String, Object> map);

	/**
	 * 获取培训中心【tcrId】下的用户数量
	 * @param tcrId 培训中心
	 * @return
	 */
	long getRegUserCountByTcrId(Map<String, Object> params);

	List<RegUser> getDelUptAffectUsr(Map<String, Object> map);
	
	Timestamp getLastLoginDate(long usr_ent_id);

	Boolean selectUsrPwdNeedChangeInd(long usr_ent_id);

	Timestamp selectUsrPwdUpdTimestamp(long usr_ent_id);

	List<RegUser> getPageUserByGroupId(Page<RegUser> page);
	
	List<RegUser> getUserInfoByGroupOrUserId(Map<String,Object> map);
	
	List<Map> getUgrAndUsgByUsrId();
    
    List<RegUser> findUserListByIds(Map map);
    
    List<RegUser> findUserListByIdsHistory(Map map);
    
    List<RegUser> findUserCpdRegListById(Map map);

    List<RegUser> findUserCpdRegListByIdHistory(Map map);

    List<RegUser> findUserListForCpdExcel(Map<String,Object> map);

    List<RegUser> findUserListForCpdExcelHistory(Map<String,Object> map);
    
    List<RegUser> findUserListForCpdExcelTa(Map<String,Object> map);
    
    List<RegUser> findUserListForCpdTa(Map<String,Object> map);
    
    List<RegUser> findUserListForCpdHistoryTa(Map<String,Object> map);

	/**保存用户的关系信息到历史表
	 * @param map
	 */
	void insertUserRelationHistory(Map<String, Object> map);
	
}