package com.jacklin.inforeset;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.jacklin.constant.Constants;
import com.jacklin.db.HibernateUtils;
import com.jacklin.db.User;
import com.jacklin.http.HttpJson;
import com.jacklin.http.HttpUtils;

/**
 * 重置用户信息
 * 
 * @author john
 * 
 */
public class ResetUserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ResetUserInfoServlet.class);

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
		// 获取用户信息JSON数据
		JSONObject reqJson = hdb.getReqJson();
		JSONObject respJson = new JSONObject();

		String photo=req.getParameter(Constants.ResetUserInfo.RequestParams.PHOTO);
		String game =req.getParameter(Constants.ResetUserInfo.RequestParams.GAME);
		
		// 获取用户信息具体数据
		String user_phone = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.USER_PHONE);
		String name = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.NAME);
		String gender = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.GENDER);
		/*String photo = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.PHOTO);*/
		String birthday = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.BIRTHDAY);
		String book = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.BOOK);
		String film = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.FILM);
		String company_school = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.COMPANY_SCHOOL);
		String mood = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.MOOD);
//		String game = reqJson
//				.optString(Constants.ResetUserInfo.RequestParams.GAME);
		String tip = reqJson
				.optString(Constants.ResetUserInfo.RequestParams.TIP);
		String answer=reqJson.optString(Constants.ResetUserInfo.RequestParams.ANSWER);

		// 参数缺失(可以不要)
		if (user_phone.isEmpty()) {
			logger.info("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}

		// 查询用户
		User user = (User) HibernateUtils.get(User.class, user_phone);
		user.setName(name);
		user.setGender(gender);
		user.setPhoto(photo);
		user.setBirthday(birthday);
		user.setBook(book);
		user.setFilm(film);
		user.setCompany_school(company_school);
		user.setMood(mood);
		user.setGame(game);
		user.setTip(tip);
		user.setAnswer(answer);

		// 更新用户信息是否成功
		boolean bResetUserInfo = HibernateUtils.update(user);
		if (bResetUserInfo) {
			logger.debug("手机号：" + user_phone + "更新用户信息成功");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_SUCCESS);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_SUCCESS);
			out.write(respJson.toString());
			return;
		} else {
			logger.debug("手机号：" + user_phone + "更新用户信息失败");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResetUserInfo.ErrorInfo.CODE_RESET_FAILED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResetUserInfo.ErrorInfo.MSG_RESET_FAILED);
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
