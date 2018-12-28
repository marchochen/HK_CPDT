package com.cw.wizbank.util;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.qdb.qdbEnv;
import com.cwn.wizbank.utils.CommonLog;

public class RoleManager {
	private static final Logger logger = Log4jFactory.createLogger(com.cw.wizbank.util.RoleManager.class);
	private static final SqlMapClientDataSource sqlMapClient = SQLMapClientFactory.getSqlMapClient();
	private static final String function_map_file_name = "function_map.xml";
	private static final String id = "id";
	private static final String tc_related = "tc_related";
	
	private static final String jurgetRoleFunctionSql = new StringBuffer("select rfn_ftn_id from Acrolefunction")
	.append(" inner join acRole on (rfn_rol_id = rol_id)")
	.append(" inner join acFunction on (rfn_ftn_id = ftn_id)")
	.append(" where rol_ext_id = ? and ftn_ext_id = ?").toString();
	
	private static final String jurgetRoleFunctionsSql = new StringBuffer("select rfn_ftn_id from Acrolefunction")
	.append(" inner join acRole on (rfn_rol_id = rol_id)")
	.append(" inner join acFunction on (rfn_ftn_id = ftn_id)")
	.append(" where rol_ext_id = ? and ftn_ext_id in (#)").toString();
	
	private static String tc_related_function_id_lst = null;
	
	private static Connection getConnection() throws Exception{
		cwSQL sqlCon = new cwSQL();
        sqlCon.setParam(Dispatcher.getWizbini());
        Connection con = sqlCon.openDB(false);
        return con;
	}
	
	private static void getElementList(Element rootElement, Set elementList){
		if(rootElement.attributeValue(id) != null && !rootElement.attributeValue(id).equals("") && "true".equals(rootElement.attributeValue(tc_related))){
			elementList.add(rootElement.attributeValue(id));
		}
		List childElementLst = rootElement.elements();
		if(childElementLst != null && !childElementLst.isEmpty()){
			for (Iterator iterator = childElementLst.iterator(); iterator.hasNext();) {
				Element childElement = (Element) iterator.next();
				getElementList(childElement, elementList);
			}
		}
	}
	
	public static final String jurgetRoleHadFunction(String rol_ext_id, String ftn_ext_id){
		Connection con = null;
		try{
			con = getConnection();
			Object ftn_id = sqlMapClient.getsingleColumn(con, jurgetRoleFunctionSql, new Object[]{rol_ext_id, ftn_ext_id}, Integer.class);
			return ftn_id != null ? "true" : "false";
		}catch(Exception e){
			return "false";
		}finally{
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}
	
	public static final String jurgetRoleHadFunctions(String rol_ext_id, String ftn_ext_ids){
		String[] ftnIds = ftn_ext_ids.split(",");
		Connection con = null;
		try{
			con = getConnection();
			Object ftn_id = sqlMapClient.getsingleColumn(con, jurgetRoleFunctionsSql, new Object[]{rol_ext_id, ftnIds}, Integer.class);
			return ftn_id != null ? "true" : "false";
		}catch(Exception e){
			return "false";
		}finally{
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}
	
	public static final String get_tc_related_function_id_lst(){
		if(tc_related_function_id_lst == null){
			try {
				String function_map_file_path = Dispatcher.getWizbini().getAppnRoot() + cwUtils.SLASH + qdbEnv.CONFIG_FOLDER 
				+ cwUtils.SLASH + qdbEnv.SYSTEM_FOLDER + cwUtils.SLASH + function_map_file_name;
				SAXReader saxReader = new SAXReader();
				Document document = saxReader.read(new File(function_map_file_path));
				Set tc_related_function_id_set = new HashSet();
				getElementList(document.getRootElement(), tc_related_function_id_set);
				if(tc_related_function_id_set.isEmpty()){
					tc_related_function_id_lst = "[]";
				}else{
					StringBuffer tc_related_function_id_array_str = new StringBuffer("[");
					for(Iterator iterator = tc_related_function_id_set.iterator(); iterator.hasNext();) {
						tc_related_function_id_array_str.append("'").append((String)iterator.next()).append("'");
						if(iterator.hasNext()){
							tc_related_function_id_array_str.append(", ");
						}
					}
					tc_related_function_id_array_str.append("]");
					tc_related_function_id_lst = tc_related_function_id_array_str.toString();
				}
			} catch (Exception e) {
				logger.error("get tc_related functionID list unseccessfully.", e);
				return "[]";
			}
		}
		return tc_related_function_id_lst;
	}
	
	

}
