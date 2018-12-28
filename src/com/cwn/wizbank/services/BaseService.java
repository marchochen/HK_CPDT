/**
 * 
 */
package com.cwn.wizbank.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.persistence.BaseMapper;
import com.cwn.wizbank.utils.Page;

/**
 * @author leon.li
 * 2014-8-7 下午3:02:27
 */
public class BaseService<T> {

	@Autowired
	BaseMapper<T> baseMapper;
	
	public Page<T> page(Page<T> page){
		page.setResults(baseMapper.page(page));
		return page;
	}
	
	public void update(T t) {
		baseMapper.update(t);
	}

	public T get(Object id) {
		return baseMapper.get(id);
	}

	public void add(T t) {
		baseMapper.insert(t);
	}

	public void delete(long id) {
		baseMapper.delete(id);
	}

	public List<T> list(Map<String,Object> map) {
		return baseMapper.list(map);
	}
	
	public Timestamp getDate() {
		return baseMapper.getDate();
	}
	
}
