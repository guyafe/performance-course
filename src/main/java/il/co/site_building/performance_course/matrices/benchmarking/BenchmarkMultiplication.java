package il.co.site_building.performance_course.matrices.benchmarking;

import il.co.site_building.performance_course.matrices.ArrayBasedMatrix;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Stopwatch;

public class BenchmarkMultiplication {

  private static final double NANOS = 1E9;
  private static final int WINDOW_SIZE = 8192;

  public static void main(String ... args){
    int size = Integer.parseInt(args[0]);
    int numberOfCycles = Integer.parseInt(args[1]);
    int numberOfWarmupCycles = Integer.parseInt(args[2]);
    System.out.println("Starting benchmark on matrix multiplication....");
    warmup(size, numberOfWarmupCycles);
    MatrixMultiplicationBenchmarkResult result = benchmark(size, numberOfCycles);
    saveResult(result);
  }

  private static void saveResult(MatrixMultiplicationBenchmarkResult result) {
    saveResult("multiplication", result.multiplicationResult());
    saveResult("transpose multiplication", result.transposeMultiplicationResult());
  }

  private static void saveResult(String operation, DescriptiveStatistics multiplicationResult) {
    double averageTimeSeconds = multiplicationResult.getMean();
    double stdTimeSeconds = Math.sqrt(multiplicationResult.getVariance());
    double stdTimePercent = stdTimeSeconds /averageTimeSeconds * 100.0;
    double percentile5 = multiplicationResult.getPercentile(5);
    double percentile50 = multiplicationResult.getPercentile(50);
    double percentile95 = multiplicationResult.getPercentile(59);
    System.out.println("Average time for " + operation + ": " + averageTimeSeconds);
    System.out.println("STD time for " + operation + ": " + stdTimeSeconds);
    System.out.println("STD time (percent) for " + operation + ": " + stdTimePercent);
    System.out.println("5 percentile time for " + operation + ": " + percentile5);
    System.out.println("50 percentile time for " + operation + ": " + percentile50);
    System.out.println("95 percentile time for " + operation + ": " + percentile95);
  }

  private static MatrixMultiplicationBenchmarkResult benchmark(int size, int numberOfCycles) {
    DescriptiveStatistics multiplicationResult = new DescriptiveStatistics(WINDOW_SIZE);
    DescriptiveStatistics transposeMultiplicationResult = new DescriptiveStatistics(WINDOW_SIZE);
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
    }
    System.out.println();
    return new MatrixMultiplicationBenchmarkResult(multiplicationResult, transposeMultiplicationResult);
  }

  private static void warmup(int size, int numberOfWarmupCycles) {
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
    }
    System.out.println();
  }
}
