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
    

    public static double[][] alpha_forward_pass(int[] obsSequence, double[][] piMatrix, double[][] aMatrix, double[][] bMatrix){

        // Initialize the first alpha as pi_i * bi(O=obsSequence[0])
        double[][] alphaMatrix = elementWiseProduct(piMatrix,getColumnFromMatrix(bMatrix, obsSequence[0]));
        
        // fill in the rest of the alphas (skipping the first step i=0)
        for (int i=1; i<obsSequence.length; i++) {
            alphaMatrix = elementWiseProduct(matrixMultiplier(alphaMatrix, aMatrix), getColumnFromMatrix(bMatrix, obsSequence[i]));
        }

        return alphaMatrix;
    }



    // Never tested
    public static double[][] beta_backward_pass(int[] obsSequence, double[][] aMatrix, double[][] bMatrix){

        // the betaMatrix is [N x M]
        // N is number of states
        // M is number of observations
        int N = aMatrix[0].length;
        int M = obsSequence.length;
        double[][] betaMatrix = new double[N][M];

        // Initialize by setting the last beta-column to 1's
        for(int i=0; i<N; i++){
            betaMatrix[i][M-1] = 1;
        }

        double[][] tempMatrix;
        double[][] bi_vector;
        double[][] beta1;
        double[][] beta_tplus1;
        

        // going backwards through the observations from the last
        for (int t=M-2; t<0; --t) {
    
            bi_vector = getColumnFromMatrix(bMatrix, obsSequence[t+1]);
            beta_tplus1 = getColumnFromMatrix(betaMatrix, t+1);
            
            tempMatrix = matrixMultiplier(aMatrix,bi_vector);

            // how should these indexes be filled out to fill whole column?
            beta1 = elementWiseProduct(tempMatrix, beta_tplus1);

            for (int j=0; j<N; j++){
                betaMatrix[j][t] = beta1[j][0];
            }

        }
        return betaMatrix;
    }


    public static void reEstimate_lambda (int[] obsSequence, double[][] aMatrix, double[][] bMatrix, double[][] alphaMatrix, double[][] betaMatrix){
        int N = aMatrix[0].length;
        int M = obsSequence.length;
        double[][] gammaMatrix = new double[N][M];
        double gamma_denominator;

        // OBS: should this be M or M-1?
        double[][] last_alpha_column = getColumnFromMatrix(alphaMatrix,M-1); // like in HMM2
        gamma_denominator = sumVector(last_alpha_column);


        // estimate gamma-matrix as described in problem 2 of stamp. 

        // for each state
        for (int i=0;i<N;i++){
            // for all timesteps
            for (int t=0;t<M;t++){
                // Divide by P(observations | model), as in HMM2
                gammaMatrix[i][t] = (alphaMatrix[i][t] * betaMatrix[i][t]) / gamma_denominator;
            }
        }

        double[][] diGammaMatrix = new double[N][M];
        return;

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





    public static void main(String args[]) throws IOException { 
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        
        double[][] aMatrix;
        double[][] bMatrix;
        double[][] piMatrix;
        int[] obsSequence;
        
        double[][] aAnswer;
        double[][] bAnswer;

        
        String obsString = readTextfromFile("obs10k.in");
        
        aMatrix = getInputAsMatrix(stdin, "3 3 0.7 0.05 0.25 0.1 0.8 0.1 0.2 0.3 0.5");
        bMatrix = getInputAsMatrix(stdin, "3 4 0.7 0.2 0.1 0 0.1 0.4 0.3 0.2 0 0.1 0.2 0.7");
        piMatrix = getInputAsMatrix(stdin, "1 2 1 0 0");
        obsSequence = getInputAsVector(stdin, obsString);
    
        //aAnswer = getInputAsMatrix(stdin, "4 4 0.545455 0.454545 0.0 0.0 0.0 0.506173 0.493827 0.0 0.0 0.0 0.504132 0.495868 0.478088 0.0 0.0 0.521912");
        //bAnswer = getInputAsMatrix(stdin, "4 4 1.0 0.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 0.0 1.0");






        piMatrix = transposeMatrix(piMatrix);
        HMM hmm = new HMM(aMatrix,bMatrix,piMatrix);

        hmm.baum_welch(obsSequence);
        
        
        //printMatrix(hmm.A);
        //printMatrix(hmm.B);

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
