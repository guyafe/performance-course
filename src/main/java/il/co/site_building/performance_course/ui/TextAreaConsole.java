package il.co.site_building.performance_course.ui;

import java.io.OutputStream;

import javax.annotation.Nonnull;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class TextAreaConsole extends OutputStream {

  private final TextArea textArea;

  public TextAreaConsole(TextArea textArea) {
    this.textArea = textArea;
  }

  @Override
  public void write(int b) {
    Platform.runLater(() -> textArea.appendText(String.valueOf((char) b)));
  }

  @Override
  public void write(@Nonnull byte[] b) {
    String s = new String(b);
    Platform.runLater(() -> textArea.appendText(s));
  }

  @Override
  public void write(@Nonnull byte[] b, int off, int len) {
    String s = new String(b, off, len);
    Platform.runLater(() -> textArea.appendText(s));
  }
}
