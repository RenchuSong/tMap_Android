package com.tmap.model.tool;

import com.tmap.library.SystemConfig;
import com.tmap.library.wifi.WifiScanner;
import com.tmap.model.tool.arch.ServerConnector;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WifiCollector extends Activity {
	public Button upX, upY, upZ, downX, downY, downZ, scan;
	public EditText x, y, z, eachTime;
	public TextView hint;
	public int remaining;
	
	public WifiScanner wifiScanner;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_conllector);
        
        upX = (Button)this.findViewById(R.id.up_x);
        upY = (Button)this.findViewById(R.id.up_y);
        upZ = (Button)this.findViewById(R.id.up_z);
        downX = (Button)this.findViewById(R.id.down_x);
        downY = (Button)this.findViewById(R.id.down_y);
        downZ = (Button)this.findViewById(R.id.down_z);
        scan = (Button)this.findViewById(R.id.scan);
        
        x = (EditText)this.findViewById(R.id.x);
        y = (EditText)this.findViewById(R.id.y);
        z = (EditText)this.findViewById(R.id.z);
        
        hint = (TextView)this.findViewById(R.id.hint);
        
        eachTime = (EditText)this.findViewById(R.id.each_time);
        
        upX.setOnClickListener(
        	new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					double dx = Double.parseDouble(x.getText().toString()) + Double.parseDouble(((EditText)findViewById(R.id.dx)).getText().toString());
					x.setText((double) (Math.round(dx*100)/100.0) + "");
				}
        	}
        );
        
        upY.setOnClickListener(
           	new OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				double dy = Double.parseDouble(y.getText().toString()) + Double.parseDouble(((EditText)findViewById(R.id.dy)).getText().toString());
					y.setText((double) (Math.round(dy*100)/100.0) + "");
    			}
           	}
        );
        
        upZ.setOnClickListener(
            new OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
   					// TODO Auto-generated method stub
    				double dz = Double.parseDouble(z.getText().toString()) + 0.2;
    				z.setText((double) (Math.round(dz*100)/100.0) + "");
    			}
           	}
        );
        
        downX.setOnClickListener(
            new OnClickListener() {
            	@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
            		double dx = Double.parseDouble(x.getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.dx)).getText().toString());
					x.setText((double) (Math.round(dx*100)/100.0) + "");
   				}
           	}
        );
            
        downY.setOnClickListener(
            new OnClickListener() {
        		@Override
        		public void onClick(View arg0) {
        			// TODO Auto-generated method stub
        			double dy = Double.parseDouble(y.getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.dy)).getText().toString());
					y.setText((double) (Math.round(dy*100)/100.0) + "");
       			}
          	}
        );
            
        downZ.setOnClickListener(
            new OnClickListener() {
        		@Override
       			public void onClick(View arg0) {
   					// TODO Auto-generated method stub
        			double dz = Double.parseDouble(z.getText().toString()) - 0.2;
    				z.setText((double) (Math.round(dz*100)/100.0) + "");
      			}
           	}
        );
        
        wifiScanner = new WifiScanner(this, handler);

        scan.setOnClickListener(
            new OnClickListener() {
           		@Override
       			public void onClick(View arg0) {
   					// TODO Auto-generated method stub
           			scan.setEnabled(false);
           			upX.setEnabled(false);
            		upY.setEnabled(false);
            		upZ.setEnabled(false);
            		downX.setEnabled(false);
            		downY.setEnabled(false);
            		downZ.setEnabled(false);
           			remaining = Integer.parseInt(((EditText)findViewById(R.id.each_time)).getText().toString());
       				hint.setText("剩余" + remaining + "次");
       				new Thread(new Runnable() {
       					public void run() {
       						wifiScanner.scan();
       					}
       				}).start();
       				
      			}
           	}
        );
    }
    
    Handler handler = new Handler() {
    	public void handleMessage(android.os.Message msg) {
            switch(msg.what){
            case SystemConfig.MSG_WIFI_COMPLETE:
            	final int roomId = Integer.parseInt(((EditText)findViewById(R.id.roomId)).getText().toString());
            	final float x = Float.parseFloat(((EditText)findViewById(R.id.x)).getText().toString());
            	final float y = Float.parseFloat(((EditText)findViewById(R.id.y)).getText().toString());
            	final float z = Float.parseFloat(((EditText)findViewById(R.id.z)).getText().toString());
            	
            	new Thread(new Runnable() {
        			public void run() {
        				try {
							Log.d("src", ServerConnector.getInstance().uploadWifi(roomId, x, y, z, wifiScanner.scanResult));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							handler.sendEmptyMessage(SystemConfig.MSG_NET_FAIL);
							e.printStackTrace();
						}
        			}
        		}).start();
            	
            	if (remaining > 1) {
            		--remaining;
            		hint.setText("剩余" + remaining + "次");
            		new Thread(new Runnable() {
       					public void run() {
       						wifiScanner.scan();
       					}
       				}).start();	
            	} else {
            		// to do
            		hint.setText("就绪");
            		scan.setEnabled(true);
            		upX.setEnabled(true);
            		upY.setEnabled(true);
            		upZ.setEnabled(true);
            		downX.setEnabled(true);
            		downY.setEnabled(true);
            		downZ.setEnabled(true);

            		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            		r.play(); 
            	}
            	break;
            case SystemConfig.MSG_NET_FAIL:
            	Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            	break;
            }
            
        };
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
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
