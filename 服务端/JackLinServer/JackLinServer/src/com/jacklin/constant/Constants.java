package com.jacklin.constant;

public class Constants {
	public static final int SERVER_PORT = 10010;
	
	// RSA加密参数
	public static final String DATA = "data";

	// 公共参数
	public static class ResponseParams {
		public static final String RES_CODE = "res_code";
		public static final String RES_MSG = "res_msg";
	}

	// 公共信息
	public static class ResponseInfo {
		public static final String CODE_SUCCESS = "100";
		public static final String MSG_SUCCESS = "success";

		public static final String CODE_DATA_NULL = "101";
		public static final String MSG_DATA_NULL = "encrypt data is null";

		public static final String CODE_RSA_DECRYPT_ERROR = "102";
		public static final String MSG_RSA_DECRYPT_ERROR = "rsa decrypt error";

		public static final String CODE_PARAM_MISSED = "103";
		public static final String MSG_PARAM_MISSED = "param is missed";
	}

	// 获取验证码
	public static class GetVerifyCode {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
		}

		public static class ErrorInfo {
			public static final String CODE_ID_EXISTED = "201";
			public static final String MSG_ID_EXISTED = "phone has been registered";

			public static final String CODE_GETVERIFYCODE_FAILED = "202";
			public static final String MSG_GETVERIFYCODE_FAILED = "get verifycode failed";

			public static final String CODE_ID_UNEXISTED = "203";
			public static final String MSG_ID_UNEXISTED = "phone has not been registered";
		}
	}

	// 校验验证码
	public static class CheckVerifyCode {
		public static final String VERIFYCODE_SESSION = "verifycode_session";
		public static final String VERIFYCODE = "verifycode";

		// public static final String TIME_LONG = "first_time";

		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String VERIFYCODE = "verifycode";
			// public static final String TIME_LONG = "last_time";
			public static final String PASSWORD = "password";
		}

		public static class ErrorInfo {
			public static final String CODE_CHECKVERIFYCODE_FAILED = "301";
			public static final String MSG_CHECKVERIFYCODE_FAILED = "check verifycode failed";
		}
	}

	// 用户注册
	public static class Register {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String NAME = "name";
			public static final String GENDER = "gender";
			public static final String PASSWORD = "password";
			public static final String PHOTO = "photo";
			public static final String GAME = "game";
			public static final String TIP="tip";
			public static final String ANSWER="answer";
			
		}

		public static class ErrorInfo {
			public static final String CODE_ID_EXISTED = "401";
			public static final String MSG_ID_EXISTED = "phone has been registered";

			public static final String CODE_REGISTER_FAILED = "402";
			public static final String MSG_REGISTER_FAILED = "register user failed";
		}
	}

	// 用户登录
	public static class Login {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String PASSWORD = "password";
		}

		public static class ResponeParams {
			public static final String USER_PHONE = "user_phone";
			public static final String NAME = "name";
			public static final String GENDER = "gender";
			public static final String PHOTO = "photo";
			public static final String BIRTHDAY = "birthday";
			public static final String BOOK = "book";
			public static final String FILM = "film";
			public static final String COMPANY_SCHOOL = "company_school";
			public static final String MOOD = "mood";
			public static final String GAME = "game";
			public static final String TIP="tip";
			public static final String ANSWER="answer";
		}

		public static class ErrorInfo {
			public static final String CODE_ID_UNRESGISTER = "501";
			public static final String MSG_ID_UNRESGISTER = "userid is unregistered";

			public static final String CODE_PSD_ERROR = "502";
			public static final String MSG_PSD_ERROR = "password id wrong";
		}
	}

	// 重置用户密码
	public static class ResetPassword {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String NEW_PASSWORD = "new_password";
			public static final String NEXT_NEW_PASSWORD = "next_new_password";
		}

		public static class ErrorInfo {
			public static final String CODE_PSD_UNMATCH = "601";
			public static final String MSG_PSD_UNMATCH = "two passwords unmatch";

			public static final String CODE_ID_UNRESGISTER = "602";
			public static final String MSG_ID_UNRESGISTER = "userid is unregistered";

			public static final String CODE_ID_UPDATEFAILED = "603";
			public static final String MSG_ID_UPDATEFAILED = "update password failed";
		}
	}

	// 重置用户信息
	public static class ResetUserInfo {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String NAME = "name";
			public static final String GENDER = "gender";
			public static final String PHOTO = "photo";
			public static final String BIRTHDAY = "birthday";
			public static final String BOOK = "book";
			public static final String FILM = "film";
			public static final String COMPANY_SCHOOL = "company_school";
			public static final String MOOD = "mood";
			public static final String GAME = "game";
			public static final String TIP="tip";
			public static final String ANSWER="answer";
		}

		public static class ErrorInfo {
			public static final String CODE_RESET_FAILED = "701";
			public static final String MSG_RESET_FAILED = "reset userinfo failed";
		}
	}

	// 获取好友列表
	public static class GetFriends {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
		}

		public static class ResponeParams {
			public static final String NUM = "num";
			public static final String FRIENDS = "friends";
		}

		/*
		 * public static class JsonParams{ public static final String
		 * FRIEND_NAME = "name"; public static final String FRIEND_GENDER =
		 * "gender"; public static final String FRIEND_PHOTO="photo"; public
		 * static final String FRIEND_BIRTHDAY="birthday"; public static final
		 * String FRIEND_BOOK="book"; public static final String
		 * FRIEND_FILM="film"; public static final String
		 * FRIEND_COMPANY_SCHOOL="company_school"; public static final String
		 * FRIEND_MOOD="mood"; }
		 */
		public static class ErrorInfo {
			public static final String CODE_GETFRIENDS_FAILED = "801";
			public static final String MSG_GETFRIENDS_FAILED = "get friends failed";
		}
	}

	// 移除好友
	public static class RemoveFriend {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String FRIEND_PHONE = "friend_phone";
		}

		public static class ErrorInfo {
			public static final String CODE_REMOVEFRIEND_FAILED = "901";
			public static final String MSG_REMOVEFRIEND_FAILED = "remove friend failed";
		}
	}

	// 搜索好友
	public static class SearchFriend {
		public static class RequestParams {
			public static final String FRIEND_PHONE = "friend_phone";
		}

		public static class ResponeParams {
			public static final String FRIEND_PHONE = "friend_phone";
			public static final String NAME = "name";
			public static final String GENDER = "gender";
			public static final String PHOTO = "photo";
			public static final String BIRTHDAY = "birthday";
			public static final String BOOK = "book";
			public static final String FILM = "film";
			public static final String COMPANY_SCHOOL = "company_school";
			public static final String MOOD = "mood";
		}

		public static class ErrorInfo {
			public static final String CODE_ADDFRIEND_FAILED = "1001";
			public static final String MSG_ADDFRIEND_FAILED = "search friend failed";
		}
	}

	// 添加好友
	public static class AddFriend {
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String FRIEND_PHONE = "friend_phone";
		}

		public static class ErrorInfo {
			public static final String CODE_ADDFRIEND_FAILED = "1101";
			public static final String MSG_ADDFRIEND_FAILED = "add friend failed";
		}
	}
	
	//获取游戏
	public static class GetGame{
		public static class RequestParams {
			public static final String FRIEND_PHONE = "friend_phone";
		}
		
		public static class ResponeParams{
			public static final String FRIEND_PHONE = "friend_phone";
			public static final String NAME = "name";
			public static final String GAME = "game";
			public static final String TIP="tip";
		}
		
		public static class ErrorInfo{
			public static final String CODE_GETGAME_UNREGISTER="1201";
			public static final String MSG_GETGAME_UNREGISTER="the friend has not been register";
			
			public static final String CODE_GETGAME_FAILED="1202";
			public static final String MSG_GETGAME_FAILED="get game failed";
		}
	}
	
	//验证游戏
	public static class CheckGame{
		public static class RequestParams {
			public static final String FRIEND_PHONE = "friend_phone";
			public static final String ANSWER="answer";
		}
		
		public static class ErrorInfo{
			public static final String CODE_GETGAME_UNREGISTER="1301";
			public static final String MSG_GETGAME_UNREGISTER="the friend has not been register";
			
			public static final String CODE_CHECKGAME_ERROR="1302";
			public static final String MSG_CHECKGAME_ERROR="the answer is wrong";
		}
	}
	//获取1公里内的陌生人
	public static class GetLocation{
		public static final String USER_PHONE = "user_phone";
		public static final String LATITUDE="latitude";
		public static final String LONGITUDE="longitude";
		
		public static class RequestParams {
			public static final String USER_PHONE = "user_phone";
			public static final String LATITUDE="latitude";
			public static final String LONGITUDE="longitude";
		}
		
		public static class ResponeParams{
			public static final String FRIEND_PHONE = "friend_phone";
			public static final String LATITUDE="latitude";
			public static final String LONGITUDE="longitude";
		}
		
		public static class ErrorInfo{
			
		}
	}
}
