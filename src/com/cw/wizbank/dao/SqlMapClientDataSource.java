package com.cw.wizbank.dao;

/*
 * 该结果公布了用户对数据库的操作
 * @author:wrren
 * 
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cw.wizbank.dao.pagination.AllPaginationInfors;

public interface SqlMapClientDataSource {
	/*
	 * 改方法用于获取单条数据库记录，并且把它适配成一个pojo类的实例对象
	 */
	public Map getObject(Connection connection, String sql, Object[] params);

	/*
	 * 改方法用于获取多条数据库记录，把每条记录都适配成相应的pojo类的实例对象，最后以List集合返回
	 */
	public List getObjectList(Connection connection, String sql, Object[] params);

	/*
	 * 改方法用于获取指定数量数据库记录，把每条记录都适配成相应的pojo类的实例对象，最后以List集合返回
	 */
	public List getObjectList(Connection connection, String sql, Object[] params, long specifyMaxCount);

	/*
	 * 查询数据库中的单条记录列表
	 */
	public List getsingleColumnList(Connection connection, String sql, Object[] params, Class objectType);

	/*
	 * 获取数据库中单个字段的值
	 */
	public Object getsingleColumn(Connection connection, String sql, Object[] params, Class objectType);

	/*
	 * 改方法分页查询多条记录，把每条记录都适配成相应的pojo类的实例对象，最后以List集合返回
	 */
	public AllPaginationInfors getObjectPaginationList(Connection connection, String sql, Object[] params, HttpServletRequest request);

	/*
	 * 该方法用于获取单条记录，并且把它适配成一个XML字符串
	 */
	public String getObjectAdapterToXmlData(Connection connection, String sql, Object[] params, String elementName);

	/*
	 * 该方法用于将数据库中的记录适配成xml字符串
	 */
	public String getObjectListAdapterToxmlData(Connection connection, String sql, 
			Object[] params, String fatherElementPath, String childElementPath);

	/*
	 * 该方法用于分页查询数据库中的记录适配成xml字符串
	 */
	public String getPaginationDataAdapterToxmlData(Connection connection, String sql, Object[] params, 
			String fatherElementPath, String childElementPath, HttpServletRequest request);

	/*
	 * 该方法用于执行DML操作的SQL语句
	 */
	public int executeUpdate(Connection connection, String sql, Object[] params);

	/*
	 * 该方法用于批量执行DML操作的SQL语句
	 */
	public int[] executeBatchUpdate(Connection connection, String sql, List paramsList);

	/*
	 * 该方法用于调用存储过程和函数
	 */
	public Map exeProcedure(Connection connection, String sql, Object[] params);
	
	/*
	 * 插入一条记录并且返回它的主键的值
	 */
	public long executeSave(Connection connection, String sql, Object[] params, String tableName, String primaryColumnValue);
	
	/*
	 * 插入多条记录并且把他们主键的值一List集合的形式返回
	 */
	public List executeSaveLst(Connection connection, String sql, List paramsList, String tableName, String primaryColumnValue);
	
	/*
	 * 获取数据库中的结果集，主要用于报表
	 */
	public PreparedStatement getPreparedStatement(Connection connection, String sql, Object[] params);
	
	/*
	 * 将分页信息适配成xml字符串
	 */
	public String adapterPaginationInforsToXMLData(AllPaginationInfors allPaginationInfors, String fatherElementPath, String childElementPath);
	
	/*
	 * 将一个List<Map<String,Object>>集合适配成xml数据
	 */
	public String getXmlDataFromObjectList(List objectList, String fatherElementPath, String childElementPath);
	
	/*
	 * 将一个Map集合适配成XML数据
	 */
	public String getXmlDataFromMap(Map atributes, String elementName);
	
	public String getStringFromObject(Object object);
}
