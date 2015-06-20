package com.jacklin.reset;

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
 * 用户重置密码
 * 
 * @author john
 * 
 */
public class ResetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ResetPasswordServlet.class);

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

		// 获取请求JSON数据
		JSONObject reqJson = hdb.getReqJson();
		JSONObject respJson = new JSONObject();

		// 获取重置密码具体信息
		String user_phone = reqJson
				.optString(Constants.ResetPassword.RequestParams.USER_PHONE);
		String new_password = reqJson
				.optString(Constants.ResetPassword.RequestParams.NEW_PASSWORD);
		String next_new_password = reqJson
				.optString(Constants.ResetPassword.RequestParams.NEXT_NEW_PASSWORD);

		// 参数缺失
		if (user_phone.isEmpty() || new_password.isEmpty()
				|| next_new_password.isEmpty()) {
			logger.info("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}

		// 判断用户是否存在
		User user = (User) HibernateUtils.get(User.class, user_phone);
		if (user != null) {
			if (new_password.equals(next_new_password)) {
				user.setPassword(new_password);
				boolean bRestPassword = HibernateUtils.update(user);
				if (bRestPassword) {
					logger.debug("手机号：" + user_phone + "重置密码成功");
					respJson.element(Constants.ResponseParams.RES_CODE,
							Constants.ResponseInfo.CODE_SUCCESS);
					respJson.element(Constants.ResponseParams.RES_MSG,
							Constants.ResponseInfo.MSG_SUCCESS);
					out.write(respJson.toString());
					return;
				}else{
					logger.debug("手机号：" + user_phone + "重置密码失败");
					respJson.element(Constants.ResponseParams.RES_CODE,
							Constants.ResetPassword.ErrorInfo.CODE_ID_UPDATEFAILED);
					respJson.element(Constants.ResponseParams.RES_MSG,
							Constants.ResetPassword.ErrorInfo.MSG_ID_UPDATEFAILED);
					out.write(respJson.toString());
					return;
				}
			} else {
				logger.debug("手机号：" + user_phone + "密码不匹配");
				respJson.element(Constants.ResponseParams.RES_CODE,
						Constants.ResetPassword.ErrorInfo.CODE_PSD_UNMATCH);
				respJson.element(Constants.ResponseParams.RES_MSG,
						Constants.ResetPassword.ErrorInfo.MSG_PSD_UNMATCH);
				out.write(respJson.toString());
				return;
			}
		} else {//(这里可以不要)
			logger.debug("手机号：" + user_phone + "尚未注册");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResetPassword.ErrorInfo.CODE_ID_UNRESGISTER);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResetPassword.ErrorInfo.MSG_ID_UNRESGISTER);
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
