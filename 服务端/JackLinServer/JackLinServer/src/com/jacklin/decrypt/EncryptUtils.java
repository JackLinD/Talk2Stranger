package com.jacklin.decrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import Decoder.BASE64Decoder;

public class EncryptUtils {
	private static final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCY8hPr+PrRGcPSIs/9fsqgftJ1pARRAz/sbazajAofeCLNIWxj7JC/wo6p5ITqCJAsr3mLFPdN1PR6ifs2wrsSY+A+0JrcbL8c7Y02lzYp+e5PpDoGhF+H1GKYEQ//mv5aGajp06sGIOusGn6mppyXgftR5zJoXyR02+jwercvLwIDAQAB";
	private static char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	// RSA明文大小
	private static final int MAX_ENCRYPT_BLOCK = 117;
	// RSA密文大小
//	private static final int MAX_DECRYPT_BLOCK = 128;

	public static String GetRsaEncrypt(String src) {
		if (src == null)
			return "";
		String enData = null;
		try {
			byte[] data=src.getBytes("UTF-8");
			Cipher cipher = Cipher.getInstance("RSA",
					new BouncyCastleProvider());
			// Cipher cipher=Cipher.getInstance("RSA");
			RSAPublicKey pubKey = GetPublicKey(publicKeyStr);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			
			
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			
			out.close();
			/*byte[] output = cipher.doFinal(src.getBytes("UTF-8"));
			System.out.println("到碗里来");*/
			enData = ByteToString(encryptedData);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enData;
	}

	private static String ByteToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
		}
		return stringBuilder.toString();
	}

	/**
	 * 将String型私钥转换为RSAPublicKey
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 */
	private static RSAPublicKey GetPublicKey(String publicKeyStr) {
		RSAPublicKey pubKey = null;
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			pubKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pubKey;
	}
}
