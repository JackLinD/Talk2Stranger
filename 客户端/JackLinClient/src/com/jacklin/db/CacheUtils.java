package com.jacklin.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;








import com.jacklin.bean.Friends;
import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.main.liaotian.ChatListModel;
import com.jacklin.main.liaotian.ChattingModel;
import com.jacklin.main.yaoyiyao.LocationModel;

public class CacheUtils {
	
	
	
	/********************UserCache**************************/
	private static User user=null;
	
	
	public static void SetUserCache(User userx){
		user=userx;
	}
	public static User GetUserCache(){
		return user;
	}
	public static String GetUserPhone(){
		if(user!=null)
			return user.getUser_phone();
		return null;
	}
	public static void SetUserPhone(String user_phone){
		if(user!=null)
			user.setUser_phone(user_phone);
	}
	public static String GetName(){
		if(user!=null)
			return user.getName();
		return null;
	}
	public static void SetName(String name){
		if(user!=null)
			user.setName(name);
	}
	public static String GetGender(){
		if(user!=null)
			return user.getGender();
		return null;
	}
	public static void SetGender(String gender){
		if(user!=null)
			user.setGender(gender);
	}
	public static String GetPhoto(){
		if(user!=null)
			return user.getPhoto();
		return null;
	}
	public static void SetPhoto(String photo){
		if(user!=null)
			user.setPhoto(photo);
	}
	public static String GetGame(){
		if(user!=null)
			return user.getGame();
		return null;
	}
	public static void SetGame(String game){
		if(user!=null)
			user.setGame(game);
	}
	public static String GetTip(){
		if(user!=null)
			return user.getTip();
		return null;
	}
	public static void SetTip(String tip){
		if(user!=null)
			user.setTip(tip);
	}
	public static String GetAnswer(){
		if(user!=null)
			return user.getAnswer();
		return null;
	}
	public static void SetAnswer(String answer){
		if(user!=null)
			user.setAnswer(answer);
	}
	public static String GetBirthday(){
		if(user!=null)
			return user.getBirthday();
		return null;
	}
	public static void SetBirthday(String birthday){
		if(user!=null)
			user.setBirthday(birthday);
	}
	public static String GetBook(){
		if(user!=null)
			return user.getBook();
		return null;
	}
	public static void SetBook(String book){
		if(user!=null)
			user.setBook(book);
	}
	public static String GetFilm(){
		if(user!=null)
			return user.getFilm();
		return null;
	}
	public static void SetFilm(String film){
		if(user!=null)
			user.setFilm(film);
	}
	public static String GetCompany_school(){
		if(user!=null)
			return user.getCompany_school();
		return null;
	}
	public static void SetCompany_school(String company_school){
		if(user!=null)
			user.setCompany_school(company_school);
	}
	public static String GetMood(){
		if(user!=null)
			return user.getMood();
		return null;
	}
	public static void SetMood(String mood){
		if(user!=null)
			user.setMood(mood);
	}
	public static void ClearUser(){
		user=null;
	}
	
	/********************FriendListCache**************************/
	private static Friends friends=null;
	/**
	 * 从服务器端获取好友列表更新缓存中的好友列表
	 * @param jsonObject
	 */
	public static void UpdateFriends(JSONObject jsonObject){
		friends=new Friends();
		List<User> list=new ArrayList<User>();
		int num=jsonObject.optInt(Constants.GetFriends.ResponeParams.NUM);
		JSONObject friendsJson=jsonObject.optJSONObject(Constants.GetFriends.ResponeParams.FRIENDS);
		JSONArray friendsArr;
		
		for(int i=0;i<num;i++)
		{
			friendsArr = friendsJson.optJSONArray(i+"");
			User user = new User();
			user.setUser_phone(friendsArr.optString(0));
			user.setName(friendsArr.optString(1));
			user.setGender(friendsArr.optString(2));
			user.setPhoto(friendsArr.optString(3));
			user.setGame(friendsArr.optString(4));
			user.setTip(friendsArr.optString(5));
			user.setAnswer(friendsArr.optString(6));
			user.setBirthday(friendsArr.optString(7));
			user.setBook(friendsArr.optString(8));
			user.setFilm(friendsArr.optString(9));
			user.setCompany_school(friendsArr.optString(10));
			user.setMood(friendsArr.optString(11));
			
			list.add(user);
		}
		friends.setFriends(list);
	}
	/**
	 * 从内存中获取好友列表更新缓存中的好友列表
	 * @param friendss
	 */
	public static void UpdateFriends(Friends friendss)
	{
		friends = friendss;
	}
	public static Friends GetFriends(){
		return friends;
	}
	public static User GetFriend(String friend_phone){
		if(friends!=null){
			return friends.getFriendsMap().get(friend_phone);
		}
		return null;
	}
	public static Friends AddFriend(User friend){
		if(friends==null){
			friends=new Friends();
		}
		List<User> list=friends.getFriends();
		list.add(friend);
		friends.setFriends(list);
		return friends;
	}
	public static Friends RemoveFriend(String friend_phone){
		List<User> list=friends.getFriends();
		for(int i=0;i<list.size();i++){
			User friend=list.get(i);
			if(friend.getUser_phone().equals(friend_phone)){
				list.remove(i);
				friends.setFriends(list);
				return friends;
			}
		}
		return friends;
	}


	/********************ChatList**************************/
	private static List<ChatListModel> chatList=null;
	public static void UpdateChatList(List<ChatListModel> list){
		chatList=list;
	}
	public static List<ChatListModel> UpdateChatList(Friends friends){
		if(chatList == null)
			return null;
		
		for(int i=0;i<chatList.size();i++)
		{
			ChatListModel model = chatList.get(i);
			String friendId = model.getFriend_Phone();
			User user = friends.getFriendsMap().get(friendId);
			if(user!=null)
			{
				//更新数据
				model.setPhoto(user.getPhoto());
				model.setTitle(user.getName());
				//删除原数据
				chatList.remove(i);
				//插入新数据
				chatList.add(i, model);
			}
		}
		
		return chatList;
	}
	public static List<ChatListModel> UpdateChatList(String friend_phone,ChatListModel model){
		if(chatList == null)
			chatList = new ArrayList<ChatListModel>();
		
		int index = GetChatPosition(friend_phone);
		if(index != -1)//聊天会话已经存在
		{
			ChatListModel m = chatList.remove(index);//删除原记录
			//未读消息数目:原来+本次
			int unread = m.getUnread() + model.getUnread();
			model.setUnread(unread);
		}
		chatList.add(0, model);//添加到首位
		
		return chatList;
	}
	
	public static boolean RemoveChat(String friend_phone)
	{
		int index = GetChatPosition(friend_phone);
		if(index != -1)//聊天会话存在
		{
			chatList.remove(index);//删除原记录
			return true;
		}
		return false;
	}
	/**获得聊天列表*/
	public static List<ChatListModel> GetChatList()
	{
		return chatList;
	}
	
	public static int GetChatPosition(String friendId)
	{
		if(chatList == null)
			return -1;
		
		for(int i=0; i<chatList.size(); i++)
		{
			ChatListModel model = chatList.get(i);
			if(model.getFriend_Phone().equals(friendId))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/********************ChattingRecode**************************/
	private static Map<String, List<ChattingModel>> chattingMap = new HashMap<String, List<ChattingModel>>(); 
	/**根据好友ID获得对应的聊天消息缓存*/
	public static List<ChattingModel> GetChattingList(String friendId)
	{
		List<ChattingModel> list = null;
		if(chattingMap.containsKey(friendId))
		{
			list = chattingMap.get(friendId);
		}
		return list;
	}
	/**删除聊天记录*/
	public static void RemoveChattingList(String friendId)
	{
		chattingMap.remove(friendId);
	}
	
	/**更新与该好友的聊天消息<BR/>覆盖更新*/
	public static void UpdateChattingList(String friendId, List<ChattingModel> chattingList)
	{
			chattingMap.put(friendId, chattingList);
	}
	
	/**添加一条聊天消息到缓存*/
	public static List<ChattingModel> AddChattingItem(String friendId, ChattingModel chatting)
	{
		List<ChattingModel> list;
		if(chattingMap.containsKey(friendId))//已经存在缓存数据
		{
			list = chattingMap.get(friendId);
		}else
		{
			list = new ArrayList<ChattingModel>();
		}
		list.add(chatting);
		chattingMap.put(friendId, list);
		return list;
	}

	/********************LocationListCache**************************/
	private static List<LocationModel> locationList=null;
	
	public static void UpdateLocationList(JSONObject jsonObject)
	{
		List<LocationModel> list=new ArrayList<LocationModel>();
		int num = jsonObject.optInt(Constants.GetLocation.ResponeParams.NUM);
		JSONObject strangersJson = jsonObject
				.optJSONObject(Constants.GetLocation.ResponeParams.STRANGERS);
		JSONArray strangersArr;
		for(int i=0;i<num;i++)
		{
			strangersArr = strangersJson.optJSONArray(i+"");
			LocationModel stranger = new LocationModel();
			stranger.setUser_phone(strangersArr.optString(0));
			stranger.setName(strangersArr.optString(1));
			stranger.setGender(strangersArr.optString(2));
			stranger.setPhoto(strangersArr.optString(3));
			stranger.setGame(strangersArr.optString(4));
			stranger.setTip(strangersArr.optString(5));
			stranger.setAnswer(strangersArr.optString(6));
			stranger.setBirthday(strangersArr.optString(7));
			stranger.setBook(strangersArr.optString(8));
			stranger.setFilm(strangersArr.optString(9));
			stranger.setCompany_school(strangersArr.optString(10));
			stranger.setMood(strangersArr.optString(11));
			stranger.setLatitude(strangersArr.optDouble(12));
			stranger.setLongitude(strangersArr.optDouble(13));
			
			list.add(stranger);
		}
		locationList=list;
	}
	
	public static void UpdateLocationList(List<LocationModel> list){
		locationList=list;
	}
	public static List<LocationModel> GetLocationList(){
		if(locationList!=null)
			return locationList;
		return null;
	}
	public static LocationModel GetStranger(String stranger_phone){
		for(int i=0;i<locationList.size();i++){
			if(locationList.get(i).getStranger_phone().equals(stranger_phone)){
				return locationList.get(i);
			}
		}
		return null;
	}
}
