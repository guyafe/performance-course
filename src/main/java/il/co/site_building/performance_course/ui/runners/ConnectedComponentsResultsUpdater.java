package il.co.site_building.performance_course.ui.runners;

import il.co.site_building.performance_course.graph.data_structures.ConnectedComponentsBenchmarkStatistics;
import il.co.site_building.performance_course.ui.controllers.PerformanceCourseUiController;

import java.util.concurrent.Future;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectedComponentsResultsUpdater implements Runnable {
  private final Logger logger = LogManager.getRootLogger();

  private final Future<ConnectedComponentsBenchmarkStatistics> future;
  private final PerformanceCourseUiController controller;

  public ConnectedComponentsResultsUpdater(Future<ConnectedComponentsBenchmarkStatistics> future,
                                           PerformanceCourseUiController controller) {
    this.future = future;
    this.controller = controller;
  }

  @Override public void run() {
    ConnectedComponentsBenchmarkStatistics statistics = null;
    try {
      statistics = future.get();
      if(statistics == null){
        return;
      }
    } catch (Exception e) {
      logger.error("Unable to finish algorithm");
      logger.error(ExceptionUtils.getStackTrace(e));
    }
    controller.updateTable(statistics);
  }
}
