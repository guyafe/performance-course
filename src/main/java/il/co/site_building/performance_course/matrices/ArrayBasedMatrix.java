package il.co.site_building.performance_course.matrices;

public class ArrayBasedMatrix {

  private final double[][] matrix;

  public ArrayBasedMatrix(int rows, int columns){
    matrix = new double[rows][columns];
  }

  public ArrayBasedMatrix(double[][] matrix){
    this.matrix = matrix;
  }

  public double get(int row, int column){
    return matrix[row][column];
  }

  public void set(int row, int column, double value){
    matrix[row][column] = value;
  }

  public int rows(){
    return matrix.length;
  }

  public int columns(){
    return matrix[0].length;
  }

  public ArrayBasedMatrix multiply(ArrayBasedMatrix other){
    if(matrix[0].length != other.matrix.length){
      throw new IllegalArgumentException("Matrices must match in size.");
    }
    int rows = matrix.length;
    int columns = other.matrix[0].length;
    double[][] result = new double[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        double item = 0;
        for (int itemIndex = 0; itemIndex < other.matrix.length; itemIndex++) {
          item += matrix[row][itemIndex] * other.matrix[itemIndex][column];
        }
        result[row][column] = item;
      }
    }
    return new ArrayBasedMatrix(result);
  }

}
