package com.cw.wizbank.qdb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.view.ViewAcHomePage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.utils.CommonLog;

public class loginProfile extends Object implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -15306863937652696L;
	public String      usr_id;
    public String      usr_pwd;
    public long        usr_ent_id; 
	public String      usr_display_bil;
	public String      usr_photo;
    public String      usr_status;	
    public String      usg_display_bil;
    public Timestamp   usr_last_login_date;
    public boolean     usr_last_login_success;
    public Timestamp   login_date;
    public String      encoding;
    public String      label_lan;
    public String      cur_lan;
    
    public long        root_ent_id;
// 用户所在的顶级培训中心ID
	public long        my_top_tc_id;
    public String      root_id;
    public String      root_code; 
    public String      root_level; 
    public String      root_display; 
    public String      usr_ste_usr_id;
    public Vector      sysRoles; 
    public Vector      usrGroups;
    public Vector      usrGrades;
    public boolean     isPublic;
    
    public String      xsl_root;
    public String      skin_root;
    
    public String      current_role;
    public String      current_role_xml;
    public String      current_role_skin_root;
    public String      role_url_home; //role homepage url in acRole.rol_url_home
    public String      common_role_id="NLRN";//the common role id between organization. do not use acRole.rol_ste_uid, cos its not same as the prefix of the current_role.
    public String      homeFtnXML; //role homepage function in XML format
    public String 	   usr_ftn_lst;	
    
    public String       current_login_status_xml;
    public String       last_login_status;
    public boolean      account_locked;
    public boolean      first_login;
    public boolean      isLrnRole;
    public boolean      bNeedToChangePwd;

	//当前用是否用移动客户端访问系统
    public boolean      isMobileDeviceClien;
    
    public boolean      hasStaff;
    
    //whether the type of login is single login.
    public boolean 		sso_login;
    
    public String 		directGroup;
    
    public String group_cmd;
    
    //LN模式所在培训中心id
    public String tcId;
    
	// only for prototype
	public boolean goAdmin;

	public String[] roles;
	
	public List<Map<String,String>> roleList;
	
	public String grantedRolesXML;
	
	public List<AcFunction> roleFunctions;
	
    public String purchase_api_url;
    public String show_alert_msg_type ; // firt_login, day_fist_login, moth_fist_login, year_fist_login
    public Float loginCredit;
	public Timestamp usr_pwd_upd_timestamp;
	public String ip;
	
	public boolean isCancelEnrol;
	public long cur_itm_id;

	public loginProfile() {
		sysRoles = new Vector();
		usrGroups = new Vector();
	}

	public String getCurLan(String lan_label) {
		String curLan = null;
		if (lan_label.equals("Big5")) {
			curLan = "zh-hk";
		}
		if (lan_label.equals("ISO-8859-1")) {
			curLan = "en-us";
		}
		if (lan_label.equals("GB2312")) {
			curLan = "zh-cn";
		}
		return curLan;
	}
    
	public void writeSession(HttpSession s) {
		if (this.usr_id != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_ID, this.usr_id);

		if (this.usr_pwd != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_PWD, this.usr_pwd);

		s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_ENT_ID, new Long(this.usr_ent_id));

		if (this.usr_display_bil != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_DISPLAY, this.usr_display_bil);

		if (this.usr_status != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_STUTAS, this.usr_status);

		if (this.usr_last_login_date != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_LAST_LOGIN_DATE, this.usr_last_login_date);

		if (this.encoding != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_ENCODING, this.encoding);

		if (this.label_lan != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_LABEL_LAN, this.label_lan);

		this.cur_lan = getCurLan(this.label_lan);

		s.setAttribute(qdbAction.SESS_QDB_PROFILE_CUR_LAN, this.cur_lan);

		s.setAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_ENT_ID, new Long(this.root_ent_id));

		if (this.root_code != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_CODE, this.root_code);

		if (this.root_level != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_LEVEL, this.root_level);

		if (this.root_display != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_DISPLAY, this.root_display);

		if (this.usr_ste_usr_id != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_STE_USR_ID, this.usr_ste_usr_id);

		if (this.sysRoles != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_SYS_ROLES, this.sysRoles);

		if (this.usrGroups != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_GROUPS, this.usrGroups);

		s.setAttribute(qdbAction.SESS_QDB_PROFILE_IS_PUBLIC, new Boolean(this.isPublic));

		if (this.xsl_root != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_XSL_ROOT, this.xsl_root);

		if (this.skin_root != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_SKIN_ROOT, this.skin_root);

		if (this.current_role != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_ROLE, this.current_role);

		if (this.current_role_xml != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_ROLE_XML, this.current_role_xml);

		if (this.homeFtnXML != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_HOME_FTN_XML, this.homeFtnXML);

		if (this.current_role_skin_root != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_ROLE_SKIN_ROOT, this.current_role_skin_root);

		if (this.role_url_home != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_ROLE_URL_HOME, this.role_url_home);

		if (this.last_login_status != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_LAST_LOGIN_STATUS, this.last_login_status);

		if (this.current_login_status_xml != null)
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_LOGIN_STATUS_XML, this.current_login_status_xml);

		s.setAttribute(qdbAction.SESS_QDB_PROFILE_ACCOUNT_LOCKED, new Boolean(this.account_locked));

		s.setAttribute(qdbAction.SESS_QDB_PROFILE_FIRST_LOGIN, new Boolean(this.first_login));

		if (this.common_role_id != null) {
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_COMMON_ROLE_ID, this.common_role_id);
		}
		
		if (!StringUtils.isEmpty(this.getIp())){
			s.setAttribute(qdbAction.SESS_QDB_PROFILE_USR_LOGIN_IP_ADDR, this.getIp());
		}
		
		s.setAttribute(qdbAction.SESS_QDB_PROFILE_SHOW_ALERT_MSG_TYPE, this.show_alert_msg_type);
		
		s.setAttribute(qdbAction.SESS_QDB_PROFILE_LOGINCREDIT, this.loginCredit);
		
		try {
			s.setAttribute(qdbAction.SESS_QDB_CPD_ENABLE, AccessControlWZB.hasCPDFunction());
		} catch (SQLException e) {
			CommonLog.error(e.getMessage());
			s.setAttribute(qdbAction.SESS_QDB_CPD_ENABLE, false);
		}
		
		return;
	}

	public void readSession(HttpSession s) {
		this.usr_id = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_ID);
		this.usr_pwd = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_PWD);
		this.usr_ent_id = ((Long) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_ENT_ID)).longValue();
		this.usr_display_bil = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_DISPLAY);
		this.usr_status = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_STUTAS);
		this.usr_last_login_date = (Timestamp) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_LAST_LOGIN_DATE);
		this.encoding = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_ENCODING);
		this.label_lan = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_LABEL_LAN);
		this.cur_lan = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_CUR_LAN);
		this.root_ent_id = ((Long) s.getAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_ENT_ID)).longValue();
		this.root_code = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_CODE);
		this.root_level = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_LEVEL);
		this.root_display = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_ROOT_DISPLAY);
		this.usr_ste_usr_id = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_STE_USR_ID);
		this.sysRoles = (Vector) s.getAttribute(qdbAction.SESS_QDB_PROFILE_SYS_ROLES);
		this.usrGroups = (Vector) s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_GROUPS);
		this.isPublic = ((Boolean) s.getAttribute(qdbAction.SESS_QDB_PROFILE_IS_PUBLIC)).booleanValue();
		this.xsl_root = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_XSL_ROOT);
		this.skin_root = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_SKIN_ROOT);
		this.current_role = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_ROLE);
		this.current_role_xml = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_ROLE_XML);
		this.homeFtnXML = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_HOME_FTN_XML);
		this.current_role_skin_root = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_ROLE_SKIN_ROOT);
		this.role_url_home = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_ROLE_URL_HOME);
		this.last_login_status = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_LAST_LOGIN_STATUS);
		this.current_login_status_xml = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_CURRENT_LOGIN_STATUS_XML);
		this.account_locked = ((Boolean) s.getAttribute(qdbAction.SESS_QDB_PROFILE_ACCOUNT_LOCKED)).booleanValue();
		this.first_login = ((Boolean) s.getAttribute(qdbAction.SESS_QDB_PROFILE_FIRST_LOGIN)).booleanValue();
		this.common_role_id = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_COMMON_ROLE_ID);
		this.show_alert_msg_type = (String) s.getAttribute(qdbAction.SESS_QDB_PROFILE_SHOW_ALERT_MSG_TYPE);
		this.loginCredit = (Float)s.getAttribute(qdbAction.SESS_QDB_PROFILE_LOGINCREDIT);
		this.ip = (String)s.getAttribute(qdbAction.SESS_QDB_PROFILE_USR_LOGIN_IP_ADDR);
		return;
	}
    
    /**
     * 用于将用户的相关信息转换成相应的XML数据，并以字符串的形式返回
     * @return
     */
	public String asXML() {
		String rootIdStr = (root_ent_id == 0) ? "" : Long.toString(root_ent_id);
		StringBuffer result = new StringBuffer(2048);
		result.append("<cur_usr id=\"").append(dbUtils.esc4XML(usr_ste_usr_id)).append("\" ");
		result.append("usr_id=\"").append(dbUtils.esc4XML(usr_id)).append("\" ");
		result.append("site_usr_id=\"").append(dbUtils.esc4XML(usr_ste_usr_id)).append("\" ");
		result.append("ent_id=\"").append(usr_ent_id).append("\" ");
		result.append("root_ent_id=\"").append(rootIdStr).append("\" ");
		result.append("ismobiledeviceclien=\"").append(isMobileDeviceClien).append("\" ");    
		result.append("root_id=\"").append(root_id).append("\" ");
		result.append("root_code=\"").append(root_code).append("\" ");
		result.append("root_level=\"").append(root_level).append("\" ");
		result.append("root_display=\"").append(dbUtils.esc4XML(root_display)).append("\" ");
		result.append("encoding=\"").append(encoding).append("\" ");
		result.append("label=\"").append(label_lan).append("\" ");
		result.append("curLan=\"").append(cur_lan).append("\" ");
		result.append("style=\"").append(skin_root).append("/").append(current_role_skin_root).append("/\" ");
		result.append("skin=\"").append(current_role_skin_root).append("\" ");
		result.append("role_url_home=\"").append((role_url_home == null) ? "" : dbUtils.esc4XML(role_url_home)).append("\" ");
		result.append("common_role_id=\"").append(common_role_id).append("\" ");
		result.append("display_bil=\"").append(dbUtils.esc4XML(usr_display_bil)).append("\">");
		result.append(current_role_xml);
		result.append(homeFtnXML);
		result.append("<login first_time=\"").append(first_login).append("\" locked=\"").append(account_locked).append("\">");
		result.append(current_login_status_xml);
		result.append("</login>");
		result.append("</cur_usr>");

		return (result.toString());
	}
    
    public String usrGroupsList()
    {
        String result = "(0"; 
        for(int i=0;i<usrGroups.size();i++) 
        {
            result += ", " + ((Long) usrGroups.elementAt(i)).toString();
            
        }
        result += ", " + usr_ent_id ; 
        result += ")";
        return result;
    }

    public String profAsXML(String domain) 
    {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<user domain=\"" + domain + "\">" + dbUtils.NEWL;     
        result += asXML(); 
        result += "</user>" + dbUtils.NEWL ; 
        
        return result;
    }
    
    public loginProfile copy() throws CloneNotSupportedException {
        //loginProfile prof = (loginProfile) this.clone();
        loginProfile prof = new loginProfile();
        prof.usr_id = usr_id;
        prof.usr_pwd = usr_pwd;
        prof.usr_ent_id = usr_ent_id; 
        prof.usr_display_bil = usr_display_bil;
        prof.usr_last_login_date = usr_last_login_date;
        prof.login_date = login_date;
        prof.encoding = encoding;
        prof.label_lan = label_lan;
        prof.cur_lan = cur_lan;
        prof.root_ent_id = root_ent_id;
        prof.root_code = root_code; 
        prof.root_level = root_level; 
        prof.root_display = root_display; 
        prof.usr_ste_usr_id = usr_ste_usr_id;
//        prof.sysRoles = sysRoles; 
        prof.usrGroups = usrGroups;
        prof.isPublic = isPublic;
        prof.xsl_root = xsl_root;
        prof.skin_root = skin_root;
        prof.current_role = prof.current_role;
        prof.current_role_xml = current_role_xml;
        prof.current_role_skin_root = current_role_skin_root;
        prof.homeFtnXML = homeFtnXML; 
        prof.last_login_status = last_login_status;
        prof.current_login_status_xml = current_login_status_xml;
        prof.account_locked = account_locked;
        prof.first_login = first_login;
        prof.common_role_id = common_role_id;
        prof.show_alert_msg_type = show_alert_msg_type;
        prof.loginCredit = loginCredit;
        return prof;
    }
    
    /**
    Return the home location of this loginProfile
    */
    public String goHome(HttpServletRequest request) {
        StringBuffer url_home = new StringBuffer(512);
        if(this.role_url_home != null) {
            String requestPath = request.getServletPath();
            String prefix;
            //if the servlet context starts with /servlet
            //the home url should go up 1 level
            //else assume the servlet context is inside skin folder 
            //(e.g. /cw/skin1/jsp/) the home url should go up 3 level
            if(requestPath.startsWith("/servlet")) {
                prefix = "../";
            } else if(requestPath.startsWith("/htm")) {
            	prefix = "../";
            } else if(requestPath.startsWith("/app")) {
            	prefix = "../../../../";
            } else {
                prefix = "../../../";
            }
            //if this profile's home starts with servlet
            //need not to add skin in front of it
            //else adds skin path in front of it
            if(this.role_url_home.startsWith("servlet") || role_url_home.startsWith("htm")|| role_url_home.startsWith("app")) {
                url_home.append(prefix).append(this.role_url_home);
            } else {
                url_home.append(prefix).append(skin_root)
                        .append("/").append(current_role_skin_root)
                        .append("/").append(this.role_url_home);
            }
        }
        return url_home.toString();
    }
    
    public static String getHomeXML(Connection con, HttpSession sess, loginProfile prof, String stylesheet, boolean tc_enabled) throws qdbException, SQLException, cwException, qdbErrMessage, cwSysMessage {

    	StringBuffer xmlBuf = new StringBuffer(1024);
    	ViewAcHomePage view = new ViewAcHomePage(con);
//    	Hashtable functions = view.getGrantedPrivilege(prof.current_role, prof);
//    	Vector v_ftn_ext_id = (Vector) functions.get(ViewAcHomePage.FTN_EXT_ID);
    	
		//wb_gen_role.xsl
		xmlBuf.append(prof.grantedRolesXML);

        // is out the warning threshold setting
        Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
        long blockThreshold = 0;
		long warnThreshold = 0;
		if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString().length() > 0){
			blockThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString());
		}
		if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString().length() > 0){
			warnThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString());
		}
        long loginUserCount = CurrentActiveUser.getcurActiveUserCount(con);
        boolean warn = false;
        if((blockThreshold > 0 && loginUserCount >= blockThreshold)
        		|| (warnThreshold > 0 && loginUserCount >= warnThreshold)) {
        	warn = true;
        }
        xmlBuf.append("<sys_warning>").append(warn).append("</sys_warning>");

        return xmlBuf.toString();
      }
    
    public Float getLoginCredit() {
		return loginCredit;
	}

    public String getUsr_id() {
		return usr_id;
	}
    
	public String getUsr_photo() {
		return usr_photo;
	}

	public void setUsr_photo(String usr_photo) {
		this.usr_photo = usr_photo;
	}
	
	public String getUsg_display_bil() {
		return usg_display_bil;
	}

	public void setUsg_display_bil(String usg_display_bil) {
		this.usg_display_bil = usg_display_bil;
	}

	public long getUsr_ent_id() {
		return usr_ent_id;
	}

	public String getUsr_display_bil() {
		return usr_display_bil;
	}

	public String getRoot_id() {
		return root_id;
	}

	public String getUsr_ste_usr_id() {
		return usr_ste_usr_id;
	}

	public boolean isLrnRole() {
		return isLrnRole;
	}
	
	public boolean getIsLrnRole() {
		return isLrnRole;
	}

	public Timestamp getUsr_last_login_date() {
		return usr_last_login_date;
	}

	public boolean isUsr_last_login_success() {
		return usr_last_login_success;
	}

	public String getLast_login_status() {
		return last_login_status;
	}

	public boolean isHasStaff() {
		return hasStaff;
	}

	public void setHasStaff(boolean hasStaff) {
		this.hasStaff = hasStaff;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getLabel_lan() {
		return label_lan;
	}

	public void setLabel_lan(String label_lan) {
		this.label_lan = label_lan;
	}

	public String getCur_lan() {
		return cur_lan;
	}

	public void setCur_lan(String cur_lan) {
		this.cur_lan = cur_lan;
	}
	
	public String getCur_label_lan() {
		String label_lan = "en";
		if (cwUtils.notEmpty(this.label_lan)) {
			if (this.label_lan.equals("Big5")) {
				label_lan = "ch";
			} else if (this.label_lan.equals("GB2312")) {
				label_lan = "gb";
			} else if (this.label_lan.equals("ISO-8859-1")) {
				label_lan = "en";
			}
		}
		return label_lan;
	}

	public String getPurchase_api_url() {
		return purchase_api_url;
	}

	public void setPurchase_api_url(String purchase_api_url) {
		this.purchase_api_url = purchase_api_url;
	}
	
	public String getCommon_role_id() {
        return common_role_id;
    }

    public void setCommon_role_id(String commonRoleId) {
        common_role_id = commonRoleId;
    }
    
    public String getRole_url_home() {
        return role_url_home;
    }

    public void setRole_url_home(String roleUrlHome) {
        role_url_home = roleUrlHome;
    }
    public long getRoot_ent_id() {
		return root_ent_id;
	}

	public void setRoot_ent_id(long rootEntId) {
		root_ent_id = rootEntId;
	}

	public List<Map<String, String>> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Map<String, String>> roleList) {
		this.roleList = roleList;
	}

	public List<AcFunction> getRoleFunctions() {
		return roleFunctions;
	}

	public void setRoleFunctions(List<AcFunction> roleFunctions) {
		this.roleFunctions = roleFunctions;
	}

	public String getCurrent_role() {
		return current_role;
	}

	public void setCurrent_role(String current_role) {
		this.current_role = current_role;
	}
	
	public String getShow_alert_msg_type() {
		return show_alert_msg_type;
	}

	public void setShow_alert_msg_type(String show_alert_msg_type) {
		this.show_alert_msg_type = show_alert_msg_type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}