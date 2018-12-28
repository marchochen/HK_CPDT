package com.cwn.wizbank.web.aspect;

import java.lang.reflect.Method;
import java.sql.Connection;

import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
/**
 * 
 * @author leon.li
 *
 */
@Aspect
public class ConnectionAspect {
	
		private final Logger logger = LoggerFactory.getLogger(ConnectionAspect.class);
		
		@Autowired
		DataSource dataSource;
		
		//@Autowired
		//SqlSessionTemplate sqlSessionTemplate;
		
		/** 
	     * 修改业务逻辑方法切入点  && (args(con,.) || args(..,con))
	     */  
	    @Pointcut("(execution(* com.cwn.wizbank.services.ForCallOldAPIService.*(..)) || execution(* com.cwn.wizbank.services.ModuleManagementService.*(..)))  ")
	    public void serviceCall() {}  
	
	    /**
	     * 给参数connection赋值
	     * @param pjp
	     * @return
	     * @throws Throwable
	     */
	   	@Around("serviceCall()")  
	    public Object allCalls(ProceedingJoinPoint pjp) throws Throwable {  
	          
	        Object result = null;  
			Connection con = null;
	        //环绕通知处理方法  
	        try {  
	        	 
	        	con = DataSourceUtils.getConnection(dataSource);

				int index = 0;	//参数的位置
				boolean hasCon = false;	//是否含有此参数
				Method method = null;	//拿到该方法
				for(Method mth : pjp.getTarget().getClass().getMethods()){
					if(mth.getName().equals(pjp.getSignature().getName())){
						method = mth;
						break;
					}
				}
				//判断是否含有此参数，以及参数的位置
				for(Class<?> cls : method.getParameterTypes()){
					if(!cls.isInstance(con) && !hasCon){
						index ++;
					} else {
						hasCon = true;
					}
				}
				
				if(!hasCon){
		            result = pjp.proceed();  
				} else {
					pjp.getArgs()[index] = con;
		            //执行操作  
		            result = pjp.proceed(pjp.getArgs());  
		        	logger.debug("pjp.proceed : " + pjp.getArgs());

				}
	               
	         } finally{
	        	DataSourceUtils.releaseConnection(con, dataSource);
	        	logger.debug("DataSourceUtils.releaseConnection(con, dataSource)");
	         }
	           
	         return result;  
	    }  
}
