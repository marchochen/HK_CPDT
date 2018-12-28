package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.KbItemView;

public interface KbItemViewMapper extends BaseMapper<KbItemView> {
	public Long get(KbItemView kbItemView);
}