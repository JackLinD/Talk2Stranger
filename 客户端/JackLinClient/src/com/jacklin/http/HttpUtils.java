package com.jacklin.http;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.jacklin.constant.Constants;

/**
 * 通过HTTP post上传Json数据 并获取服务器下发的Json数据
 * 
 * @author john
 *
 */
@SuppressWarnings("deprecation")
public class HttpUtils {
	public static void sendReqData(int id, String data,HttpCallBackListener listener) throws ConnectTimeoutException,
	SocketTimeoutException, Exception {
		boolean hasPhoto=false;
		boolean hasGame=false;
		String photo=null;
		String game=null;
		String url = null;
		switch (id) {
		// 获取验证码
		case Constants.ID.GETCODE:
			url = Constants.GetVerifyCode.URL;
			break;
		// 校验验证码
		case Constants.ID.CHECKCODE:
			url = Constants.CheckVerifyCode.URL;
			break;
		// 注册
		case Constants.ID.REGISTER:
			url = Constants.Register.URL;
			String[] strArr=data.split("&");
			if(strArr.length==3){
				photo=strArr[1];
				game=strArr[2];
				hasGame=true;
				hasPhoto=true;
			}
			data=strArr[0];
			break;
		// 登录
		case Constants.ID.LOGIN:
			url = Constants.Login.URL;
			break;
		// 重置密码时获取验证码
		case Constants.ID.RESETGETCODE:
			url = Constants.GetVerifyCode.RESETURL;
			break;
		// 重置密码时校验验证码
		case Constants.ID.RESETCHECKCODE:
			url = Constants.CheckVerifyCode.RESETURL;
			break;
		// 重置密码
		case Constants.ID.RESETPASSWORD:
			url = Constants.ResetPassword.URL;
			break;
		// 重置用户信息
		case Constants.ID.RESETUSERINFO:
			url = Constants.ResetUserInfo.URL;
			String[] strArr1=data.split("&");
			if(strArr1.length==3){
				photo=strArr1[1];
				game=strArr1[2];
				hasGame=true;
				hasPhoto=true;
			}
			data=strArr1[0];
			break;
		// 获取好友列表
		case Constants.ID.GETFRIENDS:
			url = Constants.GetFriends.URL;
			break;
		// 移除好友
		case Constants.ID.REMOVEFRIEND:
			url = Constants.RemoveFriend.URL;
			break;
		// 添加好友
		case Constants.ID.ADDFRIEND:
			url = Constants.AddFriend.URL;
			break;
		// 获取陌生人游戏
		case Constants.ID.GETGAME:
			url = Constants.GetGame.URL;
			break;
		// 匹配游戏答案
		case Constants.ID.CHECKGAME:
			url = Constants.CheckGame.URL;
			break;
		// 获取一公里陌生人地址
		case Constants.ID.GETLOCATION:
			url = Constants.GetLocation.URL;
			break;
		}
		System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Constants.RequestParams.DATA,
					data));
			if(hasPhoto){
				params.add(new BasicNameValuePair(Constants.RequestParams.PHOTO,
						photo));
				System.out.println("有图片");
			}
			if(hasGame){
				params.add(new BasicNameValuePair(Constants.RequestParams.GAME,
						game));
				System.out.println("有游戏");
			}
			System.out.println(params);
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			// 请求超时
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            // 读取超时
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000    );

			HttpResponse httpResponse = httpClient.execute(httpPost);
			System.out.println(httpResponse.getStatusLine().getStatusCode()
					+ "");
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				String respStr = EntityUtils.toString(httpResponse.getEntity());
				JSONObject respJson = new JSONObject(respStr);
				//回调
				listener.httpCallBack(id, respJson);
			} else {
				
			}
		
	}
}
