package com.cw.wizbank.batch.eLibBatch;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;



public class eLibraryMailer{

    private MimeMessage mail = null;
    private String encoding = null;
    private String contentType = null;
    
    
    protected eLibraryMailer(String mailServer, String enc, String contentType) throws MessagingException{
        Properties props = new Properties();
        props.put("mail.smtp.host", mailServer);
        Session session = Session.getDefaultInstance(props, null);
        this.mail = new MimeMessage(session);
        this.encoding = enc;
        this.contentType = contentType;
        return;
    }

    protected void setSender(String display_bil, String email) throws IOException, MessagingException {
        this.mail.setFrom(new InternetAddress(email, display_bil));
        return;
    }

    protected void setRecipient(String display_bil, String email) throws MessagingException, IOException {
        InternetAddress[] intAddr = new InternetAddress[1];
        intAddr[0] = new InternetAddress(email, display_bil);
        this.mail.setRecipients(javax.mail.Message.RecipientType.TO, intAddr);
        return;
    }

    protected void setSubject(String subject) throws MessagingException{
        this.mail.setSubject(subject, this.encoding);
        return;
    }


    protected void setContent(String content) throws MessagingException{
        /*
        Multipart multi = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html;charset=" + this.encoding);
        multi.addBodyPart(messageBodyPart);
        this.mail.setContent(multi, this.encoding);
        */
        this.mail.setContent(content, this.contentType + ";charset=" + this.encoding);
        
        return;
    }

    protected void send() throws MessagingException{
        Transport.send(this.mail);
        return;
    }
}
