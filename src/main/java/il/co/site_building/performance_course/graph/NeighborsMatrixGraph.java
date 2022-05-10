package il.co.site_building.performance_course.graph;

/**
 * Distance matrix based graph.
 * The neighbors' matrix is based on bit level matrix;
 */
public class NeighborsMatrixGraph implements SimpleGraph {

  private long[] vertices; //Bitwise array of all vertices. 1 in the relevant position indicates that the vertex exist.
  private long[][] neighborsMatrix;
      //Bitwise neighbors matrix implementation. Assuming v1 < v2: 1 in the relevant position indicates that an edge exists.
  private int maxVertex;

  /**
   * Initializes an empty distance matrix with minimum of 8 entries
   */
  public NeighborsMatrixGraph() {
    vertices = new long[1];
    neighborsMatrix = new long[1][1];
    maxVertex = 0;
  }

  /**
   * Initialize a graph with distance enough entries, and all edges exist in the range of [0..numberOfVertices)
   *
   * @param numberOfVertices All existing vertices in the graph
   */
  public NeighborsMatrixGraph(int numberOfVertices) {
    int lastBucketEntry = getBucketEntry(numberOfVertices);
    int lastBucketOffset = getBucketOffset(numberOfVertices);
    buildVertices(lastBucketEntry, lastBucketOffset);
    buildMatrix(lastBucketEntry, lastBucketOffset);
    maxVertex = numberOfVertices - 1;
  }

  private void buildMatrix(int lastBucketEntry, int lastBucketOffset) {
    neighborsMatrix = new long[lastBucketEntry * Long.SIZE + lastBucketOffset][lastBucketEntry];
  }

  private void buildVertices(int lastBucketEntry, int lastBucketOffset) {
    vertices = new long[lastBucketEntry];
    for (int entry = 0; entry < lastBucketEntry; entry++) {
      vertices[entry] = -1L; //Filling with 1s
    }
    vertices[lastBucketEntry] = ~(-1L << lastBucketOffset);
  }

  private int getBucketOffset(int vertex) {
    return vertex % Long.SIZE;
  }

  private int getBucketEntry(int vertex) {
    return vertex / Long.SIZE;
  }

  @Override public void addVertex(int vertex) {
    verifyEntryExists(vertex);
    int bucketEntry = getBucketEntry(vertex);
    long mask = createMask(vertex);
    vertices[bucketEntry] |= mask;
  }

  private void verifyEntryExists(int vertex) {
    if (vertex > maxVertex) {
      int bucketEntry = getBucketEntry(vertex);
      int bucketOffset = getBucketOffset(vertex);
      increaseVerticesArray(bucketEntry);
      increaseNeighborsMatrix(bucketEntry, bucketOffset);
    }
    maxVertex = vertex;
  }

  private void increaseNeighborsMatrix(int bucketEntry, int bucketOffset) {
    long[][] newNeighborsMatrix = new long[bucketEntry * Long.SIZE + bucketOffset][bucketEntry + 1];
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

  @Override public void removeVertex(int vertex) {

  }

  @Override public boolean vertexExists(int vertex) {
    if(vertex > maxVertex){
      return false;
    } else{
      long mask = createMask(vertex);
      int bucketEntry = getBucketEntry(vertex);
      return (vertices[bucketEntry] & mask) != 0;
    }
  }

  @Override public void addEdge(int v1, int v2) {

  }

  @Override public void removeEdge(int v1, int v2) {

  }

  @Override public boolean edgeExists(int v1, int v2) {
    return false;
  }

  private long createMask(int vertex){
    return 1L << getBucketOffset(vertex);
  }
}
