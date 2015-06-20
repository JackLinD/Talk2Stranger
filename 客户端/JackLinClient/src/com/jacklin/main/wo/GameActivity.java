package com.jacklin.main.wo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacklin.db.CacheUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.PhotoUtils;

public class GameActivity extends Activity {
	private ImageView iv,iv_back;
	private TextView tv1,tv2;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.jacklin_wo_game);
	initView();
}
public void initView(){
	iv=(ImageView) findViewById(R.id.iv);
	tv1=(TextView) findViewById(R.id.tv_my_answer);
	tv2=(TextView) findViewById(R.id.tv_my_tip);
	iv_back=(ImageView) findViewById(R.id.iv_back);
	iv_back.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			GameActivity.this.finish();
		}
	});
	if (!TextUtils.isEmpty(CacheUtils.GetGame()))
		iv.setImageDrawable(PhotoUtils.StringToDrawable(CacheUtils
				.GetGame()));
	tv1.setText(CacheUtils.GetAnswer());
	tv2.setText(CacheUtils.GetTip());
}
public void click(View view){
	Intent intent=new Intent(this,GameTActivity.class);
	startActivity(intent);
}
}
