package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.TcRelation;

public interface TcRelationMapper  extends BaseMapper<TcRelation> {
	List<TcRelation> getListByChildId(long childTcr_id);

}
