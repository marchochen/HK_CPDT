/*
 * Created on 2005-6-22
 *
 */
package com.cw.wizbank.export;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * @author dixson
 *
 */
public abstract class ExportHelper {
    static String OutputFileName;
    static String SheetName;
    static final String ZIP_FILE = "zip";
    static final String XLS_FILE = "xls";

    static final short ColumnA = 0;
    static final short ColumnB = 1;
    static final short ColumnC = 2;
    static final short ColumnD = 3;
    static final short ColumnE = 4;
    static final short ColumnF = 5;
    static final short ColumnG = 6;
    static final short ColumnH = 7;
    static final short ColumnI = 8;
    static final short ColumnJ = 9;

    public static final int STYLE_TITLE_WITH_FILLBACK = 1;
    public static final int STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT = 2;
    public static final int STYLE_QUESTION_AND_ANSWER = 4;
    public static final int STYLE_TITLE_TEXT = 5;
    public static int[] columnWidth;
    
    public static int SHEET_POSTION_FIRST = 0;
    public static int SHEET_POSTION_SECOND = 1;
    public static final int ERROR_CODE_SC_SUBQUE_OVERLOAD = 11;

    // physical file path in file system
    String fsFilePath;
    // file path for web access
    String webFilePath;
    //jxl object
    WritableWorkbook wb;
    WritableSheet sheet;
    WritableSheet errorSheet;
    WorkbookSettings wbSetting;
    int fileCount = 0;
    int nowRowNum = -1;
    int process_unit = 0;
    int errorRowNum = -1;
    
    public ExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String InOutputFileName, String InSheetName) throws WriteException, IOException {
        // web uri for downloading the output file
        OutputFileName = InOutputFileName;
        SheetName = InSheetName;
        webFilePath = "../" + tempDirName + "/" + winName + "/" + OutputFileName;
        // physical file path for writing the output file
        fsFilePath = tempDir + cwUtils.SLASH + winName;
        File xlsFile = new File(fsFilePath);
        if (!xlsFile.exists()) {
            xlsFile.mkdirs();
        }
        // maximum number of rows per output file
        this.process_unit = processUnit;
        
        getNewFile();
        wbSetting = new WorkbookSettings();
        wbSetting.setEncoding(encoding);
        wbSetting.setGCDisabled(false);
    }
    
    public void changeFileDetail (String OutputFileNameNew, String SheetNameNew) {
        OutputFileName = OutputFileNameNew;
        SheetName = SheetNameNew;
    }


    /**
     * return cellFormat by cellFormat name
     */
    public WritableCellFormat getStyleByName(int cellFormatName) throws WriteException {
        WritableCellFormat cf = null;
        switch (cellFormatName) {
            case STYLE_TITLE_WITH_FILLBACK :
                WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10);
                cf = new WritableCellFormat(arial10font);
                cf.setBackground(jxl.format.Colour.GRAY_25);
                break;

            case STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT :
                cf = new WritableCellFormat();
                cf.setAlignment(Alignment.RIGHT);
                cf.setVerticalAlignment(VerticalAlignment.TOP);
                break;

            case STYLE_QUESTION_AND_ANSWER :
                cf = new WritableCellFormat();
                cf.setAlignment(Alignment.LEFT);
                cf.setVerticalAlignment(VerticalAlignment.TOP);
                cf.setWrap(true);
                break;

            case STYLE_TITLE_TEXT :
                WritableFont font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
                cf = new WritableCellFormat(font);
                cf.setBackground(jxl.format.Colour.GRAY_25);
                break;
        }
        return cf;
    }

    /**
     * write a TitleText Style row
     */
    void setTitleText(String title) throws WriteException, IOException {
        getNewRow();
        WritableFont arial10Font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        WritableCellFormat cf = new WritableCellFormat(arial10Font);
        Label label = new Label(ColumnA, nowRowNum, title, cf);
        sheet.addCell(label);
    }

    /**
     * write a workbook to a xls file
     */
    public void writeFile() throws WriteException, IOException {
        wb.write();
        wb.close();
    }

    public String finalizeFile() throws WriteException, qdbErrMessage, IOException {
        String result = null;
        writeFile();
        if (fileCount > 1) {
            dbUtils.makeZip(fsFilePath + cwUtils.SLASH + OutputFileName + "." + ZIP_FILE
                          , fsFilePath
                          , (new File(fsFilePath)).list()
                          , false
                          );
            result = webFilePath + "." + ZIP_FILE;
        }
        else {
            result = webFilePath + "." + XLS_FILE;
        }
        return result;
    }

    /**
     * set cell content
     */
    public void setCellContent(String content, short cellNum) throws RowsExceededException, WriteException {
        Label label = new Label(cellNum, nowRowNum, content);
        sheet.addCell(label);
    }

    public void setCellContent(double content, short cellNum) throws RowsExceededException, WriteException {
        jxl.write.Number num = new jxl.write.Number(cellNum, nowRowNum, content);
        sheet.addCell(num);
    }

    public void setCellContent(int content, short cellNum) throws RowsExceededException, WriteException {
        jxl.write.Number num = new jxl.write.Number(cellNum, nowRowNum, content);
        sheet.addCell(num);
    }

    //set cell content with cellFormat
    public void setCellContent(String content, short cellNum, WritableCellFormat cf) throws RowsExceededException, WriteException {
        Label label = new Label(cellNum, nowRowNum, content, cf);
        sheet.addCell(label);
    }

    public void setCellContent(double content, short cellNum, WritableCellFormat cf) throws RowsExceededException, WriteException {
        jxl.write.Number num = new jxl.write.Number(cellNum, nowRowNum, content, cf);
        sheet.addCell(num);
    }

    public void setCellContent(int content, short cellNum, WritableCellFormat cf) throws RowsExceededException, WriteException {
        jxl.write.Number num = new jxl.write.Number(cellNum, nowRowNum, content, cf);
        sheet.addCell(num);
    }
    
    public void setCellContent(Timestamp content, short cellNum) throws RowsExceededException, WriteException {
        if (content != null) {
            java.util.Calendar calObj = java.util.Calendar.getInstance();
            calObj.setTime(content);
            calObj.set(java.util.Calendar.ZONE_OFFSET, 0);
            jxl.write.DateTime data = new jxl.write.DateTime(cellNum, nowRowNum, calObj.getTime());
            sheet.addCell(data);
        }
    }

    /**
     * add a new row and init it
     */
    public void getNewRow() throws WriteException, IOException {
        if (nowRowNum == process_unit - 1) {
            getNewFile();
            nowRowNum = -1;
            errorRowNum = -1;
        }
        nowRowNum++;
    }

    /**
     * if report rows > fileRowLimit,get new file to write
     */
    protected void getNewFile() throws IOException, WriteException {
        String filename = null;
        if (fileCount > 0) {
            filename = fsFilePath + cwUtils.SLASH + OutputFileName + fileCount + "." + XLS_FILE;
            writeFile();
        } else {
            filename = fsFilePath + cwUtils.SLASH + OutputFileName + "." + XLS_FILE;
        }
        sheet = null;
        errorSheet = null;
        wb = null;
        wb = Workbook.createWorkbook(new File(filename));
        sheet = wb.createSheet(SheetName, SHEET_POSTION_FIRST);
        fileCount++;
    }
}
