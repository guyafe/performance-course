package il.co.site_building.performance_course.ui.controllers;

public class ConnectedComponentsResultRecord {

  private final String header;
  private final double jgraphBuildResult;
  private final double jgraphCalculationResult;
  private final double neighborsMatrixBuildResult;
  private final double neighborsMatrixCalculationResult;

  public ConnectedComponentsResultRecord(String header,
                                         double jgraphBuildResult,
                                         double jgraphCalculationResult,
                                         double neighborsMatrixBuildResult,
                                         double neighborsMatrixCalculationResult) {
    this.header = header;
    this.jgraphBuildResult = jgraphBuildResult;
    this.jgraphCalculationResult = jgraphCalculationResult;
    this.neighborsMatrixBuildResult = neighborsMatrixBuildResult;
    this.neighborsMatrixCalculationResult = neighborsMatrixCalculationResult;
  }

  public String getHeader() {
    return header;
  }

  public double getJgraphBuildResult() {
    return jgraphBuildResult;
  }

  public double getJgraphCalculationResult() {
    return jgraphCalculationResult;
  }

  public double getNeighborsMatrixBuildResult() {
    return neighborsMatrixBuildResult;
  }

  public double getNeighborsMatrixCalculationResult() {
    return neighborsMatrixCalculationResult;
  }
}
