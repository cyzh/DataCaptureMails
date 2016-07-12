package com.datacap.mail.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * @author WenC
 * 
 */
public class Verification {

	/**
	 * 验证邮件格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {

		Pattern pattern = Pattern
				.compile("([a-z|A-Z|0-9]+[-|\\.]?)+[a-z|A-Z|0-9]@([a-z|0-9|A-Z]+[-]?)+([a-z|A-Z|0-9]+)[\\.?][a-zA-Z]{2,}");
		boolean flag = pattern.matcher(email).matches();
		return flag;

	}

}
