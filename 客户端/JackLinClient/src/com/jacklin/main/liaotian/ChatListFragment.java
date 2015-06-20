package com.jacklin.main.liaotian;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.db.DataUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.MsgUtils;
import com.jacklin.utils.MyListView;
import com.jacklin.utils.MyListView.OnRefreshListener1;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("InflateParams")
public class ChatListFragment extends Fragment {

	private Context mContext;
	private View mView;
	private ChatListAdapter mAdapter;
	private List<ChatListModel> mSourceDataList;
	private MyListView myListView;

	private BroadcastReceiver mBroadcast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		// 注册更新聊天列表的广播
		mBroadcast = new MsgBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.Actions.CHAT_LIST);
		mContext.registerReceiver(mBroadcast, intentFilter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.jacklin_chatlist, null);
		myListView = (MyListView) mView.findViewById(R.id.list);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSourceDataList = new ArrayList<ChatListModel>();
		mSourceDataList=DataUtils.GetChatList();
		mAdapter = new ChatListAdapter(mContext, mSourceDataList);
		myListView.setAdapter(mAdapter);
		myListView.setonRefreshListener1(new OnRefreshListener1() {
			@Override
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						updateChatList();
					}
				}.execute(null, null, null);
			}
		});
		myListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, ChattingActivity.class);
				
				Adapter adapter = myListView.getAdapter();
				ChatListModel model = (ChatListModel) adapter.getItem(position);
				User user = DataUtils.GetFriend(model.getFriend_Phone());
				
				//如果这个会话有未读消息，则需要重置
				if(model.getUnread() > 0)
				{
					model.setUnread(0);
					//更新聊天列表数据
					mSourceDataList = DataUtils.UpdateChatList(model.getFriend_Phone(), model);
					mAdapter.updateListView(mSourceDataList);
				}
				
				intent.putExtra(Constants.Flags.FRIEND_INFO, user);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		updateChatList();
		// 存储当前广播名 回到聊天列表界面，则恢复广播
		MsgUtils.SetCurrBroadCast(Constants.Actions.CHAT_LIST);
	}

	@Override
	public void onPause() {
		super.onPause();
		MsgUtils.ClearCurrBroadCast();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBroadcast != null)
			mContext.unregisterReceiver(mBroadcast);
	}

	class MsgBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			updateChatList();
		}
	}

	public void updateChatList() {
		mSourceDataList = DataUtils.GetChatList();
		mAdapter.updateListView(mSourceDataList);
	}
}
