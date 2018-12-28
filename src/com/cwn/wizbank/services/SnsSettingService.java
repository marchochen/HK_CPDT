package com.cwn.wizbank.services;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.SnsSetting;
import com.cwn.wizbank.persistence.SnsSettingMapper;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class SnsSettingService extends BaseService<SnsSetting> {

	@Autowired
	SnsSettingMapper snsSettingMapper;

	public void setSnsSettingMapper(SnsSettingMapper snsSettingMapper){
		this.snsSettingMapper = snsSettingMapper;
	}
	
	/**
	 * 获取个人隐私设置
	 * @param my_usr_ent_id 我的用户id
	 * @param he_usr_ent_id 目标用户id
	 * @return
	 */
	public SnsSetting getSnsSetting(long my_usr_ent_id, long he_usr_ent_id){
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("my_usr_ent_id", my_usr_ent_id);
		map.put("he_usr_ent_id", he_usr_ent_id);
		return snsSettingMapper.selectSnsSet(map);
	}
	
	/**
	 * 保存隐私设置
	 * @param usr_ent_id 用户id
	 * @param snsSetting
	 * @return
	 */
	public void setMyPrivacySet(long usr_ent_id, SnsSetting snsSetting){
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("my_usr_ent_id", 0l);
		map.put("he_usr_ent_id", usr_ent_id);
		snsSetting.setS_set_uid(usr_ent_id);
		if(snsSettingMapper.selectSnsSet(map) == null){
			//第一次保存隐私设置，执行插入操作
			snsSetting.setS_set_create_uid(usr_ent_id);
			snsSetting.setS_set_create_datetime(getDate());
			snsSetting.setS_set_update_uid(usr_ent_id);
			snsSetting.setS_set_update_datetime(getDate());
			snsSettingMapper.insert(snsSetting);
		} else {
			snsSetting.setS_set_update_uid(usr_ent_id);
			snsSetting.setS_set_update_datetime(getDate());
			snsSettingMapper.update(snsSetting);
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setMyPrivacySet(long usr_ent_id,Page page) throws IllegalAccessException, InvocationTargetException{
		SnsSetting snsSetting = new SnsSetting();
		Map<String,Object> params = page.getParams();
		Set<String> keys = params.keySet();
		for(Iterator key = keys.iterator(); key.hasNext(); ){
			String fieldName = (String)key.next();
			if(isFiled(snsSetting.getClass(), fieldName)){
				BeanUtils.setProperty(snsSetting, fieldName, params.get(fieldName));
			}
		}
		setMyPrivacySet(usr_ent_id, snsSetting);
	}
	/**
	 * 判断javaBean中是否有此属性
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean isFiled(Class clazz,String fieldName){
		boolean flag=false;
		if(clazz!=null&&fieldName!=null){
			Method[] methods=clazz.getMethods();
			String methodName=null;
			for(Method method:methods){
				methodName=method.getName();
				if(methodName.length()>3&&(methodName.startsWith("get")||methodName.startsWith("set"))){								
				    if(methodName.substring(3).toLowerCase().equals(fieldName)){
					     flag=true;
				    }
				}
			}
		}
		return flag;
	}
}