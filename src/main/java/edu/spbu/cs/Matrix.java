package edu.spbu.cs;

/**
 * Created by andrew on 21.09.16.
 */
public interface IMatrix {
    IMatrix mul(IMatrix m);
    void toFile(String filename);
}
