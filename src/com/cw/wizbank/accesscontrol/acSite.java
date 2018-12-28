package com.cw.wizbank.accesscontrol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.util.cwEncode;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class acSite {
    
    public static final String MSG_LIC_NOT_VALID            = "LIC001";
    public static final String MSG_LIC_MAX_USER_EXCEED      = "LIC002";
    public static final String MSG_LIC_EXPIRY               = "LIC003";
    public static final String MSG_LIC_MAX_ACT_ITEM_EXCEED  = "LIC004";
    
    public static final String STATUS_OK = "OK";
    public static final String DOMAIN_SEPARATOR = "[|]";
    public static final String DN_SEPARATOR = "[|]";
    public static final String STE_TYPE_ENTERPRISE = "ENTERPRISE";
    public static final String STE_TYPE_STANDARD = "STANDARD";
    public static final String DOMAIN_HTTP = "http";
    public static final String DOMAIN_HTTPS = "https";
    public static final String DOMAIN_DISPATCHER = "Dispatcher";
    public static final String DOMAIN_FORBIDDEN = "HTTP Error 403. Forbidden.";
    public static final String DOMAIN_REQUEST_ERROR ="HTTP Error 404. Please use a POST request."; 
    public String encoding;
    public String style;
    
    public long ste_ent_id;
    public String ste_id;
    public String ste_name;
    public Timestamp ste_join_datetime;
    public String ste_status;
    public String ste_domain;
    public String ste_login_url;
    public long ste_ird_id;
    public boolean ste_trusted; 
    public String ste_max_users;
    public String ste_eff_start_date;
    public String ste_eff_end_date;
    public String ste_lan_xml;
    
    private long ste_default_sys_ent_id;
    public String ste_ldap_host;
    public String ste_ldap_dn;
    public String ste_developer_id;

    public long ste_usr_pwd_valid_period;
    
	private WizbiniLoader wizbini = null;

        
    public acSite()  {; }
    
    public acSite(String encoding_, String style_) 
    {
        encoding = encoding_;
        style = style_;
    }

	public void setWizbiniLoader(WizbiniLoader wizbini){
		this.wizbini = wizbini;
		return;
	}


    private static final String SQL = 
        " Select ste_default_sys_ent_id From acSite where ste_ent_id = ? ";
    public long getSiteSysEntId(Connection con) throws SQLException {
                    
        PreparedStatement stmt1 = con.prepareStatement(SQL);
        stmt1.setLong(1, this.ste_ent_id);

        ResultSet rs = stmt1.executeQuery();
        
        if(rs.next())
        {
            this.ste_default_sys_ent_id = rs.getLong("ste_default_sys_ent_id");
        }
        rs.close();
        stmt1.close();
        
       return this.ste_default_sys_ent_id;
    }
    
    public static PreparedStatement getSite(Connection con) throws SQLException{
        String SQL =" SELECT ste_ent_id, ste_id, ste_name, ste_domain, "
                + " ste_max_users, ste_eff_start_date, ste_eff_end_date "
                + " ,ste_lan_xml "
                + " FROM acSite "
                + " WHERE ste_status = ? "
                + " ORDER BY ste_name ASC ";
            
        PreparedStatement stmt = con.prepareStatement(SQL);                      
        stmt.setString(1,STATUS_OK);
        return stmt;
        
    }
    
    public String siteAsXML(Connection con)
        throws SQLException
    {
        return siteAsXML(con, null);
    }
    
    public String siteAsXML(Connection con, String cur_label)
        throws SQLException
    {
        Timestamp curTime = cwSQL.getTime(con);

        StringBuffer siteXml = new StringBuffer();
        siteXml.append(cwUtils.xmlHeader);
        siteXml.append("<universe encoding=\"").append(encoding)
               .append("\" style=\"").append(style).append("/").append(wizbini.cfgSysSkinList.getDefaultId())
               .append("\" label=\"").append(cur_label)
               .append("\" skin=\"").append(wizbini.cfgSysSkinList.getDefaultId())
               .append("\">").append(cwUtils.NEWL);

        PreparedStatement stmt = getSite(con);
        ResultSet rs = stmt.executeQuery();
        Timestamp start_date = null;
        Timestamp end_date = null;
                    
        while (rs.next())
        {
            start_date = null;
            end_date = null;
            ste_eff_start_date = rs.getString("ste_eff_start_date");
            ste_eff_end_date = rs.getString("ste_eff_end_date");
            
            try {
                if (cwEncode.checkValidKey(ste_eff_start_date) && 
                    cwEncode.checkValidKey(ste_eff_end_date)) {

                    start_date = Timestamp.valueOf(cwEncode.decodeKey(ste_eff_start_date));
                    end_date = Timestamp.valueOf(cwEncode.decodeKey(ste_eff_end_date));
                }                    
            }catch (Exception e) {
                
                // do nothing
            }
            // Expiry date decoded successfully
            if (start_date !=null && end_date != null) {
                // Not expired yet
                if (curTime.after(start_date) && curTime.before(end_date)) {
                    siteXml.append("<site id=\"").append(rs.getLong("ste_ent_id"))
                        .append("\" ste_id=\"").append(rs.getString("ste_id"))
                        .append("\" domain=\"").append(rs.getString("ste_domain"))
                        .append("\"");
                        
                        if( this.wizbini != null ) {
                        	siteXml.append(" account_reg=\"");
                            siteXml.append(((UserManagement)wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getAccountRegistration().isActive());
                            siteXml.append("\" ");
                                boolean checkTrial = ((UserManagement)wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getAccountSuspension().isActive();
                            if (checkTrial) {
                        	    siteXml.append(" login_max_trial=\"")
                        			    .append(((UserManagement)wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getAccountSuspension().getMaxTrial())
                        			    .append("\" ");
                        	}
                            siteXml.append(" forget_pwd=\"");
                            siteXml.append(((UserManagement)wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getForgetPassword().isActive());
                            siteXml.append("\" ");
                        }
                    siteXml.append(">");
                    siteXml.append(cwUtils.esc4XML(rs.getString("ste_name")));
                    siteXml.append("</site>").append(cwUtils.NEWL);
                }
            }
        }
        siteXml.append("</universe>").append(cwUtils.NEWL);
        
        stmt.close();
            
        return siteXml.toString();
            
    }
    
    public void get(Connection con) 
        throws SQLException
    {
        String SQL = " SELECT ste_ent_id   "
                   + "       ,ste_id "
                   + "       ,ste_name "
                   + "       ,ste_join_datetime "
                   + "       ,ste_status " 
                   + "       ,ste_domain "
                   + "       , ste_login_url "
                   + "       , ste_trusted "
                   + "       , ste_ird_id "
                   + "       , ste_max_users "
                   + "       , ste_eff_start_date "
                   + "       , ste_eff_end_date "
                   + "       , ste_lan_xml "
                   + "       , ste_ldap_host "
                   + "       , ste_ldap_dn "
                   + " FROM acSite "
                   + " WHERE ste_ent_id = ? ";
            
        PreparedStatement stmt = con.prepareStatement(SQL);                      
        stmt.setLong(1,ste_ent_id);
        ResultSet rs = stmt.executeQuery();
            
        if (rs.next())
        {
            ste_ent_id = rs.getLong("ste_ent_id");
            ste_id = rs.getString("ste_id");
            ste_name = rs.getString("ste_name");
            ste_join_datetime = rs.getTimestamp("ste_join_datetime");
            ste_status = rs.getString("ste_status");
            ste_domain = rs.getString("ste_domain");
            ste_login_url = rs.getString("ste_login_url");
            ste_ird_id = rs.getLong("ste_ird_id");
            ste_trusted = rs.getBoolean("ste_trusted");
            // Encoded value 
            ste_max_users = rs.getString("ste_max_users");
            ste_eff_start_date = rs.getString("ste_eff_start_date");
            ste_eff_end_date = rs.getString("ste_eff_end_date");
            ste_lan_xml = rs.getString("ste_lan_xml");
            ste_ldap_host = rs.getString("ste_ldap_host");
            ste_ldap_dn = rs.getString("ste_ldap_dn");
            if (ste_lan_xml == null) {
                ste_lan_xml = new String();
            }
        }
            
        stmt.close();
    
    }
    
    public boolean getByDomain(Connection con, String host) 
        throws SQLException
    {
        // Get the domain name 
        // Example  : host = wizbank.cyberwisdom.net , domain = cyberwisdom.net
        ste_domain = host.substring(host.indexOf(".")+1, host.length());
        
        String SQL = " SELECT ste_ent_id   "
                   + "       ,ste_id "
                   + "       ,ste_name "
                   + "       ,ste_join_datetime "
                   + "       ,ste_status " 
                   + "       ,ste_domain "
                   + "       , ste_login_url "
                   + "       , ste_trusted "
                   + "       , ste_max_users "
                   + "       , ste_eff_start_date "
                   + "       , ste_eff_end_date "
                   + "       , ste_lan_xml "
                   + "       , ste_ldap_host "
                   + "       , ste_ldap_dn "
                   + " FROM acSite "
                   + " WHERE ste_domain = ? ";
            
        PreparedStatement stmt = con.prepareStatement(SQL);                      
        stmt.setString(1,ste_domain);
        ResultSet rs = stmt.executeQuery();
        boolean bExist = false;
        if (rs.next())
        {
            bExist = true;
            ste_ent_id = rs.getLong("ste_ent_id");
            ste_id = rs.getString("ste_id");
            ste_name = rs.getString("ste_name");
            ste_join_datetime = rs.getTimestamp("ste_join_datetime");
            ste_status = rs.getString("ste_status");
            ste_domain = rs.getString("ste_domain");
            ste_login_url = rs.getString("ste_login_url");
            ste_trusted = rs.getBoolean("ste_trusted");
            // Encoded value 
            ste_max_users = rs.getString("ste_max_users");
            ste_eff_start_date = rs.getString("ste_eff_start_date");
            ste_eff_end_date = rs.getString("ste_eff_end_date");
            ste_lan_xml = rs.getString("ste_lan_xml");
            ste_ldap_host = rs.getString("ste_ldap_host");
            ste_ldap_dn = rs.getString("ste_ldap_dn");
            if (ste_lan_xml == null) {
                ste_lan_xml = new String();
            }
        }
            
        stmt.close();
        return bExist;
    }
    
    
    public static long getMaxUsersAllowed(Connection con, long site_id) 
        throws SQLException, cwException, cwSysMessage
    {
        String SQL = " SELECT ste_max_users  "
                   + " FROM acSite "
                   + " WHERE ste_ent_id = ? ";
            
        PreparedStatement stmt = con.prepareStatement(SQL);                      
        stmt.setLong(1,site_id);
        ResultSet rs = stmt.executeQuery();
        String encoded_val = new String();
        if (rs.next())
        {
            encoded_val = rs.getString("ste_max_users");
        }else {
            throw new cwException("Failed to get maxium numbers of users allowed.");
        }
        
        stmt.close();
            
        if (!cwEncode.checkValidKey(encoded_val)) {
            throw new cwSysMessage(MSG_LIC_NOT_VALID);
        }
        
        long max_num = 0 ;
        try {
            max_num = Long.parseLong(cwEncode.decodeKey(encoded_val));
        }catch (Exception e) {
            throw new cwSysMessage(MSG_LIC_NOT_VALID);
        }
        
        return max_num;
    
    }
    
    public static void updRateDefId(Connection con, long siteId, long rateDefId) throws SQLException{
        String sql = "UPDATE acSite set ste_ird_id = ? WHERE ste_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, rateDefId);
        stmt.setLong(2, siteId);
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    /*
    * Get the site system user email 
    * @param site id
    */
    public static String getSysEmail_1(Connection con, long site_id)
        throws SQLException, cwException {
            
            String SQL = " SELECT usr_email FROM RegUser , acSite "
                       + " WHERE ste_ent_id = ? "
                       + " AND ste_default_sys_ent_id = usr_ent_id ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, site_id);
            ResultSet rs = stmt.executeQuery();
            String email = null;
            if( rs.next() )
                email = rs.getString("usr_email");
            else
                throw new cwException(" Failed to get site system user email , site id = " + site_id);
            
            stmt.close();
            return email;
            
        }

    public static String getSysEmail_2(Connection con, long site_id)
        throws SQLException, cwException {
            
            String SQL = " SELECT usr_email_2 FROM RegUser , acSite "
                       + " WHERE ste_ent_id = ? "
                       + " AND ste_default_sys_ent_id = usr_ent_id ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, site_id);
            ResultSet rs = stmt.executeQuery();
            String email = null;
            if( rs.next() )
                email = rs.getString("usr_email_2");
            else
                throw new cwException(" Failed to get site system user email , site id = " + site_id);
            
            stmt.close();
            return email;
            
        }

    
    public static String getSysUsrId(Connection con, long site_id)
        throws SQLException, cwException {
            
            String SQL = " SELECT usr_id "
                       + " FROM acSite, RegUSer "
                       + " WHERE ste_default_sys_ent_id = usr_ent_id "
                       + " AND ste_ent_id = ? " ;
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, site_id);
            ResultSet rs = stmt.executeQuery();
            String usr_id = null;
            if(rs.next())
                usr_id = rs.getString("usr_id");
            else
                throw new SQLException("Failed to get sys user id");
            rs.close();
            stmt.close();
            return usr_id;

        }
    
    
    /**
     * Gets the maximum number of items allowed in each organization
     *
     * @author      kawai
     * @version     2002.03.26
     * @param       inCon    the Connection object used by this method
     * @param       inSiteID the entity ID of the organization to be queried
     * @return      the value for the specified organization
     * @exception   SQLException    if an error occured during sql operation
     * @exception   cwException     if a never expected error occured
     * @exception   cwSysMessage    if a value validation violation occured
     */
    private static final String sql_get_site_max_items =
        "SELECT ste_ctl_4 FROM acSite WHERE ste_ent_id = ? ";
    private static final String MAXITEMPREFIX = "ITEM";
    private static final String MAXITEMSUBFIX = "ITEM";
    public static int getMaxItemsAllowed(Connection inCon, long inSiteID)
        throws SQLException, cwException, cwSysMessage
    {
        // get the encoded value from database
        int col = 1;
        PreparedStatement stmt = inCon.prepareStatement(sql_get_site_max_items);
        stmt.setLong(col++, inSiteID);
        ResultSet rs = stmt.executeQuery();
        String encoded_val = null;
        if (rs.next()) {
            col = 1;
            encoded_val = rs.getString(col++);
        } else {
            throw new cwException("Failed to get the maximum number of items allow.(Site ID:" + inSiteID + ")");
        }
        stmt.close();
        
        // validate the value returned from database
        if (!cwEncode.checkValidKey(encoded_val)) {
            throw new cwSysMessage(MSG_LIC_NOT_VALID);
        }
        String decoded_val = cwEncode.decodeKey(encoded_val);
        if (!decoded_val.startsWith(MAXITEMPREFIX) || !decoded_val.endsWith(MAXITEMSUBFIX)) {
            throw new cwSysMessage(MSG_LIC_NOT_VALID);
        }
        
        // extract the embedded value between PREFIX and SUBFIX
        int max_num = 0;
        try {
            max_num = Integer.parseInt(decoded_val.substring(MAXITEMPREFIX.length(), (decoded_val.length() - MAXITEMSUBFIX.length())));
        }catch (Exception e) {
            throw new cwSysMessage(MSG_LIC_NOT_VALID);
        }
        
        return max_num;
    }
    
    
    /**
    * Get the boolean value of the ste_target_by_peer.<br>
    * eg. Item targeted by gradeA, if value is true, then item is also targeted by gradeA's peer
    * otherwise only targeted by gradeA    
    */
    public static boolean isTargetByPeer(Connection con, long siteId)
        throws SQLException{
            
            String SQL = " SELECT ste_target_by_peer_ind FROM acSite Where ste_ent_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, siteId);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if( rs.next() ) {
                flag = rs.getBoolean("ste_target_by_peer_ind");
            }
            rs.close();
            stmt.close();
            return flag;
        }
     
    
    public static String getSteApprStaffRole(Connection con, long siteId)
        throws SQLException{
            
            String SQL = " SELECT ste_appr_staff_role FROM acSite Where ste_ent_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, siteId);
            ResultSet rs = stmt.executeQuery();
            String result = "";
            if( rs.next() ) {
                result = rs.getString("ste_appr_staff_role");
            }
            stmt.close();
            return result;
        }

    public static String[] getQRTypes(Connection con, long siteId)
        throws SQLException{
            
            final String SQL = " SELECT ste_qr_mod_types FROM acSite Where ste_ent_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, siteId);
            ResultSet rs = stmt.executeQuery();
            String result = "";
            if( rs.next() ) {
                result = rs.getString("ste_qr_mod_types");
            }
            stmt.close();
            String[] qrTypes = cwUtils.splitToString(result, "~");
            return qrTypes;
        }
        
    public static String getQRTypesAsXML(Connection con, long siteId)
        throws SQLException{
            
            String[] qrTypes = getQRTypes(con, siteId);
            StringBuffer xml= new StringBuffer(1024);
            xml.append("<qr_mod_type_list>");
            if (qrTypes != null && qrTypes.length > 0) {
                for (int i=0;i<qrTypes.length;i++) {
                    xml.append("<module type=\"").append(qrTypes[i]).append("\"/>");
                }
            }
            xml.append("</qr_mod_type_list>");
            return xml.toString();
        }        

    private static final String SQL_GET_SITE_LIST 
        = " select ste_ent_id, ste_name, ste_join_datetime, ste_status "
        + ", ste_domain, ste_ird_id, ste_trusted, ste_max_users "
        + ", ste_eff_start_date, ste_eff_end_date, ste_max_login_trial "
        + ", ste_cov_syn_datetime, ste_default_sys_ent_id, ste_targeted_entity_lst "
        + ", ste_rsv_min_gap, ste_rsv_min_len, ste_rsv_link, ste_lan_xml "
        + ", ste_guest_ent_id, ste_ctl_4,ste_target_by_peer_ind, ste_ldap_host "
        + ", ste_ldap_dn, ste_appr_staff_role, ste_usr_pwd_valid_period "
        + ", ste_qr_mod_types, ste_id, ste_login_url "
        + " from acSite "
        + " where ste_status = ? "
        + " order by ste_name asc ";

    public static Vector getActiveSites(Connection con) throws SQLException {
        Vector v = new Vector();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_SITE_LIST);
            stmt.setString(1, STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                acSite site = new acSite();
                site.ste_ent_id = rs.getLong("ste_ent_id");
                site.ste_id = rs.getString("ste_id");
                site.ste_name = rs.getString("ste_name");
                site.ste_join_datetime = rs.getTimestamp("ste_join_datetime");
                site.ste_status = rs.getString("ste_status");
                site.ste_domain = rs.getString("ste_domain");
                site.ste_login_url = rs.getString("ste_login_url");
                site.ste_ird_id = rs.getLong("ste_ird_id");
                site.ste_trusted = rs.getBoolean("ste_trusted"); 
                site.ste_max_users = rs.getString("ste_max_users");
                site.ste_eff_start_date = rs.getString("ste_eff_start_date");
                site.ste_eff_end_date = rs.getString("ste_eff_end_date");
                site.ste_lan_xml = rs.getString("ste_lan_xml");
                site.ste_default_sys_ent_id = rs.getLong("ste_default_sys_ent_id");
                site.ste_ldap_host = rs.getString("ste_ldap_host");
                site.ste_ldap_dn = rs.getString("ste_ldap_dn");
                site.ste_usr_pwd_valid_period = rs.getLong("ste_usr_pwd_valid_period");
                v.addElement(site);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }
    
    public static String getSteName(Connection con, long ste_ent_id) throws SQLException{
        String ste_name = null;
        String SQL = "SELECT ste_name "
                + " FROM acSite "
                + " WHERE ste_ent_id = ? ";
            
        PreparedStatement stmt = con.prepareStatement(SQL);                      
        stmt.setLong(1,ste_ent_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            ste_name = rs.getString("ste_name");
        }
        stmt.close();
        return ste_name;
    }
    
    public static List getAllSites(Connection con){
    	List list = new ArrayList();
    	String SQL = "select ste_id from acSite where ste_status = ? order by ste_ent_id";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
		 pst = con.prepareStatement(SQL); 
		 pst.setString(1, STATUS_OK);
		 rs = pst.executeQuery();
		 while(rs.next()){
		 	list.add(rs.getString(1));
		 }
		}catch(Exception e){
			throw new RuntimeException("System error: "+e.getMessage());
		}finally{
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return list;
    }
    public static String getSteType(Connection con){
    	String ste_type = "";
    	String SQL = "select ste_type from acSite where ste_status = ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pst.setString(1, STATUS_OK);
			rs = pst.executeQuery();
			if(rs.next()) {
				//if acSite have more than 1 organization, ste_type must be STE_TYPE_ENTERPRISE
				if(rs.isLast()){
					ste_type = cwEncode.decodeKey(rs.getString("ste_type"));
				} else {
					ste_type = STE_TYPE_ENTERPRISE;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("System error: " + e.getMessage());
		} finally {
			cwSQL.closeResultSet(rs);
			cwSQL.closePreparedStatement(pst);
		}
		return ste_type;
    }
    
    public static void initSteType(Connection con){
    	Dispatcher.siteType = getSteType(con);
    	String SQL = "select bereavedFunction.*, ftn_ext_id "
					+" from bereavedFunction"
					+" inner join acFunction on ftn_id = brf_ftn_id"
					+" where brf_type = ? "
					+" order by brf_type,brf_rol_ext_id,brf_ftn_id";
    	PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pst.setString(1, Dispatcher.siteType);
			ResultSet rs = pst.executeQuery();
			Hashtable typeLst = new Hashtable();
			String rol_ext_id_temp = "";
			Vector ftn_ext_id_lst = new Vector();
			while(rs.next()) {
				String brf_rol_ext_id = rs.getString("brf_rol_ext_id");
				String ftn_ext_id = rs.getString("ftn_ext_id");
				
				if(!brf_rol_ext_id.equalsIgnoreCase(rol_ext_id_temp)){
					if(rs.isFirst()){
						rol_ext_id_temp = brf_rol_ext_id;
					} else {
						typeLst.put(rol_ext_id_temp, ftn_ext_id_lst.clone());
						
						rol_ext_id_temp = brf_rol_ext_id;
						ftn_ext_id_lst = new Vector();
					}
				}
				ftn_ext_id_lst.add(ftn_ext_id);
				if(rs.isLast()){
					typeLst.put(rol_ext_id_temp, ftn_ext_id_lst.clone());
				}
			}
			Dispatcher.bereavedFunc = new Hashtable();
			Dispatcher.bereavedFunc.put(Dispatcher.siteType, typeLst);
		} catch (Exception e) {
			throw new RuntimeException("System error: " + e.getMessage());
		} finally {
			cwSQL.closePreparedStatement(pst);
		}
    }
    
    public static String getSteDeveloperId(Connection con, long ste_ent_id) throws SQLException{
        String ste_developer_id = null;
        String SQL = "SELECT ste_developer_id FROM acSite WHERE ste_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);                      
        stmt.setLong(1,ste_ent_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
        	ste_developer_id = rs.getString("ste_developer_id");
        }
        cwSQL.cleanUp(rs, stmt);
        return ste_developer_id;
    }
    
  //redirect failure while list (进入url_failure白名单时调用)
    public static boolean  getOpenRedirect(List openRedirectWhiteList,String url_failure){
    	   boolean oValid = false;
           boolean isRedirect = false;
           if(url_failure!=null && !url_failure.trim().equals("")){
         	if(url_failure.toLowerCase().trim().startsWith(acSite.DOMAIN_HTTP) || url_failure.toLowerCase().trim().startsWith(acSite.DOMAIN_HTTPS)){
            		 int whiteListSize = openRedirectWhiteList.size();
         		 for (int i = 0; i < whiteListSize; i++) {
							 if (url_failure != null && url_failure.toLowerCase().indexOf(openRedirectWhiteList.get(i).toString().trim().toLowerCase()) >= 0) {
	                            	oValid = true;
	                            }
						}
	         		 if(!oValid){
	            			isRedirect = true;
	            		}
					}
               }
    	return isRedirect;
    }
    
   //是否【POST】请求进入
    public static boolean getMethodCallBack(String method){
    	boolean result = false;
    	if(!"POST".equalsIgnoreCase(method)){
    		result = true;
		}
		return result;
    	
    }
}