package matrix;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by andrew on 21.09.16.
 */
public class DenseMatrix implements IMatrix {
    protected double[][] data;

    public DenseMatrix(int row, int col) {
        data = new double[row][col];
    }

    public DenseMatrix(double[][] data) {
        this.data = data;
    }

    public DenseMatrix(String fileName) {
        File file = null;
        Scanner input = null;
        double [][] result = {};
        int currentLine = 0;
        String[] line = {};
        try {
            file = new File(fileName);
            input = new Scanner(file);
            if (input.hasNextLine()) {
                line = input.nextLine().split(" ");
                result = new double[line.length][line.length];
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

    public IMatrix mul(IMatrix m) {
        if (m instanceof DenseMatrix) {
            return mul((DenseMatrix) m);
        } else if (m instanceof SparseMatrix) {
            return mul((SparseMatrix) m);
        } else return null;
    }


    public DenseMatrix mul(SparseMatrix m) {
        int size = data.length;
        double result[][] = new double[size][size];
        double sum = 0;

        SparseMatrix mT = m.transposed();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                sum = 0;
                for (int l = mT.pointer[j]; l < mT.pointer[j + 1]; ++l) {
                    sum += mT.values[l] * data[i][mT.cols[l]];
                }

                result[i][j] = sum;
            }
        }

        return new DenseMatrix(result);
    }

    public DenseMatrix mul(DenseMatrix m) {
        int row1 = data.length;
        int col1 = data[0].length;
        int row2 = m.data.length;
        int col2 = m.data[0].length;

        double mT[][] = new double[col2][row2];
        for (int i = 0; i < row2; ++i) {
            for (int j = 0; j < col2; ++j) {
                mT[i][j] = m.data[j][i];
            }
        }

        double result[][] = new double[row1][col2];
        for (int i = 0; i < row1; ++i) {
            for (int j = 0; j < col2; ++j) {
                double sum = 0;
                for (int k = 0; k < col1; ++k) {
                    sum += data[i][k] * mT[j][k];
                }

                result[i][j] = sum;
            }
        }

        return new DenseMatrix(result);
    }

    public double[][] getData() {
        return data;
    }

    public void toFile(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            int n = data.length;
            int m = data[0].length;
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < m; ++j) {
                    writer.print(data[i][j] + " ");
                }

                writer.println();
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
