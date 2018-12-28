// Copyright (C) 1998 by Jason Hunter <jhunter@acm.org>.  All rights reserved.
// Use of this class is limited.  Please see the LICENSE for more information.

package com.oreilly.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

//WaiLun get the session id to create a temp filename for a chinese filename
import javax.servlet.http.HttpSession;

import com.cwn.wizbank.utils.CommonLog;
import org.apache.commons.lang.StringUtils;

import com.cw.wizbank.Application;
import com.cw.wizbank.util.UploadListener;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.ParamPart;

/** 
 * A utility class to handle <code>multipart/form-data</code> requests,
 * the kind of requests that support file uploads.  This class emulates the 
 * interface of <code>HttpServletRequest</code>, making it familiar to use. 
 * It uses a "push" model where any incoming files are read and saved directly
 * to disk in the constructor. If you wish to have more flexibility, e.g. 
 * write the files to a database, use the "pull" model 
 * <code>MultipartParser</code> instead.
 * <p>
 * This class can receive arbitrarily large files (up to an artificial limit 
 * you can set), and fairly efficiently too.  
 * It cannot handle nested data (multipart content within multipart content)
 * or internationalized content (such as non Latin-1 filenames).
 * <p>
 * See the included <a href="upload.war">upload.war</a> 
 * for an example of how to use this class.
 * <p>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at <a href="http://www.ietf.org/rfc/rfc1867.txt">
 * http://www.ietf.org/rfc/rfc1867.txt</a>.
 *
 * @see MultipartParser
 * 
 * @author Jason Hunter
 * @author Geoff Soutter
 * @version 1.6, 00/07/21, redid internals to use MultipartParser,
 *                         thanks to Geoff Soutter
 * @version 1.5, 00/02/04, added auto MacBinary decoding for IE on Mac
 * @version 1.4, 00/01/05, added getParameterValues(),
 *                         WebSphere 2.x getContentType() workaround,
 *                         stopped writing empty "unknown" file
 * @version 1.3, 99/12/28, IE4 on Win98 lastIndexOf("boundary=") workaround
 * @version 1.2, 99/12/20, IE4 on Mac readNextPart() workaround
 * @version 1.1, 99/01/15, JSDK readLine() bug workaround
 * @version 1.0, 98/09/18
 */
public class MultipartRequest {

  private static final int DEFAULT_MAX_POST_SIZE = 1024 * 1024;  // 1 Meg
  
  // WaiLun : the name of the cookie for storing the uploaded and converted filename
  private static final String ORIGINAL_FILENAME = "ORIGINAL_FILENAME";
  private static final String NEW_FILENAME = "NEW_FILENAME";
  
  // Chris Lo : default encoding
  private static String DEFAULT_ENCODING = "ISO-8859-1";

  private File dir;

  public Hashtable parameters = new Hashtable();  // name - Vector of values
  private Hashtable files = new Hashtable();       // name - UploadedFile

  public final static String FILE_UPLOAD_STATS = "FILE_UPLOAD_STATS";
  public final static String FILE_UPLOAD_LISTENER = "FILE_UPLOAD_LISTENER";
  
  private MultipartParser parser;
  
  private boolean upload_listener = false;
  
  private UploadListener listener = null;
  
  //不允许上传格式的文件
  private List<String> forBiddenFiles = new ArrayList<String>(); 
  /**
   * Constructs a new MultipartRequest to handle the specified request, 
   * saving any uploaded files to the given directory, and limiting the 
   * upload size to 1 Megabyte.  If the content is too large, an
   * IOException is thrown.  This constructor actually parses the 
   * <tt>multipart/form-data</tt> and throws an IOException if there's any 
   * problem reading or parsing the request.
   *
   * @param request the servlet request.
   * @param saveDirectory the directory in which to save any uploaded files.
   * @exception IOException if the uploaded content is larger than 1 Megabyte
   * or there's a problem reading or parsing the request.
   */
  public MultipartRequest(HttpServletRequest request,
                          String saveDirectory) throws IOException {
    this(request, saveDirectory, DEFAULT_ENCODING, DEFAULT_MAX_POST_SIZE);
  }

  /**
   * Constructs a new MultipartRequest to handle the specified request, 
   * saving any uploaded files to the given directory, and limiting the 
   * upload size to the specified length.  If the content is too large, an 
   * IOException is thrown.  This constructor actually parses the 
   * <tt>multipart/form-data</tt> and throws an IOException if there's any 
   * problem reading or parsing the request.
   *
   * @param request the servlet request.
   * @param saveDirectory the directory in which to save any uploaded files.
   * @param maxPostSize the maximum size of the POST content.
   * @exception IOException if the uploaded content is larger than 
   * <tt>maxPostSize</tt> or there's a problem reading or parsing the request.
 * @throws
   */
  public MultipartRequest(HttpServletRequest request,
                          String saveDirectory, String encoding,
                          int maxPostSize) throws IOException {
   CommonLog.debug("Call MultipartRequest() .....");
   HttpSession session = request.getSession();
   String showHeadImg ="";
    // Sanity check values
    if (request == null)
      throw new IllegalArgumentException("request cannot be null");
    if (saveDirectory == null)
      throw new IllegalArgumentException("saveDirectory cannot be null");
    if (maxPostSize <= 0) {
      throw new IllegalArgumentException("maxPostSize must be positive");
    }

    // Save the dir
    dir = new File(saveDirectory);

    // Check saveDirectory is truly a directory
    if (!dir.isDirectory())
      throw new IllegalArgumentException("Not a directory: " + saveDirectory);

    // Check saveDirectory is writable
    if (!dir.canWrite())
      throw new IllegalArgumentException("Not writable: " + saveDirectory);
    try {
        String lis = request.getParameter("upload_listener");
        if (lis != null && Boolean.valueOf(lis).booleanValue()) {
            upload_listener = true;
        }
    } catch (Exception e) {
        
    }
    if (upload_listener) {
        listener = new UploadListener(request.getContentLength());
        listener.start();
        session.setAttribute(FILE_UPLOAD_LISTENER, listener);
        session.setAttribute(FILE_UPLOAD_STATS, listener.getFileUploadStats());
    }

    // Parse the incoming multipart, storing files in the dir provided, 
    // and populate the meta objects which describe what we found
    parser = new MultipartParser(request, maxPostSize);

    Part part;
    while ((part = parser.readNextPart()) != null) {
      String name = part.getName();
//System.out.println("name : " + name );      
      if (part.isParam()) {
        // It's a parameter part, add it to the vector of values
        ParamPart paramPart = (ParamPart) part;
        
        // Chris Lo : Get the parameter using the encoding
        //String value = paramPart.getStringValue();
        String value = paramPart.getStringValue(encoding);
        
        if(name != null && value != null){
        	if(name.equals("image_radio") && value.equals("default_photo")){
        		showHeadImg = "2";
        		session.setAttribute("user_uplode_head_img", "off");
        	}
        	if(name.equals("image_radio") && value.equals("on")){
        		showHeadImg = "3";
        	}
        	
        }
        
        //out.println("param; name=" + name + ", value=" + value ;
        Vector existingValues = (Vector)parameters.get(name);
        if (existingValues == null) {
          existingValues = new Vector();
          parameters.put(name, existingValues);
        }
        existingValues.addElement(value);
      }
      else if (part.isFile()) {
        // It's a file part
        FilePart filePart = (FilePart) part;
        String fileName = filePart.getFileName();
        if (fileName != null) {
          // The part actually contained a file
            // WaiLun : check the filename contains chinese
            //if(chineseFilename(fileName,encoding)) {
            String rename = getParameter("rename");
            String suffix = fileName.substring(fileName.lastIndexOf(".")+1);  //获取后缀名
//            System.out.println("Rename new new: " + rename);
            if(rename == null || rename.trim().length() == 0) {
                fileName = convertFilename(request, fileName, encoding);
                filePart.setFileName(fileName);
                if(showHeadImg.equals("3")){
                	session.setAttribute("user_uplode_head_img", fileName);
                }
                CommonLog.debug("New Filename = " + fileName);
            }
          filePart.setListener(listener);
          filePart.writeTo(dir);
          files.put(name, new UploadedFile(dir.toString(), fileName, filePart.getContentType()));
          Vector existingValues = (Vector)parameters.get(name);
          if (existingValues == null) {
            existingValues = new Vector();
            parameters.put(name, existingValues);
          }
          existingValues.addElement(fileName);
          if(!checkFile(suffix)){
        	  forBiddenFiles.add(name);
          }
        }
        else {
          // The field did not contain a file
          files.put(name, new UploadedFile(null, null, null));
        }
      }
    }
    if (upload_listener) {
        listener.uploaded();
    }
    for(String name : forBiddenFiles){
    	UploadedFile file = (UploadedFile)files.get(name);
    	if(null!=file ){
    		if(null!=file.getFile()&& file.getFile().isFile()){
    			file.getFile().delete();
    			files.remove(name);
    		}
    	}
    }
    
    
  }
  
  private boolean checkFile(String suffix){
	  String forbidden = Application.UPLOAD_FORBIDDEN;
	  if(!StringUtils.isEmpty(forbidden)){
		  String[] forbiddens =forbidden.split(",");
		  if(null!=forbiddens && forbiddens.length>0){
			for(String fb : forbiddens){
				if(fb.equalsIgnoreCase(suffix)){
					return false;
				}
			}
		  }
	  }

	  return true;
  }
  
  
    public String convertFilename(HttpServletRequest request, String fileName, String encoding) throws IOException{
        //save the original and converted filename in the session
        //create the cookie if not exist in the session                
        HttpSession sess = null;
        sess = request.getSession(true);                                        
        Vector original_filename;
        Vector new_filename;
        if(sess.getAttribute(ORIGINAL_FILENAME) == null) {
            original_filename = new Vector();
            new_filename = new Vector();
        }
        else {
            original_filename = (Vector)sess.getAttribute(ORIGINAL_FILENAME);
            new_filename = (Vector)sess.getAttribute(NEW_FILENAME);
        }


        // generate a new filename by system time
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        //delete the object if original_filename contains
        String originalFilename = new String(fileName.getBytes(DEFAULT_ENCODING), encoding);
        int loc_file = original_filename.indexOf(originalFilename);
        if (loc_file != -1) {
            original_filename.remove(loc_file);
            new_filename.remove(loc_file);
        }

        //generate the filename according to the system time
        String _filename = new_filename.size() + Long.toString((rightNow.getTime()).getTime());
        String _type = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
        fileName = _filename + "." + _type;
        new_filename.addElement(fileName);
        original_filename.addElement(originalFilename);
        
        sess.setAttribute(NEW_FILENAME,new_filename);
        sess.setAttribute(ORIGINAL_FILENAME,original_filename);
        return fileName;
    }
  
/*  
  // WaiLun : Check the filename contains chinese
  private boolean chineseFilename(String filename, String encoding) {
    try{        
        filename = new String(filename.getBytes(DEFAULT_ENCODING), encoding);
    }catch(Exception e){
        System.out.println("Change Filename : Unsupported Encoding on Filename.");
    }
    for(int i=0; i<filename.length(); i++) {
        if(filename.charAt(i) > 256)
            return true;
    }
    return false;
  }
*/  
  

  /**
   * Constructor with an old signature, kept for backward compatibility.
   * Without this constructor, a servlet compiled against a previous version 
   * of this class (pre 1.4) would have to be recompiled to link with this 
   * version.  This constructor supports the linking via the old signature.
   * Callers must simply be careful to pass in an HttpServletRequest.
   * 
   */
  public MultipartRequest(ServletRequest request,
                          String saveDirectory) throws IOException{
    this((HttpServletRequest)request, saveDirectory);
  }

  /**
   * Constructor with an old signature, kept for backward compatibility.
   * Without this constructor, a servlet compiled against a previous version 
   * of this class (pre 1.4) would have to be recompiled to link with this 
   * version.  This constructor supports the linking via the old signature.
   * Callers must simply be careful to pass in an HttpServletRequest.
   * 
   */
  public MultipartRequest(ServletRequest request,
                          String saveDirectory, String encoding,
                          int maxPostSize) throws IOException {
    this((HttpServletRequest)request, saveDirectory, encoding, maxPostSize);
  }

  /**
   * Returns the names of all the parameters as an Enumeration of 
   * Strings.  It returns an empty Enumeration if there are no parameters.
   *
   * @return the names of all the parameters as an Enumeration of Strings.
   */
  public Enumeration getParameterNames() {
    return parameters.keys();
  }

  /**
   * Returns the names of all the uploaded files as an Enumeration of 
   * Strings.  It returns an empty Enumeration if there are no uploaded 
   * files.  Each file name is the name specified by the form, not by 
   * the user.
   *
   * @return the names of all the uploaded files as an Enumeration of Strings.
   */
  public Enumeration getFileNames() {
    return files.keys();
  }

  /**
   * Returns the value of the named parameter as a String, or null if 
   * the parameter was not sent or was sent without a value.  The value 
   * is guaranteed to be in its normal, decoded form.  If the parameter 
   * has multiple values, only the last one is returned (for backward 
   * compatibility).  For parameters with multiple values, it's possible
   * the last "value" may be null.
   *
   * @param name the parameter name.
   * @return the parameter value.
   */
  public String getParameter(String name) {
    try {
      Vector values = (Vector)parameters.get(name);
      if (values == null || values.size() == 0) {
        return null;
      }
      String value = (String)values.elementAt(values.size() - 1);
      return value;
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Returns the values of the named parameter as a String array, or null if 
   * the parameter was not sent.  The array has one entry for each parameter 
   * field sent.  If any field was sent without a value that entry is stored 
   * in the array as a null.  The values are guaranteed to be in their 
   * normal, decoded form.  A single value is returned as a one-element array.
   *
   * @param name the parameter name.
   * @return the parameter values.
   */
  public String[] getParameterValues(String name) {
    try {
      Vector values = (Vector)parameters.get(name);
      if (values == null || values.size() == 0) {
        return null;
      }
      String[] valuesArray = new String[values.size()];
      values.copyInto(valuesArray);
      return valuesArray;
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Returns the filesystem name of the specified file, or null if the 
   * file was not included in the upload.  A filesystem name is the name 
   * specified by the user.  It is also the name under which the file is 
   * actually saved.
   *
   * @param name the file name.
   * @return the filesystem name of the file.
   */
  public String getFilesystemName(String name) {
    try {
      UploadedFile file = (UploadedFile)files.get(name);
      return file.getFilesystemName();  // may be null
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Returns the content type of the specified file (as supplied by the 
   * client browser), or null if the file was not included in the upload.
   *
   * @param name the file name.
   * @return the content type of the file.
   */
  public String getContentType(String name) {
    try {
      UploadedFile file = (UploadedFile)files.get(name);
      return file.getContentType();  // may be null
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Returns a File object for the specified file saved on the server's 
   * filesystem, or null if the file was not included in the upload.
   *
   * @param name the file name.
   * @return a File object for the named file.
   */
  public File getFile(String name) {
    try {
      UploadedFile file = (UploadedFile)files.get(name);
      return file.getFile();  // may be null
    }
    catch (Exception e) {
      return null;
    }
  }

public List<String> getForBiddenFiles() {
	return forBiddenFiles;
}

public void setForBiddenFiles(List<String> forBiddenFiles) {
	this.forBiddenFiles = forBiddenFiles;
}
  
  
}


// A class to hold information about an uploaded file.
//
class UploadedFile {

  private String dir;
  private String filename;
  private String type;

  UploadedFile(String dir, String filename, String type) {
    this.dir = dir;
    this.filename = filename;
    this.type = type;
  }

  public String getContentType() {
    return type;
  }

  public String getFilesystemName() {
    return filename;
  }

  public File getFile() {
    if (dir == null || filename == null) {
      return null;
    }
    else {
      return new File(dir + File.separator + filename);
    }
  }
  
  
}

