package com.cw.wizbank.util;

public class UploadListener implements OutputStreamListener {
    public static final String STATUS_START = "start";

    public static final String STATUS_UPLOADING = "uploading";

    public static final String STATUS_ERROR = "error";

    public static final String STATUS_UPLOADED = "uploaded";

    public static final String STATUS_FINISH = "finish";

    public static final String STATUS_NONE = "none";

    // 保存状态的内部类对象
    private FileUploadStats fileUploadStats = new FileUploadStats();

    // 构造方法
    public UploadListener(long totalSize) {
        fileUploadStats.setTotalSize(totalSize);
    }

    public void start() {
        // 设置当前状态为开始
        fileUploadStats.setCurrentStatus(STATUS_START);
    }

    public void bytesRead(int byteCount) {
        // 将已读取的数据保存到状态对象中
        fileUploadStats.incrementBytesRead(byteCount);
        // 设置当前的状态为读取过程中
        fileUploadStats.setCurrentStatus(STATUS_UPLOADING);
    }

    public void error(String s) {
        // 设置当前状态为出错
        fileUploadStats.setCurrentStatus(STATUS_ERROR);
        fileUploadStats.setReturnMsg(s);
    }

    public void uploaded() {
        // 设置当前已读取数据等于总数据大小
        fileUploadStats.setBytesRead(fileUploadStats.getTotalSize());
        // 设置当前状态为完成
        fileUploadStats.setCurrentStatus(STATUS_UPLOADED);
    }

    public FileUploadStats getFileUploadStats() {
        // 返回当前状态对象
        return fileUploadStats;
    }

    public void finish() {
        // 设置当前状态为完成
        fileUploadStats.setCurrentStatus(STATUS_FINISH);
    }
    
    public void returnMsg(String msgid) {
        fileUploadStats.setReturnMsg(msgid);
    }

    public void setNewResId(long id) {
        fileUploadStats.setNew_res_id(id);
    }
    // 保存状态类
    public static class FileUploadStats {
        private long totalSize = 0;// 总数据的大小

        private long bytesRead = 0;// 已读数据大小

        private long startTime = System.currentTimeMillis();// 开始读取的时间

        private String currentStatus = STATUS_NONE;// 默认的状态

        private String errorMsg = "";

        private long filesInZip = 0;// zip中文件总数

        private long extractedFile = 0;// zip中已解压文件数

        private long elapsedTimeInSecond = 0;
        
        private long uploadSpeed = 0;
        
        private long timeLeft = 0;
        
        private String returnMsg = "";
        private long new_res_id = 0;
        public long getTotalSize()// 属性totalSize的get方法
        {
            return totalSize - 100;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public long getBytesRead()// 属性bytesRead的get方法
        {
            return bytesRead;
        }

        public String getCurrentStatus()// 属性currentStatus的get方法
        {
            return currentStatus;
        }

        public void setCurrentStatus(String currentStatus) {
            this.currentStatus = currentStatus;
        }

        public void setBytesRead(long bytesRead) {
            this.bytesRead = bytesRead;
        }

        public void incrementBytesRead(int byteCount) {
            this.bytesRead += byteCount;
        }

        public long getExtractedFile() {
            return extractedFile;
        }

        public void setExtractedFile(long extractFile) {
            this.extractedFile = extractFile;
        }

        public void incrementExtractFile(int fileCount) {
            this.extractedFile += fileCount;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public long getElapsedTimeInSecond() {
            return elapsedTimeInSecond;
        }

        public void setElapsedTimeInSecond() {
            this.elapsedTimeInSecond = (System.currentTimeMillis() - startTime) / 1000;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getFilesInZip() {
            return filesInZip;
        }

        public void setFilesInZip(long filesInZip) {
            this.filesInZip = filesInZip;
        }

        public long getUploadSpeed() {
            return uploadSpeed;
        }

        public void setUploadSpeed(long uploadSpeed) {
            this.uploadSpeed = uploadSpeed;
        }
        
        public long getTimeLeft() {
            return timeLeft;
        }

        public void setTimeLeft(long timeLeft) {
            this.timeLeft = timeLeft;
        }

        public String getReturnMsg() {
            return returnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        /**
         * Calculate upload speed 
         */
        public void calSpeed() {
            setElapsedTimeInSecond();
            if (STATUS_UPLOADING.equalsIgnoreCase(currentStatus)) {
                this.uploadSpeed = bytesRead > 0 ? (long) Math.round(bytesRead / (getElapsedTimeInSecond() + 0.00001) / 1024 / 8) : 0;
                this.timeLeft = (bytesRead > 0 && uploadSpeed > 0) ? (long) Math.round((totalSize - bytesRead) / (uploadSpeed + 0.00001)) : 0;
            }
        }

        public long getNew_res_id() {
            return new_res_id;
        }

        public void setNew_res_id(long newResId) {
            new_res_id = newResId;
        }
    }
}