package com.cw.wizbank.newmessage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.util.StringUtils;

import com.cw.wizbank.Application;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.message.MessageAuthenticator;
import com.cw.wizbank.newmessage.entity.EmailMessage;
import com.cw.wizbank.qdb.SystemSetting;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class MessageOutbox {
	public final static String SMTP = "SMTP";
	public final static String HTTP = "HTTP";

	private Session session = null;
	private File logFolder = null;

	private String[] recipMails = null;
	private String[] recipNames = null;
	private String[] recipId = null;
	private String[] cc = null;
	private String[] ccEmail = null;
	private String[] ccNames = null;
	private String[] ccId = null;

	private long validEmailSize = 0;
	private long msg_id = 0;
	
	private MimeMessage msg = null;
	private qdbEnv static_env;
	
	private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private final String DEFAULT_PORT = "465";

	public MessageOutbox(qdbEnv static_env1) {
		static_env = static_env1;
		if (session == null) {
			boolean isGmail = Application.MAIL_SERVER_HOST.indexOf("gmail") != -1;
			Properties props = new Properties();
			MessageAuthenticator auth = new MessageAuthenticator(Application.MAIL_SERVER_USER, Application.MAIL_SERVER_PASSWORD);
			props.put("mail.smtp.host", Application.MAIL_SERVER_HOST);
			props.put("mail.smtp.auth", String.valueOf(Application.MAIL_SERVER_AUTH_ENABLED));
			if (isGmail) {
				Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
				props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
				props.put("mail.smtp.socketFactory.fallback", "false");
				props.put("mail.smtp.port", DEFAULT_PORT);
				props.put("mail.smtp.socketFactory.port", DEFAULT_PORT);
			}
			session = Session.getDefaultInstance(props, auth);
		}
		if (logFolder == null) {
			String logFolderStr = static_env.DOC_ROOT + dbUtils.SLASH + static_env.LOG_FOLDER;
			logFolder = new File(logFolderStr);
			if (!logFolder.exists())
				logFolder.mkdir();
		}
		return;
	}
	
	public boolean send(Connection con, String method, EmailMessage emsg) throws SQLException, UnsupportedEncodingException, cwException{

		boolean send_result = false;
		if (method.equalsIgnoreCase("HTML")
				|| method.equalsIgnoreCase("NOTES")
				|| method.equalsIgnoreCase("HTML_MAIL")) {
			
			try{
				msg_id = emsg.getEmsg_id();
				msg = new MimeMessage(session);
				//收件人
				
				Vector<String[]> rec_vec = dbRegUser.getUsrEmails(con, emsg.getEmsg_rec_ent_ids());
				recipId = rec_vec.get(0);
				recipNames = rec_vec.get(1);
				recipMails = rec_vec.get(2);
				
				if(null!=emsg.getMtp_type() && "SYS_PERFORMANCE_NOTIFY".equalsIgnoreCase(emsg.getMtp_type())){
			        Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
					String recMap = String.valueOf(curSysSet.get(SystemSetting.SYS_CFG_TYPE_EMAIL));
					if(!StringUtils.isEmpty(recMap)){
						recipMails = new String[]{recMap};
					}
				}
				
				Vector<InternetAddress> validEmail = new Vector<InternetAddress>();
				for (int i = 0; i < recipMails.length; i++) {
					//filter empty email user
					if (recipMails[i] == null || recipMails[i].length() == 0 || (recipMails[i].trim()).equalsIgnoreCase("null")) {
						errorToLog(con, msg_id, recipNames[i]+" email is empty.");
						continue;
					}
					validEmail.addElement(new InternetAddress(recipMails[i], recipNames[i],static_env.ENCODING));
				}
				validEmailSize = validEmail.size();
				if(validEmailSize>0){
					InternetAddress[] msgRecipients = new InternetAddress[validEmail.size()];
					msgRecipients = (InternetAddress[]) validEmail.toArray(msgRecipients);
					msg.setRecipients(javax.mail.Message.RecipientType.TO, msgRecipients);
				}else{
					errorToLog(con, msg_id, "No recipient email address");
					return send_result;
				}
				
				//抄送人
				if(emsg.getEmsg_cc_ent_ids() != null && emsg.getEmsg_cc_ent_ids().length() > 0 || emsg.getEmsg_cc_email() != null){
					Vector<InternetAddress> validCcEmail = new Vector<InternetAddress>();
					
					if(emsg.getEmsg_cc_ent_ids() != null && emsg.getEmsg_cc_ent_ids().length() > 0) {
						Vector<String[]> cc_vec = dbRegUser.getUsrEmails(con, emsg.getEmsg_cc_ent_ids());
						ccId = rec_vec.get(0);
						ccNames = cc_vec.get(1);
						cc = cc_vec.get(2);
						for (int i = 0; i < cc.length; i++) {
							//filter empty email user
							if (cc[i] == null || cc[i].length() == 0 || (cc[i].trim()).equalsIgnoreCase("null")) {
								errorToLog(con, msg_id, ccNames[i]+" email is empty.");
								continue;
							}
							validCcEmail.addElement(new InternetAddress(cc[i], ccNames[i],static_env.ENCODING));
						}
					}
					if(emsg.getEmsg_cc_email() != null) {
						ccEmail = emsg.getEmsg_cc_email().split(",");
						for (int i = 0; i < ccEmail.length; i++) {
							validCcEmail.addElement(new InternetAddress(ccEmail[i].trim(), null, static_env.ENCODING));
						}
					}
					
					if(validCcEmail.size()>0){
						validEmailSize = validEmailSize + validCcEmail.size();
						InternetAddress[] msgCc = new InternetAddress[validCcEmail.size()];
						msgCc = (InternetAddress[]) validCcEmail.toArray(msgCc);
						msg.setRecipients( javax.mail.Message.RecipientType.CC, msgCc);
					}
				}
				
				//发件人
				Vector<String[]> sender = dbRegUser.getUsrEmails(con, ((Long)emsg.getEmsg_send_ent_id()).toString());
				
				String senderName = (String)sender.get(1)[0];
				String senderMail = (String)sender.get(2)[0];
				
				if(emsg.getEmsg_subject().equalsIgnoreCase("Reminder") || emsg.getEmsg_subject().equalsIgnoreCase("Joining Instruction")) {
					senderName = "System";
				}
				
				// 课程支持邮件
				long itm_id = emsg.getEmsg_itm_id();
                String itm_notify_email = dbRegUser.getUsrItem(con, itm_id);
                if(itm_notify_email != null && itm_notify_email.length()>0){
                	senderName = "Course Support";
                    senderMail = itm_notify_email;
                }
                
				msg.setFrom(new InternetAddress(senderMail, senderName, static_env.ENCODING));
				
				//主题
				String msgSubject = emsg.getEmsg_subject();
				msg.setSubject(msgSubject, static_env.ENCODING);
				
				//内容
				String msgBody = emsg.getEmsg_content();
				//附件
				MimeMultipart multipart = new MimeMultipart("mixed");
				BodyPart messageBodyPart = null;
				
				@SuppressWarnings("unchecked")
				Vector<String> vec = cwUtils.splitToVecString(emsg.getEmsg_attachment(), ",");
				FileDataSource[] fds = new FileDataSource[vec.size()];
				String filename = null;
				try {
					String filedir = static_env.DOC_ROOT + dbUtils.SLASH + Message.MSG_ATTACHMENT_PATH + dbUtils.SLASH + emsg.getEmsg_id();
					for (int j = 0; j < vec.size(); j++) {
						filename = (String) vec.elementAt(j);
						fds[j] = new FileDataSource( new File(filedir, filename));
						messageBodyPart = new MimeBodyPart();
						messageBodyPart.setDataHandler(new DataHandler(fds[j]));
						messageBodyPart.setHeader( "Content-Type", "application; name=" + filename);
						messageBodyPart.setHeader( "Content-ID", "attachment_" + (j));
						// Add part to multi-part
						multipart.addBodyPart(messageBodyPart);
					}
				} catch (Exception e) {
					String attach_error = "Attachment error : " + e.getMessage();
					errorToLog(con, msg_id, attach_error);
				}
				
				messageBodyPart = new MimeBodyPart();
				if (method.equalsIgnoreCase("HTML")){
					messageBodyPart.setContent(msgBody,"text/html;charset=" + static_env.ENCODING);
				}else{
					messageBodyPart.setText(msgBody);
				}
				multipart.addBodyPart(messageBodyPart);
				// Associate multi-part with message
				msg.setContent(multipart);
			}catch (MessagingException me) {
				throw new cwException("Messageing  Exception  :  " + me);
			}
			try {
				//Send Message
				Transport.send(msg);
				send_result = true;
			}catch (SendFailedException sfe) {
				//若只有多个收件人且有人发送失败，则记历史状态为Y.
				Address[] validMail = sfe.getValidUnsentAddresses();
				Address[] invalidMail = sfe.getInvalidAddresses();
				long FvalidMailSize = 0, FinvalidMailSize = 0;
				if(validMail != null){
					FvalidMailSize = validMail.length;
				}
				if(invalidMail != null){
					FinvalidMailSize = invalidMail.length;
				}
				if(validEmailSize == FinvalidMailSize + FvalidMailSize){
					send_result = false;
				}
				boolean sendAgain = false;
				try {
					//clear existing recipients
					msg.setRecipients(javax.mail.Message.RecipientType.TO, new Address[0]);
					msg.setRecipients(javax.mail.Message.RecipientType.BCC, new Address[0]);
					msg.setRecipients(javax.mail.Message.RecipientType.CC, new Address[0]);
					if (recipMails != null) {
						for (int i = 0; i < recipMails.length; i++) {
						    boolean matchValidMail = false;
						    boolean matchInvalidMail = false;
						    
							if (validMail != null) {
								for (int j = 0; j < validMail.length; j++) {
									if (recipMails[i]
										.equalsIgnoreCase(
											((InternetAddress) validMail[j])
												.getAddress()) && recipNames[i].equalsIgnoreCase( ((InternetAddress)validMail[j]).getPersonal()) ) {
										msg.addRecipient(javax.mail.Message.RecipientType.TO,validMail[j]);
										sendAgain = true;
										matchValidMail = true;
									}
								}
							}
							if (invalidMail != null) {
								for (int j = 0; j < invalidMail.length; j++) {
									if (recipMails[i]
										.equalsIgnoreCase(
											((InternetAddress) invalidMail[j])
												.getAddress()) && recipNames[i].equalsIgnoreCase( ((InternetAddress)invalidMail[j]).getPersonal())) {
										matchInvalidMail = true;

										errorToLog(con, msg_id, recipId[i]+ " invalid email address");
									}
								}
								if(recipId.length == 1){
									return false;
								}
							}
							if (!matchValidMail && !matchInvalidMail){
                                errorToLog(con, msg_id, recipId[i]+" "+sfe.getMessage());
                                return false;
                            }
						}
					}

					if (cc != null) {
						for (int i = 0; i < cc.length; i++) {
                            boolean matchValidMail = false;
						    boolean matchInvalidMail = false;

							if (validMail != null) {
								for (int j = 0; j < validMail.length; j++) {
									if (cc[i]
										.equalsIgnoreCase(
											((InternetAddress) validMail[j])
												.getAddress()) && ccNames[i].equalsIgnoreCase( ((InternetAddress)validMail[j]).getPersonal())) {
										msg.addRecipient(javax.mail.Message.RecipientType.CC, validMail[j]);
										sendAgain = true;
										matchValidMail = true;
									}
								}
							}
							if (invalidMail != null) {
								for (int j = 0; j < invalidMail.length; j++) {
									if (cc[i]
										.equalsIgnoreCase(
											((InternetAddress) invalidMail[j])
												.getAddress())  && ccNames[i].equalsIgnoreCase( ((InternetAddress)invalidMail[j]).getPersonal())) {
										matchInvalidMail = true;
										errorToLog(con, msg_id, ccId[i]+ " invalid carbon copy email address");
									}
								}
							}
                            if (!matchValidMail && !matchInvalidMail){
                            	errorToLog(con, msg_id, ccId[i]+" "+sfe.getMessage());
                            	return false;
                            }
						}
					}
					if (sendAgain) {
						Transport.send(msg);
						send_result = true;
					}
				} catch (MessagingException me) {
					throw new cwException("Messageing  Exception  :  " + me);
				}
			} catch (MessagingException me) {
				throw new cwException("Messageing  Exception  :  " + me);
			} 
		}
		return send_result;
	}
	
	public void errorToLog(Connection con, long msg_id, String error)
			throws cwException {

			String content = new String();
			try {
				Timestamp curTime = cwSQL.getTime(con);
				content += curTime + "          ";
				if (msg_id > 0){
					content += " [msg id = " + msg_id + "]" + System.getProperty("line.separator");
				}
				content += error + System.getProperty("line.separator") + System.getProperty("line.separator");

				if (logFolder == null) {
					String logFolderStr =
						static_env.DOC_ROOT + dbUtils.SLASH + static_env.LOG_FOLDER;
					logFolder = new File(logFolderStr);
					if (!logFolder.exists())
						logFolder.mkdir();
				}
				
				File logFile = new File(logFolder, static_env.MAIL_NOTES_LOG);
				FileWriter fw = new FileWriter(logFile.toString(), true);
				fw.write(content);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				throw new cwException(e.getMessage());
			}catch (SQLException e1) {
				throw new cwException(e1.getMessage());
			}
			return;
		}
}
