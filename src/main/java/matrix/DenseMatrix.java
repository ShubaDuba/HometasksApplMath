package matrix;

import java.io.File;
import java.util.Scanner;

/**
 * Created by andrew on 21.09.16.
 */
public class DenseMatrix implements iMatrix {
    protected int[][] data;

    public DenseMatrix(int row, int col) {
        data = new int[row][col];
    }

    public DenseMatrix(int[][] data) {
        this.data = data;
    }

    public DenseMatrix(String fileName) {
        File file = null;
        Scanner input = null;
        int [][] result = {};
        int currentLine = 0;
        String[] line = {};
        try {
            file = new File(fileName);
            input = new Scanner(file);
            if (input.hasNextLine()) {
                line = input.nextLine().split(" ");
                result = new int[line.length][line.length];
                for (int i = 0; i < line.length; ++i) {
                    result[currentLine][i] = Integer.parseInt(line[i]);
                }

                ++currentLine;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            while (input.hasNextLine()) {
                line = input.nextLine().split(" ");
                for (int i = 0; i < line.length; ++i) {
                    result[currentLine][i] = Integer.parseInt(line[i]);
                }

                ++currentLine;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data = result;
    }

    public iMatrix mul(iMatrix m) {
        if (m instanceof DenseMatrix) {
            return mul((DenseMatrix) m);
        } else if (m instanceof SparseMatrix) {
            return this;
        } else return null;
    }

    // mock
    public DenseMatrix mul(SparseMatrix m) {
        return new DenseMatrix(1, 2);
    }

    public DenseMatrix mul(DenseMatrix m) {
        int row1 = data.length;
        int col1 = data[0].length;
        int row2 = m.data.length;
        int col2 = m.data[0].length;

        int transM[][] = new int[col2][row2];
        for (int i = 0; i < row2; ++i) {
            for (int j = 0; j < col2; ++j) {
                transM[i][j] = m.data[j][i];
            }
        }

        int result[][] = new int[row1][col2];
        for (int i = 0; i < row1; ++i) {
            for (int j = 0; j < col2; ++j) {
                int sum = 0;
                for (int k = 0; k < col1; ++k) {
                    sum += data[i][k] * transM[j][k];
                }

                result[i][j] = sum;
            }
        }

        return this;
    }
}
