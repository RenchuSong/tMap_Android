package com.tmap.library.wifi;

import java.util.ArrayList;
import java.util.List;

import com.tmap.library.SystemConfig;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class WifiScanner {
	public ArrayList<BssidRssiPair> scanResult = null;	// BSSID-RSSI pair list
	private WifiReceiver wifiReceiver = null;
	private WifiManager wifiManager = null;
	private Activity activity;
	private Handler actHandler;
	
	public class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context c, Intent i){
			List<ScanResult> scanResults = wifiManager.getScanResults();
			for (ScanResult scanItem : scanResults) {
				scanResult.add(new BssidRssiPair(scanItem.BSSID, scanItem.level));	
			}
			actHandler.sendEmptyMessage(SystemConfig.MSG_WIFI_COMPLETE);
		}
	}
	
	public WifiScanner(Activity activity, Handler handler) {
		wifiReceiver = new WifiReceiver();
		this.activity = activity;
		this.actHandler = handler;
		wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
		activity.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}
	
	public void onPause() {
		activity.unregisterReceiver(wifiReceiver);
	}
	
	public void onResume() {
		activity.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));	
	}
	
	public void scan() {
		scanResult = new ArrayList<BssidRssiPair>();
		wifiManager.startScan();
	}
}
