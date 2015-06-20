package com.jacklin.main.yaoyiyao;
import com.jacklin.constant.Constants;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.PhotoUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MapGameActivity extends Activity implements OnClickListener{
	private ImageView iv,iv_back;
	private TextView tv_tip,tv_answer;
	private RelativeLayout rl;
	private Button bt;
	private LocationModel stranger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_location_game);
		iv=(ImageView) findViewById(R.id.iv_location);
		tv_tip=(TextView) findViewById(R.id.tv_location_tip);
		tv_answer=(TextView) findViewById(R.id.tv_location_answer);
		rl=(RelativeLayout) findViewById(R.id.rl_location);
		bt=(Button) findViewById(R.id.bt_over);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		bt.setOnClickListener(this);
		rl.setOnClickListener(this);
		Intent intent=getIntent();
		stranger=(LocationModel) intent.getSerializableExtra(Constants.Flags.STRANGER);
		iv.setImageDrawable(PhotoUtils.StringToDrawable(stranger.getGame()));
		tv_tip.setText(stranger.getTip());
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_over:
			if(!TextUtils.isEmpty(tv_answer.getText().toString())){
				if(stranger.getAnswer().equals(tv_answer.getText().toString())){
					Intent intent=new Intent(MapGameActivity.this,StrangerInfoActivity.class);
					intent.putExtra(Constants.Flags.STRANGER, stranger);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					startActivity(intent);
				}else{
					new AlertDialog.Builder(MapGameActivity.this)
					.setTitle("验证答案")
					.setMessage("答案不正确正确")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
				}
			}
			break;

		case R.id.rl_location:
			final EditText input = new EditText(MapGameActivity.this);
			new AlertDialog.Builder(MapGameActivity.this)
					.setTitle("填写答案")
					.setView(input)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (!TextUtils.isEmpty(input.getText()))
										tv_answer.setText(input.getText());
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
}
