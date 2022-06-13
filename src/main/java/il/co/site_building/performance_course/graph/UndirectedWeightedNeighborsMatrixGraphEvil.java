package il.co.site_building.performance_course.graph;

import java.util.Arrays;
import java.util.Random;

/**
 * Concrete implementation of a weighted undirected graph.
 * Implements the shortest path using Dijkstra algorithm, but with one long method.
 */
public class UndirectedWeightedNeighborsMatrixGraphEvil extends UndirectedWeightedNeighborsMatrixGraph {

  private static final int UNDEFINED = -1;
  private static final int NOT_FOUND = -1;

  public UndirectedWeightedNeighborsMatrixGraphEvil() {
    super();
  }

  public UndirectedWeightedNeighborsMatrixGraphEvil(int numberOfVertices) {
    super(numberOfVertices);
  }

  /**
   * Finds the shortest path using Dijkstra's algorithm.
   *
   * @param source Source vertex.
   * @param dest   Destination vertex.
   * @return The shortest path as an object of the path and its total length.
   */
  @Override public PathResult findShortestPath(int source, int dest) {
    //Initialization
    double[] distances = new double[maxVertex + 1]; //Initial distances
    Arrays.fill(distances, Double.POSITIVE_INFINITY);
    int[] previousNodes =
        new int[maxVertex + 1]; //previousNodes[j] is the node that comes before j in the shortest path from source
    Arrays.fill(previousNodes, UNDEFINED);
    long[] shortestPathSet = new long[maxVertex / Long.SIZE + 1];
    addVertex(source, shortestPathSet);
    distances[source] = 0.0;
    previousNodes[source] = source;
    int currentNeighbor = source;
    while (true) {
      int closestNeighbor = findClosestNeighbor(distances, shortestPathSet, currentNeighbor);
      if (closestNeighbor == NOT_FOUND) {
        break;
      }
      updateNeighborsDistances(currentNeighbor, distances, shortestPathSet, previousNodes);
      addVertex(currentNeighbor, shortestPathSet);
      if (closestNeighbor == dest) {
        break;
      }
      currentNeighbor = closestNeighbor;
    }
    return buildResult(source, dest, distances, previousNodes);
  }

  private PathResult buildResult(int source, int dest, double[] distances, int[] previousNodes) {
    PathResult result = new PathResult();
    result.distances = distances[dest];
    if (distances[dest] < Double.POSITIVE_INFINITY) {
      int neighbor = dest;
      while (neighbor != source) {
        result.path.add(neighbor);
        neighbor = previousNodes[neighbor];
      }
      result.path.add(source);
    }
    return result;
  }

  private void updateNeighborsDistances(int currentNeighbor, double[] distances, long[] shortestPathSet,
                                        int[] previousNodes) {
    for (int neighbor = 0; neighbor < maxVertex + 1; neighbor++) {
      if (vertexExists(neighbor) && containsEdge(currentNeighbor, neighbor) &&
          !vertexInArray(neighbor, shortestPathSet)) {
        double alternativeDistance = distances[currentNeighbor] + getEdgeWeight(neighbor, currentNeighbor);
        if (alternativeDistance < distances[neighbor]) {
          distances[neighbor] = alternativeDistance;
          previousNodes[neighbor] = currentNeighbor;
        }
      }
    }
  }

  private int findClosestNeighbor(double[] distances, long[] shortestPathSet, int currentNeighbor) {
    int closestNeighbor = NOT_FOUND;
    double minDistance = Double.POSITIVE_INFINITY;
    for (int neighbor = 0; neighbor < distances.length; neighbor++) {
      if (neighbor != currentNeighbor && vertexExists(neighbor) && !vertexInArray(neighbor, shortestPathSet)) {
        double distance = getEdgeWeight(currentNeighbor, neighbor);
        if (distance < minDistance) {
          minDistance = distance;
          closestNeighbor = neighbor;
        }
      }
    }
    return closestNeighbor;
  }

  /**
   * Creates a random graph with the given size and load factor. The edges' weights are unigormally distributed between 0.0-1.0.
   * @param random Random generator
   * @param numberOfVertices Size of the graph
   * @param loadFactor Load factor. Represents the probability of having an edge between two vertices.
   * @return The generated graph.
   */
  public static UndirectedWeightedNeighborsMatrixGraphEvil generateRandomGraph(Random random,
                                                                               int numberOfVertices,
                                                                               double loadFactor) {
    UndirectedWeightedNeighborsMatrixGraphEvil graph = new UndirectedWeightedNeighborsMatrixGraphEvil(numberOfVertices);
    for (int source = 0; source < numberOfVertices; source++) {
      for(int dest = source + 1; dest < numberOfVertices; dest++){
        if(random.nextDouble() < loadFactor){
          graph.setEdge(source, dest, random.nextDouble());
        }
      }
    }
    return graph;
  }
}
