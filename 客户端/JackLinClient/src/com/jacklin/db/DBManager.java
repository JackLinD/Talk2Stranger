package com.jacklin.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacklin.bean.Friends;
import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.main.liaotian.ChatListModel;
import com.jacklin.main.liaotian.ChattingModel;

public class DBManager {
	private static final String NAME_PREFERENCE = "com.jacklin.db";

	private SQLiteDatabase mSQLite;
	private Context mContext;
	private DBHelper mDBHelper;
	@SuppressWarnings("unused")
	private SharedPreferences mSharedPref;

	public DBManager(Context context, String user_phone) {
		mContext = context;
		mDBHelper = new DBHelper(mContext, user_phone);
		mSQLite = mDBHelper.getWritableDatabase();
		mSharedPref = mContext.getSharedPreferences(NAME_PREFERENCE,
				Context.MODE_PRIVATE);
	}

	public void closeSQLite() {
		if (mSQLite != null)
			mSQLite.close();
	}

	/************************************* 好友列表 ****************************************/
	/**
	 * 从服务器端更新数据库中好友列表
	 * 
	 * @param jsonObject
	 */
	public void UpdateFriends(JSONObject jsonObject) {
		mSQLite.delete(DBHelper.FRIENDS, null, null);
		int num = jsonObject.optInt(Constants.GetFriends.ResponeParams.NUM);
		JSONObject friendsJson = jsonObject
				.optJSONObject(Constants.GetFriends.ResponeParams.FRIENDS);
		JSONArray friendsArr;

		mSQLite.beginTransaction();
		try {
			for (int i = 0; i < num; i++) {
				friendsArr = friendsJson.optJSONArray(i + "");
				mSQLite.execSQL("insert into " + DBHelper.FRIENDS
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { friendsArr.opt(0), friendsArr.opt(1),
								friendsArr.opt(2), friendsArr.opt(3),
								friendsArr.opt(4), friendsArr.opt(5),
								friendsArr.opt(6), friendsArr.opt(7),
								friendsArr.opt(8), friendsArr.opt(9),
								friendsArr.opt(10), friendsArr.opt(11) });
			}

			mSQLite.setTransactionSuccessful();
		} finally {

			mSQLite.endTransaction();
		}
	}

	/**
	 * 从缓存中更新数据库中好友列表
	 * 
	 * @param friends
	 */
	public void UpdateFriends(Friends friends) {
		List<User> friendlist = friends.getFriends();
		User friend;

		mSQLite.beginTransaction();
		try {
			for (int i = 0; i < friendlist.size(); i++) {
				friend = friendlist.get(i);
				mSQLite.execSQL(
						"insert into " + DBHelper.FRIENDS
								+ " values(?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { friend.getUser_phone(),
								friend.getName(), friend.getGender(),
								friend.getPhoto(), friend.getGame(),
								friend.getTip(), friend.getAnswer(),
								friend.getBirthday(), friend.getBook(),
								friend.getFilm(), friend.getCompany_school(),
								friend.getMood() });
			}

			mSQLite.setTransactionSuccessful();
		} finally {

			mSQLite.endTransaction();
		}
	}

	/**
	 * 从数据库获取好友列表
	 * 
	 * @return
	 */
	public Friends GetFriends() {
		Friends friends = new Friends();
		List<User> friendsList = new ArrayList<User>();

		Cursor cursor = mSQLite.rawQuery("select * from " + DBHelper.FRIENDS,
				null);
		while (cursor.moveToNext()) {
			User user = new User();

			user.setUser_phone(cursor.getString(cursor
					.getColumnIndex("user_phone")));
			user.setName(cursor.getString(cursor.getColumnIndex("name")));
			user.setGender(cursor.getString(cursor.getColumnIndex("gender")));
			user.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
			user.setGame(cursor.getString(cursor.getColumnIndex("game")));
			user.setTip(cursor.getString(cursor.getColumnIndex("tip")));
			user.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
			user.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
			user.setBook(cursor.getString(cursor.getColumnIndex("book")));
			user.setFilm(cursor.getString(cursor.getColumnIndex("film")));
			user.setCompany_school(cursor.getString(cursor
					.getColumnIndex("company_school")));
			user.setMood(cursor.getString(cursor.getColumnIndex("mood")));

			friendsList.add(user);
		}

		cursor.close();

		friends.setFriends(friendsList);

		return friends;
	}

	/************************************* 聊天列表 ****************************************/
	public List<ChatListModel> GetChatList(){
		List<ChatListModel> chatList=new ArrayList<ChatListModel>();
		Cursor cursor=mSQLite.rawQuery("select * from "+DBHelper.CHAT_LIST,null);
		while(cursor.moveToNext()){
			ChatListModel model=new ChatListModel();
			String friend_phone=cursor.getString(cursor.getColumnIndex("user_phone"));
			model.setFriend_Phone(friend_phone);
			model.setContent(cursor.getString(cursor.getColumnIndex("content")));
			model.setTime(cursor.getString(cursor.getColumnIndex("time")));
			model.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
			Cursor c=mSQLite.rawQuery("select * from "+DBHelper.FRIENDS+" where user_phone=?", new String[]{friend_phone});
			if(c.moveToNext()){
				model.setTitle(c.getString(c.getColumnIndex("name")));
				model.setPhoto(c.getString(c.getColumnIndex("photo")));
			}
			chatList.add(model);
		}
		cursor.close();
		return chatList;
	}
	public void UpdateChatList(List<ChatListModel> chatList){
		mSQLite.delete(DBHelper.CHAT_LIST, null, null);
		ChatListModel model;
		mSQLite.beginTransaction();
		try{
			for(int i=0;i<chatList.size();i++){
				model=chatList.get(i);
				mSQLite.execSQL("insert into "+DBHelper.CHAT_LIST+" values(?,?,?,?)",new Object[]{model.getFriend_Phone(),model.getUnread(),model.getContent(),model.getTitle()});
				
			}
			mSQLite.setTransactionSuccessful();
		}finally{
			mSQLite.endTransaction();
		}
	}

	/************************************* 聊天记录 ****************************************/
	public List<ChattingModel> GetChattingList(String friend_phone){
		mDBHelper.createChattingTable(mSQLite, DBHelper.CHATTING_PREFIX+friend_phone);
		List<ChattingModel> list=new ArrayList<ChattingModel>();
		String photo="";
		Cursor cursor1=mSQLite.rawQuery("select * from "+DBHelper.FRIENDS+" where user_phone=?", new String[]{friend_phone});
		if(cursor1.moveToNext()){
			photo=cursor1.getString(cursor1.getColumnIndex("photo"));
		}
		cursor1.close();
		
		Cursor cursor2=mSQLite.rawQuery("select * from "+DBHelper.CHATTING_PREFIX+friend_phone, null);
		while(cursor2.moveToNext()){
			ChattingModel model=new ChattingModel();
			model.setPhoto(photo);
			model.setTime(cursor2.getString(cursor2.getColumnIndex("time")));
			model.setMsg(cursor2.getString(cursor2.getColumnIndex("msg")));
			model.setSend(cursor2.getInt(cursor2.getColumnIndex("send")));
			model.setShowTime(cursor2.getInt(cursor2.getColumnIndex("show_time")));
			list.add(0,model);
		}
		cursor2.close();
		return list;
	}
	
	public void UpdateChattingList(String friend_phone,List<ChattingModel> list){
		mDBHelper.createChattingTable(mSQLite, DBHelper.CHATTING_PREFIX+friend_phone);
		ChattingModel model;
		mSQLite.beginTransaction();
		try{
			for(int i=list.size()-1;i>=0;i--){
				model=list.get(i);
				int send=model.isSend()?1:0;
				int showTime=model.isShowTime()?1:0;
				mSQLite.execSQL("insert into "+DBHelper.CHATTING_PREFIX+friend_phone+" value(null,?,?,?,?)",new Object[]{model.getTime(),model.getMsg(),send,showTime});
			}
			mSQLite.setTransactionSuccessful();
		}finally{
			mSQLite.endTransaction();
		}
	}
	
	public void RemoveChattingList(String friend_phone) {
		// 如果该好友的聊天记录表不存在，则会创建
		mDBHelper.createChattingTable(mSQLite, DBHelper.CHATTING_PREFIX
				+ friend_phone);

		mSQLite.delete(DBHelper.CHATTING_PREFIX + friend_phone, null, null);
	}
}
