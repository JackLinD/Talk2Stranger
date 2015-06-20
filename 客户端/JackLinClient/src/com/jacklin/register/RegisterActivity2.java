package com.jacklin.register;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.PhotoUtils;

/**
 * 注册第二步：头像、昵称和性别，确认后连带上一步数据一起传递到下一步
 * 
 * @author john
 *
 */
@SuppressLint("ShowToast")
public class RegisterActivity2 extends Activity implements OnClickListener {
	private EditText et_name;
	private TextView tv_gender;
	private ImageView iv;
	private Button bt_back, bt_huaban;
	private String user_phone, password, photo, name, gender;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private String[] items1 = new String[] { "男", "女" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_registertwo);

		initView();
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		iv = (ImageView) findViewById(R.id.iv);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_huaban = (Button) findViewById(R.id.bt_regist_huaban);
		iv.setOnClickListener(this);
		tv_gender.setOnClickListener(this);
		bt_back.setOnClickListener(this);
		bt_huaban.setOnClickListener(this);

	}

	/**
	 * 初始化数据
	 */
	public void initValue() {
		Intent intent = getIntent();
		user_phone = intent.getStringExtra("user_phone");
		password = intent.getStringExtra("password");
		photo = PhotoUtils.DrawableToString(iv.getDrawable());
		name = et_name.getText().toString();
		gender = tv_gender.getText().toString();
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
	 * 选择头像的响应函数
	 */
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
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv:
			hideKeyBoard();
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
		case R.id.bt_back:
			Intent intent1 = new Intent(this, RegisterActivity1.class);
			this.finish();
			startActivity(intent1);
			break;
		case R.id.bt_regist_huaban:
			hideKeyBoard();
			initValue();

			if (TextUtils.isEmpty(name) || TextUtils.isEmpty(gender)) {
				Toast.makeText(this, "参数不为空", 0).show();
			} else {
				Intent intent = new Intent(this, RegisterActivity3.class);
				intent.putExtra("user_phone", user_phone);
				intent.putExtra("password", password);
				intent.putExtra("photo", photo);
				intent.putExtra("name", name);
				intent.putExtra("gender", gender);
				startActivity(intent);
			}
			break;
		case R.id.tv_gender:
			hideKeyBoard();
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
		}

	}
}
