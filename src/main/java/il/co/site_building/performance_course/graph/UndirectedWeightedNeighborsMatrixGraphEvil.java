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
    int sourceBucketEntry = source / Long.SIZE;
    long sourceMask = 1L << (source % Long.SIZE);
    shortestPathSet[sourceBucketEntry] |= sourceMask;
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
          neighborExists = ((vertices[neighborBucketEntry] & neighborMask) != 0);
        }
        boolean neighborInShortestPathSet;
        long mask = 1L << neighbor % Long.SIZE;
        int bucketEntry = neighbor / Long.SIZE;
        neighborInShortestPathSet = ((shortestPathSet[bucketEntry] & mask) != 0);
        if (neighbor != currentNeighbor && neighborExists && !neighborInShortestPathSet) {
          double distance;
          boolean currentNeighborExists2;
          if (currentNeighbor > maxVertex) {
            currentNeighborExists2 = false;
          } else {
            long currentNeighborMask2 = 1L << (currentNeighbor % Long.SIZE);
            int currentNeighborBucketEntry2 = currentNeighbor / Long.SIZE;
            currentNeighborExists2 = ((vertices[currentNeighborBucketEntry2] & currentNeighborMask2) != 0);
          }
          boolean neighborExists2;
          if (neighbor > maxVertex) {
            neighborExists2 = false;
          } else {
            long neighborMask2 = 1L << (neighbor % Long.SIZE);
            int neighborBucketEntry2 = neighbor / Long.SIZE;
            neighborExists2 = ((vertices[neighborBucketEntry2] & neighborMask2) != 0);
          }
          if (!currentNeighborExists2 || !neighborExists2) {
            distance = Double.NaN;
          } else if (currentNeighbor == neighbor) {
            distance = 0;
          } else {
            int tempNeighbor = neighbor;
            int tempCurrentNeighbor = currentNeighbor;
            if (tempCurrentNeighbor > tempNeighbor) {
              int vTemp = tempCurrentNeighbor;
              tempCurrentNeighbor = tempNeighbor;
              tempNeighbor = vTemp;
            }
            distance = neighborsMatrix[tempCurrentNeighbor][tempNeighbor];
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
          containsEdge = false;
        } else {
          int tempCurrentNeighbor = currentNeighbor;
          int tempNeighbor = neighbor;
          if (tempCurrentNeighbor > tempNeighbor) {
            int vTemp = tempCurrentNeighbor;
            tempCurrentNeighbor = tempNeighbor;
            tempNeighbor = vTemp;
          }
          containsEdge = neighborsMatrix[tempCurrentNeighbor][tempNeighbor] < Double.POSITIVE_INFINITY;
        }
        boolean neighborInShortestPathSet;
        long mask = 1L << neighbor % Long.SIZE;
        int bucketEntry = neighbor / Long.SIZE;
        neighborInShortestPathSet = ((shortestPathSet[bucketEntry] & mask) != 0);
        if (neighborExists && containsEdge && !neighborInShortestPathSet) {
          double edgeWeight;
          boolean currentNeighborExists2;
          if (currentNeighbor > maxVertex) {
            currentNeighborExists2 = false;
          } else {
            long currentNeighborMask2 = 1L << (currentNeighbor % Long.SIZE);
            int currentNeighborBucketEntry2 = currentNeighbor / Long.SIZE;
            currentNeighborExists2 = ((vertices[currentNeighborBucketEntry2] & currentNeighborMask2) != 0);
          }
          boolean neighborExists2;
          if (neighbor > maxVertex) {
            neighborExists2 = false;
          } else {
            long neighborMask2 = 1L << (neighbor % Long.SIZE);
            int neighborBucketEntry2 = neighbor / Long.SIZE;
            neighborExists2 = ((vertices[neighborBucketEntry2] & neighborMask2) != 0);
          }
          if (!currentNeighborExists2 || !neighborExists2) {
            edgeWeight = Double.NaN;
          } else if (currentNeighbor == neighbor) {
            edgeWeight = 0;
          } else {
            int tempNeighbor = neighbor;
            int tempCurrentNeighbor = currentNeighbor;
            if (tempCurrentNeighbor > tempNeighbor) {
              int vTemp = tempCurrentNeighbor;
              tempCurrentNeighbor = tempNeighbor;
              tempNeighbor = vTemp;
            }
            edgeWeight = neighborsMatrix[tempCurrentNeighbor][tempNeighbor];
          }
          double alternativeDistance = distances[currentNeighbor] + edgeWeight;
          if (alternativeDistance < distances[neighbor]) {
            distances[neighbor] = alternativeDistance;
            previousNodes[neighbor] = currentNeighbor;
          }
        }
      }
      int currentNeighborBucketEntry = currentNeighbor / Long.SIZE;
      long currentNeighborMask = 1L << (currentNeighbor % Long.SIZE);
      shortestPathSet[currentNeighborBucketEntry] |= currentNeighborMask;
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
