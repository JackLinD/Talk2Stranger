package com.jacklin.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jacklin.jacklinclient.R;
import com.jacklin.login.LoginActivity;
/**
 * 注册第一步：手机号、密码和确认密码，完成后验证密码是否匹配，如果匹配则将数据传递给下一步
 * @author john
 *
 */
@SuppressLint("ShowToast")
public class RegisterActivity1 extends Activity {
	
	private EditText et_phone,et_password1,et_password2;
	private String phone,password1,password2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_registerone);
		initView();
	}
	
	public void initView(){
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_password1=(EditText) findViewById(R.id.et_password1);
		et_password2=(EditText) findViewById(R.id.et_password2);
	}

	public void initValue(){
		phone=et_phone.getText().toString().trim();
		password1=et_password1.getText().toString().trim();
		password2=et_password2.getText().toString().trim();
	}

	private void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void click(View view){
		initValue();
		hideKeyBoard();
		
		if(TextUtils.isEmpty(password2)||TextUtils.isEmpty(password1)||TextUtils.isEmpty(phone)){
			Toast.makeText(this, "参数不能为空", 0).show();
		}else if(phone.length()<11){
			Toast.makeText(this, "手机号码长度不匹配", 0).show();
		}else if(password1.length()<6){
			Toast.makeText(this, "密码长度不能小于6", 0).show();
		}else{
			if(password1.equals(password2)){
				Intent intent=new Intent(this,RegisterActivity2.class);
				intent.putExtra("user_phone", phone);
				intent.putExtra("password", password1);
				startActivity(intent);
			}else{
				Toast.makeText(this, "密码前后不一致", 0).show();
			}
		}
		
	}
	
	public void clicklogin(View view){
		Intent intent=new Intent(this,LoginActivity.class);
		startActivity(intent);
	}
	
	
}
