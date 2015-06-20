package com.jacklin.main.yaoyiyao;




import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.jacklin.constant.Constants;
import com.jacklin.constant.HandlerID;
import com.jacklin.db.CacheUtils;
import com.jacklin.encrypt.EncryptUtils;
import com.jacklin.http.HttpCallBackListener;
import com.jacklin.http.HttpUtils;
import com.jacklin.jacklinclient.R;
import com.jacklin.utils.ViewUtils;

@SuppressLint({ "ShowToast", "HandlerLeak" })
public class MapActivity extends Activity implements HttpCallBackListener{
	
	
	// 定位相关
	LocationClient mLocClient;
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	BaiduMap mBaiduMap;
	private Context mContext;
	private String user_phone;
	private double latitude;
	private double longitude;
	// UI相关
	boolean isFirstLoc = true;// 是否首次定位
	
	ImageView iv_back;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jacklin_map);

		initMap();
		initView();

	}

	public void initView() {
		mContext = this;
		user_phone = CacheUtils.GetUserPhone();
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MapActivity.this.finish();
			}
		});
	}
	
	

	/**************************** 地图 *****************************/
	public void initMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatus mMapStatus = new MapStatus.Builder().zoom(14).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus); // 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();

		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型

		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			latitude = location.getLatitude();
			longitude = location.getLongitude();

			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

			}
			getLocation(user_phone, latitude, longitude);
			mLocClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	
	
	

	/**************************** 获取附近一公里人的动作 *****************************/
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerID.SHOW_PRO_DIALOG:
				ViewUtils.ShowProgressDialog(mContext, (String) msg.obj);
				break;
			case HandlerID.HIDE_PRO_DIALOG:
				ViewUtils.HideProgressDialog();
				BitmapDescriptor bdB = BitmapDescriptorFactory
						.fromResource(R.drawable.male);
				BitmapDescriptor bdA = BitmapDescriptorFactory
						.fromResource(R.drawable.female);
				for(int i=0;i<CacheUtils.GetLocationList().size();i++){
					if(CacheUtils.GetLocationList().get(i).getGender().equals("男")){
						
						OverlayOptions ooB = new MarkerOptions().position(new LatLng(CacheUtils.GetLocationList().get(i).getLatitude(), CacheUtils.GetLocationList().get(i).getLongitude())).icon(bdB)
								.zIndex(9).draggable(true);
						mBaiduMap.addOverlay(ooB);
					}else{
						OverlayOptions ooA = new MarkerOptions().position(new LatLng(CacheUtils.GetLocationList().get(i).getLatitude(), CacheUtils.GetLocationList().get(i).getLongitude())).icon(bdA)
								.zIndex(9).draggable(true);
						mBaiduMap.addOverlay(ooA);
					}
				}
				mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
					public boolean onMarkerClick(final Marker marker) {
						for(int i=0;i<CacheUtils.GetLocationList().size();i++){
							if(marker.getPosition().latitude==CacheUtils.GetLocationList().get(i).getLatitude()&&marker.getPosition().longitude==CacheUtils.GetLocationList().get(i).getLongitude()){
								
								Intent intent=new Intent(MapActivity.this,MapGameActivity.class);
								intent.putExtra(Constants.Flags.STRANGER, CacheUtils.GetLocationList().get(i));
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);
								startActivity(intent);
								
								
								/*Drawable game=PhotoUtils.StringToDrawable(CacheUtils.GetLocationList().get(i).getGame());
								String tip=CacheUtils.GetLocationList().get(i).getTip();
								String answer=CacheUtils.GetLocationList().get(i).getAnswer();
								
								
								
								//弹出框
								LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
								View layout = inflater.inflate(R.layout.jacklin_location_game, null); 
								PopupWindow menuWindow = new PopupWindow(layout,(int) getResources().getDimension(R.dimen.kaka_250_dip),(int) getResources().getDimension(R.dimen.kaka_350_dip)); 
								
			
								menuWindow.setFocusable(true); 
								menuWindow.setOutsideTouchable(true); 
								menuWindow.setBackgroundDrawable(new BitmapDrawable());
								
								DisplayMetrics dm = new DisplayMetrics();
								getWindowManager().getDefaultDisplay().getMetrics(dm);
								int screenWidth = dm.widthPixels;
								
								menuWindow.showAsDropDown(findViewById(R.id.titleLayout),screenWidth/2-menuWindow.getWidth()/2, 4);
								
								
								ImageView iv=(ImageView) layout.findViewById(R.id.iv_location);
								TextView tv_tip=(TextView) layout.findViewById(R.id.tv_location_tip);
								final TextView tv_answer=(TextView) layout.findViewById(R.id.tv_location_answer);
								RelativeLayout rl=(RelativeLayout) layout.findViewById(R.id.rl_location);
								rl.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										final EditText input = new EditText(MapActivity.this);
										new AlertDialog.Builder(MapActivity.this)
												.setTitle("填写答案")
												.setView(input)
												.setPositiveButton("确定",
														new DialogInterface.OnClickListener() {

															public void onClick(DialogInterface dialog,
																	int whichButton) {
																if (!TextUtils.isEmpty(input.getText()))
																	tv_answer.setText(input.getText());
															}
														})
												.setNegativeButton("取消",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(DialogInterface dialog,
																	int which) {
															}
														}).show();
									}
								});*/
							}
						}
						return true;
					}});
				break;
			case HandlerID.CONNECT_TIMEOUT_DIALOG:
				ViewUtils.ShowConnectTimeoutDialog(mContext);
				break;
			case HandlerID.SHOW_ERROR_DIALOG:
				ViewUtils.ShowErrorDialog(mContext, (String) msg.obj);
				break;
			}
		};
	};

	public void getLocation(final String user_phone, final double latitude,
			final double longitude) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = HandlerID.SHOW_PRO_DIALOG;
				msg.obj = "正在搜索";
				mHandler.sendMessage(msg);

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put(
							Constants.GetLocation.RequestParams.USER_PHONE,
							user_phone);
					jsonObject.put(
							Constants.GetLocation.RequestParams.LATITUDE,
							latitude);
					jsonObject.put(
							Constants.GetLocation.RequestParams.LONGITUDE,
							longitude);
					final String data = EncryptUtils.GetRsaEncrypt(jsonObject
							.toString());

					HttpUtils.sendReqData(Constants.ID.GETLOCATION, data,
							MapActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
					mHandler.sendEmptyMessage(HandlerID.CONNECT_TIMEOUT_DIALOG);
				}
			}
		}).start();
	}
	

	
	
	
	@Override
	public void httpCallBack(int id, JSONObject resp) {
		if (id == Constants.ID.GETLOCATION) {
			String resCode = resp.optString(Constants.ResponseParams.RES_CODE);

			if ("100".equals(resCode)) {
				
				// 缓存陌生人的信息
				CacheUtils.UpdateLocationList(resp);
				

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
			} else {

				String resMsg = resp
						.optString(Constants.ResponseParams.RES_MSG);

				mHandler.sendEmptyMessage(HandlerID.HIDE_PRO_DIALOG);
				if (resp != null) {
					Message msg = new Message();
					msg.what = HandlerID.SHOW_ERROR_DIALOG;
					msg.obj = resMsg;
					mHandler.sendMessage(msg);
				}
			}
		}

	}
}
