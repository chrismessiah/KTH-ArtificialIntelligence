// https://kth.kattis.com/problems/kth.ai.hmm2

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class Main {

    // SAMPLE INPUT
    // 4 4 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.8 0.1 0.1 0.0
    // 4 4 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.1 0.0 0.0 0.9
    // 1 4 1.0 0.0 0.0 0.0
    // 8 0 1 2 3 0 1 2 3

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
            System.out.println("elementWiseVectorProduct requires equal length matrices. \nSHUTTING DOWN PROGRAM");
            System.exit(0);
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

    public static void main(String args[]) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        double[][] aMatrix = getInputAsMatrix(stdin, "4 4 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.8 0.1 0.1 0.0");
        double[][] bMatrix = getInputAsMatrix(stdin, "4 4 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.1 0.0 0.0 0.9");
        double[][] piMatrix = getInputAsMatrix(stdin, "1 4 1.0 0.0 0.0 0.0");
        int[] obsSequence = getInputAsVector(stdin, "8 0 1 2 3 0 1 2 3");

        // get first column from inputMatrix
        double[][] alpha = elementWiseProduct(piMatrix,getColumnFromMatrix(bMatrix, obsSequence[0]));
        for (int i=1; i<obsSequence.length; i++) {
            alpha = elementWiseProduct(matrixMultiplier(alpha, aMatrix), getColumnFromMatrix(bMatrix, obsSequence[i]));
        }
        
        System.out.println(alpha[0][obsSequence[obsSequence.length-1]]);


    }

}
