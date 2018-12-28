package com.cw.wizbank.batch.eLibBatch;

import java.io.*;
import java.sql.*;

import javax.naming.NamingException;

import com.cw.wizbank.util.*;
import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cwn.wizbank.utils.CommonLog;

public class eLibraryApp{

    private String iniFile = null;        //Default ini. filename
    private String lastActionFile = ".." + File.separator + "config" + File.separator + "action_track.dat"; //File store the last action timestmp
    private Connection con = null;
    private int numOfStage = 0;
    
    protected static cwIniFile cwIni = null;
    protected static String enc = "ISO-8859-1";
    protected static long siteId;
    protected static Timestamp lastActionTimestamp;
    protected static Timestamp curTime;
    protected static boolean debugMode = false;
    protected static boolean sendXML = false;
    protected static String defaultMailAccount = null;
    protected static String mailContentType = "text/html";
    protected static String appRoot = null;

    protected static PrintWriter logWriter = null;

    private static final String MSG_INVALID_INPUT_ARGV = "Usage: eLibraryApp -inifile filename ";

    public static void main(String[] argv) throws Exception{
        eLibraryApp app = new eLibraryApp();

        try{
            for (int i = 0; i < argv.length; i++) {
                if (argv[i].charAt(0) == '-') {
                    if (argv[i].equalsIgnoreCase("-inifile")){
                        app.iniFile = argv[++i];
                        //app.cwIni = new cwIniFile(app.iniFile);
                    }else if( argv[i].equalsIgnoreCase("-encoding") ) {
                        i++;
                        eLibraryApp.enc = argv[i];
                    }else if( argv[i].equalsIgnoreCase("-debug") ) {
                        i++;
                        if( argv[i] != null && argv[i].equalsIgnoreCase("true") )
                            eLibraryApp.debugMode = true;
                    }else if( argv[i].equalsIgnoreCase("-lastActionTimestamp") ){
                        i++;
                        if( argv[i] != null && argv[i].length() > 0 )
                            eLibraryApp.lastActionTimestamp = Timestamp.valueOf(argv[i]);
                    }else if( argv[i].equalsIgnoreCase("-sendXML") ){
                        i++;
                        if( argv[i] != null && argv[i].length() > 0 )
                            eLibraryApp.sendXML = true;
                    }else if( argv[i].equalsIgnoreCase("-defaultMailAccount") ){
                        i++;
                        if( argv[i] != null && argv[i].length() > 0 )
                            eLibraryApp.defaultMailAccount = argv[i];
                    }else if( argv[i].equalsIgnoreCase("-mailContentType") ){
                        i++;
                        if( argv[i] != null && argv[i].length() > 0 )
                            eLibraryApp.mailContentType = argv[i];
                    }else if( argv[i].equalsIgnoreCase("-approot") ){
                        i++;
                        if( argv[i] != null && argv[i].length() > 0 )
                            eLibraryApp.appRoot = argv[i];
                    }else{
                        throw new IllegalArgumentException("Invalid argument:" +  argv[i] + cwUtils.NEWL + MSG_INVALID_INPUT_ARGV);
                    }
                }else{
                    throw new IllegalArgumentException("Invalid argument:" +  argv[i] + cwUtils.NEWL + MSG_INVALID_INPUT_ARGV);
                }
            }
            //app.cwIni = new cwIniFile(app.iniFile, eLibraryApp.enc);
        }catch (Exception e){
            throw new IllegalArgumentException(MSG_INVALID_INPUT_ARGV);
        }

        try{
            app.init();
        }catch (Exception e){
            CommonLog.error("Failed to init. the application. "+e.getMessage(),e);
            throw new cwException();
        }

        try{
            app.action();
        }catch(Exception e){
            CommonLog.error("Failed to run the application. "+e.getMessage(),e);
            throw new cwException();
        }

        try{
            app.saveLastActionTimestamp();
        }catch(Exception e){
            CommonLog.error("Failed to mark the last action timestamp. "+e.getMessage(),e);
            throw new cwException();
        }

        return;
    }

    private void init()
        throws IOException, cwException, SQLException, NamingException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        if( cwIni == null ){
            //this.cwIni = new cwIniFile(this.iniFile,eLibraryApp.enc);
            this.cwIni = new cwIniFile(this.iniFile);
        }

        this.con = BatchUtils.openDB(this.appRoot);
        this.curTime = cwSQL.getTime(con);
        this.siteId = BatchUtils.getSiteId(this.iniFile);
        if( this.lastActionTimestamp == null )
            this.lastActionTimestamp = getLastActionTimestamp();
        File logFile = utils.getLogFile(this.cwIni.getValue("LOG_FOLDER"), cwSQL.getTime(con));
        this.logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
        return;
    }


    private void action()
        throws IOException, cwException, SQLException {

            eLibrary eLib = new eLibrary();
            eLib.con = this.con;
            
            this.logWriter.println("Program Start");
            this.logWriter.flush();

            try{
                eLib.eLibMailer = new eLibraryMailer(this.cwIni.getValue("MAIL_SERVER"), this.cwIni.getValue("ENCODING"), this.mailContentType);
            }catch(Exception e){
                CommonLog.error("Init. Mailer Failure. "+e.getMessage(),e);;
                System.exit(0);
            }
            

            if( eLibraryApp.debugMode ) {
            	CommonLog.debug(" =============================== Started ===================== ");
            }

            eLib.sendMessage();

            if( eLibraryApp.debugMode ) {
            	CommonLog.debug(" =============================== Finished ===================== ");
            }


            this.logWriter.println("Program end");
            this.logWriter.flush();

            return;

        }




    private void saveLastActionTimestamp()
        throws IOException, SQLException {

            File file = new File(lastActionFile);
            FileOutputStream fOut = new FileOutputStream(file.getAbsolutePath(), false);
            fOut.write((utils.getDateEnd(this.curTime)).toString().getBytes());
            fOut.flush();
            fOut.close();

            return;

        }

    private Timestamp getLastActionTimestamp()
        throws IOException {

            File file = new File(lastActionFile);
            if( !file.exists() )
                return null;
            BufferedReader bIn = new BufferedReader(new FileReader(file));
            String buf = null;
            if( (buf = bIn.readLine()) != null ) {
                lastActionTimestamp = Timestamp.valueOf(buf);
            }else {
                lastActionTimestamp = null;
            }
            return lastActionTimestamp;

        }

}