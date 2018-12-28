/*
 * Created on 2005-6-22
 *
 */
package com.cw.wizbank.report;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormats;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author dixson
 *
 */
public abstract class ExportHelper {
    static final String OutputFileName = "report";
    static final String ZIP_FILE = "zip";
    static final String XLS_FILE = "xls";
    static final String SheetName = "Sheet1";
    static final String NULL_REPLACE = "--";
    static final String UNSPECIFIED = "Unspecified";

    public static final short ColumnA = 0;
    public static final short ColumnB = 1;
    public static final short ColumnC = 2;
    public static final short ColumnD = 3;
    public static final short ColumnE = 4;
    public static final short ColumnF = 5;
    public static final short ColumnG = 6;
    public static final short ColumnH = 7;
    public static final short ColumnI = 8;
    public static final short ColumnJ = 9;

    public static final int STYLE_TITLE_WITH_FILLBACK = 1;
    public static final int STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT = 2;
    public static final int STYLE_QUESTION_AND_ANSWER = 4;
    public static final int STYLE_TITLE_TEXT = 5;
    public static final int STYLE_BOLD_FONT = 6;
    public static final int STYLE_BLUE_FONT = 7;
    public static final int STYLE_FONT_ALIGNMENT_CENTER = 8;
    public static final int STYLE_TITLE_TEXT_ALIGNMENT_CENTER = 9;
    public static int[] columnWidth;
	public static SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
    // physical file path in file system
    String fsFilePath;
    // file path for web access
    String webFilePath;
    //jxl object
    WritableWorkbook wb;
    WritableSheet sheet;
    WorkbookSettings wbSetting;
    int fileCount = 0;
    int nowRowNum = -1;
    int process_unit = 0;
    private String fileName = null;

    public ExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        // web uri for downloading the output file
        webFilePath = "../" + tempDirName + "/" + winName + "/" + OutputFileName;
    	fileName =  OutputFileName;
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

	public ExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String url) throws WriteException, IOException {
		this.init(tempDir, tempDirName, winName, encoding, processUnit, null, url);
	}

	public ExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String custFileName, String url) throws WriteException, IOException {
		this.init(tempDir, tempDirName, winName, encoding, processUnit, custFileName,url);
	}
	
	public void init(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String custFileName,String url) throws WriteException, IOException {
		// web uri for downloading the output file
		url = cwUtils.getFileURL(url);
    	fileName = cwUtils.notEmpty(custFileName) ? custFileName : OutputFileName;
    	webFilePath = url + winName + "/" + this.fileName;
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

            case STYLE_BOLD_FONT :
                WritableFont bold_font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                cf = new WritableCellFormat(bold_font);
                break;
            case STYLE_BLUE_FONT :
                WritableFont blue_font = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD,true,UnderlineStyle.NO_UNDERLINE,Colour.BLUE);
                cf = new WritableCellFormat(blue_font);
                break;

            case STYLE_FONT_ALIGNMENT_CENTER :
                cf = new WritableCellFormat();
                cf.setAlignment(Alignment.CENTRE);
                cf.setVerticalAlignment(VerticalAlignment.TOP);
                break;

            case STYLE_TITLE_TEXT_ALIGNMENT_CENTER :
                WritableFont font_center = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
                cf = new WritableCellFormat(font_center);
                cf.setAlignment(Alignment.CENTRE);
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
        	String[] files = (new File(fsFilePath)).list();
        	Vector files_vec = cwUtils.String2vector(files);
        	if(files_vec.contains(fileName + "." + ZIP_FILE)){
        		files_vec.remove(fileName + "." + ZIP_FILE);
        	}
        	files = cwUtils.vec2strArray(files_vec);
            dbUtils.makeZip(fsFilePath + cwUtils.SLASH + fileName + "." + ZIP_FILE
                          , fsFilePath
                          , files
                          , false
                          );
            result = webFilePath + "." + ZIP_FILE;
        }
        else {
            result = webFilePath + "." + XLS_FILE;
        }
        return result;
    }
	public String finalizePDFFile() throws WriteException, qdbErrMessage, IOException {
		String result = null;
		writeFile();
		String[] files = (new File(fsFilePath)).list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".pdf")) {
					return true;
				}
				return false;
			}
		});
		Vector files_vec = cwUtils.String2vector(files);
		if (files_vec.contains(fileName + "." + ZIP_FILE)) {
			files_vec.remove(fileName + "." + ZIP_FILE);
		}
		files = cwUtils.vec2strArray(files_vec);
		if(files.length > 0){
			dbUtils.makeZip(fsFilePath + cwUtils.SLASH + fileName + "." + ZIP_FILE, fsFilePath, files, false);
		}
		result = webFilePath + "." + ZIP_FILE;

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

    public void setCellContent(String content, int rowNum, short cellNum) throws RowsExceededException, WriteException {
    	Label label = new Label(cellNum, rowNum, content);
        sheet.addCell(label);
    }

    //input seconds,get total count time count as "H:mm:ss" format
    public void setTimeCountCellContent(float content, short cellNum) throws RowsExceededException, WriteException {
        if (content > 0) {
//            String temp = "1900-01-01 " + dbAiccPath.getTime(content);
//            Timestamp time = Timestamp.valueOf(temp);
//
//            java.util.Calendar calObj = java.util.Calendar.getInstance();
//            calObj.setTime(time);
//            WritableCellFormat cf = new WritableCellFormat(DateFormats.FORMAT11);
//            jxl.write.DateTime data = new jxl.write.DateTime(cellNum, nowRowNum, calObj.getTime(), cf);
        	String hh = (long)content / 60 /60 + "";
        	String mm = (long)content / 60 % 60 + "" ;
        	String ss = (long)content % 60 + "";
        	Label label = new Label(cellNum, nowRowNum, hh + ":" + (mm.length() == 1 ? "0" + mm: mm) + ":" + (ss.length() == 1 ? "0" + ss: ss));
            sheet.addCell(label);
        }
    }

    /**
     * write a blank row and all cell has being merged
     * @param cellNum      the number of cell you want merge in one row.
     */
    public void setBlankRow(int cellNum) throws RowsExceededException, WriteException {
        getNewRow();
        sheet.mergeCells(ColumnA, nowRowNum, cellNum, nowRowNum);
    }

    /**
     * add a new row and init it
     */
    public void getNewRow() {
        if (nowRowNum == process_unit - 1) {
            try {
                getNewFile();
                nowRowNum = -1;
            }
            catch (WriteException e) {
                CommonLog.error(e.getMessage(),e);
            }
            catch (IOException e) {
                CommonLog.error(e.getMessage(),e);
            }
        }
        nowRowNum++;
    }

    /**
     * if report rows > fileRowLimit,get new file to write
     */
    protected void getNewFile() throws IOException, WriteException {
    	String newFileName = null;
		if (fileCount > 0) {
			newFileName = fsFilePath + cwUtils.SLASH + fileName + fileCount + "." + XLS_FILE;
			writeFile();
		} else {
			newFileName = fsFilePath + cwUtils.SLASH + fileName + "." + XLS_FILE;
		}
        sheet = null;
        wb = null;
        wb = Workbook.createWorkbook(new File(newFileName));
        sheet = wb.createSheet(SheetName, 0);
        fileCount++;
    }
    public static String getRptTitle(String lang,String suff_label){
    	String reportTitle = "";
    	String lab_rte = "lab_rte_";
    	if("en-us".equalsIgnoreCase(lang)){
    		lab_rte = "lab_rte_2_";
    	}
        reportTitle = LangLabel.getValue(lang, lab_rte + suff_label);
        return reportTitle;
    }
    
    /**
     * 指定行的高度
     * @param height  指定高度
     * @throws RowsExceededException
     * @throws WriteException
     */
    public void setRowView( int height) throws RowsExceededException, WriteException {
        sheet.setRowView(nowRowNum,height); 
    }
}
