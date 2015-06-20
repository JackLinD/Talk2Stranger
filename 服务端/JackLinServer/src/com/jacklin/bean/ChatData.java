package com.jacklin.bean;

import java.io.Serializable;

public class ChatData implements Serializable{
	private static final long serialVersionUID = 10086L;
	
	/** 消息类型 */
	public enum Type{
		/** 聊天信息*/
		CHATTING,
		/** 离线信息 */
		OFFLINE_MSG,
		/** 注销登陆 */
		LOGOUT
	}
	
	/** 用户ID，对应线程标识 */
	public static class ID implements Serializable {
		private static final long serialVersionUID = 10085L;
		private String user_phone;
		public String getUser_phone() {
			return user_phone;
		}
		public void setUser_phone(String user_phone) {
			this.user_phone = user_phone;
		}
	}
	
	/** 消息数据 */
	public static class MSG implements Serializable{
		private static final long serialVersionUID = 10084L;
		private Type type;
		private String fromUser;
		private String toUser;
		private String msg;
		private String time;
		
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}
		public String getFromUser() {
			return fromUser;
		}
		public void setFromUser(String fromUser) {
			this.fromUser = fromUser;
		}
		public String getToUser() {
			return toUser;
		}
		public void setToUser(String toUser) {
			this.toUser = toUser;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
	}
}
