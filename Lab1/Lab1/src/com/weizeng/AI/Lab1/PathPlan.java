//package com.weizeng.AI.Lab1;

/*Wei Zeng
 * CSCI630 Lab1
 * 10/11/2018
 * */
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;


import javax.imageio.ImageIO;

public class PathPlan {
	private ArrayList<MapPoint> mapPoints;//all points at map
	
	private ArrayList<MapPoint> openList;//all points waiting for check
	
	private ArrayList<MapPoint> closeList;// all points have been checked
	
	private Player player;// the player to join activities
	
	// set/get functions
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setMapPoints(ArrayList<MapPoint> mapPoints) {
		this.mapPoints = mapPoints;
	}

	public ArrayList<MapPoint> getMapPoints() {
		return mapPoints;
	}
	
	public void setOpenList(ArrayList<MapPoint> openList) {
		this.openList = openList;
	}
	
	public ArrayList<MapPoint> getOpenList() {
		return openList;
	}
	
	public void setCloseList(ArrayList<MapPoint> closeList) {
		this.closeList = closeList;
	}
	
	public ArrayList<MapPoint> getCloseList() {
		return closeList;
	}

	
	public float[][] loadElevationText(String fileName) {
		//store elevation information of all points
		float[][] elevationArr = new float[500][395];
		int elevationArrIndex = 0;
		File elevationFile = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(elevationFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] lineStrArr = line.split("   ");
				for (int i = 1; i <= 395; i++) {
					BigDecimal db = new BigDecimal(lineStrArr[i]);
					elevationArr[elevationArrIndex][i - 1] = Float.valueOf(db.toPlainString());
				}
				elevationArrIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elevationArr;
	}

	public String[][] loadTerrainMap(String fileName) {
		// store terrain information of all information
		String[][] terrainArr = new String[500][395];
		if (fileName != null) {
			BufferedImage terrMap;
			try {
				terrMap = ImageIO.read(new File(fileName));
				for (int i = 0; i < 500; i++) {
					for (int j = 0; j < 395; j++) {
						int pixel = terrMap.getRGB(j, i);
						int[] rgbArr = new int[3];
						rgbArr[0] = (pixel & 0xff0000) >> 16;
						rgbArr[1] = (pixel & 0xff00) >> 8;
						rgbArr[2] = (pixel & 0xff);
						if (rgbArr[0] == 248 && rgbArr[1] == 148 && rgbArr[2] == 18) {
							terrainArr[i][j] = "Open land";
						}
						if (rgbArr[0] == 255 && rgbArr[1] == 192 && rgbArr[2] == 0) {
							terrainArr[i][j] = "Rough meadow";
						}
						if (rgbArr[0] == 255 && rgbArr[1] == 255 && rgbArr[2] == 255) {
							terrainArr[i][j] = "Easy movement forest";
						}
						if (rgbArr[0] == 2 && rgbArr[1] == 208 && rgbArr[2] == 60) {
							terrainArr[i][j] = "Slow run forest";
						}
						if (rgbArr[0] == 2 && rgbArr[1] == 136 && rgbArr[2] == 40) {
							terrainArr[i][j] = "Walk forest";
						}
						if (rgbArr[0] == 5 && rgbArr[1] == 73 && rgbArr[2] == 24) {
							terrainArr[i][j] = "Impassible vegetation";
						}
						if (rgbArr[0] == 0 && rgbArr[1] == 0 && rgbArr[2] == 255) {
							terrainArr[i][j] = "Lake/Swamp/Marsh";
						}
						if (rgbArr[0] == 71 && rgbArr[1] == 51 && rgbArr[2] == 3) {
							terrainArr[i][j] = "Paved road";
						}
						if (rgbArr[0] == 0 && rgbArr[1] == 0 && rgbArr[2] == 0) {
							terrainArr[i][j] = "Footpath";
						}
						if (rgbArr[0] == 205 && rgbArr[1] == 0 && rgbArr[2] == 101) {
							terrainArr[i][j] = "Out of bounds";
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return terrainArr;
	}

	public ArrayList<MapPoint> findPath(MapPoint startPoint, MapPoint desPoint) {
		/*Use A* Algorithm to find the lowest cost path between two points
		 * G: the real time cost from start point to current point
		 * H: the evaluation time cost from current point to destination point
		 * F: the sum of G and H
		 * */
		this.openList.add(startPoint);
		startPoint.setG(0);
		this.H(startPoint, desPoint);
		this.F(startPoint);
		while(!this.openList.isEmpty()) {
			MapPoint currPoint = this.findNextPoint();
			this.closeList.add(currPoint);
			this.openList.remove(currPoint);
			if (currPoint.equalTo(desPoint)) {
				break;
			}
			for(MapPoint nextPoint : this.findNeighbors(currPoint)) {
				if (this.closeList.contains(nextPoint)) {
					continue;
				}
				if (!this.openList.contains(nextPoint)) {
					nextPoint.setParentPoint(currPoint);
					this.G(nextPoint);
					this.H(nextPoint, desPoint);
					this.F(nextPoint);
					this.openList.add(nextPoint);
				}else {
					MapPoint temP = new MapPoint(nextPoint.getX(), nextPoint.getY(), nextPoint.getElevation(), nextPoint.getTerrainType());
					temP.setParentPoint(currPoint);
					this.G(temP);
					if (temP.getG() < nextPoint.getG()) {
						nextPoint.setParentPoint(currPoint);
						this.G(nextPoint);
						this.H(nextPoint, desPoint);
						this.F(nextPoint);
					}
				}
			}
		}
		if (openList.isEmpty()) {
			desPoint.setParentPoint(null);
		}
		ArrayList<MapPoint> pathPoints = new ArrayList<>();
		MapPoint point = desPoint;
		while(point != null) {
			pathPoints.add(point);
			point = point.getParentPoint();
		}
		ArrayList<MapPoint> returnArr = new ArrayList<>();
		for(int i = pathPoints.size() - 1; i >= 0; i--) {
			returnArr.add(pathPoints.get(i));
		}
		return returnArr;
	}
	
	public ArrayList<MapPoint> findScoreOPath(ArrayList<MapPoint> controlsList, float time) {
		/*find the path including most control points in ScoreO activities
		 * 1. use findPath function to check all cost from start point to the control points 
		 * 2. select the lowest cost point and use the point as start point
		 * 3. repeat step1 and step2 until no point in control points list or there is no time
		 * */
		MapPoint originalPoint = controlsList.get(0);
		MapPoint start = controlsList.get(0);
		controlsList.remove(start);
		float realTime = 0;
		ArrayList<MapPoint> resultList = new ArrayList<>();
		MapPoint lastPoint = null;
		while(controlsList.size() > 0) {
			int nextIndex = 0;
			float sumCost = time;
			for(int i = 0; i < controlsList.size(); i++) {
				ArrayList<MapPoint> lowestCostPath = this.findPath(start, controlsList.get(i));
				float realG = lowestCostPath.get(lowestCostPath.size() - 1).getG();
				this.H(controlsList.get(i), originalPoint);
				float realH = controlsList.get(i).getH();
				if (realG + realH < sumCost) {
					sumCost = realG + realH;
					nextIndex = i;
				}
				this.openList.clear();
				this.closeList.clear();
				this.resetMapPoints();
			}
			ArrayList<MapPoint> tempArr = this.findPath(start, controlsList.get(nextIndex));
			this.H(tempArr.get(tempArr.size() - 1), originalPoint);
			if (tempArr.get(tempArr.size() - 1).getG() + tempArr.get(tempArr.size() - 1).getH() < time) {
				lastPoint = controlsList.get(nextIndex);
				for(int i = 0; i < tempArr.size() - 1; i++) {
					resultList.add(tempArr.get(i));
				}
			}else {
				break;
			}
			this.resetMapPoints();
			start = tempArr.get(tempArr.size() - 1);
			controlsList.remove(start);	
		}
		resultList.add(lastPoint);
		return resultList;
	}
	
	public MapPoint findNextPoint() {
		//find the point of the lowest f value in open list
		int index = 0;
		float lowestF = this.getOpenList().get(0).getF();
		for(int i = 0; i < this.getOpenList().size(); i++) {
			if (this.getOpenList().get(i).getF() <= lowestF) {
				lowestF = this.getOpenList().get(i).getF();
				index = i;
			}
		}
		return this.getOpenList().get(index);
	}
	
	public ArrayList<MapPoint> findNeighbors(MapPoint currPoint) {
		//find all eight neighbor points and judge whether they can pass or not
		ArrayList<MapPoint> neighborPoints = new ArrayList<>();
		ArrayList<MapPoint> returnNeighbors = new ArrayList<>();
		neighborPoints.add(this.getPoint(currPoint.getX() - 1, currPoint.getY() - 1));
		neighborPoints.add(this.getPoint(currPoint.getX(), currPoint.getY() - 1));
		neighborPoints.add(this.getPoint(currPoint.getX() + 1, currPoint.getY() - 1));
		neighborPoints.add(this.getPoint(currPoint.getX() + 1, currPoint.getY()));
		neighborPoints.add(this.getPoint(currPoint.getX() + 1, currPoint.getY() + 1));
		neighborPoints.add(this.getPoint(currPoint.getX(), currPoint.getY() + 1));
		neighborPoints.add(this.getPoint(currPoint.getX() - 1, currPoint.getY() + 1));
		neighborPoints.add(this.getPoint(currPoint.getX() - 1, currPoint.getY()));
		for(int i = 0; i < neighborPoints.size(); i++) {
			if (neighborPoints.get(i) != null) {
				if (neighborPoints.get(i).getTerrainType().equals("Lake/Swamp/Marsh") || neighborPoints.get(i).getTerrainType().equals("Impassible vegetation")) {
					continue;
				}else {
					returnNeighbors.add(neighborPoints.get(i));
				}
			}
		}
		
		return returnNeighbors;
	}
	
	public void G(MapPoint currPoint) {
		//real time cost to current point
		int length = 0;
		if (currPoint.getX() != currPoint.getParentPoint().getX() && currPoint.getY() != currPoint.getParentPoint().getY()) {
			int xDiff = Math.abs(currPoint.getX() - currPoint.getParentPoint().getX());
			int yDiff = Math.abs(currPoint.getY() - currPoint.getParentPoint().getY());
			xDiff = 10 * xDiff;
			yDiff = 7 * yDiff;
			length = (int) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		}else if (currPoint.getX() == currPoint.getParentPoint().getX()) {
			int yDiff = Math.abs(currPoint.getY() - currPoint.getParentPoint().getY());
			length = yDiff * 7;
		}else if (currPoint.getY() == currPoint.getParentPoint().getY()) {
			int xDiff = Math.abs(currPoint.getX() - currPoint.getParentPoint().getX());
			length = xDiff * 10;
		}
		int speed = this.getPlayer().getSpeeds()[this.matchSpeeds(currPoint.getTerrainType())];
		if (currPoint.getElevation() - currPoint.getParentPoint().getElevation() > 5) {
			if (currPoint.getElevation() - currPoint.getParentPoint().getElevation() > 10) {
				speed = speed - 5;
			}else {
				speed = speed - 2;
			}
		}
	    float timeCost = length / speed;
	    currPoint.setG(currPoint.getParentPoint().getG() + timeCost);
	}
	
	public void H(MapPoint currPoint, MapPoint desPoint) {
		//evaluation time cost from current point to destination point
		int xDiff = Math.abs(desPoint.getX() - currPoint.getX());
		int yDiff = Math.abs(desPoint.getY() - currPoint.getY());
		int length = 0;
		if (yDiff == 0) {
			length = xDiff * 10;
		}
		if (xDiff == 0) {
			length = yDiff * 7;
		}else {
			xDiff = 10 * xDiff;
			yDiff = 7 * yDiff;
			length = (int) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		}
		float speed = (this.getPlayer().getSpeeds()[this.matchSpeeds(currPoint.getTerrainType())] + this.getPlayer().getSpeeds()[this.matchSpeeds(desPoint.getTerrainType())]) / 2;
		currPoint.setH(length / speed);
	}
	
	public void F(MapPoint currPoint) {
		// the sum of G and H
		currPoint.setF(currPoint.getG() + currPoint.getH());
	}

	public MapPoint getPoint(int x, int y) {
		//find the point by x coordinates and y coordinates
		MapPoint point = null;
		for(int i = 0; i < this.getMapPoints().size(); i++) {
			if (this.getMapPoints().get(i).getX() == x && this.getMapPoints().get(i).getY() == y) {
				point = this.getMapPoints().get(i);
			}
		}
		return point;
	}
	
	public int matchSpeeds(String terrainType) {
		//Use terrain type to find the index of speeds array
		int index = -1;
		if (terrainType.equals("Open land")) {
			index = 0;
		}
		if (terrainType.equals("Rough meadow")) {
			index = 1;
		}
		if (terrainType.equals("Easy movement forest")) {
			index = 2;
		}
		if (terrainType.equals("Slow run forest")) {
			index = 3;
		}
		if (terrainType.equals("Walk forest")) {
			index = 4;
		}
		if (terrainType.equals("Paved road")) {
			index = 5;
		}
		if (terrainType.equals("Footpath")) {
			index = 6;
		}
		if (terrainType.equals("Out of bounds")) {
			index = 7;
		}
		return index;
	}
	
	public ArrayList<String> readFile(String fileName) {
		//read a text file which including all goal points 
		ArrayList<String> pointStr = new ArrayList<>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				pointStr.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pointStr;
	}
	
	public ArrayList<MapPoint> convertToPoints(ArrayList<String> pointsArr) {
		//convert the points in string to the points in array list
		ArrayList<MapPoint> convertPoints = new ArrayList<>();
		for(int i = 0; i < pointsArr.size(); i++) {
			String pointStr = pointsArr.get(i);
			String[] tempArr = pointStr.split(" ");
			convertPoints.add(this.getPoint(Integer.valueOf(tempArr[0]), Integer.valueOf(tempArr[1])));
		}
		return convertPoints;
	}
	
	public void resetMapPoints() {
		//reset all points
		for(int i = 0; i < this.getMapPoints().size(); i++) {
			this.getMapPoints().get(i).setG(0);
			this.getMapPoints().get(i).setH(0);
			this.getMapPoints().get(i).setF(0);
			this.getMapPoints().get(i).setParentPoint(null);
		}
	}
	
	public static void main(String[] args) {
		PathPlan pathPlan = new PathPlan();//executer
		float[][] elevationArr = pathPlan.loadElevationText(args[0]);
		String[][] terrMapArr = pathPlan.loadTerrainMap(args[1]);

		ArrayList<MapPoint> mapPoints = new ArrayList<>();
		for (int i = 0; i < 500; i++) {
			for (int j = 0; j < 395; j++) {
				MapPoint point = new MapPoint(j, i, elevationArr[i][j], terrMapArr[i][j]);
				mapPoints.add(point);
			}
		}
		pathPlan.setMapPoints(mapPoints);//store all points in the map
		pathPlan.setOpenList(new ArrayList<>());
		pathPlan.setCloseList(new ArrayList<>());
		//player1 abilities
		int[] speeds = new int[8];
		speeds[0] = 60;
		speeds[1] = 10;
		speeds[2] = 40;
		speeds[3] = 30;
		speeds[4] = 20;
		speeds[5] = 80;
		speeds[6] = 70;
		speeds[7] = 50;
		Player player = new Player("Wei Zeng", speeds);//player1
		pathPlan.setPlayer(player);
		
		String fileName = args[2];
		ArrayList<String> pointStr = pathPlan.readFile(fileName);
		String eventType = pointStr.get(0);
		float time = 0;
		if (pointStr.get(0).equals("Classic")) {
			pointStr.remove(pointStr.get(0));
		}else if (pointStr.get(0).equals("ScoreO")) {
			time = Integer.valueOf(pointStr.get(1)) / 60;
			String type = pointStr.get(0);
			String timeT = pointStr.get(1);
			pointStr.remove(type);
			pointStr.remove(timeT);
		}
		ArrayList<MapPoint> pointsArr = pathPlan.convertToPoints(pointStr);
		
		ArrayList<MapPoint> pathPoints = new ArrayList<>();
		if (eventType.equals("Classic")) {
			for(int i = 0; i < pointsArr.size() - 1; i++) {
				MapPoint startPoint = pointsArr.get(i);
				MapPoint desPoint = pointsArr.get(i + 1);
				ArrayList<MapPoint> tempArr = pathPlan.findPath(startPoint, desPoint);
				for(int j = 0; j < tempArr.size() - 1; j++) {
					pathPoints.add(tempArr.get(j));
				}
				pathPlan.resetMapPoints();
				pathPlan.openList = new ArrayList<>();
				pathPlan.closeList = new ArrayList<>();	
			}
			pathPoints.add(pointsArr.get(pointsArr.size() - 1));
			//print classic path
			for(int i = 0; i < pathPoints.size(); i++) {
				if (i < pathPoints.size() - 1) {
					if (pointsArr.contains(pathPoints.get(i))) {
						System.out.print("**(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")**" + " -> ");
					}else {
						System.out.print("(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")" + " -> ");
					}
				}else {
					System.out.print("**(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")**");
				}
			}
		}
		
		
		if (eventType.equals("ScoreO")) {
			ArrayList<MapPoint> listCopy = new ArrayList<>();
			for(int i = 0; i < pointsArr.size(); i++) {
				listCopy.add(pointsArr.get(i));
			}
			pathPoints = pathPlan.findScoreOPath(listCopy, time - 30);
			pathPlan.openList.clear();
			pathPlan.closeList.clear();
			pathPlan.resetMapPoints();
			ArrayList<MapPoint> toStartPath = pathPlan.findPath(pathPoints.get(pathPoints.size() - 1), pathPoints.get(0));
			for(int i = 1; i < toStartPath.size(); i++) {
				pathPoints.add(toStartPath.get(i));
			}
			int sum = 0;
			//print player1 path
			for(int i = 0; i < pathPoints.size(); i++) {
				if (i < pathPoints.size() - 1) {
					if (pointsArr.contains(pathPoints.get(i))) {
						sum++;
						System.out.print("**(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")**" + " -> ");
					}else {
						System.out.print("(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")" + " -> ");
					}
				}else {
					System.out.print("**(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")**");
				}
			}
			System.out.println("");
			System.out.println(pathPlan.getPlayer().getPlayerName() + " Passed " + (sum - 1) + " Control Points!");
			
			System.out.println("\n");
			//player2 abilities
			int[] speedsTwo = new int[8];
			speedsTwo[0] = 30;
			speedsTwo[1] = 8;
			speedsTwo[2] = 20;
			speedsTwo[3] = 15;
			speedsTwo[4] = 10;
			speedsTwo[5] = 40;
			speedsTwo[6] = 40;
			speedsTwo[7] = 30;
			Player playerTwo = new Player("Turtle", speedsTwo);// player2
			pathPlan.setPlayer(playerTwo);
			pathPoints.clear();
			ArrayList<MapPoint> listCopyTwo = new ArrayList<>();
			for(int i = 0; i < pointsArr.size(); i++) {
				listCopyTwo.add(pointsArr.get(i));
			}
			pathPoints = pathPlan.findScoreOPath(listCopyTwo, time - 30);
			pathPlan.openList.clear();
			pathPlan.closeList.clear();
			pathPlan.resetMapPoints();
			ArrayList<MapPoint> toStartPathTwo = pathPlan.findPath(pathPoints.get(pathPoints.size() - 1), pathPoints.get(0));
			for(int i = 1; i < toStartPathTwo.size(); i++) {
				pathPoints.add(toStartPathTwo.get(i));
			}
			int sumTwo = 0;
			//print player2 path
			for(int i = 0; i < pathPoints.size(); i++) {
				if (i < pathPoints.size() - 1) {
					if (pointsArr.contains(pathPoints.get(i))) {
						sumTwo++;
						System.out.print("**(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")**" + " -> ");
					}else {
						System.out.print("(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")" + " -> ");
					}
				}else {
					System.out.print("**(" + pathPoints.get(i).getX() + ", " + pathPoints.get(i).getY() + ")**");
				}
			}
			System.out.println("");
			System.out.println(pathPlan.getPlayer().getPlayerName() + " Passed " + (sumTwo - 1) + " Control Points!");
		}
	}
}


