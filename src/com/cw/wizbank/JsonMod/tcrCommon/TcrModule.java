package com.cw.wizbank.JsonMod.tcrCommon;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.dao.Log4jFactory;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;


public class TcrModule extends ServletModule {
	protected final Class NATIVE_CLASS = this.getClass();
	protected final Logger logger = Log4jFactory.createLogger(NATIVE_CLASS);
	protected final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	protected final SqlMapClientDataSource sqlMapClient = SQLMapClientFactory.getSqlMapClient();
	protected final AccessControlWZB acWZB = new AccessControlWZB();
	protected final String TC_MAIN = "TC_MAIN";
	
	protected boolean isTtcModule = true;
	protected TcrParam tcrParam = null;
	protected StringBuffer dataXml = new StringBuffer();
	protected boolean isTransactionException = false;
	private String funtionId = null;
	private String[] witchMethodwithoutNeedTopopedomValidate = null;
	private String[] witchMethodTcrIdCanBeZero = null;

	public TcrModule(String funtionId, Class tcrParamClass,
			String[] witchMethodwithoutNeedTopopedomValidate,
			String[] witchMethodTcrIdCanBeZero) {
		this.funtionId = (funtionId == null || funtionId.equals("")) ? TC_MAIN
				: funtionId;
		try {
			this.tcrParam = (TcrParam) tcrParamClass.newInstance();
		} catch (Exception e) {
			logger.error("the param must extend tcrParam.", e);
			throw new RuntimeException("the param must extend tcrParam.");
		}
		param = this.tcrParam;
		this.witchMethodwithoutNeedTopopedomValidate = witchMethodwithoutNeedTopopedomValidate;
		this.witchMethodTcrIdCanBeZero = witchMethodTcrIdCanBeZero;
	}
	
	private static boolean jurgeIsTheMethod(String cmd, String[] witchMethod) {
		if (witchMethod != null) {
			for (int i = 0; i < witchMethod.length; i++) {
				if (cmd.startsWith(witchMethod[i])) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * 判断该用户是否是该培训中心的超级管理员
	 */
	private boolean isTrainingOffer(long tcr_id, long usrId) {
//		Object[] params = new Object[] { new Long(tcr_id), new Long(usrId), "TADM_1", "OK" };
//		boolean result = sqlMapClient.getObject(con,
//				"select distinct tcr_id from tcTrainingCenter,tcRelation,tcTrainingCenterOfficer where tcr_id = ? and tco_usr_ent_id = ? and tco_rol_ext_id = ? and tcr_status = ? and ( tcr_id = tco_tcr_id or (tcr_id = tcn_child_tcr_id and tcn_ancestor = tco_tcr_id))", params) != null ? true
//				: false;
		AcTrainingCenter actc = new AcTrainingCenter(con);
		boolean result = false;
		try {
			result = actc.canTaMgtTc(usrId, tcr_id);
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		}
		if (!result) {
			sysMsg = getErrorMsg("ACL002", param.getUrl_failure());// lable_zh.js
		}
		return result;
	}

	// 判断用户是否具有权限
	private boolean isGoOn() throws SQLException, cwException, IOException {
		// 若还是未登录
		if (prof == null || this.prof.usr_ent_id == 0) {
			response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
			return false;
		}
		// 如果不是学员
		if (!prof.isLrnRole) {
			//如果不需要进行权限验证
			if(!jurgeIsTheMethod(this.tcrParam.getCmd(), witchMethodwithoutNeedTopopedomValidate)){
				return true;
			}
			// 判断用户的当前角色是否具有该功能
//			if (!acWZB.hasUserPrivilege(prof.current_role, funtionId)) {
//				sysMsg = getErrorMsg("ACL002", param.getUrl_failure());// lable_zh.js
//				return false;
//			}
			//如果是培训中心Module侧
			if(isTtcModule){
				// 如果不是应用管理员
				if (wizbini.cfgTcEnabled
						&& prof.current_role
								.startsWith(AccessControlWZB.ROL_STE_UID_TADM)) {
					// 判断是否具有培训中心的管理员身份
					if (!ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id)) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());// lable_zh.js
						return false;
					}
	                //判断该用户是否是该培训中心的管理员
					if (jurgeIsTheMethod(this.tcrParam.getCmd(),
							witchMethodTcrIdCanBeZero)
							&& !isTrainingOffer(tcrParam.getTcr_id(),
									prof.usr_ent_id)) {
						sysMsg = getErrorMsg("ACL002", param.getUrl_failure());// lable_zh.js
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * 请求执行的入口 (non-Javadoc)
	 * 
	 * @see com.cw.wizbank.ServletModule#process()
	 */
	public void process() throws SQLException, cwException, IOException {
		String cmd = tcrParam.getCmd();
		String methodName = cmd.endsWith("_xml") ? cmd.substring(0, cmd
				.indexOf("_xml")) : cmd;
		if (!isGoOn())
			return;
		try {
			encoding(tcrParam);
			Method processMethod = NATIVE_CLASS.getMethod(methodName,
					new Class[] {});
			processMethod.invoke(this, new Object[] {});
		} catch (Exception e) {
			logger.error("method " + methodName + "() process error.", e);
			throw new cwException("method " + methodName + "() process error.");
		}finally{
			if(isTransactionException){
				con.rollback();
			}
		}
	}
	
	protected void encoding(BaseParam baseParam)throws Exception{
		Class baseParamClassType = baseParam.getClass();
		Field[] fields = baseParamClassType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].getType() == String.class){
				String fieldName = fields[i].getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				String fieldValue = (String)baseParamClassType.getMethod(getMethodName, new Class[]{}).invoke(baseParam, new Object[]{});
				if(fieldValue != null){
					fieldValue = cwUtils.unicodeFrom(fieldValue, baseParam.getClientEnc(), baseParam.getEncoding());
					String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					baseParamClassType.getMethod(setMethodName, new Class[]{String.class}).invoke(baseParam, new Object[]{fieldValue});
				}
			}
		}
	}
}
