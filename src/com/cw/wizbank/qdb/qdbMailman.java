package com.cw.wizbank.qdb;

import java.io.*;
import java.util.*;
import sun.net.smtp.SmtpClient;
import javax.mail.*;
import javax.mail.internet.*;

import com.cw.wizbank.message.MessageAuthenticator;

// low level mail sending class (2000.05.05 by wai)
// the CC does not seem to be working, so it is commented out here
public class qdbMailman {
    private final String myName_ = "wqMailman 1.1 (Java)";
    public final static int HTML            =   1;
    public final static int PLAIN_TEXT      =   2;
    
    private String mailServer_;
    private String mailSender_;
    private InternetAddress sender_;
    private String mailReceiver_;
    private InternetAddress receiver_;
    private String mailCC_;
    private InternetAddress cc_;
    private String mailBCC_;
    private InternetAddress bcc_;
    private String mailReplier_;
    private InternetAddress[] replier_;
    private String mailSubject_;
    private String mailBody_;
    private int mailType_;
    private String mailEnc_;
    
    qdbMailman() {
        mailServer_   = null;
        mailSender_   = null;
        mailReceiver_ = null;
        mailCC_       = null;
        mailBCC_      = null;
        mailReplier_  = null;
        mailSubject_  = null;
        mailBody_     = null;
        mailType_     = 1;
        mailEnc_      = "ISO-885901";
        
        sender_       = null;
        receiver_     = null;
        cc_           = null;
        bcc_          = null;
    }
    public void setType(int mailType){
        mailType_ = mailType;
    }
    public void setEnc(String mailEnc){
        mailEnc_ = mailEnc;
    }
    
    public void setServer(String inServer) {
        mailServer_ = inServer;
    }
    public void setSender(String inSender) {
        mailSender_ = inSender;
    }
    public void setSender(String[] inSender) throws IOException{
        try{
            if( inSender[1] != null )
                sender_ = new InternetAddress(inSender[0], inSender[1]);
            else
                sender_ = new InternetAddress(inSender[0]);
        }catch(UnsupportedEncodingException e){
            throw new IOException(e.getMessage());
        }catch(AddressException e) {
            throw new IOException(e.getMessage());
        }
    }
    
    public void setReceiver(String inReceiver) {
        mailReceiver_ = inReceiver;
    }
    public void setReceiver(String[] inReceiver) throws IOException {
        try{
            if( inReceiver[1] != null )
                receiver_ = new InternetAddress(inReceiver[0], inReceiver[1]);
            else
                receiver_ = new InternetAddress(inReceiver[0]);
        }catch(UnsupportedEncodingException e){
            throw new IOException(e.getMessage());
        }catch(AddressException e) {
            throw new IOException(e.getMessage());
        }
    }
    
    public void setCC(String inCC) {
        mailCC_ = inCC;
    }
    public void setCC(String[] inCC) throws IOException {
        try{
            if( inCC[1] != null )
                cc_ = new InternetAddress(inCC[0], inCC[1]);
            else
                cc_ = new InternetAddress(inCC[0]);
        }catch(UnsupportedEncodingException e){
            throw new IOException(e.getMessage());
        }catch(AddressException e) {
            throw new IOException(e.getMessage());
        }
        
    }
    
    public void setBCC(String inBCC){
            mailBCC_ = inBCC;
    }
    public void setBCC(String[] inBCC) throws IOException {
        try{
            if( inBCC[1] != null )
                bcc_ = new InternetAddress(inBCC[0], inBCC[1]);
            else
                bcc_ = new InternetAddress(inBCC[0]);
        }catch(UnsupportedEncodingException e){
            throw new IOException(e.getMessage());
        }catch(AddressException e) {
            throw new IOException(e.getMessage());
        }
    }
    
    public void setReplier(String inReplier) {
        mailReplier_ = inReplier;
    }
    public void setReplier(String[] inReplier) throws IOException {
        try{
            replier_ = new InternetAddress[1];
            if( inReplier[1] != null )
                replier_[0] = new InternetAddress(inReplier[0], inReplier[1]);
            else
                replier_[0] = new InternetAddress(inReplier[0]);
        }catch(UnsupportedEncodingException e){
            throw new IOException(e.getMessage());
        }catch(AddressException e) {
            throw new IOException(e.getMessage());
        }
    }
    
    public void setSubject(String inSubject) {
        mailSubject_ = inSubject;
    }
    public void setBody(String inBody) {
        mailBody_ = inBody;
    }
    
    public void send() throws IOException {
        SmtpClient mailer = new SmtpClient(mailServer_);
        mailer.from(mailSender_);
        mailer.to(mailReceiver_);
        PrintStream mailContent = mailer.startMessage();
        mailContent.println("X-Mailer: " + myName_);
        
        if(mailSender_ != null && mailSender_.length() > 0)
            mailContent.println("From: " + mailSender_);
        if (mailReceiver_ != null && mailReceiver_.length() > 0)
            mailContent.println("To: " + mailReceiver_);
        if (mailCC_ != null && mailCC_.length() > 0)
            mailContent.println("Cc: " + mailCC_);
        if (mailReplier_ != null && mailReplier_.length() > 0)
            mailContent.println("Reply-To: " + mailReplier_);
        if (mailSubject_ != null && mailSubject_.length() > 0)
            mailContent.println("Subject: " + mailSubject_);
        if (mailBody_ != null && mailBody_.length() > 0)
            mailContent.println(mailBody_);

        mailer.closeServer();
    }
    
    public void sendMail(String UserName , String PassWord, boolean AuthEnabled) throws IOException {
        Properties props = new Properties();
        MessageAuthenticator auth = new MessageAuthenticator(UserName , PassWord);
        props.put("mail.smtp.host", mailServer_);
        props.put("mail.smtp.auth", String.valueOf(AuthEnabled));
        Session session = Session.getDefaultInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
        
        try{
            if (sender_ != null)
                msg.setFrom(sender_);
            if (receiver_ != null)
                msg.setRecipient(javax.mail.Message.RecipientType.TO, receiver_);
            if (cc_ != null )
                msg.setRecipient(javax.mail.Message.RecipientType.CC, cc_);
            if (bcc_ != null )
                msg.setRecipient(javax.mail.Message.RecipientType.BCC, bcc_);
            if (replier_ != null)
                msg.setReplyTo(replier_);
            if (mailSubject_ != null && mailSubject_.length() > 0)
                msg.setSubject(mailSubject_);
            if (mailBody_ != null && mailBody_.length() > 0){
                if( mailType_ == HTML )
                    msg.setContent(mailBody_, "text/html;charset=" + mailEnc_);
                else
                msg.setText(mailBody_);
            }
    
            Transport.send(msg);
        }catch(MessagingException e){
            throw new IOException(e.getMessage());
        }
        return;
    }
    
}