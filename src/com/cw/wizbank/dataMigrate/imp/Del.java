package com.cw.wizbank.dataMigrate.imp;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dataMigrate.imp.bean.ImportObject;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.upload.ImportTemplate;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpd.service.CpdImportAwardedHoursService;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.utils.CharUtils;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Del {

	Logger log;
	Logger logSuccess;
	Logger logFailure;

	public static long DEFAULT_ENT_ID_ROOT = 1;
	public static long DEFAULT_ENT_ID = 3;
	public static long DEFAULT_GROUP_ROOT = 1;
	public static long DEFAULT_GRADE_ROOT = 5;
	public static long DEFAULT_GRADE_ID = 7;

	public static int code_e = 50;// Critical Error
	public static int code_w = 3; // Warning
	public static int code_s = 0;// Integration Success

	public static String msg_code_w = "001"; // Email for Warning
	public static String msg_code_e_002 = "002";// Interface file not found
	public static String msg_code_e_003 = "003";// A certain percentage of
												// records get warning message.
	public static String msg_code_e_004 = "004";// Number of record parsed is
												// not equal to the number of
												// data records in Trailer
	public static String msg_code_e_005 = "005";// Incorrect Header/Trailer
												// format

	public final static String GSM_PREFIX = "G";
	public final static String DSM_PREFIX = "D";
	
	public String[] label_title=null;

	int warning_p = 50;

	long data_long = 0;

	String file_name_date_format = "yyMMddHHssmm";
	String log_date_format = "yyyyMMdd";
	String content_date_format = "yyyyMMdd";

	String datetime_format = "yyyy-MM-dd HH:ss:mm";
	public static String time_format = "yyyy-MM-dd";
	String integration_name;
	String int_file_pre = "";

	String[] fieldLabel;
	int[] allowEmpty;
	int[] colMaxLen;
	int[] isDate;
	int[] isEmail;
	int[] isNumber;
	File logErrFile = null;
	File logFailureFile = null;
	boolean isCPDRestrationImport = false;
	public static Connection con;
	public static WizbiniLoader wizbini;
	public static String log_dir;
	public static String input_file;
	public String log_file_name = "import_";

	public abstract void finalCheck(Vector vec, ImportStatus import_status, boolean clear) throws Exception;

	public abstract void importData(Vector Vec, loginProfile prof, ImportStatus importStatus,boolean clear) throws Exception;

	// 赋值ImportObject
	public abstract ImportObject putField(String[] value, ImportObject record, int colIdx,loginProfile prof);

	public abstract ImportObject getNewRecordBean();
	public void logError(String message){
		logFailure.info(message);
	}
	// 初始化日志文件
	public ImportStatus initLog(loginProfile prof) throws Exception {
		ImportStatus import_status = new ImportStatus();
		try{
		Date beginDate = new Date();
		String logdir = log_dir;
		File dir = new File(logdir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		System.setProperty("lms_integ_log_path", logdir);
		System.setProperty("file_encoding", wizbini.cfgSysSetupadv.getEncoding());
		PropertyConfigurator.configure(wizbini.getCfgFileLog4jDir());

		// 淇濆瓨瀵煎叆鐨勭姸鎬佷俊鎭�
		import_status.logdir = logdir;
		logErrFile = new File(logdir, "error.txt");
		File logSuccessFile = new File(logdir, "success.txt");
		File logFile = new File(logdir, "log.txt");
		logFailureFile = new File(logdir, "failure.txt");
		import_status.start_date = beginDate;
		import_status.log_file = logFile;
		import_status.log_success_file = logSuccessFile;
		import_status.log_error_file = logErrFile;
		import_status.log_failure_file=logFailureFile;
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
			throw new Exception("err_init_del_user_log");
		}
		return import_status;
	}

	/**
	 * 
	 * @param prof
	 * @param clear
	 *            ture表示全量导入 false 表示增量同步
	 * @return
	 * @throws Exception
	 */
	public ImportStatus doImp(loginProfile prof, boolean clear) throws Exception {

		ImportStatus import_status = initLog(prof);
		log(LangLabel.getValue( prof.cur_lan,"start_del_user") + import_status.start_date);
		File curFile = null;
		String curFileName = null;
		String curFilePath = null;
		try {

			curFile = new File(input_file);
			curFileName = curFile.getName();
			curFilePath = curFile.getAbsolutePath();

			log(LangLabel.getValue(prof.cur_lan,"file_path") + curFilePath);
			Vector<ImportObject> vec = parseFile(curFile, import_status,prof);
			if (notEmptyVector(vec)) {
				

				finalCheck(vec, import_status, clear);

				importData(vec, prof, import_status,clear);

			}
			try {
				// 剪切文件
				log(LangLabel.getValue(prof.cur_lan,"copy_file") + curFile.getAbsolutePath() + LangLabel.getValue(prof.cur_lan,"to" ) + import_status.logdir);
				File destFile = new File(import_status.logdir, curFileName);
				dbUtils.moveFile(curFile.getPath(), destFile.getPath());
				if (curFile.exists()) {
					curFile.delete();
				}
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
				throw new Exception(LangLabel.getValue( prof.cur_lan,"copy_file_err") + curFile.getAbsolutePath() + LangLabel.getValue( prof.cur_lan,"to") + import_status.logdir);
			}

		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			e.printStackTrace();
			logFailure(LangLabel.getValue(prof.cur_lan,"import_file_err" ), e);
		}
		Date endDate = new Date();
		log(LangLabel.getValue(prof.cur_lan,"end_import_user" ) + endDate);
		import_status.end_date = endDate;
		con.commit();
		return import_status;
	}

	
	   /**
     * CPD用户导入时数
     * @param prof
     * @return
     * @throws Exception
     */
    public ImportStatus doImpCpdHours(loginProfile prof, boolean clear,String file_path,CpdImportAwardedHoursService cpdImportAwardedHoursService,String upload_desc,qdbEnv static_env) throws Exception {

        ImportStatus import_status = initLog(prof);
        log(LangLabel.getValue( prof.cur_lan,"start_import_user") + import_status.start_date);
        File curFile = null;
        String curFileName = null;
        String curFilePath = null;
        try {
            curFile = new File(file_path);
            curFileName = curFile.getName();
            curFilePath = curFile.getAbsolutePath();

            log(LangLabel.getValue(prof.cur_lan,"file_path") + curFilePath);
            Vector<String[]> vec = cpdImportAwardedHoursService.parseFile(file_path);
            if (notEmptyVector(vec)) {
                //finalCheck(vec, import_status, clear);
                //importData(vec, prof, import_status,clear);
                try {
                    Map map = cpdImportAwardedHoursService.parseData(vec,curFileName);
                    Vector<String[]> vector1 = (Vector<String[]>) map.get("vector");
                    
                    List list = new ArrayList();//错误信息
                    list = (List) map.get("list");
                    for (int i = 0; i < list.size(); i++) {
                        logFailure(list.get(i).toString());
                    }
                    
                    List listSuccess = new ArrayList();//成功信息
                    listSuccess = (List) map.get("listSuccess");
                    for (int i = 0; i < listSuccess.size(); i++) {
                        logSuccess(listSuccess.get(i).toString());
                    }
                    
                    import_status.cnt_success =  vector1.size();
                    import_status.cnt_error = list.size();
                    
                    cpdImportAwardedHoursService.saveEnroll(vector1, prof, wizbini,file_path,upload_desc,static_env);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                

            }else{//错误文件
                logFailure("File error: "+curFileName+"   module error");
            }

        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            logFailure(LangLabel.getValue(prof.cur_lan,"import_file_err" ), e);
        }

        try {
            // 剪切文件
            log(LangLabel.getValue(prof.cur_lan,"copy_file") + curFile.getAbsolutePath() + LangLabel.getValue(prof.cur_lan,"to" ) + import_status.logdir);
            File destFile = new File(import_status.logdir, curFileName);
            dbUtils.moveFile(curFile.getPath(), destFile.getPath());
            if (curFile.exists()) {
                curFile.delete();
            }
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            throw new Exception(LangLabel.getValue( prof.cur_lan,"copy_file_err") + curFile.getAbsolutePath() + LangLabel.getValue( prof.cur_lan,"to") + import_status.logdir);
        }
        
        Date endDate = new Date();
        log(LangLabel.getValue(prof.cur_lan,"end_import_user" ) + endDate);
        import_status.end_date = endDate;
        con.commit();
        return import_status;
    }

    
    
	// 文件排序
	public File[] sortFile(File[] fs) {
		List<File> files = Arrays.asList(fs);
		Collections.sort(files, new Comparator<File>() {
			public int compare(File o1, File o2) {
				if (o1.isDirectory() && o2.isFile())
					return -1;
				if (o1.isFile() && o2.isDirectory())
					return 1;
				return o1.getName().compareTo(o2.getName());
			}
		});
		fs = files.toArray(fs);
		return fs;
	}

	public boolean checkFile(String fileName) {
		fileName = fileName.toUpperCase();
		String extension = ".txt";
		Pattern p = Pattern.compile(int_file_pre.toUpperCase() + "(\\d+)" + extension);
		Matcher m = p.matcher(fileName);
		if (m.find()) {
			String date = m.group(1);
			return isDate(date, file_name_date_format);
		} else {
			return false;
		}
	}

	public Date getFileDate(String fileName) {
		fileName = fileName.toUpperCase();
		String extension = ".txt";
		Date date = null;
		Pattern p = Pattern.compile(int_file_pre.toUpperCase() + "(\\d+)" + extension);
		Matcher m = p.matcher(fileName);
		if (m.find()) {
			String tmp = m.group(1);
			if (isDate(tmp, file_name_date_format)) {
				date = parseDate(tmp, file_name_date_format);
			}
		}
		if (date == null) {
			date = new Date();
		}
		return date;
	}

	// 解析文件，封装文件数据
	public Vector<ImportObject> parseFile(File file, ImportStatus import_status,loginProfile prof){
		
		Vector vec = new Vector();
		try {
			if (file == null) {
				throw new Exception(LangLabel.getValue(prof.cur_lan,"file" )+file.getAbsolutePath()+LangLabel.getValue(prof.cur_lan,"not_find" ));
			}
			log(LangLabel.getValue(prof.cur_lan,"start_parse_file" ) + file.getAbsolutePath());
			if(!checkTemplate(file)){
				throw new Exception(LangLabel.getValue(prof.cur_lan, "err_module_title"));
			}
			Vector recVec = parseSourseFile(file,prof);
			// 设置初始值，跳过第一行
			label_title=(String[])recVec.get(0);
			int sumcol = fieldLabel.length;
			Map labelMap=(Map)ImportTemplate.delProf.get(prof.root_id);
			for(int title=0;title<sumcol;title++){
				if(label_title[title]==null||label_title[title].equals("")){
					throw new Exception(LangLabel.getValue(prof.cur_lan, "err_module_title"));
				}
				String label=label_title[title];
				String modLabel="";
				if(isCPDRestrationImport){
					modLabel=LangLabel.getValue(prof.cur_lan,fieldLabel[title]);					
				}else{
					modLabel=( (Map)labelMap.get(fieldLabel[title])).get(prof.cur_lan).toString();
					if(modLabel.equals("岗位")){
						modLabel="岗位编号";
					}else if(modLabel.equals("崗位")){
						modLabel="崗位編號";
					}	if(modLabel.equals("角色") && prof.cur_lan.equalsIgnoreCase("zh-cn")){
						modLabel="角色代码";
					}else if(modLabel.equals("角色") && prof.cur_lan.equalsIgnoreCase("zh-hk")){
						modLabel="角色代碼";
					}if(modLabel.equals("最高報名審批用戶組")){
						modLabel="最高報名審批用戶組編號";
					}else if(modLabel.equals("最高报名审批用户组")){
						modLabel="最高报名审批用户组编号";
					}	if(modLabel.equals("下属部门")){
						modLabel="下属部门编号";
					}else if(modLabel.equals("管理部門")){
						modLabel="管理部門編號";
					}
				}
				if(label==null||!label.trim().equals(modLabel)){
					throw new Exception(LangLabel.getValue(prof.cur_lan, "err_module_title"));
				}
			}
			boolean has_record = false;
			//获取总记录数
			import_status.setTotal(recVec.size()-1);
			for (int i = 1; i < recVec.size(); i++) {
				String[] row = (String[]) recVec.get(i);
				int lineno=Integer.valueOf(row[row.length-1]);
				if (UploadUtils.isRowEmpty(row)) {
					log(LangLabel.getValue(prof.cur_lan,"line_number" ) + lineno + " "+ LangLabel.getValue(prof.cur_lan,"empty_row")+".");
					logFailure(LangLabel.getValue( prof.cur_lan,"line_number") + lineno+" " + LangLabel.getValue(prof.cur_lan,"empty_row" )+".");
					continue;
				}
				has_record = true;
				ImportObject record = getNewRecordBean();
				record.lineno = lineno;
				if(checkRowIsEmpty(row)){continue;} //判断是否该行的单元格都是只有空格值 .如果只有空格就跳过该条数据
				record.valid = checkRecord(row, lineno, record,prof);

				if (record.valid) {
					//记录通过的条数
					import_status.addPassRecord();
					record = putField(row, record, lineno,prof);
				}else{
					//记录未通过的条数
					import_status.addnuPassRecord();
				}
				vec.add(record);

			}

			if (!has_record) {
				//没有记录的文件
				throw new Exception(LangLabel.getValue(prof.cur_lan,"no_record"));
			}

			log(LangLabel.getValue(prof.cur_lan,"end_parse_file") + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			if(!e.getMessage().equals(LangLabel.getValue(prof.cur_lan,"no_record"))){
				import_status.addnuPassRecord();
			}
			CommonLog.error(e.getMessage(),e);
			logFailure(LangLabel.getValue(prof.cur_lan,"parse_file_err")  +file.getName()+"  "+e.getMessage());
		}
		return vec;
	}

	public void checkWarnPercentage(Vector vec, ImportStatus import_status) throws Exception {
		//

		// 瀵瑰嚭閿欑殑璁板綍鍐檒og
		for (int i = 0; i < vec.size(); i++) {
			ImportObject record = (ImportObject) vec.get(i);
			if (!record.valid) {
				import_status.addWarn();
			}
		}

		if (import_status.cnt_warn > 0 && import_status.total > 0) {
			import_status.setReturnCode(code_w);
			// 濡傛灉鍑洪敊鐨勮褰曡秴杩囪瀹氱殑鐧惧垎姣旓紝鍒欓��鍑哄悓姝�
			if ((Float.valueOf(import_status.cnt_warn).floatValue() / Float.valueOf(import_status.total).floatValue()) * 100 > warning_p) {
				import_status.setReturnCode(code_e);
				import_status.msg_code = msg_code_e_003;
				logFailure("[Critical Error] Number of error records in interface file exceeds the threshold " + warning_p + "%");
				return;
			}
		}

	}

	public String getFieldLabel(int index) {
		if (index >= 0 && index <= fieldLabel.length) {
			return "Field " + index + " " + fieldLabel[index];
		}
		return "";
	}

	/**
	 * 验证数据的合法性
	 * @param row
	 * @param lineno
	 * @param record
	 * @return
	 * @throws cwException 
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	public boolean checkRecord(String[] row, int lineno, ImportObject record,loginProfile prof) throws cwException, SQLException, ParseException {
		Map minLengthMap = (Map) ImportTemplate.minLength.get(prof.getRoot_id());
		boolean valid = true;
		if (row != null) {
			if (row.length-1 != data_long) {
				logFailure(LangLabel.getValue(prof.cur_lan,"line_number" ) + lineno +" "+LangLabel.getValue(prof.cur_lan,"record_length_err" )+ ".");
				valid = false;
			} else {
				for (int i = 0; i < fieldLabel.length; i++) {
					String val = row[i];
					if (!ImportTemplate.allowEmpty.get(fieldLabel[i])) {
						if (empty(val)) {
							logFailure(LangLabel.getValue(prof.cur_lan,"line_number") + lineno + " [" + label_title[i] + "]  " +  LangLabel.getValue(prof.cur_lan,"not_null")+".");
							valid = false;
						}
					}
					if (ImportTemplate.dbMaxLength.get(fieldLabel[i]) > 0) {
						if (notEmpty(val) && CharUtils.getStringLength(val.trim()) > ImportTemplate.dbMaxLength.get(fieldLabel[i])) {
							logFailure(LangLabel.getValue( prof.cur_lan,"line_number") + lineno + " [" + label_title[i] + "]  " +  LangLabel.getValue( prof.cur_lan,"maxlength_out_of_bounds")+ImportTemplate.dbMaxLength.get(fieldLabel[i])+".");
							valid = false;
						}
						
					}
					if (minLengthMap.get(fieldLabel[i])!=null && Integer.parseInt(minLengthMap.get(fieldLabel[i]).toString())> 0) {
						if (notEmpty(val) && CharUtils.getStringLength(val.trim()) < Integer.parseInt(minLengthMap.get(fieldLabel[i]).toString())) {
							logFailure(LangLabel.getValue( prof.cur_lan,"line_number") + lineno + " [" + label_title[i] + "]  " +  LangLabel.getValue( prof.cur_lan,"minlength_out_of_bounds")+Integer.parseInt(minLengthMap.get(fieldLabel[i]).toString())+".");
							valid = false;
						}
						
					}
					//如果是登录名称需要检查是否符合格式
					if(notEmpty(val) && fieldLabel[i].equals("user_id")){
						if(!dbRegUser.validateUserId(val.trim())){
							logFailure(LangLabel.getValue(prof.cur_lan,"line_number") + lineno + " [" + label_title[i] + "] " +  LangLabel.getValue( prof.cur_lan,"not_loging_name")+".");
							valid = false;
						}else {
							dbRegUser dbRegUser=new dbRegUser();
							RegUser user= com.cw.wizbank.qdb.dbRegUser.getUsrBySteUsrId(con,val);
							if(user==null){
								logFailure(LangLabel.getValue(prof.cur_lan,"line_number") + lineno + " [" + label_title[i] + "] " +  LangLabel.getValue( prof.cur_lan,"not_found")+".");
								valid = false;
							}else if("DELETED".equals(user.getUsr_status())){
								logFailure(LangLabel.getValue(prof.cur_lan,"line_number") + lineno + " [" + label_title[i] + "] " +  LangLabel.getValue( prof.cur_lan,"not_deleted")+".");
								valid = false;
							}
						}
					}
				}
			}
		} else {
			logFailure(LangLabel.getValue( prof.cur_lan,"line_number") + lineno  + " "+LangLabel.getValue( prof.cur_lan,"empty_row"));
			valid = false;
		}
		return valid;
	}

	protected void log(String msg) {
		log.info(msg);
	}

	protected void log_debug(String msg) {
		log.info(msg);
	}

	protected void logSuccess(String msg) {
		logSuccess.info(msg);
	}

	protected void logFailure(String msg) {
		logFailure.info(msg);
	}
	
	protected void logFailure(String msg, Throwable e) {
		logFailure.error(msg, e);
	}

	// private void exceptionUser(String msg) throws Exception {
	// throw new Exception(msg);
	// }

	public boolean isEmail(String text) {
		Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	public boolean isDate(String text) {
		return isDate(text, content_date_format);
	}

	public boolean isDate(String text, String p) {
		boolean result = false;
		if (notEmpty(text)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(p);
				Date date = sdf.parse(text);
				result = true;
				// if (text.equals(sdf.format(date))) {
				// Calendar c = Calendar.getInstance();
				// c.setTime(date);
				// int year = c.get(Calendar.YEAR);
				// if (year >= 1753 && year <= 9999) {
				// result = true;
				// }
				// }
			} catch (Exception e) {
			}
		}
		return result;
	}
	
	public boolean isNumber(String value) {
		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public Vector<Long> getMapValueAsLongVector(Map<String, Long> map) {
		Vector<Long> vec = new Vector<Long>();
		if (notEmptyMap(map)) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				Long value = map.get(key);
				vec.add(value);
			}
		}
		return vec;
	}

	protected boolean notEmpty(String str) {
		return (str == null) ? false : str.trim().length() > 0;
	}

	protected boolean empty(String str) {
		return (str == null) ? true : str.trim().length() <= 0;
	}

	protected boolean notEmptyVector(Vector vec) {
		return (vec == null || vec.size() <= 0) ? false : true;
	}

	protected boolean notEmptyMap(Map map) {
		return (map == null || map.size() <= 0) ? false : true;
	}

	// 解析文件
	public Vector<String[]> parseSourseFile(File file,loginProfile prof) throws Exception {
		BufferedInputStream in = null;
			POIFSFileSystem fs = null;
			HSSFWorkbook wb = null;
			HSSFRow row =null;
			Vector<String[]> data = new Vector<String[]>();
			try {
				in = new BufferedInputStream(new FileInputStream(file));

			fs = new POIFSFileSystem(in);

			wb = new HSSFWorkbook(fs);

			HSSFCell cell = null;
		
			//获取第一个Sheet
			HSSFSheet st = wb.getSheetAt(0);
			if(st==null){
				return data;
			}
		
			int sumcol = fieldLabel.length+1;//添加一个行号
			boolean flag;
			// 循环Sheet中的行数
			for (int rowindex = 0; rowindex <= st.getLastRowNum(); rowindex++) {
 				flag = true;
				row = st.getRow(rowindex);
				if (row == null) {
					continue;
				}
				
				String[] val = new String[sumcol];

				// 循环列数
				for (int col = 0; col < sumcol; col++) {
					cell = row.getCell(col);
					if (cell == null) {
						val[col] = null;
					} else {
						val[col] = getCellValue(cell,col).trim();
					}
					if(val[col] != null && !"".equals(val[col])){ //如果每一行的最少有一列有值（除了空格），进入将flag变成false
						flag = false; 
					}
				}
				if(flag){continue;}//如果每列都没有值，然后又有空格则跳过
				val[val.length-1]=String.valueOf(rowindex+1);
				data.addElement(val);
			}
			


		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException("file parse error:" + e.getMessage());
		}
		return data;
	}

	//验证导入模板是否正确
	public boolean checkTemplate(File file) throws cwException {

		BufferedInputStream in = null;
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));

			fs = new POIFSFileSystem(in);

			wb = new HSSFWorkbook(fs);


			//获取第一个Sheet
			HSSFSheet st = wb.getSheetAt(0);
			if(st!=null){
				if(st.getRow(0)!=null&&st.getRow(0).getCell(fieldLabel.length)!=null){
					return false;
				}
			}





		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException("file parse error:" + e.getMessage());
		}
		return true;
	}


	public String getCellValue(Cell cell,int index) {
		String ret;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			ret = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			ret = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			ret = null;
			break;
		case Cell.CELL_TYPE_FORMULA:
			Workbook wb = cell.getSheet().getWorkbook();
			CreationHelper crateHelper = wb.getCreationHelper();
			FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
			ret = getCellValue(evaluator.evaluateInCell(cell),index);
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				//ret =String.valueOf(cell.getDateCellValue().getTime());
				DateFormat formater = new SimpleDateFormat("yyyy/MM/dd"); 
				ret = formater.format(cell.getDateCellValue().getTime());
			} else {
				//如果日期只输入了一串数字，那么就是格式错误
				if(index == 5 || index == 12 || index == 13){
					ret = "dateformaterror";
					break;
				}
				ret = NumberToTextConverter.toText(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			ret = cell.getRichStringCellValue().getString();
			break;
		default:
			ret = null;
		}

		return ret;
	}

	public Vector<String[]> splitFileToVector(File file, String enc, String delimiter) throws cwException, cwSysMessage {
		Vector<String[]> result = new Vector<String[]>();

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(file), cwUtils.ENC_UTF));
			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}

				String[] inElement = split(line.toString(), delimiter);
				result.addElement(inElement);
			}
			in.close();
		} catch (FileNotFoundException e) {
			throw new cwException("file error:" + e.getMessage() + " not found.");
		} catch (IOException e) {
			throw new cwException("read file error:" + e.getMessage());
		}
		return result;
	}

	public static String[] split(String str, String splitsign) {
		int index;
		if (str == null || splitsign == null)
			return null;

		ArrayList arr = new ArrayList();
		while ((index = str.indexOf(splitsign)) != -1) {
			arr.add(str.substring(0, index));
			str = str.substring(index + splitsign.length());
		}
		arr.add(str);
		return (String[]) arr.toArray(new String[0]);
	}

	public static String formatDate(Date d, String p) {
		if (d == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(p);
		return sdf.format(d);
	}

	protected Timestamp formatDate(String d) {
		Timestamp ts = null;
		if (notEmpty(d)) {
			Date dt = parseDate(d, content_date_format);
			if (dt != null) {
				ts = new Timestamp(dt.getTime());
			}
		}
		return ts;
	}

	public static Date parseDate(String d, String p) {
		if (d == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(p);
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (ParseException e) {
			CommonLog.error(e.getMessage(),e);
		}
		return date;
	}

	protected void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
	}

	public static void writeLog(String log_file, String content, Exception ine) {
		try {
			Date beginDate = new Date();
			String dateStr = formatDate(new Date(), "yy-MM-dd HH:mm:ss");
			content = dateStr + "  " + content;
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(log_file, true), "UTF-8"));
			out.write(content + System.getProperty("line.separator"));
			out.flush();
			if (ine != null) {
				out.write(ine.getMessage() + System.getProperty("line.separator"));
			}
			out.close();
		} catch (Exception e) {
			System.err.println("write file exception:" + e.getMessage());
		}
	}

	// 读取日志文件生成XML
	public String readLogToXml(File srouce_file,qdbEnv static_env,ImportStatus importStatus,Boolean usr_pwd_need_change_ind,Boolean identical_usr_no_import,Boolean oldusr_pwd_need_update_ind) throws Exception {
		StringBuilder xml = new StringBuilder();
		xml.append("<upload_user>");
		//添加总数
		xml.append("<record_count>"+importStatus.total+"</record_count>");
		xml.append("<pass_count>"+importStatus.cnt_pass_record+"</pass_count>");
		xml.append("<unpass_count>"+importStatus.cnt_nupass_record+"</unpass_count>");
		xml.append("<usr_pwd_need_change_ind>"+usr_pwd_need_change_ind+"</usr_pwd_need_change_ind>");
		xml.append("<identical_usr_no_import>"+identical_usr_no_import+"</identical_usr_no_import>");
		xml.append("<oldusr_pwd_need_update_ind>"+oldusr_pwd_need_update_ind+"</oldusr_pwd_need_update_ind>");
		try {
			String encoding = "UTF-8";
			File file = logFailureFile;
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				xml.append("<sourceFile>"+ cwUtils.esc4XML(getFilePath(srouce_file))+"</sourceFile>");
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					xml.append("<errMessage><err>" + StringUtils.replaceUtils(lineTxt) + "</err></errMessage>");
				}
				read.close();
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new Exception("err_get_soucre_file");
		}
		xml.append("</upload_user>");
		return xml.toString();
	}

	// 获取日志文件路径
	public static String getFilePath(File file) {
		String root ="www";
		if (file.getParentFile().getName().equals(root)) {
			return IMSLog.URI_PATH_SEPARATOR + file.getName();
		}
		return getFilePath(file.getParentFile()) + IMSLog.URI_PATH_SEPARATOR + file.getName();
	}

	public class ImportStatus {
		int cnt_insert = 0;
		int cnt_update = 0;
		int cnt_terminate = 0;
		int cnt_delete = 0;
		public int cnt_error = 0;
		int cnt_warn = 0;
		int total = 0;
		public int cnt_success = 0;
		int cnt_sum=0;
		int cnt_pass_record=0;
		int cnt_nupass_record=0;
		public int return_code = 0; // 50:Critical Error; 3:Warning ;0:
									// ntegration Success
		public String msg_code = Del.msg_code_w; // 50:Critical Error; 3:Warning
													// ;0: ntegration Success

		public Date start_date;
		public Date end_date;
		public File log_file;
		public File log_success_file;
		public File log_error_file;
		public File log_failure_file;
		public String logdir = "";

		public Vector del_id_lst = new Vector();

		public void addPassRecord(){
			this.cnt_pass_record++;
		}
		public void addnuPassRecord(){
			this.cnt_nupass_record++;
		}
		public void addSuccess() {
			this.cnt_success++;
		}

		public void setTotal(int count) {
			this.total = count;
		}

		public void setReturnCode(int return_code) {
			this.return_code = return_code;
		}

		public void addIns() {
			this.cnt_insert++;
		}

		public void addWarn() {
			this.cnt_warn++;
		}

		public void addUpd() {
			this.cnt_update++;
		}

		public void addTer() {
			this.cnt_terminate++;
		}

		public void addDel() {
			this.cnt_delete++;
		}

		public void addErr() {
			this.cnt_error++;
		}
	}
	public static Timestamp getTimestamp(String str){
		Timestamp timestamp=null;
		try{
			if(str.indexOf("/") != -1){
				String[] s = str.split("/");
				com.cwn.wizbank.utils.DateUtil dateUtil = new com.cwn.wizbank.utils.DateUtil();
				
				if(s.length >= 3){
					Date date = dateUtil.getDate(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					String dateStr = dateFormat.format(date);
					timestamp = Timestamp.valueOf(dateStr);
				}
			}else{
				
			}
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
		}
		return timestamp;
		
	}
	public static Timestamp getTimestamp1(String str){
		Timestamp timestamp=null;
		try{
			if(str.indexOf("/") != -1){
				String[] s = str.split("/");
				com.cwn.wizbank.utils.DateUtil dateUtil = new com.cwn.wizbank.utils.DateUtil();
				
				if(s.length >= 3){
					Date date = dateUtil.getDate(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					String dateStr = dateFormat.format(date);
					timestamp = Timestamp.valueOf(dateStr);
				}
			}else{
				if(str != null && !"".equals(str.trim())){
					long time=Long.valueOf(str);
					timestamp=new Timestamp(time);
				}
			}
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
		}
		return timestamp;
		
	}
//	public static String getStr(String str){
//		byte[] bytes =str.getBytes();
//		for (int j = 0; j < bytes.length; j++) {
//			if (bytes[j] == -96||bytes[j] == -62) {
//				bytes[j] = 32;
//			}
//		}
//		return new String(bytes);
//	}
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public boolean checkRowIsEmpty(String[] row) {
		boolean valid = true;
		if (row != null) {
			if (row.length-1 != data_long) {//没什么用
				valid = false;
			} else {
				for (int i = 0; i < fieldLabel.length; i++) {
					String val = row[i];
					if(val != null && !"".equals(val.trim())){
						valid = false;
					}
				}
			}
		} else {
			valid = false;
		}
		return valid;
	}

}
