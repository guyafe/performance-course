package il.co.site_building.performance_course.matrices.benchmarking;

import il.co.site_building.performance_course.matrices.ArrayBasedMatrix;
import il.co.site_building.performance_course.matrices.BlocksMatrix;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Stopwatch;

public class BenchmarkMultiplication {

  private static final double NANOS = 1E9;
  private static final int WINDOW_SIZE = 8192;
  private static final String GREEN_BOLD = "\033[1;32m";  // GREEN
  private static final String RESET = "\033[0m";

  public static void main(String... args) {
    int size = Integer.parseInt(args[0]);
    int numberOfCycles = Integer.parseInt(args[1]);
    int numberOfWarmupCycles = Integer.parseInt(args[2]);
    int blockSize = Integer.parseInt(args[3]);
    System.out.println("Starting benchmark on matrix multiplication....");
    warmup(size, numberOfWarmupCycles, blockSize);
    MatrixMultiplicationBenchmarkResult result = benchmark(size, numberOfCycles, blockSize);
    saveResult(result);
  }

  private static void saveResult(MatrixMultiplicationBenchmarkResult result) {
    saveResult("multiplication", result.multiplicationResult());
    saveResult("transpose multiplication", result.transposeMultiplicationResult());
    saveResult("blocks multiplication", result.blocksMatrixResult());
    saveResult("blocks Parallel multiplication", result.blockParallelMatrixResult());
  }

  private static void saveResult(String operation, DescriptiveStatistics multiplicationResult) {
    double averageTimeSeconds = multiplicationResult.getMean();
    double stdTimeSeconds = Math.sqrt(multiplicationResult.getVariance());
    double stdTimePercent = stdTimeSeconds / averageTimeSeconds * 100.0;
    double percentile5 = multiplicationResult.getPercentile(5);
    double percentile50 = multiplicationResult.getPercentile(50);
    double percentile95 = multiplicationResult.getPercentile(59);
    System.out.println(GREEN_BOLD + "Average time for " + operation + ": " + averageTimeSeconds + RESET);
    System.out.println("STD time for " + operation + ": " + stdTimeSeconds);
    System.out.println("STD time (percent) for " + operation + ": " + String.format("%.02f", stdTimePercent) + "%");
    System.out.println("5 percentile time for " + operation + ": " + percentile5);
    System.out.println("50 percentile time for " + operation + ": " + percentile50);
    System.out.println("95 percentile time for " + operation + ": " + percentile95);
  }

  private static MatrixMultiplicationBenchmarkResult benchmark(int size, int numberOfCycles, int blockSize) {
    DescriptiveStatistics multiplicationResult = new DescriptiveStatistics(WINDOW_SIZE);
    DescriptiveStatistics transposeMultiplicationResult = new DescriptiveStatistics(WINDOW_SIZE);
    DescriptiveStatistics blocksMatrixResult = new DescriptiveStatistics(WINDOW_SIZE);
    DescriptiveStatistics blocksParallelMatrixResult = new DescriptiveStatistics(WINDOW_SIZE);
    System.out.println("Starting benchmark cycles...");
    Random random = new Random();
    for (int cycle = 1; cycle <= numberOfCycles; cycle++) {
      System.gc();
      System.out.print("\rGenerating first random matrix for benchmark cycle " + cycle);
      ArrayBasedMatrix matrix1 = ArrayBasedMatrix.generateRandomMatrix(random, size);
      System.out.print("\rGenerating second random matrix for benchmark cycle " + cycle);
      ArrayBasedMatrix matrix2 = ArrayBasedMatrix.generateRandomMatrix(random, size);

      System.out.print("\rMultiplying matrices for cycle " + cycle);
      Stopwatch multiplyStopwatch = Stopwatch.createStarted();
      matrix1.multiply(matrix2);
      multiplyStopwatch.stop();
      multiplicationResult.addValue(multiplyStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);

      System.out.print("\rTranspose multiplying matrices for cycle " + cycle);
      Stopwatch transposeMultiplyStopwatch = Stopwatch.createStarted();
      matrix1.multiplyTranspose(matrix2);
      transposeMultiplyStopwatch.stop();
      transposeMultiplicationResult.addValue(transposeMultiplyStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);

      System.out.print("\rGenerating first random blocks matrix for benchmark cycle " + cycle);
      BlocksMatrix blocksMatrix1 = BlocksMatrix.generateRandomMatrix(random, size, blockSize);
      System.out.print("\rGenerating second random blocks matrix for benchmark cycle " + cycle);
      BlocksMatrix blocksMatrix2 = BlocksMatrix.generateRandomMatrix(random, size, blockSize);
      System.out.print("\rMultiplying blocks matrices for cycle " + cycle);
      Stopwatch blocksMultiplyStopwatch = Stopwatch.createStarted();
      blocksMatrix1.multiply(blocksMatrix2);
      blocksMultiplyStopwatch.stop();
      blocksMatrixResult.addValue(blocksMultiplyStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);

      System.out.print("\rMultiplying Parallel blocks matrices for cycle " + cycle);
      Stopwatch blocksParallelMultiplyStopwatch = Stopwatch.createStarted();
      blocksMatrix1.multiplyParallel(blocksMatrix2, 10);
      blocksParallelMultiplyStopwatch.stop();
      blocksParallelMatrixResult.addValue(blocksParallelMultiplyStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);
    }
    System.out.println();
    return new MatrixMultiplicationBenchmarkResult(multiplicationResult,
                                                   transposeMultiplicationResult,
                                                   blocksMatrixResult,
                                                   blocksParallelMatrixResult);
  }

  private static void warmup(int size, int numberOfWarmupCycles, int blockSize) {
    System.out.println("Starting warmup cycles....");
    Random random = new Random();
    for (int cycle = 1; cycle <= numberOfWarmupCycles; cycle++) {
      System.out.print("\rGenerating first random matrix for warmup cycle " + cycle);
      ArrayBasedMatrix matrix1 = ArrayBasedMatrix.generateRandomMatrix(random, size);
      System.out.print("\rGenerating second random matrix for warmup cycle " + cycle);
      ArrayBasedMatrix matrix2 = ArrayBasedMatrix.generateRandomMatrix(random, size);
      System.out.print("\rMultiplying warmup matrices for cycle " + cycle);
      matrix1.multiply(matrix2);
      System.out.print("\rTranspose warmup multiplying matrices for cycle " + cycle);
      matrix1.multiplyTranspose(matrix2);

      System.out.print("\rGenerating first blocks matrix for warmup cycle " + cycle);
      BlocksMatrix blockMatrix1 = BlocksMatrix.generateRandomMatrix(random, size, blockSize);
      System.out.print("\rGenerating second blocks matrix for warmup cycle " + cycle);
      BlocksMatrix blockMatrix2 = BlocksMatrix.generateRandomMatrix(random, size, blockSize);
      System.out.print("\rMultiplying warmup blocks matrices for cycle " + cycle);
      blockMatrix1.multiply(blockMatrix2);
      System.out.print("\rParallel Multiplying warmup blocks matrices for cycle " + cycle);
      blockMatrix1.multiplyParallel(blockMatrix2, 10);
    }
    System.out.println();
  }
}
