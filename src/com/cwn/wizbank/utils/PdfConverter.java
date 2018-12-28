package com.cwn.wizbank.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.cw.wizbank.util.cwUtils;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//import com.sun.pdfview.PDFFile;
//import com.sun.pdfview.PDFPage;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Element;
//import com.lowagie.text.Rectangle;
//import com.lowagie.text.pdf.BaseFont;
//import com.lowagie.text.pdf.PdfContentByte;
//import com.lowagie.text.pdf.PdfGState;
//import com.lowagie.text.pdf.PdfReader;
//import com.lowagie.text.pdf.PdfStamper;


import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import java.awt.image.RenderedImage;

import javax.imageio.ImageIO;
  
/** 
 * office文件转换为PDF 
 */  
public class PdfConverter {  
    private String filePath;  
    public String pdf2jsonPath;
    private File docFile; 
    private File pdfFile;  
    private File jsonFile;
      
    public PdfConverter(String fileString) {  
        ini(fileString);  
    }  
  
    /** 
     * 重新设置file 
     *  
     * @param fileString 
     */  
    public void setFile(String fileString) {  
        ini(fileString);  
    }  
  
    /** 
     * 初始化 
     *  
     * @param fileString 
     */  
    private void ini(String fileString) {
    	filePath = fileString.substring(0, fileString.lastIndexOf("."));  
        docFile = new File(fileString);  
        pdfFile = new File(filePath + ".pdf");
        jsonFile = new File(filePath + ".pdf.js");
    }  
      
    /** 
     * 转为PDF 
     *  
     */  
    public void file2pdf(OpenOfficeConnection connection, String host, int port, Boolean isDel) throws Exception {  
        if (docFile.exists()) {  
            if (!pdfFile.exists()) {  
//                OpenOfficeConnection connection = new SocketOpenOfficeConnection(host, port);  
                try {  
                    connection.connect();  
                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);  
                    converter.convert(docFile, pdfFile); 
                    // close the connection  
                    connection.disconnect();  
//                    PdfReader pdfReader = new PdfReader(pdfFile.getPath());
//                    PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(pdfFile.getPath()));
//                    addWatermark(pdfStamper, "www.360sdn.com");
//                    pdfStamper.close(); 
                    pdf2json();
                    if(isDel){
                    	docFile.delete();
                    }
//                    System.out.println("****pdf转换成功，PDF输出：" + pdfFile.getPath() + "****");  
                } catch (java.net.ConnectException e) {  
                	CommonLog.error("****pdf转换器异常，openoffice服务未启动！****");  
                    throw e;  
                } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {  
                    CommonLog.error("****pdf转换器异常，读取转换文件失败****");  
                    throw e;  
                } catch (Exception e) {  
                	CommonLog.error(e.getMessage(),e);
                    throw e;  
                }  
            } else {  
            	CommonLog.info("****已经转换为pdf，不需要再进行转化****");  
            }  
        } else {  
        	CommonLog.error("****pdf转换器异常，需要转换的文档不存在，无法转换****");  
        }  
    }  
    
    /*public void pdf2jpg_() throws IOException {  
        // load a pdf from a byte buffer  
        @SuppressWarnings("resource")
		RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");  
        FileChannel channel = raf.getChannel();  
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());  
        PDFFile pdffile = new PDFFile(buf);  
  
        for (int i = 1; i <= pdffile.getNumPages(); i++) {  
            // draw the first page to an image  
            PDFPage page = pdffile.getPage(i);  
  
            // get the width and height for the doc at the default zoom  
            java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, (int) page.getBBox().getWidth(), 
            		(int) page.getBBox().getHeight());  
  
            // generate the image  
            Image img = page.getImage(rect.width*2, rect.height*2, rect, null, true, true);  
  
            BufferedImage tag = new BufferedImage(rect.width*2, rect.height*2, BufferedImage.TYPE_INT_RGB);  
            tag.getGraphics().drawImage(img, 0, 0, (rect.width *2), (rect.height*2), null);  
            FileOutputStream out = new FileOutputStream(filePath + ".pdf_" + i + ".jpg"); // 输出到文件流  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
            encoder.encode(tag); // JPEG编码  
  
            out.close();  
        }
    } 
    */
    public void pdf2json() throws IOException {
//    	File pdf2json = new File(pdf2jsonPath);
//    	if(pdf2json.exists()){
//    		String command = "\"" + pdf2jsonPath + "\" \"" + pdfFile.getPath() + "\" -enc UTF-8 -compress \"" + jsonFile.getPath() + "\"";
//    		// 由于在lunix下无法找到转为json的方法，所以使用了其它显示方法，就不需要再转为json
//			//Runtime.getRuntime().exec(command);
//			pdf2jpg();
//    	} else {
//         	System.out.println("****pdf2json转换异常,pdf2json.exe不存在****"); 
//    	}
    	pdf2jpg();
    }
    
    /** 
     * 为pdf加水印
     *  
     * @param pdfStamper
     * @param waterMarkName
     */ 
//	private static void addWatermark(PdfStamper pdfStamper, String waterMarkName) {
//		PdfContentByte content = null;
//		BaseFont base = null;
//		Rectangle pageRect = null;
//		PdfGState gs = new PdfGState();
//		try {
//			// 设置字体
//			base = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			if (base == null || pdfStamper == null) {
//				return;
//			}
//	       	// 设置透明度为0.4
//	     	gs.setFillOpacity(0.4f);
//	    	gs.setStrokeOpacity(0.4f);
//	    	int toPage = pdfStamper.getReader().getNumberOfPages();
//	     	for (int i = 1; i <= toPage; i++) {
//	         	pageRect = pdfStamper.getReader().
//	           	getPageSizeWithRotation(i);
//	          	// 计算水印X,Y坐标
//	         	float x = pageRect.getWidth() / 2;
//	         	float y = pageRect.getHeight() / 2;
//	         	//获得PDF最顶层
//	         	content = pdfStamper.getOverContent(i);
//	         	content.saveState();
//	         	// set Transparency
//	         	content.setGState(gs);
//	         	content.beginText();
//	         	content.setFontAndSize(base, 60);
//	            // 水印文字成45度角倾斜
//	        	content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, x, y, 45);
//	        	content.endText();
//		     }
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			content = null;
//			base = null;
//			pageRect = null;
//		}
//	}
    
    
    
    public  void pdf2jpg()throws IOException
	{     
		Document document = new Document();
		try 
		{
			document.setFile(pdfFile.getAbsolutePath());
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		int pages = document.getNumberOfPages();
//		System.out.println("pages  " + pages);
		List<String> imgPaths = new ArrayList<String>();
		BufferedImage image = null;
		RenderedImage rendImage = null;
		int height = 0;
		int width = 0;
		for( int i = 0 ; i < pages ; i++ )
		{
			image = (BufferedImage)document.getPageImage(i,GraphicsRenderingHints.SCREEN,Page.BOUNDARY_TRIMBOX, 0f, 1.5f);;
			rendImage = image;
//			System.out.println("/t capturing page " + i);
			
			File file = new File(filePath + ".pdf_" + (i+1) + ".jpg");
			
			imgPaths.add("c_"+i+".png");
			
			if( !file.exists() )
			{
				height = image.getHeight();
				width  = image.getWidth();
				try {
					file.createNewFile();
				} 
				catch (IOException e) {
					CommonLog.error(e.getMessage(),e);
				}
				try {
					
					//限制文件转为图片后，图片宽、高不能超过1500
					boolean resize = false;
					while(true){
						if(height >= 1500 || width >= 1500){
							height = (int) (height * 0.9);
							width = (int) (width * 0.9);
							resize = true;
						}else{
							break;
						}
					}
					if(resize){
						rendImage = cwUtils.resize(image, width, height);
					}
					ImageIO.write(rendImage, "jpg", file);
				} 
				catch (IOException e) {
					CommonLog.error(e.getMessage(),e);
				}
				image.flush();
			}
			
			else{
				CommonLog.info("/t capturing page is exist !");
			}
		}
		StringBuffer Json_data = new StringBuffer();
		Json_data.append("[{\"pages\":").append(pages).append(",\"height\":").append(height).append(",\"width\":").append(width).append("}]");
		File js_file = new File(filePath + ".pdf.js");
		if(!js_file.exists()){
			js_file.createNewFile();
		}else{
			js_file.delete();
			js_file.createNewFile();
		}
		writeFile(js_file.getAbsolutePath(), Json_data.toString());
		
		document.dispose();
	}

    public static void writeFile(String filename, String content){
        try{
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), "UTF-8"));
            out.write(content.toCharArray());
            out.newLine();
            out.flush();
            out.close();
        }catch (Exception e){
            CommonLog.error(e.getMessage(),e);
            System.err.println("write file exception:" + e.getMessage());
        }
    }
  
}  

