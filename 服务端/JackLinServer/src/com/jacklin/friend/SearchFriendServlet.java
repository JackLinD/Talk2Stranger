package com.jacklin.friend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jacklin.constant.Constants;
import com.jacklin.http.HttpJson;
import com.jacklin.http.HttpUtils;

public class SearchFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置字符编码集
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		// 解析参数
		HttpJson hdb = HttpUtils.ResolveParams(req);
		if (!hdb.isSuccess()) {
			out.write(hdb.getRespJson().toString());
			return;
		}

		// 获取JSON数据
		JSONObject reqJson = hdb.getReqJson();
		JSONObject respJson = new JSONObject();
		
		//获取好友id
		String friend_phone = reqJson
				.optString(Constants.SearchFriend.RequestParams.FRIEND_PHONE);
		
		//获取好友信息
		respJson = FriendsUtils.SearchFriend(friend_phone);
		out.write(respJson.toString());
		return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
