package com.cw.wizbank.JsonMod.role;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.cache.wizbCacheManager;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class RoleModule extends ServletModule {
    RoleModuleParam modParam;
    
    private static String RFMODULE = "role_function";
    
    private final static String SMSG_INS_SUCC_MSG = "ROL001";

    private final static String SMSG_UPD_MSG = "ROL003";

    private final static String SMSG_DEL_MSG = "ROL004";
    
    public RoleModule() {
        super();
        modParam = new RoleModuleParam();
        param = modParam;
    }
    
    public void process() throws SQLException, IOException, cwException {
        try {
                if(this.prof == null || this.prof.usr_ent_id == 0){
                    throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
                } else {
                    String profile = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
                    if (modParam.getCmd().equalsIgnoreCase("get_role_list")
                            || modParam.getCmd().equalsIgnoreCase("get_role_list_xml")) {
                        RoleManager rf = new RoleManager();
                        String dataXML = rf.getRoleListAsXml(con, modParam, prof);
                        resultXml = formatXML(dataXML, null, RFMODULE);
                    } else if (modParam.getCmd().equalsIgnoreCase("get_role_detail")
                            || modParam.getCmd().equalsIgnoreCase("get_role_detail_xml")) {
                        RoleManager rf = new RoleManager();
                        String dataXML = rf.getRoleByIdAsXml(con, modParam.rol_id) + wizbini.getFunctionMapXml();
                        resultXml = formatXML(dataXML, profile, RFMODULE);
                    } else if (modParam.getCmd().equalsIgnoreCase("add_role")
                            || modParam.getCmd().equalsIgnoreCase("add_role_xml")) {
                        resultXml = formatXML(wizbini.getFunctionMapXml(), profile, RFMODULE);
                    } else if (modParam.getCmd().equalsIgnoreCase("del_muti_role")
                            || modParam.getCmd().equalsIgnoreCase("del_muti_role_xml")) {
                        RoleManager rf = new RoleManager();
                        if (rf.delRole(con, modParam.rol_id_lst)) {
                        	wizbCacheManager.clearCache();
                            cwSysMessage sms = new cwSysMessage(SMSG_DEL_MSG);
                            msgBox(MSG_STATUS, sms, param.getUrl_success(), out);
                        } else {
                            StringBuffer sb = new StringBuffer();
                            Vector vec = rf.del_fail_rol;
                            for (int i = 0; i < vec.size(); i++) {
                                sb.append(vec.get(i) + "        ");
                            }
                            cwSysMessage sms = new cwSysMessage("ROL005", sb.toString());
                            msgBox(MSG_ERROR, sms, param.getUrl_failure(), out);
                        }
                    } else if (modParam.getCmd().equalsIgnoreCase("del_role")
                            || modParam.getCmd().equalsIgnoreCase("del_role_xml")) {
                        long[] rol_id_lst = new long[] { modParam.rol_id };
                        RoleManager rf = new RoleManager();
                        if (rf.delRole(con, rol_id_lst)) {
                        	wizbCacheManager.clearCache();
                            cwSysMessage sms = new cwSysMessage(SMSG_DEL_MSG);
                            msgBox(MSG_STATUS, sms, param.getUrl_success(), out);
                        } else {
                            StringBuffer sb = new StringBuffer();
                            Vector vec = rf.del_fail_rol;
                            for (int i = 0; i < vec.size(); i++) {
                                sb.append(vec.get(i) + "        ");
                            }
                            cwSysMessage sms = new cwSysMessage("ROL005", sb.toString());
                            msgBox(MSG_ERROR, sms, param.getUrl_failure(), out);
                        }
                    } else if (modParam.getCmd().equalsIgnoreCase("add_role_exec")
                            || modParam.getCmd().equalsIgnoreCase("add_role_exec_xml")) {
                        RoleManager rf = new RoleManager();
                        if(rf.isOnlyRoleTitle(con,modParam,prof)){
                            rf.addRole(con, modParam, prof, wizbini);
                            con.commit();
                            wizbCacheManager.clearCache();
                            cwSysMessage sms = new cwSysMessage(SMSG_INS_SUCC_MSG);
                            msgBox(MSG_STATUS, sms, param.getUrl_success(), out);
                        }else{
                            cwSysMessage sms = new cwSysMessage("ROL009",modParam.getRol_title());
                            msgBox(MSG_ERROR, sms, param.getUrl_failure(), out);
                        }
                    } else if (modParam.getCmd().equalsIgnoreCase("modify_role")
                            || modParam.getCmd().equalsIgnoreCase("modify_role_xml")) {
                        RoleManager rf = new RoleManager();
                        if (rf.isSystemRole(con, modParam)) {
                            cwSysMessage sms = new cwSysMessage("ROL006");
                            msgBox(MSG_ERROR, sms, modParam.getUrl_failure(), out);
                        } else {
                                if(rf.isModifyOnlyRoleTitle(con, modParam, prof)){
                                    rf.modifyRole(con, modParam, wizbini, prof);
                                    wizbCacheManager.clearCache();
                                    cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
                                    msgBox(MSG_STATUS, sms, modParam.getUrl_success(), out);
                                }else{
                                    cwSysMessage sms = new cwSysMessage("ROL007",modParam.getRol_title());
                                    msgBox(MSG_ERROR, sms, modParam.getUrl_failure(), out);
                                }
                        }
                        
                    } else {
                        throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
                    }
                }
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage(), e);
        }
    }   

}
