package com.jacklin.main.liaotian;
import com.jacklin.utils.PhotoUtils;

import android.graphics.drawable.Drawable;

public class ChattingModel {
	private boolean isSend;			//是否本用户发出的消息，用于显示时判断
	private boolean isShowTime;	//是否显示消息时间
	private String time;					//消息时间
	private Drawable photo;			//头像
	private String msg;						//消息
	public boolean isSend() {
		return isSend;
	}
	public void setSend(boolean isSend) {
		this.isSend = isSend;
	}
	//兼容SQLite没有boolean型 0:false,1:true
	public void setSend(int isSend) {
		if(isSend == 0)
		{
			this.isSend = false;
		}else
		{
			this.isSend = true;
		}
	}
	public boolean isShowTime() {
		return isShowTime;
	}
	public void setShowTime(boolean isShowTime) {
		this.isShowTime = isShowTime;
	}
	//兼容SQLite没有boolean型 0:false,1:true
	public void setShowTime(int isShowTime) {
		if(isShowTime == 0)
		{
			this.isShowTime = false;
		}else
		{
			this.isShowTime = true;
		}
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
