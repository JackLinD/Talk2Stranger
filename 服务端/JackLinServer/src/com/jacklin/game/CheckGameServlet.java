package com.jacklin.game;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jacklin.constant.Constants;
import com.jacklin.db.HibernateUtils;
import com.jacklin.db.User;
import com.jacklin.http.HttpJson;
import com.jacklin.http.HttpUtils;

public class CheckGameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(CheckGameServlet.class);

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

		// 获取好友id
		String friend_phone = reqJson
				.optString(Constants.CheckGame.RequestParams.FRIEND_PHONE);
		String answer = reqJson
				.optString(Constants.CheckGame.RequestParams.ANSWER);

		// 参数缺失（可以不要）
		if (friend_phone.isEmpty() || answer.isEmpty()) {
			logger.error("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}

		// 查询好友是否存在
		User friend = (User) HibernateUtils.get(User.class, friend_phone);
		if (friend == null) {
			// 可以不要
			logger.debug("手机号：" + friend_phone + "该陌生人尚未注册");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.CheckGame.ErrorInfo.CODE_GETGAME_UNREGISTER);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.CheckGame.ErrorInfo.MSG_GETGAME_UNREGISTER);
			out.write(respJson.toString());
			return;
		}
		
		// 验证答案是否正确
		if (answer.equals(friend.getAnswer())) {
			logger.debug("手机号：" + friend_phone + "答案正确");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_SUCCESS);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_SUCCESS);

			// 返回陌生人详细信息
			respJson.element(Constants.SearchFriend.ResponeParams.FRIEND_PHONE,
					friend.getUser_phone());
			respJson.element(Constants.SearchFriend.ResponeParams.NAME,
					friend.getName());
			respJson.element(Constants.SearchFriend.ResponeParams.GENDER,
					friend.getGender());
			respJson.element(Constants.SearchFriend.ResponeParams.PHOTO,
					friend.getPhoto());
			respJson.element(Constants.SearchFriend.ResponeParams.BIRTHDAY,
					friend.getBirthday());
			respJson.element(Constants.SearchFriend.ResponeParams.BOOK,
					friend.getBook());
			respJson.element(Constants.SearchFriend.ResponeParams.FILM,
					friend.getFilm());
			respJson.element(
					Constants.SearchFriend.ResponeParams.COMPANY_SCHOOL,
					friend.getCompany_school());
			respJson.element(Constants.SearchFriend.ResponeParams.MOOD,
					friend.getMood());
			out.write(respJson.toString());
			return;
		} else {
			logger.debug("手机号：" + friend_phone + "答案不正确");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.CheckGame.ErrorInfo.CODE_CHECKGAME_ERROR);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.CheckGame.ErrorInfo.MSG_CHECKGAME_ERROR);
			out.write(respJson.toString());
			return;
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
