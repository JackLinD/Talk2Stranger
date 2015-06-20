package com.jacklin.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 获取好友Map集合
 * @author john
 *
 */
public class Friends {
	public List<User> friends=new ArrayList<User>();
	public Map<String,User> friendsMap=new HashMap<String, User>();
	
	public List<User> getFriends() {
		return friends;
	}
	
	public Map<String, User> getFriendsMap() {
		for(int i=0;i<friends.size();i++){
			User user=friends.get(i);
			friendsMap.put(user.getUser_phone(), user);
		}
		return friendsMap;
	}
	
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
}
