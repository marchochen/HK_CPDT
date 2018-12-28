package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.entity.Tag;
import com.cwn.wizbank.utils.Page;

public interface TagMapper extends BaseMapper<Tag> {

	public List<Tag> searchAll(Page<Tag> page);

	public List<Map<String, Object>> jsonList(@Param("filter") String filter, @Param("usr_ent_id") long usr_ent_id,@Param("current_role") String current_role);

	public List<Tag> selectTagByTcr(Map<String, Object> map);

	public boolean isExistForTitle(Tag tag);

}
