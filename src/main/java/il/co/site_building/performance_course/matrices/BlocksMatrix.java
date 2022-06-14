package il.co.site_building.performance_course.matrices;


/**
 * Matrix represented by blocks. Used as cache friendly matrix for multiplication operations.
 */
public class BlocksMatrix {

  private final double[][][][] blocks;
  private final int blockSize;
  private final int rows;
  private final int columns;

  public BlocksMatrix(int rows, int columns, int blockSize) {
    int blockRows = rows / blockSize;
    if (rows % blockSize != 0) {
      blockRows++;
    }
    int blockColumns = columns / blockSize;
    if (columns % blockSize != 0) {
      blockColumns++;
    }
    blocks = new double[blockRows][blockColumns][blockSize][blockSize];
    this.rows = rows;
    this.columns = columns;
    this.blockSize = blockSize;
  }

  public double get(int row, int column) {
    return blocks[row / blockSize][column / blockSize][row % blockSize][column % blockSize];
  }

  public void set(int row, int column, double value) {
    blocks[row / blockSize][column / blockSize][row % blockSize][column % blockSize] = value;
  }

  public int rows() {
    return rows;
  }

  public int columns() {
    return columns;
  }

  public BlocksMatrix multiply(BlocksMatrix other) {
    if (columns != other.rows) {
      throw new IllegalArgumentException("Matrices must match dimensions.");
    }
    if (blockSize != other.blockSize) {
      throw new IllegalArgumentException("Matrices must have same block size.");
    }
    return multiplyInternal(other);
  }

  private BlocksMatrix multiplyInternal(BlocksMatrix other) {
    BlocksMatrix result = new BlocksMatrix(rows, other.columns, blockSize);
    int blockRows = blocks.length;
    int blockColumns = other.blocks[0].length;
    int blockItems = other.blocks.length;
    for (int blockRow = 0; blockRow < blockRows; blockRow++) {
      for (int blockColumn = 0; blockColumn < blockColumns; blockColumn++) {
        for (int blockItem = 0; blockItem < blockItems; blockItem++) {
          multiplyAndAddBlock(result, other, blockRow, blockColumn, blockItem);
        }
      }
    }
    return result;
  }

  private void multiplyAndAddBlock(BlocksMatrix result,
                                   BlocksMatrix other,
                                   int blockRow,
                                   int blockColumn,
                                   int blockItem) {
    double[][] thisBlock = this.blocks[blockRow][blockItem];
    double[][] otherBlock = other.blocks[blockItem][blockColumn];
    double[][] resultBlock = result.blocks[blockRow][blockColumn];
    for (int row = 0; row < blockSize; row++) {
      for (int column = 0; column < blockSize; column++) {
        for (int item = 0; item < blockSize; item++) {
          resultBlock[row][column] += thisBlock[row][item] * otherBlock[item][column];
        }
      }
    }
  }
}
