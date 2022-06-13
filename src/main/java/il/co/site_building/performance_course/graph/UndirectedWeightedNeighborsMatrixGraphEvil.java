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
    int bucketEntry = source / Long.SIZE;
    long mask = 1L << (source % Long.SIZE);
    shortestPathSet[bucketEntry] |= mask;
    distances[source] = 0.0;
    previousNodes[source] = source;
    int currentNeighbor = source;
    while (true) {
      int closestNeighbor = NOT_FOUND;
      double minDistance = Double.POSITIVE_INFINITY;
      for (int neighbor = 0; neighbor < distances.length; neighbor++) {
        boolean neighborExists;
        if (neighbor > maxVertex) {
          neighborExists = false;
        } else {
          long neighborMask = 1L << (neighbor % Long.SIZE);
          int neighborBucketEntry = neighbor / Long.SIZE;
          neighborExists = (vertices[neighborBucketEntry] & neighborMask) != 0;
        }
        boolean neighborInShortestPathSet;
        long neighborMask = 1L << (neighbor % Long.SIZE);
        int neighborBucketEntry = neighbor / Long.SIZE;
        neighborInShortestPathSet = (shortestPathSet[neighborBucketEntry] & neighborMask) != 0;
        if (neighbor != currentNeighbor && neighborExists && !neighborInShortestPathSet) {
          double distance;
          boolean currentNeighborExists;
          if (currentNeighbor > maxVertex) {
            currentNeighborExists = false;
          } else {
            long currentNeighborMask = 1L << (currentNeighbor % Long.SIZE);
            int currentNeighborBucketEntry = currentNeighbor / Long.SIZE;
            currentNeighborExists = ((vertices[currentNeighborBucketEntry] & currentNeighborMask) != 0);
          }
          boolean neighborExists2;
          if (neighbor > maxVertex) {
            neighborExists2 = false;
          } else {
            long neighborMask2 = 1L << (neighbor % Long.SIZE);
            int neighborBucketEntry2 = neighbor / Long.SIZE;
            neighborExists2 = ((vertices[neighborBucketEntry2] & neighborMask2) != 0);
          }
          if (!currentNeighborExists || !neighborExists2) {
            distance = Double.NaN;
          } else if (currentNeighbor == neighbor) {
            distance = 0;
          } else {
            if (currentNeighbor > neighbor) {
              int vTemp = currentNeighbor;
              currentNeighbor = neighbor;
              neighbor = vTemp;
            }
            distance = neighborsMatrix[currentNeighbor][neighbor];
          }
          if (distance < minDistance) {
            minDistance = distance;
            closestNeighbor = neighbor;
          }
        }
      }
      if (closestNeighbor == NOT_FOUND) {
        break;
      }
      for (int neighbor = 0; neighbor < maxVertex + 1; neighbor++) {
        boolean neighborExists;
        if (neighbor > maxVertex) {
          neighborExists = false;
        } else {
          long neighborMask = 1L << (neighbor % Long.SIZE);
          int neighborBucketEntry = neighbor / Long.SIZE;
          neighborExists = ((vertices[neighborBucketEntry] & neighborMask) != 0);
        }
        boolean containsEdge;
        boolean currentNeighborExists;
        if (currentNeighbor > maxVertex) {
          currentNeighborExists = false;
        } else {
          long currentNeighborMask = 1L << (currentNeighbor % Long.SIZE);
          int currentNeighborBucketEntry = currentNeighbor / Long.SIZE;
          currentNeighborExists = ((vertices[currentNeighborBucketEntry] & currentNeighborMask) != 0);
        }
        boolean neighborExists2;
        if (neighbor > maxVertex) {
          neighborExists2 = false;
        } else {
          long neighborMask2 = 1L << (neighbor % Long.SIZE);
          int neighborBucketEntry2 = neighbor / Long.SIZE;
          neighborExists2 = (vertices[neighborBucketEntry2] & neighborMask2) != 0;
        }
        if (!currentNeighborExists || !neighborExists2) {
          containsEdge = false;
        } else {
          if (currentNeighbor > neighbor) {
            int vTemp = currentNeighbor;
            currentNeighbor = neighbor;
            neighbor = vTemp;
          }
          containsEdge = neighborsMatrix[currentNeighbor][neighbor] < Double.POSITIVE_INFINITY;
        }
        boolean neighborInShortestPathSet;
        long neighborMask = 1L << (neighbor % Long.SIZE);
        int neighborBucketEntry = neighbor / Long.SIZE;
        neighborInShortestPathSet = (shortestPathSet[neighborBucketEntry] & neighborMask) != 0;
        if (neighborExists && containsEdge && !neighborInShortestPathSet) {
          double alternativeDistance;
          boolean currentNeighborExists3;
          if (currentNeighbor > maxVertex) {
            currentNeighborExists3 = false;
          } else {
            long currentNeighborMask3 = 1L << (currentNeighbor % Long.SIZE);
            int currentNeighborBucketEntry3 = currentNeighbor / Long.SIZE;
            currentNeighborExists3 = ((vertices[currentNeighborBucketEntry3] & currentNeighborMask3) != 0);
          }
          boolean neighborExists3;
          if (neighbor > maxVertex) {
            neighborExists3 = false;
          } else {
            long neighborMask3 = 1L << (neighbor % Long.SIZE);
            int neighborBucketEntry3 = neighbor / Long.SIZE;
            neighborExists3 = ((vertices[neighborBucketEntry3] & neighborMask3) != 0);
          }
          if (!currentNeighborExists3 || !neighborExists3) {
            alternativeDistance = Double.NaN;
          } else if (currentNeighbor == neighbor) {
            alternativeDistance = 0;
          } else {
            if (currentNeighbor > neighbor) {
              int vTemp = currentNeighbor;
              currentNeighbor = neighbor;
              neighbor = vTemp;
            }
            alternativeDistance = neighborsMatrix[currentNeighbor][neighbor];
          }
          if (alternativeDistance < distances[neighbor]) {
            distances[neighbor] = alternativeDistance;
            previousNodes[neighbor] = currentNeighbor;
          }
        }
      }
      int bucketEntry4 = currentNeighbor / Long.SIZE;
      long mask4 = 1L << (currentNeighbor % Long.SIZE);
      shortestPathSet[bucketEntry4] |= mask4;
      if (closestNeighbor == dest) {
        break;
      }
      currentNeighbor = closestNeighbor;
    }
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

  /**
   * Creates a random graph with the given size and load factor. The edges' weights are unigormally distributed between 0.0-1.0.
   *
   * @param random           Random generator
   * @param numberOfVertices Size of the graph
   * @param loadFactor       Load factor. Represents the probability of having an edge between two vertices.
   * @return The generated graph.
   */
  public static UndirectedWeightedNeighborsMatrixGraphEvil generateRandomGraph(Random random,
                                                                               int numberOfVertices,
                                                                               double loadFactor) {
    UndirectedWeightedNeighborsMatrixGraphEvil graph = new UndirectedWeightedNeighborsMatrixGraphEvil(numberOfVertices);
    for (int source = 0; source < numberOfVertices; source++) {
      for (int dest = source + 1; dest < numberOfVertices; dest++) {
        if (random.nextDouble() < loadFactor) {
          graph.setEdge(source, dest, random.nextDouble());
        }
      }
    }
    return graph;
  }
}
