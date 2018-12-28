package com.cw.wizbank.qdb;

import java.io.*;

public class qdbMailSender {
    private final String NEWL_ = System.getProperty("line.separator");
    // the gap in the email template should be $data_?_ where ? is any number
    private final String GAP1_ = "$data_";
    private final String GAP2_ = "_";
    // the postman!!!
    private qdbMailman mailer_;
    
    public qdbMailSender(String mailServer) {
        mailer_ = new qdbMailman();
        mailer_.setServer(mailServer);
    }
    public qdbMailSender(String mailServer, String enc, int type) {
        mailer_ = new qdbMailman();
        mailer_.setServer(mailServer);
        mailer_.setType(type);
        mailer_.setEnc(enc);
    }

    public void send(String toAddress, String fromAddress, String body) throws IOException {
        mailer_.setReceiver(toAddress);
        mailer_.setSender(fromAddress);   
        mailer_.setReplier(fromAddress);
        mailer_.setBody(body);
        mailer_.send();
    }    

    public void send(String toAddress, String fromAddress, String templateName, String templateBody, String content[]) throws IOException {
        if (content != null)
            for (int i = 0; i < content.length; i++)
                templateBody = substituteSrc(templateBody, getGap(i+1), content[i]);
                
        mailer_.setReceiver(toAddress);
        mailer_.setSender(fromAddress);   
        mailer_.setReplier(fromAddress);
        mailer_.setBody(templateBody);
        mailer_.send();
    }     

                    
    public void sendWithTemplate(String toAddress, String fromAddress, String templateName, String templateBody, String content[]) throws IOException {
        if (content != null)
            for (int i = 0; i < content.length; i++)
                templateBody = substituteSrc(templateBody, getGap(i+1), content[i]);
        mailer_.setReceiver(toAddress);
        mailer_.setSender(fromAddress);   
        mailer_.setReplier(fromAddress);
        mailer_.setBody(templateBody);
        mailer_.send();
    }    
   
    public void sendWithTemplate(String toAddress, String fromAddress, String templateName, String content[]) throws IOException {
        String templateBody = getTemplateSrc(templateName);         
        send(toAddress, fromAddress, templateName, templateBody, content);
    }

    
    public void sendWithTemplate(String[] fromAddress, String[] toAddress, String[] ccAddress, String[] bccAddress, String[] repAddress, String templateName, String[] content, String UserName , String PassWord, boolean AuthEnabled) throws IOException {
        String templateBody = getTemplateSrc(templateName);        
        if (content != null)
            for (int i = 0; i < content.length; i++)
                templateBody = substituteSrc(templateBody, getGap(i+1), content[i]);
        
        int index = templateBody.indexOf('\n');
        
        String mailBody = templateBody.substring(index, templateBody.length());
        String mailSubject = templateBody.substring(0, index - 1);
        if(fromAddress != null)
            mailer_.setSender(fromAddress);
        if(toAddress != null)
            mailer_.setReceiver(toAddress);
        if(ccAddress != null)
            mailer_.setCC(ccAddress);
        if(bccAddress != null)
            mailer_.setBCC(bccAddress);
        if(repAddress != null)
            mailer_.setReplier(repAddress);
        mailer_.setSubject(mailSubject);
        mailer_.setBody(mailBody);
        mailer_.sendMail(UserName ,PassWord, AuthEnabled);
        return;
    }



    // read in the email template file
    public String getTemplateSrc(String templateName) throws IOException {
        String outSource = "";
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(templateName));            
            String inline;
            
            while ((inline = in.readLine()) != null) {
                outSource += inline + NEWL_;
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new IOException("template error:" + e.getMessage() + " not found.");
        } catch (IOException e) {
            throw new IOException("read template error:" + e.getMessage());
        }
        
        return outSource;
    }
    
    // substitute old string by new string in workString
    public String substituteSrc(String workString, String oldStr, String newStr) {
        int oldStrLen = oldStr.length();
        int newStrLen = newStr.length();
        String tempString = "";
          
        int i = 0, j = 0;
        while (j > -1) {
            j = workString.indexOf(oldStr, i);
            if (j > -1) {
                tempString = workString.substring(0, j) + newStr + workString.substring(j+oldStrLen);
                workString = tempString;
                i = j + newStrLen;
            }
        }
        
        return workString;
    }
    
    private String getGap(int order) {
        return GAP1_ + String.valueOf(order) + GAP2_;
    }
}