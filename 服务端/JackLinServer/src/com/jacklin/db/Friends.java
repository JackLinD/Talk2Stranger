package com.jacklin.db;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据库表friends映射类Friends
 * @author john
 *
 */
public class Friends {
	private String user_phone;//用户ID
	private String friends;//好友ID列表，用“&”分开
	
	public String getUser_phone() {
		return user_phone;
	}
	
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	
	public String getFriends() {
		return friends;
	}
	
	public void setFriends(String friends) {
		this.friends = friends;
	}
	/**
	 * 获取好友列表
	 * @return
	 */
	public List<String> getFriendList(){
		List<String> list=new ArrayList<String>();
		if(friends==null||friends.isEmpty()){
			return list;
		}
		String[] friend=friends.split("&");
		for(int i=0;i<friend.length;i++){
			list.add(friend[i]);
		}
		return list;
	}
	/**
	 * 设置用户好友列表
	 * @param list
	 */
	public void setFriendList(List<String> list){
		StringBuilder sb=new StringBuilder();
		int size=list.size();
		for(int i=0;i<size;i++){
			sb.append(list.get(i));
			if(i!=size-1){
				sb.append("&");
			}
		}
		friends=sb.toString();
	}
	
}
