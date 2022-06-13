package il.co.site_building.performance_course.graph;

import java.util.Arrays;

/**
 * A class representing an undirected and unweighted graph as a neighbors matrix graph.
 * Each edge is an integer number, and a negative value means that it doesn't exist.
 */
public abstract class UndirectedWeightedNeighborsMatrixGraph {

  protected long[] vertices;
      //Bitwise array of all vertices. 1 in the relevant position indicates that the vertex exist.
  protected double[][] neighborsMatrix;
  //Neighbors matrix implementation. Each value indicates the weight of an edge. Negative value indicates that the edge doesn't exist.
  protected int maxVertex;

  /**
   * Initializes an empty distance matrix with minimum of 1 entry
   */
  protected UndirectedWeightedNeighborsMatrixGraph() {
    vertices = new long[0];
    neighborsMatrix = new double[0][0];
    maxVertex = -1;
  }

  /**
   * Initialize a graph with distance enough entries, and all edges exist in the range of [0..numberOfVertices)
   *
   * @param numberOfVertices All existing vertices in the graph
   */
  protected UndirectedWeightedNeighborsMatrixGraph(int numberOfVertices) {
    int lastBucketEntry = getBucketEntry(numberOfVertices);
    int lastBucketOffset = getBucketOffset(numberOfVertices);
    buildVertices(lastBucketEntry, lastBucketOffset);
    neighborsMatrix = new double[numberOfVertices][numberOfVertices];
    for (int row = 0; row < numberOfVertices; row++) {
      Arrays.fill(neighborsMatrix[row], Double.POSITIVE_INFINITY);
    }
    maxVertex = numberOfVertices;
  }

  public void addVertex(int vertex) {
    verifyEntryExists(vertex);
    addVertex(vertex, vertices);
  }

  protected void addVertex(int vertex, long[] array) {
    int bucketEntry = getBucketEntry(vertex);
    long mask = createMask(vertex);
    array[bucketEntry] |= mask;
  }

  public void removeVertex(int vertex) {
    if (!vertexExists(vertex)) {
      return; //Nothing to do
    }
    int bucketEntry = getBucketEntry(vertex);
    long mask = createMask(vertex);
    mask = ~mask; //Negating to remove the vertex
    vertices[bucketEntry] &= mask;
  }

  public void setEdge(int v1, int v2, double weight) {
    if (!vertexExists(v1) || !vertexExists(v2)) {
      return;
    }
    if (v1 == v2) {
      return; //An edge is always 0 distance from itself.
    }
    if (0 <= weight) {
      if (v1 > v2) {
        int vTemp = v1;
        v1 = v2;
        v2 = vTemp;
      }
      neighborsMatrix[v1][v2] = weight;
    }
  }

  public abstract PathResult findShortestPath(int source, int dest);

  public boolean containsEdge(int v1, int v2) {
    if (!vertexExists(v1) || !vertexExists(v2)) {
      return false;
    }
    if (v1 > v2) {
      int vTemp = v1;
      v1 = v2;
      v2 = vTemp;
    }
    return neighborsMatrix[v1][v2] < Double.POSITIVE_INFINITY;
  }

  public double getEdgeWeight(int v1, int v2) {
    if (!vertexExists(v1) || !vertexExists(v2)) {
      return Double.NaN;
    }
    if (v1 == v2) {
      return 0;
    }
    if (v1 > v2) {
      int vTemp = v1;
      v1 = v2;
      v2 = vTemp;
    }
    return neighborsMatrix[v1][v2];
  }

  public boolean vertexExists(int vertex) {
    if (vertex > maxVertex) {
      return false;
    } else {
      return vertexInArray(vertex, vertices);
    }
  }

  protected boolean vertexInArray(int vertex, long[] array) {
    long mask = createMask(vertex);
    int bucketEntry = getBucketEntry(vertex);
    return (array[bucketEntry] & mask) != 0;
  }

  private void verifyEntryExists(int vertex) {
    if (vertex > maxVertex) {
      int bucketEntry = getBucketEntry(vertex);
      increaseVerticesArray(bucketEntry);
      increaseNeighborsMatrix(vertex);
      maxVertex = vertex;
    }
  }

  private void increaseNeighborsMatrix(int vertex) {
    double[][] newNeighborsMatrix = new double[vertex + 1][vertex + 1];
    for (int row = 0; row < vertex + 1; row++) {
      for (int column = row + 1; column < vertex + 1; column++) {
        newNeighborsMatrix[row][column] = Double.POSITIVE_INFINITY;
      }
    }
    for (int rowIndex = 0; rowIndex < neighborsMatrix.length; rowIndex++) {
      System.arraycopy(neighborsMatrix[rowIndex],
                       0,
                       newNeighborsMatrix[rowIndex],
                       0,
                       neighborsMatrix[rowIndex].length);
    }
    neighborsMatrix = newNeighborsMatrix;
  }

  private void increaseVerticesArray(int bucketEntry) {
    long[] newVerticesArray = new long[bucketEntry + 1];
    System.arraycopy(vertices, 0, newVerticesArray, 0, vertices.length);
    vertices = newVerticesArray;
  }

  private long createMask(int vertex) {
    return 1L << getBucketOffset(vertex);
  }

  private int getBucketOffset(int vertex) {
    return vertex % Long.SIZE;
  }

  private int getBucketEntry(int vertex) {
    return vertex / Long.SIZE;
  }

  private void buildVertices(int lastBucketEntry, int lastBucketOffset) {
    vertices = new long[lastBucketEntry + 1];
    for (int entry = 0; entry < lastBucketEntry; entry++) {
      vertices[entry] = -1L; //Filling with 1s
    }
    vertices[lastBucketEntry] = ~(-1L << lastBucketOffset);
  }
}
