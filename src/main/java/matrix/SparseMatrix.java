package matrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrew on 21.09.16.
 */
public class SparseMatrix implements iMatrix{
    SparseMatrix() {}

    private class Key {
        final int x;
        final int y;
        public Key(int x, int y) { this.x = x; this.y = y; }
    }

    private Map<Key, Integer> hashmap = new HashMap<Key, Integer>();

    public void add(int row, int col, int value) {
        hashmap.put(new Key(row, col), value);
    }

    public int get(int row, int col) {
        return hashmap.get(new Key(row, col));
    }

    public iMatrix mult(iMatrix m){
        if (m instanceof DenseMatrix) {
            return mult((DenseMatrix) m);
        } else if (m instanceof SparseMatrix) {
            return this;
        } else return null;
    }

    public DenseMatrix mult (SparseMatrix m) {
        return new DenseMatrix(1, 2);
    }

    public DenseMatrix mult (DenseMatrix m) {
        return new DenseMatrix(1, 2);
    }
}
