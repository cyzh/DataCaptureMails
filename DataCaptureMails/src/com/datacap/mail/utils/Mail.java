package com.datacap.mail.utils;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class Mail {

	private static final Logger log = Logger.getLogger(Mail.class);

	private MimeMessage mimeMsg;
	private Session session;
	private Properties props;
	// private boolean needAuth = false;
	private String username;
	private String password;
	private Multipart mp;

	public Mail(String smtp) {
		setSmtpHost(smtp);
		createMimeMessage();
	}

	private void setSmtpHost(String hostName) {
		if (props == null)
			props = System.getProperties();
		props.put("mail.smtp.host", hostName);
	}

	private boolean createMimeMessage() {
		try {
			session = Session.getDefaultInstance(props, null);
		} catch (Exception e) {
			return false;
		}

		try {
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void setNeedAuth(boolean need) {
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	public void setNamePass(String name, String pass) {
		username = name;
		password = pass;
	}

	private boolean setSubject(String mailSubject) {
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=GBK");
			mp.addBodyPart(bp);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean setFrom(String from) {
		try {
			mimeMsg.setFrom(new InternetAddress(from));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean setTo(String to) {
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// private boolean setCopyTo(String copyto) {
	// if (copyto == null)
	// return false;
	// try {
	// mimeMsg.setRecipients(Message.RecipientType.CC,
	// (Address[]) InternetAddress.parse(copyto));
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }

	private boolean sendOut(String state) {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();

			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username,
					password);
			transport.sendMessage(mimeMsg,
					mimeMsg.getRecipients(Message.RecipientType.TO));
			if (state.equals("copyTure")) {
				transport.sendMessage(mimeMsg,
						mimeMsg.getRecipients(Message.RecipientType.CC));
			}
			transport.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean sendAndCc(String smtp, String from, String to,
			String copyto, String subject, String content, String username,
			String password, String state) {
		Mail theMail = new Mail(smtp);
		theMail.setNeedAuth(true);
		if (!theMail.setSubject(subject))
			return false;
		if (!theMail.setBody(content))
			return false;
		if (!theMail.setTo(to))
			return false;
//		if (!theMail.setCopyTo(copyto))
//			return false;
		if (!theMail.setFrom(from))
			return false;
		theMail.setNamePass(username, password);
		if (!theMail.sendOut(state))
			return false;
		return true;
	}

	public static void sendMessage(String subject, String content) {

		log.debug("发送邮件中....");
		EmailConfig econfig = new EmailConfig();
		Map<String, Object> conf = econfig.emailConf();
		if (conf != null) {
			String smtp = (String) conf.get("smtpserver");
			log.debug("smtp: " + smtp);
			String state = "CopyTure";
			String username = (String) conf.get("username");
			log.debug("发送邮件用户名: " + username);
			String password = (String) conf.get("password");
			List<?> tolist = (List<?>) conf.get("emails");
			String from = username;

			for (Object to : tolist) {
				String receiver = (String) to;
				boolean flag = sendAndCc(smtp, from, receiver, null, subject,
						content, username, password, state);
				if (flag) {
					log.debug("向 " + receiver + " : 发送邮件成功");
				} else {
					log.debug("向 " + receiver + " : 发送邮件失败");
				}
			}

		} else {
			log.debug("Email配置文件map为空，emails.e配置格式异常");
		}
	}

	/******************************************************************************************************/
	/*
	 * private boolean addFileAffix(String filename) { try { BodyPart bp = new
	 * MimeBodyPart(); FileDataSource fileds = new FileDataSource(filename);
	 * bp.setDataHandler(new DataHandler(fileds));
	 * bp.setFileName(fileds.getName());
	 * 
	 * mp.addBodyPart(bp);
	 * 
	 * return true; } catch (Exception e) { return false; } }
	 */

	/*
	 * private boolean send(String smtp, String from, String to, String subject,
	 * String content, String username, String password, String state) { Mail
	 * theMail = new Mail(smtp); theMail.setNeedAuth(true); if
	 * (!theMail.setSubject(subject)) return false; if
	 * (!theMail.setBody(content)) return false; if (!theMail.setTo(to)) return
	 * false; if (!theMail.setFrom(from)) return false;
	 * theMail.setNamePass(username, password);
	 * 
	 * if (!theMail.sendOut(state)) return true; return true; }
	 */

	/*
	 * private boolean send(String smtp, String from, String to, String subject,
	 * String content, String username, String password, String filename, String
	 * state) { Mail theMail = new Mail(smtp); theMail.setNeedAuth(true);
	 * 
	 * if (!theMail.setSubject(subject)) return false; if
	 * (!theMail.setBody(content)) return false; if
	 * (!theMail.addFileAffix(filename)) return false; if (!theMail.setTo(to))
	 * return false; if (!theMail.setFrom(from)) return false;
	 * theMail.setNamePass(username, password);
	 * 
	 * if (!theMail.sendOut(state)) return false; return true; }
	 */

	/*
	 * private boolean sendAndCc(String smtp, String from, String to, String
	 * copyto, String subject, String content, String username, String password,
	 * String filename, String state) { Mail theMail = new Mail(smtp);
	 * theMail.setNeedAuth(true);
	 * 
	 * if (!theMail.setSubject(subject)) return false; if
	 * (!theMail.setBody(content)) return false; if
	 * (!theMail.addFileAffix(filename)) return false; if (!theMail.setTo(to))
	 * return false; if (!theMail.setCopyTo(copyto)) return false; if
	 * (!theMail.setFrom(from)) return false; theMail.setNamePass(username,
	 * password);
	 * 
	 * if (!theMail.sendOut(state)) return false; return true; }
	 */
}