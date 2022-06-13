package il.co.site_building.performance_course.graph.benchmarking;

import il.co.site_building.performance_course.graph.data_structures.GraphBenchmarkStatistics;
import il.co.site_building.performance_course.graph.JGraphSimpleGraphImpl;
import il.co.site_building.performance_course.graph.NeighborsMatrixGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Stopwatch;

/**
 * Connected components benchmarking class.
 * The main method receives the following command line arguments:
 * 1) Number of vertices - int
 * 2) Load factor - double
 * 3) Number of benchmarking cycles - int
 * 4) Number of warmup cycles - int
 * 5) CSV output file name - String
 * <p>
 * Then for both graph types prints avg time, number of benchmarks, STD (by value and percent), 95%, 50%, 5% both for building and finding the components.
 */
public class ConnectedComponentsBenchmarking {

  private static final double NANOS = 1E9;
  private static final int STATISTICS_WINDOW_SIZE = 8192;
  private static final String[] CSV_RECORDS_HEADERS =
      new String[]{"Graph Name", "Average Build Time", "Build Time STD", "Build Time STD Percent", "5th Percentile Build Time", "50 Percentile Build Time", "95 Percentile Build Time", "Average Connected Component Time", "Connected Component Time STD", "Connected Component Build Time STD (Percent)", "5th Percentile Connected Component Time", "50 Percentile Connected Component Time", "95 Percentile Connected Component Time"};

  public static void main(String... args) {
    try {
      final int SEED = 0;
      int numberOfVertices = Integer.parseInt(args[0]);
      double loadFactor = Double.parseDouble(args[1]);
      int numberOfBenchmarkingCycles = Integer.parseInt(args[2]);
      int numberOfWarmupCycles = Integer.parseInt(args[3]);
      String csvFileName = args[4];
      warmup(numberOfVertices, loadFactor, numberOfWarmupCycles);
      GraphBenchmarkStatistics neighborsMatrixBenchmarkStatistics =
          benchmarkNeighborsMatrix(SEED, numberOfBenchmarkingCycles, loadFactor, numberOfVertices);
      GraphBenchmarkStatistics connectedComponentsBenchmarkStatistics =
          benchmarkJGraphMatrix(SEED, numberOfBenchmarkingCycles, loadFactor, numberOfVertices);
      saveStatistics(neighborsMatrixBenchmarkStatistics, connectedComponentsBenchmarkStatistics, csvFileName);
    } catch (Exception e) {
      e.printStackTrace();
      printUsage();
    }
  }

  private static void saveStatistics(GraphBenchmarkStatistics neighborsMatrixBenchmarkStatistics,
                                     GraphBenchmarkStatistics connectedComponentsBenchmarkStatistics,
                                     String csvFileName) throws IOException {
    FileWriter csvFile = new FileWriter(csvFileName);
    CSVFormat csvFormat =
        CSVFormat.Builder.create(CSVFormat.EXCEL).setHeader(CSV_RECORDS_HEADERS).setSkipHeaderRecord(true).build();
    try (CSVPrinter csvPrinter = new CSVPrinter(csvFile, csvFormat)) {
      csvPrinter.printRecord((Object[]) CSV_RECORDS_HEADERS);
      saveStatistics(neighborsMatrixBenchmarkStatistics, "Neighbors Matrix", csvPrinter);
      saveStatistics(connectedComponentsBenchmarkStatistics, "JGraph", csvPrinter);
    }
    csvFile.close();
  }

  private static void saveStatistics(GraphBenchmarkStatistics benchmarkStatistics, String graphName,
                                     CSVPrinter csvPrinter) throws IOException {
    double neighborsMatrixBuildAvg = benchmarkStatistics.buildStatistics().getMean();
    double neighborsMatrixBuildStd = Math.sqrt(benchmarkStatistics.buildStatistics().getVariance());
    double neighborsMatrix5Percentile = benchmarkStatistics.buildStatistics().getPercentile(5);
    double neighborsMatrix50Percentile = benchmarkStatistics.buildStatistics().getPercentile(50);
    double neighborsMatrix95Percentile = benchmarkStatistics.buildStatistics().getPercentile(95);
    double neighborsMatrixConnectedComponentsAvg = benchmarkStatistics.connectedComponents().getMean();
    double neighborsMatrixConnectedComponentsStd = Math.sqrt(benchmarkStatistics.connectedComponents().getVariance());
    double neighborsMatrixConnectedComponents5Percentile = benchmarkStatistics.connectedComponents().getPercentile(5);
    double neighborsMatrixConnectedComponents50Percentile = benchmarkStatistics.connectedComponents().getPercentile(50);
    double neighborsMatrixConnectedComponents95Percentile = benchmarkStatistics.connectedComponents().getPercentile(95);
    System.out.println(graphName + " average build time: " + neighborsMatrixBuildAvg);
    System.out.println(graphName + " std build time: " + neighborsMatrixBuildStd);
    System.out.println(
        graphName + " std build time (percent) : " + neighborsMatrixBuildStd / neighborsMatrixBuildAvg * 100.0 + "%");
    System.out.println(graphName + " 5 percentile build time: " + neighborsMatrix5Percentile);
    System.out.println(graphName + " 50 percentile build time: " + neighborsMatrix50Percentile);
    System.out.println(graphName + " 95 percentile build time: " + neighborsMatrix95Percentile);
    System.out.println(graphName + " average connected components time: " + neighborsMatrixConnectedComponentsAvg);
    System.out.println(graphName + " std build time: " + neighborsMatrixConnectedComponentsStd);
    System.out.println(
        graphName + " std connected components time (percent) : " +
            neighborsMatrixConnectedComponentsStd / neighborsMatrixConnectedComponentsAvg * 100.0 + "%");
    System.out.println(
        graphName + " 5 percentile connected components time: " + neighborsMatrixConnectedComponents5Percentile);
    System.out.println(
        graphName + " 50 percentile connected components time: " + neighborsMatrixConnectedComponents50Percentile);
    System.out.println(
        graphName + " 95 percentile connected components time: " + neighborsMatrixConnectedComponents95Percentile);
    csvPrinter.printRecord(graphName,
                           neighborsMatrixBuildAvg,
                           neighborsMatrixBuildStd,
                           neighborsMatrixBuildStd / neighborsMatrixBuildAvg * 100.0,
                           neighborsMatrix5Percentile,
                           neighborsMatrix50Percentile,
                           neighborsMatrix95Percentile,
                           neighborsMatrixConnectedComponentsAvg,
                           neighborsMatrixConnectedComponentsStd,
                           neighborsMatrixConnectedComponentsStd / neighborsMatrixConnectedComponentsAvg * 100.0,
                           neighborsMatrixConnectedComponents5Percentile,
                           neighborsMatrixConnectedComponents50Percentile,
                           neighborsMatrixConnectedComponents95Percentile);
  }

  private static GraphBenchmarkStatistics benchmarkNeighborsMatrix(int seed,
                                                                   int numberOfBenchmarkingCycles,
                                                                   double loadFactor,
                                                                   int numberOfVertices) {
    Random random = new Random(seed);
    DescriptiveStatistics buildStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    DescriptiveStatistics connectedComponentsStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    System.out.println("Starting benchmark for neighbors matrix graph");
    for (int cycle = 1; cycle <= numberOfBenchmarkingCycles; cycle++) {
      System.gc(); //Cleanup
      System.out.print("\rNeighbors matrix build cycle number " + cycle);
      Stopwatch buildStopwatch = Stopwatch.createStarted();
      NeighborsMatrixGraph graph = NeighborsMatrixGraph.createRandomGraph(random, numberOfVertices, loadFactor);
      buildStopwatch.stop();
      System.out.print("\rNeighbors matrix connected components cycle number " + cycle);
      double buildTimeSeconds = buildStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS;
      buildStatistics.addValue(buildTimeSeconds);
      Stopwatch connectedComponentsStopwatch = Stopwatch.createStarted();
      graph.createConnectedComponents();
      connectedComponentsStopwatch.stop();
      double connectedComponentsTime = connectedComponentsStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS;
      connectedComponentsStatistics.addValue(connectedComponentsTime);
    }
    System.out.println();
    return new GraphBenchmarkStatistics(buildStatistics, connectedComponentsStatistics);
  }

  private static GraphBenchmarkStatistics benchmarkJGraphMatrix(int seed,
                                                                int numberOfBenchmarkingCycles,
                                                                double loadFactor,
                                                                int numberOfVertices) {
    Random random = new Random(seed);
    DescriptiveStatistics buildStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    DescriptiveStatistics connectedComponentsStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    System.out.println("Starting benchmark for JGraph graph");
    for (int cycle = 1; cycle <= numberOfBenchmarkingCycles; cycle++) {
      System.gc(); //Cleanup
      System.out.print("\rJGraph build cycle number " + cycle);
      Stopwatch buildStopwatch = Stopwatch.createStarted();
      JGraphSimpleGraphImpl graph = JGraphSimpleGraphImpl.createRandomGraph(random, numberOfVertices, loadFactor);
      buildStopwatch.stop();
      System.out.print("\rJGraph connected components cycle number " + cycle);
      double buildTimeSeconds = buildStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS;
      buildStatistics.addValue(buildTimeSeconds);
      Stopwatch connectedComponentsStopwatch = Stopwatch.createStarted();
      graph.createConnectedComponents();
      connectedComponentsStopwatch.stop();
      double connectedComponentsTime = connectedComponentsStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS;
      connectedComponentsStatistics.addValue(connectedComponentsTime);
    }
    System.out.println();
    return new GraphBenchmarkStatistics(buildStatistics, connectedComponentsStatistics);
  }

  private static void warmup(int numberOfVertices, double loadFactor, int numberOfWarmupCycles) {
    Random random = new Random();
    System.out.println("Starting warmup cycles for connected components graph...");
    for (int cycle = 1; cycle <= numberOfWarmupCycles; cycle++) {
      System.out.print("\rNeighbors Matrix Graph warmup cycle " + cycle + " build graph...");
      NeighborsMatrixGraph graph = NeighborsMatrixGraph.createRandomGraph(random, numberOfVertices, loadFactor);
      System.out.print("\rNeighbors Matrix Graph warmup cycle " + cycle + " connected components...");
      graph.createConnectedComponents();
    }
    System.out.println();
    for (int cycle = 1; cycle <= numberOfWarmupCycles; cycle++) {
      System.out.print("\rJGraph Simple Graph warmup cycle " + cycle + " build graph...");
      JGraphSimpleGraphImpl graph = JGraphSimpleGraphImpl.createRandomGraph(random, numberOfVertices, loadFactor);
      System.out.print("\rJGraph Simple Graph warmup cycle " + cycle + " connected components...");
      graph.createConnectedComponents();
    }
    System.out.println();
    System.out.println("Finished Warmup");
  }

  private static void printUsage() {
    System.err.println(
        "Command line arguments sage: <number of vertices> <load factor> <number of benchmarking cycles> <number of warmup cycles> <CSV file name>");
  }
}
