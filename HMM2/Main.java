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

    public static float[][] matrixMultiplier(float[][] A, float[][] B) {

        if (A[0].length != B.length) {
            System.out.println("ERROR MATRIX DIMENSIONS INCORRECT! A: " + Integer.toString(A[0].length) + " B: " + Integer.toString(B.length) + "\nSHUTTING DOWN PROGRAM");
            System.exit(0);
        }

        float[][] C = new float[A.length][B[0].length];
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

    public static void printArray(float[] array) {
        System.out.println(Arrays.toString(array));
    }
    
    public static void printArray(int[] array) {
        System.out.println(Arrays.toString(array));
    }

    public static void printMatrix(float[][] matrix) {
        System.out.println("");
        for (int i=0;i < matrix.length; i++) {
            printArray(matrix[i]);
        }
    }

    public static float[][] vectorToMatrix(String[] vector, int rows, int columns) {
        float[][] matrix = new float[rows][columns];
        int k=0;
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                matrix[i][j] = Float.parseFloat(vector[k]);
                k+=1;
            }
        }
        return matrix;
    }

    public static float[][] elementWiseProduct(float[][] vec1, float[][] vec2){
        
        /* OBS: include transpose function if vec1.length == 1. */

        /* OBS: include transpose function if vec2.length == 1. */

        float[][] resultVector = new float[vec1.length][1];

        // checks that length is equal
        if (vec1.length != vec2.length){
            System.out.println("elementWiseVectorProduct requires equal length matrices. \nSHUTTING DOWN PROGRAM");
            System.exit(0);
        }

        // for each element, multiply
        for (int i=0; i<vec1.length; i++) {
            resultVector = vec1[0][i] * vec2[0][i]

        return resultVector;

    }



    public static void printMatrixForKattis(float[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        String output = "" + Integer.toString(rows) + " " + Integer.toString(columns) + " ";
        for (int i=0; i<rows; i++) {
            for (int j=0;j<columns; j++) {
                output += Float.toString(matrix[i][j]);
                if (i+1 != rows || j+1 != columns) {
                   output += " ";
                }
            }
        }
        System.out.println(output);
    }

    public static float[][] getInputAsMatrix(BufferedReader input) throws IOException {

        String inputData = input.readLine();
        String[] inputParts = inputData.split(" ");
        
        int rows = Integer.parseInt(inputParts[0]);
        int columns = Integer.parseInt(inputParts[1]);
        String[] inputVector = Arrays.copyOfRange(inputParts, 2, inputParts.length);
        float[][] matrix = vectorToMatrix(inputVector, rows, columns);
        return matrix;
    }
    
    public static float[][] getColumnFromMatrix(float[][] matrix, int column) {
        column -= 1;
        float[][] columnVector = new float[matrix.length][1];
        for (int i=0;i<matrix.length; i++) {
            columnVector[i][0] = matrix[i][column];
        }
        return columnVector;
    }
    
    public static float[][] transposeMatrix(float[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        float[][] newMatrix = new float[columns][rows];

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

    public static void main(String args[]) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        //float[][] aMatrix = getInputAsMatrix(stdin);
        float[][] bMatrix = getInputAsMatrix(stdin);
        float[][] columnVector = getColumnFromMatrix(result, 1);
        
        float[][] piMatrix = getInputAsMatrix(stdin);
        
        float[][] result = matrixMultiplier(piMatrix, bMatrix);
        
        printMatrix(bMatrix);
        printMatrix(result);
        printMatrix(columnVector);
        //int[] vector = getInputAsVector(stdin);
        
        //printMatrix(aMatrix);
        //printMatrix(bMatrix);
        //printMatrix(columnVector);
        //printMatrix(piMatrix);
        //printArray(vector);

        //float[][] resultVector = matrixMultiplier(piMatrix, aMatrix);
        //float[][] resultVector2 = matrixMultiplier(resultVector, bMatrix);
        //printMatrix(resultVector2);
    }

}
