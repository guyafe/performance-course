package il.co.site_building.performance_course.ui.runners;

import java.util.Random;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import il.co.site_building.performance_course.graph.JGraphSimpleGraphImpl;
import il.co.site_building.performance_course.graph.NeighborsMatrixGraph;
import il.co.site_building.performance_course.ui.PercentageUpdater;
import il.co.site_building.performance_course.ui.controllers.ConnectedComponentsController;

public class ConnectedComponentsRunner implements Callable<Void> {
  private final Logger logger = LogManager.getRootLogger();
  private final int numberOfVertices;
  private final int numberOfWarmupCycles;
  private final int numberOfBenchmarkCycles;
  private final double loadFactor;
  private final boolean shouldRunJGraph;
  private final boolean shouldRunNeighborsMatrix;
  private final ConnectedComponentsController controller;
  private boolean shouldRun = false;

  public ConnectedComponentsRunner(int numberOfVertices,
                                   int numberOfWarmupCycles,
                                   int numberOfBenchmarkCycles,
                                   double loadFactor,
                                   boolean shouldRunJGraph,
                                   boolean shouldRunNeighborsMatrix,
                                   ConnectedComponentsController controller) {
    this.numberOfVertices = numberOfVertices;
    this.numberOfWarmupCycles = numberOfWarmupCycles;
    this.numberOfBenchmarkCycles = numberOfBenchmarkCycles;
    this.loadFactor = loadFactor;
    this.shouldRunJGraph = shouldRunJGraph;
    this.shouldRunNeighborsMatrix = shouldRunNeighborsMatrix;
    this.controller = controller;
  }

  @Override public Void call() {
    controller.reportStart();
    logger.info("Running simulation....");
    logger.info("Number of warmup cycles: {}", numberOfWarmupCycles);
    logger.info("Number of benchmark cycles: {}", numberOfBenchmarkCycles);
    logger.info("Load Factor: {}", loadFactor);
    logger.info("Should run JGraph algorithm: {}", shouldRunJGraph);
    logger.info("Should run Neighbors Matrix algorithm: {}", shouldRunNeighborsMatrix);
    runConnectedComponentsSimulation();
    controller.reportStop();
    return null;
  }

  private void runConnectedComponentsSimulation() {
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
      return;
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
    for (int cycle = 1; cycle <= numberOfBenchmarkCycles && shouldRun; cycle++) {
      runBenchmarkCycle(cycle,
                       numberOfVertices,
                       loadFactor,
                       shouldRunJGraph,
                       shouldRunNeighborsMatrix,
                       percentageUpdater);
    }
    percentageUpdater.finish();
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
                                PercentageUpdater percentageUpdater) {
    Random random = new Random();
    if (shouldRunJGraph && shouldRun) {
      controller.updateStatus("Running benchmark cycle " + cycle + " on JGraph implementation");
      JGraphSimpleGraphImpl graph = JGraphSimpleGraphImpl.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
    if (shouldRunNeighborsMatrix && shouldRun) {
      controller.updateStatus("Running benchmark cycle " + cycle + " on Neighbors Matrix implementation");
      NeighborsMatrixGraph graph = NeighborsMatrixGraph.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
  }

  public void stop(){
    controller.reportStopping();
    shouldRun = false;
  }
}
