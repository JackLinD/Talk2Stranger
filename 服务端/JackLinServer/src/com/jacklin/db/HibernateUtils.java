package com.jacklin.db;

import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * hibernate工具类，增删改查
 * 
 * @author john
 * 
 */
@SuppressWarnings("deprecation")
public class HibernateUtils {
	private static SessionFactory sessionFactory;
	private static ThreadLocal<Session> session = new ThreadLocal<Session>();

	private HibernateUtils() {
	}

	static {
		Configuration cfg = new Configuration();
		cfg.configure();
		sessionFactory = cfg.buildSessionFactory();
	}

	/**
	 * 获取Seesion工厂
	 * 
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 获取Seesion
	 * 
	 * @return
	 */
	public static Session getSession() {
		return sessionFactory.openSession();
	}

	/**
	 * 如果当前线程没有Seesion，就新建一个Seesion
	 * 
	 * @return
	 */
	public static Session getThreadLocalSession() {
		Session s = (Session) session.get();
		if (s == null) {
			s = getSession();
			session.set(s);
		}
		return s;
	}

	/**
	 * 关闭Seesion
	 */
	public static void closeSession() {
		Session s = (Session) session.get();
		if (s != null) {
			s.close();
			session.set(null);
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean add(Object obj) {
		Session s = null;
		boolean bRet = false;
		try {
			s = getSession();
			Transaction tx = s.beginTransaction();
			s.save(obj);
			tx.commit();
			bRet = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		return bRet;
	}

	/**
	 * 更新数据
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean update(Object obj) {
		Session s = null;
		boolean bRet = false;
		try {
			s = getSession();
			Transaction tx = s.beginTransaction();
			s.update(obj);
			tx.commit();
			bRet = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		return bRet;
	}

	/**
	 * 删除数据
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean delete(Object obj) {
		Session s = null;
		boolean bRet = false;
		try {
			s = getSession();
			Transaction tx = s.beginTransaction();
			s.delete(obj);
			tx.commit();
			bRet = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		return bRet;
	}

	/**
	 * 查询数据
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static Object get(Class<?> clazz, Serializable id) {
		Session s = null;
		Object obj = null;
		try {
			s = getSession();
			obj = s.get(clazz, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		return obj;
	}
}
