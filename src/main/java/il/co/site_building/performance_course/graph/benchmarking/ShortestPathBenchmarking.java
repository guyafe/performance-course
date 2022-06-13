package il.co.site_building.performance_course.graph.benchmarking;

import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraph;
import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraphImpl;
import il.co.site_building.performance_course.graph.UndirectedWeightedNeighborsMatrixGraphEvil;
import il.co.site_building.performance_course.graph.data_structures.ShortesPathResult;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Stopwatch;

/**
 * Shortest path benchmarking class.
 * The main method receives the following command line arguments:
 * 1) Number of vertices - int
 * 2) Load factor - double
 * 3) Number of benchmarking cycles - int
 * 4) Number of warmup cycles - int
 * 5) CSV output file name - String
 * <p>
 * Then for both graph types prints avg time, number of benchmarks, STD (by value and percent), 95%, 50%, 5% both for finding the shortest path from first vertex (0) to the last ont (number of vertices - 1);.
 */
public class ShortestPathBenchmarking {

  private static final double NANOS = 1E9;
  private static final int STATISTICS_WINDOW_SIZE = 8192;
  private static final int SEED = 0;

  public static void main(String... args) {
    int numberOfVertices = Integer.parseInt(args[0]);
    double loadFactor = Double.parseDouble(args[1]);
    int numberOfBenchmarkingCycles = Integer.parseInt(args[2]);
    int numberOfWarmupCycles = Integer.parseInt(args[3]);
    String csvFileName = args[4];
    warmup(numberOfVertices, loadFactor, numberOfWarmupCycles);
    ShortesPathResult resultStatistics =
        benchmarkShortestPath(numberOfVertices, loadFactor, numberOfBenchmarkingCycles);
    saveStatistics(resultStatistics);
  }

  private static void saveStatistics(ShortesPathResult resultStatistics) {
    saveShortestPathStatistics("Shortest Path ", resultStatistics.shortestPathResult());
    saveShortestPathStatistics("Shortest Path Evil ", resultStatistics.shortestPathEvilResult());
  }

  private static void saveShortestPathStatistics(String graphName, DescriptiveStatistics shortestPathResult) {
    double shortestPathAvg = shortestPathResult.getMean();
    double shortestPathStd = Math.sqrt(shortestPathResult.getVariance());
    double shortestPath5Percentile = shortestPathResult.getPercentile(5);
    double shortestPath50Percentile = shortestPathResult.getPercentile(50);
    double shortestPath95Percentile = shortestPathResult.getPercentile(95);
    System.out.println(graphName + " average shortest path time: " + shortestPathAvg);
    System.out.println(graphName + " std shortest path time: " + shortestPathStd);
    System.out.println(
        graphName + " std build time (percent) : " + shortestPathStd / shortestPathAvg * 100.0 + "%");
    System.out.println(graphName + " 5 percentile build time: " + shortestPath5Percentile);
    System.out.println(graphName + " 50 percentile build time: " + shortestPath50Percentile);
    System.out.println(graphName + " 95 percentile build time: " + shortestPath95Percentile);
  }

  private static ShortesPathResult benchmarkShortestPath(int numberOfVertices,
                                                         double loadFactor,
                                                         int numberOfBenchmarkingCycles) {
    DescriptiveStatistics shortestPathStatistics =
        benchmarkShortestPathGood(numberOfVertices, loadFactor, numberOfBenchmarkingCycles);
    DescriptiveStatistics shortestPathEvilStatistics =
        benchmarkShortestPathEvil(numberOfVertices, loadFactor, numberOfBenchmarkingCycles);
    return new ShortesPathResult(shortestPathStatistics, shortestPathEvilStatistics);
  }

  private static DescriptiveStatistics benchmarkShortestPathEvil(int numberOfVertices,
                                                                 double loadFactor,
                                                                 int numberOfBenchmarkingCycles) {
    Random random = new Random(SEED);
    System.out.println("Starting benchmarking on shortest path graph...");
    DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    for (int cycle = 1; cycle <= numberOfBenchmarkingCycles; cycle++) {
      UndirectedWeightedNeighborsMatrixGraph graph =
          UndirectedWeightedNeighborsMatrixGraphEvil.generateRandomGraph(random, numberOfVertices, loadFactor);
      System.gc();
      System.out.print("\rRunning shortest path evil cycle " + cycle + "...");
      Stopwatch stopwatch = Stopwatch.createStarted();
      graph.findShortestPath(0, numberOfVertices - 1);
      stopwatch.stop();
      double buildTimeSeconds = stopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS;
      descriptiveStatistics.addValue(buildTimeSeconds);
    }
    System.out.println();
    return descriptiveStatistics;
  }

  private static DescriptiveStatistics benchmarkShortestPathGood(int numberOfVertices,
                                                                 double loadFactor,
                                                                 int numberOfBenchmarkingCycles) {
    Random random = new Random(SEED);
    System.out.println("Starting benchmarking on shortest path graph...");
    DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    for (int cycle = 1; cycle <= numberOfBenchmarkingCycles; cycle++) {
      UndirectedWeightedNeighborsMatrixGraph graph =
          UndirectedWeightedNeighborsMatrixGraphImpl.generateRandomGraph(random, numberOfVertices, loadFactor);
      System.gc();
      System.out.print("\rRunning shortest path cycle " + cycle + "...");
      Stopwatch stopwatch = Stopwatch.createStarted();
      graph.findShortestPath(0, numberOfVertices - 1);
      stopwatch.stop();
      double buildTimeSeconds = stopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS;
      descriptiveStatistics.addValue(buildTimeSeconds);
    }
    System.out.println();
    return descriptiveStatistics;
  }

  private static void warmup(int numberOfVertices, double loadFactor, int numberOfWarmupCycles) {
    Random random = new Random();
    System.out.println("Staring warmup for shortest path benchmarking...");
    for (int cycle = 1; cycle <= numberOfWarmupCycles; cycle++) {
      System.out.print("\rGraph warmup cycle " + cycle + "....");
      UndirectedWeightedNeighborsMatrixGraph graph =
          UndirectedWeightedNeighborsMatrixGraphImpl.generateRandomGraph(random, numberOfVertices, loadFactor);
      graph.findShortestPath(0, numberOfVertices - 1);
    }
    System.out.println();
    System.out.println("Staring warmup for evil shortest path benchmarking...");
    for (int cycle = 1; cycle <= numberOfWarmupCycles; cycle++) {
      System.out.print("\rEvil Graph warmup cycle " + cycle + "....");
      UndirectedWeightedNeighborsMatrixGraph graph =
          UndirectedWeightedNeighborsMatrixGraphEvil.generateRandomGraph(random, numberOfVertices, loadFactor);
      graph.findShortestPath(0, numberOfVertices - 1);
    }
    System.out.println();
  }
}
