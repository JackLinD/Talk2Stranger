package com.jacklin.code;

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
 * 考虑到防止测试时发生手机验证服务停止而无法注册的情况，我们将手机验证功能去掉
 * @author john
 *
 */
public class PwdCheckVerifyCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(PwdCheckVerifyCodeServlet.class);

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

		// 获取注册信息JSON数据
		JSONObject reqJson = hdb.getReqJson();
		JSONObject respJson = new JSONObject();

		// 获取用户电话和验证码
		String user_phone = reqJson
				.optString(Constants.CheckVerifyCode.RequestParams.USER_PHONE);
		String verifycode = reqJson
				.optString(Constants.CheckVerifyCode.RequestParams.VERIFYCODE);
		
//		String password=reqJson.optString(Constants.CheckVerifyCode.RequestParams.PASSWORD);

//		String lasttime = reqJson
//				.optString(Constants.CheckVerifyCode.RequestParams.TIME_LONG);

		// 参数缺失
		if (user_phone.isEmpty()|| verifycode.isEmpty()) {
			logger.error("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}

		// 查询用户是否存在(可以不要)
		User user = (User) HibernateUtils.get(User.class, user_phone);
		if (user == null) {
			logger.info("手机号：" + user_phone + "尚未注册");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.GetVerifyCode.ErrorInfo.CODE_ID_UNEXISTED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.GetVerifyCode.ErrorInfo.MSG_ID_UNEXISTED);
			out.write(respJson.toString());
			return;
		}

		// 取出验证码
		JSONObject codeJson = (JSONObject) req
				.getSession()
				.getServletContext()
				.getAttribute(
						Constants.CheckVerifyCode.VERIFYCODE_SESSION
								+ user_phone);
		String code = codeJson.optString(Constants.CheckVerifyCode.VERIFYCODE);
//		Long time = codeJson.optLong(Constants.CheckVerifyCode.TIME_LONG);

		if (!code.isEmpty() && code.equals(verifycode)) {
//			long times = Long.parseLong(lasttime) - time;
//			if (times > 0 && times < 2 * 60 * 1000) {
				logger.info("手机号：" + user_phone + "校验验证码成功");
				respJson.element(Constants.ResponseParams.RES_CODE,
						Constants.ResponseInfo.CODE_SUCCESS);
				respJson.element(Constants.ResponseParams.RES_MSG,
						Constants.ResponseInfo.MSG_SUCCESS);
				out.write(respJson.toString());
				return;
//			}else{
//				logger.info("手机号：" + user_phone + "校验验证码超时");
//				out.write(respJson.toString());
//				return;
//			}
		} else {
			logger.info("手机号：" + user_phone + "校验验证码失败");
			respJson.element(
					Constants.ResponseParams.RES_CODE,
					Constants.CheckVerifyCode.ErrorInfo.CODE_CHECKVERIFYCODE_FAILED);
			respJson.element(
					Constants.ResponseParams.RES_MSG,
					Constants.CheckVerifyCode.ErrorInfo.MSG_CHECKVERIFYCODE_FAILED);
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
