package com.jacklin.code;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class VerifyCodeUtils {
	static Logger logger = Logger.getLogger(VerifyCodeUtils.class);

	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

	public static boolean sendVerifyCode(String user_phone, int mobile_code) {

		boolean bSendVerifyCode = true;

		HttpClient client = new HttpClient();

		PostMethod method = new PostMethod(Url);

		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType",
				"application/x-www-form-urlencoded;charset=UTF-8");

		String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");

		NameValuePair[] data = { new NameValuePair("account", "cf_jacklinx"),
				new NameValuePair("password", "880808088"),
				new NameValuePair("mobile", user_phone),
				new NameValuePair("content", content), };

		method.setRequestBody(data);

		try {
			client.executeMethod(method);

			String SubmitResult = method.getResponseBodyAsString();

			Document doc = DocumentHelper.parseText(SubmitResult);

			Element root = doc.getRootElement();

			String code = root.elementText("code");
			/*
			 * String msg = root.elementText("msg"); String smsid =
			 * root.elementText("smsid");
			 */

			if (code.equals("2")) {

				// System.out.println("短信提交成功");
				logger.info("提交短信成功");

				bSendVerifyCode = true;

			} else {
				logger.info("提交短信失败");

				bSendVerifyCode = false;

			}

		} catch (HttpException e) {
			e.printStackTrace();
			bSendVerifyCode = false;
		} catch (IOException e) {
			e.printStackTrace();
			bSendVerifyCode = false;
		} catch (DocumentException e) {
			e.printStackTrace();
			bSendVerifyCode = false;
		}

		return bSendVerifyCode;
	}

	/*
	 * public static boolean checkVerifyCode(String user_phone,String
	 * verifycode){ boolean bCheckVerifyCode=true;
	 * 
	 * return bCheckVerifyCode; }
	 */
}
