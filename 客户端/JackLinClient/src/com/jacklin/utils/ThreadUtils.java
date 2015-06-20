package com.jacklin.utils;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * 耗时线程工具类
 * @author Vaint
 *@E-mail vaintwyt@163.com
 *
 */
public class ThreadUtils {
	/**
	 * 获取开启新线程的Handler
	 * @param name 线程名称
	 * @return 多线程Handler
	 */
	public static Handler GetMultiHandler(String name) {
	    HandlerThread thread = new HandlerThread(name + "_MultiThread");
	    thread.start();
	    return new Handler(thread.getLooper());
	}
}
