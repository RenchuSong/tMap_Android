package com.tmap.model.tool.arch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.tmap.library.SystemConfig;
import com.tmap.library.datatransfer.HttpUtils;
import com.tmap.library.datatransfer.JsonUtils;
import com.tmap.library.server_interface.Location;
import com.tmap.library.wifi.BssidRssiPair;

// Communicate with Server, encapsulated functions to link specific URLs
public class ServerConnector {
	private static ServerConnector instance = new ServerConnector();
	private ServerConnector() {}
	public static ServerConnector getInstance() {
		return instance;
	}
	
	// Upload a wifi sample data
	public String uploadWifi(int roomId, float x, float y, float z, ArrayList<BssidRssiPair> wifiData) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String wifiPack = JsonUtils.getInstance().packListToJson(wifiData);
		params.add(new BasicNameValuePair("wifiData", wifiPack));
		String url = "tMap/wifi/uploadWifi/" + roomId + "/" + x + "/" + y + "/" + z;
		return HttpUtils.getInstance().postData(AppConfig.SERVER_ADDR + url, params);
	}
	
	// Get Wi-Fi locating result
	public Location wifiLocating(ArrayList<BssidRssiPair> wifiData) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String wifiPack = JsonUtils.getInstance().packListToJson(wifiData);
		params.add(new BasicNameValuePair("wifiData", wifiPack));
		String url = "tMap/wifi/wifiLocating/";
		return JsonUtils.parseLocation(HttpUtils.getInstance().postData(AppConfig.SERVER_ADDR + url, params));
	}
}
