package com.jacklin.location;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.jacklin.constant.Constants;
import com.jacklin.db.Friends;
import com.jacklin.db.HibernateUtils;
import com.jacklin.db.User;
import com.jacklin.db.UserLocation;

public class LocationUtils {
	static Logger logger = Logger.getLogger(LocationUtils.class);

	/**
	 * 返回1公里以内的陌生人
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject GetNearby(double latitude, double longitude,
			String date, String user_phone) {
		JSONObject respJson = new JSONObject();

		Friends friends = (Friends) HibernateUtils.get(Friends.class,
				user_phone);
		List<UserLocation> locations = null;
		try {
			locations = HibernateUtils
					.getSession()
					.createCriteria(UserLocation.class)
					.add(Restrictions.between("latitude", latitude - 1,
							latitude + 1))
					.add(Restrictions.between("longitude", longitude - 1,
							longitude + 1)).list();

			for (int i = 0; i < locations.size(); i++) {
				UserLocation location = locations.get(i);
				double distance = computeDistance(latitude, longitude,
						location.getLatitude(), location.getLongitude());
				long spanTime = timeCompare(location.getDate(), date);
				if (distance > 1000) {
					locations.remove(i);
				} else if (spanTime > 2 * 60 * 1000) {
					locations.remove(i);
				} else if (location.getUser_phone().equals(user_phone)) {
					locations.remove(i);
				} else if (friends != null) {
					List<String> friendList = friends.getFriendList();
					if (friendList.contains(location.getUser_phone())) {
						locations.remove(i);
					}
				}
			}

			JSONObject strangersJson = new JSONObject();
			int index = 0;
			User stranger;
			for (int i = 0; i < locations.size(); i++) {
				JSONArray strangerInfo = new JSONArray();
				stranger = (User) HibernateUtils.get(User.class,
						locations.get(i).getUser_phone());
				if (stranger == null) {
					continue;
				}
				strangerInfo.add(0, stranger.getUser_phone());
				strangerInfo.add(1, stranger.getName());
				strangerInfo.add(2, stranger.getGender());
				strangerInfo.add(3, stranger.getPhoto());
				strangerInfo.add(4, stranger.getGame());
				strangerInfo.add(5, stranger.getTip());
				strangerInfo.add(6, stranger.getAnswer());
				strangerInfo.add(7, stranger.getBirthday());
				strangerInfo.add(8, stranger.getBook());
				strangerInfo.add(9, stranger.getFilm());
				strangerInfo.add(10, stranger.getCompany_school());
				strangerInfo.add(11, stranger.getMood());
				strangerInfo.add(12, locations.get(i).getLatitude());
				strangerInfo.add(13, locations.get(i).getLongitude());

				strangersJson.element(index + "", strangerInfo);
				index++;
			}
			respJson.element(Constants.ResponseParams.RES_CODE,
					Constants.ResponseInfo.CODE_SUCCESS);
			respJson.element(Constants.ResponseParams.RES_MSG,
					Constants.ResponseInfo.MSG_SUCCESS);
			respJson.element(Constants.GetLocation.ResponeParams.NUM, index);
			respJson.element(Constants.GetLocation.ResponeParams.STRANGERS,
					strangersJson.toString());
			return respJson;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HibernateUtils.getSession() != null)
				HibernateUtils.getSession().close();
		}

		respJson.element(Constants.ResponseParams.RES_CODE,
				Constants.GetLocation.ErrorInfo.CODE_GETLOCATION_FAILED);
		respJson.element(Constants.ResponseParams.RES_MSG,
				Constants.GetLocation.ErrorInfo.MSG_GETLOCATION_FAILED);

		return respJson;
	}

	/**
	 * 计算两个位置的距离
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public static double computeDistance(double lat1, double lon1, double lat2,
			double lon2) {
		double PI = 3.14159265358979323;
		double R = 6371229;
		double x, y, distance;
		x = (lon2 - lon1) * PI * R * Math.cos(((lat1 + lat2) / 2) * PI / 180)
				/ 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);
		return distance;
	}

	public static long timeCompare(String t1, String t2) {
		long date1 = Long.parseLong(t1);
		long date2 = Long.parseLong(t2);
		long spanTime = date2 - date1;
		return spanTime;
	}
}
