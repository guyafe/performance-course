package il.co.site_building.performance_course.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Distance matrix based graph.
 * The neighbors' matrix is based on bit level matrix;
 */
public class NeighborsMatrixGraph implements SimpleGraph {

  private long[] vertices; //Bitwise array of all vertices. 1 in the relevant position indicates that the vertex exist.
  private long[][] neighborsMatrix;
  //Bitwise neighbors matrix implementation. 1 in the relevant position indicates that an edge exists.
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
    maxVertex = numberOfVertices;
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
      maxVertex = vertex;
    }
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
    if (!vertexExists(vertex)) {
      return; //Nothing to do
    }
    int bucketEntry = getBucketEntry(vertex);
    long mask = createMask(vertex);
    mask = ~mask; //Negating to remove the vertex
    vertices[bucketEntry] &= mask;
  }

  @Override public boolean vertexExists(int vertex) {
    if (vertex > maxVertex) {
      return false;
    } else {
      long mask = createMask(vertex);
      int bucketEntry = getBucketEntry(vertex);
      return (vertices[bucketEntry] & mask) != 0;
    }
  }

  @Override public void addEdge(int v1, int v2) {
    handleEdge(v1, v2, ((rowIndex, bucketEntry, mask) -> {
      neighborsMatrix[rowIndex][bucketEntry] |= mask;
      return true;
    }));
  }

  @Override public void removeEdge(int v1, int v2) {
    handleEdge(v1, v2, ((rowIndex, bucketEntry, mask) -> {
      neighborsMatrix[rowIndex][bucketEntry] &= ~mask;
      return true;
    }));
  }

  private boolean handleEdge(int v1, int v2, EntryMarker entryMarker) {
    if (!vertexExists(v1) || !vertexExists(v2)) {
      return false; //No vertices
    }
    return addEdgeInternal(v1, v2, entryMarker);
  }

  private boolean addEdgeInternal(int v1, int v2, EntryMarker entryMarker) {
    if (v2 >= v1) {
      int vTemp = v1;
      v1 = v2;
      v2 = vTemp;
    }
    int bucketEntry = getBucketEntry(v2);
    long mask = createMask(v2);
    return entryMarker.markEntry(v1, bucketEntry, mask);
  }

  @Override public boolean edgeExists(int v1, int v2) {
    if (v1 == v2) {
      return true;
    }
    return handleEdge(v1, v2, (rowIndex, bucketEntry, mask) -> (neighborsMatrix[rowIndex][bucketEntry] & mask) != 0);
  }

  @Override public Collection<SimpleGraph> createConnectedComponents() {
    Collection<SimpleGraph> connectedComponents = new HashSet<>();
    long[] visited = new long[vertices.length]; // A bit wise boolean visited array used for DFS traversal
    int firstExistingVertex = 0;
    for (int nextVertex = firstExistingVertex; nextVertex < maxVertex; nextVertex++) {
      if (!vertexDiscovered(nextVertex, visited)) {
        SimpleGraph connectedComponent = runDfs(nextVertex, visited);
        connectedComponents.add(connectedComponent);
      }
    }
    return connectedComponents;
  }

  @Override public Set<Integer> vertexSet() {
    Set<Integer> vertexSet = new HashSet<>();
    for (int vertex = 0; vertex < maxVertex; vertex++) {
      if (vertexExists(vertex)) {
        vertexSet.add(vertex);
      }
    }
    return vertexSet;
  }

  @Override public void randomizeEdges(Random random, int maxVertex, double loadFactor) {
    for (int vertex = 0; vertex < maxVertex; vertex++) {
      for (int neighbor = vertex; neighbor < maxVertex; neighbor++) {
        double lucky = random.nextDouble();
        if (lucky <= loadFactor) {
          addEdge(vertex, neighbor);
        }
      }
    }
  }

  private boolean vertexDiscovered(int vertex, long[] bitSet) {
    long mask = createMask(vertex);
    int bucketEntry = getBucketEntry(vertex);
    return (bitSet[bucketEntry] & mask) != 0;
  }

  private void setVertexDiscovered(int vertex, long[] bitSet) {
    long mask = createMask(vertex);
    int bucketEntry = getBucketEntry(vertex);
    bitSet[bucketEntry] |= mask;
  }

  private SimpleGraph runDfs(int firstVertex, long[] visited) {
    SimpleGraph simpleGraph = new NeighborsMatrixGraph();
    IntStack verticesStack = new IntStack(maxVertex);
    verticesStack.push(firstVertex);
    while (!verticesStack.isEmpty()) {
      int vertex = verticesStack.pop();
      long mask = createMask(vertex);
      int bucketEntry = getBucketEntry(vertex);
      boolean vertexDiscovered = (vertices[bucketEntry] & mask) != 0;
      if (vertexDiscovered) {
        continue;
      }
      setVertexDiscovered(vertex, visited);
      for (int neighbor = 0; neighbor < maxVertex; neighbor++) {
        if (edgeExists(vertex, neighbor) && !vertexDiscovered(neighbor, visited)) {
          verticesStack.push(neighbor);
          simpleGraph.addEdge(vertex, neighbor);
        }
      }
    }
    return simpleGraph;
  }

  private long createMask(int vertex) {
    return 1L << getBucketOffset(vertex);
  }

  private interface EntryMarker {
    boolean markEntry(int rowIndex, int bucketEntry, long mask);
  }

  public boolean equalsJGraphImpl(JGraphSimpleGraphImpl other) {
    Set<Integer> otherVertexSet = other.vertexSet();
    if (!otherVertexSet.equals(vertexSet())) {
      return false;
    }
    for (int vertex = 0; vertex < maxVertex; vertex++) {
      for (int neighbor = 0; neighbor < maxVertex; neighbor++) {
        if (vertexExists(vertex) && vertexExists(neighbor)) {
          if (edgeExists(vertex, neighbor) && !other.edgeExists(vertex, neighbor)) {
            return false;
          } else if (!edgeExists(vertex, neighbor) && other.edgeExists(vertex, neighbor)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * A stack of integers
   */
  private static class IntStack {

    private static final int INITIAL_SIZE = 8192;
    int[] stack;
    int nextEntry;

    public IntStack(int size) {
      int actualSize = size;
      if (size == 0) {
        actualSize = 1;
      }
      stack = new int[actualSize];
      nextEntry = 0;
    }

    public IntStack() {
      this(INITIAL_SIZE);
    }

    private void push(int value) {
      if (nextEntry == stack.length) {
        int[] newStack = new int[stack.length];
        System.arraycopy(stack, 0, newStack, 0, stack.length);
        stack = newStack;
      }
      stack[nextEntry] = value;
      nextEntry++;
    }

    private int pop() {
      nextEntry--;
      return stack[nextEntry];
    }

    private boolean isEmpty() {
      return nextEntry == 0;
    }
  }

  public static NeighborsMatrixGraph createRandomGraph(Random random, int numberOfVertices, double loadFactor) {
    NeighborsMatrixGraph graph = new NeighborsMatrixGraph(numberOfVertices);
    graph.randomizeEdges(random, numberOfVertices, loadFactor);
    return graph;
  }

}
