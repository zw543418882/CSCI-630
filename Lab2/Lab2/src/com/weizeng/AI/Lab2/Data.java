//package com.weizeng.AI.Lab2;

import java.util.ArrayList;
/*
 * Wei Zeng
 * Lab 2
 * 11/28/2018
 * */
public class Data {
	private ArrayList<Integer> attrList;//whether contain the feature
	
	private Integer result;// the type of instance
	
	public Data(ArrayList<Integer> attrList, int result) {
		this.attrList = attrList;
		this.result = result;
	}
	
	public void setAttrList(ArrayList<Integer> attrList) {
		this.attrList = attrList;
	}
	
	public ArrayList<Integer> getAttrList() {
		return attrList;
	}
	
	public void setResult(Integer result) {
		this.result = result;
	}
	
	public Integer getResult() {
		return result;
	}
	
	

}
