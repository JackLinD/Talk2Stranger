package com.jacklin.utils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

public class ViewUtils {	
	/** 圆形进度条 */
	private static ProgressDialog mProDialog;
	/***/
	

	/** 显示圆形进度条 */
	public static void ShowProgressDialog(Context ctx, String msg) {
		mProDialog = new ProgressDialog(ctx);
		mProDialog.setMessage(msg);
		mProDialog.setCancelable(true);
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.show();
	}
	/** 显示的圆形进度条 */
	public static void ShowProgressDialog(Context ctx, String msg, boolean cancel) {
		mProDialog = new ProgressDialog(ctx);
		mProDialog.setMessage(msg);
		mProDialog.setCancelable(true);
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.show();
	}

	/** 隐藏圆形进度条 */
	public static void HideProgressDialog() {
		if (mProDialog != null && mProDialog.isShowing()) {
			mProDialog.dismiss();
			mProDialog = null;
		}
	}
	
	public static void ShowConnectTimeoutDialog(Context mContext) {
		new AlertDialog.Builder(mContext).setMessage("连接超时")
		.setPositiveButton("确定", null).show();
	}
	
	/** 提示用户错误信息 */
	public static void ShowErrorDialog(Context ctx, String msg) {
		if(msg==null)
			return;
		new AlertDialog.Builder(ctx).setMessage(msg)
				.setPositiveButton("确定", null).show();
	}
}
