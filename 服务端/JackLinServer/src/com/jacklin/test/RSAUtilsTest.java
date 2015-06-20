package com.jacklin.test;

import org.junit.Test;

import com.jacklin.decrypt.RSAUtils;
/**
 * RSAUtils测试类
 * @author john
 *
 */
public class RSAUtilsTest {
	private static final String privateKeyStr="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJjyE+v4+tEZw9Iiz/1+yqB+0nWkBFEDP+xtrNqMCh94Is0hbGPskL/CjqnkhOoIkCyveYsU903U9HqJ+zbCuxJj4D7QmtxsvxztjTaXNin57k+kOgaEX4fUYpgRD/+a/loZqOnTqwYg66wafqamnJeB+1HnMmhfJHTb6PB6ty8vAgMBAAECgYATHshQyrqIY4EwhvubhDtOnywuWsfTXOWj5/4hADrPvlQTcuc7ArsBzg/Ju3DGVTW48GLweTmFeGbr3s4SBXwfWfOTq/qrT9SZ2QrptrV+RdE/ROKbF87i38WI+mZbP4Uqz8+my9qLqHqrQj3UBYY9vqjazlJQsLqqpPDekK/GuQJBAMlv+LCyjr55guLBpo+l0MJA8gcwZe8O6VmrkRRFjJeahOeV9vSolDcGfCRMAa9jcXNTcXAF+yJezL4vdTxDpUsCQQDCX5mo2UvSVSwhFMew2WcUwkxEhWTj3lSXwtiewrYSSAtojYouPTv7YgYoOGKueVHmFFlZ/F6k34xRu78f4cMtAkBUvL6gDi0QIWuW2iGoEffUj5+AuLJYsywkpZCdN3iiGcVI6oP9faED2L2GQ1b+IdYKzpJMrqdrWPfGFY66sDYrAkEAtubIq/cVfu2Aofxwz/wzIHiY1d2EC1w507iYqAPm2hsLCS4u+cXLPIiH7K7qRO2KYGuDjMaKtLBa1MpLVY/WXQJAJhHHPu4bdHk7tcHwxFHIwVzwHNKvl993Qbwg4nLBPfUUWKf2m04nHv/MHX/B0GfDFWn+veI6JABP6zAwWMy68A==";
	
	private static final String publicKeyStr="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCY8hPr+PrRGcPSIs/9fsqgftJ1pARRAz/sbazajAofeCLNIWxj7JC/wo6p5ITqCJAsr3mLFPdN1PR6ifs2wrsSY+A+0JrcbL8c7Y02lzYp+e5PpDoGhF+H1GKYEQ//mv5aGajp06sGIOusGn6mppyXgftR5zJoXyR02+jwercvLwIDAQAB";
	
	@Test
	public void testRSA(){
		try {
			/*Map<String,Object> keyMap=RSAUtils.genKeyPair();
			
			String publicKeyStr=RSAUtils.getPublicKey(keyMap);
			
			String privateKeyStr=RSAUtils.getPrivateKey(keyMap);
			
			System.out.println("公钥：\n"+publicKeyStr);
			
			System.out.println("私钥：\n"+privateKeyStr);*/
			
			System.out.println("公钥加密-私钥解密");
			
			String source="我叫郭泽林，今年大三，二十有三";
			
			System.out.println("加密前：\n"+source);
			
			byte[] data=source.getBytes();
			
			byte[] encodeData=RSAUtils.encryptByPublicKey(data, publicKeyStr);
			
			System.out.println("加密后：\n"+new String(encodeData));
			
			byte[] decodeData=RSAUtils.decryptByPrivateKey(encodeData, privateKeyStr);
			
			System.out.println("解密后：\n"+new String(decodeData));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
