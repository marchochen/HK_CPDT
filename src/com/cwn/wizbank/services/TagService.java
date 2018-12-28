package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.Tag;
import com.cwn.wizbank.persistence.KbItemTagMapper;
import com.cwn.wizbank.persistence.TagMapper;
import com.cwn.wizbank.utils.Page;

@Service
public class TagService extends BaseService<Tag> {

	@Autowired
	TagMapper tagMapper;

	@Autowired
	KbItemTagMapper kbItemTagMapper;

	public boolean isExistForTitle(Tag tag) {
		return tagMapper.isExistForTitle(tag);
	}

	public Tag getTagDetail(long tag_id) {
		Tag tag = tagMapper.get(tag_id);
		return tag;
	}

	public Page<Tag> searchAll(Page<Tag> page, loginProfile prof) {
		if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
			page.getParams().put("usr_ent_id", prof.root_ent_id);
			page.getParams().put("current_role", AcRole.ROLE_ADM_1);
		}else{
			page.getParams().put("usr_ent_id", prof.usr_ent_id);
			page.getParams().put("current_role", prof.current_role);
		}
		tagMapper.searchAll(page);
		return page;
	}

	public List<Map<String, Object>> treeJsonList(String filter, loginProfile prof) throws Exception {
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			return tagMapper.jsonList("%" + filter + "%", prof.usr_ent_id, prof.current_role);
		}else{
			return tagMapper.jsonList("%" + filter + "%", prof.root_ent_id, AcRole.ROLE_ADM_1);
		}
		
	}

	public List<Tag> getTagByTcr(WizbiniLoader wizbini, loginProfile prof, String kbi_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usr_ent_id", prof.usr_ent_id);
		map.put("kbi_id", kbi_id);
		map.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		return tagMapper.selectTagByTcr(map);
	}

	public void delete(Tag tag) {
		kbItemTagMapper.delKnowledgeTag(tag.getTag_id());
		tagMapper.delete(tag.getTag_id());
	}

	public void saveOrUpdate(Tag tag, loginProfile prof) {
		tag.setTag_title(tag.getTag_title().trim());
		if (tag.getTag_id() == null || tag.getTag_id().equals("")) {
			tag.setTag_create_datetime(super.getDate());
			tag.setTag_create_user_id(prof.usr_id);
			super.add(tag);
		} else {
			tag.setTag_update_datetime(super.getDate());
			tag.setTag_update_user_id(prof.usr_id);
			super.update(tag);
		}
	}

	public List<Tag> getTagByIds(Long[] ids) {
		List<Tag> list = new ArrayList<Tag>();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				list.add(tagMapper.get(Long.valueOf(ids[i])));
			}
		}
		return list;
	}
}
