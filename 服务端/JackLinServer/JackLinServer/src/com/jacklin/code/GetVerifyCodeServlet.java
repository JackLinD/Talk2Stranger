package com.jacklin.code;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
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

public class GetVerifyCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(GetVerifyCodeServlet.class);

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

		// 获取用户电话
		String user_phone = reqJson
				.optString(Constants.GetVerifyCode.RequestParams.USER_PHONE);

		// 参数缺失
		if (user_phone.isEmpty()) {
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
			logger.info("手机号：" + user_phone + "已经注册");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.GetVerifyCode.ErrorInfo.CODE_ID_EXISTED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.GetVerifyCode.ErrorInfo.MSG_ID_EXISTED);
			out.write(respJson.toString());
			return;
		}
		
		//获取验证码
		int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
		
		boolean bSendVerifyCode=VerifyCodeUtils.sendVerifyCode(user_phone,mobile_code);
		
		if(bSendVerifyCode){
			logger.info("手机号：" + user_phone + "获取验证码成功，验证码为"+mobile_code);
			
			//存储验证码
			JSONObject codeJson = new JSONObject();
			codeJson.element(Constants.CheckVerifyCode.VERIFYCODE, mobile_code);
//			codeJson.element(Constants.CheckVerifyCode.TIME_LONG,
//					System.currentTimeMillis());
			ServletContext sc = req.getSession().getServletContext();
			sc.setAttribute(
					Constants.CheckVerifyCode.VERIFYCODE_SESSION
							+ user_phone, codeJson);
			
			
			
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_SUCCESS);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_SUCCESS);
			out.write(respJson.toString());
			return;
		}else{
			logger.info("手机号：" + user_phone + "获取验证码失败");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.GetVerifyCode.ErrorInfo.CODE_GETVERIFYCODE_FAILED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.GetVerifyCode.ErrorInfo.MSG_GETVERIFYCODE_FAILED);
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
