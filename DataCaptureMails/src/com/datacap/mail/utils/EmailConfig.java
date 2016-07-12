package com.datacap.mail.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class EmailConfig {
	private static final Logger log = Logger.getLogger(EmailConfig.class);
	private static String path = EmailConfig.class.getResource("/").getPath();

	/**
	 * 读取邮件信息的配置文件
	 * 
	 * @return
	 */
	public Map<String, Object> emailConf() {

		Map<String, Object> econfig = null;
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		try {
			document = builder.build(new File(path + "emails.xml"));
		} catch (JDOMException e1) {
			log.debug("邮件配置文件解析异常");
			e1.printStackTrace();
		} catch (IOException e1) {
			log.debug("邮件配置文件IO异常");
			e1.printStackTrace();
		}

		// 读取发送邮件的账号密码和接收邮件的email列表
		int flag = 0;// 用于标识
		String smtpserver = null;
		String username = null;
		String password = null;
		List<String> emails = null;

		Element root = document.getRootElement();
		Element smtp = root.getChild("smtp-server");
		if (smtp != null) {
			if (smtp.getText() != null && !smtp.getText().isEmpty()) {
				smtpserver = smtp.getText();
				flag++;
			}
		}

		Element sender = root.getChild("sender");
		for (Element e : sender.getChildren()) {
			// 如果username不为空并且符合邮箱的格式
			if ("username".equals(e.getName())) {
				if (!e.getText().isEmpty() && Verification.isEmail(e.getText())) {
					log.debug("邮件用户名符合");
					username = e.getText();
					flag++;
				}
			}
			// 如果password不为空
			if ("password".equals(e.getName())) {
				if (!e.getText().isEmpty()) {
					log.debug("邮件密码符合");
					password = e.getText();
					flag++;
				}
			}
		}

		List<Element> receivers = root.getChild("receivers").getChildren();
		for (Element e : receivers) {
			if ("receiver".equals(e.getName())) {
				// 不为空并且符合邮箱的格式
				if (!e.getText().isEmpty() && Verification.isEmail(e.getText())) {
					// 新建邮箱列表
					if (emails == null) {
						log.debug("接受邮件符合");
						emails = new ArrayList<String>();
						flag++;
					}
					emails.add(e.getText());
				}
			}
		}

		// 将读取到的配置文件返回
		if (flag == 4) {
			log.debug("读取邮件配置文件...");
			econfig = new HashMap<String, Object>();
			econfig.put("smtpserver", smtpserver);
			econfig.put("username", username);
			econfig.put("password", password);
			econfig.put("emails", emails);
		}

		return econfig;
	}
}
