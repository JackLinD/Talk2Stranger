package com.jacklin.http;

import net.sf.json.JSONObject;

public class HttpJson {
	private boolean isSuccess;
	private JSONObject reqJson;
	private JSONObject respJson;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public JSONObject getReqJson() {
		return reqJson;
	}

	public void setReqJson(JSONObject reqJson) {
		this.reqJson = reqJson;
	}

	public JSONObject getRespJson() {
		return respJson;
	}

	public void setRespJson(JSONObject respJson) {
		this.respJson = respJson;
	}
}
