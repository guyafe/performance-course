package il.co.site_building.performance_course.ui.runners;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Stopwatch;

import il.co.site_building.performance_course.graph.JGraphSimpleGraphImpl;
import il.co.site_building.performance_course.graph.NeighborsMatrixGraph;
import il.co.site_building.performance_course.graph.data_structures.ConnectedComponentsBenchmarkStatistics;
import il.co.site_building.performance_course.ui.PercentageUpdater;
import il.co.site_building.performance_course.ui.controllers.PerformanceCourseUiController;

public class ConnectedComponentsRunner implements Callable<ConnectedComponentsBenchmarkStatistics> {

  private static final int STATISTICS_WINDOW_SIZE = 8192;
  private static final double NANOS = 1E9;
  private final Logger logger = LogManager.getRootLogger();
  private final int numberOfVertices;
  private final int numberOfWarmupCycles;
  private final int numberOfBenchmarkCycles;
  private final double loadFactor;
  private final boolean shouldRunJGraph;
  private final boolean shouldRunNeighborsMatrix;
  private final PerformanceCourseUiController controller;
  private boolean shouldRun = false;

  public ConnectedComponentsRunner(int numberOfVertices,
                                   int numberOfWarmupCycles,
                                   int numberOfBenchmarkCycles,
                                   double loadFactor,
                                   boolean shouldRunJGraph,
                                   boolean shouldRunNeighborsMatrix,
                                   PerformanceCourseUiController controller) {
    this.numberOfVertices = numberOfVertices;
    this.numberOfWarmupCycles = numberOfWarmupCycles;
    this.numberOfBenchmarkCycles = numberOfBenchmarkCycles;
    this.loadFactor = loadFactor;
    this.shouldRunJGraph = shouldRunJGraph;
    this.shouldRunNeighborsMatrix = shouldRunNeighborsMatrix;
    this.controller = controller;
  }

  @Override public ConnectedComponentsBenchmarkStatistics call() {
    controller.reportStart();
    logger.info("Running simulation....");
    logger.info("Number of warmup cycles: {}", numberOfWarmupCycles);
    logger.info("Number of benchmark cycles: {}", numberOfBenchmarkCycles);
    logger.info("Load Factor: {}", loadFactor);
    logger.info("Should run JGraph algorithm: {}", shouldRunJGraph);
    logger.info("Should run Neighbors Matrix algorithm: {}", shouldRunNeighborsMatrix);
    ConnectedComponentsBenchmarkStatistics statistics = runConnectedComponentsSimulation();
    controller.reportStop();
    return statistics;
  }

  private ConnectedComponentsBenchmarkStatistics runConnectedComponentsSimulation() {
    shouldRun = true;
    int numberOfAlgorithms = 0;
    if (shouldRunJGraph) {
      numberOfAlgorithms++;
    }
    if (shouldRunNeighborsMatrix) {
      numberOfAlgorithms++;
    }
    if (numberOfAlgorithms == 0) {
      logger.info("No algorithms selected.");
      return null;
    }
    int totalAlgorithmsRuns = (numberOfWarmupCycles + numberOfBenchmarkCycles) * numberOfAlgorithms;
    double percentageIncrease = 100.0 / totalAlgorithmsRuns;
    PercentageUpdater percentageUpdater = new PercentageUpdater(percentageIncrease, controller);

    for (int cycle = 1; cycle <= numberOfWarmupCycles && shouldRun; cycle++) {
      runWarmupCycle(cycle,
                     numberOfVertices,
                     loadFactor,
                     shouldRunJGraph,
                     shouldRunNeighborsMatrix,
                     percentageUpdater);
    }
    DescriptiveStatistics jgraphBuildStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    DescriptiveStatistics jgraphCalculationStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    DescriptiveStatistics neighborsMatrixBuildStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    DescriptiveStatistics neighborsMatrixCalculationStatistics = new DescriptiveStatistics(STATISTICS_WINDOW_SIZE);
    ConnectedComponentsBenchmarkStatistics statistics =
        new ConnectedComponentsBenchmarkStatistics(jgraphBuildStatistics,
                                                   jgraphCalculationStatistics,
                                                   neighborsMatrixBuildStatistics,
                                                   neighborsMatrixCalculationStatistics);
    for (int cycle = 1; cycle <= numberOfBenchmarkCycles && shouldRun; cycle++) {
      runBenchmarkCycle(cycle,
                        numberOfVertices,
                        loadFactor,
                        shouldRunJGraph,
                        shouldRunNeighborsMatrix,
                        percentageUpdater,
                        statistics);
    }
    percentageUpdater.finish();
    if (shouldRun) {
      return statistics;
    } else {
      return null;
    }
  }

  private void runWarmupCycle(int cycle,
                              int numberOfVertices,
                              double loadFactor,
                              boolean shouldRunJGraph,
                              boolean shouldRunNeighborsMatrix,
                              PercentageUpdater percentageUpdater) {
    Random random = new Random();
    if (shouldRunJGraph && shouldRun) {
      controller.updateStatus("Running warmup cycle " + cycle + " on JGraph implementation");
      JGraphSimpleGraphImpl graph = JGraphSimpleGraphImpl.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
    if (shouldRunNeighborsMatrix && shouldRun) {
      controller.updateStatus("Running warmup cycle " + cycle + " on Neighbors Matrix implementation");
      NeighborsMatrixGraph graph = NeighborsMatrixGraph.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
  }

  private void runBenchmarkCycle(int cycle,
                                 int numberOfVertices,
                                 double loadFactor,
                                 boolean shouldRunJGraph,
                                 boolean shouldRunNeighborsMatrix,
                                 PercentageUpdater percentageUpdater,
                                 ConnectedComponentsBenchmarkStatistics statistics) {
    Random random = new Random();

    if (shouldRunJGraph && shouldRun) {
      controller.updateStatus("Running benchmark cycle " + cycle + " on JGraph implementation");
      Stopwatch buildStopwatch = Stopwatch.createStarted();
      JGraphSimpleGraphImpl graph = JGraphSimpleGraphImpl.createRandomGraph(random, numberOfVertices, loadFactor);
      buildStopwatch.stop();
      statistics.jgraphBuildStatistics().addValue(buildStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);
      Stopwatch calculationStopwatch = Stopwatch.createStarted();
      graph.createConnectedComponents();
      calculationStopwatch.stop();
      statistics.jgraphCalculationStatistics().addValue(calculationStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);
      percentageUpdater.increase();
    }
    if (shouldRunNeighborsMatrix && shouldRun) {
      controller.updateStatus("Running benchmark cycle " + cycle + " on Neighbors Matrix implementation");
      Stopwatch buildStopwatch = Stopwatch.createStarted();
      NeighborsMatrixGraph graph = NeighborsMatrixGraph.createRandomGraph(random, numberOfVertices, loadFactor);
      buildStopwatch.stop();
      statistics.neighborsMatrixBuildStatistics().addValue(buildStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);
      Stopwatch calculationStopwatch = Stopwatch.createStarted();
      graph.createConnectedComponents();
      calculationStopwatch.stop();
      statistics.neighborsMatrixCalculationStatistics()
                .addValue(calculationStopwatch.elapsed(TimeUnit.NANOSECONDS) / NANOS);
      percentageUpdater.increase();
    }
  }

  public void stop() {
    controller.reportStopping();
    shouldRun = false;
  }
}
