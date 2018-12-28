package com.cw.wizbank.search;

import java.util.Calendar;
import java.util.Vector;

import com.cwn.wizbank.utils.CommonLog;

public class IndexFilesScheduler {
    static String ADD = "ADD";
    static String REMOVE = "REMOVE";
    
    // default value
    int schedulerSleepDuration = 5000;
            
    Vector vtIndexFiles = new Vector();
    IndexFiles myIndexFiles = null;

    public IndexFilesScheduler() {
    }
    
    public IndexFilesScheduler(int schedulerSleepDuration) {
        this.schedulerSleepDuration = schedulerSleepDuration*1000;
    }
    
    public void addIndex(IndexFiles myIndexFiles) {
        myIndexFiles.action = IndexFiles.ADD;
        vtIndexFiles.addElement(myIndexFiles);
    }
    
    public void removeIndex(IndexFiles myIndexFiles) {
        myIndexFiles.action = IndexFiles.REMOVE;
        vtIndexFiles.addElement(myIndexFiles);
    }
    
    public void finishIndex(int index) {
        vtIndexFiles.remove(index);
    }
    
    public void startScheduler() {
        ThreadBuildIndex myThreadBuildIndex = new ThreadBuildIndex();
        myThreadBuildIndex.setPriority(Thread.MIN_PRIORITY);
        myThreadBuildIndex.start();
    }

    public class ThreadBuildIndex extends Thread {
        public ThreadBuildIndex() {
        }
        
        public void run() {
            while (true) {
                if (vtIndexFiles.size() > 0) {
                    IndexFiles myIndexFiles = (IndexFiles)vtIndexFiles.elementAt(0);
                    if (myIndexFiles.action.equalsIgnoreCase(IndexFiles.ADD)) {
                        try {
CommonLog.info("start building index .......");                            
                            myIndexFiles.addIndex();
CommonLog.info("finish building index .......");                            
                        } catch(Exception e) {
                        	CommonLog.error(e.getMessage(),e);
                        }
                    }
                    else {
                        try {
CommonLog.info("start removing index .......");                            
                            myIndexFiles.removeIndex();
CommonLog.info("finish removing index .......");                            
                        } catch(Exception e) {
                        	CommonLog.error(e.getMessage(),e);
                        }
                    }
                    finishIndex(0);
                }
                else {
                    try {
                        Thread.sleep(schedulerSleepDuration);
                    } catch (Exception e) {
                    	CommonLog.error(e.getMessage(),e);
                    }
                }
            }
        }
        
    }
    
    // usage: IndexFilesScheduler index-path file . . . 
    public static void main(String[] args) throws Exception {
        IndexFilesScheduler myIndexFilesScheduler = new IndexFilesScheduler();
        myIndexFilesScheduler.startScheduler();
        
        String[] docPath = new String[args.length-1];
        
        // testing purpose only
        String[] objID = new String[args.length-1];
        String[] verNum = new String[args.length-1];
        String[] filename = new String[args.length-1];
        String[] workFolderPath = new String[args.length-1];
        String[] workFolderID = new String[args.length-1];
        String[][] domainPath = new String[args.length-1][2];
        String[][] domainID = new String[args.length-1][2];
        String[] title = new String[args.length-1];
        String[] description = new String[args.length-1];
        String[] keyword = new String[args.length-1];
        String[] typeID = new String[args.length-1];
        String[] author = new String[args.length-1];
        String[] modifiedDate = new String[args.length-1];
        
        for (int i=1; i<args.length; i++) {        
            docPath[i-1] = args[i];
            
            // testing purpose only
            objID[i-1] = Integer.toString(i);
            verNum[i-1] = "Ver. " + Integer.toString(i);
            filename[i-1] = docPath[i-1];
            //workFolderPath[i-1] = "Workfolder Path " + Integer.toString(i);
            //workFolderID[i-1] = "Workfolder ID " + Integer.toString(i);
            title[i-1] = "Title " + Integer.toString(i);
            description[i-1] = "Desc " + Integer.toString(i);
            keyword[i-1] = "Keyword " + Integer.toString(i);
            typeID[i-1] = "DOC";
            author[i-1] = "Author " + Integer.toString(i);
            modifiedDate[i-1] = Long.toString(Calendar.getInstance().getTime().getTime());
            domainID[i-1][0] = "1";
            domainID[i-1][1] = Integer.toString(i);            
        }
        
        for (int i=0; i<args.length-1; i++) {
            IndexFiles myIndexFiles = new IndexFiles(args[0]);
            
            // set the key
            myIndexFiles.setKey(docPath[i]);
            
            myIndexFiles.setPath(docPath[i]);
            
            // testing purpose only
            myIndexFiles.setObjID(objID[i]);
            myIndexFiles.setVersion(verNum[i]);
            myIndexFiles.setFilename(filename[i]);
            myIndexFiles.setKeyword(keyword[i]);
            myIndexFiles.setDomain(domainID[i]);
            myIndexFiles.setTitle(title[i]);
            myIndexFiles.setDescription(description[i]);
            myIndexFiles.setTypeID(typeID[i]);
            myIndexFiles.setAuthor(author[i]);
            myIndexFiles.setModifiedDate(modifiedDate[i]);
                
            myIndexFiles.setEncoding("Big5");
            
            if (i==2) {
                myIndexFilesScheduler.removeIndex(myIndexFiles);
            }
            else {
                myIndexFilesScheduler.addIndex(myIndexFiles);
            }
            
/*            
            try {
                Thread.sleep(10000);
            } catch(Exception e) {
            }
            */
        }
    }
}