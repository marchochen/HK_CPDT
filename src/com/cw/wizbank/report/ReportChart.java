/**
 * @date: 2007-07-30
 */
package com.cw.wizbank.report;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;


/**
 * @author jackyx
 *
 */
public class ReportChart {

	private static final long PiePlotImgWidth = 622;
	
	private static final long PiePlotImgHeight = 350;
	
	private static final float PiePlotBackgroundAlpha = 0.5f;
	
	private static final float PiePlotForegroundAlpha = 0.9f;
	
	private static final double PiePlotMaximumLabelWidth = 0.41;
	
	private static final double PiePlotMinimumArcAngleToDraw = 0.58;
	
	private static final double PiePlotInteriorGap = 0.15;
	
	private static final String PiePlotImgFileName = ".PNG";
		
	private static final String PiePlotNoDataMessage = "No data available";
	
	/*private static final Font ChartTitleFont = new Font("SansSerif", Font.BOLD, 14);
	
	private static final Font PiePlotLabelFont = new Font("SansSerif", Font.PLAIN, 12);*/
	
	private static final Font ChartTitleFont = getFont(Font.BOLD, 14);
	
	private static final Font PiePlotLabelFont = getFont(Font.PLAIN, 12);
	
	private static final Color BackgroundPaint = Color.white;
	
	private static final StandardPieSectionLabelGenerator PiePlotLabelSectionFormat = new StandardPieSectionLabelGenerator("{0}:{1}({2})", new DecimalFormat("#0"), new DecimalFormat("0.0%"));;
	
	public class PiePlotData{
		public String label;
		public double value;
		public int pie_ats_id;
	}
	
	
	   private static File fontfile = null;

	   private static void initFontFile() {
			if (fontfile == null) {
				String vPath = ".."+cwUtils.SLASH+"wb_image" + cwUtils.SLASH + "fonts" + File.separator + "simhei.ttf";
				fontfile = new File(vPath);
				CommonLog.debug(fontfile.getAbsolutePath());
			}
		}

		public static Font getFont(int style, int size) {
			Font defFont = new Font("黑体", style, size);
			try {
				initFontFile();
				if (fontfile == null || !fontfile.exists()) {
					return defFont;
				}
				java.io.FileInputStream fi = new java.io.FileInputStream(fontfile);
				Font nf = Font.createFont(Font.TRUETYPE_FONT, fi);
				fi.close();
				// 这一句需要注意
				// Font.deriveFont() 方法用来创建一个新的字体对象
				nf = nf.deriveFont(style, size);
				return nf;
			} catch (Exception e) {
			}
			return defFont;
		}

	
	
	//for testing
	public static void main(String[] args) throws IOException {
		Hashtable hash = new Hashtable();
		Vector vec = new Vector();
		ReportChart rc = new ReportChart();
		PiePlotData jdata = rc.new PiePlotData();
		jdata.label ="Enrolled:9825000";
		jdata.value = 0;
		vec.add(jdata);
		PiePlotData jdata2 = rc.new PiePlotData();
		jdata2.label ="Completed:8521000";
		jdata2.value = 0;
		vec.add(jdata2);
		PiePlotData jdata3 = rc.new PiePlotData();
		jdata3.label ="Incomplete:84521200";
		jdata3.value = 0;
		vec.add(jdata3);
		PiePlotData jdata4 = rc.new PiePlotData();
		jdata4.label ="Withdrawn:2144100";
		jdata4.value = 0;
		vec.add(jdata4);
		rc.create("D:", "test123", vec);
	}
	
	private PieDataset createDataset(List chartdata) {
		DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
		if (chartdata != null) {
			for (int i = 0; i < chartdata.size(); i++) {
				PiePlotData piedata = (PiePlotData) chartdata.get(i);
				if (piedata != null) {
					defaultpiedataset.setValue(piedata.label, new Double(piedata.value));
				}
			}
		}
		return defaultpiedataset;
	}
	
	private void setPaintColor(PiePlot pie, List data){
		if(data != null){
			for(int i=0; i< data.size(); i++) {
				PiePlotData pieData = (PiePlotData)data.get(i);
				if(pieData != null) {
					pie.setSectionPaint(pieData.label, getPaintColor(pieData.pie_ats_id));
				}
			}
		}
	}
	
	private PiePlot createPielot(JFreeChart chart, List pieData){
		PiePlot pie = (PiePlot)chart.getPlot();
		setPaintColor(pie, pieData);
		pie.setIgnoreZeroValues(true);
		pie.setLabelFont(PiePlotLabelFont);
	    StandardPieSectionLabelGenerator slbl = PiePlotLabelSectionFormat; 
	    pie.setLabelGenerator(slbl); 
		pie.setLegendLabelGenerator(slbl); 
        pie.setCircular(true);
		pie.setBackgroundPaint(BackgroundPaint);
		pie.setNoDataMessage(PiePlotNoDataMessage); 
		pie.setMaximumLabelWidth(PiePlotMaximumLabelWidth);
		pie.setMinimumArcAngleToDraw(PiePlotMinimumArcAngleToDraw);
		pie.setInteriorGap(PiePlotInteriorGap);
		pie.setBackgroundAlpha(PiePlotBackgroundAlpha);
		pie.setForegroundAlpha(PiePlotForegroundAlpha);
		
		return pie;
	}
	
    public String create(String chartImgSavedPath, String chartTitle, List chartData) throws IOException {
		
		JFreeChart chart = ChartFactory.createPieChart(chartTitle, createDataset(chartData), false,true, false);
		chart.setTitle(new TextTitle(chartTitle, ChartTitleFont));
		chart.setAntiAlias(true);
		chart.setTextAntiAlias(true);
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
//		chart.setBackgroundPaint(Color.white);
		PiePlot pie = createPielot(chart, chartData);
		
		String rfname = System.currentTimeMillis() + PiePlotImgFileName;
		String fileName =  chartImgSavedPath + cwUtils.SLASH  + rfname;
		ChartUtilities.saveChartAsPNG(new File(fileName), chart, 622, 350);

		return rfname;
        
    }
    public Color getPaintColor(int ats_id) {
    	Color color_id = null;
    	switch(ats_id) {
    		case 1 :
				color_id = Color.green;
				break;
			case 2 :
				color_id = Color.blue;
				break;
			case 3 :
				color_id = Color.red;
				break;
			case 4 :
				color_id = Color.yellow;
				break;
			default :
				color_id = Color.orange;
    	}
    	return color_id;
    }
}
