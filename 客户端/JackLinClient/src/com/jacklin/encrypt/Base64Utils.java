package com.jacklin.encrypt;

import it.sauronsoftware.base64.Base64;

public class Base64Utils {

	// 通过BASE64编码为字节数据
	public static byte[] decode(String base64) throws Exception {
		return Base64.decode(base64.getBytes());
	}

	// 通过BASE64解码为字符串
	public static String encode(byte[] bytes) throws Exception {
		return new String(Base64.encode(bytes));
	}
}
