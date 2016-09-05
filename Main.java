import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class Main {

    // SAMPLE INPUT
    // 4 4 0.2 0.5 0.3 0.0 0.1 0.4 0.4 0.1 0.2 0.0 0.4 0.4 0.2 0.3 0.0 0.5
    // 4 3 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.2 0.6 0.2
    // 1 4 0.0 0.0 0.0 1.0

    // SAMPLE OUTPUT
    // 1 3 0.3 0.6 0.1

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

    public static float[][] getInput(BufferedReader input) throws IOException {

        String inputData = input.readLine();
        String[] inputParts = inputData.split(" ");
        
        int rows = Integer.parseInt(inputParts[0]);
        int columns = Integer.parseInt(inputParts[1]);
        String[] inputVector = Arrays.copyOfRange(inputParts, 2, inputParts.length);
        float[][] matrix = vectorToMatrix(inputVector, rows, columns);
        return matrix;
    }

    public static void main(String args[]) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        float[][] aMatrix = getInput(stdin);
        float[][] bMatrix = getInput(stdin);
        float[][] piMatrix = getInput(stdin);

        float[][] resultVector = matrixMultiplier(piMatrix, aMatrix);
        float[][] resultVector2 = matrixMultiplier(resultVector, bMatrix);
        //printMatrix(resultVector2);
        printMatrixForKattis(resultVector2);
    }

}
