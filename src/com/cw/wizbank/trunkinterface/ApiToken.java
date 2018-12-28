package com.cw.wizbank.trunkinterface;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

public class ApiToken {
	
	public final static int TOKEN_LENGTH = 12;
	
	/*
	 * 注意"atk_"开头的字符串会自动insert到apiToken表中
	 */
	private String atk_id;//随机生成的标示
	private String atk_usr_id;//用户登录ID
	private long atk_usr_ent_id;//
	private Timestamp atk_create_timestamp;//创建时间（即登录时间）
	private Timestamp atk_expiry_timestamp;//有效期限
	private String atk_developer_id;//请求客户端标示
	
	private final static List<String> UNCHECK_COLUMS = Arrays.asList();
	
	
	/**
	 * 生成并记录apitoken
	 */
	public synchronized void insToken(Connection con) throws cwException {
		
		PreparedStatement stmt = null;
		ArrayList<Field> Columns = getColumns(this.getClass());
		try{
			String column_lst = "";
			String column_w_lst = "";
			for(int i = 0; i < Columns.size(); i++){
				if(i >= 1){
					column_lst += ",";
					column_w_lst += ",";
				}
				column_lst += Columns.get(i).getName();
				column_w_lst += "?";
			}
			
			String sql = "Insert into APIToken ("+column_lst+") Values ("+column_w_lst+")";
			stmt = con.prepareStatement(sql);
			
			for(int i = 0; i < Columns.size();i ++){
				Field f = Columns.get(i);
				Object o = f.get(this);
				if(!UNCHECK_COLUMS.contains(f.getName())){
					if(o == null){
						throw new cwException("Check Error : null = " + f.getName());
					}
				}
				stmt.setObject(i+1, o);
			}
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new cwException("ApiToken.genToken() : " + e.getMessage());
		} finally {
	        cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**
	 * 删除超时的apitoken
	 */
	public static void delToken(Connection con, Timestamp cur_time) throws cwException {
		
		PreparedStatement stmt = null;
		try{
			String sql = "delete from ApiToken where atk_expiry_timestamp < ?";
			stmt = con.prepareStatement(sql);
			stmt.setTimestamp(1, cur_time);
			stmt.executeUpdate();			
		} catch (Exception e) {
			throw new cwException("ApiToken.delToken() : " + e.getMessage());
		} finally {
	        cwSQL.closePreparedStatement(stmt);
		}
	}
	
	private static ArrayList<Field> getColumns(Class c) {
		ArrayList<Field> Columns = new ArrayList<Field>();
		Field[] fieldlist =  c.getDeclaredFields();
		for(int i = 0; i < fieldlist.length;i ++){
			Field f = fieldlist[i];
			if(f.getName() != null && f.getName().startsWith("atk_")){
				Columns.add(f);
			}
		}
		return Columns;
	}
	/**
	 * 从ApiToken中获取数据初始化prof
	 */
	public static void initProfFromToken (Connection con, loginProfile prof, String atk_id, Timestamp cur_time, String developer_id) throws cwException {
		
		ApiToken atk = getToken(con, atk_id);

		//无效的
		if(atk.getAtk_usr_id() == null || atk.getAtk_usr_id().length() <= 0){
			throw new cwException("Token is overdue or invalid : " + atk_id);
		}
		//删除已经过期的Token
		if(atk.atk_expiry_timestamp.before(cur_time)){
			ApiToken.delToken(con, cur_time);
			throw new cwException("Token is overdue : " + atk_id);
		}
		
		if(atk.getAtk_usr_id() != null && atk.getAtk_usr_id().length() > 0){
		     int time_out_min= DeveloperConfig.getDeveloperTimeOut(developer_id);
		     if(time_out_min < 1){
		         time_out_min = 30;
		     }
			InterfaceManagement.initLoginProfile(con, prof, atk.getAtk_usr_id(), atk.getAtk_usr_ent_id());
			updateExpiryTimestamp(con, atk_id, new Timestamp(cur_time.getTime()+time_out_min*60*1000));
		}
	}
	
	/**
	 * 从ApiToken中获取数据初始化prof
	 */
	public static void initWeixinProfFromToken (Connection con, loginProfile prof, String atk_id, Timestamp cur_time) throws cwException {
		
		ApiToken atk = getToken(con, atk_id);

		//无效的
		if(atk.getAtk_usr_id() == null || atk.getAtk_usr_id().length() <= 0){
			throw new cwException("Token is overdue or invalid : " + atk_id);
		}
		//删除已经过期的Token
		if(atk.atk_expiry_timestamp.before(cur_time)){
			//ApiToken.delToken(con, cur_time);
			//throw new cwException("Token is overdue : " + atk_id);
		}
		
		if(atk.getAtk_usr_id() != null && atk.getAtk_usr_id().length() > 0){
			InterfaceManagement.initLoginProfile(con, prof, atk.getAtk_usr_id(), atk.getAtk_usr_ent_id());
			updateExpiryTimestamp(con, atk_id, new Timestamp(cur_time.getTime()+30*60*1000));
		}
	}
	/**
	 * 增加有效期
	 */
	public static void updateExpiryTimestamp(Connection con, String atk_id, Timestamp expiry_timestamp) throws cwException {
		
		PreparedStatement stmt = null;
		try{
			String sql = "update ApiToken set atk_expiry_timestamp=? where atk_id=?";
			stmt = con.prepareStatement(sql);
			stmt.setTimestamp(1, expiry_timestamp);
			stmt.setString(2, atk_id);
			stmt.executeUpdate();			
		} catch (Exception e) {
			throw new cwException("ApiToken.updateExpiryTimestamp() : " + e.getMessage());
		} finally {
	        cwSQL.closePreparedStatement(stmt);
		}
	}
	/**
	 * 
	 */
	public static ApiToken getToken(Connection con, String atk_id) throws cwException {
		
		ApiToken atk = new ApiToken();
		ArrayList<Field> Columns = getColumns(atk.getClass());
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql = "select * from APIToken where atk_id=?";
			stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			int index = 1;
			stmt.setString(index++, atk_id);
			rs = stmt.executeQuery();
			while(rs.next()){
				//如果查到超过1条记录则报错
				if(!rs.isLast()){
					throw new cwException("Error :: ApiToken.getToken() token is more than 1 !! atk_id="+atk_id);
				}
				for(int i = 0; i < Columns.size();i ++){
					Field f = Columns.get(i);
					if("atk_usr_ent_id".equalsIgnoreCase(f.getName())){
						//atk_usr_ent_id 从数据库拿出来为float,与实体字段long映射出错
						f.set(atk, Long.valueOf(rs.getObject(f.getName()).toString()));
					}else {
						f.set(atk, rs.getObject(f.getName()));
					}
				}
			}
		} catch (Exception e) {
			throw new cwException("Error :: ApiToken.getToken() SQLException !! atk_id="+atk_id);
		} finally {
	        cwSQL.cleanUp(rs, stmt);
		}
		return atk;
	}
	
/**根据用户名获取有效期最长的token
 * @param con
 * @param atk_id
 * @return
 * @throws cwException
 */
public static ApiToken getEffToken(Connection con, String usr_id) throws cwException {
		
		ApiToken atk = new ApiToken();
		ArrayList<Field> Columns = getColumns(atk.getClass());
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String sql = "select * from APIToken where atk_usr_id=? and atk_developer_id = 'MOBILE' order by atk_expiry_timestamp desc";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, usr_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				for(int i = 0; i < Columns.size();i ++){
					Field f = Columns.get(i);
					f.set(atk, rs.getObject(f.getName()));
				}
			}
		} catch (Exception e) {
			throw new cwException("Error :: ApiToken.getToken() SQLException !! usr_id="+usr_id);
		} finally {
	        cwSQL.cleanUp(rs, stmt);
		}
		return atk;
	}
	
	public void setAtk_id(String atk_id) {
		this.atk_id = atk_id;
	}
	public String getAtk_id() {
		return atk_id;
	}
	public void setAtk_usr_id(String atk_usr_id) {
		this.atk_usr_id = atk_usr_id;
	}
	public String getAtk_usr_id() {
		return atk_usr_id;
	}
	public long getAtk_usr_ent_id() {
		return atk_usr_ent_id;
	}
	public void setAtk_usr_ent_id(long atkUsrEntId) {
		atk_usr_ent_id = atkUsrEntId;
	}
	public void setAtk_create_timestamp(Timestamp atk_create_timestamp) {
		this.atk_create_timestamp = atk_create_timestamp;
	}
	public Timestamp getAtk_create_timestamp() {
		return atk_create_timestamp;
	}

	public void setAtk_expiry_timestamp(Timestamp atk_expiry_timestamp) {
		this.atk_expiry_timestamp = atk_expiry_timestamp;
	}

	public Timestamp getAtk_expiry_timestamp() {
		return atk_expiry_timestamp;
	}

	public void setAtk_developer_id(String atk_developer_id) {
		this.atk_developer_id = atk_developer_id;
	}

	public String getAtk_developer_id() {
		return atk_developer_id;
	}
	

}
