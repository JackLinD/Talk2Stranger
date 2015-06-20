package com.jacklin.test;

import org.json.JSONException;
import org.json.JSONObject;

import com.jacklin.constant.Constants;

import android.test.AndroidTestCase;

public class HttpUtilsTest extends AndroidTestCase {
	// 测试获取验证码
	public void testGetCode() {
		String user_phone = "18316057270";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.GetVerifyCode.RequestParams.USER_PHONE,
					user_phone);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonObject.toString());
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.GETCODE,
				jsonObject).toString());*/
	}

	// 测试校验验证码
	public void testCheckCode() {
		String user_phone = "18316057270";
		String code = "672492";
		String password = "123456";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.CheckVerifyCode.RequestParams.USER_PHONE,
					user_phone);
			jsonObject.put(Constants.CheckVerifyCode.RequestParams.VERIFYCODE,
					code);
			jsonObject.put(Constants.CheckVerifyCode.RequestParams.PASSWORD,
					password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.CHECKCODE,
				jsonObject).toString());*/
	}

	// 测试注册
	public void testRegister() {
		String user_phone = "18316057270";
		String password = "123456";
		String name = "郭泽林";
		String gender = "男";
		String game = "2";
		String answer = "大象";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.Register.RequestParams.USER_PHONE,
					user_phone);
			jsonObject.put(Constants.Register.RequestParams.PASSWORD, password);
			jsonObject.put(Constants.Register.RequestParams.NAME, name);
			jsonObject.put(Constants.Register.RequestParams.GENDER, gender);
			jsonObject.put(Constants.Register.RequestParams.GAME, game);
			jsonObject.put(Constants.Register.RequestParams.ANSWER, answer);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonObject.toString());
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.REGISTER,
				jsonObject).toString());*/
	}

	// 测试登录
	public void testLogin() {
		String user_phone = "15625017635";
		String password = "654321";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject
					.put(Constants.Login.RequestParams.USER_PHONE, user_phone);
			jsonObject.put(Constants.Login.RequestParams.PASSWORD, password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*System.out.println(HttpUtils
				.sendReqData(Constants.ID.LOGIN, jsonObject).toString());*/
	}

	// 测试重置密码
	public void testResetPassword() {
		String user_phone = "15625017635";
		String password = "654321";
		String new_password = "654321";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ResetPassword.RequestParams.USER_PHONE,
					user_phone);
			jsonObject.put(Constants.ResetPassword.RequestParams.NEW_PASSWORD,
					password);
			jsonObject.put(
					Constants.ResetPassword.RequestParams.NEXT_NEW_PASSWORD,
					new_password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.RESETPASSWORD,
				jsonObject).toString());*/
	}

	// 测试重置用户信息
	public void testResetUserInfo() {
		String user_phone = "18316057270";
		String brithday = "1992年5月10日";
		String name = "郭泽林";
		String gender = "男";
		String book = "Android开发大神";
		String film = "钢铁侠";
		String company = "华南师范大学";
		String mood = "很差";
		String tip = "一种动物";
		String photo = "1";
		String game = "2";
		String answer = "大象";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ResetUserInfo.RequestParams.USER_PHONE,
					user_phone);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.NAME, name);
			jsonObject
					.put(Constants.ResetUserInfo.RequestParams.GENDER, gender);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.GAME, game);
			jsonObject
					.put(Constants.ResetUserInfo.RequestParams.ANSWER, answer);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.BIRTHDAY,
					brithday);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.BOOK, book);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.FILM, film);
			jsonObject.put(
					Constants.ResetUserInfo.RequestParams.COMPANY_SCHOOL,
					company);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.MOOD, mood);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.TIP, tip);
			jsonObject.put(Constants.ResetUserInfo.RequestParams.PHOTO, photo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonObject.toString());
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.RESETUSERINFO,
				jsonObject).toString());*/
	}

	// 测试获取好友列表
	public void testFriends() {
		String user_phone = "15625018356";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.GetFriends.RequestParams.USER_PHONE,
					user_phone);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonObject.toString());
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.GETFRIENDS,
				jsonObject).toString());*/
	}

	// 测试添加好友
	public void testAddFriend() {
		String user_phone = "15625018356";
		String friend_phone = "18316057270";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.AddFriend.RequestParams.USER_PHONE,
					user_phone);
			jsonObject.put(Constants.AddFriend.RequestParams.FRIEND_PHONE,
					friend_phone);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonObject.toString());
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.ADDFRIEND,
				jsonObject).toString());*/
	}

	// 测试移除好友
	public void testRemoveFriend() {
		String user_phone = "15625018356";
		String friend_phone = "18316057270";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.RemoveFriend.RequestParams.USER_PHONE,
					user_phone);
			jsonObject.put(Constants.RemoveFriend.RequestParams.FRIEND_PHONE,
					friend_phone);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonObject.toString());
		/*System.out.println(HttpUtils.sendReqData(Constants.ID.REMOVEFRIEND,
				jsonObject).toString());*/
	}
}
