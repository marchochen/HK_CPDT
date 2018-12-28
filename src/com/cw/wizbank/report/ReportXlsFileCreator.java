/*
 * Created on 2005-6-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.report;

import java.io.File;
import java.io.IOException;

import jxl.CellView;
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
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class ReportXlsFileCreator {
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

    static final int STYLE_TITLE_WITH_FILLBACK = 1;
    static final int STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT = 2;
    static final int STYLE_QUESTION_AND_ANSWER = 4;
    static final int STYLE_TITLE_TEXT = 5;

    //output file attrib
    static final String OutputFileName = "report";
    String xlsFilePath;
    int fileRowLimit = 999;
    public static int[] columnWidth;

    //jxl object
    WritableWorkbook wb;
    WritableSheet sheet;
    WorkbookSettings wbSetting;
    int fileCount = 1;
    int nowRowNum = -1;

    public ReportXlsFileCreator() {
    }

    /**
     * return cellFormat by cellFormat name
     */
    WritableCellFormat getStyleByName(int cellFormatName) throws WriteException {
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
    void setTitleText(String title) throws WriteException {
        getNewRow();
        WritableFont arial10Font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        WritableCellFormat cf = new WritableCellFormat(arial10Font);
        cf.setBackground(jxl.format.Colour.GRAY_25);
        Label label = new Label(ColumnA, nowRowNum, title, cf);
        sheet.addCell(label);
        sheet.mergeCells(ColumnA, nowRowNum, ColumnH, nowRowNum);
    }

    /**
     * write a blank row and all cell has being merged
     * @param cellNum      the number of cell you want merge in one row.
     */
    void setBlankRow(int cellNum) throws RowsExceededException, WriteException {
        getNewRow();
        sheet.mergeCells(ColumnA, nowRowNum, cellNum, nowRowNum);
    }

    /**
     * write a workbook to a xls file
     */
    public void writeToXls(boolean closeFile, String path) throws WriteException, IOException {
        //if dir dosen't exite,creat it
        File xlsFile = new File(path);
        if (!xlsFile.exists()) {
            xlsFile.mkdirs();
        }

        //
        setColumnWidth(columnWidth);
        wb.write();
        wb.close();
    }

    /**
     * set cell content
     */
    public void setCellContent(String content, short cellNum) throws RowsExceededException, WriteException  {
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

    /**
     * add a new row and init it
     */
    public void getNewRow() {
        if (nowRowNum == fileRowLimit) {
            try {
                getNewFile(xlsFilePath);
                nowRowNum = -1;
            }
            catch (WriteException e) {
            	CommonLog.error("error : can't write to file.");
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
            	CommonLog.error(e.getMessage(),e);
            }
        }
        nowRowNum++;
    }

    /**
     * if report rows > fileRowLimit,get new file to write
     */
    protected void getNewFile(String filePath) throws IOException, WriteException {
        writeToXls(true, filePath);
        fileCount++;
        wb = null;
        wb = Workbook.createWorkbook(new File(filePath + cwUtils.SLASH + OutputFileName + fileCount + ".xls"));
        sheet = null;
        sheet = wb.createSheet("First Sheet", 0);
    }

    /**
     * @param zipFilePath
     * @throws qdbErrMessage
     * @throws IOException
     * 
     * if report has more than one file,then zip all file to report.zip
     */
    void zipXlsRpt(String zipFilePath) throws qdbErrMessage, IOException {
        File parentFile = new File(zipFilePath);
        File[] files = parentFile.listFiles();

        //zip file name
        String zipFileName = zipFilePath + cwUtils.SLASH + OutputFileName + ".zip";
        String[] filesName = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filesName[i] = files[i].getName();
        }
        dbUtils.makeZip(zipFileName, zipFilePath, filesName, false);
    }

    public void setColumnWidth(int[] columnWidth) {
        CellView cv = new CellView();
        for (int i = 0; i < columnWidth.length; i++) {
            cv.setSize(columnWidth[i]);
            sheet.setColumnView(i, cv);
        }
    }
    
    public WritableCellFormat getCellFormatWithBold(int ps) throws WriteException{
    	WritableFont arial10Font = new WritableFont(WritableFont.ARIAL, ps, WritableFont.BOLD);
        WritableCellFormat cf = new WritableCellFormat(arial10Font);
        return cf;
    }

}
