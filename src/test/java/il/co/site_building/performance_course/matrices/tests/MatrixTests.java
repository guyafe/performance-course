package il.co.site_building.performance_course.matrices.tests;

import il.co.site_building.performance_course.matrices.ArrayBasedMatrix;
import il.co.site_building.performance_course.matrices.BlocksMatrix;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MatrixTests {

  @Test
  public void testMatrixMultiplication(){
    int rows1 = 10;
    int cols1 = 20;
    int rows2 = 20;
    int cols2 = 30;
    ArrayBasedMatrix matrix1 = new ArrayBasedMatrix(rows1, cols1);
    ArrayBasedMatrix matrix2 = new ArrayBasedMatrix(rows2, cols2);
    RealMatrix referenceMatrix1 = MatrixUtils.createRealMatrix(rows1, cols1);
    RealMatrix referenceMatrix2 = MatrixUtils.createRealMatrix(rows2, cols2);
    Random random = new Random();
    for (int row = 0; row < rows1; row++) {
      for (int col = 0; col < cols1; col++) {
        double value = random.nextDouble();
        matrix1.set(row, col, value);
        referenceMatrix1.setEntry(row, col, value);
      }
    }
    for (int row = 0; row < rows2; row++) {
      for (int col = 0; col < cols2; col++) {
        double value = random.nextDouble();
        matrix2.set(row, col, value);
        referenceMatrix2.setEntry(row, col, value);
      }
    }
    ArrayBasedMatrix result = matrix1.multiply(matrix2);
    RealMatrix referenceResult = referenceMatrix1.multiply(referenceMatrix2);
    verifyMatricesEqual(result, referenceResult);
  }

  @Test
  public void testBlocksMatrixMultiplication(){
    int blockSize = 3;
    int rows1 = 10;
    int cols1 = 20;
    int rows2 = 20;
    int cols2 = 30;
    BlocksMatrix matrix1 = new BlocksMatrix(rows1, cols1, blockSize);
    BlocksMatrix matrix2 = new BlocksMatrix(rows2, cols2, blockSize);
    RealMatrix referenceMatrix1 = MatrixUtils.createRealMatrix(rows1, cols1);
    RealMatrix referenceMatrix2 = MatrixUtils.createRealMatrix(rows2, cols2);
    Random random = new Random();
    for (int row = 0; row < rows1; row++) {
      for (int col = 0; col < cols1; col++) {
        double value = random.nextDouble();
        matrix1.set(row, col, value);
        referenceMatrix1.setEntry(row, col, value);
      }
    }
    for (int row = 0; row < rows2; row++) {
      for (int col = 0; col < cols2; col++) {
        double value = random.nextDouble();
        matrix2.set(row, col, value);
        referenceMatrix2.setEntry(row, col, value);
      }
    }
    BlocksMatrix result = matrix1.multiply(matrix2);
    RealMatrix referenceResult = referenceMatrix1.multiply(referenceMatrix2);
    verifyMatricesEqual(result, referenceResult);
  }

  private void verifyMatricesEqual(BlocksMatrix result, RealMatrix referenceResult) {
    for (int row = 0; row < result.rows(); row++) {
      for (int col = 0; col < result.columns(); col++) {
        Assertions.assertEquals(result.get(row, col), referenceResult.getEntry(row, col));
      }
    }
  }

  @Test
  public void testTransposedMatrixMultiplication(){
    int rows1 = 10;
    int cols1 = 20;
    int rows2 = 20;
    int cols2 = 30;
    ArrayBasedMatrix matrix1 = new ArrayBasedMatrix(rows1, cols1);
    ArrayBasedMatrix matrix2 = new ArrayBasedMatrix(rows2, cols2);
    RealMatrix referenceMatrix1 = MatrixUtils.createRealMatrix(rows1, cols1);
    RealMatrix referenceMatrix2 = MatrixUtils.createRealMatrix(rows2, cols2);
    Random random = new Random();
    for (int row = 0; row < rows1; row++) {
      for (int col = 0; col < cols1; col++) {
        double value = random.nextDouble();
        matrix1.set(row, col, value);
        referenceMatrix1.setEntry(row, col, value);
      }
    }
    for (int row = 0; row < rows2; row++) {
      for (int col = 0; col < cols2; col++) {
        double value = random.nextDouble();
        matrix2.set(row, col, value);
        referenceMatrix2.setEntry(row, col, value);
      }
    }
    ArrayBasedMatrix result = matrix1.multiplyTranspose(matrix2);
    RealMatrix referenceResult = referenceMatrix1.multiply(referenceMatrix2);
    verifyMatricesEqual(result, referenceResult);
  }

  private void verifyMatricesEqual(ArrayBasedMatrix result, RealMatrix referenceResult) {
    for (int row = 0; row < result.rows(); row++) {
      for (int col = 0; col < result.columns(); col++) {
        Assertions.assertEquals(result.get(row, col), referenceResult.getEntry(row, col));
      }
    }
  }
}
