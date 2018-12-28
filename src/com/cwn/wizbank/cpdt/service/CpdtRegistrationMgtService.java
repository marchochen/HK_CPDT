package com.cwn.wizbank.cpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.cpdt.persistence.CpdtRegistrationMapper;
import com.cwn.wizbank.cpdt.entity.CpdtType;
import com.cwn.wizbank.services.BaseService;

@Service
public class CpdtRegistrationMgtService extends BaseService<CpdtRegistrationMapper> {

	@Autowired
	CpdtRegistrationMapper cpdtRegistrationMapper;
	
    /**
     * 获取大牌列表
     * @return
     */
    public List<CpdtType> getCpdtType(){
        List<CpdtType> cpdtType = cpdtRegistrationMapper.getCpdtType();
        return cpdtType;
    }
}
