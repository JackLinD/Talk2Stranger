package com.jacklin.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jacklin.bean.ChatData;
/**
 * 管理离线信息
 * @author john
 *
 */
public class MsgManager {
	static Logger logger = Logger.getLogger(MsgManager.class);
	private static Map<String,List<ChatData.MSG>> msgManager= new HashMap<String, List<ChatData.MSG>>();
	
	/** 添加离线缓存*/
	public static void addMsgCache(String id,ChatData.MSG msg){
		List<ChatData.MSG> list;
		if(msgManager.containsKey(id)){
			list=msgManager.get(id);
			list.add(msg);
		}else{
			list=new ArrayList<ChatData.MSG>();
			list.add(msg);
		}
		logger.debug("USER_PHONE:"+id+"有一条离线信息："+msg.getMsg());
		msgManager.put(id, list);
		System.out.println("缓存中"+list.get(0).getMsg());
	}
	
	/** 获得离线消息序列 */
	public static List<ChatData.MSG> getOfflineMsg(String id){
		return msgManager.get(id);
	}
	
	/** 移除消息序列 */
	public static void removeMsgCache(String id){
		msgManager.remove(id);
	}
}
