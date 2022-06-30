package il.co.site_building.performance_course.ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import il.co.site_building.ui_controls.IntegerTextFieldControl;
import il.co.site_building.ui_controls.NumericDoubleFieldControl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class PerformanceCourseUiController implements Initializable {
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
  public final ExecutorService workersThreadPool = Executors.newCachedThreadPool();
  private final ConnectedComponentsController connectedComponentsController;

  public PerformanceCourseUiController() {
    connectedComponentsController = new ConnectedComponentsController(this);
  }

  @Override public void initialize(URL location, ResourceBundle resources) {
    connectedComponentsController.initialize();
  }


  public void runConnectedComponentsSimulation() {
    connectedComponentsController.runConnectedComponentsSimulation();
  }

}
