/**
 * 
 */
package com.cwn.wizbank.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.exception.BeanAssignException;

/**
 * @author linchers@gmail.com
 * 2014年5月24日 下午1:36:52
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	/**
	 * obj0 的值赋给 obj1
	 * @param obj0 带有值的对象
	 * @param obj1 接受赋值的对象
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws BeanAssignException 
	 */
	public static void setValue(Object obj0, Object obj1) throws BeanAssignException{
		try {
			Field[] fields = obj0.getClass().getDeclaredFields();
			Field[] accepts = obj1.getClass().getDeclaredFields();
			for (Field apt : accepts) {
				boolean accessible = apt.isAccessible();
				apt.setAccessible(true);
				if (Modifier.isFinal(apt.getModifiers())) {
					continue;
				}
				for (Field f : fields) {
					boolean isaccessible = f.isAccessible();
					f.setAccessible(true);
					if (f.getName().equals(apt.getName())) {
						apt.set(obj1, f.get(obj0));
						f.setAccessible(isaccessible);
						break;
					}
					f.setAccessible(isaccessible);
				}
				apt.setAccessible(accessible);
			}
		} catch (Exception e) {
			throw new BeanAssignException("对象赋值出错");
		}
	}
	
	
	/**
	 * obj0 的值赋给 obj1
	 * @param obj0 带有值的对象
	 * @param obj1 接受赋值的对象
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws BeanAssignException 
	 */
	public static void setNotNullValue(Object obj0, Object obj1) throws BeanAssignException{
		try {
			Field[] fields = obj0.getClass().getDeclaredFields();
			Field[] accepts = obj1.getClass().getDeclaredFields();
			for (Field apt : accepts) {
				boolean accessible = apt.isAccessible();
				apt.setAccessible(true);
				if (Modifier.isFinal(apt.getModifiers())) {
					continue;
				}
				for (Field f : fields) {
					boolean isaccessible = f.isAccessible();
					f.setAccessible(true);
					if (f.getName().equals(apt.getName()) && f.get(obj0) != null) {
						apt.set(obj1, f.get(obj0));
						f.setAccessible(isaccessible);
						break;
					}
					f.setAccessible(isaccessible);
				}
				apt.setAccessible(accessible);
			}
		} catch (Exception e) {
			throw new BeanAssignException("对象赋值出错");
		}
	}
	
	public static void main(String[] args) throws BeanAssignException {
		SnsDoing obj1 = new SnsDoing();
		obj1.setS_doi_title("我乐观其u");
		SnsDoing obj2 = new SnsDoing();
		setValue(obj1, obj2);
		System.out.println(obj2.getS_doi_title());
	}
}
