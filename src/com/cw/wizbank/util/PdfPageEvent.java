package com.cw.wizbank.util;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PdfPageEvent extends PdfPageEventHelper{


	/** A template that will hold the total number of pages. */
	public PdfTemplate tpl;

	/** The font that will be used. */
	public BaseFont helv;

	public void onOpenDocument(PdfWriter writer, Document document) {
		try{
			//创建模板
			tpl = writer.getDirectContent().createTemplate(100, 100);
			helv = BaseFont.createFont("Helvetica",BaseFont.WINANSI,false);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		String text = "Page " + writer.getPageNumber() + " of ";
		float textSize = helv.getWidthPoint(text, 9);
		float textBase = document.bottom();
		cb.beginText();
		cb.setFontAndSize(helv, 9);
		
		cb.setTextMatrix(document.left(), textBase);
		cb.newlineShowText(text);
		cb.endText();
		cb.addTemplate(tpl, document.left()+textSize, textBase);
	      
	}

	public void onStartPage(PdfWriter writer, Document document) {

	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		tpl.beginText();
		tpl.setFontAndSize(helv, 9);
		tpl.setTextMatrix(0, 0);
		tpl.newlineShowText(""+(writer.getPageNumber()-1));
		tpl.endText();
	}
}
