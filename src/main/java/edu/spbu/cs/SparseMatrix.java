package matrix;

import java.io.PrintWriter;
import java.util.*;
import java.io.File;

/**
 * Created by andrew on 21.09.16.
 */
public class SparseMatrix implements IMatrix {
    protected int size;
    protected double[] values;
    protected int[] cols;
    protected int[] pointer;

    private SparseMatrix() {}

    //sorry
    private int[] toIntArray(ArrayList<Integer> a) {
        int[] result = new int[a.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = a.get(i);
        }

        return result;
    }

    private double[] toDoubleArray(ArrayList<Integer> a) {
        double[] result = new double[a.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = a.get(i);
        }

        return result;
    }

    public SparseMatrix(double[] values, int[] cols, int[] pointer) {
        this.values = values;
        this.cols = cols;
        this.pointer = pointer;
        this.size = pointer.length - 1;
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

            values = this.toDoubleArray(a);
            cols = this.toIntArray(c);
            pointer = this.toIntArray(p);
            size = line.length;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public IMatrix mul(IMatrix m){
        if (m instanceof DenseMatrix) {
            return mul((DenseMatrix) m);
        } else if (m instanceof SparseMatrix) {
            return mul((SparseMatrix) m);
        } else return null;
    }

    public SparseMatrix mul (SparseMatrix m) {
        int rValuesCounter = 0;
        int arrayLengthCounter = values.length;
        double rValues[] = new double[arrayLengthCounter];
        int rCols[] = new int[arrayLengthCounter];
        int rPointer[] = new int[size + 1];

        SparseMatrix mT = m.transposed();

        double[] tmp = new double[size];
        double sum = 0;
        int[] tmpCols;
        double[] tmpValues;
        for (int i = 0; i < pointer.length - 1; ++i) {
            rPointer[i] = rValuesCounter;
            for (int j = pointer[i]; j < pointer[i + 1]; ++j) {
                tmp[cols[j]] = values[j];
            }

            for (int n = 0; n < size; ++n) {
                sum = 0;
                for (int l = mT.pointer[n]; l < mT.pointer[n + 1]; ++l) {
                    sum += mT.values[l] * tmp[mT.cols[l]];
                }

                if (sum != 0) {
                    if (rValuesCounter == arrayLengthCounter) {
                        arrayLengthCounter *= 1.3;
                        tmpValues = new double[arrayLengthCounter];
                        tmpCols = new int[arrayLengthCounter];
                        System.arraycopy(rValues, 0, tmpValues, 0, rValues.length);
                        System.arraycopy(rCols, 0, tmpCols, 0, rCols.length);
                        rValues = tmpValues;
                        rCols = tmpCols;
                    }

                    rValues[rValuesCounter] = sum;
                    rCols[rValuesCounter++] = n;
                }
            }

            for (int k = 0; k < size; ++k) {
                tmp[k] = 0;
            }
        }

        if (rValuesCounter < arrayLengthCounter) {
            tmpValues = new double[rValuesCounter];
            tmpCols = new int[rValuesCounter];
            System.arraycopy(rValues, 0, tmpValues, 0, tmpValues.length);
            System.arraycopy(rCols, 0, tmpCols, 0, tmpCols.length);
            rValues = tmpValues;
            rCols = tmpCols;
        }

        rPointer[size] = rValues.length;

        return new SparseMatrix(rValues, rCols, rPointer);
    }

    public DenseMatrix mul(DenseMatrix m) {
        double[][] result = new double[size][size];
        int row2 = m.data.length;
        int col2 = m.data[0].length;

        double mT[][] = new double[col2][row2];
        for (int i = 0; i < row2; ++i) {
            for (int j = 0; j < col2; ++j) {
                mT[i][j] = m.data[j][i];
            }
        }

        double sum = 0;
        double[] tmp = new double[size];

        for (int i = 0; i < pointer.length - 1; ++i) {
            for (int j = pointer[i]; j < pointer[i + 1]; ++j) {
                tmp[cols[j]] = values[j];
            }

            for (int n = 0; n < size; ++n) {
                sum = 0;
                for (int l = 0; l < size; ++l) {
                    sum += mT[n][l] * tmp[l];
                }

                result[i][n] = sum;
            }

            for (int k = 0; k < size; ++k) {
                tmp[k] = 0;
            }
        }

        return new DenseMatrix(result);
    }

    public SparseMatrix transposed() {
        class Pair {
            double value;
            int row;

            public Pair(double value, int row) {
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

        result.values = new double[values.length];
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

    public int getSize() {
        return size;
    }

    public double[] getValues() { return values; }

    public void toFile(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);

            double[] tmp = new double[size];
            for (int i = 0; i < pointer.length - 1; ++i) {
                for (int j = pointer[i]; j < pointer[i + 1]; ++j) {
                    tmp[cols[j]] = values[j];
                }

                for (int n = 0; n < size; ++n) {
                    writer.print(tmp[n] + " ");
                }

                writer.println();
                for (int k = 0; k < size; ++k) {
                    tmp[k] = 0;
                }
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
