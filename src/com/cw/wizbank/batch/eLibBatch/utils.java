package com.cw.wizbank.batch.eLibBatch;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.Date;

public class utils{

    private final static String logFilename = "log";
    private final static String logFilenameExt = "txt";

    public static File getLogFile(String logFolder, Timestamp curTime)
        throws IOException{

            Calendar cal = Calendar.getInstance();
            cal.setTime(curTime);
            String subFolder = cal.get(Calendar.YEAR)
                             + "-"
                             + (cal.get(Calendar.MONTH) + 1)
                             + "-"
                             + cal.get(Calendar.DATE);

            File folder = new File(logFolder, subFolder);
            if( !folder.exists() )
                folder.mkdir();               
            int index = 1;
            File logFile = new File(folder, logFilename + "_" + Integer.toString(index) + "." + logFilenameExt );
            while( logFile.exists() ){
                index++;
                logFile = new File(folder, logFilename + "_" + Integer.toString(index) + "." + logFilenameExt );
            }
            return logFile;
        }

    public static String[] vector2StrArray(Vector vec){

        if( vec == null || vec.isEmpty() )
            return new String[0];

        String[] str = new String[vec.size()];
        for(int i=0; i<str.length; i++)
            str[i] = ((Long)vec.elementAt(i)).toString();

        return str;

    }



    public static Timestamp getDateBefore(Timestamp timestamp, int dateBefore){
        if( timestamp == null )
            return null;
        Date date = new Date(timestamp.getTime());
        date.setDate(date.getDate() - dateBefore);
        return new Timestamp(date.getTime());

    }


    public static Timestamp getDateAfter(Timestamp timestamp, int dateAfter){
        dateAfter *= -1;
        return getDateBefore(timestamp, dateAfter);
    }


    public static Timestamp getDateStart(Timestamp timestamp){
        if( timestamp == null )
            return null;
        return changeTimestampHHMMSS(timestamp, 0, 0, 0);
    }

    public static Timestamp getDateEnd(Timestamp timestamp){
        if( timestamp == null )
            return null;
        return changeTimestampHHMMSS(timestamp, 23, 59, 59);
    }

    public static Timestamp changeTimestampHHMMSS(Timestamp timestamp, int hh, int mm, int ss) {
        if( timestamp == null )
            return null;
        Date date = new Date(timestamp.getTime());
        date.setHours(hh);
        date.setMinutes(mm);
        date.setSeconds(ss);
        return new Timestamp(date.getTime());
    }

    public static String[] split2Array(String in, String delimiter) {

        Vector q = new Vector();
        if( in == null || in.length() == 0 ) {
            return new String[0];
        }

        int pos =0;
        pos = in.indexOf(delimiter);

        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos);
            }
            q.addElement(val.trim());
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.addElement(in.trim());
        }

        String[] str = new String[q.size()];
        for(int i=0; i< str.length; i++){
            str[i] = (String)q.elementAt(i);
        }
        return str;


    }


    public static String vector2list(Vector idVec)
    {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(");

        if (idVec != null) {
            for(int i=0;i<idVec.size();i++){
                if (i > 0)
                    listBuf.append(",");

                listBuf.append(((Long) idVec.elementAt(i)).longValue());
            }
        }
        listBuf.append(")");
        return listBuf.toString();
    }
}