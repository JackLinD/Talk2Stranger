package com.jacklin.db;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
/**
 * 服务器开启和关闭的监听类
 * @author john
 *
 */
public class JDBCListener implements ServletContextListener {
	static Logger logger = Logger.getLogger(JDBCListener.class);
	/**
	 * 服务器关闭时要删除JDBC驱动
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.debug("服务器关闭了");
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		Driver driver = null;
		while(drivers.hasMoreElements()){
			try {
				driver=drivers.nextElement();
				DriverManager.deregisterDriver(driver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

	}

}
