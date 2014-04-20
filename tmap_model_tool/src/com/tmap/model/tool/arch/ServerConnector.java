package com.tmap.model.tool.arch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.tmap.library.SystemConfig;
import com.tmap.library.datatransfer.HttpUtils;

// Communicate with Server, encapsulated functions to link specific URLs
public class ServerConnector {
	private ServerConnector instance = new ServerConnector();
	private ServerConnector() {}
	public ServerConnector getInstance() {
		return instance;
	}
	
	// Upload a wifi sample data
	public String uploadWifi(int roomId, float x, float y, float z, String wifiData) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("wifiData", wifiData));
		String url = "tMap/wifi/uploadWifi/" + roomId + "/" + x + "/" + y + "/" + z;
		return HttpUtils.getInstance().postData(SystemConfig.SERVER_ADDR + url, params);
	}
	
	//
}
