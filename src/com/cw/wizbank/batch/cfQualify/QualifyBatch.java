package com.cw.wizbank.batch.cfQualify;

import java.sql.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.cf.*;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.batch.batchUtil.*;
import com.cwn.wizbank.utils.CommonLog;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class QualifyBatch {
    //
    private Connection con;
    private static final String CTF_STATUS_OFF = "OFF";
    private static final String CTF_STATUS_ON = "ON";
    public QualifyBatch() {

    }

    //
        // for item linkage
    public boolean isQualified(int ctf_id, int usr_ent_id)
           throws SQLException ,cwException {
        boolean isQualified = false;
        try {

            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ctf_link);
            int count = 0;
            stmt.setInt(++count,ctf_id);
            ResultSet rs = stmt.executeQuery();

            String ctf_link = null;
            String className = null;
            CFCertificateLink aeClass;
            String methodName = "isQualified";
            Class[] parameterTypes = {Integer.TYPE};
            Object[] parameters = {new Integer(usr_ent_id)};
            Class[] parameterTypes_constructor = {Class.forName("java.sql.Connection")};
            Object[] parameters_constructor = {con};
            while (rs.next()) {
                ctf_link = rs.getString("ctf_link");
                if (ctf_link != null) {
                    className = ctf_link;
//                    System.out.println("className:"+className);
                    aeClass = (CFCertificateLink)Class.forName(className).getConstructor(parameterTypes_constructor).newInstance(parameters_constructor);
//                    isQualified = (Boolean)aeClass.getClass().getDeclaredMethod(methodName,parameterTypes).invoke(aeClass,parameters);
                    isQualified = aeClass.isQualified(ctf_id, usr_ent_id);
//                    System.out.println("in method isQualified= "+isQualified);
                }
            }
            stmt.close();
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
        }
        return isQualified;
    }
    //
    public void setQualificationInd(int ctf_id, int lrn_ent_id, boolean qualification)
           throws SQLException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        cf.setQualificationInd(ctf_id, lrn_ent_id, qualification);

        return;
    }
    //
    public boolean getQualificationInd(int ctf_id, int lrn_ent_id)
           throws SQLException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        return cf.getQualificationInd(ctf_id, lrn_ent_id);
    }
    //
    public static void main(String[] args) {
        //

        try {
            QualifyBatch qb = new QualifyBatch();

            String usr_id = null;
            if ((args == null)||(args.length == 0)) {
                throw new Exception("You Must Input the ini file");
            } else {
                qb.con = BatchUtils.openDB(args[0]);
                //for test
                //get con
//                CFUnitTest un = new CFUnitTest();
//                qb.con = un.getConnection();

                PreparedStatement stmt = qb.con.prepareStatement(SqlStatements.sql_get_qualify_batch);
                int count = 0;
                stmt.setString(++count,CTF_STATUS_ON);
                ResultSet rs = stmt.executeQuery();
                int cfn_ctf_id;
                int cfn_ent_id;
                while (rs.next()) {
                    cfn_ctf_id = rs.getInt("cfn_ctf_id");
                    cfn_ent_id = rs.getInt("cfn_ent_id");
//                    System.out.println("cfn_ctf_id= "+cfn_ctf_id);
//                    System.out.println("cfn_ent_id= "+cfn_ent_id);
//                    System.out.println("isQualified= "+qb.isQualified(cfn_ctf_id,cfn_ent_id));
                    qb.setQualificationInd(cfn_ctf_id,cfn_ent_id,qb.isQualified(cfn_ctf_id,cfn_ent_id));
                }
                stmt.close();
                qb.con.commit();
            }

        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
        }
    }
}