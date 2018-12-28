package com.cw.wizbank.cert;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import Word.Style;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class CertPDF {
	private WizbiniLoader wizbini = null;

	public CertPDF(WizbiniLoader wizbini) {
		this.wizbini = wizbini;
	}

	public void CertTemplate(OutputStream os, HttpServletResponse response, String bg_url,
			HashMap<String, String> outpdf, boolean isDownload) throws DocumentException,
			IOException {
		try {
			int width = 900;
			int height = 650;
			File file = new File(bg_url);
			if (file.exists()) {
				BufferedImage Bi = ImageIO.read(file);
				width = Bi.getWidth();
				height = Bi.getHeight();
			}
			Rectangle pSize = new Rectangle(width, height);
			Document doc = new Document(pSize, 0, 0, 0, 0);
			PdfWriter.getInstance(doc, os);
			String font = this.wizbini.getWebInfRoot() + cwUtils.SLASH + "fonts" + cwUtils.SLASH
					+ "SURSONG.TTF";
			BaseFont bfChinese = BaseFont.createFont(font, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font Fontcerttitle = new Font(bfChinese, 18, Font.BOLD);//
			Font FontChinese = new Font(bfChinese, 20, Font.BOLD);//
			Font font_state = new Font(bfChinese, 22, Font.ITALIC);// 状态栏(小字体风格)
			Font font_date = new Font(bfChinese, 22, Font.BOLD);// 日期栏
			Font font_core = new Font(bfChinese, 12, Font.BOLD);// 
			Font font_content = new Font(bfChinese, 14, Font.BOLD);
			font_content.setStyle(Style.clsid);
			doc.open();
			Image image = Image.getInstance(bg_url);
			image.setAlignment(Image.UNDERLYING);
			
			// image.scalePercent(65);
			doc.add(image);
			// 证书编号
			Paragraph context1 = null;
			// 证书名称
			Paragraph context2 = null;

			if (isDownload) {
				// 证书编号
				context1 = new Paragraph(outpdf.get("certcore"),font_core);//导出证书需要带日期的可以用这段代码：new Paragraph(outpdf.get("certcore") + outpdf.get("certcoredate")
				// 证书名称
				context2 = new Paragraph(outpdf.get("certtitle"), Fontcerttitle);
			} else {
				// 证书编号
				context1 = new Paragraph(outpdf.get("certcore"), font_core);
				// 证书名称
				context2 = new Paragraph(outpdf.get("certtitle"), Fontcerttitle);

			}

			// 证书编号
			context1.setSpacingBefore(60); // 离上一段落空的行数
			context1.setAlignment(Element.ALIGN_RIGHT);
			context1.setIndentationRight(70); // 距离右边的距离
			doc.add(context1);

			// 证书名称
			context2.setAlignment(Element.ALIGN_CENTER);
			context2.setSpacingBefore(60); // 离上一段落空的行数
			context2.setIndentationLeft(70);
			context2.setIndentationRight(70);
			doc.add(context2);

			// 内容
			String note = outpdf.get("note");
			Paragraph contextnote = new Paragraph(note.substring(0, note.length()), font_content);
			contextnote.setAlignment(Element.ALIGN_CENTER);
			contextnote.setSpacingBefore(50);		//上边空白距离   	padding-top
			contextnote.setIndentationLeft(60);		//左边空白距离 	padding-left
			contextnote.setIndentationRight(60);	//右边空白距离	padding-right
			doc.add(contextnote);
			
			// 学员姓名
			Paragraph context = new Paragraph(outpdf.get("participantName"), FontChinese);
			context.setAlignment(Element.ALIGN_CENTER);
			context.setExtraParagraphSpace(30);
			context.setIndentationLeft(60);		//左边空白距离 	padding-left
			context.setIndentationRight(60);	//右边空白距离	padding-right
			context.setSpacingBefore(15);// 离上一段落空的行数
			doc.add(context);
			
			String note2 = outpdf.get("note2");
			Paragraph contextnote2 = new Paragraph(note2.substring(0, note2.length()), font_content);
			contextnote2.setAlignment(Element.ALIGN_CENTER);
			contextnote2.setSpacingBefore(15);		//上边空白距离   	padding-top
			contextnote2.setIndentationLeft(60);		//左边空白距离 	padding-left
			contextnote2.setIndentationRight(60);	//右边空白距离	padding-right
			doc.add(contextnote2);
			
			// 课程名称
			Paragraph course_context = new Paragraph(outpdf.get("courseTitle"), FontChinese);
			course_context.setAlignment(Element.ALIGN_CENTER);
			course_context.setExtraParagraphSpace(30);
			course_context.setIndentationLeft(60);		//左边空白距离 	padding-left
			course_context.setIndentationRight(60);	//右边空白距离	padding-right
			course_context.setSpacingBefore(15);// 离上一段落空的行数
			doc.add(course_context);
			
			String note3 = outpdf.get("note3");
			Paragraph contextnote3 = new Paragraph(note3.substring(0, note3.length()), font_content);
			contextnote3.setAlignment(Element.ALIGN_CENTER);
			contextnote3.setSpacingBefore(15);		//上边空白距离   	padding-top
			contextnote3.setIndentationLeft(60);		//左边空白距离 	padding-left
			contextnote3.setIndentationRight(60);	//右边空白距离	padding-right
			doc.add(contextnote3);
			
			// 日期名称
			Paragraph date_context = new Paragraph(outpdf.get("dateText"), FontChinese);
			date_context.setAlignment(Element.ALIGN_CENTER);
			date_context.setExtraParagraphSpace(30);
			date_context.setIndentationLeft(60);		//左边空白距离 	padding-left
			date_context.setIndentationRight(60);	//右边空白距离	padding-right
			date_context.setSpacingBefore(15);// 离上一段落空的行数
			doc.add(date_context);

			doc.close();

		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}
	}
}
