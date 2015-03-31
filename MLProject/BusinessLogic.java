

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * 
 * @author upendra
 *
 */
public class BusinessLogic {
	
	private int numberOfFolds;
	private double numberOfExamples;
	private double numberOfPermutations;
	private String[] permutations;
	private ArrayList<CoordinatesAndValues> examples;
	private char[][] outputData;

	/**
	 * set cross validation process
	 * @param crossValidationPath
	 * @param dataInfoPath
	 * @throws FileNotFoundException
	 */
	public void setUpCrossValidationStuff(String crossValidationPath, String dataInfoPath) throws FileNotFoundException {
		Scanner input = new Scanner(new File(crossValidationPath));
		while(input.hasNextLine()) {
			String[] firstLine = input.nextLine().split(" ");
			numberOfFolds = Integer.parseInt(firstLine[0]);
			numberOfExamples = Integer.parseInt(firstLine[1]);
			numberOfPermutations = Integer.parseInt(firstLine[2]);
			permutations = new String[(int)numberOfPermutations];
			for(int i = 0; i < numberOfPermutations; i++) {
				permutations[i] = input.nextLine().replaceAll(" ", "");
			}
		}
		
		input = new Scanner(new File(dataInfoPath));		
		while(input.hasNextLine()) {
			String[] firstLine = input.nextLine().split(" ");
			int rows = Integer.parseInt(firstLine[0]);
			int columns = Integer.parseInt(firstLine[1]);
			examples = new ArrayList<CoordinatesAndValues>();
			outputData = new char[rows][columns];
			for(int i = 0; i < rows; i++) {
				String[] sampleRow = input.nextLine().split(" ");
				for(int j = 0; j < columns; j++) {
					outputData[i][j] = sampleRow[j].charAt(0);
					if(!sampleRow[j].equals(".")) {
						examples.add(sampleRow[j].equals("+") ? new CoordinatesAndValues(j, i, true) : new CoordinatesAndValues(j, i, false));
					}
				}
			}			
		}
		input.close();	
	}
	
	/**
	 * startValidation
	 */
	public void startValidation() {
		int n = (int)numberOfExamples / numberOfFolds;
		for(int i = 1; i <= 5; i++) {
			double[] E = new double[(int)numberOfPermutations];
			for(int j = 0; j < numberOfPermutations; j++) {				
				double noOfErrors = 0;				
				if(numberOfExamples % numberOfFolds != 0) {
					for(int k = 0; k < numberOfFolds; k++) {
						String testSet = new String();
						if(k == numberOfFolds - 1) {
							testSet = permutations[j].substring(k * n, k * n + n + 1);							
						} else {
							testSet = permutations[j].substring(k * n, k * n + n);
						}
						String trainingSet = removeGivenSubString(permutations[j], testSet, n);
						noOfErrors += findNumberOfErrors(testSet, trainingSet, i);
					}
				} else {
					for(int k = 0; k < numberOfFolds; k++) {
						String testSet = permutations[j].substring(k * n, k * n + n);
						String trainingSet = removeGivenSubString(permutations[j], testSet, n);
						noOfErrors += findNumberOfErrors(testSet, trainingSet, i);
					}
				}
				E[j] = noOfErrors/numberOfExamples;
			}
			double e = 0;
			double variance = 0;
			for(int x = 0; x < numberOfPermutations; x++) {
				e += E[x];				
			}
			e = e / numberOfPermutations;
			System.out.println("K nearest neighbour when k = " + i);
			System.out.println("==================================");			
			System.out.println("error e is " + e);
			for(int x = 0; x < numberOfPermutations; x++) {
				variance += (E[x] - e) * (E[x] - e);
			}
			double standardDeviation = Math.sqrt(variance/(numberOfPermutations - 1));
			System.out.println("Standard deviation " + standardDeviation);
			
			printOutputData(i);
			System.out.println("==================================");	
			System.out.println("==================================");	
		}
	}
	
	private void printOutputData(int k) {
		for(int i = 0; i < outputData.length; i++) {
			for(int j =0; j < outputData[i].length; j++) {
				if(outputData[i][j] == '.') {
					getClassifiedValue(i, j, k);
					System.out.print(getClassifiedValue(i, j, k) + " ");
				} else {
					System.out.print(outputData[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	private char getClassifiedValue(int i, int j, int nearstNeighbour) {
		PriorityQueue<DistanceAndCoordinates> heap = new PriorityQueue<DistanceAndCoordinates>();
		for(int k = 0; k < examples.size(); k++) {
			double distance = getDistance(j, i, examples.get(k));	
			heap.add(new DistanceAndCoordinates(distance, k));			
		}
		
		return getClassifier(heap, nearstNeighbour) ? '+' : '-';
	}
	
	private double getDistance(int i, int j, CoordinatesAndValues coordinatesAndValues) {
		return Math.sqrt(Math.pow((i - coordinatesAndValues.getX1Coordinate()), 2) + Math.pow((j - coordinatesAndValues.getX2Coordinate()), 2));
	}

	private int findNumberOfErrors(String testSet, String trainingSet, int n) {
		int numberOfErrors = 0;
		char[] testPoints = testSet.toCharArray();
		char[] trainingPoints = trainingSet.toCharArray();
		for(int i = 0; i < testPoints.length; i++) {
			PriorityQueue<DistanceAndCoordinates> heap = new PriorityQueue<DistanceAndCoordinates>();
			for(int j = 0; j < trainingPoints.length; j++) {
				double distance = getDistance(Integer.parseInt(testPoints[i] + ""), Integer.parseInt(trainingPoints[j] + ""));
				heap.add(new DistanceAndCoordinates(distance, Integer.parseInt(trainingPoints[j] + "")));
			}
			boolean classifier = getClassifier(heap, n);
			if(classifier != examples.get(Integer.parseInt(testPoints[i] + "")).isValue()) {
				numberOfErrors++;
			}
		}
		return numberOfErrors;
	}
	
	private boolean getClassifier(PriorityQueue<DistanceAndCoordinates> heap, int n) {
		DistanceAndCoordinates minValue = heap.peek();
		int positiveCount = 0;
		int negativeCount = 0;
		while((!heap.isEmpty()) && (n > 0 || (heap.peek() != null && minValue.getDistance() == heap.peek().getDistance()))) {
			DistanceAndCoordinates current = heap.poll();
			if(examples.get(current.getIndex()).isValue()) {
				positiveCount++;
			} else {
				negativeCount++;
			}
			n--;
			minValue = current;
		}
		return positiveCount > negativeCount ? true : false;
	}

	private double getDistance(int testValuePoint, int traningValuePoint) {
		CoordinatesAndValues trainingValue = examples.get(traningValuePoint);
		CoordinatesAndValues testValue = examples.get(testValuePoint);
		return Math.sqrt(Math.pow((trainingValue.getX1Coordinate() - testValue.getX1Coordinate()), 2) + Math.pow((trainingValue.getX2Coordinate() - testValue.getX2Coordinate()), 2));		
	}

	private String removeGivenSubString(String input, String toBeRemoved, int n) {
		return input.substring(0, input.indexOf(toBeRemoved)) + input.substring(input.indexOf(toBeRemoved) + toBeRemoved.length(), input.length());
	}

	
}
