package com.jacklin.main.yaoyiyao;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class MapApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);
	}
}
