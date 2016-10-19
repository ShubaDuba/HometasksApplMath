package matrix;

import java.util.*;
import java.io.File;

/**
 * Created by andrew on 21.09.16.
 */
public class SparseMatrix implements iMatrix{
    private int size;
    private int[] values;
    private int[] cols;
    private int[] pointer;


    public int getSize() {
        return size;
    }

    private SparseMatrix() {}


    //sorry
    private int[] toIntArray(ArrayList<Integer> a) {
        int[] result = new int[a.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = a.get(i);
        }

        return result;
    }

    public SparseMatrix transposed() {
        class Pair {
            int value;
            int row;

            public Pair(int value, int row) {
                this.value = value;
                this.row = row;
            }
        }

        SparseMatrix result = new SparseMatrix();
        result.size = size;
        result.pointer = new int[size + 1];
        result.pointer[0] = 0;
        ArrayList<ArrayList<Pair>> colsArray = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            colsArray.add(new ArrayList<>());
        }

        for (int i = 0; i < pointer.length - 1; ++i) {
            for (int j = pointer[i]; j < pointer[i + 1]; ++j) {
                colsArray.get(cols[j]).add(new Pair(values[j], i));
            }
        }

        result.values = new int[values.length];
        result.cols = new int[cols.length];
        int current = 0;
        int colSize = 0;
        Pair tmp;
        ArrayList<Pair> col;
        for (int i = 0; i < size; ++i) {
            col = colsArray.get(i);
            colSize = col.size();
            for (int j = 0; j < colSize; ++j) {
                tmp = col.get(j);
                result.values[current] = tmp.value;
                result.cols[current++] = tmp.row;
            }

            result.pointer[i + 1] = result.pointer[i] + colSize;
        }

        return result;
    }

    public SparseMatrix(String fileName) {
        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> c = new ArrayList<>();
        ArrayList<Integer> p = new ArrayList<>();
        p.add(0);
        int currentLine = 0;
        String[] line = {};
        int nonZero = 0;
        try {
            File file = new File(fileName);
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                nonZero = 0;
                line = input.nextLine().split(" ");
                for (int i = 0; i < line.length; ++i) {
                    if (!line[i].equals("0")) {
                        a.add(Integer.parseInt(line[i]));
                        c.add(i);
                        ++nonZero;
                    }
                }

                currentLine += nonZero;
                p.add(currentLine);
            }

            values = this.toIntArray(a);
            cols = this.toIntArray(c);
            pointer = this.toIntArray(p);
            size = line.length;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public iMatrix mul(iMatrix m){
        if (m instanceof DenseMatrix) {
            return mul((DenseMatrix) m);
        } else if (m instanceof SparseMatrix) {
            return mul((SparseMatrix) m);
        } else return null;
    }

    public DenseMatrix mul (SparseMatrix m) {
        int[][] result = new int[size][m.size];
        SparseMatrix mT = m.transposed();
        int[] tmp = new int[size];
        int pos1 = 0;
        int pos2 = 0;
        int sum = 0;
        for (int i = 0; i < pointer.length - 1; ++i) {
            for (int j = pointer[i]; j < pointer[i + 1]; ++j) {
                tmp[cols[j]] = values[j];
            }

            for (int n = 0; n < size; ++n) {
                sum = 0;
                for (int l = mT.pointer[n]; l < mT.pointer[n + 1]; ++l) {
                    sum += mT.values[l] * tmp[mT.cols[l]];
                }

                result[i][n] = sum;
            }

            for (int k = 0; k < size; ++k) {
                tmp[k] = 0;
            }
        }

        return new DenseMatrix(result);
    }

    // mock
    public DenseMatrix mul (DenseMatrix m) {
        return new DenseMatrix(1, 2);
    }
}
