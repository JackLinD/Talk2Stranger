package com.jacklin.http;

import org.json.JSONObject;
public interface HttpCallBackListener {
	void httpCallBack(int id, JSONObject resp);
}
