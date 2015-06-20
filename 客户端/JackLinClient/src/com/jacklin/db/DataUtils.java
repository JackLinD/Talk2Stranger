package com.jacklin.db;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.jacklin.bean.Friends;
import com.jacklin.bean.User;
import com.jacklin.main.liaotian.ChatListModel;
import com.jacklin.main.liaotian.ChattingModel;

public class DataUtils {
	private static DBManager mDBManager;
	
	public static void Init(Context ctx,String user_phone){
		mDBManager=new DBManager(ctx, user_phone);
	}
	public static void Close(){
		mDBManager.closeSQLite();
	}
	
	/********************好友列表**************************/
	/**
	 * 更新好友列表
	 * @param jsonObject
	 */
	public static void UpdateFriends(JSONObject jsonObject)
	{
		mDBManager.UpdateFriends(jsonObject);
		CacheUtils.UpdateFriends(jsonObject);
	}
	public static void UpdateFriends(Friends friends){
		mDBManager.UpdateFriends(friends);
		CacheUtils.UpdateFriends(friends);
	}
	/**
	 * 添加一个好友
	 * @param friend
	 */
	public static void AddFriend(User friend)
	{
		Friends friendList=CacheUtils.AddFriend(friend);
		mDBManager.UpdateFriends(friendList);
	}
	/**
	 * 删除一个好友
	 * @param friend_phone
	 */
	public static void RemoveFriend(String friend_phone)
	{
		Friends friends=CacheUtils.RemoveFriend(friend_phone);
		mDBManager.UpdateFriends(friends);
		RemoveChat(friend_phone);
	}
	/**
	 * 获取好友列表
	 * @return
	 */
	public static Friends GetFriends()
	{
		Friends friends = CacheUtils.GetFriends();
		if(friends == null)
		{
			friends=mDBManager.GetFriends();
			CacheUtils.UpdateFriends(friends);
		}
		return friends;
	}
	/**
	 * 获取好友信息
	 * @param friend_phone
	 * @return
	 */
	public static User GetFriend(String friend_phone)
	{
		User friend = CacheUtils.GetFriend(friend_phone);
		if(friend_phone == null)
		{
			CacheUtils.UpdateFriends(mDBManager.GetFriends());
		}
		friend = CacheUtils.GetFriend(friend_phone);
		return friend;
	}

	
	/********************聊天列表**************************/
	/**
	 * 更新一天聊天会话，如果不出在，则创建
	 * @param friend_phone
	 * @param model
	 * @return
	 */
	public static List<ChatListModel> UpdateChatList(String friend_phone,ChatListModel model){
		List<ChatListModel> chatList=CacheUtils.UpdateChatList(friend_phone,model);
		mDBManager.UpdateChatList(chatList);
		return chatList;
	}
	/**
	 * 根据好友列表信息更新聊天会话信息，如头像和昵称
	 * @param friends
	 */
	public static void UpdateChatListInfo(Friends friends){
		List<ChatListModel> chatList=CacheUtils.UpdateChatList(friends);
		if(chatList!=null){
			mDBManager.UpdateChatList(chatList);
		}
	}
	/**
	 * 删除一个聊天会话，同时删除聊天记录
	 * @param friend_phone
	 */
	public static void RemoveChat(String friend_phone){
		boolean ret = CacheUtils.RemoveChat(friend_phone);
		if(ret)//聊天会话存在
		{
			mDBManager.UpdateChatList(CacheUtils.GetChatList());
			//删除聊天记录
			RemoveChattingList(friend_phone);
		}
	}
	/**
	 * 获得聊天列表
	 * @return
	 */
	public static List<ChatListModel> GetChatList(){
		List<ChatListModel> chatList =CacheUtils.GetChatList();
		if(chatList==null){
			chatList=mDBManager.GetChatList();
			CacheUtils.UpdateChatList(chatList);
		}
		return chatList;
	}

	
	/********************聊天记录**************************/
	/**
	 * 获得与该好友的聊天记录
	 * @param friend_phone
	 * @return
	 */
	public static List<ChattingModel> GetChattingList(String friend_phone)
	{
		List<ChattingModel> chattingList = CacheUtils.GetChattingList(friend_phone);
		if(chattingList==null){
			chattingList=mDBManager.GetChattingList(friend_phone);
			CacheUtils.UpdateChattingList(friend_phone, chattingList);
		}
		return chattingList;
	}
	/**
	 * 更新与该好友的聊天信息
	 * @param friendId
	 * @param chattingList
	 */
	public static void UpdateChattingList(String friendId, List<ChattingModel> chattingList)
	{
		CacheUtils.UpdateChattingList(friendId, chattingList);
		mDBManager.UpdateChattingList(friendId, chattingList);
	}
	/**
	 * 添加一条聊天信息
	 * @param friendId
	 * @param model
	 * @param isChangeFile
	 */
	public static void AddChattingItem(String friendId, ChattingModel model, boolean isChangeFile)
	{
		List<ChattingModel> list =CacheUtils.AddChattingItem(friendId, model);
		if(isChangeFile){
			mDBManager.UpdateChattingList(friendId, list);
		}
	}
	/**
	 * 删除对应聊天信息
	 * @param friendId
	 */
	public static void RemoveChattingList(String friendId)
	{
		CacheUtils.RemoveChattingList(friendId);
		mDBManager.RemoveChattingList(friendId);
	}


	/********************附近的人**************************//*
	*//**
	 * 更新附近一公里陌生人
	 * @param list
	 *//*
	public static void UpdateLocationList(List<LocationModel> list){
		CacheUtils.UpdateLocationList(list);
	}
	*//**
	 * 获得附近一公里陌生人
	 *//*
	public static List<LocationModel> GetLocationList(){
		List<LocationModel> list=CacheUtils.GetLocationList();
		return list;
	}
	*//**
	 * 获取某个陌生人的详细信息
	 * @param stranger_phone
	 * @return
	 *//*
	public static User GetStranger(String stranger_phone){
		User stranger=CacheUtils.GetStranger(stranger_phone);
		return stranger;
	}*/
}
