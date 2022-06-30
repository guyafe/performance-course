package il.co.site_building.performance_course.ui.controllers;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.Future;

import il.co.site_building.performance_course.graph.data_structures.ConnectedComponentsBenchmarkStatistics;
import il.co.site_building.performance_course.ui.runners.ConnectedComponentsResultsUpdater;
import il.co.site_building.performance_course.ui.runners.ConnectedComponentsRunner;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConnectedComponentsController {
  private static final boolean DISABLE = true;
  private static final boolean ENABLE = false;
  private static final String BUILD_TIME = "Build Time";
  private static final String CALCULATION_TIME = "Calculation Time";
  private static final String JGRAPH = "JGraph";
  private static final String NEIGHBORS_MATRIX_GRAPH = "Neighbors Matrix Graph";

  private final PerformanceCourseUiController parentController;

  public ConnectedComponentsController(PerformanceCourseUiController parentController) {
    this.parentController = parentController;
  }

  public void initialize() {
    parentController.headersColumn.setCellValueFactory(new PropertyValueFactory<>("header"));
    parentController.jgraphBuildColumn.setCellValueFactory(new PropertyValueFactory<>("jgraphBuildResult"));
    parentController.jgraphCalculationColumn.setCellValueFactory(new PropertyValueFactory<>("jgraphCalculationResult"));
    parentController.neighborsMatrixBuildColumn.setCellValueFactory(new PropertyValueFactory<>(
        "neighborsMatrixBuildResult"));
    parentController.neighborsMatrixCalculationColumn.setCellValueFactory(new PropertyValueFactory<>(
        "neighborsMatrixCalculationResult"));
    parentController.algorithmsCategoryAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(JGRAPH,
                                                                                                          NEIGHBORS_MATRIX_GRAPH)));
  }


  public void runConnectedComponentsSimulation() {
    int numberOfVertices = Integer.parseInt(parentController.numberOfVerticesTextField.getText());
    int numberOfWarmupCycles = Integer.parseInt(parentController.numberOfWarmupCyclesTextField.getText());
    int numberOfBenchmarkCycles = Integer.parseInt(parentController.numberOfBenchmarkCyclesTextField.getText());
    double loadFactor = Double.parseDouble(parentController.loadFactorControl.getValue());
    boolean shouldRunJGraph = parentController.shouldRunJGraphAlgorithm.isSelected();
    boolean shouldRunNeighborsMatrix = parentController.shouldRunNeighborsMatrixAlgorithm.isSelected();
    ConnectedComponentsRunner runner = new ConnectedComponentsRunner(numberOfVertices,
                                                                     numberOfWarmupCycles,
                                                                     numberOfBenchmarkCycles,
                                                                     loadFactor,
                                                                     shouldRunJGraph,
                                                                     shouldRunNeighborsMatrix,
                                                                     this);
    parentController.stopSimulationButton.onMouseClickedProperty().set(event -> runner.stop());
    Future<ConnectedComponentsBenchmarkStatistics> connectedComponentsFuture =
        parentController.workersThreadPool.submit(runner);
    parentController.workersThreadPool.submit(new ConnectedComponentsResultsUpdater(connectedComponentsFuture, this));
  }

  public void updateStatus(String status) {
    Platform.runLater(() -> parentController.statusLabel.setText(status));
  }

  public void updateProgressBar(double progress) {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    Platform.runLater(() -> {
      parentController.connectedComponentsProgressBar.setProgress(progress / 100.0);
      parentController.percentageLabel.setText(df.format(progress) + "%");
    });
  }

  public void reportStart() {
    Platform.runLater(() -> {
      parentController.resultsTable.getItems().clear();
      parentController.resultsChart.getData().clear();
      parentController.runSimulationButton.setDisable(DISABLE);
      parentController.stopSimulationButton.setDisable(ENABLE);
    });
  }

  public void reportStopping() {
    Platform.runLater(() -> {
      parentController.runSimulationButton.setDisable(DISABLE);
      parentController.stopSimulationButton.setDisable(DISABLE);
    });
  }

  public void reportStop() {
    Platform.runLater(() -> {
      parentController.runSimulationButton.setDisable(ENABLE);
      parentController.stopSimulationButton.setDisable(DISABLE);
    });
  }

  public void updateUiResults(ConnectedComponentsBenchmarkStatistics statistics) {
    updateTable(statistics);
    updateChart(statistics);
  }

  private void updateChart(ConnectedComponentsBenchmarkStatistics statistics) {
    XYChart.Series<String, Double> buildTimSeries = new XYChart.Series<>();
    buildTimSeries.getData().add(new XYChart.Data<>(JGRAPH, statistics.jgraphBuildStatistics().getMean()));
    buildTimSeries.getData()
                  .add(new XYChart.Data<>(NEIGHBORS_MATRIX_GRAPH,
                                          statistics.neighborsMatrixBuildStatistics().getMean()));
    buildTimSeries.setName(BUILD_TIME);
    XYChart.Series<String, Double> calculationTimeSeries = new XYChart.Series<>();
    calculationTimeSeries.getData()
                         .add(new XYChart.Data<>(JGRAPH, statistics.jgraphCalculationStatistics().getMean()));
    calculationTimeSeries.getData()
                         .add(new XYChart.Data<>(NEIGHBORS_MATRIX_GRAPH,
                                                 statistics.neighborsMatrixCalculationStatistics().getMean()));
    calculationTimeSeries.setName(CALCULATION_TIME);
    Platform.runLater(() -> {
      parentController.resultsChart.getData().add(buildTimSeries);
      parentController.resultsChart.getData().add(calculationTimeSeries);
    });
  }

  private void updateTable(ConnectedComponentsBenchmarkStatistics statistics) {
    Platform.runLater(() -> {
      parentController.resultsTable.getItems()
                                   .add(new ConnectedComponentsResultRecord("Mean",
                                                                            statistics.jgraphBuildStatistics()
                                                                                      .getMean(),
                                                                            statistics.jgraphCalculationStatistics()
                                                                                      .getMean(),
                                                                            statistics.neighborsMatrixBuildStatistics()
                                                                                      .getMean(),
                                                                            statistics.neighborsMatrixCalculationStatistics()
                                                                                      .getMean()));
      parentController.resultsTable.getItems()
                                   .add(new ConnectedComponentsResultRecord("Max",
                                                                            statistics.jgraphBuildStatistics().getMax(),
                                                                            statistics.jgraphCalculationStatistics()
                                                                                      .getMax(),
                                                                            statistics.neighborsMatrixBuildStatistics()
                                                                                      .getMax(),
                                                                            statistics.neighborsMatrixCalculationStatistics()
                                                                                      .getMax()));
      parentController.resultsTable.getItems()
                                   .add(new ConnectedComponentsResultRecord("Min",
                                                                            statistics.jgraphBuildStatistics().getMin(),
                                                                            statistics.jgraphCalculationStatistics()
                                                                                      .getMin(),
                                                                            statistics.neighborsMatrixBuildStatistics()
                                                                                      .getMin(),
                                                                            statistics.neighborsMatrixCalculationStatistics()
                                                                                      .getMin()));
      parentController.resultsTable.getItems()
                                   .add(new ConnectedComponentsResultRecord("50 percentile",
                                                                            statistics.jgraphBuildStatistics()
                                                                                      .getPercentile(50),
                                                                            statistics.jgraphCalculationStatistics()
                                                                                      .getPercentile(50),
                                                                            statistics.neighborsMatrixBuildStatistics()
                                                                                      .getPercentile(50),
                                                                            statistics.neighborsMatrixCalculationStatistics()
                                                                                      .getPercentile(50)));
      parentController.resultsTable.getItems()
                                   .add(new ConnectedComponentsResultRecord("95 percentile",
                                                                            statistics.jgraphBuildStatistics()
                                                                                      .getPercentile(95),
                                                                            statistics.jgraphCalculationStatistics()
                                                                                      .getPercentile(95),
                                                                            statistics.neighborsMatrixBuildStatistics()
                                                                                      .getPercentile(95),
                                                                            statistics.neighborsMatrixCalculationStatistics()
                                                                                      .getPercentile(95)));
    });
  }

}
