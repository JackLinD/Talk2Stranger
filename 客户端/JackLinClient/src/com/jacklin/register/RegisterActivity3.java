package com.jacklin.register;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jacklin.constant.Constants;
import com.jacklin.constant.HandlerID;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.login.LoginActivity;
import com.jacklin.utils.MyCanvas;
import com.jacklin.utils.PhotoUtils;
import com.jacklin.utils.ViewUtils;

@SuppressLint({ "ShowToast", "HandlerLeak" })
public class RegisterActivity3 extends Activity implements
		HttpCallBackListener, OnClickListener {
	private MyCanvas myCanvas;
	private TextView tv_tip, tv_answer;
	private Button bt_back, bt_over;
	private RelativeLayout rl1, rl2;
	private Paint mPaint;
	private String user_phone, password, name, gender, photo, game, tip,
			answer;
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
				Intent intent = new Intent(RegisterActivity3.this,
						LoginActivity.class);
				startActivity(intent);
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
		setContentView(R.layout.jacklin_registerthree);
		initView();
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		mContext = this;
		myCanvas = (MyCanvas) findViewById(R.id.iv);
		initPaint();
		myCanvas.setPaint(mPaint);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_answer = (TextView) findViewById(R.id.tv_answer);
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_over = (Button) findViewById(R.id.bt_over);
		rl1 = (RelativeLayout) findViewById(R.id.rl_kkkk);
		rl2 = (RelativeLayout) findViewById(R.id.rl_iiii);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
		bt_back.setOnClickListener(this);
		bt_over.setOnClickListener(this);
	}

	/**
	 * 初始化画笔
	 */
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

	/**
	 * 初始化数据
	 */
	public void initValue() {
		Intent intent = getIntent();
		user_phone = intent.getStringExtra("user_phone");
		password = intent.getStringExtra("password");
		name = intent.getStringExtra("name");
		gender = intent.getStringExtra("gender");
		photo = intent.getStringExtra("photo");
		game = PhotoUtils.DrawableToString(myCanvas.getDraw());
		tip = tv_tip.getText().toString();
		answer = tv_answer.getText().toString();
	}

	/**
	 * 注册
	 * 
	 * @param user_phone1
	 * @param password1
	 * @param name1
	 * @param gender1
	 * @param photo1
	 * @param game1
	 */
	public void register(final String user_phone1, final String password1,
			final String name1, final String gender1, final String photo1,
			final String game1, final String tip1, final String answer1) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				Message msg = new Message();
				msg.what = HandlerID.SHOW_PRO_DIALOG;
				msg.obj = "正在注册";
				mHandler.sendMessage(msg);

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put(Constants.Register.RequestParams.USER_PHONE,
							user_phone1);
					jsonObject.put(Constants.Register.RequestParams.PASSWORD,
							password1);
					jsonObject
							.put(Constants.Register.RequestParams.NAME, name1);
					jsonObject.put(Constants.Register.RequestParams.GENDER,
							gender1);
					jsonObject.put(Constants.Register.RequestParams.TIP, tip1);
					jsonObject.put(Constants.Register.RequestParams.ANSWER,
							answer1);

					String data = EncryptUtils.GetRsaEncrypt(jsonObject
							.toString()) + "&" + photo1 + "&" + game1;

					HttpUtils.sendReqData(Constants.ID.REGISTER, data,
							RegisterActivity3.this);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
					mHandler.sendEmptyMessage(HandlerID.CONNECT_TIMEOUT_DIALOG);
				}
			}
		}).start();
	}

	/**
	 * HTTP回调函数
	 */
	@Override
	public void httpCallBack(int id, JSONObject resp) {
		if (id == Constants.ID.REGISTER) {
			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);
			if (Constants.ResponseInfo.CODE_SUCCESS.equals(resCode)) {
				Intent intent = new Intent(this, LoginActivity.class);

				startActivity(intent);

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			Intent intent = new Intent(this, RegisterActivity2.class);
			this.finish();
			startActivity(intent);
			break;

		case R.id.bt_over:
			myCanvas.saveImage();
			initValue();
			if (TextUtils.isEmpty(answer)) {
				Toast.makeText(this, "答案不能为空", 0).show();
			} else {
				register(user_phone, password, name, gender, photo, game, tip,
						answer);
			}
			break;
		case R.id.rl_kkkk:
			final EditText input = new EditText(RegisterActivity3.this);
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
			final EditText input1 = new EditText(RegisterActivity3.this);
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
		}

	}

}
