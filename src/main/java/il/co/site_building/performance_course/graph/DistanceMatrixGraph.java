package il.co.site_building.performance_course.graph;

/**
 * Distance matrix based graph.
 * The neighbors' matrix is based on bit level matrix;
 */
public class DistanceMatrixGraph implements SimpleGraph{

  private long[] vertices; //Bitwise array of all vertices. 1 in the relevant position indicates that the vertex exist.
  private long[][] neighborsMatrix; //Bitwise neighbors matrix implementation. Assuming v1 < v2: 1 in the relevant position indicates that an edge exists.

  /**
   * Initializes an empty distance matrix with minimum of 8 entries
    */
  public DistanceMatrixGraph(){
    vertices = new long[1];
    neighborsMatrix = new long[Long.SIZE][1];
  }

  /**
   * Initialize a graph with distance enough entries, and all edges exist in the range of [0..numberOfVertices)
   * @param numberOfVertices All existing vertices in the graph
   */
  public DistanceMatrixGraph(int numberOfVertices){
    int lastBucketEntry = getBucketEntry(numberOfVertices);
    int lastBucketOffset = getBucketOffset(numberOfVertices);
    buildVertices(lastBucketEntry, lastBucketOffset);
    buildMatrix(lastBucketEntry, lastBucketOffset);
  }

  private void buildMatrix(int lastBucketEntry, int lastBucketOffset) {
    neighborsMatrix = new long[lastBucketEntry * Long.SIZE + lastBucketOffset][lastBucketEntry + 1];
  }

  private void buildVertices(int lastBucketEntry, int lastBucketOffset) {
    vertices = new long[lastBucketEntry + 1];
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

  }

  @Override public void removeVertex(int vertex) {

  }

  @Override public boolean vertexExists(int vertex) {
    return false;
  }

  @Override public void addEdge(int v1, int v2) {

  }

  @Override public void removeEdge(int v1, int v2) {

  }

  @Override public boolean edgeExists(int v1, int v2) {
    return false;
  }
}
