package com.tmap.library.datatransfer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmap.library.server_interface.Location;

public class JsonUtils {
	private JsonUtils() {}
	private static JsonUtils instance = new JsonUtils();
	public static JsonUtils getInstance() {
		return instance;
	}
	
	/** Packing */
	public String packStringToJson(String obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
	public String packObjToJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
	public String packListToJson(List objs) {
		Gson gson = new Gson();
		return gson.toJson(objs);
	}
	
	public String packMapToJson(Map objs) {
		Gson gson = new Gson();
		return gson.toJson(objs);
	}
	
	
	/** Unpacking */
	public String parseString(String data) {
		Gson gson = new Gson();
		return gson.fromJson(data,  String.class);
	}
	
	public static String[] parseStringList(String data) {
	 	Gson gson = new Gson();
		return gson.fromJson(data, String[].class);
	}
	
	public static Location parseLocation(String data) {
		Gson gson = new Gson();
		return gson.fromJson(data, Location.class);
	}
}
