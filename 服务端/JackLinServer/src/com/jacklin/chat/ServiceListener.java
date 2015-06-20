package com.jacklin.chat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceListener implements ServletContextListener {
	ChatServerThread serverThread=null;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(serverThread!=null&&!serverThread.isInterrupted()){
			serverThread.closeServer();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		if(serverThread==null){
			serverThread=new ChatServerThread();
			serverThread.start();
		}
	}

}
