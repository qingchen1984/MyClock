package com.cn.daming.deskclock.map;

import java.io.IOException;

import com.baidu.mapapi.utils.DistanceUtil;
import com.cn.daming.deskclock.R;

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

public class MyService extends Service {
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private LocationManager locationManager;
	private double now_Latitude;
	private double now_Longitude;
	private CharSequence[] data=null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mediaPlayer = MediaPlayer.create(this, R.raw.bzj);
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		data=intent.getCharSequenceArrayExtra("xx");
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, new LocationTest());
		
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mediaPlayer.stop();
	}
	
	private final class LocationTest implements LocationListener {

		

		public void onLocationChanged(Location arg0) {
			System.out.println("经度" + arg0.getLatitude());
			System.out.println("纬度" + arg0.getLongitude());
			now_Latitude=arg0.getLatitude();
			now_Longitude=arg0.getLongitude();
			
			float[] results=new float[1];
			Location.distanceBetween(now_Latitude, now_Longitude, Double.parseDouble(data[0].toString()), Double.parseDouble(data[1].toString()), results);
			String diatance = data[2].toString();
			diatance = diatance.substring(0,diatance.length()-1);
			if(results[0]<Double.parseDouble(diatance)){
				mediaPlayer.start();
			}
			

		}

		public void onProviderDisabled(String arg0) {

		}

		public void onProviderEnabled(String arg0) {

		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

	}
}
