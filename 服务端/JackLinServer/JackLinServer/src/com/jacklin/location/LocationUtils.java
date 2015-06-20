package com.jacklin.location;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jacklin.db.HibernateUtils;

public class LocationUtils {
	static Logger logger=Logger.getLogger(LocationUtils.class);
	/**
	 * 返回1公里以内的陌生人
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	/*public static JSONObject GetNearby(double latitude,double longitude){
		JSONObject respJson=new JSONObject();
		
		Session s = null;
		try {
			s = HibernateUtils.getSession();
			Criteria criteria=s.createCriteria(Location.class);
			criteria.add(Restrictions.gt("latitude", latitude-1));
			criteria.add(Restrictions.lt("latitude", latitude+1));
			criteria.add(Restrictions.gt("longitude", longitude-1));
			criteria.add(Restrictions.lt("longitude", longitude+1));
			List locations=criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		
		return respJson;
	}*/
	/**
	 * 计算两个位置的距离
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public static double computeDistance(double lat1,double lon1,double lat2,double lon2){
		double PI=3.14159265358979323;
		double R=6371229;
		double x,y,distance;
		x=(lon2-lon1)*PI*R*Math.cos(((lat1+lat2)/2)*PI/180)/180;
		y=(lat2-lat1)*PI*R/180;
		distance=Math.hypot(x, y);
		return distance;
	}
}
