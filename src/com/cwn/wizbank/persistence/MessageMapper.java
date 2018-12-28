package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cw.wizbank.JsonMod.Ann.bean.ReceiptBean;
import com.cwn.wizbank.entity.Message;


public interface MessageMapper extends BaseMapper<Message>{

	public List<Message> getMessagesByResId(Map<String, Object> map);

	public int getNewCount(Map<String, Object> map);
	
	public long getRecUsgId(long rec_ent_id);

	public void insReceipt( ReceiptBean rec);

	public Integer getReceipt(Map<String, Object> map);
	
	public Integer getIsOrNotReceipt(long msgId);
	
	public Integer getNotReceipt(Map<String, Object> map);

	public int getNotReadCountByUsrId(Map<String, Object> map);

}