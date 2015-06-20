package com.jacklin.main.yaoyiyao;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacklin.constant.Constants;
import com.jacklin.constant.HandlerID;
import com.jacklin.db.CacheUtils;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.main.MainjacklinActivity;
import com.jacklin.utils.PhotoUtils;
import com.jacklin.utils.ViewUtils;


@SuppressLint("HandlerLeak")
public class StrangerInfoActivity extends Activity implements OnClickListener,HttpCallBackListener{
	private static final String TAG = StrangerInfoActivity.class.getSimpleName();
	private TextView tv_title, tv_name, tv_gender, tv_mood, tv_birthday,
			tv_book, tv_film;
	private ImageView iv,iv_back;
	private Button bt_add;
	private Context mContext;
	private LocationModel stranger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_stranger_info);
		Intent intent=getIntent();
		stranger=(LocationModel) intent.getSerializableExtra(Constants.Flags.STRANGER);
		tv_title=(TextView) findViewById(R.id.tv_stranger_title);
		tv_name=(TextView) findViewById(R.id.tv_stranger_name);
		tv_gender=(TextView) findViewById(R.id.tv_stranger_gender);
		tv_mood=(TextView) findViewById(R.id.tv_stranger_mood);
		tv_birthday=(TextView) findViewById(R.id.tv_stranger_birthday);
		tv_book=(TextView) findViewById(R.id.tv_stranger_book);
		tv_film=(TextView) findViewById(R.id.tv_stranger_film);
		bt_add=(Button) findViewById(R.id.bt_stranger);
		iv=(ImageView) findViewById(R.id.iv_stranger_photo);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		bt_add.setOnClickListener(this);
		
		iv.setImageDrawable(PhotoUtils.StringToDrawable(stranger.getPhoto()));
		tv_title.setText(stranger.getName());
		tv_name.setText(stranger.getName());
		tv_gender.setText(stranger.getGender());
		tv_mood.setText(stranger.getMood());
		tv_birthday.setText(stranger.getBirthday());
		tv_book.setText(stranger.getBook());
		tv_film.setText(stranger.getFilm());
	}
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_stranger:
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = HandlerID.SHOW_PRO_DIALOG;
					msg.obj = "正在登录";
					mHandler.sendMessage(msg);

					JSONObject jsonObject = new JSONObject();
					try {
						jsonObject.put(Constants.AddFriend.RequestParams.USER_PHONE,
								CacheUtils.GetUserPhone());
						jsonObject.put(Constants.AddFriend.RequestParams.FRIEND_PHONE,
								stranger.getUser_phone());
						final String data = EncryptUtils.GetRsaEncrypt(jsonObject
								.toString());


						HttpUtils.sendReqData(Constants.ID.ADDFRIEND, data,StrangerInfoActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
						mHandler.sendEmptyMessage(HandlerID.CONNECT_TIMEOUT_DIALOG);
					}
				}
			
		}).start();
			break;
		case R.id.iv_back:
			this.finish();
			break;
		}
		
	}
	@Override
	public void httpCallBack(int id, JSONObject resp) {
		if (id == Constants.ID.LOGIN) {
			Log.d(TAG, "请求登录的回调函数");

			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);
			if ("100".equals(resCode)) {

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
				Message msg = new Message();
				msg.what = HandlerID.SHOW_PRO_DIALOG;
				msg.obj = "添加好友成功";
				mHandler.sendMessage(msg);
				//添加好友成功后操作
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				Intent intent=new Intent(this,MainjacklinActivity.class);
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
		
	}}
