package com.cw.wizbank.batch.ansi2utf8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ANSI2UTF8 {
	private static final byte[] BOM_UTF8 = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
	
	public static void traverse(File path) throws Exception {
        if (path == null) {
            return;
        }
        if (path.isDirectory() && !path.getName().endsWith(".svn")) {
            String[] files = path.list();
            for (int i = 0; i < files.length; i++) {
                traverse(new File(path, files[i]));
            }
        } else {
            if (path.getAbsolutePath().endsWith(".java")) {
            	//boolean foundBOM = isUTF8File(path);
            	//if(!foundBOM) {
	            	StringBuffer sb = read(path);
	                File file = new File(path.getAbsolutePath());
	                FileOutputStream fop = new FileOutputStream(file);
	                //fop.write(BOM_UTF8);
	                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fop, "UTF-8"));
	                out.write(sb.toString());
	                out.flush();
	                out.close();
            	//}
            }
        }
    }

    public static StringBuffer read(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        reader.close();
        return sb;
    }
    
    public static boolean isUTF8File(File file) throws IOException {
    	FileInputStream inStream = new FileInputStream(file);

        boolean foundBOM = true;
        byte[] inBOM = new byte[BOM_UTF8.length];
        if (inStream.read(inBOM) == BOM_UTF8.length) {
            for (int i = 0; i < BOM_UTF8.length; i++) {
                if (inBOM[i] != BOM_UTF8[i]) {
                    foundBOM = false;
                    break;
                }
            }
        }
        else {
            foundBOM = false;
        }
        
        return foundBOM;
    }

    public static void main(String args[]) throws Exception {
    	String outputDir = "";
    	for (int i = 0; i < args.length; i++) {
			//System.out.println(args[i]);
			if (args[i].charAt(0) == '-') {
				
				if (args[i].equals("-o")) {
					outputDir = args[++i];
				}
			}
		}
    	File path = new File(outputDir);
    	ANSI2UTF8.traverse(path);
    }

}
