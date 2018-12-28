/**
 * 
 */
package com.cwn.wizbank.persistence;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.cwn.wizbank.utils.Page;

/**
 * @author leon.li
 * 2014-7-31 上午10:24:14
 */
public interface BaseMapper<T> {
	
	public T get(Object id);
	
	public List<T> page(Page<T> page);
	
	public void update(T t);
	
	public void insert(T t);
	
	public void delete(long id);
	
	public List<T> list(Map<String,Object> map);
	
	public Timestamp getDate();
	
	public boolean hasRolFtn(Map<String, Object> map);
	
}
