package com.jacklin.main.wo;

import java.io.File;
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
import com.jacklin.utils.ViewUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class InfoActivity extends Activity implements OnClickListener,HttpCallBackListener{
	ImageView iv,iv_back;
	TextView tv_name, tv_gender, tv_mood, tv_birthday, tv_book, tv_film;
	Button bt_over;
	RelativeLayout rl_name, rl_gender, rl_mood, rl_birthday, rl_book, rl_film;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private String[] items1 = new String[] { "男", "女" };
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
		setContentView(R.layout.jacklin_wo_info);
		initView();
	}

	public void initView() {
		mContext = this;
		bt_over=(Button) findViewById(R.id.bt_over);
		iv = (ImageView) findViewById(R.id.iv);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_mood = (TextView) findViewById(R.id.tv_mood);
		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		tv_book = (TextView) findViewById(R.id.tv_book);
		tv_film = (TextView) findViewById(R.id.tv_film);
		rl_name = (RelativeLayout) findViewById(R.id.rl_name);
		rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
		rl_mood = (RelativeLayout) findViewById(R.id.rl_mood);
		rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
		rl_book = (RelativeLayout) findViewById(R.id.rl_book);
		rl_film = (RelativeLayout) findViewById(R.id.rl_film);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		
		if (!TextUtils.isEmpty(CacheUtils.GetPhoto()))
			iv.setImageDrawable(PhotoUtils.StringToDrawable(CacheUtils
					.GetPhoto()));
		tv_name.setText(CacheUtils.GetName());
		tv_gender.setText(CacheUtils.GetGender());
		tv_mood.setText(CacheUtils.GetMood());
		tv_birthday.setText(CacheUtils.GetBirthday());
		tv_book.setText(CacheUtils.GetBook());
		tv_film.setText(CacheUtils.GetFilm());

		
		iv.setOnClickListener(this);
		rl_name.setOnClickListener(this);
		rl_gender.setOnClickListener(this);
		rl_mood.setOnClickListener(this);
		rl_birthday.setOnClickListener(this);
		rl_book.setOnClickListener(this);
		rl_film.setOnClickListener(this);
		bt_over.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv:
			new AlertDialog.Builder(this).setTitle("设置头像")
					.setItems(items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								dialog.dismiss();
								Intent intent = new Intent(Intent.ACTION_PICK,
										null);
								intent.setDataAndType(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										"image/*");
								startActivityForResult(intent, 1);
								break;
							case 1:
								dialog.dismiss();
								Intent intent1 = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												"jacklin.jpg")));
								startActivityForResult(intent1, 2);

								break;
							}

						}
					}).show();
			break;
		case R.id.rl_name:
			final EditText input = new EditText(InfoActivity.this);
			new AlertDialog.Builder(this)
					.setTitle("设置昵称")
					.setView(input)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!TextUtils.isEmpty(input.getText()))
										tv_name.setText(input.getText());
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
		case R.id.rl_gender:
			new AlertDialog.Builder(this).setTitle("选择性别")
					.setItems(items1, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								dialog.dismiss();
								tv_gender.setText("男");
								break;
							case 1:
								dialog.dismiss();
								tv_gender.setText("女");

								break;
							}

						}
					}).show();
			break;
		case R.id.rl_mood:
			final EditText input1 = new EditText(InfoActivity.this);
			new AlertDialog.Builder(this)
					.setTitle("设置心情")
					.setView(input1)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!TextUtils.isEmpty(input1.getText()))
										tv_mood.setText(input1.getText());
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
		case R.id.rl_birthday:
			final EditText input2 = new EditText(InfoActivity.this);
			new AlertDialog.Builder(this)
					.setTitle("设置出生日期")
					.setView(input2)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!TextUtils.isEmpty(input2.getText()))
										tv_birthday.setText(input2.getText());
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
		case R.id.rl_book:
			final EditText input3 = new EditText(InfoActivity.this);
			new AlertDialog.Builder(this)
					.setTitle("设置喜欢的书籍")
					.setView(input3)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!TextUtils.isEmpty(input3.getText()))
										tv_book.setText(input3.getText());
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
		case R.id.rl_film:
			final EditText input4 = new EditText(InfoActivity.this);
			new AlertDialog.Builder(this)
					.setTitle("设置喜欢的电影")
					.setView(input4)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!TextUtils.isEmpty(input4.getText()))
										tv_film.setText(input4.getText());
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
		case R.id.bt_over:
			String user_phone=CacheUtils.GetUserPhone();
			String name=tv_name.getText().toString();
			String gender=tv_gender.getText().toString();
			String photo=PhotoUtils.DrawableToString(iv.getDrawable());
			String mood=tv_mood.getText().toString();
			String birthday=tv_birthday.getText().toString();
			String book=tv_book.getText().toString();
			String film=tv_film.getText().toString();
			String tip=CacheUtils.GetTip();
			String answer=CacheUtils.GetAnswer();
			String game=CacheUtils.GetGame();
			resetInfo(user_phone,name, gender, photo, game, tip, answer, birthday, book, film, mood);
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
							InfoActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
					mHandler.sendEmptyMessage(HandlerID.CONNECT_TIMEOUT_DIALOG);
				}
			}
		}).start();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_CANCELED) {
		switch (requestCode) {
		case 1:
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(data.getData(), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 150);
			intent.putExtra("outputY", 150);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 3);
			break;
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/jacklin.jpg");
			Intent intent1 = new Intent("com.android.camera.action.CROP");
			intent1.setDataAndType(Uri.fromFile(temp), "image/*");
			intent1.putExtra("crop", "true");
			intent1.putExtra("aspectX", 1);
			intent1.putExtra("aspectY", 1);
			intent1.putExtra("outputX", 150);
			intent1.putExtra("outputY", 150);
			intent1.putExtra("return-data", true);
			startActivityForResult(intent1, 3);
			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					Drawable drawable = new BitmapDrawable(photo);
					iv.setImageDrawable(drawable);
				}
			}
			break;
		}}
	}

	@Override
	public void httpCallBack(int id, JSONObject resp) {
		if (id == Constants.ID.RESETUSERINFO) {
			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);
			if (Constants.ResponseInfo.CODE_SUCCESS.equals(resCode)) {
				
				User user = new User();
				user.setUser_phone(CacheUtils.GetUserPhone());
				user.setName(tv_name.getText().toString());
				user.setGender(tv_gender.getText().toString());
				user.setPhoto(PhotoUtils.DrawableToString(iv.getDrawable()));
				user.setGame(CacheUtils.GetGame());
				user.setTip(CacheUtils.GetTip());
				user.setAnswer(CacheUtils.GetAnswer());
				user.setBirthday(tv_birthday.getText().toString());
				user.setBook(tv_book.getText().toString());
				user.setFilm(tv_film.getText().toString());
				user.setCompany_school(CacheUtils.GetCompany_school());
				user.setMood(tv_mood.getText().toString());

				CacheUtils.SetUserCache(user);
				
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
}
