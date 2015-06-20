package com.jacklin.http;

import javax.servlet.http.HttpServletRequest;

import com.jacklin.constant.Constants;
import com.jacklin.decrypt.DecryptUtils;

import net.sf.json.JSONObject;

public class HttpUtils {
	public static HttpJson ResolveParams(HttpServletRequest req) {
		HttpJson hdb = new HttpJson();
		JSONObject respJson = new JSONObject();
		String data = req.getParameter(Constants.DATA);

		// 参数不存在
		if (data.isEmpty()) {
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_DATA_NULL);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_DATA_NULL);
			hdb.setSuccess(false);
			hdb.setRespJson(respJson);
			return hdb;
		}

		// RSA解密
		String deStr = DecryptUtils.GetRsaDecrypt(data);

		// 解密失败
		if (deStr.isEmpty()) {
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_RSA_DECRYPT_ERROR);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_RSA_DECRYPT_ERROR);

			hdb.setSuccess(false);
			hdb.setRespJson(respJson);
			return hdb;
		}

		JSONObject reqJson = JSONObject.fromObject(deStr);

		hdb.setSuccess(true);
		hdb.setReqJson(reqJson);
		return hdb;
	}
}
