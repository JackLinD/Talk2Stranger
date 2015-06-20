package com.jacklin.welcome;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.constant.HandlerID;
import com.jacklin.db.CacheUtils;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.login.LoginActivity;
import com.jacklin.main.MainjacklinActivity;
import com.jacklin.utils.ViewUtils;

/**
 * 欢迎界面，判断用户手机和密码是否存在，如果存在直接登录，不存在则转到登录界面
 * 
 * @author john
 *
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class WelcomeActivity extends Activity implements HttpCallBackListener {
	private static final String TAG = WelcomeActivity.class.getSimpleName();
	private AlphaAnimation start_anima;
	private View view;

	private SharedPreferences mSharedPreference;
	private static final String NAME_PREFERENCE = "com.jacklin.login.sharedpreference";
	private static final String KEY_USER_PHONE = "com.jacklin.login.user_phone";
	private static final String KEY_PASSWORD = "com.jacklin.login.password";

	private String user_phone, password;

	private Context mContext;

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
		view = LayoutInflater.from(this)
				.inflate(R.layout.jacklin_welcome, null);
		setContentView(view);
		init();
		initAnima();
	}

	private void initAnima() {
		start_anima = new AlphaAnimation(0.2f, 1.0f);
		start_anima.setDuration(2000);
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				handle();
			}
		});
	}

	public void init() {
		mSharedPreference = getSharedPreferences(NAME_PREFERENCE,
				Context.MODE_PRIVATE);
		user_phone = mSharedPreference.getString(KEY_USER_PHONE, "");
		password = mSharedPreference.getString(KEY_PASSWORD, "");
		mContext = this;
	}

	public void handle() {
		// 分三种情况：1、有用户名没密码；2、有用户名和密码；3、没有用户名和密码
		if (!TextUtils.isEmpty(user_phone) && TextUtils.isEmpty(password)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					Intent intent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					WelcomeActivity.this.finish();
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					startActivity(intent);
				}
			}, 2900);
		} else if (!TextUtils.isEmpty(user_phone)
				&& !TextUtils.isEmpty(password)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					login(user_phone, password);
				}
			}, 2900);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					WelcomeActivity.this.finish();
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					startActivity(intent);
				}
			}, 2900);
		}
	}

	public void logout() {
		Editor editor = mSharedPreference.edit();
		editor.putString(KEY_PASSWORD, "");
		editor.commit();
	}

	private void login(final String user_phone, final String password) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put(Constants.Login.RequestParams.USER_PHONE,
							user_phone);
					jsonObject.put(Constants.Login.RequestParams.PASSWORD,
							password);
					final String data = EncryptUtils.GetRsaEncrypt(jsonObject
							.toString());
					HttpUtils.sendReqData(Constants.ID.LOGIN, data,
							WelcomeActivity.this);

				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
					mHandler.sendEmptyMessage(HandlerID.CONNECT_TIMEOUT_DIALOG);
					logout();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Intent intent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					WelcomeActivity.this.finish();
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					startActivity(intent);
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
				Intent intent = new Intent(WelcomeActivity.this,
						MainjacklinActivity.class);

				WelcomeActivity.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				startActivity(intent);

			} else {
				logout();

				String resMsg = resp
						.optString(Constants.ResponseParams.RES_MSG);

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
				if (resp != null) {
					Message msg = new Message();
					msg.what = HandlerID.SHOW_ERROR_DIALOG;
					msg.obj = resMsg;
					mHandler.sendMessage(msg);
				}

				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);

				WelcomeActivity.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				startActivity(intent);
			}
		}

	}
}
