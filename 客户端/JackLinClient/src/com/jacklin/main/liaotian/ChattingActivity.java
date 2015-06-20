package com.jacklin.main.liaotian;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jacklin.bean.ChatData;
import com.jacklin.bean.ChatData.MSG;
import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.db.CacheUtils;
import com.jacklin.db.DataUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.MsgUtils;
import com.jacklin.utils.MyListView;
import com.jacklin.utils.ThreadUtils;

@SuppressLint({ "ClickableViewAccessibility", "ShowToast" })
public class ChattingActivity extends Activity {
	private static final String TAG = ChattingActivity.class.getSimpleName();
	
	private Context mContext;
	private MyListView mListView;
	private Button mButton;
	private EditText mEditText;
	private ChattingAdapter mAdapter;
	private Handler mMultiHandler;
	
	private User mFriend;
	private String mFriend_Phone;
	private BroadcastReceiver mMsgBroadcast;
	
	private String mLastMsg;
	private String mLastTime;
	private long lastTime;
	private ImageView iv_back;
	
	private List<ChattingModel> mNewChattingList=new ArrayList<ChattingModel>();
	private List<ChattingModel> mChattingList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_chatting);
		
		mContext=this;
		mMultiHandler=ThreadUtils.GetMultiHandler(TAG);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mListView=(MyListView) findViewById(R.id.chatting_listView);
		mButton=(Button)findViewById(R.id.chatting_button);
		mEditText=(EditText) findViewById(R.id.chatting_editText);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChattingActivity.this.finish();
			}
		});
		
		mListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyBoard();
				return false;
			}
		});
		
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideKeyBoard();
				if(TextUtils.isEmpty(mEditText.getText().toString())){
					Toast.makeText(mContext, "发送内容不能为空", 0).show();
				}else{
					ChattingModel model=new ChattingModel();
					final ChatData.MSG Msg=new ChatData.MSG();
					long currTime=System.currentTimeMillis();
					String time=null;//对时间进行格式化
					//这里是给客户端看的
					model.setTime(time);
					model.setShowTime((currTime-lastTime)>60000);
					model.setPhoto(mFriend.getPhoto());
					model.setSend(true);
					model.setMsg(mEditText.getText().toString());
					
					//这是发送到服务器上的
					Msg.setTime(Long.toString(currTime));
					Msg.setFromUser("18316057270");
					Msg.setToUser("18316057271");
					Msg.setMsg(mEditText.getText().toString());
					
					lastTime=currTime;
					mLastMsg=mEditText.getText().toString();
					mLastTime=time;
					
					mNewChattingList.add(model);
					mChattingList.add(model);
					
					mAdapter=new ChattingAdapter(mContext, mChattingList);
					mListView.setAdapter(mAdapter);
					
					mMultiHandler.post(new Runnable() {
						public void run() {
							MsgUtils.SendMsg(Msg);
						}
					});
				}
			}
		});
		
		//获取聊天列表的数据
		Intent intent=getIntent();
		mFriend=(User) intent.getSerializableExtra(Constants.Flags.FRIEND_INFO);
		mFriend_Phone=mFriend.getUser_phone();
		
		//获取该好友的聊天记录
		mChattingList=DataUtils.GetChattingList(mFriend_Phone);
		
		mAdapter=new ChattingAdapter(mContext,mChattingList);
		mListView.setAdapter(mAdapter);
		
		//注册接收消息的广播
		IntentFilter intentFilter=new IntentFilter();
		mMsgBroadcast=new MsgBroadcastReceiver();
		intentFilter.addAction(Constants.Actions.CHATTING_PREFIX+mFriend_Phone);
		registerReceiver(mMsgBroadcast, intentFilter);
		
		//存储当前好友会话的广播名
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mNewChattingList.size() > 0)
		{
			//保存聊天记录
			DataUtils.UpdateChattingList(mFriend_Phone, mChattingList);
			
			//更新聊天列表
			ChatListModel model = new ChatListModel();
			model.setPhoto(mFriend.getPhoto());
			model.setTitle(mFriend.getName());
			model.setUnread(0);
			model.setFriend_Phone(mFriend_Phone);
			model.setContent(mLastMsg);
			model.setTime(mLastTime);
			
			DataUtils.UpdateChatList(mFriend_Phone, model);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mMsgBroadcast!=null){
			unregisterReceiver(mMsgBroadcast);
		}
	}
	
	/** 隐藏软键盘 */
	private void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}
	class MsgBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			MSG msg=(MSG) intent.getSerializableExtra(Constants.Flags.MSG);
			ChattingModel model=new ChattingModel();
			User friend=CacheUtils.GetFriend(msg.getFromUser());
			model.setPhoto(friend.getPhoto());
			model.setMsg(msg.getMsg());
			model.setSend(false);
			model.setTime(msg.getTime());
			long currTime=System.currentTimeMillis();
			model.setShowTime((currTime - lastTime) > 60000);
			lastTime=currTime;
			
			//记录聊天记录
			mNewChattingList.add(model);
			mChattingList.add(model);
			
			mAdapter=new ChattingAdapter(mContext,mChattingList);
			mListView.setAdapter(mAdapter);
		}
		
	}
}
	
	
