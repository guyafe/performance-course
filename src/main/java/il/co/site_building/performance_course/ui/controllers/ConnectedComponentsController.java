package il.co.site_building.performance_course.ui.controllers;

import il.co.site_building.performance_course.ui.runners.ConnectedComponentsRunner;
import il.co.site_building.ui_controls.IntegerTextFieldControl;
import il.co.site_building.ui_controls.NumericDoubleFieldControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectedComponentsController {
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
  @FXML public TableView resultsTable;
  @FXML public TableColumn headersColumn;
  @FXML public TableColumn jgraphColumn;
  @FXML public TableColumn neighborsMatrixColumn;
  @FXML public Button stopSimulationButton;
  private final ExecutorService workersThreadPool = Executors.newCachedThreadPool();

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
                                                                     shouldRunNeighborsMatrix, this);
    stopSimulationButton.onMouseClickedProperty().set(event -> runner.stop());
    Future<Void> connectedComponentsFuture = workersThreadPool.submit(runner);
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


}
