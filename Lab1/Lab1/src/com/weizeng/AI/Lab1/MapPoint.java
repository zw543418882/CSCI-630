//package com.weizeng.AI.Lab1;

public class MapPoint {
	private float elevation;
	private String terrainType;
	private int x;
	private int y;
	private float g;
	private float h;
	private float f;
	private MapPoint parentPoint;
	
	public void setElevation(float elevation) {
		this.elevation = elevation;
	}
	
	public float getElevation() {
		return elevation;
	}
	
	public void setTerrainType(String terrainType) {
		this.terrainType = terrainType;
	}
	
	public String getTerrainType() {
		return terrainType;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	public void setG(float g) {
		this.g = g;
	}
	
	public float getG() {
		return g;
	}
	
	public void setH(float h) {
		this.h = h;
	}
	
	public float getH() {
		return h;
	}
	
	public void setF(float f) {
		this.f = f;
	}
	
	public float getF() {
		return f;
	}
	
	public void setParentPoint(MapPoint parentPoint) {
		this.parentPoint = parentPoint;
	}
	
	public MapPoint getParentPoint() {
		return parentPoint;
	}
	
	public boolean equalTo(MapPoint point) {
		Boolean result = false;
		if (this.x  == point.getX() && this.y == point.getY()) {
			result = true;
		}
		return result;
	}
	
	public MapPoint(int x, int y, float elevation, String terrainType) {
		this.x = x;
		this.y = y;
		this.elevation = elevation;
		this.terrainType = terrainType;
	}
	

}
