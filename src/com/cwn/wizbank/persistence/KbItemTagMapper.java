package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.KbItemTag;

public interface KbItemTagMapper extends BaseMapper<KbItemTag> {
	public void delKnowledgeTag(long id);
}