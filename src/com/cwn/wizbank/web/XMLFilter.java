package com.cwn.wizbank.web;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Servlet Filter implementation class XMLFilter
 */

 class WrapperResponse extends HttpServletResponseWrapper {   
	   private MyPrintWriter tmpWriter;   
	   private ByteArrayOutputStream output;   
	   public WrapperResponse(HttpServletResponse httpServletResponse) {   
	      super(httpServletResponse);   
	      output = new ByteArrayOutputStream();   
	      tmpWriter = new MyPrintWriter(output);   
	   }   
	   public void finalize() throws Throwable {   
	      super.finalize();   
	      output.close();   
	      tmpWriter.close();   
	   }   
	   public String getContent() {   
	     // tmpWriter.flush();  
		 String s = tmpWriter.getByteArrayOutputStream().toString();   
		 return s;   
	   }   
	  
	   //覆盖getWriter()方法，使用我们自己定义的Writer   
	   public PrintWriter getWriter() throws IOException {   
	      return tmpWriter;   
	   }   
	   public void close() throws IOException {   
	      tmpWriter.close();   
	   }   
	  
	   //自定义PrintWriter，为的是把response流写到自己指定的输入流当中   
	   //而非默认的ServletOutputStream   
	   private static class MyPrintWriter extends PrintWriter {   
	      ByteArrayOutputStream myOutput;   //此即为存放response输入流的对象   
	  
	      public MyPrintWriter(ByteArrayOutputStream output) {   
	         super(output);   
	         myOutput = output;   
	      }   
	      public ByteArrayOutputStream getByteArrayOutputStream() {   
	         return myOutput;   
	      }   
	   }   
	}   


public class XMLFilter implements Filter {

    /**
     * Default constructor. 
     */
    public XMLFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain]
		
		
			
		HttpServletRequest req = (HttpServletRequest)request;
	
		
		String cmd = (String) req.getParameter("cmd");
		
		
		if(cmd != null && cmd.contains("_xml"))
		{
			HttpServletResponse hResponse = (HttpServletResponse)response;
		
			WrapperResponse wrapperResponse = new WrapperResponse(hResponse); 
			 
			 chain.doFilter(request, wrapperResponse);   
			 String html = wrapperResponse.getContent();   
			 html =  html.replaceAll("<", "&lt;");   
			 html = html.replaceAll(">", "&gt;");
		

			 response.getWriter().print("<pre>"+html+"</pre>");	
			 
		
			 
		}
		else
		{
			chain.doFilter(request, response);
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
