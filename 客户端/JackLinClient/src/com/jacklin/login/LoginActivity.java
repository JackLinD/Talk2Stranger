package com.jacklin.login;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.constant.HandlerID;
import com.jacklin.db.CacheUtils;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.main.MainjacklinActivity;
import com.jacklin.register.RegisterActivity1;
import com.jacklin.utils.ViewUtils;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements HttpCallBackListener {
	private static final String TAG = LoginActivity.class.getSimpleName();
	private EditText et_login_phone, et_login_password;
	private SharedPreferences mSharedPreference;
	private Context mContext;
	private String user_phone, password;

	private static final String NAME_PREFERENCE = "com.jacklin.login.sharedpreference";
	private static final String KEY_USER_PHONE = "com.jacklin.login.user_phone";
	private static final String KEY_PASSWORD = "com.jacklin.login.password";

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerID.SHOW_PRO_DIALOG:
				ViewUtils.ShowProgressDialog(mContext, (String) msg.obj);
				break;
			case HandlerID.HIDE_PRO_DIALOG:
				ViewUtils.HideProgressDialog();
				break;
			case HandlerID.CONNECT_TIMEOUT_DIALOG:
				ViewUtils.ShowConnectTimeoutDialog(mContext);
				break;
			case HandlerID.SHOW_ERROR_DIALOG:
				ViewUtils.ShowErrorDialog(mContext, (String) msg.obj);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_login);

		initView();
		initValue1();
		handle();
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		mContext = this;
		mSharedPreference = getSharedPreferences(NAME_PREFERENCE,
				Context.MODE_PRIVATE);
		et_login_phone = (EditText) findViewById(R.id.et_login_phone);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
	}

	/**
	 * 初始化数据
	 */
	public void initValue1() {
		user_phone = mSharedPreference.getString(KEY_USER_PHONE, "");
		password = mSharedPreference.getString(KEY_PASSWORD, "");
	}

	public void initValue2() {
		user_phone = et_login_phone.getText().toString();
		password = et_login_password.getText().toString();
	}

	public void handle() {
		if (!TextUtils.isEmpty(user_phone) && !TextUtils.isEmpty(password)) {
			login(user_phone, password);
		} else if (!TextUtils.isEmpty(user_phone)
				&& TextUtils.isEmpty(password)) {
			et_login_phone.setText(user_phone);
		}
	}

	/**
	 * 保存数据到sharedpreference
	 */
	public void saveLogin() {
		Editor editor = mSharedPreference.edit();
		editor.putString(KEY_USER_PHONE, user_phone);
		editor.putString(KEY_PASSWORD, password);
		editor.commit();
	}

	/**
	 * 登陆响应事件
	 * 
	 * @param view
	 */
	public void click_login(View view) {
		initValue2();
		hideKeyBoard();

		if (TextUtils.isEmpty(user_phone) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "用户手机或密码不能为空", Toast.LENGTH_SHORT).show();
		} else {
			saveLogin();
			login(user_phone, password);
		}
	}

	/**
	 * 返回注册
	 * 
	 * @param view
	 */
	public void click_register(View view) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity1.class);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		startActivity(intent);
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 登录操作
	 * 
	 * @param user_phone
	 *            用户电话
	 * @param password
	 *            密码
	 */
	private void login(final String user_phone, final String password) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = HandlerID.SHOW_PRO_DIALOG;
				msg.obj = "正在登录";
				mHandler.sendMessage(msg);

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put(Constants.Login.RequestParams.USER_PHONE,
							user_phone);
					jsonObject.put(Constants.Login.RequestParams.PASSWORD,
							password);
					final String data = EncryptUtils.GetRsaEncrypt(jsonObject
							.toString());

					System.out.println(user_phone + "和he" + password);

					HttpUtils.sendReqData(Constants.ID.LOGIN, data,
							LoginActivity.this);
					System.out.println(user_phone + "和he" + password);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
					mHandler.sendEmptyMessage(HandlerID.CONNECT_TIMEOUT_DIALOG);
				}
			}
		}).start();
	}

	@Override
	public void httpCallBack(int id, JSONObject resp) {
		if (id == Constants.ID.LOGIN) {
			Log.d(TAG, "请求登录的回调函数");

			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);
			if ("100".equals(resCode)) {

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);

				// 缓存用户信息
				User user = new User();
				user.setUser_phone(resp
						.optString(Constants.Login.ResponeParams.USER_PHONE));
				user.setName(resp.optString(Constants.Login.ResponeParams.NAME));
				user.setGender(resp
						.optString(Constants.Login.ResponeParams.GENDER));
				user.setPhoto(resp
						.optString(Constants.Login.ResponeParams.PHOTO));
				user.setGame(resp.optString(Constants.Login.ResponeParams.GAME));
				user.setTip(resp.optString(Constants.Login.ResponeParams.TIP));
				user.setAnswer(resp
						.optString(Constants.Login.ResponeParams.ANSWER));
				user.setBirthday(resp
						.optString(Constants.Login.ResponeParams.BIRTHDAY));
				user.setBook(resp.optString(Constants.Login.ResponeParams.BOOK));
				user.setFilm(resp.optString(Constants.Login.ResponeParams.FILM));
				user.setCompany_school(resp
						.optString(Constants.Login.ResponeParams.COMPANY_SCHOOL));
				user.setMood(resp.optString(Constants.Login.ResponeParams.MOOD));

				CacheUtils.SetUserCache(user);

				Intent intent = new Intent(LoginActivity.this,
						MainjacklinActivity.class);

				LoginActivity.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				startActivity(intent);

			} else {

				String resMsg = resp
						.optString(Constants.ResponseParams.RES_MSG);

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
				if (resp != null) {
					Message msg = new Message();
					msg.what = HandlerID.SHOW_ERROR_DIALOG;
					msg.obj = resMsg;
					mHandler.sendMessage(msg);
				}
			}
		}

	}
}
