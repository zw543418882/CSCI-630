//package com.weizeng.AI.Lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/*
 * Wei Zeng
 * Lab 2
 * 11/28/2018
 * */
public class Wiki {
	static Integer[] transferStrArrToIntArr(String[] attrStrArr) {
		/*Read a string array which is an instance in training files
		 * transfer the string array to integer array excepts the last element which represents the instance is English or Dutch
		 * */
		Integer[] attrArr = new Integer[attrStrArr.length - 1];
		for (int i = 0; i < attrStrArr.length - 1; i++) {
			attrArr[i] = Integer.valueOf(attrStrArr[i]);
		}
		return attrArr;
	}

	static Node buildTrainingTree(String trainingFileName) {
		/*Use information gain to build a decision tree
		 * 1. Store all instances in training file
		 * 2. call function buildTree in class Node 
		 * */
		Node rootNode = null;
		ArrayList<Data> dataList = new ArrayList<>();
		try {
			File trainingFile = new File(trainingFileName);
			BufferedReader reader = new BufferedReader(new FileReader(trainingFile));
			String line = "";
			int numberOfLine = 1;
			while ((line = reader.readLine()) != null) {
				if (numberOfLine == 1) {
					numberOfLine++;
					continue;
				} else {
					String[] attrStrArray = line.split("            ");
					String[] attrStrArr = new String[11];
					for (int i = 0; i < 11; i++) {
						attrStrArr[i] = attrStrArray[i];
					}
					Integer[] attrArr = transferStrArrToIntArr(attrStrArr);
					Integer result = Integer.valueOf(attrStrArr[attrStrArr.length - 1]);
					ArrayList<Integer> attrList = new ArrayList<Integer>(
							Arrays.asList(attrArr).subList(0, attrArr.length));
					Data data = new Data(attrList, result);
					dataList.add(data);
				}
			}
			rootNode = new Node(dataList);
			rootNode.buildTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Decision Tree Has Been Generated!");
		return rootNode;
	}

	static boolean hasWord(String[] lineArr, String word, String word2) {
		/*judge a string array whether has a word or the word in capitals
		 * 1. if the array has return true
		 * 2. if the array does not have, return false
		 * */
		boolean hasWord = false;
		for (int i = 0; i < lineArr.length; i++) {
			if (lineArr[i].equals(word) || lineArr[i].equals(word2)) {
				hasWord = true;
				break;
			}
		}
		return hasWord;
	}

	static ArrayList<Integer> transferTestDataToDataList(String testFileName) {
		/*read test file and transfer the txt file to numeric array according to ten features
		 * 1. if the test file contains the feature, return 1
		 * 2. if the test file does not contain, return 0
		 * */
		int[] dataArr = new int[10];
		ArrayList<Integer> dataList = new ArrayList<>();
		for (int i = 0; i < dataArr.length; i++) {
			dataArr[i] = 0;
		}
		File testFile = new File(testFileName);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(testFile));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("q") || line.contains("Q")) {
					dataArr[0] = 1;
				}
				if (line.contains("x") || line.contains("X")) {
					dataArr[1] = 1;
				}
				if (line.contains("y") || line.contains("Y")) {
					dataArr[2] = 1;
				}
				String[] lineArr = line.split(" ");
				if (hasWord(lineArr, "is", "Is")) {
					dataArr[3] = 1;
				}
				if (hasWord(lineArr, "the", "The")) {
					dataArr[4] = 1;
				}
				if (hasWord(lineArr, "van", "Van")) {
					dataArr[5] = 1;
				}
				if (hasWord(lineArr, "voor", "Voor")) {
					dataArr[6] = 1;
				}
				if (hasWord(lineArr, "de", "De")) {
					dataArr[7] = 1;
				}
				if (hasWord(lineArr, "it", "It")) {
					dataArr[8] = 1;
				}
				if (hasWord(lineArr, "and", "And")) {
					dataArr[9] = 1;
				}
			}
			for (int i = 0; i < dataArr.length; i++) {
				dataList.add(Integer.valueOf(dataArr[i]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataList;
	}

	static ArrayList<Data> getDatas(String traningFileName) {
		/*1.when use the model of decision stumps, read training file and then transfer txt files to numeric arrays
		 * 2. if the txt file contains the feature, return 1
		 * 3. if the txt file does not contain, return -1
		 * 4. use an arrayList to store the instances
		 * */
		ArrayList<Data> datas = new ArrayList<>();
		try {
			File trainingFile = new File(traningFileName);
			BufferedReader reader = new BufferedReader(new FileReader(trainingFile));
			String line = "";
			int numberOfLine = 1;
			while ((line = reader.readLine()) != null) {
				if (numberOfLine == 1) {
					numberOfLine++;
					continue;
				} else {
					String[] attrStrArray = line.split("            ");
					String[] attrStrArr = new String[11];
					for (int i = 0; i < 11; i++) {
						attrStrArr[i] = attrStrArray[i];
					}
					Integer[] attrArr = transferStrArrToIntArr(attrStrArr);
					Integer result = 0;
					if (Integer.valueOf(attrStrArr[attrStrArr.length - 1]) == 0) {
						result = -1;
					} else if (Integer.valueOf(attrStrArr[attrStrArr.length - 1]) == 1) {
						result = 1;
					}
					ArrayList<Integer> attrList = new ArrayList<Integer>(
							Arrays.asList(attrArr).subList(0, attrArr.length));
					Data data = new Data(attrList, result);
					datas.add(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}

	static int findStumpOfMinRateOfErr(ArrayList<Stump> stumps, ArrayList<Data> datas, double[] weightOfSamples) {
		/*find the stump of the minimum rate of error 
		 * 1. use samples with same weights to train all stumps
		 * 2. find the stump with the minimum rate of error
		 * */
		double rateOfError = Double.MAX_VALUE;
		int numOfStump = -1;
		Stump stump = null;
		for (int i = 0; i < stumps.size(); i++) {
			stump = stumps.get(i);
			stump.train(datas, weightOfSamples);
			if (stump.rateOfError < rateOfError) {
				rateOfError = stump.rateOfError;
				numOfStump = i;
			}
		}
		return numOfStump;
	}

	public static void main(String[] args) {
		Node rootNode = null;
		ArrayList<Stump> selectedStumps = null;
		ArrayList<Double> weightOfStupms = null;
		System.out.println("(Training Command: train dt/ds 10/20/50)");
		System.out.println("(Predict Command: predict fileName.txt)");
		while (true) {
			System.out.println("");
			System.out.println("Enter a command Please:");
			Scanner reader = new Scanner(System.in);
			String command = reader.nextLine();
			if (command.equals("Quit")) {
				break;
			} else {
				String[] commandArr = command.split(" ");
				String trainingFileName = "";
				if (commandArr[0].equals("train")) {
					if (commandArr[2].equals("50")) {
						trainingFileName = "50wordstraining.txt";
					} else if (commandArr[2].equals("20")) {
						trainingFileName = "20wordstraining.txt";
					} else if (commandArr[2].equals("10")) {
						trainingFileName = "10wordstraining.txt";
					}
					if (commandArr[1].equals("dt")) {
						//use decision tree to train 
						rootNode = buildTrainingTree(trainingFileName);
						selectedStumps = null;
					} else if (commandArr[1].equals("ds")) {
						//use decision stumps to train
						ArrayList<Data> datas = getDatas(trainingFileName);
						double[] weightOfSamples = new double[datas.size()];
						for (int i = 0; i < weightOfSamples.length; i++) {
							weightOfSamples[i] = 1.0 / weightOfSamples.length;
						}
						ArrayList<Stump> stumps = new ArrayList<>();
						for (int i = 0; i < datas.get(0).getAttrList().size(); i++) {
							Stump stump = new Stump(i);
							stumps.add(stump);
						}
						selectedStumps = new ArrayList<>();//the stumps that the model selected after training
						weightOfStupms = new ArrayList<>();
						for (int t = 0; t < 10; t++) {
							int numOfStump = findStumpOfMinRateOfErr(stumps, datas, weightOfSamples);
							Stump stump = null;
							if (numOfStump >= 0) {
								stump = stumps.get(numOfStump);
							} else {
								break;
							}
							double rateOfError = stump.rateOfError;
							double weightOfStump = 0.5 * Math.log((1 - rateOfError) / rateOfError);// update the weight of stumps
							if (!selectedStumps.contains(stump)) {
								selectedStumps.add(stump);
								weightOfStupms.add(weightOfStump);
							}
							double z = 0.0;
							for (int i = 0; i < weightOfSamples.length; i++) {
								//update weights of samples
								weightOfSamples[i] = weightOfSamples[i] * Math
										.exp(-weightOfStump * datas.get(i).getResult() * stump.classify(datas.get(i)));
								z += weightOfSamples[i];
							}
							for (int i = 0; i < weightOfSamples.length; i++) {
								weightOfSamples[i] /= z;
							}
							if (rateOfError < 0.05) {
								//the threshold 
								break;
							}
							stumps.remove(numOfStump);
						}
						System.out.println("Decision Stumps Have Been Generated!");
						rootNode = null;
					}
				} else if (commandArr[0].equals("predict")) {
					String testFileName = commandArr[1];
					ArrayList<Integer> testDataList = transferTestDataToDataList(testFileName);
					if (rootNode != null) {
						/*predict the type of articles by decision tree
						 * 1.use the numeric data of test file on the model of decision tree, then find the leaf node the test data arrives
						 * 2. if the value of the leaf node is -2, the test data is English
						 * 3. if the value of the leaf node is -3, the test data is Dutch
						 * */
						Node leafNode = rootNode.getResult(testDataList);
						if (leafNode.getIndexOfAttr() == -2) {
							System.out.println("The Article is English!");
						}
						if (leafNode.getIndexOfAttr() == -3) {
							System.out.println("The Article is Dutch!");
						}
					} else if (selectedStumps != null) {
						/*predict the type of articles by decision stumps
						 * 1. if the total value is negative, the article is Dutch
						 * 2. if the total value is positive, the article is English
						 * */
						double result = 0.0;
						Data data = new Data(testDataList, 0);
						for (int i = 0; i < selectedStumps.size(); i++) {
							Stump stump = selectedStumps.get(i);
							result += stump.classify(data) * weightOfStupms.get(i);
						}
						if (result >0) {
							System.out.println("The Article is English!");
						}else {
							System.out.println("The Article is Dutch!");
						}
					}else {
						System.out.println("Build Decision Tree or Decision Stumps First!");
					}
				} else {
					System.out.println("Wrong Command! Please Try Again!");
				}
			}
		}
	}
}
