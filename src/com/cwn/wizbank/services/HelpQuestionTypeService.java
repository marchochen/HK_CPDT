package com.cwn.wizbank.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.HelpQuestionType;
import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.entity.vo.HelpQuestionTypeVo;
import com.cwn.wizbank.persistence.HelpQuestionTypeMapper;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

@Service
public class HelpQuestionTypeService extends BaseService<HelpQuestionType>{
	
	@Autowired
	HelpQuestionTypeMapper helpQuestionTypeMapper;
	
	public List<HelpQuestionTypeVo> getQuestinTypes(Params params){
		return helpQuestionTypeMapper.getQuestionTypeList(params);
	}
	
	public HelpQuestionType save(HelpQuestionType type){
		Date date=new Date(System.currentTimeMillis());
		type.setHqt_create_timestamp(date);
		type.setHqt_pid(0);
		helpQuestionTypeMapper.insert(type);
		return type;
	}
	
	public HelpQuestionTypeVo getHelpQuestionType(Integer id){
		return helpQuestionTypeMapper.getHelpQuestionType(id);
	}
	
	public void deleteHelpQuestionType(Integer id){
		helpQuestionTypeMapper.delHelpQuestionType(id);
	}
	
	/**
	 * 获取类型列表
	 * @param page
	 * @param prof
	 * @return
	 */
	public Page<HelpQuestionTypeVo> listPage(Page<HelpQuestionTypeVo> page, loginProfile prof) {
		if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
			page.getParams().put("usr_ent_id", prof.root_ent_id);
			page.getParams().put("current_role", AcRole.ROLE_ADM_1);
		}else{
			page.getParams().put("usr_ent_id", prof.usr_ent_id);
			page.getParams().put("current_role", prof.current_role);
		}
		helpQuestionTypeMapper.getTypeList(page);
		return page;
	} 
	

}
