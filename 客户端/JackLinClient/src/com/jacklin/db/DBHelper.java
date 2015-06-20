package com.jacklin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	public static final String FRIENDS = "friends";
	public static final String LOCATIONS = "locations";
	public static final String CHAT_LIST = "chat_list";
	public static final String CHATTING_PREFIX = "chatting_";

	public DBHelper(Context context, String user_phone) {
		super(context, user_phone, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists "
				+ FRIENDS
				+ " (user_phone varchar(13) primary key,name varcher(20),gender varchar(10),photo text,game text,tip varchar(100),answer varchar(100),birthday varchar(100),book varchar(100),film varchar(100),company_school varchar(100),mood varchar(100))");
		db.execSQL("create table if not exists " 
				+ CHAT_LIST 
				+ " (user_phone varchar(20) primary key,unread integer, content varchar(10),time varchar(10))");

	}

	public void createChattingTable(SQLiteDatabase db, String dbName) {
		db.execSQL("create table if not exists "
				+ CHATTING_PREFIX+dbName
				+ " (id integer primary key autoincrement, time varchar(10), msg text, send integer, show_time integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
