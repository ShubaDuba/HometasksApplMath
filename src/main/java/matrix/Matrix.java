package matrix;

import java.io.IOException;
import edu.spbu.cs.MatrixGenerator;

/**
 * Created by andrew on 21.09.16.
 */
public class Matrix {
    public static void main(String args[]) throws IOException {
        MatrixGenerator mg1 = new MatrixGenerator(1, 1, "m1.txt", 2000);
        MatrixGenerator mg2 = new MatrixGenerator(2, 1, "m2.txt", 2000);
        mg1.generate();
        mg2.generate();
        SparseMatrix m1 = new SparseMatrix("m1.txt");
        SparseMatrix m2 = new SparseMatrix("m2.txt");
        long start = System.currentTimeMillis();
        DenseMatrix mlt = m1.mul(m2);
        System.out.println("Sparse Matrix time: " +(System.currentTimeMillis() - start));
    }
}
