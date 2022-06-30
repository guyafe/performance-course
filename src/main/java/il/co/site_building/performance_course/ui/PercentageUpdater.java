package il.co.site_building.performance_course.ui;

import java.text.DecimalFormat;

import il.co.site_building.performance_course.ui.controllers.PerformanceCourseUiController;

public class PercentageUpdater {

  private double percentage = 0;
  private final double increase;
  private final PerformanceCourseUiController controller;

  public PercentageUpdater(double increase,
                           PerformanceCourseUiController controller) {
    this.increase = increase;
    this.controller = controller;
  }

  public void increase() {
    percentage += increase;
    updateProgressBar(percentage);

  }

  private void updateProgressBar(double percentage) {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    controller.updateProgressBar(percentage);
  }

  public void finish() {
    percentage = 100.0;
    updateProgressBar(percentage);
  }
}
