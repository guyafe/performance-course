package il.co.site_building.performance_course.ui.controllers;

import il.co.site_building.ui_controls.IntegerTextFieldControl;
import il.co.site_building.ui_controls.NumericDoubleFieldControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectedComponentsController {
  private final Logger logger = LogManager.getRootLogger();

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
  public CheckBox shouldRunEvilNeighborsMatrixAlgorithm;
  @FXML
  public Button runSimulationButton;

  public void runSimulation(MouseEvent mouseEvent) {
    logger.info("Running simulation....");
    logger.info("Number of warmup cycles: {}", Integer.parseInt(numberOfWarmupCyclesTextField.getText()));
    logger.info("Number of benchmark cycles: {}", Integer.parseInt(numberOfBenchmarkCyclesTextField.getText()));
    logger.info("Load Factor: {}", Double.parseDouble(loadFactorControl.getValue()));
  }
}
