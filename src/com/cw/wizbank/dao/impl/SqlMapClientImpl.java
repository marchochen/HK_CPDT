package com.cw.wizbank.dao.impl;

/*
 * 该类封装了jdbc操作
 * @author:wrren
 * 
 */


import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.cw.wizbank.dao.Order;
import com.cw.wizbank.dao.OutParam;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.dao.File.BigData;
import com.cw.wizbank.dao.exception.AdapterElementException;
import com.cw.wizbank.dao.exception.SQLStatementProcessException;
import com.cw.wizbank.dao.pagination.AllPaginationInfors;
import com.cw.wizbank.dao.pagination.ProcessPagination;
import com.cw.wizbank.dao.Log4jFactory;

public class SqlMapClientImpl implements SqlMapClientDataSource {
	private static final Pattern patternPrams = Pattern
			.compile("(\\s)+[Oo][Rr][Dd][Ee][Rr](\\s)+[Bb][Yy](\\s)+");
	private static final Logger logger = Log4jFactory
			.createLogger(com.cw.wizbank.dao.impl.SqlMapClientImpl.class);
	/*
	 * 是否显示sql语句
	 */
	private boolean showSql = false;
	/*
	 * 该对象主要用来帮助该对象进行数据适配
	 */
	private SqlMapClientHelper sqlMapClientHelper = null;
	
	public SqlMapClientImpl (ProcessPagination processPagination){
		this.sqlMapClientHelper = SqlMapClientHelper.getInstance();
		this.sqlMapClientHelper.setProcessPagination(processPagination);
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
	
	public boolean isShowSql() {
		return showSql;
	}

	public void setProcessPagination(ProcessPagination processPagination) {
		this.sqlMapClientHelper.setProcessPagination(processPagination);
	}

	public String adapterPaginationInforsToXMLData(AllPaginationInfors allPaginationInfors, String fatherElePath, String childElePath) {
		try {
			return sqlMapClientHelper.adapterDataToXMLElement(
					allPaginationInfors, fatherElePath, childElePath);
		} catch (RuntimeException e) {
			logger.error("adapter Element error.", e);
			throw new AdapterElementException("adapter Element error.");
		}
	}

	public Map exeProcedure(Connection connection, String sql, Object[] params) {
		CallableStatement callStatement = null;
		try {
			Map outresult = new HashMap();
			List listoutparams = new ArrayList();
			if (showSql)
				logger.info(sql);
			callStatement = connection.prepareCall(sql);
			if (params != null) {
				for (int n = 0; n < params.length; n++) {
					if (params[n] instanceof OutParam) {
						OutParam outParam = (OutParam) params[n];
						callStatement.registerOutParameter(n + 1, outParam
								.getValue());
						outParam.setIndex(n + 1);
						listoutparams.add(outParam);

					} else {
						callStatement.setObject(n + 1, params[n]);
					}
				}
			}

			callStatement.execute();

			if (!listoutparams.isEmpty()) {
				for (Iterator iterator = listoutparams.iterator(); iterator
						.hasNext();) {
					OutParam outParam = (OutParam) iterator.next();
					outresult.put(outParam.getKey(), callStatement
							.getObject(outParam.getIndex()));
				}
				return outresult;
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(null, callStatement);
		}
	}

	public int[] executeBatchUpdate(Connection connection, String sql, List paramsList) {
		if(paramsList != null && !paramsList.isEmpty()){
			PreparedStatement preStatement = null;
			try {
				preStatement = this.sqlMapClientHelper.createCommonStatement(connection, sql, (Object[])paramsList.get(0), showSql);
				preStatement.addBatch();
				if (paramsList != null && paramsList.size() > 1) {
					for (int n = 1; n < paramsList.size(); n++) {
						List paramList = this.sqlMapClientHelper.getListFromParams((Object[])paramsList.get(n));
						for (int i = 0; i < paramList.size(); i++) {
							Object objectParams = paramList.get(i);
							if(objectParams instanceof com.cw.wizbank.dao.File.BigData){
								BigData bigData = (BigData) objectParams;
								InputStream inputStream = bigData.getInputStrean();
								preStatement.setBinaryStream(i + 1, inputStream, inputStream.available());
							}else{
								preStatement.setObject(i + 1, objectParams);
							}
						}
						preStatement.addBatch();
					}
				}
				return preStatement.executeBatch();
			} catch (Exception e) {
				logger.error("sqlstatement execute error.\n" + sql, e);
				throw new SQLStatementProcessException(
						"sqlstatement execute error.\n" + sql);
			} finally {
				this.closeSqlStatement(null, preStatement);
			}
		}else{
			return null;
		}
	}

	public int executeUpdate(Connection connection, String sql, Object[] params) {
		PreparedStatement preStatement = null;
		try {
			int flag = 0;
			preStatement = this.sqlMapClientHelper.createCommonStatement(
					connection, sql, params, showSql);
			flag = preStatement.executeUpdate();
			
			return flag;
		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(null, preStatement);
		}
	}
	
	public  long executeSave(Connection connection, String sql, Object[] params, String tableName, String primaryColumnValue) {
		PreparedStatement preStatement = null;
		PreparedStatement getPrimarykeyPrepared = null;
		ResultSet getPrimaryKeyResultSet = null;
		try {
			preStatement = this.sqlMapClientHelper.createCommonStatement(connection, sql, params, showSql);
			preStatement.executeUpdate();
			
			getPrimarykeyPrepared = connection.prepareStatement("SELECT MAX(" + primaryColumnValue + ") AS PRI_ID FROM " + tableName);
			getPrimaryKeyResultSet = getPrimarykeyPrepared.executeQuery();
			if(getPrimaryKeyResultSet.next()){
				return getPrimaryKeyResultSet.getLong("PRI_ID");
			}
			return 0;
		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(null, preStatement);
			this.closeSqlStatement(getPrimaryKeyResultSet, getPrimarykeyPrepared);
		}
	}
	
	public  List executeSaveLst(Connection connection, String sql, List paramsList, String tableName, String primaryColumnValue){
		if(paramsList != null && !paramsList.isEmpty()){
			PreparedStatement preStatement = null;
			PreparedStatement getPrimaryPreparedStatement = null;
			ResultSet getPrimaryResultSet = null;
			try {
				preStatement = this.sqlMapClientHelper.createCommonStatement(connection, sql, (Object[])paramsList.get(0), showSql);
				preStatement.addBatch();
				if (paramsList != null && paramsList.size() > 1) {
					for (int n = 1; n < paramsList.size(); n++) {
						List paramList = this.sqlMapClientHelper.getListFromParams((Object[])paramsList.get(n));
						for (int i = 0; i < paramList.size(); i++) {
							Object objectParams = paramList.get(i);
							if(objectParams instanceof com.cw.wizbank.dao.File.BigData){
								BigData bigData = (BigData) objectParams;
								InputStream inputStream = bigData.getInputStrean();
								preStatement.setBinaryStream(i + 1, inputStream, inputStream.available());
							}else{
								preStatement.setObject(i + 1, objectParams);
							}
						}
						preStatement.addBatch();
					}
				}
				preStatement.executeBatch();
				
				List primary_key_list = new ArrayList();
				long max_primary_key_value = 0;
				getPrimaryPreparedStatement = connection.prepareStatement("SELECT MAX(" + primaryColumnValue + ") AS PRI_ID FROM " + tableName);
				getPrimaryResultSet = getPrimaryPreparedStatement.executeQuery();
				if(getPrimaryResultSet.next()){
					max_primary_key_value = getPrimaryResultSet.getLong("PRI_ID");
				}
				for (long i = paramsList.size() - 1; i >=0; i--) {
					primary_key_list.add(new Long(max_primary_key_value - i));
				}
				return primary_key_list;
			} catch (Exception e) {
				logger.error("sqlstatement execute error.\n" + sql, e);
				throw new SQLStatementProcessException(
						"sqlstatement execute error.\n" + sql);
			} finally {
				this.closeSqlStatement(null, preStatement);
			}
		}else{
			return null;
		}
	}

	public String getObjectListAdapterToxmlData(Connection connection, String sql,
			Object[] params, String fatherElementPath, String childElementPath) {
		List resultSetMapList = this.getObjectList(connection, sql, params);
		try {
			return sqlMapClientHelper.adapterDataToXMLElement(resultSetMapList,
					fatherElementPath, childElementPath);
		} catch (RuntimeException e) {
			logger.error("adapter Element error.", e);
			throw new AdapterElementException("adapter Element error.");
		}
	}

	public Map getObject(Connection connection, String sql, Object[] params) {
		ResultSet resultSet = null;
		PreparedStatement preStatement = null;
		Map restuletMap = null;
		try {
			preStatement = this.sqlMapClientHelper.createCommonStatement(
					connection, sql, params, showSql);

			resultSet = preStatement.executeQuery();
			restuletMap = sqlMapClientHelper.getMapResultSetMap(resultSet);

			if (restuletMap == null)
				return null;
			return this.sqlMapClientHelper.getLowerkeyMap(restuletMap);
		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(resultSet, preStatement);
		}
	}

	public String getObjectAdapterToXmlData(Connection connection, String sql,
			Object[] params, String elementName) {
		Map mapResult = this.getObject(
				connection, sql, params);
		if (mapResult == null) {
			mapResult = new HashMap();
		}
		return this.sqlMapClientHelper.processXMLData(elementName, mapResult);
	}

	public List getObjectList(Connection connection, String sql, Object[] params) {
		ResultSet resultSet = null;
		PreparedStatement preStatement = null;
		List resultSetMapList = null;
		try {
			preStatement = this.sqlMapClientHelper.createCommonStatement(
					connection, sql, params, showSql);

			resultSet = preStatement.executeQuery();
			resultSetMapList = sqlMapClientHelper
					.getMapResultSetList(resultSet);

			if (resultSetMapList == null)
				return new ArrayList();
			return this.sqlMapClientHelper.getLowerkeyMapList(resultSetMapList);

		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(resultSet, preStatement);
		}
	}

	public List getObjectList(Connection connection, String sql,
			Object[] params, long specifyMaxCount) {
		ResultSet resultSet = null;
		PreparedStatement preStatement = null;
		List resultSetMapList = null;
		try {
			preStatement = this.sqlMapClientHelper.createCommonStatement(
					connection, sql, params, showSql);

			resultSet = preStatement.executeQuery();
			resultSetMapList = sqlMapClientHelper.getMapResultSetList(
					resultSet, specifyMaxCount);

			if (resultSetMapList == null)
				return new ArrayList();
			return this.sqlMapClientHelper.getLowerkeyMapList(resultSetMapList);

		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(resultSet, preStatement);
		}
	}

	public AllPaginationInfors getObjectPaginationList(Connection connection,
			String sql, Object[] params, HttpServletRequest request) {
		ResultSet resultSet = null;
		PreparedStatement preStatement = null;
		AllPaginationInfors allPaginationInfors = null;
		try {
			Order order = sqlMapClientHelper.getOrder(request);
			if (order != null) {
				StringBuffer sqlBuffer = new StringBuffer();
				Matcher matcher = patternPrams.matcher(sql);
				if (matcher.find()) {
					sqlBuffer
							.append(sql.substring(0, matcher.start()))
							.append(" ");
				} else {
					sqlBuffer.append(sql).append(" ");
				}
				sqlBuffer.append("ORDER BY ").append(
						order.getOrderFieldName().toUpperCase()).append(" ")
						.append(order.getOrderWay().toUpperCase());
				sql = sqlBuffer.toString();
			}
			preStatement = this.sqlMapClientHelper.createCommonStatement(
					connection, sql, params, showSql);
			resultSet = preStatement.executeQuery();

			allPaginationInfors = sqlMapClientHelper.adepterResultSetToPaginationInfor(resultSet, request);
			return allPaginationInfors;
		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(resultSet, preStatement);
		}
	}

	public String getPaginationDataAdapterToxmlData(Connection connection,
			String sql, Object[] params, String fatherElementpath, String childElementPath,
			HttpServletRequest request) {
		AllPaginationInfors allPaginationInfors = this.getObjectPaginationList(
				connection, sql, params, request);
		return sqlMapClientHelper.adapterDataToXMLElement(allPaginationInfors, fatherElementpath, childElementPath);
	}

	public Object getsingleColumn(Connection connection, String sql,
			Object[] params, Class objectType) {
		ResultSet resultSet = null;
		PreparedStatement preStatement = null;
		try {
			preStatement = this.sqlMapClientHelper.createCommonStatement(
					connection, sql, params, showSql);

			resultSet = preStatement.executeQuery();
			if (resultSet.next()) {
				Object obResult = resultSet.getObject(1);
			    if(obResult != null){
			    	return sqlMapClientHelper.changeDataKind(objectType.getName(), obResult);
			    }
			}
			return null;
		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(resultSet, preStatement);
		}
	}

	public List getsingleColumnList(Connection connection, String sql,
			Object[] params, Class objectType) {
		ResultSet resultSet = null;
		PreparedStatement preStatement = null;
		List resultSetMapList = new ArrayList();
		try {
			preStatement = this.sqlMapClientHelper.createCommonStatement(connection, sql, params, showSql);
			resultSet = preStatement.executeQuery();
			while (resultSet.next()) {
				Object obResult = resultSet.getObject(1);
				resultSetMapList.add((obResult == null) ? null : sqlMapClientHelper.changeDataKind(objectType.getName(), obResult));
			}
			return resultSetMapList;
		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		} finally {
			this.closeSqlStatement(resultSet, preStatement);
		}
	}
	
	public PreparedStatement getPreparedStatement(Connection connection, String sql, Object[] params){
		try {
			return this.sqlMapClientHelper.createCommonStatement(
					connection, sql, params, showSql);
		} catch (Exception e) {
			logger.error("sqlstatement execute error.\n" + sql, e);
			throw new SQLStatementProcessException(
					"sqlstatement execute error.\n" + sql);
		}
	}
	
	public String getXmlDataFromObjectList(List objectList, String fatherElementPath, String childElementPath){
		return this.sqlMapClientHelper.adapterDataToXMLElement(objectList, fatherElementPath, childElementPath);
	}
	
	public String getXmlDataFromMap(Map atributes, String elementName){
		return this.sqlMapClientHelper.processXMLData(elementName, atributes);
	}
	
	public String getStringFromObject(Object object){
		return this.sqlMapClientHelper.getStrFromOb(object);
	}
	
	private void closeSqlStatement(ResultSet resultSet, PreparedStatement statement) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

		} catch (SQLException e) {
			logger.error("SQLObject close exception.", e);
			throw new SQLStatementProcessException("SQLObject close exception.");
		}
	}

}
