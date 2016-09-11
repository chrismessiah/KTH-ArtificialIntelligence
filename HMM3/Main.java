// https://kth.kattis.com/problems/kth.ai.hmm3

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.lang.Math;
import java.util.Collections;
import java.util.ArrayList;

public class Main {

    public static double[][] matrixMultiplier(double[][] A, double[][] B) {

        if (A[0].length != B.length) {
            System.out.println("ERROR MATRIX DIMENSIONS INCORRECT! A: " + Integer.toString(A[0].length) + " B: " + Integer.toString(B.length) + "\nSHUTTING DOWN PROGRAM");
            System.exit(0);
        }

        double[][] C = new double[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                C[i][j] = 0;
            }
        }

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    public static void printArray(double[] array) {
        System.out.println(Arrays.toString(array));
    }
    
    public static void printArray(int[] array) {
        System.out.println(Arrays.toString(array));
    }

    public static void printMatrix(double[][] matrix) {
        System.out.println("");
        for (int i=0;i < matrix.length; i++) {
            printArray(matrix[i]);
        }
    }
    
    public static void printMatrix(int[][] matrix) {
        System.out.println("");
        for (int i=0;i < matrix.length; i++) {
            printArray(matrix[i]);
        }
    }

    public static double[][] vectorToMatrix(String[] vector, int rows, int columns) {
        double[][] matrix = new double[rows][columns];
        int k=0;
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                matrix[i][j] = Double.parseDouble(vector[k]);
                k+=1;
            }
        }
        return matrix;
    }
    
    public static void shutDownProgram(String errorMessage) {
        System.out.println("ERROR: " + errorMessage);
        System.exit(0);
    }

    public static double[][] elementWiseProduct(double[][] vec1, double[][] vec2){
        
        /* OBS: include transpose function if vec1.length == 1. */
        if (vec1[0].length == 1){
            vec1 = transposeMatrix(vec1);
        }

        /* OBS: include transpose function if vec2.length == 1. */
        if (vec2[0].length == 1){
            vec2 = transposeMatrix(vec2);
        }
        
        double[][] resultVector = new double[1][vec1[0].length];

        // checks that length is equal
        if (vec1[0].length != vec2[0].length){
            shutDownProgram("elementWiseVectorProduct requires equal length matrices. \nSHUTTING DOWN PROGRAM");
        }

        // for each element, multiply
        for (int i=0; i<vec1[0].length; i++) {
            resultVector[0][i] = vec1[0][i] * vec2[0][i];
        }
        
        return resultVector;
    }



    public static void printMatrixForKattis(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        String output = "" + Integer.toString(rows) + " " + Integer.toString(columns) + " ";
        for (int i=0; i<rows; i++) {
            for (int j=0;j<columns; j++) {
                output += Double.toString(matrix[i][j]);
                if (i+1 != rows || j+1 != columns) {
                   output += " ";
                }
            }
        }
        System.out.println(output);
    }

    public static double[][] getInputAsMatrix(BufferedReader input) throws IOException {

        String inputData = input.readLine();
        String[] inputParts = inputData.split(" ");
        
        int rows = Integer.parseInt(inputParts[0]);
        int columns = Integer.parseInt(inputParts[1]);
        String[] inputVector = Arrays.copyOfRange(inputParts, 2, inputParts.length);
        double[][] matrix = vectorToMatrix(inputVector, rows, columns);
        return matrix;
    }
    
    public static double[][] getInputAsMatrix(BufferedReader input, String inputString) throws IOException {

        String[] inputParts = inputString.split(" ");
        
        int rows = Integer.parseInt(inputParts[0]);
        int columns = Integer.parseInt(inputParts[1]);
        String[] inputVector = Arrays.copyOfRange(inputParts, 2, inputParts.length);
        double[][] matrix = vectorToMatrix(inputVector, rows, columns);
        return matrix;
    }
    
    public static double[][] getColumnFromMatrix(double[][] matrix, int column) {
        double[][] columnVector = new double[matrix.length][1];
        for (int i=0;i<matrix.length; i++) {
            columnVector[i][0] = matrix[i][column];
        }
        return columnVector;
    }
    
    public static double[][] getRowFromMatrix(double[][] matrix, int rowNumber) {
        double[][] transpose = transposeMatrix(matrix);
        double[][] columnVector = getColumnFromMatrix(transpose, rowNumber);
        double[][] rowVector = transposeMatrix(columnVector);
        return rowVector;
    }
    
    public static double[][] transposeMatrix(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        double[][] newMatrix = new double[columns][rows];

        for(int i = 0; i < columns; i++) {
            for(int j = 0; j < rows; j++) {
                newMatrix[i][j] = matrix[j][i];
            }
        }

        return newMatrix;
    }
    
    public static int[] getInputAsVector(BufferedReader input) throws IOException {

        String inputData = input.readLine();
        String[] inputParts = inputData.split(" ");
        
        int numberOfElements = Integer.parseInt(inputParts[0]);
        int[] vector = new int[numberOfElements];
        for (int i=0; i<numberOfElements; i++) {
            vector[i] = Integer.parseInt(inputParts[i+1]);
        }
        return vector;
    }
    
    public static int[] getInputAsVector(BufferedReader input, String inputString) throws IOException {

        String[] inputParts = inputString.split(" ");
        
        int numberOfElements = Integer.parseInt(inputParts[0]);
        int[] vector = new int[numberOfElements];
        for (int i=0; i<numberOfElements; i++) {
            vector[i] = Integer.parseInt(inputParts[i+1]);
        }
        return vector;
    }
    
    
    // rounds double to specified decimal (unlike Math.round)
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    public static double sumVector(double[][] vector) {
        double sum = 0;
        for (int i=0; i<vector[0].length; i++) {
            sum += vector[0][i];
        }
        return sum;
    }
    
    public static double[][] storeColumnVectorInMatrix(double[][] matrix, double[][] columnVector, int storeInColumnNum) {
        if (columnVector[0].length != 1) {
            printMatrix(matrix);
            printMatrix(columnVector);
            shutDownProgram("Column vector dimensions incorrect in storeColumnVectorInMatrix");
        }
        
        for (int i=0; i<columnVector.length; i++) {
            matrix[i][storeInColumnNum] = columnVector[i][0];
        }
        
        return matrix;
    }
    
    public static int[][] storeColumnVectorInMatrix(int[][] matrix, int[][] columnVector, int storeInColumnNum) {
        if (columnVector[0].length != 1) {
            printMatrix(matrix);
            printMatrix(columnVector);
            shutDownProgram("Column vector dimensions incorrect in storeColumnVectorInMatrix");
        }
        
        for (int i=0; i<columnVector.length; i++) {
            matrix[i][storeInColumnNum] = columnVector[i][0];
        }
        
        return matrix;
    }
    
    public static double[][] scaleMatrix(double[][] matrix, double scalar) {
        for (int i=0; i<matrix.length; i++) {
            for (int j=0; j<matrix[0].length; j++) {
                matrix[i][j] = matrix[i][j]*scalar;
            }
        }
        return matrix;
    }
    
    public static double[][] calcNextDelta(double[][] aMatrix,  double[][] prevDeltaVector, double[][] bColumn) {
        double[][] tempMatrix = new double[aMatrix.length][aMatrix.length];
        for (int i=0; i<aMatrix.length; i++) {
            double[][] aRow = getRowFromMatrix(aMatrix, i);    
            double[][] tempVector = scaleMatrix(aRow, prevDeltaVector[i][0]);
            tempVector = transposeMatrix(elementWiseProduct(bColumn, tempVector));
            tempMatrix = storeColumnVectorInMatrix(tempMatrix, tempVector, i);
        }
        double[][] output = new double[aMatrix.length][2];
        for (int i=0; i<aMatrix.length; i++) {
            double[] evaluateArrayToMaxArray = getRowFromMatrix(tempMatrix, i)[0];
            ArrayList<Double> evaluateArrayToMaxArrayList = new ArrayList<Double>();
            for (double num : evaluateArrayToMaxArray) {
                evaluateArrayToMaxArrayList.add(num);
            }
            double maxValue = Collections.max(evaluateArrayToMaxArrayList);
            if (maxValue > 0) {
                output[i][0] = maxValue;
                output[i][1] = evaluateArrayToMaxArrayList.indexOf(maxValue);
            } else {
                output[i][0] = maxValue;
                output[i][1] = -1;
            }
        }
        return output;
        
    }
    
    public static int[][] doubleMatrixToIntMatrix(double[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix[0].length];
        for (int i=0; i<matrix.length; i++) {
            for (int j=0; j<matrix[0].length; j++) {
                Double d = new Double(matrix[i][j]);
                newMatrix[i][j] = d.intValue();
            }
        }
        return newMatrix;
    }
    

    public static void main(String args[]) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        
        // OUR INPUT
        // String line1 = "4 4 0.6 0.1 0.1 0.2 0.0 0.3 0.2 0.5 0.8 0.1 0.0 0.1 0.2 0.0 0.1 0.7";
        // String line2 = "4 4 0.6 0.2 0.1 0.1 0.1 0.4 0.1 0.4 0.0 0.0 0.7 0.3 0.0 0.0 0.1 0.9";
        // String line3 = "1 4 0.5 0.0 0.0 0.5";
        // String line4 = "4 2 0 3 1";
        
        // SAMPLE OUTPUT
        // 0 1 2 1
        
        // SAMPLE INPUT
        String line1 = "4 4 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.8 0.1 0.1 0.0";
        String line2 = "4 4 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.1 0.0 0.0 0.9";
        String line3 = "1 4 1.0 0.0 0.0 0.0";
        String line4 = "4 1 1 2 2";
        
        // SAMPLE OUTPUT
        // 0 1 2 1
        
        // Used by kattis
        double[][] aMatrix = getInputAsMatrix(stdin);
        double[][] bMatrix = getInputAsMatrix(stdin);
        double[][] piMatrix = getInputAsMatrix(stdin);
        int[] obsSequence = getInputAsVector(stdin);
        
        // Used for testing
        // double[][] aMatrix = getInputAsMatrix(stdin, line1);
        // double[][] bMatrix = getInputAsMatrix(stdin, line2);
        // double[][] piMatrix = getInputAsMatrix(stdin, line3);
        // int[] obsSequence = getInputAsVector(stdin, line4);
        
        double[][] deltaMatrix = new double[piMatrix[0].length][obsSequence.length];
        int[][] deltaMatrixIndex = new int[piMatrix[0].length][obsSequence.length];
        int[][] deltaIndex;
        double[][] deltaVector, output;
        

        // Initalize (and get first column from inputMatrix)
        deltaVector = transposeMatrix(elementWiseProduct(piMatrix,getColumnFromMatrix(bMatrix, obsSequence[0])));
        deltaMatrix = storeColumnVectorInMatrix(deltaMatrix, deltaVector, 0);
        
        // After initalize
        for (int i=1; i<obsSequence.length; i++) {
            output = calcNextDelta(aMatrix, deltaVector, getColumnFromMatrix(bMatrix, obsSequence[i]));
            deltaVector = getColumnFromMatrix(output, 0);
            deltaIndex = doubleMatrixToIntMatrix(getColumnFromMatrix(output, 1));
            deltaMatrix = storeColumnVectorInMatrix(deltaMatrix, deltaVector, i);
            deltaMatrixIndex = storeColumnVectorInMatrix(deltaMatrixIndex, deltaIndex, i);
        }
        
        
        // System.out.println("deltaMatrix");
        // printMatrix(deltaMatrix);
        // System.out.println("deltaMatrixIndex");
        // printMatrix(deltaMatrixIndex);
        // System.out.println("");
        
        
        // part 1 - get start index
        String toPrint = "";
        ArrayList<Double> tempList = new ArrayList<Double>();
        double[][] tempArray123 =  getColumnFromMatrix(deltaMatrix, deltaMatrix[0].length-1);
        for (int i=0; i<tempArray123.length; i++) {
            tempList.add(tempArray123[i][0]);
        }
        double maxValue = Collections.max(tempList);
        
        int state = tempList.indexOf(maxValue);
        int index = deltaMatrixIndex[state][3];
        toPrint += Integer.toString(state) + " ";
        
            
        // part 2 - get all other
        for (int t=deltaMatrixIndex[0].length-2; t>-1; t--) {
            state = index;
            index = deltaMatrixIndex[index][t];
            toPrint += Integer.toString(state) + " ";
        }
        toPrint = new StringBuilder(toPrint).reverse().toString().trim();
        System.out.println(toPrint);


    }

}
