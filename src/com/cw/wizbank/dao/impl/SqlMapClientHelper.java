package com.cw.wizbank.dao.impl;

/*
 * 该类主要对查询出来的数据进行相应的适配处理
 * @author:wrren
 * 
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.cw.wizbank.dao.Order;
import com.cw.wizbank.dao.File.BigData;
import com.cw.wizbank.dao.exception.AdapterElementException;
import com.cw.wizbank.dao.exception.SQLStatementProcessException;
import com.cw.wizbank.dao.pagination.AllPaginationInfors;
import com.cw.wizbank.dao.pagination.GetPaginationInfor;
import com.cw.wizbank.dao.pagination.NowPage;
import com.cw.wizbank.dao.pagination.ProcessPagination;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.util.cwUtils;

public class SqlMapClientHelper {
	private static final Logger logger = Log4jFactory.createLogger(com.cw.wizbank.dao.impl.SqlMapClientHelper.class);
	private static final Pattern patternPrams = Pattern.compile("(\\s)+[iI][nN](\\s)*\\((\\s)*#(\\s)*\\)");
	private static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String ALLPAGINATIONINFORS = "allPaginationInfors";
	private static final String PAGEINFORS = "PageInfors";
	private static final String STARTRECORD = "startRecord";// 起始记录
	private static final String ENDRECORD = "endRecord";// 结束记录
	private static final String RECORDCOUNT = "recordCount";
	private static final String ONEPAGEDISRECORDCOUNT = "onePageDisRecordCount";
	private static final String NOWPAGENUMBER = "nowpageNumber";// 当前页面号码
	private static final String ALLPAGECOUNT = "allPageCount";// 页面总数量
	private static final String PAGELIST = "pageList";// 页面导航的的序列
	private static final String PAGEINDEX = "pageIndex";
	private static SqlMapClientHelper sqlMapClientHelper = null;
	/*
	 * 处理分页的类
	 */
	private ProcessPagination processPagination = new ProcessPagination();
	
	private SqlMapClientHelper (){}

	public void setProcessPagination(ProcessPagination processPagination) {
		this.processPagination = processPagination;
	}
	
	public static SqlMapClientHelper getInstance(){
		if(sqlMapClientHelper == null){
			sqlMapClientHelper = new SqlMapClientHelper();
		}
		return sqlMapClientHelper;
	}

	/*
	 * 从resultSet结果集中得到一个以字段名称为Key，以字段值为value的Map集合
	 */
	public final Map getMapResultSetMap(ResultSet resultSet) {
		Map mapResult = new HashMap();
		try {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			if (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					String columnName = resultSetMetaData.getColumnName(i)
							.toUpperCase();
					Object columnValue = resultSet.getObject(columnName);
					if (columnValue instanceof java.sql.Clob) {
						 mapResult.put(columnName, new BigData((java.sql.Clob) columnValue));
					} else if (columnValue instanceof java.sql.Blob) {
						mapResult.put(columnName, new BigData((java.sql.Blob) columnValue));
					} else if (columnValue instanceof byte[]) {
						mapResult.put(columnName, new BigData(((byte[]) columnValue)));
					}else{
						mapResult.put(columnName, columnValue);
					}
				}
			}

		} catch (SQLException e) {
			logger.error("get resultSet exception.", e);
			throw new SQLStatementProcessException("get resultSet exception.");
		}

		return mapResult.isEmpty() ? null : mapResult;
	}

	/*
	 * 把一个ResultSet结果集适配成一个List<Map<String, Object>>类型的数据，List集合中的每一个Map集合代表数据
	 * 库中的一条记录
	 */
	public final List getMapResultSetList(
			ResultSet resultSet) {
		List mapResultList = new ArrayList();
		try {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();

			while (resultSet.next()) {
				Map mapResult = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = resultSetMetaData.getColumnName(i)
							.toUpperCase();
					Object columnValue = resultSet.getObject(columnName);
					if (columnValue instanceof java.sql.Clob) {
						 mapResult.put(columnName, new BigData((java.sql.Clob) columnValue));
					} else if (columnValue instanceof java.sql.Blob) {
						mapResult.put(columnName, new BigData((java.sql.Blob) columnValue));
					} else if (columnValue instanceof byte[]) {
						mapResult.put(columnName, new BigData(((byte[]) columnValue)));
					}else{
						mapResult.put(columnName, columnValue);
					}
				}
				mapResultList.add(mapResult);
			}

		} catch (SQLException e) {
			logger.error("get resultSet exception.", e);
			throw new SQLStatementProcessException("get resultSet exception.");
		}

		return mapResultList.isEmpty() ? null : mapResultList;
	}

	/*
	 * 把一个ResultSet结果集适配成一个List<Map<String, Object>>类型的数据，List集合中的每一个Map集合代表数据
	 * 库中的一条记录(specifyMaxCount为集合的元素的最大个数)
	 */
	public final List getMapResultSetList(
			ResultSet resultSet, long specifyMaxCount) {
		List mapResultList = new ArrayList();
		try {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();

			for (long count = 0; (count < specifyMaxCount && resultSet.next()); count++) {
				Map mapResult = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = resultSetMetaData.getColumnName(i)
							.toUpperCase();
					Object columnValue = resultSet.getObject(columnName);
					if (columnValue instanceof java.sql.Clob) {
						 mapResult.put(columnName, new BigData((java.sql.Clob) columnValue));
					} else if (columnValue instanceof java.sql.Blob) {
						mapResult.put(columnName, new BigData((java.sql.Blob) columnValue));
					} else if (columnValue instanceof byte[]) {
						mapResult.put(columnName, new BigData(((byte[]) columnValue)));
					}else{
						mapResult.put(columnName, columnValue);
					}
				}
				mapResultList.add(mapResult);
			}

		} catch (SQLException e) {
			logger.error("get resultSet exception.", e);
			throw new SQLStatementProcessException("get resultSet exception.");
		}

		return mapResultList.isEmpty() ? null : mapResultList;
	}

	/*
	 * 把一个ResultSet结果集按分页信息适配成一个List<Map<String,
	 * Object>>类型的数据，List集合中的每一个Map集合代表数据 库中的一条记录
	 */
	public final AllPaginationInfors adepterResultSetToPaginationInfor(ResultSet resultSet,
			HttpServletRequest request) {
		try {
			resultSet.last();
			long recordCount = resultSet.getRow();

			// 得到分页信息，default 5 条记录 ，default 10 页面导航 ,next defalut 不换页 （null and
			// 参数转换异常）
			Object oneTimeCountParam = request.getParameter(this.processPagination.getOneTimeCountPro());
			oneTimeCountParam = (oneTimeCountParam == null || "".equals(oneTimeCountParam)) ? request.getAttribute(this.processPagination.getOneTimeCountPro()) : oneTimeCountParam;
			oneTimeCountParam = (oneTimeCountParam == null || "".equals(oneTimeCountParam)) ? new Long(this.processPagination.getDefaultOneTimeCount()) : oneTimeCountParam;
			Object pageNaviCountParam = request.getParameter(this.processPagination.getDisPageCountPro());
			pageNaviCountParam = (pageNaviCountParam == null || "".equals(pageNaviCountParam)) ? request.getAttribute(this.processPagination.getDisPageCountPro()) : pageNaviCountParam;
			Object nextPageParam = request.getParameter(this.processPagination.getNextPagePro());
			nextPageParam = (nextPageParam == null || "".equals(nextPageParam)) ? request.getAttribute(this.processPagination.getNextPagePro()) : nextPageParam;

			// 得到分页处理对象
			GetPaginationInfor getPaginationInfor = new GetPaginationInfor(
					recordCount, oneTimeCountParam,
					pageNaviCountParam, this.processPagination.getDefaultOneTimeCount(),
					this.processPagination.getDefaultOnePageCount());
			// 处理后的分页信息
			AllPaginationInfors allPaginationInfors = getPaginationInfor
					.getAllInformation(this.processPagination
							.getNowPage(request), nextPageParam);

			if (allPaginationInfors == null)
				return new AllPaginationInfors();
			// 更新session中的下一页信息
			request.getSession().setAttribute(
					this.processPagination.getNowPageInSessionPro(),
					new NowPage(this.processPagination.getURl(request),
							allPaginationInfors.getNowPageNumber()));

			// 从数据库结果结果集中获取数据
			List mapResultList = new ArrayList();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			long startRecord = allPaginationInfors.getstartRecord();// start
			long endRecord = allPaginationInfors.getEndRecord();// end
			resultSet.absolute((int)(startRecord + 1));
			resultSet.previous();
			while (resultSet.next() && startRecord <= endRecord) {
				Map mapResult = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = resultSetMetaData.getColumnName(i)
							.toUpperCase();
					Object columnValue = resultSet.getObject(columnName);
					if (columnValue instanceof java.sql.Clob) {
						 mapResult.put(columnName, new BigData((java.sql.Clob) columnValue));
					} else if (columnValue instanceof java.sql.Blob) {
						mapResult.put(columnName, new BigData((java.sql.Blob) columnValue));
					} else if (columnValue instanceof byte[]) {
						mapResult.put(columnName, new BigData(((byte[]) columnValue)));
					}else{
						mapResult.put(columnName, columnValue);
					}
					
				}
				mapResultList.add(mapResult);
				++startRecord;
			}

			allPaginationInfors.setQueryResultList(getLowerkeyMapList(mapResultList)); // 1 10
			// 10-1+1
			allPaginationInfors.setRecordCount(recordCount);// 设置记录总数量
			allPaginationInfors.setOnePageDisRecordCount(Long.valueOf(String
					.valueOf(oneTimeCountParam)).longValue());// 设置一页显示的记录数量
			return allPaginationInfors;

		} catch (SQLException e) {
			logger.error("get resultSet exception.", e);
			throw new SQLStatementProcessException("get resultSet exception.");
		}

	}

	/*
	 * 将List<Map<String, Object>>类型的数据适配成xml字符串
	 */
	public final String adapterDataToXMLElement(
			List resultSetMapList, String fatherElementPath, String childElementPath) {
		String isEmpty = (resultSetMapList == null || resultSetMapList.isEmpty()) ? "true" : "false";
		StringBuffer target = new StringBuffer();
		target.append("<").append(fatherElementPath).append(" isEmpty=\""+isEmpty+"\">");
		if(!isEmpty.equals("true")){
			for (Iterator iterator = resultSetMapList.iterator(); iterator.hasNext();) {
				Map mapresult = (Map) iterator.next();
				target.append(this.processXMLData(childElementPath, mapresult));
			}
		}
		target.append("</").append(fatherElementPath).append(">");

		return target.toString();
	}
	
	/*
	 * 将一个Map集合适配成xml字符串
	 */
	public final String processXMLData(String elementName, Map atributes) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<").append(elementName + " ");
		Set keysets = atributes.keySet();
		for (Iterator ikeyStr = keysets.iterator(); ikeyStr.hasNext();) {
			String keyName = (String) ikeyStr.next();
			stringBuffer.append(keyName).append("=\"").append(
					getStrFromOb(atributes.get(keyName))).append("\" ");
		}
		stringBuffer.append("/>");

		return stringBuffer.toString();
	}

	/*
	 * 将AllpaginationInfors 信息适配成 xml字符串
	 */
	public final String adapterDataToXMLElement(
			AllPaginationInfors allPaginationInfors, String fatherElePath, String childElePath) {
		String isEmpty = (allPaginationInfors == null || allPaginationInfors.getQueryResultList() == null || allPaginationInfors.getQueryResultList().isEmpty()) ? "true" : "false";
		if(isEmpty.equals("true")) {
			String fatherElementString = "<" + fatherElePath + "></" + fatherElePath + ">";
			StringBuffer targetString = new StringBuffer("<allPaginationInfors isEmpty=\"true\">");
			targetString.append(fatherElementString).append("<PageInfors></PageInfors>");
			targetString.append("</allPaginationInfors>");
			return targetString.toString();
		}
		
		List resultSetMapList = allPaginationInfors.getQueryResultList();
		StringBuffer targetStr = new StringBuffer();
		targetStr.append("<").append(ALLPAGINATIONINFORS).append(" isEmpty=\"false\">");
		
		targetStr.append("<").append(fatherElePath).append(">");
		for (Iterator iterator = resultSetMapList.iterator(); iterator
				.hasNext();) {
			Map mapresult = (Map) iterator.next();
			targetStr.append(this.processXMLData(childElePath, mapresult));
		}
		targetStr.append("</").append(fatherElePath).append(">");

		Map pageInforsMap = new HashMap();
		pageInforsMap.put(STARTRECORD, new Long(allPaginationInfors.getstartRecord()));
		pageInforsMap.put(ENDRECORD, new Long(allPaginationInfors.getEndRecord()));
		pageInforsMap.put(NOWPAGENUMBER, new Long(allPaginationInfors.getNowPageNumber()));
		pageInforsMap.put(ALLPAGECOUNT, new Long(allPaginationInfors.getAllPathCount()));
		pageInforsMap.put(RECORDCOUNT, new Long(allPaginationInfors.getRecordCount()));
		pageInforsMap.put(ONEPAGEDISRECORDCOUNT, new Long(allPaginationInfors.getOnePageDisRecordCount()));
		targetStr.append(this.processXMLDataWithOutEnd(PAGEINFORS, pageInforsMap));
		for (Iterator iterator = allPaginationInfors.getPageList().iterator(); iterator
				.hasNext();) {
			Long singePath = (Long) iterator.next();
			Map singlePageIndex = new HashMap();
			singlePageIndex.put(PAGEINDEX, singePath);
			targetStr.append(this.processXMLData(PAGELIST, singlePageIndex));
		}
		targetStr.append("</").append(PAGEINFORS).append(">");
		targetStr.append("</").append(ALLPAGINATIONINFORS).append(">");

		return targetStr.toString();
	}

	public final List getListFromParams(Object[] params) {
		List objectList = new ArrayList();
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				if (params[i] instanceof Collection) {
					Collection collection = (Collection) params[i];
					objectList.addAll(collection);
				} else if (params[i].getClass().isArray()) {
					Object[] objests = this.getObejctArrays(params[i]);
					for (int j = 0; j < objests.length; j++) {
						objectList.add(objests[j]);
					}
				} else {
					objectList.add(params[i]);
				}
			}
		}
		return objectList;
	}

	public final Order getOrder(HttpServletRequest request) {
		String orderbyColumnNamePro = processPagination.getOrderByColumnPro();
		String pernumationWayPro = processPagination.getPernumationWayPro();
		String orderByColumnName = request.getParameter(orderbyColumnNamePro);
		orderByColumnName = ((orderByColumnName == null || orderByColumnName.equals("")) && request.getAttribute(orderbyColumnNamePro) != null) ? String.valueOf(request.getAttribute(orderbyColumnNamePro)) : orderByColumnName;		
		if (orderByColumnName != null && !orderByColumnName.equals("")) {
			String pernumationWay = request.getParameter(pernumationWayPro);
			pernumationWay = ((pernumationWay == null || pernumationWay.equals("")) && request.getAttribute(pernumationWayPro) != null ) ? String.valueOf(request.getAttribute(pernumationWayPro)) : pernumationWay;
			Order order = new Order(orderByColumnName, pernumationWay);
			return order;
		} else {
			return null;
		}
	}
	
	public final Map getLowerkeyMap(Map targetObject){
		Map resultMap = new HashMap();
		if(targetObject != null){
			Set keySet = targetObject.keySet();
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				resultMap.put(key.toLowerCase(), targetObject.get(key));
			}
		}
		return resultMap;
	}
	
	public final List getLowerkeyMapList(List uperMapList){
		List lowerMapList = new ArrayList();
		for (Iterator iterator = uperMapList.iterator(); iterator.hasNext();) {
			Map upderMap = (Map)iterator.next();
			lowerMapList.add(getLowerkeyMap(upderMap));
		}
		return lowerMapList;
	}
	
	/*
	 * 该方法用于创建查询的sql语句
	 */
	public final PreparedStatement createCommonStatement(Connection con, String sqlStatement, 
			Object[] params, boolean showSql) throws SQLException, IOException {
		String newSqlStatement = sqlStatement;
		PreparedStatement preparedStatement = null;
		List listParams = new ArrayList();
		List collectionIndexList = new ArrayList();
		List collectionCondition = new ArrayList();
		for(int n=0; n<params.length; n++){
			if(params[n].getClass().isArray()){
				Object[] arrays = getObejctArrays(params[n]);
				for(int i=0; i<arrays.length; i++){
					listParams.add(arrays[i]);
				}
				collectionCondition.add(getArrayCondition(arrays.length));
				
			}else if(params[n] instanceof Collection){
				Collection collection = (Collection) params[n];
				for(Iterator paramIte = collection.iterator(); paramIte.hasNext(); ){
					listParams.add(paramIte.next());
				}
				collectionCondition.add(getArrayCondition(collection.size()));
			}else{
				listParams.add(params[n]);
			}
		}
		Matcher matcher = patternPrams.matcher(sqlStatement);
		while(matcher.find()){
			for(int j = matcher.start(); j < matcher.end(); j++){
				if(newSqlStatement.substring(j, j+1).equals("#")){
					collectionIndexList.add(new Integer(j));
				}
			}
		}
		if(collectionCondition.size() != collectionIndexList.size()){
			throw new CreateSqlStatementException("the params is error.");
		}
		for(int k = collectionIndexList.size()-1; k>=0; k--){
			int indexValue = ((Integer)collectionIndexList.get(k)).intValue();
			newSqlStatement = newSqlStatement.substring(0, indexValue) + collectionCondition.get(k) + newSqlStatement.substring(indexValue + 1);
		}
		if(showSql){
			logger.info(newSqlStatement);
		}
		preparedStatement = con.prepareStatement(newSqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		for(int l=0; l<listParams.size(); l++){
			Object objectParams = listParams.get(l);
			if(objectParams instanceof com.cw.wizbank.dao.File.BigData){
				BigData bigData = (BigData) objectParams;
				InputStream inputStream = bigData.getInputStrean();
				preparedStatement.setBinaryStream(l + 1,
						inputStream, inputStream.available());
			}else{
				preparedStatement.setObject(l + 1, objectParams);
			}
			
		}
		return preparedStatement;
	}
	
	public final Object changeDataKind(String filedTypeName, Object fieldValue){
		if (filedTypeName.equals("int")
				|| filedTypeName.equals("java.lang.Integer")) {
			return Integer.valueOf(
					String.valueOf(fieldValue));
		} else if (filedTypeName.equals("long")
				|| filedTypeName.equals("java.lang.Long")) {
			return Long.valueOf(String.valueOf(fieldValue));
		} else if (filedTypeName.equals("java.lang.String")) {
			return String
					.valueOf(String.valueOf(fieldValue));
		} else if (filedTypeName.equals("float")
				|| filedTypeName.equals("java.lang.Float")) {
			return Float.valueOf(String.valueOf(fieldValue));
		} else if (filedTypeName.equals("double")
				|| filedTypeName.equals("java.lang.Double")) {
			return Double
					.valueOf(String.valueOf(fieldValue));
		} else if (filedTypeName.equals("java.sql.Date") || filedTypeName.equals("java.util.Date")) {
			return java.sql.Date.valueOf(String.valueOf(fieldValue));
		} else if (filedTypeName.equals("java.sql.Timestamp")) {
			return java.sql.Timestamp.valueOf(String
					.valueOf(fieldValue));
		}else if (filedTypeName
				.equals("com.cw.wizbank.dao.File.BigData")) {
			if (fieldValue instanceof java.sql.Clob) {
				return new BigData(
						(java.sql.Clob) fieldValue);
			} else if (fieldValue instanceof java.sql.Blob) {
				return new BigData(
						(java.sql.Blob) fieldValue);
			} else if (fieldValue instanceof byte[]) {
				return new BigData(((byte[]) fieldValue));
			}
		}
		return fieldValue;
	}
	
	/*
	 * 在将数据适配成xml字符串时对数据进行相应的转换
	 */
	public final String getStrFromOb(Object object) {
		try {
			if (object == null) {
				return "--";
			} else if (object instanceof java.util.Date) {
				return SIMPLEDATEFORMAT.format(object);
			} else if(object instanceof String){
				return cwUtils.esc4XML(String.valueOf(object));
			}else{
				return String.valueOf(object);
			}
		} catch (Exception e) {
			logger.error("adapter element exception", e);
			throw new AdapterElementException(
					"adapter element change data type failure.");
		}

	}

	private String getArrayCondition(int arraySize){
		StringBuffer arrayCondition = new StringBuffer();
		for(int n=0; n<arraySize; n++){
			arrayCondition.append("?");
			if((n+1) < arraySize){
				arrayCondition.append(", ");
			}
		}
		return arraySize == 0 ? "null" : arrayCondition.toString();
	}
	
	private Object[] getObejctArrays(Object object){
		if(object instanceof long[]){
			long[] longArrays = (long[])object;
			Long[] longArrays02 = new Long[longArrays.length];
			for(int n=0; n<longArrays.length; n++){
				longArrays02[n] = new Long(longArrays[n]);
			}
			return longArrays02;
		}else if(object instanceof double[]){
			double[] doubleArrays = (double[])object;
			Double[] doubleArrays02 = new Double[doubleArrays.length];
			for(int n=0; n<doubleArrays.length; n++){
				doubleArrays02[n] = new Double(doubleArrays[n]);
			}
			return doubleArrays02;
		}else if(object instanceof int[]){
			int[] intArrays = (int[])object;
			Integer[] intArrays02 = new Integer[intArrays.length];
			for(int n=0; n<intArrays.length; n++){
				intArrays02[n] = new Integer(intArrays[n]);
			}
			return intArrays02;
		}else if(object instanceof float[]){
			float[] floatArrays = (float[])object;
			Float[] floatArrays02 = new Float[floatArrays.length];
			for(int n=0; n<floatArrays.length; n++){
				floatArrays02[n] = new Float(floatArrays[n]);
			}
			return floatArrays02;
		}else if(object instanceof short[]){
			short[] shortArryas = (short[])object;
			Short[] shortArrays02 = new Short[shortArryas.length];
			for(int n=0; n<shortArryas.length; n++){
				shortArrays02[n] = new Short(shortArryas[n]);
			}
			return shortArrays02;
		}else if(object instanceof byte[]){
			byte[] byteArrays = (byte[])object;
			Byte[] byteArrays02 = new Byte[byteArrays.length];
			for(int n=0; n<byteArrays.length; n++){
				byteArrays02[n] = new Byte(byteArrays[n]);
			}
			return byteArrays02;
		}else if(object instanceof char[]){
			char[] charArrays = (char[])object;
			Character[] charArrays02 = new Character[charArrays.length];
			for(int n=0; n<charArrays.length; n++){
				charArrays02[n] = new Character(charArrays[n]);
			}
			return charArrays02;
		}else if(object instanceof boolean[]){
			boolean[] booleanArrays = (boolean[])object;
			Boolean[] booleanArrays02 = new Boolean[booleanArrays.length];
			for (int i = 0; i < booleanArrays.length; i++) {
				booleanArrays02[i] = new Boolean(booleanArrays[i]);
			}
			return booleanArrays02;
		}else{
			return (Object[])object;
		}
	}

	/*
	 * 将一个Map集合适配成xml字符串，该xml字符串没有结束
	 */
	private String processXMLDataWithOutEnd(String elementName,
			Map atributes) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<").append(elementName + " ");
		Set keysets = atributes.keySet();
		for (Iterator ikeyStr = keysets.iterator(); ikeyStr.hasNext();) {
			String keyName = (String) ikeyStr.next();
			stringBuffer.append(keyName).append("=\"").append(
					getStrFromOb(atributes.get(keyName))).append("\" ");
		}
		stringBuffer.append(">");

		return stringBuffer.toString();
	}
	
	private static final class CreateSqlStatementException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public CreateSqlStatementException() {
		
		}
		
		public CreateSqlStatementException(String message) {
			super(message);
		}
	
	}

}
