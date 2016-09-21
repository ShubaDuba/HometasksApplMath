package matrix;

/**
 * Created by andrew on 21.09.16.
 */
public class DenseMatrix implements iMatrix{
    private int[][] data;

    public DenseMatrix(int row, int col) {
        data = new int[row][col];
    }

    public DenseMatrix(int[][] data) {
        this.data = data;
    }

    public iMatrix mult(iMatrix m){
        if (m instanceof DenseMatrix) {
            return mult((DenseMatrix) m);
        } else if (m instanceof SparseMatrix) {
            return this;
        } else return null;
    }

    public DenseMatrix mult (SparseMatrix m) {
        return this;
    }

    public DenseMatrix mult (DenseMatrix m) {
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

                result[row1][col2] = sum;
            }
        }

        return this;
    }
}