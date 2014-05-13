package com.tmap.library.server_interface;

public class Location {
	public int buildingId, roomId;
	public double x, y, z;
	
	public Location(int buildingId, int roomId, double x, double y, double z) {
		this.buildingId = buildingId;
		this.roomId = roomId;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
