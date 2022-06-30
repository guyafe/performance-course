package il.co.site_building.performance_course.ui.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import il.co.site_building.performance_course.graph.data_structures.ConnectedComponentsBenchmarkStatistics;
import il.co.site_building.performance_course.ui.runners.ConnectedComponentsResultsUpdater;
import il.co.site_building.performance_course.ui.runners.ConnectedComponentsRunner;
import il.co.site_building.ui_controls.IntegerTextFieldControl;
import il.co.site_building.ui_controls.NumericDoubleFieldControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class PerformanceCourseUiController implements Initializable {
  private final Logger logger = LogManager.getRootLogger();
  private static final boolean DISABLE = true;
  private static final boolean ENABLE = false;

  @FXML public IntegerTextFieldControl numberOfVerticesTextField;
  @FXML public AnchorPane connectedComponentsPane;
  @FXML public IntegerTextFieldControl numberOfWarmupCyclesTextField;
  @FXML public IntegerTextFieldControl numberOfBenchmarkCyclesTextField;
  @FXML public NumericDoubleFieldControl loadFactorControl;
  @FXML public ProgressBar connectedComponentsProgressBar;
  @FXML public CheckBox shouldRunJGraphAlgorithm;
  @FXML public CheckBox shouldRunNeighborsMatrixAlgorithm;
  @FXML public Button runSimulationButton;
  @FXML public Label statusLabel;
  @FXML public Label percentageLabel;
  @FXML public TableView<ConnectedComponentsResultRecord> resultsTable;
  @FXML public TableColumn<String, String> headersColumn;
  @FXML public TableColumn<String, Double> jgraphBuildColumn;
  @FXML public TableColumn<String, Double> jgraphCalculationColumn;
  @FXML public TableColumn<String, Double> neighborsMatrixBuildColumn;
  @FXML public TableColumn<String, Double> neighborsMatrixCalculationColumn;
  @FXML public Button stopSimulationButton;
  private final ExecutorService workersThreadPool = Executors.newCachedThreadPool();

  @Override public void initialize(URL location, ResourceBundle resources) {
    headersColumn.setCellValueFactory(new PropertyValueFactory<>("header"));
    jgraphBuildColumn.setCellValueFactory(new PropertyValueFactory<>("jgraphBuildResult"));
    jgraphCalculationColumn.setCellValueFactory(new PropertyValueFactory<>("jgraphCalculationResult"));
    neighborsMatrixBuildColumn.setCellValueFactory(new PropertyValueFactory<>("neighborsMatrixBuildResult"));
    neighborsMatrixCalculationColumn.setCellValueFactory(new PropertyValueFactory<>("neighborsMatrixCalculationResult"));
  }


  public void runConnectedComponentsSimulation() {
    int numberOfVertices = Integer.parseInt(numberOfVerticesTextField.getText());
    int numberOfWarmupCycles = Integer.parseInt(numberOfWarmupCyclesTextField.getText());
    int numberOfBenchmarkCycles = Integer.parseInt(numberOfBenchmarkCyclesTextField.getText());
    double loadFactor = Double.parseDouble(loadFactorControl.getValue());
    boolean shouldRunJGraph = shouldRunJGraphAlgorithm.isSelected();
    boolean shouldRunNeighborsMatrix = shouldRunNeighborsMatrixAlgorithm.isSelected();
    ConnectedComponentsRunner runner = new ConnectedComponentsRunner(numberOfVertices,
                                                                     numberOfWarmupCycles,
                                                                     numberOfBenchmarkCycles,
                                                                     loadFactor,
                                                                     shouldRunJGraph,
                                                                     shouldRunNeighborsMatrix,
                                                                     this);
    stopSimulationButton.onMouseClickedProperty().set(event -> runner.stop());
    Future<ConnectedComponentsBenchmarkStatistics> connectedComponentsFuture = workersThreadPool.submit(runner);
    workersThreadPool.submit(new ConnectedComponentsResultsUpdater(connectedComponentsFuture, this));
  }

  public void updateStatus(String status) {
    Platform.runLater(() -> statusLabel.setText(status));
  }

  public void updateProgressBar(double progress) {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    Platform.runLater(() -> {
      connectedComponentsProgressBar.setProgress(progress / 100.0);
      percentageLabel.setText(df.format(progress) + "%");
    });
  }

  public void reportStart() {
    Platform.runLater(() -> {
      resultsTable.getItems().clear();
      runSimulationButton.setDisable(DISABLE);
      stopSimulationButton.setDisable(ENABLE);
    });
  }

  public void reportStopping() {
    Platform.runLater(() -> {
      runSimulationButton.setDisable(DISABLE);
      stopSimulationButton.setDisable(DISABLE);
    });
  }

  public void reportStop() {
    Platform.runLater(() -> {
      runSimulationButton.setDisable(ENABLE);
      stopSimulationButton.setDisable(DISABLE);
    });
  }

  public void updateTable(ConnectedComponentsBenchmarkStatistics statistics) {
    Platform.runLater(() -> {
      resultsTable.getItems()
                  .add(new ConnectedComponentsResultRecord("Mean",
                                                           statistics.jgraphBuildStatistics().getMean(),
                                                           statistics.jgraphCalculationStatistics().getMean(),
                                                           statistics.neighborsMatrixBuildStatistics().getMean(),
                                                           statistics.neighborsMatrixCalculationStatistics()
                                                                     .getMean()));
      resultsTable.getItems()
                  .add(new ConnectedComponentsResultRecord("Max",
                                                           statistics.jgraphBuildStatistics().getMax(),
                                                           statistics.jgraphCalculationStatistics().getMax(),
                                                           statistics.neighborsMatrixBuildStatistics().getMax(),
                                                           statistics.neighborsMatrixCalculationStatistics()
                                                                     .getMax()));
      resultsTable.getItems()
                  .add(new ConnectedComponentsResultRecord("Min",
                                                           statistics.jgraphBuildStatistics().getMin(),
                                                           statistics.jgraphCalculationStatistics().getMin(),
                                                           statistics.neighborsMatrixBuildStatistics().getMin(),
                                                           statistics.neighborsMatrixCalculationStatistics()
                                                                     .getMin()));
      resultsTable.getItems()
                  .add(new ConnectedComponentsResultRecord("50 percentile",
                                                           statistics.jgraphBuildStatistics().getPercentile(50),
                                                           statistics.jgraphCalculationStatistics().getPercentile(50),
                                                           statistics.neighborsMatrixBuildStatistics()
                                                                     .getPercentile(50),
                                                           statistics.neighborsMatrixCalculationStatistics()
                                                                     .getPercentile(50)));
      resultsTable.getItems()
                  .add(new ConnectedComponentsResultRecord("95 percentile",
                                                           statistics.jgraphBuildStatistics().getPercentile(95),
                                                           statistics.jgraphCalculationStatistics().getPercentile(95),
                                                           statistics.neighborsMatrixBuildStatistics()
                                                                     .getPercentile(95),
                                                           statistics.neighborsMatrixCalculationStatistics()
                                                                     .getPercentile(95)));
    });
  }

}
