package com.jacklin.main;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jacklin.constant.Constants;
import com.jacklin.db.CacheUtils;
import com.jacklin.db.DataUtils;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.MsgUtils;
import com.jacklin.utils.ThreadUtils;

public class MainjacklinActivity extends FragmentActivity implements
		OnClickListener, HttpCallBackListener {
	private static final String TAG = MainjacklinActivity.class.getSimpleName();
	private ImageView iva, ivb, ivc;
	private RelativeLayout rla, rlb, rlc;
	private Fragment fm1, fm2, fm3, fm;
	private ChatConnThread mConnThread;
	private Handler mChatConnHandler;
	private Handler mMultiHandler;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_main);
		initView();
		initThread();
	}

	/******************************** 主界面线程管理 **************************************/
	public void initThread() {
		mMultiHandler = ThreadUtils.GetMultiHandler(TAG);
		mChatConnHandler = ThreadUtils.GetMultiHandler(TAG + "_ChatConn");

		mMultiHandler.post(mGetContactsListThread);
		mConnThread = new ChatConnThread(mContext);
		mChatConnHandler.post(mConnThread);

		MsgUtils.SetConnThrea(mConnThread);
		DataUtils.Init(mContext, CacheUtils.GetUserPhone());
	}

	// 获取好友
	Thread mGetContactsListThread = new Thread(new Runnable() {
		@Override
		public void run() {
			JSONObject reqJson = new JSONObject();
			try {
				reqJson.put(Constants.GetFriends.RequestParams.USER_PHONE,
						CacheUtils.GetUserPhone());
				final String data = EncryptUtils.GetRsaEncrypt(reqJson
						.toString());
				HttpUtils.sendReqData(Constants.ID.GETFRIENDS, data,
						MainjacklinActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
				mMultiHandler.postDelayed(this, 10000);
			}
		}
	});

	/******************************** 主界面线程管理 **************************************/

	/******************************** 底部标签点击事件 **************************************/
	public void initView() {
		mContext = this;
		iva = (ImageView) findViewById(R.id.iv_yaoyiyao);
		ivb = (ImageView) findViewById(R.id.iv_xiaoxi);
		ivc = (ImageView) findViewById(R.id.iv_wo);
		rla = (RelativeLayout) findViewById(R.id.rl_yaoyiyao);
		rlb = (RelativeLayout) findViewById(R.id.rl_xiaoxi);
		rlc = (RelativeLayout) findViewById(R.id.rl_wo);
		rla.setOnClickListener(this);
		rlb.setOnClickListener(this);
		rlc.setOnClickListener(this);

		if (fm1 == null) {
			fm1 = new YaoyiyaoFragment();
		}
		if (!fm1.isAdded()) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content, fm1).commit();
			fm = fm1;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_yaoyiyao:
			if (fm1 == null) {
				fm1 = new YaoyiyaoFragment();
			}
			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					fm1);
			iva.setBackgroundResource(R.drawable.icon_r2_c2);
			ivb.setBackgroundResource(R.drawable.icon_r6_c5);
			ivc.setBackgroundResource(R.drawable.icon_r6_c10);
			break;
		case R.id.rl_xiaoxi:
			if (fm2 == null) {
				fm2 = new LiaotianFragment();
			}
			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					fm2);
			iva.setBackgroundResource(R.drawable.shake);
			ivb.setBackgroundResource(R.drawable.icon_r3_c6);
			ivc.setBackgroundResource(R.drawable.icon_r6_c10);
			break;
		case R.id.rl_wo:
			if (fm3 == null) {
				fm3 = new WoFragment();
			}
			addOrShowFragment(getSupportFragmentManager().beginTransaction(),
					fm3);
			iva.setBackgroundResource(R.drawable.shake);
			ivb.setBackgroundResource(R.drawable.icon_r6_c5);
			ivc.setBackgroundResource(R.drawable.icon_r1_c9);
			break;
		}

	}

	private void addOrShowFragment(FragmentTransaction transaction,
			Fragment fragment) {
		if (fm == fragment)
			return;
		transaction.remove(fm).add(R.id.content, fragment).commit();
		fm = fragment;
	}

	/******************************** 底部标签点击事件 **************************************/

	@Override
	public void httpCallBack(int id, JSONObject resp) {
		if (id == Constants.ID.GETFRIENDS) {
			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);
			if ("100".equals(resCode)) {
				//更新好友列表数据
				DataUtils.UpdateFriends(resp);
				//根据好友列表更新聊天会话的好友头像和昵称
				DataUtils.UpdateChatListInfo(CacheUtils.GetFriends());
				//获取离线消息
				MsgUtils.GetOfflineMsg();
				//之后每隔一个小时获取好友列表,以便更新好友的昵称和头像的修改
				mMultiHandler.postDelayed(mGetContactsListThread, 60*60*1000);
			}else
			{
				//失败则在10秒后再次获取好友列表
				mMultiHandler.postDelayed(mGetContactsListThread, 10000);
			}
		}
	}

}
