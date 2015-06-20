package com.jacklin.main.wo;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.constant.HandlerID;
import com.jacklin.db.CacheUtils;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.main.MainjacklinActivity;
import com.jacklin.utils.MyCanvas;
import com.jacklin.utils.PhotoUtils;
import com.jacklin.utils.ViewUtils;

@SuppressLint("HandlerLeak")
public class GameTActivity extends Activity implements OnClickListener,HttpCallBackListener {
	private MyCanvas myCanvas;
	private Paint mPaint;
	private Button bt_over;
	private TextView tv_tip, tv_answer;
	private RelativeLayout rl1, rl2;
private Context mContext;
private ImageView iv_back;
	
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
		setContentView(R.layout.jacklin_wo_gametwo);
		initView();
	}

	public void initView() {
		mContext = this;
		myCanvas = (MyCanvas) findViewById(R.id.iv);
		initPaint();
		myCanvas.setPaint(mPaint);
		bt_over = (Button) findViewById(R.id.bt_over);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_answer = (TextView) findViewById(R.id.tv_answer);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		rl1 = (RelativeLayout) findViewById(R.id.rl_kkkk);
		rl2 = (RelativeLayout) findViewById(R.id.rl_iiii);
		bt_over.setOnClickListener(this);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xff000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(10);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_over:
			String user_phone=CacheUtils.GetUserPhone();
			String name=CacheUtils.GetName();
			String gender=CacheUtils.GetGender();
			String photo=CacheUtils.GetPhoto();
			String mood=CacheUtils.GetMood();
			String birthday=CacheUtils.GetBirthday();
			String book=CacheUtils.GetBook();
			String film=CacheUtils.GetFilm();
			String tip=tv_tip.getText().toString();
			String answer=tv_answer.getText().toString();
			String game = PhotoUtils.DrawableToString(myCanvas.getDraw());
			resetInfo(user_phone,name, gender, photo, game, tip, answer, birthday, book, film, mood);
			break;

		case R.id.rl_kkkk:
			final EditText input = new EditText(GameTActivity.this);
			new AlertDialog.Builder(this)
					.setTitle("设置提示")
					.setView(input)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!TextUtils.isEmpty(input.getText()))
										tv_tip.setText(input.getText());
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		case R.id.rl_iiii:
			final EditText input1 = new EditText(GameTActivity.this);
			new AlertDialog.Builder(this)
					.setTitle("设置谜底")
					.setView(input1)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									if (!TextUtils.isEmpty(input1.getText()))
										tv_answer.setText(input1.getText());
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		case R.id.iv_back:
			this.finish();
			break;
		}
	}
	public void resetInfo(final String user_phone1,final String name1, final String gender1, final String photo1,
			final String game1, final String tip1, final String answer1,final String birthday1,final String book1,final String film1,final String mood1) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				Message msg = new Message();
				msg.what = HandlerID.SHOW_PRO_DIALOG;
				msg.obj = "正在更新";
				mHandler.sendMessage(msg);

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject
					.put(Constants.ResetUserInfo.RequestParams.USER_PHONE, user_phone1);
					jsonObject
							.put(Constants.ResetUserInfo.RequestParams.NAME, name1);
					jsonObject.put(Constants.ResetUserInfo.RequestParams.GENDER,
							gender1);
					jsonObject.put(Constants.ResetUserInfo.RequestParams.TIP, tip1);
					jsonObject.put(Constants.ResetUserInfo.RequestParams.ANSWER,
							answer1);
					jsonObject.put(Constants.ResetUserInfo.RequestParams.BIRTHDAY, birthday1);
					jsonObject.put(Constants.ResetUserInfo.RequestParams.BOOK, book1);
					jsonObject.put(Constants.ResetUserInfo.RequestParams.FILM, film1);
					jsonObject.put(Constants.ResetUserInfo.RequestParams.MOOD, mood1);

					String data = EncryptUtils.GetRsaEncrypt(jsonObject
							.toString()) + "&" + photo1 + "&" + game1;

					HttpUtils.sendReqData(Constants.ID.RESETUSERINFO, data,
							GameTActivity.this);
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
		if (id == Constants.ID.RESETUSERINFO) {
			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);
			if (Constants.ResponseInfo.CODE_SUCCESS.equals(resCode)) {
				
				User user = new User();
				user.setUser_phone(CacheUtils.GetUserPhone());
				user.setName(CacheUtils.GetName());
				user.setGender(CacheUtils.GetGender());
				user.setPhoto(CacheUtils.GetPhoto());
				user.setGame(PhotoUtils.DrawableToString(myCanvas.getDraw()));
				user.setTip(tv_tip.getText().toString());
				user.setAnswer(tv_answer.getText().toString());
				user.setBirthday(CacheUtils.GetBirthday());
				user.setBook(CacheUtils.GetBook());
				user.setFilm(CacheUtils.GetFilm());
				user.setCompany_school(CacheUtils.GetCompany_school());
				user.setMood(CacheUtils.GetMood());

				CacheUtils.SetUserCache(user);
				
				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
				
				Intent intent = new Intent(this, MainjacklinActivity.class);
				startActivity(intent);
			} else {
				String resMsg = resp
						.optString(Constants.ResponseParams.RES_MSG);

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
				if (resMsg != null) {
					Message msg = new Message();
					msg.what = HandlerID.SHOW_ERROR_DIALOG;
					msg.obj = resMsg;
					mHandler.sendMessage(msg);
				}
			}
		}
		
	}
}
