package com.jacklin.test;
import org.junit.Test;


import com.jacklin.db.HibernateUtils;
import com.jacklin.db.User;

/**
 * HibernateUtils测试类
 * @author john
 * 
 */
public class HibernateUtilsTest {
	
	@Test
	public void TestAdd() {
		User user = new User();
		user.setUser_phone("15625017635");
		user.setName("谢益欣");
		user.setGender("男");
		user.setPassword("123456");
		user.setBirthday("1993年06月01日");
		user.setBook("产品经理的生存之道");
		user.setFilm("小时到");
		user.setMood("还可以咯");
		user.setCompany_school("华南师范大学");
		HibernateUtils.add(user);
	}

	@Test
	public void TestUpdate() {
		User user = (User) HibernateUtils.get(User.class, "13726171747");
		user.setAnswer("kk");
		HibernateUtils.update(user);
	}

	@Test
	public void TestDelete() {
		User user = new User();
		user.setUser_phone("15625017635");
		HibernateUtils.delete(user);
		/*Friends friends=new Friends();
		friends.setUser_phone("110");
		HibernateUtils.delete(friends);*/
	}

	@Test
	public void TestGet() {
		String userPhone = "13726171747";
		User user = (User) HibernateUtils.get(User.class, userPhone);
		System.out.println("用户昵称：" + user.getName() + "\n用户性别："
				+ user.getGender() + "\n用户头像：" + user.getPhoto());
	}
}
