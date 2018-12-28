package com.cwn.wizbank.cpd.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.vo.CpdHourVO;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SuperviseTargetEntity;
import com.cwn.wizbank.persistence.CpdGroupMapper;
import com.cwn.wizbank.persistence.CpdGroupRegHoursHistoryMapper;
import com.cwn.wizbank.persistence.CpdGroupRegistrationMapper;
import com.cwn.wizbank.persistence.CpdLrnAwardRecordMapper;
import com.cwn.wizbank.persistence.CpdRegistrationMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.services.RegUserService;

@Service
public class CpdOutstandingEmailService  extends BaseService<CpdGroupRegHoursHistory>{
	

    
    @Autowired
    CpdManagementService cpdManagementService;
    
    @Autowired
    CpdOutstandingReportService cpdOutstandingReportService;

    @Autowired
    CpdGroupRegistrationMapper cpdGroupRegistrationMapper;
    
    @Autowired
    CpdLrnAwardRecordMapper cpdLrnAwardRecordMapper;
    
    @Autowired
    RegUserService regUserService;

    @Autowired
    CpdIndividualReportService cpdIndividualReportService;

    @Autowired
    CpdUtilService cpdUtilService;
    

    @Autowired
    CpdGroupRegHoursHistoryMapper cpdGroupRegHoursHistoryMapper;
    

    @Autowired
    CpdGroupMapper cpdGroupMapper;

    @Autowired
    CpdRegistrationMapper cpdRegistrationMapper;
    
    
    
    /**
     * 生成cpd未完成时数邮件（学员）
     * @param prof
     * @param cg_ct_id
     * @param con
     * @throws Exception
     */
    public void addOutStandingEmailLearner(loginProfile prof,long cg_ct_id,Connection con,Timestamp sendTime,WizbiniLoader wizbini,String sender_usr_id) throws Exception {
            //获取该大牌的当前周期
            CpdPeriodVO cpdPeriodVO =  cpdUtilService.getCurrentPeriod(cg_ct_id);
            int period = cpdPeriodVO.getPeriod();
            
            MessageTemplate mtp = new MessageTemplate();
            mtp.setMtp_tcr_id(prof.my_top_tc_id);
            mtp.setMtp_type("CPTD_OUTSTANDING_LEARNER");
            mtp.getByTcr(con);
            MessageService messageService = new MessageService();
            Date currentTime = new Date();
            
            //注册了这个牌照, 且在当前周期未除牌（即除牌时间大于当前周期的结束时间）的学员
            long[] usr_ent_id = getUsrCpdReg(cg_ct_id,cpdPeriodVO.getEndTime());
            Long[] cghiCtIdArray = {cg_ct_id};
            //注册了这个牌照, 且在当前周期未除牌（即除牌时间大于当前周期的结束时间），但在当时还没有满足所要求CPD时数的学员
            List<Long> usrIds = outStandingUsrIds(usr_ent_id, cghiCtIdArray,  period);
            
            CpdType cpdType = cpdManagementService.getById(cg_ct_id);
            
            
            for (int i = 0; i < usrIds.size(); i++) {
                String[] contents = messageService.getCpdOutstandingMsgContent(con, mtp, usrIds.get(i),cpdType.getCt_license_alias());
                long[] rec_ent_id = {usrIds.get(i)};
                long[] cc_ent_id = null;
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String fileName = formatter.format(currentTime)+".pdf";
                long emsg_id = messageService.insMessage(con, mtp, sender_usr_id,rec_ent_id, cc_ent_id, sendTime, contents,fileName,0, null);
                
                //生成附件
                List<RegUser>  userList = new ArrayList<RegUser>();
                userList.add(regUserService.get(usrIds.get(i)));
                String download_pdf = cpdIndividualReportService.expor(userList, cghiCtIdArray, period, prof.cur_lan, wizbini
                        ,CpdIndividualReportService.FORMAT_TYPE_IN_ONE , 1, fileName, emsg_id);
            }
            
    }
    
    
    /**
     * 注册了这个牌照, 且在当前周期未除牌（即除牌时间大于当前周期的结束时间）的学员
     * @param cr_ct_id
     * @param cr_de_reg_datetime
     * @return
     */
    public long[] getUsrCpdReg(long cr_ct_id,Date cr_de_reg_datetime){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cr_ct_id", cr_ct_id);
        map.put("cr_de_reg_datetime", cr_de_reg_datetime);
        List<Long>  cpdRegistrations = cpdRegistrationMapper.getUsrCpdReg(map);
        
        if(cpdRegistrations!=null){
            long[] usrIdArray = new long[cpdRegistrations.size()];
            for (int i = 0; i < cpdRegistrations.size(); i++) {
                usrIdArray[i] = cpdRegistrations.get(i);
            }
            return usrIdArray;
        }else{
            return null;
        }
        
        
        
    }
    
    
    /**
     * 生成cpd未完成时数邮件（我的下属）
     * @param prof
     * @param cg_ct_id
     * @param con
     * @throws Exception
     */
    public void addOutStandingEmailSupervisor(loginProfile prof,long cg_ct_id,Connection con,Timestamp sendTime,WizbiniLoader wizbini,String sender_usr_id) throws Exception {
             //获取该大牌的当前周期
            CpdPeriodVO cpdPeriodVO =  cpdUtilService.getCurrentPeriod(cg_ct_id);
            int period = cpdPeriodVO.getPeriod();
            
            MessageTemplate mtp = new MessageTemplate();
            mtp.setMtp_tcr_id(prof.my_top_tc_id);
            mtp.setMtp_type("CPTD_OUTSTANDING_SUPERVISOR");
            mtp.getByTcr(con);
            MessageService messageService = new MessageService();
            CpdType cpdType = cpdManagementService.getById(cg_ct_id);
            long[] cghiCtIdArray = {cg_ct_id};
            //注册了这个牌照, 且在当前周期未除牌（即除牌时间大于当前周期的结束时间）的学员
            long[] usrEntId = getUsrCpdReg(cg_ct_id,cpdPeriodVO.getEndTime());
            //注册了这个牌照, 且在当前周期未除牌（即除牌时间大于当前周期的结束时间），但在当时还没有满足所要求CPD时数的学员
            Long[] cghiCtIdArrays = {cg_ct_id};
            List<Long> usrIds = outStandingUsrIds(usrEntId, cghiCtIdArrays,  period);
            
            //获取某牌照下未完成时数的学员的各个上司（直属上司）
            //List<SuperviseTargetEntity> supervisorIds = regUserService.getDirectSupervise(usrIds);
            
            //获取某牌照下未完成时数的学员的部门上司
            //String sql =  "select distinct spt_source_usr_ent_id  from   superviseTargetEntity     inner join EntityRelation supervise on supervise.ern_ancestor_ent_id = spt_target_ent_id   and spt_type = 'SUPERVISE'  and supervise.ern_type = 'USR_PARENT_USG' and supervise.ern_child_ent_id in (16, 17, 18, 19, 20, 21, 22, 23,25)";
            List<SuperviseTargetEntity> supervisorIds = regUserService.getGroupSupervise(usrIds);
            
            
            for (int i = 0; i < supervisorIds.size(); i++) {
                String[] contents = messageService.getCpdOutstandingMsgContent(con, mtp, supervisorIds.get(i).getSpt_source_usr_ent_id(), cpdType.getCt_license_alias());
                long[] rec_ent_id = {supervisorIds.get(i).getSpt_source_usr_ent_id()};
                long[] cc_ent_id = null;
                

                String fileName = "CPTD_Outstanding_Hours_Report_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
                long emsg_id= messageService.insMessage(con, mtp, sender_usr_id,rec_ent_id, cc_ent_id, sendTime, contents, fileName,0, null);
                
                //生成报表附件
                long[] usr_ent_id = getUserForAllSupervisor(wizbini, supervisorIds.get(i).getSpt_source_usr_ent_id());//查询上司的所有下属(仅在未完成时数的学员范围)
                //long[] usr_ent_id = getUserForSupervisor( supervisorIds.get(i).getSpt_source_usr_ent_id(),usrIds);//上司的直属下属(仅在未完成时数的学员范围)
                Map<Integer, List> map =  cpdOutstandingReportService.excelData(usr_ent_id, cghiCtIdArray, 0, period,wizbini,prof);
                cpdOutstandingReportService.expor(prof,wizbini,map,cghiCtIdArray,period,fileName,emsg_id);
            }
            
    }
    

    
    
    public List outStandingUsrIds(long[] usr_ent_id, Long[] cghiCtIdArray,int period){
        Map<String, Object> param = new HashMap<String,Object>();
        Map<Integer, List> map = new HashMap<Integer, List>();
        param.put("usr_ent_id",  usr_ent_id);
        param.put("cghiCtIds",  cghiCtIdArray);
        param.put("period", period);
        List<RegUser> userlist = cpdGroupRegHoursHistoryMapper.getUserDetailForExcel(param);
        List<CpdGroup> cpdGroupList = cpdGroupMapper.searchByType(param);
        
        List list = new ArrayList();
        for (int j = 0; j < userlist.size(); j++) {
            RegUser regUser = userlist.get(j);
            boolean isOutStanding = false; //是否存在超时
            for (int i = 0; i <cpdGroupList.size(); i++) {
                Map<String, Object> param1 = new HashMap<String,Object>();
                param1.put("usr_ent_id", regUser.getUsr_ent_id());
                param1.put("period", period);
                param1.put("cg_id", cpdGroupList.get(i).getCg_id());
                CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationMapper.getHoursDate(param1);
                if(cpdGroupRegistration!=null){
                    param1.put("start_time", cpdGroupRegistration.getCpdGroupRegHours().getCgrh_cal_start_date());
                    param1.put("end_time", cpdGroupRegistration.getCpdGroupRegHours().getCgrh_cal_end_date());
                    
                    CpdHourVO cpdHourVO = cpdLrnAwardRecordMapper.sumAwardHoursOutding(param1);
                    //计算核心时数是否足够
                    float coreHousrOut = 0f;
                    float coreHousrOutForNom = 0f;//增加一个核心时数的复制品，给非核心时数进行借时数使用；
                    if(cpdHourVO!=null){
                        coreHousrOut = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours()-cpdHourVO.getTotle_award_core_hours();
                        coreHousrOutForNom = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours()-cpdHourVO.getTotle_award_core_hours();
                    }else{
                        coreHousrOut = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours();
                        coreHousrOutForNom = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_core_hours();
                    }
                    if(coreHousrOut<=0){
                        coreHousrOut = 0.0f;
                    }else{
                        isOutStanding = true;
                    }
                    
                    float nonCoreHousrOut = 0f;
                    if(cpdGroupRegistration.getCpdGroup().getCg_contain_non_core_ind()==1){
                        //计算非核心时数是否足够
                        if(cpdHourVO!=null){
                            if(null!=cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours() && cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours()!=0.0f){
                                nonCoreHousrOut = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours()-cpdHourVO.getTotle_award_non_core_hours();
                            }else{
                                nonCoreHousrOut = 0;
                            }
                           
                        }else{
                            nonCoreHousrOut = cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours()==null? 0.0f:cpdGroupRegistration.getCpdGroupRegHours().getCgrh_execute_non_core_hours();
                        }
                        if(nonCoreHousrOut<=0){
                            nonCoreHousrOut = 0.0f;
                        }else if(nonCoreHousrOut>0 && coreHousrOutForNom<=0){//当完成的非核心时数不足时，并且完成的核心时数有多，从完成的核心时数借
                            nonCoreHousrOut = nonCoreHousrOut+coreHousrOutForNom;
                            if(nonCoreHousrOut<=0){
                                nonCoreHousrOut = 0.0f;
                            }else{
                                isOutStanding = true;
                            }
                        }
                    }
                    
                    
                }
                
            }
            if(isOutStanding == true){
                list.add(regUser.getUsr_ent_id());
            }
            isOutStanding = false; 
        }
        return list;
        
    }
    
    
    
    /**
     * 某用户下的直属下属
     * @return
     */
    public long[] getUserForSupervisor(long usrId,List<Long> usrIds ){
        Map<String,Object> paramUser = new HashMap<String,Object>();
        long[] usr_ent_id = null;
        paramUser.put("tc_independent", false);
        paramUser.put("my_top_tc_id", 1);
        paramUser.put("usr_ent_id", usrId);
        paramUser.put("current_role", "NLRN_1");
        //当前角色是否与培训中心关联
        paramUser.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd("NLRN_1"));
        
        if(usrIds!=null){
            long[] usrIdArray = new long[usrIds.size()];
            for (int i = 0; i < usrIds.size(); i++) {
                usrIdArray[i] = usrIds.get(i);
            }
            paramUser.put("usrIdArray", usrIdArray);
        }
        
        List<RegUser> regUsers  = regUserService.findsuperviseForCpdExcel(paramUser);
        long usertIdArrays[]=new long[regUsers.size()];
        for (int i = 0; i < regUsers.size(); i++) {
            usertIdArrays[i] = regUsers.get(i).getUsr_ent_id();
        }
        usr_ent_id = usertIdArrays;
        return usr_ent_id;
    }   
    
    /**
     * 导出excel的用户（所有下属）
     * @return
     */
    public long[] getUserForAllSupervisor(WizbiniLoader wizbini, long usrEntId){
        //Map<String, Object> param = new HashMap<String,Object>();
        Map<String,Object> paramUser = new HashMap<String,Object>();
        long[] usr_ent_id = null;
        paramUser.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
        paramUser.put("my_top_tc_id", 1);//
        paramUser.put("usr_ent_id", usrEntId);
        paramUser.put("current_role", "NLRN_1");//
        //当前角色是否与培训中心关联
        paramUser.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd("NLRN_1"));
        paramUser.put("supervise", "SUPERVISE");//所有下属
        List<RegUser> regUsers  = regUserService.findUserListForCpdExcel(paramUser);
        long usertIdArrays[]=new long[regUsers.size()];
        for (int i = 0; i < regUsers.size(); i++) {
            usertIdArrays[i] = regUsers.get(i).getUsr_ent_id();
        }
        usr_ent_id = usertIdArrays;
        return usr_ent_id;
    }   
    
    
	
}
