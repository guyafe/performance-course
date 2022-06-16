package il.co.site_building.performance_course.matrices;


import java.util.Random;

/**
 * Matrix represented by blocks. Used as cache friendly matrix for multiplication operations.
 */
public class BlocksMatrix {

  private final double[][][][] blocks;
  private final int blockSize;
  private final int rows;
  private final int columns;
  private static final int UNROLLING_FACTOR = 16;

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
    assertDimensions(other);
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

  public BlocksMatrix multiplyUnrolling(BlocksMatrix other) {
    assertDimensions(other);
    BlocksMatrix result = new BlocksMatrix(rows, other.columns, blockSize);
    int blockRows = blocks.length;
    int blockColumns = other.blocks[0].length;
    int blockItems = other.blocks.length;
    for (int blockRow = 0; blockRow < blockRows; blockRow++) {
      for (int blockColumn = 0; blockColumn < blockColumns; blockColumn++) {
        multiplyBlockItem(other, result, blockItems, blockRow, blockColumn);
      }
    }
    return result;
  }

  private void multiplyBlockItem(BlocksMatrix other,
                                 BlocksMatrix result,
                                 int blockItems,
                                 int blockRow,
                                 int blockColumn) {
    int unrollingLoops = blockItems / UNROLLING_FACTOR;
    for (int loop = 0; loop < unrollingLoops; loop++) {
      int startBlockRow = loop * UNROLLING_FACTOR;
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 1);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 2);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 3);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 4);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 5);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 6);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 7);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 8);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 9);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 10);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 11);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 12);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 13);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 14);
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, startBlockRow + 15);
    }
    for (int blockItem = unrollingLoops * UNROLLING_FACTOR; blockItem < blockItems; blockItem++) {
      multiplyAndAddBlockUnrolling(result, other, blockRow, blockColumn, blockItem);
    }
  }

  private void assertDimensions(BlocksMatrix other) {
    if (columns != other.rows) {
      throw new IllegalArgumentException("Matrices must match dimensions.");
    }
    if (blockSize != other.blockSize) {
      throw new IllegalArgumentException("Matrices must have same block size.");
    }
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

  private void multiplyAndAddBlockUnrolling(BlocksMatrix result,
                                            BlocksMatrix other,
                                            int blockRow,
                                            int blockColumn,
                                            int blockItem) {
    double[][] thisBlock = this.blocks[blockRow][blockItem];
    double[][] otherBlock = other.blocks[blockItem][blockColumn];
    double[][] resultBlock = result.blocks[blockRow][blockColumn];
    int unrollingLoops = blockSize / UNROLLING_FACTOR;
    for (int loop = 0; loop < unrollingLoops; loop++) {
      int startRow = loop * UNROLLING_FACTOR;
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 1);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 2);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 3);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 4);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 5);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 6);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 7);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 8);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 9);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 10);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 11);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 12);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 13);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 14);
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, startRow + 15);
    }
    for (int row = unrollingLoops * UNROLLING_FACTOR; row < blockSize; row++) {
      columnProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row);
    }
  }

  private void columnProductUnrolled(double[][] thisBlock,
                                     double[][] otherBlock,
                                     double[][] resultBlock,
                                     int unrollingLoops,
                                     int row) {
    for (int loop = 0; loop < unrollingLoops; loop++) {
      int startColumn = loop * UNROLLING_FACTOR;
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 1);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 2);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 3);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 4);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 5);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 6);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 7);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 8);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 9);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 10);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 11);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 12);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 13);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 14);
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, startColumn + 15);
    }
    for (int column = unrollingLoops * UNROLLING_FACTOR; column < blockSize; column++) {
      scalarProductUnrolled(thisBlock, otherBlock, resultBlock, unrollingLoops, row, column);
    }
  }

  private void scalarProductUnrolled(double[][] thisBlock,
                                     double[][] otherBlock,
                                     double[][] resultBlock,
                                     int unrollingLoops,
                                     int row,
                                     int column) {
    for (int loop = 0; loop < unrollingLoops; loop++) {
      int loopStart = loop * UNROLLING_FACTOR;
      resultBlock[row][column] += thisBlock[row][loopStart] * otherBlock[loopStart][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 1] * otherBlock[loopStart + 1][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 2] * otherBlock[loopStart + 2][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 3] * otherBlock[loopStart + 3][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 4] * otherBlock[loopStart + 4][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 5] * otherBlock[loopStart + 5][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 6] * otherBlock[loopStart + 6][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 7] * otherBlock[loopStart + 7][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 8] * otherBlock[loopStart + 8][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 9] * otherBlock[loopStart + 9][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 10] * otherBlock[loopStart + 10][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 11] * otherBlock[loopStart + 11][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 12] * otherBlock[loopStart + 12][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 13] * otherBlock[loopStart + 13][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 14] * otherBlock[loopStart + 14][column];
      resultBlock[row][column] += thisBlock[row][loopStart + 15] * otherBlock[loopStart + 15][column];
    }
    for (int item = unrollingLoops * UNROLLING_FACTOR; item < blockSize; item++) {
      resultBlock[row][column] += thisBlock[row][item] * otherBlock[item][column];
    }
  }

  public static BlocksMatrix generateRandomMatrix(Random random, int size, int blockSize) {
    BlocksMatrix matrix = new BlocksMatrix(size, size, blockSize);
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        matrix.set(row, col, random.nextDouble());
      }
    }
    return matrix;
  }
}
