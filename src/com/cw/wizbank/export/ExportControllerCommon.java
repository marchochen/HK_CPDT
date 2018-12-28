/*
 * Created on 2005-9-16
 */
package com.cw.wizbank.export;

import com.cw.wizbank.util.cwUtils;

/**
 * @author dixson
 */
public class ExportControllerCommon {
    private int currentRow = 0;
    private int totalRow = -1;
    private boolean stopFlag = false;
    private String downloadFilePath = null;
    private String xlsFileXls = null;
    private String xlsFileZip = null;
    private String errorMsg = null;
    public static String CONTROLLER_NAME = "export_controller";

    public void next() {
        this.currentRow++;
    }

    public boolean isCancelled() {
        return stopFlag;
    }

    public void cancel() {
        this.stopFlag = true;
    }

    public String getProgress() {
        StringBuffer pos = new StringBuffer();
        int progressValue = 0;
        if (totalRow > 0) {
            progressValue = (int) ((float)currentRow / (float)totalRow * 100);
        }
        else
        if (totalRow == 0) {
            progressValue = -1;
        }
        pos.append("<data>").append("<progress>").append(progressValue).append("</progress>");
        if (progressValue >= 100 && downloadFilePath != null) {
            pos.append("<file_path>").append(downloadFilePath).append("</file_path>");
        }
        if (errorMsg != null) {
            pos.append("<error>").append(cwUtils.esc4XML(errorMsg)).append("</error>");
        }
        pos.append("</data>");
        return pos.toString();
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public void setFile(String inFile) {
        this.downloadFilePath = inFile;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
