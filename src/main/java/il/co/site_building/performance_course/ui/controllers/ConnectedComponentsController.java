package il.co.site_building.performance_course.ui.controllers;

import il.co.site_building.performance_course.graph.JGraphSimpleGraphImpl;
import il.co.site_building.performance_course.graph.NeighborsMatrixGraph;
import il.co.site_building.performance_course.ui.PercentageUpdater;
import il.co.site_building.ui_controls.IntegerTextFieldControl;
import il.co.site_building.ui_controls.NumericDoubleFieldControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectedComponentsController {
  private final Logger logger = LogManager.getRootLogger();

  @FXML
  public IntegerTextFieldControl numberOfVerticesTextField;
  @FXML
  public AnchorPane connectedComponentsPane;
  @FXML
  public IntegerTextFieldControl numberOfWarmupCyclesTextField;
  @FXML
  public IntegerTextFieldControl numberOfBenchmarkCyclesTextField;
  @FXML
  public NumericDoubleFieldControl loadFactorControl;
  @FXML
  public ProgressBar connectedComponentsProgressBar;
  @FXML
  public CheckBox shouldRunJGraphAlgorithm;
  @FXML
  public CheckBox shouldRunNeighborsMatrixAlgorithm;
  @FXML
  public Button runSimulationButton;
  @FXML
  public Label statusLabel;
  @FXML
  public Label percentageLabel;

  private final ExecutorService workersThreadPool = Executors.newCachedThreadPool();

  public void runSimulation() {
    logger.info("Running simulation....");
    int numberOfVertices = Integer.parseInt(numberOfVerticesTextField.getText());
    int numberOfWarmupCycles = Integer.parseInt(numberOfWarmupCyclesTextField.getText());
    int numberOfBenchmarkCycles = Integer.parseInt(numberOfBenchmarkCyclesTextField.getText());
    double loadFactor = Double.parseDouble(loadFactorControl.getValue());
    boolean shouldRunJGraph = shouldRunJGraphAlgorithm.isSelected();
    boolean shouldRunNeighborsMatrix = shouldRunNeighborsMatrixAlgorithm.isSelected();
    logger.info("Number of warmup cycles: {}", numberOfWarmupCycles);
    logger.info("Number of benchmark cycles: {}", numberOfBenchmarkCycles);
    logger.info("Load Factor: {}", loadFactor);
    logger.info("Should run JGraph algorithm: {}", shouldRunJGraph);
    logger.info("Should run Neighbors Matrix algorithm: {}", shouldRunNeighborsMatrix);
    workersThreadPool.submit(() -> runSimulation(numberOfVertices,
                                                 loadFactor,
                                                 numberOfWarmupCycles,
                                                 numberOfBenchmarkCycles,
                                                 shouldRunJGraph,
                                                 shouldRunNeighborsMatrix));
  }

  private void runSimulation(int numberOfVertices,
                             double loadFactor,
                             int numberOfWarmupCycles,
                             int numberOfBenchmarkCycles,
                             boolean shouldRunJGraph,
                             boolean shouldRunNeighborsMatrix) {
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
    PercentageUpdater percentageUpdater =
        new PercentageUpdater(percentageIncrease, connectedComponentsProgressBar, percentageLabel);
    for (int cycle = 1; cycle <= numberOfWarmupCycles; cycle++) {
      runWarmupCycle(cycle,
                     numberOfVertices,
                     loadFactor,
                     shouldRunJGraph,
                     shouldRunNeighborsMatrix,
                     percentageUpdater);
    }
    for (int cycle = 1; cycle <= numberOfBenchmarkCycles; cycle++) {
      rnBenchmarkCycle(cycle,
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
    if (shouldRunJGraph) {
      Platform.runLater(() -> statusLabel.setText("Running warmup cycle " + cycle + " on JGraph implementation"));
      JGraphSimpleGraphImpl graph = JGraphSimpleGraphImpl.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
    if (shouldRunNeighborsMatrix) {
      Platform.runLater(() -> statusLabel.setText(
          "Running warmup cycle " + cycle + " on Neighbors Matrix implementation"));
      NeighborsMatrixGraph graph = NeighborsMatrixGraph.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
  }

  private void rnBenchmarkCycle(int cycle,
                                int numberOfVertices,
                                double loadFactor,
                                boolean shouldRunJGraph,
                                boolean shouldRunNeighborsMatrix,
                                PercentageUpdater percentageUpdater) {
    Random random = new Random();
    if (shouldRunJGraph) {
      Platform.runLater(() -> statusLabel.setText("Running benchmark cycle " + cycle + " on JGraph implementation"));
      JGraphSimpleGraphImpl graph = JGraphSimpleGraphImpl.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
    if (shouldRunNeighborsMatrix) {
      Platform.runLater(() -> statusLabel.setText(
          "Running benchmark cycle " + cycle + " on Neighbors Matrix implementation"));
      NeighborsMatrixGraph graph = NeighborsMatrixGraph.createRandomGraph(random, numberOfVertices, loadFactor);
      graph.createConnectedComponents();
      percentageUpdater.increase();
    }
  }
}
