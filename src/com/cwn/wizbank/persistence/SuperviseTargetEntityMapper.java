package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SuperviseTargetEntity;
import com.cwn.wizbank.utils.Page;
import org.apache.ibatis.annotations.Param;


public interface SuperviseTargetEntityMapper extends BaseMapper<SuperviseTargetEntity>{

	List<RegUser> selectSubordinateList(Page<RegUser> page);
	
	List<RegUser> selectMySupervise(long usr_ent_id);

	void delBySourceEntId(long usr_ent_id);

	Boolean isUserSuperviser(@Param("userEntId") long userEntId, @Param("superviserEntId") long superviserEntId);

	Boolean isUserGroupSuperviser(@Param("userEntId") long userEntId, @Param("superviserEntId") long superviserEntId);

    List<SuperviseTargetEntity> getDirectSupervise(Map<String, Object> map    );

    List<SuperviseTargetEntity> getGroupSupervise(Map<String, Object> map    );
    
}