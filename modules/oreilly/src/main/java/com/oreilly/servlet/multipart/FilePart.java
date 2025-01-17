// Copyright (C) 1999 by Jason Hunter <jhunter@acm.org>.  All rights reserved.
// Use of this class is limited.  Please see the LICENSE for more information.

package com.oreilly.servlet.multipart;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletInputStream;

import com.cw.wizbank.util.OutputStreamListener;

/**
 * A <code>FilePart</code> is an upload part which represents a 
 * <code>INPUT TYPE="file"</code> form parameter.
 * 
 * @author Geoff Soutter
 */
public class FilePart extends Part {
  
  /** "file system" name of the file  */
  private String fileName;     
  
  /** content type of the file */
  private String contentType;   
  
  /** input stream containing file data */
  private PartInputStream partInput;  
  
  private OutputStreamListener listener;
  /**
   * Construct a file part; this is called by the parser.
   * 
   * @param name the name of the parameter.
   * @param in the servlet input stream to read the file from.
   * @param boundary the MIME boundary that delimits the end of file.
   * @param contentType the content type of the file provided in the 
   * MIME header.
   * @param fileName the file system name of the file provided in the 
   * MIME header.
   * 
   * @exception IOException    if an input or output exception has occurred.
   */
  FilePart(String name, ServletInputStream in, String boundary,
           String contentType, String fileName) throws IOException {
    super(name);
    this.fileName = fileName;
    this.contentType = contentType;
    partInput = new PartInputStream(in, boundary);
  }
  
  /**
   * Returns the name that the file was stored with on the remote system, 
   * or <code>null</code> if the user didn't enter a file to be uploaded. 
   * Note: this is not the same as the name of the form parameter used to 
   * transmit the file; that is available from the <code>getName</code>
   * method.
   * 
   * @return name of file uploaded or <code>null</code>.
   * 
   * @see Part#getName()
   */
  public String getFileName() {
    return fileName;
  }

/* ============================================== */
/* WaiLun Set filename  */
  public void setFileName(String filename) {
    fileName = filename;
    return;
  }
/* ============================================== */  


  public OutputStreamListener getListener() {
    return listener;
}

public void setListener(OutputStreamListener listener) {
    this.listener = listener;
}

/** 
   * Returns the content type of the file data contained within.
   * 
   * @return content type of the file data.
   */
  public String getContentType() {
    return contentType;
  }
  
  /**
   * Returns an input stream which contains the contents of the
   * file supplied. If the user didn't enter a file to upload
   * there will be <code>0</code> bytes in the input stream.
   * 
   * @return an input stream containing contents of file.
   */
  public InputStream getInputStream() {
    return partInput;
  }

  /**
   * Write this file part to a file or directory. If the user 
   * supplied a file, we write it to that file, and if they supplied
   * a directory, we write it to that directory with the filename
   * that accompanied it. If this part doesn't contain a file this
   * method does nothing.
   *
   * @return number of bytes written
   * @exception IOException    if an input or output exception has occurred.
   */
  public long writeTo(File fileOrDirectory) throws IOException {
    long written = 0;
    
    OutputStream fileOut = null;
    try {
      // Only do something if this part contains a file
      if (fileName != null) {
        // Check if user supplied directory
        File file;
        if (fileOrDirectory.isDirectory()) {
          // Write it to that dir the user supplied, 
          // with the filename it arrived with
          file = new File(fileOrDirectory, fileName);
        }
        else {
          // Write it to the file the user supplied,
          // ignoring the filename it arrived with
          file = fileOrDirectory;
        }
        fileOut = new BufferedOutputStream(new FileOutputStream(file));
        written = write(fileOut);
      }
    }
    finally {
      if (fileOut != null) fileOut.close();
    }
    return written;
  }

  /**
   * Write this file part to the given output stream. If this part doesn't 
   * contain a file this method does nothing.
   *
   * @return number of bytes written.
   * @exception IOException    if an input or output exception has occurred.
   */
  public long writeTo(OutputStream out) throws IOException {
    long size=0;
    // Only do something if this part contains a file
    if (fileName != null) {
      // Write it out
      write( out );
    }
    return size;
  }

  /**
   * Internal method to write this file part; doesn't check to see
   * if it has contents first.
   *
   * @return number of bytes written.
   * @exception IOException    if an input or output exception has occurred.
   */
  long write(OutputStream out) throws IOException {
    // decode macbinary if this was sent
    if (contentType.equals("application/x-macbinary")) {
      out = new MacBinaryDecoderOutputStream(out);
    }
    long size=0;
    int read;
    byte[] buf = new byte[8 * 1024];
    while((read = partInput.read(buf)) != -1) {
      out.write(buf, 0, read);
      size += read;
      if (listener != null) listener.bytesRead(read);
    }
    return size;
  }
  
  /**
   * Returns <code>true</code> to indicate this part is a file.
   * 
   * @return true.
   */
  public boolean isFile() {
    return true;
  }
}
