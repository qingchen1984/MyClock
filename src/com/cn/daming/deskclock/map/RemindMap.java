package com.cn.daming.deskclock.map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.cn.daming.deskclock.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RemindMap extends Activity {

	private EditText shurujingdu, shuruweidu;
	private int spi_lin=0;
	private CharSequence data[]=new CharSequence[3];
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static final double EARTH_RADIUS = 6378137.0;
	private static final String[] m={"选择距离","100米","500米","1000米","5000米"};
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
	MapView mMapView = null; 
	BaiduMap mBaiduMap;
	LocationClient mLocClient;
	Location location;
	Button btn_start;
	Button btn_stop;
	TextView jingdu,weidu;	
    @Override  
    
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_map);  
        Toast.makeText(this, "通过经纬度查找地点", 0).show();
        
        mMapView = (MapView) findViewById(R.id.bmapView);
        
        spinner = (Spinner) findViewById(R.id.Spinner01);
        btn_start = (Button) findViewById(R.id.btn_sta);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        
        jingdu = (TextView) findViewById(R.id.dangqianjingdu);
        weidu = (TextView) findViewById(R.id.dangqianweidu);
        
        shurujingdu = (EditText) findViewById(R.id.shurujingdu);
        shuruweidu = (EditText) findViewById(R.id.shuruweidu);
        
        mBaiduMap = mMapView.getMap(); 

        //显示坐标
        showlocation();
       /*
        * 创建下拉列表用的 
        */
        spinner = (Spinner) findViewById(R.id.Spinner01);
      //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
         
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
         
        //添加事件Spinner事件监听  
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
         
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        
        //设置按钮监听
        
        btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				data[0]=shurujingdu.getText().toString();
		        data[1]=shuruweidu.getText().toString();
		        data[2]=spi_lin+"";
		        Intent intentStart=new Intent(RemindMap.this,MyService.class);
		        Log.e("", "开始");
		        intentStart.putExtra("xx", data);
		        startService(intentStart);
				
			}
		});
        
        btn_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("", "关闭");
				Intent intentStop=new Intent(RemindMap.this,MyService.class);
		        stopService(intentStop);
			}
		});
    }  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        mMapView.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        mMapView.onPause();  
        }  
   
    private  class LocationTest implements LocationListener {

		public void onLocationChanged(Location arg0) {
			Log.e("经度",arg0.getLatitude()+"");
			Log.e("经度",arg0.getLongitude()+"");
			jingdu.setText("" + arg0.getLatitude());
			weidu.setText("" + arg0.getLongitude());

			double s = DistanceOfTwoPoints(1,1,1,1);
			if(s<100000000){
				Log.e("", "唱歌");
			}
		}

		public void onProviderDisabled(String arg0) {

		}

		public void onProviderEnabled(String arg0) {

		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

	}
    
    



	private void showlocation() {
		Log.e("", "show");
		LocationManager locationManager = (LocationManager) RemindMap.this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,10, new LocationTest());

	}



	//使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	spi_lin=arg2;
        	Log.e("栀子花开", spi_lin+"");
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
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
}
