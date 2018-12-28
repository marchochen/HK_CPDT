package com.cw.wizbank.dataupgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeTreeNodeRelation;
import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbPsnBiography;
import com.cw.wizbank.db.DbRegUser;
import com.cw.wizbank.db.view.ViewCourseMeasurement;
import com.cw.wizbank.enterprise.IMSUtils;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwIniFile;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author DeanChen version from: 4.7 version to: 5.0.0
 */
public class Version5_0_0 {

	private static final String _MSG_INVALID_INPUT_ARGV = "Usage: Version5_0_0 -inifile filename -approot path -userpropertymapfile filename -log filename ";

	private static final String _MSG_OPERATION_FAIL = "fail to";
	private static final String _MSG_START = "start...";
	private static final String _MSG_END = "end.";
	private static final String _MSG_SUCCESS_UPGRADE = "upgrade completed!";
	
	private static final String _MSG_READ_INIFILE = "read inifile";
	private static final String _MSG_BUILD_CONNECTION = "build database connection";
	private static final String _MSG_READ_USER_PROPERTY_PROPERTY_FILE = "read user property file";
	private static final String _MSG_READ_LOG_FILE = "get log file";
	
	private static final String _MSG_UPD_USER_PROPERTY = "update user property";
	private static final String _MSG_UPD_STUDY_PROGRESS_RATE = "update study progress of user";
	private static final String _MSG_UPD_COS_STRUCTURE_JSON = "update json structure of course";
	private static final String _MSG_UPD_ITEM_SRH_CONTENT = "update search content of item";
	private static final String _MSG_INS_TREE_NODE_RELATION = "insert the relation that is between directory and directory and between item and directory";
	
	private static final String _INTERVAL_UNDERLIINE = "_";
	
	// initialization file
	private String iniFile = null;
	private String app_root = null;
	private String doc_root = null;
	// the map of user property between version 5.0.0 and version 4.7.0
	private String diff_user_property_map_filepath = null;
	// log file
	public String log_file = null;

	private loginProfile prof = null;
	private Hashtable diff_user_property_map = null;
	private Connection con = null;

	public static void main(String[] args) {
		Version5_0_0 versionObj = new Version5_0_0();
		versionObj.parseArguments(args);

		try {
			versionObj.init();
			versionObj.process();
			versionObj.commit();
		} catch (Exception e) {
			try {
				versionObj.rollback();
				versionObj.close();
			} catch (SQLException sqle) {
			}
		} 
	}

	private void parseArguments(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i].charAt(0) == '-') {
					if (args[i].equalsIgnoreCase("-inifile")) {
						this.iniFile = args[++i];
					} else if (args[i].equalsIgnoreCase("-approot")) {
						i++;
						if (isVaildFilePath(args[i])) {
							this.app_root = args[i];
						} else {
							new Exception("Invaild approot path.");
						}
					} else if (args[i].equalsIgnoreCase("-userpropertymapfile")) {
						i++;
						if (isVaildFilePath(args[i])) {
							this.diff_user_property_map_filepath = args[i];
						} else {
							new Exception("Invaild userpropertymapfile path.");
						}
					} else if (args[i].equalsIgnoreCase("-log")) {
						i++;
						if (args[i] != null && args[i].length() > 0) {
							this.log_file = args[i];
						} else {
							new Exception("Invaild log path.");
						}
					} else {
						throw new IllegalArgumentException("Invalid argument:" + args[i] + cwUtils.NEWL + _MSG_INVALID_INPUT_ARGV);
					}
				} else {
					throw new IllegalArgumentException("Invalid argument:" + args[i] + cwUtils.NEWL + _MSG_INVALID_INPUT_ARGV);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(_MSG_INVALID_INPUT_ARGV);
		}
	}
	
	private void init() throws Exception {
		try {
			this.openLogFile();
		} catch (Exception e) {
			throw new Exception(getFailMsg(_MSG_READ_LOG_FILE));
		}
		
		try {
			this.readIniFile();
		} catch (Exception e) {
			writeLog(getFailMsg(_MSG_READ_INIFILE) + ":" + iniFile);
			writeLog(e);
			throw new Exception(e.getMessage());
		}

		try {
			this.readUserPropertyMap();
		} catch (Exception e) {
			writeLog(getFailMsg(_MSG_READ_USER_PROPERTY_PROPERTY_FILE) + ":" + diff_user_property_map_filepath);
			writeLog(e);
			throw new Exception(e.getMessage());
		}

		try {
			this.con = BatchUtils.openDB(this.app_root);
		} catch (Exception e) {
			writeLog(getFailMsg(_MSG_BUILD_CONNECTION));
			writeLog(e);
			throw new Exception(e.getMessage());
		}
	}

	private void readIniFile() throws IOException {
		prof = new loginProfile();

		cwIniFile inifile = new cwIniFile(this.iniFile);

		if (inifile.getValue("TADM_USR_ID") != null && !inifile.getValue("TADM_USR_ID").equals("")) {
			prof.usr_id = inifile.getValue("TADM_USR_ID");
		} else {
			prof.usr_id = "s1u3";
		}
		if (inifile.getValue("TADM_USR_ENT_ID") != null && !inifile.getValue("TADM_USR_ENT_ID").equals("")) {
			prof.usr_ent_id = Long.valueOf(inifile.getValue("TADM_USR_ENT_ID")).longValue();
		} else {
			prof.usr_ent_id = 3;
		}
		if (inifile.getValue("DOC_ROOT") != null && !inifile.getValue("DOC_ROOT").equals("")) {
			doc_root = app_root + File.separator + inifile.getValue("DOC_ROOT");
		} else {
			doc_root = app_root + File.separator + "www";
		}
		
		prof.xsl_root = "xsl";
	}

	private void readUserPropertyMap() throws IOException {
		Properties userpropertymap = new Properties();
		userpropertymap.load(cwUtils.openUTF8FileStream(new File(this.diff_user_property_map_filepath)));
		this.diff_user_property_map = userpropertymap;
	}
	
	private void process() throws Exception {
		try {
			writeLog(getStartMsg(_MSG_UPD_USER_PROPERTY));
			this.updUserProperty();
			writeLog(getEndMsg(_MSG_UPD_USER_PROPERTY));
		} catch (Exception e) {
			writeLog(e);
			throw new Exception(e.getMessage());
		}
		
		try {
			writeLog(getStartMsg(_MSG_UPD_STUDY_PROGRESS_RATE));
			this.updProgressRateOfCourse();
			writeLog(getEndMsg(_MSG_UPD_STUDY_PROGRESS_RATE));
		} catch (Exception e) {
			writeLog(e);
			throw new Exception(e.getMessage());
		}
		
		try {
			writeLog(getStartMsg(_MSG_UPD_COS_STRUCTURE_JSON));
			this.updCourseStructureJson();
			writeLog(getEndMsg(_MSG_UPD_COS_STRUCTURE_JSON));
		} catch (Exception e) {
			writeLog(e);
			throw new Exception(e.getMessage());
		}
		
		try {
			writeLog(getStartMsg(_MSG_UPD_ITEM_SRH_CONTENT));
			this.updItemSearchContent();
			writeLog(getEndMsg(_MSG_UPD_ITEM_SRH_CONTENT));
		} catch (Exception e) {
			writeLog(e);
			throw new Exception(e.getMessage());
		}
		
		try {
			writeLog(getStartMsg(_MSG_INS_TREE_NODE_RELATION));
			this.insTreeNodeRelation();
			writeLog(getEndMsg(_MSG_INS_TREE_NODE_RELATION));
		} catch (Exception e) {
			writeLog(e);
			throw new Exception(e.getMessage());
		}
	}

	private void updUserProperty() throws SQLException {
		this.updPsnBiographyOption();
		this.updUserSelfDesc();
	}

	private void updPsnBiographyOption() throws SQLException {
		Hashtable pbgOptionMap = DbPsnBiography.getAllPbgOption(con);
		Enumeration pbgOptionMapKeys = getHashtableKeys(pbgOptionMap);
		
		while (pbgOptionMapKeys != null && pbgOptionMapKeys.hasMoreElements()) {
			Long userEntId = (Long) pbgOptionMapKeys.nextElement();
			String OldPbgOption = (String) pbgOptionMap.get(userEntId);
			String newPbgOption = reBuildPbgOption(OldPbgOption);
			if(newPbgOption != null && newPbgOption.length() > 0) {
				DbPsnBiography.updatePbgOption(con, userEntId.longValue(), newPbgOption);
			}
		}
	}
	
	private String reBuildPbgOption(String oldPbgOptionStr) {
		String newPbgOptionStr = "";
		Enumeration propertyMapKeys = getHashtableKeys(diff_user_property_map);
		while (propertyMapKeys != null && propertyMapKeys.hasMoreElements()) {
			String oldProperty = (String) propertyMapKeys.nextElement();
			String newProperty = (String) diff_user_property_map.get(oldProperty);
			//check whether the property is updated and the property is exist.
			if(oldPbgOptionStr != null 
					&& oldPbgOptionStr.indexOf(newProperty + DbPsnBiography._PBG_OPTION_INTERVAL) == -1 
					&& oldPbgOptionStr.indexOf(oldProperty + DbPsnBiography._PBG_OPTION_INTERVAL) != -1) {
				int newPbgOptionLength = newPbgOptionStr.length() + newProperty.length() + 1;
				//check whether the length of new pbg_option is greater than the max length of property pbg_option in database.
				if(newPbgOptionLength <= DbPsnBiography._PBG_OPTION_LENGTH) {
					newPbgOptionStr += newProperty + DbPsnBiography._PBG_OPTION_INTERVAL;
				} else {
					break;
				}
			} 
		}
		return newPbgOptionStr;
	}

	private void updUserSelfDesc() throws SQLException {
		Hashtable userSelfDescMap = DbPsnBiography.getUserSelfDesc(con);

		Enumeration userSelfDescMapKeys = getHashtableKeys(userSelfDescMap);
		while (userSelfDescMapKeys != null && userSelfDescMapKeys.hasMoreElements()) {
			Long userEntIdObj = (Long) userSelfDescMapKeys.nextElement();
			DbRegUser.updateUserSelfDesc(con, userEntIdObj.longValue(), (String) userSelfDescMap.get(userEntIdObj));
		}
	}

	private void updProgressRateOfCourse() throws SQLException, qdbErrMessage, qdbException, cwSysMessage {
		// get all aeApplication and related record
		Vector cosCriteriaVec = DbCourseCriteria.getCourseCriteriaByType(con, DbCourseCriteria.TYPE_COMPLETION);
		
		// compute progress rate
		Hashtable cosEvaluationMap = getCosProgressMap(cosCriteriaVec);
		
		// update progress rate
		Enumeration cosEvaluationKeys = getHashtableKeys(cosEvaluationMap);
		while(cosEvaluationKeys != null && cosEvaluationKeys.hasMoreElements()) {
			String cosEvaluationKey = (String) cosEvaluationKeys.nextElement();
			dbCourseEvaluation.updateCosProgress(con, Long.parseLong(cosEvaluationKey.split(_INTERVAL_UNDERLIINE)[0]),
					Long.parseLong(cosEvaluationKey.split(_INTERVAL_UNDERLIINE)[1]), 
					Long.parseLong(cosEvaluationKey.split(_INTERVAL_UNDERLIINE)[2]), 
					((Float)cosEvaluationMap.get(cosEvaluationKey)).floatValue());
		}
		
	}

	private Hashtable getCosProgressMap(Vector cosCriteriaVec) throws SQLException, qdbErrMessage, qdbException, cwSysMessage {
		Hashtable cosEvaluationMap = new Hashtable();
		if (cosCriteriaVec != null) {
			for (int appIndex = 0; appIndex < cosCriteriaVec.size(); appIndex++) {
				HashMap cosCriteriaMap = (HashMap) cosCriteriaVec.elementAt(appIndex);
				long userEntId = ((Long)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_APP_ENT_ID)).longValue();
				long tkhId = ((Long)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_APP_TKH_ID)).longValue();
				float attRate = ((Float)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_ATT_RATE)).floatValue();
				long ccrId = ((Long)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_CCR_ID)).longValue();
				boolean ccrPassInd = ((Boolean)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_CCR_PASS_IND)).booleanValue();
				float ccrPassScore = ((Float)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_CCR_PASS_SCORE)).floatValue();
				int ccrAttendanceRate = ((Integer)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_CCR_ATTENDANCE_RATE)).intValue();
				long cosResId = ((Long)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_COS_RES_ID)).longValue();
				float cosScove = ((Float)cosCriteriaMap.get(DbCourseCriteria._MAP_KEY_COV_SCORE)).floatValue();
				
				/* need optimized: couple with database access layout */
				DbCourseCriteria ccr = new DbCourseCriteria();
				ccr.ccr_id = ccrId;
				List cmtList = ViewCourseMeasurement.getRelateMeasurement(con, ccrId);
				CourseCriteria.checkAllMeasurement(con, ccr, cmtList, userEntId, tkhId);
				
				//get cov_progress
				float covProgress = CourseCriteria.getCovProgress(ccrPassInd, ccrAttendanceRate, cosScove, ccrPassScore, attRate, ccrAttendanceRate, 
						ccr.passCondCnt, ccr.allCondCnt);
				
				String cosEvaluationMapKey = cosResId + _INTERVAL_UNDERLIINE + userEntId + _INTERVAL_UNDERLIINE + tkhId;
				
				cosEvaluationMap.put(cosEvaluationMapKey, new Float(covProgress));
			}
		}
		return cosEvaluationMap;
	}

	private void updCourseStructureJson() throws SQLException, cwException {
		Hashtable cosStructureXmlMap = dbCourse.getCosStructureXmlMap(con);

		Enumeration cosStructureXmlHashKeys = getHashtableKeys(cosStructureXmlMap);
		while (cosStructureXmlHashKeys != null && cosStructureXmlHashKeys.hasMoreElements()) {
			Long resIdObj = (Long) cosStructureXmlHashKeys.nextElement();
			qdbEnv static_env = new qdbEnv();
			static_env.DOC_ROOT = this.doc_root;
			String cosStructureJson = static_env.transformXML((String) cosStructureXmlMap.get(resIdObj),"cos_structure_json_js.xsl", prof.xsl_root);
			dbCourse.updCosStructureJson(con, resIdObj.longValue(), cosStructureJson);
		}
	}

	private void updItemSearchContent() throws SQLException, cwException {
		Hashtable itemIdAndXmlMap = aeItem.getItemIdAndXmlMap(con);

		Enumeration itemIdAndXmlMapKeys = getHashtableKeys(itemIdAndXmlMap);
		aeItem itm = new aeItem();
		while (itemIdAndXmlMapKeys != null && itemIdAndXmlMapKeys.hasMoreElements()) {
			Long itemIdObj = (Long) itemIdAndXmlMapKeys.nextElement();
			itm.itm_id = itemIdObj.longValue();
			itm.itm_xml = (String) itemIdAndXmlMap.get(itemIdObj);
			Vector vClobColName = new Vector();
			Vector vClobColValue = new Vector();
			vClobColName.add("itm_xml");
			vClobColValue.add(itm.itm_xml);
			itm.updOterClobColumns(con, vClobColName, vClobColValue);
		}
	}

	private void insTreeNodeRelation() throws SQLException {
		/* insert relation between tnd and tnd */
		// get all related record in aeTreeNode and build catalog tree with hashtable
		Vector normalTreeNodeVec = aeTreeNode.getTreeNodeRelation(con, aeTreeNode.TND_TYPE_NORMAL);
		Hashtable catalogNodeRelationMap = buildAllParentNodeTree(normalTreeNodeVec, null);
		insertTreeNodeRelationList(catalogNodeRelationMap, aeTreeNodeRelation.TNR_TYPE_TND_PARENT_TND);

		/* insert relation between tnd and item */
		// copy tnd tree above
		// get all related record in aeTreeNode and build tnd tree with hashtable(add element of direct parent relation)
		Vector itemTreeNodeVec = aeTreeNode.getTreeNodeRelation(con, aeTreeNode.TND_TYPE_ITEM);
		Hashtable itemNodeRelationMap = buildAllParentNodeTree(itemTreeNodeVec, catalogNodeRelationMap);
		itemNodeRelationMap = filterCatalogRelationMap(itemNodeRelationMap, itemTreeNodeVec);
		insertTreeNodeRelationList(itemNodeRelationMap, aeTreeNodeRelation.TNR_TYPE_ITEM_PARENT_TND);
	}
	
	private Hashtable filterCatalogRelationMap(Hashtable treeNodeRelationMap, Vector itemTreeNodeVec) {
		Hashtable itemRelationMap = new Hashtable();
		if (itemTreeNodeVec != null) {
			for (int treeNodeIndex = 0; treeNodeIndex < itemTreeNodeVec.size(); treeNodeIndex++) {
				Vector treeNode = (Vector) itemTreeNodeVec.elementAt(treeNodeIndex);
				Long tndIdObj = (Long) treeNode.elementAt(0);
				if (treeNodeRelationMap != null && treeNodeRelationMap.containsKey(tndIdObj)) {
					itemRelationMap.put(tndIdObj, treeNodeRelationMap.get(tndIdObj));
				}
			}
		}
		return itemRelationMap;
	}

	/**
	 * build all relation of related node
	 * 
	 * @param allTreeNodeMap the record in table aeTreeNode according to related type
	 * @param treeNodeRelationMap all relation of allTreeNodeMap according to related type
	 * @return
	 */
	public Hashtable buildAllParentNodeTree(Vector allTreeNodeVec, Hashtable treeNodeRelationMap) {
		if (treeNodeRelationMap == null) {
			treeNodeRelationMap = new Hashtable();
		}
		
		if(allTreeNodeVec != null) {
			for(int treeNodeIndex = 0; treeNodeIndex < allTreeNodeVec.size(); treeNodeIndex++) {
				Vector treeNode = (Vector) allTreeNodeVec.elementAt(treeNodeIndex);
				buildParentNodeTree(treeNodeRelationMap, ((Long)treeNode.elementAt(0)).longValue(), ((Long)treeNode.elementAt(1)).longValue());
			}
		}
		return treeNodeRelationMap;
	}

	private Hashtable buildParentNodeTree(Hashtable treeNodeMap, long tndId, long parentTndId) {
		Vector parentTndIdVec = new Vector();
		
		if (treeNodeMap.get(new Long(tndId)) != null) {// if parent structure vector is not null, you need only to add current catalog relation to the vector.
			parentTndIdVec.addAll((Vector) treeNodeMap.get(new Long(tndId)));
		} else if (treeNodeMap.get(new Long(parentTndId)) != null) {//if parent structure vector is null and parent structure vector of parent node is not null:add current catalog relation to the vector;
			parentTndIdVec.addAll((Vector) treeNodeMap.get(new Long(parentTndId)));
		}

		// add element into parent vector
		if (parentTndIdVec.contains(new Long(parentTndId))) {
			// filter repeat record
		} else {
			parentTndIdVec.addElement(new Long(parentTndId));
		}

		// update parent node tree of current catalog
		treeNodeMap.put(new Long(tndId), parentTndIdVec);

		return treeNodeMap;
	}

	private void insertTreeNodeRelationList(Hashtable treeNodeMap, String tndType) throws SQLException {
		Enumeration treeNodeMapKeys = treeNodeMap.keys();
		while (treeNodeMapKeys.hasMoreElements()) {
			Long tndIdObj = (Long) treeNodeMapKeys.nextElement();
			Vector parentTndIdVec = (Vector) treeNodeMap.get(tndIdObj);
			aeTreeNodeRelation treeNodeRelation = new aeTreeNodeRelation();
			treeNodeRelation.ins(con, prof.usr_id, tndType, parentTndIdVec, tndIdObj.longValue());
		}
	}

	private void openLogFile() throws IOException {
		File logFile = new File(this.log_file);
		if(!logFile.exists()) {
			if(!(new File(logFile.getParent())).exists()) {
				new File(logFile.getParent()).mkdirs();
			}
			logFile.createNewFile();
		}
		IMSUtils.setLogDir(logFile.getParent());
	}
	
	private static String getStartMsg(String logMessage) {
		return "\"" + logMessage + "\" " + _MSG_START;
	}
	
	private static String getEndMsg(String logMessage) {
		return "\"" + logMessage + "\" " + _MSG_END;
	}
	
	private static String getFailMsg(String logMessage) {
		return _MSG_OPERATION_FAIL + " "  + logMessage;
	}
	
	private void writeLog(String logMessage) {
		try{
        	long my_time = System.currentTimeMillis();
            Date my_date = new Date(my_time);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(log_file, true), cwUtils.ENC_UTF));
            out.write("[" + my_date.toString() + "] " + logMessage + System.getProperty("line.separator"));
            out.close();
        }catch (Exception e){
            System.err.println("write file exception:" + e.getMessage());
        }
	}
	
	private void writeLog(Exception ine) {
		try{
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(log_file, true), cwUtils.ENC_UTF));
            ine.printStackTrace(out);
            CommonLog.error(ine.getMessage(),ine);
            out.close();
        }catch (Exception e){
            System.err.println("write file exception:" + e.getMessage());
        }
	}

	public void commit() throws SQLException {
		if (this.con != null) {
			this.con.commit();
		}
		writeLog(_MSG_SUCCESS_UPGRADE);
	}

	public void rollback() throws SQLException {
		if (this.con != null) {
			this.con.rollback();
		}
	}
	
	public void close() throws SQLException {
		if (this.con != null) {
			this.con.close();
		}
	}
	
	public static boolean isVaildFilePath(String filePath) {
		boolean isVaildFilePath = false;
		if(filePath != null && !"".equals(filePath)) {
			File file = new File(filePath);
			isVaildFilePath = file.exists();
			file = null;
		}
		return isVaildFilePath;
	}
	
	public static Enumeration getHashtableKeys(Hashtable hashtable) {
		return hashtable == null ? null : hashtable.keys();
	}
}
