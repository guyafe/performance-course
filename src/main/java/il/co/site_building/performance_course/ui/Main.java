package il.co.site_building.performance_course.ui;

import java.io.PrintStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Main entry point for the UI.
 * Allows demonstrating all algorithms and examples in the course.
 */
public class Main extends Application {

  private static final String STAGE_TITLE = "Java Performance Course Examples and Demo Tool";
  private static final Logger logger = LogManager.getRootLogger();
  private static final String LOGGING_TEXT_AREA_NAME = "#loggingArea";

  public static void main(String... args) {
    logger.info("Starting performance examples tool");
    launch(args);
  }

  @Override public void start(Stage primaryStage) throws Exception {
    String fxmlName = "il/co/site_building/performance_course/main-scene.fxml";
    URL resource = Main.class.getClassLoader().getResource(fxmlName);
    if (resource == null) {
      logger.error("Unable to load FXML resource: {}", () -> fxmlName);
      return;
    }
    Parent root = FXMLLoader.load(resource);
    Scene primaryScene = new Scene(root);
    primaryStage.setTitle(STAGE_TITLE);
    primaryStage.setScene(primaryScene);
    primaryStage.show();
    setLoggingToTextArea(primaryScene);
  }

  private void setLoggingToTextArea(Scene primaryScene) {
    TextArea loggingArea = (TextArea) primaryScene.lookup(LOGGING_TEXT_AREA_NAME);
    if (loggingArea == null) {
      logger.error("Unable to load logging text area");
      return;
    }
    TextAreaConsole textAreaConsole = new TextAreaConsole(loggingArea);
    PrintStream ps = new PrintStream(textAreaConsole, true);
    System.setOut(ps);
    System.setErr(ps);
    logger.info("Ready to start demo app.");
  }
}
