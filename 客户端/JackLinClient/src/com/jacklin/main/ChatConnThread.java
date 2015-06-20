package com.jacklin.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.jacklin.bean.ChatData;
import com.jacklin.constant.Constants;
import com.jacklin.db.CacheUtils;
import com.jacklin.utils.MsgUtils;

import android.content.Context;
import android.util.Log;

public class ChatConnThread extends Thread {
	private static final String TAG = ChatConnThread.class.getSimpleName();


	private Context mContext;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean flag = true;

	public ChatConnThread(Context ctx) {
		mContext = ctx;
	}

	@Override
	public void run() {
		while (flag) {
			try {
				checkConnSocket();
				Object obj = in.readObject();
				if (obj instanceof ChatData.MSG) {
					ChatData.MSG msg = (ChatData.MSG) obj;
					switch (msg.getType()) {
					case CHATTING:
						MsgUtils.ShowChattingMsg(mContext, msg);
						break;
					default:
						break;
					}
				}
				Thread.sleep(250);
			} catch (Exception e) {
				e.printStackTrace();
				closeSocket();
			}
		}
		closeSocket();
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 */
	public void sendMsg(ChatData.MSG msg) {
		try {
			checkConnSocket();
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查连接，如果断开需要重新连接
	 */
	public synchronized void checkConnSocket() {
		while (flag && !isOnLine()) {
			try {
				socket = new Socket(Constants.SERVER_IP, Constants.SERVER_PORT);
				socket.setKeepAlive(true);
				out = new ObjectOutputStream(socket.getOutputStream());

				ChatData.ID id = new ChatData.ID();
				id.setUser_phone(CacheUtils.GetUserPhone());

				out.writeObject(id);
				out.flush();

				in = new ObjectInputStream(socket.getInputStream());
				Thread.sleep(250);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断客户端连接
	 * 
	 * @return
	 */
	public boolean isOnLine() {
		if (socket == null) {
			return false;
		}
		boolean ret = true;
		try {
			socket.sendUrgentData(0xFF);
		} catch (IOException e) {
			ret = false;
			closeSocket();
		}
		return ret;

	}

	/**关闭连接线程*/
	public void closeConn()
	{
		flag = false;
	}
	
	/**
	 * 关闭socket
	 */
	private void closeSocket() {
		try {
			if (socket != null) {
				socket.close();
				socket = null;
				Log.d(TAG, "关闭Socket");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
