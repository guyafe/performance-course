package il.co.site_building.performance_course.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

/**
 * JGraphT based implementation.
 * Delegates all functionality to JGraphT's.
 */
public class JGraphSimpleGraphImpl implements SimpleGraph {

  private final Graph<Integer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);

  @Override public void addVertex(int vertex) {
    graph.addVertex(vertex);
  }

  @Override public void removeVertex(int vertex) {
    graph.removeVertex(vertex);
  }

  @Override public boolean vertexExists(int vertex) {
    return graph.containsVertex(vertex);
  }

  @Override public void addEdge(int v1, int v2) {
    graph.addEdge(v1, v2);
  }

  @Override public void removeEdge(int v1, int v2) {
    graph.removeEdge(v1, v2);
  }

  @Override public boolean edgeExists(int v1, int v2) {
    return graph.containsEdge(v1, v2);
  }
}
