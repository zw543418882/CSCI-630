//package com.weizeng.AI.Lab1;


public class Player {
	private String playerName;
	private int[] speeds;
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setSpeeds(int[] speeds) {
		this.speeds = speeds;
	}
	
	public int[] getSpeeds() {
		return speeds;
	}
	

	public Player(String playerName, int[] speeds) {
		this.playerName = playerName;
		this.speeds = speeds;
	}
	
	
	
	

}
