/*
 * Created on 2004-9-15
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.ae.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbItemCost {
	private long ito_itm_id;
	private long ito_type;
	private BigDecimal ito_budget;
	private BigDecimal ito_actual;
	private Timestamp ito_create_timestamp;
	private String ito_create_usr_id;
	private Timestamp ito_update_timestamp;
	private String ito_update_usr_id;
	
	//0 for insert and 1 for update
	private int chang_type;
	
	//public DbItemCost(){}
	
	public DbItemCost(long id,long type){
		if(id == 0 || type == 0){
			throw new NullPointerException("No ito_itm_id or ito_type in DbItemCost!!!");
		}
		this.ito_itm_id = id;
		this.ito_type = type;
	}
	
	private String insert = "insert into aeItemCost (ito_itm_id,ito_type,ito_budget,ito_actual,ito_create_timestamp,ito_create_usr_id, "		                 +" ito_update_timestamp,ito_update_usr_id ) values(?,?,?,?,?,?,?,?)";
	private String update = "update aeItemCost set ito_budget = ? , ito_actual = ?,ito_update_timestamp = ?,ito_update_usr_id = ? "
	                     +" where ito_itm_id = ? and ito_type = ?";
	                     
	private String del = "delete from aeItemCost where ito_itm_id = ? and ito_type = ?";
	public String getInsClause(){
		return insert;
	}
	
	/*persistent method for aeItemCost*/
	public void ins(Connection con){
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(insert);
			pst.setLong(1,this.getIto_itm_id());
			pst.setLong(2,this.getIto_type());
			pst.setBigDecimal(3,this.getIto_budget());
			pst.setBigDecimal(4,this.getIto_actual());
			pst.setTimestamp(5,this.getIto_create_timestamp());
			pst.setString(6,this.getIto_create_usr_id());
			pst.setTimestamp(7,this.getIto_update_timestamp());
			pst.setString(8,this.getIto_update_usr_id());

			pst.executeUpdate();
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}finally{
	        cwSQL.closePreparedStatement(pst);
		}
	}
	
	public void upd(Connection con){
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(update);
			//System.out.println("++++B++"+this.getIto_budget()+"++++A++"+this.getIto_actual());
			pst.setBigDecimal(1,this.getIto_budget());
			pst.setBigDecimal(2,this.getIto_actual());
			pst.setTimestamp(3,this.getIto_update_timestamp());
			pst.setString(4,this.getIto_update_usr_id());
			pst.setLong(5,this.getIto_itm_id());
			pst.setLong(6,this.getIto_type());
			
			pst.executeUpdate();
		}catch(Exception e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	public void del(Connection con){
		PreparedStatement pst = null;
		try{
			pst = con.prepareStatement(del);
			pst.setLong(1,this.getIto_itm_id());
			pst.setLong(2,this.getIto_type());
			
			pst.executeUpdate();
		}catch(Exception e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	/*xml maker for one aeItemCost record*/
	public String object2Xml(Connection con){
		StringBuffer sb = new StringBuffer(128);
		try{
		 if(getIto_itm_id()!=0 && getIto_type()!=0){
		   sb.append("<cost type=\"").append(getIto_type()).append("\">");
		   sb.append("<budget_fee>")
		    .append(getIto_budget() == null?"":getIto_budget().toString())
		    .append("</budget_fee>");
		   sb.append("<actual_fee>")
		    .append(getIto_actual() == null?"":getIto_actual().toString())
		    .append("</actual_fee>");
		   sb.append("<last_updated usr_id=\"").append(getIto_update_usr_id()).append("\"")
		    .append(" timestamp=\"").append(getIto_update_timestamp()).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con,getIto_update_usr_id())))
		    .append("</last_updated>");
		   sb.append("<creator usr_id=\"").append(getIto_create_usr_id()).append("\"")
			 .append(" timestamp=\"").append(getIto_create_timestamp()).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con,getIto_create_usr_id())))
			 .append("</creator>");
		   sb.append("</cost>");
		 }
		}catch(SQLException e){
			throw new RuntimeException(e.getMessage());
		}
		return sb.toString();
	}
	
	/*match if the two obj point to the same record*/
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		if(!(o instanceof DbItemCost)){
			return false;
		}
		DbItemCost dbObj = (DbItemCost)o;
		return this.getIto_itm_id() == dbObj.getIto_itm_id() &&
		       this.getIto_type() == dbObj.getIto_type(); 
		       //&& this.getIto_update_timestamp().equals(dbObj.getIto_update_timestamp()) &&
		       //this.getIto_actual().equals(dbObj.getIto_actual()) &&
		       //this.getIto_budget().equals(dbObj.getIto_budget());
	}
	
	public boolean isChangedByOther(DbItemCost dbObj){
		return !(this.getIto_update_timestamp().equals(dbObj.getIto_update_timestamp()));
	}
	
	public boolean isValueChanged(DbItemCost dbObj){
        boolean flag = false;
        if(this.getIto_actual() == null){
//        	if(dbObj.getIto_actual() != null){
//        		return true;
//        	}
            return true;
        }else{
        	if(dbObj.getIto_actual() == null){
        		return true;
        	}
        }

		if(this.getIto_budget() == null){
//			if(dbObj.getIto_budget() != null){
//				return true;
//			}
            return true;
		}else{
			if(dbObj.getIto_budget() == null){
				return true;
			}
		}

		if(!(this.getIto_actual().equals(dbObj.getIto_actual()) &&
		this.getIto_budget().equals(dbObj.getIto_budget()))){
			  flag = true;
		}
        return flag;
	}
	
	
	public BigDecimal getIto_actual() {
		return ito_actual;
	}

	public BigDecimal getIto_budget() {
		return ito_budget;
	}

	public Timestamp getIto_create_timestamp() {
		return ito_create_timestamp;
	}


	public String getIto_create_usr_id() {
		return ito_create_usr_id;
	}


	public long getIto_itm_id() {
		return ito_itm_id;
	}

	public long getIto_type() {
		return ito_type;
	}

	public Timestamp getIto_update_timestamp() {
		return ito_update_timestamp;
	}


	public String getIto_update_usr_id() {
		return ito_update_usr_id;
	}


	public void setIto_actual(BigDecimal decimal) {
		ito_actual = decimal;
	}

	public void setIto_budget(BigDecimal decimal) {
		ito_budget = decimal;
	}


	public void setIto_create_timestamp(Timestamp timestamp) {
		ito_create_timestamp = timestamp;
	}


	public void setIto_create_usr_id(String string) {
		ito_create_usr_id = string;
	}


	public void setIto_update_timestamp(Timestamp timestamp) {
		ito_update_timestamp = timestamp;
	}


	public void setIto_update_usr_id(String string) {
		ito_update_usr_id = string;
	}

	public int getChang_type() {
		return chang_type;
	}

	public void setChang_type(int i) {
		chang_type = i;
	}
}
