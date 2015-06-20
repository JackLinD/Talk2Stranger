package com.jacklin.utils;
import android.content.Context;
import android.content.Intent;

import com.jacklin.bean.ChatData;
import com.jacklin.bean.ChatData.MSG;
import com.jacklin.bean.ChatData.Type;
import com.jacklin.bean.User;
import com.jacklin.constant.Constants;
import com.jacklin.db.CacheUtils;
import com.jacklin.db.DataUtils;
import com.jacklin.main.ChatConnThread;
import com.jacklin.main.liaotian.ChatListModel;
import com.jacklin.main.liaotian.ChattingModel;

public class MsgUtils {
	private static ChatConnThread connThread;
	private static String currBroadcast;
	
	public static void SetConnThrea(ChatConnThread thread){
		connThread=thread;
	}
	
	public static void GetOfflineMsg(){
		ChatData.MSG msg=new ChatData.MSG();
		msg.setType(ChatData.Type.OFFLINE_MSG);
		msg.setFromUser(CacheUtils.GetUserPhone());
		
		connThread.sendMsg(msg);
	}
	
	public static void SendMsg(ChatData.MSG msg){
		connThread.sendMsg(msg);
	}
	
	public static void CloseConn(){
		if(connThread.isOnLine()){
			MSG msg=new MSG();
			msg.setFromUser(CacheUtils.GetUserPhone());
			msg.setType(Type.LOGOUT);
			SendMsg(msg);
		}
		connThread.closeConn();
	}
	
	/*****************************消息显示********************************/
	public static void ShowChattingMsg(Context ctx,MSG msg){
		if(msg.getFromUser().endsWith(currBroadcast)){
			Intent intent=new Intent(Constants.Actions.CHATTING_PREFIX+msg.getFromUser());
			intent.putExtra(Constants.Flags.MSG, msg);
			ctx.sendBroadcast(intent);
		}
		
		String friend_phone=msg.getFromUser();
		User friend=CacheUtils.GetFriend(friend_phone);
		{
			ChattingModel model=new ChattingModel();
			model.setPhoto(friend.getPhoto());
			model.setSend(false);
			model.setMsg(msg.getMsg());
			model.setTime(msg.getTime());
			model.setShowTime(true);
			
			//添加一条聊天记录
			DataUtils.AddChattingItem(friend_phone, model, true);
		}
		{
			//聊天列表
			ChatListModel model = new ChatListModel();
			model.setPhoto(friend.getPhoto());
			model.setTitle(friend.getName());
			model.setUnread(1);
			model.setFriend_Phone(friend_phone);
			model.setContent(msg.getMsg());
			model.setTime(msg.getTime());
			
			//更新聊天列表
			DataUtils.UpdateChatList(friend_phone, model);
		}
		if(Constants.Actions.CHAT_LIST.equals(currBroadcast)){
			Intent intent=new Intent(Constants.Actions.CHAT_LIST);
			ctx.sendBroadcast(intent);
		}
	}
	
	/**存储当前的Broadcast*/
	public static void SetCurrBroadCast(String broadcastName)
	{
		currBroadcast = broadcastName;
	}
	
	/**清除当前的Broadcast*/
	public static void ClearCurrBroadCast()
	{
		currBroadcast = null;
	}
	
	
}
