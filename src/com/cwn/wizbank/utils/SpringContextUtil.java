package com.cwn.wizbank.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
	
	// Spring应用上下文环境  
    private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		 SpringContextUtil.applicationContext = applicationContext;
	}
	
	 /** 
     * @return ApplicationContext 
     */  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
    
    /** 
     * 获取对象 
     * 这里重写了bean方法，起主要作用 
     * @param name 
     * @return Object 一个以所给名字注册的bean的实例 
     * @throws BeansException 
     */  
    public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);  
    }  
    
    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true 
     * @param name
     * @return boolean
     */
     public static boolean containsBean(String name) {
       return applicationContext.containsBean(name);
     }
    
     /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）   
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     */
     public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
       return applicationContext.isSingleton(name);
     }
    
     /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     */
     public static Class getType(String name) throws NoSuchBeanDefinitionException {
       return applicationContext.getType(name);
     }
    
     /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名   
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
     public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
       return applicationContext.getAliases(name);
     }

}
