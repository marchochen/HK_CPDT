package com.cwn.wizbank.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.entity.KbAttachment;

public interface KbAttachmentMapper extends BaseMapper<KbAttachment> {
	public List<KbAttachment> getKbAttachmentsByKbItemId(@Param("id") Long id);

	public List<KbAttachment> getInvalidAttachment();

	public void updateUrl(KbAttachment attachment);
}
