package com.cw.wizbank.qdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;
import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.util.cwSQL;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TempDirCleaner extends ScheduledTask implements Job{
    private static final long defCheckFrequency = 60*60*1000;
    private static final long defExpireDuration = 60*60*1000;
    
    private long checkFrequency = defCheckFrequency;
    private long expireDuration = defExpireDuration;
    
    private Vector TargetDirList = new Vector();

    public void setCheckFrequency(long inCheckFrequency) {
        if (inCheckFrequency <= 0) {
            this.checkFrequency = TempDirCleaner.defCheckFrequency;
        } else {
            this.checkFrequency = inCheckFrequency * 1000;
        }
    }
    public void setExpireDuration(long inExpireDuration) {
        if (inExpireDuration <= 0) {
            this.expireDuration = TempDirCleaner.defExpireDuration;
        } else {
            this.expireDuration = inExpireDuration * 1000;
        }
    }
    
    
    public void setResTmpUploadDir(String inResTmpUploadDir) {
        //this.resTmpUploadDir = inResTmpUploadDir;
        TargetDirList.addElement(new TargetDir(inResTmpUploadDir, this.expireDuration));
        
    }
    public void setItmTmpUploadDir(String inItmTmpUploadDir) {
        //this.itmTmpUploadDir = inItmTmpUploadDir;
		TargetDirList.addElement(new TargetDir(inItmTmpUploadDir, this.expireDuration));
    }
    public void setTmpUploadDir(String inTmpUploadDir) {
        //this.tmpUploadDir = inTmpUploadDir;
		TargetDirList.addElement(new TargetDir(inTmpUploadDir, this.expireDuration));
    }
    

    public void addTargetDir(String filePath, long inExpireDuration){
		TargetDirList.addElement(new TargetDir(filePath, inExpireDuration));
    }
    
	public void addTargetDir(String filePath){
		TargetDirList.addElement(new TargetDir(filePath, this.expireDuration));
	}

    
    public TempDirCleaner() {
        wizbini = WizbiniLoader.getInstance();
        logger = Logger.getLogger(MessageScheduler.class);
        static_env = qdbAction.static_env;
    }
    
    public void init() {
		if (this.param != null) {
			for (int i = 0; i < this.param.size(); i++) {
				ParamType paramType = (ParamType) this.param.get(i);
				if (paramType.getName().equals("temp_file_time_to_live")) {
					setExpireDuration(Long.valueOf(paramType.getValue()).longValue());
				}
			}
		}
		setResTmpUploadDir(static_env.INI_DIR_UPLOAD_TMP);
		setItmTmpUploadDir(static_env.INI_ITM_DIR_UPLOAD_TMP);
		setTmpUploadDir(wizbini.getFileUploadTmpDirAbs());
	}

	public void process() {
        cleanTempDir();
	}
    

    public void cleanTempDir(File fileTempDir, long inExpireDuration) {
        if (fileTempDir.exists()) {
            File[] fileChildDir = fileTempDir.listFiles();
            for (int i=0; fileChildDir != null && i < fileChildDir.length; i++) {
                cleanDir(fileChildDir[i], inExpireDuration);
            }
        }
    }
    
    public void cleanTempDir(){
    	
    	for(int i=0; i<this.TargetDirList.size(); i++ ){
    		TargetDir targetDir = (TargetDir) this.TargetDirList.elementAt(i);
    		logger.debug("TempDirCleaner.cleanTempDir() _ " + targetDir.filePath);
    		cleanTempDir(new File(targetDir.filePath), targetDir.fileExpireDuration);
    	}
    	return;
    	
    }
    
    public boolean cleanDir(File targetDir, long inExpireDuration) {
        if (targetDir.isFile()) {
            if (isExpire(targetDir, inExpireDuration)) {
                targetDir.delete();
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (isExpire(targetDir, inExpireDuration)) {
                boolean isDirExpire = true;
                File[] fileChildDir = targetDir.listFiles();
                for (int i=0; fileChildDir != null && i < fileChildDir.length; i++) {
                    // keep on cleaning other child folders even there are still some files/fodler not expire yet
                    if (cleanDir(fileChildDir[i], inExpireDuration) == false) {
                        isDirExpire = false;
                    }
                }
                
                // delete the folder if all child folders/files have been deleted
                if (isDirExpire) {
                    targetDir.delete();
                }
                else {
                    // do nothing
                }
                
                return isDirExpire;
            }
            else {
                return false;
            }
        }
    }
    
    public boolean isExpire(File myFile, long inExpireDuration) {
        long modifiedTime = -1;
        long currTime = Calendar.getInstance().getTime().getTime();
        
        modifiedTime = myFile.lastModified();
        // the file expires and is ready to be deleted
        if (currTime - modifiedTime > inExpireDuration) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String,Object> params = jobExecutionContext.getMergedJobDataMap();
        if(param == null){
            param = new ArrayList();
        }
        for (String key : params.keySet()){
            ParamType paramType = new ParamTypeImpl();
            paramType.setName(key);
            paramType.setValue(params.get(key).toString());
            param.add(paramType);
        }
        init();
        process();
    }


    private class TargetDir{
    	public String filePath;
    	public long fileExpireDuration;
    	public TargetDir(String path, long inExpireDuration){
    		this.filePath = path;
    		this.fileExpireDuration = inExpireDuration;
    		return;
    	}
    }
    
}