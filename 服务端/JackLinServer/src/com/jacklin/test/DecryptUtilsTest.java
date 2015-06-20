package com.jacklin.test;

import org.junit.Test;

import com.jacklin.decrypt.DecryptUtils;
import com.jacklin.decrypt.EncryptUtils;
/**
 * EncryptUtils与DecryptUtils测试类
 * @author john
 *
 */
public class DecryptUtilsTest {
	@Test
	public void test(){
//		String data="{\"friend_phone\":\"553726172747\",\"answer\":\"kk\"}";
		String data="{\"user_phone\":\"15625017644\",\"latitude\":\"21.154\",\"longitude\":\"113.36\"}";
		String encryptData=EncryptUtils.GetRsaEncrypt(data);
		System.out.println(encryptData);
		String decryptData=DecryptUtils.GetRsaDecrypt(encryptData);
		System.out.println(decryptData);
	}
}
