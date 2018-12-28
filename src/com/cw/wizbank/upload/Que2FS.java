package com.cw.wizbank.upload;

import java.io.*;

import com.cw.wizbank.qdb.extendQue;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.utils.CommonLog;

// provide the interface for media files of questions to be uploaded to the file system
// 2000.08.14 wai

public class Que2FS {
    // directories for copying media files
    private String inputDir_;
    private String outputDir_;
    
    Que2FS(String inDir, String outDir) {
        inputDir_  = inDir;
        outputDir_ = outDir;
    }
    
    public void populate(extendQue que) throws cwException {
        // copy any media files
        String destDir = Long.toString(que.id);
        if (que.cont_pic != null)
            putQueFile(que.cont_pic, destDir);
        for (int i = 0; i < que.inter.length; i++) {
            if (que.inter[i].type == null) break;
            for (int j = 0; j < que.inter[i].opt.length; j++) {
                if (que.inter[i].opt[j].cont == null) break;
                if (que.inter[i].opt[j].cont_pic != null)
                    putQueFile(que.inter[i].opt[j].cont_pic, destDir);
            }
        }
    }
    
    // put the specified file to the default destination directory
    private void putQueFile(String inFileName, String outDirName) throws cwException {
        File frFile = new File(inputDir_, inFileName);
        if (!frFile.canRead())
            throw new cwException("file not accessible:" + frFile.getAbsolutePath());
        File toDir = new File(outputDir_, outDirName);
        toDir.mkdir();
        File toFile = new File(toDir, inFileName);
        if (!toDir.canWrite())
            throw new cwException("file not accessible:" + toFile.getAbsolutePath());
        copyFile(frFile, toFile);
    }
    
    // copy a physical file from one location to another
    private void copyFile(File inFile, File outFile) throws cwException {
    	CommonLog.info("copying from " + inFile.getAbsolutePath() + " to " + outFile.getAbsolutePath() + "... ");
        try {
            BufferedInputStream  in  = new BufferedInputStream(new FileInputStream(inFile));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
            int c;
            while ((c = in.read()) != -1)
               out.write(c);
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            throw new cwException("file error:" + e.getMessage());
        } catch (IOException e) {
            throw new cwException("read/write error:" + e.getMessage());
        }
        CommonLog.info("done.");
    }
}