package com.tmap.model.tool;

import com.tmap.library.SystemConfig;
import com.tmap.library.server_interface.Location;
import com.tmap.library.wifi.WifiScanner;
import com.tmap.model.tool.arch.ServerConnector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Locating  extends Activity {
	private Button start_stop, person;
	private EditText xRange, yRange;
	private TextView building, room, location;
	
	private double xLen = 5, yLen = 8;
	private int width = 180, height = 260;
	
	public WifiScanner wifiScanner;
	
	private boolean isLocating = false;
	private Location locating = null;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locating);
        
        start_stop = (Button)this.findViewById(R.id.start_stop);
        person = (Button)this.findViewById(R.id.person);
        
        xRange = (EditText)this.findViewById(R.id.xRange);
        yRange = (EditText)this.findViewById(R.id.yRange);
        
        building = (TextView)this.findViewById(R.id.building);
        room = (TextView)this.findViewById(R.id.room);
        location = (TextView)this.findViewById(R.id.location);
        
        wifiScanner = new WifiScanner(this, handler);
        
        start_stop.setOnClickListener(
        	new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					isLocating = !isLocating;
					if (isLocating) {
						xRange.setEnabled(false);
						yRange.setEnabled(false);
						start_stop.setText("Stop");
					} else {
						xRange.setEnabled(true);
						yRange.setEnabled(true);
						start_stop.setText("Start");
					}
					xLen = Double.parseDouble(xRange.getText().toString());
					yLen = Double.parseDouble(yRange.getText().toString());
					width = (int) (1000 * xLen);
					height = (int) (1000 * yLen);
					
					if (height > 300) {
						width = width * 300 / height;
						height = 300;
					}
					
					if (width > 200) {
						height = height * 200 / width;
						width = 200;
					}
					Log.v("src", width +" " + height);
//					RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(width,height);
//					timeParams.leftMargin=10;	     
//					timeParams.topMargin=10;
					((RelativeLayout)findViewById(R.id.mock_room)).getLayoutParams().height = height;
					((RelativeLayout)findViewById(R.id.mock_room)).getLayoutParams().width = width;
					
					//.setLayoutParams(new RelativeLayout.LayoutParams(timeParams));
					
					//.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
					new Thread(new Runnable() {
       					public void run() {
       						wifiScanner.scan();
       					}
       				}).start();	
				}
        	}
        );
        
	}
	
	private int getPixels(int dipValue) {
		Resources r = getResources();
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
	}
	
	Handler handler = new Handler() {
    	public void handleMessage(android.os.Message msg) {
            switch(msg.what){
            case SystemConfig.MSG_WIFI_COMPLETE:
            	new Thread(new Runnable() {
        			public void run() {
        				try {
        					locating = ServerConnector.getInstance().wifiLocating(wifiScanner.scanResult);
							handler.sendEmptyMessage(SystemConfig.MSG_NET_COMPLETE);
							Log.d("src", locating.x+" "+locating.y+" "+locating.z);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							handler.sendEmptyMessage(SystemConfig.MSG_NET_FAIL);
							e.printStackTrace();
						}
        			}
        		}).start();

            	break;
            case SystemConfig.MSG_NET_FAIL:
            	Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
            	
            	if (isLocating) {
            		new Thread(new Runnable() {
       					public void run() {
       						wifiScanner.scan();
       					}
       				}).start();	
            	}
            	break;
            case SystemConfig.MSG_NET_COMPLETE:
            	building.setText("Building: " + locating.buildingId);
            	room.setText("Room: " + locating.roomId);
            	location.setText("X: " + locating.x + "   Y: " + locating.y + "    Z: " + locating.z);
            	person.setX(getPixels((int)(locating.x * width / xLen)));
                person.setY(getPixels(height - (int)(locating.y * height / yLen)));
                
            	if (isLocating) {
            		new Thread(new Runnable() {
       					public void run() {
       						wifiScanner.scan();
       					}
       				}).start();	
            	}
            	break;
            }
            	
        };
    };
	
	@Override
    public void onPause() {
    	wifiScanner.onPause();
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	wifiScanner.onResume();
    	super.onResume();
    }
}
