package com.cw.wizbank.message;

import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import java.util.*;

import com.cw.wizbank.Application;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;

import java.security.Security;
import java.sql.*;

import com.cw.wizbank.db.DbXslTemplate;

public class MessageOutbox {

	public final static String SMTP = "SMTP";
	public final static String HTTP = "HTTP";

	private Session session = null;
	private File logFolder = null;

	private MimeMessage msg = null;
	private String[] recipMails = null;
	private String[] recipNames = null;
	private String[] recipId = null;
	private String[] cc = null;
	private String[] ccNames = null;
	private String[] ccId = null;
	private String[] bcc = null;
	private String[] bccNames = null;
	private String[] bccId = null;
	private Hashtable recTable = null;
	private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private final String DEFAULT_PORT = "465";
			  
	public MessageOutbox(qdbEnv static_env) {
		if (session == null) {
			boolean isGmail = Application.MAIL_SERVER_HOST.indexOf("gmail") != -1;
			Properties props = new Properties();
			MessageAuthenticator auth = new MessageAuthenticator(Application.MAIL_SERVER_USER, Application.MAIL_SERVER_PASSWORD);
			props.put("mail.smtp.host", Application.MAIL_SERVER_HOST);
			props.put("mail.smtp.auth", String.valueOf(Application.MAIL_SERVER_AUTH_ENABLED));
			if(isGmail) {
				Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());			  
				props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
				props.put("mail.smtp.socketFactory.fallback", "false");
				props.put("mail.smtp.port", DEFAULT_PORT);
				props.put("mail.smtp.socketFactory.port", DEFAULT_PORT);
			}
			session = Session.getDefaultInstance(props, auth);
		}
		if (logFolder == null) {
			String logFolderStr =
				static_env.DOC_ROOT + dbUtils.SLASH + static_env.LOG_FOLDER;
			logFolder = new File(logFolderStr);
			if (!logFolder.exists())
				logFolder.mkdir();
		}
		return;
	}

	public Hashtable send(
		String method,
		String msgApi,
		String xslFile,
		String xml,
		qdbEnv static_env,
		Connection con,
		String sysEmail,
		long msg_id,
		Vector ReplyVec)
		throws IOException, cwException, SQLException {

		recTable = new Hashtable();
		xslFile =
			static_env.DOC_ROOT
				+ dbUtils.SLASH
				+ static_env.INI_XSL_HOME
				+ dbUtils.SLASH
				+ xslFile;
		if (method.equalsIgnoreCase("HTML")
			|| method.equalsIgnoreCase("NOTES")
			|| method.equalsIgnoreCase("HTML_MAIL")) {
			xmlObj xObj = new xmlObj(xml.toString());
			try {
				msg = new MimeMessage(session);

				//Set Message Recipients
				xObj.matchTag("recipient");
				recipMails = xObj.getAttributeValues("entity", "email");
				recipNames = xObj.getAttributeValues("entity", "display_name");
				recipId = xObj.getAttributeValues("entity", "rec_id");
				Vector validEmail = new Vector();
				for (int i = 0; i < recipMails.length; i++) {
					//filter empty email user
					if (recipMails[i] == null
						|| recipMails[i].length() == 0
						|| (recipMails[i].trim()).equalsIgnoreCase("null")) {
						errorToLog(
							con,
							"notes",
							Long.parseLong(recipId[i]),
							"No recipient email address",
							static_env);
						recTable.put(new Long(recipId[i]), "N");
						continue;
					}
					validEmail.addElement(
						new InternetAddress(recipMails[i], recipNames[i],static_env.ENCODING));
					recTable.put(new Long(recipId[i]), "Y");
				}
				InternetAddress[] msgRecipients =
					new InternetAddress[validEmail.size()];
				msgRecipients =
					(InternetAddress[]) validEmail.toArray(msgRecipients);
				msg.setRecipients(
					javax.mail.Message.RecipientType.TO,
					msgRecipients);

				//Set Message CarbonCopy
				if (xObj.matchTag("carboncopy")) {
					cc = xObj.getAttributeValues("entity", "email");
					ccNames = xObj.getAttributeValues("entity", "display_name");
					ccId = xObj.getAttributeValues("entity", "rec_id");
					Vector validCcEmail = new Vector();
					for (int i = 0; i < cc.length; i++) {
						//filter empty email user
						if (cc[i] == null
							|| cc[i].length() == 0
							|| (cc[i].trim()).equalsIgnoreCase("null")) {
							errorToLog(
								con,
								"notes",
								Long.parseLong(ccId[i]),
								"No carbon copy email address",
								static_env);
							recTable.put(new Long(ccId[i]), "N");
							continue;
						}
						validCcEmail.addElement(
							new InternetAddress(cc[i], ccNames[i],static_env.ENCODING));
						recTable.put(new Long(ccId[i]), "Y");
					}
					InternetAddress[] msgCc =
						new InternetAddress[validCcEmail.size()];
					msgCc = (InternetAddress[]) validCcEmail.toArray(msgCc);
					msg.setRecipients(
						javax.mail.Message.RecipientType.CC,
						msgCc);
				}

				//Set Message BlindCarbonCopy
				InternetAddress[] msgBcc = null;
				if (xObj.matchTag("blindcarboncopy")) {
					bcc = xObj.getAttributeValues("entity", "email");
					bccNames =
						xObj.getAttributeValues("entity", "display_name");
					bccId = xObj.getAttributeValues("entity", "rec_id");

					Vector validBccEmail = new Vector();
					for (int i = 0; i < bcc.length; i++) {
						//filter empty email user
						if (bcc[i] == null
							|| bcc[i].length() == 0
							|| (bcc[i].trim()).equalsIgnoreCase("null")) {
							errorToLog(
								con,
								"notes",
								Long.parseLong(bccId[i]),
								"No blind carbon copy email address",
								static_env);
							recTable.put(new Long(bccId[i]), "N");
							continue;
						}
						validBccEmail.addElement(
							new InternetAddress(bcc[i], bccNames[i],static_env.ENCODING));
						recTable.put(new Long(bccId[i]), "Y");
					}
					msgBcc = new InternetAddress[validBccEmail.size()];
					msgBcc = (InternetAddress[]) validBccEmail.toArray(msgBcc);
					msg.setRecipients(
						javax.mail.Message.RecipientType.BCC,
						msgBcc);
				}
				//Bcc to system mail
				if (sysEmail != null && sysEmail.length() > 0) {
					msg.addRecipient(
						javax.mail.Message.RecipientType.BCC,
						new InternetAddress(sysEmail));
				}

				//Set Message Sender
				xObj.matchTag("\\");
				String senderMail = xObj.getAttributeValue("sender", "email");
				String senderName =
					xObj.getAttributeValue("sender", "display_name");
				if (senderMail == null
					|| senderMail.equalsIgnoreCase("null")) {
					senderMail = "";
				}
				if (senderName == null
					|| senderName.equalsIgnoreCase("null")) {
					senderName = "";
				}
				msg.setFrom(new InternetAddress(senderMail, senderName,static_env.ENCODING));

				//Set Message Reply to
				boolean flag = xObj.matchTag("reply_to");
				if (flag) {
					String[] reply_to =
						xObj.getAttributeValues("entity", "email");
					String[] reply_to_name =
						xObj.getAttributeValues("entity", "display_name");
					InternetAddress[] msgReplyTo =
						new InternetAddress[reply_to.length];
					for (int i = 0; i < msgReplyTo.length; i++)
						msgReplyTo[i] =
							new InternetAddress(reply_to[i], reply_to_name[i],static_env.ENCODING);
					msg.setReplyTo(msgReplyTo);
				} else {
					xObj.matchTag("parameters");
					String[] paramName =
						xObj.getAttributeValues("param", "name");
					String[] paramValue =
						xObj.getAttributeValues("param", "value");
					for (int i = 0; i < paramName.length; i++) {
						if (paramName[i].equalsIgnoreCase("reply_to")) {
							InternetAddress[] msgReplyTo =
								new InternetAddress[1];
							msgReplyTo[0] = new InternetAddress(paramValue[i]);
							msg.setReplyTo(msgReplyTo);
						}
					}
				}

				if (ReplyVec != null && ReplyVec.size() > 0) {
					InternetAddress[] _msgReplyTo = (InternetAddress[]) msg.getReplyTo();
					int tmp = (_msgReplyTo != null && _msgReplyTo.length > 0) ? _msgReplyTo.length : 0;
					InternetAddress[] msgReplyTo = new InternetAddress[ReplyVec.size() + tmp];
					if (tmp > 0)
						System.arraycopy(_msgReplyTo, 0, msgReplyTo, 0, tmp);
					for (int i = 0 ; i < ReplyVec.size(); i++) {
						String[] reply_usr = (String[]) ReplyVec.get(i);
						msgReplyTo[tmp + i] = new InternetAddress(reply_usr[1], reply_usr[2]);
					}
					msg.setReplyTo(msgReplyTo);
				}
				
				//Set Message Subject
				xObj.matchTag("\\");
				String msgSubject = xObj.getNodeValue("subject");
				msg.setSubject(msgSubject, static_env.ENCODING);
				//Transform xml to message body
				String msgBody = cwXSL.processFromFile(xml.toString(), xslFile);
				//Create a related multi-part to combine the parts
				MimeMultipart multipart = new MimeMultipart("mixed");
				BodyPart messageBodyPart = null;
				DbXslTemplate[] xslTpl =
					Message.getAttachmentTemplate(con, msg_id);
				FileDataSource[] fds = new FileDataSource[xslTpl.length];
				File[] tmpFile = new File[xslTpl.length];

				int i = 0;
				for (i = 0; i < xslTpl.length; i++) {
					xslFile =
						static_env.DOC_ROOT
							+ dbUtils.SLASH
							+ static_env.INI_XSL_HOME
							+ dbUtils.SLASH
							+ xslTpl[i].xtp_xsl;
					String attachment = cwXSL.processFromFile(xml.toString(), xslFile);
					tmpFile[i] = new File("tmpFile" + i);
					Writer wout =
						new BufferedWriter(new FileWriter(tmpFile[i]));
					wout.write(attachment, 0, attachment.length());
					wout.flush();
					wout.close();

					fds[i] = new FileDataSource(tmpFile[i]);
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.setDataHandler(new DataHandler(fds[i]));
					messageBodyPart.setHeader(
						"Content-Type",
						"application; name=" + xslTpl[i].xtp_title);
					messageBodyPart.setHeader("Content-ID", "attachment_" + i);
					// Add part to multi-part
					multipart.addBodyPart(messageBodyPart);

				}

				// Attach message specific file
				// Get the values of all the param name with "attachment" prefix
				Vector vec = Message.getMsgAttachments(con, msg_id);
				fds = new FileDataSource[vec.size()];
				String filename = null;
				try {
					for (int j = 0; j < vec.size(); j++) {
						filename = (String) vec.elementAt(j);
						fds[j] =
							new FileDataSource(
								new File(
									static_env.DOC_ROOT
										+ dbUtils.SLASH
										+ Message.MSG_ATTACHMENT_PATH
										+ dbUtils.SLASH
										+ msg_id,
									filename));
						messageBodyPart = new MimeBodyPart();
						messageBodyPart.setDataHandler(new DataHandler(fds[j]));
						messageBodyPart.setHeader(
							"Content-Type",
							"application; name=" + filename);
						messageBodyPart.setHeader(
							"Content-ID",
							"attachment_" + (i + j));
						// Add part to multi-part
						multipart.addBodyPart(messageBodyPart);
					}
				} catch (Exception e) {
					String attach_error =
						"Attachment error : " + e.getMessage();
					errorToLog(con, "html", 0, attach_error, static_env);
				}

				messageBodyPart = new MimeBodyPart();
				if (method.equalsIgnoreCase("HTML"))
					messageBodyPart.setContent(
						msgBody,
						"text/html;charset=" + static_env.ENCODING);
				else
					messageBodyPart.setText(msgBody);
				multipart.addBodyPart(messageBodyPart);
				// Associate multi-part with message
				msg.setContent(multipart);
				//Send Message
				Transport.send(msg);
//			} catch (SAXException se) {
//				throw new cwException(
//					"SAXException, Failed to transform by xsl : " + se);

			} catch (SendFailedException sfe) {
				Address[] validMail = sfe.getValidUnsentAddresses();
				Address[] invalidMail = sfe.getInvalidAddresses();
				boolean sendAgain = false;
				try {
					//clear existing recipients
					msg.setRecipients(
						javax.mail.Message.RecipientType.TO,
						new Address[0]);
					msg.setRecipients(
						javax.mail.Message.RecipientType.BCC,
						new Address[0]);
					msg.setRecipients(
						javax.mail.Message.RecipientType.CC,
						new Address[0]);
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
										msg.addRecipient(
											javax.mail.Message.RecipientType.TO,
											validMail[j]);
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
										recTable.put(new Long(recipId[i]), "N");
										matchInvalidMail = true;

										errorToLog(
											con,
											"notes",
											Long.parseLong(recipId[i]),
											"No recipient email address",
											static_env);

									}
								}
							}
							if (!matchValidMail && !matchInvalidMail){
                                recTable.put(new Long(recipId[i]), "N");
                                errorToLog(con,"notes",Long.parseLong(recipId[i]),sfe.getMessage(),static_env);
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
										msg.addRecipient(
											javax.mail.Message.RecipientType.CC,
											validMail[j]);
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
										recTable.put(new Long(ccId[i]), "N");
										matchInvalidMail = true;

										errorToLog(
											con,
											"notes",
											Long.parseLong(ccId[i]),
											"No carbon copy email address",
											static_env);

									}
								}
							}
                            if (!matchValidMail && !matchInvalidMail){
                                recTable.put(new Long(ccId[i]), "N");
                                errorToLog(con,"notes",Long.parseLong(ccId[i]),sfe.getMessage(),static_env);
                            }

						}
					}
					if (bcc != null) {
						for (int i = 0; i < bcc.length; i++) {
                            boolean matchValidMail = false;
						    boolean matchInvalidMail = false;
							if (validMail != null) {

								for (int j = 0; j < validMail.length; j++) {
									if (bcc[i]
										.equalsIgnoreCase(
											((InternetAddress) validMail[j])
												.getAddress())  && bccNames[i].equalsIgnoreCase( ((InternetAddress)validMail[j]).getPersonal())) {
										msg.addRecipient(
											javax
												.mail
												.Message
												.RecipientType
												.BCC,
											validMail[j]);
										sendAgain = true;
										matchValidMail = true;
									}
								}
							}
							if (invalidMail != null) {

								for (int j = 0; j < invalidMail.length; j++) {
									if (bcc[i]
										.equalsIgnoreCase(
											((InternetAddress) invalidMail[j])
												.getAddress())  && bccNames[i].equalsIgnoreCase( ((InternetAddress)invalidMail[j]).getPersonal())) {
										recTable.put(new Long(bccId[i]), "N");
                                        matchInvalidMail = true;
										errorToLog(
											con,
											"notes",
											Long.parseLong(bccId[i]),
											"No blind carbon copy email address",
											static_env);

									}
								}
							}
                            if (!matchValidMail && !matchInvalidMail){
                                recTable.put(new Long(bccId[i]), "N");
                                errorToLog(con,"notes",Long.parseLong(ccId[i]),sfe.getMessage(),static_env);
                            }
						}
					}
					if (sendAgain) {
						Transport.send(msg);
					}
				} catch (MessagingException me) {
					throw new cwException("Messageing  Exception  :  " + me);
				}
			} catch (MessagingException me) {
				throw new cwException("Messageing  Exception  :  " + me);
			}

		} else if (method.equalsIgnoreCase("ETRAY")) {
//			try {
				xmlObj xObj = new xmlObj(xml.toString());
				String eTrayXml = cwXSL.processFromFile(xml.toString(), xslFile);
				String returnValue;
				try {
					//returnValue = (Message.returnByUrl(msgApi + eTrayXml)).trim();
					returnValue =
						(Message.returnByHttpApi(msgApi, eTrayXml)).trim();
				} catch (Exception e) {
					errorToLog(
						con,
						"etray",
						0,
						"External link error : " + msgApi,
						static_env);
					throw new cwException(
						"Failed to get the message detials : " + e);
				}

				recTable = xmlObj.processReturnEtray(returnValue);
				if (!recTable.isEmpty()) {
					errorToLog(con, "etray", 0, returnValue, static_env);
					//write message to log file
					// msg_id = msg_id, returnValue;
				}
//			} catch (SAXException se) {
//				throw new cwException(
//					"SAXException, Failed to transformed by xsl : " + se);
//			}
		}

		return recTable;
	}

	private void errorToLog(
		Connection con,
		String subtype,
		long rec_id,
		String error,
		qdbEnv static_env)
		throws SQLException, cwException {

		String content = new String();
		Timestamp curTime = cwSQL.getTime(con);
		content += curTime + "          ";
		if (rec_id > 0)
			content += " [recipient id = "
				+ rec_id
				+ "]"
				+ System.getProperty("line.separator");
		content += error
			+ System.getProperty("line.separator")
			+ System.getProperty("line.separator");

		try {
			File logFile = new File(logFolder, static_env.MAIL_NOTES_LOG);
			FileWriter fw = new FileWriter(logFile.toString(), true);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			throw new cwException(e.getMessage());
		}
		return;
	}

}