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

    public static void printArray(String[] array) {
        System.out.println(Arrays.toString(array));
    }

    public static void printMatrix(String[][] matrix) {
        for (int i=0;i < matrix.length; i++) {
            printArray(matrix[i]);
        }
        System.out.println("");
    }

    public static String[][] getInput(BufferedReader input) throws IOException {

        String inputData = input.readLine();
        String[] inputParts = inputData.split(" ");
        
        int rows = Integer.parseInt(inputParts[0]);
        int columns = Integer.parseInt(inputParts[1]);
        String[] inputVector = Arrays.copyOfRange(inputParts, 2, inputParts.length);
        String[][] matrix = new String[rows][columns];

        int k=0;
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                matrix[i][j] = inputVector[k];
                k+=1;
            }
        }
        return matrix;
    }

    public static void main(String args[]) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String[][] aMatrix = getInput(stdin);
        String[][] bMatrix = getInput(stdin);
        String[][] piMatrix = getInput(stdin);
        //printMatrix(aMatrix);
        //printMatrix(bMatrix);
        //printMatrix(piMatrix);
    }

}
