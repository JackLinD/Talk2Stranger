package com.jacklin.chat;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jacklin.bean.ChatData;

public class ChatConnManager {
	static Logger logger = Logger.getLogger(ChatConnManager.class);
	private static Map<String,ChatConnThread> connManager=new HashMap<String, ChatConnThread>();
	
	/** 添加客户端通信线程*/
	public static void addConnThread(String id,ChatConnThread connThread){
		logger.info(id + "创建通信连接");
		ChatConnThread conn=connManager.remove(id);
		if(conn!=null){
			conn.closeConn();
		}
		connManager.put(id, connThread);
	}
	
	/** 移除客户端通信线程*/
	public static void removeConnThread(String id){
		ChatConnThread connThread=connManager.remove(id);
		if(connThread!=null){
			connThread.closeConn();
		}
	}
	
	/** 给指定客户端发送消息*/
	public static void sendMsg(ChatData.MSG msg){
		String toUser=msg.getToUser();
		ChatConnThread connThread=connManager.get(toUser);
		if(connThread!=null&&connThread.isOnLine()){
			connThread.sendMsg(msg);
		}
		else if(connThread!=null &&!connThread.isOnLine()){
			removeConnThread(toUser);
			MsgManager.addMsgCache(toUser, msg);
		
		}
		else{
			MsgManager.addMsgCache(toUser, msg);
		}
	}
}
