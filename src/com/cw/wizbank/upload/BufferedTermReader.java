package com.cw.wizbank.upload;

import java.io.*;

// extending BufferedReader, this class expects to read from a Reader with delimited text
// 2000.08.13 wai
public class BufferedTermReader extends BufferedReader {
    // line delimiters used in the input file (only valid for MSDOS)
    private final char lineDelimiter1_ = '\r';
    private final char lineDelimiter2_ = '\n';
    // system specific line break
    private final String newLine_ = System.getProperty("line.separator");
    
    private char termDelimiter;
    private boolean lineBreak;
    private StringBuffer term;
    
    BufferedTermReader(Reader dataSrc, char inDelimiter) {
        super(dataSrc);
        termDelimiter = inDelimiter;
        lineBreak = false;
        term = new StringBuffer();
    }
    
    // return whether end-of-line is reached, this flag will be set after a term is found
    public boolean eol() {
        return lineBreak;
    }
    
    // return a term from the input file, null if end of file is reached
    public String readTerm() throws IOException {
        int temp;
        char inChar;
        String outTerm = null;
        term.setLength(0);
        
        while ((temp = read()) != -1) {
            inChar = (char)temp;
            if (inChar == termDelimiter || inChar == lineDelimiter1_) {
                // term break
                outTerm = term.toString();
                // determine whether it is at end of line
                if (inChar == lineDelimiter1_) {
                    lineBreak = true;
                    // there is a second character within a line break (for MSDOS only)
                    temp = read();
                } else {
                    lineBreak = false;
                }
                break;
            } else {
                // delimiter not yet reached, keep on constructing the term
                // append with the system specific line break instead of the line delimiter
                if (inChar == lineDelimiter2_)
                    term.append(newLine_);
                else
                    term.append(inChar);
            }
        }
        
        return outTerm;
    }
}