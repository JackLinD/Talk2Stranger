package com.jacklin.test;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.jacklin.friend.FriendsUtils;

public class FriendUtilsTest {
	@Test
	public void testAdd(){
		FriendsUtils.AddFriend("15625018356", "15625017635");
	}
	@Test
	public void testGet(){
		JSONObject friends=FriendsUtils.GetFriends("15625018356");
		System.out.println(friends.toString());
	}
	@Test
	public void testRemove(){
		FriendsUtils.RemoveFriend("15625018356", "13726171747");
	}
	@Test
	public void testSearch(){
		JSONObject friend=FriendsUtils.SearchFriend("13726171747");
		System.out.print(friend.toString());
	}
}
