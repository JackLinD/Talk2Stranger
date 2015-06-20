package com.jacklin.register;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.jacklin.jacklinclient.R;

public class MyCustomDialog extends Dialog {
	
	public interface OnCustomDialogListener{
		public void back(String name);
	}
	
	private OnCustomDialogListener customDialogListener;

	protected MyCustomDialog(Context context,
			OnCustomDialogListener customDialogListener) {
		super(context);
		this.customDialogListener=customDialogListener;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_tanchu_gender);  


		RelativeLayout rl1=(RelativeLayout) findViewById(R.id.rl_nan);
		RelativeLayout rl2=(RelativeLayout) findViewById(R.id.rl_nvvv);
		rl1.setOnClickListener(clickListener);
		rl2.setOnClickListener(clickListener);
	}
	
	
	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_nan:
				customDialogListener.back("男");
				MyCustomDialog.this.dismiss();
				break;
			case R.id.rl_nvvv:
				customDialogListener.back("女");
				break;
			}
            
		}
};

}
