/**
 * <p>Title:cwOrgProducer</p>
 * <p>Description:core version 4.0.22b</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Cyberwisdom.net</p>
 * <p>e-mail:qjqyx@avl.com.cn</p>
 * @author Christ Qiu	
 * @version 4.0.22b
 */
package com.cw.wizbank.organization;

import java.sql.*;
import java.util.*;
import java.io.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.batch.batchUtil.*;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewCwOrgProducer;
import com.cwn.wizbank.utils.CommonLog;

public class cwOrgProducer{
	public cwOrgProducer() {
}
	private static long new_ste_ent_id;
	public static void main(String argv[]) {
	    Connection con = null;
	    try {
	        int arg_index = 0;
	        String inifile = argv[arg_index++];
	        String src_ste_id = argv[arg_index++];
	        String target_ste_id = argv[arg_index++];
	        String target_ste_name = argv[arg_index++];
			String srcDir = argv[arg_index++];
            con = BatchUtils.openDB(inifile);
			//<Christ>
			String sourceDir = srcDir + File.separatorChar + src_ste_id;
			String desDir = srcDir + File.separatorChar + target_ste_id;
			//</Christ>
			Hashtable htNewRoleID = new Hashtable();
			Hashtable htNewRoleExtID = new Hashtable();
			ViewCwOrgProducer myViewCwOrgProducer = new ViewCwOrgProducer(con, htNewRoleID, htNewRoleExtID);
            long src_ste_ent_id = myViewCwOrgProducer.get_src_ent_id(con, src_ste_id);
			long target_ste_ent_id =
				myViewCwOrgProducer.copyRootNode(con, src_ste_ent_id, target_ste_id, target_ste_name);
			new_ste_ent_id = target_ste_ent_id;
			CommonLog.info("Finish copying Root Node.");            
            //long target_ste_ent_id = 68;
            myViewCwOrgProducer.copyRecycleBin(con, src_ste_ent_id, target_ste_ent_id);
            CommonLog.info("Finish copying Recycle Bin.");            
			myViewCwOrgProducer.copyUserRole(con, src_ste_ent_id, target_ste_ent_id);
			CommonLog.info("Finish copying User Role.");            
			Vector vtSrcEntityID = new Vector();
			Vector vtNewEntityID = new Vector();
			myViewCwOrgProducer.copyUserGrade(
				con,
				src_ste_ent_id,
				target_ste_ent_id,
				vtSrcEntityID,
				vtNewEntityID);
			CommonLog.info("Finish copying User Grade.");
			String new_admin_usr_id =
				myViewCwOrgProducer.copySysUser(
					con,
					src_ste_ent_id,
					target_ste_ent_id,
					vtSrcEntityID,
					vtNewEntityID);
			CommonLog.info("Finish copying Sys Users.");            
           	//String new_admin_usr_id = "s68u70";
            myViewCwOrgProducer.copyHomepageFtn(con, src_ste_ent_id, target_ste_ent_id, new_admin_usr_id);
            CommonLog.info("Finish copying Homepage Functions.");            
            
			myViewCwOrgProducer.copyLearningSolnType(
				con,
				src_ste_ent_id,
				target_ste_ent_id,
				new_admin_usr_id);
			CommonLog.info("Finish copying Learning Soln Type.");            
			myViewCwOrgProducer.copyManagementReport(
				con,
				src_ste_ent_id,
				target_ste_ent_id,
				new_admin_usr_id);
			CommonLog.info("Finish copying Management Report.");
            
            //copy head training center and link target entity to "all usergroup"
            long head_tc_id = DbTrainingCenter.copyHeadTcToAnotherOrg(con, src_ste_ent_id, target_ste_ent_id, new_admin_usr_id);
            //copy training center and user group relate.
            //"all usergroup"'s id always same to site_id. 
            DbTrainingCenter obj = new DbTrainingCenter(head_tc_id);
            long[] top_user_group = new long[1];
            top_user_group[0] = target_ste_ent_id;
            obj.setTcr_update_usr_id(new_admin_usr_id);
            obj.storeTargetEntity(con,top_user_group);
            CommonLog.info("Finish copying head training center and tcTargetEntity.");
			
			//<Christ Qiu>
			//copy other related tables of a new organization
			myViewCwOrgProducer.copyOtherRelatedTab(con, src_ste_ent_id, target_ste_ent_id);
			CommonLog.info("Finish copying Other Related Tables.");
            con.commit();
			//copy related directories and files of a new organization
			cwOrgProducer.copyConfigDir(sourceDir, desDir, htNewRoleExtID);
			CommonLog.info("Finish copying Root Dirs and Files.");
			CommonLog.info("Closing connections...");
			//</Christ Qiu>
			con.close();
        } catch (IOException e) {
            try {
                con.rollback();
			} catch (Exception e2) {
			};
			CommonLog.error("Error in copying organization:" + e.toString());
        } catch (SQLException e) {
        	CommonLog.error(e.getMessage(),e);
            try {
                con.rollback();
			} catch (Exception e2) {
			};
			CommonLog.error("Error in copying organization:" + e.toString());
        } catch (cwException e) {
            try {
                con.rollback();
            } catch (Exception e2) {};
            CommonLog.error("Error in copying organization:" + e.toString());
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			CommonLog.error("Error in copying organization:" + e.toString());
		} finally {
			try {
				if (con.isClosed()) {
					CommonLog.info("Succeed!");
				}else{
					try {
                            con.commit();
							con.close();
							CommonLog.info("ok!");
						} catch (SQLException e) {
							CommonLog.info("Can't close connection.");
						}
					
				}
			} catch (SQLException e1) {
				CommonLog.error("Error in copying organization:" + e1.toString());
		}

	}
	}

	/**
	 * copy related directories and files for new site
	 * @author:Christ Qiu
	 * e-mail:qjqyx@avl.com.cn
	 * @param:sourceDir: source organization directory
	 * @param:targetDir: directory for new organization
	 * @param:htNewRoleExtID:rol_ext_id of new organization
	 * 
	 */
	
	public static void copyConfigDir(String sourceDir, String targetDir, Hashtable htNewRoleExtID)
		throws IOException {
		File sourceFile = new File(sourceDir);
		if (sourceFile.exists() && sourceFile.isDirectory()) {
			File targetFile = new File(targetDir);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			} //for all files of source dir do: 
			String[] filesToCopy = sourceFile.list();
			int count = filesToCopy.length;
			for (int i = 0; i < count; i++) {
				//for each source file do     
				String fileName = filesToCopy[i];
				File file = new File(sourceDir + File.separatorChar + fileName);

				if (file.isFile()) {
					cwOrgProducer.copyFile(
						fileName,
						sourceDir + File.separatorChar + fileName,
						targetDir,
						htNewRoleExtID);

				} else { //file is directory            
					cwOrgProducer.copyConfigDir(
						sourceDir + File.separatorChar + fileName,
						targetDir + File.separatorChar + fileName,
						htNewRoleExtID);
				}
			} //next file 

		}
	}
	/**
	 * copy related files for new site
	 * @author:Christ Qiu
	 * e-mail:qjqyx@avl.com.cn
	 * @param fileName: new status operate by approver
	 * @param srcFile: name of a source file
	 * @param:targetDir:directory for new organization
	 * @param:htNewRoleExtID:rol_ext_id of new organization
	 */
	public static void copyFile(String fileName, String srcFile, String targetDir, Hashtable htNewRoleExtID)
		throws IOException {
		File file = new File(targetDir + File.separatorChar + fileName);
		//			BufferedReader src_buf = new BufferedReader(new FileReader(srcFile));
		//			BufferedWriter des_buf = new BufferedWriter(new FileWriter(file));
		BufferedReader src_buf =
			new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF8"));
		BufferedWriter des_buf =
			new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
		String line = new String();
		if (!fileName.equalsIgnoreCase("personalization.xml")) {
			while ((line = src_buf.readLine()) != null) {
				des_buf.write(line);
				des_buf.newLine();
			}
		} else {
			while ((line = src_buf.readLine()) != null) {
				for (Enumeration e = htNewRoleExtID.keys(); e.hasMoreElements();) {
					String src_rol_ext_id = (String) e.nextElement();
					String new_rol_ext_id = (String) htNewRoleExtID.get(src_rol_ext_id);
					if (line.indexOf(src_rol_ext_id) != -1) {
						line = 
							ViewCwOrgProducer.replaceStr(
								line,
								"<role>" + src_rol_ext_id + "</role>",
								"<role>" + new_rol_ext_id + "</role>");
					}
				}
				des_buf.write(line);
				des_buf.newLine();
			}
		}
		src_buf.close();
		des_buf.close();
	}
}