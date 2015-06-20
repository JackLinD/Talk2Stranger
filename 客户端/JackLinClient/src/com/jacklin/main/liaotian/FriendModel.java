package com.jacklin.main.liaotian;

import com.jacklin.utils.PhotoUtils;

import android.graphics.drawable.Drawable;

public class FriendModel{
	private String user_phone;
	private String name;
	private String gender;
	private Drawable photo;
	
	public String getUser_Phone() {
		return user_phone;
	}
	public void setUser_Phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public Drawable getPhoto() {
		return photo;
	}
	public void setPhoto(Drawable photo) {
		this.photo = photo;
	}
	public void setPhoto(String photo) {
		this.photo = PhotoUtils.StringToDrawable(photo);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}
