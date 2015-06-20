package com.jacklin.location;

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
import com.jacklin.http.HttpJson;
import com.jacklin.http.HttpUtils;

public class GetLocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger=Logger.getLogger(GetLocationServlet.class);

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

		//获取用户位置
		String user_phone=reqJson.optString(Constants.GetLocation.RequestParams.USER_PHONE);
		String latitude=reqJson.optString(Constants.GetLocation.RequestParams.LATITUDE);
		String longitude=reqJson.optString(Constants.GetLocation.RequestParams.LONGITUDE);
		
		//参数缺失(可以不要)
		if(user_phone.isEmpty()||latitude.isEmpty()||longitude.isEmpty()){
			logger.info("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}
		
		//算了= =不再去判断用户是否存在了= =肯定存在的！！
		//将用户位置存储到SeesionContext中，供其他用户查询
		ServletContext sc = req.getSession().getServletContext();
		
		
		JSONObject location=new JSONObject();
		location.element(Constants.GetLocation.USER_PHONE, user_phone);
		location.element(Constants.GetLocation.LATITUDE, latitude);
		location.element(Constants.GetLocation.LONGITUDE, longitude);
		sc.setAttribute(Constants.GetLocation.USER_PHONE,location);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
