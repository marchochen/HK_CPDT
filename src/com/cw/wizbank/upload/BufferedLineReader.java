package com.cw.wizbank.upload;


import java.io.Reader;
import java.io.IOException;
import java.io.BufferedReader; 

/**
 * @author wailun
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BufferedLineReader extends BufferedReader {
	
	//line delimiters used in the input file (only valid for MSDOS)
	private static final char lineFeed = '\n';
	private static final char carriageReturn  = '\r';
	
	public BufferedLineReader(Reader in) {
		super(in);
	}

	
	public String readLine() throws IOException {
		try{
			StringBuffer str = new StringBuffer(1024);
			int temp;
			char inChar1;
			char inChar2;
			boolean eof = true;
			while( (temp = read()) != -1 ) {
				eof = false;
				inChar1 = (char) temp;
				if( inChar1 == carriageReturn ) {
					temp = read();
					inChar2 = (char) temp;
					if( temp == -1 ) {
						return str.toString();
					} else if( inChar2 == lineFeed ) {
						return str.toString();
					} else {
						str.append(inChar1).append(inChar2);
					}
				} else {
					str.append(inChar1);
				}
			}
			if( eof ) {
				return null;
			} else {
				return str.toString();
			}
		}catch(Exception e) {
			throw new IOException(e.getMessage());
		}
	}
	
}
