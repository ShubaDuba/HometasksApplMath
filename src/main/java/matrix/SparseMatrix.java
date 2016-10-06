package matrix;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by andrew on 21.09.16.
 */
public class SparseMatrix implements iMatrix{
    SparseMatrix() {}

    private int col;
    private int row;

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    protected class Key {
        public int x;
        public int y;
        public Key(int x, int y) { this.x = x; this.y = y; }
    }

    protected Map<Key, Integer> hashmap;

    public void add(int row, int col, int value) {
        this.col = col;
        this.row = row;
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
        int result[][] = new int[row][m.data[0].length];

        Iterator iter = hashmap.entrySet().iterator();
        Key currentKey;
        Map.Entry pair;
        while (iter.hasNext()) {
            pair = (Map.Entry)iter.next();
            currentKey = (Key)pair.getKey();
            for (int i = 0; i < m.data[currentKey.y].length; ++i) {
                result[currentKey.x][i] += (int)pair.getValue() * m.data[currentKey.y][i];
            }
        }

        return new DenseMatrix(result);
    }
}
