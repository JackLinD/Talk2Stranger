package com.jacklin.main.liaotian;

import java.util.ArrayList;
import java.util.List;

import com.jacklin.bean.Friends;
import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.db.DataUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.MyListView;
import com.jacklin.utils.MyListView.OnRefreshListener1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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

@SuppressLint("InflateParams")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FriendFragment extends Fragment {
	private Context mContext;
	private FriendAdapter mAdapter;
	private List<FriendModel> mSourceDataList;
	private View mView;
	private MyListView myListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.jacklin_friend, null);
		myListView=(MyListView) mView.findViewById(R.id.list);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSourceDataList = new ArrayList<FriendModel>();
		mSourceDataList=filledData(DataUtils.GetFriends());

		mAdapter = new FriendAdapter(mContext, mSourceDataList);
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
						updateContactsList();
					}
				}.execute(null, null, null);
			}
		});
		myListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Adapter adapter = myListView.getAdapter();
				FriendModel friend = (FriendModel) adapter.getItem(position);
				
				User user = DataUtils.GetFriend(friend.getUser_Phone());
				
				Intent intent = new Intent(getActivity(),
						FriendInfoActivity.class);
				intent.putExtra(Constants.Flags.FRIEND_INFO, user);
				getActivity().overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		updateContactsList();
	}
	
	
	private List<FriendModel> filledData(Friends friendList){
		List<FriendModel> mSortList = new ArrayList<FriendModel>();
		List<User> friends = friendList.getFriends();
		if(friends == null)
			return mSortList;
		
		for(int i=0; i<friends.size(); i++){
			FriendModel contactsModel = new FriendModel();
			User user = friends.get(i);
			contactsModel.setPhoto(user.getPhoto());
			contactsModel.setName(user.getName());
			contactsModel.setGender(user.getGender());
			contactsModel.setUser_Phone(user.getUser_phone());
			
			mSortList.add(contactsModel);
		}
		return mSortList;
	}
	
	public void updateContactsList()
	{
		mSourceDataList = filledData(DataUtils.GetFriends());
		mAdapter.updateListView(mSourceDataList);
	}
}
