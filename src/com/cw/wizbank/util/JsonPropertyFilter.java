package com.cw.wizbank.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import net.sf.json.util.PropertyFilter;

/**
 * FormBean的过滤类 通过调用类中的方法可以添加或者删除FormBean类中需要过滤掉的信息 默认是不过滤任何属性.
 * 
 * @author leeli
 * 
 */
public class JsonPropertyFilter implements PropertyFilter {
	
	
	// 存放设置的过滤项
	private HashMap property_filter;

	// 是否过滤掉空值的数据
	private boolean filter_null = true;

	/**
	 * 添加一个过滤项 传入的过滤项将会追加至原有的过滤项后面
	 * 
	 * @param field
	 *            :String 需过滤的field名称
	 */
	public void addFilter(String field) {
		if (property_filter == null) {
			property_filter = new HashMap();
		}
		property_filter.put(field, field);
	}

	/**
	 * 添加多个过滤项 传入的过滤项将会追加至原有的过滤项后面
	 * 
	 * @param fields
	 *            : String[] 需要过滤的field名称数组
	 */
	public void addFilters(String[] fields) {
		if (property_filter == null) {
			property_filter = new HashMap();
		}
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				property_filter.put(fields[i], fields[i]);
			}
		}
	}

	/**
	 * 添加多个过滤项 传入的过滤项将会追加至原有的过滤项后面
	 * 
	 * @param fields
	 *            : Field[] 需要过滤的Field数组
	 */
	public void addFilters(Field[] fields) {
		if (property_filter == null) {
			property_filter = new HashMap();
		}
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				property_filter.put(fields[i].getName(), fields[i].getName());
			}
		}
	}
	public void addFilters(List list) {
		if (property_filter == null) {
			property_filter = new HashMap();
		}
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				property_filter.put(list.get(i), list.get(i));
			}
		}
	}

	/**
	 * 删除一个过滤项
	 * 
	 * @param field
	 *            : String 需删除的过滤项名称
	 */
	public void removeFilter(String field) {
		if (property_filter != null && !property_filter.isEmpty()) {
			property_filter.remove(field);
		}
	}

	/**
	 * 删除多个过滤项
	 * 
	 * @param fields
	 *            : String[] 需删除的过滤项名称数组
	 */
	public void removeFilters(String[] fields) {
		if (property_filter != null && !property_filter.isEmpty()) {
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					property_filter.remove(fields[i]);
				}
			}
		}
	}

	/**
	 * 删除多个过滤项
	 * 
	 * @param fields
	 *            : Field[] 需删除的过滤项名称数组
	 */
	public void removeFilters(Field[] fields) {
		if (property_filter != null && !property_filter.isEmpty()) {
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					property_filter.remove(fields[i].getName());
				}
			}
		}
	}
	
	public void removeAllFilters(){
		if (property_filter != null && !property_filter.isEmpty()) {
			property_filter.clear();
		}
	}

	/**
	 * 设置是否过滤值为"null"的属性 默认不过滤.
	 * 
	 * @param filterNull
	 */
	public void setFilterNullValue(boolean filterNull) {
		filter_null = filterNull;
	}

	/**
	 * 接口中的方法实法
	 */
	public boolean apply(Object source, String name, Object value) {
		// TODO Auto-generated method stub
		if ((filter_null && value == null )
				|| (property_filter != null && property_filter.containsKey(name))) {
			return true;
		}
		return false;
	}

}
