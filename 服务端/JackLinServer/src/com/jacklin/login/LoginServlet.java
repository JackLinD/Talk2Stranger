package com.jacklin.login;

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

/**
 * 用户登录
 * 需要考虑的问题：1、参数缺失；2、用户不存在；3、密码错误；4、登录成功。
 * @author john
 * 
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(LoginServlet.class);

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

		// 获取用户登录JSON数据
		JSONObject reqJson = hdb.getReqJson();
		JSONObject respJson = new JSONObject();

		// 获取用户登录信息
		String user_phone = reqJson
				.optString(Constants.Login.RequestParams.USER_PHONE);
		String password = reqJson
				.optString(Constants.Login.RequestParams.PASSWORD);

		// 参数缺失
		if (user_phone.isEmpty() || password.isEmpty()) {
			logger.error("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}

		// 查询用户是否存在
		User user = (User) HibernateUtils.get(User.class, user_phone);

		if (user != null) {
			String pwd = user.getPassword();
			if (password.equals(pwd)) {
				logger.info("手机号：" + user_phone + "登录成功");
				respJson.element(Constants.ResponseParams.RES_CODE,
						Constants.ResponseInfo.CODE_SUCCESS);
				respJson.element(Constants.ResponseParams.RES_MSG,
						Constants.ResponseInfo.MSG_SUCCESS);
				// 用于客户端缓存
				respJson.element(Constants.Login.ResponeParams.USER_PHONE,
						user.getUser_phone());
				respJson.element(Constants.Login.ResponeParams.NAME,
						user.getName());
				respJson.element(Constants.Login.ResponeParams.GENDER,
						user.getGender());
				respJson.element(Constants.Login.ResponeParams.PHOTO,
						user.getPhoto());
				respJson.element(Constants.Login.ResponeParams.BIRTHDAY,
						user.getBirthday());
				respJson.element(Constants.Login.ResponeParams.BOOK,
						user.getBook());
				respJson.element(Constants.Login.ResponeParams.FILM,
						user.getFilm());
				respJson.element(Constants.Login.ResponeParams.MOOD,
						user.getMood());
				respJson.element(Constants.Login.ResponeParams.GAME,
						user.getGame());
				respJson.element(Constants.Login.ResponeParams.TIP,
						user.getTip());
				respJson.element(Constants.Login.ResponeParams.ANSWER,
						user.getAnswer());
				out.write(respJson.toString());
				return;
			} else {
				logger.debug("手机号：" + user_phone + "密码不正确");
				respJson.element(Constants.ResponseParams.RES_CODE,
						Constants.Login.ErrorInfo.CODE_PSD_ERROR);
				respJson.element(Constants.ResponseParams.RES_MSG,
						Constants.Login.ErrorInfo.MSG_PSD_ERROR);
				out.write(respJson.toString());
				return;
			}
		} else {
			logger.debug("手机号：" + user_phone + "尚未注册");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.Login.ErrorInfo.CODE_ID_UNRESGISTER);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.Login.ErrorInfo.MSG_ID_UNRESGISTER);
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
