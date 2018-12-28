package com.cwn.wizbank.cpd.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.cpd.service.CpdManagementService;
import com.cwn.wizbank.cpd.service.CpdOutstandingEmailService;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.JsonFormat;

@RequestMapping("cpdOutstandingEmail")
@Controller
public class CpdOutstandingEmailController {
    

    @Autowired
    CpdOutstandingEmailService cpdOutstandingEmailService;
    
    @Autowired
    CpdManagementService cpdManagementService;
 
	
	//生成cpd未完成时数邮件
    @RequestMapping(method = RequestMethod.POST, value = "addEmailContentCpdOutstanding")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String getcpdTypeRegHistory(Model model, loginProfile prof,WizbiniLoader wizbini,
            @RequestParam(value = "cg_ct_id", required = false, defaultValue = "0") long cg_ct_id) throws Exception {
            Connection con = null;
            // get the database connection for this request
            try {
                cwSQL sqlCon = new cwSQL();
                sqlCon.setParam(wizbini);
                con = sqlCon.openDB(false);
            } catch (Exception e) {
                return " Sorry, the server is too busy. ";
            }
            
            dbRegUser sender =  new dbRegUser();
            acSite site = new acSite();
            site.ste_ent_id = 1;
            sender.usr_ent_id = site.getSiteSysEntId(con);
            sender.get(con);
            String sender_usr_id = sender.usr_id;

            Timestamp sendTime = cwSQL.getTime(con);
            cpdOutstandingEmailService.addOutStandingEmailLearner( prof, cg_ct_id, con,sendTime,wizbini,sender_usr_id) ;//生成cpd未完成时数邮件（学员）
            cpdOutstandingEmailService.addOutStandingEmailSupervisor( prof, cg_ct_id, con,sendTime,wizbini,sender_usr_id) ;//生成cpd未完成时数邮件（我的下属）
            cpdManagementService.updLastEmailSendTime(sendTime,cg_ct_id);//修改牌照最后一次发送邮件时间
            
            con.commit();
            if(con!=null){
                con.close();
            }
            
            return JsonFormat.format(model);
    }
    


    
    
    
}
