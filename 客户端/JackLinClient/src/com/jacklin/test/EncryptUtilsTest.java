package com.jacklin.test;

import org.json.JSONException;
import org.json.JSONObject;

import com.jacklin.constant.Constants;
import com.jacklin.encrypt.DecryptUtils;
import com.jacklin.encrypt.EncryptUtils;

import android.test.AndroidTestCase;

public class EncryptUtilsTest extends AndroidTestCase{
	public void testEncrypt(){
		String user_phone="18316057270";
		String password="123456";
		String name="郭泽林";
		String gender="男";
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put(Constants.Register.RequestParams.USER_PHONE, user_phone);
			jsonObject.put(Constants.Register.RequestParams.PASSWORD,password);
			jsonObject.put(Constants.Register.RequestParams.NAME,name);
			jsonObject.put(Constants.Register.RequestParams.GENDER,gender);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonObject.toString());
		String data=EncryptUtils.GetRsaEncrypt(jsonObject.toString());
		System.out.println(data);
		String dataDe=DecryptUtils.GetRsaDecrypt(data);
		System.out.println(dataDe);
	}
}
