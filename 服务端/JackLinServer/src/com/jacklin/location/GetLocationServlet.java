package com.jacklin.location;

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
import com.jacklin.db.UserLocation;
import com.jacklin.http.HttpJson;
import com.jacklin.http.HttpUtils;

public class GetLocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(GetLocationServlet.class);

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

		// 获取用户位置
		String user_phone = reqJson
				.optString(Constants.GetLocation.RequestParams.USER_PHONE);
		String latitudeStr = reqJson
				.optString(Constants.GetLocation.RequestParams.LATITUDE);
		String longitudeStr = reqJson
				.optString(Constants.GetLocation.RequestParams.LONGITUDE);

		// 参数缺失(可以不要)
		if (user_phone.isEmpty() || latitudeStr.isEmpty()
				|| longitudeStr.isEmpty()) {
			logger.info("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}

		double latitude = Double.valueOf(latitudeStr);
		double longitude = Double.valueOf(longitudeStr);
		// 获取当前系统时间
		long currentTime=System.currentTimeMillis();
		String date=Long.toString(currentTime);

		UserLocation location = (UserLocation) HibernateUtils.get(
				UserLocation.class, user_phone);

		if (location != null) {
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			location.setDate(date);
			boolean bUpdateLocationInfo = HibernateUtils.update(location);
			if (bUpdateLocationInfo) {
				logger.info("手机号：" + user_phone + "更新地址成功");
			} else {
				logger.info("手机号：" + user_phone + "更新地址失败");
				respJson.element(
						Constants.ResponseParams.RES_CODE,
						Constants.GetLocation.ErrorInfo.CODE_GETLOCATION_UPDATEFAILED);
				respJson.element(
						Constants.ResponseParams.RES_MSG,
						Constants.GetLocation.ErrorInfo.MSG_GETLOCATION_UPDATEFAILED);
				out.write(respJson.toString());
				return;
			}
		} else {
			UserLocation newLocation = new UserLocation();
			newLocation.setUser_phone(user_phone);
			newLocation.setLatitude(latitude);
			newLocation.setLongitude(longitude);
			newLocation.setDate(date);
			boolean bAddLocation = HibernateUtils.add(newLocation);
			if (bAddLocation) {
			} else {
				logger.info("手机号：" + user_phone + "添加地址失败");
				respJson.element(
						Constants.ResponseParams.RES_CODE,
						Constants.GetLocation.ErrorInfo.CODE_GETLOCATION_ADDFAILED);
				respJson.element(
						Constants.ResponseParams.RES_MSG,
						Constants.GetLocation.ErrorInfo.MSG_GETLOCATION_ADDFAILED);
				out.write(respJson.toString());
				return;
			}
		}
		
		// 查询一公里以内的用户
		respJson=LocationUtils.GetNearby(latitude, longitude, date, user_phone);
		out.write(respJson.toString());
		return;

		// 算了= =不再去判断用户是否存在了= =肯定存在的！！
		// 将用户位置存储到SeesionContext中，供其他用户查询
		/*
		 * ServletContext sc = req.getSession().getServletContext();
		 * 
		 * 
		 * JSONObject location=new JSONObject();
		 * location.element(Constants.GetLocation.USER_PHONE, user_phone);
		 * location.element(Constants.GetLocation.LATITUDE, latitude);
		 * location.element(Constants.GetLocation.LONGITUDE, longitude);
		 * location.element(Constants.GetLocation.DATE, dateString);
		 * sc.setAttribute(Constants.GetLocation.USER_PHONE,location);
		 */

	}

	/*public static double getDistance(double lat1, double longt1, double lat2,
			double longt2) {
		double PI = 3.14159265358979323; // 圆周率
		double R = 6371229; // 地球的半径

		double x, y, distance;
		x = (longt2 - longt1) * PI * R
				* Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);

		return distance;
	}

	public static long timeCompare(String t1, String t2) {
		long date1=Long.parseLong(t1);
		long date2=Long.parseLong(t2);
		long spanTime = date2-date1;
		return spanTime;
	}*/

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
