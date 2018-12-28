package com.cw.wizbank.importcos;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;
import com.cw.wizbank.util.*;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.*;

public class ImportCos {
    static public String NETG_CDF_PARAM_NAME = "NAME=";
    static public String NETG_CDF_PARAM_DESC = "DESC=";
    static public String NETG_CDF_PARAM_DURA = "DURA=";
    static public String NETG_CDF_PARAM_EXEC = "EXEC=";
    static public String NETG_CDF_PARAM_COOKIE_NAME = "OPTINSTALLPARM=";
    static public String NETG_CDF_PARAM_COMP = "COMP=";

    static public String NETG_CDF_PARAM_HOUR = "HOURS";
    static public String NETG_CDF_PARAM_MIN = "MINUTES";
    static public String NETG_CDF_PARAM_SEC = "SECONDS";

	private final static String DEFAULT_ENC = "UnicodeLittle";
	    
    public dbModule myDbModule;
    public long new_mod_id;

    public String cookieName;
    
    public ImportCos() {
    }

    public ImportCos(dbModule inDbModule) {
        myDbModule = inDbModule;
    }

    public long importNETgCookie(Connection con, loginProfile prof, String domain, dbCourse myDbCourse, dbModule myDbModule, String cdfPath, Vector vtParentObj, boolean notCourse) throws IOException, SQLException, qdbException, cwSysMessage {
        String title = "";
        String desc = "";
        String launch_url = "";
        String cookie_name = "";
        float duration_in_min = 0;
        
        String paramName = "";
        String temp = "";
        
        long mod_id = 0;
        
        Vector obj_id_list = new Vector();
        
        String line = "";
        BufferedReader in = null;
        /*
		if( !cwUtils.isUnicodeLittleFile(new File(cdfPath)) ) {
			throw new qdbErrMessage("GEN008");
		} 
		*/       
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(cdfPath)), cwUtils.ENC_UTF));    
        } catch(Exception e) {
            throw new IOException(e.toString());
        }
        
        line = in.readLine();
        while (line != null) {
            line = line.trim();
            
            if (line.equalsIgnoreCase("[CDF Info]") == true) {
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("[") == true) {
                        break;
                    }
                }
            }
            else if (line.equalsIgnoreCase("[Course Info]") == true) {
                while ((line = in.readLine()) != null) {
                    if (line.toUpperCase().startsWith(NETG_CDF_PARAM_NAME) == true) {
                        title = line.substring(NETG_CDF_PARAM_NAME.length());
                    }
                    else if (line.toUpperCase().startsWith(NETG_CDF_PARAM_DESC) == true) {
                        if (desc.length() > 0) {
                            desc += "\n";
                        }
                        desc += line.substring(NETG_CDF_PARAM_DESC.length());
                    }
                    else if (line.toUpperCase().startsWith(NETG_CDF_PARAM_DURA) == true) {
                        temp = line.substring(NETG_CDF_PARAM_DURA.length());
                        if (line.toUpperCase().endsWith(NETG_CDF_PARAM_HOUR) == true) {
                            temp = temp.substring(0, temp.length()-NETG_CDF_PARAM_HOUR.length());
                            temp = temp.trim();
                            duration_in_min = Float.parseFloat(temp) * 60;
                        }
                        else if (line.toUpperCase().endsWith(NETG_CDF_PARAM_MIN) == true) {
                            temp = temp.substring(0, temp.length()-NETG_CDF_PARAM_MIN.length());
                            temp = temp.trim();
                            duration_in_min = Float.parseFloat(temp) * 1;
                        }
                        else if (line.toUpperCase().endsWith(NETG_CDF_PARAM_SEC) == true) {
                            temp = temp.substring(0, temp.length()-NETG_CDF_PARAM_SEC.length());
                            temp = temp.trim();
                            duration_in_min = Float.parseFloat(temp)/60;
                        }
                    }
                    else if (line.startsWith("[") == true) {
                        break;
                    }
                }
            }
            else if (line.equalsIgnoreCase("[Runtime Info]") == true) {
                while ((line = in.readLine()) != null) {
                    if (line.toUpperCase().startsWith(NETG_CDF_PARAM_EXEC) == true) {
                        launch_url = line.substring(NETG_CDF_PARAM_EXEC.length());
                    }
                    else if (line.startsWith("[") == true) {
                        break;
                    }
                }
            }
            else if (line.equalsIgnoreCase("[Workstation]") == true) {
                while ((line = in.readLine()) != null) {
                    if (line.toUpperCase().startsWith(NETG_CDF_PARAM_COOKIE_NAME) == true) {
                        cookie_name = line.substring(NETG_CDF_PARAM_COOKIE_NAME.length());
                    }
                    else if (line.startsWith("[") == true) {
                        break;
                    }
                }
            }
            else if (line.toUpperCase().equalsIgnoreCase("[Scoring Elements]") == true) {
                String tempObjStr = "";
                String developer_id = "";
                String obj_title = "";
                int obj_id = 0;
                
                dbObjective dbObj = null;
                
                int idx = 0;
                while ((line = in.readLine()) != null) {
                    if (line.toUpperCase().startsWith(NETG_CDF_PARAM_COMP) == true) {
                        tempObjStr = line.substring(NETG_CDF_PARAM_COMP.length());
                        StringTokenizer st = new StringTokenizer(tempObjStr, ";");
                        developer_id = st.nextToken();                        
                        obj_title = st.nextToken();
                        
                        if (developer_id.equalsIgnoreCase("00000000")) {
                            continue;
                        }
                        
                        if (!notCourse) {
	                        dbObj = new dbObjective();
	                        dbObj.obj_type = "NETG";
	                        dbObj.obj_desc = obj_title;
	                        // if the supplied parent is not empty, use it as the parent of the current objective
	                        if (vtParentObj.size() > 0) {
	                            dbObj.obj_obj_id_parent = ((Integer) vtParentObj.get(idx++)).longValue();
	                        }
	                        dbObj.obj_title = obj_title;
	                        dbObj.obj_developer_id = developer_id;
	                        dbObj.obj_import_xml = null;
	                        obj_id = (int)dbObj.insAicc(con, prof);
	                        obj_id_list.addElement(new Integer(obj_id));
                        }
                    }
                    else if (line.startsWith("[") == true) {
                        break;
                    }
                }
                // if the supplied parent is empty, use it to store newly inserted objective for the caller to use
                if (vtParentObj.size() == 0) {
                    for (int i = 0; i < obj_id_list.size(); i++) {
                        vtParentObj.addElement(obj_id_list.get(i));                       
                    }
                }
            }
            else {
                line = in.readLine();
            }
        }

        in.close();

//        myDbModule.mod_max_score = Float.parseFloat(max_score);;
//        myDbModule.mod_pass_score = Float.parseFloat(mastery_score);
        myDbModule.res_title = title;
        myDbModule.res_desc = desc;
        myDbModule.res_duration = duration_in_min;
        myDbModule.res_type = "MOD";
        myDbModule.res_subtype = "NETG_COK";
        myDbModule.mod_type = myDbModule.res_subtype;
        myDbModule.res_src_type = "CDF";
        //myDbModule.res_subtype = "AICC_AU";
        //myDbModule.res_src_type = "AICC_FILES";
        myDbModule.res_src_link = launch_url;
        cookieName = cookie_name;
        if (!notCourse) {
    
            mod_id = myDbCourse.insModule(con, myDbModule, domain, prof);
            myDbModule.saveNETgSpecificInfo(con, prof, cookie_name);
       
            long[] int_mod_id_list = new long[] { mod_id };
            long[] int_obj_id_list = new long[1];
            dbResourceObjective dbResObj = new dbResourceObjective();
            for (int i=0; i<obj_id_list.size(); i++) {
                int_obj_id_list[0] = ((Integer)obj_id_list.elementAt(i)).longValue();
                dbResObj.insResObj(con, int_mod_id_list, int_obj_id_list);
            }
        }
        
        return mod_id;
    }
    
    /**
     * SCORM课件解析器
     * <br>用来将imsmanifest.xml文件的模块，添加为课程或班级中的内容模块
     */
    protected ScormContentParser myScormContentParser = null;

    public boolean importScorm(Connection con, loginProfile prof, int cos_id, String csfPath) throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage {
        dbCourse myDbCourse = new dbCourse();
        myDbCourse.cos_res_id = cos_id;
        myDbCourse.res_id = cos_id;
        myDbCourse.get(con);
        
        myScormContentParser = new ScormContentParser(con, prof, myDbCourse);
        myScormContentParser.parseCSF(csfPath);
        
        return true;
    }
    
    public boolean importScorm1_2(Connection con, loginProfile prof, int cos_id, String imsmanifestPath, String cosUrlPrefix, boolean notCourse, boolean isUpdate, boolean fromzip, dbCourse myDbCourse) throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage, cwException {
       
        
        
        myScormContentParser = new ScormContentParser(con, prof, myDbCourse);
        myScormContentParser.setScormVerion(ScormContentParser.SCORM_VERSION_1_2);
        myScormContentParser.fromzip = fromzip;
        myScormContentParser.parseImsmanifest(imsmanifestPath, cosUrlPrefix, notCourse, null);
        new_mod_id = myScormContentParser.mod_id;
        return true;
    }
    
    /**
     * Import scorm2004 imsmanifestfile.
     * @param con
     * @param prof
     * @param cos_id
     * @param imsmanifestPath
     * @param cosUrlPrefix
     * @param notCourse
     * @param isUpdate
     * @return
     */
    public boolean importScorm2004(Connection con, loginProfile prof, int cos_id, String imsmanifestPath, String cosUrlPrefix, boolean notCourse, boolean isUpdate, boolean fromzip, WizbiniLoader wizbini, dbCourse myDbCourse) throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage, cwException {
 
        
        myScormContentParser = new ScormContentParser(con, prof, myDbCourse);
        myScormContentParser.setScormVerion(ScormContentParser.SCORM_VERSION_2004);
        myScormContentParser.fromzip = fromzip;
        myScormContentParser.parseImsmanifest(imsmanifestPath, cosUrlPrefix, notCourse, wizbini);
        new_mod_id = myScormContentParser.mod_id;
    	return true;
    }
    
    /**
     * 获取已添加的模块ID列表
     */
    public List getResIdListInserted() {
    	return myScormContentParser == null ? new ArrayList() : myScormContentParser.getResIdListInserted();
    }
    
}