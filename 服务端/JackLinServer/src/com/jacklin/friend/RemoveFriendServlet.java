package com.jacklin.friend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jacklin.constant.Constants;
import com.jacklin.http.HttpJson;
import com.jacklin.http.HttpUtils;
/**
 * 移除好友
 * @author john
 *
 */
public class RemoveFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger=Logger.getLogger(RemoveFriendServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//设置字符编码集
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out=resp.getWriter();
		
		//解析参数
		HttpJson hdb=HttpUtils.ResolveParams(req);
		if(!hdb.isSuccess()){
			out.write(hdb.getRespJson().toString());
			return;
		}
		
		//获取JSON数据
		JSONObject reqJson=hdb.getReqJson();
		JSONObject respJson=new JSONObject();
		
		//获取用户和要移除的好友ID信息
		String user_phone=reqJson.optString(Constants.RemoveFriend.RequestParams.USER_PHONE);
		String friend_phone=reqJson.optString(Constants.RemoveFriend.RequestParams.FRIEND_PHONE);
		
		//参数缺失
		if(user_phone==null||friend_phone==null){
			logger.info("参数缺失");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_PARAM_MISSED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_PARAM_MISSED);
			out.write(respJson.toString());
			return;
		}
		
		//移除好友
		boolean bRemoveFriend=FriendsUtils.RemoveFriend(user_phone, friend_phone);
		
		if (!bRemoveFriend) {
			logger.info("移除好友失败");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.RemoveFriend.ErrorInfo.CODE_REMOVEFRIEND_FAILED);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.RemoveFriend.ErrorInfo.MSG_REMOVEFRIEND_FAILED);
			out.write(respJson.toString());
			return;
		} else {
			logger.info("移除好友成功");
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_SUCCESS);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_SUCCESS);
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
