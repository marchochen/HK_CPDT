package com.cw.wizbank.JsonMod.qqConsultation;

import java.sql.Connection;
import java.sql.SQLException;
//import org.apache.log4j.Logger;
//import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;

public class QQConsultationLogic {
	//private final Logger logger = Log4jFactory.createLogger(com.cw.wizbank.JsonMod.qqConsultation.QQConsultationLogic.class);
	private final SqlMapClientDataSource sqlMapClient = SQLMapClientFactory.getSqlMapClient();
	private final String companyQQLst = "companyQQLst";
	private final String companyQQ = "companyQQ";
	private final String ADD = "ADD";
	private final String UPD = "UPD";
	private final String DEL = "DEL";
	private final String ON = "ON";
	private final String OFF = "OFF";
	private static QQConsultationLogic qqConsultationLogic = null;
	
	private QQConsultationLogic(){}
	public static QQConsultationLogic getInstance(){
		if(qqConsultationLogic == null){
			qqConsultationLogic = new QQConsultationLogic();
		}
		return qqConsultationLogic;
	}
	
	public final String getCompanyQQLst(Connection con){
		String result = sqlMapClient.getObjectListAdapterToxmlData(con, "select cpq_id, cpq_code, cpq_title, cpq_number, cpq_create_timestamp, usr_display_bil from companyQQ inner join reguser on (cpq_create_ent_id = usr_ent_id) where cpq_status = 'ON' order by cpq_code asc", new Object[]{}, companyQQLst, companyQQ);
		 String status = sqlMapClient.getObjectListAdapterToxmlData(con, "select top(1)ttm_mod_status from TcrTemplateModule,TcrModule where ttm_tm_id = tm_id and tm_code = 'QQ_CONSULTATION' and ttm_mod_status =1",new Object[]{}, "company_QQ", "company_QQ_status");
		 return result + status.toString();
	}
	
	public final String disOperatingCompanyQQPre(Connection con, QQConsultationParam qqParam, loginProfile prof){
		if(qqParam.getOperating().equalsIgnoreCase(ADD)){
			return "<oprating>ADD</oprating><companyQQ></companyQQ>";
		}else if(qqParam.getOperating().equalsIgnoreCase(UPD)){
			return "<oprating>UPD</oprating>" + sqlMapClient.getObjectAdapterToXmlData(con, "select cpq_id, cpq_code, cpq_title, cpq_number, cpq_desc from companyQQ where cpq_id = ? and cpq_status = ?", 
					new Object[]{new Long(qqParam.getCpq_id()), "ON"}, companyQQ);
		}else{
			return "";
		}
	}
	
	public final void OperatingCompanyQQExe(Connection con, QQConsultationParam qqParam, loginProfile prof) throws SQLException{
		if(qqParam.getOperating().equalsIgnoreCase(ADD)){
			sqlMapClient.executeUpdate(con, "insert into companyQQ (cpq_code, cpq_title, cpq_number, cpq_desc, cpq_create_ent_id, cpq_update_ent_id) values (?, ?, ?, ?, ?, ?)", 
					new Object[]{qqParam.getCpq_code(), qqParam.getCpq_title(), qqParam.getCpq_number(), qqParam.getCpq_desc(), new Long(prof.usr_ent_id), new Long(prof.usr_ent_id)});
		}else if(qqParam.getOperating().equalsIgnoreCase(UPD)){
			sqlMapClient.executeUpdate(con, "update companyQQ set cpq_code = ?, cpq_title = ?, cpq_number = ?, cpq_desc = ?, cpq_update_timestamp = ?, cpq_update_ent_id = ? where cpq_id = ?", 
					new Object[]{qqParam.getCpq_code(), qqParam.getCpq_title(), qqParam.getCpq_number(), qqParam.getCpq_desc(), 
					cwSQL.getTime(con), new Long(prof.usr_ent_id), new Long(qqParam.getCpq_id())});
		}else if(qqParam.getOperating().equalsIgnoreCase(DEL)){
			sqlMapClient.executeUpdate(con, "delete from companyQQ where cpq_id in (#)", new Object[]{qqParam.getCpq_id_lst()});
		}else{
			throw new RuntimeException("Unknow Operating.\n");
		}
	}
	
	public final void updateQQStatus(Connection con ,QQConsultationParam qqParam) throws SQLException{
		if(qqParam.getOperating().equalsIgnoreCase(ON)){
			sqlMapClient.executeUpdate(con, "update tcrTemplateModule set ttm_mod_status= ? where ttm_tm_id in(select tm_id from tcrModule where tm_code= ? )", new Object[]{new Long(1),"QQ_CONSULTATION"});
		}else if(qqParam.getOperating().equalsIgnoreCase(OFF)){
			sqlMapClient.executeUpdate(con, "update tcrTemplateModule set ttm_mod_status= ? where ttm_tm_id in(select tm_id from tcrModule where tm_code= ? )", new Object[]{new Long(0),"QQ_CONSULTATION"});
		}else{
			throw new RuntimeException("Unknow Operation.\n");
		}
	}

}
