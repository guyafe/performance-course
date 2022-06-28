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
    System.out.println("We are ready to start simulation...");
  }

}
