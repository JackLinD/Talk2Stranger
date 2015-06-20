package com.jacklin.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;

import com.jacklin.bean.ChatData;
import com.jacklin.bean.ChatData.MSG;

public class ChatConnThread extends Thread {
	static Logger logger = Logger.getLogger(ChatConnThread.class);
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String userId;
	private volatile boolean flag = true;
	
	
	public ChatConnThread(Socket socket, ObjectInputStream in, String userId) throws IOException {
		this.socket = socket;
		this.in = in;
		this.userId = userId;
		this.out=new ObjectOutputStream(socket.getOutputStream());
	}
	
	/** 发送消息给本客户端*/
	public void sendMsg(ChatData.MSG msg){
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 发送离线消息给本客户端*/
	public void sendOfflineMsg(String userId){
		List<MSG> list=MsgManager.getOfflineMsg(userId);
		if(list==null)
			return;
		for(int i=0;i<list.size();i++){
			sendMsg(list.get(i));
		}
		MsgManager.removeMsgCache(userId);
	}
	
	@Override
	public void run() {
		while(flag){
			try {
				Object obj=in.readObject();
				if(obj instanceof ChatData.MSG){
					
					ChatData.MSG msg=(MSG) obj;
					System.out.println(msg.getMsg());
					switch (msg.getType()) {
					case CHATTING:
						ChatConnManager.sendMsg(msg);
						break;
						
					case OFFLINE_MSG:
						sendOfflineMsg(msg.getFromUser());
						break;
						
					case LOGOUT:
						ChatConnManager.removeConnThread(msg.getFromUser());
						break;
					}
				}
			}  catch (Exception e) {
				e.printStackTrace();
				flag=false;
				break;
			}
			
		}
		closeSocket();
	}

	
	private void closeSocket() {
		try {
			if(socket!=null){
				out.close();
				in.close();
				socket.close();
				socket=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void closeConn(){
		flag=false;
	}
	
	
	public boolean isOnLine(){
		boolean ret=true;
		try {
			socket.sendUrgentData(0xFF);
		} catch (IOException e) {
			logger.debug(userId + " 掉线了");
			e.printStackTrace();
		}
		return ret;
	}
}
