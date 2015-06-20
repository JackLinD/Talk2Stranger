package com.jacklin.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.jacklin.bean.ChatData;
import com.jacklin.bean.ChatData.ID;
import com.jacklin.constant.Constants;

/**
 * 聊天服务线程
 * @author john
 *
 */
public class ChatServerThread extends Thread {
	static Logger logger = Logger.getLogger(ChatServerThread.class);
	
	private ServerSocket ss=null;
	private volatile boolean flag=true;
	
	@Override
	public void run() {
		logger.info("开启聊天服务");
		Socket socket=null;
		ObjectInputStream in = null;
		try {
			ss=new ServerSocket(Constants.SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(flag){
			try {
				socket=ss.accept();
				in=new ObjectInputStream(socket.getInputStream());
				
				Object obj=in.readObject();
				
				if(obj instanceof ChatData.ID){
					ChatData.ID id=(ID) obj;
					String userId=id.getUser_phone();
					ChatConnThread connThread=new ChatConnThread(socket, in, userId);
					ChatConnManager.addConnThread(userId, connThread);
					connThread.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if(in!=null)
						in.close();
					if(socket!=null)
						socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		closeSocket();
	}
	private void closeSocket() {
		if(ss!=null){
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void closeServer(){
		flag=false;
	}
}
