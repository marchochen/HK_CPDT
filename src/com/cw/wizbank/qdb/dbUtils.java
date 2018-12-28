package com.cw.wizbank.qdb;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import javax.servlet.http.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.sql.*;

// Perl regular expression
import com.oroinc.text.perl.*;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.AcRegUser;
import com.cw.wizbank.util.SendHttpRequest;
import com.cw.wizbank.util.cwEncode;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;
public class dbUtils
{

  public static final String ENC_ENG = "ISO-8859-1";
  public static final String ENC_UTF = "UTF-8";
  public static final String NEWL = System.getProperty("line.separator");
  public static final String SLASH = System.getProperty("file.separator");

  public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"" + ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
  
  public static final String MSG_REC_NOT_FOUND = "GEN005";
  
  public static final String DOMAIN_SCHOOL = "school";
  public static final String DOMAIN_PUBLIC = "public";
  public static final String DOMAIN_PERSONAL = "personal";
  public static final String DOMAIN_ALL = "all";

  public static final String SORT_BY_TITLE = "title";
  public static final String SORT_BY_ID = "id";
  public static final String SORT_BY_TIME = "time";
  public static final String SORT_BY_STATUS = "status";
  public static final String SORT_BY_TYPE = "type";
  public static final String SORT_BY_AUTHOR = "author";
  public static final String SORT_BY_DESCP = "descp";
  
  public static final String DBVENDOR_ORACLE = "oracle";
  public static final String DBVENDOR_MSSQL = "microsoft sql server";
  
  public static final String IMMEDIATE = "IMMEDIATE"; 
  public static final String UNLIMITED = "UNLIMITED";  
  public static final String MIN_TIMESTAMP = "1753-01-01 00:00:00";  
  public static final String MAX_TIMESTAMP = "9999-12-31 23:59:59";  
  
  
  public static Perl5Util perl = new Perl5Util();
  
  public dbUtils () {;}

  public static Timestamp[] string2timestamp(String var[]) {
      Timestamp[] result = new Timestamp[var.length];
        
      for(int i=0;i<result.length;i++) {
          try {
            result[i] = Timestamp.valueOf(var[i]);
          }
          catch(IllegalArgumentException e) {
            result[i] = new Timestamp(0);
          }
      }
      return result;         
  }
    
  public static long[] string2long(String[] str) {
      int size = str.length;
      long[] l = new long[size];
      for(int i=0;i<size;i++) {
        try {
            l[i] = Long.parseLong(str[i]);
        }
        catch(NumberFormatException e) {
            l[i] = -1;
        }
      }
      return l;
  }
  
  public static int[] string2int(String[] str) {
      int size = str.length;
      int[] ia = new int[size];
      for(int i=0;i<size;i++) {
        try {
            ia[i] = Integer.parseInt(str[i]);
        }
        catch(NumberFormatException e) {
            ia[i] = -1;
        }
      }
      return ia;
  }

  public static boolean isMaxTimestamp(Timestamp ts) {
      if(ts.equals(Timestamp.valueOf(MAX_TIMESTAMP)))
          return true;
      else
          return false;
  }
   
  public static boolean isMinTimestamp(Timestamp ts) {
      if(ts.equals(Timestamp.valueOf(MIN_TIMESTAMP)))
          return true;
      else
          return false;
  }
  
  public static String convertEndDate(Timestamp eff_end_date) {
      String tmp_end_date="";
      if(eff_end_date != null)
          if(isMaxTimestamp(eff_end_date) == true)
              tmp_end_date = UNLIMITED; //convert to String to "UNLIMITED"
          else
              tmp_end_date = eff_end_date.toString();
      return tmp_end_date;
  }

  public static Timestamp convertEndDate(String val) {
        if ( val.equalsIgnoreCase(UNLIMITED))
            return Timestamp.valueOf(MAX_TIMESTAMP);
        else
            return Timestamp.valueOf(val);
  }
  
  public static Timestamp convertStartDate(String val) {
        if ( val.equalsIgnoreCase(IMMEDIATE))
            return Timestamp.valueOf(MIN_TIMESTAMP);
            //cannot get the current time as DB connection not yet started
        else
            return Timestamp.valueOf(val);
  }

  public static Timestamp getTime(Connection con)
          throws qdbException
  {
    try{
        Timestamp curTime = cwSQL.getTime(con);
        return curTime;
        
    } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());   
    }
      
  }

    public static String array2list(long[] id) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(0");
        if(id!=null) {
            for(int i=0;i<id.length;i++) 
                listBuf.append(",").append(id[i]);
        }
        listBuf.append(")");    
        list = new String(listBuf);
        return list;
    }
  
  public static String array2list(String[] id_lst)
  {
        String result = ""; 
        if (id_lst == null || id_lst.length ==0 || id_lst[0] == null || id_lst[0].length() ==0)
            return "(0)";
        
        for (int i=0;i<id_lst.length;i++) {
            if (i==0) 
                result += "( " + id_lst[i]; 
            else 
                result += ", " + id_lst[i];
        }
        result += ")"; 
        
        if (id_lst.length ==0)
            result = "(0)";
        
        return result; 
  }
  
  public static String vec2list(Vector id_lst)
  {
        String[] list = new String[id_lst.size()]; 
        for (int i=0;i<id_lst.size();i++) {
            list[i] = ((Long) id_lst.elementAt(i)).toString();
        }
        
        return array2list(list); 
  }
  
  public static String longArray2String(long[] ids, String delimiter)
  {
        StringBuffer str = new StringBuffer();
        for(int i=0; i<ids.length; i++) {
            if(i == 0)
                str.append(ids[i]);
            else
                str.append(delimiter).append(ids[i]);
        }
        
        return str.toString();
    
  }
  
  public static long[] string2LongArray(String str, String delimiter)
  {
        str += delimiter;
        Vector vec = new Vector();
        int index = str.indexOf(delimiter, 0);
        String element;
        while (index > 0) {
            element = str.substring(0, index);
            if(element != null && element.trim().length() > 0)
                vec.addElement(element);
            str = str.substring(index + delimiter.length()); 
            index = str.indexOf(delimiter, 0); 
        }

        long[] ids = new long[vec.size()];
        for(int i=0; i<ids.length; i++)
            ids[i] = Long.parseLong((String)vec.elementAt(i));

        return ids;
  }
  
  /*
  public static String vecStr2list(Vector id_lst)
  {
        String[] list = new String[id_lst.size()]; 
        for (int i=0;i<id_lst.size();i++) {
            list[i] = "'" + ((String) id_lst.elementAt(i)).toString() + "'";
        }
        
        return array2list(list); 
  } 
  */
  public static String vec2String(Vector id_lst)
  {
        String resIds = null;
        
        if (id_lst == null || id_lst.size() == 0)
            return resIds;
            
        resIds = "(";
             
        for (int i=0;i<id_lst.size();i++) {
            resIds += ((Long) id_lst.elementAt(i)).toString();
            if (i!=id_lst.size()-1)
                resIds += ",";
            else 
                resIds += ")";
        }
        return resIds; 
  }
  
  public static String vec2String(Vector id_lst, String delimiter)
  {
        String entIds = null;
        
        if (id_lst == null || id_lst.size() == 0)
            return entIds;
        
        if(delimiter == null || "".equals(delimiter)) {
            delimiter = "~";
        }
             
        entIds = (String)id_lst.elementAt(0);
        for (int i=1;i<id_lst.size();i++)
            entIds += delimiter + (String)id_lst.elementAt(i);

        return entIds; 
  }

  
  
  public static Vector convert2Vec(String in)
  {
        Vector vec = new Vector(); 
        if (in==null || in.length() == 0)
            return vec; 
        
        String pattern = "~DELIMITER~";
        
        int indexS = in.indexOf(pattern,0); 
        while (indexS > 0) {
            String element = new String(in.substring(0, indexS)); 
            vec.addElement(element); 
            in = in.substring(indexS + pattern.length()); 
            indexS = in.indexOf(pattern, 0); 
        }
        return vec; 
  }
  
  
  public static Vector convert2Vec(String in, String delimiter)
  {
        Vector vec = new Vector();
        if( in == null || in.length() == 0 )
            return vec;
            
        int index = in.indexOf(delimiter, 0);
        String element;
        while(index > 0) {
            element = in.substring(0, index);
            if( element != null && element.trim().length() > 0 )
                vec.addElement(element);
            in = in.substring(index + delimiter.length());
            index = in.indexOf(delimiter, 0);
        }
        return vec;
  }
  
  public static String decodeURL(String in)
    {
        
        if(in==null || in.length()==0)
            return in;

        String result="";
        if(perl.match("#%3A#i", in))
            in = perl.substitute("s#%3A#:#ig", in);
         if(perl.match("#%3F#i", in))
            in = perl.substitute("s#%3F#?#ig", in);
        if(perl.match("#%3D#i", in))
            in = perl.substitute("s#%3D#=#ig", in);    
        if(perl.match("#%26#i", in))
            in = perl.substitute("s#%26#&#ig", in);    

        return in;
    }
  
   public static String esc4XML(String in)
    {
        if(in==null || in.length()==0)
            return in;
            
        
        return cwUtils.esc4XML(in);
    }
    
    public static String unEscXML(String in)
    {
        if(in==null || in.length()==0)
            return in;
            
        String result="";
        if(perl.match("#&amp;#i", in))
            in = perl.substitute("s#&amp;#&#ig", in);
        if(perl.match("#&lt;#i", in))
            in = perl.substitute("s#&lt;#<#ig", in);
        if(perl.match("#&quot;#i", in))
            in = perl.substitute("s#&quot;#\"#ig", in);
        return in;
    }
    
    public static String delTab(String in)
    {
        if(in==null || in.length()==0)
            return in;
            
        String result="";
        if(perl.match("#\t#i", in))
            in = perl.substitute("s#\t# #ig", in);

        return in;
    }

    public static String trimSpace(String in)
    {
        if(in==null || in.length()==0)
            return in;
            
        String result="";
        //
        //if(perl.match("#\\s+#i", in))
        //    in = perl.substitute("s# ##ig", in);
        //
        if(perl.match("# #i", in))
            in = perl.substitute("s# ##ig", in);


        return in;
    }
    
    // convert input string from encoding "fmEnc" to unicode
    // "inEnc" is the code page of the client got by request.getCharacterEncoding
    // "frmEnc" is one of {"Big5", "GD2312"}
    // e.g. unicodeFrom(someString, "Cp950" ,"Big5")
     public static String unicodeFrom(String in, String inEnc, String fmEnc, boolean bMultipart)
        throws UnsupportedEncodingException
    {
        //System.out.println("inEnc : " + inEnc);
        //System.out.println("fmEnc : " + fmEnc);
//        if (bMultipart)
//            return in;
//        else if ( in==null || in.length()==0)
//            return in;
//        else
//            return new String(in.getBytes(inEnc), fmEnc);
        return in;
    }

    public static String unicodeFrom(String in, String inEnc, String fmEnc)
        throws UnsupportedEncodingException
    {
        // default value of bMultipart is fasle;
        return unicodeFrom(in, inEnc, fmEnc, false);
    }

    /*
    public static String unicode2Big5(String str)
        throws qdbException
    {
       try{         
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        byte[] sb = str.getBytes("Big5");
        bas.write(sb);
        String result = bas.toString("Big5");
        return result;
       } catch(IOException e) {
            throw new qdbException("Error 2 Big5");
       }        
    }
    
    public static String unicode2GB(String str)
        throws qdbException
    {
       try{         
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        byte[] sb = str.getBytes("GB2312");
        bas.write(sb);
        String result = bas.toString("GB2312");
        return result;
       } catch(IOException e) {
            throw new qdbException("Error 2 GB2312");
       }        
    }
    */
    
    public static void copyDir(String srcDir, String toDir)
        throws qdbException
    {

        File fromDir = new File(srcDir);
        File saveDir = null; 
        if(fromDir.exists()) {
            saveDir = new File(toDir);
            if(!saveDir.exists())
                saveDir.mkdirs();
        }else {
            return; 
        }
        
        String[] flList = fromDir.list();
        File oF = null;
        File nF = null;
        RandomAccessFile f1 = null;
        RandomAccessFile f2 = null;
        byte buffer1[];
        int bufsize;
        try { 
            
            for(int i=0;i<flList.length;i++)
            {
                oF = new File(fromDir, flList[i]);  

                if (oF.isDirectory()) {
                    String subSrcDir = srcDir + dbUtils.SLASH + flList[i];
                    String subToDir = toDir + dbUtils.SLASH + flList[i];
                    dbUtils.copyDir(subSrcDir, subToDir);
                } else {
                    nF = new File(saveDir, oF.getName());
                    if (nF.exists()) {
                        nF.delete();
                        nF = new File(saveDir, oF.getName());
                    }
                    copyFile(oF, nF);
                    /*
                    f1 = new RandomAccessFile(oF, "r");
                    bufsize = (int) f1.length();
                    nF = new File(saveDir, oF.getName());
                    if (nF.exists()) {
                        nF.delete();
                        nF = new File(saveDir, oF.getName());
                    }
                    f2 = new RandomAccessFile(nF, "rw");
                    buffer1 = new byte [bufsize];
                    f1.readFully(buffer1, 0, bufsize);
                    f2.write(buffer1, 0, bufsize);
                    f1.close();
                    f2.close();
                    */
                }
            }

        } catch(IOException e) {
                throw new qdbException("IO Error: " + e.getMessage());
        }
    }


    /**
    Copy the source File to the target File
    */
    public static void copyFile(File source, File destination) throws FileNotFoundException, IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
        byte[] data = new byte[1024*1024];
        int length=0;
        while((length = in.read(data)) > -1) {
            out.write(data,0,length);
        }
        in.close();
        out.close();
    }
    
    public static void copyFile(String srcFile, String desPath)
    		throws qdbException
    {
    	File fromFile = new File(srcFile);
        File saveDir = null; 
        if(fromFile.exists() && fromFile.isFile()) {
            saveDir = new File(desPath);
            if(!saveDir.exists())
                saveDir.mkdirs();
        }else {
            return; 
        }
	    try { 
	         File nF = new File(saveDir, fromFile.getName());
	         if (nF.exists()) {
	             nF.delete();
	             nF = new File(saveDir, fromFile.getName());
	         }
	         copyFile(fromFile, nF);
	    } catch(IOException e) {
	         throw new qdbException("IO Error: " + e.getMessage());
	    }
	}
    
    public static void copyMediaFrom(String uploadDir, long frmId, long toId)
        throws qdbException
    {

        String fromDirPath = uploadDir + SLASH + frmId;
        String saveDirPath = uploadDir + SLASH + toId;

        dbUtils.copyDir(fromDirPath, saveDirPath); 
        
    }
    
    public static void moveDir(String srcDirPath, String saveDirPath)
        throws qdbException
    {
        dbUtils.copyDir(srcDirPath, saveDirPath); 
        dbUtils.delDir(srcDirPath);
    }
    
    /*
    public static void moveDir(String srcDirPath, String saveDirPath)
        throws qdbException
    {
        boolean bOk = true; 
        
        if( srcDirPath==null || srcDirPath.equals(""))
            return;
        File srcDir = new File(srcDirPath);
        if( !srcDir.exists() )
            return;
            
        File saveDir = new File( saveDirPath );
        // move files in tmpSaveDir to saveDir and remove tmpSaveDir
        String[] flList = srcDir.list();
        File oF = null;
        String nName = "";
        File nF = null;
        if(flList.length>0)
        {
        for(int i=0;i<flList.length;i++)
        {
                oF = new File(srcDir, flList[i]);
                if(oF.getName().equalsIgnoreCase("unknown"))
                {
                    bOk = oF.delete(); 
                    if (!bOk)
                        throw new qdbException("Failed to cleanup temp file.");
                    continue;
                }
                nName = saveDirPath + dbUtils.SLASH + oF.getName();
                nF = new File(nName);
                // if not exist, create the dir
                if (!saveDir.exists())
                bOk = saveDir.mkdirs();
                if (!bOk)
                    throw new qdbException("Failed to create dir: " + saveDirPath);
                
                // delete and then rename
                if(nF.exists())
                {
                bOk = nF.delete();
                if(!bOk)
                    throw new qdbException("Failed to delete original file:" + nName);
                }
                    
                bOk = oF.renameTo(nF);
                if( !bOk )
                    throw new qdbException("Failed to move file to " + nName);
            }
        }
        
        // remove tmpSaveDir
        bOk = srcDir.delete();
        if(!bOk)
            throw new qdbException("Failed to remove source dir: " + srcDirPath);
        
    }
    */
    
    public static String[] split(String in, String delimiter)
    {
        Vector q = new Vector();
        String[] result = null;
        
        if (in == null || in.length()==0)
            return result;
        
        int pos =0;
        pos = in.indexOf(delimiter);
        
        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos); 
            }
            q.addElement(val);
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.addElement(in);
        }

        result = new String[q.size()];
        for (int i=0; i<q.size();i++) {
            result[i] = (String) q.elementAt(i);
        }
        
        return result;
        
    }
    
    public static String CharsetToLanguage(String encoding) 
    {
        Hashtable map = new Hashtable();

        map.put("ISO-8859-1", "en");
        map.put("Shift_JIS", "ja");
        map.put("EUC-KR", "ko");     // Requires JDK 1.1.6
        map.put("ISO-8859-5", "uk");
        map.put("GB2312", "zh");
        map.put("Big5", "zh_TW");
        
        String val = (String) map.get(encoding);
        return val; 
    }
    
    public static void delDir(String srcDirPath)
    {   
        File dir = new File(srcDirPath);

        if ( !dir.exists()) 
            return;
            
        String[] fList;
        File fh = null;
        
        if (dir.isDirectory()) {
            fList = dir.list();

            for (int i = 0; i < fList.length; i++) {
                fh = new File(dir, fList[i]);
                
                if (fh.isDirectory()) {
                    delDir(srcDirPath + dbUtils.SLASH + fList[i]);
                } else {
                    fh.delete();
                }
            }
            
            dir.delete();
        }
    }
    
    
    public static void delFiles(String srcDirPath) {
    	File dir = new File(srcDirPath);

        if ( !dir.exists()) 
            return;
            
        String[] fList;
        File fh = null;
        
        if (dir.isDirectory()) {
	        fList = dir.list();
	        for (int i = 0; i < fList.length; i++) {
	            fh = new File(dir, fList[i]);
	            if (fh.isDirectory()) {
	            	delDir(srcDirPath + dbUtils.SLASH + fList[i]);
	            } else {
	                fh.delete();
	            }
	        }
        }
    }
    public static void delOtherFiles(String srcDirPath,Vector selfList) {
    	File dir = new File(srcDirPath);
    	if(selfList == null){
    		selfList = new Vector();
    	}
        if ( !dir.exists()) 
            return;
            
        String[] fList;
        File fh = null;
        
        if (dir.isDirectory()) {
	        fList = dir.list();
	        for (int i = 0; i < fList.length; i++) {
	            fh = new File(dir, fList[i]);
	            if (fh.isDirectory()) {
	            	delOtherFiles(srcDirPath + dbUtils.SLASH + fList[i],null);
	            } else {
	            	if(!selfList.contains(fList[i])){
	            		fh.delete();
	            	}
	            }
	        }
        }
    }
   /**
    * Creates a Zip archive. If the name of the file passed in is a
    * directory, the directory's contents will be made into a Zip file.
    */
   public static void makeZip(String zipFilename, String zipPath, String[] files, boolean includeSubDir)
         throws FileNotFoundException, qdbErrMessage, IOException, ZipException
   {
      File file = new File(zipFilename);

      if (! file.exists()) {
          File parentPath = new File(file.getParent());
          parentPath.mkdirs();
      }
      
      File fh;
      String[] fileNames;
      ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
      //Call recursion.

      for (int i=0; i<files.length; i++) {
        //Recursively add each array entry to make sure that we get 
        //subdirectories as well as normal files in the directory.
        
        //Check if the directory or files exists
        File resFile = new File(zipPath, files[i]);
        if (resFile.exists()) {
            recurseFiles(new File(zipPath, files[i]), zipPath, zos, includeSubDir);
        }            
      }

      //We are done adding entries to the zip archive,
      //so close the Zip output stream.
      zos.close();      
   }

   /**
    * Recurses down a directory and its subdirectories to look for
    * files to add to the Zip. If the current file being looked at
    * is not a directory, the method adds it to the Zip file.
    */
   public static void recurseFiles(File file, String zipPath, ZipOutputStream zos, boolean includeSubDir)
      throws IOException, FileNotFoundException
   {
      if (file.isDirectory()) {
         //Create an array with all of the files and subdirectories
         //of the current directory.
         if (includeSubDir) {
            String[] fileNames = file.list();
            if (fileNames != null) {
               //Recursively add each array entry to make sure that we get 
               //subdirectories as well as normal files in the directory.
               for (int i=0; i<fileNames.length; i++)  {
                  recurseFiles(new File(file, fileNames[i]), zipPath, zos, includeSubDir);
               }
            }
         }
      }
      //Otherwise, a file so add it as an entry to the Zip file.
      else {
         byte[] buf = new byte[1024];
         int len;

         //Create a new Zip entry with the file's name.
         String filePath = file.toString();
         int index = -1;
         
         if ((index = filePath.indexOf(zipPath, index)) == 0) {
            filePath = filePath.substring(index + zipPath.length() + 1);
         }
         
//         ZipEntry zipEntry = new ZipEntry(filePath);
         ZipEntry zipEntry = new ZipEntry(filePath.replace(File.separatorChar,'/'));

         //Create a buffered input stream out of the file
         //we're trying to add into the Zip archive.
         FileInputStream fin = new FileInputStream(file);
         BufferedInputStream in = new BufferedInputStream(fin);
         zos.putNextEntry(zipEntry);

         //Read bytes from the file and write into the Zip archive.
         while ((len = in.read(buf)) >= 0) {
            zos.write(buf, 0, len);
         }
         //Close the input stream.
         in.close();
         //Close this entry in the Zip stream.
         zos.closeEntry();
      }
   }   
   
    public static void unzip(String zipfilename, String zip2path)
          throws IOException, FileNotFoundException, cwSysMessage
    {
    	ZipFile zipfile = new ZipFile(zipfilename);
        ZipEntry zipentry;
        //check the files first,check if it has non-alphanumeric filename
        Enumeration enum_temp = zipfile.entries();
        InputStream dataIn_temp;
        while (enum_temp != null && enum_temp.hasMoreElements()) {
        	zipentry = (ZipEntry)enum_temp.nextElement();
        	dataIn_temp = zipfile.getInputStream(zipentry);
            if(dataIn_temp == null){
            	Vector v_data = new Vector();
                v_data.addElement(zipentry.getName());
            	throw new cwSysMessage("MOD019", v_data);
            }
        }        
        Enumeration enumeration = zipfile.entries();
        InputStream dataIn;
        FileOutputStream fileOut;
        File file;
        File fileDir;
        byte[] b = new byte[1024*1024];
            
        try {
            while (enumeration != null && enumeration.hasMoreElements()) {
                zipentry = (ZipEntry)enumeration.nextElement();
                dataIn = zipfile.getInputStream(zipentry);
                file = new File(zip2path, zipentry.getName());
                fileDir = new File(file.getParent());
             
                String path_str = zipentry.toString();
                String endChar = path_str.substring(path_str.length()-1, path_str.length());
                
                if (! file.exists() && endChar.equals("/")) {
                    file.mkdirs();
                } else if (! fileDir.exists()) {
                    fileDir.mkdirs();
                }

                if (! file.isDirectory()) {
                    //System.out.println(">>>>" + file.getPath());
                    
                    fileOut = new FileOutputStream(file);        
                    /*
                    b = new byte[(int)zipentry.getSize()];

                    dataIn.readFully(b);
                    */
		            int length=0;
		            while((length = dataIn.read(b)) > -1) {
		                fileOut.write(b,0,length);
		            }
		            dataIn.close();
                    fileOut.close();
                }
            }
            
            zipfile.close();
        } catch (ZipException e) {
            zipfile.close();
            CommonLog.error(e.getMessage(),e);
       }    
   }
   
   public static String subsitute(String in, String matchStr, String toStr) 
   {
      //if(perl.match("#" + matchChar + "#i", in))
      //      in = perl.substitute("s#" + matchChar + "#" + toChar + "#ig", in);

      if (in == null || matchStr == null || toStr == null)
        return in;
      
      StringBuffer out = new StringBuffer();
      int index = in.indexOf(matchStr);
      
      if (index > -1) {
            out.append(in.substring(0,index));
            out.append(toStr);
            String tailStr = in.substring(index + matchStr.length(), in.length());
            out.append(dbUtils.subsitute(tailStr, matchStr, toStr));
      }else {
            out.append(in);
      }
            
      return out.toString();
   }
  
   public static boolean strArrayContains(String[] array, String matchStr) 
   {
        return strArrayContains(array, matchStr, false); 
   }
   
   public static boolean strArrayContains(String[] array, String matchStr, boolean cs) 
   {

        for (int i=0;i<array.length;i++) {
            if (cs && array[i].equals(matchStr))
                return true;
            else if (!cs && array[i].equalsIgnoreCase(matchStr))
                return true;
        }
        return false;
   }
   
   public static void appendVec(Vector vec1, Vector vec2)
    {
        for (int i=0;i<vec2.size();i++) {
            if (!vec1.contains(vec2.elementAt(i)))
                vec1.addElement(vec2.elementAt(i));
        }
    }
    
    
    // remove duplicate element in the vector
    public static void removeDuplicate(Vector vec) {
        if( vec == null || vec.isEmpty() )
            return;
        Long id;
        int pos;
        for(int i=vec.size()-1; i>=0; i--){
            id = (Long)vec.elementAt(i);
            if( (pos = vec.indexOf(id)) != -1 && pos != i)
                vec.removeElementAt(pos);
        }
        /*
        int pos;
        for(int i=0; i<vec.size(); i++) {
            id = (Long)vec.elementAt(i);
            for(int j=i+1; j<vec.size(); j++) {
                pos = vec.indexOf(id, j);
                if( pos != -1 ) {
                    vec.removeElementAt(pos);
                }
            }
        }
        */
        return;
    }
    
    //get the roles of the user has
    public static String[] getUserRoles(Connection con, long usr_ent_id) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        return acl.getUserRoles(con, usr_ent_id);
    }

    //get the roles of the user has
    public static String[] getUserRolesCanLogin(Connection con, long usr_ent_id) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        return acl.getUserRolesCanLogin(con, usr_ent_id);
    }
    
    /*
     * get the roles that the user has for json
     */
    public static Vector getUserRolesJsonCanLogin(Connection con, long usr_ent_id) throws SQLException {
    	String[] roleArray = getUserRolesCanLogin(con, usr_ent_id);
    	Vector rolVec = null;
    	if(roleArray != null && roleArray.length > 0) {
    		rolVec = new Vector();
    		for(int roleIndex = 0; roleIndex < roleArray.length; roleIndex++) {
    			HashMap roleMap = new HashMap();
    			roleMap.put("id", roleArray[roleIndex]);
    			rolVec.addElement(roleMap);
    		}
    	}
    	return rolVec;
    }

    public static String getRoleURL(Connection con, String rol_ext_id) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        return acl.getRoleURL(con, rol_ext_id);
    }
    
    public static String[] getRoleNSkinNHomeURL(Connection con, String rol_ext_id) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        return acl.getRoleDescNSkinRootNHomeURL(con, rol_ext_id);
    }    
    
    public static String getAllRoleAsXML(Connection con, String rootTag, long ste_ent_id) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        Hashtable roles = acl.getAllRoleDesc(con, ste_ent_id, false);
        Vector v_rol_xml = (Vector) roles.get(AccessControlWZB.ROL_XML);
        StringBuffer xmlBuf = new StringBuffer(1024);
        String this_rol_xml;
        
        xmlBuf.append("<").append(rootTag).append(">");
        for(int i=0; i<v_rol_xml.size(); i++) {
            
            this_rol_xml = (String) v_rol_xml.elementAt(i);            
            xmlBuf.append(this_rol_xml).append(dbUtils.NEWL);
        }
        xmlBuf.append("</").append(rootTag).append(">");
        return xmlBuf.toString();
    }
    
    // return rol_ext_id
	public static Vector getAuthRole(Connection con, loginProfile prof) throws SQLException {
		AccessControlWZB acl = new AccessControlWZB();
        Hashtable roles = acl.getAllRoleDesc(con, prof.root_ent_id, false);
		Vector v_rol_ext_id = (Vector) roles.get(AccessControlWZB.ROL_EXT_ID);
		Vector v_rol_auth_level = (Vector)roles.get(AccessControlWZB.ROL_AUTH_LEVEL);
		Vector v_auth_ext_id = new Vector();
        String rol_ext_id;
        
		boolean mgt_same_and_lower_role = false;
		boolean mgt_lower_role = false;
		try{
			mgt_same_and_lower_role = true;
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
			mgt_same_and_lower_role = false;
		}
		if(!mgt_same_and_lower_role) {
			try{
				mgt_lower_role = true;
			}catch(Exception e){
				CommonLog.error(e.getMessage(),e);
				mgt_lower_role = false;
			}		
		}
		long auth_level = 0;
		try{
			auth_level = acl.getRoleAuthLevel(con, prof.root_ent_id, prof.current_role);
		}catch(Exception e){
			throw new SQLException("Failed to get rol_auth_level, rol_ext_id = " + prof.current_role);
		}
		        
		if( mgt_same_and_lower_role || mgt_lower_role )

		for(int i=0; i<v_rol_ext_id.size(); i++) {
            if( mgt_same_and_lower_role ) {
            	if( auth_level > ( (Long)v_rol_auth_level.elementAt(i) ).longValue() ){
            		continue;
            	}
            } else if (mgt_lower_role) {
            	if( auth_level >= ((Long) v_rol_auth_level.elementAt(i)).longValue()){
            		continue;
            	}
            }
			v_auth_ext_id.addElement(v_rol_ext_id.elementAt(i));
		}
		return v_auth_ext_id;
	}    
    
	public static String getAuthRoleAsXML(Connection con, loginProfile prof) throws SQLException {
		AccessControlWZB acl = new AccessControlWZB();
		Hashtable roles = acl.getAllRoleDesc(con, prof.root_ent_id, false);
		Vector v_rol_xml = (Vector) roles.get(AccessControlWZB.ROL_XML);
		Vector v_rol_auth_level = (Vector)roles.get(AccessControlWZB.ROL_AUTH_LEVEL);
		StringBuffer xmlBuf = new StringBuffer(1024);
		String this_rol_xml;
        
		boolean mgt_same_and_lower_role = false;
		boolean mgt_lower_role = false;
		try{
			mgt_same_and_lower_role = true;
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
			mgt_same_and_lower_role = false;
		}
		if(!mgt_same_and_lower_role) {
			try{
				mgt_lower_role = true;
			}catch(Exception e){
				CommonLog.error(e.getMessage(),e);
				mgt_lower_role = false;
			}		
		}
		long auth_level = 0;
		try{
			auth_level = acl.getRoleAuthLevel(con, prof.root_ent_id, prof.current_role);
		}catch(Exception e){
			throw new SQLException("Failed to get rol_auth_level, rol_ext_id = " + prof.current_role);
		}
		        
		xmlBuf.append("<auth_rol_list>");
		if( mgt_same_and_lower_role || mgt_lower_role )
		for(int i=0; i<v_rol_xml.size(); i++) {
            if( mgt_same_and_lower_role ) {
            	if( auth_level > ( (Long)v_rol_auth_level.elementAt(i) ).longValue() && ( (Long)v_rol_auth_level.elementAt(i) ).longValue() != 0){
            		continue;
            	}
            } else if (mgt_lower_role) {
            	if( auth_level >= ((Long) v_rol_auth_level.elementAt(i)).longValue()){
            		continue;
            	}
            }
			this_rol_xml = (String) v_rol_xml.elementAt(i);            
			xmlBuf.append(this_rol_xml).append(dbUtils.NEWL);
			
		}
		xmlBuf.append("</auth_rol_list>");
		return xmlBuf.toString();
	}    
	
	
	public static String DisableRolesXml(Connection con,long root_ent_id)throws SQLException{
					StringBuffer sb = new StringBuffer(64);
					Set roles = getDisableRoles(con,root_ent_id);
					sb.append("<disable_role_list>");
					for(Iterator iter=roles.iterator();iter.hasNext();){
						sb.append("<role id=\"").append(iter.next().toString()).append("\"/>");
					}
					sb.append("</disable_role_list>");
					return sb.toString();
	}
    
    

    public static String urlRedirect(String args, HttpServletRequest request, Hashtable argsTable, String urlEncoding)
        throws Exception {
            
            int index = args.indexOf("?");
            String url = args.substring(0, index);
            int length = args.length();
            args = args.substring(index+1, length);
            return urlRedirect(url, args, request, argsTable, urlEncoding);
    
        }

    
    

    
    /**
    Make a http connection and post the args
    @param url of the http connection
    @param args to be passed
    @param http request
    @return value returned by the http connection
    */
    public static String urlRedirect(String url, String args, HttpServletRequest request, Hashtable argsTable, String urlEncoding)
        throws Exception {


            StringBuffer xml = new StringBuffer();

            try{
                args += "&";
                StringBuffer requestParam = new StringBuffer();
                int index = args.indexOf("&");
                while( index > 0 ) {
                    String element = args.substring(0, index);
                    int subIndex = element.indexOf("=");
                    int strLength = element.length();
                    requestParam.append(URLEncoder.encode(element.substring(0,subIndex)));
                    requestParam.append("=");
                    requestParam.append(URLEncoder.encode(element.substring(subIndex+1,strLength)));
                    requestParam.append("&");
                    args = args.substring(index+1);
                    index = args.indexOf("&");
                }

                if( argsTable != null ) {
                    Enumeration enumeration = argsTable.keys();
                    while(enumeration.hasMoreElements()) {
                        String name = (String)enumeration.nextElement();
                        requestParam.append("&");
                        requestParam.append(cwEncode.encodeURL(name, urlEncoding));
                        requestParam.append("=");
                        requestParam.append(cwEncode.encodeURL((String)argsTable.get(name), urlEncoding));                        
                    }

                }
                if(url != null && url.length() > 0){
                    xml.append( SendHttpRequest.sendUrl(url, requestParam.toString(), null, request, null));
                }

            } catch (Exception e) {
            	CommonLog.error("Thread Error : " + e.getMessage(),e);
            }

            return (xml.toString()).trim();
        }

  


    
    
    /**
    Return true if the user's current role is learner role.  Otherwise, return false <BR>
    */
    public static boolean isUserLrnRole(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
//        AccessControlWZB acl = new AccessControlWZB();
        if(rol_ext_id != null && rol_ext_id.startsWith("NLRN"))
            return true;
        else
            return false;
//        return acl.isUserLrnRole(con, usr_ent_id, rol_ext_id);
    }

    /**
    Get approver roles of user <BR>
    */
    public static String[] getUserApprRole(Connection con, long usr_ent_id) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        return acl.getUserRolesByTargetInd(con, usr_ent_id);
    }
    
    /**
    get the roles by the input target entity types
    @param con Connection to database
    @param root_ent_id organization entity id
    @return Vector contains 2 Vectors in it. The 1st one is the role external id and 
                                             the 2nd one id the role target type
    */
    public static Vector getOrgApprRole(Connection con, long root_ent_id) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        return acl.getOrgApprRole(con, root_ent_id);
    }

    /**
    get the roles by the input target entity types
    @param con Connection to database
    @param root_ent_id organization entity id
    @parma target_ent_types String array of target types
    @return Vector contains 2 Vectors in it. The 1st one is the role external id and 
                                             the 2nd one id the role target type
    */
    public static Vector getOrgRoleByTargetEntType(Connection con, long root_ent_id, String[] target_ent_types) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        return acl.getRolesByTargetEntType(con, root_ent_id, target_ent_types);
    }
    
    /**
    Return true if the user's current role is approver role.  Otherwise, return false <BR>
    */
    public static boolean isUserApprRole(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
        String[] usr_role_lst = getUserApprRole(con, usr_ent_id);
        if (usr_role_lst != null && usr_role_lst.length > 0) {
            for (int i=0; i<usr_role_lst.length; i++) {
                if (usr_role_lst[i].equalsIgnoreCase(rol_ext_id)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public static Vector getRole(Connection con, long root_ent_id, String SQL) throws SQLException {
        Vector v = new Vector();
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, root_ent_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v.addElement(rs.getString("rol_ext_id"));
        }
        stmt.close();
        return v;
    }
    
    private static final String sql_get_rol_by_ent_ind = 
        " Select rol_ext_id From acRole Where " +
        " rol_ste_ent_id = ? ";
    /**
    Get a Vector of rol_ext_id which have rol_target_ent_type is not null if input target_ent_ind is true,
                                     else rol_target_ent_type is null if input target_ent_ind is false
    */
    public static Vector getRoleByTargetEntInd(Connection con, long root_ent_id, boolean target_ent_ind) 
        throws SQLException {
        
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append(sql_get_rol_by_ent_ind);
        if(target_ent_ind) {
            SQLBuf.append(" And rol_target_ent_type is not NULL ");
        } else {
            SQLBuf.append(" And rol_target_ent_type is NULL ");
        }
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, root_ent_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v.addElement(rs.getString("rol_ext_id"));
        }
        stmt.close();
        return v;
    }

    public static long[] vec2longArray(Vector vec) {
        long[] longArray = new long[vec.size()];
        for(int i=0; i<vec.size(); i++)
            longArray[i] = ((Long)vec.elementAt(i)).longValue();
        return longArray;
    }

    public static long[] vec2longArray(long usr_ent_id, Vector v) {
        long[] list = new long[v.size() + 1];
        list[0] = usr_ent_id;
        for(int i=0;i<v.size();i++)
            list[i+1] = ((Long) v.elementAt(i)).longValue();
        return list;
    }

    /**
    Convert an array of string to a string with the format "('id1', 'id2', 'id3')"
    Note that this function will not esc the "'"
    */
    public static String array2SQLList(String[] id) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(");
        if(id!=null) {
            for(int i=0;i<id.length-1;i++){ 
                listBuf.append("'").append(id[i]).append("'").append(",");
            }
            listBuf.append("'").append(id[id.length-1]).append("'");
        }
        listBuf.append(")");    
        return listBuf.toString();
    }

    public static Vector vectorUnion(Vector v1, Vector v2){

        if( v1 == null || v1.isEmpty() )
            return v2;
        if( v2 == null || v2.isEmpty() )
            return v1;

        Vector v = new Vector();
        int max = v1.size() > v2.size() ? v1.size() : v2.size();

        for(int i=0; i<max; i++) {
            if(i<v1.size() && v.indexOf(v1.elementAt(i)) == -1 ) 
                v.addElement(v1.elementAt(i));
            if(i<v2.size() && v.indexOf(v2.elementAt(i)) == -1 )
                v.addElement(v2.elementAt(i));
        }

        return v;
    }
    
    
    public static Vector vectorIntersect(Vector v1, Vector v2){

        if( v1 == null || v1.isEmpty() || v2 == null || v2.isEmpty() )
            return new Vector();
        Vector v = new Vector();    
        if( v1.size() > v2.size() ) {
            for(int i=0; i<v1.size(); i++){
                if( v2.indexOf(v1.elementAt(i)) != -1 ){
                    v.addElement(v1.elementAt(i));
                }
            }
        }else {
            for(int i=0; i<v2.size(); i++){
                if( v1.indexOf(v2.elementAt(i)) != -1 ){
                    v.addElement(v2.elementAt(i));
                }
            }
        }

        return v;
    }

	public static void moveFile(String srcFile, String destFile)
		throws IOException {
    		
			File fromFile = new File(srcFile);
			if( !fromFile.exists() ){
				return;
			}
			File toFile = new File(destFile);
			RandomAccessFile f1 = new RandomAccessFile(fromFile, "r");
			int bufsize = (int) f1.length();
			if ( toFile.exists()) {
				toFile.delete();
				toFile = new File(destFile);
			}
			RandomAccessFile f2 = new RandomAccessFile(toFile, "rw");
			byte[] buffer1 = new byte [bufsize];
			f1.readFully(buffer1, 0, bufsize);
			f2.write(buffer1, 0, bufsize);
			f1.close();
			f2.close();
			fromFile.delete();
			return;
		}
		
	 
		
		private static List getTcAdminRole(Connection con,long root_ent_id){
			AccessControlWZB acl = new AccessControlWZB();
			return acl.getTcOfficerRoles(con,root_ent_id);
		}
		
		public static Set getDisableRoles(Connection con,long root_ent_id)throws SQLException{
			String[] instr = new String[] {"INSTR_1"};;
			List list = getTcAdminRole(con,root_ent_id);
			Set set = new HashSet();
			for(int i=0;i<instr.length;i++){
				set.add(instr[i]);
			}
			for(int i=0,n=list.size();i<n;i++){
				set.add(list.get(i).toString());
			}
			return set;
		}
        public static int get_rol_id(Connection con,String rol_ext_id,long root_ent_id)
            throws SQLException{
            int rol_id = 0;
            PreparedStatement pstmt = con.prepareStatement("select rol_id from acRole where rol_ext_id=?");
            pstmt.setString(1,rol_ext_id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                rol_id = rs.getInt(1);
            }
            cwSQL.cleanUp(rs, pstmt);
            return rol_id;
        }
        
        /**
         * to get the sort sql for current query.
         * @param sortColMap
         * @param sortOrderMap
         * @param sortCol
         * @param sortOrder
         * @param defaultSort
         * @param defaultOrder
         * @return
         * @throws cwException
         */
        public static String getSortSQL(Vector sortColVec, String sortCol, String sortOrder, String defaultSort, String defaultOrder) {
            boolean isVaildSortColVec = true;
            boolean isVaildSortCol = true;
            boolean isVaildSortOrder = true;
            boolean isVaildDefaultSortCol = true;
            
        	if(sortColVec == null || sortColVec.size() == 0) {
        		isVaildSortColVec = false;
            }
            if(sortCol == null || "".equals(sortCol)) {
            	isVaildSortCol = false;
            }
            if(sortOrder == null || "".equals(sortOrder)) {
            	isVaildSortOrder = false;
            }
            
            //default sort column and order
            if(defaultSort == null || "".equals(defaultSort)) {
            	isVaildDefaultSortCol = false;
            }
            if(defaultOrder == null || "".equals(defaultOrder)) {
            	defaultOrder = "asc";
            }
            
            if (isVaildSortColVec && isVaildSortCol && sortColVec.contains(sortCol)) {
				isVaildSortCol = true;
			} else {
				isVaildSortCol = false;
			}
	
			if (isVaildSortOrder && isValidSortOrder(sortOrder)) {
				isVaildSortOrder = true;
			} else {
				isVaildSortOrder = false;
			}
            
            //generate sort sql
			String sortSql = null;
			if(isVaildSortCol || isVaildDefaultSortCol) {
				sortSql = "order by ";
				//sort column
				if(isVaildSortCol) {
					sortSql += sortCol;
				} else if(isVaildDefaultSortCol){
					sortSql += defaultSort;
				}
				sortSql += " ";
				//sort order
				if(isVaildSortOrder) {
					sortSql += sortOrder;
				} else {
					sortSql += defaultOrder;
				}
			}

            return sortSql;
        }
    
        /*
         * check whether order of sort is vaild. 
         */
        public static boolean isValidSortOrder(String sortOrder) {
        	boolean isValidOrder = false;
        	if(sortOrder != null && !"".equals(sortOrder)) {
        		HashMap sortOrderMap = new HashMap();
        		sortOrderMap.put("asc","asc");
        		sortOrderMap.put("desc","desc");
        		if(sortOrderMap.get(sortOrder.toLowerCase()) != null) {
        			isValidOrder = true;
        		}
        	}
        	return isValidOrder;
        }
        
        public static String editxml(Connection con, String xml, long ent_id, long cos_id, String tkh_id) throws qdbException, cwSysMessage {
            Document document = null;
           try {
               document = DocumentHelper.parseText(xml);
           } catch (DocumentException e) {
               CommonLog.error(e.getMessage(),e);
           }                
           Element root=document.getRootElement();    
           root.addAttribute("end_id",new Long(ent_id).toString());
           root.addAttribute("cos_id",new Long(cos_id).toString());
           root.addAttribute("tkh_id",tkh_id);
           //root.addAttribute("mod_id", mod_id);
           dbResource res = new dbResource();
           /*
           for ( Iterator i = root.elementIterator("item"); i.hasNext();) 
           {       
               Element ele = (Element) i.next();
               String id = ele.attributeValue("identifierref");
               if (id != null){
                   res.res_id = Long.parseLong(id);
                   res.get(con);
                   Element link = ele.addElement("link");
                   link.addText(res.res_src_link);
               }
               //ele.getAttributes().getNamedItem("email").getNodeValue();
           }
           */
          /* Element elm = findElement(root, res, mod_id);
           if (elm != null) {
               getElementList(elm,res,con);
           }*/
           getElementList(root,res,con);
           return document.asXML(); 
       }
        
        /** 
         * 递归遍历方法 
         * 
         * @param element 
         * @throws cwSysMessage 
         * @throws qdbException 
         */ 
        public static void getElementList(Element element, dbResource res, Connection con) throws qdbException, cwSysMessage { 
            List elements = element.elements("item"); 
            if (elements.size() == 0) {
                //没有子元素 
                String id = element.attributeValue("identifierref");
                String restype = element.elementText("restype");
                if (id != null && restype != null && restype.equals("SCO")){
                    res.res_id = Long.parseLong(id);
                    res.get(con);
                    Element link = element.addElement("link");
                    link.addText(res.res_src_link);
                    Element statu = element.addElement("statu");
                    statu.addText(res.res_status);

                }
            } else { 
                //有子元素 
                for (Iterator it = elements.iterator(); it.hasNext();) { 
                    Element elem = (Element) it.next(); 
                    //递归遍历 
                    getElementList(elem, res, con); 
                } 
            } 
        }
        
        public static Element findElement(Element element, dbResource res, String res_id) {
            List elements = element.elements("item");
            Element sconode = null;
            if (elements.size() > 0) {
                for (Iterator it = elements.iterator(); it.hasNext();) { 
                    Element el = null;
                    Element elem = (Element) it.next(); 
                    String itmtype = elem.elementText("itemtype");
                    String id = elem.attributeValue("identifier");
                    if (itmtype != null && itmtype.equalsIgnoreCase("FDR") && id.equalsIgnoreCase("scoroot_"+res_id)) {
                        el = elem;
                        sconode = elem;
                    } else {
                        el = findElement(elem, res, res_id);
                    }
                    if (el == null) {
                        element.remove(elem);
                    }
                } 
            }
            return sconode;
        }
        
    /**
     * transfer scorm2004 time interval(P[yY][mM][dD][T[hH][mM][s[.s]S]) into h:m:s format.
     * this method only process the H,M,S part, Y,M,D part is ignored. 
     * @param iTime
     * @return
     */
	public static String transSco2004TimeInterval(String iTime) {
		// P[yY][mM][dD][T[hH][mM][s[.s]S]
		// P1Y3M2DT3H
		// PT3H5M
		String result = "";
		int[] ioArray = new int[7];
		String mInitArray[];
		String mTempArray2[] = { "0", "0", "0" };
		String mDate = "0";
		String mTime = "0";

		// make sure the string is not null
		if (iTime == null) {
			return result;
		}

		// make sure that the string has the right format to split
		if ((iTime.length() == 1) || (iTime.indexOf("P") == -1)) {
			return result;
		}

		try {
			mInitArray = iTime.split("P");

			// T is present so split into day and time part
			// when "P" is first character in string, rest of string goes in
			// array index 1
			if (mInitArray[1].indexOf("T") != -1) {
				mTempArray2 = mInitArray[1].split("T");
				mDate = mTempArray2[0];
				mTime = mTempArray2[1];
			} else {
				mDate = mInitArray[1];
			}

			// Y is present so get year
			if (mDate.indexOf("Y") != -1) {
				mInitArray = mDate.split("Y");
				Integer tempInt = new Integer(mInitArray[0]);
				ioArray[0] = tempInt.intValue();
			} else {
				mInitArray[1] = mDate;
			}

			// M is present so get month
			if (mDate.indexOf("M") != -1) {
				mTempArray2 = mInitArray[1].split("M");
				Integer tempInt = new Integer(mTempArray2[0]);
				ioArray[1] = tempInt.intValue();
			} else {
				if (mInitArray.length != 2) {
					mTempArray2[1] = "";
				} else {
					mTempArray2[1] = mInitArray[1];
				}
			}

			// D is present so get day
			if (mDate.indexOf("D") != -1) {
				mInitArray = mTempArray2[1].split("D");
				Integer tempInt = new Integer(mInitArray[0]);
				ioArray[2] = tempInt.intValue();
			} else {
				mInitArray = new String[2];
			}

			// if string has time portion
			if (!mTime.equals("0")) {
				// Reset array
				mInitArray = new String[2];

				// H is present so get hour
				if (mTime.indexOf("H") != -1) {
					mInitArray = mTime.split("H");
					Integer tempInt = new Integer(mInitArray[0]);
					ioArray[3] = tempInt.intValue();
				} else {
					mInitArray[1] = mTime;
				}

				// M is present so get minute
				if (mTime.indexOf("M") != -1) {
					mTempArray2 = mInitArray[1].split("M");
					Integer tempInt = new Integer(mTempArray2[0]);
					ioArray[4] = tempInt.intValue();
				} else {
					if (mInitArray.length != 2) {
						mTempArray2[1] = "";
					} else {
						mTempArray2[1] = mInitArray[1];
					}
				}

				// S is present so get seconds
				if (mTime.indexOf("S") != -1) {
					mInitArray = mTempArray2[1].split("S");

					if (mTime.indexOf(".") != -1) {
						// split requires this regular expression for "."
						mTempArray2 = mInitArray[0].split("[.]");

						// correct for case such as ".2"
						if (mTempArray2[1].length() == 1) {
							mTempArray2[1] = mTempArray2[1] + "0";
						}

						Integer tempInt2 = new Integer(mTempArray2[1]);
						ioArray[6] = tempInt2.intValue();
						Integer tempInt = new Integer(mTempArray2[0]);
						ioArray[5] = tempInt.intValue();
					} else {
						Integer tempInt = new Integer(mInitArray[0]);
						ioArray[5] = tempInt.intValue();
					}
				}
			}
		} catch (NumberFormatException nfe) {
			// Do Nothing
		}
		result = ioArray[3] + ":" + ioArray[4] + ":" + ioArray[5];
		return result;
	}
}

