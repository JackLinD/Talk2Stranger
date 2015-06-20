package com.jacklin.main.liaotian;

import org.json.JSONObject;
import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.constant.HandlerID;
import com.jacklin.db.CacheUtils;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.PhotoUtils;
import com.jacklin.utils.ThreadUtils;
import com.jacklin.utils.ViewUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendInfoActivity extends Activity implements OnClickListener,HttpCallBackListener{
	private static final String TAG = FriendInfoActivity.class.getSimpleName();
	private TextView tv_title, tv_name, tv_gender, tv_mood, tv_birthday,
			tv_book, tv_film;
	private ImageView iv,iv_back;
	private Button bt_send, bt_remove;
	private User mUser;
	private Context mContext;
	private Handler mMultiHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_friend_info);
		initView();
	}

	public void initView() {
		mContext=this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_name = (TextView) findViewById(R.id.tv_friend_name);
		tv_gender = (TextView) findViewById(R.id.tv_friend_gender);
		tv_mood = (TextView) findViewById(R.id.tv_friend_mood);
		tv_birthday = (TextView) findViewById(R.id.tv_friend_birthday);
		tv_book = (TextView) findViewById(R.id.tv_friend_book);
		tv_film = (TextView) findViewById(R.id.tv_friend_film);
		iv = (ImageView) findViewById(R.id.iv_friend_photo);
		bt_send = (Button) findViewById(R.id.bt_friend_send);
		bt_remove = (Button) findViewById(R.id.bt_friend_remove);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		Intent intent = getIntent();
		mUser = (User) intent.getSerializableExtra(Constants.Flags.FRIEND_INFO);

		iv.setImageDrawable(PhotoUtils.StringToDrawable(mUser.getPassword()));
		tv_title.setText(mUser.getName());
		tv_name.setText(mUser.getName());
		tv_gender.setText(mUser.getGender());
		tv_mood.setText(mUser.getMood());
		tv_birthday.setText(mUser.getBirthday());
		tv_book.setText(mUser.getBook());
		tv_film.setText(mUser.getFilm());
		bt_send.setOnClickListener(this);
		bt_remove.setOnClickListener(this);
		
		mMultiHandler=ThreadUtils.GetMultiHandler(TAG);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_friend_send:
			Intent intent = new Intent(mContext, ChattingActivity.class);
			intent.putExtra(Constants.Flags.CHATTING_FRIEND, mUser);
			((Activity) mContext).finish();
			startActivity(intent);
			break;
		case R.id.bt_friend_remove:
			mMultiHandler.post(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = HandlerID.SHOW_PRO_DIALOG;
					msg.obj = "请稍等...";
					mHandler.sendMessage(msg);
					JSONObject jsonObject = new JSONObject();
					try {
						jsonObject.put(Constants.RemoveFriend.RequestParams.USER_PHONE,
								CacheUtils.GetUserPhone());
						jsonObject.put(Constants.RemoveFriend.RequestParams.FRIEND_PHONE,
								mUser.getUser_phone());
						final String data = EncryptUtils.GetRsaEncrypt(jsonObject
								.toString());

						HttpUtils.sendReqData(Constants.ID.REMOVEFRIEND, data,
								FriendInfoActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
						mHandler.sendEmptyMessage(HandlerID.CONNECT_TIMEOUT_DIALOG);
					}
				}
			});
			break;
		case R.id.iv_back:
			this.finish();
			break;
		}

	}
	
	@SuppressLint("HandlerLeak")
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
	public void httpCallBack(int id, JSONObject resp) {
		if (id == Constants.ID.REMOVEFRIEND) {
			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);
			if ("100".equals(resCode)) {

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);

				

				((Activity) mContext).finish();

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
