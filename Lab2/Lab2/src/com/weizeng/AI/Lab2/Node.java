//package com.weizeng.AI.Lab2;

import java.util.ArrayList;
/*
 * Wei Zeng
 * Lab 2
 * 11/28/2018
 * */
public class Node {
	private int indexOfAttr;
	
	private ArrayList<Data> dataList;
	
	private Node leftNode;
	
	private Node rightNode;
	
	private int leftValue;
	
	private int rightValue;
	
	private double entropy;
	
	private boolean isLeaf;
	
	public double getEntropy() {
		return entropy;
	}
	
	public boolean getIsLeaf() {
		return this.isLeaf;
	}
	
	public int getIndexOfAttr() {
		return indexOfAttr;
	}
	
	public Node getLeftNode() {
		return leftNode;
	}
	
	public Node getRightNode() {
		return rightNode;
	}
	
	public int getLeftValue() {
		return leftValue;
	}
	
	public int getRightValue() {
		return rightValue;
	}
	
	public void setIndexOfAttr(int indexOfAttr) {
		this.indexOfAttr = indexOfAttr;
	}
	
	public boolean isLeafNode() {
		//judge whether the node is leaf node or not
		int result = this.dataList.get(0).getResult();
		for(int i = 1; i < this.dataList.size(); i++) {
			if (this.dataList.get(i).getResult() != result) {
				return false;
			}
		}
		return true;
	}
	
	public double calcEntropy() {
		//calculate the entropy of the node
		if (this.dataList.isEmpty()) {
			return 0;
		}
		int numberOfGoalResult = 0;
		for(int i = 0; i < this.dataList.size(); i++) {
			if (this.dataList.get(i).getResult() == 1) {
				numberOfGoalResult++;
			}
		}
		double p1 = ((double)numberOfGoalResult) / this.dataList.size();
		double p2 = 1 - p1;
		if (p1 == 0 || p1 == 1) {
			return 0;
		}else {
			return -p1 * (Math.log(p1) / Math.log(2)) - p2 * (Math.log(p2) / Math.log(2));
		}
	}
	
	public Node(ArrayList<Data> dataList) {
		this.dataList = dataList;
		this.entropy = calcEntropy();
		this.isLeaf = isLeafNode();
		if (this.isLeaf && this.dataList.get(0).getResult() == 1) {
			this.indexOfAttr = -2;
		}
		if (this.isLeaf && this.dataList.get(0).getResult() == 0) {
			this.indexOfAttr = -3;
		}
		if (!this.isLeaf) {
			this.indexOfAttr = -1;
		}
	}
	
	public void splitData(int indexOfAttr) {
		//after the node has selected the feature, split all instances in the node to child nodes.
		this.indexOfAttr = indexOfAttr;
		if (!this.isLeaf) {
			ArrayList<Data> left = new ArrayList<>();
			ArrayList<Data> right = new ArrayList<>();
			int flag = this.dataList.get(0).getAttrList().get(indexOfAttr);
			for(int i = 0; i < this.dataList.size(); i++) {
				if (this.dataList.get(i).getAttrList().get(indexOfAttr) == flag) {
					left.add(this.dataList.get(i));
				}else {
					right.add(this.dataList.get(i));
				}
			}
			if (left.size() > 0) {
				this.leftNode = new Node(left);
				this.leftValue = flag;
			}
			if (right.size() > 0) {
				this.rightNode = new Node(right);
				this.rightValue = right.get(0).getAttrList().get(indexOfAttr);
			}
		}
	}
	
	public int selectAttr() {
		//select the feature which can get maximum information gain
		int indexOfAttr = -1;
		double maxInformationGain = 0;
		double leftEntropy = 0;
		double rightEntropy = 0;
		for(int i = 0; i < this.dataList.get(0).getAttrList().size(); i++) {
			Node temp = new Node(this.dataList);
			temp.splitData(i);
			if (temp.leftNode != null) {
				leftEntropy = ((double)temp.leftNode.dataList.size() / (double)temp.dataList.size()) * (temp.leftNode.calcEntropy()); 
			}else {
				leftEntropy = 0;
			}
			if (temp.rightNode != null) {
				rightEntropy = ((double)temp.rightNode.dataList.size() / (double)temp.dataList.size()) * (temp.rightNode.calcEntropy());
			}else {
				rightEntropy = 0;
			}
			double informationGain = temp.calcEntropy() - (leftEntropy + rightEntropy);
			if (informationGain > maxInformationGain) {
				indexOfAttr = i;
				maxInformationGain = informationGain;
			}
		}
		return indexOfAttr;
	}
	
	public void buildTree() {
		//built decision tree recursively 
		this.indexOfAttr = this.selectAttr();
		this.splitData(this.indexOfAttr);
		if ((this.leftNode != null) && (!this.leftNode.isLeaf)) {
			this.leftNode.buildTree();
		}
		if ((this.rightNode != null) && (!this.rightNode.isLeaf)) {
			this.rightNode.buildTree();
		}
	}
	
	public Node toNextNode(ArrayList<Integer> testData) {
		//when predict the type of instance, find the next child node according to the value of current node 
		if (testData.get(this.indexOfAttr) == this.leftValue) {
			return this.leftNode;
		}else {
			return this.rightNode;
		}
	}
	
	public Node getResult(ArrayList<Integer> testData) {
		//get the final leaf node the instance arrives 
		Node nextNode = this.toNextNode(testData);
		if (nextNode != null) {
			if (!nextNode.isLeaf) {
				return nextNode.toNextNode(testData);
			}else {
				return nextNode;
			}	
		}else {
			return this;
		}
	}
	
	
	
	
	
	
}
