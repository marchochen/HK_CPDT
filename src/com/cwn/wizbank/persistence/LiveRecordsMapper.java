package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.LiveRecords;
import com.cwn.wizbank.utils.Page;


public interface LiveRecordsMapper extends BaseMapper<LiveRecords> {
	List<LiveRecords> getLiveRecordsByUsrAndLvId(Map<String, Object> map);
	List<LiveRecords> getLiveOnlineUser(Page<LiveRecords> page);
	int getLiveInvolvementTotal(Map<String, Object> map);
}
