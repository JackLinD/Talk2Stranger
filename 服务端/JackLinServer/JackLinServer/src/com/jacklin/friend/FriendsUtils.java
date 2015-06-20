package com.jacklin.friend;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jacklin.constant.Constants;
import com.jacklin.db.Friends;
import com.jacklin.db.HibernateUtils;
import com.jacklin.db.User;

/**
 * 好友管理工具类
 * 
 * @author john
 * 
 */
public class FriendsUtils {
	static Logger logger = Logger.getLogger(FriendsUtils.class);

	/**
	 * 获取用户好友
	 * 
	 * @param user_phone
	 * @return
	 */
	public static JSONObject GetFriends(String user_phone) {
		JSONObject respJson = new JSONObject();
		Friends friends = (Friends) HibernateUtils.get(Friends.class,
				user_phone);
		if (friends == null) {
			logger.info("手机号：" + user_phone + "尚未有好友");
			return respJson;
		}

		List<String> friendList = friends.getFriendList();
		JSONObject friendsJson = new JSONObject();
		int index = 0;
		User friend;
		for (int i = 0; i < friendList.size(); i++) {
			JSONArray friendInfo = new JSONArray();
			friend = (User) HibernateUtils.get(User.class, friendList.get(i));
			if (friend == null) {
				continue;
			}
			friendInfo.add(0, friend.getName());
			friendInfo.add(1, friend.getGender());
			friendInfo.add(2, friend.getPhoto());
			friendInfo.add(3, friend.getBirthday());
			friendInfo.add(4, friend.getCompany_school());
			friendInfo.add(5, friend.getBook());
			friendInfo.add(6, friend.getFilm());
			friendInfo.add(7, friend.getMood());
			friendsJson.element(index + "", friendInfo);
			index++;
		}
		respJson.element(Constants.GetFriends.ResponeParams.NUM, index);
		respJson.element(Constants.GetFriends.ResponeParams.FRIENDS,
				friendsJson.toString());
		return respJson;

	}

	/**
	 * 添加好友
	 * 
	 * @param user_phone
	 * @param friend_phone
	 * @return
	 */
	public static boolean AddFriend(String user_phone, String friend_phone) {
		boolean bAddFriend = true;
		Friends friends_user = (Friends) HibernateUtils.get(Friends.class,
				user_phone);
		List<String> friendList;
		if (friends_user == null) {
			friends_user = new Friends();
			friends_user.setUser_phone(user_phone);
			friends_user.setFriends(friend_phone);
			bAddFriend = HibernateUtils.add(friends_user);
		} else {
			friendList = friends_user.getFriendList();
			if (!friendList.contains(friend_phone)) {
				friendList.add(friend_phone);
				friends_user.setFriendList(friendList);
				bAddFriend = HibernateUtils.update(friends_user);
			}
		}

		Friends friends_friend = (Friends) HibernateUtils.get(Friends.class,
				friend_phone);
		if (friends_friend == null) {
			friends_friend = new Friends();
			friends_friend.setUser_phone(friend_phone);
			friends_friend.setFriends(user_phone);
			bAddFriend = HibernateUtils.add(friends_friend);
		} else {
			friendList = friends_friend.getFriendList();
			if (!friendList.contains(user_phone)) {
				friendList.add(user_phone);
				friends_friend.setFriendList(friendList);
				bAddFriend = HibernateUtils.update(friends_friend);
			}
		}
		return bAddFriend;
	}

	/**
	 * 移除好友
	 * 
	 * @param user_phone
	 * @param friend_phone
	 * @return
	 */
	public static boolean RemoveFriend(String user_phone, String friend_phone) {
		boolean bRemoveFriend = true;
		List<String> friendList;
		Friends friends_user = (Friends) HibernateUtils.get(Friends.class,
				user_phone);
		if (friends_user == null) {

		} else {
			friendList = friends_user.getFriendList();
			friendList.remove(friend_phone);
			friends_user.setFriendList(friendList);
			bRemoveFriend = HibernateUtils.update(friends_user);
		}

		Friends friends_friend = (Friends) HibernateUtils.get(Friends.class,
				friend_phone);

		if (friends_friend == null) {

		} else {
			friendList = friends_friend.getFriendList();
			friendList.remove(user_phone);
			friends_friend.setFriendList(friendList);
			bRemoveFriend = HibernateUtils.update(friends_friend);
		}
		return bRemoveFriend;

	}
	public static JSONObject SearchFriend(String friend_phone){
		JSONObject respJson = new JSONObject();
		User friend=(User) HibernateUtils.get(User.class, friend_phone);
		if(friend==null){
			logger.info("手机号：" + friend_phone + "尚未不存在");
			return respJson;
		}
		respJson.element(Constants.SearchFriend.ResponeParams.FRIEND_PHONE, friend.getUser_phone());
		respJson.element(Constants.SearchFriend.ResponeParams.NAME, friend.getName());
		respJson.element(Constants.SearchFriend.ResponeParams.GENDER, friend.getGender());
		respJson.element(Constants.SearchFriend.ResponeParams.PHOTO, friend.getPhoto());
		respJson.element(Constants.SearchFriend.ResponeParams.BIRTHDAY, friend.getBirthday());
		respJson.element(Constants.SearchFriend.ResponeParams.BOOK, friend.getBook());
		respJson.element(Constants.SearchFriend.ResponeParams.FILM, friend.getFilm());
		respJson.element(Constants.SearchFriend.ResponeParams.COMPANY_SCHOOL, friend.getCompany_school());
		respJson.element(Constants.SearchFriend.ResponeParams.MOOD, friend.getMood());
		return respJson;
	}

}
