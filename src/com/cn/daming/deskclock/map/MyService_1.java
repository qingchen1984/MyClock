package com.cn.daming.deskclock.map;


import com.cn.daming.deskclock.R;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService_1 extends Service {
	private static final double EARTH_RADIUS = 6378137.0;
	private MediaPlayer mediaPlayer=null;
	private CharSequence[] data=null;
	private LocationManager locationManager=null;
	private double now_Latitude,now_Longitude;
	private double input_Latitude,input_Longitude;
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	public void onCreate() {
		super.onCreate();
		Log.e("创建服务", "");
		mediaPlayer=MediaPlayer.create(MyService_1.this, R.raw.bzj);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		locationManager=(LocationManager)MyService_1.this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, new LocationTest());
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("启动服务", "");
		data=intent.getCharSequenceArrayExtra("xx");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.stop();
	}
	
	
	private final class LocationTest implements LocationListener {

		public void onLocationChanged(Location arg0) {
			System.out.println("经度" + arg0.getLatitude());
			System.out.println("纬度" + arg0.getLongitude());
			now_Latitude=arg0.getLatitude();
			now_Longitude=arg0.getLongitude();
			

		}

		public void onProviderDisabled(String arg0) {

		}

		public void onProviderEnabled(String arg0) {

		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

	}
	
	private static double rad(double d) {  
        return d * Math.PI / 180.0;  
    }  
  
        /** 
     * 根据两点间经纬度坐标（double值），计算两点间距离， 
     *  
     * @param lat1 
     * @param lng1 
     * @param lat2 
     * @param lng2 
     * @return 距离：单位为米 
     */  
	
    public static double DistanceOfTwoPoints(double lat1,double lng1,   
             double lat2,double lng2) {  
        double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
        double a = radLat1 - radLat2;  
        double b = rad(lng1) - rad(lng2);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
                + Math.cos(radLat1) * Math.cos(radLat2)  
                * Math.pow(Math.sin(b / 2), 2)));  
        s = s * EARTH_RADIUS;  
        s = Math.round(s * 10000) / 10000;  
        return s;  
    } 
    //判断执行响铃
    private void doAction(CharSequence[] c) {
		if(c[0]==null){
			input_Latitude=0;
		}else {
			input_Latitude=Double.parseDouble(c[0].toString());
		}
		if(c[1]==null){
			input_Longitude=0;
		}else{
			input_Longitude=Double.parseDouble(c[1].toString());
		}

		double s = DistanceOfTwoPoints(input_Latitude,input_Longitude,now_Latitude,now_Longitude);
		if(s<1000000000){
			Log.e("执行了", s+"");
//			ringbell(1);
		} else {
			Log.e("没有执行", s+"");
		}
	}
}
 


