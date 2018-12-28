package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.ModuleTempFile;
import com.cwn.wizbank.entity.vo.AttachmentVo;


public interface ModuleTempFileMapper extends BaseMapper<ModuleTempFile>{

	List<ModuleTempFile> getList(Map<String, Object> map);

	void updateList(Map<String, Object> map);

	void deleteList(Map<String, Object> map);

	List<AttachmentVo> getAttachmentList(Map<String, Object> map);

}