package com.cw.wizbank.report;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.imageio.ImageIO;

import jxl.CellView;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;




import com.cw.wizbank.ae.aeItemLesson;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.report.ReportExporter.SpecData;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.QRCodeEvents;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import com.google.zxing.WriterException;

public class aeItemLessonExportXls extends ExportHelper  {
	
	public aeItemLessonExportXls(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String fileName, String url) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit, fileName, url);
    } 
	
	public  void outputReport(SpecData specData, Vector<aeItemLesson> vec, String rpt_title, ExportController controller, loginProfile prof) throws SQLException, FileNotFoundException, qdbErrMessage, IOException, cwException {
		try {
			columnWidth = new int[] { 3000, 3000, 3000, 10000, 3000, 3000, 5000, 3000 };
			setColumnWidth(columnWidth);
			setRptTitle1(rpt_title+" -- " + LangLabel.getValue(prof.cur_lan, "1158"));
			setRptTitle2(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_001"));
			setColmun(specData, vec);
			
			QRCodeEvents qr = new QRCodeEvents();
			qr.GenerateQR(specData.tempDir+cwUtils.SLASH+specData.window_name, String.valueOf(specData.ils_id), specData.window_name+".png");
			setImage(vec.size()+4, specData.tempDir+cwUtils.SLASH+specData.window_name+cwUtils.SLASH+specData.window_name+".png");
			
		} catch (RowsExceededException e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException(e.getMessage());
		} catch (WriteException e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException(e.getMessage());
		} catch (WriterException e) {
			CommonLog.error(e.getMessage(),e);
		}
	}
	
	public void setColumnWidth(int[] columnWidth) {
        CellView cv = new CellView();
        for (int i = 0; i < columnWidth.length; i++) {
            cv.setSize(columnWidth[i]);
            sheet.setColumnView(i, cv);
        }
    }
	
	public void setRptTitle1(String title)
		throws cwException, RowsExceededException, WriteException {
		getNewRow();
		WritableFont arial10Font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
	    WritableCellFormat cf = new WritableCellFormat(arial10Font);
	    Label label = new Label(ColumnA, nowRowNum, title, cf);
	    sheet.addCell(label);
		sheet.mergeCells(ColumnA, nowRowNum, ColumnH, nowRowNum);
	}
	
	public void setImage(long s, String files) throws IOException {
		File file = new File(files);
		// 开始位置
		double picBeginCol = 1;
		double picBeginRow = s;
		// 图片时间的高度，宽度
		double picCellWidth = 0.0;
		double picCellHeight = 0.0;
		// 读入图片
		BufferedImage picImage = ImageIO.read(file);
		// 取得图片的像素高度，宽度
		int picWidth = picImage.getWidth();
		int picHeight = picImage.getHeight();

		// 计算图片的实际宽度
		int picWidth_t = picWidth * 32; // 具体的实验值，原理不清楚。
		for (int x = 0; x < 1234; x++) {
			int bc = (int) Math.floor(picBeginCol + x);
			// 得到单元格的宽度
			int v = sheet.getColumnView(bc).getSize();
			double offset0_t = 0.0;
			if (0 == x)
				offset0_t = (picBeginCol - bc) * v;
			if (0.0 + offset0_t + picWidth_t > v) {
				// 剩余宽度超过一个单元格的宽度
				double ratio_t = 1.0;
				if (0 == x) {
					ratio_t = (0.0 + v - offset0_t) / v;
				}
				picCellWidth += ratio_t;
				picWidth_t -= (int) (0.0 + v - offset0_t);
			} else { // 剩余宽度不足一个单元格的宽度
				double ratio_r = 0.0;
				if (v != 0)
					ratio_r = (0.0 + picWidth_t) / v;
				picCellWidth += ratio_r;
				break;
			}
		}
		// 计算图片的实际高度
		int picHeight_t = picHeight * 15;
		for (int x = 0; x < 1234; x++) {
			int bc = (int) Math.floor(picBeginRow + x);
			// 得到单元格的高度
			int v = sheet.getRowView(bc).getSize();
			double offset0_r = 0.0;
			if (0 == x)
				offset0_r = (picBeginRow - bc) * v;
			if (0.0 + offset0_r + picHeight_t > v) {
				// 剩余高度超过一个单元格的高度
				double ratio_q = 1.0;
				if (0 == x)
					ratio_q = (0.0 + v - offset0_r) / v;
				picCellHeight += ratio_q;
				picHeight_t -= (int) (0.0 + v - offset0_r);
			} else {// 剩余高度不足一个单元格的高度
				double ratio_m = 0.0;
				if (v != 0)
					ratio_m = (0.0 + picHeight_t) / v;
				picCellHeight += ratio_m;
				break;
			}
		}
		// 生成一个图片对象。
		WritableImage image = new WritableImage(picBeginCol, picBeginRow,
				picCellWidth, picCellHeight, file);
		// WritableImage image = new WritableImage(1,s,6,18,file);
		sheet.addImage(image);
	}
	
	public void setRptTitle2(String title)
		throws cwException, RowsExceededException, WriteException {
		getNewRow();
		setCellContent(title, ColumnA);
		sheet.mergeCells(ColumnA, nowRowNum, ColumnH, nowRowNum);
	}
	
	public void setColmun(SpecData specData, Vector<aeItemLesson> vec) throws WriteException{
		getNewRow();
		WritableCellFormat cf = getStyleByName(STYLE_TITLE_WITH_FILLBACK);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_002"), ColumnA, cf);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_003"), ColumnB, cf);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_004"), ColumnC, cf);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_005"), ColumnD, cf);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_006"), ColumnE, cf);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_007"), ColumnF, cf);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_008"), ColumnG, cf);
		setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ils_exp_009"), ColumnH, cf);
		
		for (aeItemLesson aeItemLesson : vec) {
			getNewRow();
			setCellContent(cwUtils.format2simple(aeItemLesson.ils_date), ColumnA);
			setCellContent(cwUtils.format2HHMM(aeItemLesson.ils_start_time), ColumnB);
			setCellContent(cwUtils.format2HHMM(aeItemLesson.ils_end_time), ColumnC);
			setCellContent(aeItemLesson.ils_title, ColumnD);
			if(aeItemLesson.ils_qiandao_chidao_time !=null && aeItemLesson.ils_qiandao == 1)
				setCellContent(cwUtils.format2HHMM(aeItemLesson.ils_qiandao_chidao_time), ColumnE);
			else
				setCellContent("--", ColumnE);
			
			if(aeItemLesson.ils_qiandao_queqin_time !=null && aeItemLesson.ils_qiandao == 1)
				setCellContent(cwUtils.format2HHMM(aeItemLesson.ils_qiandao_queqin_time), ColumnF);
			else
				setCellContent("--", ColumnF);
			
			if(aeItemLesson.ils_qiandao_youxiaoqi_time !=null && aeItemLesson.ils_qiandao == 1)
				setCellContent(cwUtils.format2HHMM(aeItemLesson.ils_qiandao_youxiaoqi_time) + "-" + cwUtils.format2HHMM(aeItemLesson.ils_end_time), ColumnG);
			else
				setCellContent("--", ColumnG);
			
			if(aeItemLesson.insture_name !=null)
				setCellContent(aeItemLesson.insture_name, ColumnH);
			else
				setCellContent("--", ColumnH);
		}
		getNewRow();
		
	}
}
