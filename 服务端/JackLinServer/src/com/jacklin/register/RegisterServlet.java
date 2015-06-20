package com.jacklin.register;

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
 * 新用户注册
 * 需要考虑的问题：1、参数缺失；2、用户存在；3、注册失败（写入数据库失败）；4、注册成功。
 * @author john
 * 
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(RegisterServlet.class);
	
	

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

		// 获取注册信息数据
		String photo=req.getParameter(Constants.Register.RequestParams.PHOTO);
		String game = req.getParameter(Constants.Register.RequestParams.GAME);
		
		
		String user_phone = reqJson.optString(Constants.Register.RequestParams.USER_PHONE);
		String name = reqJson.optString(Constants.Register.RequestParams.NAME);
		String gender = reqJson.optString(Constants.Register.RequestParams.GENDER);
		String password = reqJson.optString(Constants.Register.RequestParams.PASSWORD);
		String tip = reqJson.optString(Constants.Register.RequestParams.TIP);
		String answer=reqJson.optString(Constants.Register.RequestParams.ANSWER);
		
		// 参数缺失
		if (user_phone.isEmpty() || name.isEmpty() || gender.isEmpty() || password.isEmpty()) {
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

		if (user != null) {
			logger.info("手机号：" + user_phone + "已经注册");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.Register.ErrorInfo.CODE_ID_EXISTED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.Register.ErrorInfo.MSG_ID_EXISTED);
			out.write(respJson.toString());
			return;
		}
		
		// 用户不存在
		User newUser = new User();
		newUser.setUser_phone(user_phone);
		newUser.setPassword(password);
		newUser.setPhoto(photo);
		newUser.setName(name);
		newUser.setGender(gender);
		newUser.setGame(game);
		newUser.setTip(tip);
		newUser.setAnswer(answer);

		// 注册是否成功
		boolean bRegister = HibernateUtils.add(newUser);

		if (bRegister) {
			logger.info("手机号:" + user_phone + "注册成功");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_SUCCESS);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_SUCCESS);
			out.write(respJson.toString());
			return;
		} else {
			logger.info("手机号:" + user_phone + "注册失败");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.Register.ErrorInfo.CODE_REGISTER_FAILED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.Register.ErrorInfo.MSG_REGISTER_FAILED);
			out.write(respJson.toString());
			return;
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
