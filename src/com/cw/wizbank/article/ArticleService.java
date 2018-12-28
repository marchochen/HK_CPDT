package com.cw.wizbank.article;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.supplier.utils.XMLHelper;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.utils.ContextPath;
/**
 * 
 * @author Leon.li 2013-5-24 4:41:20 
 * message: Supplier service 
 */
public class ArticleService {


	public static final String HAVE_COOPERATION = "a_have"; 
	public static final String BID_PASS = "b_pass"; 
	public static final String HAD_MEETING = "c_meet"; 
	public static final String FIRST_CONTACT = "d_contact"; 
	public static final String EXITED = "e_exited"; 
	public static final String DEL = "0"; 

	public static final String SPL_NAME = "996";
	public static final String SPL_STATUS = "997";
	public static final String SPL_REPRESENTATIVE= "1006";
	public static final String SPL_ESTABLISHEDDATE = "1007";
	public static final String SPL_REGISTEREDCAPITAL = "1008";
	public static final String SPL_TYPE = "1009";
	public static final String SPL_ADDRESS = "1010";
	public static final String SPL_CONTACT = "1011";
	public static final String SPL_TEL = "1012";
	public static final String SPL_MOBILE = "1013";
	public static final String SPL_EMAIL = "1014";
	public static final String SPL_TOTAL_STAFF = "1015";
	public static final String SPL_FULL_TIME_INST = "1016";
	public static final String SPL_PART_TIME_INST = "1017";
	public static final String SPL_EXPERTISE = "1018";
	public static final String SPL_COURSE = "1019";
	
	public static final String SHEET_NAME = "1035";
	public static final String SHEET2_NAME = "1036";
	
	public static final String SCE_START_DATE = "1025";
	public static final String SCE_END_DATE = "1026";
	public static final String SCE_ITM_NAME = "1027";
	public static final String SCE_DESC = "1028";
	public static final String SCE_DPT = "1029";


	//public static final String SCM_SCORE = "979";
	
	public static final int XLS_COULMN_WIDTH = 20;	
	
	
	ArticleDao artDao = new ArticleDao();

	/**
	 * 查询列表转成xml
	 * @param con
	 * @param modParam
	 * @param b
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public String getAllarticle2Xml(Connection con, ArticleModuleParam modParam, loginProfile prof,  boolean b) throws IllegalArgumentException, IllegalAccessException, SQLException {
		StringBuffer result = new StringBuffer();
		Article art = null;
		List<Article> list = getList(con, modParam, prof, DEL);

		result.append("<articles>");
		for (int i = 0; i < list.size(); i++) {
			art = list.get(i);
			result.append(XMLHelper.javaBeanToXML(art));
		}
		result.append("</articles>");
		result.append(modParam.getCwPage().asXML());
		return result.toString();
	}
	/**
	 * 获取列表
	 * @param con
	 * @param modParam
	 * @param suffix
	 * @return
	 * @throws SQLException
	 */
	public List<Article> getList(Connection con, ArticleModuleParam modParam, loginProfile prof, String suffix) throws SQLException{
		return artDao.list(con, modParam, prof, suffix);
	}


	public void delArticleById(Connection con, String artIdStr) throws SQLException {
		String[] artIds = artIdStr.split(",");
		artDao.delete(con, artIds);
		
	}


	public boolean isExists(Connection con, Integer artId) throws SQLException {
		return artDao.isExist(con, artId, DEL);
	}


	public void update(Connection con, loginProfile prof, ArticleModuleParam modParam) throws IllegalArgumentException, IllegalAccessException, SQLException {
		Article art = artDao.get(con, modParam.getArt_id(), DEL);
		Field[] fs = art.getClass().getDeclaredFields();
		Field[] params = modParam.getClass().getDeclaredFields();
		for (Field f : fs) {
			f.setAccessible(true);
			for (Field p : params) {
				p.setAccessible(true);
				if (f.getName().equals(p.getName())) {
					Object value = p.get(modParam);
					if(value==null || "".equals(value) || ("0".equals(value+"") && !p.getName().equalsIgnoreCase("art_status") && !p.getName().equalsIgnoreCase("art_push_mobile"))){
						break;
					}
					f.set(art, value);
					break;
				}
			}
		}
		art.setArt_update_user_id((int)(prof.usr_ent_id));
		if("msg_icon_default".equals(modParam.getArt_icon_select())){
			art.setArt_icon_file(modParam.getDefault_image());
		}
	/*	art.setMsg_icon_file(msg_icon_file);*/
		artDao.update(con,art);

	}


	public long add(Connection con, loginProfile prof, ArticleModuleParam modParam) throws SQLException, IllegalArgumentException, IllegalAccessException {
		Article art = new Article();

		Field[] fs = art.getClass().getDeclaredFields();
		Field[] params = modParam.getClass().getDeclaredFields();
		for (Field f : fs) {
			f.setAccessible(true);
			for (Field p : params) {
				p.setAccessible(true);
				if (f.getName().equals(p.getName())) {
					Object value = p.get(modParam);
					f.set(art, value);
					break;
				}
			}
		}
		art.setArt_user_id((int)prof.usr_ent_id);
		if("msg_icon_default".equals(modParam.getArt_icon_select())){
			art.setArt_icon_file(modParam.getDefault_image());
		}
		return artDao.insert(con, art);

	}
	
	public String getDetailXML(Connection con, Integer artId) throws IllegalArgumentException, IllegalAccessException, SQLException{
		Article art = artDao.get(con, artId, DEL);
		art.setArt_icon_file(ContextPath.getContextPath() + "/article/" + art.getArt_id() + "/" + art.getArt_icon_file());
		return XMLHelper.javaBeanToXML(art);
	}
	
	public String getTitleById(Connection con, Long artId) throws SQLException{
		return artDao.getTitleById(con, artId);
	}

}
