package il.co.site_building.performance_course.graph;

import java.util.Collection;
import java.util.Set;

/**
 * Simple Unweighted and Undirected Graph
 * Allows adding and removing vertices, adding and removing edges, checking if two vertices are connected, and creating connected components graphs.
 * Vertices are represented by simple integers.
 */
public interface SimpleGraph {

  /**
   * Adds a vertex. Does nothing if the vertex exist.
   * @param vertex The vertex to add
   */
  void addVertex(int vertex);

  /**
   * Removes a vertex. Does nothing if the vertex doesn't exist.
   * @param vertex The vertex to remove.
   */
  void removeVertex(int vertex);

  /**
   * Checks if a vertex exist
   * @param vertex The vertex to look for.
   * @return True if the vertex exist, false otherwise.
   */
  boolean vertexExists(int vertex);

  /**
   * Adds an edge between two vertices. Does nothing if the edge already exist.
   * @param v1 The first vertex
   * @param v2 The second vertex
   */
  void addEdge(int v1, int v2);

  /**
   * Removes an edge between two vertices. Does nothing if the edge doesn't exist.
   * @param v1 The first vertex.
   * @param v2 The second vertex.
   */
  void removeEdge(int v1, int v2);

  /**
   * Checks if an edge exists between two vertices
   * @param v1 First vertex
   * @param v2 Second vertex
   * @return True if these vertices are connected, false otherwise.
   */
  boolean edgeExists(int v1, int v2);

  /**
   * Creates all connected components sub graphs of this graph.
   * @return A collection of the connected components.
   */
  Collection<SimpleGraph> createConnectedComponents();

  Set<Integer> vertexSet();
}
