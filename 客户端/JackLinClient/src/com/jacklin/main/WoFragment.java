package com.jacklin.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jacklin.db.CacheUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.login.LoginActivity;
import com.jacklin.main.wo.GameActivity;
import com.jacklin.main.wo.InfoActivity;
import com.jacklin.utils.PhotoUtils;

@SuppressLint("InflateParams")
public class WoFragment extends Fragment implements OnClickListener{
	private SharedPreferences mSharedPreference;
	private static final String NAME_PREFERENCE = "com.jacklin.login.sharedpreference";
	private static final String KEY_USER_PHONE = "com.jacklin.login.user_phone";
	private static final String KEY_PASSWORD = "com.jacklin.login.password";
	private RelativeLayout rl_myhuaban;
	private Button bt_logout;
	private ImageView iv_myphoto;
	private View v;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.jacklin_wo_fragment, null);
		initView();
		return v;
	}
	
	public void initView(){
		rl_myhuaban=(RelativeLayout) v.findViewById(R.id.rl_myhuaban);
		bt_logout=(Button)v.findViewById(R.id.bt_logout);
		iv_myphoto=(ImageView) v.findViewById(R.id.iv_myphoto);
		if (!TextUtils.isEmpty(CacheUtils.GetPhoto()))
			iv_myphoto.setImageDrawable(PhotoUtils.StringToDrawable(CacheUtils
					.GetPhoto()));
		mSharedPreference = getActivity().getSharedPreferences(NAME_PREFERENCE,
				Context.MODE_PRIVATE);
		rl_myhuaban.setOnClickListener(this);
		bt_logout.setOnClickListener(this);
		iv_myphoto.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_myhuaban:
			Intent intent1=new Intent(getActivity(),GameActivity.class);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			startActivity(intent1);
			break;
		case R.id.bt_logout:
			saveLogin();
			Intent intent2=new Intent(getActivity(),LoginActivity.class);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			startActivity(intent2);
			break;
		case R.id.iv_myphoto:
			Intent intent =new Intent(getActivity(),InfoActivity.class);
			getActivity().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			startActivity(intent);
			break;
		
		}
		
	}
	
	public void saveLogin() {
		Editor editor = mSharedPreference.edit();
		editor.putString(KEY_USER_PHONE, CacheUtils.GetUserPhone());
		editor.putString(KEY_PASSWORD, "");
		editor.commit();
	}

}
