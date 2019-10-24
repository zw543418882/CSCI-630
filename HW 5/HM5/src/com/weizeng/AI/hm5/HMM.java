package com.weizeng.AI.hm5;



public class HMM {

	public static void main(String[] args) {
		//set transition matrix
		double[][] transMatrix = new double[3][3];
		transMatrix[0][0] = 0.5;
		transMatrix[0][1] = 0.25;
		transMatrix[0][2] = 0.25;
		transMatrix[1][0] = 0.5;
		transMatrix[1][1] = 0.25;
		transMatrix[1][2] = 0.25;
		transMatrix[2][0] = 0.33333333;
		transMatrix[2][1] = 0.66666667;
		transMatrix[2][2] = 0.0;
		//set initial probability
		double[][] results = new double[12][3];
		results[0][0] = 0.5;
		results[0][1] = 0.5;
		results[0][2] = 0.0;
		//use transition matrix and the probability of previous state to calculate the current probability
		for(int i = 1; i < 12; i++) {
			results[i][0] = results[i - 1][0] * transMatrix[0][0] + results[i - 1][1] * transMatrix[1][0] + results[i - 1][2] * transMatrix[2][0];
			results[i][1] = results[i - 1][0] * transMatrix[0][1] + results[i - 1][1] * transMatrix[1][1] + results[i - 1][2] * transMatrix[2][1];
			results[i][2] = results[i - 1][0] * transMatrix[0][2] + results[i - 1][1] * transMatrix[1][2] + results[i - 1][2] * transMatrix[2][2];
		}
		//print the result
		for(int i = 0; i < 12; i++) {
			int step = i + 1;
			System.out.println("At the " + step + " time step," + " the probability distribution is:");
			System.out.format("Working: %.8f Surfing: %.8f Meeting: %.8f\n\n", results[i][0], results[i][1], results[i][2]);
		}
		

	}

}
