// Problem:
// https://kth.kattis.com/problems/kth.ai.hmm4
// 
// Course:
// Artificial Intelligence
//
// Authors:
// Amund Vedal
// Christian Abdelmassih

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.lang.Math;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    
    public static void printArray(String[] array) {
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
    

    public static String readTextfromFile(String filename) {
        String output = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            output = br.readLine();
        } catch (IOException e) {
            System.out.println("Some error occured in readTextfromFile");
        }
        return output;
    }


    public static int[] getSubsetVector(int[] array, int num) {
        return Arrays.copyOfRange(array, 1, num+1);
    }


    public static void main(String args[]) throws IOException { 
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        
        double[][] aMatrix;
        double[][] bMatrix;
        double[][] piMatrix;
        int[] obsSequence;
        
        double[][] aAnswer;
        double[][] bAnswer;

        int iterations = 1000;
        String filename = "obs10k.in";
        int amountOfObservationsToUse = 3500;
        
        aMatrix = getInputAsMatrix(stdin, "3 3 0.54 0.26 0.20 0.19 0.53 0.28 0.22 0.18 0.6");
        bMatrix = getInputAsMatrix(stdin, "3 4 0.5 0.2 0.11 0.19 0.22 0.28 0.23 0.27 0.19 0.21 0.15 0.45");
        piMatrix = getInputAsMatrix(stdin, "1 3 0.3 0.2 0.5");
        obsSequence = getSubsetVector(getInputAsVector(stdin, readTextfromFile(filename)), amountOfObservationsToUse);
        
        HMM hmm = new HMM(aMatrix, bMatrix, piMatrix, iterations);
        hmm.baum_welch(obsSequence);

        double[][] A_iter = hmm.A;
        double[][] B_iter = hmm.B;

        
        for (int i=0;i<A_iter[0].length;i++){
            for (int j=0;j<A_iter[0].length;j++){
                A_iter[i][j] = round(A_iter[i][j],7);
            }
        }

        for (int t=0;t<B_iter[0].length;t++){
            for (int i=0;i<transposeMatrix(B_iter)[0].length;i++){
                B_iter[i][t] = round(B_iter[i][t],7);
            }
        }
        
        printMatrixForKattis(A_iter);
        
        printMatrixForKattis(B_iter);


    


    // Initialize the first alpha as pi_i * bi(O)
    //double[][] alpha = alpha_forward_pass(obsSequence);
    //double[][] beta = beta_backward_pass(obsSequence, aMatrix);

    
    //double output = sumVector(alpha);
    //System.out.println(round(output,6));


    }

}
