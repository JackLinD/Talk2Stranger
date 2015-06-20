package com.jacklin.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacklin.jacklinclient.R;
import com.jacklin.main.yaoyiyao.MapActivity;
import com.jacklin.main.yaoyiyao.ShakeListener;
import com.jacklin.main.yaoyiyao.ShakeListener.OnShakeListener;

@SuppressLint({ "HandlerLeak", "InflateParams" })
public class YaoyiyaoFragment extends Fragment {
	private ShakeListener mShakeListener;
	@SuppressWarnings("unused")
	private Vibrator mVibrator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mVibrator = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		mShakeListener = new ShakeListener(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mShakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				mShakeListener.stop();
				Intent intent = new Intent(getActivity(),
						MapActivity.class);
				getActivity().overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				startActivity(intent);
			}
		});
		return inflater.inflate(R.layout.jacklin_yaoyiyao_fragment, null);
	}
}
