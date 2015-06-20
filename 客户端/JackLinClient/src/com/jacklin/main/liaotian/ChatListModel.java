package com.jacklin.main.liaotian;


import com.jacklin.utils.PhotoUtils;

import android.graphics.drawable.Drawable;

public class ChatListModel{
	private String friend_phone;//好友ID
	private Drawable photo;	//头像
	private String title;//聊天会话标题：用户昵称
	private int unread;//未读消息数目
	private String content;//最新聊天内容
	private String time;//最新聊天时间
	public String getFriend_Phone() {
		return friend_phone;
	}
	public void setFriend_Phone(String friend_phone) {
		this.friend_phone = friend_phone;
	}
	public Drawable getPhoto() {
		return photo;
	}
	public String getPhotoString() {
		return PhotoUtils.DrawableToString(photo);
	}
	public void setPhoto(Drawable photo) {
		this.photo = photo;
	}
	public void setPhoto(String photo) {
		this.photo = PhotoUtils.StringToDrawable(photo);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getUnread() {
		return unread;
	}
	public void setUnread(int unread) {
		this.unread = unread;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
