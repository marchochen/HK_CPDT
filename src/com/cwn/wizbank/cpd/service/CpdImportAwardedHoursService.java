package com.cwn.wizbank.cpd.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.dataMigrate.imp.Imp;
import com.cw.wizbank.dataMigrate.imp.ImpCPDRegistration;
import com.cw.wizbank.dataMigrate.imp.ImpUser;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.view.ViewIMSLog;
import com.cw.wizbank.db.view.ViewIMSLog.ViewUsrIMSLog;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.enterprise.IMSUtils;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.upload.UploadEnrollment.EnrollmentRecord;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeAttendance;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.Course;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ImsLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.TrackingHistory;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.AeAttendanceMapper;
import com.cwn.wizbank.persistence.AeItemCPDGourpItemMapper;
import com.cwn.wizbank.persistence.AeItemCPDItemMapper;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.persistence.CourseEvaluationMapper;
import com.cwn.wizbank.persistence.CourseMapper;
import com.cwn.wizbank.persistence.CpdGroupMapper;
import com.cwn.wizbank.persistence.CpdGroupRegistrationMapper;
import com.cwn.wizbank.persistence.CpdLrnAwardRecordMapper;
import com.cwn.wizbank.persistence.CpdRegistrationMapper;
import com.cwn.wizbank.persistence.CpdTypeMapper;
import com.cwn.wizbank.persistence.ImsLogMapper;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.persistence.TrackingHistoryMapper;
import com.cwn.wizbank.services.BaseService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.Page;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@Service
public class CpdImportAwardedHoursService extends BaseService<CpdLrnAwardRecord> {
	
	@Autowired
	CpdLrnAwardRecordMapper cpdLrnAwardRecordMapper;
	@Autowired
	CpdGroupService cpdGroupService;
	@Autowired
	AeItemCPDItemService aeItemCPDItemService;
    @Autowired
    RegUserMapper regUserMapper;
    @Autowired
    AeItemMapper aeItemMapper;
    @Autowired
    CpdTypeMapper cpdTypeMapper;
    @Autowired
    CpdGroupMapper cpdGroupMapper;
    @Autowired
    AeApplicationMapper aeApplicationMapper;
    @Autowired
    CpdRegistrationMapper cpdRegistrationMapper;
    @Autowired
    CpdGroupRegistrationMapper cpdGroupRegistrationMapper;
    @Autowired
    AeItemCPDGourpItemMapper aeItemCPDGourpItemMapper;
    @Autowired
    AeItemCPDItemMapper aeItemCPDItemMapper;
    @Autowired
    AeAttendanceMapper aeAttendanceMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TrackingHistoryMapper trackingHistoryMapper;
    @Autowired
    CourseEvaluationMapper courseEvaluationMapper;
    @Autowired
    ImsLogMapper imsLogMapper;
    @Autowired
    CpdUtilService cpdUtilService;
    
    private String[] REQUIRED_TARGET_COLUMN = {
            "User ID*",
            "Course Code/Class Code*",
            "License type*",
            "CPT/D Group Code*",
            "CPT/D Hours Award Date*",
            "Core Hours Awarded*",
            "Non-core Hours Awarded",
        };
    
    
    
    /***
     * 保存文件
     *
     * @param file
     * @return
     */
    public Map<String, Object> saveFile(MultipartFile file,WizbiniLoader wizbini,qdbEnv static_env,loginProfile prof) {
        // 判断文件是否为空
        Map map = new HashMap<String, Object>();
        String msg = "";
        //if (!file.isEmpty()) {
            String fileName = file.getName();
            fileName = file.getOriginalFilename();
            
            java.util.Date ts = new java.util.Date();
            SimpleDateFormat fm = new SimpleDateFormat("SSSHHmmss");
            String tmpUploadPath = static_env.INI_MSG_DIR_UPLOAD_TMP+ cwUtils.SLASH + fm.format(ts)+"\\cpd_awarded_hours_upload";
            File srcFile = new File (tmpUploadPath, fileName);
            if (!srcFile.getParentFile().exists())
                srcFile.getParentFile().mkdirs();
            Imp imp=new ImpCPDRegistration(null,wizbini,tmpUploadPath+cwUtils.SLASH,srcFile.getAbsolutePath());
            
             
            try {
                // 转存文件
                file.transferTo(srcFile);
                Vector<String[]> vector = null;
                if (!file.isEmpty()) {
                    vector = parseFile(srcFile.toString());
                }

                if(vector!=null){
                    //获取上传限制用户最大数
                    int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
                    //如果大于上传用户最大数
                    if (vector.size() > maxUploadCount){
                        throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(maxUploadCount).toString());
                    }
                }
               
               
                map = parseData(vector,fileName);
                map.put("filePath", srcFile.toString());
            } catch (cwSysMessage e) {
                String encoding;
                if(prof == null){
                    encoding = static_env.ENCODING;
                }else{
                    encoding = prof.label_lan;
                }
                msg = ((cwSysMessage) e).getSystemMessage(encoding);

                if(srcFile.exists()){
                    srcFile.delete();
                }
                //e.printStackTrace();
            } catch(Exception ex){
                if(srcFile.exists()){
                    srcFile.delete();
                }
                ex.printStackTrace();
            }
       // }
         map.put("msg", msg);
        return map;
    }
    
    
    /**
     * 导入用户获得CPT/D时数日志数据
     * @param page
     * @return
     */
    public Page<com.cwn.wizbank.entity.ImsLog> searchAll(Page<ImsLog> page,loginProfile prof,qdbEnv static_env) {
        page.getParams().put("ilg_type", "CPDAWARDHOURS");
        page.getParams().put("ilg_process", "IMPORT");
        page.getParams().put("current_role", prof.current_role);
        page.getParams().put("root_ent_id", prof.root_ent_id);
        page.getParams().put("my_top_tc_id", prof.my_top_tc_id);
        imsLogMapper.searchAll(page);
        List<ImsLog> list =  page.getResults();
        IMSLog log = new IMSLog();//日志工具类
        for (int i = 0; i < list.size(); i++) {
            ImsLog  imsLog = list.get(i);
            try {
                imsLog.file_uri = log.getUploadedFileURI(imsLog.ilg_id,imsLog.ilg_filename, static_env);

                Hashtable h_file_uri = log.getLogFilesURI(imsLog.ilg_id, static_env);
                for(int k=0; k<3; k++){
                    if(log.LOG_LIST[k].equals("SUCCESS")){
                        imsLog.success_file_uri = (String)h_file_uri.get(log.FILENAME_LIST[k]);
                    }else if(log.LOG_LIST[k].equals("UNSUCCESS")){
                        imsLog.unsuccess_file_uri = (String)h_file_uri.get(log.FILENAME_LIST[k]);
                    }else if(log.LOG_LIST[k].equals("ERROR")){
                        imsLog.error_file_uri = (String)h_file_uri.get(log.FILENAME_LIST[k]);
                    }
                }
                
            } catch (cwException e) {
                e.printStackTrace();
            }
        }
        return page;
    }
    
    
    
    /*
     * 解析数据
     */
    public Vector<String[]> parseFile(String sourceFile) throws cwException, cwSysMessage, SQLException {
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Vector<String[]> vtEnrollment = new Vector<String[]>();
            File inputWorkbook = new File(sourceFile);
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(0);
            if (sheet != null) {
                int allCount = 0;
                if(sheet.getRows()>0){
                    allCount = sheet.getRows() - 1;
                }else{
                    w.close();       
                    return null;
                }
                if(allCount==0){
                    w.close();       
                    return null;
                }
                
                Cell cell[] = sheet.getRow(0);
                if(cell.length!=7){
                    w.close();       
                    return null;
                }
                if(!cell[0].getContents().trim().equals(REQUIRED_TARGET_COLUMN[0])){
                    w.close();       
                    return null;
                }
                if(!cell[1].getContents().trim().equals(REQUIRED_TARGET_COLUMN[1])){
                    w.close();       
                    return null;
                }
                if(!cell[2].getContents().trim().equals(REQUIRED_TARGET_COLUMN[2])){
                    w.close();       
                    return null;
                }
                if(!cell[3].getContents().trim().equals(REQUIRED_TARGET_COLUMN[3])){
                    w.close();       
                    return null;
                }
                if(!cell[4].getContents().trim().equals(REQUIRED_TARGET_COLUMN[4])){
                    w.close();       
                    return null;
                }
                if(!cell[5].getContents().trim().equals(REQUIRED_TARGET_COLUMN[5])){
                    w.close();       
                    return null;
                }
                if(!cell[6].getContents().trim().equals(REQUIRED_TARGET_COLUMN[6])){
                    w.close();       
                    return null;
                }
                

                String col_value = null;
                EnrollmentRecord enroll = null;
                int incount = 0, okcount = 0;
                // The first line is header row
                int linenum = 1;

                Timestamp curTime = new Timestamp(System.currentTimeMillis());
                Calendar cal = Calendar.getInstance();
                cal.setTime(curTime);
                int row = sheet.getRows();

                // while ((temp = in.read()) != -1) {
                for (int i = 1; i < row; i++) {
                    linenum++;
                    cell = sheet.getRow(i);
                    //过滤空行
                    String[] tem_row_array = new String[cell.length];
                    for (int colidx = 0; colidx < cell.length; colidx++) {
                        String temp_value = cell[colidx].getContents();
                        if(temp_value != null){
                        	System.out.println(temp_value);
                            if (cell[colidx].getType() == CellType.DATE) {
                              DateCell dc = (DateCell) cell[colidx]; 
                              TimeZone gmtZone = TimeZone.getTimeZone("GMT");
                              format.setTimeZone(gmtZone);
                              col_value = format.format(dc.getDate());
                              tem_row_array[colidx] = col_value;
                            }else{
                                tem_row_array[colidx] = temp_value;
                            }
                        }else{
                            tem_row_array[colidx] = "";
                        }
                    }
                    if(UploadUtils.isRowEmpty(tem_row_array)){
                        continue;
                    }
                    vtEnrollment.add(tem_row_array);
            } 
            }else{
                w.close();       
                return null;
            }
            w.close();                        
            return vtEnrollment;
        } catch (BiffException e) {
            throw new cwSysMessage("GEN009");
            // throw new cwException("file error:" + e.getMessage() + " not
            // found.");
        } catch (IOException e) {
            throw new cwException("read file error:" + e.getMessage());
        }
    }
    
    /**
     * 验证数据
     * @param vector
     */
    public Map<String, Object> parseData(Vector<String[]> vector,String fileName) {
        List list = new ArrayList();//错误提示信息
        List listSuccess = new ArrayList();//成功提示信息
        Map<String, Object> map = new HashMap<String, Object>();
        Vector<String[]> vector1 = new Vector<String[]>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
        int errorNumber = 0;
        int successNumber = 0;
        int totalNumber = 0;
        boolean flag = true;
        if(vector!=null && vector.size()>0){
        	//检查导入的记录是否存在多条记录指向一个牌照，且牌照设置只可获取一次时数。
        	Map<String,Object> c_map =  new HashMap<String, Object>();
            for (int i = 0; i < vector.size(); i++) {
                String[] tem_row_array =vector.get(i);
                
                //User ID
                RegUser regUser = null;
                if(tem_row_array.length>=1){
                    if(tem_row_array[0]==null || tem_row_array[0].trim()==""){
                        list.add("[Error] Line "+(i+2)+": User ID cannot be null");
                        flag = false;
                    }else if(tem_row_array[0].trim().length() < 3){
                        list.add("[Error] Line "+(i+2)+": User ID should not be less than 3 characters");
                        flag = false;
                    }else if(tem_row_array[0].trim().length() > 20){
                        list.add("[Error] Line "+(i+2)+": User ID should not exceed 20 characters");
                        flag = false;
                    }else{
                        regUser = regUserMapper.getUserBySteId(tem_row_array[0].trim());
                        if(regUser==null){
                            list.add("[Error] Line "+(i+2)+": User "+tem_row_array[0]+" does not exist");
                            flag = false;
                        }
                    }
                }else{
                    list.add("[Error] Line "+(i+2)+": User ID cannot be null");
                    flag = false;
                }
                
                
                // Course code/class code 
                AeItem aeItem = null;
                if(tem_row_array.length>=2){
                    if(tem_row_array[1]==null || tem_row_array[1].trim()==""){
                        list.add("[Error] Line "+(i+2)+": Course code/class code cannot be null");
                        flag = false;
                    }else if(tem_row_array[1].trim().length() > 50){
                        list.add("[Error] Line "+(i+2)+": Course Code/Class Code should not exceed 50 characters");
                        flag = false;
                    }else{
                        aeItem =aeItemMapper.checkAeitemExist(tem_row_array[1].trim());
                        if(aeItem==null){
                            list.add("[Error] Line "+(i+2)+": Course/class {"+tem_row_array[1]+"} does not exist");
                            flag = false;
                        }else if(aeItem.getItm_create_run_ind()==1 && aeItem.getItm_run_ind()==0 && aeItem.getItm_integrated_ind() ==0){//如果是classroom,需用班级的code
                            list.add("[Error] Line "+(i+2)+": Please specify class code instead of course code");
                            flag = false;
                        }else if(!aeItem.getItm_type().equals("CLASSROOM") && !aeItem.getItm_type().equals("SELFSTUDY")){//只能是CLASSROOM和SELFSTUDY类型
                            list.add("[Error] Line "+(i+2)+": Course code/class code {"+tem_row_array[1]+"} belongs to an invalid learning solution type");
                            flag = false;
                        }else{
                            if(regUser!=null && aeItem!=null){
                                AeApplication aeApplication = new AeApplication();
                                aeApplication.setApp_ent_id(regUser.getUsr_ent_id());
                                aeApplication.setApp_itm_id(aeItem.getItm_id());
                                AeApplication aeApplication2 = aeApplicationMapper.getAeApplicationByitmUsrId(aeApplication);
                                if(aeApplication2!=null){//如果已经报名
                                    list.add("[Error] Line "+(i+2)+": The user is already enrolled in the course/class");
                                    flag = false;
                                }
                            }
                            
                        }
                    }
                }else{
                    list.add("[Error] Line "+(i+2)+": Course code/class code cannot be null");
                    flag = false;
                }
                
                
                
                //License type 
                CpdType cpdType2 = null;
                if(tem_row_array.length>=3){
                    if(tem_row_array[2]==null || tem_row_array[2].trim()==""){
                        list.add("[Error] Line "+(i+2)+": License type cannot be null");
                        flag = false;
                    }else if(tem_row_array[2].trim().length() > 20){
                        list.add("[Error] Line "+(i+2)+": License type should not exceed 20 characters");
                        flag = false;
                    }else{
                        CpdType cpdType = new CpdType();
                        cpdType.setCt_license_type(tem_row_array[2].trim());
                        cpdType2 = cpdTypeMapper.getTypeByCode(cpdType);
                        if(cpdType2==null){
                            list.add("[Error] Line "+(i+2)+": License type "+tem_row_array[2]+" does not exist");
                            flag = false;
                        }else{
                            if(regUser!=null && cpdType2!=null){
                                CpdRegistration cpdRegistration = new CpdRegistration();
                                cpdRegistration.setCr_usr_ent_id(regUser.getUsr_ent_id());
                                cpdRegistration.setCr_ct_id(cpdType2.getCt_id());
                                CpdRegistration cpdRegistration1 = cpdRegistrationMapper.isRegisterForImport(cpdRegistration);
                                if(cpdRegistration1==null){//判断是否有注册该大牌
                                    list.add("[Error] Line "+(i+2)+": The user does not register in the license");
                                    flag = false;
                                }
                            }
                            
                        }
                    }
                    if(cpdType2!=null){
                        tem_row_array[2] = cpdType2.getCt_id().toString();
                    }
                }else{
                    list.add("[Error] Line "+(i+2)+": License type cannot be null");
                    flag = false;
                }
                
                
                //CPT/D group code
                CpdGroup cpdGroup2 = null;
                if(tem_row_array.length>=4){
                    if(tem_row_array[3]==null || tem_row_array[3].trim()==""){
                        list.add("[Error] Line "+(i+2)+": CPT/D group code cannot be null");
                        flag = false;
                    }else if(tem_row_array[3].trim().length() > 20){
                        list.add("[Error] Line "+(i+2)+": CPT/D group code should not exceed 20 characters");
                        flag = false;
                    }else{
                        CpdGroup cpdGroup = new CpdGroup();
                        cpdGroup.setCg_code(tem_row_array[3].trim());
                        cpdGroup2 = cpdGroupMapper.getGroupCode(cpdGroup);
                        if(cpdGroup2==null){
                            list.add("[Error] Line "+(i+2)+":  CPT/D group code "+tem_row_array[3]+" does not exist");
                            flag = false;
                        }else{
                            if(cpdType2!=null){
                                if(!cpdType2.getCt_id().equals(cpdGroup2.getCg_ct_id())){
                                    list.add("[Error] Line "+(i+2)+":  CPT/D group code "+tem_row_array[3]+"  does not belong to the specified license type");
                                    flag = false;
                                }else{
                                    if(regUser!=null && cpdGroup2!=null){
                                        CpdGroupRegistration  cpdGroupRegistration = new  CpdGroupRegistration();
                                        cpdGroupRegistration.setCgr_usr_ent_id(regUser.getUsr_ent_id());
                                        cpdGroupRegistration.setCgr_cg_id(cpdGroup2.getCg_id());
                                        CpdGroupRegistration  cpdGroupRegistration1 = cpdGroupRegistrationMapper.isRegisterCpdgroupForImport(cpdGroupRegistration);
                                        if(cpdGroupRegistration1==null){//判断是否有注册该小牌
                                            list.add("[Error] Line "+(i+2)+": The user does not register in the CPT/D group");
                                            flag = false;
                                        }
                                    }
                                   
                                }
                            }
                            
                        }
                    }
                    if(cpdGroup2!=null){
                        tem_row_array[3] = cpdGroup2.getCg_id().toString();
                    }
                }else{
                    list.add("[Error] Line "+(i+2)+": CPT/D group code cannot be null");
                    flag = false;
                }
                
               
                        

                if(tem_row_array.length>=5){
                    if(tem_row_array[4]==null || tem_row_array[4].trim()==""){//CPT/D hours award date
                        list.add("[Error] Line "+(i+2)+": CPT/D hours award date cannot be null");
                        flag = false;
                    }else{//日期格式验证
                        boolean convertSuccess=true;//指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
                          SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                          try {// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                             format.setLenient(false);
                             Date date = new Date();
                             date = format.parse(tem_row_array[4].trim().toString());
                          } catch (Exception e) {
                              //e.printStackTrace();
                              // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
                              convertSuccess=false;
                          } 
                          if(convertSuccess==false){
                              list.add("[Error] Line "+(i+2)+": CPT/D hours award date format is invalid");
                              flag = false;
                          }
                    }
                }else{
                    list.add("[Error] Line "+(i+2)+": CPT/D hours award date cannot be null");
                    flag = false;
                }
                

                List<AeItemCPDGourpItem> aeItemCPDGourpItems = null;
                if(aeItem!=null && cpdGroup2 != null ){
                    Map param = new HashMap<String,Object>();
                    param.put("itm_id", aeItem.getItm_id());
                    param.put("cg_id", cpdGroup2.getCg_id());
                    aeItemCPDGourpItems = aeItemCPDGourpItemMapper.getAeItemCPDGourpItem (param);//判断该课程是否有对该小牌设置时数
                }

                if(tem_row_array.length>=6){
                    if(tem_row_array[5]==null || tem_row_array[5].trim()==""){//Core hours awarded
                        list.add("[Error] Line "+(i+2)+": Core hours awarded cannot be null");
                        flag = false;
                    }else{
                        float corehours = 0;
                        boolean convertSuccess=true;
                        try {
                            corehours = Float.parseFloat(tem_row_array[5].toString());
                        } catch (Exception e) {
                            convertSuccess = false;//转换异常，不是小数
                            list.add("[Error] Line "+(i+2)+": Core hours awarded is not a number");
                            flag = false;
                        }
                        if(convertSuccess){
                            // Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");//判断是否为小数
                            Pattern pattern = Pattern.compile("^[0-9]+.?[0-9]{0,2}$");//判断是否为小数
                            convertSuccess = pattern.matcher(tem_row_array[5].toString()).matches();
                            
                            if(convertSuccess){
                                if(corehours >=1000 || corehours<0){
                                    list.add("[Error] Line "+(i+2)+": Core hours awarded should not exceed 999.99");
                                    flag = false;
                                }
                            }else{
                                list.add("[Error] Line "+(i+2)+": Core hours awarded is not a number");
                                flag = false;
                            }
                        }
                        
                        
                        if((aeItemCPDGourpItems==null || aeItemCPDGourpItems.size()==0) && corehours>=0){//课程没设能获取时数，但报表设了
                            list.add("[Error] Line "+(i+2)+": The course/class itself does not award any core hours but core hours awarded is specified");
                            flag = false;
                        }else if(aeItemCPDGourpItems!=null){
                            if(aeItemCPDGourpItems.get(0).getAcgi_award_core_hours()<0 && corehours>=0){//课程没设能获取时数，但报表设了
                                list.add("[Error] Line "+(i+2)+": The course/class itself does not award any core hours but core hours awarded is specified");
                                flag = false;
                            }else{
                                if(aeItemCPDGourpItems.get(0).getAcgi_award_core_hours()<corehours){//报表设的核心时数大于课程设的能获取核心时数
                                    list.add("[Error] Line "+(i+2)+": Core hours awarded exceeds the maximum amount awarded by the course/class");
                                    flag = false;
                                }
                            }
                        }
                    }
                }else{
                    list.add("[Error] Line "+(i+2)+": Core hours awarded cannot be null");
                    flag = false;
                }
                
                if(tem_row_array.length>=7){
                    if(tem_row_array[6]!=null && tem_row_array[6].trim()!=""){///CPT/D non-core hours award date
                        try {
                            float noncorehours = 0;
                            boolean convertSuccess=true;
                            try {
                                noncorehours = Float.parseFloat(tem_row_array[6].toString());
                            } catch (Exception e) {
                                convertSuccess = false;//转换异常，不是小数
                                list.add("[Error] Line "+(i+2)+": Non-core hours awarded is not a number");
                                flag = false;
                            }
                            if(convertSuccess){
                                Pattern pattern = Pattern.compile("^[0-9]+.?[0-9]{0,2}$");//判断是否数字或为小数
                                convertSuccess = pattern.matcher(tem_row_array[6].toString()).matches();
                                
                                if(convertSuccess){
                                    if(noncorehours >1000 || noncorehours<0){
                                        list.add("[Error] Line "+(i+2)+": Non-core hours awarded should not exceed 999.99");
                                        flag = false;
                                    }
                                }else{
                                    list.add("[Error] Line "+(i+2)+": Non-core hours awarded is not a number");
                                    flag = false;
                                }
                            }
                            
                            
                            if( cpdGroup2.getCg_contain_non_core_ind()==0 && noncorehours>0){//小牌没设置非核心时数，但是报表添加的非核心时数
                                list.add("[Error] Line "+(i+2)+": The license itself does not have non-core hours but non-core hours awarded is specified");
                                flag = false;
                            }else{
                                 if((aeItemCPDGourpItems==null || aeItemCPDGourpItems.size()==0) && noncorehours>0){//课程没设能获取非核心时数，但报表设了
                                    list.add("[Error] Line "+(i+2)+": The course/class itself does not award any non-core hours but non-core hours awarded is specified");
                                    flag = false;
                                }else{
                                    if(aeItemCPDGourpItems.get(0).getAcgi_award_non_core_hours()<=0 && noncorehours>0){//课程没设能获取非核心时数，但报表设了
                                        list.add("[Error] Line "+(i+2)+": The course/class itself does not award any non-core hours but non-core hours awarded is specified");
                                        flag = false;
                                    }else{
                                        if(aeItemCPDGourpItems.get(0).getAcgi_award_non_core_hours()<noncorehours){//报表设的非核心时数大于课程设的能获取非核心时数
                                            list.add("[Error] Line "+(i+2)+": Non-core hours awarded exceeds the maximum amount awarded by the course/class");
                                            flag = false;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                       
                    }
                }
                

                List<AeItemCPDItem>  aeItemCPDItems = null;
                if(aeItem!=null){
                    Map paramType = new HashMap<String,Object>();
                    paramType.put("itm_id", aeItem.getItm_id());
                    aeItemCPDItems=aeItemCPDItemMapper.getAeItemCPDItem(paramType);
                }
                Date date;
                try {
                    if(tem_row_array.length>=5){
                        if(tem_row_array[4]!=null || tem_row_array[4].trim()!=""){
                            date = sdf.parse(tem_row_array[4].toString());
                            if(aeItem!=null){
                                if(aeItem.getItm_create_run_ind() == 0 && aeItem.getItm_run_ind() ==0 && aeItem.getItm_integrated_ind()==0){
                                    if(aeItemCPDItems!=null && aeItemCPDItems.size()>0){//如果是网上课程：获取时数时间不能大于设置的时数获取限制日期
                                        if(aeItemCPDItems.get(0).getAci_hours_end_date()!=null){
                                            if(aeItemCPDItems.get(0).getAci_hours_end_date().getTime()<date.getTime()){
                                                list.add("[Error] Line "+(i+2)+": CPT/D hours award date should not be later than CPT/D hours end date");
                                                flag = false;
                                            }
                                        }
                                    }
                                }else if(aeItem.getItm_run_ind()==1){//如果是班级：获取时数时间不能大于班级结束时间
                                    if(aeItem.getItm_eff_end_datetime().getTime()<date.getTime()){
                                        list.add("[Error] Line "+(i+2)+": CPT/D hours award date should not be later than class end date");
                                        flag = false;
                                    }
                                }
                            }
                        }
                    }
                    
                } catch (ParseException e) {
                    //e.printStackTrace();
                }  
                
                //验证牌照设置获取时数规则   clar_usr_ent_id  clar_itm_id clar_cg_id
                //牌照设置的获取规则
                //CpdType.AWARD_HOURS_TYPE_FINAL 每门课程完成就可以获得   
                if(flag && null != cpdType2.getCt_award_hours_type() && cpdType2.getCt_award_hours_type() != CpdType.AWARD_HOURS_TYPE_FINAL){
                	//获取每人在该课程已获取的时数记录
                	Map<String,Object> param = new HashMap<String,Object>();
                	param.put("usr_id", regUser.getUsr_id());
                	param.put("clar_usr_ent_id", regUser.getUsr_ent_id());
    				param.put("clar_itm_id", aeItem.getItm_id());
    				param.put("clar_cg_id", cpdGroup2.getCg_id());
                	List<CpdLrnAwardRecord> records = cpdLrnAwardRecordMapper.getCpdLrnAwardRecord(param);
                	
                	//检测要导入的数据中是否存在设置不可重复获得时数的多条记录
                	boolean is_flg = false;
                	String key = null == aeItem.getItm_parent_id() ? cpdType2.getCt_license_type()+aeItem.getItm_id()+cpdGroup2.getCg_id()+regUser.getUsr_id() : cpdType2.getCt_license_type()+aeItem.getItm_parent_id()+cpdGroup2.getCg_id()+regUser.getUsr_id() ;
                	if(null != c_map.get(key)){
                		is_flg  = true;
                	}
                	
                	//每门课程在该评估周期内可获取一次时数
                	if(cpdType2.getCt_award_hours_type() == CpdType.AWARD_HOURS_TYPE_PERIOD_ONCE){
                		//获取在该评估周期内的每人在该课程已获取的时数记录
        				try {
							records = cpdUtilService.getPeriodOnceList(DateUtil.getInstance().getDate(),cpdType2.getCt_id(),records,null);
							if((null != records && records.size() > 0) || is_flg){
	                			 list.add("[Error] Line "+(i+2)+": License type "+cpdType2.getCt_license_type()+" – award hour(s) is issued only once from the same course within the same assessment period, record skipped");
	                             flag = false;
	                		}
						} catch (SQLException e) {
							e.printStackTrace();
						}
                	}else{
                		//每门课只可获取一次时数
                		if((null != records && records.size() > 0) || is_flg){
                			 list.add("[Error] Line "+(i+2)+": License type "+cpdType2.getCt_license_type()+" – award hour(s) is issued only once from the same course, record skipped");
                             flag = false;
                		}
                	}
                	
                	//检验要导入的数据  是否存在多条记录对应的牌照设置 只可获取一次时数的记录。
                	if(flag){
                		c_map.put(key, cpdType2.getCt_license_type());
                	}
                	
                }
                

                if(flag){
                    listSuccess.add( "Line "+(i+2)+": Import success");
                    vector1.add(tem_row_array);
                    successNumber++;
                }else{
                    flag = true;
                    errorNumber++;
                }
                totalNumber++;
            }
        }else{
            map.put("errorFile", "File error: "+fileName+"   module error");
        }
        map.put("list", list);
        map.put("listSuccess", listSuccess);
        map.put("vector", vector1);
        map.put("successNumber", successNumber);
        map.put("errorNumber", errorNumber);
        map.put("totalNumber", totalNumber);
        return map;
    }
    
    
    /**
     * 确认导入
     * @param static_env
     */
    public Map<String, Object> comfirmUpload(String uploadFile,String upload_desc, loginProfile prof,qdbEnv static_env,WizbiniLoader wizbini){
        Connection con = null;
        cwSQL sqlCon = new cwSQL();
        sqlCon.setParam(wizbini);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            con = sqlCon.openDB(false);
            String dir =static_env.DOC_ROOT+cwUtils.SLASH+static_env.LOG_FOLDER+cwUtils.SLASH+IMSLog.CPD_FOLDER_IMSLOG+cwUtils.SLASH;
                try{
                    String file_path = dir + uploadFile;
                    File srcFile=new File(file_path);
                    //向数据库记录此次导入文件，日志信息
                    DbIMSLog dbIlg=new DbIMSLog();
                    dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_CPDAWARDHOURS;
                    dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
                    dbIlg.ilg_create_usr_id = prof.usr_id;
                    dbIlg.ilg_create_timestamp = cwSQL.getTime(con);
                    dbIlg.ilg_method=IMSLog.ACTION_TRIGGERED_BY_UI;
                    dbIlg.ilg_desc=upload_desc;
                    dbIlg.ilg_filename=srcFile.getName();
                    dbIlg.ilg_tcr_id=prof.my_top_tc_id;
                    long logid=DbIMSLog.ins(con, dbIlg);
                    con.commit();
                    File logFile=new File(dir+logid);
                    //如果目录存在就创建
                    if(!logFile.exists()){
                        logFile.mkdir();
                    }
                    //开始导入用户获得CPT/D时数
                    long startTime = System.currentTimeMillis();
                    dbRegUser reguser = new dbRegUser();
                    reguser.ent_id = prof.usr_ent_id;
                    reguser.usr_ent_id = prof.usr_ent_id;
                    reguser.get(con);
                    Imp imp = new ImpUser(con, wizbini, logFile.getAbsolutePath()+cwUtils.SLASH, file_path,false,false,false);
                    Imp.ImportStatus importStatus= imp.doImpCpdHours(prof, false,file_path,this,dbIlg.ilg_desc,static_env);
                    long endTime = System.currentTimeMillis();

                    map.put("success_total", String.valueOf(importStatus.cnt_success));
                    map.put("unsuccess_total", String.valueOf(importStatus.cnt_error));
                    IMSLog imsLog = new IMSLog();
                    imsLog.CPD = true;
                    Hashtable h_file_uri = imsLog.getLogFilesURI(dbIlg.ilg_id, static_env);
                    map.put("success_href", (String)h_file_uri.get("success.txt"));
                    
                    
                    
                    /*取消线程，不用邮件
                     * 
                     * 
                    //导入用户之后给导入人发送邮件
                    String success_total =String.valueOf(importStatus.cnt_success);//插入成功的条数
                    String unsuccess_total =String.valueOf(importStatus.cnt_error);//插入失败的条数
                    
                    if(MessageTemplate.isActive(con, prof.my_top_tc_id, "CPD_AWARDED_HOURS_IMPORT_SUCCESS")){
                      //插入邮件及邮件内容
                        MessageService msgService = new MessageService();
                        Timestamp sendTime = cwSQL.getTime(con);
                        MessageTemplate mtp = new MessageTemplate();
                        mtp.setMtp_tcr_id(prof.my_top_tc_id);
                        mtp.setMtp_type("CPD_AWARDED_HOURS_IMPORT_SUCCESS");
                        mtp.getByTcr(con);
                        String[] contents = msgService.getImportMsgContent(con, mtp, prof.usr_ent_id, srcFile.getName(), startTime, endTime, success_total, unsuccess_total);
                        msgService.insMessage(con, mtp, "s1u3",  new long[] {prof.usr_ent_id}, new long[0], sendTime, contents,0);
                       
                    }*/

                    con.commit();
                    
                }catch(Exception ex){
                    //logger.debug("ImportUserFromFile.process() error", ex);
                }
            
        } catch (Exception e) {
            //e.printStackTrace();
            //logger.debug("ImportUserFromFile.process() error", e);
        }finally {
            if(con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
        }
        return map;
    }
    
    
    
    
    /*
     * 保存数据到数据库（导入用户获得CPT/D时数）
     */
    public void saveEnroll(Vector<String[]> vector,loginProfile prof,WizbiniLoader wizbini,String filePath,String upload_desc,qdbEnv static_env){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
        for (int i = 0; i < vector.size(); i++) {
            String[] tem_row_array =vector.get(i);
            RegUser regUser = regUserMapper.getUserBySteId(tem_row_array[0]);
            AeItem aeItem =aeItemMapper.checkAeitemExist(tem_row_array[1]);
            if(aeItem!=null){
                Map param = new HashMap<String,Object>();
                param.put("itm_id", aeItem.getItm_id());
                param.put("cg_id", Long.parseLong(tem_row_array[3]));
                List<AeItemCPDGourpItem> aeItemCPDGourpItems = aeItemCPDGourpItemMapper.getAeItemCPDGourpItem (param);
                
                Date date1 = null;
                try {
                    date1 = sdf.parse(tem_row_array[4].toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }  
                
                AeApplication app1 = new AeApplication();
                app1.setApp_ent_id(regUser.getUsr_ent_id());
                app1.setApp_itm_id(aeItem.getItm_id());
                List<AeApplication> app = aeApplicationMapper.getAeApplicationByitmAttend(app1);

                aeApplication aeApplication = null;
                
                if(app.size()==0){//没有报名且已完成记录
                    //导入报名；
                    aeApplication = insApplication(regUser.getUsr_ent_id(), aeItem.getItm_id(), prof,wizbini);
                    
                    //使该用户自己完成该课程completion records (Completion status = Completed) 
                    try {
                    CourseEvaluation courseEvaluation = new CourseEvaluation();
                    courseEvaluation = courseEvaluationMapper.getCourseEvaluationByThkId(aeApplication.app_tkh_id);
                    courseEvaluation.setCov_status("C");
                    courseEvaluation.setCov_progress(0.00);
                    //courseEvaluation.setCov_commence_datetime(date1);
                    courseEvaluation.setCov_complete_datetime(date1);
                    courseEvaluationMapper.completeCourse(courseEvaluation);
                    
                    AeAttendance aeAttendance = new AeAttendance();
                    aeAttendance = aeAttendanceMapper.get(aeApplication.app_id);
                    aeAttendance.setAtt_ats_id(1);
                    aeAttendance.setAtt_timestamp(date1);
                    aeAttendanceMapper.updateComplete(aeAttendance);
                    
                    } catch (Exception e) {
                        e.printStackTrace();
                        // TODO: handle exception
                    }
                }
                
                
                //导入用户获得CPT/D时数
                CpdLrnAwardRecord cpdLrnAwardRecord = new CpdLrnAwardRecord();
                cpdLrnAwardRecord.setClar_manul_ind(1);
                cpdLrnAwardRecord.setClar_usr_ent_id(regUser.getUsr_ent_id());
                cpdLrnAwardRecord.setClar_itm_id(aeItem.getItm_id());
                cpdLrnAwardRecord.setClar_ct_id(Long.parseLong(tem_row_array[2]));
                cpdLrnAwardRecord.setClar_cg_id(Long.parseLong(tem_row_array[3]));
                cpdLrnAwardRecord.setClar_acgi_id(aeItemCPDGourpItems.get(0).getAcgi_id());
                cpdLrnAwardRecord.setClar_award_core_hours(Float.parseFloat(tem_row_array[5]));
                if(tem_row_array.length>=7){
                	//cpdLrnAwardRecord.setClar_award_non_core_hours(0.0f);
                	if(null != tem_row_array[6] && !tem_row_array[6].trim().equals("")){
                		cpdLrnAwardRecord.setClar_award_non_core_hours(Float.parseFloat(tem_row_array[6]));
                	}else{
                		cpdLrnAwardRecord.setClar_award_non_core_hours(0.0f);
                	}
                }else{
                    cpdLrnAwardRecord.setClar_award_non_core_hours(0.0f);
                }
                cpdLrnAwardRecord.setClar_create_usr_ent_id(prof.usr_ent_id);
                cpdLrnAwardRecord.setClar_create_datetime(getDate());
                cpdLrnAwardRecord.setClar_update_usr_ent_id(prof.usr_ent_id);
                cpdLrnAwardRecord.setClar_update_datetime(getDate());
                if(app.size()>0){
                    cpdLrnAwardRecord.setClar_app_id(app.get(0).getApp_id());//
                }else{
                    cpdLrnAwardRecord.setClar_app_id(aeApplication.app_id);//
                }
                cpdLrnAwardRecord.setClar_award_datetime(date1);
                cpdLrnAwardRecordMapper.insert(cpdLrnAwardRecord);
            }
            
            
        }
        
    }
    
 
    
    public void insFile(WizbiniLoader wizbini,loginProfile prof,String filePath,String upload_desc,qdbEnv static_env){
        try {
            File file = new File(filePath);
            // 剪切文件
            String dir =static_env.DOC_ROOT+cwUtils.SLASH+static_env.LOG_FOLDER+cwUtils.SLASH+IMSLog.CPD_FOLDER_IMSLOG+cwUtils.SLASH;
            File destFile = new File(dir, file.getName());
            if (!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();
            dbUtils.moveFile(file.getPath(), destFile.getPath());
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    
    /*
     * 添加报名记录
     */
    public aeApplication insApplication( long ent_id, long itm_id, loginProfile prof,WizbiniLoader wizbini){
        Connection con = null;
        cwSQL sqlCon = new cwSQL();
        sqlCon.setParam(wizbini);
        
        aeQueueManager aeQueueManager = new aeQueueManager();
        aeItem item = new aeItem();
        aeApplication aeApplication = new aeApplication();
        try {
            con = sqlCon.openDB(false);
            aeApplication = aeQueueManager.insAppNoWorkflow(con, null, ent_id, itm_id, null, null, prof, item);
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aeApplication;
    }
    
    

}
