/**
 * 
 */
package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.vo.SearchResultVo;
import com.cwn.wizbank.utils.Page;



/**
 * @author leon.li
 * 2014-7-31 上午10:24:14
 */
public interface SearchMapper {
	
	public List<SearchResultVo> search(Page<SearchResultVo> page);
	
}
