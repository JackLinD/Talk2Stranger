package com.jacklin.encrypt;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import Decoder.BASE64Decoder;

public class DecryptUtils {
	private static final String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJjyE+v4+tEZw9Iiz/1+yqB+0nWkBFEDP+xtrNqMCh94Is0hbGPskL/CjqnkhOoIkCyveYsU903U9HqJ+zbCuxJj4D7QmtxsvxztjTaXNin57k+kOgaEX4fUYpgRD/+a/loZqOnTqwYg66wafqamnJeB+1HnMmhfJHTb6PB6ty8vAgMBAAECgYATHshQyrqIY4EwhvubhDtOnywuWsfTXOWj5/4hADrPvlQTcuc7ArsBzg/Ju3DGVTW48GLweTmFeGbr3s4SBXwfWfOTq/qrT9SZ2QrptrV+RdE/ROKbF87i38WI+mZbP4Uqz8+my9qLqHqrQj3UBYY9vqjazlJQsLqqpPDekK/GuQJBAMlv+LCyjr55guLBpo+l0MJA8gcwZe8O6VmrkRRFjJeahOeV9vSolDcGfCRMAa9jcXNTcXAF+yJezL4vdTxDpUsCQQDCX5mo2UvSVSwhFMew2WcUwkxEhWTj3lSXwtiewrYSSAtojYouPTv7YgYoOGKueVHmFFlZ/F6k34xRu78f4cMtAkBUvL6gDi0QIWuW2iGoEffUj5+AuLJYsywkpZCdN3iiGcVI6oP9faED2L2GQ1b+IdYKzpJMrqdrWPfGFY66sDYrAkEAtubIq/cVfu2Aofxwz/wzIHiY1d2EC1w507iYqAPm2hsLCS4u+cXLPIiH7K7qRO2KYGuDjMaKtLBa1MpLVY/WXQJAJhHHPu4bdHk7tcHwxFHIwVzwHNKvl993Qbwg4nLBPfUUWKf2m04nHv/MHX/B0GfDFWn+veI6JABP6zAwWMy68A==";
	// RSA明文大小
//	private static final int MAX_ENCRYPT_BLOCK = 117;
	// RSA密文大小
	private static final int MAX_DECRYPT_BLOCK = 128;

	public static String GetRsaDecrypt(String data) {
		String deData = null;
		try {
			byte[] encryptedData=StringToByte(data);
			Cipher cipher = Cipher.getInstance("RSA",
					new BouncyCastleProvider());
			RSAPrivateKey priKey = GetPrivateKey(privateKeyStr);
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			
			int inputLen = encryptedData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher
							.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher
							.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			/*byte[] output = cipher.doFinal(StringToByte(data));*/
			deData = new String(decryptedData, "utf-8");// 此处如果不指定编码格式，则会产生中文乱码
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deData;
	}

	private static byte[] StringToByte(String str) {
		int len = str.length();
		byte[] data = new byte[len / 2];
		char[] ch = str.toCharArray();
		String tmp;
		int i = 0, j = 0;
		while (i < len) {
			tmp = String.valueOf(ch[i++]);
			int high = Integer.parseInt(tmp, 16);
			tmp = String.valueOf(ch[i++]);
			int low = Integer.parseInt(tmp, 16);

			data[j] = (byte) ((high << 4) + low);
			j++;

		}
		return data;
	}

	private static RSAPrivateKey GetPrivateKey(String privatekeystr2) {
		RSAPrivateKey priKey = null;
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			priKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return priKey;
	}
}
