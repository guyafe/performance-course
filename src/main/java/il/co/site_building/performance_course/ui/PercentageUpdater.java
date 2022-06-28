package il.co.site_building.performance_course.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.text.DecimalFormat;

public class PercentageUpdater {

  private double percentage = 0;
  private final double increase;
  private final ProgressBar progressBar;
  private final Label percentageLabel;

  public PercentageUpdater(double increase, ProgressBar progressBar, Label percentageLabel) {
    this.increase = increase;
    this.progressBar = progressBar;
    this.percentageLabel = percentageLabel;
    progressBar.setProgress(0);
  }

  public void increase() {
    percentage += increase;
    updateProgressBar(percentage);

  }

  private void updateProgressBar(double percentage) {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    Platform.runLater(() -> {
                        progressBar.setProgress(percentage / 100.0);
                        percentageLabel.setText(df.format(percentage) + "%");
                      }
    );
  }

  public void finish() {
    percentage = 100.0;
    updateProgressBar(percentage);
  }
}
