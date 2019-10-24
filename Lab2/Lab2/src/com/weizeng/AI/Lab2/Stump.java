//package com.weizeng.AI.Lab2;

import java.util.ArrayList;
/*
 * Wei Zeng
 * Lab 2
 * 11/28/2018
 * */
public class Stump {
	public int numOfDim;//which feature the stump selects 
	public double rateOfError;//the rate of error of the stump
	
	public Stump(int numOfDim) {
		this.numOfDim = numOfDim;
	}
	
	public int classify(Data data) {
		//judge the type of the instance according to whether the instance contains the feature or not 
		int result = 0;
		if (data.getAttrList().get(this.numOfDim) == 1) {
			result = 1;
		}else if (data.getAttrList().get(this.numOfDim) == 0) {
			result = -1;
		}
		return result;
	}
	
	public void train(ArrayList<Data> datas, double[] weightOfSamples) {
		//use the set of samples to train the stump
		double rate = 0.0;
		for(int i = 0; i < datas.size(); i++) {
			if (classify(datas.get(i)) != datas.get(i).getResult()) {
				rate += weightOfSamples[i];
			}
		}
		this.rateOfError = rate;
	}

}
