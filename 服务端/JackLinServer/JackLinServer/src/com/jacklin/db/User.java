package com.jacklin.db;

/**
 * 数据库表user映射类User
 * 
 * @author john
 * 
 */
public class User {
	private String user_phone;// 用户ID
	private String name;// 用户昵称
	private String gender;// 用户性别
	private String photo;// 用户头像
	private String password;// 用户密码
	private String birthday;// 用户生日
	private String book;// 用户喜欢的书籍
	private String film;// 用户喜欢的电影
	private String company_school;// 用户所在的公司或学校
	private String mood;// 用户情感状况
	private String tip;// 你画我猜提示
	private String game;// 你画我猜
	private String answer;// 你画我猜答案
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getFilm() {
		return film;
	}

	public void setFilm(String film) {
		this.film = film;
	}

	public String getCompany_school() {
		return company_school;
	}

	public void setCompany_school(String company_school) {
		this.company_school = company_school;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}
}
